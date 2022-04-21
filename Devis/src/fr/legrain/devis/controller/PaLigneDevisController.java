package fr.legrain.devis.controller;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ejb.FinderException;
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
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.bindings.keys.ParseException;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.IContentProposalListener2;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.contexts.IContextActivation;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.internal.expressions.ActivePartExpression;
import org.eclipse.ui.part.WorkbenchPart;

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.TaPrixDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaPrix;
import fr.legrain.article.model.TaUnite;
import fr.legrain.articles.ecran.PaArticleSWT;
import fr.legrain.articles.ecran.PaPrixSWT;
import fr.legrain.articles.ecran.PaUniteSWT;
import fr.legrain.articles.ecran.ParamAfficheArticles;
import fr.legrain.articles.ecran.ParamAffichePrix;
import fr.legrain.articles.ecran.ParamAfficheUnite;
import fr.legrain.articles.ecran.SWTPaArticlesController;
import fr.legrain.articles.ecran.SWTPaPrixController;
import fr.legrain.articles.ecran.SWTPaUniteController;
import fr.legrain.articles.editor.ArticleMultiPageEditor;
import fr.legrain.articles.editor.EditorInputArticle;
import fr.legrain.articles.editor.EditorInputPrix;
import fr.legrain.articles.editor.EditorInputUnite;
import fr.legrain.articles.editor.EditorPrix;
import fr.legrain.articles.editor.EditorUnite;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaPrixServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaTvaServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaUniteServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaDevisServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLDevisServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTLigneServiceRemote;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.calcultva.controller.PaAideCalcTVAController;
import fr.legrain.calcultva.divers.ParamAideCalcTVA;
import fr.legrain.calcultva.divers.ResultAideCalcTVA;
import fr.legrain.calcultva.ecran.PaAideCalcTVASWT;
import fr.legrain.devis.DevisPlugin;
import fr.legrain.devis.divers.ParamAfficheLDevis;
import fr.legrain.devis.ecran.PaSaisieLigneDevis;
import fr.legrain.document.controller.MessagesEcran;
import fr.legrain.document.dto.ILigneDocumentDTO;
import fr.legrain.document.dto.TaLDevisDTO;
import fr.legrain.document.model.SWTDocument;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaLDevis;
import fr.legrain.document.events.SWTModificationDocumentEvent;
import fr.legrain.document.events.SWTModificationDocumentListener;
import fr.legrain.gestCom.Appli.Const;
//import fr.legrain.gestCom.Appli.LgrDozerMapper;
import fr.legrain.gestCom.Appli.ModelGeneralObjetEJB;
import fr.legrain.gestCom.Module_Document.IHMTotauxLignesDocument;
import fr.legrain.gestCom.gestComBd.gestComBdPlugin;
import fr.legrain.gestCom.librairiesEcran.LibrairiesEcranPlugin;
import fr.legrain.gestCom.librairiesEcran.editor.EditorAide;
import fr.legrain.gestCom.librairiesEcran.editor.EditorInputAide;
import fr.legrain.gestCom.librairiesEcran.swt.DeclencheCommandeControllerEvent;
import fr.legrain.gestCom.librairiesEcran.swt.EJBBaseControllerSWTStandard;
import fr.legrain.gestCom.librairiesEcran.swt.LgrAbstractHandler;
import fr.legrain.gestCom.librairiesEcran.swt.LgrShellUtil;
import fr.legrain.gestCom.librairiesEcran.swt.ParamAfficheAideRechercheSWT;
import fr.legrain.gestCom.librairiesEcran.swt.ParamAfficheAideSWT;
import fr.legrain.gestCom.librairiesEcran.swt.SWTPaAideControllerSWT;
import fr.legrain.gestCom.librairiesEcran.swt.SWTPaAideRechercheControllerSWT;
import fr.legrain.gestCom.librairiesEcran.workbench.ChangementDePageEvent;
import fr.legrain.gestCom.librairiesEcran.workbench.EJBLgrEditorPart;
import fr.legrain.gestCom.librairiesEcran.workbench.LgrPartListener;
import fr.legrain.gestionCommerciale.GestionCommercialePlugin;
import fr.legrain.lib.data.ContentProposalProvider;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.lib.data.InfosVerifSaisie;
import fr.legrain.lib.data.LibChaine;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.VerrouInterface;
import fr.legrain.lib.ejb.EJBLookup;
import fr.legrain.lib.gui.ParamAffiche;
import fr.legrain.lib.gui.ParamAfficheSWT;
import fr.legrain.lib.gui.ResultAffiche;
import fr.legrain.lib.gui.RetourEcranEvent;
import fr.legrain.lib.gui.RetourEcranListener;
import fr.legrain.lib.gui.aide.PaAideRechercheSWT;
import fr.legrain.lib.gui.aide.PaAideSWT;
import fr.legrain.lib.gui.aide.ParamAfficheAide;
import fr.legrain.lib.gui.grille.LgrTableViewer;
import fr.legrain.lib.gui.grille.LgrViewerSupport;
import fr.legrain.lib.validator.AbstractApplicationDAOClient;
import fr.legrain.libMessageLGR.LgrMess;
import fr.legrain.tiers.TiersPlugin;
import fr.legrain.tiers.clientutility.JNDILookupClass;

