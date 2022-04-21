import { Component, OnInit } from '@angular/core';
import { AppSettings } from '../env';
import { EspaceClientService } from '../services/espace-client.service';
import { AuthenticationService } from '../services/authentication.service';
import { DomSanitizer } from '@angular/platform-browser';

import { Telechargement, TelechargementAdapter } from '../model/telechargement';
import { Subject } from 'rxjs';
import { Abonnement } from '../model/abonnement';
import { MessageService } from 'primeng/api';
import { DialogService } from 'primeng/dynamicdialog';
import { AbonnementService } from '../services/abonnement.service';
import { PanierService } from '../services/panier.service';
import { Router } from '@angular/router';
import { HttpEventType } from '@angular/common/http';

@Component({
  selector: 'app-telechargement',
  templateUrl: './telechargement.component.html',
  styleUrls: ['./telechargement.component.css'],
  providers: [MessageService, DialogService]
})
export class TelechargementComponent implements OnInit {

  public telechargements: Telechargement[];
  public afficheTelechargement: boolean;

  private searchTerms = new Subject<string>();
  public abonnements: Abonnement[];
  public selectedAbonnement: Abonnement;
  public paiementPossible = false;
  public loading = false;
  public modificationSelectionEcheance = false;
  public ancienneVersion = false;

  constructor(private authenticationService: AuthenticationService,
     private sanitizer: DomSanitizer,
     private abonnementService: AbonnementService,
     private messageService: MessageService,
     public dialogService: DialogService,
     private espaceClientService: EspaceClientService,
     private panierService: PanierService,
     private appSettings: AppSettings,
     private router: Router,) { }

  ngOnInit() {

    var tenant: string = this.appSettings.BDG_TENANT_DOSSIER;
    var codeClient: string = this.appSettings.CODE_CLIENT_CHEZ_FRS;
    var debut: Date = this.appSettings.EXO_DATE_DEBUT;
    var fin: Date = this.appSettings.EXO_DATE_FIN;
    // this.fournisseurPermetPaiementParCB();
    this.loading = true;
    this.abonnementService.chercheAbonnements(tenant, codeClient, debut, fin).subscribe(data => {
      this.abonnements = data;
      this.loading = false;

    });

  }

  telechargementAutorise(abo: Abonnement): boolean {
    var maintenant = Date.now();
    var dateLimite = new Date('2019-12-31');
    //        this.authenticationService.nomFichier(abo.codeArticle).subscribe( data => {
    //            if(data) {
    //                abo.produitTelechargeable = true;
    //            }
    //        });
    if (+maintenant < +dateLimite)
      return false;
    else
      return abo.suspension;
  }

  telechargement(event, abo: Abonnement) {

    var nomFichier;

    nomFichier = abo.codeArticle;
    this.telechargementFichier(abo, true, nomFichier);

  }

  public telechargementFichier(abo: Abonnement, download: boolean, nomFichier: string): void {
    var tenant: string = this.appSettings.BDG_TENANT_DOSSIER;
    //var codeFact = "FV1900141";
    //      var codeFact = fact.codeDocument;
    var codeFact = "xxxxxxxxxxx";
    var nomFichierDownload = "setup.exe";
    abo.isDisabled = true;
    this.espaceClientService.nomFichier(nomFichier).subscribe(data => {
      //alert(data);
      nomFichierDownload = data;
    });

    /*
    this.fileNavigationService.downloadFile(element).subscribe(result => {
          if (result.type === HttpEventType.DownloadProgress) {
          const percentDone = Math.round(100 * result.loaded / result.total);
          console.log(percentDone);
          }
          if (result.type === HttpEventType.Response) {
          this.generateDownload(result.body);
          }
      });
    */

    this.espaceClientService.download(nomFichier)
      .subscribe(result => {
        if (result.type === HttpEventType.DownloadProgress) {
          abo.percentDone = Math.round(100 * result.loaded / result.total);
          //console.log(abo.percentDone);
        }
        if (result.type === HttpEventType.Response) {
          // It is necessary to create a new blob object with mime-type explicitly set
          // otherwise only Chrome works like it should
          //var newBlob = new Blob([x], { type: "application/pdf" });
          var newBlob = new Blob([result.body], { type: "application/octet-stream" });

          // IE doesn't allow using a blob object directly as link href
          // instead it is necessary to use msSaveOrOpenBlob
          if (window.navigator && window.navigator.msSaveOrOpenBlob) {
            window.navigator.msSaveOrOpenBlob(newBlob);
            return;
          }

          // For other browsers: 
          // Create a link pointing to the ObjectURL containing the blob.
          const data = window.URL.createObjectURL(newBlob);
          if (download) {
            var link = document.createElement('a');
            link.href = data;
            link.download = nomFichierDownload;
            // this is necessary as link.click() does not work on the latest firefox
            link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));

            setTimeout(function () {
              // For Firefox it is necessary to delay revoking the ObjectURL
              window.URL.revokeObjectURL(data);
              link.remove();
            }, 100);
          } else {
            window.open(data);
          }
          abo.isDisabled = false;
        }
      });
  }



}
