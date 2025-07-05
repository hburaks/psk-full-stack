import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {TestTemplateResponse} from 'src/app/services/models';
import {FileControllerService} from 'src/app/services/services';

@Component({
  selector: 'app-test-card',
  templateUrl: './test-card.component.html',
  styleUrls: ['./test-card.component.scss'],
})
export class TestCardComponent implements OnInit {
  @Input() testTemplate!: TestTemplateResponse;
  @Input() testCard!: TestTemplateResponse; // Legacy property for backward compatibility
  @Input() isEditPage: boolean = false;
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

  editTestTemplate() {
    this.editTestTemplateEvent.emit(this.testTemplate || this.testCard);
  }

  deleteTestTemplate() {
    this.deleteTestTemplateEvent.emit(this.testTemplate || this.testCard);
  }

  get currentData() {
    return this.testTemplate || this.testCard;
  }

  // Legacy methods for backward compatibility
  editTest() {
    this.editTestTemplate();
  }

  deleteTest() {
    this.deleteTestTemplate();
  }
}
