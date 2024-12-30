import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonService } from 'src/app/custom-services/common-service/common.service';
import { SessionPsychologistNoteRequest } from 'src/app/services/models/session-psychologist-note-request';
import { SessionResponseV2 } from 'src/app/services/models/session-response-v-2';
import { SessionStatusRequest } from 'src/app/services/models/session-status-request';
import { SessionUserNoteRequest } from 'src/app/services/models/session-user-note-request';
import { SessionControllerV2Service } from 'src/app/services/services/session-controller-v-2.service';

@Component({
  selector: 'app-edit-user-session',
  templateUrl: './edit-user-session.component.html',
  styleUrls: ['./edit-user-session.component.scss'],
})
export class EditUserSessionComponent {
  @Input() session: SessionResponseV2 | null = null;
  @Input() userId: number | null = null;
  @Output() showEditSessionModal = new EventEmitter<boolean>();

  isAddSession: boolean = false;

  dateToUpdateSession: string | null = null;
  statusMap: { [key: string]: string } = {};
  statusList: string[] = [];

  bindingNoteForUser: string = '';
  bindingNoteForPsychologist: string = '';

  isEditingNoteForUser: boolean = false;
  isEditingNoteForPsychologist: boolean = false;

  toastErrorMessage: string = '';
  showToast: boolean = false;

  constructor(
    private sessionControllerV2Service: SessionControllerV2Service,
    private commonService: CommonService
  ) {
    this.statusMap = this.commonService.sessionStatusMap;
    this.statusList = Object.keys(this.statusMap).filter(
      (status) => status !== 'UNAVAILABLE'
    );
  }

  updateDateToUpdateSession(date: string | null) {
    this.dateToUpdateSession = date;

    if (this.dateToUpdateSession) {
      if (this.isAddSession) {
        this.addSession();
      } else {
        this.updateSessionDate();
      }
    }
  }

  ngAfterViewInit() {
    this.isAddSession = this.session?.sessionId === undefined;
  }

  closeModal() {
    this.showEditSessionModal.emit(false);
    window.location.reload();
  }

  editNoteForUser() {
    this.isEditingNoteForUser = true;
  }

  editNoteForPsychologist() {
    this.isEditingNoteForPsychologist = true;
  }

  addNoteForUser() {
    if (this.session) {
      const requestBody: SessionUserNoteRequest = {
        sessionId: this.session.sessionId,
        noteForUser: this.bindingNoteForUser,
      };
      this.sessionControllerV2Service
        .addNoteToSessionForUserV2({ body: requestBody })
        .subscribe({
          next: (response: SessionResponseV2) => {
            this.session = response;
            this.bindingNoteForUser = response.noteForUser ?? '';
            this.isEditingNoteForUser = false;
          },
          error: (error) => {
            this.toastErrorMessage = 'Not eklenirken bir hata oluştu';
            this.showToast = true;
          },
        });
    }
  }

  addNoteForPsychologist() {
    if (this.session) {
      const requestBody: SessionPsychologistNoteRequest = {
        sessionId: this.session.sessionId,
        noteForPsychologist: this.bindingNoteForPsychologist,
      };

      this.sessionControllerV2Service
        .addNoteToSessionForPsychologistV2({ body: requestBody })
        .subscribe({
          next: (response: SessionResponseV2) => {
            this.bindingNoteForPsychologist =
              response.noteForPsychologist ?? '';
            this.session = response;
            this.isEditingNoteForPsychologist = false;
          },
          error: (error) => {
            this.toastErrorMessage = 'Not eklenirken bir hata oluştu';
            this.showToast = true;
          },
        });
    }
  }

  addSession() {
    if (this.isAddSession && this.userId && this.dateToUpdateSession) {
      this.sessionControllerV2Service
        .createSessionForUserV2({
          date: this.dateToUpdateSession,
          userId: this.userId,
        })
        .subscribe({
          next: (response: number) => {
            //this.showEditSessionModal.emit(false);
            window.location.reload();
          },
          error: (error) => {
            this.toastErrorMessage = 'Seans oluşturulurken bir hata oluştu';
            this.showToast = true;
          },
        });
    }
  }

  updateSessionDate() {
    if (this.dateToUpdateSession && this.session) {
      this.sessionControllerV2Service
        .updateSessionDateV2({
          body: {
            date: this.dateToUpdateSession,
            sessionId: this.session!.sessionId,
          },
        })
        .subscribe({
          next: () => {
            window.location.reload();
          },
          error: (error) => {
            this.toastErrorMessage =
              'Seans tarihi güncellenirken bir hata oluştu';
            this.showToast = true;
          },
        });
    }
  }

  changeStatus(status: string) {
    const sessionStatusRequest: SessionStatusRequest = {
      sessionId: this.session!.sessionId,
      sessionStatusType: status as
        | 'AWAITING_THERAPIST_APPROVAL'
        | 'APPOINTMENT_SCHEDULED'
        | 'COMPLETED'
        | 'CANCELED'
        | 'UNAVAILABLE',
    };

    this.sessionControllerV2Service
      .updateSessionStatusV2({ body: sessionStatusRequest })
      .subscribe({
        next: (response: number) => {
          this.session!.sessionStatus = sessionStatusRequest.sessionStatusType;
        },
        error: (error) => {
          this.toastErrorMessage = 'Durum değiştirirken bir hata oluştu';
          this.showToast = true;
        },
      });
  }
}
