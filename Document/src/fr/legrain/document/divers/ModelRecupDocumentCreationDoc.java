package fr.legrain.document.divers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import fr.legrain.bdg.documents.service.remote.ITaAcompteServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaApporteurServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAvisEcheanceServiceRemote;
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
import fr.legrain.gestCom.Appli.ModelGeneralObjetEJB;
import fr.legrain.gestCom.Module_Document.IHMEnteteDocument;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.ejb.EJBLookup;
import fr.legrain.tiers.clientutility.JNDILookupClass;
import fr.legrain.tiers.model.TaTiers;


public class ModelRecupDocumentCreationDoc<DTO> extends ModelGeneralObjetEJB{
	
	IGenericCRUDServiceRemote dao=null;
	//private LinkedList<IHMEnteteDocument> listeObjet = new LinkedList<IHMEnteteDocument>();
	public ModelRecupDocumentCreationDoc(IGenericCRUDServiceRemote dao, Class<DTO> typeObjet ) {
		super(dao /*, typeObjet*/);
		this.dao=dao;
	}

	TypeDoc typeDocPresent = TypeDoc.getInstance();
//	EntityManager em =new TaFactureDAO().getEntityManager();
//	private Collection <IDocumentTiers> listeEntity = new LinkedList<IDocumentTiers>();
	private ITaTiersServiceRemote daoTiers = null;
	private ITaFactureServiceRemote daoFacture = null;
	private ITaAvoirServiceRemote daoAvoir = null;
	private ITaBonlivServiceRemote daoBonliv = null;
	private ITaBoncdeServiceRemote daoBoncde = null;
	private ITaApporteurServiceRemote daoApporteur = null;
	private ITaProformaServiceRemote daoProforma = null;
	private ITaDevisServiceRemote daoDevis = null;
	private ITaPrelevementServiceRemote daoPrelevement = null;
	private ITaAcompteServiceRemote daoAcompte = null;
	private ITaAvisEcheanceServiceRemote daoAvisEcheance = null;
	
	private String selectedTypeSelection=null;

