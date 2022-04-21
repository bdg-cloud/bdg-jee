import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { concat, from, Observable, of } from 'rxjs';
import { map, mergeMap, switchMap, take, toArray} from 'rxjs/operators';

import { Abonnement, AbonnementAdapter } from '../model/abonnement';

import { Tiers } from '../model/tiers';
import { AppSettings } from '../env';
import { DatePipe } from '@angular/common';
import { Echeance, EcheanceAdapter } from '../model/echeance';
import { Panier, PanierAdapter } from '../model/panier';
import { PanierService } from './panier.service';

@Injectable({
  providedIn: 'root'
})
export class AbonnementService {

  private _url = "";

  constructor(private http: HttpClient, private datePipe: DatePipe,
    private abonnementAdapter: AbonnementAdapter, private echeanceAdapter: EcheanceAdapter, private appSettings: AppSettings,
    private panierService: PanierService, private panierAdapter : PanierAdapter) {
    this._url = this.appSettings.BASE_API_ENDPOINT + '/factures';
  }

  chercheAbonnements(codeDossierFournisseur: String, codeTiers: String, debut: Date, fin: Date): Observable<Abonnement[]> {

    var dateDebutStr = this.datePipe.transform(debut, this.appSettings.DATE_FORMAT);
    var dateFinStr = this.datePipe.transform(fin, this.appSettings.DATE_FORMAT);
    console.log(dateDebutStr);
    console.log(dateFinStr);

    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/abonnements'
      // +"?codeDossierFournisseur="+codeDossierFournisseur
      + "?codeTiers=" + codeTiers
      + "&debut=" + dateDebutStr + "&fin=" + dateFinStr;

    return this.http.get(
      urlAvecParam,
      this.appSettings.httpOptionsAcceptJson
    ).pipe(
      // Adapt each item in the raw data array
      map(
        (data: any[]) =>
          data.map(item => this.abonnementAdapter.adapt(item))
      ),
    );

  }

  chercheEcheances(codeDossierFournisseur: String, codeTiers: String, debut: Date, fin: Date): Observable<Echeance[]> {

    //List<String> codeEtats = new ArrayList<String>();
		//	codeEtats.add("doc_suspendu");
		//	codeEtats.add("doc_encours");
    var listeCodeEtat = "[doc_suspendu, doc_encours]";
    var codeModuleBDG = "false";
    console.log(listeCodeEtat);
    console.log(codeModuleBDG);

    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/abonnements/echeances'
      // +"?codeDossierFournisseur="+codeDossierFournisseur
      + "?codeTiers=" + codeTiers
      + "&listeCodeEtat=" + listeCodeEtat
      + "&codeModuleBDG=" + codeModuleBDG;

    return this.http.get(
      urlAvecParam,
      this.appSettings.httpOptionsAcceptJson
    ).pipe(
      // Adapt each item in the raw data array
      map(
        (data: any[]) =>
          data.map(item => this.echeanceAdapter.adapt(item))
      ),
    );

  }

  ajouterLigneEcheancePanier(ech : Echeance, panier : Panier) : Observable<Panier>{
    panier = this.panierService.getPanier();

    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/paniers/'+panier.id+'/lignes-echeances';

    return this.http.post(
      urlAvecParam,ech,
      this.appSettings.httpOptionsAcceptJson
    ).pipe(
    // Adapt each item in the raw data array
      map((data: any) => this.panierAdapter.adapt(data)),
    );
    /*
    @Path("{id}/lignes-echeances")
	public Response postAjouterLigneEcheance(
			@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id,
			@Parameter(required = true, allowEmptyValue = false) TaLEcheanceDTO ldto) {
    */
  }

  ajouterLignesEcheancePanier(ech : Echeance[], panier : Panier) : Observable<Panier>{
    panier = this.panierService.getPanier();

    var ech$ = from(ech);
    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/paniers/'+panier.id+'/lignes-echeances';
/*
    return ech$.pipe(
      take(2),
      switchMap( a => this.ajouterLigneEcheancePanier(a,panier)
    )
    );
*/


    const source = [ 1, 2, 3, 4, 5 ];

const observables = ech.map(x => this.ajouterLigneEcheancePanier(x,panier));
return concat(
  ...observables
)
//.pipe(
// toArray()
//)
;

  }
}
