package fr.legrain.push.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.LgrHibernateValidated;

@Entity
@Table(name = "ta_message_push")
@NamedQueries(value = { 
		@NamedQuery(name=TaMessagePush.QN.FIND_ALL_LIGHT, query="select a from TaMessagePush a")
})
public class TaMessagePush implements java.io.Serializable {

	private static final long serialVersionUID = -8546752769021452543L;

	public static class QN {
		public static final String FIND_ALL_LIGHT = "TaMessagePush.findAllLight";
	}

	private int idMessagePush;
	
	private TaUtilisateur taUtilisateur;
	private String sujet;
	private String resume;
	private String contenu;
	private String url;
	private String urlImage;
	private String style;
	private Date dateCreation;
	private Date dateEnvoi;
	private boolean lu;
	private boolean recu;

	private String messageID;
	private String infosService;
	
	private Set<TaContactMessagePush> destinataires;

	private String typeServiceExterne;
	private String codeCompteServiceWebExterne;
	private boolean expedie = false;
	
	private String etatMessageServiceExterne;
	private String statusServiceExterne;
	
	private String version;
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
		
	public TaMessagePush() {
	}

	public TaMessagePush(int idMessagePush) {
		this.idMessagePush = idMessagePush;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_message_push", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_message_push",table = "ta_message_push",champEntite="idMessagePush", clazz = TaMessagePush.class)
	public int getIdMessagePush() {
		return this.idMessagePush;
	}

	public void setIdMessagePush(int idMessagePush) {
		this.idMessagePush = idMessagePush;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
	@JoinColumn(name = "id_utilisateur")
	public TaUtilisateur getTaUtilisateur() {
		return taUtilisateur;
	}

	public void setTaUtilisateur(TaUtilisateur taUtilisateur) {
		this.taUtilisateur = taUtilisateur;
	}
	
	
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "taMessagePush", orphanRemoval=true)
	public Set<TaContactMessagePush> getDestinataires() {
		return destinataires;
	}

	public void setDestinataires(Set<TaContactMessagePush> destinataires) {
		this.destinataires = destinataires;
	}


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_creation")
	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date date_creation) {
		this.dateCreation = date_creation;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_envoi")
	public Date getDateEnvoi() {
		return dateEnvoi;
	}

	public void setDateEnvoi(Date date_envoi) {
		this.dateEnvoi = date_envoi;
	}

	@Column(name = "lu")
	public boolean isLu() {
		return lu;
	}

	public void setLu(boolean lu) {
		this.lu = lu;
	}

	@Column(name = "recu")
	public boolean isRecu() {
		return recu;
	}

	public void setRecu(boolean recu) {
		this.recu = recu;
	}

	@Column(name = "version", length = 20)
	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
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

	@Column(name = "message_id")
	public String getMessageID() {
		return messageID;
	}

	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}

	@Column(name = "infos_service")
	public String getInfosService() {
		return infosService;
	}

	public void setInfosService(String infosService) {
		this.infosService = infosService;
	}


	@Column(name = "type_service_externe")
	public String getTypeServiceExterne() {
		return typeServiceExterne;
	}

	public void setTypeServiceExterne(String type_service_externe) {
		this.typeServiceExterne = type_service_externe;
	}

	@Column(name = "code_compte_service_web_externe")
	public String getCodeCompteServiceWebExterne() {
		return codeCompteServiceWebExterne;
	}

	public void setCodeCompteServiceWebExterne(String code_compte_service_web_externe) {
		this.codeCompteServiceWebExterne = code_compte_service_web_externe;
	}

	@Column(name = "expedie")
	public boolean isExpedie() {
		return expedie;
	}

	public void setExpedie(Boolean expedie) {
		this.expedie = expedie;
	}

	@Column(name = "etat_message_service_externe")
	public String getEtatMessageServiceExterne() {
		return etatMessageServiceExterne;
	}

	public void setEtatMessageServiceExterne(String etat_message_service_externe) {
		this.etatMessageServiceExterne = etat_message_service_externe;
	}

	@Column(name = "status_service_externe")
	public String getStatusServiceExterne() {
		return statusServiceExterne;
	}

	public void setStatusServiceExterne(String status_service_externe) {
		this.statusServiceExterne = status_service_externe;
	}

	@Column(name = "sujet")
	public String getSujet() {
		return sujet;
	}

	public void setSujet(String sujet) {
		this.sujet = sujet;
	}

	@Column(name = "resume")
	public String getResume() {
		return resume;
	}

	public void setResume(String resume) {
		this.resume = resume;
	}

	@Column(name = "contenu")
	public String getContenu() {
		return contenu;
	}

	public void setContenu(String contenu) {
		this.contenu = contenu;
	}

	@Column(name = "url")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "url_image")
	public String getUrlImage() {
		return urlImage;
	}

	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}

	@Column(name = "style")
	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public void setExpedie(boolean expedie) {
		this.expedie = expedie;
	}


}
