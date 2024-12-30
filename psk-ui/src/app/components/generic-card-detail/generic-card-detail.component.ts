import {
  Component,
  Input,
  Output,
  EventEmitter,
  OnInit,
  OnChanges,
  SimpleChanges,
} from '@angular/core';
import { BlogRequest, BlogResponse } from 'src/app/services/models';
import { HttpClient } from '@angular/common/http';
import { CommonService } from 'src/app/custom-services/common-service/common.service';

@Component({
  selector: 'app-generic-card-detail',
  templateUrl: './generic-card-detail.component.html',
  styleUrls: ['./generic-card-detail.component.scss'],
})
export class GenericCardDetailComponent implements OnInit, OnChanges {
  @Input() blogId: number | null = null;
  @Input() text: string = '';
  @Input() title: string = '';
  @Input() imageUrl: string = '';
  @Input() subTitle: string = '';
  @Input() shareable: boolean = true;

  @Input() isTextCenter: boolean = false;

  @Input() isEditable: boolean = false;

  @Input() isBlogEditable: boolean = false;

  @Output() endEditingEvent = new EventEmitter<void>();

  blogCard: BlogResponse | null = null;

  blob: Blob | null = null;

  selectedFile: File | null = null;

  constructor(private commonService: CommonService) {}

  ngOnInit() {
    this.initializeBlogCard();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (
      changes['blogId'] ||
      changes['title'] ||
      changes['text'] ||
      changes['imageUrl'] ||
      changes['subTitle'] ||
      changes['shareable']
    ) {
      this.initializeBlogCard();
    }
  }

  private initializeBlogCard() {
    this.blogCard = {
      id: this.blogId ?? undefined,
      imageUrl: this.imageUrl ?? undefined,
      shareable: this.shareable,
      subTitle: this.subTitle,
      text: this.text,
      title: this.title,
    };
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.imageUrl = e.target.result;
        this.updateBlogCard();
      };
      reader.readAsDataURL(file);
    }
  }

  uploadImage() {
    // Replace the button click with file input click
    const fileInput = document.createElement('input');
    fileInput.type = 'file';
    fileInput.accept = 'image/*';
    fileInput.onchange = (e) => this.onFileSelected(e);
    fileInput.click();
  }

  updateShareable(event: any) {
    this.shareable = event.target.value === 'true';
    this.updateBlogCard();
  }

  updateTitle(event: any) {
    this.title = event.target.value;
    this.updateBlogCard();
  }

  updateText(event: any) {
    this.text = event.target.value;
    this.updateBlogCard();
  }

  saveBlog() {
    const formData = new FormData();
    if (this.title) {
      formData.append('title', this.title);
    }
    if (this.subTitle) {
      formData.append('subTitle', this.subTitle);
    }
    if (this.text) {
      formData.append('text', this.text);
    }
    formData.append('shareable', String(this.shareable));

    if (this.selectedFile instanceof File) {
      formData.append('image', this.selectedFile);
    }
    if (!this.blogCard?.id) {
      this.commonService.saveBlog(formData).subscribe({
        next: (response) => {
          console.log('Save success:', response);
          this.isBlogEditable = false;
          this.endEditingEvent.emit();
        },
        error: (error) => {
          console.log('Save error:', error);
        },
      });
    } else {
      this.commonService.updateBlog(this.blogCard.id, formData).subscribe({
        next: (response) => {
          console.log('Update success:', response);
          this.isBlogEditable = false;
          this.endEditingEvent.emit();
        },
        error: (error) => {
          console.log('Update error:', error);
        },
      });
    }
  }

  updateSubTitle(event: any) {
    this.subTitle = event.target.value;
    this.updateBlogCard();
  }

  updateBlogCard() {
    this.initializeBlogCard();
  }
}
