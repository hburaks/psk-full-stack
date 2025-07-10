import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {TestTemplateResponse} from 'src/app/services/models';

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

  constructor() {
  }

  ngOnInit(): void {
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

}
