import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateMySessionComponent } from './create-my-session.component';

describe('CreateMySessionComponent', () => {
  let component: CreateMySessionComponent;
  let fixture: ComponentFixture<CreateMySessionComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CreateMySessionComponent]
    });
    fixture = TestBed.createComponent(CreateMySessionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
