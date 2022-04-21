import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import html2canvas from 'html2canvas';
import jsPDF from 'jspdf';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { AppSettings } from '../env';
import { CompteEspaceClient, CompteEspaceClientAdapter } from '../model/compteEspaceClient';
import { ParamEmailConfirmationCommandeBoutique } from '../model/param-email-confirmation-commande-boutique';
import { PanierService } from '../services/panier.service';

@Component({
  selector: 'app-confirmation-paiement-panier',
  templateUrl: './confirmation-paiement-panier.component.html',
  styleUrls: ['./confirmation-paiement-panier.component.scss']
})
export class ConfirmationPaiementPanierComponent implements OnInit {

  public texteConfirmation: String;

  public codePanier: string;
  public codeCommande: string;
  public codeReglement: string;
  public codeReglementInternet: string;
  public montantHT: number;
  public montantTTC: number;

  private idPanier:any;
  private emailConfirmationCmd:boolean;

  constructor(private http: HttpClient, private appSettings: AppSettings,private route: ActivatedRoute, private panierService: PanierService,) { }

  ngOnInit(): void {
    this.texteConfirmation = this.appSettings.PARAMETRES.texteConfirmationPaiementBoutique;
    this.emailConfirmationCmd = this.appSettings.PARAMETRES.activeEmailConfirmationCmd;
    this.codePanier = this.route.snapshot.paramMap.get('codePanier')
    this.idPanier = this.route.snapshot.paramMap.get('idPanier')

    this.panierService.infosPanierValide(this.codePanier,this.idPanier).subscribe(
      data => {
          this.codePanier = data.codePanier;
          this.codeCommande = data.codeCommande;
          this.codeReglement = data.codeReglement;
          this.codeReglementInternet = data.codeReglementInternet;
          this.montantHT = data.montantHT;
          this.montantTTC = data.montantTTC;
      }
    );

    if(this.emailConfirmationCmd) {
      this.panierService.emailConfirmationCommandeBoutique(this.codePanier,this.idPanier).subscribe(
        data => {
            //alert("Email de confirmation de commande envoyé.");
        }
      );
    }
  }

  exportAsPDF(divId){
        let data = document.getElementById(divId);  
        /*
        html2canvas(data).then(canvas => {
        const contentDataURL = canvas.toDataURL('image/png')  
        let pdf = new jsPDF('p', 'cm', 'a4'); //Generates PDF in landscape mode
        // let pdf = new jspdf('p', 'cm', 'a4'); Generates PDF in portrait mode
        pdf.addImage(contentDataURL, 'PNG', 0, 0, 29.7, 21.0);  
        pdf.save('Commande.pdf');   
      }); 
      */
     html2canvas(data, { allowTaint: true }).then(canvas => {
      let HTML_Width = canvas.width;
      let HTML_Height = canvas.height;
      let top_left_margin = 15;
      let PDF_Width = HTML_Width + (top_left_margin * 2);
      let PDF_Height = (PDF_Width * 1.5) + (top_left_margin * 2);
      let canvas_image_width = HTML_Width;
      let canvas_image_height = HTML_Height;
      let totalPDFPages = Math.ceil(HTML_Height / PDF_Height) - 1;
      canvas.getContext('2d');
      let imgData = canvas.toDataURL("image/jpeg", 1.0);
      let pdf = new jsPDF('p', 'pt', [PDF_Width, PDF_Height]);
      pdf.addImage(imgData, 'JPG', top_left_margin, top_left_margin, canvas_image_width, canvas_image_height);
      for (let i = 1; i <= totalPDFPages; i++) {
        pdf.addPage([PDF_Width, PDF_Height], 'p');
        pdf.addImage(imgData, 'JPG', top_left_margin, -(PDF_Height * i) + (top_left_margin * 4), canvas_image_width, canvas_image_height);
      }
       pdf.save("Commande_"+this.codeCommande+".pdf");
    });
    }

}
