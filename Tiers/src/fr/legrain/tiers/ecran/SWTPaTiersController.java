package fr.legrain.tiers.ecran;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.FinderException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.persistence.EntityManager;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tiers.service.remote.ITaAdresseServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaCPaiementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaComplServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaDocumentTiersServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaEmailServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaEntrepriseServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTAdrServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTCPaiementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTCiviliteServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTEmailServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTEntiteServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTPaiementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTTarifServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTTelServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTTiersServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTTvaDocServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTWebServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTelephoneServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaWebServiceRemote;
import fr.legrain.document.dto.TaTPaiementDTO;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.gestCom.Appli.Const;
//import fr.legrain.gestCom.Appli.LgrDozerMapper;
import fr.legrain.gestCom.Appli.ModelGeneralObjetEJB;
import fr.legrain.gestCom.Module_Tiers.ModelTiers;
import fr.legrain.gestCom.gestComBd.gestComBdPlugin;
import fr.legrain.gestCom.librairiesEcran.LibrairiesEcranPlugin;
import fr.legrain.gestCom.librairiesEcran.editor.EditorAide;
import fr.legrain.gestCom.librairiesEcran.editor.EditorInputAide;
import fr.legrain.gestCom.librairiesEcran.swt.EJBBaseControllerSWTStandard;
import fr.legrain.gestCom.librairiesEcran.swt.ILgrListView;
import fr.legrain.gestCom.librairiesEcran.swt.LgrAbstractHandler;
import fr.legrain.gestCom.librairiesEcran.swt.LgrShellUtil;
import fr.legrain.gestCom.librairiesEcran.swt.ParamAfficheAideRechercheSWT;
import fr.legrain.gestCom.librairiesEcran.swt.ParamAfficheAideSWT;
import fr.legrain.gestCom.librairiesEcran.swt.ParamAfficheVisualisation;
import fr.legrain.gestCom.librairiesEcran.swt.SWTPaAideControllerSWT;
import fr.legrain.gestCom.librairiesEcran.swt.SWTPaAideRechercheControllerSWT;
import fr.legrain.gestCom.librairiesEcran.workbench.AnnuleToutEvent;
import fr.legrain.gestCom.librairiesEcran.workbench.ChangementDePageEvent;
import fr.legrain.gestCom.librairiesEcran.workbench.ChangementDeSelectionEvent;
import fr.legrain.gestCom.librairiesEcran.workbench.ChangementMasterEntityEvent;
import fr.legrain.gestCom.librairiesEcran.workbench.EJBLgrEditorPart;
import fr.legrain.gestCom.librairiesEcran.workbench.IChangementMasterEntityListener;
import fr.legrain.gestCom.librairiesEcran.workbench.LgrFileBundleLocator;
import fr.legrain.gestCom.librairiesEcran.workbench.LgrPartListener;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.lib.data.InfosVerifSaisie;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;
import fr.legrain.lib.data.VerrouInterface;
import fr.legrain.lib.ejb.EJBLookup;
import fr.legrain.lib.gui.ParamAffiche;
import fr.legrain.lib.gui.ResultAffiche;
import fr.legrain.lib.gui.RetourEcranEvent;
import fr.legrain.lib.gui.RetourEcranListener;
import fr.legrain.lib.gui.aide.PaAideRechercheSWT;
import fr.legrain.lib.gui.aide.PaAideSWT;
import fr.legrain.lib.gui.aide.ParamAfficheAide;
import fr.legrain.lib.gui.aide.ResultAide;
import fr.legrain.lib.validator.AbstractApplicationDAOClient;
import fr.legrain.lib.windows.WinRegistry;
import fr.legrain.libMessageLGR.LgrMess;
import fr.legrain.openmail.mail.OpenmailFAX;
import fr.legrain.openmail.mail.OpenmailMail;
import fr.legrain.openmail.sms.OpenmailSMS;
import fr.legrain.publipostage.divers.ParamPublipostage;
import fr.legrain.publipostage.divers.TypeVersionPublipostage;
import fr.legrain.publipostage.msword.PublipostageMsWord;
import fr.legrain.publipostage.openoffice.AbstractPublipostageOOo;
import fr.legrain.publipostage.openoffice.PublipostageOOoFactory;
import fr.legrain.services.Branding;
import fr.legrain.services.IServiceBranding;
import fr.legrain.tiers.TiersPlugin;
import fr.legrain.tiers.clientutility.JNDILookupClass;
import fr.legrain.tiers.dao.TaDocumentTiersDAO;
import fr.legrain.tiers.dao.TaTiersDAO;
import fr.legrain.tiers.divers.FichierDonneesAdresseTiers;
import fr.legrain.tiers.divers.ParamAfficheTPaiement;
import fr.legrain.tiers.divers.TiersUtil;
import fr.legrain.tiers.dto.TaAdresseDTO;
import fr.legrain.tiers.dto.TaCPaiementDTO;
import fr.legrain.tiers.dto.TaComplDTO;
import fr.legrain.tiers.dto.TaEmailDTO;
import fr.legrain.tiers.dto.TaEntrepriseDTO;
import fr.legrain.tiers.dto.TaTCiviliteDTO;
import fr.legrain.tiers.dto.TaTEntiteDTO;
import fr.legrain.tiers.dto.TaTTarifDTO;
import fr.legrain.tiers.dto.TaTTiersDTO;
import fr.legrain.tiers.dto.TaTTvaDocDTO;
import fr.legrain.tiers.dto.TaTelephoneDTO;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.dto.TaWebDTO;
import fr.legrain.tiers.editor.EditorConditionPaiement;
import fr.legrain.tiers.editor.EditorInputAdresse;
import fr.legrain.tiers.editor.EditorInputCommercial;
import fr.legrain.tiers.editor.EditorInputComplement;
import fr.legrain.tiers.editor.EditorInputConditionPaiement;
import fr.legrain.tiers.editor.EditorInputEmail;
import fr.legrain.tiers.editor.EditorInputTelephone;
import fr.legrain.tiers.editor.EditorInputTypeCivilite;
import fr.legrain.tiers.editor.EditorInputTypeEntite;
import fr.legrain.tiers.editor.EditorInputTypePaiement;
import fr.legrain.tiers.editor.EditorInputTypeTarif;
import fr.legrain.tiers.editor.EditorInputTypeTiers;
import fr.legrain.tiers.editor.EditorInputWeb;
import fr.legrain.tiers.editor.EditorTypeCivilite;
import fr.legrain.tiers.editor.EditorTypeEntite;
import fr.legrain.tiers.editor.EditorTypePaiement;
import fr.legrain.tiers.editor.EditorTypeTarif;
import fr.legrain.tiers.editor.EditorTypeTiers;
import fr.legrain.tiers.editor.EditorTypeTvaDoc;
import fr.legrain.tiers.model.TaAdresse;
import fr.legrain.tiers.model.TaCPaiement;
import fr.legrain.tiers.model.TaCompl;
import fr.legrain.tiers.model.TaDocumentTiers;
import fr.legrain.tiers.model.TaEmail;
import fr.legrain.tiers.model.TaEntreprise;
import fr.legrain.tiers.model.TaTAdr;
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
import fr.legrain.tiers.preferences.PreferenceConstants;
import fr.legrain.tiers.views.ListeTiersView;


