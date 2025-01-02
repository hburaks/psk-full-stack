import { Component, EventEmitter, Input, Output, AfterViewInit } from '@angular/core';
import { HeaderService } from 'src/app/modules/user/service/header.service';

@Component({
  selector: 'app-user-nav-underline',
  templateUrl: './user-nav-underline.component.html',
  styleUrls: ['./user-nav-underline.component.scss'],
})
export class UserNavUnderlineComponent implements AfterViewInit {
  navItems: any[] = [];

  selectedNavItem: any;

  isActiveItem(item: any) {
    return this.selectedNavItem === item;
  }

  constructor(private headerService: HeaderService) {
    this.navItems = this.headerService.navItems;
  }

  ngOnInit() {
    this.headerService.selectedNavItem$.subscribe((item) => {
      this.selectedNavItem = item;
    });
  } 
  ngAfterViewInit() {
    this.selectedNavItem = this.headerService.getActiveItem();
  }

  selectNavItem(item: any) {
    this.headerService.setActiveItem(item);
  }
}
