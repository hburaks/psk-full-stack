import { Component, OnInit } from '@angular/core';
import { TestTemplateResponse } from 'src/app/services/models';
import { TestTemplateAdminService } from 'src/app/services/services';

@Component({
  selector: 'app-test',
  templateUrl: './test.component.html',
  styleUrls: ['./test.component.scss'],
})
export class TestComponent implements OnInit {
  testCardList: TestTemplateResponse[] = [];

  test: TestTemplateResponse = {
    id: 1,
    title: 'Test',
    subTitle: 'Test kartı için oluşturulmuş kısa bir örnek alt başlık içeriği.',
    createdDate: '',
    lastModifiedDate: ''
  };

  fetchedTestList: TestTemplateResponse[] = [this.test, this.test, this.test];

  constructor(private testTemplateAdminService: TestTemplateAdminService) {}

  ngOnInit(): void {
    this.getTestList();
  }

  getTestList() {
    this.testTemplateAdminService.getAllTestTemplates().subscribe({
      next: (testTemplates: Array<TestTemplateResponse>) => {
        this.fetchedTestList = testTemplates || [];
        if (this.fetchedTestList.length !== 0) {
          this.testCardList = this.fetchedTestList;
        }
      },
      error: (err) => {
        console.error('Error fetching test templates', err);
      },
    });
  }
}
