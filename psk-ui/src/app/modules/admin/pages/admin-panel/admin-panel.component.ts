import {Component} from '@angular/core';
import { AdminNavItems } from 'src/app/components/user-nav-underline/user-nav-underline-enum';

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
