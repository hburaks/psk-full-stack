import { Component } from '@angular/core';
import { TestTemplateResponse } from 'src/app/services/models';
import { TestTemplateAdminService } from 'src/app/services/services';

@Component({
  selector: 'app-edit-test',
  templateUrl: './edit-test.component.html',
  styleUrls: ['./edit-test.component.scss'],
})
export class EditTestComponent {
  isEditingTest: boolean = false;
  testTemplateToEdit: TestTemplateResponse | null = null;

  constructor(private testTemplateAdminService: TestTemplateAdminService) {}

  editTestTemplate(testTemplate: TestTemplateResponse) {
    this.testTemplateToEdit = testTemplate;
    this.isEditingTest = true;
  }

  addTest() {
    this.testTemplateToEdit = {
      id: undefined,
      title: '',
      subTitle: '',
      imageUrl: '',
      isActive: true,
    };
    this.isEditingTest = true;
  }

  closeModal() {
    this.isEditingTest = false;
    this.testTemplateToEdit = null;
  }
}
