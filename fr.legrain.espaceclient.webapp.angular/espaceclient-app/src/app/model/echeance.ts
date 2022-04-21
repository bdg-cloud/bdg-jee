import { Injectable } from '@angular/core';
import { Adapter } from './adapter';

export class Echeance {
    constructor(
            public id : number,
            public codeDocument : string,
            public codeArticle : string,
            public liblArticle : string,
            public libLDocument : string,


            public debutPeriode : Date,
            public finPeriode : Date,
            public dateEcheance : Date,
            public mtTtcLDocument : number,
            public codeEtat : string,

    ){}
    
   
}

@Injectable({
    providedIn: 'root'
})
export class EcheanceAdapter implements Adapter<Echeance> {

  adapt(item: any): Echeance {
      console.log(item);
    return new Echeance(
      item.id,
      item.codeDocument,
      item.codeArticle,
      item.liblArticle,
      item.libLDocument,

      item.debutPeriode?new Date(item.debutPeriode):null,
      item.finPeriode?new Date(item.finPeriode):null,
      item.dateEcheance?new Date(item.dateEcheance):null,
      item.mtTtcLDocument,
      item.codeEtat,
      //true,
    );
  }
}
