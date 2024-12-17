import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserTestCardComponent } from './user-test-card.component';

describe('UserTestCardComponent', () => {
  let component: UserTestCardComponent;
  let fixture: ComponentFixture<UserTestCardComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UserTestCardComponent]
    });
    fixture = TestBed.createComponent(UserTestCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
