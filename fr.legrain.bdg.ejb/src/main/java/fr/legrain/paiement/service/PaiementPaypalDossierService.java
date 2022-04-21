package fr.legrain.paiement.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;
import javax.ejb.Stateless;

import org.json.JSONObject;

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
//import fr.legrain.abonnement.stripe.model.TaStripeSubscription;
import fr.legrain.bdg.paiement.service.remote.IPaiementEnLigneDossierService;
import fr.legrain.document.dto.IDocumentPayableCB;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaPanier;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TaTicketDeCaisse;
import fr.legrain.paiement.model.PaiementCarteBancaire;
import fr.legrain.paiement.model.RetourPaiementCarteBancaire;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.tiers.model.TaTiers;

@Stateless
@PaiementLgr(PaiementLgr.TYPE_PAYPAL)
public class PaiementPaypalDossierService implements IPaiementEnLigneDossierService {
	
	
//	@Override
//	public String payer(BigDecimal montant, String numeroCarte, int moisCarte, int anneeCarte, String cryptogrammeCarte, String description) throws EJBException{
//		return payer(montant, numeroCarte,moisCarte,anneeCarte,cryptogrammeCarte,null,description);
//	}
//	
//	@Override
//	public String payer(BigDecimal montant, String numeroCarte, int moisCarte, int anneeCarte, String cryptogrammeCarte, String nomPorteurCarte, String description) throws EJBException{
//		System.out.println("PaiementPaypalDossierService.payer()");
//		return "paypal";
//	}

