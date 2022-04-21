package fr.legrain.documents.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.inject.Typed;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.documents.service.remote.IModelCreationDocServiceRemote;
import fr.legrain.bdg.documents.service.remote.IModelRecupLigneDocumentCreationDocServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBonlivServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLBoncdeAchatServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLFlashServiceRemote;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.LigneDocumentDTO;
import fr.legrain.document.dto.TaLigneALigneDTO;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaFlash;
import fr.legrain.document.model.TaRDocument;
import fr.legrain.document.model.TypeDoc;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.tiers.model.TaTiers;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class ModelRecupLigneDocumentCreationDocService implements IModelRecupLigneDocumentCreationDocServiceRemote{
	private LinkedList<TaLigneALigneDTO> listeObjet = new LinkedList<TaLigneALigneDTO>();
	private Collection<ILigneDocumentTiers> listeEntity = new LinkedList<ILigneDocumentTiers>();
//	@EJB private IModelCreationDocServiceRemote modelCreationDoc ;

	TypeDoc typeDocPresent = TypeDoc.getInstance();

	@EJB private ITaLBoncdeAchatServiceRemote taLBoncdeAchatService;
	@EJB private ITaLFlashServiceRemote taLFlashService;


	
	private String selectedTypeSelection=null;

	@Override
	public LinkedList<ILigneDocumentTiers> remplirListe(Date dateDeb,Date dateFin,List<TaTiers> listeTiers,String selectedTypeSelection,
			String selectedTypeCreation) {
		LinkedList<ILigneDocumentTiers> list=new LinkedList<ILigneDocumentTiers>();
		LinkedList<ILigneDocumentTiers> listTemp=new LinkedList<ILigneDocumentTiers>();
		for (TaTiers taTiers : listeTiers) {
			listTemp=remplirListe(dateDeb,dateFin,listeTiers,selectedTypeSelection,selectedTypeCreation);
			for (ILigneDocumentTiers ihmEnteteDocument : listTemp) {
				list.add(ihmEnteteDocument);
			}
		}
		return list;
	}
	public LinkedList<TaLigneALigneDTO> remplirListe(Date dateDeb,Date dateFin,String codeTiers,String selectedTypeSelection,
			String selectedTypeCreation,IDocumentTiers doc,TaEtat etat) {
		this.selectedTypeSelection=selectedTypeSelection;
		this.setListeEntity(new LinkedList<ILigneDocumentTiers>());
		ILigneDocumentTiers document;
		getListeEntity().clear();
		getListeObjet().clear();
		Boolean accepte=true;
		LinkedList<TaLigneALigneDTO> listeDocumentsFinal =	new LinkedList<TaLigneALigneDTO>();
		List<TaLigneALigneDTO> listeDocuments=null;
		try {
			//en fonction du type de document selection
			if(selectedTypeSelection.equals(TypeDoc.TYPE_BON_COMMANDE_ACHAT)) {
				listeDocuments = taLBoncdeAchatService.selectLigneDocNonAffecte(doc,codeTiers,selectedTypeSelection,
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
	
	public LinkedList<TaLigneALigneDTO> remplirListe(Date dateDeb,Date dateFin,String codeTiers,String selectedTypeSelection,
			String selectedTypeCreation,TaFlash doc,TaEtat etat) {
		this.selectedTypeSelection=selectedTypeSelection;
		this.setListeEntity(new LinkedList<ILigneDocumentTiers>());
		ILigneDocumentTiers document;
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
			if(selectedTypeSelection.equals(TypeDoc.TYPE_PREPARATION) && selectedTypeCreation.equals(TypeDoc.TYPE_FLASH)) {
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
//		ILigneDocumentTiers document;
//		getListeEntity().clear();
//		getListeObjet().clear();
//		Boolean accepte=true;
//		LinkedList<ILigneDocumentTiers> listeDocumentsFinal =	new LinkedList<ILigneDocumentTiers>();
//		
//		try {
//		
//			List<ILigneDocumentTiers> listeDocuments = daoLBoncdeAchat.selectLigneDocNonAffecte(doc,codeTiers,selectedTypeSelection,
//					selectedTypeCreation,dateDeb,dateFin);
//			for (ILigneDocumentTiers taDocument : listeDocuments) {
//				listeDocumentsFinal.add((ILigneDocumentTiers)taDocument);
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
//			if(saisieTTC==null)saisieTTC=LibConversion.intToBoolean(((ILigneDocumentTiers)doc).getTtc());
			ihm=new TaLigneALigneDTO();
			ihm.setId(((ILigneDocumentTiers)doc).getIdLDocument());
			ihm.setIdDocument(((ILigneDocumentTiers)doc).getTaDocument().getIdDocument());
			ihm.setCodeDocument(((ILigneDocumentTiers)doc).getTaDocument().getCodeDocument());
			ihm.setDatePrevue(((ILigneDocumentTiers)doc).getTaDocument().getDateLivDocument());
			ihm.setCodeArticle(((ILigneDocumentTiers)doc).getTaArticle().getCodeArticle());
			ihm.setLibelleLigne(((ILigneDocumentTiers)doc).getLibLDocument());
			ihm.setQteOrigine(((ILigneDocumentTiers)doc).getQteLDocument());
			ihm.setUniteOrigine(((ILigneDocumentTiers)doc).getU1LDocument());
			ihm.setPrixUnitaire(((ILigneDocumentTiers)doc).getPrixULDocument());
			ihm.setQteRestante(((ILigneDocumentTiers)doc).getQteLDocument());
//			ihm.setCodeTiers(((ILigneDocumentTiers)doc).getTaTiers().getCodeTiers());
//			ihm.setNomTiers(((ILigneDocumentTiers)doc).getTaTiers().getNomTiers());
//			ihm.setNetTtcCalc(((ILigneDocumentTiers)doc).getNetTtcCalc());
//			ihm.setTxRemHtDocument(((ILigneDocumentTiers)doc).getTxRemHtDocument());
//			ihm.setTxRemTtcDocument(((ILigneDocumentTiers)doc).getTxRemTtcDocument());
//			if((((ILigneDocumentTiers)doc).getTxRemHtDocument().compareTo(BigDecimal.valueOf(0))!=0
//					||((ILigneDocumentTiers)doc).getTxRemTtcDocument().compareTo(BigDecimal.valueOf(0))!=0)||
//					(saisieTTC.compareTo(LibConversion.intToBoolean(((ILigneDocumentTiers)doc).getTtc()))==0))
			ihm.setAccepte(true);
//			else ihm.setAccepte(true);
//			ihm.setTtc(LibConversion.intToBoolean(((ILigneDocumentTiers)doc).getTtc()));
			listeObjet.add(ihm);
			ihm=null;
		}
		return listeObjet;
	}

//	public Boolean rechercheRDocument(ILigneDocumentTiers doc, String selectedTypeDoc){
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
	public Collection<ILigneDocumentTiers> getListeEntity() {
		return listeEntity;
	}
	public void setListeEntity(Collection<ILigneDocumentTiers> listeEntity) {
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
	public Boolean rechercheRDocument(ILigneDocumentTiers doc, String selectedTypeDoc) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public IModelCreationDocServiceRemote getModelCreationDoc() {
		// TODO Auto-generated method stub
		return null;
	}




}
