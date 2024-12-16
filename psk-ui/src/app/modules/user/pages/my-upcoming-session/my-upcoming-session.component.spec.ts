import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyUpcomingSessionComponent } from './my-upcoming-session.component';

describe('MyUpcomingSessionComponent', () => {
  let component: MyUpcomingSessionComponent;
  let fixture: ComponentFixture<MyUpcomingSessionComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MyUpcomingSessionComponent]
    });
    fixture = TestBed.createComponent(MyUpcomingSessionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
