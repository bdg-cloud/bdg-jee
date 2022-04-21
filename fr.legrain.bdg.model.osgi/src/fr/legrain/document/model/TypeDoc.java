package fr.legrain.document.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaFabrication;
import fr.legrain.tiers.model.TaTiers;



public class TypeDoc {
	
	
	 public static final String TYPE_TOUS = "Tous";// "Facture";
	 	
	 	public static final String PATH_IMAGE_ROUE_DENTEE="btn/roue-dentee.svg";
	 
	    public static final String TYPE_FACTURE = TaFacture.TYPE_DOC;// "Facture";
	    public static final String TYPE_DEVIS = TaDevis.TYPE_DOC; //"Devis";
	    public static final String TYPE_BON_LIVRAISON = TaBonliv.TYPE_DOC; //"Bon de livraison";
	    public static final String TYPE_BON_RECEPTION = TaBonReception.TYPE_DOC; //"Bon de réception";
	    public static final String TYPE_APPORTEUR = TaApporteur.TYPE_DOC; //"Apporteur";
	    public static final String TYPE_BON_COMMANDE =TaBoncde.TYPE_DOC; //"Bon de commande";
	    public static final String TYPE_BON_COMMANDE_ACHAT =TaBoncdeAchat.TYPE_DOC; //"Bon de commande achat";
	    public static final String TYPE_AVOIR = TaAvoir.TYPE_DOC; //"Avoir";
	    public static final String TYPE_PROFORMA = TaProforma.TYPE_DOC; //"Proforma";
	    public static final String TYPE_ACOMPTE = TaAcompte.TYPE_DOC; //"Acompte";
	    public static final String TYPE_PRELEVEMENT = TaPrelevement.TYPE_DOC; //"Prelevement";
	    public static final String TYPE_REGLEMENT = TaReglement.TYPE_DOC; //"Reglement";
	    public static final String TYPE_REMISE = TaRemise.TYPE_DOC; //"Remise";
	    public static final String TYPE_TIERS = TaTiers.TYPE_DOC; //"";
	    public static final String TYPE_ARTICLE = TaArticle.TYPE_DOC; //"";
	    public static final String TYPE_AVIS_ECHEANCE = TaAvisEcheance.TYPE_DOC; //"AvisEcheance";
	    public static final String TYPE_TICKET_DE_CAISSE = TaTicketDeCaisse.TYPE_DOC; //"TicketDeCaisse";
	    public static final String TYPE_PREPARATION =TaPreparation.TYPE_DOC; //"Preparation";
	    public static final String TYPE_PANIER = TaPanier.TYPE_DOC; // Panier
	    public static final String TYPE_FLASH =TaFlash.TYPE_DOC; //"Bon de flashage";
	    
	    
	    public static final String TYPE_FABRICATION = TaFabrication.TYPE_DOC; //


	    public static final String TYPE_FACTURE_BUNDLEID = "Facture";
	    public static final String TYPE_DEVIS_BUNDLEID = "Devis";
	    public static final String TYPE_BON_RECEPTION_BUNDLEID = "BonReception"; //"Bon de réception";
	    public static final String TYPE_BON_LIVRAISON_BUNDLEID = "BonLivraison";
	    public static final String TYPE_APPORTEUR_BUNDLEID = "Apporteur";
	    public static final String TYPE_BON_COMMANDE_BUNDLEID = "Boncde";
	    public static final String TYPE_BON_COMMANDE_ACHAT_BUNDLEID = "BoncdeAchat";
	    public static final String TYPE_AVOIR_BUNDLEID = "Avoir";
	    public static final String TYPE_PROFORMA_BUNDLEID = "Proforma";
	    public static final String TYPE_ACOMPTE_BUNDLEID = "Acompte";
	    public static final String TYPE_PRELEVEMENT_BUNDLEID = "Prelevement";
	    public static final String TYPE_REMISE_BUNDLEID = "Remise";
	    public static final String TYPE_REGLEMENT_BUNDLEID = "fr.legrain.reglement";
	    public static final String TYPE_AVIS_ECHEANCE_BUNDLEID = "avisEcheance";
	    public static final String TYPE_TICKET_DE_CAISSE_BUNDLEID = "TicketDeCaisse";
	    
