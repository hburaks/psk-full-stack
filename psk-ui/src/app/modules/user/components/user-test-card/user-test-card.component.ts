import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MyTestResponse } from 'src/app/services/models';

@Component({
  selector: 'app-user-test-card',
  templateUrl: './user-test-card.component.html',
  styleUrls: ['./user-test-card.component.scss'],
})
export class UserTestCardComponent {
  @Input() testCard!: MyTestResponse;

  @Output() startTestEvent = new EventEmitter<MyTestResponse>();

  startTest() {
    this.startTestEvent.emit(this.testCard);
  }
}
