import {Component} from '@angular/core';
import {CommonService} from 'src/app/custom-services/common-service/common.service';
import {SessionResponse} from 'src/app/services/models/session-response';
import {SessionControllerService} from 'src/app/services/services';

@Component({
  selector: 'app-my-sessions',
  templateUrl: './my-sessions.component.html',
  styleUrls: ['./my-sessions.component.scss'],
})
export class MySessionsComponent {
  mySessions: SessionResponse[] = [];

  constructor(private sessionControllerService: SessionControllerService, private commonService: CommonService) {
    this.getAllMySessions();
  }

  getAllMySessions() {
    this.sessionControllerService.getMySessions().subscribe({
      next: (res: Array<SessionResponse>) => {
        this.mySessions = res;
      }
    });
  }

  getStatusText(status: string): string {
    return this.commonService.getStatusText(status);
  }

  isSessionCancelable(status: string): boolean {
    return status === 'AWAITING_THERAPIST_APPROVAL';
  }

  cancelSession(sessionId: number) {
    this.sessionControllerService.cancelUserSession({ id: sessionId }).subscribe({
      next: (res) => {
        this.getAllMySessions();
      },
      error: (error) => {
        console.log(error);
      },
    });
  }
}
