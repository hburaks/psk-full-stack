import { Component } from '@angular/core';
import { CommonService } from 'src/app/custom-services/common-service/common.service';
import { SessionResponse, UserResponse } from 'src/app/services/models';
import {
  SessionControllerService,
  SessionControllerV3Service,
  UserService,
} from 'src/app/services/services';

@Component({
  selector: 'app-my-upcoming-session',
  templateUrl: './my-upcoming-session.component.html',
  styleUrls: ['./my-upcoming-session.component.scss'],
})
export class MyUpcomingSessionComponent {
  isUpcomingSession: boolean = false;

  sessionsOfUser: SessionResponse[] = [];

  upcomingSession: SessionResponse | null = null;

  user: UserResponse | null = null;

  showToast: boolean = false;

  toastErrorMessage: string = '';

  statusMap: { [key: string]: string } = {};
  statusList: string[] = [];

  constructor(
    private mySessionService: SessionControllerService,
    private userService: UserService,
    private commonService: CommonService
  ) {
    this.getUpcomingSession();
    this.fetchUser();
    this.fetchMySessions();
    this.statusMap = this.commonService.sessionStatusMap;
    this.statusList = Object.keys(this.statusMap).filter(
      (status) =>
        status !== 'UNAVAILABLE' && status !== 'AWAITING_PSYCHOLOGIST_APPROVAL'
    );
  }

  fetchMySessions() {
    this.mySessionService.getMySessions().subscribe((sessions) => {
      this.sessionsOfUser = sessions;
    });
  }

  getUpcomingSession() {
    this.mySessionService.getUpcomingSession().subscribe((session) => {
      this.upcomingSession = session;
    });
  }

  fetchUser() {
    this.userService.getUser().subscribe({
      next: (user) => {
        this.user = user;
      },
      error: (error) => {
        console.log(error);
      },
    });
  }

  goToSession() {
    // TODO
    console.log(this.upcomingSession);
  }

}
