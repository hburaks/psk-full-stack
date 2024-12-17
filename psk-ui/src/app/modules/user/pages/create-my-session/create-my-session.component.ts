import { Component } from '@angular/core';

@Component({
  selector: 'app-create-my-session',
  templateUrl: './create-my-session.component.html',
  styleUrls: ['./create-my-session.component.scss']
})
export class CreateMySessionComponent {
  closeModal() {
    console.log('closeModal');
  }
}
