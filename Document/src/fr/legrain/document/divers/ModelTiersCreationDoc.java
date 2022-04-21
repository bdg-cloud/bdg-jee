package fr.legrain.document.divers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;

import fr.legrain.bdg.documents.service.remote.ITaAcompteServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaApporteurServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAvoirServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBoncdeServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBonlivServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaDevisServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPrelevementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaProformaServiceRemote;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.model.TaRDocument;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.gestCom.Appli.ModelGeneralObjetEJB;
import fr.legrain.gestCom.Module_Tiers.SWTTiers;
import fr.legrain.lib.ejb.EJBLookup;
import fr.legrain.tiers.clientutility.JNDILookupClass;
import fr.legrain.tiers.model.TaTiers;

public class ModelTiersCreationDoc<DTO> extends ModelGeneralObjetEJB{
	
	IGenericCRUDServiceRemote dao=null;
	//private LinkedList<IHMEnteteDocument> listeObjet = new LinkedList<IHMEnteteDocument>();
	public ModelTiersCreationDoc(ITaTiersServiceRemote dao, Class<TaTiers> typeObjet ) {
		super(dao /*, typeObjet*/);
		this.dao=dao;
		setListeEntity(new LinkedList<TaTiers>());
	}

	TypeDoc typeDocPresent = TypeDoc.getInstance();
//	EntityManager em =new TaFactureDAO().getEntityManager();
//	private Collection <IDocumentTiers> listeEntity = new LinkedList<IDocumentTiers>();
	private ITaTiersServiceRemote daoTiers=null;
	private ITaFactureServiceRemote daoFacture = null;
	private ITaAvoirServiceRemote daoAvoir = null;
	private ITaBonlivServiceRemote daoBonliv = null;
	private ITaBoncdeServiceRemote daoBoncde = null;
	private ITaApporteurServiceRemote daoApporteur = null;
	private ITaProformaServiceRemote daoProforma = null;
	private ITaDevisServiceRemote daoDevis = null;
	private ITaPrelevementServiceRemote daoPrelevement = null;
	private ITaAcompteServiceRemote daoAcompte = null;
	
