import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MyTestResponse, UserTestListResponse } from 'src/app/services/models';

@Component({
  selector: 'app-user-test-card',
  templateUrl: './user-test-card.component.html',
  styleUrls: ['./user-test-card.component.scss'],
})
export class UserTestCardComponent {
  @Input() testCard!: MyTestResponse;
  @Input() userTest!: UserTestListResponse;

  @Output() startTestEvent = new EventEmitter<MyTestResponse>();
  @Output() startUserTestEvent = new EventEmitter<UserTestListResponse>();

  startTest() {
    this.startTestEvent.emit(this.testCard);
  }

  startUserTest() {
    this.startUserTestEvent.emit(this.userTest);
  }

  get currentTest() {
    return this.userTest || this.testCard;
  }

  get testTitle() {
    return this.userTest?.testTemplateTitle || this.testCard?.title || 'Untitled Test';
  }

  get testSubTitle() {
    return this.userTest?.testTemplateSubTitle || this.testCard?.subTitle || '';
  }

  get testImageUrl() {
    return this.userTest?.testTemplateImageUrl || this.testCard?.imageUrl;
  }

  get isCompleted() {
    return this.userTest?.isCompleted || false;
  }

  get personalNotes() {
    return this.userTest?.personalNotes;
  }
}
