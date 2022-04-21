import { Component, OnInit, AfterViewInit } from '@angular/core';

import { Observable, Subject } from 'rxjs';

import {
    debounceTime, distinctUntilChanged, switchMap
} from 'rxjs/operators';


import { CatalogueService } from '../services/catalogue.service';

import { Article } from '../model/article';
import { Categorie } from '../model/categorie';

import { TableModule } from 'primeng/table';
import { DataViewModule } from 'primeng/dataview';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { DynamicDialogModule } from 'primeng/dynamicdialog';
import { DialogService } from 'primeng/dynamicdialog';
import { PaiementCBComponent } from '../paiement-cb/paiement-cb.component';
import { PaiementStripeService } from '../services/paiement-stripe.service';
import { PanierService } from '../services/panier.service';
import { DomSanitizer } from '@angular/platform-browser';
import { Input, OnChanges,ChangeDetectorRef } from "@angular/core";

import {SelectItem} from 'primeng/api';
import {MenuItem} from 'primeng/api';

import { AppSettings } from '../env';
import { areAllEquivalent } from '@angular/compiler/src/output/output_ast';
import { Panier } from '../model/panier';

@Component({
  selector: 'app-catalogue',
  templateUrl: './catalogue.component.html',
  styleUrls: ['./catalogue.component.css'],
  providers: [MessageService, DialogService]
})
export class CatalogueComponent implements OnInit, AfterViewInit, OnChanges {

  private searchTerms = new Subject<string>();
    public articles: Article[];
    //public categories: Categorie[];
    public categories: SelectItem[];
    public selectedArticle: Article;
    //public selectedCategorie: Categorie;
    public selectedCategorie: String;
    public paiementPossible = false;
    public prixCatalogueHt: boolean = false;

    public categorieRecherche: Categorie;

    public items: MenuItem[];

    sortOptions: SelectItem[];

    sortOrder: number;

    sortField: string;

    public afficherPrix: boolean;
    public afficherPanier: boolean;
    public texteAccueilBoutique: String;

    constructor(
        private catalogueService: CatalogueService,
        private messageService: MessageService,
        public dialogService: DialogService,
        private sanitizer: DomSanitizer,
        private ref: ChangeDetectorRef,
        private paiementStripeService: PaiementStripeService,
        private appSettings: AppSettings,
        private panierService: PanierService) { }


    ngOnInit(): void {
        var tenant: string = this.appSettings.BDG_TENANT_DOSSIER;
        var codeClient: string = this.appSettings.CODE_CLIENT_CHEZ_FRS;
        //var debut: Date = this.appSettings.EXO_DATE_DEBUT;
        //var fin: Date = this.appSettings.EXO_DATE_FIN;
        this.afficherPrix = this.appSettings.PARAMETRES.afficherPrixCatalogue;
        this.afficherPanier = this.appSettings.PARAMETRES.activePanier /*|| this.appSettings.PARAMETRES.activeCommandeEmailSimple*/;
        this.texteAccueilBoutique = this.appSettings.PARAMETRES.texteAccueilCatalogue;
        this.prixCatalogueHt = this.appSettings.PARAMETRES.prixCatalogueHt;
        
        /*
        this.catalogueService.listeArticleCatalogue(tenant, codeClient).subscribe(data => {
            this.articles = data;
        });
        
        */
        this.catalogueService.listeArticleCatalogue(tenant, codeClient/*, debut, fin*/).subscribe(data => {
            this.articles = data;
        });
        
        this.catalogueService.listeCategorieCatalogueSelectItem(tenant, codeClient/*, debut, fin*/).subscribe(data => {
            /*var c = new Categorie(0,
                'TOUS',
                'TOUS',
                'TOUS',
                'TOUS',
                'TOUS',
                'TOUS',
                'TOUS',
              );*/
            var c = {label:'TOUS', value:new Categorie(0,
                'TOUS',
                'TOUS',
                'TOUS',
                'TOUS',
                'TOUS',
                'TOUS',
                'TOUS',
              )
              };
              data.unshift(c);
            this.categories = data;
        });

        this.catalogueService.listeCategorieCatalogue(tenant, codeClient/*, debut, fin*/).subscribe(data => {
            var c = new Categorie(0,
                'Tous',
                'Tous',
                'Tous',
                'Tous',
                'Tous',
                'Tous',
                'Tous',
              );
              data.unshift(c);
            this.items = [];
            for (let index = 0; index < data.length; index++) {
                if(data[index].libelleCategorieArticle=='Tous') {
                    this.items.push(
                        { 
                            label: data[index].libelleCategorieArticle, 
                            icon: 'fa fa-th-large', //'fa-dot-circle', //fa-th-large
                            command: (event) => {this.clickMenu(data[index])}
                        }
                    );
                } else {
                    this.items.push(
                        { 
                            label: data[index].libelleCategorieArticle, 
                            icon: 'far fa-dot-circle', //'fa-dot-circle', //fa-th-large
                            command: (event) => {this.clickMenu(data[index])}
                        }
                    );
                }
            }
        });

        /*
        this.panierService.cherchePanierActif(tenant, codeClient).subscribe(data => {
            var panier:Panier = data;
            this.panierService.panier = panier;
            console.log(panier);
        });
        */

        console.log('aaaa');

        this.sortOptions = [
            {label: 'Du + cher au - cher', value: '!prixttcPrix'},
            {label: 'Du - cher au + cher', value: 'prixttcPrix'}
        ];

        this.categorieRecherche = null;
    }

