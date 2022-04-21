import { Component, OnInit, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { User, UserAdapter } from '../model/user';
import { Tiers, TiersAdapter } from '../model/tiers';
import { AppSettings } from '../env';
import { EspaceClientService } from '../services/espace-client.service';
import { AuthenticationService } from '../services/authentication.service';
import { CompteEspaceClient, CompteEspaceClientAdapter } from '../model/compteEspaceClient';

@Component({
  selector: 'app-choix-compte-tiers',
  templateUrl: './choix-compte-tiers.component.html',
  styleUrls: ['./choix-compte-tiers.component.css']
})
export class ChoixCompteTiersComponent implements OnInit {

  public listeTiersCompteEspaceClient: Tiers[];
  public codeTiersCourant: String;

  private _url = "";

  constructor(        
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService,
    private espaceClientService: EspaceClientService,
    private appSettings: AppSettings) { }

  ngOnInit() {

    this.authenticationService.listeTiers().subscribe(data => {
      this.listeTiersCompteEspaceClient = data;
      
    });
    this.codeTiersCourant = this.appSettings.CODE_CLIENT_CHEZ_FRS;
  }

  handleClick(event, tiers) {
    console.log(tiers.nomTiers);

    this.appSettings.CODE_CLIENT_CHEZ_FRS = tiers.codeTiers;

    let currentUser = JSON.parse(localStorage.getItem('currentUser'));
    currentUser.codeTiers = tiers.codeTiers;
    currentUser.nom = tiers.nomTiers;
    currentUser.prenom = tiers.prenomTiers;
    localStorage.setItem('currentUser', JSON.stringify(currentUser));    

    window.location.reload();
  }

}
