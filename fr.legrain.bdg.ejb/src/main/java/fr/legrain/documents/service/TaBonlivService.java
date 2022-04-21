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

import fr.legrain.article.dao.ITvaDAO;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBonlivServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaDevisServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLigneALigneEcheanceServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaAutorisationsDossierServiceRemote;
import fr.legrain.bdg.edition.osgi.EditionProgrammeDefaut;
import fr.legrain.bdg.edition.service.remote.ITaActionEditionServiceRemote;
import fr.legrain.bdg.edition.service.remote.ITaEditionServiceRemote;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.model.mapping.mapper.TaBonlivMapper;
import fr.legrain.birt.BirtUtil;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.data.AbstractDocumentService;
import fr.legrain.document.dashboard.dto.TaArticlesParTiersDTO;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IDocumentTiersDTO;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaBonlivDTO;
import fr.legrain.document.model.LigneTva;
import fr.legrain.document.model.SWTDocument;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaBonReception;
import fr.legrain.document.model.TaBoncdeAchat;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaLBonliv;
import fr.legrain.document.model.TaLFacture;
import fr.legrain.document.model.TaMiseADisposition;
import fr.legrain.document.model.TaLPreparation;
import fr.legrain.document.model.TaLigneALigne;
import fr.legrain.document.model.TaPreparation;
import fr.legrain.document.model.TaRDocument;
import fr.legrain.document.model.TaRReglementLiaison;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.documents.dao.IBonlivDAO;
import fr.legrain.documents.dao.IReglementDAO;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.droits.model.IModulesProgramme;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.edition.model.TaActionEdition;
import fr.legrain.edition.model.TaEdition;
import fr.legrain.hibernate.multitenant.SchemaResolver;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.IHMEtat;
import fr.legrain.lib.data.LibCalcul;
import fr.legrain.tiers.dao.ICPaiementDAO;
import fr.legrain.tiers.dao.ITCPaiementDAO;
import fr.legrain.tiers.model.TaCPaiement;
import fr.legrain.tiers.model.TaTCPaiement;
import fr.legrain.tiers.model.TaTiers;

