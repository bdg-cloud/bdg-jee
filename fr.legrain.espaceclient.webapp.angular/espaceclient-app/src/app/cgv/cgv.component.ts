import { Component, OnInit } from '@angular/core';
import { AppSettings } from '../env';

@Component({
  selector: 'app-cgv',
  templateUrl: './cgv.component.html',
  styleUrls: ['./cgv.component.scss']
})
export class CGVComponent implements OnInit {

  public cgvHTML: String;

  constructor(private appSettings: AppSettings,) { }

  ngOnInit(): void {
    this.cgvHTML = this.appSettings.PARAMETRES.cgvCatWeb;
  }

}
