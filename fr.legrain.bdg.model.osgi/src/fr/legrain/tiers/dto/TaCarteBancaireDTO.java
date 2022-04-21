package fr.legrain.tiers.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.validator.LgrHibernateValidated;

public class TaCarteBancaireDTO extends ModelObject implements java.io.Serializable {
	
	private static final long serialVersionUID = -3237640595922167364L;
	private Integer id;
	private Integer idTiers;
	private String nomBanque;
	
	
	private String nomProprietaire;// :No name provided
	private String numeroCarte; //Number :•••• 9363
	private String empreinte;//Fingerprint : 4SZKQRQv6IJpjeV7
	private int moisExpiration;//Expires : 2 / 2020
	private int anneeExpiration;
	private String type;//Type : MasterCard credit card
	private String paysOrigine;//Origine : France
	private String last4;
	private String cvc;//CVC : 777
	
	private String adresse1;
	private String adresse2;
	private String codePostal;
	private String ville;
	private String pays;
	

	private String nomCompte;
	private Integer idTBanque;
	private String codeTBanque;
	private String cptcomptable;
	
	private Integer versionObj;
	
	public boolean estVide() {
		if(this.getNumeroCarte()!=null && !this.getNumeroCarte().isEmpty())return false;
		if(this.getLast4()!=null && !this.getLast4().isEmpty())return false;
		
		return true;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdTiers() {
		return idTiers;
	}

	public void setIdTiers(Integer idTiers) {
		this.idTiers = idTiers;
	}

	public String getNomBanque() {
		return nomBanque;
	}

	public void setNomBanque(String nomBanque) {
		this.nomBanque = nomBanque;
	}

	public String getNomProprietaire() {
		return nomProprietaire;
	}

	public void setNomProprietaire(String nomProprietaire) {
		this.nomProprietaire = nomProprietaire;
	}

	public String getNumeroCarte() {
		return numeroCarte;
	}

	public void setNumeroCarte(String numeroCarte) {
		this.numeroCarte = numeroCarte;
	}

	public String getEmpreinte() {
		return empreinte;
	}

	public void setEmpreinte(String empreinte) {
		this.empreinte = empreinte;
	}

	public int getMoisExpiration() {
		return moisExpiration;
	}

	public void setMoisExpiration(int moisExpiration) {
		this.moisExpiration = moisExpiration;
	}

	public int getAnneeExpiration() {
		return anneeExpiration;
	}

	public void setAnneeExpiration(int anneeExpiration) {
		this.anneeExpiration = anneeExpiration;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPaysOrigine() {
		return paysOrigine;
	}

	public void setPaysOrigine(String paysOrigine) {
		this.paysOrigine = paysOrigine;
	}

	public String getLast4() {
		return last4;
	}

	public void setLast4(String last4) {
		this.last4 = last4;
	}

	public String getCvc() {
		return cvc;
	}

	public void setCvc(String cvc) {
		this.cvc = cvc;
	}

	public String getAdresse1() {
		return adresse1;
	}

	public void setAdresse1(String adresse1) {
		this.adresse1 = adresse1;
	}

	public String getAdresse2() {
		return adresse2;
	}

	public void setAdresse2(String adrsse2) {
		this.adresse2 = adrsse2;
	}

	public String getCodePostal() {
		return codePostal;
	}

	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public String getPays() {
		return pays;
	}

	public void setPays(String pays) {
		this.pays = pays;
	}

	public String getNomCompte() {
		return nomCompte;
	}

	public void setNomCompte(String nomCompte) {
		this.nomCompte = nomCompte;
	}

	public Integer getIdTBanque() {
		return idTBanque;
	}

	public void setIdTBanque(Integer idTBanque) {
		this.idTBanque = idTBanque;
	}

	public String getCodeTBanque() {
		return codeTBanque;
	}

	public void setCodeTBanque(String codeTBanque) {
		this.codeTBanque = codeTBanque;
	}

	public String getCptcomptable() {
		return cptcomptable;
	}

	public void setCptcomptable(String cptcomptable) {
		this.cptcomptable = cptcomptable;
	}

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	
	
	public TaCarteBancaireDTO() {
		
	}
	
}
