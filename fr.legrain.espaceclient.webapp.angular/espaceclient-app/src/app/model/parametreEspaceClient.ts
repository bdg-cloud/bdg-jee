import { Injectable } from '@angular/core';
import { Adapter } from './adapter';

export class ParametreEspaceClient {
    constructor(
            public afficheDevis : boolean,
            public afficheFactures : boolean,
            public afficheCommandes : boolean,
            public afficheLivraisons : boolean,
            public afficheAvisEcheance : boolean,
            public afficheBoutique : boolean,
            public afficheCatalogue : boolean,
            public activePanier : boolean,
            public paiementCb : boolean,
            public espaceClientActif : boolean,
           
            public affichePublicite: boolean,
            public activeEmailRenseignementProduit,
            public activeCommandeEmailSimple,
           /* public byte[] logoLogin;
            public byte[] logoPagesSimples;
            public byte[] logoHeader;
            public byte[] logoFooter;
            public byte[] imageBackgroundLogin*/
            public nomImageLogoLogin:String,
            public nomImageLogoPagesSimples:String,
            public nomImageLogoHeader:String,
            public nomImageLogoFooter:String,
            public nomImageBackgroundLogin:String,
            public urlPerso:String,
            public texteLogin1:String,
            public texteLogin2:String,
            public texteAccueil:String,
            public texteAccueilCatalogue:String,
            public themeDefaut:String,
            
            public raisonSociale: string,
            public contactEmail: string,
            public contactWeb: string,

            public cgvCatWeb:String,
            public fraisPortFixe:number,
            public fraisPortLimiteOffert:number,
            public afficherPrixCatalogue: boolean,

            public idArticleFdp:number,
            public codeArticleFdp:String,
            public libelleArticleFdp:String,
            public prixPrixArticleFdp:number,
            public prixttcPrixArticleFdp:number,
            public codeTvaArticleFdp:String,
            public tauxTvaArticleFdp:number,

            public activeLivraison: boolean,
            public activePaiementPanierCB: boolean,
            public activePaiementPanierSurPlace: boolean,
            public generationDocAuPaiementPanier:String,
            public activeEmailConfirmationCmd: boolean,

            public texteConfirmationPaiementBoutique:String,

            public contactTel:String,
            public adresse1:String,
            public adresse2:String,
            public adresse3:String,
            public codePostal:String,
            public ville:String,
            public pays:String,

            public activePointRetrait: boolean,

            public idAdressePointRetrait:number,
            public adresse1PointRetrait:String,
            public adresse2PointRetrait:String,
            public adresse3PointRetrait:String,
            public codepostalPointRetrait:String,
            public villePointRetrait:String,
            public paysPointRetrait:String,
            public latitudeDecPointRetrait:String,
            public longitudeDecPointRetrait:String,
            
            public horairesOuverturePointRetrait:String,
            
            public activePaiementPanierCheque: boolean,
            public activePaiementPanierVirement : boolean,
            public prixCatalogueHt: boolean,
            public activePlanningRetrait: boolean,

            public idCompteBanquePaiementVirement:number,
            public ibanPaiementVirement:String,
            public swiftPaiementVirement:String,

            public utilisateurPeuCreerCompte: boolean,
            public liaisonNouveauCompteEmailAuto: boolean,
            public liaisonNouveauCompteCodeClientAuto: boolean,
            public autoriseMajDonneeParClient: boolean,
            
    ){}
    
}

@Injectable({
    providedIn: 'root'
})
export class ParametreEspaceClientAdapter implements Adapter<ParametreEspaceClient> {

  adapt(item: any): ParametreEspaceClient {
      console.log(item);
    return new ParametreEspaceClient(
      item.afficheDevis,
      item.afficheFactures,
      item.afficheCommandes,
      item.afficheLivraisons,
      item.afficheAvisEcheance,
      item.afficheBoutique,
      item.afficheCatalogue,
      item.activePanier,
      item.paiementCb,
      item.espaceClientActif,
      /*
      item.logoLogin,
      item.logoPagesSimple,
      item.logoFooter,
      */
      
      item.affichePublicite,
      item.activeEmailRenseignementProduit,
      item.activeCommandeEmailSimple,
      item.nomImageLogoLogin,
      item.nomImageLogoPagesSimples,
      item.nomImageLogoHeader,
      item.nomImageLogoFooter,
      item.nomImageBackgroundLogin,
      item.urlPerso,
      item.texteLogin1,
      item.texteLogin2,
      item.texteAccueil,
      item.texteAccueilCatalogue,
      item.themeDefaut,
      
      item.raisonSociale,
      item.contactEmail,
      item.contactWeb,

      item.cgvCatWeb,
      item.fraisPortFixe,
      item.fraisPortLimiteOffert,
      item.afficherPrixCatalogue,

      item.idArticleFdp,
      item.codeArticleFdp,
      item.libelleArticleFdp,
      item.prixPrixArticleFdp,
      item.prixttcPrixArticleFdp,
      item.codeTvaArticleFdp,
      item.tauxTvaArticleFdp,

      item.activeLivraison,
      item.activePaiementPanierCB,
      item.activePaiementPanierSurPlace,
      item.generationDocAuPaiementPanie,
      item.activeEmailConfirmationCmd,
      item.texteConfirmationPaiementBoutique,


      item.contactTel,
      item.adresse1,
      item.adresse2,
      item.adresse3,
      item.codePostal,
      item.ville,
      item.pays,

      item.activePointRetrait,

      item.idAdressePointRetrait,
      item.adresse1PointRetrait,
      item.adresse2PointRetrait,
      item.adresse3PointRetrait,
      item.codepostalPointRetrait,
      item.villePointRetrait,
      item.paysPointRetrait,
      item.latitudeDecPointRetrait,
      item.longitudeDecPointRetrait,
      
      item.horairesOuverturePointRetrait,
      
      item.activePaiementPanierCheque,
      item.activePaiementPanierVirement ,
      item.prixCatalogueHt,
      item.activePlanningRetrait,

      item.idCompteBanquePaiementVirement,
      item.ibanPaiementVirement,
      item.swiftPaiementVirement,

      item.utilisateurPeuCreerCompte,
      item.liaisonNouveauCompteEmailAuto,
      item.liaisonNouveauCompteCodeClientAuto,
      item.autoriseMajDonneeParClient,
      
    );
  }
}
