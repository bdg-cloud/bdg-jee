import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmationPaiementPanierComponent } from './confirmation-paiement-panier.component';

describe('ConfirmationPaiementPanierComponent', () => {
  let component: ConfirmationPaiementPanierComponent;
  let fixture: ComponentFixture<ConfirmationPaiementPanierComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConfirmationPaiementPanierComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfirmationPaiementPanierComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
