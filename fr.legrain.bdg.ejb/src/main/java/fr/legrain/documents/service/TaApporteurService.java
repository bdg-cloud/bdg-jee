package fr.legrain.documents.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import fr.legrain.bdg.documents.service.remote.ITaApporteurServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaDevisServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLigneALigneEcheanceServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaAutorisationsDossierServiceRemote;
import fr.legrain.bdg.edition.osgi.EditionProgrammeDefaut;
import fr.legrain.bdg.edition.service.remote.ITaActionEditionServiceRemote;
import fr.legrain.bdg.edition.service.remote.ITaEditionServiceRemote;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.model.mapping.mapper.TaApporteurMapper;
import fr.legrain.birt.BirtUtil;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.data.AbstractDocumentService;
import fr.legrain.document.dashboard.dto.TaArticlesParTiersDTO;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaApporteurDTO;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.model.LigneTva;
import fr.legrain.document.model.SWTDocument;
import fr.legrain.document.model.TaAbonnement;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaBoncdeAchat;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaLApporteur;
import fr.legrain.document.model.TaLFacture;
import fr.legrain.document.model.TaMiseADisposition;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.documents.dao.IApporteurDAO;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.droits.model.IModulesProgramme;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.edition.model.TaActionEdition;
import fr.legrain.edition.model.TaEdition;
import fr.legrain.hibernate.multitenant.SchemaResolver;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibCalcul;
import fr.legrain.tiers.dao.ICPaiementDAO;
import fr.legrain.tiers.dao.ICompteBanqueDAO;
import fr.legrain.tiers.dao.ITCPaiementDAO;
import fr.legrain.tiers.model.TaCPaiement;
import fr.legrain.tiers.model.TaTCPaiement;
import fr.legrain.tiers.model.TaTiers;

