import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-create-my-session',
  templateUrl: './create-my-session.component.html',
  styleUrls: ['./create-my-session.component.scss']
})
export class CreateMySessionComponent {
  closeModal() {
    console.log('closeModal');
  }
  @Output() isShowAddSessionModal = new EventEmitter<boolean>();

  showAddSessionModal(isShowAddSessionModal: boolean) {
    this.isShowAddSessionModal.emit(isShowAddSessionModal);
  }
}
