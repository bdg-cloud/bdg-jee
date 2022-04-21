import { Component, OnInit } from '@angular/core';
import {Router, ActivatedRoute, Params} from '@angular/router';


import { CatalogueService } from '../services/catalogue.service';

import { Article } from '../model/article';

import { TableModule } from 'primeng/table';
import { DataViewModule } from 'primeng/dataview';
//import { MessageService } from 'primeng/components/common/messageservice';
import { ButtonModule } from 'primeng/button';
import { DynamicDialogModule } from 'primeng/dynamicdialog';
//import { DialogService } from 'primeng/api';
import { PaiementCBComponent } from '../paiement-cb/paiement-cb.component';
import { DemandeRenseignementComponent } from '../demande-renseignement/demande-renseignement.component';
import { PaiementStripeService } from '../services/paiement-stripe.service';
import { DomSanitizer } from '@angular/platform-browser';
import { Input, OnChanges,ChangeDetectorRef } from "@angular/core";
import { PanierService } from '../services/panier.service';

import { MessageService } from 'primeng/api';
import { DialogService } from 'primeng/dynamicdialog';

import {SelectItem} from 'primeng/api';

import { AppSettings } from '../env';

@Component({
  selector: 'app-fiche-produit',
  templateUrl: './fiche-produit.component.html',
  styleUrls: ['./fiche-produit.component.css'],
  providers: [MessageService, DialogService]
})
export class FicheProduitComponent implements OnInit {
/*
https://stackoverflow.com/questions/35688084/how-to-get-query-params-from-url-in-angular-2
https://medium.com/better-programming/angular-6-url-parameters-860db789db85
*/

  private _idArticle;
  private _art:Article;
  private _listeIdImage: any[];

  public afficherPrix: boolean;
  public afficherPanier: boolean;
  public afficherDemandeRenseignement: boolean;
  public prixCatalogueHt: boolean = false;

  get art(): Article {
    return this._art;
  }

  get listeIdImage(): any[] {
    return this._listeIdImage;
  }

  get idArticle() {
    return this._idArticle;
  }

  constructor(private activatedRoute: ActivatedRoute,private appSettings: AppSettings, private catalogueService: CatalogueService,
    private panierService: PanierService, private messageService: MessageService,
    public dialogService: DialogService,) {}

  ngOnInit() {
    var tenant: string = this.appSettings.BDG_TENANT_DOSSIER;
        var codeClient: string = this.appSettings.CODE_CLIENT_CHEZ_FRS;
        //var debut: Date = this.appSettings.EXO_DATE_DEBUT;
        //var fin: Date = this.appSettings.EXO_DATE_FIN;

        this.afficherPrix = this.appSettings.PARAMETRES.afficherPrixCatalogue;
        this.afficherPanier = this.appSettings.PARAMETRES.activePanier /*|| this.appSettings.PARAMETRES.activeCommandeEmailSimple*/;
        this.afficherDemandeRenseignement = this.appSettings.PARAMETRES.activeEmailRenseignementProduit;
        this.prixCatalogueHt = this.appSettings.PARAMETRES.prixCatalogueHt;
        /*
        this.catalogueService.listeArticleCatalogue(tenant, codeClient).subscribe(data => {
            this.articles = data;
        });
        
        */
       
    /*
    // Note: Below 'queryParams' can be replaced with 'params' depending on your requirements
    this.activatedRoute.queryParams.subscribe(params => {
        const userId = params['idArticle'];
        console.log(userId);
      });
    */
   this._idArticle = this.activatedRoute.snapshot.paramMap.get("idArticle");

      
   this.catalogueService.ficheArticleCatalogue(tenant, codeClient, this.idArticle).subscribe(data => {
          
    this._art = data;
      
    });

    this.catalogueService.imageArticleListeId(this.idArticle).subscribe(data => {
          
      console.log('liste ID images : ');
      console.log(data);
      this._listeIdImage = data;
      });
    
  }

  addToCart(product) {
    this.panierService.addToCart(product);
    //window.alert('Le produit a été ajouté au panier.');
    this.messageService.add({ severity: 'info', summary: 'Panier', detail: product.libelleCatalogueCatWeb+' a été ajouté au panier.' });
  }

  handleClick(event, art) {
    const ref = this.dialogService.open(DemandeRenseignementComponent, {
        header: 'Demande de renseignement',
        width: '70%',
        data: art
    });
    ref.onClose.subscribe(() => {
        //this.ngOnInit(); //refresh
    });
  }

}
