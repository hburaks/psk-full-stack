import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BlogCardDetailComponent } from './blog-card-detail.component';

describe('BlogCardDetailComponent', () => {
  let component: BlogCardDetailComponent;
  let fixture: ComponentFixture<BlogCardDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BlogCardDetailComponent]
    });
    fixture = TestBed.createComponent(BlogCardDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
