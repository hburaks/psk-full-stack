import {Component} from '@angular/core';
import {BlogService} from "../../services/services/blog.service";
import {PageResponseBlogResponse} from "../../services/models/page-response-blog-response";
import {BlogResponse} from "../../services/models/blog-response";

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.scss']
})
export class MainPageComponent {
  blog: BlogResponse = {
    cover: ["string"],
    createdDate: "",
    id: 1,
    shareable: true,
    subTitle: "Blog kartı için oluşturulmuş kısa bir örnek alt başlık içeriği.",
    text: "string",
    title: "Blog Yazısı"
  }
  blogCardList: BlogResponse[] = [this.blog, this.blog, this.blog];
  fetchedBlogList: BlogResponse[] = [];

  constructor(
    private blogService: BlogService
  ) {
  }

  BreakPointObserver: any;

  getBlogList() {
    this.blogService.findAllBlogsShareable().subscribe({
      next: (blogs: PageResponseBlogResponse) => {
        this.fetchedBlogList = blogs.content || [];
        if (this.fetchedBlogList.length != 0) {
          this.blogCardList = [];
          for (let i = 0; i < 3; i++) {
            this.blogCardList.push(this.fetchedBlogList[i]);
          }
        }
      }
    });
  }

}
