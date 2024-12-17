import {
  AfterViewInit,
  Component,
  Input,
  Output,
  EventEmitter,
} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonService } from 'src/app/custom-services/common-service/common.service';
import { FindBlogById$Params } from 'src/app/services/fn/blog/find-blog-by-id';
import {
  AdminTestCommentRequest,
  PublicChoiceRequest,
  PublicChoiceResponse,
  PublicQuestionResponse,
  PublicTestAnswerCommentResponse,
  PublicTestAnswerQuestionRequest,
  PublicTestQuestionRequest,
  PublicTestRequest,
  PublicTestResponse,
  AdminTestResponse,
  AdminTestCommentResponse,
} from 'src/app/services/models';
import { TestService } from 'src/app/services/services';
declare var bootstrap: any;

@Component({
  selector: 'app-test-card-detail',
  templateUrl: './test-card-detail.component.html',
  styleUrls: ['./test-card-detail.component.scss'],
})
export class TestCardDetailComponent implements AfterViewInit {
  @Input() testCardList: PublicTestResponse[] = [];
  @Input() isEditPage: boolean = false;

  cardId: number = this.route.snapshot.params['id'];
  @Input() editableTestCard: AdminTestResponse | null = null;

  @Output() closeModalEvent = new EventEmitter<void>();

  testCard: PublicTestResponse | null = null;
  selectedChoices: { [key: number]: number } = {};
  testResult: PublicTestAnswerCommentResponse | null = null;
  questions?: PublicTestAnswerQuestionRequest[] = [];

  comments: AdminTestCommentRequest[] = [];

  title: string = 'Psikolojik Testler';
  text: string =
    'Kendinizi daha iyi tanımak için tasarlanmış çeşitli psikolojik testlerimizi deneyebilirsiniz. Bu testler kişilik özellikleri, duygusal durum ve davranış kalıplarınız hakkında içgörü kazanmanıza yardımcı olacaktır.';

  constructor(
    private commonService: CommonService,
    private route: ActivatedRoute,
    private testService: TestService
  ) {
    this.cardId = this.route.snapshot.params['id'];
  }

  ngAfterViewInit() {
    if (!this.editableTestCard && !this.isEditPage) {
      this.getTestDetail();
    }
  }

  getTestDetail() {
    const params: FindBlogById$Params = { id: this.cardId };
    this.testService.getPublicTestById(params).subscribe({
      next: (test: PublicTestResponse) => {
        this.testCard = test;
      },
    });
  }

  selectChoice(questionIndex: number, choiceIndex: number, questionId: number) {
    let chosenAnswer:
      | 'ANSWER_A'
      | 'ANSWER_B'
      | 'ANSWER_C'
      | 'ANSWER_D'
      | 'ANSWER_E';
    if (choiceIndex === 0) chosenAnswer = 'ANSWER_A';
    else if (choiceIndex === 1) chosenAnswer = 'ANSWER_B';
    else if (choiceIndex === 2) chosenAnswer = 'ANSWER_C';
    else if (choiceIndex === 3) chosenAnswer = 'ANSWER_D';
    else chosenAnswer = 'ANSWER_E';
    if (this.questions?.find((q) => q.questionId === questionId)) {
      this.questions = this.questions.map((q) =>
        q.questionId === questionId ? { ...q, chosenAnswer: chosenAnswer } : q
      );
    } else {
      this.questions?.push({
        chosenAnswer: chosenAnswer,
        questionId: questionId,
      });
    }
    this.selectedChoices[questionIndex] = choiceIndex;
  }

  getResult() {
    if (
      this.questions?.length === 0 ||
      this.questions?.length !== this.testCard?.questions?.length
    ) {
      this.showToast();
      return;
    }
    this.testService
      .checkPublicTestAnswer({
        body: { questions: this.questions, testId: this.cardId },
      })
      .subscribe({
        next: (result: PublicTestAnswerCommentResponse) => {
          this.testResult = result;
        },
      });
  }

  showToast() {
    const toastElement = document.querySelector('.toast');
    if (toastElement) {
      const bsToast = new bootstrap.Toast(toastElement);
      bsToast.show();
    }
  }

  choiceResponseToRequestMapper(
    choiceResponse: PublicChoiceResponse
  ): PublicChoiceRequest {
    return {
      choiceId: choiceResponse.id,
      text: choiceResponse.text,
      answerType: choiceResponse.answerType,
    };
  }

  choiceResponseListToRequestMapper(
    choiceResponseList: PublicChoiceResponse[]
  ): PublicChoiceRequest[] {
    return choiceResponseList.map((choice) =>
      this.choiceResponseToRequestMapper(choice)
    );
  }

  questionResponseToRequestMapper(
    questionResponse: PublicQuestionResponse
  ): PublicTestQuestionRequest {
    return {
      questionId: questionResponse.id,
      text: questionResponse.text,
      publicChoiceRequestList: this.choiceResponseListToRequestMapper(
        questionResponse.publicChoiceResponseList || []
      ),
    };
  }

