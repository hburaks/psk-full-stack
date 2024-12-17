import { Component, Input, OnInit, Output } from '@angular/core';

declare var bootstrap: any;

@Component({
  selector: 'app-toast',
  templateUrl: './toast.component.html',
  styleUrls: ['./toast.component.scss'],
})
export class ToastComponent implements OnInit {
  @Input() toastErrorMessage: string = '';

  ngOnInit() {
    this.showToast();
  }

  showToast() {
    const toastElement = document.querySelector('.toast');
    if (toastElement) {
      const bsToast = new bootstrap.Toast(toastElement);
      bsToast.show();
    }
  }
}
