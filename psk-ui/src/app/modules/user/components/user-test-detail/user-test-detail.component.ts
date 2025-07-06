import { Component, EventEmitter, Input, Output, OnInit } from '@angular/core';
import { UserTestListResponse, UserTestResponse, QuestionResponse, SubmitTestRequest, SubmitAnswerRequest, SubmitTestResponse } from 'src/app/services/models';
import { UserTestService, UserAnswerService, TestTemplateAdminService } from 'src/app/services/services';

@Component({
  selector: 'app-user-test-detail',
  templateUrl: './user-test-detail.component.html',
  styleUrls: ['./user-test-detail.component.scss'],
})
export class UserTestDetailComponent implements OnInit {
  @Input() userTestSummary!: UserTestListResponse;
  @Output() onCompleteTest = new EventEmitter<void>();

  userTest: UserTestResponse | null = null;
  questions: QuestionResponse[] = [];
  selectedAnswers: { [questionId: number]: SubmitAnswerRequest } = {};
  isLoading: boolean = true;
  isSubmitting: boolean = false;
  errorMessage: string = '';

  // For template access
  Object = Object;

  constructor(
    private userTestService: UserTestService,
    private userAnswerService: UserAnswerService,
    private testTemplateAdminService: TestTemplateAdminService
  ) {}

  ngOnInit() {
    this.loadUserTest();
  }

  loadUserTest() {
    if (!this.userTestSummary.id) {
      this.errorMessage = 'Test ID not found';
      this.isLoading = false;
      return;
    }

    // First, start the test to mark it as started
    this.userTestService.startTest({ id: this.userTestSummary.id }).subscribe({
      next: (userTest) => {
        this.userTest = userTest;
        this.loadQuestions();
      },
      error: (error) => {
        console.error('Error starting test', error);
        this.errorMessage = 'Test başlatılırken bir hata oluştu';
        this.isLoading = false;
      },
    });
  }

  loadQuestions() {
    if (!this.userTest?.testTemplateId) {
      this.errorMessage = 'Test template ID not found';
      this.isLoading = false;
      return;
    }

    this.testTemplateAdminService.getTestTemplateQuestions1({ id: this.userTest.testTemplateId }).subscribe({
      next: (questions: any) => {
        this.questions = questions || [];
        this.isLoading = false;
      },
      error: (error: any) => {
        console.error('Error loading questions', error);
        this.errorMessage = 'Sorular yüklenirken bir hata oluştu';
        this.isLoading = false;
      },
    });
  }

  selectChoice(questionId: number, choiceId: number, answerType: 'ANSWER_A' | 'ANSWER_B' | 'ANSWER_C' | 'ANSWER_D' | 'ANSWER_E') {
    this.selectedAnswers[questionId] = {
      questionId: questionId,
      choiceId: choiceId,
      answerType: answerType,
      userTestId: this.userTest?.id || 0,
    };
  }

  isChoiceSelected(questionId: number, choiceId: number): boolean {
    return this.selectedAnswers[questionId]?.choiceId === choiceId;
  }

  canSubmitTest(): boolean {
    return this.questions.length > 0 && 
           this.questions.every(q => q.id && this.selectedAnswers[q.id]);
  }

  submitTest() {
    if (!this.canSubmitTest() || !this.userTest?.id) {
      this.errorMessage = 'Lütfen tüm soruları cevaplayın';
      return;
    }

    this.isSubmitting = true;
    this.errorMessage = '';

    const answers = Object.values(this.selectedAnswers);
    const submitRequest: SubmitTestRequest = {
      userTestId: this.userTest.id,
      answers: answers,
      personalNotes: undefined, // Could be added as an optional feature
    };

    this.userAnswerService.submitTest({ userTestId: this.userTest.id, body: submitRequest }).subscribe({
      next: (response: SubmitTestResponse) => {
        this.isSubmitting = false;
        this.onCompleteTest.emit();
      },
      error: (error) => {
        console.error('Error submitting test', error);
        this.errorMessage = 'Test gönderilirken bir hata oluştu';
        this.isSubmitting = false;
      },
    });
  }

  get testTitle() {
    return this.userTest?.testTemplateTitle || this.userTestSummary.testTemplateTitle || 'Test';
  }

  get testSubTitle() {
    return this.userTest?.testTemplateSubTitle || this.userTestSummary.testTemplateSubTitle || '';
  }

  get testImageUrl() {
    return this.userTest?.testTemplateImageUrl || this.userTestSummary.testTemplateImageUrl;
  }
}