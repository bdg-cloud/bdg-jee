import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map, tap, takeWhile, filter } from 'rxjs/operators';

import { Facture, FactureAdapter } from '../model/facture';
import { Devis, DevisAdapter } from '../model/devis';
import { AvisEcheance, AvisEcheanceAdapter } from '../model/avisEcheance';
import { Abonnement, AbonnementAdapter } from '../model/abonnement';

import { Tiers } from '../model/tiers';
import { AppSettings } from '../env';
import { DatePipe } from '@angular/common';

@Injectable({
    providedIn: 'root'
})
export class DocumentService {

    private _url = "";

    constructor(private http: HttpClient, private datePipe: DatePipe, private factureAdapter: FactureAdapter, private devisAdapter: DevisAdapter,
        private avisEcheanceAdapter: AvisEcheanceAdapter, private abonnementAdapter: AbonnementAdapter, private appSettings: AppSettings) {
        this._url = this.appSettings.BASE_API_ENDPOINT + '/factures';
    }
}
