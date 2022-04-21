package fr.legrain.tiers.model;

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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlElement;

import org.apache.log4j.Logger;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import fr.legrain.article.model.TaArticle;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.LgrHibernateValidated;

@Entity
@Table(name = "ta_param_espace_client")
public class TaParamEspaceClient implements java.io.Serializable {

	private static final long serialVersionUID = -2346162814985796013L;
	
	@Transient
	static Logger logger = Logger.getLogger(TaParamEspaceClient.class.getName());
	
	private int idParamEspaceClient;
	
	/*Constante a faire correspondre pour l'instant avec celles des boutons radio dans les parametres de la boutique*/
	public static final String GENERATION_DOCUMENT_AUCUN = "";
	public static final String GENERATION_DOCUMENT_FACTURE = "fact";
	public static final String GENERATION_DOCUMENT_BON_COMMANDE = "bc";
	public static final String GENERATION_DOCUMENT_BON_COMMANDE_ET_FACTURE = "bc_fact";
	
	private Boolean afficheDevis = true;
	private Boolean afficheFactures = true;
	private Boolean afficheCommandes = true;
	private Boolean afficheLivraisons = true;
	private Boolean afficheAvisEcheance = true;
	private Boolean afficheBoutique = false;
	private Boolean afficheCatalogue = false;
	private Boolean activePanier = false;
	private Boolean paiementCb  = true;
	private Boolean espaceClientActif = true;
	private Boolean miseADispositionAutomatiqueDesFactures = false;
	private Boolean affichePublicite = false;
	private Boolean activeEmailRenseignementProduit = false;
	private Boolean activeCommandeEmailSimple = false;
	private byte[] logoLogin;
	private byte[] logoPagesSimples;
	private byte[] logoHeader;
	private byte[] logoFooter;
	private byte[] imageBackgroundLogin;
	private String nomImageLogoLogin;
	private String nomImageLogoPagesSimples;
	private String nomImageLogoHeader;
	private String nomImageLogoFooter;
	private String nomImageBackgroundLogin;
	private String urlPerso;
	private String texteLogin1;
	private String texteLogin2;
	private String texteAccueil;
	private String texteAccueilCatalogue;
	private String themeDefaut;
	
	private byte[] cgvFileCatWeb;
	private String cgvCatWeb;
	private BigDecimal fraisPortFixe; //A Supprimer, remplacer par un "article FDP"
	private BigDecimal fraisPortLimiteOffert;
	private Boolean afficherPrixCatalogue = false;
	
	private Boolean activeLivraison = false;
	private Boolean activePaiementPanierCB = false;
	private Boolean activePaiementPanierSurPlace = false;
	private String generationDocAuPaiementPanier;
	private Boolean activeEmailConfirmationCmd = false;
	private String texteConfirmationPaiementBoutique;
	
	private TaArticle taArticleFdp = null;
	////////////////
	private Boolean activePointRetrait = false;

	private TaAdresse taAdressePointRetrait; //les champs "en dur" seront surement a supprimer, pourvoir sélectionner une adresse dans une liste d'adresse de l'entreprise
	private String adresse1PointRetrait;
	private String adresse2PointRetrait;
	private String adresse3PointRetrait;
	private String codepostalPointRetrait;
	private String villePointRetrait;
	private String paysPointRetrait ;
	private String latitudeDecPointRetrait;
	private String longitudeDecPointRetrait;
	
	private String horairesOuverturePointRetrait;
	
	private Boolean activePaiementPanierCheque = false;
	private Boolean activePaiementPanierVirement = false;
	private Boolean prixCatalogueHt = false;
	private Boolean activePlanningRetrait = false;
	
