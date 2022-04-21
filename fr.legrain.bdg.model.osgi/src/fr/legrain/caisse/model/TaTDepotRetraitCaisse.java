package fr.legrain.caisse.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import fr.legrain.validator.LgrHibernateValidated;

@Entity
@Table(name = "ta_t_depot_retrait_caisse")
//@NamedQueries(value = { 
//		@NamedQuery(name=TaTDepotRetraitCaisse.QN.FIND_BY_CODE_LIGHT, query="select new fr.legrain.article.dto.TaTTvaDTO(f.idTTva,f.codeTTva,f.libTTva) from TaTTva f where f.codeTTva like ? order by f.codeTTva"),
//		@NamedQuery(name=TaTDepotRetraitCaisse.QN.FIND_ALL_LIGHT, query="select new fr.legrain.article.dto.TaTTvaDTO(f.idTTva,f.codeTTva,f.libTTva) from TaTTva f order by f.codeTTva")	
//})
public class TaTDepotRetraitCaisse implements java.io.Serializable {

	private static final long serialVersionUID = 1094296384073579735L;

	public static class QN {
		public static final String FIND_BY_CODE_LIGHT = "TaTDepotRetraitCaisse.findByCodeLight";
		public static final String FIND_ALL_LIGHT = "TaTDepotRetraitCaisse.findAllTaTTvaLight";
	}
	
	public static final String DEPOT = "D";
	public static final String RETRAIT = "R";
	
	private int idTDepotRetraitCaisse;
	private String codeTDepotRetraitCaisse;
	private String libelleTDepotRetraitCaisse;

	private Boolean systeme;
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private String version;
	private Integer versionObj;

	public TaTDepotRetraitCaisse() {
	}

	public TaTDepotRetraitCaisse(int idTDepotRetraitCaisse) {
		this.idTDepotRetraitCaisse = idTDepotRetraitCaisse;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_t_depot_retrait_caisse", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_t_depot_retrait_caisse",table = "ta_t_depot_retrait_caisse", champEntite="idTDepotRetraitCaisse", clazz = TaTDepotRetraitCaisse.class)
	public int getIdTDepotRetraitCaisse() {
		return this.idTDepotRetraitCaisse;
	}

	public void setIdTDepotRetraitCaisse(int idTTva) {
		this.idTDepotRetraitCaisse = idTTva;
	}

	@Column(name = "version", length = 20)
	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	@Version
	@Column(name = "version_obj", precision = 15)
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	@Column(name = "code_t_depot_retrait_caisse", length = 1)
	@LgrHibernateValidated(champBd = "code_t_depot_retrait_caisse",table = "ta_t_depot_retrait_caisse", champEntite="codeTDepotRetraitCaisse", clazz = TaTDepotRetraitCaisse.class)
	public String getCodeTDepotRetraitCaisse() {
		return this.codeTDepotRetraitCaisse;
	}

	public void setCodeTDepotRetraitCaisse(String codeTTva) {
		this.codeTDepotRetraitCaisse = codeTTva;
	}

	@Column(name = "libelle_t_depot_retrait_caisse")
	@LgrHibernateValidated(champBd = "libelle_t_depot_retrait_caisse",table = "ta_t_depot_retrait_caisse", champEntite="libelleTDepotRetraitCaisse", clazz = TaTDepotRetraitCaisse.class)
	public String getLibelleTDepotRetraitCaisse() {
		return this.libelleTDepotRetraitCaisse;
	}

	public void setLibelleTDepotRetraitCaisse(String libTTva) {
		this.libelleTDepotRetraitCaisse = libTTva;
	}

	@Column(name = "qui_cree", length = 50)
	public String getQuiCree() {
		return this.quiCree;
	}

	public void setQuiCree(String quiCreeTTva) {
		this.quiCree = quiCreeTTva;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return this.quandCree;
	}

	public void setQuandCree(Date quandCreeTTva) {
		this.quandCree = quandCreeTTva;
	}

	@Column(name = "qui_modif", length = 50)
	public String getQuiModif() {
		return this.quiModif;
	}

	public void setQuiModif(String quiModifTTva) {
		this.quiModif = quiModifTTva;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_modif", length = 19)
	public Date getQuandModif() {
		return this.quandModif;
	}

	public void setQuandModif(Date quandModifTTva) {
		this.quandModif = quandModifTTva;
	}

	@Column(name = "ip_acces", length = 50)
	public String getIpAcces() {
		return this.ipAcces;
	}

	public void setIpAcces(String ipAcces) {
		this.ipAcces = ipAcces;
	}

	@Column(name = "systeme")
	public Boolean getSysteme() {
		return systeme;
	}

	public void setSysteme(Boolean systeme) {
		this.systeme = systeme;
	}

}
