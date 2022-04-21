import { Injectable } from '@angular/core';
import { Adapter } from './adapter';

export class LignePanier {
    constructor(
            public numLigneLDocument : string,
            public idArticle : number,
            public codeArticle: string,
            public libLDocument: string,
            public qteLDocument : number,
            public u1LDocument : string,
            public prixULDocument : number,
            public tauxTvaLDocument : number,
            public codeTvaLDocument : string,
            public mtHtLDocument : number,
            public mtTtcLDocument : number,
            public remTxLDocument : number,
            public remHtLDocument : number,
    ){}
    
}

@Injectable({
    providedIn: 'root'
})
export class LignePanierAdapter implements Adapter<LignePanier> {

  adapt(item: any): LignePanier {
      console.log(item);
    return new LignePanier(
      item.numLigneLDocument,
      item.idArticle,
      item.codeArticle,
      item.libLDocument,
      item.qteLDocument,
      item.u1LDocument,
      item.prixULDocument,
      item.tauxTvaLDocument,
      item.codeTvaLDocument,
      item.mtHtLDocument,
      item.mtTtcLDocument,
      item.remTxLDocument,
      item.remHtLDocument,

    );
  }
}
