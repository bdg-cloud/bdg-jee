package fr.legrain.articles.ecran;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.MathContext;
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
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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

import fr.legrain.article.dto.TaCiblePrixDTO;
import fr.legrain.article.dto.TaPrixDTO;
import fr.legrain.article.dto.TaUniteDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaPrix;
import fr.legrain.article.model.TaUnite;
import fr.legrain.articles.ArticlesPlugin;
import fr.legrain.articles.editor.EditorInputUnite;
import fr.legrain.articles.editor.EditorUnite;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaPrixServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaUniteServiceRemote;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.model.mapping.mapper.TaPrixMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTTarifServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.gestCom.Appli.Const;
//import fr.legrain.gestCom.Appli.LgrDozerMapper;
import fr.legrain.gestCom.Appli.ModelGeneralObjetEJB;
import fr.legrain.gestCom.librairiesEcran.editor.EditorAide;
import fr.legrain.gestCom.librairiesEcran.editor.EditorInputAide;
import fr.legrain.gestCom.librairiesEcran.swt.DeclencheCommandeControllerEvent;
import fr.legrain.gestCom.librairiesEcran.swt.EJBBaseControllerSWTStandard;
import fr.legrain.gestCom.librairiesEcran.swt.IDetailController;
import fr.legrain.gestCom.librairiesEcran.swt.LgrAbstractHandler;
import fr.legrain.gestCom.librairiesEcran.swt.LgrShellUtil;
import fr.legrain.gestCom.librairiesEcran.swt.ParamAfficheAideRechercheSWT;
import fr.legrain.gestCom.librairiesEcran.swt.ParamAfficheAideSWT;
import fr.legrain.gestCom.librairiesEcran.swt.SWTPaAideControllerSWT;
import fr.legrain.gestCom.librairiesEcran.swt.SWTPaAideRechercheControllerSWT;
import fr.legrain.gestCom.librairiesEcran.workbench.AnnuleToutEvent;
import fr.legrain.gestCom.librairiesEcran.workbench.ChangementDePageEvent;
import fr.legrain.gestCom.librairiesEcran.workbench.ChangementDeSelectionEvent;
import fr.legrain.gestCom.librairiesEcran.workbench.EJBLgrEditorPart;
import fr.legrain.gestCom.librairiesEcran.workbench.LgrPartListener;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.lib.data.InfosVerifSaisie;
import fr.legrain.lib.data.LibConversion;
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
import fr.legrain.lib.gui.grille.LgrTableViewer;
import fr.legrain.lib.gui.grille.LgrViewerSupport;
import fr.legrain.lib.validator.AbstractApplicationDAOClient;
import fr.legrain.libMessageLGR.LgrMess;
import fr.legrain.tiers.clientutility.JNDILookupClass;
import fr.legrain.tiers.dto.TaTTarifDTO;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.ecran.PaTiersSWT;
import fr.legrain.tiers.ecran.PaTypeTarifSWT;
import fr.legrain.tiers.ecran.ParamAfficheTiers;
import fr.legrain.tiers.ecran.ParamAfficheTypeTarif;
import fr.legrain.tiers.ecran.SWTPaTiersController;
import fr.legrain.tiers.ecran.SWTPaTypeTarifController;
import fr.legrain.tiers.editor.EditorInputTiers;
import fr.legrain.tiers.editor.EditorInputTypeTarif;
import fr.legrain.tiers.editor.EditorTiers;
import fr.legrain.tiers.editor.EditorTypeTarif;
import fr.legrain.tiers.model.TaTTarif;
import fr.legrain.tiers.model.TaTiers;


