package fr.legrain.document.dto;


import java.util.Date;

import fr.legrain.bdg.model.ModelObject;

public class TaRelanceDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = -1077350807674480608L;
	
	private Integer id;
	private String codeRelance;
	private Date dateRelance;
	private String codeTiers;
	private Date dateDebut;
	private Date dateFin;
	private Integer etat = 0;
	private Integer versionObj;
	
	public TaRelanceDTO() {
	}

	public void setIHMLRelance(TaRelanceDTO ihmLRelance) {
		setCodeRelance(ihmLRelance.codeRelance) ;
		setIdRelance(ihmLRelance.id) ;
		setDateRelance(ihmLRelance.dateRelance);
		setCodeTiers(ihmLRelance.codeTiers);
		setEtat(ihmLRelance.etat);		
		setDateDebut(ihmLRelance.dateDebut);
		setDateFin(ihmLRelance.dateFin);
	}
	
	static public TaRelanceDTO copy(TaRelanceDTO ihmLRelance) {
		TaRelanceDTO swtLRelanceLoc = new TaRelanceDTO();
		swtLRelanceLoc.setCodeRelance(ihmLRelance.codeRelance);
		swtLRelanceLoc.setIdRelance(ihmLRelance.id);
		swtLRelanceLoc.setDateRelance(ihmLRelance.dateRelance);
		swtLRelanceLoc.setCodeTiers(ihmLRelance.codeTiers);
		swtLRelanceLoc.setEtat(ihmLRelance.etat);
		swtLRelanceLoc.setDateDebut(ihmLRelance.dateDebut);
		swtLRelanceLoc.setDateFin(ihmLRelance.dateFin);
		return swtLRelanceLoc;
	}



	public String getCodeRelance() {
		return codeRelance;
	}


	public void setCodeRelance(String codeRelance) {
		firePropertyChange("codeRelance", this.codeRelance, this.codeRelance = codeRelance);
	}


	public Integer getIdRelance() {
		return id;
	}


	public void setIdRelance(Integer idRelance) {
		firePropertyChange("idRelance", this.id, this.id = idRelance);
	}


	public Date getDateRelance() {
		return dateRelance;
	}


	public void setDateRelance(Date dateEcheance) {
		firePropertyChange("dateEcheance", this.dateRelance, this.dateRelance = dateEcheance);
	}


	public String getCodeTiers() {
		return codeTiers;
	}


	public void setCodeTiers(String codeTiers) {
		firePropertyChange("codeTiers", this.codeTiers, this.codeTiers = codeTiers);
	}


	public Integer getEtat() {
		return etat;
	}


	public void setEtat(Integer etat) {
		firePropertyChange("etat", this.etat, this.etat = etat);
	}
	
	
	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		firePropertyChange("dateDebut", this.dateDebut, this.dateDebut = dateDebut);
	}
	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		firePropertyChange("dateFin", this.dateFin, this.dateFin = dateFin);
	}







	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codeRelance == null) ? 0 : codeRelance.hashCode());
		result = prime * result
				+ ((codeTiers == null) ? 0 : codeTiers.hashCode());
		result = prime * result
				+ ((dateDebut == null) ? 0 : dateDebut.hashCode());
		result = prime * result + ((dateFin == null) ? 0 : dateFin.hashCode());
		result = prime * result
				+ ((dateRelance == null) ? 0 : dateRelance.hashCode());
		result = prime * result + ((etat == null) ? 0 : etat.hashCode());
		result = prime * result
				+ ((id == null) ? 0 : id.hashCode());
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
		TaRelanceDTO other = (TaRelanceDTO) obj;
		if (codeRelance == null) {
			if (other.codeRelance != null)
				return false;
		} else if (!codeRelance.equals(other.codeRelance))
			return false;
		if (codeTiers == null) {
			if (other.codeTiers != null)
				return false;
		} else if (!codeTiers.equals(other.codeTiers))
			return false;
		if (dateDebut == null) {
			if (other.dateDebut != null)
				return false;
		} else if (!dateDebut.equals(other.dateDebut))
			return false;
		if (dateFin == null) {
			if (other.dateFin != null)
				return false;
		} else if (!dateFin.equals(other.dateFin))
			return false;
		if (dateRelance == null) {
			if (other.dateRelance != null)
				return false;
		} else if (!dateRelance.equals(other.dateRelance))
			return false;
		if (etat == null) {
			if (other.etat != null)
				return false;
		} else if (!etat.equals(other.etat))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}







	public Integer getId() {
		return id;
	}







	public void setId(Integer id) {
		this.id = id;
	}







	public Integer getVersionObj() {
		return versionObj;
	}







	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}



}
