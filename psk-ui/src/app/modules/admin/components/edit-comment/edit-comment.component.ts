import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import {
  AdminTestCommentRequest,
  AdminTestCommentResponse,
} from 'src/app/services/models';
import { FileControllerService, TestService } from 'src/app/services/services';

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
    private testService: TestService,
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
      reader.readAsDataURL(file);
      this.testService
        .uploadImageForComment({
          commentId: this.comments![index].commentId!,
          body: { file: file },
        })
        .subscribe({
          next: (response) => {
            reader.onload = () => {
              this.comments![index].imageUrl = reader.result as string;
            };
            reader.readAsDataURL(file);
          },
          error: (error) => {
            console.error('Error uploading image:', error);
          },
        });
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
