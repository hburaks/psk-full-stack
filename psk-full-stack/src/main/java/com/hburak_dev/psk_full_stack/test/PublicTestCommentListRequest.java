package com.hburak_dev.psk_full_stack.test;


import com.hburak_dev.psk_full_stack.comment.AdminTestCommentRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PublicTestCommentListRequest {

    private Integer testId;

    private List<AdminTestCommentRequest> comments; 

}
