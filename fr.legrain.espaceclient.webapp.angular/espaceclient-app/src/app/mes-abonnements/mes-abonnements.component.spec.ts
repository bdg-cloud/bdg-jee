import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MesAbonnementsComponent } from './mes-abonnements.component';

describe('MesAbonnementsComponent', () => {
  let component: MesAbonnementsComponent;
  let fixture: ComponentFixture<MesAbonnementsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MesAbonnementsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MesAbonnementsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
