package fr.legrain.abonnement.stripe.service;

import java.io.Serializable;

import fr.legrain.abonnement.stripe.model.TaStripePaiementPrevu;

public class TimerInfoPaiementPrevuTenant implements Serializable{

	private static final long serialVersionUID = 7825028278078055854L;
	
	private TaStripePaiementPrevu taStripePaiementPrevu;
	private String tenant;
	
	public TimerInfoPaiementPrevuTenant(TaStripePaiementPrevu taStripePaiementPrevu, String tenant) {
		this.taStripePaiementPrevu = taStripePaiementPrevu;
		this.tenant = tenant;
	}
	
	public TaStripePaiementPrevu getTaStripePaiementPrevu() {
		return taStripePaiementPrevu;
	}
	public void setTaStripePaiementPrevu(TaStripePaiementPrevu taStripePaiementPrevu) {
		this.taStripePaiementPrevu = taStripePaiementPrevu;
	}
	public String getTenant() {
		return tenant;
	}
	public void setTenant(String tenant) {
		this.tenant = tenant;
	}
	
	

}
