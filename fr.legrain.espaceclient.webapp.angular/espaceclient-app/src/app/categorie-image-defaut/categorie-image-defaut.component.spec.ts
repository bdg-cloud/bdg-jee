import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CategorieImageDefautComponent } from './categorie-image-defaut.component';

describe('CategorieImageDefautComponent', () => {
  let component: CategorieImageDefautComponent;
  let fixture: ComponentFixture<CategorieImageDefautComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CategorieImageDefautComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CategorieImageDefautComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