  questionResponseListToRequestMapper(
    questionResponseList: PublicQuestionResponse[]
  ): PublicTestQuestionRequest[] {
    return questionResponseList.map((question) =>
      this.questionResponseToRequestMapper(question)
    );
  }

  testResponseToRequestMapper(
    editableTestCard: AdminTestResponse
  ): PublicTestRequest {
    const questionsRequest: PublicTestQuestionRequest[] =
      this.questionResponseListToRequestMapper(
        editableTestCard.questions || []
      );
    return {
      cover: editableTestCard.cover,
      isActive: editableTestCard.isActive,
      publicTestQuestionRequestList: questionsRequest,
      subTitle: editableTestCard.subTitle,
      testId: editableTestCard.id,
      title: editableTestCard.title,
      comments: this.commentResponseListToRequestMapper(
        editableTestCard.comments || []
      ),
    };
  }

  commentResponseListToRequestMapper(
    commentResponseList: AdminTestCommentResponse[]
  ): AdminTestCommentRequest[] {
    return commentResponseList.map((comment) =>
      this.commentResponseToRequestMapper(comment)
    );
  }

  commentResponseToRequestMapper(
    commentResponse: AdminTestCommentResponse
  ): AdminTestCommentRequest {
    return {
      commentId: commentResponse.commentId ?? undefined,
      cover: commentResponse.cover,
      score: commentResponse.score,
      text: commentResponse.text,
      title: commentResponse.title,
    };
  }

  saveChanges() {
    if (this.editableTestCard) {
      let testRequest: PublicTestRequest = this.testResponseToRequestMapper(
        this.editableTestCard
      );
      testRequest = this.checkQuestionAnswerType(testRequest);
      this.testService.createPublicTestV2({ body: testRequest }).subscribe({
        next: () => {
          console.log('Test saved successfully');
          this.closeModalEvent.emit();
        },
        error: (error) => {
          console.log(error);
        },
      });
    }
  }

  addQuestion() {
    if (this.editableTestCard) {
      this.editableTestCard.questions = this.editableTestCard.questions || [];
      this.editableTestCard.questions.push({
        text: '',
        publicChoiceResponseList: [],
      });
    }
  }

  addChoice(questionIndex: number) {
    let answerType:
      | 'ANSWER_A'
      | 'ANSWER_B'
      | 'ANSWER_C'
      | 'ANSWER_D'
      | 'ANSWER_E';

    const choiceCount =
      this.editableTestCard!.questions![questionIndex]?.publicChoiceResponseList
        ?.length || 0;

    switch (choiceCount) {
      case 0:
        answerType = 'ANSWER_A';
        break;
      case 1:
        answerType = 'ANSWER_B';
        break;
      case 2:
        answerType = 'ANSWER_C';
        break;
      case 3:
        answerType = 'ANSWER_D';
        break;
      case 4:
        answerType = 'ANSWER_E';
        break;
      default:
        throw new Error('Invalid choice count');
    }

    const choice: PublicChoiceRequest = {
      text: '',
      answerType: answerType,
      choiceId: undefined,
    };

    if (this.editableTestCard && this.editableTestCard.questions) {
      this.editableTestCard.questions[
        questionIndex
      ].publicChoiceResponseList?.push(choice);
    }
  }

  async onFileChange(event: any) {
    /* const file = event.target.files[0];
    const base64 = await this.commonService.fileToBase64(file);
    this.editableTestCard!.cover = [base64]; */
    console.log('file changed');
  }

  removeChoice(questionIndex: number, choiceIndex: number) {
    this.editableTestCard!.questions![
      questionIndex
    ].publicChoiceResponseList?.splice(choiceIndex, 1);
  }

  checkQuestionAnswerType(testRequest: PublicTestRequest) {
    testRequest.publicTestQuestionRequestList?.forEach((question) => {
      question.publicChoiceRequestList?.forEach((choice, choiceIndex) => {
        switch (choiceIndex) {
          case 0:
            choice.answerType = 'ANSWER_A';
            break;
          case 1:
            choice.answerType = 'ANSWER_B';
            break;
          case 2:
            choice.answerType = 'ANSWER_C';
            break;
          case 3:
            choice.answerType = 'ANSWER_D';
            break;
          case 4:
            choice.answerType = 'ANSWER_E';
            break;
        }
      });
    });
    return testRequest;
  }

  removeQuestion(questionIndex: number) {
    this.editableTestCard!.questions?.splice(questionIndex, 1);
  }

  removeComment(commentIndex: number) {
    this.comments.splice(commentIndex, 1);
  }

  updateComments(comments: AdminTestCommentResponse[]) {
    this.editableTestCard!.comments = comments;
  }
}
