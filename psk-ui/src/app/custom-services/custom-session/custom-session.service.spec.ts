import { TestBed } from '@angular/core/testing';

import { CustomSessionService } from './custom-session.service';

describe('CustomSessionService', () => {
  let service: CustomSessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CustomSessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
