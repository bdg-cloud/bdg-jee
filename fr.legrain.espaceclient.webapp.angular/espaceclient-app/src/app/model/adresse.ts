import { Injectable } from '@angular/core';
import { Adapter } from './adapter';
  
export class Adresse {
    constructor(
            public id : number,

            public adresse1Adresse : string,
            public adresse2Adresse : string,
            public adresse3Adresse : string,
            public codepostalAdresse : string,
            public villeAdresse : string,
            public paysAdresse : string,

            public idTAdr : number,
            public idTiers : number,
            public codeTAdr : string,
            public ordre : number,

            public livraison : string,
            public latitudeDec : string,
            public longitudeDec : string,
            public defaut : boolean,
    ){}
    
}

@Injectable({
    providedIn: 'root'
})
export class Adressedapter implements Adapter<Adresse> {

  adapt(item: any): Adresse {
      console.log(item);
    return new Adresse(
      item.id,
      item.adresse1Adresse,
      item.adresse2Adresse,
      item.adresse3Adresse,
      item.codepostalAdresse,
      item.villeAdresse,
      item.paysAdresse,

      item.idTAdr,
      item.idTiers,
      item.codeTAdr,
      item.ordre,
      item.livraison,
      item.latitudeDec,
      item.longitudeDec,
      item.defaut,
    );
  }

  
}
