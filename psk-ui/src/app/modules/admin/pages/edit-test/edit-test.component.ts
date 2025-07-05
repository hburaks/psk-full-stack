import { Component } from '@angular/core';
import { PublicTestResponse, TestTemplateResponse } from 'src/app/services/models';
import { TestService, TestTemplateAdminService } from 'src/app/services/services';

@Component({
  selector: 'app-edit-test',
  templateUrl: './edit-test.component.html',
  styleUrls: ['./edit-test.component.scss'],
})
export class EditTestComponent {
  isEditingTest: boolean = false;
  testToEdit: PublicTestResponse | null = null;
  testTemplateToEdit: TestTemplateResponse | null = null;

  constructor(private testService: TestService, private testTemplateAdminService: TestTemplateAdminService) {}

  editTest(test: PublicTestResponse) {
    this.testToEdit = test;
    this.isEditingTest = true;
  }

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
    this.testToEdit = null;
    this.testTemplateToEdit = null;
  }
}
