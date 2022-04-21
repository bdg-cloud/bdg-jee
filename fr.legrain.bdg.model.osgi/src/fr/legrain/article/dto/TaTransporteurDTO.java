package fr.legrain.article.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fr.legrain.article.model.TaTransporteur;
import fr.legrain.bdg.model.ModelObject;
import fr.legrain.validator.LgrHibernateValidated;


public class TaTransporteurDTO extends ModelObject implements java.io.Serializable {


	private static final long serialVersionUID = 6872976469850948430L;
	private Integer id;
	private String codeTransporteur;
	private String liblTransporteur;
	
	private Integer idTTransport;
	private String codeTTransport;
	private String liblTTransport;
	
	private Integer versionObj;

	public TaTransporteurDTO() {
	}
	
	public TaTransporteurDTO(Integer id,String codeTransporteur,String liblTransporteur) {
		this.id = id;
		this.codeTransporteur = codeTransporteur;
		this.liblTransporteur = liblTransporteur;
	}
	
	public TaTransporteurDTO(Integer id,String codeTransporteur,String liblTransporteur, Integer idTTransport, String codeTTransport, String liblTTransport) {
		this.id = id;
		this.codeTransporteur = codeTransporteur;
		this.liblTransporteur = liblTransporteur;
		this.idTTransport = idTTransport;
		this.codeTTransport = codeTTransport;
		this.liblTTransport = liblTTransport;
	}

	public void setTaTransporteurDTO(TaTransporteurDTO taTransporteurDTO) {
		this.id = taTransporteurDTO.id;
		this.codeTransporteur = taTransporteurDTO.codeTransporteur;
		this.liblTransporteur = taTransporteurDTO.liblTransporteur;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}

	@NotNull
	@Size(min=1, max=20)
	@LgrHibernateValidated(champBd = "code_transporteur",table = "ta_transporteur", champEntite="codeTransporteur", clazz = TaTransporteurDTO.class)
	public String getCodeTransporteur() {
		return this.codeTransporteur;
	}

	public void setCodeTransporteur(String codeTransporteur) {
		firePropertyChange("codeTransporteur", this.codeTransporteur, this.codeTransporteur = codeTransporteur);
	}

	@LgrHibernateValidated(champBd = "libl_transporteur",table = "ta_transporteur", champEntite="liblTransporteur", clazz = TaTransporteurDTO.class)
	public String getLiblTransporteur() {
		return this.liblTransporteur;
	}

	public void setLiblTransporteur(String liblTransporteur) {
		firePropertyChange("liblTransporteur", this.liblTransporteur, this.liblTransporteur = liblTransporteur);
	}
	
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TaTransporteurDTO))
			return false;
		TaTransporteurDTO castOther = (TaTransporteurDTO) other;

		return ((this.getId() == castOther.getId()) || (this
				.getId() != null
				&& castOther.getId() != null && this.getId()
				.equals(castOther.getId())))
				&& ((this.getCodeTransporteur() == castOther.getCodeTransporteur()) || (this
						.getCodeTransporteur() != null
						&& castOther.getCodeTransporteur() != null && this
						.getCodeTransporteur().equals(castOther.getCodeTransporteur())))
				&& ((this.getLiblTransporteur() == castOther.getLiblTransporteur()) || (this
						.getLiblTransporteur() != null
						&& castOther.getLiblTransporteur() != null && this
						.getLiblTransporteur().equals(castOther.getLiblTransporteur())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getId() == null ? 0 : this.getId().hashCode());
		result = 37
				* result
				+ (getCodeTransporteur() == null ? 0 : this.getCodeTransporteur()
						.hashCode());
		result = 37
				* result
				+ (getLiblTransporteur() == null ? 0 : this.getLiblTransporteur()
						.hashCode());
		return result;
	}

	
	public static TaTransporteurDTO copy(TaTransporteurDTO taTransporteurDTO){
		TaTransporteurDTO taTransporteurDTOLoc = new TaTransporteurDTO();
		taTransporteurDTOLoc.setId(taTransporteurDTO.getId());                //1
		taTransporteurDTOLoc.setCodeTransporteur(taTransporteurDTO.getCodeTransporteur());        //2
		taTransporteurDTOLoc.setLiblTransporteur(taTransporteurDTO.getLiblTransporteur());            //3
		return taTransporteurDTOLoc;
	}

	public Integer getIdTTransport() {
		return idTTransport;
	}

	public void setIdTTransport(Integer idTTransport) {
		this.idTTransport = idTTransport;
	}

	public String getCodeTTransport() {
		return codeTTransport;
	}

	public void setCodeTTransport(String codeTTransport) {
		this.codeTTransport = codeTTransport;
	}

	public String getLiblTTransport() {
		return liblTTransport;
	}

	public void setLiblTTransport(String liblTTransport) {
		this.liblTTransport = liblTTransport;
	}
}
