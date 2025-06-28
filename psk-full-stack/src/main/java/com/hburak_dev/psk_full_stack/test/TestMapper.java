package com.hburak_dev.psk_full_stack.test;

import com.hburak_dev.psk_full_stack.comment.CommentMapper;
import com.hburak_dev.psk_full_stack.question.QuestionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestMapper {

        @Value("${server.port}")
        private String serverPort;

        private final QuestionMapper questionMapper;
        private final CommentMapper commentMapper;

        public PublicTestResponse toPublicTestResponse(Test test) {
                return PublicTestResponse.builder()
                                .id(test.getId())
                        .imageUrl(test.getImageUrl() != null
                                ? String.format("http://localhost:%s/api/v3/files/test/download/%s", serverPort, test.getImageUrl())
                                : null)
                                .questions(test.getQuestions().stream()
                                                .map(questionMapper::toPublicQuestionResponse)
                                                .collect(Collectors.toList()))
                                .subTitle(test.getSubTitle())
                                .title(test.getTitle())
                                .build();
        }

        public MyTestResponse toMyTestResponse(Test test) {
                String imageUrl = null;
                if (test.getImageUrl() != null) {
                        imageUrl = String.format("http://localhost:%s/api/v3/files/test/download/%s",
                                        serverPort, test.getImageUrl());
                }
                return MyTestResponse.builder()
                                .testId(test.getId())
                                .title(test.getTitle())
                                .subTitle(test.getSubTitle())
                                .imageUrl(imageUrl)
                                .questions(test.getQuestions().stream()
                                                .map(questionMapper::toMyQuestionResponse)
                                                .collect(Collectors.toList()))
                                .build();
        }

        public Test toTest(PublicTestRequest publicTestRequest, Integer userId) {
                Test test = Test.builder()
                                .title(publicTestRequest.getTitle())
                                .subTitle(publicTestRequest.getSubTitle())
                                .isActive(publicTestRequest.getIsActive())
                                .build();

                test.setQuestions(new ArrayList<>());

                test.setComments(new ArrayList<>());
                test.setCreatedBy(userId);

                return test;
        }

        public PublicTestAdminResponse toPublicTestAdminResponse(Test test) {
                if (test.getQuestions().isEmpty()) {
                        test.setQuestions(new ArrayList<>());
                }
                String imageUrl = null;
                if (test.getImageUrl() != null) {
                        imageUrl = String.format("http://localhost:%s/api/v3/files/test/download/%s",
                                        serverPort, test.getImageUrl());
                }
                return PublicTestAdminResponse.builder()
                                .id(test.getId())
                                .title(test.getTitle())
                                .subTitle(test.getSubTitle())
                                .imageUrl(imageUrl)
                                .isActive(test.getIsActive())
                                .questions(test.getQuestions().stream()
                                                .map(questionMapper::toPublicQuestionAdminResponse)
                                                .collect(Collectors.toList()))
                                .build();
        }

        public UserTestForAdminResponse toUserTestForAdminResponse(Test test) {
                String imageUrl = null;
                if (test.getImageUrl() != null) {
                        imageUrl = String.format("http://localhost:%s/api/v3/files/test/download/%s",
                                        serverPort, test.getImageUrl());
                }

                return UserTestForAdminResponse.builder()
                                .testId(test.getId())
                                .title(test.getTitle())
                                .subTitle(test.getSubTitle())
                                .imageUrl(imageUrl)
                                .userId(test.getUser().getId())
                                .questions(test.getQuestions().stream()
                                                .map(questionMapper::toUserQuestionResponse)
                                                .collect(Collectors.toList()))
                                .comments(test.getComments().stream()
                                                .map(commentMapper::toUserCommentResponse)
                                                .collect(Collectors.toList()))
                                .lastModifiedDate(test.getLastModifiedDate())
                                .build();
        }

        public AdminTestResponse toAdminTestResponse(Test test) {
                String imageUrl = null;
                if (test.getImageUrl() != null) {
                        imageUrl = String.format("http://localhost:%s/api/v3/files/test/download/%s",
                                        serverPort, test.getImageUrl());
                }
                return AdminTestResponse.builder()
                                .id(test.getId())
                                .title(test.getTitle())
                                .subTitle(test.getSubTitle())
                                .imageUrl(imageUrl)
                                .isActive(test.getIsActive())
                                .comments(test.getComments().stream()
                                                .map(commentMapper::toAdminCommentResponse)
                                                .collect(Collectors.toList()))
                                .questions(test.getQuestions().stream()
                                                .map(questionMapper::toPublicQuestionAdminResponse)
                                                .collect(Collectors.toList()))
                                .build();
        }

}
