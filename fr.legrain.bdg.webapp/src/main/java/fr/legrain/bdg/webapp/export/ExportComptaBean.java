package fr.legrain.bdg.webapp.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import fr.legrain.bdg.documents.service.remote.ITaApporteurServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAvoirServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaReglementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaRemiseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.exportation.service.remote.IExportationServiceRemote;
import fr.legrain.bdg.importation.service.remote.IImportationServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.EmailParam;
import fr.legrain.bdg.webapp.app.EmailPieceJointeParam;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.TaApporteurDTO;
import fr.legrain.document.dto.TaAvoirDTO;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.dto.TaReglementDTO;
import fr.legrain.document.dto.TaRemiseDTO;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TaRemise;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.email.model.TaMessageEmail;
import fr.legrain.exportation.model.ExportationEpicea;
import fr.legrain.exportation.model.OptionExportation;
import fr.legrain.importation.model.Importation;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;
import fr.legrain.tiers.model.TaEmail;

@Named
@ViewScoped 
public class ExportComptaBean extends AbstractController{
	
	
	@Inject @Named(value="etatExportationDocumentBean")
	private EtatExportationDocumentBean etatExportationDocumentBean;
	
	private String typeClientExport=ExportationEpicea.CLIENT_EPICEA;
	private String tabCourant;
	private String tabOld;
	private StreamedContent file;
	private StreamedContent fileImportAgrigest;
	public static final String ID_TAB_TOUS ="idTabTous";
	public static final String ID_TAB_PAR_REF ="idTabParRef";
	public static final String ID_TAB_PAR_DATE ="idTabParDate";
	public static final String ID_TAB_LISTE ="idEtatExportationDocument";
	
	//option d'exportation
	private boolean exporterTousLesRegelements; 
//	public static final String C_ACOMPTE = ITaPreferencesServiceRemote.ACOMPTES;
//	public static final String C_SIMPLE = ITaPreferencesServiceRemote.REGLEMENT_SIMPLE;
//	public static final String C_REMISE = ITaPreferencesServiceRemote.REMISE;
//	public static final String C_APPORTEUR = ITaPreferencesServiceRemote.APPORTEUR;
//	public static final String C_CENTRALISER_ECRITURES = ITaPreferencesServiceRemote.CENTRALISER_ECRITURES;
//	public static final String C_DOCUMENTS_LIES = ITaPreferencesServiceRemote.DOCUMENTS_LIES;
//	public static final String C_REGLEMENTS_LIES = ITaPreferencesServiceRemote.REGLEMENTS_LIES;
//	public static final String C_POINTAGES = ITaPreferencesServiceRemote.POINTAGES;
	
	
//	private String libAcompte = ITaPreferencesServiceRemote.ACOMPTES;
	private String libSimpleReglement = ITaPreferencesServiceRemote.REGLEMENT_SIMPLE;
	private String libRemise = ITaPreferencesServiceRemote.REMISE;
	private String libApporteur = ITaPreferencesServiceRemote.APPORTEUR;
	private String libCentraliserEcritures = ITaPreferencesServiceRemote.CENTRALISER_ECRITURES;
	private String libDocumentsLies = ITaPreferencesServiceRemote.DOCUMENTS_LIES;
	private String libReglementsLies = ITaPreferencesServiceRemote.REGLEMENTS_LIES;
	private String libPointages = ITaPreferencesServiceRemote.POINTAGES;
	
	private String typeFacture =TaFacture.TYPE_DOC;
	private String typeAvoir =TaAvoir.TYPE_DOC;
//	private String typeAcompte =TaAcompte.TYPE_DOC;
	private String typeApporteur =TaApporteur.TYPE_DOC;
	private String typeReglement =TaReglement.TYPE_DOC;
	private String typeRemise =TaRemise.TYPE_DOC;
	
	private List<SelectItem> reglements;
    private String[] selectedReglement;
	 
	//général
	private boolean exporterReglementLies;
	private boolean exporterDocumentLies;
	private boolean exporterReglementTout;
	private boolean exporterAvecPointage;
	private boolean centraliserEcriture;
	private boolean exporterApporteur;
	private boolean ecraserFichier=true;
	//par date ou par référence

	
	private boolean exporterFacture;
	private boolean exporterFactureAvoir;
	private boolean exporterFactureApporteur;
//	private boolean exporterAcompte;
//	private boolean exporterReglement;
	private boolean exporterReglementSimple;
	private boolean exporterRemise;
	
	private boolean reexporterFacture;
	private boolean reexporterFactureAvoir;
	private boolean reexporterFactureApporteur;
//	private boolean reexporterAcompte;
	private boolean reexporterReglement;
	private boolean reexporterRemise;
	private boolean toutReexporter;
	

	
	
	private boolean rempliVentes;
	private boolean rempliAvoirs;
//	private boolean rempliAcomptes;
	private boolean rempliApporteur;
	private boolean rempliReglement;
	private boolean rempliRemise;

	
	//par référence
	private String debFacture;
	private String finFacture;
	private String debAvoir;
	private String finAvoir;
	private String debApporteur;
	private String finApporteur;
//	private String debAcompte;
//	private String finAcompte;
	private String debReglement;
	private String finReglement;
	private String debRemise;
	private String finRemise;
	
	//par date
	private Date debDateVente;
	private Date finDateVente;
	private Date debDateReglement;
	private Date finDateReglement;
	
	
	
    private List<TaFactureDTO> listeDocumentsFactures=null;
    private List<TaAvoirDTO> listeDocumentsAvoirs=null;
    private List<TaApporteurDTO> listeDocumentsApporteurs=null;
//    private List<TaAcompteDTO> listeDocumentsAcomptes=null;
    private List<TaReglementDTO> listeDocumentsReglements=null;
    private List<TaRemiseDTO> listeDocumentsRemises=null;
    
	ExportationEpicea exportationEpicea;
	Importation importation;
	
	private List<IDocumentTiers> listeDocumentsManquantsLocal= new LinkedList<IDocumentTiers>();
	
	@EJB ITaFactureServiceRemote taFactureService;
	@EJB ITaAvoirServiceRemote taAvoirService;
	@EJB ITaApporteurServiceRemote taApporteurService;
//	@EJB ITaAcompteServiceRemote taAcompteService;
	@EJB ITaReglementServiceRemote taReglementService;
	@EJB ITaRemiseServiceRemote taRemiseService;

	
	@EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
//    @Inject IFactureDAO daoFacture;
//    @Inject IAvoirDAO daoAvoir;
//    @Inject IAcompteDAO daoAcompte;
//    @Inject IReglementDAO daoReglement;
//    @Inject IRemiseDAO daoRemise;
	@EJB private ITaPreferencesServiceRemote PreferencesService;
	@EJB private IExportationServiceRemote ExportationService;
	@EJB private IImportationServiceRemote ImportationService;
	
//	@Inject private EtatExportationDocumentBean etatExportationDocumentBean;
	
	
	private IDocumentTiers[] selection;
	private IDocumentTiers[] selection2;
	
	@PostConstruct
	public void init() {
		reglements = new ArrayList<SelectItem>();
//        reglements.add(new SelectItem(libAcompte, "Acomptes"));
        reglements.add(new SelectItem(getLibSimpleReglement(), "Règlement simple"));
        reglements.add(new SelectItem(libRemise, "Remise"));
        
        actRefresh();
	}
	
	public void etatExportationDesDocument(ActionEvent e) {
		
	}
	
	public void actFermer(ActionEvent e) {
		reset();
		etatExportationDocumentBean.reset();
	}
	
//	public void actToutExporterAgrigest(ActionEvent e) {
//		typeClientExport=ExportationEpicea.CLIENT_AGRIGEST;
//		actToutExporter(e);
//	}
	public void actToutExporter(String typeClient) {
   
		typeClientExport=typeClient;
				List<fr.legrain.document.model.TaFacture> idFactureAExporter = null;
				List<TaAvoir> idAvoirAExporter = null;
				List<TaApporteur> idApporteurAExporter = null;
//				List<TaAcompte> idAcompteAExporter = null;
				List<TaReglement> idReglementAExporter = null;
				List<TaRemise> idRemiseAExporter = null;
				
				String codeDeb = null;
				String codeFin = null;
				String codeDebAvoir = null;
				String codeFinAvoir = null;
				String codeDebApporteur = null;
				String codeFinApporteur = null;	
//				String codeDebAcompte = null;
//				String codeFinAcompte = null;
				String codeDebReglement = null;
				String codeFinReglement = null;
				String codeDebRemise = null;
				String codeFinRemise = null;
//				exporterAcompte=true;
//				exporterReglementSimple=true;
//				exporterAvecPointage=true;
				initEcranSurPreferences();
				
				
				if(listeDocumentsFactures.size()>0){
					codeDeb = (listeDocumentsFactures.get(0).getCodeDocument());

					codeFin = (listeDocumentsFactures.get(listeDocumentsFactures.size()-1).getCodeDocument());
				}
				if(listeDocumentsAvoirs.size()>0){
					codeDebAvoir = (listeDocumentsAvoirs.get(0).getCodeDocument());

					codeFinAvoir = (listeDocumentsAvoirs.get(listeDocumentsAvoirs.size()-1).getCodeDocument());
				}
				if(listeDocumentsApporteurs.size()>0){
					codeDebApporteur = (listeDocumentsApporteurs.get(0).getCodeDocument());

					codeFinApporteur = (listeDocumentsApporteurs.get(listeDocumentsApporteurs.size()-1).getCodeDocument());
				}				
				if(exporterReglementLies){
//				if(exporterAcompte){
//					if(listeDocumentsAcomptes.size()>0){
//						codeDebAcompte = (listeDocumentsAcomptes.get(0).getCodeDocument());
//
//						codeFinAcompte = (listeDocumentsAcomptes.get(listeDocumentsAcomptes.size()-1).getCodeDocument());
//					}
//				}
				if(exporterReglementSimple){		
					if(listeDocumentsReglements.size()>0){
						codeDebReglement = (listeDocumentsReglements.get(0).getCodeDocument());

						codeFinReglement = (listeDocumentsReglements.get(listeDocumentsReglements.size()-1).getCodeDocument());
					}
				}
				if(exporterRemise){
					if(listeDocumentsRemises.size()>0){
						codeDebRemise = (listeDocumentsRemises.get(0).getCodeDocument());

						codeFinRemise = (listeDocumentsRemises.get(listeDocumentsRemises.size()-1).getCodeDocument());
					}
				}
				}
				//||codeDebAcompte!=null
				if(codeDeb!=null || codeDebAvoir!=null ||   codeDebReglement!=null || codeDebRemise!=null) {//1 code => cette facture
//					logger.debug("Exportation - selection par code");
					if(codeDeb!=null) {
						if(codeFin!=null) {//2 codes => factures entre les 2 codes si intervalle correct
							idFactureAExporter = taFactureService.rechercheDocument(codeDeb,codeFin);
						} else {
							codeFin = codeDeb;
							idFactureAExporter = taFactureService.rechercheDocument(codeDeb,codeFin);
						}
					}
					if(codeDebAvoir!=null) {
						if(codeFinAvoir!=null) {//2 codes => factures entre les 2 codes si intervalle correct
							idAvoirAExporter = taAvoirService.rechercheDocument(codeDebAvoir,codeFinAvoir);
						} else {
							codeFinAvoir = codeDebAvoir;
							idAvoirAExporter = taAvoirService.rechercheDocument(codeDebAvoir,codeFinAvoir);
						}
					}
					if(codeDebApporteur!=null) {
						if(codeFinApporteur!=null) {//2 codes => factures entre les 2 codes si intervalle correct
							idApporteurAExporter = taApporteurService.rechercheDocument(codeDebApporteur,codeFinApporteur);
						} else {
							codeFinApporteur = codeDebApporteur;
							idApporteurAExporter = taApporteurService.rechercheDocument(codeDebApporteur,codeFinApporteur);
						}
					}						
//					if(codeDebAcompte!=null) {
//						if(codeFinAcompte!=null) {//2 codes => factures entre les 2 codes si intervalle correct
//							idAcompteAExporter = taAcompteService.rechercheDocument(codeDebAcompte,codeFinAcompte);
//						} else {
//							codeFinAcompte = codeDebAcompte;
//							idAcompteAExporter = taAcompteService.rechercheDocument(codeDebAcompte,codeFinAcompte);
//						}
//					}
					if(codeDebReglement!=null) {
						if(codeFinReglement!=null) {//2 codes => factures entre les 2 codes si intervalle correct
							idReglementAExporter = taReglementService.rechercheDocument(codeDebReglement,codeFinReglement);
						} else {
							codeFinReglement = codeDebReglement;
							idReglementAExporter = taReglementService.rechercheDocument(codeDebReglement,codeFinReglement);
						}
					}
					if(codeDebRemise!=null) {
						if(codeFinRemise!=null) {//2 codes => factures entre les 2 codes si intervalle correct
							idRemiseAExporter = taRemiseService.rechercheDocument(codeDebRemise,codeFinRemise);
						} else {
							codeFinRemise = codeDebRemise;
							idRemiseAExporter = taRemiseService.rechercheDocument(codeDebRemise,codeFinRemise);
						}
					}			
				 
					
				} 
				///////////////////////////////////////
//				final String[] finalIdFactureAExporter = idFactureAExporter;
//				final String[] finalIdAvoirAExporter = idAvoirAExporter;
				 List<TaFacture> finalIdFactureAExporter = idFactureAExporter;
				 List<TaAvoir> finalIdAvoirAExporter = idAvoirAExporter;
				 List<TaApporteur> finalIdApporteurAExporter = idApporteurAExporter;
//				 List<TaAcompte> finalIdAcompteAExporter = idAcompteAExporter;
				 List<TaReglement> finalIdReglementAExporter = idReglementAExporter;
				 List<TaRemise> finalIdRemiseAExporter = idRemiseAExporter;
				
				final boolean finalReExportFacture = false;
				final boolean finalReExportAvoir = false;
				final boolean finalReExportApporteur = false;
//				final boolean finalReExportAcompte = false;
				final boolean finalReExportReglement = false;
				final boolean finalReExportRemise = false;
				
				 OptionExportation optionsInitiales = new OptionExportation();

//				optionsInitiales.setAcomptes(PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.ACOMPTES));
				optionsInitiales.setApporteurs(PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.APPORTEUR));
				optionsInitiales.setReglementLies(PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.REGLEMENTS_LIES));
				optionsInitiales.setReglementSimple(PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.REGLEMENT_SIMPLE));
				optionsInitiales.setRemise(PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.REMISE));
				optionsInitiales.setDocumentLies(PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.DOCUMENTS_LIES));
				optionsInitiales.setPointages(PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.POINTAGES));
				
				OptionExportation options = new OptionExportation();
				if(exporterReglementLies){
//					options.setAcomptes(optionsInitiales.getAcomptes());
					options.setDocumentLies(optionsInitiales.getDocumentLies());
					options.setReglementLies(optionsInitiales.getReglementLies());
					options.setReglementSimple(optionsInitiales.getReglementSimple());
					options.setRemise(optionsInitiales.getRemise());
				}else{
//					options.setAcomptes(false);
					options.setDocumentLies(false);
					options.setReglementLies(false);
					options.setReglementSimple(false);
					options.setRemise(false);
				}
				options.setPointages(exporterAvecPointage);
				options.setCentraliser(centraliserEcriture);
				options.setApporteurs(exporterApporteur);
				
				//modifie de façon temporaire les options de transfert
				initPreferences(options);
