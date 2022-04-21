import { TestBed } from '@angular/core/testing';

import { AvisEcheanceService } from './avis-echeance.service';

describe('AvisEcheanceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: AvisEcheanceService = TestBed.get(AvisEcheanceService);
    expect(service).toBeTruthy();
  });
});
