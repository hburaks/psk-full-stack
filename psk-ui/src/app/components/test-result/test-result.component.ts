import { Component, Input } from '@angular/core';
import { PublicTestAnswerCommentResponse } from 'src/app/services/models/public-test-answer-comment-response';

@Component({
  selector: 'app-test-result',
  templateUrl: './test-result.component.html',
  styleUrls: ['./test-result.component.scss'],
})
export class TestResultComponent {
  @Input() testResult!: PublicTestAnswerCommentResponse;
  text: string = '';
  title: string = '';
  cover: string[] = [];

  ngOnInit(): void {
    this.text = this.testResult.text ?? '';
    this.title = this.testResult.title ?? '';
    this.cover = this.testResult.cover ?? [];
  }
}
