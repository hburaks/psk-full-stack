import { Component, HostListener, Input } from '@angular/core';
import { BlogResponse } from '../../services/models/blog-response';

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

  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    this.updateScreenSize();
  }

  constructor() {
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
