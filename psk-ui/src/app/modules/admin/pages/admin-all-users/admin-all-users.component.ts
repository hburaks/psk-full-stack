import { Component, EventEmitter, Output } from '@angular/core';
import { CommonService } from 'src/app/custom-services/common-service/common.service';
import { GetAllUsersWithSessionV2$Params } from 'src/app/services/fn/session-controller-v-2/get-all-users-with-session-v-2';
import { PageResponseUserWithIncomingSessionResponse } from 'src/app/services/models/page-response-user-with-incoming-session-response';
import { UserWithIncomingSessionResponse } from 'src/app/services/models/user-with-incoming-session-response';
import { SessionControllerV2Service } from 'src/app/services/services/session-controller-v-2.service';

@Component({
  selector: 'app-admin-all-users',
  templateUrl: './admin-all-users.component.html',
  styleUrls: ['./admin-all-users.component.scss'],
})
export class AdminAllUsersComponent {
  usersPageResponse: PageResponseUserWithIncomingSessionResponse = {
    content: [],
    totalPages: 0,
    totalElements: 0,
  };
  users: UserWithIncomingSessionResponse[] = [];
  selectedUser: UserWithIncomingSessionResponse | null = null;

  constructor(
    private sessionControllerV2: SessionControllerV2Service,
    private commonService: CommonService
  ) {}

  @Output() selectedUserEvent =
    new EventEmitter<UserWithIncomingSessionResponse | null>();

  selectUser(user: UserWithIncomingSessionResponse | null) {
    this.selectedUser = user;
    this.selectedUserEvent.emit(user);
  }

  ngOnInit(): void {
    const params: GetAllUsersWithSessionV2$Params = {
      page: 0,
      size: 10,
    };
    this.sessionControllerV2.getAllUsersWithSessionV2(params).subscribe(
      (next) => {
        this.usersPageResponse = next;
        this.users = next.content ?? [];
      },
      (error) => {
        console.log(error);
      }
    );
  }

  getStatusText(status: string): string {
    return this.commonService.getStatusText(status);
  }

  closeUpcomingSession() {
    this.selectedUser = null;
  }
}
