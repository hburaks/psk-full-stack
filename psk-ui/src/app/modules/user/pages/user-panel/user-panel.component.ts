import {Component} from '@angular/core';
import { UserNavItems } from 'src/app/components/user-nav-underline/user-nav-underline-enum';

@Component({
  selector: 'app-user-panel',
  templateUrl: './user-panel.component.html',
  styleUrls: ['./user-panel.component.scss'],
})
export class UserPanelComponent {
  selectedNavItem: string = UserNavItems.MY_UPCOMING_SESSION;

  onSelectedNavItem(item: string) {
    this.selectedNavItem = item;
  }
  UserNavItems = UserNavItems;
}