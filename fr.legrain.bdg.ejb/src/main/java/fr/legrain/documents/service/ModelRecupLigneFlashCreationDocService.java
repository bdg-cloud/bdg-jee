package fr.legrain.documents.service;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.documents.service.remote.IModelCreationDocServiceRemote;
import fr.legrain.bdg.documents.service.remote.IModelRecupLigneFlashCreationDocServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLBoncdeAchatServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLFlashServiceRemote;
import fr.legrain.document.dto.TaLigneALigneDTO;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaFlash;
import fr.legrain.document.model.TaLFlash;
import fr.legrain.document.model.TypeDoc;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.model.TaTiers;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class ModelRecupLigneFlashCreationDocService implements IModelRecupLigneFlashCreationDocServiceRemote{
	private LinkedList<TaLigneALigneDTO> listeObjet = new LinkedList<TaLigneALigneDTO>();
	private Collection<TaLFlash> listeEntity = new LinkedList<TaLFlash>();
//	@EJB private IModelCreationDocServiceRemote modelCreationDoc ;

	TypeDoc typeDocPresent = TypeDoc.getInstance();

	@EJB private ITaLBoncdeAchatServiceRemote taLBoncdeAchatService;
	@EJB private ITaLFlashServiceRemote taLFlashService;


	
	private String selectedTypeSelection=null;

	@Override
	public LinkedList<TaLFlash> remplirListe(Date dateDeb,Date dateFin,List<TaTiers> listeTiers,String selectedTypeSelection,
			String selectedTypeCreation) {
		LinkedList<TaLFlash> list=new LinkedList<TaLFlash>();
		LinkedList<TaLFlash> listTemp=new LinkedList<TaLFlash>();
		for (TaTiers taTiers : listeTiers) {
			listTemp=remplirListe(dateDeb,dateFin,listeTiers,selectedTypeSelection,selectedTypeCreation);
			for (TaLFlash ihmEnteteDocument : listTemp) {
				list.add(ihmEnteteDocument);
			}
		}
		return list;
	}
	
	
	public LinkedList<TaLigneALigneDTO> remplirListe(Date dateDeb,Date dateFin,String codeTiers,String selectedTypeSelection,
			String selectedTypeCreation,TaFlash doc,TaEtat etat) {
		this.selectedTypeSelection=selectedTypeSelection;
		this.setListeEntity(new LinkedList<TaLFlash>());
		TaLFlash document;
		getListeEntity().clear();
		getListeObjet().clear();
		Boolean accepte=true;
		LinkedList<TaLigneALigneDTO> listeDocumentsFinal =	new LinkedList<TaLigneALigneDTO>();
		List<TaLigneALigneDTO> listeDocuments=null;
		try {
			//en fonction du type de document selection
//			if(selectedTypeSelection.equals(TypeDoc.TYPE_BON_COMMANDE_ACHAT)) {
//				listeDocuments = taLBoncdeAchatService.selectLigneDocNonAffecte(doc,codeTiers,selectedTypeSelection,
//						selectedTypeCreation,dateDeb,dateFin, etat,TaEtat.ENCOURS);
//			}
			if(selectedTypeSelection.equals(TypeDoc.TYPE_FLASH) && selectedTypeCreation.equals(TypeDoc.TYPE_PREPARATION)) {
				listeDocuments = taLFlashService.selectLigneDocNonAffecte(doc,codeTiers,selectedTypeSelection,
						selectedTypeCreation,dateDeb,dateFin, etat,TaEtat.ENCOURS);
			}
			for (TaLigneALigneDTO taDocument : listeDocuments) {
				listeDocumentsFinal.add((TaLigneALigneDTO)taDocument);
			}

			if(getListeEntity()!=null && getListeEntity().size()>0 )
				getListeObjet().addAll(this.remplirListe());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return listeDocumentsFinal;
	}
	
//	public LinkedList<TaLigneALigneDTO> remplirListe(Date dateDeb,Date dateFin,String codeTiers,String selectedTypeSelection,
//			String selectedTypeCreation,IDocumentTiers doc) {
//		this.selectedTypeSelection=selectedTypeSelection;
//		this.setListeEntity(new LinkedList<TaLigneALigneDTO>());
//		TaLFlash document;
//		getListeEntity().clear();
//		getListeObjet().clear();
//		Boolean accepte=true;
//		LinkedList<TaLFlash> listeDocumentsFinal =	new LinkedList<TaLFlash>();
//		
//		try {
//		
//			List<TaLFlash> listeDocuments = daoLBoncdeAchat.selectLigneDocNonAffecte(doc,codeTiers,selectedTypeSelection,
//					selectedTypeCreation,dateDeb,dateFin);
//			for (TaLFlash taDocument : listeDocuments) {
//				listeDocumentsFinal.add((TaLFlash)taDocument);
//			}
//			setListeEntity(listeDocumentsFinal);
//	
//			if(getListeEntity()!=null && getListeEntity().size()>0 )
//				getListeObjet().addAll(this.remplirListe());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return listeDocumentsFinal;
//	}
	
	@SuppressWarnings("unchecked")
	public LinkedList<TaLigneALigneDTO> remplirListe() {
		TaLigneALigneDTO ihm=null;
		LinkedList<TaLigneALigneDTO> listeObjet=new LinkedList<TaLigneALigneDTO>();
		
		listeObjet.clear();
		Boolean saisieTTC=null;
		for (Object doc : getListeEntity()) {
			ihm=new TaLigneALigneDTO();
			ihm.setId(((TaLFlash)doc).getIdLFlash());
			ihm.setIdDocument(((TaLFlash)doc).getTaFlash().getIdFlash());
			ihm.setCodeDocument(((TaLFlash)doc).getTaFlash().getCodeFlash());
			ihm.setDatePrevue(((TaLFlash)doc).getTaFlash().getDateFlash());
			ihm.setCodeArticle(((TaLFlash)doc).getTaArticle().getCodeArticle());
			ihm.setLibelleLigne(((TaLFlash)doc).getLibLFlash());
			ihm.setQteOrigine(((TaLFlash)doc).getQteLFlash());
			ihm.setUniteOrigine(((TaLFlash)doc).getU1LFlash());
//			ihm.setPrixUnitaire(((TaLFlash)doc).getPrixULDocument());
			ihm.setQteRestante(((TaLFlash)doc).getQteLFlash());
//			ihm.setCodeTiers(((TaLFlash)doc).getTaTiers().getCodeTiers());
//			ihm.setNomTiers(((TaLFlash)doc).getTaTiers().getNomTiers());
//			ihm.setNetTtcCalc(((TaLFlash)doc).getNetTtcCalc());
//			ihm.setTxRemHtDocument(((TaLFlash)doc).getTxRemHtDocument());
//			ihm.setTxRemTtcDocument(((TaLFlash)doc).getTxRemTtcDocument());
//			if((((TaLFlash)doc).getTxRemHtDocument().compareTo(BigDecimal.valueOf(0))!=0
//					||((TaLFlash)doc).getTxRemTtcDocument().compareTo(BigDecimal.valueOf(0))!=0)||
//					(saisieTTC.compareTo(LibConversion.intToBoolean(((TaLFlash)doc).getTtc()))==0))
			ihm.setAccepte(true);
//			else ihm.setAccepte(true);
//			ihm.setTtc(LibConversion.intToBoolean(((TaLFlash)doc).getTtc()));
			listeObjet.add(ihm);
			ihm=null;
		}
		return listeObjet;
	}

//	public Boolean rechercheRDocument(TaLFlash doc, String selectedTypeDoc){
//		if(selectedTypeDoc==TypeDoc.TYPE_ACOMPTE)
//			for (TaRDocument relation : doc.getTaRDocuments()) {
//				if(relation.getTaAcompte()!=null){
//					return true;
//					}
//			}
//		if(selectedTypeDoc==TypeDoc.TYPE_APPORTEUR)
//			for (TaRDocument relation : doc.getTaRDocuments()) {
//				if(relation.getTaApporteur()!=null){
//					return true;
//					}
//			}
//		if(selectedTypeDoc==TypeDoc.TYPE_AVOIR)
//			for (TaRDocument relation : doc.getTaRDocuments()) {
//				if(relation.getTaAvoir()!=null){
//					return true;
//					}
//			}
//		if(selectedTypeDoc==TypeDoc.TYPE_BON_COMMANDE)
//			for (TaRDocument relation : doc.getTaRDocuments()) {
//				if(relation.getTaBoncde()!=null){
//					return true;
//					}
//			}
//		if(selectedTypeDoc==TypeDoc.TYPE_BON_LIVRAISON)
//			for (TaRDocument relation : doc.getTaRDocuments()) {
//				if(relation.getTaBonliv()!=null){
//					return true;
//					}
//			}
//		if(selectedTypeDoc==TypeDoc.TYPE_DEVIS)
//			for (TaRDocument relation : doc.getTaRDocuments()) {
//				if(relation.getTaDevis()!=null){
//					return true;
//					}
//			}
//		if(selectedTypeDoc==TypeDoc.TYPE_FACTURE)
//			for (TaRDocument relation : doc.getTaRDocuments()) {
//				if(relation.getTaFacture()!=null){
//					return true;
//					}
//			}
//		if(selectedTypeDoc==TypeDoc.TYPE_PRELEVEMENT)
//			for (TaRDocument relation : doc.getTaRDocuments()) {
//				if(relation.getTaPrelevement()!=null){
//					return true;
//					}
//			}
//		if(selectedTypeDoc==TypeDoc.TYPE_PROFORMA)
//			for (TaRDocument relation : doc.getTaRDocuments()) {
//				if(relation.getTaProforma()!=null){
//					return true;
//					}
//			}
//		if(selectedTypeDoc==TypeDoc.TYPE_AVIS_ECHEANCE)
//			for (TaRDocument relation : doc.getTaRDocuments()) {
//				if(relation.getTaAvisEcheance()!=null){
//					return true;
//					}
//			}		
//	  return false;	
//	}
	
	
	
	public LinkedList<TaLigneALigneDTO> getListeObjet() {
		return listeObjet;
	}
	public void setListeObjet(LinkedList<TaLigneALigneDTO> listeObjet) {
		this.listeObjet = listeObjet;
	}
	public Collection<TaLFlash> getListeEntity() {
		return listeEntity;
	}
	public void setListeEntity(Collection<TaLFlash> listeEntity) {
		this.listeEntity = listeEntity;
	}
	public TypeDoc getTypeDocPresent() {
		return typeDocPresent;
	}
	public void setTypeDocPresent(TypeDoc typeDocPresent) {
		this.typeDocPresent = typeDocPresent;
	}
	public String getSelectedTypeSelection() {
		return selectedTypeSelection;
	}
	public void setSelectedTypeSelection(String selectedTypeSelection) {
		this.selectedTypeSelection = selectedTypeSelection;
	}
	
	@Override
	public Boolean rechercheRDocument(TaLFlash doc, String selectedTypeDoc) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public IModelCreationDocServiceRemote getModelCreationDoc() {
		// TODO Auto-generated method stub
		return null;
	}




}
