import { Injectable } from '@angular/core';
import { Adapter } from './adapter';

export class CompteEspaceClient {
    constructor(
            public id : number,
            public hostname : string,
            public port : string,
            public codeTiers : string,
            public nom : string,
            public prenom : string,
            public email : string,
            public password : string,
            public passwordConf : string,
            public actif : boolean,
            
            public infoCompteCryptees : string,
            
            public token: string,
            
            public accessToken: string,
            public refreshToken: string,
//            public tenantCode: string,
//            public tokenExpiration: string,
    ){}
    
}

@Injectable({
    providedIn: 'root'
})
export class CompteEspaceClientAdapter implements Adapter<CompteEspaceClient> {

  adapt(item: any): CompteEspaceClient {
      console.log(item);
    return new CompteEspaceClient(
      item.id,
      item.hostname,
      item.port,
      item.codeTiers,
      item.nom,
      item.prenom,
      item.email,
      item.password,
      item.passwordConf,
      item.actif,
      item.infoCompteCryptees,
      item.token,
      item.accessToken,
      item.refreshToken
    );
  }
}