public class PaLigneDevisController extends EJBBaseControllerSWTStandard
implements RetourEcranListener, SWTModificationDocumentListener, ISelectionProvider {

	static Logger logger = Logger.getLogger(PaLigneDevisController.class.getName());
	
	public static final String C_COMMAND_LIGNE_FACTURE_AIDE_ID 			= "fr.legrain.ligneFacture.aide";
	public static final String C_COMMAND_LIGNE_FACTURE_ANNULER_ID 		= "fr.legrain.ligneFacture.annuler";
	public static final String C_COMMAND_LIGNE_FACTURE_ENREGISTRER_ID 	= "fr.legrain.ligneFacture.enregsitrer";
	public static final String C_COMMAND_LIGNE_FACTURE_FERMER_ID 		= "fr.legrain.ligneFacture.fermer";
	public static final String C_COMMAND_LIGNE_FACTURE_IMPRIMER_ID 		= "fr.legrain.ligneFacture.imprimer";
	public static final String C_COMMAND_LIGNE_FACTURE_INSERER_ID 		= "fr.legrain.ligneFacture.inserer";
	public static final String C_COMMAND_LIGNE_FACTURE_MODIFIER_ID 		= "fr.legrain.ligneFacture.modifier";
	public static final String C_COMMAND_LIGNE_FACTURE_SUPPRIMER_ID 	= "fr.legrain.ligneFacture.supprimer";
	public static final String C_COMMAND_LIGNE_FACTURE_REFRESH_ID 	    = "fr.legrain.ligneFacture.refresh";
	public static final String C_COMMAND_LIGNE_FACTURE_ENTETE_ENREGISTRER_ID 	= "fr.legrain.ligneFactureEntete.enregsitrer";
	protected ActionEnregistrerDevis actionEnregistrerDevis = new ActionEnregistrerDevis("Enregistrer Facture [Ctrl+F3]");
	protected HandlerEnregistrerDevis handlerEnregistrerDevis = new HandlerEnregistrerDevis();
	
	public List<TaArticle> listeArticles = null;
	public static final String C_COMMAND_LIGNE_FACTURE_AJOUT_ID 		= "fr.legrain.ligneFacture.ajout";
	protected ActionAjouterLigne actionAjouter = new ActionAjouterLigne("Ajouter [F6]");
	protected HandlerAjouter handlerAjouter = new HandlerAjouter();

	public static final String C_COMMAND_LIGNE_FACTURE_AIDETVA_ID 		= "fr.legrain.ligneFacture.aideTva";

	protected ActionAideCalcTVA actionAideTva = new ActionAideCalcTVA("Aide Tva [F8]");
	protected HandlerAideTva handlerAideTva = new HandlerAideTva();

	public static final String C_COMMAND_LIGNE_FACTURE_CHANGETVA_ID 		= "fr.legrain.ligneFacture.changeTva";

	protected HandlerChangeTva handlerChangeTva = new HandlerChangeTva();
	

	private PaSaisieLigneDevis vue = null;


	private ITaLDevisServiceRemote dao = null;//new TaLDevisDAO();
	
	private Object ecranAppelant = null;
	private TaLDevisDTO taLDevisDTO;
	private IHMTotauxLignesDocument ihmTotauxLignesDocument;
	private TaLDevisDTO ihmOldLDevis;
	private Realm realm;
	private DataBindingContext dbc;
	private DataBindingContext dbcTotaux = null;

//	private ModelLigneDevis modelLDevis /*=  new ModelLigneFacture(ibTaTable)*/;
	private Class classModel = TaLDevisDTO.class;
	private ModelGeneralObjetEJB<TaLDevis,TaLDevisDTO> modelLDocument /*= new ModelGeneralObjet<TaLDevisDTO,TaLDevisDAO,TaLDevis>(dao,classModel)*/;
	private LgrTableViewer tableViewer;
	private WritableList writableList;
	private IObservableValue selectedLigneDevis;
	private Object selectedTotauxLigneDocument;
	private String[] listeChamp;
	private String nomClassController = this.getClass().getSimpleName();
	private SelectionGrille select = new SelectionGrille();
 
	private ContentProposalAdapter articleContentProposal;
	
	private MapChangeListener changeListener = new MapChangeListener();
	public boolean forceAnnuler=false;
	public EJBBaseControllerSWTStandard parentEcran = null;
	public static IContextActivation iContext;
	public List<IContextActivation> listeContextEntete;
	
	protected Map<Control, String> mapComposantChampsTotauxLignes = null;
	
	private TaLDevis taLDevis = null;
	
	private TaDevis masterEntity = null;
	private ITaDevisServiceRemote masterDAO = null;
	
	String[] listeCodeArticles=null;
	String[] listeDescriptionArticles=null;

	private LgrDozerMapper<TaLDevisDTO,TaLDevis> mapperUIToModel = new LgrDozerMapper<TaLDevisDTO,TaLDevis>();
	private LgrDozerMapper<TaLDevis,TaLDevisDTO> mapperModelToUI = new LgrDozerMapper<TaLDevis,TaLDevisDTO>();
	
	private LgrDozerMapper<TaDevis,IHMTotauxLignesDocument> mapperModelToUITotauxLignes = new LgrDozerMapper<TaDevis,IHMTotauxLignesDocument>();

	public PaLigneDevisController(){
		
	}
	
	public PaLigneDevisController(final PaSaisieLigneDevis vue/*, SWTFacture facture, WorkbenchPart part*/) {
		this(vue,null);
	}
	
	public PaLigneDevisController(final PaSaisieLigneDevis vue/*, SWTFacture facture, WorkbenchPart part*/,EntityManager em) {
//		if(em!=null) {
//			setEm(em);
//		}
		
		try {
			dao = new EJBLookup<ITaLDevisServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_L_DEVIS_SERVICE, ITaLDevisServiceRemote.class.getName());
			
			setVue(vue);
			// a faire avant l'initialisation du Binding
			vue.getGrille().addSelectionListener(select);
			vue.getShell().addShellListener(this);

//			// Branchement action annuler : empêche la fermeture automatique de la
//			// fenetre sur ESC
			//vue.getShell().addTraverseListener(new Traverse());
			
			vue.getGrille().addMouseListener(new MouseAdapter(){
				int rang;
				int rangTmp;
				public void mouseDown(MouseEvent e) {
					rang = vue.getGrille().getSelectionIndex();					
				}

				public void mouseUp(MouseEvent e) {
					vue.getGrille().setSelection(vue.getGrille().getItem(new Point(e.x,e.y)));
					rangTmp = vue.getGrille().getSelectionIndex();					
					if(rang!=rangTmp){
						try {
							try {
								DeclencheCommandeControllerEvent decl = new DeclencheCommandeControllerEvent(this,C_COMMAND_GLOBAL_MODIFIER_ID);
								fireDeclencheCommandeController(decl);
							} catch (Exception e2) {
								logger.error("",e2);
							}		

							//passage ejb
//							masterEntity.getLigne(rang).setModeLigne(EnumModeObjet.C_MO_EDITION);
							masterEntity.deplaceLigne(masterEntity.getLigne(rang), rangTmp);
							//passage ejb
//							masterEntity.getLigne(rang).setModeLigne(EnumModeObjet.C_MO_CONSULTATION);
//							masterEntity.setModeDocument(EnumModeObjet.C_MO_EDITION);
							actRefresh();
						} catch (Exception e1) {
							logger.error("", e1);
						}
					}
				}				
			});
			
			initController();
		}
		catch (Exception e) {
			logger.debug(e);
		}
	}

	private void initController() {
		try {
			setGrille(vue.getGrille());
			setDaoStandard(dao);
			setTableViewerStandard(tableViewer);
			setDbcStandard(dbc);

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
			Menu[] tabPopups = new Menu[] { popupMenuFormulaire, popupMenuGrille };
			this.initPopupAndButtons(mapActions, tabPopups);
			vue.getPaSaisie().setMenu(popupMenuFormulaire);
			vue.getGrille().setMenu(popupMenuGrille);
			bind(vue);
			initEtatComposant();
			initEtatBouton(false);
			initTTC();
		} catch (Exception e) {
			//vue.getLaMessage().setText(e.getMessage());
			logger.error("Erreur : PaArticlesController", e);
		}
	}
	/**
	 * Creation des objet pour l'interface, a partir de l'entite principale.<br>
	 * Ici : creation d'une liste de ligne pour l'IHM, a partir de la liste des lignes contenue dans l'entite facture.
	 * @return
	 */
	public List<TaLDevisDTO> IHMmodel() {
		LinkedList<TaLDevis> ldao = new LinkedList<TaLDevis>();
		LinkedList<TaLDevisDTO> lswt = new LinkedList<TaLDevisDTO>();
		
		if(masterEntity!=null && !masterEntity.getLignes().isEmpty()) {

			ldao.addAll(masterEntity.getLignes());

			LgrDozerMapper<TaLDevis,TaLDevisDTO> mapper = new LgrDozerMapper<TaLDevis,TaLDevisDTO>();
			for (TaLDevis o : ldao) {
				TaLDevisDTO t = null;
				t = (TaLDevisDTO) mapper.map(o, TaLDevisDTO.class);
				lswt.add(t);
			}
			
		}
		return lswt;
	}


	public void bind(PaSaisieLigneDevis paArticleSWT) {
		try {
			if (modelLDocument==null)modelLDocument = new ModelGeneralObjetEJB<TaLDevis,TaLDevisDTO>(dao);
			if (realm==null)realm = SWTObservables.getRealm(vue.getParent().getDisplay());

			if (tableViewer==null){
				tableViewer = new LgrTableViewer(paArticleSWT.getGrille());
				tableViewer.createTableCol(paArticleSWT.getGrille(), nomClassController,
						Const.C_FICHIER_LISTE_CHAMP_GRILLE);
				listeChamp = tableViewer.setListeChampGrille(nomClassController,
						Const.C_FICHIER_LISTE_CHAMP_GRILLE);

//				ObservableListContentProvider viewerContent = new ObservableListContentProvider();
//				tableViewer.setContentProvider(viewerContent);
//
//				IObservableMap[] attributeMaps = BeansObservables.observeMaps(
//						viewerContent.getKnownElements(), classModel, listeChamp);
//
//				tableViewer.setLabelProvider(new ObservableMapLabelProvider(attributeMaps));
//				writableList = new WritableList(realm, IHMmodel(), classModel);
//				tableViewer.setInput(writableList);
				
				// Set up data binding.
				LgrViewerSupport.bind(tableViewer, 
						new WritableList(IHMmodel(), classModel),
						BeanProperties.values(listeChamp)
						);

				selectedLigneDevis = ViewersObservables.observeSingleSelection(tableViewer);
			}
			dbc = new DataBindingContext(realm);

			dbc.getValidationStatusMap().addMapChangeListener(changeListener);
			tableViewer.selectionGrille(0);

			setTableViewerStandard(tableViewer);
			setDbcStandard(dbc);

			bindingFormMaitreDetail(dbc, realm, selectedLigneDevis,classModel);
			
			tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
				public void selectionChanged(SelectionChangedEvent event) {
					if(masterEntity!=null) {
						masterEntity.setLigneCourante(vue.getGrille().getSelectionIndex());
						//#JPA a remettre ??
//						select.widgetSelected(null);
					}
				}
			});
			bindTotaux();
		} catch (Exception e) {
			//vue.getLaMessage().setText(e.getMessage());
			logger.error("", e);
		}
	}
	
	public void bindTotaux() {
		try {
			realm = SWTObservables.getRealm(vue.getParent().getDisplay());

			
			if(ihmTotauxLignesDocument==null) ihmTotauxLignesDocument = new IHMTotauxLignesDocument();
			if(masterEntity!=null)
				mapperModelToUITotauxLignes.map(masterEntity,ihmTotauxLignesDocument);

			selectedTotauxLigneDocument = ihmTotauxLignesDocument;
			dbcTotaux = new DataBindingContext(realm);
			bindingFormSimple(mapComposantChampsTotauxLignes, dbcTotaux, realm, selectedTotauxLigneDocument, IHMTotauxLignesDocument.class);

		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public Composite getVue() {
		return vue;
	}

	public ResultAffiche configPanel(ParamAffiche param) {
		if (param != null) {
//			ibTaTable.ouvreDataset();
			if (((ParamAfficheLDevis)param).getFocusSWT()!=null)
				((ParamAfficheLDevis)param).getFocusSWT().forceFocus();
			else ((ParamAfficheLDevis)param).setFocusSWT(vue.getTfCODE_ARTICLE());			
			if(param.getEcranAppelant()!=null) {
				ecranAppelant = param.getEcranAppelant();
			}

			param.setFocusSWT(getModeEcran().getFocusCourantSWT());

			if (param.getEcranAppelant() != null) {
				ecranAppelant = param.getEcranAppelant();
			}


		} else {
//			ibTaTable.getFIBQuery().close();
//			ibTaTable.getFIBQuery().setQuery(new QueryDescriptor(ibTaTable.getFIBBase(),ibTaTable.getRequete(),true));
//			ibTaTable.getFIBQuery().open();
//			ibTaTable.getParamSQL().remove(Const.C_ID_DEVIS);
//			ibTaTable.getParamSQL().put(Const.C_ID_DEVIS,new String[]{"=",String.valueOf(((SWTEnteteDevis)swtDevis.getEntete()).getID_DEVIS())});
//			//ibTaTable.getParamSQL().put("",new String[]{" order by ",Const.C_NUM_LIGNE_L_FACTURE});
//			ibTaTable.setParamWhereSQL(ibTaTable.getParamSQL());
//			ibTaTable.getFIBQuery().setRowId(ibTaTable.champIdTable, true);
//			ibTaTable.getFIBQuery().setSort(null);
		}		
		
		if(tableViewer==null) {
			//databinding pas encore realise
			bind(vue);
		} else {
			try {
				actRefresh();
				refreshTotauxLignes();
			} catch (Exception e) {
				logger.error("",e);
			}
		}
		
		if (param.getModeEcran() != null
				&& param.getModeEcran().compareTo(EnumModeObjet.C_MO_INSERTION) == 0) {
			try {
				insertionLigne(false,((ParamAfficheLDevis)param).getInitFocus());
				//actInserer(((ParamAfficheLFacture)param).getInitFocus());
			} catch (Exception e) {
				//vue.getLaMessage().setText(e.getMessage());
				logger.error("", e);
			}
		} else {
			initEtatBouton();
		}
		// permet de se positionner sur le 1er enregistrement et de remplir
		// le formulaire
		tableViewer.selectionGrille(0);
		tableViewer.tri(classModel, nomClassController,Const.C_FICHIER_LISTE_CHAMP_GRILLE);
		VerrouInterface.setVerrouille(false);
		setIhmOldLDevis();

		return null;
	}
	
	public void initTypeLigne() {
		ITaTLigneServiceRemote dao;
		try {
			dao = new EJBLookup<ITaTLigneServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_T_LIGNE_SERVICE, ITaTLigneServiceRemote.class.getName());
			taLDevis.setTaTLigne(dao.findByCode(SWTDocument.C_CODE_T_LIGNE_H));
		} catch (NamingException e) {
			logger.error("", e);
		} catch (FinderException e) {
			logger.error("", e);
		}
		
	}
	
	/**
	 * Mise a jour des totaux a l'ecran, en fonction du contenu de l'entite principale.
	 * Cette fonction ne declanche aucun calcul, elle utilise les valeurs telles qu'elles sont.
	 */
	private void refreshTotauxLignes() {
		if(masterEntity!=null && ihmTotauxLignesDocument!=null) {
			mapperModelToUITotauxLignes.map(masterEntity,ihmTotauxLignesDocument);
		}
	}
	
	protected void initVerifySaisie() {
		if (mapInfosVerifSaisie == null)
			mapInfosVerifSaisie = new HashMap<Control, InfosVerifSaisie>();
		
		mapInfosVerifSaisie.put(vue.getTfQTE_L_DEVIS(), new InfosVerifSaisie(new TaLDevis(),Const.C_QTE_L_DOCUMENT,new int[]{InfosVerifSaisie.CTRL_QUE_DES_CHIFFRES_POSITIFS}));
		mapInfosVerifSaisie.put(vue.getTfQTE2_L_DEVIS(), new InfosVerifSaisie(new TaLDevis(),Const.C_QTE2_L_DOCUMENT,new int[]{InfosVerifSaisie.CTRL_QUE_DES_CHIFFRES_POSITIFS}));
		mapInfosVerifSaisie.put(vue.getTfMT_HT_L_DEVIS(), new InfosVerifSaisie(new TaLDevis(),Const.C_MT_HT_L_DOCUMENT,new int[]{InfosVerifSaisie.CTRL_QUE_DES_CHIFFRES_POSITIFS}));
		mapInfosVerifSaisie.put(vue.getTfMT_TTC_L_DEVIS(), new InfosVerifSaisie(new TaLDevis(),Const.C_MT_TTC_L_DOCUMENT,new int[]{InfosVerifSaisie.CTRL_QUE_DES_CHIFFRES_POSITIFS}));
		mapInfosVerifSaisie.put(vue.getTfPRIX_U_L_DEVIS(), new InfosVerifSaisie(new TaLDevis(),Const.C_PRIX_U_L_DOCUMENT,new int[]{InfosVerifSaisie.CTRL_QUE_DES_CHIFFRES}));
		mapInfosVerifSaisie.put(vue.getTfREM_TX_L_DEVIS(), new InfosVerifSaisie(new TaLDevis(),Const.C_REM_TX_L_DOCUMENT,new int[]{InfosVerifSaisie.CTRL_QUE_DES_CHIFFRES_POSITIFS}));
		mapInfosVerifSaisie.put(vue.getTfNUM_LIGNE_L_DEVIS(), new InfosVerifSaisie(new TaLDevis(),Const.C_NUM_LIGNE_L_DOCUMENT,new int[]{InfosVerifSaisie.CTRL_QUE_DES_CHIFFRES_POSITIFS}));
		mapInfosVerifSaisie.put(vue.getTfCODE_ARTICLE(), new InfosVerifSaisie(new TaArticle(),Const.C_CODE_ARTICLE,null));
		mapInfosVerifSaisie.put(vue.getTfLIB_L_DEVIS(), new InfosVerifSaisie(new TaLDevis(),Const.C_LIB_L_DOCUMENT,null));
		mapInfosVerifSaisie.put(vue.getTfU1_L_DEVIS(), new InfosVerifSaisie(new TaLDevis(),Const.C_U1_L_DOCUMENT,null));
		mapInfosVerifSaisie.put(vue.getTfU2_L_DEVIS(), new InfosVerifSaisie(new TaLDevis(),Const.C_U1_L_DOCUMENT,null));
		
		initVerifyListener(mapInfosVerifSaisie, dao);
	}
	
	protected void initComposantsVue() throws ExceptLgr {
	}

	protected void initMapComposantChamps() {
		if (mapComposantChamps == null)
			mapComposantChamps = new LinkedHashMap<Control, String>();
		if (mapComposantChampsTotauxLignes == null)
			mapComposantChampsTotauxLignes = new LinkedHashMap<Control, String>();
		if (listeComposantFocusable == null)
			listeComposantFocusable = new ArrayList<Control>();
		listeComposantFocusable.add(vue.getGrille());
		
		mapComposantChampsTotauxLignes.put(vue.getTfMT_HT_CALC(),Const.C_MT_HT_CALC);
		mapComposantChampsTotauxLignes.put(vue.getTfMT_TTC_CALC(),Const.C_MT_TTC_AVANTREMISEGLOBALE_CALC);
		mapComposantChampsTotauxLignes.put(vue.getTfNET_HT_CALC(),Const.C_NET_HT_CALC);
		mapComposantChampsTotauxLignes.put(vue.getTfNET_TTC_CALC(),Const.C_NET_TTC_CALC);
		mapComposantChampsTotauxLignes.put(vue.getTfNET_TVA_CALC(),Const.C_NET_TVA_CALC);
		
		mapComposantChamps.put(vue.getTfCODE_ARTICLE(), Const.C_CODE_ARTICLE);
		mapComposantChamps.put(vue.getTfLIB_L_DEVIS(), Const.C_LIB_L_DOCUMENT);
		mapComposantChamps.put(vue.getTfQTE_L_DEVIS(), Const.C_QTE_L_DOCUMENT);
		mapComposantChamps.put(vue.getTfU1_L_DEVIS(), Const.C_U1_L_DOCUMENT);
		mapComposantChamps.put(vue.getTfQTE2_L_DEVIS(), Const.C_QTE2_L_DOCUMENT);
		mapComposantChamps.put(vue.getTfU2_L_DEVIS(), Const.C_U2_L_DOCUMENT);
		mapComposantChamps.put(vue.getTfPRIX_U_L_DEVIS(), Const.C_PRIX_U_L_DOCUMENT);
		mapComposantChamps.put(vue.getTfREM_TX_L_DEVIS(), Const.C_REM_TX_L_DOCUMENT);
		mapComposantChamps.put(vue.getTfMT_HT_L_DEVIS(), Const.C_MT_HT_L_DOCUMENT);
		mapComposantChamps.put(vue.getTfMT_TTC_L_DEVIS(), Const.C_MT_TTC_L_DOCUMENT);

		vue.getTfCODE_ARTICLE().addFocusListener(new org.eclipse.swt.events.FocusAdapter(){
			public void focusGained(FocusEvent e) {
//				String[][] TabArticles = initAdapterArticle();
//				String[] listeCodeArticles=null;
//				String[] listeDescriptionArticles=null;
//				if(TabArticles!=null){
//					listeCodeArticles=new String[TabArticles.length];
//					listeDescriptionArticles=new String[TabArticles.length];
//					for (int i=0; i<TabArticles.length; i++) {
//						listeCodeArticles [i]=TabArticles[i][0];
//						listeDescriptionArticles [i]=TabArticles[i][1];
//					}
//				}
				articleContentProposal.setContentProposalProvider(
						contentProposalProviderArticles());
			}
		});
		
		for (Control c : mapComposantChamps.keySet()) {
			c.setToolTipText(mapComposantChamps.get(c));
		}

		for (Control c : mapComposantChamps.keySet()) {
			listeComposantFocusable.add(c);
		}
		
		mapComposantChamps.put(vue.getTfNUM_LIGNE_L_DEVIS(), Const.C_NUM_LIGNE_L_DOCUMENT);
		
		
		
		listeComposantFocusable.add(vue.getPaBtnAvecAssistant().getPaBtn().getBtnEnregistrer());
		listeComposantFocusable.add(vue.getPaBtnAvecAssistant().getPaBtnAssitant().getBtnSuivant());
		listeComposantFocusable.add(vue.getPaBtnAvecAssistant().getPaBtn().getBtnInserer());
		listeComposantFocusable.add(vue.getPaBtnAvecAssistant().getPaBtn().getBtnModifier());
		listeComposantFocusable.add(vue.getPaBtnAvecAssistant().getPaBtn().getBtnSupprimer());
		listeComposantFocusable.add(vue.getPaBtnAvecAssistant().getPaBtn().getBtnFermer());
		listeComposantFocusable.add(vue.getPaBtnAvecAssistant().getPaBtn().getBtnAnnuler());
		listeComposantFocusable.add(vue.getPaBtnAvecAssistant().getPaBtn().getBtnImprimer());
		
		listeComposantFocusable.add(vue.getPaBtnAvecAssistant().getPaBtnAssitant().getBtnPrecedent());
		class TraverseDown implements org.eclipse.swt.events.KeyListener {

			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.ARROW_DOWN) {
					try {
					actDown();
					} catch (Exception e1) {
					logger.error("", e1);
				}
			}
				
			}

			public void keyReleased(KeyEvent e) {
				// TODO Raccord de méthode auto-généré
				
			}

		};
		
		class  TraverseUp implements org.eclipse.swt.events.KeyListener {

			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.ARROW_UP ) {
					try {
						actUp();
					} catch (Exception e1) {
						logger.error("", e1);
					}

				}
			}

			public void keyReleased(KeyEvent e) {
		}
		};
		
		for (Control element : mapComposantChamps.keySet()) {
			element.addKeyListener(new TraverseUp());
		}
		for (Control element : mapComposantChamps.keySet()) {
			element.addKeyListener(new TraverseDown());
		}
		vue.getGrille().addKeyListener(new TraverseUp());
		vue.getGrille().addKeyListener(new TraverseDown());
		
		if (mapInitFocus == null)
			mapInitFocus = new LinkedHashMap<EnumModeObjet, Control>();
		mapInitFocus.put(EnumModeObjet.C_MO_INSERTION, vue.getTfCODE_ARTICLE());
		mapInitFocus.put(EnumModeObjet.C_MO_EDITION, vue.getTfCODE_ARTICLE());
		mapInitFocus.put(EnumModeObjet.C_MO_CONSULTATION, vue.getTfCODE_ARTICLE());


		activeModifytListener();
		
		vue.getTfQTE_L_DEVIS().addVerifyListener(queDesChiffresPositifs);
		vue.getTfQTE2_L_DEVIS().addVerifyListener(queDesChiffresPositifs);
		vue.getTfMT_HT_L_DEVIS().addVerifyListener(queDesChiffresPositifs);
		vue.getTfMT_TTC_L_DEVIS().addVerifyListener(queDesChiffresPositifs);
		vue.getTfPRIX_U_L_DEVIS().addVerifyListener(queDesChiffres);
		vue.getTfREM_TX_L_DEVIS().addVerifyListener(queDesChiffresPositifs);
		vue.getTfNUM_LIGNE_L_DEVIS().addVerifyListener(queDesChiffresPositifs);
		
		KeyStroke keyStroke;
		try {
			keyStroke = KeyStroke.getInstance("Ctrl+Space");

//			String[][] TabArticles = initAdapterArticle();
//			if(TabArticles!=null){
//				listeCodeArticles=new String[TabArticles.length];
//				listeDescriptionArticles=new String[TabArticles.length];
//				for (int i=0; i<TabArticles.length; i++) {
//					listeCodeArticles [i]=TabArticles[i][0];
//					listeDescriptionArticles [i]=TabArticles[i][1];
//				}
//			}
			
			articleContentProposal = new ContentProposalAdapter(
					vue.getTfCODE_ARTICLE(), new TextContentAdapter(), 
					contentProposalProviderArticles(),keyStroke, null);
			articleContentProposal.setFilterStyle(ContentProposalAdapter.FILTER_NONE);
			articleContentProposal.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
			articleContentProposal.addContentProposalListener(new IContentProposalListener2(){

				public void proposalPopupClosed(ContentProposalAdapter adapter) {
					articleContentProposal.setEnabled(true);					
				}

				public void proposalPopupOpened(ContentProposalAdapter adapter) {
					if(masterEntity.getLigne(masterEntity.getLigneCourante())==null ) {
						if(!getModeEcran().dataSetEnModif())articleContentProposal.setEnabled(false);
					}else
					if(!getModeEcran().dataSetEnModif()&& taLDevis.ligneEstVide()  )
						articleContentProposal.setEnabled(false);
					articleContentProposal.setEnabled(true);
				}
					
				
			});
		} catch (ParseException e1) {
			logger.error("",e1);
		}

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
		mapCommand.put(C_COMMAND_GLOBAL_IMPRIMER_ID, handlerImprimer);
		mapCommand.put(C_COMMAND_LIGNE_FACTURE_CHANGETVA_ID, handlerChangeTva);
		

		mapCommand.put(C_COMMAND_GLOBAL_PRECEDENT_ID, handlerPrecedent);
		mapCommand.put(C_COMMAND_GLOBAL_SUIVANT_ID, handlerSuivant);
		
		initFocusCommand(String.valueOf(this.hashCode()),listeComposantFocusable,mapCommand);
		
		if (mapActions == null)
			mapActions = new LinkedHashMap<Button, Object>();
		
		mapActions.put(vue.getPaBtnAvecAssistant().getPaBtn().getBtnAnnuler(), C_COMMAND_GLOBAL_ANNULER_ID);
		mapActions.put(vue.getPaBtnAvecAssistant().getPaBtn().getBtnEnregistrer(), C_COMMAND_GLOBAL_ENREGISTRER_ID);
		mapActions.put(vue.getPaBtnAvecAssistant().getPaBtn().getBtnInserer(), C_COMMAND_GLOBAL_INSERER_ID);
		mapActions.put(vue.getPaBtnAvecAssistant().getPaBtn().getBtnModifier(), C_COMMAND_GLOBAL_MODIFIER_ID);
		mapActions.put(vue.getPaBtnAvecAssistant().getPaBtn().getBtnSupprimer(), C_COMMAND_GLOBAL_SUPPRIMER_ID);
		mapActions.put(vue.getPaBtnAvecAssistant().getPaBtn().getBtnFermer(), C_COMMAND_GLOBAL_FERMER_ID);
		mapActions.put(vue.getPaBtnAvecAssistant().getPaBtn().getBtnImprimer(), C_COMMAND_GLOBAL_IMPRIMER_ID);
		
		mapActions.put(vue.getPaBtnAvecAssistant().getPaBtnAssitant().getBtnPrecedent(), C_COMMAND_GLOBAL_PRECEDENT_ID);
		mapActions.put(vue.getPaBtnAvecAssistant().getPaBtnAssitant().getBtnSuivant(), C_COMMAND_GLOBAL_SUIVANT_ID);

		Object[] tabActionsAutres = new Object[] { C_COMMAND_GLOBAL_AIDE_ID, C_COMMAND_GLOBAL_REFRESH_ID ,C_COMMAND_LIGNE_FACTURE_CHANGETVA_ID};
		mapActions.put(null, tabActionsAutres);
	}	
	
	@Override
	protected void initImageBouton() {
		vue.getPaBtnAvecAssistant().getPaBtn().getBtnAnnuler().setImage(
				LibrairiesEcranPlugin.ir.get(LibrairiesEcranPlugin.IMAGE_ANNULER));
		vue.getPaBtnAvecAssistant().getPaBtn().getBtnEnregistrer().setImage(
				LibrairiesEcranPlugin.ir.get(LibrairiesEcranPlugin.IMAGE_ENREGISTRER));
		vue.getPaBtnAvecAssistant().getPaBtn().getBtnFermer().setImage(
				LibrairiesEcranPlugin.ir.get(LibrairiesEcranPlugin.IMAGE_FERMER));
		vue.getPaBtnAvecAssistant().getPaBtn().getBtnInserer().setImage(
				LibrairiesEcranPlugin.ir.get(LibrairiesEcranPlugin.IMAGE_INSERER));
		vue.getPaBtnAvecAssistant().getPaBtn().getBtnModifier().setImage(
				LibrairiesEcranPlugin.ir.get(LibrairiesEcranPlugin.IMAGE_MODIFIER));
		vue.getPaBtnAvecAssistant().getPaBtn().getBtnSupprimer().setImage(
				LibrairiesEcranPlugin.ir.get(LibrairiesEcranPlugin.IMAGE_SUPPRIMER));
		vue.getPaBtnAvecAssistant().getPaBtn().getBtnImprimer().setImage(
				LibrairiesEcranPlugin.ir.get(LibrairiesEcranPlugin.IMAGE_IMPRIMER));
		vue.layout(true);
	}
	
	public void initWorkenchPartCommands(WorkbenchPart part){
		if(handlerService == null)
			handlerService = (IHandlerService)PlatformUI.getWorkbench().getService(IHandlerService.class);
		if(contextService == null)
			contextService = (IContextService) PlatformUI.getWorkbench().getService(IContextService.class);
		activePartExpression = new ActivePartExpression(part);

		handlerService.activateHandler(C_COMMAND_LIGNE_FACTURE_AIDE_ID, handlerAide,activePartExpression);
		handlerService.activateHandler(C_COMMAND_LIGNE_FACTURE_ANNULER_ID, handlerAnnuler,activePartExpression);
		handlerService.activateHandler(C_COMMAND_LIGNE_FACTURE_ENREGISTRER_ID, handlerEnregistrer,activePartExpression);
		handlerService.activateHandler(C_COMMAND_LIGNE_FACTURE_FERMER_ID, handlerFermer,activePartExpression);
		handlerService.activateHandler(C_COMMAND_LIGNE_FACTURE_IMPRIMER_ID, handlerImprimer,activePartExpression);
		handlerService.activateHandler(C_COMMAND_LIGNE_FACTURE_INSERER_ID, handlerInserer,activePartExpression);
		handlerService.activateHandler(C_COMMAND_LIGNE_FACTURE_AJOUT_ID, handlerAjouter,activePartExpression);
		handlerService.activateHandler(C_COMMAND_LIGNE_FACTURE_MODIFIER_ID, handlerModifier,activePartExpression);
		handlerService.activateHandler(C_COMMAND_LIGNE_FACTURE_SUPPRIMER_ID, handlerSupprimer,activePartExpression);	
		handlerService.activateHandler(C_COMMAND_LIGNE_FACTURE_REFRESH_ID, handlerRefresh,activePartExpression);
		handlerService.activateHandler(C_COMMAND_LIGNE_FACTURE_AIDETVA_ID, handlerAideTva,activePartExpression);
		handlerService.activateHandler(C_COMMAND_LIGNE_FACTURE_ENTETE_ENREGISTRER_ID, handlerEnregistrerDevis,activePartExpression);
			
	}

	public PaLigneDevisController getThis() {
		return this;
	}

	@Override
	public boolean onClose() throws ExceptLgr {
		return false;
	}

	public void retourEcran(final RetourEcranEvent evt) {
		try {
			if (evt.getRetour() != null && (evt.getSource() instanceof SWTPaAideControllerSWT)) {
				if (getFocusAvantAideSWT() instanceof Text) {
					try {
						((Text) getFocusAvantAideSWT()).setText(((ResultAffiche) evt
								.getRetour()).getResult());	
						if(getFocusAvantAideSWT().equals(vue.getTfCODE_ARTICLE())||getFocusAvantAideSWT().equals(vue.getTfLIB_L_DEVIS())){
							TaArticle f = null;
							ITaArticleServiceRemote t = new EJBLookup<ITaArticleServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_ARTICLE_SERVICE, ITaArticleServiceRemote.class.getName(),false);
							f = t.findById(Integer.parseInt(((ResultAffiche) evt.getRetour()).getIdResult()));
							t = null;
							taLDevis.setLegrain(true);
							taLDevis.setTaArticle(f);
							initTypeLigne(); //=> passage ejb
						}
						if(getFocusAvantAideSWT().equals(vue.getTfU1_L_DEVIS())
								||getFocusAvantAideSWT().equals(vue.getTfU2_L_DEVIS())){
							TaUnite f = null;
							ITaUniteServiceRemote t = new EJBLookup<ITaUniteServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_UNITE_SERVICE, ITaUniteServiceRemote.class.getName());
							f = t.findById(Integer.parseInt(((ResultAffiche) evt.getRetour()).getIdResult()));
							t = null;
							if(getFocusAvantAideSWT().equals(vue.getTfU1_L_DEVIS()))
								taLDevis.setU1LDocument(f.getCodeUnite());
							else
								taLDevis.setU2LDocument(f.getCodeUnite());
						}					
						if(getFocusAvantAideSWT().equals(vue.getTfPRIX_U_L_DEVIS())){
							TaPrix f = null;
							ITaPrixServiceRemote t = new EJBLookup<ITaPrixServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_PRIX_SERVICE, ITaPrixServiceRemote.class.getName());
							f = t.findById(Integer.parseInt(((ResultAffiche) evt.getRetour()).getIdResult()));
							t = null;
							taLDevis.setLegrain(true);
							if(masterEntity.getTtc()==1)
								taLDevis.setPrixULDocument(f.getPrixttcPrix());
							else
								taLDevis.setPrixULDocument(f.getPrixPrix());
						}					
						ctrlUnChampsSWT(getFocusAvantAideSWT());
					} catch (Exception e) {
						logger.error("",e);
					}
					String[][] TabArticles = initAdapterArticle();
					if(TabArticles!=null){
					String[] listeCodeArticles=new String[TabArticles.length];
					String[] listeDescriptionArticles=new String[TabArticles.length];
					for (int i=0; i<TabArticles.length; i++) {
						listeCodeArticles [i]=TabArticles[i][0];
						listeDescriptionArticles [i]=TabArticles[i][1];
					}
					articleContentProposal.setContentProposalProvider(new ContentProposalProvider(listeCodeArticles,listeDescriptionArticles));
					}
				}
			} else if(evt.getRetour()!=null && (evt.getSource() instanceof PaAideCalcTVAController)) { 
				if(this.getFocusAvantAideSWT() instanceof Text && this.getFocusAvantAideSWT()==vue.getTfPRIX_U_L_DEVIS()){
					if(masterEntity.getTtc()==0)
						((Text)this.getFocusAvantAideSWT()).setText(((ResultAideCalcTVA)evt.getRetour()).getMtHT());
					else
						((Text)this.getFocusAvantAideSWT()).setText(((ResultAideCalcTVA)evt.getRetour()).getMtTTC());
					try {
						ctrlUnChampsSWT(this.getFocusAvantAideSWT())	;
					} catch (Exception e) {
						logger.error("",e);
					}
				}
			} else if(evt.getRetour()!=null){
				if (getFocusAvantAideSWT() instanceof Table) {
					if (getFocusAvantAideSWT() == vue.getGrille()) {
//						ibTaTable.lgrLocateID(((ResultAffiche) evt.getRetour()).getIdResult());
					}
				}
			}
			//super.retourEcran(evt);
		}finally{
			//ne pas enlever car sur demande de l'aide, je rends enable false tous les boutons
			//donc au retour de l'aide, je reinitialise les boutons suivant l'état du dataset
			//activeWorkenchPartCommands(true);
			initEtatBouton(false);
			if(parentEcran!=null) {
				//((SWTPaEditorFactureController)parentEcran).activeWorkenchPartCommands(true);
				((PaEditorDevisController)parentEcran).initEtatBouton(false);
			}
		}
		}


	protected void actAideTva() throws Exception{
//		org.eclipse.ui.PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable(){
		
//		public void run() {
		if (getFocusCourantSWT()==vue.getTfPRIX_U_L_DEVIS()){
			try {
				Shell s = new Shell(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),LgrShellUtil.styleLgr);
				PaAideCalcTVASWT paAideCalcTVA = new PaAideCalcTVASWT(s,SWT.NONE);
				PaAideCalcTVAController paAideCalcTVAController = new PaAideCalcTVAController(paAideCalcTVA);
				ParamAideCalcTVA paramAideCalcTVA= new ParamAideCalcTVA();
				
				if(!LibChaine.empty(vue.getTfCODE_ARTICLE().getText())&& masterEntity.getLigne(masterEntity.getLigneCourante())!=null) {
					ITaArticleServiceRemote taArticleDAO = new EJBLookup<ITaArticleServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_ARTICLE_SERVICE, ITaArticleServiceRemote.class.getName(),false);
					TaArticle taArticle = taArticleDAO.findById(((TaLDevis)masterEntity.getLigne(masterEntity.getLigneCourante())).getTaArticle().getIdArticle());
					if(taArticle.getTaTva().getTauxTva()!=null)
						paramAideCalcTVA.setTaux(LibConversion.bigDecimalToString(taArticle.getTaTva().getTauxTva()));
					else
						paramAideCalcTVA.setTaux("0.0");
				}
				if (masterEntity.getTtc()==0){
					paramAideCalcTVA.setFocusSWT(paAideCalcTVA.getTfMtTTC());
					paramAideCalcTVA.setMtHT(vue.getTfPRIX_U_L_DEVIS().getText());
				}
				else{
					paramAideCalcTVA.setFocusSWT(paAideCalcTVA.getTfMtHT());
					paramAideCalcTVA.setMtTTC(vue.getTfPRIX_U_L_DEVIS().getText());
				}
				paramAideCalcTVA.setEcranAppelant(this);
				paAideCalcTVAController.addRetourEcranListener(getThis());
				
				ParamAfficheSWT paramAfficheSWT = new ParamAfficheSWT();
				paramAfficheSWT.setHauteur(180);
				paramAfficheSWT.setLargeur(260);
				paramAfficheSWT.setTitre("Aide au calcul de la TVA.");
				LgrShellUtil.afficheAideSWT(paramAideCalcTVA, paramAfficheSWT, paAideCalcTVA,paAideCalcTVAController, s);
				if(paramAideCalcTVA.getFocusSWT()!=null)
					paramAideCalcTVA.getFocusSWT().forceFocus();
			} catch (Exception e1) {
				logger.error("Erreur : actionPerformed", e1);
			}
		 }
