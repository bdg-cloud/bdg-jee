import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable } from 'rxjs';
import { map} from 'rxjs/operators';

import { Devis, DevisAdapter } from '../model/devis';

import { Tiers } from '../model/tiers';
import { AppSettings } from '../env';
import { DatePipe } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class DevisService {

  private _url = "";

  constructor(private http: HttpClient, private datePipe: DatePipe, private devisAdapter: DevisAdapter,
    private appSettings: AppSettings) {
    this._url = this.appSettings.BASE_API_ENDPOINT + '/factures';
  }

  chercheDevis(codeDossierFournisseur: String, codeTiers: String, debut: Date, fin: Date): Observable<Devis[]> {

    var dateDebutStr = this.datePipe.transform(debut, this.appSettings.DATE_FORMAT);
    var dateFinStr = this.datePipe.transform(fin, this.appSettings.DATE_FORMAT);
    console.log(dateDebutStr);
    console.log(dateFinStr);

    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/devis'
      // +"?codeDossierFournisseur="+codeDossierFournisseur
      + "?codeTiers=" + codeTiers
      + "&debut=" + dateDebutStr + "&fin=" + dateFinStr;

    return this.http.get(
      urlAvecParam,
      this.appSettings.httpOptionsAcceptJson
    ).pipe(
      // Adapt each item in the raw data array
      map((data: any[]) => data.map(item => this.devisAdapter.adapt(item))),
    );

  }

  downloadFileDevis(codeDossierFournisseur: String, codeDocument: String): Observable<Blob> {

    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/devis/pdf'
      //  +"?codeDossierFournisseur="+codeDossierFournisseur
      + "?codeDocument=" + codeDocument;

    return this.http.get(
      urlAvecParam,
      this.appSettings.httpOptionsFile
    );
  }
}
