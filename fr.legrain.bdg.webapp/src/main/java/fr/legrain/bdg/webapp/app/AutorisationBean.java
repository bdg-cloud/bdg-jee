package fr.legrain.bdg.webapp.app;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import fr.legrain.autorisation.xml.ListeModules;
import fr.legrain.autorisation.xml.Module;
import fr.legrain.bdg.dossier.service.remote.ITaAutorisationsDossierServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.bdg.webapp.Auth;
import fr.legrain.document.model.ImageLgr;
import fr.legrain.document.model.TypeDoc;
import fr.legrain.dossier.model.TaAutorisationsDossier;
import fr.legrain.droits.model.IModulesProgramme;
import fr.legrain.droits.model.TaUtilisateur;

@Named()
@ViewScoped 
public class AutorisationBean implements Serializable, IModulesProgramme {

	private static final long serialVersionUID = -7359665457471702834L;
	
	private TaUtilisateur taUtilisateur = null;
	private List<String> listeAutorisation;
	
	//global
	private boolean modeFacturation = true;
	private boolean modeTracabilite = true;
	private boolean modeGestionLot = true;
	private boolean modeGestionCommerciale = true;
	private boolean modeGestionCapsules = true;

	@EJB private ITaAutorisationsDossierServiceRemote taAutorisationDossierService;
	
	@EJB private ITaUtilisateurServiceRemote userService;
	
  // liste intermédiaire entre typeDoc et autorisation
    private Map<String,String> typeDocAutorisations = new LinkedHashMap<String, String>(); 
    
    //liste des plugin reelement present dans l'application
    private Map<String,String> typeDocPresentSelectionAcompte = new LinkedHashMap<String, String>(); 
    
    //liste de tous les plugins contenant un document réellement present pour l'impression
    private Map<String,String> typeDocImpressionPresent = new LinkedHashMap<String, String>(); 
    
    //liste de tous les plugins contenant un document réellement présent
    private Map<String,String> typeDocCompletPresent = new LinkedHashMap<String, String>(); 
    
    //liste de tous les plugins contenant un document réellement présent
    private Map<String,ImageLgr> typeDocCompletPresentAvecImage = new LinkedHashMap<String, ImageLgr>(); 
    
