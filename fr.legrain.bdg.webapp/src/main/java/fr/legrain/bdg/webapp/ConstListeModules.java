package fr.legrain.bdg.webapp;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;


@Named
@ViewScoped
public class ConstListeModules implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -416370070352970476L;
	private static List<String> listeModule = null;

	
//	public static String MENU_ARTICLES ="fr.legrain.Articles";
//	public static String MENU_BONLIV ="fr.legrain.BonLivraison";
//	public static String MENU_DEVIS ="fr.legrain.Devis";
//	public static String MENU_DOCUMENT ="fr.legrain.Document";
//	public static String MENU_EDITION ="fr.legrain.Edition";
//	public static String MENU_EDITION_SPECIFIQUES ="fr.legrain.EditionsSpecifiques";
//	public static String MENU_EXPORTATION ="fr.legrain.Exportation";
//	public static String MENU_FACTURE ="fr.legrain.Facture";
//	public static String MENU_GESTION_DOSSIER ="fr.legrain.GestionDossier";
//	public static String MENU_LIASSE_FISCALE ="fr.legrain.LiasseFiscale";
//	public static String MENU_SAUVEGARDE ="fr.legrain.Sauvegarde";
//	public static String MENU_STOCKS ="fr.legrain.Stocks";
//	public static String MENU_TIERS ="fr.legrain.Tiers";
//	public static String MENU_VISUALISATION ="fr.legrain.Visualisation";
//	public static String MENU_GENERATION_DOCUMENT ="fr.legrain.generationdocument";
//	public static String MENU_APPORTEUR ="fr.legrain.apporteur";
//	public static String MENU_AVOIR ="fr.legrain.avoir";
//	public static String MENU_BONCDE ="fr.legrain.bonCde";
//	public static String MENU_PROFORMA ="fr.legrain.Proforma";
//	public static String MENU_SAISIE_CAISSE ="fr.legrain.SaisieCaisse";
//	public static String MENU_IMPRESSION ="fr.legrain.document.impression";
//	public static String MENU_SAUVEGARDE_FTP ="fr.legrain.sauvegardeFTP";
//	public static String MENU_RESTAURATION_FTP ="fr.legrain.restaurationFTP";
//	public static String MENU_ARTICLES_STATISTIQUES ="fr.legrain.articles.statistiques";
//	public static String MENU_TIERS_STATISTIQUES ="fr.legrain.tiers.statistiques";
//	public static String MENU_ACOMPTE ="fr.legrain.acompte";
//	public static String MENU_REGLEMENT ="fr.legrain.reglement";
//	public static String MENU_RELANCE_FACTURE ="fr.legrain.relanceFacture";
//	public static String MENU_PRELEVEMENT ="fr.legrain.prelevement";
//	public static String MENU_PUBLIPOSTAGE ="fr.legrain.publipostage";
//	public static String MENU_PUBLIPOSTAGE_TIERS ="fr.legrain.publipostageTiers";
//	public static String MENU_ARTICLE_EXPORT_CATALOGUE ="fr.legrain.articles.export.catalogue";
//	public static String MENU_LIAISON_DOCUMENT ="fr.legrain.LiaisonDocument";
//	public static String MENU_GESTION_DMS ="fr.legrain.gestionDms";
//	public static String MENU_TRAITE ="fr.legrain.traite";
//	public static String MENU_RECHERCHE_MULTI_CRITERES ="fr.legrain.rechercherMultiCrit";
//	public static String MENU_STATISTIQUES_ACOMPTE ="fr.legrain.statistiques.acompte";
//	public static String MENU_STATISTIQUES_APPORTEUR ="fr.legrain.statistiques.apporteur";
//	public static String MENU_STATISTIQUES_AVOIR ="fr.legrain.statistiques.avoir";
//	public static String MENU_STATISTIQUES_COMMANDE ="fr.legrain.statistiques.commande";
//	public static String MENU_STATISTIQUES_DEVIS ="fr.legrain.statistiques.devis";
//	public static String MENU_STATISTIQUES_LIVRAISON ="fr.legrain.statistiques.livraison";
//	public static String MENU_STATISTIQUES_PROFORMA ="fr.legrain.statistiques.proforma";
//	public static String MENU_REMISE_CHEQUE ="fr.legrain.remiseCheque";
//	public static String MENU_CREATION_DOMENU_MULTIPLE_TIERS ="fr.legrain.CreationDocMultipleTiers";
//	public static String MENU_DOCUMENT_ETAT_DEVIS ="fr.legrain.document.etat.devis";
//	public static String MENU_DOCUMENT_ETAT_BONCDE ="fr.legrain.document.etat.boncde";
//	public static String MENU_DOCUMENT_ETAT_PRELEVEMENT ="fr.legrain.document.etat.prelevement";
//	public static String MENU_DOCUMENT_ETAT_PROFORMA ="fr.legrain.document.etat.proforma";
//	public static String MENU_OPENMAIL_MAIL ="fr.legrain.openmail.mail";
//	public static String MENU_OPENMAIL_SMS ="fr.legrain.openmail.sms";
//	public static String MENU_OPENMAIL_FAX ="fr.legrain.openmail.fax";
//	public static String MENU_GESTION_CAPSULES ="r.legrain.gestioncapsules";
//	public static String MENU_ABONNEMENT ="r.legrain.abonnement";
//	public static String MENU_POINT_BONUS ="fr.legrain.pointsBonus";
//	public static String MENU_TYPE_SUPPORT ="fr.legrain.typeSupport";
//	public static String MENU_LICENCE ="fr.legrain.licence";
//	public static String MENU_AVIS_ECHEANCE ="fr.legrain.avisEcheance";
//	public static String MENU_PRELEVEMENT_EXPORTATION ="r.legrain.prelevement.exportation";

