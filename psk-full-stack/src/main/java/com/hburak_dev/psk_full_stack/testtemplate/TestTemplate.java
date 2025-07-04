package com.hburak_dev.psk_full_stack.testtemplate;

import com.hburak_dev.psk_full_stack.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Table(name = "test_template")
public class TestTemplate extends BaseEntity {

    @NotBlank(message = "Title is required")
    @Column(nullable = false)
    private String title;

    private String subTitle;

    private String imageUrl;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isActive = true;
}