	    public static final String TYPE_PREPARATION_BUNDLEID = "Preparation";
	    public static final String TYPE_PANIER_BUNDLEID = "Panier";
	    public static final String TYPE_FLASH_BUNDLEID = "Flash";
	    
	    


//	    public static final String TYPE_COMPTE_POINT_BONUS_BUNDLEID = TaComptePoint.TYPE_DOC;
//	    public static final String TYPE_SUPPORT_ABONNEMENT_BUNDLEID = TaSupportAbon.TYPE_DOC;

//	    //liste de tous les plugins contenant un document réellement présent
//	    private Map<String,String> typeDocCompletPresent = new LinkedHashMap<String, String>(); //<bundle ID><libelle>


	    //liste de tous les plugins contenant un document
	    private Map<String,String> typeDocComplet = new LinkedHashMap<String, String>(); //<bundle ID><libelle>



	    //liste de tous les bundle contenant un document
	    private Map<String,String> typeBundleComplet = new LinkedHashMap<String, String>(); //<bundle ID><libelle>


//	    //liste de tous les plugins contenant un document réellement present pour l'impression
//	    private Map<String,String> typeDocImpressionPresent = new LinkedHashMap<String, String>(); //<bundle ID><libelle>


	    //liste de tous les plugins contenant un document réellement present pour l'impression
	    private Map<String,String> typeDocImpression = new LinkedHashMap<String, String>(); //<bundle ID><libelle>


//	    //liste des plugin reelement present dans l'application
//	    private Map<String,String> typeDocPresentSelectionAcompte = new LinkedHashMap<String, String>(); //<bundle ID><libelle>

	    //liste des editeurs pour chaque document
	    private Map<String,String> editorDoc = new HashMap<String, String>(); //<libelle><id editor>


	    private List<String[]> editorDocPossibleCreationDocument = new LinkedList<String[]>(); //<libelle><id editor>

	    //liste de tous les bundle contenant un document
	    private Map<String,ImageLgr> pathImageCouleurDoc = new LinkedHashMap<String, ImageLgr>();

	    //liste de tous les bundle contenant un document
	    private Map<String,ImageLgr> pathImageBlancDoc = new LinkedHashMap<String, ImageLgr>();
	    
	    //liste de tous les bundle contenant un document
	    private Map<String,ImageLgr> pathImageGrisDoc = new LinkedHashMap<String, ImageLgr>();

	    private TypeDoc() {}

	    private static TypeDoc instance = null;

