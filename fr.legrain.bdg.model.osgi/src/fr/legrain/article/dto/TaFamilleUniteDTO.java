package fr.legrain.article.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.validator.LgrHibernateValidated;

public class TaFamilleUniteDTO extends ModelObject implements java.io.Serializable {
	
	private static final long serialVersionUID = -7356028629921827259L;
	
	private Integer id;
	private String codeFamille;
	private String libcFamille;
	private String liblFamille;
	
	private Integer versionObj;

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public TaFamilleUniteDTO() {
	}

	public TaFamilleUniteDTO(Integer ID_FAMILLE, String CODE_FAMILLE,
			String LIBC_FAMILLE, String LIBL_FAMILLE) {
		this.id = ID_FAMILLE;
		this.codeFamille = CODE_FAMILLE;
		this.libcFamille = LIBC_FAMILLE;
		this.liblFamille = LIBL_FAMILLE;
	}
	
	public void setSWTFamille(TaFamilleUniteDTO swtFamille) {
		this.id = swtFamille.id;
		this.codeFamille = swtFamille.codeFamille;
		this.libcFamille = swtFamille.libcFamille;
		this.liblFamille = swtFamille.liblFamille;
	}
	
	public static TaFamilleUniteDTO copy(TaFamilleUniteDTO swtFamille){
		TaFamilleUniteDTO swtFamilleLoc = new TaFamilleUniteDTO();
		swtFamilleLoc.setId(swtFamille.id);
		swtFamilleLoc.setCodeFamille(swtFamille.codeFamille);
		swtFamilleLoc.setLibcFamille(swtFamille.libcFamille);
		swtFamilleLoc.setLiblFamille(swtFamille.liblFamille);
		return swtFamilleLoc;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer ID_FAMILLE) {
		firePropertyChange("id", this.id, this.id = ID_FAMILLE);
	}

	//@NotNull
	//@Size(min=1, max=20)
	//@LgrHibernateValidated(champBd = "code_famille",table = "ta_famille_unite", champEntite="codeFamille", clazz = TaFamilleUniteDTO.class)
	public String getCodeFamille() {
		return this.codeFamille;
	}

	public void setCodeFamille(String CODE_FAMILLE) {
		firePropertyChange("codeFamille", this.codeFamille, this.codeFamille = CODE_FAMILLE);
	}

	//@LgrHibernateValidated(champBd = "libc_famille",table = "ta_famille_unite", champEntite="libcFamille", clazz = TaFamilleUniteDTO.class)
	public String getLibcFamille() {
		return this.libcFamille;
	}

	public void setLibcFamille(String LIBC_FAMILLE) {
		firePropertyChange("libcFamille", this.libcFamille, this.libcFamille = LIBC_FAMILLE);
	}

	//@LgrHibernateValidated(champBd = "libl_famille",table = "ta_famille_unite", champEntite="liblFamille", clazz = TaFamilleUniteDTO.class)
	public String getLiblFamille() {
		return this.liblFamille;
	}

	public void setLiblFamille(String LIBL_FAMILLE) {
		firePropertyChange("liblFamille", this.liblFamille, this.liblFamille = LIBL_FAMILLE);
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TaFamilleUniteDTO))
			return false;
		TaFamilleUniteDTO castOther = (TaFamilleUniteDTO) other;

		return ((this.getId() == castOther.getId()) || (this
				.getId() != null
				&& castOther.getId() != null && this.getId()
				.equals(castOther.getId())))
				&& ((this.getCodeFamille() == castOther.getCodeFamille()) || (this
						.getCodeFamille() != null
						&& castOther.getCodeFamille() != null && this
						.getCodeFamille().equals(castOther.getCodeFamille())))
				&& ((this.getLibcFamille() == castOther.getLibcFamille()) || (this
						.getLibcFamille() != null
						&& castOther.getLibcFamille() != null && this
						.getLibcFamille().equals(castOther.getLibcFamille())))
				&& ((this.getLiblFamille() == castOther.getLiblFamille()) || (this
						.getLiblFamille() != null
						&& castOther.getLiblFamille() != null && this
						.getLiblFamille().equals(castOther.getLiblFamille())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getId() == null ? 0 : this.getId()
						.hashCode());
		result = 37
				* result
				+ (getCodeFamille() == null ? 0 : this.getCodeFamille()
						.hashCode());
		result = 37
				* result
				+ (getLibcFamille() == null ? 0 : this.getLibcFamille()
						.hashCode());
		result = 37
				* result
				+ (getLiblFamille() == null ? 0 : this.getLiblFamille()
						.hashCode());
		return result;
	}

}
