import {Component, Input, OnInit} from '@angular/core';
import {UserTestListResponse} from 'src/app/services/models/user-test-list-response';
import {UserTestAdminService} from 'src/app/services/services';
import {UserAnswerResponse} from 'src/app/services/models/user-answer-response';

interface AdminTestResultData extends UserTestListResponse {
  userAnswers?: UserAnswerResponse[];
}

@Component({
  selector: 'app-admin-test-result',
  templateUrl: './admin-test-result.component.html',
  styleUrls: ['./admin-test-result.component.scss'],
})
export class AdminTestResultComponent implements OnInit {
  @Input() testResult: AdminTestResultData | null = null;

  selectedChoices: string[] = [
    'ANSWER_A',
    'ANSWER_B',
    'ANSWER_C',
    'ANSWER_D',
    'ANSWER_E',
  ];

  constructor(private userTestAdminService: UserTestAdminService) {
  }

  ngOnInit(): void {
    if (this.testResult && this.testResult.id) {
      this.userTestAdminService.getUserTestAnswers({userTestId: this.testResult.id}).subscribe({
        next: (answers: UserAnswerResponse[]) => {
          if (this.testResult) {
            this.testResult.userAnswers = answers;
          }
        },
        error: (err: any) => {
          console.error('Failed to fetch user answers', err);
        }
      });
    }
  }

  formatAnswerDistribution(answerDistribution: any) {
    // Legacy formatting method - simplified implementation
    if (!answerDistribution) return {};
    return answerDistribution;
  }

  getAnswerType(index: number): string {
    return this.selectedChoices[index];
  }
}
