import {Component} from '@angular/core';
import { CommonService } from 'src/app/custom-services/common-service/common.service';
import { BlogResponse, PageResponseBlogResponse } from 'src/app/services/models';
import { BlogService } from 'src/app/services/services';

@Component({
  selector: 'app-blog',
  templateUrl: './blog.component.html',
  styleUrls: ['./blog.component.scss'],
})
export class BlogComponent {
  blog: BlogResponse = {
    cover: ['https://i.ibb.co/qj44W4n/Auth-bg.jpg'],
    createdDate: '',
    id: 1,
    shareable: true,
    subTitle: 'Blog kartı için oluşturulmuş kısa bir örnek alt başlık içeriği.',
    text: 'string',
    title: 'Blog Yazısı',
  };
  blogCardList: BlogResponse[] = [];
  fetchedBlogList: BlogResponse[] = [this.blog, this.blog, this.blog];

  constructor(private blogService: BlogService, private commonService: CommonService) {
    this.getBlogList();
  }

  getBlogList() {
    this.blogService.findAllBlogsShareable().subscribe({
      next: (blogs: PageResponseBlogResponse) => {
        this.fetchedBlogList = blogs.content || [];
        this.commonService.fetchedBlogList = this.fetchedBlogList;
        if (this.fetchedBlogList.length != 0) {
          this.blogCardList = [];
          for (let i = 0; i < this.fetchedBlogList.length; i++) {
            if (this.fetchedBlogList[i]) {
              //TODO test base64 conversion: this.fetchedBlogList[i].cover = this.fetchedBlogList[i].cover.map(cover => `data:image/jpeg;base64,${cover}`);
            }
            this.blogCardList.push(this.fetchedBlogList[i]);
          }
        }
      },
      error: (error) => {
        console.error('Error fetching blogs', error);
      },
    });
  }
}
