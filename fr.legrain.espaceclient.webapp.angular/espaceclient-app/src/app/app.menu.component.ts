import { Component, Input, OnInit, AfterViewInit, ViewChild } from '@angular/core';
import { trigger, state, style, transition, animate } from '@angular/animations';
import { MenuItem } from 'primeng/api';
//import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { AProposComponent } from './a-propos/a-propos.component';
import { DynamicDialogModule } from 'primeng/dynamicdialog';
import { DialogService } from 'primeng/dynamicdialog';

import { EspaceClientService } from './services/espace-client.service';
import { AuthenticationService } from './services/authentication.service';
import { ParametreEspaceClient } from './model/parametreEspaceClient';
import { AppSettings } from './env';
import { Tiers } from './model/tiers';

@Component({
    selector: 'app-menu',
    template: `
    <ul class="layout-menu">
        <li app-menuitem *ngFor="let item of (app.grouped ? modelGrouped : modelUngrouped); let i = index;"
            [item]="item" [index]="i" [visible]="true" [root]="true"></li>
    </ul>
    `,
    providers: [DialogService]
})
export class AppMenuComponent implements OnInit, AfterViewInit {

    modelGrouped: any[];

    modelUngrouped: any[];

    public modeLegrain: boolean;
    public modeBoutique: boolean = false;
    public listeTiersCompteEspaceClient: Tiers[];

    constructor(public app: HomeComponent, private authenticationService: AuthenticationService, private espaceClientService: EspaceClientService, private appSettings: AppSettings, public dialogService: DialogService, ) { }

    private getMenuItem(array: any, label: string): any {
        return array.find(item => item.label === label);
    }

    public showHideMenuItems(param: ParametreEspaceClient): void {
        const topLevelAccueil = this.getMenuItem(this.modelGrouped, 'Accueil');
        //        this.getMenuItem(topLevelAccueil.items, 'Téléchargement').visible = this.modeLegrain;
        if(this.listeTiersCompteEspaceClient!=null)
        this.getMenuItem(topLevelAccueil.items, 'Changer de tiers').visible = this.listeTiersCompteEspaceClient.length>=2;


        const topLevel = this.getMenuItem(this.modelGrouped, 'Mes documents');

        this.getMenuItem(topLevel.items, 'Mes factures').visible = param.afficheFactures;
        this.getMenuItem(topLevel.items, 'Mes devis').visible = param.afficheDevis;
        this.getMenuItem(topLevel.items, 'Mes commandes').visible = param.afficheCommandes;
        this.getMenuItem(topLevel.items, 'Mes livraisons').visible = param.afficheLivraisons;
        //this.getMenuItem(topLevel.items, 'Avis d\'échéances').visible = param.afficheAvisEcheance;
        this.getMenuItem(topLevel.items, 'Mes échéances').visible = this.modeLegrain;
        this.getMenuItem(topLevel.items, 'Mes services').visible = this.modeLegrain;

        //        const topLevelParametres = this.getMenuItem(this.modelGrouped, 'Personnalisation');
        //        topLevelParametres.visible = false;

        var topLevelBoutique = this.getMenuItem(this.modelGrouped, 'Boutique');
        if(param.afficheBoutique===false)
            topLevelBoutique.label = '';

        if(this.modeBoutique===true) {
            this.getMenuItem(topLevelBoutique.items, 'Boutique').visible = true;
            this.getMenuItem(topLevelBoutique.items, 'Catalogue').visible = false;
        } else {
            this.getMenuItem(topLevelBoutique.items, 'Catalogue').visible = param.afficheCatalogue;
            this.getMenuItem(topLevelBoutique.items, 'Boutique').visible = false;
        }
    }

    handleClickAPropos() {
        const ref = this.dialogService.open(AProposComponent, {
            header: 'A Propos',
            width: '70%',
            //data : fact
            data: ''
        });
        ref.onClose.subscribe(() => {
            //this.ngOnInit(); //refresh
        });
    }

