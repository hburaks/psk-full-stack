import { Component } from '@angular/core';
import { BlogResponse } from 'src/app/services/models/blog-response';
import { PageResponseBlogResponse } from 'src/app/services/models/page-response-blog-response';
import { BlogService } from 'src/app/services/services/blog.service';

@Component({
  selector: 'app-edit-blog',
  templateUrl: './edit-blog.component.html',
  styleUrls: ['./edit-blog.component.scss'],
})
export class EditBlogComponent {
  blogPageResponse: PageResponseBlogResponse = {
    content: [],
    totalPages: 0,
    totalElements: 0,
  };
  blogCardList: BlogResponse[] = [];

  isAddingBlog: boolean = false;
  isEditingBlog: boolean = false;
  editedBlogId: number | null = null;

  constructor(private blogService: BlogService) {
    this.findAllBlogs();
  }

  findAllBlogs() {
    this.blogService.findAllBlogs().subscribe({
      next: (blogs: PageResponseBlogResponse) => {
        this.blogPageResponse = blogs;
        this.blogCardList = blogs.content || [];
        console.log(this.blogCardList, 'blogCardList');
      },
      error: (error) => {
        console.error(error);
      },
    });
  }

  editBlog(id: number) {
    this.editedBlogId = id;
    this.isEditingBlog = true;
  }

  addBlog() {
    this.isEditingBlog = true;
  }

  getBlogId(): number | null {
    return this.editedBlogId;
  }

  endEditing() {
    this.isEditingBlog = false;
    this.editedBlogId = null;
    this.findAllBlogs();
  }
}
