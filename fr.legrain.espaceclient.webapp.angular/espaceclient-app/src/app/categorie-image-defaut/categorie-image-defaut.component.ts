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
    selector: 'app-categorie-image-defaut',
    templateUrl: './categorie-image-defaut.component.html',
    styleUrls: ['./categorie-image-defaut.component.css']
  })
  export class CategorieImageDefautComponent implements OnInit {
    // This code block just creates an rxjs stream from the src
    // this makes sure that we can handle source changes
    // or even when the component gets destroyed
    // So basically turn src into src$
    @Input() public hauteur: string;
    @Input() public largeur: string;
    @Input() private idCategorie: number;
    @Input() private idImage: number;
  
    private idCategorie$ /*= new BehaviorSubject(this.src);*/;
    private idImage$;
    private _dataUrl$;

    get dataUrl$() {
      return this._dataUrl$;
    }
  
    constructor(private httpClient: HttpClient, private sanitizer: DomSanitizer,
      private catalogueService: CatalogueService,private appSettings: AppSettings) {
   }
  
    ngOnInit() {
      if(this.idCategorie!=null)
        this.idCategorie$ = new BehaviorSubject(this.idCategorie);
      if(this.idImage!=null)
        this.idImage$ = new BehaviorSubject(this.idImage);
  
      // this stream will contain the actual url that our img tag will load
      // everytime the src changes, the previous call would be canceled and the
      // new resource would be loaded
      this._dataUrl$ = this.idCategorie$.pipe(switchMap((idCategorie:number/*, idImage: number*/) => this.loadImage(idCategorie,this.idImage)))
    }
  
    ngOnChanges(): void {
      if(this.idCategorie$!=null)
        this.idCategorie$.next(this.idCategorie);
      if(this.idImage$!=null)
        this.idImage$.next(this.idImage);
    }
  
    private loadImage(idArticle: number, idImage: number): Observable<any> {
  
      if(idImage!=null)
        console.log('non implémenté');//return this.catalogueService.imageArticleSafeUrl(idArticle,idImage);
      else
        return this.catalogueService.imageDefautCategorieArticleSafeUrl(idArticle);
    }
  
  }
  
