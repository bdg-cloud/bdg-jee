import { Injectable } from '@angular/core';
import { map, switchMap} from 'rxjs/operators';
import { Panier, PanierAdapter } from '../model/panier';
import { LignePanier, LignePanierAdapter } from '../model/lignePanier';
import { Article } from '../model/article';
import { Observable, BehaviorSubject, of } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { AppSettings } from '../env';
import { ParamEmailConfirmationCommandeBoutique } from '../model/param-email-confirmation-commande-boutique';
import { CompteEspaceClient, CompteEspaceClientAdapter } from '../model/compteEspaceClient';
import { InfosPanierValideResult, InfosPanierValideResultAdapter } from '../model/infos-panier-valide-result';
import { ParamConfirmationCommandeBoutique } from '../model/param-confirmation-commande-boutique';

@Injectable({
  providedIn: 'root'
})
export class PanierService {

  private _url = "";

  constructor(private http: HttpClient, private datePipe: DatePipe, private panierAdapter: PanierAdapter, private infosPanierValideResulAdapter :InfosPanierValideResultAdapter,
    private appSettings: AppSettings) {
    this._url = this.appSettings.BASE_API_ENDPOINT + '/paniers';

    if(!this.panier && localStorage.getItem('panier')!=null) {
            this.panier = JSON.parse(localStorage.getItem('panier'));
            //localStorage.setItem('panier', JSON.stringify(this.panier));
    }
    //if(!this.panier) {
    //  this.panier = new Panier(null,"code","libl",new Date(),0,0,0,0,null,"");
    //}
  }

  //items = [];
  private panierSource = new BehaviorSubject<Panier>(new Panier(null,"code","libl",new Date(),0,0,0,0,null,""));

  public panier: Observable<Panier> = this.panierSource.asObservable();

  public changePanier(p: Panier) {
    this.panierSource.next(p);
  }

  public addToCart(product: Article) {
    //TODO vérifier l'ajout OU incrémentation de la quantité, voir avec BDG
    var qte:number = 1;
    var ligne = new LignePanier(null,product.id,product.codeArticle,product.libelleCatalogueCatWeb,qte,product.codeUnite,product.prixttcPrix,product.tauxTva,product.codeTva,
      0,product.prixttcPrix*qte,0,0);
  
/*
    this.panier.netTtcCalc = this.panier.netTtcCalc+ligne.mtTtcLDocument;
    
    this.panier.lignes.push(ligne);
*/     
    
    var tenant: string = this.appSettings.BDG_TENANT_DOSSIER;
    var codeClient: string = this.appSettings.CODE_CLIENT_CHEZ_FRS;
    this.ajouteLigne(tenant, codeClient,this.panierSource.getValue(), ligne).subscribe(data => {
      //var panier:Panier = data;
      //this.panier = data;
      this.panierSource.next(data);
      console.log(data);
    });
    
  }

  public addFdp(panier:Panier) {
    var tenant: string = this.appSettings.BDG_TENANT_DOSSIER;
    var codeClient: string = this.appSettings.CODE_CLIENT_CHEZ_FRS;
    this.ajouteFdpFixePanier(tenant, codeClient,this.panierSource.getValue()).subscribe(data => {
      this.panierSource.next(data);
      console.log(data);
    });
  }

  public removeFdp(panier:Panier) {
    var tenant: string = this.appSettings.BDG_TENANT_DOSSIER;
    var codeClient: string = this.appSettings.CODE_CLIENT_CHEZ_FRS;
    this.supprimeFdpFixePanier(tenant, codeClient,this.panierSource.getValue()).subscribe(data => {
      this.panierSource.next(data);
      console.log(data);
    });
  }

  public addLignePrixVariable(panier:Panier) {
    var tenant: string = this.appSettings.BDG_TENANT_DOSSIER;
    var codeClient: string = this.appSettings.CODE_CLIENT_CHEZ_FRS;
    this.ajouteLignePrixVariable(tenant, codeClient,this.panierSource.getValue()).subscribe(data => {
      this.panierSource.next(data);
      console.log(data);
    });
  }

  public removeLignePrixVariable(panier:Panier) {
    var tenant: string = this.appSettings.BDG_TENANT_DOSSIER;
    var codeClient: string = this.appSettings.CODE_CLIENT_CHEZ_FRS;
    this.supprimeLignePrixVariable(tenant, codeClient,this.panierSource.getValue()).subscribe(data => {
      this.panierSource.next(data);
      console.log(data);
    });
  }

