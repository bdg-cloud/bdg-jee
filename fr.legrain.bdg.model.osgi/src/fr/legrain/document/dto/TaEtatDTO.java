package fr.legrain.document.dto;

import fr.legrain.bdg.model.ModelObject;

public class TaEtatDTO extends ModelObject implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1198140668643469959L;

	/**		Extrait de BdDs
	* 	id_etat serial NOT NULL,
	  	code_etat character varying(20) DEFAULT ''::character varying,
	  	libelle_etat character varying(255) DEFAULT ''::character varying,
	  	qui_cree character varying(50) DEFAULT ''::character varying,
		quand_cree timestamp with time zone DEFAULT now(),
		qui_modif character varying(50) DEFAULT ''::character varying,
		quand_modif timestamp with time zone DEFAULT now(),
		version character varying(20) DEFAULT ''::character varying,
		ip_acces character varying(50) DEFAULT '0'::character varying,
		version_obj integer DEFAULT 0,
		CONSTRAINT ta_etat_pkey PRIMARY KEY (id_etat)
	 */
	
	private Integer idEtat;
	private String codeEtat;
	private String liblEtat;
	
	private Integer versionObj;

	/**
	 * @param idEtat
	 * @param codeEtat
	 * @param libelleEtat
	 * @param versionObj
	 */
	public TaEtatDTO(Integer idEtat, String codeEtat, String liblEtat,
			Integer versionObj) {
		super();
		this.idEtat = idEtat;
		this.codeEtat = codeEtat;
		this.liblEtat = liblEtat;
		this.versionObj = versionObj;
	}
	
	/**
	 * Constructeur sans paramentres
	 */
	public TaEtatDTO() {}

	/**
	 * copie d'objet
	 */
	public static TaEtatDTO copy(TaEtatDTO swtEtat){
		TaEtatDTO swtEtatLoc = new TaEtatDTO();
		swtEtatLoc.setCodeEtat(swtEtat.codeEtat);
		swtEtatLoc.setIdEtat(swtEtat.idEtat);
		swtEtatLoc.setLiblEtat(swtEtat.liblEtat);
		return swtEtatLoc;
	}
	
	/**
	 * Setter d'objet
	 */
	public void setTaEtatDTO(TaEtatDTO swtEtat){
		this.idEtat = swtEtat.idEtat;
		this.codeEtat = swtEtat.codeEtat;
		this.liblEtat = swtEtat.liblEtat;
	}
	
	/**
	 * @return the idEtat
	 */
	public Integer getIdEtat() {
		return idEtat;
	}

	/**
	 * @param idEtat the idEtat to set
	 */
	public void setIdEtat(Integer idEtat) {
		this.idEtat = idEtat;
	}

	/**
	 * @return the codeEtat
	 */
	public String getCodeEtat() {
		return codeEtat;
	}

	/**
	 * @param codeEtat the codeEtat to set
	 */
	public void setCodeEtat(String codeEtat) {
		this.codeEtat = codeEtat;
	}

	/**
	 * @return the libelleEtat
	 */
	public String getLiblEtat() {
		return liblEtat;
	}

	/**
	 * @param liblEtat the liblEtat to set
	 */
	public void setLiblEtat(String liblEtat) {
		this.liblEtat = liblEtat;
	}

	/**
	 * @return the versionObj
	 */
	public Integer getVersionObj() {
		return versionObj;
	}

	/**
	 * @param versionObj the versionObj to set
	 */
	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codeEtat == null) ? 0 : codeEtat.hashCode());
		result = prime * result + ((idEtat == null) ? 0 : idEtat.hashCode());
		result = prime * result
				+ ((liblEtat == null) ? 0 : liblEtat.hashCode());
		result = prime * result
				+ ((versionObj == null) ? 0 : versionObj.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaEtatDTO other = (TaEtatDTO) obj;
		if (codeEtat == null) {
			if (other.codeEtat != null)
				return false;
		} else if (!codeEtat.equals(other.codeEtat))
			return false;
		if (idEtat == null) {
			if (other.idEtat != null)
				return false;
		} else if (!idEtat.equals(other.idEtat))
			return false;
		if (liblEtat == null) {
			if (other.liblEtat != null)
				return false;
		} else if (!liblEtat.equals(other.liblEtat))
			return false;
		if (versionObj == null) {
			if (other.versionObj != null)
				return false;
		} else if (!versionObj.equals(other.versionObj))
			return false;
		return true;
	}
	
	
}
