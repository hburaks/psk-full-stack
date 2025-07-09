import {Component} from '@angular/core';
import { Router } from '@angular/router';
import { UserNavItems } from 'src/app/components/user-nav-underline/user-nav-underline-enum';
import { UserTestListResponse } from 'src/app/services/models';
import { HeaderService } from '../../service/header.service';

@Component({
  selector: 'app-user-panel',
  templateUrl: './user-panel.component.html',
  styleUrls: ['./user-panel.component.scss'],
})
export class UserPanelComponent {
  selectedNavItem: string;
  selectedUserTest: UserTestListResponse | null = null;

  UserNavItems = UserNavItems;

  constructor(
    private headerService: HeaderService,
    private router: Router
  ) {
    this.selectedNavItem = this.headerService.getActiveItem();
  }

  startUserTest(userTest: UserTestListResponse) {
    this.router.navigate(['/user-test', userTest.id]);
  }

  onCompleteTest() {
    this.selectedUserTest = null;
  }

  showAddSessionModal(isShowAddSessionModal: boolean) {
    if (isShowAddSessionModal) {
      this.headerService.setActiveItem(UserNavItems.CREATE_APPOINTMENT);
    } else {
      this.headerService.setActiveItem(UserNavItems.MY_UPCOMING_SESSION);
    }
  }

  isActiveItem(item: string) {
    return this.headerService.getActiveItem() === item;
  }
}
