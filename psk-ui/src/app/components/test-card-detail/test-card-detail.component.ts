import {AfterViewInit, Component, EventEmitter, Input, Output,} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {
  Choice,
  QuestionResponse,
  ScoringStrategyResponse,
  TestTemplateResponse,
  TestTemplateUpdateRequest,
} from 'src/app/services/models';
import {QuestionAdminService, ScoringAdminService, TestTemplateAdminService} from 'src/app/services/services';

@Component({
  selector: 'app-test-card-detail',
  templateUrl: './test-card-detail.component.html',
  styleUrls: ['./test-card-detail.component.scss'],
})
export class TestCardDetailComponent implements AfterViewInit {
  @Input() testCardList: TestTemplateResponse[] = [];
  @Input() isEditPage: boolean = false;

  cardId: number = this.route.snapshot.params['id'];
  @Input() editableTestTemplate: TestTemplateResponse | null = null;

  @Output() closeModalEvent = new EventEmitter<void>();

  updateTestTemplate: TestTemplateUpdateRequest = {} as TestTemplateUpdateRequest;

  testTemplate: TestTemplateResponse | null = null;

  testResult: any = null;
  testCard: TestTemplateResponse | null = null;

  title: string = 'Psikolojik Testler';
  text: string =
    'Kendinizi daha iyi tanımak için tasarlanmış çeşitli psikolojik testlerimizi deneyebilirsiniz. Bu testler kişilik özellikleri, duygusal durum ve davranış kalıplarınız hakkında içgörü kazanmanıza yardımcı olacaktır.';

  selectedFile: File | null = null;
  @Input() imageUrl: string = '';

  // Question management for test templates
  templateQuestions: QuestionResponse[] = [];
  showQuestionManagement: boolean = false;