    ngOnInit() {
        this.espaceClientService.parametres().subscribe(
            data => {
                this.appSettings.PARAMETRES = data;
                this.modeLegrain = this.appSettings.modeLegrain;
                this.modeBoutique = this.appSettings.PARAMETRES.activePanier;
                this.showHideMenuItems(this.appSettings.PARAMETRES);
                this.changeTheme(this.appSettings.PARAMETRES.themeDefaut,"dark");
            }
        );

        this.authenticationService.listeTiers().subscribe(data => {
            if(data!==null)
                this.listeTiersCompteEspaceClient = data;
            else
                this.listeTiersCompteEspaceClient = [];
          });

        this.modelGrouped = [
            {
                label: 'Accueil', icon: 'pi pi-fw pi-home',
                items: [
                    { label: 'Tableau de bord', icon: 'pi pi-fw pi-home', routerLink: ['/dashboard'] },
                    { label: 'Changer de tiers', icon: 'pi pi-fw pi-users', routerLink: ['/choix-compte-tiers'] },
                    { label: 'Mes informations', icon: 'pi pi-fw pi-user', routerLink: ['/mes-informations'] },

                    //                  {label: 'Téléchargement', icon: 'pi pi-fw pi-cloud-download', routerLink: ['/telechargement']},
                ]
            },
            //            {
            //                label: 'Fournisseurs', icon: 'pi pi-fw pi-star',
            //                items: [
            //                    { label: 'Ajouter une liaison', icon: 'pi pi-fw pi-th-large', routerLink: ['/liste-fournisseurs']  },
            //                    { label: 'Mes fournisseurs', icon: 'pi pi-fw pi-clone', routerLink: ['/mes-fournisseurs'] }
            //                ]
            //            },
            {
                label: 'Mes documents', icon: 'pi pi-fw pi-copy',
                items: [
                    { label: 'Devis', icon: 'pi pi-fw pi-file-o', routerLink: ['/mes-devis'] },
                    { label: 'Factures', icon: 'pi pi-fw pi-file-o', routerLink: ['/mes-factures'] },
                    { label: 'Commandes', icon: 'pi pi-fw pi-file-o', routerLink: ['/mes-commandes'], },
                    { label: 'Livraisons', icon: 'pi pi-fw pi-file-o', routerLink: ['/mes-livraisons'] },
                   // { label: "Avis d'échéances", icon: 'pi pi-fw pi-file-o', routerLink: ['/mes-avis-echeance'] },
                    { label: 'Mes échéances', icon: 'pi pi-fw pi-cloud', routerLink: ['/mes-abonnements'] },
                    { label: 'Mes services', icon: 'pi pi-fw pi-cloud-download', routerLink: ['/telechargement'] },
                ]
            },
            {
                label: 'Boutique', icon: 'pi pi-shopping-cart',
                items: [
                    { label: 'Catalogue', icon: 'pi pi-list', routerLink: ['/catalogue'] },
                    { label: 'Boutique', icon: 'pi pi-shopping-cart', routerLink: ['/catalogue'] },
                ]
            },
           
            {
                label: 'Aide', icon: 'pi pi-fw pi-home',
                items: [
                    { label: 'A Propos', icon: 'pi pi-fw pi-info-circle', command: (onclick) => { this.handleClickAPropos() } },
                ]
            },
        ];

        this.modelUngrouped = [
            {
                label: 'Menu principal',
                icon: 'pi pi-fw pi-home',
                items: this.modelGrouped
            }
        ];
    }

    ngAfterViewInit() {
        //setTimeout(() => { this.app.layoutMenuScrollerViewChild.moveBar(); }, 100);
        
    }

    changeTheme(theme: String, scheme: string) {
        const layoutLink: HTMLLinkElement = document.getElementById('layout-css') as HTMLLinkElement;
        layoutLink.href = 'assets/layout/css/layout-' + theme + '.css';

        const themeLink: HTMLLinkElement = document.getElementById('theme-css') as HTMLLinkElement;
        //themeLink.href = 'assets/theme/' + theme + '/theme-' + scheme + '.css';
        themeLink.href = 'assets/theme/theme-' + theme + '.css';

        const topbarLogo: HTMLImageElement = document.getElementById('layout-topbar-logo') as HTMLImageElement;

        const menuLogo: HTMLImageElement = document.getElementById('layout-menu-logo') as HTMLImageElement;
/*
        if (theme === 'yellow' || theme === 'lime') {
            topbarLogo.src = 'assets/layout/images/logo-black.png';
            menuLogo.src = 'assets/layout/images/logo-black.png';
        } else {
            //            topbarLogo.src = 'assets/layout/images/logo-white.png';
            //            menuLogo.src = 'assets/layout/images/logo-white.png';
            
            //topbarLogo.src = 'assets/layout/images/logo-bdg-esp-client.png';
            //menuLogo.src = 'assets/layout/images/logo-bdg-esp-client.png';

            topbarLogo.src = 'assets/images/bdg-espace-client.svg';
            menuLogo.src = 'assets/images/bdg-espace-client.svg';
        }
        */
        if (scheme === 'dark') {
            this.app.darkMenu = true;
        } else if (scheme === 'light') {
            this.app.darkMenu = false;
        }
    }
}





