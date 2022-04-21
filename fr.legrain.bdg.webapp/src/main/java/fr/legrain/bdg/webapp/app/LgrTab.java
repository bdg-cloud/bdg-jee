package fr.legrain.bdg.webapp.app;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import fr.legrain.document.model.TypeDoc;

public class LgrTab implements Serializable{
	
	private String typeOnglet;
	private String titre;
	private String toolTipTitre;
	private String style = "tab tab-default";
	private String paramId;
	private String template;
	private Object data;
	private String idTab = null;
	
	//liste des editeurs pour chaque document
	private Map<String,String> tabDocCorrespondance = new HashMap<String, String>(); //<libelle><id editor>
	
	public static final String CSS_CLASS_TAB_PARAM = "tab tab-param";
	public static final String CSS_CLASS_TAB_AGENDA= "tab tab-agenda";
	public static final String CSS_CLASS_TAB_INFOS_ENTREPRISE= "tab tab-infos-entreprise";
	public static final String CSS_CLASS_TAB_DASHBOARD_ACCUEIL = "tab tab-dashboard-accueil";
	public static final String CSS_CLASS_TAB_TIERS = "tab tab-tiers";
	public static final String CSS_CLASS_TAB_REF_ARTICLE_FOURNISSEUR = "tab tab-ref-article-fournisseur";
	public static final String CSS_CLASS_TAB_ARTICLE = "tab tab-article";
	public static final String CSS_CLASS_TAB_STOCK = "tab tab-stock";
	public static final String CSS_CLASS_TAB_RECEPTION = "tab tab-reception";
	public static final String CSS_CLASS_TAB_DEVIS = "tab tab-devis";
	public static final String CSS_CLASS_TAB_DASH_ALL = "tab tab-dash-all";
	public static final String CSS_CLASS_TAB_FACTURE = "tab tab-facture";
	public static final String CSS_CLASS_TAB_TICKET_DE_CAISSE = "tab tab-ticket-caisse";
	public static final String CSS_CLASS_TAB_TICKET_DE_CAISSE_SESSION = "tab tab-ticket-caisse-session";
	public static final String CSS_CLASS_TAB_LIVRAISON = "tab tab-livraison";
	public static final String CSS_CLASS_TAB_ACOMPTE = "tab tab-acompte";
	public static final String CSS_CLASS_TAB_AVIS_ECHEANCE = "tab tab-avis-echeance";
	public static final String CSS_CLASS_TAB_APPORTEUR = "tab tab-apporteur";
	public static final String CSS_CLASS_TAB_PROFORMA = "tab tab-proforma";
	public static final String CSS_CLASS_TAB_BONCDE_ACHAT = "tab tab-boncde-achat";
	public static final String CSS_CLASS_TAB_BONCDE = "tab tab-boncde";
	public static final String CSS_CLASS_TAB_PREPARATION = "tab tab-preparation";
	public static final String CSS_CLASS_TAB_PANIER = "tab tab-panier";
//	public static final String CSS_CLASS_TAB_DEVIS = "tab tab-param";
//	public static final String CSS_CLASS_TAB_FACTURE = "tab tab-param";
	public static final String CSS_CLASS_TAB_AVOIR = "tab tab-avoir";
	public static final String CSS_CLASS_TAB_PRELEVEMENT = "tab tab-prelevement";
	public static final String CSS_CLASS_TAB_FABRICATION = "tab tab-fabrication";
	public static final String CSS_CLASS_TAB_MODELE_FABRICATION = "tab tab-modele-fabrication";
	public static final String CSS_CLASS_TAB_INVENTAIRE = "tab tab-inventaire";
	public static final String CSS_CLASS_TAB_RECHERCHE_MULTICRITERE = "tab tab-recherche-multicritere";
	public static final String CSS_CLASS_TAB_LOTS = "tab tab-lots";
	public static final String CSS_CLASS_TAB_ABONNEMENTS = "tab tab-abonnements";
	public static final String CSS_CLASS_TAB_LIAISON_EXTERNE = "tab tab-liaison-externe";
	public static final String CSS_CLASS_TAB_ABONNEMENT = "tab tab-abonnement";
	public static final String CSS_CLASS_TAB_DMS = "tab tab-dms";
	public static final String CSS_CLASS_TAB_CAPSULES = "tab tab-capsules";
	public static final String CSS_CLASS_TAB_PAIEMENT_INTERNET = "tab tab-paiement-internet";
	public static final String CSS_CLASS_TAB_EMAILS_ENVOYES = "tab tab-emails-envoyes";
	public static final String CSS_CLASS_TAB_ETAT_STOCKS = "tab tab-etat-stocks";
	public static final String CSS_CLASS_TAB_ETAT_NON_CONFORMITE = "tab tab-etat-non-conformite";
	public static final String CSS_CLASS_TAB_ETAT_FOURNISSEURS_PRODUITS = "tab tab-etat-fournisseurs-produit";
	public static final String CSS_CLASS_TAB_ETAT_PRODUITS_FOURNISSEUR = "tab tab-etat-produits-fournisseur";
	public static final String CSS_CLASS_TAB_CONTROLE_CONFORMITE = "tab tab-controle-conf";
	public static final String CSS_CLASS_TAB_MODELE_CONTROLE = "tab tab-modele-controle";
	public static final String CSS_CLASS_TAB_MODELE_GROUPE_CONTROLE = "tab tab-modele-groupe-controle";
	public static final String CSS_CLASS_TAB_TRACABILITE = "tab tab-param";
	public static final String CSS_CLASS_TAB_EXPORT_COMPTA = "tab tab-param";
	
