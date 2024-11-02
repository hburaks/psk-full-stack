package com.hburak_dev.psk_full_stack.test;

import com.hburak_dev.psk_full_stack.comment.Comment;
import com.hburak_dev.psk_full_stack.comment.CommentMapper;
import com.hburak_dev.psk_full_stack.question.Question;
import com.hburak_dev.psk_full_stack.question.QuestionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestMapper {

    private final QuestionMapper questionMapper;
    private final CommentMapper commentMapper;

    public PublicTestResponse toPublicTestResponse(Test test) {
        return PublicTestResponse.builder()
                .id(test.getId())
                .cover(test.getCover())
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
                .cover(test.getCover())
                .questions(test.getQuestions().stream()
                        .map(questionMapper::toMyQuestionResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    public Test toTest(PublicTestRequest publicTestRequest) {
        Test test = Test.builder()
                .title(publicTestRequest.getTitle())
                .subTitle(publicTestRequest.getSubTitle())
                .cover(publicTestRequest.getCover())
                .isActive(publicTestRequest.getIsActive())
                .build();

        List<Question> questions = publicTestRequest.getPublicTestQuestionRequestList().stream()
                .map(publicTestQuestionRequest -> questionMapper.toQuestion(publicTestQuestionRequest, test))
                .collect(Collectors.toList());

        test.setQuestions(questions);

        List<Comment> comments = publicTestRequest.getComments().stream()
                .map(commentMapper::toComment)
                .collect(Collectors.toList());

        test.setComments(comments);

        return test;
    }

    public PublicTestAdminResponse toPublicTestAdminResponse(Test test) {
        return PublicTestAdminResponse.builder()
                .id(test.getId())
                .title(test.getTitle())
                .subTitle(test.getSubTitle())
                .cover(test.getCover())
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
                .cover(test.getCover())
                .userId(test.getUser().getId())
                .questions(test.getQuestions().stream()
                        .map(questionMapper::toUserQuestionResponse)
                        .collect(Collectors.toList()))
                .comments(test.getComments().stream()
                        .map(commentMapper::toUserCommentResponse)
                        .collect(Collectors.toList()))
                .build();
    }
}