  public addDateRetrait(panier:Panier, dateRetrait:Date) {
    var tenant: string = this.appSettings.BDG_TENANT_DOSSIER;
    var codeClient: string = this.appSettings.CODE_CLIENT_CHEZ_FRS;
    this.ajouteCommentaireDateRetrait(tenant, codeClient,this.panierSource.getValue(),dateRetrait).subscribe(data => {
      this.panierSource.next(data);
      console.log(data);
    });
  }

  public removeDateRetrait(panier:Panier) {
    var tenant: string = this.appSettings.BDG_TENANT_DOSSIER;
    var codeClient: string = this.appSettings.CODE_CLIENT_CHEZ_FRS;
    this.supprimeCommentaireDateRetrait(tenant, codeClient,this.panierSource.getValue()).subscribe(data => {
      this.panierSource.next(data);
      console.log(data);
    });
  }

  /*
     this.panierService.removeFdp(this.panier);
    this.panierService.removeDateRetrait(this.panier);
    this.panierService.removeLignePrixVariable(this.panier);
  */
  public removeTout(panier:Panier) : Observable<Panier> {
    var tenant: string = this.appSettings.BDG_TENANT_DOSSIER;
    var codeClient: string = this.appSettings.CODE_CLIENT_CHEZ_FRS;

    /*
    this.supprimeLignePrixVariable(tenant, codeClient,this.panierSource.getValue()).subscribe(data => {
      this.panierSource.next(data);
      console.log(data);

      this.supprimeFdpFixePanier(tenant, codeClient,this.panierSource.getValue()).subscribe(data => {
        this.panierSource.next(data);
        console.log(data);

        this.supprimeCommentaireDateRetrait(tenant, codeClient,this.panierSource.getValue()).subscribe(data => {
          this.panierSource.next(data);
          console.log(data);
        });
      });
    }); 
    */

    /*
    this.supprimeOptionsPanier(tenant, codeClient,this.panierSource.getValue()).subscribe(data => {
      this.panierSource.next(data);
      console.log(data);
    }); 
    */

    
    const currentCityTemperature$ = this.supprimeLignePrixVariable(tenant, codeClient,panier)
    .pipe(
      switchMap(p => {
        return this.supprimeFdpFixePanier(tenant, codeClient,p)
    }),
    switchMap(panier2 => {
      return this.supprimeCommentaireDateRetrait(tenant, codeClient,panier2)
    }),
    
    );

    return currentCityTemperature$;
    

    //return this.supprimeOptionsPanier(tenant, codeClient,this.panierSource.getValue()); 
  }

  public initResumePanier(panier:Panier,selectedTypeExpedition:string,dateRetrait:Date) : Observable<Panier> {

    var tenant: string = this.appSettings.BDG_TENANT_DOSSIER;
    var codeClient: string = this.appSettings.CODE_CLIENT_CHEZ_FRS;
    /*
    this.ajouteLignePrixVariable(tenant, codeClient,this.panierSource.getValue()).subscribe(data => {
      this.panierSource.next(data);
      console.log(data);

      if(selectedTypeExpedition==='2') {
        this.supprimeCommentaireDateRetrait(tenant, codeClient,this.panierSource.getValue()).subscribe(data => {
          this.panierSource.next(data);
          console.log(data);

          this.ajouteFdpFixePanier(tenant, codeClient,this.panierSource.getValue()).subscribe(data => {
            this.panierSource.next(data);
            console.log(data);
          });
        });

      } else {
        this.ajouteCommentaireDateRetrait(tenant, codeClient,this.panierSource.getValue(),dateRetrait).subscribe(data => {
          this.panierSource.next(data);
          console.log(data);

          this.supprimeFdpFixePanier(tenant, codeClient,this.panierSource.getValue()).subscribe(data => {
            this.panierSource.next(data);
            console.log(data);
          });
        });
      }
    });
   */

        /*
    this.resumerPanier(tenant, codeClient,this.panierSource.getValue()).subscribe(data => {
      this.panierSource.next(data);
      console.log(data);
    }); 
    */

    /*
    const currentCityTemperature$ = this.ajouteLignePrixVariable(tenant, codeClient,panier)
    .pipe(switchMap(p => {
      if(selectedTypeExpedition==='2') {
        return this.supprimeCommentaireDateRetrait(tenant, codeClient,p).pipe(switchMap(panier2 => {
          return this.ajouteFdpFixePanier(tenant, codeClient,panier2)})
        )
      } else {
        return this.ajouteCommentaireDateRetrait(tenant, codeClient,p,dateRetrait).pipe(switchMap(panier2 => {
          return this.supprimeFdpFixePanier(tenant, codeClient,panier2)})
        )
      }
    }));
*/

    const currentCityTemperature$ = this.ajouteLignePrixVariable(tenant, codeClient,panier)
    .pipe(
      switchMap(p => {
      if(selectedTypeExpedition=='2') {
         return this.supprimeCommentaireDateRetrait(tenant, codeClient,p)
      } else {
        return this.ajouteCommentaireDateRetrait(tenant, codeClient,p,dateRetrait)
      }
    }),
    switchMap(panier2 => {
      if(selectedTypeExpedition=='2') {
          return this.ajouteFdpFixePanier(tenant, codeClient,panier2)
      } else {
          return this.supprimeFdpFixePanier(tenant, codeClient,panier2)
      }
    }),
    
    );

    return currentCityTemperature$;
    

    //return this.resumerPanier(tenant, codeClient,this.panierSource.getValue(),selectedTypeExpedition,dateRetrait); 
  }

