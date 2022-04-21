import { Injectable } from '@angular/core';
import { Adapter } from './adapter';

export class DemandeRenseignement {
    constructor(
            public id : number,
            public idTiers : number,
            public codeTiers : string,
            
            public email : string,
            public idcompteEspaceClient : number,
            public emailCompteEspaceClient : string,
            public texteDemande : string,
            public idArticle : number,
            public codeArticle : string,
            public libelleArticle : string,

    ){}
   
}

@Injectable({
    providedIn: 'root'
})
export class PubliciteAdapter implements Adapter<DemandeRenseignement> {

  adapt(item: any): DemandeRenseignement {
      console.log(item);
    return new DemandeRenseignement(
      item.id,
      item.idTiers,
      item.codeTiers,
      
      item.email,
      item.idcompteEspaceClient,
      item.emailCompteEspaceClient,
      item.texteDemande,
      item.idArticle,
      item.codeArticle,
      item.libelleArticle,
    );
  }
}

