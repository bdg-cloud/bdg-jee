import { Component, OnInit } from '@angular/core';

import { Observable, Subject } from 'rxjs';

import {
    debounceTime, distinctUntilChanged, switchMap
} from 'rxjs/operators';


import { FactureService } from '../services/facture.service';

import { Facture } from '../model/facture';


import { TableModule } from 'primeng/table';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { DynamicDialogModule } from 'primeng/dynamicdialog';
import { DialogService } from 'primeng/dynamicdialog';
import { PaiementCBComponent } from '../paiement-cb/paiement-cb.component';
import { PaiementStripeService } from '../services/paiement-stripe.service';

import { AppSettings } from '../env';

@Component({
    selector: 'app-mes-factures',
    templateUrl: './mes-factures.component.html',
    styleUrls: ['./mes-factures.component.css'],
    providers: [MessageService, DialogService]
})
export class MesFacturesComponent implements OnInit {

    private searchTerms = new Subject<string>();
    public factures: Facture[];
    public selectedFacture: Facture;
    public paiementPossible = false;
    public loading = false;

    constructor(
        private factureService: FactureService,
        private messageService: MessageService,
        public dialogService: DialogService,
        private paiementStripeService: PaiementStripeService,
        private appSettings: AppSettings) { }


    ngOnInit(): void {
        var tenant: string = this.appSettings.BDG_TENANT_DOSSIER;
        var codeClient: string = this.appSettings.CODE_CLIENT_CHEZ_FRS;
        var debut: Date = this.appSettings.EXO_DATE_DEBUT;
        var fin: Date = this.appSettings.EXO_DATE_FIN;
        this.fournisseurPermetPaiementParCB();
        this.loading = true;
        this.factureService.chercheFactures(tenant, codeClient, debut, fin).subscribe(data => {
            this.factures = data;
            this.loading = false;

        });

        console.log('aaaa');
    }

    fournisseurPermetPaiementParCB() {
        this.paiementStripeService.fournisseurPermetPaiementParCB(this.appSettings.BDG_TENANT_DOSSIER, this.appSettings.CODE_CLIENT_CHEZ_FRS)
            .subscribe(data => {
                //alert(data);
                this.paiementPossible = data;
            });
    }

    onRowSelect(event) {
        this.messageService.add({ severity: 'info', summary: 'Facture', detail: 'N°: ' + event.data.codeDocument });
    }

    onRowUnselect(event) {
        this.messageService.add({ severity: 'info', summary: 'Facture', detail: 'N°: ' + event.data.codeDocument });
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

    public showPDF(download: boolean, fact: Facture): void {
        var tenant: string = this.appSettings.BDG_TENANT_DOSSIER;
        //var codeFact = "FV1900141";
        var codeFact = fact.codeDocument;
        this.factureService.downloadFile(tenant, codeFact)
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
