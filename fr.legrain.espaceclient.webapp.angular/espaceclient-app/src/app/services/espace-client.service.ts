import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';

import { Publicite } from '../model/publicite';
import { CompteEspaceClient, CompteEspaceClientAdapter } from '../model/compteEspaceClient';
import { ParametreEspaceClient } from '../model/parametreEspaceClient';
import { Tiers, TiersAdapter } from '../model/tiers';
import { AppSettings } from '../env';
import { Adresse, Adressedapter } from '../model/adresse';

@Injectable({
  providedIn: 'root'
})
export class EspaceClientService {

  private _url = "";

  constructor(private http: HttpClient, private tiersAdapter: TiersAdapter, private compteEspaceClientAdapter: CompteEspaceClientAdapter,
    private adressedapter: Adressedapter, private appSettings: AppSettings) {

    this._url = this.appSettings.BASE_API_ENDPOINT + '/auth';
  }

  public emailCreerCompteEspaceClient(compte: CompteEspaceClient): Observable<CompteEspaceClient> {
    var aaaa = this.appSettings.BASE_API_ENDPOINT + '/espace-client/email-creer-compte-espace-client';
    return this.http.post<any>(aaaa, compte,
      this.appSettings.httpOptionsCTJson
    ).pipe(map(res => res));
  }

  public creerCompteEspaceClient(compte: CompteEspaceClient): Observable<CompteEspaceClient> {
    var aaaa = this.appSettings.BASE_API_ENDPOINT + '/espace-client/creer-compte-espace-client';
    return this.http.post<any>(aaaa, compte,
      this.appSettings.httpOptionsCTJson
    ).pipe(map((data: any) => this.compteEspaceClientAdapter.adapt(data)));
  }

  public liaisonNouveauCompteEspaceClient(compte: CompteEspaceClient): Observable<CompteEspaceClient> {
    var aaaa = this.appSettings.BASE_API_ENDPOINT + '/espace-client/liaison-nouveau-compte-espace-client';
    return this.http.put<any>(aaaa, compte,
      this.appSettings.httpOptionsCTJson
    ).pipe(map((data: any) => this.compteEspaceClientAdapter.adapt(data)));
  }

  public modifierCompteEspaceClient(compte: CompteEspaceClient): Observable<CompteEspaceClient> {
    var aaaa = this.appSettings.BASE_API_ENDPOINT + '/espace-client/modifier-compte-espace-client';
    return this.http.put<any>(aaaa, compte,
      this.appSettings.httpOptionsCTJson
    ).pipe(map((data: any) => this.compteEspaceClientAdapter.adapt(data)));
  }

  public emailPublcite(pub: Publicite): Observable<any> {
    let currentUser = JSON.parse(localStorage.getItem('currentUser'));
    var a: CompteEspaceClientAdapter = new CompteEspaceClientAdapter();
    var cec: CompteEspaceClient = a.adapt(currentUser);

    pub.email = cec.email;
    pub.codeTiers = cec.codeTiers;

    var aaaa = this.appSettings.BASE_API_ENDPOINT + '/espace-client/email-publicite';
    return this.http.post<any>(aaaa, pub,
      this.appSettings.httpOptionsCTJson
    ).pipe(map(res => res));
  }



  public demandeMdpCompteEspaceClient(compte: CompteEspaceClient): Observable<CompteEspaceClient> {
    var aaaa = this.appSettings.BASE_API_ENDPOINT + '/espace-client/demande-mdp';
    return this.http.post<any>(aaaa, compte,
      this.appSettings.httpOptionsCTJson
    ).pipe(map(res => res));
    
  }

  public confirmeMdpCompteEspaceClient(compte: CompteEspaceClient): Observable<CompteEspaceClient> {
    var aaaa = this.appSettings.BASE_API_ENDPOINT + '/espace-client/confirme-mdp';
    return this.http.post<any>(aaaa, compte,
      this.appSettings.httpOptionsCTJson
    ).pipe(map(res => res));
  }

  public crypte(compte: CompteEspaceClient): Observable<CompteEspaceClient> {
    var aaaa = this.appSettings.BASE_API_ENDPOINT + '/espace-client/crypter';
    return this.http.post<any>(aaaa, compte,
      this.appSettings.httpOptionsCTJson
    ).pipe(map(res => res));
  }

  public decrypte(compte: CompteEspaceClient): Observable<CompteEspaceClient> {
    var aaaa = this.appSettings.BASE_API_ENDPOINT + '/espace-client/decrypter';
    return this.http.post<any>(aaaa, compte,
      this.appSettings.httpOptionsCTJson
    ).pipe(map(res => res));
  }

