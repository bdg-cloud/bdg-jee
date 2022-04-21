package fr.legrain.documents.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.ejb.CreateException;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.article.dao.ILotDAO;
import fr.legrain.article.dao.ITvaDAO;
import fr.legrain.article.model.TaLFabricationPF;
import fr.legrain.article.model.TaLot;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaMouvementStockServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBonReceptionServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLigneALigneEcheanceServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaAutorisationsDossierServiceRemote;
import fr.legrain.bdg.edition.osgi.EditionProgrammeDefaut;
import fr.legrain.bdg.general.service.remote.BusinessValidationException;
import fr.legrain.bdg.model.mapping.mapper.TaBonReceptionMapper;
import fr.legrain.birt.BirtUtil;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.data.AbstractDocumentService;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.ITaLFabrication;
import fr.legrain.document.dto.TaBonReceptionDTO;
import fr.legrain.document.dto.TaLBonReceptionDTO;
import fr.legrain.document.model.LigneTva;
import fr.legrain.document.model.SWTDocument;
import fr.legrain.document.model.TaBonReception;
import fr.legrain.document.model.TaBoncdeAchat;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaHistREtat;
import fr.legrain.document.model.TaHistREtatLigneDocument;
import fr.legrain.document.model.TaLBonReception;
import fr.legrain.document.model.TaLBoncdeAchat;
import fr.legrain.document.model.TaLigneALigne;
import fr.legrain.document.model.TaPreparation;
import fr.legrain.document.model.TaREtat;
import fr.legrain.document.model.TaREtatLigneDocument;
import fr.legrain.documents.dao.IBonReceptionDAO;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.droits.model.IModulesProgramme;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.edition.model.TaActionEdition;
import fr.legrain.hibernate.multitenant.SchemaResolver;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibCalcul;
import fr.legrain.stock.model.TaMouvementStock;
import fr.legrain.tiers.dao.ICPaiementDAO;
import fr.legrain.tiers.dao.ITCPaiementDAO;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaCPaiement;
import fr.legrain.tiers.model.TaTCPaiement;


