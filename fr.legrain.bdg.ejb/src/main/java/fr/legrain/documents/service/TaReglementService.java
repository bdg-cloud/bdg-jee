package fr.legrain.documents.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
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
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.article.dao.ITvaDAO;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLigneALigneEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaReglementServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaAutorisationsDossierServiceRemote;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.bdg.model.mapping.mapper.TaReglementMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dashboard.dto.TaArticlesParTiersDTO;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.dto.TaRReglementDTO;
import fr.legrain.document.dto.TaReglementDTO;
import fr.legrain.document.model.LigneTva;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaLAvoir;
import fr.legrain.document.model.TaRReglement;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TaRemise;
import fr.legrain.documents.dao.IRReglementDAO;
import fr.legrain.documents.dao.IReglementDAO;
import fr.legrain.droits.model.IModulesProgramme;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibCalcul;
import fr.legrain.tiers.dao.ITCPaiementDAO;
import fr.legrain.tiers.model.TaCPaiement;
import fr.legrain.tiers.model.TaTCPaiement;
import fr.legrain.tiers.model.TaTiers;

/**
 * Session Bean implementation class TaReglementBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaReglementService extends AbstractApplicationDAOServer<TaReglement, TaReglementDTO> implements ITaReglementServiceRemote {

	static Logger logger = Logger.getLogger(TaReglementService.class);

	@Inject private IReglementDAO dao;
	@Inject private IRReglementDAO daoRReglement;
	@Inject private ITvaDAO taTvaDAO;
	@Inject private ITCPaiementDAO taTCPaiementDAO;
	@EJB private ITaGenCodeExServiceRemote gencode;
	@Inject private	SessionInfo sessionInfo;
	@EJB private ITaFactureServiceRemote taFactureService;
	
	@EJB private ITaLEcheanceServiceRemote taLEcheanceService;
	@EJB private ITaLigneALigneEcheanceServiceRemote taLigneALigneEcheanceService;
	@EJB private ITaAutorisationsDossierServiceRemote taAutorisationDossierService;

	/**
	 * Default constructor. 
	 */
	public TaReglementService() {
		super(TaReglement.class,TaReglementDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaReglement a";
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


	/**
	 * Repartir le total chaque code TVA sur l'ensemble des lignes concernées par ce code. 
	 */
	public void dispatcherTva(TaReglement doc) {
		
//		BigDecimal tvaLigne = new BigDecimal(0); //Montant de TVA de la ligne du document courante
//		BigDecimal totalTemp = new BigDecimal(0); //Somme des montants HT des lignes du document (mis à jour au fil des iterations)
//
//		
//		boolean derniereLignePourTVA = false;
//		
//		for (Object ligne : doc.getLignes()) {
//			if(((TaLAvoir)ligne).getMtHtLDocument()!=null)
//				totalTemp = totalTemp.add(((TaLAvoir)ligne).getMtHtLDocument());
//		}
//		if(totalTemp!=null && doc.getTxRemHtDocument()!=null)
//			doc.setRemHtDocument(totalTemp.multiply(doc.getTxRemHtDocument().divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_HALF_UP));	
//		
//		for (TaLAvoir ligne : doc.getLignes()) {
//			if(doc.getTxRemHtDocument()!=null && doc.getTxRemHtDocument().signum()>0 && ligne.getMtHtLDocument()!=null  && ligne.getMtTtcLDocument()!=null) {
//				if(doc.getTtc()==1){
//					((TaLAvoir)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLAvoir)ligne).getMtTtcLDocument().subtract(((TaLAvoir)ligne).getMtTtcLDocument()
//							.multiply(doc.getTxRemHtDocument()).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
//					((TaLAvoir)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLAvoir)ligne).getMtTtcLApresRemiseGlobaleDocument());
//					
//				}else{
//					((TaLAvoir)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLAvoir)ligne).getMtHtLDocument().subtract(((TaLAvoir)ligne).getMtHtLDocument()
//							.multiply(doc.getTxRemHtDocument()).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
//					((TaLAvoir)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLAvoir)ligne).getMtHtLApresRemiseGlobaleDocument());	
//				}
//			}
//		}
//		
//		//pour chaque ligne/code TVA
//		for (LigneTva ligneTva : doc.getLignesTVA()) { 
//
//			if (ligneTva.getMtTva()!=null) {
//				int lignepasse=1;
//				BigDecimal tvaTmp = ligneTva.getMtTva(); //montant total de la TVA pour cette ligne/code TVA décrémenter du montant de TVA des lignes du documents deja traite
//				BigDecimal ttcTmp = LibCalcul.arrondi(ligneTva.getMontantTotalTtcAvecRemise());
//				BigDecimal htTmp = LibCalcul.arrondi(ligneTva.getMontantTotalHtAvecRemise());
//				BigDecimal tvaCalcule = new BigDecimal(0);
//				
//				//TaLAvoir derniereLigneFactureAvecMontantDifferentDeZero = null;
//				derniereLignePourTVA = false;
//
//				//pour chaque ligne du document
//				for (Object ligne : doc.getLignes()) {
//					//si c'est une ligne "normale" (ligne HT et non une ligne de commentaire ou autre)
//					if(((TaLAvoir)ligne).getTaTLigne().getCodeTLigne().equals(SWTDocument.C_CODE_T_LIGNE_H)) {
//						//si le code TVA de la ligne correspond à celui traite (boucle superieure)
//						if(((TaLAvoir)ligne).getCodeTvaLDocument()!=null&&((TaLAvoir)ligne).getCodeTvaLDocument().equals(ligneTva.getCodeTva())){
//							tvaLigne = prorataMontantTVALigne(doc,(TaLAvoir)ligne, ligneTva);
//							
//							tvaTmp =  tvaTmp.subtract(tvaLigne);
//							if(tvaTmp.compareTo(resteTVA(doc,ligneTva))==0  && !derniereLignePourTVA) {
//								//Le reste de TVA a traiter correspond a la difference d'arrondi,
//								//les lignes de documents suivantes (s'il en reste) ont un montant HT nul
//								//c'est donc la derniere ligne sur laquelle on peut mettre de la TVA => on ajoute le reliquat
//								tvaLigne = tvaLigne.add(tvaTmp);
//								derniereLignePourTVA = true;
//							}
//							totalTemp = totalTemp.add(((TaLAvoir)ligne).getMtHtLDocument());
//
//							//===Correction des totaux après remise de la ligne du document
//							if(doc.getTxRemHtDocument()!=null && doc.getTxRemHtDocument().signum()>0) {
//								if  (lignepasse>= ligneTva.getNbLigneDocument()) {
//									((TaLAvoir)ligne).setMtHtLApresRemiseGlobaleDocument(htTmp);
//									((TaLAvoir)ligne).setMtTtcLApresRemiseGlobaleDocument(ttcTmp);
//								} else {
//									if(doc.getTtc()==1){
//										((TaLAvoir)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLAvoir)ligne).getMtTtcLDocument().subtract(((TaLAvoir)ligne).getMtTtcLDocument()
//												.multiply(doc.getTxRemHtDocument()).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
//										((TaLAvoir)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLAvoir)ligne).getMtTtcLApresRemiseGlobaleDocument().divide(BigDecimal.valueOf(1).add(
//												 (((TaLAvoir)ligne).getTauxTvaLDocument().divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(3,BigDecimal.ROUND_HALF_UP)
//											)),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP));
//										
//									}else{
//										((TaLAvoir)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLAvoir)ligne).getMtHtLDocument().subtract(((TaLAvoir)ligne).getMtHtLDocument()
//												.multiply(doc.getTxRemHtDocument()).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
//										tvaCalcule = (((TaLAvoir)ligne).getMtHtLApresRemiseGlobaleDocument().
//										multiply(((TaLAvoir)ligne).getTauxTvaLDocument().divide(new BigDecimal(100),MathContext.DECIMAL128))).setScale(2,BigDecimal.ROUND_HALF_UP);
//										((TaLAvoir)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLAvoir)ligne).getMtHtLApresRemiseGlobaleDocument().add(tvaCalcule));	
//									}
////									((TaLAvoir)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLAvoir)ligne).getMtHtLDocument().subtract(((TaLAvoir)ligne).getMtHtLDocument().multiply(txRemHtDocument).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
////									((TaLAvoir)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLAvoir)ligne).getMtTtcLDocument().subtract(((TaLAvoir)ligne).getMtTtcLDocument().multiply(txRemHtDocument).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
//								}
//
//							} else {
//								if(doc.getTtc()==1)
//									if  (lignepasse>= ligneTva.getNbLigneDocument()) {
//										((TaLAvoir)ligne).setMtHtLApresRemiseGlobaleDocument(htTmp);
//									}else{
//										((TaLAvoir)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLAvoir)ligne).getMtTtcLDocument().subtract(tvaLigne));
//									}
//								else
//									if  (lignepasse>= ligneTva.getNbLigneDocument()) {
//										((TaLAvoir)ligne).setMtTtcLApresRemiseGlobaleDocument(ttcTmp);
//									}else {
//										((TaLAvoir)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLAvoir)ligne).getMtHtLDocument().add(tvaLigne));
//									}
//
//							}
//							ttcTmp =  ttcTmp.subtract(((TaLAvoir)ligne).getMtTtcLApresRemiseGlobaleDocument());
//							htTmp =  htTmp.subtract(((TaLAvoir)ligne).getMtHtLApresRemiseGlobaleDocument());
//
//							lignepasse++;
//						}
//					}
//					doc.setRemHtDocument(totalTemp.multiply(doc.getTxRemHtDocument().divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_HALF_UP));						
//
//////					setRemHtDocument(getRemHtDocument().add(totalTemp.multiply(txRemHtDocument.divide(new BigDecimal(100)))));						
//
//				}
//			}
//
//		}


	}
	
	/**
	 * Calcule le montant de TVA d'une ligne du document par rapport au montant total de TVA pour un code TVA donnee
	 * @param ligne - 
	 * @param ligneTva
	 * @return
	 */
	public BigDecimal prorataMontantTVALigne(TaReglement doc, TaLAvoir ligne, LigneTva ligneTva) {
		BigDecimal tvaLigne = new BigDecimal(0);
		
		if (ligneTva.getMontantTotalHt().signum()==0) 
			tvaLigne = ((TaLAvoir)ligne).getMtHtLDocument().multiply(ligneTva.getTauxTva()).divide(new BigDecimal(100));
		else {
//			if  (lignepasse>= ligneTva.getNbLigneDocument()) //si c'est la deniere ligne, on prend tout ce qui reste
//				tvaLigne = tvaTmp;
//			else {
				if(doc.getTtc()==1){ //si saisie TTC
					if(LibCalcul.arrondi(ligneTva.getMontantTotalTtcAvecRemise()).signum()<=0)
						tvaLigne=BigDecimal.valueOf(0);
					else
						tvaLigne = (ligneTva.getMtTva().multiply(((TaLAvoir)ligne).getMtTtcLDocument())).divide(LibCalcul.arrondi(ligneTva.getMontantTotalTtcAvecRemise()),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
				}
				else{
					if(LibCalcul.arrondi(ligneTva.getMontantTotalHtAvecRemise()).signum()<=0)
						tvaLigne =BigDecimal.valueOf(0);
					else
						tvaLigne = (ligneTva.getMtTva().multiply(((TaLAvoir)ligne).getMtHtLDocument())).divide(LibCalcul.arrondi(ligneTva.getMontantTotalHtAvecRemise()),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
				}
//			}
		}
		return tvaLigne;
	}
	
	/**
	 * Calcule le montant de TVA qui reste après répartion de la TVA sur les lignes au prorata du monant HT.
	 * Ce montant de TVA restant de 1 ou 2 centimes provient des arrondis successifs.
	 * @param ligneTva
	 * @return
	 */
	public BigDecimal resteTVA(TaReglement doc, LigneTva ligneTva) {
//		BigDecimal resteTVA = ligneTva.getMtTva();
//		for (Object ligne : doc.getLignes()) {
//			//si c'est une ligne "normale" (ligne HT et non une ligne de commentaire ou autre)
//			if(((TaLAvoir)ligne).getTaTLigne().getCodeTLigne().equals(SWTDocument.C_CODE_T_LIGNE_H)) {
//				//si le code TVA de la ligne correspond à celui traite (boucle superieure)
//				if(((TaLAvoir)ligne).getCodeTvaLDocument()!=null&&((TaLAvoir)ligne).getCodeTvaLDocument().equals(ligneTva.getCodeTva())){
//					resteTVA = resteTVA.subtract(prorataMontantTVALigne(doc,((TaLAvoir)ligne),ligneTva));
//				}
//			}
//		}
//		return resteTVA;
		return null;
	}
	/**
	 * Lance la fonction de calcul du montant sur chacunes des lignes du document.
	 */
	public void calculMontantLigneDocument(TaReglement doc) {
//		for (Object ligne : doc.getLignes()) {
//			((TaLAvoir)ligne).calculMontant();
//		}
	}
	
	/**
	 * Calcul de la grille de TVA en fonction de lignes du document et du taux de remise HT global.
	 * Mise à jour de la propriété <code>lignesTVA</code>
	 * et mise à jour du montant de la TVA dans les lignes du document
	 */
	public void calculTvaTotal(TaReglement doc) {
		calculMontantLigneDocument(doc);
		calculLignesTva(doc);
		dispatcherTva(doc);
	}
	
	/**
	 * Calcul de la grille de TVA en fonction de lignes du document et du taux de remise HT global.
	 * Mise à jour de la propriété <code>lignesTVA</code>
	 */
	public void calculLignesTva(TaReglement doc) {
//
//		Map<String,BigDecimal> montantTotalHt = new HashMap<String,BigDecimal>();
//		Map<String,BigDecimal> montantTotalTtc = new HashMap<String,BigDecimal>();
//		Map<String,BigDecimal> montantTotalHtAvecRemise = new HashMap<String,BigDecimal>();
//		Map<String,BigDecimal> montantTotalTtcAvecRemise = new HashMap<String,BigDecimal>();
//		Map<String,BigDecimal> mtTVA = new HashMap<String,BigDecimal>();
//		Map<String,BigDecimal> tauxTVA = new HashMap<String,BigDecimal>();
//		Map<String,Integer> nbLigne = new HashMap<String,Integer>();
//		String codeTVA = null;
//		
//		/*
//		 * calcul de la TVA different en fonction de la propriete TTC
//		 */
//		BigDecimal ttcLigne = null;
//		BigDecimal htLigne = null;
//		for (Object ligne : doc.getLignes()) {
//			//en commentaire pour ne pas refaire les calculs pendants les editions, 
//			//((TaLAvoir)ligne).calculMontant();
//			codeTVA = ((TaLAvoir)ligne).getCodeTvaLDocument();
//			if(codeTVA!=null && !codeTVA.equals("")) {
//				ttcLigne = ((TaLAvoir)ligne).getMtTtcLDocument();
//				htLigne = ((TaLAvoir)ligne).getMtHtLDocument();
//				if(montantTotalHt.containsKey(codeTVA)) {
//					montantTotalTtc.put(codeTVA,montantTotalTtc.get(codeTVA).add(ttcLigne));
//					montantTotalHt.put(codeTVA,montantTotalHt.get(codeTVA).add(htLigne));
//					montantTotalTtcAvecRemise.put(codeTVA,montantTotalTtcAvecRemise.get(codeTVA).add(ttcLigne));
//					montantTotalHtAvecRemise.put(codeTVA,montantTotalHtAvecRemise.get(codeTVA).add(htLigne));
//					nbLigne.put(codeTVA,nbLigne.get(codeTVA)+1);
//				} else {
//					montantTotalTtc.put(codeTVA,ttcLigne);
//					montantTotalHt.put(codeTVA,htLigne);
//					montantTotalTtcAvecRemise.put(codeTVA,ttcLigne);
//					montantTotalHtAvecRemise.put(codeTVA,htLigne);
//					tauxTVA.put(codeTVA,((TaLAvoir)ligne).getTauxTvaLDocument());
//					nbLigne.put(codeTVA,1);
//				}
//			}
//		}
//
//		for (String codeTva : montantTotalTtc.keySet()) {
//			//les 2 maps ont les meme cles
//			BigDecimal mtTtcTotal = montantTotalTtc.get(codeTva);
//			BigDecimal mtHtTotal = montantTotalHt.get(codeTva);
//			BigDecimal tva =null;
//			if(doc.getTxRemHtDocument()!=null && doc.getTxRemHtDocument().signum()>0) {
////				mtTtcTotal =LibCalcul.arrondi(mtTtcTotal.subtract(     mtTtcTotal.multiply(   txRemHtDocument.divide(new BigDecimal(100))  )       ));
////				mtHtTotal = LibCalcul.arrondi(mtHtTotal.subtract(    mtHtTotal.multiply( (txRemHtDocument.divide(new BigDecimal(100))))     ) ) ;
//				BigDecimal valeurInterTTC=mtTtcTotal.multiply(   doc.getTxRemHtDocument().divide(new BigDecimal(100)),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
//				mtTtcTotal =LibCalcul.arrondi(mtTtcTotal.subtract(valeurInterTTC )) ;
//				BigDecimal valeurInterHT=mtHtTotal.multiply( doc.getTxRemHtDocument().divide(new BigDecimal(100)),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
//				mtHtTotal = LibCalcul.arrondi(mtHtTotal.subtract( valeurInterHT )) ;
//				montantTotalTtcAvecRemise.put(codeTva, mtTtcTotal);
//				montantTotalHtAvecRemise.put(codeTva, mtHtTotal);
//			} 
//
//			if (doc.getTtc()==1) {
//				tva=mtTtcTotal.subtract((mtTtcTotal.multiply(BigDecimal.valueOf(100))) .divide((BigDecimal.valueOf(100).add(tauxTVA.get(codeTva))) ,MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)     ) ;
//				mtTVA.put(codeTva, tva);
//				montantTotalHtAvecRemise.put(codeTva, mtTtcTotal.subtract(tva));
//			} else {
//				tva=mtHtTotal.multiply(   (tauxTVA.get(codeTva).divide(new BigDecimal(100)))) ;
//				mtTVA.put(codeTva, tva );
//				montantTotalTtcAvecRemise.put(codeTva, mtHtTotal.add(tva));
//			}
//		}
//
//		doc.getLignesTVA().clear();
//		for (String codeTva : mtTVA.keySet()) {
//			LigneTva ligneTva = new LigneTva();
//			ligneTva.setCodeTva(codeTva);
//			ligneTva.setTauxTva(tauxTVA.get(codeTva));
//			ligneTva.setMtTva(mtTVA.get(codeTva));
//			ligneTva.setMontantTotalHt(montantTotalHt.get(codeTva));
//			ligneTva.setMontantTotalTtc(montantTotalTtc.get(codeTva));
//			ligneTva.setMontantTotalHtAvecRemise(montantTotalHtAvecRemise.get(codeTva));
//			ligneTva.setMontantTotalTtcAvecRemise(montantTotalTtcAvecRemise.get(codeTva));
//			ligneTva.setLibelle(taTvaDAO.findByCode(codeTva).getLibelleTva());
//			ligneTva.setNbLigneDocument(nbLigne.get(codeTva));
//			doc.getLignesTVA().add(ligneTva);
//		}
//		
//		//dispatcherTva();
	}
	
	/**
	 * Calcul des totaux de la facture
	 */
	public void calculTotaux(TaReglement doc) {
//		
////			    MT_TVA Numeric(15,2),
//		doc.setMtHtCalc(new BigDecimal(0));
//			doc.setNetHtCalc(new BigDecimal(0));
//			doc.setMtTtcCalc(new BigDecimal(0));
//			doc.setMtTtcAvantRemiseGlobaleCalc(new BigDecimal(0));
//			for (Object ligne : doc.getLignes()) {
//				if(((TaLAvoir)ligne).getTaTLigne().getCodeTLigne().equals(SWTDocument.C_CODE_T_LIGNE_H)) {
//					if(((TaLAvoir)ligne).getMtHtLApresRemiseGlobaleDocument()!=null)
//						doc.setNetHtCalc(doc.getNetHtCalc().add(((TaLAvoir)ligne).getMtHtLApresRemiseGlobaleDocument()));
//					if(((TaLAvoir)ligne).getMtTtcLApresRemiseGlobaleDocument()!=null)
//						doc.setMtTtcCalc(doc.getMtTtcCalc().add(((TaLAvoir)ligne).getMtTtcLApresRemiseGlobaleDocument()));
//					if(((TaLAvoir)ligne).getMtHtLDocument()!=null)
//						doc.setMtHtCalc(doc.getMtHtCalc().add(((TaLAvoir)ligne).getMtHtLDocument()));
//					if(((TaLAvoir)ligne).getMtTtcLDocument()!=null)
//						doc.setMtTtcAvantRemiseGlobaleCalc(doc.getMtTtcAvantRemiseGlobaleCalc().add(((TaLAvoir)ligne).getMtTtcLDocument()));
//				}
//				
//			}
//			
//			doc.setNetTvaCalc(doc.getMtTtcCalc().subtract(doc.getNetHtCalc()));
//			BigDecimal tva = new BigDecimal(0);
//			for (LigneTva ligneTva : doc.getLignesTVA()) {
//				tva = tva.add(ligneTva.getMtTva());
//			}
//			if(tva.compareTo(doc.getNetTvaCalc())!=0) {
//				logger.error("Montant de la TVA incorrect : "+doc.getNetTvaCalc()+" ** "+tva);
//			}
//			
//			//setNetTtcCalc(getMtTtcCalc().subtract(getMtTtcCalc().multiply(getRemTtcFacture().divide(new BigDecimal(100)))));
//			//setNetTtcCalc(getMtTtcCalc().subtract(getMtTtcCalc().multiply(getTxRemTtcDocument().divide(new BigDecimal(100)))));
//			doc.setNetTtcCalc(doc.getMtTtcCalc().subtract(doc.getMtTtcCalc().multiply(doc.getTxRemTtcDocument().divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_HALF_UP)));
//			
//			/*
//			 * remise HT déjà calculée dans dispatcherTva()
//			 */
//			//setRemTtcDocument(getMtTtcCalc().subtract(getNetTtcCalc()));
//			doc.setRemTtcDocument(doc.getMtTtcCalc().subtract(doc.getNetTtcCalc()).setScale(2,BigDecimal.ROUND_HALF_UP));
//			
//			doc.setNetAPayer(doc.getNetTtcCalc().subtract(doc.getRegleDocument()));
//			
//			//TODO A Finir ou a supprimer
////			select sum(f.mt_tva_recup) from calcul_tva_direct(:module,:id_document,:taux_r_ht,:ttc) f into :MTNETTVA;
////			tva=:mtnettva;
////			mt_ttc=:totalttc;
////			mt_tva=:mt_ttc-:mt_ht;
////			if (ttc=1) {
////			       txremiseht = taux_r_ht;
////			       mtnetttc=:mt_ttc - (:mt_ttc*(:txremiseht/100));
////			       MTNETHT=:mtnetttc - :MTNETTVA;
////			       remise_ht =  :totalttc - :mtnetttc ;
////			} else {
////			      txremiseht = taux_r_ht;
////			      MTNETHT=:mt_ht-(:mt_ht*(:txremiseht/100));
////			      mtnetttc=:MTNETHT + :MTNETTVA;
////			      remise_ht = mt_ht - mtnetht;
////			}
////			  txremisettc = taux_r_ttc;
////			  remise_ttc = (:mtnetttc * (:txremisettc/100));
////			  mtnetttc = :mtnetttc -:remise_ttc;
////			  netapayer = :mtnetttc - :regle;
	}
	
	public void calculeTvaEtTotaux(TaReglement doc){
		calculTvaTotal(doc);
		calculTotaux(doc);
	}
	
	public void calculDateEcheanceAbstract(TaReglement doc,Integer report, Integer finDeMois){
		calculDateEcheance(doc,report,finDeMois);
	}
	public Date calculDateEcheance(TaReglement doc,Integer report, Integer finDeMois) {
		TaTCPaiement typeCP = taTCPaiementDAO.findByCode(TaTCPaiement.C_CODE_TYPE_AVOIR);
		TaCPaiement conditionDoc = null;
		TaCPaiement conditionTiers = null;
		TaCPaiement conditionSaisie = null;
		
		if(typeCP!=null) conditionDoc = typeCP.getTaCPaiement();
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
		doc.setDateEchDocument(nouvelleDate);
		return nouvelleDate;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaReglement transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaReglement transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaReglement persistentInstance) throws RemoveException {
		try {
			if(taAutorisationDossierService.autoriseMenu(IModulesProgramme.ID_MODULE_ABONNEMENT, sessionInfo.getUtilisateur())) {
				//taLigneALigneEcheanceService.removeAllByIdDocumentAndTypeDoc(persistentInstance.getIdDocument(), *.TYPE_DOC);
			}
			persistentInstance=findById(persistentInstance.getIdDocument());
			
			List<IDocumentTiers> listeLien = new LinkedList<>();
			for (TaRReglement o : persistentInstance.getTaRReglements()) {
				if(o.getTaFacture()!=null) {
					TaFacture fac = taFactureService.findByIDFetch(o.getTaFacture().getIdDocument());
					for (TaRReglement rr : fac.getTaRReglements()) {
						if(rr.getId()==o.getId())
							fac.removeReglement(rr);
						listeLien.add(fac);
					}
				}
			}
			
			dao.remove(persistentInstance);
			
			mergeEntityLieParLigneALigneReglement(listeLien,true);
			
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
	public TaReglement merge(TaReglement detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaReglement merge(TaReglement detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		TaReglement objInitial = detachedInstance;
		
		if(detachedInstance.getNetTtcCalc()==null)detachedInstance.setNetTtcCalc(BigDecimal.ZERO);
		
		try {
			if(detachedInstance.getIdDocument()!=0)
				objInitial=findById(detachedInstance.getIdDocument());
		} catch (FinderException e) {
			// TODO Auto-generated catch block
		}
		
		List<IDocumentTiers>  listeLien = new LinkedList<>();
		try {
			for (TaRReglement o : objInitial.getTaRReglements()) {
				TaFacture fac = taFactureService.findByIDFetch(o.getTaFacture().getIdDocument());
				for (TaRReglement rr : fac.getTaRReglements()) {
					if(rr.getId()==o.getId())
						fac.removeReglement(rr);
					listeLien.add(fac);
				}
				
			}
			
			for (TaRReglement o : detachedInstance.getTaRReglements()) {
				TaFacture fac = taFactureService.findByIDFetch(o.getTaFacture().getIdDocument());
				if(o.getTaFacture()!=null) { //s'il y a un lien avec une facture
					for (TaRReglement rr : fac.getTaRReglements()) {//parcours les rreglements pour voir s'il n'existe pas déjà
						if(rr.getId()==o.getId())//si le lien exite, on l'enlève pour qu'il soit remplacé par nouvelle valeur
							fac.removeReglement(rr);
					}					
					fac.addRReglement(o);
					listeLien.add(fac);
				}
			}
			
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		detachedInstance= dao.merge(detachedInstance);
		
		mergeEntityLieParLigneALigneReglement(listeLien,true);
		for (IDocumentTiers taFacture : listeLien) {
			taFactureService.mergeEtat(taFacture);
		}
		
		return detachedInstance;
	}

	public TaReglement findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaReglement findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaReglement> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaReglementDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaReglementDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaReglement> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaReglementDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaReglementDTO entityToDTO(TaReglement entity) {
//		ReglementDTO dto = new ReglementDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaReglementMapper mapper = new TaReglementMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaReglementDTO> listEntityToListDTO(List<TaReglement> entity) {
		List<TaReglementDTO> l = new ArrayList<TaReglementDTO>();

		for (TaReglement taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaReglementDTO> selectAllDTO() {
		System.out.println("List of ReglementDTO EJB :");
		ArrayList<TaReglementDTO> liste = new ArrayList<TaReglementDTO>();

		List<TaReglement> projects = selectAll();
		for(TaReglement project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaReglementDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaReglementDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaReglementDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaReglementDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaReglementDTO dto, String validationContext) throws EJBException {
		try {
			TaReglementMapper mapper = new TaReglementMapper();
			TaReglement entity = null;
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
	
	public void persistDTO(TaReglementDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaReglementDTO dto, String validationContext) throws CreateException {
		try {
			TaReglementMapper mapper = new TaReglementMapper();
			TaReglement entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaReglementDTO dto) throws RemoveException {
		try {
			TaReglementMapper mapper = new TaReglementMapper();
			TaReglement entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaReglement refresh(TaReglement persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaReglement value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaReglement value, String propertyName, String validationContext) {
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
	public void validateDTO(TaReglementDTO dto, String validationContext) {
		try {
			TaReglementMapper mapper = new TaReglementMapper();
			TaReglement entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<ReglementDTO> validator = new BeanValidator<ReglementDTO>(ReglementDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaReglementDTO dto, String propertyName, String validationContext) {
		try {
			TaReglementMapper mapper = new TaReglementMapper();
			TaReglement entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<ReglementDTO> validator = new BeanValidator<ReglementDTO>(ReglementDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaReglementDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaReglementDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaReglement value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaReglement value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}
	
	@Override
	public List<TaReglement> rechercheDocument(Date dateDeb, Date dateFin) {
		return dao.rechercheDocument(dateDeb, dateFin);
	}

	@Override
	public List<Object[]> rechercheDocument(Date dateDeb, Date dateFin,
			Boolean light) {
		return dao.rechercheDocument(dateDeb, dateFin, light);
	}

	@Override
	public List<TaReglement> rechercheDocument(String codeDeb, String codeFin) {
		return dao.rechercheDocument(codeDeb, codeFin);
	}

	@Override
	public List<TaReglement> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers) {
		return dao.rechercheDocument(dateDeb, dateFin, codeTiers);
	}

	@Override
	public List<Object[]> rechercheDocumentLight(Date dateDeb, Date dateFin,
			String codeTiers) {
		return dao.rechercheDocumentLight(dateDeb, dateFin, codeTiers);
	}
	
	@Override
	public List<TaReglementDTO> rechercheDocumentDTO(Date dateDeb, Date dateFin,String codeTiers) {
		return dao.rechercheDocumentDTO(dateDeb, dateFin, codeTiers);
	}

	@Override
	public List<Object[]> rechercheDocumentLight(String codeDoc,
			String codeTiers) {
		return dao.rechercheDocumentLight(codeDoc, codeTiers);
	}

	@Override
	public List<TaReglement> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers) {
		return dao.rechercheDocument(codeDeb, codeFin, codeTiers);
	}

	@Override
	public List<TaReglement> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers, Boolean export) {
		// TODO Auto-generated method stub
		return dao.rechercheDocument(dateDeb, dateFin, codeTiers, export);
	}

	@Override
	public List<TaReglement> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers, Boolean export) {
		// TODO Auto-generated method stub
		return dao.rechercheDocument(codeDeb, codeFin, codeTiers, export);
	}

	@Override
	public List<TaReglement> selectAll(IDocumentTiers taDocument, Date dateDeb,
			Date dateFin) {
		// TODO Auto-generated method stub
		return dao.selectAll(taDocument, dateDeb, dateFin);
	}

	@Override
	public TaReglement findByIdDocument(int id) throws FinderException {
		return findById(id);
	}
	
	public String genereCode( Map<String, String> params) {
		//return dao.genereCode();
		try {
			return gencode.genereCodeJPA(TaReglement.class.getSimpleName(),params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NOUVEAU CODE";
	}
	public void annuleCode(String code) {
		try {
			
			gencode.annulerCodeGenere(gencode.findByCode(TaReglement.class.getSimpleName()),code);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void verrouilleCode(String code) {
		try {
			gencode.rentreCodeGenere(gencode.findByCode(TaReglement.class.getSimpleName()),code, sessionInfo.getSessionID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public List<TaReglement> rechercheDocumentNonExporte(Date dateDeb, Date dateFin, boolean parDate) {
		// TODO Auto-generated method stub
		return dao.rechercheDocumentNonExporte(dateDeb, dateFin, parDate);
	}

	@Override
	public List<TaReglementDTO> rechercheReglementNonRemisesDTO(String codeTiers, Date dateDeb, Date DateFin, Boolean export,
			String codeTPaiement, String ibanBancaire, boolean byDate,int nbLigneMax) {
		// TODO Auto-generated method stub
		return dao.rechercheReglementNonRemisesDTO(codeTiers, dateDeb, DateFin, export, codeTPaiement, ibanBancaire, byDate, nbLigneMax);
	}
	
	@Override
	public List<TaReglement> rechercheReglementNonRemises(String codeTiers, Date dateDeb, Date DateFin, Boolean export,
			String codeTPaiement, String ibanBancaire, boolean byDate,int nbLigneMax) {
		// TODO Auto-generated method stub
		return dao.rechercheReglementNonRemises(codeTiers, dateDeb, DateFin, export, codeTPaiement, ibanBancaire, byDate, nbLigneMax);
	}

	@Override
	public List<TaReglementDTO> rechercheDocumentNonExporteLight(Date dateDeb, Date dateFin, boolean parDate) {
		// TODO Auto-generated method stub
		return dao.rechercheDocumentNonExporteLight(dateDeb, dateFin, parDate);
	}

	@Override
	public List<TaReglementDTO> selectReglementNonLieAuDocument(TaFactureDTO taDocument, Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return dao.selectReglementNonLieAuDocument(taDocument, dateDeb, dateFin);
	}

	@Override
	public List<TaReglementDTO> selectAllLieAuDocument(TaFactureDTO taDocument, Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return dao.selectAllLieAuDocument(taDocument, dateDeb, dateFin);
	}

	@Override
	public TaReglement findByCodeAcompte(String code) {
		// TODO Auto-generated method stub
		return dao.findByCodeAcompte(code);
	}

	@Override
	public TaReglement findByCodePrelevement(String code) {
		// TODO Auto-generated method stub
		return dao.findByCodePrelevement(code);
	}

	@Override
	public int selectCountDisponible(TaTiers taTiers) {
		// TODO Auto-generated method stub
		return dao.selectCountDisponible(taTiers);
	}

	@Override
	public List<TaReglementDTO> rechercheDocumentDTO(String codeTiers) {
		// TODO Auto-generated method stub
		return dao.rechercheDocumentDTO(codeTiers);
	}


	@Override
	public List<TaReglement> rechercheDocumentVerrouille(Date dateDeb, Date dateFin, String codeTiers, Boolean verrouille) {
		// TODO Auto-generated method stub
		return dao.rechercheDocumentVerrouille(dateDeb, dateFin, codeTiers, verrouille);
	}

	@Override
	public List<TaReglement> rechercheDocumentVerrouille(String codeDeb, String codeFin, String codeTiers,
			Boolean verrouille) {
		// TODO Auto-generated method stub
		return dao.rechercheDocumentVerrouille(codeDeb, codeFin, codeTiers, verrouille);
	}

	@Override
	public List<TaReglement> rechercheDocument(String codeTiers, Date dateExport, Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return dao.rechercheDocument(dateExport, codeTiers, dateDeb, dateFin);
	}

	@Override
	public DocumentChiffreAffaireDTO chiffreAffaireNonTransformeARelancerTotalDTO(Date dateDebut, Date dateFin,
			int deltaNbJours,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.chiffreAffaireNonTransformeARelancerTotalDTO(dateDebut, dateFin, deltaNbJours, codeTiers);
	}

	@Override
	public DocumentChiffreAffaireDTO chiffreAffaireNonTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.chiffreAffaireNonTransformeTotalDTO(dateDebut, dateFin, codeTiers);
	}

	@Override
	public DocumentChiffreAffaireDTO chiffreAffaireTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.chiffreAffaireTotalDTO(dateDebut, dateFin, codeTiers);
	}

	@Override
	public DocumentChiffreAffaireDTO chiffreAffaireTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.chiffreAffaireTransformeTotalDTO(dateDebut, dateFin, codeTiers);
	}

	// début requête dashboard
	//	@Override
	//	public long countFacture(Date debut, Date fin) {
	//		return dao.countFacture(debut, fin);
	//	}
	//	@Override
	//	public long countFactureNonPaye(Date debut, Date fin) {
	//		return dao.countFactureNonPayes(debut, fin);
	//	}
	//
	//	@Override
	//	public long countFactureNonPayesARelancer(Date debut, Date fin, int deltaNbJours){
	//		return dao.countFactureNonPayeARelancer(debut, fin, deltaNbJours);
	//	}
	//	
	//	@Override
	//	public long countFacturePayes(Date debut, Date fin){
	//		return dao.countFacturePayes(debut, fin);
	//	}
		
		@Override
		public long countDocument(Date debut, Date fin,String codeTiers) {
			return dao.countDocument(debut, fin, codeTiers);
		}

	@Override
	public long countDocumentNonTransforme(Date debut, Date fin,String codeTiers) {
		return dao.countDocumentNonTransforme(debut, fin, codeTiers);
	}

	@Override
	public long countDocumentNonTransformeARelancer(Date debut, Date fin, int deltaNbJours,String codeTiers){
		return dao.countDocumentNonTransformeARelancer(debut, fin, deltaNbJours, codeTiers);
	}

	@Override
	public long countDocumentTransforme(Date debut, Date fin,String codeTiers){
		return dao.countDocumentTransforme(debut, fin, codeTiers);
	}

	@Override
	public List<DocumentDTO> findAllDTOPeriode(Date dateDebut, Date dateFin,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.findAllDTOPeriode(dateDebut, dateFin, codeTiers);
	}

	@Override
	public List<DocumentDTO> findDocumentNonTransfosARelancerDTO(Date dateDebut, Date dateFin, int deltaNbJours,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.findDocumentNonTransfosARelancerDTO(dateDebut,dateFin,deltaNbJours,codeTiers);
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
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeARelancerJmaDTO(Date dateDebut, Date dateFin,
			int precision, int deltaNbJours,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireNonTransformeARelancerJmaDTO(dateDebut, dateFin, precision, deltaNbJours, codeTiers);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeARelancerTotalDTO(Date dateDebut,
			Date dateFin, int deltaNbJours,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireNonTransformeARelancerTotalDTO(dateDebut, dateFin, deltaNbJours, codeTiers);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeJmaDTO(Date dateDebut, Date dateFin,
			int precision,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireNonTransformeJmaDTO(dateDebut, dateFin, precision, codeTiers);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireNonTransformeTotalDTO(dateDebut, dateFin, codeTiers);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireTotalDTO(dateDebut, dateFin, codeTiers);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalJmaDTO(Date dateDebut, Date dateFin, int precision,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireTotalJmaDTO(dateDebut, dateFin, precision, codeTiers);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalSansAvoirDTO(Date dateDebut, Date dateFin,
			String codeTiers) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireTotalSansAvoirDTO(dateDebut, dateFin, codeTiers);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTransformeJmaDTO(Date dateDebut, Date dateFin,
			int precision,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireTransformeJmaDTO(dateDebut, dateFin, precision, codeTiers);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireTransformeTotalDTO(dateDebut, dateFin, codeTiers);
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
	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransformeARelancer(Date debut, Date fin, int deltaNbJours) {
		// TODO Auto-generated method stub
		return dao.findArticlesParTiersNonTransformeARelancer(debut, fin, deltaNbJours);
	}

	@Override
	public DocumentChiffreAffaireDTO chiffreAffaireTotalDTOParTypeRegroupement(Date dateDebut, Date dateFin,
			String codeTiers, String typeRegroupement, Object valeurRegroupement, boolean regrouper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTOParTypeRegroupement(Date dateDebut, Date dateFin,
			String codeTiers, String typeRegroupement, Object valeurRegroupement, boolean regrouper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalJmaDTOParRegroupement(Date dateDebut, Date dateFin,
			int precision, String codeTiers, String typeRegroupement, Object valeurRegroupement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> chiffreAffaireParEtat(Date debut, Date fin, String codeTiers,
			Object codeEtat, String typeRegroupement, Object valeurRegroupement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> countDocumentAndCodeEtatParTypeRegroupement(Date debut, Date fin,
			String codeTiers, Object codeEtat, String typeRegroupement, Object valeurRegroupement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentDTO> findAllDTOPeriodeAndCodeEtatParTypeRegroupement(Date debut, Date fin, String codeTiers,
			Object codeEtat, String typeRegroupement, Object valeurRegroupement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersMoisAndCodeEtatParTypeRegroupement(Date debut, Date fin,
			Object codeEtat, String typeRegroupement, Object valeurRegroupement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTOAndCodeEtatParTypeRegroupement(Date dateDebut,
			Date dateFin, String codeTiers, Object codeEtat, String typeRegroupement, Object valeurRegroupement,
			boolean regrouper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersParMois(Date debut, Date fin, String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersTransforme(Date debut, Date fin, String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransforme(Date debut, Date fin, String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransformeARelancer(Date debut, Date fin,
			int deltaNbJours, String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersParMoisParTypeRegroupement(Date dateDebut, Date dateFin,
			String codeTiers, String typeRegroupement, Object valeurRegroupement, boolean regroupee) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersParMoisParTypeRegroupement(Date dateDebut, Date dateFin,
			String typeRegroupement, Object valeurRegroupement) {
		// TODO Auto-generated method stub
		return null;
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
	public List<TaReglementDTO> rechercheDocumentDTO(Date dateExport, String codeTiers, Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return dao.rechercheDocumentDTO(dateExport, codeTiers, dateDeb, dateFin);
	}


	@Override
	public List<TaReglementDTO> rechercheDocumentDTO(Date dateDeb, Date dateFin,String codeTiers, Boolean export){
		// TODO Auto-generated method stub
		return dao.rechercheDocumentDTO(dateDeb, dateFin, codeTiers, export);
	}

	@Override
	public List<TaReglementDTO> rechercheDocumentDTO(String codeDeb, String codeFin) {
		// TODO Auto-generated method stub
		return dao.rechercheDocumentDTO(codeDeb, codeFin);
	}

	
	@Override
	public Date selectMinDateDocumentNonExporte(Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return dao.selectMinDateDocumentNonExporte(dateDeb, dateFin);
	}

	@Override
	public int findDocByLDocDTO(ILigneDocumentTiers lDoc) {
		// TODO Auto-generated method stub
		return 0;
	}

//	@Transactional(value=TxType.REQUIRES_NEW)
	public  void  mergeEntityLieParLigneALigneReglement(List<IDocumentTiers>listeLien,boolean rechargerDoc) {
        logger.debug("mergeEntityLieParLigneALigne");
        try {
            int idDoc;
            List<Integer> listeDoc=new LinkedList<>();
              for (IDocumentTiers doc : listeLien) {

                    if(doc instanceof TaFacture) {
//                    	idDoc=taFactureServiceRemote.findDocByLDocDTO(ldoc);     
                          if( !listeDoc.contains(doc.getIdDocument())) {
//                        	  if(rechargerDoc) doc=taFactureServiceRemote.findDocByLDoc(ldoc);
                        	  taFactureService.mergeEtat((TaFacture) doc);
                        	  listeDoc.add(doc.getIdDocument());
                          }
                    }

              }
        } catch (RuntimeException re) {
              RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
              logger.error("mergeEntityLieParLigneALigne failed", re);
              throw re2;
//        } catch (FinderException e) {
//            logger.error("mergeEntityLieParLigneALigne failed", e);
		} 
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
