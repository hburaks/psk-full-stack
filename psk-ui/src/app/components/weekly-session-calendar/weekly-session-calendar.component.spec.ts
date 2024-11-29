import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WeeklySessionCalendarComponent } from './weekly-session-calendar.component';

describe('WeeklySessionCalendarComponent', () => {
  let component: WeeklySessionCalendarComponent;
  let fixture: ComponentFixture<WeeklySessionCalendarComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WeeklySessionCalendarComponent]
    });
    fixture = TestBed.createComponent(WeeklySessionCalendarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
