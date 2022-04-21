package fr.legrain.tiers.ecran;

import java.lang.reflect.InvocationTargetException;
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

import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.model.mapping.mapper.TaCompteBanqueMapper;
import fr.legrain.bdg.tiers.service.remote.ITaCompteBanqueServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTBanqueServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.gestCom.Appli.Const;
//import fr.legrain.gestCom.Appli.LgrDozerMapper;
import fr.legrain.gestCom.Appli.ModelGeneralObjetEJB;
import fr.legrain.gestCom.librairiesEcran.editor.EditorAide;
import fr.legrain.gestCom.librairiesEcran.editor.EditorInputAide;
import fr.legrain.gestCom.librairiesEcran.swt.DeclencheCommandeControllerEvent;
import fr.legrain.gestCom.librairiesEcran.swt.EJBBaseControllerSWTStandard;
import fr.legrain.gestCom.librairiesEcran.swt.IDetailController;
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
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.lib.data.InfosVerifSaisie;
import fr.legrain.lib.data.ModeObjet;
import fr.legrain.lib.data.EnumModeObjet;
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
import fr.legrain.tiers.TiersPlugin;
import fr.legrain.tiers.clientutility.JNDILookupClass;
import fr.legrain.tiers.dto.TaCompteBanqueDTO;
import fr.legrain.tiers.dto.TaTBanqueDTO;
import fr.legrain.tiers.editor.EditorBanque;
import fr.legrain.tiers.editor.EditorInputBanque;
import fr.legrain.tiers.editor.EditorInputTypeBanque;
import fr.legrain.tiers.editor.EditorTypeBanque;
import fr.legrain.tiers.model.TaCompteBanque;
import fr.legrain.tiers.model.TaTBanque;
import fr.legrain.tiers.model.TaTiers;