//				Display.getDefault().syncExec(new Runnable() {
//					@Override
//					public void run() {
//						ExportationEpicea retour;
						FacesContext context = FacesContext.getCurrentInstance();  
						try {
							exportationEpicea=new ExportationEpicea();
//							exportationEpicea = exporter(finalIdFactureAExporter,finalIdAvoirAExporter,finalIdApporteurAExporter,finalIdAcompteAExporter,finalIdReglementAExporter,finalIdRemiseAExporter,
//									finalReExportFacture,finalReExportAvoir,finalReExportApporteur,finalReExportAcompte,finalReExportReglement,finalReExportRemise,
//									exporterAvecPointage,centraliserEcriture,ecraserFichier);
							exportationEpicea = exporter(finalIdFactureAExporter,finalIdAvoirAExporter,finalIdApporteurAExporter,null,finalIdReglementAExporter,finalIdRemiseAExporter,
									finalReExportFacture,finalReExportAvoir,finalReExportApporteur,false,finalReExportReglement,finalReExportRemise,
									exporterAvecPointage,centraliserEcriture,ecraserFichier,typeClientExport);
							if(!exportationEpicea.getRetour()&& !exportationEpicea.getMessageRetour().equals(""))
							context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Exportation abandonnée", exportationEpicea.getMessageRetour())); 
//								MessageDialog.openError(vue.getShell(), "Exportation abandonnée", retour.getMessageRetour());
							else
								if(exportationEpicea.getMessageRetour().equals(""))
									exportationEpicea.setMessageRetour(  Const.C_MESSAGE_FIN_EXPORTATION1+exportationEpicea.getLocationFichier()+Const.C_MESSAGE_FIN_EXPORTATION2);
							context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Exportation Terminée", exportationEpicea.getMessageRetour())); 
//							PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO,"Exportation Terminée", exportationEpicea.getMessageRetour()));

							initPreferences(optionsInitiales);
//							try{
//								 InputStream stream = new FileInputStream(new File(exportationEpicea.getFichierExportation()));
//								    file = new DefaultStreamedContent(stream, "text/txt", "E2-Impor.txt");
//									} catch (IOException e1) {
//									// TODO Auto-generated catch block
//									e1.printStackTrace();
//								}
						} catch (Exception e1) {
//							logger.error("", e1);
							initPreferences(optionsInitiales);
						}
						actRefresh();
					}
//				});						


//			}
	
	
	protected ExportationEpicea exporter(List<TaFacture> idFactureAExporter,List<TaAvoir> idAvoirAExporter,List<TaApporteur> idApporteurAExporter,List<TaAcompte> idAcompteAExporter,
			List<TaReglement> idReglementAExporter,List<TaRemise> idRemiseAExporter,boolean reExportFacture,boolean reExportAvoir,boolean reExportApporteur,boolean reExportAcompte,
			boolean reExportReglement,boolean reExportRemise,final boolean gererPointages,final boolean centralisation,boolean ecraserFichier,String typeClient) throws Exception{
//		ExportationEpicea export = null;
		//		final String[] finalIdFactureAExporter = idFactureAExporter;
		//		final String[] finalIdAvoirAExporter = idAvoirAExporter;
		List<TaFacture> finalIdFactureAExporter = idFactureAExporter;
		List<TaAvoir> finalIdAvoirAExporter = idAvoirAExporter;
		List<TaApporteur> finalIdApporteurAExporter = idApporteurAExporter;
//		List<TaAcompte> finalIdAcompteAExporter = idAcompteAExporter;
		List<TaReglement> finalIdReglementAExporter = idReglementAExporter;
		List<TaRemise> finalIdRemiseAExporter = idRemiseAExporter;

		int finalReExportFacture = LibConversion.booleanToInt(reExportFacture);
		int finalReExportAvoir = LibConversion.booleanToInt(reExportAvoir);
		int finalReExportApporteur = LibConversion.booleanToInt(reExportApporteur);
//		int finalReExportAcompte = LibConversion.booleanToInt(reExportAcompte);
		int finalReExportReglement = LibConversion.booleanToInt(reExportReglement);
		int finalReExportRemise = LibConversion.booleanToInt(reExportRemise);


		int nbFacture = 0;
		int nbAvoir = 0;
		int nbApporteur = 0;
//		int nbAcompte = 0;
		int nbReglement = 0;
		int nbRemise = 0;

		if(finalIdFactureAExporter!=null) nbFacture = finalIdFactureAExporter.size();
		if(finalIdAvoirAExporter!=null) nbAvoir = finalIdAvoirAExporter.size();
		if(finalIdApporteurAExporter!=null) nbApporteur = finalIdApporteurAExporter.size();
//		if(finalIdAcompteAExporter!=null) nbAcompte = finalIdAcompteAExporter.size();
		if(finalIdReglementAExporter!=null) nbReglement = finalIdReglementAExporter.size();
		if(finalIdRemiseAExporter!=null) nbRemise = finalIdRemiseAExporter.size();

//		final int ticks = nbFacture+nbAvoir+nbAcompte+nbReglement+nbRemise;
		final int ticks = nbFacture+nbAvoir+nbReglement+nbRemise;
		//				monitor.beginTask("Exportation", ticks);
		try {

//			exportationEpicea=ExportationService.exportJPA(finalIdFactureAExporter,finalIdAvoirAExporter,finalIdApporteurAExporter,finalIdAcompteAExporter,finalIdReglementAExporter,
//					finalIdRemiseAExporter,finalReExportFacture,finalReExportAvoir,finalReExportApporteur,finalReExportAcompte,
//					finalReExportReglement,finalReExportRemise,gererPointages,centralisation,ecraserFichier);
			exportationEpicea=ExportationService.exportJPA(finalIdFactureAExporter,finalIdAvoirAExporter,finalIdApporteurAExporter,null,finalIdReglementAExporter,
					finalIdRemiseAExporter,finalReExportFacture,finalReExportAvoir,finalReExportApporteur,0,
					finalReExportReglement,finalReExportRemise,gererPointages,centralisation,ecraserFichier,typeClient);
			if(exportationEpicea.getRetour()){
				finalIdFactureAExporter = new  LinkedList<TaFacture>();
				finalIdAvoirAExporter = new  LinkedList<TaAvoir>();
				finalIdApporteurAExporter = new  LinkedList<TaApporteur>();
//				finalIdAcompteAExporter = new  LinkedList<TaAcompte>();
				finalIdReglementAExporter = new  LinkedList<TaReglement>();
				finalIdRemiseAExporter = new  LinkedList<TaRemise>();
				
				exportationEpicea.listeDocumentsManquants =exportationEpicea.verifCoherenceExportation();
//				listeDocumentsManquantsLocal.clear();
//				for (IDocumentTiers obj : exportationEpicea.listeDocumentsManquants) {
//					listeDocumentsManquantsLocal.add(obj);
//				}
//				for (IDocumentTiers o : listeDocumentsManquantsLocal) {
//					o.setAccepte(true);
//				}
//				contientDesDocumentsManquants();					
			}
		} catch (Exception e) {
		} finally {
		}

		return exportationEpicea;	
	}	
	
	private boolean trouveDansSelection(IDocumentTiers doc){
		for (int i = 0; i < selection.length; i++) {
			if(selection[i].equals(doc))return true;
		}
		return false;
	}
	


	
