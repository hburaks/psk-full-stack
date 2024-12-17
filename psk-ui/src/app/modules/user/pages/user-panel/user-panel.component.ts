import {Component} from '@angular/core';
import { UserNavItems } from 'src/app/components/user-nav-underline/user-nav-underline-enum';
import { MyTestResponse } from 'src/app/services/models';

@Component({
  selector: 'app-user-panel',
  templateUrl: './user-panel.component.html',
  styleUrls: ['./user-panel.component.scss'],
})
export class UserPanelComponent {
  selectedNavItem: string = UserNavItems.MY_UPCOMING_SESSION;

  selectedTest: MyTestResponse | null = null;

  onSelectedNavItem(item: string) {
    this.selectedNavItem = item;
  }
  UserNavItems = UserNavItems;

  startTest(test: MyTestResponse) {
    this.selectedTest = test;
  }

  onCompleteTest() {
    this.selectedTest = null;
  }
}
