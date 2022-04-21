package fr.legrain.article.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.validator.LgrHibernateValidated;


public class TaTTransportDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = -746303424939924258L;
	private Integer id;
	private String codeTTransport;
	private String liblTTransport;
	private Integer versionObj;

	public TaTTransportDTO() {
	}
	
	public TaTTransportDTO(Integer id,String codeTTransport,String liblTTransport) {
		this.id = id;
		this.codeTTransport = codeTTransport;
		this.liblTTransport = liblTTransport;
	}

	public void setTaTTransportDTO(TaTTransportDTO taTTransportDTO) {
		this.id = taTTransportDTO.id;
		this.codeTTransport = taTTransportDTO.codeTTransport;
		this.liblTTransport = taTTransportDTO.liblTTransport;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}

	@NotNull
	@Size(min=1, max=20)
	@LgrHibernateValidated(champBd = "code_t_transport",table = "ta_t_transport", champEntite="codeTTransport", clazz = TaTTransportDTO.class)
	public String getCodeTTransport() {
		return this.codeTTransport;
	}

	public void setCodeTTransport(String codeTTransport) {
		firePropertyChange("codeTTransport", this.codeTTransport, this.codeTTransport = codeTTransport);
	}

	@LgrHibernateValidated(champBd = "libl_t_transport",table = "ta_t_transport", champEntite="liblTTransport", clazz = TaTTransportDTO.class)
	public String getLiblTTransport() {
		return this.liblTTransport;
	}

	public void setLiblTTransport(String liblTTransport) {
		firePropertyChange("liblTTransport", this.liblTTransport, this.liblTTransport = liblTTransport);
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
		if (!(other instanceof TaTTransportDTO))
			return false;
		TaTTransportDTO castOther = (TaTTransportDTO) other;

		return ((this.getId() == castOther.getId()) || (this
				.getId() != null
				&& castOther.getId() != null && this.getId()
				.equals(castOther.getId())))
				&& ((this.getCodeTTransport() == castOther.getCodeTTransport()) || (this
						.getCodeTTransport() != null
						&& castOther.getCodeTTransport() != null && this
						.getCodeTTransport().equals(castOther.getCodeTTransport())))
				&& ((this.getLiblTTransport() == castOther.getLiblTTransport()) || (this
						.getLiblTTransport() != null
						&& castOther.getLiblTTransport() != null && this
						.getLiblTTransport().equals(castOther.getLiblTTransport())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getId() == null ? 0 : this.getId().hashCode());
		result = 37
				* result
				+ (getCodeTTransport() == null ? 0 : this.getCodeTTransport()
						.hashCode());
		result = 37
				* result
				+ (getLiblTTransport() == null ? 0 : this.getLiblTTransport()
						.hashCode());
		return result;
	}

	
	public static TaTTransportDTO copy(TaTTransportDTO taTTransportDTO){
		TaTTransportDTO taTTransportDTOLoc = new TaTTransportDTO();
		taTTransportDTOLoc.setId(taTTransportDTO.getId());                //1
		taTTransportDTOLoc.setCodeTTransport(taTTransportDTO.getCodeTTransport());        //2
		taTTransportDTOLoc.setLiblTTransport(taTTransportDTO.getLiblTTransport());            //3
		return taTTransportDTOLoc;
	}
}
