package fr.legrain.tiers.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.validator.LgrHibernateValidated;

public class TaCompteBanqueDTO extends ModelObject implements java.io.Serializable {
	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getLast4() {
		return last4;
	}

	public void setLast4(String last4) {
		this.last4 = last4;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -3237640595922167364L;
	private Integer id;
	private Integer idTiers;
	private String nomBanque;
	private String compte;
	private String codeBanque;
	private String codeGuichet;
	private String cleRib;
	private String adresse1Banque;
	private String adresse2Banque;
	private String cpBanque;
	private String villeBanque;
	private String iban;
	private String codeBIC;
	private String nomCompte;
	private String titulaire;
	private Integer idTBanque;
	private String codeTBanque;
	private String cptcomptable;
	
	private String bankCode;//: 12406
	private String branchCode;//: 00023
	private String country;//: FR
	private String last4;//: 0977
	
	private Integer versionObj;
	
	public TaCompteBanqueDTO() {
		
	}
	
	public boolean estVide() {
		if(this.getIban()!=null && !this.getIban().isEmpty())return false;
		if(this.getCodeBanque()!=null && !this.getCodeBanque().isEmpty())return false;
		if(this.getCodeBIC()!=null && !this.getCodeBIC().isEmpty())return false;
		if(this.getNomBanque()!=null && !this.getNomBanque().isEmpty())return false;
		if(this.getCompte()!=null && !this.getCompte().isEmpty())return false;
		if(this.getCodeGuichet()!=null && !this.getCodeGuichet().isEmpty())return false;
		if(this.getCleRib()!=null && !this.getCleRib().isEmpty())return false;
		return true;
	}
	
	
	public Integer getIdTiers() {
		return idTiers;
	}

	public void setIdTiers(Integer ID_TIERS) {
		firePropertyChange("idTiers", this.idTiers, this.idTiers = ID_TIERS);
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer idCompteBanque) {
		firePropertyChange("id", this.id, this.id = idCompteBanque);
	}
	

	@Size(min=0, max=20)
	@LgrHibernateValidated(champBd = "code_t_banque",table = "ta_t_banque",champEntite="codeTBanque",clazz = TaCompteBanqueDTO.class)
	public String getCodeTBanque() {
		return codeTBanque;
	}
	public void setCodeTBanque(String codeTBanque) {
		firePropertyChange("codeTBanque", this.codeTBanque, this.codeTBanque = codeTBanque);
	}
	
	@LgrHibernateValidated(champBd = "nom_banque",table = "ta_compte_banque",champEntite="nomBanque",clazz = TaCompteBanqueDTO.class)
	public String getNomBanque() {
		return nomBanque;
	}
	public void setNomBanque(String nomBanque) {
		firePropertyChange("nomBanque", this.nomBanque, this.nomBanque = nomBanque);
	}
	

	@Size(min=0, max=20)
	@LgrHibernateValidated(champBd = "compte",table = "ta_compte_banque",champEntite="compte",clazz = TaCompteBanqueDTO.class)
	public String getCompte() {
		return compte;
	}
	public void setCompte(String compte) {
		firePropertyChange("compte", this.compte, this.compte = compte);
	}
	
	@NotNull
	@Size(min=1, max=20)
	@LgrHibernateValidated(champBd = "code_banque",table = "ta_compte_banque",champEntite="codeBanque",clazz = TaCompteBanqueDTO.class)
	public String getCodeBanque() {
		return codeBanque;
	}
	public void setCodeBanque(String codeBanque) {
		firePropertyChange("codeBanque", this.codeBanque, this.codeBanque = codeBanque);
	}
	

	@Size(min=0, max=20)
	@LgrHibernateValidated(champBd = "code_guichet",table = "ta_compte_banque",champEntite="codeGuichet",clazz = TaCompteBanqueDTO.class)
	public String getCodeGuichet() {
		return codeGuichet;
	}
	public void setCodeGuichet(String codeGuichet) {
		firePropertyChange("codeGuichet", this.codeGuichet, this.codeGuichet = codeGuichet);
	}
	

	@Size(min=0, max=2)
	@LgrHibernateValidated(champBd = "cle_rib",table = "ta_compte_banque",champEntite="cleRib",clazz = TaCompteBanqueDTO.class)
	public String getCleRib() {
		return cleRib;
	}
	public void setCleRib(String cleRib) {
		firePropertyChange("cleRib", this.cleRib, this.cleRib = cleRib);
	}
	
	@LgrHibernateValidated(champBd = "adresse1_banque",table = "ta_compte_banque",champEntite="adresse1Banque",clazz = TaCompteBanqueDTO.class)
	public String getAdresse1Banque() {
		return adresse1Banque;
	}
	public void setAdresse1Banque(String adresse1Banque) {
		firePropertyChange("adresse1Banque", this.adresse1Banque, this.adresse1Banque = adresse1Banque);
	}
	
	@LgrHibernateValidated(champBd = "adresse2_banque",table = "ta_compte_banque",champEntite="adresse2Banque",clazz = TaCompteBanqueDTO.class)
	public String getAdresse2Banque() {
		return adresse2Banque;
	}
	public void setAdresse2Banque(String adresse2Banque) {
		firePropertyChange("adresse2Banque", this.adresse2Banque, this.adresse2Banque = adresse2Banque);
	}
	

	@Size(min=0, max=8)
	@LgrHibernateValidated(champBd = "cp_banque",table = "ta_compte_banque",champEntite="cpBanque",clazz = TaCompteBanqueDTO.class)
	public String getCpBanque() {
		return cpBanque;
	}
	public void setCpBanque(String cpBanque) {
		firePropertyChange("cpBanque", this.cpBanque, this.cpBanque = cpBanque);
	}
	
	@LgrHibernateValidated(champBd = "ville_banque",table = "ta_compte_banque",champEntite="villeBanque",clazz = TaCompteBanqueDTO.class)
	public String getVilleBanque() {
		return villeBanque;
	}
	public void setVilleBanque(String villeBanque) {
		firePropertyChange("villeBanque", this.villeBanque, this.villeBanque = villeBanque);
	}
	
	@LgrHibernateValidated(champBd = "iban",table = "ta_compte_banque",champEntite="iban",clazz = TaCompteBanqueDTO.class)
	public String getIban() {
		return iban;
	}
	public void setIban(String iban) {
		firePropertyChange("iban", this.iban, this.iban = iban);
	}
	
	@Size(min=0, max=20)
	@LgrHibernateValidated(champBd = "code_b_i_c",table = "ta_compte_banque",champEntite="codeBIC",clazz = TaCompteBanqueDTO.class)
	public String getCodeBIC() {
		return codeBIC;
	}
	public void setCodeBIC(String codeBIC) {
		firePropertyChange("codeBIC", this.codeBIC, this.codeBIC = codeBIC);
	}
	
	@LgrHibernateValidated(champBd = "titulaire",table = "ta_compte_banque",champEntite="titulaire",clazz = TaCompteBanqueDTO.class)
	public String getTitulaire() {
		return titulaire;
	}
	public void setTitulaire(String titulaire) {
		firePropertyChange("titulaire", this.titulaire, this.titulaire = titulaire);
	}

	public Integer getIdTBanque() {
		return idTBanque;
	}

	public void setIdTBanque(Integer ID_T_BANQUE) {
		firePropertyChange("idTBanque", this.idTBanque, this.idTBanque = ID_T_BANQUE);
	}
	

	@Size(min=0, max=8)
	@LgrHibernateValidated(champBd = "cptcomptable",table = "ta_compte_banque",champEntite="cptcomptable",clazz = TaCompteBanqueDTO.class)
	public String getCptcomptable() {
		return cptcomptable;
	}
	public void setCptcomptable(String cptcomptable) {
		firePropertyChange("cptcomptable", this.cptcomptable, this.cptcomptable = cptcomptable);
	}
	
	@LgrHibernateValidated(champBd = "nom_compte",table = "ta_compte_banque",champEntite="nomCompte",clazz = TaCompteBanqueDTO.class)
	public String getNomCompte() {
		return nomCompte;
	}
	public void setNomCompte(String nomCompte) {
		firePropertyChange("nomCompte", this.nomCompte, this.nomCompte = nomCompte);
	}
	
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}
	
