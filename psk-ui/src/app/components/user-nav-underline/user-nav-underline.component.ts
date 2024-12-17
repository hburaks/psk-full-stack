import { Component, EventEmitter, Input, Output } from '@angular/core';
import { TokenService } from 'src/app/custom-services/token/token.service';
import { AdminNavItems, UserNavItems } from './user-nav-underline-enum';
import { SessionControllerV2Service } from 'src/app/services/services/session-controller-v-2.service';
import { SessionResponseV2 } from 'src/app/services/models/session-response-v-2';

@Component({
  selector: 'app-user-nav-underline',
  templateUrl: './user-nav-underline.component.html',
  styleUrls: ['./user-nav-underline.component.scss'],
})
export class UserNavUnderlineComponent {
  navItems: any[] = [];
  selectedNavItem: any;

  @Output()
  selectedNavItemEvent = new EventEmitter<string>();

  isActiveItem(item: any) {
    return this.selectedNavItem === item;
  }

  constructor(
    private tokenService: TokenService ) {
    this.createNavItems();
    if (this.navItems.length > 0) {
      this.selectNavItem(this.navItems[0]);
    }
  }

  createNavItems() {
    if (this.tokenService.isAdmin()) {
      this.createAdminNavItems();
    } else {
      this.createUserNavItems();
    }
  }

  createAdminNavItems() {
    Object.values(AdminNavItems).map((name) => {
      this.navItems.push(name);
    });
  }

  createUserNavItems() {
    Object.values(UserNavItems).map((name) => this.navItems.push(name));
  }

  selectNavItem(item: any) {
    this.selectedNavItem = item;
    this.selectedNavItemEvent.emit(item);
  }
}
