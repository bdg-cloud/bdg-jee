import { Injectable } from '@angular/core';
import { Adapter } from './adapter';

export class Publicite {
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
export class PubliciteAdapter implements Adapter<Publicite> {

  adapt(item: any): Publicite {
      console.log(item);
    return new Publicite(
      item.id,
      item.titre,
      item.urlImage,
      
      item.email,
      item.codeTiers,
    );
  }
}

