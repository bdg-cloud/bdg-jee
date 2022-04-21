package fr.legrain.article.titretransport.dto;

import fr.legrain.bdg.model.ModelObject;


public class TaTitreTransportDTO extends ModelObject implements java.io.Serializable {
	
	private static final long serialVersionUID = 2374012389636190241L;
	
	private Integer id;
	private String codeTitreTransport;
	private String libelleTitreTransport;
	private Integer qteMinTitreTransport;
	
	private Integer versionObj;
	
	
	
	
	public TaTitreTransportDTO(Integer id, String codeTitreTransport, String libelleTitreTransport,
			Integer qteMinTitreTransport) {
		super();
		this.id = id;
		this.codeTitreTransport = codeTitreTransport;
		this.libelleTitreTransport = libelleTitreTransport;
		this.qteMinTitreTransport = qteMinTitreTransport;
	}

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public TaTitreTransportDTO() {
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer idTNoteArticle) {
		firePropertyChange("id", this.id, this.id = idTNoteArticle);
	}
	public String getCodeTitreTransport() {
		return codeTitreTransport;
	}
	public void setCodeTitreTransport(String codeTNoteArticle) {
		firePropertyChange("codeTNoteArticle", this.codeTitreTransport, this.codeTitreTransport = codeTNoteArticle);
	}
	
	public String getLibelleTitreTransport() {
		return libelleTitreTransport;
	}

	public void setLibelleTitreTransport(String libelleTitreTransport) {
		firePropertyChange("libelleTitreTransport", this.libelleTitreTransport, this.libelleTitreTransport = libelleTitreTransport);
	}

	public Integer getQteMinTitreTransport() {
		return qteMinTitreTransport;
	}

	public void setQteMinTitreTransport(Integer qteMinTitreTransport) {
		firePropertyChange("qteMinTitreTransport", this.qteMinTitreTransport, this.qteMinTitreTransport = qteMinTitreTransport);
	}
	
	public static TaTitreTransportDTO copy(TaTitreTransportDTO swtTitreTransport){
		TaTitreTransportDTO swtTitreTransportLoc = new TaTitreTransportDTO();
		swtTitreTransportLoc.setCodeTitreTransport(swtTitreTransport.codeTitreTransport);
		swtTitreTransportLoc.setId(swtTitreTransport.id);
		swtTitreTransportLoc.setLibelleTitreTransport(swtTitreTransport.libelleTitreTransport);
		swtTitreTransportLoc.setQteMinTitreTransport(swtTitreTransport.qteMinTitreTransport);
		return swtTitreTransportLoc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codeTitreTransport == null) ? 0 : codeTitreTransport.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		TaTitreTransportDTO other = (TaTitreTransportDTO) obj;
		if (codeTitreTransport == null) {
			if (other.codeTitreTransport != null)
				return false;
		} else if (!codeTitreTransport.equals(other.codeTitreTransport))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
