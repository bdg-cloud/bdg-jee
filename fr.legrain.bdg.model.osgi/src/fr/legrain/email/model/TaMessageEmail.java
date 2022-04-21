package fr.legrain.email.model;

import java.math.BigDecimal;
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
import javax.persistence.SqlResultSetMapping;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.email.dto.TaMessageEmailDTO;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.LgrHibernateValidated;

@Entity
@Table(name = "ta_message_email")
@NamedQueries(value = { 
		@NamedQuery(name=TaMessageEmail.QN.FIND_ALL_LIGHT, query="select a from TaMessageEmail a")
})
@SqlResultSetMapping(name="EmailResult", classes = {
	    @ConstructorResult(targetClass = TaMessageEmailDTO.class, 
	    columns = {
	    		@ColumnResult(name="id_email",type = int.class), 
	    		@ColumnResult(name="subject"),
	    		@ColumnResult(name="adresse_expediteur"),
	    		@ColumnResult(name="body_plain"),
	    		@ColumnResult(name="body_html"),
	    		@ColumnResult(name="date_creation"),
	    		@ColumnResult(name="date_envoi"),
	    		@ColumnResult(name="suivi"),
	    		@ColumnResult(name="spam"),
	    		@ColumnResult(name="lu"),
	    		@ColumnResult(name="accuse_reception_lu"),
	    		@ColumnResult(name="smtp"),
	    		@ColumnResult(name="liste_adresses",type = String.class),
	    		@ColumnResult(name="nb_pj", type = Integer.class),
	    		@ColumnResult(name="liste_etiquette", type = String.class)
	    })
	})
public class TaMessageEmail implements java.io.Serializable {

	private static final long serialVersionUID = -841985444192109353L;
	
	public static class QN {
		public static final String FIND_ALL_LIGHT = "TaMessageEmail.findAllLight";
	}

	private int idEmail;
	
	private TaUtilisateur taUtilisateur;
	private String subject;
	private String adresseExpediteur;
	private String bodyPlain;
	private String bodyHtml;
	private Date dateCreation;
	private Date dateEnvoi;
	private boolean suivi;
	private boolean spam;
	private boolean lu;
	private boolean accuseReceptionLu;
	private boolean recu;
	private String smtp;
//	private TaLogEmail taLogEmailPrecedent;
//	private TaLogEmail taLogEmailSuivant;
	
	private String nomExpediteur;
	private String messageID;
	private String infosService;
	
	private Set<TaContactMessageEmail> destinataires;
	private Set<TaContactMessageEmail> cc;
	private Set<TaContactMessageEmail> bcc;
	
	private Set<TaEtiquetteEmail> etiquettes;
	private Set<TaPieceJointeEmail> piecesJointes;
	
	private String adresseReplyTo;
	private String typeServiceExterne;
	private String codeCompteServiceWebExterne;
	private Boolean expedie = false;
	
	private String etatMessageServiceExterne;
	private String statusServiceExterne;
	
	private String version;
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
		
	public TaMessageEmail() {
	}

	public TaMessageEmail(int idLogPaiementInternet) {
		this.idEmail = idLogPaiementInternet;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_email", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_email",table = "ta_message_email",champEntite="idEmail", clazz = TaMessageEmail.class)
	public int getIdEmail() {
		return this.idEmail;
	}

	public void setIdEmail(int idLogPaiementInternet) {
		this.idEmail = idLogPaiementInternet;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
	@JoinColumn(name = "id_utilisateur")
	public TaUtilisateur getTaUtilisateur() {
		return taUtilisateur;
	}

	public void setTaUtilisateur(TaUtilisateur taUtilisateur) {
		this.taUtilisateur = taUtilisateur;
	}
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "ta_r_etiquette_email",
			joinColumns = {@JoinColumn(name = "id_email")},inverseJoinColumns = {@JoinColumn(name = "id_etiquette_email")})
	public Set<TaEtiquetteEmail> getEtiquettes() {
		return etiquettes;
	}