//	public void actExporterLesDocsManquantsAgrigest(ActionEvent e) throws Exception {
//		typeClientExport=ExportationEpicea.CLIENT_AGRIGEST;
//		actExporterLesDocsManquants(e);
//	}
	
	public void actExporterLesDocsManquants(String typeClient) throws Exception {
		List<TaFacture> finalIdFactureAExporter = new ArrayList<TaFacture>() ;
		List<TaAvoir> finalIdAvoirAExporter = new ArrayList<TaAvoir>() ;
		List<TaApporteur> finalIdApporteurAExporter = new ArrayList<TaApporteur>() ;
//		List<TaAcompte> finalIdAcompteAExporter = new ArrayList<TaAcompte>() ;
		List<TaReglement> finalIdReglementAExporter = new ArrayList<TaReglement>() ;
		List<TaRemise> finalIdRemiseAExporter =new ArrayList<TaRemise>();

		typeClientExport=typeClient;

            for (IDocumentTiers doc : listeDocumentsManquantsLocal) {
				if(trouveDansSelection(doc)){
					if(doc.getTypeDocument()==TaFacture.TYPE_DOC){
						finalIdFactureAExporter.add((TaFacture)doc);
					}
					if(doc.getTypeDocument()==TaAvoir.TYPE_DOC){
						finalIdAvoirAExporter.add((TaAvoir)doc);
					}
					if(doc.getTypeDocument()==TaApporteur.TYPE_DOC){
						finalIdApporteurAExporter.add((TaApporteur)doc);
					}						
//					if(doc.getTypeDocument()==TaAcompte.TYPE_DOC){
//						finalIdAcompteAExporter.add((TaAcompte)doc);
//					}
					if(doc.getTypeDocument()==TaReglement.TYPE_DOC){
						finalIdReglementAExporter.add((TaReglement)doc);
					}
					if(doc.getTypeDocument()==TaRemise.TYPE_DOC){
						finalIdRemiseAExporter.add((TaRemise)doc);
					}
				}
			}
            FacesContext context = FacesContext.getCurrentInstance();
//			exportationEpicea = exporter(finalIdFactureAExporter,finalIdAvoirAExporter,finalIdApporteurAExporter,finalIdAcompteAExporter,finalIdReglementAExporter,finalIdRemiseAExporter,
//					reexporterFacture,reexporterFactureAvoir,reexporterFactureApporteur,reexporterAcompte,reexporterReglement,reexporterRemise,
//					false,centraliserEcriture,false);
			exportationEpicea = exporter(finalIdFactureAExporter,finalIdAvoirAExporter,finalIdApporteurAExporter,null,finalIdReglementAExporter,finalIdRemiseAExporter,
					reexporterFacture,reexporterFactureAvoir,reexporterFactureApporteur,false,reexporterReglement,reexporterRemise,
					false,centraliserEcriture,false,typeClientExport);            
			if(!exportationEpicea.getRetour()&& !exportationEpicea.getMessageRetour().equals("")){
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Exportation abandonnée", exportationEpicea.getMessageRetour()));
			}
			else { exportationEpicea.setMessageRetour( Const.C_MESSAGE_FIN_EXPORTATION1+exportationEpicea.getLocationFichier()+Const.C_MESSAGE_FIN_EXPORTATION2);
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Exportation Terminée", exportationEpicea.getMessageRetour())); 
			}
    		
		
	}
//	public void actExporterVenteRefAgrigest(ActionEvent e) throws Exception {
//		typeClientExport=ExportationEpicea.CLIENT_AGRIGEST;
//		actExporterVenteRef(e);
//	}
	
	public void actExporterVenteRef(String typeClient) throws Exception {
		typeClientExport=typeClient;
		List<TaFacture> idFactureAExporter = null;
		List<TaAvoir> idAvoirAExporter = null;
		List<TaApporteur> idApporteurAExporter = null;
		

		
		Date dateDeb = null;
		Date dateFin = null;
		String codeDeb = null;
		String codeFin = null;
		String codeDebAvoir = null;
		String codeFinAvoir = null;
		String codeDebApporteur = null;
		String codeFinApporteur = null;		

		
		if(!debFacture.isEmpty()) {
			codeDeb = debFacture.toUpperCase();
		}
		if(!finFacture.isEmpty()) {
			codeFin = finFacture.toUpperCase();
		}
		if(!debAvoir.isEmpty()) {
			codeDebAvoir = debAvoir.toUpperCase();
		}
		if(!finAvoir.isEmpty()) {
			codeFinAvoir = finAvoir.toUpperCase();
		}
		if(!debApporteur.isEmpty()) {
			codeDebApporteur = debApporteur.toUpperCase();
		}
		if(!finApporteur.isEmpty()) {
			codeFinApporteur = finApporteur.toUpperCase();
		}		

		FacesContext context = FacesContext.getCurrentInstance(); 
		if(codeDeb!=null || codeDebAvoir!=null || codeDebApporteur!=null) {//1 code => cette facture
//			logger.debug("Exportation - selection par code");
			if(codeDeb!=null) {
				if(codeFin==null) {//2 codes => factures entre les 2 codes si intervalle correct					
					codeFin = codeDeb;
				}
				idFactureAExporter = taFactureService.rechercheDocument(codeDeb,codeFin);
			}
			if(codeDebAvoir!=null) {
				if(codeFinAvoir==null) {//2 codes => factures entre les 2 codes si intervalle correct
					codeFinAvoir = codeDebAvoir;
				}
				idAvoirAExporter = taAvoirService.rechercheDocument(codeDebAvoir,codeFinAvoir);
			}
			if(codeDebApporteur!=null) {
				if(codeFinApporteur==null) {//2 codes => factures entre les 2 codes si intervalle correct
					codeFinApporteur = codeDebApporteur;
				}
				idApporteurAExporter = taApporteurService.rechercheDocument(codeDebApporteur,codeFinApporteur);
			}			
			
		} else if(dateDeb!=null) {//1 date => toutes les factures à cette date
//			logger.debug("Exportation - selection par date");

			if(dateFin!=null) {//2 dates => factures entre les 2 dates si intervalle correct				
				if(LibDate.compareTo(dateDeb,dateFin)>0) {
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"ATTENTION", "La date de début doit être antérieure à la date fin")); 
					throw new Exception("probleme intervalle des dates");
				}				

			} else {
				dateFin = dateDeb;
			}
			idFactureAExporter = taFactureService.rechercheDocument(dateDeb,dateFin);
			idAvoirAExporter = taAvoirService.rechercheDocument(dateDeb,dateFin);
			idApporteurAExporter = taApporteurService.rechercheDocument(dateDeb,dateFin);

			
		} 
		///////////////////////////////////////

		final List<TaFacture> finalIdFactureAExporter = idFactureAExporter;
		final List<TaAvoir> finalIdAvoirAExporter = idAvoirAExporter;
		final List<TaApporteur> finalIdApporteurAExporter = idApporteurAExporter;



		exportationEpicea=new ExportationEpicea();

//		exportationEpicea=exporter(finalIdFactureAExporter,finalIdAvoirAExporter,finalIdApporteurAExporter,null,null,null,
//				reexporterFacture,reexporterFactureAvoir,reexporterFactureApporteur,reexporterAcompte,reexporterReglement,reexporterRemise,exporterAvecPointage,centraliserEcriture,ecraserFichier);
		exportationEpicea=exporter(finalIdFactureAExporter,finalIdAvoirAExporter,finalIdApporteurAExporter,null,null,null,
				reexporterFacture,reexporterFactureAvoir,reexporterFactureApporteur,false,reexporterReglement,reexporterRemise,exporterAvecPointage,centraliserEcriture,ecraserFichier,typeClientExport);
		if(!exportationEpicea.getRetour()&& exportationEpicea.getMessageRetour()!=null && !exportationEpicea.getMessageRetour().equals(""))
						context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Exportation abandonnée", exportationEpicea.getMessageRetour()));
					else
						if(exportationEpicea.getMessageRetour()!=null && exportationEpicea.getMessageRetour().equals(""))
							exportationEpicea.setMessageRetour(  Const.C_MESSAGE_FIN_EXPORTATION1+exportationEpicea.getLocationFichier()+Const.C_MESSAGE_FIN_EXPORTATION2);
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Exportation Terminée", exportationEpicea.getMessageRetour()));
//					PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO,"Exportation Terminée", exportationEpicea.getMessageRetour()));		

//					try{
//						InputStream stream = new FileInputStream(new File(exportationEpicea.getFichierExportation()));
//						file = new DefaultStreamedContent(stream, "text/txt", "E2-Impor.txt");
//					} catch (IOException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
					actRefresh();
	}
	
	public boolean initFichierATelechargerSiExiste(){

		try{
			if(exportationEpicea!=null ){
				File fichier=new File(exportationEpicea.getFichierExportationServeur());
				if( fichier!=null){
					InputStream stream = new FileInputStream(fichier);
					if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_ISAGRIFINAL))
						file = new DefaultStreamedContent(stream, "CSV/csv", "ISAEXPORT.TXT");
					else
					if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_CGID))
						file = new DefaultStreamedContent(stream, "CSV/csv", "E2-IMPOR.CSV");
					else
						if(exportationEpicea.getTypeClientExport().equals(ExportationEpicea.CLIENT_SAGE))
							file = new DefaultStreamedContent(stream, "CSV/csv", "export_bdg_sage.csv");
						else
						file = new DefaultStreamedContent(stream, "text/txt", "E2-IMPOR.TXT");
					return true;
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return false;	
	}
	
	public boolean initFichierInstallationImportAgrigestSiExiste(){

		try{
			
			InputStream stream = this.getClass().getResourceAsStream("/fr/legrain/bdg/webapp/export/setupImportAgrigest.exe" );
			InputStream stream2 = this.getClass().getResourceAsStream("setupImportAgrigest.exe" );
			if( stream!=null){
					fileImportAgrigest = new DefaultStreamedContent(stream, "Executable/exe", "SetupImportAgrigest.exe");
				return true;
			}
			
//			FacesContext context = FacesContext.getCurrentInstance();
//			InputStream stream = context.getExternalContext().getResourceAsStream("/export/agrigest/setupImportAgrigest1.exe" );
//			fileImportAgrigest = new DefaultStreamedContent(stream, "Executable/exe", "SetupImportAgrigest.exe");
//			return true;


		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return false;	
	}
	
	public boolean contientDesDocumentsManquants(){
		boolean retour=false;
		
		retour= (exportationEpicea!=null && listeDocumentsManquantsLocal!=null 
				&& listeDocumentsManquantsLocal.size()>0);
		if(retour)
			selection2=(IDocumentTiers[]) listeDocumentsManquantsLocal.toArray(new IDocumentTiers[listeDocumentsManquantsLocal.size()] );
		
		PrimeFaces.current().ajax().addCallbackParam("afficheListeDocManquant", retour);		
		return retour;
	}

//	public void actExporterReglementRefAgrigest(ActionEvent e) throws Exception {
//		typeClientExport=ExportationEpicea.CLIENT_AGRIGEST;
//		actExporterReglementRef(e);
//	}
	public void actExporterReglementRef(String typeClient) throws Exception {
		typeClientExport=typeClient;
//		List<TaAcompte> idAcompteAExporter = null;
		List<TaReglement> idReglementAExporter = null;
		List<TaRemise> idRemiseAExporter = null;
		
		
		Date dateDeb = null;
		Date dateFin = null;

//		String codeDebAcompte = null;
//		String codeFinAcompte = null;
		String codeDebReglement = null;
		String codeFinReglement = null;
		String codeDebRemise = null;
		String codeFinRemise = null;

//		if(!debAcompte.isEmpty()) {
//			codeDebAcompte = debAcompte.toUpperCase();
//		}
//		if(!finAcompte.isEmpty()) {
//			codeFinAcompte = finAcompte.toUpperCase();
//		}
		if(!debReglement.isEmpty()) {
			codeDebReglement = debReglement.toUpperCase();
		}
		if(!finReglement.isEmpty()) {
			codeFinReglement = finReglement.toUpperCase();
		}
		if(!debRemise.isEmpty()) {
			codeDebRemise = debRemise.toUpperCase();
		}
		if(!finRemise.isEmpty()) {
			codeFinRemise = finRemise.toUpperCase();
		}
		FacesContext context = FacesContext.getCurrentInstance(); 
		//codeDebAcompte!=null ||
		if(  codeDebReglement!=null || codeDebRemise!=null) {//1 code => cette facture

//			if(codeDebAcompte!=null) {
//				if(codeFinAcompte==null) {//2 codes => factures entre les 2 codes si intervalle correct
//					codeFinAcompte = codeDebAcompte;
//				}
//				idAcompteAExporter = taAcompteService.rechercheDocument(codeDebAcompte,codeFinAcompte);
//			}
			if(codeDebReglement!=null) {
				if(codeFinReglement==null) {//2 codes => factures entre les 2 codes si intervalle correct
					codeFinReglement = codeDebReglement;
				}
				idReglementAExporter = taReglementService.rechercheDocument(codeDebReglement,codeFinReglement);				
			}
			if(codeDebRemise!=null) {
				if(codeFinRemise==null) {//2 codes => factures entre les 2 codes si intervalle correct
					codeFinRemise = codeDebRemise;
				}
				idRemiseAExporter = taRemiseService.rechercheDocument(codeDebRemise,codeFinRemise);
			}			
		} else if(dateDeb!=null) {//1 date => toutes les factures à cette date
//			logger.debug("Exportation - selection par date");
			if(dateFin!=null) {//2 dates => factures entre les 2 dates si intervalle correct
				if(LibDate.compareTo(dateDeb,dateFin)>0) {
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"ATTENTION", "La date de début doit être antérieure à la date fin")); 
					throw new Exception("probleme intervalle des dates");
				}				

			} else {
				dateFin = dateDeb;
			}

//			idAcompteAExporter = taAcompteService.rechercheAcompteNonRemises(null,dateDeb,dateFin,reexporterReglement,null,null,false);
			idReglementAExporter = taReglementService.rechercheReglementNonRemises(null,dateDeb,dateFin,reexporterReglement,null,null,false,0);
			idRemiseAExporter = taRemiseService.rechercheDocument(dateDeb,dateFin);
			
		} 
		///////////////////////////////////////

