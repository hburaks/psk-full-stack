package com.hburak_dev.psk_full_stack.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PublicTestAnswerCommentResponse {

    private String title;

    private String text;

    byte[] cover;

}
