package com.hburak_dev.psk_full_stack.useranswer;

import com.hburak_dev.psk_full_stack.common.BaseEntity;
import com.hburak_dev.psk_full_stack.usertest.UserTest;
import com.hburak_dev.psk_full_stack.question.Question;
import com.hburak_dev.psk_full_stack.choice.Choice;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_answer", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_test_id", "question_id"})
})
public class UserAnswer extends BaseEntity {

    @NotNull(message = "User test ID is required")
    @Column(name = "user_test_id", nullable = false)
    private Long userTestId;

    @NotNull(message = "Question ID is required")
    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Column(name = "choice_id")
    private Long choiceId;

    @Column(name = "text_answer", columnDefinition = "TEXT")
    private String textAnswer;

    @NotNull(message = "Answered date is required")
    @Column(name = "answered_at", nullable = false)
    private LocalDateTime answeredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_test_id", insertable = false, updatable = false)
    private UserTest userTest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", insertable = false, updatable = false)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "choice_id", insertable = false, updatable = false)
    private Choice choice;

    @PrePersist
    @PreUpdate
    private void validateAnswer() {
        if ((choiceId == null && (textAnswer == null || textAnswer.trim().isEmpty())) ||
            (choiceId != null && textAnswer != null && !textAnswer.trim().isEmpty())) {
            throw new IllegalArgumentException("Either choiceId or textAnswer must be provided, but not both");
        }
    }
}