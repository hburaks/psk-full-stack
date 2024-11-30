import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserNavUnderlineComponent } from './user-nav-underline.component';

describe('UserNavUnderlineComponent', () => {
  let component: UserNavUnderlineComponent;
  let fixture: ComponentFixture<UserNavUnderlineComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UserNavUnderlineComponent]
    });
    fixture = TestBed.createComponent(UserNavUnderlineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
