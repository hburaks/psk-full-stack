import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonService } from 'src/app/custom-services/common.service';
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
      this.getBlogList();
    } else {
      const blogDetail = this.commonService.getBlogCardDetail(this.cardId);
      this.blogCard = blogDetail !== undefined ? blogDetail : null;
    }
  }

  getBlogList() {
    this.blogService.findAllBlogsShareable().subscribe({
      next: (blogs: PageResponseBlogResponse) => {
        this.fetchedBlogList = blogs.content || [];
        this.commonService.fetchedBlogList = this.fetchedBlogList;
        const blogDetail = this.commonService.getBlogCardDetail(this.cardId);
        this.blogCard = blogDetail !== undefined ? blogDetail : null;
        //TODO test base64 conversion: this.fetchedBlogList[i].cover = this.fetchedBlogList[i].cover.map(cover => `data:image/jpeg;base64,${cover}`);
      },
      error: (error) => {
        console.error('Error fetching blogs', error);
      },
    });
  }
}
