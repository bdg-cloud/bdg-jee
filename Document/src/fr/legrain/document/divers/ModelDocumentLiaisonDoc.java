package fr.legrain.document.divers;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaPrelevement;
import fr.legrain.document.model.TaProforma;
import fr.legrain.document.model.TaRDocument;
import fr.legrain.gestCom.Appli.ModelGeneralObjetEJB;
import fr.legrain.gestCom.Module_Document.IHMEnteteDocument;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.ejb.EJBLookup;
import fr.legrain.tiers.clientutility.JNDILookupClass;

public class ModelDocumentLiaisonDoc<DTO> extends ModelGeneralObjetEJB{
	
	IGenericCRUDServiceRemote dao=null;
	//private LinkedList<IHMEnteteDocument> listeObjet = new LinkedList<IHMEnteteDocument>();
	public ModelDocumentLiaisonDoc(IGenericCRUDServiceRemote dao, Class<DTO> typeObjet ,String selectedType) {
		super(dao /*, typeObjet*/);
		this.selectedTypeSelection=selectedType;
		this.dao=dao;
	}

	TypeDoc typeDocPresent = TypeDoc.getInstance();
//	EntityManager em =new TaFactureDAO().getEntityManager();
//	private Collection <IDocumentTiers> listeEntity = new LinkedList<IDocumentTiers>();
	private ITaFactureServiceRemote daoFacture = null;
	private ITaAvoirServiceRemote daoAvoir = null;
	private ITaBonlivServiceRemote daoBonliv = null;
	private ITaBoncdeServiceRemote daoBoncde = null;
	private ITaApporteurServiceRemote daoApporteur = null;
	private ITaProformaServiceRemote daoProforma = null;
	private ITaDevisServiceRemote daoDevis = null;
	private ITaPrelevementServiceRemote daoPrelevement = null;
	private ITaAcompteServiceRemote daoAcompte = null;
	
	private String selectedTypeSelection = null;
	
