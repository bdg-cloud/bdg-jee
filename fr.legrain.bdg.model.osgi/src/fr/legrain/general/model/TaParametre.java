package fr.legrain.general.model;

import java.util.Date;
import java.util.HashSet;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import fr.legrain.conformite.model.TaModeleConformite;
import fr.legrain.conformite.model.TaRGroupeModeleConformite;
import fr.legrain.droits.model.TaUtilisateur;

@Entity
@Table(name = "ta_parametre")
public class TaParametre implements java.io.Serializable {

	private static final long serialVersionUID = 1469847214792590548L;

	private int idParametre;

	private String dossierMaitre;
	
	private String mailjetApikeyPrivateDefautClient;
	private String mailjetApikeyPublicDefautClient;
	private String mailjetApikeyPrivateDefautProgramme;
	private String mailjetApikeyPublicDefautProgramme;

	private String apiGandiBdgSecurityKey;

	private String stripeEndPointSecret;
	
	private String stripeLiveSecretApiKeyProgramme;
	private String stripeLivePublicApiKeyProgramme;

	private String stripeTestSecretApiKeyProgramme;
	private String stripeTestPublicApiKeyProgramme;
	
	private String nomProjetGoogleConsole;
	private String nomFichierCleJsonGoogleConsole;
	
	private String googleMapApikey;
	private String passwordAccesDev;

	private String microsoftApiApplicationId;
	private String microsoftApiApplicationPassword;
	
	private String widgetAtlassianDataKey;
	
	private String bugzillaApiKey;
	private String bugzillaApiUrl;

	private String jenkinsApiHost;
	private String jenkinsLogin;
	private String jenkinsPassword;
	private String jenkinsApiKey;
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	private String version;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_parametre", unique = true, nullable = false)
	public int getIdParametre() {
		return idParametre;
	}
	
	public void setIdParametre(int idParametre) {
		this.idParametre = idParametre;
	}
	
	@Column(name = "dossier_maitre")
	public String getDossierMaitre() {
		return dossierMaitre;
	}
	