	public static final String CSS_CLASS_TAB_IMPORTATION = "tab tab-param";
	
	public static final String CSS_CLASS_TAB_TUNNEL_VENTE_RENOUVELLEMENT = "tab tab-param";
	public static final String CSS_CLASS_TAB_LIAISON_DOSSIER_MAITRE = "tab tab-param";
	public static final String CSS_CLASS_TAB_CREATION_ESPACE_CLIENT = "tab tab-param";
	
	public static final String CSS_CLASS_TAB_SERVICE_WEB_EXTERNE = "tab tab-service-web-externe";
	public static final String CSS_CLASS_TAB_COMPTE_SERVICE_WEB_EXTERNE = "tab tab-compte-service-web-externe";
	public static final String CSS_CLASS_TAB_GENERATION_DOCUMENT_MULTIPLE = "tab tab-generation-document-multiple";
	public static final String CSS_CLASS_TAB_GENERATION_LIGNE_A_LIGNE_MULTIPLE = "tab tab-generation-ligne-a-ligne-multiple";
	public static final String CSS_CLASS_TAB_IMPRIMER_DOCUMENT= "tab tab-imprimer-document";
	public static final String CSS_CLASS_TAB_GESTION_REGLEMENT = "tab tab-gestion-reglement";
	public static final String CSS_CLASS_TAB_MON_AGENDA = "tab tab-mon-agenda";
	public static final String CSS_CLASS_TAB_REMISE = "tab tab-remise";
	public static final String CSS_CLASS_TAB_RELANCE = "tab tab-relance";
	public static final String CSS_CLASS_TAB_MULTI_TARIFS = "tab tab-multi-tarifs";
	public static final String CSS_CLASS_TAB_GRILLE_REFERENCE = "tab tab-multi-tarifs";
	public static final String CSS_CLASS_TAB_GRILLE_TARIFAIRE = "tab tab-multi-tarifs";
	public static final String CSS_CLASS_TAB_GRILLE_TARIFAIRE_TIERS = "tab tab-multi-tarifs";
	public static final String CSS_CLASS_TAB_EDITION_DOSSIER = "tab tab-edition-dossier";
	public static final String CSS_CLASS_TAB_EDITION_CATALOGUE = "tab tab-edition-catalogue";
	public static final String CSS_CLASS_TAB_REGLEMENT = "tab tab-reglement";
	public static final String CSS_CLASS_TAB_ETAT_DOCUMENT = "tab tab-etat-document";
	
	//Gandi 
	public static final String CSS_CLASS_TAB_GANDI_DOMAIN = "tab tab-gandi-domain";
	public static final String CSS_CLASS_TAB_GANDI_SIMPLE_HOSTING = "tab tab-simple-hosting";
	public static final String CSS_CLASS_TAB_GANDI_SERVER = "tab tab-gandi-server";
	public static final String CSS_CLASS_TAB_GANDI_CERTIF_SSL = "tab tab-gandi-certif-ssl";
	
	
	public static final String TYPE_TAB_REF_FOURNISSEUR_ARTICLE = "fr.legrain.onglet.refFournisseurArticle";
	public static final String TYPE_TAB_ARTICLE = "fr.legrain.onglet.article";
	public static final String TYPE_TAB_TIERS = "fr.legrain.onglet.tiers";
	public static final String TYPE_TAB_FABRICATION = "fr.legrain.onglet.creationFabrication";
	public static final String TYPE_TAB_ABONNEMENT = "fr.legrain.onglet.abonnement";
	public static final String TYPE_TAB_MODELE_FABRICATION = "fr.legrain.onglet.creationModeleFabrication";
	public static final String TYPE_TAB_BON_RECEPTION = "fr.legrain.onglet.bonReception";
	public static final String TYPE_TAB_TYPE_WEB = "fr.legrain.onglet.listeTypeWeb";
	public static final String TYPE_TAB_MOUVEMENT_STOCK = "fr.legrain.onglet.mouvementStock";
	public static final String TYPE_TAB_MOUVEMENT_INVENTAIRE = "fr.legrain.onglet.inventaire";
	public static final String TYPE_TAB_DEVIS = "fr.legrain.onglet.devis";
	
	public static final String TYPE_TAB_NIVEAU_RELANCE = "fr.legrain.onglet.niveauRelance";
	