	private String selectedTypeSelection=null;
	
//	public LinkedList<TaTiers> remplirListe(Date dateDeb,Date dateFin,String tiers,String selectedTypeSelection,String selectedTypeCreation) {
//		this.selectedTypeSelection=selectedTypeSelection;
//		this.setListeEntity(new LinkedList<TaTiers>());
//		TaTiers document;
//		getListeEntity().clear();
//		getListeObjet().clear();
//		Boolean accepte=true;
//		List<TaTiers> listeDocumentsFinal =	new ArrayList<TaTiers>(0);
//		if(this.selectedTypeSelection.equalsIgnoreCase(TypeDoc.TYPE_BON_LIVRAISON)){
//			daoBonliv=new TaBonlivDAO();
//			daoBonliv.setEm(daoBonliv.getEntityManager());
//			List<TaBonliv> listeDocuments =daoBonliv.findByCodeTiersAndDate(tiers,dateDeb,dateFin);
//			for (IDocumentTiers taDocument : listeDocuments) {
//				accepte=!rechercheRDocument(taDocument,selectedTypeCreation);
//				if(accepte && !listeDocumentsFinal.contains((TaTiers) taDocument.getTaTiers()))
//					listeDocumentsFinal.add((TaTiers) taDocument.getTaTiers());
//				accepte=true;
//			}
//			setListeEntity(listeDocumentsFinal);
//		}
//		if(this.selectedTypeSelection.equalsIgnoreCase(TypeDoc.TYPE_FACTURE)){
//			daoFacture=new TaFactureDAO();
//			daoFacture.setEm(daoFacture.getEntityManager());
//			List<TaFacture> listeDocuments =daoFacture.findByCodeTiersAndDate(tiers,dateDeb,dateFin);
//			for (IDocumentTiers taDocument : listeDocuments) {
//				accepte=!rechercheRDocument(taDocument,selectedTypeCreation);
//				if(accepte && !listeDocumentsFinal.contains((TaTiers) taDocument.getTaTiers()))
//					listeDocumentsFinal.add((TaTiers) taDocument.getTaTiers());
//				accepte=true;
//			}
//			setListeEntity(listeDocumentsFinal);
//		}	
//		if(this.selectedTypeSelection.equalsIgnoreCase(TypeDoc.TYPE_ACOMPTE)){
//			daoAcompte=new TaAcompteDAO();
//			daoAcompte.setEm(daoAcompte.getEntityManager());
//			List<TaAcompte> listeDocuments =daoAcompte.findByCodeTiersAndDate(tiers,dateDeb,dateFin);
//			for (IDocumentTiers taDocument : listeDocuments) {
//				accepte=!rechercheRDocument(taDocument,selectedTypeCreation);
//				if(accepte && !listeDocumentsFinal.contains((TaTiers) taDocument.getTaTiers()))
//					listeDocumentsFinal.add((TaTiers) taDocument.getTaTiers());
//				accepte=true;
//			}
//			setListeEntity(listeDocumentsFinal);
//		}
//		if(this.selectedTypeSelection.equalsIgnoreCase(TypeDoc.TYPE_DEVIS)){
//			daoDevis=new TaDevisDAO();
//			daoDevis.setEm(daoDevis.getEntityManager());
//			List<TaDevis> listeDocuments =daoDevis.findByCodeTiersAndDate(tiers,dateDeb,dateFin);
//			for (IDocumentTiers taDocument : listeDocuments) {
//				accepte=!rechercheRDocument(taDocument,selectedTypeCreation);
//				if(accepte && !listeDocumentsFinal.contains((TaTiers) taDocument.getTaTiers()))
//					listeDocumentsFinal.add((TaTiers) taDocument.getTaTiers());
//				accepte=true;
//			}
//			setListeEntity(listeDocumentsFinal);
//		}
//		if(this.selectedTypeSelection.equalsIgnoreCase(TypeDoc.TYPE_AVOIR)){
//			daoAvoir=new TaAvoirDAO();
//			daoAvoir.setEm(daoAvoir.getEntityManager());
//			List<TaAvoir> listeDocuments =daoAvoir.findByCodeTiersAndDate(tiers,dateDeb,dateFin);
//			for (IDocumentTiers taDocument : listeDocuments) {
//				accepte=!rechercheRDocument(taDocument,selectedTypeCreation);
//				if(accepte && !listeDocumentsFinal.contains((TaTiers) taDocument.getTaTiers()))
//					listeDocumentsFinal.add((TaTiers) taDocument.getTaTiers());
//				accepte=true;
//			}
//			setListeEntity(listeDocumentsFinal);
//		}
//		if(this.selectedTypeSelection.equalsIgnoreCase(TypeDoc.TYPE_PROFORMA)){
//			daoProforma=new TaProformaDAO();
//			daoProforma.setEm(daoProforma.getEntityManager());
//			List<TaProforma> listeDocuments =daoProforma.findByCodeTiersAndDate(tiers,dateDeb,dateFin);
//			for (IDocumentTiers taDocument : listeDocuments) {
//				accepte=!rechercheRDocument(taDocument,selectedTypeCreation);
//				if(accepte && !listeDocumentsFinal.contains((TaTiers) taDocument.getTaTiers()))
//					listeDocumentsFinal.add((TaTiers) taDocument.getTaTiers());
//				accepte=true;
//			}
//			setListeEntity(listeDocumentsFinal);
//		}
//		if(this.selectedTypeSelection.equalsIgnoreCase(TypeDoc.TYPE_PRELEVEMENT)){
//			daoPrelevement=new TaPrelevementDAO();
//			daoPrelevement.setEm(daoPrelevement.getEntityManager());
//			List<TaPrelevement> listeDocuments =daoPrelevement.findByCodeTiersAndDate(tiers,dateDeb,dateFin);
//			for (IDocumentTiers taDocument : listeDocuments) {
//				accepte=!rechercheRDocument(taDocument,selectedTypeCreation);
//				if(accepte && !listeDocumentsFinal.contains((TaTiers) taDocument.getTaTiers()))
//					listeDocumentsFinal.add((TaTiers) taDocument.getTaTiers());
//				accepte=true;
//			}
//			setListeEntity(listeDocumentsFinal);
//		}
//		if(this.selectedTypeSelection.equalsIgnoreCase(TypeDoc.TYPE_APPORTEUR)){
//			daoApporteur=new TaApporteurDAO();
//			daoApporteur.setEm(daoApporteur.getEntityManager());
//			List<TaApporteur> listeDocuments =daoApporteur.findByCodeTiersAndDate(tiers,dateDeb,dateFin);
//			for (IDocumentTiers taDocument : listeDocuments) {
//				accepte=!rechercheRDocument(taDocument,selectedTypeCreation);
//				if(accepte && !listeDocumentsFinal.contains((TaTiers) taDocument.getTaTiers()))
//					listeDocumentsFinal.add((TaTiers) taDocument.getTaTiers());
//				accepte=true;
//			}
//			setListeEntity(listeDocumentsFinal);
//		}		
//			if(getListeEntity()!=null && getListeEntity().size()>0)
//				getListeObjet().addAll(this.remplirListe());
//		
//		return getListeObjet();
//	}
//	
	
