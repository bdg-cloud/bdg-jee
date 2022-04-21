import { Component, OnInit, OnChanges, Input } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { switchMap} from 'rxjs/operators';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { map} from 'rxjs/operators';
import { DomSanitizer } from '@angular/platform-browser';

import { CatalogueService } from '../services/catalogue.service';
import { AppSettings } from '../env';

@Component({
  selector: 'app-article-image-defaut',
  templateUrl: './article-image-defaut.component.html',
  styleUrls: ['./article-image-defaut.component.css']
})
export class ArticleImageDefautComponent implements OnInit, OnChanges  {
  // This code block just creates an rxjs stream from the src
  // this makes sure that we can handle source changes
  // or even when the component gets destroyed
  // So basically turn src into src$
  @Input() private idArticle: number;
  @Input() private idImage: number;
  @Input() public hauteur: string;
  @Input() public largeur: string;
  @Input() private urlTest: any;
  @Input() public taille: string;

  public maxWidth: string;
  public maxHeight: string;

  private idArticle$ /*= new BehaviorSubject(this.src);*/;
  private idImage$;
  private _dataUrl$;

  get dataUrl$() {
    return this._dataUrl$;
  }

  constructor(private httpClient: HttpClient, private sanitizer: DomSanitizer,
    private catalogueService: CatalogueService,private appSettings: AppSettings) {
 }

  ngOnInit() {
  console.log(this.hauteur);
    this.idArticle$ = new BehaviorSubject(this.idArticle);
    if(this.idImage!=null)
      this.idImage$ = new BehaviorSubject(this.idImage);

      if(this.taille=="m") {
        if(this.largeur==null) {
          this.maxWidth="113px";
          this.largeur="113px";
        }
        if(this.hauteur==null) {
          this.maxHeight="81px";
          //this.hauteur="81px";
        }
      } else if(this.taille=="l") {
        if(this.largeur==null) {
          this.maxWidth="361px";
          this.largeur="361px";
        }
        if(this.hauteur==null) {
          this.maxHeight="259px";
          //this.hauteur="259px";
        }
      }
      
    // this stream will contain the actual url that our img tag will load
    // everytime the src changes, the previous call would be canceled and the
    // new resource would be loaded
    this._dataUrl$ = this.idArticle$.pipe(switchMap((idArticle:number/*, idImage: number*/) => this.loadImage(idArticle,this.idImage)))
    //this._dataUrl$ = this.urlTest;
  }

  ngOnChanges(): void {
    if(this.idArticle$!=null)
      this.idArticle$.next(this.idArticle);
    if(this.idImage$!=null)
      this.idImage$.next(this.idImage);
  }

  private loadImage(idArticle: number, idImage: number): Observable<any> {

    if(idImage!=null) {
      return this.catalogueService.imageArticleSafeUrl(idArticle,idImage);
    } else {
      if(this.taille==="s")
        return this.catalogueService.imageDefautArticleSafeUrl(idArticle,"s");
      else if(this.taille==="m")
        return this.catalogueService.imageDefautArticleSafeUrl(idArticle,"m");
      else if(this.taille==="l")
        return this.catalogueService.imageDefautArticleSafeUrl(idArticle,"l");
      else
        return this.catalogueService.imageDefautArticleSafeUrl(idArticle);
    }
  }

}
