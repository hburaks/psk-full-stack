package com.hburak_dev.psk_full_stack.blog;

import com.hburak_dev.psk_full_stack.common.PageResponse;
import com.hburak_dev.psk_full_stack.exception.OperationNotPermittedException;
import com.hburak_dev.psk_full_stack.service.FileStorageService;
import com.hburak_dev.psk_full_stack.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final BlogMapper blogMapper;
    private final FileStorageService fileStorageService;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${server.port}")
    private String serverPort;

    @Override
    @Transactional
    public Integer saveBlog(BlogRequest request, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Blog blog = blogMapper.toBlog(request);

        blog.setCreatedBy(user.getId());
        Blog savedBlog = blogRepository.save(blog);
        return savedBlog.getId();
    }

    @Override
    public String uploadImage(MultipartFile file, Integer blogId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new EntityNotFoundException("Bu ID ile blog bulunamadı :: " + blogId));
        if (blog.getImageFileName() != null) {
            fileStorageService.deleteFile(blog.getImageFileName(), "blogs");
        }
        String fileName = fileStorageService.storeFile(file, "blogs");
        blog.setImageFileName(fileName);
        blogRepository.save(blog);
        return fileName;
    }

    @Override
    @Transactional
    public PageResponse<BlogResponse> findAllBlogsShareable(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Blog> blogs = blogRepository.findAllShareableBlogs(pageable);
        List<BlogResponse> blogsResponse = blogs.stream()
                .map(blog -> toBlogResponse(blog))
                .toList();
        return new PageResponse<>(
                blogsResponse,
                blogs.getNumber(),
                blogs.getSize(),
                blogs.getTotalElements(),
                blogs.getTotalPages(),
                blogs.isFirst(),
                blogs.isLast());
    }

    @Override
    @Transactional
    public PageResponse<BlogResponse> findAllBlogs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Blog> blogs = blogRepository.findAllBlogs(pageable);
        List<BlogResponse> blogsResponse = blogs.stream()
                .map(blog -> toBlogResponse(blog))
                .toList();
        return new PageResponse<>(
                blogsResponse,
                blogs.getNumber(),
                blogs.getSize(),
                blogs.getTotalElements(),
                blogs.getTotalPages(),
                blogs.isFirst(),
                blogs.isLast());
    }

    @Override
    @Transactional
    public Integer updateSelectedBlog(Integer id, BlogRequest request, Authentication authentication) {
        Blog existingBlog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bu ID ile blog bulunamadı :: " + id));

        User user = (User) authentication.getPrincipal();
        if (!existingBlog.getCreatedBy().equals(user.getId())) {
            throw new RuntimeException("Blog güncellemek için gerekli yetkiye sahip değilsiniz");
        }
        if (request.getTitle() != null)
            existingBlog.setTitle(request.getTitle());
        if (request.getSubTitle() != null)
            existingBlog.setSubTitle(request.getSubTitle());
        if (request.getText() != null)
            existingBlog.setText(request.getText());

        existingBlog.setShareable(request.isShareable());

        return blogRepository.save(existingBlog).getId();
    }

    @Override
    public Integer updateShareableStatus(Integer id, Authentication connectedUser) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bu ID ile blog bulunamadı :: " + id));
        User user = ((User) connectedUser.getPrincipal());
        if (!Objects.equals(blog.getCreatedBy(), user.getId())) {
            throw new OperationNotPermittedException("Blog statüsünü sadece admin değiştirebilir.");
        }
        blog.setShareable(!blog.isShareable());
        blogRepository.save(blog);
        return blog.getId();
    }

    @Override
    @Transactional
    public ResponseEntity<Boolean> removeSelectedBlog(Integer id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bu ID ile blog bulunamadı :: " + id));
        if (blog.getImageFileName() != null) {
            try {
                fileStorageService.deleteFile(blog.getImageFileName(), "blogs");
            } catch (Exception e) {
                log.error("Blog resmi silinirken bir hata oluştu :: " + e.getMessage());
            }
        }
        blogRepository.deleteById(id);

        log.info("{} ID'sine sahip blog silindi", id);
        return ResponseEntity.ok(true);
    }

    @Override
    @Transactional
    public BlogResponse findBlogById(Integer id) {
        return toBlogResponse(blogRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bu ID ile blog bulunamadı :: " + id)));
    }

    private BlogResponse toBlogResponse(Blog blog) {
        String imageUrl = null;
        if (blog.getImageFileName() != null) {
            imageUrl = String.format("http://localhost:%s/api/v3/files/blog/download/%s",
                    serverPort, blog.getImageFileName());
        }

        return BlogResponse.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .text(blog.getText())
                .subTitle(blog.getSubTitle())
                .shareable(blog.isShareable())
                .imageUrl(imageUrl)
                .build();
    }
}