//		final List<TaAcompte> finalIdAcompteAExporter = idAcompteAExporter;
		final List<TaReglement> finalIdReglementAExporter = idReglementAExporter;
		final List<TaRemise> finalIdRemiseAExporter = idRemiseAExporter;
		
		exportationEpicea=new ExportationEpicea();

//		exportationEpicea=exporter(null,null,null,finalIdAcompteAExporter,finalIdReglementAExporter,finalIdRemiseAExporter,
//				reexporterFacture,reexporterFactureAvoir,reexporterFactureApporteur,reexporterAcompte,reexporterReglement,reexporterRemise,exporterAvecPointage,centraliserEcriture,ecraserFichier);
		exportationEpicea=exporter(null,null,null,null,finalIdReglementAExporter,finalIdRemiseAExporter,
				reexporterFacture,reexporterFactureAvoir,reexporterFactureApporteur,false,reexporterReglement,reexporterRemise,exporterAvecPointage,centraliserEcriture,ecraserFichier,typeClientExport);

       
					if(!exportationEpicea.getRetour()&& !exportationEpicea.getMessageRetour().equals(""))
						context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Exportation abandonnée", exportationEpicea.getMessageRetour())); 
					else
						if(exportationEpicea.getMessageRetour().equals(""))
							exportationEpicea.setMessageRetour(  Const.C_MESSAGE_FIN_EXPORTATION1+exportationEpicea.getLocationFichier()+Const.C_MESSAGE_FIN_EXPORTATION2);
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Exportation Terminée", exportationEpicea.getMessageRetour()));
//					PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO,"Exportation Terminée", exportationEpicea.getMessageRetour()));

//					try{
//						InputStream stream = new FileInputStream(new File(exportationEpicea.getFichierExportation()));
//						file = new DefaultStreamedContent(stream, "text/txt", "E2-Impor.txt");
//					} catch (IOException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//					actRefresh();
	}

//	public void actExporterVenteDateAgrigest(ActionEvent e)throws Exception {
//		typeClientExport=ExportationEpicea.CLIENT_AGRIGEST;
//		actExporterVenteDate(e);
//	}
	public void actExporterVenteDate(String typeClient)throws Exception {
		//Récupération des paramètres dans l'ihm
		typeClientExport=typeClient;
				List<TaFacture> idFactureAExporter = null;
				List<TaAvoir> idAvoirAExporter = null;
				List<TaApporteur> idApporteurAExporter = null;
				Date dateDebVentes = null;
				Date dateFinVentes = null;

				if( debDateVente!=null ) {
					dateDebVentes = debDateVente;
				}
				if(finDateVente!=null ) {
					dateFinVentes = finDateVente;
				}
				
				FacesContext context = FacesContext.getCurrentInstance(); 
				if(dateDebVentes!=null) {//1 date => toutes les factures à cette date
//					logger.debug("Exportation - selection par date");
					if(dateFinVentes!=null) {//2 dates => factures entre les 2 dates si intervalle correct

						if(LibDate.compareTo(dateDebVentes,dateFinVentes)>0) {
							context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"ATTENTION", "La date de début des ventes doit être antérieure à la date fin")); 
							throw new Exception("probleme intervalle des dates de ventes");
						}				

					} else {
						dateFinVentes = dateDebVentes;
					}			
				} 		
				
				if(rempliVentes)idFactureAExporter = taFactureService.rechercheDocumentOrderByDate(dateDebVentes,dateFinVentes);
				if(rempliAvoirs)idAvoirAExporter = taAvoirService.rechercheDocumentOrderByDate(dateDebVentes,dateFinVentes);		
				if(rempliApporteur)idApporteurAExporter = taApporteurService.rechercheDocumentOrderByDate(dateDebVentes,dateFinVentes);	

			 
				///////////////////////////////////////

				final List<TaFacture> finalIdFactureAExporter = idFactureAExporter;
				final List<TaAvoir> finalIdAvoirAExporter = idAvoirAExporter;
				final List<TaApporteur> finalIdApporteurAExporter = idApporteurAExporter;


				exportationEpicea=new ExportationEpicea();
				
//				exportationEpicea=exporter(finalIdFactureAExporter,finalIdAvoirAExporter,finalIdApporteurAExporter,null,null,null,
//						reexporterFacture,reexporterFactureAvoir,reexporterFactureApporteur,reexporterAcompte,reexporterReglement,reexporterRemise,exporterAvecPointage,centraliserEcriture,ecraserFichier);
				exportationEpicea=exporter(finalIdFactureAExporter,finalIdAvoirAExporter,finalIdApporteurAExporter,null,null,null,
						reexporterFacture,reexporterFactureAvoir,reexporterFactureApporteur,false,reexporterReglement,reexporterRemise,exporterAvecPointage,centraliserEcriture,ecraserFichier,typeClientExport);
							if(!exportationEpicea.getRetour()&& !exportationEpicea.getMessageRetour().equals(""))
								context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Exportation abandonnée", exportationEpicea.getMessageRetour())); 
							else
								if(exportationEpicea.getMessageRetour().equals(""))
									exportationEpicea.setMessageRetour(  Const.C_MESSAGE_FIN_EXPORTATION1+exportationEpicea.getLocationFichier()+Const.C_MESSAGE_FIN_EXPORTATION2);
							context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Exportation Terminée", exportationEpicea.getMessageRetour()));
//							PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO,"Exportation Terminée", exportationEpicea.getMessageRetour()));
							actRefresh();
			}

	
//	public void actExporterReglementDateAgrigest(ActionEvent e) throws Exception{
//		typeClientExport=ExportationEpicea.CLIENT_AGRIGEST;
//		actExporterReglementDate(e);
//	}
	public void actExporterReglementDate(String typeClient) throws Exception{
		typeClientExport=typeClient;
		//Récupération des paramètres dans l'ihm
				List<TaAcompte> idAcompteAExporter = null;
				List<TaReglement> idReglementAExporter = null;
				List<TaRemise> idRemiseAExporter = null;
				Date dateDebReglement = null;
				Date dateFinReglement = null;

				if( debDateReglement!=null ) {
					dateDebReglement = debDateReglement;
				}
				if(finDateReglement!=null ) {
					dateFinReglement = finDateReglement;
				}
						
				FacesContext context = FacesContext.getCurrentInstance(); 
				if(dateDebReglement!=null) {//1 date => toutes les factures à cette date
//					logger.debug("Exportation - selection par date");
					if(dateFinReglement!=null) {//2 dates => factures entre les 2 dates si intervalle correct

						if(LibDate.compareTo(dateDebReglement,dateFinReglement)>0) {
							context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"ATTENTION", "La date de début des réglements doit être antérieure à la date fin")); 
							throw new Exception("probleme intervalle des dates des réglements");
						}				
					} else {
						dateFinReglement = dateDebReglement;
					}			
				}
					
//				if(rempliAcomptes)idAcompteAExporter = taAcompteService.rechercheAcompteNonRemises(null,dateDebReglement,dateFinReglement,reexporterReglement,null,null,true);
				if(rempliReglement)idReglementAExporter = taReglementService.rechercheReglementNonRemises(null,dateDebReglement,dateFinReglement,reexporterReglement,null,null,true,0);
				if(rempliRemise)idRemiseAExporter = taRemiseService.rechercheDocumentOrderByDate(dateDebReglement,dateFinReglement);
			 
				///////////////////////////////////////

//				final List<TaAcompte> finalIdAcompteAExporter = idAcompteAExporter;
				final List<TaReglement> finalIdReglementAExporter = idReglementAExporter;
				final List<TaRemise> finalIdRemiseAExporter = idRemiseAExporter;
				

				exportationEpicea=new ExportationEpicea();
				
//				exportationEpicea=exporter(null,null,null,finalIdAcompteAExporter,finalIdReglementAExporter,finalIdRemiseAExporter,
//						reexporterFacture,reexporterFactureAvoir,reexporterFactureApporteur,reexporterAcompte,reexporterReglement,reexporterRemise,exporterAvecPointage,centraliserEcriture,ecraserFichier);
				exportationEpicea=exporter(null,null,null,null,finalIdReglementAExporter,finalIdRemiseAExporter,
						reexporterFacture,reexporterFactureAvoir,reexporterFactureApporteur,false,reexporterReglement,reexporterRemise,exporterAvecPointage,centraliserEcriture,ecraserFichier,typeClientExport);
							if(!exportationEpicea.getRetour()&& !exportationEpicea.getMessageRetour().equals(""))
								context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Exportation abandonnée", exportationEpicea.getMessageRetour())); 
							else
								if(exportationEpicea.getMessageRetour().equals(""))
									exportationEpicea.setMessageRetour(  Const.C_MESSAGE_FIN_EXPORTATION1+exportationEpicea.getLocationFichier()+Const.C_MESSAGE_FIN_EXPORTATION2);
							context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Exportation Terminée", exportationEpicea.getMessageRetour())); 
