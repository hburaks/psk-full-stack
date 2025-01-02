import { Component, OnInit } from '@angular/core';
import { PublicTestResponse } from 'src/app/services/models';
import { TestService } from 'src/app/services/services';

@Component({
  selector: 'app-test',
  templateUrl: './test.component.html',
  styleUrls: ['./test.component.scss'],
})
export class TestComponent implements OnInit {
  testCardList: PublicTestResponse[] = [];

  test: PublicTestResponse = {
    imageUrl: './assets/materials/logo-ex.svg',
    id: 1,
    questions: [],
    subTitle: 'Test kartı için oluşturulmuş kısa bir örnek alt başlık içeriği.',
    title: 'Test',
  };

  fetchedTestList: PublicTestResponse[] = [this.test, this.test, this.test];

  constructor(private testServiceV3: TestService) {}

  ngOnInit(): void {
    this.getTestList();
  }

  getTestList() {
    this.testServiceV3.getAllPublicTests().subscribe({
      next: (publicTests: Array<PublicTestResponse>) => {
        this.fetchedTestList = publicTests || [];
        if (this.fetchedTestList.length !== 0) {
          this.testCardList = this.fetchedTestList;
        }
      },
      error: (err) => {
        console.error('Error fetching public tests', err);
      },
    });
  }
}
