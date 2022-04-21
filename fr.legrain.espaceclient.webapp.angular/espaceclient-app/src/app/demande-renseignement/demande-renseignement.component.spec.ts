import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DemandeRenseignementComponent } from './demande-renseignement.component';

describe('DemandeRenseignementComponent', () => {
  let component: DemandeRenseignementComponent;
  let fixture: ComponentFixture<DemandeRenseignementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DemandeRenseignementComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DemandeRenseignementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