//							PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO,"Exportation Terminée", exportationEpicea.getMessageRetour()));

							actRefresh();
			}
	
	public void enregistreOption(String cle,Object valeur){
		if(valeur instanceof Boolean) PreferencesService.mergePreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION, cle, (Boolean) valeur);
		if(valeur instanceof Integer) PreferencesService.mergePreferenceInteger(ITaPreferencesServiceRemote.EXPORTATION, cle, (Integer) valeur);
		if(valeur instanceof String) PreferencesService.mergePreferenceString(ITaPreferencesServiceRemote.EXPORTATION, cle, (String) valeur);
		initEcranSurPreferences();
	}
	
	public void optionTousReglement() {
		if(exporterTousLesRegelements) {
//			selectedReglement = new String[]{libAcompte,libSimpleReglement,libRemise};
			selectedReglement = new String[]{libSimpleReglement,libRemise};
		} else {
			selectedReglement = new String[]{};	
		}
	}
	public void optionTousReglementDepart() {
		ArrayList<String> listeOptions=new ArrayList<String>();
			selectedReglement = new String[]{};	
//			if(exporterAcompte)listeOptions.add(libAcompte);
			if(exporterReglementSimple)listeOptions.add(libSimpleReglement);
			if(exporterRemise)listeOptions.add(libRemise);
			selectedReglement=listeOptions.toArray(new String[listeOptions.size()]);
			if(selectedReglement.length<2) {
				exporterTousLesRegelements = false;
			} else {
				exporterTousLesRegelements = true;
			}
	}

	
	public void optionReglement() {
//		enregistreOption(libAcompte, false);
		enregistreOption(libSimpleReglement, false);
		enregistreOption(libRemise, false);
		if(selectedReglement.length<2) {
			exporterTousLesRegelements = false;
		} else {
			exporterTousLesRegelements = true;
		}
		for (String obj : selectedReglement) {
			enregistreOption(obj, true);
		}	
		initEcranSurPreferences();
	}
	
	public boolean isExporterReglementLies() {
		return exporterReglementLies;
	}
	public void setExporterReglementLies(boolean exporterAvecRegelement) {
		this.exporterReglementLies = exporterAvecRegelement;
	}
	public boolean isExporterAvecPointage() {
		return exporterAvecPointage;
	}
	public void setExporterAvecPointage(boolean exporterAvecPointage) {
		this.exporterAvecPointage = exporterAvecPointage;
	}
	public boolean isCentraliserEcriture() {
		return centraliserEcriture;
	}
	public void setCentraliserEcriture(boolean centraliserEcriture) {
		this.centraliserEcriture = centraliserEcriture;
	}
	public boolean isExporterApporteur() {
		return exporterApporteur;
	}
	public void setExporterApporteur(boolean exporterApporteur) {
		this.exporterApporteur = exporterApporteur;
	}

	public List<SelectItem> getReglements() {
		return reglements;
	}

	public void setReglements(List<SelectItem> reglements) {
		this.reglements = reglements;
	}

	public String[] getSelectedReglement() {
		return selectedReglement;
	}

	public void setSelectedReglement(String[] selectedReglement) {
		this.selectedReglement = selectedReglement;
	}

	public boolean isExporterTousLesRegelements() {
		return exporterTousLesRegelements;
	}

	public void setExporterTousLesRegelements(boolean exporterTousLesRegelements) {
		this.exporterTousLesRegelements = exporterTousLesRegelements;
	}



	public boolean isExporterFacture() {
		return exporterFacture;
	}

	public void setExporterFacture(boolean exporterFacture) {
		this.exporterFacture = exporterFacture;
	}

	public boolean isExporterFactureAvoir() {
		return exporterFactureAvoir;
	}

	public void setExporterFactureAvoir(boolean exporterFactureAvoir) {
		this.exporterFactureAvoir = exporterFactureAvoir;
	}

	public boolean isExporterFactureApporteur() {
		return exporterFactureApporteur;
	}

	public void setExporterFactureApporteur(boolean exporterFactureApporteur) {
		this.exporterFactureApporteur = exporterFactureApporteur;
	}

//	public boolean isExporterAcompte() {
//		return exporterAcompte;
//	}
//
//	public void setExporterAcompte(boolean exporterAcompte) {
//		this.exporterAcompte = exporterAcompte;
//	}


	public boolean isExporterRemise() {
		return exporterRemise;
	}

	public void setExporterRemise(boolean exporterRemise) {
		this.exporterRemise = exporterRemise;
	}



	public String getDebFacture() {
		return debFacture;
	}

	public void setDebFacture(String debFacture) {
		this.debFacture = debFacture;
	}

	public String getFinFacture() {
		return finFacture;
	}

	public void setFinFacture(String finFacture) {
		this.finFacture = finFacture;
	}

	public String getDebAvoir() {
		return debAvoir;
	}

	public void setDebAvoir(String debAvoir) {
		this.debAvoir = debAvoir;
	}

	public String getFinAvoir() {
		return finAvoir;
	}

	public void setFinAvoir(String finAvoir) {
		this.finAvoir = finAvoir;
	}

	public String getDebApporteur() {
		return debApporteur;
	}

	public void setDebApporteur(String debApporteur) {
		this.debApporteur = debApporteur;
	}

	public String getFinApporteur() {
		return finApporteur;
	}

	public void setFinApporteur(String finApporteur) {
		this.finApporteur = finApporteur;
	}

//	public String getDebAcompte() {
//		return debAcompte;
//	}
//
//	public void setDebAcompte(String debAcompte) {
//		this.debAcompte = debAcompte;
//	}
//
//	public String getFinAcompte() {
//		return finAcompte;
//	}
//
//	public void setFinAcompte(String finAcompte) {
//		this.finAcompte = finAcompte;
//	}

	public String getDebReglement() {
		return debReglement;
	}

	public void setDebReglement(String debReglement) {
		this.debReglement = debReglement;
	}

	public String getFinReglement() {
		return finReglement;
	}

	public void setFinReglement(String finReglement) {
		this.finReglement = finReglement;
	}

	public String getDebRemise() {
		return debRemise;
	}

	public void setDebRemise(String debRemise) {
		this.debRemise = debRemise;
	}

	public String getFinRemise() {
		return finRemise;
	}

	public void setFinRemise(String finRemise) {
		this.finRemise = finRemise;
	}

	public Date getDebDateVente() {
		return debDateVente;
	}

	public void setDebDateVente(Date debDateVente) {
		this.debDateVente = debDateVente;
	}

	public Date getFinDateVente() {
		return finDateVente;
	}

	public void setFinDateVente(Date finDateVente) {
		this.finDateVente = finDateVente;
	}

	public Date getDebDateReglement() {
		return debDateReglement;
	}

	public void setDebDateReglement(Date debDateReglement) {
		this.debDateReglement = debDateReglement;
	}

	public Date getFinDateReglement() {
		return finDateReglement;
	}

	public void setFinDateReglement(Date finDateReglement) {
		this.finDateReglement = finDateReglement;
	}

	public boolean isExporterReglementSimple() {
		return exporterReglementSimple;
	}

	public void setExporterReglementSimple(boolean exporterReglementSimple) {
		this.exporterReglementSimple = exporterReglementSimple;
	}

	public StreamedContent getFile() {
		return file;
	}
	
 

	
	public void actRefresh()  {
		// TODO Raccord de méthode auto-généré
		toutReexporter=false;
		initTousLesCheck(false);
		initEcranSurPreferences();
		initProposalAdapter();
		initBornes();
		initBoutonBornes();
		initBoutonReglement();
		initBornesDatesVentes();
		initBornesDatesReglement();
		optionTousReglementDepart();
		etatExportationDocumentBean.actRefresh();
//		if(exporterAvecPointage)exporterAvecPointage=exporterReglementLies;
	}

	public void initPreferences(OptionExportation option){
//		PreferencesService.mergePreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION, ITaPreferencesServiceRemote.ACOMPTES,option.getAcomptes());		
		PreferencesService.mergePreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION, ITaPreferencesServiceRemote.REGLEMENT_SIMPLE,option.getReglementSimple());
		PreferencesService.mergePreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION, ITaPreferencesServiceRemote.REMISE,option.getRemise());
		PreferencesService.mergePreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION, ITaPreferencesServiceRemote.DOCUMENTS_LIES,option.getDocumentLies());
		PreferencesService.mergePreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION, ITaPreferencesServiceRemote.REGLEMENTS_LIES,option.getReglementLies());
		PreferencesService.mergePreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION, ITaPreferencesServiceRemote.POINTAGES,option.getPointages());
		PreferencesService.mergePreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION, ITaPreferencesServiceRemote.APPORTEUR,option.getApporteurs());
		PreferencesService.mergePreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION, ITaPreferencesServiceRemote.CENTRALISER_ECRITURES,option.getCentraliser());
	}

	public ExportationEpicea getExportationEpicea() {
		return exportationEpicea;
	}

	public void setExportationEpicea(ExportationEpicea exportationEpicea) {
		this.exportationEpicea = exportationEpicea;
	}



	public void initEcranSurPreferences(){
		boolean enabled=true;
		boolean tous;
		try {
//			exporterAcompte=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION, ITaPreferencesServiceRemote.ACOMPTES);
			exporterReglementLies=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION, ITaPreferencesServiceRemote.REGLEMENTS_LIES);

			exporterReglementSimple=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION, ITaPreferencesServiceRemote.REGLEMENT_SIMPLE);
			
			exporterRemise=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION, ITaPreferencesServiceRemote.REMISE);
			exporterDocumentLies=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION, ITaPreferencesServiceRemote.DOCUMENTS_LIES);
			exporterAvecPointage=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION, ITaPreferencesServiceRemote.POINTAGES);
			centraliserEcriture=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION, ITaPreferencesServiceRemote.CENTRALISER_ECRITURES);
			exporterApporteur=PreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION, ITaPreferencesServiceRemote.APPORTEUR);
			
		//déclenche la mise à jour de l'écran parent
//		DeclencheInitBorneController(); 
		} catch (Exception e1) {
//			logger.error("", e1);
		}
	}

	public void initProposalAdapter(){		
		initProposalAdapterFacture();
		initProposalAdapterAvoir();
		initProposalAdapterApporteur();
//		initProposalAdapterAcompte();
		initProposalAdapterReglement();
		initProposalAdapterRemises();
//		initBornes();
	}
	public void initProposalAdapterFacture(){
		listeDocumentsFactures=factureAutoCompleteDTOLight("");
		rempliVentes=!listeDocumentsFactures.isEmpty();
		initBornes(TaFacture.TYPE_DOC);
		initBornesDates(TaFacture.TYPE_DOC, rempliVentes);
	}
	public void initProposalAdapterAvoir(){
		listeDocumentsAvoirs=avoirAutoCompleteDTOLight("");
		rempliAvoirs=!listeDocumentsAvoirs.isEmpty();
		initBornes(TaAvoir.TYPE_DOC);
		initBornesDates(TaAvoir.TYPE_DOC, rempliAvoirs);
	}
	public void initProposalAdapterApporteur(){
		listeDocumentsApporteurs=apporteurAutoCompleteDTOLight("");
		rempliApporteur=!listeDocumentsApporteurs.isEmpty() && exporterApporteur;
		initBornes(TaApporteur.TYPE_DOC);
		initBornesDates(TaApporteur.TYPE_DOC, rempliApporteur);
	}
