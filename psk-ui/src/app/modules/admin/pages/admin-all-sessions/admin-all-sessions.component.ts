import { Component, EventEmitter, Output } from '@angular/core';
import { PageResponseSessionResponseV2 } from 'src/app/services/models/page-response-session-response-v-2';
import { SessionResponseV2 } from 'src/app/services/models/session-response-v-2';
import { UserWithIncomingSessionResponse } from 'src/app/services/models/user-with-incoming-session-response';
import { SessionControllerV2Service } from 'src/app/services/services/session-controller-v-2.service';

@Component({
  selector: 'app-admin-all-sessions',
  templateUrl: './admin-all-sessions.component.html',
  styleUrls: ['./admin-all-sessions.component.scss'],
})
export class AdminAllSessionsComponent {
  constructor(private sessionControllerV2Service: SessionControllerV2Service) {
    this.getAllSessions(0);
  }

  sessionsResponsePage: PageResponseSessionResponseV2 | null = null;
  sessions: SessionResponseV2[] = [];
  currentPage = 0;
  selectedSession: SessionResponseV2 | null = null;
  showEditSessionModal: boolean = false;

  receiveSelectedSession(session: SessionResponseV2 | null) {
    this.selectedSession = session;
    this.showEditSessionModal = true;
  }

  receiveShowEditSessionModal(showEditSessionModal: boolean) {
    this.showEditSessionModal = showEditSessionModal;
  }

  getAllSessions(pageAdd: number) {
    this.currentPage = this.currentPage + pageAdd;
    this.sessionControllerV2Service
      .getAllSessionsV2({ page: this.currentPage, size: 100 })
      .subscribe({
        next: (res: PageResponseSessionResponseV2) => {
          this.sessionsResponsePage = res;
          this.sessions = res.content ?? [];
        },
        error: (error) => {
          console.log(error);
        },
      });
  }
}
