import { TestBed } from '@angular/core/testing';

import { EspaceClientService } from './espace-client.service';

describe('EspaceClientService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: EspaceClientService = TestBed.get(EspaceClientService);
    expect(service).toBeTruthy();
  });
});
