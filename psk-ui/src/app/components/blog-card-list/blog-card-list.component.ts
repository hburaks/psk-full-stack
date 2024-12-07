import {
  Component,
  HostListener,
  Input,
  Output,
  EventEmitter,
} from '@angular/core';
import { BlogResponse } from '../../services/models/blog-response';
import { BlogService } from 'src/app/services/services/blog.service';

@Component({
  selector: 'app-blog-card-list',
  templateUrl: './blog-card-list.component.html',
  styleUrls: ['./blog-card-list.component.scss'],
})
export class BlogCardListComponent {
  isScreenSmall: boolean = false;
  isScreenMedium: boolean = false;
  isScreenLarge: boolean = false;
  @Input() blogCardList: BlogResponse[] = [];
  fetchedBlogList: BlogResponse[] = [];

  @Input() isBlogEditable: boolean = false;

  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    this.updateScreenSize();
  }

  @Output() editBlogEvent = new EventEmitter<number>();
  @Output() addBlogEvent = new EventEmitter<void>();

  constructor(private blogService: BlogService) {
    this.updateScreenSize();
  }

  editBlog(id: number) {
    this.editBlogEvent.emit(id);
  }

  addBlog() {
    this.addBlogEvent.emit();
  }

  deleteBlog(id: number) {
    this.blogService.deleteBlog({ id: id }).subscribe({
      next: (response) => {
        this.blogCardList = this.blogCardList.filter((blog) => blog.id !== id);
      },
      error: (error) => {
        console.log(error);
      }
    });
  }

  updateScreenSize() {
    this.isScreenSmall = false;
    this.isScreenMedium = false;
    this.isScreenLarge = false;

    this.isScreenSmall = window.innerWidth < 576;
    this.isScreenMedium = window.innerWidth < 768 && window.innerWidth >= 576;
    this.isScreenLarge = window.innerWidth >= 768;
  }
}
