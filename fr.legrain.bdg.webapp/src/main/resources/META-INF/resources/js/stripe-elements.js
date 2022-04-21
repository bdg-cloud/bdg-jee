var stripe = null;
var elements = null;
var style = null;
var card = null;

//var cle_publique_stripe_lgr = 'pk_test_Dj5aRLvgEd6K8zpNMxKqlc82';

//function initStripe(cle_publique_stripe) {
//	initStripe(cle_publique_stripe,true);
//}

function initStripe(cle_publique_stripe,paiement) {
	initStripe(cle_publique_stripe,null,paiement);
}
	
function initStripe(cle_publique_stripe,compteStripeConnect,paiement) {
	paiement = !!paiement;
	// Create a Stripe client.
	//alert(cle_publique_stripe);
	//alert(compteStripeConnect);
	
	if(compteStripeConnect!=null) {
		stripe = Stripe(cle_publique_stripe, {
			  stripeAccount: compteStripeConnect
		});
	} else {
		stripe = Stripe(cle_publique_stripe);
	}
	
	
	// Create an instance of Elements.
	elements = stripe.elements();
	
	// Custom styling can be passed to options when creating an Element.
	// (Note that this demo uses a wider set of styles than the guide below.)
	style = {
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
	card = elements.create('card', {style: style});
	
	// Add an instance of the card Element into the `card-element` <div>.
	card.mount('#card-element');
	
	// Handle real-time validation errors from the card Element.
	card.addEventListener('change', function(event) {
	  var displayError = document.getElementById('card-errors');
	  if (event.error) {
	    displayError.textContent = event.error.message;
	  } else {
	    displayError.textContent = '';
	  }
	});
	
	// Handle form submission.
	var form = document.getElementById('payment-form');
	var cardholderName = document.getElementById('cardholder-name');
//	var cardholderEmail= document.getElementById('cardholder-email');
//	var cardholderPhone = document.getElementById('cardholder-phone');
	var cardButton = document.getElementById('card-button');
	var clientSecret = cardButton.dataset.secret;
	
//	form.addEventListener('submit', function(event) {
//	  event.preventDefault();
//	
//	  stripe.createToken(card).then(function(result) {
//	    if (result.error) {
//	      // Inform the user if there was an error.
//	      var errorElement = document.getElementById('card-errors');
//	      errorElement.textContent = result.error.message;
//	    } else {
//	      // Send the token to your server.
//	      stripeTokenHandler(result.token);
//	    }
//	  });
//	});
	
	if(paiement === true) {
		form.addEventListener('submit', function(event) {
			  event.preventDefault();
//			  if ( cardholderPhone.value == null ){
//				  cardholderPhone.value = '.';
//			  }
//			  if ( cardholderEmail.value == null ){
//				  cardholderEmail.value = '.';
//			  }
			  
			  var d = {name: cardholderName.value};
//	    	  if (cardholderPhone.value){
//				  d.phone = cardholderPhone.value;
//			  }
//			  if (cardholderEmail.value){
//				  d.email = cardholderEmail.value;
//			  }
			  
		      stripe.handleCardPayment(
				clientSecret,
		        card,
		        {
		          payment_method_data: {
		        	 // billing_details: {name: cardholderName.value/*, phone: cardholderPhone.value, email: cardholderEmail.value*/}
				      billing_details : d
		          }
		        }
		      ).then(function(result) {
		    	  if (result.error) {
		    	      // Inform the user if there was an error.
		    	      var errorElement = document.getElementById('card-errors');
		    	      errorElement.textContent = result.error.message;
		    	  } else {
		    	     paiementEnCours();
		    	  }

		      });
		});
	} else {//uniquement pour un enregistrement de la carte
		form.addEventListener('submit', function(event) {
			  event.preventDefault();
//			  if ( cardholderPhone.value == null ){
//				  cardholderPhone.value = '.';
//			  }
//			  if ( cardholderEmail.value == null ){
//				  cardholderEmail.value = '.';
//			  }
			  
		      stripe.handleCardSetup(
				clientSecret,
		        card,
		        {
		          payment_method_data: {
		        	  billing_details: {name: cardholderName.value/*, phone: cardholderPhone.value, email: cardholderEmail.value*/}
		          }
		        }
		      ).then(function(result) {
//		    	  	alert(result);
		    	    if (result.error) {
		    	    	// Inform the user if there was an error.
			    	    var errorElement = document.getElementById('card-errors');
			    	    errorElement.textContent = result.error.message;
		    	    } else if (result.setupIntent.status === 'succeeded') {
	//	    	    	// Success! Payment is confirmed. Update the interface to display the confirmation screen.
//		    	    	alert('bb');
		    	    	enregistrePM([{name:'x', value:result.setupIntent.payment_method}]);
	//	    	    } else if (result.paymentIntent.status === 'processing') {
	//	    	    	// Success! Now waiting for payment confirmation. Update the interface to display the confirmation screen.
	//	    	    	alert('cc');
		    	    } else {
	//	    	        // Payment has failed.
//		    	    	alert('dd');
		    	    }
		      });
		});
	}

}

function lgrstr() {
	stripe.createToken(card).then(function(result) {
	    if (result.error) {
	      // Inform the user if there was an error.
	      var errorElement = document.getElementById('card-errors');
	      errorElement.textContent = result.error.message;
	    } else {
	      // Send the token to your server.
	      stripeTokenHandler(result.token);
	    }
	});
}

function stripeTokenHandler(token) {
	//4242 4242 4242 4242
	//alert(token);
	//alert(token.id);
	t = token.id;
	//appeler le serveur avec une remote command en lui passant le token genere
	rcToken([{name:'x', value:t}]);
}

function stripeSourceHandler(source) {
	//4242 4242 4242 4242
	//alert(token);
	//alert(source.id);
	t = source.id;
	//appeler le serveur avec une remote command en lui passant la source genere
	rcSource([{name:'x', value:t}]);
}

function lgrstrSource() {
	var ownerInfo = null;
//	ownerInfo = {
//	  owner: {
//	    name: 'Jenny Rosen',
//	    address: {
//	      line1: 'Nollendorfstraße 27',
//	      city: 'Berlin',
//	      postal_code: '10777',
//	      country: 'DE',
//	    },
//	    email: 'jenny.rosen@example.com'
//	  },
//	};
	
	stripe.createSource(card, ownerInfo).then(function(result) {
	    if (result.error) {
	      // Inform the user if there was an error
	      var errorElement = document.getElementById('card-errors');
	      errorElement.textContent = result.error.message;
	    } else {
	      // Send the source to your server
	      stripeSourceHandler(result.source);
	    }
	  });
}





