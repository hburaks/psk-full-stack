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
  @Output() showEditSessionModal = new EventEmitter<boolean>();

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
      (status) =>
        status !== 'UNAVAILABLE' && status !== 'AWAITING_PSYCHOLOGIST_APPROVAL'
    );  
  }

  closeModal() {
    this.showEditSessionModal.emit(false);
  }

  editNoteForUser() {
    this.isEditingNoteForUser = true;
  }

  editNoteForPsychologist() {
    this.isEditingNoteForPsychologist = true;
  }

  addNotForUser() {
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
            this.isEditingNoteForUser = false;
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
          },
          error: (error) => {
            this.toastErrorMessage = 'Not eklenirken bir hata oluştu';
            this.showToast = true;
          },
        });
    }
  }

  changeStatus(status: string) {
    this.session!.sessionStatus = status as 'AWAITING_PSYCHOLOGIST_APPROVAL' | 'AWAITING_PAYMENT' | 'AWAITING_PAYMENT_CONFIRMATION' | 'APPOINTMENT_SCHEDULED' | 'COMPLETED' | 'CANCELED' | 'UNAVAILABLE';
    const sessionStatusRequest: SessionStatusRequest = {
      sessionId: this.session!.sessionId,
      sessionStatusType: status as 'AWAITING_PSYCHOLOGIST_APPROVAL' | 'AWAITING_PAYMENT' | 'AWAITING_PAYMENT_CONFIRMATION' | 'APPOINTMENT_SCHEDULED' | 'COMPLETED' | 'CANCELED' | 'UNAVAILABLE',
    };

    this.sessionControllerV2Service.updateSessionStatusV2({ body: sessionStatusRequest }).subscribe({
      next: (response: number) => {
        console.log(response);
        },
        error: (error) => {
          this.toastErrorMessage = 'Durum değiştirirken bir hata oluştu';
          this.showToast = true;
        },
      });
  }
}