public class SWTPaTiersController extends EJBBaseControllerSWTStandard 
implements RetourEcranListener, ISelectionListener{

	static Logger logger = Logger.getLogger(SWTPaTiersController.class.getName());	

	private PaTiersSWT vue = null;
	private ITaTiersServiceRemote dao = null;//new TaTiersDAO();
	//private SWT_IB_TA_TIERS ibTaTable;// = new SWT_IB_TA_TIERS();
	private String idCommentaire = null;
	private String idTypeTiers = null;
	private String idTiers = null;

	private Object ecranAppelant = null;
	private TaTiersDTO swtTiers;
	private TaTiersDTO swtOldTiers;
	private Realm realm;
	private DataBindingContext dbc;

	private Class classModel = TaTiersDTO.class;
	private ModelTiers modelTiers = new ModelTiers();	
//	private ModelGeneralObjet<TaTiersDTO,TaTiersDAO,TaTiers> modelTiers = null;//new ModelGeneralObjet<TaTiersDTO,TaTiersDAO,TaTiers>(dao,classModel);
	//private ModelGeneral<TaTiersDTO> modelTiers;
//	private LgrTableViewer tableViewer;
//	private WritableList writableList;
	//private IObservableValue selectedTiers;
	private String[] listeChamp;
	private String nomClass = this.getClass().getSimpleName();
	private MapChangeListener changeListener = new MapChangeListener();
	
//	private Map<Button, File> listeModelesPublipostage = null;
	private Map<Button, TaDocumentTiers> listeModelesPublipostage = null;
	private TaDocumentTiersDAO daoDocumentTiers = null;

	public static final String C_COMMAND_ADRESSE_ID = "fr.legrain.gestionCommerciale.tiers.adresse";
	public static final String C_COMMAND_TELEPHONE_ID = "fr.legrain.gestionCommerciale.tiers.telephone";
	public static final String C_COMMAND_EMAIL_ID = "fr.legrain.gestionCommerciale.tiers.email";
	public static final String C_COMMAND_WEB_ID = "fr.legrain.gestionCommerciale.tiers.web";
	public static final String C_COMMAND_COMPLEMENT_ID = "fr.legrain.gestionCommerciale.tiers.complement";
	public static final String C_COMMAND_CONDITION_PAIEMENT_ID = "fr.legrain.gestionCommerciale.tiers.condPaiement";
	public static final String C_COMMAND_COMMERCIAL_ID = "fr.legrain.gestionCommerciale.tiers.commercial";
	public static final String C_COMMAND_TIERS_PUBLIPOSTAGE_ID = "fr.legrain.gestionCommerciale.tiers.publipostage";
	public static final String C_COMMAND_TIERS_OUVRIR_COURRIER_ID = "fr.legrain.gestionCommerciale.tiers.ouvrircourrier";
	
	public static final String C_COMMAND_TIERS_EMAIL_DOCUMENTS_ID = "fr.legrain.gestionCommerciale.tiers.fichetiers.email.documents";
	public static final String C_COMMAND_TIERS_EMAIL_ID = "fr.legrain.gestionCommerciale.tiers.fichetiers.email";
	public static final String C_COMMAND_TIERS_SMS_ID = "fr.legrain.gestionCommerciale.tiers.fichetiers.sms";
	public static final String C_COMMAND_TIERS_FAX_ID = "fr.legrain.gestionCommerciale.tiers.fichetiers.fax";

	private HandlerAjoutAdresse handlerAjoutAdresse = new HandlerAjoutAdresse();
	private HandlerAjoutCommercial handlerAjoutCommercial = new HandlerAjoutCommercial();
	private HandlerAjoutCompl handlerAjoutCompl = new HandlerAjoutCompl();
	private HandlerAjoutConditionPaiement handlerAjoutConditionPaiement = new HandlerAjoutConditionPaiement();
	private HandlerAjoutEmail handlerAjoutEmail = new HandlerAjoutEmail();
	private HandlerAjoutSiteWeb handlerAjoutSiteWeb = new HandlerAjoutSiteWeb();
	private HandlerAjoutTelephone handlerAjoutTelephone = new HandlerAjoutTelephone();
	
	private HandlerPublipostageTiers handlerPublipostageTiers = new HandlerPublipostageTiers();
	private HandlerOuvrirCourrierTiers handlerOuvrirCourrierTiers = new HandlerOuvrirCourrierTiers();
	
	private HandlerSendMailDocmuentsTiers handlerSendMailDocmuentsTiers = new HandlerSendMailDocmuentsTiers();
	private HandlerSendMailTiers handlerSendMailTiers = new HandlerSendMailTiers();
	private HandlerSendSMSTiers handlerSendSMSTiers = new HandlerSendSMSTiers();
	private HandlerSendFaxTiers handlerSendFaxTiers = new HandlerSendFaxTiers();

	private LgrDozerMapper<TaTiersDTO,TaTiers> mapperUIToModel  = new LgrDozerMapper<TaTiersDTO,TaTiers>();
	private LgrDozerMapper<TaTiers,TaTiersDTO> mapperModelToUI  = new LgrDozerMapper<TaTiers,TaTiersDTO>();
	private TaTiers taTiers = null;
	
	private static final List<String> listeDocumentsFinaux = new ArrayList<String>();
	private static boolean wait = false;
	
	/** Version contact "crado", à remplacer par un ensemble de points d'extension*/
	public static boolean versionContactCrado = false;

//	private Impression impression = new Impression();
	
	private static ITaTiersServiceRemote doLookup() {
		Context context = null;
		ITaTiersServiceRemote bean = null;
		try {
			// 1. Obtaining Context
			context = JNDILookupClass.getInitialContext();
			// 2. Generate JNDI Lookup name
			String beanName = "TaTiersService";
			final String interfaceName = ITaTiersServiceRemote.class.getName();
			String lookupName = getLookupName(beanName,interfaceName);
			// 3. Lookup and cast
			bean = (ITaTiersServiceRemote) context.lookup(lookupName+"?stateful");

		} catch (NamingException e) {
			e.printStackTrace();
		}
		return bean;
	}

	public SWTPaTiersController(PaTiersSWT vue) {
		this(vue,null);
	}

	public SWTPaTiersController(PaTiersSWT vue,EntityManager em) {
//		if(em!=null) {
//			setEm(em);
//		}
//		dao = new TaTiersDAO(getEm());
		dao = doLookup();
		//ejb
		//daoDocumentTiers = new TaDocumentTiersDAO(getEm());
		
		//modelTiers = new ModelGeneralObjet<TaTiersDTO,TaTiersDAO,TaTiers>(dao,classModel);
		setVue(vue);
		//vue.getCompositeForm().setOrientation(SWT.VERTICAL);
		//vue.getCompositeForm().setWeights(new int[]{20,80});

		//a faire avant l'initialisation du Binding
//		vue.getGrille().addSelectionListener( new SelectionAdapter(){
//			public void widgetSelected(SelectionEvent e) {
//				setSwtOldTiers();
//				//				ibTaTable.setTiersCourant((TaTiersDTO)selectedTiers.getValue());
//			}			
//		});
//		vue.getShell().addShellListener(this);

		//Branchement action annuler : empeche la fermeture automatique de la fenetre sur ESC
		vue.getShell().addTraverseListener(new Traverse());

		initController();
		initSashFormWeight();
		vue.getDateTimeDATEANNIV().addTraverseListener(new DateTraverse());
		vue.getDateTimeDATEANNIV().addFocusListener(dateFocusListener);
		//PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().addSelectionListener(ListeTiersView.ID, this);
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().addPostSelectionListener(ListeTiersView.ID, this);
	}



	private void initController()	{
		try {	
//			setGrille(vue.getGrille());
			initSashFormWeight();
			setDaoStandard(dao);
			//			setTableViewerStandard(tableViewer);
			//			setDbcStandard(dbc);

			initMapComposantChamps();
			initVerifySaisie();
			initMapComposantDecoratedField();
			listeComponentFocusableSWT(listeComposantFocusable);
			initFocusOrder();
			initActions();
			initDeplacementSaisie(listeComposantFocusable);

			branchementBouton();

			Menu popupMenuFormulaire = new Menu(vue.getShell(), SWT.POP_UP);
			Menu popupMenuGrille = new Menu(vue.getShell(), SWT.POP_UP);
			Menu[] tabPopups = new Menu[] {
					popupMenuFormulaire, popupMenuGrille };
			this.initPopupAndButtons(mapActions, tabPopups);
			vue.getPaCorpsFormulaire().setMenu(popupMenuFormulaire);
//			vue.getPaGrille().setMenu(popupMenuGrille);

			initEtatBouton();
			initPublipostage();
		} catch (Exception e) {
			vue.getLaMessage().setText(e.getMessage());
			logger.error("Erreur : PaTiersController", e);
		}
	}
	
	public void bind(PaTiersSWT paTiersSWT){
		bind(paTiersSWT,null);
	}
	
	public void bindID(PaTiersSWT paTiersSWT, int idTiers ){
		TaTiersDTO swtTiersTmp = new TaTiersDTO();
		try {
			modelTiers.mapping(dao.findById(idTiers), swtTiersTmp, true);
			bind(paTiersSWT, swtTiersTmp);
		} catch (FinderException e) {
			logger.error("",e);
		}
	}
	
	public void bindCode(PaTiersSWT paTiersSWT, String code ){
		TaTiersDTO swtTiersTmp = new TaTiersDTO();
		try {
			modelTiers.mapping(dao.findByCode(code), swtTiersTmp, true);
			bind(paTiersSWT, swtTiersTmp);
		} catch (FinderException e) {
			logger.error("",e);
		}
	}
	
	public void bind(PaTiersSWT paTiersSWT, TaTiersDTO selection ){
		try {
			//			modelTiers = new ModelTiers(ibTaTable);		
//			modelTiers = new ModelGeneralObjet<TaTiersDTO,TaTiersDAO,TaTiers>(dao,classModel);
			realm = SWTObservables.getRealm(vue.getParent().getDisplay());
			
//			if(selection!=null) {
				
				
//				modelInfoEntreprise = new ModelGeneralObjet<SWTInfoEntreprise,TaInfoEntrepriseDAO,TaInfoEntreprise>(dao,classModel);
//				realm = SWTObservables.getRealm(vue.getParent().getDisplay());
//
//				modelInfoEntreprise.remplirListe();
//				if(!modelInfoEntreprise.getListeObjet().isEmpty())
//					selectedInfoEntreprise = modelInfoEntreprise.getListeObjet().getFirst();

				dbc = new DataBindingContext(realm);

				if(selection!=null  && selection.getId()!=null && selection.getId()!=0) {
					taTiers =  dao.findById(selection.getId());
					
					/*selection =*/ modelTiers.mapping(taTiers, selection, true);
					swtTiers = selection;
				} else {
					if(swtTiers==null) {
						swtTiers = new TaTiersDTO();
					}
					selection = swtTiers;
				}

				dbc.getValidationStatusMap().addMapChangeListener(changeListener);
				setDbcStandard(dbc);

				bindingFormSimple(dbc, realm, selection,classModel);
				
				changementDeSelection();
				
				
//			} else {
//
//			tableViewer = new LgrTableViewer(paTiersSWT.getGrille());
//			/* ****************************************************************************
//			 * Test LazyLoading et remplissage à partir d'un thread pour le TableViewer
//			 * ****************************************************************************/
////			tableViewer.createTableCol(classModel,paTiersSWT.getGrille(),nomClass,Const.C_FICHIER_LISTE_CHAMP_GRILLE,-1);
////			listeChamp = tableViewer.setListeChampGrille(nomClass,Const.C_FICHIER_LISTE_CHAMP_GRILLE);
////
////			//			ObservableListContentProvider viewerContent = new ObservableListContentProvider();		
////			//			tableViewer.setContentProvider(viewerContent);
////			//
////			//			IObservableMap[] attributeMaps = BeansObservables.observeMaps(
////			//					viewerContent.getKnownElements(), classModel, listeChamp );
////			//
////			//			tableViewer.setLabelProvider(new ObservableMapLabelProvider(attributeMaps));
////			//			writableList =new WritableList(realm, modelTiers.remplirListe(), classModel);
////			//			tableViewer.setInput(writableList);
////			
////			if(idTypeTiers!=null && idTypeTiers.equals(TaTiersDAO.C_ID_IDENTITE_ENTREPRISE_STR)) {
////				modelTiers.setJPQLQuery(dao.getDefaultJPQLQueryIdentiteEntrepise());
////			}
////			else
////			if(idTiers!=null ) {
////				modelTiers.setJPQLQuery(dao.getJPQLQuery());
////			}
////			
////			ModelTiers.MyContentProvider cp = modelTiers.initCP(tableViewer);
////			
////			ObservableListContentProvider contentProvider = new ObservableListContentProvider();
////			//WritableList input = new WritableList(modelTiers.remplirListe(getEm()), classModel);
////			WritableList input = cp.elements;
////			if (tableViewer.getInput() != null)
////				tableViewer.setInput(null);
////			//tableViewer.setContentProvider(contentProvider);
////			tableViewer.setContentProvider(cp);
////			
////			tableViewer.setLabelProvider(modelTiers.getTableLabelProvider(listeChamp)); 
////			
//////			tableViewer.setLabelProvider(new MyLabelProvider(Properties.observeEach(contentProvider.getKnownElements(),
//////							BeanProperties.values(listeChamp))));
////			
//////			tableViewer.setLabelProvider(new MyLabelProvider(Properties.observeEach(cp.getKnownElements(),
//////			BeanProperties.values(listeChamp))));
////			
////			//if (input != null)
////			//	tableViewer.setInput(input);
////				tableViewer.setInput(cp);
////			tableViewer.setItemCount(new TaTiersDAO().selectAll().size());
////			
////
////			// Set up data binding.
//////			LgrViewerSupport.bind(tableViewer, 
//////					new WritableList(modelTiers.remplirListe(getEm()), classModel),
//////					BeanProperties.values(listeChamp)
//////			);
//////			LgrViewerSupport.bind(tableViewer, 
//////					new WritableList(new LinkedList<TaTiersDTO>(), classModel),
//////					BeanProperties.values(listeChamp)
//////			);
//			/* ****************************************************************************
//			 * Fin code de Test
//			 * ****************************************************************************/
//			
//			/* ****************************************************************************
//			 * Code "Normal"
//			 * ****************************************************************************/
//			tableViewer.createTableCol(classModel,paTiersSWT.getGrille(),nomClass,Const.C_FICHIER_LISTE_CHAMP_GRILLE,0);
//			listeChamp = tableViewer.setListeChampGrille(nomClass,Const.C_FICHIER_LISTE_CHAMP_GRILLE);
//
//			//			ObservableListContentProvider viewerContent = new ObservableListContentProvider();		
//			//			tableViewer.setContentProvider(viewerContent);
//			//
//			//			IObservableMap[] attributeMaps = BeansObservables.observeMaps(
//			//					viewerContent.getKnownElements(), classModel, listeChamp );
//			//
//			//			tableViewer.setLabelProvider(new ObservableMapLabelProvider(attributeMaps));
//			//			writableList =new WritableList(realm, modelTiers.remplirListe(), classModel);
//			//			tableViewer.setInput(writableList);
//			
//			if(idTypeTiers!=null && idTypeTiers.equals(TaTiersDAO.C_ID_IDENTITE_ENTREPRISE_STR)) {
//				modelTiers.setJPQLQuery(dao.getDefaultJPQLQueryIdentiteEntrepise());
//			}
//			else
//			if(idTiers!=null ) {
//				modelTiers.setJPQLQuery(dao.getJPQLQuery());
//			}
//
//			// Set up data binding.
//			LgrViewerSupport.bind(tableViewer, 
//					new WritableList(modelTiers.remplirListe(getEm()), classModel),
//					BeanProperties.values(listeChamp)
//			);
////			LgrViewerSupport.bind(tableViewer, 
////					new WritableList(new LinkedList<TaTiersDTO>(), classModel),
////					BeanProperties.values(listeChamp)
////			);
//			/* ****************************************************************************
//			 * Fin code "Normal"
//			 * ****************************************************************************/
//
//			selectedTiers = ViewersObservables.observeSingleSelection(tableViewer);
//
//			dbc = new DataBindingContext(realm);
//
//			dbc.getValidationStatusMap().addMapChangeListener(changeListener);
//			tableViewer.selectionGrille(0);
//			setTableViewerStandard(tableViewer);
//			setDbcStandard(dbc);
//
//			bindingFormMaitreDetail(dbc, realm, selectedTiers,classModel);
//			changementDeSelection();
//			selectedTiers.addChangeListener(new IChangeListener() {
//
//				public void handleChange(ChangeEvent event) {
//					changementDeSelection();
//				}
//
//			});
//			}

		} catch(Exception e) {
			vue.getLaMessage().setText(e.getMessage());
			logger.error("",e);
		}
	}

	private void changementDeSelection() {		
//		if(selectedTiers!=null && selectedTiers.getValue()!=null) {
//			if(((TaTiersDTO) selectedTiers.getValue()).getIdTiers()!=null) {
//				taTiers = dao.findById(((TaTiersDTO) selectedTiers.getValue()).getIdTiers());
//			}
//			//null a tester ailleurs, car peut etre null en cas d'insertion
//			fireChangementDeSelection(new ChangementDeSelectionEvent(SWTPaTiersController.this));
//		}
		if(swtTiers!=null) {
			if(swtTiers.getId()!=null) {
				try {
					taTiers = dao.findById(swtTiers.getId());
				} catch (FinderException e) {
					logger.error("",e);
				}
			}
			//null a tester ailleurs, car peut etre null en cas d'insertion
			fireChangementDeSelection(new ChangementDeSelectionEvent(SWTPaTiersController.this));
		}
		initEtatComposant();
	}
	
	public void initPublipostage() throws NamingException {
		
		if(listeModelesPublipostage==null) {
			listeModelesPublipostage = new HashMap<Button, TaDocumentTiers>();
		} else {
			listeModelesPublipostage.clear();
		}
		
		ITaDocumentTiersServiceRemote dao = new EJBLookup<ITaDocumentTiersServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_DOCUMENT_TIERS_SERVICE, ITaDocumentTiersServiceRemote.class.getName());
		//List<TaDocumentTiers> listeDoc = daoDocumentTiers.selectAll();
		List<TaDocumentTiers> listeDoc = dao.selectAll();

		if(!listeDoc.isEmpty()) {
		
			Button b = null;
			for (TaDocumentTiers taDocumentTiers : listeDoc) {
				if(taDocumentTiers.getActif().compareTo(1)==0 && new File(taDocumentTiers.getCheminModelDocumentTiers()).exists()) {
					b = new Button(vue.getCompositeCheckBoxPubli(),SWT.CHECK);
					b.setText(taDocumentTiers.getLibelleDocumentTiers());
					b.setToolTipText(taDocumentTiers.getCheminModelDocumentTiers());
					listeModelesPublipostage.put(b, taDocumentTiers);
				}
			}
			vue.getScrolledCompositePublipostage().setMinSize(vue.getCompositeCheckBoxPubli().computeSize(SWT.DEFAULT, SWT.DEFAULT));
			changeCouleur(vue.getCompositeCheckBoxPubli());
			vue.getCompositeCheckBoxPubli().layout();
		} else {
			vue.getBtnFusionPublipostage().setEnabled(false);
			vue.getBtnPublipostage().setEnabled(false);
		}
	}

	public Composite getVue() {return vue;}

	public ResultAffiche configPanel(ParamAffiche param){
		Date dateDeb = new Date();
		if (param!=null){
			Map<String,String[]> map = dao.getParamWhereSQL();
			//#JPA
			//			ibTaTable.ouvreDataset();
			if (param.getFocusDefautSWT()!=null && !param.getFocusDefautSWT().isDisposed())
				param.getFocusDefautSWT().setFocus();
//			else param.setFocusDefautSWT(vue.getGrille());

			if(param.getTitreFormulaire()!=null){
				vue.getLaTitreFormulaire().setText(param.getTitreFormulaire());
			} else {
				vue.getLaTitreFormulaire().setText(ParamAfficheTiers.C_TITRE_FORMULAIRE);
			}

//			if(param.getTitreGrille()!=null){
//				vue.getLaTitreGrille().setText(param.getTitreGrille());
//			} else {
//				vue.getLaTitreGrille().setText(ParamAfficheTiers.C_TITRE_GRILLE);
//			}

			if(param.getSousTitre()!=null){
				vue.getLaTitreFenetre().setText(param.getSousTitre());
			} else {
				vue.getLaTitreFenetre().setText(ParamAfficheTiers.C_SOUS_TITRE);
			}

//				if(param.getIdFiche()!=null &&  !param.getIdFiche().equals("")) {
//					this.idTiers=param.getIdFiche();
//					if(map==null) map = new HashMap<String,String[]>();
//					map.clear();
//					map.put("a.idTiers",new String[]{"=",idTiers});
//					dao.setParamWhereSQL(map);
////					vue.getBtnTous().setVisible(true);
////					vue.getGrille().setVisible(false);
////					vue.getLaTitreGrille().setVisible(false);
//					vue.getCompositeForm().setWeights(new int[]{0,100});					
//				}
//				if(param instanceof ParamAfficheTiers){
//					if(((ParamAfficheTiers)param).getIdTiers()!=null && !((ParamAfficheTiers)param).getIdTiers().equals("")) {
//						if(((ParamAfficheTiers)param).getIdTypeTiers()!=null){
//							this.idTypeTiers=((ParamAfficheTiers)param).getIdTypeTiers();
//							if(map==null) map = new HashMap<String,String[]>();
//							map.clear();
//							map.put("a.taTTiers."+Const.C_ID_T_TIERS,new String[]{"=",idTypeTiers});
//							dao.setParamWhereSQL(map);
//						}
//						if(((ParamAfficheTiers)param).getIdTiers()!=null){
//							this.idTiers=((ParamAfficheTiers)param).getIdTiers();
//							if(map==null) map = new HashMap<String,String[]>();
//							map.clear();
//							map.put("a.idTiers",new String[]{"=",idTiers});
//							dao.setParamWhereSQL(map);
////							vue.getBtnTous().setVisible(((ParamAfficheTiers)param).getIdTypeTiers()==null);
////							vue.getGrille().setVisible(false);
////							vue.getLaTitreGrille().setVisible(false);
//							vue.getCompositeForm().setWeights(new int[]{0,100});					
//						}
//
//					}			
//				}
			
			if(param.getEcranAppelant()!=null) {
				ecranAppelant = param.getEcranAppelant();
			}
			//modelTiers.setListeEntity(null);
			if(param.getSelection()!=null 
					&& !param.getSelection().isEmpty()
					&& param.getSelection() instanceof IStructuredSelection
					&& ((IStructuredSelection)param.getSelection()).getFirstElement()!=null
					&& ((IStructuredSelection)param.getSelection()).getFirstElement() instanceof TaTiersDTO
					) {
				bind(vue,(TaTiersDTO)((IStructuredSelection)param.getSelection()).getFirstElement());
			} else if(param.getIdFiche()!=null &&  !param.getIdFiche().equals("")) {
				this.idTiers=param.getIdFiche();
				bindID(vue,LibConversion.stringToInteger(idTiers));
			} else if(param.getCodeDocument()!=null && !param.getCodeDocument().equals("")) {
				bindCode(vue,param.getCodeDocument());
			} else if(param instanceof ParamAfficheTiers && ((ParamAfficheTiers)param).getIdTiers()!=null && !((ParamAfficheTiers)param).getIdTiers().equals("")) {
				this.idTiers=((ParamAfficheTiers)param).getIdTiers();
				bindID(vue,LibConversion.stringToInteger(idTiers));
			} else {
				bind(vue);
			}

			//permet de se positionner sur le 1er enregistrement et de remplir le formulaire

//				tableViewer.selectionGrille(0);
//				tableViewer.tri(classModel,nomClass,Const.C_FICHIER_LISTE_CHAMP_GRILLE);
				VerrouInterface.setVerrouille(false);
				setSwtOldTiers();

				if(param.getCodeDocument()!=null) {
					TaTiersDTO tiers = modelTiers.recherche(Const.C_CODE_TIERS, param.getCodeDocument());
					if(tiers!=null) {
//						tableViewer.setSelection(new StructuredSelection(tiers),true);
						
					}
				}
			

			if(param.getModeEcran()!=null)  {
				try {
					if(param.getModeEcran().compareTo(EnumModeObjet.C_MO_INSERTION)==0) {
						actInserer();
					} else if (param.getModeEcran().compareTo(EnumModeObjet.C_MO_SUPPRESSION)==0) {
						actSupprimer();
					}
				} catch (Exception e) {
					if(e!=null)
						vue.getLaMessage().setText(e.getMessage());
					logger.error("",e);
				}
			}
			
			//initEtatBouton();
			
		}
		Date dateFin = new Date();
		
//		logger.info("temp config panel "+
//				String.valueOf(dateFin.getMinutes()-dateDeb.getMinutes())+" mn"
//				+String.valueOf(dateFin.getSeconds()-dateDeb.getSeconds())+" s");
		
		if(taTiers!=null && taTiers.getIdTiers()==TaTiersDAO.C_ID_IDENTITE_ENTREPRISE_INT) {
			logger.debug("Cache les informations des tiers qui ne sont pas utile pour une description d'entreprise");
			
			vue.getGroupCompta().setVisible(false);
			vue.getGroupParam().setVisible(false);
			vue.getGroupPublipostage().setVisible(false);
			
			vue.getLaNOM_TIERS().setVisible(false);
			vue.getTfNOM_TIERS().setVisible(false);
			showGridLayoutComponent(vue.getLaNOM_TIERS(),false);
			showGridLayoutComponent(vue.getTfNOM_TIERS(),false);
			
			vue.getLaCODE_TIERS().setVisible(false);
			vue.getTfCODE_TIERS().setVisible(false);
			showGridLayoutComponent(vue.getLaCODE_TIERS(),false);
			showGridLayoutComponent(vue.getTfCODE_TIERS(),false);
			
			vue.getLaCODE_T_TIERS().setVisible(false);
			vue.getTfCODE_T_TIERS().setVisible(false);
			showGridLayoutComponent(vue.getLaCODE_T_TIERS(),false);
			showGridLayoutComponent(vue.getTfCODE_T_TIERS(),false);
			
			vue.getLaCODE_T_CIVILITE().setVisible(false);
			vue.getTfCODE_T_CIVILITE().setVisible(false);
			showGridLayoutComponent(vue.getLaCODE_T_CIVILITE(),false);
			showGridLayoutComponent(vue.getTfCODE_T_CIVILITE(),false);
			
			vue.getLaPRENOM_TIERS().setVisible(false);
			vue.getTfPRENOM_TIERS().setVisible(false);
			showGridLayoutComponent(vue.getLaPRENOM_TIERS(),false);
			showGridLayoutComponent(vue.getTfPRENOM_TIERS(),false);
			
			
			vue.getLaEntite().setVisible(false);
			vue.getTfEntite().setVisible(false);
			showGridLayoutComponent(vue.getLaEntite(),false);
			showGridLayoutComponent(vue.getTfEntite(),false);
			
			vue.getLaCODE_ENTREPRISE().setText("Nom");
			vue.getLaCODE_ENTREPRISE().moveAbove(null);
			vue.getTfCODE_ENTREPRISE().getParent().moveBelow(vue.getLaCODE_ENTREPRISE()); //getParent à cause des champs décorés
			vue.getLaTVA_I_COM_COMPL().moveBelow(vue.getTfCODE_ENTREPRISE().getParent());
			vue.getTfTVA_I_COM_COMPL().getParent().moveBelow(vue.getLaTVA_I_COM_COMPL());
			vue.getLaACCISE().moveBelow(vue.getTfTVA_I_COM_COMPL().getParent());
			vue.getTfACCISE().getParent().moveBelow(vue.getLaACCISE());
			vue.getGroupPublipostage().moveBelow(null);
			
			((GridData)vue.getGroupPublipostage().getLayoutData()).heightHint = 0;
			((GridData)vue.getGroupPublipostage().getLayoutData()).grabExcessVerticalSpace = false;
			
			((GridData)vue.getGroupCommentaire().getLayoutData()).horizontalSpan = 3;

			vue.layout(true,true);
			vue.getGroupIdentite().layout();
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////
		/** Version contact "crado", à remplacer par un ensemble de points d'extension*/
		BundleContext context = Platform.getBundle(gestComBdPlugin.PLUGIN_ID).getBundleContext();
		IServiceBranding service = null;
		ServiceReference<?> serviceReference = context.getServiceReference(IServiceBranding.class.getName());
		if(serviceReference!=null) {
			service = (IServiceBranding) context.getService(serviceReference); 
		}

		String typeVersionCrado = null;
		if(service!=null && service.getBranding()!=null && service.getBranding().getTypeVersion()!=null) {
			typeVersionCrado = service.getBranding().getTypeVersion();
		}
		if(typeVersionCrado!=null && typeVersionCrado.equals(Branding.C_VERSION_CONTACT)) {
			versionContactCrado = true;
		}
		if(versionContactCrado) {
			vue.getGroupCompta().setVisible(false);
			
			vue.getLaCODE_C_PAIEMENT().setVisible(false);
			vue.getTfCODE_C_PAIEMENT().setVisible(false);
			showGridLayoutComponent(vue.getLaCODE_C_PAIEMENT(),false);
			showGridLayoutComponent(vue.getTfCODE_C_PAIEMENT(),false);
			
			vue.getLaCODE_T_PAIEMENT().setVisible(false);
			vue.getTfCODE_T_PAIEMENT().setVisible(false);
			showGridLayoutComponent(vue.getLaCODE_T_PAIEMENT(),false);
			showGridLayoutComponent(vue.getTfCODE_T_PAIEMENT(),false);
			
			vue.getLaCODE_T_TVA_DOC().setVisible(false);
			vue.getTfCODE_T_TVA_DOC().setVisible(false);
			showGridLayoutComponent(vue.getLaCODE_T_TVA_DOC(),false);
			showGridLayoutComponent(vue.getTfCODE_T_TVA_DOC(),false);
			
			vue.getLaCODE_T_TARIF().setVisible(false);
			vue.getTfCODE_T_TARIF().setVisible(false);
			showGridLayoutComponent(vue.getLaCODE_T_TARIF(),false);
			showGridLayoutComponent(vue.getTfCODE_T_TARIF(),false);
			
			vue.getLaTTC().setVisible(false);
			vue.getTfTTC().setVisible(false);
			showGridLayoutComponent(vue.getLaTTC(),false);
			showGridLayoutComponent(vue.getTfTTC(),false);
			
			vue.getLaACCISE().setVisible(false);
			vue.getTfACCISE().setVisible(false);
			showGridLayoutComponent(vue.getLaACCISE(),false);
			showGridLayoutComponent(vue.getTfACCISE(),false);
			
			vue.getLaTVA_I_COM_COMPL().setVisible(false);
			vue.getTfTVA_I_COM_COMPL().setVisible(false);
			showGridLayoutComponent(vue.getLaTVA_I_COM_COMPL(),false);
			showGridLayoutComponent(vue.getTfTVA_I_COM_COMPL(),false);
		}
		////////////////////////////////////////////////////////////////////////////////////////////////////////
	
		logger.info("temp config panel "+new Date(dateFin.getTime()-dateDeb.getTime()));
		return null;
	}
	
	/**
	 * 
	 * @param c
	 * @param show
	 */
	public void showGridLayoutComponent(Control c, boolean show) {
//		if(c.getLayoutData()!=null) {
//			if(c.getLayoutData() instanceof GridData) {
//				((GridData)c.getLayoutData()).exclude = !show;
//			} else if(c.getLayoutData() instanceof FormData) {
//				if(c.getParent().getLayoutData() instanceof GridData) {
//					((GridData)c.getParent().getLayoutData()).exclude = !show;
//				}
//			}
//		} else {
//			GridData gd = new GridData();
//			gd.exclude = !show;
//			c.setLayoutData(gd);
//		}
	}
	
	protected void initImageBouton() {
		super.initImageBouton();
		vue.getBtnPublipostage().setImage(LibrairiesEcranPlugin.ir.get(LibrairiesEcranPlugin.IMAGE_IMPRIMER));
		vue.getBtnOuvreRepertoireCourrier().setImage(LibrairiesEcranPlugin.ir.get(LibrairiesEcranPlugin.IMAGE_REPERTOIRE));
		vue.getBtnEmail().setImage(LibrairiesEcranPlugin.ir.get(LibrairiesEcranPlugin.IMAGE_EMAIL));
		vue.getBtnEmailDocuments().setImage(LibrairiesEcranPlugin.ir.get(LibrairiesEcranPlugin.IMAGE_EMAIL));
		vue.getBtnSMS().setImage(LibrairiesEcranPlugin.ir.get(LibrairiesEcranPlugin.IMAGE_SMS));
		vue.getBtnFax().setImage(LibrairiesEcranPlugin.ir.get(LibrairiesEcranPlugin.IMAGE_GENERE));
		vue.getBtnPublipostage().setToolTipText("Ouvrir les documents");
		vue.getBtnOuvreRepertoireCourrier().setToolTipText("Ouvrir le répertoire courrier du tiers");
		vue.getBtnEmail().setToolTipText("Email");
		vue.getBtnEmailDocuments().setToolTipText("Envoie des documents par email");
		vue.getBtnSMS().setToolTipText("SMS");
		vue.getBtnFax().setToolTipText("Fax");
		vue.layout(true);
	}
	
	protected void initVerifySaisie() {
		if (mapInfosVerifSaisie == null)
			mapInfosVerifSaisie = new HashMap<Control, InfosVerifSaisie>();
		
		mapInfosVerifSaisie.put(vue.getTfCODE_TIERS(), new InfosVerifSaisie(new TaTiers(),Const.C_CODE_TIERS,null));
		mapInfosVerifSaisie.put(vue.getTfEntite(), new InfosVerifSaisie(new TaTEntite(),Const.C_CODE_T_ENTITE,null));
		mapInfosVerifSaisie.put(vue.getTfCODE_T_TIERS(), new InfosVerifSaisie(new TaTTiers(),Const.C_CODE_T_TIERS,null));
		mapInfosVerifSaisie.put(vue.getTfCODE_ENTREPRISE(), new InfosVerifSaisie(new TaEntreprise(),Const.C_NOM_ENTREPRISE,null));
		mapInfosVerifSaisie.put(vue.getTfCODE_T_CIVILITE(), new InfosVerifSaisie(new TaTCivilite(),Const.C_CODE_T_CIVILITE,null));
		mapInfosVerifSaisie.put(vue.getTfNOM_TIERS(), new InfosVerifSaisie(new TaTiers(),Const.C_NOM_TIERS,null));
		mapInfosVerifSaisie.put(vue.getTfPRENOM_TIERS(), new InfosVerifSaisie(new TaTiers(),Const.C_PRENOM_TIERS,null));
		mapInfosVerifSaisie.put(vue.getTfTVA_I_COM_COMPL(), new InfosVerifSaisie(new TaCompl(),Const.C_TVA_I_COM_COMPL,null));
		mapInfosVerifSaisie.put(vue.getTfACCISE(), new InfosVerifSaisie(new TaCompl(),Const.C_ACCISE,null));
		mapInfosVerifSaisie.put(vue.getTfADRESSE1_ADRESSE(), new InfosVerifSaisie(new TaAdresse(),Const.C_ADRESSE1_ADRESSE,null));
		mapInfosVerifSaisie.put(vue.getTfADRESSE2_ADRESSE(), new InfosVerifSaisie(new TaAdresse(),Const.C_ADRESSE2_ADRESSE,null));
		mapInfosVerifSaisie.put(vue.getTfADRESSE3_ADRESSE(), new InfosVerifSaisie(new TaAdresse(),Const.C_ADRESSE3_ADRESSE,null));
		mapInfosVerifSaisie.put(vue.getTfCODEPOSTAL_ADRESSE(), new InfosVerifSaisie(new TaAdresse(),Const.C_CODEPOSTAL_ADRESSE,null));
		mapInfosVerifSaisie.put(vue.getTfVILLE_ADRESSE(), new InfosVerifSaisie(new TaAdresse(),Const.C_VILLE_ADRESSE,null));
		mapInfosVerifSaisie.put(vue.getTfPAYS_ADRESSE(), new InfosVerifSaisie(new TaAdresse(),Const.C_PAYS_ADRESSE,null));
		mapInfosVerifSaisie.put(vue.getTfNUMERO_TELEPHONE(), new InfosVerifSaisie(new TaTelephone(),Const.C_NUMERO_TELEPHONE,null));
		mapInfosVerifSaisie.put(vue.getTfADRESSE_WEB(), new InfosVerifSaisie(new TaWeb(),Const.C_ADRESSE_WEB,null));
		mapInfosVerifSaisie.put(vue.getTfADRESSE_EMAIL(), new InfosVerifSaisie(new TaEmail(),Const.C_ADRESSE_EMAIL,null));
		mapInfosVerifSaisie.put(vue.getTfCOMPTE(), new InfosVerifSaisie(new TaTiers(),Const.C_COMPTE,null));
		mapInfosVerifSaisie.put(vue.getTfCODE_C_PAIEMENT(), new InfosVerifSaisie(new TaCPaiement(),Const.C_CODE_C_PAIEMENT,null));
		mapInfosVerifSaisie.put(vue.getTfCODE_T_TARIF(), new InfosVerifSaisie(new TaTTarif(),Const.C_CODE_T_TARIF,null));
		mapInfosVerifSaisie.put(vue.getTfCODE_COMPTA(), new InfosVerifSaisie(new TaTiers(),Const.C_CODE_COMPTA,null));
		
		initVerifyListener(mapInfosVerifSaisie, dao);
	}

	protected void initComposantsVue() throws ExceptLgr {}

	protected void initEtatBouton() {
		//super.initEtatBouton();
		initEtatBoutonCommand();
		
		if(idTypeTiers!=null && idTypeTiers.equals(TaTiersDAO.C_ID_IDENTITE_ENTREPRISE_STR)) {
			boolean trouve = false;
			if(modelTiers!=null) {
				trouve = modelTiers.getListeObjet().size()>0;
			} else {
				trouve = daoStandard.selectAll().size()>0;
			}
			switch (getModeEcran().getMode()) {
			case C_MO_INSERTION:
				enableActionAndHandler(C_COMMAND_GLOBAL_INSERER_ID,false);
				enableActionAndHandler(C_COMMAND_GLOBAL_MODIFIER_ID,false);
				enableActionAndHandler(C_COMMAND_GLOBAL_ENREGISTRER_ID,true);
				enableActionAndHandler(C_COMMAND_GLOBAL_ANNULER_ID,true);
				enableActionAndHandler(C_COMMAND_GLOBAL_IMPRIMER_ID,false);
				enableActionAndHandler(C_COMMAND_GLOBAL_FERMER_ID,true);
				enableActionAndHandler(C_COMMAND_GLOBAL_SUPPRIMER_ID,false);
				enableActionAndHandler(C_COMMAND_GLOBAL_AIDE_ID,true);
				enableActionAndHandler(C_COMMAND_TIERS_PUBLIPOSTAGE_ID,false);
				enableActionAndHandler(C_COMMAND_TIERS_OUVRIR_COURRIER_ID,false);
				enableActionAndHandler(C_COMMAND_TIERS_EMAIL_DOCUMENTS_ID,false);
				break;
			case C_MO_EDITION:
				enableActionAndHandler(C_COMMAND_GLOBAL_INSERER_ID,false);
				enableActionAndHandler(C_COMMAND_GLOBAL_MODIFIER_ID,false);
				enableActionAndHandler(C_COMMAND_GLOBAL_ENREGISTRER_ID,true);
				enableActionAndHandler(C_COMMAND_GLOBAL_ANNULER_ID,true);
				enableActionAndHandler(C_COMMAND_GLOBAL_IMPRIMER_ID,false);
				enableActionAndHandler(C_COMMAND_GLOBAL_FERMER_ID,true);
				enableActionAndHandler(C_COMMAND_GLOBAL_SUPPRIMER_ID,false);
				enableActionAndHandler(C_COMMAND_GLOBAL_AIDE_ID,true);
				enableActionAndHandler(C_COMMAND_TIERS_PUBLIPOSTAGE_ID,false);
				enableActionAndHandler(C_COMMAND_TIERS_OUVRIR_COURRIER_ID,false);
				enableActionAndHandler(C_COMMAND_TIERS_EMAIL_DOCUMENTS_ID,false);
				break;
			case C_MO_CONSULTATION:
				enableActionAndHandler(C_COMMAND_GLOBAL_INSERER_ID,!trouve);
				enableActionAndHandler(C_COMMAND_GLOBAL_MODIFIER_ID,trouve);
				enableActionAndHandler(C_COMMAND_GLOBAL_ENREGISTRER_ID,false);
				enableActionAndHandler(C_COMMAND_GLOBAL_ANNULER_ID,true);
				enableActionAndHandler(C_COMMAND_GLOBAL_IMPRIMER_ID,true);
				enableActionAndHandler(C_COMMAND_GLOBAL_FERMER_ID,true);
				enableActionAndHandler(C_COMMAND_GLOBAL_SUPPRIMER_ID,false);
				enableActionAndHandler(C_COMMAND_GLOBAL_AIDE_ID,true);
				enableActionAndHandler(C_COMMAND_TIERS_PUBLIPOSTAGE_ID,true);
				enableActionAndHandler(C_COMMAND_TIERS_OUVRIR_COURRIER_ID,true);
				enableActionAndHandler(C_COMMAND_TIERS_EMAIL_DOCUMENTS_ID,true);
				break;
			default:
				break;
			}
			initFocusSWT(getModeEcran(),mapInitFocus);
		}
		
		switch (getModeEcran().getMode()) {
		case C_MO_INSERTION:
			activeGrilleView(false);
			break;
		case C_MO_EDITION:
			activeGrilleView(false);
			break;
		case C_MO_CONSULTATION:
			activeGrilleView(true);
			break;
		default:
			break;
		}
	
	}	

	protected void initMapComposantChamps() {
		if (mapComposantChamps == null) 
			mapComposantChamps = new LinkedHashMap<Control,String>();

		if (listeComposantFocusable == null) 
			listeComposantFocusable = new ArrayList<Control>();
//		listeComposantFocusable.add(vue.getGrille());
		


		vue.getTfCODE_TIERS().setToolTipText("C_CODE_TIERS");
		vue.getTfCODE_COMPTA().setToolTipText("C_CODE_COMPTA");
		vue.getTfNOM_TIERS().setToolTipText("C_NOM_TIERS");
		vue.getTfPRENOM_TIERS().setToolTipText("C_PRENOM_TIERS");
		vue.getDateTimeDATEANNIV().setToolTipText("C_DATE_ANNIVERSAIRE");
		//		vue.getTfSURNOM_TIERS().setToolTipText("C_SURNOM_TIERS");
		vue.getCbACTIF_TIERS().setToolTipText("C_ACTIF_TIERS");
		vue.getTfTTC().setToolTipText("C_TTC_TIERS");
		vue.getTfCODE_T_CIVILITE().setToolTipText("C_CODE_T_CIVILITE");
		vue.getTfCODE_ENTREPRISE().setToolTipText("C_CODE_ENTREPRISE");
		vue.getTfCODE_T_TIERS().setToolTipText("C_CODE_T_TIERS");
		vue.getTfCOMPTE().setToolTipText("C_COMPTE");
		vue.getTfLIBL_COMMENTAIRE().setToolTipText("C_LIBL_COMMENTAIRE");
		//#JPA
		//		vue.getTfCODE_BANQUE().setToolTipText("C_CODE_BANQUE");
		vue.getTfEntite().setToolTipText("C_CODE_T_ENTITE");
		vue.getTfCODE_TIERS().setToolTipText(Const.C_CODE_TIERS);
		vue.getTfCODE_COMPTA().setToolTipText(Const.C_CODE_COMPTA);
		vue.getTfCODE_T_TVA_DOC().setToolTipText(Const.C_CODE_T_TVA_DOC);
		vue.getTfNOM_TIERS().setToolTipText(Const.C_NOM_TIERS);
		vue.getTfPRENOM_TIERS().setToolTipText(Const.C_PRENOM_TIERS);
		//		vue.getTfSURNOM_TIERS().setToolTipText( Const.C_SURNOM_TIERS);
		vue.getCbACTIF_TIERS().setToolTipText( Const.C_ACTIF_TIERS);
		vue.getTfTTC().setToolTipText( Const.C_TTC_TIERS);
		vue.getTfCODE_T_CIVILITE().setToolTipText( Const.C_CODE_T_CIVILITE);
		vue.getTfCODE_ENTREPRISE().setToolTipText( Const.C_NOM_ENTREPRISE);
		vue.getTfCODE_T_TIERS().setToolTipText( Const.C_CODE_T_TIERS);
		vue.getTfCOMPTE().setToolTipText(Const.C_COMPTE);
		vue.getTfLIBL_COMMENTAIRE().setToolTipText( Const.C_LIBL_COMMENTAIRE);
		vue.getTfEntite().setToolTipText( Const.C_CODE_T_ENTITE);

		vue.getTfADRESSE1_ADRESSE().setToolTipText(Const.C_ADRESSE1_ADRESSE);
		vue.getTfADRESSE2_ADRESSE().setToolTipText(Const.C_ADRESSE2_ADRESSE);
		vue.getTfADRESSE3_ADRESSE().setToolTipText(Const.C_ADRESSE3_ADRESSE);
		vue.getTfCODEPOSTAL_ADRESSE().setToolTipText(Const.C_CODEPOSTAL_ADRESSE);
		vue.getTfVILLE_ADRESSE().setToolTipText(Const.C_VILLE_ADRESSE);
		vue.getTfPAYS_ADRESSE().setToolTipText(Const.C_PAYS_ADRESSE);

		vue.getTfADRESSE_EMAIL().setToolTipText(Const.C_ADRESSE_EMAIL);
		vue.getTfADRESSE_EMAIL().setToolTipText(Const.C_ADRESSE_WEB);
		vue.getTfNUMERO_TELEPHONE().setToolTipText(Const.C_NUMERO_TELEPHONE);

		vue.getTfACCISE().setToolTipText(Const.C_ACCISE);
		vue.getTfTVA_I_COM_COMPL().setToolTipText(Const.C_TVA_I_COM_COMPL);

		vue.getTfCODE_C_PAIEMENT().setToolTipText(Const.C_CODE_C_PAIEMENT);
		vue.getTfCODE_T_PAIEMENT().setToolTipText(Const.C_CODE_T_PAIEMENT);
		vue.getTfCODE_T_TARIF().setToolTipText(Const.C_CODE_T_TARIF);


		mapComposantChamps.put(vue.getTfCODE_TIERS(),Const.C_CODE_TIERS);
		mapComposantChamps.put(vue.getTfEntite(), Const.C_CODE_T_ENTITE);
		mapComposantChamps.put(vue.getTfCODE_T_TIERS(), Const.C_CODE_T_TIERS);
		mapComposantChamps.put(vue.getTfCODE_ENTREPRISE(), Const.C_NOM_ENTREPRISE);
		mapComposantChamps.put(vue.getTfCODE_T_CIVILITE(), Const.C_CODE_T_CIVILITE);
		mapComposantChamps.put(vue.getTfNOM_TIERS(),Const.C_NOM_TIERS);
		mapComposantChamps.put(vue.getTfPRENOM_TIERS(),Const.C_PRENOM_TIERS);
		mapComposantChamps.put(vue.getDateTimeDATEANNIV(),Const.C_DATE_ANNIV);
		mapComposantChamps.put(vue.getTfTVA_I_COM_COMPL(),Const.C_TVA_I_COM_COMPL);
		mapComposantChamps.put(vue.getTfACCISE(),Const.C_ACCISE);
		mapComposantChamps.put(vue.getTfADRESSE1_ADRESSE(),Const.C_ADRESSE1_ADRESSE);
		mapComposantChamps.put(vue.getTfADRESSE2_ADRESSE(),Const.C_ADRESSE2_ADRESSE);
		mapComposantChamps.put(vue.getTfADRESSE3_ADRESSE(),Const.C_ADRESSE3_ADRESSE);
		mapComposantChamps.put(vue.getTfCODEPOSTAL_ADRESSE(),Const.C_CODEPOSTAL_ADRESSE);
		mapComposantChamps.put(vue.getTfVILLE_ADRESSE(),Const.C_VILLE_ADRESSE);
		mapComposantChamps.put(vue.getTfPAYS_ADRESSE(),Const.C_PAYS_ADRESSE);
		mapComposantChamps.put(vue.getTfNUMERO_TELEPHONE(),Const.C_NUMERO_TELEPHONE);
		mapComposantChamps.put(vue.getTfADRESSE_EMAIL(),Const.C_ADRESSE_EMAIL);
		mapComposantChamps.put(vue.getTfADRESSE_WEB(),Const.C_ADRESSE_WEB);
		mapComposantChamps.put(vue.getTfCODE_COMPTA(),Const.C_CODE_COMPTA);
		mapComposantChamps.put(vue.getTfCOMPTE(),Const.C_COMPTE);//
		mapComposantChamps.put(vue.getTfCODE_T_TVA_DOC(),Const.C_CODE_T_TVA_DOC);
		mapComposantChamps.put(vue.getCbACTIF_TIERS(), Const.C_ACTIF_TIERS);
		mapComposantChamps.put(vue.getTfTTC(), Const.C_TTC_TIERS);
		mapComposantChamps.put(vue.getTfCODE_C_PAIEMENT(), Const.C_CODE_C_PAIEMENT);
		mapComposantChamps.put(vue.getTfCODE_T_PAIEMENT(), Const.C_CODE_T_PAIEMENT);
		mapComposantChamps.put(vue.getTfCODE_T_TARIF(), Const.C_CODE_T_TARIF);
		mapComposantChamps.put(vue.getTfLIBL_COMMENTAIRE(), Const.C_LIBL_COMMENTAIRE);
		//		mapComposantChamps.put(vue.getTfSURNOM_TIERS(), Const.C_SURNOM_TIERS);
		//#JPA
		//		mapComposantChamps.put(vue.getTfCODE_BANQUE(), Const.C_CODE_BANQUE);


		for (Control c : mapComposantChamps.keySet()) {
			listeComposantFocusable.add(c);
		}

		//		listeComposantFocusable.add(vue.getBtnAjoutCommercial());
		//		listeComposantFocusable.add(vue.getBtnAjoutAdr());
		//		listeComposantFocusable.add(vue.getBtnAjoutTelephone());
		//		listeComposantFocusable.add(vue.getBtnAjoutEmail());
		//		listeComposantFocusable.add(vue.getBtnAjoutSiteWeb());
		//		listeComposantFocusable.add(vue.getBtnConditionPaiement());
		//		listeComposantFocusable.add(vue.getBtnCompl());

		listeComposantFocusable.add(vue.getBtnEnregistrer());
		listeComposantFocusable.add(vue.getBtnInserer());
		listeComposantFocusable.add(vue.getBtnModifier());
		listeComposantFocusable.add(vue.getBtnSupprimer());
		listeComposantFocusable.add(vue.getBtnFermer());
		listeComposantFocusable.add(vue.getBtnAnnuler());

		listeComposantFocusable.add(vue.getBtnImprimer());
//		listeComposantFocusable.add(vue.getBtnTous());
		
		listeComposantFocusable.add(vue.getBtnPublipostage());
		listeComposantFocusable.add(vue.getBtnOuvreRepertoireCourrier());
		
		listeComposantFocusable.add(vue.getBtnEmail());
		listeComposantFocusable.add(vue.getBtnSMS());
		listeComposantFocusable.add(vue.getBtnFax());
		listeComposantFocusable.add(vue.getBtnEmailDocuments());

		if(mapInitFocus == null) 
			mapInitFocus = new LinkedHashMap<EnumModeObjet,Control>();
		mapInitFocus.put(EnumModeObjet.C_MO_INSERTION,vue.getTfCODE_TIERS());
		mapInitFocus.put(EnumModeObjet.C_MO_EDITION,vue.getTfCODE_TIERS());
//		mapInitFocus.put(EnumModeObjet.C_MO_CONSULTATION,vue.getGrille());
		mapInitFocus.put(EnumModeObjet.C_MO_CONSULTATION,vue.getTfCODE_TIERS());

		activeModifytListener();

		vue.getTfCOMPTE().addVerifyListener(queDesChiffres);
	}

	protected void initActions() {		
		mapCommand = new HashMap<String, IHandler>();

		mapCommand.put(C_COMMAND_GLOBAL_MODIFIER_ID, handlerModifier);
		mapCommand.put(C_COMMAND_GLOBAL_ANNULER_ID, handlerAnnuler);
		mapCommand.put(C_COMMAND_GLOBAL_ENREGISTRER_ID, handlerEnregistrer);
		mapCommand.put(C_COMMAND_GLOBAL_INSERER_ID, handlerInserer);
		mapCommand.put(C_COMMAND_GLOBAL_SUPPRIMER_ID, handlerSupprimer);
		mapCommand.put(C_COMMAND_GLOBAL_REFRESH_ID, handlerRefresh);
		mapCommand.put(C_COMMAND_GLOBAL_AIDE_ID, handlerAide);
		mapCommand.put(C_COMMAND_GLOBAL_FERMER_ID, handlerFermer);
		mapCommand.put(C_COMMAND_GLOBAL_IMPRIMER_ID, handlerImprimer);

		mapCommand.put(C_COMMAND_GLOBAL_PRECEDENT_ID, handlerPrecedent);
		mapCommand.put(C_COMMAND_GLOBAL_SUIVANT_ID, handlerSuivant);

		mapCommand.put(C_COMMAND_ADRESSE_ID, handlerAjoutAdresse);
		mapCommand.put(C_COMMAND_COMMERCIAL_ID, handlerAjoutCommercial);
		mapCommand.put(C_COMMAND_COMPLEMENT_ID, handlerAjoutCompl);
		mapCommand.put(C_COMMAND_CONDITION_PAIEMENT_ID, handlerAjoutConditionPaiement);
		mapCommand.put(C_COMMAND_EMAIL_ID, handlerAjoutEmail);
		mapCommand.put(C_COMMAND_WEB_ID, handlerAjoutSiteWeb);
		mapCommand.put(C_COMMAND_TELEPHONE_ID, handlerAjoutTelephone);
		mapCommand.put(C_COMMAND_GLOBAL_SELECTION_ID, handlerSelection);
		
		mapCommand.put(C_COMMAND_DOCUMENT_AFFICHER_TOUS_ID, handlerAfficherTous);
		
		mapCommand.put(C_COMMAND_TIERS_PUBLIPOSTAGE_ID, handlerPublipostageTiers);
		mapCommand.put(C_COMMAND_TIERS_OUVRIR_COURRIER_ID, handlerOuvrirCourrierTiers);
		
		mapCommand.put(C_COMMAND_TIERS_EMAIL_ID, handlerSendMailTiers);
		mapCommand.put(C_COMMAND_TIERS_SMS_ID, handlerSendSMSTiers);
		mapCommand.put(C_COMMAND_TIERS_FAX_ID, handlerSendFaxTiers);
		mapCommand.put(C_COMMAND_TIERS_EMAIL_DOCUMENTS_ID, handlerSendMailDocmuentsTiers);
		
		initFocusCommand(String.valueOf(this.hashCode()),listeComposantFocusable,mapCommand);

		if (mapActions == null)
			mapActions = new LinkedHashMap<Button, Object>();

		mapActions.put(vue.getBtnAnnuler(), C_COMMAND_GLOBAL_ANNULER_ID);
		mapActions.put(vue.getBtnEnregistrer(), C_COMMAND_GLOBAL_ENREGISTRER_ID);
		mapActions.put(vue.getBtnInserer(), C_COMMAND_GLOBAL_INSERER_ID);
		mapActions.put(vue.getBtnModifier(), C_COMMAND_GLOBAL_MODIFIER_ID);
		mapActions.put(vue.getBtnSupprimer(), C_COMMAND_GLOBAL_SUPPRIMER_ID);
		mapActions.put(vue.getBtnFermer(), C_COMMAND_GLOBAL_FERMER_ID);
		mapActions.put(vue.getBtnImprimer(), C_COMMAND_GLOBAL_IMPRIMER_ID);
//		mapActions.put(vue.getBtnTous(), C_COMMAND_DOCUMENT_AFFICHER_TOUS_ID);
		mapActions.put(vue.getBtnPublipostage(), C_COMMAND_TIERS_PUBLIPOSTAGE_ID);
		mapActions.put(vue.getBtnOuvreRepertoireCourrier(), C_COMMAND_TIERS_OUVRIR_COURRIER_ID);
		
		mapActions.put(vue.getBtnEmail(), C_COMMAND_TIERS_EMAIL_ID);
		mapActions.put(vue.getBtnSMS(), C_COMMAND_TIERS_SMS_ID);
		mapActions.put(vue.getBtnFax(), C_COMMAND_TIERS_FAX_ID);
		mapActions.put(vue.getBtnEmailDocuments(), C_COMMAND_TIERS_EMAIL_DOCUMENTS_ID);

		//		mapActions.put(vue.getBtnAjoutAdr(), C_COMMAND_ADRESSE_ID);
		//		mapActions.put(vue.getBtnAjoutCommercial(), C_COMMAND_COMMERCIAL_ID);
		//		mapActions.put(vue.getBtnCompl(), C_COMMAND_COMPLEMENT_ID);
		//		mapActions.put(vue.getBtnConditionPaiement(), C_COMMAND_CONDITION_PAIEMENT_ID);
		//		mapActions.put(vue.getBtnAjoutEmail(), C_COMMAND_EMAIL_ID);
		//		mapActions.put(vue.getBtnAjoutSiteWeb(), C_COMMAND_WEB_ID);
		//		mapActions.put(vue.getBtnAjoutTelephone(), C_COMMAND_TELEPHONE_ID);

		Object[] tabActionsAutres = new Object[] { C_COMMAND_GLOBAL_AIDE_ID, C_COMMAND_GLOBAL_REFRESH_ID };
		mapActions.put(null, tabActionsAutres);
	}

	//#JPA
	//	protected void initImageBouton() {
	//		super.initImageBouton();
	//		String C_IMAGE_AJOUT_ADRESSE = "/icons/book_open.png";
	//		String C_IMAGE_AJOUT_COMMERCIAL = "/icons/user.png";
	//		String C_IMAGE_AJOUT_EMAIL = "/icons/email.png";
	//		String C_IMAGE_AJOUT_SITE_WEB = "/icons/world_link.png";
	//		String C_IMAGE_AJOUT_TELEPHONE = "/icons/phone.png";
	//		String C_IMAGE_AJOUT_COMPL = "/icons/note.png";
	//		String C_IMAGE_AJOUT_COND_PAIEMENT = "/icons/creditcards.png";
	//		if(vue instanceof PaTiersSWT) {
	//			((PaTiersSWT)vue).getBtnAjoutAdr().setImage(TiersPlugin.getImageDescriptor(C_IMAGE_AJOUT_ADRESSE).createImage());
	//			((PaTiersSWT)vue).getBtnAjoutCommercial().setImage(TiersPlugin.getImageDescriptor(C_IMAGE_AJOUT_COMMERCIAL).createImage());
	//			((PaTiersSWT)vue).getBtnAjoutEmail().setImage(TiersPlugin.getImageDescriptor(C_IMAGE_AJOUT_EMAIL).createImage());
	//			((PaTiersSWT)vue).getBtnAjoutSiteWeb().setImage(TiersPlugin.getImageDescriptor(C_IMAGE_AJOUT_SITE_WEB).createImage());
	//			((PaTiersSWT)vue).getBtnAjoutTelephone().setImage(TiersPlugin.getImageDescriptor(C_IMAGE_AJOUT_TELEPHONE).createImage());
	//			((PaTiersSWT)vue).getBtnCompl().setImage(TiersPlugin.getImageDescriptor(C_IMAGE_AJOUT_COMPL).createImage());
	//			((PaTiersSWT)vue).getBtnConditionPaiement().setImage(TiersPlugin.getImageDescriptor(C_IMAGE_AJOUT_COND_PAIEMENT).createImage());
	//			vue.layout(true);
	//		}
	//	}

	protected class HandlerAjoutAdresse extends LgrAbstractHandler {

		public Object execute(ExecutionEvent event) throws ExecutionException{
			try {
				//				//PaAdresse
				//				ParamAfficheAdresse paramAfficheAdresse= new ParamAfficheAdresse();
				//				paramAfficheAdresse.setIdTiers(((TaTiersDTO)selectedTiers.getValue()).getID_TIERS());
				//				
				//				Shell s = new Shell(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),LgrShellUtil.styleLgr);
				//				PaAdresseSWT paAdresseSWT = new PaAdresseSWT(s,SWT.NULL);
				//				SWTPaAdresseController swtPaAdresseController = new SWTPaAdresseController(paAdresseSWT);
				//				
				//				swtPaAdresseController.addRetourEcranListener(getThis()); 
				//				LgrShellUtil.afficheSWT(paramAfficheAdresse, null, paAdresseSWT, swtPaAdresseController, s);

				LgrPartListener.getLgrPartListener().setLgrActivePart(null);
				IEditorPart e = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().
				openEditor(new EditorInputAdresse(), "fr.legrain.tiers.editor.EditorAdresse");
				LgrPartListener.getLgrPartListener().getLgrNavigation().add(e);
				LgrPartListener.getLgrPartListener().setLgrActivePart(e);
				//				SWTPaAdresseController swtPaAdresseController = new SWTPaAdresseController(((EditorAdresse)e).getComposite());
				//				
				//				((EJBLgrEditorPart)e).setController(swtPaAdresseController);
				//				((EJBLgrEditorPart)e).setPanel(((EditorAdresse)e).getComposite());

				ParamAfficheAdresse paramAfficheAdresse = new ParamAfficheAdresse();
				paramAfficheAdresse.setIdTiers(LibConversion.integerToString(swtTiers.getId()));
				((EJBLgrEditorPart)e).setPanel(((EJBLgrEditorPart)e).getController().getVue());
				((EJBLgrEditorPart)e).getController().configPanel(paramAfficheAdresse);
			} catch (Exception e1) {
				logger.error("Erreur : actionPerformed", e1);
			}
			return event;
		}
	}

	protected class HandlerAjoutCommercial extends LgrAbstractHandler {

		public Object execute(ExecutionEvent event) throws ExecutionException{
			try {
				//				ParamAfficheCommercial paramAfficheCommercial= new ParamAfficheCommercial();
				//				paramAfficheCommercial.setIdTiers(((TaTiersDTO)selectedTiers.getValue()).getID_TIERS());
				//				
				//				Shell s = new Shell(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),LgrShellUtil.styleLgr);
				//				PaCommercialSWT paCommercialSWT = new PaCommercialSWT(s,SWT.NULL);
				//				SWTPaCommercialController swtPaCommercialController = new SWTPaCommercialController(paCommercialSWT);
				//				
				//				swtPaCommercialController.addRetourEcranListener(getThis()); 				 
				//				LgrShellUtil.afficheSWT(paramAfficheCommercial, null, paCommercialSWT, swtPaCommercialController, s);
				LgrPartListener.getLgrPartListener().setLgrActivePart(null);
				IEditorPart e = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new EditorInputCommercial(), "fr.legrain.tiers.editor.EditorCommercial");
				LgrPartListener.getLgrPartListener().getLgrNavigation().add(e);
				LgrPartListener.getLgrPartListener().setLgrActivePart(e);
				//				SWTPaCommercialController swtPaCommercialController = new SWTPaCommercialController(((EditorCommercial)e).getComposite());
				//				
				//				((EJBLgrEditorPart)e).setController(swtPaCommercialController);
				//				((EJBLgrEditorPart)e).setPanel(((EditorCommercial)e).getComposite());

				ParamAfficheCommercial paramAfficheCommercial = new ParamAfficheCommercial();
				paramAfficheCommercial.setIdTiers(LibConversion.integerToString(swtTiers.getId()));
				((EJBLgrEditorPart)e).setPanel(((EJBLgrEditorPart)e).getController().getVue());
				((EJBLgrEditorPart)e).getController().configPanel(paramAfficheCommercial);

			} catch (Exception e1) {
				logger.error("Erreur : actionPerformed", e1);
			}
			return event;
		}
	}

	protected class HandlerAjoutCompl extends LgrAbstractHandler {

		public Object execute(ExecutionEvent event) throws ExecutionException{
			try {
				//				ParamAfficheCompl paramAfficheCompl= new ParamAfficheCompl();
				//				paramAfficheCompl.setIdTiers(((TaTiersDTO)selectedTiers.getValue()).getID_TIERS());
				//				
				//				Shell s = new Shell(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),LgrShellUtil.styleLgr);
				//				PaComplSWT paComplSWT = new PaComplSWT(s,SWT.NULL);
				//				SWTPaComplController swtPaComplController = new SWTPaComplController(paComplSWT);
				//				swtPaComplController.addRetourEcranListener(getThis());
				//				LgrShellUtil.afficheSWT(paramAfficheCompl, null, paComplSWT, swtPaComplController, s);
				LgrPartListener.getLgrPartListener().setLgrActivePart(null);
				IEditorPart e = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new EditorInputComplement(), "fr.legrain.tiers.editor.EditorComplement");
				LgrPartListener.getLgrPartListener().getLgrNavigation().add(e);
				LgrPartListener.getLgrPartListener().setLgrActivePart(e);
				//				SWTPaComplController swtPaComplController = new SWTPaComplController(((EditorComplement)e).getComposite());
				//				
				//				((EJBLgrEditorPart)e).setController(swtPaComplController);
				//				((EJBLgrEditorPart)e).setPanel(((EditorComplement)e).getComposite());

				ParamAfficheCompl paramAfficheCompl = new ParamAfficheCompl();
				paramAfficheCompl.setIdTiers(swtTiers.getId());
				((EJBLgrEditorPart)e).setPanel(((EJBLgrEditorPart)e).getController().getVue());
				((EJBLgrEditorPart)e).getController().configPanel(paramAfficheCompl);

			} catch (Exception e1) {
				logger.error("Erreur : actionPerformed", e1);
			}
			return event;
		}
	}

	protected class HandlerAjoutConditionPaiement extends LgrAbstractHandler {

		public Object execute(ExecutionEvent event) throws ExecutionException{
			try {
				//				//PaConditionPaiement
				//				ParamAfficheConditionPaiement paramAfficheConditionPaiement= new ParamAfficheConditionPaiement();
				//				paramAfficheConditionPaiement.setIdTiers(LibConversion.integerToString(((TaTiersDTO)selectedTiers.getValue()).getID_TIERS()));
				//				Shell s = new Shell(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),LgrShellUtil.styleLgr);
				//				PaConditionPaiementSWT paConditionPaiementSWT = new PaConditionPaiementSWT(s,SWT.NULL);
				//				SWTPaConditionPaiementController swtPaConditionPaiementController = new SWTPaConditionPaiementController(paConditionPaiementSWT);
				//				swtPaConditionPaiementController.addRetourEcranListener(getThis());
				//				LgrShellUtil.afficheSWT(paramAfficheConditionPaiement, null, paConditionPaiementSWT, swtPaConditionPaiementController, s);
				LgrPartListener.getLgrPartListener().setLgrActivePart(null);
				IEditorPart e = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new EditorInputConditionPaiement(), "fr.legrain.tiers.editor.EditorConditionPaiement");
				LgrPartListener.getLgrPartListener().getLgrNavigation().add(e);
				LgrPartListener.getLgrPartListener().setLgrActivePart(e);
				//				SWTPaConditionPaiementController swtPaConditionPaiementController = new SWTPaConditionPaiementController(((EditorConditionPaiement)e).getComposite());
				//				
				//				((EJBLgrEditorPart)e).setController(swtPaConditionPaiementController);
				//				((EJBLgrEditorPart)e).setPanel(((EditorConditionPaiement)e).getComposite());

				ParamAfficheConditionPaiement paramAfficheConditionPaiement = new ParamAfficheConditionPaiement();
				paramAfficheConditionPaiement.setIdTiers(swtTiers.getId());
				((EJBLgrEditorPart)e).setPanel(((EJBLgrEditorPart)e).getController().getVue());
				((EJBLgrEditorPart)e).getController().configPanel(paramAfficheConditionPaiement);
			} catch (Exception e1) {
				logger.error("Erreur : actionPerformed", e1);
			}
			return event;
		}
	}

	protected class HandlerAjoutEmail extends LgrAbstractHandler {

		public Object execute(ExecutionEvent event) throws ExecutionException{
			try {
				//				ParamAfficheEmail paramAfficheEmail= new ParamAfficheEmail();
				//				paramAfficheEmail.setIdTiers(LibConversion.integerToString(((TaTiersDTO)selectedTiers.getValue()).getID_TIERS()));
				//				Shell s = new Shell(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),LgrShellUtil.styleLgr);
				//				PaEmailSWT paEmailSWT = new PaEmailSWT(s,SWT.NULL);
				//				SWTPaEmailController swtPaEmailController = new SWTPaEmailController(paEmailSWT);
				//				swtPaEmailController.addRetourEcranListener(getThis());
				//				LgrShellUtil.afficheSWT(paramAfficheEmail, null, paEmailSWT, swtPaEmailController, s);
				LgrPartListener.getLgrPartListener().setLgrActivePart(null);
				IEditorPart e = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new EditorInputEmail(), "fr.legrain.tiers.editor.EditorEmail");
				LgrPartListener.getLgrPartListener().getLgrNavigation().add(e);
				LgrPartListener.getLgrPartListener().setLgrActivePart(e);
				//				SWTPaEmailController swtPaEmailController = new SWTPaEmailController(((EditorEmail)e).getComposite());
				//				
				//				((EJBLgrEditorPart)e).setController(swtPaEmailController);
				//				((EJBLgrEditorPart)e).setPanel(((EditorEmail)e).getComposite());

				ParamAfficheEmail paramAfficheEmail = new ParamAfficheEmail();
				paramAfficheEmail.setIdTiers(LibConversion.integerToString(swtTiers.getId()));
				((EJBLgrEditorPart)e).setPanel(((EJBLgrEditorPart)e).getController().getVue());
				((EJBLgrEditorPart)e).getController().configPanel(paramAfficheEmail);
			} catch (Exception e1) {
				logger.error("Erreur : actionPerformed", e1);
			}
			return event;
		}
	}

	protected class HandlerAjoutSiteWeb extends LgrAbstractHandler {

		public Object execute(ExecutionEvent event) throws ExecutionException{
			try {
				//				//PaWeb
				//				ParamAfficheWeb paramAfficheWeb= new ParamAfficheWeb();
				//				paramAfficheWeb.setIdTiers(((TaTiersDTO)selectedTiers.getValue()).getID_TIERS());
				//				
				//				Shell s = new Shell(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),LgrShellUtil.styleLgr);
				//				PaWebSWT paWebSWT = new PaWebSWT(s,SWT.NULL);
				//				SWTPaWebController swtPaWebController = new SWTPaWebController(paWebSWT);
				//				swtPaWebController.addRetourEcranListener(getThis());
				//				LgrShellUtil.afficheSWT(paramAfficheWeb, null, paWebSWT, swtPaWebController, s);
				LgrPartListener.getLgrPartListener().setLgrActivePart(null);
				IEditorPart e = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new EditorInputWeb(), "fr.legrain.tiers.editor.EditorWeb");
				LgrPartListener.getLgrPartListener().getLgrNavigation().add(e);
				LgrPartListener.getLgrPartListener().setLgrActivePart(e);
				//				SWTPaWebController swtPaWebController = new SWTPaWebController(((EditorWeb)e).getComposite());
				//				
				//				((EJBLgrEditorPart)e).setController(swtPaWebController);
				//				((EJBLgrEditorPart)e).setPanel(((EditorWeb)e).getComposite());

				ParamAfficheWeb paramAfficheWeb = new ParamAfficheWeb();
				paramAfficheWeb.setIdTiers(swtTiers.getId());
				((EJBLgrEditorPart)e).setPanel(((EJBLgrEditorPart)e).getController().getVue());
				((EJBLgrEditorPart)e).getController().configPanel(paramAfficheWeb);
			} catch (Exception e1) {
				logger.error("Erreur : actionPerformed", e1);
			}
			return event;
		}
	}

	protected class HandlerAjoutTelephone extends LgrAbstractHandler {

		public Object execute(ExecutionEvent event) throws ExecutionException{
			try {
				//				//PaTelephone
				//				ParamAfficheTelephone paramAfficheTelephone= new ParamAfficheTelephone();
				//				paramAfficheTelephone.setIdTiers(((TaTiersDTO)selectedTiers.getValue()).getID_TIERS());
				//				
				//				Shell s = new Shell(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),LgrShellUtil.styleLgr);
				//				PaTelephoneSWT paTelephoneSWT = new PaTelephoneSWT(s,SWT.NULL);
				//				SWTPaTelephoneController swtPaTelephoneController = new SWTPaTelephoneController(paTelephoneSWT);
				//				swtPaTelephoneController.addRetourEcranListener(getThis()); 
				//				LgrShellUtil.afficheSWT(paramAfficheTelephone, null, paTelephoneSWT, swtPaTelephoneController, s);
				LgrPartListener.getLgrPartListener().setLgrActivePart(null);
				IEditorPart e = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new EditorInputTelephone(), "fr.legrain.tiers.editor.EditorTelephone");
				LgrPartListener.getLgrPartListener().getLgrNavigation().add(e);
				LgrPartListener.getLgrPartListener().setLgrActivePart(e);
				//				SWTPaTelephoneController swtPaTelephoneController = new SWTPaTelephoneController(((EditorTelephone)e).getComposite());
				//				
				//				((EJBLgrEditorPart)e).setController(swtPaTelephoneController);
				//				((EJBLgrEditorPart)e).setPanel(((EditorTelephone)e).getComposite());

				ParamAfficheTelephone paramAfficheTelephone = new ParamAfficheTelephone();
				paramAfficheTelephone.setIdTiers(swtTiers.getId());
				((EJBLgrEditorPart)e).setPanel(((EJBLgrEditorPart)e).getController().getVue());
				((EJBLgrEditorPart)e).getController().configPanel(paramAfficheTelephone);
			} catch (Exception e1) {
				logger.error("Erreur : actionPerformed", e1);
			}
			return event;
		}
	}
	
	protected class HandlerOuvrirCourrierTiers extends LgrAbstractHandler {

		public Object execute(ExecutionEvent event) throws ExecutionException{
			try {
				new TiersUtil().ouvreRepertoireCourrierTiers(taTiers);
			} catch (Exception e1) {
				logger.error("Erreur : actionPerformed", e1);
			}
			return event;
		}
	}
	
	
	protected class HandlerPublipostageTiers extends LgrAbstractHandler {

		public Object execute(ExecutionEvent event) throws ExecutionException{
			try {
				listeDocumentsFinaux.clear();
				boolean publipostage = true; //vrai si les documents .doc et .odt doivent être traités avec les fonctions de publipostage/fusion
				boolean fusionPublipostage = vue.getBtnFusionPublipostage().getSelection(); //vrai si on souhaite obtenir au final un seul document par type de logiciel de traitement de texte utilisé
				
				final String timeStamp = LibDate.dateToStringTimeStamp(new Date(),"","_","");
				
				//Création du fichier de données pour le tiers sélectionné
				FichierDonneesAdresseTiers fichierDonneesAdresseTiers = new FichierDonneesAdresseTiers();
				List<TaTiers> listeTiersPublipostage = new ArrayList<TaTiers>();
				listeTiersPublipostage.add(taTiers);
				
				File cheminFichierMotCleDefaut = LgrFileBundleLocator.bundleToFile(fr.legrain.publipostage.Activator.getDefault().getBundle(), "/default/motcle.properties");

				String repertTravail = Const.C_CHEMIN_REP_TMP_COMPLET;
				String repertFinal = null;
				final TiersUtil tiersUtil = new TiersUtil();
				repertFinal = tiersUtil.repertoireCourrierTiers(taTiers);
				
				final File fileRepertFinal = new File(repertFinal);
				if(fileRepertFinal.mkdirs()) {
					logger.info("Création du répertoire :"+repertFinal);
				}
				
				String cheminFichierDonnees = new File(repertTravail+"/Tiers"+"-"+timeStamp+".txt").getPath();

				fichierDonneesAdresseTiers.creationFichierDonneesAdresse(listeTiersPublipostage,repertTravail,cheminFichierDonnees,false);
				
				final TypeVersionPublipostage typeVersion = TypeVersionPublipostage.getInstance();

				//Pour chaque case de document coché créer initialisé un objet ParamPublipostage
				final Map<String,List<ParamPublipostage>> listeParamPubliParTypeLogiciel = new HashMap<String,List<ParamPublipostage>>();
				for (Button b : listeModelesPublipostage.keySet()) {
					if(b.getSelection()) {

						String documentName = new File(listeModelesPublipostage.get(b).getCheminModelDocumentTiers()).getName();
						String extensionDocument = documentName.substring(documentName.lastIndexOf(".")+1);

						if(publipostage && typeVersion.getLogicielParExtension().containsKey(extensionDocument)) {
							//c'est un fichier de traitement de texte, on peu faire un publipostage
							ParamPublipostage param = new ParamPublipostage();
							param.setCheminFichierDonnees(cheminFichierDonnees);

							if(listeModelesPublipostage.get(b).getDefaut()==1){
								param.setCheminFichierModelLettre(Const.C_REPERTOIRE_BASE+Const.C_REPERTOIRE_PROJET+"/"+Const.FOLDER_MODEL_LETTRE+"/"+listeModelesPublipostage.get(b).getCheminModelDocumentTiers());
								param.setCheminFichierMotCle(cheminFichierMotCleDefaut.getAbsolutePath());
							}
							else{
//								param.setCheminFichierModelLettre(listeModelesPublipostage.get(b).getCheminModelDocumentTiers());
//								param.setCheminFichierMotCle(listeModelesPublipostage.get(b).getCheminCorrespDocumentTiers());
								param.setCheminFichierModelLettre(listeModelesPublipostage.get(b).getCheminModelDocumentTiers());
								if(listeModelesPublipostage.get(b).getCheminCorrespDocumentTiers()!=null && !listeModelesPublipostage.get(b).getCheminCorrespDocumentTiers().equals("")) {
									param.setCheminFichierMotCle(listeModelesPublipostage.get(b).getCheminCorrespDocumentTiers());
								} else {
									//param.setCheminFichierMotCle(fichierMotCle.getAbsolutePath());
									param.setCheminFichierMotCle(cheminFichierMotCleDefaut.getAbsolutePath());
								}
							}

							//param.setCheminFichierFinal(repert+"/Tiers"+"-"+listeModelesPublipostage.get(b).getName()+"-"+LibDate.dateToString(new Date(),"")+".txt");
							String repDoc = null;
							if(!fusionPublipostage) {
								repDoc = repertFinal;
							} else {
								repDoc = repertTravail;
							}
							param.setCheminFichierFinal(repDoc+"/Tiers"+"-"+listeModelesPublipostage.get(b).getLibelleDocumentTiers()+"-"+timeStamp+"."+extensionDocument);
							param.setCheminRepertoireFinal(repertFinal);
							if(listeParamPubliParTypeLogiciel.get(typeVersion.getLogicielParExtension().get(extensionDocument))==null) {
								listeParamPubliParTypeLogiciel.put(typeVersion.getLogicielParExtension().get(extensionDocument), new ArrayList<ParamPublipostage>());
							}
							listeParamPubliParTypeLogiciel.get(typeVersion.getLogicielParExtension().get(extensionDocument)).add(param);

							if(!fusionPublipostage) {
								//Il faut une liste par document pour eviter les problèmes de thread
								final List<ParamPublipostage> listeParamPubliSansFusion = new ArrayList<ParamPublipostage>();
								listeParamPubliSansFusion.add(param);
								String extensionFinale = typeVersion.getExtensionFinaleDefaut().get(typeVersion.getLogicielParExtension().get(extensionDocument));
								final String nonFichierFinal = "publiTiersFusion-"+taTiers.getCodeTiers()+"-"+listeModelesPublipostage.get(b).getLibelleDocumentTiers()+"-"+timeStamp+extensionFinale;
								lancePublipostage(typeVersion.getLogicielParExtension().get(extensionDocument), nonFichierFinal, listeParamPubliParTypeLogiciel.get(typeVersion.getLogicielParExtension().get(extensionDocument)), wait);
								listeDocumentsFinaux.add(param.getCheminFichierFinal()/*+"/"+nonFichierFinal*/);
							}
						} else {
							// autre type de doc, pas de publipostage, on essai juste de l'ouvrir
							final String finalURL = new File(listeModelesPublipostage.get(b).getCheminModelDocumentTiers()).getAbsolutePath();
							listeDocumentsFinaux.add(finalURL);
							PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
								public void run() {
									File file = new File(finalURL);
									String cheminCompletDest = fileRepertFinal.getPath()+"/";
									cheminCompletDest += file.getName().substring(0,file.getName().lastIndexOf("."));
									cheminCompletDest += "_"+timeStamp;
									cheminCompletDest += file.getName().substring(file.getName().lastIndexOf("."));
									File destFile = new File(cheminCompletDest);
									if (Desktop.isDesktopSupported()) {
										Desktop desktop = Desktop.getDesktop();
										if (desktop.isSupported(Desktop.Action.OPEN)) {
											try {
												if(file.exists()) {
													TiersUtil.copyFile(file, destFile);
													desktop.open(destFile);
												}
												else
													MessageDialog.openError(vue.getShell(), "Erreur", 
													"Le chemin est invalide ou inaccessible pour l'instant.");
											} catch (IOException e) {
												logger.error("",e);
											}
										}
									}
								}	
							});
						}
					}
				}

				//Finir de paramétrer les paramètres généraux du publipostage (fichier final, ...)
				//lancer le publipostage dans un thread
				if(fusionPublipostage) {
					//il y une liste de publipostage a générer
					for (String typeLogiciel : listeParamPubliParTypeLogiciel.keySet()) {
						String extensionFinale = typeVersion.getExtensionFinaleDefaut().get(typeLogiciel);
						final String nomFichierFinal = "publiTiersFusion-"+taTiers.getCodeTiers()+"-"+timeStamp+extensionFinale;
						lancePublipostage(typeLogiciel, nomFichierFinal, listeParamPubliParTypeLogiciel.get(typeLogiciel),wait);
						listeDocumentsFinaux.add(nomFichierFinal);
					}
				}

			} catch (Exception e1) {
				logger.error("Erreur : actionPerformed", e1);
			}
			return event;
		}
	}
	
	protected class HandlerSendMailDocmuentsTiers extends LgrAbstractHandler {

		public Object execute(ExecutionEvent event) throws ExecutionException{
			try {
				wait = true;
				handlerPublipostageTiers.execute(null);
				wait = false;
				if(listeDocumentsFinaux!=null && listeDocumentsFinaux.size()>0) {
					//String[] fichierAJoindre = new String[listeDocumentsFinaux.size()];
					Map<String, String> fichierAJoindre = new HashMap<String, String>();
					//int i = 0;
					for (String cheminFichier : listeDocumentsFinaux) {
						fichierAJoindre.put(cheminFichier, null);
						//fichierAJoindre[i] = cheminFichier;
						//i++;
					}
					
					OpenmailMail om= new OpenmailMail();
					if(taTiers!=null && taTiers.getTaEmail()!=null && taTiers.getTaEmail().getAdresseEmail()!=null) {
						om.sendMail(new String[] {taTiers.getTaEmail().getAdresseEmail()},
								null,
								null,
								fichierAJoindre);
					}
				}
				System.out
						.println("SWTPaTiersController.HandlerSendMailTiers.execute()");
			} catch (Exception e1) {
				wait = false;
				logger.error("Erreur : actionPerformed", e1);
			}
			return event;
		}
	}
	
	protected class HandlerSendMailTiers extends LgrAbstractHandler {

		public Object execute(ExecutionEvent event) throws ExecutionException{
			try {
				OpenmailMail om= new OpenmailMail();
				if(taTiers!=null && taTiers.getTaEmail()!=null && taTiers.getTaEmail().getAdresseEmail()!=null) {
					om.sendMail(taTiers.getTaEmail().getAdresseEmail());
				}
				System.out
						.println("SWTPaTiersController.HandlerSendMailTiers.execute()");
			} catch (Exception e1) {
				logger.error("Erreur : actionPerformed", e1);
			}
			return event;
		}
	}
	
	private class MyPopup extends PopupDialog {
		//org.eclipse.jdt.internal.ui.text.JavaOutlineInformationControl

		private TaTiers t = null;

		public MyPopup(TaTiers t, Shell parent, int shellStyle, boolean takeFocusOnOpen,
				boolean persistSize, boolean persistLocation,
				boolean showDialogMenu, boolean showPersistActions,
				String titleText, String infoText) {
			super(parent, shellStyle, takeFocusOnOpen, persistSize, persistLocation,
					showDialogMenu, showPersistActions, titleText, infoText);
			
			this.t = t;	
		}

		@Override
		protected Control createDialogArea(Composite parent) {

			Composite c = new Composite(parent,0);
			c.setLayout(new FillLayout());
			if(t!=null) {
				for (TaTelephone tel : t.getTaTelephones()) {
					Label l = new Label(c, SWT.BORDER);
					l.setText(tel.getNumeroTelephone());
				}
			}
			
			parent.layout();
			
			//return super.createDialogArea(parent);
			return c;
		}
	}
	
	protected class HandlerSendSMSTiers extends LgrAbstractHandler {

		public Object execute(ExecutionEvent event) throws ExecutionException{
			try {
				
//				SWTPaBrowserController.openURL("http://legrain.fr",null,null, "", "");
	
				OpenmailSMS om= new OpenmailSMS();
				
				if(taTiers.getTaTelephones().size()>=2) {
					//PopupDialog dia = new MyPopup(taTiers, vue.getShell(), SWT.NONE, true, false, false, true, true, "Choix du numéro", "infos");
					//dia.open();
					ElementListSelectionDialog dialog = 
							new ElementListSelectionDialog(vue.getShell(), new LabelProvider());
					String[] content = new String[taTiers.getTaTelephones().size()];
					int i = 0;
					for (TaTelephone tel : taTiers.getTaTelephones()) {
						content[i] = tel.getNumeroTelephone();
						i++;
					}
					dialog.setElements(content);
					dialog.setTitle("Choix du numéro");
					// User pressed cancel
					if (dialog.open() != Window.OK) {
						return false;
					}
					Object[] result = dialog.getResult();
					if(result!=null)
						//System.out.println(result[0]);
						om.sendSMS(result[0].toString());
					else
						System.out.println("vide");
				} else {
					if(taTiers!=null && taTiers.getTaTelephone()!=null && taTiers.getTaTelephone().getNumeroTelephone()!=null) {
						om.sendSMS(taTiers.getTaTelephone().getNumeroTelephone());
					}
				}
				System.out
						.println("SWTPaTiersController.HandlerSendSMSTiers.execute()");
			} catch (Exception e1) {
				logger.error("Erreur : actionPerformed", e1);
			}
			return event;
		}
	}
	
	protected class HandlerSendFaxTiers extends LgrAbstractHandler {

		public Object execute(ExecutionEvent event) throws ExecutionException{
			try {
				
//				SWTPaBrowserController.openURL("http://legrain.fr",null,null, "", "");
	
				OpenmailFAX om = new OpenmailFAX();
				
				if(taTiers.getTaTelephones().size()>=2) {
					//PopupDialog dia = new MyPopup(taTiers, vue.getShell(), SWT.NONE, true, false, false, true, true, "Choix du numéro", "infos");
					//dia.open();
					ElementListSelectionDialog dialog = new ElementListSelectionDialog(vue.getShell(), new LabelProvider());
					String[] content = new String[taTiers.getTaTelephones().size()];
					int i = 0;
					for (TaTelephone tel : taTiers.getTaTelephones()) {
						content[i] = tel.getNumeroTelephone();
						i++;
					}
					dialog.setElements(content);
					dialog.setTitle("Choix du numéro");
					// User pressed cancel
					if (dialog.open() != Window.OK) {
						return false;
					}
					Object[] result = dialog.getResult();
					if(result!=null)
						//System.out.println(result[0]);
						om.sendFax(result[0].toString());
					else
						System.out.println("vide");
				} else {
					if(taTiers!=null && taTiers.getTaTelephone()!=null && taTiers.getTaTelephone().getNumeroTelephone()!=null) {
						om.sendFax(taTiers.getTaTelephone().getNumeroTelephone());
					}
				}
				System.out
						.println("SWTPaTiersController.HandlerSendSMSTiers.execute()");
			} catch (Exception e1) {
				logger.error("Erreur : actionPerformed", e1);
			}
			return event;
		}
	}
	
	//private void lancePublipostage(final TypeVersionPublipostage typeVersion, final String nonFichierFinal, final List<ParamPublipostage> listeParamPubli, final TaParamPublipostage taParamPublipostage) {
	private void lancePublipostage(final String typeVersion, final String nonFichierFinal, final List<ParamPublipostage> listeParamPubli, boolean wait) {
		Thread t = new Thread() {

			@Override
			public void run() {
				//if(typeVersion.getType().get(taParamPublipostage.getLogicielPublipostage()).equals(TypeVersionPublipostage.TYPE_OPENOFFICE)){
				if(typeVersion.equals(TypeVersionPublipostage.TYPE_OPENOFFICE)){

					//PublipostageOOoWin32 pub = new PublipostageOOoWin32(listeParamPubli);
					AbstractPublipostageOOo pub = PublipostageOOoFactory.createPublipostageOOo(listeParamPubli);
					String pathOpenOffice = "";
					try {
						if(Platform.getOS().equals(Platform.OS_WIN32)){
							pathOpenOffice = WinRegistry.readString(
									WinRegistry.HKEY_LOCAL_MACHINE,
									WinRegistry.KEY_REGISTR_WIN_OPENOFFICE,
							"");
						} else if(Platform.getOS().equals(Platform.OS_LINUX)){
							pathOpenOffice = "/usr/bin/soffice";
						} else if(Platform.getOS().equals(Platform.OS_MACOSX)) {}
					}
					catch (Exception e3) {
						logger.error("",e3);
					}    

					pub.setCheminOpenOffice(new File(pathOpenOffice).getPath());
					pub.setPortOpenOffice("8100");
					pub.setNomFichierFinalFusionne(new File(nonFichierFinal).getPath());
					pub.demarrerServeur();
					pub.publipostages();

					//}else if(typeVersion.getType().get(taParamPublipostage.getLogicielPublipostage()).
				}else if(typeVersion.equals(TypeVersionPublipostage.TYPE_MSWORD)){
					PublipostageMsWord pub = new PublipostageMsWord(listeParamPubli);
					pub.setNomFichierFinalFusionne(new File(nonFichierFinal).getPath());
					pub.publipostages();
				}

			}//fin run
		}; //fin thread
		t.start();
		
		if(wait) {
			try {
				t.join();
			} catch (InterruptedException e) {
				logger.error("",e);
			}
		}
	}

	class ActionAjoutAdresse extends Action {
		public ActionAjoutAdresse(String name) {
			super(name);
		}

		public void run() {
			super.run();
			try {
				handlerService.executeCommand(C_COMMAND_ADRESSE_ID, null);
			} catch (ExecutionException e) {
				logger.error("",e);
			} catch (NotDefinedException e) {
				logger.error("",e);
			} catch (NotEnabledException e) {
				logger.error("",e);
			} catch (NotHandledException e) {
				logger.error("",e);
			}
		}
	}

	class ActionAjoutTelephone extends Action {
		public ActionAjoutTelephone(String name) {
			super(name);
		}

		public void run() {
			super.run();
			try {
				handlerService.executeCommand(C_COMMAND_TELEPHONE_ID, null);
			} catch (ExecutionException e) {
				logger.error("",e);
			} catch (NotDefinedException e) {
				logger.error("",e);
			} catch (NotEnabledException e) {
				logger.error("",e);
			} catch (NotHandledException e) {
				logger.error("",e);
			}
		}
	}

	class ActionAjoutConditionPaiement extends Action {
		public ActionAjoutConditionPaiement(String name) {
			super(name);
		}

		public void run() {
			super.run();
			try {
				handlerService.executeCommand(C_COMMAND_CONDITION_PAIEMENT_ID, null);
			} catch (ExecutionException e) {
				logger.error("",e);
			} catch (NotDefinedException e) {
				logger.error("",e);
			} catch (NotEnabledException e) {
				logger.error("",e);
			} catch (NotHandledException e) {
				logger.error("",e);
			}
		}
	}

	class ActionAjoutEmail extends Action {
		public ActionAjoutEmail(String name) {
			super(name);
		}

		public void run() {
			super.run();
			try {
				handlerService.executeCommand(C_COMMAND_EMAIL_ID, null);
			} catch (ExecutionException e) {
				logger.error("",e);
			} catch (NotDefinedException e) {
				logger.error("",e);
			} catch (NotEnabledException e) {
				logger.error("",e);
			} catch (NotHandledException e) {
				logger.error("",e);
			}
		}
	}

	class ActionAjoutCommercial extends Action {
		public ActionAjoutCommercial(String name) {
			super(name);
		}

		public void run() {
			super.run();
			try {
				handlerService.executeCommand(C_COMMAND_COMMERCIAL_ID, null);
			} catch (ExecutionException e) {
				logger.error("",e);
			} catch (NotDefinedException e) {
				logger.error("",e);
			} catch (NotEnabledException e) {
				logger.error("",e);
			} catch (NotHandledException e) {
				logger.error("",e);
			}
		}
	}


	class ActionAjoutSiteWeb extends Action {
		public ActionAjoutSiteWeb(String name) {
			super(name);
		}

		public void run() {
			super.run();
			try {
				handlerService.executeCommand(C_COMMAND_WEB_ID, null);
			} catch (ExecutionException e) {
				logger.error("",e);
			} catch (NotDefinedException e) {
				logger.error("",e);
			} catch (NotEnabledException e) {
				logger.error("",e);
			} catch (NotHandledException e) {
				logger.error("",e);
			}
		}
	}

	class ActionAjoutCompl extends Action {
		public ActionAjoutCompl(String name) {
			super(name);
		}

		public void run() {
			super.run();
			try {
				handlerService.executeCommand(C_COMMAND_COMPLEMENT_ID, null);
			} catch (ExecutionException e) {
				logger.error("",e);
			} catch (NotDefinedException e) {
				logger.error("",e);
			} catch (NotEnabledException e) {
				logger.error("",e);
			} catch (NotHandledException e) {
				logger.error("",e);
			}
		}
	}


	public SWTPaTiersController getThis(){
		return this;
	}

	@Override
	public boolean onClose() throws ExceptLgr {
		boolean retour = true;
		VerrouInterface.setVerrouille(true);
		switch (getModeEcran().getMode()) {
		case C_MO_INSERTION:
		case C_MO_EDITION:
			if(MessageDialog.openQuestion(
					vue.getShell(),
					MessagesEcran.getString("Message.Attention"),
					MessagesEcran.getString("Tiers.Message.Enregistrer"))) {

				try {
					actEnregistrer();
				} catch(Exception e) {
					vue.getLaMessage().setText(e.getMessage());
					logger.error("",e);
					retour= false;
				}
			} else {
				silencieu = true;
				try {
					actAnnuler();
					//fireAnnuleTout(new AnnuleToutEvent(this));
				} catch (Exception e) {
					logger.error("",e);
					vue.getLaMessage().setText(e.getMessage());
				}
			}

			break;
		case C_MO_CONSULTATION:
			break;
		default:
			break;
		}

		if(retour) {
			if (ecranAppelant instanceof SWTPaAideControllerSWT) {
				setListeEntity(getModelTiers().remplirListe());
				dao.initValeurIdTable(taTiers);
				fireRetourEcran(new RetourEcranEvent(this, new ResultAide(
						dao.getChampIdEntite(), dao.getValeurIdTable(),
						swtTiers/*selectedTiers.getValue()*/)));
				retour = true;
			}
		} 
		//		else {
		//			setFocusCourantSWT(ibTaTable.getFModeObjet().getFocusCourantSWT());
		//			getFocusCourantSWT().setFocus();
		//		}


		return retour;
	}

	public void retourEcran(final RetourEcranEvent evt) {
		//LgrPartListener.getLgrPartListener().setLgrActivePart(null);
		if (evt.getRetour() != null
				&& (evt.getSource() instanceof SWTPaAideControllerSWT)) {
			if (getFocusAvantAideSWT() instanceof Text) {
				try {
					((Text) getFocusAvantAideSWT()).setText(((ResultAffiche) evt
							.getRetour()).getResult());		
					if(getFocusAvantAideSWT().equals(vue.getTfEntite())){
						TaTEntite entity = null;
						//TaTEntiteDAO dao = new TaTEntiteDAO(getEm());
						ITaTEntiteServiceRemote dao = new EJBLookup<ITaTEntiteServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_T_ENTITE_SERVICE, ITaTTiersServiceRemote.class.getName());
						entity = dao.findById(Integer.parseInt(((ResultAffiche) evt.getRetour()).getIdResult()));
						dao = null;
						taTiers.setTaTEntite(entity);
					}
					if(getFocusAvantAideSWT().equals(vue.getTfCODE_T_TVA_DOC())){
						TaTTvaDoc entity = null;
						//TaTTvaDocDAO dao = new TaTTvaDocDAO(getEm());
						ITaTTvaDocServiceRemote dao = new EJBLookup<ITaTTvaDocServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_T_TVA_DOC_SERVICE, ITaTTvaDocServiceRemote.class.getName());
						entity = dao.findById(Integer.parseInt(((ResultAffiche) evt.getRetour()).getIdResult()));
						dao = null;
						taTiers.setTaTTvaDoc(entity);
					}
					if(getFocusAvantAideSWT().equals(vue.getTfCODE_T_CIVILITE())){
						TaTCivilite entity = null;
						//TaTCiviliteDAO dao = new TaTCiviliteDAO(getEm());
						ITaTCiviliteServiceRemote dao = new EJBLookup<ITaTCiviliteServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_T_CIVILITE_SERVICE, ITaTCiviliteServiceRemote.class.getName());
						entity = dao.findById(Integer.parseInt(((ResultAffiche) evt.getRetour()).getIdResult()));
						dao = null;
						taTiers.setTaTCivilite(entity);
					}
					if(getFocusAvantAideSWT().equals(vue.getTfCODE_C_PAIEMENT())){
						TaCPaiement entity = null;
						//TaCPaiementDAO dao = new TaCPaiementDAO(getEm());
						ITaCPaiementServiceRemote dao = new EJBLookup<ITaCPaiementServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_C_PAIEMENT_SERVICE, ITaCPaiementServiceRemote.class.getName());
						entity = dao.findById(Integer.parseInt(((ResultAffiche) evt.getRetour()).getIdResult()));
						dao = null;
						taTiers.setTaCPaiement(entity);
					}
					if(getFocusAvantAideSWT().equals(vue.getTfCODE_T_TARIF())){
						TaTTarif entity = null;
						//TaTTarifDAO dao = new TaTTarifDAO(getEm());
						ITaTTarifServiceRemote dao = new EJBLookup<ITaTTarifServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_T_TARIF_SERVICE, ITaTTarifServiceRemote.class.getName());
						entity = dao.findById(Integer.parseInt(((ResultAffiche) evt.getRetour()).getIdResult()));
						dao = null;
						taTiers.setTaTTarif(entity);
					}
					if(getFocusAvantAideSWT().equals(vue.getTfCODE_T_TIERS())){
						TaTTiers entity = null;
						//TaTTiersDAO dao = new TaTTiersDAO(getEm());
						ITaTTiersServiceRemote dao = new EJBLookup<ITaTTiersServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_T_TIERS_SERVICE, ITaTTiersServiceRemote.class.getName());
						entity = dao.findById(Integer.parseInt(((ResultAffiche) evt.getRetour()).getIdResult()));
						dao = null;
						taTiers.setTaTTiers(entity);
						if(swtTiers!=null )
							swtTiers.setCompte(entity.getCompteTTiers());
					}
					if(getFocusAvantAideSWT().equals(vue.getTfCODE_T_PAIEMENT())){
						//ejb
//						TaTPaiement entity = null;
//						TaTPaiementDAO dao = new TaTPaiementDAO(getEm());
//						//ITaTTiersServiceRemote dao = new EJBLookup<ITaTTiersServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_T_TIERS_SERVICE, ITaTTiersServiceRemote.class.getName());
//						entity = dao.findById(Integer.parseInt(((ResultAffiche) evt.getRetour()).getIdResult()));
//						dao = null;
//						taTiers.setTaTPaiement(entity);
					}					
					if(getFocusAvantAideSWT().equals(vue.getTfCODE_ENTREPRISE())){
						/** va ajouter **/
					}
					ctrlUnChampsSWT(getFocusAvantAideSWT());
				} catch (Exception e) {
					logger.error("",e);
					if(getFocusAvantAideSWT()!=null)setFocusCourantSWT(getFocusAvantAideSWT());
					vue.getLaMessage().setText(e.getMessage());
				}
				if (getFocusAvantAideSWT()==vue.getTfLIBL_COMMENTAIRE()){
					setIdCommentaire(((ResultAffiche)evt.getRetour()).getIdResult());						
				}

			}			
			if (getFocusAvantAideSWT() instanceof Table) {
//				if (getFocusAvantAideSWT() == vue.getGrille()) {
//					if(((ResultAffiche) evt.getRetour()).getSelection()!=null){
//						TaTiersDTO tiersRetour =modelTiers.recherche(Const.C_ID_TIERS
//								, LibConversion.stringToInteger(((ResultAffiche) evt.getRetour()).getIdResult())); 
//						if(tiersRetour!=null){
//							tableViewer.setSelection(new StructuredSelection(tiersRetour),true);
//						}
//					}
//				}
			}
		} else if (evt.getRetour() != null){
			if (getFocusAvantAideSWT() instanceof Table) {
//				if (getFocusAvantAideSWT() == vue.getGrille()) {
//				}
			}
		}
		super.retourEcran(evt);
	}

	@Override
	protected void actInserer() throws Exception{
		try {
			if(getModeEcran().getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
				
				ITaTTiersServiceRemote daoTTiers = new EJBLookup<ITaTTiersServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_T_TIERS_SERVICE, ITaTTiersServiceRemote.class.getName());
				
				VerrouInterface.setVerrouille(true);
				setSwtOldTiers();
				String codeTiersDefaut = "C";
				
				taTiers = new TaTiers();//ne pas déplacer pour que les autres écrans récupère le bon masterEntity
				//taTiers.setTaTTiers(daoTTiers.findByCode(codeTiersDefaut));
				
				unBind(dbc);
				swtTiers = new TaTiersDTO();
				bind(vue,swtTiers);
				
				swtTiers.setCodeTiers(dao.genereCode(null)); //ejb
				validateUIField(Const.C_CODE_TIERS,swtTiers.getCodeTiers());//permet de verrouiller le code genere
				
				swtTiers.setCodeTTiers(codeTiersDefaut);
				
			swtTiers.setCodeCompta("aa"); //ejb
			validateUIField(Const.C_CODE_COMPTA,swtTiers.getCodeCompta()); //ejb
			swtTiers.setCompte("111"); //ejb
			validateUIField(Const.C_COMPTE,swtTiers.getCompte()); //ejb
				
			
				//TaTTiersDAO daoTTiers = new TaTTiersDAO(getEm());
				TaTTiers taTTiers = daoTTiers.findByCode(codeTiersDefaut);
				if(taTTiers!=null && taTTiers.getCompteTTiers()!=null) {
					swtTiers.setCompte(taTTiers.getCompteTTiers());
				} else {
					swtTiers.setCompte(TiersPlugin.getDefault().getPreferenceStore().
							getString(PreferenceConstants.TIERS_COMPTE_COMPTALE_DEFAUT));
				}
				swtTiers.setActifTiers(true);
				swtTiers.setTtcTiers(TiersPlugin.getDefault().getPreferenceStore().
						getBoolean(PreferenceConstants.TIERS_SAISIE_TTC_DEFAUT));
				swtTiers.setCodeTTvaDoc("F");

				//dao.inserer(taTiers); //=> ejb ne fait rien sur le serveur
				
				if(idTypeTiers!=null && idTypeTiers.equals(TaTiersDAO.C_ID_IDENTITE_ENTREPRISE_STR)) {
					//TaTTiersDAO daoTaTTiersDAO = new TaTTiersDAO(getEm());
					taTiers.setTaTTiers(daoTTiers.findById(TaTiersDAO.C_ID_IDENTITE_ENTREPRISE_INT));
					taTiers.setIdTiers(TaTiersDAO.C_ID_IDENTITE_ENTREPRISE_INT);
				}
				
				getModeEcran().setMode(EnumModeObjet.C_MO_INSERTION);//ejb
				initEtatBouton();
			}
		//} catch (ExceptLgr e1) {
		} catch (Exception e1) {
			vue.getLaMessage().setText(e1.getMessage());
			logger.error("Erreur : actionPerformed", e1);
		} finally {
			initEtatComposant();
			VerrouInterface.setVerrouille(false);
		}
	}

	@Override
	protected void actModifier() throws Exception{
		try {
			if(getModeEcran().getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
				if(!LgrMess.isDOSSIER_EN_RESEAU()){
					setSwtOldTiers();
					//taTiers = dao.findById(swtTiers.getId()); //ejb commentaire			
				}else{
					if(!setSwtOldTiersRefresh())throw new Exception();
				}
				dao.modifier(taTiers);
				
				modeEcran.setMode(EnumModeObjet.C_MO_EDITION);//ejb
				
				initEtatBouton();
			}
		} catch (Exception e1) {
			vue.getLaMessage().setText(e1.getMessage());
			logger.error("Erreur : actionPerformed", e1);
		}
	}

	@Override
	protected void actSupprimer() throws Exception{
//		EntityTransaction transaction = dao.getEntityManager().getTransaction();
		try {
			VerrouInterface.setVerrouille(true);
			if(isUtilise())MessageDialog.openInformation(vue.getShell(), MessagesEcran
					.getString("Message.Attention"), MessagesEcran
					.getString("Message.suppression"));
			else
				if (MessageDialog.openConfirm(vue.getShell(), MessagesEcran
						.getString("Message.Attention"), MessagesEcran
						.getString("Tiers.Message.Supprimer"))) {
//					dao.begin(transaction);
					TaTiers u = dao.findById(swtTiers.getId());
					dao.supprimer(u);
//					dao.commit(transaction);
//					Object suivant=tableViewer.getElementAt(tableViewer.getTable().getSelectionIndex()+1);
					if(containsEntity(u)) modelTiers.getListeEntity().remove(u);
					taTiers=null;
//					tableViewer.selectionGrille(0);
					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
//					if(suivant!=null)tableViewer.setSelection(new StructuredSelection(suivant),true);
//					else tableViewer.selectionGrille(0);
					
					removeView(u);
					//selectView(0);
					

				}
		} catch (ExceptLgr e1) {
			vue.getLaMessage().setText(e1.getMessage());
			logger.error("Erreur : actionPerformed", e1);
		}finally {
//			if(transaction!=null && transaction.isActive()) {
//				transaction.rollback();
//			}
			initEtatBouton();
			VerrouInterface.setVerrouille(false);
		}
	}

	@Override
	protected void actFermer() throws Exception{
		//(controles de sortie et fermeture de la fenetre) => onClose()
		if(onClose())
			closeWorkbenchPart();
	}

	@Override
	protected void actAnnuler() throws Exception{
		//		// verifier si l'objet est en modification ou en consultation
		//		// si modification ou insertion, alors demander si annulation des
		//		// modifications si ok, alors annulation si pas ok, alors arreter le processus d'annulation
		//		// si consultation declencher l'action "fermer".
		try {
			VerrouInterface.setVerrouille(true);
			//			//InputVerifier inputVerifier = getFocusCourant().getInputVerifier();
			//			//getFocusCourant().setInputVerifier(null);
			switch (getModeEcran().getMode()) {
			case C_MO_INSERTION:
				if (silencieu || (!silencieu && MessageDialog.openQuestion(
						vue.getShell(),
						MessagesEcran.getString("Message.Attention"),
						MessagesEcran.getString("Tiers.Message.Annuler")))){
					remetTousLesChampsApresAnnulationSWT(dbc);
					if(swtTiers==null){
//						modelTiers.getListeObjet().remove(swtTiers);
//						writableList =new WritableList(realm, modelTiers.getListeObjet(),classModel);
//						tableViewer.setInput(writableList);
//						tableViewer.refresh();
//						tableViewer.selectionGrille(0);
					}
					
					dao.annuler(taTiers);
					hideDecoratedFields();	
					if(!annuleToutEnCours) {
						fireAnnuleTout(new AnnuleToutEvent(this));
					}
				}
				initEtatBouton();
				selectView(0);
				break;
			case C_MO_EDITION:
				if (silencieu || (!silencieu && MessageDialog.openQuestion(
						vue.getShell(),
						MessagesEcran.getString("Message.Attention"),
						MessagesEcran.getString("Tiers.Message.Annuler")))){
					//int rang = getGrille().getSelectionIndex();
					//int rang = modelTiers.getListeObjet().indexOf(swtTiers);
					//selectedTiers.setValue(swtOldTiers);
					//remetTousLesChampsApresAnnulationSWT(dbc);
					//modelTiers.getListeObjet().set(rang, swtOldTiers);
					
//					writableList =new WritableList(realm, modelTiers.getListeObjet(),classModel);
//					tableViewer.setInput(writableList);
//					tableViewer.refresh();
//					tableViewer.setSelection(new StructuredSelection(swtOldTiers),true);
					//					ibTaTable.annuler();
					dao.annuler(taTiers);
					changementDeSelection(); //pour réintialiser les autres onglets à partir des données de la bdd
					hideDecoratedFields();
					if(!annuleToutEnCours) {
						fireAnnuleTout(new AnnuleToutEvent(this));
					}
				}
				getModeEcran().setMode(EnumModeObjet.C_MO_CONSULTATION);//ejb
				initEtatBouton();	
				
				updateView(taTiers, false);

				break;
			case C_MO_CONSULTATION:
				//actionFermer.run();
				break;
			default:
				break;
			}
			//getFocusCourant().setInputVerifier(inputVerifier);
			//initEtatBouton();			
		} finally {
			VerrouInterface.setVerrouille(false);
		}
	}

	@Override
	protected void actImprimer() throws Exception{
//passage ejb
////				testJPQL();
//				
//		TaInfoEntrepriseDAO taInfoEntrepriseDAO = new TaInfoEntrepriseDAO(getEm());
//		TaInfoEntreprise taInfoEntreprise = taInfoEntrepriseDAO.findInstance();
//
////		int idTiers = taTiers.getIdTiers();
////		TaFactureDAO taFactureDAO = new TaFactureDAO(getEm());
//
//		//		List<Object>  objectEdition = taFactureDAO.rechercheAllFactureBaseArticle(idTiers,taInfoEntreprise.getDatedebInfoEntreprise(),
//		//									  taInfoEntreprise.getDatefinInfoEntreprise());
//		//		
//		//		for (int i = 0; i < objectEdition.size(); i++) {
//		//			TaTiers taTiers = (TaTiers) objectEdition.get(i);
//		//			Object[] objects = taTiers.getTaTelephones().toArray();
//		//			for (int j = 0; j < objects.length; j++) {
//		//				TaTelephone taTelephone = (TaTelephone)objects[j];
//		//				System.out.println(taTelephone.getNumeroTelephone()+" === "+taTelephone.getTaTTel().getCodeTTel());
//		//			}
//		//			
//		//		}
//
//
//
////		String nomChampIdTable =  dao.getChampIdTable();
//
//		FonctionGetInfosXmlAndProperties fonctionGetInfosXmlAndProperties = new FonctionGetInfosXmlAndProperties(mapperUIToModel);
//		fonctionGetInfosXmlAndProperties.cleanValueMapAttributeTable();
//
//		fonctionGetInfosXmlAndProperties.setValueMapAttributeTable(TaTiers.class.getSimpleName()+".head");
//		fonctionGetInfosXmlAndProperties.setValueMapAttributeTable(TaTiers.class.getSimpleName()+".detail");
//
//
//		ConstEdition constEdition = new ConstEdition(getEm()); 
//		
//		LinkedHashMap<String,AttributElementResport> mapAttributeTablHead = fonctionGetInfosXmlAndProperties.getMapAttributeTablHead(); 
//		LinkedHashMap<String,AttributElementResport> mapAttributeTablDetail = fonctionGetInfosXmlAndProperties.getMapAttributeTablDetail();
//
//		if(listeChamp==null) {
//			listeChamp = new String[]{"codeTiers","nomEntreprise","nomTiers","prenomTiers","codepostalAdresse","villeAdresse","numeroTelephone","adresseEmail","actifTiers"};
//		}
//
//		fonctionGetInfosXmlAndProperties.findInfosFileXml(TaTiers.class.getName(),TaTiersDTO.class.getName(),
//				listeChamp,"mapping");
//		fonctionGetInfosXmlAndProperties.getInfosObjetJPA(taTiers);
//		
//		/*********09/206/2010****************/
//		//constEdition.getOrderEcran(modelTiers.getListeEntity(),tableViewer,Const.C_CODE_TIERS);
//		
//		modelTiers.remplirListe(getEm());
//		Collection<TaTiers> collectionTaTiers = modelTiers.getListeEntity();
//		constEdition.getCollectionEntity().addAll(collectionTaTiers);
//		//Collection<TaTiers> collectionTaTiers = constEdition.getCollectionEntity();
//		/***********************************/
//
//		
//		//constEdition.setTaInfoEntreprise(taInfoEntreprise);
////		Impression impression = new Impression(constEdition,taTiers,collectionTaTiers,nomChampIdTable,taTiers.getIdTiers()
////				,getEm());
////		impression.setShellParent(vue.getShell());
//
//		String nomDossier = null;
//
//		int nombreLine = collectionTaTiers.size();
//
//		if(nombreLine==0){
//			MessageDialog.openWarning(vue.getShell(), ConstEdition.TITRE_MESSAGE_EDITION_VIDE,
//					ConstEdition.EDITION_VIDE);
//		}
//		else{
//			if(taInfoEntreprise.getIdInfoEntreprise()==0){
//				nomDossier = ConstEdition.INFOS_VIDE;
//			}
//			else{
//				nomDossier = taInfoEntreprise.getNomInfoEntreprise();	
//			}
//
//			constEdition.addValueList(tableViewer, nomClass);
//
//			/**
//			 * pathFileReport ==> le path de ficher de edition dynamique
//			 */
//			String folderEditionDynamique = Const.C_RCP_INSTANCE_LOCATION+ConstEdition.SEPARATOR+
//			Const.C_NOM_PROJET_TMP+ConstEdition.SEPARATOR+TaTiers.class.getSimpleName();
//			
//			constEdition.makeFolderEditions(folderEditionDynamique);
//			Path pathFileReport = new Path(folderEditionDynamique+ConstEdition.SEPARATOR+Const.C_NOM_VU_TIERS+".rptdesign");
//			//Path pathFileReport = new Path(folderEditionDynamique+"/"+ConstEdition.START_V+TaTiers.class.getSimpleName()+".rptdesign");
//			final String pathFileReportDynamic = pathFileReport.toPortableString();
//
//
//
//			MakeDynamiqueReport DynamiqueReport = new MakeDynamiqueReport(constEdition.getNameTableEcran(),
//					constEdition.getNameTableBDD(),pathFileReportDynamic,Const.C_NOM_VU_TIERS,
//					ConstEdition.PAGE_ORIENTATION_LANDSCAPE,nomDossier); 
//
//			/**************************************************************/
//			DynamiqueReport.setFonctionGetInfosXml(fonctionGetInfosXmlAndProperties);
//			DynamiqueReport.setNomObjet(TaTiers.class.getSimpleName());
//			/**************************************************************/
//
//			Map<String, AttributElementResport> attribuGridHeader = new LinkedHashMap<String, AttributElementResport>();
//
//			attribuGridHeader.put(ConstEditionTiers.TITLE_EDITION_TIERS, new AttributElementResport("",ConstEdition.TEXT_ALIGN_CENTER,
//					ConstEdition.FONT_SIZE_XX_LARGE,ConstEdition.FONT_WEIGHT_BOLD,"",
//					ConstEdition.COLUMN_DATA_TYPE_STRING,ConstEdition.UNITS_VIDE,""));
//
//			attribuGridHeader.put(ConstEditionTiers.SOUS_TITLE_EDITION_TIERS, new AttributElementResport("",ConstEdition.TEXT_ALIGN_CENTER,
//					ConstEdition.FONT_SIZE_X_LARGE,ConstEdition.FONT_WEIGHT_BOLD,"",
//					ConstEdition.COLUMN_DATA_TYPE_STRING,ConstEdition.UNITS_VIDE,ConstEdition.COLOR_GRAY));
//
//			//DynamiqueReport.buildDesignConfig();
//
//			ConstEdition.CONTENT_COMMENTS =ConstEditionTiers.COMMENTAIRE_EDITION_DEFAUT;
//
//			DynamiqueReport.setSimpleNameEntity(TaTiers.class.getSimpleName());
//			
//			DynamiqueReport.initializeBuildDesignReportConfig();
//			DynamiqueReport.makePageMater("0.5"/*top*/,"1"/*left*/,"0.5", "1"/*right*/, "100");
//			DynamiqueReport.makeReportHeaderGrid(3,5,100,ConstEdition.UNITS_PERCENTAGE,attribuGridHeader);
//			DynamiqueReport.biuldTableReport("100", ConstEdition.UNITS_PERCENTAGE, 
//					Const.C_NOM_VU_TIERS,1,1,2,ConstEdition.NOMBRE_LINE_EDITION_DYNAMIQUE, 
//					mapAttributeTablHead, mapAttributeTablDetail);
//			DynamiqueReport.savsAsDesignHandle();
//
//			Bundle bundleCourant = TiersPlugin.getDefault().getBundle();
//			String namePlugin = bundleCourant.getSymbolicName();
//
////			impression.imprimer(true,pathFileReportDynamic,ConstEdition.FICHE_FILE_REPORT_TIERS,namePlugin,
////					TaTiers.class.getSimpleName(),true);
//			
//			/** 01/03/2010 modifier les editions (zhaolin) **/
//			BaseImpressionEdition impressionEdition = new BaseImpressionEdition(constEdition,taTiers,
//					getEm(),collectionTaTiers,taTiers.getIdTiers());
//			
//			IPreferenceStore preferenceStore = TiersPlugin.getDefault().getPreferenceStore();
//			impressionEdition.impressionEdition(preferenceStore,TaTiers.class.getSimpleName()/*,true*/,
//					new File(pathFileReportDynamic),namePlugin,ConstEdition.FICHE_FILE_REPORT_TIERS,
//					false,true,false,false,false,false,ConstEditionTiers.PARAM_REPORT_ID_TIER);
//			
//		}

	}

	@Override
	protected void actPrecedent() throws Exception {
		ChangementDePageEvent evt = new ChangementDePageEvent(this,
				ChangementDePageEvent.PRECEDENT);
		fireChangementDePage(evt);
	}

	@Override
	protected void actSuivant() throws Exception {
		ChangementDePageEvent evt = new ChangementDePageEvent(this,
				ChangementDePageEvent.SUIVANT);
		fireChangementDePage(evt);
	}

	@Override
	protected boolean aideDisponible() {
		boolean result = false;
		switch ((SWTPaTiersController.this.getModeEcran().getMode())) {
		case C_MO_CONSULTATION:
//			if(getFocusCourantSWT().equals(vue.getGrille()))
//				result = true;
			break;
		case C_MO_EDITION:
		case C_MO_INSERTION:										
			//creation d'une recherche
			if(getFocusCourantSWT().equals(vue.getTfCODE_T_CIVILITE())
					|| getFocusCourantSWT().equals(vue.getTfEntite())
					|| getFocusCourantSWT().equals(vue.getTfCODE_T_TVA_DOC())
					|| getFocusCourantSWT().equals(vue.getTfCODE_T_TIERS())
					|| getFocusCourantSWT().equals(vue.getTfCODE_C_PAIEMENT())
					|| getFocusCourantSWT().equals(vue.getTfCODE_T_TARIF())
					|| getFocusCourantSWT().equals(vue.getTfCODE_T_PAIEMENT()))
				result = true;
			break;
		default:
			break;
		}
		return result;
	}

	@Override
	protected void actAide() throws Exception{
		actAide(null);
	}

	@Override
	protected void actAide(String message) throws Exception{
		if(aideDisponible()){
			//vue.getDisplay().syncExec(new Runnable(){
			//public void run() {
			try{
				setActiveAide(true);
				VerrouInterface.setVerrouille(true);
				vue.setCursor(Display.getCurrent().getSystemCursor(SWT.CURSOR_WAIT));
				ParamAfficheAideRechercheSWT paramAfficheAideRecherche = new ParamAfficheAideRechercheSWT();
				//#JPA
				//				paramAfficheAideRecherche.setDb(getThis().getIbTaTable().getFIBBase());
				paramAfficheAideRecherche.setMessage(message);
				//Creation de l'ecran d'aide principal
				Shell s = new Shell(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),LgrShellUtil.styleLgr);
				PaAideSWT paAide = new PaAideSWT(s,SWT.NONE);
				SWTPaAideControllerSWT paAideController /*= new SWTPaAideControllerSWT(paAide)*/;
				/***************************************************************/
				LgrPartListener.getLgrPartListener().setLgrActivePart(null);
				IEditorPart e = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new EditorInputAide(), EditorAide.ID);
				LgrPartListener.getLgrPartListener().setLgrActivePart(e);
				LgrPartListener.getLgrPartListener().getLgrNavigation().add(e);
				paAideController = new SWTPaAideControllerSWT(((EditorAide)e).getComposite());
				((EJBLgrEditorPart)e).setController(paAideController);
				((EJBLgrEditorPart)e).setPanel(((EditorAide)e).getComposite());
				/***************************************************************/
				EJBBaseControllerSWTStandard controllerEcranCreation = null;
				ParamAffiche parametreEcranCreation = null;
				IEditorPart editorCreation = null;
				String editorCreationId = null;
				IEditorInput editorInputCreation = null;
				Shell s2 = new Shell(s, LgrShellUtil.styleLgr);


				switch ((SWTPaTiersController.this.getModeEcran().getMode())) {
				case C_MO_CONSULTATION:
//					if(getFocusCourantSWT().equals(vue.getGrille())){
//						PaTiersSWT paTiersSWT = new PaTiersSWT(s2,SWT.NULL);
//						SWTPaTiersController swtPaTiersController = new SWTPaTiersController(paTiersSWT);
//
//						editorCreationId = TiersMultiPageEditor.ID_EDITOR;
//						editorInputCreation = new EditorInputTiers();
//
//						ParamAfficheTiers paramAfficheTiers = new ParamAfficheTiers();
//						paramAfficheAideRecherche.setJPQLQuery(dao.getJPQLQuery());
//						paramAfficheTiers.setModeEcran(EnumModeObjet.C_MO_INSERTION);
//						paramAfficheTiers.setEcranAppelant(paAideController);
//						controllerEcranCreation = swtPaTiersController;
//						parametreEcranCreation = paramAfficheTiers;
//						
//						paramAfficheAideRecherche.setTypeEntite(TaTiers.class);
//						paramAfficheAideRecherche.setChampsRecherche(Const.C_CODE_TIERS);
//						paramAfficheAideRecherche.setDebutRecherche(vue.getTfCODE_TIERS().getText());
//						paramAfficheAideRecherche.setControllerAppelant(getThis());
////						paramAfficheAideRecherche.setModel(swtPaTiersController.getModelTiers());
//						paramAfficheAideRecherche.setModel(new ModelGeneralObjet<TaTiersDTO, TaTiersDAO, TaTiers>(TaTiersDTO.class, getEm()));
//						paramAfficheAideRecherche.setTypeObjet(swtPaTiersController.getClassModel());
//						paramAfficheAideRecherche.setChampsIdentifiant(Const.C_ID_TIERS);
//					}
					break;
				case C_MO_EDITION:
				case C_MO_INSERTION:										
					//creation d'une recherche
					if(getFocusCourantSWT().equals(vue.getTfCODE_T_CIVILITE())){
						PaTypeCiviliteSWT paTCiviliteSWT = new PaTypeCiviliteSWT(s2,SWT.NULL);
						SWTPaTypeCiviliteController swtPaTCiviliteController = new SWTPaTypeCiviliteController(paTCiviliteSWT);

						editorCreationId = EditorTypeCivilite.ID;
						editorInputCreation = new EditorInputTypeCivilite();

						ParamAfficheTypeCivilite paramAfficheTCivilite = new ParamAfficheTypeCivilite();
						ITaTCiviliteServiceRemote dao = new EJBLookup<ITaTCiviliteServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_T_CIVILITE_SERVICE, ITaTCiviliteServiceRemote.class.getName());
						paramAfficheAideRecherche.setJPQLQuery(dao.getJPQLQuery());
						paramAfficheTCivilite.setModeEcran(EnumModeObjet.C_MO_INSERTION);
						paramAfficheTCivilite.setEcranAppelant(paAideController);
						
						/* 
						 * controllerEcranCreation ne sert plus a rien, pour l'editeur de creation, on creer un nouveau controller
						 */
						controllerEcranCreation = swtPaTCiviliteController;
						parametreEcranCreation = paramAfficheTCivilite;

						paramAfficheAideRecherche.setTypeEntite(TaTCivilite.class);
						paramAfficheAideRecherche.setChampsRecherche(Const.C_CODE_T_CIVILITE);
						paramAfficheAideRecherche.setDebutRecherche(vue.getTfCODE_T_CIVILITE().getText());
						//paramAfficheAideRecherche.setControllerAppelant(getThis());
						paramAfficheAideRecherche.setControllerAppelant(SWTPaTiersController.this);
						ModelGeneralObjetEJB<TaTCivilite,TaTCiviliteDTO> modelTypeCivilite = new ModelGeneralObjetEJB<TaTCivilite,TaTCiviliteDTO>(dao);
						paramAfficheAideRecherche.setModel(modelTypeCivilite);
						paramAfficheAideRecherche.setTypeObjet(swtPaTCiviliteController.getClassModel());

						//paramAfficheAideRecherche.setChampsIdentifiant(swtPaTCiviliteController.getDao().getChampIdEntite());
						paramAfficheAideRecherche.setChampsIdentifiant(Const.C_ID_DTO_GENERAL);
					}
					if(getFocusCourantSWT().equals(vue.getTfCODE_T_TVA_DOC())){
						PaTypeTtvaDocSWT paTypeTtvaDocSWT = new PaTypeTtvaDocSWT(s2,SWT.NULL);
						SWTPaTypeTvaDocController swtPaTypeTvaDocController = new SWTPaTypeTvaDocController(paTypeTtvaDocSWT);

						editorCreationId = EditorTypeTvaDoc.ID;
						editorInputCreation = new EditorInputTypeCivilite();

						ParamAfficheTypeTvaDoc paramAfficheTypeTvaDoc = new ParamAfficheTypeTvaDoc();
						ITaTTvaDocServiceRemote dao = new EJBLookup<ITaTTvaDocServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_T_TVA_DOC_SERVICE, ITaTTvaDocServiceRemote.class.getName());
						paramAfficheAideRecherche.setJPQLQuery(dao.getJPQLQuery());
						paramAfficheTypeTvaDoc.setModeEcran(EnumModeObjet.C_MO_INSERTION);
						paramAfficheTypeTvaDoc.setEcranAppelant(paAideController);
						/* 
						 * controllerEcranCreation ne sert plus a rien, pour l'editeur de creation, on creer un nouveau controller
						 */
						
//						paramAfficheAideRecherche.setForceAffichageAideRemplie(true);
//						paramAfficheAideRecherche.setAfficheDetail(false);
						
//						controllerEcranCreation = swtPaTypeTvaDocController; //ejb
						parametreEcranCreation = paramAfficheTypeTvaDoc;

						paramAfficheAideRecherche.setTypeEntite(TaTTvaDoc.class);
						paramAfficheAideRecherche.setChampsRecherche(Const.C_CODE_T_TVA_DOC);
						paramAfficheAideRecherche.setDebutRecherche(vue.getTfCODE_T_TVA_DOC().getText());
//						paramAfficheAideRecherche.setControllerAppelant(SWTPaTiersController.this);
						ModelGeneralObjetEJB<TaTTvaDoc,TaTTvaDocDTO> modelTypeTvaDoc = new ModelGeneralObjetEJB<TaTTvaDoc,TaTTvaDocDTO>(dao);
						paramAfficheAideRecherche.setModel(modelTypeTvaDoc);
						paramAfficheAideRecherche.setTypeObjet(swtPaTypeTvaDocController.getClassModel());

						paramAfficheAideRecherche.setChampsIdentifiant(Const.C_ID_DTO_GENERAL);
					}
					
					if(getFocusCourantSWT().equals(vue.getTfEntite())){
						PaTypeEntiteSWT paTEntiteSWT = new PaTypeEntiteSWT(s2,SWT.NULL);
						SWTPaTypeEntiteController swtPaTEntiteController = new SWTPaTypeEntiteController(paTEntiteSWT);

						editorCreationId = EditorTypeEntite.ID;
						editorInputCreation = new EditorInputTypeEntite();

						ParamAfficheTypeEntite paramAfficheTEntite = new ParamAfficheTypeEntite();
						ITaTEntiteServiceRemote dao = new EJBLookup<ITaTEntiteServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_T_ENTITE_SERVICE, ITaTEntiteServiceRemote.class.getName());
						paramAfficheAideRecherche.setJPQLQuery(dao.getJPQLQuery());
						paramAfficheTEntite.setModeEcran(EnumModeObjet.C_MO_INSERTION);
						paramAfficheTEntite.setEcranAppelant(paAideController);
						controllerEcranCreation = swtPaTEntiteController;
						parametreEcranCreation = paramAfficheTEntite;

						paramAfficheAideRecherche.setTypeEntite(TaTEntite.class);
						paramAfficheAideRecherche.setChampsRecherche(Const.C_CODE_T_ENTITE);
						paramAfficheAideRecherche.setDebutRecherche(vue.getTfEntite().getText());
						paramAfficheAideRecherche.setControllerAppelant(getThis());
						ModelGeneralObjetEJB<TaTEntite,TaTEntiteDTO> modelTypeEntite = new ModelGeneralObjetEJB<TaTEntite,TaTEntiteDTO>(dao);
						paramAfficheAideRecherche.setModel(modelTypeEntite);
						paramAfficheAideRecherche.setTypeObjet(swtPaTEntiteController.getClassModel());

						//paramAfficheAideRecherche.setChampsIdentifiant(swtPaTEntiteController.getDao().getChampIdTable());
						paramAfficheAideRecherche.setChampsIdentifiant(Const.C_ID_DTO_GENERAL);
					}
					if(getFocusCourantSWT().equals(vue.getTfCODE_T_TIERS())){
						PaTypeTiersSWT paTTiersSWT = new PaTypeTiersSWT(s2,SWT.NULL);
						SWTPaTypeTiersController swtPaTTiersController = new SWTPaTypeTiersController(paTTiersSWT);

						editorCreationId = EditorTypeTiers.ID;
						editorInputCreation = new EditorInputTypeTiers();

						ParamAfficheTypeTiers paramAfficheTTiers = new ParamAfficheTypeTiers();
						ITaTTiersServiceRemote dao = new EJBLookup<ITaTTiersServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_T_TIERS_SERVICE, ITaTTiersServiceRemote.class.getName());
						paramAfficheAideRecherche.setJPQLQuery(dao.getJPQLQuery());
						paramAfficheTTiers.setModeEcran(EnumModeObjet.C_MO_INSERTION);
						paramAfficheTTiers.setEcranAppelant(paAideController);
						controllerEcranCreation = swtPaTTiersController;
						parametreEcranCreation = paramAfficheTTiers;

						paramAfficheAideRecherche.setTypeEntite(TaTTiers.class);
						paramAfficheAideRecherche.setChampsRecherche(Const.C_CODE_T_TIERS);
						paramAfficheAideRecherche.setDebutRecherche(vue.getTfCODE_T_TIERS().getText());
						paramAfficheAideRecherche.setControllerAppelant(getThis());
						ModelGeneralObjetEJB<TaTTiers,TaTTiersDTO> modelTypeTiers = new ModelGeneralObjetEJB<TaTTiers,TaTTiersDTO>(dao);
						paramAfficheAideRecherche.setModel(modelTypeTiers);

						paramAfficheAideRecherche.setTypeObjet(swtPaTTiersController.getClassModel());

						//paramAfficheAideRecherche.setChampsIdentifiant(swtPaTTiersController.getDao().getChampIdTable());
						paramAfficheAideRecherche.setChampsIdentifiant(Const.C_ID_DTO_GENERAL);
					}
					if(getFocusCourantSWT().equals(vue.getTfCODE_C_PAIEMENT())){
						PaConditionPaiementSWT paConditionPaiementSWT = new PaConditionPaiementSWT(s2,SWT.NULL);
						SWTPaConditionPaiementController swtPaConditionPaiementController = new SWTPaConditionPaiementController(paConditionPaiementSWT);

						editorCreationId = EditorConditionPaiement.ID;
						editorInputCreation = new EditorInputConditionPaiement();

						ParamAfficheConditionPaiement paramAfficheConditionPaiement = new ParamAfficheConditionPaiement();
						ITaTCPaiementServiceRemote dao = new EJBLookup<ITaTCPaiementServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_C_PAIEMENT_SERVICE, ITaTCPaiementServiceRemote.class.getName());
						paramAfficheAideRecherche.setJPQLQuery(dao.getJPQLQuery());
						paramAfficheConditionPaiement.setModeEcran(EnumModeObjet.C_MO_INSERTION);
						paramAfficheConditionPaiement.setEcranAppelant(paAideController);
						//controllerEcranCreation = swtPaConditionPaiementController; //ejb
						parametreEcranCreation = paramAfficheConditionPaiement;
						
