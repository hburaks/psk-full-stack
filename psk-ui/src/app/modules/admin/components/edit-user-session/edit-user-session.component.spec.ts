import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditUserSessionComponent } from './edit-user-session.component';

describe('EditUserSessionComponent', () => {
  let component: EditUserSessionComponent;
  let fixture: ComponentFixture<EditUserSessionComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EditUserSessionComponent]
    });
    fixture = TestBed.createComponent(EditUserSessionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
