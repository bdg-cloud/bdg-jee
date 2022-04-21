package fr.legrain.edition.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@Table(name = "ta_action_edition")
@NamedQueries(value = { 
		@NamedQuery(name=TaActionEdition.QN.FIND_BY_CODE_LIGHT, query="select new fr.legrain.edition.dto.TaActionEditionDTO(f.idActionEdition,f.codeAction,f.libelle) from TaActionEdition f where f.codeAction like :codeAction order by f.codeAction"),
		@NamedQuery(name=TaActionEdition.QN.FIND_ALL_LIGHT, query="select new fr.legrain.edition.dto.TaActionEditionDTO(f.idActionEdition,f.codeAction,f.libelle) from TaActionEdition f order by f.codeAction"),
		@NamedQuery(name=TaActionEdition.QN.FIND_ALL_BY_ID_EDITION_DTO, query="select new fr.legrain.edition.dto.TaActionEditionDTO(li.idActionEdition,li.codeAction,li.libelle) from TaEdition e join e.actionEdition li where e.idEdition = :idEdition")

})
//@NamedNativeQueries({
//	@NamedNativeQuery(name=TaActionEdition.QN.FIND_ALL_BY_ID_EDITION_DTO, query="select ac.id_action_edition,ac.code_action,ac.libelle "
//			+ " from ta_action_edition ac"
//			+ " left join ta_r_edition_action_edition re on re.id_action_edition = ac.id_action_edition"
//			+ " left join ta_edition e on e.id_edition = re.id_edition where e.id_edition = :idEdition")
//})
public class TaActionEdition implements java.io.Serializable {



	/**
	 * 
	 */
	private static final long serialVersionUID = -8725503396942452738L;
	
	public static final String code_impression = "impression";
	public static final String code_mail = "mail";
	public static final String code_espacecli = "espacecli";
	
	public static class QN {
		public static final String FIND_BY_CODE_LIGHT = "TaActionEdition.findByCodeLight";
		public static final String FIND_ALL_LIGHT = "TaActionEdition.findAllLight";
		public static final String FIND_ALL_BY_ID_EDITION_DTO = "TaActionEdition.findAllByIdEdtionDTO";
	}
	
	
	private int idActionEdition;

	private String codeAction;
	private String libelle;
	
//	@ManyToMany(mappedBy = "ta_action_edition")
//    private Set<TaEdition> editions;
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	private String version;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_action_edition", unique = true, nullable = false)
	public int getIdActionEdition() {
		return idActionEdition;
	}

	public void setIdActionEdition(int idActionEdition) {
		this.idActionEdition = idActionEdition;
	}
	

	
	@Column(name = "libl_action_edition")
	public String getLibelle() {
		return libelle;
	}
	
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	
	@Column(name = "qui_cree", length = 50)
	public String getQuiCree() {
		return this.quiCree;
	}

	public void setQuiCree(String quiCreeAdresse) {
		this.quiCree = quiCreeAdresse;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return this.quandCree;
	}

	public void setQuandCree(Date quandCreeAdresse) {
		this.quandCree = quandCreeAdresse;
	}

	@Column(name = "qui_modif", length = 50)
	public String getQuiModif() {
		return this.quiModif;
	}

	public void setQuiModif(String quiModifAdresse) {
		this.quiModif = quiModifAdresse;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_modif", length = 19)
	public Date getQuandModif() {
		return this.quandModif;
	}

	public void setQuandModif(Date quandModifAdresse) {
		this.quandModif = quandModifAdresse;
	}
	
	@Column(name = "ip_acces", length = 50)
	public String getIpAcces() {
		return this.ipAcces;
	}

	public void setIpAcces(String ipAcces) {
		this.ipAcces = ipAcces;
	}

	@Version
	@Column(name = "version_obj")
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	@Column(name = "version")
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	@Column(name = "code_action_edition")
	public String getCodeAction() {
		return codeAction;
	}

	public void setCodeAction(String codeAction) {
		this.codeAction = codeAction;
	}

//	public Set<TaEdition> getEditions() {
//		return editions;
//	}
//
//	public void setEditions(Set<TaEdition> editions) {
//		this.editions = editions;
//	}



}