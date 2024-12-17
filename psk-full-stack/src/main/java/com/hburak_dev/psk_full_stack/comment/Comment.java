package com.hburak_dev.psk_full_stack.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hburak_dev.psk_full_stack.common.BaseEntity;
import com.hburak_dev.psk_full_stack.test.Test;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
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
public class Comment extends BaseEntity {

    private Integer score;

    private String title;

    private String text;

    private byte[] cover;

    @ManyToMany(mappedBy = "comments")
    @JsonIgnore
    private List<Test> tests;

}