//						paramAfficheAideRecherche.setAfficheDetail(false);
						
						paramAfficheAideRecherche.setTypeEntite(TaCPaiement.class);
						paramAfficheAideRecherche.setChampsRecherche(Const.C_CODE_C_PAIEMENT);
						paramAfficheAideRecherche.setDebutRecherche(vue.getTfCODE_C_PAIEMENT().getText());
//						paramAfficheAideRecherche.setControllerAppelant(getThis());
						//paramAfficheAideRecherche.setModel(new ModelGeneralObjet<SWTCPaiement,TaCPaiementDAO,TaCPaiement>(SWTCPaiement.class,dao.getEntityManager()));
						ModelGeneralObjetEJB<TaCPaiement,TaCPaiementDTO> modelCodePaiement = new ModelGeneralObjetEJB<TaCPaiement,TaCPaiementDTO>(dao);
						//paramAfficheAideRecherche.setModel(new ModelGeneralObjet<SWTCPaiement,TaCPaiementDAO,TaCPaiement>(new TaCPaiementDAO().rechercheParType(TaTCPaiement.C_CODE_TYPE_TIERS),SWTCPaiement.class,dao.getEntityManager()));
						paramAfficheAideRecherche.setModel(modelCodePaiement);

						paramAfficheAideRecherche.setTypeObjet(swtPaConditionPaiementController.getClassModel());

						paramAfficheAideRecherche.setChampsIdentifiant(Const.C_ID_DTO_GENERAL);
					}
					if(getFocusCourantSWT().equals(vue.getTfCODE_T_TARIF())){
						PaTypeTarifSWT paTypeTarifSWT = new PaTypeTarifSWT(s2,SWT.NULL);
						SWTPaTypeTarifController swtPaTypeTarifController = new SWTPaTypeTarifController(paTypeTarifSWT);

						editorCreationId = EditorTypeTarif.ID;
						editorInputCreation = new EditorInputTypeTarif();

						ParamAfficheTypeTarif paramAfficheTypeTarif = new ParamAfficheTypeTarif();
						ITaTTarifServiceRemote dao = new EJBLookup<ITaTTarifServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_T_TARIF_SERVICE, ITaTTarifServiceRemote.class.getName());
						paramAfficheAideRecherche.setJPQLQuery(dao.getJPQLQuery());
						paramAfficheTypeTarif.setModeEcran(EnumModeObjet.C_MO_INSERTION);
						paramAfficheTypeTarif.setEcranAppelant(paAideController);
						controllerEcranCreation = swtPaTypeTarifController;
						parametreEcranCreation = paramAfficheTypeTarif;
						
						paramAfficheAideRecherche.setAfficheDetail(false);
						
						paramAfficheAideRecherche.setTypeEntite(TaTTarif.class);
						paramAfficheAideRecherche.setChampsRecherche(Const.C_CODE_T_TARIF);
						paramAfficheAideRecherche.setDebutRecherche(vue.getTfCODE_T_TARIF().getText());
						paramAfficheAideRecherche.setControllerAppelant(getThis());
						//paramAfficheAideRecherche.setModel(new ModelGeneralObjet<SWTCPaiement,TaCPaiementDAO,TaCPaiement>(SWTCPaiement.class,dao.getEntityManager()));
						ModelGeneralObjetEJB<TaTTarif,TaTTarifDTO> modelTypeTarif = new ModelGeneralObjetEJB<TaTTarif,TaTTarifDTO>(dao);
						paramAfficheAideRecherche.setModel(modelTypeTarif);

						paramAfficheAideRecherche.setTypeObjet(swtPaTypeTarifController.getClassModel());

						//paramAfficheAideRecherche.setChampsIdentifiant(swtPaTypeTarifController.getDao().getChampIdTable());
						paramAfficheAideRecherche.setChampsIdentifiant(Const.C_ID_DTO_GENERAL);
					}
					if (getFocusCourantSWT().equals(vue.getTfCODE_T_PAIEMENT())) {
						PaTypePaiementSWT paTypePaiementSWT = new PaTypePaiementSWT(s2, SWT.NULL);
						SWTPaTypePaiementController swtPaTypePaiementController = new SWTPaTypePaiementController(
								paTypePaiementSWT);
						editorCreationId = EditorTypePaiement.ID;
						editorInputCreation = new EditorInputTypePaiement();
						ParamAfficheTPaiement paramAfficheTPaiement = new ParamAfficheTPaiement();
						ITaCPaiementServiceRemote dao = new EJBLookup<ITaCPaiementServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_C_PAIEMENT_SERVICE, ITaCPaiementServiceRemote.class.getName());
						paramAfficheAideRecherche.setJPQLQuery(dao.getJPQLQuery());
						paramAfficheTPaiement.setModeEcran(EnumModeObjet.C_MO_INSERTION);
						paramAfficheTPaiement.setEcranAppelant(paAideController);
						//controllerEcranCreation = swtPaTypePaiementController; //ejb
						parametreEcranCreation = paramAfficheTPaiement;
						paramAfficheAideRecherche.setTypeEntite(TaTPaiement.class);
						paramAfficheAideRecherche.setChampsRecherche(Const.C_CODE_T_PAIEMENT);
						paramAfficheAideRecherche.setDebutRecherche("");
						paramAfficheAideRecherche.setControllerAppelant(getThis());
						ModelGeneralObjetEJB<TaTPaiement,TaTPaiementDTO> modelTypPaiement = new ModelGeneralObjetEJB<TaTPaiement,TaTPaiementDTO>(dao);
						paramAfficheAideRecherche.setModel(modelTypPaiement);
						paramAfficheAideRecherche.setTypeObjet(swtPaTypePaiementController
										.getClassModel());
						paramAfficheAideRecherche.setChampsIdentifiant(Const.C_ID_DTO_GENERAL);
					}
				default:
					break;
				}

				//if (paramAfficheAideRecherche.getJPQLQuery()!=null){ //ejb commentaire


					PaAideRechercheSWT paAideRecherche1 = new PaAideRechercheSWT(s,SWT.NULL);	
					SWTPaAideRechercheControllerSWT paAideRechercheController1 = new SWTPaAideRechercheControllerSWT(paAideRecherche1);

					//Parametrage de la recherche
					paramAfficheAideRecherche.setFocusSWT(((PaAideRechercheSWT)paAideRechercheController1.getVue()).getTfChoix());
					paramAfficheAideRecherche.setRefCreationSWT(controllerEcranCreation);
					paramAfficheAideRecherche.setEditorCreation(editorCreation);
					paramAfficheAideRecherche.setEditorCreationId(editorCreationId);
					paramAfficheAideRecherche.setEditorInputCreation(editorInputCreation);
					paramAfficheAideRecherche.setParamEcranCreation(parametreEcranCreation);
					paramAfficheAideRecherche.setShellCreation(s2);
					paAideRechercheController1.configPanel(paramAfficheAideRecherche);
					//paramAfficheAideRecherche.setFocusDefaut(paAideRechercheController1.getVue().getTfChoix());

					//Ajout d'une recherche
					paAideController.addRecherche(paAideRechercheController1, paramAfficheAideRecherche.getTitreRecherche());

					//Parametrage de l'ecran d'aide principal
					ParamAfficheAideSWT paramAfficheAideSWT = new ParamAfficheAideSWT();
					ParamAfficheAide paramAfficheAide = new ParamAfficheAide();

					//enregistrement pour le retour de l'ecran d'aide
					paAideController.addRetourEcranListener(getThis());
					Control focus = vue.getShell().getDisplay().getFocusControl();
					//affichage de l'ecran d'aide principal (+ ses recherches)

					dbc.getValidationStatusMap().removeMapChangeListener(changeListener);
					//LgrShellUtil.afficheAideSWT(paramAfficheAide, null, paAide,paAideController, s);
					/*****************************************************************/
					paAideController.configPanel(paramAfficheAide);
					/*****************************************************************/	
					dbc.getValidationStatusMap().addMapChangeListener(changeListener);

				//}

			}finally{
				VerrouInterface.setVerrouille(false);
				vue.setCursor(Display.getCurrent().getSystemCursor(SWT.CURSOR_ARROW));
			}
		}
	}

	/**
	 * Creation d'un objet "TaCompl" pour l'objet "TaTiers" gerer par cet ecran
	 * dans le cas ou la propriete taCompl de ce dernier est nulle.
	 */