	public static TaCompteBanqueDTO copy(TaCompteBanqueDTO taCompteBanqueDTO){
		TaCompteBanqueDTO swtCompteBanqueLoc = new TaCompteBanqueDTO();
		swtCompteBanqueLoc.setId(taCompteBanqueDTO.getId());                //1
		swtCompteBanqueLoc.setIdTiers(taCompteBanqueDTO.getIdTiers());                //1
		swtCompteBanqueLoc.setNomBanque(taCompteBanqueDTO.getNomBanque());                //1
		swtCompteBanqueLoc.setCompte(taCompteBanqueDTO.getCompte());                //1
		swtCompteBanqueLoc.setCodeBanque(taCompteBanqueDTO.getCodeBanque());                //1
		swtCompteBanqueLoc.setCodeGuichet(taCompteBanqueDTO.getCodeGuichet());                //1
		swtCompteBanqueLoc.setCleRib(taCompteBanqueDTO.getCleRib());                //1
		swtCompteBanqueLoc.setAdresse1Banque(taCompteBanqueDTO.getAdresse1Banque());                //1
		swtCompteBanqueLoc.setAdresse2Banque(taCompteBanqueDTO.getAdresse2Banque());                //1
		swtCompteBanqueLoc.setCpBanque(taCompteBanqueDTO.getCpBanque());                //1
		swtCompteBanqueLoc.setVilleBanque(taCompteBanqueDTO.getVilleBanque());                //1
		swtCompteBanqueLoc.setIban(taCompteBanqueDTO.getIban());                //1
		swtCompteBanqueLoc.setCodeBIC(taCompteBanqueDTO.getCodeBIC());                //1
		swtCompteBanqueLoc.setNomCompte(taCompteBanqueDTO.getNomCompte()); 
		swtCompteBanqueLoc.setTitulaire(taCompteBanqueDTO.getTitulaire());                //1
		swtCompteBanqueLoc.setIdTBanque(taCompteBanqueDTO.getIdTBanque());                //1
		swtCompteBanqueLoc.setCodeTBanque(taCompteBanqueDTO.getCodeTBanque()); 
		swtCompteBanqueLoc.setCptcomptable(taCompteBanqueDTO.getCptcomptable());//1
		return swtCompteBanqueLoc;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((adresse1Banque == null) ? 0 : adresse1Banque.hashCode());
		result = prime * result
				+ ((adresse2Banque == null) ? 0 : adresse2Banque.hashCode());
		result = prime * result + ((cleRib == null) ? 0 : cleRib.hashCode());
		result = prime * result + ((codeBIC == null) ? 0 : codeBIC.hashCode());
		result = prime * result
				+ ((codeBanque == null) ? 0 : codeBanque.hashCode());
		result = prime * result
				+ ((codeGuichet == null) ? 0 : codeGuichet.hashCode());
		result = prime * result
				+ ((codeTBanque == null) ? 0 : codeTBanque.hashCode());
		result = prime * result + ((compte == null) ? 0 : compte.hashCode());
		result = prime * result
				+ ((cpBanque == null) ? 0 : cpBanque.hashCode());
		result = prime * result
				+ ((cptcomptable == null) ? 0 : cptcomptable.hashCode());
		result = prime * result + ((iban == null) ? 0 : iban.hashCode());
		result = prime * result
				+ ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((idTBanque == null) ? 0 : idTBanque.hashCode());
		result = prime * result + ((idTiers == null) ? 0 : idTiers.hashCode());
		result = prime * result
				+ ((nomBanque == null) ? 0 : nomBanque.hashCode());
		result = prime * result
				+ ((titulaire == null) ? 0 : titulaire.hashCode());
		result = prime * result
				+ ((villeBanque == null) ? 0 : villeBanque.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaCompteBanqueDTO other = (TaCompteBanqueDTO) obj;
		if (adresse1Banque == null) {
			if (other.adresse1Banque != null)
				return false;
		} else if (!adresse1Banque.equals(other.adresse1Banque))
			return false;
		if (adresse2Banque == null) {
			if (other.adresse2Banque != null)
				return false;
		} else if (!adresse2Banque.equals(other.adresse2Banque))
			return false;
		if (cleRib == null) {
			if (other.cleRib != null)
				return false;
		} else if (!cleRib.equals(other.cleRib))
			return false;
		if (codeBIC == null) {
			if (other.codeBIC != null)
				return false;
		} else if (!codeBIC.equals(other.codeBIC))
			return false;
		if (codeBanque == null) {
			if (other.codeBanque != null)
				return false;
		} else if (!codeBanque.equals(other.codeBanque))
			return false;
		if (codeGuichet == null) {
			if (other.codeGuichet != null)
				return false;
		} else if (!codeGuichet.equals(other.codeGuichet))
			return false;
		if (codeTBanque == null) {
			if (other.codeTBanque != null)
				return false;
		} else if (!codeTBanque.equals(other.codeTBanque))
			return false;
		if (compte == null) {
			if (other.compte != null)
				return false;
		} else if (!compte.equals(other.compte))
			return false;
		if (cpBanque == null) {
			if (other.cpBanque != null)
				return false;
		} else if (!cpBanque.equals(other.cpBanque))
			return false;
		if (cptcomptable == null) {
			if (other.cptcomptable != null)
				return false;
		} else if (!cptcomptable.equals(other.cptcomptable))
			return false;
		if (iban == null) {
			if (other.iban != null)
				return false;
		} else if (!iban.equals(other.iban))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (idTBanque == null) {
			if (other.idTBanque != null)
				return false;
		} else if (!idTBanque.equals(other.idTBanque))
			return false;
		if (idTiers == null) {
			if (other.idTiers != null)
				return false;
		} else if (!idTiers.equals(other.idTiers))
			return false;
		if (nomBanque == null) {
			if (other.nomBanque != null)
				return false;
		} else if (!nomBanque.equals(other.nomBanque))
			return false;
		if (titulaire == null) {
			if (other.titulaire != null)
				return false;
		} else if (!titulaire.equals(other.titulaire))
			return false;
		if (villeBanque == null) {
			if (other.villeBanque != null)
				return false;
		} else if (!villeBanque.equals(other.villeBanque))
			return false;
		return true;
	}




}
