import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MesFournisseursComponent } from './mes-fournisseurs.component';

describe('MesFournisseursComponent', () => {
  let component: MesFournisseursComponent;
  let fixture: ComponentFixture<MesFournisseursComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MesFournisseursComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MesFournisseursComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
