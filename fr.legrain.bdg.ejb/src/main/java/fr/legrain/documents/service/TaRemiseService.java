package fr.legrain.documents.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
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
import fr.legrain.bdg.documents.service.remote.ITaRemiseServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaRemiseMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.dto.TaRemiseDTO;
import fr.legrain.document.model.LigneTva;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaLAvoir;
import fr.legrain.document.model.TaRemise;
import fr.legrain.documents.dao.IRemiseDAO;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibCalcul;
import fr.legrain.tiers.dao.ITCPaiementDAO;
import fr.legrain.tiers.model.TaCPaiement;
import fr.legrain.tiers.model.TaTCPaiement;

/**
 * Session Bean implementation class TaRemiseBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaRemiseService extends AbstractApplicationDAOServer<TaRemise, TaRemiseDTO> implements ITaRemiseServiceRemote {

	static Logger logger = Logger.getLogger(TaRemiseService.class);

	@Inject private IRemiseDAO dao;
	@Inject private ITvaDAO taTvaDAO;
	@Inject private ITCPaiementDAO taTCPaiementDAO;
	@EJB private ITaGenCodeExServiceRemote gencode;
	@Inject private	SessionInfo sessionInfo;
	/**
	 * Default constructor. 
	 */
	public TaRemiseService() {
		super(TaRemise.class,TaRemiseDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaRemise a";
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


	/**
	 * Repartir le total chaque code TVA sur l'ensemble des lignes concernées par ce code. 
	 */
	public void dispatcherTva(TaRemise doc) {
		
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
	public BigDecimal prorataMontantTVALigne(TaRemise doc, TaLAvoir ligne, LigneTva ligneTva) {
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
	public BigDecimal resteTVA(TaRemise doc, LigneTva ligneTva) {
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
	public void calculMontantLigneDocument(TaRemise doc) {
//		for (Object ligne : doc.getLignes()) {
//			((TaLAvoir)ligne).calculMontant();
//		}
	}
	
	/**
	 * Calcul de la grille de TVA en fonction de lignes du document et du taux de remise HT global.
	 * Mise à jour de la propriété <code>lignesTVA</code>
	 * et mise à jour du montant de la TVA dans les lignes du document
	 */
	public void calculTvaTotal(TaRemise doc) {
		calculMontantLigneDocument(doc);
		calculLignesTva(doc);
		dispatcherTva(doc);
	}
	
	/**
	 * Calcul de la grille de TVA en fonction de lignes du document et du taux de remise HT global.
	 * Mise à jour de la propriété <code>lignesTVA</code>
	 */
	public void calculLignesTva(TaRemise doc) {

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
	public void calculTotaux(TaRemise doc) {
		
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
	
	public void calculeTvaEtTotaux(TaRemise doc){
		calculTvaTotal(doc);
		calculTotaux(doc);
	}
	
	public void calculDateEcheanceAbstract(TaRemise doc,Integer report, Integer finDeMois){
		calculDateEcheance(doc,report,finDeMois);
	}
	public Date calculDateEcheance(TaRemise doc,Integer report, Integer finDeMois) {
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
	
	public void persist(TaRemise transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaRemise transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaRemise persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdDocument()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaRemise merge(TaRemise detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaRemise merge(TaRemise detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaRemise findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaRemise findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaRemise> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaRemiseDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaRemiseDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaRemise> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaRemiseDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaRemiseDTO entityToDTO(TaRemise entity) {
//		RemiseDTO dto = new RemiseDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaRemiseMapper mapper = new TaRemiseMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaRemiseDTO> listEntityToListDTO(List<TaRemise> entity) {
		List<TaRemiseDTO> l = new ArrayList<TaRemiseDTO>();

		for (TaRemise taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaRemiseDTO> selectAllDTO() {
		System.out.println("List of RemiseDTO EJB :");
		ArrayList<TaRemiseDTO> liste = new ArrayList<TaRemiseDTO>();

		List<TaRemise> projects = selectAll();
		for(TaRemise project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaRemiseDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaRemiseDTO findByCodeDTO(String code) throws FinderException {
		return dao.findByCodeDTO(code);
	}

	@Override
	public void error(TaRemiseDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaRemiseDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaRemiseDTO dto, String validationContext) throws EJBException {
		try {
			TaRemiseMapper mapper = new TaRemiseMapper();
			TaRemise entity = null;
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
	
	public void persistDTO(TaRemiseDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaRemiseDTO dto, String validationContext) throws CreateException {
		try {
			TaRemiseMapper mapper = new TaRemiseMapper();
			TaRemise entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaRemiseDTO dto) throws RemoveException {
		try {
			TaRemiseMapper mapper = new TaRemiseMapper();
			TaRemise entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaRemise refresh(TaRemise persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaRemise value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaRemise value, String propertyName, String validationContext) {
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
	public void validateDTO(TaRemiseDTO dto, String validationContext) {
		try {
			TaRemiseMapper mapper = new TaRemiseMapper();
			TaRemise entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<RemiseDTO> validator = new BeanValidator<RemiseDTO>(RemiseDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaRemiseDTO dto, String propertyName, String validationContext) {
		try {
			TaRemiseMapper mapper = new TaRemiseMapper();
			TaRemise entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<RemiseDTO> validator = new BeanValidator<RemiseDTO>(RemiseDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaRemiseDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaRemiseDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaRemise value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaRemise value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}
	
	@Override
	public List<TaRemise> rechercheDocument(Date dateDeb, Date dateFin) {
		return dao.rechercheDocument(dateDeb, dateFin);
	}

	@Override
	public List<Object[]> rechercheDocument(Date dateDeb, Date dateFin,
			Boolean light) {
		return dao.rechercheDocument(dateDeb, dateFin, light);
	}

	@Override
	public List<TaRemise> rechercheDocument(String codeDeb, String codeFin) {
		return dao.rechercheDocument(codeDeb, codeFin);
	}

	@Override
	public List<TaRemise> rechercheDocument(Date dateDeb, Date dateFin,
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
	public List<TaRemise> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers) {
		return dao.rechercheDocument(codeDeb, codeFin, codeTiers);
	}

	@Override
	public List<TaRemise> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers, Boolean export) {
		// TODO Auto-generated method stub
		return dao.rechercheDocument(dateDeb, dateFin, codeTiers, export);
	}

	@Override
	public List<TaRemise> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers, Boolean export) {
		// TODO Auto-generated method stub
		return dao.rechercheDocument(codeDeb, codeFin, codeTiers, export);
	}

	@Override
	public List<TaRemise> selectAll(IDocumentTiers taDocument, Date dateDeb,
			Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaRemise> findSiReglementDansRemise(String code) {
		return dao.findSiReglementDansRemise(code);
	}

	@Override
	public List<TaRemise> findSiAcompteDansRemise(String code) {
		return dao.findSiAcompteDansRemise(code);
	}

	@Override
	public TaRemise findByIdDocument(int id) throws FinderException {
		return findById(id);
	}

	@Override
	public List<TaRemise> rechercheDocumentNonExporte(Date dateDeb, Date dateFin, boolean parDate) {
		// TODO Auto-generated method stub
		return dao.rechercheDocumentNonExporte(dateDeb, dateFin, parDate);
	}

	@Override
	public List<TaRemise> rechercheDocumentOrderByDate(Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return dao.rechercheDocumentOrderByDate(dateDeb, dateFin);
	}

	
	public String genereCode( Map<String, String> params) {
		//return dao.genereCode();
		try {
			return gencode.genereCodeJPA(TaRemise.class.getSimpleName(),params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NOUVEAU CODE";
	}
	public void annuleCode(String code) {
		try {
			
			gencode.annulerCodeGenere(gencode.findByCode(TaRemise.class.getSimpleName()),code);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void verrouilleCode(String code) {
		try {
			gencode.rentreCodeGenere(gencode.findByCode(TaRemise.class.getSimpleName()),code, sessionInfo.getSessionID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public List<TaRemiseDTO> rechercheDocumentDTO(Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return dao.rechercheDocumentDTO(dateDeb, dateFin);
	}

	@Override
	public List<TaRemiseDTO> RechercheDocumentDTO(String codeDeb, String codeFin) {
		// TODO Auto-generated method stub
		return dao.RechercheDocumentDTO(codeDeb, codeFin);
	}

	@Override
	public List<TaRemise> rechercheDocumentVerrouille(Date dateDeb, Date dateFin, String codeTiers, Boolean verrouille) {
		// TODO Auto-generated method stub
		return dao.rechercheDocumentVerrouille(dateDeb, dateFin, codeTiers, verrouille);
	}

	@Override
	public List<TaRemise> rechercheDocumentVerrouille(String codeDeb, String codeFin, String codeTiers,
			Boolean verrouille) {
		// TODO Auto-generated method stub
		return dao.rechercheDocumentVerrouille(codeDeb, codeFin, codeTiers, verrouille);
	}

	@Override
	public List<TaRemise> rechercheDocument(String codeTiers, Date dateExport, Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return dao.rechercheDocument(dateExport, codeTiers, dateDeb, dateFin);
	}

	@Override
	public List<TaRemiseDTO> rechercheDocumentDTO(Date dateExport, String codeTiers, Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return dao.rechercheDocumentDTO(dateExport, codeTiers, dateDeb, dateFin);
	}

	
	

	@Override
	public List<TaRemiseDTO> rechercheDocumentDTO(Date dateDeb, Date dateFin,String codeTiers, Boolean export){
		// TODO Auto-generated method stub
		return dao.rechercheDocumentDTO(dateDeb, dateFin, codeTiers, export);
	}

	@Override
	public List<TaRemiseDTO> rechercheDocumentNonExporteDTO(Date dateDeb, Date dateFin, boolean parDate) {
		// TODO Auto-generated method stub
		return dao.rechercheDocumentNonExporteDTO(dateDeb, dateFin, parDate);
	}

	@Override
	public List<TaRemiseDTO> rechercheDocumentDTO(Date dateDeb, Date dateFin, boolean parDate) {
		// TODO Auto-generated method stub
		return dao.rechercheDocumentDTO(dateDeb, dateFin, parDate);
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

}
