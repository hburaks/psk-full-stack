import {Component} from '@angular/core';
import { UserNavItems } from 'src/app/components/user-nav-underline/user-nav-underline-enum';
import { MyTestResponse } from 'src/app/services/models';
import { HeaderService } from '../../service/header.service';

@Component({
  selector: 'app-user-panel',
  templateUrl: './user-panel.component.html',
  styleUrls: ['./user-panel.component.scss'],
})
export class UserPanelComponent {
  selectedNavItem: string;
  selectedTest: MyTestResponse | null = null;

  UserNavItems = UserNavItems;



  constructor(private headerService: HeaderService) {
    this.selectedNavItem = this.headerService.getActiveItem();
  }


  startTest(test: MyTestResponse) {
    this.selectedTest = test;
  }

  onCompleteTest() {
    this.selectedTest = null;
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
