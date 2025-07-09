package com.hburak_dev.psk_full_stack.comment;

import com.hburak_dev.psk_full_stack.common.BaseEntity;
import com.hburak_dev.psk_full_stack.testtemplate.TestTemplate;

import jakarta.persistence.*;
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
@Table(name = "comment")
public class Comment extends BaseEntity {

    private Integer score;

    private String title;

    private String text;

    private String imageUrl;

    @Column(name = "test_template_id")
    private Long testTemplateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_template_id", insertable = false, updatable = false)
    private TestTemplate testTemplate;

}