//	private void initComplTiers(Object value) {
//		if(taTiers.getTaCompl()==null&& (value!=null&&!value.equals(""))) {
//			//initialisation du complement
//			TaCompl p = new TaCompl();
//			p.getTaTierses().add(taTiers);
//			taTiers.setTaCompl(p);
//		}else if(taTiers.getTaCompl()!=null && (value==null||value.equals(""))) {
//			if(taTiers.getTaCompl().getSiretCompl() == null && taTiers.getTaCompl().getAccise() == null
//					&& taTiers.getTaCompl().getTvaIComCompl() == null) {
//				taTiers.setTaCompl(null);
//			}
//			
//		}
//	}
//
//	private void initCodeTTiers(Object value) {
//		if(taTiers.getTaTTiers()==null&& (value!=null&&!value.equals(""))) {
//			//initialisation du type tiers
//			TaTTiers p = new TaTTiers();
//			//p.getTaTierses().add(taTiers); //ejb
//			taTiers.setTaTTiers(p);
//		}else if(taTiers.getTaTTiers()!=null&& (value==null||value.equals(""))) {
//			taTiers.setTaTTiers(null);
//		}
//	}
//
//	private void initEntrepriseTiers(Object value) {
//		if(taTiers.getTaEntreprise()==null&& (value!=null&&!value.equals(""))) {
//			//initialisation de l'entreprise
//			TaEntreprise p = new TaEntreprise();
//			p.getTaTierses().add(taTiers);
//			taTiers.setTaEntreprise(p);
//		}else if(taTiers.getTaEntreprise()!=null&& (value==null||value.equals(""))) {
//			taTiers.setTaEntreprise(null);
//		}
//	}
//
//	private void initTelephoneTiers(Object value) {
//		if(taTiers.getTaTelephone()==null&& (value!=null&&!value.equals(""))) {
//			//initialisation du telephone
//			TaTelephone p = new TaTelephone();
//			//valeur par défaut pour les booleens qui ne peuvent pas être initialisé pendant la validation de l'ihm car non affiché 
//			if(TiersPlugin.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.CORRESPONDANCE_TELEPHONE_ADMINISTRATIF_DEFAUT)) {
//				p.setCommAdministratifTelephone(1);
//			} else {
//				p.setCommAdministratifTelephone(0);
//			}
//			
//			if(TiersPlugin.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.CORRESPONDANCE_TELEPHONE_COMMERCIAL_DEFAUT)) {
//				p.setCommCommercialTelephone(1);
//			} else {
//				p.setCommCommercialTelephone(0);
//			}
//			p.setTaTiers(taTiers);
//			taTiers.setTaTelephone(p);
//			taTiers.addTelephone(p);
//		}else if(taTiers.getTaTelephone()!=null&& (value==null||value.equals(""))) {
//			taTiers.setTaTelephone(null);
//		}
//	}
//
//	private void initAdresseTiers(Object value) {
//		if(taTiers.getTaAdresse()==null && adresseEstRempli()) {
//			//initialisation de l'adresse
//			TaAdresse p = new TaAdresse();
//			if(TiersPlugin.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.CORRESPONDANCE_ADRESSE_ADMINISTRATIF_DEFAUT)) {
//				p.setCommAdministratifAdresse(1);
//			} else {
//				p.setCommAdministratifAdresse(0);
//			}
//			
//			if(TiersPlugin.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.CORRESPONDANCE_ADRESSE_COMMERCIAL_DEFAUT)) {
//				p.setCommCommercialAdresse(1);
//			} else {
//				p.setCommCommercialAdresse(0);
//			}
//			p.setTaTiers(taTiers);
//			taTiers.setTaAdresse(p);
//			taTiers.addAdresse(p);
//		}else if(taTiers.getTaAdresse()!=null && !adresseEstRempli()) {
//			taTiers.setTaAdresse(null);
//		}
//	}
//
//	private void initCommentaireTiers(Object value) {
//		if(taTiers.getTaCommentaire()==null&& (value!=null&&!value.equals(""))) {
//			//initialisation du commentaire
//			TaCommentaire p = new TaCommentaire();
//			p.getTaTierses().add(taTiers);
//			taTiers.setTaCommentaire(p);
//		}else if(taTiers.getTaCommentaire()!=null&& (value==null||value.equals(""))) {
//			taTiers.setTaCommentaire(null);
//		}
//	}
//
//	private void initCodeCompta(){
//		if(swtTiers==null){
//			swtTiers=new TaTiersDTO();
//			swtTiers = TaTiersDTO.copy(swtTiers);
//		}
////		if(swtTiers!=null){
////			if(swtTiers.getCodeTiers()!=null && !swtTiers.getCodeTiers().isEmpty())
////				if(swtTiers.getCodeCompta()==null ||
////						swtTiers.getCodeCompta().isEmpty())	{
////					swtTiers.setCodeCompta(swtTiers.getCodeTiers());
////					validateUIField(Const.C_CODE_COMPTA,swtTiers.getCodeCompta());
////				}
////		}
//	}
//
//	private void initEmailTiers(Object value) {
//		if(taTiers.getTaEmail()==null&& (value!=null&&!value.equals(""))) {
//			//initialisation de l'email
//			TaEmail p = new TaEmail();
//			//valeur par défaut pour les booleens qui ne peuvent pas être initialisé pendant la validation de l'ihm car non affiché 
//			if(TiersPlugin.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.CORRESPONDANCE_EMAIL_ADMINISTRATIF_DEFAUT)) {
//				p.setCommAdministratifEmail(1);
//			} else {
//				p.setCommAdministratifEmail(0);
//			}
//			
//			if(TiersPlugin.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.CORRESPONDANCE_EMAIL_COMMERCIAL_DEFAUT)) {
//				p.setCommCommercialEmail(1);
//			} else {
//				p.setCommCommercialEmail(0);
//			}
//			p.setTaTiers(taTiers);
//			taTiers.setTaEmail(p);
//			taTiers.addEmail(p);
//		}else if(taTiers.getTaEmail()!=null&& (value==null||value.equals(""))) {
//			taTiers.setTaEmail(null);
//		}
//	}
//
//	private void initWebTiers(Object value) {
//		if(taTiers.getTaWeb()==null&& (value!=null&&!value.equals(""))) {
//			//initialisation de l'adresse web
//			TaWeb p = new TaWeb();
//			p.setTaTiers(taTiers);
//			taTiers.setTaWeb(p);
//			taTiers.addWeb(p);
//		}else if(taTiers.getTaWeb()!=null&& (value==null||value.equals(""))) {
//			taTiers.setTaWeb(null);
//		}
//	}

	public IStatus validateUI() throws Exception {
		if ((getModeEcran().getMode().compareTo(EnumModeObjet.C_MO_INSERTION) == 0)
				|| (getModeEcran().getMode().compareTo(EnumModeObjet.C_MO_EDITION) == 0)) {
			ctrlTousLesChampsAvantEnregistrementSWT();
			mapperUIToModel.map(swtTiers,taTiers);
		}
		return null;
	}

	public IStatus validateUIField(String nomChamp,Object value) {
		
		boolean changement=false;
		try {
			//IStatus s = null;

			IStatus resultat = new Status(IStatus.OK,TiersPlugin.PLUGIN_ID,"validateUIField champ : "+nomChamp!=null?nomChamp:"null"+" valeur : "+value!=null?value.toString():"valeur nulle"+" validation OK");
			
			int VALIDATION_CLIENT = 1;
			int VALIDATION_SERVEUR = 2;
			int VALIDATION_CLIENT_ET_SERVEUR = 3;
			
			//int TYPE_VALIDATION = VALIDATION_CLIENT;
			//int TYPE_VALIDATION = VALIDATION_SERVEUR;
			int TYPE_VALIDATION = VALIDATION_CLIENT_ET_SERVEUR;
			
			AbstractApplicationDAOClient<TaTiersDTO> a = new AbstractApplicationDAOClient<TaTiersDTO>();
			
				
				if(nomChamp.equals(Const.C_CODE_T_TIERS)) {
//					TaTTiersDAO dao = new TaTTiersDAO(getEm());
//					
//					dao.setModeObjet(getDao().getModeObjet());
//					TaTTiers f = new TaTTiers();
//					PropertyUtils.setSimpleProperty(f, nomChamp, value);
//					s = dao.validate(f,nomChamp,validationContext);
//	
//					if(s.getSeverity()!=IStatus.ERROR ){
//						f = dao.findByCode((String)value);
//						taTiers.setTaTTiers(f);
//					}
//					dao = null;
					
					ITaTTiersServiceRemote dao = new EJBLookup<ITaTTiersServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_T_TIERS_SERVICE, ITaTTiersServiceRemote.class.getName());
					TaTTiersDTO dto = new TaTTiersDTO();
					try {
						if(TYPE_VALIDATION==VALIDATION_CLIENT || TYPE_VALIDATION==VALIDATION_CLIENT_ET_SERVEUR) {
							AbstractApplicationDAOClient<TaTTiersDTO> validationClient = new AbstractApplicationDAOClient<TaTTiersDTO>();
							PropertyUtils.setSimpleProperty(dto, nomChamp, value);
							validationClient.validate(dto,nomChamp,ITaTiersServiceRemote.validationContext);
						}
						if(resultat==null && (TYPE_VALIDATION==VALIDATION_SERVEUR || TYPE_VALIDATION==VALIDATION_CLIENT_ET_SERVEUR)) {
							PropertyUtils.setSimpleProperty(dto, nomChamp, value);
							dao.validateDTOProperty(dto, nomChamp,ITaTiersServiceRemote.validationContext);
						}
					} catch(Exception e) {
						resultat = new Status(IStatus.ERROR,TiersPlugin.PLUGIN_ID, e.getMessage(), e);
					}
					if(resultat!=null && resultat.getSeverity()!=IStatus.ERROR ){
						TaTTiers entity = new TaTTiers();
						entity = dao.findByCode((String)value);
						taTiers.setTaTTiers(entity);
					}
					dao = null;
				} else if(nomChamp.equals(Const.C_CODE_T_CIVILITE)) {
//					TaTCiviliteDAO dao = new TaTCiviliteDAO(getEm());
//	
//					dao.setModeObjet(getDao().getModeObjet());
//					TaTCivilite f = new TaTCivilite();
//					PropertyUtils.setSimpleProperty(f, nomChamp, value);
//					s = dao.validate(f,nomChamp,validationContext);
//	
//					if(s.getSeverity()!=IStatus.ERROR ){
//						f = dao.findByCode((String)value);
//						taTiers.setTaTCivilite(f);
//					}
//					dao = null;
					
					ITaTCiviliteServiceRemote dao = new EJBLookup<ITaTCiviliteServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_T_CIVILITE_SERVICE, ITaTCiviliteServiceRemote.class.getName());
					TaTCiviliteDTO dto = new TaTCiviliteDTO();
					try {
						if(TYPE_VALIDATION==VALIDATION_CLIENT || TYPE_VALIDATION==VALIDATION_CLIENT_ET_SERVEUR) {
							AbstractApplicationDAOClient<TaTCiviliteDTO> validationClient = new AbstractApplicationDAOClient<TaTCiviliteDTO>();
							PropertyUtils.setSimpleProperty(dto, nomChamp, value);
							validationClient.validate(dto,nomChamp,ITaTiersServiceRemote.validationContext);
						}
						if(resultat==null && (TYPE_VALIDATION==VALIDATION_SERVEUR || TYPE_VALIDATION==VALIDATION_CLIENT_ET_SERVEUR)) {
							PropertyUtils.setSimpleProperty(dto, nomChamp, value);
							dao.validateDTOProperty(dto, nomChamp,ITaTiersServiceRemote.validationContext);
						}
					} catch(Exception e) {
						resultat = new Status(IStatus.ERROR,TiersPlugin.PLUGIN_ID, e.getMessage(), e);
					}
					if(resultat!=null && resultat.getSeverity()!=IStatus.ERROR ){
						TaTCivilite entity = new TaTCivilite();
						entity = dao.findByCode((String)value);
						taTiers.setTaTCivilite(entity);
					}
					dao = null;
				}else if(nomChamp.equals(Const.C_CODE_T_TVA_DOC)) {
//					TaTTvaDocDAO dao = new TaTTvaDocDAO(getEm());
//	
//					dao.setModeObjet(getDao().getModeObjet());
//					TaTTvaDoc f = new TaTTvaDoc();
//					PropertyUtils.setSimpleProperty(f, nomChamp, value);
//					s = dao.validate(f,nomChamp,validationContext);
//	
//					if(s.getSeverity()!=IStatus.ERROR ){
//						f = dao.findByCode((String)value);
//						taTiers.setTaTTvaDoc(f);
//					}
//					dao = null;
					
					ITaTTvaDocServiceRemote dao = new EJBLookup<ITaTTvaDocServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_T_TVA_DOC_SERVICE, ITaTTvaDocServiceRemote.class.getName());
					TaTTvaDocDTO dto = new TaTTvaDocDTO();
					try {
						if(TYPE_VALIDATION==VALIDATION_CLIENT || TYPE_VALIDATION==VALIDATION_CLIENT_ET_SERVEUR) {
							AbstractApplicationDAOClient<TaTTvaDocDTO> validationClient = new AbstractApplicationDAOClient<TaTTvaDocDTO>();
							PropertyUtils.setSimpleProperty(dto, nomChamp, value);
							validationClient.validate(dto,nomChamp,ITaTiersServiceRemote.validationContext);
						}
						if(resultat==null && (TYPE_VALIDATION==VALIDATION_SERVEUR || TYPE_VALIDATION==VALIDATION_CLIENT_ET_SERVEUR)) {
							PropertyUtils.setSimpleProperty(dto, nomChamp, value);
							dao.validateDTOProperty(dto, nomChamp,ITaTiersServiceRemote.validationContext);
						}
					} catch(Exception e) {
						resultat = new Status(IStatus.ERROR,TiersPlugin.PLUGIN_ID, e.getMessage(), e);
					}
					if(resultat!=null && resultat.getSeverity()!=IStatus.ERROR ){
						TaTTvaDoc entity = new TaTTvaDoc();
						entity = dao.findByCode((String)value);
						taTiers.setTaTTvaDoc(entity);
					}
					dao = null;
				}
				else if(nomChamp.equals(Const.C_TVA_I_COM_COMPL)|| nomChamp.equals(Const.C_ACCISE)
						|| nomChamp.equals(Const.C_SIRET_COMPL)) {
//					TaComplDAO dao = new TaComplDAO(getEm());
//	
//					dao.setModeObjet(getDao().getModeObjet());
//					TaCompl f = new TaCompl();
//					PropertyUtils.setSimpleProperty(f, nomChamp, value);
//					s = dao.validate(f,nomChamp,validationContext);
//	
//					if(s.getSeverity()!=IStatus.ERROR ){
//						initComplTiers(value);
//	
//						//if(value!=null && !value.equals("")) {
//							if(value!=null && taTiers.getTaCompl()!=null) {
//							PropertyUtils.setSimpleProperty(taTiers.getTaCompl(), nomChamp, value);
//						}
//					}
//					dao = null;
					ITaComplServiceRemote dao = new EJBLookup<ITaComplServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_COMPL_SERVICE, ITaComplServiceRemote.class.getName());
					TaComplDTO dto = new TaComplDTO();
					try {
						if(TYPE_VALIDATION==VALIDATION_CLIENT || TYPE_VALIDATION==VALIDATION_CLIENT_ET_SERVEUR) {
							AbstractApplicationDAOClient<TaComplDTO> validationClient = new AbstractApplicationDAOClient<TaComplDTO>();
							PropertyUtils.setSimpleProperty(dto, nomChamp, value);
							validationClient.validate(dto,nomChamp,ITaTiersServiceRemote.validationContext);
						}
						if(resultat==null && (TYPE_VALIDATION==VALIDATION_SERVEUR || TYPE_VALIDATION==VALIDATION_CLIENT_ET_SERVEUR)) {
							PropertyUtils.setSimpleProperty(dto, nomChamp, value);
							dao.validateDTOProperty(dto, nomChamp,ITaTiersServiceRemote.validationContext);
						}
					} catch(Exception e) {
						resultat = new Status(IStatus.ERROR,TiersPlugin.PLUGIN_ID, e.getMessage(), e);
					}
					if(resultat!=null && resultat.getSeverity()!=IStatus.ERROR ){
						taTiers.initComplTiers(value);
//						if(value!=null && !value.equals("")) {
						
						if(value!=null && taTiers.getTaCompl()!=null) {
						PropertyUtils.setSimpleProperty(taTiers.getTaCompl(), nomChamp, value);
					}

					}
					dao = null;
					
				}
				else if(nomChamp.equals(Const.C_CODE_T_ENTITE)) {
//					TaTEntiteDAO dao = new TaTEntiteDAO(getEm());
//	
//					dao.setModeObjet(getDao().getModeObjet());
//					TaTEntite f = new TaTEntite();
//					PropertyUtils.setSimpleProperty(f, nomChamp, value);
//					s = dao.validate(f,nomChamp,validationContext);
//	
//					if(s.getSeverity()!=IStatus.ERROR ){
//						f = dao.findByCode((String)value);
//						taTiers.setTaTEntite(f);
//					}
//					dao = null;
					ITaTEntiteServiceRemote dao = new EJBLookup<ITaTEntiteServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_T_ENTITE_SERVICE, ITaTEntiteServiceRemote.class.getName());
					TaTEntiteDTO dto = new TaTEntiteDTO();
					try {
						if(TYPE_VALIDATION==VALIDATION_CLIENT || TYPE_VALIDATION==VALIDATION_CLIENT_ET_SERVEUR) {
							AbstractApplicationDAOClient<TaTEntiteDTO> validationClient = new AbstractApplicationDAOClient<TaTEntiteDTO>();
							PropertyUtils.setSimpleProperty(dto, nomChamp, value);
							validationClient.validate(dto,nomChamp,ITaTiersServiceRemote.validationContext);
						}
						if(resultat==null && (TYPE_VALIDATION==VALIDATION_SERVEUR || TYPE_VALIDATION==VALIDATION_CLIENT_ET_SERVEUR)) {
							PropertyUtils.setSimpleProperty(dto, nomChamp, value);
							dao.validateDTOProperty(dto, nomChamp,ITaTiersServiceRemote.validationContext);
						}
					} catch(Exception e) {
						resultat = new Status(IStatus.ERROR,TiersPlugin.PLUGIN_ID, e.getMessage(), e);
					}
					if(resultat!=null && resultat.getSeverity()!=IStatus.ERROR ){
						TaTEntite entity = new TaTEntite();
						entity = dao.findByCode((String)value);
						taTiers.setTaTEntite(entity);
					}
					dao = null;					
				}else if(nomChamp.equals(Const.C_CODE_C_PAIEMENT)) {
//					TaCPaiementDAO dao = new TaCPaiementDAO(getEm());
//	
//					dao.setModeObjet(getDao().getModeObjet());
//					TaCPaiement f = new TaCPaiement();
//					PropertyUtils.setSimpleProperty(f, nomChamp, value);
//					s = dao.validate(f,nomChamp,validationContext);
//	
//					if(s.getSeverity()!=IStatus.ERROR ){
//						f = dao.findByCode((String)value);
//						taTiers.setTaCPaiement(f);
//					}
//					dao = null;
					ITaCPaiementServiceRemote dao = new EJBLookup<ITaCPaiementServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_C_PAIEMENT_SERVICE, ITaCPaiementServiceRemote.class.getName());
					TaCPaiementDTO dto = new TaCPaiementDTO();
					try {
						if(TYPE_VALIDATION==VALIDATION_CLIENT || TYPE_VALIDATION==VALIDATION_CLIENT_ET_SERVEUR) {
							AbstractApplicationDAOClient<TaCPaiementDTO> validationClient = new AbstractApplicationDAOClient<TaCPaiementDTO>();
							PropertyUtils.setSimpleProperty(dto, nomChamp, value);
							validationClient.validate(dto,nomChamp,ITaTiersServiceRemote.validationContext);
						}
						if(resultat==null && (TYPE_VALIDATION==VALIDATION_SERVEUR || TYPE_VALIDATION==VALIDATION_CLIENT_ET_SERVEUR)) {
							PropertyUtils.setSimpleProperty(dto, nomChamp, value);
							dao.validateDTOProperty(dto, nomChamp,ITaTiersServiceRemote.validationContext);
						}
					} catch(Exception e) {
						resultat = new Status(IStatus.ERROR,TiersPlugin.PLUGIN_ID, e.getMessage(), e);
					}
					if(resultat!=null && resultat.getSeverity()!=IStatus.ERROR ){
						TaCPaiement entity = new TaCPaiement();
						entity = dao.findByCode((String)value);
						taTiers.setTaCPaiement(entity);
					}
					dao = null;						
				}else if(nomChamp.equals(Const.C_CODE_T_PAIEMENT)) {
//					TaTPaiementDAO dao = new TaTPaiementDAO(getEm());
//	
//					dao.setModeObjet(getDao().getModeObjet());
//					TaTPaiement f = new TaTPaiement();
//					PropertyUtils.setSimpleProperty(f, nomChamp, value);
//					s = dao.validate(f,nomChamp,validationContext);
//	
//					if(s.getSeverity()!=IStatus.ERROR ){
//						f = dao.findByCode((String)value);
//						taTiers.setTaTPaiement(f);
//					}
//					dao = null;
					ITaTPaiementServiceRemote dao = new EJBLookup<ITaTPaiementServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_T_PAIEMENT_SERVICE, ITaTPaiementServiceRemote.class.getName());
					TaTPaiementDTO dto = new TaTPaiementDTO();
					try {
						if(TYPE_VALIDATION==VALIDATION_CLIENT || TYPE_VALIDATION==VALIDATION_CLIENT_ET_SERVEUR) {
							AbstractApplicationDAOClient<TaTPaiementDTO> validationClient = new AbstractApplicationDAOClient<TaTPaiementDTO>();
							PropertyUtils.setSimpleProperty(dto, nomChamp, value);
							validationClient.validate(dto,nomChamp,ITaTiersServiceRemote.validationContext);
						}
						if(resultat==null && (TYPE_VALIDATION==VALIDATION_SERVEUR || TYPE_VALIDATION==VALIDATION_CLIENT_ET_SERVEUR)) {
							PropertyUtils.setSimpleProperty(dto, nomChamp, value);
							dao.validateDTOProperty(dto, nomChamp,ITaTiersServiceRemote.validationContext);
						}
					} catch(Exception e) {
						resultat = new Status(IStatus.ERROR,TiersPlugin.PLUGIN_ID, e.getMessage(), e);
					}
					if(resultat!=null && resultat.getSeverity()!=IStatus.ERROR ){
						TaTPaiement entity = new TaTPaiement();
						entity = dao.findByCode((String)value);
						taTiers.setTaTPaiement(entity);
					}
					dao = null;						
				}else if(nomChamp.equals(Const.C_CODE_T_TARIF)) {
//					TaTTarifDAO dao = new TaTTarifDAO(getEm());
//	
//					dao.setModeObjet(getDao().getModeObjet());
//					TaTTarif f = new TaTTarif();
//					PropertyUtils.setSimpleProperty(f, nomChamp, value);
//					s = dao.validate(f,nomChamp,validationContext);
//	
//					if(s.getSeverity()!=IStatus.ERROR ){
//						f = dao.findByCode((String)value);
//						taTiers.setTaTTarif(f);
//					}
//					dao = null;
					ITaTTarifServiceRemote dao = new EJBLookup<ITaTTarifServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_T_TARIF_SERVICE, ITaTTarifServiceRemote.class.getName());
					TaTTarifDTO dto = new TaTTarifDTO();
					try {
						if(TYPE_VALIDATION==VALIDATION_CLIENT || TYPE_VALIDATION==VALIDATION_CLIENT_ET_SERVEUR) {
							AbstractApplicationDAOClient<TaTTarifDTO> validationClient = new AbstractApplicationDAOClient<TaTTarifDTO>();
							PropertyUtils.setSimpleProperty(dto, nomChamp, value);
							validationClient.validate(dto,nomChamp,ITaTiersServiceRemote.validationContext);
						}
						if(resultat==null && (TYPE_VALIDATION==VALIDATION_SERVEUR || TYPE_VALIDATION==VALIDATION_CLIENT_ET_SERVEUR)) {
							PropertyUtils.setSimpleProperty(dto, nomChamp, value);
							dao.validateDTOProperty(dto, nomChamp,ITaTiersServiceRemote.validationContext);
						}
					} catch(Exception e) {
						resultat = new Status(IStatus.ERROR,TiersPlugin.PLUGIN_ID, e.getMessage(), e);
					}
					if(resultat!=null && resultat.getSeverity()!=IStatus.ERROR ){
						TaTTarif entity = new TaTTarif();
						entity = dao.findByCode((String)value);
						taTiers.setTaTTarif(entity);
					}
					dao = null;						
				}else if(nomChamp.equals(Const.C_NUMERO_TELEPHONE)) {
//					TaTelephoneDAO dao = new TaTelephoneDAO(getEm());
//	
//					if(value==null || value.equals("")) { 
//						taTiers.removeTelephone(taTiers.getTaTelephone());
//					}
//					
//					dao.setModeObjet(getDao().getModeObjet());
//					TaTelephone f = new TaTelephone();
//					PropertyUtils.setSimpleProperty(f, nomChamp, value);
//					s = dao.validate(f,nomChamp,validationContext);
//	
//					if(s.getSeverity()!=IStatus.ERROR ){
//						initTelephoneTiers(value);
//						if(value!=null && !value.equals("")) { 
//							PropertyUtils.setSimpleProperty(taTiers.getTaTelephone(), nomChamp, value);
//						}
//						if(taTiers.getTaTelephone()!=null &&
//								taTiers.getTaTelephone().getTaTTel()==null){
//							TaTTel taTTel = new TaTTel();
//							taTTel.setCodeTTel(TiersPlugin.getDefault().getPreferenceStore().
//									getString(PreferenceConstants.TTEL_DEFAUT));
//							if(!taTTel.getCodeTTel().equals("")){
//								TaTTelDAO taTTelDAO = new TaTTelDAO(getEm());
//								taTTel=taTTelDAO.findByCode(taTTel.getCodeTTel());
//								taTiers.getTaTelephone().setTaTTel(taTTel);
//							}
//						}					
//					}
//					dao = null;
					
					ITaTelephoneServiceRemote dao = new EJBLookup<ITaTelephoneServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_TELEPHONE_SERVICE, ITaTelephoneServiceRemote.class.getName());
					TaTelephoneDTO dto = new TaTelephoneDTO();
					
					if(value==null || value.equals("")) { 
						taTiers.removeTelephone(taTiers.getTaTelephone());
					}
					
					try {
						if(TYPE_VALIDATION==VALIDATION_CLIENT || TYPE_VALIDATION==VALIDATION_CLIENT_ET_SERVEUR) {
							AbstractApplicationDAOClient<TaTelephoneDTO> validationClient = new AbstractApplicationDAOClient<TaTelephoneDTO>();
							PropertyUtils.setSimpleProperty(dto, nomChamp, value);
							validationClient.validate(dto,nomChamp,ITaTiersServiceRemote.validationContext);
						}
						if(resultat==null && (TYPE_VALIDATION==VALIDATION_SERVEUR || TYPE_VALIDATION==VALIDATION_CLIENT_ET_SERVEUR)) {
							PropertyUtils.setSimpleProperty(dto, nomChamp, value);
							dao.validateDTOProperty(dto, nomChamp,ITaTiersServiceRemote.validationContext);
						}
					} catch(Exception e) {
						resultat = new Status(IStatus.ERROR,TiersPlugin.PLUGIN_ID, e.getMessage(), e);
					}
					if(resultat!=null && resultat.getSeverity()!=IStatus.ERROR ){
						taTiers.initTelephoneTiers(value,TiersPlugin.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.CORRESPONDANCE_TELEPHONE_ADMINISTRATIF_DEFAUT),
								TiersPlugin.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.CORRESPONDANCE_TELEPHONE_COMMERCIAL_DEFAUT));
						if(value!=null && !value.equals("")) { 
							PropertyUtils.setSimpleProperty(taTiers.getTaTelephone(), nomChamp, value);
						}
						if(taTiers.getTaTelephone()!=null &&
								taTiers.getTaTelephone().getTaTTel()==null){
							TaTTel taTTel = new TaTTel();
							taTTel.setCodeTTel(TiersPlugin.getDefault().getPreferenceStore().
									getString(PreferenceConstants.TTEL_DEFAUT));
							if(!taTTel.getCodeTTel().equals("")){
								//TaTTelDAO taTTelDAO = new TaTTelDAO(getEm());
								ITaTTelServiceRemote taTTelDAO = new EJBLookup<ITaTTelServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_T_TEL_SERVICE, ITaTTelServiceRemote.class.getName());
								taTTel=taTTelDAO.findByCode(taTTel.getCodeTTel());
								taTiers.getTaTelephone().setTaTTel(taTTel);
							}
						}					
					}
					dao = null;
				}else if(nomChamp.equals(Const.C_NOM_ENTREPRISE)) {
//					TaEntrepriseDAO dao = new TaEntrepriseDAO(getEm());
//	
//					dao.setModeObjet(getDao().getModeObjet());
//					TaEntreprise f = new TaEntreprise();
//					PropertyUtils.setSimpleProperty(f, nomChamp, value);
//					s = dao.validate(f,nomChamp,validationContext);
//	
//					if(s.getSeverity()!=IStatus.ERROR ){
//						initEntrepriseTiers(value);
//						if(value!=null && !value.equals("")) { 
//							PropertyUtils.setSimpleProperty(taTiers.getTaEntreprise(), nomChamp, value);
//						}
//	
//					}
//					dao = null;
					ITaEntrepriseServiceRemote dao = new EJBLookup<ITaEntrepriseServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_ENTREPRISE_SERVICE, ITaEntrepriseServiceRemote.class.getName());
					TaEntrepriseDTO dto = new TaEntrepriseDTO();
					try {
						if(TYPE_VALIDATION==VALIDATION_CLIENT || TYPE_VALIDATION==VALIDATION_CLIENT_ET_SERVEUR) {
							AbstractApplicationDAOClient<TaEntrepriseDTO> validationClient = new AbstractApplicationDAOClient<TaEntrepriseDTO>();
							PropertyUtils.setSimpleProperty(dto, nomChamp, value);
							validationClient.validate(dto,nomChamp,ITaTiersServiceRemote.validationContext);
						}
						if(resultat==null && (TYPE_VALIDATION==VALIDATION_SERVEUR || TYPE_VALIDATION==VALIDATION_CLIENT_ET_SERVEUR)) {
							PropertyUtils.setSimpleProperty(dto, nomChamp, value);
							dao.validateDTOProperty(dto, nomChamp,ITaTiersServiceRemote.validationContext);
						}
					} catch(Exception e) {
						resultat = new Status(IStatus.ERROR,TiersPlugin.PLUGIN_ID, e.getMessage(), e);
					}
					if(resultat!=null && resultat.getSeverity()!=IStatus.ERROR ){
						taTiers.initEntrepriseTiers(value);
						if(value!=null && !value.equals("")) { 
						PropertyUtils.setSimpleProperty(taTiers.getTaEntreprise(), nomChamp, value);
						}
					}
					dao = null;
				}else if(nomChamp.equals(Const.C_ADRESSE_EMAIL)) {
//					TaEmailDAO dao = new TaEmailDAO(getEm());
//					
//					if(value==null || value.equals("")) { 
//						taTiers.removeEmail(taTiers.getTaEmail());
//					}
//	
//					dao.setModeObjet(getDao().getModeObjet());
//					TaEmail f = new TaEmail();
//					PropertyUtils.setSimpleProperty(f, nomChamp, value);
//					s = dao.validate(f,nomChamp,validationContext);
//	
//					if(s.getSeverity()!=IStatus.ERROR ){
//						initEmailTiers(value);
//						if(value!=null && !value.equals("")) {
//							PropertyUtils.setSimpleProperty(taTiers.getTaEmail(), nomChamp, value);
//						}
//						if(taTiers.getTaEmail()!=null &&
//								taTiers.getTaEmail().getTaTEmail()==null){
//							TaTEmail taTEmail = new TaTEmail();
//							taTEmail.setCodeTEmail(TiersPlugin.getDefault().getPreferenceStore().
//									getString(PreferenceConstants.TEMAIL_DEFAUT));
//							if(!taTEmail.getCodeTEmail().equals("")){
//								TaTEmailDAO taTEmailDAO = new TaTEmailDAO(getEm());
//								taTEmail=taTEmailDAO.findByCode(taTEmail.getCodeTEmail());
//								taTiers.getTaEmail().setTaTEmail(taTEmail);
//							}
//						}	
//					}
//	
//					dao = null;
					
					ITaEmailServiceRemote dao = new EJBLookup<ITaEmailServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_EMAIL_SERVICE, ITaEmailServiceRemote.class.getName());
					TaEmailDTO dto = new TaEmailDTO();
					
					if(value==null || value.equals("")) { 
						taTiers.removeEmail(taTiers.getTaEmail());
					}
					
					try {
						if(TYPE_VALIDATION==VALIDATION_CLIENT || TYPE_VALIDATION==VALIDATION_CLIENT_ET_SERVEUR) {
							AbstractApplicationDAOClient<TaEmailDTO> validationClient = new AbstractApplicationDAOClient<TaEmailDTO>();
							PropertyUtils.setSimpleProperty(dto, nomChamp, value);
							validationClient.validate(dto,nomChamp,ITaTiersServiceRemote.validationContext);
						}
						if(resultat==null && (TYPE_VALIDATION==VALIDATION_SERVEUR || TYPE_VALIDATION==VALIDATION_CLIENT_ET_SERVEUR)) {
							PropertyUtils.setSimpleProperty(dto, nomChamp, value);
							dao.validateDTOProperty(dto, nomChamp,ITaTiersServiceRemote.validationContext);
						}
					} catch(Exception e) {
						resultat = new Status(IStatus.ERROR,TiersPlugin.PLUGIN_ID, e.getMessage(), e);
					}
					if(resultat!=null && resultat.getSeverity()!=IStatus.ERROR ){
						taTiers.initEmailTiers(value,TiersPlugin.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.CORRESPONDANCE_EMAIL_ADMINISTRATIF_DEFAUT),
								TiersPlugin.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.CORRESPONDANCE_EMAIL_COMMERCIAL_DEFAUT));
						if(value!=null && !value.equals("")) {
							PropertyUtils.setSimpleProperty(taTiers.getTaEmail(), nomChamp, value);
						}
						if(taTiers.getTaEmail()!=null &&
								taTiers.getTaEmail().getTaTEmail()==null){
							TaTEmail taTEmail = new TaTEmail();
							taTEmail.setCodeTEmail(TiersPlugin.getDefault().getPreferenceStore().
									getString(PreferenceConstants.TEMAIL_DEFAUT));
							if(!taTEmail.getCodeTEmail().equals("")){
								ITaTEmailServiceRemote taTEmailDAO = new EJBLookup<ITaTEmailServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_T_EMAIL_SERVICE, ITaTEmailServiceRemote.class.getName());
								taTEmail=taTEmailDAO.findByCode(taTEmail.getCodeTEmail());
								taTiers.getTaEmail().setTaTEmail(taTEmail);
							}
						}	
						
					}
					dao = null;
				}else if(nomChamp.equals(Const.C_ADRESSE_WEB)) {
//					TaWebDAO dao = new TaWebDAO(getEm());
//					
//					if(value==null || value.equals("")) { 
//						taTiers.removeWeb(taTiers.getTaWeb());
//					}
//	
//					dao.setModeObjet(getDao().getModeObjet());
//					TaWeb f = new TaWeb();
//					PropertyUtils.setSimpleProperty(f, nomChamp, value);
//					s = dao.validate(f,nomChamp,validationContext);
//	
//					if(s.getSeverity()!=IStatus.ERROR ){
//						initWebTiers(value);
//						if(value!=null && !value.equals("")) {
//							PropertyUtils.setSimpleProperty(taTiers.getTaWeb(), nomChamp, value);
//						}
//						if(taTiers.getTaWeb()!=null &&
//								taTiers.getTaWeb().getTaTWeb()==null){
//							TaTWeb taTWeb = new TaTWeb();
//							taTWeb.setCodeTWeb(TiersPlugin.getDefault().getPreferenceStore().getString(PreferenceConstants.TWEB_DEFAUT));
//							if(!taTWeb.getCodeTWeb().equals("")){
//								TaTWebDAO taTWebDAO = new TaTWebDAO(getEm());
//								taTWeb=taTWebDAO.findByCode(taTWeb.getCodeTWeb());
//								taTiers.getTaWeb().setTaTWeb(taTWeb);
//							}
//						}					
//					}
//					dao = null;
					
					ITaWebServiceRemote dao = new EJBLookup<ITaWebServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_WEB_SERVICE, ITaWebServiceRemote.class.getName());
					TaWebDTO dto = new TaWebDTO();
					
					if(value==null || value.equals("")) { 
						taTiers.removeWeb(taTiers.getTaWeb());
					}
					
					try {
						if(TYPE_VALIDATION==VALIDATION_CLIENT || TYPE_VALIDATION==VALIDATION_CLIENT_ET_SERVEUR) {
							AbstractApplicationDAOClient<TaWebDTO> validationClient = new AbstractApplicationDAOClient<TaWebDTO>();
							PropertyUtils.setSimpleProperty(dto, nomChamp, value);
							validationClient.validate(dto,nomChamp,ITaTiersServiceRemote.validationContext);
						}
						if(resultat==null && (TYPE_VALIDATION==VALIDATION_SERVEUR || TYPE_VALIDATION==VALIDATION_CLIENT_ET_SERVEUR)) {
							PropertyUtils.setSimpleProperty(dto, nomChamp, value);
							dao.validateDTOProperty(dto, nomChamp,ITaTiersServiceRemote.validationContext);
						}
					} catch(Exception e) {
						resultat = new Status(IStatus.ERROR,TiersPlugin.PLUGIN_ID, e.getMessage(), e);
					}
					if(resultat!=null && resultat.getSeverity()!=IStatus.ERROR ){
						taTiers.initWebTiers(value);
						if(value!=null && !value.equals("")) {
							PropertyUtils.setSimpleProperty(taTiers.getTaWeb(), nomChamp, value);
						}
						if(taTiers.getTaWeb()!=null &&
								taTiers.getTaWeb().getTaTWeb()==null){
							TaTWeb taTWeb = new TaTWeb();
							taTWeb.setCodeTWeb(TiersPlugin.getDefault().getPreferenceStore().getString(PreferenceConstants.TWEB_DEFAUT));
							if(!taTWeb.getCodeTWeb().equals("")){
								ITaTWebServiceRemote taTWebDAO = new EJBLookup<ITaTWebServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_T_WEB_SERVICE, ITaTWebServiceRemote.class.getName());
								//TaTWebDAO taTWebDAO = new TaTWebDAO(getEm());
								taTWeb=taTWebDAO.findByCode(taTWeb.getCodeTWeb());
								taTiers.getTaWeb().setTaTWeb(taTWeb);
							}
						}			
					}
					dao = null;
				}else if(nomChamp.equals(Const.C_LIBL_COMMENTAIRE)) {
//					TaCommentaireDAO dao = new TaCommentaireDAO(getEm());
//	
//					dao.setModeObjet(getDao().getModeObjet());
//					TaCommentaire f = new TaCommentaire();
//					PropertyUtils.setSimpleProperty(f, nomChamp, value);
//					s = dao.validate(f,nomChamp,validationContext);
//	
//					if(s.getSeverity()!=IStatus.ERROR ){
//						initCommentaireTiers(value);
//						if(value!=null && !value.equals("")) {						
//							PropertyUtils.setSimpleProperty(taTiers.getTaCommentaire(), nomChamp, value);
//						}
//					}
//					dao = null;				
				}else if(nomChamp.equals(Const.C_ADRESSE1_ADRESSE)
						|| nomChamp.equals(Const.C_ADRESSE2_ADRESSE)
						|| nomChamp.equals(Const.C_ADRESSE3_ADRESSE)
						|| nomChamp.equals(Const.C_CODEPOSTAL_ADRESSE)
						|| nomChamp.equals(Const.C_VILLE_ADRESSE)
						|| nomChamp.equals(Const.C_PAYS_ADRESSE)) {
//					TaAdresseDAO dao = new TaAdresseDAO(getEm());
//	
//					dao.setModeObjet(getDao().getModeObjet());
//					TaAdresse f = new TaAdresse();
//					PropertyUtils.setSimpleProperty(f, nomChamp, value);
//					if((value==null || value.equals(""))&&(!adresseEstRempli()))
//						s = new Status(Status.OK,gestComBdPlugin.PLUGIN_ID,"OK");
//					else
//						s = dao.validate(f,nomChamp,validationContext);
//					if(s.getSeverity()!=IStatus.ERROR || (!adresseEstRempli())){
//						initAdresseTiers(value);
//						if(value!=null 
//	//							&& !value.equals("")
//								/*
//								 * Bug #1192
//								 * Si on laisse !value.equals("") on ne peu plus effacer de ligne d'adresse
//								 */
//						) {	
//							if(taTiers.getTaAdresse()!=null)
//								PropertyUtils.setSimpleProperty(taTiers.getTaAdresse(), nomChamp, value);
//						}
//						if(taTiers.getTaAdresse()!=null &&
//								taTiers.getTaAdresse().getTaTAdr()==null){
//							TaTAdr taTAdr = new TaTAdr();
//							taTAdr.setCodeTAdr(TiersPlugin.getDefault().getPreferenceStore().getString(PreferenceConstants.TADR_DEFAUT));
//							if(!taTAdr.getCodeTAdr().equals("")){
//								TaTAdrDAO taTAdrDAO = new TaTAdrDAO(getEm());
//								taTAdr=taTAdrDAO.findByCode(taTAdr.getCodeTAdr());
//								taTiers.getTaAdresse().setTaTAdr(taTAdr);
//							}
//						}					
//					}
//					dao = null;
					
					ITaAdresseServiceRemote dao = new EJBLookup<ITaAdresseServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_ADRESSE_SERVICE, ITaAdresseServiceRemote.class.getName());
					TaAdresseDTO dto = new TaAdresseDTO();
					
					if((value==null || value.equals(""))&&(!adresseEstRempli())) {
						resultat = new Status(Status.OK,gestComBdPlugin.PLUGIN_ID,"OK");
					} else {

						try {
							if(TYPE_VALIDATION==VALIDATION_CLIENT || TYPE_VALIDATION==VALIDATION_CLIENT_ET_SERVEUR) {
								AbstractApplicationDAOClient<TaAdresseDTO> validationClient = new AbstractApplicationDAOClient<TaAdresseDTO>();
								PropertyUtils.setSimpleProperty(dto, nomChamp, value);
								validationClient.validate(dto,nomChamp,ITaTiersServiceRemote.validationContext);
							}
							if(resultat==null && (TYPE_VALIDATION==VALIDATION_SERVEUR || TYPE_VALIDATION==VALIDATION_CLIENT_ET_SERVEUR)) {
								PropertyUtils.setSimpleProperty(dto, nomChamp, value);
								dao.validateDTOProperty(dto, nomChamp, ITaTiersServiceRemote.validationContext);
							}
						} catch(Exception e) {
							resultat = new Status(IStatus.ERROR,TiersPlugin.PLUGIN_ID, e.getMessage(), e);
						}
					}
					if(resultat!=null && resultat.getSeverity()!=IStatus.ERROR ){
						taTiers.initAdresseTiers(value,adresseEstRempli(),TiersPlugin.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.CORRESPONDANCE_ADRESSE_ADMINISTRATIF_DEFAUT),
								TiersPlugin.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.CORRESPONDANCE_ADRESSE_COMMERCIAL_DEFAUT));
						if(value!=null 
	//							&& !value.equals("")
								/*
								 * Bug #1192
								 * Si on laisse !value.equals("") on ne peu plus effacer de ligne d'adresse
								 */
						) {	
							if(taTiers.getTaAdresse()!=null)
								PropertyUtils.setSimpleProperty(taTiers.getTaAdresse(), nomChamp, value);
						}
						if(taTiers.getTaAdresse()!=null &&
								taTiers.getTaAdresse().getTaTAdr()==null){
							TaTAdr taTAdr = new TaTAdr();
							taTAdr.setCodeTAdr(TiersPlugin.getDefault().getPreferenceStore().getString(PreferenceConstants.TADR_DEFAUT));
							if(!taTAdr.getCodeTAdr().equals("")){
								//TaTAdrDAO taTAdrDAO = new TaTAdrDAO(getEm());
								ITaTAdrServiceRemote taTAdrDAO = new EJBLookup<ITaTAdrServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_T_ADRESSE_SERVICE, ITaTAdrServiceRemote.class.getName());
								taTAdr = taTAdrDAO.findByCode(taTAdr.getCodeTAdr());
								taTiers.getTaAdresse().setTaTAdr(taTAdr);
							}
						}			
					}
				} else {
//					boolean verrouilleModifCode = false;
//					TaTiers u = new TaTiers();
//					if(nomChamp.equals(Const.C_TTC_TIERS) || nomChamp.equals(Const.C_ACTIF_TIERS)) { //traitement des booleens
//						if((Boolean)value==true) value=new Integer(1); else value=new Integer(0);
//					}
//	
//					PropertyUtils.setSimpleProperty(u, nomChamp, value);
//					
//					if(swtTiers!=null && swtTiers.getId()!=null) {
//						u.setIdTiers(swtTiers.getId());
//					}
//	
//					if(nomChamp.equals(Const.C_CODE_TIERS)){
//						verrouilleModifCode = true;
//						if(taTiers!=null && !u.getCodeTiers().equalsIgnoreCase(taTiers.getCodeTiers())){
//							PropertyUtils.setSimpleProperty(taTiers, Const.C_CODE_COMPTA, u.getCodeTiers());
//							swtTiers.setCodeCompta(taTiers.getCodeCompta());
//						}
//					}
//	
//					s = dao.validate(u,nomChamp,validationContext,verrouilleModifCode);
//					
//					if(s.getSeverity()!=Status.ERROR){
//						/*
//						 * Bug #1192
//						 * Forcer le remplissage de l'entité "à la main" car si on compte sur le mapping de Dozer qui
//						 * s'effectue plus tard, l'option map-empty-string étant à faux pour TaTiersDTO, il est impossible d'effacer
//						 * complètement un champ texte (se trouvant au premier niveau dans la classe TaTiers).
//						 * 
//						 * Par contre, il faut bien faire attention de ne pas modifié l'ID de l'objet
//						 */
//						//if(nomChamp.equals(Const.C_ID_TIERS)) {
//							PropertyUtils.setSimpleProperty(taTiers, nomChamp, value);
//						//}
//	
//					}
				}
