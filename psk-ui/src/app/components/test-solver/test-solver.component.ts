import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {PublicTestService} from 'src/app/services/services/public-test.service';
import {UserTestService} from 'src/app/services/services/user-test.service';
import {TestTemplateResponse} from 'src/app/services/models/test-template-response';
import {QuestionResponse} from 'src/app/services/models/question-response';
import {PublicTestSubmissionRequest} from 'src/app/services/models/public-test-submission-request';
import {PublicTestResultResponse} from 'src/app/services/models/public-test-result-response';
import {SubmitAnswerRequest} from 'src/app/services/models/submit-answer-request';
import {UserTestResponse} from 'src/app/services/models/user-test-response';
import {SubmitTestRequest} from 'src/app/services/models/submit-test-request';

@Component({
  selector: 'app-test-solver',
  templateUrl: './test-solver.component.html',
  styleUrls: ['./test-solver.component.scss']
})
export class TestSolverComponent implements OnInit {
  @Input() isUserTest: boolean = false;

  testTemplate: TestTemplateResponse | null = null;
  userTest: UserTestResponse | null = null;
  questions: QuestionResponse[] = [];
  currentQuestionIndex: number = 0;
  answers: SubmitAnswerRequest[] = [];
  testResult: PublicTestResultResponse | null = null;
  isLoading: boolean = false;
  isTestCompleted: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private publicTestService: PublicTestService,
    private userTestService: UserTestService,
  ) {}

  ngOnInit(): void {
    // Detect if this is a user test based on the route
    this.isUserTest = this.route.snapshot.url[0]?.path === 'user-test';

    const testId = this.route.snapshot.paramMap.get('id');
    if (testId) {
      if (this.isUserTest) {
        this.loadUserTest(+testId);
      } else {
        this.loadPublicTest(+testId);
      }
    }
  }

  loadPublicTest(testId: number): void {
    this.isLoading = true;

    // Load test template
    this.publicTestService.getTestTemplateById({ id: testId }).subscribe({
      next: (template) => {
        this.testTemplate = template;
        this.loadQuestions(testId);
      },
      error: (error) => {
        console.error('Error loading test template:', error);
        this.isLoading = false;
      }
    });
  }

  loadUserTest(userTestId: number): void {
    this.isLoading = true;

    // Load user test
    this.userTestService.getUserTest({ id: userTestId }).subscribe({
      next: (userTest) => {
        this.userTest = userTest;
        this.loadQuestionsForUserTest(userTest.testTemplateId!);
      },
      error: (error) => {
        console.error('Error loading user test:', error);
        this.isLoading = false;
      }
    });
  }

  loadQuestions(testId: number): void {
    this.publicTestService.getTestTemplateQuestions({ id: testId }).subscribe({
      next: (questions) => {
        this.questions = questions;
        this.initializeAnswers();
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading questions:', error);
        this.isLoading = false;
      }
    });
  }

  loadQuestionsForUserTest(testTemplateId: number): void {
    this.publicTestService.getTestTemplateQuestions({ id: testTemplateId }).subscribe({
      next: (questions) => {
        this.questions = questions;
        this.initializeAnswers();
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading questions:', error);
        this.isLoading = false;
      }
    });
  }

  initializeAnswers(): void {
    this.answers = this.questions.map(question => ({
      questionId: question.id!,
      answerType: undefined,
      userTestId: this.isUserTest ? (this.userTest?.id || 0) : 0
    }));
  }

  selectAnswer(choiceId?: number, textAnswer?: string): void {
    if (this.currentQuestionIndex < this.answers.length) {
      const currentAnswer = this.answers[this.currentQuestionIndex];
      if (choiceId !== undefined) {
        currentAnswer.choiceId = choiceId;
        currentAnswer.textAnswer = undefined; // Clear text answer if a choice is selected
      } else if (textAnswer !== undefined) {
        currentAnswer.textAnswer = textAnswer;
        currentAnswer.choiceId = undefined; // Clear choice if text is entered
      }
    }
  }

  nextQuestion(): void {
    if (this.currentQuestionIndex < this.questions.length - 1) {
      this.currentQuestionIndex++;
    }
  }

  previousQuestion(): void {
    if (this.currentQuestionIndex > 0) {
      this.currentQuestionIndex--;
    }
  }

  goToQuestion(index: number): void {
    this.currentQuestionIndex = index;
  }

  canProceed(): boolean {
    const currentAnswer = this.answers[this.currentQuestionIndex];
    return currentAnswer?.choiceId !== undefined || (currentAnswer?.textAnswer !== undefined && currentAnswer?.textAnswer !== '');
  }

  isLastQuestion(): boolean {
    return this.currentQuestionIndex === this.questions.length - 1;
  }

  allQuestionsAnswered(): boolean {
    return this.answers.every(answer =>
      answer.choiceId !== undefined || (answer.textAnswer !== undefined && answer.textAnswer !== '')
    );
  }

  submitTest(): void {
    if (!this.allQuestionsAnswered()) {
      return;
    }

    this.isLoading = true;

    if (this.isUserTest) {
      this.submitUserTest();
    } else {
      this.submitPublicTest();
    }
  }

  submitPublicTest(): void {
    if (!this.testTemplate?.id) {
      return;
    }

    const userAnswers: SubmitAnswerRequest[] = this.questions.map((question, index) => {
      const answer = this.answers[index];
      if (question.choices && question.choices.length > 0) {
        return {
          questionId: question.id!,
          choiceId: answer.choiceId,
          userTestId: 0 // Public tests don't have a userTestId
        };
      } else {
        return {
          questionId: question.id!,
          textAnswer: answer.textAnswer,
          userTestId: 0 // Public tests don't have a userTestId
        };
      }
    });

    const submission: PublicTestSubmissionRequest = {
      answers: userAnswers,
    };

    this.publicTestService.submitTestAnswers({
      id: this.testTemplate.id,
      body: submission
    }).subscribe({
      next: (result) => {
        this.testResult = result;
        this.isTestCompleted = true;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error submitting public test:', error);
        this.isLoading = false;
      }
    });
  }

  submitUserTest(): void {
    if (!this.userTest?.id) {
      return;
    }

    const currentTestId = this.userTest.id;

    const userAnswers: SubmitAnswerRequest[] = this.questions.map((question, index) => {
      const answer = this.answers[index];
      if (question.choices && question.choices.length > 0) {
        return {
          questionId: question.id!,
          choiceId: answer.choiceId,
          userTestId: currentTestId
        };
      } else {
        return {
          questionId: question.id!,
          textAnswer: answer.textAnswer,
          userTestId: currentTestId
        };
      }
    });

    const submission: SubmitTestRequest = {
      userTestId: this.userTest.id,
      answers: userAnswers
    };

    this.userTestService.submitAndCompleteTest({
      id: this.userTest.id,
      body: submission
    }).subscribe({
      next: () => {
        this.isTestCompleted = true;
        this.isLoading = false;
      },
      error: (error: any) => {
        console.error('Error submitting user test:', error);
        this.isLoading = false;
      }
    });
  }

  getCurrentQuestion(): QuestionResponse | null {
    return this.questions[this.currentQuestionIndex] || null;
  }

  getAnswerForCurrentQuestion(): string | undefined {
    return this.answers[this.currentQuestionIndex]?.answerType;
  }

  getTextAreaValue(event: Event): string | undefined {
    return (event.target as HTMLTextAreaElement)?.value;
  }

  hasChoices(question: QuestionResponse | null): boolean {
    return question !== null && question.choices !== undefined && question.choices.length > 0;
  }

  restartTest(): void {
    this.currentQuestionIndex = 0;
    this.isTestCompleted = false;
    this.testResult = null;
    this.initializeAnswers();
  }

  goBackToTests(): void {
    if (this.isUserTest) {
      this.router.navigate(['/user/panel']);
    } else {
      this.router.navigate(['/test']);
    }
  }
}
