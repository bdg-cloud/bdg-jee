import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { forkJoin, Observable, of } from 'rxjs';
import { catchError, map, switchMap} from 'rxjs/operators';

import { Article, ArticleAdapter } from '../model/article';
import { Categorie, CategorieAdapter, CategorieSelectItemAdapter } from '../model/categorie';

import { Tiers } from '../model/tiers';
import { AppSettings } from '../env';
import { DatePipe } from '@angular/common';
import { DomSanitizer } from '@angular/platform-browser';

import {SelectItem} from 'primeng/api';
import { DemandeRenseignement } from '../model/demande-renseignement';
import { CompteEspaceClient, CompteEspaceClientAdapter } from '../model/compteEspaceClient';

@Injectable({
  providedIn: 'root'
})
export class CatalogueService {

  private _url = "";

  constructor(private http: HttpClient, private datePipe: DatePipe, 
    private articleAdapter: ArticleAdapter,
    private categorieAdapter: CategorieAdapter,
    private categorieSelectItemAdapter: CategorieSelectItemAdapter,
    private sanitizer: DomSanitizer,
    private appSettings: AppSettings) {
    this._url = this.appSettings.BASE_API_ENDPOINT + '/catalogue';
  }

  listeArticleCatalogue(codeDossierFournisseur: String, codeTiers: String, /*codeCategorie: String*/ ): Observable<Article[]> {

    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/catalogue/articles'
      // +"?codeDossierFournisseur="+codeDossierFournisseur
      + "?codeTiers=" + codeTiers
      //+ "&debut=" + dateDebutStr + "&fin=" + dateFinStr;

    return this.http.get(
      urlAvecParam,
      this.appSettings.httpOptionsAcceptJson
    )
    .pipe(
     map((data: any[]) => data.map(item => this.articleAdapter.adapt(item)))
    )
    /*
    //https://blog.danieleghidoli.it/2016/10/22/http-rxjs-observables-angular/
    //les appels séquentiel pour chaque élément de la liste fonctionne switchMap+forkJoin

    .pipe(
      switchMap((articles: Article[]) => {
        if (articles.length > 0) {
          return forkJoin(
            articles.map((article: any) => {
              return this.imageDefautArticleSafeUrl(article.id).
              pipe(
                catchError(error => { //le catchError pour une erreur 404 sur la récupération d'une image par exemple ne fonctionne pas ici (plante tout)
                  if (error.error instanceof ErrorEvent) {
                      console.log(`Error: ${error.error.message}`);
                  } else {
                    console.log(`Error: ${error.message}`);
                  }
                  return of();
              })
              )
              
              .pipe(
                map((safeUrlBlobImageDefaut: any) => {
                  article.imageDefaut = safeUrlBlobImageDefaut;
                  return article;
                })
              )
            })
          );
        }
        return of([]);
      })
    )
    */

  }

  listeArticleCategorieCatalogue(codeDossierFournisseur: String, codeTiers: String, idCategorie: number ): Observable<Article[]> {

    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/catalogue/categories/'+idCategorie+'/articles'
      // +"?codeDossierFournisseur="+codeDossierFournisseur
      + "?codeTiers=" + codeTiers
      //+ "&debut=" + dateDebutStr + "&fin=" + dateFinStr;

    return this.http.get(
      urlAvecParam,
      this.appSettings.httpOptionsAcceptJson
    )
    .pipe(
      // Adapt each item in the raw data array
     map((data: any[]) => data.map(item => this.articleAdapter.adapt(item)))
    )
  }

  listeCategorieCatalogue(codeDossierFournisseur: String, codeTiers: String ): Observable<Categorie[]> {

    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/catalogue/categories'
      // +"?codeDossierFournisseur="+codeDossierFournisseur
      + "?codeTiers=" + codeTiers
      //+ "&debut=" + dateDebutStr + "&fin=" + dateFinStr;

    return this.http.get(
      urlAvecParam,
      this.appSettings.httpOptionsAcceptJson
    )
    .pipe(
      // Adapt each item in the raw data array
     map((data: any[]) => data.map(item => this.categorieAdapter.adapt(item)))
    )
  }

  listeCategorieCatalogueSelectItem(codeDossierFournisseur: String, codeTiers: String ): Observable<SelectItem[]> {

    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/catalogue/categories'
      // +"?codeDossierFournisseur="+codeDossierFournisseur
      + "?codeTiers=" + codeTiers
      //+ "&debut=" + dateDebutStr + "&fin=" + dateFinStr;

    return this.http.get(
      urlAvecParam,
      this.appSettings.httpOptionsAcceptJson
    )
    .pipe(
      // Adapt each item in the raw data array
     map((data: any[]) => data.map(item => this.categorieSelectItemAdapter.adapt(item)))
    )
  }

