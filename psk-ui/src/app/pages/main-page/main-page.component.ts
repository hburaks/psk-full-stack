import { Component, HostListener, OnInit } from '@angular/core';
import { BlogService } from '../../services/services/blog.service';
import { PageResponseBlogResponse } from '../../services/models/page-response-blog-response';
import { BlogResponse } from '../../services/models/blog-response';
import { PublicTestResponse } from '../../services/models/public-test-response';
import { MyTestResponse } from '../../services/models/my-test-response';
import { TestService } from '../../services/services/test.service';
import { SessionControllerV3Service } from 'src/app/services/services';
import {
  DailyCalendarResponse,
  HourlySessionResponse,
} from 'src/app/services/models';

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.scss'],
})
export class MainPageComponent implements OnInit {
  isScreenSmall: boolean = false;
  isScreenMedium: boolean = false;
  isScreenLarge: boolean = false;

  objectKeys(obj: object): string[] {
    return Object.keys(obj);
  }

  blog: BlogResponse = {
    cover: ['https://i.ibb.co/qj44W4n/Auth-bg.jpg'],
    createdDate: '',
    id: 1,
    shareable: true,
    subTitle: 'Blog kartı için oluşturulmuş kısa bir örnek alt başlık içeriği.',
    text: 'string',
    title: 'Blog Yazısı',
  };

  test: PublicTestResponse = {
    cover: ['./assets/materials/logo-ex.svg'],
    id: 1,
    questions: [],
    subTitle: 'Test kartı için oluşturulmuş kısa bir örnek alt başlık içeriği.',
    title: 'Test',
  };

  currentSelectedWeek: Date = new Date();
  previousWeekAvailable: boolean = false;
  nextWeekAvailable: boolean = true;

  blogCardList: BlogResponse[] = [this.blog, this.blog, this.blog];
  fetchedBlogList: BlogResponse[] = [this.blog, this.blog, this.blog];

  testCardList: MyTestResponse[] = [this.test, this.test, this.test];
  fetchedTestList: MyTestResponse[] = [this.test, this.test, this.test];

  weeklySessionCalendar: DailyCalendarResponse[] = [];

  todayStartOfDay: number = new Date().setHours(0, 0, 0, 0);

  //TODO: base64 ten resime dönüşüm sağlanmalı

  constructor(
    private blogService: BlogService,
    private testServiceV3: TestService,
    private sessionControllerV3Service: SessionControllerV3Service
  ) {}

