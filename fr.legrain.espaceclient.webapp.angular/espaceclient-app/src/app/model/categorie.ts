import { Injectable } from '@angular/core';
import { Adapter } from './adapter';
import {SelectItem} from 'primeng/api';
  
export class Categorie {
    constructor(
            public id : number,
            public categorieMereArticle : string,
            public codeCategorieArticle : string,
            public libelleCategorieArticle : string,
            public desciptionCategorieArticle : string,
            public urlRewritingCategorieArticle : string,
            public cheminImageCategorieArticle : string,
            public nomImageCategorieArticle : string,
    ){}
    
}

@Injectable({
    providedIn: 'root'
})
export class CategorieAdapter implements Adapter<Categorie> {

  adapt(item: any): Categorie {
      console.log(item);
    return new Categorie(
      item.id,
      item.categorieMereArticle,
      item.codeCategorieArticle,
      item.libelleCategorieArticle,
      item.desciptionCategorieArticle,
      item.urlRewritingCategorieArticle,
      item.cheminImageCategorieArticle,
      item.nomImageCategorieArticle,
    );
  }

  
}

@Injectable({
  providedIn: 'root'
})
export class CategorieSelectItemAdapter implements Adapter<SelectItem> {

adapt(item: any): SelectItem {
    console.log(item);
  return {label:item.codeCategorieArticle,value : new Categorie(
    item.id,
    item.categorieMereArticle,
    item.codeCategorieArticle,
    item.libelleCategorieArticle,
    item.desciptionCategorieArticle,
    item.urlRewritingCategorieArticle,
    item.cheminImageCategorieArticle,
    item.nomImageCategorieArticle,
  )
  };
}


}
