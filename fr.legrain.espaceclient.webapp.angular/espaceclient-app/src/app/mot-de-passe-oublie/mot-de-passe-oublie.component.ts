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
  selector: 'app-mot-de-passe-oublie',
  templateUrl: './mot-de-passe-oublie.component.html',
  styleUrls: ['./mot-de-passe-oublie.component.css']
})
export class MotDePasseOublieComponent implements OnInit {

  mdpOublieForm: FormGroup;
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
    this.mdpOublieForm = this.formBuilder.group({
      email: ['', Validators.required],
    });

    this.returnUrl = /*this.route.snapshot.queryParams['returnUrl'] ||*/ '/login';

    this.appSettings.initTenantFromUrl();
    console.log(this.appSettings.BDG_TENANT_DOSSIER);
  }

  public actAnnuler(event) {
    this.router.navigate([this.returnUrl]);
  }

  //convenience getter for easy access to form fields
  get f() { return this.mdpOublieForm.controls; }

  onSubmit() {
    this.submitted = true;

    // stop here if form is invalid
    if (this.mdpOublieForm.invalid) {
      return;
    }

    this.loading = true;
    var compte: CompteEspaceClient = new CompteEspaceClient(
      this.appSettings.BDG_ID_ESPACE_CLIENT,this.appSettings.HOST_NAME, this.appSettings.HOST_PORT, "a finir", "a finir", "a finir", this.mdpOublieForm.value.email, "a finir", "a finir", true, "a finir", "", "", ""
    );
    this.espaceClientService.demandeMdpCompteEspaceClient(compte)
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