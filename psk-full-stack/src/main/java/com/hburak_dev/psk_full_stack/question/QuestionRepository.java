package com.hburak_dev.psk_full_stack.question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

    List<Question> findByTestTemplateId(Long testTemplateId);

    List<Question> findByTestTemplateIdOrderByOrderIndex(Long testTemplateId);

    @Query("SELECT q FROM Question q WHERE q.testTemplateId = :testTemplateId ORDER BY q.orderIndex ASC, q.id ASC")
    List<Question> findByTestTemplateIdOrderByOrderIndexAndId(@Param("testTemplateId") Long testTemplateId);

    @Query("SELECT COUNT(q) FROM Question q WHERE q.testTemplateId = :testTemplateId")
    Long countByTestTemplateId(@Param("testTemplateId") Long testTemplateId);

    @Query("SELECT MAX(q.orderIndex) FROM Question q WHERE q.testTemplateId = :testTemplateId")
    Integer findMaxOrderIndexByTestTemplateId(@Param("testTemplateId") Long testTemplateId);

}
