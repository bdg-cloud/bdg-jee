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
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.article.dao.ITvaDAO;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTicketDeCaisseServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLigneALigneEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaReglementServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaAutorisationsDossierServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.edition.osgi.EditionProgrammeDefaut;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.mapper.TaTicketDeCaisseMapper;
import fr.legrain.bdg.paiement.service.remote.IPaiementEnLigneDossierService;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaCompteServiceWebExterneServiceRemote;
import fr.legrain.birt.BirtUtil;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.data.AbstractDocumentService;
import fr.legrain.document.dashboard.dto.CountDocumentParTypeRegrouptement;
import fr.legrain.document.dashboard.dto.TaArticlesParTiersDTO;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.dto.TaTicketDeCaisseDTO;
import fr.legrain.document.model.LigneTva;
import fr.legrain.document.model.SWTDocument;
import fr.legrain.document.model.TaAbonnement;
import fr.legrain.document.model.TaBoncdeAchat;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaLFacture;
import fr.legrain.document.model.TaTicketDeCaisse;
import fr.legrain.document.model.TaLTicketDeCaisse;
import fr.legrain.document.model.TaLTicketDeCaisse;
import fr.legrain.document.model.TaMiseADisposition;
import fr.legrain.document.model.TaProforma;
import fr.legrain.document.model.TaRAcompte;
import fr.legrain.document.model.TaRAvoir;
import fr.legrain.document.model.TaRReglement;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.documents.dao.IAvoirDAO;
import fr.legrain.documents.dao.IFactureDAO;
import fr.legrain.documents.dao.IReglementDAO;
import fr.legrain.documents.dao.ITicketDeCaisseDAO;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.droits.model.IModulesProgramme;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.edition.model.TaActionEdition;
import fr.legrain.hibernate.multitenant.SchemaResolver;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.IHMEtat;
import fr.legrain.lib.data.LibCalcul;
import fr.legrain.paiement.model.PaiementCarteBancaire;
import fr.legrain.paiement.model.RetourPaiementCarteBancaire;
import fr.legrain.paiement.service.MyQualifierLiteral;
import fr.legrain.paiement.service.PaiementLgr;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaTServiceWebExterne;
import fr.legrain.servicewebexterne.model.UtilServiceWebExterne;
import fr.legrain.tiers.dao.ICPaiementDAO;
import fr.legrain.tiers.dao.ICompteBanqueDAO;
import fr.legrain.tiers.dao.ITCPaiementDAO;
import fr.legrain.tiers.dao.ITPaiementDAO;
import fr.legrain.tiers.model.TaCPaiement;
import fr.legrain.tiers.model.TaTCPaiement;
import fr.legrain.tiers.model.TaTiers;