/**
 * Session Bean implementation class TaBonlivBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaBonlivService extends AbstractDocumentService<TaBonliv, TaBonlivDTO> implements ITaBonlivServiceRemote {

	static Logger logger = Logger.getLogger(TaBonlivService.class);

	@Inject private IBonlivDAO dao;
	@Inject private ITvaDAO taTvaDAO;
	@Inject private ITCPaiementDAO taTCPaiementDAO;
	@Inject private ICPaiementDAO taCPaiementDAO;
	@Inject private	SessionInfo sessionInfo;
	@EJB private ITaGenCodeExServiceRemote gencode;
	
	@EJB private ITaActionEditionServiceRemote taActionEditionService;
	@EJB private ITaEditionServiceRemote taEditionService;
	@Inject private IReglementDAO daoReglement;
	
	@EJB private ITaLEcheanceServiceRemote taLEcheanceService;
	@EJB private ITaLigneALigneEcheanceServiceRemote taLigneALigneEcheanceService;
	@EJB private ITaAutorisationsDossierServiceRemote taAutorisationDossierService;
	
	/**
	 * Default constructor. 
	 */
	public TaBonlivService() {
		super(TaBonliv.class,TaBonlivDTO.class);
		editionDefaut = EditionProgrammeDefaut.EDITION_DEFAUT_BONLIV_FICHIER;
	}
	
	public List<TaBonlivDTO> findAllLight() {
		return dao.findAllLight();
	}
	
	public List<TaBonlivDTO> findByCodeLight(String code) {
		return dao.findByCodeLight(code);
	}

	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public String genereCode( Map<String, String> params) {
		//return dao.genereCode();
		try {
			return gencode.genereCodeJPA(TaBonliv.class.getSimpleName(),params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NOUVEAU CODE";
	}
	
	public void annuleCode(String code) {
		try {
			
			gencode.annulerCodeGenere(gencode.findByCode(TaBonliv.class.getSimpleName()),code);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void verrouilleCode(String code) {
		try {
			gencode.rentreCodeGenere(gencode.findByCode(TaBonliv.class.getSimpleName()),code, sessionInfo.getSessionID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Date calculDateEcheance(TaBonliv doc, Integer report, Integer finDeMois,TaTPaiement taTPaiement) {
		TaTCPaiement typeCP = taTCPaiementDAO.findByCode(TaTCPaiement.C_CODE_TYPE_BON_LIVRAISON);
		TaCPaiement conditionDoc = null;
		TaCPaiement conditionTiers = null;
		TaCPaiement conditionSaisie = null;
		TaCPaiement conditionTPaiement = null;
		
		if(typeCP!=null) conditionDoc = typeCP.getTaCPaiement();
		List<TaCPaiement> liste=taCPaiementDAO.rechercheParType(typeCP.getCodeTCPaiement());
		if(liste!=null && !liste.isEmpty())conditionDoc=liste.get(0);
		if(doc.getTaTiers()!=null) conditionTiers = doc.getTaTiers().getTaCPaiement();
		if(taTPaiement!=null){
			conditionTPaiement=new TaCPaiement();
			conditionTPaiement.setReportCPaiement(taTPaiement.getReportTPaiement());
			conditionTPaiement.setFinMoisCPaiement(taTPaiement.getFinMoisTPaiement());
		}
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
		if(conditionTPaiement!=null){
			nouvelleDate = conditionTPaiement.calculeNouvelleDate(doc.getDateDocument());
		}
		if(conditionTiers!=null) {
			nouvelleDate = conditionTiers.calculeNouvelleDate(doc.getDateDocument());
		}
		if(conditionSaisie!=null) {
			nouvelleDate = conditionSaisie.calculeNouvelleDate(doc.getDateDocument());
		}
//		doc.setDateEchDocument(nouvelleDate);
		doc.setDateLivDocument(nouvelleDate);
		return nouvelleDate;
	}
	
	public void calculeTvaEtTotaux(TaBonliv doc){
		calculTvaTotal(doc);
		calculTotaux(doc);
	}
	
	/**
	 * Calcul des totaux de la facture
	 */
	public void calculTotaux(TaBonliv doc) {
		
//			    MT_TVA Numeric(15,2),
			doc.setMtHtCalc(new BigDecimal(0));
			doc.setNetHtCalc(new BigDecimal(0));
			doc.setMtTtcCalc(new BigDecimal(0));
			doc.setMtTtcAvantRemiseGlobaleCalc(new BigDecimal(0));
			for (Object ligne : doc.getLignes()) {
				if(((TaLBonliv)ligne).getTaTLigne().getCodeTLigne().equals(SWTDocument.C_CODE_T_LIGNE_H)) {
					if(((TaLBonliv)ligne).getMtHtLApresRemiseGlobaleDocument()!=null)
						doc.setNetHtCalc(doc.getNetHtCalc().add(((TaLBonliv)ligne).getMtHtLApresRemiseGlobaleDocument()));
					if(((TaLBonliv)ligne).getMtTtcLApresRemiseGlobaleDocument()!=null)
						doc.setMtTtcCalc(doc.getMtTtcCalc().add(((TaLBonliv)ligne).getMtTtcLApresRemiseGlobaleDocument()));
					if(((TaLBonliv)ligne).getMtHtLDocument()!=null)
						doc.setMtHtCalc(doc.getMtHtCalc().add(((TaLBonliv)ligne).getMtHtLDocument()));
					if(((TaLBonliv)ligne).getMtTtcLDocument()!=null)
						doc.setMtTtcAvantRemiseGlobaleCalc(doc.getMtTtcAvantRemiseGlobaleCalc().add(((TaLBonliv)ligne).getMtTtcLDocument()));
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
			
			doc.setNetAPayer(doc.getNetTtcCalc());
			
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
	public void dispatcherTva(TaBonliv doc) {

		BigDecimal tvaLigne = new BigDecimal(0);
		BigDecimal totalTemp = new BigDecimal(0);

		for (Object ligne : doc.getLignes()) {
			if(((TaLBonliv)ligne).getMtHtLDocument()!=null)
				totalTemp = totalTemp.add(((TaLBonliv)ligne).getMtHtLDocument());
		}
		if(totalTemp!=null && doc.getTxRemHtDocument()!=null)
			doc.setRemHtDocument(totalTemp.multiply(doc.getTxRemHtDocument().divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_HALF_UP));	
		
		for (TaLBonliv ligne : doc.getLignes()) {
			if(doc.getTxRemHtDocument()!=null && doc.getTxRemHtDocument().signum()>0 && ligne.getMtHtLDocument()!=null  && ligne.getMtTtcLDocument()!=null) {
				if(doc.getTtc()==1){
					((TaLBonliv)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLBonliv)ligne).getMtTtcLDocument().subtract(((TaLBonliv)ligne).getMtTtcLDocument()
							.multiply(doc.getTxRemHtDocument()).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
					((TaLBonliv)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLBonliv)ligne).getMtTtcLApresRemiseGlobaleDocument());
					
				}else{
					((TaLBonliv)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLBonliv)ligne).getMtHtLDocument().subtract(((TaLBonliv)ligne).getMtHtLDocument()
							.multiply(doc.getTxRemHtDocument()).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
					((TaLBonliv)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLBonliv)ligne).getMtHtLApresRemiseGlobaleDocument());	
				}
			}
		}
				
		for (LigneTva ligneTva : doc.getLignesTVA()) {

			if (ligneTva.getMtTva()!=null) {
				int lignepasse=1;
				BigDecimal tvaTmp = ligneTva.getMtTva();
				BigDecimal ttcTmp = LibCalcul.arrondi(ligneTva.getMontantTotalTtcAvecRemise());
				BigDecimal htTmp = LibCalcul.arrondi(ligneTva.getMontantTotalHtAvecRemise());

				for (Object ligne :doc.getLignes()) {
					if(((TaLBonliv)ligne).getTaTLigne().getCodeTLigne().equals(SWTDocument.C_CODE_T_LIGNE_H)) {
						if(((TaLBonliv)ligne).getCodeTvaLDocument()!=null&&
								((TaLBonliv)ligne).getCodeTvaLDocument().equals(ligneTva.getCodeTva())){
							if (ligneTva.getMontantTotalHt().signum()==0) 
								tvaLigne = ((TaLBonliv)ligne).getMtHtLDocument().multiply(ligneTva.getTauxTva()).divide(new BigDecimal(100));
							else {
								if  (lignepasse>= ligneTva.getNbLigneDocument()) 
									tvaLigne = tvaTmp;
								else {
									if(doc.getTtc()==1){
										if(LibCalcul.arrondi(ligneTva.getMontantTotalTtcAvecRemise()).signum()<=0)
											tvaLigne=BigDecimal.valueOf(0);
										else
											tvaLigne = (ligneTva.getMtTva().multiply(((TaLBonliv)ligne).getMtTtcLDocument())).divide(LibCalcul.arrondi(ligneTva.getMontantTotalTtcAvecRemise()),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
									}
									else{
										if(LibCalcul.arrondi(ligneTva.getMontantTotalHtAvecRemise()).signum()<=0)
											tvaLigne =BigDecimal.valueOf(0);
										else
											tvaLigne = (ligneTva.getMtTva().multiply(((TaLBonliv)ligne).getMtHtLDocument())).divide(LibCalcul.arrondi(ligneTva.getMontantTotalHtAvecRemise()),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
									}
								}
							}
							tvaTmp =  tvaTmp.subtract(tvaLigne);
							totalTemp = totalTemp.add(((TaLBonliv)ligne).getMtHtLDocument());

							if(doc.getTxRemHtDocument()!=null && doc.getTxRemHtDocument().signum()>0) {
								if  (lignepasse>= ligneTva.getNbLigneDocument()) {
									((TaLBonliv)ligne).setMtHtLApresRemiseGlobaleDocument(htTmp);
									((TaLBonliv)ligne).setMtTtcLApresRemiseGlobaleDocument(ttcTmp);
								} else {
									((TaLBonliv)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLBonliv)ligne).getMtHtLDocument().subtract(((TaLBonliv)ligne).getMtHtLDocument().multiply(doc.getTxRemHtDocument()).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
									((TaLBonliv)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLBonliv)ligne).getMtTtcLDocument().subtract(((TaLBonliv)ligne).getMtTtcLDocument().multiply(doc.getTxRemHtDocument()).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
								}
//								ttcTmp =  ttcTmp.subtract(((TaLBonliv)ligne).getMtTtcLFacture());
//								htTmp =  htTmp.subtract(((TaLBonliv)ligne).getMtHtLFacture());
							} else {
								if(doc.getTtc()==1)
									if  (lignepasse>= ligneTva.getNbLigneDocument()) {
										((TaLBonliv)ligne).setMtHtLApresRemiseGlobaleDocument(htTmp);
									}else{
										((TaLBonliv)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLBonliv)ligne).getMtTtcLDocument().subtract(tvaLigne));
									}
								else
									if  (lignepasse>= ligneTva.getNbLigneDocument()) {
										((TaLBonliv)ligne).setMtTtcLApresRemiseGlobaleDocument(ttcTmp);
									}else {
										((TaLBonliv)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLBonliv)ligne).getMtHtLDocument().add(tvaLigne));
									}

							}
							ttcTmp =  ttcTmp.subtract(((TaLBonliv)ligne).getMtTtcLApresRemiseGlobaleDocument());
							htTmp =  htTmp.subtract(((TaLBonliv)ligne).getMtHtLApresRemiseGlobaleDocument());

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
	public void calculMontantLigneDocument(TaBonliv doc) {
		for (Object ligne : doc.getLignes()) {
			((TaLBonliv)ligne).calculMontant();
		}
	}
	
	/**
	 * Calcul de la grille de TVA en fonction de lignes du document et du taux de remise HT global.
	 * Mise à jour de la propriété <code>lignesTVA</code>
	 * et mise à jour du montant de la TVA dans les lignes du document
	 */
	public void calculTvaTotal(TaBonliv doc) {
		calculMontantLigneDocument(doc);
		calculLignesTva(doc);
		dispatcherTva(doc);
	}
	
	/**
	 * Calcul de la grille de TVA en fonction de lignes du document et du taux de remise HT global.
	 * Mise à jour de la propriété <code>lignesTVA</code>
	 */
	public void calculLignesTva(TaBonliv doc) {

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
			//((TaLBonliv)ligne).calculMontant();
			codeTVA = ((TaLBonliv)ligne).getCodeTvaLDocument();
			if(codeTVA!=null && !codeTVA.equals("")) {
				ttcLigne = ((TaLBonliv)ligne).getMtTtcLDocument();
				htLigne = ((TaLBonliv)ligne).getMtHtLDocument();
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
					tauxTVA.put(codeTVA,((TaLBonliv)ligne).getTauxTvaLDocument());
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
				BigDecimal valeurInterTTC=mtTtcTotal.multiply(   doc.getTxRemHtDocument().divide(new BigDecimal(100)),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
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
	

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaBonliv transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaBonliv transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	@Override
	public void remove(TaBonliv persistentInstance, boolean recharger) throws RemoveException {
		// TODO Auto-generated method stub
		try {
			if(taAutorisationDossierService.autoriseMenu(IModulesProgramme.ID_MODULE_ABONNEMENT, sessionInfo.getUtilisateur())) {
				//taLigneALigneEcheanceService.removeAllByIdDocumentAndTypeDoc(persistentInstance.getIdDocument(), *.TYPE_DOC);
			}
			if(recharger)persistentInstance=findByIDFetch(persistentInstance.getIdDocument());
			List<ILigneDocumentTiers> listeLien = recupListeLienLigneALigne(persistentInstance);
			dao.remove(persistentInstance);
			mergeEntityLieParLigneALigne(listeLien);
			this.annuleCode(persistentInstance.getCodeDocument());
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
	public void remove(TaBonliv persistentInstance) throws RemoveException {
		remove(persistentInstance, true);
	}
	
	public TaBonliv merge(TaBonliv detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaBonliv merge(TaBonliv detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);
		List<ILigneDocumentTiers> listeLien =null;
		TaBonliv objInitial = detachedInstance;
		try {
			if(detachedInstance.getIdDocument()!=0)
				objInitial=findByIDFetch(detachedInstance.getIdDocument());
		} catch (FinderException e) {
			// TODO Auto-generated catch block
		}
		modifEtatLigne(detachedInstance);		
		TaEtat etat=changeEtatDocument(detachedInstance);
		if(etat!=null)detachedInstance.addREtat(etat);

		//récupération des liens avec le document d'origine en cours si ces liens sont les sources du document d'origine
		listeLien = recupListeLienLigneALigne(objInitial);
		//récupération des liens avec le document modifié en cours si ces liens sont les sources du document modifié 
		listeLien =recupListeLienLigneALigne(detachedInstance,listeLien);
		
		TaMiseADisposition miseADispo = detachedInstance.getTaMiseADisposition();
		
		//je détache les réglements avant d'enregistrer le panier
		List<TaRReglementLiaison> listeReglementTmp=new LinkedList<>();
		for (TaRReglementLiaison ligne : detachedInstance.getTaRReglementLiaisons()) {
			listeReglementTmp.add(ligne);
		}
		detachedInstance.getTaRReglementLiaisons().clear();		
		for (TaRReglementLiaison taRReglement : listeReglementTmp) {
			taRReglement.setTaBonliv(null);
		}
		//j'enregistre
		detachedInstance=dao.merge(detachedInstance);
		
		//je rattache les réglements à la facture
		for (TaRReglementLiaison taRReglement : listeReglementTmp) {
			taRReglement.setTaBonliv(detachedInstance);
			detachedInstance.addRReglementLiaison(taRReglement);
		}
		//j'enregistre les réglements
		TaReglement reglement=null;
		for (TaRReglementLiaison ligne : detachedInstance.getTaRReglementLiaisons()) {
			reglement=ligne.getTaReglement();
			if(reglement!=null && reglement.getIdDocument()==0){
				reglement.getTaRReglementLiaisons().remove(ligne);
				reglement=daoReglement.merge(ligne.getTaReglement());
//				reglement.addRReglementLiaison(ligne);
				ligne.setTaReglement(reglement);
			}
		}
		for (TaRReglementLiaison ligne : detachedInstance.getTaRReglementLiaisons()) {
			if(ligne.getTaReglement()!=null) {
				if(ligne.getEtat()==IHMEtat.integre) {
					ligne.setTaMiseADisposition(miseADispo);
					ligne.getTaReglement().setTaMiseADisposition(miseADispo);
				}
				if((ligne.getEtatDeSuppression()==IHMEtat.suppression)){
					ligne.getTaReglement().removeReglementLiaison(ligne);
				}
				
				reglement=daoReglement.merge(ligne.getTaReglement());
				ligne.setTaBonliv(detachedInstance);
				ligne.setTaReglement(reglement);
			}
		}

		

		detachedInstance=dao.merge(detachedInstance);		
		mergeEntityLieParLigneALigne(listeLien);
		
		annuleCode(detachedInstance.getCodeDocument());
		return detachedInstance;
	}
	
	public TaBonliv mergeAndFindById(TaBonliv detachedInstance, String validationContext) {
		TaBonliv br = merge(detachedInstance,validationContext);
		try {
			br = findByIDFetch(br.getIdDocument()); //pour etre sur que les valeur initialisé par les triggers "qui_cree,..." soit bien affecté avant une éventuelle nouvelle modification du document
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return br;
	}

	public TaBonliv findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaBonliv findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaBonliv> selectAll() {
		List<TaBonliv> l=dao.selectAll();
		return l;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaBonlivDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaBonlivDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaBonliv> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaBonlivDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaBonlivDTO entityToDTO(TaBonliv entity) {
//		TaBonlivDTO dto = new TaBonlivDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaBonlivMapper mapper = new TaBonlivMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaBonlivDTO> listEntityToListDTO(List<TaBonliv> entity) {
		List<TaBonlivDTO> l = new ArrayList<TaBonlivDTO>();

		for (TaBonliv taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaBonlivDTO> selectAllDTO() {
		System.out.println("List of TaBonlivDTO EJB :");
		ArrayList<TaBonlivDTO> liste = new ArrayList<TaBonlivDTO>();

		List<TaBonliv> projects = selectAll();
		for(TaBonliv project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaBonlivDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaBonlivDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaBonlivDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaBonlivDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaBonlivDTO dto, String validationContext) throws EJBException {
		try {
			TaBonlivMapper mapper = new TaBonlivMapper();
			TaBonliv entity = null;
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
	
	public void persistDTO(TaBonlivDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaBonlivDTO dto, String validationContext) throws CreateException {
		try {
			TaBonlivMapper mapper = new TaBonlivMapper();
			TaBonliv entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaBonlivDTO dto) throws RemoveException {
		try {
			TaBonlivMapper mapper = new TaBonlivMapper();
			TaBonliv entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaBonliv refresh(TaBonliv persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaBonliv value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaBonliv value, String propertyName, String validationContext) {
		try {
			if(validationContext==null) validationContext="";
			validate(value, propertyName, validationContext);
			//dao.validateField(value,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOValidationContext")
	public void validateDTO(TaBonlivDTO dto, String validationContext) {
		try {
			TaBonlivMapper mapper = new TaBonlivMapper();
			TaBonliv entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaBonlivDTO> validator = new BeanValidator<TaBonlivDTO>(TaBonlivDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaBonlivDTO dto, String propertyName, String validationContext) {
		try {
			TaBonlivMapper mapper = new TaBonlivMapper();
			TaBonliv entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaBonlivDTO> validator = new BeanValidator<TaBonlivDTO>(TaBonlivDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaBonlivDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaBonlivDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaBonliv value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaBonliv value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}
	
	@Override
	public List<TaBonliv> rechercheDocument(Date dateDeb, Date dateFin) {
		return dao.rechercheDocument(dateDeb, dateFin);
	}

	@Override
	public List<Object[]> rechercheDocument(Date dateDeb, Date dateFin,
			Boolean light) {
		return dao.rechercheDocument(dateDeb, dateFin, light);
	}

	@Override
	public List<TaBonliv> rechercheDocument(String codeDeb, String codeFin) {
		return dao.rechercheDocument(codeDeb, codeFin);
	}

	@Override
	public List<TaBonliv> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers) {
		return dao.rechercheDocument(dateDeb, dateFin, codeTiers);
	}

	@Override
	public List<Object[]> rechercheDocumentLight(Date dateDeb, Date dateFin,
			String codeTiers) {
		return dao.rechercheDocumentLight(dateDeb, dateFin, codeTiers);
	}

	@Override
	public List<Object[]> rechercheDocumentLight(String codeDoc,
			String codeTiers) {
		return dao.rechercheDocumentLight(codeDoc, codeTiers);
	}

	@Override
	public List<TaBonliv> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers) {
		return dao.rechercheDocument(codeDeb, codeFin, codeTiers);
	}

	@Override
	public List<TaBonliv> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers, Boolean export) {
		// TODO Auto-generated method stub
		return dao.rechercheDocument(dateDeb, dateFin, codeTiers, export);
	}

	@Override
	public List<TaBonliv> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers, Boolean export) {
		// TODO Auto-generated method stub
		return dao.rechercheDocument(codeDeb, codeFin, codeTiers, export);
	}

	@Override
	public List<TaBonliv> findByCodeTiersAndDate(String codeTiers, Date debut,
			Date fin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object> findChiffreAffaireByCodeTiers(String codeTiers,
			Date debut, Date fin, int precision) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaTiers> findTiersByCodeArticleAndDate(String codeArticle,
			Date debut, Date fin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object> findChiffreAffaireByCodeArticle(String codeArticle,
			Date debut, Date fin, int precision) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IDocumentTiers> selectDocNonTransformeRequete(
			IDocumentTiers doc, String codeTiers, String typeOrigine,
			String typeDest, Date debut, Date fin) {
		// TODO Auto-generated method stub
		return dao.selectDocNonTransformeRequete(doc, codeTiers, typeOrigine, typeDest, debut, fin);
	}

	@Override
	public List<TaBonliv> selectAll(IDocumentTiers taDocument, Date dateDeb,
			Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaBonliv findByIdDocument(int id) throws FinderException {
		return findById(id);
	}

	@Override
	public Date calculDateEcheanceAbstract(TaBonliv doc, Integer report, Integer finDeMois) {
		// TODO Auto-generated method stub
		return calculDateEcheance(doc,report,finDeMois);
	}

	@Override
	public Date calculDateEcheance(TaBonliv doc, Integer report, Integer finDeMois) {
		// TODO Auto-generated method stub
		return null;
	}
// ----------------------------------------------------

	@Override
	public List<DocumentDTO> findAllDTOPeriode(Date dateDebut, Date dateFin,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.findAllDTOPeriode(dateDebut, dateFin, codeTiers);
	}

	@Override
	public List<DocumentDTO> findDocumentNonTransfosDTO(Date dateDebut, Date dateFin,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.findDocumentNonTransfosDTO(dateDebut, dateFin,codeTiers);
	}
	
	@Override
	public List<DocumentDTO> findDocumentTransfosDTO(Date dateDebut, Date dateFin,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.findDocumentTransfosDTO(dateDebut, dateFin,codeTiers);
	}
	
	@Override
	public List<DocumentDTO> findDocumentNonTransfosARelancerDTO(Date dateDebut, Date dateFin, int deltaNbJours,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.findDocumentNonTransfosARelancerDTO(dateDebut,dateFin,deltaNbJours,codeTiers);
	}

	@Override
	public long countDocument(Date debut, Date fin,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.countDocument(debut, fin, codeTiers);
	}

	@Override
	public long countDocumentNonTransforme(Date debut, Date fin,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.countDocumentNonTransforme(debut, fin, codeTiers);
	}

	@Override
	public long countDocumentNonTransformeARelancer(Date debut, Date fin, int deltaNbJours,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.countDocumentNonTransformeARelancer(debut, fin, deltaNbJours, codeTiers);
	}

	@Override
	public long countDocumentTransforme(Date debut, Date fin,String codeTiers){
		return dao.countDocumentTransforme(debut, fin, codeTiers);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireTotalDTO(dateDebut, dateFin, codeTiers);
	}

	@Override
	public DocumentChiffreAffaireDTO chiffreAffaireTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.chiffreAffaireTotalDTO(dateDebut, dateFin, codeTiers);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireTransformeTotalDTO(dateDebut, dateFin, codeTiers);
	}

	@Override
	public DocumentChiffreAffaireDTO chiffreAffaireTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.chiffreAffaireTransformeTotalDTO(dateDebut, dateFin, codeTiers);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireNonTransformeTotalDTO(dateDebut, dateFin, codeTiers);
	}

	@Override
	public DocumentChiffreAffaireDTO chiffreAffaireNonTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.chiffreAffaireNonTransformeTotalDTO(dateDebut, dateFin, codeTiers);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeARelancerTotalDTO(Date dateDebut,
			Date dateFin, int deltaNbJours,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireNonTransformeARelancerTotalDTO(dateDebut, dateFin, deltaNbJours, codeTiers);
	}

	@Override
	public DocumentChiffreAffaireDTO chiffreAffaireNonTransformeARelancerTotalDTO(Date dateDebut, Date dateFin,
			int deltaNbJours,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.chiffreAffaireNonTransformeARelancerTotalDTO(dateDebut, dateFin, deltaNbJours, codeTiers);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalJmaDTO(Date dateDebut, Date dateFin, int precision,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireTotalJmaDTO(dateDebut, dateFin, precision, codeTiers);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeJmaDTO(Date dateDebut, Date dateFin,
			int precision,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireNonTransformeJmaDTO(dateDebut, dateFin, precision, codeTiers);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTransformeJmaDTO(Date dateDebut, Date dateFin,
			int precision,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireTransformeJmaDTO(dateDebut, dateFin, precision, codeTiers);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeARelancerJmaDTO(Date dateDebut, Date dateFin,
			int precision, int deltaNbJours,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireNonTransformeARelancerJmaDTO(dateDebut, dateFin, precision, deltaNbJours, codeTiers);
	}

	@Override
	public String generePDF(int idDoc, Map<String,Object> mapParametreEdition, List<String> listResourcesPath, String theme) {
		return generePDF(idDoc,editionDefaut,mapParametreEdition, null, null);
	}
	
	
   //Rajout YANN pour impression edition defaut choisi par l'utilisateur
	/**
	 * Méthode intérmédiaire a laquelle on peut passé une action edition (canal) en param par exemple mail, impression, ou espace client
	 * L'autre particularité de cette méthode est qu'elle n'a pas besoin de modeleEdition en param,
	 *  elle va chercher l'edition choisie si il y a en focntion de l'action et elle ecrit dans un fichier tmp le birt xml
	 * Cette méthode fini quoi qu'il arrive par appellé l'autre generePDF final a 5 param
	 * Cette méthode ne peut pas être extraite telle quelle car elle contient des références propre au document (taFacture, editionDefaut etc...)
	 * @return generePDF final
	 * @author yann
	 */
	public String generePDF(int idDoc, Map<String,Object> mapParametreEdition, List<String> listResourcesPath, String theme, TaActionEdition action) {
		if(action != null) {
			TaEdition edition = taEditionService.rechercheEditionActionDefaut(null, action, TaBonliv.TYPE_DOC);
			
			if(edition != null) {//si j'ai trouver une edition par defaut
				TaEdition editionDefautChoisie = edition;
				//je vais chercher l'entité avec l'id du DTO
				String localPath = writingFileEdition(edition);
				BdgProperties bdgProperties = new BdgProperties();
				List<String> listeResourcesPath = null;
				
				if(editionDefautChoisie.getResourcesPath()!=null) {
					listeResourcesPath = new ArrayList<>();
					listeResourcesPath.add(editionDefautChoisie.getResourcesPath());
				}
				
				return generePDF(idDoc,  localPath, mapParametreEdition, listeResourcesPath,  editionDefautChoisie.getTheme()); 
				
			//si pas d'édition par defaut
			}else {
				return generePDF(idDoc,editionDefaut,mapParametreEdition, null, null);
			}
			
		}else {// si action nulle
			return generePDF(idDoc,editionDefaut,mapParametreEdition, null, null);
		}
		
	}
	
	
	
	
	/**
	 * Méthode qui génére un pdf à partir de 5 param avec BIRT
	 * Méthode FINALE, toute les méthode generePDF finisse par appelé celle-ci.
	 * @param int idFacture
	 * @param String modeleEdition
	 * @param Map<String,Object> mapParametreEdition
	 * @param List<String> listResourcesPath
	 * @param String theme
	 */
	public String generePDF(int idDoc, String modeleEdition, Map<String,Object> mapParametreEdition, List<String> listResourcesPath, String theme) {
		try {
			TaBonliv doc = findById(idDoc);
			SchemaResolver sr = new SchemaResolver();
			String localPath = bdgProperties.osTempDirDossier(sr.resolveCurrentTenantIdentifier())+"/"+bdgProperties.tmpFileName(doc.getCodeDocument(), doc.getTaTiers().getCodeTiers(),"bonlivraison.pdf");
			System.out.println(localPath);
			System.out.println(bdgProperties.urlDemoHttps()+modeleEdition);
			
			Map<String,Object> mapEdition = new HashMap<String,Object>();
			
			boolean editionClient = false;
			boolean impression_livraison = taPreferencesService.retourPreferenceBoolean("bonliv", "impression_livraison");
			
			doc.calculeTvaEtTotaux();

			mapEdition.put("doc",doc );
			
			mapEdition.put("taInfoEntreprise", entrepriseService.findInstance());

			mapEdition.put("lignes", doc.getLignes());

			
			if(mapParametreEdition == null) {
				mapParametreEdition = new HashMap<String,Object>();
			} else {
				if(mapParametreEdition.containsKey("mode")) {
					mapEdition.put("mode", mapParametreEdition.get("mode") );
				} 
				if(mapParametreEdition.containsKey("editionClient")) {
					editionClient = (boolean) mapParametreEdition.get("editionClient");
				}
			}
			List<TaPreferences> liste= taPreferencesService.findByGroupe("bonliv");
			if(!mapParametreEdition.containsKey("preferences")) mapParametreEdition.put("preferences", liste);
			if(!mapParametreEdition.containsKey("gestionLot")) mapParametreEdition.put("gestionLot", false);
			if(!mapParametreEdition.containsKey("bonliv")) mapParametreEdition.put("impression_livraison", impression_livraison);
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
				listResourcesPath.add(EditionProgrammeDefaut.LIBRAIRIE_EDITION_DEFAUT_BONLIV);
			}else if(listResourcesPath.isEmpty()) {
					listResourcesPath = new ArrayList<String>();
					listResourcesPath.add(EditionProgrammeDefaut.LIBRAIRIE_EDITION_DEFAUT_BONLIV);
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
			
//			if(editionClient) {
//				//Mise à jour de la mise à dispostion
//				if(doc.getTaMiseADisposition()==null) {
//					doc.setTaMiseADisposition(new TaMiseADisposition());
//				}
//				Date dateMiseADispositionClient = new Date();
//				doc.getTaMiseADisposition().setImprimePourClient(dateMiseADispositionClient);
//				doc = mergeAndFindById(doc,ITaDevisServiceRemote.validationContext);
//			}
			 
			 return localPath;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersParMois(Date debut, Date fin, String codeTiers) {
		// TODO Auto-generated method stub
		return dao.findArticlesParTiersParMois(debut, fin,codeTiers);
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersTransforme(Date debut, Date fin, String codeTiers) {
		// TODO Auto-generated method stub
		return dao.findArticlesParTiersTransforme(debut, fin,codeTiers);
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransforme(Date debut, Date fin, String codeTiers) {
		// TODO Auto-generated method stub
		return dao.findArticlesParTiersNonTransforme(debut, fin,codeTiers);
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransformeARelancer(Date debut, Date fin, int deltaNbJours, String codeTiers) {
		// TODO Auto-generated method stub
		return dao.findArticlesParTiersNonTransformeARelancer(debut, fin, deltaNbJours,codeTiers);
	}	
	@Override
	public List<TaBonliv> rechercheDocumentVerrouille(Date dateDeb, Date dateFin, String codeTiers,
			Boolean verrouille) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaBonliv> rechercheDocumentVerrouille(String codeDeb, String codeFin, String codeTiers,
			Boolean verrouille) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaBonliv> rechercheDocument(String codeTiers, Date dateExport, Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}


	
	
/////// regroupement
	
	@Override
	public DocumentChiffreAffaireDTO chiffreAffaireTotalDTOParTypeRegroupement(Date dateDebut, Date dateFin,
			String codeTiers, String typeRegroupement, Object valeurRegroupement, boolean regrouper) {
		// TODO Auto-generated method stub
		return dao.chiffreAffaireTotalDTOParTypeRegroupement(dateDebut, dateFin, codeTiers, typeRegroupement, valeurRegroupement, regrouper);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTOParTypeRegroupement(Date dateDebut, Date dateFin,
			String codeTiers, String typeRegroupement, Object valeurRegroupement, boolean regrouper) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireTotalDTOParTypeRegroupement(dateDebut, dateFin, codeTiers, typeRegroupement, valeurRegroupement, regrouper);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalJmaDTOParRegroupement(Date dateDebut, Date dateFin,
			int precision, String codeTiers, String typeRegroupement, Object valeurRegroupement) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireTotalJmaDTOParRegroupement(dateDebut, dateFin, precision, codeTiers, typeRegroupement, valeurRegroupement);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> chiffreAffaireParEtat(Date debut, Date fin, String codeTiers,
			Object codeEtat, String typeRegroupement, Object valeurRegroupement) {
		// TODO Auto-generated method stub
		return dao.chiffreAffaireParEtat(debut, fin, codeTiers, codeEtat, typeRegroupement, valeurRegroupement);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> countDocumentAndCodeEtatParTypeRegroupement(Date debut, Date fin,
			String codeTiers, Object codeEtat, String typeRegroupement, Object valeurRegroupement) {
		// TODO Auto-generated method stub
		return dao.countDocumentAndCodeEtatParTypeRegroupement(debut, fin, codeTiers, codeEtat, typeRegroupement, valeurRegroupement);
	}

	@Override
	public List<DocumentDTO> findAllDTOPeriodeAndCodeEtatParTypeRegroupement(Date debut, Date fin, String codeTiers,
			Object codeEtat, String typeRegroupement, Object valeurRegroupement) {
		// TODO Auto-generated method stub
		return dao.findAllDTOPeriodeAndCodeEtatParTypeRegroupement(debut, fin, codeTiers, codeEtat, typeRegroupement, valeurRegroupement);
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersMoisAndCodeEtatParTypeRegroupement(Date debut, Date fin,
			Object codeEtat, String typeRegroupement, Object valeurRegroupement) {
		// TODO Auto-generated method stub
		return dao.findArticlesParTiersMoisAndCodeEtatParTypeRegroupement(debut, fin, codeEtat, typeRegroupement, valeurRegroupement);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTOAndCodeEtatParTypeRegroupement(Date dateDebut,
			Date dateFin, String codeTiers, Object codeEtat, String typeRegroupement, Object valeurRegroupement,
			boolean regrouper) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireTotalDTOAndCodeEtatParTypeRegroupement(dateDebut, dateFin, codeTiers, codeEtat, typeRegroupement, valeurRegroupement, regrouper);
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersParMoisParTypeRegroupement(Date dateDebut, Date dateFin,
			String codeTiers, String typeRegroupement, Object valeurRegroupement, boolean regroupee) {
		// TODO Auto-generated method stub
		return dao.findArticlesParTiersParMoisParTypeRegroupement(dateDebut, dateFin, codeTiers, typeRegroupement, valeurRegroupement, regroupee);
	}
	

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersParMois(Date debut, Date fin) {
		// TODO Auto-generated method stub
		return dao.findArticlesParTiersParMois(debut, fin);
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersTransforme(Date debut, Date fin) {
		// TODO Auto-generated method stub
		return dao.findArticlesParTiersTransforme(debut, fin);
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransforme(Date debut, Date fin) {
		// TODO Auto-generated method stub
		return dao.findArticlesParTiersNonTransforme(debut, fin);
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransformeARelancer(Date debut, Date fin,
			int deltaNbJours) {
		// TODO Auto-generated method stub
		return dao.findArticlesParTiersNonTransformeARelancer(debut, fin, deltaNbJours);
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersParMoisParTypeRegroupement(Date dateDebut, Date dateFin,
			String typeRegroupement, Object valeurRegroupement) {
		// TODO Auto-generated method stub
		return dao.findArticlesParTiersParMoisParTypeRegroupement(dateDebut, dateFin, typeRegroupement, valeurRegroupement);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeSumChiffreAffaireTotalDTOArticle(Date dateDebut, Date dateFin,
			String codeArticle, String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeSumChiffreAffaireTotalDTOArticleMois(Date dateDebut, Date dateFin,
			String codeArticle, String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> countChiffreAffaireTotalDTOArticle(Date dateDebut, Date dateFin,
			String codeArticle, String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> sumChiffreAffaireTotalDTOArticle(Date dateDebut, Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<DocumentChiffreAffaireDTO> sumChiffreAffaireTotalDTOArticle(Date dateDebut, Date dateFin,
			String codeArticle) {
		return dao.sumChiffreAffaireTotalDTOArticle(dateDebut,  dateFin, codeArticle);
	}
	@Override
	public List<DocumentChiffreAffaireDTO> sumChiffreAffaireTotalDTOArticleMoinsAvoir(Date dateDebut, Date dateFin, String codeArticle){
		return dao.sumChiffreAffaireTotalDTOArticleMoinsAvoir(dateDebut, dateFin,  codeArticle);
	}

	public TaEtat rechercheEtatInitialDocument() {
		try {
			return taEtatService.documentEncours(null);
		} catch (Exception e) {
			return null;
		}

	}
	
	@Override
	public List<DocumentDTO> findAllDTOPeriodeSimple(Date dateDebut, Date dateFin,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.findAllDTOPeriodeSimple(dateDebut, dateFin, codeTiers);
	}
	
	@Override
	public List<DocumentDTO> findAllDTOIntervalle(String codeDebut, String codeFin,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.findAllDTOIntervalle(codeDebut, codeFin, codeTiers);
	}

	@Override
	public TaBonliv findDocByLDoc(ILigneDocumentTiers lDoc) {
		// TODO Auto-generated method stub
		TaBonliv o= (TaBonliv) dao.findDocByLDoc(lDoc);
		o=(TaBonliv) recupSetREtat(o);
		o=(TaBonliv) recupSetHistREtat(o);
		o=(TaBonliv) recupSetLigneALigne(o);
		o=(TaBonliv) recupSetRDocument(o);
		o=(TaBonliv) recupSetREtatLigneDocuments(o);
		o=(TaBonliv) recupSetHistREtatLigneDocuments(o);
		recupSetRReglementLiaison(o);
		return o;
	}

	@Override
	public TaBonliv mergeEtat(IDocumentTiers detachedInstance) {
		// TODO Auto-generated method stub
//		à mettre si gestion des états !!!!
		modifEtatLigne(detachedInstance);		
		TaEtat etat=changeEtatDocument(detachedInstance);
		((TaBonliv) detachedInstance).addREtat(etat);
		
		detachedInstance=dao.merge((TaBonliv) detachedInstance);	
		return (TaBonliv) detachedInstance;
	}

	
	@Override
	public TaBonliv mergeEtat(IDocumentTiers detachedInstance,boolean avecLien) {
//		à mettre si gestion des états !!!!
		modifEtatLigne(detachedInstance);		
		TaEtat etat=changeEtatDocument(detachedInstance);
		((TaBonliv) detachedInstance).addREtat(etat);
		
		
		List<ILigneDocumentTiers> listeLien = null;
		if(avecLien) {
			listeLien = recupListeLienLigneALigne(detachedInstance);
		}
		
		detachedInstance=dao.merge((TaBonliv) detachedInstance);
		
		if(avecLien) {
			mergeEntityLieParLigneALigne(listeLien);
		}

		return (TaBonliv) detachedInstance;
	}


	@Override
	public int findDocByLDocDTO(ILigneDocumentTiers lDoc) {
		return dao.findDocByLDocDTO(lDoc);
	}

	@Override
	public TaEtat etatLigneInsertion(TaBonliv masterEntity) {
		// TODO Auto-generated method stub
		return super.etatLigneInsertion(masterEntity);
	}
	

	@Override
	public TaBonliv findByCodeFetch(String code)  throws FinderException {
		// TODO Auto-generated method stub
		TaBonliv o= (TaBonliv) dao.findByCodeFetch(code);
		recupSetREtat(o);
		recupSetHistREtat(o);
		recupSetLigneALigne(o);
		recupSetRDocument(o);
		recupSetREtatLigneDocuments(o);
		recupSetHistREtatLigneDocuments(o);
		recupSetRReglementLiaison(o);
		return o;
	}

	@Override
	public TaBonliv findByIDFetch(int id)  throws FinderException {
		// TODO Auto-generated method stub
		TaBonliv o=  (TaBonliv) dao.findByIdFetch(id);
		recupSetREtat(o);
		recupSetHistREtat(o);
		recupSetLigneALigne(o);
		recupSetRDocument(o);
		recupSetREtatLigneDocuments(o);
		recupSetHistREtatLigneDocuments(o);
		recupSetRReglementLiaison(o);
		return o;
	}

	@Override
	public List<IDocumentTiersDTO> selectTourneeTransporteur(String codeTransporteur, Date debut, Date fin) {
		// TODO Auto-generated method stub
		return dao.selectTourneeTransporteur(codeTransporteur, debut, fin);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeBonlivTransporteurDTO(String codeTransporteur,Date dateDebut, Date dateFin,
			String codeArticle) {
		// TODO Auto-generated method stub
		return dao.listLigneArticlePeriodeBonlivTransporteurDTO(codeTransporteur,dateDebut, dateFin, codeArticle);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeBonlivTransporteurDTO(String codeTransporteur,Date dateDebut, Date dateFin,
			String codeArticle, Boolean synthese) {
		// TODO Auto-generated method stub
		return dao.listLigneArticlePeriodeBonlivTransporteurDTO(codeTransporteur,dateDebut, dateFin, codeArticle, synthese);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeBonlivTransporteurDTO(String codeTransporteur,Date dateDebut, Date dateFin,
			String codeArticle, Boolean synthese, String orderBy) {
		// TODO Auto-generated method stub
		return dao.listLigneArticlePeriodeBonlivTransporteurDTO(codeTransporteur,dateDebut, dateFin, codeArticle, synthese, orderBy);
	}

	@Override
	public List<IDocumentTiersDTO> selectBLNonTermineSansLotTransporteur(String codeTransporteur, Date debut,
			Date fin) {
		// TODO Auto-generated method stub
		return dao.selectBLNonTermineSansLotTransporteur(codeTransporteur, debut, fin);
	}


	

	@Override
	public List<DocumentDTO> findAllDTOPeriodeAvecEtat(Date dateDebut, Date dateFin, String codeTiers, String etat) {
		// TODO Auto-generated method stub
		return dao.findAllDTOPeriodeAvecEtat(dateDebut, dateFin, codeTiers, etat);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> chiffreAffaireTotalAvecEtatDTO(Date dateDebut, Date dateFin, String codeTiers,
			String etat) {
		// TODO Auto-generated method stub
		return dao.chiffreAffaireTotalAvecEtatDTO(dateDebut, dateFin, codeTiers, etat);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalAvecEtatDTO(Date dateDebut, Date dateFin,
			String codeTiers, String etat) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireTotalAvecEtatDTO(dateDebut, dateFin, codeTiers, etat);
	}

	@Override
	public DocumentChiffreAffaireDTO chiffreAffaireTotalSurEtatDTO(Date dateDebut, Date dateFin, String codeTiers,
			String etat) {
		// TODO Auto-generated method stub
		return dao.chiffreAffaireTotalSurEtatDTO(dateDebut, dateFin, codeTiers, etat);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalSurEtatDTO(Date dateDebut, Date dateFin,
			String codeTiers, String etat) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireTotalSurEtatDTO(dateDebut, dateFin, codeTiers, etat);
	}


	@Override
	public List<DocumentChiffreAffaireDTO> countDocumentAvecEtat(Date debut, Date fin, String codeTiers,
			String codeArticle, String etat) {
		// TODO Auto-generated method stub
		return dao.countDocumentAvecEtat(debut, fin, codeTiers, codeArticle, etat);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalJmaAvecEtatDTO(Date dateDebut, Date dateFin,
			int precision, String codeTiers, String etat) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireTotalJmaAvecEtatDTO(dateDebut, dateFin, precision, codeTiers, etat);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiersAvecEtat(Date dateDebut, Date dateFin,
			String codeArticle, String codeTiers, String etat, int deltaNbJours) {
		// TODO Auto-generated method stub
		return dao.listLigneArticleDTOTiersAvecEtat(dateDebut, dateFin, codeArticle, codeTiers, etat, deltaNbJours);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiersAvecEtat(Date dateDebut, Date dateFin,
			String codeArticle, String codeTiers, String etat, String orderBy, int deltaNbJours) {
		// TODO Auto-generated method stub
		return dao.listLigneArticleDTOTiersAvecEtat(dateDebut, dateFin, codeArticle, codeTiers, etat, orderBy, deltaNbJours);
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersParMoisAvecEtat(Date debut, Date fin, String codeTiers,
			String etat, int deltaNbJours) {
		// TODO Auto-generated method stub
		return dao.findArticlesParTiersParMoisAvecEtat(debut, fin, codeTiers, etat, deltaNbJours);
	}
}


