import { Component, OnInit } from '@angular/core';
import { PanierService } from '../services/panier.service';
import { MenuItem } from 'primeng/api';
import { Router } from '@angular/router';

import { MessageService } from 'primeng/api';
import { DialogService } from 'primeng/dynamicdialog';

import { Panier } from '../model/panier';
import { LignePanier } from '../model/lignePanier';
import { Tiers, TiersAdapter } from '../model/tiers';
import { EspaceClientService } from '../services/espace-client.service';

import { PaiementCBComponent } from '../paiement-cb/paiement-cb.component';
import { PaiementStripeService } from '../services/paiement-stripe.service';
import { CGVComponent } from '../cgv/cgv.component';

import { AppSettings } from '../env';
import { InfosPanier } from '../model/infosPanier';

import { FormGroup, FormControl, AbstractControl, ValidationErrors } from '@angular/forms';
import { Validators, ValidatorFn } from '@angular/forms';
import { Adresse } from '../model/adresse';

import { PrimeNGConfig } from 'primeng/api';

@Component({
  selector: 'app-validation-panier',
  templateUrl: './validation-panier.component.html',
  styleUrls: ['./validation-panier.component.scss'],
  providers: [MessageService, DialogService]
})
export class ValidationPanierComponent implements OnInit {

  menuItems: MenuItem[];
  activeIndex: number = 0;

  //items: LignePanier[];
  panier: Panier;
  public tiers: Tiers;

  selectedTypeExpedition: string = "1";
  selectedTypePaiement: string = "1";

  fraisDePortFixe : number;
  limiteFraisDePortOffert : number;

  cgv: boolean = false;
  activeLivraison: boolean = false;
  activePointRetrait: boolean = false;

  activePaiementPanierCB: boolean = false;
  activePaiementPanierCheque: boolean = false;
  activePaiementPanierVirement: boolean = false;
  activePlanningRetrait: boolean = false;
  activePaiementPanierSurPlace: boolean = false;

  nouveauClient: boolean = false;

  loadingResume: boolean = true;

  public paiementPossible = false; //le fournisseur a un compte stripe

  public adresse1PointRetrait:String;
  public adresse2PointRetrait:String;
  public adresse3PointRetrait:String;
  public codePostalPointRetrait:String;
  public villePointRetrait:String;
  public paysPointRetrait:String;
  public latitudeDecPointRetrait:String;
  public longitudeDecPointRetrait:String;

  public horairesOuverturePointRetrait:String;
  
  public ibanPaiementVirement:String;
  public swiftPaiementVirement:String;

  public infosClientForm:FormGroup;
  public adresseFactForm:FormGroup;
  public adresseLivForm:FormGroup;

  private dateRetraitDefaut:Date;
  public dateRetrait:Date;
  public minDateRetrait:Date;
  public maxDateRetrait:Date;

  fr: any;  

  constructor( private router: Router,
    private panierService: PanierService,
    private espaceClientService: EspaceClientService,
    private appSettings: AppSettings,
    private paiementStripeService: PaiementStripeService,
    private dialogService: DialogService,
    private config: PrimeNGConfig,) { }

