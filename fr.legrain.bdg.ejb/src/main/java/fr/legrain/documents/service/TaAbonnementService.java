package fr.legrain.documents.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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
import org.jboss.ejb3.annotation.TransactionTimeout;

import fr.legrain.abonnement.dto.TaAbonnementFullDTO;
import fr.legrain.abonnement.dto.TaJourRelanceDTO;
import fr.legrain.abonnement.model.TaFrequence;
import fr.legrain.abonnement.model.TaJourRelance;
import fr.legrain.abonnement.service.remote.ITaJourRelanceServiceRemote;
import fr.legrain.abonnement.stripe.dao.IStripePlanDAO;
import fr.legrain.abonnement.stripe.model.TaStripePlan;
import fr.legrain.abonnement.stripe.service.MultitenantProxyTimerAbonnement;
import fr.legrain.article.dao.ITvaDAO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAbonnementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLAbonnementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLAvisEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLigneALigneEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLigneALigneServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.edition.osgi.EditionProgrammeDefaut;
import fr.legrain.bdg.model.mapping.mapper.TaAbonnementMapper;
import fr.legrain.birt.BirtUtil;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dashboard.dto.TaArticlesParTiersDTO;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaAbonnementDTO;
import fr.legrain.document.model.LigneTva;
import fr.legrain.document.model.SWTDocument;
import fr.legrain.document.model.TaAbonnement;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaLAbonnement;
import fr.legrain.document.model.TaLAvisEcheance;
import fr.legrain.document.model.TaLEcheance;
import fr.legrain.document.model.TaLigneALigne;
import fr.legrain.document.model.TaLigneALigneEcheance;
import fr.legrain.documents.dao.IAbonnementDAO;
import fr.legrain.documents.dao.IDevisDAO;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.edition.model.TaActionEdition;
import fr.legrain.hibernate.multitenant.SchemaResolver;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibCalcul;
import fr.legrain.tiers.dao.ICPaiementDAO;
import fr.legrain.tiers.dao.ITCPaiementDAO;
import fr.legrain.tiers.model.TaCPaiement;
import fr.legrain.tiers.model.TaTCPaiement;
import fr.legrain.tiers.model.TaTiers;


