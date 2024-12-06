import { Component, HostListener, Input } from '@angular/core';
import { BlogResponse } from '../../services/models/blog-response';
import { BlogService } from 'src/app/services/services/blog.service';
import { PageResponseBlogResponse } from 'src/app/services/models/page-response-blog-response';
import { CommonService } from 'src/app/custom-services/common-service/common.service';

@Component({
  selector: 'app-blog-card',
  templateUrl: './blog-card.component.html',
  styleUrls: ['./blog-card.component.scss'],
})
export class BlogCardComponent {
  isScreenSmall: boolean = false;
  isScreenMedium: boolean = false;
  isScreenLarge: boolean = false;
  @Input() blogCardList: BlogResponse[] = [];
  fetchedBlogList: BlogResponse[] = [];

  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    this.updateScreenSize();
  }

  constructor(
    private blogService: BlogService,
    private commonService: CommonService
  ) {
    this.updateScreenSize();
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
