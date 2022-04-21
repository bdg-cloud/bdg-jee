import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ArticleImageDefautComponent } from './article-image-defaut.component';

describe('ArticleImageDefautComponent', () => {
  let component: ArticleImageDefautComponent;
  let fixture: ComponentFixture<ArticleImageDefautComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ArticleImageDefautComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ArticleImageDefautComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
