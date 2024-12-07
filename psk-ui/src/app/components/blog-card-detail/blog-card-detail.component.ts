import { HttpErrorResponse } from '@angular/common/http';
import { Component, Input, AfterViewInit, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonService } from 'src/app/custom-services/common-service/common.service';
import { FindBlogById$Params } from 'src/app/services/fn/blog/find-blog-by-id';
import { BlogResponse } from 'src/app/services/models';
import { BlogService } from 'src/app/services/services';
@Component({
  selector: 'app-blog-card-detail',
  templateUrl: './blog-card-detail.component.html',
  styleUrls: ['./blog-card-detail.component.scss'],
})
export class BlogCardDetailComponent implements AfterViewInit {
  blogCard: BlogResponse | null = null;
  fetchedBlogList: BlogResponse[] = [];

  @Input() isEditable: boolean = false;

  @Input() isBlogEditable: boolean = false;

  @Input() cardId: number | null = null;

  @Output() endEditingEvent = new EventEmitter<void>();

  constructor(
    private commonService: CommonService,
    private blogService: BlogService,
    private route: ActivatedRoute
  ) {
    
  }

  ngAfterViewInit(): void {
    if (this.cardId == null) {
      this.cardId = this.route.snapshot.params['id'];
    }
    if (this.cardId != null) {
      if (this.commonService.fetchedBlogList.length != 0) {
        this.getBlogDetailFromList();
      }
      if (this.blogCard == null) {
        this.getBlogDetail();
      }
    }
  }

  endEditing() {
    this.endEditingEvent.emit();
  }

  getBlogDetailFromList() {
    const blogDetail = this.commonService.getBlogCardDetail(this.cardId!);
    this.blogCard = blogDetail !== undefined ? blogDetail : null;
  }

  getBlogDetail() {
    const params: FindBlogById$Params = { id: this.cardId! };
    this.blogService.findBlogById(params).subscribe({
      next: (blog: BlogResponse) => {
        this.blogCard = blog;
      },
      error: (error: HttpErrorResponse) => {
        console.log(error);
      },
    });
  }
}