  public removeFromCart(ligneASupprimer: LignePanier) {
    /*
    const index: number = this.panier.lignes.indexOf(ligneASupprimer);
    if (index !== -1) {
        this.panier.lignes.splice(index, 1);
    }  
    */
    /********************************** */
    var tenant: string = this.appSettings.BDG_TENANT_DOSSIER;
    var codeClient: string = this.appSettings.CODE_CLIENT_CHEZ_FRS;
    this.supprimeLigne(tenant, codeClient,this.panierSource.getValue(), ligneASupprimer).subscribe(data => {
      //var panier:Panier = data;
      //this.panier = data;
      this.panierSource.next(data);
      console.log(data);
    });
  }

  public updateCartLine(ligneAMettreAJour: LignePanier) {
    //ligneAMettreAJour.mtTtcLDocument = ligneAMettreAJour.qteLDocument*ligneAMettreAJour.prixULDocument;
    /********************************** */
    var tenant: string = this.appSettings.BDG_TENANT_DOSSIER;
    var codeClient: string = this.appSettings.CODE_CLIENT_CHEZ_FRS;
    this.modifieLigne(tenant, codeClient,this.panierSource.getValue(), ligneAMettreAJour).subscribe(data => {
      //var panier:Panier = data;
      //this.panier = data;
      this.panierSource.next(data);
      console.log(data);
    });
  }

  public getItems() {
    return this.panierSource.getValue().lignes;
  }

  public getPanier() {
    return this.panierSource.getValue();
  }

  public clearCart() {
    /*
    this.panier.lignes = [];
    return this.panier.lignes;
    */
  }

  cherchePanierActif(codeDossierFournisseur: String, codeTiers: String
                        /*, debut: Date, fin: Date*/): Observable<Panier> {

      /*
      var dateDebutStr = this.datePipe.transform(debut, this.appSettings.DATE_FORMAT);
      var dateFinStr = this.datePipe.transform(fin, this.appSettings.DATE_FORMAT);
      console.log(dateDebutStr);
      console.log(dateFinStr);
      */
  
      var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/paniers/actif'
        // +"?codeDossierFournisseur="+codeDossierFournisseur
        + "?codeTiers=" + codeTiers
       // + "&debut=" + dateDebutStr + "&fin=" + dateFinStr;
  
      return this.http.get(
        urlAvecParam,
        this.appSettings.httpOptionsAcceptJson
      ).pipe(
        // Adapt each item in the raw data array
        //map((data: any) => data.map(item => this.panierAdapter.adapt(item))),
        map((data: any) => this.panierAdapter.adapt(data)),
      );
  
  }

  ajouteLigne(codeDossierFournisseur: String, codeTiers: String, 
      panier:Panier, nouvelleLigne:LignePanier): Observable<Panier> {

      var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/paniers/'+panier.id+'/lignes'
      //+ "?codeTiers=" + codeTiers

      return this.http.post(
      urlAvecParam,nouvelleLigne,
      this.appSettings.httpOptionsAcceptJson
      ).pipe(
      // Adapt each item in the raw data array
      map((data: any) => this.panierAdapter.adapt(data)),
      );

  }

