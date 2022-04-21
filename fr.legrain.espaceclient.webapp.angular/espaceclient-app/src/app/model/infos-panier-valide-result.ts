import { Injectable } from '@angular/core';
import { Adapter } from './adapter';

export class InfosPanierValideResult {
    constructor(
            public id : number,
            public codePanier : string,
            public codeCommande : string,
            public codeFacture : string,
            public codeReglement : string,
            public codeReglementInternet : string,
            public montantHT : number,
            public montantTTC : number,

    ){}
   
}

@Injectable({
    providedIn: 'root'
})
export class InfosPanierValideResultAdapter implements Adapter<InfosPanierValideResult> {

  adapt(item: any): InfosPanierValideResult {
      console.log(item);
    return new InfosPanierValideResult(
      item.id,
      item.codePanier,
      item.codeCommande,
      item.codeFacture,
      item.codeReglement,
      item.codeReglementInternet,
      item.montantHT,
      item.montantTTC,
    );
  }
}