public class SWTPaPrixController extends EJBBaseControllerSWTStandard
implements RetourEcranListener,IDetailController {

	static Logger logger = Logger.getLogger(SWTPaPrixController.class.getName());
	private PaPrixSWT vue = null;
	private ITaPrixServiceRemote dao = null;//new TaPrixDAO();
	private String idArticle = null;

	private Object ecranAppelant = null;
	private TaPrixDTO swtPrix;
	private TaPrixDTO swtOldPrix;
	private Realm realm;
	private DataBindingContext dbc;

	private Class classModel = TaPrixDTO.class;
	private ModelGeneralObjetEJB<TaPrix,TaPrixDTO> modelPrix = null;//new ModelGeneralObjet<TaPrixDTO,TaPrixDAO,TaPrix>(dao,classModel);
	private LgrTableViewer tableViewer;
	private WritableList writableList;
	private IObservableValue selectedPrix;
	private String[] listeChamp;
	private String nomClassController = this.getClass().getSimpleName();
	
	private LgrTableViewer tableViewerCible;
	private WritableList writableListCible;
	private String[] listeChampCible;
	private IObservableValue selectedCiblePrix;

	private TaArticle masterEntity = null;
	private ITaArticleServiceRemote masterDAO = null;

	private TaPrix taPrix = null;

	private MapChangeListener changeListener = new MapChangeListener();
	
	public static final String C_COMMAND_AJOUTER_CIBLE_PRIX_ID = "fr.legrain.gestionCommerciale.article.ajouter.cibleprix";
	public static final String C_COMMAND_SUPPRIMER_CIBLE_PRIX_ID = "fr.legrain.gestionCommerciale.article.supprimer.cibleprix";
	private HandlerAjouterCiblePrix handlerAjouterCiblePrix = new HandlerAjouterCiblePrix();
	private HandlerSupprimerCiblePrix handlerSupprimerCiblePrix = new HandlerSupprimerCiblePrix();


	public SWTPaPrixController(PaPrixSWT vue) {
		this(vue,null);
	}

	public SWTPaPrixController(PaPrixSWT vue,EntityManager em) {
//		if(em!=null) {
//			setEm(em);
//		}
		try {
			dao = new EJBLookup<ITaPrixServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_PRIX_SERVICE, ITaPrixServiceRemote.class.getName());
		} catch (NamingException e1) {
			logger.error("",e1);
		}
		modelPrix = new ModelGeneralObjetEJB<TaPrix,TaPrixDTO>(dao);
		setVue(vue);
		// a faire avant l'initialisation du Binding
		vue.getGrille().addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setSwtOldPrix();
			}
		});
		vue.getShell().addShellListener(this);

		// Branchement action annuler : empeche la fermeture automatique de la
		// fenetre sur ESC
		vue.getShell().addTraverseListener(new Traverse());

		initController();
	}

	private void initController() {
		try {
			setGrille(vue.getGrille());
			initSashFormWeight();
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
			vue.getPaCorpsFormulaire().setMenu(popupMenuFormulaire);
			vue.getPaGrille().setMenu(popupMenuGrille);

			initEtatBouton();
		} catch (Exception e) {
			vue.getLaMessage().setText(e.getMessage());
			logger.error("Erreur : SWTPaPrixController", e);
		}
	}

	/**
	 * Creation des objet pour l'interface, a partir de l'entite principale.<br>
	 * Ici : creation d'une liste de prix pour l'IHM, a partir de la liste des prix contenue dans l'entite article.
	 * @return
	 */
	public List<TaPrixDTO> IHMmodel() {
		LinkedList<TaPrix> ldao = new LinkedList<TaPrix>();
		LinkedList<TaPrixDTO> lswt = new LinkedList<TaPrixDTO>();
		//masterEntity.getTaPrixes().clear();
		//if(masterEntity!=null)	masterDAO.refresh(masterEntity);
		if(masterEntity!=null && !masterEntity.getTaPrixes().isEmpty()) {

			ldao.addAll(masterEntity.getTaPrixes());

			LgrDozerMapper<TaPrix,TaPrixDTO> mapper = new LgrDozerMapper<TaPrix,TaPrixDTO>();
			for (TaPrix o : ldao) {
				TaPrixDTO t = null;
				t = (TaPrixDTO) mapper.map(o, TaPrixDTO.class);
				lswt.add(t);
			}

		}
		return lswt;
	}
	
	public List<TaCiblePrixDTO> IHMmodelCible() {
		LinkedList<TaTiers> ldaoTaTiers = new LinkedList<TaTiers>();
		LinkedList<TaTTarif> ldaoTaTTarif = new LinkedList<TaTTarif>();
		LinkedList<TaCiblePrixDTO> lswt = new LinkedList<TaCiblePrixDTO>();
		//masterEntity.getTaPrixes().clear();
		//if(masterEntity!=null)	masterDAO.refresh(masterEntity);
		//if(masterEntity!=null && !masterEntity.getTaPrixes().isEmpty()) {
		if(taPrix!=null && !taPrix.getTaTiers().isEmpty()) {

			ldaoTaTiers.addAll(taPrix.getTaTiers());

			LgrDozerMapper<TaTiers,TaCiblePrixDTO> mapper = new LgrDozerMapper<TaTiers,TaCiblePrixDTO>();
			for (TaTiers o : ldaoTaTiers) {
				TaCiblePrixDTO t = null;
				t = (TaCiblePrixDTO) mapper.map(o, TaCiblePrixDTO.class);
				t.setType(TaCiblePrixDTO.TYPE_TIERS);
				lswt.add(t);
			}

		}
		if(taPrix!=null && !taPrix.getTaTTarif().isEmpty()) {

			ldaoTaTTarif.addAll(taPrix.getTaTTarif());

			LgrDozerMapper<TaTTarif,TaCiblePrixDTO> mapper = new LgrDozerMapper<TaTTarif,TaCiblePrixDTO>();
			for (TaTTarif o : ldaoTaTTarif) {
				TaCiblePrixDTO t = null;
				t = (TaCiblePrixDTO) mapper.map(o, TaCiblePrixDTO.class);
				t.setType(TaCiblePrixDTO.TYPE_TARIF);
				lswt.add(t);
			}

		}
		return lswt;
	}

	public void bind(PaPrixSWT paPrixSWT) {
		try {
			realm = SWTObservables.getRealm(vue.getParent().getDisplay());

			tableViewer = new LgrTableViewer(paPrixSWT.getGrille());
			tableViewer.createTableCol(classModel,paPrixSWT.getGrille(), nomClassController,
					Const.C_FICHIER_LISTE_CHAMP_GRILLE,-1);
			listeChamp = tableViewer.setListeChampGrille(nomClassController,
					Const.C_FICHIER_LISTE_CHAMP_GRILLE);
			selectedPrix = ViewersObservables.observeSingleSelection(tableViewer);

			LgrViewerSupport.bind(tableViewer, 
					new WritableList(IHMmodel(), classModel),
					BeanProperties.values(listeChamp)
			);

			dbc = new DataBindingContext(realm);

			dbc.getValidationStatusMap().addMapChangeListener(changeListener);
			tableViewer.selectionGrille(0);

			setTableViewerStandard(tableViewer);
			setDbcStandard(dbc);
			bindCible();

			bindingFormMaitreDetail(dbc, realm, selectedPrix,classModel);
			tableViewer.setChecked(tableViewer.getTable().getColumn(1),true);
			changementDeSelection();
			selectedPrix.addChangeListener(new IChangeListener() {

				public void handleChange(ChangeEvent event) {
					changementDeSelection();
				}

			});

		} catch (Exception e) {
			vue.getLaMessage().setText(e.getMessage());
			logger.error("", e);
		}
	}
	
	public void bindCible() {
		try {
			//modelLignesTVA = new ModelLignesTVA();
			realm = SWTObservables.getRealm(vue.getParent().getDisplay());

			tableViewerCible = new LgrTableViewer(vue.getTable());
//			tableViewer.createTableCol(vue.getTable(), "FactureLignesTVA",
//					Const.C_FICHIER_LISTE_CHAMP_GRILLE);
//			listeChamp = tableViewer.setListeChampGrille("FactureLignesTVA",
//					Const.C_FICHIER_LISTE_CHAMP_GRILLE);

			tableViewerCible.createTableCol(vue.getTable(), new String[]{"Type","Code","Libelle"}, new String[]{"100","100","100"});
			listeChampCible = new String[]{"type","code","libelle"};

			LgrViewerSupport.bind(tableViewerCible, new WritableList(IHMmodelCible(), TaCiblePrixDTO.class),
					BeanProperties.values(listeChampCible));
			selectedCiblePrix = ViewersObservables.observeSingleSelection(tableViewerCible);

			dbc = new DataBindingContext(realm);

		} catch (Exception e) {
			if (e.getMessage() != null)
				vue.getLaMessage().setText(e.getMessage());
			logger.error("", e);
		}
	}

	private void changementDeSelection() {
		if(selectedPrix!=null && selectedPrix.getValue()!=null) {
			if(((TaPrixDTO) selectedPrix.getValue()).getId()!=null) {
				try {
					taPrix = dao.findById(((TaPrixDTO) selectedPrix.getValue()).getId());
				} catch (FinderException e) {
					logger.error("",e);
				}
			}
			
			refreshCiblePrix();
			
			//null a tester ailleurs, car peut etre null en cas d'insertion
			fireChangementDeSelection(new ChangementDeSelectionEvent(SWTPaPrixController.this));
		}
	}
	public Composite getVue() {
		return vue;
	}

	public ResultAffiche configPanel(ParamAffiche param) {
		if (param != null) {
			Map<String,String[]> map = dao.getParamWhereSQL();
			if (param.getFocusDefautSWT() != null && !param.getFocusDefautSWT().isDisposed())
				param.getFocusDefautSWT().setFocus();
			else
				param.setFocusDefautSWT(vue
						.getGrille());
			if(param.getTitreFormulaire()!=null){
				vue.getLaTitreFormulaire().setText(param.getTitreFormulaire());
			} else {
				vue.getLaTitreFormulaire().setText(ParamAffichePrix.C_TITRE_FORMULAIRE);
			}

			if(param.getTitreGrille()!=null){
				vue.getLaTitreGrille().setText(param.getTitreGrille());
			} else {
				vue.getLaTitreGrille().setText(ParamAffichePrix.C_TITRE_GRILLE);
			}

			if(param.getSousTitre()!=null){
				vue.getLaTitreFenetre().setText(param.getSousTitre());
			} else {
				vue.getLaTitreFenetre().setText(ParamAffichePrix.C_SOUS_TITRE);
			}

			if (param.getEcranAppelant() != null) {
				ecranAppelant = param.getEcranAppelant();
			}

			if(tableViewer==null) {
				//databinding pas encore realise
				bind(vue);
				tableViewer.tri(classModel, nomClassController,Const.C_FICHIER_LISTE_CHAMP_GRILLE);
			} else {
				try {
					taPrix=null;
					selectedPrix.setValue(null);
					actRefresh();
				} catch (Exception e) {
					logger.error("",e);
				}
			}
			// permet de se positionner sur le 1er enregistrement et de remplir
			// le formulaire
			//			tableViewer.tri(classModel, nomClassController,Const.C_FICHIER_LISTE_CHAMP_GRILLE);
			VerrouInterface.setVerrouille(false);
			setSwtOldPrix();

			if (param.getModeEcran() != null
					&& param.getModeEcran().compareTo(
							EnumModeObjet.C_MO_INSERTION) == 0) {
				try {
					actInserer();
				} catch (Exception e) {
					vue.getLaMessage().setText(e.getMessage());
					logger.error("", e);
				}
			} else {
				initEtatBouton();
			}

		}
		return null;
	}

	protected void initVerifySaisie() {
		if (mapInfosVerifSaisie == null)
			mapInfosVerifSaisie = new HashMap<Control, InfosVerifSaisie>();

		mapInfosVerifSaisie.put(vue.getTfPRIX_PRIX(), new InfosVerifSaisie(new TaPrix(),Const.C_PRIX_PRIX,new int[]{InfosVerifSaisie.CTRL_QUE_DES_CHIFFRES_POSITIFS}));
		mapInfosVerifSaisie.put(vue.getTfPRIXTTC_PRIX(), new InfosVerifSaisie(new TaPrix(),Const.C_PRIXTTC_PRIX,new int[]{InfosVerifSaisie.CTRL_QUE_DES_CHIFFRES_POSITIFS}));
		
		mapInfosVerifSaisie.put(vue.getTfCODE_UNITE(), new InfosVerifSaisie(new TaUnite(),Const.C_CODE_UNITE,null));

		initVerifyListener(mapInfosVerifSaisie, dao);
	}
	
	protected void initComposantsVue() throws ExceptLgr {
	}

	protected void initMapComposantChamps() {
		if (mapComposantChamps == null)
			mapComposantChamps = new LinkedHashMap<Control, String>();

		if (listeComposantFocusable == null)
			listeComposantFocusable = new ArrayList<Control>();
		listeComposantFocusable.add(vue.getGrille());

		vue.getTfCODE_UNITE().setToolTipText(Const.C_CODE_UNITE);
		vue.getTfPRIX_PRIX().setToolTipText(Const.C_PRIX_PRIX);
		vue.getTfPRIXTTC_PRIX().setToolTipText(Const.C_PRIXTTC_PRIX);
		vue.getCbID_REF_PRIX().setToolTipText(Const.C_ID_REF_PRIX);
		vue.getTfCodeTiers().setToolTipText(Const.C_CODE_TIERS);
		vue.getTfCodeTTarif().setToolTipText(Const.C_CODE_T_TARIF);

		mapComposantChamps.put(vue.getTfCODE_UNITE(), Const.C_CODE_UNITE);
		mapComposantChamps.put(vue.getTfPRIX_PRIX(), Const.C_PRIX_PRIX);
		mapComposantChamps.put(vue.getTfPRIXTTC_PRIX(), Const.C_PRIXTTC_PRIX);
		mapComposantChamps.put(vue.getCbID_REF_PRIX(), Const.C_ID_REF_PRIX);
		
		listeComposantFocusable.add(vue.getTfCodeTiers());
		listeComposantFocusable.add(vue.getTfCodeTTarif());
//		mapComposantChamps.put(vue.getTfCodeTiers(), Const.C_CODE_TIERS);
//		mapComposantChamps.put(vue.getTfCodeTTarif(), Const.C_CODE_T_TARIF);

		for (Control c : mapComposantChamps.keySet()) {
			listeComposantFocusable.add(c);
		}

		listeComposantFocusable.add(vue.getBtnEnregistrer());
		listeComposantFocusable.add(vue.getBtnInserer());
		listeComposantFocusable.add(vue.getBtnModifier());
		listeComposantFocusable.add(vue.getBtnSupprimer());
		listeComposantFocusable.add(vue.getBtnFermer());
		listeComposantFocusable.add(vue.getBtnAnnuler());
		listeComposantFocusable.add(vue.getBtnImprimer());
		listeComposantFocusable.add(vue.getBtnAjouter());
		listeComposantFocusable.add(vue.getBtnSupprimerCible());

		if (mapInitFocus == null)
			mapInitFocus = new LinkedHashMap<EnumModeObjet, Control>();
		mapInitFocus.put(EnumModeObjet.C_MO_INSERTION, vue
				.getTfCODE_UNITE());
		mapInitFocus.put(EnumModeObjet.C_MO_EDITION, vue
				.getTfCODE_UNITE());
		mapInitFocus.put(EnumModeObjet.C_MO_CONSULTATION, vue
				.getGrille());

		activeModifytListener();

		vue.getTfPRIX_PRIX().addVerifyListener(queDesChiffres);
		vue.getTfPRIXTTC_PRIX().addVerifyListener(queDesChiffres);
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
		mapCommand.put(C_COMMAND_AJOUTER_CIBLE_PRIX_ID, handlerAjouterCiblePrix);
		mapCommand.put(C_COMMAND_SUPPRIMER_CIBLE_PRIX_ID, handlerSupprimerCiblePrix);

		mapCommand.put(C_COMMAND_GLOBAL_PRECEDENT_ID, handlerPrecedent);
		mapCommand.put(C_COMMAND_GLOBAL_SUIVANT_ID, handlerSuivant);

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
		mapActions.put(vue.getBtnAjouter(), C_COMMAND_AJOUTER_CIBLE_PRIX_ID);
		mapActions.put(vue.getBtnSupprimerCible(), C_COMMAND_SUPPRIMER_CIBLE_PRIX_ID);

		Object[] tabActionsAutres = new Object[] { C_COMMAND_GLOBAL_AIDE_ID, C_COMMAND_GLOBAL_REFRESH_ID };
		mapActions.put(null, tabActionsAutres);
	}

	protected void initEtatBouton() {
		//initEtatBoutonCommand();
		initEtatBouton(IHMmodel());
		enableActionAndHandler(C_COMMAND_GLOBAL_IMPRIMER_ID,false);
		switch (getModeEcran().getMode()) {
		case C_MO_INSERTION:
			enableActionAndHandler(C_COMMAND_AJOUTER_CIBLE_PRIX_ID,true);
			enableActionAndHandler(C_COMMAND_SUPPRIMER_CIBLE_PRIX_ID,IHMmodelCible().isEmpty()?false:true);
			break;
		case C_MO_EDITION:
			enableActionAndHandler(C_COMMAND_AJOUTER_CIBLE_PRIX_ID,true);
			enableActionAndHandler(C_COMMAND_SUPPRIMER_CIBLE_PRIX_ID,IHMmodelCible().isEmpty()?false:true);
			break;
		case C_MO_CONSULTATION:
			enableActionAndHandler(C_COMMAND_AJOUTER_CIBLE_PRIX_ID,false);
			enableActionAndHandler(C_COMMAND_SUPPRIMER_CIBLE_PRIX_ID,false);
			break;
		default:
			break;
		}
	}	

	public SWTPaPrixController getThis() {
		return this;
	}

	@Override
	public boolean onClose() throws ExceptLgr {
		boolean retour = true;
		VerrouInterface.setVerrouille(true);
		switch (getModeEcran().getMode()) {
		case C_MO_INSERTION:
		case C_MO_EDITION:
			if (MessageDialog.openQuestion(vue.getShell(), MessagesEcran
					.getString("Message.Attention"), MessagesEcran
					.getString("Prix.Message.Enregistrer"))) {

				try {
					actEnregistrer();
				} catch (Exception e) {
					vue.getLaMessage().setText(e.getMessage());
					logger.error("", e);
				}
			} else {
				fireAnnuleTout(new AnnuleToutEvent(this,true));
			}
			break;
		case C_MO_CONSULTATION:
			break;
		default:
			break;
		}
		if (retour) {
			if(getModeEcran().dataSetEnModif())
				try {
					dao.annuler(taPrix);
				} catch (Exception e) {
					throw new ExceptLgr();
				}
				if (ecranAppelant instanceof SWTPaAideControllerSWT) {
					setListeEntity(getModelPrix().remplirListe());
					dao.initValeurIdTable(taPrix);
					fireRetourEcran(new RetourEcranEvent(this, new ResultAide(
							dao.getChampIdEntite(), dao.getValeurIdTable(),
							selectedPrix.getValue())));

					retour = true;
				}
		}
		return retour;
	}

	public void retourEcran(final RetourEcranEvent evt) {
		if (evt.getRetour() != null
				&& (evt.getSource() instanceof SWTPaAideControllerSWT)) {
			if (getFocusAvantAideSWT() instanceof Text) {
				try {
					((Text) getFocusAvantAideSWT()).setText(((ResultAffiche) evt.getRetour()).getResult());	
					if(getFocusAvantAideSWT().equals(vue.getTfCODE_UNITE())){
						TaUnite u = null;
						//TaUniteDAO t = new TaUniteDAO(getEm());
						ITaUniteServiceRemote t = new EJBLookup<ITaUniteServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_UNITE_SERVICE, ITaUniteServiceRemote.class.getName());
						u = t.findById(Integer.parseInt(((ResultAffiche) evt.getRetour()).getIdResult()));
						t = null;
						taPrix.setTaUnite(u);
					}
					ctrlUnChampsSWT(getFocusAvantAideSWT());
				} catch (Exception e) {
					logger.error("",e);
					vue.getLaMessage().setText(e.getMessage());
				}
			}
			if (getFocusAvantAideSWT() instanceof Table) {
				if (getFocusAvantAideSWT() == vue.getGrille()) {
					if(((ResultAffiche) evt.getRetour()).getSelection()!=null)
						tableViewer.setSelection(((ResultAffiche) evt.getRetour()).getSelection(),true);
				}
			}			
		} else if (evt.getRetour() != null){
			if (getFocusAvantAideSWT() instanceof Table) {
				if (getFocusAvantAideSWT() == vue.getGrille()) {
				}
			}
		}
		super.retourEcran(evt);
	}

	@Override
	protected void actInserer() throws Exception {
		try {
			boolean continuer=true;
			VerrouInterface.setVerrouille(true);
			masterDAO.verifAutoriseModification(masterEntity);
			setSwtOldPrix();
			if(LgrMess.isDOSSIER_EN_RESEAU()){
				DeclencheCommandeControllerEvent e = new DeclencheCommandeControllerEvent(this,C_COMMAND_GLOBAL_MODIFIER_ID);
				fireDeclencheCommandeController(e);
				continuer=getMasterModeEcran().dataSetEnModif();
			}
			if(continuer){
				//setMasterEntity(masterDAO.findById(masterEntity.getIdArticle()));
				swtPrix = new TaPrixDTO();
				if (masterEntity.getTaPrix()==null){
					swtPrix.setIdRefPrix(true);
				}
				else
					swtPrix.setIdRefPrix(false);
				swtPrix.setPrixPrix(new BigDecimal(0));
				swtPrix.setPrixttcPrix(new BigDecimal(0));
				swtPrix.setCodeArticle(masterEntity.getCodeArticle());
//				dao.getModeObjet().setMode(EnumModeObjet.C_MO_INSERTION);
				taPrix = new TaPrix();
				List l = IHMmodel();
				l.add(swtPrix);
				writableList = new WritableList(realm, l, classModel);
				tableViewer.setInput(writableList);
				tableViewer.refresh();
				tableViewer.setSelection(new StructuredSelection(swtPrix));
				
				getModeEcran().setMode(EnumModeObjet.C_MO_INSERTION);//ejb
				initEtatBouton();

				try {
					DeclencheCommandeControllerEvent e = new DeclencheCommandeControllerEvent(this,C_COMMAND_GLOBAL_MODIFIER_ID);
					fireDeclencheCommandeController(e);
				} catch (Exception e) {
					logger.error("",e);
				}
			}
		} finally {
			initEtatComposant();
			VerrouInterface.setVerrouille(false);
		}
	}

	@Override
	protected void actModifier() throws Exception {
		try {
			boolean continuer=true;
			setSwtOldPrix();
			if(LgrMess.isDOSSIER_EN_RESEAU()){
				DeclencheCommandeControllerEvent e = new DeclencheCommandeControllerEvent(this,C_COMMAND_GLOBAL_MODIFIER_ID);
				fireDeclencheCommandeController(e);
				continuer=getMasterModeEcran().dataSetEnModif();
			}
			if(continuer){
//				setMasterEntity(masterDAO.findById(masterEntity.getIdArticle()));
				for (TaPrix p : masterEntity.getTaPrixes()) {
					if(p.getIdPrix()==((TaPrixDTO) selectedPrix.getValue()).getId()) {
						taPrix = p;
						//					if(LgrMess.isDOSSIER_EN_RESEAU())actRefresh();
					}
				}			
				masterDAO.verifAutoriseModification(masterEntity);
				DeclencheCommandeControllerEvent e = new DeclencheCommandeControllerEvent(this,C_COMMAND_GLOBAL_MODIFIER_ID);
				fireDeclencheCommandeController(e);
				modeEcran.setMode(EnumModeObjet.C_MO_EDITION);//ejb
				initEtatBouton();
			}
		} catch (Exception e1) {
			vue.getLaMessage().setText(e1.getMessage());
			logger.error("Erreur : actionPerformed", e1);
		}
	}

	@Override
	protected void actSupprimer() throws Exception {
//		EntityTransaction transaction = dao.getEntityManager().getTransaction();		
		try {
			boolean continuer=true;
			VerrouInterface.setVerrouille(true);
			if(isUtilise())MessageDialog.openInformation(vue.getShell(), MessagesEcran
					.getString("Message.Attention"), MessagesEcran
					.getString("Message.suppression"));
			else
				if(LgrMess.isDOSSIER_EN_RESEAU()){
					DeclencheCommandeControllerEvent e = new DeclencheCommandeControllerEvent(this,C_COMMAND_GLOBAL_MODIFIER_ID);
					fireDeclencheCommandeController(e);
					continuer=getMasterModeEcran().dataSetEnModif();
				}

			if(continuer){
//				setMasterEntity(masterDAO.findById(masterEntity.getIdArticle()));
				if (MessageDialog.openConfirm(vue.getShell(), MessagesEcran
						.getString("Message.Attention"), MessagesEcran
						.getString("Prix.Message.Supprimer"))) {				
//					dao.getModeObjet().setMode(EnumModeObjet.C_MO_SUPPRESSION);
					try {
						DeclencheCommandeControllerEvent e = new DeclencheCommandeControllerEvent(this,C_COMMAND_GLOBAL_MODIFIER_ID);
						fireDeclencheCommandeController(e);
					} catch (Exception e) {
						logger.error("",e);
					}
					for (TaPrix p : masterEntity.getTaPrixes()) {
						if(p.getIdPrix()==((TaPrixDTO) selectedPrix.getValue()).getId()) {
							taPrix = p;
						}
					}
//					dao.begin(transaction);
//					dao.supprimer(taPrix);
//					taPrix.setTaArticle(null);
//					masterEntity.setTaPrix(null);
					masterEntity.removePrix(taPrix);
//					dao.commit(transaction);
					Object suivant=tableViewer.getElementAt(tableViewer.getTable().getSelectionIndex()+1);
					modelPrix.removeEntity(taPrix);			
					taPrix=masterEntity.getTaPrix();
					tableViewer.selectionGrille(0);
					try {
						DeclencheCommandeControllerEvent e = new DeclencheCommandeControllerEvent(this,C_COMMAND_GLOBAL_ENREGISTRER_ID);
						fireDeclencheCommandeController(e);
					} catch (Exception e) {
						logger.error("",e);
					}	
					if(suivant!=null)tableViewer.setSelection(new StructuredSelection(suivant),true);
					else tableViewer.selectionGrille(0);
					
					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
					actRefresh();		
					initEtatBouton();

				}
			}
		} catch (Exception e1) {
			vue.getLaMessage().setText(e1.getMessage());
			logger.error("Erreur : actionPerformed", e1);
		} finally {
			initEtatBouton();
			VerrouInterface.setVerrouille(false);
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
		try {
			VerrouInterface.setVerrouille(true);
			switch (getModeEcran().getMode()) {
			case C_MO_INSERTION:
				if (silencieu || (!silencieu && MessageDialog.openQuestion(vue.getShell(), MessagesEcran
						.getString("Message.Attention"), MessagesEcran
						.getString("Prix.Message.Annuler")))) {
					remetTousLesChampsApresAnnulationSWT(dbc);
					actRefresh();
					//dao.getModeObjet().setMode(EnumModeObjet.C_MO_CONSULTATION);
					getModeEcran().setMode(EnumModeObjet.C_MO_CONSULTATION);//ejb
					hideDecoratedFields();
				}
				initEtatBouton();
				if(!annuleToutEnCours) {
					fireAnnuleTout(new AnnuleToutEvent(this));
				}
				break;
			case C_MO_EDITION:
				if (silencieu || (!silencieu && MessageDialog.openQuestion(vue.getShell(), MessagesEcran
						.getString("Message.Attention"), MessagesEcran
						.getString("Prix.Message.Annuler")))) {
					int rang =((WritableList)tableViewer.getInput()).indexOf(selectedPrix.getValue());
					List<TaPrixDTO> l = IHMmodel();
					if(rang!=-1)
						l.set(rang, swtOldPrix);
					remetTousLesChampsApresAnnulationSWT(dbc);
					writableList = new WritableList(realm, l, classModel);
					tableViewer.setInput(writableList);
					tableViewer.refresh();
					tableViewer.setSelection(new StructuredSelection(swtOldPrix), true);

					ctrlTousLesChampsAvantEnregistrementSWT();

					//dao.getModeObjet().setMode(EnumModeObjet.C_MO_CONSULTATION);
					getModeEcran().setMode(EnumModeObjet.C_MO_CONSULTATION);//ejb
					hideDecoratedFields();
					if(!annuleToutEnCours) {
						fireAnnuleTout(new AnnuleToutEvent(this));
					}
				}
				initEtatBouton();

				break;
			case C_MO_CONSULTATION:
				//actionFermer.run();
				fireChangementDePage(new ChangementDePageEvent(this,ChangementDePageEvent.DEBUT));
				break;
			default:
				break;
			}
		} finally {
			VerrouInterface.setVerrouille(false);
		}
	}

	@Override
	protected void actImprimer() throws Exception {
	}

	@Override
	protected boolean aideDisponible() {
		boolean result = false;
		switch ((getThis().getModeEcran().getMode())) {
		case C_MO_CONSULTATION:
			if(getFocusCourantSWT().equals(vue.getGrille()))
				result = true;
			break;
		case C_MO_EDITION:
		case C_MO_INSERTION:
			if(getFocusCourantSWT().equals(vue.getTfCODE_UNITE())
					|| getFocusCourantSWT().equals(vue.getTfCodeTiers())
					|| getFocusCourantSWT().equals(vue.getTfCodeTTarif()))
				result = true;
			break;
		default:
			break;
		}
		return result;
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
	protected void actAide() throws Exception {
		actAide(null);
	}

	@Override
	protected void actAide(String message) throws Exception {
		if(aideDisponible()){
			try {
				VerrouInterface.setVerrouille(true);
				vue.setCursor(Display.getCurrent().getSystemCursor(SWT.CURSOR_WAIT));
				ParamAfficheAideRechercheSWT paramAfficheAideRecherche = new ParamAfficheAideRechercheSWT();
				paramAfficheAideRecherche.setMessage(message);
				// Creation de l'ecran d'aide principal
				Shell s = new Shell(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),LgrShellUtil.styleLgr);
				PaAideSWT paAide = new PaAideSWT(s, SWT.NONE);
				SWTPaAideControllerSWT paAideController = new SWTPaAideControllerSWT(
						paAide);
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

				switch ((getThis().getModeEcran().getMode())) {
				case C_MO_CONSULTATION:
					if(getFocusCourantSWT().equals(vue.getGrille())){
//						PaPrixSWT paPrixSWT = new PaPrixSWT(s2,SWT.NULL);
//						SWTPaPrixController swtPaPrixController = new SWTPaPrixController(paPrixSWT);
//
//						editorCreationId = EditorPrix.ID;
//						editorInputCreation = new EditorInputPrix();
//
//						
//						paramAfficheAideRecherche.setForceAffichageAideRemplie(true);
//						paramAfficheAideRecherche.setAfficheDetail(false);
//						
//						
//						ParamAffichePrix paramAffichePrix = new ParamAffichePrix();
//						paramAfficheAideRecherche.setJPQLQuery(dao.getJPQLQuery());
//						paramAffichePrix.setModeEcran(EnumModeObjet.C_MO_INSERTION);
//						paramAffichePrix.setEcranAppelant(paAideController);
//						controllerEcranCreation = swtPaPrixController;
//						parametreEcranCreation = paramAffichePrix;
//
//						paramAfficheAideRecherche.setTypeEntite(TaPrix.class);
//						paramAfficheAideRecherche.setChampsRecherche(Const.C_PRIX_PRIX);
//						paramAfficheAideRecherche.setDebutRecherche(vue.getTfPRIX_PRIX().getText());
//						paramAfficheAideRecherche.setControllerAppelant(getThis());
//						paramAfficheAideRecherche.setModel(swtPaPrixController.getModelPrix());
//						paramAfficheAideRecherche.setTypeObjet(swtPaPrixController.getClassModel());
//						paramAfficheAideRecherche.setChampsIdentifiant(Const.C_ID_PRIX);
					}
					break;
				case C_MO_EDITION:
				case C_MO_INSERTION:
					if(getFocusCourantSWT().equals(vue.getTfCODE_UNITE())){
						PaUniteSWT paUniteSWT = new PaUniteSWT(s2,SWT.NULL);
						SWTPaUniteController swtPaUniteController = new SWTPaUniteController(paUniteSWT);

						editorCreationId = EditorUnite.ID;
						editorInputCreation = new EditorInputUnite();

						ParamAfficheUnite paramAfficheUnite = new ParamAfficheUnite();
						ITaUniteServiceRemote dao = new EJBLookup<ITaUniteServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_UNITE_SERVICE, ITaUniteServiceRemote.class.getName());
						paramAfficheAideRecherche.setJPQLQuery(dao.getJPQLQuery());
						paramAfficheUnite.setModeEcran(EnumModeObjet.C_MO_INSERTION);
						paramAfficheUnite.setEcranAppelant(paAideController);
						controllerEcranCreation = swtPaUniteController;
						parametreEcranCreation = paramAfficheUnite;

						paramAfficheAideRecherche.setTypeEntite(TaUnite.class);
						paramAfficheAideRecherche.setChampsRecherche(Const.C_CODE_UNITE);
						paramAfficheAideRecherche.setDebutRecherche(vue.getTfCODE_UNITE().getText());
						paramAfficheAideRecherche.setControllerAppelant(SWTPaPrixController.this);
						ModelGeneralObjetEJB<TaUnite,TaUniteDTO> modelUnite = new ModelGeneralObjetEJB<TaUnite,TaUniteDTO>(dao);
						paramAfficheAideRecherche.setModel(modelUnite);
						paramAfficheAideRecherche.setTypeObjet(swtPaUniteController.getClassModel());

						paramAfficheAideRecherche.setChampsIdentifiant(Const.C_ID_DTO_GENERAL);
					}
					if(getFocusCourantSWT().equals(vue.getTfCodeTiers())){
						PaTiersSWT paTiersSWT = new PaTiersSWT(s2,SWT.NULL);
						SWTPaTiersController swtPaTiersController = new SWTPaTiersController(paTiersSWT);

						editorCreationId = EditorTiers.ID;
						editorInputCreation = new EditorInputTiers();

						ParamAfficheTiers paramAfficheTiers = new ParamAfficheTiers();
						ITaTiersServiceRemote dao = new EJBLookup<ITaTiersServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_TIERS_SERVICE, ITaTiersServiceRemote.class.getName());
						paramAfficheAideRecherche.setJPQLQuery(dao.getJPQLQuery());
						paramAfficheTiers.setModeEcran(EnumModeObjet.C_MO_INSERTION);
						paramAfficheTiers.setEcranAppelant(paAideController);
						controllerEcranCreation = swtPaTiersController;
						parametreEcranCreation = paramAfficheTiers;

						paramAfficheAideRecherche.setTypeEntite(TaTiers.class);
						paramAfficheAideRecherche.setChampsRecherche(Const.C_CODE_TIERS);
						paramAfficheAideRecherche.setDebutRecherche(vue.getTfCodeTiers().getText());
						paramAfficheAideRecherche.setControllerAppelant(SWTPaPrixController.this);
						ModelGeneralObjetEJB<TaTiers,TaTiersDTO> modelTiers = new ModelGeneralObjetEJB<TaTiers,TaTiersDTO>(dao);
						paramAfficheAideRecherche.setModel(modelTiers);
						paramAfficheAideRecherche.setTypeObjet(swtPaTiersController.getClassModel());

						paramAfficheAideRecherche.setChampsIdentifiant(Const.C_ID_DTO_GENERAL);
					}
					if(getFocusCourantSWT().equals(vue.getTfCodeTTarif())){
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

						paramAfficheAideRecherche.setTypeEntite(TaTTarif.class);
						paramAfficheAideRecherche.setChampsRecherche(Const.C_CODE_T_TARIF);
						paramAfficheAideRecherche.setDebutRecherche(vue.getTfCodeTTarif().getText());
						paramAfficheAideRecherche.setControllerAppelant(SWTPaPrixController.this);
						ModelGeneralObjetEJB<TaTTarif,TaTTarifDTO> modelTTarif = new ModelGeneralObjetEJB<TaTTarif,TaTTarifDTO>(dao);
						paramAfficheAideRecherche.setModel(modelTTarif);
						paramAfficheAideRecherche.setTypeObjet(swtPaTypeTarifController.getClassModel());

						paramAfficheAideRecherche.setChampsIdentifiant(Const.C_ID_DTO_GENERAL);
					}
					break;
				default:
					break;
				}

//				if (paramAfficheAideRecherche.getJPQLQuery() != null) {

					PaAideRechercheSWT paAideRecherche1 = new PaAideRechercheSWT(s,
							SWT.NULL);
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

					// Ajout d'une recherche
					paAideController.addRecherche(paAideRechercheController1,
							paramAfficheAideRecherche.getTitreRecherche());

					// Parametrage de l'ecran d'aide principal
					ParamAfficheAideSWT paramAfficheAideSWT = new ParamAfficheAideSWT();
					ParamAfficheAide paramAfficheAide = new ParamAfficheAide();

					// enregistrement pour le retour de l'ecran d'aide
					paAideController.addRetourEcranListener(getThis());
					// affichage de l'ecran d'aide principal (+ ses recherches)

					dbc.getValidationStatusMap().removeMapChangeListener(changeListener);
					/*****************************************************************/
					paAideController.configPanel(paramAfficheAide);
					/*****************************************************************/
					dbc.getValidationStatusMap().addMapChangeListener(changeListener);

//				}

			} finally {
				VerrouInterface.setVerrouille(false);
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

		try {
			//IStatus s = null;
			
			IStatus resultat = new Status(IStatus.OK,ArticlesPlugin.PLUGIN_ID,"validateUIField champ : "+nomChamp!=null?nomChamp:"null"+" valeur : "+value!=null?value.toString():"valeur nulle"+" validation OK");
			
			int VALIDATION_CLIENT = 1;
			int VALIDATION_SERVEUR = 2;
			int VALIDATION_CLIENT_ET_SERVEUR = 3;
			
			//int TYPE_VALIDATION = VALIDATION_CLIENT;
			//int TYPE_VALIDATION = VALIDATION_SERVEUR;
			int TYPE_VALIDATION = VALIDATION_CLIENT_ET_SERVEUR;
			
			AbstractApplicationDAOClient<TaPrixDTO> a = new AbstractApplicationDAOClient<TaPrixDTO>();

			if(nomChamp.equals(Const.C_CODE_UNITE)) {
//				TaUniteDAO dao = new TaUniteDAO(getEm());
//
//				dao.setModeObjet(getDao().getModeObjet());
//				TaUnite f = new TaUnite();
//				PropertyUtils.setSimpleProperty(f, nomChamp, value);
//				s = dao.validate(f,nomChamp,validationContext);
//
//				if(s.getSeverity()!=IStatus.ERROR) {
//					f = dao.findByCode((String)value);
//					taPrix.setTaUnite(f);
//				}
//				dao = null;
				ITaUniteServiceRemote dao = new EJBLookup<ITaUniteServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_UNITE_SERVICE, ITaUniteServiceRemote.class.getName());
				TaUniteDTO dto = new TaUniteDTO();
				try {
					if(TYPE_VALIDATION==VALIDATION_CLIENT || TYPE_VALIDATION==VALIDATION_CLIENT_ET_SERVEUR) {
						AbstractApplicationDAOClient<TaUniteDTO> validationClient = new AbstractApplicationDAOClient<TaUniteDTO>();
						PropertyUtils.setSimpleProperty(dto, nomChamp, value);
						validationClient.validate(dto,nomChamp,ITaPrixServiceRemote.validationContext);
					}
					if(resultat==null && (TYPE_VALIDATION==VALIDATION_SERVEUR || TYPE_VALIDATION==VALIDATION_CLIENT_ET_SERVEUR)) {
						PropertyUtils.setSimpleProperty(dto, nomChamp, value);
						dao.validateDTOProperty(dto, nomChamp,ITaPrixServiceRemote.validationContext);
					}
				} catch(Exception e) {
					resultat = new Status(IStatus.ERROR,ArticlesPlugin.PLUGIN_ID, e.getMessage(), e);
				}
				if(resultat!=null && resultat.getSeverity()!=IStatus.ERROR ){
					TaUnite entity = new TaUnite();
					entity = dao.findByCode((String)value);
					taPrix.setTaUnite(entity);
				}
				dao = null;				
			} else {
//				TaPrix u = new TaPrix();
//				u.setTaArticle(masterEntity);
//				PropertyUtils.setSimpleProperty(u, nomChamp, value);
//
//				int change=0;
//				if(nomChamp.equals(Const.C_PRIX_PRIX)) {
//					u.setPrixttcPrix(((TaPrixDTO) selectedPrix.getValue()).getPrixttcPrix());
//					change =u.getPrixPrix().compareTo(((TaPrixDTO) selectedPrix.getValue()).getPrixPrix());
//				}
//				if(nomChamp.equals(Const.C_PRIXTTC_PRIX)) {
//					u.setPrixPrix(((TaPrixDTO) selectedPrix.getValue()).getPrixPrix());
//					change =u.getPrixttcPrix().compareTo(((TaPrixDTO) selectedPrix.getValue()).getPrixttcPrix());
//				}
//
//				if(((TaPrixDTO) selectedPrix.getValue()).getIdPrix()!=null) {
//					u.setIdPrix(((TaPrixDTO) selectedPrix.getValue()).getIdPrix());
//				}
//
//
//				s = dao.validate(u,nomChamp,ITaPrixServiceRemote.validationContext);
//				if(s.getSeverity()!=IStatus.ERROR && change!=0) {
//					((TaPrixDTO) selectedPrix.getValue()).setPrixPrix(u.getPrixPrix());				
//					((TaPrixDTO) selectedPrix.getValue()).setPrixttcPrix(u.getPrixttcPrix());
//				}
				int change=0;
				try {
					TaPrixDTO u = new TaPrixDTO();
					PropertyUtils.setSimpleProperty(u, nomChamp, value);
					dao.validateDTOProperty(u,nomChamp,ITaPrixServiceRemote.validationContext);
					
					if(nomChamp.equals(Const.C_PRIX_PRIX)) {
						u.setPrixttcPrix(((TaPrixDTO) selectedPrix.getValue()).getPrixttcPrix());
						change =u.getPrixPrix().compareTo(((TaPrixDTO) selectedPrix.getValue()).getPrixPrix());
					}
					if(nomChamp.equals(Const.C_PRIXTTC_PRIX)) {
						u.setPrixPrix(((TaPrixDTO) selectedPrix.getValue()).getPrixPrix());
						change =u.getPrixttcPrix().compareTo(((TaPrixDTO) selectedPrix.getValue()).getPrixttcPrix());
					}	
					if(((TaPrixDTO) selectedPrix.getValue()).getId()!=null) {
					u.setId(((TaPrixDTO) selectedPrix.getValue()).getId());
				}
					
				} catch(Exception e) {
					resultat = new Status(IStatus.ERROR,ArticlesPlugin.PLUGIN_ID, e.getMessage(), e);
				}
				if(resultat!=null && resultat.getSeverity()!=IStatus.ERROR && change!=0){
					PropertyUtils.setSimpleProperty(taPrix, nomChamp, value);
					taPrix.setTaArticle(masterEntity);
					if(nomChamp.equals(Const.C_PRIXTTC_PRIX)) {
						taPrix.majPrixTTC();
					}
					if(nomChamp.equals(Const.C_PRIX_PRIX)) {
						taPrix.majPrix();
					}
					((TaPrixDTO) selectedPrix.getValue()).setPrixPrix(taPrix.getPrixPrix());				
					((TaPrixDTO) selectedPrix.getValue()).setPrixttcPrix(taPrix.getPrixttcPrix());
				}
			}
			return resultat;
		} catch (Exception e) {
			logger.error("",e);
		}
		return null;
	}

	@Override
	protected void actEnregistrer() throws Exception {
//		EntityTransaction transaction = dao.getEntityManager().getTransaction();
		try {
			//TODO ejb, controle à remettre
//			ctrlUnChampsSWT(getFocusCourantSWT());
			ctrlTousLesChampsAvantEnregistrementSWT();
			
//			dao.begin(transaction);
			//TaPrix u = null;
			TaPrixMapper mapper = new TaPrixMapper();
			if ((modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION) == 0)) {
//				LgrDozerMapper<TaPrixDTO,TaPrix> mapper = new LgrDozerMapper<TaPrixDTO,TaPrix>();
//				mapper.map((TaPrixDTO) selectedPrix.getValue(),taPrix);
				mapper.mapDtoToEntity((TaPrixDTO) selectedPrix.getValue(), taPrix);

			} else if ((modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION) == 0)) {
//				LgrDozerMapper<TaPrixDTO,TaPrix> mapper = new LgrDozerMapper<TaPrixDTO,TaPrix>();
//				mapper.map((TaPrixDTO) selectedPrix.getValue(),taPrix);
				mapper.mapDtoToEntity((TaPrixDTO) selectedPrix.getValue(), taPrix);
				taPrix.setTaArticle(masterEntity);
				masterEntity.addPrix(taPrix);
				if(((TaPrixDTO) selectedPrix.getValue()).getIdRefPrix())
					masterEntity.setTaPrix(taPrix);				
			}

			try {
				if(!enregistreToutEnCours) {
					sortieChamps();
					fireEnregistreTout(new AnnuleToutEvent(this,true));
					DeclencheCommandeControllerEvent e = new DeclencheCommandeControllerEvent(this,C_COMMAND_GLOBAL_ENREGISTRER_ID);
					fireDeclencheCommandeController(e);
				}
			} catch (Exception e) {
				logger.error("",e);
			}		
			taPrix=masterEntity.getTaPrix();
			
			getModeEcran().setMode(EnumModeObjet.C_MO_CONSULTATION);//ejb
			
