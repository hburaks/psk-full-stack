import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CommonService } from 'src/app/custom-services/common-service/common.service';
import { PageResponseSessionResponseV2 } from 'src/app/services/models/page-response-session-response-v-2';
import { SessionResponseV2 } from 'src/app/services/models/session-response-v-2';

@Component({
  selector: 'app-admin-sessions',
  templateUrl: './admin-sessions.component.html',
  styleUrls: ['./admin-sessions.component.scss'],
})
export class AdminSessionsComponent {
  @Input() sessions: SessionResponseV2[] = [];

  @Output() selectedSessionEvent = new EventEmitter<SessionResponseV2 | null>();

  constructor(private commonService: CommonService) {
    
  }


  selectedSession: SessionResponseV2 | null = null;
  showEditSessionModal = false;

  sessionsResponsePage: PageResponseSessionResponseV2 | null = null;

  getStatusText(status: string): string {
    return this.commonService.getStatusText(status);
  }

  editSession(sessionId: number) {
    window.scrollTo(0, 0);
    this.selectedSession =
      this.sessions.find((session) => session.sessionId === sessionId) ?? null;
    this.selectedSessionEvent.emit(this.selectedSession);
    this.showEditSessionModal = true;
  }
}
