package com.hburak_dev.psk_full_stack.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByTestTemplateIdOrderByScoreAsc(Long testTemplateId);

    @Query("SELECT c FROM Comment c WHERE c.testTemplateId = :testTemplateId AND c.score <= :score ORDER BY c.score DESC")
    List<Comment> findByTestTemplateIdAndScoreLessThanEqualOrderByScoreDesc(@Param("testTemplateId") Long testTemplateId, @Param("score") Integer score);

}