//	}
//	);
	};
	
	protected class HandlerAideTva extends LgrAbstractHandler {

		public Object execute(ExecutionEvent event) throws ExecutionException{
			try {
				actAideTva();
			} catch (Exception e) {
				logger.error("",e);
			}
			return event;
		}
	}
	protected class HandlerChangeTva extends LgrAbstractHandler {

		public Object execute(ExecutionEvent event) throws ExecutionException{
			try {
				actChangeTva();
			} catch (Exception e) {
				logger.error("",e);
			}
			return event;
		}
	}
	
	protected void actChangeTva() throws Exception{
		if(!LibChaine.empty(((ILigneDocumentDTO)selectedLigneDevis.getValue()).getCodeArticle())
				&& masterEntity.getLigne(masterEntity.getLigneCourante())!=null) {
			InputDialog input = new InputDialog(vue.getShell()
					,"Code Tva","Indiquer le code tva que vous souhaitez affecter à la ligne.",
					((TaLDevisDTO)selectedLigneDevis.getValue()).getCodeTvaLDocument(),new IInputValidator() {

				@Override
				public String isValid(String newText) {
					// TODO Auto-generated method stub
					modifMode();
					try{
						ITaTvaServiceRemote daoTva = new EJBLookup<ITaTvaServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_TVA_SERVICE, ITaTvaServiceRemote.class.getName());
						newText=newText.toUpperCase();
					
						if(daoTva.findByCode(newText)!=null)return null;
					}catch (Exception e) {
						logger.error("", e);
						return "Ce code n'est pas valide.";
					}
					return null;
				}
			});
			modifMode();
			if(getMasterModeEcran().dataSetEnModif()){
				input.open();
				if(input.getValue()!=null ){
					if(MessageDialog.openConfirm(vue.getShell()
							,"Modification du code Tva","Etes-vous sûr de vouloir modifier le code Tva de cette ligne ?")){
						String retour =input.getValue().toUpperCase();
						((TaLDevisDTO)selectedLigneDevis.getValue()).setCodeTvaLDocument(retour);
						if(input.getValue().equals("")){
							taLDevis.setCodeTvaLDocument("");
							taLDevis.setTauxTvaLDocument(BigDecimal.valueOf(0));
						}else{
							ITaTvaServiceRemote daoTva = new EJBLookup<ITaTvaServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_TVA_SERVICE, ITaTvaServiceRemote.class.getName());
							taLDevis.setCodeTvaLDocument(retour);
							taLDevis.setTauxTvaLDocument(daoTva.findByCode(retour).getTauxTva());
						}
						//passage ejb
						//masterEntity.calculTvaTotal();
						masterDAO.calculTvaTotal(masterEntity);
					}
				}
			}
		}

	}
	
	@Override
	protected void actInserer() throws Exception {
		insertionLigne(false,true);
	}

	@Override
	protected void actModifier() throws Exception {
		try {
			boolean continuer=true;
			setIhmOldLDevis();
//			if (LgrMess.isDOSSIER_EN_RESEAU()){
				DeclencheCommandeControllerEvent e = new DeclencheCommandeControllerEvent(this,C_COMMAND_GLOBAL_MODIFIER_ID);
				fireDeclencheCommandeController(e);
				continuer=getMasterModeEcran().dataSetEnModif();
//			}
			if(continuer){
				masterDAO.verifAutoriseModification(masterEntity);
				modeEcran.setMode(EnumModeObjet.C_MO_EDITION);//ejb
				for (TaLDevis p : masterEntity.getLignes()) {
					if(p.getNumLigneLDocument().equals(((TaLDevisDTO) selectedLigneDevis.getValue()).getNumLigneLDocument())) {
						taLDevis = p;
						taLDevis.setLegrain(true);
						taLDevisDTO= taLDevisDTO.copy((TaLDevisDTO) selectedLigneDevis.getValue());
					}				
				}
				initEtatBouton(false);
			}
		} catch (Exception e1) {
			//vue.getLaMessage().setText(e1.getMessage());
			logger.error("Erreur : actionPerformed", e1);
		}
	}

	@Override
	protected void actSupprimer() throws Exception {
		boolean verrouLocal=VerrouInterface.isVerrouille();
		VerrouInterface.setVerrouille(true);
		try {
			if (LgrMess.isDOSSIER_EN_RESEAU())masterDAO.verifAutoriseModification(masterEntity);

			try {
				DeclencheCommandeControllerEvent e = new DeclencheCommandeControllerEvent(this,C_COMMAND_GLOBAL_MODIFIER_ID);
				fireDeclencheCommandeController(e);
			} catch (Exception e) {
				logger.error("",e);
			}
			if(getMasterModeEcran().dataSetEnModif()){
			for (TaLDevis p : masterEntity.getLignes()) {
				if(p.getNumLigneLDocument().equals(((TaLDevisDTO) selectedLigneDevis.getValue()).getNumLigneLDocument())) {
					taLDevis = p;
				}				
			}	
//				dao.getModeObjet().setMode(EnumModeObjet.C_MO_SUPPRESSION);
				dao.supprimer(taLDevis);
				taLDevis.setTaDocument(null);
				masterEntity.removeLigne(taLDevis);
				taLDevis=null;
				actRefresh();
				refreshTotauxLignes();
				initEtatBouton();
				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			}
		} catch (ExceptLgr e1) {
			//vue.getLaMessage().setText(e1.getMessage());
			logger.error("Erreur : actionPerformed", e1);
		} finally {
			initEtatBouton();
			VerrouInterface.setVerrouille(verrouLocal);
		}
	}


	@Override
	protected void actFermer() throws Exception {
		// (controles de sortie et fermeture de la fenetre) => onClose()
		if(onClose())
			closeWorkbenchPart();
	}

	@Override
	

	protected void actAnnuler() throws Exception {
		// // verifier si l'objet est en modification ou en consultation
		// // si modification ou insertion, alors demander si annulation des
		// // modifications si ok, alors annulation si pas ok, alors arreter le
		// processus d'annulation
		// // si consultation declencher l'action "fermer".
		if((focusDansLigne())||forceAnnuler){
			forceAnnuler=false;
		boolean repondu = true;
		boolean message =false; 
		boolean verrouLocal = VerrouInterface.isVerrouille();
		try {
			VerrouInterface.setVerrouille(true);
			//int lignesupprimee = masterEntity.getLigneCourante();
			switch (getModeEcran().getMode()) {
			case C_MO_INSERTION:
				message =!taLDevis.ligneEstVide();
				if(message)repondu=MessageDialog.openQuestion(vue.getShell(), MessagesEcran
						.getString("Message.Attention"), MessagesEcran.getString("Ligne.Message.Annuler"));
				if(repondu){
					if(selectedLigneDevis!=null && selectedLigneDevis.getValue() != null
							//&& ((IHMLFacture) selectedLigneFacture.getValue()).getID_ARTICLE()!=null
							){

						masterEntity.removeLigne(taLDevis);
						actRefresh();
					}
					getModeEcran().setMode(EnumModeObjet.C_MO_CONSULTATION);//ejb
					hideDecoratedFields();
				}
				initEtatBouton(focusDansLigne());
				break;
			case C_MO_EDITION:
				message =!taLDevis.ligneEstVide();
				if(message)repondu=MessageDialog.openQuestion(vue.getShell(), MessagesEcran
						.getString("Message.Attention"), MessagesEcran
						.getString("Ligne.Message.Annuler"));
				if(repondu){
					int rang =((WritableList)tableViewer.getInput()).indexOf(selectedLigneDevis.getValue());
					List<TaLDevisDTO> l = IHMmodel();
					if(rang!=-1)
					  l.set(rang, ihmOldLDevis);
					
					writableList = new WritableList(realm, l, classModel);
					tableViewer.setInput(writableList);
					tableViewer.refresh();
					tableViewer.setSelection(new StructuredSelection(ihmOldLDevis), true);
					
					mapperUIToModel.map(ihmOldLDevis,taLDevis);
//					ctrlTousLesChampsAvantEnregistrementSWT();//si on contrôle tous les champs
					//il réinitialise l'article entièrement

					getModeEcran().setMode(EnumModeObjet.C_MO_CONSULTATION);//ejb
					hideDecoratedFields();
				}
				initEtatBouton(focusDansLigne());

				break;
			case C_MO_CONSULTATION:
				if(vue.getTfCODE_ARTICLE().isFocusControl())
					fireChangementDePage(new ChangementDePageEvent(this,ChangementDePageEvent.PRECEDENT));
				break;
			default:
				break;
			}
		} finally {
			initEtatBouton(focusDansLigne());
			VerrouInterface.setVerrouille(verrouLocal);
		}
		}
	}

	@Override
	protected void actImprimer() throws Exception {
		// // TODO procedure d'impression
		// JOptionPane.showMessageDialog(vue, Const.C_A_IMPLEMENTER,
		// MessagesEcran.getString("Message.Attention"),
		// JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
		initFocusSWT(getModeEcran(), mapInitFocus);
	}
	
	@Override
	protected boolean aideDisponible() {
		boolean result = false;
		switch (getModeEcran().getMode()) {
		case C_MO_CONSULTATION:
		case C_MO_EDITION:
		case C_MO_INSERTION:
			if (getFocusCourantSWT()==vue.getTfCODE_ARTICLE()
					||getFocusCourantSWT()==vue.getTfLIB_L_DEVIS()
					|| getFocusCourantSWT()==vue.getTfU1_L_DEVIS() 
					|| getFocusCourantSWT()==vue.getTfU2_L_DEVIS() 
					|| getFocusCourantSWT()==vue.getTfPRIX_U_L_DEVIS())
				result = true;
			break;
		default:
			break;
		}

		return result;
	}
	
	@Override
	protected void actPrecedent() throws Exception {
		if(taLDevis!=null && taLDevis.ligneEstVide()){
			vue.getPaBtnAvecAssistant().getPaBtnAssitant().getBtnPrecedent().forceFocus();
			actAnnuler();			
		}
		else
			actEnregistrer(false);
		ChangementDePageEvent evt = new ChangementDePageEvent(this,ChangementDePageEvent.PRECEDENT);
		fireChangementDePage(evt);
	}

	@Override
	protected void actSuivant() throws Exception {
		if(taLDevis!=null && taLDevis.ligneEstVide()){
			vue.getPaBtnAvecAssistant().getPaBtnAssitant().getBtnPrecedent().forceFocus();
			actAnnuler();			
		}
		else
			actEnregistrer(false);
		ChangementDePageEvent evt = new ChangementDePageEvent(this,ChangementDePageEvent.SUIVANT);
		fireChangementDePage(evt);
	}

	@Override
	protected void actAide() throws Exception {
		actAide(null);
	}

	@Override
	protected void actAide(String message) throws Exception {
		if(aideDisponible()) {		
			setActiveAide(true);
			boolean verrouLocal=VerrouInterface.isVerrouille();
			VerrouInterface.setVerrouille(true);
			try {
				vue.setCursor(Display.getCurrent().getSystemCursor(SWT.CURSOR_WAIT));
				ParamAfficheAideRechercheSWT paramAfficheAideRecherche = new ParamAfficheAideRechercheSWT();
//				paramAfficheAideRecherche.setDb(getThis().getIbTaTable().getFIBBase());
				paramAfficheAideRecherche.setMessage(message);
				// Creation de l'ecran d'aide principal
				Shell s = new Shell(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),LgrShellUtil.styleLgr);
				PaAideSWT paAide = new PaAideSWT(s, SWT.NONE);
				SWTPaAideControllerSWT paAideController = new SWTPaAideControllerSWT(paAide);
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

				switch ((PaLigneDevisController.this.getModeEcran().getMode())) {
				case C_MO_CONSULTATION:

				case C_MO_EDITION:
				case C_MO_INSERTION:
					if (getFocusCourantSWT()==vue.getTfCODE_ARTICLE()){
						PaArticleSWT paArticleSWT = new PaArticleSWT(s2,SWT.NULL);
						SWTPaArticlesController swtPaArticlesController = new SWTPaArticlesController(paArticleSWT);
						paramAfficheAideRecherche.setForceAffichageAideRemplie(false);

						editorCreationId = ArticleMultiPageEditor.ID_EDITOR;
						editorInputCreation = new EditorInputArticle();

						ParamAfficheArticles paramAfficheArticles = new ParamAfficheArticles();
						ITaArticleServiceRemote dao = new EJBLookup<ITaArticleServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_ARTICLE_SERVICE, ITaArticleServiceRemote.class.getName(),false);
						paramAfficheAideRecherche.setJPQLQuery(dao.getJPQLQuery());
						paramAfficheArticles.setModeEcran(EnumModeObjet.C_MO_INSERTION);
						paramAfficheArticles.setEcranAppelant(paAideController);
						controllerEcranCreation = swtPaArticlesController;
						parametreEcranCreation = paramAfficheArticles;

						paramAfficheAideRecherche.setTypeEntite(TaArticle.class);
						paramAfficheAideRecherche.setChampsRecherche(Const.C_CODE_ARTICLE);
						paramAfficheAideRecherche.setDebutRecherche(vue.getTfCODE_ARTICLE().getText());
						paramAfficheAideRecherche.setControllerAppelant(getThis());
						ModelGeneralObjetEJB<TaArticle,TaArticleDTO> modelArticle = new ModelGeneralObjetEJB<TaArticle,TaArticleDTO>(dao);
						paramAfficheAideRecherche.setModel(modelArticle);
						paramAfficheAideRecherche.setTypeObjet(swtPaArticlesController.getClassModel());
						paramAfficheAideRecherche.setChampsIdentifiant(Const.C_ID_DTO_GENERAL);
					}	
					if (getFocusCourantSWT()==vue.getTfLIB_L_DEVIS()){
						PaArticleSWT paArticleSWT = new PaArticleSWT(s2,SWT.NULL);
						SWTPaArticlesController swtPaArticlesController = new SWTPaArticlesController(paArticleSWT);
						paramAfficheAideRecherche.setForceAffichageAideRemplie(false);

						editorCreationId = ArticleMultiPageEditor.ID_EDITOR;
						editorInputCreation = new EditorInputArticle();

						ParamAfficheArticles paramAfficheArticles = new ParamAfficheArticles();
						ITaArticleServiceRemote dao = new EJBLookup<ITaArticleServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_ARTICLE_SERVICE, ITaArticleServiceRemote.class.getName(),false);
						paramAfficheAideRecherche.setJPQLQuery(dao.getJPQLQuery());
						paramAfficheArticles.setModeEcran(EnumModeObjet.C_MO_INSERTION);
						paramAfficheArticles.setEcranAppelant(paAideController);
						controllerEcranCreation = swtPaArticlesController;
						parametreEcranCreation = paramAfficheArticles;

						paramAfficheAideRecherche.setTypeEntite(TaArticle.class);
						paramAfficheAideRecherche.setChampsRecherche(Const.C_LIBELLEC_ARTICLE);
						paramAfficheAideRecherche.setDebutRecherche(vue.getTfLIB_L_DEVIS().getText());
						paramAfficheAideRecherche.setControllerAppelant(getThis());
						ModelGeneralObjetEJB<TaArticle,TaArticleDTO> modelArticle = new ModelGeneralObjetEJB<TaArticle,TaArticleDTO>(dao);
						paramAfficheAideRecherche.setModel(modelArticle);
						//paramAfficheAideRecherche.setModel(new ModelGeneralObjet<SWTArticle,TaArticleDAO,TaArticle>(SWTArticle.class,getEm()));
						//paramAfficheAideRecherche.setModel(new ModelGeneral<SWTArticle>(swtPaArticlesController.getIbTaTable().getFIBQuery(),SWTArticle.class));
						paramAfficheAideRecherche.setTypeObjet(swtPaArticlesController.getClassModel());
						paramAfficheAideRecherche.setChampsIdentifiant(Const.C_ID_DTO_GENERAL);
					}					
					if (getFocusCourantSWT()==vue.getTfU1_L_DEVIS()||
							getFocusCourantSWT()==vue.getTfU2_L_DEVIS()){
						PaUniteSWT paUniteSWT = new PaUniteSWT(s2,SWT.NULL);
						SWTPaUniteController swtPaUniteController = new SWTPaUniteController(paUniteSWT);

						editorCreationId = EditorUnite.ID;
						editorInputCreation = new EditorInputUnite();

						paramAfficheAideRecherche.setForceAffichageAideRemplie(true);
						
						ParamAfficheUnite paramAfficheUnite = new ParamAfficheUnite();
						paramAfficheUnite.setModeEcran(EnumModeObjet.C_MO_INSERTION);
						paramAfficheUnite.setEcranAppelant(paAideController);
						paramAfficheAideRecherche.setJPQLQuery(swtPaUniteController.getDao().getJPQLQuery());
						paramAfficheAideRecherche.setChampsRecherche(Const.C_CODE_UNITE);
						
						paramAfficheAideRecherche.setTypeEntite(TaUnite.class);
						if(getFocusCourantSWT()==vue.getTfU1_L_DEVIS())
							paramAfficheAideRecherche.setDebutRecherche(vue.getTfU1_L_DEVIS().getText());
						else
							paramAfficheAideRecherche.setDebutRecherche(vue.getTfU2_L_DEVIS().getText());
						paramAfficheAideRecherche.setChampsIdentifiant(Const.C_ID_DTO_GENERAL);
						controllerEcranCreation = swtPaUniteController;
						parametreEcranCreation = paramAfficheUnite;
						paramAfficheAideRecherche.setControllerAppelant(getThis());
						//passage ejb
						//paramAfficheAideRecherche.setModel(new TaUniteDAO(getEm()).modelObjetUniteArticle(taLDevis.getTaArticle().getCodeArticle()));
						paramAfficheAideRecherche.setTypeObjet(swtPaUniteController.getClassModel());
					}					
					if (getFocusCourantSWT()==vue.getTfPRIX_U_L_DEVIS()){
						PaPrixSWT paPrixSWT = new PaPrixSWT(s2,SWT.NULL);
						SWTPaPrixController swtPaPrixController = new SWTPaPrixController(paPrixSWT);

						editorCreationId = EditorPrix.ID;
						editorInputCreation = new EditorInputPrix();

						paramAfficheAideRecherche.setForceAffichageAideRemplie(true);
						
						paramAfficheAideRecherche.setAfficheDetail(false);

						ParamAffichePrix paramAffichePrix = new ParamAffichePrix();
						paramAffichePrix.setModeEcran(EnumModeObjet.C_MO_INSERTION);
						paramAffichePrix.setEcranAppelant(paAideController);
						paramAffichePrix.setIdArticle(LibConversion.integerToString(getSelectedLigneDevis().getIdArticle()));
						paramAfficheAideRecherche.setJPQLQuery(swtPaPrixController.getDao().getJPQLQuery());
						if(masterEntity.getTtc()==1)
							paramAfficheAideRecherche.setChampsRecherche(Const.C_PRIXTTC_PRIX);
						else
							paramAfficheAideRecherche.setChampsRecherche(Const.C_PRIX_PRIX);
						
						paramAfficheAideRecherche.setTypeEntite(TaPrix.class);
						paramAfficheAideRecherche.setDebutRecherche(vue.getTfPRIX_U_L_DEVIS().getText());
						paramAfficheAideRecherche.setChampsIdentifiant(Const.C_ID_DTO_GENERAL);
						controllerEcranCreation = swtPaPrixController;
						parametreEcranCreation = paramAffichePrix;
						paramAfficheAideRecherche.setControllerAppelant(getThis());
						ModelGeneralObjetEJB<TaPrix,TaPrixDTO> modelPrix = new ModelGeneralObjetEJB<TaPrix,TaPrixDTO>(swtPaPrixController.getDao());
						paramAfficheAideRecherche.setModel(modelPrix);
						paramAfficheAideRecherche.setTypeObjet(swtPaPrixController.getClassModel());
					}
					break;
				default:
					break;
				}

