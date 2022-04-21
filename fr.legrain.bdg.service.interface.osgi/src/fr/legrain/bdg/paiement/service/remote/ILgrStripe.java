package fr.legrain.bdg.paiement.service.remote;

import java.math.BigDecimal;

import javax.ejb.EJBException;

//import com.stripe.model.Charge;

public interface ILgrStripe {

	public String payer(BigDecimal montant, String numeroCarte, int moisCarte,
			int anneeCarte, String cryptogrammeCarte, String nomPorteurCarte, String Description)
			throws EJBException;
	
	public String payer(BigDecimal montant, String numeroCarte, int moisCarte,
			int anneeCarte, String cryptogrammeCarte, String Description)
			throws EJBException;

//	public Charge remontePaiement(String idPaiement);
	
	
	
}