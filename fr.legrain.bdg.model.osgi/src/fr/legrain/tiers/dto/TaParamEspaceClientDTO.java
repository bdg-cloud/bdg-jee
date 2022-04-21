package fr.legrain.tiers.dto;

import java.math.BigDecimal;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.tiers.model.TaAdresse;
import fr.legrain.tiers.model.TaCompteBanque;

public class TaParamEspaceClientDTO extends ModelObject implements java.io.Serializable {


	private static final long serialVersionUID = 2371159789946188318L;
	private Integer id;
	
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
	private Boolean afficherPrixCatalogue = false;
	
	private Integer idArticleFdp;
	private String codeArticleFdp;
	private String libelleArticleFdp;
	private BigDecimal prixPrixArticleFdp;
	private BigDecimal prixttcPrixArticleFdp;
	private String codeTvaArticleFdp;
	private BigDecimal tauxTvaArticleFdp;
	
	private Boolean activeLivraison = false;
	private Boolean activePaiementPanierCB = false;
	private Boolean activePaiementPanierSurPlace = false;
	private String generationDocAuPaiementPanier = "";
	private Boolean activeEmailConfirmationCmd = false;
	
	private String raisonSociale;
	private String contactEmail;
	private String contactWeb;
	private String contactTel;
	private String adresse1;
	private String adresse2;
	private String adresse3;
	private String codePostal;
	private String ville;
	private String pays;
	
	private byte[] cgvFileCatWeb;
	private String cgvCatWeb;
	private BigDecimal fraisPortFixe;
	private BigDecimal fraisPortLimiteOffert;
	private String texteConfirmationPaiementBoutique;
	
	private Boolean activePointRetrait = false;

	private Integer idAdressePointRetrait; 
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
	
	private Boolean utilisateurPeuCreerCompte = false;
	private Boolean liaisonNouveauCompteEmailAuto = false;
	private Boolean liaisonNouveauCompteCodeClientAuto = false;
	private Boolean autoriseMajDonneeParClient = false;
	
	private Integer idCompteBanquePaiementVirement;
	private String ibanPaiementVirement;
	private String swiftPaiementVirement;

	private Integer versionObj;

	public TaParamEspaceClientDTO() {
	}

	public void setEspaceClientDTO(TaParamEspaceClientDTO taEspaceClientDTO) {
		this.id = taEspaceClientDTO.id;

	}

