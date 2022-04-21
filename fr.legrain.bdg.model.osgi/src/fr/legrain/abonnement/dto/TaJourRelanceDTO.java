package fr.legrain.abonnement.dto;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import fr.legrain.article.model.TaArticle;
import fr.legrain.bdg.model.ModelObject;

public class TaJourRelanceDTO extends ModelObject implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -385317691750999483L;

	private Integer idJourRelance;
	
	private String codeArticle;
	private Integer idArticle;
	private String libellecArticle;
	
	private Integer nbJour;
	
	

	private Integer versionObj;
	
	public TaJourRelanceDTO() {
		
	}
	
	public TaJourRelanceDTO(Integer idJourRelance, String codeArticle,Integer idArticle,
			String libellecArticle, Integer nbJour) {
		this.idJourRelance=idJourRelance;
		this.codeArticle=codeArticle;
		this.idArticle=idArticle;
		this.libellecArticle=libellecArticle;
		this.nbJour=nbJour;
		
	}


	public Integer getIdJourRelance() {
		return idJourRelance;
	}

	public void setIdJourRelance(Integer idJourRelance) {
		this.idJourRelance = idJourRelance;
	}
	

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public Integer getNbJour() {
		return nbJour;
	}

	public void setNbJour(Integer nbJour) {
		this.nbJour = nbJour;
	}

	public String getCodeArticle() {
		return codeArticle;
	}

	public void setCodeArticle(String codeArticle) {
		this.codeArticle = codeArticle;
	}

	public Integer getIdArticle() {
		return idArticle;
	}

	public void setIdArticle(Integer idArticle) {
		this.idArticle = idArticle;
	}

	public String getLibellecArticle() {
		return libellecArticle;
	}

	public void setLibellecArticle(String libellecArticle) {
		this.libellecArticle = libellecArticle;
	}



}
