package fr.legrain.tiers.ecran;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.ejb.FinderException;
import javax.naming.Context;
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

import fr.legrain.bdg.tiers.service.remote.ITaTCiviliteServiceRemote;
import fr.legrain.gestCom.Appli.Const;
import fr.legrain.gestCom.Appli.ModelGeneralObjetEJB;
import fr.legrain.gestCom.librairiesEcran.editor.EditorAide;
import fr.legrain.gestCom.librairiesEcran.editor.EditorInputAide;
import fr.legrain.gestCom.librairiesEcran.swt.EJBBaseControllerSWTStandard;
import fr.legrain.gestCom.librairiesEcran.swt.LgrShellUtil;
import fr.legrain.gestCom.librairiesEcran.swt.ParamAfficheAideRechercheSWT;
import fr.legrain.gestCom.librairiesEcran.swt.ParamAfficheAideSWT;
import fr.legrain.gestCom.librairiesEcran.swt.SWTPaAideControllerSWT;
import fr.legrain.gestCom.librairiesEcran.swt.SWTPaAideRechercheControllerSWT;
import fr.legrain.gestCom.librairiesEcran.workbench.ChangementDeSelectionEvent;
import fr.legrain.gestCom.librairiesEcran.workbench.EJBLgrEditorPart;
import fr.legrain.gestCom.librairiesEcran.workbench.LgrPartListener;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.lib.data.InfosVerifSaisie;
import fr.legrain.lib.data.ModeObjet;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.VerrouInterface;
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
import fr.legrain.tiers.dto.TaTCiviliteDTO;
import fr.legrain.tiers.editor.EditorInputTypeCivilite;
import fr.legrain.tiers.editor.EditorTypeCivilite;
import fr.legrain.tiers.model.TaTCivilite;

