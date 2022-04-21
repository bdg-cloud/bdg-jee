import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import { TiersDossier, TiersDossierAdapter } from '../model/tiers-dossier';
import { Tiers } from '../model/tiers';
import { AppSettings } from '../env';

@Injectable({
  providedIn: 'root'
})
export class FournisseurService {

    private _url = this.appSettings.BASE_API_ENDPOINT+'/espace-client/fournisseurs'; 

    constructor( private http: HttpClient, private adapter: TiersDossierAdapter, private appSettings: AppSettings) { }

    getFournisseurs (): Observable<TiersDossier[]> {
  
        return this.http.get(
                this._url,
                this.appSettings.httpOptionsCTJson
                ).pipe(
                // Adapt each item in the raw data array
                map((data: any[]) => data.map(item => this.adapter.adapt(item))),
              );

    }

   
 
}
