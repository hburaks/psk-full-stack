import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GenericCardDetailComponent } from './generic-card-detail.component';

describe('GenericCardDetailComponent', () => {
  let component: GenericCardDetailComponent;
  let fixture: ComponentFixture<GenericCardDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GenericCardDetailComponent]
    });
    fixture = TestBed.createComponent(GenericCardDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
