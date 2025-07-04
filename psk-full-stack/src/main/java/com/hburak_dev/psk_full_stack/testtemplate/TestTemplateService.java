package com.hburak_dev.psk_full_stack.testtemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestTemplateService {

    private final TestTemplateRepository testTemplateRepository;

    public List<TestTemplate> findAll() {
        return testTemplateRepository.findAll();
    }

    public Optional<TestTemplate> findById(Integer id) {
        return testTemplateRepository.findById(id);
    }

    public TestTemplate save(TestTemplate testTemplate) {
        return testTemplateRepository.save(testTemplate);
    }

    public void deleteById(Integer id) {
        testTemplateRepository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return testTemplateRepository.existsById(id);
    }

    public List<TestTemplate> findByIsActive(Boolean isActive) {
        return testTemplateRepository.findByIsActive(isActive);
    }
}