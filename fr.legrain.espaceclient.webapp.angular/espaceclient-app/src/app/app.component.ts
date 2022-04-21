import { Component, AfterViewInit, ElementRef, Renderer2, ViewChild, OnDestroy } from '@angular/core';
import { ScrollPanel } from 'primeng/scrollpanel';
import { Router } from '@angular/router';

import { AuthenticationService } from './services/authentication.service';
import { EspaceClientService } from './services/espace-client.service';
import { User } from './model/user';
import { AppSettings } from './env';
import {MenuService} from './app.menu.service';
import {PrimeNGConfig} from 'primeng/api';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent implements AfterViewInit {

    currentUser: User;


    constructor(
        public renderer: Renderer2,
        private router: Router,
        private authenticationService: AuthenticationService,
        private espaceClientService: EspaceClientService,
        private appSettings: AppSettings,
        private menuService: MenuService, 
        private primengConfig: PrimeNGConfig
    ) {
        this.authenticationService.currentUser.subscribe(x => this.currentUser = x);
        //this.currentUser = new User();
    }

    logout() {
        this.authenticationService.logout();
        this.router.navigate(['/login']);
    }

    ngOnInit() {
        this.primengConfig.ripple = true;
    }

    ngAfterViewInit() {
        //        setTimeout(() => {this.layoutMenuScrollerViewChild.moveBar(); }, 100);
        //        var hostname = window.location.hostname.split("."); //dev.espace-client.demo.promethee.biz
        //        var tenant = hostname[hostname.length-3];
        //        this.appSettings.BDG_TENANT_DOSSIER = tenant;
        this.appSettings.initTenantFromUrl();

        console.log(this.appSettings.BDG_TENANT_DOSSIER);
        this.espaceClientService.parametres().subscribe(x => { console.log(x); this.appSettings.PARAMETRES = x; });

    }



}
