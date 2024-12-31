import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MyAnswerQuestionRequest, MyTestResponse } from 'src/app/services/models';
import { TestService } from 'src/app/services/services/test.service';

@Component({
  selector: 'app-user-test-card-detail',
  templateUrl: './user-test-card-detail.component.html',
  styleUrls: ['./user-test-card-detail.component.scss'],
})
export class UserTestCardDetailComponent {
  constructor(private testService: TestService) {}

  @Input() testCard!: MyTestResponse;

  @Output() onCompleteTest = new EventEmitter<void>();

  selectedChoices: { [key: number]: number } = {};

  questions?: MyAnswerQuestionRequest[] = [];

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

  completeTest() {
    this.testService
      .saveMyTestAnswer({
        body: {
          testId: this.testCard.testId,
          questions: this.questions,
        },
      })
      .subscribe({
        next: (res) => {
          this.onCompleteTest.emit();
        },
        error: (error) => {
          console.log(error);
        },
      });
  }
}
