import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';

import { EspaceClientService } from '../services/espace-client.service';
import { AuthenticationService } from '../services/authentication.service';
import { CompteEspaceClient, CompteEspaceClientAdapter } from '../model/compteEspaceClient';
import { AppSettings } from '../env';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
    selector: 'app-creer-compte',
    templateUrl: './creer-compte.component.html',
    styleUrls: ['./creer-compte.component.css']
})
export class CreerCompteComponent implements OnInit {
    creerCompteForm: FormGroup;
    loading = false;
    submitted = false;
    returnUrl: string;
    error = '';
    image: any = null;
    public saisie = true;

    constructor(
        private formBuilder: FormBuilder,
        private route: ActivatedRoute,
        private router: Router,
        private authenticationService: AuthenticationService,
        private espaceClientService: EspaceClientService,
        private sanitizer: DomSanitizer,
        private appSettings: AppSettings
    ) {
        this.appSettings.initTenantFromUrl();

        this.espaceClientService.logoPageSimple().subscribe((blob: any) => {
            let objectURL = URL.createObjectURL(blob);
            this.image = this.sanitizer.bypassSecurityTrustUrl(objectURL);
        },
            error => {
                this.image = null;
            }
        );
    }

    ngOnInit() {
        this.creerCompteForm = this.formBuilder.group({
            nom: ['', Validators.required],
            prenom: ['', Validators.required],
            email: ['', Validators.required],
            mdp: ['', Validators.required],
            mdpVerif: ['', Validators.required],
            // codeTiers: [''],
        });

        this.returnUrl = /*this.route.snapshot.queryParams['returnUrl'] ||*/ '/login';

        this.appSettings.initTenantFromUrl();
        console.log(this.appSettings.BDG_TENANT_DOSSIER);
    }

    public actAnnuler(event) {
        this.router.navigate([this.returnUrl]);
    }

    //    public checkPasswords(group: FormGroup) { // here we have the 'passwords' group
    //        let pass = group.get('mdp').value;
    //        let confirmPass = group.get('mdpVerif').value;
    //
    //        return pass === confirmPass ? null : { notSame: true }     
    //    }

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
            null,//this.creerCompteForm.value.codeTiers,
            this.creerCompteForm.value.nom,
            this.creerCompteForm.value.prenom,
            this.creerCompteForm.value.email,
            this.creerCompteForm.value.mdp,
            this.creerCompteForm.value.mdpVerif,
            true,
            "",
            "", "", ""

            //                this.f.codeTiers.value,
            //                this.f.nom.value,
            //                this.f.prenom.value,
            //                this.f.email.value,
            //                this.f.mdp.value,
            //                true,
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