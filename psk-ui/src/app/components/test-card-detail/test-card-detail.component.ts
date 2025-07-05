import {
  AfterViewInit,
  Component,
  Input,
  Output,
  EventEmitter,
} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {
  TestTemplateResponse,
  TestTemplateUpdateRequest,
  QuestionResponse,
  Choice,
} from 'src/app/services/models';
import { TestTemplateAdminService, QuestionAdminService } from 'src/app/services/services';

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

  // Properties for template compatibility
  testResult: any = null;
  editableTestCard: any = null;
  updateTestCard: any = null;
  testCard: TestTemplateResponse | null = null;
  selectedChoices: number[] = [];

  title: string = 'Psikolojik Testler';
  text: string =
    'Kendinizi daha iyi tanımak için tasarlanmış çeşitli psikolojik testlerimizi deneyebilirsiniz. Bu testler kişilik özellikleri, duygusal durum ve davranış kalıplarınız hakkında içgörü kazanmanıza yardımcı olacaktır.';

  selectedFile: File | null = null;
  @Input() imageUrl: string = '';

  // Question management for test templates
  templateQuestions: QuestionResponse[] = [];
  showQuestionManagement: boolean = false;

  constructor(
    private route: ActivatedRoute, 
    private testTemplateAdminService: TestTemplateAdminService,
    private questionAdminService: QuestionAdminService
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
      };
      // Set testCard for template compatibility
      this.testCard = this.editableTestTemplate;
    }
  }

  getTestTemplateDetail() {
    this.testTemplateAdminService.getTestTemplateById({ id: this.cardId }).subscribe({
      next: (template: TestTemplateResponse) => {
        this.testTemplate = template;
        this.testCard = template; // Set for template compatibility
        this.imageUrl = template.imageUrl || '';
      },
      error: (err: any) => {
        console.error('Error fetching test template', err);
      },
    });
  }

  saveOrUpdateTestInfo() {
    this.saveOrUpdateTestTemplateInfo();
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

  // Legacy methods for template compatibility
  selectChoice(questionIndex: number, choiceIndex: number, questionId: number) {
    this.selectedChoices[questionIndex] = choiceIndex;
  }

  getResult() {
    // Legacy method - no longer functional in TestTemplate workflow
    console.log('getResult method called - not implemented for TestTemplate workflow');
  }

  // Legacy question/choice management methods (no-op for TestTemplate)
  addQuestion() {
    console.log('addQuestion method called - use template question management instead');
  }

  removeQuestion(index: number) {
    console.log('removeQuestion method called - use template question management instead');
  }

  addChoice(questionIndex: number) {
    console.log('addChoice method called - use template question management instead');
  }

  removeChoice(questionIndex: number, choiceIndex: number) {
    console.log('removeChoice method called - use template question management instead');
  }

  updateTestQuestions() {
    console.log('updateTestQuestions method called - use template question management instead');
  }

  updateComments(event: any) {
    console.log('updateComments method called - not implemented for TestTemplate workflow');
  }

  updateTestComments() {
    console.log('updateTestComments method called - not implemented for TestTemplate workflow');
  }
}
