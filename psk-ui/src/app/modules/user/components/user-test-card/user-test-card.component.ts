import { Component, EventEmitter, Input, Output } from '@angular/core';
import { UserTestListResponse } from 'src/app/services/models';

@Component({
  selector: 'app-user-test-card',
  templateUrl: './user-test-card.component.html',
  styleUrls: ['./user-test-card.component.scss'],
})
export class UserTestCardComponent {
  @Input() userTest!: UserTestListResponse;

  @Output() startUserTestEvent = new EventEmitter<UserTestListResponse>();

  startUserTest() {
    this.startUserTestEvent.emit(this.userTest);
  }

  get currentTest() {
    return this.userTest;
  }

  get testTitle() {
    return this.userTest?.testTemplateTitle || 'Untitled Test';
  }

  get testSubTitle() {
    return this.userTest?.testTemplateSubTitle || '';
  }

  get testImageUrl() {
    return this.userTest?.testTemplateImageUrl;
  }

  get isCompleted() {
    return this.userTest?.isCompleted || false;
  }

}
