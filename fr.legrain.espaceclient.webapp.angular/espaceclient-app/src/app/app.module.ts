import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
//import { HttpClientModule } from '@angular/common/http';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LocationStrategy, HashLocationStrategy } from '@angular/common';
import { AppRoutes } from './app.routes';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AccordionModule } from 'primeng/accordion';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { BreadcrumbModule } from 'primeng/breadcrumb';
import { ButtonModule } from 'primeng/button';
import { CalendarModule } from 'primeng/calendar';
import { CardModule } from 'primeng/card';
import { CarouselModule } from 'primeng/carousel';
import { ChartModule } from 'primeng/chart';
import { CheckboxModule } from 'primeng/checkbox';
import { ChipsModule } from 'primeng/chips';
import { CodeHighlighterModule } from 'primeng/codehighlighter';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ColorPickerModule } from 'primeng/colorpicker';
import { ContextMenuModule } from 'primeng/contextmenu';
import { DataViewModule } from 'primeng/dataview';
import { DialogModule } from 'primeng/dialog';
import { DropdownModule } from 'primeng/dropdown';
import { DynamicDialogModule} from 'primeng/dynamicdialog';
import { EditorModule } from 'primeng/editor';
import { FieldsetModule } from 'primeng/fieldset';
import { FileUploadModule } from 'primeng/fileupload';
import { FullCalendarModule } from 'primeng/fullcalendar';
import { GalleriaModule } from 'primeng/galleria';
import { ToastModule } from 'primeng/toast';
import { InplaceModule } from 'primeng/inplace';
import { InputMaskModule } from 'primeng/inputmask';
import { InputSwitchModule } from 'primeng/inputswitch';
import { InputTextModule } from 'primeng/inputtext';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { InputNumberModule } from 'primeng/inputnumber';
import { LightboxModule } from 'primeng/lightbox';
import { ListboxModule } from 'primeng/listbox';
import { MegaMenuModule } from 'primeng/megamenu';
import { MenuModule } from 'primeng/menu';
import { MenubarModule } from 'primeng/menubar';
import { MessagesModule } from 'primeng/messages';
import { MessageModule } from 'primeng/message';
import { MultiSelectModule } from 'primeng/multiselect';
import { OrderListModule } from 'primeng/orderlist';
import { OrganizationChartModule } from 'primeng/organizationchart';
import { OverlayPanelModule } from 'primeng/overlaypanel';
import { PaginatorModule } from 'primeng/paginator';
import { PanelModule } from 'primeng/panel';
import { PanelMenuModule } from 'primeng/panelmenu';
import { PasswordModule } from 'primeng/password';
import { PickListModule } from 'primeng/picklist';
import { ProgressBarModule } from 'primeng/progressbar';
import { RadioButtonModule } from 'primeng/radiobutton';
import { RatingModule } from 'primeng/rating';
import { ScrollPanelModule } from 'primeng/scrollpanel';
import { SelectButtonModule } from 'primeng/selectbutton';
import { SlideMenuModule } from 'primeng/slidemenu';
import { SliderModule } from 'primeng/slider';
import { SpinnerModule } from 'primeng/spinner';
import { SplitButtonModule } from 'primeng/splitbutton';
import { StepsModule } from 'primeng/steps';
import { TabMenuModule } from 'primeng/tabmenu';
import { TableModule } from 'primeng/table';
import { TabViewModule } from 'primeng/tabview';
import { TerminalModule } from 'primeng/terminal';
import { TieredMenuModule } from 'primeng/tieredmenu';
import { ToggleButtonModule } from 'primeng/togglebutton';
import { ToolbarModule } from 'primeng/toolbar';
import { TooltipModule } from 'primeng/tooltip';
import { TreeModule } from 'primeng/tree';
import { TreeTableModule } from 'primeng/treetable';
import { VirtualScrollerModule } from 'primeng/virtualscroller';
import { ProgressSpinnerModule} from 'primeng/progressspinner';
import { DividerModule} from 'primeng/divider';

import { AppComponent } from './app.component';
import { AppProfileComponent } from './app.profile.component';
import { AppMenuComponent } from './app.menu.component';
import { AppBreadcrumbComponent } from './app.breadcrumb.component';
import { AppTopBarComponent } from './app.topbar.component';
import { AppFooterComponent } from './app.footer.component';
import {AppConfigComponent} from './app.config.component';
import {AppMenuitemComponent} from './app.menuitem.component';


import { LoginComponent } from './login/login.component';

import { JwtInterceptor } from './services/interceptors/jwt.service';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ListeFournisseursComponent } from './liste-fournisseurs/liste-fournisseurs.component';
import { MesFournisseursComponent } from './mes-fournisseurs/mes-fournisseurs.component';
import { MesFacturesComponent } from './mes-factures/mes-factures.component';
import { MesInformationsComponent } from './mes-informations/mes-informations.component';
import { PaiementCBComponent } from './paiement-cb/paiement-cb.component';
import { HomeComponent } from './home/home.component';
import { DatePipe} from '@angular/common';
import { CreerCompteComponent } from './creer-compte/creer-compte.component';
import { MotDePasseOublieComponent } from './mot-de-passe-oublie/mot-de-passe-oublie.component';
import { ValidationCreationCompteComponent } from './validation-creation-compte/validation-creation-compte.component';
import { ValidationModifMdpComponent } from './validation-modif-mdp/validation-modif-mdp.component';

