import { Injectable } from '@angular/core';
import { Adapter } from './adapter';

export class ParamEmailConfirmationCommandeBoutique {
    constructor(
            public id : number,
            public idTiers : number,
            public codeTiers : string,
            
            public email : string,
            public idcompteEspaceClient : number,
            public emailCompteEspaceClient : string,
            public codePanier : string,
            public codeCommande : string,

    ){}
   
}

@Injectable({
    providedIn: 'root'
})
export class ParamEmailConfirmationCommandeBoutiqueAdapter implements Adapter<ParamEmailConfirmationCommandeBoutique> {

  adapt(item: any): ParamEmailConfirmationCommandeBoutique {
      console.log(item);
    return new ParamEmailConfirmationCommandeBoutique(
      item.id,
      item.idTiers,
      item.codeTiers,
      
      item.email,
      item.idcompteEspaceClient,
      item.emailCompteEspaceClient,
      item.codePanier,
      item.codeCommande,
    );
  }
}

