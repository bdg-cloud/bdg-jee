import { Injectable } from '@angular/core';
import { HttpClient,  } from '@angular/common/http';
import { Observable, of } from 'rxjs';
//import { catchError, map, tap, takeWhile, of } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { Facture } from '../model/facture';
import { AvisEcheance } from '../model/avisEcheance';
import { AppSettings } from '../env';
import { Panier } from '../model/panier';

@Injectable({
  providedIn: 'root'
})
export class PaiementStripeService {
    
    private _url = this.appSettings.BASE_API_ENDPOINT+'/paiement/paiement-document-cb';
    
    private constructor(private http: HttpClient,private appSettings: AppSettings) { }
  
    public creerPaymentIntent(fact:Facture, montant:number) :Observable<string> {
          
          return this.http.post<any>(this._url,  
                  '{ "codeDossierFournisseur":"'+this.appSettings.BDG_TENANT_DOSSIER+'", "codeClientChezCeFournisseur":"'
                  +fact.codeTiers+'","montant": '
                  +montant+',"codeDocument":"'
                  +fact.codeDocument+'","typeDocument":"Facture"}'
              ,this.appSettings.httpOptionsCTJson
              ).pipe(map(res => res['paymentSecret']));
    } 
    
    public creerPaymentIntentAvisEcheance(fact:AvisEcheance, montant:number) :Observable<string> {
        
        return this.http.post<any>(this._url,  
                '{ "codeDossierFournisseur":"'+this.appSettings.BDG_TENANT_DOSSIER+'", "codeClientChezCeFournisseur":"'
                +fact.codeTiers+'","montant": '
                +montant+',"codeDocument":"'
                +fact.codeDocument+'","typeDocument":"AvisEcheance"}'
            ,this.appSettings.httpOptionsCTJson
            ).pipe(map(res => res['paymentSecret']));
    }

    public creerPaymentIntentPanier(fact:Panier, montant:number) :Observable<string> {
        
        return this.http.post<any>(this._url,  
                '{ "codeDossierFournisseur":"'+this.appSettings.BDG_TENANT_DOSSIER+'", "codeClientChezCeFournisseur":"'
                +fact.codeTiers+'","montant": '
                +montant+',"codeDocument":"'
                +fact.codeDocument+'","typeDocument":"Panier"}'
            ,this.appSettings.httpOptionsCTJson
            ).pipe(map(res => res['paymentSecret']));
    }
    
    fournisseurPermetPaiementParCB(codeDossierFournisseur:String, codeClientChezCeForunisseur:String): Observable<boolean> {
        
        if(this.appSettings.PARAMETRES.paiementCb===true) {
            var urlAvecParam = this.appSettings.BASE_API_ENDPOINT+'/paiement/paiment-cb-possible'
               // +"?codeDossierFournisseur="+codeDossierFournisseur
               // +"?codeClientChezCeFournisseur="+codeClientChezCeForunisseur;;
            
            return this.http.get<any>(
                    urlAvecParam,
                    this.appSettings.httpOptionsAcceptJson
                   ).pipe(map(res => res['b']));
        } else {
            return of(false);
        }

    }
    
    clePubliqueStripe(): Observable<string> {
        
        var urlAvecParam = this.appSettings.BASE_API_ENDPOINT+'/paiement/cle-publique-stripe';
        
        return this.http.get<any>(
                urlAvecParam,
                this.appSettings.httpOptionsAcceptJson
            ).pipe(map(res => res['b']));

    }

    cleConnectStripe(): Observable<string> {
        
        var urlAvecParam = this.appSettings.BASE_API_ENDPOINT+'/paiement/cle-connect-stripe';
        
        return this.http.get<any>(
                urlAvecParam,
                this.appSettings.httpOptionsAcceptJson
            ).pipe(map(res => res['b']));

    }
    
    etatPaiementCourant(codeDossierFournisseur:String, idPaymentIntent:String): Observable<number> {
        
        var urlAvecParam = this.appSettings.BASE_API_ENDPOINT+'/paiement/etat-paiement-courant'
            //+"?codeDossierFournisseur="+codeDossierFournisseur
            +"?idPaymentIntent="+idPaymentIntent;
        
        return this.http.get<any>(
                urlAvecParam,
                this.appSettings.httpOptionsAcceptJson
               ).pipe(map(res => res['b']));

    }

//    rechercherCleStripe(): string {
//      return 'pk_test_Dj5aRLvgEd6K8zpNMxKqlc82';
////        this.clePubliqueStripe().subscribe( data => {
////            return data;
////          });
////        return null;
//    }
    
    public payer(): void {
     
    }
}
