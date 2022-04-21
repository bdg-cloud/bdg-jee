package fr.legrain.paiement.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import fr.legrain.abonnement.stripe.model.TaStripeAccount;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeAccountServiceRemote;
import fr.legrain.bdg.paiement.service.remote.IPaiementEnLigneDossierService;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaCompteServiceWebExterneServiceRemote;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaTServiceWebExterne;
import fr.legrain.servicewebexterne.model.UtilServiceWebExterne;

@Stateless
public class PaiementGeneralDossierService /*implements IPaiementEnLigneDossierService*/ {
	
	private @EJB ITaCompteServiceWebExterneServiceRemote taCompteServiceWebExterneService;
	
	@Inject @Any private Instance<IPaiementEnLigneDossierService> iServicePaiementEnLigneDossierInstance;
	private @Inject @PaiementLgr(PaiementLgr.TYPE_STRIPE) IPaiementEnLigneDossierService paiementEnLigneDossierServiceStripe;
	private @EJB ITaStripeAccountServiceRemote taStripeAccountService;
	
	private TaCompteServiceWebExterne compte = null;
	
	public IPaiementEnLigneDossierService findImplementation(TaCompteServiceWebExterne compte) {
//		if(compte==null) {
//			compte =  taCompteServiceWebExterneService.findCompteDefautPourAction(TaTServiceWebExterne.TYPE_PAIEMENT);
//		}

		if(compte!=null) {

			String implementation = "";

			switch (compte.getTaServiceWebExterne().getCodeServiceWebExterne()) {
			case UtilServiceWebExterne.CODE_SWE_STRIPE:
				implementation = PaiementLgr.TYPE_STRIPE;
				break;
			case UtilServiceWebExterne.CODE_SWE_STRIPE_CONNECT:
				implementation = PaiementLgr.TYPE_STRIPE_CONNECT;
				break;
			case UtilServiceWebExterne.CODE_SWE_PAYPAL:
				implementation = PaiementLgr.TYPE_PAYPAL;
				break;
			default:
				break;
			}
			
			IPaiementEnLigneDossierService service = iServicePaiementEnLigneDossierInstance.select(new MyQualifierLiteral(implementation)).get();
			return service;
		}  else {
			//pas de compte paramétré, on force l'appel sur l'implémentation Stripe, qui utilisera le compte Stripe Connect du dossier en priorité s'il existe
			return iServicePaiementEnLigneDossierInstance.select(new MyQualifierLiteral(PaiementLgr.TYPE_STRIPE_CONNECT)).get();
		}

//		return null;
	}

	public boolean dossierPermetPaiementCB() {
		boolean paiementPossible = false;
		TaCompteServiceWebExterne config = taCompteServiceWebExterneService.findCompteDefautPourAction(TaTServiceWebExterne.TYPE_PAIEMENT);
		if(config!=null) {
			if(!config.getTaServiceWebExterne().getCodeServiceWebExterne().equals(UtilServiceWebExterne.CODE_SWE_STRIPE_CONNECT)) {
				//TODO il faudrait introduire une notion de compte "vérifier/valider" au moins 1 fois après chaque modif de clé api ou login password pour valider que les informations de connections sont réputé correcte et utilisable
				paiementPossible = true;
			} 
			//else {} //Stripe connect, donc on passe a la condition suivante pour vérifier la liaison stripe
		}
		
		if(!paiementPossible) {
			TaStripeAccount taStripeAccount = taStripeAccountService.findInstance();
			if(taStripeAccount.getIdExterne()!=null && !taStripeAccount.getIdExterne().equals("")) {
				
				if(paiementEnLigneDossierServiceStripe.liaisonBDGCompteOKChezStripe(taStripeAccount.getIdExterne())) {
					if(taStripeAccount.getChargesEnabled()!=null && taStripeAccount.getChargesEnabled() 
					&& taStripeAccount.getDetailsSubmitted()!=null &&taStripeAccount.getDetailsSubmitted()) {
						//compte verifié par stripe
						paiementPossible = true;
					}
				}
			}
		}
		return paiementPossible;
	}

	
}