  ficheArticleCatalogue(codeDossierFournisseur: String, codeTiers: String, idArticle: String ): Observable<Article> {

    //var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/catalogue/article/'+idArticle
    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/catalogue/articles/'+idArticle
      + "?codeTiers=" + codeTiers

    return this.http.get(
      urlAvecParam,
      this.appSettings.httpOptionsAcceptJson
    )
    .pipe(
     map((data: any) => this.articleAdapter.adapt(data)))

  }

  adaptImage(item: any): Article {
    console.log(item);
  let art = new Article(
    item.id,
    item.codeArticle,
    item.libellecArticle,
    /*new Date(item.dateDocument),
    new Date(item.dateEchDocument),*/
    item.libellelArticle,
    item.libelleCatalogueCatWeb,
    item.imageDefaut,
    item.descriptionLongueCatWeb,

    item.prixPrix,
    item.prixttcPrix,
    item.codeUnite,
    item.codeTva,
    item.tauxTva,
    item.nonDisponibleCatalogueWeb,
    item.resumeCatWeb,
    item.prixVariable,
  );

  this.imageDefautArticleBlob(art.id).subscribe((blob: any) => {
    let objectURL = URL.createObjectURL(blob);
    art.imageDefaut = this.sanitizer.bypassSecurityTrustStyle('url(' + objectURL + ')');
   // this.ref.detectChanges();
  });
  return art;
}

  public initImageDefautArticle(art:any): Article {

            var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/catalogue/articles/'+art.id+'/image-defaut';

            this.http.get(
              urlAvecParam,
              this.appSettings.httpOptionsFile
            )
            .subscribe((blob: any) => {
              let objectURL = URL.createObjectURL(blob);
              art.imageDefaut = this.sanitizer.bypassSecurityTrustUrl(objectURL);
              return art;
              },
              error => {
                art.imageDefaut = null;
                return art;
              }
            )
            ;
return art;
  
  }


  public imageDefautArticleBlob(idArticle: number): Observable<Blob> {

    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/catalogue/articles/'+idArticle+'/image-defaut';

    return this.http.get(
      urlAvecParam,
      this.appSettings.httpOptionsFile
    )
  }

  public imageDefautArticleSafeUrl(idArticle: number, taille?:string): Observable<any> {

    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/catalogue/articles/'+idArticle+'/image-defaut';
    if(taille!==null) {
      urlAvecParam += "/"+taille;
    }

    return this.http.get(
      urlAvecParam,
      this.appSettings.httpOptionsFile
    ).pipe(
      map(e => this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(e)))
    );
  }

  public imageDefautCategorieArticleSafeUrl(idCategorie: number): Observable<any> {

    if(idCategorie!==null) {
      var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/catalogue/categories/'+idCategorie+'/image-defaut';

      return this.http.get(
        urlAvecParam,
        this.appSettings.httpOptionsFile
      ).pipe(
        map(e => this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(e)))
      );
    }
  }

  public imageArticleListeId(idArticle: number): Observable<any> {

    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/catalogue/articles/'+idArticle+'/images-ids';

    return this.http.get(
      urlAvecParam,
      this.appSettings.httpOptionsCTJson
    ).pipe(
      map(e => e)
    );
  }

  public imageArticleSafeUrl(idArticle: number, idImage: number): Observable<any> {

    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/catalogue/articles/'+idArticle+'/images/'+idImage;

    return this.http.get(
      urlAvecParam,
      this.appSettings.httpOptionsFile
    ).pipe(
      map(e => this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(e)))
    );
  }

  public emailDemandeRenseignement(art: Article, q:string): Observable<any> {
    var question:DemandeRenseignement = new DemandeRenseignement(null,null,null,null,null,null,null,null,null,null);
    let currentUser = JSON.parse(localStorage.getItem('currentUser'));
    var a: CompteEspaceClientAdapter = new CompteEspaceClientAdapter();
    var cec: CompteEspaceClient = a.adapt(currentUser);

    question.id = cec.id;
    question.idcompteEspaceClient = cec.id;
    question.email = cec.email;
    question.codeTiers = cec.codeTiers;
    question.texteDemande = q;
    question.idArticle = art.id;
    question.codeArticle = art.codeArticle;
    question.libelleArticle = art.libelleCatalogueCatWeb;

    var aaaa = this.appSettings.BASE_API_ENDPOINT + '/catalogue/demande-renseignement';
    return this.http.post<any>(aaaa, question,
      this.appSettings.httpOptionsCTJson
    ).pipe(map(res => res));
  }

}
