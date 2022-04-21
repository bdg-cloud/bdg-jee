import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ValidationModifMdpComponent } from './validation-modif-mdp.component';

describe('ValidationModifMdpComponent', () => {
  let component: ValidationModifMdpComponent;
  let fixture: ComponentFixture<ValidationModifMdpComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ValidationModifMdpComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ValidationModifMdpComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