  ngOnInit(): void {
    this.getBlogList();
    this.getTestList();
    this.updateScreenSize();
    this.updateBlogListBasedOnScreenSize();
    this.updateTestListBasedOnScreenSize();
    this.getWeeklySessions();
  }

  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    this.updateScreenSize();
    this.updateBlogListBasedOnScreenSize();
    this.updateTestListBasedOnScreenSize();
  }

  updateScreenSize() {
    this.isScreenSmall = false;
    this.isScreenMedium = false;
    this.isScreenLarge = false;

    this.isScreenSmall = window.innerWidth < 576;
    this.isScreenMedium = window.innerWidth < 768 && window.innerWidth >= 576;
    this.isScreenLarge = window.innerWidth >= 768;
  }

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
        if (this.fetchedTestList.length !== 0) {
          this.testCardList = this.fetchedTestList.slice(0, 3);
        }
      },
      error: (err) => {
        console.error('Error fetching public tests', err);
      },
    });
  }

  private getWeeklySessions() {
    const specificDate = this.currentSelectedWeek;
    const dateTime =
      specificDate.getUTCFullYear() +
      '-' +
      String(specificDate.getUTCMonth() + 1).padStart(2, '0') +
      '-' +
      String(specificDate.getUTCDate()).padStart(2, '0') +
      'T' +
      String(specificDate.getUTCHours()).padStart(2, '0') +
      ':' +
      String(specificDate.getUTCMinutes()).padStart(2, '0') +
      ':' +
      String(specificDate.getUTCSeconds()).padStart(2, '0');

    this.sessionControllerV3Service.getWeeklyCalendar({ dateTime }).subscribe({
      next: (sessions: Array<DailyCalendarResponse>) => {
        this.weeklySessionCalendar = sessions;
      },
      error: (error) => {
        console.error('Error fetching weekly sessions', error);
        this.weeklySessionCalendar = this.createDefaultWeeklyCalendar(false);
      },
    });
  }

  private createDefaultWeeklyCalendar(
    isAvailable: boolean
  ): DailyCalendarResponse[] {
    let defaultWeeklyCalendar: DailyCalendarResponse[] = [];
    for (let day of [
      'MONDAY',
      'TUESDAY',
      'WEDNESDAY',
      'THURSDAY',
      'FRIDAY',
      'SATURDAY',
    ]) {
      let sessions: HourlySessionResponse[] = [];
      for (let i = 10; i < 20; i++) {
        const date = new Date(this.currentSelectedWeek);
        const dayIndex = [
          'SUNDAY',
          'MONDAY',
          'TUESDAY',
          'WEDNESDAY',
          'THURSDAY',
          'FRIDAY',
          'SATURDAY',
        ].indexOf(day);
        date.setDate(date.getDate() - date.getDay() + dayIndex);
        date.setHours(i, 0, 0, 0);

        let session: HourlySessionResponse = {
          available: isAvailable,
          date: date.toISOString(),
        };
        sessions.push(session);
      }

      defaultWeeklyCalendar.push({
        dayOfWeek: day as
          | 'MONDAY'
          | 'TUESDAY'
          | 'WEDNESDAY'
          | 'THURSDAY'
          | 'FRIDAY'
          | 'SATURDAY'
          | 'SUNDAY',
        sessions: sessions,
      });
    }

    return defaultWeeklyCalendar;
  }

  previousWeek() {
    const previousWeekDate = new Date(this.currentSelectedWeek);
    previousWeekDate.setDate(previousWeekDate.getDate() - 7);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    if (previousWeekDate <= today) {
      console.log('Cannot go back to previous week');
    } else {
      this.currentSelectedWeek = previousWeekDate;
      this.getWeeklySessions();
      this.previousWeekAvailable = this.checkPreviousWeekAvailable();
      this.nextWeekAvailable = this.checkNextWeekAvailable();
    }
  }

  checkPreviousWeekAvailable() {
    const previousWeekDate = new Date(this.currentSelectedWeek);
    previousWeekDate.setDate(previousWeekDate.getDate() - 7);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    return previousWeekDate >= today;
  }

  nextWeek() {
    const nextWeekDate = new Date(this.currentSelectedWeek);
    nextWeekDate.setDate(nextWeekDate.getDate() + 7);
    const maxDate = new Date();
    maxDate.setDate(maxDate.getDate() + 28);
    if (nextWeekDate > maxDate) {
      console.log('Cannot go to next week');
    } else {
      this.currentSelectedWeek = nextWeekDate;
      this.nextWeekAvailable = this.checkNextWeekAvailable();
      this.previousWeekAvailable = this.checkPreviousWeekAvailable();
      this.getWeeklySessions();
    }
  }

  checkNextWeekAvailable() {
    const maxDate = new Date();
    maxDate.setDate(maxDate.getDate() + 28);
    const nextWeekDate = new Date(this.currentSelectedWeek);
    nextWeekDate.setDate(nextWeekDate.getDate() + 7);
    return nextWeekDate <= maxDate;
  }

  private updateBlogListBasedOnScreenSize() {
    if (this.isScreenMedium) {
      this.blogCardList = [];
      for (let i = 0; i < 2; i++) {
        this.blogCardList.push(this.fetchedBlogList[i]);
      }
    } else if (this.isScreenSmall) {
      this.blogCardList = [];
      for (let i = 0; i < 3; i++) {
        this.blogCardList.push(this.fetchedBlogList[i]);
      }
    } else if (this.isScreenLarge) {
      this.blogCardList = [];
      for (let i = 0; i < 3; i++) {
        this.blogCardList.push(this.fetchedBlogList[i]);
      }
    }
  }

  private updateTestListBasedOnScreenSize() {
    if (this.isScreenMedium) {
      this.testCardList = [];
      for (let i = 0; i < 2; i++) {
        this.testCardList.push(this.fetchedTestList[i]);
      }
    } else if (this.isScreenSmall) {
      this.testCardList = [];
      for (let i = 0; i < 3; i++) {
        this.testCardList.push(this.fetchedTestList[i]);
      }
    } else if (this.isScreenLarge) {
      this.testCardList = [];
      for (let i = 0; i < 3; i++) {
        this.testCardList.push(this.fetchedTestList[i]);
      }
    }
  }

  getFormattedDate(day: DailyCalendarResponse): string {
    let session = day.sessions?.[0];
    const date = new Date(session?.date || '');
    const options = {
      day: '2-digit',
      month: '2-digit',
      year: '2-digit',
    } as const;
    return new Intl.DateTimeFormat('tr-TR', options).format(date);
  }

  getFormattedShortDate(day: DailyCalendarResponse): string {
    let session = day.sessions?.[0];
    const date = new Date(session?.date || '');
    const options = {
      day: '2-digit',
      month: '2-digit',
    } as const;
    return new Intl.DateTimeFormat('tr-TR', options).format(date);
  }

  getFormattedShortDayName(dayOfWeek: string): string {
    const longDayName = this.getFormattedLongDayName(dayOfWeek);
    if (longDayName == 'Pazartesi') {
      return 'Pzt';
    } else if (longDayName == 'Cumartesi' || longDayName == 'Pazar') {
      return 'Cts';
    }
    return longDayName.substring(0, 3);
  }

  getFormattedLongDayName(dayOfWeek: string): string {
    const dayMap = {
      MONDAY: 'Pazartesi',
      TUESDAY: 'Salı',
      WEDNESDAY: 'Çarşamba',
      THURSDAY: 'Perşembe',
      FRIDAY: 'Cuma',
      SATURDAY: 'Cumartesi',
    };
    return dayMap[dayOfWeek as keyof typeof dayMap] || '';
  }

  formatSessionDate(date: string): string {
    return new Intl.DateTimeFormat('tr-TR', {
      hour: '2-digit',
      minute: '2-digit',
    }).format(new Date(date));
  }
}