public class SWTPaTypeCiviliteController extends EJBBaseControllerSWTStandard
		implements RetourEcranListener {

	static Logger logger = Logger.getLogger(SWTPaTypeCiviliteController.class.getName());
	private PaTypeCiviliteSWT vue = null;
	//private TaTCiviliteDAO dao = null;//new TaTCiviliteDAO();

	private Object ecranAppelant = null;
	private TaTCiviliteDTO swtTypeCivilite;
	private TaTCiviliteDTO swtOldTypecivilite;
	private Realm realm;
	private DataBindingContext dbc;

	private Class classModel = TaTCiviliteDTO.class;
	private ModelGeneralObjetEJB<TaTCivilite,TaTCiviliteDTO> modelTypeCivilite = null;//new ModelGeneralObjet<TaTCiviliteDTO,TaTCiviliteDAO,TaTCivilite>(dao,classModel);
	private LgrTableViewer tableViewer;
	private WritableList writableList;
	private IObservableValue selectedTypeCivilite;
	private String[] listeChamp;
	private String nomClassController = this.getClass().getSimpleName();

	//private LgrDozerMapper<TaTCiviliteDTO,TaTCivilite> mapperUIToModel  = new LgrDozerMapper<TaTCiviliteDTO,TaTCivilite>();
	private TaTCivilite taTCivilite = null;
	
	private MapChangeListener changeListener = new MapChangeListener();
	
	private ITaTCiviliteServiceRemote dao = null;
	
	private static ITaTCiviliteServiceRemote doLookup() {
		Context context = null;
		ITaTCiviliteServiceRemote bean = null;
		try {
			// 1. Obtaining Context
			context = JNDILookupClass.getInitialContext();
			// 2. Generate JNDI Lookup name
			String beanName = "TaTCiviliteService";
			final String interfaceName = ITaTCiviliteServiceRemote.class.getName();
			String lookupName = getLookupName(beanName,interfaceName);
			// 3. Lookup and cast
			bean = (ITaTCiviliteServiceRemote) context.lookup(lookupName);

		} catch (NamingException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	public SWTPaTypeCiviliteController(PaTypeCiviliteSWT vue) {
		this(vue,null);
	}

	public SWTPaTypeCiviliteController(PaTypeCiviliteSWT vue,EntityManager em) {
//		if(em!=null) {
//			setEm(em);
//		}
		//dao = new TaTCiviliteDAO(getEm());
		dao = doLookup();
		modelTypeCivilite = new ModelGeneralObjetEJB<TaTCivilite,TaTCiviliteDTO>(dao);
		setVue(vue);

		// a faire avant l'initialisation du Binding
		vue.getGrille().addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setSwtOldTypeCivilite();
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
			Menu[] tabPopups = new Menu[] { popupMenuFormulaire,popupMenuGrille };
			this.initPopupAndButtons(mapActions, tabPopups);
			vue.getPaCorpsFormulaire().setMenu(popupMenuFormulaire);
			vue.getPaGrille().setMenu(popupMenuGrille);

			initEtatBouton();
		} catch (Exception e) {
			vue.getLaMessage().setText(e.getMessage());
			logger.error("Erreur : PaTiersController", e);
		}
	}

	public void bind(PaTypeCiviliteSWT paTypeCiviliteSWT) {
		try {
			realm = SWTObservables.getRealm(vue.getParent().getDisplay());

			tableViewer = new LgrTableViewer(paTypeCiviliteSWT.getGrille());
			tableViewer.createTableCol(classModel,paTypeCiviliteSWT.getGrille(), nomClassController,
					Const.C_FICHIER_LISTE_CHAMP_GRILLE,0);
			listeChamp = tableViewer.setListeChampGrille(nomClassController,
					Const.C_FICHIER_LISTE_CHAMP_GRILLE);

//			ObservableListContentProvider viewerContent = new ObservableListContentProvider();
//			tableViewer.setContentProvider(viewerContent);
//
//			IObservableMap[] attributeMaps = BeansObservables.observeMaps(
//					viewerContent.getKnownElements(), classModel,listeChamp);
//
//			tableViewer.setLabelProvider(new ObservableMapLabelProvider(attributeMaps));
//			writableList = new WritableList(realm, modelTypeCivilite.remplirListe(), classModel);
//			tableViewer.setInput(writableList);
			
			// Set up data binding.
			LgrViewerSupport.bind(tableViewer, 
					new WritableList(modelTypeCivilite.remplirListe(), classModel),
					BeanProperties.values(listeChamp)
					);

			selectedTypeCivilite = ViewersObservables.observeSingleSelection(tableViewer);

			dbc = new DataBindingContext(realm);

			dbc.getValidationStatusMap().addMapChangeListener(changeListener);
			tableViewer.selectionGrille(0);

			setTableViewerStandard(tableViewer);
			setDbcStandard(dbc);

			bindingFormMaitreDetail(dbc, realm, selectedTypeCivilite,classModel);
			changementDeSelection();
			selectedTypeCivilite.addChangeListener(new IChangeListener() {

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
		if(selectedTypeCivilite!=null && selectedTypeCivilite.getValue()!=null) {
			if(((TaTCiviliteDTO) selectedTypeCivilite.getValue()).getId()!=null) {
				try {
					taTCivilite = dao.findById(((TaTCiviliteDTO) selectedTypeCivilite.getValue()).getId());
				} catch (FinderException e) {
					logger.error("", e);
				}
			}
			//null a tester ailleurs, car peut etre null en cas d'insertion
			fireChangementDeSelection(new ChangementDeSelectionEvent(SWTPaTypeCiviliteController.this));
		}
	}

	public Composite getVue() {
		return vue;
	}

	public ResultAffiche configPanel(ParamAffiche param) {
		if (param != null) {
			//ibTaTable.ouvreDataset();
			if (((ParamAfficheTypeCivilite) param).getFocusDefautSWT() != null && !((ParamAfficheTypeCivilite) param).getFocusDefautSWT().isDisposed())
				((ParamAfficheTypeCivilite) param).getFocusDefautSWT().setFocus();
			else
				((ParamAfficheTypeCivilite) param).setFocusDefautSWT(vue.getGrille());
			vue.getLaTitreFormulaire().setText(((ParamAfficheTypeCivilite) param).getTitreFormulaire());
			vue.getLaTitreGrille().setText(((ParamAfficheTypeCivilite) param).getTitreGrille());
			vue.getLaTitreFenetre().setText(((ParamAfficheTypeCivilite) param).getSousTitre());

			if (param.getEcranAppelant() != null) {
				ecranAppelant = param.getEcranAppelant();
			}
			
			/*
			 * passage ejb
			Map<String,String[]> map = dao.getParamWhereSQL();
			if(param.getIdFiche()!=null &&  !param.getIdFiche().equals("")) {
				if(map==null) map = new HashMap<String,String[]>();
				map.clear();
				map.put("idTCivilite",new String[]{"=",param.getIdFiche()});
				dao.setParamWhereSQL(map);
				vue.getBtnTous().setVisible(true);
				vue.getGrille().setVisible(false);
				vue.getLaTitreGrille().setVisible(false);
				vue.getCompositeForm().setWeights(new int[]{0,100});					
			}
			*/
			
			bind(vue);
			// permet de se positionner sur le 1er enregistrement et de remplir
			// le formulaire
			tableViewer.selectionGrille(0);
			tableViewer.tri(classModel, nomClassController,Const.C_FICHIER_LISTE_CHAMP_GRILLE);
			VerrouInterface.setVerrouille(false);
			setSwtOldTypeCivilite();

			if (param.getModeEcran() != null
					&& param.getModeEcran().compareTo(EnumModeObjet.C_MO_INSERTION) == 0) {
				try {
					actInserer();
				} catch (Exception e) {
					vue.getLaMessage().setText(e.getMessage());
					logger.error("", e);
				}
			}
		}
		return null;
	}
	
	protected void initVerifySaisie() {
		if (mapInfosVerifSaisie == null)
			mapInfosVerifSaisie = new HashMap<Control, InfosVerifSaisie>();
		
		mapInfosVerifSaisie.put(vue.getTfCODE_T_CIVILITE(), new InfosVerifSaisie(new TaTCivilite(),Const.C_CODE_T_CIVILITE,null));
		
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

		vue.getTfCODE_T_CIVILITE().setToolTipText(Const.C_CODE_T_CIVILITE);


		mapComposantChamps.put(vue.getTfCODE_T_CIVILITE(), Const.C_CODE_T_CIVILITE);

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
		listeComposantFocusable.add(vue.getBtnTous());

		if (mapInitFocus == null)
			mapInitFocus = new LinkedHashMap<EnumModeObjet, Control>();
		mapInitFocus.put(EnumModeObjet.C_MO_INSERTION, vue
				.getTfCODE_T_CIVILITE());
		mapInitFocus.put(EnumModeObjet.C_MO_EDITION, vue
				.getTfCODE_T_CIVILITE());
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
		//passage ejb
		//mapCommand.put(C_COMMAND_DOCUMENT_AFFICHER_TOUS_ID, handlerAfficherTous);
		
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
		//passage ejb
		//mapActions.put(vue.getBtnTous(), C_COMMAND_DOCUMENT_AFFICHER_TOUS_ID);

		Object[] tabActionsAutres = new Object[] { C_COMMAND_GLOBAL_AIDE_ID, C_COMMAND_GLOBAL_REFRESH_ID };
		mapActions.put(null, tabActionsAutres);
	}

	public SWTPaTypeCiviliteController getThis() {
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
					.getString("TypeCivilite.Message.Enregistrer"))) {

				try {
					actEnregistrer();
				} catch (Exception e) {
					vue.getLaMessage().setText(e.getMessage());
					logger.error("", e);
					retour= false;
				}
			} else
			break;
		case C_MO_CONSULTATION:
			break;
		default:
			break;
		}

		if (retour) {
			if (ecranAppelant instanceof SWTPaAideControllerSWT) {
				setListeEntity(getModelTypeCivilite().remplirListe());
				dao.initValeurIdTable(taTCivilite);
				fireRetourEcran(new RetourEcranEvent(this, new ResultAide(
						dao.getChampIdEntite(), dao.getValeurIdTable(),
						selectedTypeCivilite.getValue())));
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
		} else if (evt.getRetour() != null) {
			if (getFocusAvantAideSWT() instanceof Table) {
				if (getFocusAvantAideSWT() == vue.getGrille()) {
					if(((ResultAffiche) evt.getRetour()).getSelection()!=null)
						tableViewer.setSelection(((ResultAffiche) evt.getRetour()).getSelection(),true);
				}
			}
		}
		super.retourEcran(evt);
	}

	@Override
	protected void actInserer() throws Exception {
		try {
			if(getModeEcran().getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
				
				VerrouInterface.setVerrouille(true);
				setSwtOldTypeCivilite();
				
				swtTypeCivilite = new TaTCiviliteDTO();
				
				taTCivilite = new TaTCivilite();
				//dao.inserer(taTCivilite); //=> ejb ne fait rien sur le serveur
				
				modelTypeCivilite.getListeObjet().add(swtTypeCivilite);
				writableList = new WritableList(realm, modelTypeCivilite.getListeObjet(), classModel);
				tableViewer.setInput(writableList);
				tableViewer.refresh();
				tableViewer.setSelection(new StructuredSelection(swtTypeCivilite));
				
				getModeEcran().setMode(EnumModeObjet.C_MO_INSERTION);//ejb
				initEtatBouton();
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
			if(!LgrMess.isDOSSIER_EN_RESEAU()){
				setSwtOldTypeCivilite();
				taTCivilite = dao.findById(((TaTCiviliteDTO) selectedTypeCivilite.getValue()).getId());

			}else{
				if(!setSwtOldTypeCiviliteRefresh())throw new Exception();
			}			
			dao.modifier(taTCivilite);
			
			modeEcran.setMode(EnumModeObjet.C_MO_EDITION);//ejb
			initEtatBouton();
		} catch (Exception e1) {
			vue.getLaMessage().setText(e1.getMessage());
			logger.error("Erreur : actionPerformed", e1);
		}
	}
	public boolean containsEntity(TaTCivilite entite){
		if(modelTypeCivilite.getListeEntity()!=null){
			for (Object e : modelTypeCivilite.getListeEntity()) {
				if(((TaTCivilite)e).getIdTCivilite()==
					entite.getIdTCivilite())return true;
			}
		}
		return false;
	}

	public boolean setSwtOldTypeCiviliteRefresh() {
//passage ejb
//		try {	
//			if (selectedTypeCivilite.getValue()!=null){
//				TaTCivilite taArticleOld =dao.findById(taTCivilite.getIdTCivilite());
//				taArticleOld=dao.refresh(taArticleOld);
//				if(containsEntity(taTCivilite)) 
//					modelTypeCivilite.getListeEntity().remove(taTCivilite);
//				if(!taTCivilite.getVersionObj().equals(taArticleOld.getVersionObj())){
//					taTCivilite=taArticleOld;
//					if(!containsEntity(taTCivilite)) 
//						modelTypeCivilite.getListeEntity().add(taTCivilite);					
//					actRefresh();
//					dao.messageNonAutoriseModification();
//				}
//				taTCivilite=taArticleOld;
//				if(!containsEntity(taTCivilite)) 
//					modelTypeCivilite.getListeEntity().add(taTCivilite);
//				changementDeSelection();
//				this.swtOldTypecivilite=TaTCiviliteDTO.copy((TaTCiviliteDTO)selectedTypeCivilite.getValue());
//			}
			return true;
//		} catch (Exception e) {
//			return false;
//		}		
	}

//	public void setSwtOldTypeCiviliteRefresh() {
//		if (selectedTypeCivilite.getValue()!=null){
//			if(LgrMess.isDOSSIER_EN_RESEAU())dao.refresh(dao.findById(((TaTCiviliteDTO) selectedTypeCivilite.getValue()).getIdTCivilite()));
//			taTCivilite=dao.findById(((TaTCiviliteDTO) selectedTypeCivilite.getValue()).getIdTCivilite());
//			try {
//				if(LgrMess.isDOSSIER_EN_RESEAU())actRefresh();
//			} catch (Exception e) {
//				logger.error("",e);
//			}			
//			this.swtOldTypecivilite=TaTCiviliteDTO.copy((TaTCiviliteDTO)selectedTypeCivilite.getValue());
//		}
//	}
	@Override
	protected void actSupprimer() throws Exception {
//		EntityTransaction transaction = dao.getEntityManager().getTransaction();
		try {
			VerrouInterface.setVerrouille(true);
			if(isUtilise())MessageDialog.openInformation(vue.getShell(), MessagesEcran
					.getString("Message.Attention"), MessagesEcran
					.getString("Message.suppression"));
			else		
			if (MessageDialog.openConfirm(vue.getShell(), MessagesEcran
					.getString("Message.Attention"), MessagesEcran
					.getString("TypeCivilite.Message.Supprimer"))) {

//			dao.begin(transaction);
				TaTCivilite u = dao.findById(((TaTCiviliteDTO) selectedTypeCivilite.getValue()).getId());
				dao.supprimer(u);
//			dao.commit(transaction);
			Object suivant=tableViewer.getElementAt(tableViewer.getTable().getSelectionIndex()+1);
			modelTypeCivilite.removeEntity(taTCivilite);
			taTCivilite=null;
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			if(suivant!=null)tableViewer.setSelection(new StructuredSelection(suivant),true);
			else tableViewer.selectionGrille(0);
			actRefresh(); //ajouter pour tester jpa

			}
		} catch (Exception e1) {
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
						.getString("TypeCivilite.Message.Annuler")))) {
					remetTousLesChampsApresAnnulationSWT(dbc);
					if(((TaTCiviliteDTO) selectedTypeCivilite.getValue()).getId()==null){
						modelTypeCivilite.getListeObjet().remove(((TaTCiviliteDTO) selectedTypeCivilite.getValue()));
						writableList = new WritableList(realm, modelTypeCivilite.getListeObjet(), classModel);
						tableViewer.setInput(writableList);
						tableViewer.refresh();
						tableViewer.selectionGrille(0);
					}
					dao.annuler(taTCivilite);
					hideDecoratedFields();
				}
				getModeEcran().setMode(EnumModeObjet.C_MO_CONSULTATION);//ejb
				initEtatBouton();
				break;
			case C_MO_EDITION:
				if (silencieu || (!silencieu && MessageDialog.openQuestion(vue.getShell(), MessagesEcran
						.getString("Message.Attention"), MessagesEcran
						.getString("TypeCivilite.Message.Annuler")))) {
					int rang = modelTypeCivilite.getListeObjet().indexOf(selectedTypeCivilite.getValue());
					remetTousLesChampsApresAnnulationSWT(dbc);
					modelTypeCivilite.getListeObjet().set(rang, swtOldTypecivilite);
					writableList = new WritableList(realm, modelTypeCivilite.getListeObjet(), classModel);
					tableViewer.setInput(writableList);
					tableViewer.refresh();
					tableViewer.setSelection(new StructuredSelection(swtOldTypecivilite), true);
					dao.annuler(taTCivilite);
					hideDecoratedFields();
				}
				getModeEcran().setMode(EnumModeObjet.C_MO_CONSULTATION);//ejb
				initEtatBouton();

				break;
			case C_MO_CONSULTATION:
				actFermer();
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
//passage ejb
//		TaInfoEntrepriseDAO taInfoEntrepriseDAO = new TaInfoEntrepriseDAO(getEm());
//		TaInfoEntreprise taInfoEntreprise = taInfoEntrepriseDAO.findInstance();
//
//		String nomChampIdTable =  dao.getChampIdTable();
//
//		FonctionGetInfosXmlAndProperties fonctionGetInfosXmlAndProperties = new FonctionGetInfosXmlAndProperties(mapperUIToModel);
//		fonctionGetInfosXmlAndProperties.cleanValueMapAttributeTable();
//
//		fonctionGetInfosXmlAndProperties.setValueMapAttributeTable(TaTCivilite.class.getSimpleName()+".head");
//		fonctionGetInfosXmlAndProperties.setValueMapAttributeTable(TaTCivilite.class.getSimpleName()+".detail");
//		
//		LinkedHashMap<String,AttributElementResport> mapAttributeTablHead = fonctionGetInfosXmlAndProperties.getMapAttributeTablHead(); 
//		LinkedHashMap<String,AttributElementResport> mapAttributeTablDetail = fonctionGetInfosXmlAndProperties.getMapAttributeTablDetail();
//		
//		Collection<TaTCivilite> collectionTaTCivilite = modelTypeCivilite.getListeEntity();
//		
//		fonctionGetInfosXmlAndProperties.findInfosFileXml(TaTCivilite.class.getName(),TaTCiviliteDTO.class.getName(),
//				listeChamp,"mapping");
//		fonctionGetInfosXmlAndProperties.getInfosObjetJPA(taTCivilite);
//
//		ConstEdition constEdition = new ConstEdition(); 
////		Impression impression = new Impression(constEdition,taTCivilite,collectionTaTCivilite,nomChampIdTable,taTCivilite.getIdTCivilite());
//		String nomDossier = null;
//
//		int nombreLine = collectionTaTCivilite.size();
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
//			constEdition.addValueList(tableViewer, nomClassController);
//
//			/**
//			 * pathFileReport ==> le path de ficher de edition dynamique
//			 */
//			String folderEditionDynamique = Const.C_RCP_INSTANCE_LOCATION+"/"+Const.C_NOM_PROJET_TMP+"/"+TaTCivilite.class.getSimpleName();
//			constEdition.makeFolderEditions(folderEditionDynamique);
//			Path pathFileReport = new Path(folderEditionDynamique+"/"+Const.C_NOM_VU_T_CIVILITE+".rptdesign");
//			final String pathFileReportDynamic = pathFileReport.toPortableString();
//
//			MakeDynamiqueReport DynamiqueReport = new MakeDynamiqueReport(constEdition.getNameTableEcran(),
//					constEdition.getNameTableBDD(),pathFileReportDynamic,Const.C_NOM_VU_T_CIVILITE,
//					ConstEdition.PAGE_ORIENTATION_LANDSCAPE,nomDossier); 
//			
//			DynamiqueReport.setSimpleNameEntity(TaTCivilite.class.getSimpleName());
//			/**************************************************************/
//			DynamiqueReport.setFonctionGetInfosXml(fonctionGetInfosXmlAndProperties);
//			DynamiqueReport.setNomObjet(TaTCivilite.class.getSimpleName());
//			/**************************************************************/
//
//			Map<String, AttributElementResport> attribuGridHeader = new LinkedHashMap<String, AttributElementResport>();
//			String nameHeaderTitle = ConstEditionTiers.TITLE_EDITION_TYPE_CIVILITE;
//			attribuGridHeader.put(nameHeaderTitle, new AttributElementResport("",ConstEdition.TEXT_ALIGN_CENTER,
//					ConstEdition.FONT_SIZE_XX_LARGE,ConstEdition.FONT_WEIGHT_BOLD,"",
//					ConstEdition.COLUMN_DATA_TYPE_STRING,ConstEdition.UNITS_VIDE,""));
//			String nameSousHeaderTitle = ConstEditionTiers.SOUS_TITLE_EDITION_TYPE_CIVILITE;
//			attribuGridHeader.put(nameSousHeaderTitle, new AttributElementResport("",ConstEdition.TEXT_ALIGN_CENTER,
//					ConstEdition.FONT_SIZE_X_LARGE,ConstEdition.FONT_WEIGHT_BOLD,"",
//					ConstEdition.COLUMN_DATA_TYPE_STRING,ConstEdition.UNITS_VIDE,ConstEdition.COLOR_GRAY));
//
//			//DynamiqueReport.buildDesignConfig();
//			ConstEdition.CONTENT_COMMENTS = ConstEditionTiers.COMMENTAIRE_EDITION_DEFAUT;
//			DynamiqueReport.initializeBuildDesignReportConfig();
//			DynamiqueReport.makePageMater("1", "1", "1", "1", "100");
//			DynamiqueReport.makeReportHeaderGrid(3,5,100,ConstEdition.UNITS_PERCENTAGE,attribuGridHeader);
////			DynamiqueReport.makeReportTableDB(100,ConstEdition.UNITS_PERCENTAGE,
////					Const.C_NOM_VU_T_TEL,attribuTabHeader,attribuTabDetail,1,1,2,5,"30");
//			DynamiqueReport.biuldTableReport("100", ConstEdition.UNITS_PERCENTAGE, 
//					Const.C_NOM_VU_T_CIVILITE,1,1,2,"40", mapAttributeTablHead, mapAttributeTablDetail);
//			DynamiqueReport.savsAsDesignHandle();
////			impression.imprimer(true,pathFileReportDynamic,null,"Type Civilite",TaTCivilite.class.getSimpleName(),false);
//			/** 01/03/2010 modifier les editions (zhaolin) **/
//			BaseImpressionEdition impressionEdition = new BaseImpressionEdition(constEdition,taTCivilite,
//					getEm(),collectionTaTCivilite,taTCivilite.getIdTCivilite());
//			impressionEdition.impressionEditionTypeEntity(pathFileReportDynamic,"Civilite");
//		}
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
			break;
		default:
			break;
		}
		return result;
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

				switch ((getThis().getModeEcran().getMode())) {
				case C_MO_CONSULTATION:
					if(getFocusCourantSWT().equals(vue.getGrille())){
						PaTypeCiviliteSWT paTCiviliteSWT = new PaTypeCiviliteSWT(s2,SWT.NULL);
						SWTPaTypeCiviliteController swtPaTCiviliteController = new SWTPaTypeCiviliteController(paTCiviliteSWT);

						editorCreationId = EditorTypeCivilite.ID;
						editorInputCreation = new EditorInputTypeCivilite();

						ParamAfficheTypeCivilite paramAfficheTCivilite = new ParamAfficheTypeCivilite();
						paramAfficheAideRecherche.setJPQLQuery(dao.getJPQLQuery());
						paramAfficheTCivilite.setModeEcran(EnumModeObjet.C_MO_INSERTION);
						paramAfficheTCivilite.setEcranAppelant(paAideController);
						controllerEcranCreation = swtPaTCiviliteController;
						parametreEcranCreation = paramAfficheTCivilite;

						paramAfficheAideRecherche.setTypeEntite(TaTCivilite.class);
						paramAfficheAideRecherche.setChampsRecherche(Const.C_CODE_T_CIVILITE);
						paramAfficheAideRecherche.setDebutRecherche(vue.getTfCODE_T_CIVILITE().getText());
						paramAfficheAideRecherche.setControllerAppelant(getThis());
						paramAfficheAideRecherche.setModel(swtPaTCiviliteController.getModelTypeCivilite());
						paramAfficheAideRecherche.setTypeObjet(swtPaTCiviliteController.getClassModel());
						paramAfficheAideRecherche.setChampsIdentifiant(Const.C_ID_T_CIVILITE);
					}
					break;
				case C_MO_EDITION:
				case C_MO_INSERTION:
					break;
				default:
					break;
				}

				if (paramAfficheAideRecherche.getJPQLQuery() != null) {

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

					dbc.getValidationStatusMap().removeMapChangeListener(
							changeListener);
					/*****************************************************************/
					paAideController.configPanel(paramAfficheAide);
					/*****************************************************************/
					dbc.getValidationStatusMap().addMapChangeListener(changeListener);

				}

			} finally {
				VerrouInterface.setVerrouille(false);
				vue.setCursor(Display.getCurrent().getSystemCursor(SWT.CURSOR_ARROW));
			}
		}
	}
	public IStatus validateUI() {
		return null;
	}
	
	public IStatus validateUIField(String nomChamp,Object value) {
		//IStatus resultat = null;
		IStatus resultat = new Status(IStatus.OK,TiersPlugin.PLUGIN_ID,"validateUIField champ : "+nomChamp+" valeur : "+value!=null?value.toString():"valeur nulle"+" validation OK");
		
		int VALIDATION_CLIENT = 1;
		int VALIDATION_SERVEUR = 2;
		int VALIDATION_CLIENT_ET_SERVEUR = 3;
		
		//int TYPE_VALIDATION = VALIDATION_CLIENT;
		//int TYPE_VALIDATION = VALIDATION_SERVEUR;
		int TYPE_VALIDATION = VALIDATION_CLIENT_ET_SERVEUR;
		
		AbstractApplicationDAOClient<TaTCiviliteDTO> validationClient = new AbstractApplicationDAOClient<TaTCiviliteDTO>();
		
		//validation client
		if(TYPE_VALIDATION==VALIDATION_CLIENT || TYPE_VALIDATION==VALIDATION_CLIENT_ET_SERVEUR) {
			//BeanValidatorJFaceDatabinding validator = new BeanValidatorJFaceDatabinding(TaTCiviliteDTO.class,nomChamp);
			//resultat = validator.validate(selectedTypeCivilite.getValue());
			try {
				TaTCiviliteDTO f = new TaTCiviliteDTO();
				PropertyUtils.setSimpleProperty(f, nomChamp, value);
				//a.validate((TaTCiviliteDTO)selectedTypeCivilite.getValue(), nomChamp, null);
				validationClient.validate(f, nomChamp, ITaTCiviliteServiceRemote.validationContext);
			} catch(Exception e) {
				//if(resultat==null) {
					//resultat = new MultiStatus(TiersPlugin.PLUGIN_ID, 0, e.getMessage(), e);
					resultat = new Status(IStatus.ERROR,TiersPlugin.PLUGIN_ID, e.getMessage(), e);
				//}
			}
		}
		//validation serveur
		if(resultat==null && (TYPE_VALIDATION==VALIDATION_SERVEUR || TYPE_VALIDATION==VALIDATION_CLIENT_ET_SERVEUR)) {
			try {
				TaTCiviliteDTO f = new TaTCiviliteDTO();
				PropertyUtils.setSimpleProperty(f, nomChamp, value);
				//dao.validateDTOProperty((TaTCiviliteDTO)selectedTypeCivilite.getValue(), nomChamp);
				dao.validateDTOProperty(f, nomChamp, ITaTCiviliteServiceRemote.validationContext);
			} catch(Exception e) {
				//if(resultat==null) {
					//resultat = new MultiStatus(TiersPlugin.PLUGIN_ID, 0, e.getMessage(), e);
					resultat = new Status(IStatus.ERROR,TiersPlugin.PLUGIN_ID, e.getMessage(), e);
				//}
				//e.printStackTrace();
			}
		}
		
		return resultat;
		
		//return validator.validate(selectedTypeCivilite.getValue());
		
//passage ejb
//		try {
//			IStatus s = null;
//			boolean verrouilleModifCode = false;
//			int change=0;
//			TaTCivilite u = new TaTCivilite();
//			PropertyUtils.setSimpleProperty(u, nomChamp, value);
//			if(((TaTCiviliteDTO) selectedTypeCivilite.getValue()).getId()!=null) {
//				u.setIdTCivilite(((TaTCiviliteDTO) selectedTypeCivilite.getValue()).getId());
//			}
//			if(nomChamp.equals(Const.C_CODE_T_CIVILITE)){
//				verrouilleModifCode = true;
//			}
//
//			s = dao.validate(u,nomChamp,validationContext,verrouilleModifCode);
//			if(s.getSeverity()!=IStatus.ERROR && change!=0) {
//
//			}
//			return s;
//		} catch (IllegalAccessException e) {
//			logger.error("",e);
//		} catch (InvocationTargetException e) {
//			logger.error("",e);
//		} catch (NoSuchMethodException e) {
//			logger.error("",e);
//		}
//		return null;
		
		//return new Status(IStatus.OK, "Tiers", "ok");
	}
	
	@Override
	protected void actEnregistrer() throws Exception {
		//EntityTransaction transaction = dao.getEntityManager().getTransaction();
		try {
			//TODO ejb, controle à remettre
//			ctrlUnChampsSWT(getFocusCourantSWT());
			ctrlTousLesChampsAvantEnregistrementSWT();
			
//			dao.begin(transaction);
			if ((modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION) == 0)||
					(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION) == 0)) {
				//try {
				//LgrDozerMapper<TaTCiviliteDTO,TaTCivilite> mapper = new LgrDozerMapper<TaTCiviliteDTO,TaTCivilite>();
				//mapper.map((TaTCiviliteDTO) selectedTypeCivilite.getValue(),taTCivilite);

//mapper sur client, envoi d'une entité					
//				TaTCiviliteMapper mapper = new TaTCiviliteMapper();
//				mapper.mapDtoToEntity((TaTCiviliteDTO) selectedTypeCivilite.getValue(),taTCivilite);
//				
//				if (modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION) == 0)
//					taTCivilite=dao.enregistrerMerge(taTCivilite);
//				else if (modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION) == 0)
//					dao.enregistrerPersist(taTCivilite);
					
//mapper sur serveur, envoi d'un DTO					
					if (modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION) == 0)
						dao.enregistrerMergeDTO((TaTCiviliteDTO) selectedTypeCivilite.getValue(),ITaTCiviliteServiceRemote.validationContext);
					else if (modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION) == 0)
						dao.enregistrerPersistDTO((TaTCiviliteDTO) selectedTypeCivilite.getValue(),ITaTCiviliteServiceRemote.validationContext);
				
				
			} 
