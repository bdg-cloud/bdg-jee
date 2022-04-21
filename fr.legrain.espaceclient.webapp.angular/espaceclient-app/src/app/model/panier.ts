import { Injectable } from '@angular/core';
import { Adapter } from './adapter';
import { InfosPanier, InfosPanierAdapter } from './infosPanier';
import { LignePanier, LignePanierAdapter } from './lignePanier';

export class Panier {
    public lignes: LignePanier[] = [];
    public infos: InfosPanier;
    constructor(
            public id : number,
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
export class PanierAdapter implements Adapter<Panier> {

  adapt(item: any): Panier {
      console.log(item);
      var lpa:LignePanierAdapter = new LignePanierAdapter();
      var ipa:InfosPanierAdapter = new InfosPanierAdapter();
      var p:Panier = new Panier(
        item.id,
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
      if(item.lignes) {
        item.lignes.forEach(l => {
          p.lignes.push(lpa.adapt(l));
        });
      }
      if(item.infos) {
        p.infos = ipa.adapt(item.infos);
      }
      

    return p;
  }
}
