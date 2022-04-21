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
  selector: 'app-validation-modif-mdp',
  templateUrl: './validation-modif-mdp.component.html',
  styleUrls: ['./validation-modif-mdp.component.css']
})
export class ValidationModifMdpComponent implements OnInit {

  modifierMdpForm: FormGroup;
  loading = false;
  submitted = false;
  returnUrl: string;
  error = '';
  lecompte: CompteEspaceClient;
  adresseEmail: string;
  image: any = null;

  public saisie = true;
  public mdp_modifie = false;
  public mdp_non_modifie = false;

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

    this.appSettings.initTenantFromUrl();
    console.log(this.appSettings.BDG_TENANT_DOSSIER);

    this.modifierMdpForm = this.formBuilder.group({
      //            nom: ['', Validators.required],
      //            prenom: ['', Validators.required],
      //            email: ['', Validators.required],
      mdp: ['', Validators.required],
      mdpVerif: ['', Validators.required],
      //            codeTiers: ['', Validators.required],
    });

    this.returnUrl = /*this.route.snapshot.queryParams['returnUrl'] ||*/ '/login';

    //récupération de la chaine crypté contenant les données du nouveau compte à créer

    const infoCompteCryptees: string = this.route.snapshot.queryParamMap.get('p');

    //envoi de cette chaine cryptée au serveur pour création du compte

    var compte: CompteEspaceClient = new CompteEspaceClient(
      this.appSettings.BDG_ID_ESPACE_CLIENT,this.appSettings.HOST_NAME, this.appSettings.HOST_PORT, "a finir", "a finir", "a finir", "a finir", "a finir", "a finir", true, infoCompteCryptees, "", "", ""
    );

    //envoi de cette chaine cryptée au serveur pour création du compte

    this.espaceClientService.decrypte(compte)
      .pipe(first())
      .subscribe(
        data => {
          //  this.router.navigate([this.returnUrl]);
          this.lecompte = data;
          this.adresseEmail = data.email
          //alert('OK'+data);
        },
        error => {
          //                    this.error = error;
          //                    this.loading = false;
          alert('ERREUR');
        });

    //Erreur => message d'erreur
    //OK => Message de confirmation, redirection vers le login ou login automatique
  }

  public actAnnuler(event) {
    this.router.navigate([this.returnUrl]);
  }

  //convenience getter for easy access to form fields
  get f() { return this.modifierMdpForm.controls; }

  onSubmit() {
    this.submitted = true;

    // stop here if form is invalid
    if (this.modifierMdpForm.invalid) {
      return;
    }

    this.loading = true;
    var compte: CompteEspaceClient = new CompteEspaceClient(
      this.appSettings.BDG_ID_ESPACE_CLIENT,
      this.appSettings.HOST_NAME,
      this.appSettings.HOST_PORT,
      this.lecompte.codeTiers,
      this.lecompte.nom,
      this.lecompte.prenom,
      // this.modifierMdpForm.value.email,
      this.lecompte.email,
      this.modifierMdpForm.value.mdp,
      this.modifierMdpForm.value.mdpVerif,
      true,
      this.route.snapshot.queryParamMap.get('p'),
      "", "", ""
    );
    //this.authenticationService.creerCompteEspaceClient(compte)
    this.espaceClientService.confirmeMdpCompteEspaceClient(compte)
      .pipe(first())
      .subscribe(
        data => {
          //  this.router.navigate([this.returnUrl]);
          this.saisie = false;
          this.mdp_modifie = true;
          //alert('OK');
        },
        error => {
          this.error = error;
          this.loading = false;
          this.saisie = false;
          this.mdp_non_modifie = true;
          //alert('PAS OK');
        });
  }

}
