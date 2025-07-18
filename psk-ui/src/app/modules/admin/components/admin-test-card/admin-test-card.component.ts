import {Component, EventEmitter, Input, Output,} from '@angular/core';
import {TestService as CustomTestService} from 'src/app/custom-services/test/test.service';
import {AssignTestRequest, TestTemplateResponse, UserTestListResponse} from 'src/app/services/models';
import {UserTestAdminService} from 'src/app/services/services';

@Component({
  selector: 'app-admin-test-card',
  templateUrl: './admin-test-card.component.html',
  styleUrls: ['./admin-test-card.component.scss'],
})
export class AdminTestCardComponent {
  @Input() testResults: UserTestListResponse[] = [];
  @Input() testTemplates: TestTemplateResponse[] = [];

  @Output() testAddedEvent =
    new EventEmitter<UserTestListResponse | null>();

  isResultOpen: boolean = false;

  selectedTest: UserTestListResponse | null = null;

  errorMessage: string = '';
  showToast: boolean = false;

  @Input() userId: number | null | undefined = null;

  constructor(
    private customTestService: CustomTestService,
    private userTestAdminService: UserTestAdminService
  ) {}

  ngAfterViewInit() {
    this.testResults.sort((a, b) => {
      const dateA = new Date(a.assignedAt || 0).getTime();
      const dateB = new Date(b.assignedAt || 0).getTime();
      return dateB - dateA;
    });
  }

  seeResult(testId: number) {
    const selectedTest = this.testResults.find(
      (test) => test.id === testId
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
    this.userTestAdminService.deleteUserTest({userTestId: testId}).subscribe({
      next: () => {
        // Remove from UI after successful deletion
        this.testResults = this.testResults.filter(
          (test) => test.id !== testId
        );
      },
      error: (error) => {
        this.errorMessage = 'Test kaldırılırken bir hata oluştu';
        this.showToast = true;
      }
    });
  }

  addTestTemplateToUser(testTemplateId: number) {
    if (this.userId) {
      const assignRequest: AssignTestRequest = {
        testTemplateId: testTemplateId,
        userId: this.userId
      };

      this.userTestAdminService.assignTest({ body: assignRequest }).subscribe({
        next: (response) => {
          // Create a UserTestForAdminResponse from the test template for UI consistency
          const template = this.testTemplates.find((t) => t.id === testTemplateId);
          if (template) {
            const mappedTest: UserTestListResponse = {
              id: response.id, // Use the UserTest ID, not the template ID
              testTemplateTitle: template.title,
              testTemplateSubTitle: template.subTitle,
              testTemplateImageUrl: template.imageUrl,
              userId: this.userId || undefined,
            };
            this.testAddedEvent.emit(mappedTest);

            // Remove the template from available templates
            this.testTemplates = this.testTemplates.filter(
              (t) => t.id !== testTemplateId
            );
          }
        },
        error: (error) => {
          this.errorMessage = 'Test şablonu atanırken bir hata oluştu';
          this.showToast = true;
        },
      });
    }
  }
}
