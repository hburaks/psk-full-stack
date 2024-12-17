import { Component, Input } from '@angular/core';
import { SessionResponseV2 } from 'src/app/services/models/session-response-v-2';
import { UserTestForAdminResponse } from 'src/app/services/models/user-test-for-admin-response';
import { TestService } from 'src/app/services/services';
import { SessionControllerV2Service } from 'src/app/services/services/session-controller-v-2.service';
import { PublicTestResponse, UserWithIncomingSessionResponse } from 'src/app/services/models';

@Component({
  selector: 'app-upcoming-session',
  templateUrl: './upcoming-session.component.html',
  styleUrls: ['./upcoming-session.component.scss'],
})
export class UpcomingSessionComponent {
  @Input() selectedUser: UserWithIncomingSessionResponse | null = null;

  upcomingSession: SessionResponseV2 | null = null;
  showToast: boolean = false;
  showEditSessionModal: boolean = false;
  toastErrorMessage: string = '';
  isUpcomingSessionNotFound: boolean = false;
  bindingNoteForUser: string = '';
  bindingNoteForPsychologist: string = '';

  testResults: UserTestForAdminResponse[] = [];

  publicTests: PublicTestResponse[] = [];

  isAddingTestToUser: boolean = false;

  sessionsOfUser: SessionResponseV2[] = [];

  selectedSession: SessionResponseV2 | null = null;

  receiveSelectedSession(session: SessionResponseV2 | null) {
    this.selectedSession = session;
    this.showEditSessionModal = true;
  }

  receiveAddedTest(test: UserTestForAdminResponse | null) {
    if (test) {
      this.testResults = [...this.testResults, test];
    }
  }

  constructor(
    private sessionControllerV2Service: SessionControllerV2Service,
    private testService: TestService
  ) {}

  ngAfterViewInit() {
    if (!this.selectedUser?.id) {
      this.getUpcomingSession();
    } else {
      this.getUserData(this.selectedUser.id!);
    }
  }

  getUserData(userId: number) {
    this.getAllSessionsOfUser(userId);
    this.getTestResultOfUser(userId);
  }

  getUpcomingSession() {
    this.sessionControllerV2Service.getUpcomingSessionsV2().subscribe({
      next: (session) => {
        if (session == null) {
          this.isUpcomingSessionNotFound = true;
        } else {
          this.upcomingSession = session;
          this.bindingNoteForUser = this.upcomingSession?.noteForUser ?? '';
          this.bindingNoteForPsychologist =
            this.upcomingSession?.noteForPsychologist ?? '';
          if (this.upcomingSession?.userForSessionResponse?.id) {
            this.getTestResultOfUser(
              this.upcomingSession.userForSessionResponse.id
            );
            this.getAllSessionsOfUser(
              this.upcomingSession.userForSessionResponse.id
            );
          }
        }
      },
      error: (error) => {
        this.toastErrorMessage =
          'Yaklaşan seans bilgisi getirilirken bir hata oluştu';
        this.showToast = true;
      },
    });
  }

  getTestResultOfUser(userId: number) {
    this.testService
      .getAllTestsAssignedToUserV2({ userId: userId })
      .subscribe((result) => {
        if (result.length > 0) {
          this.testResults = result;
        }
      });
  }

  goToSession() {
    // TODO: google meet linki ile yönlendirme yapılacak. ayrıca upcominSession içerisinde link olmalı.
    // window.open(this.upcomingSession?.googleMeetLink, '_blank');
  }

  addTestToUser() {
    this.testService.getAllPublicTests().subscribe({
      next: (result) => {
        this.publicTests = result;
        this.isAddingTestToUser = true;
      },
      error: (error) => {
        this.toastErrorMessage = 'Testler getirilirken bir hata oluştu';
        this.showToast = true;
      },
    });
  }

  editSession() {
    window.scrollTo(0, 0);
    this.selectedSession = this.upcomingSession;
    this.showEditSessionModal = true;
  }

  getAllSessionsOfUser(userId: number) {
    this.sessionControllerV2Service
      .getAllSessionsOfUserV2({ userId: userId })
      .subscribe({
        next: (result) => {
          this.sessionsOfUser = result.sort((a, b) => {
            return (
              new Date(b.date ?? '').getTime() -
              new Date(a.date ?? '').getTime()
            );
          });

          this.upcomingSession =
            result
              .filter(
                (session) => session.date && new Date(session.date) > new Date()
              )
              .sort(
                (a, b) =>
                  new Date(a.date!).getTime() - new Date(b.date!).getTime()
              )[0] || null;
        },
        error: (error) => {
          this.toastErrorMessage = 'Seanslar getirilirken bir hata oluştu';
          this.showToast = true;
        },
      });
  }

  addSessionToUser() {
    const session: SessionResponseV2 = {} as SessionResponseV2;
    this.receiveSelectedSession(session);
    window.scrollTo(0, 0);
  }
}
