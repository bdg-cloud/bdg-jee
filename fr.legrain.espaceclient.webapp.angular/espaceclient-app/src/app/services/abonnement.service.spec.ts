import { TestBed } from '@angular/core/testing';

import { AbonnementService } from './abonnement.service';

describe('AbonnementService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: AbonnementService = TestBed.get(AbonnementService);
    expect(service).toBeTruthy();
  });
});