	public LinkedList<IDocumentTiers> remplirListe(Date dateDeb,Date dateFin,List<TaTiers> listeTiers,String selectedTypeSelection,
			String selectedTypeCreation) {
		LinkedList<IDocumentTiers> list=new LinkedList<IDocumentTiers>();
		LinkedList<IDocumentTiers> listTemp=new LinkedList<IDocumentTiers>();
		for (TaTiers taTiers : listeTiers) {
			listTemp=remplirListe(dateDeb,dateFin,listeTiers,selectedTypeSelection,selectedTypeCreation);
			for (IDocumentTiers ihmEnteteDocument : listTemp) {
				list.add(ihmEnteteDocument);
			}
		}
		return list;
	}
	public LinkedList<IDocumentTiers> remplirListe(Date dateDeb,Date dateFin,String codeTiers,String selectedTypeSelection,
			String selectedTypeCreation,IDocumentTiers doc) {
		this.selectedTypeSelection=selectedTypeSelection;
		this.setListeEntity(new LinkedList<IDocumentTiers>());
		IDocumentTiers document;
		getListeEntity().clear();
		getListeObjet().clear();
		Boolean accepte=true;
		LinkedList<IDocumentTiers> listeDocumentsFinal =	new LinkedList<IDocumentTiers>();
		
		try {
		
		//if(this.selectedTypeSelection.equalsIgnoreCase(TypeDoc.TYPE_BON_LIVRAISON)){
			daoBonliv = new EJBLookup<ITaBonlivServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_BONLIV_SERVICE, ITaBonlivServiceRemote.class.getName());
//			daoBonliv.setEm(daoBonliv.getEntityManager());
			List<IDocumentTiers> listeDocuments = daoBonliv.selectDocNonTransformeRequete(doc,codeTiers,selectedTypeSelection,
					selectedTypeCreation,dateDeb,dateFin);
			for (IDocumentTiers taDocument : listeDocuments) {
				listeDocumentsFinal.add((IDocumentTiers)taDocument);
			}
			setListeEntity(listeDocumentsFinal);
		//}
//		if(this.selectedTypeSelection.equalsIgnoreCase(TypeDoc.TYPE_FACTURE)){
//			daoFacture=new TaFactureDAO();
//			daoFacture.setEm(daoFacture.getEntityManager());
//			List<IDocumentTiers> listeDocuments =daoFacture.selectDocNonTransformeRequete(doc,codeTiers,selectedTypeSelection,
//					selectedTypeCreation,dateDeb,dateFin);
//			for (IDocumentTiers taDocument : listeDocuments) {
//				listeDocumentsFinal.add((IDocumentTiers)taDocument);
//			}
//			setListeEntity(listeDocumentsFinal);
//		}	
//		if(this.selectedTypeSelection.equalsIgnoreCase(TypeDoc.TYPE_ACOMPTE)){
//			daoAcompte=new TaAcompteDAO();
//			daoAcompte.setEm(daoAcompte.getEntityManager());
//			List<IDocumentTiers> listeDocuments =daoAcompte.selectDocNonTransformeRequete(doc,codeTiers,selectedTypeSelection,
//					selectedTypeCreation,dateDeb,dateFin);
//			for (IDocumentTiers taDocument : listeDocuments) {
//				listeDocumentsFinal.add((IDocumentTiers)taDocument);
//			}
//			setListeEntity(listeDocumentsFinal);
//		}
//		if(this.selectedTypeSelection.equalsIgnoreCase(TypeDoc.TYPE_DEVIS)){
//			daoDevis=new TaDevisDAO();
//			daoDevis.setEm(daoDevis.getEntityManager());
//			List<IDocumentTiers> listeDocuments =daoDevis.selectDocNonTransformeRequete(doc,codeTiers,selectedTypeSelection,
//					selectedTypeCreation,dateDeb,dateFin);
//			for (IDocumentTiers taDocument : listeDocuments) {
//				listeDocumentsFinal.add((IDocumentTiers)taDocument);
//			}
//			setListeEntity(listeDocumentsFinal);
//		}
//		if(this.selectedTypeSelection.equalsIgnoreCase(TypeDoc.TYPE_AVOIR)){
//			daoAvoir=new TaAvoirDAO();
//			daoAvoir.setEm(daoAvoir.getEntityManager());
//			List<IDocumentTiers> listeDocuments =daoAvoir.selectDocNonTransformeRequete(doc,codeTiers,selectedTypeSelection,
//					selectedTypeCreation,dateDeb,dateFin);
//			for (IDocumentTiers taDocument : listeDocuments) {
//				listeDocumentsFinal.add((IDocumentTiers)taDocument);
//			}
//			setListeEntity(listeDocumentsFinal);
//		}
//		if(this.selectedTypeSelection.equalsIgnoreCase(TypeDoc.TYPE_PROFORMA)){
//			daoProforma=new TaProformaDAO();
//			daoProforma.setEm(daoProforma.getEntityManager());
//			List<IDocumentTiers> listeDocuments =daoProforma.selectDocNonTransformeRequete(doc,codeTiers,selectedTypeSelection,
//					selectedTypeCreation,dateDeb,dateFin);
//			for (IDocumentTiers taDocument : listeDocuments) {
//				listeDocumentsFinal.add((IDocumentTiers)taDocument);
//			}
//			setListeEntity(listeDocumentsFinal);
//		}
//		if(this.selectedTypeSelection.equalsIgnoreCase(TypeDoc.TYPE_PRELEVEMENT)){
//			daoPrelevement=new TaPrelevementDAO();
//			daoPrelevement.setEm(daoPrelevement.getEntityManager());
//			List<IDocumentTiers> listeDocuments =daoPrelevement.selectDocNonTransformeRequete(doc,codeTiers,selectedTypeSelection,
//					selectedTypeCreation,dateDeb,dateFin);
//			for (IDocumentTiers taDocument : listeDocuments) {
//				listeDocumentsFinal.add((IDocumentTiers)taDocument);
//			}
//			setListeEntity(listeDocumentsFinal);
//		}
//		if(this.selectedTypeSelection.equalsIgnoreCase(TypeDoc.TYPE_APPORTEUR)){
//			daoApporteur=new TaApporteurDAO();
//			daoApporteur.setEm(daoApporteur.getEntityManager());
//			List<IDocumentTiers> listeDocuments =daoApporteur.selectDocNonTransformeRequete(doc,codeTiers,selectedTypeSelection,
//					selectedTypeCreation,dateDeb,dateFin);
//			for (IDocumentTiers taDocument : listeDocuments) {
//				listeDocumentsFinal.add((IDocumentTiers)taDocument);
//			}
//			setListeEntity(listeDocumentsFinal);
//		}		
			if(getListeEntity()!=null && getListeEntity().size()>0 )
				getListeObjet().addAll(this.remplirListe());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return listeDocumentsFinal;
	}
	
	@SuppressWarnings("unchecked")
	public LinkedList<IHMEnteteDocument> remplirListe() {
		IHMEnteteDocument ihm=null;
		LinkedList<IHMEnteteDocument> listeObjet=new LinkedList<IHMEnteteDocument>();
		
		listeObjet.clear();
		Boolean saisieTTC=null;
		for (Object doc : getListeEntity()) {
//			"codeDocument","dateDocument","dateLivDocument","codeTiers","nomTiers","netTtcCalc",
			//"txRemHtDocument","txRemTtcDocument","accepte"}
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
		if(selectedTypeDoc==TypeDoc.TYPE_AVIS_ECHEANCE)
			for (TaRDocument relation : doc.getTaRDocuments()) {
				if(relation.getTaAvisEcheance()!=null){
					return true;
					}
			}		
	  return false;	
	}
}
