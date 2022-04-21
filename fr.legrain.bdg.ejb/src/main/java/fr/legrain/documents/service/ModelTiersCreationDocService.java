package fr.legrain.documents.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.documents.service.remote.IModelTiersCreationDocServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.model.TaRDocument;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.document.model.TypeDoc;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaTiers;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class ModelTiersCreationDocService implements IModelTiersCreationDocServiceRemote {
	private LinkedList<TaTiersDTO> listeObjet = new LinkedList<TaTiersDTO>();
	private Collection<TaTiers> listeEntity = new LinkedList<TaTiers>();


	TypeDoc typeDocPresent = TypeDoc.getInstance();
	@EJB private ITaTiersServiceRemote daoTiers;

	
	private String selectedTypeSelection=null;
	

	public LinkedList<TaTiersDTO> remplirListe(Date dateDeb,Date dateFin,String selectedTypeSelection,String selectedTypeCreation
			,String partieRequeteTiers,IDocumentTiers doc , TaTPaiement taTPaiement) {
		try {
			this.selectedTypeSelection=selectedTypeSelection;
			getListeEntity().clear();
			getListeObjet().clear();
			List<TaTiers> listeDocumentsFinal =	new ArrayList<TaTiers>(0);

			listeDocumentsFinal = daoTiers.selectTiersTypeDoc(partieRequeteTiers,doc,selectedTypeSelection, selectedTypeCreation, dateDeb, dateFin);
			for (TaTiers taTiers : listeDocumentsFinal) {
				if(taTPaiement==null || (taTiers.getTaTPaiement()!=null && taTiers.getTaTPaiement().getCodeTPaiement().equals(taTPaiement.getCodeTPaiement())))
					getListeEntity().add(taTiers);
			}
	
				if(getListeEntity()!=null && getListeEntity().size()>0)
					getListeObjet().addAll(this.remplirListe());
		} catch(Exception e) {
			e.printStackTrace();
		}
				
		return getListeObjet();
	}
	

	public LinkedList<TaTiersDTO> remplirListe() {
		TaTiersDTO ihm=null;
		LinkedList<TaTiersDTO> listeObjet=new LinkedList<TaTiersDTO>();
		
		listeObjet.clear();
		for (Object t : getListeEntity()) {
			ihm=new TaTiersDTO();
			ihm.setId(((TaTiers)t).getIdTiers());
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


	public LinkedList<TaTiersDTO> getListeObjet() {
		return listeObjet;
	}


	public void setListeObjet(LinkedList<TaTiersDTO> listeObjet) {
		this.listeObjet = listeObjet;
	}


	public Collection<TaTiers> getListeEntity() {
		return listeEntity;
	}


	public void setListeEntity(Collection<TaTiers> listeEntity) {
		this.listeEntity = listeEntity;
	}


	public String getSelectedTypeSelection() {
		return selectedTypeSelection;
	}


	public void setSelectedTypeSelection(String selectedTypeSelection) {
		this.selectedTypeSelection = selectedTypeSelection;
	}
}
