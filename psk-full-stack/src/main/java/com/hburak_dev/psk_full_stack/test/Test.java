package com.hburak_dev.psk_full_stack.test;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hburak_dev.psk_full_stack.comment.Comment;
import com.hburak_dev.psk_full_stack.common.BaseEntity;
import com.hburak_dev.psk_full_stack.question.Question;
import com.hburak_dev.psk_full_stack.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
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

    private String imageUrl;
    
    private Boolean isActive;

    @Builder.Default
    private Boolean isCompleted = Boolean.FALSE;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;

    @ManyToOne
    @JsonIgnoreProperties("tests")
    private User user;

    @ManyToMany
    private List<Comment> comments;

}

