import {
  Component,
  EventEmitter,
  HostListener,
  Input,
  Output,
} from '@angular/core';
import { TestTemplateResponse } from 'src/app/services/models';
import { TestTemplateAdminService } from 'src/app/services/services';
import { debounceTime } from 'rxjs/operators';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-test-card-list',
  templateUrl: './test-card-list.component.html',
  styleUrls: ['./test-card-list.component.scss'],
})
export class TestCardListComponent {
  @Input() testCardList: TestTemplateResponse[] = [];

  testTemplateList: TestTemplateResponse[] = [];

  @Input() isEditPage: boolean = false;
  @Output() editTestTemplateEvent = new EventEmitter<TestTemplateResponse>();

  @Output() addTestEvent = new EventEmitter<void>();

  isScreenSmall: boolean = false;
  isScreenMedium: boolean = false;
  isScreenLarge: boolean = false;
  title: string = 'Psikolojik Testler';
  text: string =
    'Kendinizi daha iyi tanımak için tasarlanmış çeşitli psikolojik testlerimizi deneyebilirsiniz. Bu testler kişilik özellikleri, duygusal durum ve davranış kalıplarınız hakkında içgörü kazanmanıza yardımcı olacaktır.';

  private resizeSubject = new Subject<void>();

  constructor(private testTemplateAdminService: TestTemplateAdminService) {
    this.resizeSubject
      .pipe(debounceTime(1000))
      .subscribe(() => this.updateScreenSize());
    this.updateScreenSize();
  }

  ngOnInit() {
    if (this.testCardList.length === 0) {
      this.fetchTestList();
    }
  }

  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    this.resizeSubject.next();
  }

  editTestTemplate(testTemplate: TestTemplateResponse) {
    this.editTestTemplateEvent.emit(testTemplate);
  }

  deleteTestTemplate(testTemplate: TestTemplateResponse) {
    if (this.isEditPage) {
      this.testTemplateAdminService.deleteTestTemplate({ id: testTemplate.id! }).subscribe({
        next: () => {
          this.testTemplateList = this.testTemplateList.filter(
            (t) => t.id !== testTemplate.id
          );
        },
        error: (err: any) => {
          console.error('Error deleting test template', err);
        },
      });
    }
  }

  addTest() {
    this.addTestEvent.emit();
  }

  updateScreenSize() {
    this.isScreenSmall = window.innerWidth < 576;
    this.isScreenMedium = window.innerWidth < 768 && window.innerWidth >= 576;
    this.isScreenLarge = window.innerWidth >= 768;
  }

  fetchTestList() {
    if (this.isEditPage) {
      this.testTemplateAdminService.getAllTestTemplates().subscribe({
        next: (testTemplates: Array<TestTemplateResponse>) => {
          this.testTemplateList = testTemplates || [];
        },
        error: (err: any) => {
          console.error('Error fetching test templates', err);
        },
      });
    }
  }
}
