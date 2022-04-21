package fr.legrain.bdg.paiement.service.remote;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;

import fr.legrain.abonnement.stripe.dto.TaStripeChargeDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeCouponDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeCustomerDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeInvoiceDTO;
import fr.legrain.abonnement.stripe.dto.TaStripePlanDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeProductDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeSourceDTO;
import fr.legrain.abonnement.stripe.model.TaStripeAccount;
import fr.legrain.abonnement.stripe.model.TaStripeCharge;
import fr.legrain.abonnement.stripe.model.TaStripeInvoice;
import fr.legrain.abonnement.stripe.model.TaStripePaymentIntent;
import fr.legrain.abonnement.stripe.model.TaStripePlan;
import fr.legrain.document.dto.IDocumentPayableCB;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaPanier;
import fr.legrain.document.model.TaReglement;
import fr.legrain.paiement.model.PaiementCarteBancaire;
import fr.legrain.paiement.model.RetourPaiementCarteBancaire;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.tiers.model.TaTiers;

/**
 * Les classes implémentant cette interface donneront la possiblité d'effectuer un reglèment via une API de paiement externe qui aura été paramétré dans le dossier BDG.
 * Les règlements arriveront donc sur les comptes des propiétaires respectif des compte/clé API de paiement saisis dans chacun des dossier BDG
 * 
 * Les méthode appelé depuis un evenement de WebHook Stripe et qui font elles meme un appel à l'api Stripe doivent avoir le paramètre : String connectedAccountId
 * @author nicolas
 *
 */
public interface IPaiementEnLigneDossierService {
	
	public String creerPaymentIntent(TaCompteServiceWebExterne configService, PaiementCarteBancaire paiementCarteBancaire, TaTiers taTiers, String libelle)
			throws EJBException;
	
	public String creerPaymentIntent(TaCompteServiceWebExterne configService, PaiementCarteBancaire paiementCarteBancaire, IDocumentPayableCB documentPayableCB, String libelle)
			throws EJBException;
	
	/**
	 * @deprecated Rendre tous les documents IDocumentPayableCB 
	 * ou trouver un moyen de lier un paiement à un document "non payable" (autre table TaRReglement en parallele des "vrai" règlement ?)
	 */
	public String creerPaymentIntent(TaCompteServiceWebExterne configService, PaiementCarteBancaire paiementCarteBancaire, TaAvisEcheance taAvisEcheance, String libelle)
			throws EJBException;
	
	/**
	 * @deprecated Rendre tous les documents IDocumentPayableCB 
	 * ou trouver un moyen de lier un paiement à un document "non payable" (autre table TaRReglement en parallele des "vrai" règlement ?)
	 */
	public String creerPaymentIntent(TaCompteServiceWebExterne configService, PaiementCarteBancaire paiementCarteBancaire, TaPanier taPanier, String libelle)
			throws EJBException;
	
	public String creerPaymentIntent(TaCompteServiceWebExterne configService, PaiementCarteBancaire paiementCarteBancaire, IDocumentPayableCB documentPayableCB, TaTiers taTiers, String libelle)
			throws EJBException;
	
	public void ajouterAdresseEmailPourRecu(TaCompteServiceWebExterne compte, String paymentIntentId, String adresseEmailTicketRecu);
	
	public RetourPaiementCarteBancaire validerPaymentIntent(TaCompteServiceWebExterne configService, String paymentIntentStripeID, String connectedAccountId)
			throws EJBException;
	
	public TaStripePaymentIntent validerPaymentIntentReglement(TaCompteServiceWebExterne configService, String paymentIntentStripeID, String connectedAccountId)
			throws EJBException;
	
	public RetourPaiementCarteBancaire validerPaymentIntent(String tenant, TaCompteServiceWebExterne configService, String paymentIntentStripeID, String connectedAccountId)
			throws EJBException;
	
	public TaStripePaymentIntent validerPaymentIntentReglement(String tenant, TaCompteServiceWebExterne configService, String paymentIntentStripeID, String connectedAccountId)
			throws EJBException;
	
	public IDocumentPayableCB validerPaymentIntentGenerationDoc(String tenant, TaStripePaymentIntent paymentIntentStripe)
			throws EJBException;
	
	public TaStripePaymentIntent validerPaymentIntentAffectation(String tenant, IDocumentPayableCB doc, TaReglement taReglement, TaStripePaymentIntent paymentIntentStripe)
			throws EJBException;
	
	public RetourPaiementCarteBancaire payer(TaCompteServiceWebExterne configService, PaiementCarteBancaire paiementCarteBancaire, TaTiers taTiers, String libelle)
			throws EJBException;
	
	public RetourPaiementCarteBancaire payer(TaCompteServiceWebExterne configService, PaiementCarteBancaire paiementCarteBancaire, IDocumentPayableCB taFacture, TaTiers taTiers, String libelle)
			throws EJBException;
	
