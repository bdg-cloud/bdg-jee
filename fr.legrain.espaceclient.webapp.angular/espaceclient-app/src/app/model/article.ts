import { Injectable } from '@angular/core';
import { Adapter } from './adapter';
  
export class Article {
    constructor(
            public id : number,
            public codeArticle : string,
            public libellecArticle : string,
            /*public dateDocument : Date,
            public dateEchDocument : Date,*/
            public libellelArticle : string,
            public libelleCatalogueCatWeb : string,
            public imageDefaut : any,
            public descriptionLongueCatWeb : string,

            public prixPrix : number,
            public prixttcPrix : number,
            public codeUnite : string,
            public codeTva : string,
            public tauxTva : number,
            public nonDisponibleCatalogueWeb : any,
            public resumeCatWeb: string,
            public prixVariable: boolean,
    ){}
    
}

@Injectable({
    providedIn: 'root'
})
export class ArticleAdapter implements Adapter<Article> {

  adapt(item: any): Article {
      console.log(item);
    return new Article(
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
  }

  
}
