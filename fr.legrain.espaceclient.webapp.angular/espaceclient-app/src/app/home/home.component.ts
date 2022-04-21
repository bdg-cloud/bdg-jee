import { Component, AfterViewInit, ElementRef, Renderer2, ViewChild, OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ScrollPanel } from 'primeng/scrollpanel';
import { Router } from '@angular/router';
import { Inject } from '@angular/core';

import { EspaceClientService } from '../services/espace-client.service';
import { AuthenticationService } from '../services/authentication.service';
import { User } from '../model/user';
import { Tiers, TiersAdapter } from '../model/tiers';

import { AppSettings } from '../env';

import { DomSanitizer } from '@angular/platform-browser';

import {MenuService} from '../app.menu.service';
import {PrimeNGConfig} from 'primeng/api';

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.css']
})
export class HomeComponent implements AfterViewInit, OnInit {

    public afficheLiveChat: boolean;
    public tiers: Tiers;

    currentUser: User;

    layoutMode = 'static';

    darkMenu = true;

    profileMode = 'popup';

    rotateMenuButton: boolean;

    topbarMenuActive: boolean;

    overlayMenuActive: boolean;

    staticMenuDesktopInactive: boolean;

    staticMenuMobileActive: boolean;

    menuClick: boolean;

    topbarItemClick: boolean;

    configClick: boolean;

    activeTopbarItem: any;

    menuHoverActive: boolean;

    grouped = true;

    configActive: boolean;

    inlineMenuActive: boolean;

    inlineMenuClick: boolean;

    inputStyle = 'outlined';

    ripple: boolean;

    image: any = null;

    @ViewChild('layoutMenuScroller', { static: true }) layoutMenuScrollerViewChild: ScrollPanel;

    //    constructor(public renderer: Renderer2) {}

    constructor(
        public renderer: Renderer2,
        private router: Router,
        private authenticationService: AuthenticationService,
        private espaceClientService: EspaceClientService,
        private sanitizer: DomSanitizer,
        private appSettings: AppSettings,
        private menuService: MenuService, private primengConfig: PrimeNGConfig
    ) {
        this.authenticationService.currentUser.subscribe(x => this.currentUser = x);
        //this.currentUser = new User();

        this.afficheLiveChat = this.appSettings.modeLegrain /*&& this.appSettings.PARAMETRES.affichePublicite*/;
    }

    /*
     public codeTiers : string
        public nomEntreprise : string,
        public codeTCivilite : string,
        public nomTiers : string,
        public prenomTiers : string,
        public adresseEmail : string,
        public numeroTelephone : string,
        */

    ngOnInit() {
        this.primengConfig.ripple = true;

        this.espaceClientService.infosTiers().subscribe(data => {
            this.tiers = data;

            const s2 = this.renderer.createElement('script');
            s2.type = 'text/javascript';
            s2.text =
                "function onSIApiReady() {"
                + "SI_API.setChatInfo('" + this.tiers.nomTiers + "','" + this.tiers.adresseEmail + "','" + this.tiers.numeroTelephone + "',null);"

                + "var params = [];"
                + "params.push({name:'Origine rappel',value:'Espace Client BDG'});"
                + "params.push({name:'Code tiers',value:'" + this.tiers.codeTiers + "'});"
                + "params.push({name:'Nom entreprise',value:'" + this.tiers.nomEntreprise + "'});"
                + "SI_API.addParams(params);"
                + "}"
                ;
            ;
            this.renderer.appendChild(document.body, s2);
        });

    }

    logout() {
        this.authenticationService.logout();
        this.router.navigate(['/login']);
    }

