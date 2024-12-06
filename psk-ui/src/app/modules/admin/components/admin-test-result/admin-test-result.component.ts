import { Component, Input } from '@angular/core';
import { TestService } from 'src/app/custom-services/test/test.service';
import { UserTestForAdminResponse } from 'src/app/services/models/user-test-for-admin-response';

@Component({
  selector: 'app-admin-test-result',
  templateUrl: './admin-test-result.component.html',
  styleUrls: ['./admin-test-result.component.scss'],
})
export class AdminTestResultComponent {
  @Input() testResult: UserTestForAdminResponse | null = null;

  selectedChoices: string[] = [
    'ANSWER_A',
    'ANSWER_B',
    'ANSWER_C',
    'ANSWER_D',
    'ANSWER_E',
  ];
  constructor(private testService: TestService) {}

  formatAnswerDistribution(answerDistribution: any) {
    return this.testService.formatAnswerDistribution(answerDistribution);
  }

  getAnswerType(index: number): string {
    return this.selectedChoices[index];
  }
}