	public static final String TYPE_TAB_DASH_DEVIS = "fr.legrain.onglet.dashDevis";
	public static final String TYPE_TAB_DASH_TIERS = "fr.legrain.onglet.dashTiers";
	public static final String TYPE_TAB_DASH_ARTICLE = "fr.legrain.onglet.dashArticle";
	public static final String TYPE_TAB_DASH_FABRICATION = "fr.legrain.onglet.dashFabrication";
	public static final String TYPE_TAB_DASH_RECEPTION = "fr.legrain.onglet.dashReception";
	public static final String TYPE_TAB_DASH_ABONNEMENT = "fr.legrain.onglet.dashAbonnement";
	public static final String TYPE_TAB_DASH_PANIER = "fr.legrain.onglet.dashPanier";
	
	public static final String TYPE_TAB_FACTURE = "fr.legrain.onglet.facture";
	public static final String TYPE_TAB_TICKET_DE_CAISSE = "fr.legrain.onglet.ticketDeCaisse";
	public static final String TYPE_TAB_TICKET_DE_CAISSE_SESSION = "fr.legrain.onglet.ticketDeCaisseSession";
	public static final String TYPE_TAB_DASH_FACTURE = "fr.legrain.onglet.dashFacture";
	public static final String TYPE_TAB_DASH_FACTURE_AVEC_ETAT = "fr.legrain.onglet.dashFactureAvecEtat";
	public static final String TYPE_TAB_DASH_REGLEMENT = "fr.legrain.onglet.dashReglement";
	public static final String TYPE_TAB_TRACABILITE = "fr.legrain.onglet.tracabilite";
	public static final String TYPE_TAB_BON_LIVRAISON = "fr.legrain.onglet.bonLivraison";
	public static final String TYPE_TAB_DASH_BONLIV = "fr.legrain.onglet.dashBonliv";
	public static final String TYPE_TAB_ACOMPTE = "fr.legrain.onglet.acompte";
	public static final String TYPE_TAB_DASH_ACOMPTE = "fr.legrain.onglet.dashAcompte";
	public static final String TYPE_TAB_DASH_APPORTEUR = "fr.legrain.onglet.dashApporteur";
	public static final String TYPE_TAB_APPORTEUR = "fr.legrain.onglet.apporteur";
	public static final String TYPE_TAB_PROFORMA = "fr.legrain.onglet.proforma";
	public static final String TYPE_TAB_DASH_PROFORMA = "fr.legrain.onglet.dashProforma";
	public static final String TYPE_TAB_BONCDE_ACHAT = "fr.legrain.onglet.boncdeAchat";
	public static final String TYPE_TAB_PREPARATION = "fr.legrain.onglet.preparation";
	public static final String TYPE_TAB_PANIER = "fr.legrain.onglet.panier";
	public static final String TYPE_TAB_BONCDE = "fr.legrain.onglet.boncde";
	public static final String TYPE_TAB_DASH_BONCDE_ACHAT = "fr.legrain.onglet.dashBoncdeAchat";
	public static final String TYPE_TAB_DASH_BONCDE = "fr.legrain.onglet.dashBoncde";
	public static final String TYPE_TAB_AVOIR = "fr.legrain.onglet.avoir";
	public static final String TYPE_TAB_DASH_AVOIR = "fr.legrain.onglet.dashAvoir";
	public static final String TYPE_TAB_PRELEVEMENT = "fr.legrain.onglet.prelevement";
	public static final String TYPE_TAB_DASH_PRELEVEMENT = "fr.legrain.onglet.dashPrelevement";
	public static final String TYPE_TAB_EXPORT_COMPTA_EPICEA = "fr.legrain.onglet.export.compta.epicea";
	public static final String TYPE_TAB_EXPORT_COMPTA_CEGID = "fr.legrain.onglet.export.compta.cegid";
	public static final String TYPE_TAB_EXPORT_COMPTA_SAGE = "fr.legrain.onglet.export.compta.sage";
	public static final String TYPE_TAB_EXPORT_COMPTA_AGRIGEST = "fr.legrain.onglet.export.compta.agrigest";
	public static final String TYPE_TAB_EXPORT_COMPTA_ISAGRI1 = "fr.legrain.onglet.export.compta.isagri1";
	public static final String TYPE_TAB_EXPORT_COMPTA_ISAGRI2 = "fr.legrain.onglet.export.compta.isagri2";
	public static final String TYPE_TAB_EXPORT_COMPTA_ISAGRIFINAL = "fr.legrain.onglet.export.compta.isagriFinal";
	public static final String TYPE_TAB_IMPORTATION = "fr.legrain.onglet.importation";
	
	public static final String TYPE_TAB_TUNNEL_VENTE_RENOUVELLEMENT = "fr.legrain.onglet.tunnelVente.tunnelVenteRenouvellement";
	public static final String TYPE_TAB_LIAISON_DOSSIER_MAITRE = "fr.legrain.onglet.liaisonDossierMaitre";
	public static final String TYPE_TAB_CREATION_ESPACE_CLIENT = "fr.legrain.onglet.creationEspaceClient";
	
