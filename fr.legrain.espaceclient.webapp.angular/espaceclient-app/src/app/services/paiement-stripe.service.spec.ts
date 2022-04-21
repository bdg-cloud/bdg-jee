import { TestBed } from '@angular/core/testing';

import { PaiementStripeService } from './paiement-stripe.service';

describe('PaiementStripeService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: PaiementStripeService = TestBed.get(PaiementStripeService);
    expect(service).toBeTruthy();
  });
});