	public LinkedList<TaTiers> remplirListe(Date dateDeb,Date dateFin,String selectedTypeSelection,String selectedTypeCreation
			,String partieRequeteTiers,IDocumentTiers doc , TaTPaiement taTPaiement) {
		try {
			this.selectedTypeSelection=selectedTypeSelection;
			getListeEntity().clear();
			getListeObjet().clear();
			List<TaTiers> listeDocumentsFinal =	new ArrayList<TaTiers>(0);
			//daoTiers = new TaTiersDAO();
			daoTiers = new EJBLookup<ITaTiersServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_TIERS_SERVICE, ITaTiersServiceRemote.class.getName(),true);
	//		daoTiers.setEm(daoTiers.getEntityManager());
			listeDocumentsFinal = daoTiers.selectTiersTypeDoc(partieRequeteTiers,doc,selectedTypeSelection, selectedTypeCreation, dateDeb, dateFin);
			for (TaTiers taTiers : listeDocumentsFinal) {
				if(taTPaiement==null || (taTiers.getTaTPaiement()!=null && taTiers.getTaTPaiement().getCodeTPaiement().equals(taTPaiement.getCodeTPaiement())))
					getListeEntity().add(taTiers);
			}
			//setListeEntity(listeDocumentsFinal);
	
				if(getListeEntity()!=null && getListeEntity().size()>0)
					getListeObjet().addAll(this.remplirListe());
		} catch(Exception e) {
			e.printStackTrace();
		}
				
		return getListeObjet();
	}
	

	public LinkedList<SWTTiers> remplirListe() {
		SWTTiers ihm=null;
		LinkedList<SWTTiers> listeObjet=new LinkedList<SWTTiers>();
		
		listeObjet.clear();
		for (Object t : getListeEntity()) {
			ihm=new SWTTiers();
			ihm.setIdTiers(((TaTiers)t).getIdTiers());
			ihm.setCodeTiers(((TaTiers)t).getCodeTiers());
			ihm.setNomTiers(((TaTiers)t).getNomTiers());
			ihm.setPrenomTiers(((TaTiers)t).getPrenomTiers());
			if(((TaTiers)t).getTaEntreprise()!=null)
				ihm.setNomEntreprise(((TaTiers)t).getTaEntreprise().getNomEntreprise());
			if(((TaTiers)t).getTaAdresse()!=null){
			ihm.setCodepostalAdresse(((TaTiers)t).getTaAdresse().getCodepostalAdresse());
			ihm.setVilleAdresse(((TaTiers)t).getTaAdresse().getVilleAdresse());
			}
			ihm.setAccepte(true);
			listeObjet.add(ihm);
			ihm=null;
		}
		return listeObjet;
	}

	public Boolean rechercheRDocument(IDocumentTiers doc, String selectedTypeDoc){
		if(selectedTypeDoc==TypeDoc.TYPE_ACOMPTE)
			for (TaRDocument relation : doc.getTaRDocuments()) {
				if(relation.getTaAcompte()!=null){
					return true;
					}
			}
		if(selectedTypeDoc==TypeDoc.TYPE_APPORTEUR)
			for (TaRDocument relation : doc.getTaRDocuments()) {
				if(relation.getTaApporteur()!=null){
					return true;
					}
			}
		if(selectedTypeDoc==TypeDoc.TYPE_AVOIR)
			for (TaRDocument relation : doc.getTaRDocuments()) {
				if(relation.getTaAvoir()!=null){
					return true;
					}
			}
		if(selectedTypeDoc==TypeDoc.TYPE_BON_COMMANDE)
			for (TaRDocument relation : doc.getTaRDocuments()) {
				if(relation.getTaBoncde()!=null){
					return true;
					}
			}
		if(selectedTypeDoc==TypeDoc.TYPE_BON_LIVRAISON)
			for (TaRDocument relation : doc.getTaRDocuments()) {
				if(relation.getTaBonliv()!=null){
					return true;
					}
			}
		if(selectedTypeDoc==TypeDoc.TYPE_DEVIS)
			for (TaRDocument relation : doc.getTaRDocuments()) {
				if(relation.getTaDevis()!=null){
					return true;
					}
			}
		if(selectedTypeDoc==TypeDoc.TYPE_FACTURE)
			for (TaRDocument relation : doc.getTaRDocuments()) {
				if(relation.getTaFacture()!=null){
					return true;
					}
			}
		if(selectedTypeDoc==TypeDoc.TYPE_PRELEVEMENT)
			for (TaRDocument relation : doc.getTaRDocuments()) {
				if(relation.getTaPrelevement()!=null){
					return true;
					}
			}
		if(selectedTypeDoc==TypeDoc.TYPE_PROFORMA)
			for (TaRDocument relation : doc.getTaRDocuments()) {
				if(relation.getTaProforma()!=null){
					return true;
					}
			}
	  return false;	
	}
}
