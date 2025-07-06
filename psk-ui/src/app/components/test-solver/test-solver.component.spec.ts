import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TestSolverComponent } from './test-solver.component';

describe('TestSolverComponent', () => {
  let component: TestSolverComponent;
  let fixture: ComponentFixture<TestSolverComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TestSolverComponent]
    });
    fixture = TestBed.createComponent(TestSolverComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});