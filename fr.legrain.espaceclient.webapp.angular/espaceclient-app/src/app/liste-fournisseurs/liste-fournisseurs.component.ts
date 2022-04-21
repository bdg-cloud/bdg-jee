import { Component, OnInit } from '@angular/core';

import { Observable, Subject } from 'rxjs';

import {
  debounceTime, distinctUntilChanged, switchMap
} from 'rxjs/operators';

import { Tiers } from '../model/tiers';
import { TiersDossier } from '../model/tiers-dossier';
import { FournisseurService } from '../services/fournisseur.service';

@Component({
  selector: 'app-liste-fournisseurs',
  templateUrl: './liste-fournisseurs.component.html',
  styleUrls: ['./liste-fournisseurs.component.css']
})
export class ListeFournisseursComponent implements OnInit {

  //    fournisseurs: Observable<TiersDossier[]>;
  private searchTerms = new Subject<string>();
  public fournisseurs: TiersDossier[];

  constructor(private fournisseurService: FournisseurService) { }

  //    // Push a search term into the observable stream.
  //    search(term: string): void {
  //      this.searchTerms.next(term);
  //    }

  /*
   * https://dev.to/florimondmanca/consuming-apis-in-angular--the-model-adapter-pattern-3fk5
   * https://codecraft.tv/courses/angular/http/http-with-observables/
   * https://makina-corpus.com/blog/metier/2017/premiers-pas-avec-rxjs-dans-angular
   */

  ngOnInit(): void {
    this.fournisseurService.getFournisseurs().subscribe(data => {
      this.fournisseurs = data;

    });
    console.log('aaaa');

    //      this.heroes$ = this.searchTerms.pipe(
    //        // wait 300ms after each keystroke before considering the term
    //        debounceTime(300),
    //    
    //        // ignore new term if same as previous term
    //        distinctUntilChanged(),
    //    
    //        // switch to new search observable each time the term changes
    //        switchMap((term: string) => this.heroService.searchHeroes(term)),
    //      );
  }
}
