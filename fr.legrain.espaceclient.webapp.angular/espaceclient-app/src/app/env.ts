import { HttpHeaders } from '@angular/common/http';
import { ParametreEspaceClient, ParametreEspaceClientAdapter } from './model/parametreEspaceClient';
import { Injectable } from '@angular/core';

@Injectable()
export class AppSettings {

    constructor() {
        var currentUser = JSON.parse(localStorage.getItem('currentUser'));
        if (currentUser && currentUser.codeTiers !== 'XXXXXXXXXXXXXXX') {
            //console.log("Pas de code tiers/client => déconnexion")
            this.CODE_CLIENT_CHEZ_FRS = currentUser.codeTiers;
            //this.logout();
        }

    }

    //   public static getInstance():AppSettings {
    //       return this;
    //   }

    //prog
    public /*static*/ BDG_ID_ESPACE_CLIENT: number = 0;
    public /*static*/ BDG_TOKEN: string = 'aa';
    public /*static*/ BDG_LOGIN: string = '';
    public /*static*/ BDG_PASSWORD: string = '';

    public /*static*/ BDG_TENANT_DOSSIER: string = 'xxxxxxx';

    //passés en parametre aux fonction qui envoi de email pour creer des liens dynamiquement
    public /*static*/ HOST_NAME: string = 'xxxxxxx';
    public /*static*/ HOST_PORT: string = 'xxxxxxx';

    //"Session"
    //public /*static*/ CODE_CLIENT_CHEZ_FRS:string = 'CBONVIVRE';
    public /*static*/ CODE_CLIENT_CHEZ_FRS: string = 'XXXXXXXXXXXXXXX';
    public /*static*/ EXO_DATE_DEBUT: Date = new Date(2017, (1 - 1), 1);
    public /*static*/ EXO_DATE_FIN: Date = new Date(2022, (12 - 1), 31);

    public /*static*/ DATE_FORMAT: string = 'yyyy-MM-dd';

    public /*static*/ PARAMETRES: ParametreEspaceClient = null;

    public modeLegrain: boolean = false;


    //public /*static*/ BASE_API_ENDPOINT='/rest';
    public /*static*/ BASE_API_ENDPOINT = '/v1';
    //public /*static*/ BASE_API_ENDPOINT='https://demo.devbdg.work:443/rest';

    public /*static*/  httpOptionsCTJson = {
        headers: new HttpHeaders(
            {
                //'Accept': 'application/json',
                'Content-Type': 'application/json',
                //                       'Access-Control-Allow-Origin': '*',
                //                       'Dossier': this.appSettings.BDG_TENANT_DOSSIER, 
                //                       'Lgr': this.appSettings.BDG_TOKEN, 
                //                       'Bdg-login': this.appSettings.BDG_LOGIN, 
                //                       'Bdg-password': this.appSettings.BDG_PASSWORD,
                //                       //'No-Auth': 'true',
            })
    };

    public /*static*/ httpOptionsAcceptJson = {

        headers: new HttpHeaders(
            {
                'Accept': 'application/json',
                //                       'Access-Control-Allow-Origin': '*',
                //                       'Dossier': this.appSettings.BDG_TENANT_DOSSIER, 
                //                       'Lgr': this.appSettings.BDG_TOKEN, 
                //                       'Bdg-login': this.appSettings.BDG_LOGIN, 
                //                       'Bdg-password': this.appSettings.BDG_PASSWORD,
            })
    };

    public /*static*/  httpOptionsFormEncoded = {
        headers: new HttpHeaders(
            {
                //'Accept': 'application/json',
                'Content-Type': 'application/x-www-form-urlencoded',
                //                   'Access-Control-Allow-Origin': '*',
                //                   'Dossier': this.appSettings.BDG_TENANT_DOSSIER, 
                //                   'Lgr': this.appSettings.BDG_TOKEN, 
                //                   'Bdg-login': this.appSettings.BDG_LOGIN, 
                //                   'Bdg-password': this.appSettings.BDG_PASSWORD,
                //                   //'No-Auth': 'true',
            })
    };

    public /*static*/ httpOptionsFile = {
        responseType: 'blob' as 'blob',
        headers: new HttpHeaders(
            {
                'Accept': 'application/octet-stream',
                //                       'Access-Control-Allow-Origin': '*',
                //                       'Dossier': this.appSettings.BDG_TENANT_DOSSIER, 
                //                       'Lgr': this.appSettings.BDG_TOKEN, 
                //                       'Bdg-login': this.appSettings.BDG_LOGIN, 
                //                       'Bdg-password': this.appSettings.BDG_PASSWORD,

                // 'Content-Type': 'application/json'
            }),
        //reportProgress: true,
        //observe: "events" 
    };

    public /*static*/ initTenantFromUrl() {
        var hostname = window.location.hostname.split("."); //dev.espace-client.demo.promethee.biz
        //si le hostname n'est pas un domaine bdg (bdg.cloud, pprodbdg.work,..., promethee.biz) c'est a priori un alias d'un client
        //envoyer le dossier/tenant avec "alias:" devant => alias:xxxxxxxx, comme ça la substitution de tenant pourra se faire.
        // var tenant = hostname[hostname.length-3]; //dev.espace-client.demo.promethee.biz
        var tenant = hostname[hostname.length - 4]; //demo.espace-client.promethee.biz //4eme element en partant de la fin du tableau
        var domaine: string = hostname[hostname.length - 2] + "." + hostname[hostname.length - 1];
        if ((domaine !== "bdg.cloud")
            && (domaine !== "devbdg.work")
            && (domaine !== "pprodbdg.work")
            && (domaine !== "testprodbdg.work")
            && (domaine !== "promethee.biz")
        ) {
            tenant = "alias:" + window.location.hostname;
            if ((domaine === "legrain.fr")) {
                //l'alias est un sous domaine de legrain.fr
                this.modeLegrain = true;
            }
        } else {
            this.modeLegrain = true;
        }
        this.HOST_NAME = window.location.hostname;
        this.HOST_PORT = window.location.port;
        this.BDG_TENANT_DOSSIER = tenant;
    }
}