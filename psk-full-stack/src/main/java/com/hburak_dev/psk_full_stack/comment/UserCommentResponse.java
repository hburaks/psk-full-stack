package com.hburak_dev.psk_full_stack.comment;

import jakarta.persistence.Lob;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserCommentResponse {

    private Integer score;

    private String title;

    private String text;

    @Lob
    byte[] cover;

}