/**
 * Session Bean implementation class TaAbonnementBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaAbonnementService extends AbstractApplicationDAOServer<TaAbonnement, TaAbonnementDTO> implements ITaAbonnementServiceRemote {

	static Logger logger = Logger.getLogger(TaAbonnementService.class);

	@Inject private IAbonnementDAO dao;
	@Inject private ITvaDAO taTvaDAO;
	@Inject private ITCPaiementDAO taTCPaiementDAO;
	@Inject private ICPaiementDAO taCPaiementDAO;
	@Inject private	SessionInfo sessionInfo;
	@EJB private ITaGenCodeExServiceRemote gencode;
	@EJB private ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	
	@EJB private ITaLEcheanceServiceRemote taLEcheanceService;
	@EJB private ITaLAvisEcheanceServiceRemote taLAvisEcheanceService;
	@EJB private ITaLFactureServiceRemote taLFactureService;
	@EJB private ITaLAbonnementServiceRemote taLAbonnementService;
	@EJB private ITaLigneALigneEcheanceServiceRemote taLigneALigneEcheanceService;
	@EJB private ITaLigneALigneServiceRemote taLigneALigneService;
	@Inject private IStripePlanDAO daoPlan;
	
	@EJB private ITaArticleServiceRemote taArticleService;
	@EJB private ITaJourRelanceServiceRemote taJourRelanceService;
	
	
	
	/**
	 * Default constructor. 
	 */
	public TaAbonnementService() {
		super(TaAbonnement.class,TaAbonnementDTO.class);
		editionDefaut = EditionProgrammeDefaut.EDITION_DEFAUT_DEVIS_FICHIER;
	}
	
	//	private String defaultJPQLQuery = "select a from TaAbonnement a";
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public List<TaAbonnementDTO> findAllEnCoursDTOArrivantAEcheanceDansXJours(Integer nbJours, String codeTiers){
		return dao.findAllEnCoursDTOArrivantAEcheanceDansXJours( nbJours,  codeTiers);
	}
	public List<TaAbonnementFullDTO> findAllFullDTOByIdTiers(Integer idTiers){
		return dao.findAllFullDTOByIdTiers(idTiers);
	}
	public List<TaAbonnementDTO> rechercherAbonnementNonStripeCustomerDTO(String idExterneCustomer){
		return dao.rechercherAbonnementNonStripeCustomerDTO(idExterneCustomer);
	}
	public List<TaAbonnementDTO> rechercherAbonnementCustomerDTO(String idExterneCustomer){
		return dao.rechercherAbonnementCustomerDTO(idExterneCustomer);
	}
	public List<TaAbonnementDTO> rechercherAbonnementCustomerDTO(String idExterneCustomer, Boolean stripe){
		return dao.rechercherAbonnementCustomerDTO(idExterneCustomer, stripe);
	}
	public List<TaAbonnementDTO> selectAllASuspendre(){
		return dao.selectAllASuspendre();
	}
	public TaAbonnement findByIdLEcheance(Integer idLEcheance){
		return dao.findByIdLEcheance(idLEcheance);
	}
	
	public List<TaAbonnement> selectAllAvecFetchLignesEtPlan(){
		return dao.selectAllAvecFetchLignesEtPlan();
	}
	public List<TaAbonnement> findAllEnCours(){
		List<TaAbonnement> liste = dao.findAllEnCours();
//		for (TaAbonnement taAbonnement : liste) {
//			for (TaLAbonnement li : taAbonnement.getLignes()) {
//				li.getTaPlan();
//			}
//		}
		return liste ;
	}
	
	public List<TaAbonnement>  findAllSansDatesPeriode(){
		List<TaAbonnement> liste = dao.findAllSansDatesPeriode();
		return liste ;
	}
	public String genereCode( Map<String, String> params) {
		//return dao.genereCode();
		try {
			return gencode.genereCodeJPA(TaAbonnement.class.getSimpleName(),params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NOUVEAU CODE";
	}
	
	public void annuleCode(String code) {
		try {
			
			gencode.annulerCodeGenere(gencode.findByCode(TaAbonnement.class.getSimpleName()),code);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void verrouilleCode(String code) {
		try {
			gencode.rentreCodeGenere(gencode.findByCode(TaAbonnement.class.getSimpleName()),code, sessionInfo.getSessionID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	public IDocumentTiers calculeTvaEtTotaux(IDocumentTiers doc) {
//		doc.calculeTvaEtTotaux();
//		return doc;
//	}
	
	private TaAbonnement initList(TaAbonnement doc) {
		//Le processus de sérialization affecte null pour une liste vide, donc il faut "recréer" les liste vide
		if(doc.getLignes()==null)
			doc.setLignes(new ArrayList<TaLAbonnement>(0));
		
		if(doc.getLignesTVA()==null)
			doc.setLignesTVA(new ArrayList<LigneTva>());
		
		return doc;
	}
	
	public void calculeTvaEtTotaux(TaAbonnement doc){
		calculTvaTotal(initList(doc));
		calculTotaux(initList(doc));
	}
	
	/**
	 * Calcul des totaux de la facture
	 */
	public void calculTotaux(TaAbonnement doc) {
		
//			    MT_TVA Numeric(15,2),
			doc.setMtHtCalc(new BigDecimal(0));
			doc.setNetHtCalc(new BigDecimal(0));
			doc.setMtTtcCalc(new BigDecimal(0));
			doc.setMtTtcAvantRemiseGlobaleCalc(new BigDecimal(0));
			for (Object ligne : doc.getLignes()) {
				if(((TaLAbonnement)ligne).getTaTLigne().getCodeTLigne().equals(SWTDocument.C_CODE_T_LIGNE_H)) {
					if(((TaLAbonnement)ligne).getMtHtLApresRemiseGlobaleDocument()!=null)
						doc.setNetHtCalc(doc.getNetHtCalc().add(((TaLAbonnement)ligne).getMtHtLApresRemiseGlobaleDocument()));
					if(((TaLAbonnement)ligne).getMtTtcLApresRemiseGlobaleDocument()!=null)
						doc.setMtTtcCalc(doc.getMtTtcCalc().add(((TaLAbonnement)ligne).getMtTtcLApresRemiseGlobaleDocument()));
					if(((TaLAbonnement)ligne).getMtHtLDocument()!=null)
						doc.setMtHtCalc(doc.getMtHtCalc().add(((TaLAbonnement)ligne).getMtHtLDocument()));
					if(((TaLAbonnement)ligne).getMtTtcLDocument()!=null)
						doc.setMtTtcAvantRemiseGlobaleCalc(doc.getMtTtcAvantRemiseGlobaleCalc().add(((TaLAbonnement)ligne).getMtTtcLDocument()));
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

	/**
	 * Reparti le total chaque code TVA sur l'ensemble des lignes concernées par ce code. 
	 */
	public void dispatcherTva(TaAbonnement doc) {

		BigDecimal tvaLigne = new BigDecimal(0);
		BigDecimal totalTemp = new BigDecimal(0);

		for (Object ligne : doc.getLignes()) {
			if(((TaLAbonnement)ligne).getMtHtLDocument()!=null)
				totalTemp = totalTemp.add(((TaLAbonnement)ligne).getMtHtLDocument());
		}
		if(totalTemp!=null && doc.getTxRemHtDocument()!=null)
			doc.setRemHtDocument(totalTemp.multiply(doc.getTxRemHtDocument().divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_HALF_UP));	
		
		for (TaLAbonnement ligne : doc.getLignes()) {
		if(doc.getTxRemHtDocument()!=null && doc.getTxRemHtDocument().signum()>0 && ligne.getMtHtLDocument()!=null  && ligne.getMtTtcLDocument()!=null) {
				if(doc.getTtc()==1){
					((TaLAbonnement)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLAbonnement)ligne).getMtTtcLDocument().subtract(((TaLAbonnement)ligne).getMtTtcLDocument()
							.multiply(doc.getTxRemHtDocument()).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
					((TaLAbonnement)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLAbonnement)ligne).getMtTtcLApresRemiseGlobaleDocument());
					
				}else{
					((TaLAbonnement)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLAbonnement)ligne).getMtHtLDocument().subtract(((TaLAbonnement)ligne).getMtHtLDocument()
							.multiply(doc.getTxRemHtDocument()).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
					((TaLAbonnement)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLAbonnement)ligne).getMtHtLApresRemiseGlobaleDocument());	
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
					if(((TaLAbonnement)ligne).getTaTLigne().getCodeTLigne().equals(SWTDocument.C_CODE_T_LIGNE_H)) {
						if(((TaLAbonnement)ligne).getCodeTvaLDocument()!=null &&
								((TaLAbonnement)ligne).getCodeTvaLDocument().equals(ligneTva.getCodeTva())){
							if (ligneTva.getMontantTotalHt().signum()==0) 
								tvaLigne = ((TaLAbonnement)ligne).getMtHtLDocument().multiply(ligneTva.getTauxTva()).divide(new BigDecimal(100));
							else {
								if  (lignepasse>= ligneTva.getNbLigneDocument()) 
									tvaLigne = tvaTmp;
								else {
									if(doc.getTtc()==1){
										if(LibCalcul.arrondi(ligneTva.getMontantTotalTtcAvecRemise()).signum()<=0)
											tvaLigne=BigDecimal.valueOf(0);
										else
											tvaLigne = (ligneTva.getMtTva().multiply(((TaLAbonnement)ligne).getMtTtcLDocument())).divide(LibCalcul.arrondi(ligneTva.getMontantTotalTtcAvecRemise()),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
									}
									else{
										if(LibCalcul.arrondi(ligneTva.getMontantTotalHtAvecRemise()).signum()<=0)
											tvaLigne =BigDecimal.valueOf(0);
										else
											tvaLigne = (ligneTva.getMtTva().multiply(((TaLAbonnement)ligne).getMtHtLDocument())).divide(LibCalcul.arrondi(ligneTva.getMontantTotalHtAvecRemise()),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
									}
								}
							}
							tvaTmp =  tvaTmp.subtract(tvaLigne);
							totalTemp = totalTemp.add(((TaLAbonnement)ligne).getMtHtLDocument());

							if(doc.getTxRemHtDocument()!=null && doc.getTxRemHtDocument().signum()>0) {
								if  (lignepasse>= ligneTva.getNbLigneDocument()) {
									((TaLAbonnement)ligne).setMtHtLApresRemiseGlobaleDocument(htTmp);
									((TaLAbonnement)ligne).setMtTtcLApresRemiseGlobaleDocument(ttcTmp);
								} else {
									((TaLAbonnement)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLAbonnement)ligne).getMtHtLDocument().subtract(((TaLAbonnement)ligne).getMtHtLDocument().multiply(doc.getTxRemHtDocument()).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
									((TaLAbonnement)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLAbonnement)ligne).getMtTtcLDocument().subtract(((TaLAbonnement)ligne).getMtTtcLDocument().multiply(doc.getTxRemHtDocument()).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
								}
//								ttcTmp =  ttcTmp.subtract(((TaLAbonnement)ligne).getMtTtcLFacture());
//								htTmp =  htTmp.subtract(((TaLAbonnement)ligne).getMtHtLFacture());
							} else {
								if(doc.getTtc()==1)
									if  (lignepasse>= ligneTva.getNbLigneDocument()) {
										((TaLAbonnement)ligne).setMtHtLApresRemiseGlobaleDocument(htTmp);
									}else{
										((TaLAbonnement)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLAbonnement)ligne).getMtTtcLDocument().subtract(tvaLigne));
									}
								else
									if  (lignepasse>= ligneTva.getNbLigneDocument()) {
										((TaLAbonnement)ligne).setMtTtcLApresRemiseGlobaleDocument(ttcTmp);
									}else {
										((TaLAbonnement)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLAbonnement)ligne).getMtHtLDocument().add(tvaLigne));
									}

							}
							ttcTmp =  ttcTmp.subtract(((TaLAbonnement)ligne).getMtTtcLApresRemiseGlobaleDocument());
							if(((TaLAbonnement)ligne).getMtHtLApresRemiseGlobaleDocument()!=null)
								htTmp =  htTmp.subtract(((TaLAbonnement)ligne).getMtHtLApresRemiseGlobaleDocument());

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
	public void calculMontantLigneDocument(TaAbonnement doc) {
		for (Object ligne : doc.getLignes()) {
			((TaLAbonnement)ligne).calculMontant();
		}
	}
	
	/**
	 * Calcul de la grille de TVA en fonction de lignes du document et du taux de remise HT global.
	 * Mise à jour de la propriété <code>lignesTVA</code>
	 * et mise à jour du montant de la TVA dans les lignes du document
	 */
	public void calculTvaTotal(TaAbonnement doc) {
		calculMontantLigneDocument(doc);
		calculLignesTva(doc);
		dispatcherTva(doc);
	}
	
	/**
	 * Calcul de la grille de TVA en fonction de lignes du document et du taux de remise HT global.
	 * Mise à jour de la propriété <code>lignesTVA</code>
	 */
	public void calculLignesTva(TaAbonnement doc) {

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
			//((TaLAbonnement)ligne).calculMontant();
			codeTVA = ((TaLAbonnement)ligne).getCodeTvaLDocument();
			if(codeTVA!=null && !codeTVA.equals("")) {
				ttcLigne = ((TaLAbonnement)ligne).getMtTtcLDocument();
				htLigne = ((TaLAbonnement)ligne).getMtHtLDocument();
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
					tauxTVA.put(codeTVA,((TaLAbonnement)ligne).getTauxTvaLDocument());
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
	
	public Date calculDateEcheanceAbstract(TaAbonnement doc, Integer report, Integer finDeMois){
		return calculDateEcheance(doc,report,finDeMois);
	}
	
	public Date calculDateEcheance(TaAbonnement doc, Integer report, Integer finDeMois) {
//		TaTCPaiementDAO taTCPaiementDAO = new TaTCPaiementDAO();
		TaTCPaiement typeCP = taTCPaiementDAO.findByCode(TaTCPaiement.C_CODE_TYPE_DEVIS);
		TaCPaiement conditionDoc = null;
		TaCPaiement conditionTiers = null;
		TaCPaiement conditionSaisie = null;
		
		if(typeCP!=null) conditionDoc = typeCP.getTaCPaiement();
		List<TaCPaiement> liste=taCPaiementDAO.rechercheParType(typeCP.getCodeTCPaiement());
		if(liste!=null && !liste.isEmpty())conditionDoc=liste.get(0);
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
		if(nouvelleDate!=null)doc.setDateEchDocument(nouvelleDate);
		return nouvelleDate;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaAbonnement transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaAbonnement transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}


	@Override
	public void remove(TaAbonnement persistentInstance, boolean recharger) throws RemoveException {
		try {
			if(recharger)persistentInstance=findById(persistentInstance.getIdDocument());
			dao.remove(persistentInstance);
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public void remove(TaAbonnement persistentInstance) throws RemoveException {
		 remove(persistentInstance, true);
	}
	
	public TaAbonnement merge(TaAbonnement detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaAbonnement merge(TaAbonnement detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		detachedInstance=dao.merge(detachedInstance);
		annuleCode(detachedInstance.getCodeDocument());
		return detachedInstance;
	}

	public TaAbonnement findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaAbonnement findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaAbonnement> selectAll() {
		return dao.selectAll();
	}
	public List<TaAbonnement> selectAllPourInsertionPeriodeV2() {
		return dao.selectAllPourInsertionPeriodeV2();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaAbonnementDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaAbonnementDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaAbonnement> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaAbonnementDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaAbonnementDTO entityToDTO(TaAbonnement entity) {
//		TaAbonnementDTO dto = new TaAbonnementDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaAbonnementMapper mapper = new TaAbonnementMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaAbonnementDTO> listEntityToListDTO(List<TaAbonnement> entity) {
		List<TaAbonnementDTO> l = new ArrayList<TaAbonnementDTO>();

		for (TaAbonnement taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaAbonnementDTO> selectAllDTO() {
		System.out.println("List of TaAbonnementDTO EJB :");
		List<TaAbonnementDTO> liste = new ArrayList<TaAbonnementDTO>();
		
		TaInfoEntreprise infos = taInfoEntrepriseService.findInstance();
		
		liste = findAllLight();

//		List<TaAbonnement> projects = selectAll();
//		for(TaAbonnement project : projects) {
//			liste.add(entityToDTO(project));
//		}

		return liste;
	}

	public TaAbonnementDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaAbonnementDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaAbonnementDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaAbonnementDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaAbonnementDTO dto, String validationContext) throws EJBException {
		try {
			TaAbonnementMapper mapper = new TaAbonnementMapper();
			TaAbonnement entity = null;
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
	
	public void persistDTO(TaAbonnementDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaAbonnementDTO dto, String validationContext) throws CreateException {
		try {
			TaAbonnementMapper mapper = new TaAbonnementMapper();
			TaAbonnement entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaAbonnementDTO dto) throws RemoveException {
		try {
			TaAbonnementMapper mapper = new TaAbonnementMapper();
			TaAbonnement entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
			//supprimer(findById(dto.getId()));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaAbonnement refresh(TaAbonnement persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaAbonnement value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaAbonnement value, String propertyName, String validationContext) {
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
	public void validateDTO(TaAbonnementDTO dto, String validationContext) {
		try {
			TaAbonnementMapper mapper = new TaAbonnementMapper();
			TaAbonnement entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaAbonnementDTO> validator = new BeanValidator<TaAbonnementDTO>(TaAbonnementDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaAbonnementDTO dto, String propertyName, String validationContext) {
		try {
			TaAbonnementMapper mapper = new TaAbonnementMapper();
			TaAbonnement entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaAbonnementDTO> validator = new BeanValidator<TaAbonnementDTO>(TaAbonnementDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaAbonnementDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaAbonnementDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaAbonnement value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaAbonnement value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

	@Override
	public List<TaAbonnement> rechercheDocument(Date dateDeb, Date dateFin) {
		return dao.rechercheDocument(dateDeb, dateFin);
	}

	@Override
	public List<Object[]> rechercheDocument(Date dateDeb, Date dateFin,
			Boolean light) {
		return dao.rechercheDocument(dateDeb, dateFin, light);
	}

	@Override
	public List<TaAbonnement> rechercheDocument(String codeDeb, String codeFin) {
		return dao.rechercheDocument(codeDeb, codeFin);
	}

	@Override
	public List<TaAbonnement> rechercheDocument(Date dateDeb, Date dateFin,
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
	public List<TaAbonnement> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers) {
		return dao.rechercheDocument(codeDeb, codeFin, codeTiers);
	}

	@Override
	public List<TaAbonnement> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers, Boolean export) {
		// TODO Auto-generated method stub
		return dao.rechercheDocument(dateDeb, dateFin, codeTiers, export);
	}

	@Override
	public List<TaAbonnement> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers, Boolean export) {
		// TODO Auto-generated method stub
		return dao.rechercheDocument(codeDeb, codeFin, codeTiers, export);
	}

	@Override
	public List<TaAbonnement> selectAll(IDocumentTiers taDocument, Date dateDeb,
			Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaAbonnement> findByCodeTiersAndDate(String codeTiers, Date debut,
			Date fin) {
		// TODO Auto-generated method stub
		return dao.findByCodeTiersAndDate(codeTiers, debut, fin);
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
	
	
	/**
	 * Nouvelle méthode qui permet de généré une échéance quand on modifie un abo en y ajoutant une ligne d'abo
	 * @param taLAbonnement la nouvelle ligne d'abonnement
	 * @return List<TaLEcheance> liste des échéances générés (une seule normalement)
	 * @author yann
	 */
	public List<TaLEcheance> generePremiereEcheance(TaLAbonnement taLAbonnement) {
		Date now = new Date();
		List<TaLEcheance> listePremieresEcheances = new ArrayList<>();
		
		TaAbonnement taAbonnement = taLAbonnement.getTaDocument();
			
			if(taAbonnement.getDateAnnulation()==null) { //l'abonnement n'est pas suspendu/annuler
			
				Date date = taAbonnement.getDateDebut();
				
				LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
				LocalDateTime localDateTimeDebutPeriode = null;
				LocalDateTime localDateTimeFinPeriode = null;
				
				Date debutPeriode = null;
				Date finPeriode = null;
				
				
				// l'abonnement a des dates de périodes actives
				if(taAbonnement.getDateDebutPeriodeActive() != null) {
					
					 localDateTimeDebutPeriode = taAbonnement.getDateDebutPeriodeActive().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
					 localDateTimeFinPeriode = taAbonnement.getDateFinPeriodeActive().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
										
					
				//si l'abonnement n'a pas encore de période de date active
				}else {
					// on calcule les dates périodes de l'abonnement avec le nb mois fréquence facturation
					 localDateTimeDebutPeriode = taAbonnement.getDateDebut().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
					 localDateTimeFinPeriode = taAbonnement.getDateDebut().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
					 
					 Integer nbMoisFacturation = taAbonnement.getNbMoisFrequenceFacturation();
					 localDateTimeFinPeriode = localDateTimeFinPeriode.plusMonths(nbMoisFacturation);

					
				}
				
				

	
							
				//rajout yann
				taLAbonnement = taLAbonnementService.donneEtatSuspendu(taLAbonnement);
				
				
				
				debutPeriode = taAbonnement.getDateDebut();
				
				TaStripePlan itemPlan = daoPlan.findByLigneAbo(taLAbonnement);
				
				boolean finDeMois = localDateTime.getMonth().length(localDateTime.toLocalDate().isLeapYear())==localDateTime.getDayOfMonth();
				
				//on se sert soit du NbMoisFrequenceFacturation ou on peut prendre directement la dateFinPeriodeActive
				//localDateTimeFinPeriode = localDateTimeFinPeriode.plusMonths(nbMoisFacturation);

				//prendre le jour du mois de la date création => gérer 28/29, 30, 31, fin de mois
				// => touver la prochaine date d'échéance ce mois ci ou cette année
				if(finDeMois) {
					localDateTimeFinPeriode = localDateTimeFinPeriode.withDayOfMonth(localDateTimeFinPeriode.getMonth().length(localDateTimeFinPeriode.toLocalDate().isLeapYear()));
				}
				
				

				finPeriode = Date.from(localDateTimeFinPeriode.atZone(ZoneId.systemDefault()).toInstant());
						
				TaLEcheance ech = new TaLEcheance();

				ech.setTaAbonnement(taAbonnement);
				ech.setTaLAbonnement(taLAbonnement);
				ech.setTaArticle(itemPlan.getTaArticle());
				ech.setQteLDocument(taLAbonnement.getQteLDocument());
				ech.setU1LDocument(itemPlan.getTaArticle().getTaUnite1().getCodeUnite());
				ech.setNbDecimalesPrix(2);
				ech.setNbDecimalesQte(2);
				ech.setRemTxLDocument(taLAbonnement.getRemTxLDocument());
				ech.setPrixULDocument(itemPlan.getAmount());
				ech.setLibLDocument(itemPlan.getTaArticle().getLibellecArticle());
				ech.setTauxTvaLDocument(taLAbonnement.getTauxTvaLDocument());
				//ech.setTauxTvaLDocument(itemPlan.getTaArticle().getTaTva()!=null?itemPlan.getTaArticle().getTaTva().getTauxTva():null);
				ech.setDebAbonnement(taAbonnement.getDateDebut());
				ech.setFinAbonnement(taAbonnement.getDateFin());
				ech.setDebutPeriode(debutPeriode);
				ech.setFinPeriode(finPeriode);
				
				ech.setCoefMultiplicateur(taLAbonnement.getCoefMultiplicateur());
				//à payer au plus tard la veille du début de la période suivante
				Date dateEcheance = Date.from(localDateTimeDebutPeriode.minusDays(1).atZone(ZoneId.systemDefault()).toInstant());
				
				//ech.setDateEcheance(now); //premier paiement au moment de la création de l'abonnement
				//a changer on prend la date de fin période comme un renouvellement classique
				
				ech.setDateEcheance(dateEcheance);
				
				
				ech.calculMontant();
				
				ech = taLEcheanceService.merge(ech);
				
				TaEtat etat;
				try {
					etat = taEtatService.findByCode("doc_encours");
					ech = taLEcheanceService.donneEtat(ech, etat);
				} catch (FinderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				listePremieresEcheances.add(ech);
						
				
			}
//		}
		return listePremieresEcheances;
	}

	/**
	 * Actuellement utilisé pour généré les premières échéances d'un abonnement à l'insertion
	 */
	public List<TaLEcheance> generePremieresEcheances(TaAbonnement taAbonnement) {
		Date now = new Date();
		List<TaLEcheance> listePremieresEcheances = new ArrayList<>();
			
			if(taAbonnement.getDateAnnulation()==null) { //l'abonnement n'est pas suspendu/annuler
			
				Date date = taAbonnement.getDateDebut();
				
				LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
				
				LocalDateTime localDateTimeNow = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
				//on ne se sert plus de la TaFrequence, mais du nombre de mois choisi pour la fréquence facturation, voir dessous
				//TaFrequence taFrequence = taAbonnement.getTaFrequence();
				Integer nbMoisFacturation = taAbonnement.getNbMoisFrequenceFacturation();
				if(!taAbonnement.getLignes().isEmpty()) {
					TaTiers tiers = taAbonnement.getTaTiers();
						for (TaLAbonnement item : taAbonnement.getLignes()) {
							
							//rajout yann
							item = taLAbonnementService.donneEtatSuspendu(item);
							
							
							
							Date dateFinPeriode = null;
							
							LocalDateTime localDateTimeDebutPeriode = taAbonnement.getDateDebut().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
							LocalDateTime localDateTimeFinPeriode = taAbonnement.getDateDebut().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
							
							
							TaStripePlan itemPlan = daoPlan.findByLigneAbo(item);
							
							boolean finDeMois = localDateTime.getMonth().length(localDateTime.toLocalDate().isLeapYear())==localDateTime.getDayOfMonth();
							
							//on se sert soit du NbMoisFrequenceFacturation ou on peut prendre directement la dateFinPeriodeActive
							localDateTimeFinPeriode = localDateTimeFinPeriode.plusMonths(nbMoisFacturation);
			
							//prendre le jour du mois de la date création => gérer 28/29, 30, 31, fin de mois
							// => touver la prochaine date d'échéance ce mois ci ou cette année
							if(finDeMois) {
								localDateTimeFinPeriode = localDateTimeFinPeriode.withDayOfMonth(localDateTimeFinPeriode.getMonth().length(localDateTimeFinPeriode.toLocalDate().isLeapYear()));
							}
							
							

							dateFinPeriode = Date.from(localDateTimeFinPeriode.atZone(ZoneId.systemDefault()).toInstant());
									
							TaLEcheance ech = new TaLEcheance();
							
//							ech.setTaStripeSubscription(taStripeSubscription);
//							ech.setTaStripeSubscriptionItem(item);
							ech.setTaAbonnement(taAbonnement);
							ech.setTaLAbonnement(item);
							ech.setTaArticle(itemPlan.getTaArticle());
							//ech.setQteLDocument(new BigDecimal(item.getQuantity()));
							ech.setQteLDocument(item.getQteLDocument());
							ech.setU1LDocument(itemPlan.getTaArticle().getTaUnite1().getCodeUnite());
							ech.setNbDecimalesPrix(2);
							ech.setNbDecimalesQte(2);
							ech.setRemTxLDocument(item.getRemTxLDocument());
							ech.setPrixULDocument(itemPlan.getAmount());
							ech.setLibLDocument(itemPlan.getTaArticle().getLibellecArticle());
							ech.setTauxTvaLDocument(item.getTauxTvaLDocument());
							//ech.setTauxTvaLDocument(itemPlan.getTaArticle().getTaTva()!=null?itemPlan.getTaArticle().getTaTva().getTauxTva():null);
							ech.setDebAbonnement(taAbonnement.getDateDebut());
							ech.setFinAbonnement(taAbonnement.getDateFin());
							ech.setDebutPeriode(taAbonnement.getDateDebut()); //premiere période au début de l'abonnement
							ech.setFinPeriode(dateFinPeriode);
							//à payer au plus tard la veille du début de la période suivante
							Date dateEcheance = Date.from(localDateTimeDebutPeriode.minusDays(1).atZone(ZoneId.systemDefault()).toInstant());
							
							//ech.setDateEcheance(now); //premier paiement au moment de la création de l'abonnement
							//a changer on prend la date de fin période comme un renouvellement classique
							
							ech.setDateEcheance(dateEcheance);
							
							ech.setCoefMultiplicateur(item.getCoefMultiplicateur());
							ech.calculMontant();
							
							listePremieresEcheances.add(ech);
						}
				}
			}
//		}
		return listePremieresEcheances;
	}
	/**
	 * @author yann
	 */
	public Integer donneEtatEnCoursLigneAboSansEtat() {
		Integer nb = 0;
		List<TaLAbonnement> listeAbo = new ArrayList<TaLAbonnement>();
		listeAbo = taLAbonnementService.findAllSansEtat();

		for(TaLAbonnement li : listeAbo) {
			if(li.getTaArticle() != null ) {
				System.out.println(li.getTaDocument().getCodeDocument());
				TaEtat etat;
				try {
					etat = taEtatService.findByCode("doc_encours");
					taLAbonnementService.donneEtat(li, etat);
					nb++;
				} catch (FinderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}

		return nb;
		}
	
	
	
	
	/**
	 * méthode plus utilisé pour l'instant car elle tient compte de l'échéance précédante
	 */
	public TaLEcheance genereUneProchaineEcheance(TaLEcheance echeancePrecedente) {
		Date now = new Date();
//		Date nowPlusDeltaAvantEcheance = new Date();
		TaLEcheance ech = new TaLEcheance();
//		LocalDateTime.from(nowPlusDeltaAvantEcheance.toInstant().atZone(ZoneId.systemDefault())).plusDays(MultitenantProxyTimerAbonnement.NB_JOUR_AVANT_ECHEANCE);
		TaLAbonnement item = taLAbonnementService.findByIdLEcheance(echeancePrecedente.getIdLEcheance());
		TaAbonnement taAbonnement = findByIdLEcheance(echeancePrecedente.getIdLEcheance());
//		if(echeancePrecedente.getIdLEcheance() == 76) {
//					System.out.println("je suis sur l'écheance recherchée");
//		}
		//si la date d'échéance de cette échéance précédantes est dans 10 jours ou moins
		// on génère
		//si l'abo n'est pas annulé
		if(taAbonnement.getDateAnnulation()==null) {
			Date dateDebutAbo = taAbonnement.getDateDebut();
			Date dateFinAbo = taAbonnement.getDateFin();
			
			Boolean generationProchaine = false;
			
		
			//si il y a une date de fin d'abo et que cette date de fin est après aujourd'hui
			//si oui l'abonnement est de type avec engagement
			if(dateFinAbo != null) {
				//si la date de fin est dans le futur 
				
				LocalDate localDateDebut = dateDebutAbo.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				LocalDate localDateFin = dateFinAbo.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				
				//a voir si on laisse la condition date de fin dans le futur
				if(dateFinAbo.after(now)) {
								
					//si la date de fin de l'abo est dans au moins 1 fois la fréquence de facturation
					Date nowPlusDeltaFinAbonnement = new Date();
					//Date nowPlusDeltaFinAbonnement = echeancePrecedente.getFinPeriode();
					//LocalDate localDatenowPlusDeltaFinAbonnement = echeancePrecedente.getFinPeriode().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					LocalDate localDatenowPlusDeltaFinAbonnement = Instant.ofEpochMilli( echeancePrecedente.getFinPeriode().getTime())
				      .atZone(ZoneId.systemDefault())
				      .toLocalDate();
					localDatenowPlusDeltaFinAbonnement =  localDatenowPlusDeltaFinAbonnement.plusMonths(taAbonnement.getNbMoisFrequenceFacturation());
					localDatenowPlusDeltaFinAbonnement = localDatenowPlusDeltaFinAbonnement.minusDays(1);
					nowPlusDeltaFinAbonnement = Date.from(localDatenowPlusDeltaFinAbonnement.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
					

					//on regarde si la date de fin de l'abo est après la date de fin période de l'échéance plus 1 fois la fréquence de facturation choisi (en mois)
					//moins 1 jour car la date de fin est la veille de la dernière datre fin période à 23h59
					if(dateFinAbo.after(nowPlusDeltaFinAbonnement)) {
					
						generationProchaine = true;
						
					//sinon c'est que c'était la dernière échéance
					}else {
						//si c'était la derniere échéance et que l'abonnement est en reconduction tacite d'engagement
						if(taAbonnement.getReconductionTaciteEngagement() == true) {
							//on génère 
							generationProchaine = true;
						//pas de reconduction tacite de l'engagement, donc pas de génération de la suivante
						}else {
							generationProchaine = false;
						}
					}

					
					

					
				//la date de fin est passé 
				}else {
					generationProchaine = false;
				}
				
			//il n' y a pas de dates de fin d'abo donc c'est un abonnement sans engagement.
			}else {
				//si c'est un abo avec reconduction tacite d'abonnement
				if(taAbonnement.getReconductionTacite()) {
					generationProchaine = true;
				}else {
					generationProchaine = false;
				}
				
			}
			
			//on lance la génération de la prochaine échéance 		
			if(generationProchaine) {
							
					//Date date = taStripeSubscription.getQuandCree();
					Date date = taAbonnement.getQuandCree();
					
					LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
					LocalDateTime localDateTimeDebutPeriode = new Date(echeancePrecedente.getDebutPeriode().getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
					LocalDateTime localDateTimeFinPeriode = new Date(echeancePrecedente.getFinPeriode().getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
					
					if(item != null) {

					
						Date dateDebutPeriode = null;
						Date dateFinPeriode = null;
						Date dateEcheance= null;
						
						localDateTimeDebutPeriode = localDateTimeFinPeriode.plusDays(1);
						TaStripePlan itemPlan = daoPlan.findByLigneAbo(taAbonnement.getLignes().iterator().next());

						boolean finDeMois = localDateTime.getMonth().length(localDateTime.toLocalDate().isLeapYear())==localDateTime.getDayOfMonth();
						
						localDateTimeFinPeriode = localDateTimeFinPeriode.plusMonths(taAbonnement.getNbMoisFrequenceFacturation());
		
						//prendre le jour du mois de la date création => gérer 28/29, 30, 31, fin de mois
						// => touver la prochaine date d'échéance ce mois ci ou cette année
						if(finDeMois) {
							localDateTimeFinPeriode = localDateTimeFinPeriode.withDayOfMonth(localDateTimeFinPeriode.getMonth().length(localDateTimeFinPeriode.toLocalDate().isLeapYear()));
						}

						
						dateFinPeriode = Date.from(localDateTimeFinPeriode.atZone(ZoneId.systemDefault()).toInstant());
						dateDebutPeriode = Date.from(localDateTimeDebutPeriode.atZone(ZoneId.systemDefault()).toInstant());
						//à payer au plus tard la veille du début de la période suivante
						dateEcheance = Date.from(localDateTimeDebutPeriode.minusDays(1).atZone(ZoneId.systemDefault()).toInstant());
								
						
						

						ech.setTaAbonnement(taAbonnement);
						ech.setTaLAbonnement(item);
						ech.setTaArticle(itemPlan.getTaArticle());
						//ech.setQteLDocument(new BigDecimal(item.getQuantity()));
						ech.setQteLDocument(item.getQteLDocument());
						ech.setPrixULDocument(itemPlan.getAmount());
						//ech.setRemTxLDocument(item.getTaLAbonnement().getRemTxLDocument());
						ech.setRemTxLDocument(item.getRemTxLDocument());
						ech.setLibLDocument(itemPlan.getTaArticle().getLibellecArticle());
						ech.setTauxTvaLDocument(itemPlan.getTaArticle().getTaTva()!=null?itemPlan.getTaArticle().getTaTva().getTauxTva():null);
						ech.setDebAbonnement(taAbonnement.getDateDebut());
						ech.setFinAbonnement(taAbonnement.getDateFin());
						ech.setU1LDocument(itemPlan.getTaArticle().getTaUnite1().getCodeUnite());
						ech.setNbDecimalesPrix(2);
						ech.setNbDecimalesQte(2);
						ech.setDebutPeriode(dateDebutPeriode); //le jour suivant la fin de période précédente
						ech.setFinPeriode(dateFinPeriode);
						ech.setDateEcheance(dateEcheance); 
						ech.calculMontant();
						
						TaEtat etatEncours;
						try {
							etatEncours = taEtatService.findByCode("doc_encours");
							ech = taLEcheanceService.merge(ech);
							ech = taLEcheanceService.donneEtat(ech,etatEncours);
						} catch (FinderException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						

				
							
					}//fin if item n'est pas null 
						
				
			}//fin if generationprochaine
			

		}//fin if si l'échéance a un abo ou annulé
				



		
		return ech;
		
	}
	//nouvelle méthode de génération des prochaines échéances d'un abonnement 01/10/2020
	public List<TaLEcheance> genereProchainesEcheances(TaAbonnementDTO taAbonnementDTO) {
		System.out.println("****TRAITEMENT ABONNEMENT "+taAbonnementDTO.getCodeDocument()+" pour éventuelle génération des prochaines échéances...****");
		List<TaLEcheance> listeProchainesEcheances = new ArrayList<>();
		Date now = new Date();
		Boolean generationProchaine = false;
	
		try {
			Date dateDebutAbo = taAbonnementDTO.getDateDebut();
			Date dateFinAbo = taAbonnementDTO.getDateFin();
			Date dateDebutPeriode = taAbonnementDTO.getDateDebutPeriodeActive();
			Date dateFinPeriode = taAbonnementDTO.getDateFinPeriodeActive();
			TaAbonnement taAbonnement = findByCode(taAbonnementDTO.getCodeDocument());
			List<TaLAbonnement> lignesAbo = taAbonnement.getLignes();
			
			//si il y a une date de fin d'abo et que cette date de fin est après aujourd'hui
			//si oui l'abonnement est de type avec engagement
			if(dateFinAbo != null) {
				//si la date de fin est dans le futur 
				
				LocalDate localDateDebut = dateDebutAbo.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				LocalDate localDateFin = dateFinAbo.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				
				//a voir si on laisse la condition date de fin dans le futur
				if(dateFinAbo.after(now)) {
					//si la date de fin de l'abo est dans au moins 1 fois la fréquence de facturation
					Date nowPlusDeltaFinAbonnement = new Date();
					LocalDate localDatenowPlusDeltaFinAbonnement = Instant.ofEpochMilli( dateFinPeriode.getTime())
				      .atZone(ZoneId.systemDefault())
				      .toLocalDate();
					localDatenowPlusDeltaFinAbonnement =  localDatenowPlusDeltaFinAbonnement.plusMonths(taAbonnement.getNbMoisFrequenceFacturation());
					localDatenowPlusDeltaFinAbonnement = localDatenowPlusDeltaFinAbonnement.minusDays(1);
					nowPlusDeltaFinAbonnement = Date.from(localDatenowPlusDeltaFinAbonnement.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
					

					//on regarde si la date de fin de l'abo est après la date de fin période de l'abo plus 1 fois la fréquence de facturation choisi (en mois)
					//moins 1 jour car la date de fin est la veille de la dernière datre fin période à 23h59
					if(dateFinAbo.after(nowPlusDeltaFinAbonnement)) {
					
						generationProchaine = true;
						
					//sinon c'est que c'était la dernière échéance
					}else {
						//si c'était la derniere échéance et que l'abonnement est en reconduction tacite d'engagement
						if(taAbonnement.getReconductionTaciteEngagement() == true) {
							//on génère 
							generationProchaine = true;
						//pas de reconduction tacite de l'engagement, donc pas de génération de la suivante
						}else {
							System.out.println("****On ne va pas généré d'échéances pour "+taAbonnementDTO.getCodeDocument()+" car pas de reconduction tacite d'engagement.****");
							generationProchaine = false;
						}
					}
					
				}else {//la date de fin est passé
					generationProchaine = false;
				}
				//il n' y a pas de dates de fin d'abo donc c'est un abonnement sans engagement.
			}else {
				//si c'est un abo avec reconduction tacite d'abonnement
				generationProchaine = true;
				
			}
			
			if(generationProchaine) {
				//pour chaque ligne de l'abonnement
				for (TaLAbonnement ligneAbo : lignesAbo) {
					//on contrôle si cette ligne à l'etat en cours
					if(ligneAbo.getTaREtatLigneDocument() != null) {
						
						if(ligneAbo.getTaREtatLigneDocument().getTaEtat().getCodeEtat().equals("doc_encours")) {
							//on vérifie que la ligne d'abonnement n'ai pas deja une échéance en cours
							List<TaLEcheance> echeances = taLEcheanceService.rechercheEcheanceEnCoursByIdLAbonnement(ligneAbo.getIdLDocument());
							
							if(echeances == null || echeances.isEmpty()) {
								//on génère
								Date date = taAbonnement.getQuandCree();
								if(date == null) {
									date = new Date();
								}
								
								LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
								LocalDateTime localDateTimeDebutPeriode = new Date(dateDebutPeriode.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
								LocalDateTime localDateTimeFinPeriode = new Date(dateFinPeriode.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

								Date dateDebutPeriodeEch = null;
								Date dateFinPeriodeEch = null;
								Date dateEcheance= null;
									
								localDateTimeDebutPeriode = localDateTimeFinPeriode.plusDays(1);
								TaStripePlan itemPlan = daoPlan.findByLigneAbo(ligneAbo);

								boolean finDeMois = localDateTime.getMonth().length(localDateTime.toLocalDate().isLeapYear())==localDateTime.getDayOfMonth();
								
								localDateTimeFinPeriode = localDateTimeFinPeriode.plusMonths(taAbonnement.getNbMoisFrequenceFacturation());
				
								//prendre le jour du mois de la date création => gérer 28/29, 30, 31, fin de mois
								// => touver la prochaine date d'échéance ce mois ci ou cette année
								if(finDeMois) {
								//	localDateTimeFinPeriode = localDateTimeFinPeriode.withDayOfMonth(localDateTimeFinPeriode.getMonth().length(localDateTimeFinPeriode.toLocalDate().isLeapYear()));
								}

								
								dateFinPeriodeEch = Date.from(localDateTimeFinPeriode.atZone(ZoneId.systemDefault()).toInstant());
								dateDebutPeriodeEch = Date.from(localDateTimeDebutPeriode.atZone(ZoneId.systemDefault()).toInstant());
								//à payer au plus tard la veille du début de la période suivante
								dateEcheance = Date.from(localDateTimeDebutPeriode.minusDays(1).atZone(ZoneId.systemDefault()).toInstant());
										
								
								TaLEcheance ech = new TaLEcheance();

								ech.setTaAbonnement(taAbonnement);
								ech.setTaLAbonnement(ligneAbo);
								ech.setTaArticle(itemPlan.getTaArticle());
								//ech.setQteLDocument(new BigDecimal(item.getQuantity()));
								ech.setQteLDocument(ligneAbo.getQteLDocument());
								ech.setPrixULDocument(itemPlan.getAmount());
								//ech.setRemTxLDocument(item.getTaLAbonnement().getRemTxLDocument());
								ech.setRemTxLDocument(ligneAbo.getRemTxLDocument());
								ech.setLibLDocument(itemPlan.getTaArticle().getLibellecArticle());
								
								ech.setTauxTvaLDocument(ligneAbo.getTauxTvaLDocument());
								//ech.setTauxTvaLDocument(itemPlan.getTaArticle().getTaTva()!=null?itemPlan.getTaArticle().getTaTva().getTauxTva():null);
								ech.setDebAbonnement(ligneAbo.getDateDebutAbonnement());
								ech.setFinAbonnement(taAbonnement.getDateFin());
								ech.setU1LDocument(itemPlan.getTaArticle().getTaUnite1().getCodeUnite());
								ech.setNbDecimalesPrix(2);
								ech.setNbDecimalesQte(2);
								ech.setDebutPeriode(dateDebutPeriodeEch); //le jour suivant la fin de période précédente
								ech.setFinPeriode(dateFinPeriodeEch);
								ech.setDateEcheance(dateEcheance); 
								ech.setCoefMultiplicateur(ligneAbo.getCoefMultiplicateur());
								ech.calculMontant();
								
								TaEtat etatEncours;
								try {
									etatEncours = taEtatService.findByCode("doc_encours");
									ech = taLEcheanceService.merge(ech);
									ech = taLEcheanceService.donneEtat(ech,etatEncours);
									
									listeProchainesEcheances.add(ech);
									System.out.println("****GENERATION ECHEANCE SUCCES pour ligne abo (id: "+ligneAbo.getIdLDocument()+") de l'abonnement "+taAbonnementDTO.getCodeDocument()+"  ! . ID ECHEANCE : "+ech.getIdLEcheance()+"****");
								} catch (FinderException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}
							//il existe deja une échéance en cours pour cette ligne d'abonnement, on ne génère pas de nouvelles échéances
							System.out.println("****On ne va pas généré d'échéances pour la ligne (id: "+ligneAbo.getIdLDocument()+") de l'abonnement "+taAbonnementDTO.getCodeDocument()+" car il y a déjà une échéance en cours pour cette ligne ! .****");
							
						}
						
						System.out.println("****On ne va pas généré d'échéances pour la ligne (id: "+ligneAbo.getIdLDocument()+") de l'abonnement "+taAbonnementDTO.getCodeDocument()+" car la ligne n'a pas l'état en cours ! .****");
					}
					
					System.out.println("****On ne va pas généré d'échéances pour la ligne (id: "+ligneAbo.getIdLDocument()+") de l'abonnement "+taAbonnementDTO.getCodeDocument()+" car la ligne n'a pas d'état ! .****");
				}//fin boucle
			}//fin si génération
			

			
			
			
		} catch (FinderException e) {
			System.out.println("Aucun abonnement trouvé avec le code document de ce DTO, génération des prochaines échéances impossibles.");
			e.printStackTrace();
		}
		
		return listeProchainesEcheances;
	}
	
	
	/**
	 * Plus utilisé
	 */
	public List<TaLEcheance> genereProchainesEcheances(TaAbonnement taAbonnement, TaLEcheance echeancePrecedente) {
		/*
		 * TODO Attention de ne pas générer plusieurs "la même échéance suivante"
		 */
		Date now = new Date();
		Date nowPlusDeltaAvantEcheance = new Date();
		LocalDateTime localDateNowPlusDeltaAvantEcheance = new Date(nowPlusDeltaAvantEcheance.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		//LocalDateTime.from(nowPlusDeltaAvantEcheance.toInstant().atZone(ZoneId.systemDefault())).plusDays(MultitenantProxyTimerAbonnement.NB_JOUR_AVANT_ECHEANCE);
		localDateNowPlusDeltaAvantEcheance = localDateNowPlusDeltaAvantEcheance.plusDays(MultitenantProxyTimerAbonnement.NB_JOUR_AVANT_ECHEANCE);
		nowPlusDeltaAvantEcheance = Date.from(localDateNowPlusDeltaAvantEcheance.atZone(ZoneId.systemDefault()).toInstant());
		List<TaLEcheance> listeProchainesEcheances = new ArrayList<>();
//		if(echeancePrecedente.getIdLEcheance() == 76) {
//					System.out.println("je suis sur l'écheance recherchée");
//		}
		//si la date d'échéance de cette échéance précédantes est dans 10 jours ou moins
		// on génère
		if(echeancePrecedente.getDateEcheance().before(nowPlusDeltaAvantEcheance)) {
				
			//vieux système
			//TaFrequence taFrequence = echeancePrecedente.getTaAbonnement().getTaFrequence();
			
			//si l'abo n'est pas annulé
			if(taAbonnement.getDateAnnulation()==null) {
				//rajout yann pour calcul dynamique des prochaines échéances à générer
				//a vérifier car les abonnements dans les échéances sont censé etre en LAZY
				Date dateDebutAbo = echeancePrecedente.getTaAbonnement().getDateDebut();
				Date dateFinAbo = echeancePrecedente.getTaAbonnement().getDateFin();
				Boolean generationProchaine = false;
				
				
				
				
				
				
				//si il y a une date de fin d'abo et que cette date de fin est après aujourd'hui  Attention : à vérifier quand la fréquence choisie est jours
				//on vérifie qu'il reste des lignes d'échéances à générer
				if(dateFinAbo != null) {
					//si la date de fin est dans le futur 
					
//					if(dateFinAbo.after(nowPlusDeltaAvantEcheance)) {
					if(dateFinAbo.after(now)) {
						
						//si l'abonnement a un type 
						if(taAbonnement.getTypeAbonnement() != null ) {
							
							//si l'abonnement est de type avec engagement
							if(taAbonnement.getTypeAbonnement().equals(TaAbonnement.TYPE_AVEC_ENGAGEMENT)) {
								//si l'abonnement est en reconduction tacite d'engagement
								if(taAbonnement.getReconductionTaciteEngagement() == true) {
									
								}
							
								
								
							
							//si l'abonnement est sans engagement
							}else {
								
							}
						//l'abonnement n' a pas de type	
						}else {
							
						}
						
						
						
						
						
						
						//on va calculer le nb d'échéances restantes a générer.
//						Integer nbEcheancesDejaCree = 0;
//						Integer nbEcheancesTotale = 0;
//						Integer nbEcheancesRestante = 0;
						
						LocalDate localDateDebut = dateDebutAbo.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
						LocalDate localDateFin = dateFinAbo.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
						
						//calcul du nombre echéances total pour cet item qu'il devrait y avoir, avec la date début abo, date fin abo et la fréquence.
						//pour cela, il faut trouver combien de fois la fréquences rentre entre la date de début et la date de fin +1 jour.
//						switch (taFrequence.getCodeFrequence()) {
//						case TaFrequence.CODE_FREQUENCE_AN:
//							nbEcheancesTotale = (int) java.time.temporal.ChronoUnit.YEARS.between(localDateDebut, localDateFin.plusDays(1));
//							break;
//						case TaFrequence.CODE_FREQUENCE_JOUR:
//							nbEcheancesTotale = (int) java.time.temporal.ChronoUnit.DAYS.between(localDateDebut, localDateFin.plusDays(1));			
//							break;
//						case TaFrequence.CODE_FREQUENCE_MOIS:
//							nbEcheancesTotale = (int) java.time.temporal.ChronoUnit.MONTHS.between(localDateDebut, localDateFin.plusDays(1));			
//						    break;
//						case TaFrequence.CODE_FREQUENCE_SEMAINE:
//							nbEcheancesTotale = (int) java.time.temporal.ChronoUnit.WEEKS.between(localDateDebut, localDateFin.plusDays(1));
//							break;
//						}
						
						//System.out.println(" Nombre de ligne d'échéances total que cet abonnement devrait généré par ligne : "+nbEcheancesTotale);
						
						//on va chercher le nombre de lignes d'échéances existantes pour ce subscriptionItem (attention, cette requete remonte même les lignes d'échéances désactivées).
						//nbEcheancesDejaCree = taLEcheanceService.countAllByIdSubscriptionItem(echeancePrecedente.getTaStripeSubscriptionItem().getIdStripeSubscriptionItem());
					//	nbEcheancesDejaCree = taLEcheanceService.countAllByIdLigneAbo(echeancePrecedente.getTaLAbonnement().getIdLDocument());
						//on calcule le nombre de lignes d'échéances qu'il reste à générer 
					//	nbEcheancesRestante = nbEcheancesTotale - nbEcheancesDejaCree;
						//si il reste des lignes d'échéances à générer, on peut générer la prochaine.
//						if(nbEcheancesRestante > 0) {
//							generationProchaine = true;
//						}
						
					//la date de fin est passé 
					}else {
						generationProchaine = false;
					}
					
				//il n' y a pas de dates de fin d'abo donc génération de la prochaines ligne d'échéance.
				}else {
					generationProchaine = true;
				}
				
				//on lance la génération de la prochaine échéance si :
				//l'abo n'a pas de date fin ou si il en a une dans le futur et si il reste des lignes d'échéances à générer 
				if(generationProchaine) {
								
						//Date date = taStripeSubscription.getQuandCree();
						Date date = taAbonnement.getQuandCree();
						if(date == null) {
							date = new Date();
						}
						
						LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
						LocalDateTime localDateTimeDebutPeriode = new Date(echeancePrecedente.getDebutPeriode().getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
						LocalDateTime localDateTimeFinPeriode = new Date(echeancePrecedente.getFinPeriode().getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
						LocalDateTime localDateTimeNow = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
						
						if(!taAbonnement.getLignes().isEmpty()) {
			
								for (TaLAbonnement item : taAbonnement.getLignes()) {
									Date dateDebutPeriode = null;
									Date dateFinPeriode = null;
									Date dateEcheance= null;
									
									localDateTimeDebutPeriode = localDateTimeFinPeriode.plusDays(1);
										
									//TaStripePlan itemPlan = daoPlan.findByStripeSubscriptionItem(taStripeSubscription.getItems().iterator().next());
									TaStripePlan itemPlan = daoPlan.findByLigneAbo(item);
									//String interval = itemPlan.getInterval();
//									switch (taFrequence.getCodeFrequence()) {
						//			case "day":
						//				break;
						//			case "week":
						//				break;
//									case TaFrequence.CODE_FREQUENCE_MOIS:
									boolean finDeMois = localDateTime.getMonth().length(localDateTime.toLocalDate().isLeapYear())==localDateTime.getDayOfMonth();
									
									localDateTimeFinPeriode = localDateTimeFinPeriode.plusMonths(1);
					
									//prendre le jour du mois de la date création => gérer 28/29, 30, 31, fin de mois
									// => touver la prochaine date d'échéance ce mois ci ou cette année
									if(finDeMois) {
										localDateTimeFinPeriode = localDateTimeFinPeriode.withDayOfMonth(localDateTimeFinPeriode.getMonth().length(localDateTimeFinPeriode.toLocalDate().isLeapYear()));
									}
										
//										break;
//									case TaFrequence.CODE_FREQUENCE_AN:
//										//localDateTimeFinPeriode = localDateTimeFinPeriode.withYear(localDateTimeFinPeriode.getYear());
//										localDateTimeFinPeriode = localDateTimeFinPeriode.withYear(localDateTimeFinPeriode.plusYears(1).getYear());
//										break;
//									default:
//										break;
//									}
									
									dateFinPeriode = Date.from(localDateTimeFinPeriode.atZone(ZoneId.systemDefault()).toInstant());
									dateDebutPeriode = Date.from(localDateTimeDebutPeriode.atZone(ZoneId.systemDefault()).toInstant());
									//à payer au plus tard la veille du début de la période suivante
									dateEcheance = Date.from(localDateTimeDebutPeriode.minusDays(1).atZone(ZoneId.systemDefault()).toInstant());
											
									TaLEcheance ech = new TaLEcheance();
									

									ech.setTaAbonnement(taAbonnement);
									ech.setTaLAbonnement(item);
									ech.setTaArticle(itemPlan.getTaArticle());
									//ech.setQteLDocument(new BigDecimal(item.getQuantity()));
									ech.setQteLDocument(item.getQteLDocument());
									ech.setPrixULDocument(itemPlan.getAmount());
									//ech.setRemTxLDocument(item.getTaLAbonnement().getRemTxLDocument());
									ech.setRemTxLDocument(item.getRemTxLDocument());
									ech.setLibLDocument(itemPlan.getTaArticle().getLibellecArticle());
									ech.setTauxTvaLDocument(itemPlan.getTaArticle().getTaTva()!=null?itemPlan.getTaArticle().getTaTva().getTauxTva():null);
									ech.setDebAbonnement(taAbonnement.getDateDebut());
									ech.setFinAbonnement(taAbonnement.getDateFin());
									ech.setU1LDocument(itemPlan.getTaArticle().getTaUnite1().getCodeUnite());
									ech.setNbDecimalesPrix(2);
									ech.setNbDecimalesQte(2);
									ech.setDebutPeriode(dateDebutPeriode); //le jour suivant la fin de période précédente
									ech.setFinPeriode(dateFinPeriode);
									ech.setDateEcheance(dateEcheance); //premier paiement au moment de la création de l'abonnement
									ech.calculMontant();
			
									listeProchainesEcheances.add(ech);
								}//fin boucle sur les items
								
						}//fin if items n'est pas empty 
							
							
						
		
					
				}//fin if generationprochaine
				
	
			}//fin if si l'échéance a un abo sans fréquences ou annulé
				
				
				
		}//fin si date échéance est dans 10 jours ou moins


		return listeProchainesEcheances;
	}
//
//	@Override
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireJmaDTO(Date debut, Date fin, int precision) {
//		// TODO Auto-generated method stub
//		return dao.findChiffreAffaireJmaDTO(debut, fin, precision);
//	}
//	
//	@Override
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotalDTO(Date debut, Date fin) {
//		// TODO Auto-generated method stub
//		return dao.findChiffreAffaireTotalDTO(debut, fin);
//	}
//
//	@Override
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTransformeJmaDTO(Date debut, Date fin, int precision) {
//		// TODO Auto-generated method stub
//		return dao.findChiffreAffaireTransformeJmaDTO(debut, fin, precision);
//	}
//	
//	@Override
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTransformeTotalDTO(Date debut, Date fin) {
//		// TODO Auto-generated method stub
//		return dao.findChiffreAffaireTransformeTotalDTO(debut, fin);
//	}
//
//
//	@Override
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireNonTransformeJmaDTO(Date debut, Date fin, int precision) {
//		// TODO Auto-generated method stub
//		return dao.findChiffreAffaireNonTransformeJmaDTO(debut, fin, precision);
//	}
//	
//	@Override
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireNonTransformeTotalDTO(Date debut, Date fin) {
//		// TODO Auto-generated method stub
//		return dao.findChiffreAffaireNonTransformeTotalDTO(debut, fin);
//	}
	
	@Override
	public List<Object> findChiffreAffaireByCodeArticle(String codeArticle,
			Date debut, Date fin, int precision) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaAbonnement findByIdDocument(int id) throws FinderException {
		return findById(id);
	}

	@Override
	public TaAbonnement mergeAndFindById(TaAbonnement detachedInstance, String validationContext) {
			TaAbonnement br = merge(detachedInstance,validationContext);
		try {
			br = findByIDFetch(br.getIdDocument()); //pour etre sur que les valeur initialisé par les triggers "qui_cree,..." soit bien affecté avant une éventuelle nouvelle modification du document
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return br;
	}
	
	public List<TaAbonnementDTO> findAllLight() {
		return dao.findAllLight();
	}
	
	public TaAbonnement findByIdSubscription(Integer idStripeSubscription) {
		return dao.findByIdSubscription( idStripeSubscription);
	}

	// A supprimer puisque remplacée 
//	@Override
//	public List<Object> findCADevisSurPeriode(Date debut, Date fin) {
//		// TODO Auto-generated method stub
//		return dao.findCADevisSurPeriode(debut, fin);
//	}
	@Override
	public List<TaAbonnementDTO> findAllEnCoursDTOPeriode(Date dateDebut, Date dateFin,String codeTiers){
		return dao.findAllEnCoursDTOPeriode(dateDebut, dateFin, codeTiers);
	}
	@Override
	public DocumentChiffreAffaireDTO chiffreAffaireTotalEnCoursDTO(Date dateDebut, Date dateFin,String codeTiers) {
		return dao.chiffreAffaireTotalEnCoursDTO(dateDebut, dateFin, codeTiers);
	}
	@Override
	public long countDocumentEnCours(Date dateDebut, Date dateFin,String codeTiers) {
		return dao.countDocumentEnCours(dateDebut, dateFin, codeTiers);
	}
	@Override
	public List<TaAbonnementDTO> findAllSuspenduDTOPeriode(Date dateDebut, Date dateFin,String codeTiers){
		return dao.findAllSuspenduDTOPeriode(dateDebut, dateFin, codeTiers);
	}
	@Override
	public DocumentChiffreAffaireDTO chiffreAffaireTotalSuspenduDTO(Date dateDebut, Date dateFin,String codeTiers) {
		return dao.chiffreAffaireTotalSuspenduDTO(dateDebut, dateFin, codeTiers);
	}
	@Override
	public long countDocumentSuspendu(Date dateDebut, Date dateFin,String codeTiers) {
		return dao.countDocumentSuspendu(dateDebut, dateFin, codeTiers);
	}

	
	
	
	
	@Override
	public List<DocumentDTO> findAllDTOPeriode(Date dateDebut, Date dateFin,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.findAllDTOPeriode(dateDebut, dateFin, codeTiers);
	}
	@Override
	public List<TaAbonnementDTO> findAllAnnuleDTOPeriode(Date dateDebut, Date dateFin,String codeTiers){
		return dao.findAllAnnuleDTOPeriode( dateDebut,  dateFin, codeTiers);
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
	
	
//	@Override
//	public List<DocumentChiffreAffaireDTO> caDevisNonTransfosARelancerDTO(Date dateDebut, Date dateFin, int deltaNbJours){
//		return dao.caDevisNonTransfosARelancerDTO(dateDebut,dateFin,deltaNbJours);
//	}
//
//	@Override
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTransformeARelancerJmaDTO(Date debut, Date fin,
//			int precision) {
//		// TODO Auto-generated method stub
//		return dao.findChiffreAffaireNonTransformeARelancerJmaDTO(debut, fin, precision);
//	}
	
	@Override
	public long countDocument(Date debut, Date fin,String codeTiers) {
		return dao.countDocument(debut, fin,codeTiers);
	}


	@Override
	public long countDocumentNonTransforme(Date debut, Date fin,String codeTiers) {
		return dao.countDocumentNonTransforme(debut, fin,codeTiers);
	}

	@Override
	public long countDocumentNonTransformeARelancer(Date debut, Date fin, int deltaNbJours,String codeTiers){
		return dao.countDocumentNonTransformeARelancer(debut, fin, deltaNbJours,codeTiers);
	}
	
	@Override
	public long countDocumentTransforme(Date debut, Date fin,String codeTiers){
		return dao.countDocumentTransforme(debut, fin,codeTiers);
	}
	@Override
	public long countDocumentAnnule(Date dateDebut, Date dateFin,String codeTiers) {
		return dao.countDocumentAnnule( dateDebut,  dateFin, codeTiers);
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
	public DocumentChiffreAffaireDTO chiffreAffaireTotalAnnuleDTO(Date dateDebut, Date dateFin,String codeTiers) {
		return dao.chiffreAffaireTotalAnnuleDTO(dateDebut, dateFin, codeTiers);
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

	
	public String generePDF(int idDoc, Map<String,Object> mapParametreEdition, List<String> listResourcesPath, String theme) {
		return generePDF(idDoc,editionDefaut,mapParametreEdition, null, null);
	}
	
	public String generePDF(int idDoc, String modeleEdition, Map<String,Object> mapParametreEdition, List<String> listResourcesPath, String theme) {
		try {
			SchemaResolver sr = new SchemaResolver();
			String localPath = bdgProperties.osTempDirDossier(sr.resolveCurrentTenantIdentifier())+"/"+bdgProperties.tmpFileName("devis.pdf");
			System.out.println(localPath);
			System.out.println(bdgProperties.urlDemoHttps()+modeleEdition);
			
			Map<String,Object> mapEdition = new HashMap<String,Object>();

			TaAbonnement doc = findById(idDoc);
			doc.calculeTvaEtTotaux();

			mapEdition.put("doc",doc );

			mapEdition.put("taInfoEntreprise", entrepriseService.findInstance());

			mapEdition.put("lignes", doc.getLignes());

			
			if(mapParametreEdition == null) {
				mapParametreEdition = new HashMap<String,Object>();
			}
			List<TaPreferences> liste= taPreferencesService.findByGroupe("devis");
			if(!mapParametreEdition.containsKey("preferences")) mapParametreEdition.put("preferences", liste);
			if(!mapParametreEdition.containsKey("gestionLot")) mapParametreEdition.put("gestionLot", false);
			if(!mapParametreEdition.containsKey("mode")) mapParametreEdition.put("mode", "");
//			if(!mapParametreEdition.containsKey("Theme")) mapParametreEdition.put("Theme", "defaultTheme");
//			if(!mapParametreEdition.containsKey("Librairie")) mapParametreEdition.put("Librairie", "bdgFactTheme1");
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
				listResourcesPath.add(EditionProgrammeDefaut.LIBRAIRIE_EDITION_DEFAUT_AVIS_ECHEANCE);
			}else if(listResourcesPath.isEmpty()) {
					listResourcesPath = new ArrayList<String>();
					listResourcesPath.add(EditionProgrammeDefaut.LIBRAIRIE_EDITION_DEFAUT_AVIS_ECHEANCE);
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
	public List<TaArticlesParTiersDTO> findArticlesParTiersParMois(Date debut, Date fin, String codeTiers) {
		// TODO Auto-generated method stub
		return dao.findArticlesParTiersParMois(debut, fin, codeTiers);
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersTransforme(Date debut, Date fin, String codeTiers) {
		// TODO Auto-generated method stub
		return dao.findArticlesParTiersTransforme(debut, fin, codeTiers);
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransforme(Date debut, Date fin, String codeTiers) {
		// TODO Auto-generated method stub
		return dao.findArticlesParTiersNonTransforme(debut, fin, codeTiers);
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransformeARelancer(Date debut, Date fin,
			int deltaNbJours, String codeTiers) {
		// TODO Auto-generated method stub
		return dao.findArticlesParTiersNonTransformeARelancer(debut, fin, deltaNbJours, codeTiers);
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersParMoisParTypeRegroupement(Date dateDebut, Date dateFin,
			String codeTiers, String typeRegroupement, Object valeurRegroupement, boolean regroupee) {
		// TODO Auto-generated method stub
		return dao.findArticlesParTiersParMoisParTypeRegroupement(dateDebut, dateFin, codeTiers, typeRegroupement, valeurRegroupement, regroupee);
	}

	@Override
	public List<TaAbonnement> rechercheDocumentVerrouille(Date dateDeb, Date dateFin, String codeTiers, Boolean verrouille) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaAbonnement> rechercheDocumentVerrouille(String codeDeb, String codeFin, String codeTiers,
			Boolean verrouille) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaAbonnement> rechercheDocument(String codeTiers, Date dateExport, Date dateDeb, Date dateFin) {
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
		return dao.listeSumChiffreAffaireTotalDTOArticleMois( dateDebut,  dateFin,
			 codeArticle,  codeTiers);
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
	public String generePDF(int idDoc, Map<String, Object> mapParametreEdition, List<String> listResourcesPath,
			String theme, TaActionEdition action) {
		// TODO Auto-generated method stub
		return null;
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
	public TaAbonnement findDocByLDoc(ILigneDocumentTiers lDoc) {
		TaAbonnement o= (TaAbonnement) dao.findDocByLDoc(lDoc);
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
	public TaAbonnement mergeEtat(IDocumentTiers detachedInstance) {
//		à mettre si gestion des états !!!!
//		modifEtatLigne(detachedInstance);		
//		TaEtat etat=changeEtatDocument(detachedInstance);
//		((TaAbonnement) detachedInstance).addREtat(etat);
		
		detachedInstance=dao.merge((TaAbonnement) detachedInstance);	
		return (TaAbonnement) detachedInstance;
	}

	
	@Override
	public int findDocByLDocDTO(ILigneDocumentTiers lDoc) {
		return dao.findDocByLDocDTO(lDoc);
	}

	@Override
	public TaAbonnement findByCodeFetch(String code) throws FinderException {
		// TODO Auto-generated method stub
		TaAbonnement o= (TaAbonnement) dao.findByCodeFetch(code);
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
	public TaAbonnement findByIDFetch(int id) throws FinderException {
		// TODO Auto-generated method stub
		TaAbonnement o= (TaAbonnement) dao.findByIdFetch(id);
		recupSetREtat(o);
		recupSetHistREtat(o);
		recupSetLigneALigne(o);
		recupSetRDocument(o);
		recupSetREtatLigneDocuments(o);
		recupSetHistREtatLigneDocuments(o);
		recupSetRReglementLiaison(o);
		return o;
	}
	public List<TaAbonnementDTO> findAllAnnuleDTO(String codeTiers){
		return dao.findAllAnnuleDTO(codeTiers);
	}
	@TransactionTimeout(value=30,unit=TimeUnit.MINUTES)
	public void annuleLigneAbo() {
		List<TaAbonnementDTO> listeAbo =findAllAnnuleDTO(null);
		Integer nbLigneAboSuppr = 0;
		for (TaAbonnementDTO taAbonnementDTO : listeAbo) {
			List<TaLAbonnement> listeLigneAbo = taLAbonnementService.findAllByIdAbonnement(taAbonnementDTO.getId());
			for (TaLAbonnement taLAbonnement : listeLigneAbo) {
				taLAbonnementService.donneEtatAnnule(taLAbonnement);
				nbLigneAboSuppr++;
			}
		}
		System.out.println(nbLigneAboSuppr+" lignes abonnement viennent d'être annulés");
	}
	
	
	@TransactionTimeout(value=30,unit=TimeUnit.MINUTES)
	public void insertionPeriode() {
		List<TaAbonnement> listeAbo = selectAll();
		List<TaLEcheance> listeEcheanceAControlerManuellement = new ArrayList<TaLEcheance>();
		List<TaAbonnement> listeAbonnementAControlerManuellement = new ArrayList<TaAbonnement>();
		Integer aboTraite = 0;
		Integer aboParcourus = 0;
		Integer aboSansDatePeriode = 0;
		//on parcours tous les abonnements
		for (TaAbonnement abo : listeAbo) {
			aboParcourus++;
			TaLEcheance dernierEch = null;
			if(abo.getDateDebutPeriodeActive() == null) {
				aboSansDatePeriode++;
				List<TaLEcheance> listeEcheance = taLEcheanceService.rechercheEcheanceLieAAbonnement(abo);
				//on parcours toutes les échéances trouvées pour cet abonnement
				for (TaLEcheance taLEcheance : listeEcheance) {
					//si c'est une échéance totalement transformés (donc payée)
					if(taLEcheance.getTaREtat() != null) {
						
							//on trouve la dernière échéance (celle avec l'id le plus grand)
							if(dernierEch == null) {
								dernierEch = taLEcheance;
							}else if(taLEcheance.getIdLEcheance() > dernierEch.getIdLEcheance()){
									dernierEch = taLEcheance;
							}
						
						
					}
					
				}
			}
			//on attribue les dates périodes active avec les dates de période de la dernière échéance
			if(dernierEch != null) {
				if(dernierEch.getTaREtat().getTaEtat().getCodeEtat().equals("doc_tot_Transforme")) {
					aboTraite++;
					abo.setDateDebutPeriodeActive(dernierEch.getDebutPeriode());
					abo.setDateFinPeriodeActive(dernierEch.getFinPeriode());
					merge(abo);
				}else {
					listeEcheanceAControlerManuellement.add(dernierEch);
					listeAbonnementAControlerManuellement.add(dernierEch.getTaAbonnement());
				}
				
			}
			
		}
		System.out.println(aboSansDatePeriode+" abonnement sans dates périodes active");
		System.out.println(aboParcourus+" abonnement parcourus en tout");
		System.out.println(aboTraite+" abonnement viennent d'avoir leur date période affecté");
		System.out.println(listeEcheanceAControlerManuellement.size()+" échéances sont à controller manuellement dont "+listeAbonnementAControlerManuellement.size()+" abonnements");
		
		System.out.println("liste d'abonnement à controler : ");
		for (TaAbonnement taAbonnement : listeAbonnementAControlerManuellement) {
			System.out.println(taAbonnement.getCodeDocument());
		}
		System.out.println("liste d'échéances à controler : ");
		for (TaLEcheance ech : listeEcheanceAControlerManuellement) {
			System.out.println(ech.getIdLEcheance());
		}
		
		
		
		
	}
	@TransactionTimeout(value=30,unit=TimeUnit.MINUTES)
	public void insertionPeriodeV2(){
		//List<TaAbonnement> listeAbo = selectAllPourInsertionPeriodeV2();
		List<TaAbonnement> listeAbo = findAllSansDatesPeriode();
		System.out.println(listeAbo.size());
		Integer aboTraite = 0;
		//on parcours tous les abonnements
		for (TaAbonnement abo : listeAbo) {
			TaLEcheance dernierEch = null;
			if(abo.getDateDebutPeriodeActive() == null) {
						List<TaLEcheance> listeEcheance = taLEcheanceService.rechercheEcheanceLieAAbonnement(abo);
						//on parcours toutes les échéances trouvées pour cet abonnement
						for (TaLEcheance taLEcheance : listeEcheance) {
							//si c'est une échéance totalement transformés (donc payée)
							if(taLEcheance.getTaREtat() != null 
									&& taLEcheance.getTaREtat().getTaEtat().getCodeEtat().equals("doc_tot_Transforme")) {
								
									//on trouve la dernière échéance (celle avec l'id le plus grand)
									if(dernierEch == null) {
										dernierEch = taLEcheance;
									}else if(taLEcheance.getIdLEcheance() > dernierEch.getIdLEcheance()){
											dernierEch = taLEcheance;
									}
								
								
							}
							
						}
					}
					//on attribue les dates périodes active avec les dates de période de la dernière échéance
					if(dernierEch != null) {
						if(dernierEch.getTaREtat().getTaEtat().getCodeEtat().equals("doc_tot_Transforme")) {
							abo.setDateDebutPeriodeActive(dernierEch.getDebutPeriode());
							abo.setDateFinPeriodeActive(dernierEch.getFinPeriode());
							merge(abo);
							aboTraite++;
						}
						
					}
					
				}
				
		System.out.println(aboTraite+" abonnement viennent d'avoir leur date période affecté");
	}
	
	
	
	
	@TransactionTimeout(value=30,unit=TimeUnit.MINUTES)
	public void insertionLigneALigneEcheanceAvis() {
		List<TaLEcheance> listeEcheance = new ArrayList<TaLEcheance>();
		listeEcheance = taLEcheanceService.findAllLieAAvisEcheanceTiers(null);
		
		//on parcous toutes les échéances liées a une ligne avis
		for (TaLEcheance taLEcheance : listeEcheance) {
			
			List<TaLAvisEcheance> lignesAvis = taLAvisEcheanceService.findAllByIdEcheance(taLEcheance.getIdLEcheance());
			//on parcours toutes les lignes avis lié a cette échéance
			for (TaLAvisEcheance ligneAvis : lignesAvis) {
				
				//on controle que ce se soit pas une ligne de commentaire
				if(ligneAvis.getTaArticle() != null) {
					//on insère un ligne a ligne échéance
					TaLigneALigneEcheance lal = new TaLigneALigneEcheance();
					lal.setIdLigneSrc(taLEcheance.getIdLEcheance());
					lal.setTaLAvisEcheance(ligneAvis); 
					lal.setTaLEcheance(taLEcheance);
					//on passe la colonne abonnement de la ligne avis a true
					ligneAvis.setAbonnement(true);
					
					taLAvisEcheanceService.merge(ligneAvis);
					taLigneALigneEcheanceService.merge(lal);
				}
				
			}
			
		}
	}
	
	
	@TransactionTimeout(value=60,unit=TimeUnit.MINUTES)
	public void insertionLigneALigneEcheanceFacture() {
		List<TaLEcheance> listeEcheance = new ArrayList<TaLEcheance>();
		listeEcheance = taLEcheanceService.findAllLieAAvisEcheancePayeTiers(null);
		
		//on parcous toutes les échéances liées a une ligne avis et a une facture
		for (TaLEcheance taLEcheance : listeEcheance) {
			
			List<TaLAvisEcheance> lignesAvis = taLAvisEcheanceService.findAllByIdEcheanceSansLigneCom(taLEcheance.getIdLEcheance());
			//on parcours toutes les lignes avis lié a cette échéance
			for (TaLAvisEcheance ligneAvis : lignesAvis) {
					//on récupère le le ligne a ligne entre cette ligne avis et la ligne facture
					//Set<TaLigneALigne> listeLal = ligneAvis.getTaLigneALignes();
					List<TaLigneALigne> listeLal = taLigneALigneService.findByLDocument(ligneAvis);
					//on parcours les lignes liées a cette ligne avis
					for (TaLigneALigne lalAvis : listeLal) {
						//si cette ligne d'avis est lié a une ligne de facture
						if(lalAvis.getTaLFacture() != null) {
							
							//on insère un ligne a ligne échéance
							TaLigneALigneEcheance lal = new TaLigneALigneEcheance();
							lal.setIdLigneSrc(taLEcheance.getIdLEcheance());
							lal.setTaLFacture(lalAvis.getTaLFacture()); 
							lal.setTaLEcheance(taLEcheance);
							//on passe la colonne abonnement de la ligne avis a true
							lalAvis.getTaLFacture().setAbonnement(true);
							
							taLFactureService.merge(lalAvis.getTaLFacture());
							taLigneALigneEcheanceService.merge(lal);
							
						}
					}
					
					


				
			}
			
		}
		
	}
	@TransactionTimeout(value=60,unit=TimeUnit.MINUTES)
	public void addTaJourRelance() {
		List<TaArticle> articles = taArticleService.selectAll();
		
		for (TaArticle taArticle : articles) {
			List<TaJourRelanceDTO> relances = taJourRelanceService.findByIdArticleDTO(taArticle.getIdArticle());
			Boolean existeDeja = false;
			
			for (TaJourRelanceDTO rel : relances) {
				if(rel.getNbJour() == 20) {
					existeDeja = true;
				}
			}
			if(!existeDeja) {
				TaJourRelance jourRelance = new TaJourRelance();
				jourRelance.setTaArticle(taArticle);
				jourRelance.setNbJour(20);
				
				taJourRelanceService.merge(jourRelance);
			}
			
		}
	}
	
	
	@TransactionTimeout(value=30,unit=TimeUnit.MINUTES)
	public void insertionLigneALigneClassiqueAboAvis() {
		
	}

	
	@Override
	public Integer genereProchainesEcheancesRegul() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer generePremiereEcheancesRegul() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer supprimeAvisEcheanceFaux() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer supprimeEcheanceEnCours() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer supprimeTousAvisEcheance() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void supprimeTousRDocumentEvenementAvisEcheance() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void metADispositionTousAvisEcheance() {
		// TODO Auto-generated method stub
		
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

