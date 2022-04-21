import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { first } from 'rxjs/operators';

import { EspaceClientService } from '../services/espace-client.service';
import { AuthenticationService } from '../services/authentication.service';
import { CompteEspaceClient, CompteEspaceClientAdapter } from '../model/compteEspaceClient';
import { AppSettings } from '../env';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-validation-creation-compte',
  templateUrl: './validation-creation-compte.component.html',
  styleUrls: ['./validation-creation-compte.component.css']
})
export class ValidationCreationCompteComponent implements OnInit {

  public compte_cree = false;
  public liaisonNouveauCompteCodeClientAuto = false;
  public codeTiers = null;
  public codeTiersSaisi = null;
  public compte_lie = false;
  public compte_non_cree = false;
  public returnUrl: String;
  public compteCourant:CompteEspaceClient;
  public messageErreur = null;
  public typeCompte: String;
  image: any = null;

  constructor(private route: ActivatedRoute, private router: Router,
    private authenticationService: AuthenticationService, private espaceClientService: EspaceClientService, 
    private sanitizer: DomSanitizer, private appSettings: AppSettings) {
    this.appSettings.initTenantFromUrl();

    this.espaceClientService.logoPageSimple().subscribe((blob: any) => {
      let objectURL = URL.createObjectURL(blob);
      this.image = this.sanitizer.bypassSecurityTrustUrl(objectURL);
    },
      error => {
        this.image = null;
      }
    );
  }

  ngOnInit() {
    this.appSettings.initTenantFromUrl();
    console.log(this.appSettings.BDG_TENANT_DOSSIER);
    this.returnUrl = /*this.route.snapshot.queryParams['returnUrl'] ||*/ '/login';

    if (this.appSettings.PARAMETRES == null) {
      this.espaceClientService.parametres().subscribe(
          data => {
              this.appSettings.PARAMETRES = data;
              this.liaisonNouveauCompteCodeClientAuto = this.appSettings.PARAMETRES.liaisonNouveauCompteCodeClientAuto;             
           }
          
      );
  }
    //récupération de la chaine crypté contenant les données du nouveau compte à créer

    const infoCompteCryptees: string = this.route.snapshot.queryParamMap.get('p');

    //envoi de cette chaine cryptée au serveur pour création du compte

    var compte: CompteEspaceClient = new CompteEspaceClient(
      this.appSettings.BDG_ID_ESPACE_CLIENT,this.appSettings.HOST_NAME, this.appSettings.HOST_PORT, "a finir", "a finir", "a finir", "a finir", "a finir", "a finir", true, infoCompteCryptees, "", "", ""
    );

    this.espaceClientService.creerCompteEspaceClient(compte)
      .pipe(first())
      .subscribe(
        data => {
          //  this.router.navigate([this.returnUrl]);
          this.compte_cree = true;
          this.codeTiers = data.codeTiers;
          this.compteCourant = data;
          if(!this.liaisonNouveauCompteCodeClientAuto && this.codeTiers==null) {
            //on n'a pas fait de liaision automatique avec un tiers et on ne vas pas demander à saisir un code client
            //donc on créer un nouveau tiers dans BDG
          }
        },
        error => {
          if (error.error.includes("même email avec ce meme id tiers existe deja")) {
            error = this.sanitizer.bypassSecurityTrustHtml('Un compte avec cette adresse email pour ce code client existe déjà.<br/>Si vous avez oublié votre mot de passe vous pouvez en demander un nouveau, sinon vous pouvez nous contacter.');
          } else if (error.error.includes("même email sans code tiers existe deja")) {
            error = this.sanitizer.bypassSecurityTrustHtml('Un compte avec cette adresse email existe déjà.<br/>Si vous avez oublié votre mot de passe vous pouvez en demander un nouveau, sinon vous pouvez nous contacter.');
          } else {
            error = error.error;
          }
          this.messageErreur = error;
          //                  this.loading = false;
          this.compte_non_cree = true;
        });

    //Erreur => message d'erreur
    //OK => Message de confirmation, redirection vers le login ou login automatique
  }

  public actAnnuler(event) {
    this.router.navigate([this.returnUrl]);
  }