//				//			s = new Status(Status.OK,gestComBdPlugin.PLUGIN_ID,"OK");
//				if(s.getSeverity()!=Status.ERROR){
//					if(nomChamp.equals(Const.C_CODE_TIERS)) {
//						//PropertyUtils.setSimpleProperty(taTiers, Const.C_CODE_TIERS, value);
//						initCodeCompta();
//						
//					}				
//				}
			return resultat;
//			return retour;
//		} catch (IllegalAccessException e) {
//			logger.error("",e);
//		} catch (InvocationTargetException e) {
//			logger.error("",e);
//		} catch (NoSuchMethodException e) {
//			logger.error("",e);
		} catch (Exception e) {
			logger.error("",e);
		}
		return null;
	}

	@Override
	protected void actEnregistrer() throws Exception {
//		EntityTransaction transaction = dao.getEntityManager().getTransaction();
		try {
			try {
				boolean declanchementExterne = false;
				if(sourceDeclencheCommandeController==null|| mapping) {
					declanchementExterne = true;
				}
				
				boolean inserer = true;
				if ( (modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION) == 0)) {
					inserer = false;
				}
				
				if ((modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION) == 0)
						|| (modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION) == 0)) {

					if(declanchementExterne) {
						//ctrlTousLesChampsAvantEnregistrementSWT();
					}

					if(taTiers==null)taTiers=new TaTiers();	
					if(declanchementExterne) {
						mapperUIToModel.map(swtTiers,taTiers);
					}
					
					fireEnregistreTout(new AnnuleToutEvent(this,true));

					if(!enregistreToutEnCours) {
//						dao.begin(transaction);
						
//						if((modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION) == 0)){	
//							taTiers=dao.enregistrerMerge(taTiers);
//							//modelTiers.getListeEntity().add(taTiers);
//						}
//						else taTiers=dao.enregistrerMerge(taTiers);
						
						if ((modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION) == 0)||
								(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION) == 0)) {
							//try {
							//LgrDozerMapper<TaTiersDTO,TaTiers> mapper = new LgrDozerMapper<TaTiersDTO,TaTiers>();
							//mapper.map((TaTiersDTO) selectedTypeCivilite.getValue(),taTiers);

//mapper sur client, envoi d'une entité					
//							TaTiersMapper mapper = new TaTiersMapper();
//							mapper.mapDtoToEntity((TaTiersDTO) swtTiers,taTiers);
							if(declanchementExterne ) {
								mapperUIToModel.map(swtTiers,taTiers);
							}
							
							
							if (modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION) == 0)
								taTiers = dao.enregistrerMerge(taTiers,ITaTiersServiceRemote.validationContext);
							else if (modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION) == 0) {
								//dao.enregistrerPersist(taTiers);
								taTiers = dao.enregistrerMerge(taTiers,ITaTiersServiceRemote.validationContext); //object composé avec des type existant => merge
							}
								
			//mapper sur serveur, envoi d'un DTO					
