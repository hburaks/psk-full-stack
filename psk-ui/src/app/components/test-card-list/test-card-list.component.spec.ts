import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TestCardListComponent } from './test-card-list.component';

describe('TestCardListComponent', () => {
  let component: TestCardListComponent;
  let fixture: ComponentFixture<TestCardListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TestCardListComponent],
    });
    fixture = TestBed.createComponent(TestCardListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
