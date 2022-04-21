import { Injectable } from '@angular/core';
import { Adapter } from './adapter';

export class ParamConfirmationCommandeBoutique {
    constructor(
            public id : number,
            public idTiers : number,
            public codeTiers : string,
            
            public idTypePaiementPrevu : number,
            public idTypeLivraisonPrevu : number,
            
            public idcompteEspaceClient : number,
            public emailCompteEspaceClient : string,
            public codePanier : string,
            public codeCommande : string,

    ){}
   
}

@Injectable({
    providedIn: 'root'
})
export class ParamEmailConfirmationCommandeBoutiqueAdapter implements Adapter<ParamConfirmationCommandeBoutique> {

  adapt(item: any): ParamConfirmationCommandeBoutique {
      console.log(item);
    return new ParamConfirmationCommandeBoutique(
      item.id,
      item.idTiers,
      item.codeTiers,
      
      item.idTypePaiementPrevu,
      item.idTypeLivraisonPrevu,
      item.idcompteEspaceClient,
      item.emailCompteEspaceClient,
      item.codePanier,
      item.codeCommande,
    );
  }
}