//				if (paramAfficheAideRecherche.getJPQLQuery() != null) {

					PaAideRechercheSWT paAideRecherche1 = new PaAideRechercheSWT(s, SWT.NULL);
					SWTPaAideRechercheControllerSWT paAideRechercheController1 = new SWTPaAideRechercheControllerSWT(
							paAideRecherche1);

					// Parametrage de la recherche
					paramAfficheAideRecherche.setFocusSWT(((PaAideRechercheSWT) paAideRechercheController1.getVue()).getTfChoix());
					paramAfficheAideRecherche.setRefCreationSWT(controllerEcranCreation);
					paramAfficheAideRecherche.setEditorCreation(editorCreation);
					paramAfficheAideRecherche.setEditorCreationId(editorCreationId);
					paramAfficheAideRecherche.setEditorInputCreation(editorInputCreation);
					paramAfficheAideRecherche.setParamEcranCreation(parametreEcranCreation);
					paramAfficheAideRecherche.setShellCreation(s2);
					paAideRechercheController1.configPanel(paramAfficheAideRecherche);
					// paramAfficheAideRecherche.setFocusDefaut(paAideRechercheController1.getVue().getTfChoix());

					// Ajout d'une recherche
					paAideController.addRecherche(paAideRechercheController1,
							paramAfficheAideRecherche.getTitreRecherche());

					// Parametrage de l'ecran d'aide principal
					ParamAfficheAideSWT paramAfficheAideSWT = new ParamAfficheAideSWT();
					ParamAfficheAide paramAfficheAide = new ParamAfficheAide();

					// enregistrement pour le retour de l'ecran d'aide
					paAideController.addRetourEcranListener(getThis());
					Control focus = vue.getShell().getDisplay().getFocusControl();
					// affichage de l'ecran d'aide principal (+ ses recherches)

					dbc.getValidationStatusMap().removeMapChangeListener(changeListener);
					//LgrShellUtil.afficheAideSWT(paramAfficheAide, null, paAide,paAideController, s);
					/*****************************************************************/
					paAideController.configPanel(paramAfficheAide);
					/*****************************************************************/	
					dbc.getValidationStatusMap().addMapChangeListener(changeListener);

					// je rends enable false tous les boutons avant de passer dans l'écran d'aide
					// pour ne pas que les actions de l'écran des factures interfèrent ceux de l'écran d'aide
					//activeWorkenchPartCommands(false);
					//if(parentEcran!=null) ((SWTPaEditorFactureController)parentEcran).activeWorkenchPartCommands(false);