/**
 * Session Bean implementation class TaBonReceptionBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaBonReceptionService extends AbstractDocumentService<TaBonReception, TaBonReceptionDTO> implements ITaBonReceptionServiceRemote {

	static Logger logger = Logger.getLogger(TaBonReceptionService.class);

	@Inject private IBonReceptionDAO dao;
	@Inject private ITvaDAO taTvaDAO;
	@Inject private ITCPaiementDAO taTCPaiementDAO;
	@Inject private ICPaiementDAO taCPaiementDAO;
	@Inject private	SessionInfo sessionInfo;
	
	@EJB private ITaLotServiceRemote taLotService;
	@EJB private ITaGenCodeExServiceRemote gencode;
	
	@EJB private ITaLEcheanceServiceRemote taLEcheanceService;
	@EJB private ITaLigneALigneEcheanceServiceRemote taLigneALigneEcheanceService;
	@EJB private ITaAutorisationsDossierServiceRemote taAutorisationDossierService;
	/**
	 * Default constructor. 
	 */
	public TaBonReceptionService() {
		super(TaBonReception.class,TaBonReceptionDTO.class);
		editionDefaut = EditionProgrammeDefaut.EDITION_DEFAUT_BON_RECEPTION_FICHIER;
	}
	
	public List<TaBonReceptionDTO> findAllLight() {
		return dao.findAllLight();
	}
	
	public List<TaBonReceptionDTO> findByCodeLight(String code) {
		return dao.findByCodeLight(code);
	}
	
	//	private String defaultJPQLQuery = "select a from TaBonReception a";
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public String genereCode( Map<String, String> params) {
		//return dao.genereCode();
		try {
			return gencode.genereCodeJPA(TaBonReception.class.getSimpleName(),params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NOUVEAU CODE";
	}
	
	public void annuleCode(String code) {
		try {
			
			gencode.annulerCodeGenere(gencode.findByCode(TaBonReception.class.getSimpleName()),code);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void verrouilleCode(String code) {
		try {
			gencode.rentreCodeGenere(gencode.findByCode(TaBonReception.class.getSimpleName()),code, sessionInfo.getSessionID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	public IDocumentTiers calculeTvaEtTotaux(IDocumentTiers doc) {
//		doc.calculeTvaEtTotaux();
//		return doc;
//	}
	
	private TaBonReception initList(TaBonReception doc) {
		//Le processus de sérialization affecte null pour une liste vide, donc il faut "recréer" les liste vide
		if(doc.getLignes()==null)
			doc.setLignes(new ArrayList<TaLBonReception>(0));
		
		if(doc.getLignesTVA()==null)
			doc.setLignesTVA(new ArrayList<LigneTva>());
		
		return doc;
	}
	
	public void calculeTvaEtTotaux(TaBonReception doc){
		calculTvaTotal(initList(doc));
		calculTotaux(initList(doc));
	}
	
	/**
	 * Calcul des totaux de la facture
	 */
	public void calculTotaux(TaBonReception doc) {
		
//			    MT_TVA Numeric(15,2),
			doc.setMtHtCalc(new BigDecimal(0));
			doc.setNetHtCalc(new BigDecimal(0));
			doc.setMtTtcCalc(new BigDecimal(0));
			doc.setMtTtcAvantRemiseGlobaleCalc(new BigDecimal(0));
			for (Object ligne : doc.getLignes()) {
				if(((TaLBonReception)ligne).getTaTLigne().getCodeTLigne().equals(SWTDocument.C_CODE_T_LIGNE_H)) {
					if(((TaLBonReception)ligne).getMtHtLApresRemiseGlobaleDocument()!=null)
						doc.setNetHtCalc(doc.getNetHtCalc().add(((TaLBonReception)ligne).getMtHtLApresRemiseGlobaleDocument()));
					if(((TaLBonReception)ligne).getMtTtcLApresRemiseGlobaleDocument()!=null)
						doc.setMtTtcCalc(doc.getMtTtcCalc().add(((TaLBonReception)ligne).getMtTtcLApresRemiseGlobaleDocument()));
					if(((TaLBonReception)ligne).getMtHtLDocument()!=null)
						doc.setMtHtCalc(doc.getMtHtCalc().add(((TaLBonReception)ligne).getMtHtLDocument()));
					if(((TaLBonReception)ligne).getMtTtcLDocument()!=null)
						doc.setMtTtcAvantRemiseGlobaleCalc(doc.getMtTtcAvantRemiseGlobaleCalc().add(((TaLBonReception)ligne).getMtTtcLDocument()));
				}
				
			}
			
			doc.setNetTvaCalc(doc.getMtTtcCalc().subtract(doc.getNetHtCalc()));
			BigDecimal tva = new BigDecimal(0);
			for (LigneTva ligneTva : doc.getLignesTVA()) {
				tva = tva.add(ligneTva.getMtTva());
			}
			if(tva.compareTo(doc.getNetTvaCalc())!=0) {
				logger.error("Montant de la TVA incorrect : "+doc.getNetTvaCalc()+" ** "+tva);
			}
			
			//setNetTtcCalc(getMtTtcCalc().subtract(getMtTtcCalc().multiply(getRemTtcFacture().divide(new BigDecimal(100)))));
			//setNetTtcCalc(getMtTtcCalc().subtract(getMtTtcCalc().multiply(getTxRemTtcDocument().divide(new BigDecimal(100)))));
			doc.setNetTtcCalc(doc.getMtTtcCalc().subtract(doc.getMtTtcCalc().multiply(doc.getTxRemTtcDocument().divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_HALF_UP)));
			
			/*
			 * remise HT déjà calculée dans dispatcherTva()
			 */
			//setRemTtcDocument(getMtTtcCalc().subtract(getNetTtcCalc()));
			doc.setRemTtcDocument(doc.getMtTtcCalc().subtract(doc.getNetTtcCalc()).setScale(2,BigDecimal.ROUND_HALF_UP));

//#Bon_de_reception			
//			doc.setNetAPayer(doc.getNetTtcCalc().subtract(doc.getRegleDocument()));
			
			//TODO A Finir ou a supprimer
//			select sum(f.mt_tva_recup) from calcul_tva_direct(:module,:id_document,:taux_r_ht,:ttc) f into :MTNETTVA;
//			tva=:mtnettva;
//			mt_ttc=:totalttc;
//			mt_tva=:mt_ttc-:mt_ht;
//			if (ttc=1) {
//			       txremiseht = taux_r_ht;
//			       mtnetttc=:mt_ttc - (:mt_ttc*(:txremiseht/100));
//			       MTNETHT=:mtnetttc - :MTNETTVA;
//			       remise_ht =  :totalttc - :mtnetttc ;
//			} else {
//			      txremiseht = taux_r_ht;
//			      MTNETHT=:mt_ht-(:mt_ht*(:txremiseht/100));
//			      mtnetttc=:MTNETHT + :MTNETTVA;
//			      remise_ht = mt_ht - mtnetht;
//			}
//			  txremisettc = taux_r_ttc;
//			  remise_ttc = (:mtnetttc * (:txremisettc/100));
//			  mtnetttc = :mtnetttc -:remise_ttc;
//			  netapayer = :mtnetttc - :regle;
	}

	/**
	 * Reparti le total chaque code TVA sur l'ensemble des lignes concernées par ce code. 
	 */
	public void dispatcherTva(TaBonReception doc) {

		BigDecimal tvaLigne = new BigDecimal(0);
		BigDecimal totalTemp = new BigDecimal(0);

		for (Object ligne : doc.getLignes()) {
			if(((TaLBonReception)ligne).getMtHtLDocument()!=null)
				totalTemp = totalTemp.add(((TaLBonReception)ligne).getMtHtLDocument());
		}
		if(totalTemp!=null && doc.getTxRemHtDocument()!=null)
			doc.setRemHtDocument(totalTemp.multiply(doc.getTxRemHtDocument().divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_HALF_UP));	
		
		for (TaLBonReception ligne : doc.getLignes()) {
		if(doc.getTxRemHtDocument()!=null && doc.getTxRemHtDocument().signum()>0 && ligne.getMtHtLDocument()!=null  && ligne.getMtTtcLDocument()!=null) {
				if(doc.getTtc()==1){
					((TaLBonReception)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLBonReception)ligne).getMtTtcLDocument().subtract(((TaLBonReception)ligne).getMtTtcLDocument()
							.multiply(doc.getTxRemHtDocument()).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
					((TaLBonReception)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLBonReception)ligne).getMtTtcLApresRemiseGlobaleDocument());
					
				}else{
					((TaLBonReception)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLBonReception)ligne).getMtHtLDocument().subtract(((TaLBonReception)ligne).getMtHtLDocument()
							.multiply(doc.getTxRemHtDocument()).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
					((TaLBonReception)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLBonReception)ligne).getMtHtLApresRemiseGlobaleDocument());	
				}
			}
		}
				
		for (LigneTva ligneTva : doc.getLignesTVA()) {

			if (ligneTva.getMtTva()!=null) {
				int lignepasse=1;
				BigDecimal tvaTmp = ligneTva.getMtTva();
				BigDecimal ttcTmp = LibCalcul.arrondi(ligneTva.getMontantTotalTtcAvecRemise());
				BigDecimal htTmp = LibCalcul.arrondi(ligneTva.getMontantTotalHtAvecRemise());

				for (Object ligne : doc.getLignes()) {
					if(((TaLBonReception)ligne).getTaTLigne().getCodeTLigne().equals(SWTDocument.C_CODE_T_LIGNE_H)) {
						if(((TaLBonReception)ligne).getCodeTvaLDocument()!=null &&
								((TaLBonReception)ligne).getCodeTvaLDocument().equals(ligneTva.getCodeTva())){
							if (ligneTva.getMontantTotalHt().signum()==0) 
								tvaLigne = ((TaLBonReception)ligne).getMtHtLDocument().multiply(ligneTva.getTauxTva()).divide(new BigDecimal(100));
							else {
								if  (lignepasse>= ligneTva.getNbLigneDocument()) 
									tvaLigne = tvaTmp;
								else {
									if(doc.getTtc()==1){
										if(LibCalcul.arrondi(ligneTva.getMontantTotalTtcAvecRemise()).signum()<=0)
											tvaLigne=BigDecimal.valueOf(0);
										else
											tvaLigne = (ligneTva.getMtTva().multiply(((TaLBonReception)ligne).getMtTtcLDocument())).divide(LibCalcul.arrondi(ligneTva.getMontantTotalTtcAvecRemise()),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
									}
									else{
										if(LibCalcul.arrondi(ligneTva.getMontantTotalHtAvecRemise()).signum()<=0)
											tvaLigne =BigDecimal.valueOf(0);
										else
											tvaLigne = (ligneTva.getMtTva().multiply(((TaLBonReception)ligne).getMtHtLDocument())).divide(LibCalcul.arrondi(ligneTva.getMontantTotalHtAvecRemise()),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
									}
								}
							}
							tvaTmp =  tvaTmp.subtract(tvaLigne);
							totalTemp = totalTemp.add(((TaLBonReception)ligne).getMtHtLDocument());

							if(doc.getTxRemHtDocument()!=null && doc.getTxRemHtDocument().signum()>0) {
								if  (lignepasse>= ligneTva.getNbLigneDocument()) {
									((TaLBonReception)ligne).setMtHtLApresRemiseGlobaleDocument(htTmp);
									((TaLBonReception)ligne).setMtTtcLApresRemiseGlobaleDocument(ttcTmp);
								} else {
									((TaLBonReception)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLBonReception)ligne).getMtHtLDocument().subtract(((TaLBonReception)ligne).getMtHtLDocument().multiply(doc.getTxRemHtDocument()).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
									((TaLBonReception)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLBonReception)ligne).getMtTtcLDocument().subtract(((TaLBonReception)ligne).getMtTtcLDocument().multiply(doc.getTxRemHtDocument()).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
								}
//								ttcTmp =  ttcTmp.subtract(((TaLBonReception)ligne).getMtTtcLFacture());
//								htTmp =  htTmp.subtract(((TaLBonReception)ligne).getMtHtLFacture());
							} else {
								if(doc.getTtc()==1)
									if  (lignepasse>= ligneTva.getNbLigneDocument()) {
										((TaLBonReception)ligne).setMtHtLApresRemiseGlobaleDocument(htTmp);
									}else{
										((TaLBonReception)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLBonReception)ligne).getMtTtcLDocument().subtract(tvaLigne));
									}
								else
									if  (lignepasse>= ligneTva.getNbLigneDocument()) {
										((TaLBonReception)ligne).setMtTtcLApresRemiseGlobaleDocument(ttcTmp);
									}else {
										((TaLBonReception)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLBonReception)ligne).getMtHtLDocument().add(tvaLigne));
									}

							}
							ttcTmp =  ttcTmp.subtract(((TaLBonReception)ligne).getMtTtcLApresRemiseGlobaleDocument());
							if(((TaLBonReception)ligne).getMtHtLApresRemiseGlobaleDocument()!=null)
								htTmp =  htTmp.subtract(((TaLBonReception)ligne).getMtHtLApresRemiseGlobaleDocument());

							lignepasse++;
						}
					}
					doc.setRemHtDocument(totalTemp.multiply(doc.getTxRemHtDocument().divide(new BigDecimal(100))));						
	
//					setRemHtDocument(getRemHtDocument().add(totalTemp.multiply(txRemHtDocument.divide(new BigDecimal(100)))));						

				}
			}
		}
//		}

	}
	
	/**
	 * Lance la fonction de calcul du montant sur chacunes des lignes du document.
	 */
	public void calculMontantLigneDocument(TaBonReception doc) {
		for (Object ligne : doc.getLignes()) {
			((TaLBonReception)ligne).calculMontant();
		}
	}
	
	/**
	 * Calcul de la grille de TVA en fonction de lignes du document et du taux de remise HT global.
	 * Mise à jour de la propriété <code>lignesTVA</code>
	 * et mise à jour du montant de la TVA dans les lignes du document
	 */
	public void calculTvaTotal(TaBonReception doc) {
		calculMontantLigneDocument(doc);
		calculLignesTva(doc);
		dispatcherTva(doc);
	}
	
	/**
	 * Calcul de la grille de TVA en fonction de lignes du document et du taux de remise HT global.
	 * Mise à jour de la propriété <code>lignesTVA</code>
	 */
	public void calculLignesTva(TaBonReception doc) {

		Map<String,BigDecimal> montantTotalHt = new HashMap<String,BigDecimal>();
		Map<String,BigDecimal> montantTotalTtc = new HashMap<String,BigDecimal>();
		Map<String,BigDecimal> montantTotalHtAvecRemise = new HashMap<String,BigDecimal>();
		Map<String,BigDecimal> montantTotalTtcAvecRemise = new HashMap<String,BigDecimal>();
		Map<String,BigDecimal> mtTVA = new HashMap<String,BigDecimal>();
		Map<String,BigDecimal> tauxTVA = new HashMap<String,BigDecimal>();
		Map<String,Integer> nbLigne = new HashMap<String,Integer>();
		String codeTVA = null;
		
		/*
		 * calcul de la TVA different en fonction de la propriete TTC
		 */
		BigDecimal ttcLigne = null;
		BigDecimal htLigne = null;
		for (Object ligne : doc.getLignes()) {
			//en commentaire pour ne pas refaire les calculs pendants les editions, 
			//((TaLBonReception)ligne).calculMontant();
			codeTVA = ((TaLBonReception)ligne).getCodeTvaLDocument();
			if(codeTVA!=null && !codeTVA.equals("")) {
				ttcLigne = ((TaLBonReception)ligne).getMtTtcLDocument();
				htLigne = ((TaLBonReception)ligne).getMtHtLDocument();
				if(montantTotalHt.containsKey(codeTVA)) {
					montantTotalTtc.put(codeTVA,montantTotalTtc.get(codeTVA).add(ttcLigne));
					montantTotalHt.put(codeTVA,montantTotalHt.get(codeTVA).add(htLigne));
					montantTotalTtcAvecRemise.put(codeTVA,montantTotalTtcAvecRemise.get(codeTVA).add(ttcLigne));
					montantTotalHtAvecRemise.put(codeTVA,montantTotalHtAvecRemise.get(codeTVA).add(htLigne));
					nbLigne.put(codeTVA,nbLigne.get(codeTVA)+1);
				} else {
					montantTotalTtc.put(codeTVA,ttcLigne);
					montantTotalHt.put(codeTVA,htLigne);
					montantTotalTtcAvecRemise.put(codeTVA,ttcLigne);
					montantTotalHtAvecRemise.put(codeTVA,htLigne);
					tauxTVA.put(codeTVA,((TaLBonReception)ligne).getTauxTvaLDocument());
					nbLigne.put(codeTVA,1);
				}
			}
		}

		for (String codeTva : montantTotalTtc.keySet()) {
			//les 2 maps ont les meme cles
			BigDecimal mtTtcTotal = montantTotalTtc.get(codeTva);
			BigDecimal mtHtTotal = montantTotalHt.get(codeTva);
			BigDecimal tva =null;
			if(doc.getTxRemHtDocument()!=null && doc.getTxRemHtDocument().signum()>0) {
//				mtTtcTotal =LibCalcul.arrondi(mtTtcTotal.subtract(     mtTtcTotal.multiply(   txRemHtDocument.divide(new BigDecimal(100))  )       ));
//				mtHtTotal = LibCalcul.arrondi(mtHtTotal.subtract(    mtHtTotal.multiply( (txRemHtDocument.divide(new BigDecimal(100))))     ) ) ;
				BigDecimal valeurInterTTC=mtTtcTotal.multiply( doc.getTxRemHtDocument().divide(new BigDecimal(100)),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
				mtTtcTotal =LibCalcul.arrondi(mtTtcTotal.subtract(valeurInterTTC )) ;
				BigDecimal valeurInterHT=mtHtTotal.multiply( doc.getTxRemHtDocument().divide(new BigDecimal(100)),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
				mtHtTotal = LibCalcul.arrondi(mtHtTotal.subtract( valeurInterHT )) ;
				montantTotalTtcAvecRemise.put(codeTva, mtTtcTotal);
				montantTotalHtAvecRemise.put(codeTva, mtHtTotal);
			} 

			if (doc.getTtc()==1) {
				tva=mtTtcTotal.subtract((mtTtcTotal.multiply(BigDecimal.valueOf(100))) .divide((BigDecimal.valueOf(100).add(tauxTVA.get(codeTva))) ,MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)     ) ;
				mtTVA.put(codeTva, tva);
				montantTotalHtAvecRemise.put(codeTva, mtTtcTotal.subtract(tva));
			} else {
				tva=mtHtTotal.multiply(   (tauxTVA.get(codeTva).divide(new BigDecimal(100)))) ;
				mtTVA.put(codeTva, tva );
				montantTotalTtcAvecRemise.put(codeTva, mtHtTotal.add(tva));
			}
		}

		doc.getLignesTVA().clear();
		for (String codeTva : mtTVA.keySet()) {
			LigneTva ligneTva = new LigneTva();
			ligneTva.setCodeTva(codeTva);
			ligneTva.setTauxTva(tauxTVA.get(codeTva));
			ligneTva.setMtTva(mtTVA.get(codeTva));
			ligneTva.setMontantTotalHt(montantTotalHt.get(codeTva));
			ligneTva.setMontantTotalTtc(montantTotalTtc.get(codeTva));
			ligneTva.setMontantTotalHtAvecRemise(montantTotalHtAvecRemise.get(codeTva));
			ligneTva.setMontantTotalTtcAvecRemise(montantTotalTtcAvecRemise.get(codeTva));
			ligneTva.setLibelle(taTvaDAO.findByCode(codeTva).getLibelleTva());
			ligneTva.setNbLigneDocument(nbLigne.get(codeTva));
			doc.getLignesTVA().add(ligneTva);
		}
		
		//dispatcherTva();
	}
	
	public Date calculDateEcheanceAbstract(TaBonReception doc, Integer report, Integer finDeMois){
		return calculDateEcheance(doc,report,finDeMois);
	}
	
	public Date calculDateEcheance(TaBonReception doc, Integer report, Integer finDeMois) {
//		TaTCPaiementDAO taTCPaiementDAO = new TaTCPaiementDAO();
		TaTCPaiement typeCP = taTCPaiementDAO.findByCode(TaTCPaiement.C_CODE_TYPE_DEVIS);
		TaCPaiement conditionDoc = null;
		TaCPaiement conditionTiers = null;
		TaCPaiement conditionSaisie = null;
		
		if(typeCP!=null) {
			conditionDoc = typeCP.getTaCPaiement();
			List<TaCPaiement> liste=taCPaiementDAO.rechercheParType(typeCP.getCodeTCPaiement());
			if(liste!=null && !liste.isEmpty())conditionDoc=liste.get(0);
		}

		if(doc.getTaTiers()!=null) conditionTiers = doc.getTaTiers().getTaCPaiement();

		if(report!=null || finDeMois!=null) { 
			conditionSaisie = new TaCPaiement();
			if(report!=null)
				conditionSaisie.setReportCPaiement(report);
			if(finDeMois!=null)
				conditionSaisie.setFinMoisCPaiement(finDeMois);
		}
		
		//on applique toute les conditions par ordre décroissant de priorité, la derniere valide est conservée
		Date nouvelleDate = doc.getDateDocument();
		if(conditionDoc!=null) {
			nouvelleDate = conditionDoc.calculeNouvelleDate(doc.getDateDocument());
		}
		if(conditionTiers!=null) {
			nouvelleDate = conditionTiers.calculeNouvelleDate(doc.getDateDocument());
		}
		if(conditionSaisie!=null) {
			nouvelleDate = conditionSaisie.calculeNouvelleDate(doc.getDateDocument());
		}
		
//#Bon_de_reception	
//		doc.setDateEchDocument(nouvelleDate);
		
		return nouvelleDate;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaBonReception transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaBonReception transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	@Override
	public void remove(TaBonReception persistentInstance, boolean recharger) throws RemoveException {
		// TODO Auto-generated method stub
		try {
			if(taAutorisationDossierService.autoriseMenu(IModulesProgramme.ID_MODULE_ABONNEMENT, sessionInfo.getUtilisateur())) {
				//taLigneALigneEcheanceService.removeAllByIdDocumentAndTypeDoc(persistentInstance.getIdDocument(), *.TYPE_DOC);
			}
			if(recharger)persistentInstance=findByIDFetch(persistentInstance.getIdDocument());
			List<ILigneDocumentTiers> listeLien = recupListeLienLigneALigne(persistentInstance);

			dao.remove(persistentInstance);
			
//			for (ILigneDocumentTiers l : persistentInstance.getLignes()) {
//				if(l.getTaLot()!=null && (!l.getTaLot().getVirtuel() || l.getTaLot().getVirtuelUnique())) {
//					taLotService.remove(l.getTaLot());
//				}
//			}
			
			mergeEntityLieParLigneALigne(listeLien);
			this.annuleCode(persistentInstance.getCodeDocument());
			for (TaLBonReception ligne : persistentInstance.getLignes()) {
				if(ligne.getTaLot()!=null)
					taLotService.annuleCode(ligne.getTaLot().getNumLot());
			}
//			taLotService.suppression_lot_non_utilise();
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
	public void remove(TaBonReception persistentInstance) throws RemoveException {
		remove(persistentInstance, true);
	}
	
	public TaBonReception merge(TaBonReception detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaBonReception merge(TaBonReception detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);
		List<ILigneDocumentTiers> listeLien =null;
		TaBonReception objInitial = detachedInstance;
		try {
			if(detachedInstance.getIdDocument()!=0)
				objInitial=findByIDFetch(detachedInstance.getIdDocument());
		} catch (FinderException e) {
			// TODO Auto-generated catch block
		}
		
		for (TaLBonReception l : detachedInstance.getLignes()) {
			//si lot non virtuel ou virtuel mais unique, alors on enregistre le lot manuellemnt puisqu'il n'y a plus de cascade
			if(l.getTaLot()!=null && (!l.getTaLot().getVirtuel() || l.getTaLot().getVirtuelUnique()||l.getTaLot().getIdLot()==0)) {
				try {
				TaLot lot=taLotService.merge(l.getTaLot());
				lot=taLotService.findById(lot.getIdLot());

					l.setTaLot(lot);
					if(l.getTaMouvementStock()!=null)l.getTaMouvementStock().setTaLot(lot);
				} catch (FinderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}		

		
		modifEtatLigne(detachedInstance);		
		TaEtat etat=changeEtatDocument(detachedInstance);
		if(etat!=null)detachedInstance.addREtat(etat);

		//récupération des liens avec le document d'origine en cours si ces liens sont les souOrces du document d'origine
		listeLien = recupListeLienLigneALigne(objInitial);
		//récupération des liens avec le document modifié en cours si ces liens sont les sources du document modifié 
		listeLien =recupListeLienLigneALigne(detachedInstance,listeLien);
		detachedInstance=dao.merge(detachedInstance);		
		mergeEntityLieParLigneALigne(listeLien);
		
		

	
		this.annuleCode(detachedInstance.getCodeDocument());
		for (TaLBonReception ligne : detachedInstance.getLignes()) {
			if(ligne.getTaLot()!=null)
				taLotService.annuleCode(ligne.getTaLot().getNumLot());
		}
		return detachedInstance;
	}
	
	

	
//	//modifier l'état des lignes et du document en fonction des liens ligne à ligne
//	public void modifEtatLigne(IDocumentTiers detachedInstance) {	
//			for (ILigneDocumentTiers ligne : detachedInstance.getLignesGeneral()) {
//				// traiter l'état de la ligne
//							BigDecimal totalTransformee=BigDecimal.ZERO;
//							String ligneBefore="";
//							TaEtat etatLigneOrg = null;
//							TaEtat etatLigneOrgBefore = null;
//
//							for (TaLigneALigne l : ligne.getTaLigneALignes()) {
//								if(l.getIdLigneSrc().equals(ligne.getIdLDocument())) {
//									totalTransformee=totalTransformee.add(l.getQteRecue());
//									}
//							}
//
//							if(totalTransformee.compareTo(ligne.getQteLDocument())>=0)
//								etatLigneOrg=taEtatService.documentTermine(null);
//							else if(totalTransformee.signum()!=0) etatLigneOrg=taEtatService.documentPartiellementTransforme(null);
//
//							if(ligne.getTaREtatLigneDocument()!=null)etatLigneOrgBefore=ligne.getTaREtatLigneDocument().getTaEtat();
//							if(etatLigneOrgBefore!=null) {
//								if(etatLigneOrgBefore.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE) || etatLigneOrgBefore.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
//									etatLigneOrg=taEtatService.documentAbandonne(null);
//								}
//								if(etatLigneOrgBefore.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME) || etatLigneOrgBefore.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)) {
//									etatLigneOrg=taEtatService.documentEncours(null);
//								}
//							}
//							
//							if(etatLigneOrg==null) {//plus rien de transforme
//								if(etatLigneOrgBefore!=null) {
//
//									if(etatLigneOrgBefore.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE) || etatLigneOrgBefore.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
//										etatLigneOrg=taEtatService.documentAbandonne(null);
//									}
//									if(etatLigneOrgBefore.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME) || etatLigneOrgBefore.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)) {
//										etatLigneOrg=taEtatService.documentEncours(null);
//									}		
//								}
//							}else //si tout transforme
//								if(etatLigneOrg.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME)){
//								if(etatLigneOrgBefore.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE) || etatLigneOrgBefore.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
//									etatLigneOrg=taEtatService.documentTotalementTransforme(null);
//								}
//							}
//							if(etatLigneOrg!=null) {
//								ligne.addREtatLigneDoc(etatLigneOrg);
//							}
//						}
//	}
	
	public TaBonReception mergeAndFindById(TaBonReception detachedInstance, String validationContext) {
		TaBonReception br = merge(detachedInstance,validationContext);
		try {
			br = findByIDFetch(br.getIdDocument()); //pour etre sur que les valeur initialisé par les triggers "qui_cree,..." soit bien affecté avant une éventuelle nouvelle modification du document
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return br;
	}

	public TaBonReception findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaBonReception findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaBonReception> selectAll() {
		List<TaBonReception> l=dao.selectAll();
		for (TaBonReception o : l) {
			o=(TaBonReception) recupSetREtat(o);
			o=(TaBonReception) recupSetHistREtat(o);
			o=(TaBonReception) recupSetLigneALigne(o);
			o=(TaBonReception) recupSetRDocument(o);
			o=(TaBonReception) recupSetREtatLigneDocuments(o);
			o=(TaBonReception) recupSetHistREtatLigneDocuments(o);
		}
		return l;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaBonReceptionDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaBonReceptionDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaBonReception> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaBonReceptionDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaBonReceptionDTO entityToDTO(TaBonReception entity) {
//		TaBonReceptionDTO dto = new TaBonReceptionDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaBonReceptionMapper mapper = new TaBonReceptionMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaBonReceptionDTO> listEntityToListDTO(List<TaBonReception> entity) {
		List<TaBonReceptionDTO> l = new ArrayList<TaBonReceptionDTO>();

		for (TaBonReception taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaBonReceptionDTO> selectAllDTO() {
		System.out.println("List of TaBonReceptionDTO EJB :");
		ArrayList<TaBonReceptionDTO> liste = new ArrayList<TaBonReceptionDTO>();

		List<TaBonReception> projects = selectAll();
		for(TaBonReception project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaBonReceptionDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaBonReceptionDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaBonReceptionDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaBonReceptionDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaBonReceptionDTO dto, String validationContext) throws EJBException {
		try {
			TaBonReceptionMapper mapper = new TaBonReceptionMapper();
			TaBonReception entity = null;
			if(dto.getId()!=null) {
				entity = dao.findById(dto.getId());
				if(dto.getVersionObj()!=entity.getVersionObj()) {
					throw new OptimisticLockException(entity,
							"L'objet à été modifié depuis le dernier accés. Client ID : "+dto.getId()+" - Client Version objet : "+dto.getVersionObj()+" -Serveur Version Objet : "+entity.getVersionObj());
				} else {
					 entity = mapper.mapDtoToEntity(dto,entity);
				}
			}
			
			//dao.merge(entity);
			dao.detach(entity); //pour passer les controles
			enregistrerMerge(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			//throw new CreateException(e.getMessage());
			throw new EJBException(e.getMessage());
		}
	}
	
	public void persistDTO(TaBonReceptionDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaBonReceptionDTO dto, String validationContext) throws CreateException {
		try {
			TaBonReceptionMapper mapper = new TaBonReceptionMapper();
			TaBonReception entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaBonReceptionDTO dto) throws RemoveException {
		try {
			TaBonReceptionMapper mapper = new TaBonReceptionMapper();
			TaBonReception entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaBonReception refresh(TaBonReception persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaBonReception value, String validationContext) /*throws ExceptLgr*/ {
		try {
			if(validationContext==null) validationContext="";
			validateAll(value,validationContext,false); //ancienne validation, extraction de l'annotation et appel
			//dao.validate(value); //validation automatique via la JSR bean validation
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateEntityPropertyValidationContext")
	public void validateEntityProperty(TaBonReception value, String propertyName, String validationContext) {
		try {
			if(validationContext==null) validationContext="";
			validate(value, propertyName, validationContext);
			//dao.validateField(value,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

//	@Override
//	@WebMethod(operationName = "validateDTOValidationContext")
//	public void validateDTO(TaBonReceptionDTO dto, String validationContext) {
//		try {
//			TaBonReceptionMapper mapper = new TaBonReceptionMapper();
//			TaBonReception entity = mapper.mapDtoToEntity(dto,null);
//			validateEntity(entity,validationContext);
//			
//			//validation automatique via la JSR bean validation
////			BeanValidator<TaBonReceptionDTO> validator = new BeanValidator<TaBonReceptionDTO>(TaBonReceptionDTO.class);
////			validator.validate(dto);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new EJBException(e.getMessage());
//		}
//	}
	
	@Override
	@WebMethod(operationName = "validateDTOValidationContext")
	public void validateDTO(TaBonReceptionDTO dto, String validationContext) {
		try {
			validateDTOAll(dto, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaBonReceptionDTO dto, String propertyName, String validationContext)  throws BusinessValidationException {
		try {			
			if(validationContext==null) validationContext="";
			validateDTO(dto, propertyName, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
//			throw new EJBException(e.getMessage());
			throw new BusinessValidationException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaBonReceptionDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaBonReceptionDTO dto, String propertyName)  throws BusinessValidationException {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaBonReception value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaBonReception value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

	@Override
	public List<TaBonReception> rechercheDocument(Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return dao.rechercheDocument(dateDeb, dateFin);
	}

	@Override
	public List<Object[]> rechercheDocument(Date dateDeb, Date dateFin,
			Boolean light) {
		// TODO Auto-generated method stub
		return dao.rechercheDocument(dateDeb, dateFin, light);
	}

	@Override
	public List<TaBonReception> rechercheDocument(String codeDeb, String codeFin) {
		// TODO Auto-generated method stub
		return dao.rechercheDocument(codeDeb, codeFin);
	}

	@Override
	public List<TaBonReception> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers) {
		// TODO Auto-generated method stub
		return dao.rechercheDocument(dateDeb, dateFin, codeTiers);
	}

	@Override
	public List<Object[]> rechercheDocumentLight(Date dateDeb, Date dateFin,
			String codeTiers) {
		// TODO Auto-generated method stub
		return dao.rechercheDocumentLight(dateDeb, dateFin, codeTiers);
	}

	@Override
	public List<Object[]> rechercheDocumentLight(String codeDoc,
			String codeTiers) {
		// TODO Auto-generated method stub
		return dao.rechercheDocumentLight(codeDoc, codeTiers);
	}

	@Override
	public List<TaBonReception> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers) {
		// TODO Auto-generated method stub
		return dao.rechercheDocument(codeDeb, codeFin, codeTiers);
	}

	@Override
	public List<TaBonReception> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers, Boolean export) {
		// TODO Auto-generated method stub
		return dao.rechercheDocument(dateDeb, dateFin, codeTiers, export);
	}

	@Override
	public List<TaBonReception> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers, Boolean export) {
		// TODO Auto-generated method stub
		return dao.rechercheDocument(codeDeb, codeFin, codeTiers, export);
	}

	// Dima - Début
	public List<TaLBonReception> bonRecepFindByCodeArticle(String codeArticle){
		return dao.bonRecepFindByCodeArticle(codeArticle);
	}
	// Dima -  Fin

	@Override
	public List<TaLBonReception> bonRecepFindByLotParDate(String numLot,
			Date dateDeb, Date dateFin) {
		return dao.bonRecepFindByLotParDate(numLot,dateDeb,dateFin);
	}

	@Override
	public List<TaLBonReception> bonRecepFindByCodeArticleParDate(
			String codeArticle, Date dateDeb, Date dateFin) {
		return dao.bonRecepFindByCodeArticleParDate(codeArticle,dateDeb,dateFin);
	}

	@Override
	public List<TaBonReception> selectAll(IDocumentTiers taDocument,
			Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaBonReception findByIdDocument(int id) throws FinderException {
		return findById(id);
	}

	@Override
	public List<TaLBonReceptionDTO> bonRecepFindByCodeFournisseurParDate(String codeFournisseur, Date dateDeb,
			Date dateFin) {
		// TODO Auto-generated method stub
		return dao.bonRecepFindByCodeFournisseurParDate(codeFournisseur, dateDeb, dateFin);
	}

	@Override
	public List<TaTiersDTO> findTiersDTOByCodeArticleAndDate(String codeArticle, Date debut, Date fin) {
		// TODO Auto-generated method stub
		return dao.findTiersDTOByCodeArticleAndDate(codeArticle, debut, fin);
	}

	@Override
	public String generePDF(int idDoc, Map<String,Object> mapParametreEdition, List<String> listResourcesPath, String theme) {
		return generePDF(idDoc,editionDefaut,mapParametreEdition, null, null);
	}
	
	public String generePDF(int idDoc, String modeleEdition, Map<String,Object> mapParametreEdition, List<String> listResourcesPath, String theme) {
		try {
			SchemaResolver sr = new SchemaResolver();
			String localPath = bdgProperties.osTempDirDossier(sr.resolveCurrentTenantIdentifier())+"/"+bdgProperties.tmpFileName("bonReception.pdf");
			System.out.println(localPath);
			System.out.println(bdgProperties.urlDemoHttps()+modeleEdition);
			
			Map<String,Object> mapEdition = new HashMap<String,Object>();

			TaBonReception doc = findById(idDoc);
			doc.calculeTvaEtTotaux();
			

			mapEdition.put("document",doc );

			mapEdition.put("taInfoEntreprise", entrepriseService.findInstance());

			mapEdition.put("lignes", doc.getLignes());

			
			if(mapParametreEdition == null) {
				mapParametreEdition = new HashMap<String,Object>();
			}
			List<TaPreferences> liste= taPreferencesService.findByGroupe("BonReception");
			if(!mapParametreEdition.containsKey("preferences")) mapParametreEdition.put("preferences", liste);
			if(!mapParametreEdition.containsKey("gestionLot")) mapParametreEdition.put("gestionLot", false);
			if(!mapParametreEdition.containsKey("mode")) mapParametreEdition.put("mode", "");
			if(!mapParametreEdition.containsKey("Theme")) mapParametreEdition.put("Theme", "defaultTheme");
			if(!mapParametreEdition.containsKey("Librairie")) mapParametreEdition.put("Librairie", "bdgFactTheme1");
			mapEdition.put("param", mapParametreEdition);

			//sessionMap.put("edition", mapEdition);
			if(theme == null) {
				theme = mapParametreEdition.get("Theme").toString();
						
			}
			
			////////////////////////////////////////////////////////////////////////////////////////
			//Test génération de fichier PDF
			
			HashMap<String,Object> hm = new HashMap<>();

			hm.put("edition", mapEdition);
			
			
			if(listResourcesPath == null) {
				listResourcesPath = new ArrayList<String>();
				listResourcesPath.add(EditionProgrammeDefaut.LIBRAIRIE_EDITION_DEFAUT_BON_RECEPTION);
			}else if(listResourcesPath.isEmpty()) {
					listResourcesPath = new ArrayList<String>();
					listResourcesPath.add(EditionProgrammeDefaut.LIBRAIRIE_EDITION_DEFAUT_BON_RECEPTION);
			}
			
			BirtUtil.setAppContextEdition(hm);
			BirtUtil.startReportEngine();
			
			BirtUtil.renderReportToFile(
					//bdgProperties.urlDemoHttps()+modeleEdition, //"https://dev.demo.promethee.biz:8443/reports/documents/facture/FicheFacture.rptdesign",
					modeleEdition,
					localPath, 
					new HashMap<>(), 
					BirtUtil.PDF_FORMAT,
					listResourcesPath,
					theme);
			 
			 return localPath;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public TaBonReception findAvecResultatConformites(int idf) {
		// TODO Auto-generated method stub
		return dao.findAvecResultatConformites(idf);
	}

	@Override
	public List<TaBonReception> rechercheDocumentVerrouille(Date dateDeb, Date dateFin, String codeTiers,
			Boolean verrouille) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaBonReception> rechercheDocumentVerrouille(String codeDeb, String codeFin, String codeTiers,
			Boolean verrouille) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaBonReception> rechercheDocument(String codeTiers, Date dateExport, Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String generePDF(int idDoc, Map<String, Object> mapParametreEdition, List<String> listResourcesPath,
			String theme, TaActionEdition action) {
		// TODO Auto-generated method stub
		return null;
	}

	public TaEtat rechercheEtatInitialDocument() {
		try {
			return taEtatService.documentEncours(null);
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public TaEtat etatLigneInsertion(TaBonReception masterEntity) {
		// TODO Auto-generated method stub
		return super.etatLigneInsertion(masterEntity);
	}

	@Override
	public TaBonReception findDocByLDoc(ILigneDocumentTiers lDoc) {
		// TODO Auto-generated method stub
		TaBonReception o= (TaBonReception) dao.findDocByLDoc(lDoc);
		o=(TaBonReception) recupSetREtat(o);
		o=(TaBonReception) recupSetHistREtat(o);
		o=(TaBonReception) recupSetLigneALigne(o);
		o=(TaBonReception) recupSetRDocument(o);
		o=(TaBonReception) recupSetREtatLigneDocuments(o);
		o=(TaBonReception) recupSetHistREtatLigneDocuments(o);
//		recupSetRReglementLiaison(o);
		return o;
	}

	@Override
	public TaBonReception mergeEtat(IDocumentTiers detachedInstance) {
		// TODO Auto-generated method stub
		modifEtatLigne(detachedInstance);
		TaEtat etat=changeEtatDocument(detachedInstance);
		((TaBonReception) detachedInstance).addREtat(etat);
		
		detachedInstance=dao.merge((TaBonReception) detachedInstance);	
		

		return (TaBonReception) detachedInstance;
	}

	@Override
	public int findDocByLDocDTO(ILigneDocumentTiers lDoc) {
		return dao.findDocByLDocDTO(lDoc);
	}

	@Override
	public TaBonReception findByCodeFetch(String code) throws FinderException {
		// TODO Auto-generated method stub
		TaBonReception o = (TaBonReception) dao.findByCodeFetch(code);
		if(o!=null) {
		recupSetREtat(o);
		recupSetHistREtat(o);
		recupSetLigneALigne(o);
		recupSetRDocument(o);
		recupSetREtatLigneDocuments(o);
		recupSetHistREtatLigneDocuments(o);
//		recupSetRReglementLiaison(o);
		}
		return o;
	}

	@Override
	public TaBonReception findByIDFetch(int id) throws FinderException {
		// TODO Auto-generated method stub
		TaBonReception o = (TaBonReception) dao.findByIdFetch(id);
		if(o!=null) {
		recupSetREtat(o);
		recupSetHistREtat(o);
		recupSetLigneALigne(o);
		recupSetRDocument(o);
		recupSetREtatLigneDocuments(o);
		recupSetHistREtatLigneDocuments(o);
//		recupSetRReglementLiaison(o);
		}
		return o;
	}



}