	public static TaParamEspaceClientDTO copy(TaParamEspaceClientDTO taEspaceClientDTO){
		TaParamEspaceClientDTO taEspaceClientDTOLoc = new TaParamEspaceClientDTO();
		taEspaceClientDTOLoc.setId(taEspaceClientDTO.getId());                               
		return taEspaceClientDTOLoc;
	}
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}
	
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public Boolean getAfficheDevis() {
		return afficheDevis;
	}

	public void setAfficheDevis(Boolean afficheDevis) {
		this.afficheDevis = afficheDevis;
	}

	public Boolean getAfficheFactures() {
		return afficheFactures;
	}

	public void setAfficheFactures(Boolean afficheFactures) {
		this.afficheFactures = afficheFactures;
	}

	public Boolean getAfficheCommandes() {
		return afficheCommandes;
	}

	public void setAfficheCommandes(Boolean afficheCommandes) {
		this.afficheCommandes = afficheCommandes;
	}

	public Boolean getAfficheLivraisons() {
		return afficheLivraisons;
	}

	public void setAfficheLivraisons(Boolean afficheLivraisons) {
		this.afficheLivraisons = afficheLivraisons;
	}

	public Boolean getPaiementCb() {
		return paiementCb;
	}

	public void setPaiementCb(Boolean paiementCb) {
		this.paiementCb = paiementCb;
	}

	public Boolean getEspaceClientActif() {
		return espaceClientActif;
	}

	public void setEspaceClientActif(Boolean espaceClientActif) {
		this.espaceClientActif = espaceClientActif;
	}

	public byte[] getLogoLogin() {
		return logoLogin;
	}

	public void setLogoLogin(byte[] logoLogin) {
		this.logoLogin = logoLogin;
	}



	public byte[] getLogoFooter() {
		return logoFooter;
	}

	public void setLogoFooter(byte[] logoFooter) {
		this.logoFooter = logoFooter;
	}

	public byte[] getLogoPagesSimples() {
		return logoPagesSimples;
	}

	public void setLogoPagesSimples(byte[] logoPagesSimples) {
		this.logoPagesSimples = logoPagesSimples;
	}

	public byte[] getLogoHeader() {
		return logoHeader;
	}

	public void setLogoHeader(byte[] logoHeader) {
		this.logoHeader = logoHeader;
	}

	public byte[] getImageBackgroundLogin() {
		return imageBackgroundLogin;
	}

	public void setImageBackgroundLogin(byte[] imageBackgroundLogin) {
		this.imageBackgroundLogin = imageBackgroundLogin;
	}

	public String getNomImageLogoLogin() {
		return nomImageLogoLogin;
	}

	public void setNomImageLogoLogin(String nomImageLogoLogin) {
		this.nomImageLogoLogin = nomImageLogoLogin;
	}

	public String getNomImageLogoPagesSimples() {
		return nomImageLogoPagesSimples;
	}

	public void setNomImageLogoPagesSimples(String nomImageLogoPagesSimples) {
		this.nomImageLogoPagesSimples = nomImageLogoPagesSimples;
	}

	public String getNomImageLogoHeader() {
		return nomImageLogoHeader;
	}

	public void setNomImageLogoHeader(String nomImageLogoHeader) {
		this.nomImageLogoHeader = nomImageLogoHeader;
	}

	public String getNomImageLogoFooter() {
		return nomImageLogoFooter;
	}

	public void setNomImageLogoFooter(String nomImageLogoFooter) {
		this.nomImageLogoFooter = nomImageLogoFooter;
	}

	public String getNomImageBackgroundLogin() {
		return nomImageBackgroundLogin;
	}

	public void setNomImageBackgroundLogin(String nomImageBackgroundLogin) {
		this.nomImageBackgroundLogin = nomImageBackgroundLogin;
	}

	public String getUrlPerso() {
		return urlPerso;
	}

	public void setUrlPerso(String urlPerso) {
		this.urlPerso = urlPerso;
	}

	public String getTexteLogin1() {
		return texteLogin1;
	}

	public void setTexteLogin1(String texteLogin1) {
		this.texteLogin1 = texteLogin1;
	}

	public String getTexteLogin2() {
		return texteLogin2;
	}

	public void setTexteLogin2(String texteLogin2) {
		this.texteLogin2 = texteLogin2;
	}

	public String getTexteAccueil() {
		return texteAccueil;
	}

	public void setTexteAccueil(String texteAccueil) {
		this.texteAccueil = texteAccueil;
	}

	public String getThemeDefaut() {
		return themeDefaut;
	}

	public void setThemeDefaut(String themeDefaut) {
		this.themeDefaut = themeDefaut;
	}

	public Boolean getAfficheAvisEcheance() {
		return afficheAvisEcheance;
	}

	public void setAfficheAvisEcheance(Boolean afficheAvisEcheance) {
		this.afficheAvisEcheance = afficheAvisEcheance;
	}

	public Boolean getMiseADispositionAutomatiqueDesFactures() {
		return miseADispositionAutomatiqueDesFactures;
	}

	public void setMiseADispositionAutomatiqueDesFactures(Boolean miseADispositionAutomatiqueDesFactures) {
		this.miseADispositionAutomatiqueDesFactures = miseADispositionAutomatiqueDesFactures;
	}

	public Boolean getAffichePublicite() {
		return affichePublicite;
	}

	public void setAffichePublicite(Boolean affichePublicite) {
		this.affichePublicite = affichePublicite;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getContactWeb() {
		return contactWeb;
	}

	public void setContactWeb(String contactWeb) {
		this.contactWeb = contactWeb;
	}
	
	public String getContactTel() {
		return contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}

	public String getAdresse1() {
		return adresse1;
	}

	public void setAdresse1(String adresse1) {
		this.adresse1 = adresse1;
	}

	public String getAdresse2() {
		return adresse2;
	}

	public void setAdresse2(String adresse2) {
		this.adresse2 = adresse2;
	}

	public String getAdresse3() {
		return adresse3;
	}

	public void setAdresse3(String adresse3) {
		this.adresse3 = adresse3;
	}

	public String getCodePostal() {
		return codePostal;
	}

	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public String getPays() {
		return pays;
	}

	public void setPays(String pays) {
		this.pays = pays;
	}

	public Boolean getAfficheBoutique() {
		return afficheBoutique;
	}

	public void setAfficheBoutique(Boolean afficheBoutique) {
		this.afficheBoutique = afficheBoutique;
	}

	public Boolean getAfficheCatalogue() {
		return afficheCatalogue;
	}

	public void setAfficheCatalogue(Boolean afficheCatalogue) {
		this.afficheCatalogue = afficheCatalogue;
	}

	public Boolean getActivePanier() {
		return activePanier;
	}

	public void setActivePanier(Boolean activePanier) {
		this.activePanier = activePanier;
	}

	public Boolean getActiveEmailRenseignementProduit() {
		return activeEmailRenseignementProduit;
	}

	public void setActiveEmailRenseignementProduit(Boolean activeEmailRenseignementProduit) {
		this.activeEmailRenseignementProduit = activeEmailRenseignementProduit;
	}

	public Boolean getActiveCommandeEmailSimple() {
		return activeCommandeEmailSimple;
	}

	public void setActiveCommandeEmailSimple(Boolean activeCommandeEmailSimple) {
		this.activeCommandeEmailSimple = activeCommandeEmailSimple;
	}

	public String getTexteAccueilCatalogue() {
		return texteAccueilCatalogue;
	}

	public void setTexteAccueilCatalogue(String texteAccueilCatalogue) {
		this.texteAccueilCatalogue = texteAccueilCatalogue;
	}

	public byte[] getCgvFileCatWeb() {
		return cgvFileCatWeb;
	}

	public void setCgvFileCatWeb(byte[] cgvFileCatWeb) {
		this.cgvFileCatWeb = cgvFileCatWeb;
	}

	public String getCgvCatWeb() {
		return cgvCatWeb;
	}

	public void setCgvCatWeb(String cgvCatWeb) {
		this.cgvCatWeb = cgvCatWeb;
	}

	public BigDecimal getFraisPortFixe() {
		return fraisPortFixe;
	}

	public void setFraisPortFixe(BigDecimal fraisPortFixe) {
		this.fraisPortFixe = fraisPortFixe;
	}

	public BigDecimal getFraisPortLimiteOffert() {
		return fraisPortLimiteOffert;
	}

	public void setFraisPortLimiteOffert(BigDecimal fraisPortLimiteOffert) {
		this.fraisPortLimiteOffert = fraisPortLimiteOffert;
	}

	public Boolean getAfficherPrixCatalogue() {
		return afficherPrixCatalogue;
	}

	public void setAfficherPrixCatalogue(Boolean afficherPrixCatalogue) {
		this.afficherPrixCatalogue = afficherPrixCatalogue;
	}

	public Integer getIdArticleFdp() {
		return idArticleFdp;
	}

	public void setIdArticleFdp(Integer idArticleFdp) {
		this.idArticleFdp = idArticleFdp;
	}

	public String getCodeArticleFdp() {
		return codeArticleFdp;
	}

	public void setCodeArticleFdp(String codeArticleFdp) {
		this.codeArticleFdp = codeArticleFdp;
	}

	public String getLibelleArticleFdp() {
		return libelleArticleFdp;
	}

	public void setLibelleArticleFdp(String libelleArticleFdp) {
		this.libelleArticleFdp = libelleArticleFdp;
	}

	public BigDecimal getPrixPrixArticleFdp() {
		return prixPrixArticleFdp;
	}

	public void setPrixPrixArticleFdp(BigDecimal prixPrixArticleFdp) {
		this.prixPrixArticleFdp = prixPrixArticleFdp;
	}

	public BigDecimal getPrixttcPrixArticleFdp() {
		return prixttcPrixArticleFdp;
	}

	public void setPrixttcPrixArticleFdp(BigDecimal prixttcPrixArticleFdp) {
		this.prixttcPrixArticleFdp = prixttcPrixArticleFdp;
	}

	public String getCodeTvaArticleFdp() {
		return codeTvaArticleFdp;
	}

	public void setCodeTvaArticleFdp(String codeTvaArticleFdp) {
		this.codeTvaArticleFdp = codeTvaArticleFdp;
	}

	public BigDecimal getTauxTvaArticleFdp() {
		return tauxTvaArticleFdp;
	}

	public void setTauxTvaArticleFdp(BigDecimal tauxTvaArticleFdp) {
		this.tauxTvaArticleFdp = tauxTvaArticleFdp;
	}

	public Boolean getActiveLivraison() {
		return activeLivraison;
	}

	public void setActiveLivraison(Boolean activeLivraison) {
		this.activeLivraison = activeLivraison;
	}

	public Boolean getActivePaiementPanierCB() {
		return activePaiementPanierCB;
	}

	public void setActivePaiementPanierCB(Boolean activePaiementPanierCB) {
		this.activePaiementPanierCB = activePaiementPanierCB;
	}

	public Boolean getActivePaiementPanierSurPlace() {
		return activePaiementPanierSurPlace;
	}

	public void setActivePaiementPanierSurPlace(Boolean activePaiementPanierSurPlace) {
		this.activePaiementPanierSurPlace = activePaiementPanierSurPlace;
	}

	public String getGenerationDocAuPaiementPanier() {
		return generationDocAuPaiementPanier;
	}

	public void setGenerationDocAuPaiementPanier(String generationDocAuPaiementPanier) {
		this.generationDocAuPaiementPanier = generationDocAuPaiementPanier;
	}

	public Boolean getActiveEmailConfirmationCmd() {
		return activeEmailConfirmationCmd;
	}

	public void setActiveEmailConfirmationCmd(Boolean activeEmailConfirmationCmd) {
		this.activeEmailConfirmationCmd = activeEmailConfirmationCmd;
	}

	public String getTexteConfirmationPaiementBoutique() {
		return texteConfirmationPaiementBoutique;
	}

	public void setTexteConfirmationPaiementBoutique(String texteConfirmationPaiementBoutique) {
		this.texteConfirmationPaiementBoutique = texteConfirmationPaiementBoutique;
	}

	public Boolean getActivePointRetrait() {
		return activePointRetrait;
	}

	public void setActivePointRetrait(Boolean activePointRetrait) {
		this.activePointRetrait = activePointRetrait;
	}

	public Integer getIdAdressePointRetrait() {
		return idAdressePointRetrait;
	}

	public void setIdAdressePointRetrait(Integer idAdressePointRetrait) {
		this.idAdressePointRetrait = idAdressePointRetrait;
	}

	public String getAdresse1PointRetrait() {
		return adresse1PointRetrait;
	}

	public void setAdresse1PointRetrait(String adresse1PointRetrait) {
		this.adresse1PointRetrait = adresse1PointRetrait;
	}

	public String getAdresse2PointRetrait() {
		return adresse2PointRetrait;
	}

	public void setAdresse2PointRetrait(String adresse2PointRetrait) {
		this.adresse2PointRetrait = adresse2PointRetrait;
	}

	public String getAdresse3PointRetrait() {
		return adresse3PointRetrait;
	}

	public void setAdresse3PointRetrait(String adresse3PointRetrait) {
		this.adresse3PointRetrait = adresse3PointRetrait;
	}

	public String getCodepostalPointRetrait() {
		return codepostalPointRetrait;
	}

	public void setCodepostalPointRetrait(String codepostalPointRetrait) {
		this.codepostalPointRetrait = codepostalPointRetrait;
	}

	public String getVillePointRetrait() {
		return villePointRetrait;
	}

	public void setVillePointRetrait(String villePointRetrait) {
		this.villePointRetrait = villePointRetrait;
	}

	public String getPaysPointRetrait() {
		return paysPointRetrait;
	}

	public void setPaysPointRetrait(String paysPointRetrait) {
		this.paysPointRetrait = paysPointRetrait;
	}

	public String getLatitudeDecPointRetrait() {
		return latitudeDecPointRetrait;
	}

	public void setLatitudeDecPointRetrait(String latitudeDecPointRetrait) {
		this.latitudeDecPointRetrait = latitudeDecPointRetrait;
	}

	public String getLongitudeDecPointRetrait() {
		return longitudeDecPointRetrait;
	}

	public void setLongitudeDecPointRetrait(String longitudeDecPointRetrait) {
		this.longitudeDecPointRetrait = longitudeDecPointRetrait;
	}

	public String getHorairesOuverturePointRetrait() {
		return horairesOuverturePointRetrait;
	}

	public void setHorairesOuverturePointRetrait(String horairesOuverturePointRetrait) {
		this.horairesOuverturePointRetrait = horairesOuverturePointRetrait;
	}

	public Boolean getActivePaiementPanierCheque() {
		return activePaiementPanierCheque;
	}

	public void setActivePaiementPanierCheque(Boolean activePaiementPanierCheque) {
		this.activePaiementPanierCheque = activePaiementPanierCheque;
	}

	public Boolean getActivePaiementPanierVirement() {
		return activePaiementPanierVirement;
	}

	public void setActivePaiementPanierVirement(Boolean activePaiementPanierVirement) {
		this.activePaiementPanierVirement = activePaiementPanierVirement;
	}

	public Boolean getPrixCatalogueHt() {
		return prixCatalogueHt;
	}

	public void setPrixCatalogueHt(Boolean prixCatalogueHt) {
		this.prixCatalogueHt = prixCatalogueHt;
	}

	public Boolean getActivePlanningRetrait() {
		return activePlanningRetrait;
	}

	public void setActivePlanningRetrait(Boolean activePlanningRetrait) {
		this.activePlanningRetrait = activePlanningRetrait;
	}

	public String getIbanPaiementVirement() {
		return ibanPaiementVirement;
	}

	public void setIbanPaiementVirement(String ibanPaiementVirement) {
		this.ibanPaiementVirement = ibanPaiementVirement;
	}

	public String getSwiftPaiementVirement() {
		return swiftPaiementVirement;
	}

	public void setSwiftPaiementVirement(String swiftPaiementVirement) {
		this.swiftPaiementVirement = swiftPaiementVirement;
	}

	public Integer getIdCompteBanquePaiementVirement() {
		return idCompteBanquePaiementVirement;
	}

	public void setIdCompteBanquePaiementVirement(Integer idCompteBanquePaiementVirement) {
		this.idCompteBanquePaiementVirement = idCompteBanquePaiementVirement;
	}

	public String getRaisonSociale() {
		return raisonSociale;
	}

	public void setRaisonSociale(String raisonSociale) {
		this.raisonSociale = raisonSociale;
	}

	public Boolean getUtilisateurPeuCreerCompte() {
		return utilisateurPeuCreerCompte;
	}

	public void setUtilisateurPeuCreerCompte(Boolean utilisateurPeuCreerCompte) {
		this.utilisateurPeuCreerCompte = utilisateurPeuCreerCompte;
	}

	public Boolean getLiaisonNouveauCompteEmailAuto() {
		return liaisonNouveauCompteEmailAuto;
	}

	public void setLiaisonNouveauCompteEmailAuto(Boolean liaisonNouveauCompteEmailAuto) {
		this.liaisonNouveauCompteEmailAuto = liaisonNouveauCompteEmailAuto;
	}

	public Boolean getLiaisonNouveauCompteCodeClientAuto() {
		return liaisonNouveauCompteCodeClientAuto;
	}

	public void setLiaisonNouveauCompteCodeClientAuto(Boolean liaisonNouveauCompteCodeClientAuto) {
		this.liaisonNouveauCompteCodeClientAuto = liaisonNouveauCompteCodeClientAuto;
	}

	public Boolean getAutoriseMajDonneeParClient() {
		return autoriseMajDonneeParClient;
	}

	public void setAutoriseMajDonneeParClient(Boolean autoriseMajDonneeParClient) {
		this.autoriseMajDonneeParClient = autoriseMajDonneeParClient;
	}

}
