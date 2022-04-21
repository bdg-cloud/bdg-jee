import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';

//import { Tiers, TiersAdapter } from '../model/tiers';

import { EspaceClientService } from '../services/espace-client.service';
import { AuthenticationService } from '../services/authentication.service';
import { AppSettings } from '../env';

import { DomSanitizer } from '@angular/platform-browser';

@Component({ templateUrl: 'login.component.html' })
export class LoginComponent implements OnInit {
    loginForm: FormGroup;
    loading = false;
    submitted = false;
    creationDeCompte = false;
    returnUrl: string;
    error = '';
    image: any = null;
    backgroundImage: any = null;
    backgroundStyle: any = null;

    texteAccueil: String;
    texte1: String;
    texte2: String;

    //public listeTiers: Tiers[];

    constructor(
        private formBuilder: FormBuilder,
        private route: ActivatedRoute,
        private router: Router,
        private authenticationService: AuthenticationService,
        private espaceClientService: EspaceClientService,
        private sanitizer: DomSanitizer,
        private appSettings: AppSettings
    ) {
        // redirect to home if already logged in
        if (this.authenticationService.currentUserValue) {
            this.router.navigate(['/']);
        }
    }

    ngOnInit() {
        this.loginForm = this.formBuilder.group({
            username: ['', Validators.required],
            password: ['', Validators.required]
        });

        // get return url from route parameters or default to '/'
        this.returnUrl = /*this.route.snapshot.queryParams['returnUrl'] ||*/ '/';


        this.appSettings.initTenantFromUrl();
        this.espaceClientService.logo().subscribe((blob: any) => {
            let objectURL = URL.createObjectURL(blob);
            this.image = this.sanitizer.bypassSecurityTrustUrl(objectURL);
        },
            error => {
                this.image = null;
            }
        );

        this.espaceClientService.backgroundLogin().subscribe((blob: any) => {
            let objectURL = URL.createObjectURL(blob);
            this.backgroundImage = this.sanitizer.bypassSecurityTrustStyle('url(' + objectURL + ')');
            this.backgroundStyle = {
                background: 'url(' + objectURL + ')',
                backgroundSize: 'cover',
            }
        },
            error => {
                this.backgroundImage = null;
                this.backgroundStyle = null;
            }
        );

        if (this.appSettings.PARAMETRES == null) {
            this.espaceClientService.parametres().subscribe(
                data => {
                    this.appSettings.PARAMETRES = data;
                    this.texteAccueil = this.appSettings.PARAMETRES.texteAccueil;
                    this.texte1 = this.appSettings.PARAMETRES.texteLogin1;
                    this.texte2 = this.appSettings.PARAMETRES.texteLogin2;
                    this.creationDeCompte = this.appSettings.PARAMETRES.utilisateurPeuCreerCompte;
                    if (this.appSettings.PARAMETRES.themeDefaut) {
                        //this.changeTheme(this.appSettings.PARAMETRES.themeDefaut.split("-")[0], this.appSettings.PARAMETRES.themeDefaut.split("-")[1]);
                        this.changeTheme(this.appSettings.PARAMETRES.themeDefaut, "light");
                    }
                }
            );
        }

    }


    changeTheme(theme: String, scheme: string) {
        const layoutLink: HTMLLinkElement = document.getElementById('layout-css') as HTMLLinkElement;
        layoutLink.href = 'assets/layout/css/layout-' + theme + '.css';

        const themeLink: HTMLLinkElement = document.getElementById('theme-css') as HTMLLinkElement;
        //themeLink.href = 'assets/theme/' + theme + '/theme-' + scheme + '.css';
        themeLink.href = 'assets/theme/theme-' + theme + '.css';

        const topbarLogo: HTMLImageElement = document.getElementById('layout-topbar-logo') as HTMLImageElement;

        const menuLogo: HTMLImageElement = document.getElementById('layout-menu-logo') as HTMLImageElement;

        if (theme === 'yellow' || theme === 'lime') {
            topbarLogo.src = 'assets/layout/images/logo-black.png';
            menuLogo.src = 'assets/layout/images/logo-black.png';
        } else {
            //            topbarLogo.src = 'assets/layout/images/logo-white.png';
            //            menuLogo.src = 'assets/layout/images/logo-white.png';

            //            topbarLogo.src = 'assets/layout/images/logo-bdg-esp-client.png';
            //            menuLogo.src = 'assets/layout/images/logo-bdg-esp-client.png';
        }

        //        if (scheme === 'dark') {
        //            this.app.darkMenu = true;
        //        } else if (scheme === 'light') {
        //            this.app.darkMenu = false;
        //        }
    }

    // convenience getter for easy access to form fields
    get f() { return this.loginForm.controls; }

    onSubmit() {
        this.submitted = true;

        // stop here if form is invalid
        if (this.loginForm.invalid) {
            return;
        }

        this.loading = true;
        this.authenticationService.login(this.f.username.value, this.f.password.value)
            .pipe(first())
            .subscribe(
                data => {

         //           this.authenticationService.listeTiers().subscribe(data => {
           //             this.listeTiers = data;
             //           if(this.listeTiers.length==0) {
                            this.router.navigate([this.returnUrl]);
               //         }
                 //     });


                    
                },
                error => {
                    console.log('oops', error)
                    if (error.includes("Email ou mot de passe invalide")) {
                        error = 'Email ou mot de passe invalide';
                    }
                    this.error = error;
                    this.loading = false;
                }
            );
    }
}