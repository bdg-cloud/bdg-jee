import { Component, OnInit } from '@angular/core';
import { AppSettings } from '../env';
import { EspaceClientService } from '../services/espace-client.service';
import { AuthenticationService } from '../services/authentication.service';
import { ParametreEspaceClient, ParametreEspaceClientAdapter } from '../model/parametreEspaceClient';

import { Publicite, PubliciteAdapter } from '../model/publicite';

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

    public publicites: Publicite[];
    public affichePublicite: boolean;

    public afficheDevis: boolean;
    public afficheFactures: boolean;
    public afficheCommandes: boolean;
    public afficheLivraisons: boolean;
    public afficheAvisEcheance: boolean;

    public afficheCatalogue: boolean;
    public afficheBoutique: boolean;
    public afficheBoutiquePanier: boolean;

    constructor(private authenticationService: AuthenticationService, private espaceClientService: EspaceClientService, private appSettings: AppSettings) { }

    ngOnInit() {
        if (this.appSettings.PARAMETRES == null) {
            this.espaceClientService.parametres().subscribe(

                data => {
                    this.appSettings.PARAMETRES = data;
                    this.initVisible(this.appSettings.PARAMETRES);
                }

            )
                ;
        } else {
            this.initVisible(this.appSettings.PARAMETRES);
        }


    }

    initPublicite() {
        this.affichePublicite = this.appSettings.modeLegrain && this.appSettings.PARAMETRES.affichePublicite;
        var a = new Publicite(1, "BDG Cloud Standard", "assets/images/publicite_lgr/1-bdg-cloud-standard.png", null, null);
        var b = new Publicite(2, "BDG Cloud Viti", "assets/images/publicite_lgr/2-bdg-cloud-viti.png", null, null);
        var c = new Publicite(3, "BDG Cloud Conserverie", "assets/images/publicite_lgr/3-bdg-cloud-conserverie.png", null, null);
        var d = new Publicite(4, "Epicea", "assets/images/publicite_lgr/4-epicea.png", null, null);
        var e = new Publicite(5, "Site Vitrine", "assets/images/publicite_lgr/5-site-vitrine.png", null, null);
        var f = new Publicite(6, "Site e-commerce", "assets/images/publicite_lgr/6-site-e-commerce.png", null, null);
        var g = new Publicite(7, "Sauvegarde", "assets/images/publicite_lgr/7-sauvegarde.png", null, null);
        var h = new Publicite(8, "Anti virus", "assets/images/publicite_lgr/8-anti-virus.png", null, null);


        this.publicites = [a, b, c, d, e, f, g, h];
        //      this.publicites = [a,b,c];
    }

    public mailPublicite(p: Publicite) {
        this.espaceClientService.emailPublcite(p).subscribe(

            data => {
                alert("Votre demande bien a été prise en compte, nous vous contacterons prochainement.");
            }

        );
    }

    public initVisible(set: ParametreEspaceClient) {
        this.afficheDevis = false;
        this.afficheFactures = false;
        this.afficheCommandes = false;
        this.afficheLivraisons = false;
        this.afficheAvisEcheance = false;

        this.afficheDevis = this.appSettings.PARAMETRES.afficheDevis;
        this.afficheFactures = this.appSettings.PARAMETRES.afficheFactures;
        this.afficheCommandes = this.appSettings.PARAMETRES.afficheCommandes;
        this.afficheLivraisons = this.appSettings.PARAMETRES.afficheLivraisons;
        this.afficheAvisEcheance = this.appSettings.PARAMETRES.afficheAvisEcheance;

        this.afficheCatalogue = this.appSettings.PARAMETRES.afficheCatalogue;
        this.afficheBoutique = this.appSettings.PARAMETRES.afficheBoutique;
        this.afficheBoutiquePanier = this.appSettings.PARAMETRES.activePanier;
        

        this.initPublicite();

    }

}
