import { Component, OnInit } from '@angular/core';

import { Observable, Subject } from 'rxjs';

import {
    debounceTime, distinctUntilChanged, switchMap
  } from 'rxjs/operators';

import { Tiers } from '../model/tiers';
import { TiersDossier } from '../model/tiers-dossier';
import { FournisseurService } from '../services/fournisseur.service';

import {TableModule} from 'primeng/table';
import {MessageService} from 'primeng/api';

@Component({
  selector: 'app-mes-fournisseurs',
  templateUrl: './mes-fournisseurs.component.html',
  styleUrls: ['./mes-fournisseurs.component.css'],
  providers: [MessageService]
})
export class MesFournisseursComponent implements OnInit {

    private searchTerms = new Subject<string>();
    public fournisseurs: TiersDossier[];
    public selectedFournisseur: TiersDossier;
    
    constructor(private fournisseurService: FournisseurService, 
      private messageService: MessageService
      ) {}
   
    /*
     * https://dev.to/florimondmanca/consuming-apis-in-angular--the-model-adapter-pattern-3fk5
     * https://codecraft.tv/courses/angular/http/http-with-observables/
     * https://makina-corpus.com/blog/metier/2017/premiers-pas-avec-rxjs-dans-angular
     */ 
    
    ngOnInit(): void {
        this.fournisseurService.getFournisseurs().subscribe( data => {
            this.fournisseurs = data;
            
          });
        console.log('aaaa');

    }
    
    onRowSelect(event) {
        this.messageService.add({severity:'info', summary:'Fournisseur', detail:'Entreprise: ' + event.data.nomEntreprise});
    }

    onRowUnselect(event) {
        this.messageService.add({severity:'info', summary:'Fournisseur', detail:'Entreprise: ' + event.data.nomEntreprise});
    }
}