import { registerLocaleData } from '@angular/common';
import localeFr from '@angular/common/locales/fr';
import { MesDevisComponent } from './mes-devis/mes-devis.component';
import { MesCommandesComponent } from './mes-commandes/mes-commandes.component';
import { MesLivraisonsComponent } from './mes-livraisons/mes-livraisons.component';
import { MesAvisEcheanceComponent } from './mes-avis-echeance/mes-avis-echeance.component';
//import localeFrExtra from '@angular/common/locales/extra/fr';

import { AppSettings } from './env';
import { TelechargementComponent } from './telechargement/telechargement.component';
import { MesAbonnementsComponent } from './mes-abonnements/mes-abonnements.component';
import { AProposComponent } from './a-propos/a-propos.component';
import { ChoixCompteTiersComponent } from './choix-compte-tiers/choix-compte-tiers.component';
import { CatalogueComponent } from './catalogue/catalogue.component';
import { ArticleImageDefautComponent } from './article-image-defaut/article-image-defaut.component';
import { FicheProduitComponent } from './fiche-produit/fiche-produit.component';
import { CategorieImageDefautComponent } from './categorie-image-defaut/categorie-image-defaut.component';

import {BreadcrumbService} from './app.breadcrumb.service';
import {MenuService} from './app.menu.service';
import { PanierComponent } from './panier/panier.component';
import { ValidationPanierComponent } from './validation-panier/validation-panier.component';
import { CGVComponent } from './cgv/cgv.component';
import { DemandeRenseignementComponent } from './demande-renseignement/demande-renseignement.component';
import { ConfirmationPaiementPanierComponent } from './confirmation-paiement-panier/confirmation-paiement-panier.component';

registerLocaleData(localeFr, 'fr');
//registerLocaleData(localeFr, 'fr-FR', localeFrExtra);

@NgModule({
    imports: [
        BrowserModule,
        FormsModule,
        AppRoutes,
        HttpClientModule,
        BrowserAnimationsModule,
        AccordionModule,
        AutoCompleteModule,
        BreadcrumbModule,
        ButtonModule,
        CalendarModule,
        CardModule,
        CarouselModule,
        ChartModule,
        CheckboxModule,
        ChipsModule,
        CodeHighlighterModule,
        ConfirmDialogModule,
        ColorPickerModule,
        ContextMenuModule,
        DataViewModule,
        DialogModule,
        DropdownModule,
        DynamicDialogModule,
        EditorModule,
        FieldsetModule,
        FileUploadModule,
        FullCalendarModule,
        GalleriaModule,
        ToastModule,
        InplaceModule,
        InputMaskModule,
        InputSwitchModule,
        InputTextModule,
        InputTextareaModule,
        InputNumberModule,
        LightboxModule,
        ListboxModule,
        MegaMenuModule,
        MenuModule,
        MenubarModule,
        MessageModule,
        MessagesModule,
        MultiSelectModule,
        OrderListModule,
        OrganizationChartModule,
        OverlayPanelModule,
        PaginatorModule,
        PanelModule,
        PanelMenuModule,
        PasswordModule,
        PickListModule,
        ProgressBarModule,
        RadioButtonModule,
        RatingModule,
        ScrollPanelModule,
        SelectButtonModule,
        SlideMenuModule,
        SliderModule,
        SpinnerModule,
        SplitButtonModule,
        StepsModule,
        TableModule,
        TabMenuModule,
        TabViewModule,
        TerminalModule,
        TieredMenuModule,
        ToastModule,
        ToggleButtonModule,
        ToolbarModule,
        TooltipModule,
        TreeModule,
        TreeTableModule,
        VirtualScrollerModule,
        ProgressSpinnerModule,
        DividerModule,
        
        ReactiveFormsModule
    ],
    declarations: [
        AppComponent,
        AppMenuComponent,
        AppMenuitemComponent,
        AppProfileComponent,
        AppBreadcrumbComponent,
        AppConfigComponent,
        AppTopBarComponent,
        AppFooterComponent,
        LoginComponent,
        DashboardComponent,
        ListeFournisseursComponent,
        MesFournisseursComponent,
        MesFacturesComponent,
        MesInformationsComponent,
        PaiementCBComponent,
        HomeComponent,
        CreerCompteComponent,
        MotDePasseOublieComponent,
        ValidationCreationCompteComponent,
        ValidationModifMdpComponent,
        MesDevisComponent,
        MesCommandesComponent,
        MesLivraisonsComponent,
        MesAvisEcheanceComponent,
        TelechargementComponent,
        MesAbonnementsComponent,
        AProposComponent,
        ChoixCompteTiersComponent,
        CatalogueComponent,
        ArticleImageDefautComponent,
        FicheProduitComponent,
        CategorieImageDefautComponent,
        PanierComponent,
        ValidationPanierComponent,
        CGVComponent,
        DemandeRenseignementComponent,
        ConfirmationPaiementPanierComponent,
    ],
    providers: [
        { provide: LocationStrategy, useClass: HashLocationStrategy },
        { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
        BreadcrumbService, DatePipe, MenuService, BreadcrumbService,
        AppSettings
    ],
    entryComponents: [
        PaiementCBComponent, AProposComponent
    ],
    bootstrap: [AppComponent]
})
export class AppModule { }
