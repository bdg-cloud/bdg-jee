import { Component, OnInit } from '@angular/core';
import { MessageService } from 'primeng/api';
import { DialogService } from 'primeng/dynamicdialog';
import { Subject } from 'rxjs';
import { AppSettings } from '../env';
import { Commande } from '../model/commande';
import { PaiementCBComponent } from '../paiement-cb/paiement-cb.component';
import { CommandeService } from '../services/commande.service';
import { PaiementStripeService } from '../services/paiement-stripe.service';

@Component({
  selector: 'app-mes-commandes',
  templateUrl: './mes-commandes.component.html',
  styleUrls: ['./mes-commandes.component.css'],
  providers: [MessageService, DialogService]
})
export class MesCommandesComponent implements OnInit {

  private searchTerms = new Subject<string>();
    public commandes: Commande[];
    public selectedCommande: Commande;
    public paiementPossible = false;
    public loading = false;

    constructor(
        private commandeService: CommandeService,
        private messageService: MessageService,
        public dialogService: DialogService,
        private paiementStripeService: PaiementStripeService,
        private appSettings: AppSettings) { }


    ngOnInit(): void {
        var tenant: string = this.appSettings.BDG_TENANT_DOSSIER;
        var codeClient: string = this.appSettings.CODE_CLIENT_CHEZ_FRS;
        var debut: Date = this.appSettings.EXO_DATE_DEBUT;
        var fin: Date = this.appSettings.EXO_DATE_FIN;
        //         this.fournisseurPermetPaiementParCB();
        this.loading = true;
        this.commandeService.chercheCommande(tenant, codeClient, debut, fin).subscribe(data => {
            this.commandes = data;
            this.loading = false;

        });

        console.log('aaaa');

    }

    /*
    fournisseurPermetPaiementParCB() {
        this.paiementStripeService.fournisseurPermetPaiementParCB(this.appSettings.BDG_TENANT_DOSSIER,this.appSettings.CODE_CLIENT_CHEZ_FRS)
        .subscribe( data => {
            //alert(data);
            this.paiementPossible = data;
        });
    }
    */

    onRowSelect(event) {
        this.messageService.add({ severity: 'info', summary: 'Commande', detail: 'N°: ' + event.data.codeDocument });
    }

    onRowUnselect(event) {
        this.messageService.add({ severity: 'info', summary: 'Commande', detail: 'N°: ' + event.data.codeDocument });
    }

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

    handleClick2(event, fact) {
        this.showPDF(true, fact);
    }

    public showPDF(download: boolean, commande: Commande): void {
        var tenant: string = this.appSettings.BDG_TENANT_DOSSIER;
        //var codeFact = "FV1900141";
        var codeFact = commande.codeDocument;
        this.commandeService.downloadFileCommande(tenant, codeFact)
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