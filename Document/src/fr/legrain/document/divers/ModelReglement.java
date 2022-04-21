package fr.legrain.document.divers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;

import fr.legrain.bdg.documents.service.remote.ITaAcompteServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAvoirServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaReglementServiceRemote;
import fr.legrain.document.divers.TypeDoc;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaReglement;
import fr.legrain.gestCom.Module_Document.IDocumentTiersComplet;
import fr.legrain.gestCom.Module_Document.IHMEtat;
import fr.legrain.gestCom.Module_Document.IHMReglement;
import fr.legrain.lib.ejb.EJBLookup;
import fr.legrain.tiers.clientutility.JNDILookupClass;

public class ModelReglement {

	LinkedList<IHMReglement> listeObjet = new LinkedList<IHMReglement>();
	TypeDoc typeDocPresent = TypeDoc.getInstance();
	List<TaReglement> listeEntity = null;


	List<TaAvoir> listeEntityAvoir = null;
	List<TaAcompte> listeEntityAcompte = null;
	


	public LinkedList<IHMReglement> remplirListeReglements(IDocumentTiers taDocument,Date dateDeb,Date dateFin, EntityManager em) {
		IHMReglement ihmReglement = null;
		try {
			ITaReglementServiceRemote taReglementDAO = new EJBLookup<ITaReglementServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_REGLEMENT_SERVICE, ITaReglementServiceRemote.class.getName());
			listeObjet.clear();
			listeEntity=taReglementDAO.selectAll(taDocument,dateDeb,dateFin);
			if(listeEntity!=null){
				for (TaReglement reglement : listeEntity) {
					ihmReglement = new IHMReglement();
					ihmReglement.setId(reglement.getIdDocument());
					ihmReglement.setCodeDocument(reglement.getCodeDocument());
					ihmReglement.setTypeDocument("");
					ihmReglement.setDateDocument(reglement.getDateDocument());
					ihmReglement.setDateLivDocument(reglement.getDateLivDocument());
					if(reglement.getTaCompteBanque()!=null){
						ihmReglement.setIdCompteBanque(reglement.getTaCompteBanque().getIdCompteBanque());
						ihmReglement.setCodeBanque(reglement.getTaCompteBanque().getCodeBanque());
						ihmReglement.setCompte(reglement.getTaCompteBanque().getCompte());
						ihmReglement.setCodeGuichet(reglement.getTaCompteBanque().getCodeGuichet());
						ihmReglement.setCleRib(reglement.getTaCompteBanque().getCleRib());
					}
					if(reglement.getTaTPaiement()!=null){
						ihmReglement.setIdTPaiement(reglement.getTaTPaiement().getIdTPaiement());
						ihmReglement.setCodeTPaiement(reglement.getTaTPaiement().getCodeTPaiement());
					}
//					ihmReglement.setEtat(reglement.getEtat()); //EJB
					ihmReglement.setExport(reglement.getExport());
					ihmReglement.setLibelleDocument(reglement.getLibelleDocument());
					ihmReglement.setAffectation(BigDecimal.valueOf(0));
					ihmReglement.setNetTtcCalc(reglement.getNetTtcCalc());
					//					reglement.getTaReglement().remonteListReglements();
					ihmReglement.setMulti(reglement.getTaRReglements().size()>1);
					ihmReglement.setResteAAffecter(reglement.getResteAAffecter());
					listeObjet.add(ihmReglement);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		return listeObjet;
	}

	public LinkedList<IHMReglement> remplirListeReglementsNonTotalementRegles(IDocumentTiers taDocument,Date dateDeb,Date dateFin, EntityManager em) {
		IHMReglement ihmReglement = null;
//		TaReglementDAO taReglementDAO = new TaReglementDAO(em);
		try {
			ITaReglementServiceRemote taReglementDAO = new EJBLookup<ITaReglementServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_REGLEMENT_SERVICE, ITaReglementServiceRemote.class.getName());
			listeObjet.clear();
			listeEntity=taReglementDAO.selectAll(taDocument,dateDeb,dateFin);
			if(listeEntity!=null){
				for (TaReglement reglement : listeEntity) {
					if(reglement.getResteAAffecter().
							compareTo(BigDecimal.valueOf(0))>0){
						//si resteAAffecter > 0
						ihmReglement = new IHMReglement();
						ihmReglement.setId(reglement.getIdDocument());
						ihmReglement.setCodeDocument(reglement.getCodeDocument());
						ihmReglement.setTypeDocument("");
						ihmReglement.setDateDocument(reglement.getDateDocument());
						ihmReglement.setDateLivDocument(reglement.getDateLivDocument());
						if(reglement.getTaCompteBanque()!=null){
							ihmReglement.setIdCompteBanque(reglement.getTaCompteBanque().getIdCompteBanque());
							ihmReglement.setCodeBanque(reglement.getTaCompteBanque().getCodeBanque());
							ihmReglement.setCompte(reglement.getTaCompteBanque().getCompte());
							ihmReglement.setCodeGuichet(reglement.getTaCompteBanque().getCodeGuichet());
							ihmReglement.setCleRib(reglement.getTaCompteBanque().getCleRib());
						}
						if(reglement.getTaTPaiement()!=null){
							ihmReglement.setIdTPaiement(reglement.getTaTPaiement().getIdTPaiement());
							ihmReglement.setCodeTPaiement(reglement.getTaTPaiement().getCodeTPaiement());
						}
						//ihmReglement.setEtat(reglement.getEtat()); //EJB
						ihmReglement.setExport(reglement.getExport());
						ihmReglement.setLibelleDocument(reglement.getLibelleDocument());
						ihmReglement.setAffectation(BigDecimal.valueOf(0));
						ihmReglement.setNetTtcCalc(reglement.getNetTtcCalc());
						//					reglement.getTaReglement().remonteListReglements();
						ihmReglement.setMulti(reglement.getTaRReglements().size()>1);
						ihmReglement.setResteAAffecter(reglement.getResteAAffecter());
						listeObjet.add(ihmReglement);
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return listeObjet;
	}

	public LinkedList<IHMReglement> remplirListeAvoirsNonTotalementRegles(IDocumentTiers taDocument,Date dateDeb,Date dateFin, EntityManager em) {
		IHMReglement ihmReglement = null;
//		TaAvoirDAO taAvoirDAO = new TaAvoirDAO(em);
		try {
				
			ITaAvoirServiceRemote taAvoirDAO = new EJBLookup<ITaAvoirServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_AVOIR_SERVICE, ITaAvoirServiceRemote.class.getName());
			listeObjet.clear();
			listeEntityAvoir=taAvoirDAO.selectAll(taDocument,dateDeb,dateFin);
			if(listeEntityAvoir!=null){
				for (TaAvoir reglement : listeEntityAvoir) {
					if(reglement.getResteAAffecter().
							compareTo(BigDecimal.valueOf(0))>0){
						//si resteAAffecter > 0
						ihmReglement = new IHMReglement();
						ihmReglement.setId(reglement.getIdDocument());
						ihmReglement.setCodeDocument(reglement.getCodeDocument());
						ihmReglement.setTypeDocument("");
						ihmReglement.setDateDocument(reglement.getDateDocument());
						ihmReglement.setDateLivDocument(reglement.getDateLivDocument());
						if(reglement.getTaTPaiement()!=null){
							ihmReglement.setIdTPaiement(reglement.getTaTPaiement().getIdTPaiement());
							ihmReglement.setCodeTPaiement(reglement.getTaTPaiement().getCodeTPaiement());
						}
						ihmReglement.setEtat(reglement.getEtatDeSuppression());
						ihmReglement.setExport(reglement.getExport());
						ihmReglement.setLibelleDocument(reglement.getLibelleDocument());
						ihmReglement.setAffectation(BigDecimal.valueOf(0));
						ihmReglement.setNetTtcCalc(reglement.getNetTtcCalc());
						//					reglement.getTaReglement().remonteListReglements();
						ihmReglement.setMulti(reglement.getTaRAvoirs().size()>1);
						ihmReglement.setResteAAffecter(reglement.getResteAAffecter());
						listeObjet.add(ihmReglement);
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return listeObjet;
	}
	
	
	public LinkedList<IHMReglement> remplirListeAcomptesNonTotalementRegles(IDocumentTiers taDocument,Date dateDeb,Date dateFin, EntityManager em) {
		IHMReglement ihmReglement = null;
//		TaAcompteDAO taAcompteDAO = new TaAcompteDAO(em);
		try {
			ITaAcompteServiceRemote taAcompteDAO = new EJBLookup<ITaAcompteServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_ACOMPTE_SERVICE, ITaAcompteServiceRemote.class.getName());
			listeObjet.clear();
			listeEntityAcompte=taAcompteDAO.selectAll(taDocument,dateDeb,dateFin);
			if(listeEntityAcompte!=null){
				for (TaAcompte reglement : listeEntityAcompte) {
					if(reglement.getResteARegler().
							compareTo(BigDecimal.valueOf(0))>0){
						//si resteAAffecter > 0
						ihmReglement = new IHMReglement();
						ihmReglement.setId(reglement.getIdDocument());
						ihmReglement.setCodeDocument(reglement.getCodeDocument());
						ihmReglement.setTypeDocument("");
						ihmReglement.setDateDocument(reglement.getDateDocument());
						ihmReglement.setDateLivDocument(reglement.getDateLivDocument());
						if(reglement.getTaTPaiement()!=null){
							ihmReglement.setIdTPaiement(reglement.getTaTPaiement().getIdTPaiement());
							ihmReglement.setCodeTPaiement(reglement.getTaTPaiement().getCodeTPaiement());
						}
						ihmReglement.setEtat(reglement.getEtatDeSuppression());
						ihmReglement.setExport(reglement.getExport());
						ihmReglement.setLibelleDocument(reglement.getLibelleDocument());
						ihmReglement.setAffectation(BigDecimal.valueOf(0));
						ihmReglement.setNetTtcCalc(reglement.getNetTtcCalc());
						//					reglement.getTaReglement().remonteListReglements();
						ihmReglement.setMulti(reglement.getTaRAcomptes().size()>1);
						ihmReglement.setResteAAffecter(reglement.getResteARegler());
						listeObjet.add(ihmReglement);
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return listeObjet;
	}
	public LinkedList<IHMReglement> razListeReglements(EntityManager em) {
		IHMReglement ihmReglement = null;
		listeObjet.clear();

		return listeObjet;
	}


	public LinkedList<IHMReglement> getListeObjet() {
		return listeObjet;
	}

	public List<TaAvoir> getListeEntityAvoir() {
		return listeEntityAvoir;
	}

	public void setListeEntityAvoir(List<TaAvoir> listeEntityAvoir) {
		this.listeEntityAvoir = listeEntityAvoir;
	}

	public List<TaReglement> getListeEntity() {
		return listeEntity;
	}

	public List<TaAcompte> getListeEntityAcompte() {
		return listeEntityAcompte;
	}

	public void setListeEntityAcompte(List<TaAcompte> listeEntityAcompte) {
		this.listeEntityAcompte = listeEntityAcompte;
	}
}
