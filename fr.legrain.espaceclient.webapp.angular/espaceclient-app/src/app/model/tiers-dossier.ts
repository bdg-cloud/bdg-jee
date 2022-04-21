import { Injectable } from '@angular/core';
import { Adapter } from './adapter';

export class TiersDossier {
    constructor(
            public codeDossier : string,
            public nomTiers : string,
            public codeTiers : string,
            public nomEntreprise : string,
    //public taTiers: Tiers,
    //    public taTiers: any,
    ){}
    
}

@Injectable({
    providedIn: 'root'
})
export class TiersDossierAdapter implements Adapter<TiersDossier> {

  adapt(item: any): TiersDossier {
      console.log(item);
    return new TiersDossier(
      item.codeDossier,
      item.taTiersDTO.nomTiers,
      item.taTiersDTO.codeTiers,
      item.taTiersDTO.nomEntreprise,
//      item.code,
//      item.name,
//      new Date(item.created),
    );
  }
}