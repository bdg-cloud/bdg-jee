import { Component, OnInit } from '@angular/core';

import { Observable, Subject } from 'rxjs';

import {
  debounceTime, distinctUntilChanged, switchMap
} from 'rxjs/operators';

import { AbonnementService } from '../services/abonnement.service';
import { Abonnement } from '../model/abonnement';

import { EspaceClientService } from '../services/espace-client.service';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { DynamicDialogModule } from 'primeng/dynamicdialog';
import { DialogService } from 'primeng/dynamicdialog';
import { Router } from '@angular/router';

import { AppSettings } from '../env';

import { HttpEventType } from '@angular/common/http';
import { Echeance } from '../model/echeance';
import { PanierService } from '../services/panier.service';

import { AvisEcheanceService } from '../services/avis-echeance.service';

import { AvisEcheance, AvisEcheanceAdapter } from '../model/avisEcheance';

@Component({
  selector: 'app-mes-abonnements',
  templateUrl: './mes-abonnements.component.html',
  styleUrls: ['./mes-abonnements.component.css'],
  providers: [MessageService, DialogService]
})
export class MesAbonnementsComponent implements OnInit {

  private searchTerms = new Subject<string>();
  public abonnements: Abonnement[];
  public selectedAbonnement: Abonnement;
  public paiementPossible = false;
  public loading = false;
  public modificationSelectionEcheance = false;
  public ancienneVersion = false;

  public avisEcheances: AvisEcheance[];
  public selectedAvisEcheance: AvisEcheance;

  public echeances: Echeance[];
  public selectedEcheances: Echeance[];

  constructor(
    private avisEcheanceService: AvisEcheanceService,
    private abonnementService: AbonnementService,
    private messageService: MessageService,
    public dialogService: DialogService,
    private espaceClientService: EspaceClientService,
    private panierService: PanierService,
    private appSettings: AppSettings,
    private router: Router,) { }


  ngOnInit(): void {
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

    this.abonnementService.chercheEcheances(tenant, codeClient, debut, fin).subscribe(data => {
      this.echeances = data;
      this.selectedEcheances = this.echeances;
      this.loading = false;

    });

    this.avisEcheanceService.chercheAvisEcheance(tenant, codeClient, debut, fin).subscribe(data => {
      this.avisEcheances = data;
      this.loading = false;
      //            this.avisEcheances.forEach(function (value) {
      //                this.documentService.facturePourAvisEcheance(tenant,value.codeDocument).subscribe( data => {
      //                    if(data) {
      //                        //il y a une facture pour cet avis d'échéance
      //                        value.codeFacture = data.codeDocument;
      //                    } else {
      //                        //pas de facture
      //                    }
      //                });
      //              }); 

  });

    console.log('aaaa');
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


  public renouveler(): void {
    //selectedEcheances
    //alert(this.selectedEcheances);
    
    /*
    this.abonnementService.ajouterLigneEcheancePanier(this.selectedEcheances[0], null).subscribe(data => {
      this.panierService.changePanier(data);
    });
    */

    this.abonnementService.ajouterLignesEcheancePanier(this.selectedEcheances, null).subscribe(data => {
      this.panierService.changePanier(data);
      if(data.lignes!=null && data.lignes.length>0) {
        this.router.navigate(['/validation-panier']);
      }
    });
  }

  public modifier(): void {
    this.modificationSelectionEcheance = !this.modificationSelectionEcheance;
  }

  public changeVersion(): void {
    this.ancienneVersion = !this.ancienneVersion;
  }

  onRowSelect(event) {
    this.messageService.add({ severity: 'info', summary: 'AvisEcheance', detail: 'N°: ' + event.data.codeDocument });
}

onRowUnselect(event) {
    this.messageService.add({ severity: 'info', summary: 'AvisEcheance', detail: 'N°: ' + event.data.codeDocument });
}

/*
handleClick(event, fact) {
    const ref = this.dialogService.open(PaiementCBComponent, {
        header: 'Paiement',
        width: '70%',
        data: fact
    });
    ref.onClose.subscribe(() => {
        this.ngOnInit(); //refresh
    });
}
*/

handleClick2(event, fact) {
    this.showPDF(true, fact);
}

public showPDF(download: boolean, AvisEcheance: AvisEcheance): void {
    var tenant: string = this.appSettings.BDG_TENANT_DOSSIER;
    var codeFact = AvisEcheance.codeDocument;
    this.avisEcheanceService.downloadFileAvisEcheance(tenant, codeFact)
        .subscribe(x => {
            // It is necessary to create a new blob object with mime-type explicitly set
            // otherwise only Chrome works like it should
            var newBlob = new Blob([x], { type: "application/pdf" });

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
                link.download = codeFact + ".pdf";
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
        });
}
}
