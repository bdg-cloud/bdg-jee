package fr.legrain.tiers.dto;

import java.util.LinkedList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.document.dto.TaTDocDTO;
import fr.legrain.validator.LgrHibernateValidated;


public class TaParamCreeDocTiersDTO extends ModelObject implements java.io.Serializable {


	private static final long serialVersionUID = -8420674597834025746L;
	
	private Integer id;
	private Integer idTiers;
	private Boolean tiers;
	private Boolean document;
	private Boolean semaine;
	private Boolean deuxSemaines;
	private Boolean mois;
	private Boolean xJours;
	private Integer nbJours=1;
	private String codeParam;
	private Boolean decade;
	
	private Integer versionObj;


	private List<TaTDocDTO> taTDoc = new LinkedList<TaTDocDTO>();
	


	public TaParamCreeDocTiersDTO() {
	}


	public static TaParamCreeDocTiersDTO copy(TaParamCreeDocTiersDTO swt){
		TaParamCreeDocTiersDTO swtLoc = new TaParamCreeDocTiersDTO();
		swtLoc.setId(swt.getId()); 
		swtLoc.setDeuxSemaines(swt.getDeuxSemaines()); 
		swtLoc.setDocument(swt.getDocument()); 
		swtLoc.setMois(swt.getMois()); 
		swtLoc.setNbJours(swt.getNbJours()); 
		swtLoc.setSemaine(swt.getSemaine()); 
		swtLoc.setTaTDoc(swt.getTaTDoc()); 
		swtLoc.setIdTiers(swt.getIdTiers()); 
		swtLoc.setTiers(swt.getTiers()); 
		swtLoc.setxJours(swt.getxJours()); 
		swtLoc.setDecade(swt.getDecade());
		return swtLoc;
	}
	
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer idParamCreeDocTiers) {
		firePropertyChange("id", this.id, this.id = idParamCreeDocTiers);
	}
	public Integer getIdTiers() {
		return idTiers;
	}


	public void setIdTiers(Integer idTiers) {
		firePropertyChange("idTiers", this.idTiers, this.idTiers = idTiers);
	}
	
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}


	public List<TaTDocDTO> getTaTDoc() {
		return taTDoc;
	}

	public void setTaTDoc(List<TaTDocDTO> taTDoc) {
		firePropertyChange("taTDoc", this.taTDoc, this.taTDoc = taTDoc);
	}

	public Boolean getTiers() {
		return tiers;
	}

	public void setTiers(Boolean tiers) {
		firePropertyChange("tiers", this.tiers, this.tiers = tiers);
	}

	@LgrHibernateValidated(champBd = "document",table = "ta_param_cree_doc_tiers",champEntite="document", clazz = TaParamCreeDocTiersDTO.class)
	public Boolean getDocument() {
		return document;
	}

	public void setDocument(Boolean document) {
		firePropertyChange("document", this.document, this.document = document);
	}

	@LgrHibernateValidated(champBd = "semaine",table = "ta_param_cree_doc_tiers",champEntite="semaine", clazz = TaParamCreeDocTiersDTO.class)
	public Boolean getSemaine() {
		return semaine;
	}

	public void setSemaine(Boolean semaine) {
		firePropertyChange("semaine", this.semaine, this.semaine = semaine);
	}

	@LgrHibernateValidated(champBd = "deux_semaines",table = "ta_param_cree_doc_tiers",champEntite="deuxSemaines", clazz = TaParamCreeDocTiersDTO.class)
	public Boolean getDeuxSemaines() {
		return deuxSemaines;
	}

	public void setDeuxSemaines(Boolean deuxSemaines) {
		firePropertyChange("deuxSemaines", this.deuxSemaines, this.deuxSemaines = deuxSemaines);
	}

	@LgrHibernateValidated(champBd = "mois",table = "ta_param_cree_doc_tiers",champEntite="mois", clazz = TaParamCreeDocTiersDTO.class)
	public Boolean getMois() {
		return mois;
	}

	public void setMois(Boolean mois) {
		firePropertyChange("mois", this.mois, this.mois = mois);
	}

	@LgrHibernateValidated(champBd = "x_jours",table = "ta_param_cree_doc_tiers",champEntite="xJours", clazz = TaParamCreeDocTiersDTO.class)
	public Boolean getxJours() {
		return xJours;
	}

	public void setxJours(Boolean xJours) {
		firePropertyChange("xJours", this.xJours, this.xJours = xJours);
	}

	@LgrHibernateValidated(champBd = "nb_jours",table = "ta_param_cree_doc_tiers",champEntite="nbJours", clazz = TaParamCreeDocTiersDTO.class)
	public Integer getNbJours() {
		return nbJours;
	}

	public void setNbJours(Integer nbJours) {
		firePropertyChange("nbJours", this.nbJours, this.nbJours = nbJours);
	}


	@NotNull
	@Size(min=1, max=20)
	@LgrHibernateValidated(champBd = "code_param",table = "ta_param_cree_doc_tiers",champEntite="codeParam", clazz = TaParamCreeDocTiersDTO.class)
	public String getCodeParam() {
		return codeParam;
	}


	public void setCodeParam(String codeParam) {
		firePropertyChange("codeParam", this.codeParam, this.codeParam = codeParam);
	}

	@LgrHibernateValidated(champBd = "decade",table = "ta_param_cree_doc_tiers",champEntite="decade", clazz = TaParamCreeDocTiersDTO.class)
	public Boolean getDecade() {
		return decade;
	}


	public void setDecade(Boolean decade) {
		firePropertyChange("decade", this.decade, this.decade = decade);
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codeParam == null) ? 0 : codeParam.hashCode());
		result = prime * result + ((decade == null) ? 0 : decade.hashCode());
		result = prime * result
				+ ((deuxSemaines == null) ? 0 : deuxSemaines.hashCode());
		result = prime * result
				+ ((document == null) ? 0 : document.hashCode());
		result = prime
				* result
				+ ((id == null) ? 0 : id
						.hashCode());
		result = prime * result + ((idTiers == null) ? 0 : idTiers.hashCode());
		result = prime * result + ((mois == null) ? 0 : mois.hashCode());
		result = prime * result + ((nbJours == null) ? 0 : nbJours.hashCode());
		result = prime * result + ((semaine == null) ? 0 : semaine.hashCode());
		result = prime * result + ((taTDoc == null) ? 0 : taTDoc.hashCode());
		result = prime * result + ((tiers == null) ? 0 : tiers.hashCode());
		result = prime * result + ((xJours == null) ? 0 : xJours.hashCode());
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
		TaParamCreeDocTiersDTO other = (TaParamCreeDocTiersDTO) obj;
		if (codeParam == null) {
			if (other.codeParam != null)
				return false;
		} else if (!codeParam.equals(other.codeParam))
			return false;
		if (decade == null) {
			if (other.decade != null)
				return false;
		} else if (!decade.equals(other.decade))
			return false;
		if (deuxSemaines == null) {
			if (other.deuxSemaines != null)
				return false;
		} else if (!deuxSemaines.equals(other.deuxSemaines))
			return false;
		if (document == null) {
			if (other.document != null)
				return false;
		} else if (!document.equals(other.document))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (idTiers == null) {
			if (other.idTiers != null)
				return false;
		} else if (!idTiers.equals(other.idTiers))
			return false;
		if (mois == null) {
			if (other.mois != null)
				return false;
		} else if (!mois.equals(other.mois))
			return false;
		if (nbJours == null) {
			if (other.nbJours != null)
				return false;
		} else if (!nbJours.equals(other.nbJours))
			return false;
		if (semaine == null) {
			if (other.semaine != null)
				return false;
		} else if (!semaine.equals(other.semaine))
			return false;
		if (taTDoc == null) {
			if (other.taTDoc != null)
				return false;
		} else if (!taTDoc.equals(other.taTDoc))
			return false;
		if (tiers == null) {
			if (other.tiers != null)
				return false;
		} else if (!tiers.equals(other.tiers))
			return false;
		if (xJours == null) {
			if (other.xJours != null)
				return false;
		} else if (!xJours.equals(other.xJours))
			return false;
		return true;
	}

}
