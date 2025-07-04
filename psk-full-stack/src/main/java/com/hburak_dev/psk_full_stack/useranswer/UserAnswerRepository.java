package com.hburak_dev.psk_full_stack.useranswer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserAnswerRepository extends JpaRepository<UserAnswer, Integer> {

    List<UserAnswer> findByUserTestId(Long userTestId);

    Optional<UserAnswer> findByUserTestIdAndQuestionId(Long userTestId, Long questionId);

    List<UserAnswer> findByQuestionId(Long questionId);

    @Query("SELECT ua FROM UserAnswer ua WHERE ua.choiceId = :choiceId")
    List<UserAnswer> findByChoiceId(@Param("choiceId") Long choiceId);

    @Query("SELECT ua FROM UserAnswer ua WHERE ua.userTestId = :userTestId AND ua.choiceId IS NOT NULL")
    List<UserAnswer> findMultipleChoiceAnswersByUserTest(@Param("userTestId") Long userTestId);

    @Query("SELECT ua FROM UserAnswer ua WHERE ua.userTestId = :userTestId AND ua.textAnswer IS NOT NULL")
    List<UserAnswer> findTextAnswersByUserTest(@Param("userTestId") Long userTestId);

    @Query("SELECT COUNT(ua) FROM UserAnswer ua WHERE ua.userTestId = :userTestId")
    Long countAnswersByUserTest(@Param("userTestId") Long userTestId);

    @Query("SELECT ua FROM UserAnswer ua WHERE ua.questionId = :questionId AND ua.choiceId = :choiceId")
    List<UserAnswer> findByQuestionIdAndChoiceId(@Param("questionId") Long questionId, @Param("choiceId") Long choiceId);
}