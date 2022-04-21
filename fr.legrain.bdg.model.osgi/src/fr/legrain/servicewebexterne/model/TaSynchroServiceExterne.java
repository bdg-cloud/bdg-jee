package fr.legrain.servicewebexterne.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@Table(name = "ta_synchro_service_externe")
@NamedQueries(value = { 
		
		@NamedQuery(name=TaSynchroServiceExterne.QN.FIND_ALL_BY_TYPE_ENTITE_AND_BY_ID_COMPTE_SERVICE_WEB_EXTERNE_DTO, query="select new fr.legrain.servicewebexterne.dto.TaSynchroServiceExterneDTO("
				+ " f.idSynchroServiceExterne, f.derniereSynchro, f.typeEntite, s.idCompteServiceWebExterne, s.codeCompteServiceWebExterne, s.libelleCompteServiceWebExterne ) "
				+ " from TaSynchroServiceExterne f join f.taCompteServiceWebExterne s "
				+ " where f.typeEntite = :typeEntite and f.taCompteServiceWebExterne.idCompteServiceWebExterne = :idCompteServiceWebExterne "
				+ " order by f.derniereSynchro DESC"),
//		@NamedQuery(name=TaSynchroServiceExterne.QN.FIND_LAST_DATE_BY_TYPE_ENTITE_AND_BY_ID_COMPTE_SERVICE_WEB_EXTERNE_DTO, query="select new fr.legrain.servicewebexterne.dto.TaSynchroServiceExterneDTO("
//				+ " f.idSynchroServiceExterne, f.derniereSynchro, f.typeEntite, s.idCompteServiceWebExterne, s.codeCompteServiceWebExterne, s.libelleCompteServiceWebExterne ) "
//				+ " from TaSynchroServiceExterne f join f.taCompteServiceWebExterne s "
//				+ " where f.typeEntite = :typeEntite and f.taCompteServiceWebExterne.idCompteServiceWebExterne = :idCompteServiceWebExterne "
//				+ " and f.derniereSynchro = (select max (f.derniereSynchro)"
//				+ " order by f.derniereSynchro DESC"),

})		
public class TaSynchroServiceExterne implements java.io.Serializable{


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1363326525487279235L;
	
	public static class QN {
		public static final String FIND_ALL_BY_TYPE_ENTITE_AND_BY_ID_COMPTE_SERVICE_WEB_EXTERNE_DTO = "TaSynchroServiceExterne.findAllByTypeEntiteAndByIdCompteServiceWebExterneDTO";
//		public static final String FIND_LAST_DATE_BY_TYPE_ENTITE_AND_BY_ID_COMPTE_SERVICE_WEB_EXTERNE_DTO="TaSynchroServiceExterne.findLastDateByTypeEntiteAndByIdCompteServiceWebExterneDTO";
	}
	
	public static final String TYPE_ENTITE_ARTICLE ="article";
	public static final String TYPE_ENTITE_ADRESSE_LIVRAISON ="adresse_livraison";
	public static final String TYPE_ENTITE_TIERS ="tiers";
	public static final String TYPE_ENTITE_LIGNE_SERVICE_EXTERNE ="ligne_service_externe";
	public static final String TYPE_ENTITE_COMMANDE ="commande";
	public static final String TYPE_ENTITE_DEVIS ="devis";

	private int idSynchroServiceExterne;
	
    private Date  derniereSynchro;
	private String typeEntite;
	private TaCompteServiceWebExterne taCompteServiceWebExterne;
	
	private String version;
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_synchro_service_externe", unique = true, nullable = false)
	public int getIdSynchroServiceExterne() {
		return idSynchroServiceExterne;
	}

	public void setIdSynchroServiceExterne(int idSynchroServiceExterne) {
		this.idSynchroServiceExterne = idSynchroServiceExterne;
	}
	@Column(name = "qui_cree", length = 50)
	public String getQuiCree() {
		return quiCree;
	}

	public void setQuiCree(String quiCree) {
		this.quiCree = quiCree;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return quandCree;
	}

	public void setQuandCree(Date quandCree) {
		this.quandCree = quandCree;
	}
	@Column(name = "qui_modif", length = 50)
	public String getQuiModif() {
		return quiModif;
	}

	public void setQuiModif(String quiModif) {
		this.quiModif = quiModif;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_modif", length = 19)
	public Date getQuandModif() {
		return quandModif;
	}

	public void setQuandModif(Date quandModif) {
		this.quandModif = quandModif;
	}
	@Version
	@Column(name = "version_obj")
	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	@Column(name = "ip_acces", length = 50)
	public String getIpAcces() {
		return ipAcces;
	}

	public void setIpAcces(String ipAcces) {
		this.ipAcces = ipAcces;
	}
	
	@Column(name = "version", length = 20)
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}


	@Column(name = "type_entite")
	public String getTypeEntite() {
		return typeEntite;
	}

	public void setTypeEntite(String typeEntite) {
		this.typeEntite = typeEntite;
	}


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "derniere_synchro", length = 19)
	public Date getDerniereSynchro() {
		return derniereSynchro;
	}

	public void setDerniereSynchro(Date derniereSynchro) {
		this.derniereSynchro = derniereSynchro;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_compte_service_web_externe")
	public TaCompteServiceWebExterne getTaCompteServiceWebExterne() {
		return taCompteServiceWebExterne;
	}

	public void setTaCompteServiceWebExterne(TaCompteServiceWebExterne taCompteServiceWebExterne) {
		this.taCompteServiceWebExterne = taCompteServiceWebExterne;
	}



}
