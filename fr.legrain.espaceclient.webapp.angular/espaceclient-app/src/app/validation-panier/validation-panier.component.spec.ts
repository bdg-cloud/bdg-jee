import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ValidationPanierComponent } from './validation-panier.component';

describe('ValidationPanierComponent', () => {
  let component: ValidationPanierComponent;
  let fixture: ComponentFixture<ValidationPanierComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ValidationPanierComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ValidationPanierComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
