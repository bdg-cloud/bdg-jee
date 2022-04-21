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
@Table(name = "ta_liaison_service_externe")
@NamedQueries(value = { 
		@NamedQuery(name=TaLiaisonServiceExterne.QN.FIND_ALL_BY_TYPE_TIERS_AND_BY_ID_SERVICE_WEB_EXTERNE_DTO, query="select new fr.legrain.servicewebexterne.dto.TaLiaisonServiceExterneDTO("
				+ " f.idLiaisonServiceExterne, f.idEntite, f.refExterne, f.typeEntite, s.idServiceWebExterne, s.codeServiceWebExterne, s.libelleServiceWebExterne, "
				+ " tiers.codeTiers, tiers.nomTiers, tiers.prenomTiers )"
				+ " from TaLiaisonServiceExterne f join f.serviceWebExterne s join TaTiers tiers on tiers.idTiers = f.idEntite"
				+ " where f.typeEntite = :typeEntite and f.serviceWebExterne.idServiceWebExterne = :idServiceWebExterne "
				+ " order by f.quandCree"),
		@NamedQuery(name=TaLiaisonServiceExterne.QN.FIND_ALL_BY_TYPE_ARTICLE_AND_BY_ID_SERVICE_WEB_EXTERNE_DTO, query="select new fr.legrain.servicewebexterne.dto.TaLiaisonServiceExterneDTO("
				+ " f.idLiaisonServiceExterne, f.idEntite, f.refExterne, f.typeEntite, s.idServiceWebExterne, s.codeServiceWebExterne, s.libelleServiceWebExterne, "
				+ " art.codeArticle, art.libellecArticle )"
				+ " from TaLiaisonServiceExterne f join f.serviceWebExterne s join TaArticle art on art.idArticle = f.idEntite"
				+ " where f.typeEntite = :typeEntite and f.serviceWebExterne.idServiceWebExterne = :idServiceWebExterne "
				+ " order by f.quandCree"),
		@NamedQuery(name=TaLiaisonServiceExterne.QN.FIND_ALL_BY_TYPE_T_PAIEMENT_AND_BY_ID_SERVICE_WEB_EXTERNE_DTO, query="select new fr.legrain.servicewebexterne.dto.TaLiaisonServiceExterneDTO("
				+ " f.idLiaisonServiceExterne, f.idEntite, f.refExterne, f.typeEntite, s.idServiceWebExterne, s.codeServiceWebExterne, s.libelleServiceWebExterne, "
				+ " tp.codeTPaiement, tp.libTPaiement )"
				+ " from TaLiaisonServiceExterne f join f.serviceWebExterne s join TaTPaiement tp on tp.idTPaiement = f.idEntite"
				+ " where f.typeEntite = :typeEntite and f.serviceWebExterne.idServiceWebExterne = :idServiceWebExterne "
				+ " order by f.quandCree")

})		
public class TaLiaisonServiceExterne implements java.io.Serializable{


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1363326525487279235L;
	
	public static class QN {
		public static final String FIND_ALL_BY_TYPE_TIERS_AND_BY_ID_SERVICE_WEB_EXTERNE_DTO = "TaLiaisonServiceExterne.findAllByTypeTiersAndByIdServiceWebExterneDTO";	
		public static final String FIND_ALL_BY_TYPE_ARTICLE_AND_BY_ID_SERVICE_WEB_EXTERNE_DTO = "TaLiaisonServiceExterne.findAllByTypeArticleAndByIdServiceWebExterneDTO";
		public static final String FIND_ALL_BY_TYPE_T_PAIEMENT_AND_BY_ID_SERVICE_WEB_EXTERNE_DTO = "TaLiaisonServiceExterne.findAllByTypeTPaiementAndByIdServiceWebExterneDTO";
	}
	
	public static final String TYPE_ENTITE_ARTICLE ="article";
	public static final String TYPE_ENTITE_ADRESSE_LIVRAISON ="adresse_livraison";
	public static final String TYPE_ENTITE_TIERS ="tiers";
	public static final String TYPE_ENTITE_T_PAIEMENT ="type_paiement";
	public static final String TYPE_ENTITE_LIGNE_SERVICE_EXTERNE ="ligne_service_externe";

	private int idLiaisonServiceExterne;
	
	private Integer idEntite;
	private String refExterne;
	private String typeEntite;
	private TaServiceWebExterne serviceWebExterne;
	
	private String version;
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_liaison_service_externe", unique = true, nullable = false)
	public int getIdLiaisonServiceExterne() {
		return idLiaisonServiceExterne;
	}

	public void setIdLiaisonServiceExterne(int idLiaisonServiceExterne) {
		this.idLiaisonServiceExterne = idLiaisonServiceExterne;
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
	@Column(name = "id_entite")
	public Integer getIdEntite() {
		return idEntite;
	}

	public void setIdEntite(Integer idEntite) {
		this.idEntite = idEntite;
	}
	@Column(name = "ref_externe")
	public String getRefExterne() {
		return refExterne;
	}

	public void setRefExterne(String refExterne) {
		this.refExterne = refExterne;
	}
	@Column(name = "type_entite")
	public String getTypeEntite() {
		return typeEntite;
	}

	public void setTypeEntite(String typeEntite) {
		this.typeEntite = typeEntite;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_service_web_externe")
	public TaServiceWebExterne getServiceWebExterne() {
		return serviceWebExterne;
	}

	public void setServiceWebExterne(TaServiceWebExterne serviceWebExterne) {
		this.serviceWebExterne = serviceWebExterne;
	}



}
