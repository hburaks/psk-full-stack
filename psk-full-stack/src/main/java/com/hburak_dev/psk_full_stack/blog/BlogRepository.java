package com.hburak_dev.psk_full_stack.blog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Blog, Integer> {

    Page<Blog> findAllBlogs(Pageable pageable);
}