//	public void initProposalAdapterAcompte(){
//		listeDocumentsAcomptes=acompteAutoCompleteDTOLight("");
//		rempliAcomptes=!listeDocumentsAcomptes.isEmpty();
//		initBornes(TaAcompte.TYPE_DOC);
//		initBornesDates(TaAcompte.TYPE_DOC, rempliAcomptes);
//	}
	public void initProposalAdapterReglement(){
		listeDocumentsReglements=reglementAutoCompleteDTOLight("");
		rempliReglement=!listeDocumentsReglements.isEmpty();
		initBornes(TaReglement.TYPE_DOC);
		initBornesDates(TaReglement.TYPE_DOC, rempliReglement);
	}
	public void initProposalAdapterRemises(){		
		listeDocumentsRemises=remiseAutoCompleteDTOLight("");
		rempliRemise=!listeDocumentsRemises.isEmpty();
		initBornes(TaRemise.TYPE_DOC);
		initBornesDates(TaRemise.TYPE_DOC, rempliRemise);
	}
	
	public List<TaFactureDTO> factureAutoCompleteDTOLight(String query) {
		TaInfoEntreprise infos=taInfoEntrepriseService.findInstance();
		List<TaFactureDTO> allValues = new ArrayList<TaFactureDTO>();
		if(reexporterFacture==false)allValues= taFactureService.rechercheDocumentNonExporteLight(infos.getDatedebInfoEntreprise(),infos.getDatefinInfoEntreprise(),false);
		else {
			List<Object[]> liste=taFactureService.rechercheDocumentLight(infos.getDatedebInfoEntreprise(),infos.getDatefinInfoEntreprise(),"%");
			for (Object[] objects : liste) {
				TaFactureDTO dto=new TaFactureDTO();
				dto.setId((Integer) objects[0]);
				dto.setCodeDocument((String) objects[1]);
				dto.setDateDocument((Date) objects[2]);
				dto.setLibelleDocument((String) objects[3]);
				dto.setCodeTiers((String) objects[4]);
				dto.setNomTiers((String) objects[5]);
				dto.setDateEchDocument((Date) objects[6]);
				dto.setDateExport(((Date) objects[7]));
				dto.setNetHtCalc((BigDecimal) objects[8]);
				dto.setNetTtcCalc((BigDecimal) objects[9]);
		
				allValues.add(dto);
			}
		}
		List<TaFactureDTO> filteredValues = new ArrayList<TaFactureDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			TaFactureDTO civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeDocument().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	
	public List<TaAvoirDTO> avoirAutoCompleteDTOLight(String query) {
		TaInfoEntreprise infos=taInfoEntrepriseService.findInstance();
		List<TaAvoirDTO> allValues = new ArrayList<TaAvoirDTO>(); 
		if(reexporterFactureAvoir==false)allValues= taAvoirService.rechercheDocumentNonExporteLight(infos.getDatedebInfoEntreprise(),infos.getDatefinInfoEntreprise(),false);
		else {
			List<Object[]> liste=taAvoirService.rechercheDocumentLight(infos.getDatedebInfoEntreprise(),infos.getDatefinInfoEntreprise(),"%");
			for (Object[] objects : liste) {
				TaAvoirDTO dto=new TaAvoirDTO();
				dto.setId((Integer) objects[0]);
				dto.setCodeDocument((String) objects[1]);
				dto.setDateDocument((Date) objects[2]);
				dto.setLibelleDocument((String) objects[3]);
				dto.setCodeTiers((String) objects[4]);
				dto.setNomTiers((String) objects[5]);
				dto.setDateEchDocument((Date) objects[6]);
				dto.setDateExport(((Date) objects[7]));
				dto.setNetHtCalc((BigDecimal) objects[8]);
				dto.setNetTtcCalc((BigDecimal) objects[9]);
		
				allValues.add(dto);
			}
		}
		List<TaAvoirDTO> filteredValues = new ArrayList<TaAvoirDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			TaAvoirDTO civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeDocument().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	
	public List<TaApporteurDTO> apporteurAutoCompleteDTOLight(String query) {
		TaInfoEntreprise infos=taInfoEntrepriseService.findInstance();
		List<TaApporteurDTO> allValues=new ArrayList<TaApporteurDTO>();
		if(reexporterFactureApporteur==false)allValues= taApporteurService.rechercheDocumentNonExporteLight(infos.getDatedebInfoEntreprise(),infos.getDatefinInfoEntreprise(),false);
		else {
			List<Object[]> liste=taApporteurService.rechercheDocumentLight(infos.getDatedebInfoEntreprise(),infos.getDatefinInfoEntreprise(),"%");
			for (Object[] objects : liste) {
				TaApporteurDTO dto=new TaApporteurDTO();
				dto.setId((Integer) objects[0]);
				dto.setCodeDocument((String) objects[1]);
				dto.setDateDocument((Date) objects[2]);
				dto.setLibelleDocument((String) objects[3]);
				dto.setCodeTiers((String) objects[4]);
				dto.setNomTiers((String) objects[5]);
				dto.setDateEchDocument((Date) objects[6]);
				dto.setDateExport(((Date) objects[7]));
				dto.setNetHtCalc((BigDecimal) objects[8]);
				dto.setNetTtcCalc((BigDecimal) objects[9]);
		
				allValues.add(dto);
			}
		}
		List<TaApporteurDTO> filteredValues = new ArrayList<TaApporteurDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			TaApporteurDTO civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeDocument().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
//	public List<TaAcompteDTO> acompteAutoCompleteDTOLight(String query) {
//		TaInfoEntreprise infos=taInfoEntrepriseService.findInstance();
//		List<TaAcompteDTO> allValues = new ArrayList<TaAcompteDTO>();
//		if(reexporterAcompte==false)allValues= taAcompteService.rechercheDocumentNonExporteLight(infos.getDatedebInfoEntreprise(),infos.getDatefinInfoEntreprise(),false);
//		else {
//			List<Object[]> liste=taAcompteService.rechercheDocumentLight(infos.getDatedebInfoEntreprise(),infos.getDatefinInfoEntreprise(),"%");
//			for (Object[] objects : liste) {
//				TaAcompteDTO dto=new TaAcompteDTO();
//				dto.setId((Integer) objects[0]);
//				dto.setCodeDocument((String) objects[1]);
//				dto.setDateDocument((Date) objects[2]);
//				dto.setLibelleDocument((String) objects[3]);
//				dto.setCodeTiers((String) objects[4]);
//				dto.setNomTiers((String) objects[5]);
//				dto.setDateEchDocument((Date) objects[6]);
//				dto.setExport(LibConversion.intToBoolean(((Integer) objects[7])));
//				dto.setNetHtCalc((BigDecimal) objects[8]);
//				dto.setNetTtcCalc((BigDecimal) objects[9]);
//		
//				allValues.add(dto);
//			}
//		}
//		List<TaAcompteDTO> filteredValues = new ArrayList<TaAcompteDTO>();
//
//		for (int i = 0; i < allValues.size(); i++) {
//			TaAcompteDTO civ = allValues.get(i);
//			if(query.equals("*") || civ.getCodeDocument().toLowerCase().contains(query.toLowerCase())) {
//				filteredValues.add(civ);
//			}
//		}
//
//		return filteredValues;
//	}
	
	public List<TaRemiseDTO> remiseAutoCompleteDTOLight(String query) {
		TaInfoEntreprise infos=taInfoEntrepriseService.findInstance();
		List<TaRemiseDTO> allValues;
		if(reexporterRemise==false)allValues= taRemiseService.rechercheDocumentNonExporteDTO(infos.getDatedebInfoEntreprise(),infos.getDatefinInfoEntreprise(),false);
		else allValues= taRemiseService.rechercheDocumentDTO(infos.getDatedebInfoEntreprise(),infos.getDatefinInfoEntreprise());
		List<TaRemiseDTO> filteredValues = new ArrayList<TaRemiseDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			TaRemiseDTO civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeDocument().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	
	
	public List<TaReglementDTO> reglementAutoCompleteDTOLight(String query) {
		TaInfoEntreprise infos=taInfoEntrepriseService.findInstance();
		List<TaReglementDTO> allValues = new ArrayList<TaReglementDTO>();
		if(reexporterReglement==false)allValues = taReglementService.rechercheDocumentNonExporteLight(infos.getDatedebInfoEntreprise(),infos.getDatefinInfoEntreprise(),false);
		else 
			allValues=taReglementService.rechercheDocumentDTO(infos.getDatedebInfoEntreprise(),infos.getDatefinInfoEntreprise(),"%");
		//			for (Object[] objects : liste) {
		//				TaReglementDTO dto=new TaReglementDTO();
		//				dto.setId((Integer) objects[0]);
		//				dto.setCodeDocument((String) objects[1]);
		//				dto.setDateDocument((Date) objects[2]);
		//				dto.setLibelleDocument((String) objects[3]);
		//				dto.setCodeTiers((String) objects[4]);
		//				dto.setNomTiers((String) objects[5]);
		//				dto.setDateExport(((Date) objects[6]));
		//				dto.setNetTtcCalc((BigDecimal) objects[7]);
		//		
		//				allValues.add(dto);
		//			}

		List<TaReglementDTO> filteredValues = new ArrayList<TaReglementDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			TaReglementDTO civ = allValues.get(i);
			if(civ.getCodeRemise()==null) {
				if(query.equals("*") || civ.getCodeDocument().toLowerCase().contains(query.toLowerCase())) {
					filteredValues.add(civ);
				}
			}
		}

		return filteredValues;
	}
	
	public boolean initBoutonBornes(){
		return (listeDocumentsFactures!=null&&listeDocumentsFactures.size()>0) || 
				(listeDocumentsAvoirs!=null&&listeDocumentsAvoirs.size()>0) ||
				(listeDocumentsApporteurs!=null&&listeDocumentsApporteurs.size()>0) ||
//				(listeDocumentsAcomptes!=null&&listeDocumentsAcomptes.size()>0)	 || 
				(listeDocumentsReglements!=null&&listeDocumentsReglements.size()>0) ||
				(listeDocumentsRemises!=null&&listeDocumentsRemises.size()>0);
	}
	public boolean initBoutonReglement(){
//		return 	exporterAcompte||exporterReglementSimple||exporterRemise;
		return 	exporterReglementSimple||exporterRemise;
	}
	
	public boolean isExporterDocumentLies() {
		return exporterDocumentLies;
	}

	public void setExporterDocumentLies(boolean exporterDocumentLies) {
		this.exporterDocumentLies = exporterDocumentLies;
	}

	public void DeclencheInitBorneController() {
		try {
			initProposalAdapter();
			initBoutonBornes();
			initBoutonReglement();
		} catch (Exception e) {
//			logger.error("",e);
		} finally {
		}
	}

	public boolean isExporterReglementTout() {
		return exporterReglementTout;
	}

	public void setExporterReglementTout(boolean exporterReglementTout) {
		this.exporterReglementTout = exporterReglementTout;
	}

public void enregistreOptionSurEcran(){
//	PreferencesService.mergePreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION, ITaPreferencesServiceRemote.ACOMPTES,exporterAcompte);
	PreferencesService.mergePreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION, ITaPreferencesServiceRemote.DOCUMENTS_LIES,exporterDocumentLies);
	PreferencesService.mergePreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION, ITaPreferencesServiceRemote.POINTAGES,exporterAvecPointage);
	PreferencesService.mergePreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION, ITaPreferencesServiceRemote.REGLEMENT_SIMPLE,exporterReglementSimple);
	PreferencesService.mergePreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION, ITaPreferencesServiceRemote.REGLEMENTS_LIES,exporterReglementLies);
	PreferencesService.mergePreferenceBoolean(ITaPreferencesServiceRemote.EXPORTATION, ITaPreferencesServiceRemote.REMISE,exporterRemise);
}

public boolean isRempliVentes() {
	return rempliVentes;
}

public void setRempliVentes(boolean rempliVentes) {
	this.rempliVentes = rempliVentes;
}

public boolean isRempliAvoirs() {
	return rempliAvoirs;
}