//								if (modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION) == 0)
//									dao.enregistrerMergeDTO((TaTiersDTO) swtTiers);
//								else if (modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION) == 0)
//									dao.enregistrerPersistDTO((TaTiersDTO) swtTiers);

						
						if(taTiers.getIdTiers()==TaTiersDAO.C_ID_IDENTITE_ENTREPRISE_INT) {
						//if(idTypeTiers!=null && idTypeTiers.equals(TaTiersDAO.C_ID_IDENTITE_ENTREPRISE_STR)) {
							logger.debug("Mise à jour des infos entreprise à partir de celle du tiers (-1)");
							//TaInfoEntrepriseDAO infoDAO = new TaInfoEntrepriseDAO(getEm());
							ITaInfoEntrepriseServiceRemote infoDAO = new EJBLookup<ITaInfoEntrepriseServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_INFO_ENTREPRISE_SERVICE, ITaInfoEntrepriseServiceRemote.class.getName());
							TaInfoEntreprise taInfoEntreprise = infoDAO.findInstance();
							
							if(taInfoEntreprise.getCodexoInfoEntreprise()==null) {
								/*
								 * nouveau dossier, on initialise les date avec le même traitement 
								 * que dans actinserer de SWTPaInfoEntrepriseSimpleController
								 */	
								Date dateDeb = new Date();
								Date dateFin = new Date(); 
								dateFin=LibDate.incrementDate(dateFin, -1, 0, 1);
								
								taInfoEntreprise.setDatedebInfoEntreprise(dateDeb);
								taInfoEntreprise.setDatefinInfoEntreprise(dateFin);
								
								/*
								 * Pour le code exo,
								 * on prend les 2 derniers chiffre de l'année courante
								 */
								String dateStr = LibDate.dateToString(new Date());
								taInfoEntreprise.setCodexoInfoEntreprise(dateStr.substring(dateStr.length()-2));
							}
							if(taTiers.getTaEntreprise()!=null) {
								taInfoEntreprise.setNomInfoEntreprise(taTiers.getTaEntreprise().getNomEntreprise());
							}
							if(taTiers.getTaAdresse()!=null) {
								taInfoEntreprise.setAdresse1InfoEntreprise(taTiers.getTaAdresse().getAdresse1Adresse());
								taInfoEntreprise.setAdresse2InfoEntreprise(taTiers.getTaAdresse().getAdresse2Adresse());
								taInfoEntreprise.setAdresse3InfoEntreprise(taTiers.getTaAdresse().getAdresse3Adresse());
								taInfoEntreprise.setCodepostalInfoEntreprise(taTiers.getTaAdresse().getCodepostalAdresse());
								taInfoEntreprise.setVilleInfoEntreprise(taTiers.getTaAdresse().getVilleAdresse());
								taInfoEntreprise.setPaysInfoEntreprise(taTiers.getTaAdresse().getPaysAdresse());
							}
							if(taTiers.getTaTelephone()!=null) {
								taInfoEntreprise.setTelInfoEntreprise(taTiers.getTaTelephone().getNumeroTelephone());
							}
							//TaTTelDAO taTTelDAO=new TaTTelDAO();
							ITaTTelServiceRemote taTTelDAO = new EJBLookup<ITaTTelServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_T_TEL_SERVICE, ITaTTelServiceRemote.class.getName());
							TaTTel typeFax=taTTelDAO.findByCode("FAX");
							TaTelephone telFax=taTiers.aDesTelephoneDuType(typeFax.getCodeTTel());
							if(telFax!=null) {
								taInfoEntreprise.setFaxInfoEntreprise(telFax.getNumeroTelephone());
							}
							if(taTiers.getTaInfoJuridique()!=null) {
								taInfoEntreprise.setSiretInfoEntreprise(taTiers.getTaInfoJuridique().getSiretInfoJuridique());
								taInfoEntreprise.setApeInfoEntreprise(taTiers.getTaInfoJuridique().getApeInfoJuridique());
								taInfoEntreprise.setRcsInfoEntreprise(taTiers.getTaInfoJuridique().getRcsInfoJuridique());
								taInfoEntreprise.setCapitalInfoEntreprise(taTiers.getTaInfoJuridique().getCapitalInfoJuridique());
							}
							if(taTiers.getTaCompl()!=null) {
								taInfoEntreprise.setTvaIntraInfoEntreprise(taTiers.getTaCompl().getTvaIComCompl());
							}
							if(taTiers.getTaEmail()!=null) {
								taInfoEntreprise.setEmailInfoEntreprise(taTiers.getTaEmail().getAdresseEmail());
							}
							if(taTiers.getTaWeb()!=null) {
								taInfoEntreprise.setWebInfoEntreprise(taTiers.getTaWeb().getAdresseWeb());
							}
							
							//Ajouter des valeurs par defaut dans infos entreprise pour les champs obligatoire non remplis ?
							if(taInfoEntreprise.getVilleInfoEntreprise()==null
									|| taInfoEntreprise.getPaysInfoEntreprise()==null
									|| taInfoEntreprise.getCodexoInfoEntreprise()==null) {
								
							}
							
