package com.hburak_dev.psk_full_stack.test;


import com.hburak_dev.psk_full_stack.comment.PublicTestCommentRequest;
import com.hburak_dev.psk_full_stack.question.PublicTestQuestionRequest;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PublicTestRequest {

    private Integer testId;

    private String title;

    private String subTitle;

    @Lob
    byte[] cover;

    List<PublicTestQuestionRequest> publicTestQuestionRequestList;

    @OneToMany(mappedBy = "test")
    private List<PublicTestCommentRequest> comments;

    private Boolean isActive;

}