	@Override
	public RetourPaiementCarteBancaire payer(TaCompteServiceWebExterne configService,
			PaiementCarteBancaire paiementCarteBancaire, TaTiers taTiers, String libelle) throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RetourPaiementCarteBancaire payer(TaCompteServiceWebExterne configService,
			PaiementCarteBancaire paiementCarteBancaire, IDocumentPayableCB taFacture, TaTiers taTiers, String libelle)
			throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RetourPaiementCarteBancaire payer(TaCompteServiceWebExterne configService,
			PaiementCarteBancaire paiementCarteBancaire, IDocumentPayableCB taFacture, String libelle)
			throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String creerCarteStripe(TaCompteServiceWebExterne configService, BigDecimal montant, String numeroCarte,
			int moisCarte, int anneeCarte, String cryptogrammeCarte, String nomPorteurCarte, String description)
			throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String creerCarteStripe(TaCompteServiceWebExterne configService, String carteTokenStripe)
			throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String creerSourcePrelevementSEPA(TaCompteServiceWebExterne configService, String iban,
			String nomPersonneAPrelever) throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String creerCustomerStripe(TaCompteServiceWebExterne configService, String email, String sourceId,
			String description) throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String attacherSourceStripe(TaCompteServiceWebExterne configService, String sourceId, String customerId)
			throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String detacherSourceStripe(TaCompteServiceWebExterne configService, String sourceId) throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String payerAvecSourceStripe(TaCompteServiceWebExterne configService, BigDecimal montant,
			String customerStripeID, String sourceStripeID, String description) throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String payerParPrelevementSEPA(TaCompteServiceWebExterne configService, BigDecimal montant,
			String customerStripeID, String sourceStripeID) throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String creerWebhook(TaCompteServiceWebExterne configService, String url, String enabledEvents)
			throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String creerCouponStripe(TaCompteServiceWebExterne configService, String duration, Integer durationInMonths,
			String id, BigDecimal percentOff) throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String creerInvoiceItemStripe(TaCompteServiceWebExterne configService, String customerStripeID,
			String description, BigDecimal montant) throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String creerInvoiceStripe(TaCompteServiceWebExterne configService, String customerStripeID)
			throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String creerPlanStripe(TaCompteServiceWebExterne configService, String nickname, String interval,
			String currency, BigDecimal montant, String productStripeID) throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String creerProductStripe(TaCompteServiceWebExterne configService, String name, String type)
			throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaStripeCustomerDTO rechercherCustomer(TaCompteServiceWebExterne configService, String customerStripeID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaStripePlanDTO rechercherPlan(TaCompteServiceWebExterne configService, String planStripeID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaStripeProductDTO rechercherProduct(TaCompteServiceWebExterne configService, String productStripeID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaStripeSourceDTO rechercherSource(TaCompteServiceWebExterne configService, String sourceStripeID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaStripeInvoiceDTO rechercherInvoice(TaCompteServiceWebExterne configService, String invoiceStripeID) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public TaStripeSubscriptionDTO rechercherSubscription(TaCompteServiceWebExterne configService,
//			String subscriptionStripeID) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public Boolean supprimerPlan(TaCompteServiceWebExterne configService, String planStripeID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean supprimerProduct(TaCompteServiceWebExterne configService, String productStripeID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean supprimerCustomer(TaCompteServiceWebExterne configService, String customerStripeID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean supprimerCoupon(TaCompteServiceWebExterne configService, String couponStripeID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaStripePlanDTO updatePlan(TaCompteServiceWebExterne configService, String planStripeID, String nickname,
			Boolean active) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaStripeProductDTO updateProduct(TaCompteServiceWebExterne configService, String productStripeID,
			String name, Boolean active) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaStripeCustomerDTO updateCustomer(TaCompteServiceWebExterne configService, String customerStripeID,
			String email, String sourceDefaultId, String description, String couponId, String invoicePrefix) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaStripeCouponDTO updateCoupon(TaCompteServiceWebExterne configService, String couponStripeID, String name) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public TaStripeSubscriptionDTO updateSubscription(TaCompteServiceWebExterne configService,
//			String subscriptionStripeID, String billing, String billing_cycle_anchor, String coupon,
//			String days_until_due, String default_source) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public String refundCharge(TaCompteServiceWebExterne configService, String chargeStripeID, BigDecimal montant) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String creerSubscriptionStripe(TaCompteServiceWebExterne configService, String customerStripeID,
			Map<String, Integer> mapSubscriptionItemPlanStripeIDQuantity, String sourceStripeID,
			Integer timpeStampBillingCycleAnchor, String billing, Integer daysUntilDue) throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String annulerSubscriptionStripe(TaCompteServiceWebExterne configService, String subscriptionStripeID)
			throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public List<TaStripeSubscriptionDTO> rechercheSubscriptionStripeForCustomer(TaCompteServiceWebExterne configService,
//			String customerStripeID, Integer limite, String status) throws EJBException {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public TaStripeInvoiceDTO payerInvoice(TaCompteServiceWebExterne configService, String invoiceStripeID,
			Boolean paidOutOfBand) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaStripeInvoiceDTO upcomingInvoiceForCustomer(TaCompteServiceWebExterne configService,
			String customerStripeID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaStripeInvoiceDTO> rechercheInvoiceStripeForSubscription(TaCompteServiceWebExterne configService,
			String subscriptionStripeID, Integer limite) throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaStripeInvoiceDTO> rechercheInvoiceStripeForCustomer(TaCompteServiceWebExterne configService,
			String customerStripeID, Integer limite) throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaStripeChargeDTO> rechercheChargeStripe(TaCompteServiceWebExterne configService, Integer limite,
			Map<String, String> sourceType) throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaStripeChargeDTO> rechercheChargeStripeForCustomer(TaCompteServiceWebExterne configService,
			String customerStripeID, Integer limite, Map<String, String> sourceType) throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaStripePlanDTO> recherchePlanStripe(TaCompteServiceWebExterne configService, Integer limite,
			Boolean active) throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaStripePlanDTO> recherchePlanStripeForProduct(TaCompteServiceWebExterne configService,
			String productStripeID, Integer limite, Boolean active) throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaStripePlan updatePlanFromStripe(TaCompteServiceWebExterne configService, String planStripeID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaStripeInvoice updateInvoiceFromStripe(TaCompteServiceWebExterne configService, String invoiceStripeID,
			String subscriptionStripeID) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public TaStripeSubscription updateSubscriptionFromStripe(TaCompteServiceWebExterne configService,
//			String subscriptionStripeID) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public TaStripeCharge updateChargeFromStripe(TaCompteServiceWebExterne configService, String chargeStripeID, String connectedAccountId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String payerAvecSourceStripe(TaCompteServiceWebExterne configService, BigDecimal montant,
			String customerStripeID, String sourceStripeID, String description, IDocumentPayableCB documentPayableCB,
			TaTiers taTiers, String originePaiement) throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String creerPaymentIntent(TaCompteServiceWebExterne configService,
			PaiementCarteBancaire paiementCarteBancaire, TaTiers taTiers, String libelle) throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String creerPaymentIntent(TaCompteServiceWebExterne configService,
			PaiementCarteBancaire paiementCarteBancaire, IDocumentPayableCB documentPayableCB, String libelle)
			throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String creerPaymentIntent(TaCompteServiceWebExterne configService,
			PaiementCarteBancaire paiementCarteBancaire, IDocumentPayableCB documentPayableCB, TaTiers taTiers,
			String libelle) throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public RetourPaiementCarteBancaire validerPaymentIntent(TaCompteServiceWebExterne configService,
			String paymentIntentStripeID, String connectedAccountId) throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String creerSetupIntent(TaCompteServiceWebExterne configService) throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaStripeSourceDTO rechercherPaymentMethod(TaCompteServiceWebExterne configService,
			String paymentMethodStripeID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String attacherPayementMethodStripe(TaCompteServiceWebExterne configService, String paymentMethodId,
			String customerId) throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String detacherPaymentMethodStripe(TaCompteServiceWebExterne configService, String paymentMethodId)
			throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void ajouterAdresseEmailPourRecu(TaCompteServiceWebExterne compte, String paymentIntentId,
			String adresseEmailTicketRecu) {
		// TODO Auto-generated method stub
		
	}

	@Override
	/**
	 * @deprecated
	 */
	public String creerPaymentIntent(TaCompteServiceWebExterne configService,
			PaiementCarteBancaire paiementCarteBancaire, TaAvisEcheance taAvisEcheance, String libelle)
			throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RetourPaiementCarteBancaire validerPaymentIntent(String tenant, TaCompteServiceWebExterne configService,
			String paymentIntentStripeID, String connectedAccountId) throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaStripePaymentIntent validerPaymentIntentReglement(TaCompteServiceWebExterne configService,
			String paymentIntentStripeID, String connectedAccountId) throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaStripePaymentIntent validerPaymentIntentReglement(String tenant, TaCompteServiceWebExterne configService,
			String paymentIntentStripeID, String connectedAccountId) throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDocumentPayableCB validerPaymentIntentGenerationDoc(String tenant,
			TaStripePaymentIntent paymentIntentStripe) throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaStripePaymentIntent validerPaymentIntentAffectation(String tenant, IDocumentPayableCB doc,TaReglement taReglement,
			TaStripePaymentIntent paymentIntentStripe) throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String creerPaymentIntent(TaCompteServiceWebExterne configService,
			PaiementCarteBancaire paiementCarteBancaire, TaPanier taPanier, String libelle) throws EJBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaStripeAccount updateAccountFromStripe(TaCompteServiceWebExterne configService, String accountStripeID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createAccount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createAccountLink(String idAccount, String refreshUrl, String returnUrl, String param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean liaisonBDGCompteOKChezStripe(String idAccount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deauthorizeStripeOAuthAccount(String clientIdSecretKey, String idAccount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String autorizationTokenOAuthAccount(String authorisationCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String capture(TaCompteServiceWebExterne configService, String paymentIntentStripeID,
			BigDecimal amountToCapture) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String cancelPaymentIntent(TaCompteServiceWebExterne configService, String paymentIntentStripeID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaStripePaymentIntent updatePaymentIntentFromStripe(TaCompteServiceWebExterne configService,
			String paymentIntentStripeID, String connectedAccountId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaStripePaymentIntent updatePaymentIntentFromStripe(String tenant, TaCompteServiceWebExterne configService,
			String paymentIntentStripeID, String connectedAccountId) {
		// TODO Auto-generated method stub
		return null;
	}

}
