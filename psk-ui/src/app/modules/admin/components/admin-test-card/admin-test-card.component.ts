import {
  Component,
  EventEmitter,
  HostListener,
  Input,
  Output,
} from '@angular/core';
import { TestService as CustomTestService } from 'src/app/custom-services/test/test.service';
import { TestService as TestApiService } from 'src/app/services/services/test.service';
import { UserTestForAdminResponse } from 'src/app/services/models/user-test-for-admin-response';
import { ToastComponent } from 'src/app/components/toast/toast.component';
import { PublicTestResponse } from 'src/app/services/models';
import { TokenService } from 'src/app/custom-services/token/token.service';

@Component({
  selector: 'app-admin-test-card',
  templateUrl: './admin-test-card.component.html',
  styleUrls: ['./admin-test-card.component.scss'],
})
export class AdminTestCardComponent {
  @Input() testResults: UserTestForAdminResponse[] = [];
  @Input() publicTests: PublicTestResponse[] = [];

  @Output() testAddedEvent =
    new EventEmitter<UserTestForAdminResponse | null>();

  isResultOpen: boolean = false;

  selectedTest: UserTestForAdminResponse | null = null;

  errorMessage: string = '';
  showToast: boolean = false;

  @Input() userId: number | null | undefined = null;

  constructor(
    private customTestService: CustomTestService,
    private testApiService: TestApiService
  ) {}

  ngAfterViewInit() {
    this.testResults.sort((a, b) => {
      const dateA = new Date(a.lastModifiedDate || 0).getTime();
      const dateB = new Date(b.lastModifiedDate || 0).getTime();
      return dateB - dateA; 
    });
  }

  seeResult(testId: number) {
    const selectedTest = this.testResults.find(
      (test) => test.testId === testId
    );
    if (selectedTest) {
      this.selectedTest = selectedTest;
      this.isResultOpen = true;
    }
  }

  formatAnswerDistribution(answerDistribution: any) {
    return this.customTestService.formatAnswerDistribution(answerDistribution);
  }

  closeResult() {
    this.isResultOpen = false;
  }

  removeTestFromUser(testId: number) {
    this.testApiService.removeTestFromUserV2({ testId }).subscribe({
      next: (response) => {
        this.testResults = this.testResults.filter(
          (test) => test.testId !== testId
        );
      },
      error: (error) => {
        this.errorMessage = 'Test silinirken bir hata oluştu';
        this.showToast = true;
      },
    });
  }

  addTestToTestResults(testId: number) {
    const test = this.publicTests.find((test) => test.id === testId);
    if (test) {
      const mappedTest: UserTestForAdminResponse = {
        testId: testId,
        title: test.title,
        subTitle: test.subTitle,
        cover: test.cover,
        questions: test.questions,
      };
      this.testAddedEvent.emit(mappedTest);
    }
  }

  addTestToUser(testId: number) {
    if (this.userId) {
      this.testApiService
        .assignTestToUserV2({ testId, userId: this.userId })
        .subscribe({
          next: (response) => {
            this.addTestToTestResults(testId);

            this.publicTests = this.publicTests.filter(
              (test) => test.id !== testId
            );
          },
          error: (error) => {
            this.errorMessage = 'Test atanırken bir hata oluştu';
            this.showToast = true;
          },
        });
    }
  }
}