  supprimeLigne(codeDossierFournisseur: String, codeTiers: String, 
      panier:Panier, nouvelleLigne:LignePanier): Observable<Panier> {

      var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/paniers/'+panier.id+'/lignes/'+nouvelleLigne.numLigneLDocument
      + "?codeTiers=" + codeTiers

      return this.http.delete(
      urlAvecParam,
      this.appSettings.httpOptionsAcceptJson
      ).pipe(
      // Adapt each item in the raw data array
      map((data: any) => this.panierAdapter.adapt(data)),
      );

  }

  modifieLigne(codeDossierFournisseur: String, codeTiers: String, 
      panier:Panier, nouvelleLigne:LignePanier): Observable<Panier> {

      var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/paniers/'+panier.id+'/lignes'
      + "?codeTiers=" + codeTiers

      return this.http.put(
      urlAvecParam,nouvelleLigne,
      this.appSettings.httpOptionsAcceptJson
      ).pipe(
      // Adapt each item in the raw data array
      map((data: any) => this.panierAdapter.adapt(data)),
      );

  }

  modifiePanier(codeDossierFournisseur: String, codeTiers: String, 
    panier:Panier): Observable<Panier> {

    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/paniers/'+panier.id
    + "?codeTiers=" + codeTiers

    return this.http.put(
    urlAvecParam,panier,
    this.appSettings.httpOptionsAcceptJson
    ).pipe(
    // Adapt each item in the raw data array
    map((data: any) => this.panierAdapter.adapt(data)),
    );
  }

  ajouteFdpFixePanier(codeDossierFournisseur: String, codeTiers: String, 
    panier:Panier): Observable<Panier> {

    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/paniers/'+panier.id+'/ajouter-fdp-fixe'
    + "?codeTiers=" + codeTiers

    return this.http.post(
    urlAvecParam,panier,
    this.appSettings.httpOptionsAcceptJson
    ).pipe(
    // Adapt each item in the raw data array
    map((data: any) => this.panierAdapter.adapt(data)),
    );
  }


  supprimeFdpFixePanier(codeDossierFournisseur: String, codeTiers: String, 
    panier:Panier): Observable<Panier> {

    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/paniers/'+panier.id+'/supprimer-fdp-fixe'
    + "?codeTiers=" + codeTiers

    return this.http.post(
    urlAvecParam,panier,
    this.appSettings.httpOptionsAcceptJson
    ).pipe(
    // Adapt each item in the raw data array
    map((data: any) => this.panierAdapter.adapt(data)),
    );
  }

  ajouteLignePrixVariable(codeDossierFournisseur: String, codeTiers: String, 
    panier:Panier): Observable<Panier> {

    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/paniers/'+panier.id+'/ajouter-ligne-prix-variable'
    + "?codeTiers=" + codeTiers

    return this.http.post(
    urlAvecParam,panier,
    this.appSettings.httpOptionsAcceptJson
    ).pipe(
    // Adapt each item in the raw data array
    map((data: any) => this.panierAdapter.adapt(data)),
    );
  }


  supprimeLignePrixVariable(codeDossierFournisseur: String, codeTiers: String, 
    panier:Panier): Observable<Panier> {

    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/paniers/'+panier.id+'/supprimer-ligne-prix-variable'
    + "?codeTiers=" + codeTiers

    return this.http.post(
    urlAvecParam,panier,
    this.appSettings.httpOptionsAcceptJson
    ).pipe(
    // Adapt each item in the raw data array
    map((data: any) => this.panierAdapter.adapt(data)),
    );
  }

  supprimeOptionsPanier(codeDossierFournisseur: String, codeTiers: String, 
    panier:Panier): Observable<Panier> {

    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/paniers/'+panier.id+'/supprimer-options-panier'
    + "?codeTiers=" + codeTiers

    return this.http.post(
    urlAvecParam,panier,
    this.appSettings.httpOptionsAcceptJson
    ).pipe(
    // Adapt each item in the raw data array
    map((data: any) => this.panierAdapter.adapt(data)),
    );
  }

  resumerPanier(codeDossierFournisseur: String, codeTiers: String, 
    panier:Panier,selectedTypeExpedition:string,dateRetrait:Date): Observable<Panier> {

    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/paniers/'+panier.id+'/resumer-panier/'+selectedTypeExpedition
    + "?codeTiers=" + codeTiers

    return this.http.post(
    urlAvecParam,dateRetrait!=null?dateRetrait.getTime():'nc',
    this.appSettings.httpOptionsAcceptJson
    ).pipe(
    // Adapt each item in the raw data array
    map((data: any) => this.panierAdapter.adapt(data)),
    );
  }