/**
 * Session Bean implementation class TaTicketDeCaisseBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaTicketDeCaisseService extends AbstractDocumentService<TaTicketDeCaisse, TaTicketDeCaisseDTO> implements ITaTicketDeCaisseServiceRemote {

	static Logger logger = Logger.getLogger(TaTicketDeCaisseService.class);

	@Inject private ITicketDeCaisseDAO dao;
	@Inject private IAvoirDAO daoAvoir;
	@Inject private IReglementDAO daoReglement;
	@Inject private ITvaDAO taTvaDAO;
	@Inject private ITCPaiementDAO taTCPaiementDAO;
	@Inject private ICPaiementDAO taCPaiementDAO;
	@Inject private ITPaiementDAO taTPaiementDAO;
	@EJB private ITaReglementServiceRemote taReglementService;
	@Inject private ICompteBanqueDAO taCompteBanqueDAO;
	@EJB private ITaGenCodeExServiceRemote gencode;
//	@EJB private ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	@EJB private ITaPreferencesServiceRemote taPreferencesService;
	@Inject private	SessionInfo sessionInfo;
	@Inject private	TenantInfo tenantInfo;
	
	@EJB private ITaLEcheanceServiceRemote taLEcheanceService;
	@EJB private ITaLigneALigneEcheanceServiceRemote taLigneALigneEcheanceService;
	@EJB private ITaAutorisationsDossierServiceRemote taAutorisationDossierService;
	
	/**
	 * Default constructor. 
	 */
	public TaTicketDeCaisseService() {
		super(TaTicketDeCaisse.class,TaTicketDeCaisseDTO.class);
		//editionDefaut = "/reports/documents/facture/FicheFacture.rptdesign";
		editionDefaut = EditionProgrammeDefaut.EDITION_DEFAUT_FACTURE_FICHIER;
	}
	
	//	private String defaultJPQLQuery = "select a from TaTicketDeCaisse a";
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public String generePDF(int idFacture, Map<String,Object> mapParametreEdition, List<String> listResourcesPath, String theme) {
		return generePDF(idFacture,editionDefaut,mapParametreEdition, null, null);
	}
	
	public String generePDF(int idFacture, String modeleEdition, Map<String,Object> mapParametreEdition, List<String> listResourcesPath, String theme) {
		try {
			SchemaResolver sr = new SchemaResolver();
			String localPath = bdgProperties.osTempDirDossier(sr.resolveCurrentTenantIdentifier())+"/"+bdgProperties.tmpFileName("facture.pdf");
			System.out.println(localPath);
			System.out.println(bdgProperties.urlDemoHttps()+modeleEdition);
			
			Map<String,Object> mapEdition = new HashMap<String,Object>();

			TaTicketDeCaisse doc = findById(idFacture);
			doc.calculeTvaEtTotaux();

			mapEdition.put("doc",doc );

			mapEdition.put("taInfoEntreprise", entrepriseService.findInstance());

			mapEdition.put("lignes", doc.getLignes());

			mapEdition.put("taRReglement", doc.getTaRReglements());
			
			//Traitement des différents règlements
			List<TaRReglement> listeReglement =new LinkedList<>(); 
			List<TaRReglement> listeAcompte =new LinkedList<>();
			List<TaRAvoir> listeAvoir =new LinkedList<>();
			for (TaRReglement ligne : doc.getTaRReglements()) {
				if(ligne.getTaReglement().getTaAcompte()==null)
					listeReglement.add(ligne);
				else
					listeAcompte.add(ligne);
			}
			
			mapEdition.put("listeReglement", listeReglement);
			mapEdition.put("listeAcompte", listeAcompte);
			mapEdition.put("listeAvoir", doc.getTaRAvoirs());
			
			if(mapParametreEdition == null) {
				mapParametreEdition = new HashMap<String,Object>();
			}
			List<TaPreferences> liste= taPreferencesService.findByGroupe("facture");
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
				listResourcesPath.add(EditionProgrammeDefaut.LIBRAIRIE_EDITION_DEFAUT_FACTURE);
			}else if(listResourcesPath.isEmpty()) {
					listResourcesPath = new ArrayList<String>();
					listResourcesPath.add(EditionProgrammeDefaut.LIBRAIRIE_EDITION_DEFAUT_FACTURE);
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
	
	public List<TaTicketDeCaisse> findByCodeTiersAndDateCompteClient(String codeTiers, Date debut, Date fin) {
		return dao.findByCodeTiersAndDateCompteClient(codeTiers, debut, fin);
	}
	
	public String genereCode( Map<String, String> params) {
		//return dao.genereCode();
		try {
			return gencode.genereCodeJPA(TaTicketDeCaisse.class.getSimpleName(),params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NOUVEAU CODE";
	}
	
	public void annuleCode(String code) {
		try {
			
			gencode.annulerCodeGenere(gencode.findByCode(TaTicketDeCaisse.class.getSimpleName()),code);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void verrouilleCode(String code) {
		try {
			gencode.rentreCodeGenere(gencode.findByCode(TaTicketDeCaisse.class.getSimpleName()),code, sessionInfo.getSessionID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	/**
//	 * Repartir le total chaque code TVA sur l'ensemble des lignes concernées par ce code. 
//	 */
//	public void dispatcherTva(TaTicketDeCaisse doc) {
//		
//		BigDecimal tvaLigne = new BigDecimal(0); //Montant de TVA de la ligne du document courante
//		BigDecimal totalTemp = new BigDecimal(0); //Somme des montants HT des lignes du document (mis à jour au fil des iterations)
//
//		boolean derniereLignePourTVA = false;
//
//		for (Object ligne : doc.getLignes()) {
//			if(((TaLTicketDeCaisse)ligne).getMtHtLDocument()!=null)
//				totalTemp = totalTemp.add(((TaLTicketDeCaisse)ligne).getMtHtLDocument());
//		}
//		if(totalTemp!=null && doc.getTxRemHtDocument()!=null)
//			doc.setRemHtDocument(totalTemp.multiply(doc.getTxRemHtDocument().divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_HALF_UP));	
//		
//		for (TaLTicketDeCaisse ligne : doc.getLignes()) {
//			if(doc.getTxRemHtDocument()!=null && doc.getTxRemHtDocument().signum()>0 && ligne.getMtHtLDocument()!=null  && ligne.getMtTtcLDocument()!=null) {
//				if(doc.getTtc()==1){
//					((TaLTicketDeCaisse)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLTicketDeCaisse)ligne).getMtTtcLDocument().subtract(((TaLTicketDeCaisse)ligne).getMtTtcLDocument()
//							.multiply(doc.getTxRemHtDocument()).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
//					((TaLTicketDeCaisse)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLTicketDeCaisse)ligne).getMtTtcLApresRemiseGlobaleDocument());
//					
//				}else{
//					((TaLTicketDeCaisse)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLTicketDeCaisse)ligne).getMtHtLDocument().subtract(((TaLTicketDeCaisse)ligne).getMtHtLDocument()
//							.multiply(doc.getTxRemHtDocument()).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
//					((TaLTicketDeCaisse)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLTicketDeCaisse)ligne).getMtHtLApresRemiseGlobaleDocument());	
//				}
//			}
//		}
//		
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
//				//TaLTicketDeCaisse derniereLigneFactureAvecMontantDifferentDeZero = null;
//				derniereLignePourTVA = false;
//
//				//pour chaque ligne du document
//				for (Object ligne : doc.getLignes()) {
//					//si c'est une ligne "normale" (ligne HT et non une ligne de commentaire ou autre)
//					if(((TaLTicketDeCaisse)ligne).getTaTLigne().getCodeTLigne().equals(SWTDocument.C_CODE_T_LIGNE_H)) {
//						//si le code TVA de la ligne correspond à celui traite (boucle superieure)
//						if(((TaLTicketDeCaisse)ligne).getCodeTvaLDocument()!=null&&((TaLTicketDeCaisse)ligne).getCodeTvaLDocument().equals(ligneTva.getCodeTva())){
//							tvaLigne = prorataMontantTVALigne(doc,(TaLTicketDeCaisse)ligne, ligneTva);
//							
//							tvaTmp =  tvaTmp.subtract(tvaLigne);
//							if(tvaTmp.compareTo(resteTVA(doc,ligneTva))==0 && !derniereLignePourTVA) {
//								//Le reste de TVA a traiter correspond a la difference d'arrondi,
//								//les lignes de documents suivantes (s'il en reste) ont un montant HT nul
//								//c'est donc la derniere ligne sur laquelle on peut mettre de la TVA => on ajoute le reliquat
//								tvaLigne = tvaLigne.add(tvaTmp);
//								derniereLignePourTVA = true;
//							}
//							totalTemp = totalTemp.add(((TaLTicketDeCaisse)ligne).getMtHtLDocument());
//
//							//===Correction des totaux après remise de la ligne du document
//							if(doc.getTxRemHtDocument()!=null && doc.getTxRemHtDocument().signum()>0) {
//								if  (lignepasse>= ligneTva.getNbLigneDocument()) {
//									((TaLTicketDeCaisse)ligne).setMtHtLApresRemiseGlobaleDocument(htTmp);
//									((TaLTicketDeCaisse)ligne).setMtTtcLApresRemiseGlobaleDocument(ttcTmp);
//								} else {
//									if(doc.getTtc()==1){
//										((TaLTicketDeCaisse)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLTicketDeCaisse)ligne).getMtTtcLDocument().subtract(((TaLTicketDeCaisse)ligne).getMtTtcLDocument()
//												.multiply(doc.getTxRemHtDocument()).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
//										((TaLTicketDeCaisse)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLTicketDeCaisse)ligne).getMtTtcLApresRemiseGlobaleDocument().divide(BigDecimal.valueOf(1).add(
//												 (((TaLTicketDeCaisse)ligne).getTauxTvaLDocument().divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(3,BigDecimal.ROUND_HALF_UP)
//											)),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP));
//										
//									}else{
//										((TaLTicketDeCaisse)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLTicketDeCaisse)ligne).getMtHtLDocument().subtract(((TaLTicketDeCaisse)ligne).getMtHtLDocument()
//												.multiply(doc.getTxRemHtDocument()).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
//										tvaCalcule = (((TaLTicketDeCaisse)ligne).getMtHtLApresRemiseGlobaleDocument().
//										multiply(((TaLTicketDeCaisse)ligne).getTauxTvaLDocument().divide(new BigDecimal(100),MathContext.DECIMAL128))).setScale(2,BigDecimal.ROUND_HALF_UP);
//										((TaLTicketDeCaisse)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLTicketDeCaisse)ligne).getMtHtLApresRemiseGlobaleDocument().add(tvaCalcule));	
//									}
////									((TaLTicketDeCaisse)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLTicketDeCaisse)ligne).getMtTtcLDocument().subtract(((TaLTicketDeCaisse)ligne).getMtTtcLDocument()
////											.multiply(txRemHtDocument).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
//								}
//
//							} else {
//								if(doc.getTtc()==1)
//									if  (lignepasse>= ligneTva.getNbLigneDocument()) {
//										((TaLTicketDeCaisse)ligne).setMtHtLApresRemiseGlobaleDocument(htTmp);
//									}else{
//										((TaLTicketDeCaisse)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLTicketDeCaisse)ligne).getMtTtcLDocument().subtract(tvaLigne));
//									}
//								else
//									if  (lignepasse>= ligneTva.getNbLigneDocument()) {
//										((TaLTicketDeCaisse)ligne).setMtTtcLApresRemiseGlobaleDocument(ttcTmp);
//									}else {
//										((TaLTicketDeCaisse)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLTicketDeCaisse)ligne).getMtHtLDocument().add(tvaLigne));
//									}
//
//							}
//							ttcTmp =  ttcTmp.subtract(((TaLTicketDeCaisse)ligne).getMtTtcLApresRemiseGlobaleDocument());
//							htTmp =  htTmp.subtract(((TaLTicketDeCaisse)ligne).getMtHtLApresRemiseGlobaleDocument());
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
//
//
//	}
	
	/**
	 * Calcule le montant de TVA d'une ligne du document par rapport au montant total de TVA pour un code TVA donnee
	 * @param ligne - 
	 * @param ligneTva
	 * @return
	 */
	public BigDecimal prorataMontantTVALigne(TaTicketDeCaisse doc, TaLTicketDeCaisse ligne, LigneTva ligneTva) {
		BigDecimal tvaLigne = new BigDecimal(0);
		
		if (ligneTva.getMontantTotalHt().signum()==0) 
			tvaLigne = ((TaLTicketDeCaisse)ligne).getMtHtLDocument().multiply(ligneTva.getTauxTva()).divide(new BigDecimal(100));
		else {
//			if  (lignepasse>= ligneTva.getNbLigneDocument()) //si c'est la deniere ligne, on prend tout ce qui reste
//				tvaLigne = tvaTmp;
//			else {
				if(doc.getTtc()==1){ //si saisie TTC
					if(LibCalcul.arrondi(ligneTva.getMontantTotalTtcAvecRemise()).signum()<=0)
						tvaLigne=BigDecimal.valueOf(0);
					else
						tvaLigne = (ligneTva.getMtTva().multiply(((TaLTicketDeCaisse)ligne).getMtTtcLDocument())).divide(LibCalcul.
								arrondi(ligneTva.getMontantTotalTtcAvecRemise()),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
				}
				else{
					if(LibCalcul.arrondi(ligneTva.getMontantTotalHtAvecRemise()).signum()<=0)
						tvaLigne =BigDecimal.valueOf(0);
					else
						tvaLigne = (ligneTva.getMtTva().multiply(((TaLTicketDeCaisse)ligne).getMtHtLDocument())).divide(LibCalcul.
								arrondi(ligneTva.getMontantTotalHtAvecRemise()),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
				}
//			}
		}
		return tvaLigne;
	}
	
	/**
	 * Calcule le montant de TVA d'une ligne du document par rapport au montant total de TVA pour un code TVA donnee <b>avant remise</b>
	 * @param ligne
	 * @param ligneTva
	 * @return
	 */
	public BigDecimal prorataMontantTVALigneAvantRemise(TaTicketDeCaisse doc, TaLTicketDeCaisse ligne, LigneTva ligneTva) {
		BigDecimal tvaLigne = new BigDecimal(0);
		
		if (ligneTva.getMontantTotalHt().signum()==0) 
			tvaLigne = ((TaLTicketDeCaisse)ligne).getMtHtLDocument().multiply(ligneTva.getTauxTva()).divide(new BigDecimal(100));
		else {
//			if  (lignepasse>= ligneTva.getNbLigneDocument()) 
//				tvaLigne = tvaAvantRemiseTmp;
//			else {
				if(doc.getTtc()==1){
					if(LibCalcul.arrondi(ligneTva.getMontantTotalTtc()).signum()<=0)
						tvaLigne=BigDecimal.valueOf(0);
					else
						tvaLigne = (ligneTva.getMtTvaAvantRemise().multiply(((TaLTicketDeCaisse)ligne).getMtTtcLDocument())).divide(LibCalcul.arrondi(ligneTva.getMontantTotalTtc()),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
				}
				else{
					if(LibCalcul.arrondi(ligneTva.getMontantTotalHt()).signum()<=0)
						tvaLigne =BigDecimal.valueOf(0);
					else
						tvaLigne = (ligneTva.getMtTvaAvantRemise().multiply(((TaLTicketDeCaisse)ligne).getMtHtLDocument())).divide(LibCalcul.arrondi(ligneTva.getMontantTotalHt()),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
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
	public BigDecimal resteTVAAvantRemise(TaTicketDeCaisse doc, LigneTva ligneTva) {
		BigDecimal resteTVA = ligneTva.getMtTva();
		for (Object ligne : doc.getLignes()) {
			//si c'est une ligne "normale" (ligne HT et non une ligne de commentaire ou autre)
			if(((TaLTicketDeCaisse)ligne).getTaTLigne().getCodeTLigne().equals(SWTDocument.C_CODE_T_LIGNE_H)) {
				//si le code TVA de la ligne correspond à celui traite (boucle superieure)
				if(((TaLTicketDeCaisse)ligne).getCodeTvaLDocument()!=null&&((TaLTicketDeCaisse)ligne).getCodeTvaLDocument().equals(ligneTva.getCodeTva())){
					resteTVA = resteTVA.subtract(prorataMontantTVALigneAvantRemise(doc,((TaLTicketDeCaisse)ligne),ligneTva));
				}
			}
		}
		return resteTVA;
	}
	
	/**
	 * Calcule le montant de TVA qui reste après répartion de la TVA sur les lignes au prorata du monant HT.
	 * Ce montant de TVA restant de 1 ou 2 centimes provient des arrondis successifs.
	 * @param ligneTva
	 * @return
	 */
	public BigDecimal resteTVA(TaTicketDeCaisse doc, LigneTva ligneTva) {
		BigDecimal resteTVA = ligneTva.getMtTva();
		for (Object ligne : doc.getLignes()) {
			//si c'est une ligne "normale" (ligne HT et non une ligne de commentaire ou autre)
			if(((TaLTicketDeCaisse)ligne).getTaTLigne().getCodeTLigne().equals(SWTDocument.C_CODE_T_LIGNE_H)) {
				//si le code TVA de la ligne correspond à celui traite (boucle superieure)
				if(((TaLTicketDeCaisse)ligne).getCodeTvaLDocument()!=null&&((TaLTicketDeCaisse)ligne).getCodeTvaLDocument().equals(ligneTva.getCodeTva())){
					resteTVA = resteTVA.subtract(prorataMontantTVALigne(doc,((TaLTicketDeCaisse)ligne),ligneTva));
				}
			}
		}
		return resteTVA;
	}

	/**
	 * Repartir le total chaque code TVA sur l'ensemble des lignes concernées par ce code. 
	 */
	public void dispatcherTva(TaTicketDeCaisse doc) {

		BigDecimal tvaLigne = new BigDecimal(0);
		BigDecimal totalTemp = new BigDecimal(0);

		for (LigneTva ligneTva : doc.getLignesTVA()) {

			if (ligneTva.getMtTva()!=null) {
				int lignepasse=1;
				BigDecimal tvaTmp = ligneTva.getMtTva();
				BigDecimal ttcTmp = LibCalcul.arrondi(ligneTva.getMontantTotalTtcAvecRemise());
				BigDecimal htTmp = LibCalcul.arrondi(ligneTva.getMontantTotalHtAvecRemise());
				

				for (Object ligne : doc.getLignes()) {
					if(((TaLTicketDeCaisse)ligne).getTaTLigne().getCodeTLigne().equals(Const.C_CODE_T_LIGNE_H)) {
						if(((TaLTicketDeCaisse)ligne).getCodeTvaLDocument()!=null&&((TaLTicketDeCaisse)ligne).getCodeTvaLDocument().equals(ligneTva.getCodeTva())){
							if (ligneTva.getMontantTotalHt().signum()==0) 
								tvaLigne = ((TaLTicketDeCaisse)ligne).getMtHtLDocument().multiply(ligneTva.getTauxTva()).divide(new BigDecimal(100));
							else {
								if  (lignepasse>= ligneTva.getNbLigneDocument()) 
									tvaLigne = tvaTmp;
								else {
									if(doc.getTtc()==1){
										if(LibCalcul.arrondi(ligneTva.getMontantTotalTtcAvecRemise()).signum()<=0)
											tvaLigne=BigDecimal.valueOf(0);
										else
											tvaLigne = (ligneTva.getMtTva().multiply(((TaLTicketDeCaisse)ligne).getMtTtcLDocument())).divide(LibCalcul.arrondi(ligneTva.getMontantTotalTtcAvecRemise()),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
									}
									else{
										if(LibCalcul.arrondi(ligneTva.getMontantTotalHtAvecRemise()).signum()<=0)
											tvaLigne =BigDecimal.valueOf(0);
										else
											tvaLigne = (ligneTva.getMtTva().multiply(((TaLTicketDeCaisse)ligne).getMtHtLDocument())).divide(LibCalcul.arrondi(ligneTva.getMontantTotalHtAvecRemise()),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
									}
								}
							}
							tvaTmp =  tvaTmp.subtract(tvaLigne);
							totalTemp = totalTemp.add(((TaLTicketDeCaisse)ligne).getMtHtLDocument());

							if(doc.getTxRemHtDocument()!=null && doc.getTxRemHtDocument().signum()>0) {
								if  (lignepasse>= ligneTva.getNbLigneDocument()) {
									((TaLTicketDeCaisse)ligne).setMtHtLApresRemiseGlobaleDocument(htTmp);
									((TaLTicketDeCaisse)ligne).setMtTtcLApresRemiseGlobaleDocument(ttcTmp);
								} else {
									((TaLTicketDeCaisse)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLTicketDeCaisse)ligne).getMtHtLDocument().subtract(((TaLTicketDeCaisse)ligne).getMtHtLDocument().multiply(doc.getTxRemHtDocument()).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
									((TaLTicketDeCaisse)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLTicketDeCaisse)ligne).getMtTtcLDocument().subtract(((TaLTicketDeCaisse)ligne).getMtTtcLDocument().multiply(doc.getTxRemHtDocument()).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
								}
//								ttcTmp =  ttcTmp.subtract(((TaLTicketDeCaisse)ligne).getMtTtcLFacture());
//								htTmp =  htTmp.subtract(((TaLTicketDeCaisse)ligne).getMtHtLFacture());
							} else {
								if(doc.getTtc()==1)
									if  (lignepasse>= ligneTva.getNbLigneDocument()) {
										((TaLTicketDeCaisse)ligne).setMtHtLApresRemiseGlobaleDocument(htTmp);
									}else{
										((TaLTicketDeCaisse)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLTicketDeCaisse)ligne).getMtTtcLDocument().subtract(tvaLigne));
									}
								else
									if  (lignepasse>= ligneTva.getNbLigneDocument()) {
										((TaLTicketDeCaisse)ligne).setMtTtcLApresRemiseGlobaleDocument(ttcTmp);
									}else {
										((TaLTicketDeCaisse)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLTicketDeCaisse)ligne).getMtHtLDocument().add(tvaLigne));
									}

							}
							ttcTmp =  ttcTmp.subtract(((TaLTicketDeCaisse)ligne).getMtTtcLApresRemiseGlobaleDocument());
							htTmp =  htTmp.subtract(((TaLTicketDeCaisse)ligne).getMtHtLApresRemiseGlobaleDocument());

							lignepasse++;
						}
					}
					doc.setRemHtDocument(totalTemp.multiply(doc.getTxRemHtDocument().divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_HALF_UP));						

//					setRemHtDocument(getRemHtDocument().add(totalTemp.multiply(txRemHtDocument.divide(new BigDecimal(100)))));						

				}
			}
		}
//		}

	}
	
//	public void dispatcherTvaAvantRemise(TaTicketDeCaisse doc) {
//		BigDecimal tvaLigne = new BigDecimal(0);
//		
//		boolean derniereLignePourTVA = false;
//
//		for (LigneTva ligneTva : doc.getLignesTVA()) {
//			if (ligneTva.getMtTvaAvantRemise()!=null) {
//				int lignepasse=1;
//				BigDecimal tvaAvantRemiseTmp = ligneTva.getMtTvaAvantRemise();
//				
//				derniereLignePourTVA = false;
//
//				for (Object ligne : doc.getLignes()) {
//					if(((TaLTicketDeCaisse)ligne).getTaTLigne().getCodeTLigne().equals(SWTDocument.C_CODE_T_LIGNE_H)) {
//						if(((TaLTicketDeCaisse)ligne).getCodeTvaLDocument()!=null&&((TaLTicketDeCaisse)ligne).getCodeTvaLDocument().equals(ligneTva.getCodeTva())){
////							if (ligneTva.getMontantTotalHt().signum()==0) 
////								tvaLigne = ((TaLTicketDeCaisse)ligne).getMtHtLDocument().multiply(ligneTva.getTauxTva()).divide(new BigDecimal(100));
////							else {
////								if  (lignepasse>= ligneTva.getNbLigneDocument()) 
////									tvaLigne = tvaAvantRemiseTmp;
////								else {
////									if(ttc==1){
////										if(LibCalcul.arrondi(ligneTva.getMontantTotalTtc()).signum()<=0)
////											tvaLigne=BigDecimal.valueOf(0);
////										else
////											tvaLigne = (ligneTva.getMtTvaAvantRemise().multiply(((TaLTicketDeCaisse)ligne).getMtTtcLDocument())).divide(LibCalcul.arrondi(ligneTva.getMontantTotalTtc()),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
////									}
////									else{
////										if(LibCalcul.arrondi(ligneTva.getMontantTotalHt()).signum()<=0)
////											tvaLigne =BigDecimal.valueOf(0);
////										else
////											tvaLigne = (ligneTva.getMtTvaAvantRemise().multiply(((TaLTicketDeCaisse)ligne).getMtHtLDocument())).divide(LibCalcul.arrondi(ligneTva.getMontantTotalHt()),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
////									}
////								}
////							}
//							tvaLigne = prorataMontantTVALigneAvantRemise(doc,(TaLTicketDeCaisse)ligne, ligneTva);
//							
//							tvaAvantRemiseTmp =  tvaAvantRemiseTmp.subtract(tvaLigne);
//							
//							if(tvaAvantRemiseTmp.compareTo(resteTVA(doc,ligneTva))==0) {
//								//Le reste de TVA a traiter correspond a la difference d'arrondi,
//								//les lignes de documents suivantes (s'il en reste) ont un montant HT nul
//								//c'est donc la derniere ligne sur laquelle on peut mettre de la TVA => on ajoute le reliquat
//								tvaLigne = tvaLigne.add(tvaAvantRemiseTmp);
//								derniereLignePourTVA = true;
//							}
//
//							if(doc.getTtc()==1)
//								((TaLTicketDeCaisse)ligne).setMtHtLDocument(((TaLTicketDeCaisse)ligne).getMtTtcLDocument().subtract(tvaLigne));
//							else
//								((TaLTicketDeCaisse)ligne).setMtTtcLDocument(((TaLTicketDeCaisse)ligne).getMtHtLDocument().add(tvaLigne));
//
//							lignepasse++;
//						}
//					}
//
//				}
//			}
//		}
//	}
	
	public void dispatcherTvaAvantRemise(TaTicketDeCaisse doc) {
		BigDecimal tvaLigne = new BigDecimal(0);

		for (LigneTva ligneTva : doc.getLignesTVA()) {
			if (ligneTva.getMtTvaAvantRemise()!=null) {
				int lignepasse=1;
				BigDecimal tvaAvantRemiseTmp = ligneTva.getMtTvaAvantRemise();

				for (Object ligne : doc.getLignes()) {
					if(((TaLTicketDeCaisse)ligne).getTaTLigne().getCodeTLigne().equals(Const.C_CODE_T_LIGNE_H)) {
						if(((TaLTicketDeCaisse)ligne).getCodeTvaLDocument()!=null&&((TaLTicketDeCaisse)ligne).getCodeTvaLDocument().equals(ligneTva.getCodeTva())){
							if (ligneTva.getMontantTotalHt().signum()==0) 
								tvaLigne = ((TaLTicketDeCaisse)ligne).getMtHtLDocument().multiply(ligneTva.getTauxTva()).divide(new BigDecimal(100));
							else {
								if  (lignepasse>= ligneTva.getNbLigneDocument()) 
									tvaLigne = tvaAvantRemiseTmp;
								else {
									if(doc.getTtc()==1){
										if(LibCalcul.arrondi(ligneTva.getMontantTotalTtc()).signum()<=0)
											tvaLigne=BigDecimal.valueOf(0);
										else
											tvaLigne = (ligneTva.getMtTvaAvantRemise().multiply(((TaLTicketDeCaisse)ligne).getMtTtcLDocument())).divide(LibCalcul.arrondi(ligneTva.getMontantTotalTtc()),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
									}
									else{
										if(LibCalcul.arrondi(ligneTva.getMontantTotalHt()).signum()<=0)
											tvaLigne =BigDecimal.valueOf(0);
										else
											tvaLigne = (ligneTva.getMtTvaAvantRemise().multiply(((TaLTicketDeCaisse)ligne).getMtHtLDocument())).divide(LibCalcul.arrondi(ligneTva.getMontantTotalHt()),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
									}
								}
							}
							tvaAvantRemiseTmp =  tvaAvantRemiseTmp.subtract(tvaLigne);

							if(doc.getTtc()==1)
								((TaLTicketDeCaisse)ligne).setMtHtLDocument(((TaLTicketDeCaisse)ligne).getMtTtcLDocument().subtract(tvaLigne));
							else
								((TaLTicketDeCaisse)ligne).setMtTtcLDocument(((TaLTicketDeCaisse)ligne).getMtHtLDocument().add(tvaLigne));

							lignepasse++;
						}
					}

				}
			}
		}
	}

	/**
	 * Lance la fonction de calcul du montant sur chacunes des lignes du document.
	 */
	public void calculMontantLigneDocument(TaTicketDeCaisse doc) {
		for (Object ligne : doc.getLignes()) {
			((TaLTicketDeCaisse)ligne).calculMontant();
		}
	}
	
	/**
	 * Calcul de la grille de TVA en fonction de lignes du document et du taux de remise HT global.
	 * Mise à jour de la propriété <code>lignesTVA</code>
	 * et mise à jour du montant de la TVA dans les lignes du document
	 */
	public void calculTvaTotal(TaTicketDeCaisse doc) {
		calculMontantLigneDocument(doc);
		calculLignesTva(doc);
		dispatcherTvaAvantRemise(doc);
		dispatcherTva(doc);
	}

	/**
	 * Calcul de la grille de TVA en fonction de lignes du document et du taux de remise HT global.
	 * Mise à jour de la propriété <code>lignesTVA</code>
	 */
	public void calculLignesTva(TaTicketDeCaisse doc) {
		Map<String,BigDecimal> montantTotalHt = new HashMap<String,BigDecimal>();
		Map<String,BigDecimal> montantTotalTtc = new HashMap<String,BigDecimal>();
		Map<String,BigDecimal> montantTotalHtAvecRemise = new HashMap<String,BigDecimal>();
		Map<String,BigDecimal> montantTotalTtcAvecRemise = new HashMap<String,BigDecimal>();
		Map<String,BigDecimal> mtTVA = new HashMap<String,BigDecimal>();
		Map<String,BigDecimal> mtTVAAvantRemise = new HashMap<String,BigDecimal>();
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
			//((TaLTicketDeCaisse)ligne).calculMontant();
			codeTVA = ((TaLTicketDeCaisse)ligne).getCodeTvaLDocument();
			if(codeTVA!=null && !codeTVA.equals("")) {
				ttcLigne = ((TaLTicketDeCaisse)ligne).getMtTtcLDocument();
				htLigne = ((TaLTicketDeCaisse)ligne).getMtHtLDocument();
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
					tauxTVA.put(codeTVA,((TaLTicketDeCaisse)ligne).getTauxTvaLDocument());
					nbLigne.put(codeTVA,1);
				}
			}
		}

		for (String codeTva : montantTotalTtc.keySet()) {
			//les 2 maps ont les meme cles
			BigDecimal mtTtcTotal = montantTotalTtc.get(codeTva);
			BigDecimal mtHtTotal = montantTotalHt.get(codeTva);
			BigDecimal tva =null;
			//traitement tva avant remise
			if (doc.getTtc()==1) {
				tva=mtTtcTotal.subtract((mtTtcTotal.multiply(BigDecimal.valueOf(100))) .divide((BigDecimal.valueOf(100).add(tauxTVA.get(codeTva))) ,MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)     ) ;
				mtTVAAvantRemise.put(codeTva, tva);
			} else {
				tva=mtHtTotal.multiply(   (tauxTVA.get(codeTva).divide(new BigDecimal(100)))) ;
				mtTVAAvantRemise.put(codeTva, tva );
			}
			//traitement remise
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
			//traitement tva après remise
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
			ligneTva.setMtTvaAvantRemise(mtTVAAvantRemise.get(codeTva));
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
	
	public BigDecimal calculSommeAvoirIntegres(TaTicketDeCaisse doc){
		BigDecimal sommeAvoir = new BigDecimal(0);
		for (TaRAvoir taRAvoir : doc.getTaRAvoirs()) {
			if(taRAvoir.getTaAvoir()!=null && (taRAvoir.getEtat()&IHMEtat.integre)!=0 && 
					(taRAvoir.getEtat()&IHMEtat.suppression)==0)
				sommeAvoir=sommeAvoir.add(taRAvoir.getAffectation());
		}
		//setAvoirs(sommeAvoir);
		return sommeAvoir;
	}
	public BigDecimal calculSommeAvoir(TaTicketDeCaisse doc){
		BigDecimal sommeAvoir = new BigDecimal(0);
		for (TaRAvoir taRAvoir : doc.getTaRAvoirs()) {
			if(taRAvoir.getTaAvoir()!=null && (taRAvoir.getEtat()&IHMEtat.suppression)==0)
				sommeAvoir=sommeAvoir.add(taRAvoir.getAffectation());
		}
		//setAvoirs(sommeAvoir);
		return sommeAvoir;
	}
	public BigDecimal calculSommeAvoir(TaTicketDeCaisse doc, TaRAvoir avoirEnCours){
		BigDecimal sommeAvoir = new BigDecimal(0);
		for (TaRAvoir taRAvoir : doc.getTaRAvoirs()) {
			if(taRAvoir.getTaAvoir()!=null && taRAvoir.getId()!=avoirEnCours.getId() && (taRAvoir.getEtat()&IHMEtat.suppression)==0)
				sommeAvoir=sommeAvoir.add(taRAvoir.getAffectation());
		}
		return sommeAvoir;
	}
	public BigDecimal calculSommeAcomptes(TaTicketDeCaisse doc, TaRAcompte acompteEnCours){
		BigDecimal sommeAcompte = new BigDecimal(0);
		for (TaRAcompte taRAcompte : doc.getTaRAcomptes()) {
			if(taRAcompte.getTaAcompte()!=null && taRAcompte.getId()!=acompteEnCours.getId() && !taRAcompte.isEtatDeSuppression())
				sommeAcompte=sommeAcompte.add(taRAcompte.getAffectation());
		}
		return sommeAcompte;
	}
	
	public void calculSommeAcomptes(TaTicketDeCaisse doc){
		BigDecimal sommeAcompte = new BigDecimal(0);
		for (TaRAcompte taRAcompte : doc.getTaRAcomptes()) {
			if(taRAcompte.getTaAcompte()!=null && !taRAcompte.isEtatDeSuppression())
				sommeAcompte=sommeAcompte.add(taRAcompte.getAffectation());
		}
		doc.setAcomptes(sommeAcompte);
	}
	
	public BigDecimal calculSommeReglementsIntegresEcran(TaTicketDeCaisse doc){
		Integer nbReglement=0;
		BigDecimal sommeReglements = new BigDecimal(0);
		for (TaRReglement taRReglement : doc.getTaRReglements()) {
			if(taRReglement.getTaTicketDeCaisse()!=null && (taRReglement.getEtat()&IHMEtat.integre)!=0 
					&& ((taRReglement.getEtatDeSuppression()&IHMEtat.suppression)==0))
				sommeReglements=sommeReglements.add(taRReglement.getAffectation());
			nbReglement++;
		}
		if(doc.getTaRReglement()!=null && !doc.getTaRReglements().contains(doc.getTaRReglement())&& 
				!multiReglement(doc))//((taRReglement.getEtatDeSuppression()&IHMEtat.multiple)==0)
			if(doc.getTaRReglement().getAffectation()!=null){
				sommeReglements=sommeReglements.add(doc.getTaRReglement().getAffectation());
				nbReglement++;
			}
		logger.debug(sommeReglements);
		logger.debug(nbReglement);	
		return sommeReglements;
}
	
	public BigDecimal calculSommeReglementsIntegres(TaTicketDeCaisse doc){
		Integer nbReglement=0;
		BigDecimal sommeReglements = new BigDecimal(0);
		for (TaRReglement taRReglement : doc.getTaRReglements()) {
			if(taRReglement.getTaTicketDeCaisse()!=null && (taRReglement.getEtat()&IHMEtat.integre)!=0 
					&& ((taRReglement.getEtatDeSuppression()&IHMEtat.suppression)==0))
				sommeReglements=sommeReglements.add(taRReglement.getAffectation());
			nbReglement++;
		}
		logger.debug(sommeReglements);
		logger.debug(nbReglement);
		return sommeReglements;
	}
	
	public Boolean aDesReglementsIndirects(TaTicketDeCaisse doc){
		for (TaRReglement taRReglement : doc.getTaRReglements()) {
			if(taRReglement.getTaTicketDeCaisse()!=null 
					&& (taRReglement.getEtat()&IHMEtat.integre)==0 
					&& ((taRReglement.getEtatDeSuppression()&IHMEtat.suppression)==0))
				return true;
		}
		return false;
	}
	
	public Boolean aDesAvoirsIndirects(TaTicketDeCaisse doc){
		for (TaRAvoir taRavoir : doc.getTaRAvoirs()) {
			if(taRavoir.getTaTicketDeCaisse()!=null 
					&& (taRavoir.getEtat()&IHMEtat.integre)==0 
					&& ((taRavoir.getEtat()&IHMEtat.suppression)==0))
				return true;
		}
		return false;
	}
	
	public BigDecimal calculSommeReglementsComplet(TaTicketDeCaisse doc){
		BigDecimal sommeReglements = new BigDecimal(0);
		for (TaRReglement taRReglement : doc.getTaRReglements()) {
			if(taRReglement.getTaTicketDeCaisse()!=null 
//					&& (taReglement.getTaReglement().getEtat()&IHMEtat.integre)==0 
					&& ((taRReglement.getEtatDeSuppression()&IHMEtat.suppression)==0))
				//if(taReglement.getAffectation()!=null)
					sommeReglements=sommeReglements.add(taRReglement.getAffectation());
		}
		return sommeReglements;
	}
	
	public BigDecimal calculSommeReglementsComplet(TaTicketDeCaisse doc,TaRReglement taRReglementEnCours){
		BigDecimal sommeReglements = new BigDecimal(0);
		for (TaRReglement taRReglement : doc.getTaRReglements()) {
			if(taRReglement.getTaTicketDeCaisse()!=null 
//					&& (taReglement.getTaReglement().getEtat()&IHMEtat.integre)==0 
					&& ((taRReglement.getEtatDeSuppression()&IHMEtat.suppression)==0) 
					&& taRReglementEnCours.getId()!=taRReglement.getId())
				//if(taReglement.getAffectation()!=null)
					sommeReglements=sommeReglements.add(taRReglement.getAffectation());
		}
		return sommeReglements;
	}
	
	public BigDecimal calculRegleDocumentComplet(TaTicketDeCaisse doc){
//		setRegleDocument(calculSommeReglementsIntegres().add(calculSommeReglementsNonIntegres()));
		doc.setRegleCompletDocument(calculSommeReglementsComplet(doc).add(doc.getAcomptes().add(calculSommeAvoir(doc))));
		doc.setResteAReglerComplet(doc.getNetTtcCalc().subtract(doc.getRegleCompletDocument()));
		return doc.getRegleCompletDocument();
	}
	
	public TaTicketDeCaisse calculRegleDocument(TaTicketDeCaisse doc){
		doc.setRegleDocument(calculSommeReglementsIntegresEcran(doc).add(doc.getAcomptes().add(doc.getAvoirs())));
		doc.setResteARegler(doc.getNetTtcCalc().subtract(doc.getRegleDocument()));
		return doc;
	}
	
	public BigDecimal calculResteAReglerComplet(TaTicketDeCaisse doc){
//		setRegleCompletDocument(calculSommeReglementsComplet());
		doc.setResteAReglerComplet(doc.getNetTtcCalc().subtract(calculSommeReglementsComplet(doc).add(doc.getAcomptes().add(calculSommeAvoir(doc)))));
		return doc.getResteAReglerComplet();
	}
	
	public boolean multiReglement(TaTicketDeCaisse doc){
		int nb=0;
		for (TaRReglement taRReglement : doc.getTaRReglements()) {
			if (((taRReglement.getEtatDeSuppression()&IHMEtat.suppression)==0)
					&& (taRReglement.getEtat()&IHMEtat.integre)!=0)
			{
				nb++;
			}
		}		
		return nb>1;
	}
	
//	@Override
	public void calculDateEcheanceAbstract(TaTicketDeCaisse doc, Integer report, Integer finDeMois) {
		// TODO Auto-generated method stub
		calculDateEcheanceAbstract(doc,report,finDeMois,null);
	}
	
	public Date calculDateEcheanceAbstract(TaTicketDeCaisse doc, Integer report, Integer finDeMois,TaTPaiement taTPaiement) {

		return calculDateEcheance(doc,report,finDeMois,taTPaiement);
	}
	
	public Date calculDateEcheance(TaTicketDeCaisse doc, Integer report, Integer finDeMois,TaTPaiement taTPaiement) {
		TaTCPaiement typeCP = taTCPaiementDAO.findByCode(TaTCPaiement.C_CODE_TYPE_TICKET_DE_CAISSE);
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
	
	public TaRReglement creeRReglement(TaTicketDeCaisse doc,TaRReglement taRReglement,Boolean integrer,String typePaiementDefaut,TaReglement reglement) throws Exception{
		if(taRReglement!=null){
			taRReglement.setTaTicketDeCaisse(doc);
			taRReglement.getTaReglement().setDateDocument(doc.getDateDocument());
			taRReglement.getTaReglement().setTaTiers(doc.getTaTiers());
			taRReglement.getTaReglement().setDateLivDocument(doc.getDateEchDocument());
			taRReglement.getTaReglement().setTaCompteBanque(taCompteBanqueDAO.findByTiersEntreprise(taRReglement.getTaReglement().getTaTPaiement()));
//			taReglement.setEtat(IHMEtat.integre);
		}else{
			Boolean devientMultiReglement=reglementExiste(doc) &&
				doc.getTaRReglement().getTaReglement().getTaRReglements().size()>0 && integrer;
			Integer report=0;
			Integer finDeMois=0;
			TaTPaiement typePaiement=null;
			TaRReglement taRReglementTmp = new TaRReglement();
			TaReglement taReglement = new TaReglement();
			
			if(reglement!=null)taReglement=reglement;
			
			taRReglementTmp.setTaTicketDeCaisse(doc);
			taRReglementTmp.setTaReglement(taReglement);		
			taRReglementTmp.getTaReglement().setDateDocument(doc.getDateDocument());
			taRReglementTmp.getTaReglement().setDateLivDocument(doc.getDateEchDocument());
			if(!devientMultiReglement && doc.getTaRReglement()!=null && doc.getTaRReglement().getTaReglement()!=null && 
					doc.getTaRReglement().getTaReglement().getTaTPaiement()!=null){
				typePaiement=doc.getTaRReglement().getTaReglement().getTaTPaiement();
				report=doc.getTaRReglement().getTaReglement().getTaTPaiement().getReportTPaiement();
				finDeMois=doc.getTaRReglement().getTaReglement().getTaTPaiement().getFinMoisTPaiement();
			}				
			else
			if(doc.getTaTiers()!=null && doc.getTaTiers().getTaTPaiement()!=null ){
				typePaiement=doc.getTaTiers().getTaTPaiement();
					report=doc.getTaTiers().getTaTPaiement().getReportTPaiement();
					finDeMois=doc.getTaTiers().getTaTPaiement().getFinMoisTPaiement();
			}
			
			else {
				if (typePaiementDefaut == null || typePaiementDefaut=="")
					typePaiementDefaut="C";
				try {
					typePaiement = taTPaiementDAO
							.findByCode(typePaiementDefaut);

				} catch (Exception e) {
				}
				//typePaiement=new TaTPaiementDAO(Em).findByCode("C");
			}
			if(taRReglementTmp.getTaReglement().getNetTtcCalc().signum()>0){	
			taRReglementTmp.getTaReglement().setTaTPaiement(typePaiement);
			if(taRReglementTmp.getTaReglement().getLibelleDocument()==null || 
					taRReglementTmp.getTaReglement().getLibelleDocument().equals(""))
			taRReglementTmp.getTaReglement().setLibelleDocument(typePaiement.getLibTPaiement());
			}
			//taRReglementTmp.getTaReglement().setDateLivDocument(this.getDateEchDocument());
			//taRReglementTmp.getTaReglement().setDateLivDocument(calculDateEcheance(report,finDeMois,typePaiement));
			
			
			taRReglementTmp.getTaReglement().setTaCompteBanque(taCompteBanqueDAO.findByTiersEntreprise(typePaiement));
			taRReglementTmp.getTaReglement().setTaTiers(doc.getTaTiers());
			taRReglementTmp.setEtatDeSuppression(IHMEtat.insertion);
			taRReglement=taRReglementTmp;
		}
		taRReglement.getTaReglement().addRReglement(taRReglement);
		if((taRReglement.getEtat()&IHMEtat.multiple)==0 && integrer)	
			taRReglement.setEtat(IHMEtat.integre);
		else 
			taRReglement.setEtat(0);
		
			List<String> listeCodes = new LinkedList<String>();
			for (TaRReglement reglement_ : doc.getTaRReglements()) {
				listeCodes.add(reglement_.getTaReglement().getCodeDocument());
			}
			if(taRReglement.getTaReglement().getCodeDocument()==null ||taRReglement.getTaReglement().getCodeDocument().equals("")) {
				  taRReglement.getTaReglement().setCodeDocument(taReglementService.genereCode(null));
			}
		return taRReglement;

	}
	public TaRReglement creeRReglement(TaTicketDeCaisse doc,TaRReglement taRReglement,Boolean integrer,String typePaiementDefaut) throws Exception{
		return creeRReglement(doc, taRReglement, integrer, typePaiementDefaut,null);
	}
	
	
//	public void addRAcompte(TaTicketDeCaisse doc, TaRAcompte taRAcompte){
//		if(!doc.getTaRAcomptes().contains(taRAcompte))
//			doc.getTaRAcomptes().add(taRAcompte);	
//		calculSommeAcomptes(doc);
//	}
	public void removeRAvoir(TaTicketDeCaisse doc, TaRAvoir taRAvoir){
		doc.getTaRAvoirs().remove(taRAvoir);
		calculSommeAvoir(doc);
	}

	public void addRAvoir(TaTicketDeCaisse doc, TaRAvoir taRAvoir){
		if(!doc.getTaRAvoirs().contains(taRAvoir))
			doc.getTaRAvoirs().add(taRAvoir);	
		doc.setAvoirs(calculSommeAvoir(doc));
		
	}
//	public void removeRAcompte(TaTicketDeCaisse doc, TaRAcompte taRAcompte){
//		doc.getTaRAcomptes().remove(taRAcompte);
//		calculSommeAcomptes(doc);
//	}
	
	public TaTicketDeCaisse addRReglement(TaTicketDeCaisse doc, TaRReglement taReglement){
		if(!doc.getTaRReglements().contains(taReglement)){
			taReglement.setTaTicketDeCaisse(doc);
			doc.getTaRReglements().add(taReglement);	
		}
		return doc;
	}
	public TaTicketDeCaisse removeReglement(TaTicketDeCaisse doc, TaRReglement taRReglement){
		doc.getTaRReglements().remove(taRReglement);
//		calculSommeAcomptes();
		return doc;
	}

	public TaTicketDeCaisse removeTousRReglements(TaTicketDeCaisse doc) throws Exception{
		List<TaRReglement> listeTemp=new LinkedList<TaRReglement>();
		for (TaRReglement element : doc.getTaRReglements()) {
			listeTemp.add(element);
//			removeReglement(element);
			element.getTaReglement().removeReglement(element);
			if(element.getTaReglement().getTaRReglements().size()==0) {
				
				//passage ejb
				//taReglementDAO.supprimer(element.getTaReglement());
				taReglementService.remove(element.getTaReglement());
			}
		} 

		for (TaRReglement element : listeTemp) {
			removeReglement(doc,element);
		}
//		calculSommeAcomptes();
		return doc;
	}
	
//
//	public void removeTousRAcomptes(TaTicketDeCaisse doc) throws Exception{
//		List<TaRAcompte> listeTemp=new LinkedList<TaRAcompte>();
//		for (TaRAcompte element : doc.getTaRAcomptes()) {
//			listeTemp.add(element);
//			element.getTaAcompte().removeRAcompte(element);
//		} 
//
//		for (TaRAcompte element : listeTemp) {
//			removeRAcompte(doc,element);
//		}
//	}
	
	public void removeTousRAvoirs(TaTicketDeCaisse doc) throws Exception{
		List<TaRAvoir> listeTemp=new LinkedList<TaRAvoir>();
		for (TaRAvoir element : doc.getTaRAvoirs()) {
			listeTemp.add(element);
			element.getTaAvoir().removeRAvoir(element);
		} 

		for (TaRAvoir element : listeTemp) {
			removeRAvoir(doc,element);
		}
	}
	
	public void gestionReglement(TaTicketDeCaisse doc, TaRReglement taReglement){
		if(taReglement.getAffectation()==null
				//||taReglement.getAffectation().compareTo(BigDecimal.valueOf(0))==0
				||(taReglement.getEtat()&IHMEtat.integre)==0){
			removeReglement(doc,taReglement);
		}else
			if((taReglement.getEtat()&IHMEtat.integre)!=0)
				addRReglement(doc,taReglement);
	}
	
	public TaTicketDeCaisse affecteReglementFacture(TaTicketDeCaisse doc, String typePaiementDefaut) throws Exception{
		TaRReglement rR =creeRReglement(doc,null,true,typePaiementDefaut);
		doc.setTaRReglement(rR);
		doc.getTaRReglement().setEtatDeSuppression(IHMEtat.insertion);
//		getTaRReglement().setEtat(IHMEtat.integre);
		if(!multiReglement(doc)){
			for (TaRReglement taReglement : doc.getTaRReglements()) {
				if (((taReglement.getEtatDeSuppression()&IHMEtat.suppression)==0)
						&& (taReglement.getEtat()&IHMEtat.integre)!=0)
					doc.setTaRReglement(taReglement);				
			}			
		}else{
			doc.setTaRReglement(creeRReglement(doc,null,false,typePaiementDefaut));
			doc.getTaRReglement().getTaReglement().setTaTPaiement(null);
			doc.getTaRReglement().setAffectation(calculSommeReglementsIntegresEcran(doc));
			doc.getTaRReglement().getTaReglement().setLibelleDocument("Réglements multiples");
			doc.getTaRReglement().setEtatDeSuppression(IHMEtat.multiple);
			doc.getTaRReglement().setEtat(0);
		}
//		mettreAJourDateEcheanceReglement(taRReglement.getTaReglement());
		doc.getTaRReglement().getTaReglement().setTaTiers(doc.getTaTiers());
		doc.getTaRReglement().setTaTicketDeCaisse(doc);
		return doc;
	}
	
	public void modifieLibellePaiementMultiple(TaTicketDeCaisse doc){
		String libelleValide="";
		for (TaRReglement taRReglement : doc.getTaRReglements()) {
			if (((taRReglement.getEtatDeSuppression()&IHMEtat.suppression)==0)&& (taRReglement.getEtat()&IHMEtat.integre)!=0)
			{
				if(!multiReglement(doc) && (taRReglement.getTaReglement().getLibelleDocument()!=null&&
						taRReglement.getTaReglement().getLibelleDocument().equals("Multiples réglements")))
					libelleValide="";
				else if(!multiReglement(doc))libelleValide=taRReglement.getTaReglement().getLibelleDocument();
				else libelleValide="Multiples réglements";
			}
		}
		if(doc.getTaRReglement()!=null && doc.getTaRReglement().getTaReglement()!=null) 
			doc.getTaRReglement().getTaReglement().setLibelleDocument(libelleValide);
	}
	
	public void modifieTypePaiementMultiple(TaTicketDeCaisse doc){
		TaTPaiement taTPaiement=null;
		for (TaRReglement taRReglement : doc.getTaRReglements()) {
			if (((taRReglement.getEtatDeSuppression()&IHMEtat.suppression)==0)
					&& (taRReglement.getEtat()&IHMEtat.integre)!=0)
			{
				if(!multiReglement(doc)) taTPaiement=taRReglement.getTaReglement().getTaTPaiement();
			}
		}
		if(doc.getTaRReglement()!=null && doc.getTaRReglement().getTaReglement()!=null) 
			doc.getTaRReglement().getTaReglement().setTaTPaiement(taTPaiement);
	}
	
	/**
	 * Calcul des totaux de la facture
	 */
	public void calculTotaux(TaTicketDeCaisse doc) {
		
//			    MT_TVA Numeric(15,2),
			doc.setMtHtCalc(new BigDecimal(0));
			doc.setNetHtCalc(new BigDecimal(0));
			doc.setMtTtcCalc(new BigDecimal(0));
			doc.setMtTtcAvantRemiseGlobaleCalc(new BigDecimal(0));
			for (Object ligne : doc.getLignes()) {
				if(((TaLTicketDeCaisse)ligne).getTaTLigne().getCodeTLigne().equals(SWTDocument.C_CODE_T_LIGNE_H)) {
					if(((TaLTicketDeCaisse)ligne).getMtHtLApresRemiseGlobaleDocument()!=null) {
						doc.setNetHtCalc(doc.getNetHtCalc().add(((TaLTicketDeCaisse)ligne).getMtHtLApresRemiseGlobaleDocument()));
					}if(((TaLTicketDeCaisse)ligne).getMtTtcLApresRemiseGlobaleDocument()!=null)
						doc.setMtTtcCalc(doc.getMtTtcCalc().add(((TaLTicketDeCaisse)ligne).getMtTtcLApresRemiseGlobaleDocument()));
					if(((TaLTicketDeCaisse)ligne).getMtHtLDocument()!=null)
						doc.setMtHtCalc(doc.getMtHtCalc().add(((TaLTicketDeCaisse)ligne).getMtHtLDocument()));
					if(((TaLTicketDeCaisse)ligne).getMtTtcLDocument()!=null)
						doc.setMtTtcAvantRemiseGlobaleCalc(doc.getMtTtcAvantRemiseGlobaleCalc().add(((TaLTicketDeCaisse)ligne).getMtTtcLDocument()));
				}
				
			}
//			setRemHtDocument(getMtHtCalc().subtract(getNetHtCalc()));
			doc.setNetTvaCalc(doc.getMtTtcCalc().subtract(doc.getNetHtCalc()));
			BigDecimal tva = new BigDecimal(0);
			for (LigneTva ligneTva : doc.getLignesTVA()) {
				tva = tva.add(ligneTva.getMtTva());
			}
			if(tva.compareTo(doc.getNetTvaCalc())!=0) {
				logger.error("Montant de la TVA incorrect : "+doc.getNetTvaCalc()+" ** "+tva);
			}
			BigDecimal tvaAvantRemise = new BigDecimal(0);
			for (LigneTva ligneTva : doc.getLignesTVA()) {
				tvaAvantRemise = tvaAvantRemise.add(ligneTva.getMtTvaAvantRemise());
			}
			doc.setMtTvaCalc(tvaAvantRemise);
			//setNetTtcCalc(getMtTtcCalc().subtract(getMtTtcCalc().multiply(getRemTtcFacture().divide(new BigDecimal(100)))));
			doc.setNetTtcCalc(doc.getMtTtcCalc().subtract(doc.getMtTtcCalc().multiply(doc.getTxRemTtcDocument().divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_HALF_UP)));
//			setNetTtcCalc(getMtTtcAvantRemiseGlobaleCalc().subtract(getMtTtcAvantRemiseGlobaleCalc().multiply(getTxRemTtcDocument().divide(new BigDecimal(100)))));
			
			/*
			 * remise HT déjà calculée dans dispatcherTva()
			 */
			doc.setRemTtcDocument(doc.getMtTtcCalc().subtract(doc.getNetTtcCalc()).setScale(2,BigDecimal.ROUND_HALF_UP));
			calculSommeAcomptes(doc);
			//calculSommeAvoirIntegres();
			//modifié suite à changement écran le 23/04/2010 par isa
			//setNetAPayer(getNetTtcCalc().subtract(getRegleDocument()));
			doc.setResteARegler(doc.getNetTtcCalc().subtract(doc.getRegleDocument()).subtract(doc.getAcomptes()).subtract(calculSommeAvoirIntegres(doc)));
			
	}
	
	public void calculeTvaEtTotaux(TaTicketDeCaisse doc){
		calculTvaTotal(doc);
		calculTotaux(doc);
	}
	


	public LinkedList<TaRReglement> rechercheSiDocumentContientTraite(TaTicketDeCaisse doc, String typeTraite){
		LinkedList<TaRReglement> listeTraite =new LinkedList<TaRReglement>();
		for (TaRReglement rReglement : doc.getTaRReglements()) {
			if(rReglement.getTaReglement().getTaTPaiement()!=null){
				if(rReglement.getTaReglement().getTaTPaiement().getCodeTPaiement().equals(typeTraite)
						&& (rReglement.getEtat()&IHMEtat.integre)!=0 )
					listeTraite.add(rReglement);				
			}
		}
		return listeTraite;
	}

	public boolean reglementExiste(TaTicketDeCaisse doc){
		return doc.getTaRReglement()!=null && doc.getTaRReglement().getTaReglement()!=null ;
	}

	public boolean reglementRempli(TaTicketDeCaisse doc){
		for (TaRReglement elem : doc.getTaRReglements()) {
			if(elem.getAffectation().signum()!=0)
				return true;
		}
		return false;
	}
	
	public void mettreAJourDateEcheanceReglements(TaTicketDeCaisse doc){
		if(!multiReglement(doc)){
		for (TaRReglement rReglement : doc.getTaRReglements()) {
			if(rReglement.getTaReglement()!=null){
				if(  !rReglement.getTaReglement().affectationMultiple(doc))
					rReglement.getTaReglement().setDateLivDocument(doc.getDateEchDocument());
				}
			}
		}
	}
	

	public boolean contientReglementAffectationMultiple(TaTicketDeCaisse doc){
		for (TaRReglement rReglement : doc.getTaRReglements()) {
			if(rReglement.getTaReglement()!=null && !rReglement.getTaReglement().affectationMultiple(doc)){
				if(  rReglement.getTaReglement().affectationMultiple(doc))
					return true;
				}
			}
		return false;
	}
	
	public void mettreAJourDateEcheanceReglement(TaTicketDeCaisse doc, TaReglement reglement){
		if(!multiReglement(doc) && !reglement.affectationMultiple(doc))
			reglement.setDateLivDocument(doc.getDateEchDocument());
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTicketDeCaisse transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTicketDeCaisse transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}


	@Override
	public void remove(TaTicketDeCaisse persistentInstance, boolean recharger) throws RemoveException {
		// TODO Auto-generated method stub
		try {
			if(taAutorisationDossierService.autoriseMenu(IModulesProgramme.ID_MODULE_ABONNEMENT, sessionInfo.getUtilisateur())) {
				//taLigneALigneEcheanceService.removeAllByIdDocumentAndTypeDoc(persistentInstance.getIdDocument(), *.TYPE_DOC);
			}
			if(recharger)persistentInstance=findByIDFetch(persistentInstance.getIdDocument());
			List<ILigneDocumentTiers> listeLien = recupListeLienLigneALigne(persistentInstance);
			for (TaRReglement ligne : persistentInstance.getTaRReglements()) {
				ligne.getTaReglement().removeReglement(ligne);
				daoReglement.merge(ligne.getTaReglement());
			}
			for (TaRAvoir ligne : persistentInstance.getTaRAvoirs()) {
				ligne.getTaAvoir().removeRAvoir(ligne);
				daoAvoir.merge(ligne.getTaAvoir());
			}
			persistentInstance.getTaRReglements().clear();
			persistentInstance.getTaRAvoirs().clear();
			persistentInstance=findById(persistentInstance.getIdDocument());
			dao.remove(persistentInstance);
			mergeEntityLieParLigneALigne(listeLien);
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public void remove(TaTicketDeCaisse persistentInstance) throws RemoveException {
		remove(persistentInstance, true);
	}
	
	public TaTicketDeCaisse merge(TaTicketDeCaisse detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTicketDeCaisse merge(TaTicketDeCaisse detachedInstance, String validationContext) {
		TaMiseADisposition miseADispo = detachedInstance.getTaMiseADisposition(); 
		calculTotaux(detachedInstance);
		
		validateEntity(detachedInstance, validationContext);
		List<ILigneDocumentTiers> listeLien =null;
		TaTicketDeCaisse objInitial = detachedInstance;
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
		
		//je détache les réglements avant d'enregistrer la facture
		List<TaRReglement> listeReglementTmp=new LinkedList<>();
		for (TaRReglement ligne : detachedInstance.getTaRReglements()) {
			listeReglementTmp.add(ligne);
		}
		detachedInstance.getTaRReglements().clear();		
		for (TaRReglement taRReglement : listeReglementTmp) {
			taRReglement.setTaTicketDeCaisse(null);
		}
		//j'enregistre
		detachedInstance=dao.merge(detachedInstance);
		
		//je rattache les réglements à la facture
		for (TaRReglement taRReglement : listeReglementTmp) {
			taRReglement.setTaTicketDeCaisse(detachedInstance);
			detachedInstance.addRReglement(taRReglement);
		}
		//j'enregistre les réglements
		TaReglement reglement=null;
		for (TaRReglement ligne : detachedInstance.getTaRReglements()) {
			reglement=ligne.getTaReglement();
			if(reglement.getIdDocument()==0){
				reglement.getTaRReglements().remove(ligne);
				reglement=daoReglement.merge(ligne.getTaReglement());
				reglement.addRReglement(ligne);
				ligne.setTaReglement(reglement);
			}
		}
		for (TaRReglement ligne : detachedInstance.getTaRReglements()) {
			if(ligne.getEtat()==IHMEtat.integre) {
				ligne.setTaMiseADisposition(miseADispo);
				ligne.getTaReglement().setTaMiseADisposition(miseADispo);
			}
			if((ligne.getEtatDeSuppression()==IHMEtat.suppression)){
				ligne.getTaReglement().removeReglement(ligne);
			}
			
			reglement=daoReglement.merge(ligne.getTaReglement());
			ligne.setTaTicketDeCaisse(detachedInstance);
			ligne.setTaReglement(reglement);
		}
		for (TaRAvoir ligne : detachedInstance.getTaRAvoirs()) {
			if(ligne.getEtat()==IHMEtat.integre) {
				ligne.setTaMiseADisposition(miseADispo);
				ligne.getTaAvoir().setTaMiseADisposition(miseADispo);
			}
			if((ligne.getEtatDeSuppression()==IHMEtat.suppression)){
				ligne.getTaAvoir().removeRAvoir(ligne);
			}
			daoAvoir.merge(ligne.getTaAvoir());
		}
		
		
		//je ré-enregistre la facture avec les réglements
		detachedInstance=dao.merge(detachedInstance);
		
		mergeEntityLieParLigneALigne(listeLien);
		
		annuleCode(detachedInstance.getCodeDocument());
		return detachedInstance;
	}

	public TaTicketDeCaisse findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTicketDeCaisse findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTicketDeCaisse> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTicketDeCaisseDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTicketDeCaisseDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTicketDeCaisse> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTicketDeCaisseDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTicketDeCaisseDTO entityToDTO(TaTicketDeCaisse entity) {
//		TaTicketDeCaisseDTO dto = new TaTicketDeCaisseDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaTicketDeCaisseMapper mapper = new TaTicketDeCaisseMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTicketDeCaisseDTO> listEntityToListDTO(List<TaTicketDeCaisse> entity) {
		List<TaTicketDeCaisseDTO> l = new ArrayList<TaTicketDeCaisseDTO>();

		for (TaTicketDeCaisse taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTicketDeCaisseDTO> selectAllDTO() {
		System.out.println("List of TaTicketDeCaisseDTO EJB :");
		List<TaTicketDeCaisseDTO> liste = new ArrayList<TaTicketDeCaisseDTO>();
		
		liste = findAllLight();

//		List<TaTicketDeCaisse> projects = selectAll();
//		for(TaTicketDeCaisse project : projects) {
//			liste.add(entityToDTO(project));
//		}

		return liste;
	}

	public TaTicketDeCaisseDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTicketDeCaisseDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTicketDeCaisseDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTicketDeCaisseDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTicketDeCaisseDTO dto, String validationContext) throws EJBException {
		try {
			TaTicketDeCaisseMapper mapper = new TaTicketDeCaisseMapper();
			TaTicketDeCaisse entity = null;
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
	
	public void persistDTO(TaTicketDeCaisseDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTicketDeCaisseDTO dto, String validationContext) throws CreateException {
		try {
			TaTicketDeCaisseMapper mapper = new TaTicketDeCaisseMapper();
			TaTicketDeCaisse entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTicketDeCaisseDTO dto) throws RemoveException {
		try {
			TaTicketDeCaisseMapper mapper = new TaTicketDeCaisseMapper();
			TaTicketDeCaisse entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTicketDeCaisse refresh(TaTicketDeCaisse persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTicketDeCaisse value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTicketDeCaisse value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTicketDeCaisseDTO dto, String validationContext) {
		try {
			TaTicketDeCaisseMapper mapper = new TaTicketDeCaisseMapper();
			TaTicketDeCaisse entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTicketDeCaisseDTO> validator = new BeanValidator<TaTicketDeCaisseDTO>(TaTicketDeCaisseDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTicketDeCaisseDTO dto, String propertyName, String validationContext) {
		try {
			TaTicketDeCaisseMapper mapper = new TaTicketDeCaisseMapper();
			TaTicketDeCaisse entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTicketDeCaisseDTO> validator = new BeanValidator<TaTicketDeCaisseDTO>(TaTicketDeCaisseDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTicketDeCaisseDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTicketDeCaisseDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTicketDeCaisse value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTicketDeCaisse value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

	public void removeTousLesAbonnements(TaTicketDeCaisse persistentInstance) throws Exception {
//		try {    //****** pour compile maven à remettre isa*****///
//			if(Platform.getBundle(TaAbonnement.TYPE_DOC)!=null){
//			List<TaAbonnement> listeAbonnement=null;
//			listeAbonnement=daoAbonnement.selectAbonnementFacture(persistentInstance);
//			if(listeAbonnement!=null && listeAbonnement.size()>0){
//				if(MessageDialog.openQuestion(PlatformUI.getWorkbench().getDisplay().getActiveShell(), "Facture liée à abonnement", 
//						"Cette facture est liée à "+listeAbonnement.size()+" abonnement(s). Ils vont être supprimés. Voulez-vous continuer ?")){
//					for (TaAbonnement taAbonnement : listeAbonnement) {
//						daoAbonnement.remove(taAbonnement);
//					}
//				}else throw new Exception();
//			}
//			}
//		} catch (RuntimeException re) {
//			logger.error("removeTousLesAbonnements failed", re);
//			throw re;
//		}
	}
	public void removeTousLesSupportAbons(TaTicketDeCaisse persistentInstance) throws Exception {
	    //****** pour compile maven à remettre isa*****///
//		// ***TODO ISA***  a remettre après avoir trouver comment contourner le problème des liaison avec les points bonus pour autres utilisateur de BDG   ******
//		
////		try {
//		if(Platform.getBundle(TaSupportAbon.TYPE_DOC)!=null){
//			List<TaSupportAbon> listeSupport=null;
//			List<TaAbonnement> listeAbonnement=null;
//			listeSupport=daoSupport.selectTaSupportAbonFacture(persistentInstance);
//			
//			if(listeSupport!=null && listeSupport.size()>0){
//				if(MessageDialog.openQuestion(PlatformUI.getWorkbench().getDisplay().getActiveShell(), "Facture liée à support", 
//						"Cette facture est liée à "+listeSupport.size()+" support d'abonnement(s). Ils vont être supprimés. Voulez-vous continuer ?")){
//					for (TaSupportAbon taSupport : listeSupport) {
//						//regarder si support lié à abonnement
//						listeAbonnement=daoAbonnement.selectAbonnementSupport(taSupport);
//						for (TaAbonnement taAbonnement : listeAbonnement) {
//							daoAbonnement.remove(taAbonnement);
//						}
//						daoSupport.remove(taSupport);
//					}
//				}else throw new Exception();
//			}
//		}
////		} catch (RuntimeException re) {
////			logger.error("removeTousLesSupportAbons failed", re);
////			throw re;
////		}
	}

	@Override
	public List<TaTicketDeCaisse> rechercheDocument(Date dateDeb, Date dateFin) {
		return dao.rechercheDocument(dateDeb, dateFin);
	}

	@Override
	public List<Object[]> rechercheDocument(Date dateDeb, Date dateFin,
			Boolean light) {
		return dao.rechercheDocument(dateDeb, dateFin, light);
	}

	@Override
	public List<TaTicketDeCaisse> rechercheDocument(String codeDeb, String codeFin) {
		return dao.rechercheDocument(codeDeb, codeFin);
	}

	@Override
	public List<TaTicketDeCaisse> rechercheDocument(Date dateDeb, Date dateFin,
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
	public List<TaTicketDeCaisse> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers) {
		return dao.rechercheDocument(codeDeb, codeFin, codeTiers);
	}

	@Override
	public List<TaTicketDeCaisse> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers, Boolean export) {
		// TODO Auto-generated method stub
		return dao.rechercheDocument(dateDeb, dateFin, codeTiers, export);
	}

	@Override
	public List<TaTicketDeCaisse> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers, Boolean export) {
		// TODO Auto-generated method stub
		return dao.rechercheDocument(codeDeb, codeFin, codeTiers, export);
	}

	@Override
	public List<TaTicketDeCaisse> selectAll(IDocumentTiers taDocument, Date dateDeb,
			Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaTicketDeCaisse> findByCodeTiersAndDate(String codeTiers, Date debut,
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

	@Override
	public List<Object> findChiffreAffaireByCodeArticle(String codeArticle,
			Date debut, Date fin, int precision) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaTicketDeCaisse findByIdDocument(int id) throws FinderException {
		return findById(id);
	}

	@Override
	public TaTicketDeCaisse mergeAndFindById(TaTicketDeCaisse detachedInstance, String validationContext) {
		TaTicketDeCaisse br = merge(detachedInstance,validationContext);
		try {
			br = findByIDFetch(br.getIdDocument()); //pour etre sur que les valeur initialisé par les triggers "qui_cree,..." soit bien affecté avant une éventuelle nouvelle modification du document
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return br;
	}

	public List<TaTicketDeCaisseDTO> findAllLight() {
		return dao.findAllLight();
	}

	@Override
	public List<TaTicketDeCaisse> rechercheDocumentNonExporte(Date dateDeb, Date dateFin, Boolean parDate) {
		// TODO Auto-generated method stub
		return dao.rechercheDocumentNonExporte(dateDeb, dateFin, parDate);
	}

	@Override
	public List<TaTicketDeCaisse> rechercheDocumentOrderByDate(Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return dao.rechercheDocumentOrderByDate(dateDeb, dateFin);
	}

	@Override
	public List<TaTicketDeCaisseDTO> rechercheDocumentNonExporteLight(Date dateDeb, Date dateFin, boolean parDate) {
		// TODO Auto-generated method stub
		return dao.rechercheDocumentNonExporteLight(dateDeb, dateFin, parDate);
	}

	@Override
	public List<Object> findChiffreAffaireTotal(Date debut, Date fin, int precision) {
		// TODO Auto-generated method stub
		return dao.findChiffreAffaireTotal(debut, fin, precision);
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

//	@Override
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireJmaDTO(Date debut, Date fin, int precision) {
//		// TODO Auto-generated method stub
//		return dao.findChiffreAffaireJmaDTO(debut, fin, precision);
//	}
	
	
	public List<TaTicketDeCaisseDTO> findAllDTOPeriode(Date dateDebut, Date dateFin) {
		// TODO Auto-generated method stub
		return dao.findAllDTOPeriode(dateDebut, dateFin);
	}
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

//	@Override
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotalDTO(Date debut, Date fin) {
//		// TODO Auto-generated method stub
//		return dao.findChiffreAffaireTotalDTO(debut, fin);
//	}
	
//	@Override
//	public List<DocumentChiffreAffaireDTO> findChiffreAffairePayeTotalDTO(Date dateDebut, Date dateFin){
//		return dao.findChiffreAffairePayeTotalDTO(dateDebut, dateFin);
//	}
	// fin requête dashboard

	@Override
	public List<TaTicketDeCaisse> rechercheDocumentNonTotalementRegle(Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
//		return dao.rechercheDocumentNonTotalementRegle(dateDeb, dateFin);
		return dao.rechercheDocumentNonTotalementRegleReel(dateDeb, dateFin);
	}

	@Override
	public List<TaTicketDeCaisseDTO> rechercheDocumentNonTotalementRegle(Date dateDeb, Date dateFin, String tiers,
			String document) {
		// TODO Auto-generated method stub
		return dao.rechercheDocumentNonTotalementRegle(dateDeb, dateFin, tiers, document);
	}

	@Override
	public List<TaTicketDeCaisse> rechercheDocumentNonTotalementRegleAEcheance(Date dateDeb, Date dateFin, String tiers,
			String document, BigDecimal limite) {
		// TODO Auto-generated method stub
		return dao.rechercheDocumentNonTotalementRegleAEcheance(dateDeb, dateFin, tiers, document, limite);
	}

	@Override
	public List<Object[]> rechercheDocumentNonTotalementRegleAEcheance2(Date dateDeb, Date dateFin, String tiers,
			String document) {
		// TODO Auto-generated method stub
		return dao.rechercheDocumentNonTotalementRegleAEcheance2(dateDeb, dateFin, tiers, document);
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
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalSansAvoirDTO(Date dateDebut, Date dateFin,
			String codeTiers) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireTotalSansAvoirDTO(dateDebut, dateFin, codeTiers);
	}

	@Override
	public List<TaTicketDeCaisse> rechercheDocumentVerrouille(Date dateDeb, Date dateFin, String codeTiers,
			Boolean verrouille) {
		// TODO Auto-generated method stub
		return dao.rechercheDocumentVerrouille(dateDeb, dateFin, codeTiers, verrouille);
	}

	@Override
	public List<TaTicketDeCaisse> rechercheDocumentVerrouille(String codeDeb, String codeFin, String codeTiers,
			Boolean verrouille) {
		// TODO Auto-generated method stub
		return dao.rechercheDocumentVerrouille(codeDeb, codeFin, codeTiers, verrouille);
	}

	@Override
	public List<TaTicketDeCaisse> rechercheDocument(String codeTiers, Date dateExport, Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return dao.rechercheDocument(dateExport, codeTiers, dateDeb, dateFin);
	}

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

//	@Override
//	public List<DocumentDTO> findAllDTOPeriodeParTypeRegroupement(Date dateDebut, Date dateFin, String codeTiers,
//			String typeRegroupement, Object valeurRegroupement) {
//		// TODO Auto-generated method stub
//		return dao.findAllDTOPeriodeParTypeRegroupement(dateDebut, dateFin, codeTiers, typeRegroupement, valeurRegroupement);
//	}
//
//	@Override
//	public List<DocumentDTO> findDocumentNonTransfosARelancerDTOParTypeRegroupement(Date dateDebut, Date dateFin,
//			int deltaNbJours, String codeTiers, String typeRegroupement, Object valeurRegroupement) {
//		// TODO Auto-generated method stub
//		return dao.findDocumentNonTransfosARelancerDTOParTypeRegroupement(dateDebut, dateFin, deltaNbJours, codeTiers, typeRegroupement, valeurRegroupement);
//	}
//
//	@Override
//	public List<DocumentDTO> findDocumentNonTransfosDTOParTypeRegroupement(Date dateDebut, Date dateFin,
//			String codeTiers, String typeRegroupement, Object valeurRegroupement) {
//		// TODO Auto-generated method stub
//		return dao.findDocumentNonTransfosDTOParTypeRegroupement(dateDebut, dateFin, codeTiers, typeRegroupement, valeurRegroupement);
//	}
//
//	@Override
//	public List<DocumentDTO> findDocumentTransfosDTOParTypeRegroupement(Date dateDebut, Date dateFin, String codeTiers,
//			String typeRegroupement, Object valeurRegroupement) {
//		// TODO Auto-generated method stub
//		return dao.findDocumentTransfosDTOParTypeRegroupement(dateDebut, dateFin, codeTiers, typeRegroupement, valeurRegroupement);
//	}
//
//
//
//
//
//
//	public List<DocumentChiffreAffaireDTO> countDocumentParTypeRegroupement(Date debut, Date fin,
//			String codeTiers, String typeRegroupement, Object valeurRegroupement) {
//		// TODO Auto-generated method stub
//		return dao.countDocumentParTypeRegroupement(debut, fin, codeTiers, typeRegroupement, valeurRegroupement);
//	}



//	@Override
//	public List<DocumentChiffreAffaireDTO> chiffreAffaireParTypeRegroupement(Date debut, Date fin, String codeTiers,
//			String typeRegroupement, Object valeurRegroupement) {
//		// TODO Auto-generated method stub
//		return dao.chiffreAffaireParTypeRegroupement(debut, fin, codeTiers, typeRegroupement, valeurRegroupement);
//	}
	
	
	
	
/////// regroupement
	

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
	public TaTicketDeCaisse findDocByLDoc(ILigneDocumentTiers lDoc) {
		TaTicketDeCaisse o= (TaTicketDeCaisse) dao.findDocByLDoc(lDoc);
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
	public TaTicketDeCaisse mergeEtat(IDocumentTiers detachedInstance) {
//		à mettre si gestion des états !!!!
		modifEtatLigne(detachedInstance);		
		TaEtat etat=changeEtatDocument(detachedInstance);
		((TaTicketDeCaisse) detachedInstance).addREtat(etat);
		
		detachedInstance=dao.merge((TaTicketDeCaisse) detachedInstance);	
		return (TaTicketDeCaisse) detachedInstance;
	}

	@Override
	public int findDocByLDocDTO(ILigneDocumentTiers lDoc) {
		return dao.findDocByLDocDTO(lDoc);
	}

	@Override
	public TaTicketDeCaisse findByCodeFetch(String code) throws FinderException {
		// TODO Auto-generated method stub
		TaTicketDeCaisse o =(TaTicketDeCaisse) dao.findByCodeFetch(code);
		if(o!=null) {
		recupSetREtat(o);
		recupSetHistREtat(o);
		recupSetLigneALigne(o);
		recupSetRDocument(o);
		recupSetREtatLigneDocuments(o);
		recupSetHistREtatLigneDocuments(o);
		recupSetRReglementLiaison(o);
		}
		return o;
	}

	@Override
	public TaTicketDeCaisse findByIDFetch(int id) throws FinderException {
		// TODO Auto-generated method stub
		TaTicketDeCaisse o = (TaTicketDeCaisse) dao.findByIdFetch(id);
		if(o!=null) {
		recupSetREtat(o);
		recupSetHistREtat(o);
		recupSetLigneALigne(o);
		recupSetRDocument(o);
		recupSetREtatLigneDocuments(o);
		recupSetHistREtatLigneDocuments(o);
		recupSetRReglementLiaison(o);
		}
		return o;
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



