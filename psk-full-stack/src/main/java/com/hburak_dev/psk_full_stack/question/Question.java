package com.hburak_dev.psk_full_stack.question;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hburak_dev.psk_full_stack.choice.Choice;
import com.hburak_dev.psk_full_stack.common.BaseEntity;
import com.hburak_dev.psk_full_stack.testtemplate.TestTemplate;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "question")
public class Question extends BaseEntity {

    private String text;

    @NotNull(message = "Test template ID is required")
    @Column(name = "test_template_id", nullable = false)
    private Long testTemplateId;

    @Builder.Default
    @Column(name = "order_index")
    private Integer orderIndex = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_template_id", insertable = false, updatable = false)
    @JsonIgnore
    private TestTemplate testTemplate;

    @Enumerated(EnumType.STRING)
    private AnswerType userAnswer;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<Choice> choices;


}
