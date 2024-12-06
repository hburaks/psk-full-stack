import { Component } from '@angular/core';
import { AdminNavItems } from 'src/app/components/user-nav-underline/user-nav-underline-enum';
import { CustomSessionService } from 'src/app/custom-services/custom-session/custom-session.service';
import { SessionResponseV2 } from 'src/app/services/models/session-response-v-2';
import { UserWithIncomingSessionResponse } from 'src/app/services/models/user-with-incoming-session-response';

@Component({
  selector: 'app-admin-panel',
  templateUrl: './admin-panel.component.html',
  styleUrls: ['./admin-panel.component.scss'],
})
export class AdminPanelComponent {
  selectedNavItem: string = AdminNavItems.UPCOMING_SESSIONS;
  
  onSelectedNavItem(item: string) {
    this.selectedNavItem = item;
  }
  AdminNavItems = AdminNavItems;
}