public void setRempliAvoirs(boolean rempliAvoirs) {
	this.rempliAvoirs = rempliAvoirs;
}

//public boolean isRempliAcomptes() {
//	return rempliAcomptes;
//}
//
//public void setRempliAcomptes(boolean rempliAcomptes) {
//	this.rempliAcomptes = rempliAcomptes;
//}

public boolean isRempliApporteur() {
	return rempliApporteur;
}

public void setRempliApporteur(boolean rempliApporteur) {
	this.rempliApporteur = rempliApporteur;
}

public boolean isRempliReglement() {
	return rempliReglement;
}

public void setRempliReglement(boolean rempliReglement) {
	this.rempliReglement = rempliReglement;
}

public boolean isRempliRemise() {
	return rempliRemise;
}

public void setRempliRemise(boolean rempliRemise) {
	this.rempliRemise = rempliRemise;
}

private void initBornes(){
	initBornesVente();
	initBornesTousReglements();
	initBornesDatesVentes();
	initBornesDatesReglement();
}

private void initBornesVente(){
	initBornes(TaFacture.TYPE_DOC);
	initBornes(TaAvoir.TYPE_DOC);
	initBornes(TaApporteur.TYPE_DOC);
}
private void initBornesTousReglements(){
//	initBornes(TaAcompte.TYPE_DOC);
	initBornes(TaReglement.TYPE_DOC);
	initBornes(TaRemise.TYPE_DOC);
}

public void initBornesFacture(){
	initBornes(TaFacture.TYPE_DOC);
	initBornesDates(TaFacture.TYPE_DOC,rempliVentes);
}
public void initBornesAvoir(){
	initBornes(TaAvoir.TYPE_DOC);
	initBornesDates(TaAvoir.TYPE_DOC,rempliAvoirs);
}
public void initBornesApporteur(){
	initBornes(TaApporteur.TYPE_DOC);
	initBornesDates(TaApporteur.TYPE_DOC,rempliApporteur);
}
//public void initBornesAcompte(){
//	initBornes(TaAcompte.TYPE_DOC);
//	initBornesDates(TaAcompte.TYPE_DOC,rempliAcomptes);
//}
public void initBornesReglement(){
	initBornes(TaReglement.TYPE_DOC);
	initBornesDates(TaReglement.TYPE_DOC,rempliReglement);
}
public void initBornesRemise(){
	initBornes(TaRemise.TYPE_DOC);
	initBornesDates(TaRemise.TYPE_DOC,rempliRemise);
}

public void initBornes(String type){
	if(type.equals(TaFacture.TYPE_DOC)){
		debFacture="";
		finFacture="";
		if(rempliVentes){
			rempliVentes= listeDocumentsFactures!=null &&listeDocumentsFactures.size()>0;
		}
	}
	if(type.equals(TaAvoir.TYPE_DOC)){
		debAvoir="";
		finAvoir="";
		if(rempliAvoirs)
			rempliAvoirs= listeDocumentsAvoirs!=null &&listeDocumentsAvoirs.size()>0;
	}
	if(type.equals(TaApporteur.TYPE_DOC)){
		debApporteur="";
		finApporteur="";
		if(rempliApporteur)
			rempliApporteur= listeDocumentsApporteurs!=null &&listeDocumentsApporteurs.size()>0;
	}
	//reglements
//	if(type.equals(TaAcompte.TYPE_DOC)){
//		debAcompte="";
//		finAcompte="";
//		if(rempliAcomptes)
//			rempliAcomptes= listeDocumentsAcomptes!=null &&listeDocumentsAcomptes.size()>0;
//	}
	if(type.equals(TaReglement.TYPE_DOC)){
		debReglement="";
		finReglement="";
		if(rempliReglement)
			rempliReglement= listeDocumentsReglements!=null &&listeDocumentsReglements.size()>0;
	}
	if(type.equals(TaRemise.TYPE_DOC)){
		debRemise="";
		finRemise="";
		if(rempliRemise)
			rempliRemise= listeDocumentsRemises!=null &&listeDocumentsRemises.size()>0;
	}

	if(type.equals(TaFacture.TYPE_DOC)){
		if(rempliVentes){
			if(listeDocumentsFactures!=null &&listeDocumentsFactures.size()>0){
				debFacture=listeDocumentsFactures.get(0).getCodeDocument();
				finFacture=listeDocumentsFactures.get(listeDocumentsFactures.size()-1).getCodeDocument();
			}
		}
	}
	if(type.equals(TaAvoir.TYPE_DOC)){
		if(rempliAvoirs){
			if(listeDocumentsAvoirs!=null &&listeDocumentsAvoirs.size()>0){
				debAvoir=listeDocumentsAvoirs.get(0).getCodeDocument();
				finAvoir=listeDocumentsAvoirs.get(listeDocumentsAvoirs.size()-1).getCodeDocument();
			}
		}
	}
	if(type.equals(TaApporteur.TYPE_DOC)){
		if(rempliApporteur){
			if(listeDocumentsApporteurs!=null &&listeDocumentsApporteurs.size()>0){
				debApporteur=listeDocumentsApporteurs.get(0).getCodeDocument();
				finApporteur=listeDocumentsApporteurs.get(listeDocumentsApporteurs.size()-1).getCodeDocument();
			}
		}
	}	
	if(type.equals(TaReglement.TYPE_DOC)){
		if(rempliReglement){
			if(listeDocumentsReglements!=null &&listeDocumentsReglements.size()>0){
				debReglement=listeDocumentsReglements.get(0).getCodeDocument();
				finReglement=listeDocumentsReglements.get(listeDocumentsReglements.size()-1).getCodeDocument();
			}
		}
	}
//	if(type.equals(TaAcompte.TYPE_DOC)){
//		if(rempliAcomptes){
//			if(listeDocumentsAcomptes!=null &&listeDocumentsAcomptes.size()>0){
//				debAcompte=listeDocumentsAcomptes.get(0).getCodeDocument();
//				finAcompte=listeDocumentsAcomptes.get(listeDocumentsAcomptes.size()-1).getCodeDocument();
//			}
//		}
//	}
	if(type.equals(TaRemise.TYPE_DOC)){
		if(rempliRemise){
			if(listeDocumentsRemises!=null &&listeDocumentsRemises.size()>0){
				debRemise=listeDocumentsRemises.get(0).getCodeDocument();
				finRemise=listeDocumentsRemises.get(listeDocumentsRemises.size()-1).getCodeDocument();
			}
		}
	}
}

public ITaPreferencesServiceRemote getPreferencesService() {
	return PreferencesService;
}

//public String getLibAcompte() {
//	return libAcompte;
//}
//
//public void setLibAcompte(String libAcompte) {
//	this.libAcompte = libAcompte;
//}

public String getLibSimpleReglement() {
	return libSimpleReglement;
}

public void setLibSimpleReglement(String libSimpleReglement) {
	this.libSimpleReglement = libSimpleReglement;
}

public String getLibRemise() {
	return libRemise;
}

public void setLibRemise(String libRemise) {
	this.libRemise = libRemise;
}

public String getLibApporteur() {
	return libApporteur;
}

public void setLibApporteur(String libApporteur) {
	this.libApporteur = libApporteur;
}

public String getLibCentraliserEcritures() {
	return libCentraliserEcritures;
}

public void setLibCentraliserEcritures(String libCentraliserEcritures) {
	this.libCentraliserEcritures = libCentraliserEcritures;
}

public String getLibDocumentsLies() {
	return libDocumentsLies;
}

public void setLibDocumentsLies(String libDocumentsLies) {
	this.libDocumentsLies = libDocumentsLies;
}

public String getLibReglementsLies() {
	return libReglementsLies;
}

public void setLibReglementsLies(String libReglementsLies) {
	this.libReglementsLies = libReglementsLies;
}

public String getLibPointages() {
	return libPointages;
}

public void setLibPointages(String libPointages) {
	this.libPointages = libPointages;
}

public String getTypeFacture() {
	return typeFacture;
}

public void setTypeFacture(String typeFacture) {
	this.typeFacture = typeFacture;
}

public String getTypeAvoir() {
	return typeAvoir;
}

public void setTypeAvoir(String typeAvoir) {
	this.typeAvoir = typeAvoir;
}

//public String getTypeAcompte() {
//	return typeAcompte;
//}
//
//public void setTypeAcompte(String typeAcompte) {
//	this.typeAcompte = typeAcompte;
//}

public String getTypeApporteur() {
	return typeApporteur;
}

public void setTypeApporteur(String typeApporteur) {
	this.typeApporteur = typeApporteur;
}

public String getTypeReglement() {
	return typeReglement;
}

public void setTypeReglement(String typeReglement) {
	this.typeReglement = typeReglement;
}

public String getTypeRemise() {
	return typeRemise;
}

public void setTypeRemise(String typeRemise) {
	this.typeRemise = typeRemise;
}


private void initBornesDatesVentes(){
	initBornesDates(TaFacture.TYPE_DOC,rempliVentes);
	initBornesDates(TaAvoir.TYPE_DOC,rempliAvoirs);
	initBornesDates(TaApporteur.TYPE_DOC,rempliApporteur);
}

private void initBornesDatesReglement(){
	initBornesDates(TaReglement.TYPE_DOC,rempliReglement);
//	initBornesDates(TaAcompte.TYPE_DOC,rempliAcomptes);
	initBornesDates(TaRemise.TYPE_DOC,rempliRemise);
}

public void initBornesDatesFacture(){
	initBornesDates(TaFacture.TYPE_DOC,rempliVentes);
}
public void initBornesDatesAvoir(){
	initBornesDates(TaAvoir.TYPE_DOC,rempliAvoirs);
}
public void initBornesDatesApporteur(){
	initBornesDates(TaApporteur.TYPE_DOC,rempliApporteur);
}
public void initBornesDatesReglementSimple(){
	initBornesDates(TaReglement.TYPE_DOC,rempliReglement);
}
//public void initBornesDatesAcompte(){
//	initBornesDates(TaAcompte.TYPE_DOC,rempliAcomptes);
//}
public void initBornesDatesRemise(){
	initBornesDates(TaRemise.TYPE_DOC,rempliRemise);
}