//							if(taInfoEntreprise.getCodexoInfoEntreprise()!=null) {
//								taInfoEntreprise.setWebInfoEntreprise(taTiers.getTaWeb().getAdresseWeb());
//							}
							
							taInfoEntreprise = infoDAO.enregistrerMerge(taInfoEntreprise);
						}

						//dao.commit(transaction);

//						if(!containsEntity(taTiers)) 
//							modelTiers.getListeEntity().add(taTiers);
						actRefresh(); 
						changementDeSelection();
						swtTiers.setId(taTiers.getIdTiers());
						updateView(taTiers,inserer);
						getModeEcran().setMode(EnumModeObjet.C_MO_CONSULTATION);//ejb
						
						hideDecoratedFields();
						vue.getLaMessage().setText("");
					} 
					//transaction = null;
					}				
					}
			}
			catch (Exception e) {
				logger.error("",e);
				
				if(e.getMessage()!=null)
					vue.getLaMessage().setText(e.getMessage());
				
				afficheDecoratedField(vue.getTfCODE_TIERS(),e.getMessage());
				
				throw new Exception(e);
			}	
		}finally {
//			if(transaction!=null && transaction.isActive()) {
//				transaction.rollback();
//			}
			initEtatBouton();
		}
	}
	
	private ILgrListView<TaTiers> findViewListeTiers() {
		IViewReference[] vr = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences();
		for (int i = 0; i < vr.length; i++) {
			if(vr[i].getId().endsWith(ListeTiersView.ID)) {
					return ((ILgrListView<TaTiers>)vr[i].getView(false));
			}
		}
		return null;
	}
	
	public void updateView(TaTiers taTiers,boolean inserer) {
		ILgrListView<TaTiers> view = findViewListeTiers(); 
		if(view!=null) {
			if(inserer) {
				view.update(taTiers);
			} else
				view.refresh(taTiers);
		}
	}
	
	public void activeGrilleView(boolean active) {
		ILgrListView<TaTiers> view = findViewListeTiers(); 
		if(view!=null) {
			if(view instanceof ListeTiersView) {
				((ListeTiersView)view).getVue().getLgrViewer().getViewer().getTable().setEnabled(active);
				((ListeTiersView)view).getVue().getTfCodeTiers().setEnabled(active);
			}
		}
	}
	
	public void removeView(TaTiers taTiers) {
		ILgrListView<TaTiers> view = findViewListeTiers(); 
		if(view!=null) {
				view.remove(taTiers);
		}
	}
	
	public void selectView(TaTiers taTiers) {
		ILgrListView<TaTiers> view = findViewListeTiers(); 
		if(view!=null) {
				view.select(taTiers);
		}
	}
	
	public void selectView(int index) {
		ILgrListView<TaTiers> view = findViewListeTiers(); 
		if(view!=null) {
				view.select(index);
		}
	}

	public boolean isUtilise(){
//		return (selectedTiers!=null && selectedTiers.getValue()!=null &&
//				((TaTiersDTO)selectedTiers.getValue()).getIdTiers()!=null && 
//				!dao.recordModifiable(dao.getNomTable(),
//						((TaTiersDTO)selectedTiers.getValue()).getIdTiers()))||
//						!dao.autoriseModification(taTiers);	
		return (swtTiers!=null &&
				swtTiers.getId()!=null && !dao.recordModifiable(dao.getNomTable(),swtTiers.getId()))
				|| !dao.autoriseModification(taTiers);
	}

	public String getIdCommentaire() {
		return idCommentaire;
	}

	public void setIdCommentaire(String idCommentaire) {
		this.idCommentaire = idCommentaire;
	}

	//	public SWT_IB_TA_TIERS getIbTaTable() {
	//		return ibTaTable;
	//	}

	public void initEtatComposant(){
		vue.getTfCODE_TIERS().setEditable(!isUtilise());
		changeCouleur(vue);
	}

	public TaTiersDTO getSwtOldTiers() {
		return swtOldTiers;
	}

	public void setSwtOldTiers(TaTiersDTO swtOldTiers) {
		this.swtOldTiers = swtOldTiers;
	}
	public void setSwtOldTiers() {
		if(swtTiers!=null) {
			this.swtOldTiers=TaTiersDTO.copy(swtTiers);
//		if (selectedTiers!=null&&selectedTiers.getValue()!=null){
//			//dao.refresh(dao.findById(((TaTiersDTO) selectedTiers.getValue()).getIdTiers()));
//			this.swtOldTiers=TaTiersDTO.copy((TaTiersDTO)selectedTiers.getValue());
//		}
//		else{
//			if (tableViewer.selectionGrille(0)){
//				//dao.refresh(dao.findById(((TaTiersDTO) selectedTiers.getValue()).getIdTiers()));
//				this.swtOldTiers=TaTiersDTO.copy((TaTiersDTO)selectedTiers.getValue());
//				tableViewer.setSelection(new StructuredSelection((TaTiersDTO)selectedTiers.getValue()),true);
//			}
		}
	}

	//	public void setSwtOldTiersRefresh() {
	//		if (selectedTiers.getValue()!=null){
	//			if(LgrMess.isDOSSIER_EN_RESEAU())
	//				dao.refresh(dao.findById(((TaTiersDTO) selectedTiers.getValue()).getIdTiers()));
	//			taTiers=dao.findById(((TaTiersDTO) selectedTiers.getValue()).getIdTiers());
	//			TaTiersDTO oldSwtTiers=(TaTiersDTO) selectedTiers.getValue();
	//			mapperModelToUI.map(taTiers,swtOldTiers);
	//			if(!oldSwtTiers.equals(swtOldTiers)){
	//				try {
	//					actRefresh();
	//				} catch (Exception e) {
	//					logger.error("",e);
	//				}
	//			}
	//			this.swtOldTiers=TaTiersDTO.copy((TaTiersDTO)selectedTiers.getValue());
	//		}
	//	}
	public boolean containsEntity(TaTiers entite){
		if(modelTiers.getListeEntity()!=null){
			for (Object e : modelTiers.getListeEntity()) {
				if(((TaTiers)e).getIdTiers()==
					entite.getIdTiers())return true;
			}
		}
		return false;
	}

	public boolean setSwtOldTiersRefresh() {
		try {	
			if (swtTiers!=null){
				TaTiers taOld =dao.findById(taTiers.getIdTiers());
				//taOld=dao.refresh(taOld); //ejb commentaire
				
//				if(containsEntity(taTiers)) 
//					modelTiers.getListeEntity().remove(taTiers);
//				if(!taTiers.getVersionObj().equals(taOld.getVersionObj())){
//					taTiers=taOld;
//					if(!containsEntity(taTiers)) 
//						modelTiers.getListeEntity().add(taTiers);					
//					if(!containsEntity(taTiers)) 
//						modelTiers.getListeEntity().add(taTiers);
//					changementDeSelection();
//					actRefresh();
//					dao.messageNonAutoriseModification();
//				}
				taTiers=taOld;
				fireChangementMaster(new ChangementMasterEntityEvent(this,taTiers,true));
				this.swtOldTiers=TaTiersDTO.copy(swtTiers);
			}
			return true;
		} catch (Exception e) {
			return false;
		}		
	}
	public void setVue(PaTiersSWT vue) {
		super.setVue(vue);
		this.vue = vue;
	}



	@Override
	protected void initMapComposantDecoratedField() {
		if (mapComposantDecoratedField == null) 
			mapComposantDecoratedField = new LinkedHashMap<Control,ControlDecoration>();
		mapComposantDecoratedField.put(vue.getTfCODE_TIERS(),vue.getFieldCODE_TIERS());
		mapComposantDecoratedField.put(vue.getTfCODE_COMPTA(),vue.getFieldCODE_COMPTA());
		mapComposantDecoratedField.put(vue.getTfCODE_T_TVA_DOC(),vue.getFieldCODE_T_TVA_DOC());
		mapComposantDecoratedField.put(vue.getTfNOM_TIERS(),vue.getFieldNOM_TIERS());
		mapComposantDecoratedField.put(vue.getTfPRENOM_TIERS(),vue.getFieldPRENOM_TIERS());
		mapComposantDecoratedField.put(vue.getTfSURNOM_TIERS(),vue.getFieldSURNOM_TIERS());
		mapComposantDecoratedField.put(vue.getDateTimeDATEANNIV(),vue.getFieldDATE_ANNIV());
		//		mapComposantDecoratedField.put(vue.getCbACTIF_TIERS(), vue.getCbACTIF_TIERS());
		//		mapComposantDecoratedField.put(vue.getTfTTC(), vue.getTfTTC());
		mapComposantDecoratedField.put(vue.getTfCODE_T_CIVILITE(),vue.getFieldCODE_T_CIVILITE());
		mapComposantDecoratedField.put(vue.getTfCODE_ENTREPRISE(), vue.getFieldCODE_ENTREPRISE());
		mapComposantDecoratedField.put(vue.getTfCODE_T_TIERS(), vue.getFieldCODE_T_TIERS());
		mapComposantDecoratedField.put(vue.getTfCOMPTE(),vue.getFieldCOMPTE());
		mapComposantDecoratedField.put(vue.getTfLIBL_COMMENTAIRE(), vue.getFieldLIBL_COMMENTAIRE());
		//#JPA
		//		mapComposantDecoratedField.put(vue.getTfCODE_BANQUE(), vue.getFieldCODE_BANQUE());
		mapComposantDecoratedField.put(vue.getTfEntite(), vue.getFieldEntite());

		mapComposantDecoratedField.put(vue.getTfADRESSE1_ADRESSE(), vue.getFieldADRESSE1_ADRESSE());
		mapComposantDecoratedField.put(vue.getTfADRESSE2_ADRESSE(), vue.getFieldADRESSE2_ADRESSE());
		mapComposantDecoratedField.put(vue.getTfADRESSE3_ADRESSE(), vue.getFieldADRESSE3_ADRESSE());
		mapComposantDecoratedField.put(vue.getTfCODEPOSTAL_ADRESSE(), vue.getFieldCODEPOSTAL_ADRESSE());
		mapComposantDecoratedField.put(vue.getTfVILLE_ADRESSE(), vue.getFieldVILLE_ADRESSE());
		mapComposantDecoratedField.put(vue.getTfPAYS_ADRESSE(), vue.getFieldPAYS_ADRESSE());
		mapComposantDecoratedField.put(vue.getTfTVA_I_COM_COMPL(), vue.getFieldTVA_I_COM_COMPL());
		mapComposantDecoratedField.put(vue.getTfACCISE(), vue.getFieldACCISE());
		mapComposantDecoratedField.put(vue.getTfNUMERO_TELEPHONE(), vue.getFieldNUMERO_TELEPHONE());
		mapComposantDecoratedField.put(vue.getTfADRESSE_EMAIL(), vue.getFieldADRESSE_EMAIL());
		mapComposantDecoratedField.put(vue.getTfADRESSE_WEB(), vue.getFieldADRESSE_WEB());
		mapComposantDecoratedField.put(vue.getTfCODE_C_PAIEMENT(), vue.getFieldCODE_C_PAIEMENT());
		mapComposantDecoratedField.put(vue.getTfCODE_T_PAIEMENT(), vue.getFieldCODE_T_PAIEMENT());
		mapComposantDecoratedField.put(vue.getTfCODE_T_TARIF(), vue.getFieldCODE_T_TARIF());
	}



	public Class getClassModel() {
		return classModel;
	}



	@Override
	protected void sortieChamps() {
		//		try {
		//			if(ibTaTable.dataSetEnModif())
		//				ibTaTable.setInterface((TaTiersDTO)selectedTiers.getValue());
		//		} catch (Exception e) {
		//			logger.error("",e);
		//			vue.getLaMessage().setText(e.getMessage());
		//		}

	}

	protected void actSelection() throws Exception {
		try{
			IEditorPart e = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
			.getActivePage().openEditor(new fr.legrain.visualisation.editor.EditorInputSelectionVisualisation(), 
					fr.legrain.visualisation.editor.EditorSelectionVisualisation.ID);
			LgrPartListener.getLgrPartListener().getLgrNavigation().add(e);

			ParamAfficheVisualisation paramAfficheSelectionVisualisation = new ParamAfficheVisualisation();

			paramAfficheSelectionVisualisation.setNomClassController(nomClass);
			paramAfficheSelectionVisualisation.setNomRequete(Const.C_NOM_VU_TIERS);
			paramAfficheSelectionVisualisation.setModule("tiers");

			((EJBLgrEditorPart)e).setPanel(((EJBLgrEditorPart)e).getControllerSelection().getVue());
			((EJBLgrEditorPart)e).getControllerSelection().configPanel(paramAfficheSelectionVisualisation);

		}catch (Exception e) {
			logger.error("",e);
		}	
	}

	@Override
	protected void actRefresh() throws Exception {
		try{
			vue.setCursor(Display.getCurrent().getSystemCursor(SWT.CURSOR_WAIT));		
		//repositionnement sur la valeur courante
		int idActuel = 0;
		if (taTiers!=null) { //enregistrement en cours de modification/insertion
			idActuel = taTiers.getIdTiers();
		} else if(swtTiers!=null && swtTiers.getId()!=null) {
			idActuel = swtTiers.getId();
		}

		
		if (getModeEcran().getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION) == 0) {
//			writableList = new WritableList(realm, modelTiers.remplirListe(getEm()), classModel);
//			tableViewer.setInput(writableList);
		} else {
			if (taTiers!=null && swtTiers!=null) {
				mapperModelToUI.map(taTiers, swtTiers);
			}
		}

//		if(idActuel!=0) {
//			tableViewer.setSelection(new StructuredSelection(modelTiers.recherche(Const.C_ID_TIERS
//					, idActuel)),true);
//		}else
//			tableViewer.selectionGrille(0);	

	}finally{
		vue.setCursor(Display.getCurrent().getSystemCursor(SWT.CURSOR_ARROW));
	}
}

