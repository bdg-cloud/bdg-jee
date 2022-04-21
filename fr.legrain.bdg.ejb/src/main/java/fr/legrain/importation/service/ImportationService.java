package fr.legrain.importation.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.TransactionTimeout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.icu.text.SimpleDateFormat;

import fr.legrain.abonnement.model.TaFrequence;
import fr.legrain.abonnement.service.remote.ITaFrequenceServiceRemote;
import fr.legrain.abonnement.stripe.model.TaStripePlan;
import fr.legrain.article.dao.IArticleDAO;
import fr.legrain.article.dao.IFamilleDAO;
import fr.legrain.article.dao.IMarqueArticleDAO;
import fr.legrain.article.dao.IPrixDAO;
import fr.legrain.article.dao.ITTvaDAO;
import fr.legrain.article.dao.ITvaDAO;
import fr.legrain.article.dao.IUniteDAO;
import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.champDiversDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaEntrepot;
import fr.legrain.article.model.TaFamille;
import fr.legrain.article.model.TaLot;
import fr.legrain.article.model.TaMarqueArticle;
import fr.legrain.article.model.TaPrix;
import fr.legrain.article.model.TaRTaTitreTransport;
import fr.legrain.article.model.TaRapportUnite;
import fr.legrain.article.model.TaTReception;
import fr.legrain.article.model.TaTTva;
import fr.legrain.article.model.TaTva;
import fr.legrain.article.model.TaUnite;
import fr.legrain.article.titretransport.model.TaTitreTransport;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeCustomerServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripePlanServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeSourceServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaEntrepotServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaEtatStockServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaGrMouvStockServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaMarqueArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaMouvementStockServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaRefArticleFournisseurServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaTReceptionServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaTypeMouvementServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaUniteServiceRemote;
import fr.legrain.bdg.conformite.service.remote.ITaConformiteServiceRemote;
import fr.legrain.bdg.conformite.service.remote.ITaResultatConformiteServiceRemote;
import fr.legrain.bdg.conformite.service.remote.ITaStatusConformiteServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAbonnementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAcompteServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaApporteurServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAvisEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAvoirServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBonReceptionServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBoncdeAchatServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBoncdeServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBonlivServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaDevisServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFlashServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLigneALigneServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPrelevementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPreparationServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaProformaServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaRDocumentServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTLigneServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTicketDeCaisseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.bdg.importation.service.remote.IImportationServiceRemote;
import fr.legrain.bdg.importation.service.remote.ITransactionnalMergeEtatDocumentService;
import fr.legrain.bdg.tiers.service.remote.ITaEtatServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTDocServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTPaiementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.conformite.model.TaStatusConformite;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.model.TaAbonnement;
import fr.legrain.document.model.TaBonReception;
import fr.legrain.document.model.TaInfosAbonnement;
import fr.legrain.document.model.TaInfosBonReception;
import fr.legrain.document.model.TaLAbonnement;
import fr.legrain.document.model.TaLBonReception;
import fr.legrain.document.model.TaLEcheance;
import fr.legrain.document.model.TaTLigne;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.documents.dao.IEtatDAO;
import fr.legrain.documents.dao.IFactureDAO;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.importation.model.Importation;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.lib.data.LibChaine;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;
import fr.legrain.stock.model.TaGrMouvStock;
import fr.legrain.stock.model.TaMouvementStock;
import fr.legrain.tiers.dao.ICPaiementDAO;
import fr.legrain.tiers.dao.ICompteBanqueDAO;
import fr.legrain.tiers.dao.IFamilleTiersDAO;
import fr.legrain.tiers.dao.ITAdrDAO;
import fr.legrain.tiers.dao.ITBanqueDAO;
import fr.legrain.tiers.dao.ITCPaiementDAO;
import fr.legrain.tiers.dao.ITCiviliteDAO;
import fr.legrain.tiers.dao.ITEmailDAO;
import fr.legrain.tiers.dao.ITEntiteDAO;
import fr.legrain.tiers.dao.ITPaiementDAO;
import fr.legrain.tiers.dao.ITTarifDAO;
import fr.legrain.tiers.dao.ITTelDAO;
import fr.legrain.tiers.dao.ITTiersDAO;
import fr.legrain.tiers.dao.ITTvaDocDAO;
import fr.legrain.tiers.dao.ITWebDAO;
import fr.legrain.tiers.dao.ITiersDAO;
import fr.legrain.tiers.dto.TaAdresseDTO;
import fr.legrain.tiers.dto.TaCommentaireDTO;
import fr.legrain.tiers.dto.TaCompteBanqueDTO;
import fr.legrain.tiers.dto.TaEmailDTO;
import fr.legrain.tiers.dto.TaFamilleTiersDTO;
import fr.legrain.tiers.dto.TaInfoJuridiqueDTO;
import fr.legrain.tiers.dto.TaTCiviliteDTO;
import fr.legrain.tiers.dto.TaTTarifDTO;
import fr.legrain.tiers.dto.TaTelephoneDTO;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.dto.TaWebDTO;
import fr.legrain.tiers.model.TaAdresse;
import fr.legrain.tiers.model.TaCPaiement;
import fr.legrain.tiers.model.TaCommentaire;
import fr.legrain.tiers.model.TaCompl;
import fr.legrain.tiers.model.TaCompteBanque;
import fr.legrain.tiers.model.TaEmail;
import fr.legrain.tiers.model.TaEntreprise;
import fr.legrain.tiers.model.TaFamilleTiers;
import fr.legrain.tiers.model.TaInfoJuridique;
import fr.legrain.tiers.model.TaRPrix;
import fr.legrain.tiers.model.TaTAdr;
import fr.legrain.tiers.model.TaTBanque;
import fr.legrain.tiers.model.TaTCivilite;
import fr.legrain.tiers.model.TaTEmail;
import fr.legrain.tiers.model.TaTEntite;
import fr.legrain.tiers.model.TaTTarif;
import fr.legrain.tiers.model.TaTTel;
import fr.legrain.tiers.model.TaTTiers;
import fr.legrain.tiers.model.TaTTvaDoc;
import fr.legrain.tiers.model.TaTWeb;
import fr.legrain.tiers.model.TaTelephone;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.tiers.model.TaWeb;
import org.json.*;

