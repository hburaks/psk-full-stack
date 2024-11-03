package com.hburak_dev.psk_full_stack.blog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BlogRepository extends JpaRepository<Blog, Integer> {

    @Query("SELECT b FROM Blog b")
    Page<Blog> findAllBlogs(Pageable pageable);

    @Query("SELECT b FROM Blog b WHERE b.shareable = true")
    Page<Blog> findAllShareableBlogs(Pageable pageable);

}