//	protected void actRefreshCourant() throws Exception {
//
//			vue.setCursor(Display.getCurrent().getSystemCursor(SWT.CURSOR_WAIT));		
//		//repositionnement sur la valeur courante
//		int idActuel = 0;
//		if (taTiers!=null) { //enregistrement en cours de modification/insertion
//			idActuel = taTiers.getIdTiers();
//		} else if(swtTiers!=null) {
//			idActuel = swtTiers.getId();
//		}
//
//		int rang = modelTiers.getListeObjet().indexOf(swtTiers);
//		mapperModelToUI.map(taTiers, swtOldTiers);
//		if (rang>0) modelTiers.getListeObjet().set(rang, swtOldTiers);
//		rang =((WritableList)tableViewer.getInput()).indexOf(swtOldTiers);
//		if (rang>0){
//			((WritableList)tableViewer.getInput()).set(rang, swtOldTiers);
//		}else{
//		writableList =new WritableList(realm, modelTiers.getListeObjet(),
//				classModel);
//		tableViewer.setInput(writableList);
//		}
//		if(idActuel!=0) {
//			tableViewer.setSelection(new StructuredSelection(modelTiers.recherche(Const.C_ID_TIERS
//					, idActuel)),true);
//		}else
//			tableViewer.selectionGrille(0);				
//	}



	public ModelTiers getModelTiers() {
		return modelTiers;
	}

//	public ModelGeneralObjet<TaTiersDTO,TaTiersDAO,TaTiers>  getModelTiers() {
//		return modelTiers;
//	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}



	public ITaTiersServiceRemote getDao() {
		return dao;
	}



	public TaTiersDTO getSwtTiers() {
		return swtTiers;
	}



	public TaTiers getTaTiers() {
		return taTiers;
	}

	public boolean changementPageValide(){		
		return (daoStandard.selectCount()>0);
	}



	public void setDao(ITaTiersServiceRemote dao) {
		this.dao = dao;
	};

	private boolean adresseEstRempli() {
		boolean retour=false;
		if(!vue.getTfADRESSE1_ADRESSE().getText().equals(""))return true;
		if(!vue.getTfADRESSE2_ADRESSE().getText().equals(""))return true;
		if(!vue.getTfADRESSE3_ADRESSE().getText().equals(""))return true;
		if(!vue.getTfVILLE_ADRESSE().getText().equals(""))return true;
		if(!vue.getTfCODEPOSTAL_ADRESSE().getText().equals(""))return true;
		if(!vue.getTfPAYS_ADRESSE().getText().equals(""))return true;
		return retour;
	}

	public void addChangementMasterEntityListener(IChangementMasterEntityListener l) {
		listenerList.add(IChangementMasterEntityListener.class, l);
	}
	
	public void removeChangementMasterEntityListener(IChangementMasterEntityListener l) {
		listenerList.remove(IChangementMasterEntityListener.class, l);
	}

	protected void fireChangementMaster(ChangementMasterEntityEvent e) throws Exception {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == IChangementMasterEntityListener.class) {
				if (e == null)
					e = new ChangementMasterEntityEvent(this);
				( (IChangementMasterEntityListener) listeners[i + 1]).changementMasterEntity(e);
			}
		}
	}


	public void testJPQL(){

//		//		String jpql = "select tbl from TaBonliv tbl "+
//		//		   "where "+
//		//		   "not exists "+ 
//		//		   "(select trd.taBonliv.idDocument from TaRDocument trd where trd.taBonliv.idDocument=tbl.idDocument) "+
//		//		   "or exists "+
//		//		   "(select trd.taBonliv.idDocument from TaRDocument trd where trd.taBonliv.idDocument=tbl.idDocument and trd.taFacture is null)"+
//		//		   "and "+
//		//		   "tbl.dateDocument "+
//		//		   "between "+
//		//		   "(select tie.datedebInfoEntreprise from TaInfoEntreprise tie) "+
//		//		   "and "+
//		//		   "(select tie.datefinInfoEntreprise from TaInfoEntreprise tie) "+
//		//		   "and tbl.taTiers.idTiers=1";
//		String jpql = "select tbl from TaBonliv tbl "+
//		"where tbl.taTiers.idTiers=1 "+
//		"and " +
//		"tbl.dateDocument " +
//		"between " +
//		"(select tie.datedebInfoEntreprise from TaInfoEntreprise tie) " +
//		"and " +
//		"(select tie.datefinInfoEntreprise from TaInfoEntreprise tie) " +
//		"and " +
//		"not exists " +
//		"(select trd.taBonliv.idDocument from TaRDocument trd where trd.taBonliv.idDocument=tbl.idDocument) " +
//		"or " +
//		"exists " +
//		"(select trd.taBonliv.idDocument from TaRDocument trd where trd.taBonliv.idDocument=tbl.idDocument and trd.taFacture is null)";
//
//		jpql = "select tb from TaBoncde tb where tb.idDocument not in (select trd.taBoncde.idDocument from "+ 
//		"TaRDocument trd where trd.taBoncde.idDocument is not null and trd.taFacture.idDocument is not null)";
//		
//		jpql = "select tlBoncmd from TaLBoncde tlBoncmd where tlBoncmd.taDocument.idDocument not in (select trd.taBoncde.idDocument from "+ 
//		"TaRDocument trd where trd.taBoncde.idDocument is not null and trd.taFacture.idDocument is not null)";
//		
//		JPABdLgr bdLgr = new JPABdLgr();
//		
//		Query query = bdLgr.getEntityManager().createQuery(jpql);
//		for (int i = 0; i < query.getResultList().size(); i++) {
//
//			System.out.println(query.getResultList().size());
//		}

	}
	
//	public void actAfficherTous() throws Exception{
//		vue.getGrille().setVisible(true);  
//		vue.getBtnTous().setVisible(false);
//		vue.getLaTitreGrille().setVisible(true);
//		vue.getCompositeForm().setWeights(new int[]{50,100});
//		dao.setJPQLQuery(dao.getJPQLQueryInitial());
//		modelTiers.setJPQLQuery(null);
//		modelTiers.setListeEntity(null);
//		try {
//			actRefresh();
//		} catch (Exception e1) {
//			logger.error("", e1);
//		}
//	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if(!vue.isDisposed()) {
			if(selection instanceof IStructuredSelection) {
				if ((getModeEcran().getMode().compareTo(EnumModeObjet.C_MO_INSERTION) != 0)
						&& (getModeEcran().getMode().compareTo(EnumModeObjet.C_MO_EDITION) != 0)) {
					TaTiersDTO t = (TaTiersDTO)((IStructuredSelection)selection).getFirstElement();
					if(t!=null) {
						System.out.println(t.getCodeTiers());
						ParamAffiche paramAffiche = new ParamAffiche();
						//paramAffiche.setIdFiche(String.valueOf(t.getIdTiers()));
						paramAffiche.setSelection(selection);
						configPanel(paramAffiche);
					}
				}
			}
		} else {
			//PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().removeSelectionListener(ListeTiersView.ID, this);
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().removePostSelectionListener(ListeTiersView.ID, this);
		}
		
	}
} 