private void initBornesDates(String type,boolean selection){
if(type.equals(TaFacture.TYPE_DOC)){
		rempliVentes= selection && listeDocumentsFactures!=null && !listeDocumentsFactures.isEmpty();
	}
	if(type.equals(TaAvoir.TYPE_DOC)){
		rempliAvoirs= selection && listeDocumentsAvoirs!=null && !listeDocumentsAvoirs.isEmpty();
	}
	if(type.equals(TaApporteur.TYPE_DOC)){
		rempliApporteur= selection && listeDocumentsApporteurs!=null && !listeDocumentsApporteurs.isEmpty();
	}		
	if(type.equals(TaFacture.TYPE_DOC)||type.equals(TaAvoir.TYPE_DOC)||type.equals(TaApporteur.TYPE_DOC)){
		debDateVente=null;
		finDateVente=null;
	}
//	if(type.equals(TaAcompte.TYPE_DOC)||type.equals(TaReglement.TYPE_DOC)||type.equals(TaRemise.TYPE_DOC)){
	if(type.equals(TaReglement.TYPE_DOC)||type.equals(TaRemise.TYPE_DOC)){
		debDateReglement=null;
		finDateReglement=null;
	}
	
//	if(type.equals(TaAcompte.TYPE_DOC)){
//		rempliAcomptes= selection && listeDocumentsAcomptes!=null && !listeDocumentsAcomptes.isEmpty();
//	}
	if(type.equals(TaReglement.TYPE_DOC)){
		rempliReglement= selection && listeDocumentsReglements!=null && !listeDocumentsReglements.isEmpty();
	}
	if(type.equals(TaRemise.TYPE_DOC)){
		rempliRemise= selection && listeDocumentsRemises!=null && !listeDocumentsRemises.isEmpty();
	}
	TaInfoEntreprise infos=taInfoEntrepriseService.findInstance();
	Date dateInit=infos.getDatefinInfoEntreprise();
	Date dateFacture=dateInit;
	Date dateAvoir = dateInit;
	Date dateApporteur = dateInit;
	Date dateAcompte=dateInit;
	Date dateReglement=dateInit;
	Date dateRemise=dateInit;
	boolean VentesVisible=false;
	boolean reglementVisible=false;
	
	if(type.equals(TaFacture.TYPE_DOC)||type.equals(TaAvoir.TYPE_DOC)||type.equals(TaApporteur.TYPE_DOC)){
		if(listeDocumentsFactures!=null && listeDocumentsFactures.size()>0){
			dateFacture=taFactureService.selectMinDateDocumentNonExporte(infos.getDatedebInfoEntreprise(), infos.getDatefinInfoEntreprise());
//			dateFacture=listeDocumentsFactures.get(0).getDateDocument();
			VentesVisible=true;
		}
		if(listeDocumentsAvoirs!=null && listeDocumentsAvoirs.size()>0){
			dateAvoir=taAvoirService.selectMinDateDocumentNonExporte(infos.getDatedebInfoEntreprise(), infos.getDatefinInfoEntreprise());
//			dateAvoir=listeDocumentsAvoirs.get(0).getDateDocument();
			VentesVisible=true;
		}
		if(listeDocumentsApporteurs!=null && listeDocumentsApporteurs.size()>0){
			dateApporteur=taApporteurService.selectMinDateDocumentNonExporte(infos.getDatedebInfoEntreprise(), infos.getDatefinInfoEntreprise());
//			dateApporteur=listeDocumentsApporteurs.get(0).getDateDocument();
			VentesVisible=true;
		}			
		if(VentesVisible){
			if(rempliVentes) debDateVente=dateFacture; 
			if(rempliAvoirs) debDateVente=dateAvoir;
			if(rempliApporteur) debDateVente=dateApporteur;
			List<Date> listeDate=new ArrayList<Date>();
			if(dateAvoir!=null && rempliAvoirs)listeDate.add(dateAvoir);
			if(dateFacture!=null && rempliVentes)listeDate.add(dateFacture);
			if(dateApporteur!=null && rempliApporteur)listeDate.add(dateApporteur);
			Collections.sort(listeDate);
			if(!listeDate.isEmpty()){
				debDateVente=listeDate.get(0);
			}
				
			if(debDateVente!=null) finDateVente=infos.getDatefinInfoEntreprise();
		}
	}
	
//	if(type.equals(TaAcompte.TYPE_DOC)||type.equals(TaReglement.TYPE_DOC)||type.equals(TaRemise.TYPE_DOC)){
	if(type.equals(type.equals(TaReglement.TYPE_DOC))||type.equals(TaRemise.TYPE_DOC)){
//	if(listeDocumentsAcomptes!=null && listeDocumentsAcomptes.size()>0){
//		dateAcompte=listeDocumentsAcomptes.get(0).getDateDocument();
//		reglementVisible=true;
//	}
	if(listeDocumentsReglements!=null && listeDocumentsReglements.size()>0){
		dateReglement=taReglementService.selectMinDateDocumentNonExporte(infos.getDatedebInfoEntreprise(), infos.getDatefinInfoEntreprise());
//		dateReglement=listeDocumentsReglements.get(0).getDateDocument();
		reglementVisible=true;
	}
	if(listeDocumentsRemises!=null && listeDocumentsRemises.size()>0){
		dateRemise=taRemiseService.selectMinDateDocumentNonExporte(infos.getDatedebInfoEntreprise(), infos.getDatefinInfoEntreprise());
//		dateRemise=listeDocumentsRemises.get(0).getDateDocument();
		reglementVisible=true;
	}
	if(reglementVisible){
//		if(rempliAcomptes) debDateReglement=dateAcompte;
		if(rempliReglement) debDateReglement=dateReglement;
		if(rempliRemise) debDateReglement=dateRemise;

		List<Date> listeDate=new ArrayList<Date>();
//		if(dateAcompte!=null && rempliAcomptes)listeDate.add(dateAcompte);
		if(dateReglement!=null && rempliReglement)listeDate.add(dateReglement);	
		if(dateRemise!=null && rempliRemise)listeDate.add(dateRemise);
		Collections.sort(listeDate);
		if(!listeDate.isEmpty()){
			debDateReglement=listeDate.get(0);
		}
	if(debDateReglement!=null) finDateReglement=infos.getDatefinInfoEntreprise();
	}
	}
	
//	if(debDateVente==null)debDateVente=new Date();
//	if(finDateVente==null)finDateVente=new Date();
//	if(debDateReglement==null)debDateReglement=new Date();
//	if(finDateReglement==null)finDateReglement=new Date();
}

public void onTabChange(TabChangeEvent event) {
	tabCourant=event.getTab().getId();
	if(tabOld!=null && tabOld.equals(ID_TAB_LISTE)){
		//mettre à jour les autres écran;
		actRefresh();
	}
	tabOld=event.getTab().getId();
}



public void cocheTousLesCheck(){
	initTousLesCheck(toutReexporter);
	initProposalAdapter();
}

public void initTousLesCheck(boolean valeur){
//	reexporterAcompte=valeur;
	reexporterFactureAvoir=valeur;
	reexporterFactureApporteur=valeur;
	reexporterReglement=valeur;
	reexporterRemise=valeur;
	reexporterFacture=valeur;	
}






public boolean isReexporterFacture() {
	return reexporterFacture;
}

public void setReexporterFacture(boolean reexporterFacture) {
	this.reexporterFacture = reexporterFacture;
}

public boolean isReexporterFactureAvoir() {
	return reexporterFactureAvoir;
}

public void setReexporterFactureAvoir(boolean reexporterFactureAvoir) {
	this.reexporterFactureAvoir = reexporterFactureAvoir;
}

public boolean isReexporterFactureApporteur() {
	return reexporterFactureApporteur;
}

public void setReexporterFactureApporteur(boolean reexporterFactureApporteur) {
	this.reexporterFactureApporteur = reexporterFactureApporteur;
}

//public boolean isReexporterAcompte() {
//	return reexporterAcompte;
//}
//
//public void setReexporterAcompte(boolean reexporterAcompte) {
//	this.reexporterAcompte = reexporterAcompte;
//}

public boolean isReexporterReglement() {
	return reexporterReglement;
}

public void setReexporterReglement(boolean reexporterReglement) {
	this.reexporterReglement = reexporterReglement;
}

public boolean isReexporterRemise() {
	return reexporterRemise;
}

public void setReexporterRemise(boolean reexporterRemise) {
	this.reexporterRemise = reexporterRemise;
}

public boolean isToutReexporter() {
	return toutReexporter;
}

public void setToutReexporter(boolean toutReexporter) {
	this.toutReexporter = toutReexporter;
}

public IDocumentTiers[] getSelection() {
	return selection;
}

public void setSelection(IDocumentTiers[] selection) {
	this.selection = selection;
}

public boolean isEcraserFichier() {
	return ecraserFichier;
}

public void setEcraserFichier(boolean ecraserFichier) {
	this.ecraserFichier = ecraserFichier;
}

public List<IDocumentTiers> getListeDocumentsManquantsLocal() {
	return listeDocumentsManquantsLocal;
}

public void setListeDocumentsManquantsLocal(List<IDocumentTiers> listeDocumentsManquantsLocal) {
	this.listeDocumentsManquantsLocal = listeDocumentsManquantsLocal;
}

public IDocumentTiers[] getSelection2() {
	return selection2;
}

public void setSelection2(IDocumentTiers[] selection2) {
	this.selection2 = selection2;
}

public EtatExportationDocumentBean getEtatExportationDocumentBean() {
	return etatExportationDocumentBean;
}

public void setEtatExportationDocumentBean(EtatExportationDocumentBean etatExportationDocumentBean) {
	this.etatExportationDocumentBean = etatExportationDocumentBean;
}

public StreamedContent getFileImportAgrigest() {
	return fileImportAgrigest;
}



public void reset() {
	listeDocumentsAvoirs.clear();
	listeDocumentsApporteurs.clear();
	listeDocumentsFactures.clear();
	listeDocumentsManquantsLocal.clear();
	listeDocumentsReglements.clear();
	listeDocumentsRemises.clear();
	init();
}

public void actDialogEmailComptable(ActionEvent actionEvent) {
    
    Map<String,Object> options = new HashMap<String, Object>();
    options.put("modal", true);
    options.put("draggable", false);
    options.put("closable", false);
    options.put("resizable", true);
    options.put("contentHeight", 710);
    options.put("contentWidth", "100%");
    //options.put("contentHeight", "100%");
    options.put("width", 1024);
    
    //Map<String,List<String>> params = null;
    Map<String,List<String>> params = new HashMap<String,List<String>>();
    List<String> list = new ArrayList<String>();
    list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
    params.put("modeEcranDefaut", list);
    
    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	Map<String, Object> sessionMap = externalContext.getSessionMap();
	
	TaEmail emailComptable = new TaEmail();
	emailComptable.setAdresseEmail(PreferencesService.retourPreferenceString(ITaPreferencesServiceRemote.EXPORTATION,ITaPreferencesServiceRemote.EMAIL_ADRESSE_COMPTABLE_DEFAUT));
	EmailParam email = new EmailParam();
	email.setEmailExpediteur(null);
	email.setNomExpediteur(taInfoEntrepriseService.findInstance().getNomInfoEntreprise()); 
	email.setSubject(taInfoEntrepriseService.findInstance().getNomInfoEntreprise()+" Exportation"); 
	email.setBodyPlain("fichier exportation comptabilité de la société : "+taInfoEntrepriseService.findInstance().getNomInfoEntreprise());
	email.setBodyHtml("fichier exportation comptabilité de la société : "+taInfoEntrepriseService.findInstance().getNomInfoEntreprise());
	email.setDestinataires(new String[]{emailComptable.getAdresseEmail()});
	email.setEmailDestinataires(new TaEmail[]{emailComptable});
	
	EmailPieceJointeParam pj1 = new EmailPieceJointeParam();
	pj1.setFichier(new File(exportationEpicea.getFichierExportationServeur()));
	pj1.setTypeMime("pdf");
	pj1.setNomFichier(pj1.getFichier().getName());
	email.setPiecesJointes(
			new EmailPieceJointeParam[]{pj1}
			);
	
	sessionMap.put(EmailParam.NOM_OBJET_EN_SESSION, email);
    
    PrimeFaces.current().dialog().openDynamic("/dialog_email", options, params);    
}

public void handleReturnDialogEmailComptable(SelectEvent event) {
	FacesContext context = FacesContext.getCurrentInstance();  

	if(event!=null && event.getObject()!=null) {
		//List<ControleConformiteJSF> listeControle = (List<ControleConformiteJSF>) event.getObject();
		TaMessageEmail j = (TaMessageEmail) event.getObject();
//		if(j!=null && j.isExpedie())	context.addMessage(null, new FacesMessage("Email","Email envoyée ")); 
		if(j!=null)	context.addMessage(null, new FacesMessage("Email","Email envoyée ")); 
		else context.addMessage(null, new FacesMessage("Email","Echec de l'envoi de l'email ")); 
	}
}
}

