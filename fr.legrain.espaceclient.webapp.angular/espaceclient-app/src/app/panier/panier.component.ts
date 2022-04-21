import { Component, OnInit } from '@angular/core';
import { PanierService } from '../services/panier.service';
import { AppSettings } from '../env';
import { Router } from '@angular/router';

import { Panier } from '../model/panier';
import { LignePanier } from '../model/lignePanier';

@Component({
  selector: 'app-panier',
  templateUrl: './panier.component.html',
  styleUrls: ['./panier.component.scss']
})
export class PanierComponent implements OnInit {

  //items: LignePanier[];
  panier: Panier;
  loading: boolean = true;

  constructor(private appSettings: AppSettings,
    private panierService: PanierService,
    private router: Router,) { }

  ngOnInit(): void {
    //this.items = this.panierService.getItems();
    this.panier = this.panierService.getPanier();

    /* //FAIT DANS LE APP-TOP-BAR (pour l'instant)
    var tenant: string = this.appSettings.BDG_TENANT_DOSSIER;
    var codeClient: string = this.appSettings.CODE_CLIENT_CHEZ_FRS;
    this.panierService.cherchePanierActif(tenant, codeClient).subscribe(data => {
      this.panier = data;
      if(localStorage.getItem('panier')==null) {
        //this.currentUser = JSON.parse(localStorage.getItem('panier'));
        localStorage.setItem('panier', JSON.stringify(this.panier));
      }
      //this.panierService.panier = this.panier;
      this.panier = this.panierService.getPanier();
      //console.log(panier);
    });
*/
    this.loading = true;
    this.panierService.panier.subscribe(
      panier => {this.panier = panier;
    }
    );
    //this.panierService.removeFdp(this.panier);
    //this.panierService.removeDateRetrait(this.panier);
    //this.panierService.removeLignePrixVariable(this.panier);
    //this.panierService.removeTout(this.panier);

    this.panierService.removeTout(this.panier).subscribe(data => {
      this.panierService.changePanier(data);
      this.loading = false;
      console.log(data);
    }); 
  }

  actPasserCommande() {
    if(this.panier.lignes!=null && this.panier.lignes.length>0) {
      this.router.navigate(['/validation-panier']);
    }
    //reprendre les données de BDG
  }

  actModifQte(event, ligne:LignePanier) {
    //alert(ligne.libelleCatalogueCatWeb);
    /*
    ligne.mtTtcLDocument = ligne.qteLDocument*ligne.prixULDocument;
*/
    this.panierService.updateCartLine(ligne);
    //mettre a jour via BDG
    //mettre a jour les totaux 
  }

  actSupprimerLigne(ligne:LignePanier) {
    //alert(ligne.libelleCatalogueCatWeb);
    this.panierService.removeFromCart(ligne);
    /*
    const index: number = this.panier.lignes.indexOf(ligne);
    if (index !== -1) {
        this.panier.lignes.splice(index, 1);
    }  
    */
    //mettre a jour via BDG
    //mettre a jour les totaux 
  }

}