	public void setEtiquettes(Set<TaEtiquetteEmail> etiquettes) {
		this.etiquettes = etiquettes;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "taMessageEmail", orphanRemoval=true)
	public Set<TaPieceJointeEmail> getPiecesJointes() {
		return piecesJointes;
	}

	public void setPiecesJointes(Set<TaPieceJointeEmail> piecesJointes) {
		this.piecesJointes = piecesJointes;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "taMessageEmail", orphanRemoval=true)
	public Set<TaContactMessageEmail> getDestinataires() {
		return destinataires;
	}

	public void setDestinataires(Set<TaContactMessageEmail> destinataires) {
		this.destinataires = destinataires;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "taMessageEmailCc", orphanRemoval=true)
	public Set<TaContactMessageEmail> getCc() {
		return cc;
	}

	public void setCc(Set<TaContactMessageEmail> cc) {
		this.cc = cc;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "taMessageEmailBcc", orphanRemoval=true)
	public Set<TaContactMessageEmail> getBcc() {
		return bcc;
	}

	public void setBcc(Set<TaContactMessageEmail> bcc) {
		this.bcc = bcc;
	}

	@Column(name = "subject")
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Column(name = "adresse_expediteur")
	public String getAdresseExpediteur() {
		return adresseExpediteur;
	}

	public void setAdresseExpediteur(String adresse_expediteur) {
		this.adresseExpediteur = adresse_expediteur;
	}

	@Column(name = "body_plain")
	public String getBodyPlain() {
		return bodyPlain;
	}

	public void setBodyPlain(String body_plain) {
		this.bodyPlain = body_plain;
	}

	@Column(name = "body_html")
	public String getBodyHtml() {
		return bodyHtml;
	}

	public void setBodyHtml(String body_html) {
		this.bodyHtml = body_html;
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

	@Column(name = "suivi")
	public boolean isSuivi() {
		return suivi;
	}

	public void setSuivi(boolean suivi) {
		this.suivi = suivi;
	}

	@Column(name = "spam")
	public boolean isSpam() {
		return spam;
	}

	public void setSpam(boolean spam) {
		this.spam = spam;
	}

	@Column(name = "lu")
	public boolean isLu() {
		return lu;
	}

	public void setLu(boolean lu) {
		this.lu = lu;
	}

	@Column(name = "accuse_reception_lu")
	public boolean isAccuseReceptionLu() {
		return accuseReceptionLu;
	}

	public void setAccuseReceptionLu(boolean accuse_reception_lu) {
		this.accuseReceptionLu = accuse_reception_lu;
	}

	@Column(name = "recu")
	public boolean isRecu() {
		return recu;
	}

	public void setRecu(boolean recu) {
		this.recu = recu;
	}

	@Column(name = "smtp")
	public String getSmtp() {
		return smtp;
	}

	public void setSmtp(String smtp) {
		this.smtp = smtp;
	}

//	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy="taTiers", orphanRemoval=true)
//	@JoinColumn(name = "id_mail_precedent")
//	public TaLogEmail getTaLogEmailPrecedent() {
//		return taLogEmailPrecedent;
//	}
//
//	public void setTaLogEmailPrecedent(TaLogEmail id_mail_precedent) {
//		this.taLogEmailPrecedent = id_mail_precedent;
//	}
//
//	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy="taTiers", orphanRemoval=true)
//	@JoinColumn(name = "id_mail_suivant")
//	public TaLogEmail getTaLogEmailSuivant() {
//		return taLogEmailSuivant;
//	}
//	
//	public void setTaLogEmailSuivant(TaLogEmail id_mail_suivant) {
//		this.taLogEmailSuivant = id_mail_suivant;
//	}

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

	@Column(name = "nom_expediteur")
	public String getNomExpediteur() {
		return nomExpediteur;
	}

	public void setNomExpediteur(String nomExpediteur) {
		this.nomExpediteur = nomExpediteur;
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

	@Column(name = "adresse_reply_to")
	public String getAdresseReplyTo() {
		return adresseReplyTo;
	}

	public void setAdresseReplyTo(String adresse_reply_to) {
		this.adresseReplyTo = adresse_reply_to;
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
	public Boolean isExpedie() {
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



}
