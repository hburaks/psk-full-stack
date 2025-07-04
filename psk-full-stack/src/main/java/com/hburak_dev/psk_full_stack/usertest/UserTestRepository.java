package com.hburak_dev.psk_full_stack.usertest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserTestRepository extends JpaRepository<UserTest, Integer> {

    List<UserTest> findByUserId(Long userId);

    List<UserTest> findByTestTemplateId(Long testTemplateId);

    List<UserTest> findByUserIdAndIsCompleted(Long userId, Boolean isCompleted);

    Optional<UserTest> findByUserIdAndTestTemplateId(Long userId, Long testTemplateId);

    @Query("SELECT ut FROM UserTest ut WHERE ut.assignedBy = :assignedBy")
    List<UserTest> findByAssignedBy(@Param("assignedBy") Long assignedBy);

    @Query("SELECT ut FROM UserTest ut WHERE ut.userId = :userId AND ut.isCompleted = true")
    List<UserTest> findCompletedTestsByUserId(@Param("userId") Long userId);

    @Query("SELECT ut FROM UserTest ut WHERE ut.userId = :userId AND ut.isCompleted = false")
    List<UserTest> findPendingTestsByUserId(@Param("userId") Long userId);
}