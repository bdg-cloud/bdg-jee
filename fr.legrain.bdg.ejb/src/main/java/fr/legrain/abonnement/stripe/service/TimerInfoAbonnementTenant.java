package fr.legrain.abonnement.stripe.service;

import java.io.Serializable;

import fr.legrain.document.model.TaAbonnement;

public class TimerInfoAbonnementTenant implements Serializable{

	private static final long serialVersionUID = 4323240277429429068L;
	
//	private TaStripeSubscription taStripeSubscription;
	private TaAbonnement taAbonnement;
	private String tenant;
	
	public TimerInfoAbonnementTenant(TaAbonnement taAbonnement, String tenant) {
		this.taAbonnement = taAbonnement;
		this.tenant = tenant;
	}
	
//	public TaStripeSubscription getTaStripeSubscription() {
//		return taStripeSubscription;
//	}
//	public void setTaStripeSubscription(TaStripeSubscription taStripeSubscription) {
//		this.taStripeSubscription = taStripeSubscription;
//	}
	public String getTenant() {
		return tenant;
	}
	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

	public TaAbonnement getTaAbonnement() {
		return taAbonnement;
	}

	public void setTaAbonnement(TaAbonnement taAbonnement) {
		this.taAbonnement = taAbonnement;
	}
	
	

}
