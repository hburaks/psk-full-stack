import { Component, Input, OnInit, Output } from '@angular/core';

declare var bootstrap: any;

@Component({
  selector: 'app-toast',
  templateUrl: './toast.component.html',
  styleUrls: ['./toast.component.scss'],
})
export class ToastComponent implements OnInit {
  @Input() toastMessage: string = '';
  @Input() toastType: 'success' | 'error' = 'error';
  // Backward compatibility
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

  get toastClass() {
    return this.toastType === 'success' ? 'text-bg-success' : 'text-bg-danger';
  }

  get toastIcon() {
    return this.toastType === 'success' ? 'fas fa-check-circle' : 'fas fa-exclamation-circle';
  }

  get displayMessage() {
    return this.toastMessage || this.toastErrorMessage;
  }
}