    public ngOnChanges(): void {
        //this.articles = data;
    }

    
    addToCart(product:Article) {
        this.panierService.addToCart(product);
        //window.alert('Le produit a été ajouté au panier.');
        this.messageService.add({ severity: 'info', summary: 'Panier', detail: product.libelleCatalogueCatWeb+' a été ajouté au panier.' });
    }
    

    onChange(event) {
        console.log('event :' + event);
        console.log(event.value);
        var tenant: string = this.appSettings.BDG_TENANT_DOSSIER;
        var codeClient: string = this.appSettings.CODE_CLIENT_CHEZ_FRS;

        if(event.value.id!=0) {
            this.catalogueService.listeArticleCategorieCatalogue(tenant, codeClient,event.value.id).subscribe(data => {
                this.articles = data;
            });
        } else {
            this.catalogueService.listeArticleCatalogue(tenant, codeClient/*, debut, fin*/).subscribe(data => {
                this.articles = data;
            });
        }
    }

    clickMenu(event) {
        console.log('event :' + event);
        console.log(event.value);
        var tenant: string = this.appSettings.BDG_TENANT_DOSSIER;
        var codeClient: string = this.appSettings.CODE_CLIENT_CHEZ_FRS;

        if(event.id!=0) {
            this.categorieRecherche = event;

            this.catalogueService.listeArticleCategorieCatalogue(tenant, codeClient,event.id).subscribe(data => {
                this.articles = data;
            });
        } else {
            this.categorieRecherche = null; //a changer si on met une image par defaut pour TOUS
            this.catalogueService.listeArticleCatalogue(tenant, codeClient/*, debut, fin*/).subscribe(data => {
                this.articles = data;
            });
        }
    }

    ngAfterViewInit() {
        /*
        this.articles.forEach((art, index) => {
            this.catalogueService.imageDefautArticle(art.id).subscribe((blob: any) => {
                let objectURL = URL.createObjectURL(blob);
                this.articles[index].imageDefaut = this.sanitizer.bypassSecurityTrustStyle('url(' + objectURL + ')');
               // this.ref.detectChanges();
            }
            )
        });
        */
      }  
    
    onSortChange(event) {
        let value = event.value;

        if (value.indexOf('!') === 0) {
            this.sortOrder = -1;
            this.sortField = value.substring(1, value.length);
        }
        else {
            this.sortOrder = 1;
            this.sortField = value;
        }
    }

    onRowSelect(event) {
        this.messageService.add({ severity: 'info', summary: 'Article', detail: 'N°: ' + event.data.codeArticle });
    }

    onRowUnselect(event) {
        this.messageService.add({ severity: 'info', summary: 'Article', detail: 'N°: ' + event.data.codeArticle });
    }

    handleClick(event, fact) {
      /*
        const ref = this.dialogService.open(PaiementCBComponent, {
            header: 'Paiement',
            width: '70%',
            data: fact
        });
        ref.onClose.subscribe(() => {
            this.ngOnInit(); //refresh
        });
        */
    }

}
