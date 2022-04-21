import { Injectable } from '@angular/core';
import { Adapter } from './adapter';

export class Telechargement {
    constructor(
            public id : number,
            public titre : string,
            public urlImage : string,
            
            public codeTiers : string,
            public email : string,
    ){}
   
}

@Injectable({
    providedIn: 'root'
})
export class TelechargementAdapter implements Adapter<Telechargement> {

  adapt(item: any): Telechargement {
      console.log(item);
    return new Telechargement(
      item.id,
      item.titre,
      item.urlImage,
      
      item.email,
      item.codeTiers,
    );
  }
}

