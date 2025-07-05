import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {AdminTestResponse, TestTemplateResponse} from 'src/app/services/models';
import {FileControllerService} from 'src/app/services/services';

@Component({
  selector: 'app-test-card',
  templateUrl: './test-card.component.html',
  styleUrls: ['./test-card.component.scss'],
})
export class TestCardComponent implements OnInit {
  @Input() testCard!: AdminTestResponse;
  @Input() testTemplate!: TestTemplateResponse;
  @Input() isEditPage: boolean = false;
  @Output() editTestEvent = new EventEmitter<AdminTestResponse>();
  @Output() deleteTestEvent = new EventEmitter<AdminTestResponse>();
  @Output() editTestTemplateEvent = new EventEmitter<TestTemplateResponse>();
  @Output() deleteTestTemplateEvent = new EventEmitter<TestTemplateResponse>();

  @Input() imageUrl: string | null = null;

  constructor(private fileService: FileControllerService) {
  }

  ngOnInit(): void {
    if (this.imageUrl && this.imageUrl.length > 0) {
      this.fileService
        .downloadTestFile({ fileName: this.imageUrl })
        .subscribe((response) => {
          console.log(response);
        });
    }
  }

  editTest() {
    this.editTestEvent.emit(this.testCard);
  }

  deleteTest() {
    this.deleteTestEvent.emit(this.testCard);
  }

  editTestTemplate() {
    this.editTestTemplateEvent.emit(this.testTemplate);
  }

  deleteTestTemplate() {
    this.deleteTestTemplateEvent.emit(this.testTemplate);
  }

  get currentData() {
    return this.testTemplate || this.testCard;
  }
}