  ngOnInit(): void {

    this.config.setTranslation({
     // firstDayOfWeek: 1,
      dayNames: [ "dimanche","lundi","mardi","mercredi","jeudi","vendredi","samedi" ],
      dayNamesShort: [ "dim","lun","mar","mer","jeu","ven","sam" ],
      dayNamesMin: [ "D","L","M","M","J","V","S" ],
      monthNames: [ "Janvier","Février","Mars","Avril","Mai","Juin","Juillet","Aout","Septembre","Octobre","Novembre","Decembre" ],
      monthNamesShort: [ "Jan","Fév","Mar","Avr","Mai","Juin","Juil","aou","sep","oct","nov","déc" ],
      today: 'Maintenant',
      clear: 'Vider'
    });

    /*
    this.fr = {
      firstDayOfWeek: 1,
      dayNames: [ "dimanche","lundi","mardi","mercredi","jeudi","vendredi","samedi" ],
      dayNamesShort: [ "dim","lun","mar","mer","jeu","ven","sam" ],
      dayNamesMin: [ "D","L","M","M","J","V","S" ],
      monthNames: [ "Janvier","Février","Mars","Avril","Mai","Juin","Juillet","Aout","Septembre","Octobre","Novembre","Decembre" ],
      monthNamesShort: [ "Jan","Fév","Mar","Avr","Mai","Juin","Juil","aou","sep","oct","nov","déc" ],
      today: 'Maintenant',
      clear: 'Vider'
    }
    */

    //Retrait maximum dans 2 semaines / 14 jours
    this.minDateRetrait = new Date();
    this.maxDateRetrait = new Date();
    this.maxDateRetrait.setDate(this.minDateRetrait.getDate() + 14);

    //Date de retrait par defaut, le lendemain à 9:00
    this.dateRetraitDefaut = new Date();
    this.dateRetraitDefaut.setDate(this.dateRetraitDefaut.getDate() + 1); //le lendemain
    this.dateRetraitDefaut.setHours(9,0,0,0);

    this.fournisseurPermetPaiementParCB();

    this.menuItems = [
    {
        label: 'Informations personnelles',
        command: (event: any) => {
          this.activeIndex = 0;
        }
    },
    {
        label: 'Livraison',
        command: (event: any) => {
          this.activeIndex = 1;
        }
    },
    {
      label: 'Confirmation',
      command: (event: any) => {
        this.activeIndex = 2;
      }
    },
    {
      label: 'Paiement',
      command: (event: any) => {
        this.activeIndex = 3;
      }
    }
    ];

    this.espaceClientService.infosTiers().subscribe(data => {
      this.tiers = data;
      if(this.tiers.codeTTiers!=null && this.tiers.codeTTiers=="VISIT") {
        this.nouveauClient = true;
      }
      if(this.panier.infos==null) {
        this.panier.infos = new InfosPanier(0,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null);
      }
      if(this.panier.infos.adresse1==null) {
        this.panier.infos.adresse1 = this.tiers.adresse1Adresse;
        this.panier.infos.adresse2 = this.tiers.adresse2Adresse;
        this.panier.infos.adresse3 = this.tiers.adresse3Adresse;
        this.panier.infos.codepostal = this.tiers.codepostalAdresse;
        this.panier.infos.ville = this.tiers.villeAdresse;
        this.panier.infos.pays = this.tiers.paysAdresse;
      }

      if(this.panier.infos.adresse1Liv==null) {
        this.panier.infos.adresse1Liv = this.tiers.adresse1Adresse;
        this.panier.infos.adresse2Liv = this.tiers.adresse2Adresse;
        this.panier.infos.adresse3Liv = this.tiers.adresse3Adresse;
        this.panier.infos.codepostalLiv = this.tiers.codepostalAdresse;
        this.panier.infos.villeLiv = this.tiers.villeAdresse;
        this.panier.infos.paysLiv = this.tiers.paysAdresse;
      }

      if(this.nouveauClient) {

        const validationTiers: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
          const nomEntreprise = control.get('nomEntreprise');
          const siretCompl = control.get('siretCompl');
          const nomTiers = control.get('nomTiers');
        
          var erreur:any = {}
          erreur.errSiretPourEntrepriseVide = false;
          erreur.errNomTiersEtRaisonSocialeVide = false;
          /*
          //Pas d'obligation de saisie du SIRET ou du numéro de TVA intra pour l'instant
          if( (nomEntreprise.value && !siretCompl.value) || (!nomEntreprise.value && siretCompl.value)) {
            erreur.errSiretPourEntrepriseVide = true;
          } 
          */
          if( !nomTiers.value && !nomEntreprise.value) {
            erreur.errNomTiersEtRaisonSocialeVide = true;
          }
          if(erreur.errSiretPourEntrepriseVide == true
            || erreur.errNomTiersEtRaisonSocialeVide == true) {
              return erreur;
          } else {
            return null;
          }
        };

        this.infosClientForm = new FormGroup({
          tvaIComCompl: new FormControl(this.tiers.tvaIComCompl),
          siretCompl: new FormControl(this.tiers.siretCompl),
          numeroTelephone: new FormControl(this.tiers.numeroTelephone,[Validators.required]),
          adresseEmail: new FormControl(this.tiers.adresseEmail,[Validators.required]),
          adresseWeb: new FormControl(this.tiers.adresseWeb),
          nomTiers: new FormControl(this.tiers.nomTiers),
          prenomTiers: new FormControl(this.tiers.prenomTiers),
          nomEntreprise: new FormControl(this.tiers.nomEntreprise),
        },validationTiers); 
      }
    });

