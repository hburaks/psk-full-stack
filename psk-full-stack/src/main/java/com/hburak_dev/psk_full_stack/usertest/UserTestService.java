package com.hburak_dev.psk_full_stack.usertest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserTestService {

    private final UserTestRepository userTestRepository;

    public List<UserTest> findAll() {
        return userTestRepository.findAll();
    }

    public Optional<UserTest> findById(Integer id) {
        return userTestRepository.findById(id);
    }

    public UserTest save(UserTest userTest) {
        return userTestRepository.save(userTest);
    }

    public void deleteById(Integer id) {
        userTestRepository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return userTestRepository.existsById(id);
    }

    public UserTest assignTestToUser(Long userId, Long testTemplateId, Long assignedBy) {
        Optional<UserTest> existingAssignment = userTestRepository.findByUserIdAndTestTemplateId(userId, testTemplateId);
        
        if (existingAssignment.isPresent()) {
            throw new IllegalArgumentException("Test template is already assigned to this user");
        }

        UserTest userTest = UserTest.builder()
                .userId(userId)
                .testTemplateId(testTemplateId)
                .assignedBy(assignedBy)
                .assignedAt(LocalDateTime.now())
                .isCompleted(false)
                .build();

        return userTestRepository.save(userTest);
    }

    public List<UserTest> getUserTests(Long userId) {
        return userTestRepository.findByUserId(userId);
    }

    public List<UserTest> getUserTestsByCompletion(Long userId, Boolean isCompleted) {
        return userTestRepository.findByUserIdAndIsCompleted(userId, isCompleted);
    }

    public List<UserTest> getCompletedTestsByUserId(Long userId) {
        return userTestRepository.findCompletedTestsByUserId(userId);
    }

    public List<UserTest> getPendingTestsByUserId(Long userId) {
        return userTestRepository.findPendingTestsByUserId(userId);
    }

    public UserTest markTestAsCompleted(Integer userTestId, String personalNotes) {
        UserTest userTest = userTestRepository.findById(userTestId)
                .orElseThrow(() -> new IllegalArgumentException("UserTest not found with id: " + userTestId));

        if (userTest.getIsCompleted()) {
            throw new IllegalStateException("Test is already completed");
        }

        userTest.setIsCompleted(true);
        userTest.setCompletedAt(LocalDateTime.now());
        if (personalNotes != null && !personalNotes.trim().isEmpty()) {
            userTest.setPersonalNotes(personalNotes);
        }

        return userTestRepository.save(userTest);
    }

    public UserTest updatePersonalNotes(Integer userTestId, String personalNotes) {
        UserTest userTest = userTestRepository.findById(userTestId)
                .orElseThrow(() -> new IllegalArgumentException("UserTest not found with id: " + userTestId));

        userTest.setPersonalNotes(personalNotes);
        return userTestRepository.save(userTest);
    }

    public List<UserTest> getTestsByTemplateId(Long testTemplateId) {
        return userTestRepository.findByTestTemplateId(testTemplateId);
    }

    public List<UserTest> getTestsAssignedBy(Long assignedBy) {
        return userTestRepository.findByAssignedBy(assignedBy);
    }

    public Optional<UserTest> findByUserIdAndTestTemplateId(Long userId, Long testTemplateId) {
        return userTestRepository.findByUserIdAndTestTemplateId(userId, testTemplateId);
    }
}