package fr.legrain.bdg.webapp.paiement;

import java.io.Serializable;

import fr.legrain.abonnement.stripe.model.TaStripeCustomer;
import fr.legrain.abonnement.stripe.model.TaStripeSource;
import fr.legrain.tiers.model.TaCarteBancaire;
import fr.legrain.tiers.model.TaCompteBanque;
import fr.legrain.tiers.model.TaTiers;

public class CreerSourceStripeParam implements Serializable{
	
	public static final String NOM_OBJET_EN_SESSION = "paramCreerSourceStripe";
	
	private TaStripeSource taStripeSource;
	private TaStripeCustomer taStripeCustomer;
	private TaCarteBancaire taCarteBancaire;
	private TaCompteBanque taCompteBanque;
	private TaTiers taTiers;

	private String libelle;

	public TaStripeSource getTaStripeSource() {
		return taStripeSource;
	}

	public void setTaStripeSource(TaStripeSource taStripeSource) {
		this.taStripeSource = taStripeSource;
	}

	public TaStripeCustomer getTaStripeCustomer() {
		return taStripeCustomer;
	}

	public void setTaStripeCustomer(TaStripeCustomer taStripeCustomer) {
		this.taStripeCustomer = taStripeCustomer;
	}

	public TaCarteBancaire getTaCarteBancaire() {
		return taCarteBancaire;
	}

	public void setTaCarteBancaire(TaCarteBancaire taCarteBancaire) {
		this.taCarteBancaire = taCarteBancaire;
	}

	public TaCompteBanque getTaCompteBanque() {
		return taCompteBanque;
	}

	public void setTaCompteBanque(TaCompteBanque taCompteBanque) {
		this.taCompteBanque = taCompteBanque;
	}

	public TaTiers getTaTiers() {
		return taTiers;
	}

	public void setTaTiers(TaTiers taTiers) {
		this.taTiers = taTiers;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	
}
