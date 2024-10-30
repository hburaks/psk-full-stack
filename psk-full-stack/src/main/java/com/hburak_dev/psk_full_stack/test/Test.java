package com.hburak_dev.psk_full_stack.test;

import com.hburak_dev.psk_full_stack.comment.Comment;
import com.hburak_dev.psk_full_stack.common.BaseEntity;
import com.hburak_dev.psk_full_stack.question.Question;
import com.hburak_dev.psk_full_stack.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Test extends BaseEntity {

    private String title;

    private String subTitle;

    @Lob
    byte[] cover;

    private boolean isActive;

    private boolean isPublicTest;

    @OneToMany(mappedBy = "test")
    private List<Question> questions;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @OneToMany(mappedBy = "test")
    private List<Comment> comments;


}
