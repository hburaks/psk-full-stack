import { Component, Input, Output, EventEmitter } from '@angular/core';
import {
  AdminTestCommentRequest,
  AdminTestCommentResponse,
} from 'src/app/services/models';

@Component({
  selector: 'app-edit-comment',
  templateUrl: './edit-comment.component.html',
  styleUrls: ['./edit-comment.component.scss'],
})
export class EditCommentComponent {
  @Input() comments?: Array<AdminTestCommentResponse>;

  @Output() updateModalEvent = new EventEmitter<AdminTestCommentResponse[]>();

  addComment() {
    this.comments = this.comments || [];
    this.comments.push({
      title: '',
      text: '',
      score: 0,
      imageUrl: '',
    });
    this.updateModal();
  }

  removeComment(index: number) {
    this.comments?.splice(index, 1);
    this.updateModal();
  }

  updateModal() {
    this.updateModalEvent.emit(this.comments);
  }

  onFileChange(event: any, index: number) {
    //TODO console.log('onFileChange', event);
    this.updateModal();
  }

  onCommentChange(index: number) {
    this.updateModal();
  }
}
