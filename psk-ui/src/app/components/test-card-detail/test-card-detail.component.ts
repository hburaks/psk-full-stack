import { Component, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonService } from 'src/app/custom-services/common.service';
import { FindBlogById$Params } from 'src/app/services/fn/blog/find-blog-by-id';
import {
  PublicTestAnswerCommentResponse,
  PublicTestAnswerQuestionRequest,
  PublicTestResponse,
} from 'src/app/services/models';
import { TestService } from 'src/app/services/services';
declare var bootstrap: any;

@Component({
  selector: 'app-test-card-detail',
  templateUrl: './test-card-detail.component.html',
  styleUrls: ['./test-card-detail.component.scss'],
})
export class TestCardDetailComponent {
  @Input() testCardList: PublicTestResponse[] = [];
  cardId: number = this.route.snapshot.params['id'];
  testCard: PublicTestResponse | null = null;
  selectedChoices: { [key: number]: number } = {};
  testResult: PublicTestAnswerCommentResponse | null = null;
  questions?: PublicTestAnswerQuestionRequest[] = [];

  title: string = 'Psikolojik Testler';
  text: string =
    'Kendinizi daha iyi tanımak için tasarlanmış çeşitli psikolojik testlerimizi deneyebilirsiniz. Bu testler kişilik özellikleri, duygusal durum ve davranış kalıplarınız hakkında içgörü kazanmanıza yardımcı olacaktır.';

  constructor(
    private commonService: CommonService,
    private route: ActivatedRoute,
    private testService: TestService
  ) {
    this.cardId = this.route.snapshot.params['id'];

    if (this.commonService.fetchedBlogList.length == 0) {
      this.getTestDetail();
    } else {
      this.getTestDetailFromList();
    }
  }

  getTestDetailFromList() {
    const testDetail = this.commonService.getBlogCardDetail(this.cardId);
    this.testCard = testDetail !== undefined ? testDetail : null;
  }

  getTestDetail() {
    const params: FindBlogById$Params = { id: this.cardId };
    this.testService.getPublicTestById(params).subscribe({
      next: (test: PublicTestResponse) => {
        this.testCard = test;
      },
    });
  }

  selectChoice(questionIndex: number, choiceIndex: number, questionId: number) {
    let chosenAnswer:
      | 'ANSWER_A'
      | 'ANSWER_B'
      | 'ANSWER_C'
      | 'ANSWER_D'
      | 'ANSWER_E';
    if (choiceIndex === 0) chosenAnswer = 'ANSWER_A';
    else if (choiceIndex === 1) chosenAnswer = 'ANSWER_B';
    else if (choiceIndex === 2) chosenAnswer = 'ANSWER_C';
    else if (choiceIndex === 3) chosenAnswer = 'ANSWER_D';
    else chosenAnswer = 'ANSWER_E';
    if (this.questions?.find((q) => q.questionId === questionId)) {
      this.questions = this.questions.map((q) =>
        q.questionId === questionId ? { ...q, chosenAnswer: chosenAnswer } : q
      );
    } else {
      this.questions?.push({
        chosenAnswer: chosenAnswer,
        questionId: questionId,
      });
    }
    this.selectedChoices[questionIndex] = choiceIndex;
  }

  getResult() {
    if (
      this.questions?.length === 0 ||
      this.questions?.length !== this.testCard?.questions?.length
    ) {
      this.showToast();
      return;
    }
    this.testService
      .checkPublicTestAnswer({
        body: { questions: this.questions, testId: this.cardId },
      })
      .subscribe({
        next: (result: PublicTestAnswerCommentResponse) => {
          this.testResult = result;
          console.log(result);
        },
      });
  }

  showToast() {
    const toastElement = document.querySelector('.toast');
    if (toastElement) {
      const bsToast = new bootstrap.Toast(toastElement);
      bsToast.show();
    }
  }
}
