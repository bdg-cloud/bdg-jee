import { Component, OnInit } from '@angular/core';

import { ParametreEspaceClient, ParametreEspaceClientAdapter } from './model/parametreEspaceClient';
import { AppSettings } from './env';
import { AuthenticationService } from './services/authentication.service';
import { EspaceClientService } from './services/espace-client.service';

@Component({
  selector: 'app-footer',
  templateUrl: './app.footer.component.html'
})
export class AppFooterComponent implements OnInit {

  public modeLegrain: boolean;
  public url: string;
  public email: string;

  constructor(private authenticationService: AuthenticationService, private espaceClientService: EspaceClientService, private appSettings: AppSettings) { }

  ngOnInit() {
    this.espaceClientService.parametres().subscribe(
      data => {
        this.appSettings.PARAMETRES = data;
        this.modeLegrain = this.appSettings.modeLegrain;

        this.url = this.appSettings.PARAMETRES.contactWeb;
        this.email = this.appSettings.PARAMETRES.contactEmail;
      }
    );
  }

  handleClickUrl(event: Event) {
    window.open(this.url);
  }

  handleClickEmail(event: Event) {
    location.href = "mailto:" + this.email;//+'?cc='+emailCC+'&subject='+emailSub+'&body='+emailBody;

  }

}