	public RetourPaiementCarteBancaire payer(TaCompteServiceWebExterne configService, PaiementCarteBancaire paiementCarteBancaire, IDocumentPayableCB taFacture, String libelle)
			throws EJBException;
	
//	public RetourPaiementCarteBancaire payer(TaCompteServiceWebExterne configService, PaiementCarteBancaire paiementCarteBancaire, TaTicketDeCaisse taTicketDeCaisse)
//			throws EJBException;
//	
//	public RetourPaiementCarteBancaire payer(TaCompteServiceWebExterne configService, PaiementCarteBancaire paiementCarteBancaire, TaAcompte taAcompte)
//			throws EJBException;

//	public String payer(TaCompteServiceWebExterne configService, BigDecimal montant, String numeroCarte, int moisCarte,
//			int anneeCarte, String cryptogrammeCarte, String nomPorteurCarte, String Description)
//			throws EJBException;
//	
//	public String payer(TaCompteServiceWebExterne configService, BigDecimal montant, String numeroCarte, int moisCarte,
//			int anneeCarte, String cryptogrammeCarte, String Description)
//			throws EJBException;
	
	/***************************************************************************************************************************************************/
	public String creerCarteStripe(TaCompteServiceWebExterne configService, BigDecimal montant, String numeroCarte, int moisCarte, int anneeCarte, String cryptogrammeCarte, String nomPorteurCarte, String description)  throws EJBException;
	
	public String creerCarteStripe(TaCompteServiceWebExterne configService, String carteTokenStripe) throws EJBException;
	
	public String creerSourcePrelevementSEPA(TaCompteServiceWebExterne configService, String iban, String nomPersonneAPrelever) throws EJBException;
	
	public String creerCustomerStripe(TaCompteServiceWebExterne configService, String email, String sourceId, String description) throws EJBException;
	public String attacherSourceStripe(TaCompteServiceWebExterne configService, String sourceId, String customerId) throws EJBException;
	public String detacherSourceStripe(TaCompteServiceWebExterne configService, String sourceId) throws EJBException;
	
	public String payerAvecSourceStripe(TaCompteServiceWebExterne configService, BigDecimal montant, String customerStripeID, String sourceStripeID, String description) throws EJBException;
	public String payerAvecSourceStripe(TaCompteServiceWebExterne configService, BigDecimal montant, String customerStripeID, String sourceStripeID, String description, IDocumentPayableCB documentPayableCB, TaTiers taTiers, String originePaiement) throws EJBException;
	public String payerParPrelevementSEPA(TaCompteServiceWebExterne configService, BigDecimal montant, String customerStripeID, String sourceStripeID) throws EJBException;
	
	public String creerWebhook(TaCompteServiceWebExterne configService, String url, String enabledEvents) throws EJBException;
	public String creerCouponStripe(TaCompteServiceWebExterne configService, String duration, Integer durationInMonths, String id, BigDecimal percentOff/*, BigDecimal amountOff*/) throws EJBException;
	public String creerInvoiceItemStripe(TaCompteServiceWebExterne configService, String customerStripeID, String description, BigDecimal montant) throws EJBException;
	public String creerInvoiceStripe(TaCompteServiceWebExterne configService, String customerStripeID) throws EJBException;
	public String creerPlanStripe(TaCompteServiceWebExterne configService, String nickname, String interval, String currency, BigDecimal montant, String productStripeID) throws EJBException;
	public String creerProductStripe(TaCompteServiceWebExterne configService, String name, String type) throws EJBException;
	
	public TaStripeCustomerDTO rechercherCustomer(TaCompteServiceWebExterne configService, String customerStripeID);
	public TaStripePlanDTO rechercherPlan(TaCompteServiceWebExterne configService, String planStripeID);
	public TaStripeProductDTO rechercherProduct(TaCompteServiceWebExterne configService, String productStripeID);
	public TaStripeSourceDTO rechercherSource(TaCompteServiceWebExterne configService, String sourceStripeID);
	public TaStripeInvoiceDTO rechercherInvoice(TaCompteServiceWebExterne configService, String invoiceStripeID);
	//public TaStripeSubscriptionDTO rechercherSubscription(TaCompteServiceWebExterne configService, String subscriptionStripeID);
	
	public Boolean supprimerPlan(TaCompteServiceWebExterne configService, String planStripeID);
	public Boolean supprimerProduct(TaCompteServiceWebExterne configService, String productStripeID);
	public Boolean supprimerCustomer(TaCompteServiceWebExterne configService, String customerStripeID);
	public Boolean supprimerCoupon(TaCompteServiceWebExterne configService, String couponStripeID);
	
