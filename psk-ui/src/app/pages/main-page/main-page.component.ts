import { Component, HostListener, OnInit } from '@angular/core';
import { BlogService } from '../../services/services/blog.service';
import { PageResponseBlogResponse } from '../../services/models/page-response-blog-response';
import { BlogResponse } from '../../services/models/blog-response';
import { PublicTestResponse } from '../../services/models/public-test-response';
import { TestService } from '../../services/services/test.service';
import { SessionControllerV3Service } from 'src/app/services/services';
import {
  DailyCalendarResponse,
  HourlySessionResponse,
} from 'src/app/services/models';
import { CommonService } from 'src/app/custom-services/common-service/common.service';
import { TokenService } from 'src/app/custom-services/token/token.service';

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.scss'],
})
export class MainPageComponent implements OnInit {
  isScreenSmall: boolean = false;
  isScreenMedium: boolean = false;
  isScreenLarge: boolean = false;

  isUserLoggedIn: boolean = false;

  objectKeys(obj: object): string[] {
    return Object.keys(obj);
  }

  

  blogCardList: BlogResponse[] = [];
  fetchedBlogList: BlogResponse[] = [];

  testCardList: PublicTestResponse[] = [];
  fetchedTestList: PublicTestResponse[] = [];


  constructor(
    private blogService: BlogService,
    private testServiceV3: TestService,
    private commonService: CommonService,
    private tokenService: TokenService
  ) {}

  ngOnInit(): void {
    this.getBlogList();
    this.getTestList();
    this.updateScreenSize();
    this.isUserLoggedIn = this.tokenService.isTokenValid();
  }

  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    this.updateScreenSize();
  }

  updateScreenSize() {
    this.isScreenSmall = window.innerWidth < 576;
    this.isScreenMedium = window.innerWidth < 768 && window.innerWidth >= 576;
    this.isScreenLarge = window.innerWidth >= 768;

    this.updateBlogListBasedOnScreenSize();
    this.updateTestListBasedOnScreenSize();
  }

  updateBlogListBasedOnScreenSize() {
    if (this.isScreenSmall) {
      this.blogCardList = this.fetchedBlogList;
    } else if (this.isScreenMedium) {
      this.blogCardList = this.fetchedBlogList.slice(0, 2);
    } else if (this.isScreenLarge) {
      this.blogCardList = this.fetchedBlogList.slice(0, 3);
    }
  }

  updateTestListBasedOnScreenSize() {
    if (this.isScreenSmall) {
      this.testCardList = this.fetchedTestList;
    } else if (this.isScreenMedium) {
      this.testCardList = this.fetchedTestList.slice(0, 2);
    } else if (this.isScreenLarge) {
      this.testCardList = this.fetchedTestList.slice(0, 3);
    }
  }

  getBlogList() {
    this.blogService.findAllBlogsShareable().subscribe({
      next: (blogs: PageResponseBlogResponse) => {
        this.fetchedBlogList = blogs.content || [];
        this.updateBlogListBasedOnScreenSize();
        this.commonService.fetchedBlogList = this.fetchedBlogList;
      },
      error: (error) => {
        console.error('Error fetching blogs', error);
      },
    });
  }

  getTestList() {
    this.testServiceV3.getAllPublicTests().subscribe({
      next: (publicTests: Array<PublicTestResponse>) => {
        this.fetchedTestList = publicTests || [];
        console.log(this.fetchedTestList);
        this.updateTestListBasedOnScreenSize();
      },
      error: (err) => {
        console.error('Error fetching public tests', err);
      },
    });
  }
}
