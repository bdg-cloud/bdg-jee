import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ValidationCreationCompteComponent } from './validation-creation-compte.component';

describe('ValidationCreationCompteComponent', () => {
  let component: ValidationCreationCompteComponent;
  let fixture: ComponentFixture<ValidationCreationCompteComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ValidationCreationCompteComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ValidationCreationCompteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
