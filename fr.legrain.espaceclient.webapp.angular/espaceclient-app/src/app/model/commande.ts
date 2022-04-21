import { Injectable } from '@angular/core';
import { Adapter } from './adapter';

export class Commande {
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
    ){}
    
}

@Injectable({
    providedIn: 'root'
})
export class CommandeAdapter implements Adapter<Commande> {

  adapt(item: any): Commande {
      console.log(item);
    return new Commande(
      item.codeDocument,
      item.libelleDocument,
      new Date(item.dateDocument),
      item.netHtCalc,
      item.netTvaCalc,
      item.netTtcCalc,
      item.resteAReglerComplet,
      new Date(item.dateEchDocument),
      item.codeTiers,

    );
  }
}
