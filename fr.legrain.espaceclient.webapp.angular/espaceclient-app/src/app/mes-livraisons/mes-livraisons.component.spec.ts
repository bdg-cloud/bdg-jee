import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MesLivraisonsComponent } from './mes-livraisons.component';

describe('MesLivraisonsComponent', () => {
  let component: MesLivraisonsComponent;
  let fixture: ComponentFixture<MesLivraisonsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MesLivraisonsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MesLivraisonsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