	public LinkedList<IHMEnteteDocument> remplirListe(Date dateDeb,Date dateFin,String tiers,String selectedType1,String selectedType2,boolean dejaLie,EntityManager em) {
		//this.selectedTypeSelection=selectedTypeSelection;
		try {
			this.setListeEntity(new LinkedList<IDocumentTiers>());
			IDocumentTiers document;
			getListeEntity().clear();
			getListeObjet().clear();
			Boolean accepte=true;
			List<IDocumentTiers> listeDocumentsFinal =	new ArrayList<IDocumentTiers>(0);
			if(selectedType1.equalsIgnoreCase(TypeDoc.TYPE_BON_LIVRAISON)){
				daoBonliv = new EJBLookup<ITaBonlivServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_BONLIV_SERVICE, ITaBonlivServiceRemote.class.getName());
				List<TaBonliv> listeDocuments = daoBonliv.findByCodeTiersAndDate(tiers,dateDeb,dateFin);
				for (IDocumentTiers taDocument : listeDocuments) {
					//taDocument.setTypeDocument(TypeDoc.TYPE_BON_LIVRAISON);
					if(!dejaLie)
						accepte=!rechercheRDocument(taDocument,selectedType2);
	//				if(!accepte)break;
					if(accepte)listeDocumentsFinal.add((IDocumentTiers)taDocument);
					accepte=true;
				}
				setListeEntity(listeDocumentsFinal);
			}
			if(selectedType1.equalsIgnoreCase(TypeDoc.TYPE_BON_COMMANDE)){
				daoBoncde = new EJBLookup<ITaBoncdeServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_BONCDE_SERVICE, ITaBoncdeServiceRemote.class.getName());
				List<TaBoncde> listeDocuments = daoBoncde.findByCodeTiersAndDate(tiers,dateDeb,dateFin);
				for (IDocumentTiers taDocument : listeDocuments) {
					//taDocument.setTypeDocument(TypeDoc.TYPE_BON_COMMANDE);
					if(!dejaLie)accepte=!rechercheRDocument(taDocument,selectedType2);
	//				if(!accepte)break;
					if(accepte)listeDocumentsFinal.add((IDocumentTiers)taDocument);
					accepte=true;
				}
				setListeEntity(listeDocumentsFinal);
			}
			if(selectedType1.equalsIgnoreCase(TypeDoc.TYPE_FACTURE)){
				daoFacture = new EJBLookup<ITaFactureServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_FACTURE_SERVICE, ITaFactureServiceRemote.class.getName());
				List<TaFacture> listeDocuments = daoFacture.findByCodeTiersAndDate(tiers,dateDeb,dateFin);
				for (IDocumentTiers taDocument : listeDocuments) {
					//taDocument.setTypeDocument(TypeDoc.TYPE_FACTURE);
					if(!dejaLie)
						accepte=!rechercheRDocument(taDocument,selectedType2);
	//				if(!accepte)break;
					if(accepte)listeDocumentsFinal.add((IDocumentTiers)taDocument);
					accepte=true;
				}
				setListeEntity(listeDocumentsFinal);
			}	
			if(selectedType1.equalsIgnoreCase(TypeDoc.TYPE_ACOMPTE)){
				daoAcompte = new EJBLookup<ITaAcompteServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_ACOMPTE_SERVICE, ITaAcompteServiceRemote.class.getName());
				List<TaAcompte> listeDocuments = daoAcompte.findByCodeTiersAndDate(tiers,dateDeb,dateFin);
				for (IDocumentTiers taDocument : listeDocuments) {
					//taDocument.setTypeDocument(TypeDoc.TYPE_ACOMPTE);
					if(!dejaLie)accepte=!rechercheRDocument(taDocument,selectedType2);
	//				if(!accepte)break;
					if(accepte)listeDocumentsFinal.add((IDocumentTiers)taDocument);
					accepte=true;
				}
				setListeEntity(listeDocumentsFinal);
			}
			if(selectedType1.equalsIgnoreCase(TypeDoc.TYPE_DEVIS)){
				daoDevis = new EJBLookup<ITaDevisServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_DEVIS_SERVICE, ITaDevisServiceRemote.class.getName());
				List<TaDevis> listeDocuments = daoDevis.findByCodeTiersAndDate(tiers,dateDeb,dateFin);
				for (IDocumentTiers taDocument : listeDocuments) {
					//taDocument.setTypeDocument(TypeDoc.TYPE_DEVIS);
					if(!dejaLie)accepte=!rechercheRDocument(taDocument,selectedType2);
	//				if(!accepte)break;
					if(accepte)listeDocumentsFinal.add((IDocumentTiers)taDocument);
					accepte=true;
				}
				setListeEntity(listeDocumentsFinal);
			}
			if(selectedType1.equalsIgnoreCase(TypeDoc.TYPE_AVOIR)){
				daoAvoir = new EJBLookup<ITaAvoirServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_AVOIR_SERVICE, ITaAvoirServiceRemote.class.getName());
				List<TaAvoir> listeDocuments = daoAvoir.findByCodeTiersAndDate(tiers,dateDeb,dateFin);
				for (IDocumentTiers taDocument : listeDocuments) {
					//taDocument.setTypeDocument(TypeDoc.TYPE_AVOIR);
					if(!dejaLie)accepte=!rechercheRDocument(taDocument,selectedType2);
	//				if(!accepte)break;
					if(accepte)listeDocumentsFinal.add((IDocumentTiers)taDocument);
					accepte=true;
				}
				setListeEntity(listeDocumentsFinal);
			}
			if(selectedType1.equalsIgnoreCase(TypeDoc.TYPE_PROFORMA)){
				daoProforma = new EJBLookup<ITaProformaServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_PROFORMA_SERVICE, ITaProformaServiceRemote.class.getName());
				List<TaProforma> listeDocuments = daoProforma.findByCodeTiersAndDate(tiers,dateDeb,dateFin);
				for (IDocumentTiers taDocument : listeDocuments) {
					//taDocument.setTypeDocument(TypeDoc.TYPE_PROFORMA);
					if(!dejaLie)accepte=!rechercheRDocument(taDocument,selectedType2);
	//				if(!accepte)break;
					if(accepte)listeDocumentsFinal.add((IDocumentTiers)taDocument);
					accepte=true;
				}
				setListeEntity(listeDocumentsFinal);
			}
			if(selectedType1.equalsIgnoreCase(TypeDoc.TYPE_PRELEVEMENT)){
				daoPrelevement = new EJBLookup<ITaPrelevementServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_PRELEVEMENT_SERVICE, ITaPrelevementServiceRemote.class.getName());
				List<TaPrelevement> listeDocuments = daoPrelevement.findByCodeTiersAndDate(tiers,dateDeb,dateFin);
				for (IDocumentTiers taDocument : listeDocuments) {
					//taDocument.setTypeDocument(TypeDoc.TYPE_PRELEVEMENT);
					if(!dejaLie)accepte=!rechercheRDocument(taDocument,selectedType2);
	//				if(!accepte)break;
					if(accepte)listeDocumentsFinal.add((IDocumentTiers)taDocument);
					accepte=true;
				}
				setListeEntity(listeDocumentsFinal);
			}
			if(selectedType1.equalsIgnoreCase(TypeDoc.TYPE_APPORTEUR)){
				daoApporteur = new EJBLookup<ITaApporteurServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_APPORTEUR_SERVICE, ITaApporteurServiceRemote.class.getName());
				List<TaApporteur> listeDocuments = daoApporteur.findByCodeTiersAndDate(tiers,dateDeb,dateFin);
				for (IDocumentTiers taDocument : listeDocuments) {
					//taDocument.setTypeDocument(TypeDoc.TYPE_APPORTEUR);
					if(!dejaLie)accepte=!rechercheRDocument(taDocument,selectedType2);
	//				if(!accepte)break;
					if(accepte)listeDocumentsFinal.add((IDocumentTiers)taDocument);
					accepte=true;
				}
				setListeEntity(listeDocumentsFinal);
			}		
				if(getListeEntity()!=null && getListeEntity().size()>0)
					getListeObjet().addAll(this.remplirListe());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return getListeObjet();
	}
	
	@SuppressWarnings("unchecked")
	public LinkedList<IHMEnteteDocument> remplirListe() {
		IHMEnteteDocument ihm=null;
		LinkedList<IHMEnteteDocument> listeObjet=new LinkedList<IHMEnteteDocument>();
		
		listeObjet.clear();
		Boolean saisieTTC=null;
		for (Object doc : getListeEntity()) {
			if(saisieTTC==null)saisieTTC=LibConversion.intToBoolean(((IDocumentTiers)doc).getTtc());
			ihm=new IHMEnteteDocument();
			ihm.setIdTiers(((IDocumentTiers)doc).getTaTiers().getIdTiers());
			ihm.setIdDocument(((IDocumentTiers)doc).getIdDocument());
			ihm.setCodeDocument(((IDocumentTiers)doc).getCodeDocument());
			ihm.setDateDocument(((IDocumentTiers)doc).getDateDocument());
			ihm.setDateLivDocument(((IDocumentTiers)doc).getDateLivDocument());
			ihm.setCodeTiers(((IDocumentTiers)doc).getTaTiers().getCodeTiers());
			ihm.setNomTiers(((IDocumentTiers)doc).getTaTiers().getNomTiers());
			ihm.setNetTtcCalc(((IDocumentTiers)doc).getNetTtcCalc());
			ihm.setTxRemHtDocument(((IDocumentTiers)doc).getTxRemHtDocument());
			ihm.setTxRemTtcDocument(((IDocumentTiers)doc).getTxRemTtcDocument());
			if((((IDocumentTiers)doc).getTxRemHtDocument().compareTo(BigDecimal.valueOf(0))!=0
					||((IDocumentTiers)doc).getTxRemTtcDocument().compareTo(BigDecimal.valueOf(0))!=0)||
					(saisieTTC.compareTo(LibConversion.intToBoolean(((IDocumentTiers)doc).getTtc()))==0))
			ihm.setAccepte(false);
			else ihm.setAccepte(true);
			ihm.setTtc(LibConversion.intToBoolean(((IDocumentTiers)doc).getTtc()));
			ihm.setTypeDocument(((IDocumentTiers)doc).getTypeDocument());
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

	public LinkedList<IHMEnteteDocument> remplirListe(IDocumentTiers masterEntity,EntityManager em) {
		this.setListeEntity(new LinkedList<IDocumentTiers>());
		IDocumentTiers document;
		getListeEntity().clear();
		getListeObjet().clear();
		if(masterEntity != null) {
			for(TaRDocument rDocument : masterEntity.getTaRDocuments()){
				
				if(rDocument.getTaFacture()!=null){	
					document=((IDocumentTiers)rDocument.getTaFacture());
					if(document!=masterEntity){
						document.getTypeDocument();
						getListeEntity().add(document);
					}
				}
				if(rDocument.getTaAvoir()!=null  ){	
					document=((IDocumentTiers)rDocument.getTaAvoir());
					if(document!=masterEntity){
						document.getTypeDocument();
						getListeEntity().add(document);
					}
				}
				if(rDocument.getTaDevis()!=null  ){	
					document=((IDocumentTiers)rDocument.getTaDevis());
					if(document!=masterEntity){
						document.getTypeDocument();
						getListeEntity().add(document);
					}
				}
				if(rDocument.getTaAcompte()!=null  ){	
					document=((IDocumentTiers)rDocument.getTaAcompte());
					if(document!=masterEntity){
						getListeEntity().add(document);
					}
				}
				if(rDocument.getTaApporteur()!=null  ){	
					document=((IDocumentTiers)rDocument.getTaApporteur());
					if(document!=masterEntity){
						getListeEntity().add(document);
					}
				}
				if(rDocument.getTaBoncde()!=null  ){	
					document=((IDocumentTiers)rDocument.getTaBoncde());
					if(document!=masterEntity){
						getListeEntity().add(document);
					}
				}
				if(rDocument.getTaBonliv()!=null  ){	
					document=((IDocumentTiers)rDocument.getTaBonliv());
					if(document!=masterEntity){
						getListeEntity().add(document);
					}
				}
				if(rDocument.getTaPrelevement()!=null  ){	
					document=((IDocumentTiers)rDocument.getTaPrelevement());
					if(document!=masterEntity){
						getListeEntity().add(document);
					}
				}
				if(rDocument.getTaProforma()!=null  ){	
					document=((IDocumentTiers)rDocument.getTaProforma());
					if(document!=masterEntity){
						getListeEntity().add(document);
					}
				}
			}
			if(getListeEntity()!=null && getListeEntity().size()>0)
				getListeObjet().addAll(this.remplirListe());
		}
		return getListeObjet();
	}
}