	public TaStripePlanDTO updatePlan(TaCompteServiceWebExterne configService, String planStripeID, String nickname, Boolean active);
	public TaStripeProductDTO updateProduct(TaCompteServiceWebExterne configService, String productStripeID, String name, Boolean active);
	public TaStripeCustomerDTO updateCustomer(TaCompteServiceWebExterne configService, String customerStripeID,String email, String sourceDefaultId, String description, String couponId, String invoicePrefix);
	public TaStripeCouponDTO updateCoupon(TaCompteServiceWebExterne configService, String couponStripeID, String name);
	//public TaStripeSubscriptionDTO updateSubscription(TaCompteServiceWebExterne configService, String subscriptionStripeID, String billing, String billing_cycle_anchor, String coupon, String days_until_due, String default_source);
	
	public String refundCharge(TaCompteServiceWebExterne configService, String chargeStripeID, BigDecimal montant);
	
	public String creerSubscriptionStripe(TaCompteServiceWebExterne configService, String customerStripeID, Map<String,Integer> mapSubscriptionItemPlanStripeIDQuantity,
			String sourceStripeID, Integer timpeStampBillingCycleAnchor, String billing, Integer daysUntilDue) throws EJBException;
	public String annulerSubscriptionStripe(TaCompteServiceWebExterne configService, String subscriptionStripeID) throws EJBException;
	//public List<TaStripeSubscriptionDTO>rechercheSubscriptionStripeForCustomer(TaCompteServiceWebExterne configService, String customerStripeID, Integer limite, String status) throws EJBException;
	
	public TaStripeInvoiceDTO payerInvoice(TaCompteServiceWebExterne configService, String invoiceStripeID, Boolean  paidOutOfBand);
	public TaStripeInvoiceDTO upcomingInvoiceForCustomer(TaCompteServiceWebExterne configService, String customerStripeID); //prochain invoice en attente pour ce customer
	public List<TaStripeInvoiceDTO> rechercheInvoiceStripeForSubscription(TaCompteServiceWebExterne configService, String subscriptionStripeID, Integer limite) throws EJBException;
	public List<TaStripeInvoiceDTO> rechercheInvoiceStripeForCustomer(TaCompteServiceWebExterne configService, String customerStripeID, Integer limite) throws EJBException;
	
	public List<TaStripeChargeDTO>rechercheChargeStripe(TaCompteServiceWebExterne configService, Integer limite, Map<String,String> sourceType) throws EJBException;
	public List<TaStripeChargeDTO>rechercheChargeStripeForCustomer(TaCompteServiceWebExterne configService, String customerStripeID, Integer limite, Map<String,String> sourceType) throws EJBException;
	
	public List<TaStripePlanDTO>recherchePlanStripe(TaCompteServiceWebExterne configService, Integer limite, Boolean active) throws EJBException;
	public List<TaStripePlanDTO>recherchePlanStripeForProduct(TaCompteServiceWebExterne configService, String productStripeID, Integer limite, Boolean active) throws EJBException;
	
	public TaStripePlan updatePlanFromStripe(TaCompteServiceWebExterne configService, String planStripeID);
	public TaStripeInvoice updateInvoiceFromStripe(TaCompteServiceWebExterne configService, String invoiceStripeID, String subscriptionStripeID);
	//public TaStripeSubscription updateSubscriptionFromStripe(TaCompteServiceWebExterne configService, String subscriptionStripeID);
	public TaStripeCharge updateChargeFromStripe(TaCompteServiceWebExterne configService, String chargeStripeID, String connectedAccountId);
	public TaStripePaymentIntent updatePaymentIntentFromStripe(TaCompteServiceWebExterne configService, String paymentIntentStripeID,String connectedAccountId);
	public TaStripePaymentIntent updatePaymentIntentFromStripe(String tenant,TaCompteServiceWebExterne configService, String paymentIntentStripeID, String connectedAccountId);
		
	
	public String creerSetupIntent(TaCompteServiceWebExterne configService) throws EJBException;
	public TaStripeSourceDTO rechercherPaymentMethod(TaCompteServiceWebExterne configService, String paymentMethodStripeID);
	
	public String attacherPayementMethodStripe(TaCompteServiceWebExterne configService, String paymentMethodId, String customerId) throws EJBException;
	public String detacherPaymentMethodStripe(TaCompteServiceWebExterne configService, String paymentMethodId) throws EJBException;
	
	public TaStripeAccount updateAccountFromStripe(TaCompteServiceWebExterne configService, String accountStripeID);
	
	public String createAccount();
	public String createAccountLink(String idAccount, String refreshUrl, String returnUrl, String param);
	public boolean liaisonBDGCompteOKChezStripe(String idAccount);
	public boolean deauthorizeStripeOAuthAccount(String clientIdSecretKey, String idAccount);
	public String autorizationTokenOAuthAccount(String authorisationCode);
	
	
	public String capture(TaCompteServiceWebExterne configService, String paymentIntentStripeID, BigDecimal amountToCapture);
	public String cancelPaymentIntent(TaCompteServiceWebExterne configService, String paymentIntentStripeID);
	

}