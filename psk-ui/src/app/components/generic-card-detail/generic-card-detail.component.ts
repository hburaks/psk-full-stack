import { Component, Input, Output, EventEmitter } from '@angular/core';
import { SaveBlog$Params } from 'src/app/services/fn/blog/save-blog';
import { BlogResponse } from 'src/app/services/models';
import { BlogService } from 'src/app/services/services';

@Component({
  selector: 'app-generic-card-detail',
  templateUrl: './generic-card-detail.component.html',
  styleUrls: ['./generic-card-detail.component.scss'],
})
export class GenericCardDetailComponent {
  @Input() blogId: number | null = null;
  @Input() text: string = '';
  @Input() title: string = '';
  @Input() cover: string[] = [];
  @Input() subTitle: string = '';
  @Input() shareable: boolean = true;

  @Input() isTextCenter: boolean = false;

  @Input() isEditable: boolean = false;

  @Input() isBlogEditable: boolean = false;

  @Output() endEditingEvent = new EventEmitter<void>();

  blogCard: BlogResponse | null = null;

  blob: Blob | null = null;

  constructor(private blogService: BlogService) {}

  uploadImage() {
    // TODO: Implement image upload
    //this.cover = ['https://i.ibb.co/WD8V52c/empty-card.png'];
    this.updateBlogCard();
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
    if (!this.blogCard?.id) {
      const blogParams: any = {};

      if (this.cover) blogParams.cover = this.cover;
      if (this.shareable !== undefined) blogParams.shareable = this.shareable;
      if (this.subTitle) blogParams.subTitle = this.subTitle;
      if (this.text) blogParams.text = this.text;
      if (this.title) blogParams.title = this.title;

      const saveBlogParams: SaveBlog$Params = {
        body: blogParams,
      };

      this.blogService.saveBlog(saveBlogParams).subscribe({
        next: (response) => {
          this.isBlogEditable = false;
          this.endEditingEvent.emit();
        },
        error: (error) => {
          console.log(error);
        },
      });
    } else {
      const blogParams: any = {};

      if (this.cover) blogParams.cover = this.cover;
      if (this.shareable !== undefined) blogParams.shareable = this.shareable;
      if (this.subTitle) blogParams.subTitle = this.subTitle;
      if (this.text) blogParams.text = this.text;
      if (this.title) blogParams.title = this.title;

      this.blogService
        .updateBlog({ id: this.blogCard.id, body: blogParams })
        .subscribe({
          next: (response) => {
            this.isBlogEditable = false;
            this.endEditingEvent.emit();
          },
        });
    }
  }

  updateSubTitle(event: any) {
    this.subTitle = event.target.value;
    this.updateBlogCard();
  }

  updateBlogCard() {
    this.blogCard = {
      id: this.blogId ?? undefined,
      cover: this.cover,
      shareable: this.shareable,
      subTitle: this.subTitle,
      text: this.text,
      title: this.title,
    };
  }
}
