import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';

import { EspaceClientService } from '../services/espace-client.service';
import { AuthenticationService } from '../services/authentication.service';
import { CompteEspaceClient, CompteEspaceClientAdapter } from '../model/compteEspaceClient';
import { Tiers, TiersAdapter } from '../model/tiers';
import { AppSettings } from '../env';

@Component({
    selector: 'app-mes-informations',
    templateUrl: './mes-informations.component.html',
    styleUrls: ['./mes-informations.component.css']
})
export class MesInformationsComponent implements OnInit {

    creerCompteForm: FormGroup;
    loading = false;
    submitted = false;
    returnUrl: string;
    error = '';
    public saisie = true;
    public tiers: Tiers;
    public compteEspaceClient: CompteEspaceClient;


    constructor(
        private formBuilder: FormBuilder,
        private route: ActivatedRoute,
        private router: Router,
        private authenticationService: AuthenticationService,
        private espaceClientService: EspaceClientService,
        private appSettings: AppSettings
    ) { }

    ngOnInit() {
        this.creerCompteForm = this.formBuilder.group({
            nom: ['', Validators.required],
            prenom: ['', Validators.required],
            email: ['', Validators.required],
            mdp: ['', Validators.required],
            mdpVerif: ['', Validators.required],
            codeTiers: [''],
        });

        this.returnUrl = /*this.route.snapshot.queryParams['returnUrl'] ||*/ '/login';

        this.espaceClientService.infosTiers().subscribe(data => {
            this.tiers = data;
            // this.creerCompteForm.get('codeTiers').setValue(this.tiers.codeTiers);
        });

        this.espaceClientService.infosCompteEspaceClient().subscribe(data => {
            this.compteEspaceClient = data;
            this.creerCompteForm.get('nom').setValue(this.compteEspaceClient.nom);
            this.creerCompteForm.get('prenom').setValue(this.compteEspaceClient.prenom);
            this.creerCompteForm.get('email').setValue(this.compteEspaceClient.email);
            this.creerCompteForm.get('codeTiers').setValue(this.compteEspaceClient.codeTiers);
        });

        this.appSettings.initTenantFromUrl();
        console.log(this.appSettings.BDG_TENANT_DOSSIER);
    }

    public actAnnuler(event) {
        this.router.navigate([this.returnUrl]);
    }

    //convenience getter for easy access to form fields
    get f() { return this.creerCompteForm.controls; }

    onSubmit() {
        this.submitted = true;

        // stop here if form is invalid
        if (this.creerCompteForm.invalid) {
            return;
        }

        this.loading = true;
        var compte: CompteEspaceClient = new CompteEspaceClient(
            this.appSettings.BDG_ID_ESPACE_CLIENT,
            this.appSettings.HOST_NAME,
            this.appSettings.HOST_PORT,
            this.creerCompteForm.value.codeTiers,
            this.creerCompteForm.value.nom,
            this.creerCompteForm.value.prenom,
            this.creerCompteForm.value.email,
            this.creerCompteForm.value.mdp,
            this.creerCompteForm.value.mdpVerif,
            true,
            "",
            "", "", ""

            //            this.f.codeTiers.value,
            //            this.f.nom.value,
            //            this.f.prenom.value,
            //            this.f.email.value,
            //            this.f.mdp.value,
            //            true,
        );
        //this.espaceClientService.creerCompteEspaceClient(compte)
        this.espaceClientService.emailCreerCompteEspaceClient(compte)
            .pipe(first())
            .subscribe(
                data => {
                    //  this.router.navigate([this.returnUrl]);
                    this.saisie = false;
                },
                error => {
                    this.error = error;
                    this.loading = false;
                });
    }
}