    //this.items = this.panierService.getItems();
    //this.panier = this.panierService.getPanier();
    this.panierService.panier.subscribe(panier => {
        this.panier = panier;
      });

    //this.fraisDePortFixe = this.appSettings.PARAMETRES.fraisPortFixe;
    this.fraisDePortFixe = this.appSettings.PARAMETRES.prixttcPrixArticleFdp;
    this.limiteFraisDePortOffert = this.appSettings.PARAMETRES.fraisPortLimiteOffert;
    this.activeLivraison = this.appSettings.PARAMETRES.activeLivraison;
    this.activePointRetrait = this.appSettings.PARAMETRES.activePointRetrait;
    this.activePlanningRetrait = this.appSettings.PARAMETRES.activePlanningRetrait;

    this.activePaiementPanierCB = this.appSettings.PARAMETRES.activePaiementPanierCB;
    this.activePaiementPanierCheque = this.appSettings.PARAMETRES.activePaiementPanierCheque;
    this.activePaiementPanierVirement = this.appSettings.PARAMETRES.activePaiementPanierVirement;
    this.activePaiementPanierSurPlace = this.appSettings.PARAMETRES.activePaiementPanierSurPlace;
    this.initMoyenPaiementDefaut();

    this.adresse1PointRetrait = this.appSettings.PARAMETRES.adresse1PointRetrait;
    this.adresse2PointRetrait = this.appSettings.PARAMETRES.adresse2PointRetrait;
    this.adresse3PointRetrait = this.appSettings.PARAMETRES.adresse3PointRetrait;
    this.codePostalPointRetrait = this.appSettings.PARAMETRES.codepostalPointRetrait;
    this.villePointRetrait = this.appSettings.PARAMETRES.villePointRetrait;
    this.paysPointRetrait = this.appSettings.PARAMETRES.paysPointRetrait;
    this.initTypeExpeditionDefaut();

    this.latitudeDecPointRetrait = this.appSettings.PARAMETRES.latitudeDecPointRetrait;
    this.longitudeDecPointRetrait = this.appSettings.PARAMETRES.longitudeDecPointRetrait;

    this.horairesOuverturePointRetrait = this.appSettings.PARAMETRES.horairesOuverturePointRetrait;

    this.ibanPaiementVirement = this.appSettings.PARAMETRES.ibanPaiementVirement;
    this.swiftPaiementVirement = this.appSettings.PARAMETRES.swiftPaiementVirement;
    
    this.adresseFactForm = new FormGroup({
      adresse1: new FormControl(this.panier.infos.adresse1,[Validators.required]),
      adresse2: new FormControl(this.panier.infos.adresse2),
      adresse3: new FormControl(this.panier.infos.adresse3),
      codepostal: new FormControl(this.panier.infos.codepostal,[Validators.required]),
      ville: new FormControl(this.panier.infos.ville,[Validators.required]),
      pays: new FormControl(this.panier.infos.pays),
    }); 

