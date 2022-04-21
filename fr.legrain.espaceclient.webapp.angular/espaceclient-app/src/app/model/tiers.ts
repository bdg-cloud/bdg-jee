import { Injectable } from '@angular/core';
import { Adapter } from './adapter';

export class Tiers {
    constructor(
            public id : number,
            public codeTiers : string,

            public codeTTiers : string,
            
            public codeTEntite : string,
            public nomEntreprise : string,
            
            public codeTCivilite : string,
            public nomTiers : string,
            public prenomTiers : string,
            
            public adresse1Adresse : string,
            public adresse2Adresse : string,
            public adresse3Adresse : string,
            public codepostalAdresse : string,
            public villeAdresse : string,
            public paysAdresse : string,
            
            public adresseEmail : string,
            public adresseWeb : string,
            public numeroTelephone : string,
            
            public siretCompl : string,
            public tvaIComCompl : string,


    ){}
   
}

@Injectable({
    providedIn: 'root'
})
export class TiersAdapter implements Adapter<Tiers> {

  adapt(item: any): Tiers {
      console.log(item);
    return new Tiers(
      item.id,
      item.codeTiers,

      item.codeTTiers,
      
      item.codeTEntite,
      item.nomEntreprise,
      
      item.codeTCivilite,
      item.nomTiers,
      item.prenomTiers,
      
      item.adresse1Adresse,
      item.adresse2Adresse,
      item.adresse3Adresse,
      item.codepostalAdresse,
      item.villeAdresse,
      item.paysAdresse,
      
      item.adresseEmail,
      item.adresseWeb,
      item.numeroTelephone,
      
      item.siretCompl,
      item.tvaIComCompl,

    );
  }
}

