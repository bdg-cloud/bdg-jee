package fr.legrain.servicewebexterne.model;

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
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import fr.legrain.droits.model.TaUtilisateur;

@Entity
@Table(name = "ta_compte_service_web_externe")
//@NamedQueries(value = { 
//		@NamedQuery(name=TaCompteServiceWebExterne.QN.FIND_BY_CODE_LIGHT, query="select new fr.legrain.servicewebexterne.dto.TaCompteServiceWebExterneDTO(f.id,f.codeTServiceWebExterne,f.libelleTServiceWebExterne,f.descriptionTServiceWebExterne,f.systeme) from TaTServiceWebExterne f where f.codeTServiceWebExterne like ? order by f.codeTServiceWebExterne"),
//		@NamedQuery(name=TaCompteServiceWebExterne.QN.FIND_ALL_LIGHT, query="select new fr.legrain.servicewebexterne.dto.TaCompteServiceWebExterneDTO(f.id,f.codeTServiceWebExterne,f.libelleTServiceWebExterne,f.descriptionTServiceWebExterne,f.systeme) from TaTServiceWebExterne f order by f.codeTServiceWebExterne")	
//})
public class TaCompteServiceWebExterne implements java.io.Serializable {

	private static final long serialVersionUID = 3968987279030440552L;
	
//	public static class QN {
//		public static final String FIND_BY_CODE_LIGHT = "TaCompteServiceWebExterne.findByCodeLight";
//		public static final String FIND_ALL_LIGHT = "TaCompteServiceWebExterne.findAllLight";
//	}
	
	private int idCompteServiceWebExterne;
	private String codeCompteServiceWebExterne;
	private String libelleCompteServiceWebExterne;
	private String descriptionCompteServiceWebExterne;
	private TaTAuthentification taTAuthentification;
	private TaServiceWebExterne taServiceWebExterne;
	private TaUtilisateur proprietaire;
	private String urlService;
	
	private String login;
	private String password;
	private String apiKey1;
	private String apiKey2;
	private String valeur1;
	private String valeur2;
	private String valeur3;
	private String valeur4;
	private String valeur5;
	private String valeur6;
	private String valeur7;
	private String valeur8;
	private String valeur9;
	private String valeur10;
	private boolean actif;
	private boolean defaut;
	private boolean systeme;
	private boolean compteTest;
	private Date debut;
	private Date fin;
	private Date derniereUtilisation;
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	private String version;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_compte_service_web_externe", unique = true, nullable = false)
	public int getIdCompteServiceWebExterne() {
		return idCompteServiceWebExterne;
	}
	public void setIdCompteServiceWebExterne(int idAgenda) {
		this.idCompteServiceWebExterne = idAgenda;
	}
	
	@Column(name = "code_compte_service_web_externe")
	public String getCodeCompteServiceWebExterne() {
		return codeCompteServiceWebExterne;
	}
	public void setCodeCompteServiceWebExterne(String codeCompteServiceWebExterne) {
		this.codeCompteServiceWebExterne = codeCompteServiceWebExterne;
	}
	
	@Column(name = "libelle_compte_service_web_externe")
	public String getLibelleCompteServiceWebExterne() {
		return libelleCompteServiceWebExterne;
	}
	public void setLibelleCompteServiceWebExterne(String libelleCompteServiceWebExterne) {
		this.libelleCompteServiceWebExterne = libelleCompteServiceWebExterne;
	}
	
