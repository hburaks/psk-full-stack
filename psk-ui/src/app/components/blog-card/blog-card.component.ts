import { Component, Input, Output, EventEmitter } from '@angular/core';
import { BlogResponse } from 'src/app/services/models/blog-response';

@Component({
  selector: 'app-blog-card',
  templateUrl: './blog-card.component.html',
  styleUrls: ['./blog-card.component.scss'],
})
export class BlogCardComponent {
  @Input() blogCard: BlogResponse | null = null;
  @Input() isBlogEditable: boolean = false;
  @Input() isInDetailEdit: boolean = false;
  @Output() editBlogEvent = new EventEmitter<void>();
  @Output() deleteBlogEvent = new EventEmitter<void>();


  editBlog() {
    this.editBlogEvent.emit();
  }

  deleteBlog() {
    this.deleteBlogEvent.emit();
  }
}
