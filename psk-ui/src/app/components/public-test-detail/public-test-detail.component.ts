import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PublicTestService } from 'src/app/services/services/public-test.service';
import { TestTemplateResponse } from 'src/app/services/models/test-template-response';
import { QuestionResponse } from 'src/app/services/models/question-response';
import { PublicTestSubmissionRequest } from 'src/app/services/models/public-test-submission-request';
import { PublicTestResultResponse } from 'src/app/services/models/public-test-result-response';
import { SubmitAnswerRequest } from 'src/app/services/models/submit-answer-request';

@Component({
  selector: 'app-public-test-detail',
  templateUrl: './public-test-detail.component.html',
  styleUrls: ['./public-test-detail.component.scss']
})
export class PublicTestDetailComponent implements OnInit {
  testTemplate: TestTemplateResponse | null = null;
  questions: QuestionResponse[] = [];
  currentQuestionIndex: number = 0;
  answers: SubmitAnswerRequest[] = [];
  testResult: PublicTestResultResponse | null = null;
  isLoading: boolean = false;
  isTestCompleted: boolean = false;
  personalNotes: string = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private publicTestService: PublicTestService
  ) {}

  ngOnInit(): void {
    const testId = this.route.snapshot.paramMap.get('id');
    if (testId) {
      this.loadTest(+testId);
    }
  }

  loadTest(testId: number): void {
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

  initializeAnswers(): void {
    this.answers = this.questions.map(question => ({
      questionId: question.id!,
      answerType: undefined,
      userTestId: 0  // Not used for public tests but required by interface
    }));
  }

  selectAnswer(answerType: string): void {
    if (this.currentQuestionIndex < this.answers.length) {
      this.answers[this.currentQuestionIndex].answerType = answerType as any;
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
    return this.answers[this.currentQuestionIndex]?.answerType !== undefined;
  }

  isLastQuestion(): boolean {
    return this.currentQuestionIndex === this.questions.length - 1;
  }

  allQuestionsAnswered(): boolean {
    return this.answers.every(answer => answer.answerType !== undefined);
  }

  submitTest(): void {
    if (!this.allQuestionsAnswered() || !this.testTemplate?.id) {
      return;
    }

    this.isLoading = true;
    const submission: PublicTestSubmissionRequest = {
      answers: this.answers,
      personalNotes: this.personalNotes || undefined
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
        console.error('Error submitting test:', error);
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

  restartTest(): void {
    this.currentQuestionIndex = 0;
    this.isTestCompleted = false;
    this.testResult = null;
    this.personalNotes = '';
    this.initializeAnswers();
  }

  goBackToTests(): void {
    this.router.navigate(['/test']);
  }
}