public class SWTPaBanqueController extends EJBBaseControllerSWTStandard
implements RetourEcranListener,IDetailController {

	static Logger logger = Logger.getLogger(SWTPaBanqueController.class.getName());
	private PaBanqueSWT vue = null;
	private ITaCompteBanqueServiceRemote dao = null;//new TaCompteBanqueDAO();

	private Object ecranAppelant = null;
	private TaCompteBanqueDTO swtCompteBanque;
	private TaCompteBanqueDTO swtOldCompteBanque;
	private Realm realm;
	private DataBindingContext dbc;



	private Class classModel = TaCompteBanqueDTO.class;
	private ModelGeneralObjetEJB<TaCompteBanque,TaCompteBanqueDTO> modelBanque = null;// new ModelGeneralObjet<TaCompteBanqueDTO,TaCompteBanqueDAO,TaCompteBanque>(dao,classModel);

	private LgrTableViewer tableViewer;
	private WritableList writableList;
	private IObservableValue selectedCompteBanque;
	private String[] listeChamp;
	private String nomClassController = this.getClass().getSimpleName();

	private TaTiers masterEntity = null;
	private ITaTiersServiceRemote masterDAO = null;

	private LgrDozerMapper<TaCompteBanqueDTO,TaCompteBanque> mapperUIToModel  = new LgrDozerMapper<TaCompteBanqueDTO,TaCompteBanque>();
	private TaCompteBanque taCompteBanque = null;

	private MapChangeListener changeListener = new MapChangeListener();

	private Integer idTiers = null;

	public SWTPaBanqueController(PaBanqueSWT vue) {
		this(vue,null);
	}

	public SWTPaBanqueController(PaBanqueSWT vue,EntityManager em) {
//		if(em!=null) {
//			setEm(em);
//		}
//		dao = new TaCompteBanqueDAO(getEm());
		try {
			dao = new EJBLookup<ITaCompteBanqueServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_COMPTE_BANQUE_SERVICE, ITaCompteBanqueServiceRemote.class.getName());
		} catch (NamingException e1) {
			e1.printStackTrace();
		}
		modelBanque = new ModelGeneralObjetEJB<TaCompteBanque,TaCompteBanqueDTO>(dao);
		setVue(vue);

		// a faire avant l'initialisation du Binding
		vue.getGrille().addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setSwtOldCompteBanque();
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
			//setIbTaTableStandard(ibTaTable);
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
			Menu[] tabPopups = new Menu[] { popupMenuFormulaire,
					popupMenuGrille };
			this.initPopupAndButtons(mapActions, tabPopups);
			vue.getPaCorpsFormulaire().setMenu(popupMenuFormulaire);
			vue.getPaGrille().setMenu(popupMenuGrille);

			initEtatBouton();
		} catch (Exception e) {
			vue.getLaMessage().setText(e.getMessage());
			logger.error("Erreur : PaTiersController", e);
		}
	}

	/**
	 * Creation des objet pour l'interface, a partir de l'entite principale.<br>
	 * @return
	 */
	public List<TaCompteBanqueDTO> IHMmodel() {
		LinkedList<TaCompteBanque> ldao = new LinkedList<TaCompteBanque>();
		LinkedList<TaCompteBanqueDTO> lswt = new LinkedList<TaCompteBanqueDTO>();

		if(masterEntity!=null && !masterEntity.getTaCompteBanques().isEmpty()) {

			ldao.addAll(masterEntity.getTaCompteBanques());

			//LgrDozerMapper<TaCompteBanque,TaCompteBanqueDTO> mapper = new LgrDozerMapper<TaCompteBanque,TaCompteBanqueDTO>();
			TaCompteBanqueMapper mapper = new TaCompteBanqueMapper();
			for (TaCompteBanque o : ldao) {
				TaCompteBanqueDTO t = null;
				//t = (TaCompteBanqueDTO) mapper.map(o, TaCompteBanqueDTO.class);
				t = (TaCompteBanqueDTO) mapper.mapEntityToDto(o, null);
				lswt.add(t);
			}

		}
		return lswt;
	}

	protected void initEtatBouton() {
		initEtatBouton(IHMmodel());
	}

	public void bind(PaBanqueSWT paBanqueSWT) {
		try {
			//modelBanque = new ModelTypeTiers(ibTaTable);
			//			 modelBanque = new ModelGeneral<SWTBanque>(ibTaTable.getFIBQuery(),classModel);
			realm = SWTObservables.getRealm(vue.getParent().getDisplay());

			tableViewer = new LgrTableViewer(paBanqueSWT.getGrille());
			tableViewer.createTableCol(classModel,paBanqueSWT.getGrille(), nomClassController,
					Const.C_FICHIER_LISTE_CHAMP_GRILLE,0);
			listeChamp = tableViewer.setListeChampGrille(nomClassController,
					Const.C_FICHIER_LISTE_CHAMP_GRILLE);

			//			ObservableListContentProvider viewerContent = new ObservableListContentProvider();
			//			tableViewer.setContentProvider(viewerContent);
			//
			//			IObservableMap[] attributeMaps = BeansObservables.observeMaps(
			//					viewerContent.getKnownElements(), classModel,
			//					listeChamp);
			//
			//			tableViewer.setLabelProvider(new ObservableMapLabelProvider(attributeMaps));
			//			List<TaCompteBanqueDTO> lswt = IHMmodel();
			//			//writableList = new WritableList(realm, modelBanque.remplirListe(), classModel);
			//			writableList = new WritableList(realm, lswt, classModel);
			//			tableViewer.setInput(writableList);

			// Set up data binding.
			LgrViewerSupport.bind(tableViewer, 
					new WritableList(IHMmodel(), classModel),
					BeanProperties.values(listeChamp)
			);

			selectedCompteBanque = ViewersObservables.observeSingleSelection(tableViewer);

			dbc = new DataBindingContext(realm);

			dbc.getValidationStatusMap().addMapChangeListener(changeListener);
			tableViewer.selectionGrille(0);

			setTableViewerStandard(tableViewer);
			setDbcStandard(dbc);

			bindingFormMaitreDetail(dbc, realm, selectedCompteBanque,classModel);
			changementDeSelection();
			selectedCompteBanque.addChangeListener(new IChangeListener() {

				public void handleChange(ChangeEvent event) {
					changementDeSelection();
				}

			});

		} catch (Exception e) {
			vue.getLaMessage().setText(e.getMessage());
			logger.error("", e);
		}
	}

	private void changementDeSelection() {
		if(selectedCompteBanque!=null && selectedCompteBanque.getValue()!=null) {
			if(((TaCompteBanqueDTO) selectedCompteBanque.getValue()).getId()!=null) {
				try {
					taCompteBanque = dao.findById(((TaCompteBanqueDTO) selectedCompteBanque.getValue()).getId());
				} catch (FinderException e) {
					logger.error("",e);
				}
			}
			//null a tester ailleurs, car peut etre null en cas d'insertion
			fireChangementDeSelection(new ChangementDeSelectionEvent(SWTPaBanqueController.this));
		}
	}

	public Composite getVue() {
		return vue;
	}

	public ResultAffiche configPanel(ParamAffiche param) {
		if (param != null) {
			Map<String,String[]> map = dao.getParamWhereSQL();
			//ibTaTable.ouvreDataset();
			if (((ParamAfficheBanque) param).getFocusDefautSWT() != null && !((ParamAfficheBanque) param).getFocusDefautSWT().isDisposed())
				((ParamAfficheBanque) param).getFocusDefautSWT().setFocus();
			else
				((ParamAfficheBanque) param).setFocusDefautSWT(vue
						.getGrille());
			vue.getLaTitreFormulaire().setText(
					((ParamAfficheBanque) param).getTitreFormulaire());
			vue.getLaTitreGrille().setText(
					((ParamAfficheBanque) param).getTitreGrille());
			vue.getLaTitreFenetre().setText(
					((ParamAfficheBanque) param).getSousTitre());


			if (param.getEcranAppelant() != null) {
				ecranAppelant = param.getEcranAppelant();
			}
			if(tableViewer==null) {
				//databinding pas encore realise
				bind(vue);
				tableViewer.tri(classModel, nomClassController,Const.C_FICHIER_LISTE_CHAMP_GRILLE);
			} else {
				try {
					taCompteBanque=null;
					selectedCompteBanque.setValue(null);
					actRefresh();
				} catch (Exception e) {
					logger.error("",e);
				}
			}

			// permet de se positionner sur le 1er enregistrement et de remplir
			// le formulaire
			//			tableViewer.tri(classModel, nomClassController,Const.C_FICHIER_LISTE_CHAMP_GRILLE);
			VerrouInterface.setVerrouille(false);
			setSwtOldCompteBanque();

			if (param.getModeEcran() != null
					&& param.getModeEcran().compareTo(EnumModeObjet.C_MO_INSERTION) == 0) {
				try {
					actInserer();
				} catch (Exception e) {
					vue.getLaMessage().setText(e.getMessage());
					logger.error("", e);
				}
			}

			//param.setFocus(ibTaTable.getFModeObjet().getFocusCourant());
		}
		initEtatBouton();
		return null;
	}

	protected void initVerifySaisie() {
		if (mapInfosVerifSaisie == null)
			mapInfosVerifSaisie = new HashMap<Control, InfosVerifSaisie>();
		
		mapInfosVerifSaisie.put(vue.getTfADRESSE1_BANQUE(), new InfosVerifSaisie(new TaCompteBanque(),Const.C_ADRESSE1_BANQUE,null));
		mapInfosVerifSaisie.put(vue.getTfADRESSE2_BANQUE(), new InfosVerifSaisie(new TaCompteBanque(),Const.C_ADRESSE2_BANQUE,null));
		mapInfosVerifSaisie.put(vue.getTfCLE_RIB(), new InfosVerifSaisie(new TaCompteBanque(),Const.C_CLE_RIB_BANQUE,null));
		mapInfosVerifSaisie.put(vue.getTfCODE_B_I_C(), new InfosVerifSaisie(new TaCompteBanque(),Const.C_CODE_B_I_C_BANQUE,null));
		mapInfosVerifSaisie.put(vue.getTfCODE_BANQUE(), new InfosVerifSaisie(new TaCompteBanque(),Const.C_CODE_BANQUE,null));
		mapInfosVerifSaisie.put(vue.getTfCODE_GUICHET(), new InfosVerifSaisie(new TaCompteBanque(),Const.C_CODE_GUICHET_BANQUE,null));
		mapInfosVerifSaisie.put(vue.getTfCODE_T_BANQUE(), new InfosVerifSaisie(new TaTBanque(),Const.C_CODE_T_BANQUE,null));
		mapInfosVerifSaisie.put(vue.getTfCOMPTE(), new InfosVerifSaisie(new TaCompteBanque(),Const.C_COMPTE_BANQUE,null));
		mapInfosVerifSaisie.put(vue.getTfCP_BANQUE(), new InfosVerifSaisie(new TaCompteBanque(),Const.C_CP_BANQUE,null));
		mapInfosVerifSaisie.put(vue.getTfIBAN(), new InfosVerifSaisie(new TaCompteBanque(),Const.C_IBAN_BANQUE,null));
		mapInfosVerifSaisie.put(vue.getTfNOM_BANQUE(), new InfosVerifSaisie(new TaCompteBanque(),Const.C_NOM_BANQUE,null));
		mapInfosVerifSaisie.put(vue.getTfNOM_COMPTE(), new InfosVerifSaisie(new TaCompteBanque(),Const.C_NOM_COMPTE,null));
		mapInfosVerifSaisie.put(vue.getTfTITULAIRE(), new InfosVerifSaisie(new TaCompteBanque(),Const.C_TITULAIRE_BANQUE,null));
		mapInfosVerifSaisie.put(vue.getTfVILLE_BANQUE(), new InfosVerifSaisie(new TaCompteBanque(),Const.C_VILLE_BANQUE,null));
		mapInfosVerifSaisie.put(vue.getTfCPTCOMPTABLE(), new InfosVerifSaisie(new TaCompteBanque(),Const.C_CPTCOMPTABLE,null));

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

		vue.getTfNOM_BANQUE().setToolTipText(Const.C_NOM_BANQUE);
		vue.getTfNOM_COMPTE().setToolTipText(Const.C_NOM_COMPTE);
		vue.getTfCOMPTE().setToolTipText(Const.C_COMPTE_BANQUE);
		vue.getTfCODE_BANQUE().setToolTipText(Const.C_CODE_BANQUE);
		vue.getTfCODE_GUICHET().setToolTipText(Const.C_CODE_GUICHET_BANQUE);
		vue.getTfCLE_RIB().setToolTipText(Const.C_CLE_RIB_BANQUE);
		vue.getTfADRESSE1_BANQUE().setToolTipText(Const.C_ADRESSE1_BANQUE);
		vue.getTfADRESSE2_BANQUE().setToolTipText(Const.C_ADRESSE2_BANQUE);
		vue.getTfCP_BANQUE().setToolTipText(Const.C_CP_BANQUE);
		vue.getTfVILLE_BANQUE().setToolTipText(Const.C_VILLE_BANQUE);
		vue.getTfIBAN().setToolTipText(Const.C_IBAN_BANQUE);
		vue.getTfCODE_B_I_C().setToolTipText(Const.C_CODE_B_I_C_BANQUE);
		vue.getTfTITULAIRE().setToolTipText(Const.C_TITULAIRE_BANQUE);
		vue.getTfCODE_T_BANQUE().setToolTipText(Const.C_CODE_T_BANQUE);
		vue.getTfCPTCOMPTABLE().setToolTipText(Const.C_CPTCOMPTABLE);

		//
		mapComposantChamps.put(vue.getTfTITULAIRE(),Const.C_TITULAIRE_BANQUE);
		mapComposantChamps.put(vue.getTfCODE_T_BANQUE(),Const.C_CODE_T_BANQUE);
		mapComposantChamps.put(vue.getTfCPTCOMPTABLE(),Const.C_CPTCOMPTABLE);

		mapComposantChamps.put(vue.getTfCODE_BANQUE(),Const.C_CODE_BANQUE);
		mapComposantChamps.put(vue.getTfCODE_GUICHET(), Const.C_CODE_GUICHET_BANQUE);
		mapComposantChamps.put(vue.getTfCOMPTE(), Const.C_COMPTE_BANQUE);
		mapComposantChamps.put(vue.getTfCLE_RIB(), Const.C_CLE_RIB_BANQUE);

		
		mapComposantChamps.put(vue.getTfNOM_BANQUE(), Const.C_NOM_BANQUE);
		mapComposantChamps.put(vue.getTfNOM_COMPTE(), Const.C_NOM_COMPTE);
		mapComposantChamps.put(vue.getTfADRESSE1_BANQUE(),Const.C_ADRESSE1_BANQUE);
		mapComposantChamps.put(vue.getTfADRESSE2_BANQUE(), Const.C_ADRESSE2_BANQUE);
		mapComposantChamps.put(vue.getTfCP_BANQUE(), Const.C_CP_BANQUE);
		mapComposantChamps.put(vue.getTfVILLE_BANQUE(),Const.C_VILLE_BANQUE);
		
		mapComposantChamps.put(vue.getTfIBAN(), Const.C_IBAN_BANQUE);
		mapComposantChamps.put(vue.getTfCODE_B_I_C(), Const.C_CODE_B_I_C_BANQUE);

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

		if (mapInitFocus == null)
			mapInitFocus = new LinkedHashMap<EnumModeObjet, Control>();
		mapInitFocus.put(EnumModeObjet.C_MO_INSERTION, vue
				.getTfTITULAIRE());
		mapInitFocus.put(EnumModeObjet.C_MO_EDITION, vue
				.getTfTITULAIRE());
		mapInitFocus.put(EnumModeObjet.C_MO_CONSULTATION, vue
				.getGrille());

		activeModifytListener();
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

		Object[] tabActionsAutres = new Object[] { C_COMMAND_GLOBAL_AIDE_ID, C_COMMAND_GLOBAL_REFRESH_ID };
		mapActions.put(null, tabActionsAutres);

	}

	public SWTPaBanqueController getThis() {
		return this;
	}

	@Override
	public boolean onClose() throws ExceptLgr {
		boolean retour = true;
		VerrouInterface.setVerrouille(true);
		//switch (ibTaTable.getFModeObjet().getMode()) {
		switch (getModeEcran().getMode()) {
		case C_MO_INSERTION:
		case C_MO_EDITION:
			if (MessageDialog.openQuestion(vue.getShell(), MessagesEcran
					.getString("Message.Attention"), MessagesEcran
					.getString("CompteBanque.Message.Enregistrer"))) {

				try {
					actEnregistrer();
				} catch (Exception e) {
					vue.getLaMessage().setText(e.getMessage());
					logger.error("", e);
				}
			} else {
				fireAnnuleTout(new AnnuleToutEvent(this,true));
			}
			//ibTaTable.annuler();

			break;
		case C_MO_CONSULTATION:
			break;
		default:
			break;
		}

		if (retour) {
			if (ecranAppelant instanceof SWTPaAideControllerSWT) {
				setListeEntity(getModelBanque().remplirListe());
				dao.initValeurIdTable(taCompteBanque);
				//				fireRetourEcran(new RetourEcranEvent(this, new ResultAide(
				//						ibTaTable.champIdTable, ibTaTable.valeurIdTable,
				//						selectedBanque.getValue())));
				fireRetourEcran(new RetourEcranEvent(this, new ResultAide(
						dao.getChampIdEntite(), dao.getValeurIdTable(),
						selectedCompteBanque.getValue())));
				//				vue.getDisplay().asyncExec(new Runnable() {
				//					public void run() {
				//						vue.getShell().setVisible(false);
				//					}
				//				});
				//				retour = false;
				retour = true;
			}
		}
		//		if (retour && !(ecranAppelant instanceof SWTPaAideControllerSWT)) {
		//			fireDestroy(new DestroyEvent(ibTaTable));
		//		}
		return retour;
	}

	public void retourEcran(final RetourEcranEvent evt) {
		if (evt.getRetour() != null
				&& (evt.getSource() instanceof SWTPaAideControllerSWT)) {
			if (getFocusAvantAideSWT() instanceof Text) {
				try {
					((Text) getFocusAvantAideSWT()).setText(((ResultAffiche) evt
							.getRetour()).getResult());			
					if(getFocusAvantAideSWT().equals(vue.getTfCODE_T_BANQUE())){
						TaTBanque u = null;
						//TaTBanqueDAO t = new TaTBanqueDAO(getEm());
						ITaTBanqueServiceRemote t = new EJBLookup<ITaTBanqueServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_T_BANQUE_SERVICE, ITaTBanqueServiceRemote.class.getName());
						u = t.findById(Integer.parseInt(((ResultAffiche) evt.getRetour()).getIdResult()));
						t = null;
						taCompteBanque.setTaTBanque(u);
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
			setSwtOldCompteBanque();
			if(LgrMess.isDOSSIER_EN_RESEAU()){
				DeclencheCommandeControllerEvent e = new DeclencheCommandeControllerEvent(this,C_COMMAND_GLOBAL_MODIFIER_ID);
				fireDeclencheCommandeController(e);
				continuer = getMasterModeEcran().dataSetEnModif();
			}
			if(continuer){
				swtCompteBanque = new TaCompteBanqueDTO();
				swtCompteBanque.setIdTiers(idTiers);
				String titulaireDefaut = "";
				if(masterEntity.getTaTCivilite()!=null && masterEntity.getTaTCivilite().getCodeTCivilite()!=null) {
					titulaireDefaut += masterEntity.getTaTCivilite().getCodeTCivilite();
				} else if(masterEntity.getTaTEntite()!=null && masterEntity.getTaTEntite().getCodeTEntite()!=null) {
					titulaireDefaut += masterEntity.getTaTEntite().getCodeTEntite();
				}
				if(masterEntity.getNomTiers()!=null) {
					titulaireDefaut += " "+masterEntity.getNomTiers();
				}
				if(masterEntity.getPrenomTiers()!=null) {
					titulaireDefaut += " "+masterEntity.getPrenomTiers();
				}
				swtCompteBanque.setTitulaire(titulaireDefaut);
//				dao.getModeObjet().setMode(EnumModeObjet.C_MO_INSERTION);
				taCompteBanque = new TaCompteBanque();
				List l = IHMmodel();
				l.add(swtCompteBanque);
				writableList = new WritableList(realm, l, classModel);
				tableViewer.setInput(writableList);
				tableViewer.refresh();
				tableViewer.setSelection(new StructuredSelection(swtCompteBanque));
				
				getModeEcran().setMode(EnumModeObjet.C_MO_INSERTION);//ejb
				initEtatBouton();

				try {
					DeclencheCommandeControllerEvent e = new DeclencheCommandeControllerEvent(this,C_COMMAND_GLOBAL_MODIFIER_ID);
					fireDeclencheCommandeController(e);
				} catch (Exception e) {
					logger.error("",e);
				}
			}
		} catch (Exception e1) {
			vue.getLaMessage().setText(e1.getMessage());
			logger.error("Erreur : actionPerformed", e1);
		} finally {
			initEtatComposant();
			VerrouInterface.setVerrouille(false);
		}
	}

	@Override
	protected void actModifier() throws Exception {
		try {
			boolean continuer=true;
			setSwtOldCompteBanque();
			if(LgrMess.isDOSSIER_EN_RESEAU()){
				DeclencheCommandeControllerEvent e = new DeclencheCommandeControllerEvent(this,C_COMMAND_GLOBAL_MODIFIER_ID);
				fireDeclencheCommandeController(e);
				continuer = getMasterModeEcran().dataSetEnModif();
			}
			if(continuer){
				for (TaCompteBanque p : masterEntity.getTaCompteBanques()) {
					if(p.getIdCompteBanque()==((TaCompteBanqueDTO) selectedCompteBanque.getValue()).getId()) {
						taCompteBanque = p;
//						if(LgrMess.isDOSSIER_EN_RESEAU())actRefresh();
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
					continuer = getMasterModeEcran().dataSetEnModif();
				}
				
				if(continuer){
//					setMasterEntity(masterDAO.findById(masterEntity.getIdTiers()));
					if (MessageDialog.openConfirm(vue.getShell(), MessagesEcran
							.getString("Message.Attention"), MessagesEcran
							.getString("CompteBanque.Message.Supprimer"))) {				
//						dao.getModeObjet().setMode(EnumModeObjet.C_MO_SUPPRESSION);
						try {
							DeclencheCommandeControllerEvent e = new DeclencheCommandeControllerEvent(this,C_COMMAND_GLOBAL_MODIFIER_ID);
							fireDeclencheCommandeController(e);
						} catch (Exception e) {
							logger.error("",e);
						}
						for (TaCompteBanque p : masterEntity.getTaCompteBanques()) {
							if(p.getIdCompteBanque()==((TaCompteBanqueDTO) selectedCompteBanque.getValue()).getId()) {
								taCompteBanque = p;
							}
						}
//						dao.begin(transaction);
//						dao.supprimer(taCompteBanque); //ejb commentaire
						taCompteBanque.setTaTiers(null);
						masterEntity.removeCompteBanque(taCompteBanque);
						Object suivant=tableViewer.getElementAt(tableViewer.getTable().getSelectionIndex()+1);
//						dao.commit(transaction);
						modelBanque.removeEntity(taCompteBanque);
						taCompteBanque=null;
//						transaction=null;
						if(suivant!=null)tableViewer.setSelection(new StructuredSelection(suivant),true);
						else tableViewer.selectionGrille(0);
						try {
							DeclencheCommandeControllerEvent e = new DeclencheCommandeControllerEvent(this,C_COMMAND_GLOBAL_ENREGISTRER_ID);
							fireDeclencheCommandeController(e,false);
						} catch (Exception e) {
							logger.error("",e);
						}
						
						modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
						actRefresh();		
						initEtatBouton();

					}
			}
		} catch (ExceptLgr e1) {
			vue.getLaMessage().setText(e1.getMessage());
			logger.error("Erreur : actionPerformed", e1);
		} finally {
//			if(transaction!=null && transaction.isActive()) {
//				transaction.rollback();
//			}
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
		try {
			VerrouInterface.setVerrouille(true);
			switch (getModeEcran().getMode()) {
			case C_MO_INSERTION:
				if (silencieu || (!silencieu && MessageDialog.openQuestion(vue.getShell(), MessagesEcran
						.getString("Message.Attention"), MessagesEcran
						.getString("CompteBanque.Message.Annuler")))) {
					remetTousLesChampsApresAnnulationSWT(dbc);
					actRefresh();
					
					getModeEcran().setMode(EnumModeObjet.C_MO_CONSULTATION);//ejb
					
					hideDecoratedFields();
					if(!annuleToutEnCours) {
						fireAnnuleTout(new AnnuleToutEvent(this));
					}
				}
				initEtatBouton();
				break;
			case C_MO_EDITION:
				if (silencieu || (!silencieu && MessageDialog.openQuestion(vue.getShell(), MessagesEcran
						.getString("Message.Attention"), MessagesEcran
						.getString("CompteBanque.Message.Annuler")))) {
					int rang = ((WritableList)tableViewer.getInput()).indexOf(selectedCompteBanque.getValue());
					List<TaCompteBanqueDTO> l = IHMmodel();
					if(rang!=-1)l.set(rang, swtOldCompteBanque);
					remetTousLesChampsApresAnnulationSWT(dbc);
					writableList = new WritableList(realm, l, classModel);
					tableViewer.setInput(writableList);
					tableViewer.refresh();
					tableViewer.setSelection(new StructuredSelection(swtOldCompteBanque), true);
					//remetTousLesChampsApresAnnulationSWT(dbc);
					
					getModeEcran().setMode(EnumModeObjet.C_MO_CONSULTATION);//ejb

					hideDecoratedFields();
					if(!annuleToutEnCours) {
						fireAnnuleTout(new AnnuleToutEvent(this));
					}
				}
				initEtatBouton();

				break;
			case C_MO_CONSULTATION:
				//				actionFermer.run();
				fireChangementDePage(new ChangementDePageEvent(this,ChangementDePageEvent.DEBUT));
				break;
			default:
				break;
			}
			// getFocusCourant().setInputVerifier(inputVerifier);
			// initEtatBouton();
		} finally {
			VerrouInterface.setVerrouille(false);
		}
	}

	@Override
	protected void actImprimer() throws Exception {
//passage ejb
//		TaInfoEntrepriseDAO taInfoEntrepriseDAO = new TaInfoEntrepriseDAO(getEm());
//		TaInfoEntreprise taInfoEntreprise = taInfoEntrepriseDAO.findInstance();
//
//		String nomChampIdTable =  dao.getChampIdTable();
//
//		FonctionGetInfosXmlAndProperties fonctionGetInfosXmlAndProperties = new FonctionGetInfosXmlAndProperties(mapperUIToModel);
//		fonctionGetInfosXmlAndProperties.cleanValueMapAttributeTable();
//
//		fonctionGetInfosXmlAndProperties.setValueMapAttributeTable(TaCompteBanque.class.getSimpleName()+".head");
//		fonctionGetInfosXmlAndProperties.setValueMapAttributeTable(TaCompteBanque.class.getSimpleName()+".detail");
//
//
//		LinkedHashMap<String,AttributElementResport> mapAttributeTablHead = fonctionGetInfosXmlAndProperties.getMapAttributeTablHead(); 
//		LinkedHashMap<String,AttributElementResport> mapAttributeTablDetail = fonctionGetInfosXmlAndProperties.getMapAttributeTablDetail();
//
//		Collection<TaCompteBanque> collectionTaCompteBanque = masterEntity.getTaCompteBanques();
//
//		fonctionGetInfosXmlAndProperties.findInfosFileXml(TaCompteBanque.class.getName(),TaCompteBanqueDTO.class.getName(),
//				listeChamp,"mapping");
//		fonctionGetInfosXmlAndProperties.getInfosObjetJPA(taCompteBanque);
//
//		ConstEdition constEdition = new ConstEdition(); 
//		Impression impression = new Impression(constEdition,taCompteBanque,collectionTaCompteBanque,nomChampIdTable,taCompteBanque.getIdCompteBanque());
//		String nomDossier = null;
//
//		int nombreLine = collectionTaCompteBanque.size();
//
//		if(nombreLine==0){
//			MessageDialog.openError(vue.getShell(), "ALTER",
//			"Il n'y a rien dans Cette Edition !");
//		}
//		else{
//			if(taInfoEntreprise.getIdInfoEntreprise()==0){
//				nomDossier = ConstEdition.INFOS_VIDE;
//			}
//			else{
//				nomDossier = taInfoEntreprise.getNomInfoEntreprise();	
//			}
//
//			constEdition.addValueList(tableViewer, nomClassController);
//
//			/**
//			 * pathFileReport ==> le path de ficher de edition dynamique
//			 */
//			String folderEditionDynamique = Const.C_RCP_INSTANCE_LOCATION+"/"+Const.C_NOM_PROJET_TMP+"/"+TaCompteBanque.class.getSimpleName();
//			constEdition.makeFolderEditions(folderEditionDynamique);
//			Path pathFileReport = new Path(folderEditionDynamique+"/"+Const.C_NOM_VU_BANQUE+".rptdesign");
//			final String pathFileReportDynamic = pathFileReport.toPortableString();
//
//
//			MakeDynamiqueReport DynamiqueReport = new MakeDynamiqueReport(constEdition.getNameTableEcran(),
//					constEdition.getNameTableBDD(),pathFileReportDynamic,Const.C_NOM_VU_BANQUE,
//					ConstEdition.PAGE_ORIENTATION_LANDSCAPE,nomDossier); 
//
//			/**************************************************************/
//			DynamiqueReport.setFonctionGetInfosXml(fonctionGetInfosXmlAndProperties);
//			DynamiqueReport.setNomObjet(TaCompteBanque.class.getSimpleName());
//			/**************************************************************/
//
//			Map<String, AttributElementResport> attribuGridHeader = new LinkedHashMap<String, AttributElementResport>();
//			String nameHeaderTitle = ConstEditionTiers.TITLE_EDITION_TIERS_BANQUE;
//			attribuGridHeader.put(nameHeaderTitle, new AttributElementResport("",ConstEdition.TEXT_ALIGN_CENTER,
//					ConstEdition.FONT_SIZE_XX_LARGE,ConstEdition.FONT_WEIGHT_BOLD,"",
//					ConstEdition.COLUMN_DATA_TYPE_STRING,ConstEdition.UNITS_VIDE
//					,""));
//			String nameSousHeaderTitle = ConstEditionTiers.SOUS_TITLE_EDITION_TIERS_BANQUE;
//			attribuGridHeader.put(nameSousHeaderTitle, new AttributElementResport("",ConstEdition.TEXT_ALIGN_CENTER,
//					ConstEdition.FONT_SIZE_X_LARGE,ConstEdition.FONT_WEIGHT_BOLD,"",
//					ConstEdition.COLUMN_DATA_TYPE_STRING,ConstEdition.UNITS_VIDE
//					,ConstEdition.COLOR_GRAY));
//
//			ConstEdition.CONTENT_COMMENTS = ConstEditionTiers.COMMENTAIRE_EDITION_DEFAUT;
//			DynamiqueReport.setSimpleNameEntity(TaCompteBanque.class.getSimpleName());
//			DynamiqueReport.initializeBuildDesignReportConfig();
//			DynamiqueReport.makePageMater("1", "1", "1", "1", "100");
//			DynamiqueReport.makeReportHeaderGrid(3,5,100,ConstEdition.UNITS_PERCENTAGE,attribuGridHeader);
//
//			//				DynamiqueReport.makeReportTableDB(100,ConstEdition.UNITS_PERCENTAGE,
//			//						Const.C_NOM_VU_TELEPHONE,attribuTabHeader,attribuTabDetail,1,1,2,5,"30");
//			DynamiqueReport.biuldTableReport("100", ConstEdition.UNITS_PERCENTAGE, 
//					Const.C_NOM_VU_BANQUE,1,1,2,"40", mapAttributeTablHead, mapAttributeTablDetail);
//
//			DynamiqueReport.savsAsDesignHandle();
//			impression.imprimer(true,pathFileReportDynamic,null,"Banques",TaCompteBanque.class.getSimpleName());
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
	protected void actAide() throws Exception {
		actAide(null);
	}
	@Override
	protected boolean aideDisponible() {
		boolean result = false;
		switch (getModeEcran().getMode()) {
		case C_MO_CONSULTATION:
			if (getFocusCourantSWT().equals(vue.getTfCODE_T_BANQUE()))
				result = true;
			break;
		case C_MO_EDITION:
		case C_MO_INSERTION:
			if (getFocusCourantSWT().equals(vue.getTfCODE_T_BANQUE()))
				result = true;
			break;
		default:
			break;
		}
		return result;
	}

	@Override
	protected void actAide(String message) throws Exception {
		if(aideDisponible()){
			try {
				VerrouInterface.setVerrouille(true);
				vue.setCursor(Display.getCurrent().getSystemCursor(SWT.CURSOR_WAIT));
				ParamAfficheAideRechercheSWT paramAfficheAideRecherche = new ParamAfficheAideRechercheSWT();
				//paramAfficheAideRecherche.setDb(getThis().getIbTaTable().getFIBBase());
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
				//SWTBaseControllerSWTStandard controllerEcranCreation = null;
				EJBBaseControllerSWTStandard controllerEcranCreation = null;
				ParamAffiche parametreEcranCreation = null;
				IEditorPart editorCreation = null;
				String editorCreationId = null;
				IEditorInput editorInputCreation = null;
				Shell s2 = new Shell(s, LgrShellUtil.styleLgr);

				//switch ((getThis().ibTaTable.getFModeObjet().getMode())) {
				switch (getModeEcran().getMode()) {
				case C_MO_CONSULTATION:
					if(getFocusCourantSWT().equals(vue.getGrille())){
						PaBanqueSWT paBanqueSWT = new PaBanqueSWT(s2,SWT.NULL);
						SWTPaBanqueController swtPaBanqueController = new SWTPaBanqueController(paBanqueSWT);

						editorCreationId = EditorBanque.ID;
						editorInputCreation = new EditorInputBanque();
						
						paramAfficheAideRecherche.setForceAffichageAideRemplie(true);
						paramAfficheAideRecherche.setAfficheDetail(false);

						ParamAfficheBanque paramAfficheBanque = new ParamAfficheBanque();
						paramAfficheAideRecherche.setJPQLQuery(dao.getJPQLQuery());
						paramAfficheBanque.setModeEcran(EnumModeObjet.C_MO_INSERTION);
						paramAfficheBanque.setEcranAppelant(paAideController);
						controllerEcranCreation = swtPaBanqueController;
						parametreEcranCreation = paramAfficheBanque;

						paramAfficheAideRecherche.setTypeEntite(TaCompteBanque.class);
						
						paramAfficheAideRecherche.setChampsRecherche(Const.C_NOM_BANQUE);
						paramAfficheAideRecherche.setDebutRecherche(vue.getTfNOM_BANQUE().getText());
						paramAfficheAideRecherche.setControllerAppelant(getThis());
						paramAfficheAideRecherche.setModel(swtPaBanqueController.getModelBanque());
						paramAfficheAideRecherche.setTypeObjet(swtPaBanqueController.getClassModel());
						//paramAfficheAideRecherche.setChampsIdentifiant(Const.C_ID_COMPTE_BANQUE);
						paramAfficheAideRecherche.setChampsIdentifiant(Const.C_ID_DTO_GENERAL);
					}
					break;
				case C_MO_EDITION:
				case C_MO_INSERTION:
					if(getFocusCourantSWT().equals(vue.getTfCODE_T_BANQUE())){
						PaTBanqueSWT paTBanqueSWT = new PaTBanqueSWT(s2,SWT.NULL);
						SWTPaTBanqueController swtPaTBanqueController = new SWTPaTBanqueController(paTBanqueSWT);

						editorCreationId = EditorTypeBanque.ID;
						editorInputCreation = new EditorInputTypeBanque();


						ParamAfficheTypeBanque paramAfficheTypeBanque = new ParamAfficheTypeBanque();
						ITaTBanqueServiceRemote dao = new EJBLookup<ITaTBanqueServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_T_BANQUE_SERVICE, ITaTBanqueServiceRemote.class.getName());
						paramAfficheAideRecherche.setJPQLQuery(dao.getJPQLQuery());
						paramAfficheTypeBanque.setModeEcran(EnumModeObjet.C_MO_INSERTION);
						paramAfficheTypeBanque.setEcranAppelant(paAideController);
						controllerEcranCreation = swtPaTBanqueController;
						parametreEcranCreation = paramAfficheTypeBanque;

						paramAfficheAideRecherche.setTypeEntite(TaCompteBanque.class);
						paramAfficheAideRecherche.setChampsRecherche(Const.C_CODE_T_BANQUE);
						paramAfficheAideRecherche.setDebutRecherche(vue.getTfCODE_T_BANQUE().getText());
						paramAfficheAideRecherche.setControllerAppelant(getThis());
						//paramAfficheAideRecherche.setModel(new ModelGeneral<SWTTAdr>(swtPaTAdrController.getIbTaTable().getFIBQuery(),SWTTAdr.class));
						ModelGeneralObjetEJB<TaTBanque,TaTBanqueDTO> modelTypeBanque = new ModelGeneralObjetEJB<TaTBanque,TaTBanqueDTO>(dao);
						paramAfficheAideRecherche.setModel(modelTypeBanque);

						paramAfficheAideRecherche.setTypeObjet(swtPaTBanqueController.getClassModel());

						//paramAfficheAideRecherche.setChampsIdentifiant(swtPaTBanqueController.getDao().getChampIdTable());
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
					paramAfficheAideRecherche
					.setFocusSWT(((PaAideRechercheSWT) paAideRechercheController1
							.getVue()).getTfChoix());
					paramAfficheAideRecherche
					.setRefCreationSWT(controllerEcranCreation);
					paramAfficheAideRecherche.setEditorCreation(editorCreation);
					paramAfficheAideRecherche.setEditorCreationId(editorCreationId);
					paramAfficheAideRecherche.setEditorInputCreation(editorInputCreation);
					paramAfficheAideRecherche
					.setParamEcranCreation(parametreEcranCreation);
					paramAfficheAideRecherche.setShellCreation(s2);
					paAideRechercheController1
					.configPanel(paramAfficheAideRecherche);
					// paramAfficheAideRecherche.setFocusDefaut(paAideRechercheController1.getVue().getTfChoix());

					// Ajout d'une recherche
					paAideController.addRecherche(paAideRechercheController1,
							paramAfficheAideRecherche.getTitreRecherche());

					// Paramï¿½trage de l'ecran d'aide principal
					ParamAfficheAideSWT paramAfficheAideSWT = new ParamAfficheAideSWT();
					ParamAfficheAide paramAfficheAide = new ParamAfficheAide();

					// enregistrement pour le retour de l'ecran d'aide
					paAideController.addRetourEcranListener(getThis());
					Control focus = vue.getShell().getDisplay().getFocusControl();
					// affichage de l'ecran d'aide principal (+ ses recherches)

					dbc.getValidationStatusMap().removeMapChangeListener(
							changeListener);
					//LgrShellUtil.afficheAideSWT(paramAfficheAide, null, paAide,paAideController, s);
					/*****************************************************************/
					paAideController.configPanel(paramAfficheAide);
					/*****************************************************************/
					dbc.getValidationStatusMap().addMapChangeListener(
							changeListener);

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
			
			IStatus resultat = new Status(IStatus.OK,TiersPlugin.PLUGIN_ID,"validateUIField champ : "+nomChamp!=null?nomChamp:"null"+" valeur : "+value!=null?value.toString():"valeur nulle"+" validation OK");
			
			int VALIDATION_CLIENT = 1;
			int VALIDATION_SERVEUR = 2;
			int VALIDATION_CLIENT_ET_SERVEUR = 3;
			
			//int TYPE_VALIDATION = VALIDATION_CLIENT;
			//int TYPE_VALIDATION = VALIDATION_SERVEUR;
			int TYPE_VALIDATION = VALIDATION_CLIENT_ET_SERVEUR;
			
			AbstractApplicationDAOClient<TaCompteBanqueDTO> a = new AbstractApplicationDAOClient<TaCompteBanqueDTO>();
			

			if(nomChamp.equals(Const.C_CODE_T_BANQUE)) {
//				TaTBanqueDAO dao = new TaTBanqueDAO(getEm());
//
//				dao.setModeObjet(getDao().getModeObjet());
//				TaTBanque f = new TaTBanque();
//				PropertyUtils.setSimpleProperty(f, nomChamp, value);
//				s = dao.validate(f,nomChamp,validationContext);
//
//				if(s.getSeverity()!=IStatus.ERROR) {
//					f = dao.findByCode((String)value);
//					taCompteBanque.setTaTBanque(f);
//				}
//				dao = null;
				ITaTBanqueServiceRemote dao = new EJBLookup<ITaTBanqueServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_T_BANQUE_SERVICE, ITaTBanqueServiceRemote.class.getName());
				TaTBanqueDTO dto = new TaTBanqueDTO();
				try {
					if(TYPE_VALIDATION==VALIDATION_CLIENT || TYPE_VALIDATION==VALIDATION_CLIENT_ET_SERVEUR) {
						AbstractApplicationDAOClient<TaTBanqueDTO> validationClient = new AbstractApplicationDAOClient<TaTBanqueDTO>();
						PropertyUtils.setSimpleProperty(dto, nomChamp, value);
						validationClient.validate(dto,nomChamp,ITaCompteBanqueServiceRemote.validationContext);
					}
					if(resultat==null && (TYPE_VALIDATION==VALIDATION_SERVEUR || TYPE_VALIDATION==VALIDATION_CLIENT_ET_SERVEUR)) {
						PropertyUtils.setSimpleProperty(dto, nomChamp, value);
						dao.validateDTOProperty(dto, nomChamp,ITaCompteBanqueServiceRemote.validationContext);
					}
				} catch(Exception e) {
					resultat = new Status(IStatus.ERROR,TiersPlugin.PLUGIN_ID, e.getMessage(), e);
				}
				if(resultat!=null && resultat.getSeverity()!=IStatus.ERROR ){
					TaTBanque entity = new TaTBanque();
					entity = dao.findByCode((String)value);
					taCompteBanque.setTaTBanque(entity);
				}
				dao = null;
			} else {
//				TaCompteBanque u = new TaCompteBanque();
//				u.setTaTiers(masterEntity);
//				PropertyUtils.setSimpleProperty(u, nomChamp, value);
//				s = dao.validate(u,nomChamp,validationContext);
				try {
					TaCompteBanqueDTO u = new TaCompteBanqueDTO();
					//u.setTaTiers(masterEntity);
					PropertyUtils.setSimpleProperty(u, nomChamp, value);
					dao.validateDTOProperty(u,nomChamp,ITaCompteBanqueServiceRemote.validationContext);
				} catch(Exception e) {
					resultat = new Status(IStatus.ERROR,TiersPlugin.PLUGIN_ID, e.getMessage(), e);
				}

			}
			//return s;
			return resultat;
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
			
			//TODO ejb, controle à remettre
//			ctrlUnChampsSWT(getFocusCourantSWT());
			ctrlTousLesChampsAvantEnregistrementSWT();
//			dao.begin(transaction);
			TaCompteBanqueMapper mapper = new TaCompteBanqueMapper();
			if ((modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION) == 0)) {
//				LgrDozerMapper<TaCompteBanqueDTO,TaCompteBanque> mapper = new LgrDozerMapper<TaCompteBanqueDTO,TaCompteBanque>();
//				mapper.map((TaCompteBanqueDTO) selectedCompteBanque.getValue(),taCompteBanque);
				mapper.mapDtoToEntity((TaCompteBanqueDTO) selectedCompteBanque.getValue(), taCompteBanque);
				masterEntity.addCompteBanque(taCompteBanque);

			} else if ((modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION) == 0)) {
//				LgrDozerMapper<TaCompteBanqueDTO,TaCompteBanque> mapper = new LgrDozerMapper<TaCompteBanqueDTO,TaCompteBanque>();
//				mapper.map((TaCompteBanqueDTO) selectedCompteBanque.getValue(),taCompteBanque);
				mapper.mapDtoToEntity((TaCompteBanqueDTO) selectedCompteBanque.getValue(), taCompteBanque);
				taCompteBanque.setTaTiers(masterEntity);
				masterEntity.addCompteBanque(taCompteBanque);				
			}
			boolean mapping=modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION) == 0;
			try {
				if(!enregistreToutEnCours) {
					sortieChamps();
					fireEnregistreTout(new AnnuleToutEvent(this,true));
					DeclencheCommandeControllerEvent e = new DeclencheCommandeControllerEvent(this,C_COMMAND_GLOBAL_ENREGISTRER_ID);
					fireDeclencheCommandeController(e,mapping);
				}
			} catch (Exception e) {
				logger.error("",e);
			}		
//			dao.commit(transaction);
			changementDeSelection();
			actRefresh();
//			transaction = null;

			getModeEcran().setMode(EnumModeObjet.C_MO_CONSULTATION);//ejb
			
			hideDecoratedFields();
			vue.getLaMessage().setText("");

		} finally {
//			if(transaction!=null && transaction.isActive()) {
//				transaction.rollback();
//			}
			initEtatBouton();
		}
	}

	public void initEtatComposant() {
		try {
			//vue.getTfCODE_T_BANQUE().setEditable(!isUtilise());
			changeCouleur(vue);
		} catch (Exception e) {
			vue.getLaMessage().setText(e.getMessage());
		}
	}

	public boolean isUtilise(){
		return (((TaCompteBanqueDTO)selectedCompteBanque.getValue()).getIdTBanque()!=null && 
				!dao.recordModifiable(dao.getNomTable(),
						((TaCompteBanqueDTO)selectedCompteBanque.getValue()).getIdTBanque()))||
						!masterDAO.autoriseModification(masterEntity);		
	}

	public TaCompteBanqueDTO getSwtOldCompteBanque() {
		return swtOldCompteBanque;
	}

	public void setSwtOldCompteBanque(TaCompteBanqueDTO swtOldCompteBanque) {
		this.swtOldCompteBanque = swtOldCompteBanque;
	}

	public void setSwtOldCompteBanque() {
		if (selectedCompteBanque.getValue() != null)
			this.swtOldCompteBanque = TaCompteBanqueDTO.copy((TaCompteBanqueDTO) selectedCompteBanque.getValue());
		else {
			if (tableViewer.selectionGrille(0)){
				this.swtOldCompteBanque = TaCompteBanqueDTO.copy((TaCompteBanqueDTO) selectedCompteBanque.getValue());
				tableViewer.setSelection(new StructuredSelection(
						(TaCompteBanqueDTO) selectedCompteBanque.getValue()), true);
			}}
	}

	public void setVue(PaBanqueSWT vue) {
		super.setVue(vue);
		this.vue = vue;
	}

	@Override
	protected void initMapComposantDecoratedField() {
		if (mapComposantDecoratedField == null)
			mapComposantDecoratedField = new LinkedHashMap<Control, ControlDecoration>();
		mapComposantDecoratedField.put(vue.getTfNOM_BANQUE(), vue
				.getFieldNOM_BANQUE());
		mapComposantDecoratedField.put(vue.getTfNOM_COMPTE(), vue
				.getFieldNOM_COMPTE());
		mapComposantDecoratedField.put(vue.getTfCOMPTE(), vue
				.getFieldCOMPTE());
		mapComposantDecoratedField.put(vue.getTfCODE_BANQUE(), vue
				.getFieldCODE_BANQUE());
		mapComposantDecoratedField.put(vue.getTfCODE_GUICHET(), vue
				.getFieldCODE_GUICHET());
		mapComposantDecoratedField.put(vue.getTfCLE_RIB(), vue
				.getFieldCLE_RIB());
		mapComposantDecoratedField.put(vue.getTfADRESSE1_BANQUE(), vue
				.getFieldADRESSE1_BANQUE());
		mapComposantDecoratedField.put(vue.getTfADRESSE2_BANQUE(), vue
				.getFieldADRESSE2_BANQUE());
		mapComposantDecoratedField.put(vue.getTfCP_BANQUE(), vue
				.getFieldCP_BANQUE());
		mapComposantDecoratedField.put(vue.getTfVILLE_BANQUE(), vue
				.getFieldVILLE_BANQUE());
		mapComposantDecoratedField.put(vue.getTfIBAN(), vue
				.getFieldIBAN());
		mapComposantDecoratedField.put(vue.getTfCODE_B_I_C(), vue
				.getFieldCODE_B_I_C());
		mapComposantDecoratedField.put(vue.getTfTITULAIRE(), vue
				.getFieldTITULAIRE());
		mapComposantDecoratedField.put(vue.getTfCODE_T_BANQUE(), vue
				.getFieldCODE_T_BANQUE());
		mapComposantDecoratedField.put(vue.getTfCPTCOMPTABLE(), vue
				.getFieldCPTCOMPTABLE());

	}

	public Class getClassModel() {
		return classModel;
	}

	@Override
	protected void sortieChamps() {
		// TODO Raccord de methode auto-genere

	}

	@Override
	protected void actRefresh() throws Exception {

		//repositionnement sur la valeur courante
		int idActuel = 0;
		if (taCompteBanque!=null) { //enregistrement en cours de modification/insertion
			idActuel = taCompteBanque.getIdCompteBanque();
		} else if(selectedCompteBanque!=null && (TaCompteBanqueDTO) selectedCompteBanque.getValue()!=null) {
			idActuel = ((TaCompteBanqueDTO) selectedCompteBanque.getValue()).getId();
		}

		writableList = new WritableList(realm, IHMmodel(), classModel);
		tableViewer.setInput(writableList);


		if(idActuel!=0) {
			tableViewer.setSelection(new StructuredSelection(recherche(/*Const.C_ID_COMPTE_BANQUE*/"id", idActuel)));
		}else
			tableViewer.selectionGrille(0);	
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
	//public ModelGeneral<SWTBanque> getModelBanque() {
	public ModelGeneralObjetEJB<TaCompteBanque,TaCompteBanqueDTO> getModelBanque() {
		return modelBanque;
	}

	public TaTiers getMasterEntity() {
		return masterEntity;
	}

	public void setMasterEntity(TaTiers masterEntity) {
		this.masterEntity = masterEntity;
	}

	public ITaTiersServiceRemote getMasterDAO() {
		return masterDAO;
	}

	public void setMasterDAO(ITaTiersServiceRemote masterDAO) {
		this.masterDAO = masterDAO;
	}

	public ITaCompteBanqueServiceRemote getDao() {
		return dao;
	}

	public void modifMode(){
		if (!VerrouInterface.isVerrouille() ){
			try {
				if(!getModeEcran().dataSetEnModif()) {
					if(!masterEntity.getTaCompteBanques().isEmpty()) {
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
