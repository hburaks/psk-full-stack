import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonService } from 'src/app/custom-services/common.service';
import { FindBlogById$Params } from 'src/app/services/fn/blog/find-blog-by-id';
import {
  BlogResponse,
  PageResponseBlogResponse,
} from 'src/app/services/models';
import { BlogService } from 'src/app/services/services';
@Component({
  selector: 'app-blog-card-detail',
  templateUrl: './blog-card-detail.component.html',
  styleUrls: ['./blog-card-detail.component.scss'],
})
export class BlogCardDetailComponent {
  cardId: number = this.route.snapshot.params['id'];
  blogCard: BlogResponse | null = null;
  fetchedBlogList: BlogResponse[] = [];

  constructor(
    private commonService: CommonService,
    private route: ActivatedRoute,
    private blogService: BlogService
  ) {
    this.cardId = this.route.snapshot.params['id'];

    if (this.commonService.fetchedBlogList.length == 0) {
      this.getBlogDetail();
    } else {
      this.getBlogDetailFromList();
    }
  }

  getBlogDetailFromList() {
    const blogDetail = this.commonService.getBlogCardDetail(this.cardId);
    this.blogCard = blogDetail !== undefined ? blogDetail : null;
  }

  getBlogDetail() {
    const params: FindBlogById$Params = { id: this.cardId };
    this.blogService.findBlogById(params).subscribe({
      next: (blog: BlogResponse) => {
        this.blogCard = blog;
      },
    });
  }
  
}