	@PostConstruct
	public void init() {
		taUtilisateur = Auth.findUserInSession();
		
		modeGestionLot = autoriseMenu(ID_MODULE_GESTION_LOTS);
		modeFacturation = autoriseMenu(ID_MODULE_FACTURATION);
		modeTracabilite = autoriseMenu(ID_MODULE_TRACABILITE);
		modeGestionCommerciale = autoriseMenu(ID_MODULE_GESTION_COMMERCIALE);
		modeGestionCapsules = autoriseMenu(ID_MODULE_GESTION_CAPSULES);
		
		
		typeDocAutorisations.put(TypeDoc.TYPE_FACTURE_BUNDLEID,ID_MODULE_FACTURE);
		typeDocAutorisations.put(TypeDoc.TYPE_DEVIS_BUNDLEID,ID_MODULE_DEVIS);
		typeDocAutorisations.put(TypeDoc.TYPE_BON_LIVRAISON_BUNDLEID,ID_MODULE_BON_LIVRAISON);
		typeDocAutorisations.put(TypeDoc.TYPE_APPORTEUR_BUNDLEID,ID_MODULE_APPORTEUR);
		typeDocAutorisations.put(TypeDoc.TYPE_BON_COMMANDE_BUNDLEID,ID_MODULE_BON_COMMANDE);
		typeDocAutorisations.put(TypeDoc.TYPE_BON_COMMANDE_ACHAT_BUNDLEID,ID_MODULE_BON_COMMANDE_ACHAT);
		typeDocAutorisations.put(TypeDoc.TYPE_AVOIR_BUNDLEID,ID_MODULE_AVOIR);
		typeDocAutorisations.put(TypeDoc.TYPE_PROFORMA_BUNDLEID,ID_MODULE_PROFORMA);
		typeDocAutorisations.put(TypeDoc.TYPE_ACOMPTE_BUNDLEID,ID_MODULE_ACOMPTE);
		typeDocAutorisations.put(TypeDoc.TYPE_PRELEVEMENT_BUNDLEID,ID_MODULE_PRELEVEMENT);
		typeDocAutorisations.put(TypeDoc.TYPE_REMISE_BUNDLEID,ID_MODULE_REMISE);
		typeDocAutorisations.put(TypeDoc.TYPE_REGLEMENT_BUNDLEID,ID_MODULE_REGLEMENTS);
		typeDocAutorisations.put(TypeDoc.TYPE_AVIS_ECHEANCE_BUNDLEID,ID_MODULE_AVIS_ECHEANCE);
		typeDocAutorisations.put(TypeDoc.TYPE_BON_RECEPTION_BUNDLEID,ID_MODULE_BON_RECEPTION);
		typeDocAutorisations.put(TypeDoc.TYPE_PREPARATION_BUNDLEID,ID_MODULE_PREPARATION);
		typeDocAutorisations.put(TypeDoc.TYPE_PANIER_BUNDLEID,ID_MODULE_PANIER);
		
		TypeDoc.getInstance();
		
        for (String bundleId : TypeDoc.getInstance().getTypeDocImpression().keySet()) {
        	String autorisation = typeDocAutorisations.get(bundleId);
            if(autoriseMenu(autorisation)!=null)
            	typeDocImpressionPresent.put(bundleId,  TypeDoc.getInstance().getTypeDocImpression().get(bundleId));
        }
        
        for (String bundleId : TypeDoc.getInstance().getTypeDocComplet().keySet()) {
        	String autorisation = typeDocAutorisations.get(bundleId);
        	 if(autoriseMenu(autorisation)!=null){
        		if(bundleId.equals(TypeDoc.TYPE_DEVIS_BUNDLEID)||
        				bundleId.equals(TypeDoc.TYPE_BON_COMMANDE_BUNDLEID)||
        				bundleId.equals(TypeDoc.TYPE_PROFORMA_BUNDLEID)){
        			typeDocPresentSelectionAcompte.put(bundleId, TypeDoc.getInstance().getTypeDocComplet().get(bundleId));
        		}
        		typeDocCompletPresent.put(bundleId, TypeDoc.getInstance().getTypeDocComplet().get(bundleId));
        		String cle=TypeDoc.getInstance().getTypeDocComplet().get(bundleId);
        		typeDocCompletPresentAvecImage.put(bundleId, TypeDoc.getInstance().getPathImageCouleurDoc().get(cle));
        	}
        }

        
	}

	public Boolean autoriseMenu(String idMenu){
		return taAutorisationDossierService.autoriseMenu(idMenu,taUtilisateur);
	}
	
	public String classeCssMenu(String idMenu){
		String retour = "";
		if(autoriseMenu(idMenu)) {
			
		} else {
			retour = "disabled";
		}
		return retour;
	}

	public void listeAutorisationTiers(){
		listeAutorisation = taAutorisationDossierService.listeAutorisationUtilisateur(taUtilisateur);
//		if(listeAutorisation==null) {
//			listeAutorisation=new LinkedList<String>();
//			List<String> listeAutorisationDossier = new LinkedList<String>();
//
//			//Autorisations dossier
//			TaAutorisationsDossier autorisationsDossier = taAutorisationDossierService.findInstance();
//			ListeModules listeModuleDossier = new ListeModules();
//			listeModuleDossier = listeModuleDossier.recupModulesXml(autorisationsDossier.getFichier());
//			for(Module m : listeModuleDossier.module) {
//				//listeAutorisation.add(m.nomModule);
//				listeAutorisationDossier.add(m.id);
//			}
//			
//			//Autorisations roles
//			
//			//Autorisations utilisateur
//			if(taUtilisateur.getAdminDossier()!=null && taUtilisateur.getAdminDossier()) {
//				//L'utilisateur connecté est l'administrateur du dossier, il peu accéder à tous les modules du dossier
//				listeAutorisation.addAll(listeAutorisationDossier);
//			} else {
//				//L'utilisateur connecté n'est pas l'administrateur du dossier, il faut consulté ses droits spécifique
//				ListeModules listeModuleUtilisateur = new ListeModules();
//				if(taUtilisateur.getAutorisations()!=null) {
//					listeModuleUtilisateur = listeModuleUtilisateur.recupModulesXml(taUtilisateur.getAutorisations());
//					for(Module m : listeModuleUtilisateur.module) {
//						if(listeAutorisationDossier.contains(m.id)) {
//							//le module est accessible pour ce dossier et l'utilisateur est autorisé à y accéder
//							listeAutorisation.add(m.id);
//						} else {
//							//les droits de l'utilisateur sont supérieur à ceux du dossier ==> problème
//						}
//					}
//				} else {
//					//pas de droits spécifique pour cet utilisateur
//				}
//			}
//			
//		}
	}