	public static final String TYPE_TAB_SYNCHRONISATION_SHIPPINGBO = "fr.legrain.onglet.synchronisation.shippingbo";
	public static final String TYPE_TAB_SYNCHRONISATION_WOOCOMMERCE = "fr.legrain.onglet.synchronisation.woocommerce";
	public static final String TYPE_TAB_SYNCHRONISATION_PRESTASHOP = "fr.legrain.onglet.synchronisation.prestashop";
	public static final String TYPE_TAB_SYNCHRONISATION_MENSURA = "fr.legrain.onglet.synchronisation.mensura";
	public static final String TYPE_TAB_GENERATION_DOCUMENT_MULTIPLE = "fr.legrain.onglet.generation.document.multiple";
	public static final String TYPE_TAB_GENERATION_LIGNE_A_LIGNE_MULTIPLE = "fr.legrain.onglet.generation.ligne.a.ligne.multiple";
	public static final String TYPE_TAB_GENERATION_LALBCABR = "fr.legrain.onglet.generation.ligne.a.ligne.boncdeAchat.br";
	public static final String TYPE_TAB_GENERATION_LALFLASHPREPA = "fr.legrain.onglet.generation.ligne.a.ligne.flash.prepa";
	
	public static final String TYPE_TAB_SERVICE_WEB_EXTERNE = "fr.legrain.onglet.servicewebexterne";
	public static final String TYPE_TAB_COMPTE_SERVICE_WEB_EXTERNE = "fr.legrain.onglet.compte.servicewebexterne";
	public static final String TYPE_TAB_IMPRIMER_DOCUMENT = "fr.legrain.onglet.imprimerDocument";
	public static final String TYPE_TAB_GESTION_REGLEMENT = "fr.legrain.onglet.gestionReglement";
	public static final String TYPE_TAB_REGLEMENT = "fr.legrain.onglet.reglement";
	public static final String TYPE_TAB_REMISE = "fr.legrain.onglet.remise";
	public static final String TYPE_TAB_RELANCE= "fr.legrain.onglet.relance";
	public static final String TYPE_TAB_MULTI_TARIFS = "fr.legrain.onglet.multiTarifs";
	public static final String TYPE_TAB_GRILLE_REFERENCE_MT = "fr.legrain.onglet.grilleReferenceMT";
	public static final String TYPE_TAB_GRILLE_TARIFAIRE_MT = "fr.legrain.onglet.grilleTarifaireMT";
	public static final String TYPE_TAB_GRILLE_TARIFAIRE_TIERS_MT = "fr.legrain.onglet.grilleTarifaireTiersMT";
	public static final String TYPE_TAB_EMAILS_ENVOYES = "fr.legrain.onglet.emails.envoyes";
	public static final String TYPE_TAB_REMP_GRILLE_TARIFAIRE_TIERS_MT = "fr.legrain.onglet.rempgrilleTarifaireTiersMT";
	public static final String TYPE_TAB_AVIS_ECHEANCE = "fr.legrain.onglet.avisEcheance";
	public static final String TYPE_TAB_CRON_DOSSIER = "fr.legrain.onglet.cron.dossier";
	public static final String TYPE_TAB_DASH_AVIS_ECHEANCE = "fr.legrain.onglet.dashAvisEcheance";

	
	//Gandi
	public static final String TYPE_TAB_GANDI_DOMAIN = "fr.legrain.onglet.gandiDomain";
	public static final String TYPE_TAB_GANDI_SIMPLE_HOSTING = "fr.legrain.onglet.gandiSimpleHosting";
	public static final String TYPE_TAB_GANDI_SERVER = "fr.legrain.onglet.gandiServer";
	public static final String TYPE_TAB_GANDI_CERTIF_SSL = "fr.legrain.onglet.gandiCertifSSL";
	
	
	
	//gestion des id
	public static final String ID_TAB_ARTICLE = "idTabArticle";
	public static final String ID_TAB_TIERS = "idTabTiers";
	public static final String ID_TAB_REF_ARTICLE_FOURNISSEUR = "idTabRefArticleFournisseur";
	public static final String ID_TAB_FABRICATION = "idTabFabrication";
	public static final String ID_TAB_ABONNEMENT = "idTabAbonnement";
	public static final String ID_TAB_MODELE_FABRICATION = "idTabModeleFabrication";
	public static final String ID_TAB_BON_RECEPTION = "idTabBR";
	public static final String ID_TAB_TYPE_WEB = "idTabTypeWeb";
	public static final String ID_TAB_MOUVEMENT_STOCK = "idTabMouvements";
	public static final String ID_TAB_MOUVEMENT_INVENTAIRE = "idTabInventaire";
	public static final String ID_TAB_DEVIS = "idTabDevis";
	
	public static final String ID_TAB_DASH_ABONNEMENT = "idTabDashAbonnement";
	public static final String ID_TAB_DASH_DEVIS = "idTabDashDevis";
	public static final String ID_TAB_DASH_TIERS = "idTabDashTiers";
	public static final String ID_TAB_DASH_ARTICLE = "idTabDashArticle";
	public static final String ID_TAB_DASH_FABRICATION = "idTabDashFabrication";
	public static final String ID_TAB_DASH_RECEPTION = "idTabDashReception";
	public static final String ID_TAB_DASH_PANIER = "idTabDashPanier";
	
