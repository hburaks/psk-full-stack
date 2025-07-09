import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { FileControllerService } from 'src/app/services/services';

interface AdminTestCommentRequest {
  commentId?: number;
  text?: string;
  score?: number;
  title?: string;
}

interface AdminTestCommentResponse {
  commentId?: number;
  text?: string;
  score?: number;
  title?: string;
  imageUrl?: string;
}

@Component({
  selector: 'app-edit-comment',
  templateUrl: './edit-comment.component.html',
  styleUrls: ['./edit-comment.component.scss'],
})
export class EditCommentComponent {
  @Input() comments?: Array<AdminTestCommentResponse>;

  @Output() updateModalEvent = new EventEmitter<AdminTestCommentResponse[]>();

  @Output() commentsToUpdate = new EventEmitter<AdminTestCommentResponse[]>();



  constructor(
    private fileService: FileControllerService
  ) {}


  commentListResponseToRequest(
    comments: AdminTestCommentResponse[]
  ): AdminTestCommentRequest[] {
    return comments.map((comment) => ({
      commentId: comment.commentId,
      text: comment.text,
      score: comment.score,
      title: comment.title,
    }));
  }

  addComment() {
    this.comments = this.comments || [];
    this.comments.push({
      title: '',
      text: '',
      score: 0,
    });
    this.updateModal();
  }

  removeComment(index: number) {
    this.comments?.splice(index, 1);
    this.updateModal();
  }

  updateModal() {
    this.updateModalEvent.emit(this.comments);
    this.commentsToUpdate.emit(this.comments);
  }

  uploadImage(index: number) {
    const fileInput = document.createElement('input');
    fileInput.type = 'file';
    fileInput.accept = 'image/*';
    fileInput.onchange = (e) => this.onFileSelected(e, index);
    fileInput.click();
  }

  async onFileSelected(event: any, index: number) {
    const file: File = event.target.files[0];

    if (file) {
      this.uploadCommentImageToServer(index, file);
    }
  }

  uploadCommentImageToServer(index: number, file: File) {
    if (file) {
      const reader = new FileReader();
      reader.onload = () => {
        this.comments![index].imageUrl = reader.result as string;
        this.updateModal();
      };
      reader.readAsDataURL(file);
    }
  }

  toAdminTestCommentRequest(comment: AdminTestCommentResponse) {
    return {
      commentId: comment.commentId,
      text: comment.text,
      score: comment.score,
      title: comment.title,
    };
  }

  onCommentChange(index: number) {
    this.updateModal();
  }
}
