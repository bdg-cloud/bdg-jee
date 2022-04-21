import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ChoixCompteTiersComponent } from './choix-compte-tiers.component';

describe('ChoixCompteTiersComponent', () => {
  let component: ChoixCompteTiersComponent;
  let fixture: ComponentFixture<ChoixCompteTiersComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ChoixCompteTiersComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChoixCompteTiersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