	public static final String ID_TAB_FACTURE = "idTabFacture";
	public static final String ID_TAB_TICKET_DE_CAISSE = "idTabTicketDeCaisse";
	public static final String ID_TAB_TICKET_DE_CAISSE_SESSION = "idTabTicketDeCaisseSession";
	public static final String ID_TAB_DASH_FACTURE = "idTabDashFacture";
	public static final String ID_TAB_DASH_FACTURE_AVEC_ETAT = "idTabDashFactureAvecEtat";
	
	public static final String ID_TAB_DASH_REGLEMENT = "idTabDashReglement";
	public static final String ID_TAB_BON_LIVRAISON = "idTabBonLivraison";
	public static final String ID_TAB_DASH_BONLIV = "idTabDashBonliv";
	public static final String ID_TAB_APPORTEUR = "idTabApporteur";
	public static final String ID_TAB_PROFORMA = "idTabProforma";
	public static final String ID_TAB_DASH_PROFORMA = "idTabDashProforma";
	public static final String ID_TAB_ACOMPTE = "idTabAcompte";
	public static final String ID_TAB_DASH_ACOMPTE = "idTabDashAcompte";
	public static final String ID_TAB_DASH_APPORTEUR = "idTabDashApporteur";
	public static final String ID_TAB_BONCDE = "idTabBoncde";
	public static final String ID_TAB_BONCDE_ACHAT = "idTabBoncdeAchat";
	public static final String ID_TAB_PREPARATION = "idTabPreparation";
	public static final String ID_TAB_PANIER = "idTabPanier";
	public static final String ID_TAB_DASH_BONCDE = "idTabDashBoncde";
	public static final String ID_TAB_DASH_BONCDE_ACHAT = "idTabDashBoncdeAchat";
	public static final String ID_TAB_TRACABILITE = "idTabTracabilite";
	public static final String ID_TAB_ETAT_TRACABILITE = "idTabEtatTracabilite";
	public static final String ID_TAB_TABLEAUBORD = "idTabDashboard";
	public static final String ID_TAB_EDITION_DOSSIER = "idTabEditionDossier";
	public static final String ID_TAB_EDITION_CATALOGUE = "idTabEditionCatalogue";
	public static final String ID_TAB_AGENDA = "idTabAgenda";
	public static final String ID_TAB_EDITION_DU_DOSSIER = "idTabEditionDuDossier";
	public static final String ID_TAB_GESTION_ACTION_INTERNE_PROG = "idTabGestionActionInterneProg";
	public static final String ID_TAB_MON_AGENDA = "idTabMonAgenda";
	public static final String ID_TAB_TYPE_EVENEMENT = "idTabTypeEvenement";
	public static final String ID_TAB_ETIQUETTE_EMAIL = "idTabEtiquetteEmail";
	public static final String ID_TAB_RECHERCHE_MULTICRITERE = "idTabRechercheMulticritere";
	public static final String ID_TAB_TYPE_BANQUE = "idTabTypeBanque";	
	public static final String ID_TAB_TYPE_CIVILITE = "idTabTypeCivilite";
	public static final String ID_TAB_TYPE_CODEBARRE = "idTabTypeCodeBarre";
	public static final String ID_TAB_TYPE_ENTITE = "idTabTypeEntite";
	public static final String ID_TAB_TYPE_TEL = "idTabTypeTel";
	public static final String ID_TAB_LISTE_TIERS = "idTabListeTiers";
	public static final String ID_TAB_LISTE_ARTICLES = "idTabListeArticles";
	public static final String ID_TAB_FAMILLE_UNITE = "idTabFamilleUnite";
	public static final String ID_TAB_CRON_DOSSIER = "idTabCronDossier";
	public static final String ID_TAB_UNITE = "idTabUnite";
	public static final String ID_TAB_COEFFICIENT_UNITE = "idTabCoefficientUnite";
	public static final String ID_TAB_ENTREPOT = "idTabEntrepot";
	public static final String ID_TAB_TYPE_FABRICATION = "idTabTypeFabrication";
	public static final String ID_TAB_TYPE_RECEPTION = "idTabTypeReception";
	public static final String ID_TAB_TYPE_EDITION_CATALOGUE = "idTabTypeEditionCatalogue";
	public static final String ID_TAB_TYPE_EDITION_DOSSIER = "idTabTypeEditionDossier";
	public static final String ID_TAB_TYPE_NOTE_TIERS = "idTabTypeNoteTiers";
	public static final String ID_TAB_TYPE_TIERS = "idTabTypeTiers";	
	public static final String ID_TAB_NIVEAU_RELANCE = "idTabNiveauRelance";
	public static final String ID_TAB_TYPE_TARIF = "idTabTypeTarif";
	public static final String ID_TAB_CATEGORIE_ARTICLE = "idTabCategorieArticle";
	public static final String ID_TAB_TYPE_EMAIL = "idTabTypeEmail";
	public static final String ID_TAB_TYPE_LIENS = "idTabTypeLiens";
	public static final String ID_TAB_FAMILLE_TIERS = "idTabFamilleTiers";
	public static final String ID_TAB_CONDITIONPAIEMENT = "idTabContitionPaiementTiers";
	public static final String ID_TAB_CONDITIONPAIEMENT_DOC = "idTabContitionPaiementDoc";
	public static final String ID_TAB_TYPE_NOTE_ARTICLES = "idTabTypeNoteArticles";
	public static final String ID_TAB_TYPE_ADRESSE = "idTabTypeAdresse";
	public static final String ID_TAB_FAMILLE_ARTICLES = "idTabFamilleArticle";
	public static final String ID_TAB_TYPE_TVA = "idTabTypeTva";
	public static final String ID_TAB_TYPE_AUTHENTIFICATION = "idTabTypeAuthentification";
	public static final String ID_TAB_TYPE_SERVICE_WEB_EXTERNE = "idTabTypeSericeWebExterne";
	public static final String ID_TAB_SERVICE_WEB_EXTERNE = "idTabSericeWebExterne";
	public static final String ID_TAB_COMPTE_SERVICE_WEB_EXTERNE = "idTabCompteSericeWebExterne";
	public static final String ID_TAB_PARTENARIAT = "idTabPartenariat";
	public static final String ID_TAB_ADMIN_UTILISATEURS = "idTabAdminUtilisateurs";
	public static final String ID_TAB_LOGS_DOSSIER = "idTabLogsDossier";
	public static final String ID_TAB_ADMIN_UTILISATEURS_WEB_SERVICES = "idTabAdminUtilisateursWebServices";
	public static final String ID_TAB_ADMIN_UTILISATEURS_LOGIN = "idTabAdminUtilisateursLogin";
	public static final String ID_TAB_ADMIN_ROLES = "idTabAdminRoles";
	public static final String ID_TAB_ADMIN_PREFERENCES = "idTabAdminPreferences";	
	public static final String ID_TAB_ADMIN_BUGS = "idTabAdminBugs";
	public static final String ID_TAB_ADMIN_VERROUILLAGE_MANUEL = "idTabAdminVerrouillageManuel";
	public static final String ID_TAB_ADMIN_CHANGELOG = "idTabAdminChangelog";
	public static final String ID_TAB_ADMIN_SVG = "idTabAdminSvg";
	public static final String ID_TAB_ADMIN_MAJ_DIVERSES_DOSSIER = "idTabAdminMajDiversesDossier";
	public static final String ID_TAB_ADMIN_MAJBDD = "idTabAdminMajBdd";
	public static final String ID_TAB_LISTE_FABRICATION = "idTabListeFabrication";
	public static final String ID_TAB_LISTE_MODELE_FABRICATION = "idTabListeModeleFabrication";
	public static final String ID_TAB_LISTE_BR = "idTabListeBR";
	public static final String ID_TAB_LISTE_DEVIS = "idTabListeDevis";
	public static final String ID_TAB_LISTE_AVOIR = "idTabListeAvoir";
	public static final String ID_TAB_LISTE_FACTURE = "idTabListeFacture";
	public static final String ID_TAB_LISTE_TICKET_DE_CAISSE = "idTabListeTicketDeCaisse";
	public static final String ID_TAB_LISTE_MOUVEMENTS = "idTabListeMouvements";
	public static final String ID_TAB_LISTE_INVENTAIRE = "idTabListeInventaire";
	public static final String ID_TAB_GESTION_LOT = "idTabGestionLot";
	public static final String ID_TAB_GESTION_ABONNEMENT = "idTabGestionAbonnement";
	public static final String ID_TAB_GESTION_LIAISON_EXTERNE = "idTabGestionLiaisonExterne";
	public static final String ID_TAB_GESTION_DMS = "idTabGestionDms";
	public static final String ID_TAB_GESTION_CAPSULES = "idTabGestionCapsules";
	public static final String ID_TAB_GESTION_PAIEMENT_INTERNET = "idTabGestionPaiementInternet";
	public static final String ID_TAB_GESTION_EMAILS_ENVOYES = "idTabGestionEmailsEnvoyes";
	public static final String ID_TAB_ETAT_NON_CONFORMITE = "idTabEtatNonConformite";
	public static final String ID_TAB_ETAT_STOCKS = "idTabEtatStocks";
	public static final String ID_TAB_ETAT_FOURNISSEURS_PRODUITS = "idTabEtatFournisseursProduit";
	public static final String ID_TAB_ETAT_PRODUITS_FOURNISSEUR = "idTabEtatProduitsFournisseur";
	public static final String ID_TAB_TYPE_PAIEMENT = "idTabTypePaiement";
	public static final String ID_TAB_ETAT_DOCUMENT = "idTabEtatDocument";
	public static final String ID_TAB_LABEL_ARTICLES = "idTabLabelArticle";
	public static final String ID_TAB_TITRE_TRANSPORT = "idTabTitreTransport";
	public static final String ID_TAB_TYPE_TRANSPORT = "idTabTypeTransport";
	public static final String ID_TAB_TRANSPORTEUR = "idTabTransporteur";
	public static final String ID_TAB_CATEGORIE_ARTICLES = "idTabCategorieArticle";
	public static final String ID_TAB_CODEBARRE = "idTabCodeBarre";
	public static final String ID_TAB_INFOENTREPRISE = "idTabInfoEntreprise";
	public static final String ID_TAB_LISTE_MODELE_CONTROLE = "idTabListeModeleControle";
	public static final String ID_TAB_GROUPE_CONTROLE = "idTabGroupeControle";
	public static final String ID_TAB_LISTE_MODELE_GROUPE_CONTROLE = "idTabListeModeleGroupeControle";	
	public static final String ID_TAB_GENCODEEX = "idTabGencodeExtended";
	public static final String ID_TAB_GESTION_COMPTE_CLIENT = "idTabGestionCompteClient";
	public static final String ID_TAB_PARAMETRE_COMPTE_CLIENT = "idTabParametreCompteClient";
	public static final String ID_TAB_MODELE_CONTROLE = "idTabModeleControle";
	public static final String ID_TAB_MODELE_GROUPE_CONTROLE = "idTabModeleGroupeControle";
	public static final String ID_TAB_MARQUE_ARTICLE = "idTabMarqueArticle";
	public static final String ID_TAB_AVOIR = "idTabAvoir";
	public static final String ID_TAB_DASH_AVOIR = "idTabDashAvoir";
	public static final String ID_TAB_PRELEVEMENT = "idTabPrelevement";
	public static final String ID_TAB_DASH_PRELEVEMENT = "idTabDashPrelevement";
	public static final String ID_TAB_EXPORT_COMPTA_EPICEA= "idTabExportComptaEpicea";
	public static final String ID_TAB_EXPORT_COMPTA_CEGID= "idTabExportComptaCegid";
	public static final String ID_TAB_EXPORT_COMPTA_SAGE= "idTabExportComptaSage";
	public static final String ID_TAB_EXPORT_COMPTA_AGRIGEST= "idTabExportComptaAgrigest";
	public static final String ID_TAB_EXPORT_COMPTA_ISAGRI1= "idTabExportComptaIsagri1";
	public static final String ID_TAB_EXPORT_COMPTA_ISAGRI2= "idTabExportComptaIsagri2";
	public static final String ID_TAB_EXPORT_COMPTA_ISAGRIFINAL= "idTabExportComptaIsagriFinal";
	public static final String ID_TAB_IMPORTATION= "idTabImportation";
	
