import {
  Component,
  EventEmitter,
  HostListener,
  Input,
  Output,
} from '@angular/core';
import { AdminTestResponse, PublicTestResponse } from 'src/app/services/models';
import { TestService } from 'src/app/services/services';
import { debounceTime } from 'rxjs/operators';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-test-card-list',
  templateUrl: './test-card-list.component.html',
  styleUrls: ['./test-card-list.component.scss'],
})
export class TestCardListComponent {
  @Input() testCardList: PublicTestResponse[] = [];

  adminTestList: AdminTestResponse[] = [];

  @Input() isEditPage: boolean = false;
  @Output() editTestEvent = new EventEmitter<AdminTestResponse>();

  @Output() addTestEvent = new EventEmitter<void>();

  isScreenSmall: boolean = false;
  isScreenMedium: boolean = false;
  isScreenLarge: boolean = false;
  title: string = 'Psikolojik Testler';
  text: string =
    'Kendinizi daha iyi tanımak için tasarlanmış çeşitli psikolojik testlerimizi deneyebilirsiniz. Bu testler kişilik özellikleri, duygusal durum ve davranış kalıplarınız hakkında içgörü kazanmanıza yardımcı olacaktır.';

  private resizeSubject = new Subject<void>();

  constructor(private testService: TestService) {
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

  editTest(test: AdminTestResponse) {
    this.editTestEvent.emit(test);
  }

  deleteTest(test: AdminTestResponse) {
    if (this.isEditPage) {
      this.testService.deleteTestV2({ testId: test.id! }).subscribe({
        next: () => {
          this.adminTestList = this.adminTestList.filter(
            (t) => t.id !== test.id
          );
        },
        error: (err: any) => {
          console.error('Error deleting test', err);
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
      this.testService.getAllTest().subscribe({
        next: (adminTests: Array<AdminTestResponse>) => {
          this.adminTestList = adminTests || [];
        },
        error: (err: any) => {
          console.error('Error fetching public tests', err);
        },
      });
    }
  }
}