/**
 * Session Bean implementation class TaApporteurBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaApporteurService extends AbstractDocumentService<TaApporteur, TaApporteurDTO> implements ITaApporteurServiceRemote {

	static Logger logger = Logger.getLogger(TaApporteurService.class);

	@Inject private IApporteurDAO dao;
	@Inject private ITvaDAO taTvaDAO;
	@Inject private ITCPaiementDAO taTCPaiementDAO;
	@Inject private ICPaiementDAO taCPaiementDAO;
	@EJB private ITaGenCodeExServiceRemote gencode;
	@Inject private	SessionInfo sessionInfo;
	
	@EJB private ITaActionEditionServiceRemote taActionEditionService;
	@EJB private ITaEditionServiceRemote taEditionService;
	@EJB private ITaLEcheanceServiceRemote taLEcheanceService;
	@EJB private ITaLigneALigneEcheanceServiceRemote taLigneALigneEcheanceService;
	@EJB private ITaAutorisationsDossierServiceRemote taAutorisationDossierService;
	/**
	 * Default constructor. 
	 */
	public TaApporteurService() {
		super(TaApporteur.class,TaApporteurDTO.class);
		editionDefaut = EditionProgrammeDefaut.EDITION_DEFAUT_APPORTEUR_FICHIER;
	}
	
	//	private String defaultJPQLQuery = "select a from TaApporteur a";
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public String genereCode( Map<String, String> params) {
		//return dao.genereCode();
		try {
			return gencode.genereCodeJPA(TaApporteur.class.getSimpleName(),params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NOUVEAU CODE";
	}
	
	public void annuleCode(String code) {
		try {
			
			gencode.annulerCodeGenere(gencode.findByCode(TaApporteur.class.getSimpleName()),code);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void verrouilleCode(String code) {
		try {
			gencode.rentreCodeGenere(gencode.findByCode(TaApporteur.class.getSimpleName()),code, sessionInfo.getSessionID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Reparti le total chaque code TVA sur l'ensemble des lignes concernées par ce code. 
	 */
	public void dispatcherTva(TaApporteur doc) {

		BigDecimal tvaLigne = new BigDecimal(0);
		BigDecimal totalTemp = new BigDecimal(0);

		for (Object ligne : doc.getLignes()) {
			if(((TaLApporteur)ligne).getMtHtLDocument()!=null)
				totalTemp = totalTemp.add(((TaLApporteur)ligne).getMtHtLDocument());
		}
		if(totalTemp!=null && doc.getTxRemHtDocument()!=null)
			doc.setRemHtDocument(totalTemp.multiply(doc.getTxRemHtDocument().divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_HALF_UP));	
		
		for (TaLApporteur ligne : doc.getLignes()) {
			if(doc.getTxRemHtDocument()!=null && doc.getTxRemHtDocument().signum()>0 && ligne.getMtHtLDocument()!=null  && ligne.getMtTtcLDocument()!=null) {
				if(doc.getTtc()==1){
					((TaLApporteur)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLApporteur)ligne).getMtTtcLDocument().subtract(((TaLApporteur)ligne).getMtTtcLDocument()
							.multiply(doc.getTxRemHtDocument()).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
					((TaLApporteur)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLApporteur)ligne).getMtTtcLApresRemiseGlobaleDocument());
					
				}else{
					((TaLApporteur)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLApporteur)ligne).getMtHtLDocument().subtract(((TaLApporteur)ligne).getMtHtLDocument()
							.multiply(doc.getTxRemHtDocument()).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
					((TaLApporteur)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLApporteur)ligne).getMtHtLApresRemiseGlobaleDocument());	
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
					if(((TaLApporteur)ligne).getTaTLigne().getCodeTLigne().equals(SWTDocument.C_CODE_T_LIGNE_H)) {
						if(((TaLApporteur)ligne).getCodeTvaLDocument()!=null &&
								((TaLApporteur)ligne).getCodeTvaLDocument().equals(ligneTva.getCodeTva())){
							if (ligneTva.getMontantTotalHt().signum()==0) 
								tvaLigne = ((TaLApporteur)ligne).getMtHtLDocument().multiply(ligneTva.getTauxTva()).divide(new BigDecimal(100));
							else {
								if  (lignepasse>= ligneTva.getNbLigneDocument()) 
									tvaLigne = tvaTmp;
								else {
									if(doc.getTtc()==1){
										if(LibCalcul.arrondi(ligneTva.getMontantTotalTtcAvecRemise()).signum()<=0)
											tvaLigne=BigDecimal.valueOf(0);
										else
											tvaLigne = (ligneTva.getMtTva().multiply(((TaLApporteur)ligne).getMtTtcLDocument())).divide(LibCalcul.arrondi(ligneTva.getMontantTotalTtcAvecRemise()),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
									}
									else{
										if(LibCalcul.arrondi(ligneTva.getMontantTotalHtAvecRemise()).signum()<=0)
											tvaLigne =BigDecimal.valueOf(0);
										else
											tvaLigne = (ligneTva.getMtTva().multiply(((TaLApporteur)ligne).getMtHtLDocument())).divide(LibCalcul.arrondi(ligneTva.getMontantTotalHtAvecRemise()),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
									}
								}
							}
							tvaTmp =  tvaTmp.subtract(tvaLigne);
							totalTemp = totalTemp.add(((TaLApporteur)ligne).getMtHtLDocument());

							if(doc.getTxRemHtDocument()!=null && doc.getTxRemHtDocument().signum()>0) {
								if  (lignepasse>= ligneTva.getNbLigneDocument()) {
									((TaLApporteur)ligne).setMtHtLApresRemiseGlobaleDocument(htTmp);
									((TaLApporteur)ligne).setMtTtcLApresRemiseGlobaleDocument(ttcTmp);
								} else {
									((TaLApporteur)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLApporteur)ligne).getMtHtLDocument().subtract(((TaLApporteur)ligne).getMtHtLDocument().multiply(doc.getTxRemHtDocument()).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
									((TaLApporteur)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLApporteur)ligne).getMtTtcLDocument().subtract(((TaLApporteur)ligne).getMtTtcLDocument().multiply(doc.getTxRemHtDocument()).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
								}
								//								ttcTmp =  ttcTmp.subtract(((TaLApporteur)ligne).getMtTtcLFacture());
								//								htTmp =  htTmp.subtract(((TaLApporteur)ligne).getMtHtLFacture());
							} else {
								if(doc.getTtc()==1)
									if  (lignepasse>= ligneTva.getNbLigneDocument()) {
										((TaLApporteur)ligne).setMtHtLApresRemiseGlobaleDocument(htTmp);
									}else{
										((TaLApporteur)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLApporteur)ligne).getMtTtcLDocument().subtract(tvaLigne));
									}
								else
									if  (lignepasse>= ligneTva.getNbLigneDocument()) {
										((TaLApporteur)ligne).setMtTtcLApresRemiseGlobaleDocument(ttcTmp);
									}else {
										((TaLApporteur)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLApporteur)ligne).getMtHtLDocument().add(tvaLigne));
									}

							}
							ttcTmp =  ttcTmp.subtract(((TaLApporteur)ligne).getMtTtcLApresRemiseGlobaleDocument());
							htTmp =  htTmp.subtract(((TaLApporteur)ligne).getMtHtLApresRemiseGlobaleDocument());

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
	public void calculMontantLigneDocument(TaApporteur doc) {
		for (Object ligne : doc.getLignes()) {
			((TaLApporteur)ligne).calculMontant();
		}
	}

	/**
	 * Calcul de la grille de TVA en fonction de lignes du document et du taux de remise HT global.
	 * Mise à jour de la propriété <code>lignesTVA</code>
	 * et mise à jour du montant de la TVA dans les lignes du document
	 */
	public void calculTvaTotal(TaApporteur doc) {
		calculMontantLigneDocument(doc);
		calculLignesTva(doc);
		dispatcherTva(doc);
	}

	/**
	 * Calcul de la grille de TVA en fonction de lignes du document et du taux de remise HT global.
	 * Mise à jour de la propriété <code>lignesTVA</code>
	 */
	public void calculLignesTva(TaApporteur doc) {

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
			//((TaLApporteur)ligne).calculMontant();
			codeTVA = ((TaLApporteur)ligne).getCodeTvaLDocument();
			if(codeTVA!=null && !codeTVA.equals("")) {
				ttcLigne = ((TaLApporteur)ligne).getMtTtcLDocument();
				htLigne = ((TaLApporteur)ligne).getMtHtLDocument();
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
					tauxTVA.put(codeTVA,((TaLApporteur)ligne).getTauxTvaLDocument());
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

	/**
	 * Calcul des totaux de la facture
	 */
	public void calculTotaux(TaApporteur doc) {

		//			    MT_TVA Numeric(15,2),
		doc.setMtHtCalc(new BigDecimal(0));
		doc.setNetHtCalc(new BigDecimal(0));
		doc.setMtTtcCalc(new BigDecimal(0));
		doc.setMtTtcAvantRemiseGlobaleCalc(new BigDecimal(0));
		for (Object ligne : doc.getLignes()) {
			if(((TaLApporteur)ligne).getTaTLigne().getCodeTLigne().equals(SWTDocument.C_CODE_T_LIGNE_H)) {
				if(((TaLApporteur)ligne).getMtHtLApresRemiseGlobaleDocument()!=null)
					doc.setNetHtCalc(doc.getNetHtCalc().add(((TaLApporteur)ligne).getMtHtLApresRemiseGlobaleDocument()));
				if(((TaLApporteur)ligne).getMtTtcLApresRemiseGlobaleDocument()!=null)
					doc.setMtTtcCalc(doc.getMtTtcCalc().add(((TaLApporteur)ligne).getMtTtcLApresRemiseGlobaleDocument()));
				if(((TaLApporteur)ligne).getMtHtLDocument()!=null)
					doc.setMtHtCalc(doc.getMtHtCalc().add(((TaLApporteur)ligne).getMtHtLDocument()));
				if(((TaLApporteur)ligne).getMtTtcLDocument()!=null)
					doc.setMtTtcAvantRemiseGlobaleCalc(doc.getMtTtcAvantRemiseGlobaleCalc().add(((TaLApporteur)ligne).getMtTtcLDocument()));
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

		doc.setNetAPayer(doc.getNetTtcCalc().subtract(doc.getRegleDocument()));

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

	public Date calculDateEcheanceAbstract(TaApporteur doc, Integer report, Integer finDeMois) {
		// TODO Auto-generated method stub
		return calculDateEcheanceAbstract(doc,report,finDeMois,null);
	}
	
	public Date calculDateEcheanceAbstract(TaApporteur doc, Integer report, Integer finDeMois,TaTPaiement taTPaiement) {

		return calculDateEcheance(doc,report,finDeMois,taTPaiement);
	}
	
	public Date calculDateEcheance(TaApporteur doc, Integer report, Integer finDeMois,TaTPaiement taTPaiement) {
		TaTCPaiement typeCP = taTCPaiementDAO.findByCode(TaTCPaiement.C_CODE_TYPE_APORTEUR);
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
		doc.setDateEchDocument(nouvelleDate);
		return nouvelleDate;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaApporteur transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaApporteur transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}
	
	@Override
	public void remove(TaApporteur persistentInstance, boolean recharger) throws RemoveException {
		try {
			if(taAutorisationDossierService.autoriseMenu(IModulesProgramme.ID_MODULE_ABONNEMENT, sessionInfo.getUtilisateur())) {
//				//taLigneALigneEcheanceService.removeAllByIdDocumentAndTypeDoc(persistentInstance.getIdDocument(), *.TYPE_DOC);
			}
			if(recharger)persistentInstance=findByIDFetch(persistentInstance.getIdDocument());
			List<ILigneDocumentTiers> listeLien = recupListeLienLigneALigne(persistentInstance);
			dao.remove(persistentInstance);
			mergeEntityLieParLigneALigne(listeLien);
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
	public void remove(TaApporteur persistentInstance) throws RemoveException {
		 remove(persistentInstance, true);
	}
	
	public TaApporteur merge(TaApporteur detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaApporteur merge(TaApporteur detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);
		for (TaLApporteur l : detachedInstance.getLignes()) {
			l.setMouvementerStock(detachedInstance.getMouvementerStock());
		}
		
		List<ILigneDocumentTiers> listeLien =null;
		TaApporteur objInitial = detachedInstance;
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
		
		detachedInstance=dao.merge(detachedInstance);
		
		mergeEntityLieParLigneALigne(listeLien);
		
		annuleCode(detachedInstance.getCodeDocument());
		return detachedInstance;
	}

	public TaApporteur findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaApporteur findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaApporteur> selectAll() {
		List<TaApporteur> l=dao.selectAll();
		for (TaApporteur o : l) {
			o=(TaApporteur) recupSetREtat(o);
			o=(TaApporteur) recupSetHistREtat(o);
			o=(TaApporteur) recupSetLigneALigne(o);
			o=(TaApporteur) recupSetRDocument(o);
			o=(TaApporteur) recupSetREtatLigneDocuments(o);
			o=(TaApporteur) recupSetHistREtatLigneDocuments(o);
		}
		return l;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaApporteurDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaApporteurDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaApporteur> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaApporteurDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaApporteurDTO entityToDTO(TaApporteur entity) {
//		TaApporteurDTO dto = new TaApporteurDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaApporteurMapper mapper = new TaApporteurMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaApporteurDTO> listEntityToListDTO(List<TaApporteur> entity) {
		List<TaApporteurDTO> l = new ArrayList<TaApporteurDTO>();

		for (TaApporteur taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

	public List<TaApporteurDTO> findAllLight() {
		return dao.findAllLight();
	}
	
//	@RolesAllowed("admin")
	public List<TaApporteurDTO> selectAllDTO() {
		System.out.println("List of TaApporteurDTO EJB :");
		List<TaApporteurDTO> liste = new ArrayList<TaApporteurDTO>();
		
		liste = findAllLight();

//		List<TaFacture> projects = selectAll();
//		for(TaFacture project : projects) {
//			liste.add(entityToDTO(project));
//		}

		return liste;
	}

	public TaApporteurDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaApporteurDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaApporteurDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaApporteurDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaApporteurDTO dto, String validationContext) throws EJBException {
		try {
			TaApporteurMapper mapper = new TaApporteurMapper();
			TaApporteur entity = null;
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
	
	public void persistDTO(TaApporteurDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaApporteurDTO dto, String validationContext) throws CreateException {
		try {
			TaApporteurMapper mapper = new TaApporteurMapper();
			TaApporteur entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaApporteurDTO dto) throws RemoveException {
		try {
			TaApporteurMapper mapper = new TaApporteurMapper();
			TaApporteur entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaApporteur refresh(TaApporteur persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaApporteur value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaApporteur value, String propertyName, String validationContext) {
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
	public void validateDTO(TaApporteurDTO dto, String validationContext) {
		try {
			TaApporteurMapper mapper = new TaApporteurMapper();
			TaApporteur entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaApporteurDTO> validator = new BeanValidator<TaApporteurDTO>(TaApporteurDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaApporteurDTO dto, String propertyName, String validationContext) {
		try {
			TaApporteurMapper mapper = new TaApporteurMapper();
			TaApporteur entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaApporteurDTO> validator = new BeanValidator<TaApporteurDTO>(TaApporteurDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaApporteurDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaApporteurDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaApporteur value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaApporteur value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}
	
	@Override
	public List<TaApporteur> rechercheDocument(Date dateDeb, Date dateFin) {
		return dao.rechercheDocument(dateDeb, dateFin);
	}

	@Override
	public List<Object[]> rechercheDocument(Date dateDeb, Date dateFin,
			Boolean light) {
		return dao.rechercheDocument(dateDeb, dateFin, light);
	}

	@Override
	public List<TaApporteur> rechercheDocument(String codeDeb, String codeFin) {
		return dao.rechercheDocument(codeDeb, codeFin);
	}

	@Override
	public List<TaApporteur> rechercheDocument(Date dateDeb, Date dateFin,
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
	public List<TaApporteur> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers) {
		return dao.rechercheDocument(codeDeb, codeFin, codeTiers);
	}

	@Override
	public List<TaApporteur> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers, Boolean export) {
		// TODO Auto-generated method stub
		return dao.rechercheDocument(dateDeb, dateFin, codeTiers, export);
	}

	@Override
	public List<TaApporteur> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers, Boolean export) {
		// TODO Auto-generated method stub
		return dao.rechercheDocument(codeDeb, codeFin, codeTiers, export);
	}

	@Override
	public List<TaApporteur> selectAll(IDocumentTiers taDocument, Date dateDeb,
			Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaApporteur> findByCodeTiersAndDate(String codeTiers,
			Date debut, Date fin) {
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
	public TaApporteur findByIdDocument(int id) throws FinderException {
		return findById(id);
	}

	@Override
	public TaApporteur mergeAndFindById(TaApporteur detachedInstance, String validationContext) {
		TaApporteur br = merge(detachedInstance,validationContext);
		try {
			br = findByIDFetch(br.getIdDocument()); //pour etre sur que les valeur initialisé par les triggers "qui_cree,..." soit bien affecté avant une éventuelle nouvelle modification du document
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return br;
	}

	@Override
	public List<TaApporteur> rechercheDocumentNonExporte(Date dateDeb, Date dateFin, boolean parDate) {
		// TODO Auto-generated method stub
		return dao.rechercheDocumentNonExporte(dateDeb, dateFin, parDate);
	}

	@Override
	public List<TaApporteur> rechercheDocumentOrderByDate(Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return dao.rechercheDocumentOrderByDate(dateDeb, dateFin);
	}

	@Override
	public List<TaApporteurDTO> rechercheDocumentNonExporteLight(Date dateDeb, Date dateFin, boolean parDate) {
		// TODO Auto-generated method stub
		return dao.rechercheDocumentNonExporteLight(dateDeb, dateFin, parDate);
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
			TaEdition edition = taEditionService.rechercheEditionActionDefaut(null, action, TaApporteur.TYPE_DOC);
			
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
			TaApporteur doc = findById(idDoc);
			SchemaResolver sr = new SchemaResolver();
			String localPath = bdgProperties.osTempDirDossier(sr.resolveCurrentTenantIdentifier())+"/"+bdgProperties.tmpFileName(doc.getCodeDocument(), doc.getTaTiers().getCodeTiers(),"apporteur.pdf");
			System.out.println(localPath);
			System.out.println(bdgProperties.urlDemoHttps()+modeleEdition);
			
			Map<String,Object> mapEdition = new HashMap<String,Object>();
			
			boolean editionClient = false;

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
			List<TaPreferences> liste= taPreferencesService.findByGroupe("apporteur");
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
				listResourcesPath.add(EditionProgrammeDefaut.LIBRAIRIE_EDITION_DEFAUT_APPORTEUR);
			}else if(listResourcesPath.isEmpty()) {
					listResourcesPath = new ArrayList<String>();
					listResourcesPath.add(EditionProgrammeDefaut.LIBRAIRIE_EDITION_DEFAUT_APPORTEUR);
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
//				doc = mergeAndFindById(doc,ITaApporteurServiceRemote.validationContext);
//			}
			 
			 return localPath;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

		@Override
		public List<TaApporteur> rechercheDocumentVerrouille(Date dateDeb, Date dateFin, String codeTiers,
				Boolean verrouille) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<TaApporteur> rechercheDocumentVerrouille(String codeDeb, String codeFin, String codeTiers,
				Boolean verrouille) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<TaApporteur> rechercheDocument(String codeTiers, Date dateExport, Date dateDeb, Date dateFin) {
			// TODO Auto-generated method stub
			return dao.rechercheDocument(dateExport, codeTiers, dateDeb, dateFin);
		}
		/**A PARTIR D'ICI RAJOUT METHODE POUR DASHBOARD YANN**/
		@Override
		public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTO(Date dateDebut, Date dateFin,
				String codeTiers) {
			return dao.listeChiffreAffaireTotalDTO(dateDebut, dateFin, codeTiers);
		}

		@Override
		public DocumentChiffreAffaireDTO chiffreAffaireTotalDTO(Date dateDebut, Date dateFin, String codeTiers) {
			return dao.chiffreAffaireTotalDTO( dateDebut,  dateFin,  codeTiers);
		}

		@Override
		public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTransformeTotalDTO(Date dateDebut, Date dateFin,
				String codeTiers) {
			return dao.listeChiffreAffaireTransformeTotalDTO( dateDebut,  dateFin,
					 codeTiers);
		}

		@Override
		public DocumentChiffreAffaireDTO chiffreAffaireTransformeTotalDTO(Date dateDebut, Date dateFin,
				String codeTiers) {
			return dao.chiffreAffaireTransformeTotalDTO( dateDebut,  dateFin,
					 codeTiers);
		}

		@Override
		public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeTotalDTO(Date dateDebut, Date dateFin,
				String codeTiers) {
			return dao.listeChiffreAffaireNonTransformeTotalDTO( dateDebut,  dateFin,
					 codeTiers);
		}

		@Override
		public DocumentChiffreAffaireDTO chiffreAffaireNonTransformeTotalDTO(Date dateDebut, Date dateFin,
				String codeTiers) {
			return dao.chiffreAffaireNonTransformeTotalDTO( dateDebut,  dateFin,
					 codeTiers);
		}

		@Override
		public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeARelancerTotalDTO(Date dateDebut,
				Date dateFin, int deltaNbJours, String codeTiers) {
			return dao.listeChiffreAffaireNonTransformeARelancerTotalDTO( dateDebut,
					 dateFin,  deltaNbJours,  codeTiers);
		}

		@Override
		public DocumentChiffreAffaireDTO chiffreAffaireNonTransformeARelancerTotalDTO(Date dateDebut, Date dateFin,
				int deltaNbJours, String codeTiers) {
			return dao.chiffreAffaireNonTransformeARelancerTotalDTO( dateDebut,  dateFin,
					 deltaNbJours,  codeTiers);
		}

		@Override
		public long countDocument(Date dateDebut, Date dateFin, String codeTiers) {
			return dao.countDocument( dateDebut,  dateFin,  codeTiers);
		}

		@Override
		public long countDocumentTransforme(Date dateDebut, Date dateFin, String codeTiers) {
			return dao.countDocumentTransforme( dateDebut,  dateFin,  codeTiers);
		}

		@Override
		public long countDocumentNonTransforme(Date dateDebut, Date dateFin, String codeTiers) {
			return dao.countDocumentNonTransforme( dateDebut,  dateFin,  codeTiers);
		}

		@Override
		public long countDocumentNonTransformeARelancer(Date dateDebut, Date dateFin, int deltaNbJours,
				String codeTiers) {
			return dao.countDocumentNonTransformeARelancer( dateDebut,  dateFin,  deltaNbJours,
					 codeTiers);
		}

		@Override
		public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalJmaDTO(Date dateDebut, Date dateFin,
				int precision, String codeTiers) {
			return dao.listeChiffreAffaireTotalJmaDTO(dateDebut, dateFin, precision, codeTiers);
		}

		@Override
		public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeJmaDTO(Date dateDebut, Date dateFin,
				int precision, String codeTiers) {
			return dao.listeChiffreAffaireNonTransformeJmaDTO(dateDebut, dateFin, precision, codeTiers);
		}

		@Override
		public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTransformeJmaDTO(Date dateDebut, Date dateFin,
				int precision, String codeTiers) {
			return dao.listeChiffreAffaireTransformeJmaDTO(dateDebut, dateFin, precision, codeTiers);
		}

		@Override
		public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeARelancerJmaDTO(Date dateDebut,
				Date dateFin, int precision, int deltaNbJours, String codeTiers) {
			return dao.listeChiffreAffaireNonTransformeARelancerJmaDTO(dateDebut, dateFin, precision, deltaNbJours, codeTiers);
		}

		@Override
		public List<DocumentDTO> findAllDTOPeriode(Date dateDebut, Date dateFin, String codeTiers) {
			return dao.findAllDTOPeriode(dateDebut, dateFin, codeTiers);
		}

		@Override
		public List<DocumentDTO> findDocumentNonTransfosDTO(Date dateDebut, Date dateFin, String codeTiers) {
			return dao.findDocumentNonTransfosDTO(dateDebut, dateFin, codeTiers);
		}

		@Override
		public List<DocumentDTO> findDocumentNonTransfosARelancerDTO(Date dateDebut, Date dateFin, int deltaNbJours,
				String codeTiers) {
			return dao.findDocumentNonTransfosARelancerDTO(dateDebut, dateFin, deltaNbJours, codeTiers);
		}

		@Override
		public List<DocumentDTO> findDocumentTransfosDTO(Date dateDebut, Date dateFin, String codeTiers) {
			return dao.findDocumentTransfosDTO(dateDebut, dateFin, codeTiers);
		}

		@Override
		public List<TaArticlesParTiersDTO> findArticlesParTiersParMois(Date debut, Date fin, String codeTiers) {
			return dao.findArticlesParTiersParMois(debut, fin, codeTiers);
		}

		@Override
		public List<TaArticlesParTiersDTO> findArticlesParTiersTransforme(Date debut, Date fin, String codeTiers) {
			return dao.findArticlesParTiersTransforme(debut, fin, codeTiers);
		}

		@Override
		public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransforme(Date debut, Date fin, String codeTiers) {
			return dao.findArticlesParTiersNonTransforme(debut, fin, codeTiers);
		}

		@Override
		public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransformeARelancer(Date debut, Date fin,
				int deltaNbJours, String codeTiers) {
			return dao.findArticlesParTiersNonTransformeARelancer(debut, fin, deltaNbJours, codeTiers);
		}

		@Override
		public List<TaArticlesParTiersDTO> findArticlesParTiersParMoisParTypeRegroupement(Date dateDebut, Date dateFin,
				String codeTiers, String typeRegroupement, Object valeurRegroupement, boolean regroupee) {
			return dao.findArticlesParTiersParMoisParTypeRegroupement(dateDebut, dateFin, codeTiers, typeRegroupement, valeurRegroupement, regroupee);
		}

		@Override
		public List<TaArticlesParTiersDTO> findArticlesParTiersParMois(Date debut, Date fin) {
			return dao.findArticlesParTiersParMois(debut, fin);
		}

		@Override
		public List<TaArticlesParTiersDTO> findArticlesParTiersTransforme(Date debut, Date fin) {
			return dao.findArticlesParTiersTransforme(debut, fin);
		}

		@Override
		public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransforme(Date debut, Date fin) {
			return dao.findArticlesParTiersNonTransforme(debut, fin);
		}

		@Override
		public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransformeARelancer(Date debut, Date fin,
				int deltaNbJours) {
			return dao.findArticlesParTiersNonTransformeARelancer(debut, fin, deltaNbJours);
		}

		@Override
		public List<TaArticlesParTiersDTO> findArticlesParTiersParMoisParTypeRegroupement(Date dateDebut, Date dateFin,
				String typeRegroupement, Object valeurRegroupement) {
			return dao.findArticlesParTiersParMoisParTypeRegroupement(dateDebut, dateFin, typeRegroupement, valeurRegroupement);
		}

		@Override
		public DocumentChiffreAffaireDTO chiffreAffaireTotalDTOParTypeRegroupement(Date dateDebut, Date dateFin,
				String codeTiers, String typeRegroupement, Object valeurRegroupement, boolean regrouper) {
			return dao.chiffreAffaireTotalDTOParTypeRegroupement(dateDebut, dateFin, codeTiers, typeRegroupement, valeurRegroupement, regrouper);
		}

		@Override
		public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTOParTypeRegroupement(Date dateDebut,
				Date dateFin, String codeTiers, String typeRegroupement, Object valeurRegroupement, boolean regrouper) {
			return dao.listeChiffreAffaireTotalDTOParTypeRegroupement(dateDebut, dateFin, codeTiers, typeRegroupement, valeurRegroupement, regrouper);
		}

		@Override
		public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalJmaDTOParRegroupement(Date dateDebut,
				Date dateFin, int precision, String codeTiers, String typeRegroupement, Object valeurRegroupement) {
			return dao.listeChiffreAffaireTotalJmaDTOParRegroupement(dateDebut, dateFin, precision, codeTiers, typeRegroupement, valeurRegroupement);
		}

		@Override
		public List<DocumentChiffreAffaireDTO> chiffreAffaireParEtat(Date debut, Date fin, String codeTiers,
				Object codeEtat, String typeRegroupement, Object valeurRegroupement) {
			return dao.chiffreAffaireParEtat(debut, fin, codeTiers, codeEtat, typeRegroupement, valeurRegroupement);
		}

		@Override
		public List<DocumentChiffreAffaireDTO> listeSumChiffreAffaireTotalDTOArticle(Date dateDebut, Date dateFin,
				String codeArticle, String codeTiers) {
			return dao.listeSumChiffreAffaireTotalDTOArticle(dateDebut, dateFin, codeArticle, codeTiers);
		}

		@Override
		public List<DocumentChiffreAffaireDTO> listeSumChiffreAffaireTotalDTOArticleMois(Date dateDebut, Date dateFin,
				String codeArticle, String codeTiers) {
			return dao.listeSumChiffreAffaireTotalDTOArticleMois(dateDebut, dateFin, codeArticle, codeTiers);
		}

		@Override
		public List<DocumentChiffreAffaireDTO> countChiffreAffaireTotalDTOArticle(Date dateDebut, Date dateFin,
				String codeArticle, String codeTiers) {
			return dao.countChiffreAffaireTotalDTOArticle(dateDebut, dateFin, codeArticle, codeTiers);
		}

		@Override
		public List<DocumentChiffreAffaireDTO> sumChiffreAffaireTotalDTOArticle(Date dateDebut, Date dateFin) {
			return dao.sumChiffreAffaireTotalDTOArticle(dateDebut, dateFin);
		}

		@Override
		public List<DocumentChiffreAffaireDTO> sumChiffreAffaireTotalDTOArticle(Date dateDebut, Date dateFin,
				String codeArticle) {
			return dao.sumChiffreAffaireTotalDTOArticle(dateDebut, dateFin, codeArticle);
		}

		@Override
		public List<DocumentChiffreAffaireDTO> sumChiffreAffaireTotalDTOArticleMoinsAvoir(Date dateDebut, Date dateFin,
				String codeArticle) {
			return dao.sumChiffreAffaireTotalDTOArticleMoinsAvoir(dateDebut, dateFin, codeArticle);
		}

		@Override
		public List<DocumentChiffreAffaireDTO> countDocumentAndCodeEtatParTypeRegroupement(Date debut, Date fin,
				String codeTiers, Object codeEtat, String typeRegroupement, Object valeurRegroupement) {
			return dao.countDocumentAndCodeEtatParTypeRegroupement(debut, fin, codeTiers, codeEtat, typeRegroupement, valeurRegroupement);
		}

		@Override
		public List<DocumentDTO> findAllDTOPeriodeAndCodeEtatParTypeRegroupement(Date debut, Date fin, String codeTiers,
				Object codeEtat, String typeRegroupement, Object valeurRegroupement) {
			return dao.findAllDTOPeriodeAndCodeEtatParTypeRegroupement(debut, fin, codeTiers, codeEtat, typeRegroupement, valeurRegroupement);
		}

		@Override
		public List<TaArticlesParTiersDTO> findArticlesParTiersMoisAndCodeEtatParTypeRegroupement(Date debut, Date fin,
				Object codeEtat, String typeRegroupement, Object valeurRegroupement) {
			return dao.findArticlesParTiersMoisAndCodeEtatParTypeRegroupement(debut, fin, codeEtat, typeRegroupement, valeurRegroupement);
		}

		@Override
		public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTOAndCodeEtatParTypeRegroupement(Date dateDebut,
				Date dateFin, String codeTiers, Object codeEtat, String typeRegroupement, Object valeurRegroupement,
				boolean regrouper) {
			// TODO Auto-generated method stub
			return dao.listeChiffreAffaireTotalDTOAndCodeEtatParTypeRegroupement(dateDebut, dateFin, codeTiers, codeEtat, typeRegroupement, valeurRegroupement, regrouper);
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
		public TaApporteur findDocByLDoc(ILigneDocumentTiers lDoc) {
			TaApporteur o= (TaApporteur) dao.findDocByLDoc(lDoc);
			 recupSetREtat(o);
			 recupSetHistREtat(o);
			 recupSetLigneALigne(o);
			 recupSetRDocument(o);
			 recupSetREtatLigneDocuments(o);
			 recupSetHistREtatLigneDocuments(o);
			return o;
		}


		@Override
		public TaApporteur mergeEtat(IDocumentTiers detachedInstance) {
//			à mettre si gestion des états !!!!
			modifEtatLigne(detachedInstance);		
			TaEtat etat=changeEtatDocument(detachedInstance);
			((TaApporteur) detachedInstance).addREtat(etat);
			
			detachedInstance=dao.merge((TaApporteur) detachedInstance);	
			return (TaApporteur) detachedInstance;
		}
		
		@Override
		public List<TaApporteurDTO> rechercheDocumentDTO(Date dateExport, String codeTiers, Date dateDeb, Date dateFin) {
			// TODO Auto-generated method stub
			return dao.rechercheDocumentDTO(dateExport, codeTiers, dateDeb, dateFin);
		}



		@Override
		public List<TaApporteurDTO> rechercheDocumentDTO(Date dateDeb, Date dateFin,String codeTiers, Boolean export){
			// TODO Auto-generated method stub
			return dao.rechercheDocumentDTO(dateDeb, dateFin, codeTiers, export);
		}


		@Override
		public Date selectMinDateDocumentNonExporte(Date dateDeb, Date dateFin) {
			// TODO Auto-generated method stub
			return dao.selectMinDateDocumentNonExporte(dateDeb, dateFin);
		}

		
		@Override
		public int findDocByLDocDTO(ILigneDocumentTiers lDoc) {
			return dao.findDocByLDocDTO(lDoc);
		}

		@Override
		public TaApporteur findByCodeFetch(String code) throws FinderException {
			// TODO Auto-generated method stub
			TaApporteur o= (TaApporteur) dao.findByCodeFetch(code);
			if(o!=null) {
			recupSetREtat(o);
			recupSetHistREtat(o);
			recupSetLigneALigne(o);
			recupSetRDocument(o);
			recupSetREtatLigneDocuments(o);
			recupSetHistREtatLigneDocuments(o);
			}
			return o;
		}

		@Override
		public TaApporteur findByIDFetch(int id) throws FinderException {
			// TODO Auto-generated method stub
			TaApporteur o= (TaApporteur) dao.findByIdFetch(id);
			if(o!=null) {
			recupSetREtat(o);
			recupSetHistREtat(o);
			recupSetLigneALigne(o);
			recupSetRDocument(o);
			recupSetREtatLigneDocuments(o);
			recupSetHistREtatLigneDocuments(o);
			}
			return o;
		}


		
		@Override
		public TaEtat etatLigneInsertion(TaApporteur masterEntity) {
			// TODO Auto-generated method stub
			return super.etatLigneInsertion(masterEntity);
		}

		@Override
		public TaApporteur mergeEtat(IDocumentTiers detachedInstance, List<IDocumentTiers> avecLien) {
//				à mettre si gestion des états !!!!
				modifEtatLigne(detachedInstance);		
				TaEtat etat=changeEtatDocument(detachedInstance);
				((TaApporteur) detachedInstance).addREtat(etat);
				
				
			
				detachedInstance=dao.merge((TaApporteur) detachedInstance);
				
				if(avecLien!=null) {
					mergeEntityLieParLigneALigneDTO(avecLien);
				}

				return (TaApporteur) detachedInstance;
			}
		
		
		public List<TaApporteur> selectAllFetch() {
			List<TaApporteur> l=dao.selectAll();
			for (TaApporteur o : l) {
				o=(TaApporteur) recupSetREtat(o);
				o=(TaApporteur) recupSetHistREtat(o);
				o=(TaApporteur) recupSetLigneALigne(o);
				o=(TaApporteur) recupSetRDocument(o);
				o=(TaApporteur) recupSetREtatLigneDocuments(o);
				o=(TaApporteur) recupSetHistREtatLigneDocuments(o);
			}
			return l;
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

