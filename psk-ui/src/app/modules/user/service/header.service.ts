import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { AdminNavItems, UserNavItems } from 'src/app/components/user-nav-underline/user-nav-underline-enum';
import { TokenService } from 'src/app/custom-services/token/token.service';

@Injectable({
  providedIn: 'root',
})
export class HeaderService {
  selectedNavItem: string='';
  navItems: string[] = [];
  selectedNavItem$ = new BehaviorSubject<string>(this.selectedNavItem);
  constructor(private tokenService: TokenService) {
    this.createNavItems();
  }

  createNavItems() {
    if (this.tokenService.isAdmin()) {
      this.createAdminNavItems();
      this.selectedNavItem = AdminNavItems.UPCOMING_SESSIONS;
    } else {
      this.createUserNavItems();
      this.selectedNavItem = UserNavItems.MY_UPCOMING_SESSION;
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

  setActiveItem(item: string) {
    this.selectedNavItem = item;
    this.selectedNavItem$.next(item);
  }

  getActiveItem() {
    return this.selectedNavItem;
  }
}