//			dao.commit(transaction);
			changementDeSelection();
			actRefresh();
//			transaction = null;


		} finally {
//			if(transaction!=null && transaction.isActive()) {
//				transaction.rollback();
//			}
			initEtatBouton();
		}
	}



	public void initEtatComposant() {
		try {
			if(taPrix!=null) {
				if(!taPrix.getTaTiers().isEmpty() || !taPrix.getTaTTarif().isEmpty()) {
					vue.getCbID_REF_PRIX().setSelection(false);
					vue.getCbID_REF_PRIX().setEnabled(false);
				} else {
					vue.getCbID_REF_PRIX().setEnabled(true);
				}
			}
			vue.getTfCODE_UNITE().setEditable(!isUtilise());
			changeCouleur(vue);
		} catch (Exception e) {
			vue.getLaMessage().setText(e.getMessage());
		}

	}
	public boolean isUtilise(){
		return (((TaPrixDTO)selectedPrix.getValue()).getIdArticle()!=null && 
				!dao.recordModifiable(dao.getNomTable(),
						((TaPrixDTO)selectedPrix.getValue()).getIdArticle()))||
						!masterDAO.autoriseModification(masterEntity);		
	}
	public TaPrixDTO getSwtOldPrix() {
		return swtOldPrix;
	}

	public void setSwtOldUnite(TaPrixDTO swtOldPrix) {
		this.swtOldPrix = swtOldPrix;
	}

	public void setSwtOldPrix() {
		if (selectedPrix.getValue() != null)
			this.swtOldPrix = TaPrixDTO.copy((TaPrixDTO) selectedPrix.getValue());
		else {
			if(tableViewer.selectionGrille(0)){
				this.swtOldPrix = TaPrixDTO.copy((TaPrixDTO) selectedPrix.getValue());
				tableViewer.setSelection(new StructuredSelection(
						(TaPrixDTO) selectedPrix.getValue()), true);
			}}
	}

	public void setVue(PaPrixSWT vue) {
		super.setVue(vue);
		this.vue = vue;
	}

	@Override
	protected void initMapComposantDecoratedField() {
		if (mapComposantDecoratedField == null)
			mapComposantDecoratedField = new LinkedHashMap<Control, ControlDecoration>();

		mapComposantDecoratedField.put(vue.getTfCODE_UNITE(), vue.getFieldCODE_UNITE());
		mapComposantDecoratedField.put(vue.getTfPRIX_PRIX(), vue.getFieldPRIX_PRIX());
		mapComposantDecoratedField.put(vue.getTfPRIXTTC_PRIX(), vue.getFieldPRIXTTC_PRIX());
		mapComposantDecoratedField.put(vue.getCbID_REF_PRIX(), vue.getFieldID_REF_PRIX());
	}

	public Class getClassModel() {
		return classModel;
	}



	@Override
	protected void sortieChamps() {
		if(selectedPrix!=null && selectedPrix.getValue()!=null){
			if(vue.getTfPRIX_PRIX().equals(getFocusCourantSWT())){
				if(((TaPrixDTO)selectedPrix.getValue()).getTauxTva()!=null)
					((TaPrixDTO)selectedPrix.getValue()).setPrixttcPrix(((TaPrixDTO)selectedPrix.getValue()).getPrixPrix().multiply(new BigDecimal(1).
							add(((TaPrixDTO)selectedPrix.getValue()).getTauxTva().divide(new BigDecimal(100)))));
				vue.getTfPRIXTTC_PRIX().setText(LibConversion.bigDecimalToString(((TaPrixDTO)selectedPrix.getValue()).getPrixttcPrix()));
			} else if(vue.getTfPRIXTTC_PRIX().equals(getFocusCourantSWT())){
				if(((TaPrixDTO)selectedPrix.getValue()).getTauxTva()!=null)
					((TaPrixDTO)selectedPrix.getValue()).setPrixPrix(((TaPrixDTO)selectedPrix.getValue()).getPrixttcPrix().divide(new BigDecimal(1).
							add(((TaPrixDTO)selectedPrix.getValue()).getTauxTva().divide(new BigDecimal(100))),MathContext.DECIMAL32));
				vue.getTfPRIX_PRIX().setText(LibConversion.bigDecimalToString(((TaPrixDTO)selectedPrix.getValue()).getPrixPrix()));
			}
		}		
	}

	@Override
	protected void actRefresh() throws Exception {
		//repositionnement sur la valeur courante
		int idActuel = 0;
		if (taPrix!=null) { //enregistrement en cours de modification/insertion
			idActuel = taPrix.getIdPrix();
		} else if(selectedPrix!=null && (TaPrixDTO) selectedPrix.getValue()!=null) {
			idActuel = ((TaPrixDTO) selectedPrix.getValue()).getId();
		}

		//rafraichissement des valeurs dans la grille
		writableList = new WritableList(realm, IHMmodel(), classModel);
		tableViewer.setInput(writableList);
		
		if(idActuel!=0) {
			tableViewer.setSelection(new StructuredSelection(recherche(Const.C_ID_DTO_GENERAL, idActuel)));
		}else
			tableViewer.selectionGrille(0);				
	}
	
	private void refreshCiblePrix() {
		writableListCible = new WritableList(realm, IHMmodelCible(), TaCiblePrixDTO.class);
		tableViewerCible.setInput(writableListCible);
	}
	
	protected class HandlerAjouterCiblePrix extends LgrAbstractHandler {

		public Object execute(ExecutionEvent event) throws ExecutionException{
			try {
				System.out.println("SWTPaPrixController.HandlerAjouterCiblePrix.execute()");
				
				//verif tiers ou type tarif
				//tiers
				String codeTiers = vue.getTfCodeTiers().getText();
				if(!"".equals(codeTiers)) {
					if(!masterEntity.possedeDejaUnPrixPourCeTiers(codeTiers)) {
//						TaTiersDAO taTiersDAO = new TaTiersDAO(getEm());
						ITaTiersServiceRemote taTiersDAO = new EJBLookup<ITaTiersServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_TIERS_SERVICE, ITaTiersServiceRemote.class.getName());
						TaTiers tiers = taTiersDAO.findByCode(codeTiers);
						if(tiers!=null) {
							taPrix.addTaTiers(tiers);
						}
					} else {
						logger.info("L'article possède déjà un prix pour ce tiers");
					}
				}
				
				//type tarif
				String codeTTarif = vue.getTfCodeTTarif().getText();
				if(!"".equals(codeTTarif)) {
					if(!masterEntity.possedeDejaUnPrixPourCeTypeDeTarif(codeTTarif)) {
						ITaTTarifServiceRemote taTTarifDAO = new EJBLookup<ITaTTarifServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_T_TARIF_SERVICE, ITaTTarifServiceRemote.class.getName());
//						TaTTarifDAO taTTarifDAO = new TaTTarifDAO(getEm());
						TaTTarif tTarif = taTTarifDAO.findByCode(codeTTarif);
						if(tTarif!=null) {
							taPrix.addTaTTarif(tTarif);
						}
					} else {
						logger.info("L'article possède déjà un prix pour ce type de tarif");
					}
				}
				
				refreshCiblePrix();
				
			} catch (Exception e1) {
				logger.error("Erreur : actionPerformed", e1);
			}
			return event;
		}
	}
	
	protected class HandlerSupprimerCiblePrix extends LgrAbstractHandler {

		public Object execute(ExecutionEvent event) throws ExecutionException{
			try {
				System.out.println("SWTPaPrixController.HandlerSupprimerCiblePrix.execute()");

				if(selectedCiblePrix!=null && selectedCiblePrix.getValue()!=null) {
					//verif tiers ou type tarif
					//tiers
					if(((TaCiblePrixDTO)selectedCiblePrix.getValue()).getType().equals(TaCiblePrixDTO.TYPE_TIERS)) {
						String codeTiers = ((TaCiblePrixDTO)selectedCiblePrix.getValue()).getCode();
						if(!"".equals(codeTiers)) {
							ITaTiersServiceRemote taTiersDAO = new EJBLookup<ITaTiersServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_TIERS_SERVICE, ITaTiersServiceRemote.class.getName());
							TaTiers tiers = taTiersDAO.findByCode(codeTiers);
							if(tiers!=null) {
								taPrix.removeTaTiers(tiers);
							}
						}
					} else if(((TaCiblePrixDTO)selectedCiblePrix.getValue()).getType().equals(TaCiblePrixDTO.TYPE_TARIF)) {
						//type tarif
						String codeTTarif = ((TaCiblePrixDTO)selectedCiblePrix.getValue()).getCode();
						if(!"".equals(codeTTarif)) {
//							TaTTarifDAO taTTarifDAO = new TaTTarifDAO(getEm());
							ITaTTarifServiceRemote taTTarifDAO = new EJBLookup<ITaTTarifServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_T_TARIF_SERVICE, ITaTTarifServiceRemote.class.getName());
							TaTTarif tTarif = taTTarifDAO.findByCode(codeTTarif);
							if(tTarif!=null) {
								taPrix.removeTaTTarif(tTarif);
							}
						}
					}
				}
				
				refreshCiblePrix();

			} catch (Exception e1) {
				logger.error("Erreur : actionPerformed", e1);
			}
			return event;
		}
	}
	
	protected void initImageBouton() {
		super.initImageBouton();
//		vue.getBtnSupprimerCible().setImage(LibrairiesEcranPlugin.ir.get(LibrairiesEcranPlugin.IMAGE_SUPPRIMER));
//		vue.getBtnAjouter().setImage(LibrairiesEcranPlugin.ir.get(LibrairiesEcranPlugin.IMAGE_INSERER));
	}


	public	ModelGeneralObjetEJB<TaPrix,TaPrixDTO> getModelPrix() {
		return modelPrix;
	}

	public TaArticle getMasterEntity() {
		return masterEntity;
	}

	public void setMasterEntity(TaArticle taArticle) {
		this.masterEntity = taArticle;
	}

	public ITaArticleServiceRemote getMasterDAO() {
		return masterDAO;
	}

	public void setMasterDAO(ITaArticleServiceRemote masterDAO) {
		this.masterDAO = masterDAO;
	}

	public TaPrix getTaPrix() {
		return taPrix;
	}

	public ITaPrixServiceRemote getDao() {
		return dao;
	}


	public Object recherche(String propertyName, Object value) {
		boolean trouve = false;
		int i = 0;
		while(!trouve && i<IHMmodel().size()){
			try {
				if(PropertyUtils.getProperty(IHMmodel().get(i), propertyName).equals(value)) {
					trouve = true;
				} else {
					i++;
				}
			} catch (IllegalAccessException e) {
				logger.error("",e);
			} catch (InvocationTargetException e) {
				logger.error("",e);
			} catch (NoSuchMethodException e) {
				logger.error("",e);
			}
		}

		if(trouve)
			return IHMmodel().get(i);
		else 
			return null;

	}

	public boolean changementPageValide(){
		if (getModeEcran().getMode().compareTo(EnumModeObjet.C_MO_EDITION) == 0
				|| getModeEcran().getMode().compareTo(EnumModeObjet.C_MO_INSERTION) == 0) {
			//mise a jour de l'entite principale
			if(taPrix!=null && selectedPrix!=null && ((TaPrixDTO) selectedPrix.getValue())!=null) {
				LgrDozerMapper<TaPrixDTO,TaPrix> mapper = new LgrDozerMapper<TaPrixDTO,TaPrix>();
				mapper.map((TaPrixDTO) selectedPrix.getValue(),taPrix);
			}
		}
		//		dao.getModeObjet().setMode(EnumModeObjet.C_MO_CONSULTATION);
		//		initEtatBouton();
		return true;
	};

}
