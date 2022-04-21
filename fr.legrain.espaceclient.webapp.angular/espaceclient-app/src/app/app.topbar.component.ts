import { Component, OnInit } from '@angular/core';
import { HomeComponent } from './home/home.component';

import { EspaceClientService } from './services/espace-client.service';
import { AuthenticationService } from './services/authentication.service';
import { Router } from '@angular/router';
import { AppSettings } from './env';
import { PanierService } from './services/panier.service';

import { DomSanitizer } from '@angular/platform-browser';
import { Panier } from './model/panier';
import { LignePanier } from './model/lignePanier';

@Component({
  selector: 'app-topbar',
  templateUrl: './app.topbar.component.html'
})
export class AppTopBarComponent implements OnInit {

  public currentUser: any;
  public afficherPanier: boolean;

  image: any = null;

  //items: LignePanier[];
  panier: Panier;
  nbLignePanier:number =0;

  constructor(public app: HomeComponent,
    private router: Router,
    private authenticationService: AuthenticationService,
    private espaceClientService: EspaceClientService,
    private sanitizer: DomSanitizer,
    private appSettings: AppSettings,
    private panierService: PanierService
  ) {
    // this.authenticationService.currentUser.subscribe(x => this.currentUser = x);
    this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
    //            this.currentUser.codeTiers = this.appSettings.CODE_CLIENT_CHEZ_FRS;

    if (this.currentUser.codeTiers === 'XXXXXXXXXXXXXXX') {
      console.log("Pas de code tiers/client => déconnexion")
      this.logout();
    }

    this.appSettings.initTenantFromUrl();

    this.espaceClientService.logoHeader().subscribe((blob: any) => {
      let objectURL = URL.createObjectURL(blob);
      this.image = this.sanitizer.bypassSecurityTrustUrl(objectURL);
    },
      error => {
        this.image = null;
      }
    );
  }

  ngOnInit() {    
      this.espaceClientService.parametres().subscribe(
        //TODO voir si on peut éviter de récupérer 2 fois les param (ici et dans app.menu.components)
        data => {
            this.appSettings.PARAMETRES = data;
            this.afficherPanier = this.appSettings.PARAMETRES.activePanier /*|| this.appSettings.PARAMETRES.activeCommandeEmailSimple*/;   
        }
    );

    //this.items = this.panierService.getItems();
    this.panier = this.panierService.getPanier();

    var tenant: string = this.appSettings.BDG_TENANT_DOSSIER;
    var codeClient: string = this.appSettings.CODE_CLIENT_CHEZ_FRS;
    
    /*if(codeClient!=null) {*/
      //le compte connecté est un tiers BDG

      this.panierService.cherchePanierActif(tenant, codeClient).subscribe(data => {
        this.panier = data;
        if(localStorage.getItem('panier')==null) {
          localStorage.setItem('panier', JSON.stringify(this.panier));
        }
        //this.panierService.panier = this.panier;
        this.panierService.changePanier(data);
        //console.log(panier);
      });
      /*
    } else {
      //on travaille sur un panier local
      if(localStorage.getItem('panier')!=null) {
        //recuperation du panier dans le localStorage du navigateur
        this.panier = JSON.parse(localStorage.getItem('panier'));
      } else {
        //création d'un nouveau panier et stockage dans le storage
        this.panier = new Panier(null,"code","libl",new Date(),0,0,0,0,null,"");
        localStorage.setItem('panier', JSON.stringify(this.panier));
      }
      this.panierService.changePanier(this.panier);
    }
    */

    this.panierService.panier.subscribe(panier => {
      //mise a jour de la pastille représentant le nombre de ligne dans le panier
      this.panier = panier;
      if(panier && panier.lignes)
        this.nbLignePanier = panier.lignes.length;
        var trouve:boolean = false;
        panier.lignes.forEach( ligne => {if(ligne.libLDocument==="Frais de port") trouve=true;}) //faire comparaison avec code art des param fdp
        if(trouve) this.nbLignePanier = this.nbLignePanier-1;

        trouve = false;
        panier.lignes.forEach( ligne => {if(ligne.libLDocument.startsWith("RETRAIT")) trouve=true;}) //faire comparaison avec code art des param fdp
        if(trouve) this.nbLignePanier = this.nbLignePanier-1;
    });
  }

  logout() {
    this.authenticationService.logout();
    this.router.navigate(['/login']);
    window.location.reload();
  }

  moncompte() {
    this.router.navigate(['/mes-informations']);
  }
}