	    public static TypeDoc getInstance() {
	    	if(instance==null) {
	    		instance = new TypeDoc();
	    		
	    		instance.typeBundleComplet.put(TYPE_FACTURE,TYPE_FACTURE_BUNDLEID);
	    		instance.typeBundleComplet.put(TYPE_DEVIS,TYPE_DEVIS_BUNDLEID);
	    		instance.typeBundleComplet.put(TYPE_BON_RECEPTION,TYPE_BON_RECEPTION_BUNDLEID);
	    		instance.typeBundleComplet.put(TYPE_BON_LIVRAISON,TYPE_BON_LIVRAISON_BUNDLEID);
	    		instance.typeBundleComplet.put(TYPE_APPORTEUR,TYPE_APPORTEUR_BUNDLEID);
	    		instance.typeBundleComplet.put(TYPE_BON_COMMANDE,TYPE_BON_COMMANDE_BUNDLEID);
	    		instance.typeBundleComplet.put(TYPE_BON_COMMANDE_ACHAT,TYPE_BON_COMMANDE_ACHAT_BUNDLEID);
	    		instance.typeBundleComplet.put(TYPE_AVOIR,TYPE_AVOIR_BUNDLEID);
	    		instance.typeBundleComplet.put(TYPE_PROFORMA,TYPE_PROFORMA_BUNDLEID);
	    		instance.typeBundleComplet.put(TYPE_ACOMPTE,TYPE_ACOMPTE_BUNDLEID);
	    		instance.typeBundleComplet.put(TYPE_PRELEVEMENT,TYPE_PRELEVEMENT_BUNDLEID);
	    		instance.typeBundleComplet.put(TYPE_REMISE,TYPE_REMISE_BUNDLEID);
	    		instance.typeBundleComplet.put(TYPE_REGLEMENT,TYPE_REGLEMENT_BUNDLEID);
	    		instance.typeBundleComplet.put(TYPE_TICKET_DE_CAISSE,TYPE_TICKET_DE_CAISSE_BUNDLEID);
	    		instance.typeBundleComplet.put(TYPE_AVIS_ECHEANCE,TYPE_AVIS_ECHEANCE_BUNDLEID);
	    		instance.typeBundleComplet.put(TYPE_PREPARATION,TYPE_PREPARATION_BUNDLEID);
	    		instance.typeBundleComplet.put(TYPE_PANIER,TYPE_PANIER_BUNDLEID);
	    		instance.typeBundleComplet.put(TYPE_FLASH,TYPE_FLASH_BUNDLEID);
	    		

	    		instance.typeDocComplet.put(TYPE_FACTURE_BUNDLEID, TYPE_FACTURE);
	    		instance.typeDocComplet.put(TYPE_DEVIS_BUNDLEID, TYPE_DEVIS);
	    		instance.typeDocComplet.put(TYPE_BON_RECEPTION_BUNDLEID, TYPE_BON_RECEPTION);
	    		instance.typeDocComplet.put(TYPE_BON_LIVRAISON_BUNDLEID, TYPE_BON_LIVRAISON);
	    		instance.typeDocComplet.put(TYPE_APPORTEUR_BUNDLEID,TYPE_APPORTEUR);
	    		instance.typeDocComplet.put(TYPE_BON_COMMANDE_BUNDLEID,TYPE_BON_COMMANDE);
	    		instance.typeDocComplet.put(TYPE_BON_COMMANDE_ACHAT_BUNDLEID,TYPE_BON_COMMANDE_ACHAT);
	    		instance.typeDocComplet.put(TYPE_AVOIR_BUNDLEID,TYPE_AVOIR);
	    		instance.typeDocComplet.put(TYPE_PROFORMA_BUNDLEID,TYPE_PROFORMA);
	    		instance.typeDocComplet.put(TYPE_ACOMPTE_BUNDLEID,TYPE_ACOMPTE);
	    		instance.typeDocComplet.put(TYPE_PRELEVEMENT_BUNDLEID,TYPE_PRELEVEMENT);
	    		instance.typeDocComplet.put(TYPE_REMISE_BUNDLEID,TYPE_REMISE);
	    		instance.typeDocComplet.put(TYPE_REGLEMENT_BUNDLEID,TYPE_REGLEMENT);
	    		instance.typeDocComplet.put(TYPE_TICKET_DE_CAISSE_BUNDLEID,TYPE_TICKET_DE_CAISSE);
	    		instance.typeDocComplet.put(TYPE_AVIS_ECHEANCE_BUNDLEID,TYPE_AVIS_ECHEANCE);
	    		instance.typeDocComplet.put(TYPE_PREPARATION_BUNDLEID,TYPE_PREPARATION);
	    		instance.typeDocComplet.put(TYPE_PANIER_BUNDLEID,TYPE_PANIER);
	    		instance.typeDocComplet.put(TYPE_FLASH_BUNDLEID,TYPE_FLASH);


	    		instance.getTypeDocImpression().put(TYPE_FACTURE_BUNDLEID, TYPE_FACTURE);
	    		instance.getTypeDocImpression().put(TYPE_DEVIS_BUNDLEID, TYPE_DEVIS);
	    		instance.getTypeDocImpression().put(TYPE_BON_RECEPTION_BUNDLEID, TYPE_BON_RECEPTION);
	    		instance.getTypeDocImpression().put(TYPE_BON_LIVRAISON_BUNDLEID, TYPE_BON_LIVRAISON);
	    		instance.getTypeDocImpression().put(TYPE_APPORTEUR_BUNDLEID,TYPE_APPORTEUR);
	    		instance.getTypeDocImpression().put(TYPE_BON_COMMANDE_BUNDLEID,TYPE_BON_COMMANDE);
	    		instance.getTypeDocImpression().put(TYPE_BON_COMMANDE_ACHAT_BUNDLEID,TYPE_BON_COMMANDE_ACHAT);
	    		instance.getTypeDocImpression().put(TYPE_AVOIR_BUNDLEID,TYPE_AVOIR);
	    		instance.getTypeDocImpression().put(TYPE_PROFORMA_BUNDLEID,TYPE_PROFORMA);
	    		instance.getTypeDocImpression().put(TYPE_ACOMPTE_BUNDLEID,TYPE_ACOMPTE);
	    		instance.getTypeDocImpression().put(TYPE_PRELEVEMENT_BUNDLEID,TYPE_PRELEVEMENT);
	    		instance.getTypeDocImpression().put(TYPE_AVIS_ECHEANCE_BUNDLEID,TYPE_AVIS_ECHEANCE);
	    		instance.getTypeDocImpression().put(TYPE_TICKET_DE_CAISSE_BUNDLEID,TYPE_TICKET_DE_CAISSE);
	    		instance.getTypeDocImpression().put(TYPE_PREPARATION_BUNDLEID,TYPE_PREPARATION);
	    		instance.getTypeDocImpression().put(TYPE_PANIER_BUNDLEID,TYPE_PANIER);
	    		instance.getTypeDocImpression().put(TYPE_FLASH_BUNDLEID,TYPE_FLASH);

	    		instance.editorDoc.put(TYPE_FACTURE, "fr.legrain.editor.facture.swt.multi");
	    		instance.editorDoc.put(TYPE_DEVIS, "fr.legrain.editor.devis.swt.multi");
	    		instance.editorDoc.put(TYPE_BON_LIVRAISON, "fr.legrain.editor.Bl.swt.multi");
	    		instance.editorDoc.put(TYPE_APPORTEUR, "fr.legrain.editor.apporteur.swt.multi");
	    		instance.editorDoc.put(TYPE_BON_COMMANDE, "fr.legrain.editor.boncde.swt.multi");
	    		instance.editorDoc.put(TYPE_BON_COMMANDE_ACHAT, "fr.legrain.editor.boncdeAchat.swt.multi");
	    		instance.editorDoc.put(TYPE_AVOIR, "fr.legrain.editor.avoir.swt.multi");
	    		instance.editorDoc.put(TYPE_PROFORMA, "fr.legrain.editor.proforma.swt.multi");
	    		instance.editorDoc.put(TYPE_ACOMPTE, "fr.legrain.editor.acompte.swt.multi");
	    		instance.editorDoc.put(TYPE_PRELEVEMENT, "fr.legrain.editor.prelevement.swt.multi");
	    		instance.editorDoc.put(TYPE_REGLEMENT, "fr.legrain.reglement.editor.EditorGestionReglement");
	    		instance.editorDoc.put(TYPE_REMISE, "fr.legrain.remise.multi");
	    		instance.editorDoc.put(TYPE_AVIS_ECHEANCE, "fr.legrain.editor.avisEcheance.swt.multi");
	    		instance.editorDoc.put(TYPE_TICKET_DE_CAISSE, "fr.legrain.editor.ticketcaisse.swt.multi");

	    		//gestion de la création des documents
	    		//c'est ici qu'il faut en enlever ou en rajouter
	    		instance.editorDocPossibleCreationDocument.add(new String[] {TYPE_DEVIS, TYPE_FACTURE});
	    		instance.editorDocPossibleCreationDocument.add(new String[] {TYPE_DEVIS, TYPE_BON_LIVRAISON});
	    		instance.editorDocPossibleCreationDocument.add(new String[] {TYPE_DEVIS, TYPE_BON_COMMANDE});
	    		instance.editorDocPossibleCreationDocument.add(new String[] {TYPE_DEVIS, TYPE_PROFORMA});

//	    		instance.editorDocPossibleCreationDocument.add(new String[] {TYPE_FACTURE, TYPE_BON_LIVRAISON});
	    		instance.editorDocPossibleCreationDocument.add(new String[] {TYPE_FACTURE, TYPE_AVOIR});
//	    		instance.editorDocPossibleCreationDocument.add(new String[] {TYPE_FACTURE, TYPE_AVIS_ECHEANCE});

	    		instance.editorDocPossibleCreationDocument.add(new String[] {TYPE_PROFORMA, TYPE_FACTURE});
	    		instance.editorDocPossibleCreationDocument.add(new String[] {TYPE_PROFORMA, TYPE_BON_LIVRAISON});
	    		instance.editorDocPossibleCreationDocument.add(new String[] {TYPE_PROFORMA, TYPE_BON_COMMANDE});
	    		
	    		instance.editorDocPossibleCreationDocument.add(new String[] {TYPE_BON_COMMANDE_ACHAT, TYPE_BON_RECEPTION});
//	    		instance.editorDocPossibleCreationDocument.add(new String[] {TYPE_BON_COMMANDE_ACHAT, TYPE_PREPARATION});

	    		instance.editorDocPossibleCreationDocument.add(new String[] {TYPE_BON_COMMANDE, TYPE_FACTURE});
	    		instance.editorDocPossibleCreationDocument.add(new String[] {TYPE_BON_COMMANDE, TYPE_PREPARATION});
	    		instance.editorDocPossibleCreationDocument.add(new String[] {TYPE_BON_COMMANDE, TYPE_BON_LIVRAISON});
	    		
	    		instance.editorDocPossibleCreationDocument.add(new String[] {TYPE_PREPARATION, TYPE_BON_LIVRAISON});
	    		

	    		instance.editorDocPossibleCreationDocument.add(new String[] {TYPE_BON_LIVRAISON, TYPE_FACTURE});

	    		instance.editorDocPossibleCreationDocument.add(new String[] {TYPE_PRELEVEMENT, TYPE_FACTURE});
	    		instance.editorDocPossibleCreationDocument.add(new String[] {TYPE_PRELEVEMENT, TYPE_BON_LIVRAISON});
	    		instance.editorDocPossibleCreationDocument.add(new String[] {TYPE_PRELEVEMENT, TYPE_BON_COMMANDE});

	    		instance.editorDocPossibleCreationDocument.add(new String[] {TYPE_AVIS_ECHEANCE, TYPE_FACTURE});
	    		instance.editorDocPossibleCreationDocument.add(new String[] {TYPE_AVIS_ECHEANCE, TYPE_PRELEVEMENT});
	    		
	    		instance.editorDocPossibleCreationDocument.add(new String[] {TYPE_TICKET_DE_CAISSE, TYPE_FACTURE});
	    		
	    		instance.editorDocPossibleCreationDocument.add(new String[] {TYPE_PANIER, TYPE_FACTURE});
	    		instance.editorDocPossibleCreationDocument.add(new String[] {TYPE_PANIER, TYPE_BON_COMMANDE});
	    		

	    		instance.pathImageCouleurDoc.put(TYPE_FACTURE,new ImageLgr(TYPE_FACTURE,TaFacture.PATH_ICONE_COULEUR));
	    		instance.pathImageCouleurDoc.put(TYPE_DEVIS,new ImageLgr(TYPE_DEVIS,TaDevis.PATH_ICONE_COULEUR));
	    		instance.pathImageCouleurDoc.put(TYPE_BON_LIVRAISON,new ImageLgr(TYPE_BON_LIVRAISON,TaBonliv.PATH_ICONE_COULEUR));
	    		instance.pathImageCouleurDoc.put(TYPE_BON_RECEPTION,new ImageLgr(TYPE_BON_RECEPTION,TaBonReception.PATH_ICONE_COULEUR));
	    		instance.pathImageCouleurDoc.put(TYPE_APPORTEUR,new ImageLgr(TYPE_APPORTEUR,TaApporteur.PATH_ICONE_COULEUR));
	    		instance.pathImageCouleurDoc.put(TYPE_BON_COMMANDE,new ImageLgr(TYPE_BON_COMMANDE,TaBoncde.PATH_ICONE_COULEUR));
	    		instance.pathImageCouleurDoc.put(TYPE_BON_COMMANDE_ACHAT,new ImageLgr(TYPE_BON_COMMANDE_ACHAT,TaBoncde.PATH_ICONE_COULEUR));
	    		instance.pathImageCouleurDoc.put(TYPE_AVOIR,new ImageLgr(TYPE_AVOIR,TaAvoir.PATH_ICONE_COULEUR));
	    		instance.pathImageCouleurDoc.put(TYPE_PROFORMA,new ImageLgr(TYPE_PROFORMA,TaProforma.PATH_ICONE_COULEUR));
	    		instance.pathImageCouleurDoc.put(TYPE_ACOMPTE,new ImageLgr(TYPE_ACOMPTE,TaAcompte.PATH_ICONE_COULEUR));
	    		instance.pathImageCouleurDoc.put(TYPE_TICKET_DE_CAISSE,new ImageLgr(TYPE_TICKET_DE_CAISSE,TaTicketDeCaisse.PATH_ICONE_COULEUR));
	    		instance.pathImageCouleurDoc.put(TYPE_PRELEVEMENT,new ImageLgr(TYPE_PRELEVEMENT,TaPrelevement.PATH_ICONE_COULEUR));
	    		instance.pathImageCouleurDoc.put(TYPE_REMISE,new ImageLgr(TYPE_REMISE,TaRemise.PATH_ICONE_COULEUR));
	    		instance.pathImageCouleurDoc.put(TYPE_REGLEMENT,new ImageLgr(TYPE_REGLEMENT,TaReglement.PATH_ICONE_COULEUR));
	    		instance.pathImageCouleurDoc.put(TYPE_AVIS_ECHEANCE,new ImageLgr(TYPE_AVIS_ECHEANCE,TaAvisEcheance.PATH_ICONE_COULEUR));
	    		instance.pathImageCouleurDoc.put(TYPE_TIERS,new ImageLgr(TYPE_TIERS,TaTiers.PATH_ICONE_COULEUR));
	    		instance.pathImageCouleurDoc.put(TYPE_ARTICLE,new ImageLgr(TYPE_ARTICLE,TaArticle.PATH_ICONE_COULEUR));
	    		instance.pathImageCouleurDoc.put(TYPE_PREPARATION,new ImageLgr(TYPE_PREPARATION,TaPreparation.PATH_ICONE_COULEUR));
	    		instance.pathImageCouleurDoc.put(TYPE_PANIER,new ImageLgr(TYPE_PANIER,TaPanier.PATH_ICONE_COULEUR));
	    		instance.pathImageCouleurDoc.put(TYPE_FLASH,new ImageLgr(TYPE_FLASH,TaFlash.PATH_ICONE_COULEUR));

	    		instance.pathImageBlancDoc.put(TYPE_FACTURE,new ImageLgr(TYPE_FACTURE,TaFacture.PATH_ICONE_BLANC));
	    		instance.pathImageBlancDoc.put(TYPE_DEVIS,new ImageLgr(TYPE_DEVIS,TaDevis.PATH_ICONE_BLANC));
	    		instance.pathImageBlancDoc.put(TYPE_BON_RECEPTION,new ImageLgr(TYPE_BON_RECEPTION,TaBonReception.PATH_ICONE_BLANC));
	    		instance.pathImageBlancDoc.put(TYPE_BON_LIVRAISON,new ImageLgr(TYPE_BON_LIVRAISON,TaBonliv.PATH_ICONE_BLANC));
	    		instance.pathImageBlancDoc.put(TYPE_APPORTEUR,new ImageLgr(TYPE_APPORTEUR,TaApporteur.PATH_ICONE_BLANC));
	    		instance.pathImageBlancDoc.put(TYPE_BON_COMMANDE_ACHAT,new ImageLgr(TYPE_BON_COMMANDE_ACHAT,TaBoncde.PATH_ICONE_BLANC));
	    		instance.pathImageBlancDoc.put(TYPE_BON_COMMANDE,new ImageLgr(TYPE_BON_COMMANDE,TaBoncde.PATH_ICONE_BLANC));
	    		instance.pathImageBlancDoc.put(TYPE_AVOIR,new ImageLgr(TYPE_AVOIR,TaAvoir.PATH_ICONE_BLANC));
	    		instance.pathImageBlancDoc.put(TYPE_PROFORMA,new ImageLgr(TYPE_PROFORMA,TaProforma.PATH_ICONE_BLANC));
	    		instance.pathImageBlancDoc.put(TYPE_ACOMPTE,new ImageLgr(TYPE_ACOMPTE,TaAcompte.PATH_ICONE_BLANC));
	    		instance.pathImageBlancDoc.put(TYPE_TICKET_DE_CAISSE,new ImageLgr(TYPE_TICKET_DE_CAISSE,TaTicketDeCaisse.PATH_ICONE_BLANC));
	    		instance.pathImageBlancDoc.put(TYPE_PRELEVEMENT,new ImageLgr(TYPE_PRELEVEMENT,TaPrelevement.PATH_ICONE_BLANC));
	    		instance.pathImageBlancDoc.put(TYPE_REMISE,new ImageLgr(TYPE_REMISE,TaRemise.PATH_ICONE_BLANC));
	    		instance.pathImageBlancDoc.put(TYPE_REGLEMENT,new ImageLgr(TYPE_REGLEMENT,TaReglement.PATH_ICONE_BLANC));
	    		instance.pathImageBlancDoc.put(TYPE_AVIS_ECHEANCE,new ImageLgr(TYPE_AVIS_ECHEANCE,TaAvisEcheance.PATH_ICONE_BLANC));
	    		instance.pathImageBlancDoc.put(TYPE_TIERS,new ImageLgr(TYPE_TIERS,TaTiers.PATH_ICONE_BLANC));
	    		instance.pathImageBlancDoc.put(TYPE_ARTICLE,new ImageLgr(TYPE_ARTICLE,TaArticle.PATH_ICONE_BLANC));
	    		instance.pathImageBlancDoc.put(TYPE_PREPARATION,new ImageLgr(TYPE_PREPARATION,TaPreparation.PATH_ICONE_BLANC));
	    		instance.pathImageBlancDoc.put(TYPE_PANIER,new ImageLgr(TYPE_PANIER,TaPanier.PATH_ICONE_BLANC));
	    		instance.pathImageBlancDoc.put(TYPE_FLASH,new ImageLgr(TYPE_FLASH,TaFlash.PATH_ICONE_BLANC));
	    		

	    		instance.pathImageGrisDoc.put(TYPE_FACTURE,new ImageLgr(TYPE_FACTURE,TaFacture.PATH_ICONE_GRIS));
	    		instance.pathImageGrisDoc.put(TYPE_DEVIS,new ImageLgr(TYPE_DEVIS,TaDevis.PATH_ICONE_GRIS));
	    		instance.pathImageGrisDoc.put(TYPE_BON_RECEPTION,new ImageLgr(TYPE_BON_RECEPTION,TaBonReception.PATH_ICONE_GRIS));
	    		instance.pathImageGrisDoc.put(TYPE_BON_LIVRAISON,new ImageLgr(TYPE_BON_LIVRAISON,TaBonliv.PATH_ICONE_GRIS));
	    		instance.pathImageGrisDoc.put(TYPE_APPORTEUR,new ImageLgr(TYPE_APPORTEUR,TaApporteur.PATH_ICONE_GRIS));
	    		instance.pathImageGrisDoc.put(TYPE_BON_COMMANDE_ACHAT,new ImageLgr(TYPE_BON_COMMANDE_ACHAT,TaBoncde.PATH_ICONE_GRIS));
	    		instance.pathImageGrisDoc.put(TYPE_BON_COMMANDE,new ImageLgr(TYPE_BON_COMMANDE,TaBoncde.PATH_ICONE_GRIS));
	    		instance.pathImageGrisDoc.put(TYPE_AVOIR,new ImageLgr(TYPE_AVOIR,TaAvoir.PATH_ICONE_GRIS));
	    		instance.pathImageGrisDoc.put(TYPE_PROFORMA,new ImageLgr(TYPE_PROFORMA,TaProforma.PATH_ICONE_GRIS));
	    		instance.pathImageGrisDoc.put(TYPE_ACOMPTE,new ImageLgr(TYPE_ACOMPTE,TaAcompte.PATH_ICONE_GRIS));
	    		instance.pathImageBlancDoc.put(TYPE_TICKET_DE_CAISSE,new ImageLgr(TYPE_TICKET_DE_CAISSE,TaTicketDeCaisse.PATH_ICONE_GRIS));
	    		instance.pathImageGrisDoc.put(TYPE_PRELEVEMENT,new ImageLgr(TYPE_PRELEVEMENT,TaPrelevement.PATH_ICONE_GRIS));
	    		instance.pathImageGrisDoc.put(TYPE_REMISE,new ImageLgr(TYPE_REMISE,TaRemise.PATH_ICONE_GRIS));
	    		instance.pathImageGrisDoc.put(TYPE_REGLEMENT,new ImageLgr(TYPE_REGLEMENT,TaReglement.PATH_ICONE_GRIS));
	    		instance.pathImageGrisDoc.put(TYPE_AVIS_ECHEANCE,new ImageLgr(TYPE_AVIS_ECHEANCE,TaAvisEcheance.PATH_ICONE_GRIS));
	    		instance.pathImageGrisDoc.put(TYPE_TIERS,new ImageLgr(TYPE_TIERS,TaTiers.PATH_ICONE_GRIS));
	    		instance.pathImageGrisDoc.put(TYPE_ARTICLE,new ImageLgr(TYPE_ARTICLE,TaArticle.PATH_ICONE_GRIS));
	    		instance.pathImageGrisDoc.put(TYPE_PREPARATION,new ImageLgr(TYPE_PREPARATION,TaPreparation.PATH_ICONE_GRIS));
	    		instance.pathImageGrisDoc.put(TYPE_PANIER,new ImageLgr(TYPE_PANIER,TaPanier.PATH_ICONE_GRIS));
	    		instance.pathImageGrisDoc.put(TYPE_FLASH,new ImageLgr(TYPE_FLASH,TaFlash.PATH_ICONE_GRIS));
	    		
	    		///!!!!!!!!!!!  code transféré dans AutorisationBean !!!!!!!!!!!!!!!///////////
	    		
	    		//	            for (String bundleId : instance.typeDocComplet.keySet()) {
	    			//	            	
	    		//	                if(Platform.getBundle(bundleId)!=null){
	    		//	                    if(bundleId.equals(TYPE_DEVIS_BUNDLEID)||
	    		//	bundleId.equals(TYPE_BON_COMMANDE_BUNDLEID)||
	    		//	bundleId.equals(TYPE_PROFORMA_BUNDLEID)){
	    		//	instance.typeDocPresentSelectionAcompte.put(bundleId, instance.typeDocComplet.get(bundleId));
	    		//	                    }
	    		//	                    instance.typeDocCompletPresent.put(bundleId, instance.typeDocComplet.get(bundleId));
	    		//	                }
	    		//	            }
	    		//	            for (String bundleId : instance.typeDocImpression.keySet()) {
	    		//	                if(Platform.getBundle(bundleId)!=null)
	    		//	instance.typeDocImpressionPresent.put(bundleId, instance.typeDocImpression.get(bundleId));
	    		//	            }

	    	}
	    	return instance;
	    }

//	    public Map<String, String> getTypeDocCompletPresent() {
//	        return typeDocCompletPresent;
//	    }
//
//	    public Map<String, String> getTypeDocPresentSelectionAcompte() {
//	        return typeDocPresentSelectionAcompte;
//	    }

