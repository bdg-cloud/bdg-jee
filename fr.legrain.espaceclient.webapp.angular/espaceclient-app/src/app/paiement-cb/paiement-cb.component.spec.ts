import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PaiementCBComponent } from './paiement-cb.component';

describe('PaiementCBComponent', () => {
  let component: PaiementCBComponent;
  let fixture: ComponentFixture<PaiementCBComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PaiementCBComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PaiementCBComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
