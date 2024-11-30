import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpcomingSessionComponent } from './upcoming-session.component';

describe('UpcomingSessionComponent', () => {
  let component: UpcomingSessionComponent;
  let fixture: ComponentFixture<UpcomingSessionComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UpcomingSessionComponent]
    });
    fixture = TestBed.createComponent(UpcomingSessionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
