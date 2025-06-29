import {Component, Input} from '@angular/core';
import {PublicTestAnswerCommentResponse} from 'src/app/services/models/public-test-answer-comment-response';
import {FileControllerService} from 'src/app/services/services';

@Component({
  selector: 'app-test-result',
  templateUrl: './test-result.component.html',
  styleUrls: ['./test-result.component.scss'],
})
export class TestResultComponent {
  @Input() testResult!: PublicTestAnswerCommentResponse | null;
  text: string = '';
  title: string = '';
  imageUrl: string = '';

  constructor(private fileService: FileControllerService) {}

  ngOnInit(): void {
    if (this.testResult) {
    this.text = this.testResult.text ?? '';
    this.title = this.testResult.title ?? '';
    this.imageUrl = this.testResult.imageUrl ?? '';
    }
  }

}