    ngAfterViewInit() {
        //setTimeout(() => { this.layoutMenuScrollerViewChild.moveBar(); }, 100);

        this.appSettings.initTenantFromUrl();

        this.espaceClientService.logoHeader().subscribe((blob: any) => {
            let objectURL = URL.createObjectURL(blob);
            this.image = this.sanitizer.bypassSecurityTrustUrl(objectURL);
        },
            error => {
                this.image = null;
            }
        );

        /*
        if (this.afficheLiveChat) {
            const s = this.renderer.createElement('script');
            // s.onload = this.loadNextScript.bind(this);
            s.type = 'text/javascript';
            s.src = 'https://www.socialintents.com/api/chat/socialintents.1.3.js#2c9fa23c70deaf570170f7b3f3c73355'; // Defines someGlobalObject
            s.text = ``;
            this.renderer.appendChild(document.body, s);
        }
        */

        if (this.afficheLiveChat) {
            //https://community.atlassian.com/t5/Jira-Service-Management/Dynamically-Embedding-Service-Desk-Widget/qaq-p/899686
            const s = this.renderer.createElement('script');
            s.type = 'text/javascript';
            this.renderer.setAttribute(s, 'data-jsd-embedded', 'true');
            this.renderer.setAttribute(s, 'data-key', 'cac75c39-7a75-4090-a12d-cef1348f5cd7');
            this.renderer.setAttribute(s, 'data-base-url', "https://jsd-widget.atlassian.com");
            s.src = 'https://jsd-widget.atlassian.com/assets/embed.js'; 
            s.text = `var DOMContentLoaded_event = document.createEvent("Event")
            DOMContentLoaded_event.initEvent("DOMContentLoaded", true, true)
            window.document.dispatchEvent(DOMContentLoaded_event)`;
            this.renderer.appendChild(document.body, s);

            setTimeout(() => {
                var DOMContentLoaded_event = document.createEvent("Event");
                DOMContentLoaded_event.initEvent("DOMContentLoaded", true, true);
                window.document.dispatchEvent(DOMContentLoaded_event);
            },5000);
        }
    }

    onLayoutClick() {
        if (!this.topbarItemClick) {
            this.activeTopbarItem = null;
            this.topbarMenuActive = false;
        }

        if (!this.menuClick || (this.inlineMenuClick && this.isSlim())) {
            if (this.isHorizontal() || this.isSlim()) {
                this.menuService.reset();
            }

            if (this.overlayMenuActive || this.staticMenuMobileActive) {
                this.hideOverlayMenu();
            }

            this.menuHoverActive = false;
        }

        if (this.configActive && !this.configClick) {
            this.configActive = false;
        }

        if (this.inlineMenuActive && !this.inlineMenuClick) {
            this.inlineMenuActive = false;
        }

        this.inlineMenuClick = false;
        this.configClick = false;
        this.topbarItemClick = false;
        this.menuClick = false;
    }

    onMenuButtonClick(event) {
        this.menuClick = true;
        this.rotateMenuButton = !this.rotateMenuButton;
        this.topbarMenuActive = false;

        if (this.layoutMode === 'overlay' && (!this.isMobile() && !this.isTablet())) {
            this.overlayMenuActive = !this.overlayMenuActive;
        } else {
            if (this.isDesktop()) {
                this.staticMenuDesktopInactive = !this.staticMenuDesktopInactive;
            } else {
                this.staticMenuMobileActive = !this.staticMenuMobileActive;
            }
        }

        event.preventDefault();
    }

    onMenuClick($event) {
        this.menuClick = true;

        if (this.inlineMenuActive && !this.inlineMenuClick) {
            this.inlineMenuActive = false;
        }
    }

    onInlineMenuClick(event) {
        this.inlineMenuActive = !this.inlineMenuActive;
        this.inlineMenuClick = true;
    }

    onTopbarMenuButtonClick(event) {
        this.topbarItemClick = true;
        this.topbarMenuActive = !this.topbarMenuActive;

        this.hideOverlayMenu();

        event.preventDefault();
    }

    onTopbarItemClick(event, item) {
        this.topbarItemClick = true;

        if (this.activeTopbarItem === item) {
            this.activeTopbarItem = null;
        } else {
            this.activeTopbarItem = item;
        }

        event.preventDefault();
    }

    onTopbarSubItemClick(event) {
        event.preventDefault();
    }

    onConfigClick(event) {
        this.configClick = true;
    }

    onRippleChange(event) {
        this.ripple = event.checked;
    }

    hideOverlayMenu() {
        this.rotateMenuButton = false;
        this.overlayMenuActive = false;
        this.staticMenuMobileActive = false;
    }

    isTablet() {
        const width = window.innerWidth;
        return width <= 1024 && width > 640;
    }

    isDesktop() {
        return window.innerWidth > 1024;
    }

    isMobile() {
        return window.innerWidth <= 640;
    }

    isOverlay() {
        return this.layoutMode === 'overlay';
    }

    isHorizontal() {
        return this.layoutMode === 'horizontal';
    }

    isSlim() {
        return this.layoutMode === 'slim';
    }

    isStatic() {
        return this.layoutMode === 'static';
    }


}
