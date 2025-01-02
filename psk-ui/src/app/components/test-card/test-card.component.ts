import { Component, EventEmitter, Input, Output } from '@angular/core';
import { TestService } from 'src/app/custom-services/test/test.service';
import { AdminTestResponse, PublicTestResponse } from 'src/app/services/models';
import { FileControllerService } from 'src/app/services/services';

@Component({
  selector: 'app-test-card',
  templateUrl: './test-card.component.html',
  styleUrls: ['./test-card.component.scss'],
})
export class TestCardComponent {
  @Input() testCard!: AdminTestResponse;
  @Input() isEditPage: boolean = false;
  @Output() editTestEvent = new EventEmitter<AdminTestResponse>();
  @Output() deleteTestEvent = new EventEmitter<AdminTestResponse>();

  @Input() imageUrl: string = '';
  editTest() {
    this.editTestEvent.emit(this.testCard);
  }

  constructor(private fileService: FileControllerService) {
    if (this.imageUrl.length > 0) {
      this.fileService
        .downloadTestFile({ fileName: this.imageUrl })
        .subscribe((response) => {
          console.log(response);
        });
    }
  }

  deleteTest() {
    this.deleteTestEvent.emit(this.testCard);
  }
}