	public boolean isModeFacturation() {
		return modeFacturation;
	}

	public void setModeFacturation(boolean modeFacturation) {
		this.modeFacturation = modeFacturation;
	}

	public boolean isModeTracabilite() {
		return modeTracabilite;
	}

	public void setModeTracabilite(boolean modeTracabilite) {
		this.modeTracabilite = modeTracabilite;
	}

	public boolean isModeGestionLot() {
		return modeGestionLot;
	}

	public void setModeGestionLot(boolean modeGestionLot) {
		this.modeGestionLot = modeGestionLot;
	}

	public TaUtilisateur getTaUtilisateur() {
		return taUtilisateur;
	}

	public boolean isModeGestionCommerciale() {
		return modeGestionCommerciale;
	}

	public void setModeGestionCommerciale(boolean modeGestionCommerciale) {
		this.modeGestionCommerciale = modeGestionCommerciale;
	}
	public String getIdModuleImportationShippingBo() {
		return ID_MODULE_IMPORTATION_SHIPPINGBO;
	}
	public String getIdModuleImportationWooCommerce() {
		return ID_MODULE_IMPORTATION_WOOCOMMERCE;
	}
	public String getIdModuleArticle() {
		return ID_MODULE_ARTICLE;
	}

	public String getIdModuleTiers() {
		return ID_MODULE_TIERS;
	}

	public String getIdModuleDocument() {
		return ID_MODULE_DOCUMENT;
	}

	public String getIdModuleInventaire() {
		return ID_MODULE_INVENTAIRE;
	}

	public String getIdModuleTracabilite() {
		return ID_MODULE_TRACABILITE;
	}

	public String getIdModuleCodeBarre() {
		return ID_MODULE_CODE_BARRE;
	}

	public String getIdModuleEntrepot() {
		return ID_MODULE_ENTREPOT;
	}

	public String getIdModuleFabrication() {
		return ID_MODULE_FABRICATION;
	}

	public String getIdModuleDevis() {
		return ID_MODULE_DEVIS;
	}

	public String getIdModuleFacture() {
		return ID_MODULE_FACTURE;
	}

	public String getIdModuleGestionLots() {
		return ID_MODULE_GESTION_LOTS;
	}

	public String getIdModuleBonReception() {
		return ID_MODULE_BON_RECEPTION;
	}

	public String getIdModuleMouvementStock() {
		return ID_MODULE_MOUVEMENT_STOCK;
	}

	public String getIdModuleAvoir() {
		return ID_MODULE_AVOIR;
	}

	public String getIdModuleAcompte() {
		return ID_MODULE_ACOMPTE;
	}

	public String getIdModuleBonCommande() {
		return ID_MODULE_BON_COMMANDE;
	}

	public String getIdModuleBonPreparation() {
		return ID_MODULE_PREPARATION;
	}
	
	public String getIdModulePanier() {
		return ID_MODULE_PANIER;
	}
	
	
	public String getIdModuleBonCommandeAchat() {
		return ID_MODULE_BON_COMMANDE_ACHAT;
	}
	
	public String getIdModuleBonLivraison() {
		return ID_MODULE_BON_LIVRAISON;
	}

	public String getIdModuleProforma() {
		return ID_MODULE_PROFORMA;
	}

	public String getIdModuleApporteur() {
		return ID_MODULE_APPORTEUR;
	}

	public String getIdModulePrelevement() {
		return ID_MODULE_PRELEVEMENT;
	}

	public String getIdModuleExportation() {
		return ID_MODULE_EXPORTATION;
	}

	public String getIdModuleExportationEpicea() {
		return ID_MODULE_EXPORTATION_EPICEA;
	}

	public String getIdModulePartenariat() {
		return ID_MODULE_PARTENARIAT;
	}

	public String getIdModuleSolstyce() {
		return ID_MODULE_SOLSTYCE;
	}

	public String getIdModuleReglements() {
		return ID_MODULE_REGLEMENTS;
	}

