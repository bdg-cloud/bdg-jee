import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MesAvisEcheanceComponent } from './mes-avis-echeance.component';

describe('MesAvisEcheanceComponent', () => {
  let component: MesAvisEcheanceComponent;
  let fixture: ComponentFixture<MesAvisEcheanceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MesAvisEcheanceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MesAvisEcheanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
