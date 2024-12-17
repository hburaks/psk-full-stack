import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserTestCardDetailComponent } from './user-test-card-detail.component';

describe('UserTestCardDetailComponent', () => {
  let component: UserTestCardDetailComponent;
  let fixture: ComponentFixture<UserTestCardDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UserTestCardDetailComponent]
    });
    fixture = TestBed.createComponent(UserTestCardDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
