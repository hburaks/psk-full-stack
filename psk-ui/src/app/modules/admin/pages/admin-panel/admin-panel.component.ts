import { Component } from '@angular/core';
import { AdminNavItems } from 'src/app/components/user-nav-underline/user-nav-underline-enum';
import { HeaderService } from 'src/app/modules/user/service/header.service';
import { UserWithIncomingSessionResponse } from 'src/app/services/models/user-with-incoming-session-response';

@Component({
  selector: 'app-admin-panel',
  templateUrl: './admin-panel.component.html',
  styleUrls: ['./admin-panel.component.scss'],
})
export class AdminPanelComponent {
  selectedNavItem: string = AdminNavItems.UPCOMING_SESSIONS;


  constructor(private headerService: HeaderService) {
    this.selectedNavItem = this.headerService.getActiveItem();
  }
  AdminNavItems = AdminNavItems;

  selectedUser: UserWithIncomingSessionResponse | null = null;

  onSelectedUser(user: UserWithIncomingSessionResponse | null) {
    this.selectedUser = user;
  }

  isActiveItem(item: string) {
    return this.headerService.getActiveItem() === item;
  }
}
