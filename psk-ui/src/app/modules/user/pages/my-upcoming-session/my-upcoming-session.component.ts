import {Component, EventEmitter, Output} from '@angular/core';
import {CommonService} from 'src/app/custom-services/common-service/common.service';
import {MyTestResponse, SessionResponse, UserResponse,} from 'src/app/services/models';
import {SessionControllerService, TestService, UserService,} from 'src/app/services/services';

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

  myTests: MyTestResponse[] = [];

  @Output() startTestEvent = new EventEmitter<MyTestResponse>();

  startTest(test: MyTestResponse) {
    this.startTestEvent.emit(test);
  }

  constructor(
    private mySessionService: SessionControllerService,
    private userService: UserService,
    private commonService: CommonService,
    private myTestService: TestService
  ) {
    this.getUpcomingSession();
    this.fetchUser();
    this.fetchMyTests();
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

  fetchMyTests() {
    this.myTestService.getAllMyTests().subscribe((tests) => {
      this.myTests = tests;
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
