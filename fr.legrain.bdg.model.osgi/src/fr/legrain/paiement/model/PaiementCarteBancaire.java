package fr.legrain.paiement.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class PaiementCarteBancaire implements Serializable {
	
	private static final long serialVersionUID = -6331681197445229637L;
	
	private static final BigDecimal pourcentageFraisPlateforme = new BigDecimal("1.0");
	private static final BigDecimal fraisFixeFraisPlateforme = BigDecimal.ZERO;
	
	private String numeroCarte;
	private int moisCarte;
	private int anneeCarte;
	private String cryptogrammeCarte;
	private String nomPorteurCarte;
	
	private String devise;
	private BigDecimal fraisPlateforme;
	
	private BigDecimal montantPaiement;
	private String descriptionPaiement;
	
	private String originePaiement; //BDG / BDG espace client / ...
	private String servicePaiement; //stripe / paypal / ...
	
	private String emailTicketRecu;
	private boolean conserverCarte = false;
	private boolean capturablePlusTard = false;
	
	private boolean compteClient = false;
	
	public PaiementCarteBancaire() {
		
	}
	
	public String getNumeroCarte() {
		return numeroCarte;
	}
	public void setNumeroCarte(String numeroCarte) {
		this.numeroCarte = numeroCarte;
	}
	
	public int getMoisCarte() {
		return moisCarte;
	}
	public void setMoisCarte(int moisCarte) {
		this.moisCarte = moisCarte;
	}
	
	public int getAnneeCarte() {
		return anneeCarte;
	}
	public void setAnneeCarte(int anneeCarte) {
		this.anneeCarte = anneeCarte;
	}
	
	public String getCryptogrammeCarte() {
		return cryptogrammeCarte;
	}
	public void setCryptogrammeCarte(String cryptogrammeCarte) {
		this.cryptogrammeCarte = cryptogrammeCarte;
	}
	
	public String getNomPorteurCarte() {
		return nomPorteurCarte;
	}
	public void setNomPorteurCarte(String nomPorteurCarte) {
		this.nomPorteurCarte = nomPorteurCarte;
	}
	
	public BigDecimal getMontantPaiement() {
		return montantPaiement;
	}
	public void setMontantPaiement(BigDecimal montant) {
		this.montantPaiement = montant;
	}
	
	public String getDescriptionPaiement() {
		return descriptionPaiement;
	}
	public void setDescriptionPaiement(String description) {
		this.descriptionPaiement = description;
	}

	public String getOriginePaiement() {
		return originePaiement;
	}

	public void setOriginePaiement(String originePaiement) {
		this.originePaiement = originePaiement;
	}

	public String getServicePaiement() {
		return servicePaiement;
	}

	public void setServicePaiement(String servicePaiement) {
		this.servicePaiement = servicePaiement;
	}

	public boolean isCompteClient() {
		return compteClient;
	}

	public void setCompteClient(boolean compteClient) {
		this.compteClient = compteClient;
	}

	public String getEmailTicketRecu() {
		return emailTicketRecu;
	}

	public void setEmailTicketRecu(String emailTicketRecu) {
		this.emailTicketRecu = emailTicketRecu;
	}

	public boolean isConserverCarte() {
		return conserverCarte;
	}

	public void setConserverCarte(boolean conserverCarte) {
		this.conserverCarte = conserverCarte;
	}

	public String getDevise() {
		return devise;
	}

	public void setDevise(String devise) {
		this.devise = devise;
	}

	public BigDecimal getFraisPlateforme() {
		return fraisPlateforme;
	}

	public void setFraisPlateforme(BigDecimal fraisPlateforme) {
		this.fraisPlateforme = fraisPlateforme;
	}
	
	public void calculFraisPlateform() {
		if(montantPaiement!=null) {
			fraisPlateforme = montantPaiement.multiply(pourcentageFraisPlateforme).divide(new BigDecimal(100));
			if(fraisFixeFraisPlateforme!=null) {
				fraisPlateforme = fraisPlateforme.add(fraisFixeFraisPlateforme);
			}
		}
	}

	public boolean isCapturablePlusTard() {
		return capturablePlusTard;
	}

	public void setCapturablePlusTard(boolean capturablePlusTard) {
		this.capturablePlusTard = capturablePlusTard;
	}

}
