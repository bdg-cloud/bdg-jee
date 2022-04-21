import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { Facture, FactureAdapter } from '../model/facture';
import { AvisEcheance, AvisEcheanceAdapter } from '../model/avisEcheance';

import { Tiers } from '../model/tiers';
import { AppSettings } from '../env';
import { DatePipe } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class AvisEcheanceService {

  private _url = "";

  constructor(private http: HttpClient, private datePipe: DatePipe, private factureAdapter: FactureAdapter, 
    private avisEcheanceAdapter: AvisEcheanceAdapter, private appSettings: AppSettings) {
    this._url = this.appSettings.BASE_API_ENDPOINT + '/factures';
  }

  chercheAvisEcheance(codeDossierFournisseur: String, codeTiers: String, debut: Date, fin: Date): Observable<AvisEcheance[]> {

    var dateDebutStr = this.datePipe.transform(debut, this.appSettings.DATE_FORMAT);
    var dateFinStr = this.datePipe.transform(fin, this.appSettings.DATE_FORMAT);
    console.log(dateDebutStr);
    console.log(dateFinStr);

    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/avis-echeance'
      // +"?codeDossierFournisseur="+codeDossierFournisseur
      + "?codeTiers=" + codeTiers
      + "&debut=" + dateDebutStr + "&fin=" + dateFinStr;

    return this.http.get(
      urlAvecParam,
      this.appSettings.httpOptionsAcceptJson
    ).pipe(
      // Adapt each item in the raw data array
      map((data: any[]) => data.map(item => this.avisEcheanceAdapter.adapt(item))),
    );

  }

  downloadFileAvisEcheance(codeDossierFournisseur: String, codeDocument: String): Observable<Blob> {

    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/avis-echeance/pdf'
      // +"?codeDossierFournisseur="+codeDossierFournisseur
      + "?codeDocument=" + codeDocument;

    return this.http.get(
      urlAvecParam,
      this.appSettings.httpOptionsFile
    );
  }

  facturePourAvisEcheance(codeDossierFournisseur: String, codeAvisEcheance: String): Observable<Facture> {

    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/avis-echeance/facture-pour-avis-echeance'
      // +"?codeDossierFournisseur="+codeDossierFournisseur
      + "?codeAvisEcheance=" + codeAvisEcheance;

    return this.http.get(
      urlAvecParam,
      this.appSettings.httpOptionsAcceptJson
    ).pipe(
      // Adapt each item in the raw data array
      map((data: any) => data.map(item => this.factureAdapter.adapt(item))),
    );

  }
}
