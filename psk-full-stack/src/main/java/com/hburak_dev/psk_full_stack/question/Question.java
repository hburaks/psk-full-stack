package com.hburak_dev.psk_full_stack.question;

import com.hburak_dev.psk_full_stack.choice.Choice;
import com.hburak_dev.psk_full_stack.common.BaseEntity;
import com.hburak_dev.psk_full_stack.test.Test;
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
public class Question extends BaseEntity {

    private String text;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;


    @Enumerated(EnumType.STRING)
    private AnswerType userAnswer;

    @OneToMany(mappedBy = "question")
    private List<Choice> choices;

}