	private TaCompteBanque taCompteBanquePaiementVirement;
	private String ibanPaiementVirement;
	private String swiftPaiementVirement;
	///////////////
	private Boolean utilisateurPeuCreerCompte = false;
	private Boolean liaisonNouveauCompteEmailAuto = false;
	private Boolean liaisonNouveauCompteCodeClientAuto = false;
	private Boolean autoriseMajDonneeParClient = false;
	//////////////////
	private String version;
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_param_espace_client", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_param_espace_client",table = "ta_param_espace_client",champEntite="idParamEspaceClient", clazz = TaParamEspaceClient.class)
	public int getIdParamEspaceClient() {
		return this.idParamEspaceClient;
	}

	public void setIdParamEspaceClient(int idParamEspaceClient) {
		this.idParamEspaceClient = idParamEspaceClient;
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

	public void setQuiCree(String quiCreeTelephone) {
		this.quiCree = quiCreeTelephone;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return this.quandCree;
	}

	public void setQuandCree(Date quandCreeTelephone) {
		this.quandCree = quandCreeTelephone;
	}

	@Column(name = "qui_modif", length = 50)
	public String getQuiModif() {
		return this.quiModif;
	}

	public void setQuiModif(String quiModifTelephone) {
		this.quiModif = quiModifTelephone;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_modif", length = 19)
	public Date getQuandModif() {
		return this.quandModif;
	}

	public void setQuandModif(Date quandModifTelephone) {
		this.quandModif = quandModifTelephone;
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

	@Column(name = "affiche_devis")
	public Boolean getAfficheDevis() {
		return afficheDevis;
	}

	public void setAfficheDevis(Boolean affiche_devis) {
		this.afficheDevis = affiche_devis;
	}

	@Column(name = "affiche_factures")
	public Boolean getAfficheFactures() {
		return afficheFactures;
	}

	public void setAfficheFactures(Boolean affiche_factures) {
		this.afficheFactures = affiche_factures;
	}

	@Column(name = "affiche_commandes")
	public Boolean getAfficheCommandes() {
		return afficheCommandes;
	}

	public void setAfficheCommandes(Boolean affiche_commandes) {
		this.afficheCommandes = affiche_commandes;
	}

	@Column(name = "affiche_livraisons")
	public Boolean getAfficheLivraisons() {
		return afficheLivraisons;
	}

	public void setAfficheLivraisons(Boolean affiche_livraisons) {
		this.afficheLivraisons = affiche_livraisons;
	}

	@Column(name = "paiement_cb")
	public Boolean getPaiementCb() {
		return paiementCb;
	}

	public void setPaiementCb(Boolean paiement_cb) {
		this.paiementCb = paiement_cb;
	}

	@Column(name = "espace_client_actif")
	public Boolean getEspaceClientActif() {
		return espaceClientActif;
	}

	public void setEspaceClientActif(Boolean espace_client_actif) {
		this.espaceClientActif = espace_client_actif;
	}

	@Column(name = "logo_login")
	public byte[] getLogoLogin() {
		return logoLogin;
	}

	public void setLogoLogin(byte[] logo_login) {
		this.logoLogin = logo_login;
	}

	@Column(name = "logo_pages_simples")
	public byte[] getLogoPagesSimples() {
		return logoPagesSimples;
	}

	public void setLogoPagesSimples(byte[] logo_pages_simple) {
		this.logoPagesSimples = logo_pages_simple;
	}

	@Column(name = "logo_footer")
	public byte[] getLogoFooter() {
		return logoFooter;
	}

	public void setLogoFooter(byte[] logo_footer) {
		this.logoFooter = logo_footer;
	}

	@Column(name = "logo_header")
	public byte[] getLogoHeader() {
		return logoHeader;
	}

	public void setLogoHeader(byte[] logo_header) {
		this.logoHeader = logo_header;
	}

	@Column(name = "image_background_login")
	public byte[] getImageBackgroundLogin() {
		return imageBackgroundLogin;
	}

	public void setImageBackgroundLogin(byte[] image_backgroud_login) {
		this.imageBackgroundLogin = image_backgroud_login;
	}

	@Column(name = "nom_image_logo_login")
	public String getNomImageLogoLogin() {
		return nomImageLogoLogin;
	}

	public void setNomImageLogoLogin(String nom_image_logo_login) {
		this.nomImageLogoLogin = nom_image_logo_login;
	}

	@Column(name = "nom_image_logo_pages_simples")
	public String getNomImageLogoPagesSimples() {
		return nomImageLogoPagesSimples;
	}

	public void setNomImageLogoPagesSimples(String nom_image_logo_pages_simples) {
		this.nomImageLogoPagesSimples = nom_image_logo_pages_simples;
	}

	@Column(name = "nom_image_logo_header")
	public String getNomImageLogoHeader() {
		return nomImageLogoHeader;
	}

	public void setNomImageLogoHeader(String nom_image_logo_header) {
		this.nomImageLogoHeader = nom_image_logo_header;
	}

	@Column(name = "nom_image_logo_footer")
	public String getNomImageLogoFooter() {
		return nomImageLogoFooter;
	}

	public void setNomImageLogoFooter(String nom_image_logo_footer) {
		this.nomImageLogoFooter = nom_image_logo_footer;
	}

	@Column(name = "nom_image_background_login")
	public String getNomImageBackgroundLogin() {
		return nomImageBackgroundLogin;
	}

	public void setNomImageBackgroundLogin(String nom_image_backgroud_login) {
		this.nomImageBackgroundLogin = nom_image_backgroud_login;
	}

	@Column(name = "url_perso")
	public String getUrlPerso() {
		return urlPerso;
	}

	public void setUrlPerso(String url_perso) {
		this.urlPerso = url_perso;
	}

	@Column(name = "texte_login_1")
	public String getTexteLogin1() {
		return texteLogin1;
	}

	public void setTexteLogin1(String texte_login_1) {
		this.texteLogin1 = texte_login_1;
	}

	@Column(name = "texte_login_2")
	public String getTexteLogin2() {
		return texteLogin2;
	}

	public void setTexteLogin2(String texte_login_2) {
		this.texteLogin2 = texte_login_2;
	}

	@Column(name = "texte_accueil")
	public String getTexteAccueil() {
		return texteAccueil;
	}

	public void setTexteAccueil(String texte_accueil) {
		this.texteAccueil = texte_accueil;
	}

	@Column(name = "theme_defaut")
	public String getThemeDefaut() {
		return themeDefaut;
	}

	public void setThemeDefaut(String theme_defaut) {
		this.themeDefaut = theme_defaut;
	}

	@Column(name = "affiche_avis_echeance")
	public Boolean getAfficheAvisEcheance() {
		return afficheAvisEcheance;
	}

	public void setAfficheAvisEcheance(Boolean afficheAvisEcheance) {
		this.afficheAvisEcheance = afficheAvisEcheance;
	}

	@Column(name = "mise_a_disposition_facture_auto")
	public Boolean getMiseADispositionAutomatiqueDesFactures() {
		return miseADispositionAutomatiqueDesFactures;
	}

	public void setMiseADispositionAutomatiqueDesFactures(Boolean miseADispositionAutomatiqueDesFactures) {
		this.miseADispositionAutomatiqueDesFactures = miseADispositionAutomatiqueDesFactures;
	}

	@Column(name = "affiche_publicite")
	public Boolean getAffichePublicite() {
		return affichePublicite;
	}

	public void setAffichePublicite(Boolean affichePublicite) {
		this.affichePublicite = affichePublicite;
	}

	@Column(name = "affiche_boutique")
	public Boolean getAfficheBoutique() {
		return afficheBoutique;
	}

	public void setAfficheBoutique(Boolean afficheBoutique) {
		this.afficheBoutique = afficheBoutique;
	}

	@Column(name = "affiche_catalogue")
	public Boolean getAfficheCatalogue() {
		return afficheCatalogue;
	}

	public void setAfficheCatalogue(Boolean afficheCatalogue) {
		this.afficheCatalogue = afficheCatalogue;
	}

	@Column(name = "active_panier")
	public Boolean getActivePanier() {
		return activePanier;
	}

	public void setActivePanier(Boolean activePanier) {
		this.activePanier = activePanier;
	}

	@Column(name = "active_email_renseignement_produit")
	public Boolean getActiveEmailRenseignementProduit() {
		return activeEmailRenseignementProduit;
	}

	public void setActiveEmailRenseignementProduit(Boolean activeEmailRenseignementProduit) {
		this.activeEmailRenseignementProduit = activeEmailRenseignementProduit;
	}

	@Column(name = "active_commande_email_simple")
	public Boolean getActiveCommandeEmailSimple() {
		return activeCommandeEmailSimple;
	}

	public void setActiveCommandeEmailSimple(Boolean activeCommandeEmailSimple) {
		this.activeCommandeEmailSimple = activeCommandeEmailSimple;
	}

	@Column(name = "texte_accueil_catalogue")
	public String getTexteAccueilCatalogue() {
		return texteAccueilCatalogue;
	}

	public void setTexteAccueilCatalogue(String texteAccueilCatalogue) {
		this.texteAccueilCatalogue = texteAccueilCatalogue;
	}

	@Column(name = "cgv_file_cat_web")
	public byte[] getCgvFileCatWeb() {
		return cgvFileCatWeb;
	}

	public void setCgvFileCatWeb(byte[] cgvFileCatWeb) {
		this.cgvFileCatWeb = cgvFileCatWeb;
	}

	@Column(name = "cgv_cat_web")
	public String getCgvCatWeb() {
		return cgvCatWeb;
	}

	public void setCgvCatWeb(String cgvCatWeb) {
		this.cgvCatWeb = cgvCatWeb;
	}

	@Column(name = "frais_port_fixe")
	public BigDecimal getFraisPortFixe() {
		return fraisPortFixe;
	}

	public void setFraisPortFixe(BigDecimal fraisPortFixe) {
		this.fraisPortFixe = fraisPortFixe;
	}

	@Column(name = "frais_port_limite_offert")
	public BigDecimal getFraisPortLimiteOffert() {
		return fraisPortLimiteOffert;
	}

	public void setFraisPortLimiteOffert(BigDecimal fraisPortLimiteOffert) {
		this.fraisPortLimiteOffert = fraisPortLimiteOffert;
	}

	@Column(name = "afficher_prix_catalogue")
	public Boolean getAfficherPrixCatalogue() {
		return afficherPrixCatalogue;
	}

	public void setAfficherPrixCatalogue(Boolean afficherPrixCatalogue) {
		this.afficherPrixCatalogue = afficherPrixCatalogue;
	}

	@OneToOne(fetch = FetchType.EAGER, cascade = {/*CascadeType.REMOVE,*/ CascadeType.REFRESH} , orphanRemoval=false)
	@JoinColumn(name = "id_article_fdp")
	public TaArticle getTaArticleFdp() {
		return taArticleFdp;
	}

	public void setTaArticleFdp(TaArticle taArticleFdp) {
		this.taArticleFdp = taArticleFdp;
	}

	
	@Column(name = "active_livraison")
	public Boolean getActiveLivraison() {
		return activeLivraison;
	}

	public void setActiveLivraison(Boolean activeLivraison) {
		this.activeLivraison = activeLivraison;
	}

	@Column(name = "active_paiement_panier_cb")
	public Boolean getActivePaiementPanierCB() {
		return activePaiementPanierCB;
	}

	public void setActivePaiementPanierCB(Boolean activePaiementPanierCB) {
		this.activePaiementPanierCB = activePaiementPanierCB;
	}

	@Column(name = "active_paiement_panier_sur_place")
	public Boolean getActivePaiementPanierSurPlace() {
		return activePaiementPanierSurPlace;
	}

	public void setActivePaiementPanierSurPlace(Boolean activePaiementPanierSurPlace) {
		this.activePaiementPanierSurPlace = activePaiementPanierSurPlace;
	}

	@Column(name = "generation_doc_paiement_panier")
	public String getGenerationDocAuPaiementPanier() {
		return generationDocAuPaiementPanier;
	}

	public void setGenerationDocAuPaiementPanier(String generationDocAuPaiementPanier) {
		this.generationDocAuPaiementPanier = generationDocAuPaiementPanier;
	}

	@Column(name = "active_email_confirmation_cmd")
	public Boolean getActiveEmailConfirmationCmd() {
		return activeEmailConfirmationCmd;
	}

	public void setActiveEmailConfirmationCmd(Boolean activeEmailConfirmationCmd) {
		this.activeEmailConfirmationCmd = activeEmailConfirmationCmd;
	}

	@Column(name = "texte_confirmation_paiement_boutique")
	public String getTexteConfirmationPaiementBoutique() {
		return texteConfirmationPaiementBoutique;
	}

	public void setTexteConfirmationPaiementBoutique(String texteConfirmationPaiementBoutique) {
		this.texteConfirmationPaiementBoutique = texteConfirmationPaiementBoutique;
	}

	@Column(name = "active_point_retrait")
	public Boolean getActivePointRetrait() {
		return activePointRetrait;
	}

	public void setActivePointRetrait(Boolean activePointRetrait) {
		this.activePointRetrait = activePointRetrait;
	}

	@OneToOne(fetch = FetchType.EAGER, cascade = {/*CascadeType.REMOVE,*/ CascadeType.REFRESH} , orphanRemoval=false)
	@JoinColumn(name = "id_adresse_point_retrait")
	public TaAdresse getTaAdressePointRetrait() {
		return taAdressePointRetrait;
	}

	public void setTaAdressePointRetrait(TaAdresse taAdressePointRetrait) {
		this.taAdressePointRetrait = taAdressePointRetrait;
	}

	@Column(name = "adresse1_point_retrait")
	public String getAdresse1PointRetrait() {
		return adresse1PointRetrait;
	}

	public void setAdresse1PointRetrait(String adresse1PointRetrait) {
		this.adresse1PointRetrait = adresse1PointRetrait;
	}

	@Column(name = "adresse2_point_retrait")
	public String getAdresse2PointRetrait() {
		return adresse2PointRetrait;
	}

	public void setAdresse2PointRetrait(String adresse2PointRetrait) {
		this.adresse2PointRetrait = adresse2PointRetrait;
	}

	@Column(name = "adresse3_point_retrait")
	public String getAdresse3PointRetrait() {
		return adresse3PointRetrait;
	}

	public void setAdresse3PointRetrait(String adresse3PointRetrait) {
		this.adresse3PointRetrait = adresse3PointRetrait;
	}

	@Column(name = "code_postal_point_retrait")
	public String getCodepostalPointRetrait() {
		return codepostalPointRetrait;
	}

	public void setCodepostalPointRetrait(String codepostalPointRetrait) {
		this.codepostalPointRetrait = codepostalPointRetrait;
	}

	@Column(name = "ville_point_retrait")
	public String getVillePointRetrait() {
		return villePointRetrait;
	}

	public void setVillePointRetrait(String villePointRetrait) {
		this.villePointRetrait = villePointRetrait;
	}

	@Column(name = "pays_point_retrait")
	public String getPaysPointRetrait() {
		return paysPointRetrait;
	}

	public void setPaysPointRetrait(String paysPointRetrait) {
		this.paysPointRetrait = paysPointRetrait;
	}

	@Column(name = "latitude_dec_point_retrait")
	public String getLatitudeDecPointRetrait() {
		return latitudeDecPointRetrait;
	}

	public void setLatitudeDecPointRetrait(String latitudeDecPointRetrait) {
		this.latitudeDecPointRetrait = latitudeDecPointRetrait;
	}

	@Column(name = "longitude_dec_point_retrait")
	public String getLongitudeDecPointRetrait() {
		return longitudeDecPointRetrait;
	}

	public void setLongitudeDecPointRetrait(String longitudeDecPointRetrait) {
		this.longitudeDecPointRetrait = longitudeDecPointRetrait;
	}

	@Column(name = "horaires_ouverture_point_retrait")
	public String getHorairesOuverturePointRetrait() {
		return horairesOuverturePointRetrait;
	}

	public void setHorairesOuverturePointRetrait(String horairesOuverturePointRetrait) {
		this.horairesOuverturePointRetrait = horairesOuverturePointRetrait;
	}

	@Column(name = "active_paiement_panier_cheque")
	public Boolean getActivePaiementPanierCheque() {
		return activePaiementPanierCheque;
	}

	public void setActivePaiementPanierCheque(Boolean activePaiementPanierCheque) {
		this.activePaiementPanierCheque = activePaiementPanierCheque;
	}

	@Column(name = "active_paiement_panier_virement")
	public Boolean getActivePaiementPanierVirement() {
		return activePaiementPanierVirement;
	}

	public void setActivePaiementPanierVirement(Boolean activePaiementPanierVirement) {
		this.activePaiementPanierVirement = activePaiementPanierVirement;
	}

	@Column(name = "prix_catalogue_ht")
	public Boolean getPrixCatalogueHt() {
		return prixCatalogueHt;
	}

	public void setPrixCatalogueHt(Boolean prixCatalogueHt) {
		this.prixCatalogueHt = prixCatalogueHt;
	}

	@Column(name = "active_planning_retrait")
	public Boolean getActivePlanningRetrait() {
		return activePlanningRetrait;
	}

	public void setActivePlanningRetrait(Boolean activePlanningRetrait) {
		this.activePlanningRetrait = activePlanningRetrait;
	}
	
	@OneToOne(fetch = FetchType.EAGER, cascade = {/*CascadeType.REMOVE,*/ CascadeType.REFRESH} , orphanRemoval=false)
	@JoinColumn(name = "id_compte_banque_paiement_virement")
	public TaCompteBanque getTaCompteBanquePaiementVirement() {
		return taCompteBanquePaiementVirement;
	}

	public void setTaCompteBanquePaiementVirement(TaCompteBanque taCompteBanquePaiementVirement) {
		this.taCompteBanquePaiementVirement = taCompteBanquePaiementVirement;
	}

	@Column(name = "iban_paiement_virement")
	public String getIbanPaiementVirement() {
		return ibanPaiementVirement;
	}

	public void setIbanPaiementVirement(String ibanPaiementVirement) {
		this.ibanPaiementVirement = ibanPaiementVirement;
	}

	@Column(name = "swift_paiement_virement")
	public String getSwiftPaiementVirement() {
		return swiftPaiementVirement;
	}

	public void setSwiftPaiementVirement(String swiftPaiementVirement) {
		this.swiftPaiementVirement = swiftPaiementVirement;
	}

	@Column(name = "utilisateur_peu_creer_compte")
	public Boolean getUtilisateurPeuCreerCompte() {
		return utilisateurPeuCreerCompte;
	}

	public void setUtilisateurPeuCreerCompte(Boolean utilisateur_peu_creer_compte) {
		this.utilisateurPeuCreerCompte = utilisateur_peu_creer_compte;
	}

	@Column(name = "liaison_nouveau_compte_email_auto")
	public Boolean getLiaisonNouveauCompteEmailAuto() {
		return liaisonNouveauCompteEmailAuto;
	}

	public void setLiaisonNouveauCompteEmailAuto(Boolean liaison_nouveau_compte_email_auto) {
		this.liaisonNouveauCompteEmailAuto = liaison_nouveau_compte_email_auto;
	}

	@Column(name = "liaison_nouveau_compte_code_client_auto")
	public Boolean getLiaisonNouveauCompteCodeClientAuto() {
		return liaisonNouveauCompteCodeClientAuto;
	}

	public void setLiaisonNouveauCompteCodeClientAuto(Boolean liaison_nouveau_compte_code_client_auto) {
		this.liaisonNouveauCompteCodeClientAuto = liaison_nouveau_compte_code_client_auto;
	}

	@Column(name = "autorise_maj_donnee_par_client")
	public Boolean getAutoriseMajDonneeParClient() {
		return autoriseMajDonneeParClient;
	}

	public void setAutoriseMajDonneeParClient(Boolean autorise_maj_donnee_par_client) {
		this.autoriseMajDonneeParClient = autorise_maj_donnee_par_client;
	}

	

}
