import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AdminTestCardComponent } from './admin-test-card.component';

describe('AdminTestCardComponent', () => {
  let component: AdminTestCardComponent;
  let fixture: ComponentFixture<AdminTestCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AdminTestCardComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminTestCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
