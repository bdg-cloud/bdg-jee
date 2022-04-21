package fr.legrain.bdg.webapp.reglementMultiple;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import fr.legrain.bdg.documents.service.remote.ITaAcompteServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAvoirServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaReglementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaCompteBanqueServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTPaiementServiceRemote;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.dto.TaReglementDTO;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaRAvoir;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TypeDoc;


@Named
@ViewScoped
public class ModelReglement implements Serializable {

	private static final long serialVersionUID = -1147430385613971885L;
	
	@EJB ITaReglementServiceRemote taReglementService;
	@EJB ITaFactureServiceRemote taFactureService;
	@EJB ITaAvoirServiceRemote taAvoirService;
	@EJB ITaAcompteServiceRemote taAcompteService;
	@EJB ITaCompteBanqueServiceRemote taCompteBanqueService;
	@EJB ITaTPaiementServiceRemote taTPaiementService;
	
	LinkedList<TaReglementDTOJSF> listeObjet = new LinkedList<TaReglementDTOJSF>();
	TypeDoc typeDocPresent = TypeDoc.getInstance();

	List<TaReglementDTO> listeEntity = null;
	List<TaReglementDTO> listeEntityReglePartiel = null;
	List<TaAvoir> listeEntityAvoir = null;




	public LinkedList<TaReglementDTOJSF> remplirListeReglements(TaFactureDTO taDocument,Date dateDeb,Date dateFin) {
		TaReglementDTOJSF ihmReglement = null;
		try {
			listeObjet.clear();
			listeEntity=taReglementService.selectAllLieAuDocument(taDocument,dateDeb,dateFin);
			if(listeEntity!=null){
				for (TaReglementDTO reglement : listeEntity) {


								ihmReglement = remplirIHMReglement(reglement);
//								ihmReglement.getDto().setAffectation(reglement.calculAffectationEnCoursReel((TaFacture) taDocument));
//								ihmReglement.getDto().setExportAffectation(LibConversion.intToBoolean(rReglement.getExport()));
								listeObjet.add(ihmReglement);
							
						
//					ihmReglement.getDto().setMulti(reglement.getTaRReglements().size()>1 || reglement.affectationMultiple((TaFacture) taDocument));
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		return listeObjet;
	}

	public LinkedList<TaReglementDTOJSF> remplirListeReglementsNonTotalementRegles(TaFactureDTO taDocument,Date dateDeb,Date dateFin) {
		TaReglementDTOJSF ihmReglement = null;
		try {
			listeObjet.clear();
			listeEntityReglePartiel=taReglementService.selectReglementNonLieAuDocument(taDocument,dateDeb,dateFin);
			if(listeEntityReglePartiel!=null){
				for (TaReglementDTO reglement : listeEntityReglePartiel) {
//						if(reglement.getTaRReglements()==null ||reglement.getTaRReglements().isEmpty()){
//							ihmReglement = remplirIHMReglement(reglement,taDocument);
//							listeObjet.add(ihmReglement);
//						}else{
									ihmReglement = remplirIHMReglement(reglement);
									listeObjet.add(ihmReglement);
//						}
//						ihmReglement.getDto().setMulti(reglement.getTaRReglements().size()>1 || reglement.affectationMultiple((TaFacture) taDocument));
					

				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		return listeObjet;
	}

	public TaReglementDTOJSF remplirIHMReglement(TaReglementDTO reglement){
		TaReglementDTOJSF ihmReglement = new TaReglementDTOJSF();
//		ihmReglement.setDto(reglement);

		ihmReglement.getDto().setId(reglement.getIdDocument());
		ihmReglement.getDto().setCodeDocument(reglement.getCodeDocument());
		ihmReglement.getDto().setTypeDocument(TaReglement.TYPE_DOC);
		ihmReglement.getDto().setDateDocument(reglement.getDateDocument());
		ihmReglement.getDto().setDateLivDocument(reglement.getDateLivDocument());

			ihmReglement.getDto().setIdCompteBanque(reglement.getIdCompteBanque());
			ihmReglement.getDto().setCodeBanque(reglement.getCodeBanque());
			ihmReglement.getDto().setCompte(reglement.getCompte());
			ihmReglement.getDto().setCodeGuichet(reglement.getCodeGuichet());
			ihmReglement.getDto().setCleRib(reglement.getCleRib());
			ihmReglement.getDto().setIban(reglement.getIban());
			ihmReglement.getDto().setCodeBic(reglement.getCodeBic());


			ihmReglement.getDto().setIdTPaiement(reglement.getIdTPaiement());
			ihmReglement.getDto().setCodeTPaiement(reglement.getCodeTPaiement());


		ihmReglement.getDto().setEtat(reglement.getEtat());
		ihmReglement.getDto().setLibelleDocument(reglement.getLibelleDocument());
		ihmReglement.getDto().setAffectation(reglement.getAffectation());
		ihmReglement.getDto().setNetTtcCalc(reglement.getNetTtcCalc());
		ihmReglement.getDto().setMulti(reglement.getMulti());
		ihmReglement.getDto().setResteAAffecter(reglement.getResteAAffecter());
		ihmReglement.getDto().setCodeAcompte(reglement.getCodeAcompte());
		ihmReglement.getDto().setCodePrelevement(reglement.getCodePrelevement());
		ihmReglement.getDto().setEstMiseADisposition(reglement.getEstMisADisposition());
		ihmReglement.getDto().setDateExport(reglement.getDateExport());
		ihmReglement.getDto().setDateVerrouillage(reglement.getDateVerrouillage());
		
		ihmReglement.getDto().setEstMiseADispositionAffectation(reglement.getEstMisADispositionAffectation());
		ihmReglement.getDto().setDateExportAffectation(reglement.getDateExportAffectation());
		ihmReglement.getDto().setDateVerrouillageAffectation(reglement.getDateVerrouillageAffectation());
		
		if(ihmReglement.getDto().getIdCompteBanque()!=null){
			try {
				ihmReglement.setTaCompteBanque(taCompteBanqueService.findById(ihmReglement.getDto().getIdCompteBanque()));
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(ihmReglement.getDto().getIdTPaiement()!=null){
			try {
				ihmReglement.setTaTPaiement(taTPaiementService.findById(ihmReglement.getDto().getIdTPaiement()));
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ihmReglement;		

	}

	public LinkedList<TaReglementDTOJSF> remplirListeAvoirs(TaFactureDTO taDocument,Date dateDeb,Date dateFin) {
		TaReglementDTOJSF ihmReglement = null;
		try {

			listeObjet.clear();
			listeEntityAvoir=taAvoirService.selectAllLieAuDocument(taDocument,dateDeb,dateFin);
			if(listeEntityAvoir!=null){
				for (TaAvoir avoir : listeEntityAvoir) {
					//					if(reglement.getResteAAffecter().
					//							compareTo(BigDecimal.valueOf(0))>0){
					//si resteAAffecter > 0
					ihmReglement = new TaReglementDTOJSF();
					ihmReglement.getDto().setId(avoir.getIdDocument());
					ihmReglement.getDto().setCodeDocument(avoir.getCodeDocument());
					ihmReglement.getDto().setTypeDocument("");
					ihmReglement.getDto().setDateDocument(avoir.getDateDocument());
					ihmReglement.getDto().setDateLivDocument(avoir.getDateLivDocument());
					if(avoir.getTaTPaiement()!=null){
						ihmReglement.getDto().setIdTPaiement(avoir.getTaTPaiement().getIdTPaiement());
						ihmReglement.getDto().setCodeTPaiement(avoir.getTaTPaiement().getCodeTPaiement());
					}
					ihmReglement.getDto().setEtat(avoir.getEtatDeSuppression());
					ihmReglement.getDto().setLibelleDocument(avoir.getLibelleDocument());
					//						for (TaRAvoir rAvoir : avoir.getTaRAvoirs()) {
					//							rAvoir=tar
					//						}
					TaFacture fac =taFactureService.findById(taDocument.getId());
					ihmReglement.getDto().setAffectation(avoir.calculAffectationEnCoursReel(fac));
					ihmReglement.getDto().setNetTtcCalc(avoir.getNetTtcCalc());
					//					reglement.getTaReglement().remonteListReglements();
					ihmReglement.getDto().setMulti(avoir.getTaRAvoirs().size()>1);
					ihmReglement.getDto().setResteAAffecter(avoir.getResteAAffecter());
					
					ihmReglement.getDto().setDateExport(avoir.getDateExport());
					ihmReglement.getDto().setDateVerrouillage(avoir.getDateVerrouillage());
					ihmReglement.getDto().setEstMiseADisposition(avoir.getTaMiseADisposition()!=null);
					
					for (TaRAvoir rr : avoir.getTaRAvoirs()) {
						if(rr.getTaFacture().getCodeDocument().equals(taDocument.getCodeDocument())) {
							ihmReglement.getDto().setDateExportAffectation(rr.getDateExport());
							ihmReglement.getDto().setDateVerrouillageAffectation(rr.getDateVerrouillage());
							ihmReglement.getDto().setEstMiseADispositionAffectation(rr.getTaMiseADisposition()!=null);
						}
					}
					listeObjet.add(ihmReglement);
					//					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		return listeObjet;
	}


	public LinkedList<TaReglementDTOJSF> remplirListeAvoirsNonTotalementRegles(TaFactureDTO taDocument,Date dateDeb,Date dateFin) {
		TaReglementDTOJSF ihmReglement = null;
		//		TaAvoirDAO taAvoirDAO = new TaAvoirDAO(em);
		try {

			listeObjet.clear();
			listeEntityAvoir=taAvoirService.selectReglementNonLieAuDocument(taDocument,dateDeb,dateFin);
			if(listeEntityAvoir!=null){
				for (TaAvoir reglement : listeEntityAvoir) {
					if(reglement.getResteAAffecter().
							compareTo(BigDecimal.valueOf(0))>0){
						//si resteAAffecter > 0
						ihmReglement = new TaReglementDTOJSF();
						ihmReglement.getDto().setId(reglement.getIdDocument());
						ihmReglement.getDto().setCodeDocument(reglement.getCodeDocument());
						ihmReglement.getDto().setTypeDocument("");
						ihmReglement.getDto().setDateDocument(reglement.getDateDocument());
						ihmReglement.getDto().setDateLivDocument(reglement.getDateLivDocument());
						if(reglement.getTaTPaiement()!=null){
							ihmReglement.getDto().setIdTPaiement(reglement.getTaTPaiement().getIdTPaiement());
							ihmReglement.getDto().setCodeTPaiement(reglement.getTaTPaiement().getCodeTPaiement());
						}
						ihmReglement.getDto().setEtat(reglement.getEtatDeSuppression());
						ihmReglement.getDto().setDateExport(reglement.getDateExport());
						ihmReglement.getDto().setLibelleDocument(reglement.getLibelleDocument());
						ihmReglement.getDto().setAffectation(BigDecimal.ZERO);
						ihmReglement.getDto().setNetTtcCalc(reglement.getNetTtcCalc());
						//					reglement.getTaReglement().remonteListReglements();
						ihmReglement.getDto().setMulti(reglement.getTaRAvoirs().size()>1);
						ihmReglement.getDto().setResteAAffecter(reglement.getResteAAffecter());
						listeObjet.add(ihmReglement);
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		return listeObjet;
	}


	//	public LinkedList<TaRReglementDTOJSF> remplirListeAcomptesNonTotalementRegles(IDocumentTiers taDocument,Date dateDeb,Date dateFin) {
	//		TaRReglementDTOJSF ihmReglement = null;
	////		TaAcompteDAO taAcompteDAO = new TaAcompteDAO(em);
	//		try {
	//			listeObjet.clear();
	//			listeEntityAcompte=taAcompteService.selectAll(taDocument,dateDeb,dateFin);
	//			if(listeEntityAcompte!=null){
	//				for (TaAcompte reglement : listeEntityAcompte) {
	//					if(reglement.getResteARegler().
	//							compareTo(BigDecimal.valueOf(0))>0){
	//						//si resteAAffecter > 0
	//						ihmReglement = new TaRReglementDTOJSF();
	//						ihmReglement.getDto().setId(reglement.getIdDocument());
	//						ihmReglement.getDto().setCodeDocument(reglement.getCodeDocument());
	//						ihmReglement.getDto().setTypeDocument("");
	//						ihmReglement.getDto().setDateDocument(reglement.getDateDocument());
	//						ihmReglement.getDto().setDateLivDocument(reglement.getDateLivDocument());
	//						if(reglement.getTaTPaiement()!=null){
	//							ihmReglement.getDto().setIdTPaiement(reglement.getTaTPaiement().getIdTPaiement());
	//							ihmReglement.getDto().setCodeTPaiement(reglement.getTaTPaiement().getCodeTPaiement());
	//						}
	//						ihmReglement.getDto().setEtat(reglement.getEtatDeSuppression());
	//						ihmReglement.getDto().setExport(LibConversion.intToBoolean(reglement.getExport()));
	//						ihmReglement.getDto().setLibelleDocument(reglement.getLibelleDocument());
	//						ihmReglement.getDto().setAffectation(reglement.calculSommeAffectationAvecDocument((TaFacture) taDocument));
	//						ihmReglement.getDto().setNetTtcCalc(reglement.getNetTtcCalc());
	//						//					reglement.getTaReglement().remonteListReglements();
	//						ihmReglement.getDto().setMulti(reglement.getTaRAcomptes().size()>1);
	//						ihmReglement.getDto().setResteAAffecter(reglement.getResteARegler());
	//						listeObjet.add(ihmReglement);
	//					}
	//				}
	//			}
	//		} catch(Exception e) {
	//			e.printStackTrace();
	//		}
	//		return listeObjet;
	//	}
	public LinkedList<TaReglementDTOJSF> razListeReglements() {
		TaReglementDTOJSF ihmReglement = null;
		listeObjet.clear();

		return listeObjet;
	}


	public LinkedList<TaReglementDTOJSF> getListeObjet() {
		return listeObjet;
	}

	public List<TaAvoir> getListeEntityAvoir() {
		return listeEntityAvoir;
	}

	public void setListeEntityAvoir(List<TaAvoir> listeEntityAvoir) {
		this.listeEntityAvoir = listeEntityAvoir;
	}

	public List<TaReglementDTO> getListeEntity() {
		return listeEntity;
	}

	public List<TaReglementDTO> getListeEntityReglePartiel() {
		return listeEntityReglePartiel;
	}
}