	public void setDossierMaitre(String dossierMaitre) {
		this.dossierMaitre = dossierMaitre;
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

	@Column(name = "stripe_end_point_secret")
	public String getStripeEndPointSecret() {
		return stripeEndPointSecret;
	}

	public void setStripeEndPointSecret(String stripe_end_point_secret) {
		this.stripeEndPointSecret = stripe_end_point_secret;
	}

	@Column(name = "mailjet_apikey_private_defaut_client")
	public String getMailjetApikeyPrivateDefautClient() {
		return mailjetApikeyPrivateDefautClient;
	}

	public void setMailjetApikeyPrivateDefautClient(String mailjet_apikey_private_defaut_client) {
		this.mailjetApikeyPrivateDefautClient = mailjet_apikey_private_defaut_client;
	}

	@Column(name = "mailjet_apikey_public_defaut_client")
	public String getMailjetApikeyPublicDefautClient() {
		return mailjetApikeyPublicDefautClient;
	}

	public void setMailjetApikeyPublicDefautClient(String mailjet_apikey_public_defaut_client) {
		this.mailjetApikeyPublicDefautClient = mailjet_apikey_public_defaut_client;
	}

	@Column(name = "mailjet_apikey_private_defaut_programme")
	public String getMailjetApikeyPrivateDefautProgramme() {
		return mailjetApikeyPrivateDefautProgramme;
	}

	public void setMailjetApikeyPrivateDefautProgramme(String mailjet_apikey_private_defaut_programme) {
		this.mailjetApikeyPrivateDefautProgramme = mailjet_apikey_private_defaut_programme;
	}

	@Column(name = "mailjet_apikey_public_defaut_programme")
	public String getMailjetApikeyPublicDefautProgramme() {
		return mailjetApikeyPublicDefautProgramme;
	}

	public void setMailjetApikeyPublicDefautProgramme(String mailjet_apikey_public_defaut_programme) {
		this.mailjetApikeyPublicDefautProgramme = mailjet_apikey_public_defaut_programme;
	}

	@Column(name = "api_gandi_bdg_security_key")
	public String getApiGandiBdgSecurityKey() {
		return apiGandiBdgSecurityKey;
	}

	public void setApiGandiBdgSecurityKey(String api_gandi_bdg_security_key) {
		this.apiGandiBdgSecurityKey = api_gandi_bdg_security_key;
	}

	@Column(name = "stripe_live_secret_api_key_programme")
	public String getStripeLiveSecretApiKeyProgramme() {
		return stripeLiveSecretApiKeyProgramme;
	}

	public void setStripeLiveSecretApiKeyProgramme(String stripe_live_secret_api_key_programme) {
		this.stripeLiveSecretApiKeyProgramme = stripe_live_secret_api_key_programme;
	}

	@Column(name = "stripe_live_public_api_key_programme")
	public String getStripeLivePublicApiKeyProgramme() {
		return stripeLivePublicApiKeyProgramme;
	}

	public void setStripeLivePublicApiKeyProgramme(String stripe_live_public_api_key_programme) {
		this.stripeLivePublicApiKeyProgramme = stripe_live_public_api_key_programme;
	}

	@Column(name = "stripe_test_secret_api_key_programme")
	public String getStripeTestSecretApiKeyProgramme() {
		return stripeTestSecretApiKeyProgramme;
	}

	public void setStripeTestSecretApiKeyProgramme(String stripe_test_secret_api_key_programme) {
		this.stripeTestSecretApiKeyProgramme = stripe_test_secret_api_key_programme;
	}

	@Column(name = "stripe_test_public_api_key_programme")
	public String getStripeTestPublicApiKeyProgramme() {
		return stripeTestPublicApiKeyProgramme;
	}

	public void setStripeTestPublicApiKeyProgramme(String stripe_test_public_api_key_programme) {
		this.stripeTestPublicApiKeyProgramme = stripe_test_public_api_key_programme;
	}

	@Column(name = "nom_projet_google_console")
	public String getNomProjetGoogleConsole() {
		return nomProjetGoogleConsole;
	}

	public void setNomProjetGoogleConsole(String nom_projet_google_console) {
		this.nomProjetGoogleConsole = nom_projet_google_console;
	}

	@Column(name = "nom_fichier_cle_json_google_console")
	public String getNomFichierCleJsonGoogleConsole() {
		return nomFichierCleJsonGoogleConsole;
	}

	public void setNomFichierCleJsonGoogleConsole(String nom_fichier_cle_json_google_console) {
		this.nomFichierCleJsonGoogleConsole = nom_fichier_cle_json_google_console;
	}

	@Column(name = "google_map_api_key")
	public String getGoogleMapApikey() {
		return googleMapApikey;
	}

	public void setGoogleMapApikey(String google_map_api_key) {
		this.googleMapApikey = google_map_api_key;
	}

	@Column(name = "password_acces_dev")
	public String getPasswordAccesDev() {
		return passwordAccesDev;
	}

	public void setPasswordAccesDev(String password_acces_dev) {
		this.passwordAccesDev = password_acces_dev;
	}

	@Column(name = "microsoft_api_application_id")
	public String getMicrosoftApiApplicationId() {
		return microsoftApiApplicationId;
	}

	public void setMicrosoftApiApplicationId(String microsoft_api_application_id) {
		this.microsoftApiApplicationId = microsoft_api_application_id;
	}

	@Column(name = "microsoft_api_application_password")
	public String getMicrosoftApiApplicationPassword() {
		return microsoftApiApplicationPassword;
	}

	public void setMicrosoftApiApplicationPassword(String microsoft_api_application_password) {
		this.microsoftApiApplicationPassword = microsoft_api_application_password;
	}

	@Column(name = "widget_atlassian_data_key")
	public String getWidgetAtlassianDataKey() {
		return widgetAtlassianDataKey;
	}

	public void setWidgetAtlassianDataKey(String widget_atlassian_data_key) {
		this.widgetAtlassianDataKey = widget_atlassian_data_key;
	}

	@Column(name = "bugzilla_api_key")
	public String getBugzillaApiKey() {
		return bugzillaApiKey;
	}

	public void setBugzillaApiKey(String bugzilla_api_key) {
		this.bugzillaApiKey = bugzilla_api_key;
	}

	@Column(name = "bugzilla_api_url")
	public String getBugzillaApiUrl() {
		return bugzillaApiUrl;
	}

	public void setBugzillaApiUrl(String bugzilla_api_url) {
		this.bugzillaApiUrl = bugzilla_api_url;
	}

	@Column(name = "jenkins_api_host")
	public String getJenkinsApiHost() {
		return jenkinsApiHost;
	}

	public void setJenkinsApiHost(String jenkins_api_host) {
		this.jenkinsApiHost = jenkins_api_host;
	}

	@Column(name = "jenkins_login")
	public String getJenkinsLogin() {
		return jenkinsLogin;
	}

	public void setJenkinsLogin(String jenkins_login) {
		this.jenkinsLogin = jenkins_login;
	}

	@Column(name = "jenkins_password")
	public String getJenkinsPassword() {
		return jenkinsPassword;
	}

	public void setJenkinsPassword(String jenkins_password) {
		this.jenkinsPassword = jenkins_password;
	}

	@Column(name = "jenkins_api_key")
	public String getJenkinsApiKey() {
		return jenkinsApiKey;
	}

	public void setJenkinsApiKey(String jenkins_api_key) {
		this.jenkinsApiKey = jenkins_api_key;
	}

}