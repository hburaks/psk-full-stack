package com.hburak_dev.psk_full_stack.usertest;

import com.hburak_dev.psk_full_stack.user.User;
import com.hburak_dev.psk_full_stack.exception.UserTestNotFoundException;
import com.hburak_dev.psk_full_stack.exception.UserTestAccessDeniedException;
import com.hburak_dev.psk_full_stack.exception.UserTestAlreadyCompletedException;
import com.hburak_dev.psk_full_stack.handler.BusinessErrorCodes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserTestServiceImpl implements UserTestServiceInterface {

    private final UserTestRepository userTestRepository;
    private final UserTestMapper userTestMapper;

    @Override
    @Transactional
    public UserTestResponse assignTestToUser(AssignTestRequest request, Authentication connectedUser) {
        User admin = (User) connectedUser.getPrincipal();
        
        // Check if test is already assigned to this user
        Optional<UserTest> existingAssignment = userTestRepository.findByUserIdAndTestTemplateId(
                request.getUserId(), request.getTestTemplateId());
        
        if (existingAssignment.isPresent()) {
            throw new UserTestAlreadyCompletedException("Test template is already assigned to this user", 
                    BusinessErrorCodes.USER_TEST_ALREADY_COMPLETED);
        }

        UserTest userTest = UserTest.builder()
                .userId(request.getUserId())
                .testTemplateId(request.getTestTemplateId())
                .assignedAt(LocalDateTime.now())
                .assignedBy(admin.getId().longValue())
                .personalNotes(request.getPersonalNotes())
                .isCompleted(false)
                .build();
        
        UserTest savedUserTest = userTestRepository.save(userTest);
        log.info("Test template {} assigned to user {} by admin {}", 
                request.getTestTemplateId(), request.getUserId(), admin.getId());
        
        return userTestMapper.toUserTestResponse(savedUserTest);
    }

    @Override
    @Transactional
    public List<UserTestListResponse> getAllUserTests() {
        List<UserTest> userTests = userTestRepository.findAll();
        return userTests.stream()
                .map(userTestMapper::toUserTestListResponse)
                .toList();
    }

    @Override
    @Transactional
    public List<UserTestListResponse> getCurrentUserTests(Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        List<UserTest> userTests = userTestRepository.findByUserId(user.getId().longValue());
        return userTests.stream()
                .map(userTestMapper::toUserTestListResponse)
                .toList();
    }

    @Override
    @Transactional
    public UserTestResponse getUserTestById(Integer id, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        UserTest userTest = userTestRepository.findById(id)
                .orElseThrow(() -> new UserTestNotFoundException("User test not found with id: " + id, 
                        BusinessErrorCodes.USER_TEST_NOT_FOUND));
        
        // Ensure user can only access their own tests
        if (!userTest.getUserId().equals(user.getId().longValue())) {
            throw new UserTestAccessDeniedException("Access denied: This test does not belong to you", 
                    BusinessErrorCodes.USER_TEST_ACCESS_DENIED);
        }
        
        return userTestMapper.toUserTestResponse(userTest);
    }

    @Override
    @Transactional
    public UserTestResponse startUserTest(Integer id, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        UserTest userTest = userTestRepository.findById(id)
                .orElseThrow(() -> new UserTestNotFoundException("User test not found with id: " + id, 
                        BusinessErrorCodes.USER_TEST_NOT_FOUND));
        
        // Ensure user can only start their own tests
        if (!userTest.getUserId().equals(user.getId().longValue())) {
            throw new UserTestAccessDeniedException("Access denied: This test does not belong to you", 
                    BusinessErrorCodes.USER_TEST_ACCESS_DENIED);
        }
        
        // Check if test is already completed
        if (userTest.getIsCompleted()) {
            throw new UserTestAlreadyCompletedException("Test is already completed", 
                    BusinessErrorCodes.USER_TEST_ALREADY_COMPLETED);
        }
        
        // Mark test as started (you might want to add a startedAt field to UserTest entity)
        // For now, we'll just return the test as is
        log.info("User {} started test {}", user.getId(), id);
        return userTestMapper.toUserTestResponse(userTest);
    }

    @Override
    @Transactional
    public UserTestResponse completeUserTest(Integer id, CompleteTestRequest request, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        UserTest userTest = userTestRepository.findById(id)
                .orElseThrow(() -> new UserTestNotFoundException("User test not found with id: " + id, 
                        BusinessErrorCodes.USER_TEST_NOT_FOUND));
        
        // Ensure user can only complete their own tests
        if (!userTest.getUserId().equals(user.getId().longValue())) {
            throw new UserTestAccessDeniedException("Access denied: This test does not belong to you", 
                    BusinessErrorCodes.USER_TEST_ACCESS_DENIED);
        }
        
        // Check if test is already completed
        if (userTest.getIsCompleted()) {
            throw new UserTestAlreadyCompletedException("Test is already completed", 
                    BusinessErrorCodes.USER_TEST_ALREADY_COMPLETED);
        }
        
        // Mark test as completed
        userTest.setIsCompleted(true);
        userTest.setCompletedAt(LocalDateTime.now());
        if (request.getPersonalNotes() != null) {
            userTest.setPersonalNotes(request.getPersonalNotes());
        }
        
        UserTest savedUserTest = userTestRepository.save(userTest);
        log.info("User {} completed test {}", user.getId(), id);
        
        return userTestMapper.toUserTestResponse(savedUserTest);
    }

    // Repository methods (keep existing functionality)
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