//				}

			} finally {
				VerrouInterface.setVerrouille(verrouLocal);
				vue.setCursor(Display.getCurrent().getSystemCursor(SWT.CURSOR_ARROW));
			}
		}
	}

	public IStatus validateUI() throws Exception {
		if ((getModeEcran().getMode().compareTo(EnumModeObjet.C_MO_INSERTION) == 0)
				|| (getModeEcran().getMode().compareTo(EnumModeObjet.C_MO_EDITION) == 0)) {
			ctrlTousLesChampsAvantEnregistrementSWT();
		}
		return null;
	}
	
	public IStatus validateUIField(String nomChamp,Object value) {
		
		IStatus s = null;
		
		IStatus resultat = new Status(IStatus.OK,DevisPlugin.PLUGIN_ID,"validateUIField champ : "+nomChamp!=null?nomChamp:"null"+" valeur : "+value!=null?value.toString():"valeur nulle"+" validation OK");
		
		int VALIDATION_CLIENT = 1;
		int VALIDATION_SERVEUR = 2;
		int VALIDATION_CLIENT_ET_SERVEUR = 3;
		
		//int TYPE_VALIDATION = VALIDATION_CLIENT;
		//int TYPE_VALIDATION = VALIDATION_SERVEUR;
		int TYPE_VALIDATION = VALIDATION_CLIENT_ET_SERVEUR;
		
		AbstractApplicationDAOClient<TaLDevisDTO> a = new AbstractApplicationDAOClient<TaLDevisDTO>();
		
		try {
			
			boolean change=true;
			if(nomChamp.equals(Const.C_CODE_ARTICLE)) {
				
				ITaArticleServiceRemote dao = new EJBLookup<ITaArticleServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_ARTICLE_SERVICE, ITaArticleServiceRemote.class.getName(),false);
			
				if(value=="") return new org.eclipse.core.runtime.Status(org.eclipse.core.runtime.Status.OK,gestComBdPlugin.PLUGIN_ID,"OK"); 
				
				TaArticle f = new TaArticle();
				PropertyUtils.setSimpleProperty(f, nomChamp, value);
				try {
					dao.validateEntityProperty(f,nomChamp,ITaLDevisServiceRemote.validationContext);
				} catch(Exception e) {
					resultat = new Status(IStatus.ERROR,TiersPlugin.PLUGIN_ID, e.getMessage(), e);
				}

				if(resultat.getSeverity()!=IStatus.ERROR) {

					f = dao.findByCode((String)value);
					if(taLDevisDTO!=null)
						change=!f.getCodeArticle().equals(taLDevisDTO.getCodeArticle());
					if(change){
						taLDevis.setTaArticle(f);
						initTypeLigne(); //=> passage ejb
						taLDevis.initQte2(f); 
						mapperModelToUI.map(taLDevis, taLDevisDTO);
					}
				}
				dao = null;
				s =resultat;

//passage ejb
//				if(value=="") return new org.eclipse.core.runtime.Status(org.eclipse.core.runtime.Status.OK,gestComBdPlugin.PLUGIN_ID,"OK"); 
//				
//				TaArticleDAO dao = new TaArticleDAO(getEm());
//
//				dao.setModeObjet(getMasterDAO().getModeObjet());
//				TaArticle f = new TaArticle();
//				PropertyUtils.setSimpleProperty(f, nomChamp, value);
//				s = dao.validate(f,nomChamp,ITaLDevisServiceRemote.validationContext);
//
//				if(s.getSeverity()!=IStatus.ERROR) {
//
//					f = dao.findByCode((String)value);
//					if(TaLDevisDTO!=null)
//						change=!f.getCodeArticle().equals(TaLDevisDTO.getCodeArticle());
//					if(change){
//						taLDevis.setTaArticle(f);
//						taLDevis.initQte2(f);
//						mapperModelToUI.map(taLDevis, TaLDevisDTO);
//					}
//				}
//				dao = null;

			} else {
				TaLDevis u = new TaLDevis();
				try {
					u.setTaDocument(masterEntity);
					if(nomChamp.equals(Const.C_NUM_LIGNE_L_DOCUMENT) && value instanceof String) {
						value = LibConversion.stringToInteger(value.toString());
					}
					PropertyUtils.setSimpleProperty(u, nomChamp, value);
					dao.validateEntityProperty(u,nomChamp,ITaLDevisServiceRemote.validationContext);
				} catch(Exception e) {
					resultat = new Status(IStatus.ERROR,TiersPlugin.PLUGIN_ID, e.getMessage(), e);
				}
				
				if(nomChamp.equals(Const.C_QTE_L_DOCUMENT)){
					if (taLDevisDTO!=null)
						change =u.getQteLDocument()!=null&& !u.getQteLDocument().equals(taLDevisDTO.getQteLDocument());
				}
				if(resultat.getSeverity()!=IStatus.ERROR) {
					PropertyUtils.setSimpleProperty(taLDevis, nomChamp, value);
				}
				s =resultat;
//passage ejb				
//				TaLDevis u = new TaLDevis();
//				u.setTaDocument(masterEntity);
//				if(nomChamp.equals(Const.C_NUM_LIGNE_L_DOCUMENT) && value instanceof String) {
//					value = LibConversion.stringToInteger(value.toString());
//				}
//				PropertyUtils.setSimpleProperty(u, nomChamp, value);
//				s = dao.validate(u,nomChamp,ITaLDevisServiceRemote.validationContext);
//				
//				if(nomChamp.equals(Const.C_QTE_L_DOCUMENT)){
//					if (TaLDevisDTO!=null)
//						change =u.getQteLDocument()!=null&& !u.getQteLDocument().equals(TaLDevisDTO.getQteLDocument());
//				}
//				if(s.getSeverity()!=IStatus.ERROR) {
//					PropertyUtils.setSimpleProperty(taLDevis, nomChamp, value);
//				}
			}
			
			if(s.getSeverity()!=IStatus.ERROR) {
				if(nomChamp.equals(Const.C_QTE_L_DOCUMENT)&& change) {
					if(taLDevis.getTaArticle()!=null){
						taLDevis.initQte2(taLDevis.getTaArticle());
					}
				}
				mapperModelToUI.map(taLDevis, ((TaLDevisDTO)selectedLigneDevis.getValue()));
			}

			return s;
		} catch (IllegalAccessException e) {
			logger.error("",e);
		} catch (InvocationTargetException e) {
			logger.error("",e);
		} catch (NoSuchMethodException e) {
			logger.error("",e);
		} catch (FinderException e) {
			logger.error("",e);
		} catch (NamingException e1) {
			logger.error("",e1);
		}
		return null;
		
	}

	@Override
	protected void actEnregistrer() throws Exception {
		actEnregistrer(true);
	}


	protected void actEnregistrer(boolean insertion) throws Exception {
		try {
			if (getModeEcran().getMode().compareTo(EnumModeObjet.C_MO_EDITION) == 0
					|| getModeEcran().getMode().compareTo(EnumModeObjet.C_MO_INSERTION) == 0) {

				if(insertion)
					insertion=getModeEcran().getMode()==EnumModeObjet.C_MO_INSERTION;

				ctrlUnChampsSWT(getFocusCourantSWT());
				ctrlTousLesChampsAvantEnregistrementSWT();
				if ((getModeEcran().getMode().compareTo(EnumModeObjet.C_MO_EDITION) == 0)) {
					LgrDozerMapper<TaLDevisDTO,TaLDevis> mapper = new LgrDozerMapper<TaLDevisDTO,TaLDevis>();
					mapper.map((TaLDevisDTO) selectedLigneDevis.getValue(),taLDevis);

				} else if ((getModeEcran().getMode().compareTo(EnumModeObjet.C_MO_INSERTION) == 0)) {
					LgrDozerMapper<TaLDevisDTO,TaLDevis> mapper = new LgrDozerMapper<TaLDevisDTO,TaLDevis>();
					mapper.map((TaLDevisDTO) selectedLigneDevis.getValue(),taLDevis);
				}

				masterEntity.enregistreLigne(taLDevis);
				
				//masterDAO.calculeTvaEtTotaux(masterEntity); //=> passage ejb
				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				
				if(focusDansLigne()&& insertion) {
					insertionLigne(true,true);//ajout d'une ligne à la suite des autres
				}
			}
		} finally {
			initEtatBouton(IHMmodel());
			VerrouInterface.setVerrouille(false);
		}
	}
