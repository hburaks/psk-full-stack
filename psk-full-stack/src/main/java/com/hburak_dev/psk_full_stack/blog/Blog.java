package com.hburak_dev.psk_full_stack.blog;

import com.hburak_dev.psk_full_stack.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
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

    private String title;

    private String subTitle;

    private String text;

    @Lob
    byte[] cover;

    private boolean shareable;
}