	public String getIdModuleAchats() {
		return ID_MODULE_ACHATS;
	}

	public String getIdModuleStock() {
		return ID_MODULE_STOCK;
	}

	public String getIdModuleFacturation() {
		return ID_MODULE_FACTURATION;
	}

	public String getIdModuleFactureDevis() {
		return ID_MODULE_FACTURE_DEVIS;
	}

	public String getIdModuleGestionCommerciale() {
		return ID_MODULE_GESTION_COMMERCIALE;
	}

	public String getIdModuleCrm() {
		return ID_MODULE_CRM;
	}

	public String getIdModuleCompteClient() {
		return ID_MODULE_COMPTE_CLIENT;
	}

	public String getIdModuleRemise() {
		return ID_MODULE_REMISE;
	}

	public String getIdModuleAvisEcheance() {
		return ID_MODULE_AVIS_ECHEANCE;
	}

	public String getIdModuleGestionReglement() {
		return ID_MODULE_GESTION_REGLEMENT;
	}
	
	public String getIdModuleImportationMensura() {
		return ID_MODULE_IMPORTATION_MENSURA;
	}
	public String getIdModuleImportationPrestashop() {
		return ID_MODULE_IMPORTATION_PRESTASHOP;
	}

	public Map<String, String> getTypeDocAutorisations() {
		return typeDocAutorisations;
	}

	public void setTypeDocAutorisations(Map<String, String> typeDocAutorisations) {
		this.typeDocAutorisations = typeDocAutorisations;
	}

	public Map<String, String> getTypeDocPresentSelectionAcompte() {
		return typeDocPresentSelectionAcompte;
	}

	public void setTypeDocPresentSelectionAcompte(Map<String, String> typeDocPresentSelectionAcompte) {
		this.typeDocPresentSelectionAcompte = typeDocPresentSelectionAcompte;
	}

	public Map<String, String> getTypeDocImpressionPresent() {
		return typeDocImpressionPresent;
	}

	public void setTypeDocImpressionPresent(Map<String, String> typeDocImpressionPresent) {
		this.typeDocImpressionPresent = typeDocImpressionPresent;
	}

	public Map<String, String> getTypeDocCompletPresent() {
		return typeDocCompletPresent;
	}

	public void setTypeDocCompletPresent(Map<String, String> typeDocCompletPresent) {
		this.typeDocCompletPresent = typeDocCompletPresent;
	}

	public Map<String, ImageLgr> getTypeDocCompletPresentAvecImage() {
		return typeDocCompletPresentAvecImage;
	}

	public void setTypeDocCompletPresentAvecImage(Map<String, ImageLgr> typeDocCompletPresentAvecImage) {
		this.typeDocCompletPresentAvecImage = typeDocCompletPresentAvecImage;
	}

	
	
	public String getIdModuleCaisse() {
		return ID_MODULE_CAISSE;
	}

	public String getIdModuleGestionCapsules() {
		return ID_MODULE_GESTION_CAPSULES;
	}

	public  String getIdModuleGestionDms() {
		return ID_MODULE_GESTION_DMS;
	}

	public boolean isModeGestionCapsules() {
		return modeGestionCapsules;
	}

	public void setModeGestionCapsules(boolean modeGestionCapsules) {
		this.modeGestionCapsules = modeGestionCapsules;
	}

	public  String getIdModuleMultiTarifs() {
		return ID_MODULE_MULTI_TARIFS;
	}

	public  String getIdModuleExportationAgrigest() {
		return ID_MODULE_EXPORTATION_AGRIGEST;
	}

	public  String getIdModuleAchat() {
		return ID_MODULE_ACHAT;
	}

	public String getIdModuleCatalogue() {
		return ID_MODULE_CATALOGUE;
	}

	public String getIdModulePreparation() {
		return ID_MODULE_PREPARATION;
	}

	public String getIdModuleImportationShippingbo() {
		return ID_MODULE_IMPORTATION_SHIPPINGBO;
	}

	public String getIdModuleImportationWoocommerce() {
		return ID_MODULE_IMPORTATION_WOOCOMMERCE;
	}

	public String getIdModuleBoutique() {
		return ID_MODULE_BOUTIQUE;
	}
	
	public String getIdModuleAbonnement() {
		return ID_MODULE_ABONNEMENT;
	}


}
