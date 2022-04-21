import { Routes, RouterModule } from '@angular/router';
import { ModuleWithProviders } from '@angular/core';

import { DashboardComponent } from './dashboard/dashboard.component';
import { ListeFournisseursComponent } from './liste-fournisseurs/liste-fournisseurs.component';
import { MesFournisseursComponent } from './mes-fournisseurs/mes-fournisseurs.component';
import { MesFacturesComponent } from './mes-factures/mes-factures.component';
import {MesDevisComponent } from './mes-devis/mes-devis.component';
import {MesAvisEcheanceComponent } from './mes-avis-echeance/mes-avis-echeance.component';
import { MesLivraisonsComponent } from './mes-livraisons/mes-livraisons.component';
import {MesCommandesComponent } from './mes-commandes/mes-commandes.component';
import { MesInformationsComponent } from './mes-informations/mes-informations.component';
import { MesAbonnementsComponent } from './mes-abonnements/mes-abonnements.component';
import { HomeComponent } from './home/home.component';
import {TelechargementComponent } from './telechargement/telechargement.component';

import { AuthGuard } from './guards/auth.guard';
import { LoginComponent } from './login/login.component';

import { CreerCompteComponent } from './creer-compte/creer-compte.component';
import { ValidationCreationCompteComponent } from './validation-creation-compte/validation-creation-compte.component';
import { MotDePasseOublieComponent } from './mot-de-passe-oublie/mot-de-passe-oublie.component';
import { ValidationModifMdpComponent } from './validation-modif-mdp/validation-modif-mdp.component';
import { ChoixCompteTiersComponent } from './choix-compte-tiers/choix-compte-tiers.component';
import { CatalogueComponent } from './catalogue/catalogue.component';
import { FicheProduitComponent } from './fiche-produit/fiche-produit.component';
import { PanierComponent } from './panier/panier.component';
import { ValidationPanierComponent } from './validation-panier/validation-panier.component';
import { ConfirmationPaiementPanierComponent } from './confirmation-paiement-panier/confirmation-paiement-panier.component';


export const routes: Routes = [
  //  { path: '', component: DashboardComponent, canActivate: [AuthGuard]  },
    { path: '', component: HomeComponent, canActivate: [AuthGuard] , children: [
        { path:'', redirectTo: 'dashboard', pathMatch: 'full' },   
        { path: 'dashboard', component: DashboardComponent },
        { path: 'choix-compte-tiers', component: ChoixCompteTiersComponent },
        { path: 'liste-fournisseurs', component: ListeFournisseursComponent },
        { path: 'mes-fournisseurs', component: MesFournisseursComponent },
        { path: 'mes-factures', component: MesFacturesComponent },
        { path: 'mes-devis', component: MesDevisComponent },
        { path: 'mes-livraisons', component: MesLivraisonsComponent },
        { path: 'mes-commandes', component: MesCommandesComponent },
        { path: 'mes-avis-echeance', component: MesAvisEcheanceComponent },
        { path: 'mes-informations', component: MesInformationsComponent },
        { path: 'mes-abonnements', component: MesAbonnementsComponent },
        { path: 'telechargement', component: TelechargementComponent },

        { path: 'catalogue', component: CatalogueComponent },
        { path: 'fiche-produit/:idArticle', component: FicheProduitComponent },
        { path: 'panier', component: PanierComponent },
        { path: 'validation-panier', component: ValidationPanierComponent },
        { path: 'confirmation-paiement-panier', component: ConfirmationPaiementPanierComponent },
        
      ]},
//    { path: 'liste-fournisseurs', component: ListeFournisseursComponent },
//    { path: 'mes-fournisseurs', component: MesFournisseursComponent },
//    { path: 'mes-documents', component: MesDocumentsComponent },
//    { path: 'mes-informations', component: MesInformationsComponent },
    
//    { path: 'overlays', component: OverlaysDemoComponent },
//    { path: 'menus', component: MenusDemoComponent },
//    { path: 'messages', component: MessagesDemoComponent },
//    { path: 'misc', component: MiscDemoComponent },
//    { path: 'empty', component: EmptyDemoComponent },
//    { path: 'charts', component: ChartsDemoComponent },
//    { path: 'file', component: FileDemoComponent },
//    { path: 'documentation', component: DocumentationComponent },
    
    { path: 'login', component: LoginComponent},
    
    { path: 'creer-compte', component: CreerCompteComponent},
    { path: 'mot-de-passe-oublie', component: MotDePasseOublieComponent},
    { path: 'validation-creation-compte', component: ValidationCreationCompteComponent},
    { path: 'validation-modif-mdp', component: ValidationModifMdpComponent},

    { path: '**', redirectTo: '' }
];

//export const routes: Routes = [
//                        { path: '', component: DashboardDemoComponent, canActivate: [AuthGuard] },
//                        { path: 'login', component: LoginComponent },
//
//                        // otherwise redirect to home
//                        { path: '**', redirectTo: '' }
//                    ];

export const AppRoutes: ModuleWithProviders<RouterModule> = RouterModule.forRoot(routes, {scrollPositionRestoration: 'enabled'});
