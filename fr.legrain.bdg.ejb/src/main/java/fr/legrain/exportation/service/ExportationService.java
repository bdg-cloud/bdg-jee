package fr.legrain.exportation.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.record.chart.FontBasisRecord;
import org.jboss.ejb3.annotation.TransactionTimeout;

import fr.legrain.article.dao.ITvaDAO;
import fr.legrain.article.model.TaTva;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.exportation.service.remote.IExportationServiceRemote;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IRelationDoc;
import fr.legrain.document.model.LigneTva;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaLApporteur;
import fr.legrain.document.model.TaLAvoir;
import fr.legrain.document.model.TaLFacture;
import fr.legrain.document.model.TaLRemise;
import fr.legrain.document.model.TaRAcompte;
import fr.legrain.document.model.TaRAvoir;
import fr.legrain.document.model.TaRReglement;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TaRemise;
import fr.legrain.documents.dao.IAcompteDAO;
import fr.legrain.documents.dao.IApporteurDAO;
import fr.legrain.documents.dao.IAvoirDAO;
import fr.legrain.documents.dao.IFactureDAO;
import fr.legrain.documents.dao.IRAvoirDAO;
import fr.legrain.documents.dao.IRReglementDAO;
import fr.legrain.documents.dao.IReglementDAO;
import fr.legrain.documents.dao.IRemiseDAO;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.exportation.model.ExportationEpicea;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;
import fr.legrain.tiers.dao.ICompteBanqueDAO;
import fr.legrain.tiers.dao.ITCPaiementDAO;
import fr.legrain.tiers.dao.ITPaiementDAO;
import fr.legrain.tiers.dao.ITTvaDocDAO;
import fr.legrain.tiers.model.TaTTvaDoc;
import fr.legrain.tiers.model.TaTiers;

/**
 * Session Bean implementation class TaFactureBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class ExportationService implements IExportationServiceRemote {

	static Logger logger = Logger.getLogger(ExportationService.class);

	@Inject private IFactureDAO dao;
	@Inject private IRemiseDAO daoRemise;
	
	@Inject private IAcompteDAO daoAcompte;
	@Inject private IAvoirDAO daoAvoir;
	@Inject private IApporteurDAO daoApporteur;
	@Inject private ITTvaDocDAO daoTTvaDoc;
	@Inject private ITvaDAO daoTvaDoc;
	
	@Inject private ICompteBanqueDAO daoCompteBanque;
	@Inject private ITCPaiementDAO daoCPaiment;
	@Inject private ITPaiementDAO daoTPaiement;
	
	@Inject private IReglementDAO daoReglement;
	@Inject private IRReglementDAO daoRReglement;
	@Inject private IRAvoirDAO daoRAvoir;
	@EJB private ITaPreferencesServiceRemote PreferencesService;
	@Inject private	SessionInfo sessionInfo;
	@Inject protected TenantInfo tenantInfo;
	
    private Writer fw = null;
    private BufferedWriter bw = null;
	
	String journalFacture ;
	String journalAvoir ;
	String journalApporteur;
	String journalReglement ;
	String journalRemise;

	
	public ExportationEpicea exportationEpicea=new ExportationEpicea();
//	Map<String,String> mapPreferences = new HashMap<String,String>();
	/**
	 * Default constructor. 
	 */
	public ExportationService() {

	}
	
//	public ExportationEpicea exportJPA(List<TaFacture> idFactureAExporter,List<TaAvoir> idAvoirAExporter,List<TaApporteur> idApporteurAExporter,List<TaAcompte> idAcompteAExporter,
//			List<TaReglement> idReglementAExporter,List<TaRemise> idRemiseAExporter, 
//			int reExportFacture,int reExportAvoir,int reExportAcpporteur,int reExportAcompte,int reExportReglement,int reExportRemise,boolean gererPointages,boolean centralisation) throws Exception {
//		int nb=0;
//		exportationEpicea=new ExportationEpicea();
//		if(idFactureAExporter!=null)nb=idFactureAExporter.size();
//		if(idAvoirAExporter!=null)nb=idAvoirAExporter.size();
//		if(idApporteurAExporter!=null)nb=idApporteurAExporter.size();
//		if(idAcompteAExporter!=null)nb=idAcompteAExporter.size();
//		if(idReglementAExporter!=null)nb=idReglementAExporter.size();
//		if(idRemiseAExporter!=null)nb=idRemiseAExporter.size();
//		String prefRepertoire =PreferencesService.retourPreferenceString(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.REPERTOIRE_EXPORTATION);
//		if(nb==0)exportationEpicea.setMessageRetour("Il n'y a aucun document à exporter !!!");
//		String tmp=System.getProperty("java.io.tmpdir");
//		exportationEpicea.setFichierExportation(tmp+"/E2-IMPOR.TXT");
//		
//		final boolean annuler[] = new boolean[]{false};
////		if(new File(exportationEpicea.getFichierExportation()).exists()) {
////			//Le fichier existe déjà
////			PlatformUI.getWorkbench().getDisplay().syncExec( new Runnable() {
////
////					@Override
////					public void run() {
////						annuler[0] = ! MessageDialog.openQuestion(
////									PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
////									"Bureau de gestion",
//////									"Attention, des documents sont en attente d'importation en comptabilité.\n" +
//////									"Si vous continuez l'exportation, la liste des documents déjà en attente d'importation sera réinitialisée.\n\n" +
//////									"Annuler l'exportation en cours ?");
////									"Attention, des documents sont en attente d'importation en comptabilité.\n" +
////									"Si vous continuez l'exportation en cours, cette liste de documents en attente\n" +
////									"d'importation sera supprimée.\n" +
////									"\n" +
////									"Continuer l'exportation en cours ?");
////						
////					}
////					} );
////			
////		}
//
//		if(!annuler[0]) {
//
//		try {      
//			String ctpEscompteDebit = "665";
//			String ctpEscompteCredit = "765";
//			exportationEpicea.setReExportAcompte(reExportAcompte);
//			exportationEpicea.setReExportAvoir(reExportAvoir);
//			exportationEpicea.setReExportFacture(reExportFacture);
//			exportationEpicea.setReExportReglement(reExportReglement);
//			exportationEpicea.setReExportRemise(reExportRemise);
//			boolean reglementLies=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.REGLEMENTS_LIES);
//
////			Paths.get("/tmp/test.txt")
////			exportationEpicea.setFw( new FileWriter(exportationEpicea.getFichierExportation()));
//			
//			fw=new FileWriter(exportationEpicea.getFichierExportation());
//			File reportFile = new File(exportationEpicea.getFichierExportation());
//			exportationEpicea.setLocationFichier(reportFile.getAbsolutePath());
////			exportationEpicea.setBw(new BufferedWriter(exportationEpicea.getFw()));
//			bw=new BufferedWriter(fw);
//			exportationEpicea.setNumDepart(0);
//
//			if(idFactureAExporter!=null) {
//				exportationEpicea.setNumDepart(exportFactureJPA(idFactureAExporter,exportationEpicea.getListComplete(), fw,  reExportFacture,exportationEpicea.getNumDepart(),gererPointages,null,
//						reglementLies));
//			}
//			
//			if(idAvoirAExporter!=null) {
//				exportationEpicea.setNumDepart(exportAvoirJPA(idAvoirAExporter,exportationEpicea.getListComplete(), fw, reExportAvoir,exportationEpicea.getNumDepart(),gererPointages,null,
//						reglementLies));
//			}
//
//			if(idAcompteAExporter!=null) {
//				exportationEpicea.setNumDepart(exportAcompteJPA(idAcompteAExporter,exportationEpicea.getListComplete(), fw,  reExportAcompte,exportationEpicea.getNumDepart(),gererPointages,null,
//						reglementLies));
//			}
//			
//			if(idReglementAExporter!=null) {
//				exportationEpicea.setNumDepart(exportReglementJPA(idReglementAExporter,exportationEpicea.getListComplete(), fw,  reExportReglement,exportationEpicea.getNumDepart(),gererPointages,null,
//						reglementLies,null));
//			}
//			
//			if(idRemiseAExporter!=null) {
//				exportationEpicea.setNumDepart(exportRemiseJPA(idRemiseAExporter,exportationEpicea.getListComplete(), fw,  reExportRemise,exportationEpicea.getNumDepart(),gererPointages,null,
//						reglementLies));
//			}
//			 List<IDocumentTiers> listeDocumentsManquants =exportationEpicea.verifCoherenceExportation();
//			 List elementsToSelect = new LinkedList<String>();
//			for (IDocumentTiers o : listeDocumentsManquants) {
//				elementsToSelect.add(o.getCodeDocument());
//				o.setAccepte(false);
//			}
//			/************************************************  A remettre voir avec Nicolas comment ????? */
////			if(elementsToSelect.size()>0){
////            Display.getDefault().syncExec(new Runnable() {
////                @Override
////                public void run() {               	
////                	
////                	 ListSelectionDialog dlg =
////                		   new ListSelectionDialog(
////                				   PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
////                				   elementsToSelect,
////                				   new ArrayContentProvider(), 
////                			        new LabelProvider(),
////                		                 "Les documents suivants sont manquant, sélectionnez ceux que vous voulez rajouter à l'exportation ?");
////                	 dlg.setTitle("Documents manquant dans l'exportation"); //$NON-NLS-1$
////                	 dlg.setInitialElementSelections(elementsToSelect);
////                	 dlg.open();
////                	 for (Object b : dlg.getResult()) {
////                		 for (IDocumentTiers doc : listeDocumentsManquants) {
////							if(doc.getCodeDocument().equals(b))doc.setAccepte(true);
////						}
////					} 
////
////                }});
////			} 			
//			/************************************************  A remettre voir avec Nicolas comment ????? */
//            for (IDocumentTiers doc : listeDocumentsManquants) {
//				if(doc.getAccepte()){
//					if(doc.getTypeDocument()==TaFacture.TYPE_DOC){
//						idFactureAExporter = new  LinkedList<TaFacture>();
//						idFactureAExporter.add((TaFacture)doc);
//						exportationEpicea.setNumDepart(exportFactureJPA(idFactureAExporter,exportationEpicea.getListComplete(), fw,  reExportFacture,exportationEpicea.getNumDepart(),false,null,false));
//					}
//					if(doc.getTypeDocument()==TaAvoir.TYPE_DOC){
//						idAvoirAExporter=new LinkedList<TaAvoir>();
//						idAvoirAExporter.add((TaAvoir)doc);
//						exportationEpicea.setNumDepart(exportAvoirJPA(idAvoirAExporter,exportationEpicea.getListComplete(), fw,  reExportFacture,exportationEpicea.getNumDepart(),false,null,false));
//					}
//					if(doc.getTypeDocument()==TaAcompte.TYPE_DOC){
//						idAcompteAExporter = new LinkedList<TaAcompte>();
//						idAcompteAExporter.add((TaAcompte)doc);
//						exportationEpicea.setNumDepart(exportAcompteJPA(idAcompteAExporter,exportationEpicea.getListComplete(), fw,  reExportFacture,exportationEpicea.getNumDepart(),false,null,false));
//					}
//					if(doc.getTypeDocument()==TaReglement.TYPE_DOC){
//						idReglementAExporter = new  LinkedList<TaReglement>();
//						idReglementAExporter.add((TaReglement)doc);
//						exportationEpicea.setNumDepart(exportReglementJPA(idReglementAExporter,exportationEpicea.getListComplete(), fw,  reExportFacture,exportationEpicea.getNumDepart(),false,null,false,null));
//					}
//					if(doc.getTypeDocument()==TaRemise.TYPE_DOC){
//						idRemiseAExporter = new LinkedList<TaRemise>();
//						idRemiseAExporter.add((TaRemise)doc);
//						exportationEpicea.setNumDepart(exportRemiseJPA(idRemiseAExporter,exportationEpicea.getListComplete(), fw,  reExportFacture,exportationEpicea.getNumDepart(),false,null,false));
//					}
//				}
//			}
//
//
//			if(fw!=null) fw.close();
//			if(bw!=null) bw.close();
//
//
//		} catch(IOException ioe){
//			logger.error("",ioe);
//		} 
//		catch (Exception e) {
//			if(fw!=null) fw.close();
//			if(bw!=null) bw.close();
//			logger.error("",e);
//			exportationEpicea.effaceFichierTexte(exportationEpicea.getFichierExportation());
//			throw e;
//		}
//		return getExportationEpicea();
//		} else {
//			exportationEpicea.setMessageRetour("Exportation annulée.");
//			return null;
//		}
//	}
	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportApporteurCentraliseeJPA(List<TaApporteur> idFactureAExporter,List<String> listComplete,Writer fw,
			int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesDocumenstLies) throws Exception {
		int numFin=numDepart;
		try {      


			Boolean gererLesPointages = gererPointages;

//			TaApporteurDAO TaApporteurDAO = new TaApporteurDAO(em);
			TaApporteur TaApporteur = null;


			exportationEpicea.numeroDePiece = numFin;



			boolean nouvellePiece = false;


			for(int i=0; i<idFactureAExporter.size(); i++) {
				TaApporteur = idFactureAExporter.get(i);
				if(listComplete.indexOf(TaApporteur.getCodeDocument())==-1){

					/**/   // pour compenser les problèmes d'arrondis mis sur des lignes à 0
					TaApporteur.calculeTvaEtTotaux();
					/**/  //isa le 03 février 2011 en accord avec Nicolas

					if(TaApporteur.getDateExport()==null || reExport==1) { /* facture pas deja exportee ou reexportation */
						nouvellePiece = true;

						if(nouvellePiece) {
							exportationEpicea.numeroDePiece++;
							nouvellePiece = false;
							numFin++;
						}

						//on crée un nouvel objet facture pour créer la centralisation
						TaApporteur newDocument =new TaApporteur(false);
						newDocument=(TaApporteur) TaApporteur.cloneCentralisation();
						newDocument.getLignes().clear();
						
						TaLApporteur newTaLDocument;
						for (TaLApporteur obj : TaApporteur.getLignes()) {
							newTaLDocument=newDocument.contientMemeParametreCompte(obj);
							if(newTaLDocument!=null){
								newTaLDocument.setQteLDocument(BigDecimal.ONE);
								if(TaApporteur.getTtc()==1) {
									newTaLDocument.setPrixULDocument(newTaLDocument.getPrixULDocument().add(obj.getMtTtcLApresRemiseGlobaleDocument()));
								}else{
									newTaLDocument.setPrixULDocument(newTaLDocument.getPrixULDocument().add(obj.getMtHtLApresRemiseGlobaleDocument()));
								}
							}else{
								if(obj.getTaArticle()!=null){
								newTaLDocument=new TaLApporteur(false);
								newTaLDocument=obj.clone();
								newTaLDocument.setLibLDocument(newDocument.getLibelleDocument());
								newTaLDocument.setQteLDocument(BigDecimal.ONE);
								newTaLDocument.setRemTxLDocument(BigDecimal.ZERO);
								if(TaApporteur.getTtc()==1) {
									newTaLDocument.setPrixULDocument(obj.getMtTtcLApresRemiseGlobaleDocument());
								}else{
									newTaLDocument.setPrixULDocument(obj.getMtHtLApresRemiseGlobaleDocument());
								}
								newTaLDocument.setTaDocument(newDocument);
								newDocument.addLigne(newTaLDocument);
								}
							}
						}
						newDocument.setTxRemHtDocument(BigDecimal.ZERO);
						newDocument.calculeTvaEtTotaux();
						
						//recuperation des informations propre a la facture
						exportationEpicea.codePiece = TaApporteur.getCodeDocument();
						exportationEpicea.datePiece = TaApporteur.getDateDocument();
						exportationEpicea.dateEcheance = TaApporteur.getDateEchDocument();
						exportationEpicea.dateLivraisonLigne = TaApporteur.getDateDocument();//changer pour erreur 18000 dans epicéa;
//						dateLivraisonLigne = TaApporteur.getDateLivDocument();
						exportationEpicea.libelleLigne = TaApporteur.getTaInfosDocument().getNomTiers();
						exportationEpicea.numCptLigne = TaApporteur.getTaInfosDocument().getCodeCompta();
						exportationEpicea.cptCollectif = TaApporteur.getTaInfosDocument().getCompte();
						exportationEpicea.nomTiers = TaApporteur.getTaInfosDocument().getNomTiers();
						exportationEpicea.adresse1Tiers = TaApporteur.getTaInfosDocument().getAdresse1();
						exportationEpicea.adresse2Tiers = TaApporteur.getTaInfosDocument().getAdresse2();
						exportationEpicea.codePostalTiers = TaApporteur.getTaInfosDocument().getCodepostal();
						exportationEpicea.villeTiers = TaApporteur.getTaInfosDocument().getVille();
						//					ttc = taFacture.getTtc();
						//					txRemHtFacture = taFacture.getRemHtFacture();
						//					exportComtpa = taFacture.getExport();

						exportationEpicea.mtCreditLigne = LibConversion.BigDecimalToDouble(TaApporteur.getNetTtcCalc());  

						exportationEpicea.numCptLigne = "+"+TaApporteur.getTaInfosDocument().getCodeCompta();    
						exportationEpicea.numLignePiece = 1;
						exportationEpicea.typePiece = exportationEpicea.TypeAchat;
						exportationEpicea.mtDebitLigne = 0d;
						exportationEpicea.qte1 = 0d;
						exportationEpicea.qte2 = 0d;
						exportationEpicea.codeTva ="";
						exportationEpicea.tauxTva = 0d;
						exportationEpicea.mtDebitTva = 0d;
						exportationEpicea.mtCreditTva = 0d;
						exportationEpicea.exigibiliteTva = "";

						exportationEpicea.refContrepartie = "";
						exportationEpicea.dateContrepartie = null;
						exportationEpicea.mtContrepartieDebit = 0d;
						exportationEpicea.mtContrepartieCredit = 0d;
						exportationEpicea.mtAffectation = 0d;
						exportationEpicea.codeTvaComplet = "";
						exportationEpicea.refReglement = "";

						//ecriture de la ligne d'entete dans le fichier d'export
						fw.write(exportationEpicea.creationLigne(exportationEpicea.numeroDePiece,
								exportationEpicea.numLignePiece ,
								exportationEpicea.typePiece ,
								exportationEpicea.codePiece ,
								exportationEpicea.datePiece ,
								exportationEpicea.numCptLigne ,
								exportationEpicea.libelleLigne ,
								exportationEpicea.mtDebitLigne ,
								exportationEpicea.mtCreditLigne ,
								exportationEpicea.qte1 ,
								exportationEpicea.qte2 ,
								exportationEpicea.codeTva ,
								exportationEpicea.tauxTva ,
								exportationEpicea.mtCreditTva ,
								exportationEpicea.mtDebitTva ,
								exportationEpicea.dateEcheance ,
								exportationEpicea.cptCollectif ,
								exportationEpicea.nomTiers ,
								exportationEpicea.adresse1Tiers ,
								exportationEpicea.adresse2Tiers ,
								exportationEpicea.codePostalTiers ,
								exportationEpicea.villeTiers ,
								exportationEpicea.exigibiliteTva ,
								exportationEpicea.dateLivraisonLigne,
								exportationEpicea.refContrepartie,
								exportationEpicea.dateContrepartie,
								exportationEpicea.mtContrepartieDebit,
								exportationEpicea.mtContrepartieCredit,
								exportationEpicea.mtAffectation,
								exportationEpicea.codeTvaComplet,
								exportationEpicea.refReglement));
						fw.write(exportationEpicea.finDeLigne);

						//réinitialisation des variables pour le prochain document
						exportationEpicea.cptCollectif ="";
						exportationEpicea.nomTiers ="";
						exportationEpicea.adresse1Tiers ="";
						exportationEpicea.adresse2Tiers ="";
						exportationEpicea.codePostalTiers ="";
						exportationEpicea.villeTiers="";


						//traitement des lignes
						for (TaLApporteur lf : newDocument.getLignes()) {
							//récupération des valeurs propres à la ligne
							exportationEpicea.typePiece = exportationEpicea.TypeAchat;
//							if(lf.getTaArticle()!=null   && lf.getMtHtLApresRemiseGlobaleDocument()!=null && lf.getMtHtLApresRemiseGlobaleDocument().signum()!=0){
								if(lf.getTaArticle()!=null   && lf.getMtHtLApresRemiseGlobaleDocument()!=null ){
								exportationEpicea.libelleLigne = lf.getLibLDocument();
								exportationEpicea.qte1 = LibConversion.BigDecimalToDouble(lf.getQteLDocument());
								exportationEpicea.qte2 = LibConversion.BigDecimalToDouble(lf.getQte2LDocument());
								exportationEpicea.numCptLigne = lf.getCompteLDocument();
								if(exportationEpicea.numCptLigne==null || exportationEpicea.numCptLigne.equals("")){
									exportationEpicea.setRetour(false);
									exportationEpicea.setMessageRetour("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+TaApporteur.getCodeDocument());
									throw new Exception("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+TaApporteur.getCodeDocument());
								}
								//traitement du type de tva du tiers (F ou ue ou horsUE ou Franchise)
//								TaTTvaDocDAO daoTvaDoc = new TaTTvaDocDAO(em);
								String codeDoc="";
								if(TaApporteur.getTaInfosDocument().getCodeTTvaDoc()==null || TaApporteur.getTaInfosDocument().getCodeTTvaDoc().equals(""))
									codeDoc="F";
								else codeDoc=TaApporteur.getTaInfosDocument().getCodeTTvaDoc();
								TaTTvaDoc tvaDoc =daoTTvaDoc.findByCode(codeDoc);
								String journalTva="";
								if(tvaDoc!=null)journalTva=tvaDoc.getJournalTTvaDoc();
								
//									if(TaApporteur.getTaTiers()!=null && TaApporteur.getTaTiers().getTaTTvaDoc()!=null){
								if(codeDoc.equalsIgnoreCase("F"))
									exportationEpicea.codeTva = exportationEpicea.correspondanceCodeTva(codeDoc,lf.getCodeTvaLDocument(),lf.getCodeTvaLDocument(),exportationEpicea.numCptLigne,lf.getMtTtcLApresRemiseGlobaleDocument().subtract(lf.getMtHtLApresRemiseGlobaleDocument()));
								else
									exportationEpicea.codeTva = exportationEpicea.correspondanceCodeTva(codeDoc,lf.getCodeTvaLDocument(),journalTva,exportationEpicea.numCptLigne,lf.getMtTtcLApresRemiseGlobaleDocument().subtract(lf.getMtHtLApresRemiseGlobaleDocument()));	
								exportationEpicea.codeTvaComplet=lf.getCodeTvaLDocument();
								if(exportationEpicea.codeTva==null)exportationEpicea.codeTva=""; /* corrigé par isa le 11/09/2014*/
								if(lf.getTaArticle().getTaTTva()!=null)  
									exportationEpicea.exigibiliteTva = lf.getTaArticle().getTaTTva().getCodeTTva();
								if (exportationEpicea.exigibiliteTva.equals(""))exportationEpicea.exigibiliteTva="D";
								
								exportationEpicea.tauxTva = LibConversion.BigDecimalToDouble(lf.getTauxTvaLDocument());
								exportationEpicea.mtDebitTva = LibConversion.BigDecimalToDouble(lf.getMtTtcLApresRemiseGlobaleDocument().subtract(lf.getMtHtLApresRemiseGlobaleDocument()));
								exportationEpicea.mtDebitLigne = LibConversion.BigDecimalToDouble(lf.getMtHtLApresRemiseGlobaleDocument());

								exportationEpicea.numLignePiece = exportationEpicea.numLignePiece + 1;
								exportationEpicea.mtCreditLigne = 0d;
								exportationEpicea.mtCreditTva = 0d;
								exportationEpicea.cptCollectif = "";
								exportationEpicea.nomTiers = "";
								exportationEpicea.adresse1Tiers = "";
								exportationEpicea.adresse2Tiers = "";
								exportationEpicea.codePostalTiers = "";
								exportationEpicea.villeTiers = "";
								//ecriture de la ligne dans le fichier d'export

								exportationEpicea.refContrepartie = "";
								exportationEpicea.dateContrepartie = null;
								exportationEpicea.mtContrepartieDebit = 0d;
								exportationEpicea.mtContrepartieCredit = 0d;
								exportationEpicea.mtAffectation = 0d;
								exportationEpicea.refReglement = "";

								fw.write(exportationEpicea.creationLigne(exportationEpicea.numeroDePiece,
										exportationEpicea.numLignePiece ,
										exportationEpicea.typePiece ,
										exportationEpicea.codePiece ,
										exportationEpicea.datePiece ,
										exportationEpicea.numCptLigne ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.mtDebitLigne,
										exportationEpicea.mtCreditLigne ,
										exportationEpicea.qte1 ,
										exportationEpicea.qte2 ,
										exportationEpicea.codeTva ,
										exportationEpicea.tauxTva ,
										exportationEpicea.mtDebitTva,
										exportationEpicea.mtCreditTva ,
										exportationEpicea.dateEcheance ,
										exportationEpicea.cptCollectif ,
										exportationEpicea.nomTiers ,
										exportationEpicea.adresse1Tiers ,
										exportationEpicea.adresse2Tiers ,
										exportationEpicea.codePostalTiers ,
										exportationEpicea.villeTiers ,
										exportationEpicea.exigibiliteTva ,
										exportationEpicea.dateLivraisonLigne,
										exportationEpicea.refContrepartie,
										exportationEpicea.dateContrepartie,
										exportationEpicea.mtContrepartieDebit,
										exportationEpicea.mtContrepartieCredit,
										exportationEpicea.mtAffectation,
										exportationEpicea.codeTvaComplet,
										exportationEpicea.refReglement));
								fw.write(exportationEpicea.finDeLigne);
							}
						} 

//						if(TaApporteur.getExport()!=1)TaApporteur.setExport(1);
//						daoApporteur.merge(TaApporteur);
						exportationEpicea.listComplete.add(TaApporteur.getCodeDocument());
						exportationEpicea.listCompleteDoc.add(TaApporteur);
						exportationEpicea.listeVerif.add(TaApporteur);
					}
				}

			}

		} catch(IOException ioe){
			logger.error("",ioe);
			exportationEpicea.setRetour(false);
		} 

		return numFin;
	}

	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportAvoirCentraliseeJPA(List<TaAvoir> idFactureAExporter,List<String> listComplete,Writer fw,
			int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesDocumenstLies) throws Exception {
		int numFin=numDepart;
		try {      


			Boolean gererLesPointages = gererPointages;

//			TaAvoirDAO taAvoirDAO = new TaAvoirDAO(em);
			TaAvoir taAvoir = null;


			exportationEpicea.numeroDePiece = numFin;



			boolean nouvellePiece = false;


			for(int i=0; i<idFactureAExporter.size(); i++) {
				taAvoir = idFactureAExporter.get(i);
				if(listComplete.indexOf(taAvoir.getCodeDocument())==-1){

					/**/   // pour compenser les problèmes d'arrondis mis sur des lignes à 0
					taAvoir.calculeTvaEtTotaux();
					/**/  //isa le 03 février 2011 en accord avec Nicolas

					if(taAvoir.getDateExport()==null || reExport==1) { /* facture pas deja exportee ou reexportation */
						nouvellePiece = true;

						if(nouvellePiece) {
							exportationEpicea.numeroDePiece++;
							nouvellePiece = false;
							numFin++;
						}

						//on crée un nouvel objet facture pour créer la centralisation
						TaAvoir newDocument =new TaAvoir(false);
						newDocument=(TaAvoir) taAvoir.cloneCentralisation();
						newDocument.getLignes().clear();
						
						TaLAvoir newTaLDocument;
						for (TaLAvoir obj : taAvoir.getLignes()) {
							newTaLDocument=newDocument.contientMemeParametreCompte(obj);
							if(newTaLDocument!=null){
								newTaLDocument.setQteLDocument(BigDecimal.ONE);
								if(taAvoir.getTtc()==1) {
									newTaLDocument.setPrixULDocument(newTaLDocument.getPrixULDocument().add(obj.getMtTtcLApresRemiseGlobaleDocument()));
								}else{
									newTaLDocument.setPrixULDocument(newTaLDocument.getPrixULDocument().add(obj.getMtHtLApresRemiseGlobaleDocument()));
								}
							}else{
								if(obj.getTaArticle()!=null){
								newTaLDocument=new TaLAvoir(false);
								newTaLDocument=obj.clone();
								newTaLDocument.setLibLDocument(newDocument.getLibelleDocument());
								newTaLDocument.setQteLDocument(BigDecimal.ONE);
								newTaLDocument.setRemTxLDocument(BigDecimal.ZERO);
								if(taAvoir.getTtc()==1) {
									newTaLDocument.setPrixULDocument(obj.getMtTtcLApresRemiseGlobaleDocument());
								}else{
									newTaLDocument.setPrixULDocument(obj.getMtHtLApresRemiseGlobaleDocument());
								}
								newTaLDocument.setTaDocument(newDocument);
								newDocument.addLigne(newTaLDocument);
								}
							}
						}
						newDocument.setTxRemHtDocument(BigDecimal.ZERO);
						newDocument.calculeTvaEtTotaux();
						
						//recuperation des informations propre a la facture
						exportationEpicea.codePiece = taAvoir.getCodeDocument();
						exportationEpicea.datePiece = taAvoir.getDateDocument();
						exportationEpicea.dateEcheance = taAvoir.getDateEchDocument();
						exportationEpicea.dateLivraisonLigne = taAvoir.getDateDocument();//changer pour erreur 18000 dans epicéa;
//						dateLivraisonLigne = taAvoir.getDateLivDocument();
						exportationEpicea.libelleLigne = taAvoir.getTaInfosDocument().getNomTiers();
						exportationEpicea.numCptLigne = taAvoir.getTaInfosDocument().getCodeCompta();
						exportationEpicea.cptCollectif = taAvoir.getTaInfosDocument().getCompte();
						exportationEpicea.nomTiers = taAvoir.getTaInfosDocument().getNomTiers();
						exportationEpicea.adresse1Tiers = taAvoir.getTaInfosDocument().getAdresse1();
						exportationEpicea.adresse2Tiers = taAvoir.getTaInfosDocument().getAdresse2();
						exportationEpicea.codePostalTiers = taAvoir.getTaInfosDocument().getCodepostal();
						exportationEpicea.villeTiers = taAvoir.getTaInfosDocument().getVille();

						exportationEpicea.mtCreditLigne = LibConversion.BigDecimalToDouble(taAvoir.getNetTtcCalc());  

						exportationEpicea.numCptLigne = "+"+taAvoir.getTaInfosDocument().getCodeCompta();    
						exportationEpicea.numLignePiece = 1;
						exportationEpicea.typePiece = exportationEpicea.TypeAchat;
						exportationEpicea.mtDebitLigne = 0d;
						exportationEpicea.qte1 = 0d;
						exportationEpicea.qte2 = 0d;
						exportationEpicea.codeTva ="";
						exportationEpicea.tauxTva = 0d;
						exportationEpicea.mtDebitTva = 0d;
						exportationEpicea.mtCreditTva = 0d;
						exportationEpicea.exigibiliteTva = "";

						exportationEpicea.refContrepartie = "";
						exportationEpicea.dateContrepartie = null;
						exportationEpicea.mtContrepartieDebit = 0d;
						exportationEpicea.mtContrepartieCredit = 0d;
						exportationEpicea.mtAffectation = 0d;
						exportationEpicea.codeTvaComplet = "";
						exportationEpicea.refReglement = "";

						//ecriture de la ligne d'entete dans le fichier d'export
						fw.write(exportationEpicea.creationLigne(exportationEpicea.numeroDePiece,
								exportationEpicea.numLignePiece ,
								exportationEpicea.typePiece ,
								exportationEpicea.codePiece ,
								exportationEpicea.datePiece ,
								exportationEpicea.numCptLigne ,
								exportationEpicea.libelleLigne ,
								exportationEpicea.mtDebitLigne ,
								exportationEpicea.mtCreditLigne ,
								exportationEpicea.qte1 ,
								exportationEpicea.qte2 ,
								exportationEpicea.codeTva ,
								exportationEpicea.tauxTva ,
								exportationEpicea.mtCreditTva ,
								exportationEpicea.mtDebitTva ,
								exportationEpicea.dateEcheance ,
								exportationEpicea.cptCollectif ,
								exportationEpicea.nomTiers ,
								exportationEpicea.adresse1Tiers ,
								exportationEpicea.adresse2Tiers ,
								exportationEpicea.codePostalTiers ,
								exportationEpicea.villeTiers ,
								exportationEpicea.exigibiliteTva ,
								exportationEpicea.dateLivraisonLigne,
								exportationEpicea.refContrepartie,
								exportationEpicea.dateContrepartie,
								exportationEpicea.mtContrepartieDebit,
								exportationEpicea.mtContrepartieCredit,
								exportationEpicea.mtAffectation,
								exportationEpicea.codeTvaComplet,
								exportationEpicea.refReglement));
						fw.write(exportationEpicea.finDeLigne);

						//réinitialisation des variables pour le prochain document
						exportationEpicea.cptCollectif ="";
						exportationEpicea.nomTiers ="";
						exportationEpicea.adresse1Tiers ="";
						exportationEpicea.adresse2Tiers ="";
						exportationEpicea.codePostalTiers ="";
						exportationEpicea.villeTiers="";


						//traitement des lignes
						for (TaLAvoir lf : newDocument.getLignes()) {
							//récupération des valeurs propres à la ligne
							//						if(lf.getTaArticle()!=null){
							exportationEpicea.typePiece = exportationEpicea.TypeAchat;
//							if(lf.getTaArticle()!=null   && lf.getMtHtLApresRemiseGlobaleDocument()!=null && lf.getMtHtLApresRemiseGlobaleDocument().signum()!=0){
								if(lf.getTaArticle()!=null   && lf.getMtHtLApresRemiseGlobaleDocument()!=null ){
								exportationEpicea.libelleLigne = lf.getLibLDocument();
								exportationEpicea.qte1 = LibConversion.BigDecimalToDouble(lf.getQteLDocument());
								exportationEpicea.qte2 = LibConversion.BigDecimalToDouble(lf.getQte2LDocument());
								exportationEpicea.numCptLigne = lf.getCompteLDocument();
								if(exportationEpicea.numCptLigne==null || exportationEpicea.numCptLigne.equals("")){
									//transaction.rollback();
									exportationEpicea.setRetour(false);
									exportationEpicea.setMessageRetour("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taAvoir.getCodeDocument());
									throw new Exception("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taAvoir.getCodeDocument());
								}
								//traitement du type de tva du tiers (F ou ue ou horsUE ou Franchise)
//								TaTTvaDocDAO daoTvaDoc = new TaTTvaDocDAO(em);
								String codeDoc="";
								if(taAvoir.getTaInfosDocument().getCodeTTvaDoc()==null || taAvoir.getTaInfosDocument().getCodeTTvaDoc().equals(""))
									codeDoc="F";
								else codeDoc=taAvoir.getTaInfosDocument().getCodeTTvaDoc();
								TaTTvaDoc tvaDoc =daoTTvaDoc.findByCode(codeDoc);
								String journalTva="";
								if(tvaDoc!=null)journalTva=tvaDoc.getJournalTTvaDoc();
								
//									if(taAvoir.getTaTiers()!=null && taAvoir.getTaTiers().getTaTTvaDoc()!=null){
								if(codeDoc.equalsIgnoreCase("F"))
									exportationEpicea.codeTva = exportationEpicea.correspondanceCodeTva(codeDoc,lf.getCodeTvaLDocument(),lf.getCodeTvaLDocument(),exportationEpicea.numCptLigne,lf.getMtTtcLApresRemiseGlobaleDocument().subtract(lf.getMtHtLApresRemiseGlobaleDocument()));
								else
									exportationEpicea.codeTva = exportationEpicea.correspondanceCodeTva(codeDoc,lf.getCodeTvaLDocument(),journalTva,exportationEpicea.numCptLigne,lf.getMtTtcLApresRemiseGlobaleDocument().subtract(lf.getMtHtLApresRemiseGlobaleDocument()));	
								exportationEpicea.codeTvaComplet=lf.getCodeTvaLDocument();


//								exigibiliteTva = lf.getTaArticle().getTaTTva().getCodeTTva();
//								if(exigibiliteTva == null)exigibiliteTva="D";
								if(exportationEpicea.codeTva==null)exportationEpicea.codeTva=""; /* corrigé par isa le 11/09/2014*/
								if(lf.getTaArticle().getTaTTva()!=null)  
									exportationEpicea.exigibiliteTva = lf.getTaArticle().getTaTTva().getCodeTTva();
								if (exportationEpicea.exigibiliteTva.equals(""))exportationEpicea.exigibiliteTva="D";
								
								exportationEpicea.tauxTva = LibConversion.BigDecimalToDouble(lf.getTauxTvaLDocument());
								exportationEpicea.mtDebitTva = LibConversion.BigDecimalToDouble(lf.getMtTtcLApresRemiseGlobaleDocument().subtract(lf.getMtHtLApresRemiseGlobaleDocument()));
								exportationEpicea.mtDebitLigne = LibConversion.BigDecimalToDouble(lf.getMtHtLApresRemiseGlobaleDocument());

								exportationEpicea.numLignePiece = exportationEpicea.numLignePiece + 1;
								exportationEpicea.mtCreditLigne = 0d;
								exportationEpicea.mtCreditTva = 0d;
								exportationEpicea.cptCollectif = "";
								exportationEpicea.nomTiers = "";
								exportationEpicea.adresse1Tiers = "";
								exportationEpicea.adresse2Tiers = "";
								exportationEpicea.codePostalTiers = "";
								exportationEpicea.villeTiers = "";
								//ecriture de la ligne dans le fichier d'export

								exportationEpicea.refContrepartie = "";
								exportationEpicea.dateContrepartie = null;
								exportationEpicea.mtContrepartieDebit = 0d;
								exportationEpicea.mtContrepartieCredit = 0d;
								exportationEpicea.mtAffectation = 0d;
								exportationEpicea.refReglement = "";

								fw.write(exportationEpicea.creationLigne(exportationEpicea.numeroDePiece,
										exportationEpicea.numLignePiece ,
										exportationEpicea.typePiece ,
										exportationEpicea.codePiece ,
										exportationEpicea.datePiece ,
										exportationEpicea.numCptLigne ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.mtDebitLigne,
										exportationEpicea.mtCreditLigne ,
										exportationEpicea.qte1 ,
										exportationEpicea.qte2 ,
										exportationEpicea.codeTva ,
										exportationEpicea.tauxTva ,
										exportationEpicea.mtDebitTva,
										exportationEpicea.mtCreditTva ,
										exportationEpicea.dateEcheance ,
										exportationEpicea.cptCollectif ,
										exportationEpicea.nomTiers ,
										exportationEpicea.adresse1Tiers ,
										exportationEpicea.adresse2Tiers ,
										exportationEpicea.codePostalTiers ,
										exportationEpicea.villeTiers ,
										exportationEpicea.exigibiliteTva ,
										exportationEpicea.dateLivraisonLigne,
										exportationEpicea.refContrepartie,
										exportationEpicea.dateContrepartie,
										exportationEpicea.mtContrepartieDebit,
										exportationEpicea.mtContrepartieCredit,
										exportationEpicea.mtAffectation,
										exportationEpicea.codeTvaComplet,
										exportationEpicea.refReglement));
								fw.write(exportationEpicea.finDeLigne);
							}
						} 
						if(gererLesPointages){
							//traitement des lignes pour les pointages
							exportationEpicea.typePiece = exportationEpicea.typePointage;
							for (TaRAvoir lf : taAvoir.getTaRAvoirs()) {
								//récupération des valeurs propres à la ligne
								TaFacture fac =lf.getTaFacture();
								numFin=initPointage(fac, taAvoir, lf, gererLesDocumenstLies, idAppelant, numFin);

							} 
						}

//						if(taAvoir.getExport()!=1)taAvoir.setExport(1);
//						daoAvoir.merge(taAvoir);
						exportationEpicea.listComplete.add(taAvoir.getCodeDocument());
						exportationEpicea.listCompleteDoc.add(taAvoir);
						exportationEpicea.listeVerif.add(taAvoir);
					}
				}

			}

		} catch(IOException ioe){
			logger.error("",ioe);
			exportationEpicea.setRetour(false);
		} 
		return numFin;
	}


	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportAvoirCentraliseeSAGE(List<TaAvoir> idFactureAExporter,List<String> listComplete,Writer fw,
			int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesDocumenstLies) throws Exception {
		int numFin=numDepart;
		try {      


			Boolean gererLesPointages = gererPointages;

//			TaAvoirDAO taAvoirDAO = new TaAvoirDAO(em);
			TaAvoir taAvoir = null;


			exportationEpicea.numeroDePiece = numFin;



			boolean nouvellePiece = false;


			for(int i=0; i<idFactureAExporter.size(); i++) {
				taAvoir = idFactureAExporter.get(i);
				if(listComplete.indexOf(taAvoir.getCodeDocument())==-1){

					/**/   // pour compenser les problèmes d'arrondis mis sur des lignes à 0
					taAvoir.calculeTvaEtTotaux();
					/**/  //isa le 03 février 2011 en accord avec Nicolas

					if(taAvoir.getDateExport()==null || reExport==1) { /* facture pas deja exportee ou reexportation */
						nouvellePiece = true;

						if(nouvellePiece) {
							exportationEpicea.numeroDePiece++;
							nouvellePiece = false;
							numFin++;
						}

						//on crée un nouvel objet facture pour créer la centralisation
						TaAvoir newDocument =new TaAvoir(false);
						newDocument=(TaAvoir) taAvoir.cloneCentralisation();
						newDocument.getLignes().clear();
						
						TaLAvoir newTaLDocument;
						for (TaLAvoir obj : taAvoir.getLignes()) {
							newTaLDocument=newDocument.contientMemeParametreCompte(obj);
							if(newTaLDocument!=null){
								newTaLDocument.setQteLDocument(BigDecimal.ONE);
								if(taAvoir.getTtc()==1) {
									newTaLDocument.setPrixULDocument(newTaLDocument.getPrixULDocument().add(obj.getMtTtcLApresRemiseGlobaleDocument()));
								}else{
									newTaLDocument.setPrixULDocument(newTaLDocument.getPrixULDocument().add(obj.getMtHtLApresRemiseGlobaleDocument()));
								}
							}else{
								if(obj.getTaArticle()!=null){
								newTaLDocument=new TaLAvoir(false);
								newTaLDocument=obj.clone();
								newTaLDocument.setLibLDocument(newDocument.getLibelleDocument());
								newTaLDocument.setQteLDocument(BigDecimal.ONE);
								newTaLDocument.setRemTxLDocument(BigDecimal.ZERO);
								if(taAvoir.getTtc()==1) {
									newTaLDocument.setPrixULDocument(obj.getMtTtcLApresRemiseGlobaleDocument());
								}else{
									newTaLDocument.setPrixULDocument(obj.getMtHtLApresRemiseGlobaleDocument());
								}
								newTaLDocument.setTaDocument(newDocument);
								newDocument.addLigne(newTaLDocument);
								}
							}
						}
						newDocument.setTxRemHtDocument(BigDecimal.ZERO);
						newDocument.calculeTvaEtTotaux();
						
						//recuperation des informations propre a la facture
						exportationEpicea.codePiece = taAvoir.getCodeDocument();
						exportationEpicea.datePiece = taAvoir.getDateDocument();
						exportationEpicea.dateEcheance = taAvoir.getDateEchDocument();
						exportationEpicea.dateLivraisonLigne = taAvoir.getDateDocument();//changer pour erreur 18000 dans epicéa;
						exportationEpicea.libelleLigne = taAvoir.getTaInfosDocument().getNomTiers()+"-"+taAvoir.getCodeDocument();
						exportationEpicea.cptGeneral = LibConversion.stringToInteger(taAvoir.getTaInfosDocument().getCompte());
						exportationEpicea.cptAuxiliaire =taAvoir.getTaInfosDocument().getCodeCompta();   
						exportationEpicea.mtCreditLigne = LibConversion.BigDecimalToDouble(taAvoir.getNetTtcCalc());  

						exportationEpicea.numLignePiece = 1;
						exportationEpicea.journal="VTE";
						exportationEpicea.mtDebitLigne = 0d;

						exportationEpicea.typeReglement="";//initialisation
						if(taAvoir.getTaTiers()!=null && taAvoir.getTaTiers().getTaTPaiement()!=null)
							exportationEpicea.typeReglement=taAvoir.getTaTiers().getTaTPaiement().getCodeTPaiement();
						
						//ecriture de la ligne d'entete dans le fichier d'export
						fw.write(exportationEpicea.creationLigneSAGE(
								exportationEpicea.journal ,
								exportationEpicea.datePiece,
								exportationEpicea.cptGeneral ,
								exportationEpicea.codePiece ,
								exportationEpicea.cptAuxiliaire ,
								exportationEpicea.libelleLigne ,
								exportationEpicea.mtDebitLigne ,
								exportationEpicea.mtCreditLigne ,
								exportationEpicea.dateEcheance,
								exportationEpicea.typeReglement));
						fw.write(exportationEpicea.finDeLigne);


						//traitement des lignes
						for (TaLAvoir lf : newDocument.getLignes()) {
							//récupération des valeurs propres à la ligne                 
//							if(lf.getTaArticle()!=null   && lf.getMtHtLApresRemiseGlobaleDocument()!=null && lf.getMtHtLApresRemiseGlobaleDocument().signum()!=0){
								if(lf.getTaArticle()!=null   && lf.getMtHtLApresRemiseGlobaleDocument()!=null ){
//								exportationEpicea.libelleLigne = lf.getLibLDocument();
								exportationEpicea.cptGeneral = LibConversion.stringToInteger(lf.getCompteLDocument());
								exportationEpicea.cptAuxiliaire ="";
								if(exportationEpicea.cptGeneral==null || exportationEpicea.cptGeneral.equals("")){
									exportationEpicea.setRetour(false);
									exportationEpicea.setMessageRetour("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taAvoir.getCodeDocument());
									throw new Exception("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taAvoir.getCodeDocument());
								}
								
								if(lf.getMtHtLApresRemiseGlobaleDocument().signum()>0) {
									exportationEpicea.mtDebitLigne=LibConversion.BigDecimalToDouble(lf.getMtHtLApresRemiseGlobaleDocument().abs()); 
									exportationEpicea.mtCreditLigne=0d;
								}
								else {
									exportationEpicea.mtDebitLigne=0d; 
									exportationEpicea.mtCreditLigne=LibConversion.BigDecimalToDouble(lf.getMtHtLApresRemiseGlobaleDocument().abs()); 
								}

									
								//ecriture de la ligne d'escompte
								fw.write(exportationEpicea.creationLigneSAGE(
										exportationEpicea.journal ,
										exportationEpicea.datePiece,
										exportationEpicea.cptGeneral ,
										exportationEpicea.codePiece ,
										exportationEpicea.cptAuxiliaire ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.mtDebitLigne ,
										exportationEpicea.mtCreditLigne ,
										exportationEpicea.dateEcheance,
										exportationEpicea.typeReglement));
								fw.write(exportationEpicea.finDeLigne);


							}
						}
					for (LigneTva tva : newDocument.getLignesTVA()) {
						TaTva t=daoTvaDoc.findByCode(tva.getCodeTva());
						if(t!=null) {
							exportationEpicea.cptGeneral = LibConversion.stringToInteger(t.getNumcptTva());
//							exportationEpicea.libelleLigne = t.getLibelleTva();
						}
						exportationEpicea.cptAuxiliaire ="";
						if(exportationEpicea.cptGeneral==null || exportationEpicea.cptGeneral.equals("")){
							exportationEpicea.setRetour(false);
							exportationEpicea.setMessageRetour("\nErreur N° 1\n"
									+"le code tva "+tva.getCodeTva()+" n'a pas de compte comptable dans le document : "+taAvoir.getCodeDocument());
							throw new Exception("\nErreur N° 1\n"
									+"le code tva "+tva.getCodeTva()+" n'a pas de compte comptable dans le document : "+taAvoir.getCodeDocument());
						}
						
						if(tva.getMtTva().signum()>0) {
							exportationEpicea.mtDebitLigne=LibConversion.BigDecimalToDouble(tva.getMtTva().abs()); 
							exportationEpicea.mtCreditLigne=0d;
						}
						else {
							exportationEpicea.mtDebitLigne=0d; 
							exportationEpicea.mtCreditLigne=LibConversion.BigDecimalToDouble(tva.getMtTva().abs()); 
						}
						

							
						//ecriture de la ligne d'escompte
						fw.write(exportationEpicea.creationLigneSAGE(
								exportationEpicea.journal ,
								exportationEpicea.datePiece,
								exportationEpicea.cptGeneral ,
								exportationEpicea.codePiece ,
								exportationEpicea.cptAuxiliaire ,
								exportationEpicea.libelleLigne ,
								exportationEpicea.mtDebitLigne ,
								exportationEpicea.mtCreditLigne ,
								exportationEpicea.dateEcheance,
								exportationEpicea.typeReglement));
						fw.write(exportationEpicea.finDeLigne);
				}						
//						if(gererLesPointages){
//							//traitement des lignes pour les pointages
//							exportationEpicea.typePiece = exportationEpicea.typePointage;
//							for (TaRAvoir lf : taAvoir.getTaRAvoirs()) {
//								//récupération des valeurs propres à la ligne
//								TaFacture fac =lf.getTaFacture();
//								numFin=initPointage(fac, taAvoir, lf, gererLesDocumenstLies, idAppelant, numFin);
//
//							} 
//						}

						exportationEpicea.listComplete.add(taAvoir.getCodeDocument());
						exportationEpicea.listCompleteDoc.add(taAvoir);
						exportationEpicea.listeVerif.add(taAvoir);
					}
				}

			}

		} catch(IOException ioe){
			logger.error("",ioe);
			exportationEpicea.setRetour(false);
		} 
		return numFin;
	}


	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportFactureCentraliseeJPA(List<TaFacture> idFactureAExporter,List<String> listComplete,Writer fw,
			 int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesReglements) throws Exception {
		int numFin=numDepart;
//		TaFactureDAO taFactureDAO = new TaFactureDAO(em);
		try { 


			TaFacture taFacture = null;
//			TaRemiseDAO taRemiseDao = new TaRemiseDAO(em);


			exportationEpicea.numeroDePiece = numFin;


			boolean nouvellePiece = false;

			

			for(int i=0; i<idFactureAExporter.size(); i++) {
				taFacture = idFactureAExporter.get(i);
				if(listComplete.indexOf(taFacture.getCodeDocument())==-1){

					/**/   // pour compenser les problèmes d'arrondis mis sur des lignes à 0
					taFacture.calculeTvaEtTotaux();
					/**/  //isa le 03 février 2011 en accord avec Nicolas


					if(taFacture.getDateExport()==null || reExport==1) { /* facture pas deja exportee ou reexportation */
						nouvellePiece = true;

						if(nouvellePiece) {
							exportationEpicea.numeroDePiece++;
							nouvellePiece = false;
							numFin++;
						}
						//on crée un nouvel objet facture pour créer la centralisation
						TaFacture newFacture =new TaFacture(false);
						newFacture=(TaFacture) taFacture.cloneCentralisation();
						newFacture.getLignes().clear();
						
						TaLFacture newTaLFacture;
						for (TaLFacture obj : taFacture.getLignes()) {
							newTaLFacture=newFacture.contientMemeParametreCompte(obj);
							if(newTaLFacture!=null){
								newTaLFacture.setQteLDocument(BigDecimal.ONE);
								if(taFacture.getTtc()==1) {
									newTaLFacture.setPrixULDocument(newTaLFacture.getPrixULDocument().add(obj.getMtTtcLApresRemiseGlobaleDocument()));
								}else{
									newTaLFacture.setPrixULDocument(newTaLFacture.getPrixULDocument().add(obj.getMtHtLApresRemiseGlobaleDocument()));
								}
							}else{
								if(obj.getTaArticle()!=null){
								newTaLFacture=new TaLFacture(false);
								newTaLFacture=obj.clone();
								newTaLFacture.setLibLDocument(newFacture.getLibelleDocument());
								newTaLFacture.setQteLDocument(BigDecimal.ONE);
								newTaLFacture.setRemTxLDocument(BigDecimal.ZERO);
								if(taFacture.getTtc()==1) {
									newTaLFacture.setPrixULDocument(obj.getMtTtcLApresRemiseGlobaleDocument());
								}else{
									newTaLFacture.setPrixULDocument(obj.getMtHtLApresRemiseGlobaleDocument());
								}
								newTaLFacture.setTaDocument(newFacture);
								newFacture.addLigne(newTaLFacture);
								}
							}
						}
						newFacture.setTxRemHtDocument(BigDecimal.ZERO);
						newFacture.calculeTvaEtTotaux();
						
						//recuperation des informations propre a la facture
						exportationEpicea.codePiece = taFacture.getCodeDocument();
						exportationEpicea.datePiece = taFacture.getDateDocument();
						exportationEpicea.dateEcheance = taFacture.getDateEchDocument();
						exportationEpicea.dateLivraisonLigne = taFacture.getDateDocument();//changer pour erreur 18000 dans epicéa;
						exportationEpicea.libelleLigne = taFacture.getTaInfosDocument().getNomTiers();
						exportationEpicea.numCptLigne = taFacture.getTaInfosDocument().getCodeCompta();
						exportationEpicea.cptCollectif = taFacture.getTaInfosDocument().getCompte();
						exportationEpicea.nomTiers = taFacture.getTaInfosDocument().getNomTiers();
						exportationEpicea.adresse1Tiers = taFacture.getTaInfosDocument().getAdresse1();
						exportationEpicea.adresse2Tiers = taFacture.getTaInfosDocument().getAdresse2();
						exportationEpicea.codePostalTiers = taFacture.getTaInfosDocument().getCodepostal();
						exportationEpicea.villeTiers = taFacture.getTaInfosDocument().getVille();

						exportationEpicea.mtDebitLigne = LibConversion.BigDecimalToDouble(taFacture.getNetTtcCalc());  

						exportationEpicea.numCptLigne = "+"+taFacture.getTaInfosDocument().getCodeCompta();    
						exportationEpicea.numLignePiece = 1;
						exportationEpicea.typePiece = exportationEpicea.TypeVente;
						exportationEpicea.mtCreditLigne = 0d;
						exportationEpicea.qte1 = 0d;
						exportationEpicea.qte2 = 0d;
						exportationEpicea.codeTva ="";
						exportationEpicea.tauxTva = 0d;
						exportationEpicea.mtDebitTva = 0d;
						exportationEpicea.mtCreditTva = 0d;
						exportationEpicea.exigibiliteTva = "";

						exportationEpicea.refContrepartie = "";
						exportationEpicea.dateContrepartie = null;
						exportationEpicea.mtContrepartieDebit = 0d;
						exportationEpicea.mtContrepartieCredit = 0d;
						exportationEpicea.mtAffectation = 0d;
						exportationEpicea.codeTvaComplet = "";
						exportationEpicea.refReglement = "";

						//ecriture de la ligne d'entete dans le fichier d'export
						fw.write(exportationEpicea.creationLigne(exportationEpicea.numeroDePiece,
								exportationEpicea.numLignePiece ,
								exportationEpicea.typePiece ,
								exportationEpicea.codePiece ,
								exportationEpicea.datePiece ,
								exportationEpicea.numCptLigne ,
								exportationEpicea.libelleLigne ,
								exportationEpicea.mtDebitLigne ,
								exportationEpicea.mtCreditLigne ,
								exportationEpicea.qte1 ,
								exportationEpicea.qte2 ,
								exportationEpicea.codeTva ,
								exportationEpicea.tauxTva ,
								exportationEpicea.mtDebitTva ,
								exportationEpicea.mtCreditTva ,
								exportationEpicea.dateEcheance ,
								exportationEpicea.cptCollectif ,
								exportationEpicea.nomTiers ,
								exportationEpicea.adresse1Tiers ,
								exportationEpicea.adresse2Tiers ,
								exportationEpicea.codePostalTiers ,
								exportationEpicea.villeTiers ,
								exportationEpicea.exigibiliteTva ,
								exportationEpicea.dateLivraisonLigne,
								exportationEpicea.refContrepartie,
								exportationEpicea.dateContrepartie,
								exportationEpicea.mtContrepartieDebit,
								exportationEpicea.mtContrepartieCredit,
								exportationEpicea.mtAffectation,
								exportationEpicea.codeTvaComplet,
								exportationEpicea.refReglement));
						fw.write(exportationEpicea.finDeLigne);

						//réinitialisation des variables pour le prochain document
						exportationEpicea.cptCollectif ="";
						exportationEpicea.nomTiers ="";
						exportationEpicea.adresse1Tiers ="";
						exportationEpicea.adresse2Tiers ="";
						exportationEpicea.codePostalTiers ="";
						exportationEpicea.villeTiers="";

						BigDecimal escompte = taFacture.getRemTtcDocument();
						if (escompte.signum() > 0) { 
							//création d'une lignes d'escompte
							exportationEpicea.numCptLigne = exportationEpicea.ctpEscompteDebit;
							exportationEpicea.numLignePiece = exportationEpicea.numLignePiece + 1;
							exportationEpicea.typePiece = "V";
							exportationEpicea.libelleLigne = "Escompte "+taFacture.getLibelleDocument();
							exportationEpicea.mtDebitLigne = LibConversion.BigDecimalToDouble(escompte);
							exportationEpicea.mtCreditLigne = 0d;
							exportationEpicea.qte1 = 0d;
							exportationEpicea.qte2 = 0d;
							exportationEpicea.codeTva = "";
							exportationEpicea.tauxTva = 0d;
							exportationEpicea.mtDebitTva = 0d;
							exportationEpicea.mtCreditTva = 0d;
							exportationEpicea.exigibiliteTva = "";
							//ecriture de la ligne d'escompte dans le fichier d'export

							exportationEpicea.refContrepartie = "";
							exportationEpicea.dateContrepartie = null;
							exportationEpicea.mtContrepartieDebit = 0d;
							exportationEpicea.mtContrepartieCredit = 0d;
							exportationEpicea.mtAffectation = 0d;
							exportationEpicea.codeTvaComplet = "";
							exportationEpicea.refReglement = "";

							fw.write(exportationEpicea.creationLigne(exportationEpicea.numeroDePiece,
									exportationEpicea.numLignePiece ,
									exportationEpicea.typePiece ,
									exportationEpicea.codePiece ,
									exportationEpicea.datePiece ,
									exportationEpicea.numCptLigne ,
									exportationEpicea.libelleLigne ,
									exportationEpicea.mtDebitLigne ,
									exportationEpicea.mtCreditLigne ,
									exportationEpicea.qte1 ,
									exportationEpicea.qte2 ,
									exportationEpicea.codeTva ,
									exportationEpicea.tauxTva ,
									exportationEpicea.mtDebitTva ,
									exportationEpicea.mtCreditTva ,
									exportationEpicea.dateEcheance ,
									exportationEpicea.cptCollectif ,
									exportationEpicea.nomTiers ,
									exportationEpicea.adresse1Tiers ,
									exportationEpicea.adresse2Tiers ,
									exportationEpicea.codePostalTiers ,
									exportationEpicea.villeTiers ,
									exportationEpicea.exigibiliteTva ,
									exportationEpicea.dateLivraisonLigne,
									exportationEpicea.refContrepartie,
									exportationEpicea.dateContrepartie,
									exportationEpicea.mtContrepartieDebit,
									exportationEpicea.mtContrepartieCredit,
									exportationEpicea.mtAffectation,
									exportationEpicea.codeTvaComplet,
									exportationEpicea.refReglement));
							fw.write(exportationEpicea.finDeLigne);
						}

						//traitement des lignes
						for (TaLFacture lf : newFacture.getLignes()) {
							//récupération des valeurs propres à la ligne 
//							if(lf.getTaArticle()!=null   && lf.getMtHtLApresRemiseGlobaleDocument()!=null && lf.getMtHtLApresRemiseGlobaleDocument().signum()!=0){
							if(lf.getTaArticle()!=null   && lf.getMtHtLApresRemiseGlobaleDocument()!=null ){
								exportationEpicea.libelleLigne = lf.getLibLDocument();
								exportationEpicea.qte1 = LibConversion.BigDecimalToDouble(lf.getQteLDocument());
								exportationEpicea.qte2 = LibConversion.BigDecimalToDouble(lf.getQte2LDocument());
								exportationEpicea.numCptLigne = lf.getCompteLDocument();
								if(exportationEpicea.numCptLigne==null || exportationEpicea.numCptLigne.equals("")){
									exportationEpicea.setRetour(false);
									exportationEpicea.setMessageRetour("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taFacture.getCodeDocument());
									throw new Exception("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taFacture.getCodeDocument());
								}
								
								//traitement du type de tva du tiers (F ou ue ou horsUE ou Franchise)
//									TaTTvaDocDAO daoTvaDoc = new TaTTvaDocDAO(em);
									String codeDoc="";
									if(taFacture.getTaInfosDocument().getCodeTTvaDoc()==null || taFacture.getTaInfosDocument().getCodeTTvaDoc().equals(""))
										codeDoc="F";
									else codeDoc=taFacture.getTaInfosDocument().getCodeTTvaDoc();
									TaTTvaDoc tvaDoc =daoTTvaDoc.findByCode(codeDoc);
									String journalTva="";
									if(tvaDoc!=null)journalTva=tvaDoc.getJournalTTvaDoc();
									//si Franchise pas de code tva sur ligne sans code
									// si autre, L ou E sur compte 7 sauf compte 609
									if(codeDoc.equalsIgnoreCase("F"))
										exportationEpicea.codeTva = exportationEpicea.correspondanceCodeTva(codeDoc,lf.getCodeTvaLDocument(),lf.getCodeTvaLDocument(),exportationEpicea.numCptLigne,lf.getMtTtcLApresRemiseGlobaleDocument().subtract(lf.getMtHtLApresRemiseGlobaleDocument()));
									else
										exportationEpicea.codeTva = exportationEpicea.correspondanceCodeTva(codeDoc,lf.getCodeTvaLDocument(),journalTva,exportationEpicea.numCptLigne,lf.getMtTtcLApresRemiseGlobaleDocument().subtract(lf.getMtHtLApresRemiseGlobaleDocument()));	
	   								exportationEpicea.codeTvaComplet=lf.getCodeTvaLDocument();
									
								if(exportationEpicea.codeTva==null)exportationEpicea.codeTva="";
								if(lf.getTaArticle().getTaTTva()!=null)  
									exportationEpicea.exigibiliteTva = lf.getTaArticle().getTaTTva().getCodeTTva();
								if (exportationEpicea.exigibiliteTva.equals(""))exportationEpicea.exigibiliteTva="D";
								exportationEpicea.tauxTva = LibConversion.BigDecimalToDouble(lf.getTauxTvaLDocument());
								exportationEpicea.mtCreditTva = LibConversion.BigDecimalToDouble(lf.getMtTtcLApresRemiseGlobaleDocument().subtract(lf.getMtHtLApresRemiseGlobaleDocument()));
								exportationEpicea.mtCreditLigne = LibConversion.BigDecimalToDouble(lf.getMtHtLApresRemiseGlobaleDocument());

								exportationEpicea.numLignePiece = exportationEpicea.numLignePiece + 1;
								exportationEpicea.mtDebitLigne = 0d;
								exportationEpicea.mtDebitTva = 0d;
								exportationEpicea.cptCollectif = "";
								exportationEpicea.nomTiers = "";
								exportationEpicea.adresse1Tiers = "";
								exportationEpicea.adresse2Tiers = "";
								exportationEpicea.codePostalTiers = "";
								exportationEpicea.villeTiers = "";
								//ecriture de la ligne dans le fichier d'export

								exportationEpicea.refContrepartie = "";
								exportationEpicea.dateContrepartie = null;
								exportationEpicea.mtContrepartieDebit = 0d;
								exportationEpicea.mtContrepartieCredit = 0d;
								exportationEpicea.mtAffectation = 0d;
								exportationEpicea.refReglement = "";

								fw.write(exportationEpicea.creationLigne(exportationEpicea.numeroDePiece,
										exportationEpicea.numLignePiece ,
										exportationEpicea.typePiece ,
										exportationEpicea.codePiece ,
										exportationEpicea.datePiece ,
										exportationEpicea.numCptLigne ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.mtDebitLigne ,
										exportationEpicea.mtCreditLigne ,
										exportationEpicea.qte1 ,
										exportationEpicea.qte2 ,
										exportationEpicea.codeTva ,
										exportationEpicea.tauxTva ,
										exportationEpicea.mtDebitTva ,
										exportationEpicea.mtCreditTva ,
										exportationEpicea.dateEcheance ,
										exportationEpicea.cptCollectif ,
										exportationEpicea.nomTiers ,
										exportationEpicea.adresse1Tiers ,
										exportationEpicea.adresse2Tiers ,
										exportationEpicea.codePostalTiers ,
										exportationEpicea.villeTiers ,
										exportationEpicea.exigibiliteTva ,
										exportationEpicea.dateLivraisonLigne,
										exportationEpicea.refContrepartie,
										exportationEpicea.dateContrepartie,
										exportationEpicea.mtContrepartieDebit,
										exportationEpicea.mtContrepartieCredit,
										exportationEpicea.mtAffectation,
										exportationEpicea.codeTvaComplet,
										exportationEpicea.refReglement));
								fw.write(exportationEpicea.finDeLigne);
							}
						}

						if(gererLesReglements){

							List<TaRemise> listeRemise = new LinkedList<TaRemise>();
							//récupérer tous les documents directement liés à cette facture
							boolean priseEnChargeAcomptes=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.ACOMPTES);
							boolean priseEnChargeReglementSimple=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.REGLEMENT_SIMPLE);
							boolean priseEnChargeRemise=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.REGLEMENT_SIMPLE);
//							if(priseEnChargeAcomptes){
//								for (TaRAcompte rAcompte : taFacture.getTaRAcomptes()) {
//									List<TaAcompte> liste=new LinkedList<TaAcompte>();
//									List<TaRemise> listeRemiseAcompte =new LinkedList<TaRemise>();
//									listeRemiseAcompte=daoRemise.findSiAcompteDansRemise(rAcompte.getTaAcompte().getCodeDocument());
//									if(listeRemiseAcompte.size()==0 && priseEnChargeReglementSimple){
//										liste.add(rAcompte.getTaAcompte());
//										numFin=exportAcompteJPA(liste, listComplete, fw,  exportationEpicea.getReExportAcompte(), numFin, gererPointages,taFacture.getIdDocument(),false);
//									}else{
//										for (TaRemise taRemise : listeRemiseAcompte) {
//											if(listeRemise.indexOf(taRemise)==-1 )
//												listeRemise.add(taRemise);
//										}
//									}
//								}	
//							}
							for (TaRReglement rReglement : taFacture.getTaRReglements()) {
								List<TaReglement> liste=new LinkedList<TaReglement>();
								List<TaRemise> listeRemiseReglement =new LinkedList<TaRemise>();
								listeRemiseReglement=daoRemise.findSiReglementDansRemise(rReglement.getTaReglement().getCodeDocument());


								if(listeRemiseReglement.size()==0 && priseEnChargeReglementSimple){
									liste.add(rReglement.getTaReglement());
									numFin=exportReglementJPA(liste, listComplete, fw,  exportationEpicea.getReExportReglement(), numFin, gererPointages,
											taFacture.getIdDocument(),false,taFacture);
								}else{
									for (TaRemise taRemise : listeRemiseReglement) {
										if(listeRemise.indexOf(taRemise)==-1 )
											listeRemise.add(taRemise);
									}
								}

							}
							/**** Atention Cette partie est rajoutée temporaire jusqu'à gestion des règlements multiples*/
							if (taFacture.getTaReglement()!=null) {
								List<TaReglement> liste=new LinkedList<TaReglement>();
								List<TaRemise> listeRemiseReglement =new LinkedList<TaRemise>();
								listeRemiseReglement=daoRemise.findSiReglementDansRemise(taFacture.getTaReglement().getCodeDocument());


								if(listeRemiseReglement.size()==0 && priseEnChargeReglementSimple){
									liste.add(taFacture.getTaReglement());
									numFin=exportReglementJPA(liste, listComplete, fw,  exportationEpicea.getReExportReglement(), numFin, gererPointages,
											taFacture.getIdDocument(),false,taFacture);
								}else{
									for (TaRemise taRemise : listeRemiseReglement) {
										if(listeRemise.indexOf(taRemise)==-1 )
											listeRemise.add(taRemise);
									}
								}

							}							
							if(priseEnChargeRemise){
								if(listeRemise.size()>0){
									numFin=exportRemiseJPA(listeRemise, listComplete, fw,  exportationEpicea.getReExportRemise(), numFin, gererPointages, 
											taFacture.getIdDocument(),false);
								}
							}
						}else if(gererPointages){
//							for (TaRAcompte r : taFacture.getTaRAcomptes()) {
//								numFin=initPointage(taFacture, r.getTaAcompte(), r, false, idAppelant, numFin);
//							}
							for (TaRAvoir r : taFacture.getTaRAvoirs()) {
								numFin=initPointage(taFacture, r.getTaAvoir(), r, false, idAppelant, numFin);
							}
							for (TaRReglement r : taFacture.getTaRReglements()) {
								numFin=initPointage(taFacture, r.getTaReglement(), r, false, idAppelant, numFin);
							}
							if(taFacture.getTaReglement()!=null) {
								IRelationDoc r=new TaRReglement();
								r.setAffectation(taFacture.getTaReglement().getNetTtcCalc());
								numFin=initPointage(taFacture, taFacture.getTaReglement(), r, false, idAppelant, numFin);
							}							
						}
							
//						if(taFacture.getExport()!=1)taFacture.setExport(1);
//						dao.merge(taFacture);
						listComplete.add(taFacture.getCodeDocument());

						exportationEpicea.listCompleteDoc.add(taFacture);
						exportationEpicea.listeVerif.add(taFacture);
					}
				}

			}

		} catch(IOException ioe){
			logger.error("",ioe);
			exportationEpicea.setRetour(false);
		} 
		return numFin;
	}

	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportFactureCentraliseeSAGE(List<TaFacture> idFactureAExporter,List<String> listComplete,Writer fw,
			 int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesReglements) throws Exception {
		int numFin=numDepart;
		try { 


			TaFacture taFacture = null;


			exportationEpicea.numeroDePiece = numFin;


			boolean nouvellePiece = false;

			

			for(int i=0; i<idFactureAExporter.size(); i++) {
				taFacture = idFactureAExporter.get(i);
				if(listComplete.indexOf(taFacture.getCodeDocument())==-1){

					/**/   // pour compenser les problèmes d'arrondis mis sur des lignes à 0
					taFacture.calculeTvaEtTotaux();
					/**/  //isa le 03 février 2011 en accord avec Nicolas


					if(taFacture.getDateExport()==null || reExport==1) { /* facture pas deja exportee ou reexportation */
						nouvellePiece = true;

						if(nouvellePiece) {
							exportationEpicea.numeroDePiece++;
							nouvellePiece = false;
							numFin++;
						}
						//on crée un nouvel objet facture pour créer la centralisation
						TaFacture newFacture =new TaFacture(false);
						newFacture=(TaFacture) taFacture.cloneCentralisation();
						newFacture.getLignes().clear();
						
						TaLFacture newTaLFacture;
						for (TaLFacture obj : taFacture.getLignes()) {
							newTaLFacture=newFacture.contientMemeParametreCompte(obj);
							if(newTaLFacture!=null){
								newTaLFacture.setQteLDocument(BigDecimal.ONE);
								if(taFacture.getTtc()==1) {
									newTaLFacture.setPrixULDocument(newTaLFacture.getPrixULDocument().add(obj.getMtTtcLApresRemiseGlobaleDocument()));
								}else{
									newTaLFacture.setPrixULDocument(newTaLFacture.getPrixULDocument().add(obj.getMtHtLApresRemiseGlobaleDocument()));
								}
							}else{
								if(obj.getTaArticle()!=null){
								newTaLFacture=new TaLFacture(false);
								newTaLFacture=obj.clone();
								newTaLFacture.setLibLDocument(newFacture.getLibelleDocument());
								newTaLFacture.setQteLDocument(BigDecimal.ONE);
								newTaLFacture.setRemTxLDocument(BigDecimal.ZERO);
								if(taFacture.getTtc()==1) {
									newTaLFacture.setPrixULDocument(obj.getMtTtcLApresRemiseGlobaleDocument());
								}else{
									newTaLFacture.setPrixULDocument(obj.getMtHtLApresRemiseGlobaleDocument());
								}
								newTaLFacture.setTaDocument(newFacture);
								newFacture.addLigne(newTaLFacture);
								}
							}
						}
						newFacture.setTxRemHtDocument(BigDecimal.ZERO);
						newFacture.calculeTvaEtTotaux();
						
						//recuperation des informations propre a la facture
						exportationEpicea.codePiece = taFacture.getCodeDocument();
						exportationEpicea.datePiece = taFacture.getDateDocument();
						exportationEpicea.dateEcheance = taFacture.getDateEchDocument();
						exportationEpicea.libelleLigne = taFacture.getTaInfosDocument().getNomTiers()+"-"+taFacture.getCodeDocument();
						exportationEpicea.cptGeneral = LibConversion.stringToInteger(taFacture.getTaInfosDocument().getCompte());
						exportationEpicea.cptAuxiliaire =taFacture.getTaInfosDocument().getCodeCompta();   
						exportationEpicea.mtDebitLigne = LibConversion.BigDecimalToDouble(taFacture.getNetTtcCalc());  

						exportationEpicea.numLignePiece = 1;
//						exportationEpicea.typePiece = exportationEpicea.TypeVente;
						exportationEpicea.journal="VTE";
						exportationEpicea.mtCreditLigne = 0d;

						exportationEpicea.typeReglement="";//initialisation
						if(taFacture.getTaTiers()!=null && taFacture.getTaTiers().getTaTPaiement()!=null)
							exportationEpicea.typeReglement=taFacture.getTaTiers().getTaTPaiement().getCodeTPaiement();
						
						//ecriture de la ligne d'entete dans le fichier d'export
						fw.write(exportationEpicea.creationLigneSAGE(
								exportationEpicea.journal ,
								exportationEpicea.datePiece,
								exportationEpicea.cptGeneral ,
								exportationEpicea.codePiece ,
								exportationEpicea.cptAuxiliaire ,
								exportationEpicea.libelleLigne ,
								exportationEpicea.mtDebitLigne ,
								exportationEpicea.mtCreditLigne ,
								exportationEpicea.dateEcheance,
								exportationEpicea.typeReglement));
						fw.write(exportationEpicea.finDeLigne);


						BigDecimal escompte = taFacture.getRemTtcDocument();
						if (escompte.signum() > 0) { 
							//création d'une lignes d'escompte
							exportationEpicea.cptGeneral = LibConversion.stringToInteger(exportationEpicea.ctpEscompteDebit);
//							exportationEpicea.libelleLigne = "Escompte "+taFacture.getLibelleDocument();
							exportationEpicea.cptAuxiliaire ="";

							if(escompte.signum()>0) {
								exportationEpicea.mtDebitLigne=LibConversion.BigDecimalToDouble(escompte.abs()); 
								exportationEpicea.mtCreditLigne=0d;
							}
							else {
								exportationEpicea.mtDebitLigne=0d; 
								exportationEpicea.mtCreditLigne=LibConversion.BigDecimalToDouble(escompte.abs()); 
							}
							
							//ecriture de la ligne d'escompte dans le fichier d'export

							fw.write(exportationEpicea.creationLigneSAGE(
									exportationEpicea.journal ,
									exportationEpicea.datePiece,
									exportationEpicea.cptGeneral ,
									exportationEpicea.codePiece ,
									exportationEpicea.cptAuxiliaire ,
									exportationEpicea.libelleLigne ,
									exportationEpicea.mtDebitLigne ,
									exportationEpicea.mtCreditLigne ,
									exportationEpicea.dateEcheance,
									exportationEpicea.typeReglement));
							fw.write(exportationEpicea.finDeLigne);
						}

						//traitement des lignes
						for (TaLFacture lf : newFacture.getLignes()) {
								//récupération des valeurs propres à la ligne                 
//								if(lf.getTaArticle()!=null   && lf.getMtHtLApresRemiseGlobaleDocument()!=null && lf.getMtHtLApresRemiseGlobaleDocument().signum()!=0){
									if(lf.getTaArticle()!=null   && lf.getMtHtLApresRemiseGlobaleDocument()!=null ){
//									exportationEpicea.libelleLigne = lf.getLibLDocument();
									exportationEpicea.cptGeneral = LibConversion.stringToInteger(lf.getCompteLDocument());
									exportationEpicea.cptAuxiliaire ="";
									if(exportationEpicea.cptGeneral==null || exportationEpicea.cptGeneral.equals("")){
										exportationEpicea.setRetour(false);
										exportationEpicea.setMessageRetour("\nErreur N° 1\n"
												+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taFacture.getCodeDocument());
										throw new Exception("\nErreur N° 1\n"
												+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taFacture.getCodeDocument());
									}
									
									if(lf.getMtHtLApresRemiseGlobaleDocument().signum()>0) {
										exportationEpicea.mtDebitLigne=0d;
										exportationEpicea.mtCreditLigne=LibConversion.BigDecimalToDouble(lf.getMtHtLApresRemiseGlobaleDocument().abs()); 
									}
									else {
										exportationEpicea.mtDebitLigne=LibConversion.BigDecimalToDouble(lf.getMtHtLApresRemiseGlobaleDocument().abs()); 
										exportationEpicea.mtCreditLigne=0d;
									}

										
									//ecriture de la ligne d'escompte
									fw.write(exportationEpicea.creationLigneSAGE(
											exportationEpicea.journal ,
											exportationEpicea.datePiece,
											exportationEpicea.cptGeneral ,
											exportationEpicea.codePiece ,
											exportationEpicea.cptAuxiliaire ,
											exportationEpicea.libelleLigne ,
											exportationEpicea.mtDebitLigne ,
											exportationEpicea.mtCreditLigne ,
											exportationEpicea.dateEcheance,
											exportationEpicea.typeReglement));
									fw.write(exportationEpicea.finDeLigne);


								}
							}
						for (LigneTva tva : newFacture.getLignesTVA()) {
							TaTva t=daoTvaDoc.findByCode(tva.getCodeTva());
							if(t!=null) {
								exportationEpicea.cptGeneral = LibConversion.stringToInteger(t.getNumcptTva());
//								exportationEpicea.libelleLigne = t.getLibelleTva();
							}
							exportationEpicea.cptAuxiliaire ="";
							if(exportationEpicea.cptGeneral==null || exportationEpicea.cptGeneral.equals("")){
								exportationEpicea.setRetour(false);
								exportationEpicea.setMessageRetour("\nErreur N° 1\n"
										+"le code tva "+tva.getCodeTva()+" n'a pas de compte comptable dans le document : "+taFacture.getCodeDocument());
								throw new Exception("\nErreur N° 1\n"
										+"le code tva "+tva.getCodeTva()+" n'a pas de compte comptable dans le document : "+taFacture.getCodeDocument());
							}
							
							if(tva.getMtTva().signum()>0) {
								exportationEpicea.mtDebitLigne=0d;
								exportationEpicea.mtCreditLigne=LibConversion.BigDecimalToDouble(tva.getMtTva().abs()); 
							}
							else {
								exportationEpicea.mtDebitLigne=LibConversion.BigDecimalToDouble(tva.getMtTva().abs()); 
								exportationEpicea.mtCreditLigne=0d;
							}
							

								
							//ecriture de la ligne d'escompte
							fw.write(exportationEpicea.creationLigneSAGE(
									exportationEpicea.journal ,
									exportationEpicea.datePiece,
									exportationEpicea.cptGeneral ,
									exportationEpicea.codePiece ,
									exportationEpicea.cptAuxiliaire ,
									exportationEpicea.libelleLigne ,
									exportationEpicea.mtDebitLigne ,
									exportationEpicea.mtCreditLigne ,
									exportationEpicea.dateEcheance,
									exportationEpicea.typeReglement));
							fw.write(exportationEpicea.finDeLigne);
					}						

//						if(gererLesReglements){
//
//							List<TaRemise> listeRemise = new LinkedList<TaRemise>();
//							//récupérer tous les documents directement liés à cette facture
//							boolean priseEnChargeAcomptes=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.ACOMPTES);
//							boolean priseEnChargeReglementSimple=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.REGLEMENT_SIMPLE);
//							boolean priseEnChargeRemise=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.REGLEMENT_SIMPLE);
//
//							for (TaRReglement rReglement : taFacture.getTaRReglements()) {
//								List<TaReglement> liste=new LinkedList<TaReglement>();
//								List<TaRemise> listeRemiseReglement =new LinkedList<TaRemise>();
//								listeRemiseReglement=daoRemise.findSiReglementDansRemise(rReglement.getTaReglement().getCodeDocument());
//
//
//								if(listeRemiseReglement.size()==0 && priseEnChargeReglementSimple){
//									liste.add(rReglement.getTaReglement());
//									numFin=exportReglementJPA(liste, listComplete, fw,  exportationEpicea.getReExportReglement(), numFin, gererPointages,
//											taFacture.getIdDocument(),false,taFacture);
//								}else{
//									for (TaRemise taRemise : listeRemiseReglement) {
//										if(listeRemise.indexOf(taRemise)==-1 )
//											listeRemise.add(taRemise);
//									}
//								}
//
//							}
//							/**** Atention Cette partie est rajoutée temporaire jusqu'à gestion des règlements multiples*/
//							if (taFacture.getTaReglement()!=null) {
//								List<TaReglement> liste=new LinkedList<TaReglement>();
//								List<TaRemise> listeRemiseReglement =new LinkedList<TaRemise>();
//								listeRemiseReglement=daoRemise.findSiReglementDansRemise(taFacture.getTaReglement().getCodeDocument());
//
//
//								if(listeRemiseReglement.size()==0 && priseEnChargeReglementSimple){
//									liste.add(taFacture.getTaReglement());
//									numFin=exportReglementJPA(liste, listComplete, fw,  exportationEpicea.getReExportReglement(), numFin, gererPointages,
//											taFacture.getIdDocument(),false,taFacture);
//								}else{
//									for (TaRemise taRemise : listeRemiseReglement) {
//										if(listeRemise.indexOf(taRemise)==-1 )
//											listeRemise.add(taRemise);
//									}
//								}
//
//							}							
//							if(priseEnChargeRemise){
//								if(listeRemise.size()>0){
//									numFin=exportRemiseJPA(listeRemise, listComplete, fw,  exportationEpicea.getReExportRemise(), numFin, gererPointages, 
//											taFacture.getIdDocument(),false);
//								}
//							}
//						}else if(gererPointages){
////							for (TaRAcompte r : taFacture.getTaRAcomptes()) {
////								numFin=initPointage(taFacture, r.getTaAcompte(), r, false, idAppelant, numFin);
////							}
//							for (TaRAvoir r : taFacture.getTaRAvoirs()) {
//								numFin=initPointage(taFacture, r.getTaAvoir(), r, false, idAppelant, numFin);
//							}
//							for (TaRReglement r : taFacture.getTaRReglements()) {
//								numFin=initPointage(taFacture, r.getTaReglement(), r, false, idAppelant, numFin);
//							}
//							if(taFacture.getTaReglement()!=null) {
//								IRelationDoc r=new TaRReglement();
//								r.setAffectation(taFacture.getTaReglement().getNetTtcCalc());
//								numFin=initPointage(taFacture, taFacture.getTaReglement(), r, false, idAppelant, numFin);
//							}							
//						}
							

						listComplete.add(taFacture.getCodeDocument());

						exportationEpicea.listCompleteDoc.add(taFacture);
						exportationEpicea.listeVerif.add(taFacture);
					}
				}

			}

		} catch(IOException ioe){
			logger.error("",ioe);
			exportationEpicea.setRetour(false);
		} 
		return numFin;
	}

	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public ExportationEpicea exportJPA(List<TaFacture> idFactureAExporter,List<TaAvoir> idAvoirAExporter,List<TaApporteur> idApporteurAExporter,List<TaAcompte> idAcompteAExporter,
			List<TaReglement> idReglementAExporter,List<TaRemise> idRemiseAExporter, 
			int reExportFacture,int reExportAvoir,int reExportApporteur,int reExportAcompte,int reExportReglement,
			int reExportRemise,boolean gererPointages, boolean centralisation,boolean ecraserFichier,String typeClient) throws Exception {
		int nb=0;
		exportationEpicea=new ExportationEpicea(typeClient);
		if(idFactureAExporter!=null)nb=idFactureAExporter.size();
		if(idAvoirAExporter!=null)nb=idAvoirAExporter.size();
		if(idApporteurAExporter!=null)nb=idApporteurAExporter.size();
		if(idAcompteAExporter!=null)nb=idAcompteAExporter.size();
		if(idReglementAExporter!=null)nb=idReglementAExporter.size();
		if(idRemiseAExporter!=null)nb=idRemiseAExporter.size();
		String prefRepertoire =PreferencesService.retourPreferenceString(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.REPERTOIRE_EXPORTATION);
		if(nb==0)exportationEpicea.setMessageRetour("Il n'y a aucun document à exporter !!!");
		String tmp=System.getProperty("java.io.tmpdir");
		BdgProperties bdgProperties = new BdgProperties();
//		String localPath = bdgProperties.osTempDirDossier(tenantInfo.getTenantId())+"/"+bdgProperties.tmpFileName(taEdition.getFichierNom());
		if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_ISAGRIFINAL)) {
			exportationEpicea.setFichierExportationServeur(bdgProperties.osTempDirDossier(tenantInfo.getTenantId())+"/ISAEXPORT.TXT");
			exportationEpicea.setFichierExportation(bdgProperties.osTempDirDossier(tenantInfo.getTenantId())+"/ISAEXPORT.TXT");
		}
		if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_CGID)) {
			exportationEpicea.setFichierExportationServeur(bdgProperties.osTempDirDossier(tenantInfo.getTenantId())+"/E2-IMPOR.CSV");
			exportationEpicea.setFichierExportation(bdgProperties.osTempDirDossier(tenantInfo.getTenantId())+"/E2-IMPOR.CSV");
		}
		else if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_SAGE)) {
			exportationEpicea.setFichierExportationServeur(bdgProperties.osTempDirDossier(tenantInfo.getTenantId())+"/export_bdg_sage.CSV");
			exportationEpicea.setFichierExportation(bdgProperties.osTempDirDossier(tenantInfo.getTenantId())+"/export_bdg_sage.CSV");
		}
		else {
			exportationEpicea.setFichierExportationServeur(bdgProperties.osTempDirDossier(tenantInfo.getTenantId())+"/E2-IMPOR.TXT");
			exportationEpicea.setFichierExportation(bdgProperties.osTempDirDossier(tenantInfo.getTenantId())+"/E2-IMPOR.TXT");
			System.out.print("exportationEpicea.setFichierExportation : "+exportationEpicea.getFichierExportation());
			System.out.print("exportationEpicea.setFichierExportationServeur : "+exportationEpicea.getFichierExportationServeur());
		}
		 journalFacture =PreferencesService.retourPreferenceString(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.JOURNAL_FACTURE);
		 journalAvoir =PreferencesService.retourPreferenceString(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.JOURNAL_AVOIR);
		 journalApporteur =PreferencesService.retourPreferenceString(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.JOURNAL_APPORTEUR);
		 journalReglement =PreferencesService.retourPreferenceString(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.JOURNAL_REGLEMENT);
		 journalRemise =PreferencesService.retourPreferenceString(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.JOURNAL_REMISE);


		
		final boolean annuler[] = new boolean[]{false};
//		if(new File(fichierExportation).exists()) {
//			//Le fichier existe déjà
//			PlatformUI.getWorkbench().getDisplay().syncExec( new Runnable() {
//
//					@Override
//					public void run() {
//						annuler[0] = ! MessageDialog.openQuestion(
//									PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
//									"Bureau de gestion",
////									"Attention, des documents sont en attente d'importation en comptabilité.\n" +
////									"Si vous continuez l'exportation, la liste des documents déjà en attente d'importation sera réinitialisée.\n\n" +
////									"Annuler l'exportation en cours ?");
//									"Attention, des documents sont en attente d'importation en comptabilité.\n" +
//									"Si vous continuez l'exportation en cours, cette liste de documents en attente\n" +
//									"d'importation sera supprimée.\n" +
//									"\n" +
//									"Continuer l'exportation en cours ?");
//						
//					}
//					} );
//			
//		}
		
		if(!annuler[0]) {
			
			fw = null;
			bw = null;

	
			try {      
				String ctpEscompteDebit = "665";
				String ctpEscompteCredit = "765";
				exportationEpicea.setReExportAcompte(reExportAcompte);
				exportationEpicea.setReExportAvoir(reExportAvoir);
				exportationEpicea.setReExportFacture(reExportFacture);
				exportationEpicea.setReExportReglement(reExportReglement);
				exportationEpicea.setReExportRemise(reExportRemise);
	
				boolean reglementLies=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.REGLEMENTS_LIES);
				boolean documentLies=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.REGLEMENTS_LIES);
				
				File reportFile = new File(exportationEpicea.getFichierExportationServeur());
				if(ecraserFichier)exportationEpicea.effaceFichierTexte(exportationEpicea.getFichierExportationServeur());
				fw=new FileWriter(reportFile,!ecraserFichier);
				if(reportFile.exists() && !ecraserFichier){
					//on récupère le dernier numéro pour continuer l'incrémentation
					BufferedReader br = new BufferedReader(new FileReader(reportFile));
					String line;
					String line2 = null;
					while ((line = br.readLine()) != null) {
						line2=line;
					}
					if(line2!=null)
						exportationEpicea.setNumDepart(LibConversion.stringToInteger(line2.substring(1, 5).trim()));
					br.close();
				}else exportationEpicea.setNumDepart(0);

				exportationEpicea.setLocationFichier(reportFile.getAbsolutePath());
				bw=new BufferedWriter(fw);
				fw = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(new File(reportFile.getAbsolutePath())), "ISO-8859-1"));
				
				
				if(idFactureAExporter!=null) {
					if(centralisation){
						if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_ISAGRIFINAL))
							exportationEpicea.numDepart=exportFactureCentraliseeISAGRIFINAL(idFactureAExporter,exportationEpicea.listComplete, fw,  reExportFacture,exportationEpicea.numDepart,gererPointages,null,
									reglementLies);
						else
							if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_ISAGRI1))
								exportationEpicea.numDepart=exportFactureCentraliseeISAGRI1(idFactureAExporter,exportationEpicea.listComplete, fw,  reExportFacture,exportationEpicea.numDepart,gererPointages,null,
										reglementLies);
							else							
						if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_ISAGRI2))
							exportationEpicea.numDepart=exportFactureCentraliseeISAGRI2(idFactureAExporter,exportationEpicea.listComplete, fw,  reExportFacture,exportationEpicea.numDepart,gererPointages,null,
									reglementLies);
						else
						if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_SAGE))
							exportationEpicea.numDepart=exportFactureCentraliseeSAGE(idFactureAExporter,exportationEpicea.listComplete, fw,  reExportFacture,exportationEpicea.numDepart,gererPointages,null,
									reglementLies);
						else
						exportationEpicea.numDepart=exportFactureCentraliseeJPA(idFactureAExporter,exportationEpicea.listComplete, fw,  reExportFacture,exportationEpicea.numDepart,gererPointages,null,
								reglementLies);
					}else{
						if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_ISAGRIFINAL))
							exportationEpicea.numDepart=exportFactureISAGRIFinal(idFactureAExporter,exportationEpicea.listComplete, fw,  reExportFacture,exportationEpicea.numDepart,gererPointages,null,
									reglementLies);
						else 
						if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_ISAGRI2))
							exportationEpicea.numDepart=exportFactureISAGRI2(idFactureAExporter,exportationEpicea.listComplete, fw,  reExportFacture,exportationEpicea.numDepart,gererPointages,null,
									reglementLies);
						else 
						if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_ISAGRI1))
							exportationEpicea.numDepart=exportFactureISAGRI1(idFactureAExporter,exportationEpicea.listComplete, fw,  reExportFacture,exportationEpicea.numDepart,gererPointages,null,
									reglementLies);
						else 
						if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_CGID))
							exportationEpicea.numDepart=exportFactureCEGID(idFactureAExporter,exportationEpicea.listComplete, fw,  reExportFacture,exportationEpicea.numDepart,gererPointages,null,
									reglementLies);
						else 
							if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_SAGE))
								exportationEpicea.numDepart=exportFactureSAGE(idFactureAExporter,exportationEpicea.listComplete, fw,  reExportFacture,exportationEpicea.numDepart,gererPointages,null,
										reglementLies);
							else 							
						exportationEpicea.numDepart=exportFactureJPA(idFactureAExporter,exportationEpicea.listComplete, fw,  reExportFacture,exportationEpicea.numDepart,gererPointages,null,
								reglementLies);
					}
				}
				
				if(idAvoirAExporter!=null) {
					if(centralisation){
						if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_ISAGRI1))
						exportationEpicea.numDepart=exportAvoirCentraliseeISAGRI1(idAvoirAExporter,exportationEpicea.listComplete, fw,  reExportAvoir,exportationEpicea.numDepart,gererPointages,null,
								documentLies);	
						else
							if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_ISAGRI2))
								exportationEpicea.numDepart=exportAvoircentraliseeISAGRI2(idAvoirAExporter,exportationEpicea.listComplete, fw,  reExportAvoir,exportationEpicea.numDepart,gererPointages,null,
										documentLies);	
								else
									if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_ISAGRIFINAL))
										exportationEpicea.numDepart=exportAvoircentraliseeISAGRIFINAL(idAvoirAExporter,exportationEpicea.listComplete, fw,  reExportAvoir,exportationEpicea.numDepart,gererPointages,null,
												documentLies);	
										else									
						if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_SAGE))
							exportationEpicea.numDepart=exportAvoirCentraliseeSAGE(idAvoirAExporter,exportationEpicea.listComplete, fw,  reExportAvoir,exportationEpicea.numDepart,gererPointages,null,
									documentLies);							
						else
						exportationEpicea.numDepart=exportAvoirCentraliseeJPA(idAvoirAExporter,exportationEpicea.listComplete, fw,  reExportAvoir,exportationEpicea.numDepart,gererPointages,null,
								documentLies);					
					}else{
						if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_ISAGRIFINAL))
						exportationEpicea.numDepart=exportAvoirISAGRIFINAL(idAvoirAExporter,exportationEpicea.listComplete, fw,  reExportAvoir,exportationEpicea.numDepart,gererPointages,null,
								documentLies);
						else
							if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_ISAGRI1))
								exportationEpicea.numDepart=exportAvoirISAGRI1(idAvoirAExporter,exportationEpicea.listComplete, fw,  reExportAvoir,exportationEpicea.numDepart,gererPointages,null,
										documentLies);
								else
							if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_ISAGRI2))
								exportationEpicea.numDepart=exportAvoirISAGRI2(idAvoirAExporter,exportationEpicea.listComplete, fw,  reExportAvoir,exportationEpicea.numDepart,gererPointages,null,
										documentLies);
								else							
						if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_CGID))
						exportationEpicea.numDepart=exportAvoirCEGID(idAvoirAExporter,exportationEpicea.listComplete, fw,  reExportAvoir,exportationEpicea.numDepart,gererPointages,null,
								documentLies);
						else
							if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_SAGE))
								exportationEpicea.numDepart=exportAvoirSAGE(idAvoirAExporter,exportationEpicea.listComplete, fw,  reExportAvoir,exportationEpicea.numDepart,gererPointages,null,
										documentLies);
								else							
							exportationEpicea.numDepart=exportAvoirJPA(idAvoirAExporter,exportationEpicea.listComplete, fw,  reExportAvoir,exportationEpicea.numDepart,gererPointages,null,
									documentLies);
					}
				}
				if(idApporteurAExporter!=null && !exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_SAGE)) {
					if(centralisation){
						if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_ISAGRI1))
							exportationEpicea.numDepart=exportApporteurCentraliseeISAGRI1(idApporteurAExporter,exportationEpicea.listComplete, fw,  reExportApporteur,exportationEpicea.numDepart,gererPointages,null,
									documentLies);
						else
							if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_ISAGRI2))
								exportationEpicea.numDepart=exportApporteurCentraliseeISAGRI2(idApporteurAExporter,exportationEpicea.listComplete, fw,  reExportApporteur,exportationEpicea.numDepart,gererPointages,null,
										documentLies);
							else
								if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_ISAGRIFINAL))
									exportationEpicea.numDepart=exportApporteurCentraliseeISAGRIFINAL(idApporteurAExporter,exportationEpicea.listComplete, fw,  reExportApporteur,exportationEpicea.numDepart,gererPointages,null,
											documentLies);
								else
						exportationEpicea.numDepart=exportApporteurCentraliseeJPA(idApporteurAExporter,exportationEpicea.listComplete, fw,  reExportApporteur,exportationEpicea.numDepart,gererPointages,null,
								documentLies);					
					}else{
						if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_ISAGRIFINAL))
							exportationEpicea.numDepart=exportApporteurISAGRIFINAL(idApporteurAExporter,exportationEpicea.listComplete, fw,  reExportApporteur,exportationEpicea.numDepart,gererPointages,null,
									documentLies);
						else
							if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_ISAGRI1))
								exportationEpicea.numDepart=exportApporteurISAGRI1(idApporteurAExporter,exportationEpicea.listComplete, fw,  reExportApporteur,exportationEpicea.numDepart,gererPointages,null,
										documentLies);
							else
							if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_ISAGRI2))
								exportationEpicea.numDepart=exportApporteurISAGRI2(idApporteurAExporter,exportationEpicea.listComplete, fw,  reExportApporteur,exportationEpicea.numDepart,gererPointages,null,
										documentLies);
							else
						if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_CGID))
						exportationEpicea.numDepart=exportApporteurCEGID(idApporteurAExporter,exportationEpicea.listComplete, fw,  reExportApporteur,exportationEpicea.numDepart,gererPointages,null,
								documentLies);
						else
							exportationEpicea.numDepart=exportApporteurJPA(idApporteurAExporter,exportationEpicea.listComplete, fw,  reExportApporteur,exportationEpicea.numDepart,gererPointages,null,
									documentLies);
					}
				}	
				
				if(idReglementAExporter!=null && !exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_SAGE)) {
					if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_ISAGRIFINAL))
					exportationEpicea.numDepart=exportReglementISAGRIFINAL(idReglementAExporter,exportationEpicea.listComplete, fw,  reExportReglement,exportationEpicea.numDepart,gererPointages,null,
							documentLies,null);
					else
						if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_ISAGRI1))
							exportationEpicea.numDepart=exportReglementISAGRI1(idReglementAExporter,exportationEpicea.listComplete, fw,  reExportReglement,exportationEpicea.numDepart,gererPointages,null,
									documentLies,null);
							else
						if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_ISAGRI2))
							exportationEpicea.numDepart=exportReglementISAGRI2(idReglementAExporter,exportationEpicea.listComplete, fw,  reExportReglement,exportationEpicea.numDepart,gererPointages,null,
									documentLies,null);
							else
					if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_CGID))
					exportationEpicea.numDepart=exportReglementCEGID(idReglementAExporter,exportationEpicea.listComplete, fw,  reExportReglement,exportationEpicea.numDepart,gererPointages,null,
							documentLies,null);
					else
						exportationEpicea.numDepart=exportReglementJPA(idReglementAExporter,exportationEpicea.listComplete, fw,  reExportReglement,exportationEpicea.numDepart,gererPointages,null,
								documentLies,null);
				}
				
				if(idRemiseAExporter!=null && !exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_SAGE)) {
					if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_ISAGRIFINAL))
						exportationEpicea.numDepart=exportRemiseISAGRIFINAL(idRemiseAExporter,exportationEpicea.listComplete, fw,  reExportRemise,exportationEpicea.numDepart,gererPointages,null,
								documentLies);
					else
						if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_ISAGRI1))
							exportationEpicea.numDepart=exportRemiseISAGRI1(idRemiseAExporter,exportationEpicea.listComplete, fw,  reExportRemise,exportationEpicea.numDepart,gererPointages,null,
									documentLies);
						else
						if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_ISAGRI2))
							exportationEpicea.numDepart=exportRemiseISAGRI2(idRemiseAExporter,exportationEpicea.listComplete, fw,  reExportRemise,exportationEpicea.numDepart,gererPointages,null,
									documentLies);
						else
					if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_CGID))
					exportationEpicea.numDepart=exportRemiseCEGID(idRemiseAExporter,exportationEpicea.listComplete, fw,  reExportRemise,exportationEpicea.numDepart,gererPointages,null,
							documentLies);
					else
						exportationEpicea.numDepart=exportRemiseJPA(idRemiseAExporter,exportationEpicea.listComplete, fw,  reExportRemise,exportationEpicea.numDepart,gererPointages,null,
								documentLies);
				}
				

	            
				if(fw!=null) fw.close();
				if(bw!=null) bw.close();
				Date newDate=new Date();
				//merger tous les documents exportés
				for (IDocumentTiers b : exportationEpicea.listCompleteDoc) {
					if(b.getTypeDocument().equals(TaFacture.TYPE_DOC)){
//						if(b.getExport()!=1)b.setExport(1);
						b.setDateExport(newDate);
						dao.merge((TaFacture)b);
					}
					if(b.getTypeDocument().equals(TaAvoir.TYPE_DOC)){
//						if(b.getExport()!=1)b.setExport(1);
						b.setDateExport(newDate);
						daoAvoir.merge((TaAvoir)b);
					}
					if(b.getTypeDocument().equals(TaAcompte.TYPE_DOC)){
//						if(b.getExport()!=1)b.setExport(1);
						b.setDateExport(newDate);
						daoAcompte.merge((TaAcompte)b);
					}					
					if(b.getTypeDocument().equals(TaApporteur.TYPE_DOC)){
//						if(b.getExport()!=1)b.setExport(1);
						b.setDateExport(newDate);
						daoApporteur.merge((TaApporteur)b);
					}					
					if(b.getTypeDocument().equals(TaReglement.TYPE_DOC)){
//						if(b.getExport()!=1)b.setExport(1);
						b.setDateExport(newDate);
						daoReglement.merge((TaReglement)b);
					}					
					if(b.getTypeDocument().equals(TaRemise.TYPE_DOC)){
//						if(b.getExport()!=1)b.setExport(1);
						b.setDateExport(newDate);
						daoRemise.merge((TaRemise)b);
					}										
				}
				for (IRelationDoc b : exportationEpicea.listCompleteRelation) {

					if(b.getTypeDocument().equals(TaRReglement.TYPE_DOC)){
						b=daoRReglement.findById(b.getId());
//						b.setExport(1);
						b.setDateExport(newDate);
						daoRReglement.merge((TaRReglement)b);
					}
					if(b.getTypeDocument().equals(TaRAvoir.TYPE_DOC)){
						b=daoRAvoir.findById(b.getId());
//						b.setExport(1);
						b.setDateExport(newDate);
						daoRAvoir.merge((TaRAvoir)b);
					}
				}

			} catch(IOException ioe){
				logger.error("",ioe);
				exportationEpicea.setRetour(false);
			} 
			catch (Exception e) {
				if(fw!=null) fw.close();
				if(bw!=null) bw.close();
				logger.error("",e);
				exportationEpicea.setRetour(false);
				exportationEpicea.effaceFichierTexte(exportationEpicea.getFichierExportationServeur());
//				throw e; 
			}
			return getExportationEpicea();
			} else {
				exportationEpicea.setMessageRetour("Exportation annulée.");
				return null;
			}
		}
	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportApporteurCEGID(List<TaApporteur> idFactureAExporter,List<String> listComplete,Writer fw,
			int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesDocumenstLies) throws Exception {
		int numFin=numDepart;
		try {      


			TaApporteur TaApporteur = null;

			boolean nouvellePiece = false;


			for(int i=0; i<idFactureAExporter.size(); i++) {
				TaApporteur = idFactureAExporter.get(i);
				if(listComplete.indexOf(TaApporteur.getCodeDocument())==-1){

					/**/   // pour compenser les problèmes d'arrondis mis sur des lignes à 0
					TaApporteur.calculeTvaEtTotaux();
					/**/  //isa le 03 février 2011 en accord avec Nicolas

					if(TaApporteur.getDateExport()==null || reExport==1) { /* facture pas deja exportee ou reexportation */
						nouvellePiece = true;
						//					TaApporteur.calculeTvaEtTotaux();

						if(nouvellePiece) {
							exportationEpicea.numeroDePiece++;
							nouvellePiece = false;
							numFin++;
						}

						//recuperation des informations propre a la facture
						exportationEpicea.codePiece = TaApporteur.getCodeDocument();
						exportationEpicea.datePiece = TaApporteur.getDateDocument();
						exportationEpicea.dateEcheance = TaApporteur.getDateEchDocument();
						exportationEpicea.libelleLigne = TaApporteur.getTaInfosDocument().getNomTiers();
						
						exportationEpicea.cptAuxiliaire =TaApporteur.getTaInfosDocument().getCodeCompta();   
						exportationEpicea.cptGeneral = LibConversion.stringToInteger(TaApporteur.getTaInfosDocument().getCompte());

						if(TaApporteur.getNetTtcCalc().signum()>0)
							exportationEpicea.sens="C";
						else exportationEpicea.sens="D";
						
						exportationEpicea.montant = LibConversion.BigDecimalToDouble(TaApporteur.getNetTtcCalc().abs());  

						exportationEpicea.qte1 = 0d;
						
						exportationEpicea.journal=TaApporteur.getCodeDocument().substring(0, 2);

						exportationEpicea.etablissement ="";
						exportationEpicea.qualifPiece ="";
						exportationEpicea.affaire ="";
						exportationEpicea.dateExterne = null;
						exportationEpicea.refExterne="";

							
						//ecriture de la ligne d'entete dans le fichier d'export
						fw.write(exportationEpicea.creationLigneCEGID(
								exportationEpicea.datePiece,
								exportationEpicea.journal ,
								exportationEpicea.cptGeneral ,
								exportationEpicea.cptAuxiliaire ,
								exportationEpicea.sens ,
								exportationEpicea.montant ,
								exportationEpicea.libelleLigne ,
								exportationEpicea.codePiece ,
								exportationEpicea.etablissement ,
								exportationEpicea.qualifPiece ,
								exportationEpicea.affaire ,
								exportationEpicea.qte1 ,
								exportationEpicea.dateEcheance ,
								exportationEpicea.dateExterne ,
								exportationEpicea.refExterne ));
						fw.write(exportationEpicea.finDeLigne);



						//traitement des lignes
						for (TaLApporteur lf : TaApporteur.getLignes()) {
							//récupération des valeurs propres à la ligne                 
							if(lf.getTaArticle()!=null   && lf.getMtHtLApresRemiseGlobaleDocument()!=null && lf.getMtHtLApresRemiseGlobaleDocument().signum()!=0){
								exportationEpicea.cptAuxiliaire="";
								exportationEpicea.qte1 = 0d;
								exportationEpicea.libelleLigne = lf.getLibLDocument();
								exportationEpicea.qte1 = LibConversion.BigDecimalToDouble(lf.getQteLDocument());
								exportationEpicea.cptGeneral = LibConversion.stringToInteger(lf.getCompteLDocument());
								
								if(exportationEpicea.cptGeneral==null || exportationEpicea.cptGeneral.equals("")){
									exportationEpicea.setRetour(false);
									exportationEpicea.setMessageRetour("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+TaApporteur.getCodeDocument());
									throw new Exception("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+TaApporteur.getCodeDocument());
								}
								exportationEpicea.montant=LibConversion.BigDecimalToDouble(lf.getMtHtLApresRemiseGlobaleDocument().abs());
								if(lf.getMtHtLApresRemiseGlobaleDocument().signum()>0)
									exportationEpicea.sens="D";
								else exportationEpicea.sens="C";
								
								exportationEpicea.journal=TaApporteur.getCodeDocument().substring(0, 2);

								exportationEpicea.etablissement ="";
								exportationEpicea.qualifPiece ="";
								exportationEpicea.affaire ="";
								exportationEpicea.dateExterne = null;
								exportationEpicea.refExterne="";

									
								//ecriture de la ligne d'escompte
								fw.write(exportationEpicea.creationLigneCEGID(
										exportationEpicea.datePiece,
										exportationEpicea.journal ,
										exportationEpicea.cptGeneral ,
										exportationEpicea.cptAuxiliaire ,
										exportationEpicea.sens ,
										exportationEpicea.montant ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.codePiece ,
										exportationEpicea.etablissement ,
										exportationEpicea.qualifPiece ,
										exportationEpicea.affaire ,
										exportationEpicea.qte1 ,
										exportationEpicea.dateEcheance ,
										exportationEpicea.dateExterne ,
										exportationEpicea.refExterne ));
								fw.write(exportationEpicea.finDeLigne);

							}
						} 

						for (LigneTva tva : TaApporteur.getLignesTVA()) {
							exportationEpicea.cptAuxiliaire="";
							exportationEpicea.qte1 = 0d;
							TaTva t=daoTvaDoc.findByCode(tva.getCodeTva());
							if(t!=null) {
								exportationEpicea.cptGeneral = LibConversion.stringToInteger(t.getNumcptTva());
								exportationEpicea.libelleLigne = t.getLibelleTva();
							}
							
							if(exportationEpicea.cptGeneral==null || exportationEpicea.cptGeneral.equals("")){
								exportationEpicea.setRetour(false);
								exportationEpicea.setMessageRetour("\nErreur N° 1\n"
										+"le code tva "+tva.getCodeTva()+" n'a pas de compte comptable dans le document : "+TaApporteur.getCodeDocument());
								throw new Exception("\nErreur N° 1\n"
										+"le code tva "+tva.getCodeTva()+" n'a pas de compte comptable dans le document : "+TaApporteur.getCodeDocument());
							}
							exportationEpicea.montant=LibConversion.BigDecimalToDouble(tva.getMtTva().abs());
							if(tva.getMtTva().signum()>0)
								exportationEpicea.sens="C";
							else exportationEpicea.sens="D";
							

							exportationEpicea.etablissement ="";
							exportationEpicea.qualifPiece ="";
							exportationEpicea.affaire ="";
							exportationEpicea.dateExterne = null;
							exportationEpicea.refExterne="";

								
							//ecriture de la ligne d'escompte
							fw.write(exportationEpicea.creationLigneCEGID(
									exportationEpicea.datePiece,
									exportationEpicea.journal ,
									exportationEpicea.cptGeneral ,
									exportationEpicea.cptAuxiliaire ,
									exportationEpicea.sens ,
									exportationEpicea.montant ,
									exportationEpicea.libelleLigne ,
									exportationEpicea.codePiece ,
									exportationEpicea.etablissement ,
									exportationEpicea.qualifPiece ,
									exportationEpicea.affaire ,
									exportationEpicea.qte1 ,
									exportationEpicea.dateEcheance ,
									exportationEpicea.dateExterne ,
									exportationEpicea.refExterne ));
							fw.write(exportationEpicea.finDeLigne);
					}

						listComplete.add(TaApporteur.getCodeDocument());
						exportationEpicea.listCompleteDoc.add(TaApporteur);
						exportationEpicea.listeVerif.add(TaApporteur);
					}
				}
			}

		} catch(IOException ioe){
			logger.error("",ioe);
			exportationEpicea.setRetour(false);
		} 

		return numFin;
	}
	
	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportApporteurSAGE(List<TaApporteur> idFactureAExporter,List<String> listComplete,Writer fw,
			int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesDocumenstLies) throws Exception {
		int numFin=numDepart;
		try {      


			TaApporteur TaApporteur = null;

			boolean nouvellePiece = false;


			for(int i=0; i<idFactureAExporter.size(); i++) {
				TaApporteur = idFactureAExporter.get(i);
				if(listComplete.indexOf(TaApporteur.getCodeDocument())==-1){

					/**/   // pour compenser les problèmes d'arrondis mis sur des lignes à 0
					TaApporteur.calculeTvaEtTotaux();
					/**/  //isa le 03 février 2011 en accord avec Nicolas

					if(TaApporteur.getDateExport()==null || reExport==1) { /* facture pas deja exportee ou reexportation */
						nouvellePiece = true;
						//					TaApporteur.calculeTvaEtTotaux();

						if(nouvellePiece) {
							exportationEpicea.numeroDePiece++;
							nouvellePiece = false;
							numFin++;
						}

						//recuperation des informations propre a la facture
						exportationEpicea.codePiece = TaApporteur.getCodeDocument();
						exportationEpicea.datePiece = TaApporteur.getDateDocument();
						exportationEpicea.dateEcheance = TaApporteur.getDateEchDocument();
						exportationEpicea.libelleLigne = TaApporteur.getTaInfosDocument().getNomTiers();
						
						exportationEpicea.cptAuxiliaire =TaApporteur.getTaInfosDocument().getCodeCompta();   
						exportationEpicea.cptGeneral = LibConversion.stringToInteger(TaApporteur.getTaInfosDocument().getCompte());

						if(TaApporteur.getNetTtcCalc().signum()>0) {
							exportationEpicea.mtDebitLigne=0d;
							exportationEpicea.mtCreditLigne=LibConversion.BigDecimalToDouble(TaApporteur.getNetTtcCalc().abs()); 
						}
						else {
							exportationEpicea.mtDebitLigne=LibConversion.BigDecimalToDouble(TaApporteur.getNetTtcCalc().abs()); 
							exportationEpicea.mtCreditLigne=0d;
						}
						

						
						exportationEpicea.journal=TaApporteur.getCodeDocument().substring(0, 2);
						
						exportationEpicea.typeReglement="";//initialisation
						if(TaApporteur.getTaTiers()!=null && TaApporteur.getTaTiers().getTaTPaiement()!=null)
							exportationEpicea.typeReglement=TaApporteur.getTaTiers().getTaTPaiement().getCodeTPaiement();

							
						//ecriture de la ligne d'entete dans le fichier d'export
						fw.write(exportationEpicea.creationLigneSAGE(
								exportationEpicea.journal ,
								exportationEpicea.datePiece,
								exportationEpicea.cptGeneral ,
								exportationEpicea.codePiece ,
								exportationEpicea.cptAuxiliaire ,
								exportationEpicea.libelleLigne ,
								exportationEpicea.mtDebitLigne ,
								exportationEpicea.mtCreditLigne ,
								exportationEpicea.dateEcheance,
								exportationEpicea.typeReglement));
						fw.write(exportationEpicea.finDeLigne);



						//traitement des lignes
						for (TaLApporteur lf : TaApporteur.getLignes()) {
							//récupération des valeurs propres à la ligne                 
							if(lf.getTaArticle()!=null   && lf.getMtHtLApresRemiseGlobaleDocument()!=null && lf.getMtHtLApresRemiseGlobaleDocument().signum()!=0){
								exportationEpicea.cptAuxiliaire="";
								exportationEpicea.libelleLigne = lf.getLibLDocument();
								exportationEpicea.cptGeneral = LibConversion.stringToInteger(lf.getCompteLDocument());
								
								if(exportationEpicea.cptGeneral==null || exportationEpicea.cptGeneral.equals("")){
									exportationEpicea.setRetour(false);
									exportationEpicea.setMessageRetour("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+TaApporteur.getCodeDocument());
									throw new Exception("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+TaApporteur.getCodeDocument());
								}
								
								
//								exportationEpicea.montant=LibConversion.BigDecimalToDouble(lf.getMtHtLApresRemiseGlobaleDocument().abs());
								if(lf.getMtHtLApresRemiseGlobaleDocument().signum()>0) {
									exportationEpicea.mtDebitLigne=0d;
									exportationEpicea.mtCreditLigne=LibConversion.BigDecimalToDouble(lf.getMtHtLApresRemiseGlobaleDocument().abs()); 
								}
								else {
									exportationEpicea.mtDebitLigne=LibConversion.BigDecimalToDouble(lf.getMtHtLApresRemiseGlobaleDocument().abs()); 
									exportationEpicea.mtCreditLigne=0d;
								}
								
									
								//ecriture de la ligne d'escompte
								fw.write(exportationEpicea.creationLigneSAGE(
										exportationEpicea.journal ,
										exportationEpicea.datePiece,
										exportationEpicea.cptGeneral ,
										exportationEpicea.codePiece ,
										exportationEpicea.cptAuxiliaire ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.mtDebitLigne ,
										exportationEpicea.mtCreditLigne ,
										exportationEpicea.dateEcheance,
										exportationEpicea.typeReglement));
								fw.write(exportationEpicea.finDeLigne);

							}
						} 

						for (LigneTva tva : TaApporteur.getLignesTVA()) {
							exportationEpicea.cptAuxiliaire="";
							TaTva t=daoTvaDoc.findByCode(tva.getCodeTva());
							if(t!=null) {
								exportationEpicea.cptGeneral = LibConversion.stringToInteger(t.getNumcptTva());
								exportationEpicea.libelleLigne = t.getLibelleTva();
							}
							
							if(exportationEpicea.cptGeneral==null || exportationEpicea.cptGeneral.equals("")){
								exportationEpicea.setRetour(false);
								exportationEpicea.setMessageRetour("\nErreur N° 1\n"
										+"le code tva "+tva.getCodeTva()+" n'a pas de compte comptable dans le document : "+TaApporteur.getCodeDocument());
								throw new Exception("\nErreur N° 1\n"
										+"le code tva "+tva.getCodeTva()+" n'a pas de compte comptable dans le document : "+TaApporteur.getCodeDocument());
							}

							
							if(tva.getMtTva().signum()>0) {
								exportationEpicea.mtDebitLigne=0d;
								exportationEpicea.mtCreditLigne=LibConversion.BigDecimalToDouble(tva.getMtTva().abs()); 
							}
							else {
								exportationEpicea.mtDebitLigne=LibConversion.BigDecimalToDouble(tva.getMtTva().abs()); 
								exportationEpicea.mtCreditLigne=0d;
							}
							
								
							//ecriture de la ligne d'escompte
							//ecriture de la ligne d'entete dans le fichier d'export
							fw.write(exportationEpicea.creationLigneSAGE(
									exportationEpicea.journal ,
									exportationEpicea.datePiece,
									exportationEpicea.cptGeneral ,
									exportationEpicea.codePiece ,
									exportationEpicea.cptAuxiliaire ,
									exportationEpicea.libelleLigne ,
									exportationEpicea.mtDebitLigne ,
									exportationEpicea.mtCreditLigne ,
									exportationEpicea.dateEcheance,
									exportationEpicea.typeReglement));
							fw.write(exportationEpicea.finDeLigne);
					}

						listComplete.add(TaApporteur.getCodeDocument());
						exportationEpicea.listCompleteDoc.add(TaApporteur);
						exportationEpicea.listeVerif.add(TaApporteur);
					}
				}
			}

		} catch(IOException ioe){
			logger.error("",ioe);
			exportationEpicea.setRetour(false);
		} 

		return numFin;
	}

	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportApporteurJPA(List<TaApporteur> idFactureAExporter,List<String> listComplete,Writer fw,
			int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesDocumenstLies) throws Exception {
		int numFin=numDepart;
		try {      


			TaApporteur TaApporteur = null;


			exportationEpicea.numeroDePiece = numFin;



			boolean nouvellePiece = false;


			for(int i=0; i<idFactureAExporter.size(); i++) {
				TaApporteur = idFactureAExporter.get(i);
				if(listComplete.indexOf(TaApporteur.getCodeDocument())==-1){

					/**/   // pour compenser les problèmes d'arrondis mis sur des lignes à 0
					TaApporteur.calculeTvaEtTotaux();
					/**/  //isa le 03 février 2011 en accord avec Nicolas

					if(TaApporteur.getDateExport()==null || reExport==1) { /* facture pas deja exportee ou reexportation */
						nouvellePiece = true;
						//					TaApporteur.calculeTvaEtTotaux();

						if(nouvellePiece) {
							exportationEpicea.numeroDePiece++;
							nouvellePiece = false;
							numFin++;
						}

						//recuperation des informations propre a la facture
						exportationEpicea.codePiece = TaApporteur.getCodeDocument();
						exportationEpicea.datePiece = TaApporteur.getDateDocument();
						exportationEpicea.dateEcheance = TaApporteur.getDateEchDocument();
						exportationEpicea.dateLivraisonLigne = TaApporteur.getDateDocument();//changer pour erreur 18000 dans epicéa;
//						dateLivraisonLigne = TaApporteur.getDateLivDocument();
						exportationEpicea.libelleLigne = TaApporteur.getTaInfosDocument().getNomTiers();
						exportationEpicea.numCptLigne = TaApporteur.getTaInfosDocument().getCodeCompta();
						exportationEpicea.cptCollectif = TaApporteur.getTaInfosDocument().getCompte();
						exportationEpicea.nomTiers = TaApporteur.getTaInfosDocument().getNomTiers();
						exportationEpicea.adresse1Tiers = TaApporteur.getTaInfosDocument().getAdresse1();
						exportationEpicea.adresse2Tiers = TaApporteur.getTaInfosDocument().getAdresse2();
						exportationEpicea.codePostalTiers = TaApporteur.getTaInfosDocument().getCodepostal();
						exportationEpicea.villeTiers = TaApporteur.getTaInfosDocument().getVille();
			

						exportationEpicea.mtCreditLigne = LibConversion.BigDecimalToDouble(TaApporteur.getNetTtcCalc());  

						exportationEpicea.numCptLigne = "+"+TaApporteur.getTaInfosDocument().getCodeCompta();    
						exportationEpicea.numLignePiece = 1;
						exportationEpicea.typePiece = exportationEpicea.TypeAchat;
						exportationEpicea.mtDebitLigne = 0d;
						exportationEpicea.qte1 = 0d;
						exportationEpicea.qte2 = 0d;
						exportationEpicea.codeTva ="";
						exportationEpicea.tauxTva = 0d;
						exportationEpicea.mtDebitTva = 0d;
						exportationEpicea.mtCreditTva = 0d;
						exportationEpicea.exigibiliteTva = "";

						exportationEpicea.refContrepartie = "";
						exportationEpicea.dateContrepartie = null;
						exportationEpicea.mtContrepartieDebit = 0d;
						exportationEpicea.mtContrepartieCredit = 0d;
						exportationEpicea.mtAffectation = 0d;
						exportationEpicea.codeTvaComplet = "";
						exportationEpicea.refReglement = "";

						//ecriture de la ligne d'entete dans le fichier d'export
						fw.write(exportationEpicea.creationLigne(exportationEpicea.numeroDePiece,
								exportationEpicea.numLignePiece ,
								exportationEpicea.typePiece ,
								exportationEpicea.codePiece ,
								exportationEpicea.datePiece ,
								exportationEpicea.numCptLigne ,
								exportationEpicea.libelleLigne ,
								exportationEpicea.mtDebitLigne ,
								exportationEpicea.mtCreditLigne ,
								exportationEpicea.qte1 ,
								exportationEpicea.qte2 ,
								exportationEpicea.codeTva ,
								exportationEpicea.tauxTva ,
								exportationEpicea.mtCreditTva ,
								exportationEpicea.mtDebitTva ,
								exportationEpicea.dateEcheance ,
								exportationEpicea.cptCollectif ,
								exportationEpicea.nomTiers ,
								exportationEpicea.adresse1Tiers ,
								exportationEpicea.adresse2Tiers ,
								exportationEpicea.codePostalTiers ,
								exportationEpicea.villeTiers ,
								exportationEpicea.exigibiliteTva ,
								exportationEpicea.dateLivraisonLigne,
								exportationEpicea.refContrepartie,
								exportationEpicea.dateContrepartie,
								exportationEpicea.mtContrepartieDebit,
								exportationEpicea.mtContrepartieCredit,
								exportationEpicea.mtAffectation,
								exportationEpicea.codeTvaComplet,
								exportationEpicea.refReglement));
						fw.write(exportationEpicea.finDeLigne);

						//réinitialisation des variables pour le prochain document
						exportationEpicea.cptCollectif ="";
						exportationEpicea.nomTiers ="";
						exportationEpicea.adresse1Tiers ="";
						exportationEpicea.adresse2Tiers ="";
						exportationEpicea.codePostalTiers ="";
						exportationEpicea.villeTiers="";


						//traitement des lignes
						for (TaLApporteur lf : TaApporteur.getLignes()) {
							//récupération des valeurs propres à la ligne

							exportationEpicea.typePiece = exportationEpicea.TypeAchat;
							if(lf.getTaArticle()!=null   && lf.getMtHtLApresRemiseGlobaleDocument()!=null && lf.getMtHtLApresRemiseGlobaleDocument().signum()!=0){
								exportationEpicea.libelleLigne = lf.getLibLDocument();
								exportationEpicea.qte1 = LibConversion.BigDecimalToDouble(lf.getQteLDocument());
								exportationEpicea.qte2 = LibConversion.BigDecimalToDouble(lf.getQte2LDocument());
								exportationEpicea.numCptLigne = lf.getCompteLDocument();
								if(exportationEpicea.numCptLigne==null || exportationEpicea.numCptLigne.equals("")){
									//transaction.rollback();
									exportationEpicea.setRetour(false);
									exportationEpicea.setMessageRetour("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+TaApporteur.getCodeDocument());
									throw new Exception("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+TaApporteur.getCodeDocument());
								}
								//traitement du type de tva du tiers (F ou ue ou horsUE ou Franchise)
								String codeDoc="";
								if(TaApporteur.getTaInfosDocument().getCodeTTvaDoc()==null || TaApporteur.getTaInfosDocument().getCodeTTvaDoc().equals(""))
									codeDoc="F";
								else codeDoc=TaApporteur.getTaInfosDocument().getCodeTTvaDoc();
								TaTTvaDoc tvaDoc =daoTTvaDoc.findByCode(codeDoc);
								String journalTva="";
								if(tvaDoc!=null)journalTva=tvaDoc.getJournalTTvaDoc();
								
//									if(TaApporteur.getTaTiers()!=null && TaApporteur.getTaTiers().getTaTTvaDoc()!=null){
								if(codeDoc.equalsIgnoreCase("F"))
									exportationEpicea.codeTva = exportationEpicea.correspondanceCodeTva(codeDoc,lf.getCodeTvaLDocument(),lf.getCodeTvaLDocument(),exportationEpicea.numCptLigne,lf.getMtTtcLApresRemiseGlobaleDocument().subtract(lf.getMtHtLApresRemiseGlobaleDocument()));
								else
									exportationEpicea.codeTva = exportationEpicea.correspondanceCodeTva(codeDoc,lf.getCodeTvaLDocument(),journalTva,exportationEpicea.numCptLigne,lf.getMtTtcLApresRemiseGlobaleDocument().subtract(lf.getMtHtLApresRemiseGlobaleDocument()));	
								exportationEpicea.codeTvaComplet=lf.getCodeTvaLDocument();


//								exigibiliteTva = lf.getTaArticle().getTaTTva().getCodeTTva();
//								if(exigibiliteTva == null)exigibiliteTva="D";
								if(exportationEpicea.codeTva==null)exportationEpicea.codeTva=""; /* corrigé par isa le 11/09/2014*/
								if(lf.getTaArticle().getTaTTva()!=null)  
									exportationEpicea.exigibiliteTva = lf.getTaArticle().getTaTTva().getCodeTTva();
								if (exportationEpicea.exigibiliteTva.equals(""))exportationEpicea.exigibiliteTva="D";
								
								exportationEpicea.tauxTva = LibConversion.BigDecimalToDouble(lf.getTauxTvaLDocument());
								exportationEpicea.mtDebitTva = LibConversion.BigDecimalToDouble(lf.getMtTtcLApresRemiseGlobaleDocument().subtract(lf.getMtHtLApresRemiseGlobaleDocument()));
								exportationEpicea.mtDebitLigne = LibConversion.BigDecimalToDouble(lf.getMtHtLApresRemiseGlobaleDocument());

								exportationEpicea.numLignePiece = exportationEpicea.numLignePiece + 1;
								exportationEpicea.mtCreditLigne = 0d;
								exportationEpicea.mtCreditTva = 0d;
								exportationEpicea.cptCollectif = "";
								exportationEpicea.nomTiers = "";
								exportationEpicea.adresse1Tiers = "";
								exportationEpicea.adresse2Tiers = "";
								exportationEpicea.codePostalTiers = "";
								exportationEpicea.villeTiers = "";
								//ecriture de la ligne dans le fichier d'export

								exportationEpicea.refContrepartie = "";
								exportationEpicea.dateContrepartie = null;
								exportationEpicea.mtContrepartieDebit = 0d;
								exportationEpicea.mtContrepartieCredit = 0d;
								exportationEpicea.mtAffectation = 0d;
								exportationEpicea.refReglement = "";

								fw.write(exportationEpicea.creationLigne(exportationEpicea.numeroDePiece,
										exportationEpicea.numLignePiece ,
										exportationEpicea.typePiece ,
										exportationEpicea.codePiece ,
										exportationEpicea.datePiece ,
										exportationEpicea.numCptLigne ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.mtDebitLigne,
										exportationEpicea.mtCreditLigne ,
										exportationEpicea.qte1 ,
										exportationEpicea.qte2 ,
										exportationEpicea.codeTva ,
										exportationEpicea.tauxTva ,
										exportationEpicea.mtDebitTva,
										exportationEpicea.mtCreditTva ,
										exportationEpicea.dateEcheance ,
										exportationEpicea.cptCollectif ,
										exportationEpicea.nomTiers ,
										exportationEpicea.adresse1Tiers ,
										exportationEpicea.adresse2Tiers ,
										exportationEpicea.codePostalTiers ,
										exportationEpicea.villeTiers ,
										exportationEpicea.exigibiliteTva ,
										exportationEpicea.dateLivraisonLigne,
										exportationEpicea.refContrepartie,
										exportationEpicea.dateContrepartie,
										exportationEpicea.mtContrepartieDebit,
										exportationEpicea.mtContrepartieCredit,
										exportationEpicea.mtAffectation,
										exportationEpicea.codeTvaComplet,
										exportationEpicea.refReglement));
								fw.write(exportationEpicea.finDeLigne);
							}
						} 


//						if(TaApporteur.getExport()!=1)TaApporteur.setExport(1);
//						daoApporteur.merge(TaApporteur);
						listComplete.add(TaApporteur.getCodeDocument());
						exportationEpicea.listCompleteDoc.add(TaApporteur);
						exportationEpicea.listeVerif.add(TaApporteur);
					}
				}
			}

		} catch(IOException ioe){
			logger.error("",ioe);
			exportationEpicea.setRetour(false);
		} 

		return numFin;
	}

	
//	public int exportAcompteJPA(List<TaAcompte> idAcompteAExporter,List<String> listComplete,FileWriter fw,
//			int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesDocumenstLies) throws Exception {
//		int numFin=numDepart;
//		try {   
//
//
//
//			Boolean gererLesPointages = gererPointages;
//
//			TaAcompte taDoc = null;
//
//
//			exportationEpicea.numeroDePiece = numFin;
//
//			
//
//			boolean nouvellePiece = false;
//
//			for(int i=0; i<idAcompteAExporter.size(); i++) {
//				taDoc = idAcompteAExporter.get(i);
//				if(listComplete.indexOf(taDoc.getCodeDocument())==-1){
//
//
//					if(taDoc.getExport()!=1 || reExport==1) { /* facture pas deja exportee ou reexportation */
//						nouvellePiece = true;
//
//						if(nouvellePiece) {
//							exportationEpicea.numeroDePiece++;
//							nouvellePiece = false;
//							numFin++;
//						}
//
//						//recuperation des informations propre a la facture
//						exportationEpicea.codePiece = taDoc.getCodeDocument();
//						exportationEpicea.datePiece = taDoc.getDateDocument();
//						exportationEpicea.dateEcheance = taDoc.getDateEchDocument();
//						exportationEpicea.dateLivraisonLigne = taDoc.getDateLivDocument();
//						exportationEpicea.libelleLigne = taDoc.getTaInfosDocument().getNomTiers();
//						if(taDoc.getTaTPaiement()!=null)
//							exportationEpicea.numCptLigne = taDoc.getTaTPaiement().getCompte();//taDoc.getTaCompteBanque().getCptcomptable();
//						exportationEpicea.cptCollectif = "";
//						exportationEpicea.nomTiers = "";
//						exportationEpicea.adresse1Tiers = "";
//						exportationEpicea.adresse2Tiers ="";
//						exportationEpicea.codePostalTiers = "";
//						exportationEpicea.villeTiers = "";
//
//						exportationEpicea.mtCreditLigne = 0d;  
//
//						exportationEpicea.numLignePiece = 1;
//						exportationEpicea.typePiece = exportationEpicea.TypeReglement;
//						exportationEpicea.mtDebitLigne = LibConversion.BigDecimalToDouble(taDoc.getNetTtcCalc());
//						exportationEpicea.qte1 = 0d;
//						exportationEpicea.qte2 = 0d;
//						exportationEpicea.codeTva ="";
//						exportationEpicea.tauxTva = 0d;
//						exportationEpicea.mtDebitTva = 0d;
//						exportationEpicea.mtCreditTva = 0d;
//						exportationEpicea.exigibiliteTva = "";
//
//						exportationEpicea.refContrepartie = "";
//						exportationEpicea.dateContrepartie = null;
//						exportationEpicea.mtContrepartieDebit = 0d;
//						exportationEpicea.mtContrepartieCredit = 0d;
//						exportationEpicea.mtAffectation = 0d;
//
//						//ecriture de la ligne d'entete dans le fichier d'export
//						fw.write(exportationEpicea.creationLigne(exportationEpicea.numeroDePiece,
//								exportationEpicea.numLignePiece ,
//								exportationEpicea.typePiece ,
//								exportationEpicea.codePiece ,
//								exportationEpicea.datePiece ,
//								exportationEpicea.numCptLigne ,
//								exportationEpicea.libelleLigne ,
//								exportationEpicea.mtDebitLigne ,
//								exportationEpicea.mtCreditLigne ,
//								exportationEpicea.qte1 ,
//								exportationEpicea.qte2 ,
//								exportationEpicea.codeTva ,
//								exportationEpicea.tauxTva ,
//								exportationEpicea.mtCreditTva ,
//								exportationEpicea.mtDebitTva ,
//								exportationEpicea.dateEcheance ,
//								exportationEpicea.cptCollectif ,
//								exportationEpicea.nomTiers ,
//								exportationEpicea.adresse1Tiers ,
//								exportationEpicea.adresse2Tiers ,
//								exportationEpicea.codePostalTiers ,
//								exportationEpicea.villeTiers ,
//								exportationEpicea.exigibiliteTva ,
//								exportationEpicea.dateLivraisonLigne,
//								exportationEpicea.refContrepartie,
//								exportationEpicea.dateContrepartie,
//								exportationEpicea.mtContrepartieDebit,
//								exportationEpicea.mtContrepartieCredit,
//								exportationEpicea.mtAffectation));
//						fw.write(exportationEpicea.getFinDeLigne());
//
//						//réinitialisation des variables pour le prochain document
//						exportationEpicea.cptCollectif ="";
//						exportationEpicea.nomTiers ="";
//						exportationEpicea.adresse1Tiers ="";
//						exportationEpicea.adresse2Tiers ="";
//						exportationEpicea.codePostalTiers ="";
//						exportationEpicea.villeTiers="";
//
//						//traitement de la ligne de contrepartie (tiers)
//						exportationEpicea.libelleLigne = taDoc.getLibelleDocument();//libelle de la ligne de facture
//						exportationEpicea.qte1 = 0d;
//						exportationEpicea.qte2 = 0d;
//						exportationEpicea.numCptLigne = "+"+taDoc.getTaInfosDocument().getCodeCompta();
//						exportationEpicea.cptCollectif = taDoc.getTaInfosDocument().getCompte();
//						exportationEpicea.nomTiers = taDoc.getTaInfosDocument().getNomTiers();
//						exportationEpicea.adresse1Tiers = taDoc.getTaInfosDocument().getAdresse1();
//						exportationEpicea.adresse2Tiers = taDoc.getTaInfosDocument().getAdresse2();
//						exportationEpicea.codePostalTiers = taDoc.getTaInfosDocument().getCodepostal();
//						exportationEpicea.villeTiers = taDoc.getTaInfosDocument().getVille();
//
//						exportationEpicea.codeTva = "";
//						exportationEpicea.exigibiliteTva = "";
//						exportationEpicea.mtDebitTva = 0d;
//						exportationEpicea.mtDebitLigne = 0d;
//
//						exportationEpicea.numLignePiece = exportationEpicea.numLignePiece + 1;
//						exportationEpicea.mtCreditLigne = LibConversion.BigDecimalToDouble(taDoc.getNetTtcCalc());
//						exportationEpicea.mtCreditTva = 0d;
//
//						exportationEpicea.refContrepartie = "";
//						exportationEpicea.dateContrepartie = null;
//						exportationEpicea.mtContrepartieDebit = 0d;
//						exportationEpicea.mtContrepartieCredit = 0d;
//						exportationEpicea.mtAffectation = 0d;
//
//						fw.write(exportationEpicea.creationLigne(exportationEpicea.numeroDePiece,
//								exportationEpicea.numLignePiece ,
//								exportationEpicea.typePiece ,
//								exportationEpicea.codePiece ,
//								exportationEpicea.datePiece ,
//								exportationEpicea.numCptLigne ,
//								exportationEpicea.libelleLigne ,
//								exportationEpicea.mtDebitLigne,
//								exportationEpicea.mtCreditLigne ,
//								exportationEpicea.qte1 ,
//								exportationEpicea.qte2 ,
//								exportationEpicea.codeTva ,
//								exportationEpicea.tauxTva ,
//								exportationEpicea.mtDebitTva,
//								exportationEpicea.mtCreditTva ,
//								exportationEpicea.dateEcheance ,
//								exportationEpicea.cptCollectif ,
//								exportationEpicea.nomTiers ,
//								exportationEpicea.adresse1Tiers ,
//								exportationEpicea.adresse2Tiers ,
//								exportationEpicea.codePostalTiers ,
//								exportationEpicea.villeTiers ,
//								exportationEpicea.exigibiliteTva ,
//								exportationEpicea.dateLivraisonLigne,
//								exportationEpicea.refContrepartie,
//								exportationEpicea.dateContrepartie,
//								exportationEpicea.mtContrepartieDebit,
//								exportationEpicea.mtContrepartieCredit,
//								exportationEpicea.mtAffectation));
//						fw.write(exportationEpicea.getFinDeLigne());
//
//						if(gererLesPointages){				
//
//							//traitement des lignes pour les pointages
//							exportationEpicea.typePiece = exportationEpicea.typePointage;	
//							for (TaRAcompte lf : taDoc.getTaRAcomptes()) {
//								//récupération des valeurs propres à la ligne
//								TaFacture fac =lf.getTaFacture();
//								numFin=initPointage(fac, taDoc, lf, gererLesDocumenstLies, idAppelant, numFin);
//							} 
//						}
////						if(taDoc.getExport()!=1)taDoc.setExport(1);
////						daoAcompte.merge(taDoc);
//						listComplete.add(taDoc.getCodeDocument());
//						exportationEpicea.listCompleteDoc.add(taDoc);
//						exportationEpicea.getListeVerif().add(taDoc);
//					}
//				}
//			}
//
//		} catch(IOException ioe){
//			logger.error("",ioe);
//			exportationEpicea.setRetour(false);
//		} 
//
//		return numFin;
//	}
	
	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportReglementJPA(List<TaReglement> idReglementAExporter,List<String> listComplete,Writer fw,
			int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesDocumenstLies,TaFacture taFactureDirect) throws Exception {
		int numFin=numDepart;
		try {     


			Boolean gererLesPointages = gererPointages;

			TaReglement taDoc = null;

			exportationEpicea.numeroDePiece = numFin;

		

			boolean nouvellePiece = false;


			for(int i=0; i<idReglementAExporter.size(); i++) {
				taDoc = idReglementAExporter.get(i);
				if(listComplete.indexOf(taDoc.getCodeDocument())==-1 ){

					if(taDoc.getDateExport()==null || reExport==1) { /* facture pas deja exportee ou reexportation */
							if( daoRemise.findSiReglementDansRemise(taDoc.getCodeDocument()).size()==0){
							nouvellePiece = true;

							if(nouvellePiece) {
								exportationEpicea.numeroDePiece++;
								nouvellePiece = false;
								numFin++;
							}

							//recuperation des informations propre a la facture
							exportationEpicea.codePiece = taDoc.getCodeDocument();
							exportationEpicea.datePiece = taDoc.getDateDocument();
							exportationEpicea.dateEcheance = taDoc.getDateEchDocument();
							exportationEpicea.dateLivraisonLigne = taDoc.getDateLivDocument();
							if(taDoc.getLibelleDocument()!=null)
								exportationEpicea.libelleLigne = taDoc.getLibelleDocument().replace("\r","").replace("\n","");
							else
								exportationEpicea.libelleLigne = "";
							if(taDoc.getTaCompteBanque()!=null)
								exportationEpicea.numCptLigne = taDoc.getTaCompteBanque().getCptcomptable();
							if(taDoc.getTaTPaiement()!=null)
								exportationEpicea.numCptLigne = taDoc.getTaTPaiement().getCompte();
							
							//trouver le tiers lié au règlement
							TaTiers tiers=taDoc.getTaTiers();

							exportationEpicea.cptCollectif = "";
							exportationEpicea.nomTiers = "";
							exportationEpicea.adresse1Tiers = "";
							exportationEpicea.adresse2Tiers ="";
							exportationEpicea.codePostalTiers = "";
							exportationEpicea.villeTiers = "";
							
					

							exportationEpicea.mtCreditLigne = 0d;  

							exportationEpicea.numLignePiece = 1;
							if(exportationEpicea.numCptLigne.startsWith("5"))
								exportationEpicea.typePiece = exportationEpicea.TypeReglement;
							else exportationEpicea.typePiece = exportationEpicea.TypeOd;
							exportationEpicea.mtDebitLigne = LibConversion.BigDecimalToDouble(taDoc.getNetTtcCalc());
							exportationEpicea.qte1 = 0d;
							exportationEpicea.qte2 = 0d;
							exportationEpicea.codeTva ="";
							exportationEpicea.tauxTva = 0d;
							exportationEpicea.mtDebitTva = 0d;
							exportationEpicea.mtCreditTva = 0d;
							exportationEpicea.exigibiliteTva = "";

							exportationEpicea.refContrepartie = "";
							exportationEpicea.dateContrepartie = null;
							exportationEpicea.mtContrepartieDebit = 0d;
							exportationEpicea.mtContrepartieCredit = 0d;
							exportationEpicea.mtAffectation = 0d;
							exportationEpicea.codeTvaComplet = "";
							exportationEpicea.refReglement = "";

							//ecriture de la ligne d'entete dans le fichier d'export
							fw.write(exportationEpicea.creationLigne(exportationEpicea.numeroDePiece,
									exportationEpicea.numLignePiece ,
									exportationEpicea.typePiece ,
									exportationEpicea.codePiece ,
									exportationEpicea.datePiece ,
									exportationEpicea.numCptLigne ,
									exportationEpicea.libelleLigne ,
									exportationEpicea.mtDebitLigne ,
									exportationEpicea.mtCreditLigne ,
									exportationEpicea.qte1 ,
									exportationEpicea.qte2 ,
									exportationEpicea.codeTva ,
									exportationEpicea.tauxTva ,
									exportationEpicea.mtCreditTva ,
									exportationEpicea.mtDebitTva ,
									exportationEpicea.dateEcheance ,
									exportationEpicea.cptCollectif ,
									exportationEpicea.nomTiers ,
									exportationEpicea.adresse1Tiers ,
									exportationEpicea.adresse2Tiers ,
									exportationEpicea.codePostalTiers ,
									exportationEpicea.villeTiers ,
									exportationEpicea.exigibiliteTva ,
									exportationEpicea.dateLivraisonLigne,
									exportationEpicea.refContrepartie,
									exportationEpicea.dateContrepartie,
									exportationEpicea.mtContrepartieDebit,
									exportationEpicea.mtContrepartieCredit,
									exportationEpicea.mtAffectation,
									exportationEpicea.codeTvaComplet,
									exportationEpicea.refReglement));
							fw.write(exportationEpicea.finDeLigne);

							//réinitialisation des variables pour le prochain document
							exportationEpicea.cptCollectif ="";
							exportationEpicea.nomTiers ="";
							exportationEpicea.adresse1Tiers ="";
							exportationEpicea.adresse2Tiers ="";
							exportationEpicea.codePostalTiers ="";
							exportationEpicea.villeTiers="";

							//traitement de la ligne de contrepartie (tiers)
							if(taDoc.getLibelleDocument()!=null)
								exportationEpicea.libelleLigne = taDoc.getLibelleDocument().replace("\r","").replace("\n","");//libelle de la ligne de facture
							else
								exportationEpicea.libelleLigne = "";
							exportationEpicea.qte1 = 0d;
							exportationEpicea.qte2 = 0d;
							exportationEpicea.numCptLigne = "+"+taDoc.getTaTiers().getCodeCompta();
							exportationEpicea.cptCollectif = taDoc.getTaTiers().getCompte();
							exportationEpicea.nomTiers = taDoc.getTaTiers().getNomTiers();
							if(taDoc.getTaTiers().getTaAdresse()!=null){
								exportationEpicea.adresse1Tiers = taDoc.getTaTiers().getTaAdresse().getAdresse1Adresse();
								exportationEpicea.adresse2Tiers = taDoc.getTaTiers().getTaAdresse().getAdresse2Adresse();
								exportationEpicea.codePostalTiers = taDoc.getTaTiers().getTaAdresse().getCodepostalAdresse();
								exportationEpicea.villeTiers = taDoc.getTaTiers().getTaAdresse().getVilleAdresse();
							}
							exportationEpicea.codeTva = "";
							exportationEpicea.exigibiliteTva = "";
							exportationEpicea.mtDebitTva = 0d;
							exportationEpicea.mtDebitLigne = 0d;

							exportationEpicea.numLignePiece = exportationEpicea.numLignePiece + 1;
							exportationEpicea.mtCreditLigne = LibConversion.BigDecimalToDouble(taDoc.getNetTtcCalc());
							exportationEpicea.mtCreditTva = 0d;

							exportationEpicea.refContrepartie = "";
							exportationEpicea.dateContrepartie = null;
							exportationEpicea.mtContrepartieDebit = 0d;
							exportationEpicea.mtContrepartieCredit = 0d;
							exportationEpicea.mtAffectation = 0d;
							exportationEpicea.codeTvaComplet = "";
							exportationEpicea.refReglement = taDoc.getCodeDocument();

							fw.write(exportationEpicea.creationLigne(exportationEpicea.numeroDePiece,
									exportationEpicea.numLignePiece ,
									exportationEpicea.typePiece ,
									exportationEpicea.codePiece ,
									exportationEpicea.datePiece ,
									exportationEpicea.numCptLigne ,
									exportationEpicea.libelleLigne ,
									exportationEpicea.mtDebitLigne,
									exportationEpicea.mtCreditLigne ,
									exportationEpicea.qte1 ,
									exportationEpicea.qte2 ,
									exportationEpicea.codeTva ,
									exportationEpicea.tauxTva ,
									exportationEpicea.mtDebitTva,
									exportationEpicea.mtCreditTva ,
									exportationEpicea.dateEcheance ,
									exportationEpicea.cptCollectif ,
									exportationEpicea.nomTiers ,
									exportationEpicea.adresse1Tiers ,
									exportationEpicea.adresse2Tiers ,
									exportationEpicea.codePostalTiers ,
									exportationEpicea.villeTiers ,
									exportationEpicea.exigibiliteTva ,
									exportationEpicea.dateLivraisonLigne,
									exportationEpicea.refContrepartie,
									exportationEpicea.dateContrepartie,
									exportationEpicea.mtContrepartieDebit,
									exportationEpicea.mtContrepartieCredit,
									exportationEpicea.mtAffectation,
									exportationEpicea.codeTvaComplet,
									exportationEpicea.refReglement));
							fw.write(exportationEpicea.finDeLigne);

							if(gererLesPointages){
								exportationEpicea.typePiece = exportationEpicea.typePointage;	
								//traitement des lignes pour les pointages
								for (TaRReglement lf : taDoc.getTaRReglements()) {
									//récupération des valeurs propres à la ligne
									TaFacture fac =lf.getTaFacture();
									numFin=initPointage(fac, taDoc, lf, gererLesDocumenstLies, idAppelant, numFin);
								} 
								/**** Attention !!! rajouter temporairement pour les règlements unique dans les factures*/
								if(taDoc.getTaFactures()!=null && !taDoc.getTaFactures().isEmpty()) {
									for (TaFacture obj : taDoc.getTaFactures()) {
										//récupération des valeurs propres à la ligne
										IRelationDoc r=new TaRReglement();
										r.setAffectation(obj.getNetTtcCalc());
										numFin=initPointage(obj, taDoc, r, gererLesDocumenstLies, idAppelant, numFin);
									}
								} 
								if(taFactureDirect!=null) {
									//récupération des valeurs propres à la ligne
									IRelationDoc r=new TaRReglement();
									r.setAffectation(taFactureDirect.getNetTtcCalc());
									numFin=initPointage(taFactureDirect, taDoc, r, gererLesDocumenstLies, idAppelant, numFin);
								} 								
							}
//							if(taDoc.getExport()!=1)taDoc.setExport(1);
//							taDoc=daoReglement.merge(taDoc);
							listComplete.add(taDoc.getCodeDocument());
							exportationEpicea.listCompleteDoc.add(taDoc);
							exportationEpicea.listeVerif.add(taDoc);
						}
					}
				}
			}

		} catch(IOException ioe){
			logger.error("",ioe);
			exportationEpicea.setRetour(false);
		} 

		return numFin;
	}

	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportReglementCEGID(List<TaReglement> idReglementAExporter,List<String> listComplete,Writer fw,
			int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesDocumenstLies,TaFacture taFactureDirect) throws Exception {
		int numFin=numDepart;
		try {     


			Boolean gererLesPointages = gererPointages;

			TaReglement taDoc = null;

			boolean nouvellePiece = false;


			for(int i=0; i<idReglementAExporter.size(); i++) {
				taDoc = idReglementAExporter.get(i);
				if(listComplete.indexOf(taDoc.getCodeDocument())==-1 ){

					if(taDoc.getDateExport()==null || reExport==1) { /* facture pas deja exportee ou reexportation */
							if( daoRemise.findSiReglementDansRemise(taDoc.getCodeDocument()).size()==0){
							nouvellePiece = true;

							if(nouvellePiece) {
								exportationEpicea.numeroDePiece++;
								nouvellePiece = false;
								numFin++;
							}

							//recuperation des informations propre a la facture
							exportationEpicea.codePiece = taDoc.getCodeDocument();
							exportationEpicea.datePiece = taDoc.getDateDocument();
							exportationEpicea.dateEcheance = taDoc.getDateEchDocument();
							if(taDoc.getLibelleDocument()!=null)
								exportationEpicea.libelleLigne = taDoc.getLibelleDocument().replace("\r","").replace("\n","");
							else
								exportationEpicea.libelleLigne = "";
							if(taDoc.getTaCompteBanque()!=null)
								exportationEpicea.cptGeneral = LibConversion.stringToInteger(taDoc.getTaCompteBanque().getCptcomptable());
							if(taDoc.getTaTPaiement()!=null)
								exportationEpicea.cptGeneral =  LibConversion.stringToInteger(taDoc.getTaTPaiement().getCompte());
							

							exportationEpicea.montant = LibConversion.BigDecimalToDouble(taDoc.getNetTtcCalc().abs());
							if(taDoc.getNetTtcCalc().signum()>0)
								exportationEpicea.sens="D";
							else exportationEpicea.sens="C";
							

							exportationEpicea.qte1 = 0d;
							
							exportationEpicea.journal=taDoc.getCodeDocument().substring(0, 2);

							exportationEpicea.etablissement ="";
							exportationEpicea.qualifPiece ="";
							exportationEpicea.affaire ="";
							exportationEpicea.dateExterne = null;
							exportationEpicea.refExterne="";

							//ecriture de la ligne d'entete dans le fichier d'export
							fw.write(exportationEpicea.creationLigneCEGID(
									exportationEpicea.datePiece,
									exportationEpicea.journal ,
									exportationEpicea.cptGeneral ,
									exportationEpicea.cptAuxiliaire ,
									exportationEpicea.sens ,
									exportationEpicea.montant ,
									exportationEpicea.libelleLigne ,
									exportationEpicea.codePiece ,
									exportationEpicea.etablissement ,
									exportationEpicea.qualifPiece ,
									exportationEpicea.affaire ,
									exportationEpicea.qte1 ,
									exportationEpicea.dateEcheance ,
									exportationEpicea.dateExterne ,
									exportationEpicea.refExterne ));
							fw.write(exportationEpicea.finDeLigne);

							exportationEpicea.cptAuxiliaire="";
							exportationEpicea.qte1 = 0d;
							
							exportationEpicea.codePiece = taDoc.getCodeDocument();
							exportationEpicea.datePiece = taDoc.getDateDocument();
							exportationEpicea.dateEcheance = taDoc.getDateEchDocument();
							if(taDoc.getLibelleDocument()!=null)
								exportationEpicea.libelleLigne = taDoc.getLibelleDocument().replace("\r","").replace("\n","");
							else
								exportationEpicea.libelleLigne = "";
							if(taDoc.getTaCompteBanque()!=null)
								exportationEpicea.cptGeneral = LibConversion.stringToInteger(taDoc.getTaCompteBanque().getCptcomptable());
							if(taDoc.getTaTPaiement()!=null)
								exportationEpicea.cptGeneral =  LibConversion.stringToInteger(taDoc.getTaTPaiement().getCompte());
							

							exportationEpicea.montant = LibConversion.BigDecimalToDouble(taDoc.getNetTtcCalc().abs());
							if(taDoc.getNetTtcCalc().signum()>0)
								exportationEpicea.sens="D";
							else exportationEpicea.sens="C";
							

							exportationEpicea.qte1 = 0d;
							
							exportationEpicea.journal=taDoc.getCodeDocument().substring(0, 2);

							exportationEpicea.etablissement ="";
							exportationEpicea.qualifPiece ="";
							exportationEpicea.affaire ="";
							exportationEpicea.dateExterne = null;
							exportationEpicea.refExterne="";
							

							//traitement de la ligne de contrepartie (tiers)
							if(taDoc.getLibelleDocument()!=null)
								exportationEpicea.libelleLigne = taDoc.getLibelleDocument().replace("\r","").replace("\n","");//libelle de la ligne de facture
							else
								exportationEpicea.libelleLigne = "";
							exportationEpicea.qte1 = 0d;

							exportationEpicea.cptGeneral = LibConversion.stringToInteger(taDoc.getTaTiers().getCompte()); 
							exportationEpicea.cptAuxiliaire = taDoc.getTaTiers().getCodeCompta();

							exportationEpicea.montant = LibConversion.BigDecimalToDouble(taDoc.getNetTtcCalc().abs());
							if(taDoc.getNetTtcCalc().signum()>0)
								exportationEpicea.sens="C";
							else exportationEpicea.sens="D";
							

							exportationEpicea.qte1 = 0d;
							
							exportationEpicea.journal=taDoc.getCodeDocument().substring(0, 2);

							exportationEpicea.etablissement ="";
							exportationEpicea.qualifPiece ="";
							exportationEpicea.affaire ="";
							exportationEpicea.dateExterne = null;
							exportationEpicea.refExterne="";
							

							fw.write(exportationEpicea.creationLigneCEGID(
									exportationEpicea.datePiece,
									exportationEpicea.journal ,
									exportationEpicea.cptGeneral ,
									exportationEpicea.cptAuxiliaire ,
									exportationEpicea.sens ,
									exportationEpicea.montant ,
									exportationEpicea.libelleLigne ,
									exportationEpicea.codePiece ,
									exportationEpicea.etablissement ,
									exportationEpicea.qualifPiece ,
									exportationEpicea.affaire ,
									exportationEpicea.qte1 ,
									exportationEpicea.dateEcheance ,
									exportationEpicea.dateExterne ,
									exportationEpicea.refExterne ));
							fw.write(exportationEpicea.finDeLigne);

//							if(gererLesPointages){
//								exportationEpicea.typePiece = exportationEpicea.typePointage;	
//								//traitement des lignes pour les pointages
//								for (TaRReglement lf : taDoc.getTaRReglements()) {
//									//récupération des valeurs propres à la ligne
//									TaFacture fac =lf.getTaFacture();
//									numFin=initPointage(fac, taDoc, lf, gererLesDocumenstLies, idAppelant, numFin);
//								} 
//								/**** Attention !!! rajouter temporairement pour les règlements unique dans les factures*/
//								if(taDoc.getTaFactures()!=null && !taDoc.getTaFactures().isEmpty()) {
//									for (TaFacture obj : taDoc.getTaFactures()) {
//										//récupération des valeurs propres à la ligne
//										IRelationDoc r=new TaRReglement();
//										r.setAffectation(obj.getNetTtcCalc());
//										numFin=initPointage(obj, taDoc, r, gererLesDocumenstLies, idAppelant, numFin);
//									}
//								} 
//								if(taFactureDirect!=null) {
//									//récupération des valeurs propres à la ligne
//									IRelationDoc r=new TaRReglement();
//									r.setAffectation(taFactureDirect.getNetTtcCalc());
//									numFin=initPointage(taFactureDirect, taDoc, r, gererLesDocumenstLies, idAppelant, numFin);
//								} 								
//							}

							listComplete.add(taDoc.getCodeDocument());
							exportationEpicea.listCompleteDoc.add(taDoc);
							exportationEpicea.listeVerif.add(taDoc);
						}
					}
				}
			}

		} catch(IOException ioe){
			logger.error("",ioe);
			exportationEpicea.setRetour(false);
		} 

		return numFin;
	}
	
	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportRemiseJPA(List<TaRemise> idRemiseAExporter,List<String> listComplete,Writer fw,
			int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesDocumenstLies) throws Exception {
		int numFin=numDepart;
		try {      

			Boolean gererLesPointages = gererPointages;

//			TaRemiseDAO taDocDAO = new TaRemiseDAO(em);
			TaRemise taDoc = null;


			exportationEpicea.numeroDePiece = numFin;

			

			boolean nouvellePiece = false;

//			EntityTransaction transaction = taDocDAO.getEntityManager().getTransaction();
//			taDocDAO.begin(transaction);

			for(int i=0; i<idRemiseAExporter.size(); i++) {
				taDoc = idRemiseAExporter.get(i);

				if(listComplete.indexOf(taDoc.getCodeDocument())==-1){

					if(taDoc.getDateExport()==null || reExport==1) { /* remise pas deja exportee ou reexportation */
						nouvellePiece = true;

						if(nouvellePiece) {
							exportationEpicea.numeroDePiece++;
							nouvellePiece = false;
							numFin++;
						}

						//recuperation des informations propre a la REMISE
						exportationEpicea.codePiece = taDoc.getCodeDocument();
						exportationEpicea.datePiece = taDoc.getDateDocument();
						exportationEpicea.dateEcheance = taDoc.getDateEchDocument();
						exportationEpicea.dateLivraisonLigne = taDoc.getDateLivDocument();
						if(taDoc.getLibelleDocument()!=null)
							exportationEpicea.libelleLigne = taDoc.getLibelleDocument().replace("\r","").replace("\n","");
						else
							exportationEpicea.libelleLigne = "";
						if(taDoc.getTaCompteBanque()!=null)
							exportationEpicea.numCptLigne = taDoc.getTaCompteBanque().getCptcomptable();
						else
						if(taDoc.getTaTPaiement()!=null)
							exportationEpicea.numCptLigne = taDoc.getTaTPaiement().getCompte();//taDoc.getTaCompteBanque().getCptcomptable();
						exportationEpicea.cptCollectif = "";
						exportationEpicea.nomTiers = "";
						exportationEpicea.adresse1Tiers = "";
						exportationEpicea.adresse2Tiers ="";
						exportationEpicea.codePostalTiers = "";
						exportationEpicea.villeTiers = "";

						exportationEpicea.mtCreditLigne = 0d;  

						exportationEpicea.numLignePiece = 1;
						exportationEpicea.typePiece = exportationEpicea.TypeReglement;
						exportationEpicea.mtDebitLigne = LibConversion.BigDecimalToDouble(taDoc.getNetTtcCalc());
						exportationEpicea.qte1 = 0d;
						exportationEpicea.qte2 = 0d;
						exportationEpicea.codeTva ="";
						exportationEpicea.tauxTva = 0d;
						exportationEpicea.mtDebitTva = 0d;
						exportationEpicea.mtCreditTva = 0d;
						exportationEpicea.exigibiliteTva = "";

						exportationEpicea.refContrepartie = "";
						exportationEpicea.dateContrepartie = null;
						exportationEpicea.mtContrepartieDebit = 0d;
						exportationEpicea.mtContrepartieCredit = 0d;
						exportationEpicea.mtAffectation = 0d;
						exportationEpicea.codeTvaComplet = "";
						exportationEpicea.refReglement = "";

						//ecriture de la ligne d'entete dans le fichier d'export
						fw.write(exportationEpicea.creationLigne(exportationEpicea.numeroDePiece,
								exportationEpicea.numLignePiece ,
								exportationEpicea.typePiece ,
								exportationEpicea.codePiece ,
								exportationEpicea.datePiece ,
								exportationEpicea.numCptLigne ,
								exportationEpicea.libelleLigne ,
								exportationEpicea.mtDebitLigne ,
								exportationEpicea.mtCreditLigne ,
								exportationEpicea.qte1 ,
								exportationEpicea.qte2 ,
								exportationEpicea.codeTva ,
								exportationEpicea.tauxTva ,
								exportationEpicea.mtCreditTva ,
								exportationEpicea.mtDebitTva ,
								exportationEpicea.dateEcheance ,
								exportationEpicea.cptCollectif ,
								exportationEpicea.nomTiers ,
								exportationEpicea.adresse1Tiers ,
								exportationEpicea.adresse2Tiers ,
								exportationEpicea.codePostalTiers ,
								exportationEpicea.villeTiers ,
								exportationEpicea.exigibiliteTva ,
								exportationEpicea.dateLivraisonLigne,
								exportationEpicea.refContrepartie,
								exportationEpicea.dateContrepartie,
								exportationEpicea.mtContrepartieDebit,
								exportationEpicea.mtContrepartieCredit,
								exportationEpicea.mtAffectation,
								exportationEpicea.codeTvaComplet,
								exportationEpicea.refReglement));
						fw.write(exportationEpicea.finDeLigne);

						//réinitialisation des variables pour le prochain document
						exportationEpicea.cptCollectif ="";
						exportationEpicea.nomTiers ="";
						exportationEpicea.adresse1Tiers ="";
						exportationEpicea.adresse2Tiers ="";
						exportationEpicea.codePostalTiers ="";
						exportationEpicea.villeTiers="";



						//traitement pour exporter les relations avec les factures					
						//traitement des lignes
						for (TaLRemise lf : taDoc.getTaLRemises()) {
							//récupération des valeurs propres à la ligne
							if(lf.getTaReglement()!=null && listComplete.indexOf(lf.getTaReglement().getCodeDocument())==-1){
								//						if(lf.getTaReglement()!=null){//on traite le réglement
								//on traite la ligne de contrepartie (tiers)
								exportationEpicea.typePiece = exportationEpicea.TypeReglement;
								exportationEpicea.codePiece=taDoc.getCodeDocument();
								exportationEpicea.datePiece=taDoc.getDateDocument();
								if(lf.getTaReglement().getLibelleDocument()!=null)
									exportationEpicea.libelleLigne = lf.getTaReglement().getLibelleDocument().replace("\r","").replace("\n","");
								else
									exportationEpicea.libelleLigne = "";
								exportationEpicea.qte1 = 0d;
								exportationEpicea.qte2 = 0d;
								exportationEpicea.numCptLigne = "+"+lf.getTaReglement().getTaTiers().getCodeTiers();
								exportationEpicea.cptCollectif = lf.getTaReglement().getTaTiers().getCompte();
								exportationEpicea.nomTiers = "";
								exportationEpicea.adresse1Tiers = "";
								exportationEpicea.adresse2Tiers = "";
								exportationEpicea.codePostalTiers = "";
								exportationEpicea.villeTiers = "";

								exportationEpicea.codeTva = "";
								exportationEpicea.exigibiliteTva = "";
								exportationEpicea.mtDebitTva = 0d;
								exportationEpicea.mtDebitLigne = 0d;

								exportationEpicea.numLignePiece = exportationEpicea.numLignePiece + 1;
								exportationEpicea.mtCreditLigne = LibConversion.BigDecimalToDouble(lf.getTaReglement().getNetTtcCalc());
								exportationEpicea.mtCreditTva = 0d;

								exportationEpicea.refContrepartie = "";
								exportationEpicea.dateContrepartie = null;
								exportationEpicea.mtContrepartieDebit = 0d;
								exportationEpicea.mtContrepartieCredit = 0d;
								exportationEpicea.mtAffectation = 0d;
								exportationEpicea.codeTvaComplet = "";
								exportationEpicea.refReglement = lf.getTaReglement().getCodeDocument();

								fw.write(exportationEpicea.creationLigne(exportationEpicea.numeroDePiece,
										exportationEpicea.numLignePiece ,
										exportationEpicea.typePiece ,
										exportationEpicea.codePiece ,
										exportationEpicea.datePiece ,
										exportationEpicea.numCptLigne ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.mtDebitLigne,
										exportationEpicea.mtCreditLigne ,
										exportationEpicea.qte1 ,
										exportationEpicea.qte2 ,
										exportationEpicea.codeTva ,
										exportationEpicea.tauxTva ,
										exportationEpicea.mtDebitTva,
										exportationEpicea.mtCreditTva ,
										exportationEpicea.dateEcheance ,
										exportationEpicea.cptCollectif ,
										exportationEpicea.nomTiers ,
										exportationEpicea.adresse1Tiers ,
										exportationEpicea.adresse2Tiers ,
										exportationEpicea.codePostalTiers ,
										exportationEpicea.villeTiers ,
										exportationEpicea.exigibiliteTva ,
										exportationEpicea.dateLivraisonLigne,
										exportationEpicea.refContrepartie,
										exportationEpicea.dateContrepartie,
										exportationEpicea.mtContrepartieDebit,
										exportationEpicea.mtContrepartieCredit,
										exportationEpicea.mtAffectation,
										exportationEpicea.codeTvaComplet,
										exportationEpicea.refReglement));
								fw.write(exportationEpicea.finDeLigne);

								//traitment des pointages si on les gere
								if(gererLesPointages){
									exportationEpicea.typePiece = exportationEpicea.typePointage;	
									for (TaRReglement Relation : lf.getTaReglement().getTaRReglements()) {
										TaFacture fac =Relation.getTaFacture();
										idAppelant=fac.getIdDocument();
										numFin=initPointage(fac, lf.getTaReglement(), Relation, gererLesDocumenstLies, idAppelant, numFin);
									}
								}//fin si on gere les pointages
//								if(lf.getTaReglement().getExport()!=1)
//									lf.getTaReglement().setExport(1);
								exportationEpicea.listCompleteDoc.add(lf.getTaReglement());
								listComplete.add(lf.getTaReglement().getCodeDocument());
								exportationEpicea.listeVerif.add(lf.getTaReglement());
							}//fin on traite le reglement

//							if(lf.getTaAcompte()!=null && listComplete.indexOf(lf.getTaAcompte().getCodeDocument())==-1){
//								//						if(lf.getTaAcompte()!=null){//on traite l'acompte
//								//on traite la ligne de contrepartie (tiers)
//								exportationEpicea.typePiece = exportationEpicea.TypeReglement;
//								exportationEpicea.codePiece=taDoc.getCodeDocument();
//								exportationEpicea.datePiece=taDoc.getDateDocument();
//								exportationEpicea.libelleLigne = lf.getTaAcompte().getLibelleDocument();//libelle de la ligne de facture
//								exportationEpicea.qte1 = 0d;
//								exportationEpicea.qte2 = 0d;
//								exportationEpicea.numCptLigne = "+"+lf.getTaAcompte().getTaTiers().getCodeTiers();
//								exportationEpicea.cptCollectif = lf.getTaAcompte().getTaTiers().getCompte();
//								exportationEpicea.nomTiers = "";
//								exportationEpicea.adresse1Tiers = "";
//								exportationEpicea.adresse2Tiers = "";
//								exportationEpicea.codePostalTiers = "";
//								exportationEpicea.villeTiers = "";
//
//								exportationEpicea.codeTva = "";
//								exportationEpicea.exigibiliteTva = "";
//								exportationEpicea.mtDebitTva = 0d;
//								exportationEpicea.mtDebitLigne = 0d;
//
//								exportationEpicea.numLignePiece = exportationEpicea.numLignePiece + 1;
//								exportationEpicea.mtCreditLigne = LibConversion.BigDecimalToDouble(lf.getTaAcompte().getNetTtcCalc());
//								exportationEpicea.mtCreditTva = 0d;
//
//								exportationEpicea.refContrepartie = "";
//								exportationEpicea.dateContrepartie = null;
//								exportationEpicea.mtContrepartieDebit = 0d;
//								exportationEpicea.mtContrepartieCredit = 0d;
//								exportationEpicea.mtAffectation = 0d;
//
//								fw.write(exportationEpicea.creationLigne(exportationEpicea.numeroDePiece,
//										exportationEpicea.numLignePiece ,
//										exportationEpicea.typePiece ,
//										exportationEpicea.codePiece ,
//										exportationEpicea.datePiece ,
//										exportationEpicea.numCptLigne ,
//										exportationEpicea.libelleLigne ,
//										exportationEpicea.mtDebitLigne,
//										exportationEpicea.mtCreditLigne ,
//										exportationEpicea.qte1 ,
//										exportationEpicea.qte2 ,
//										exportationEpicea.codeTva ,
//										exportationEpicea.tauxTva ,
//										exportationEpicea.mtDebitTva,
//										exportationEpicea.mtCreditTva ,
//										exportationEpicea.dateEcheance ,
//										exportationEpicea.cptCollectif ,
//										exportationEpicea.nomTiers ,
//										exportationEpicea.adresse1Tiers ,
//										exportationEpicea.adresse2Tiers ,
//										exportationEpicea.codePostalTiers ,
//										exportationEpicea.villeTiers ,
//										exportationEpicea.exigibiliteTva ,
//										exportationEpicea.dateLivraisonLigne,
//										exportationEpicea.refContrepartie,
//										exportationEpicea.dateContrepartie,
//										exportationEpicea.mtContrepartieDebit,
//										exportationEpicea.mtContrepartieCredit,
//										exportationEpicea.mtAffectation));
//								fw.write(exportationEpicea.finDeLigne);
//
//								//traitment des pointages si on les gere
//								if(gererLesPointages){
//									exportationEpicea.typePiece = exportationEpicea.typePointage;
//									for (TaRAcompte Relation : lf.getTaAcompte().getTaRAcomptes()) {
//										TaFacture fac =Relation.getTaFacture();
//										numFin=initPointage(fac, lf.getTaAcompte(), Relation, gererLesDocumenstLies, idAppelant, numFin);
//									}		
//								}//fin traitement des pointages
////								if(lf.getTaAcompte().getExport()!=1)
////									lf.getTaAcompte().setExport(1);
//								exportationEpicea.listCompleteDoc.add(lf.getTaAcompte());
//								listComplete.add(lf.getTaAcompte().getCodeDocument());
//								exportationEpicea.listeVerif.add(lf.getTaAcompte());
//							}//fin on traite l'acompte						
							if(lf.getDateExport()==null)lf.setDateExport(new Date());
							
						} 
//						if(taDoc.getExport()!=1)
//							taDoc.setExport(1);
//						daoRemise.merge(taDoc);
						exportationEpicea.listCompleteDoc.add(taDoc);
						listComplete.add(taDoc.getCodeDocument());
						exportationEpicea.listeVerif.add(taDoc);
					}
				}
			}
		} catch(IOException ioe){
			logger.error("",ioe);
			exportationEpicea.setRetour(false);
		} 

		return numFin;

	}


	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportRemiseCEGID(List<TaRemise> idRemiseAExporter,List<String> listComplete,Writer fw,
			int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesDocumenstLies) throws Exception {
		int numFin=numDepart;
		try {      

			Boolean gererLesPointages = gererPointages;

			TaRemise taDoc = null;


			boolean nouvellePiece = false;


			for(int i=0; i<idRemiseAExporter.size(); i++) {
				taDoc = idRemiseAExporter.get(i);

				if(listComplete.indexOf(taDoc.getCodeDocument())==-1){

					if(taDoc.getDateExport()==null || reExport==1) { /* remise pas deja exportee ou reexportation */
						nouvellePiece = true;

						if(nouvellePiece) {
							exportationEpicea.numeroDePiece++;
							nouvellePiece = false;
							numFin++;
						}

						//recuperation des informations propre a la REMISE
						exportationEpicea.codePiece = taDoc.getCodeDocument();
						exportationEpicea.datePiece = taDoc.getDateDocument();
						exportationEpicea.dateEcheance = taDoc.getDateEchDocument();
						if(taDoc.getLibelleDocument()!=null)
							exportationEpicea.libelleLigne = taDoc.getLibelleDocument().replace("\r","").replace("\n","");
						else
							exportationEpicea.libelleLigne = "";
						if(taDoc.getTaCompteBanque()!=null)
							exportationEpicea.cptGeneral = LibConversion.stringToInteger( taDoc.getTaCompteBanque().getCptcomptable());
						else
						if(taDoc.getTaTPaiement()!=null)
							exportationEpicea.cptGeneral = LibConversion.stringToInteger(taDoc.getTaTPaiement().getCompte());
						exportationEpicea.cptAuxiliaire = "";

						exportationEpicea.montant = LibConversion.BigDecimalToDouble(taDoc.getNetTtcCalc().abs());
						
						if(taDoc.getNetTtcCalc().signum()>0)
							exportationEpicea.sens="D";
						else exportationEpicea.sens="C";
						

						exportationEpicea.qte1 = 0d;
						
						exportationEpicea.journal=taDoc.getCodeDocument().substring(0, 2);

						exportationEpicea.etablissement ="";
						exportationEpicea.qualifPiece ="";
						exportationEpicea.affaire ="";
						exportationEpicea.dateExterne = null;
						exportationEpicea.refExterne="";



						//ecriture de la ligne d'entete dans le fichier d'export
						fw.write(exportationEpicea.creationLigneCEGID(
								exportationEpicea.datePiece,
								exportationEpicea.journal ,
								exportationEpicea.cptGeneral ,
								exportationEpicea.cptAuxiliaire ,
								exportationEpicea.sens ,
								exportationEpicea.montant ,
								exportationEpicea.libelleLigne ,
								exportationEpicea.codePiece ,
								exportationEpicea.etablissement ,
								exportationEpicea.qualifPiece ,
								exportationEpicea.affaire ,
								exportationEpicea.qte1 ,
								exportationEpicea.dateEcheance ,
								exportationEpicea.dateExterne ,
								exportationEpicea.refExterne ));
						fw.write(exportationEpicea.finDeLigne);

						exportationEpicea.cptAuxiliaire="";
						exportationEpicea.qte1 = 0d;



						//traitement pour exporter les relations avec les factures					
						//traitement des lignes
						for (TaLRemise lf : taDoc.getTaLRemises()) {
							//récupération des valeurs propres à la ligne
							if(lf.getTaReglement()!=null && listComplete.indexOf(lf.getTaReglement().getCodeDocument())==-1){
								//						if(lf.getTaReglement()!=null){//on traite le réglement
								//on traite la ligne de contrepartie (tiers)
								exportationEpicea.codePiece=taDoc.getCodeDocument();
								exportationEpicea.datePiece=taDoc.getDateDocument();
								if(lf.getTaReglement().getLibelleDocument()!=null)
									exportationEpicea.libelleLigne = lf.getTaReglement().getLibelleDocument().replace("\r","").replace("\n","");
								else
									exportationEpicea.libelleLigne = "";
								exportationEpicea.qte1 = 0d;
								exportationEpicea.cptAuxiliaire = "+"+lf.getTaReglement().getTaTiers().getCodeTiers();
								exportationEpicea.cptGeneral = LibConversion.stringToInteger(lf.getTaReglement().getTaTiers().getCompte());
	
								exportationEpicea.mtCreditLigne = LibConversion.BigDecimalToDouble(lf.getTaReglement().getNetTtcCalc().abs());

								if(lf.getTaReglement().getNetTtcCalc().signum()>0)
									exportationEpicea.sens="C";
								else exportationEpicea.sens="D";
								

								exportationEpicea.qte1 = 0d;
								
								exportationEpicea.journal=taDoc.getCodeDocument().substring(0, 2);

								exportationEpicea.etablissement ="";
								exportationEpicea.qualifPiece ="";
								exportationEpicea.affaire ="";
								exportationEpicea.dateExterne = null;
								exportationEpicea.refExterne="";



								//ecriture de la ligne d'entete dans le fichier d'export
								fw.write(exportationEpicea.creationLigneCEGID(
										exportationEpicea.datePiece,
										exportationEpicea.journal ,
										exportationEpicea.cptGeneral ,
										exportationEpicea.cptAuxiliaire ,
										exportationEpicea.sens ,
										exportationEpicea.montant ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.codePiece ,
										exportationEpicea.etablissement ,
										exportationEpicea.qualifPiece ,
										exportationEpicea.affaire ,
										exportationEpicea.qte1 ,
										exportationEpicea.dateEcheance ,
										exportationEpicea.dateExterne ,
										exportationEpicea.refExterne ));
								fw.write(exportationEpicea.finDeLigne);

//								//traitment des pointages si on les gere
//								if(gererLesPointages){
//									exportationEpicea.typePiece = exportationEpicea.typePointage;	
//									for (TaRReglement Relation : lf.getTaReglement().getTaRReglements()) {
//										TaFacture fac =Relation.getTaFacture();
//										idAppelant=fac.getIdDocument();
//										numFin=initPointage(fac, lf.getTaReglement(), Relation, gererLesDocumenstLies, idAppelant, numFin);
//									}
//								}//fin si on gere les pointages

								exportationEpicea.listCompleteDoc.add(lf.getTaReglement());
								listComplete.add(lf.getTaReglement().getCodeDocument());
								exportationEpicea.listeVerif.add(lf.getTaReglement());
							}//fin on traite le reglement

		
							if(lf.getDateExport()==null)lf.setDateExport(new Date());
							
						} 

						exportationEpicea.listCompleteDoc.add(taDoc);
						listComplete.add(taDoc.getCodeDocument());
						exportationEpicea.listeVerif.add(taDoc);
					}
				}
			}
		} catch(IOException ioe){
			logger.error("",ioe);
			exportationEpicea.setRetour(false);
		} 

		return numFin;

	}
	

	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportAvoirJPA(List<TaAvoir> idFactureAExporter,List<String> listComplete,Writer fw,
			int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesDocumenstLies) throws Exception {
		int numFin=numDepart;
		try {      


			Boolean gererLesPointages = gererPointages;

			TaAvoir taAvoir = null;


			exportationEpicea.numeroDePiece = numFin;



			boolean nouvellePiece = false;

			for(int i=0; i<idFactureAExporter.size(); i++) {
				taAvoir = idFactureAExporter.get(i);
				if(listComplete.indexOf(taAvoir.getCodeDocument())==-1){

					/**/   // pour compenser les problèmes d'arrondis mis sur des lignes à 0
					taAvoir.calculeTvaEtTotaux();
					/**/  //isa le 03 février 2011 en accord avec Nicolas

					if(taAvoir.getDateExport()==null || reExport==1) { /* facture pas deja exportee ou reexportation */
						nouvellePiece = true;

						if(nouvellePiece) {
							exportationEpicea.numeroDePiece++;
							nouvellePiece = false;
							numFin++;
						}

						//recuperation des informations propre a la facture
						exportationEpicea.codePiece = taAvoir.getCodeDocument();
						exportationEpicea.datePiece = taAvoir.getDateDocument();
						exportationEpicea.dateEcheance = taAvoir.getDateEchDocument();
						exportationEpicea.dateLivraisonLigne = taAvoir.getDateDocument();//changer pour erreur 18000 dans epicéa;
						exportationEpicea.libelleLigne = taAvoir.getTaInfosDocument().getNomTiers();
						exportationEpicea.numCptLigne = taAvoir.getTaInfosDocument().getCodeCompta();
						exportationEpicea.cptCollectif = taAvoir.getTaInfosDocument().getCompte();
						exportationEpicea.nomTiers = taAvoir.getTaInfosDocument().getNomTiers();
						exportationEpicea.adresse1Tiers = taAvoir.getTaInfosDocument().getAdresse1();
						exportationEpicea.adresse2Tiers = taAvoir.getTaInfosDocument().getAdresse2();
						exportationEpicea.codePostalTiers = taAvoir.getTaInfosDocument().getCodepostal();
						exportationEpicea.villeTiers = taAvoir.getTaInfosDocument().getVille();
						exportationEpicea.mtCreditLigne = LibConversion.BigDecimalToDouble(taAvoir.getNetTtcCalc());  

						exportationEpicea.numCptLigne = "+"+taAvoir.getTaInfosDocument().getCodeCompta();    
						exportationEpicea.numLignePiece = 1;
						if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_AGRIGEST))
							exportationEpicea.typePiece = exportationEpicea.TypeVente;
						else
						exportationEpicea.typePiece = exportationEpicea.TypeAchat;
						exportationEpicea.mtDebitLigne = 0d;
						exportationEpicea.qte1 = 0d;
						exportationEpicea.qte2 = 0d;
						exportationEpicea.codeTva ="";
						exportationEpicea.tauxTva = 0d;
						exportationEpicea.mtDebitTva = 0d;
						exportationEpicea.mtCreditTva = 0d;
						exportationEpicea.exigibiliteTva = "";

						exportationEpicea.refContrepartie = "";
						exportationEpicea.dateContrepartie = null;
						exportationEpicea.mtContrepartieDebit = 0d;
						exportationEpicea.mtContrepartieCredit = 0d;
						exportationEpicea.mtAffectation = 0d;
						exportationEpicea.codeTvaComplet = "";
						exportationEpicea.refReglement = "";

						//ecriture de la ligne d'entete dans le fichier d'export
						fw.write(exportationEpicea.creationLigne(exportationEpicea.numeroDePiece,
								exportationEpicea.numLignePiece ,
								exportationEpicea.typePiece ,
								exportationEpicea.codePiece ,
								exportationEpicea.datePiece ,
								exportationEpicea.numCptLigne ,
								exportationEpicea.libelleLigne ,
								exportationEpicea.mtDebitLigne ,
								exportationEpicea.mtCreditLigne ,
								exportationEpicea.qte1 ,
								exportationEpicea.qte2 ,
								exportationEpicea.codeTva ,
								exportationEpicea.tauxTva ,
								exportationEpicea.mtCreditTva ,
								exportationEpicea.mtDebitTva ,
								exportationEpicea.dateEcheance ,
								exportationEpicea.cptCollectif ,
								exportationEpicea.nomTiers ,
								exportationEpicea.adresse1Tiers ,
								exportationEpicea.adresse2Tiers ,
								exportationEpicea.codePostalTiers ,
								exportationEpicea.villeTiers ,
								exportationEpicea.exigibiliteTva ,
								exportationEpicea.dateLivraisonLigne,
								exportationEpicea.refContrepartie,
								exportationEpicea.dateContrepartie,
								exportationEpicea.mtContrepartieDebit,
								exportationEpicea.mtContrepartieCredit,
								exportationEpicea.mtAffectation,
								exportationEpicea.codeTvaComplet,
								exportationEpicea.refReglement));
						fw.write(exportationEpicea.finDeLigne);

						//réinitialisation des variables pour le prochain document
						exportationEpicea.cptCollectif ="";
						exportationEpicea.nomTiers ="";
						exportationEpicea.adresse1Tiers ="";
						exportationEpicea.adresse2Tiers ="";
						exportationEpicea.codePostalTiers ="";
						exportationEpicea.villeTiers="";


						//traitement des lignes
						for (TaLAvoir lf : taAvoir.getLignes()) {
							//récupération des valeurs propres à la ligne
							if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_AGRIGEST))
								exportationEpicea.typePiece = exportationEpicea.TypeVente;
							else
							exportationEpicea.typePiece = exportationEpicea.TypeAchat;
							
							if(lf.getTaArticle()!=null   && lf.getMtHtLApresRemiseGlobaleDocument()!=null && lf.getMtHtLApresRemiseGlobaleDocument().signum()!=0){
								exportationEpicea.libelleLigne = lf.getLibLDocument();
								exportationEpicea.qte1 = LibConversion.BigDecimalToDouble(lf.getQteLDocument());
								exportationEpicea.qte2 = LibConversion.BigDecimalToDouble(lf.getQte2LDocument());
								exportationEpicea.numCptLigne = lf.getCompteLDocument();
								if(exportationEpicea.numCptLigne==null || exportationEpicea.numCptLigne.equals("")){
									//transaction.rollback();
									exportationEpicea.setRetour(false);
									exportationEpicea.setMessageRetour("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taAvoir.getCodeDocument());
									throw new Exception("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taAvoir.getCodeDocument());
								}
								//traitement du type de tva du tiers (F ou ue ou horsUE ou Franchise)
								String codeDoc="";
								if(taAvoir.getTaInfosDocument().getCodeTTvaDoc()==null || taAvoir.getTaInfosDocument().getCodeTTvaDoc().equals(""))
									codeDoc="F";
								else codeDoc=taAvoir.getTaInfosDocument().getCodeTTvaDoc();
								TaTTvaDoc tvaDoc =daoTTvaDoc.findByCode(codeDoc);
								String journalTva="";
								if(tvaDoc!=null)journalTva=tvaDoc.getJournalTTvaDoc();
								
								if(codeDoc.equalsIgnoreCase("F"))
									exportationEpicea.codeTva = exportationEpicea.correspondanceCodeTva(codeDoc,lf.getCodeTvaLDocument(),lf.getCodeTvaLDocument(),exportationEpicea.numCptLigne,lf.getMtTtcLApresRemiseGlobaleDocument().subtract(lf.getMtHtLApresRemiseGlobaleDocument()));
								else
									exportationEpicea.codeTva = exportationEpicea.correspondanceCodeTva(codeDoc,lf.getCodeTvaLDocument(),journalTva,exportationEpicea.numCptLigne,lf.getMtTtcLApresRemiseGlobaleDocument().subtract(lf.getMtHtLApresRemiseGlobaleDocument()));	
   								exportationEpicea.codeTvaComplet=lf.getCodeTvaLDocument();
								if(exportationEpicea.codeTva==null)exportationEpicea.codeTva=""; /* corrigé par isa le 11/09/2014*/
								if(lf.getTaArticle().getTaTTva()!=null)  
									exportationEpicea.exigibiliteTva = lf.getTaArticle().getTaTTva().getCodeTTva();
								if (exportationEpicea.exigibiliteTva.equals(""))exportationEpicea.exigibiliteTva="D";
								
								exportationEpicea.tauxTva = LibConversion.BigDecimalToDouble(lf.getTauxTvaLDocument());
								exportationEpicea.mtDebitTva = LibConversion.BigDecimalToDouble(lf.getMtTtcLApresRemiseGlobaleDocument().subtract(lf.getMtHtLApresRemiseGlobaleDocument()));
								exportationEpicea.mtDebitLigne = LibConversion.BigDecimalToDouble(lf.getMtHtLApresRemiseGlobaleDocument());

								exportationEpicea.numLignePiece = exportationEpicea.numLignePiece + 1;
								exportationEpicea.mtCreditLigne = 0d;
								exportationEpicea.mtCreditTva = 0d;
								exportationEpicea.cptCollectif = "";
								exportationEpicea.nomTiers = "";
								exportationEpicea.adresse1Tiers = "";
								exportationEpicea.adresse2Tiers = "";
								exportationEpicea.codePostalTiers = "";
								exportationEpicea.villeTiers = "";
								//ecriture de la ligne dans le fichier d'export

								exportationEpicea.refContrepartie = "";
								exportationEpicea.dateContrepartie = null;
								exportationEpicea.mtContrepartieDebit = 0d;
								exportationEpicea.mtContrepartieCredit = 0d;
								exportationEpicea.mtAffectation = 0d;
								exportationEpicea.refReglement = "";

								fw.write(exportationEpicea.creationLigne(exportationEpicea.numeroDePiece,
										exportationEpicea.numLignePiece ,
										exportationEpicea.typePiece ,
										exportationEpicea.codePiece ,
										exportationEpicea.datePiece ,
										exportationEpicea.numCptLigne ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.mtDebitLigne,
										exportationEpicea.mtCreditLigne ,
										exportationEpicea.qte1 ,
										exportationEpicea.qte2 ,
										exportationEpicea.codeTva ,
										exportationEpicea.tauxTva ,
										exportationEpicea.mtDebitTva,
										exportationEpicea.mtCreditTva ,
										exportationEpicea.dateEcheance ,
										exportationEpicea.cptCollectif ,
										exportationEpicea.nomTiers ,
										exportationEpicea.adresse1Tiers ,
										exportationEpicea.adresse2Tiers ,
										exportationEpicea.codePostalTiers ,
										exportationEpicea.villeTiers ,
										exportationEpicea.exigibiliteTva ,
										exportationEpicea.dateLivraisonLigne,
										exportationEpicea.refContrepartie,
										exportationEpicea.dateContrepartie,
										exportationEpicea.mtContrepartieDebit,
										exportationEpicea.mtContrepartieCredit,
										exportationEpicea.mtAffectation,
										exportationEpicea.codeTvaComplet,
										exportationEpicea.refReglement));
								fw.write(exportationEpicea.finDeLigne);
							}
						} 
						if(gererLesPointages){
							//traitement des lignes pour les pointages
							exportationEpicea.typePiece = exportationEpicea.typePointage;
							for (TaRAvoir lf : taAvoir.getTaRAvoirs()) {
								//récupération des valeurs propres à la ligne
								TaFacture fac =lf.getTaFacture();
								numFin=initPointage(fac, taAvoir, lf, gererLesDocumenstLies, idAppelant, numFin);
							} 
						}

//						if(taAvoir.getExport()!=1)taAvoir.setExport(1);
//						daoAvoir.merge(taAvoir);
						exportationEpicea.listCompleteDoc.add(taAvoir);
						listComplete.add(taAvoir.getCodeDocument());
						exportationEpicea.listeVerif.add(taAvoir);
					}
				}
			}
		} catch(IOException ioe){
			logger.error("",ioe);
			exportationEpicea.setRetour(false);
		} 
		return numFin;
	}

	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportAvoirCEGID(List<TaAvoir> idFactureAExporter,List<String> listComplete,Writer fw,
			int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesDocumenstLies) throws Exception {
		int numFin=numDepart;
		try {      


			Boolean gererLesPointages = gererPointages;

			TaAvoir taAvoir = null;



			boolean nouvellePiece = false;

			for(int i=0; i<idFactureAExporter.size(); i++) {
				taAvoir = idFactureAExporter.get(i);
				if(listComplete.indexOf(taAvoir.getCodeDocument())==-1){

					/**/   // pour compenser les problèmes d'arrondis mis sur des lignes à 0
					taAvoir.calculeTvaEtTotaux();
					/**/  //isa le 03 février 2011 en accord avec Nicolas

					if(taAvoir.getDateExport()==null || reExport==1) { /* facture pas deja exportee ou reexportation */
						nouvellePiece = true;

						if(nouvellePiece) {
							exportationEpicea.numeroDePiece++;
							nouvellePiece = false;
							numFin++;
						}

						//recuperation des informations propre a la facture
						exportationEpicea.codePiece = taAvoir.getCodeDocument();
						exportationEpicea.datePiece = taAvoir.getDateDocument();
						exportationEpicea.dateEcheance = taAvoir.getDateEchDocument();
						exportationEpicea.libelleLigne = taAvoir.getTaInfosDocument().getNomTiers();
						
						exportationEpicea.cptAuxiliaire =taAvoir.getTaInfosDocument().getCodeCompta();   
						exportationEpicea.cptGeneral = LibConversion.stringToInteger(taAvoir.getTaInfosDocument().getCompte());

						if(taAvoir.getNetTtcCalc().signum()>0)
							exportationEpicea.sens="C";
						else exportationEpicea.sens="D";
						
						exportationEpicea.montant = LibConversion.BigDecimalToDouble(taAvoir.getNetTtcCalc().abs());  

						exportationEpicea.qte1 = 0d;
						
						exportationEpicea.journal=taAvoir.getCodeDocument().substring(0, 2);

						exportationEpicea.etablissement ="";
						exportationEpicea.qualifPiece ="";
						exportationEpicea.affaire ="";
						exportationEpicea.dateExterne = null;
						exportationEpicea.refExterne="";

							
						//ecriture de la ligne d'entete dans le fichier d'export
						fw.write(exportationEpicea.creationLigneCEGID(
								exportationEpicea.datePiece,
								exportationEpicea.journal ,
								exportationEpicea.cptGeneral ,
								exportationEpicea.cptAuxiliaire ,
								exportationEpicea.sens ,
								exportationEpicea.montant ,
								exportationEpicea.libelleLigne ,
								exportationEpicea.codePiece ,
								exportationEpicea.etablissement ,
								exportationEpicea.qualifPiece ,
								exportationEpicea.affaire ,
								exportationEpicea.qte1 ,
								exportationEpicea.dateEcheance ,
								exportationEpicea.dateExterne ,
								exportationEpicea.refExterne ));
						fw.write(exportationEpicea.finDeLigne);


						//traitement des lignes
						for (TaLAvoir lf : taAvoir.getLignes()) {
							//récupération des valeurs propres à la ligne                 
							if(lf.getTaArticle()!=null   && lf.getMtHtLApresRemiseGlobaleDocument()!=null && lf.getMtHtLApresRemiseGlobaleDocument().signum()!=0){
								exportationEpicea.cptAuxiliaire="";
								exportationEpicea.qte1 = 0d;
								exportationEpicea.libelleLigne = lf.getLibLDocument();
								exportationEpicea.qte1 = LibConversion.BigDecimalToDouble(lf.getQteLDocument());
								exportationEpicea.cptGeneral = LibConversion.stringToInteger(lf.getCompteLDocument());
								
								if(exportationEpicea.cptGeneral==null || exportationEpicea.cptGeneral.equals("")){
									exportationEpicea.setRetour(false);
									exportationEpicea.setMessageRetour("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taAvoir.getCodeDocument());
									throw new Exception("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taAvoir.getCodeDocument());
								}
								exportationEpicea.montant=LibConversion.BigDecimalToDouble(lf.getMtHtLApresRemiseGlobaleDocument().abs());
								if(lf.getMtHtLApresRemiseGlobaleDocument().signum()>0)
									exportationEpicea.sens="D";
								else exportationEpicea.sens="C";
								
								exportationEpicea.journal=taAvoir.getCodeDocument().substring(0, 2);

								exportationEpicea.etablissement ="";
								exportationEpicea.qualifPiece ="";
								exportationEpicea.affaire ="";
								exportationEpicea.dateExterne = null;
								exportationEpicea.refExterne="";

									
								//ecriture de la ligne d'escompte
								fw.write(exportationEpicea.creationLigneCEGID(
										exportationEpicea.datePiece,
										exportationEpicea.journal ,
										exportationEpicea.cptGeneral ,
										exportationEpicea.cptAuxiliaire ,
										exportationEpicea.sens ,
										exportationEpicea.montant ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.codePiece ,
										exportationEpicea.etablissement ,
										exportationEpicea.qualifPiece ,
										exportationEpicea.affaire ,
										exportationEpicea.qte1 ,
										exportationEpicea.dateEcheance ,
										exportationEpicea.dateExterne ,
										exportationEpicea.refExterne ));
								fw.write(exportationEpicea.finDeLigne);

							}
						} 
						for (LigneTva tva : taAvoir.getLignesTVA()) {
							exportationEpicea.cptAuxiliaire="";
							exportationEpicea.qte1 = 0d;
							TaTva t=daoTvaDoc.findByCode(tva.getCodeTva());
							if(t!=null) {
								exportationEpicea.cptGeneral = LibConversion.stringToInteger(t.getNumcptTva());
								exportationEpicea.libelleLigne = t.getLibelleTva();
							}
							
							if(exportationEpicea.cptGeneral==null || exportationEpicea.cptGeneral.equals("")){
								exportationEpicea.setRetour(false);
								exportationEpicea.setMessageRetour("\nErreur N° 1\n"
										+"le code tva "+tva.getCodeTva()+" n'a pas de compte comptable dans le document : "+taAvoir.getCodeDocument());
								throw new Exception("\nErreur N° 1\n"
										+"le code tva "+tva.getCodeTva()+" n'a pas de compte comptable dans le document : "+taAvoir.getCodeDocument());
							}
							exportationEpicea.montant=LibConversion.BigDecimalToDouble(tva.getMtTva().abs());
							if(tva.getMtTva().signum()>0)
								exportationEpicea.sens="C";
							else exportationEpicea.sens="D";
							

							exportationEpicea.etablissement ="";
							exportationEpicea.qualifPiece ="";
							exportationEpicea.affaire ="";
							exportationEpicea.dateExterne = null;
							exportationEpicea.refExterne="";

								
							//ecriture de la ligne d'escompte
							fw.write(exportationEpicea.creationLigneCEGID(
									exportationEpicea.datePiece,
									exportationEpicea.journal ,
									exportationEpicea.cptGeneral ,
									exportationEpicea.cptAuxiliaire ,
									exportationEpicea.sens ,
									exportationEpicea.montant ,
									exportationEpicea.libelleLigne ,
									exportationEpicea.codePiece ,
									exportationEpicea.etablissement ,
									exportationEpicea.qualifPiece ,
									exportationEpicea.affaire ,
									exportationEpicea.qte1 ,
									exportationEpicea.dateEcheance ,
									exportationEpicea.dateExterne ,
									exportationEpicea.refExterne ));
							fw.write(exportationEpicea.finDeLigne);
					}
//						if(gererLesPointages){
//							//traitement des lignes pour les pointages
//							exportationEpicea.typePiece = exportationEpicea.typePointage;
//							for (TaRAvoir lf : taAvoir.getTaRAvoirs()) {
//								//récupération des valeurs propres à la ligne
//								TaFacture fac =lf.getTaFacture();
//								numFin=initPointage(fac, taAvoir, lf, gererLesDocumenstLies, idAppelant, numFin);
//							} 
//						}


						exportationEpicea.listCompleteDoc.add(taAvoir);
						listComplete.add(taAvoir.getCodeDocument());
						exportationEpicea.listeVerif.add(taAvoir);
					}
				}
			}
		} catch(IOException ioe){
			logger.error("",ioe);
			exportationEpicea.setRetour(false);
		} 
		return numFin;
	}

	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportAvoirSAGE(List<TaAvoir> idFactureAExporter,List<String> listComplete,Writer fw,
			int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesDocumenstLies) throws Exception {
		int numFin=numDepart;
		try {      


			Boolean gererLesPointages = gererPointages;

			TaAvoir taAvoir = null;



			boolean nouvellePiece = false;

			for(int i=0; i<idFactureAExporter.size(); i++) {
				taAvoir = idFactureAExporter.get(i);
				if(listComplete.indexOf(taAvoir.getCodeDocument())==-1){

					/**/   // pour compenser les problèmes d'arrondis mis sur des lignes à 0
					taAvoir.calculeTvaEtTotaux();
					/**/  //isa le 03 février 2011 en accord avec Nicolas

					if(taAvoir.getDateExport()==null || reExport==1) { /* facture pas deja exportee ou reexportation */
						nouvellePiece = true;

						if(nouvellePiece) {
							exportationEpicea.numeroDePiece++;
							nouvellePiece = false;
							numFin++;
						}

						//recuperation des informations propre a la facture
						exportationEpicea.codePiece = taAvoir.getCodeDocument();
						exportationEpicea.datePiece = taAvoir.getDateDocument();
						exportationEpicea.dateEcheance = taAvoir.getDateEchDocument();
						exportationEpicea.libelleLigne = taAvoir.getTaInfosDocument().getNomTiers();
						
						exportationEpicea.cptAuxiliaire =taAvoir.getTaInfosDocument().getCodeCompta();   
						exportationEpicea.cptGeneral = LibConversion.stringToInteger(taAvoir.getTaInfosDocument().getCompte());

						
						if(taAvoir.getNetTtcCalc().signum()>0) {
							exportationEpicea.mtDebitLigne=0d;
							exportationEpicea.mtCreditLigne=LibConversion.BigDecimalToDouble(taAvoir.getNetTtcCalc().abs()); 
						}
						else {
							exportationEpicea.mtDebitLigne=LibConversion.BigDecimalToDouble(taAvoir.getNetTtcCalc().abs()); 
							exportationEpicea.mtCreditLigne=0d;
						}
						
						
						exportationEpicea.journal="VTE";

						exportationEpicea.typeReglement="";//initialisation
						if(taAvoir.getTaTiers()!=null && taAvoir.getTaTiers().getTaTPaiement()!=null)
							exportationEpicea.typeReglement=taAvoir.getTaTiers().getTaTPaiement().getCodeTPaiement();

							
						//ecriture de la ligne d'entete dans le fichier d'export
						fw.write(exportationEpicea.creationLigneSAGE(
								exportationEpicea.journal ,
								exportationEpicea.datePiece,
								exportationEpicea.cptGeneral ,
								exportationEpicea.codePiece ,
								exportationEpicea.cptAuxiliaire ,
								exportationEpicea.libelleLigne ,
								exportationEpicea.mtDebitLigne ,
								exportationEpicea.mtCreditLigne ,
								exportationEpicea.dateEcheance,
								exportationEpicea.typeReglement));
						fw.write(exportationEpicea.finDeLigne);


						//traitement des lignes
						for (TaLAvoir lf : taAvoir.getLignes()) {
							//récupération des valeurs propres à la ligne                 
							if(lf.getTaArticle()!=null   && lf.getMtHtLApresRemiseGlobaleDocument()!=null && lf.getMtHtLApresRemiseGlobaleDocument().signum()!=0){
								exportationEpicea.libelleLigne = lf.getLibLDocument();
								exportationEpicea.cptGeneral = LibConversion.stringToInteger(lf.getCompteLDocument());
								exportationEpicea.cptAuxiliaire ="";
								if(exportationEpicea.cptGeneral==null || exportationEpicea.cptGeneral.equals("")){
									exportationEpicea.setRetour(false);
									exportationEpicea.setMessageRetour("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taAvoir.getCodeDocument());
									throw new Exception("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taAvoir.getCodeDocument());
								}

								
								if(lf.getMtHtLApresRemiseGlobaleDocument().signum()>0) {
									exportationEpicea.mtDebitLigne=LibConversion.BigDecimalToDouble(lf.getMtHtLApresRemiseGlobaleDocument().abs()); 
									exportationEpicea.mtCreditLigne=0d;
								}
								else {
									exportationEpicea.mtDebitLigne=0d; 
									exportationEpicea.mtCreditLigne=LibConversion.BigDecimalToDouble(lf.getMtHtLApresRemiseGlobaleDocument().abs()); 
								}
								

									
								//ecriture de la ligne d'escompte
								fw.write(exportationEpicea.creationLigneSAGE(
										exportationEpicea.journal ,
										exportationEpicea.datePiece,
										exportationEpicea.cptGeneral ,
										exportationEpicea.codePiece ,
										exportationEpicea.cptAuxiliaire ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.mtDebitLigne ,
										exportationEpicea.mtCreditLigne ,
										exportationEpicea.dateEcheance,
										exportationEpicea.typeReglement));
								fw.write(exportationEpicea.finDeLigne);

							}
						} 
						for (LigneTva tva : taAvoir.getLignesTVA()) {
							TaTva t=daoTvaDoc.findByCode(tva.getCodeTva());
							if(t!=null) {
								exportationEpicea.cptGeneral = LibConversion.stringToInteger(t.getNumcptTva());
								exportationEpicea.libelleLigne = t.getLibelleTva();
							}
							exportationEpicea.cptAuxiliaire ="";
							if(exportationEpicea.cptGeneral==null || exportationEpicea.cptGeneral.equals("")){
								exportationEpicea.setRetour(false);
								exportationEpicea.setMessageRetour("\nErreur N° 1\n"
										+"le code tva "+tva.getCodeTva()+" n'a pas de compte comptable dans le document : "+taAvoir.getCodeDocument());
								throw new Exception("\nErreur N° 1\n"
										+"le code tva "+tva.getCodeTva()+" n'a pas de compte comptable dans le document : "+taAvoir.getCodeDocument());
							}

							
							if(tva.getMtTva().signum()>0) {
								exportationEpicea.mtDebitLigne=LibConversion.BigDecimalToDouble(tva.getMtTva().abs()); 
								exportationEpicea.mtCreditLigne=0d;
							}
							else {
								exportationEpicea.mtDebitLigne=0d; 
								exportationEpicea.mtCreditLigne=LibConversion.BigDecimalToDouble(tva.getMtTva().abs()); 
							}
							
								
							//ecriture de la ligne d'escompte
							fw.write(exportationEpicea.creationLigneSAGE(
									exportationEpicea.journal ,
									exportationEpicea.datePiece,
									exportationEpicea.cptGeneral ,
									exportationEpicea.codePiece ,
									exportationEpicea.cptAuxiliaire ,
									exportationEpicea.libelleLigne ,
									exportationEpicea.mtDebitLigne ,
									exportationEpicea.mtCreditLigne ,
									exportationEpicea.dateEcheance,
									exportationEpicea.typeReglement));
							fw.write(exportationEpicea.finDeLigne);
					}


						exportationEpicea.listCompleteDoc.add(taAvoir);
						listComplete.add(taAvoir.getCodeDocument());
						exportationEpicea.listeVerif.add(taAvoir);
					}
				}
			}
		} catch(IOException ioe){
			logger.error("",ioe);
			exportationEpicea.setRetour(false);
		} 
		return numFin;
	}

	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportFactureJPA(List<TaFacture> idFactureAExporter,List<String> listComplete,Writer fw,
			 int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesReglements) throws Exception {
		int numFin=numDepart;
		try { 
			TaFacture taFacture = null;

			exportationEpicea.numeroDePiece = numFin;

			boolean nouvellePiece = false;

			for(int i=0; i<idFactureAExporter.size(); i++) {
				taFacture = idFactureAExporter.get(i);
				if(listComplete.indexOf(taFacture.getCodeDocument())==-1){

					/**/   // pour compenser les problèmes d'arrondis mis sur des lignes à 0
					taFacture.calculeTvaEtTotaux();
					/**/  //isa le 03 février 2011 en accord avec Nicolas


					if(taFacture.getDateExport()==null || reExport==1) { /* facture pas deja exportee ou reexportation */
						nouvellePiece = true;

						if(nouvellePiece) {
							exportationEpicea.numeroDePiece++;
							nouvellePiece = false;
							numFin++;
						}

						//recuperation des informations propre a la facture
						exportationEpicea.codePiece = taFacture.getCodeDocument();
						exportationEpicea.datePiece = taFacture.getDateDocument();
						exportationEpicea.dateEcheance = taFacture.getDateEchDocument();
						exportationEpicea.dateLivraisonLigne = taFacture.getDateDocument();//changer pour erreur 18000 dans epicéa;
						exportationEpicea.libelleLigne = taFacture.getTaInfosDocument().getNomTiers();
						exportationEpicea.numCptLigne = taFacture.getTaInfosDocument().getCodeCompta();
						exportationEpicea.cptCollectif = taFacture.getTaInfosDocument().getCompte();
						exportationEpicea.nomTiers = taFacture.getTaInfosDocument().getNomTiers();
						exportationEpicea.adresse1Tiers = taFacture.getTaInfosDocument().getAdresse1();
						exportationEpicea.adresse2Tiers = taFacture.getTaInfosDocument().getAdresse2();
						exportationEpicea.codePostalTiers = taFacture.getTaInfosDocument().getCodepostal();
						exportationEpicea.villeTiers = taFacture.getTaInfosDocument().getVille();

						exportationEpicea.mtDebitLigne = LibConversion.BigDecimalToDouble(taFacture.getNetTtcCalc());  

						exportationEpicea.numCptLigne = "+"+taFacture.getTaInfosDocument().getCodeCompta();    
						exportationEpicea.numLignePiece = 1;
						exportationEpicea.typePiece = exportationEpicea.TypeVente;
						exportationEpicea.mtCreditLigne = 0d;
						exportationEpicea.qte1 = 0d;
						exportationEpicea.qte2 = 0d;
						exportationEpicea.codeTva ="";
						exportationEpicea.tauxTva = 0d;
						exportationEpicea.mtDebitTva = 0d;
						exportationEpicea.mtCreditTva = 0d;
						exportationEpicea.exigibiliteTva = "";

						exportationEpicea.refContrepartie = "";
						exportationEpicea.dateContrepartie = null;
						exportationEpicea.mtContrepartieDebit = 0d;
						exportationEpicea.mtContrepartieCredit = 0d;
						exportationEpicea.mtAffectation = 0d;
						exportationEpicea.codeTvaComplet = "";
						exportationEpicea.refReglement = "";


							
						//ecriture de la ligne d'entete dans le fichier d'export
						fw.write(exportationEpicea.creationLigne(exportationEpicea.numeroDePiece,
								exportationEpicea.numLignePiece ,
								exportationEpicea.typePiece ,
								exportationEpicea.codePiece ,
								exportationEpicea.datePiece ,
								exportationEpicea.numCptLigne ,
								exportationEpicea.libelleLigne ,
								exportationEpicea.mtDebitLigne ,
								exportationEpicea.mtCreditLigne ,
								exportationEpicea.qte1 ,
								exportationEpicea.qte2 ,
								exportationEpicea.codeTva ,
								exportationEpicea.tauxTva ,
								exportationEpicea.mtDebitTva ,
								exportationEpicea.mtCreditTva ,
								exportationEpicea.dateEcheance ,
								exportationEpicea.cptCollectif ,
								exportationEpicea.nomTiers ,
								exportationEpicea.adresse1Tiers ,
								exportationEpicea.adresse2Tiers ,
								exportationEpicea.codePostalTiers ,
								exportationEpicea.villeTiers ,
								exportationEpicea.exigibiliteTva ,
								exportationEpicea.dateLivraisonLigne,
								exportationEpicea.refContrepartie,
								exportationEpicea.dateContrepartie,
								exportationEpicea.mtContrepartieDebit,
								exportationEpicea.mtContrepartieCredit,
								exportationEpicea.mtAffectation,
								exportationEpicea.codeTvaComplet,
								exportationEpicea.refReglement));
						fw.write(exportationEpicea.finDeLigne);
						

						//réinitialisation des variables pour le prochain document
						exportationEpicea.cptCollectif ="";
						exportationEpicea.nomTiers ="";
						exportationEpicea.adresse1Tiers ="";
						exportationEpicea.adresse2Tiers ="";
						exportationEpicea.codePostalTiers ="";
						exportationEpicea.villeTiers="";

						BigDecimal escompte = taFacture.getRemTtcDocument();
						if (escompte.signum() > 0) { 
							//création d'une lignes d'escompte
							exportationEpicea.numCptLigne = exportationEpicea.ctpEscompteDebit;
							exportationEpicea.numLignePiece = exportationEpicea.numLignePiece + 1;
							exportationEpicea.typePiece = "V";
							exportationEpicea.libelleLigne = "Escompte "+taFacture.getLibelleDocument();
							exportationEpicea.mtDebitLigne = LibConversion.BigDecimalToDouble(escompte);
							exportationEpicea.mtCreditLigne = 0d;
							exportationEpicea.qte1 = 0d;
							exportationEpicea.qte2 = 0d;
							exportationEpicea.codeTva = "";
							exportationEpicea.tauxTva = 0d;
							exportationEpicea.mtDebitTva = 0d;
							exportationEpicea.mtCreditTva = 0d;
							exportationEpicea.exigibiliteTva = "";
							//ecriture de la ligne d'escompte dans le fichier d'export

							exportationEpicea.refContrepartie = "";
							exportationEpicea.dateContrepartie = null;
							exportationEpicea.mtContrepartieDebit = 0d;
							exportationEpicea.mtContrepartieCredit = 0d;
							exportationEpicea.mtAffectation = 0d;
							exportationEpicea.codeTvaComplet = "";
							exportationEpicea.refReglement = "";

							fw.write(exportationEpicea.creationLigne(exportationEpicea.numeroDePiece,
									exportationEpicea.numLignePiece ,
									exportationEpicea.typePiece ,
									exportationEpicea.codePiece ,
									exportationEpicea.datePiece ,
									exportationEpicea.numCptLigne ,
									exportationEpicea.libelleLigne ,
									exportationEpicea.mtDebitLigne ,
									exportationEpicea.mtCreditLigne ,
									exportationEpicea.qte1 ,
									exportationEpicea.qte2 ,
									exportationEpicea.codeTva ,
									exportationEpicea.tauxTva ,
									exportationEpicea.mtDebitTva ,
									exportationEpicea.mtCreditTva ,
									exportationEpicea.dateEcheance ,
									exportationEpicea.cptCollectif ,
									exportationEpicea.nomTiers ,
									exportationEpicea.adresse1Tiers ,
									exportationEpicea.adresse2Tiers ,
									exportationEpicea.codePostalTiers ,
									exportationEpicea.villeTiers ,
									exportationEpicea.exigibiliteTva ,
									exportationEpicea.dateLivraisonLigne,
									exportationEpicea.refContrepartie,
									exportationEpicea.dateContrepartie,
									exportationEpicea.mtContrepartieDebit,
									exportationEpicea.mtContrepartieCredit,
									exportationEpicea.mtAffectation,
									exportationEpicea.codeTvaComplet,
									exportationEpicea.refReglement));
							fw.write(exportationEpicea.finDeLigne);
						}

						//traitement des lignes
						for (TaLFacture lf : taFacture.getLignes()) {
							//récupération des valeurs propres à la ligne                 
							if(lf.getTaArticle()!=null   && lf.getMtHtLApresRemiseGlobaleDocument()!=null && lf.getMtHtLApresRemiseGlobaleDocument().signum()!=0){
								exportationEpicea.libelleLigne = lf.getLibLDocument();
								exportationEpicea.qte1 = LibConversion.BigDecimalToDouble(lf.getQteLDocument());
								exportationEpicea.qte2 = LibConversion.BigDecimalToDouble(lf.getQte2LDocument());
								exportationEpicea.numCptLigne = lf.getCompteLDocument();
								if(exportationEpicea.numCptLigne==null || exportationEpicea.numCptLigne.equals("")){
									exportationEpicea.setRetour(false);
									exportationEpicea.setMessageRetour("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taFacture.getCodeDocument());
									throw new Exception("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taFacture.getCodeDocument());
								}
								
								//traitement du type de tva du tiers (F ou ue ou horsUE ou Franchise)
									String codeDoc="";
									if(taFacture.getTaInfosDocument().getCodeTTvaDoc()==null || taFacture.getTaInfosDocument().getCodeTTvaDoc().equals(""))
										codeDoc="F";
									else codeDoc=taFacture.getTaInfosDocument().getCodeTTvaDoc();
									TaTTvaDoc tvaDoc =daoTTvaDoc.findByCode(codeDoc);
									String journalTva="";
									if(tvaDoc!=null)journalTva=tvaDoc.getJournalTTvaDoc();
									//si Franchise pas de code tva sur ligne sans code
									// si autre, L ou E sur compte 7 sauf compte 609
									if(codeDoc.equalsIgnoreCase("F"))
										exportationEpicea.codeTva = exportationEpicea.correspondanceCodeTva(codeDoc,lf.getCodeTvaLDocument(),lf.getCodeTvaLDocument(),exportationEpicea.numCptLigne,lf.getMtTtcLApresRemiseGlobaleDocument().subtract(lf.getMtHtLApresRemiseGlobaleDocument()));
									else
										exportationEpicea.codeTva = exportationEpicea.correspondanceCodeTva(codeDoc,lf.getCodeTvaLDocument(),journalTva,exportationEpicea.numCptLigne,lf.getMtTtcLApresRemiseGlobaleDocument().subtract(lf.getMtHtLApresRemiseGlobaleDocument()));	
									exportationEpicea.codeTvaComplet=lf.getCodeTvaLDocument();
									
								if(exportationEpicea.codeTva==null)exportationEpicea.codeTva="";
								if(lf.getTaArticle().getTaTTva()!=null)  
									exportationEpicea.exigibiliteTva = lf.getTaArticle().getTaTTva().getCodeTTva();
								if (exportationEpicea.exigibiliteTva.equals(""))exportationEpicea.exigibiliteTva="D";
								exportationEpicea.tauxTva = LibConversion.BigDecimalToDouble(lf.getTauxTvaLDocument());
								exportationEpicea.mtCreditTva = LibConversion.BigDecimalToDouble(lf.getMtTtcLApresRemiseGlobaleDocument().subtract(lf.getMtHtLApresRemiseGlobaleDocument()));
								exportationEpicea.mtCreditLigne = LibConversion.BigDecimalToDouble(lf.getMtHtLApresRemiseGlobaleDocument());

								exportationEpicea.numLignePiece = exportationEpicea.numLignePiece + 1;
								exportationEpicea.mtDebitLigne = 0d;
								exportationEpicea.mtDebitTva = 0d;
								exportationEpicea.cptCollectif = "";
								exportationEpicea.nomTiers = "";
								exportationEpicea.adresse1Tiers = "";
								exportationEpicea.adresse2Tiers = "";
								exportationEpicea.codePostalTiers = "";
								exportationEpicea.villeTiers = "";
								//ecriture de la ligne dans le fichier d'export

								exportationEpicea.refContrepartie = "";
								exportationEpicea.dateContrepartie = null;
								exportationEpicea.mtContrepartieDebit = 0d;
								exportationEpicea.mtContrepartieCredit = 0d;
								exportationEpicea.mtAffectation = 0d;
								exportationEpicea.refReglement = "";

								fw.write(exportationEpicea.creationLigne(exportationEpicea.numeroDePiece,
										exportationEpicea.numLignePiece ,
										exportationEpicea.typePiece ,
										exportationEpicea.codePiece ,
										exportationEpicea.datePiece ,
										exportationEpicea.numCptLigne ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.mtDebitLigne ,
										exportationEpicea.mtCreditLigne ,
										exportationEpicea.qte1 ,
										exportationEpicea.qte2 ,
										exportationEpicea.codeTva ,
										exportationEpicea.tauxTva ,
										exportationEpicea.mtDebitTva ,
										exportationEpicea.mtCreditTva ,
										exportationEpicea.dateEcheance ,
										exportationEpicea.cptCollectif ,
										exportationEpicea.nomTiers ,
										exportationEpicea.adresse1Tiers ,
										exportationEpicea.adresse2Tiers ,
										exportationEpicea.codePostalTiers ,
										exportationEpicea.villeTiers ,
										exportationEpicea.exigibiliteTva ,
										exportationEpicea.dateLivraisonLigne,
										exportationEpicea.refContrepartie,
										exportationEpicea.dateContrepartie,
										exportationEpicea.mtContrepartieDebit,
										exportationEpicea.mtContrepartieCredit,
										exportationEpicea.mtAffectation,
										exportationEpicea.codeTvaComplet,
										exportationEpicea.refReglement));
								fw.write(exportationEpicea.finDeLigne);
							}
						}

						if(gererLesReglements){
								boolean priseEnChargeAcomptes=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.ACOMPTES);
								boolean priseEnChargeReglementSimple=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.REGLEMENT_SIMPLE);
								boolean priseEnChargeRemise=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.REMISE);

							List<TaRemise> listeRemise = new LinkedList<TaRemise>();
							//récupérer tous les documents directement liés à cette facture
//							if(priseEnChargeAcomptes){
//								for (TaRAcompte rAcompte : taFacture.getTaRAcomptes()) {
//									List<TaAcompte> liste=new LinkedList<TaAcompte>();
//									List<TaRemise> listeRemiseAcompte =new LinkedList<TaRemise>();
//									listeRemiseAcompte=daoRemise.findSiAcompteDansRemise(rAcompte.getTaAcompte().getCodeDocument());
//									if(listeRemiseAcompte.size()==0 && priseEnChargeReglementSimple){
//										liste.add(rAcompte.getTaAcompte());
//										numFin=exportAcompteJPA(liste, listComplete, fw,  exportationEpicea.getReExportAcompte(), numFin, gererPointages,taFacture.getIdDocument(),false);
//									}else{
//										for (TaRemise taRemise : listeRemiseAcompte) {
//											if(listeRemise.indexOf(taRemise)==-1 )
//												listeRemise.add(taRemise);
//										}
//									}
//								}	
//							}
							for (TaRReglement rReglement : taFacture.getTaRReglements()) {
								List<TaReglement> liste=new LinkedList<TaReglement>();
								List<TaRemise> listeRemiseReglement =new LinkedList<TaRemise>();
								listeRemiseReglement=daoRemise.findSiReglementDansRemise(rReglement.getTaReglement().getCodeDocument());


								if(listeRemiseReglement.size()==0 && priseEnChargeReglementSimple){
									liste.add(rReglement.getTaReglement());
									numFin=exportReglementJPA(liste, listComplete, fw,  exportationEpicea.getReExportReglement(), numFin, gererPointages,
											taFacture.getIdDocument(),false,null);
								}else{
									for (TaRemise taRemise : listeRemiseReglement) {
										if(listeRemise.indexOf(taRemise)==-1 )
											listeRemise.add(taRemise);
									}
								}

							}
							/**** Atention Cette partie est rajoutée temporaire jusqu'à gestion des règlements multiples*/
							if (taFacture.getTaReglement()!=null) {
								List<TaReglement> liste=new LinkedList<TaReglement>();
								List<TaRemise> listeRemiseReglement =new LinkedList<TaRemise>();
								listeRemiseReglement=daoRemise.findSiReglementDansRemise(taFacture.getTaReglement().getCodeDocument());


								if(listeRemiseReglement.size()==0 && priseEnChargeReglementSimple){
									liste.add(taFacture.getTaReglement());
									numFin=exportReglementJPA(liste, listComplete, fw,  exportationEpicea.getReExportReglement(), numFin, gererPointages,
											taFacture.getIdDocument(),false,taFacture);
								}else{
									for (TaRemise taRemise : listeRemiseReglement) {
										if(listeRemise.indexOf(taRemise)==-1 )
											listeRemise.add(taRemise);
									}
								}

							}							
							
							if(priseEnChargeRemise){
								if(listeRemise.size()>0){
									numFin=exportRemiseJPA(listeRemise, listComplete, fw,  exportationEpicea.getReExportRemise(), numFin, gererPointages, 
											taFacture.getIdDocument(),false);
								}
							}
							}else if(gererPointages){
//								for (TaRAcompte r : taFacture.getTaRAcomptes()) {
//									numFin=initPointage(taFacture, r.getTaAcompte(), r, false, idAppelant, numFin);
//								}
								for (TaRAvoir r : taFacture.getTaRAvoirs()) {
									numFin=initPointage(taFacture, r.getTaAvoir(), r, false, idAppelant, numFin);
								}
								for (TaRReglement r : taFacture.getTaRReglements()) {
									numFin=initPointage(taFacture, r.getTaReglement(), r, false, idAppelant, numFin);
								}
								if(taFacture.getTaReglement()!=null) {
									IRelationDoc r=new TaRReglement();
									r.setAffectation(taFacture.getTaReglement().getNetTtcCalc());
									numFin=initPointage(taFacture, taFacture.getTaReglement(), r, false, idAppelant, numFin);
								}							
							}
								
//							if(taFacture.getExport()!=1)taFacture.setExport(1);
//							dao.merge(taFacture);
							listComplete.add(taFacture.getCodeDocument());
							exportationEpicea.listCompleteDoc.add(taFacture);
							exportationEpicea.listeVerif.add(taFacture);
						}
					}
//					if(monitor!=null) monitor.worked(1);
				}

			} catch(IOException ioe){
				logger.error("",ioe);
				exportationEpicea.setRetour(false);
			} 

			return numFin;
		}
	
	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportFactureCEGID(List<TaFacture> idFactureAExporter,List<String> listComplete,Writer fw,
			 int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesReglements) throws Exception {
		int numFin=numDepart;
		try { 
			TaFacture taFacture = null;
			Map<String, BigDecimal> listeTva=new LinkedHashMap<String, BigDecimal>();

			exportationEpicea.numeroDePiece = numFin;

			boolean nouvellePiece = false;

			for(int i=0; i<idFactureAExporter.size(); i++) {
				taFacture = idFactureAExporter.get(i);
				if(listComplete.indexOf(taFacture.getCodeDocument())==-1){

					/**/   // pour compenser les problèmes d'arrondis mis sur des lignes à 0
					taFacture.calculeTvaEtTotaux();
					/**/  //isa le 03 février 2011 en accord avec Nicolas


					if(taFacture.getDateExport()==null || reExport==1) { /* facture pas deja exportee ou reexportation */
						nouvellePiece = true;

						if(nouvellePiece) {
							exportationEpicea.numeroDePiece++;
							nouvellePiece = false;
							numFin++;
						}

						//recuperation des informations propre à la facture
						exportationEpicea.codePiece = taFacture.getCodeDocument();
						exportationEpicea.datePiece = taFacture.getDateDocument();
						exportationEpicea.dateEcheance = taFacture.getDateEchDocument();
						exportationEpicea.libelleLigne = taFacture.getTaInfosDocument().getNomTiers();
						
						exportationEpicea.cptAuxiliaire =taFacture.getTaInfosDocument().getCodeCompta();   
						exportationEpicea.cptGeneral = LibConversion.stringToInteger(taFacture.getTaInfosDocument().getCompte());

						if(taFacture.getNetTtcCalc().signum()>0)
							exportationEpicea.sens="D";
						else exportationEpicea.sens="C";
						
						exportationEpicea.montant = LibConversion.BigDecimalToDouble(taFacture.getNetTtcCalc().abs());  

						exportationEpicea.qte1 = 0d;
						
						exportationEpicea.journal=taFacture.getCodeDocument().substring(0, 2);

						exportationEpicea.etablissement ="";
						exportationEpicea.qualifPiece ="";
						exportationEpicea.affaire ="";
						exportationEpicea.dateExterne = null;
						exportationEpicea.refExterne="";

							
						//ecriture de la ligne d'entete dans le fichier d'export
						fw.write(exportationEpicea.creationLigneCEGID(
								exportationEpicea.datePiece,
								exportationEpicea.journal ,
								exportationEpicea.cptGeneral ,
								exportationEpicea.cptAuxiliaire ,
								exportationEpicea.sens ,
								exportationEpicea.montant ,
								exportationEpicea.libelleLigne ,
								exportationEpicea.codePiece ,
								exportationEpicea.etablissement ,
								exportationEpicea.qualifPiece ,
								exportationEpicea.affaire ,
								exportationEpicea.qte1 ,
								exportationEpicea.dateEcheance ,
								exportationEpicea.dateExterne ,
								exportationEpicea.refExterne ));
						fw.write(exportationEpicea.finDeLigne);
						



						BigDecimal escompte = taFacture.getRemTtcDocument();
						if (escompte.signum() > 0) { 
							//création d'une lignes d'escompte
							exportationEpicea.cptAuxiliaire="";
							exportationEpicea.cptGeneral = LibConversion.stringToInteger(exportationEpicea.ctpEscompteDebit);
							exportationEpicea.libelleLigne = "Escompte "+taFacture.getLibelleDocument();
							
							exportationEpicea.montant = LibConversion.BigDecimalToDouble(escompte.abs());
							if(escompte.signum()>0)
								exportationEpicea.sens="C";
							else exportationEpicea.sens="D";
							

							exportationEpicea.qte1 = 0d;
							

							exportationEpicea.etablissement ="";
							exportationEpicea.qualifPiece ="";
							exportationEpicea.affaire ="";
							exportationEpicea.dateExterne = null;
							exportationEpicea.refExterne="";

								
							//ecriture de la ligne d'escompte
							fw.write(exportationEpicea.creationLigneCEGID(
									exportationEpicea.datePiece,
									exportationEpicea.journal ,
									exportationEpicea.cptGeneral ,
									exportationEpicea.cptAuxiliaire ,
									exportationEpicea.sens ,
									exportationEpicea.montant ,
									exportationEpicea.libelleLigne ,
									exportationEpicea.codePiece ,
									exportationEpicea.etablissement ,
									exportationEpicea.qualifPiece ,
									exportationEpicea.affaire ,
									exportationEpicea.qte1 ,
									exportationEpicea.dateEcheance ,
									exportationEpicea.dateExterne ,
									exportationEpicea.refExterne ));
							fw.write(exportationEpicea.finDeLigne);

						}

						//traitement des lignes
						for (TaLFacture lf : taFacture.getLignes()) {
							//récupération des valeurs propres à la ligne                 
							if(lf.getTaArticle()!=null   && lf.getMtHtLApresRemiseGlobaleDocument()!=null && lf.getMtHtLApresRemiseGlobaleDocument().signum()!=0){
								exportationEpicea.cptAuxiliaire="";
								exportationEpicea.qte1 = 0d;
								exportationEpicea.libelleLigne = lf.getLibLDocument();
								exportationEpicea.qte1 = LibConversion.BigDecimalToDouble(lf.getQteLDocument());
								exportationEpicea.cptGeneral = LibConversion.stringToInteger(lf.getCompteLDocument());
								
								if(exportationEpicea.cptGeneral==null || exportationEpicea.cptGeneral.equals("")){
									exportationEpicea.setRetour(false);
									exportationEpicea.setMessageRetour("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taFacture.getCodeDocument());
									throw new Exception("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taFacture.getCodeDocument());
								}
								exportationEpicea.montant=LibConversion.BigDecimalToDouble(lf.getMtHtLApresRemiseGlobaleDocument().abs());
								if(lf.getMtHtLApresRemiseGlobaleDocument().signum()>0)
									exportationEpicea.sens="C";
								else exportationEpicea.sens="D";
								

								exportationEpicea.etablissement ="";
								exportationEpicea.qualifPiece ="";
								exportationEpicea.affaire ="";
								exportationEpicea.dateExterne = null;
								exportationEpicea.refExterne="";

									
								//ecriture de la ligne d'escompte
								fw.write(exportationEpicea.creationLigneCEGID(
										exportationEpicea.datePiece,
										exportationEpicea.journal ,
										exportationEpicea.cptGeneral ,
										exportationEpicea.cptAuxiliaire ,
										exportationEpicea.sens ,
										exportationEpicea.montant ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.codePiece ,
										exportationEpicea.etablissement ,
										exportationEpicea.qualifPiece ,
										exportationEpicea.affaire ,
										exportationEpicea.qte1 ,
										exportationEpicea.dateEcheance ,
										exportationEpicea.dateExterne ,
										exportationEpicea.refExterne ));
								fw.write(exportationEpicea.finDeLigne);


							}
						}
						for (LigneTva tva : taFacture.getLignesTVA()) {
								exportationEpicea.cptAuxiliaire="";
								exportationEpicea.qte1 = 0d;
								TaTva t=daoTvaDoc.findByCode(tva.getCodeTva());
								if(t!=null) {
									exportationEpicea.cptGeneral = LibConversion.stringToInteger(t.getNumcptTva());
									exportationEpicea.libelleLigne = t.getLibelleTva();
								}
								
								if(exportationEpicea.cptGeneral==null || exportationEpicea.cptGeneral.equals("")){
									exportationEpicea.setRetour(false);
									exportationEpicea.setMessageRetour("\nErreur N° 1\n"
											+"le code tva "+tva.getCodeTva()+" n'a pas de compte comptable dans le document : "+taFacture.getCodeDocument());
									throw new Exception("\nErreur N° 1\n"
											+"le code tva "+tva.getCodeTva()+" n'a pas de compte comptable dans le document : "+taFacture.getCodeDocument());
								}
								exportationEpicea.montant=LibConversion.BigDecimalToDouble(tva.getMtTva().abs());
								if(tva.getMtTva().signum()>0)
									exportationEpicea.sens="C";
								else exportationEpicea.sens="D";
								

								exportationEpicea.etablissement ="";
								exportationEpicea.qualifPiece ="";
								exportationEpicea.affaire ="";
								exportationEpicea.dateExterne = null;
								exportationEpicea.refExterne="";

									
								//ecriture de la ligne d'escompte
								fw.write(exportationEpicea.creationLigneCEGID(
										exportationEpicea.datePiece,
										exportationEpicea.journal ,
										exportationEpicea.cptGeneral ,
										exportationEpicea.cptAuxiliaire ,
										exportationEpicea.sens ,
										exportationEpicea.montant ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.codePiece ,
										exportationEpicea.etablissement ,
										exportationEpicea.qualifPiece ,
										exportationEpicea.affaire ,
										exportationEpicea.qte1 ,
										exportationEpicea.dateEcheance ,
										exportationEpicea.dateExterne ,
										exportationEpicea.refExterne ));
								fw.write(exportationEpicea.finDeLigne);
						}
						
						if(gererLesReglements){
								boolean priseEnChargeAcomptes=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.ACOMPTES);
								boolean priseEnChargeReglementSimple=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.REGLEMENT_SIMPLE);
								boolean priseEnChargeRemise=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.REMISE);

							List<TaRemise> listeRemise = new LinkedList<TaRemise>();

							for (TaRReglement rReglement : taFacture.getTaRReglements()) {
								List<TaReglement> liste=new LinkedList<TaReglement>();
								List<TaRemise> listeRemiseReglement =new LinkedList<TaRemise>();
								listeRemiseReglement=daoRemise.findSiReglementDansRemise(rReglement.getTaReglement().getCodeDocument());


								if(listeRemiseReglement.size()==0 && priseEnChargeReglementSimple){
									liste.add(rReglement.getTaReglement());
									numFin=exportReglementCEGID(liste, listComplete, fw,  exportationEpicea.getReExportReglement(), numFin, gererPointages,
											taFacture.getIdDocument(),false,null);
								}else{
									for (TaRemise taRemise : listeRemiseReglement) {
										if(listeRemise.indexOf(taRemise)==-1 )
											listeRemise.add(taRemise);
									}
								}

							}
							/**** Atention Cette partie est rajoutée temporaire jusqu'à gestion des règlements multiples*/
							if (taFacture.getTaReglement()!=null) {
								List<TaReglement> liste=new LinkedList<TaReglement>();
								List<TaRemise> listeRemiseReglement =new LinkedList<TaRemise>();
								listeRemiseReglement=daoRemise.findSiReglementDansRemise(taFacture.getTaReglement().getCodeDocument());


								if(listeRemiseReglement.size()==0 && priseEnChargeReglementSimple){
									liste.add(taFacture.getTaReglement());
									numFin=exportReglementCEGID(liste, listComplete, fw,  exportationEpicea.getReExportReglement(), numFin, gererPointages,
											taFacture.getIdDocument(),false,taFacture);
								}else{
									for (TaRemise taRemise : listeRemiseReglement) {
										if(listeRemise.indexOf(taRemise)==-1 )
											listeRemise.add(taRemise);
									}
								}

							}							
							
							if(priseEnChargeRemise){
								if(listeRemise.size()>0){
									numFin=exportRemiseCEGID(listeRemise, listComplete, fw,  exportationEpicea.getReExportRemise(), numFin, gererPointages, 
											taFacture.getIdDocument(),false);
								}
							}
							}
//						else if(gererPointages){
////								for (TaRAcompte r : taFacture.getTaRAcomptes()) {
////									numFin=initPointage(taFacture, r.getTaAcompte(), r, false, idAppelant, numFin);
////								}
//								for (TaRAvoir r : taFacture.getTaRAvoirs()) {
//									numFin=initPointage(taFacture, r.getTaAvoir(), r, false, idAppelant, numFin);
//								}
//								for (TaRReglement r : taFacture.getTaRReglements()) {
//									numFin=initPointage(taFacture, r.getTaReglement(), r, false, idAppelant, numFin);
//								}
//								if(taFacture.getTaReglement()!=null) {
//									IRelationDoc r=new TaRReglement();
//									r.setAffectation(taFacture.getTaReglement().getNetTtcCalc());
//									numFin=initPointage(taFacture, taFacture.getTaReglement(), r, false, idAppelant, numFin);
//								}							
//							}
								

							listComplete.add(taFacture.getCodeDocument());
							exportationEpicea.listCompleteDoc.add(taFacture);
							exportationEpicea.listeVerif.add(taFacture);
						}
					}
//					if(monitor!=null) monitor.worked(1);
				}

			} catch(IOException ioe){
				logger.error("",ioe);
				exportationEpicea.setRetour(false);
			} 

			return numFin;
		}
	
	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportFactureSAGE(List<TaFacture> idFactureAExporter,List<String> listComplete,Writer fw,
			 int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesReglements) throws Exception {
		int numFin=numDepart;
		try { 
			TaFacture taFacture = null;
			Map<String, BigDecimal> listeTva=new LinkedHashMap<String, BigDecimal>();

			exportationEpicea.numeroDePiece = numFin;

			boolean nouvellePiece = false;

			for(int i=0; i<idFactureAExporter.size(); i++) {
				taFacture = idFactureAExporter.get(i);
				if(listComplete.indexOf(taFacture.getCodeDocument())==-1){

					/**/   // pour compenser les problèmes d'arrondis mis sur des lignes à 0
					taFacture.calculeTvaEtTotaux();
					/**/  //isa le 03 février 2011 en accord avec Nicolas


					if(taFacture.getDateExport()==null || reExport==1) { /* facture pas deja exportee ou reexportation */
						nouvellePiece = true;

						if(nouvellePiece) {
							exportationEpicea.numeroDePiece++;
							nouvellePiece = false;
							numFin++;
						}

						//recuperation des informations propre à la facture
						exportationEpicea.codePiece = taFacture.getCodeDocument();
						exportationEpicea.datePiece = taFacture.getDateDocument();
						exportationEpicea.dateEcheance = taFacture.getDateEchDocument();
						exportationEpicea.libelleLigne = taFacture.getTaInfosDocument().getNomTiers();
						
						exportationEpicea.cptAuxiliaire =taFacture.getTaInfosDocument().getCodeCompta();   
						exportationEpicea.cptGeneral = LibConversion.stringToInteger(taFacture.getTaInfosDocument().getCompte());

						
						if(taFacture.getNetTtcCalc().signum()>0) {
							exportationEpicea.mtDebitLigne=LibConversion.BigDecimalToDouble(taFacture.getNetTtcCalc().abs());
							exportationEpicea.mtCreditLigne=0d; 
						}
						else {
							exportationEpicea.mtDebitLigne= 0d;
							exportationEpicea.mtCreditLigne=LibConversion.BigDecimalToDouble(taFacture.getNetTtcCalc().abs());
						}
						
//						exportationEpicea.journal=taFacture.getCodeDocument().substring(0, 2);
						exportationEpicea.journal="VTE";

						exportationEpicea.typeReglement="";//initialisation
						if(taFacture.getTaTiers()!=null && taFacture.getTaTiers().getTaTPaiement()!=null)
							exportationEpicea.typeReglement=taFacture.getTaTiers().getTaTPaiement().getCodeTPaiement();
							
						//ecriture de la ligne d'entete dans le fichier d'export
						fw.write(exportationEpicea.creationLigneSAGE(
								exportationEpicea.journal ,
								exportationEpicea.datePiece,
								exportationEpicea.cptGeneral ,
								exportationEpicea.codePiece ,
								exportationEpicea.cptAuxiliaire ,
								exportationEpicea.libelleLigne ,
								exportationEpicea.mtDebitLigne ,
								exportationEpicea.mtCreditLigne ,
								exportationEpicea.dateEcheance,
								exportationEpicea.typeReglement));
						fw.write(exportationEpicea.finDeLigne);
						



						BigDecimal escompte = taFacture.getRemTtcDocument();
						if (escompte.signum() > 0) { 
							//création d'une lignes d'escompte
							exportationEpicea.cptGeneral = LibConversion.stringToInteger(exportationEpicea.ctpEscompteDebit);
							exportationEpicea.libelleLigne = "Escompte "+taFacture.getLibelleDocument();
							exportationEpicea.cptAuxiliaire ="";

							if(escompte.signum()>0) {
								exportationEpicea.mtDebitLigne=LibConversion.BigDecimalToDouble(escompte.abs());
								exportationEpicea.mtCreditLigne= 0d;
							}
							else {
								exportationEpicea.mtDebitLigne=0d; 
								exportationEpicea.mtCreditLigne=LibConversion.BigDecimalToDouble(escompte.abs());
							}

								
							//ecriture de la ligne d'escompte
							fw.write(exportationEpicea.creationLigneSAGE(
									exportationEpicea.journal ,
									exportationEpicea.datePiece,
									exportationEpicea.cptGeneral ,
									exportationEpicea.codePiece ,
									exportationEpicea.cptAuxiliaire ,
									exportationEpicea.libelleLigne ,
									exportationEpicea.mtDebitLigne ,
									exportationEpicea.mtCreditLigne ,
									exportationEpicea.dateEcheance,
									exportationEpicea.typeReglement));
							fw.write(exportationEpicea.finDeLigne);

						}

						//traitement des lignes
						for (TaLFacture lf : taFacture.getLignes()) {
							//récupération des valeurs propres à la ligne                 
							if(lf.getTaArticle()!=null   && lf.getMtHtLApresRemiseGlobaleDocument()!=null && lf.getMtHtLApresRemiseGlobaleDocument().signum()!=0){
								exportationEpicea.libelleLigne = lf.getLibLDocument();
								exportationEpicea.cptGeneral = LibConversion.stringToInteger(lf.getCompteLDocument());
								exportationEpicea.cptAuxiliaire ="";
								if(exportationEpicea.cptGeneral==null || exportationEpicea.cptGeneral.equals("")){
									exportationEpicea.setRetour(false);
									exportationEpicea.setMessageRetour("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taFacture.getCodeDocument());
									throw new Exception("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taFacture.getCodeDocument());
								}
								
								if(lf.getMtHtLApresRemiseGlobaleDocument().signum()>0) {
									exportationEpicea.mtDebitLigne=0d; 
									exportationEpicea.mtCreditLigne=LibConversion.BigDecimalToDouble(lf.getMtHtLApresRemiseGlobaleDocument().abs());
								}
								else {
									exportationEpicea.mtDebitLigne=LibConversion.BigDecimalToDouble(lf.getMtHtLApresRemiseGlobaleDocument().abs());
									exportationEpicea.mtCreditLigne=0d; 
								}

									
								//ecriture de la ligne d'escompte
								fw.write(exportationEpicea.creationLigneSAGE(
										exportationEpicea.journal ,
										exportationEpicea.datePiece,
										exportationEpicea.cptGeneral ,
										exportationEpicea.codePiece ,
										exportationEpicea.cptAuxiliaire ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.mtDebitLigne ,
										exportationEpicea.mtCreditLigne ,
										exportationEpicea.dateEcheance,
										exportationEpicea.typeReglement));
								fw.write(exportationEpicea.finDeLigne);


							}
						}
						for (LigneTva tva : taFacture.getLignesTVA()) {
								TaTva t=daoTvaDoc.findByCode(tva.getCodeTva());
								if(t!=null) {
									exportationEpicea.cptGeneral = LibConversion.stringToInteger(t.getNumcptTva());
									exportationEpicea.libelleLigne = t.getLibelleTva();
								}
								exportationEpicea.cptAuxiliaire ="";
								if(exportationEpicea.cptGeneral==null || exportationEpicea.cptGeneral.equals("")){
									exportationEpicea.setRetour(false);
									exportationEpicea.setMessageRetour("\nErreur N° 1\n"
											+"le code tva "+tva.getCodeTva()+" n'a pas de compte comptable dans le document : "+taFacture.getCodeDocument());
									throw new Exception("\nErreur N° 1\n"
											+"le code tva "+tva.getCodeTva()+" n'a pas de compte comptable dans le document : "+taFacture.getCodeDocument());
								}
								
								if(tva.getMtTva().signum()>0) {
									exportationEpicea.mtDebitLigne=0d;
									exportationEpicea.mtCreditLigne=LibConversion.BigDecimalToDouble(tva.getMtTva().abs()); 
								}
								else {
									exportationEpicea.mtDebitLigne=LibConversion.BigDecimalToDouble(tva.getMtTva().abs()); 
									exportationEpicea.mtCreditLigne=0d;
								}
								

									
								//ecriture de la ligne d'escompte
								fw.write(exportationEpicea.creationLigneSAGE(
										exportationEpicea.journal ,
										exportationEpicea.datePiece,
										exportationEpicea.cptGeneral ,
										exportationEpicea.codePiece ,
										exportationEpicea.cptAuxiliaire ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.mtDebitLigne ,
										exportationEpicea.mtCreditLigne ,
										exportationEpicea.dateEcheance,
										exportationEpicea.typeReglement));
								fw.write(exportationEpicea.finDeLigne);
						}
						
							

							listComplete.add(taFacture.getCodeDocument());
							exportationEpicea.listCompleteDoc.add(taFacture);
							exportationEpicea.listeVerif.add(taFacture);
						}
					}
				}

			} catch(IOException ioe){
				logger.error("",ioe);
				exportationEpicea.setRetour(false);
			} 

			return numFin;
		}
	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportFactureISAGRI1(List<TaFacture> idFactureAExporter,List<String> listComplete,Writer fw,
			 int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesReglements) throws Exception {
		int numFin=numDepart;
		try { 
			TaFacture taFacture = null;
			Map<String, BigDecimal> listeTva=new LinkedHashMap<String, BigDecimal>();

			exportationEpicea.numeroDePiece = numFin;

			boolean nouvellePiece = false;

			for(int i=0; i<idFactureAExporter.size(); i++) {
				taFacture = idFactureAExporter.get(i);
				if(listComplete.indexOf(taFacture.getCodeDocument())==-1){

					/**/   // pour compenser les problèmes d'arrondis mis sur des lignes à 0
					taFacture.calculeTvaEtTotaux();
					/**/  //isa le 03 février 2011 en accord avec Nicolas


					if(taFacture.getDateExport()==null || reExport==1) { /* facture pas deja exportee ou reexportation */
						nouvellePiece = true;

						if(nouvellePiece) {
							exportationEpicea.numeroDePiece++;
							nouvellePiece = false;
							numFin++;
							exportationEpicea.numLignePiece=0;
						}

						//recuperation des informations propre à la facture
						exportationEpicea.codePiece = taFacture.getCodeDocument();
						exportationEpicea.datePiece = taFacture.getDateDocument();
//						exportationEpicea.dateEcheance = taFacture.getDateEchDocument();
//						exportationEpicea.libelleLigne = taFacture.getTaInfosDocument().getNomTiers();
						
						exportationEpicea.cptAuxiliaire =taFacture.getTaInfosDocument().getCodeCompta();   
						exportationEpicea.cptGeneral = LibConversion.stringToInteger(taFacture.getTaInfosDocument().getCompte());


						
//						exportationEpicea.journal=taFacture.getCodeDocument().substring(0, 2);
						exportationEpicea.journal=journalFacture;
						if(taFacture.getTaTiers()!=null )exportationEpicea.nomTiers=taFacture.getTaTiers().getNomTiers();
						

						BigDecimal escompte = taFacture.getRemTtcDocument();
						if (escompte.signum() > 0) { 
							//création d'une lignes d'escompte
							exportationEpicea.numCptLigne = exportationEpicea.ctpEscompteDebit;
							exportationEpicea.libelleLigne = "Escompte "+taFacture.getLibelleDocument();

							exportationEpicea.mtTTC=LibConversion.BigDecimalToDouble(escompte);
							exportationEpicea.mtTva=0d;
							exportationEpicea.mtHT=0d;


							exportationEpicea.numLignePiece = exportationEpicea.numLignePiece + 1;
							exportationEpicea.libelleLigne = "Escompte "+taFacture.getLibelleDocument();
							exportationEpicea.qte1 = 0d;
							exportationEpicea.codeTva = "";
							exportationEpicea.tauxTva = 0d;
							exportationEpicea.mtTva = 0d;
							//ecriture de la ligne d'escompte dans le fichier d'export
								
							//ecriture de la ligne d'escompte
							fw.write(exportationEpicea.creationLigneISAGRI1(
									exportationEpicea.numLignePiece,
									exportationEpicea.journal ,
									exportationEpicea.datePiece,
									exportationEpicea.codePiece ,
									exportationEpicea.cptAuxiliaire ,
									exportationEpicea.nomTiers ,
									exportationEpicea.libelleLigne ,
									exportationEpicea.qte1 ,
									exportationEpicea.mtHT ,
									exportationEpicea.mtTva ,
									exportationEpicea.mtTTC ,
									exportationEpicea.numCptLigne,
									exportationEpicea.codeTva));
							fw.write(exportationEpicea.finDeLigne);

						}

						//traitement des lignes
						for (TaLFacture lf : taFacture.getLignes()) {
							//récupération des valeurs propres à la ligne                 
							if(lf.getTaArticle()!=null   && lf.getMtHtLApresRemiseGlobaleDocument()!=null ){
								exportationEpicea.numLignePiece = exportationEpicea.numLignePiece + 1;
								exportationEpicea.libelleLigne = lf.getLibLDocument();
								exportationEpicea.numCptLigne  = lf.getCompteLDocument();
								if(exportationEpicea.numCptLigne==null || exportationEpicea.numCptLigne.equals("")){
									exportationEpicea.setRetour(false);
									exportationEpicea.setMessageRetour("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taFacture.getCodeDocument());
									throw new Exception("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taFacture.getCodeDocument());
								}
								exportationEpicea.mtHT= LibConversion.BigDecimalToDouble(lf.getMtHtLApresRemiseGlobaleDocument()); 
								exportationEpicea.mtTva= LibConversion.BigDecimalToDouble(lf.getMtTtcLApresRemiseGlobaleDocument().subtract(lf.getMtHtLApresRemiseGlobaleDocument())); 
								exportationEpicea.mtTTC= LibConversion.BigDecimalToDouble(lf.getMtTtcLApresRemiseGlobaleDocument()); 
								exportationEpicea.qte1=LibConversion.BigDecimalToDouble(lf.getQteLDocument());
								exportationEpicea.codeTva =lf.getCodeTvaLDocument();

								fw.write(exportationEpicea.creationLigneISAGRI1(
										exportationEpicea.numLignePiece,
										exportationEpicea.journal ,
										exportationEpicea.datePiece,
										exportationEpicea.codePiece ,
										exportationEpicea.cptAuxiliaire ,
										exportationEpicea.nomTiers ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.qte1 ,
										exportationEpicea.mtHT ,
										exportationEpicea.mtTva ,
										exportationEpicea.mtTTC ,
										exportationEpicea.numCptLigne,
										exportationEpicea.codeTva));
								fw.write(exportationEpicea.finDeLigne);


							}
						}
						if(gererLesReglements){
							boolean priseEnChargeAcomptes=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.ACOMPTES);
							boolean priseEnChargeReglementSimple=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.REGLEMENT_SIMPLE);
							boolean priseEnChargeRemise=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.REMISE);

						List<TaRemise> listeRemise = new LinkedList<TaRemise>();

						for (TaRReglement rReglement : taFacture.getTaRReglements()) {
							List<TaReglement> liste=new LinkedList<TaReglement>();
							List<TaRemise> listeRemiseReglement =new LinkedList<TaRemise>();
							listeRemiseReglement=daoRemise.findSiReglementDansRemise(rReglement.getTaReglement().getCodeDocument());


							if(listeRemiseReglement.size()==0 && priseEnChargeReglementSimple){
								liste.add(rReglement.getTaReglement());
								numFin=exportReglementISAGRI1(liste, listComplete, fw,  exportationEpicea.getReExportReglement(), numFin, gererPointages,
										taFacture.getIdDocument(),false,null);
							}else{
								for (TaRemise taRemise : listeRemiseReglement) {
									if(listeRemise.indexOf(taRemise)==-1 )
										listeRemise.add(taRemise);
								}
							}

						}
						/**** Atention Cette partie est rajoutée temporaire jusqu'à gestion des règlements multiples*/
						if (taFacture.getTaReglement()!=null) {
							List<TaReglement> liste=new LinkedList<TaReglement>();
							List<TaRemise> listeRemiseReglement =new LinkedList<TaRemise>();
							listeRemiseReglement=daoRemise.findSiReglementDansRemise(taFacture.getTaReglement().getCodeDocument());


							if(listeRemiseReglement.size()==0 && priseEnChargeReglementSimple){
								liste.add(taFacture.getTaReglement());
								numFin=exportReglementISAGRI1(liste, listComplete, fw,  exportationEpicea.getReExportReglement(), numFin, gererPointages,
										taFacture.getIdDocument(),false,taFacture);
							}else{
								for (TaRemise taRemise : listeRemiseReglement) {
									if(listeRemise.indexOf(taRemise)==-1 )
										listeRemise.add(taRemise);
								}
							}

						}							
						
						if(priseEnChargeRemise){
							if(listeRemise.size()>0){
								numFin=exportRemiseISAGRI1(listeRemise, listComplete, fw,  exportationEpicea.getReExportRemise(), numFin, gererPointages, 
										taFacture.getIdDocument(),false);
							}
						}
						}
						
							listComplete.add(taFacture.getCodeDocument());
							exportationEpicea.listCompleteDoc.add(taFacture);
							exportationEpicea.listeVerif.add(taFacture);
						}
					}
				}

			} catch(IOException ioe){
				logger.error("",ioe);
				exportationEpicea.setRetour(false);
			} 

			return numFin;
		}
	
	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportFactureCentraliseeISAGRI1(List<TaFacture> idFactureAExporter,List<String> listComplete,Writer fw,
			 int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesReglements) throws Exception {
		int numFin=numDepart;
		try { 
			TaFacture taFacture = null;
			Map<String, BigDecimal> listeTva=new LinkedHashMap<String, BigDecimal>();

			exportationEpicea.numeroDePiece = numFin;

			boolean nouvellePiece = false;

			for(int i=0; i<idFactureAExporter.size(); i++) {
				taFacture = idFactureAExporter.get(i);
				if(listComplete.indexOf(taFacture.getCodeDocument())==-1){

					/**/   // pour compenser les problèmes d'arrondis mis sur des lignes à 0
					taFacture.calculeTvaEtTotaux();
					/**/  //isa le 03 février 2011 en accord avec Nicolas


					if(taFacture.getDateExport()==null || reExport==1) { /* facture pas deja exportee ou reexportation */
						nouvellePiece = true;

						if(nouvellePiece) {
							exportationEpicea.numeroDePiece++;
							nouvellePiece = false;
							numFin++;
							exportationEpicea.numLignePiece=0;
						}
						//on crée un nouvel objet facture pour créer la centralisation
						TaFacture newFacture =new TaFacture(false);
						newFacture=(TaFacture) taFacture.cloneCentralisation();
						newFacture.getLignes().clear();
						
						TaLFacture newTaLFacture;
						for (TaLFacture obj : taFacture.getLignes()) {
							newTaLFacture=newFacture.contientMemeParametreCompte(obj);
							if(newTaLFacture!=null){
								newTaLFacture.setQteLDocument(BigDecimal.ONE);
								if(taFacture.getTtc()==1) {
									newTaLFacture.setPrixULDocument(newTaLFacture.getPrixULDocument().add(obj.getMtTtcLApresRemiseGlobaleDocument()));
								}else{
									newTaLFacture.setPrixULDocument(newTaLFacture.getPrixULDocument().add(obj.getMtHtLApresRemiseGlobaleDocument()));
								}
							}else{
								if(obj.getTaArticle()!=null){
								newTaLFacture=new TaLFacture(false);
								newTaLFacture=obj.clone();
								newTaLFacture.setLibLDocument(newFacture.getLibelleDocument());
								newTaLFacture.setQteLDocument(BigDecimal.ONE);
								newTaLFacture.setRemTxLDocument(BigDecimal.ZERO);
								if(taFacture.getTtc()==1) {
									newTaLFacture.setPrixULDocument(obj.getMtTtcLApresRemiseGlobaleDocument());
								}else{
									newTaLFacture.setPrixULDocument(obj.getMtHtLApresRemiseGlobaleDocument());
								}
								newTaLFacture.setTaDocument(newFacture);
								newFacture.addLigne(newTaLFacture);
								}
							}
						}
						newFacture.setTxRemHtDocument(BigDecimal.ZERO);
						newFacture.calculeTvaEtTotaux();
						
						
						//recuperation des informations propre à la facture
						exportationEpicea.codePiece = taFacture.getCodeDocument();
						exportationEpicea.datePiece = taFacture.getDateDocument();
//						exportationEpicea.dateEcheance = taFacture.getDateEchDocument();
//						exportationEpicea.libelleLigne = taFacture.getTaInfosDocument().getNomTiers();
						
						exportationEpicea.cptAuxiliaire =taFacture.getTaInfosDocument().getCodeCompta();   
						exportationEpicea.cptGeneral = LibConversion.stringToInteger(taFacture.getTaInfosDocument().getCompte());


						
//						exportationEpicea.journal=taFacture.getCodeDocument().substring(0, 2);
						exportationEpicea.journal=journalFacture;
						if(taFacture.getTaTiers()!=null )exportationEpicea.nomTiers=taFacture.getTaTiers().getNomTiers();
						

						BigDecimal escompte = taFacture.getRemTtcDocument();
						if (escompte.signum() > 0) { 
							//création d'une lignes d'escompte
							exportationEpicea.numCptLigne = exportationEpicea.ctpEscompteDebit;
							exportationEpicea.libelleLigne = "Escompte "+taFacture.getLibelleDocument();

							exportationEpicea.mtTTC=LibConversion.BigDecimalToDouble(escompte);
							exportationEpicea.mtTva=0d;
							exportationEpicea.mtHT=0d;


							exportationEpicea.numLignePiece = exportationEpicea.numLignePiece + 1;
							exportationEpicea.libelleLigne = "Escompte "+taFacture.getLibelleDocument();
							exportationEpicea.qte1 = 0d;
							exportationEpicea.codeTva = "";
							exportationEpicea.tauxTva = 0d;
							exportationEpicea.mtTva = 0d;
							//ecriture de la ligne d'escompte dans le fichier d'export
								
							//ecriture de la ligne d'escompte
							fw.write(exportationEpicea.creationLigneISAGRI1(
									exportationEpicea.numLignePiece,
									exportationEpicea.journal ,
									exportationEpicea.datePiece,
									exportationEpicea.codePiece ,
									exportationEpicea.cptAuxiliaire ,
									exportationEpicea.nomTiers ,
									exportationEpicea.libelleLigne ,
									exportationEpicea.qte1 ,
									exportationEpicea.mtHT ,
									exportationEpicea.mtTva ,
									exportationEpicea.mtTTC ,
									exportationEpicea.numCptLigne,
									exportationEpicea.codeTva));
							fw.write(exportationEpicea.finDeLigne);

						}

						//traitement des lignes
						for (TaLFacture lf : newFacture.getLignes()) {
							//récupération des valeurs propres à la ligne                 
							if(lf.getTaArticle()!=null   && lf.getMtHtLApresRemiseGlobaleDocument()!=null ){
								exportationEpicea.numLignePiece = exportationEpicea.numLignePiece + 1;
								exportationEpicea.libelleLigne = lf.getLibLDocument();
								exportationEpicea.numCptLigne  = lf.getCompteLDocument();
								if(exportationEpicea.numCptLigne==null || exportationEpicea.numCptLigne.equals("")){
									exportationEpicea.setRetour(false);
									exportationEpicea.setMessageRetour("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taFacture.getCodeDocument());
									throw new Exception("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taFacture.getCodeDocument());
								}
								exportationEpicea.mtHT= LibConversion.BigDecimalToDouble(lf.getMtHtLApresRemiseGlobaleDocument()); 
								exportationEpicea.mtTva= LibConversion.BigDecimalToDouble(lf.getMtTtcLApresRemiseGlobaleDocument().subtract(lf.getMtHtLApresRemiseGlobaleDocument())); 
								exportationEpicea.mtTTC= LibConversion.BigDecimalToDouble(lf.getMtTtcLApresRemiseGlobaleDocument()); 
								exportationEpicea.qte1=LibConversion.BigDecimalToDouble(lf.getQteLDocument());
								exportationEpicea.codeTva =lf.getCodeTvaLDocument();

								fw.write(exportationEpicea.creationLigneISAGRI1(
										exportationEpicea.numLignePiece,
										exportationEpicea.journal ,
										exportationEpicea.datePiece,
										exportationEpicea.codePiece ,
										exportationEpicea.cptAuxiliaire ,
										exportationEpicea.nomTiers ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.qte1 ,
										exportationEpicea.mtHT ,
										exportationEpicea.mtTva ,
										exportationEpicea.mtTTC ,
										exportationEpicea.numCptLigne,
										exportationEpicea.codeTva));
								fw.write(exportationEpicea.finDeLigne);


							}
						}
						
						if(gererLesReglements){
							boolean priseEnChargeAcomptes=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.ACOMPTES);
							boolean priseEnChargeReglementSimple=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.REGLEMENT_SIMPLE);
							boolean priseEnChargeRemise=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.REMISE);

						List<TaRemise> listeRemise = new LinkedList<TaRemise>();

						for (TaRReglement rReglement : taFacture.getTaRReglements()) {
							List<TaReglement> liste=new LinkedList<TaReglement>();
							List<TaRemise> listeRemiseReglement =new LinkedList<TaRemise>();
							listeRemiseReglement=daoRemise.findSiReglementDansRemise(rReglement.getTaReglement().getCodeDocument());


							if(listeRemiseReglement.size()==0 && priseEnChargeReglementSimple){
								liste.add(rReglement.getTaReglement());
								numFin=exportReglementISAGRI1(liste, listComplete, fw,  exportationEpicea.getReExportReglement(), numFin, gererPointages,
										taFacture.getIdDocument(),false,null);
							}else{
								for (TaRemise taRemise : listeRemiseReglement) {
									if(listeRemise.indexOf(taRemise)==-1 )
										listeRemise.add(taRemise);
								}
							}

						}
						/**** Atention Cette partie est rajoutée temporaire jusqu'à gestion des règlements multiples*/
						if (taFacture.getTaReglement()!=null) {
							List<TaReglement> liste=new LinkedList<TaReglement>();
							List<TaRemise> listeRemiseReglement =new LinkedList<TaRemise>();
							listeRemiseReglement=daoRemise.findSiReglementDansRemise(taFacture.getTaReglement().getCodeDocument());


							if(listeRemiseReglement.size()==0 && priseEnChargeReglementSimple){
								liste.add(taFacture.getTaReglement());
								numFin=exportReglementISAGRI1(liste, listComplete, fw,  exportationEpicea.getReExportReglement(), numFin, gererPointages,
										taFacture.getIdDocument(),false,taFacture);
							}else{
								for (TaRemise taRemise : listeRemiseReglement) {
									if(listeRemise.indexOf(taRemise)==-1 )
										listeRemise.add(taRemise);
								}
							}

						}							
						
						if(priseEnChargeRemise){
							if(listeRemise.size()>0){
								numFin=exportRemiseISAGRI1(listeRemise, listComplete, fw,  exportationEpicea.getReExportRemise(), numFin, gererPointages, 
										taFacture.getIdDocument(),false);
							}
						}
						}
						
							listComplete.add(taFacture.getCodeDocument());
							exportationEpicea.listCompleteDoc.add(taFacture);
							exportationEpicea.listeVerif.add(taFacture);
						}
					}
				}

			} catch(IOException ioe){
				logger.error("",ioe);
				exportationEpicea.setRetour(false);
			} 

			return numFin;
		}

	
	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public ExportationEpicea  exportJPA(List<TaFacture> idFactureAExporter,List<TaAvoir> idAvoirAExporter,List<TaApporteur> idApporteurAExporter,List<TaAcompte> 
	idAcompteAExporter,List<TaReglement> idReglementAExporter,List<TaRemise> idRemiseAExporter,boolean gererPointages,
	boolean centralisation,boolean ecraserFichier,String typeClient) throws Exception {
		
		return exportJPA(idFactureAExporter,idAvoirAExporter,idApporteurAExporter,idAcompteAExporter,idReglementAExporter,idRemiseAExporter,
				0,0,0,0,0,0,gererPointages,centralisation,ecraserFichier,typeClient);
	}
	
	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	private int initPointage(IDocumentTiers fac , IDocumentTiers doc2,IRelationDoc relation, boolean gererLesDocumenstLies, 
			Integer idAppelant ,int numDepart) throws Exception{
		int numFin=numDepart++;
		try { 
		if(fac!=null ){
			TaRemise remise=null;
			if(gererLesDocumenstLies
					&& idAppelant==null){
				List<TaFacture> liste = new LinkedList<TaFacture>();
				liste.add((TaFacture)fac);
				exportFactureJPA(liste, exportationEpicea.getListComplete(), fw,  exportationEpicea.getReExportFacture(), numFin, false, doc2.getIdDocument(),false);
				exportationEpicea.getListeVerif().add(fac);
			}
			if(doc2.getTypeDocument()==TaReglement.TYPE_DOC){
				List<TaRemise> liste =daoRemise.findSiReglementDansRemise(doc2.getCodeDocument());
				if(liste.size()>0)remise=liste.get(0);
			}else
				if(doc2.getTypeDocument()==TaAcompte.TYPE_DOC){
					List<TaRemise> liste =daoRemise.findSiAcompteDansRemise(doc2.getCodeDocument());
					if(liste.size()>0)remise=liste.get(0);
				}
			if(idAppelant==null || idAppelant==fac.getIdDocument()){
				exportationEpicea.typePiece=exportationEpicea.typePointage;
				if(remise!=null){
					exportationEpicea.codePiece=remise.getCodeDocument();
					exportationEpicea.datePiece=remise.getDateDocument();
					if(remise.getLibelleDocument()!=null)
						exportationEpicea.libelleLigne = remise.getLibelleDocument().replace("\r","").replace("\n","");//libelle de la ligne de facture
				}
				else {
					exportationEpicea.codePiece=doc2.getCodeDocument();
					exportationEpicea.datePiece=doc2.getDateDocument();
					if(doc2.getLibelleDocument()!=null)
						exportationEpicea.libelleLigne = doc2.getLibelleDocument().replace("\r","").replace("\n","");//libelle de la ligne de facture
				}
				exportationEpicea.qte1 = 0d;
				exportationEpicea.qte2 = 0d;
				exportationEpicea.numCptLigne = "+"+doc2.getTaTiers().getCodeCompta();
				exportationEpicea.cptCollectif = doc2.getTaTiers().getCompte();
				exportationEpicea.nomTiers = "";
				exportationEpicea.adresse1Tiers = "";
				exportationEpicea.adresse2Tiers = "";
				exportationEpicea.codePostalTiers = "";
				exportationEpicea.villeTiers = "";

				exportationEpicea.codeTva = "";
				exportationEpicea.exigibiliteTva = "";
				exportationEpicea.mtDebitTva = 0d;
				exportationEpicea.mtDebitLigne = 0d;

				exportationEpicea.numLignePiece = exportationEpicea.numLignePiece + 1;
				exportationEpicea.mtCreditLigne = LibConversion.BigDecimalToDouble(doc2.getNetTtcCalc());
				exportationEpicea.mtCreditTva = 0d;	

				exportationEpicea.refContrepartie = fac.getCodeDocument();
				exportationEpicea.dateContrepartie = fac.getDateDocument();
				exportationEpicea.mtContrepartieDebit = LibConversion.BigDecimalToDouble(fac.getNetTtcCalc());
				exportationEpicea.mtContrepartieCredit = 0d;
				exportationEpicea.mtAffectation = LibConversion.BigDecimalToDouble(relation.getAffectation());
				exportationEpicea.refReglement = doc2.getCodeDocument();

				fw.write(exportationEpicea.creationLigne(exportationEpicea.numeroDePiece,
						exportationEpicea.numLignePiece ,
						exportationEpicea.typePiece ,
						exportationEpicea.codePiece ,
						exportationEpicea.datePiece ,
						exportationEpicea.numCptLigne ,
						exportationEpicea.libelleLigne ,
						exportationEpicea.mtDebitLigne,
						exportationEpicea.mtCreditLigne ,
						exportationEpicea.qte1 ,
						exportationEpicea.qte2 ,
						exportationEpicea.codeTva ,
						exportationEpicea.tauxTva ,
						exportationEpicea.mtDebitTva,
						exportationEpicea.mtCreditTva ,
						exportationEpicea.dateEcheance ,
						exportationEpicea.cptCollectif ,
						exportationEpicea.nomTiers ,
						exportationEpicea.adresse1Tiers ,
						exportationEpicea.adresse2Tiers ,
						exportationEpicea.codePostalTiers ,
						exportationEpicea.villeTiers ,
						exportationEpicea.exigibiliteTva ,
						exportationEpicea.dateLivraisonLigne,
						exportationEpicea.refContrepartie,
						exportationEpicea.dateContrepartie,
						exportationEpicea.mtContrepartieDebit,
						exportationEpicea.mtContrepartieCredit,
						exportationEpicea.mtAffectation,
						exportationEpicea.codeTvaComplet,
						exportationEpicea.refReglement));
				fw.write(exportationEpicea.getFinDeLigne());
				//je ne l'enregistre pas car crée des conflits si facture avec plusieurs réglements
//				if(relation.getExport()!=1)relation.setExport(1);
				if(remise!=null)doc2=remise;
				exportationEpicea.getListCompletePointage().add(new IDocumentTiers[]{doc2,fac});
				exportationEpicea.listCompleteRelation.add(relation);

			}
		}
		return numFin;
		} catch(IOException ioe){
			logger.error("",ioe);
			exportationEpicea.setRetour(false);
			return numFin;
		} 
	}

	public ExportationEpicea getExportationEpicea() {
		return exportationEpicea;
	}

	public void setExportationEpicea(ExportationEpicea exportationEpicea) {
		this.exportationEpicea = exportationEpicea;
	}

	public IApporteurDAO getDaoApporteur() {
		return daoApporteur;
	}

	public void setDaoApporteur(IApporteurDAO daoApporteur) {
		this.daoApporteur = daoApporteur;
	}

	
	public List<Date> selectionListeDateExportation(Date DateDeb,Date DateFin){
		List<Date> listeFinale=new LinkedList<>();
		List<String> listeString=new LinkedList<>();
		List<Date> liste;
		liste=dao.findDateExport(DateDeb,DateFin);
		for (Date date : liste) {
			if(listeString.indexOf(LibDate.dateToStringTimeStampSansMillis(date))==-1) {
				listeString.add(LibDate.dateToStringTimeStampSansMillis(date));
				listeFinale.add(date);
			}

		}
		
		liste=daoAcompte.findDateExport(DateDeb,DateFin);
		for (Date date : liste) {
			if(listeString.indexOf(LibDate.dateToStringTimeStampSansMillis(date))==-1) {
				listeString.add(LibDate.dateToStringTimeStampSansMillis(date));
				listeFinale.add(date);
			}
		}
		
		liste=daoApporteur.findDateExport(DateDeb,DateFin);
		for (Date date : liste) {
			if(listeString.indexOf(LibDate.dateToStringTimeStampSansMillis(date))==-1) {
				listeString.add(LibDate.dateToStringTimeStampSansMillis(date));
				listeFinale.add(date);
			}
		}
		
		liste=daoAvoir.findDateExport(DateDeb,DateFin);
		for (Date date : liste) {
			if(listeString.indexOf(LibDate.dateToStringTimeStampSansMillis(date))==-1) {
				listeString.add(LibDate.dateToStringTimeStampSansMillis(date));
				listeFinale.add(date);
			}
		}
		liste=daoReglement.findDateExport(DateDeb,DateFin);
		for (Date date : liste) {
			if(listeString.indexOf(LibDate.dateToStringTimeStampSansMillis(date))==-1) {
				listeString.add(LibDate.dateToStringTimeStampSansMillis(date));
				listeFinale.add(date);
			}
		}
		
		liste=daoRemise.findDateExport(DateDeb,DateFin);
		for (Date date : liste) {
			if(listeString.indexOf(LibDate.dateToStringTimeStampSansMillis(date))==-1) {
				listeString.add(LibDate.dateToStringTimeStampSansMillis(date));
				listeFinale.add(date);
			}
		}
		
		liste=daoRReglement.findDateExport(DateDeb,DateFin);
		for (Date date : liste) {
			if(listeString.indexOf(LibDate.dateToStringTimeStampSansMillis(date))==-1) {
				listeString.add(LibDate.dateToStringTimeStampSansMillis(date));
				listeFinale.add(date);
			}
		}
		Collections.sort(listeFinale, Collections.reverseOrder());
		return listeFinale;
	}
	

	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportAvoirISAGRI1(List<TaAvoir> idAvoirAExporter,List<String> listComplete,Writer fw,
			 int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesReglements) throws Exception {
		int numFin=numDepart;
		try { 
			TaAvoir taAvoir = null;
			Map<String, BigDecimal> listeTva=new LinkedHashMap<String, BigDecimal>();

			exportationEpicea.numeroDePiece = numFin;

			boolean nouvellePiece = false;

			for(int i=0; i<idAvoirAExporter.size(); i++) {
				taAvoir = idAvoirAExporter.get(i);
				if(listComplete.indexOf(taAvoir.getCodeDocument())==-1){

					/**/   // pour compenser les problèmes d'arrondis mis sur des lignes à 0
					taAvoir.calculeTvaEtTotaux();
					/**/  //isa le 03 février 2011 en accord avec Nicolas


					if(taAvoir.getDateExport()==null || reExport==1) { /* Avoir pas deja exportee ou reexportation */
						nouvellePiece = true;

						if(nouvellePiece) {
							exportationEpicea.numeroDePiece++;
							nouvellePiece = false;
							numFin++;
							exportationEpicea.numLignePiece=0;
						}

						//recuperation des informations propre à la Avoir
						exportationEpicea.codePiece = taAvoir.getCodeDocument();
						exportationEpicea.datePiece = taAvoir.getDateDocument();
//						exportationEpicea.dateEcheance = taAvoir.getDateEchDocument();
//						exportationEpicea.libelleLigne = taAvoir.getTaInfosDocument().getNomTiers();
						
						exportationEpicea.cptAuxiliaire =taAvoir.getTaInfosDocument().getCodeCompta();   
						exportationEpicea.cptGeneral = LibConversion.stringToInteger(taAvoir.getTaInfosDocument().getCompte());


						
//						exportationEpicea.journal=taAvoir.getCodeDocument().substring(0, 2);
						exportationEpicea.journal=journalAvoir;
						if(taAvoir.getTaTiers()!=null )exportationEpicea.nomTiers=taAvoir.getTaTiers().getNomTiers();
						

						BigDecimal escompte = taAvoir.getRemTtcDocument();
						if (escompte.signum() > 0) { 
							//création d'une lignes d'escompte
							exportationEpicea.numCptLigne = exportationEpicea.ctpEscompteDebit;
							exportationEpicea.libelleLigne = "Escompte "+taAvoir.getLibelleDocument();

							exportationEpicea.mtTTC=LibConversion.BigDecimalToDouble(escompte);
							exportationEpicea.mtTva=0d;
							exportationEpicea.mtHT=0d;


							exportationEpicea.numLignePiece = exportationEpicea.numLignePiece + 1;
							exportationEpicea.libelleLigne = "Escompte "+taAvoir.getLibelleDocument();
							exportationEpicea.qte1 = 0d;
							exportationEpicea.codeTva = "";
							exportationEpicea.tauxTva = 0d;
							exportationEpicea.mtTva = 0d;
							//ecriture de la ligne d'escompte dans le fichier d'export
								
							//ecriture de la ligne d'escompte
							fw.write(exportationEpicea.creationLigneISAGRI1(
									exportationEpicea.numLignePiece,
									exportationEpicea.journal ,
									exportationEpicea.datePiece,
									exportationEpicea.codePiece ,
									exportationEpicea.cptAuxiliaire ,
									exportationEpicea.nomTiers ,
									exportationEpicea.libelleLigne ,
									exportationEpicea.qte1 ,
									exportationEpicea.mtHT ,
									exportationEpicea.mtTva ,
									exportationEpicea.mtTTC ,
									exportationEpicea.numCptLigne,
									exportationEpicea.codeTva));
							fw.write(exportationEpicea.finDeLigne);

						}

						//traitement des lignes
						for (TaLAvoir lf : taAvoir.getLignes()) {
							//récupération des valeurs propres à la ligne                 
							if(lf.getTaArticle()!=null   && lf.getMtHtLApresRemiseGlobaleDocument()!=null ){
								exportationEpicea.numLignePiece = exportationEpicea.numLignePiece + 1;
								exportationEpicea.libelleLigne = lf.getLibLDocument();
								exportationEpicea.numCptLigne  = lf.getCompteLDocument();
								if(exportationEpicea.numCptLigne==null || exportationEpicea.numCptLigne.equals("")){
									exportationEpicea.setRetour(false);
									exportationEpicea.setMessageRetour("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taAvoir.getCodeDocument());
									throw new Exception("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taAvoir.getCodeDocument());
								}
								exportationEpicea.mtHT= LibConversion.BigDecimalToDouble(lf.getMtHtLApresRemiseGlobaleDocument()); 
								exportationEpicea.mtTva= LibConversion.BigDecimalToDouble(lf.getMtTtcLApresRemiseGlobaleDocument().subtract(lf.getMtHtLApresRemiseGlobaleDocument())); 
								exportationEpicea.mtTTC= LibConversion.BigDecimalToDouble(lf.getMtTtcLApresRemiseGlobaleDocument()); 
								exportationEpicea.qte1=LibConversion.BigDecimalToDouble(lf.getQteLDocument());
								exportationEpicea.codeTva =lf.getCodeTvaLDocument();

								fw.write(exportationEpicea.creationLigneISAGRI1(
										exportationEpicea.numLignePiece,
										exportationEpicea.journal ,
										exportationEpicea.datePiece,
										exportationEpicea.codePiece ,
										exportationEpicea.cptAuxiliaire ,
										exportationEpicea.nomTiers ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.qte1 ,
										exportationEpicea.mtHT ,
										exportationEpicea.mtTva ,
										exportationEpicea.mtTTC ,
										exportationEpicea.numCptLigne,
										exportationEpicea.codeTva));
								fw.write(exportationEpicea.finDeLigne);


							}
						}
							listComplete.add(taAvoir.getCodeDocument());
							exportationEpicea.listCompleteDoc.add(taAvoir);
							exportationEpicea.listeVerif.add(taAvoir);
						}
					}
				}

			} catch(IOException ioe){
				logger.error("",ioe);
				exportationEpicea.setRetour(false);
			} 

			return numFin;
		}
	
	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportAvoirCentraliseeISAGRI1(List<TaAvoir> idAvoirAExporter,List<String> listComplete,Writer fw,
			 int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesReglements) throws Exception {
		int numFin=numDepart;
		try { 
			TaAvoir taAvoir = null;
			Map<String, BigDecimal> listeTva=new LinkedHashMap<String, BigDecimal>();

			exportationEpicea.numeroDePiece = numFin;

			boolean nouvellePiece = false;

			for(int i=0; i<idAvoirAExporter.size(); i++) {
				taAvoir = idAvoirAExporter.get(i);
				if(listComplete.indexOf(taAvoir.getCodeDocument())==-1){

					/**/   // pour compenser les problèmes d'arrondis mis sur des lignes à 0
					taAvoir.calculeTvaEtTotaux();
					/**/  //isa le 03 février 2011 en accord avec Nicolas


					if(taAvoir.getDateExport()==null || reExport==1) { /* Avoir pas deja exportee ou reexportation */
						nouvellePiece = true;

						if(nouvellePiece) {
							exportationEpicea.numeroDePiece++;
							nouvellePiece = false;
							numFin++;
							exportationEpicea.numLignePiece=0;
						}
						//on crée un nouvel objet Avoir pour créer la centralisation
						TaAvoir newAvoir =new TaAvoir(false);
						newAvoir=(TaAvoir) taAvoir.cloneCentralisation();
						newAvoir.getLignes().clear();
						
						TaLAvoir newTaLAvoir;
						for (TaLAvoir obj : taAvoir.getLignes()) {
							newTaLAvoir=newAvoir.contientMemeParametreCompte(obj);
							if(newTaLAvoir!=null){
								newTaLAvoir.setQteLDocument(BigDecimal.ONE);
								if(taAvoir.getTtc()==1) {
									newTaLAvoir.setPrixULDocument(newTaLAvoir.getPrixULDocument().add(obj.getMtTtcLApresRemiseGlobaleDocument()));
								}else{
									newTaLAvoir.setPrixULDocument(newTaLAvoir.getPrixULDocument().add(obj.getMtHtLApresRemiseGlobaleDocument()));
								}
							}else{
								if(obj.getTaArticle()!=null){
								newTaLAvoir=new TaLAvoir(false);
								newTaLAvoir=obj.clone();
								newTaLAvoir.setLibLDocument(newAvoir.getLibelleDocument());
								newTaLAvoir.setQteLDocument(BigDecimal.ONE);
								newTaLAvoir.setRemTxLDocument(BigDecimal.ZERO);
								if(taAvoir.getTtc()==1) {
									newTaLAvoir.setPrixULDocument(obj.getMtTtcLApresRemiseGlobaleDocument());
								}else{
									newTaLAvoir.setPrixULDocument(obj.getMtHtLApresRemiseGlobaleDocument());
								}
								newTaLAvoir.setTaDocument(newAvoir);
								newAvoir.addLigne(newTaLAvoir);
								}
							}
						}
						newAvoir.setTxRemHtDocument(BigDecimal.ZERO);
						newAvoir.calculeTvaEtTotaux();
						
						
						//recuperation des informations propre à la Avoir
						exportationEpicea.codePiece = taAvoir.getCodeDocument();
						exportationEpicea.datePiece = taAvoir.getDateDocument();
//						exportationEpicea.dateEcheance = taAvoir.getDateEchDocument();
//						exportationEpicea.libelleLigne = taAvoir.getTaInfosDocument().getNomTiers();
						
						exportationEpicea.cptAuxiliaire =taAvoir.getTaInfosDocument().getCodeCompta();   
						exportationEpicea.cptGeneral = LibConversion.stringToInteger(taAvoir.getTaInfosDocument().getCompte());


						
//						exportationEpicea.journal=taAvoir.getCodeDocument().substring(0, 2);
						exportationEpicea.journal=journalAvoir;
						if(taAvoir.getTaTiers()!=null )exportationEpicea.nomTiers=taAvoir.getTaTiers().getNomTiers();
						

						BigDecimal escompte = taAvoir.getRemTtcDocument();
						if (escompte.signum() > 0) { 
							//création d'une lignes d'escompte
							exportationEpicea.numCptLigne = exportationEpicea.ctpEscompteDebit;
							exportationEpicea.libelleLigne = "Escompte "+taAvoir.getLibelleDocument();

							exportationEpicea.mtTTC=LibConversion.BigDecimalToDouble(escompte);
							exportationEpicea.mtTva=0d;
							exportationEpicea.mtHT=0d;


							exportationEpicea.numLignePiece = exportationEpicea.numLignePiece + 1;
							exportationEpicea.libelleLigne = "Escompte "+taAvoir.getLibelleDocument();
							exportationEpicea.qte1 = 0d;
							exportationEpicea.codeTva = "";
							exportationEpicea.tauxTva = 0d;
							exportationEpicea.mtTva = 0d;
							//ecriture de la ligne d'escompte dans le fichier d'export
								
							//ecriture de la ligne d'escompte
							fw.write(exportationEpicea.creationLigneISAGRI1(
									exportationEpicea.numLignePiece,
									exportationEpicea.journal ,
									exportationEpicea.datePiece,
									exportationEpicea.codePiece ,
									exportationEpicea.cptAuxiliaire ,
									exportationEpicea.nomTiers ,
									exportationEpicea.libelleLigne ,
									exportationEpicea.qte1 ,
									exportationEpicea.mtHT ,
									exportationEpicea.mtTva ,
									exportationEpicea.mtTTC ,
									exportationEpicea.numCptLigne,
									exportationEpicea.codeTva));
							fw.write(exportationEpicea.finDeLigne);

						}

						//traitement des lignes
						for (TaLAvoir lf : newAvoir.getLignes()) {
							//récupération des valeurs propres à la ligne                 
							if(lf.getTaArticle()!=null   && lf.getMtHtLApresRemiseGlobaleDocument()!=null ){
								exportationEpicea.numLignePiece = exportationEpicea.numLignePiece + 1;
								exportationEpicea.libelleLigne = lf.getLibLDocument();
								exportationEpicea.numCptLigne  = lf.getCompteLDocument();
								if(exportationEpicea.numCptLigne==null || exportationEpicea.numCptLigne.equals("")){
									exportationEpicea.setRetour(false);
									exportationEpicea.setMessageRetour("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taAvoir.getCodeDocument());
									throw new Exception("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taAvoir.getCodeDocument());
								}
								exportationEpicea.mtHT= LibConversion.BigDecimalToDouble(lf.getMtHtLApresRemiseGlobaleDocument()); 
								exportationEpicea.mtTva= LibConversion.BigDecimalToDouble(lf.getMtTtcLApresRemiseGlobaleDocument().subtract(lf.getMtHtLApresRemiseGlobaleDocument())); 
								exportationEpicea.mtTTC= LibConversion.BigDecimalToDouble(lf.getMtTtcLApresRemiseGlobaleDocument()); 
								exportationEpicea.qte1=LibConversion.BigDecimalToDouble(lf.getQteLDocument());
								exportationEpicea.codeTva =lf.getCodeTvaLDocument();

								fw.write(exportationEpicea.creationLigneISAGRI1(
										exportationEpicea.numLignePiece,
										exportationEpicea.journal ,
										exportationEpicea.datePiece,
										exportationEpicea.codePiece ,
										exportationEpicea.cptAuxiliaire ,
										exportationEpicea.nomTiers ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.qte1 ,
										exportationEpicea.mtHT ,
										exportationEpicea.mtTva ,
										exportationEpicea.mtTTC ,
										exportationEpicea.numCptLigne,
										exportationEpicea.codeTva));
								fw.write(exportationEpicea.finDeLigne);


							}
						}
							listComplete.add(taAvoir.getCodeDocument());
							exportationEpicea.listCompleteDoc.add(taAvoir);
							exportationEpicea.listeVerif.add(taAvoir);
						}
					}
				}

			} catch(IOException ioe){
				logger.error("",ioe);
				exportationEpicea.setRetour(false);
			} 

			return numFin;
		}


	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportApporteurISAGRI1(List<TaApporteur> idApporteurAExporter,List<String> listComplete,Writer fw,
			 int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesReglements) throws Exception {
		int numFin=numDepart;
		try { 
			TaApporteur taApporteur = null;
			Map<String, BigDecimal> listeTva=new LinkedHashMap<String, BigDecimal>();

			exportationEpicea.numeroDePiece = numFin;

			boolean nouvellePiece = false;

			for(int i=0; i<idApporteurAExporter.size(); i++) {
				taApporteur = idApporteurAExporter.get(i);
				if(listComplete.indexOf(taApporteur.getCodeDocument())==-1){

					/**/   // pour compenser les problèmes d'arrondis mis sur des lignes à 0
					taApporteur.calculeTvaEtTotaux();
					/**/  //isa le 03 février 2011 en accord avec Nicolas


					if(taApporteur.getDateExport()==null || reExport==1) { /* Apporteur pas deja exportee ou reexportation */
						nouvellePiece = true;

						if(nouvellePiece) {
							exportationEpicea.numeroDePiece++;
							nouvellePiece = false;
							numFin++;
							exportationEpicea.numLignePiece=0;
						}

						//recuperation des informations propre à la Apporteur
						exportationEpicea.codePiece = taApporteur.getCodeDocument();
						exportationEpicea.datePiece = taApporteur.getDateDocument();
//						exportationEpicea.dateEcheance = taApporteur.getDateEchDocument();
//						exportationEpicea.libelleLigne = taApporteur.getTaInfosDocument().getNomTiers();
						
						exportationEpicea.cptAuxiliaire =taApporteur.getTaInfosDocument().getCodeCompta();   
						exportationEpicea.cptGeneral = LibConversion.stringToInteger(taApporteur.getTaInfosDocument().getCompte());


						
//						exportationEpicea.journal=taApporteur.getCodeDocument().substring(0, 2);
						exportationEpicea.journal=journalApporteur;
						if(taApporteur.getTaTiers()!=null )exportationEpicea.nomTiers=taApporteur.getTaTiers().getNomTiers();
						

						BigDecimal escompte = taApporteur.getRemTtcDocument();
						if (escompte.signum() > 0) { 
							//création d'une lignes d'escompte
							exportationEpicea.numCptLigne = exportationEpicea.ctpEscompteDebit;
							exportationEpicea.libelleLigne = "Escompte "+taApporteur.getLibelleDocument();

							exportationEpicea.mtTTC=LibConversion.BigDecimalToDouble(escompte);
							exportationEpicea.mtTva=0d;
							exportationEpicea.mtHT=0d;


							exportationEpicea.numLignePiece = exportationEpicea.numLignePiece + 1;
							exportationEpicea.libelleLigne = "Escompte "+taApporteur.getLibelleDocument();
							exportationEpicea.qte1 = 0d;
							exportationEpicea.codeTva = "";
							exportationEpicea.tauxTva = 0d;
							exportationEpicea.mtTva = 0d;
							//ecriture de la ligne d'escompte dans le fichier d'export
								
							//ecriture de la ligne d'escompte
							fw.write(exportationEpicea.creationLigneISAGRI1(
									exportationEpicea.numLignePiece,
									exportationEpicea.journal ,
									exportationEpicea.datePiece,
									exportationEpicea.codePiece ,
									exportationEpicea.cptAuxiliaire ,
									exportationEpicea.nomTiers ,
									exportationEpicea.libelleLigne ,
									exportationEpicea.qte1 ,
									exportationEpicea.mtHT ,
									exportationEpicea.mtTva ,
									exportationEpicea.mtTTC ,
									exportationEpicea.numCptLigne,
									exportationEpicea.codeTva));
							fw.write(exportationEpicea.finDeLigne);

						}

						//traitement des lignes
						for (TaLApporteur lf : taApporteur.getLignes()) {
							//récupération des valeurs propres à la ligne                 
							if(lf.getTaArticle()!=null   && lf.getMtHtLApresRemiseGlobaleDocument()!=null ){
								exportationEpicea.numLignePiece = exportationEpicea.numLignePiece + 1;
								exportationEpicea.libelleLigne = lf.getLibLDocument();
								exportationEpicea.numCptLigne  = lf.getCompteLDocument();
								if(exportationEpicea.numCptLigne==null || exportationEpicea.numCptLigne.equals("")){
									exportationEpicea.setRetour(false);
									exportationEpicea.setMessageRetour("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taApporteur.getCodeDocument());
									throw new Exception("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taApporteur.getCodeDocument());
								}
								exportationEpicea.mtHT= LibConversion.BigDecimalToDouble(lf.getMtHtLApresRemiseGlobaleDocument()); 
								exportationEpicea.mtTva= LibConversion.BigDecimalToDouble(lf.getMtTtcLApresRemiseGlobaleDocument().subtract(lf.getMtHtLApresRemiseGlobaleDocument())); 
								exportationEpicea.mtTTC= LibConversion.BigDecimalToDouble(lf.getMtTtcLApresRemiseGlobaleDocument()); 
								exportationEpicea.qte1=LibConversion.BigDecimalToDouble(lf.getQteLDocument());
								exportationEpicea.codeTva =lf.getCodeTvaLDocument();

								fw.write(exportationEpicea.creationLigneISAGRI1(
										exportationEpicea.numLignePiece,
										exportationEpicea.journal ,
										exportationEpicea.datePiece,
										exportationEpicea.codePiece ,
										exportationEpicea.cptAuxiliaire ,
										exportationEpicea.nomTiers ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.qte1 ,
										exportationEpicea.mtHT ,
										exportationEpicea.mtTva ,
										exportationEpicea.mtTTC ,
										exportationEpicea.numCptLigne,
										exportationEpicea.codeTva));
								fw.write(exportationEpicea.finDeLigne);


							}
						}
							listComplete.add(taApporteur.getCodeDocument());
							exportationEpicea.listCompleteDoc.add(taApporteur);
							exportationEpicea.listeVerif.add(taApporteur);
						}
					}
				}

			} catch(IOException ioe){
				logger.error("",ioe);
				exportationEpicea.setRetour(false);
			} 

			return numFin;
		}
	
	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportApporteurCentraliseeISAGRI1(List<TaApporteur> idApporteurAExporter,List<String> listComplete,Writer fw,
			 int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesReglements) throws Exception {
		int numFin=numDepart;
		try { 
			TaApporteur taApporteur = null;
			Map<String, BigDecimal> listeTva=new LinkedHashMap<String, BigDecimal>();


			exportationEpicea.numeroDePiece = numFin;

			boolean nouvellePiece = false;

			for(int i=0; i<idApporteurAExporter.size(); i++) {
				taApporteur = idApporteurAExporter.get(i);
				if(listComplete.indexOf(taApporteur.getCodeDocument())==-1){

					/**/   // pour compenser les problèmes d'arrondis mis sur des lignes à 0
					taApporteur.calculeTvaEtTotaux();
					/**/  //isa le 03 février 2011 en accord avec Nicolas


					if(taApporteur.getDateExport()==null || reExport==1) { /* Apporteur pas deja exportee ou reexportation */
						nouvellePiece = true;

						if(nouvellePiece) {
							exportationEpicea.numeroDePiece++;
							nouvellePiece = false;
							numFin++;
							exportationEpicea.numLignePiece=0;
						}
						//on crée un nouvel objet Apporteur pour créer la centralisation
						TaApporteur newApporteur =new TaApporteur(false);
						newApporteur=(TaApporteur) taApporteur.cloneCentralisation();
						newApporteur.getLignes().clear();
						
						TaLApporteur newTaLApporteur;
						for (TaLApporteur obj : taApporteur.getLignes()) {
							newTaLApporteur=newApporteur.contientMemeParametreCompte(obj);
							if(newTaLApporteur!=null){
								newTaLApporteur.setQteLDocument(BigDecimal.ONE);
								if(taApporteur.getTtc()==1) {
									newTaLApporteur.setPrixULDocument(newTaLApporteur.getPrixULDocument().add(obj.getMtTtcLApresRemiseGlobaleDocument()));
								}else{
									newTaLApporteur.setPrixULDocument(newTaLApporteur.getPrixULDocument().add(obj.getMtHtLApresRemiseGlobaleDocument()));
								}
							}else{
								if(obj.getTaArticle()!=null){
								newTaLApporteur=new TaLApporteur(false);
								newTaLApporteur=obj.clone();
								newTaLApporteur.setLibLDocument(newApporteur.getLibelleDocument());
								newTaLApporteur.setQteLDocument(BigDecimal.ONE);
								newTaLApporteur.setRemTxLDocument(BigDecimal.ZERO);
								if(taApporteur.getTtc()==1) {
									newTaLApporteur.setPrixULDocument(obj.getMtTtcLApresRemiseGlobaleDocument());
								}else{
									newTaLApporteur.setPrixULDocument(obj.getMtHtLApresRemiseGlobaleDocument());
								}
								newTaLApporteur.setTaDocument(newApporteur);
								newApporteur.addLigne(newTaLApporteur);
								}
							}
						}
						newApporteur.setTxRemHtDocument(BigDecimal.ZERO);
						newApporteur.calculeTvaEtTotaux();
						
						
						//recuperation des informations propre à la Apporteur
						exportationEpicea.codePiece = taApporteur.getCodeDocument();
						exportationEpicea.datePiece = taApporteur.getDateDocument();
//						exportationEpicea.dateEcheance = taApporteur.getDateEchDocument();
//						exportationEpicea.libelleLigne = taApporteur.getTaInfosDocument().getNomTiers();
						
						exportationEpicea.cptAuxiliaire =taApporteur.getTaInfosDocument().getCodeCompta();   
						exportationEpicea.cptGeneral = LibConversion.stringToInteger(taApporteur.getTaInfosDocument().getCompte());


						
//						exportationEpicea.journal=taApporteur.getCodeDocument().substring(0, 2);
						exportationEpicea.journal=journalApporteur;
						if(taApporteur.getTaTiers()!=null )exportationEpicea.nomTiers=taApporteur.getTaTiers().getNomTiers();
						

						BigDecimal escompte = taApporteur.getRemTtcDocument();
						if (escompte.signum() > 0) { 
							//création d'une lignes d'escompte
							exportationEpicea.numCptLigne = exportationEpicea.ctpEscompteDebit;
							exportationEpicea.libelleLigne = "Escompte "+taApporteur.getLibelleDocument();

							exportationEpicea.mtTTC=LibConversion.BigDecimalToDouble(escompte);
							exportationEpicea.mtTva=0d;
							exportationEpicea.mtHT=0d;


							exportationEpicea.numLignePiece = exportationEpicea.numLignePiece + 1;
							exportationEpicea.libelleLigne = "Escompte "+taApporteur.getLibelleDocument();
							exportationEpicea.qte1 = 0d;
							exportationEpicea.codeTva = "";
							exportationEpicea.tauxTva = 0d;
							exportationEpicea.mtTva = 0d;
							//ecriture de la ligne d'escompte dans le fichier d'export
								
							//ecriture de la ligne d'escompte
							fw.write(exportationEpicea.creationLigneISAGRI1(
									exportationEpicea.numLignePiece,
									exportationEpicea.journal ,
									exportationEpicea.datePiece,
									exportationEpicea.codePiece ,
									exportationEpicea.cptAuxiliaire ,
									exportationEpicea.nomTiers ,
									exportationEpicea.libelleLigne ,
									exportationEpicea.qte1 ,
									exportationEpicea.mtHT ,
									exportationEpicea.mtTva ,
									exportationEpicea.mtTTC ,
									exportationEpicea.numCptLigne,
									exportationEpicea.codeTva));
							fw.write(exportationEpicea.finDeLigne);

						}

						//traitement des lignes
						for (TaLApporteur lf : newApporteur.getLignes()) {
							//récupération des valeurs propres à la ligne                 
							if(lf.getTaArticle()!=null   && lf.getMtHtLApresRemiseGlobaleDocument()!=null ){
								exportationEpicea.numLignePiece = exportationEpicea.numLignePiece + 1;
								exportationEpicea.libelleLigne = lf.getLibLDocument();
								exportationEpicea.numCptLigne  = lf.getCompteLDocument();
								if(exportationEpicea.numCptLigne==null || exportationEpicea.numCptLigne.equals("")){
									exportationEpicea.setRetour(false);
									exportationEpicea.setMessageRetour("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taApporteur.getCodeDocument());
									throw new Exception("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taApporteur.getCodeDocument());
								}
								exportationEpicea.mtHT= LibConversion.BigDecimalToDouble(lf.getMtHtLApresRemiseGlobaleDocument()); 
								exportationEpicea.mtTva= LibConversion.BigDecimalToDouble(lf.getMtTtcLApresRemiseGlobaleDocument().subtract(lf.getMtHtLApresRemiseGlobaleDocument())); 
								exportationEpicea.mtTTC= LibConversion.BigDecimalToDouble(lf.getMtTtcLApresRemiseGlobaleDocument()); 
								exportationEpicea.qte1=LibConversion.BigDecimalToDouble(lf.getQteLDocument());
								exportationEpicea.codeTva =lf.getCodeTvaLDocument();

								fw.write(exportationEpicea.creationLigneISAGRI1(
										exportationEpicea.numLignePiece,
										exportationEpicea.journal ,
										exportationEpicea.datePiece,
										exportationEpicea.codePiece ,
										exportationEpicea.cptAuxiliaire ,
										exportationEpicea.nomTiers ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.qte1 ,
										exportationEpicea.mtHT ,
										exportationEpicea.mtTva ,
										exportationEpicea.mtTTC ,
										exportationEpicea.numCptLigne,
										exportationEpicea.codeTva));
								fw.write(exportationEpicea.finDeLigne);


							}
						}
							listComplete.add(taApporteur.getCodeDocument());
							exportationEpicea.listCompleteDoc.add(taApporteur);
							exportationEpicea.listeVerif.add(taApporteur);
						}
					}
				}

			} catch(IOException ioe){
				logger.error("",ioe);
				exportationEpicea.setRetour(false);
			} 

			return numFin;
		}


	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportReglementISAGRI1(List<TaReglement> idReglementAExporter,List<String> listComplete,Writer fw,
			int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesDocumenstLies,TaFacture taFactureDirect) throws Exception {
		int numFin=numDepart;
		try {     


			Boolean gererLesPointages = gererPointages;

			TaReglement taDoc = null;

			exportationEpicea.numeroDePiece = numFin;

		

			boolean nouvellePiece = false;


			for(int i=0; i<idReglementAExporter.size(); i++) {
				taDoc = idReglementAExporter.get(i);
				if(listComplete.indexOf(taDoc.getCodeDocument())==-1 ){

					if(taDoc.getDateExport()==null || reExport==1) { /* facture pas deja exportee ou reexportation */
							if( daoRemise.findSiReglementDansRemise(taDoc.getCodeDocument()).size()==0){
							nouvellePiece = true;

							if(nouvellePiece) {
								exportationEpicea.numeroDePiece++;
								nouvellePiece = false;
								numFin++;
							}
							

							exportationEpicea.journal=journalReglement;
							if(taDoc.getTaTiers()!=null )exportationEpicea.nomTiers=taDoc.getTaTiers().getNomTiers();
							exportationEpicea.cptAuxiliaire =taDoc.getTaTiers().getCodeCompta();  
							
							//recuperation des informations propre a la facture
							exportationEpicea.codePiece = taDoc.getCodeDocument();
							exportationEpicea.datePiece = taDoc.getDateDocument();
							if(taDoc.getLibelleDocument()!=null)
								exportationEpicea.libelleLigne = taDoc.getLibelleDocument().replace("\r","").replace("\n","");
							else
								exportationEpicea.libelleLigne = "";
							if(taDoc.getTaCompteBanque()!=null)
								exportationEpicea.numCptLigne = taDoc.getTaCompteBanque().getCptcomptable();
							if(taDoc.getTaTPaiement()!=null)
								exportationEpicea.numCptLigne = taDoc.getTaTPaiement().getCompte();
							
							//trouver le tiers lié au règlement
							TaTiers tiers=taDoc.getTaTiers();
 

							exportationEpicea.numLignePiece = 1;



							exportationEpicea.mtHT= 0d; 
							exportationEpicea.mtTva= 0d; 
							exportationEpicea.mtTTC= LibConversion.BigDecimalToDouble(taDoc.getNetTtcCalc()); 
							exportationEpicea.qte1=0d;
							exportationEpicea.codeTva ="";

							fw.write(exportationEpicea.creationLigneISAGRI1(
									exportationEpicea.numLignePiece,
									exportationEpicea.journal ,
									exportationEpicea.datePiece,
									exportationEpicea.codePiece ,
									exportationEpicea.cptAuxiliaire ,
									exportationEpicea.nomTiers ,
									exportationEpicea.libelleLigne ,
									exportationEpicea.qte1 ,
									exportationEpicea.mtHT ,
									exportationEpicea.mtTva ,
									exportationEpicea.mtTTC ,
									exportationEpicea.numCptLigne,
									exportationEpicea.codeTva));
							fw.write(exportationEpicea.finDeLigne);

//							if(gererLesPointages){
//								exportationEpicea.typePiece = exportationEpicea.typePointage;	
//								//traitement des lignes pour les pointages
//								for (TaRReglement lf : taDoc.getTaRReglements()) {
//									//récupération des valeurs propres à la ligne
//									TaFacture fac =lf.getTaFacture();
//									numFin=initPointage(fac, taDoc, lf, gererLesDocumenstLies, idAppelant, numFin);
//								} 
//								/**** Attention !!! rajouter temporairement pour les règlements unique dans les factures*/
//								if(taDoc.getTaFactures()!=null && !taDoc.getTaFactures().isEmpty()) {
//									for (TaFacture obj : taDoc.getTaFactures()) {
//										//récupération des valeurs propres à la ligne
//										IRelationDoc r=new TaRReglement();
//										r.setAffectation(obj.getNetTtcCalc());
//										numFin=initPointage(obj, taDoc, r, gererLesDocumenstLies, idAppelant, numFin);
//									}
//								} 
//								if(taFactureDirect!=null) {
//									//récupération des valeurs propres à la ligne
//									IRelationDoc r=new TaRReglement();
//									r.setAffectation(taFactureDirect.getNetTtcCalc());
//									numFin=initPointage(taFactureDirect, taDoc, r, gererLesDocumenstLies, idAppelant, numFin);
//								} 								
//							}
//							if(taDoc.getExport()!=1)taDoc.setExport(1);
//							taDoc=daoReglement.merge(taDoc);
							listComplete.add(taDoc.getCodeDocument());
							exportationEpicea.listCompleteDoc.add(taDoc);
							exportationEpicea.listeVerif.add(taDoc);
						}
					}
				}
			}

		} catch(IOException ioe){
			logger.error("",ioe);
			exportationEpicea.setRetour(false);
		} 

		return numFin;
	}

	
	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportRemiseISAGRI1(List<TaRemise> idRemiseAExporter,List<String> listComplete,Writer fw,
			int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesDocumenstLies) throws Exception {
		int numFin=numDepart;
		try {      

			Boolean gererLesPointages = gererPointages;

//			TaRemiseDAO taDocDAO = new TaRemiseDAO(em);
			TaRemise taDoc = null;


			exportationEpicea.numeroDePiece = numFin;

			

			boolean nouvellePiece = false;

//			EntityTransaction transaction = taDocDAO.getEntityManager().getTransaction();
//			taDocDAO.begin(transaction);

			for(int i=0; i<idRemiseAExporter.size(); i++) {
				taDoc = idRemiseAExporter.get(i);

				if(listComplete.indexOf(taDoc.getCodeDocument())==-1){

					if(taDoc.getDateExport()==null || reExport==1) { /* remise pas deja exportee ou reexportation */
						nouvellePiece = true;

						if(nouvellePiece) {
							exportationEpicea.numeroDePiece++;
							nouvellePiece = false;
							numFin++;
						}

						//recuperation des informations propre a la REMISE
						exportationEpicea.codePiece = taDoc.getCodeDocument();
						exportationEpicea.datePiece = taDoc.getDateDocument();
						exportationEpicea.journal = journalRemise;
						
						if(taDoc.getTaCompteBanque()!=null)
							exportationEpicea.numCptLigne = taDoc.getTaCompteBanque().getCptcomptable();
						else
						if(taDoc.getTaTPaiement()!=null)
							exportationEpicea.numCptLigne = taDoc.getTaTPaiement().getCompte();//taDoc.getTaCompteBanque().getCptcomptable();

						exportationEpicea.mtCreditLigne = 0d;  

						exportationEpicea.numLignePiece = 1;
						
						exportationEpicea.typePiece = exportationEpicea.TypeReglement;

						//traitement pour exporter les relations avec les factures					
						//traitement des lignes
						for (TaLRemise lf : taDoc.getTaLRemises()) {
							//récupération des valeurs propres à la ligne
							if(lf.getTaReglement()!=null && listComplete.indexOf(lf.getTaReglement().getCodeDocument())==-1){
								//						if(lf.getTaReglement()!=null){//on traite le réglement
								//on traite la ligne de contrepartie (tiers)
								exportationEpicea.typePiece = exportationEpicea.TypeReglement;
								exportationEpicea.codePiece=taDoc.getCodeDocument();
								exportationEpicea.datePiece=taDoc.getDateDocument();
								if(lf.getTaReglement().getLibelleDocument()!=null)
									exportationEpicea.libelleLigne = lf.getTaReglement().getLibelleDocument().replace("\r","").replace("\n","");
								else
									exportationEpicea.libelleLigne = "";

								exportationEpicea.cptAuxiliaire = "+"+lf.getTaReglement().getTaTiers().getCodeTiers();
								exportationEpicea.nomTiers = lf.getTaReglement().getTaTiers().getNomTiers();
			

								exportationEpicea.numLignePiece = exportationEpicea.numLignePiece + 1;

								exportationEpicea.mtHT= 0d; 
								exportationEpicea.mtTva= 0d; 
								exportationEpicea.mtTTC= LibConversion.BigDecimalToDouble(lf.getTaReglement().getNetTtcCalc()); 
								exportationEpicea.qte1=0d;
								exportationEpicea.codeTva ="";

								fw.write(exportationEpicea.creationLigneISAGRI1(
										exportationEpicea.numLignePiece,
										exportationEpicea.journal ,
										exportationEpicea.datePiece,
										exportationEpicea.codePiece ,
										exportationEpicea.cptAuxiliaire ,
										exportationEpicea.nomTiers ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.qte1 ,
										exportationEpicea.mtHT ,
										exportationEpicea.mtTva ,
										exportationEpicea.mtTTC ,
										exportationEpicea.numCptLigne,
										exportationEpicea.codeTva));
								fw.write(exportationEpicea.finDeLigne);

//								//traitment des pointages si on les gere
//								if(gererLesPointages){
//									exportationEpicea.typePiece = exportationEpicea.typePointage;	
//									for (TaRReglement Relation : lf.getTaReglement().getTaRReglements()) {
//										TaFacture fac =Relation.getTaFacture();
//										idAppelant=fac.getIdDocument();
//										numFin=initPointage(fac, lf.getTaReglement(), Relation, gererLesDocumenstLies, idAppelant, numFin);
//									}
//								}//fin si on gere les pointages
//								if(lf.getTaReglement().getExport()!=1)
//									lf.getTaReglement().setExport(1);
								exportationEpicea.listCompleteDoc.add(lf.getTaReglement());
								listComplete.add(lf.getTaReglement().getCodeDocument());
								exportationEpicea.listeVerif.add(lf.getTaReglement());
							}//fin on traite le reglement

//							if(lf.getTaAcompte()!=null && listComplete.indexOf(lf.getTaAcompte().getCodeDocument())==-1){
//								//						if(lf.getTaAcompte()!=null){//on traite l'acompte
//								//on traite la ligne de contrepartie (tiers)
//								exportationEpicea.typePiece = exportationEpicea.TypeReglement;
//								exportationEpicea.codePiece=taDoc.getCodeDocument();
//								exportationEpicea.datePiece=taDoc.getDateDocument();
//								exportationEpicea.libelleLigne = lf.getTaAcompte().getLibelleDocument();//libelle de la ligne de facture
//								exportationEpicea.qte1 = 0d;
//								exportationEpicea.qte2 = 0d;
//								exportationEpicea.numCptLigne = "+"+lf.getTaAcompte().getTaTiers().getCodeTiers();
//								exportationEpicea.cptCollectif = lf.getTaAcompte().getTaTiers().getCompte();
//								exportationEpicea.nomTiers = "";
//								exportationEpicea.adresse1Tiers = "";
//								exportationEpicea.adresse2Tiers = "";
//								exportationEpicea.codePostalTiers = "";
//								exportationEpicea.villeTiers = "";
//
//								exportationEpicea.codeTva = "";
//								exportationEpicea.exigibiliteTva = "";
//								exportationEpicea.mtDebitTva = 0d;
//								exportationEpicea.mtDebitLigne = 0d;
//
//								exportationEpicea.numLignePiece = exportationEpicea.numLignePiece + 1;
//								exportationEpicea.mtCreditLigne = LibConversion.BigDecimalToDouble(lf.getTaAcompte().getNetTtcCalc());
//								exportationEpicea.mtCreditTva = 0d;
//
//								exportationEpicea.refContrepartie = "";
//								exportationEpicea.dateContrepartie = null;
//								exportationEpicea.mtContrepartieDebit = 0d;
//								exportationEpicea.mtContrepartieCredit = 0d;
//								exportationEpicea.mtAffectation = 0d;
//
//								fw.write(exportationEpicea.creationLigne(exportationEpicea.numeroDePiece,
//										exportationEpicea.numLignePiece ,
//										exportationEpicea.typePiece ,
//										exportationEpicea.codePiece ,
//										exportationEpicea.datePiece ,
//										exportationEpicea.numCptLigne ,
//										exportationEpicea.libelleLigne ,
//										exportationEpicea.mtDebitLigne,
//										exportationEpicea.mtCreditLigne ,
//										exportationEpicea.qte1 ,
//										exportationEpicea.qte2 ,
//										exportationEpicea.codeTva ,
//										exportationEpicea.tauxTva ,
//										exportationEpicea.mtDebitTva,
//										exportationEpicea.mtCreditTva ,
//										exportationEpicea.dateEcheance ,
//										exportationEpicea.cptCollectif ,
//										exportationEpicea.nomTiers ,
//										exportationEpicea.adresse1Tiers ,
//										exportationEpicea.adresse2Tiers ,
//										exportationEpicea.codePostalTiers ,
//										exportationEpicea.villeTiers ,
//										exportationEpicea.exigibiliteTva ,
//										exportationEpicea.dateLivraisonLigne,
//										exportationEpicea.refContrepartie,
//										exportationEpicea.dateContrepartie,
//										exportationEpicea.mtContrepartieDebit,
//										exportationEpicea.mtContrepartieCredit,
//										exportationEpicea.mtAffectation));
//								fw.write(exportationEpicea.finDeLigne);
//
//								//traitment des pointages si on les gere
//								if(gererLesPointages){
//									exportationEpicea.typePiece = exportationEpicea.typePointage;
//									for (TaRAcompte Relation : lf.getTaAcompte().getTaRAcomptes()) {
//										TaFacture fac =Relation.getTaFacture();
//										numFin=initPointage(fac, lf.getTaAcompte(), Relation, gererLesDocumenstLies, idAppelant, numFin);
//									}		
//								}//fin traitement des pointages
////								if(lf.getTaAcompte().getExport()!=1)
////									lf.getTaAcompte().setExport(1);
//								exportationEpicea.listCompleteDoc.add(lf.getTaAcompte());
//								listComplete.add(lf.getTaAcompte().getCodeDocument());
//								exportationEpicea.listeVerif.add(lf.getTaAcompte());
//							}//fin on traite l'acompte						
							if(lf.getDateExport()==null)lf.setDateExport(new Date());
							
						} 
//						if(taDoc.getExport()!=1)
//							taDoc.setExport(1);
//						daoRemise.merge(taDoc);
						exportationEpicea.listCompleteDoc.add(taDoc);
						listComplete.add(taDoc.getCodeDocument());
						exportationEpicea.listeVerif.add(taDoc);
					}
				}
			}
		} catch(IOException ioe){
			logger.error("",ioe);
			exportationEpicea.setRetour(false);
		} 

		return numFin;

	}

	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportFactureISAGRI2(List<TaFacture> idFactureAExporter,List<String> listComplete,Writer fw,
			 int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesReglements) throws Exception {
		int numFin=numDepart;
		try { 
			TaFacture taFacture = null;

			exportationEpicea.numeroDePiece = numFin;

			boolean nouvellePiece = false;

			for(int i=0; i<idFactureAExporter.size(); i++) {
				taFacture = idFactureAExporter.get(i);
				if(listComplete.indexOf(taFacture.getCodeDocument())==-1){

					/**/   // pour compenser les problèmes d'arrondis mis sur des lignes à 0
					taFacture.calculeTvaEtTotaux();
					/**/  //isa le 03 février 2011 en accord avec Nicolas


					if(taFacture.getDateExport()==null || reExport==1) { /* facture pas deja exportee ou reexportation */
						nouvellePiece = true;

						if(nouvellePiece) {
							exportationEpicea.numeroDePiece++;
							nouvellePiece = false;
							numFin++;
						}

						//recuperation des informations propre a la facture
						exportationEpicea.codePiece = taFacture.getCodeDocument();
						exportationEpicea.datePiece = taFacture.getDateDocument();
						exportationEpicea.journal = journalFacture;
						exportationEpicea.libellePiece = taFacture.getLibelleDocument();
						exportationEpicea.libelleLigne = taFacture.getTaInfosDocument().getNomTiers();
						exportationEpicea.nomTiers = taFacture.getTaInfosDocument().getNomTiers();

						exportationEpicea.mtDebitLigne = LibConversion.BigDecimalToDouble(taFacture.getNetTtcCalc());  

						exportationEpicea.numCptLigne = "+"+taFacture.getTaInfosDocument().getCodeCompta();    
						exportationEpicea.numLignePiece = 1;
						exportationEpicea.mtCreditLigne = 0d;
						exportationEpicea.qte1 = 0d;
						exportationEpicea.codeTva ="";
						
						exportationEpicea.refContrepartie="";
//						if(gererPointages){
//							if(taFacture.getTaRReglements()!=null && taFacture.getTaRReglements().size()>0)
//								exportationEpicea.refContrepartie=exportationEpicea.codePiece;
//						}
							
						//ecriture de la ligne d'entete dans le fichier d'export
						fw.write(exportationEpicea.creationLigneISAGRI2(
								exportationEpicea.journal,
								exportationEpicea.datePiece ,
								exportationEpicea.numCptLigne ,
								exportationEpicea.libelleLigne ,
								exportationEpicea.codePiece ,
								exportationEpicea.libellePiece ,
								exportationEpicea.qte1,
								exportationEpicea.mtDebitLigne ,
								exportationEpicea.mtCreditLigne ,
								exportationEpicea.refContrepartie ,
								exportationEpicea.codeTva ));
						fw.write(exportationEpicea.finDeLigne);
						

						BigDecimal escompte = taFacture.getRemTtcDocument();
						if (escompte.signum() > 0) { 
							//création d'une lignes d'escompte
							exportationEpicea.numCptLigne = exportationEpicea.ctpEscompteDebit;
							exportationEpicea.libelleLigne = "Escompte "+taFacture.getLibelleDocument();
							exportationEpicea.mtDebitLigne = LibConversion.BigDecimalToDouble(escompte);
							exportationEpicea.mtCreditLigne = 0d;
							exportationEpicea.qte1 = 0d;
							exportationEpicea.codeTva = "";

							//ecriture de la ligne d'escompte dans le fichier d'export


							fw.write(exportationEpicea.creationLigneISAGRI2(
									exportationEpicea.journal,
									exportationEpicea.datePiece ,
									exportationEpicea.numCptLigne ,
									exportationEpicea.libelleLigne ,
									exportationEpicea.codePiece ,
									exportationEpicea.libellePiece ,
									exportationEpicea.qte1,
									exportationEpicea.mtDebitLigne ,
									exportationEpicea.mtCreditLigne ,
									exportationEpicea.refContrepartie ,
									exportationEpicea.codeTva ));
							fw.write(exportationEpicea.finDeLigne);
						}

						//traitement des lignes
						for (TaLFacture lf : taFacture.getLignes()) {
							//récupération des valeurs propres à la ligne                 
							if(lf.getTaArticle()!=null   && lf.getMtTtcLApresRemiseGlobaleDocument()!=null ){
								exportationEpicea.libelleLigne = lf.getLibLDocument();
								exportationEpicea.qte1 = LibConversion.BigDecimalToDouble(lf.getQteLDocument());
								exportationEpicea.numCptLigne = lf.getCompteLDocument();
								if(exportationEpicea.numCptLigne==null || exportationEpicea.numCptLigne.equals("")){
									exportationEpicea.setRetour(false);
									exportationEpicea.setMessageRetour("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taFacture.getCodeDocument());
									throw new Exception("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taFacture.getCodeDocument());
								}
								
								//traitement du type de tva du tiers (F ou ue ou horsUE ou Franchise)

								exportationEpicea.codeTva=lf.getCodeTvaLDocument();
									
								if(exportationEpicea.codeTva==null)exportationEpicea.codeTva="";

//								exportationEpicea.tauxTva = LibConversion.BigDecimalToDouble(lf.getTauxTvaLDocument());
								exportationEpicea.mtCreditLigne = LibConversion.BigDecimalToDouble(lf.getMtTtcLApresRemiseGlobaleDocument());

								exportationEpicea.mtDebitLigne = 0d;


								fw.write(exportationEpicea.creationLigneISAGRI2(
										exportationEpicea.journal,
										exportationEpicea.datePiece ,
										exportationEpicea.numCptLigne ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.codePiece ,
										exportationEpicea.libellePiece ,
										exportationEpicea.qte1,
										exportationEpicea.mtDebitLigne ,
										exportationEpicea.mtCreditLigne ,
										exportationEpicea.refContrepartie ,
										exportationEpicea.codeTva ));
								fw.write(exportationEpicea.finDeLigne);
							}
						}

						if(gererLesReglements){
								boolean priseEnChargeAcomptes=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.ACOMPTES);
								boolean priseEnChargeReglementSimple=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.REGLEMENT_SIMPLE);
								boolean priseEnChargeRemise=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.REMISE);

							List<TaRemise> listeRemise = new LinkedList<TaRemise>();
							//récupérer tous les documents directement liés à cette facture
							for (TaRReglement rReglement : taFacture.getTaRReglements()) {
								List<TaReglement> liste=new LinkedList<TaReglement>();
								List<TaRemise> listeRemiseReglement =new LinkedList<TaRemise>();
								listeRemiseReglement=daoRemise.findSiReglementDansRemise(rReglement.getTaReglement().getCodeDocument());


								if(listeRemiseReglement.size()==0 && priseEnChargeReglementSimple){
									liste.add(rReglement.getTaReglement());
									numFin=exportReglementISAGRI2(liste, listComplete, fw,  exportationEpicea.getReExportReglement(), numFin, gererPointages,
											taFacture.getIdDocument(),false,null);
								}else{
									for (TaRemise taRemise : listeRemiseReglement) {
										if(listeRemise.indexOf(taRemise)==-1 )
											listeRemise.add(taRemise);
									}
								}

							}
							/**** Atention Cette partie est rajoutée temporaire jusqu'à gestion des règlements multiples*/
							if (taFacture.getTaReglement()!=null) {
								List<TaReglement> liste=new LinkedList<TaReglement>();
								List<TaRemise> listeRemiseReglement =new LinkedList<TaRemise>();
								listeRemiseReglement=daoRemise.findSiReglementDansRemise(taFacture.getTaReglement().getCodeDocument());


								if(listeRemiseReglement.size()==0 && priseEnChargeReglementSimple){
									liste.add(taFacture.getTaReglement());
									numFin=exportReglementISAGRI2(liste, listComplete, fw,  exportationEpicea.getReExportReglement(), numFin, gererPointages,
											taFacture.getIdDocument(),false,taFacture);
								}else{
									for (TaRemise taRemise : listeRemiseReglement) {
										if(listeRemise.indexOf(taRemise)==-1 )
											listeRemise.add(taRemise);
									}
								}

							}							
							
							if(priseEnChargeRemise){
								if(listeRemise.size()>0){
									numFin=exportRemiseISAGRI2(listeRemise, listComplete, fw,  exportationEpicea.getReExportRemise(), numFin, gererPointages, 
											taFacture.getIdDocument(),false);
								}
							}
							}
//						else if(gererPointages){
////								for (TaRAcompte r : taFacture.getTaRAcomptes()) {
////									numFin=initPointage(taFacture, r.getTaAcompte(), r, false, idAppelant, numFin);
////								}
//								for (TaRAvoir r : taFacture.getTaRAvoirs()) {
//									numFin=initPointage(taFacture, r.getTaAvoir(), r, false, idAppelant, numFin);
//								}
//								for (TaRReglement r : taFacture.getTaRReglements()) {
//									numFin=initPointage(taFacture, r.getTaReglement(), r, false, idAppelant, numFin);
//								}
//								if(taFacture.getTaReglement()!=null) {
//									IRelationDoc r=new TaRReglement();
//									r.setAffectation(taFacture.getTaReglement().getNetTtcCalc());
//									numFin=initPointage(taFacture, taFacture.getTaReglement(), r, false, idAppelant, numFin);
//								}							
//							}
								
							listComplete.add(taFacture.getCodeDocument());
							exportationEpicea.listCompleteDoc.add(taFacture);
							exportationEpicea.listeVerif.add(taFacture);
						}
					}
				}

			} catch(IOException ioe){
				logger.error("",ioe);
				exportationEpicea.setRetour(false);
			} 

			return numFin;
		}	

	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportAvoirISAGRI2(List<TaAvoir> idFactureAExporter,List<String> listComplete,Writer fw,
			int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesDocumenstLies) throws Exception {
		int numFin=numDepart;
		try {      


			Boolean gererLesPointages = gererPointages;

			TaAvoir taAvoir = null;


			exportationEpicea.numeroDePiece = numFin;



			boolean nouvellePiece = false;

			for(int i=0; i<idFactureAExporter.size(); i++) {
				taAvoir = idFactureAExporter.get(i);
				if(listComplete.indexOf(taAvoir.getCodeDocument())==-1){

					/**/   // pour compenser les problèmes d'arrondis mis sur des lignes à 0
					taAvoir.calculeTvaEtTotaux();
					/**/  //isa le 03 février 2011 en accord avec Nicolas

					if(taAvoir.getDateExport()==null || reExport==1) { /* facture pas deja exportee ou reexportation */
						nouvellePiece = true;

						if(nouvellePiece) {
							exportationEpicea.numeroDePiece++;
							nouvellePiece = false;
							numFin++;
						}

						//recuperation des informations propre a la facture
						exportationEpicea.codePiece = taAvoir.getCodeDocument();
						exportationEpicea.datePiece = taAvoir.getDateDocument();
						exportationEpicea.journal = journalAvoir;
						exportationEpicea.libellePiece = taAvoir.getLibelleDocument();
						exportationEpicea.libelleLigne = taAvoir.getTaInfosDocument().getNomTiers();
						exportationEpicea.nomTiers = taAvoir.getTaInfosDocument().getNomTiers();

						exportationEpicea.mtCreditLigne = LibConversion.BigDecimalToDouble(taAvoir.getNetTtcCalc());
						exportationEpicea.numCptLigne = "+"+taAvoir.getTaInfosDocument().getCodeCompta(); 
						
						exportationEpicea.numLignePiece = 1;

						exportationEpicea.mtDebitLigne = 0d;
						exportationEpicea.qte1 = 0d;
						exportationEpicea.codeTva ="";
						exportationEpicea.refContrepartie ="";
						
//						if(gererLesPointages){
//							//traitement des lignes pour les pointages
//							for (TaRAvoir lf : taAvoir.getTaRAvoirs()) {
//								//récupération des valeurs propres à la ligne
//								exportationEpicea.refContrepartie = taAvoir.getCodeDocument();
//							} 
//						}


						//ecriture de la ligne d'entete dans le fichier d'export
						fw.write(exportationEpicea.creationLigneISAGRI2(
								exportationEpicea.journal,
								exportationEpicea.datePiece ,
								exportationEpicea.numCptLigne ,
								exportationEpicea.libelleLigne ,
								exportationEpicea.codePiece ,
								exportationEpicea.libellePiece ,
								exportationEpicea.qte1,
								exportationEpicea.mtDebitLigne ,
								exportationEpicea.mtCreditLigne ,
								exportationEpicea.refContrepartie ,
								exportationEpicea.codeTva ));
						fw.write(exportationEpicea.finDeLigne);



						//traitement des lignes
						for (TaLAvoir lf : taAvoir.getLignes()) {
							//récupération des valeurs propres à la ligne

							
							if(lf.getTaArticle()!=null && lf.getMtTtcLApresRemiseGlobaleDocument().signum()!=0){
								exportationEpicea.libelleLigne = lf.getLibLDocument();
								exportationEpicea.qte1 = LibConversion.BigDecimalToDouble(lf.getQteLDocument());
								exportationEpicea.numCptLigne = lf.getCompteLDocument();
								if(exportationEpicea.numCptLigne==null || exportationEpicea.numCptLigne.equals("")){
									//transaction.rollback();
									exportationEpicea.setRetour(false);
									exportationEpicea.setMessageRetour("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taAvoir.getCodeDocument());
									throw new Exception("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taAvoir.getCodeDocument());
								}
								//traitement du type de tva du tiers (F ou ue ou horsUE ou Franchise)
								exportationEpicea.codeTva=lf.getCodeTvaLDocument();
								if(exportationEpicea.codeTva==null)exportationEpicea.codeTva=""; /* corrigé par isa le 11/09/2014*/

								
								exportationEpicea.mtDebitLigne = LibConversion.BigDecimalToDouble(lf.getMtTtcLApresRemiseGlobaleDocument());

								exportationEpicea.numLignePiece = exportationEpicea.numLignePiece + 1;
								exportationEpicea.mtCreditLigne = 0d;
								//ecriture de la ligne dans le fichier d'export


								fw.write(exportationEpicea.creationLigneISAGRI2(
										exportationEpicea.journal,
										exportationEpicea.datePiece ,
										exportationEpicea.numCptLigne ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.codePiece ,
										exportationEpicea.libellePiece ,
										exportationEpicea.qte1,
										exportationEpicea.mtDebitLigne ,
										exportationEpicea.mtCreditLigne ,
										exportationEpicea.refContrepartie ,
										exportationEpicea.codeTva ));
								fw.write(exportationEpicea.finDeLigne);
							}
						} 

						exportationEpicea.listCompleteDoc.add(taAvoir);
						listComplete.add(taAvoir.getCodeDocument());
						exportationEpicea.listeVerif.add(taAvoir);
					}
				}
			}
		} catch(IOException ioe){
			logger.error("",ioe);
			exportationEpicea.setRetour(false);
		} 
		return numFin;
	}

	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportApporteurISAGRI2(List<TaApporteur> idFactureAExporter,List<String> listComplete,Writer fw,
			int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesDocumenstLies) throws Exception {
		int numFin=numDepart;
		try {      


			TaApporteur TaApporteur = null;


			exportationEpicea.numeroDePiece = numFin;



			boolean nouvellePiece = false;


			for(int i=0; i<idFactureAExporter.size(); i++) {
				TaApporteur = idFactureAExporter.get(i);
				if(listComplete.indexOf(TaApporteur.getCodeDocument())==-1){

					/**/   // pour compenser les problèmes d'arrondis mis sur des lignes à 0
					TaApporteur.calculeTvaEtTotaux();
					/**/  //isa le 03 février 2011 en accord avec Nicolas

					if(TaApporteur.getDateExport()==null || reExport==1) { /* facture pas deja exportee ou reexportation */
						nouvellePiece = true;
						//					TaApporteur.calculeTvaEtTotaux();

						if(nouvellePiece) {
							exportationEpicea.numeroDePiece++;
							nouvellePiece = false;
							numFin++;
						}

						//recuperation des informations propre a la facture
						exportationEpicea.codePiece = TaApporteur.getCodeDocument();
						exportationEpicea.datePiece = TaApporteur.getDateDocument();
						exportationEpicea.journal = journalApporteur;
						exportationEpicea.libellePiece = TaApporteur.getLibelleDocument();
						exportationEpicea.libelleLigne = TaApporteur.getTaInfosDocument().getNomTiers();
						exportationEpicea.nomTiers = TaApporteur.getTaInfosDocument().getNomTiers();

						exportationEpicea.mtCreditLigne = LibConversion.BigDecimalToDouble(TaApporteur.getNetTtcCalc());
						exportationEpicea.numCptLigne = "+"+TaApporteur.getTaInfosDocument().getCodeCompta(); 
						
						exportationEpicea.numLignePiece = 1;

						exportationEpicea.mtDebitLigne = 0d;
						exportationEpicea.qte1 = 0d;
						exportationEpicea.codeTva ="";
						exportationEpicea.refContrepartie ="";

						//ecriture de la ligne d'entete dans le fichier d'export
						fw.write(exportationEpicea.creationLigneISAGRI2(
								exportationEpicea.journal,
								exportationEpicea.datePiece ,
								exportationEpicea.numCptLigne ,
								exportationEpicea.libelleLigne ,
								exportationEpicea.codePiece ,
								exportationEpicea.libellePiece ,
								exportationEpicea.qte1,
								exportationEpicea.mtDebitLigne ,
								exportationEpicea.mtCreditLigne ,
								exportationEpicea.refContrepartie ,
								exportationEpicea.codeTva ));
						fw.write(exportationEpicea.finDeLigne);


						//traitement des lignes
						for (TaLApporteur lf : TaApporteur.getLignes()) {
							//récupération des valeurs propres à la ligne

							exportationEpicea.typePiece = exportationEpicea.TypeAchat;
							if(lf.getTaArticle()!=null && lf.getMtTtcLApresRemiseGlobaleDocument().signum()!=0){
								exportationEpicea.libelleLigne = lf.getLibLDocument();
								exportationEpicea.qte1 = LibConversion.BigDecimalToDouble(lf.getQteLDocument());
								exportationEpicea.numCptLigne = lf.getCompteLDocument();
								if(exportationEpicea.numCptLigne==null || exportationEpicea.numCptLigne.equals("")){
									//transaction.rollback();
									exportationEpicea.setRetour(false);
									exportationEpicea.setMessageRetour("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+TaApporteur.getCodeDocument());
									throw new Exception("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+TaApporteur.getCodeDocument());
								}
								//traitement du type de tva du tiers (F ou ue ou horsUE ou Franchise)
								exportationEpicea.codeTva=lf.getCodeTvaLDocument();
								if(exportationEpicea.codeTva==null)exportationEpicea.codeTva=""; /* corrigé par isa le 11/09/2014*/

								
								exportationEpicea.mtDebitLigne = LibConversion.BigDecimalToDouble(lf.getMtTtcLApresRemiseGlobaleDocument());

								exportationEpicea.numLignePiece = exportationEpicea.numLignePiece + 1;
								exportationEpicea.mtCreditLigne = 0d;
								//ecriture de la ligne dans le fichier d'export


								fw.write(exportationEpicea.creationLigneISAGRI2(
										exportationEpicea.journal,
										exportationEpicea.datePiece ,
										exportationEpicea.numCptLigne ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.codePiece ,
										exportationEpicea.libellePiece ,
										exportationEpicea.qte1,
										exportationEpicea.mtDebitLigne ,
										exportationEpicea.mtCreditLigne ,
										exportationEpicea.refContrepartie ,
										exportationEpicea.codeTva ));
								fw.write(exportationEpicea.finDeLigne);
							}
						} 

						listComplete.add(TaApporteur.getCodeDocument());
						exportationEpicea.listCompleteDoc.add(TaApporteur);
						exportationEpicea.listeVerif.add(TaApporteur);
					}
				}
			}

		} catch(IOException ioe){
			logger.error("",ioe);
			exportationEpicea.setRetour(false);
		} 

		return numFin;
	}

	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportReglementISAGRI2(List<TaReglement> idReglementAExporter,List<String> listComplete,Writer fw,
			int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesDocumenstLies,TaFacture taFactureDirect) throws Exception {
		int numFin=numDepart;
		try {     


			Boolean gererLesPointages = gererPointages;

			TaReglement taDoc = null;

			exportationEpicea.numeroDePiece = numFin;

		

			boolean nouvellePiece = false;


			for(int i=0; i<idReglementAExporter.size(); i++) {
				taDoc = idReglementAExporter.get(i);
				if(listComplete.indexOf(taDoc.getCodeDocument())==-1 ){

					if(taDoc.getDateExport()==null || reExport==1) { /* facture pas deja exportee ou reexportation */
							if( daoRemise.findSiReglementDansRemise(taDoc.getCodeDocument()).size()==0){
							nouvellePiece = true;

							if(nouvellePiece) {
								exportationEpicea.numeroDePiece++;
								nouvellePiece = false;
								numFin++;
							}

							//recuperation des informations propre a la facture
							exportationEpicea.codePiece = taDoc.getCodeDocument();
							exportationEpicea.datePiece = taDoc.getDateDocument();
							exportationEpicea.journal = journalReglement;
							exportationEpicea.libellePiece = taDoc.getLibelleDocument();
							exportationEpicea.libelleLigne = taDoc.getLibelleDocument();

							exportationEpicea.mtDebitLigne = LibConversion.BigDecimalToDouble(taDoc.getNetTtcCalc());  

							exportationEpicea.numLignePiece = 1;
							exportationEpicea.mtCreditLigne = 0d;
							exportationEpicea.qte1 = 0d;
							exportationEpicea.codeTva ="";
							
							

							if(taDoc.getTaCompteBanque()!=null)
								exportationEpicea.numCptLigne = taDoc.getTaCompteBanque().getCptcomptable();
							if(taDoc.getTaTPaiement()!=null)
								exportationEpicea.numCptLigne = taDoc.getTaTPaiement().getCompte();
							
							//trouver le tiers lié au règlement
							TaTiers tiers=taDoc.getTaTiers();

					
					

							exportationEpicea.mtCreditLigne = 0d; 

							exportationEpicea.qte1 = 0d;

							exportationEpicea.codeTva ="";


							exportationEpicea.refContrepartie = "";
							
//							if(gererLesPointages){
//								//traitement des lignes pour les pointages
//								for (TaRReglement lf : taDoc.getTaRReglements()) {
//									//récupération des valeurs propres à la ligne
//									TaFacture fac =lf.getTaFacture();
//									numFin=initPointage(fac, taDoc, lf, gererLesDocumenstLies, idAppelant, numFin);
//								} 
//													
//							}


							//ecriture de la ligne d'entete dans le fichier d'export
							fw.write(exportationEpicea.creationLigneISAGRI2(
									exportationEpicea.journal,
									exportationEpicea.datePiece ,
									exportationEpicea.numCptLigne ,
									exportationEpicea.libelleLigne ,
									exportationEpicea.codePiece ,
									exportationEpicea.libellePiece ,
									exportationEpicea.qte1,
									exportationEpicea.mtDebitLigne ,
									exportationEpicea.mtCreditLigne ,
									exportationEpicea.refContrepartie ,
									exportationEpicea.codeTva ));
							fw.write(exportationEpicea.finDeLigne);
							
							

							//traitement de la ligne de contrepartie (tiers)
//							if(taDoc.getLibelleDocument()!=null)
//								exportationEpicea.libelleLigne = taDoc.getLibelleDocument().replace("\r","").replace("\n","");//libelle de la ligne de facture
//							else
//								exportationEpicea.libelleLigne = "";
							exportationEpicea.qte1 = 0d;
							exportationEpicea.numCptLigne = "+"+taDoc.getTaTiers().getCodeCompta();
							exportationEpicea.cptCollectif = taDoc.getTaTiers().getCompte();
							exportationEpicea.libelleLigne = taDoc.getTaTiers().getNomTiers();

							exportationEpicea.codeTva = "";

							exportationEpicea.mtDebitLigne = 0d;

							exportationEpicea.numLignePiece = exportationEpicea.numLignePiece + 1;
							exportationEpicea.mtCreditLigne = LibConversion.BigDecimalToDouble(taDoc.getNetTtcCalc());


							fw.write(exportationEpicea.creationLigneISAGRI2(
									exportationEpicea.journal,
									exportationEpicea.datePiece ,
									exportationEpicea.numCptLigne ,
									exportationEpicea.libelleLigne ,
									exportationEpicea.codePiece ,
									exportationEpicea.libellePiece ,
									exportationEpicea.qte1,
									exportationEpicea.mtDebitLigne ,
									exportationEpicea.mtCreditLigne ,
									exportationEpicea.refContrepartie ,
									exportationEpicea.codeTva ));
							fw.write(exportationEpicea.finDeLigne);

//							if(gererLesPointages){
//								exportationEpicea.typePiece = exportationEpicea.typePointage;	
//								//traitement des lignes pour les pointages
//								for (TaRReglement lf : taDoc.getTaRReglements()) {
//									//récupération des valeurs propres à la ligne
//									TaFacture fac =lf.getTaFacture();
//									numFin=initPointage(fac, taDoc, lf, gererLesDocumenstLies, idAppelant, numFin);
//								} 
//								/**** Attention !!! rajouter temporairement pour les règlements unique dans les factures*/
//								if(taDoc.getTaFactures()!=null && !taDoc.getTaFactures().isEmpty()) {
//									for (TaFacture obj : taDoc.getTaFactures()) {
//										//récupération des valeurs propres à la ligne
//										IRelationDoc r=new TaRReglement();
//										r.setAffectation(obj.getNetTtcCalc());
//										numFin=initPointage(obj, taDoc, r, gererLesDocumenstLies, idAppelant, numFin);
//									}
//								} 
//								if(taFactureDirect!=null) {
//									//récupération des valeurs propres à la ligne
//									IRelationDoc r=new TaRReglement();
//									r.setAffectation(taFactureDirect.getNetTtcCalc());
//									numFin=initPointage(taFactureDirect, taDoc, r, gererLesDocumenstLies, idAppelant, numFin);
//								} 								
//							}
//							if(taDoc.getExport()!=1)taDoc.setExport(1);
//							taDoc=daoReglement.merge(taDoc);
							listComplete.add(taDoc.getCodeDocument());
							exportationEpicea.listCompleteDoc.add(taDoc);
							exportationEpicea.listeVerif.add(taDoc);
						}
					}
				}
			}

		} catch(IOException ioe){
			logger.error("",ioe);
			exportationEpicea.setRetour(false);
		} 

		return numFin;
	}

	
	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportRemiseISAGRI2(List<TaRemise> idRemiseAExporter,List<String> listComplete,Writer fw,
			int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesDocumenstLies) throws Exception {
		int numFin=numDepart;
		try {      

			Boolean gererLesPointages = gererPointages;

			TaRemise taDoc = null;


			exportationEpicea.numeroDePiece = numFin;

			

			boolean nouvellePiece = false;


			for(int i=0; i<idRemiseAExporter.size(); i++) {
				taDoc = idRemiseAExporter.get(i);

				if(listComplete.indexOf(taDoc.getCodeDocument())==-1){

					if(taDoc.getDateExport()==null || reExport==1) { /* remise pas deja exportee ou reexportation */
						nouvellePiece = true;

						if(nouvellePiece) {
							exportationEpicea.numeroDePiece++;
							nouvellePiece = false;
							numFin++;
						}

						//recuperation des informations propre a la REMISE
						exportationEpicea.codePiece = taDoc.getCodeDocument();
						exportationEpicea.datePiece = taDoc.getDateDocument();
						exportationEpicea.journal = journalRemise;
						exportationEpicea.libellePiece = taDoc.getLibelleDocument();
						exportationEpicea.libelleLigne = taDoc.getLibelleDocument();

						exportationEpicea.mtDebitLigne = LibConversion.BigDecimalToDouble(taDoc.getNetTtcCalc());  


						exportationEpicea.qte1 = 0d;
						exportationEpicea.codeTva ="";
						
						if(taDoc.getTaCompteBanque()!=null)
							exportationEpicea.numCptLigne = taDoc.getTaCompteBanque().getCptcomptable();
						else
						if(taDoc.getTaTPaiement()!=null)
							exportationEpicea.numCptLigne = taDoc.getTaTPaiement().getCompte();//taDoc.getTaCompteBanque().getCptcomptable();


						exportationEpicea.mtCreditLigne = 0d;  

						exportationEpicea.mtDebitLigne = LibConversion.BigDecimalToDouble(taDoc.getNetTtcCalc());



						exportationEpicea.refContrepartie = "";


						//ecriture de la ligne d'entete dans le fichier d'export
						fw.write(exportationEpicea.creationLigneISAGRI2(
								exportationEpicea.journal,
								exportationEpicea.datePiece ,
								exportationEpicea.numCptLigne ,
								exportationEpicea.libelleLigne ,
								exportationEpicea.codePiece ,
								exportationEpicea.libellePiece ,
								exportationEpicea.qte1,
								exportationEpicea.mtDebitLigne ,
								exportationEpicea.mtCreditLigne ,
								exportationEpicea.refContrepartie ,
								exportationEpicea.codeTva ));
						fw.write(exportationEpicea.finDeLigne);


						//traitement pour exporter les relations avec les factures					
						//traitement des lignes
						for (TaLRemise lf : taDoc.getTaLRemises()) {
							//récupération des valeurs propres à la ligne
							if(lf.getTaReglement()!=null && listComplete.indexOf(lf.getTaReglement().getCodeDocument())==-1){

								if(lf.getTaReglement().getLibelleDocument()!=null)
									exportationEpicea.libelleLigne = lf.getTaReglement().getLibelleDocument().replace("\r","").replace("\n","");
								else
									exportationEpicea.libelleLigne = "";
								exportationEpicea.qte1 = 0d;

								exportationEpicea.numCptLigne = "+"+lf.getTaReglement().getTaTiers().getCodeTiers();
								exportationEpicea.cptCollectif = lf.getTaReglement().getTaTiers().getCompte();
								exportationEpicea.libelleLigne = lf.getTaReglement().getTaTiers().getNomTiers();

								exportationEpicea.codeTva = "";


								exportationEpicea.mtDebitLigne = 0d;

								exportationEpicea.mtCreditLigne = LibConversion.BigDecimalToDouble(lf.getTaReglement().getNetTtcCalc());




								fw.write(exportationEpicea.creationLigneISAGRI2(
										exportationEpicea.journal,
										exportationEpicea.datePiece ,
										exportationEpicea.numCptLigne ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.codePiece ,
										exportationEpicea.libellePiece ,
										exportationEpicea.qte1,
										exportationEpicea.mtDebitLigne ,
										exportationEpicea.mtCreditLigne ,
										exportationEpicea.refContrepartie ,
										exportationEpicea.codeTva ));
								fw.write(exportationEpicea.finDeLigne);

//								//traitment des pointages si on les gere
//								if(gererLesPointages){
//									exportationEpicea.typePiece = exportationEpicea.typePointage;	
//									for (TaRReglement Relation : lf.getTaReglement().getTaRReglements()) {
//										TaFacture fac =Relation.getTaFacture();
//										idAppelant=fac.getIdDocument();
//										numFin=initPointage(fac, lf.getTaReglement(), Relation, gererLesDocumenstLies, idAppelant, numFin);
//									}
//								}//fin si on gere les pointages
								exportationEpicea.listCompleteDoc.add(lf.getTaReglement());
								listComplete.add(lf.getTaReglement().getCodeDocument());
								exportationEpicea.listeVerif.add(lf.getTaReglement());
							}//fin on traite le reglement

					
							if(lf.getDateExport()==null)lf.setDateExport(new Date());
							
						} 

						exportationEpicea.listCompleteDoc.add(taDoc);
						listComplete.add(taDoc.getCodeDocument());
						exportationEpicea.listeVerif.add(taDoc);
					}
				}
			}
		} catch(IOException ioe){
			logger.error("",ioe);
			exportationEpicea.setRetour(false);
		} 

		return numFin;

	}

	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportApporteurCentraliseeISAGRI2(List<TaApporteur> idFactureAExporter,List<String> listComplete,Writer fw,
			int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesDocumenstLies) throws Exception {
		int numFin=numDepart;
		try {      


			TaApporteur TaApporteur = null;


			exportationEpicea.numeroDePiece = numFin;



			boolean nouvellePiece = false;


			for(int i=0; i<idFactureAExporter.size(); i++) {
				TaApporteur = idFactureAExporter.get(i);
				if(listComplete.indexOf(TaApporteur.getCodeDocument())==-1){

					/**/   // pour compenser les problèmes d'arrondis mis sur des lignes à 0
					TaApporteur.calculeTvaEtTotaux();
					/**/  //isa le 03 février 2011 en accord avec Nicolas

					if(TaApporteur.getDateExport()==null || reExport==1) { /* facture pas deja exportee ou reexportation */
						nouvellePiece = true;
						//					TaApporteur.calculeTvaEtTotaux();

						if(nouvellePiece) {
							exportationEpicea.numeroDePiece++;
							nouvellePiece = false;
							numFin++;
						}

						//on crée un nouvel objet Apporteur pour créer la centralisation
						TaApporteur newApporteur =new TaApporteur(false);
						newApporteur=(TaApporteur) TaApporteur.cloneCentralisation();
						newApporteur.getLignes().clear();
						
						TaLApporteur newTaLApporteur;
						for (TaLApporteur obj : TaApporteur.getLignes()) {
							newTaLApporteur=newApporteur.contientMemeParametreCompte(obj);
							if(newTaLApporteur!=null){
								newTaLApporteur.setQteLDocument(BigDecimal.ONE);
								if(TaApporteur.getTtc()==1) {
									newTaLApporteur.setPrixULDocument(newTaLApporteur.getPrixULDocument().add(obj.getMtTtcLApresRemiseGlobaleDocument()));
								}else{
									newTaLApporteur.setPrixULDocument(newTaLApporteur.getPrixULDocument().add(obj.getMtHtLApresRemiseGlobaleDocument()));
								}
							}else{
								if(obj.getTaArticle()!=null){
								newTaLApporteur=new TaLApporteur(false);
								newTaLApporteur=obj.clone();
								newTaLApporteur.setLibLDocument(newApporteur.getLibelleDocument());
								newTaLApporteur.setQteLDocument(BigDecimal.ONE);
								newTaLApporteur.setRemTxLDocument(BigDecimal.ZERO);
								if(TaApporteur.getTtc()==1) {
									newTaLApporteur.setPrixULDocument(obj.getMtTtcLApresRemiseGlobaleDocument());
								}else{
									newTaLApporteur.setPrixULDocument(obj.getMtHtLApresRemiseGlobaleDocument());
								}
								newTaLApporteur.setTaDocument(newApporteur);
								newApporteur.addLigne(newTaLApporteur);
								}
							}
						}
						newApporteur.setTxRemHtDocument(BigDecimal.ZERO);
						newApporteur.calculeTvaEtTotaux();
						
						//recuperation des informations propre a la facture
						exportationEpicea.codePiece = TaApporteur.getCodeDocument();
						exportationEpicea.datePiece = TaApporteur.getDateDocument();
						exportationEpicea.journal = journalApporteur;
						exportationEpicea.libellePiece = TaApporteur.getLibelleDocument();
						exportationEpicea.libelleLigne = TaApporteur.getTaInfosDocument().getNomTiers();
						exportationEpicea.nomTiers = TaApporteur.getTaInfosDocument().getNomTiers();

						exportationEpicea.mtCreditLigne = LibConversion.BigDecimalToDouble(TaApporteur.getNetTtcCalc());
						exportationEpicea.numCptLigne = "+"+TaApporteur.getTaInfosDocument().getCodeCompta(); 
						
						exportationEpicea.numLignePiece = 1;

						exportationEpicea.mtDebitLigne = 0d;
						exportationEpicea.qte1 = 0d;
						exportationEpicea.codeTva ="";
						exportationEpicea.refContrepartie ="";

						//ecriture de la ligne d'entete dans le fichier d'export
						fw.write(exportationEpicea.creationLigneISAGRI2(
								exportationEpicea.journal,
								exportationEpicea.datePiece ,
								exportationEpicea.numCptLigne ,
								exportationEpicea.libelleLigne ,
								exportationEpicea.codePiece ,
								exportationEpicea.libellePiece ,
								exportationEpicea.qte1,
								exportationEpicea.mtDebitLigne ,
								exportationEpicea.mtCreditLigne ,
								exportationEpicea.refContrepartie ,
								exportationEpicea.codeTva ));
						fw.write(exportationEpicea.finDeLigne);


						//traitement des lignes
						for (TaLApporteur lf : TaApporteur.getLignes()) {
							//récupération des valeurs propres à la ligne

							exportationEpicea.typePiece = exportationEpicea.TypeAchat;
							if(lf.getTaArticle()!=null && lf.getMtTtcLApresRemiseGlobaleDocument().signum()!=0){
								exportationEpicea.libelleLigne = lf.getLibLDocument();
								exportationEpicea.qte1 = LibConversion.BigDecimalToDouble(lf.getQteLDocument());
								exportationEpicea.numCptLigne = lf.getCompteLDocument();
								if(exportationEpicea.numCptLigne==null || exportationEpicea.numCptLigne.equals("")){
									//transaction.rollback();
									exportationEpicea.setRetour(false);
									exportationEpicea.setMessageRetour("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+TaApporteur.getCodeDocument());
									throw new Exception("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+TaApporteur.getCodeDocument());
								}
								//traitement du type de tva du tiers (F ou ue ou horsUE ou Franchise)
								exportationEpicea.codeTva=lf.getCodeTvaLDocument();
								if(exportationEpicea.codeTva==null)exportationEpicea.codeTva=""; /* corrigé par isa le 11/09/2014*/

								
								exportationEpicea.mtDebitLigne = LibConversion.BigDecimalToDouble(lf.getMtTtcLApresRemiseGlobaleDocument());

								exportationEpicea.numLignePiece = exportationEpicea.numLignePiece + 1;
								exportationEpicea.mtCreditLigne = 0d;
								//ecriture de la ligne dans le fichier d'export


								fw.write(exportationEpicea.creationLigneISAGRI2(
										exportationEpicea.journal,
										exportationEpicea.datePiece ,
										exportationEpicea.numCptLigne ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.codePiece ,
										exportationEpicea.libellePiece ,
										exportationEpicea.qte1,
										exportationEpicea.mtDebitLigne ,
										exportationEpicea.mtCreditLigne ,
										exportationEpicea.refContrepartie ,
										exportationEpicea.codeTva ));
								fw.write(exportationEpicea.finDeLigne);
							}
						} 

						listComplete.add(TaApporteur.getCodeDocument());
						exportationEpicea.listCompleteDoc.add(TaApporteur);
						exportationEpicea.listeVerif.add(TaApporteur);
					}
				}
			}

		} catch(IOException ioe){
			logger.error("",ioe);
			exportationEpicea.setRetour(false);
		} 

		return numFin;
	}

	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportAvoircentraliseeISAGRI2(List<TaAvoir> idFactureAExporter,List<String> listComplete,Writer fw,
			int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesDocumenstLies) throws Exception {
		int numFin=numDepart;
		try {      


			Boolean gererLesPointages = gererPointages;

			TaAvoir taAvoir = null;


			exportationEpicea.numeroDePiece = numFin;



			boolean nouvellePiece = false;

			for(int i=0; i<idFactureAExporter.size(); i++) {
				taAvoir = idFactureAExporter.get(i);
				if(listComplete.indexOf(taAvoir.getCodeDocument())==-1){

					/**/   // pour compenser les problèmes d'arrondis mis sur des lignes à 0
					taAvoir.calculeTvaEtTotaux();
					/**/  //isa le 03 février 2011 en accord avec Nicolas

					if(taAvoir.getDateExport()==null || reExport==1) { /* facture pas deja exportee ou reexportation */
						nouvellePiece = true;

						if(nouvellePiece) {
							exportationEpicea.numeroDePiece++;
							nouvellePiece = false;
							numFin++;
						}

						//on crée un nouvel objet Avoir pour créer la centralisation
						TaAvoir newAvoir =new TaAvoir(false);
						newAvoir=(TaAvoir) taAvoir.cloneCentralisation();
						newAvoir.getLignes().clear();
						
						TaLAvoir newTaLAvoir;
						for (TaLAvoir obj : taAvoir.getLignes()) {
							newTaLAvoir=newAvoir.contientMemeParametreCompte(obj);
							if(newTaLAvoir!=null){
								newTaLAvoir.setQteLDocument(BigDecimal.ONE);
								if(taAvoir.getTtc()==1) {
									newTaLAvoir.setPrixULDocument(newTaLAvoir.getPrixULDocument().add(obj.getMtTtcLApresRemiseGlobaleDocument()));
								}else{
									newTaLAvoir.setPrixULDocument(newTaLAvoir.getPrixULDocument().add(obj.getMtHtLApresRemiseGlobaleDocument()));
								}
							}else{
								if(obj.getTaArticle()!=null){
								newTaLAvoir=new TaLAvoir(false);
								newTaLAvoir=obj.clone();
								newTaLAvoir.setLibLDocument(newAvoir.getLibelleDocument());
								newTaLAvoir.setQteLDocument(BigDecimal.ONE);
								newTaLAvoir.setRemTxLDocument(BigDecimal.ZERO);
								if(taAvoir.getTtc()==1) {
									newTaLAvoir.setPrixULDocument(obj.getMtTtcLApresRemiseGlobaleDocument());
								}else{
									newTaLAvoir.setPrixULDocument(obj.getMtHtLApresRemiseGlobaleDocument());
								}
								newTaLAvoir.setTaDocument(newAvoir);
								newAvoir.addLigne(newTaLAvoir);
								}
							}
						}
						newAvoir.setTxRemHtDocument(BigDecimal.ZERO);
						newAvoir.calculeTvaEtTotaux();
						
						
						//recuperation des informations propre a la facture
						exportationEpicea.codePiece = taAvoir.getCodeDocument();
						exportationEpicea.datePiece = taAvoir.getDateDocument();
						exportationEpicea.journal = journalAvoir;
						exportationEpicea.libellePiece = taAvoir.getLibelleDocument();
						exportationEpicea.libelleLigne = taAvoir.getTaInfosDocument().getNomTiers();
						exportationEpicea.nomTiers = taAvoir.getTaInfosDocument().getNomTiers();

						exportationEpicea.mtCreditLigne = LibConversion.BigDecimalToDouble(taAvoir.getNetTtcCalc());
						exportationEpicea.numCptLigne = "+"+taAvoir.getTaInfosDocument().getCodeCompta(); 
						
						exportationEpicea.numLignePiece = 1;

						exportationEpicea.mtDebitLigne = 0d;
						exportationEpicea.qte1 = 0d;
						exportationEpicea.codeTva ="";
						exportationEpicea.refContrepartie ="";
						
//						if(gererLesPointages){
//							//traitement des lignes pour les pointages
//							for (TaRAvoir lf : taAvoir.getTaRAvoirs()) {
//								//récupération des valeurs propres à la ligne
//								exportationEpicea.refContrepartie = taAvoir.getCodeDocument();
//							} 
//						}


						//ecriture de la ligne d'entete dans le fichier d'export
						fw.write(exportationEpicea.creationLigneISAGRI2(
								exportationEpicea.journal,
								exportationEpicea.datePiece ,
								exportationEpicea.numCptLigne ,
								exportationEpicea.libelleLigne ,
								exportationEpicea.codePiece ,
								exportationEpicea.libellePiece ,
								exportationEpicea.qte1,
								exportationEpicea.mtDebitLigne ,
								exportationEpicea.mtCreditLigne ,
								exportationEpicea.refContrepartie ,
								exportationEpicea.codeTva ));
						fw.write(exportationEpicea.finDeLigne);



						//traitement des lignes
						for (TaLAvoir lf : taAvoir.getLignes()) {
							//récupération des valeurs propres à la ligne

							
							if(lf.getTaArticle()!=null && lf.getMtTtcLApresRemiseGlobaleDocument().signum()!=0){
								exportationEpicea.libelleLigne = lf.getLibLDocument();
								exportationEpicea.qte1 = LibConversion.BigDecimalToDouble(lf.getQteLDocument());
								exportationEpicea.numCptLigne = lf.getCompteLDocument();
								if(exportationEpicea.numCptLigne==null || exportationEpicea.numCptLigne.equals("")){
									//transaction.rollback();
									exportationEpicea.setRetour(false);
									exportationEpicea.setMessageRetour("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taAvoir.getCodeDocument());
									throw new Exception("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taAvoir.getCodeDocument());
								}
								//traitement du type de tva du tiers (F ou ue ou horsUE ou Franchise)
								exportationEpicea.codeTva=lf.getCodeTvaLDocument();
								if(exportationEpicea.codeTva==null)exportationEpicea.codeTva=""; /* corrigé par isa le 11/09/2014*/

								
								exportationEpicea.mtDebitLigne = LibConversion.BigDecimalToDouble(lf.getMtTtcLApresRemiseGlobaleDocument());

								exportationEpicea.numLignePiece = exportationEpicea.numLignePiece + 1;
								exportationEpicea.mtCreditLigne = 0d;
								//ecriture de la ligne dans le fichier d'export


								fw.write(exportationEpicea.creationLigneISAGRI2(
										exportationEpicea.journal,
										exportationEpicea.datePiece ,
										exportationEpicea.numCptLigne ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.codePiece ,
										exportationEpicea.libellePiece ,
										exportationEpicea.qte1,
										exportationEpicea.mtDebitLigne ,
										exportationEpicea.mtCreditLigne ,
										exportationEpicea.refContrepartie ,
										exportationEpicea.codeTva ));
								fw.write(exportationEpicea.finDeLigne);
							}
						} 

						exportationEpicea.listCompleteDoc.add(taAvoir);
						listComplete.add(taAvoir.getCodeDocument());
						exportationEpicea.listeVerif.add(taAvoir);
					}
				}
			}
		} catch(IOException ioe){
			logger.error("",ioe);
			exportationEpicea.setRetour(false);
		} 
		return numFin;
	}


	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportFactureCentraliseeISAGRI2(List<TaFacture> idFactureAExporter,List<String> listComplete,Writer fw,
			 int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesReglements) throws Exception {
		int numFin=numDepart;
		try { 
			TaFacture taFacture = null;

			exportationEpicea.numeroDePiece = numFin;

			boolean nouvellePiece = false;

			for(int i=0; i<idFactureAExporter.size(); i++) {
				taFacture = idFactureAExporter.get(i);
				if(listComplete.indexOf(taFacture.getCodeDocument())==-1){

					/**/   // pour compenser les problèmes d'arrondis mis sur des lignes à 0
					taFacture.calculeTvaEtTotaux();
					/**/  //isa le 03 février 2011 en accord avec Nicolas


					if(taFacture.getDateExport()==null || reExport==1) { /* facture pas deja exportee ou reexportation */
						nouvellePiece = true;

						if(nouvellePiece) {
							exportationEpicea.numeroDePiece++;
							nouvellePiece = false;
							numFin++;
						}

						//on crée un nouvel objet facture pour créer la centralisation
						TaFacture newFacture =new TaFacture(false);
						newFacture=(TaFacture) taFacture.cloneCentralisation();
						newFacture.getLignes().clear();
						
						TaLFacture newTaLFacture;
						for (TaLFacture obj : taFacture.getLignes()) {
							newTaLFacture=newFacture.contientMemeParametreCompte(obj);
							if(newTaLFacture!=null){
								newTaLFacture.setQteLDocument(BigDecimal.ONE);
								if(taFacture.getTtc()==1) {
									newTaLFacture.setPrixULDocument(newTaLFacture.getPrixULDocument().add(obj.getMtTtcLApresRemiseGlobaleDocument()));
								}else{
									newTaLFacture.setPrixULDocument(newTaLFacture.getPrixULDocument().add(obj.getMtHtLApresRemiseGlobaleDocument()));
								}
							}else{
								if(obj.getTaArticle()!=null){
								newTaLFacture=new TaLFacture(false);
								newTaLFacture=obj.clone();
								newTaLFacture.setLibLDocument(newFacture.getLibelleDocument());
								newTaLFacture.setQteLDocument(BigDecimal.ONE);
								newTaLFacture.setRemTxLDocument(BigDecimal.ZERO);
								if(taFacture.getTtc()==1) {
									newTaLFacture.setPrixULDocument(obj.getMtTtcLApresRemiseGlobaleDocument());
								}else{
									newTaLFacture.setPrixULDocument(obj.getMtHtLApresRemiseGlobaleDocument());
								}
								newTaLFacture.setTaDocument(newFacture);
								newFacture.addLigne(newTaLFacture);
								}
							}
						}
						newFacture.setTxRemHtDocument(BigDecimal.ZERO);
						newFacture.calculeTvaEtTotaux();
						

						
						//recuperation des informations propre a la facture
						exportationEpicea.codePiece = taFacture.getCodeDocument();
						exportationEpicea.datePiece = taFacture.getDateDocument();
						exportationEpicea.journal = journalFacture;
						exportationEpicea.libellePiece = taFacture.getLibelleDocument();
						exportationEpicea.libelleLigne = taFacture.getTaInfosDocument().getNomTiers();
						exportationEpicea.nomTiers = taFacture.getTaInfosDocument().getNomTiers();

						exportationEpicea.mtDebitLigne = LibConversion.BigDecimalToDouble(taFacture.getNetTtcCalc());  

						exportationEpicea.numCptLigne = "+"+taFacture.getTaInfosDocument().getCodeCompta();    
						exportationEpicea.numLignePiece = 1;
						exportationEpicea.mtCreditLigne = 0d;
						exportationEpicea.qte1 = 0d;
						exportationEpicea.codeTva ="";
						
						exportationEpicea.refContrepartie="";
//						if(gererPointages){
//							if(taFacture.getTaRReglements()!=null && taFacture.getTaRReglements().size()>0)
//								exportationEpicea.refContrepartie=exportationEpicea.codePiece;
//						}
							
						//ecriture de la ligne d'entete dans le fichier d'export
						fw.write(exportationEpicea.creationLigneISAGRI2(
								exportationEpicea.journal,
								exportationEpicea.datePiece ,
								exportationEpicea.numCptLigne ,
								exportationEpicea.libelleLigne ,
								exportationEpicea.codePiece ,
								exportationEpicea.libellePiece ,
								exportationEpicea.qte1,
								exportationEpicea.mtDebitLigne ,
								exportationEpicea.mtCreditLigne ,
								exportationEpicea.refContrepartie ,
								exportationEpicea.codeTva ));
						fw.write(exportationEpicea.finDeLigne);
						

						BigDecimal escompte = taFacture.getRemTtcDocument();
						if (escompte.signum() > 0) { 
							//création d'une lignes d'escompte
							exportationEpicea.numCptLigne = exportationEpicea.ctpEscompteDebit;
							exportationEpicea.libelleLigne = "Escompte "+taFacture.getLibelleDocument();
							exportationEpicea.mtDebitLigne = LibConversion.BigDecimalToDouble(escompte);
							exportationEpicea.mtCreditLigne = 0d;
							exportationEpicea.qte1 = 0d;
							exportationEpicea.codeTva = "";

							//ecriture de la ligne d'escompte dans le fichier d'export


							fw.write(exportationEpicea.creationLigneISAGRI2(
									exportationEpicea.journal,
									exportationEpicea.datePiece ,
									exportationEpicea.numCptLigne ,
									exportationEpicea.libelleLigne ,
									exportationEpicea.codePiece ,
									exportationEpicea.libellePiece ,
									exportationEpicea.qte1,
									exportationEpicea.mtDebitLigne ,
									exportationEpicea.mtCreditLigne ,
									exportationEpicea.refContrepartie ,
									exportationEpicea.codeTva ));
							fw.write(exportationEpicea.finDeLigne);
						}

						//traitement des lignes
						for (TaLFacture lf : taFacture.getLignes()) {
							//récupération des valeurs propres à la ligne                 
							if(lf.getTaArticle()!=null   && lf.getMtTtcLApresRemiseGlobaleDocument()!=null ){
								exportationEpicea.libelleLigne = lf.getLibLDocument();
								exportationEpicea.qte1 = LibConversion.BigDecimalToDouble(lf.getQteLDocument());
								exportationEpicea.numCptLigne = lf.getCompteLDocument();
								if(exportationEpicea.numCptLigne==null || exportationEpicea.numCptLigne.equals("")){
									exportationEpicea.setRetour(false);
									exportationEpicea.setMessageRetour("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taFacture.getCodeDocument());
									throw new Exception("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taFacture.getCodeDocument());
								}
								
								//traitement du type de tva du tiers (F ou ue ou horsUE ou Franchise)

								exportationEpicea.codeTva=lf.getCodeTvaLDocument();
									
								if(exportationEpicea.codeTva==null)exportationEpicea.codeTva="";

//								exportationEpicea.tauxTva = LibConversion.BigDecimalToDouble(lf.getTauxTvaLDocument());
								exportationEpicea.mtCreditLigne = LibConversion.BigDecimalToDouble(lf.getMtTtcLApresRemiseGlobaleDocument());

								exportationEpicea.mtDebitLigne = 0d;


								fw.write(exportationEpicea.creationLigneISAGRI2(
										exportationEpicea.journal,
										exportationEpicea.datePiece ,
										exportationEpicea.numCptLigne ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.codePiece ,
										exportationEpicea.libellePiece ,
										exportationEpicea.qte1,
										exportationEpicea.mtDebitLigne ,
										exportationEpicea.mtCreditLigne ,
										exportationEpicea.refContrepartie ,
										exportationEpicea.codeTva ));
								fw.write(exportationEpicea.finDeLigne);
							}
						}

						if(gererLesReglements){
								boolean priseEnChargeAcomptes=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.ACOMPTES);
								boolean priseEnChargeReglementSimple=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.REGLEMENT_SIMPLE);
								boolean priseEnChargeRemise=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.REMISE);

							List<TaRemise> listeRemise = new LinkedList<TaRemise>();
							//récupérer tous les documents directement liés à cette facture
							for (TaRReglement rReglement : taFacture.getTaRReglements()) {
								List<TaReglement> liste=new LinkedList<TaReglement>();
								List<TaRemise> listeRemiseReglement =new LinkedList<TaRemise>();
								listeRemiseReglement=daoRemise.findSiReglementDansRemise(rReglement.getTaReglement().getCodeDocument());


								if(listeRemiseReglement.size()==0 && priseEnChargeReglementSimple){
									liste.add(rReglement.getTaReglement());
									numFin=exportReglementISAGRI2(liste, listComplete, fw,  exportationEpicea.getReExportReglement(), numFin, gererPointages,
											taFacture.getIdDocument(),false,null);
								}else{
									for (TaRemise taRemise : listeRemiseReglement) {
										if(listeRemise.indexOf(taRemise)==-1 )
											listeRemise.add(taRemise);
									}
								}

							}
							/**** Atention Cette partie est rajoutée temporaire jusqu'à gestion des règlements multiples*/
							if (taFacture.getTaReglement()!=null) {
								List<TaReglement> liste=new LinkedList<TaReglement>();
								List<TaRemise> listeRemiseReglement =new LinkedList<TaRemise>();
								listeRemiseReglement=daoRemise.findSiReglementDansRemise(taFacture.getTaReglement().getCodeDocument());


								if(listeRemiseReglement.size()==0 && priseEnChargeReglementSimple){
									liste.add(taFacture.getTaReglement());
									numFin=exportReglementISAGRI2(liste, listComplete, fw,  exportationEpicea.getReExportReglement(), numFin, gererPointages,
											taFacture.getIdDocument(),false,taFacture);
								}else{
									for (TaRemise taRemise : listeRemiseReglement) {
										if(listeRemise.indexOf(taRemise)==-1 )
											listeRemise.add(taRemise);
									}
								}

							}							
							
							if(priseEnChargeRemise){
								if(listeRemise.size()>0){
									numFin=exportRemiseISAGRI2(listeRemise, listComplete, fw,  exportationEpicea.getReExportRemise(), numFin, gererPointages, 
											taFacture.getIdDocument(),false);
								}
							}
							}
//						else if(gererPointages){
////								for (TaRAcompte r : taFacture.getTaRAcomptes()) {
////									numFin=initPointage(taFacture, r.getTaAcompte(), r, false, idAppelant, numFin);
////								}
//								for (TaRAvoir r : taFacture.getTaRAvoirs()) {
//									numFin=initPointage(taFacture, r.getTaAvoir(), r, false, idAppelant, numFin);
//								}
//								for (TaRReglement r : taFacture.getTaRReglements()) {
//									numFin=initPointage(taFacture, r.getTaReglement(), r, false, idAppelant, numFin);
//								}
//								if(taFacture.getTaReglement()!=null) {
//									IRelationDoc r=new TaRReglement();
//									r.setAffectation(taFacture.getTaReglement().getNetTtcCalc());
//									numFin=initPointage(taFacture, taFacture.getTaReglement(), r, false, idAppelant, numFin);
//								}							
//							}
								
							listComplete.add(taFacture.getCodeDocument());
							exportationEpicea.listCompleteDoc.add(taFacture);
							exportationEpicea.listeVerif.add(taFacture);
						}
					}
				}

			} catch(IOException ioe){
				logger.error("",ioe);
				exportationEpicea.setRetour(false);
			} 

			return numFin;
		}	


	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportFactureISAGRIFinal(List<TaFacture> idFactureAExporter,List<String> listComplete,Writer fw,
			 int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesReglements) throws Exception {
		int numFin=numDepart;
		try { 
			TaFacture taFacture = null;

			exportationEpicea.numeroDePiece = numFin;

			boolean nouvellePiece = false;

			for(int i=0; i<idFactureAExporter.size(); i++) {
				taFacture = idFactureAExporter.get(i);
				if(listComplete.indexOf(taFacture.getCodeDocument())==-1){

					/**/   // pour compenser les problèmes d'arrondis mis sur des lignes à 0
					taFacture.calculeTvaEtTotaux();
					/**/  //isa le 03 février 2011 en accord avec Nicolas


					if(taFacture.getDateExport()==null || reExport==1) { /* facture pas deja exportee ou reexportation */
						nouvellePiece = true;

						if(nouvellePiece) {
							exportationEpicea.numeroDePiece++;
							exportationEpicea.getListeLignesTva().clear();
							nouvellePiece = false;
							numFin++;
						}

						//recuperation des informations propre a la facture
						exportationEpicea.codePiece = taFacture.getCodeDocument();
						exportationEpicea.datePiece = taFacture.getDateDocument();
						exportationEpicea.journal = journalFacture;
						exportationEpicea.libellePiece = taFacture.getLibelleDocument();
						exportationEpicea.libelleLigne = taFacture.getTaInfosDocument().getNomTiers();
						exportationEpicea.nomTiers = taFacture.getTaInfosDocument().getNomTiers();

						exportationEpicea.mtDebitLigne = LibConversion.BigDecimalToDouble(taFacture.getNetTtcCalc());  

						exportationEpicea.numCptLigne = taFacture.getTaInfosDocument().getCompte()+taFacture.getTaInfosDocument().getCodeCompta();    
						exportationEpicea.numLignePiece = 1;
						exportationEpicea.mtCreditLigne = 0d;
						exportationEpicea.qte1 = 0d;
						exportationEpicea.codeTva ="";
						
						exportationEpicea.refContrepartie="";
//						if(gererPointages){
//							if(taFacture.getTaRReglements()!=null && taFacture.getTaRReglements().size()>0)
//								exportationEpicea.refContrepartie=exportationEpicea.codePiece;
//						}
							
						//ecriture de la ligne d'entete dans le fichier d'export
						fw.write(exportationEpicea.creationLigneISAGRIFinal(
								exportationEpicea.journal,
								exportationEpicea.datePiece ,
								exportationEpicea.numCptLigne ,
								exportationEpicea.libelleLigne ,
								exportationEpicea.codePiece ,
								exportationEpicea.libellePiece ,								
								exportationEpicea.mtDebitLigne ,
								exportationEpicea.mtCreditLigne ,
								exportationEpicea.refContrepartie ,
								exportationEpicea.codeTva,
								exportationEpicea.qte1));
						fw.write(exportationEpicea.finDeLigne);
						

						BigDecimal escompte = taFacture.getRemTtcDocument();
						if (escompte.signum() > 0) { 
							//création d'une lignes d'escompte
							exportationEpicea.numCptLigne = exportationEpicea.ctpEscompteDebit;
							exportationEpicea.libelleLigne = "Escompte "+taFacture.getLibelleDocument();
							exportationEpicea.mtDebitLigne = LibConversion.BigDecimalToDouble(escompte);
							exportationEpicea.mtCreditLigne = 0d;
							exportationEpicea.qte1 = 0d;
							exportationEpicea.codeTva = "";

							//ecriture de la ligne d'escompte dans le fichier d'export


							fw.write(exportationEpicea.creationLigneISAGRIFinal(
									exportationEpicea.journal,
									exportationEpicea.datePiece ,
									exportationEpicea.numCptLigne ,
									exportationEpicea.libelleLigne ,
									exportationEpicea.codePiece ,
									exportationEpicea.libellePiece ,
									exportationEpicea.mtDebitLigne ,
									exportationEpicea.mtCreditLigne ,
									exportationEpicea.refContrepartie ,
									exportationEpicea.codeTva,
									exportationEpicea.qte1));
							fw.write(exportationEpicea.finDeLigne);
						}

						//traitement des lignes
						for (TaLFacture lf : taFacture.getLignes()) {
							//récupération des valeurs propres à la ligne                 
							if(lf.getTaArticle()!=null   && lf.getMtTtcLApresRemiseGlobaleDocument()!=null ){
								exportationEpicea.libelleLigne = lf.getLibLDocument();
								exportationEpicea.qte1 = LibConversion.BigDecimalToDouble(lf.getQteLDocument());
								exportationEpicea.numCptLigne = lf.getCompteLDocument();
								if(exportationEpicea.numCptLigne==null || exportationEpicea.numCptLigne.equals("")){
									exportationEpicea.setRetour(false);
									exportationEpicea.setMessageRetour("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taFacture.getCodeDocument());
									throw new Exception("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taFacture.getCodeDocument());
								}
								
								//traitement du type de tva du tiers (F ou ue ou horsUE ou Franchise)

								exportationEpicea.codeTva=lf.getCodeTvaLDocument();
									
								if(exportationEpicea.codeTva==null)exportationEpicea.codeTva="";

//								exportationEpicea.tauxTva = LibConversion.BigDecimalToDouble(lf.getTauxTvaLDocument());
								exportationEpicea.mtCreditLigne = LibConversion.BigDecimalToDouble(lf.getMtHtLApresRemiseGlobaleDocument());
								exportationEpicea.mtCreditTva = LibConversion.BigDecimalToDouble(lf.getMtTtcLApresRemiseGlobaleDocument().subtract(lf.getMtHtLApresRemiseGlobaleDocument()));

								if(exportationEpicea.codeTva!="") {
									rempliListeLigneTva(exportationEpicea.codeTva, lf.getMtTtcLApresRemiseGlobaleDocument().subtract(lf.getMtHtLApresRemiseGlobaleDocument()), exportationEpicea.mtCreditTva<0);
								}
								exportationEpicea.mtDebitLigne = 0d;


								fw.write(exportationEpicea.creationLigneISAGRIFinal(
										exportationEpicea.journal,
										exportationEpicea.datePiece ,
										exportationEpicea.numCptLigne ,
										"" ,
										exportationEpicea.codePiece ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.mtDebitLigne ,
										exportationEpicea.mtCreditLigne ,
										exportationEpicea.refContrepartie ,
										exportationEpicea.codeTva,
										exportationEpicea.qte1));
								fw.write(exportationEpicea.finDeLigne);
							}
						}


						for (LigneTva l : exportationEpicea.getListeLignesTva()) {
							exportationEpicea.qte1 = 0d;
							exportationEpicea.mtDebitTva=0d;
							exportationEpicea.mtCreditTva=LibConversion.BigDecimalToDouble(l.getMtTva());
							exportationEpicea.numCptLigne=l.getNumcptTva();
							fw.write(exportationEpicea.creationLigneISAGRIFinal(
									exportationEpicea.journal,
									exportationEpicea.datePiece ,
									exportationEpicea.numCptLigne ,
									"" ,
									exportationEpicea.codePiece ,
									exportationEpicea.libellePiece ,
									exportationEpicea.mtDebitTva ,
									exportationEpicea.mtCreditTva ,
									"" ,
									"",
									exportationEpicea.qte1));
							fw.write(exportationEpicea.finDeLigne);
						}
						
						if(gererLesReglements){
								boolean priseEnChargeAcomptes=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.ACOMPTES);
								boolean priseEnChargeReglementSimple=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.REGLEMENT_SIMPLE);
								boolean priseEnChargeRemise=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.REMISE);

							List<TaRemise> listeRemise = new LinkedList<TaRemise>();
							//récupérer tous les documents directement liés à cette facture
							for (TaRReglement rReglement : taFacture.getTaRReglements()) {
								List<TaReglement> liste=new LinkedList<TaReglement>();
								List<TaRemise> listeRemiseReglement =new LinkedList<TaRemise>();
								listeRemiseReglement=daoRemise.findSiReglementDansRemise(rReglement.getTaReglement().getCodeDocument());


								if(listeRemiseReglement.size()==0 && priseEnChargeReglementSimple){
									liste.add(rReglement.getTaReglement());
									numFin=exportReglementISAGRIFINAL(liste, listComplete, fw,  exportationEpicea.getReExportReglement(), numFin, gererPointages,
											taFacture.getIdDocument(),false,null);
								}else{
									for (TaRemise taRemise : listeRemiseReglement) {
										if(listeRemise.indexOf(taRemise)==-1 )
											listeRemise.add(taRemise);
									}
								}

							}
							/**** Atention Cette partie est rajoutée temporaire jusqu'à gestion des règlements multiples*/
							if (taFacture.getTaReglement()!=null) {
								List<TaReglement> liste=new LinkedList<TaReglement>();
								List<TaRemise> listeRemiseReglement =new LinkedList<TaRemise>();
								listeRemiseReglement=daoRemise.findSiReglementDansRemise(taFacture.getTaReglement().getCodeDocument());


								if(listeRemiseReglement.size()==0 && priseEnChargeReglementSimple){
									liste.add(taFacture.getTaReglement());
									numFin=exportReglementISAGRIFINAL(liste, listComplete, fw,  exportationEpicea.getReExportReglement(), numFin, gererPointages,
											taFacture.getIdDocument(),false,taFacture);
								}else{
									for (TaRemise taRemise : listeRemiseReglement) {
										if(listeRemise.indexOf(taRemise)==-1 )
											listeRemise.add(taRemise);
									}
								}

							}							
							
							if(priseEnChargeRemise){
								if(listeRemise.size()>0){
									numFin=exportRemiseISAGRIFINAL(listeRemise, listComplete, fw,  exportationEpicea.getReExportRemise(), numFin, gererPointages, 
											taFacture.getIdDocument(),false);
								}
							}
							}
//						else if(gererPointages){
////								for (TaRAcompte r : taFacture.getTaRAcomptes()) {
////									numFin=initPointage(taFacture, r.getTaAcompte(), r, false, idAppelant, numFin);
////								}
//								for (TaRAvoir r : taFacture.getTaRAvoirs()) {
//									numFin=initPointage(taFacture, r.getTaAvoir(), r, false, idAppelant, numFin);
//								}
//								for (TaRReglement r : taFacture.getTaRReglements()) {
//									numFin=initPointage(taFacture, r.getTaReglement(), r, false, idAppelant, numFin);
//								}
//								if(taFacture.getTaReglement()!=null) {
//									IRelationDoc r=new TaRReglement();
//									r.setAffectation(taFacture.getTaReglement().getNetTtcCalc());
//									numFin=initPointage(taFacture, taFacture.getTaReglement(), r, false, idAppelant, numFin);
//								}							
//							}
								
							listComplete.add(taFacture.getCodeDocument());
							exportationEpicea.listCompleteDoc.add(taFacture);
							exportationEpicea.listeVerif.add(taFacture);
						}
					}
				}

			} catch(IOException ioe){
				logger.error("",ioe);
				exportationEpicea.setRetour(false);
			} 

			return numFin;
		}	
	

	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportApporteurISAGRIFINAL(List<TaApporteur> idFactureAExporter,List<String> listComplete,Writer fw,
			int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesDocumenstLies) throws Exception {
		int numFin=numDepart;
		try {      


			TaApporteur TaApporteur = null;


			exportationEpicea.numeroDePiece = numFin;



			boolean nouvellePiece = false;


			for(int i=0; i<idFactureAExporter.size(); i++) {
				TaApporteur = idFactureAExporter.get(i);
				if(listComplete.indexOf(TaApporteur.getCodeDocument())==-1){

					/**/   // pour compenser les problèmes d'arrondis mis sur des lignes à 0
					TaApporteur.calculeTvaEtTotaux();
					/**/  //isa le 03 février 2011 en accord avec Nicolas

					if(TaApporteur.getDateExport()==null || reExport==1) { /* facture pas deja exportee ou reexportation */
						nouvellePiece = true;
						//					TaApporteur.calculeTvaEtTotaux();

						if(nouvellePiece) {
							exportationEpicea.numeroDePiece++;
							exportationEpicea.getListeLignesTva().clear();
							nouvellePiece = false;
							numFin++;
						}

						//recuperation des informations propre a la facture
						exportationEpicea.codePiece = TaApporteur.getCodeDocument();
						exportationEpicea.datePiece = TaApporteur.getDateDocument();
						exportationEpicea.journal = journalApporteur;
						exportationEpicea.libellePiece = TaApporteur.getLibelleDocument();
						exportationEpicea.libelleLigne = TaApporteur.getTaInfosDocument().getNomTiers();
						exportationEpicea.nomTiers = TaApporteur.getTaInfosDocument().getNomTiers();

						exportationEpicea.mtCreditLigne = LibConversion.BigDecimalToDouble(TaApporteur.getNetTtcCalc());
						exportationEpicea.numCptLigne = TaApporteur.getTaInfosDocument().getCompte()+TaApporteur.getTaInfosDocument().getCodeCompta();  
						
						exportationEpicea.numLignePiece = 1;

						exportationEpicea.mtDebitLigne = 0d;
						exportationEpicea.qte1 = 0d;
						exportationEpicea.codeTva ="";
						exportationEpicea.refContrepartie ="";

						//ecriture de la ligne d'entete dans le fichier d'export
						fw.write(exportationEpicea.creationLigneISAGRIFinal(
								exportationEpicea.journal,
								exportationEpicea.datePiece ,
								exportationEpicea.numCptLigne ,
								exportationEpicea.libelleLigne ,
								exportationEpicea.codePiece ,
								exportationEpicea.libellePiece ,
								exportationEpicea.mtDebitLigne ,
								exportationEpicea.mtCreditLigne ,
								exportationEpicea.refContrepartie ,
								exportationEpicea.codeTva,
								exportationEpicea.qte1));
						fw.write(exportationEpicea.finDeLigne);


						//traitement des lignes
						for (TaLApporteur lf : TaApporteur.getLignes()) {
							//récupération des valeurs propres à la ligne

							exportationEpicea.typePiece = exportationEpicea.TypeAchat;
							if(lf.getTaArticle()!=null && lf.getMtTtcLApresRemiseGlobaleDocument().signum()!=0){
								exportationEpicea.libelleLigne = lf.getLibLDocument();
								exportationEpicea.qte1 = LibConversion.BigDecimalToDouble(lf.getQteLDocument());
								exportationEpicea.numCptLigne = lf.getCompteLDocument();
								if(exportationEpicea.numCptLigne==null || exportationEpicea.numCptLigne.equals("")){
									//transaction.rollback();
									exportationEpicea.setRetour(false);
									exportationEpicea.setMessageRetour("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+TaApporteur.getCodeDocument());
									throw new Exception("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+TaApporteur.getCodeDocument());
								}
								//traitement du type de tva du tiers (F ou ue ou horsUE ou Franchise)
								exportationEpicea.codeTva=lf.getCodeTvaLDocument();
								if(exportationEpicea.codeTva==null)exportationEpicea.codeTva=""; /* corrigé par isa le 11/09/2014*/

								
								exportationEpicea.mtDebitLigne = LibConversion.BigDecimalToDouble(lf.getMtHtLApresRemiseGlobaleDocument());

								exportationEpicea.numLignePiece = exportationEpicea.numLignePiece + 1;
								exportationEpicea.mtCreditLigne = 0d;
								
								
								exportationEpicea.mtDebitTva = LibConversion.BigDecimalToDouble(lf.getMtTtcLApresRemiseGlobaleDocument().subtract(lf.getMtHtLApresRemiseGlobaleDocument()));

								if(exportationEpicea.codeTva!="") {
									rempliListeLigneTva(exportationEpicea.codeTva, lf.getMtTtcLApresRemiseGlobaleDocument().subtract(lf.getMtHtLApresRemiseGlobaleDocument()), exportationEpicea.mtDebitTva<0);
								}
								
								//ecriture de la ligne dans le fichier d'export


								fw.write(exportationEpicea.creationLigneISAGRIFinal(
										exportationEpicea.journal,
										exportationEpicea.datePiece ,
										exportationEpicea.numCptLigne ,
										"" ,
										exportationEpicea.codePiece ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.mtDebitLigne ,
										exportationEpicea.mtCreditLigne ,
										exportationEpicea.refContrepartie ,
										exportationEpicea.codeTva,
										exportationEpicea.qte1 ));
								fw.write(exportationEpicea.finDeLigne);
							}
						} 
						for (LigneTva l : exportationEpicea.getListeLignesTva()) {
							exportationEpicea.qte1 = 0d;
							exportationEpicea.mtDebitTva=LibConversion.BigDecimalToDouble(l.getMtTva());
							exportationEpicea.mtCreditTva=0d;
							exportationEpicea.numCptLigne=l.getNumcptTva();
							fw.write(exportationEpicea.creationLigneISAGRIFinal(
									exportationEpicea.journal,
									exportationEpicea.datePiece ,
									exportationEpicea.numCptLigne ,
									"" ,
									exportationEpicea.codePiece ,
									exportationEpicea.libellePiece ,
									exportationEpicea.mtDebitTva ,
									exportationEpicea.mtCreditTva ,
									"" ,
									"",
									exportationEpicea.qte1));
							fw.write(exportationEpicea.finDeLigne);
						}
						listComplete.add(TaApporteur.getCodeDocument());
						exportationEpicea.listCompleteDoc.add(TaApporteur);
						exportationEpicea.listeVerif.add(TaApporteur);
					}
				}
			}

		} catch(IOException ioe){
			logger.error("",ioe);
			exportationEpicea.setRetour(false);
		} 

		return numFin;
	}

	
	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportAvoirISAGRIFINAL(List<TaAvoir> idFactureAExporter,List<String> listComplete,Writer fw,
			int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesDocumenstLies) throws Exception {
		int numFin=numDepart;
		try {      


			Boolean gererLesPointages = gererPointages;

			TaAvoir taAvoir = null;


			exportationEpicea.numeroDePiece = numFin;



			boolean nouvellePiece = false;

			for(int i=0; i<idFactureAExporter.size(); i++) {
				taAvoir = idFactureAExporter.get(i);
				if(listComplete.indexOf(taAvoir.getCodeDocument())==-1){

					/**/   // pour compenser les problèmes d'arrondis mis sur des lignes à 0
					taAvoir.calculeTvaEtTotaux();
					/**/  //isa le 03 février 2011 en accord avec Nicolas

					if(taAvoir.getDateExport()==null || reExport==1) { /* facture pas deja exportee ou reexportation */
						nouvellePiece = true;

						if(nouvellePiece) {
							exportationEpicea.numeroDePiece++;
							exportationEpicea.getListeLignesTva().clear();
							nouvellePiece = false;
							numFin++;
						}

						//recuperation des informations propre a la facture
						exportationEpicea.codePiece = taAvoir.getCodeDocument();
						exportationEpicea.datePiece = taAvoir.getDateDocument();
						exportationEpicea.journal = journalAvoir;
						exportationEpicea.libellePiece = taAvoir.getLibelleDocument();
						exportationEpicea.libelleLigne = taAvoir.getTaInfosDocument().getNomTiers();
						exportationEpicea.nomTiers = taAvoir.getTaInfosDocument().getNomTiers();

						exportationEpicea.mtCreditLigne = LibConversion.BigDecimalToDouble(taAvoir.getNetTtcCalc());
						exportationEpicea.numCptLigne = taAvoir.getTaInfosDocument().getCompte()+taAvoir.getTaInfosDocument().getCodeCompta(); 
						
						exportationEpicea.numLignePiece = 1;

						exportationEpicea.mtDebitLigne = 0d;
						exportationEpicea.qte1 = 0d;
						exportationEpicea.codeTva ="";
						exportationEpicea.refContrepartie ="";
						
//						if(gererLesPointages){
//							//traitement des lignes pour les pointages
//							for (TaRAvoir lf : taAvoir.getTaRAvoirs()) {
//								//récupération des valeurs propres à la ligne
//								exportationEpicea.refContrepartie = taAvoir.getCodeDocument();
//							} 
//						}


						//ecriture de la ligne d'entete dans le fichier d'export
						fw.write(exportationEpicea.creationLigneISAGRIFinal(
								exportationEpicea.journal,
								exportationEpicea.datePiece ,
								exportationEpicea.numCptLigne ,
								exportationEpicea.libelleLigne ,
								exportationEpicea.codePiece ,
								exportationEpicea.libellePiece ,
								exportationEpicea.mtDebitLigne ,
								exportationEpicea.mtCreditLigne ,
								exportationEpicea.refContrepartie ,
								exportationEpicea.codeTva,
								exportationEpicea.qte1));
						fw.write(exportationEpicea.finDeLigne);



						//traitement des lignes
						for (TaLAvoir lf : taAvoir.getLignes()) {
							//récupération des valeurs propres à la ligne

							
							if(lf.getTaArticle()!=null && lf.getMtTtcLApresRemiseGlobaleDocument().signum()!=0){
								exportationEpicea.libelleLigne = lf.getLibLDocument();
								exportationEpicea.qte1 = LibConversion.BigDecimalToDouble(lf.getQteLDocument());
								exportationEpicea.numCptLigne = lf.getCompteLDocument();
								if(exportationEpicea.numCptLigne==null || exportationEpicea.numCptLigne.equals("")){
									//transaction.rollback();
									exportationEpicea.setRetour(false);
									exportationEpicea.setMessageRetour("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taAvoir.getCodeDocument());
									throw new Exception("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taAvoir.getCodeDocument());
								}
								//traitement du type de tva du tiers (F ou ue ou horsUE ou Franchise)
								exportationEpicea.codeTva=lf.getCodeTvaLDocument();
								if(exportationEpicea.codeTva==null)exportationEpicea.codeTva=""; /* corrigé par isa le 11/09/2014*/

								
								exportationEpicea.mtDebitLigne = LibConversion.BigDecimalToDouble(lf.getMtHtLApresRemiseGlobaleDocument());

								exportationEpicea.mtDebitTva = LibConversion.BigDecimalToDouble(lf.getMtTtcLApresRemiseGlobaleDocument().subtract(lf.getMtHtLApresRemiseGlobaleDocument()));

								if(exportationEpicea.codeTva!="") {
									rempliListeLigneTva(exportationEpicea.codeTva, lf.getMtTtcLApresRemiseGlobaleDocument().subtract(lf.getMtHtLApresRemiseGlobaleDocument()), exportationEpicea.mtDebitTva<0);
								}

								
								exportationEpicea.numLignePiece = exportationEpicea.numLignePiece + 1;
								exportationEpicea.mtCreditLigne = 0d;
								//ecriture de la ligne dans le fichier d'export


								fw.write(exportationEpicea.creationLigneISAGRIFinal(
										exportationEpicea.journal,
										exportationEpicea.datePiece ,
										exportationEpicea.numCptLigne ,
										"" ,
										exportationEpicea.codePiece ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.mtDebitLigne ,
										exportationEpicea.mtCreditLigne ,
										exportationEpicea.refContrepartie ,
										exportationEpicea.codeTva,
										exportationEpicea.qte1));
								fw.write(exportationEpicea.finDeLigne);
							}
						} 

						for (LigneTva l : exportationEpicea.getListeLignesTva()) {
							exportationEpicea.qte1 = 0d;
							exportationEpicea.mtDebitTva=LibConversion.BigDecimalToDouble(l.getMtTva());
							exportationEpicea.mtCreditTva=0d;
							exportationEpicea.numCptLigne=l.getNumcptTva();
							fw.write(exportationEpicea.creationLigneISAGRIFinal(
									exportationEpicea.journal,
									exportationEpicea.datePiece ,
									exportationEpicea.numCptLigne ,
									"" ,
									exportationEpicea.codePiece ,
									exportationEpicea.libellePiece ,
									exportationEpicea.mtDebitTva ,
									exportationEpicea.mtCreditTva ,
									"" ,
									"",
									exportationEpicea.qte1));
							fw.write(exportationEpicea.finDeLigne);
						}
						
						exportationEpicea.listCompleteDoc.add(taAvoir);
						listComplete.add(taAvoir.getCodeDocument());
						exportationEpicea.listeVerif.add(taAvoir);
					}
				}
			}
		} catch(IOException ioe){
			logger.error("",ioe);
			exportationEpicea.setRetour(false);
		} 
		return numFin;
	}

	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportRemiseISAGRIFINAL(List<TaRemise> idRemiseAExporter,List<String> listComplete,Writer fw,
			int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesDocumenstLies) throws Exception {
		int numFin=numDepart;
		try {      

			Boolean gererLesPointages = gererPointages;

			TaRemise taDoc = null;


			exportationEpicea.numeroDePiece = numFin;

			

			boolean nouvellePiece = false;


			for(int i=0; i<idRemiseAExporter.size(); i++) {
				taDoc = idRemiseAExporter.get(i);

				if(listComplete.indexOf(taDoc.getCodeDocument())==-1){

					if(taDoc.getDateExport()==null || reExport==1) { /* remise pas deja exportee ou reexportation */
						nouvellePiece = true;

						if(nouvellePiece) {
							exportationEpicea.numeroDePiece++;
							nouvellePiece = false;
							numFin++;
						}

						//recuperation des informations propre a la REMISE
						exportationEpicea.codePiece = taDoc.getCodeDocument();
						exportationEpicea.datePiece = taDoc.getDateDocument();
						exportationEpicea.journal = journalRemise;
						exportationEpicea.libellePiece = taDoc.getLibelleDocument();
						exportationEpicea.libelleLigne = taDoc.getLibelleDocument();



						exportationEpicea.qte1 = 0d;
						exportationEpicea.codeTva ="";
						
						if(taDoc.getTaCompteBanque()!=null)
							exportationEpicea.numCptLigne = taDoc.getTaCompteBanque().getCptcomptable();
						else
						if(taDoc.getTaTPaiement()!=null)
							exportationEpicea.numCptLigne = taDoc.getTaTPaiement().getCompte();//taDoc.getTaCompteBanque().getCptcomptable();


						exportationEpicea.mtCreditLigne = 0d;  

						exportationEpicea.mtDebitLigne = LibConversion.BigDecimalToDouble(taDoc.getNetTtcCalc());



						exportationEpicea.refContrepartie = "";


						//ecriture de la ligne d'entete dans le fichier d'export
						fw.write(exportationEpicea.creationLigneISAGRIFinal(
								exportationEpicea.journal,
								exportationEpicea.datePiece ,
								exportationEpicea.numCptLigne ,
								"" ,
								exportationEpicea.codePiece ,
								exportationEpicea.libelleLigne ,
								exportationEpicea.mtDebitLigne ,
								exportationEpicea.mtCreditLigne ,
								exportationEpicea.refContrepartie ,
								exportationEpicea.codeTva,
								exportationEpicea.qte1));
						fw.write(exportationEpicea.finDeLigne);


						//traitement pour exporter les relations avec les factures					
						//traitement des lignes
						for (TaLRemise lf : taDoc.getTaLRemises()) {
							//récupération des valeurs propres à la ligne
							if(lf.getTaReglement()!=null && listComplete.indexOf(lf.getTaReglement().getCodeDocument())==-1){

								if(lf.getTaReglement().getLibelleDocument()!=null)
									exportationEpicea.libelleLigne = lf.getTaReglement().getLibelleDocument().replace("\r","").replace("\n","");
								else
									exportationEpicea.libelleLigne = "";
								exportationEpicea.qte1 = 0d;

								exportationEpicea.numCptLigne = lf.getTaReglement().getTaTiers().getCompte()+lf.getTaReglement().getTaTiers().getCodeTiers();
								exportationEpicea.cptCollectif = lf.getTaReglement().getTaTiers().getCompte();
								exportationEpicea.libelleLigne = lf.getTaReglement().getTaTiers().getNomTiers();

								exportationEpicea.codeTva = "";


								exportationEpicea.mtDebitLigne = 0d;

								exportationEpicea.mtCreditLigne = LibConversion.BigDecimalToDouble(lf.getTaReglement().getNetTtcCalc());




								fw.write(exportationEpicea.creationLigneISAGRIFinal(
										exportationEpicea.journal,
										exportationEpicea.datePiece ,
										exportationEpicea.numCptLigne ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.codePiece ,
										exportationEpicea.libellePiece ,
										exportationEpicea.mtDebitLigne ,
										exportationEpicea.mtCreditLigne ,
										exportationEpicea.refContrepartie ,
										exportationEpicea.codeTva,
										exportationEpicea.qte1));
								fw.write(exportationEpicea.finDeLigne);

//								//traitment des pointages si on les gere
//								if(gererLesPointages){
//									exportationEpicea.typePiece = exportationEpicea.typePointage;	
//									for (TaRReglement Relation : lf.getTaReglement().getTaRReglements()) {
//										TaFacture fac =Relation.getTaFacture();
//										idAppelant=fac.getIdDocument();
//										numFin=initPointage(fac, lf.getTaReglement(), Relation, gererLesDocumenstLies, idAppelant, numFin);
//									}
//								}//fin si on gere les pointages
								exportationEpicea.listCompleteDoc.add(lf.getTaReglement());
								listComplete.add(lf.getTaReglement().getCodeDocument());
								exportationEpicea.listeVerif.add(lf.getTaReglement());
							}//fin on traite le reglement

					
							if(lf.getDateExport()==null)lf.setDateExport(new Date());
							
						} 

						exportationEpicea.listCompleteDoc.add(taDoc);
						listComplete.add(taDoc.getCodeDocument());
						exportationEpicea.listeVerif.add(taDoc);
					}
				}
			}
		} catch(IOException ioe){
			logger.error("",ioe);
			exportationEpicea.setRetour(false);
		} 

		return numFin;

	}


	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportReglementISAGRIFINAL(List<TaReglement> idReglementAExporter,List<String> listComplete,Writer fw,
			int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesDocumenstLies,TaFacture taFactureDirect) throws Exception {
		int numFin=numDepart;
		try {     


			Boolean gererLesPointages = gererPointages;

			TaReglement taDoc = null;

			exportationEpicea.numeroDePiece = numFin;

		

			boolean nouvellePiece = false;


			for(int i=0; i<idReglementAExporter.size(); i++) {
				taDoc = idReglementAExporter.get(i);
				if(listComplete.indexOf(taDoc.getCodeDocument())==-1 ){

					if(taDoc.getDateExport()==null || reExport==1) { /* facture pas deja exportee ou reexportation */
							if( daoRemise.findSiReglementDansRemise(taDoc.getCodeDocument()).size()==0){
							nouvellePiece = true;

							if(nouvellePiece) {
								exportationEpicea.numeroDePiece++;
								nouvellePiece = false;
								numFin++;
							}

							//recuperation des informations propre a la facture
							exportationEpicea.codePiece = taDoc.getCodeDocument();
							exportationEpicea.datePiece = taDoc.getDateDocument();
							exportationEpicea.journal = journalReglement;
							exportationEpicea.libellePiece = taDoc.getLibelleDocument();
							exportationEpicea.libelleLigne = taDoc.getLibelleDocument();

							exportationEpicea.mtDebitLigne = LibConversion.BigDecimalToDouble(taDoc.getNetTtcCalc());  

							exportationEpicea.numLignePiece = 1;
							exportationEpicea.mtCreditLigne = 0d;
							exportationEpicea.qte1 = 0d;
							exportationEpicea.codeTva ="";
							
							

							if(taDoc.getTaCompteBanque()!=null)
								exportationEpicea.numCptLigne = taDoc.getTaCompteBanque().getCptcomptable();
							if(taDoc.getTaTPaiement()!=null)
								exportationEpicea.numCptLigne = taDoc.getTaTPaiement().getCompte();
							
							//trouver le tiers lié au règlement
							TaTiers tiers=taDoc.getTaTiers();

					
					

							exportationEpicea.mtCreditLigne = 0d; 

							exportationEpicea.qte1 = 0d;

							exportationEpicea.codeTva ="";


							exportationEpicea.refContrepartie = "";
							
//							if(gererLesPointages){
//								//traitement des lignes pour les pointages
//								for (TaRReglement lf : taDoc.getTaRReglements()) {
//									//récupération des valeurs propres à la ligne
//									TaFacture fac =lf.getTaFacture();
//									numFin=initPointage(fac, taDoc, lf, gererLesDocumenstLies, idAppelant, numFin);
//								} 
//													
//							}


							//ecriture de la ligne d'entete dans le fichier d'export
							fw.write(exportationEpicea.creationLigneISAGRIFinal(
									exportationEpicea.journal,
									exportationEpicea.datePiece ,
									exportationEpicea.numCptLigne ,
									"" ,
									exportationEpicea.codePiece ,
									exportationEpicea.libelleLigne ,
									exportationEpicea.mtDebitLigne ,
									exportationEpicea.mtCreditLigne ,
									exportationEpicea.refContrepartie ,
									exportationEpicea.codeTva,
									exportationEpicea.qte1));
							fw.write(exportationEpicea.finDeLigne);
							
							

							//traitement de la ligne de contrepartie (tiers)
//							if(taDoc.getLibelleDocument()!=null)
//								exportationEpicea.libelleLigne = taDoc.getLibelleDocument().replace("\r","").replace("\n","");//libelle de la ligne de facture
//							else
//								exportationEpicea.libelleLigne = "";
							exportationEpicea.qte1 = 0d;
							exportationEpicea.numCptLigne = taDoc.getTaTiers().getCompte()+taDoc.getTaTiers().getCodeCompta();
							exportationEpicea.cptCollectif = taDoc.getTaTiers().getCompte();
							exportationEpicea.libelleLigne = taDoc.getTaTiers().getNomTiers();

							exportationEpicea.codeTva = "";

							exportationEpicea.mtDebitLigne = 0d;

							exportationEpicea.numLignePiece = exportationEpicea.numLignePiece + 1;
							exportationEpicea.mtCreditLigne = LibConversion.BigDecimalToDouble(taDoc.getNetTtcCalc());


							fw.write(exportationEpicea.creationLigneISAGRIFinal(
									exportationEpicea.journal,
									exportationEpicea.datePiece ,
									exportationEpicea.numCptLigne ,
									exportationEpicea.libelleLigne ,
									exportationEpicea.codePiece ,
									exportationEpicea.libellePiece ,
									exportationEpicea.mtDebitLigne ,
									exportationEpicea.mtCreditLigne ,
									exportationEpicea.refContrepartie ,
									exportationEpicea.codeTva,
									exportationEpicea.qte1));
							fw.write(exportationEpicea.finDeLigne);

//							if(gererLesPointages){
//								exportationEpicea.typePiece = exportationEpicea.typePointage;	
//								//traitement des lignes pour les pointages
//								for (TaRReglement lf : taDoc.getTaRReglements()) {
//									//récupération des valeurs propres à la ligne
//									TaFacture fac =lf.getTaFacture();
//									numFin=initPointage(fac, taDoc, lf, gererLesDocumenstLies, idAppelant, numFin);
//								} 
//								/**** Attention !!! rajouter temporairement pour les règlements unique dans les factures*/
//								if(taDoc.getTaFactures()!=null && !taDoc.getTaFactures().isEmpty()) {
//									for (TaFacture obj : taDoc.getTaFactures()) {
//										//récupération des valeurs propres à la ligne
//										IRelationDoc r=new TaRReglement();
//										r.setAffectation(obj.getNetTtcCalc());
//										numFin=initPointage(obj, taDoc, r, gererLesDocumenstLies, idAppelant, numFin);
//									}
//								} 
//								if(taFactureDirect!=null) {
//									//récupération des valeurs propres à la ligne
//									IRelationDoc r=new TaRReglement();
//									r.setAffectation(taFactureDirect.getNetTtcCalc());
//									numFin=initPointage(taFactureDirect, taDoc, r, gererLesDocumenstLies, idAppelant, numFin);
//								} 								
//							}
//							if(taDoc.getExport()!=1)taDoc.setExport(1);
//							taDoc=daoReglement.merge(taDoc);
							listComplete.add(taDoc.getCodeDocument());
							exportationEpicea.listCompleteDoc.add(taDoc);
							exportationEpicea.listeVerif.add(taDoc);
						}
					}
				}
			}

		} catch(IOException ioe){
			logger.error("",ioe);
			exportationEpicea.setRetour(false);
		} 

		return numFin;
	}


	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportAvoircentraliseeISAGRIFINAL(List<TaAvoir> idFactureAExporter,List<String> listComplete,Writer fw,
			int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesDocumenstLies) throws Exception {
		int numFin=numDepart;
		try {      


			Boolean gererLesPointages = gererPointages;

			TaAvoir taAvoir = null;


			exportationEpicea.numeroDePiece = numFin;



			boolean nouvellePiece = false;

			for(int i=0; i<idFactureAExporter.size(); i++) {
				taAvoir = idFactureAExporter.get(i);
				if(listComplete.indexOf(taAvoir.getCodeDocument())==-1){

					/**/   // pour compenser les problèmes d'arrondis mis sur des lignes à 0
					taAvoir.calculeTvaEtTotaux();
					/**/  //isa le 03 février 2011 en accord avec Nicolas

					if(taAvoir.getDateExport()==null || reExport==1) { /* facture pas deja exportee ou reexportation */
						nouvellePiece = true;

						if(nouvellePiece) {
							exportationEpicea.numeroDePiece++;
							nouvellePiece = false;
							numFin++;
						}

						//on crée un nouvel objet Avoir pour créer la centralisation
						TaAvoir newAvoir =new TaAvoir(false);
						newAvoir=(TaAvoir) taAvoir.cloneCentralisation();
						newAvoir.getLignes().clear();
						
						TaLAvoir newTaLAvoir;
						for (TaLAvoir obj : taAvoir.getLignes()) {
							newTaLAvoir=newAvoir.contientMemeParametreCompte(obj);
							if(newTaLAvoir!=null){
								newTaLAvoir.setQteLDocument(BigDecimal.ONE);
								if(taAvoir.getTtc()==1) {
									newTaLAvoir.setPrixULDocument(newTaLAvoir.getPrixULDocument().add(obj.getMtTtcLApresRemiseGlobaleDocument()));
								}else{
									newTaLAvoir.setPrixULDocument(newTaLAvoir.getPrixULDocument().add(obj.getMtHtLApresRemiseGlobaleDocument()));
								}
							}else{
								if(obj.getTaArticle()!=null){
								newTaLAvoir=new TaLAvoir(false);
								newTaLAvoir=obj.clone();
								newTaLAvoir.setLibLDocument(newAvoir.getLibelleDocument());
								newTaLAvoir.setQteLDocument(BigDecimal.ONE);
								newTaLAvoir.setRemTxLDocument(BigDecimal.ZERO);
								if(taAvoir.getTtc()==1) {
									newTaLAvoir.setPrixULDocument(obj.getMtTtcLApresRemiseGlobaleDocument());
								}else{
									newTaLAvoir.setPrixULDocument(obj.getMtHtLApresRemiseGlobaleDocument());
								}
								newTaLAvoir.setTaDocument(newAvoir);
								newAvoir.addLigne(newTaLAvoir);
								}
							}
						}
						newAvoir.setTxRemHtDocument(BigDecimal.ZERO);
						newAvoir.calculeTvaEtTotaux();
						
						
						//recuperation des informations propre a la facture
						exportationEpicea.codePiece = taAvoir.getCodeDocument();
						exportationEpicea.datePiece = taAvoir.getDateDocument();
						exportationEpicea.journal = journalAvoir;
						exportationEpicea.libellePiece = taAvoir.getLibelleDocument();
						exportationEpicea.libelleLigne = taAvoir.getTaInfosDocument().getNomTiers();
						exportationEpicea.nomTiers = taAvoir.getTaInfosDocument().getNomTiers();

						exportationEpicea.mtCreditLigne = LibConversion.BigDecimalToDouble(taAvoir.getNetTtcCalc());
						exportationEpicea.numCptLigne = taAvoir.getTaInfosDocument().getCompte()+taAvoir.getTaInfosDocument().getCodeCompta(); 
						
						exportationEpicea.numLignePiece = 1;

						exportationEpicea.mtDebitLigne = 0d;
						exportationEpicea.qte1 = 0d;
						exportationEpicea.codeTva ="";
						exportationEpicea.refContrepartie ="";
						
//						if(gererLesPointages){
//							//traitement des lignes pour les pointages
//							for (TaRAvoir lf : taAvoir.getTaRAvoirs()) {
//								//récupération des valeurs propres à la ligne
//								exportationEpicea.refContrepartie = taAvoir.getCodeDocument();
//							} 
//						}


						//ecriture de la ligne d'entete dans le fichier d'export
						fw.write(exportationEpicea.creationLigneISAGRIFinal(
								exportationEpicea.journal,
								exportationEpicea.datePiece ,
								exportationEpicea.numCptLigne ,
								exportationEpicea.libelleLigne ,
								exportationEpicea.codePiece ,
								exportationEpicea.libellePiece ,
								exportationEpicea.mtDebitLigne ,
								exportationEpicea.mtCreditLigne ,
								exportationEpicea.refContrepartie ,
								exportationEpicea.codeTva,
								exportationEpicea.qte1));
						fw.write(exportationEpicea.finDeLigne);



						//traitement des lignes
						for (TaLAvoir lf : taAvoir.getLignes()) {
							//récupération des valeurs propres à la ligne

							
							if(lf.getTaArticle()!=null && lf.getMtTtcLApresRemiseGlobaleDocument().signum()!=0){
								exportationEpicea.libelleLigne = lf.getLibLDocument();
								exportationEpicea.qte1 = LibConversion.BigDecimalToDouble(lf.getQteLDocument());
								exportationEpicea.numCptLigne = lf.getCompteLDocument();
								if(exportationEpicea.numCptLigne==null || exportationEpicea.numCptLigne.equals("")){
									//transaction.rollback();
									exportationEpicea.setRetour(false);
									exportationEpicea.setMessageRetour("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taAvoir.getCodeDocument());
									throw new Exception("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taAvoir.getCodeDocument());
								}
								//traitement du type de tva du tiers (F ou ue ou horsUE ou Franchise)
								exportationEpicea.codeTva=lf.getCodeTvaLDocument();
								if(exportationEpicea.codeTva==null)exportationEpicea.codeTva=""; /* corrigé par isa le 11/09/2014*/

								
								exportationEpicea.mtDebitLigne = LibConversion.BigDecimalToDouble(lf.getMtHtLApresRemiseGlobaleDocument());

								exportationEpicea.numLignePiece = exportationEpicea.numLignePiece + 1;
								exportationEpicea.mtCreditLigne = 0d;
								//ecriture de la ligne dans le fichier d'export
								exportationEpicea.mtDebitTva = LibConversion.BigDecimalToDouble(lf.getMtTtcLApresRemiseGlobaleDocument().subtract(lf.getMtHtLApresRemiseGlobaleDocument()));

								if(exportationEpicea.codeTva!="") {
									rempliListeLigneTva(exportationEpicea.codeTva, lf.getMtTtcLApresRemiseGlobaleDocument().subtract(lf.getMtHtLApresRemiseGlobaleDocument()), exportationEpicea.mtDebitTva<0);
								}


								fw.write(exportationEpicea.creationLigneISAGRIFinal(
										exportationEpicea.journal,
										exportationEpicea.datePiece ,
										exportationEpicea.numCptLigne ,
										"" ,
										exportationEpicea.codePiece ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.mtDebitLigne ,
										exportationEpicea.mtCreditLigne ,
										exportationEpicea.refContrepartie ,
										exportationEpicea.codeTva,
										exportationEpicea.qte1));
								fw.write(exportationEpicea.finDeLigne);
							}
						} 

						for (LigneTva l : exportationEpicea.getListeLignesTva()) {
							exportationEpicea.qte1 = 0d;
							exportationEpicea.mtDebitTva=LibConversion.BigDecimalToDouble(l.getMtTva());
							exportationEpicea.mtCreditTva=0d;
							exportationEpicea.numCptLigne=l.getNumcptTva();
							fw.write(exportationEpicea.creationLigneISAGRIFinal(
									exportationEpicea.journal,
									exportationEpicea.datePiece ,
									exportationEpicea.numCptLigne ,
									"" ,
									exportationEpicea.codePiece ,
									exportationEpicea.libellePiece ,
									exportationEpicea.mtDebitTva ,
									exportationEpicea.mtCreditTva ,
									"" ,
									"",
									exportationEpicea.qte1));
							fw.write(exportationEpicea.finDeLigne);
						}
						exportationEpicea.listCompleteDoc.add(taAvoir);
						listComplete.add(taAvoir.getCodeDocument());
						exportationEpicea.listeVerif.add(taAvoir);
					}
				}
			}
		} catch(IOException ioe){
			logger.error("",ioe);
			exportationEpicea.setRetour(false);
		} 
		return numFin;
	}


	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportApporteurCentraliseeISAGRIFINAL(List<TaApporteur> idFactureAExporter,List<String> listComplete,Writer fw,
			int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesDocumenstLies) throws Exception {
		int numFin=numDepart;
		try {      


			TaApporteur TaApporteur = null;


			exportationEpicea.numeroDePiece = numFin;



			boolean nouvellePiece = false;


			for(int i=0; i<idFactureAExporter.size(); i++) {
				TaApporteur = idFactureAExporter.get(i);
				if(listComplete.indexOf(TaApporteur.getCodeDocument())==-1){

					/**/   // pour compenser les problèmes d'arrondis mis sur des lignes à 0
					TaApporteur.calculeTvaEtTotaux();
					/**/  //isa le 03 février 2011 en accord avec Nicolas

					if(TaApporteur.getDateExport()==null || reExport==1) { /* facture pas deja exportee ou reexportation */
						nouvellePiece = true;
						//					TaApporteur.calculeTvaEtTotaux();

						if(nouvellePiece) {
							exportationEpicea.numeroDePiece++;
							nouvellePiece = false;
							numFin++;
						}

						//on crée un nouvel objet Apporteur pour créer la centralisation
						TaApporteur newApporteur =new TaApporteur(false);
						newApporteur=(TaApporteur) TaApporteur.cloneCentralisation();
						newApporteur.getLignes().clear();
						
						TaLApporteur newTaLApporteur;
						for (TaLApporteur obj : TaApporteur.getLignes()) {
							newTaLApporteur=newApporteur.contientMemeParametreCompte(obj);
							if(newTaLApporteur!=null){
								newTaLApporteur.setQteLDocument(BigDecimal.ONE);
								if(TaApporteur.getTtc()==1) {
									newTaLApporteur.setPrixULDocument(newTaLApporteur.getPrixULDocument().add(obj.getMtTtcLApresRemiseGlobaleDocument()));
								}else{
									newTaLApporteur.setPrixULDocument(newTaLApporteur.getPrixULDocument().add(obj.getMtHtLApresRemiseGlobaleDocument()));
								}
							}else{
								if(obj.getTaArticle()!=null){
								newTaLApporteur=new TaLApporteur(false);
								newTaLApporteur=obj.clone();
								newTaLApporteur.setLibLDocument(newApporteur.getLibelleDocument());
								newTaLApporteur.setQteLDocument(BigDecimal.ONE);
								newTaLApporteur.setRemTxLDocument(BigDecimal.ZERO);
								if(TaApporteur.getTtc()==1) {
									newTaLApporteur.setPrixULDocument(obj.getMtTtcLApresRemiseGlobaleDocument());
								}else{
									newTaLApporteur.setPrixULDocument(obj.getMtHtLApresRemiseGlobaleDocument());
								}
								newTaLApporteur.setTaDocument(newApporteur);
								newApporteur.addLigne(newTaLApporteur);
								}
							}
						}
						newApporteur.setTxRemHtDocument(BigDecimal.ZERO);
						newApporteur.calculeTvaEtTotaux();
						
						//recuperation des informations propre a la facture
						exportationEpicea.codePiece = TaApporteur.getCodeDocument();
						exportationEpicea.datePiece = TaApporteur.getDateDocument();
						exportationEpicea.journal = journalApporteur;
						exportationEpicea.libellePiece = TaApporteur.getLibelleDocument();
						exportationEpicea.libelleLigne = TaApporteur.getTaInfosDocument().getNomTiers();
						exportationEpicea.nomTiers = TaApporteur.getTaInfosDocument().getNomTiers();

						exportationEpicea.mtCreditLigne = LibConversion.BigDecimalToDouble(TaApporteur.getNetTtcCalc());
						exportationEpicea.numCptLigne = TaApporteur.getTaInfosDocument().getCompte()+TaApporteur.getTaInfosDocument().getCodeCompta(); 
						
						exportationEpicea.numLignePiece = 1;

						exportationEpicea.mtDebitLigne = 0d;
						exportationEpicea.qte1 = 0d;
						exportationEpicea.codeTva ="";
						exportationEpicea.refContrepartie ="";

						//ecriture de la ligne d'entete dans le fichier d'export
						fw.write(exportationEpicea.creationLigneISAGRIFinal(
								exportationEpicea.journal,
								exportationEpicea.datePiece ,
								exportationEpicea.numCptLigne ,
								exportationEpicea.libelleLigne ,
								exportationEpicea.codePiece ,
								exportationEpicea.libellePiece ,
								exportationEpicea.mtDebitLigne ,
								exportationEpicea.mtCreditLigne ,
								exportationEpicea.refContrepartie ,
								exportationEpicea.codeTva,
								exportationEpicea.qte1));
						fw.write(exportationEpicea.finDeLigne);


						//traitement des lignes
						for (TaLApporteur lf : TaApporteur.getLignes()) {
							//récupération des valeurs propres à la ligne

							exportationEpicea.typePiece = exportationEpicea.TypeAchat;
							if(lf.getTaArticle()!=null && lf.getMtTtcLApresRemiseGlobaleDocument().signum()!=0){
								exportationEpicea.libelleLigne = lf.getLibLDocument();
								exportationEpicea.qte1 = LibConversion.BigDecimalToDouble(lf.getQteLDocument());
								exportationEpicea.numCptLigne = lf.getCompteLDocument();
								if(exportationEpicea.numCptLigne==null || exportationEpicea.numCptLigne.equals("")){
									//transaction.rollback();
									exportationEpicea.setRetour(false);
									exportationEpicea.setMessageRetour("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+TaApporteur.getCodeDocument());
									throw new Exception("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+TaApporteur.getCodeDocument());
								}
								//traitement du type de tva du tiers (F ou ue ou horsUE ou Franchise)
								exportationEpicea.codeTva=lf.getCodeTvaLDocument();
								if(exportationEpicea.codeTva==null)exportationEpicea.codeTva=""; /* corrigé par isa le 11/09/2014*/

								
								exportationEpicea.mtDebitLigne = LibConversion.BigDecimalToDouble(lf.getMtHtLApresRemiseGlobaleDocument());

								exportationEpicea.numLignePiece = exportationEpicea.numLignePiece + 1;
								exportationEpicea.mtCreditLigne = 0d;
								//ecriture de la ligne dans le fichier d'export
								exportationEpicea.mtDebitTva = LibConversion.BigDecimalToDouble(lf.getMtTtcLApresRemiseGlobaleDocument().subtract(lf.getMtHtLApresRemiseGlobaleDocument()));

								if(exportationEpicea.codeTva!="") {
									rempliListeLigneTva(exportationEpicea.codeTva, lf.getMtTtcLApresRemiseGlobaleDocument().subtract(lf.getMtHtLApresRemiseGlobaleDocument()), exportationEpicea.mtDebitTva<0);
								}

								fw.write(exportationEpicea.creationLigneISAGRIFinal(
										exportationEpicea.journal,
										exportationEpicea.datePiece ,
										exportationEpicea.numCptLigne ,
										"" ,
										exportationEpicea.codePiece ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.mtDebitLigne ,
										exportationEpicea.mtCreditLigne ,
										exportationEpicea.refContrepartie ,
										exportationEpicea.codeTva,
										exportationEpicea.qte1));
								fw.write(exportationEpicea.finDeLigne);
							}
						} 
						
						for (LigneTva l : exportationEpicea.getListeLignesTva()) {
							exportationEpicea.qte1 = 0d;
							exportationEpicea.mtDebitTva=LibConversion.BigDecimalToDouble(l.getMtTva());
							exportationEpicea.mtCreditTva=0d;
							exportationEpicea.numCptLigne=l.getNumcptTva();
							fw.write(exportationEpicea.creationLigneISAGRIFinal(
									exportationEpicea.journal,
									exportationEpicea.datePiece ,
									exportationEpicea.numCptLigne ,
									"" ,
									exportationEpicea.codePiece ,
									exportationEpicea.libellePiece ,
									exportationEpicea.mtDebitTva ,
									exportationEpicea.mtCreditTva ,
									"" ,
									"",
									exportationEpicea.qte1));
							fw.write(exportationEpicea.finDeLigne);
						}

						listComplete.add(TaApporteur.getCodeDocument());
						exportationEpicea.listCompleteDoc.add(TaApporteur);
						exportationEpicea.listeVerif.add(TaApporteur);
					}
				}
			}

		} catch(IOException ioe){
			logger.error("",ioe);
			exportationEpicea.setRetour(false);
		} 

		return numFin;
	}

	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public int exportFactureCentraliseeISAGRIFINAL(List<TaFacture> idFactureAExporter,List<String> listComplete,Writer fw,
			 int reExport,int numDepart,boolean gererPointages,Integer idAppelant,boolean gererLesReglements) throws Exception {
		int numFin=numDepart;
		try { 
			TaFacture taFacture = null;

			exportationEpicea.numeroDePiece = numFin;

			boolean nouvellePiece = false;

			for(int i=0; i<idFactureAExporter.size(); i++) {
				taFacture = idFactureAExporter.get(i);
				if(listComplete.indexOf(taFacture.getCodeDocument())==-1){

					/**/   // pour compenser les problèmes d'arrondis mis sur des lignes à 0
					taFacture.calculeTvaEtTotaux();
					/**/  //isa le 03 février 2011 en accord avec Nicolas


					if(taFacture.getDateExport()==null || reExport==1) { /* facture pas deja exportee ou reexportation */
						nouvellePiece = true;

						if(nouvellePiece) {
							exportationEpicea.numeroDePiece++;
							nouvellePiece = false;
							numFin++;
						}

						//on crée un nouvel objet facture pour créer la centralisation
						TaFacture newFacture =new TaFacture(false);
						newFacture=(TaFacture) taFacture.cloneCentralisation();
						newFacture.getLignes().clear();
						
						TaLFacture newTaLFacture;
						for (TaLFacture obj : taFacture.getLignes()) {
							newTaLFacture=newFacture.contientMemeParametreCompte(obj);
							if(newTaLFacture!=null){
								newTaLFacture.setQteLDocument(BigDecimal.ONE);
								if(taFacture.getTtc()==1) {
									newTaLFacture.setPrixULDocument(newTaLFacture.getPrixULDocument().add(obj.getMtTtcLApresRemiseGlobaleDocument()));
								}else{
									newTaLFacture.setPrixULDocument(newTaLFacture.getPrixULDocument().add(obj.getMtHtLApresRemiseGlobaleDocument()));
								}
							}else{
								if(obj.getTaArticle()!=null){
								newTaLFacture=new TaLFacture(false);
								newTaLFacture=obj.clone();
								newTaLFacture.setLibLDocument(newFacture.getLibelleDocument());
								newTaLFacture.setQteLDocument(BigDecimal.ONE);
								newTaLFacture.setRemTxLDocument(BigDecimal.ZERO);
								if(taFacture.getTtc()==1) {
									newTaLFacture.setPrixULDocument(obj.getMtTtcLApresRemiseGlobaleDocument());
								}else{
									newTaLFacture.setPrixULDocument(obj.getMtHtLApresRemiseGlobaleDocument());
								}
								newTaLFacture.setTaDocument(newFacture);
								newFacture.addLigne(newTaLFacture);
								}
							}
						}
						newFacture.setTxRemHtDocument(BigDecimal.ZERO);
						newFacture.calculeTvaEtTotaux();
						

						
						//recuperation des informations propre a la facture
						exportationEpicea.codePiece = taFacture.getCodeDocument();
						exportationEpicea.datePiece = taFacture.getDateDocument();
						exportationEpicea.journal = journalFacture;
						exportationEpicea.libellePiece = taFacture.getLibelleDocument();
						exportationEpicea.libelleLigne = taFacture.getTaInfosDocument().getNomTiers();
						exportationEpicea.nomTiers = taFacture.getTaInfosDocument().getNomTiers();

						exportationEpicea.mtDebitLigne = LibConversion.BigDecimalToDouble(taFacture.getNetTtcCalc());  
						exportationEpicea.numCptLigne = taFacture.getTaInfosDocument().getCompte()+taFacture.getTaInfosDocument().getCodeCompta(); 
						exportationEpicea.numLignePiece = 1;
						exportationEpicea.mtCreditLigne = 0d;
						exportationEpicea.qte1 = 0d;
						exportationEpicea.codeTva ="";
						
						exportationEpicea.refContrepartie="";
//						if(gererPointages){
//							if(taFacture.getTaRReglements()!=null && taFacture.getTaRReglements().size()>0)
//								exportationEpicea.refContrepartie=exportationEpicea.codePiece;
//						}
							
						//ecriture de la ligne d'entete dans le fichier d'export
						fw.write(exportationEpicea.creationLigneISAGRIFinal(
								exportationEpicea.journal,
								exportationEpicea.datePiece ,
								exportationEpicea.numCptLigne ,
								exportationEpicea.libelleLigne ,
								exportationEpicea.codePiece ,
								exportationEpicea.libellePiece ,
								exportationEpicea.mtDebitLigne ,
								exportationEpicea.mtCreditLigne ,
								exportationEpicea.refContrepartie ,
								exportationEpicea.codeTva,
								exportationEpicea.qte1));
						fw.write(exportationEpicea.finDeLigne);
						

						BigDecimal escompte = taFacture.getRemTtcDocument();
						if (escompte.signum() > 0) { 
							//création d'une lignes d'escompte
							exportationEpicea.numCptLigne = exportationEpicea.ctpEscompteDebit;
							exportationEpicea.libelleLigne = "Escompte "+taFacture.getLibelleDocument();
							exportationEpicea.mtDebitLigne = LibConversion.BigDecimalToDouble(escompte);
							exportationEpicea.mtCreditLigne = 0d;
							exportationEpicea.qte1 = 0d;
							exportationEpicea.codeTva = "";

							//ecriture de la ligne d'escompte dans le fichier d'export


							fw.write(exportationEpicea.creationLigneISAGRIFinal(
									exportationEpicea.journal,
									exportationEpicea.datePiece ,
									exportationEpicea.numCptLigne ,
									exportationEpicea.libelleLigne ,
									exportationEpicea.codePiece ,
									exportationEpicea.libellePiece ,
									exportationEpicea.mtDebitLigne ,
									exportationEpicea.mtCreditLigne ,
									exportationEpicea.refContrepartie ,
									exportationEpicea.codeTva,
									exportationEpicea.qte1));
							fw.write(exportationEpicea.finDeLigne);
						}

						//traitement des lignes
						for (TaLFacture lf : taFacture.getLignes()) {
							//récupération des valeurs propres à la ligne                 
							if(lf.getTaArticle()!=null   && lf.getMtTtcLApresRemiseGlobaleDocument()!=null ){
								exportationEpicea.libelleLigne = lf.getLibLDocument();
								exportationEpicea.qte1 = LibConversion.BigDecimalToDouble(lf.getQteLDocument());
								exportationEpicea.numCptLigne = lf.getCompteLDocument();
								if(exportationEpicea.numCptLigne==null || exportationEpicea.numCptLigne.equals("")){
									exportationEpicea.setRetour(false);
									exportationEpicea.setMessageRetour("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taFacture.getCodeDocument());
									throw new Exception("\nErreur N° 1\n"
											+"Article (code article : "+lf.getTaArticle().getCodeArticle()+") sans compte dans le document "+taFacture.getCodeDocument());
								}
								
								//traitement du type de tva du tiers (F ou ue ou horsUE ou Franchise)

								exportationEpicea.codeTva=lf.getCodeTvaLDocument();
									
								if(exportationEpicea.codeTva==null)exportationEpicea.codeTva="";

//								exportationEpicea.tauxTva = LibConversion.BigDecimalToDouble(lf.getTauxTvaLDocument());
								exportationEpicea.mtCreditLigne = LibConversion.BigDecimalToDouble(lf.getMtHtLApresRemiseGlobaleDocument());
								exportationEpicea.mtCreditTva = LibConversion.BigDecimalToDouble(lf.getMtTtcLApresRemiseGlobaleDocument().subtract(lf.getMtHtLApresRemiseGlobaleDocument()));

								if(exportationEpicea.codeTva!="") {
									rempliListeLigneTva(exportationEpicea.codeTva, lf.getMtTtcLApresRemiseGlobaleDocument().subtract(lf.getMtHtLApresRemiseGlobaleDocument()), exportationEpicea.mtCreditTva<0);
								}
								exportationEpicea.mtDebitLigne = 0d;


								fw.write(exportationEpicea.creationLigneISAGRIFinal(
										exportationEpicea.journal,
										exportationEpicea.datePiece ,
										exportationEpicea.numCptLigne ,
										"" ,
										exportationEpicea.codePiece ,
										exportationEpicea.libelleLigne ,
										exportationEpicea.mtDebitLigne ,
										exportationEpicea.mtCreditLigne ,
										exportationEpicea.refContrepartie ,
										exportationEpicea.codeTva,
										exportationEpicea.qte1));
								fw.write(exportationEpicea.finDeLigne);
							}
						}
						
						for (LigneTva l : exportationEpicea.getListeLignesTva()) {
							exportationEpicea.qte1 = 0d;
							exportationEpicea.mtDebitTva=LibConversion.BigDecimalToDouble(l.getMtTva());
							exportationEpicea.mtCreditTva=0d;
							exportationEpicea.numCptLigne=l.getNumcptTva();
							fw.write(exportationEpicea.creationLigneISAGRIFinal(
									exportationEpicea.journal,
									exportationEpicea.datePiece ,
									exportationEpicea.numCptLigne ,
									"" ,
									exportationEpicea.codePiece ,
									exportationEpicea.libellePiece ,
									exportationEpicea.mtDebitTva ,
									exportationEpicea.mtCreditTva ,
									"" ,
									"",
									exportationEpicea.qte1));
							fw.write(exportationEpicea.finDeLigne);
						}
						

						if(gererLesReglements){
								boolean priseEnChargeAcomptes=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.ACOMPTES);
								boolean priseEnChargeReglementSimple=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.REGLEMENT_SIMPLE);
								boolean priseEnChargeRemise=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.REMISE);

							List<TaRemise> listeRemise = new LinkedList<TaRemise>();
							//récupérer tous les documents directement liés à cette facture
							for (TaRReglement rReglement : taFacture.getTaRReglements()) {
								List<TaReglement> liste=new LinkedList<TaReglement>();
								List<TaRemise> listeRemiseReglement =new LinkedList<TaRemise>();
								listeRemiseReglement=daoRemise.findSiReglementDansRemise(rReglement.getTaReglement().getCodeDocument());


								if(listeRemiseReglement.size()==0 && priseEnChargeReglementSimple){
									liste.add(rReglement.getTaReglement());
									numFin=exportReglementISAGRIFINAL(liste, listComplete, fw,  exportationEpicea.getReExportReglement(), numFin, gererPointages,
											taFacture.getIdDocument(),false,null);
								}else{
									for (TaRemise taRemise : listeRemiseReglement) {
										if(listeRemise.indexOf(taRemise)==-1 )
											listeRemise.add(taRemise);
									}
								}

							}
							/**** Atention Cette partie est rajoutée temporaire jusqu'à gestion des règlements multiples*/
							if (taFacture.getTaReglement()!=null) {
								List<TaReglement> liste=new LinkedList<TaReglement>();
								List<TaRemise> listeRemiseReglement =new LinkedList<TaRemise>();
								listeRemiseReglement=daoRemise.findSiReglementDansRemise(taFacture.getTaReglement().getCodeDocument());


								if(listeRemiseReglement.size()==0 && priseEnChargeReglementSimple){
									liste.add(taFacture.getTaReglement());
									numFin=exportReglementISAGRIFINAL(liste, listComplete, fw,  exportationEpicea.getReExportReglement(), numFin, gererPointages,
											taFacture.getIdDocument(),false,taFacture);
								}else{
									for (TaRemise taRemise : listeRemiseReglement) {
										if(listeRemise.indexOf(taRemise)==-1 )
											listeRemise.add(taRemise);
									}
								}

							}							
							
							if(priseEnChargeRemise){
								if(listeRemise.size()>0){
									numFin=exportRemiseISAGRIFINAL(listeRemise, listComplete, fw,  exportationEpicea.getReExportRemise(), numFin, gererPointages, 
											taFacture.getIdDocument(),false);
								}
							}
							}
//						else if(gererPointages){
////								for (TaRAcompte r : taFacture.getTaRAcomptes()) {
////									numFin=initPointage(taFacture, r.getTaAcompte(), r, false, idAppelant, numFin);
////								}
//								for (TaRAvoir r : taFacture.getTaRAvoirs()) {
//									numFin=initPointage(taFacture, r.getTaAvoir(), r, false, idAppelant, numFin);
//								}
//								for (TaRReglement r : taFacture.getTaRReglements()) {
//									numFin=initPointage(taFacture, r.getTaReglement(), r, false, idAppelant, numFin);
//								}
//								if(taFacture.getTaReglement()!=null) {
//									IRelationDoc r=new TaRReglement();
//									r.setAffectation(taFacture.getTaReglement().getNetTtcCalc());
//									numFin=initPointage(taFacture, taFacture.getTaReglement(), r, false, idAppelant, numFin);
//								}							
//							}
								
							listComplete.add(taFacture.getCodeDocument());
							exportationEpicea.listCompleteDoc.add(taFacture);
							exportationEpicea.listeVerif.add(taFacture);
						}
					}
				}

			} catch(IOException ioe){
				logger.error("",ioe);
				exportationEpicea.setRetour(false);
			} 

			return numFin;
		}	

	
	public void rempliListeLigneTva(String codeTva, BigDecimal montantTva,boolean debit) {
		boolean trouve=false;
		for (LigneTva l : exportationEpicea.getListeLignesTva()) {
			if(l.getCodeTva().equals(codeTva)) {
				trouve=true;
//				if(debit)
//					l.setMtTva(l.getMtTva().subtract(montantTva));
//				else
					l.setMtTva(l.getMtTva().add(montantTva));
			}
		}

		if(!trouve) {//s'il n'existe pas dans la liste, il faut le créer
			LigneTva ligne = new LigneTva();
			ligne.setCodeTva(codeTva);
			TaTva tva =daoTvaDoc.findByCode(codeTva);
			ligne.setNumcptTva(tva.getNumcptTva());
//			if(debit)ligne.setMtTva(BigDecimal.ZERO.subtract(montantTva));
//			else 
				ligne.setMtTva(montantTva);
			exportationEpicea.getListeLignesTva().add(ligne);
		}
	}

}
