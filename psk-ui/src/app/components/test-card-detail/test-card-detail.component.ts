import {
  AfterViewInit,
  Component,
  Input,
  Output,
  EventEmitter,
} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FindBlogById$Params } from 'src/app/services/fn/blog/find-blog-by-id';
import { SavePublicTestV2$Params } from 'src/app/services/fn/test/save-public-test-v-2';
import { UpdatePublicTestCommentsV2$Params } from 'src/app/services/fn/test/update-public-test-comments-v-2';
import { UpdatePublicTestQuestionsV2$Params } from 'src/app/services/fn/test/update-public-test-questions-v-2';
import { UpdatePublicTestV2$Params } from 'src/app/services/fn/test/update-public-test-v-2';
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
  TestTemplateResponse,
  TestTemplateCreateRequest,
  TestTemplateUpdateRequest,
  QuestionResponse,
  Choice,
} from 'src/app/services/models';
import { TestService, TestTemplateAdminService, QuestionAdminService } from 'src/app/services/services';
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
  @Input() editableTestTemplate: TestTemplateResponse | null = null;

  @Output() closeModalEvent = new EventEmitter<void>();

  updateTestCard: PublicTestRequest = {} as PublicTestRequest;
  updateTestTemplate: TestTemplateUpdateRequest = {} as TestTemplateUpdateRequest;

  testCard: PublicTestResponse | null = null;
  selectedChoices: { [key: number]: number } = {};
  testResult: PublicTestAnswerCommentResponse | null = null;
  questions?: PublicTestAnswerQuestionRequest[] = [];

  comments: AdminTestCommentRequest[] = [];

  title: string = 'Psikolojik Testler';
  text: string =
    'Kendinizi daha iyi tanımak için tasarlanmış çeşitli psikolojik testlerimizi deneyebilirsiniz. Bu testler kişilik özellikleri, duygusal durum ve davranış kalıplarınız hakkında içgörü kazanmanıza yardımcı olacaktır.';

  selectedFile: File | null = null;
  @Input() imageUrl: string = '';

  // Question management for test templates
  templateQuestions: QuestionResponse[] = [];
  showQuestionManagement: boolean = false;

  constructor(
    private route: ActivatedRoute, 
    private testService: TestService, 
    private testTemplateAdminService: TestTemplateAdminService,
    private questionAdminService: QuestionAdminService
  ) {
    this.cardId = this.route.snapshot.params['id'];
  }

  ngAfterViewInit() {
    if (!this.editableTestCard && !this.editableTestTemplate && !this.isEditPage) {
      this.getTestDetail();
    }
    
    if (this.editableTestCard) {
      this.updateTestCard = {
        title: this.editableTestCard?.title,
        subTitle: this.editableTestCard?.subTitle,
        isActive: this.editableTestCard?.isActive,
      };
    }
    
    if (this.editableTestTemplate) {
      this.updateTestTemplate = {
        title: this.editableTestTemplate?.title || '',
        subTitle: this.editableTestTemplate?.subTitle,
        imageUrl: this.editableTestTemplate?.imageUrl,
        isActive: this.editableTestTemplate?.isActive,
      };
    }
  }

  getTestDetail() {
    const params: FindBlogById$Params = { id: this.cardId };
    this.testService.getPublicTestById(params).subscribe({
      next: (test: PublicTestResponse) => {
        this.testCard = test;
        this.imageUrl = test.imageUrl!;
      },
    });
  }

  selectChoice(questionIndex: number, choiceIndex: number, id: number) {
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
    if (this.questions?.find((q) => q.id === id)) {
      this.questions = this.questions.map((q) =>
        q.id === id ? { ...q, chosenAnswer: chosenAnswer } : q
      );
    } else {
      this.questions?.push({
        chosenAnswer: chosenAnswer,
        id: id,
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
      id: choiceResponse.id,
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
      id: questionResponse.id,
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

  saveOrUpdateTestInfo() {
    // Handle TestTemplate save/update
    if (this.editableTestTemplate) {
      this.saveOrUpdateTestTemplateInfo();
      return;
    }
    
    // Handle legacy Test save/update
    if (this.editableTestCard?.id) {
      this.updateTestCard.testId = this.editableTestCard.id;
      if (
        this.updateTestCard.title ||
        this.updateTestCard.subTitle ||
        this.updateTestCard.isActive
      ) {
        const params: UpdatePublicTestV2$Params = {
          body: this.updateTestCard!,
        };

        this.testService.updatePublicTestV2(params).subscribe({
          next: (response: AdminTestResponse) => {
            if (this.selectedFile && this.updateTestCard.testId) {
              this.uploadImageToServer(this.updateTestCard.testId!);
            } else {
              this.closeModalEvent.emit();
            }
          },
        });
      } else if (this.selectedFile) {
        this.uploadImageToServer(this.updateTestCard.testId!);
      }
    } else {
      this.saveTest();
    }
  }

  saveOrUpdateTestTemplateInfo() {
    if (this.editableTestTemplate?.id) {
      // UPDATE existing template
      const params = {
        id: this.editableTestTemplate.id,
        body: this.updateTestTemplate,
      };
      this.testTemplateAdminService.updateTestTemplate(params).subscribe({
        next: (response: TestTemplateResponse) => {
          this.closeModalEvent.emit();
        },
        error: (err: any) => {
          console.error('Error updating test template', err);
        },
      });
    } else {
      // CREATE new template
      this.saveTestTemplate();
    }
  }

  saveTestTemplate() {
    const params = {
      body: this.updateTestTemplate,
    };
    this.testTemplateAdminService.createTestTemplate(params).subscribe({
      next: (response: TestTemplateResponse) => {
        this.closeModalEvent.emit();
      },
      error: (err: any) => {
        console.error('Error creating test template', err);
      },
    });
  }

  navigateToQuestionManagement() {
    if (this.editableTestTemplate?.id) {
      this.loadTemplateQuestions(this.editableTestTemplate.id);
      this.showQuestionManagement = true;
    }
  }

  loadTemplateQuestions(testTemplateId: number) {
    this.questionAdminService.getQuestionsByTestTemplate({ testTemplateId }).subscribe({
      next: (questions: QuestionResponse[]) => {
        this.templateQuestions = questions || [];
      },
      error: (err: any) => {
        console.error('Error loading template questions', err);
      },
    });
  }

  addTemplateQuestion() {
    const newQuestion: QuestionResponse = {
      text: '',
      testTemplateId: this.editableTestTemplate?.id,
      orderIndex: this.templateQuestions.length,
      choices: [
        { text: '', answerType: 'ANSWER_A' },
        { text: '', answerType: 'ANSWER_B' },
        { text: '', answerType: 'ANSWER_C' },
        { text: '', answerType: 'ANSWER_D' },
        { text: '', answerType: 'ANSWER_E' },
      ],
    };
    this.templateQuestions.push(newQuestion);
  }

  removeTemplateQuestion(index: number) {
    this.templateQuestions.splice(index, 1);
  }

  addTemplateChoice(questionIndex: number) {
    const question = this.templateQuestions[questionIndex];
    if (question.choices && question.choices.length < 5) {
      const nextAnswerType = ['ANSWER_A', 'ANSWER_B', 'ANSWER_C', 'ANSWER_D', 'ANSWER_E'][question.choices.length] as Choice['answerType'];
      question.choices.push({
        text: '',
        answerType: nextAnswerType,
      });
    }
  }

  removeTemplateChoice(questionIndex: number, choiceIndex: number) {
    const question = this.templateQuestions[questionIndex];
    if (question.choices && question.choices.length > 2) {
      question.choices.splice(choiceIndex, 1);
    }
  }

  saveTemplateQuestions() {
    // Implementation for saving questions will depend on the API structure
    // For now, just close the question management view
    this.showQuestionManagement = false;
    alert('Question saving functionality will be implemented based on the specific API requirements.');
  }

  saveTest() {
    const params: SavePublicTestV2$Params = {
      body: this.updateTestCard!,
    };
    this.testService.savePublicTestV2(params).subscribe({
      next: (response: AdminTestResponse) => {
        if (this.selectedFile) {
          this.uploadImageToServer(response.id!);
        }
        this.closeModalEvent.emit();
      },
    });
  }

  uploadImageToServer(id: number) {
    this.testService
      .uploadImage({
        testId: id,
        body: {
          file: this.selectedFile!,
        },
      })
      .subscribe({
        next: (response) => {
          this.closeModalEvent.emit();
        },
      });
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
      id: undefined,
    };

    if (this.editableTestCard && this.editableTestCard.questions) {
      this.editableTestCard.questions[
        questionIndex
      ].publicChoiceResponseList?.push(choice);
    }
  }

  async onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.imageUrl = e.target.result;
        if (this.editableTestCard) {
          this.editableTestCard.imageUrl = e.target.result;
        }
      };
      reader.readAsDataURL(file);
    }
  }

  removeChoice(questionIndex: number, choiceIndex: number) {
    this.editableTestCard!.questions![
      questionIndex
    ].publicChoiceResponseList?.splice(choiceIndex, 1);
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


  updateTestComments() {
    const params: UpdatePublicTestCommentsV2$Params = {
      body: {
        comments: this.editableTestCard!.comments,
        testId: this.editableTestCard!.id,
      },
    };
    this.testService.updatePublicTestCommentsV2(params).subscribe({
      next: (response: boolean) => {
        console.log('Update success:', response);
      },
    });
  }

  updateTestQuestions() {
    this.editableTestCard!.questions = this.editableTestCard!.questions?.map(
      (q) => {
        const publicChoiceRequestList: PublicChoiceRequest[] =
          q.publicChoiceResponseList?.map((c) => ({
            id: c.id,
            text: c.text,
            answerType: c.answerType,
          })) ?? [];
        return {
          ...q,
          publicChoiceRequestList: publicChoiceRequestList,
        };
      }
    );

    const params: UpdatePublicTestQuestionsV2$Params = {
      body: {
        publicTestQuestionRequestList: this.editableTestCard!.questions,
        testId: this.editableTestCard!.id,
      },
    };
    this.testService.updatePublicTestQuestionsV2(params).subscribe({
      next: (response: boolean) => {
        console.log('Update success:', response);
      },
    });
  }

  uploadImage() {
    const fileInput = document.createElement('input');
    fileInput.type = 'file';
    fileInput.accept = 'image/*';
    fileInput.onchange = (e) => this.onFileSelected(e);
    fileInput.click();
  }
}
