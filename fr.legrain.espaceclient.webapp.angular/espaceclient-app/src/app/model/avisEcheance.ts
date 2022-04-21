import { Injectable } from '@angular/core';
import { Adapter } from './adapter';

export class AvisEcheance {
    constructor(
            public codeDocument : string,
            public libelleDocument : string,
            public dateDocument : Date,
            public netHtCalc : number,
            public netTvaCalc : number,
            public netTtcCalc : number,
            public resteAReglerComplet : number,
            public dateEchDocument : Date,
            public codeTiers : string,
            public codeFacture : string,
    ){}
    
}

@Injectable({
    providedIn: 'root'
})
export class AvisEcheanceAdapter implements Adapter<AvisEcheance> {

  adapt(item: any): AvisEcheance {
      console.log(item);
    return new AvisEcheance(
      item.codeDocument,
      item.libelleDocument,
      new Date(item.dateDocument),
      item.netHtCalc,
      item.netTvaCalc,
      item.netTtcCalc,
      item.resteAReglerComplet,
      new Date(item.dateEchDocument),
      item.codeTiers,
      null,//codeFacture
    );
  }
}
