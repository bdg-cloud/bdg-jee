import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable } from 'rxjs';
import { map} from 'rxjs/operators';

import { Facture, FactureAdapter } from '../model/facture';

import { AppSettings } from '../env';
import { DatePipe } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class FactureService {

  private _url = "";

  constructor(private http: HttpClient, private datePipe: DatePipe, private factureAdapter: FactureAdapter,
    private appSettings: AppSettings) {
    this._url = this.appSettings.BASE_API_ENDPOINT + '/factures';
  }

  //curl -k -X GET -H "Conmethee.biz:8443/rest/espace-client/factures?codeDossierFournisseur=demo&codeClientChezCeFournisseur=CBONVIVRE&debut=2019-01-01&fin=2019-12-31"
  chercheFactures(codeDossierFournisseur: String, codeTiers: String, debut: Date, fin: Date): Observable<Facture[]> {

    var dateDebutStr = this.datePipe.transform(debut, this.appSettings.DATE_FORMAT);
    var dateFinStr = this.datePipe.transform(fin, this.appSettings.DATE_FORMAT);
    console.log(dateDebutStr);
    console.log(dateFinStr);

    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/factures'
      // +"?codeDossierFournisseur="+codeDossierFournisseur
      + "?codeTiers=" + codeTiers
      + "&debut=" + dateDebutStr + "&fin=" + dateFinStr;
    /*
    var urlAvecParam = this._url
       // +"?codeDossierFournisseur="+codeDossierFournisseur
        +"?codeClientChezCeFournisseur="+codeClientChezCeForunisseur
        +"&debut="+dateDebutStr+"&fin="+dateFinStr;
    */

    return this.http.get(
      urlAvecParam,
      this.appSettings.httpOptionsAcceptJson
    ).pipe(
      // Adapt each item in the raw data array
      map((data: any[]) => data.map(item => this.factureAdapter.adapt(item))),
    );

  }

  downloadFile(codeDossierFournisseur: String, codeDocument: String): Observable<Blob> {

    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/factures/pdf'
      //+"?codeDossierFournisseur="+codeDossierFournisseur
      + "?codeDocument=" + codeDocument;

    return this.http.get(
      urlAvecParam,
      this.appSettings.httpOptionsFile
    );
  }
}
