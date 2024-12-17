import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TestCardDetailComponent } from './test-card-detail.component';

describe('TestCardDetailComponent', () => {
  let component: TestCardDetailComponent;
  let fixture: ComponentFixture<TestCardDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TestCardDetailComponent]
    });
    fixture = TestBed.createComponent(TestCardDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
