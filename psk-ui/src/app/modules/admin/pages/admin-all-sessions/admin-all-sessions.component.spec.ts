import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminAllSessionsComponent } from './admin-all-sessions.component';

describe('AdminAllSessionsComponent', () => {
  let component: AdminAllSessionsComponent;
  let fixture: ComponentFixture<AdminAllSessionsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AdminAllSessionsComponent]
    });
    fixture = TestBed.createComponent(AdminAllSessionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
