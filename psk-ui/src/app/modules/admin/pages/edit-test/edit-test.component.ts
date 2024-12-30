import { Component } from '@angular/core';
import { PublicTestResponse } from 'src/app/services/models';
import { TestService } from 'src/app/services/services';

@Component({
  selector: 'app-edit-test',
  templateUrl: './edit-test.component.html',
  styleUrls: ['./edit-test.component.scss'],
})
export class EditTestComponent {
  isEditingTest: boolean = false;
  testToEdit: PublicTestResponse | null = null;

  constructor(private testService: TestService) {}

  editTest(test: PublicTestResponse) {
    this.testToEdit = test;
    this.isEditingTest = true;
  }

  addTest() {
    this.testToEdit = {
      id: undefined,
      title: '',
      subTitle: '',
      imageUrl: '',
      questions: [],
    };
    this.isEditingTest = true;
  }

  closeModal() {
    this.isEditingTest = false;
    this.testToEdit = null;
  }
}
