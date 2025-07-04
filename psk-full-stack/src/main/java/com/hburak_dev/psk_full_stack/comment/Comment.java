package com.hburak_dev.psk_full_stack.comment;

import com.hburak_dev.psk_full_stack.common.BaseEntity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;



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

    private String imageUrl;

}
