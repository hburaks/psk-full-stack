package com.hburak_dev.psk_full_stack.session;

import com.hburak_dev.psk_full_stack.common.BaseEntity;
import com.hburak_dev.psk_full_stack.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Session extends BaseEntity {

    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private SessionStatusType sessionStatus;

    private String noteForUser;

    private String noteForPsychologist;

    private boolean isSessionPaid;

}
