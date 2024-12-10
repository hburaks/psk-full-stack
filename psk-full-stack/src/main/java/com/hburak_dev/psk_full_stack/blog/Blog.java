package com.hburak_dev.psk_full_stack.blog;

import com.hburak_dev.psk_full_stack.common.BaseEntity;

import jakarta.persistence.Column;
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
public class Blog extends BaseEntity {

    @Column(length = 500)
    private String title;

    @Column(length = 500)
    private String subTitle;

    @Column(length = 3000)
    private String text;

    byte[] cover;

    private boolean shareable;
}
