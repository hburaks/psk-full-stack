package com.hburak_dev.psk_full_stack.testtemplate;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestTemplateRepository extends JpaRepository<TestTemplate, Integer> {
    
    List<TestTemplate> findByIsActive(Boolean isActive);
}