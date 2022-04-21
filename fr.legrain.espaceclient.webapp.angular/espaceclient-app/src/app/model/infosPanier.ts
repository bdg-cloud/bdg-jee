import { Injectable } from '@angular/core';
import { Adapter } from './adapter';

export class InfosPanier {
    constructor(
            public idInfosDocument : number,
            public nomTiers : string,
            public prenomTiers : string,
            public surnomTiers : string,
            public codeTCivilite : string,
            public codeTEntite : string,
            public tvaIComCompl : string,
            public codeCPaiement : string,
            public libCPaiement : string,
            public reportCPaiement : number,
            public finMoisCPaiement : number,
            public nomEntreprise : string,

            public adresse1 : string,
            public adresse2 : string,
            public adresse3 : string,
            public codepostal : string,
            public ville : string,
            public pays : string,

            public adresse1Liv : string,
            public adresse2Liv : string,
            public adresse3Liv : string,
            public codepostalLiv : string,
            public villeLiv : string,
            public paysLiv : string,
    ){}
    
}

@Injectable({
    providedIn: 'root'
})
export class InfosPanierAdapter implements Adapter<InfosPanier> {

  adapt(item: any): InfosPanier {
      console.log(item);
      return new InfosPanier(
          item.idInfosDocument,
          item.nomTiers,
          item.prenomTiers,
          item.surnomTiers,
          item.codeTCivilite,
          item.codeTEntite,
          item.tvaIComCompl,
          item.codeCPaiement,
          item.libCPaiement,
          item.reportCPaiement,
          item.finMoisCPaiement,
          item.nomEntreprise,
          item.adresse1,
          item.adresse2,
          item.adresse3,
          item.codepostal,
          item.ville,
          item.pays,
          item.adresse1Liv,
          item.adresse2Liv,
          item.adresse3Liv,
          item.codepostalLiv,
          item.villeLiv,
          item.paysLiv,
    );
  }
}
