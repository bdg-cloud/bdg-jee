package fr.legrain.droits.model;

/*
 * Suite à l'ajout ou la modification d'un id module, penser a mettre à jour si besoin 
 * AutorisationBean dans la webapp (au moins faire le getXXX pour l'utilisation dans les vues - menu)
 * TaAutorisationDossierService dans les EJBs (pour les service web externe disponible - en attendant de déplacer ce type de code dans la base programme)
 * et éventuellement Auth et AuthFilter dans la webapp également.
 * 
 * Ajouter les modules dans l'interface de gestion admin.moncompte.bdg.cloud
 * 
 * Dans le cas des services web externe si un nouveau service est créer avec un nouveau module, 
 * l'id en constante ici doit correspondre a celui dans la table ta_service_web_externe.id_module_bdg_autorisation
 */
public interface IModulesProgramme {
	//technique
		public static final String ID_MODULE_ARTICLE = "fr.legrain.Articles";
		public static final String ID_MODULE_TIERS = "fr.legrain.Tiers";
		public static final String ID_MODULE_DOCUMENT = "fr.legrain.Document";
		public static final String ID_MODULE_INVENTAIRE = "fr.legrain.Inventaire";
		public static final String ID_MODULE_TRACABILITE = "fr.legrain.tracabilite";
		public static final String ID_MODULE_CODE_BARRE = "fr.legrain.CodeBarre";
		public static final String ID_MODULE_ENTREPOT = "fr.legrain.Entrepot";
		public static final String ID_MODULE_FABRICATION = "fr.legrain.Fabrication";
		public static final String ID_MODULE_DEVIS = "fr.legrain.devis";
		public static final String ID_MODULE_FACTURE = "fr.legrain.facture";
		public static final String ID_MODULE_CAISSE = "fr.legrain.caisse";
		public static final String ID_MODULE_GESTION_LOTS = "fr.legrain.GestionLot";
		public static final String ID_MODULE_BON_RECEPTION = "fr.legrain.BonReception";
		public static final String ID_MODULE_MOUVEMENT_STOCK = "fr.legrain.MouvementStock";
		public static final String ID_MODULE_GESTION_CAPSULES = "fr.legrain.GestionCapsules";
		public static final String ID_MODULE_GESTION_DMS = "fr.legrain.GestionDms";
		public static final String ID_MODULE_ACHAT = "fr.legrain.Achat";
		
		
		public static final String ID_MODULE_AVOIR = "fr.legrain.avoir";
		public static final String ID_MODULE_ACOMPTE = "fr.legrain.acompte";
		public static final String ID_MODULE_BON_COMMANDE = "fr.legrain.boncommande";
		public static final String ID_MODULE_BON_COMMANDE_ACHAT = "fr.legrain.boncommandeAchat";
		public static final String ID_MODULE_BON_LIVRAISON = "fr.legrain.bonlivraison";	
		public static final String ID_MODULE_PROFORMA = "fr.legrain.proforma";
		public static final String ID_MODULE_APPORTEUR = "fr.legrain.apporteur";
		public static final String ID_MODULE_PRELEVEMENT = "fr.legrain.prelevement";
		public static final String ID_MODULE_PREPARATION = "fr.legrain.preparation";
		public static final String ID_MODULE_REMISE = "fr.legrain.remise";
		public static final String ID_MODULE_AVIS_ECHEANCE = "fr.legrain.AvisEcheance";
		public static final String ID_MODULE_PANIER = "fr.legrain.panier";
		
		public static final String  ID_MODULE_GESTION_REGLEMENT= "fr.legrain.GestionReglement"; 
	 
		
		public static final String ID_MODULE_EXPORTATION = "fr.legrain.exportation";
		public static final String ID_MODULE_EXPORTATION_EPICEA = "fr.legrain.exportation.epicea";
		public static final String ID_MODULE_EXPORTATION_AGRIGEST = "fr.legrain.exportation.agrigest";
		
		public static final String ID_MODULE_CONNEXION_DOSSIER_STRIPE = "fr.legrain.connexion.paiement.stripe";
		public static final String ID_MODULE_CONNEXION_DOSSIER_STRIPE_CONNECT = "fr.legrain.connexion.paiement.stripe.connect";
		public static final String ID_MODULE_CONNEXION_DOSSIER_MAILJET_EMAIL = "fr.legrain.connexion.email.mailjet";
		public static final String ID_MODULE_CONNEXION_DOSSIER_MAILJET_SMS = "fr.legrain.connexion.sms.mailjet";
		public static final String ID_MODULE_CONNEXION_DOSSIER_OVH_SMS = "fr.legrain.connexion.sms.ovh";
		
		public static final String ID_MODULE_IMPORTATION_SHIPPINGBO = "fr.legrain.importation.shippingbo";
		public static final String ID_MODULE_IMPORTATION_WOOCOMMERCE = "fr.legrain.importation.woocommerce";
		public static final String ID_MODULE_IMPORTATION_MENSURA = "fr.legrain.importation.mensura";
		public static final String ID_MODULE_IMPORTATION_PRESTASHOP = "fr.legrain.importation.prestashop";
		
		public static final String ID_MODULE_PARTENARIAT = "fr.legrain.partenariat";
		public static final String ID_MODULE_COMPTE_CLIENT = "fr.legrain.compteclient";

		//vendable
		public static final String ID_MODULE_SOLSTYCE = "fr.legrain.solstyce";
		public static final String ID_MODULE_REGLEMENTS = "fr.legrain.reglements";
		public static final String ID_MODULE_ACHATS = "fr.legrain.achats";
		public static final String ID_MODULE_STOCK = "fr.legrain.stock";
		public static final String ID_MODULE_FACTURATION = "fr.legrain.facturation";
		public static final String ID_MODULE_FACTURE_DEVIS = "fr.legrain.facturedevis";
		public static final String ID_MODULE_GESTION_COMMERCIALE = "fr.legrain.gestioncommerciale";
		public static final String ID_MODULE_CRM = "fr.legrain.crm";
		
		public static final String ID_MODULE_MULTI_TARIFS = "fr.legrain.multitarifs";
		
		public static final String ID_MODULE_CATALOGUE = "fr.legrain.catalogue";
		public static final String ID_MODULE_BOUTIQUE = "fr.legrain.boutique";
		public static final String ID_MODULE_ABONNEMENT = "fr.legrain.abonnement";
		public static final String ID_MODULE_ARTICLE_COMPOSE = "fr.legrain.article.compose";

}