	public static final String ID_TAB_TUNNEL_VENTE_RENOUVELLEMENT = "idTabTunnelVenteRenouvellement";
	public static final String ID_TAB_LIAISON_DOSSIER_MAITRE = "idTabLiaisonDossierMaitre";
	public static final String ID_TAB_CREATION_ESPACE_CLIENT = "idTabCreationEspaceClient";
	
	public static final String ID_TAB_SYNCHRONISATION_SHIPPINGBO= "idTabSynchronisationShippingBo";
	public static final String ID_TAB_SYNCHRONISATION_WOOCOMMERCE= "idTabSynchronisationWooCommerce";
	public static final String ID_TAB_SYNCHRONISATION_PRESTASHOP= "idTabSynchronisationPrestashop";
	public static final String ID_TAB_SYNCHRONISATION_MENSURA= "idTabSynchronisationMensura";
	public static final String ID_TAB_GENERATION_DOCUMENT_MULTIPLE = "idTabGenerationDocumentMultiple";
	public static final String ID_TAB_GENERATION_LIGNE_A_LIGNE_MULTIPLE = "idTabGenerationLigneALigneMultiple";
	public static final String ID_TAB_GENERATION_LIGNE_A_LIGNE_BCDEACHAT_BR = "idTabGenerationLigneALigneBcdeAchatBonReception";
	public static final String ID_TAB_GENERATION_LIGNE_A_LIGNE_FLASH_PREPARATION = "idTabGenerationLigneALigneFlashPreparation";
//	public static final String ID_TAB_GENERATION_LIGNE_A_LIGNE_FLASH_PANIER = "idTabGenerationLigneALigneFlashPanier";
	public static final String ID_TAB_IMPRIMER_DOCUMENT= "idTabImprimerDocument";
	public static final String ID_TAB_GESTION_REGLEMENT = "idTabGestionReglement";
	public static final String ID_TAB_REGLEMENT = "idTabReglement";
	public static final String ID_TAB_REMISE = "idTabRemise";
	public static final String ID_TAB_RELANCE = "idTabRelance";
	public static final String ID_TAB_MULTI_TARIFS = "idTabMultiTarifs";
	public static final String ID_TAB_GRILLE_REFERENCE = "idTabGrilleReference";
	public static final String ID_TAB_GRILLE_TARIFAIRE = "idTabGrilleTarifaire";
	public static final String ID_TAB_REMP_GRILLE_TARIFAIRE_TIERS = "idTabRempGrilleTarifaireTiers";
	public static final String ID_TAB_GRILLE_TARIFAIRE_TIERS = "idTabGrilleTarifaireTiers";
	public static final String ID_TAB_DASHBOARD_TIERS_GENERAL = "idtabViewDashBoardDetailTiers";
	public static final String ID_TAB_AVIS_ECHEANCE = "idTabAvisEcheance";
	public static final String ID_TAB_DASH_AVIS_ECHEANCE = "idTabDashAvisEcheance";
	