  // Strategy management
  availableStrategies: ScoringStrategyResponse[] = [];
  selectedStrategy: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private testTemplateAdminService: TestTemplateAdminService,
    private questionAdminService: QuestionAdminService,
    private scoringAdminService: ScoringAdminService
  ) {
    this.cardId = this.route.snapshot.params['id'];
  }

  ngAfterViewInit() {
    if (!this.editableTestTemplate && !this.isEditPage) {
      this.getTestTemplateDetail();
    }

    if (this.editableTestTemplate) {
      this.updateTestTemplate = {
        title: this.editableTestTemplate?.title || '',
        subTitle: this.editableTestTemplate?.subTitle,
        imageUrl: this.editableTestTemplate?.imageUrl,
        isActive: this.editableTestTemplate?.isActive,
        scoringStrategy: this.editableTestTemplate?.scoringStrategy,
      };
      this.selectedStrategy = this.editableTestTemplate?.scoringStrategy || null;
      this.testCard = this.editableTestTemplate;
    }

    // Load available strategies
    this.loadStrategies();
  }

  getTestTemplateDetail() {
    this.testTemplateAdminService.getTestTemplateById1({ id: this.cardId }).subscribe({
      next: (template: TestTemplateResponse) => {
        this.testTemplate = template;
        this.testCard = template;
      },
      error: (err: any) => {
        console.error('Error fetching test template', err);
      },
    });
  }

  saveOrUpdateTestInfo() {
    this.saveOrUpdateTestTemplateInfo();
  }

  saveAllChanges() {
    // Update the scoring strategy in the update request
    if (this.selectedStrategy) {
      this.updateTestTemplate.scoringStrategy = this.selectedStrategy as 'SIMPLE_LINEAR' | 'WEIGHTED' | 'PERCENTAGE';
    }

    if (this.editableTestTemplate?.id) {
      // UPDATE existing template
      const params = {
        id: this.editableTestTemplate.id,
        body: this.updateTestTemplate,
      };
      this.testTemplateAdminService.updateTestTemplate(params).subscribe({
        next: (response: TestTemplateResponse) => {
          // After successful template update, upload image if selected
          if (this.selectedFile && this.editableTestTemplate?.id) {
            this.uploadSelectedImage();
          } else {
            this.closeModalEvent.emit();
          }
        },
        error: (err: any) => {
          console.error('Error updating test template', err);
        },
      });
    } else {
      // CREATE new template
      const params = {
        body: this.updateTestTemplate,
      };
      this.testTemplateAdminService.createTestTemplate(params).subscribe({
        next: (response: TestTemplateResponse) => {
          // After successful template creation, upload image if selected
          if (this.selectedFile && response.id) {
            this.uploadImageForNewTemplate(response.id);
          } else {
            this.closeModalEvent.emit();
          }
        },
        error: (err: any) => {
          console.error('Error creating test template', err);
        },
      });
    }
  }

  saveOrUpdateTestTemplateInfo() {
    if (this.editableTestTemplate?.id) {
      // UPDATE existing template
      const params = {
        id: this.editableTestTemplate.id,
        body: this.updateTestTemplate,
      };
      this.testTemplateAdminService.updateTestTemplate(params).subscribe({
        next: (response: TestTemplateResponse) => {
          this.closeModalEvent.emit();
        },
        error: (err: any) => {
          console.error('Error updating test template', err);
        },
      });
    } else {
      // CREATE new template
      this.saveTestTemplate();
    }
  }

  saveTestTemplate() {
    const params = {
      body: this.updateTestTemplate,
    };
    this.testTemplateAdminService.createTestTemplate(params).subscribe({
      next: (response: TestTemplateResponse) => {
        this.closeModalEvent.emit();
      },
      error: (err: any) => {
        console.error('Error creating test template', err);
      },
    });
  }

  navigateToQuestionManagement() {
    if (this.editableTestTemplate?.id) {
      this.loadTemplateQuestions(this.editableTestTemplate.id);
      this.showQuestionManagement = true;
    }
  }

  loadTemplateQuestions(testTemplateId: number) {
    this.questionAdminService.getQuestionsByTestTemplate({ testTemplateId }).subscribe({
      next: (questions: QuestionResponse[]) => {
        this.templateQuestions = questions || [];
      },
      error: (err: any) => {
        console.error('Error loading template questions', err);
      },
    });
  }

  addTemplateQuestion() {
    const newQuestion: QuestionResponse = {
      text: '',
      testTemplateId: this.editableTestTemplate?.id,
      orderIndex: this.templateQuestions.length,
      choices: [
        { text: '', answerType: 'ANSWER_A' },
        { text: '', answerType: 'ANSWER_B' },
        { text: '', answerType: 'ANSWER_C' },
        { text: '', answerType: 'ANSWER_D' },
        { text: '', answerType: 'ANSWER_E' },
      ],
    };
    this.templateQuestions.push(newQuestion);
  }

  removeTemplateQuestion(index: number) {
    this.templateQuestions.splice(index, 1);
  }

  addTemplateChoice(questionIndex: number) {
    const question = this.templateQuestions[questionIndex];
    if (question.choices && question.choices.length < 5) {
      const nextAnswerType = ['ANSWER_A', 'ANSWER_B', 'ANSWER_C', 'ANSWER_D', 'ANSWER_E'][question.choices.length] as Choice['answerType'];
      question.choices.push({
        text: '',
        answerType: nextAnswerType,
      });
    }
  }

  removeTemplateChoice(questionIndex: number, choiceIndex: number) {
    const question = this.templateQuestions[questionIndex];
    if (question.choices && question.choices.length > 2) {
      question.choices.splice(choiceIndex, 1);
    }
  }

  saveTemplateQuestions() {
    this.showQuestionManagement = false;
    alert('Question saving functionality will be implemented based on the specific API requirements.');
  }

  loadStrategies() {
    this.scoringAdminService.getAllScoringStrategies().subscribe({
      next: (strategies: ScoringStrategyResponse[]) => {
        this.availableStrategies = strategies;
      },
      error: (err: any) => {
        console.error('Error loading strategies', err);
      },
    });
  }

  uploadImage() {
    const fileInput = document.createElement('input');
    fileInput.type = 'file';
    fileInput.accept = 'image/*';
    fileInput.onchange = (e) => this.onFileSelected(e);
    fileInput.click();
  }

  async onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.imageUrl = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  }

  uploadSelectedImage() {
    if (!this.selectedFile || !this.editableTestTemplate?.id) {
      console.error('No file selected or template ID missing');
      return;
    }

    this.testTemplateAdminService.uploadImage({
      testTemplateId: this.editableTestTemplate.id,
      body: {file: this.selectedFile}
    }).subscribe({
      next: (response) => {
        console.log('Image uploaded successfully', response);
        this.closeModalEvent.emit();
      },
      error: (error) => {
        console.error('Error uploading image:', error);
        alert('Görsel yüklenirken bir hata oluştu. Lütfen tekrar deneyin.');
      }
    });
  }

  uploadImageForNewTemplate(templateId: number) {
    if (!this.selectedFile) {
      console.error('No file selected');
      return;
    }

    this.testTemplateAdminService.uploadImage({
      testTemplateId: templateId,
      body: {file: this.selectedFile}
    }).subscribe({
      next: (response) => {
        console.log('Image uploaded successfully', response);
        this.closeModalEvent.emit();
      },
      error: (error) => {
        console.error('Error uploading image:', error);
        alert('Görsel yüklenirken bir hata oluştu. Lütfen tekrar deneyin.');
        this.closeModalEvent.emit();
      }
    });
  }

  getResult() {
    console.log('Test completion not implemented for TestTemplate workflow');
  }
}
