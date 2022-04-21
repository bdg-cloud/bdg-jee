package fr.legrain.servicewebexterne.dto;




import java.util.Date;

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.bdg.model.ModelObject;
import fr.legrain.tiers.dto.TaTiersDTO;

public class TaSynchroServiceExterneDTO  extends ModelObject implements java.io.Serializable{





	/**
	 * 
	 */
	private static final long serialVersionUID = -4458860403726469191L;

	private Integer id;
	
	private Date derniereSynchro;
	
	private String typeEntite;
	private Integer idCompteServiceExterne;
	private String codeCompteServiceExterne;
	private String libelleCompteServiceExterne;
	
	
	
	

	private Integer versionObj;
	
	public TaSynchroServiceExterneDTO() {
	}
	
	public TaSynchroServiceExterneDTO(Integer id,
			Date dernierSynchro,
			String typeEntite,
			Integer idCompteServiceExterne,
			String codeCompteServiceExterne,
			String libelleCompteServiceExterne
			) {
		this.id = id;
		this.derniereSynchro = dernierSynchro;
		this. typeEntite = typeEntite;
		this.idCompteServiceExterne = idCompteServiceExterne;
		this.codeCompteServiceExterne = codeCompteServiceExterne;
		this.libelleCompteServiceExterne = libelleCompteServiceExterne;

	}
	




	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public String getTypeEntite() {
		return typeEntite;
	}



	public void setTypeEntite(String typeEntite) {
		this.typeEntite = typeEntite;
	}



	public Integer getVersionObj() {
		return versionObj;
	}



	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}


	
	public Date getDerniereSynchro() {
		return derniereSynchro;
	}
	public void setDerniereSynchro(Date derniereSynchro) {
		this.derniereSynchro = derniereSynchro;
	}

	public String getCodeCompteServiceExterne() {
		return codeCompteServiceExterne;
	}

	public void setCodeCompteServiceExterne(String codeCompteServiceExterne) {
		this.codeCompteServiceExterne = codeCompteServiceExterne;
	}

	public Integer getIdCompteServiceExterne() {
		return idCompteServiceExterne;
	}

	public void setIdCompteServiceExterne(Integer idCompteServiceExterne) {
		this.idCompteServiceExterne = idCompteServiceExterne;
	}

	public String getLibelleCompteServiceExterne() {
		return libelleCompteServiceExterne;
	}

	public void setLibelleCompteServiceExterne(String libelleCompteServiceExterne) {
		this.libelleCompteServiceExterne = libelleCompteServiceExterne;
	}





}

