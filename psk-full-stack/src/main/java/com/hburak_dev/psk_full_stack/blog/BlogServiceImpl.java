package com.hburak_dev.psk_full_stack.blog;

import com.hburak_dev.psk_full_stack.common.PageResponse;
import com.hburak_dev.psk_full_stack.exception.OperationNotPermittedException;
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

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final BlogMapper blogMapper;

    @Override
    @Transactional
    public Integer saveBlog(BlogRequest request, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Blog blog = blogMapper.toBlog(request);
        blog.setCreatedBy(user.getId());
        blogRepository.save(blog);
        return blog.getId();
    }

    @Override
    @Transactional
    public PageResponse<BlogResponse> findAllBlogsShareable(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Blog> blogs = blogRepository.findAllBlogs(pageable);
        List<BlogResponse> blogsResponse = blogs.stream()
                .filter(Blog::isShareable)
                .map(blogMapper::toBlogResponse)
                .toList();
        return new PageResponse<>(
                blogsResponse,
                blogs.getNumber(),
                blogs.getSize(),
                blogs.getTotalElements(),
                blogs.getTotalPages(),
                blogs.isFirst(),
                blogs.isLast()
        );
    }

    @Override
    @Transactional
    public PageResponse<BlogResponse> findAllBlogs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Blog> blogs = blogRepository.findAllBlogs(pageable);
        List<BlogResponse> blogsResponse = blogs.stream()
                .map(blogMapper::toBlogResponse)
                .toList();
        return new PageResponse<>(
                blogsResponse,
                blogs.getNumber(),
                blogs.getSize(),
                blogs.getTotalElements(),
                blogs.getTotalPages(),
                blogs.isFirst(),
                blogs.isLast()
        );
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

        if (request.getTitle() != null) {
            existingBlog.setTitle(request.getTitle());
        }
        if (request.getSubTitle() != null) {
            existingBlog.setSubTitle(request.getSubTitle());
        }
        if (request.getText() != null) {
            existingBlog.setText(request.getText());
        }
        if (request.getCover() != null) {
            existingBlog.setCover(request.getCover());
        }
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
        if (blogRepository.existsById(id)) {
            blogRepository.deleteById(id);
            log.info("{} ID'sine sahip blog silindi", id);
            return ResponseEntity.ok(true);
        } else {
            log.warn("{} ID'sine sahip blog bulunamadı", id);
            return ResponseEntity.status(404).body(false);
        }
    }
}