	    public Map<String, String> getEditorDoc() {
	        return editorDoc;
	    }

	    public List<String[]> getEditorDocPossibleCreationDocument() {
	        return editorDocPossibleCreationDocument;
	    }

	    public Map<String, String> getTypeDocComplet() {
	        return typeDocComplet;
	    }

	    public void setTypeDocComplet(Map<String, String> typeDocComplet) {
	        this.typeDocComplet = typeDocComplet;
	    }

//	    public Map<String, String> getTypeDocImpressionPresent() {
//	        return typeDocImpressionPresent;
//	    }
//
//	    public void setTypeDocImpressionPresent(
//	            Map<String, String> typeDocImpressionPresent) {
//	        this.typeDocImpressionPresent = typeDocImpressionPresent;
//	    }

	    public Map<String, String> getTypeBundleComplet() {
	        return typeBundleComplet;
	    }

	    public void setTypeBundleComplet(Map<String, String> typeBundleComplet) {
	        this.typeBundleComplet = typeBundleComplet;
	    }

		public Map<String,String> getTypeDocImpression() {
			return typeDocImpression;
		}

		public void setTypeDocImpression(Map<String,String> typeDocImpression) {
			this.typeDocImpression = typeDocImpression;
		}

		public void setEditorDoc(Map<String, String> editorDoc) {
			this.editorDoc = editorDoc;
		}

		public void setEditorDocPossibleCreationDocument(List<String[]> editorDocPossibleCreationDocument) {
			this.editorDocPossibleCreationDocument = editorDocPossibleCreationDocument;
		}

		public Map<String, ImageLgr> getPathImageCouleurDoc() {
			return pathImageCouleurDoc;
		}

		public void setPathImageCouleurDoc(Map<String, ImageLgr> pathImageCouleurDoc) {
			this.pathImageCouleurDoc = pathImageCouleurDoc;
		}

		public Map<String, ImageLgr> getPathImageBlancDoc() {
			return pathImageBlancDoc;
		}

		public void setPathImageBlancDoc(Map<String, ImageLgr> pathImageBlancDoc) {
			this.pathImageBlancDoc = pathImageBlancDoc;
		}

		public Map<String, ImageLgr> getPathImageGrisDoc() {
			return pathImageGrisDoc;
		}

		public void setPathImageGrisDoc(Map<String, ImageLgr> pathImageGrisDoc) {
			this.pathImageGrisDoc = pathImageGrisDoc;
		}






	}