	@Column(name = "description_compte_service_web_externe")
	public String getDescriptionCompteServiceWebExterne() {
		return descriptionCompteServiceWebExterne;
	}
	public void setDescriptionCompteServiceWebExterne(String descriptionCompteServiceWebExterne) {
		this.descriptionCompteServiceWebExterne = descriptionCompteServiceWebExterne;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
	@JoinColumn(name = "id_t_authentification")
	public TaTAuthentification getTaTAuthentification() {
		return taTAuthentification;
	}
	public void setTaTAuthentification(TaTAuthentification taTAuthentification) {
		this.taTAuthentification = taTAuthentification;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
	@JoinColumn(name = "id_service_web_externe")
	public TaServiceWebExterne getTaServiceWebExterne() {
		return taServiceWebExterne;
	}
	public void setTaServiceWebExterne(TaServiceWebExterne taServiceWebExterne) {
		this.taServiceWebExterne = taServiceWebExterne;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
	@JoinColumn(name = "id_utilisateur")
	public TaUtilisateur getProprietaire() {
		return proprietaire;
	}
	public void setProprietaire(TaUtilisateur proprietaire) {
		this.proprietaire = proprietaire;
	}
	
	@Column(name = "url_service")
	public String getUrlService() {
		return urlService;
	}
	public void setUrlService(String urlService) {
		this.urlService = urlService;
	}
	
	@Column(name = "login")
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	
	@Column(name = "password")
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(name = "api_key_1")
	public String getApiKey1() {
		return apiKey1;
	}
	public void setApiKey1(String apiKey1) {
		this.apiKey1 = apiKey1;
	}
	
	@Column(name = "api_key_2")
	public String getApiKey2() {
		return apiKey2;
	}
	public void setApiKey2(String apiKey2) {
		this.apiKey2 = apiKey2;
	}
	
	@Column(name = "valeur_1")
	public String getValeur1() {
		return valeur1;
	}
	public void setValeur1(String valeur1) {
		this.valeur1 = valeur1;
	}
	
	@Column(name = "valeur_2")
	public String getValeur2() {
		return valeur2;
	}
	public void setValeur2(String valeur2) {
		this.valeur2 = valeur2;
	}
	
	@Column(name = "valeur_3")
	public String getValeur3() {
		return valeur3;
	}
	public void setValeur3(String valeur3) {
		this.valeur3 = valeur3;
	}
	
	@Column(name = "valeur_4")
	public String getValeur4() {
		return valeur4;
	}
	public void setValeur4(String valeur4) {
		this.valeur4 = valeur4;
	}
	
	@Column(name = "valeur_5")
	public String getValeur5() {
		return valeur5;
	}
	public void setValeur5(String valeur5) {
		this.valeur5 = valeur5;
	}
	
	@Column(name = "valeur_6")
	public String getValeur6() {
		return valeur6;
	}
	public void setValeur6(String valeur6) {
		this.valeur6 = valeur6;
	}
	
	@Column(name = "valeur_7")
	public String getValeur7() {
		return valeur7;
	}
	public void setValeur7(String valeur7) {
		this.valeur7 = valeur7;
	}
	
	@Column(name = "valeur_8")
	public String getValeur8() {
		return valeur8;
	}
	public void setValeur8(String valeur8) {
		this.valeur8 = valeur8;
	}
	
	@Column(name = "valeur_9")
	public String getValeur9() {
		return valeur9;
	}
	public void setValeur9(String valeur9) {
		this.valeur9 = valeur9;
	}
	
	@Column(name = "valeur_10")
	public String getValeur10() {
		return valeur10;
	}
	public void setValeur10(String valeur10) {
		this.valeur10 = valeur10;
	}
	
	@Column(name = "actif")
	public boolean isActif() {
		return actif;
	}
	public void setActif(boolean actif) {
		this.actif = actif;
	}
	
	@Column(name = "defaut")
	public boolean isDefaut() {
		return defaut;
	}
	public void setDefaut(boolean defaut) {
		this.defaut = defaut;
	}
	
	@Column(name = "systeme")
	public boolean isSysteme() {
		return systeme;
	}
	public void setSysteme(boolean systeme) {
		this.systeme = systeme;
	}
	
	@Column(name = "identifiant_de_test")
	public boolean isCompteTest() {
		return compteTest;
	}
	public void setCompteTest(boolean compteTest) {
		this.compteTest = compteTest;
	}
	
	@Column(name = "debut")
	public Date getDebut() {
		return debut;
	}
	public void setDebut(Date debut) {
		this.debut = debut;
	}
	
	@Column(name = "fin")
	public Date getFin() {
		return fin;
	}
	public void setFin(Date fin) {
		this.fin = fin;
	}
	
	@Column(name = "derniere_utilisation")
	public Date getDerniereUtilisation() {
		return derniereUtilisation;
	}
	public void setDerniereUtilisation(Date derniereUtilisation) {
		this.derniereUtilisation = derniereUtilisation;
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
	
}