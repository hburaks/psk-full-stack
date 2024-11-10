package com.hburak_dev.psk_full_stack.choice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hburak_dev.psk_full_stack.common.BaseEntity;
import com.hburak_dev.psk_full_stack.question.AnswerType;
import com.hburak_dev.psk_full_stack.question.Question;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Choice extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private AnswerType answerType;

    private String text;

    @ManyToMany(mappedBy = "choices")
    @JsonIgnore
    private List<Question> questions;

}
