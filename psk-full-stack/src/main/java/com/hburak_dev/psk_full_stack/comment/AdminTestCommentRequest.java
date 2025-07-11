package com.hburak_dev.psk_full_stack.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AdminTestCommentRequest {

    private Integer commentId;

    private Integer score;

    private String title;

    private String text;

}
