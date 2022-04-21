package fr.legrain.documents.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.documents.service.remote.IModelCreationDocServiceRemote;
import fr.legrain.bdg.documents.service.remote.IModelRecupDocumentCreationDocServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBonlivServiceRemote;
import fr.legrain.document.dto.CreationDocLigneDTO;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IDocumentTiersDTO;
import fr.legrain.document.model.TaCreationDoc;
import fr.legrain.document.model.TaRDocument;
import fr.legrain.document.model.TypeDoc;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.tiers.model.TaTiers;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class ModelRecupDocumentCreationDocService implements IModelRecupDocumentCreationDocServiceRemote{
	private LinkedList<DocumentDTO> listeObjet = new LinkedList<DocumentDTO>();
	private Collection<IDocumentTiers> listeEntity = new LinkedList<IDocumentTiers>();
//	private LinkedList<TaCreationDoc> listeCreation = new LinkedList<TaCreationDoc>();
	@EJB private IModelCreationDocServiceRemote modelCreationDoc ;

	TypeDoc typeDocPresent = TypeDoc.getInstance();

	@EJB private ITaBonlivServiceRemote daoBonliv ;

	
	private String selectedTypeSelection=null;

	@Override
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
		
			List<IDocumentTiers> listeDocuments = daoBonliv.selectDocNonTransformeRequete(doc,codeTiers,selectedTypeSelection,
					selectedTypeCreation,dateDeb,dateFin);
			for (IDocumentTiers taDocument : listeDocuments) {
				listeDocumentsFinal.add((IDocumentTiers)taDocument);
			}
			setListeEntity(listeDocumentsFinal);
	
			if(getListeEntity()!=null && getListeEntity().size()>0 )
				getListeObjet().addAll(this.remplirListe());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return listeDocumentsFinal;
	}
	
	@SuppressWarnings("unchecked")
	public LinkedList<DocumentDTO> remplirListe() {
		DocumentDTO ihm=null;
		LinkedList<DocumentDTO> listeObjet=new LinkedList<DocumentDTO>();
		
		listeObjet.clear();
		Boolean saisieTTC=null;
		for (Object doc : getListeEntity()) {
//			"codeDocument","dateDocument","dateLivDocument","codeTiers","nomTiers","netTtcCalc",
			//"txRemHtDocument","txRemTtcDocument","accepte"}
			if(saisieTTC==null)saisieTTC=LibConversion.intToBoolean(((IDocumentTiers)doc).getTtc());
			ihm=new DocumentDTO();
			ihm.setIdTiers(((IDocumentTiers)doc).getTaTiers().getIdTiers());
			ihm.setId(((IDocumentTiers)doc).getIdDocument());
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
	
	
	
	public LinkedList<DocumentDTO> getListeObjet() {
		return listeObjet;
	}
	public void setListeObjet(LinkedList<DocumentDTO> listeObjet) {
		this.listeObjet = listeObjet;
	}
	public Collection<IDocumentTiers> getListeEntity() {
		return listeEntity;
	}
	public void setListeEntity(Collection<IDocumentTiers> listeEntity) {
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
	public IModelCreationDocServiceRemote getModelCreationDoc() {
		return modelCreationDoc;
	}
	public void setModelCreationDoc(IModelCreationDocServiceRemote modelCreationDoc) {
		this.modelCreationDoc = (ModelCreationDocService) modelCreationDoc;
	}



}
