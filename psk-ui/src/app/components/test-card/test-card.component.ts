import { Component, EventEmitter, Input, Output } from '@angular/core';
import { AdminTestResponse, PublicTestResponse } from 'src/app/services/models';

@Component({
  selector: 'app-test-card',
  templateUrl: './test-card.component.html',
  styleUrls: ['./test-card.component.scss']
})
export class TestCardComponent {
  @Input() testCard!: AdminTestResponse;
  @Input() isEditPage: boolean = false;
  @Output() editTestEvent = new EventEmitter<AdminTestResponse>();
  @Output() deleteTestEvent = new EventEmitter<AdminTestResponse>();
  editTest() {
    this.editTestEvent.emit(this.testCard);
  }

  deleteTest() {
    this.deleteTestEvent.emit(this.testCard);
  }
}