  public actLiaisonCompteExistant(event) {
    //alert(this.codeTiersSaisi);
    this.messageErreur = null;
    this.compteCourant.codeTiers = this.codeTiersSaisi;
    this.espaceClientService.liaisonNouveauCompteEspaceClient(this.compteCourant)
    .subscribe(
      data => {
        //  this.router.navigate([this.returnUrl]);
        this.compte_cree = true;
        if(data.codeTiers) {
          this.codeTiers = data.codeTiers;
          this.compte_lie = true;

          this.compte_non_cree = false;
          this.messageErreur = null;
        }
        this.compteCourant = data;
      },
      error => {
        if (error.error.includes("même email avec ce meme id tiers existe deja")) {
          error = this.sanitizer.bypassSecurityTrustHtml('Un compte avec cette adresse email pour ce code client existe déjà.<br/>Si vous avez oublié votre mot de passe vous pouvez en demander un nouveau, sinon vous pouvez nous contacter.');
        } else if (error.error.includes("même email sans code tiers existe deja")) {
          error = this.sanitizer.bypassSecurityTrustHtml('Un compte avec cette adresse email existe déjà.<br/>Si vous avez oublié votre mot de passe vous pouvez en demander un nouveau, sinon vous pouvez nous contacter.');
        } else if (error.error.includes("Ce code tiers est déjà lié à une autre adresse email")) {
          error = this.sanitizer.bypassSecurityTrustHtml('Ce code client est déjà utilisé avec une autre adresse email.<br/>Si vous avez oublié votre mot de passe vous pouvez en demander un nouveau, sinon vous pouvez nous contacter.');
        } else if (error.error.includes("Ce code tiers introuvable")) {
          error = this.sanitizer.bypassSecurityTrustHtml("Ce code client est invalide.<br/>Si vous avez oublié votre mot de passe vous pouvez en demander un nouveau, sinon vous pouvez nous contacter.");
        } else {
          error = error.error;
        }



        this.messageErreur = error;
        //this.loading = false;
        this.compte_non_cree = true;
      });

  //Erreur => message d'erreur
  //OK => Message de confirmation, redirection vers le login ou login automatique
  }

  public actNouveauClient(event) {
    //alert("Nouveau");
    this.messageErreur = null;
    this.compteCourant.codeTiers = null;
    this.espaceClientService.liaisonNouveauCompteEspaceClient(this.compteCourant)
    .subscribe(
      data => {
        //  this.router.navigate([this.returnUrl]);
        this.compte_cree = true;
        if(data.codeTiers) {
          this.codeTiers = data.codeTiers;
          this.compte_lie = true;

          this.compte_non_cree = false;
          this.messageErreur = null;
        }
        this.compteCourant = data;
      },
      error => {
        if (error.error.includes("même email avec ce meme id tiers existe deja")) {
          error = this.sanitizer.bypassSecurityTrustHtml('Un compte avec cette adresse email pour ce code client existe déjà.<br/>Si vous avez oublié votre mot de passe vous pouvez en demander un nouveau, sinon vous pouvez nous contacter.');
        } else if (error.error.includes("même email sans code tiers existe deja")) {
          error = this.sanitizer.bypassSecurityTrustHtml('Un compte avec cette adresse email existe déjà.<br/>Si vous avez oublié votre mot de passe vous pouvez en demander un nouveau, sinon vous pouvez nous contacter.');
        } else if (error.error.includes("Ce code tiers est déjà lié à une autre adresse email")) {
          error = this.sanitizer.bypassSecurityTrustHtml('Ce code client est déjà utilisé avec une autre adresse email.<br/>Si vous avez oublié votre mot de passe vous pouvez en demander un nouveau, sinon vous pouvez nous contacter.');
        } else if (error.error.includes("Ce code tiers introuvable")) {
          error = this.sanitizer.bypassSecurityTrustHtml("Ce code client est invalide.<br/>Si vous avez oublié votre mot de passe vous pouvez en demander un nouveau, sinon vous pouvez nous contacter.");
        }



        this.messageErreur = error;
        //                  this.loading = false;
        this.compte_non_cree = true;
      });

  }

}