//
//	public SWT_IB_TA_L_PROFORMA getIbTaTable() {
//		return ibTaTable;
//	}
	
	public void initEtatComposant(String type) throws SQLException{		
		initEtatComposant();
		vue.getTfQTE_L_DEVIS().setEditable(!type.equals(Const.C_CODE_T_LIGNE_C));
		vue.getTfQTE2_L_DEVIS().setEditable(!type.equals(Const.C_CODE_T_LIGNE_C));
		vue.getTfU1_L_DEVIS().setEditable(!type.equals(Const.C_CODE_T_LIGNE_C));
		vue.getTfU2_L_DEVIS().setEditable(!type.equals(Const.C_CODE_T_LIGNE_C));
		vue.getTfPRIX_U_L_DEVIS().setEditable(!type.equals(Const.C_CODE_T_LIGNE_C));
		vue.getTfREM_TX_L_DEVIS().setEditable(!type.equals(Const.C_CODE_T_LIGNE_C));
	}

	public void initEtatComposant() {
		try {
			vue.getTfNUM_LIGNE_L_DEVIS().setEnabled(false);
			vue.getTfMT_HT_L_DEVIS().setEditable(false);
			vue.getTfMT_TTC_L_DEVIS().setEditable(false);

			vue.getTfCODE_ARTICLE().setEditable(!isUtilise());
			changeCouleur(vue);
			boolean editable =true;

			vue.getTfCODE_ARTICLE().setEditable(editable);
			vue.getTfLIB_L_DEVIS().setEditable(editable);
			vue.getTfPRIX_U_L_DEVIS().setEditable(editable);
			vue.getTfQTE_L_DEVIS().setEditable(editable);
			vue.getTfQTE2_L_DEVIS().setEditable(editable);
			vue.getTfU1_L_DEVIS().setEditable(editable);
			vue.getTfU2_L_DEVIS().setEditable(editable);
			vue.getTfREM_TX_L_DEVIS().setEditable(editable);
			vue.getTfMT_HT_L_DEVIS().setEditable(false);
			vue.getTfMT_TTC_L_DEVIS().setEditable(false);
			
		} catch (Exception e) {
			//vue.getLaMessage().setText(e.getMessage());
		}
	}

	public boolean isUtilise(){
		return ((ILigneDocumentDTO)selectedLigneDevis.getValue()).getIdLDocument()!=null && 
		!dao.recordModifiable(dao.getNomTable(),
				((ILigneDocumentDTO)selectedLigneDevis.getValue()).getIdLDocument());		
	}
	
	public ILigneDocumentDTO getIhmOldLDevis() {
		return ihmOldLDevis;
	}

	public void setIhmOldLDevis(TaLDevisDTO swtOldDevis) {
		this.ihmOldLDevis = swtOldDevis;
	}

	public void setIhmOldLDevis() {
		if (selectedLigneDevis!=null && selectedLigneDevis.getValue() != null)
			this.ihmOldLDevis = taLDevisDTO.copy((TaLDevisDTO) selectedLigneDevis.getValue());
		else {
			if(tableViewer.selectionGrille(0)){
				this.ihmOldLDevis = taLDevisDTO.copy((TaLDevisDTO) selectedLigneDevis.getValue());
				tableViewer.setSelection(new StructuredSelection(
						(ILigneDocumentDTO) selectedLigneDevis.getValue()), true);
			}}
	}

	public void setVue(final PaSaisieLigneDevis vue) {
		super.setVue(vue);
		this.vue = vue;
	}

	@Override
	protected void initMapComposantDecoratedField() {
		if (mapComposantDecoratedField == null)
			mapComposantDecoratedField = new LinkedHashMap<Control, ControlDecoration>();

		mapComposantDecoratedField.put(vue.getTfCODE_ARTICLE(), vue.getFieldCODE_ARTICLE());
		mapComposantDecoratedField.put(vue.getTfLIB_L_DEVIS(), vue.getFieldLIB_L_DEVIS());
		mapComposantDecoratedField.put(vue.getTfMT_HT_L_DEVIS(), vue.getFieldMT_HT_L_DEVIS());
		mapComposantDecoratedField.put(vue.getTfMT_TTC_L_DEVIS(), vue.getFieldMT_TTC_L_DEVIS());
		mapComposantDecoratedField.put(vue.getTfNUM_LIGNE_L_DEVIS(), vue.getFieldNUM_LIGNE_L_DEVIS());
		mapComposantDecoratedField.put(vue.getTfPRIX_U_L_DEVIS(), vue.getFieldPRIX_U_L_DEVIS());
		mapComposantDecoratedField.put(vue.getTfQTE_L_DEVIS(), vue.getFieldQTE_L_DEVIS());
		mapComposantDecoratedField.put(vue.getTfQTE2_L_DEVIS(), vue.getFieldQTE2_L_DEVIS());
		mapComposantDecoratedField.put(vue.getTfREM_TX_L_DEVIS(), vue.getFieldREM_TX_L_DEVIS());
		mapComposantDecoratedField.put(vue.getTfU1_L_DEVIS(), vue.getFieldU1_L_DEVIS());
		mapComposantDecoratedField.put(vue.getTfU2_L_DEVIS(), vue.getFieldU2_L_DEVIS());
	}

	public Class getClassModel() {
		return classModel;
	}

	@Override
	protected void sortieChamps() {
		try {
			if (masterEntity.getLigne(masterEntity.getLigneCourante())!=null && 
					( (TaLDevis)masterEntity.getLigne(masterEntity.getLigneCourante())).getTaTLigne().getIdTLigne()!=0)
				initEtatComposant(( (TaLDevis)masterEntity.getLigne(masterEntity.getLigneCourante())).getTaTLigne().getCodeTLigne());		
		} catch (SQLException e) {
			logger.debug("",e);
		} catch (Exception e) {
			logger.debug("",e);
		}
	}

	@Override
	protected void actRefresh() throws Exception {
		writableList = new WritableList(realm, IHMmodel(), classModel);
		tableViewer.setInput(writableList);
		tableViewer.refresh();
		tableViewer.selectionGrille(
				tableViewer.selectionGrille(selectedLigneDevis.getValue()));	
	}

	public void initTTC() {
		boolean ttc = false;
		if(masterEntity!=null && masterEntity.getTtc()==1) {
			ttc = true;
		} 
			vue.getTfMT_HT_L_DEVIS().setEnabled(!ttc);
			vue.getTfMT_TTC_L_DEVIS().setEnabled(ttc);
			vue.getLaMT_HT_L_DEVIS().setEnabled(!ttc);
			vue.getLaMT_TTC_L_DEVIS().setEnabled(ttc);
	}

	public void modificationDocument(SWTModificationDocumentEvent evt) {
		if(getModeEcran().getMode()!=EnumModeObjet.C_MO_EDITION
				&& getModeEcran().getMode()!=EnumModeObjet.C_MO_INSERTION) {
			try {
				actRefresh();
			} catch (Exception e) {
				logger.error("",e);
			}
		} 
		refreshTotauxLignes();//doit se produire aussi quand il y a une modification de la 
	}
	
