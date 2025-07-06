import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PublicTestDetailComponent } from './public-test-detail.component';

describe('PublicTestDetailComponent', () => {
  let component: PublicTestDetailComponent;
  let fixture: ComponentFixture<PublicTestDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PublicTestDetailComponent]
    });
    fixture = TestBed.createComponent(PublicTestDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});