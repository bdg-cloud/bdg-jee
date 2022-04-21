import { Component, OnInit, AfterViewChecked, AfterViewInit } from '@angular/core';
import { DynamicDialogRef } from 'primeng/dynamicdialog';
import { DynamicDialogConfig } from 'primeng/dynamicdialog';
import { first } from 'rxjs/operators';

import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { PaiementStripeService } from '../services/paiement-stripe.service';

import { Facture, FactureAdapter } from '../model/facture';
import { AvisEcheance, AvisEcheanceAdapter } from '../model/avisEcheance';

import { AppSettings } from '../env';
import { Panier } from '../model/panier';
/**
 * https://www.devdungeon.com/content/using-stripejs-elements-angular
 */
declare var Stripe: any;

@Component({
  selector: 'app-paiement-cb',
  templateUrl: './paiement-cb.component.html',
  styleUrls: ['./paiement-cb.component.css']
})
export class PaiementCBComponent implements OnInit, AfterViewChecked, AfterViewInit {

  private montantPaiement: number;
  private intentSecret: string;

  public saisie = true;
  public encours = true;
  public cleStripe: string;
  public etatPaiementCourant: number;

  constructor(private paiementStripeService: PaiementStripeService, public ref: DynamicDialogRef, public config: DynamicDialogConfig,
    private appSettings: AppSettings) { }

  ngOnInit() {

    //      var a:string = this.paiementStripeService.rechercherCleStripe();
    console.log("ready !" + this.config.data.codeDocument);
    //console.log(this.config.data );
    this.montantPaiement = this.config.data.netTtcCalc;
    if (this.config.data instanceof Facture) {
      this.paiementStripeService.creerPaymentIntent(this.config.data, this.montantPaiement)
        .subscribe(data => {
        this.intentSecret = data;
        });
    } else if (this.config.data instanceof AvisEcheance) {
      this.paiementStripeService.creerPaymentIntentAvisEcheance(this.config.data, this.montantPaiement)
        .subscribe(data => {
        this.intentSecret = data;
        });
    } else if (this.config.data instanceof Panier) {
      this.paiementStripeService.creerPaymentIntentPanier(this.config.data, this.montantPaiement)
        .subscribe(data => {
        this.intentSecret = data;
        });
    }
    console.log("===> " + this.intentSecret);
    console.log(this.intentSecret);
  }

  public actFermer(event) {
    this.ref.close(this.etatPaiementCourant);
  }

  public ngAfterViewChecked(): void {

  }

  public getSaisie() {
    return this.saisie;
  }

  public setSaisie(saisie) {
    return this.saisie = saisie;
  }

  ngAfterViewInit() {
    //      var a:string = this.paiementStripeService.rechercherCleStripe();


    this.paiementStripeService.clePubliqueStripe()
      .subscribe(
        data => {
          //alert(<string>data);
          /////////////////////////////////////////////////////////////////////
          this.paiementStripeService.cleConnectStripe()
            .subscribe(
              data2 => {
                //alert(<string>data2);
                if(data2!==null && data2!=='null') { //il y a un compte connect, on l'utilise
                  this.initStripe(data, data2, this.appSettings);
                } else {
                  //pas de compte connect
                  this.initStripe(data, null, this.appSettings);
                }
              },
              error => {
                //alert(error);
              });
          /////////////////////////////////////////////////////////////////////
          //this.initStripe(data, null, this.appSettings);
        },
        error => {
          //alert(error);
        });

    this.saisie = true;
    this.encours = true;

  }

  public initStripe(cle_publique_stripe: string, compteStripeConnect: string, settings: AppSettings): void {
    // Create a Stripe client.
    //alert(cle_publique_stripe);
    //const stripe = Stripe(cle_publique_stripe);
    var stripeVar;

    if(compteStripeConnect!=null) {
      stripeVar = Stripe(cle_publique_stripe, {
          stripeAccount: compteStripeConnect
      });
    } else {
      stripeVar = Stripe(cle_publique_stripe);
    }
    const stripe = stripeVar;

    var parent = this;
    // Create an instance of Elements.
    const elements = stripe.elements();

    // Custom styling can be passed to options when creating an Element.
    // (Note that this demo uses a wider set of styles than the guide below.)
    const style = {
      base: {
        color: '#32325d',
        lineHeight: '18px',
        fontFamily: '"Helvetica Neue", Helvetica, sans-serif',
        fontSmoothing: 'antialiased',
        fontSize: '16px',
        '::placeholder': {
          color: '#aab7c4'
        }
      },
      invalid: {
        color: '#fa755a',
        iconColor: '#fa755a'
      }
    };

    // Create an instance of the card Element.
    const card = elements.create('card', { style: style });

    // Add an instance of the card Element into the `card-element` <div>.
    card.mount('#card-element');

    // Handle real-time validation errors from the card Element.
    card.addEventListener('change', function (event) {
      var displayError = document.getElementById('card-errors');
      if (event.error) {
        displayError.textContent = event.error.message;
      } else {
        displayError.textContent = '';
      }
    });

    // Handle form submission.
    const form = document.getElementById('payment-form');
    var cardholderName = document.getElementById('cardholder-name') as HTMLInputElement;
    //  var cardholderEmail= document.getElementById('cardholder-email');
    //  var cardholderPhone = document.getElementById('cardholder-phone');
    var cardButton = document.getElementById('card-button');
    //      var clientSecret = cardButton.dataset.secret;

    form.addEventListener('submit', function (event) {
      var clientSecret = cardButton.dataset.secret;
      event.preventDefault();
      //              if ( cardholderPhone.value == null ){
      //                  cardholderPhone.value = '.';
      //              }
      //              if ( cardholderEmail.value == null ){
      //                  cardholderEmail.value = '.';
      //              }

      var d = { name: cardholderName.value };
      //              if (cardholderPhone.value){
      //                  d.phone = cardholderPhone.value;
      //              }
      //              if (cardholderEmail.value){
      //                  d.email = cardholderEmail.value;
      //              }

      stripe.handleCardPayment(
        clientSecret,
        card,
        {
          payment_method_data: {
            // billing_details: {name: cardholderName.value/*, phone: cardholderPhone.value, email: cardholderEmail.value*/}
            billing_details: d
          }
        }
      ).then(function (result) {
        if (result.error) {
          // Inform the user if there was an error.
          var errorElement = document.getElementById('card-errors');
          errorElement.textContent = result.error.message;
        } else {
          //paiementEnCours();
          parent.setSaisie(false);
          parent.etatPaiementCourant = 0;
          setTimeout(function () {
            //if (newState == -1) {
            var pi = clientSecret.split("_secret_")[0];
            parent.paiementStripeService.etatPaiementCourant(settings.BDG_TENANT_DOSSIER, pi)
              .subscribe(data => {
                console.log(data);
                parent.etatPaiementCourant = data;
                parent.encours = false;
              }
              );

            //parent.encours = false;
            //  }
          }, 4000);
        }

      });
    });
  }

}
