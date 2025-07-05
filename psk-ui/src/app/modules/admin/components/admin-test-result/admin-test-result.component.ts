import { Component, Input } from '@angular/core';
import { UserTestListResponse } from 'src/app/services/models/user-test-list-response';

@Component({
  selector: 'app-admin-test-result',
  templateUrl: './admin-test-result.component.html',
  styleUrls: ['./admin-test-result.component.scss'],
})
export class AdminTestResultComponent {
  @Input() testResult: UserTestListResponse | null = null;

  selectedChoices: string[] = [
    'ANSWER_A',
    'ANSWER_B',
    'ANSWER_C',
    'ANSWER_D',
    'ANSWER_E',
  ];
  constructor() {}

  formatAnswerDistribution(answerDistribution: any) {
    // Legacy formatting method - simplified implementation
    if (!answerDistribution) return {};
    return answerDistribution;
  }

  getAnswerType(index: number): string {
    return this.selectedChoices[index];
  }
}