    this.adresseLivForm = new FormGroup({
      adresse1Liv: new FormControl(this.panier.infos.adresse1Liv,[Validators.required]),
      adresse2Liv: new FormControl(this.panier.infos.adresse2Liv),
      adresse3Liv: new FormControl(this.panier.infos.adresse3Liv),
      codepostalLiv: new FormControl(this.panier.infos.codepostalLiv,[Validators.required]),
      villeLiv: new FormControl(this.panier.infos.villeLiv,[Validators.required]),
      paysLiv: new FormControl(this.panier.infos.paysLiv),
    }); 

  }

  fournisseurPermetPaiementParCB() {
    this.paiementStripeService.fournisseurPermetPaiementParCB(this.appSettings.BDG_TENANT_DOSSIER, this.appSettings.CODE_CLIENT_CHEZ_FRS)
        .subscribe(data => {
            //alert(data);
            this.paiementPossible = data;
            this.initMoyenPaiementDefaut();
        });
  } 

  initDateRetrait() {
    if(this.dateRetrait==null) {
      this.dateRetrait = this.dateRetraitDefaut;
    }
  }

  initMoyenPaiementDefaut() {
    this.selectedTypePaiement = "1";
    if(!this.activePaiementPanierCB || !this.paiementPossible) {
      //pas de paiement en ligne possible
      this.activePaiementPanierSurPlace=true;
      this.selectedTypePaiement = "2";
    }
  }

  initTypeExpeditionDefaut() {
    this.selectedTypeExpedition = "1";
    if(!this.activePointRetrait ) {
      //pas de retrait en magasin
      this.selectedTypeExpedition = "2";
    }
  }

  retourPanier() {
    this.router.navigate(['/panier']);
  }

  nextPage(num) {
    if(this.activeIndex===0) {//on est sur la page des infos perso, on met a jour les adresses et si c'est un nouveau client on met a jour le tiers
      //this.supprimeLignePrixVar();
      this.panierService.removeTout(this.panier);
      if(this.nouveauClient) {
        var codeTAdrFact:string = "FACT";
        var codeTAdrLiv:string = "LIV";
        var codeClient: string = this.appSettings.CODE_CLIENT_CHEZ_FRS;

        var adrFact:Adresse = new Adresse(0,this.panier.infos.adresse1,this.panier.infos.adresse2,this.panier.infos.adresse3,
          this.panier.infos.codepostal,this.panier.infos.ville,this.panier.infos.pays,null,null,codeTAdrFact,null,null,null,null,null);
        
        var adrLiv:Adresse = new Adresse(0,this.panier.infos.adresse1Liv,this.panier.infos.adresse2Liv,this.panier.infos.adresse3Liv,
          this.panier.infos.codepostalLiv,this.panier.infos.villeLiv,this.panier.infos.paysLiv,null,null,codeTAdrLiv,null,null,null,null,null);
       
        
        this.espaceClientService.ajouteAdresse(codeClient,this.tiers,adrFact).subscribe(data => {
          //this.tiers = data;
          console.log("1");

          this.espaceClientService.ajouteAdresse(codeClient,this.tiers,adrLiv).subscribe(data => {
            //this.tiers = data;
            console.log("2");

          });

        });

        this.espaceClientService.finaliseNouveauTiers(codeClient,this.tiers).subscribe(data => {
          //this.tiers = data;
          console.log("3");
        });

        
        

        
      }
      this.panierService.modifiePanier(this.appSettings.BDG_TENANT_DOSSIER, this.appSettings.CODE_CLIENT_CHEZ_FRS,this.panier).subscribe(data => {
        console.log(data);
      });
    } else if(this.activeIndex===1) {//on est sur la page des FDP
      /*
      this.ajouteLignePrixVar();
      if(this.selectedTypeExpedition==='2') {
        this.supprimeDateRetrait();
        this.ajouteFdp();
      } else {
        this.ajouteDateRetrait();
        this.supprimeFdp();
      }*/
     
      this.panierService.initResumePanier(this.panier,this.selectedTypeExpedition,this.dateRetrait).subscribe(data => {
        this.panierService.changePanier(data);
        console.log(data);
        this.loadingResume = false;
      });
    } else if(this.activeIndex===2) {//on est sur la page de résumé
      
    } 
    this.activeIndex = this.activeIndex+1;
  }

  reinitialiserAdresses() {
    //if(this.panier.infos.adresse1==null) {
      this.panier.infos.adresse1 = this.tiers.adresse1Adresse;
      this.panier.infos.adresse2 = this.tiers.adresse2Adresse;
      this.panier.infos.adresse3 = this.tiers.adresse3Adresse;
      this.panier.infos.codepostal = this.tiers.codepostalAdresse;
      this.panier.infos.ville = this.tiers.villeAdresse;
      this.panier.infos.pays = this.tiers.paysAdresse;
   // }

  //  if(this.panier.infos.adresse1Liv==null) {
      this.panier.infos.adresse1Liv = this.tiers.adresse1Adresse;
      this.panier.infos.adresse2Liv = this.tiers.adresse2Adresse;
      this.panier.infos.adresse3Liv = this.tiers.adresse3Adresse;
      this.panier.infos.codepostalLiv = this.tiers.codepostalAdresse;
      this.panier.infos.villeLiv = this.tiers.villeAdresse;
      this.panier.infos.paysLiv = this.tiers.paysAdresse;
  //  }
  }

  prevPage() {
    this.activeIndex = this.activeIndex-1;
  }

  ajouteFdp() {
    this.panierService.addFdp(this.panier);
  }

  supprimeFdp() {
    this.panierService.removeFdp(this.panier);
  }

  ajouteLignePrixVar() {
    this.panierService.addLignePrixVariable(this.panier);
  }

  supprimeLignePrixVar() {
    this.panierService.removeLignePrixVariable(this.panier);
  }

  ajouteDateRetrait() {
    if(this.dateRetrait!=null) {
      this.panierService.addDateRetrait(this.panier,this.dateRetrait);
    }
  }

  supprimeDateRetrait() {
    this.panierService.removeDateRetrait(this.panier);
  }

  handleClickPaiementEnLigne(event, panier) {
    const ref = this.dialogService.open(PaiementCBComponent, {
        header: 'Paiement',
        width: '70%',
        data: panier
    });
    ref.onClose.subscribe((a:number) => {
      /*
      	  		IEtatPaiementCourant.PAIEMENT_OK 1
	  		IEtatPaiementCourant.PAIEMENT_ERREUR 2
			IEtatPaiementCourant.PAIEMENT_EN_COURS 3
      */
        console.log("statut du paiement = "+a);
        if(a==1) {

          var codePanierValide = this.panier.codeDocument;
          var idPanierValide = this.panier.id;
          var tenant: string = this.appSettings.BDG_TENANT_DOSSIER;
          var codeClient: string = this.appSettings.CODE_CLIENT_CHEZ_FRS;
          this.panierService.cherchePanierActif(tenant, codeClient).subscribe(data => {
            this.panier = data;
            if(localStorage.getItem('panier')==null) {
              //this.currentUser = JSON.parse(localStorage.getItem('panier'));
              localStorage.setItem('panier', JSON.stringify(this.panier));
            }
            //this.panierService.panier = this.panier;
            this.panierService.changePanier(data);
            //console.log(panier);
          });

          this.router.navigate(['/confirmation-paiement-panier',{codePanier:codePanierValide, idPanier:idPanierValide}]);
          
        } else {
          this.ngOnInit(); //refresh
        }
        
    });
  }

  handleClickConfirmationCommandeManuel(event, panier) {

    /*
     * type paiement
     * 1 CB
     * 2 sur place
     * 3 chèque
     * 4 virement
     * 
     * type expedition
     * 1 retrait
     * 2 livraison
     */

    var typePaiement:number = Number(this.selectedTypePaiement);
    var typeExpedition:number = Number(this.selectedTypeExpedition);
/*
*****************

ajouter param dans cet appel pour ajouter une ligne de commentaire avec la date de retrait si c'est un retrait et si la date est définie

voir comment et quand ajouter cette ligne pour les paiement en ligne

SINON (mieux)
se baser sur le fonctionnement de la ligne frais de port avec un debut de libellé fixe qui permettrait de la chercher pour l'ajouter/supprimer

ET
Voir si nouvelle version de primeng corrige l'incrémentation des minutes
TESTER cette incrémentation sur un calendar avec l'heure seulment

***************
*/

      this.panierService.validerCommandePourReglementUlterieur(this.panier.codeDocument, this.panier.id, typePaiement, typeExpedition).subscribe(data => {

            var codePanierValide = this.panier.codeDocument;
            var idPanierValide = this.panier.id;
            var tenant: string = this.appSettings.BDG_TENANT_DOSSIER;
            var codeClient: string = this.appSettings.CODE_CLIENT_CHEZ_FRS;
            this.panierService.cherchePanierActif(tenant, codeClient).subscribe(data => {
              this.panier = data;
              if(localStorage.getItem('panier')==null) {
                //this.currentUser = JSON.parse(localStorage.getItem('panier'));
                localStorage.setItem('panier', JSON.stringify(this.panier));
              }
              //this.panierService.panier = this.panier;
              this.panierService.changePanier(data);
              //console.log(panier);
            });
        
            this.router.navigate(['/confirmation-paiement-panier',{codePanier:codePanierValide, idPanier:idPanierValide}]);

        }
      );
  }

  handleClickCgv(event/*, fact*/) {
    const ref = this.dialogService.open(CGVComponent, {
        header: 'Conditions générales de vente',
        width: '70%',
        //data: fact
    });
    ref.onClose.subscribe(() => {
        //this.ngOnInit(); //refresh
    });
  }

}