	public static final String ID_TAB_GANDI_SIMPLE_HOSTING = "idtabGandiSimpleHosting";
	public static final String ID_TAB_GANDI_SERVER = "idtabGandiServer";
	public static final String ID_TAB_GANDI_DOMAIN = "idtabGandiDomain";
	public static final String ID_TAB_GANDI_CERTIF_SSL = "idtabGandiCertifSSL";


	public static final String WV_TAB_DEVIS = "wvidTabDevis";
	public static final String WV_TAB_FACTURE = "wvidTabFacture";
	public static final String WV_TAB_TICKET_DE_CAISSE = "wvidTabTicketDeCaisse";
	public static final String WV_TAB_ACOMPTE = "wvidTabAcompte";
	public static final String WV_TAB_BONCDE = "wvidTabBoncde";
	public static final String WV_TAB_BONCDE_ACHAT = "wvidTabBoncdeAchat";
	public static final String WV_TAB_APPORTEUR = "wvidTabApporteur";
	public static final String WV_TAB_PROFORMA = "wvidTabProforma";
	public static final String WV_TAB_AVIS_ECHEANCE = "wvidTabAvisEcheance";
	public static final String WV_TAB_PRELEVEMENT = "wvidTabPrelevement";
	public static final String WV_TAB_BON_RECEPTION = "wvidTabBonReception";
	public static final String WV_TAB_ABONNEMENT = "wvidTabAbonnement";
	public static final String WV_TAB_PREPARATION = "wvidTabPreparation";
	public static final String WV_TAB_PANIER = "wvidTabPanier";
	
	
	private static LgrTab instance = null;
	