//			dao.commit(transaction);
			
			modelTypeCivilite.addEntity(taTCivilite);
			
			getModeEcran().setMode(EnumModeObjet.C_MO_CONSULTATION);//ejb
//			transaction = null;
			actRefresh();
			
			//nettoyage affichage erreur possible
			hideDecoratedFields();
			vue.getLaMessage().setText("");
		} catch(Exception e) {
			e.printStackTrace();
			if(e.getMessage()!=null)
				vue.getLaMessage().setText(e.getMessage());
			
			afficheDecoratedField(vue.getTfCODE_T_CIVILITE(),e.getMessage());
			
//			final ControlDecoration deco = new ControlDecoration(vue.getTfCODE_T_CIVILITE(), SWT.TOP | SWT.RIGHT);
//
//			// Re-use an existing image
//			String monIdImageTest = "mon_id_image_test";
//			FieldDecorationRegistry.getDefault().registerFieldDecoration(monIdImageTest, "ma description", 
//					LibrairiesEcranPlugin.ir.get(LibrairiesEcranPlugin.IMAGE_EXCLAMATION_ERROR_TEST));
//			
//			Image image = FieldDecorationRegistry.
//					getDefault().
//					//getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).
//					getFieldDecoration(monIdImageTest).
//					getImage();
//			// Set description and image
//			deco.setDescriptionText(e.getMessage());
//			deco.setImage(image);
//			// Hide deco if not in focus
//			deco.setShowOnlyOnFocus(true); 
//			deco.hide();
//			deco.show(); 
			
			//ControlDecorationSupport.
	
		} finally {
//			if(transaction!=null && transaction.isActive()) {
//				transaction.rollback();
//			}
			initEtatBouton();
		}
	}


	public void initEtatComposant() {
		try {
			vue.getTfCODE_T_CIVILITE().setEditable(!isUtilise());
			changeCouleur(vue);
	    } catch (Exception e) {
		vue.getLaMessage().setText(e.getMessage());
	    }
	}

	public boolean isUtilise(){
		return (((TaTCiviliteDTO)selectedTypeCivilite.getValue()).getId()!=null &&
		!dao.recordModifiable(dao.getNomTable(),
				((TaTCiviliteDTO)selectedTypeCivilite.getValue()).getId()))||
				!dao.autoriseModification(taTCivilite);		
	}
	
	public TaTCiviliteDTO getSwtOldTypecivilite() {
		return swtOldTypecivilite;
	}

	public void setSwtOldTypeCivilite(TaTCiviliteDTO swtOldTypeCivilite) {
		this.swtOldTypecivilite = swtOldTypeCivilite;
	}

	public void setSwtOldTypeCivilite() {
		if (selectedTypeCivilite.getValue() != null)
			this.swtOldTypecivilite = TaTCiviliteDTO.copy((TaTCiviliteDTO) selectedTypeCivilite.getValue());
		else {
			if(tableViewer.selectionGrille(0)){
				this.swtOldTypecivilite = TaTCiviliteDTO.copy((TaTCiviliteDTO) selectedTypeCivilite.getValue());
				tableViewer.setSelection(new StructuredSelection(
						(TaTCiviliteDTO) selectedTypeCivilite.getValue()), true);
			}
		}
	}

	public void setVue(PaTypeCiviliteSWT vue) {
		super.setVue(vue);
		this.vue = vue;
	}

	@Override
	protected void initMapComposantDecoratedField() {
		if (mapComposantDecoratedField == null)
			mapComposantDecoratedField = new LinkedHashMap<Control, ControlDecoration>();
		mapComposantDecoratedField.put(vue.getTfCODE_T_CIVILITE(), vue.getFieldCODE_T_CIVILITE());
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
		TaTCivilite u = taTCivilite;
		if (u!=null && u.getIdTCivilite()==0)
			u=dao.findById(u.getIdTCivilite());
		writableList = new WritableList(realm, modelTypeCivilite.remplirListe(), classModel);
		tableViewer.setInput(writableList);
		tableViewer.refresh();
		tableViewer.selectionGrille(
				tableViewer.selectionGrille(selectedTypeCivilite.getValue()));
		
		if(u != null) {
			Iterator<TaTCiviliteDTO> ite = modelTypeCivilite.getListeObjet().iterator();
			TaTCiviliteDTO tmp = null;
			int i = 0;
			boolean trouve = false;
			while (ite.hasNext() && !trouve) {
				tmp = ite.next();
				if(tmp.getId()==u.getIdTCivilite()) {
					tableViewer.setSelection(new StructuredSelection(tmp));
					trouve = true;
				}
				i++;
			}
		}	
	}

	public ModelGeneralObjetEJB<TaTCivilite, TaTCiviliteDTO> getModelTypeCivilite() {
		return modelTypeCivilite;
	}

	public ITaTCiviliteServiceRemote getDao() {
		return dao;
	}

	public void actAfficherTous() throws Exception{
		vue.getGrille().setVisible(true);  
		vue.getBtnTous().setVisible(false);
		vue.getLaTitreGrille().setVisible(true);
		vue.getCompositeForm().setWeights(new int[]{50,100});
		dao.setJPQLQuery(dao.getJPQLQueryInitial());
		modelTypeCivilite.setJPQLQuery(null);
		modelTypeCivilite.setListeEntity(null);
		try {
			actRefresh();
		} catch (Exception e1) {
			logger.error("", e1);
		}
	}
}
