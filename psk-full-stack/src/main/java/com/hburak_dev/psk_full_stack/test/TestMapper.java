package com.hburak_dev.psk_full_stack.test;

import com.hburak_dev.psk_full_stack.comment.Comment;
import com.hburak_dev.psk_full_stack.comment.CommentMapper;
import com.hburak_dev.psk_full_stack.question.Question;
import com.hburak_dev.psk_full_stack.question.QuestionMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
                                .imageUrl(test.getImageUrl())
                                .questions(test.getQuestions().stream()
                                                .map(questionMapper::toPublicQuestionResponse)
                                                .collect(Collectors.toList()))
                                .subTitle(test.getSubTitle())
                                .title(test.getTitle())
                                .build();
        }

        public MyTestResponse toMyTestResponse(Test test) {
                return MyTestResponse.builder()
                                .testId(test.getId())
                                .title(test.getTitle())
                                .subTitle(test.getSubTitle())
                                .imageUrl(test.getImageUrl())
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

                if (publicTestRequest.getPublicTestQuestionRequestList() != null) {
                        List<Question> questions = publicTestRequest.getPublicTestQuestionRequestList().stream()
                                        .map(questionRequest -> questionMapper.toQuestion(questionRequest, userId,
                                                        test))
                                        .collect(Collectors.toList());
                        test.setQuestions(questions);
                } else {
                        test.setQuestions(new ArrayList<>());
                }

                if (publicTestRequest.getComments() != null) {
                        List<Comment> comments = publicTestRequest.getComments().stream()
                                        .map(comment -> commentMapper.toComment(comment, test))
                                        .collect(Collectors.toList());
                        test.setComments(comments);
                } else {
                        test.setComments(new ArrayList<>());
                }
                test.setCreatedBy(userId);

                return test;
        }

        public PublicTestAdminResponse toPublicTestAdminResponse(Test test) {
                if (test.getQuestions().isEmpty()) {
                        test.setQuestions(new ArrayList<>());
                }
                return PublicTestAdminResponse.builder()
                                .id(test.getId())
                                .title(test.getTitle())
                                .subTitle(test.getSubTitle())
                                .imageUrl(test.getImageUrl())
                                .isActive(test.getIsActive())
                                .questions(test.getQuestions().stream()
                                                .map(questionMapper::toPublicQuestionAdminResponse)
                                                .collect(Collectors.toList()))
                                .build();
        }

        public UserTestForAdminResponse toUserTestForAdminResponse(Test test) {

                return UserTestForAdminResponse.builder()
                                .testId(test.getId())
                                .title(test.getTitle())
                                .subTitle(test.getSubTitle())
                                .imageUrl(test.getImageUrl())
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