	public LgrTab() {
		
	}
	
	public static LgrTab getInstance() {
		if(instance==null) {
			instance = new LgrTab(); 
			instance.tabDocCorrespondance.put(TypeDoc.TYPE_FACTURE, CSS_CLASS_TAB_FACTURE);
			instance.tabDocCorrespondance.put(TypeDoc.TYPE_DEVIS, CSS_CLASS_TAB_DEVIS);
			instance.tabDocCorrespondance.put(TypeDoc.TYPE_BON_LIVRAISON, CSS_CLASS_TAB_LIVRAISON);
			instance.tabDocCorrespondance.put(TypeDoc.TYPE_APPORTEUR, CSS_CLASS_TAB_APPORTEUR);
			instance.tabDocCorrespondance.put(TypeDoc.TYPE_BON_COMMANDE, CSS_CLASS_TAB_BONCDE);
			instance.tabDocCorrespondance.put(TypeDoc.TYPE_BON_COMMANDE_ACHAT, CSS_CLASS_TAB_BONCDE_ACHAT);
			instance.tabDocCorrespondance.put(TypeDoc.TYPE_AVOIR, CSS_CLASS_TAB_AVOIR);
			instance.tabDocCorrespondance.put(TypeDoc.TYPE_PROFORMA, CSS_CLASS_TAB_PROFORMA);
			instance.tabDocCorrespondance.put(TypeDoc.TYPE_ACOMPTE, CSS_CLASS_TAB_ACOMPTE);
			instance.tabDocCorrespondance.put(TypeDoc.TYPE_PRELEVEMENT, CSS_CLASS_TAB_PRELEVEMENT);
				instance.tabDocCorrespondance.put(TypeDoc.TYPE_REGLEMENT,CSS_CLASS_TAB_REGLEMENT );
				instance.tabDocCorrespondance.put(TypeDoc.TYPE_REMISE,CSS_CLASS_TAB_REMISE );
			instance.tabDocCorrespondance.put(TypeDoc.TYPE_AVIS_ECHEANCE, CSS_CLASS_TAB_AVIS_ECHEANCE);
			instance.tabDocCorrespondance.put(TypeDoc.TYPE_PREPARATION, CSS_CLASS_TAB_PREPARATION);
			instance.tabDocCorrespondance.put(TypeDoc.TYPE_PANIER, CSS_CLASS_TAB_PANIER);
			instance.tabDocCorrespondance.put(TypeDoc.TYPE_BON_RECEPTION, CSS_CLASS_TAB_RECEPTION);
			instance.tabDocCorrespondance.put(TypeDoc.TYPE_FABRICATION, CSS_CLASS_TAB_FABRICATION);
		}
		return instance;
	}	
		
	public String getTypeOnglet() {
		return typeOnglet;
	}
	
	public void setTypeOnglet(String typeOnglet) {
		this.typeOnglet = typeOnglet;
	}
	
	public Object getData() {
		return data;
	}
	
	public void setData(Object data) {
		this.data = data;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getParamId() {
		return paramId;
	}

	public void setParamId(String paramId) {
		this.paramId = paramId;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getToolTipTitre() {
		return toolTipTitre;
	}

	public void setToolTipTitre(String toolTipTitre) {
		this.toolTipTitre = toolTipTitre;
	}

	public String getIdTab() {
		return idTab;
	}

	public void setIdTab(String idTab) {
		this.idTab = idTab;
	}

	public Map<String, String> getTabDocCorrespondance() {
		return tabDocCorrespondance;
	}

	public void setTabDocCorrespondance(Map<String, String> tabDocCorrespondance) {
		this.tabDocCorrespondance = tabDocCorrespondance;
	}
	
}
