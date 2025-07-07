import {Component, Input, OnInit} from '@angular/core';
import {UserTestListResponse} from 'src/app/services/models/user-test-list-response';
import {UserTestAdminService} from 'src/app/services/services';
import {UserAnswerResponse} from 'src/app/services/models/user-answer-response';

interface AdminTestResultData extends UserTestListResponse {
  userAnswers?: UserAnswerResponse[];
  score?: number;
  adminComment?: string;
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

  // Check if all answers are within the same minute or ±1 minute
  allAnswersHaveSameTime(): boolean {
    if (!this.testResult?.userAnswers || this.testResult.userAnswers.length <= 1) {
      return false;
    }

    const times = this.testResult.userAnswers
      .map(answer => answer.answeredAt)
      .filter(time => time)
      .map(time => new Date(time!));

    if (times.length === 0) return false;

    const minTime = new Date(Math.min(...times.map(t => t.getTime())));
    const maxTime = new Date(Math.max(...times.map(t => t.getTime())));

    // Check if all times are within 1 minute (60000 milliseconds)
    return (maxTime.getTime() - minTime.getTime()) <= 60000;
  }

  // Get formatted time range for display
  getCommonResponseTime(): string | null {
    if (!this.testResult?.userAnswers || this.testResult.userAnswers.length === 0) {
      return null;
    }

    const times = this.testResult.userAnswers
      .map(answer => answer.answeredAt)
      .filter(time => time)
      .map(time => new Date(time!));

    if (times.length === 0) return null;

    const minTime = new Date(Math.min(...times.map(t => t.getTime())));
    const maxTime = new Date(Math.max(...times.map(t => t.getTime())));

    // If exactly the same time, return single time
    if (minTime.getTime() === maxTime.getTime()) {
      return minTime.toISOString();
    }

    // If within same minute, show the minute
    if (minTime.getFullYear() === maxTime.getFullYear() &&
      minTime.getMonth() === maxTime.getMonth() &&
      minTime.getDate() === maxTime.getDate() &&
      minTime.getHours() === maxTime.getHours() &&
      minTime.getMinutes() === maxTime.getMinutes()) {
      return minTime.toISOString();
    }

    // Return time range as formatted string
    return `${this.formatDateTime(minTime)} - ${this.formatDateTime(maxTime)}`;
  }

  private formatDateTime(date: Date): string {
    return date.toLocaleDateString('tr-TR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }
}
