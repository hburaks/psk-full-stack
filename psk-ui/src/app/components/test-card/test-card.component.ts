import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {TestTemplateResponse} from 'src/app/services/models';
import {environment} from 'src/environments/environment';

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

  get fullImageUrl() {
    const data = this.currentData;
    if (!data?.imageUrl) {
      return null;
    }
    
    if (data.imageUrl.startsWith('http')) {
      return data.imageUrl;
    }
    
    return `${environment.apiUrl}${data.imageUrl}`;
  }

}