//	public static Map<String, String> getListeModule (){
//		if(listeModule==null){
//			listeModule=new LinkedHashMap<String, String>();
//
//			listeModule.add(MENU_ARTICLES,"fr.legrain.Articles");
//			listeModule.add(MENU_BONLIV,"fr.legrain.BonLivraison");
//			listeModule.add(MENU_DEVIS,"fr.legrain.Devis");
//			listeModule.add(MENU_DOCUMENT,"fr.legrain.Document");
//			listeModule.add(MENU_EDITION,"fr.legrain.Edition");
//			listeModule.add(MENU_EDITION_SPECIFIQUES,"fr.legrain.EditionsSpecifiques");
//			listeModule.add(MENU_EXPORTATION,"fr.legrain.Exportation");
//			listeModule.add(MENU_FACTURE,"fr.legrain.Facture");
//			listeModule.add(MENU_GESTION_DOSSIER,"fr.legrain.GestionDossier");
//			listeModule.add(MENU_LIASSE_FISCALE,"fr.legrain.LiasseFiscale");
//			listeModule.add(MENU_SAUVEGARDE,"fr.legrain.Sauvegarde");
//			listeModule.add(MENU_STOCKS,"fr.legrain.Stocks");
//			listeModule.add(MENU_TIERS,"fr.legrain.Tiers");
//			listeModule.add(MENU_VISUALISATION,"fr.legrain.Visualisation");
//			listeModule.add(MENU_GENERATION_DOCUMENT,"fr.legrain.generationdocument");
//			listeModule.add(MENU_APPORTEUR,"fr.legrain.apporteur");
//			listeModule.add(MENU_AVOIR,"fr.legrain.avoir");
//			listeModule.add(MENU_BONCDE,"fr.legrain.bonCde");
//			listeModule.add(MENU_PROFORMA,"fr.legrain.Proforma");
//			listeModule.add(MENU_SAISIE_CAISSE,"fr.legrain.SaisieCaisse");
//			listeModule.add(MENU_IMPRESSION,"fr.legrain.document.impression");
//			listeModule.add(MENU_SAUVEGARDE_FTP,"fr.legrain.sauvegardeFTP");
//			listeModule.add(MENU_RESTAURATION_FTP,"fr.legrain.restaurationFTP");
//			listeModule.add(MENU_ARTICLES_STATISTIQUES,"fr.legrain.articles.statistiques");
//			listeModule.add(MENU_TIERS_STATISTIQUES,"fr.legrain.tiers.statistiques");
//			listeModule.add(MENU_ACOMPTE,"fr.legrain.acompte");
//			listeModule.add(MENU_REGLEMENT,"fr.legrain.reglement");
//			listeModule.add(MENU_RELANCE_FACTURE,"fr.legrain.relanceFacture");
//			listeModule.add(MENU_PRELEVEMENT,"fr.legrain.prelevement");
//			listeModule.add(MENU_PUBLIPOSTAGE,"fr.legrain.publipostage");
//			listeModule.add(MENU_PUBLIPOSTAGE_TIERS,"fr.legrain.publipostageTiers");
//			listeModule.add(MENU_ARTICLE_EXPORT_CATALOGUE,"fr.legrain.articles.export.catalogue");
//			listeModule.add(MENU_LIAISON_DOCUMENT,"fr.legrain.LiaisonDocument");
//			listeModule.add(MENU_GESTION_DMS,"fr.legrain.gestionDms");
//			listeModule.add(MENU_TRAITE,"fr.legrain.traite");
//			listeModule.add(MENU_RECHERCHE_MULTI_CRITERES,"fr.legrain.rechercherMultiCrit");
//			listeModule.add(MENU_STATISTIQUES_ACOMPTE,"fr.legrain.statistiques.acompte");
//			listeModule.add(MENU_STATISTIQUES_APPORTEUR,"fr.legrain.statistiques.apporteur");
//			listeModule.add(MENU_STATISTIQUES_AVOIR,"fr.legrain.statistiques.avoir");
//			listeModule.add(MENU_STATISTIQUES_COMMANDE,"fr.legrain.statistiques.commande");
//			listeModule.add(MENU_STATISTIQUES_DEVIS,"fr.legrain.statistiques.devis");
//			listeModule.add(MENU_STATISTIQUES_LIVRAISON,"fr.legrain.statistiques.livraison");
//			listeModule.add(MENU_STATISTIQUES_PROFORMA,"fr.legrain.statistiques.proforma");
//			listeModule.add(MENU_REMISE_CHEQUE,"fr.legrain.remiseCheque");
//			listeModule.add(MENU_CREATION_DOMENU_MULTIPLE_TIERS,"fr.legrain.CreationDocMultipleTiers");
//			listeModule.add(MENU_DOCUMENT_ETAT_DEVIS,"fr.legrain.document.etat.devis");
//			listeModule.add(MENU_DOCUMENT_ETAT_BONCDE,"fr.legrain.document.etat.boncde");
//			listeModule.add(MENU_DOCUMENT_ETAT_PRELEVEMENT,"fr.legrain.document.etat.prelevement");
//			listeModule.add(MENU_DOCUMENT_ETAT_PROFORMA,"fr.legrain.document.etat.proforma");
//			listeModule.add(MENU_OPENMAIL_MAIL,"fr.legrain.openmail.mail");
//			listeModule.add(MENU_OPENMAIL_SMS,"fr.legrain.openmail.sms");
//			listeModule.add(MENU_OPENMAIL_FAX,"fr.legrain.openmail.fax");
//			listeModule.add(MENU_GESTION_CAPSULES,"r.legrain.gestioncapsules");
//			listeModule.add(MENU_ABONNEMENT,"r.legrain.abonnement");
//			listeModule.add(MENU_POINT_BONUS,"fr.legrain.pointsBonus");
//			listeModule.add(MENU_TYPE_SUPPORT,"fr.legrain.typeSupport");
//			listeModule.add(MENU_LICENCE,"fr.legrain.licence");
//			listeModule.add(MENU_AVIS_ECHEANCE,"fr.legrain.avisEcheance");
//			listeModule.add(MENU_PRELEVEMENT_EXPORTATION,"r.legrain.prelevement.exportation");
//
//		}
//		return listeModule;
//	}

	public static List<String> getListeModule (){
		if(listeModule==null){
			listeModule=new LinkedList<String>();

			listeModule.add("fr.legrain.Articles");
			listeModule.add("fr.legrain.BonLivraison");
			listeModule.add("fr.legrain.Devis");
			listeModule.add("fr.legrain.Document");
			listeModule.add("fr.legrain.Edition");
			listeModule.add("fr.legrain.EditionsSpecifiques");
			listeModule.add("fr.legrain.Exportation");
			listeModule.add("fr.legrain.Facture");
			listeModule.add("fr.legrain.GestionDossier");
			listeModule.add("fr.legrain.LiasseFiscale");
			listeModule.add("fr.legrain.Sauvegarde");
			listeModule.add("fr.legrain.Stocks");
			listeModule.add("fr.legrain.Tiers");
			listeModule.add("fr.legrain.Visualisation");
			listeModule.add("fr.legrain.generationdocument");
			listeModule.add("fr.legrain.apporteur");
			listeModule.add("fr.legrain.avoir");
			listeModule.add("fr.legrain.bonCde");
			listeModule.add("fr.legrain.Proforma");
			listeModule.add("fr.legrain.SaisieCaisse");
			listeModule.add("fr.legrain.document.impression");
			listeModule.add("fr.legrain.sauvegardeFTP");
			listeModule.add("fr.legrain.restaurationFTP");
			listeModule.add("fr.legrain.articles.statistiques");
			listeModule.add("fr.legrain.tiers.statistiques");
			listeModule.add("fr.legrain.acompte");
			listeModule.add("fr.legrain.reglement");
			listeModule.add("fr.legrain.relanceFacture");
			listeModule.add("fr.legrain.prelevement");
			listeModule.add("fr.legrain.publipostage");
			listeModule.add("fr.legrain.publipostageTiers");
			listeModule.add("fr.legrain.articles.export.catalogue");
			listeModule.add("fr.legrain.LiaisonDocument");
			listeModule.add("fr.legrain.gestionDms");
			listeModule.add("fr.legrain.traite");
			listeModule.add("fr.legrain.rechercherMultiCrit");
			listeModule.add("fr.legrain.statistiques.acompte");
			listeModule.add("fr.legrain.statistiques.apporteur");
			listeModule.add("fr.legrain.statistiques.avoir");
			listeModule.add("fr.legrain.statistiques.commande");
			listeModule.add("fr.legrain.statistiques.devis");
			listeModule.add("fr.legrain.statistiques.livraison");
			listeModule.add("fr.legrain.statistiques.proforma");
			listeModule.add("fr.legrain.remiseCheque");
			listeModule.add("fr.legrain.CreationDocMultipleTiers");
			listeModule.add("fr.legrain.document.etat.devis");
			listeModule.add("fr.legrain.document.etat.boncde");
			listeModule.add("fr.legrain.document.etat.prelevement");
			listeModule.add("fr.legrain.document.etat.proforma");
			listeModule.add("fr.legrain.openmail.mail");
			listeModule.add("fr.legrain.openmail.sms");
			listeModule.add("fr.legrain.openmail.fax");
			listeModule.add("r.legrain.gestioncapsules");
			listeModule.add("r.legrain.abonnement");
			listeModule.add("fr.legrain.pointsBonus");
			listeModule.add("fr.legrain.typeSupport");
			listeModule.add("fr.legrain.licence");
			listeModule.add("fr.legrain.avisEcheance");
			listeModule.add("fr.legrain.prelevement.exportation");
			
			listeModule.add("fr.legrain.Fabrication");
			listeModule.add("fr.legrain.GestionLot");
			listeModule.add("fr.legrain.BonReception");
			listeModule.add("fr.legrain.MouvementStock");
			listeModule.add("fr.legrain.Inventaire");
			listeModule.add("fr.legrain.Entrepot");
			listeModule.add("fr.legrain.CodeBarre");
			listeModule.add("");
			listeModule.add("");
			listeModule.add("");
			listeModule.add("");
			listeModule.add("");
			listeModule.add("");
			listeModule.add("");

		}
		return listeModule;
	}

}