private ListenerList selectionChangedListeners = new ListenerList(3);
	
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		selectionChangedListeners.add(listener);
	}

	public ISelection getSelection() {
		return null;
	}
	
    protected void fireSelectionChanged(final SelectionChangedEvent event) {
        Object[] listeners = selectionChangedListeners.getListeners();
        for (int i = 0; i < listeners.length; ++i) {
            final ISelectionChangedListener l = (ISelectionChangedListener) listeners[i];
            SafeRunnable.run(new SafeRunnable() {
                public void run() {
                    l.selectionChanged(event);
                }
            });
        }
    }

	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		selectionChangedListeners.remove(listener);
	}
	
	protected void updateSelection(ISelection selection) {
		SelectionChangedEvent event = new SelectionChangedEvent(this, selection);
		fireSelectionChanged(event);
	}

	public void setSelection(ISelection selection) {
		// TODO Auto-generated method stub
	}


	
//	public int recupLignes(SWTDevis swtDevis) {
//		int nbLignes = 0;
//		try {
//			
//			setSwtDevis(swtDevis);
//			ibTaTable.setDevis(swtDevis);
//			ibTaTable.videTableTemp();
//			swtDevis.getLignes().clear();
//			swtDevis.getLignesTVA().clear();
//		
//			ibTaTable.recupLignesDevis(swtDevis.getEntete().getCODE());
//			initLigneCourantSurRow();
//			ibTaTable.initLigneCourantSurRow();
//			writableList = new WritableList(realm, modelLDevis.remplirListe(), classModel);
//			tableViewer.setInput(writableList);
//			nbLignes = modelLDevis.getListeObjet().size();
//
//			for (TaLDevisDTO ihmLigne : modelLDevis.getListeObjet()) {
//				SWTLigneDevis swtLigne = new SWTLigneDevis((TaLDevisDTO)selectedLigneDevis.getValue());
//				swtLigne.setSWTLigneDEVIS(ihmLigne);
//				swtDevis.addLigne(swtLigne);
//			}
//			tableViewer.selectionGrille(0);
//			initEtatBouton(false);
//			return nbLignes;
//		} catch(Exception e) {
//			logger.error("",e);
//			return nbLignes;
//		}
//	}

