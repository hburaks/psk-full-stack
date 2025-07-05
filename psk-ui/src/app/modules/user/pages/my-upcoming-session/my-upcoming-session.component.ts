import {Component, EventEmitter, Output} from '@angular/core';
import {CommonService} from 'src/app/custom-services/common-service/common.service';
import {SessionResponse, UserResponse, UserTestListResponse} from 'src/app/services/models';
import {SessionControllerService, UserService, UserTestService} from 'src/app/services/services';

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

  userTests: UserTestListResponse[] = [];

  @Output() startUserTestEvent = new EventEmitter<UserTestListResponse>();

  startUserTest(test: UserTestListResponse) {
    this.startUserTestEvent.emit(test);
  }

  constructor(
    private mySessionService: SessionControllerService,
    private userService: UserService,
    private commonService: CommonService,
    private userTestService: UserTestService
  ) {
    this.getUpcomingSession();
    this.fetchUser();
    this.fetchUserTests();
    //TODO it will be used in my sessions tab
    /* this.fetchMySessions(); */
    this.statusMap = this.commonService.sessionStatusMap;
    this.statusList = Object.keys(this.statusMap).filter(
      (status) => status !== 'UNAVAILABLE'
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
      this.isUpcomingSession = session != null;
    });
  }

  fetchUser() {
    this.userService.getUser().subscribe({
      next: (user) => {
        this.user = user;
      }
    });
  }


  fetchUserTests() {
    this.userTestService.getCurrentUserTests().subscribe({
      next: (tests) => {
        this.userTests = tests || [];
      },
      error: (error) => {
        console.error('Error fetching user tests', error);
        this.toastErrorMessage = 'Testler getirilirken bir hata oluştu';
        this.showToast = true;
      },
    });
  }

  goToSession() {
    //TODO
    if (this.upcomingSession?.sessionLink) {
      window.open(this.upcomingSession.sessionLink, '_blank');
    } else {
      this.toastErrorMessage = 'Google Meet linki henüz oluşturulmamış';
      this.showToast = true;
    }
  }
}
