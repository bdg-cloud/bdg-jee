import { DatePipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { AppSettings } from '../env';
import { Commande, CommandeAdapter } from '../model/commande';
import { DevisAdapter } from '../model/devis';

@Injectable({
  providedIn: 'root'
})
export class CommandeService {
  
  private _url = "";

  constructor(private http: HttpClient, private datePipe: DatePipe, private commandeAdapter: CommandeAdapter,
    private appSettings: AppSettings) {
    this._url = this.appSettings.BASE_API_ENDPOINT + '/commandes';
  }

  chercheCommande(codeDossierFournisseur: String, codeTiers: String, debut: Date, fin: Date): Observable<Commande[]> {

    var dateDebutStr = this.datePipe.transform(debut, this.appSettings.DATE_FORMAT);
    var dateFinStr = this.datePipe.transform(fin, this.appSettings.DATE_FORMAT);
    console.log(dateDebutStr);
    console.log(dateFinStr);

    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/commandes'
      // +"?codeDossierFournisseur="+codeDossierFournisseur
      + "?codeTiers=" + codeTiers
      + "&debut=" + dateDebutStr + "&fin=" + dateFinStr;

    return this.http.get(
      urlAvecParam,
      this.appSettings.httpOptionsAcceptJson
    ).pipe(
      // Adapt each item in the raw data array
      map((data: any[]) => data.map(item => this.commandeAdapter.adapt(item))),
    );

  }

  downloadFileCommande(codeDossierFournisseur: String, codeDocument: String): Observable<Blob> {

    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/commandes/pdf'
      //  +"?codeDossierFournisseur="+codeDossierFournisseur
      + "?codeDocument=" + codeDocument;

    return this.http.get(
      urlAvecParam,
      this.appSettings.httpOptionsFile
    );
  }
}