package com.hburak_dev.psk_full_stack.usertest;

import com.hburak_dev.psk_full_stack.common.BaseEntity;
import com.hburak_dev.psk_full_stack.user.User;
import com.hburak_dev.psk_full_stack.testtemplate.TestTemplate;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Table(name = "user_test", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "test_template_id"})
})
public class UserTest extends BaseEntity {

    @NotNull(message = "User ID is required")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull(message = "Test Template ID is required")
    @Column(name = "test_template_id", nullable = false)
    private Long testTemplateId;

    @NotNull(message = "Assigned date is required")
    @Column(name = "assigned_at", nullable = false)
    private LocalDateTime assignedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Builder.Default
    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted = false;

    @NotNull(message = "Assigned by is required")
    @Column(name = "assigned_by", nullable = false)
    private Long assignedBy;

    @Column(name = "personal_notes", columnDefinition = "TEXT")
    private String personalNotes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_template_id", insertable = false, updatable = false)
    private TestTemplate testTemplate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_by", insertable = false, updatable = false)
    private User assignedByUser;
}