/**
 * Session Bean implementation class TaFactureBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class ImportationService implements IImportationServiceRemote {

	static Logger logger = Logger.getLogger(ImportationService.class);
	
	private @EJB ITaLEcheanceServiceRemote taLEcheanceService;
	private @EJB ITaStripeSourceServiceRemote taStripeSourceService;
	private @EJB ITaStripeCustomerServiceRemote taStripeCustomerService;
	@EJB private ITaStripePlanServiceRemote TaStripePlanService;
	@EJB private ITaTPaiementServiceRemote TaTPaiementService;
	@EJB private ITaFrequenceServiceRemote TaFrequenceService;
	@EJB private ITaUniteServiceRemote TaUniteService;
	@EJB private ITaAbonnementServiceRemote TaAbonnementService;
	@EJB private ITaBonReceptionServiceRemote TaBonReceptionService;
	@EJB private ITaTLigneServiceRemote taTLigneService;
	@EJB private ITaTiersServiceRemote taTiersService;
	@EJB private ITaTReceptionServiceRemote taTReceptionService;
	@EJB private ITaArticleServiceRemote taArticleService;
	@EJB private ITaEntrepotServiceRemote taEntrepotService;
	@EJB private ITaEtatStockServiceRemote taEtatStockService;
	@EJB private ITaLotServiceRemote taLotService;
	@EJB private ITaConformiteServiceRemote taConformiteService;
	@EJB private ITaResultatConformiteServiceRemote taResultatConformiteService;
	@EJB private ITaStatusConformiteServiceRemote taStatusConformiteService;	
	@EJB private ITaRefArticleFournisseurServiceRemote taRefArticleFournisseurService;
	@EJB private ITaTypeMouvementServiceRemote taTypeMouvementService;
	@EJB private ITaMouvementStockServiceRemote taMouvementStockService;
	@EJB private ITaGrMouvStockServiceRemote taGrMouvStockService;
	@EJB private ITaFlashServiceRemote taFlashService;
	@EJB private ITaUtilisateurServiceRemote taUtilisateurService;
	@EJB private ITaTDocServiceRemote taTDocService;
	@EJB private ITaPreparationServiceRemote taPreparationService;
	@EJB private ITaRDocumentServiceRemote taRDocumentService;
	@EJB private ITaLigneALigneServiceRemote taLigneALigneService;
	
	@EJB private ITaBonlivServiceRemote taBonlivService;
	@EJB private ITaBoncdeAchatServiceRemote taBoncdeAchatService;
	@EJB private ITaBoncdeServiceRemote taBoncdeService;
	@EJB private ITaDevisServiceRemote taDevisService;
	@EJB private ITaFactureServiceRemote taFactureService;
	@EJB private ITaApporteurServiceRemote taApporteurService;
	@EJB private ITaAvoirServiceRemote taAvoirService;
	@EJB private ITaAvisEcheanceServiceRemote taAvisEcheanceService;
	@EJB private ITaProformaServiceRemote taProformaService;
	@EJB private ITaPrelevementServiceRemote taPrelevementService;
	@EJB private ITaTicketDeCaisseServiceRemote taTicketDeCaisseService;
	@EJB private ITaAcompteServiceRemote taAcompteService;
	
	
	@Inject private IMarqueArticleDAO taMarqueArticleDAO;
	
	@Inject private IArticleDAO daoArticle;
	
	private @EJB ITaAbonnementServiceRemote taAbonnementService;
	@EJB	private ITaEtatServiceRemote taEtatService;
	
	@Inject private ITTvaDAO daoTTva;
	@Inject private ITvaDAO daoTva;
	@Inject private IMarqueArticleDAO daoMarque;
	@Inject private IFamilleDAO daoFamille;
	@Inject private IPrixDAO daoPrix;
	@Inject private IUniteDAO daoUnite;

	@Inject private ITiersDAO daoTiers;
	@Inject private ITEntiteDAO daoEntite;
	@Inject private ITTiersDAO daoTTiers;
	@Inject private ITTelDAO daoTTel;
	@Inject private ITWebDAO daoTWeb;
	@Inject private ITBanqueDAO daoTBanque;
	@Inject private ITEmailDAO daoTEmail;
	@Inject private ITCiviliteDAO daoTCivilite;
	@Inject private IFamilleTiersDAO daoFamilleTiers;
	@Inject private ICompteBanqueDAO daoCompteBanque;
	
	@Inject private ICPaiementDAO daoCPaiement;
	@Inject private ITPaiementDAO daoTPaiement;
	@Inject private ITCPaiementDAO daoTCPaiement;
	@Inject private ITTarifDAO daoTTarif;
	@Inject private ITTvaDocDAO daoTTvaDoc;
	@Inject private ITAdrDAO daoTAdr;
	@Inject private IFactureDAO daoFacture;
	@Inject private IEtatDAO daoEtat;
	
	@EJB private ITransactionnalMergeEtatDocumentService transactionnalMergeEtatDocumentService;
	@EJB private ITaPreferencesServiceRemote PreferencesService;
	@Inject private	SessionInfo sessionInfo;
	
    private Writer fw = null;
    private BufferedWriter bw = null;
    
	private FileReader fichierImportTiers = null;
	private FileReader fichierImportArticles = null;
	private FileReader fichierImportAbonnements = null;
	private FileReader fichierImportBonReception = null;
	
	public Importation importation=new Importation();
//	Map<String,String> mapPreferences = new HashMap<String,String>();
	
	List<IDocumentTiers> listeDocumentSrc=new LinkedList<>();
			
	/**
	 * Default constructor. 
	 */
	public ImportationService() {

	}
	
	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public boolean importationBonReception() {
		String separateur=";";
		BufferedReader br = null;
		try
		{
//			br = new BufferedReader(new InputStreamReader(  
//					new FileInputStream("C:\\LGRDOSS\\BureauDeGestion\\Inventaire LTG 310319.csv"), "ISO-8859-1"));
			br = new BufferedReader(fichierImportBonReception);

    		String ligne = br.readLine();//j'ignore la première ligne num colonne
//    		ligne = br.readLine();// et deuxième ligne entête
    		
    		//à partir de la troisième ligne
    		ligne = br.readLine();
    		String[] retour = null;
    		String[] retourOld = null;
    		int i =1;
			TaBonReception obj = new TaBonReception();
			TaLBonReception lBR;
			String codeCourant;
    		while (ligne!=null && !ligne.isEmpty()){
    			retour = ligne.split(separateur);
    			
    			if(retourOld!=null && !retourOld[2].equals(retour[2])) {
    				obj.setCodeDocument(retourOld[2]);
    				//changement de bon et donc enregistrement du bon courant
//        			boolean exist=TaBonReceptionService.exist(obj.getCodeDocument());
        			boolean exist=false;
        			if(exist==false ) {
            		System.out.println(obj.getCodeDocument());
        				//remplir bon

						TaTReception taTReception = taTReceptionService.findByCode(retourOld[1]);

            		TaTiers taTiers = taTiersService.findByCode(retourOld[0]);

            		TaInfosBonReception taInfosDocument=new TaInfosBonReception();
            		obj.setDateDocument(LibDate.stringToDate(retourOld[3]));
            		
            		SimpleDateFormat dt = new SimpleDateFormat("hh:mm:ss");
            		Date d = dt.parse(retourOld[4]);
            		
            		obj.setHeureDocument(d);
            		obj.setDateLivDocument(LibDate.stringToDate(retourOld[3]));
            		obj.setLibelleDocument(retourOld[5]);
            		obj.setTaTiers(taTiers);
            		obj.setGestionLot(true);
            		obj.setTaTReception(taTReception);
					/*
					 * TODO Gérer les mouvements corrrespondant aux lignes 
					 */
					TaGrMouvStock grpMouvStock = new TaGrMouvStock();
					grpMouvStock.setCodeGrMouvStock(obj.getCodeDocument());
					grpMouvStock.setDateGrMouvStock(obj.getDateDocument());
					if(obj.getLibelleDocument()!=null) {
						grpMouvStock.setLibelleGrMouvStock(obj.getLibelleDocument());
					} else {
						grpMouvStock.setLibelleGrMouvStock("Mouv. "+obj.getCodeDocument());
					}
					grpMouvStock.setTaTypeMouvStock(taTypeMouvementService.findByCode("R"));
					obj.setTaGrMouvStock(grpMouvStock);
					grpMouvStock.setTaBonReception(obj);
					
					for (TaLBonReception l : obj.getLignes()) {
						TaMouvementStock m = new TaMouvementStock();
						if(l.getTaMouvementStock()!=null)m=l.getTaMouvementStock();
						m.setLibelleStock(l.getLibLDocument());
						m.setDateStock(obj.getDateDocument());
						m.setEmplacement(l.getEmplacementLDocument());
						m.setTaEntrepot(l.getTaEntrepot());
						if(l.getTaLot()!=null){
							l.getTaLot().setDateLot(obj.getDateDocument());
							m.setTaLot(l.getTaLot());
						}
						m.setQte1Stock(l.getQteLDocument());
						m.setQte2Stock(l.getQte2LDocument());
						m.setUn1Stock(l.getU1LDocument());
						m.setUn2Stock(l.getU2LDocument());
						m.setTaGrMouvStock(grpMouvStock);
						l.setTaMouvementStock(m);

						grpMouvStock.getTaMouvementStockes().add(m);
					}
     			
        			obj=TaBonReceptionService.merge(obj);
        			obj = new TaBonReception();
        			}
    			}
    			lBR = new TaLBonReception();  
    			TaArticle art=daoArticle.findByCode(retour[6]);
    			TaTLigne tl=taTLigneService.findByCode("H");
    			TaEntrepot taEntrepot=taEntrepotService.findByCode(retour[9]);

    			lBR.setTaEntrepot(taEntrepot);
    			lBR.setLibLDocument(retour[7]);
    			if(retour[11]!=null && !retour[11].isEmpty())lBR.setQteLDocument(LibConversion.stringToBigDecimal(retour[11].replace(",", ".")));
    			lBR.setU1LDocument(retour[12]);
    			if(retour[13]!=null && !retour[13].equals("_"))lBR.setQte2LDocument(LibConversion.stringToBigDecimal(retour[13].replace(",", ".")));
    			if(retour[14]!=null && !retour[14].equals("_"))lBR.setU2LDocument(retour[14]);
    			lBR.setTaArticle(art);
    			
    			TaLot taLot = new TaLot();
    			taLot.setNumLot(retour[8]);
    			taLot.setDateLot(obj.getDateDocument());
    			taLot.setLibelle(art.getLibellecArticle());
    			taLot.setDluo(LibDate.stringToDate(retour[10]));
    			taLot.setUnite1(lBR.getU1LDocument());
    			taLot.setUnite2(lBR.getU2LDocument());
    			taLot.setTaArticle(lBR.getTaArticle());
    			//taLot =  taLot.initListeResultatControle(taConformiteService.controleArticleDerniereVersion(taLot.getTaArticle().getIdArticle()));
				//if(taLot.getTaResultatConformites()==null || taLot.getTaResultatConformites().isEmpty() ) {
					//aucun controle définit sur cet article
					//le lot est valide par défaut
					taLot.setLotConforme(true);
					taLot.setTaStatusConformite(taStatusConformiteService.findByCode(TaStatusConformite.C_TYPE_ACTION_AUCUN_CONTROLE_DEFINIT));
//				} else {
//					//le lot est invalide par defaut, il faut faire les controles
//					taLot.setTaStatusConformite(taStatusConformiteService.findByCode(TaStatusConformite.C_TYPE_ACTION_VIDE));
//				}
				
//				if(selectedLigneJSF.getTaRapport()!=null){
//					l.setNbDecimal(selectedLigneJSF.getTaRapport().getNbDecimal());
//					l.setSens(selectedLigneJSF.getTaRapport().getSens());
//					l.setRapport(selectedLigneJSF.getTaRapport().getRapport());
//				}
				lBR.setTaLot(taLot);
				lBR.setTaDocument(obj);
				lBR.setTaTLigne(tl);
    			


					obj.addLigne(lBR);

    			retourOld=ligne.split(separateur);
    			ligne = br.readLine();
    			lBR = new TaLBonReception();  
    		}
    		//traitement dernière pièce
//			if(!retourOld[2].equals(retour[2])) {
				obj.setCodeDocument(retourOld[2]);
				//changement de bon et donc enregistrement du bon courant
//    			boolean exist=TaBonReceptionService.exist(obj.getCodeDocument());
    			boolean exist=false;
    			if(exist==false ) {
        		System.out.println(obj.getCodeDocument());
    				//remplir bon

        		System.out.println(obj.getCodeDocument());
    				//remplir bon

					TaTReception taTReception = taTReceptionService.findByCode(retourOld[1]);

        		TaTiers taTiers = taTiersService.findByCode(retourOld[0]);

        		TaInfosBonReception taInfosDocument=new TaInfosBonReception();
        		obj.setDateDocument(LibDate.stringToDate(retourOld[3]));
        		
        		SimpleDateFormat dt = new SimpleDateFormat("hh:mm:ss");
        		Date d = dt.parse(retourOld[4]);
        		
        		obj.setHeureDocument(d);
        		obj.setDateLivDocument(LibDate.stringToDate(retourOld[3]));
        		obj.setLibelleDocument(retourOld[5]);
        		obj.setTaTiers(taTiers);
        		obj.setGestionLot(true);
        		obj.setTaTReception(taTReception);
				/*
				 * TODO Gérer les mouvements corrrespondant aux lignes 
				 */
				TaGrMouvStock grpMouvStock = new TaGrMouvStock();
				grpMouvStock.setCodeGrMouvStock(obj.getCodeDocument());
				grpMouvStock.setDateGrMouvStock(obj.getDateDocument());
				if(obj.getLibelleDocument()!=null) {
					grpMouvStock.setLibelleGrMouvStock(obj.getLibelleDocument());
				} else {
					grpMouvStock.setLibelleGrMouvStock("Mouv. "+obj.getCodeDocument());
				}
				grpMouvStock.setTaTypeMouvStock(taTypeMouvementService.findByCode("R"));
				obj.setTaGrMouvStock(grpMouvStock);
				grpMouvStock.setTaBonReception(obj);
				
				for (TaLBonReception l : obj.getLignes()) {
					TaMouvementStock m = new TaMouvementStock();
					if(l.getTaMouvementStock()!=null)m=l.getTaMouvementStock();
					m.setLibelleStock(l.getLibLDocument());
					m.setDateStock(obj.getDateDocument());
					m.setEmplacement(l.getEmplacementLDocument());
					m.setTaEntrepot(l.getTaEntrepot());
					if(l.getTaLot()!=null){
						l.getTaLot().setDateLot(obj.getDateDocument());
						m.setTaLot(l.getTaLot());
					}
					m.setQte1Stock(l.getQteLDocument());
					m.setQte2Stock(l.getQte2LDocument());
					m.setUn1Stock(l.getU1LDocument());
					m.setUn2Stock(l.getU2LDocument());
					m.setTaGrMouvStock(grpMouvStock);
					l.setTaMouvementStock(m);

					grpMouvStock.getTaMouvementStockes().add(m);
				}
 			
    			obj=TaBonReceptionService.merge(obj);
    			}
//			}    		
    		br.close();
		
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			catch(IOException ioe){
				System.out.println("erreur : " + ioe);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public boolean importationArticles(int deb,int fin) {
		BufferedReader br = null;
		boolean rentree=false;
		try
		{
    		int i =1;
    		int depart=0;
			 br = new BufferedReader(new InputStreamReader(  
					new FileInputStream("C:\\LGRDOSS\\BureauDeGestion\\ImportArticles.csv"), "ISO-8859-1"));
////			String adressedufichier = "C:/LGRDOSS/BureauDeGestion/ImportArticles.csv";
////			adressedufichier=urlFichier;
////			FileReader fr = new FileReader(adressedufichier);
////			BufferedReader br = new BufferedReader(fr);
//			 br = new BufferedReader(fichierImportArticles);

    		String ligne = br.readLine();//j'ignore la première ligne entête
    		ligne = br.readLine();
    		depart=1;
    		String[] retour = null;
    		while (depart<deb){
    			ligne = br.readLine();
    			depart++;
    		}
    		while (ligne!=null && !ligne.isEmpty() && depart<=fin){
    			rentree=true;
    			retour = ligne.split(";");
    			TaArticleDTO dto = new TaArticleDTO();
    			importation = new Importation();

    			
    			//remplir l'objet Articles
    			for(int j=0; j<retour.length; j++) {
    				if (!LibChaine.empty(retour[j]) && retour[j]!=null && !retour[j].equals("null") && !retour[j].isEmpty()) {    					
    	    			if(j==0)dto.setCodeArticle(retour[j]);
    	    			if(j==1)dto.setLibellecArticle(retour[j].replace(";", ","));
    	    			if(j==2)dto.setLibellelArticle(retour[j].replace(";", ","));
    	    			if(j==3)dto.setNumcptArticle(retour[j]);
    	    			if(j==4)dto.setActif(LibConversion.StringToBoolean(retour[j]));
    	    			
    	    			if(j==5)dto.setCommentaireArticle(retour[j]);
    	    			if(j==6)dto.setDiversArticle(retour[j]);
    	    			if(j==7)dto.setStockMinArticle(LibConversion.stringToBigDecimal(retour[j]));
    	    			
    	    			if(j==8)dto.setCodeFamille(retour[j]);
    	    			if(j==9)dto.setLibcFamille(retour[j]);
    	    			
    	    			if(j==10)
    	    				dto.setPrixPrix(LibConversion.stringToBigDecimal(retour[j].trim().replace(",", ".")));
    	    			if(j==11)dto.setPrixttcPrix(LibConversion.stringToBigDecimal(retour[j].trim().replace(",", ".")));
//    	    			  if(dto.getPrixPrix()!=null) {
//    	    				dto.setPrixttcPrix(dto.getPrixPrix().add(dto.getPrixPrix().multiply(BigDecimal.valueOf(0.21))));
//    	    			}
    	    			if(j==12)dto.setCodeUnite(retour[j]);
    	    			
    	    			if(j==13)dto.setCodeUnite2(retour[j]);
    	    			if(j==14)dto.setRapport(LibConversion.stringToBigDecimal(retour[j]));
    	    			if(j==15)dto.setSens(LibConversion.StringToBoolean(retour[j]));
    	    			
    	    			if(j==16)dto.setCodeTitreTransport(retour[j]);
    	    			if(j==17)dto.setQteTitreTransport(LibConversion.stringToBigDecimal(retour[j]));
    	    			
    	    			if(j==18)dto.setCodeTTva(retour[j]);
//    	    			if(j==18)dto.setCodeTTva("E");

    	    			if(j==19)dto.setLibTTva(retour[j]);
    	    			
    	    			if(j==20)dto.setCodeTva(retour[j]);
    	    			if(j==21)dto.setNumcptTva(retour[j]);
    	    			if(j==22)dto.setTauxTva(LibConversion.stringToBigDecimal(retour[j]));

//    	    			//chokauto
//    	    			if(j==20)dto.setCodeTva("V7");
//    	    			if(j==21)dto.setNumcptTva("445718");
//    	    			if(j==22)dto.setTauxTva(BigDecimal.valueOf(21));
    	    			
    	    			//deuxième prix
    	    			if(j==23)importation.prix1HT=(LibConversion.stringToBigDecimal(retour[j].trim().replace(",", ".")));
    	    			if(j==24)importation.prix1TTC=(LibConversion.stringToBigDecimal(retour[j].trim().replace(",", ".")));
//    	    			if(importation.prix1HT!=null) {
//    	    				importation.prix1TTC=(importation.prix1HT.add(importation.prix1HT.multiply(BigDecimal.valueOf(0.21))));
//    	    			}
    	    			
    	    			//troisième prix
    	    			if(j==25)importation.prix2HT=(LibConversion.stringToBigDecimal(retour[j].trim().replace(",", ".")));
    	    			if(j==26)importation.prix2TTC=(LibConversion.stringToBigDecimal(retour[j].trim().replace(",", ".")));
//    	    			if(importation.prix2HT!=null) {
//    	    				importation.prix2TTC=(importation.prix2HT.add(importation.prix2HT.multiply(BigDecimal.valueOf(0.21))));
//    	    			}
    	    			
    	    			if(j==27)dto.setCodeTTarif(retour[j]);
    	    			if(j==28)dto.setLibTTarif(retour[j]);
    	    			if(j==29)importation.codeTTarif1=(retour[j]);
    	    			if(j==30)importation.libelleTTarif1=(retour[j]);
    	    			if(j==31)importation.codeTTarif2=(retour[j]);
    	    			if(j==32)importation.libelleTTarif2=(retour[j]);
    	    			
    	    			if(j==33)importation.codeFamille2=(retour[j]);
    	    			if(j==34)importation.libcFamille2=(retour[j]);
    	    			
    	    			if(j==35)dto.setMatierePremiere(LibConversion.StringToBoolean(retour[j]));
    	    			if(j==36)dto.setProduitFini(LibConversion.StringToBoolean(retour[j]));
//    	    			if(j==35)dto.setMatierePremiere(false);
//    	    			if(j==36)dto.setProduitFini(false);    	    			
    	    			
//						if(j==37 && retour[j]!=null && !retour[j].equals(""))dto.setCodeMarqueArticle(retour[j]);						
//						
//
//						ObjectMapper mapper = new ObjectMapper();
//						champDiversDTO divers = new champDiversDTO();							
//						
//    	    			if(dto.getImportationDivers()== null)dto.setImportationDivers("");
//    	    			if(dto.getCommentaireArticle()== null)dto.setCommentaireArticle("");
//    	    			
//						if(j==38 && retour[j]!=null && !retour[j].equals("")) {
//							divers.setNomChamp("MODELE");
//							divers.setValeur(retour[j]);
//							//Object to JSON in String
//							dto.setImportationDivers(dto.getImportationDivers()+ mapper.writeValueAsString(divers));
//							dto.setCommentaireArticle(dto.getCommentaireArticle()+"\r\n"+ divers.getNomChamp()+" : "+retour[j]);
//						}
//						if(j==39 && retour[j]!=null && !retour[j].equals("")) {
//							divers.setNomChamp("ANNEE");
//							divers.setValeur(retour[j]);
//							//Object to JSON in String
//							dto.setImportationDivers(dto.getImportationDivers()+ mapper.writeValueAsString(divers));	
//							dto.setCommentaireArticle(dto.getCommentaireArticle()+"\r\n"+ divers.getNomChamp()+" : "+retour[j]);
//						}
//						if(j==40 && retour[j]!=null && !retour[j].equals("")) {
//							divers.setNomChamp("CERTIFICATION");
//							divers.setValeur(retour[j]);
//							//Object to JSON in String
//							dto.setImportationDivers(dto.getImportationDivers()+ mapper.writeValueAsString(divers));	
//							dto.setCommentaireArticle(dto.getCommentaireArticle()+"\r\n"+ divers.getNomChamp()+" : "+retour[j]);
//						}
//						if(j==41 && retour[j]!=null && !retour[j].equals("")) {
//							divers.setNomChamp("OEM");
//							divers.setValeur(retour[j]);
//							//Object to JSON in String
//							dto.setImportationDivers(dto.getImportationDivers()+ mapper.writeValueAsString(divers));	
//							dto.setCommentaireArticle(dto.getCommentaireArticle()+"\r\n"+ divers.getNomChamp()+" : "+retour[j]);
//						}
//						if(j==42 && retour[j]!=null && !retour[j].equals("")) {
//							divers.setNomChamp("VOLUME");
//							divers.setValeur(retour[j]);
//							//Object to JSON in String
//							dto.setImportationDivers(dto.getImportationDivers()+ mapper.writeValueAsString(divers));	
//							dto.setCommentaireArticle(dto.getCommentaireArticle()+"\r\n"+ divers.getNomChamp()+" : "+retour[j]);
//						}
//						if(j==43 && retour[j]!=null && !retour[j].equals("")) {
//							divers.setNomChamp("POIDS");
//							divers.setValeur(retour[j]);
//							//Object to JSON in String
//							dto.setImportationDivers(dto.getImportationDivers()+ mapper.writeValueAsString(divers));
//							dto.setCommentaireArticle(dto.getCommentaireArticle()+"\r\n"+ divers.getNomChamp()+" : "+retour[j]);
//						}
//						if(j==44 && retour[j]!=null && !retour[j].equals("")) {
//							divers.setNomChamp("DATE DÉBUT");
//							divers.setValeur(retour[j]);
//							//Object to JSON in String
//							dto.setImportationDivers(dto.getImportationDivers()+ mapper.writeValueAsString(divers));	
//							dto.setCommentaireArticle(dto.getCommentaireArticle()+"\r\n"+ divers.getNomChamp()+" : "+retour[j]);
//						}
//						if(j==45 && retour[j]!=null && !retour[j].equals("")) {
//							divers.setNomChamp("DATE FIN");
//							divers.setValeur(retour[j]);
//							//Object to JSON in String
//							dto.setImportationDivers(dto.getImportationDivers()+ mapper.writeValueAsString(divers));	
//							dto.setCommentaireArticle(dto.getCommentaireArticle()+"\r\n"+ divers.getNomChamp()+" : "+retour[j]);
//						}
//						if(j==46 && retour[j]!=null && !retour[j].equals("")) {
//							divers.setNomChamp("NATURE");
//							divers.setValeur(retour[j]);
//							//Object to JSON in String
//							dto.setImportationDivers(dto.getImportationDivers()+ mapper.writeValueAsString(divers));	
//							dto.setCommentaireArticle(dto.getCommentaireArticle()+"\r\n"+ divers.getNomChamp()+" : "+retour[j]);
//						}
//						if(j==47 && retour[j]!=null && !retour[j].equals("")) {
//							divers.setNomChamp("CÔTÉ");
//							divers.setValeur(retour[j]);
//							//Object to JSON in String
//							dto.setImportationDivers(dto.getImportationDivers()+ mapper.writeValueAsString(divers));	
//							dto.setCommentaireArticle(dto.getCommentaireArticle()+"\r\n"+ divers.getNomChamp()+" : "+retour[j]);
//						}
//						if(j==48 && retour[j]!=null && !retour[j].equals("")) {
//							divers.setNomChamp("AV/AR");
//							divers.setValeur(retour[j]);
//							//Object to JSON in String
//							dto.setImportationDivers(dto.getImportationDivers()+ mapper.writeValueAsString(divers));	
//							dto.setCommentaireArticle(dto.getCommentaireArticle()+"\r\n"+ divers.getNomChamp()+" : "+retour[j]);
//						}
    	    			
    				}
    			}
    			TaArticle obj = null;
    			if(dto.getCodeArticle()!=null && !dto.getCodeArticle().isEmpty()) {
    			boolean exist=daoArticle.exist(dto.getCodeArticle());
//    			if(exist==true ) {
//    				obj=daoArticle.findByCode(dto.getCodeArticle());
//        			if(dto.getCodeMarqueArticle()!=null && !dto.getCodeMarqueArticle().isEmpty()) {
//        				TaMarqueArticle m =taMarqueArticleDAO.findByCode(dto.getCodeMarqueArticle()) ;
//        				if(m==null) {
//        					m=new TaMarqueArticle();
//        					m.setCodeMarqueArticle(dto.getCodeMarqueArticle());
//        					m.setLibelleMarqueArticle(dto.getCodeMarqueArticle());
//        					m=taMarqueArticleDAO.merge(m);
//        					m=taMarqueArticleDAO.findByCode(m.getCodeMarqueArticle());
//        				}
//        				obj.setTaMarqueArticle(m);
//        			}
//        			obj=daoArticle.merge(obj);
//    			}
    			if(exist==false ) {
    				obj = new TaArticle();
        		System.out.println(dto.getCodeArticle());
        		
        		if(dto.getActif()==null)dto.setActif(true);
        		if(dto.getCodeUnite()==null)dto.setCodeUnite("U");
        		dto.setCommentaireArticle(dto.getImportationDivers());
    				
    			obj.setCodeArticle(dto.getCodeArticle());
    			obj.setLibellecArticle(dto.getLibellecArticle());
    			obj.setLibellelArticle(dto.getLibellelArticle());
    			obj.setNumcptArticle(dto.getNumcptArticle());
    			obj.setActif(LibConversion.booleanToInt(dto.getActif()));
    			obj.setCommentaireArticle(dto.getCommentaireArticle());
    			obj.setDiversArticle(dto.getDiversArticle());
    			obj.setStockMinArticle(dto.getStockMinArticle());
    			if(obj.getStockMinArticle()==null)obj.setStockMinArticle(BigDecimal.ZERO);
    			obj.setProduitFini(dto.getProduitFini());
    			obj.setMatierePremiere(dto.getMatierePremiere());
    			obj.setGestionLot(dto.getGestionLot());
    			obj.setCodeBarre(dto.getCodeBarre());
    			obj.setParamDluo(dto.getParamDluo());
    			obj.setUtiliseDlc(false);
    			obj.setImportationDivers(dto.getImportationDivers());
    			if(obj.getLibellecArticle()==null)obj.setLibellecArticle(obj.getCodeArticle());

    			if(dto.getCodeMarqueArticle()!=null && !dto.getCodeMarqueArticle().isEmpty()) {
    				TaMarqueArticle m =taMarqueArticleDAO.findByCode(dto.getCodeMarqueArticle()) ;
    				if(m==null) {
    					m=new TaMarqueArticle();
    					m.setCodeMarqueArticle(dto.getCodeMarqueArticle());
    					m.setLibelleMarqueArticle(dto.getCodeMarqueArticle());
    					m=taMarqueArticleDAO.merge(m);
    					m=taMarqueArticleDAO.findByCode(m.getCodeMarqueArticle());
    				}
    				obj.setTaMarqueArticle(m);
    			}
    			
    			if(dto.getCodeFamille()!=null && !dto.getCodeFamille().isEmpty()) {
    				TaFamille fam=daoFamille.findByCode(dto.getCodeFamille());
    				if(fam==null) {
    				fam=new TaFamille();
    				fam.setCodeFamille(dto.getCodeFamille());
    				fam.setLibcFamille(dto.getLibcFamille());
    				if(fam.getLibcFamille()==null)fam.setLibcFamille(dto.getCodeFamille());
    				fam=daoFamille.merge(fam);
    				fam=daoFamille.findByCode(fam.getCodeFamille());
    				}
    				obj.getTaFamilles().add(fam);
    				obj.setTaFamille(fam);
    			}
    			
    			if(importation.getCodeFamille2()!=null && !importation.getCodeFamille2().isEmpty()) {
    				TaFamille fam=daoFamille.findByCode(importation.getCodeFamille2());
    				if(fam==null) {
    				fam=new TaFamille();
    				fam.setCodeFamille(importation.getCodeFamille2());
    				fam.setLibcFamille(importation.getLibcFamille2());
    				if(fam.getLibcFamille()==null)fam.setLibcFamille(importation.getCodeFamille2());
    				fam=daoFamille.merge(fam);
    				fam=daoFamille.findByCode(fam.getCodeFamille());
    				}
    				obj.getTaFamilles().add(fam);
    			}
    			
    			

    			if(dto.getCodeTTva()!=null && !dto.getCodeTTva().isEmpty()) {
    				TaTTva tTva=daoTTva.findByCode(dto.getCodeTTva());
    				if(tTva==null) {
    					tTva=new TaTTva();
    					tTva.setCodeTTva(dto.getCodeTTva());
    					tTva.setLibTTva(dto.getCodeTTva());
    					tTva=daoTTva.merge(tTva);
    				}
					obj.setTaTTva(tTva);
    			}
    			
    			if(dto.getCodeTva()!=null && !dto.getCodeTva().isEmpty()) {
    				TaTva tva=daoTva.findByCode(dto.getCodeTva());
    				if(tva==null) {
    					tva=new TaTva();
    					tva.setCodeTva(dto.getCodeTva());
    					tva.setLibelleTva(dto.getCodeTva());
    					if(dto.getNumcptTva()==null || dto.getNumcptTva().isEmpty())tva.setNumcptTva("445713");
    					else tva.setNumcptTva(dto.getNumcptTva());
    					tva.setTauxTva(dto.getTauxTva());
    					tva=daoTva.merge(tva);
    				}
					obj.setTaTva(tva);
    			}

    			//prix de reference
    			if(dto.getPrixPrix()!=null || dto.getPrixttcPrix()!=null) {
    				TaPrix prix=new TaPrix();
    				prix.setIdPrix(0);
    				obj.setTaPrix(prix);
    				prix.setPrixPrix(dto.getPrixPrix());
    				prix.setPrixttcPrix(dto.getPrixttcPrix());
    				prix.setTaArticle(obj);
    				if(prix.getPrixttcPrix()==null) {
    					prix.majPrix();
    					prix.setPrixttcPrix(prix.getPrixttcPrix());
    				}
    				if(dto.getCodeTTarif()!=null && !dto.getCodeTTarif().isEmpty()) {
        				TaTTarif o=daoTTarif.findByCode(dto.getCodeTTarif());
        				if(o==null) {
        					o=new TaTTarif();
        					o.setCodeTTarif(dto.getCodeTTarif());
        					o.setLiblTTarif(dto.getLibTTarif());
        					o.setSens(0);
        					o.setPourcentage(0);
        					o=daoTTarif.merge(o);
        				}
        				TaRPrix taRPrix=new TaRPrix();
        				taRPrix.setTaPrix(prix);
        				taRPrix.setTaTTarif(o);
        				taRPrix.setCoef(taRPrix.recalculCoef(false, obj.getTaPrix()));
        				prix.addTaRPrix(taRPrix);
    				}
    				obj.addPrix(prix);
    				prix.setIdPrix(0);

    			}
    			
				
				if(dto.getCodeUnite()!=null && !dto.getCodeUnite().isEmpty()) {
					TaUnite unite=daoUnite.findByCode(dto.getCodeUnite());
					if(unite==null) {
					unite=new TaUnite();
					unite.setCodeUnite(dto.getCodeUnite());
					unite.setLiblUnite(dto.getCodeUnite());
					unite=daoUnite.merge(unite);
					}
					obj.setTaUnite1(unite);
					obj.setTaUniteReference(unite);
					obj.setTaUniteStock(unite);
				}
				
    			if(dto.getCodeUnite2()!=null && !dto.getCodeUnite2().isEmpty()) {
    				if(dto.getRapport()!=null) {
    					obj.setTaRapportUnite(new TaRapportUnite());
    					obj.getTaRapportUnite().setTaArticle(obj);
    					obj.getTaRapportUnite().setTaUnite1(obj.getTaUniteReference());
    					obj.getTaRapportUnites().add(obj.getTaRapportUnite());

    					TaUnite unite=daoUnite.findByCode(dto.getCodeUnite2());
    					if(unite==null) {
    					unite=new TaUnite();
    					unite.setCodeUnite(dto.getCodeUnite2());
    					unite.setLiblUnite(dto.getCodeUnite2());
    					unite=daoUnite.merge(unite);
    					}
    					obj.getTaRapportUnite().setTaUnite2(unite);
    					obj.getTaRapportUnite().setRapport(dto.getRapport());
    					obj.getTaRapportUnite().setSens(LibConversion.booleanToInt(dto.getSens()));
    					obj.getTaRapportUnite().setNbDecimal(Integer.valueOf(2));
    				}
    				
    			}
    			if(dto.getCodeTitreTransport()!=null && !dto.getCodeTitreTransport().isEmpty()) {
    				obj.setTaRTaTitreTransport(new TaRTaTitreTransport());
    				obj.getTaRTaTitreTransport().setTaArticle(obj);
    				obj.getTaRTaTitreTransport().setQteTitreTransport(dto.getQteTitreTransport());
    				obj.getTaRTaTitreTransport().setTaTitreTransport(new TaTitreTransport());
    				obj.getTaRTaTitreTransport().getTaTitreTransport().setQteMinTitreTransport(0);
    				obj.getTaRTaTitreTransport().getTaTitreTransport().setLibelleTitreTransport(dto.getCodeTitreTransport());
    				obj.getTaRTaTitreTransport().getTaTitreTransport().setCodeTitreTransport(dto.getCodeTitreTransport());
    			}
    			
    			
    			
    			//si prix 1 rempli
    			if(importation.prix1HT!=null ||importation.prix1TTC!=null) {
    				TaPrix prix =new TaPrix();
    				prix.setIdPrix(1);
    				prix.setPrixPrix(importation.prix1HT);
    				prix.setPrixttcPrix(importation.prix1TTC);
    				if(prix.getPrixttcPrix()==null) {
    					prix.majPrix();
    					prix.setPrixttcPrix(prix.getPrixttcPrix());
    				}    				
    				prix.setTaArticle(obj);
    				obj.addPrix(prix);
    				if(importation.codeTTarif1!=null && !importation.codeTTarif1.isEmpty()) {
        				TaTTarif o=daoTTarif.findByCode(importation.codeTTarif1);
        				if(o==null) {
        					o=new TaTTarif();
        					o.setCodeTTarif(importation.codeTTarif1);
        					o.setLiblTTarif(importation.libelleTTarif1);
        					o.setSens(0);
        					o.setPourcentage(0);
        					o=daoTTarif.merge(o);
        				}
        				TaRPrix taRPrix=new TaRPrix();
        				taRPrix.setTaPrix(prix);
        				taRPrix.setTaTTarif(o);
        				taRPrix.setCoef(taRPrix.recalculCoef(false, obj.getTaPrix()));
        				prix.addTaRPrix(taRPrix);
        				prix.setIdPrix(0);
    				}
    			}
    			
    			//si prix 2 rempli
    			if(importation.prix2HT!=null ||importation.prix2TTC!=null) {
    				TaPrix prix =new TaPrix();
    				prix.setIdPrix(2);
    				prix.setPrixPrix(importation.prix2HT);
    				prix.setPrixttcPrix(importation.prix2TTC);
    				if(prix.getPrixttcPrix()==null) {
    					prix.majPrix();
    					prix.setPrixttcPrix(prix.getPrixttcPrix());
    				}
    				prix.setTaArticle(obj);
    				obj.addPrix(prix);
    				if(importation.codeTTarif2!=null && !importation.codeTTarif2.isEmpty()) {
        				TaTTarif o=daoTTarif.findByCode(importation.codeTTarif2);
        				if(o==null) {
        					o=new TaTTarif();
        					o.setCodeTTarif(importation.codeTTarif2);
        					o.setLiblTTarif(importation.libelleTTarif2);
        					o.setSens(0);
        					o.setPourcentage(0);
        					o=daoTTarif.merge(o);
        				}
        				TaRPrix taRPrix=new TaRPrix();
        				taRPrix.setTaPrix(prix);
        				taRPrix.setTaTTarif(o);
        				taRPrix.setCoef(taRPrix.recalculCoef(false, obj.getTaPrix()));
        				prix.addTaRPrix(taRPrix);
        				prix.setIdPrix(0);
    				}
    			}
    			obj.setCompose(false);
    			obj.setAbonnement(false);
    			obj=daoArticle.merge(obj);
    			}
    			}
    			ligne = br.readLine();
    			depart++;
    		}
    		br.close();
		}
			catch(IOException ioe){
				System.out.println("erreur : " + ioe);
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (ExceptLgr e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					br.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		return rentree;
	}

	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public boolean importationArticlesRemanieParDenis(int deb,int fin) {
		BufferedReader br = null;
		boolean rentree=false;
		try
		{
    		int i =1;
    		int depart=0;
			 br = new BufferedReader(new InputStreamReader(  
					new FileInputStream("C:\\LGRDOSS\\BureauDeGestion\\ImportArticles.csv"), "ISO-8859-1"));
////			String adressedufichier = "C:/LGRDOSS/BureauDeGestion/ImportArticles.csv";
////			adressedufichier=urlFichier;
////			FileReader fr = new FileReader(adressedufichier);
////			BufferedReader br = new BufferedReader(fr);
//			 br = new BufferedReader(fichierImportArticles);

    		String ligne = br.readLine();//j'ignore la première ligne entête
    		ligne = br.readLine();
    		depart=1;
    		String[] retour = null;
    		while (depart<deb){
    			ligne = br.readLine();
    			depart++;
    		}
    		while (ligne!=null && !ligne.isEmpty() && depart<=fin){
    			rentree=true;
    			retour = ligne.split(";");
    			TaArticleDTO dto = new TaArticleDTO();
    			
    			importation=new Importation();
    			
    			//remplir l'objet Articles
    			for(int j=0; j<retour.length; j++) {
    				if (!LibChaine.empty(retour[j]) && retour[j]!=null && !retour[j].equals("null") && !retour[j].isEmpty()) {    					
    	    			if(j==0)dto.setCodeArticle(retour[j]);
    	    			if(j==1)dto.setLibellecArticle(retour[j]);
    	    			if(j==2)dto.setLibellelArticle(retour[j]);
    	    			if(j==3)dto.setNumcptArticle(retour[j]);
    	    			if(j==4)dto.setCommentaireArticle(retour[j]);
    	    			if(j==5)dto.setDiversArticle(retour[j]);
    	    			if(j==6)dto.setStockMinArticle(LibConversion.stringToBigDecimal(retour[j].replace(",", ".")));
    	    			
    	    			if(j==7)dto.setCodeFamille(retour[j]);
    	    			
    	    			if(j==8)dto.setPrixPrix(LibConversion.stringToBigDecimal(retour[j].replace(",", ".")));
    	    			if(j==9)dto.setPrixttcPrix(LibConversion.stringToBigDecimal(retour[j].replace(",", ".")));
    	    			
    	    			if(j==10)dto.setCodeUniteReference(retour[j]);    	    			
    	    			if(j==11)dto.setCodeUnite(retour[j]);
    	    			if(j==12)dto.setCodeUnite2(retour[j]);

    	    			if(j==13)dto.setRapport(LibConversion.stringToBigDecimal(retour[j]));
    	    			if(j==14)dto.setSens(LibConversion.StringToBoolean(retour[j]));
    	    			
    	    			if(j==15)dto.setCodeTitreTransport(retour[j]);
    	    			if(j==16)dto.setQteTitreTransport(LibConversion.stringToBigDecimal(retour[j].replace(",", ".")));
    	    			
    	    			if(j==17)dto.setCodeTTva(retour[j]);
    	    			if(j==18)dto.setLibTTva(retour[j]);
    	    			
    	    			if(j==19)dto.setCodeTva(retour[j]);

  
    	    			
    	    			//PREMIER prix hors référence
    	    			if(j==20)importation.codeTTarif1=(retour[j]);
    	    			if(j==21)importation.prix1HT=(LibConversion.stringToBigDecimal(retour[j].replace(",", ".")));
    	    			if(j==22)importation.prix1TTC=(LibConversion.stringToBigDecimal(retour[j].replace(",", ".")));

    	    			
    	    			//deuxième prix
    	    			if(j==23)importation.codeTTarif2=(retour[j]);
    	    			if(j==24)importation.prix2HT=(LibConversion.stringToBigDecimal(retour[j].replace(",", ".")));
    	    			if(j==25)importation.prix2TTC=(LibConversion.stringToBigDecimal(retour[j].replace(",", ".")));

    	    			//troisième prix
    	    			if(j==26)importation.codeTTarif3=(retour[j]);
    	    			if(j==27)importation.prix3HT=(LibConversion.stringToBigDecimal(retour[j].replace(",", ".")));
    	    			if(j==28)importation.prix3TTC=(LibConversion.stringToBigDecimal(retour[j].replace(",", ".")));
    	    			
    	    			if(j==29)dto.setCodeTTarif(retour[j]);
    	    			if(j==30)dto.setLibTTarif(retour[j]);



    	    			
    	    			if(j==31)importation.codeFamille2=(retour[j]);
    	    			
    	    			if(j==32)dto.setMatierePremiere(LibConversion.StringToBoolean(retour[j]));
    	    			if(j==33)dto.setProduitFini(LibConversion.StringToBoolean(retour[j]));


    	    			
    				}
    			}
    			TaArticle obj = null;
    			if(dto.getCodeArticle()!=null && !dto.getCodeArticle().isEmpty()) {
    			boolean exist=daoArticle.exist((dto.getCodeArticle()));
    			if(exist==false ) {
    				obj = new TaArticle();
        		System.out.println(dto.getCodeArticle());
    				
    			obj.setCodeArticle(dto.getCodeArticle());
    			obj.setLibellecArticle(dto.getLibellecArticle());
    			obj.setLibellelArticle(dto.getLibellelArticle());
    			obj.setNumcptArticle(dto.getNumcptArticle());
    			obj.setActif(1);
    			obj.setCommentaireArticle(dto.getCommentaireArticle());
    			obj.setDiversArticle(dto.getDiversArticle());
    			obj.setStockMinArticle(dto.getStockMinArticle());
    			obj.setProduitFini(dto.getProduitFini());
    			obj.setMatierePremiere(dto.getMatierePremiere());
    			obj.setGestionLot(dto.getGestionLot());
    			obj.setCodeBarre(dto.getCodeBarre());
    			obj.setParamDluo(dto.getParamDluo());
    			obj.setUtiliseDlc(false);

    			
    			if(dto.getCodeFamille()!=null && !dto.getCodeFamille().isEmpty()) {
    				TaFamille fam=daoFamille.findByCode(dto.getCodeFamille());
    				if(fam==null) {
    				fam=new TaFamille();
    				fam.setCodeFamille(dto.getCodeFamille());
    				fam.setLibcFamille(dto.getLibcFamille());
    				fam=daoFamille.merge(fam);
    				fam=daoFamille.findByCode(fam.getCodeFamille());
    				}
    				obj.getTaFamilles().add(fam);
    				obj.setTaFamille(fam);
    			}
    			
    			if(importation.getCodeFamille2()!=null && !importation.getCodeFamille2().isEmpty()) {
    				TaFamille fam=daoFamille.findByCode(importation.getCodeFamille2());
    				if(fam==null) {
    				fam=new TaFamille();
    				fam.setCodeFamille(importation.getCodeFamille2());
    				fam.setLibcFamille(importation.getLibcFamille2());
    				fam=daoFamille.merge(fam);
    				fam=daoFamille.findByCode(fam.getCodeFamille());
    				}
    				obj.getTaFamilles().add(fam);
    			}
    			
    			

    			if(dto.getCodeTTva()!=null && !dto.getCodeTTva().isEmpty()) {
    				TaTTva tTva=daoTTva.findByCode(dto.getCodeTTva());
    				if(tTva==null) {
    					tTva=new TaTTva();
    					tTva.setCodeTTva(dto.getCodeTTva());
    					tTva.setLibTTva(dto.getCodeTTva());
    					tTva=daoTTva.merge(tTva);
    				}
					obj.setTaTTva(tTva);
    			}
    			
    			if(dto.getCodeTva()!=null && !dto.getCodeTva().isEmpty()) {
    				TaTva tva=daoTva.findByCode(dto.getCodeTva());
    				if(tva==null) {
    					tva=new TaTva();
    					tva.setCodeTva(dto.getCodeTva());
    					tva.setLibelleTva(dto.getCodeTva());
    					if(dto.getNumcptTva()==null || dto.getNumcptTva().isEmpty())tva.setNumcptTva("445713");
    					else tva.setNumcptTva(dto.getNumcptTva());
    					tva.setTauxTva(dto.getTauxTva());
    					tva=daoTva.merge(tva);
    				}
					obj.setTaTva(tva);
    			}

    			
    			//prix de reference
    			if(dto.getPrixPrix()!=null || dto.getPrixttcPrix()!=null) {
    				TaPrix prix=new TaPrix();
    				prix.setIdPrix(0);
    				obj.setTaPrix(prix);
    				prix.setPrixPrix(dto.getPrixPrix());
    				prix.setPrixttcPrix(dto.getPrixttcPrix());
    				prix.setTaArticle(obj);
    				if(prix.getPrixttcPrix()==null) {
    					prix.majPrix();
    					prix.setPrixttcPrix(prix.getPrixttcPrix());
    				}
    				if(dto.getCodeTTarif()!=null && !dto.getCodeTTarif().isEmpty()) {
        				TaTTarif o=daoTTarif.findByCode(dto.getCodeTTarif());
        				if(o==null) {
        					o=new TaTTarif();
        					o.setCodeTTarif(dto.getCodeTTarif());
        					o.setLiblTTarif(dto.getLibTTarif());
        					o.setSens(0);
        					o.setPourcentage(0);
        					o=daoTTarif.merge(o);
        				}
        				TaRPrix taRPrix=new TaRPrix();
        				taRPrix.setTaPrix(prix);
        				taRPrix.setTaTTarif(o);
        				taRPrix.setCoef(taRPrix.recalculCoef(false, obj.getTaPrix()));
        				prix.addTaRPrix(taRPrix);
    				}
    				obj.addPrix(prix);
    				prix.setIdPrix(0);

    			}
    			
				
				if(dto.getCodeUnite()!=null && !dto.getCodeUnite().isEmpty()) {
					TaUnite unite=daoUnite.findByCode(dto.getCodeUnite());
					if(unite==null) {
					unite=new TaUnite();
					unite.setCodeUnite(dto.getCodeUnite());
					unite.setLiblUnite(dto.getCodeUnite());
					unite=daoUnite.merge(unite);
					}
					obj.setTaUnite1(unite);
					obj.setTaUniteReference(unite);
					obj.setTaUniteStock(unite);
				}
				
    			if(dto.getCodeUnite2()!=null && !dto.getCodeUnite2().isEmpty()) {
    				if(dto.getRapport()!=null) {
    					obj.setTaRapportUnite(new TaRapportUnite());
    					obj.getTaRapportUnite().setTaArticle(obj);
    					obj.getTaRapportUnite().setTaUnite1(obj.getTaUniteReference());
    					obj.getTaRapportUnites().add(obj.getTaRapportUnite());

    					TaUnite unite=daoUnite.findByCode(dto.getCodeUnite2());
    					if(unite==null) {
    					unite=new TaUnite();
    					unite.setCodeUnite(dto.getCodeUnite2());
    					unite.setLiblUnite(dto.getCodeUnite2());
    					unite=daoUnite.merge(unite);
    					}
    					obj.getTaRapportUnite().setTaUnite2(unite);
    					obj.getTaRapportUnite().setRapport(dto.getRapport());
    					obj.getTaRapportUnite().setSens(LibConversion.booleanToInt(dto.getSens()));
    					obj.getTaRapportUnite().setNbDecimal(Integer.valueOf(2));
    				}
    				
    			}
//    			if(dto.getCodeTitreTransport()!=null && !dto.getCodeTitreTransport().isEmpty()) {
//    				obj.setTaRTaTitreTransport(new TaRTaTitreTransport());
//    				obj.getTaRTaTitreTransport().setTaArticle(obj);
//    				obj.getTaRTaTitreTransport().setQteTitreTransport(dto.getQteTitreTransport());
//    				obj.getTaRTaTitreTransport().setTaTitreTransport(new TaTitreTransport());
//    				obj.getTaRTaTitreTransport().getTaTitreTransport().setQteMinTitreTransport(0);
//    				obj.getTaRTaTitreTransport().getTaTitreTransport().setLibelleTitreTransport(dto.getCodeTitreTransport());
//    				obj.getTaRTaTitreTransport().getTaTitreTransport().setCodeTitreTransport(dto.getCodeTitreTransport());
//    			}
    			
    			
    			
    			//si prix 1 rempli
    			if(importation.prix1HT!=null ||importation.prix1TTC!=null) {
    				TaPrix prix =new TaPrix();
    				prix.setIdPrix(1);
    				prix.setPrixPrix(importation.prix1HT);
    				prix.setPrixttcPrix(importation.prix1TTC);
    				prix.setTaArticle(obj);
    				if(prix.getPrixttcPrix()==null) {
    					prix.majPrix();
    					prix.setPrixttcPrix(prix.getPrixttcPrix());
    				}
    				obj.addPrix(prix);
    				if(importation.codeTTarif1!=null && !importation.codeTTarif1.isEmpty()) {
        				TaTTarif o=daoTTarif.findByCode(importation.codeTTarif1);
        				if(o==null) {
        					o=new TaTTarif();
        					o.setCodeTTarif(importation.codeTTarif1);
        					o.setLiblTTarif(importation.libelleTTarif1);
        					o.setSens(0);
        					o.setPourcentage(0);
        					o=daoTTarif.merge(o);
        				}
        				TaRPrix taRPrix=new TaRPrix();
        				taRPrix.setTaPrix(prix);
        				taRPrix.setTaTTarif(o);
        				taRPrix.setCoef(taRPrix.recalculCoef(false, obj.getTaPrix()));
        				prix.addTaRPrix(taRPrix);
        				prix.setIdPrix(0);
    				}
    			}
    			
    			//si prix 2 rempli
    			if(importation.prix2HT!=null ||importation.prix2TTC!=null) {
    				TaPrix prix =new TaPrix();
    				prix.setIdPrix(2);
    				prix.setPrixPrix(importation.prix2HT);
    				prix.setPrixttcPrix(importation.prix2TTC);
    				prix.setTaArticle(obj);
    				if(prix.getPrixttcPrix()==null) {
    					prix.majPrix();
    					prix.setPrixttcPrix(prix.getPrixttcPrix());
    				}
    				obj.addPrix(prix);
    				if(importation.codeTTarif2!=null && !importation.codeTTarif2.isEmpty()) {
        				TaTTarif o=daoTTarif.findByCode(importation.codeTTarif2);
        				if(o==null) {
        					o=new TaTTarif();
        					o.setCodeTTarif(importation.codeTTarif2);
        					o.setLiblTTarif(importation.libelleTTarif2);
        					o.setSens(0);
        					o.setPourcentage(0);
        					o=daoTTarif.merge(o);
        				}
        				TaRPrix taRPrix=new TaRPrix();
        				taRPrix.setTaPrix(prix);
        				taRPrix.setTaTTarif(o);
        				taRPrix.setCoef(taRPrix.recalculCoef(false, obj.getTaPrix()));
        				prix.addTaRPrix(taRPrix);
        				prix.setIdPrix(0);
    				}
    			}
    			
    			//si prix 3 rempli
    			if(importation.prix3HT!=null ||importation.prix3TTC!=null) {
    				TaPrix prix =new TaPrix();
    				prix.setIdPrix(3);
    				prix.setPrixPrix(importation.prix3HT);
    				prix.setPrixttcPrix(importation.prix3TTC);
    				prix.setTaArticle(obj);
    				if(prix.getPrixttcPrix()==null) {
    					prix.majPrix();
    					prix.setPrixttcPrix(prix.getPrixttcPrix());
    				}
    				obj.addPrix(prix);
    				if(importation.codeTTarif3!=null && !importation.codeTTarif3.isEmpty()) {
        				TaTTarif o=daoTTarif.findByCode(importation.codeTTarif3);
        				if(o==null) {
        					o=new TaTTarif();
        					o.setCodeTTarif(importation.codeTTarif3);
        					o.setLiblTTarif(importation.libelleTTarif3);
        					o.setSens(0);
        					o.setPourcentage(0);
        					o=daoTTarif.merge(o);
        				}
        				TaRPrix taRPrix=new TaRPrix();
        				taRPrix.setTaPrix(prix);
        				taRPrix.setTaTTarif(o);
        				taRPrix.setCoef(taRPrix.recalculCoef(false, obj.getTaPrix()));
        				prix.addTaRPrix(taRPrix);
        				prix.setIdPrix(0);
    				}
    			}
    			
    			obj.setCompose(false);
    			obj.setAbonnement(false);
    			obj=daoArticle.merge(obj);
    			}
    			}
    			ligne = br.readLine();
    			depart++;
    		}
    		br.close();
		}
			catch(IOException ioe){
				System.out.println("erreur : " + ioe);
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (ExceptLgr e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					br.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		return rentree;
	}

	
	@Override
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public boolean importationArticlesCanardises() {
		BufferedReader br = null;
		try
		{
			 br = new BufferedReader(new InputStreamReader(  
					new FileInputStream("C:\\LGRDOSS\\BureauDeGestion\\ImportArticles.csv"), "ISO-8859-1"));
////			String adressedufichier = "C:/LGRDOSS/BureauDeGestion/ImportArticles.csv";
////			adressedufichier=urlFichier;
////			FileReader fr = new FileReader(adressedufichier);
////			BufferedReader br = new BufferedReader(fr);
//			 br = new BufferedReader(fichierImportArticles);

    		String ligne = br.readLine();//j'ignore la première ligne entête
    		ligne = br.readLine();
    		String[] retour = null;
    		int i =1;
    		while (ligne!=null && !ligne.isEmpty()){
    			retour = ligne.split(";");
    			TaArticleDTO dto = new TaArticleDTO();
    			String paramDluo = null;
    			String codeBarre = null;
    			boolean utiliseDlc = false;
    			Integer idFournisseur1= null;
    			Integer idFournisseur2= null;
    			

    			
    			//remplir l'objet Articles
    			for(int j=0; j<retour.length; j++) {
    				if (!LibChaine.empty(retour[j]) && retour[j]!=null && !retour[j].equals("null") && !retour[j].isEmpty()) {    					
    	    			if(j==0)dto.setCodeArticle(retour[j]);
    	    			if(j==1)dto.setLibellecArticle(retour[j]);
    	    			if(j==2)dto.setLibellelArticle(retour[j]);
    	    			if(j==3)dto.setNumcptArticle(retour[j]);
    	    			if(j==4)dto.setActif(LibConversion.StringToBoolean(retour[j]));
    	    			if(j==5)dto.setCommentaireArticle(retour[j]);
    	    			if(j==6)dto.setDiversArticle(retour[j]);
    	    			if(j==7)dto.setStockMinArticle(LibConversion.stringToBigDecimal(retour[j]));
    	    			
    	    			if(j==8)dto.setCodeFamille(retour[j]);
    	    			if(j==9)dto.setLibcFamille(retour[j]);
    	    			
    	    			if(j==10) {
    	    				if(retour[j]!=null)dto.setPrixPrix(LibConversion.stringToBigDecimal(retour[j].replace("-", "").replace("$", "").replace(",", ".").trim()));
    	    			}
    	    			if(j==11)dto.setPrixttcPrix(dto.getPrixPrix());    	    			
    	    			if(j==12)dto.setCodeUnite(retour[j]);
    	    			
    	    			if(j==13)
    	    				dto.setCodeUniteReference(retour[j]);
    	    			if(j==14)
    	    				dto.setRapport(LibConversion.stringToBigDecimal(retour[j]));
    	    			if(j==15)dto.setSens(LibConversion.StringToBoolean(retour[j]));
    	    			
    	    			if(j==16)dto.setCodeTitreTransport(retour[j]);
    	    			if(j==17)dto.setQteTitreTransport(LibConversion.stringToBigDecimal(retour[j]));
    	    			
    	    			if(j==18)dto.setCodeTTva(retour[j]);
    	    			if(j==19)dto.setLibTTva(retour[j]);
    	    			
    	    			if(j==20)dto.setCodeTva(retour[j]);
    	    			if(j==21)dto.setNumcptTva(retour[j]);
    	    			if(j==22)dto.setTauxTva(LibConversion.stringToBigDecimal(retour[j]));
    	    			
    	    			//deuxième prix
    	    			if(j==23)importation.prix1HT=(LibConversion.stringToBigDecimal(retour[j]));
    	    			if(j==24)importation.prix1TTC=(LibConversion.stringToBigDecimal(retour[j]));
    	    			
    	    			//troisième prix
    	    			if(j==25)importation.prix2HT=(LibConversion.stringToBigDecimal(retour[j]));
    	    			if(j==26)importation.prix2TTC=(LibConversion.stringToBigDecimal(retour[j]));
    	    			
    	    			if(j==27)dto.setCodeTTarif(retour[j]);
    	    			if(j==28)dto.setLibTTarif(retour[j]);
    	    			if(j==29)importation.codeTTarif1=(retour[j]);
    	    			if(j==30)importation.libelleTTarif1=(retour[j]);
    	    			if(j==31)importation.codeTTarif2=(retour[j]);
    	    			if(j==32)importation.libelleTTarif2=(retour[j]);
    	    			
    	    			if(j==33)importation.codeFamille2=(retour[j]);
    	    			if(j==34)importation.libcFamille2=(retour[j]);
    	    			if(j==35)dto.setMatierePremiere(LibConversion.StringToBoolean(retour[j]));
    	    			if(j==36)dto.setProduitFini(LibConversion.StringToBoolean(retour[j]));

    	    			if(j==37)codeBarre=retour[j];
    	    			if(j==38)utiliseDlc=(LibConversion.StringToBoolean(retour[j]));
    	    			if(j==39)paramDluo=retour[j];
    	    			if(j==40)idFournisseur1=(LibConversion.stringToInteger(retour[j]));
    	    			if(j==41)idFournisseur2=(LibConversion.stringToInteger(retour[j]));

    	    			
    				}
    			}
    			TaArticle obj = null;
    			boolean exist=daoArticle.exist((dto.getCodeArticle()));
    			if(exist==false ) {
    				obj = new TaArticle();
        		System.out.println(dto.getCodeArticle());
    				
    			obj.setCodeArticle(dto.getCodeArticle());
    			obj.setLibellecArticle(dto.getLibellecArticle());
    			obj.setLibellelArticle(dto.getLibellelArticle());
    			obj.setNumcptArticle(dto.getNumcptArticle());
    			obj.setActif(LibConversion.booleanToInt(dto.getActif()));
    			obj.setCommentaireArticle(dto.getCommentaireArticle());
    			obj.setDiversArticle(dto.getDiversArticle());
    			obj.setStockMinArticle(dto.getStockMinArticle());    			
    			obj.setProduitFini(dto.getProduitFini());
    			obj.setMatierePremiere(dto.getMatierePremiere());
    			obj.setGestionLot(dto.getGestionLot());
    			obj.setCodeBarre(codeBarre);
    			obj.setParamDluo(paramDluo);
    			obj.setUtiliseDlc(utiliseDlc);

    			
    			if(dto.getCodeFamille()!=null && !dto.getCodeFamille().isEmpty()) {
    				TaFamille fam=daoFamille.findByCode(dto.getCodeFamille());
    				if(fam==null) {
    				fam=new TaFamille();
    				fam.setCodeFamille(dto.getCodeFamille());
    				fam.setLibcFamille(dto.getLibcFamille());
    				fam=daoFamille.merge(fam);
    				fam=daoFamille.findByCode(fam.getCodeFamille());
    				}
    				obj.getTaFamilles().add(fam);
    				obj.setTaFamille(fam);
    			}
    			
    			if(importation.getCodeFamille2()!=null && !importation.getCodeFamille2().isEmpty()) {
    				TaFamille fam=daoFamille.findByCode(importation.getCodeFamille2());
    				if(fam==null) {
    				fam=new TaFamille();
    				fam.setCodeFamille(importation.getCodeFamille2());
    				fam.setLibcFamille(importation.getLibcFamille2());
    				fam=daoFamille.merge(fam);
    				fam=daoFamille.findByCode(fam.getCodeFamille());
    				}
    				obj.getTaFamilles().add(fam);
    			}
    			
    			

    			if(dto.getCodeTTva()!=null && !dto.getCodeTTva().isEmpty()) {
    				TaTTva tTva=daoTTva.findByCode(dto.getCodeTTva());
    				if(tTva==null) {
    					tTva=new TaTTva();
    					tTva.setCodeTTva(dto.getCodeTTva());
    					tTva.setLibTTva(dto.getCodeTTva());
    					tTva=daoTTva.merge(tTva);
    				}
					obj.setTaTTva(tTva);
    			}
    			
    			if(dto.getCodeTva()!=null && !dto.getCodeTva().isEmpty()) {
    				TaTva tva=daoTva.findByCode(dto.getCodeTva());
    				if(tva==null) {
    					tva=new TaTva();
    					tva.setCodeTva(dto.getCodeTva());
    					tva.setLibelleTva(dto.getCodeTva());
    					if(dto.getNumcptTva()==null || dto.getNumcptTva().isEmpty())tva.setNumcptTva("445713");
    					else tva.setNumcptTva(dto.getNumcptTva());
    					tva.setTauxTva(dto.getTauxTva());
    					tva=daoTva.merge(tva);
    				}
					obj.setTaTva(tva);
    			}

    			
    			//prix de reference
    			if(dto.getPrixPrix()!=null || dto.getPrixttcPrix()!=null) {
    				TaPrix prix=new TaPrix();
    				obj.setTaPrix(prix);
    				prix.setPrixPrix(dto.getPrixPrix());
    				prix.setPrixttcPrix(dto.getPrixttcPrix());
    				prix.setTaArticle(obj);
    				prix.majPrix();
    				prix.setPrixttcPrix(prix.getPrixttcPrix());
    				
    				if(dto.getCodeTTarif()!=null && !dto.getCodeTTarif().isEmpty()) {
        				TaTTarif o=daoTTarif.findByCode(dto.getCodeTTarif());
        				if(o==null) {
        					o=new TaTTarif();
        					o.setCodeTTarif(dto.getCodeTTarif());
        					o.setLiblTTarif(dto.getLibTTarif());
        					o.setSens(0);
        					o.setPourcentage(0);
        					o=daoTTarif.merge(o);
        				}
        				TaRPrix taRPrix=new TaRPrix();
        				taRPrix.setTaPrix(prix);
        				taRPrix.setTaTTarif(o);
        				prix.addTaRPrix(taRPrix);
    				}
    				obj.addPrix(prix);

    			}
    			
				
				if(dto.getCodeUnite()!=null && !dto.getCodeUnite().isEmpty()) {
					TaUnite unite=daoUnite.findByCode(dto.getCodeUnite());
					if(unite==null) {
					unite=new TaUnite();
					unite.setCodeUnite(dto.getCodeUnite());
					unite.setLiblUnite(dto.getCodeUnite());
					unite=daoUnite.merge(unite);
					}
					obj.setTaUnite1(unite);
					obj.setTaUniteReference(unite);
					obj.setTaUniteStock(unite);
				}
				
				
    			if(dto.getCodeTitreTransport()!=null && !dto.getCodeTitreTransport().isEmpty()) {
    				obj.setTaRTaTitreTransport(new TaRTaTitreTransport());
    				obj.getTaRTaTitreTransport().setTaArticle(obj);
    				obj.getTaRTaTitreTransport().setQteTitreTransport(dto.getQteTitreTransport());
    				obj.getTaRTaTitreTransport().setTaTitreTransport(new TaTitreTransport());
    				obj.getTaRTaTitreTransport().getTaTitreTransport().setQteMinTitreTransport(0);
    				obj.getTaRTaTitreTransport().getTaTitreTransport().setLibelleTitreTransport(dto.getCodeTitreTransport());
    				obj.getTaRTaTitreTransport().getTaTitreTransport().setCodeTitreTransport(dto.getCodeTitreTransport());
    			}
    			
    			
    			
    			//si prix 1 rempli
    			if(importation.prix1HT!=null ||importation.prix1TTC!=null) {
    				TaPrix prix =new TaPrix();
    				prix.setPrixPrix(importation.prix1HT);
    				prix.setPrixttcPrix(importation.prix1TTC);
    				prix.setTaArticle(obj);
    				obj.addPrix(prix);
    				if(importation.codeTTarif1!=null && !importation.codeTTarif1.isEmpty()) {
        				TaTTarif o=daoTTarif.findByCode(importation.codeTTarif1);
        				if(o==null) {
        					o=new TaTTarif();
        					o.setCodeTTarif(importation.codeTTarif1);
        					o.setLiblTTarif(importation.libelleTTarif1);
        					o.setSens(0);
        					o.setPourcentage(0);
        					o=daoTTarif.merge(o);
        				}
        				TaRPrix taRPrix=new TaRPrix();
        				taRPrix.setTaPrix(prix);
        				taRPrix.setTaTTarif(o);
        				prix.addTaRPrix(taRPrix);
    				}
    			}
    			
    			//si prix 2 rempli
    			if(importation.prix2HT!=null ||importation.prix2TTC!=null) {
    				TaPrix prix =new TaPrix();
    				prix.setPrixPrix(importation.prix2HT);
    				prix.setPrixttcPrix(importation.prix2TTC);
    				prix.setTaArticle(obj);
    				obj.addPrix(prix);
    				if(importation.codeTTarif2!=null && !importation.codeTTarif2.isEmpty()) {
        				TaTTarif o=daoTTarif.findByCode(importation.codeTTarif2);
        				if(o==null) {
        					o=new TaTTarif();
        					o.setCodeTTarif(importation.codeTTarif2);
        					o.setLiblTTarif(importation.libelleTTarif2);
        					o.setSens(0);
        					o.setPourcentage(0);
        					o=daoTTarif.merge(o);
        				}
        				TaRPrix taRPrix=new TaRPrix();
        				taRPrix.setTaPrix(prix);
        				taRPrix.setTaTTarif(o);
        				prix.addTaRPrix(taRPrix);
    				}
    			}
    			obj.setCompose(false);
    			obj.setAbonnement(false);
    			TaTiers fournisseur1=null;
    			TaTiers fournisseur2=null;
    			try {
    				if(idFournisseur1!=null) {
    					fournisseur1=taTiersService.findById(idFournisseur1);
    					obj.getTaFournisseurs().add(fournisseur1);
    				}
    				if(idFournisseur2!=null) {
    					fournisseur2=taTiersService.findById(idFournisseur2);
    					obj.getTaFournisseurs().add(fournisseur2);
    				}
				} catch (FinderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			
    			
    			obj=daoArticle.merge(obj);
    			}
    			ligne = br.readLine();
    		}
    		br.close();
		}
			catch(IOException ioe){
				System.out.println("erreur : " + ioe);
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (ExceptLgr e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					br.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		return true;
	}

	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public boolean importationArticlesShopEvasion() {
		try
		{
//			BufferedReader br = new BufferedReader(new InputStreamReader(  
//					new FileInputStream("C:\\Users\\isabelle\\AppData\\Local\\Temp\\bdg\\demo\\2018-09-28_15-15-50_1550_Shopevasion-article.csv"), "ISO-8859-1"));
			BufferedReader br = new BufferedReader(fichierImportArticles);
			int nb=0;
    		String ligne = br.readLine();//j'ignore la première ligne entête
    		ligne = br.readLine();
    		String[] retour = null;
    		int i =1;
			TaUnite u = daoUnite.findByCode("U");
    		while (ligne!=null && !ligne.trim().isEmpty()){
    			retour = ligne.split(";");
    			TaArticleDTO dto = new TaArticleDTO();
    			importation=new Importation();
    			
    			//remplir l'objet Articles
    			for(int j=0; j<retour.length; j++) {
    				if (!LibChaine.empty(retour[j]) && retour[j]!=null && !retour[j].equals("null") && !retour[j].isEmpty()) {    					
    	    			if(j==0 && retour[j]!=null)dto.setCodeArticle(retour[j].toUpperCase());
    	    			if(j==1)dto.setLibellecArticle(retour[j]);
    	    			if(j==2)dto.setCodeTva(retour[j]);
    	    			if(j==3 && retour[j]!=null)dto.setPrixPrix(LibConversion.stringToBigDecimal(retour[j].replaceAll(",", ".")));
    	    			if(j==4 && retour[j]!=null)dto.setPrixttcPrix(LibConversion.stringToBigDecimal(retour[j].replaceAll(",", ".")));			

    	    			if(j==5)importation.codeFamille=retour[j];
    	    			if(j==6)importation.codeFamille2=retour[j];
    	    			if(j==7)importation.codeFamille3=retour[j];
    	    			if(j==8)dto.setCodeMarqueArticle(retour[j]);

    	    			
    				}
    			}
    			TaArticle obj = null;
    			 obj=daoArticle.findByCode((dto.getCodeArticle()));
    			if(obj!=null) {
//    			obj.setCodeArticle(dto.getCodeArticle());
//    			obj.setLibellecArticle(dto.getLibellecArticle());
////    			obj.setLibellelArticle(dto.getLibellelArticle());
////    			obj.setNumcptArticle(dto.getNumcptArticle());
//    			obj.setActif(1);
////    			obj.setCommentaireArticle(dto.getCommentaireArticle());
////    			obj.setDiversArticle(dto.getDiversArticle());
////    			obj.setStockMinArticle(dto.getStockMinArticle());    			
//    			obj.setProduitFini(false);
//    			obj.setMatierePremiere(false);
//    			obj.setGestionLot(false);
////    			obj.setCodeBarre(dto.getCodeBarre());
////    			obj.setParamDluo(dto.getParamDluo());
//    			obj.setUtiliseDlc(false);
//    			obj.setAutoAlimenteFournisseurs(false);
    				
    				
    			obj.setTaFamille(null);
    			obj.getTaFamilles().clear();
    			
    			if(importation.getCodeFamille()!=null && !importation.getCodeFamille().isEmpty()) {
    				TaFamille fam1=daoFamille.findByLibelle(importation.getCodeFamille());
    				if(fam1==null) {
    					System.out.println("famille 1 non trouvée :"+importation.getCodeFamille());
    				}
    				obj.getTaFamilles().add(fam1);
    				obj.setTaFamille(fam1);
    			}

    			if(importation.getCodeFamille2()!=null && !importation.getCodeFamille2().isEmpty()) {
    				TaFamille fam2=daoFamille.findByLibelle(importation.getCodeFamille2());
    				if(fam2==null) {
    					System.out.println("famille 2 non trouvée :"+importation.getCodeFamille2());
    				}
    				obj.getTaFamilles().add(fam2);
    			}
    			
    			if(importation.getCodeFamille3()!=null && !importation.getCodeFamille3().isEmpty()) {
    				TaFamille fam3=daoFamille.findByLibelle(importation.getCodeFamille3());
    				if(fam3==null) {
    					System.out.println("famille 3 non trouvée :"+importation.getCodeFamille3());
    				}
    				obj.getTaFamilles().add(fam3);
    			}

    			obj.setTaUnite1(u);
    			obj.setTaUniteReference(u);
//    			//prix de reference
//    			if(dto.getPrixPrix()!=null || dto.getPrixttcPrix()!=null) {
//    				TaPrix prix=new TaPrix();
//    				obj.setTaPrix(prix);
//    				prix.setPrixPrix(dto.getPrixPrix());
//    				prix.setPrixttcPrix(dto.getPrixttcPrix());
//    				prix.setTaArticle(obj);
//
//    				obj.addPrix(prix);    				
// 
//    			}
//
//
//    			
//
//    				TaTTva tTva=daoTTva.findByCode("D");
//    				if(tTva==null) {
//    					tTva=new TaTTva();
//    					tTva.setCodeTTva(dto.getCodeTTva());
//    					tTva.setLibTTva(dto.getCodeTTva());
//    					tTva=daoTTva.merge(tTva);
//    				}
//					obj.setTaTTva(tTva);
//    			
//    			
//    			if(dto.getCodeTva()!=null ) {
//    				TaTva tva=daoTva.findByCode(dto.getCodeTva());
//    				if(tva==null) {
//    					tva=new TaTva();
//    					tva.setCodeTva(dto.getCodeTva());
//    					tva.setLibelleTva(dto.getCodeTva());
//    					if(dto.getNumcptTva()==null || dto.getNumcptTva().isEmpty())tva.setNumcptTva("445713");
//    					else tva.setNumcptTva(dto.getNumcptTva());
//    					tva.setTauxTva(dto.getTauxTva());
//    					tva=daoTva.merge(tva);
//    				}
//					obj.setTaTva(tva);
//    			}
//    			
//    			if(dto.getCodeMarqueArticle()!=null && !dto.getCodeMarqueArticle().isEmpty()) {
//    				TaMarqueArticle m =daoMarque.findByCode(dto.getCodeMarqueArticle());
//    				if(m==null) {
//    					m=new TaMarqueArticle();
//    					m.setCodeMarqueArticle(dto.getCodeMarqueArticle());
//    					m.setLibelleMarqueArticle(dto.getCodeMarqueArticle());
//    					m=daoMarque.merge(m);
//    				}
//    				obj.setTaMarqueArticle(m);
//    			}
 
    			obj=daoArticle.merge(obj);
    			}else {
    				System.out.println("pas dans le fichier : " + dto.getCodeArticle());
    			}
    			ligne = br.readLine();
    		}
    		br.close();
    		System.out.println("nb : " + nb);
		}
			catch(Exception ioe){
				System.out.println("erreur : " + ioe);
			}
		return true;
	}
	
	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public boolean importationTiers(int deb,int fin) {
		BufferedReader br = null;
		boolean rentree=false;
		int i =1;
		int depart=0;
		try
		{
			br = new BufferedReader(new InputStreamReader(  
					new FileInputStream("C:\\LGRDOSS\\BureauDeGestion\\ImportTiers.csv"), "ISO-8859-1"));

//			br = new BufferedReader(fichierImportTiers);

			
    		String ligne = br.readLine();//j'ignore la première ligne entête
    		ligne = br.readLine();
    		depart=1;
    		String[] retour = null;
    		while (depart<deb){
    			ligne = br.readLine();
    			depart++;
    		}

    		while (ligne!=null && !ligne.trim().isEmpty() && depart<=fin ){
    			rentree=true;
    			retour = ligne.split(";");
    			TaTiersDTO dto = new TaTiersDTO();
    			TaAdresseDTO adrPrincipale = new TaAdresseDTO();
   			TaAdresseDTO adrFact = new TaAdresseDTO();
   			TaAdresseDTO adrLiv = new TaAdresseDTO();
   			TaInfoJuridiqueDTO infos = new TaInfoJuridiqueDTO();
   			TaCompteBanqueDTO banque1 = new TaCompteBanqueDTO();
   			TaTelephoneDTO tel2 = new TaTelephoneDTO();
   			TaEmailDTO email2 = new TaEmailDTO();
   			TaWebDTO web2 = new TaWebDTO();
   			TaFamilleTiersDTO fam1=new TaFamilleTiersDTO();
   			TaTTarifDTO tTarif = new TaTTarifDTO();
   			String supp1 = null;
   			String supp2= null;
   			TaTelephoneDTO tel3 = new TaTelephoneDTO();
   			
    			
    			//remplir l'objet Tiers
    			for(int j=0; j<retour.length; j++) {
    				if (!LibChaine.empty(retour[j]) && retour[j]!=null && !retour[j].equals("null")) {    					
    					if(j==0)dto.setCodeTiers(retour[j]);
    					if(j==1)dto.setNomTiers(retour[j]);
    					if(j==2)dto.setPrenomTiers(retour[j]);
    					if(j==3)dto.setCodeTEntite(retour[j]);
    					if(j==4)dto.setLiblTEntite(retour[j]);
    					if(j==5)dto.setCodeTTiers(retour[j]);
    					if(j==6)dto.setNomEntreprise(retour[j]);
    					if(j==7)dto.setCodeTCivilite(retour[j]);
    					if(j==8)dto.setTvaIComCompl(retour[j]);
    					if(j==9)dto.setAccise(retour[j]);
    					if(j==10)infos.setApeInfoJuridique(retour[j]);
    					if(j==11)infos.setCapitalInfoJuridique(retour[j]);
    					if(j==12)infos.setRcsInfoJuridique(retour[j]);
    					if(j==13)infos.setSiretInfoJuridique(retour[j]);
    					if(j==14)dto.setCodeFamilleTiers(retour[j]);
    					if(j==15)dto.setLibelleFamilleTiers(retour[j]);
    					if(j==16)dto.setCodeCompta(retour[j]);    					
						if(j==17)dto.setCompte(retour[j]);
						if(j==18)adrPrincipale.setAdresse1Adresse(retour[j]);
						if(j==19)adrPrincipale.setAdresse2Adresse(retour[j]);
						if(j==20)adrPrincipale.setAdresse3Adresse(retour[j]);
						if(j==21)adrPrincipale.setCodepostalAdresse(retour[j]);
						if(j==22)adrPrincipale.setVilleAdresse(retour[j]);
						if(j==23)adrPrincipale.setPaysAdresse(retour[j]);
						if(j==24)adrPrincipale.setCodeTAdr(retour[j]);
						if(j==25)dto.setNumeroTelephone(retour[j]);
						if(j==26)dto.setAdresseEmail(retour[j]);
						if(j==27)dto.setAdresseWeb(retour[j]);
						if(j==28)dto.setTtcTiers(LibConversion.StringToBoolean(retour[j]));
						
						if(j==29)dto.setCodeCPaiement(retour[j]);
						if(j==30)dto.setLibCPaiement(retour[j]);
						if(j==31)dto.setReportCPaiement(LibConversion.stringToInteger(retour[j]));
						if(j==32)dto.setFinMoisCPaiement(LibConversion.stringToInteger(retour[j]));
						
						if(j==33)dto.setCodeTPaiement(retour[j]);
						if(j==34)dto.setLibTPaiement(retour[j]);
						if(j==35)dto.setCompteTPaiement(retour[j]);
						if(j==36)dto.setReportTPaiement(LibConversion.stringToInteger(retour[j]));
						if(j==37)dto.setFinMoisTPaiement(LibConversion.stringToInteger(retour[j]));
						
						if(j==38)dto.setCodeTTvaDoc(retour[j]);
						if(j==39)dto.setCommentaire(retour[j]);
						if(j==40)dto.setActifTiers(LibConversion.StringToBoolean(retour[j]));
						if(j==41)adrFact.setAdresse1Adresse(retour[j]);
						if(j==42)adrFact.setAdresse2Adresse(retour[j]);
						if(j==43)adrFact.setAdresse3Adresse(retour[j]);
						if(j==44)adrFact.setCodepostalAdresse(retour[j]);
						if(j==45)adrFact.setVilleAdresse(retour[j]);
						if(j==46)adrFact.setPaysAdresse(retour[j]);
						if(j==47)adrFact.setCodeTAdr(retour[j]);
						if(j==48)adrLiv.setAdresse1Adresse(retour[j]);
						if(j==49)adrLiv.setAdresse2Adresse(retour[j]);
						if(j==50)adrLiv.setAdresse3Adresse(retour[j]);
						if(j==51)adrLiv.setCodepostalAdresse(retour[j]);
						if(j==52)adrLiv.setVilleAdresse(retour[j]);
						if(j==53)adrLiv.setPaysAdresse(retour[j]);
						if(j==54)adrLiv.setCodeTAdr(retour[j]);
						if(j==55)tel2.setNumeroTelephone(retour[j]);
						if(j==56)tel2.setCodeTTel(retour[j]);
						if(j==57)email2.setAdresseEmail(retour[j]);
						if(j==58)email2.setCodeTEmail(retour[j]);
						if(j==59)web2.setAdresseWeb(retour[j]);
						if(j==60)web2.setCodeTWeb(retour[j]);
						if(j==61)banque1.setNomBanque(retour[j]);
						if(j==62)banque1.setAdresse1Banque(retour[j]);
						if(j==63)banque1.setAdresse2Banque(retour[j]);
						if(j==64)banque1.setCpBanque(retour[j]);
						if(j==65)banque1.setVilleBanque(retour[j]);
						if(j==66)banque1.setCodeBanque(retour[j]);
						if(j==67)banque1.setCodeGuichet(retour[j]);							
						if(j==68)banque1.setTitulaire(retour[j]);
						if(j==69)banque1.setCompte(retour[j]);
						if(j==70)banque1.setCleRib(retour[j]);
						if(j==71)banque1.setIban(retour[j]);
						if(j==72)banque1.setCodeBIC(retour[j]);
						if(j==73)banque1.setCptcomptable(retour[j]);
						if(j==74)banque1.setCodeTBanque(retour[j]);
						if(j==75)
							fam1.setCodeFamille(retour[j]);
						if(j==76)fam1.setLibcFamille(retour[j]);
						if(j==77)tTarif.setCodeTTarif(retour[j]);
						if(j==78)tTarif.setLiblTTarif(retour[j]);
						
						
//						if(j==79)tel3.setNumeroTelephone(retour[j]);
//						if(j==80)tel3.setCodeTTel(retour[j]);

//						ObjectMapper mapper = new ObjectMapper();
//						champDiversDTO divers = new champDiversDTO();							
//						
//    	    			if(dto.getImportationDivers()== null)dto.setImportationDivers("");
//    	    			if(dto.getCommentaire()== null)dto.setCommentaire("");
//
//    	    			
//						if(j==81 && retour[j]!=null && !retour[j].equals("")) {
//							divers.setNomChamp("Intervenant");
//							divers.setValeur(retour[j]);
//							//Object to JSON in String
//							dto.setImportationDivers(dto.getImportationDivers()+ mapper.writeValueAsString(divers));
//							dto.setCommentaire(dto.getCommentaire()+"\r\n"+ divers.getNomChamp()+" : "+retour[j]);
//						}
//						if(j==82 && retour[j]!=null && !retour[j].equals("")) {
//							divers.setNomChamp("NPièce");
//							divers.setValeur(retour[j]);
//							//Object to JSON in String
//							dto.setImportationDivers(dto.getImportationDivers()+ mapper.writeValueAsString(divers));
//							dto.setCommentaire(dto.getCommentaire()+"\r\n"+ divers.getNomChamp()+" : "+retour[j]);
//						}
//
//						if(j==83 && retour[j]!=null && !retour[j].equals("")) {
//							divers.setNomChamp("sexe");
//							divers.setValeur(retour[j]);
//							//Object to JSON in String
//							dto.setImportationDivers(dto.getImportationDivers()+ mapper.writeValueAsString(divers));
//							dto.setCommentaire(dto.getCommentaire()+"\r\n"+ divers.getNomChamp()+" : "+retour[j]);
//						}
//						if(j==84 && retour[j]!=null && !retour[j].equals("")) {
//							divers.setNomChamp("Tutoiement");
//							divers.setValeur(retour[j]);
//							//Object to JSON in String
//							dto.setImportationDivers(dto.getImportationDivers()+ mapper.writeValueAsString(divers));
//							dto.setCommentaire(dto.getCommentaire()+"\r\n"+ divers.getNomChamp()+" : "+retour[j]);
//						}
//						if(j==85 && retour[j]!=null && !retour[j].equals("")) {
//							divers.setNomChamp("lattention");
//							divers.setValeur(retour[j]);
//							//Object to JSON in String
//							dto.setImportationDivers(dto.getImportationDivers()+ mapper.writeValueAsString(divers));
//							dto.setCommentaire(dto.getCommentaire()+"\r\n"+ divers.getNomChamp()+" : "+retour[j]);
//						}
//						if(j==86 && retour[j]!=null && !retour[j].equals("")) {
//							divers.setNomChamp("cher");
//							divers.setValeur(retour[j]);
//							//Object to JSON in String
//							dto.setImportationDivers(dto.getImportationDivers()+ mapper.writeValueAsString(divers));
//							dto.setCommentaire(dto.getCommentaire()+"\r\n"+ divers.getNomChamp()+" : "+retour[j]);
//						}
//						if(j==87 && retour[j]!=null && !retour[j].equals("")) {
//							divers.setNomChamp("cloture");
//							divers.setValeur(retour[j]);
//							//Object to JSON in String
//							dto.setImportationDivers(dto.getImportationDivers()+ mapper.writeValueAsString(divers));
//							dto.setCommentaire(dto.getCommentaire()+"\r\n"+ divers.getNomChamp()+" : "+retour[j]);
//						}
//						if(j==88 && retour[j]!=null && !retour[j].equals("")) {
//							divers.setNomChamp("dossier");
//							divers.setValeur(retour[j]);
//							//Object to JSON in String
//							dto.setImportationDivers(dto.getImportationDivers()+ mapper.writeValueAsString(divers));
//							dto.setCommentaire(dto.getCommentaire()+"\r\n"+ divers.getNomChamp()+" : "+retour[j]);
//						}
//						if(j==89 && retour[j]!=null && !retour[j].equals("")) {
//							divers.setNomChamp("horaires");
//							divers.setValeur(retour[j]);
//							//Object to JSON in String
//							dto.setImportationDivers(dto.getImportationDivers()+ mapper.writeValueAsString(divers));
//							dto.setCommentaire(dto.getCommentaire()+"\r\n"+ divers.getNomChamp()+" : "+retour[j]);
//						}
//
//						if(j==90 && retour[j]!=null && !retour[j].equals("")) {
//							divers.setNomChamp("message");
//							divers.setValeur(retour[j]);
//							//Object to JSON in String
//							dto.setImportationDivers(dto.getImportationDivers()+ mapper.writeValueAsString(divers));
//							dto.setCommentaire(dto.getCommentaire()+"\r\n"+ divers.getNomChamp()+" : "+retour[j]);
//						}
//						if(j==91 && retour[j]!=null && !retour[j].equals("")) {
//							divers.setNomChamp("heureDAppel");
//							divers.setValeur(retour[j]);
//							//Object to JSON in String
//							dto.setImportationDivers(dto.getImportationDivers()+ mapper.writeValueAsString(divers));
//							dto.setCommentaire(dto.getCommentaire()+"\r\n"+ divers.getNomChamp()+" : "+retour[j]);
//						}
//						if(j==92 && retour[j]!=null && !retour[j].equals("")) {
//							divers.setNomChamp("heurePAppel");
//							divers.setValeur(retour[j]);
//							//Object to JSON in String
//							dto.setImportationDivers(dto.getImportationDivers()+ mapper.writeValueAsString(divers));
//							dto.setCommentaire(dto.getCommentaire()+"\r\n"+ divers.getNomChamp()+" : "+retour[j]);
//						}
//						if(j==93 && retour[j]!=null && !retour[j].equals("")) {
//							divers.setNomChamp("activite");
//							divers.setValeur(retour[j]);
//							//Object to JSON in String
//							dto.setImportationDivers(dto.getImportationDivers()+ mapper.writeValueAsString(divers));
//							dto.setCommentaire(dto.getCommentaire()+"\r\n"+ divers.getNomChamp()+" : "+retour[j]);
//						}
//						if(j==94 && retour[j]!=null && !retour[j].equals("")) {
//							divers.setNomChamp("intervCode");
//							divers.setValeur(retour[j]);
//							//Object to JSON in String
//							dto.setImportationDivers(dto.getImportationDivers()+ mapper.writeValueAsString(divers));
//							dto.setCommentaire(dto.getCommentaire()+"\r\n"+ divers.getNomChamp()+" : "+retour[j]);
//						}
//						if(j==95 && retour[j]!=null && !retour[j].equals("")) {
//							divers.setNomChamp("Selection");
//							divers.setValeur(retour[j]);
//							//Object to JSON in String
//							dto.setImportationDivers(dto.getImportationDivers()+ mapper.writeValueAsString(divers));
//							dto.setCommentaire(dto.getCommentaire()+"\r\n"+ divers.getNomChamp()+" : "+retour[j]);
//						}
//						if(j==96 && retour[j]!=null && !retour[j].equals("")) {
//							divers.setNomChamp("Civilite");
//							divers.setValeur(retour[j]);
//							//Object to JSON in String
//							dto.setImportationDivers(dto.getImportationDivers()+ mapper.writeValueAsString(divers));
//							dto.setCommentaire(dto.getCommentaire()+"\r\n"+ divers.getNomChamp()+" : "+retour[j]);
//						}
//						if(j==97 && retour[j]!=null && !retour[j].equals("")) {
//							divers.setNomChamp("Telex");
//							divers.setValeur(retour[j]);
//							//Object to JSON in String
//							dto.setImportationDivers(dto.getImportationDivers()+ mapper.writeValueAsString(divers));
//							dto.setCommentaire(dto.getCommentaire()+"\r\n"+ divers.getNomChamp()+" : "+retour[j]);
//						}
//						if(j==98 && retour[j]!=null && !retour[j].equals("")) {
//							divers.setNomChamp("Statut");
//							divers.setValeur(retour[j]);
//							//Object to JSON in String
//							dto.setImportationDivers(dto.getImportationDivers()+ mapper.writeValueAsString(divers));
//							dto.setCommentaire(dto.getCommentaire()+"\r\n"+ divers.getNomChamp()+" : "+retour[j]);
//						}
//						if(j==99 && retour[j]!=null && !retour[j].equals("")) {
//							divers.setNomChamp("Categorie");
//							divers.setValeur(retour[j]);
//							//Object to JSON in String
//							dto.setImportationDivers(dto.getImportationDivers()+ mapper.writeValueAsString(divers));
//							dto.setCommentaire(dto.getCommentaire()+"\r\n"+ divers.getNomChamp()+" : "+retour[j]);
//						}

   				}
    			}
//    			if(dto.getCodeTiers().equals("ALBERC")) {
    			System.out.println(dto.getCodeTiers());
    			TaTiers obj = null;
    			boolean exist=daoTiers.exist((dto.getCodeTiers()));
//    			boolean exist=false;
//    			if(exist==true) {
//    				obj=daoTiers.findByCode((dto.getCodeTiers()));
//    				obj.setImportationDivers(dto.getImportationDivers());
//    				obj=daoTiers.merge(obj);
//    			}
    			if(exist==false) {
    				obj = new TaTiers();
    			if(dto.getCodeCompta()!=null) {
    				int nb =dto.getCodeCompta().length();
    				if(nb>7)nb=7;
    				dto.setCodeCompta(dto.getCodeCompta().substring(0,nb));
    			}
    			obj.setCodeTiers(dto.getCodeTiers());   			
				obj.setNomTiers(dto.getNomTiers());   			
				obj.setPrenomTiers(dto.getPrenomTiers());
				obj.setCodeCompta(dto.getCodeCompta());    					
				obj.setCompte(dto.getCompte());
				obj.setTtcTiers(LibConversion.booleanToInt(dto.getTtcTiers()));
				obj.setActifTiers(LibConversion.booleanToInt(dto.getActifTiers()));

				if(dto.getCodeTEntite()!=null && !dto.getCodeTEntite().isEmpty()) {					
					TaTEntite a =daoEntite.findByCode(dto.getCodeTEntite().toUpperCase());
					if(a==null) {
						 a =new TaTEntite();
					a.setCodeTEntite(dto.getCodeTEntite().toUpperCase());
					a.setLiblTEntite(dto.getLiblTEntite());
					if(a.getLiblTEntite()==null)a.setLiblTEntite(a.getCodeTEntite());
					a=daoEntite.merge(a);
					}
					obj.setTaTEntite(a);
				}
  			
				if(dto.getCodeTTiers()!=null && !dto.getCodeTTiers().isEmpty()) {					
					TaTTiers a =daoTTiers.findByCode(dto.getCodeTTiers());
					if(a==null) {
						 a =new TaTTiers();
					a.setCodeTTiers(dto.getCodeTTiers());
					a.setLibelleTTiers(dto.getCodeTTiers());
					a.setCompteTTiers("411");
					a=daoTTiers.merge(a);
					}
					obj.setTaTTiers(a);
				}
				
				if(dto.getNomEntreprise()!=null && !dto.getNomEntreprise().isEmpty()) {
					TaEntreprise a = new TaEntreprise();
					a.setNomEntreprise(dto.getNomEntreprise());
					obj.setTaEntreprise(a);
					obj.addEntreprise(a);
				}
	  			
					if(dto.getCodeTCivilite()!=null && !dto.getCodeTCivilite().isEmpty()) {					
						TaTCivilite a =daoTCivilite.findByCode(dto.getCodeTCivilite());
						if(a==null) {
							 a =new TaTCivilite();
						a.setCodeTCivilite(dto.getCodeTCivilite());
						a=daoTCivilite.merge(a);
						}
						obj.setTaTCivilite(a);
					}
				
					
					if(dto.getTvaIComCompl()!=null && !dto.getTvaIComCompl().isEmpty()) {
						TaCompl a = new TaCompl();
						if(obj.getTaCompl()!=null)a=obj.getTaCompl();
						a.setTvaIComCompl(dto.getTvaIComCompl());
						obj.setTaCompl(a); 
					}
					
				
					
					if(dto.getAccise()!=null && !dto.getAccise().isEmpty()) {
						TaCompl a = new TaCompl();
						if(obj.getTaCompl()!=null)a=obj.getTaCompl();
						a.setAccise(dto.getAccise());
						obj.setTaCompl(a); 
					}
					  			
					
					if(dto.getSiretCompl()!=null && !dto.getSiretCompl().isEmpty()) {
						TaCompl a = new TaCompl();
						if(obj.getTaCompl()!=null)a=obj.getTaCompl();
						a.setSiretCompl(dto.getSiretCompl());
						obj.setTaCompl(a); 
					}   			
					
					if(infos!=null && !infos.estVide()) {
						TaInfoJuridique a = new TaInfoJuridique();
						if(obj.getTaInfoJuridique()!=null)a=obj.getTaInfoJuridique();
						a.setSiretInfoJuridique(infos.getSiretInfoJuridique());
						a.setApeInfoJuridique(infos.getApeInfoJuridique());
						a.setCapitalInfoJuridique(infos.getCapitalInfoJuridique());
						a.setRcsInfoJuridique(infos.getRcsInfoJuridique());
						obj.setTaInfoJuridique(a); 
					} 
					
					
					if(dto.getCodeFamilleTiers()!=null && !dto.getCodeFamilleTiers().isEmpty()) {
	    				TaFamilleTiers fam=daoFamilleTiers.findByCode(dto.getCodeFamilleTiers());
	    				if(fam==null) {
	    				fam=new TaFamilleTiers();
	    				fam.setCodeFamille(dto.getCodeFamilleTiers());
	    				fam.setLibcFamille(dto.getCodeFamilleTiers());
	    				fam=daoFamilleTiers.merge(fam);
	    				fam=daoFamilleTiers.findByCode(fam.getCodeFamille());
	    				}
	    				obj.setTaFamilleTiers(fam);
	    			}

					
					if(dto.getCodeCPaiement()!=null && !dto.getCodeCPaiement().isEmpty()) {
						TaCPaiement a = daoCPaiement.findByCode(dto.getCodeCPaiement());
						if(a==null) {
							a=new TaCPaiement();
						if(obj.getTaCPaiement()!=null)a=obj.getTaCPaiement();
						a.setCodeCPaiement(dto.getCodeCPaiement());
						a.setLibCPaiement(dto.getCodeCPaiement());
						a.setLibCPaiement(dto.getLibCPaiement());
						a.setReportCPaiement(dto.getReportCPaiement());
						a.setFinMoisCPaiement(dto.getFinMoisCPaiement());
						a.setTaTCPaiement(daoTCPaiement.findById(1));
						a=daoCPaiement.merge(a);
					}
						obj.setTaCPaiement(a); 
					}

					
					if(dto.getCodeTPaiement()!=null && !dto.getCodeTPaiement().isEmpty()) {
						TaTPaiement a = daoTPaiement.findByCode(dto.getCodeTPaiement());
						if(a==null) {
							a=new TaTPaiement();
						if(obj.getTaTPaiement()!=null)a=obj.getTaTPaiement();
						a.setCodeTPaiement(dto.getCodeTPaiement());
						a.setLibTPaiement(dto.getLibTPaiement());
						a.setCompte(dto.getCompteTPaiement());
						a.setReportTPaiement(dto.getReportTPaiement());
						a.setFinMoisTPaiement(dto.getFinMoisTPaiement());
						a=daoTPaiement.merge(a);
					}
						obj.setTaTPaiement(a); 
					}
					
					if(dto.getCodeTTvaDoc()!=null && !dto.getCodeTTvaDoc().isEmpty()) {
						TaTTvaDoc a = daoTTvaDoc.findByCode(dto.getCodeTTvaDoc());
						if(a==null) {
							a=new TaTTvaDoc();
						if(obj.getTaTTvaDoc()!=null)a=obj.getTaTTvaDoc();
						a.setCodeTTvaDoc(dto.getCodeTTvaDoc());
						a.setLibelleTTvaDoc(dto.getCodeTTvaDoc());
						a=daoTTvaDoc.merge(a);
						}
						obj.setTaTTvaDoc(a); 
					}				
					
					
					if(adrPrincipale!=null && !adrPrincipale.estVide()) {
						TaAdresse a = new TaAdresse();
						if(obj.getTaAdresse()!=null)a=obj.getTaAdresse();
						a.setAdresse1Adresse(adrPrincipale.getAdresse1Adresse());
						a.setAdresse2Adresse(adrPrincipale.getAdresse2Adresse());
						a.setAdresse3Adresse(adrPrincipale.getAdresse3Adresse());
						a.setCodepostalAdresse(adrPrincipale.getCodepostalAdresse());
						a.setVilleAdresse(adrPrincipale.getVilleAdresse());
						a.setPaysAdresse(adrPrincipale.getPaysAdresse());
						if(adrPrincipale.getCodeTAdr()!=null && !adrPrincipale.getCodeTAdr().isEmpty()) {
							TaTAdr b = daoTAdr.findByCode(adrPrincipale.getCodeTAdr());
							if(b==null) {
								b=new TaTAdr();
								b.setCodeTAdr(adrPrincipale.getCodeTAdr());
								b.setLiblTAdr(adrPrincipale.getCodeTAdr());
								b=daoTAdr.merge(b);
							}
							a.setTaTAdr(b);
						}
						a.setTaTiers(obj);
						obj.setTaAdresse(a); 
						obj.addAdresse(a);
					} 

					if(dto.getNumeroTelephone()!=null && !dto.getNumeroTelephone().isEmpty()) {
						TaTelephone a = new TaTelephone();
						if(obj.getTaTelephone()!=null)a=obj.getTaTelephone();
						a.setNumeroTelephone(dto.getNumeroTelephone());
						a.setTaTiers(obj);
						obj.setTaTelephone(a); 
						obj.addTelephone(a);
					} 

					if(dto.getAdresseEmail()!=null && !dto.getAdresseEmail().isEmpty()) {
						TaEmail a = new TaEmail();
						if(obj.getTaEmail()!=null)a=obj.getTaEmail();
						a.setAdresseEmail(dto.getAdresseEmail());
						a.setTaTiers(obj);
						obj.setTaEmail(a); 
						obj.addEmail(a);
					} 	

					if(dto.getAdresseWeb()!=null && !dto.getAdresseWeb().isEmpty()) {
						TaWeb a = new TaWeb();
						if(obj.getTaWeb()!=null)a=obj.getTaWeb();
						a.setAdresseWeb(dto.getAdresseWeb());
						a.setTaTiers(obj);
						obj.setTaWeb(a);
						obj.addWeb(a);
					} 
					

					
					if((dto.getCommentaire()!=null && !dto.getCommentaire().isEmpty())||(supp1!=null)) {
						TaCommentaire a = new TaCommentaire();
						if(obj.getTaCommentaire()!=null)a=obj.getTaCommentaire();
						a.setCommentaire(dto.getCommentaire().replace("§", "\r\n"));
						if(supp1!=null)a.setCommentaire(dto.getCommentaire()+supp1);
						obj.setTaCommentaire(a); 
					}
					
					if(adrFact!=null && !adrFact.estVide()) {
						TaAdresse a = new TaAdresse();
						a.setAdresse1Adresse(adrFact.getAdresse1Adresse());
						a.setAdresse2Adresse(adrFact.getAdresse2Adresse());
						a.setAdresse3Adresse(adrFact.getAdresse3Adresse());
						a.setCodepostalAdresse(adrFact.getCodepostalAdresse());
						a.setVilleAdresse(adrFact.getVilleAdresse());
						a.setPaysAdresse(adrFact.getPaysAdresse());
						if(adrFact.getCodeTAdr()!=null && !adrFact.getCodeTAdr().isEmpty()) {
							TaTAdr b = daoTAdr.findByCode(adrFact.getCodeTAdr());
							if(b==null) {
								b=new TaTAdr();
								b.setCodeTAdr(adrFact.getCodeTAdr());
								b.setLiblTAdr(adrFact.getCodeTAdr());
								b=daoTAdr.merge(b);
							}
							a.setTaTAdr(b);
						}						
						obj.addAdresse(a);
						a.setTaTiers(obj);
					}
					
					if(adrLiv!=null && !adrLiv.estVide()) {
						TaAdresse a = new TaAdresse();
						a.setAdresse1Adresse(adrLiv.getAdresse1Adresse());
						a.setAdresse2Adresse(adrLiv.getAdresse2Adresse());
						a.setAdresse3Adresse(adrLiv.getAdresse3Adresse());
						a.setCodepostalAdresse(adrLiv.getCodepostalAdresse());
						a.setVilleAdresse(adrLiv.getVilleAdresse());
						a.setPaysAdresse(adrLiv.getPaysAdresse());
						if(adrLiv.getCodeTAdr()==null)adrLiv.setCodeTAdr("LIV");
						if(adrLiv.getCodeTAdr()!=null && !adrLiv.getCodeTAdr().isEmpty()) {
							TaTAdr b = daoTAdr.findByCode(adrLiv.getCodeTAdr());
							if(b==null) {
								b=new TaTAdr();
								b.setCodeTAdr(adrLiv.getCodeTAdr());
								b.setLiblTAdr(adrLiv.getCodeTAdr());
								b=daoTAdr.merge(b);
							}
							a.setTaTAdr(b);
						}
						obj.addAdresse(a);
						a.setTaTiers(obj);
					}

					

					if(tel2!=null && !tel2.estVide()) {
						TaTelephone a = new TaTelephone();
						a.setNumeroTelephone(tel2.getNumeroTelephone());
						if(tel2.getCodeTTel()!=null && !tel2.getCodeTTel().isEmpty()) {
							TaTTel b=daoTTel.findByCode(tel2.getCodeTTel());	
							if(b==null) {
								b=new TaTTel();
								b.setCodeTTel(tel2.getCodeTTel());
								b.setLiblTTel(tel2.getCodeTTel());
								b=daoTTel.merge(b);
							}
							a.setTaTTel(b);
						}
						a.setTaTiers(obj);
						obj.addTelephone(a);
					} 

					if(email2!=null && !email2.estVide()) {
						TaEmail a = new TaEmail();
						a.setAdresseEmail(email2.getAdresseEmail());
						if(email2.getCodeTEmail()!=null && !email2.getCodeTEmail().isEmpty()) {
							TaTEmail b=daoTEmail.findByCode(email2.getCodeTEmail());
							if(b==null) {
								b=new TaTEmail();
								b.setCodeTEmail(email2.getCodeTEmail());
								b.setLiblTEmail(email2.getCodeTEmail());
								b=daoTEmail.merge(b);
							}
							a.setTaTEmail(b);
						}						
						a.setTaTiers(obj);
						obj.addEmail(a);
					} 	

					if(web2!=null && !web2.estVide()) {
						TaWeb a = new TaWeb();
						a.setAdresseWeb(web2.getAdresseWeb());
						if(web2.getCodeTWeb()!=null && !web2.getCodeTWeb().isEmpty()) {
							TaTWeb b=daoTWeb.findByCode(web2.getCodeTWeb());							
							if(b==null) {
								b=new TaTWeb();
								b.setCodeTWeb(web2.getCodeTWeb());
								b.setLiblTWeb(web2.getCodeTWeb());
								b=daoTWeb.merge(b);
							}
							a.setTaTWeb(b);
						}							
						a.setTaTiers(obj);
						obj.addWeb(a);
					} 
					
					

					if(tel3!=null && !tel3.estVide()) {
						TaTelephone a = new TaTelephone();
						a.setNumeroTelephone(tel3.getNumeroTelephone());
						if(tel3.getCodeTTel()!=null && !tel3.getCodeTTel().isEmpty()) {
							TaTTel b=daoTTel.findByCode(tel3.getCodeTTel());	
							if(b==null) {
								b=new TaTTel();
								b.setCodeTTel(tel3.getCodeTTel());
								b.setLiblTTel(tel3.getCodeTTel());
								b=daoTTel.merge(b);
							}
							a.setTaTTel(b);
						}
						a.setTaTiers(obj);
						obj.addTelephone(a);
					}
					
					
					if(banque1!=null && !banque1.estVide()) {
						TaCompteBanque a = new TaCompteBanque();
						a.setNomBanque(banque1.getNomBanque());
						a.setAdresse1Banque(banque1.getAdresse1Banque());
						a.setAdresse2Banque(banque1.getAdresse2Banque());
						a.setCpBanque(banque1.getCpBanque());
						a.setVilleBanque(banque1.getVilleBanque());
						a.setCodeBanque(banque1.getCodeBanque());
						a.setCodeGuichet(banque1.getCodeGuichet());
						a.setTitulaire(banque1.getTitulaire());
						a.setCompte(banque1.getCompte());
						a.setCleRib(banque1.getCleRib());
						a.setIban(banque1.getIban());
						a.setCodeBIC(banque1.getCodeBIC());
						a.setCptcomptable(banque1.getCptcomptable());
						if(banque1.getCodeTBanque()!=null && !banque1.getCodeTBanque().isEmpty()) {
							TaTBanque b=daoTBanque.findByCode(banque1.getCodeTBanque());	
							if(b==null) {
								b=new TaTBanque();
								b.setCodeTBanque(banque1.getCodeTBanque());
								b.setLiblTBanque(banque1.getCodeTBanque());
								b=daoTBanque.merge(b);
							}
							a.setTaTBanque(b);
						}
						if(a.getNomCompte()==null ||a.getNomCompte().isEmpty())
							a.setNomCompte("compte_"+a.getCodeBanque());
						obj.addCompteBanque(a);
						a.setTaTiers(obj);
					}

					
					if(fam1!=null && !fam1.estVide()) {
	    				TaFamilleTiers fam=daoFamilleTiers.findByCode(fam1.getCodeFamille());
	    				if(fam==null) {
	    				fam=new TaFamilleTiers();
						fam.setCodeFamille(fam1.getCodeFamille());
						fam.setLibcFamille(fam1.getLibcFamille());
	    				fam=daoFamilleTiers.merge(fam);
	    				fam=daoFamilleTiers.findByCode(fam.getCodeFamille());
	    				}
						obj.addFamilleTiers(fam);
					} 

					
					if(tTarif!=null && !tTarif.estVide()) {
						TaTTarif a = daoTTarif.findByCode(tTarif.getCodeTTarif());
						if(a==null) {
							a=new TaTTarif();
						a.setCodeTTarif(tTarif.getCodeTTarif());
						a.setLiblTTarif(tTarif.getLiblTTarif());
						a.setPourcentage(0);
						a.setSens(0);
//						a.setValeur(tTarif.getValeur());
						a=daoTTarif.merge(a);
						}
						obj.setTaTTarif(a);
					} 


    			
    			obj=daoTiers.merge(obj);
    			}
    			//}
    			ligne = br.readLine();
    			depart++;
    		}
    		br.close();
		}
			catch(IOException ioe){				
				System.out.println("erreur : " + ioe);
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		return rentree;
	}
	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public boolean importationArticlesSpecifique() {
		try
		{
//			BufferedReader br = new BufferedReader(new InputStreamReader(  
//					new FileInputStream("C:\\Users\\isabelle\\ownCloud\\developpement\\Transfert_cloud\\EnAttente\\coeffard/T_Article.csv"), "ISO-8859-1"));
			BufferedReader br = new BufferedReader(fichierImportArticles);
			int nb=0;
    		String ligne = br.readLine();//j'ignore la première ligne entête
    		ligne = br.readLine();
    		String[] retour = null;
    		int i =1;
			TaUnite u = daoUnite.findByCode("U");
    		while (ligne!=null && !ligne.trim().isEmpty()){
    			retour = ligne.split(";");
    			TaArticleDTO dto = new TaArticleDTO();
    			importation=new Importation();
    			
    			//remplir l'objet Articles
    			for(int j=0; j<retour.length; j++) {
    				if (!LibChaine.empty(retour[j]) && retour[j]!=null && !retour[j].equals("null") && !retour[j].isEmpty()) {    					
    	    			if(j==0 && retour[j]!=null)dto.setCodeArticle(retour[j].toUpperCase());    	    			
    	    			if(j==1)dto.setLibellecArticle(retour[j]);
    	    			if(j==2 && retour[j]!=null)dto.setPrixPrix(LibConversion.stringToBigDecimal(retour[j].replaceAll(" ", "").replaceAll(",", "."))); 	    			
    	    			if(j==3)dto.setCodeTva(retour[j]);
    	    			if(j==4)importation.codeFamille=retour[j];
    				}
    			}
    			TaArticle obj = null;
    			boolean exist=daoArticle.exist((dto.getCodeArticle()));
    			if(exist==false) {
    				obj = new TaArticle();
        			obj.setCodeArticle(dto.getCodeArticle());
        			obj.setLibellecArticle(dto.getLibellecArticle());
        			obj.setLibellelArticle(dto.getLibellelArticle());
        			obj.setActif(1);
        			obj.setStockMinArticle(BigDecimal.ZERO);    			
        			obj.setProduitFini(false);
        			obj.setMatierePremiere(false);
        			obj.setGestionLot(false);
        			obj.setUtiliseDlc(false);        			
        			
    			if(importation.getCodeFamille()!=null && !importation.getCodeFamille().isEmpty()) {
    				TaFamille fam1=daoFamille.findByCode(importation.getCodeFamille());
    				if(fam1==null) {
    					System.out.println("famille 1 non trouvée :"+importation.getCodeFamille());
    				}
    				obj.getTaFamilles().add(fam1);
    				obj.setTaFamille(fam1);
    			}

    			if(dto.getCodeTva()!=null ) {
    				TaTva tva=daoTva.findByCode(dto.getCodeTva());
    				if(tva==null) {
    					tva=new TaTva();
    					tva.setCodeTva(dto.getCodeTva());
    					tva.setLibelleTva(dto.getCodeTva());
    					if(dto.getNumcptTva()==null || dto.getNumcptTva().isEmpty())tva.setNumcptTva("445713");
    					else tva.setNumcptTva(dto.getNumcptTva());
    					tva.setTauxTva(dto.getTauxTva());
    					tva=daoTva.merge(tva);
    				}
					obj.setTaTva(tva);
    			}

    			obj.setTaUnite1(u);
    			obj.setTaUniteReference(u);
    			//prix de reference
    			if(dto.getPrixPrix()!=null || dto.getPrixttcPrix()!=null) {
    				TaPrix prix=new TaPrix();
    				obj.setTaPrix(prix);
    				prix.setTaArticle(obj);
    				prix.setPrixPrix(dto.getPrixPrix());
    				prix.setPrixttcPrix(dto.getPrixPrix());
    				prix.majPrix();
    				prix.setPrixttcPrix(prix.getPrixttcPrix());

    				obj.addPrix(prix);    				
 
    			}


    			

    				TaTTva tTva=daoTTva.findByCode("D");
    				if(tTva==null) {
    					tTva=new TaTTva();
    					tTva.setCodeTTva(dto.getCodeTTva());
    					tTva.setLibTTva(dto.getCodeTTva());
    					tTva=daoTTva.merge(tTva);
    				}
					obj.setTaTTva(tTva);
    			
    			

    			

 
    			obj=daoArticle.merge(obj);
    			}else {
    				System.out.println("pas dans le fichier : " + dto.getCodeArticle());
    			}
    			ligne = br.readLine();
    		}
    		br.close();
    		System.out.println("nb : " + nb);
		}
			catch(Exception ioe){
				System.out.println("erreur : " + ioe);
			}
		return true;
	}
	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public boolean importationArticlesShopEvasion2() {
		try
		{
//			BufferedReader br = new BufferedReader(new InputStreamReader(  
//					new FileInputStream("C:\\Users\\isabelle\\AppData\\Local\\Temp\\bdg\\demo\\2018-09-28_15-15-50_1550_Shopevasion-article.csv"), "ISO-8859-1"));
			BufferedReader br = new BufferedReader(fichierImportArticles);
			int nb=0;
    		String ligne = br.readLine();//j'ignore la première ligne entête
    		ligne = br.readLine();
    		String[] retour = null;
    		int i =1;
			TaUnite u = daoUnite.findByCode("U");
    		while (ligne!=null && !ligne.trim().isEmpty()){
    			retour = ligne.split(";");
    			TaArticleDTO dto = new TaArticleDTO();
    			importation=new Importation();
    			
    			//remplir l'objet Articles
    			for(int j=0; j<retour.length; j++) {
    				if (!LibChaine.empty(retour[j]) && retour[j]!=null && !retour[j].equals("null") && !retour[j].isEmpty()) {    					
    	    			if(j==0 && retour[j]!=null)dto.setCodeArticle(retour[j].toUpperCase());
    	    			if(j==1)dto.setLibellecArticle(retour[j]);
    	    			if(j==2)dto.setCodeTva(retour[j]);
    	    			if(j==3 && retour[j]!=null)dto.setPrixPrix(LibConversion.stringToBigDecimal(retour[j].replaceAll(",", ".")));
    	    			if(j==4 && retour[j]!=null)dto.setPrixttcPrix(LibConversion.stringToBigDecimal(retour[j].replaceAll(",", ".")));			

    	    			if(j==5)importation.codeFamille=retour[j];
    	    			if(j==6)importation.codeFamille2=retour[j];
    	    			if(j==7)importation.codeFamille3=retour[j];
    	    			if(j==8)dto.setCodeMarqueArticle(retour[j]);

    	    			
    				}
    			}
    			TaArticle obj = null;
    			boolean exist=daoArticle.exist((dto.getCodeArticle()));
    			if(exist==false) {
    				obj = new TaArticle();
    			obj.setCodeArticle(dto.getCodeArticle());
    			obj.setLibellecArticle(dto.getLibellecArticle());
//    			obj.setLibellelArticle(dto.getLibellelArticle());
//    			obj.setNumcptArticle(dto.getNumcptArticle());
    			obj.setActif(1);
//    			obj.setCommentaireArticle(dto.getCommentaireArticle());
//    			obj.setDiversArticle(dto.getDiversArticle());
//    			obj.setStockMinArticle(dto.getStockMinArticle());    			
    			obj.setProduitFini(false);
    			obj.setMatierePremiere(false);
    			obj.setGestionLot(false);
//    			obj.setCodeBarre(dto.getCodeBarre());
//    			obj.setParamDluo(dto.getParamDluo());
    			obj.setUtiliseDlc(false);
    			obj.setAutoAlimenteFournisseurs(false);

    			
    			if(importation.getCodeFamille()!=null && !importation.getCodeFamille().isEmpty()) {
    				TaFamille fam1=daoFamille.findByLibelle(importation.getCodeFamille());
    				if(fam1==null) {
    					System.out.println("famille 1 non trouvée :"+importation.getCodeFamille());
    				}
    				obj.getTaFamilles().add(fam1);
    				obj.setTaFamille(fam1);
    			}

    			if(importation.getCodeFamille2()!=null && !importation.getCodeFamille2().isEmpty()) {
    				TaFamille fam2=daoFamille.findByLibelle(importation.getCodeFamille2());
    				if(fam2==null) {
    					System.out.println("famille 2 non trouvée :"+importation.getCodeFamille2());
    				}
    				obj.getTaFamilles().add(fam2);
    			}
    			
    			if(importation.getCodeFamille3()!=null && !importation.getCodeFamille3().isEmpty()) {
    				TaFamille fam3=daoFamille.findByLibelle(importation.getCodeFamille3());
    				if(fam3==null) {
    					System.out.println("famille 3 non trouvée :"+importation.getCodeFamille3());
    				}
    				obj.getTaFamilles().add(fam3);
    			}

    			obj.setTaUnite1(u);
    			obj.setTaUniteReference(u);
    			//prix de reference
    			if(dto.getPrixPrix()!=null || dto.getPrixttcPrix()!=null) {
    				TaPrix prix=new TaPrix();
    				obj.setTaPrix(prix);
    				prix.setPrixPrix(dto.getPrixPrix());
    				prix.setPrixttcPrix(dto.getPrixttcPrix());
    				prix.setTaArticle(obj);

    				obj.addPrix(prix);    				
 
    			}


    			

    				TaTTva tTva=daoTTva.findByCode("D");
    				if(tTva==null) {
    					tTva=new TaTTva();
    					tTva.setCodeTTva(dto.getCodeTTva());
    					tTva.setLibTTva(dto.getCodeTTva());
    					tTva=daoTTva.merge(tTva);
    				}
					obj.setTaTTva(tTva);
    			
    			
    			if(dto.getCodeTva()!=null ) {
    				TaTva tva=daoTva.findByCode(dto.getCodeTva());
    				if(tva==null) {
    					tva=new TaTva();
    					tva.setCodeTva(dto.getCodeTva());
    					tva.setLibelleTva(dto.getCodeTva());
    					if(dto.getNumcptTva()==null || dto.getNumcptTva().isEmpty())tva.setNumcptTva("445713");
    					else tva.setNumcptTva(dto.getNumcptTva());
    					tva.setTauxTva(dto.getTauxTva());
    					tva=daoTva.merge(tva);
    				}
					obj.setTaTva(tva);
    			}
    			
    			if(dto.getCodeMarqueArticle()!=null && !dto.getCodeMarqueArticle().isEmpty()) {
    				TaMarqueArticle m =daoMarque.findByCode(dto.getCodeMarqueArticle());
    				if(m==null) {
    					m=new TaMarqueArticle();
    					m.setCodeMarqueArticle(dto.getCodeMarqueArticle());
    					m.setLibelleMarqueArticle(dto.getCodeMarqueArticle());
    					m=daoMarque.merge(m);
    				}
    				obj.setTaMarqueArticle(m);
    			}
 
    			obj=daoArticle.merge(obj);
    			}else {
    				System.out.println("doublon : " + dto.getCodeArticle());
    			}
    			ligne = br.readLine();
    		}
    		br.close();
    		System.out.println("nb : " + nb);
		}
			catch(Exception ioe){
				System.out.println("erreur : " + ioe);
			}
		return true;
	}
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public boolean importationTiersSpecifique() {
		try
		{
//			BufferedReader br = new BufferedReader(new InputStreamReader(  
//					new FileInputStream("C:\\Users\\isabelle\\ownCloud\\developpement\\Transfert_cloud\\EnAttente\\coeffard/Client.csv"), "UTF8"));
			BufferedReader br = new BufferedReader(fichierImportTiers);

    		String ligne = br.readLine();//j'ignore la première ligne entête
    		ligne = br.readLine();
    		String[] retour = null;
    		int i =1;
    		while (ligne!=null && !ligne.isEmpty()){
    			retour = ligne.split(";");
    			TaTiersDTO dto = new TaTiersDTO();
    			TaAdresseDTO adrPrincipale = new TaAdresseDTO();
//   			TaAdresseDTO adrFact = new TaAdresseDTO();
//   			TaAdresseDTO adrLiv = new TaAdresseDTO();
//   			TaInfoJuridiqueDTO infos = new TaInfoJuridiqueDTO();
//   			TaCompteBanqueDTO banque1 = new TaCompteBanqueDTO();
   			TaTelephoneDTO tel2 = new TaTelephoneDTO();
//   			TaEmailDTO email2 = new TaEmailDTO();
//   			TaWebDTO web2 = new TaWebDTO();
//   			TaFamilleTiersDTO fam1=new TaFamilleTiersDTO();
//   			TaTTarifDTO tTarif = new TaTTarifDTO();
   			TaTCiviliteDTO civ = new TaTCiviliteDTO();
   			TaCommentaireDTO com = new TaCommentaireDTO();
   			
   			
   			
   			
    			
    			//remplir l'objet Tiers
    			for(int j=0; j<retour.length; j++) {
    				if (!LibChaine.empty(retour[j]) && retour[j]!=null && !retour[j].equals("null")) {    					
    					if(j==0)dto.setCodeTiers(retour[j]);
    					if(j==1)dto.setNomTiers(retour[j]);
    					if(j==2)dto.setPrenomTiers(retour[j]);
    					if(j==3)dto.setCodeTCivilite(retour[j]);
						if(j==4)adrPrincipale.setAdresse1Adresse(retour[j]);
						if(j==5)adrPrincipale.setAdresse2Adresse(retour[j]);
						if(j==6)adrPrincipale.setAdresse3Adresse(retour[j]);
						if(j==7)adrPrincipale.setCodepostalAdresse(retour[j]);
						if(j==8)adrPrincipale.setVilleAdresse(retour[j]);
						if(j==9)dto.setNumeroTelephone(retour[j]);
						if(j==10)tel2.setNumeroTelephone(retour[j]);
						if(j==11)dto.setAdresseEmail(retour[j]);

						if(j==12)dto.setCommentaire(retour[j]);
						if(j==13)dto.setCommentaire(dto.getCommentaire()+"\r\n"+ retour[j]);
						if(j==14)dto.setCommentaire(dto.getCommentaire()+"\r\n"+ retour[j]);
					
    				}
    			}
    			System.out.println(dto.getCodeTiers());
    			TaTiers obj = null;
    			boolean exist=daoTiers.exist((dto.getCodeTiers()));
    			if(exist==false) {
    				obj = new TaTiers();

    			obj.setCodeTiers(dto.getCodeTiers());   			
				obj.setNomTiers(dto.getNomTiers());   			
				obj.setPrenomTiers(dto.getPrenomTiers());
				obj.setCodeCompta(dto.getCodeTiers());    					
				obj.setCompte("411");
				obj.setTtcTiers(0);
				obj.setActifTiers(1);


				dto.setCodeTTiers("C");
				if(dto.getCodeTTiers()!=null && !dto.getCodeTTiers().isEmpty()) {					
					TaTTiers a =daoTTiers.findByCode(dto.getCodeTTiers());
					if(a==null) {
						 a =new TaTTiers();
					a.setCodeTTiers(dto.getCodeTTiers());
					a.setLibelleTTiers(dto.getCodeTTiers());
					a.setCompteTTiers("411");
					a=daoTTiers.merge(a);
					}
					obj.setTaTTiers(a);
				}

	  			
					if(dto.getCodeTCivilite()!=null && !dto.getCodeTCivilite().isEmpty()) {					
						TaTCivilite a =daoTCivilite.findByCode(dto.getCodeTCivilite());
						if(a==null) {
							 a =new TaTCivilite();
						a.setCodeTCivilite(dto.getCodeTCivilite());
						a=daoTCivilite.merge(a);
						}
						obj.setTaTCivilite(a);
					}
				

					
					dto.setCodeTTvaDoc("F");
					if(dto.getCodeTTvaDoc()!=null && !dto.getCodeTTvaDoc().isEmpty()) {
						TaTTvaDoc a = daoTTvaDoc.findByCode(dto.getCodeTTvaDoc());
						if(a==null) {
							a=new TaTTvaDoc();
						if(obj.getTaTTvaDoc()!=null)a=obj.getTaTTvaDoc();
						a.setCodeTTvaDoc(dto.getCodeTTvaDoc());
						a.setLibelleTTvaDoc(dto.getCodeTTvaDoc());
						a=daoTTvaDoc.merge(a);
						}
						obj.setTaTTvaDoc(a); 
					}				
					
					adrPrincipale.setCodeTAdr("FACT");
					if(adrPrincipale!=null && !adrPrincipale.estVide()) {
						TaAdresse a = new TaAdresse();
						if(obj.getTaAdresse()!=null)a=obj.getTaAdresse();
						a.setAdresse1Adresse(adrPrincipale.getAdresse1Adresse());
						a.setAdresse2Adresse(adrPrincipale.getAdresse2Adresse());
						a.setAdresse3Adresse(adrPrincipale.getAdresse3Adresse());
						a.setCodepostalAdresse(adrPrincipale.getCodepostalAdresse());
						a.setVilleAdresse(adrPrincipale.getVilleAdresse());
						a.setPaysAdresse("France");
						if(adrPrincipale.getCodeTAdr()!=null && !adrPrincipale.getCodeTAdr().isEmpty()) {
							TaTAdr b = daoTAdr.findByCode(adrPrincipale.getCodeTAdr());
							if(b==null) {
								b=new TaTAdr();
								b.setCodeTAdr(adrPrincipale.getCodeTAdr());
								b.setLiblTAdr(adrPrincipale.getCodeTAdr());
								b=daoTAdr.merge(b);
							}
							a.setTaTAdr(b);
						}
						a.setTaTiers(obj);
						obj.setTaAdresse(a); 
						obj.addAdresse(a);
					} 

					if(dto.getNumeroTelephone()!=null && !dto.getNumeroTelephone().isEmpty()) {
						TaTelephone a = new TaTelephone();
						if(obj.getTaTelephone()!=null)a=obj.getTaTelephone();
						a.setNumeroTelephone(dto.getNumeroTelephone());
						a.setTaTiers(obj);
						obj.setTaTelephone(a); 
						obj.addTelephone(a);
					} 

					if(dto.getAdresseEmail()!=null && !dto.getAdresseEmail().isEmpty()) {
						TaEmail a = new TaEmail();
						if(obj.getTaEmail()!=null)a=obj.getTaEmail();
						a.setAdresseEmail(dto.getAdresseEmail());
						a.setTaTiers(obj);
						obj.setTaEmail(a); 
						obj.addEmail(a);
					} 	

		

					
					if(dto.getCommentaire()!=null && !dto.getCommentaire().isEmpty()) {
						TaCommentaire a = new TaCommentaire();
						if(obj.getTaCommentaire()!=null)a=obj.getTaCommentaire();
						a.setCommentaire(dto.getCommentaire());
						obj.setTaCommentaire(a); 
					}
					

					if(tel2!=null && !tel2.estVide()) {
						TaTelephone a = new TaTelephone();
						a.setNumeroTelephone(tel2.getNumeroTelephone());
						tel2.setCodeTTel("P");
						if(tel2.getCodeTTel()!=null && !tel2.getCodeTTel().isEmpty()) {
							TaTTel b=daoTTel.findByCode(tel2.getCodeTTel());	
							if(b==null) {
								b=new TaTTel();
								b.setCodeTTel(tel2.getCodeTTel());
								b.setLiblTTel(tel2.getCodeTTel());
								b=daoTTel.merge(b);
							}
							a.setTaTTel(b);
						}
						a.setTaTiers(obj);
						obj.addTelephone(a);
					} 



					System.out.println("tiers : " + obj.getCodeTiers());
    			obj=daoTiers.merge(obj);
    			}
    			ligne = br.readLine();
    		}
    		br.close();
		}
			catch(IOException ioe){
				System.out.println("erreur : " + ioe);
			}
		return true;
	}

	public FileReader getFichierImportTiers() {
		return fichierImportTiers;
	}

	public void setFichierImportTiers(String fichierImportTiers) {
		try {
			this.fichierImportTiers = new FileReader(fichierImportTiers);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public FileReader getFichierImportArticles() {
		return fichierImportArticles;
	}

	public void setFichierImportArticles(String fichierImportArticles) {
		try {
			this.fichierImportArticles = new FileReader(fichierImportArticles);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setFichierImportAbonnements(String fichierImportAbonnements) {
		try {
			this.fichierImportAbonnements = new FileReader(fichierImportAbonnements);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setFichierImportBonReception(String fichierImportBonReception) {
		try {
			this.fichierImportBonReception = new FileReader(fichierImportBonReception);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public boolean importationTiersCapdebon() {
		try
		{
//			BufferedReader br = new BufferedReader(new InputStreamReader(  
//					new FileInputStream("C:\\Users\\isabelle\\ownCloud\\developpement\\Transfert_cloud\\EnAttente\\Capdepon\\clients.csv"), "ISO-8859-1"));

			BufferedReader br = new BufferedReader(fichierImportTiers);

    		String ligne = br.readLine();//j'ignore la première ligne entête
    		ligne = br.readLine();
    		String[] retour = null;
    		int i =1;
    		while (ligne!=null && !ligne.isEmpty() ){
    			retour = ligne.split(";");
    			TaTiersDTO dto = new TaTiersDTO();
    			TaAdresseDTO adrPrincipale = new TaAdresseDTO();
   			TaAdresseDTO adrFact = new TaAdresseDTO();
   			TaAdresseDTO adrLiv = new TaAdresseDTO();
   			TaInfoJuridiqueDTO infos = new TaInfoJuridiqueDTO();
   			TaCompteBanqueDTO banque1 = new TaCompteBanqueDTO();
   			TaTelephoneDTO tel2 = new TaTelephoneDTO();
   			TaTelephoneDTO tel3 = new TaTelephoneDTO();
   			TaTelephoneDTO tel4 = new TaTelephoneDTO();
   			TaTelephoneDTO tel5 = new TaTelephoneDTO();
   			TaTelephoneDTO tel6 = new TaTelephoneDTO();
   			TaEmailDTO email2 = new TaEmailDTO();
   			TaWebDTO web2 = new TaWebDTO();
   			TaFamilleTiersDTO fam1=new TaFamilleTiersDTO();
   			TaTTarifDTO tTarif = new TaTTarifDTO();
   			
   			dto.setCommentaire("");
   			
    			
    			//remplir l'objet Tiers
    			for(int j=0; j<retour.length; j++) {
    				if (!LibChaine.empty(retour[j]) && retour[j]!=null && !retour[j].equals("null")) {    					
    					if(j==1)dto.setCodeTiers(retour[j]);
    					if(j==2)dto.setNomTiers(retour[j]);
//    					if(j==2)dto.setPrenomTiers(retour[j]);
//    					if(j==3)dto.setCodeTEntite(retour[j]);
//    					if(j==4)dto.setLiblTEntite(retour[j]);
    					if(j==3)dto.setCodeTTiers(retour[j]);
    					if(j==4)dto.setNomEntreprise(retour[j]);
    					if(j==5)dto.setCodeTCivilite(retour[j]);
    					if(j==6)dto.setTvaIComCompl(retour[j]);
//    					if(j==9)dto.setAccise(retour[j]);
//    					if(j==10)infos.setApeInfoJuridique(retour[j]);
//    					if(j==11)infos.setCapitalInfoJuridique(retour[j]);
//    					if(j==12)infos.setRcsInfoJuridique(retour[j]);
    					if(j==7)infos.setSiretInfoJuridique(retour[j]);
    					if(j==8)dto.setCodeFamilleTiers(retour[j]);
//    					if(j==15)dto.setLibelleFamilleTiers(retour[j]);
    					if(j==9)dto.setCodeCompta(retour[j]);    					
//						if(j==9)dto.setCompte(retour[j]);
						if(j==10)adrPrincipale.setAdresse1Adresse(retour[j]);
						if(j==11)adrPrincipale.setAdresse2Adresse(retour[j]);
						if(j==12)adrPrincipale.setAdresse3Adresse(retour[j]);
						if(j==13)adrPrincipale.setCodepostalAdresse(retour[j]);
						if(j==14)adrPrincipale.setVilleAdresse(retour[j]);
						if(j==15)adrPrincipale.setPaysAdresse(retour[j]);
						if(j==16)adrPrincipale.setCodeTAdr(retour[j]);
						if(j==17)dto.setNumeroTelephone(retour[j]);
						if(j==18)dto.setAdresseEmail(retour[j]);
						if(j==19)dto.setAdresseWeb(retour[j]);
						if(j==20)dto.setTtcTiers(LibConversion.StringToBoolean(retour[j]));
						
						if(j==21)dto.setCodeCPaiement(retour[j]);
						if(j==22)dto.setLibCPaiement(retour[j]);
//						if(j==31)dto.setReportCPaiement(LibConversion.stringToInteger(retour[j]));
//						if(j==32)dto.setFinMoisCPaiement(LibConversion.stringToInteger(retour[j]));
						
//						if(j==33)dto.setCodeTPaiement(retour[j]);
//						if(j==34)dto.setLibTPaiement(retour[j]);
//						if(j==35)dto.setCompteTPaiement(retour[j]);
//						if(j==36)dto.setReportTPaiement(LibConversion.stringToInteger(retour[j]));
//						if(j==37)dto.setFinMoisTPaiement(LibConversion.stringToInteger(retour[j]));
						
						if(j==23)dto.setCodeTTvaDoc(retour[j].toUpperCase());
//						if(j==39)dto.setCommentaire(retour[j]);
//						if(j==40)dto.setActifTiers(LibConversion.StringToBoolean(retour[j]));
						if(j==24)adrFact.setAdresse1Adresse(retour[j]);
						if(j==25)adrFact.setAdresse2Adresse(retour[j]);
						if(j==26)adrFact.setAdresse3Adresse(retour[j]);
						if(j==27)adrFact.setCodepostalAdresse(retour[j]);
						if(j==28)adrFact.setVilleAdresse(retour[j]);
						if(j==29)adrFact.setPaysAdresse(retour[j]);
						if(j==30)adrFact.setCodeTAdr(retour[j].toUpperCase());
						if(j==31)adrLiv.setAdresse1Adresse(retour[j]);
						if(j==32)adrLiv.setAdresse2Adresse(retour[j]);
						if(j==33)adrLiv.setAdresse3Adresse(retour[j]);
						if(j==34)adrLiv.setCodepostalAdresse(retour[j]);
						if(j==35)adrLiv.setVilleAdresse(retour[j]);
						if(j==36)adrLiv.setPaysAdresse(retour[j]);
						if(j==37)adrLiv.setCodeTAdr(retour[j].toUpperCase());
						if(j==38)tel2.setNumeroTelephone(retour[j]);
						if(j==39)tel2.setCodeTTel(retour[j].toUpperCase());
						if(j==40)tel3.setNumeroTelephone(retour[j]);
						if(j==41)tel3.setCodeTTel(retour[j].toUpperCase());
						if(j==42)tel4.setNumeroTelephone(retour[j]);
						if(j==43)tel4.setCodeTTel(retour[j].toUpperCase());
						if(j==44)tel5.setNumeroTelephone(retour[j]);
						if(j==45)tel5.setCodeTTel(retour[j].toUpperCase());
						if(j==46)tel6.setNumeroTelephone(retour[j]);
						if(j==47)tel6.setCodeTTel(retour[j].toUpperCase());
//						if(j==57)email2.setAdresseEmail(retour[j]);
//						if(j==58)email2.setCodeTEmail(retour[j]);
//						if(j==59)web2.setAdresseWeb(retour[j]);
//						if(j==60)web2.setCodeTWeb(retour[j]);
//						if(j==61)banque1.setNomBanque(retour[j]);
//						if(j==62)banque1.setAdresse1Banque(retour[j]);
//						if(j==63)banque1.setAdresse2Banque(retour[j]);
//						if(j==64)banque1.setCpBanque(retour[j]);
//						if(j==65)banque1.setVilleBanque(retour[j]);
//						if(j==66)banque1.setCodeBanque(retour[j]);
//						if(j==67)banque1.setCodeGuichet(retour[j]);							
//						if(j==68)banque1.setTitulaire(retour[j]);
//						if(j==69)banque1.setCompte(retour[j]);
//						if(j==70)banque1.setCleRib(retour[j]);
//						if(j==71)banque1.setIban(retour[j]);
//						if(j==72)banque1.setCodeBIC(retour[j]);
//						if(j==73)banque1.setCptcomptable(retour[j]);
//						if(j==74)banque1.setCodeTBanque(retour[j]);
//						if(j==75)fam1.setCodeFamille(retour[j]);
//						if(j==76)fam1.setLibcFamille(retour[j]);
						if(j==48)tTarif.setCodeTTarif(retour[j]);
						if(j==49)tTarif.setLiblTTarif(retour[j]);
						if(j==50)tTarif.setValeur(LibConversion.stringToBigDecimal(retour[j]));
						
						
						if(j==51 && retour[j]!=null && !retour[j].equals(""))dto.setCommentaire(dto.getCommentaire()+"\r\n"+"Observations : "+retour[j]);
						if(j==52 && retour[j]!=null && !retour[j].equals(""))dto.setCommentaire(dto.getCommentaire()+"\r\n"+"RelevéCompte : "+retour[j]);
						if(j==53 && retour[j]!=null && !retour[j].equals(""))dto.setCommentaire(dto.getCommentaire()+"\r\n"+"codeRepresentant : "+retour[j]);
						if(j==54 && retour[j]!=null && !retour[j].equals(""))dto.setCommentaire(dto.getCommentaire()+"\r\n"+"NomRepresentant : "+retour[j]);
						if(j==55 && retour[j]!=null && !retour[j].equals(""))dto.setCommentaire(dto.getCommentaire()+"\r\n"+"NombreExemplaireFacture : "+retour[j]);
						if(j==56 && retour[j]!=null && !retour[j].equals(""))dto.setCommentaire(dto.getCommentaire()+"\r\n"+"AccepterDaily : "+retour[j]);
						if(j==57 && retour[j]!=null && !retour[j].equals(""))dto.setCommentaire(dto.getCommentaire()+"\r\n"+"DateDernièreCommande : "+retour[j]);
						if(j==58 && retour[j]!=null && !retour[j].equals(""))dto.setCommentaire(dto.getCommentaire()+"\r\n"+"DateCreation : "+retour[j]);
						
						if(j==59)dto.setCodeTPaiement(retour[j]);

					
    				}
    			}
    			System.out.println(dto.getCodeTiers());
    			dto.setCompte("411");
    			TaTiers obj = null;
    			while (dto.getCodeTiers().length()<4) {
    				dto.setCodeTiers("0"+dto.getCodeTiers());					
				}
    			
    			boolean exist=daoTiers.exist((dto.getCodeTiers()));
    			if(exist==false) {
    				obj = new TaTiers();

    			obj.setCodeTiers(dto.getCodeTiers());   			
				obj.setNomTiers(dto.getNomTiers());   			
				obj.setPrenomTiers(dto.getPrenomTiers());
				obj.setCodeCompta(dto.getCodeCompta());    					
				obj.setCompte(dto.getCompte());
				obj.setTtcTiers(LibConversion.booleanToInt(dto.getTtcTiers()));
				obj.setActifTiers(LibConversion.booleanToInt(dto.getActifTiers()));

				if(dto.getCodeTEntite()!=null && !dto.getCodeTEntite().isEmpty()) {					
					TaTEntite a =daoEntite.findByCode(dto.getCodeTEntite());
					if(a==null) {
						 a =new TaTEntite();
					a.setCodeTEntite(dto.getCodeTEntite());
					a.setLiblTEntite(dto.getLiblTEntite());
					a=daoEntite.merge(a);
					}
					obj.setTaTEntite(a);
				}
  			
				if(dto.getCodeTTiers()!=null && !dto.getCodeTTiers().isEmpty()) {					
					TaTTiers a =daoTTiers.findByCode(dto.getCodeTTiers());
					if(a==null) {
						 a =new TaTTiers();
					a.setCodeTTiers(dto.getCodeTTiers());
					a.setLibelleTTiers(dto.getCodeTTiers());
					a.setCompteTTiers("411");
					a=daoTTiers.merge(a);
					}
					obj.setTaTTiers(a);
				}
				
				if(dto.getNomEntreprise()!=null && !dto.getNomEntreprise().isEmpty()) {
					TaEntreprise a = new TaEntreprise();
					a.setNomEntreprise(dto.getNomEntreprise());
					obj.setTaEntreprise(a);
					obj.addEntreprise(a);
				}
	  			
					if(dto.getCodeTCivilite()!=null && !dto.getCodeTCivilite().isEmpty()) {					
						TaTCivilite a =daoTCivilite.findByCode(dto.getCodeTCivilite());
						if(a==null) {
							 a =new TaTCivilite();
						a.setCodeTCivilite(dto.getCodeTCivilite());
						a=daoTCivilite.merge(a);
						}
						obj.setTaTCivilite(a);
					}
				
					
					if(dto.getTvaIComCompl()!=null && !dto.getTvaIComCompl().isEmpty()) {
						TaCompl a = new TaCompl();
						if(obj.getTaCompl()!=null)a=obj.getTaCompl();
						a.setTvaIComCompl(dto.getTvaIComCompl());
						obj.setTaCompl(a); 
					}
					
				
					
					if(dto.getAccise()!=null && !dto.getAccise().isEmpty()) {
						TaCompl a = new TaCompl();
						if(obj.getTaCompl()!=null)a=obj.getTaCompl();
						a.setAccise(dto.getAccise());
						obj.setTaCompl(a); 
					}
					  			
					
					if(dto.getSiretCompl()!=null && !dto.getSiretCompl().isEmpty()) {
						TaCompl a = new TaCompl();
						if(obj.getTaCompl()!=null)a=obj.getTaCompl();
						a.setSiretCompl(dto.getSiretCompl());
						obj.setTaCompl(a); 
					}   			
					
					if(infos!=null && !infos.estVide()) {
						TaInfoJuridique a = new TaInfoJuridique();
						if(obj.getTaInfoJuridique()!=null)a=obj.getTaInfoJuridique();
						a.setSiretInfoJuridique(infos.getSiretInfoJuridique());
						a.setApeInfoJuridique(infos.getApeInfoJuridique());
						a.setCapitalInfoJuridique(infos.getCapitalInfoJuridique());
						a.setRcsInfoJuridique(infos.getRcsInfoJuridique());
						obj.setTaInfoJuridique(a); 
					} 
					
					
					if(dto.getCodeFamilleTiers()!=null && !dto.getCodeFamilleTiers().isEmpty()) {
	    				TaFamilleTiers fam=daoFamilleTiers.findByCode(dto.getCodeFamilleTiers());
	    				if(fam==null) {
	    				fam=new TaFamilleTiers();
	    				fam.setCodeFamille(dto.getCodeFamilleTiers());
	    				fam.setLibcFamille(dto.getCodeFamilleTiers());
	    				fam=daoFamilleTiers.merge(fam);
	    				fam=daoFamilleTiers.findByCode(fam.getCodeFamille());
	    				}
	    				obj.setTaFamilleTiers(fam);
	    			}

					
					if(dto.getCodeCPaiement()!=null && !dto.getCodeCPaiement().isEmpty()) {
						TaCPaiement a = daoCPaiement.findByCode(dto.getCodeCPaiement());
						if(a==null) {
							a=new TaCPaiement();
						if(obj.getTaCPaiement()!=null)a=obj.getTaCPaiement();
						a.setCodeCPaiement(dto.getCodeCPaiement());
						a.setLibCPaiement(dto.getCodeCPaiement());
						a.setLibCPaiement(dto.getLibCPaiement());
						a.setReportCPaiement(dto.getReportCPaiement());
						a.setFinMoisCPaiement(dto.getFinMoisCPaiement());
						a.setTaTCPaiement(daoTCPaiement.findById(1));
						a=daoCPaiement.merge(a);
					}
						obj.setTaCPaiement(a); 
					}

					
					if(dto.getCodeTPaiement()!=null && !dto.getCodeTPaiement().isEmpty()) {
						TaTPaiement a = daoTPaiement.findByCode(dto.getCodeTPaiement());
						if(a==null) {
							a=new TaTPaiement();
						if(obj.getTaTPaiement()!=null)a=obj.getTaTPaiement();
						a.setCodeTPaiement(dto.getCodeTPaiement());
						a.setLibTPaiement(dto.getLibTPaiement());
						a.setCompte(dto.getCompteTPaiement());
						a.setReportTPaiement(dto.getReportTPaiement());
						a.setFinMoisTPaiement(dto.getFinMoisTPaiement());
						a=daoTPaiement.merge(a);
					}
						obj.setTaTPaiement(a); 
					}
					
					if(dto.getCodeTTvaDoc()!=null && !dto.getCodeTTvaDoc().isEmpty()) {
						TaTTvaDoc a = daoTTvaDoc.findByCode(dto.getCodeTTvaDoc());
						if(a==null) {
							a=new TaTTvaDoc();
						if(obj.getTaTTvaDoc()!=null)a=obj.getTaTTvaDoc();
						a.setCodeTTvaDoc(dto.getCodeTTvaDoc());
						a.setLibelleTTvaDoc(dto.getCodeTTvaDoc());
						a=daoTTvaDoc.merge(a);
						}
						obj.setTaTTvaDoc(a); 
					}				
					
					
					if(adrPrincipale!=null && !adrPrincipale.estVide()) {
						TaAdresse a = new TaAdresse();
						if(obj.getTaAdresse()!=null)a=obj.getTaAdresse();
						a.setAdresse1Adresse(adrPrincipale.getAdresse1Adresse());
						a.setAdresse2Adresse(adrPrincipale.getAdresse2Adresse());
						a.setAdresse3Adresse(adrPrincipale.getAdresse3Adresse());
						a.setCodepostalAdresse(adrPrincipale.getCodepostalAdresse());
						a.setVilleAdresse(adrPrincipale.getVilleAdresse());
						a.setPaysAdresse(adrPrincipale.getPaysAdresse());
						if(adrPrincipale.getCodeTAdr()!=null && !adrPrincipale.getCodeTAdr().isEmpty()) {
							TaTAdr b = daoTAdr.findByCode(adrPrincipale.getCodeTAdr());
							if(b==null) {
								b=new TaTAdr();
								b.setCodeTAdr(adrPrincipale.getCodeTAdr());
								b.setLiblTAdr(adrPrincipale.getCodeTAdr());
								b=daoTAdr.merge(b);
							}
							a.setTaTAdr(b);
						}
						a.setTaTiers(obj);
						obj.setTaAdresse(a); 
						obj.addAdresse(a);
					} 

					if(dto.getNumeroTelephone()!=null && !dto.getNumeroTelephone().isEmpty()) {
						TaTelephone a = new TaTelephone();
						if(obj.getTaTelephone()!=null)a=obj.getTaTelephone();
						a.setNumeroTelephone(dto.getNumeroTelephone());
						a.setTaTiers(obj);
						obj.setTaTelephone(a); 
						obj.addTelephone(a);
					} 

					if(dto.getAdresseEmail()!=null && !dto.getAdresseEmail().isEmpty()) {
						TaEmail a = new TaEmail();
						if(obj.getTaEmail()!=null)a=obj.getTaEmail();
						a.setAdresseEmail(dto.getAdresseEmail());
						a.setTaTiers(obj);
						obj.setTaEmail(a); 
						obj.addEmail(a);
					} 	

					if(dto.getAdresseWeb()!=null && !dto.getAdresseWeb().isEmpty()) {
						TaWeb a = new TaWeb();
						if(obj.getTaWeb()!=null)a=obj.getTaWeb();
						a.setAdresseWeb(dto.getAdresseWeb());
						a.setTaTiers(obj);
						obj.setTaWeb(a);
						obj.addWeb(a);
					} 
					

					
					if(dto.getCommentaire()!=null && !dto.getCommentaire().isEmpty()) {
						TaCommentaire a = new TaCommentaire();
						if(obj.getTaCommentaire()!=null)a=obj.getTaCommentaire();
						a.setCommentaire(dto.getCommentaire());
						obj.setTaCommentaire(a); 
					}
					
					if(adrFact!=null && !adrFact.estVide()) {
						TaAdresse a = new TaAdresse();
						a.setAdresse1Adresse(adrFact.getAdresse1Adresse());
						a.setAdresse2Adresse(adrFact.getAdresse2Adresse());
						a.setAdresse3Adresse(adrFact.getAdresse3Adresse());
						a.setCodepostalAdresse(adrFact.getCodepostalAdresse());
						a.setVilleAdresse(adrFact.getVilleAdresse());
						a.setPaysAdresse(adrFact.getPaysAdresse());
						if(adrFact.getCodeTAdr()!=null && !adrFact.getCodeTAdr().isEmpty()) {
							TaTAdr b = daoTAdr.findByCode(adrFact.getCodeTAdr());
							if(b==null) {
								b=new TaTAdr();
								b.setCodeTAdr(adrFact.getCodeTAdr());
								b.setLiblTAdr(adrFact.getCodeTAdr());
								b=daoTAdr.merge(b);
							}
							a.setTaTAdr(b);
						}						
						obj.addAdresse(a);
						a.setTaTiers(obj);
					}
					
					if(adrLiv!=null && !adrLiv.estVide()) {
						TaAdresse a = new TaAdresse();
						a.setAdresse1Adresse(adrLiv.getAdresse1Adresse());
						a.setAdresse2Adresse(adrLiv.getAdresse2Adresse());
						a.setAdresse3Adresse(adrLiv.getAdresse3Adresse());
						a.setCodepostalAdresse(adrLiv.getCodepostalAdresse());
						a.setVilleAdresse(adrLiv.getVilleAdresse());
						a.setPaysAdresse(adrLiv.getPaysAdresse());
						if(adrLiv.getCodeTAdr()!=null && !adrLiv.getCodeTAdr().isEmpty()) {
							TaTAdr b = daoTAdr.findByCode(adrLiv.getCodeTAdr());
							if(b==null) {
								b=new TaTAdr();
								b.setCodeTAdr(adrLiv.getCodeTAdr());
								b.setLiblTAdr(adrLiv.getCodeTAdr());
								b=daoTAdr.merge(b);
							}
							a.setTaTAdr(b);
						}
						obj.addAdresse(a);
						a.setTaTiers(obj);
					}

					

					if(tel2!=null && !tel2.estVide()) {
						TaTelephone a = new TaTelephone();
						a.setNumeroTelephone(tel2.getNumeroTelephone());
						if(tel2.getCodeTTel()!=null && !tel2.getCodeTTel().isEmpty()) {
							TaTTel b=daoTTel.findByCode(tel2.getCodeTTel());	
							if(b==null) {
								b=new TaTTel();
								b.setCodeTTel(tel2.getCodeTTel());
								b.setLiblTTel(tel2.getCodeTTel());
								b=daoTTel.merge(b);
							}
							a.setTaTTel(b);
						}
						a.setTaTiers(obj);
						obj.addTelephone(a);
					} 
					
					if(tel3!=null && !tel3.estVide()) {
						TaTelephone a = new TaTelephone();
						a.setNumeroTelephone(tel3.getNumeroTelephone());
						if(tel3.getCodeTTel()!=null && !tel3.getCodeTTel().isEmpty()) {
							TaTTel b=daoTTel.findByCode(tel3.getCodeTTel());	
							if(b==null) {
								b=new TaTTel();
								b.setCodeTTel(tel3.getCodeTTel());
								b.setLiblTTel(tel3.getCodeTTel());
								b=daoTTel.merge(b);
							}
							a.setTaTTel(b);
						}
						a.setTaTiers(obj);
						obj.addTelephone(a);
					} 
					
					if(tel4!=null && !tel4.estVide()) {
						TaTelephone a = new TaTelephone();
						a.setNumeroTelephone(tel4.getNumeroTelephone());
						if(tel4.getCodeTTel()!=null && !tel4.getCodeTTel().isEmpty()) {
							TaTTel b=daoTTel.findByCode(tel4.getCodeTTel());	
							if(b==null) {
								b=new TaTTel();
								b.setCodeTTel(tel4.getCodeTTel());
								b.setLiblTTel(tel4.getCodeTTel());
								b=daoTTel.merge(b);
							}
							a.setTaTTel(b);
						}
						a.setTaTiers(obj);
						obj.addTelephone(a);
					} 
					
					if(tel5!=null && !tel5.estVide()) {
						TaTelephone a = new TaTelephone();
						a.setNumeroTelephone(tel5.getNumeroTelephone());
						if(tel5.getCodeTTel()!=null && !tel5.getCodeTTel().isEmpty()) {
							TaTTel b=daoTTel.findByCode(tel5.getCodeTTel());	
							if(b==null) {
								b=new TaTTel();
								b.setCodeTTel(tel5.getCodeTTel());
								b.setLiblTTel(tel5.getCodeTTel());
								b=daoTTel.merge(b);
							}
							a.setTaTTel(b);
						}
						a.setTaTiers(obj);
						obj.addTelephone(a);
					} 
					
					if(tel6!=null && !tel6.estVide()) {
						TaTelephone a = new TaTelephone();
						a.setNumeroTelephone(tel6.getNumeroTelephone());
						if(tel6.getCodeTTel()!=null && !tel6.getCodeTTel().isEmpty()) {
							TaTTel b=daoTTel.findByCode(tel6.getCodeTTel());	
							if(b==null) {
								b=new TaTTel();
								b.setCodeTTel(tel6.getCodeTTel());
								b.setLiblTTel(tel6.getCodeTTel());
								b=daoTTel.merge(b);
							}
							a.setTaTTel(b);
						}
						a.setTaTiers(obj);
						obj.addTelephone(a);
					} 

					if(email2!=null && !email2.estVide()) {
						TaEmail a = new TaEmail();
						a.setAdresseEmail(email2.getAdresseEmail());
						if(email2.getCodeTEmail()!=null && !email2.getCodeTEmail().isEmpty()) {
							TaTEmail b=daoTEmail.findByCode(email2.getCodeTEmail());
							if(b==null) {
								b=new TaTEmail();
								b.setCodeTEmail(email2.getCodeTEmail());
								b.setLiblTEmail(email2.getCodeTEmail());
								b=daoTEmail.merge(b);
							}
							a.setTaTEmail(b);
						}						
						a.setTaTiers(obj);
						obj.addEmail(a);
					} 	

					if(web2!=null && !web2.estVide()) {
						TaWeb a = new TaWeb();
						a.setAdresseWeb(web2.getAdresseWeb());
						if(web2.getCodeTWeb()!=null && !web2.getCodeTWeb().isEmpty()) {
							TaTWeb b=daoTWeb.findByCode(web2.getCodeTWeb());							
							if(b==null) {
								b=new TaTWeb();
								b.setCodeTWeb(web2.getCodeTWeb());
								b.setLiblTWeb(web2.getCodeTWeb());
								b=daoTWeb.merge(b);
							}
							a.setTaTWeb(b);
						}							
						a.setTaTiers(obj);
						obj.addWeb(a);
					} 
					
					
					if(banque1!=null && !banque1.estVide()) {
						TaCompteBanque a = new TaCompteBanque();
						a.setNomBanque(banque1.getNomBanque());
						a.setAdresse1Banque(banque1.getAdresse1Banque());
						a.setAdresse2Banque(banque1.getAdresse2Banque());
						a.setCpBanque(banque1.getCpBanque());
						a.setVilleBanque(banque1.getVilleBanque());
						a.setCodeBanque(banque1.getCodeBanque());
						a.setCodeGuichet(banque1.getCodeGuichet());
						a.setTitulaire(banque1.getTitulaire());
						a.setCompte(banque1.getCompte());
						a.setCleRib(banque1.getCleRib());
						a.setIban(banque1.getIban());
						a.setCodeBIC(banque1.getCodeBIC());
						a.setCptcomptable(banque1.getCptcomptable());
						if(banque1.getCodeTBanque()!=null && !banque1.getCodeTBanque().isEmpty()) {
							TaTBanque b=daoTBanque.findByCode(banque1.getCodeTBanque());	
							if(b==null) {
								b=new TaTBanque();
								b.setCodeTBanque(banque1.getCodeTBanque());
								b.setLiblTBanque(banque1.getCodeTBanque());
								b=daoTBanque.merge(b);
							}
							a.setTaTBanque(b);
						}
						if(a.getNomCompte()==null ||a.getNomCompte().isEmpty())
							a.setNomCompte("compte_"+a.getCodeBanque());
						obj.addCompteBanque(a);
						a.setTaTiers(obj);
					}

					
					if(fam1!=null && !fam1.estVide()) {
	    				TaFamilleTiers fam=daoFamilleTiers.findByCode(fam1.getCodeFamille());
	    				if(fam==null) {
	    				fam=new TaFamilleTiers();
						fam.setCodeFamille(fam1.getCodeFamille());
						fam.setLibcFamille(fam1.getLibcFamille());
	    				}
						obj.addFamilleTiers(fam);
					} 

					
					if(tTarif!=null && !tTarif.estVide()) {
						TaTTarif a = daoTTarif.findByCode(tTarif.getCodeTTarif());
						if(a==null) {
							a=new TaTTarif();
						a.setCodeTTarif(tTarif.getCodeTTarif());
						a.setLiblTTarif(tTarif.getLiblTTarif());
						a.setPourcentage(0);
						a.setSens(0);
//						a.setValeur(tTarif.getValeur());
						}
						obj.setTaTTarif(a);
					} 


    			
    			obj=daoTiers.merge(obj);
    			}
    			ligne = br.readLine();
    		}
    		br.close();
		}
			catch(IOException ioe){
				System.out.println("erreur : " + ioe);
			}
		return true;
	}


	public FileReader getFichierImportBonReception() {
		return fichierImportBonReception;
	}



	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public boolean importationAbonnement() {
		String separateur=";";
		BufferedReader br = null;
		try
		{
			br = new BufferedReader(new InputStreamReader(  
					new FileInputStream("C:\\LGRDOSS\\BureauDeGestion\\listeAbonnement.csv"), "ISO-8859-1"));
//			BufferedReader br = new BufferedReader(fichierImportAbonnements);

    		String ligne = br.readLine();//j'ignore la première ligne num colonne
    		
    		//à partir de la troisième ligne
    		ligne = br.readLine();
    		String[] retour = null;
//    		String[] retourOld = null;
    		int i =1;
			TaAbonnement obj = new TaAbonnement();
			TaLAbonnement lBR;
    		//TaStripeSubscription stripeSubscription = new  TaStripeSubscription();
			String codeCourant;
			TaFrequence frequence = TaFrequenceService.findById(4);
			TaTPaiement tpaiement =TaTPaiementService.findByCode("C7");
			TaUnite uniteMois=TaUniteService.findById(5);
    		while (ligne!=null && !ligne.isEmpty()){
    			retour = ligne.split(separateur);
    			boolean enCours=LibConversion.stringToInteger(retour[4])<12;
    			Date dateDebut=LibDate.stringToDate(retour[7]);
    			dateDebut=LibDate.incrementDate(dateDebut, 0, 0, -1);

    				obj.setCodeDocument(retour[0]);
    				//changement de tiers et donc enregistrement du bon courant
//        			boolean exist=TaBonReceptionService.exist(obj.getCodeDocument());
        			boolean exist=false;
        			if(exist==false  && !retour[13].equals("PREL")) {
            		System.out.println(obj.getCodeDocument());
        				//remplir bon


            		TaTiers taTiers = taTiersService.findByCode(retour[0]);

            		
            		//remplir TaAbonnement
            		obj.setCodeDocument(TaAbonnementService.genereCode(null));
            		if(enCours)obj.setDateDocument(dateDebut);
            		else
            			obj.setDateDocument(new Date());
            		obj.setDateEchDocument(obj.getDateDocument());            		
            		obj.setDateLivDocument(obj.getDateDocument());
            		obj.setLibelleDocument("Maintenance logiciel 2020");
            		obj.setTaTiers(taTiers);
            		obj.setGestionLot(false);
					obj.setTaFrequence(frequence);
					obj.setTaTPaiement(tpaiement);	

					obj.setGestionTVA(true);
           		
            		//remplir infos document
            		TaInfosAbonnement taInfosDocument=new TaInfosAbonnement();
					obj.setTaInfosDocument(taInfosDocument);

            		taInfosDocument.setCodeTTvaDoc("F");
            		taInfosDocument.setTaDocument(obj);
            		taInfosDocument.setNomTiers(retour[1]);
            		taInfosDocument.setPrenomTiers(taTiers.getPrenomTiers());
					taInfosDocument.setSurnomTiers(taTiers.getSurnomTiers());
    				if(taTiers.getTaEntreprise()!=null)
    					taInfosDocument.setNomEntreprise(taTiers.getTaEntreprise().getNomEntreprise());

    				if (taTiers.getTaTTvaDoc()!=null){
    					taInfosDocument.setCodeTTvaDoc(taTiers.getTaTTvaDoc().getCodeTTvaDoc());  				

    					
    					taInfosDocument.setCodeCompta(taTiers.getCodeCompta());
    					taInfosDocument.setCompte(taTiers.getCompte());
    					obj.setNbDecimalesPrix(2);
    					obj.setNbDecimalesQte(2);

    					
    					//rajout yann
    					if(taTiers.getTaAdresse() != null) {
    						taInfosDocument.setAdresse1(taTiers.getTaAdresse().getAdresse1Adresse());
    						taInfosDocument.setAdresse2(taTiers.getTaAdresse().getAdresse2Adresse());
    						taInfosDocument.setAdresse3(taTiers.getTaAdresse().getAdresse3Adresse());
    						taInfosDocument.setAdresse1Liv(taTiers.getTaAdresse().getAdresse1Adresse());
    						taInfosDocument.setAdresse2Liv(taTiers.getTaAdresse().getAdresse2Adresse());
    						taInfosDocument.setAdresse3Liv(taTiers.getTaAdresse().getAdresse3Adresse());
    						
    						taInfosDocument.setCodepostal(taTiers.getTaAdresse().getCodepostalAdresse());
    						taInfosDocument.setCodepostalLiv(taTiers.getTaAdresse().getCodepostalAdresse());
    						
    						taInfosDocument.setVille(taTiers.getTaAdresse().getVilleAdresse());
    						taInfosDocument.setVilleLiv(taTiers.getTaAdresse().getVilleAdresse());
    						taInfosDocument.setPays(taTiers.getTaAdresse().getPaysAdresse());
    						taInfosDocument.setPaysLiv(taTiers.getTaAdresse().getPaysAdresse());
    					}
    					
    					if(taTiers.getTaTCivilite() != null) {
    						taInfosDocument.setCodeTCivilite(taTiers.getTaTCivilite().getCodeTCivilite());
    					}
    					
    					if(taTiers.getTaTEntite() != null) {
    						taInfosDocument.setCodeTEntite(taTiers.getTaTEntite().getCodeTEntite());
    					}
    					
    					
            		//remplir TaSouscription

            		obj.setBilling("Send Invoice");
            		obj.setDaysUntilDue(30);
            		if(enCours) {
            			obj.setDateDebut(dateDebut);
            			obj.setDateFin(LibDate.stringToDate(retour[7]));
            		}
            		else obj.setDateDebut(LibDate.stringToDate(retour[7]));
            		
            		obj.setCodeDocument(obj.getCodeDocument());
            		//obj.setTaAbonnement(obj);
            		//obj.setTaStripeSubscription(stripeSubscription);   	
            		
            		
           			lBR = new TaLAbonnement();  
           			lBR.setTaDocument(obj);
        			TaArticle art=daoArticle.findByCode(retour[2]);
        			TaTLigne tl=taTLigneService.findByCode("H");
        			

    				TaStripePlan plan = TaStripePlanService.findByNickname(retour[12]);
    				//TaStripeSubscriptionItem item = new TaStripeSubscriptionItem();
    				lBR.setTaPlan(plan);
        			lBR.setLibLDocument(art.getLibellecArticle());
        			lBR.setU1LDocument(uniteMois.getCodeUnite());
        			lBR.setPrixULDocument(plan.getAmount());
        			lBR.setQteLDocument(BigDecimal.ONE);
        			lBR.setLegrain(true);
        			lBR.setTaArticle(art);
        			if(enCours)  lBR.setRemTxLDocument(LibConversion.stringToBigDecimal("100"));
        			else lBR.setRemTxLDocument(LibConversion.stringToBigDecimal(retour[11].replace(",", ".")));
    				lBR.setTaDocument(obj);
    				lBR.setTaTLigne(tl);
        			
    				//item.setQuantity(lBR.getQteLDocument().intValue());
    				//item.setTaLAbonnement(lBR);
    				//item.setTaStripeSubscription(stripeSubscription);
    				//lBR.setTaStripeSubscriptionItem(item);

//    				if(stripeSubscription.getItems()==null)
//    					stripeSubscription.setItems(new HashSet<TaStripeSubscriptionItem>(0));
//    				stripeSubscription.getItems().add(item);
    				obj.addLigne(lBR);
    				obj.setSuspension(!enCours);

    				obj=majTaStripeSubscription(obj);

            		obj.calculeTvaEtTotaux();
            		 
            		
        			obj=TaAbonnementService.merge(obj);
        			
        			//créer la/les premieres echeances dans le cas ou l'on ne laisse pas stripe gérer les invoices
        			List<TaLEcheance> listePremieresEcheances = taAbonnementService.generePremieresEcheances(obj);
        			for (TaLEcheance taLEcheance : listePremieresEcheances) {
        				taLEcheance = taLEcheanceService.merge(taLEcheance);
        			}
        			
        			obj = new TaAbonnement();
        			obj.setLegrain(true);
            		//stripeSubscription = new  TaStripeSubscription();
        			}
    			}

    			ligne = br.readLine();

			}    		
    		br.close();
		
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			catch(IOException ioe){
				System.out.println("erreur : " + ioe);
             			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public boolean importationAbonnementPlusieursLignes() {
		String separateur=";";
		BufferedReader br = null;
		try
		{
			br = new BufferedReader(new InputStreamReader(  
					new FileInputStream("C:\\LGRDOSS\\BureauDeGestion\\listeAbonnementPlusieursLignes.csv"), "ISO-8859-1"));
//			BufferedReader br = new BufferedReader(fichierImportAbonnements);

    		String ligne = br.readLine();//j'ignore la première ligne num colonne
    		
    		//à partir de la troisième ligne
    		ligne = br.readLine();
    		String[] retour = null;
//    		String[] retourOld = null;
    		int i =1;
			TaAbonnement obj = new TaAbonnement();
			TaLAbonnement lBR;
    		//TaStripeSubscription stripeSubscription = new  TaStripeSubscription();
			String codeCourant;
			TaFrequence frequence = TaFrequenceService.findById(4);
			TaTPaiement tpaiement =TaTPaiementService.findByCode("C7");
			TaUnite uniteMois=TaUniteService.findById(5);
    		while (ligne!=null && !ligne.isEmpty()){
    			retour = ligne.split(separateur);
    			boolean enCours=LibConversion.stringToInteger(retour[4])<12;
    			Date dateDebut=LibDate.stringToDate(retour[7]);
    			dateDebut=LibDate.incrementDate(dateDebut, 0, 0, -1);

    				obj.setCodeDocument(retour[0]);
    				//changement de tiers et donc enregistrement du bon courant
//        			boolean exist=TaBonReceptionService.exist(obj.getCodeDocument());
        			boolean exist=false;
        			if(exist==false  && !retour[13].equals("PREL")) {
            		System.out.println(obj.getCodeDocument());
        				//remplir bon


            		TaTiers taTiers = taTiersService.findByCode(retour[0]);

            		
            		//remplir TaAbonnement
            		obj.setCodeDocument(TaAbonnementService.genereCode(null));
            		if(enCours)obj.setDateDocument(dateDebut);
            		else
            			obj.setDateDocument(new Date());
            		obj.setDateEchDocument(obj.getDateDocument());            		
            		obj.setDateLivDocument(obj.getDateDocument());
            		obj.setLibelleDocument("Maintenance logiciel 2020");
            		obj.setTaTiers(taTiers);
            		obj.setGestionLot(false);
					obj.setTaFrequence(frequence);
					obj.setTaTPaiement(tpaiement);	

					obj.setGestionTVA(true);
           		
            		//remplir infos document
            		TaInfosAbonnement taInfosDocument=new TaInfosAbonnement();
					obj.setTaInfosDocument(taInfosDocument);

            		taInfosDocument.setCodeTTvaDoc("F");
            		taInfosDocument.setTaDocument(obj);
            		taInfosDocument.setNomTiers(retour[1]);
            		taInfosDocument.setPrenomTiers(taTiers.getPrenomTiers());
					taInfosDocument.setSurnomTiers(taTiers.getSurnomTiers());
    				if(taTiers.getTaEntreprise()!=null)
    					taInfosDocument.setNomEntreprise(taTiers.getTaEntreprise().getNomEntreprise());

    				if (taTiers.getTaTTvaDoc()!=null){
    					taInfosDocument.setCodeTTvaDoc(taTiers.getTaTTvaDoc().getCodeTTvaDoc());  				

    					
    					taInfosDocument.setCodeCompta(taTiers.getCodeCompta());
    					taInfosDocument.setCompte(taTiers.getCompte());
    					obj.setNbDecimalesPrix(2);
    					obj.setNbDecimalesQte(2);

    					
    					//rajout yann
    					if(taTiers.getTaAdresse() != null) {
    						taInfosDocument.setAdresse1(taTiers.getTaAdresse().getAdresse1Adresse());
    						taInfosDocument.setAdresse2(taTiers.getTaAdresse().getAdresse2Adresse());
    						taInfosDocument.setAdresse3(taTiers.getTaAdresse().getAdresse3Adresse());
    						taInfosDocument.setAdresse1Liv(taTiers.getTaAdresse().getAdresse1Adresse());
    						taInfosDocument.setAdresse2Liv(taTiers.getTaAdresse().getAdresse2Adresse());
    						taInfosDocument.setAdresse3Liv(taTiers.getTaAdresse().getAdresse3Adresse());
    						
    						taInfosDocument.setCodepostal(taTiers.getTaAdresse().getCodepostalAdresse());
    						taInfosDocument.setCodepostalLiv(taTiers.getTaAdresse().getCodepostalAdresse());
    						
    						taInfosDocument.setVille(taTiers.getTaAdresse().getVilleAdresse());
    						taInfosDocument.setVilleLiv(taTiers.getTaAdresse().getVilleAdresse());
    						taInfosDocument.setPays(taTiers.getTaAdresse().getPaysAdresse());
    						taInfosDocument.setPaysLiv(taTiers.getTaAdresse().getPaysAdresse());
    					}
    					
    					if(taTiers.getTaTCivilite() != null) {
    						taInfosDocument.setCodeTCivilite(taTiers.getTaTCivilite().getCodeTCivilite());
    					}
    					
    					if(taTiers.getTaTEntite() != null) {
    						taInfosDocument.setCodeTEntite(taTiers.getTaTEntite().getCodeTEntite());
    					}
    					
    					
            		//remplir TaSouscription

    					obj.setBilling("Send Invoice");
    					obj.setDaysUntilDue(30);
            		if(enCours) {
            			obj.setDateDebut(dateDebut);
            			obj.setDateFin(LibDate.stringToDate(retour[7]));
            		}
            		else obj.setDateDebut(LibDate.stringToDate(retour[7]));
            		
            		//obj.setCodeDocument(obj.getCodeDocument());
            		//stripeSubscription.setTaAbonnement(obj);
            		//obj.setTaStripeSubscription(stripeSubscription);   	
            		
            		
           			lBR = new TaLAbonnement();  
           			lBR.setTaDocument(obj);
        			TaArticle art=daoArticle.findByCode(retour[2]);
        			TaTLigne tl=taTLigneService.findByCode("H");
        			

    				TaStripePlan plan = TaStripePlanService.findByNickname(retour[12]);
    				//TaStripeSubscriptionItem item = new TaStripeSubscriptionItem();
    				lBR.setTaPlan(plan);
        			lBR.setLibLDocument(art.getLibellecArticle());
        			lBR.setU1LDocument(uniteMois.getCodeUnite());
        			lBR.setPrixULDocument(plan.getAmount());
        			lBR.setQteLDocument(BigDecimal.ONE);
        			lBR.setLegrain(true);
        			lBR.setTaArticle(art);
        			if(enCours)  lBR.setRemTxLDocument(LibConversion.stringToBigDecimal("100"));
        			else lBR.setRemTxLDocument(LibConversion.stringToBigDecimal(retour[11].replace(",", ".")));
    				lBR.setTaDocument(obj);
    				lBR.setTaTLigne(tl);
        			
    				//item.setQuantity(lBR.getQteLDocument().intValue());
    				//item.setTaLAbonnement(lBR);
    				//item.setTaStripeSubscription(stripeSubscription);
    				//lBR.setTaStripeSubscriptionItem(item);

//    				if(stripeSubscription.getItems()==null)
//    					stripeSubscription.setItems(new HashSet<TaStripeSubscriptionItem>(0));
//    				stripeSubscription.getItems().add(item);
    				obj.addLigne(lBR);
    				obj.setSuspension(!enCours);

    				obj=majTaStripeSubscription(obj);

            		obj.calculeTvaEtTotaux();
            		 
            		
        			obj=TaAbonnementService.merge(obj);
        			
        			//créer la/les premieres echeances dans le cas ou l'on ne laisse pas stripe gérer les invoices
        			List<TaLEcheance> listePremieresEcheances = taAbonnementService.generePremieresEcheances(obj);
        			for (TaLEcheance taLEcheance : listePremieresEcheances) {
        				taLEcheance = taLEcheanceService.merge(taLEcheance);
        			}
        			
        			obj = new TaAbonnement();
        			obj.setLegrain(true);
            		//stripeSubscription = new  TaStripeSubscription();
        			}
    			}

    			ligne = br.readLine();

			}    		
    		br.close();
		
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			catch(IOException ioe){
				System.out.println("erreur : " + ioe);
             			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	public TaAbonnement majTaStripeSubscription(TaAbonnement abo) {
		/*******************************************************************************************/
		/*******************************************************************************************/
		try {
			abo.setTaStripeCustomer(taStripeCustomerService.rechercherCustomer(abo.getTaTiers()));
			//abo.setCodeDocument(abo.getCodeDocument());
			
			Integer timpeStampBillingCycleAnchor = null;
			String billing = null;
			Integer daysUntilDue = null;
			TaTPaiement tpaiement = null;

					//Mode manuel, pas de prélèvement automatique
					billing = "send_invoice";
					daysUntilDue = 30;
					abo.setBilling(billing);
					abo.setDaysUntilDue(daysUntilDue);

					
					abo.setTaStripeSource(taStripeSourceService.rechercherSourceManuelle("Chèque"));
//						** Comment stocker le type de moyen de paiement "manuel" ? différent des moyen de paiement automatique type source stripe
//						** Créer des sources "virtuelles" ? ou créer une table avec des moyens de paiement "fixe" ? utiliser TPaiement 
//								** ? Ajouter des booleens dans les StripeSubscription ? ou un ID vers une de ces nouvelles tables ?

						tpaiement = TaTPaiementService.findByCode("C7");

					

			//rajout yann
			abo.setTaTPaiement(tpaiement);

			
			return abo;
		} catch(Exception e) {
			e.printStackTrace();
		}
		/*******************************************************************************************/
		/*******************************************************************************************/
		return abo;
	}


	

	
	

	

}
