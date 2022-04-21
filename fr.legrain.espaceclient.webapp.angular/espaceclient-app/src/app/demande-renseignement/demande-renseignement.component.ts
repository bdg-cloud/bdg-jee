import { Component, OnInit, AfterViewChecked, AfterViewInit } from '@angular/core';
import { DynamicDialogRef } from 'primeng/dynamicdialog';
import { DynamicDialogConfig } from 'primeng/dynamicdialog';
import { first } from 'rxjs/operators';

import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { PaiementStripeService } from '../services/paiement-stripe.service';

import { Article, ArticleAdapter } from '../model/article';

import { AppSettings } from '../env';
import { DemandeRenseignement } from '../model/demande-renseignement';
import { CatalogueService } from '../services/catalogue.service';

@Component({
  selector: 'app-demande-renseignement',
  templateUrl: './demande-renseignement.component.html',
  styleUrls: ['./demande-renseignement.component.scss']
})
export class DemandeRenseignementComponent implements OnInit {

  public art:Article;
  public texteDemande:string;

  constructor(public ref: DynamicDialogRef, public config: DynamicDialogConfig,private appSettings: AppSettings,private catalogueService: CatalogueService) { }

  ngOnInit(): void {
    //this.montantPaiement = this.config.data.netTtcCalc;
    if (this.config.data instanceof Article) {
      this.art = this.config.data;
    }
    
  }

  public actEnvoyerDemande() {
    //var question:DemandeRenseignement;
    this.catalogueService.emailDemandeRenseignement(this.art,this.texteDemande).subscribe(

      data => {
          this.actFermer(null) ;
          alert("Votre demande bien a été prise en compte, nous vous contacterons prochainement.");
      }

  );
  }

  public actFermer(event) {
    this.ref.close();
  }

}