//	public WorkbenchPart getWorkbenchPart() {
//		return workbenchPart;
//	}
//
//	public void setWorkbenchPart(WorkbenchPart workbenchPart) {
//		this.workbenchPart = workbenchPart;
//	}
	
	protected class ActionInsererLigne extends ActionInserer {
		public ActionInsererLigne(String name) {
			super(name);
		}

		@Override
		public void run() {
			try {
				handlerService.executeCommand(C_COMMAND_LIGNE_FACTURE_INSERER_ID, null);
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
	protected class ActionAjouterLigne extends ActionInserer {
		public ActionAjouterLigne(String name) {
			super(name);
		}

		@Override
		public void run() {
			try {
				handlerService.executeCommand(C_COMMAND_LIGNE_FACTURE_AJOUT_ID, null);
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
	
	protected class ActionAideCalcTVA extends Action {
		public ActionAideCalcTVA(String name) {
			super(name);
		}

		@Override
		public void run() {
			try {
				handlerService.executeCommand(C_COMMAND_LIGNE_FACTURE_AIDETVA_ID, null);
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

	protected class ActionEnregistrerLigne extends ActionEnregistrer {
		public ActionEnregistrerLigne(String name) {
			super(name);
		}

		@Override
		public void run() {
			try {
				handlerService.executeCommand(C_COMMAND_LIGNE_FACTURE_ENREGISTRER_ID, null);
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

	protected class ActionEnregistrerDevis extends ActionEnregistrer {
		public ActionEnregistrerDevis(String name) {
			super(name);
		}

		@Override
		public void run() {
			try {
				handlerService.executeCommand(C_COMMAND_LIGNE_FACTURE_ENREGISTRER_ID, null);
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
	
	protected class ActionAnnulerLigne extends ActionAnnuler {
		public ActionAnnulerLigne(String name) {
			super(name);
		}

		@Override
		public void run() {
				try {
					actAnnuler();
				} catch (Exception e) {
					logger.error("",e);
				}
				//handlerService.executeCommand(C_COMMAND_LIGNE_FACTURE_ANNULER_ID, null);
//			} catch (ExecutionException e) {
//				logger.error("",e);
//			} catch (NotDefinedException e) {
//				logger.error("",e);
//			} catch (NotEnabledException e) {
//				logger.error("",e);
//			} catch (NotHandledException e) {
//				logger.error("",e);
//			}
		}
	}

	protected class ActionSupprimerLigne extends ActionSupprimer {
		public ActionSupprimerLigne(String name) {
			super(name);
		}

		@Override
		public void run() {
			try {
				handlerService.executeCommand(C_COMMAND_LIGNE_FACTURE_SUPPRIMER_ID, null);
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

	protected class ActionModifierLigne extends ActionModifier {
		public ActionModifierLigne(String name) {
			super(name);
		}

		@Override
		public void run() {
			try {
				handlerService.executeCommand(C_COMMAND_LIGNE_FACTURE_MODIFIER_ID, null);
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

	protected class ActionFermerLigne extends ActionFermer {
		public ActionFermerLigne(String name) {
			super(name);
		}

		@Override
		public void run() {
			try {
				handlerService.executeCommand(C_COMMAND_LIGNE_FACTURE_FERMER_ID, null);
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

	protected class ActionImprimerLigne extends ActionImprimer {
		public ActionImprimerLigne(String name) {
			super(name);
		}

		@Override
		public void run() {
			try {
				handlerService.executeCommand(C_COMMAND_LIGNE_FACTURE_IMPRIMER_ID, null);
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

	protected class ActionAideLigne extends ActionAide {
		public ActionAideLigne(String name) {
			super(name);
		}

		@Override
		public void run() {
			try {
				handlerService.executeCommand(C_COMMAND_LIGNE_FACTURE_AIDE_ID, null);
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

	
	protected class ActionRefreshLigne extends ActionRefresh {
		public ActionRefreshLigne(String name) {
			super(name);
		}

		@Override
		public void run() {
			try {
				handlerService.executeCommand(C_COMMAND_LIGNE_FACTURE_REFRESH_ID, null);
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
	
	private class SelectionGrille extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			setIhmOldLDevis();
			if(selectedLigneDevis.getValue()!=null && masterEntity.getLignes().size()>vue.getGrille().getSelectionIndex()){
				for (TaLDevis p : masterEntity.getLignes()) {
					if(p.getNumLigneLDocument()==((TaLDevisDTO) selectedLigneDevis.getValue()).getNumLigneLDocument()) {
						taLDevis = p;
					}				
				}
			}
		}
	}

	public TaLDevisDTO getSelectedLigneDevis() {
		TaLDevisDTO resultat = null;
		if(selectedLigneDevis!=null && selectedLigneDevis.getValue()!=null)
			resultat = (TaLDevisDTO)selectedLigneDevis.getValue();
		return resultat;
	};	

	public void initLigneCourantSurRow(){
		masterEntity.setLigneCourante(vue.getGrille().getSelectionIndex());
	}
	
	
	protected void actAjouter() throws Exception{
		insertionLigne(true,true);
	}
	
	protected class HandlerAjouter extends LgrAbstractHandler {

		public Object execute(ExecutionEvent event) throws ExecutionException{
			try {
				actAjouter();
			} catch (Exception e) {
				logger.error("",e);
			}
			return event;
		}
	}
	
	protected void insertionLigne(Boolean ajout, boolean initFocus) throws Exception{
		boolean verrouLocal = VerrouInterface.isVerrouille();
		try {
			VerrouInterface.setVerrouille(true);
			if (LgrMess.isDOSSIER_EN_RESEAU())masterDAO.verifAutoriseModification(masterEntity);
			try {
				DeclencheCommandeControllerEvent e = new DeclencheCommandeControllerEvent(this,C_COMMAND_GLOBAL_MODIFIER_ID);
				fireDeclencheCommandeController(e);
			} catch (Exception e) {
				logger.error("",e);
			}		
			if(getMasterModeEcran().dataSetEnModif()){
				setIhmOldLDevis();
				taLDevisDTO = new TaLDevisDTO();
				modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
				taLDevis = new TaLDevis(true); 
				ITaTLigneServiceRemote tLigneDAO= new EJBLookup<ITaTLigneServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_T_LIGNE_SERVICE, ITaTLigneServiceRemote.class.getName());
				taLDevis.setTaTLigne(tLigneDAO.findByCode(Const.C_CODE_T_LIGNE_C));
				
				List<TaLDevisDTO> modelListeLigneDevis = IHMmodel();
				
				if(ajout)
					masterEntity.addLigne(taLDevis);
				else{
					if(vue.getGrille().getItemCount()!=0) 
						masterEntity.insertLigne(taLDevis,vue.getGrille().getSelectionIndex());
					else
						masterEntity.insertLigne(taLDevis,0);
						
				}
	//			modelListeLigneDevis = IHMmodel();
				
				taLDevisDTO.setNumLigneLDocument(taLDevis.getNumLigneLDocument());
				taLDevis.setTaDocument(masterEntity);
				taLDevis.initialiseLigne(true);
				
				modelListeLigneDevis.add(taLDevisDTO.getNumLigneLDocument(),taLDevisDTO);
				
				writableList = new WritableList(realm, modelListeLigneDevis, classModel);
				tableViewer.setInput(writableList);
				tableViewer.refresh();
				tableViewer.setSelection(new StructuredSelection(taLDevisDTO),true);
				initEtatComposant(taLDevis.getTaTLigne().getCodeTLigne());
	
				taLDevis.setNumLigneLDocument(taLDevisDTO.getNumLigneLDocument());
	
				initEtatBouton(initFocus);
				
	//			try {
	//				DeclencheCommandeControllerEvent e = new DeclencheCommandeControllerEvent(this,C_COMMAND_GLOBAL_MODIFIER_ID);
	//				fireDeclencheCommandeController(e);
	//			} catch (Exception e) {
	//				logger.error("",e);
	//			}		
			}
		} finally {
			initEtatComposant();
			VerrouInterface.setVerrouille(verrouLocal);
		}

	}
		
	/**
	 * Initialisation des boutons suivant l'état de l'objet "ibTaTable"
	 * @param initFocus - si vrai initialise le focus en fonction du mode
	 *///,boolean etatFacture
	protected void initEtatBouton(boolean initFocus) {
		//super.initEtatBouton(initFocus);
		List l = IHMmodel();
		super.initEtatBoutonCommand(initFocus,l);
		enableActionAndHandler(C_COMMAND_GLOBAL_PRECEDENT_ID,true);
		enableActionAndHandler(C_COMMAND_GLOBAL_SUIVANT_ID,true);
		enableActionAndHandler(C_COMMAND_LIGNE_FACTURE_CHANGETVA_ID,true);
		boolean trouve = false;
		boolean etatFacture = true;
		if(masterEntity!=null) {
			trouve = l.size()>0;
		} else {
			//trouve = dao.selectAll().size()>0;
			trouve = false;
		}
		switch (getModeEcran().getMode()) {
		case C_MO_INSERTION:
			enableActionAndHandler(C_COMMAND_GLOBAL_IMPRIMER_ID,false);
			enableActionAndHandler(C_COMMAND_GLOBAL_ENREGISTRER_ID,true);
			enableActionAndHandler(C_COMMAND_GLOBAL_INSERER_ID,false);
			enableActionAndHandler(C_COMMAND_GLOBAL_MODIFIER_ID,false);
			break;
		case C_MO_EDITION:
			enableActionAndHandler(C_COMMAND_GLOBAL_IMPRIMER_ID,false);
			enableActionAndHandler(C_COMMAND_GLOBAL_INSERER_ID,false);
			enableActionAndHandler(C_COMMAND_GLOBAL_MODIFIER_ID,false);
			break;
		case C_MO_CONSULTATION:
			enableActionAndHandler(C_COMMAND_GLOBAL_IMPRIMER_ID,false);
			enableActionAndHandler(C_COMMAND_GLOBAL_INSERER_ID,etatFacture);
			enableActionAndHandler(C_COMMAND_GLOBAL_MODIFIER_ID,(trouve && etatFacture));
			break;
		default:
			break;
		}

		boolean editable =true;
		vue.getTfCODE_ARTICLE().setEditable(editable);
		vue.getTfLIB_L_DEVIS().setEditable(editable);
		vue.getTfPRIX_U_L_DEVIS().setEditable(editable);
		vue.getTfQTE_L_DEVIS().setEditable(editable);
		vue.getTfQTE2_L_DEVIS().setEditable(editable);
		vue.getTfU1_L_DEVIS().setEditable(editable);
		vue.getTfU2_L_DEVIS().setEditable(editable);
		vue.getTfREM_TX_L_DEVIS().setEditable(editable);
		vue.getTfMT_HT_L_DEVIS().setEditable(editable);
		vue.getTfMT_TTC_L_DEVIS().setEditable(editable);
		if (initFocus)initFocusSWT(getModeEcran(), mapInitFocus);
	}	

	
//	public String[][] initAdapterArticle() {
//		String[][] valeurs = null;
//		boolean affichageCtrlEspace = GestionCommercialePlugin.getDefault().getPreferenceStore().getBoolean(fr.legrain.gestionCommerciale.preferences.PreferenceConstants.AFFICHAGE_CTRL_ESPACE);
//		if(affichageCtrlEspace){
//		if(listeArticles==null){
//			TaArticleDAO taArticleDAO = new TaArticleDAO(getEm());
//			//listeArticles =taArticleDAO.selectAll(); 
//			listeArticles =taArticleDAO.findByActif(true);
//			taArticleDAO = null;
//		}		
//		valeurs = new String[listeArticles.size()][2];
//		int i = 0;
//		String description = "";
//		for (TaArticle taArticle : listeArticles) {
//			valeurs[i][0] = taArticle.getCodeArticle();
//			
//			description = "";
//			description += taArticle.getLibellecArticle();
//			if(taArticle.getTaFamille()!=null)
//				description += " \r\n Famille = " + taArticle.getTaFamille().getCodeFamille();
//			description += " \r\n Compte = " + taArticle.getNumcptArticle();
//			if(taArticle.getTaPrix()!=null) {
//				description += " \r\n Prix HT = " + taArticle.getTaPrix().getPrixPrix();
//				description += " \r\n Prix TTC = " + taArticle.getTaPrix().getPrixttcPrix();
//				if(taArticle.getTaPrix().getTaUnite()!=null) {
//					description += " \r\n Unité = " + taArticle.getTaPrix().getTaUnite().getCodeUnite();
//				}
//			}
//			if(taArticle.getTaTva()!=null)
//				description += " \r\n Code tva = " + taArticle.getTaTva().getCodeTva();
//			if(taArticle.getTaTTva()!=null)
//				description += " \r\n Exigibilité = " + taArticle.getTaTTva().getCodeTTva();
//			description += " \r\n Divers = " + taArticle.getDiversArticle();
//			description += " \r\n commentaire = " + taArticle.getCommentaireArticle();
//			valeurs[i][1] = description;
//			
//			i++;
//		}
//		}
//		return valeurs;
//	}
	public String[][] initAdapterArticle() {
		String[][] valeurs = null;
		try {
		List<Object[]> listeTemp = null;
		boolean affichageCtrlEspace = GestionCommercialePlugin.getDefault().getPreferenceStore().getBoolean(fr.legrain.gestionCommerciale.preferences.PreferenceConstants.AFFICHAGE_CTRL_ESPACE);
		if(affichageCtrlEspace){
		if(listeArticles==null){
			ITaArticleServiceRemote taArticleDAO = new EJBLookup<ITaArticleServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_ARTICLE_SERVICE, ITaArticleServiceRemote.class.getName(),false);
			//listeArticles =taArticleDAO.selectAll(); 
			listeTemp =taArticleDAO.findByActifLight(true);
			taArticleDAO = null;
		}		
		valeurs = new String[listeTemp.size()][2];
		int i = 0;
		String description = "";
		for (Object[] taArticle : listeTemp) {
			valeurs[i][0] = (String)taArticle[0];
//			 a.codeArticle,a.libellecArticle,fam.codeFamille,a.numcptArticle,p.prixPrix,p.prixttcPrix,un.codeUnite,tva.codeTva,ttva.codeTTva,a.diversArticle,a.commentaireArticle		
			description = "";
			description += (String)taArticle[1];
			if((String)taArticle[2]!=null)
				description += " \r\n Famille = " + (String)taArticle[2];
			description += " \r\n Compte = " + (String)taArticle[3];
			if((BigDecimal)taArticle[4]!=null) description += " \r\n Prix HT = " + LibConversion.bigDecimalToString((BigDecimal)taArticle[4]);
			if((BigDecimal)taArticle[5]!=null)description += " \r\n Prix TTC = " + LibConversion.bigDecimalToString((BigDecimal)taArticle[5]);
			if((String)taArticle[6]!=null) 	description += " \r\n Unité = " + (String)taArticle[6];
				
			
			if((String)taArticle[7]!=null)
				description += " \r\n Code tva = " +(String)taArticle[7];
			if((String)taArticle[8]!=null)
				description += " \r\n Exigibilité = " + (String)taArticle[8];
			description += " \r\n Divers = " + (String)taArticle[9];
			description += " \r\n commentaire = " + (String)taArticle[10];
			valeurs[i][1] = description;
			
			i++;
		}
		}
		} catch (NamingException e) {
			logger.error("",e);
		}
		return valeurs;
	}

	public boolean focusDansLigne(){
		for (Control element : listeComposantFocusable) {
			if(element.isFocusControl())
				return true;
		}
		return false;
	}
	
		
	protected void actDown() throws Exception{
		boolean annule=false;
		int selectionGrille=getGrille().getSelectionIndex();
		try {
			if(getModeEcran().dataSetEnModif()) {
//				ctrlUnChampsSWT(getFocusCourantSWT());
				if(taLDevis.ligneEstVide()){
						actAnnuler();
						annule=true;
					}else			{
						ctrlUnChampsSWT(getFocusCourantSWT());
						actEnregistrer();
					}
			}
			if(!getModeEcran().dataSetEnModif()){
				if(selectionGrille==getGrille().getItemCount()-1){
					if(actionAjouter.isEnabled())
						actAjouter();
					initFocusSWT(getModeEcran(),mapInitFocus);
				}else{ 
					if(!annule)
						tableViewer.selectionGrille(selectionGrille+1);	
					else
						tableViewer.selectionGrille(0);
				}
				initEtatComposant(taLDevis.getTaTLigne().getCodeTLigne());
				getFocusCourantSWT().forceFocus();
			}
		} catch (Exception e1) {
			logger.error("Erreur : actionPerformed", e1);
		}
	}	
	
		
		protected void actUp() throws Exception{
			int selectionGrille=getGrille().getSelectionIndex();
			try {
				if(getModeEcran().dataSetEnModif()) {
//					ctrlUnChampsSWT(getFocusCourantSWT());
					if(taLDevis.ligneEstVide()){
						actAnnuler();
					}else {
						ctrlUnChampsSWT(getFocusCourantSWT());
							actEnregistrer();
					}
				}
				if(!getModeEcran().dataSetEnModif()){
					if(selectionGrille>0)
					tableViewer.selectionGrille(selectionGrille-1);
					else
						tableViewer.selectionGrille(getGrille().getItemCount()-1);
				}
				getFocusCourantSWT().forceFocus();
				initEtatComposant(taLDevis.getTaTLigne().getCodeTLigne());
			} catch (Exception e1) {
				logger.error("Erreur : actionPerformed", e1);
			}
		}


			
			/**
			 * Initialisation des touches qui déclenchent le transfert du focus au composant suivant
			 * @param listeComponent - composants focusable
			 */
			public void initDeplacementSaisie(List<Control> listeComponent) {
				TraverseListener btnTraverseListener = (new TraverseListener(){
					public void keyTraversed(TraverseEvent e) {
						if(e.keyCode == SWT.ARROW_RIGHT || e.keyCode == SWT.ARROW_DOWN ) {
							((Control)e.widget).traverse(SWT.TRAVERSE_TAB_NEXT);
						} else if(e.keyCode == SWT.ARROW_LEFT || e.keyCode == SWT.ARROW_UP) {
							((Control)e.widget).traverse(SWT.TRAVERSE_TAB_PREVIOUS);
						} 				
					}
				});

				TraverseListener textTraverseListener = (new TraverseListener(){
					public void keyTraversed(TraverseEvent e) {
						if(e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR) {
							((Control)e.widget).traverse(SWT.TRAVERSE_TAB_NEXT);
						}
//							if(e.detail!=SWT.TRAVERSE_ESCAPE )	
//								e.doit=false;
					}
				});

				for (Control component : listeComponent) {
					if (!(component instanceof Table) ) { //Text, Button, ...
						if (component instanceof Button) {
							if( (((Button)component).getStyle() & SWT.CHECK) != 0 )
								component.addTraverseListener(textTraverseListener);	
							else
								component.addTraverseListener(btnTraverseListener);					
						} else if (component instanceof Text){
							component.addTraverseListener(textTraverseListener);	
						} else {
							//component.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,setForward);
						}
						//component.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,setBackward);			
					}			
				}		
				//changeCouleur(vue);
			}
			
			

			public EJBBaseControllerSWTStandard getParentEcran() {
				return parentEcran;
			}

			public void setParentEcran(EJBBaseControllerSWTStandard parentEcran) {
				this.parentEcran = parentEcran;
			}


			public List<IContextActivation> getListeContextEntete() {
				return listeContextEntete;
			}

			public void setListeContextEntete(List<IContextActivation> listeContextEntete) {
				this.listeContextEntete = listeContextEntete;
			}
			
			protected class HandlerEnregistrerDevis extends LgrAbstractHandler {

				public Object execute(ExecutionEvent event) throws ExecutionException{
					try {
					getParentEcran().Enregistrer();
					} catch (Exception e) {
						logger.error("",e);
					}
					return event;
				}
			}
			

			public ITaLDevisServiceRemote getDao() {
				return dao;
			}

			public boolean changementPageValide(){
				if (getModeEcran().getMode().compareTo(EnumModeObjet.C_MO_EDITION) == 0
						|| getModeEcran().getMode().compareTo(EnumModeObjet.C_MO_INSERTION) == 0) {
					//mise a jour de l'entite principale
					if(taLDevis!=null && selectedLigneDevis!=null && ((ILigneDocumentDTO) selectedLigneDevis.getValue())!=null) {
						mapperUIToModel.map((TaLDevisDTO) selectedLigneDevis.getValue(),taLDevis);
					}
				}
				return true;
			}

			public TaDevis getMasterEntity() {
				return masterEntity;
			}

			public void setMasterEntity(TaDevis masterEntity) {
				this.masterEntity = masterEntity;
			}

			public ITaDevisServiceRemote getMasterDAO() {
				return masterDAO;
			}

			public void setMasterDAO(ITaDevisServiceRemote masterDAO) {
				this.masterDAO = masterDAO;
			};

			
			public Boolean remonterDocument() throws Exception {
				try{
					boolean res = false;	
					desactiveModifyListener();
					
					res=true;
					return res;
				}
				finally{
					activeModifytListener();
					initEtatBouton(true);
				}

			}
			public ContentProposalProvider contentProposalProviderArticles(){
				String[][] TabArticles = initAdapterArticle();
				if(TabArticles!=null){
					listeCodeArticles=new String[TabArticles.length];
					listeDescriptionArticles=new String[TabArticles.length];
					for (int i=0; i<TabArticles.length; i++) {
						listeCodeArticles [i]=TabArticles[i][0];
						listeDescriptionArticles [i]=TabArticles[i][1];
					}
				}
				return new ContentProposalProvider(listeCodeArticles,
						listeDescriptionArticles);	
				}
				
				public void raffraichitListeArticles(){
					listeArticles=null;
					articleContentProposal.setContentProposalProvider(contentProposalProviderArticles());
				}
				
				public void ctrlTouslesChampsToutesLesLignes() throws Exception{
					for (int i = 0; i < ((LgrTableViewer)tableViewer).getTable().getItemCount(); i++) {
						tableViewer.selectionGrille(i);
						for (TaLDevis p : masterEntity.getLignes()) {
							if(p.getNumLigneLDocument().equals(((TaLDevisDTO) selectedLigneDevis.getValue()).getNumLigneLDocument())) {
								taLDevis = p;
								//taLDevis.setLegrain(true);
								taLDevisDTO= taLDevisDTO.copy((TaLDevisDTO) selectedLigneDevis.getValue());;
							}				
						}
						ctrlTousLesChampsAvantEnregistrementSWT();
					}

				}
				
				
				@Override
				public void modifMode(){
					if (!VerrouInterface.isVerrouille() ){
						try {
							if(!getModeEcran().dataSetEnModif()) {
								if(!(IHMmodel().size()==0)) {
									actModifier();
								} else {
									actInserer();								
								}
								initEtatBouton(false);
							}
						} catch (Exception e1) {
							logger.error("",e1);
						}				
					} 
				}		
				
	}