  public parametres(): Observable<ParametreEspaceClient> {
    var aaaa = this.appSettings.BASE_API_ENDPOINT + '/espace-client/parametres';
    return this.http.get<any>(aaaa,/*compte,*/
      this.appSettings.httpOptionsCTJson
    ).pipe(map(res => res));
  }

  public logo(): Observable<Blob> {

    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/espace-client/logo-login';

    return this.http.get(
      urlAvecParam,
      this.appSettings.httpOptionsFile
    );
  }

  public backgroundLogin(): Observable<Blob> {

    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/espace-client/background-login';

    return this.http.get(
      urlAvecParam,
      this.appSettings.httpOptionsFile
    );
  }

  // public download(fichier:string) :Observable<Blob>{   
  public download(fichier: string): Observable<HttpEvent<any>> {
    var codeDossierFournisseur = this.appSettings.BDG_TENANT_DOSSIER;

    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/espace-client/setup'
      // urlAvecParam = 'https://build.legrain.dev/crumbIssuer/api/xml?xpath=concat(//crumbRequestField,":",//crumb)';
      //urlAvecParam = 'https://build.legrain.dev/job/setup_bdg_general_N1_product/lastSuccessfulBuild/artifact/setup_bdg_general_n1.exe'

      + "?codeDossierFournisseur=" + codeDossierFournisseur
      + "&fichier=" + fichier;

    /*  return this.http.get(
              urlAvecParam,
              this.appSettings.httpOptionsFile
              );
  */
    return this.http.get(urlAvecParam, {
      responseType: "blob", reportProgress: true, observe: "events", headers: new HttpHeaders(
        { 'Content-Type': 'application/json' },
      )
    });
  }

  nomFichier(fichier: String): Observable<string> {
    var codeDossierFournisseur = this.appSettings.BDG_TENANT_DOSSIER;

    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/espace-client/nom-fichier'
      + "?codeDossierFournisseur=" + codeDossierFournisseur
      + "&fichier=" + fichier;

    return this.http.get<any>(
      urlAvecParam,
      this.appSettings.httpOptionsAcceptJson
    ).pipe(map(res => res['b']));

  }

  public logos(nomLogo: string): Observable<Blob> {

    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/espace-client/logos?nomLogo=' + nomLogo;

    return this.http.get(
      urlAvecParam,
      this.appSettings.httpOptionsFile
    );
  }

  public logoHeader(): Observable<Blob> {

    return this.logos('header');
  }

  public logoPageSimple(): Observable<Blob> {

    return this.logos('page_simple');
  }

  public logoFooter(): Observable<Blob> {

    return this.logos('footer');
  }

  public infosTiers(): Observable<Tiers> {
    var aaaa = this.appSettings.BASE_API_ENDPOINT + '/espace-client/infos-tiers?codeTiers=' + this.appSettings.CODE_CLIENT_CHEZ_FRS;
    return this.http.get<any>(aaaa,/*compte,*/
      this.appSettings.httpOptionsCTJson
    ).pipe(map(item => this.tiersAdapter.adapt(item)));
  }

  public infosCompteEspaceClient(): Observable<CompteEspaceClient> {
    let currentUser = JSON.parse(localStorage.getItem('currentUser'));
    var a: CompteEspaceClientAdapter = new CompteEspaceClientAdapter();
    var cec: CompteEspaceClient = a.adapt(currentUser);
    return of(cec);
  }

  ajouteAdresse(codeClientChezCeForunisseur: String, 
    tiers:Tiers, nouvelleAdresse:Adresse): Observable<Tiers> {

    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/espace-client/tiers/'+tiers.id+'/adresses'

    return this.http.post(
    urlAvecParam,nouvelleAdresse,
    this.appSettings.httpOptionsAcceptJson
    ).pipe(
    // Adapt each item in the raw data array
    map((data: any) => this.tiersAdapter.adapt(data)),
    );
  }
  
  modifAdresse(codeClientChezCeForunisseur: String, 
      tiers:Tiers, adresseAModifier:Adresse): Observable<Adresse> {
  
      var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/espace-client/tiers/'+tiers.id+'/adresses/'+adresseAModifier.id
  
      return this.http.put(
      urlAvecParam,adresseAModifier,
      this.appSettings.httpOptionsAcceptJson
      ).pipe(
      // Adapt each item in the raw data array
      map((data: any) => this.adressedapter.adapt(data)),
      );
  }

  finaliseNouveauTiers(codeClientChezCeForunisseur: String, 
    tiersAModifier:Tiers): Observable<Tiers> {

    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/espace-client/tiers/finalise-nouveau-tiers/'+tiersAModifier.id

    return this.http.put(
    urlAvecParam,tiersAModifier,
    this.appSettings.httpOptionsAcceptJson
    ).pipe(
    // Adapt each item in the raw data array
    map((data: any) => this.tiersAdapter.adapt(data)),
    );
}

}