  public emailConfirmationCommandeBoutique(codePanier:string, idPanier): Observable<any> {
    var question:ParamEmailConfirmationCommandeBoutique = new ParamEmailConfirmationCommandeBoutique(null,null,null,null,null,null,null,null);
    let currentUser = JSON.parse(localStorage.getItem('currentUser'));
    var a: CompteEspaceClientAdapter = new CompteEspaceClientAdapter();
    var cec: CompteEspaceClient = a.adapt(currentUser);

    question.id = cec.id;
    question.idcompteEspaceClient = cec.id;
    question.email = cec.email;
    question.codeTiers = cec.codeTiers;
    question.codePanier = codePanier;
    //question.codeCommande = q;


    var aaaa = this.appSettings.BASE_API_ENDPOINT + '/paniers/'+idPanier+'/email-confirmation-commande';
    return this.http.post<any>(aaaa, question,
      this.appSettings.httpOptionsCTJson
    ).pipe(map(res => res));
  }


  infosPanierValide( codeTiers: String, idPanier): Observable<InfosPanierValideResult> {
    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/paniers/'+idPanier+'/infos-panier-valide'
    + "?codeTiers=" + codeTiers

    return this.http.get(
      urlAvecParam,
      this.appSettings.httpOptionsAcceptJson
      ).pipe(
      // Adapt each item in the raw data array
      //map((data: any) => data.map(item => this.panierAdapter.adapt(item))),
      map((data: any) => this.infosPanierValideResulAdapter.adapt(data)),
    );

  }
/*
  public validerCommandePourReglementUlterieur(codePanier:string, idPanier): Observable<any> {
    var aaaa = this.appSettings.BASE_API_ENDPOINT + '/paniers/'+idPanier+'/valider-commande-reglement-ulterieur';
    return this.http.post<any>(aaaa,
      this.appSettings.httpOptionsCTJson
    ).pipe(map(res => res));
  }
*/
  public validerCommandePourReglementUlterieur(codePanier:string, idPanier, idTypePaiementPrevu?:number, idTypeLivraisonPrevu?:number): Observable<any> {

    var paramConfirmation:ParamConfirmationCommandeBoutique = new ParamConfirmationCommandeBoutique(null,null,null,null,null,null,null,null,null);
    let currentUser = JSON.parse(localStorage.getItem('currentUser'));
    var a: CompteEspaceClientAdapter = new CompteEspaceClientAdapter();
    var cec: CompteEspaceClient = a.adapt(currentUser);

    paramConfirmation.id = cec.id;
    paramConfirmation.idcompteEspaceClient = cec.id;
    paramConfirmation.idTypePaiementPrevu = idTypePaiementPrevu;
    paramConfirmation.idTypeLivraisonPrevu = idTypeLivraisonPrevu;
    paramConfirmation.codeTiers = cec.codeTiers;
    paramConfirmation.codePanier = codePanier;

    var aaaa = this.appSettings.BASE_API_ENDPOINT + '/paniers/'+idPanier+'/valider-commande-reglement-ulterieur';
    return this.http.post<any>(aaaa,paramConfirmation,
      this.appSettings.httpOptionsCTJson
    ).pipe(map(res => res));
  }

  ajouteCommentaireDateRetrait(codeDossierFournisseur: String, codeTiers: String, 
    panier:Panier, dateRetrait:Date): Observable<Panier> {

      var t = null;
      if(dateRetrait!=null) {     
        var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/paniers/'+panier.id+'/ajouter-commentaire-date-retrait'
        + "?codeTiers=" + codeTiers

        return this.http.post(
        urlAvecParam,dateRetrait.getTime(),
        this.appSettings.httpOptionsAcceptJson
        ).pipe(
        // Adapt each item in the raw data array
        map((data: any) => this.panierAdapter.adapt(data)),
        );
      } else {
        return of(panier);
      }
      
  }


  supprimeCommentaireDateRetrait(codeDossierFournisseur: String, codeClientChezCeFournisseur: String, 
    panier:Panier): Observable<Panier> {

    var urlAvecParam = this.appSettings.BASE_API_ENDPOINT + '/paniers/'+panier.id+'/supprimer-commentaire-date-retrait'
    + "?codeClientChezCeFournisseur=" + codeClientChezCeFournisseur

    return this.http.post(
    urlAvecParam,null,
    this.appSettings.httpOptionsAcceptJson
    ).pipe(
    // Adapt each item in the raw data array
    map((data: any) => this.panierAdapter.adapt(data)),
    );
  }

}
