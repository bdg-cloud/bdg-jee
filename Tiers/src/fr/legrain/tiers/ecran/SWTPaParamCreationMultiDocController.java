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
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.model.mapping.mapper.TaParamCreeDocTiersMapper;
import fr.legrain.bdg.tiers.service.remote.ITaParamCreeDocTiersServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTDocServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.document.dto.TaTDocDTO;
import fr.legrain.document.model.TaTDoc;
import fr.legrain.gestCom.Appli.Const;
//import fr.legrain.gestCom.Appli.LgrDozerMapper;
import fr.legrain.gestCom.Appli.ModelGeneralObjetEJB;
import fr.legrain.gestCom.gestComBd.gestComBdPlugin;
import fr.legrain.gestCom.librairiesEcran.swt.DeclencheCommandeControllerEvent;
import fr.legrain.gestCom.librairiesEcran.swt.EJBBaseControllerSWTStandard;
import fr.legrain.gestCom.librairiesEcran.swt.IDetailController;
import fr.legrain.gestCom.librairiesEcran.swt.SWTPaAideControllerSWT;
import fr.legrain.gestCom.librairiesEcran.workbench.AnnuleToutEvent;
import fr.legrain.gestCom.librairiesEcran.workbench.ChangementDePageEvent;
import fr.legrain.gestCom.librairiesEcran.workbench.ChangementDeSelectionEvent;
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
import fr.legrain.lib.gui.aide.ResultAide;
import fr.legrain.lib.gui.grille.LgrTableViewer;
import fr.legrain.lib.gui.grille.LgrViewerSupport;
import fr.legrain.lib.validator.AbstractApplicationDAOClient;
import fr.legrain.libMessageLGR.LgrMess;
import fr.legrain.tiers.TiersPlugin;
import fr.legrain.tiers.clientutility.JNDILookupClass;
import fr.legrain.tiers.dto.TaParamCreeDocTiersDTO;
import fr.legrain.tiers.model.TaParamCreeDocTiers;
import fr.legrain.tiers.model.TaTiers;

public class SWTPaParamCreationMultiDocController extends EJBBaseControllerSWTStandard
implements RetourEcranListener,IDetailController {

	static Logger logger = Logger.getLogger(SWTPaParamCreationMultiDocController.class.getName());
	private PaParamCreationDocMultiple vue = null;
	private ITaParamCreeDocTiersServiceRemote dao = null;//new TaParamCreeDocTiersDAO();


	private Object ecranAppelant = null;
	private TaParamCreeDocTiersDTO swtParamCreeDocTiers;
	private TaParamCreeDocTiersDTO swtOldParamCreeDocTiers;
	private Realm realm;
	private DataBindingContext dbc;
	private LgrTableViewer tableTypeDocViewer;
	private ModelGeneralObjetEJB<TaTDoc,TaTDocDTO> modelTypeDoc = null;
	private Class classModelTypeDoc = TaTDocDTO.class;
	private ITaTDocServiceRemote taTDocDAO = null;
	private Class  classModel = TaParamCreeDocTiersDTO.class;
	private ModelGeneralObjetEJB<TaParamCreeDocTiers,TaParamCreeDocTiersDTO> modelParamCreeDocTiers = null;
	private LgrTableViewer tableViewer;
	private WritableList writableList;
	private IObservableValue selectedParamCreeDocTiers;
	private String[] listeChamp;
	private String[] listeTitre;
	private String nomClassController = this.getClass().getSimpleName();

	private TaTiers masterEntity = null;
	private ITaTiersServiceRemote masterDAO = null;

	private LgrDozerMapper<TaParamCreeDocTiersDTO,TaParamCreeDocTiers> mapperUIToModel  = new LgrDozerMapper<TaParamCreeDocTiersDTO,TaParamCreeDocTiers>();
	private TaParamCreeDocTiers taParamCreeDocTiers = null;

	private MapChangeListener changeListener = new MapChangeListener();

	private Integer idTiers = null;

	public SWTPaParamCreationMultiDocController(PaParamCreationDocMultiple vue) {
		this(vue,null);
	}

	public SWTPaParamCreationMultiDocController(PaParamCreationDocMultiple vue,EntityManager em) {
//		if(em!=null) {
//			setEm(em);
//		}
//		dao = new TaParamCreeDocTiersDAO(getEm());
//		taTDocDAO = new TaTDocDAO(getEm());
		try {
			dao = new EJBLookup<ITaParamCreeDocTiersServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_PARAM_CREE_DOC_TIERS_SERVICE, ITaParamCreeDocTiersServiceRemote.class.getName());
			taTDocDAO  = new EJBLookup<ITaTDocServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_T_DOC_SERVICE, ITaTDocServiceRemote.class.getName());
		} catch (NamingException e1) {
			e1.printStackTrace();
		}
		modelParamCreeDocTiers = new ModelGeneralObjetEJB<TaParamCreeDocTiers,TaParamCreeDocTiersDTO>(dao);
		modelTypeDoc = new ModelGeneralObjetEJB<TaTDoc,TaTDocDTO>(taTDocDAO);
		MapperParamCreeDocTiers monMapper;
		try {
			monMapper = new MapperParamCreeDocTiers();
			modelParamCreeDocTiers.setLgrMapper(monMapper);
		} catch (NamingException e1) {
			logger.error("", e1);
		}
//		monMapper.setEm(getEm());
		
		setVue(vue);

		// a faire avant l'initialisation du Binding
		vue.getGrille().addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setSwtOldParamCreeDocTiers();
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
			//setGrille(vue.getGrille());
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
	public List<TaParamCreeDocTiersDTO> IHMmodel() {
		LinkedList<TaParamCreeDocTiers> ldao = new LinkedList<TaParamCreeDocTiers>();
		LinkedList<TaParamCreeDocTiersDTO> lswt = new LinkedList<TaParamCreeDocTiersDTO>();

		if(masterEntity!=null && !masterEntity.getTaParamCreeDocTierses().isEmpty()) {
			ldao.addAll(masterEntity.getTaParamCreeDocTierses());
			modelParamCreeDocTiers.setListeEntity(ldao);
			
			lswt=modelParamCreeDocTiers.remplirListe();
		}
		return lswt;
	}



	public void bindTableTypeDoc(PaParamCreationDocMultiple vue) {
		try {
			realm = SWTObservables.getRealm(vue.getParent().getDisplay());
			String chaineTypeDoc = "TypeDoc";
			tableTypeDocViewer = new LgrTableViewer(vue.getTableTypeDoc());
			tableTypeDocViewer.createTableCol(classModelTypeDoc,vue.getTableTypeDoc(), nomClassController+chaineTypeDoc,
					Const.C_FICHIER_LISTE_CHAMP_GRILLE,0);
			String[] listeChamp = tableTypeDocViewer.setListeChampGrille(nomClassController+chaineTypeDoc,
					Const.C_FICHIER_LISTE_CHAMP_GRILLE);

			tableTypeDocViewer.addCheckStateListener(new ICheckStateListener() {

				@Override
				public void checkStateChanged(CheckStateChangedEvent event) {
					try {
						
						
						if(selectedParamCreeDocTiers!=null
								&& selectedParamCreeDocTiers.getValue()!=null) {

							if( ((TaParamCreeDocTiersDTO)selectedParamCreeDocTiers.getValue()).getTaTDoc()==null) { //a priori, insertion d'un nouveau document
								((TaParamCreeDocTiersDTO)selectedParamCreeDocTiers.getValue()).setTaTDoc(new ArrayList<TaTDocDTO>());
							}

							modifMode();

							if(event.getChecked()) {
								if(!rechercheDansParamTiers(((TaTDocDTO)event.getElement()).getCodeTDoc(),
										((TaParamCreeDocTiersDTO)selectedParamCreeDocTiers.getValue()).getId())){
								((TaParamCreeDocTiersDTO)selectedParamCreeDocTiers.getValue()).getTaTDoc().add(((TaTDocDTO)event.getElement()));
								
								}
								else{
									MessageDialog.openWarning(getVue().getShell(), "relation existante", "un paramètre pour ce type de document est " +
											"déjà existant.");
									event.getCheckable().setChecked(event.getElement(), false);
								}
							} else {
								boolean trouve = false;
								TaTDocDTO aSupprimer = null;
								List<TaTDocDTO> l = ((TaParamCreeDocTiersDTO)selectedParamCreeDocTiers.getValue()).getTaTDoc();
								for (TaTDocDTO swttDoc : l) {
									if(swttDoc.getCodeTDoc().equals(((TaTDocDTO)event.getElement()).getCodeTDoc())) {
										trouve = true;
										aSupprimer = (TaTDocDTO)event.getElement();
									}
								}
								if(trouve) {
									((TaParamCreeDocTiersDTO)selectedParamCreeDocTiers.getValue()).getTaTDoc().remove(aSupprimer);
								}
							}

						}

					} catch (Exception e) {
						logger.error("", e);
					}
				}
			});

			tableTypeDocViewer.setCheckStateProvider(new ICheckStateProvider() {

				@Override
				public boolean isGrayed(Object element) {
					//					// TODO Auto-generated method stub
					//					if(!((SWTDocumentTiers)element).getActif())
					//						return true;
					return false;
				}

				@Override
				public boolean isChecked(Object element) {
					if(selectedParamCreeDocTiers!=null
							&& selectedParamCreeDocTiers.getValue()!=null
							&& ((TaParamCreeDocTiersDTO)selectedParamCreeDocTiers.getValue()).getTaTDoc()!=null) {
						if(!rechercheDansParamTiers(((TaTDocDTO)element).getCodeTDoc(),
								((TaParamCreeDocTiersDTO)selectedParamCreeDocTiers.getValue()).getId())){
						List<TaTDocDTO> l = ((TaParamCreeDocTiersDTO)selectedParamCreeDocTiers.getValue()).getTaTDoc();
						for (TaTDocDTO swttDoc : l) {
							if(swttDoc.getCodeTDoc()!=null && swttDoc.getCodeTDoc().equals(((TaTDocDTO)element).getCodeTDoc())) {
								return true;
							}
						}
						}
						else return false;
					}
					return false;
				}
			});
			LgrViewerSupport.bind(tableTypeDocViewer, 
					//new WritableList(IHMmodel(), classModel),
					new WritableList(modelTypeDoc.remplirListe(), classModelTypeDoc),
					BeanProperties.values(listeChamp)
			);
		} catch (Exception e) {
			vue.getLaMessage().setText(e.getMessage());
			logger.error("", e);
		}
	}

	
	public void bind(PaParamCreationDocMultiple ecran) {
		try {
			//modelAdresse = new ModelTypeTiers(ibTaTable);
			//			 modelAdresse = new ModelGeneral<SWTInfoJuridique>(ibTaTable.getFIBQuery(),classModel);
			realm = SWTObservables.getRealm(vue.getParent().getDisplay());
			
			String[] listeTitre={"code Paramètre","par tiers","par document","par semaine","par deux Semaines","par decade","par mois","par x Jours","nombre de Jours"};
			String[] listeTaille={"300","100","100","100","100","100","100","100","100"};
			String[] listeChamp={"codeParam","tiers","document","semaine","deuxSemaines","decade","mois","xJours","nbJours"};
			
			this.listeChamp=listeChamp;
			this.listeTitre=listeTitre;
			tableViewer = new LgrTableViewer(ecran.getGrille());
			tableViewer.createTableCol(ecran.getGrille(), listeTitre,listeTaille,-1);


			LgrViewerSupport.bind(tableViewer, 
					new WritableList(IHMmodel(), classModel),
					BeanProperties.values(listeChamp)
			);

			selectedParamCreeDocTiers = ViewersObservables.observeSingleSelection(tableViewer);

			dbc = new DataBindingContext(realm);

			dbc.getValidationStatusMap().addMapChangeListener(changeListener);
			tableViewer.selectionGrille(0);

			setTableViewerStandard(tableViewer);
			setDbcStandard(dbc);

			selectedParamCreeDocTiers.getValue();
			bindingFormMaitreDetail(dbc, realm, selectedParamCreeDocTiers,classModel);
			changementDeSelection();
			selectedParamCreeDocTiers.addChangeListener(new IChangeListener() {

				public void handleChange(ChangeEvent event) {
					changementDeSelection();
					if(tableTypeDocViewer!=null) {
						tableTypeDocViewer.setInput(new WritableList(modelTypeDoc.remplirListe(),classModelTypeDoc));
					}
				}

			});
			bindTableTypeDoc(vue);
		} catch (Exception e) {
			vue.getLaMessage().setText(e.getMessage());
			logger.error("", e);
		}
	}

	private void changementDeSelection() {
		if(selectedParamCreeDocTiers!=null && selectedParamCreeDocTiers.getValue()!=null) {
			if(((TaParamCreeDocTiersDTO) selectedParamCreeDocTiers.getValue()).getId()!=null && 
					((TaParamCreeDocTiersDTO) selectedParamCreeDocTiers.getValue()).getId().compareTo(0)!=0) {
				try {
					taParamCreeDocTiers = dao.findById(((TaParamCreeDocTiersDTO) selectedParamCreeDocTiers.getValue()).getId());
				} catch (FinderException e) {
					logger.error("", e);
				}
			}
			//null a tester ailleurs, car peut etre null en cas d'insertion
			fireChangementDeSelection(new ChangementDeSelectionEvent(SWTPaParamCreationMultiDocController.this));
		}
	}

	public Composite getVue() {
		return vue;
	}

	public ResultAffiche configPanel(ParamAffiche param) {
		if (param != null) {
			Map<String,String[]> map = dao.getParamWhereSQL();
			//			ibTaTable.ouvreDataset();
			if (((ParamAfficheParamCreeDocTiers) param).getFocusDefautSWT() != null && !((ParamAfficheParamCreeDocTiers) param).getFocusDefautSWT().isDisposed())
				((ParamAfficheParamCreeDocTiers) param).getFocusDefautSWT().setFocus();
			else
				((ParamAfficheParamCreeDocTiers) param).setFocusDefautSWT(vue
						.getGrille());
			vue.getLaTitreFormulaire().setText(
					((ParamAfficheParamCreeDocTiers) param).getTitreFormulaire());
			vue.getLaTitreGrille().setText(
					((ParamAfficheParamCreeDocTiers) param).getTitreGrille());
			vue.getLaTitreFenetre().setText(
					((ParamAfficheParamCreeDocTiers) param).getSousTitre());


			if (param.getEcranAppelant() != null) {
				ecranAppelant = param.getEcranAppelant();
			}

			if(tableViewer==null) {
				//databinding pas encore realise
				bind(vue);
				tableViewer.tri(classModel, listeChamp,listeTitre);
			} else {
				try {
					taParamCreeDocTiers=null;
					selectedParamCreeDocTiers.setValue(null);
					actRefresh();
				} catch (Exception e) {
					logger.error("",e);
				}
			}
			// permet de se positionner sur le 1er enregistrement et de remplir
			// le formulaire

			//			tableViewer.tri(classModel, nomClassController,Const.C_FICHIER_LISTE_CHAMP_GRILLE);
			VerrouInterface.setVerrouille(false);
			setSwtOldParamCreeDocTiers();

			if (param.getModeEcran() != null
					&& param.getModeEcran().compareTo(EnumModeObjet.C_MO_INSERTION) == 0) {
				try {
					actInserer();
				} catch (Exception e) {
					vue.getLaMessage().setText(e.getMessage());
					logger.error("", e);
				}
			} else {
				initEtatBouton();
			}
			//param.setFocus(ibTaTable.getFModeObjet().getFocusCourant());
		}
		return null;
	}
	
	protected void initVerifySaisie() {
		if (mapInfosVerifSaisie == null)
			mapInfosVerifSaisie = new HashMap<Control, InfosVerifSaisie>();
		
		mapInfosVerifSaisie.put(vue.getTfCodeParam(), new InfosVerifSaisie(new TaParamCreeDocTiers(),Const.C_CODE_PARAM,null));
		mapInfosVerifSaisie.put(vue.getTfJours(), new InfosVerifSaisie(new TaParamCreeDocTiers(),Const.C_NB_JOUR,null));
		
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

		vue.getTfJours().setToolTipText(Const.C_NB_JOUR);

		mapComposantChamps.put(vue.getTfCodeParam(), Const.C_CODE_PARAM);
		mapComposantChamps.put(vue.getBtnTiers(), Const.C_TIERS);
		mapComposantChamps.put(vue.getBtnDocument(), Const.C_DOCUMENT);
		mapComposantChamps.put(vue.getBtn1semaine(), Const.C_SEMAINE);
		mapComposantChamps.put(vue.getBtn2semaines(), Const.C_DEUX_SEMAINES);
		mapComposantChamps.put(vue.getBtnDecad(), Const.C_DECADE);
		mapComposantChamps.put(vue.getBtn1mois(), Const.C_MOIS);		
		mapComposantChamps.put(vue.getBtnXjours(), Const.C_X_JOURS);
		mapComposantChamps.put(vue.getTfJours(), Const.C_NB_JOUR);

		for (Control c : mapComposantChamps.keySet()) {
			listeComposantFocusable.add(c);
		}
		listeComposantFocusable.add(vue.getBtnTiers());
		listeComposantFocusable.add(vue.getBtnDocument());
		listeComposantFocusable.add(vue.getBtn1semaine());
		listeComposantFocusable.add(vue.getBtn2semaines());
		listeComposantFocusable.add(vue.getBtnDecad());
		listeComposantFocusable.add(vue.getBtn1mois());
		listeComposantFocusable.add(vue.getBtnXjours());
		
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
				.getTfCodeParam());
		mapInitFocus.put(EnumModeObjet.C_MO_EDITION, vue
				.getTfCodeParam());
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

	public SWTPaParamCreationMultiDocController getThis() {
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
					.getString("Adresse.Message.Enregistrer"))) {

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
				setListeEntity(getModelParamCreeDocTiers().remplirListe());
				dao.initValeurIdTable(taParamCreeDocTiers);
				fireRetourEcran(new RetourEcranEvent(this, new ResultAide(
						dao.getChampIdEntite(), dao.getValeurIdTable(),
						selectedParamCreeDocTiers.getValue())));

				retour = true;
			}
		}

		return retour;
	}

	public void retourEcran(final RetourEcranEvent evt) {
		if (evt.getRetour() != null
				&& (evt.getSource() instanceof SWTPaAideControllerSWT)) {
			if (getFocusAvantAideSWT() instanceof Text) {
//				try {
//					((Text) getFocusAvantAideSWT()).setText(((ResultAffiche) evt.getRetour()).getResult());	
//					if(getFocusAvantAideSWT().equals(vue.getTfCODE_T_ADR())){
//						TaTAdr u = null;
//						TaTAdrDAO t = new TaTAdrDAO(getEm());
//						u = t.findById(Integer.parseInt(((ResultAffiche) evt.getRetour()).getIdResult()));
//						t = null;
//						taInfoJuridique.setTaTAdr(u);
//					}
//					ctrlUnChampsSWT(getFocusAvantAideSWT());
//				} catch (Exception e) {
//					logger.error("",e);
//					vue.getLaMessage().setText(e.getMessage());
//				}
			}
			if (getFocusAvantAideSWT() instanceof Table) {
				if (getFocusAvantAideSWT() == vue.getGrille()) {
					//ibTaTable.lgrLocateID(((ResultAffiche) evt.getRetour()).getIdResult());
					if(((ResultAffiche) evt.getRetour()).getSelection()!=null)
						tableViewer.setSelection(((ResultAffiche) evt.getRetour()).getSelection(),true);
				}
			}
		} else if (evt.getRetour() != null) {
			if (getFocusAvantAideSWT() instanceof Table) {
				if (getFocusAvantAideSWT() == vue.getGrille()) {
					//ibTaTable.lgrLocateID(((ResultAffiche) evt.getRetour()).getIdResult());
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
			boolean continuer=true;
			VerrouInterface.setVerrouille(true);
			masterDAO.verifAutoriseModification(masterEntity);
			setSwtOldParamCreeDocTiers();
			if(LgrMess.isDOSSIER_EN_RESEAU()){
				DeclencheCommandeControllerEvent e = new DeclencheCommandeControllerEvent(this,C_COMMAND_GLOBAL_MODIFIER_ID);
				fireDeclencheCommandeController(e);
				continuer = getMasterModeEcran().dataSetEnModif();
			}
			if(continuer){
//				setMasterEntity(masterDAO.findById(masterEntity.getIdTiers()));
				swtParamCreeDocTiers = new TaParamCreeDocTiersDTO();
				swtParamCreeDocTiers.setTiers(true);
				swtParamCreeDocTiers.setId(0);
//				dao.getModeObjet().setMode(EnumModeObjet.C_MO_INSERTION);
				taParamCreeDocTiers = new TaParamCreeDocTiers();
				taParamCreeDocTiers.setTiers(1);
				masterEntity.addParamCreeDocTiers(taParamCreeDocTiers);
				List l = IHMmodel();
				l.add(swtParamCreeDocTiers);
				writableList = new WritableList(realm, l, classModel);
				tableViewer.setInput(writableList);
				tableViewer.refresh();
				
				tableViewer.setSelection(new StructuredSelection( recherche(Const.C_ID_DTO_GENERAL, swtParamCreeDocTiers.getId())));
				
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
			setSwtOldParamCreeDocTiers();
			if(LgrMess.isDOSSIER_EN_RESEAU()){
				DeclencheCommandeControllerEvent e = new DeclencheCommandeControllerEvent(this,C_COMMAND_GLOBAL_MODIFIER_ID);
				fireDeclencheCommandeController(e);
				continuer = getMasterModeEcran().dataSetEnModif();
			}
			if(continuer){
				for (TaParamCreeDocTiers p : masterEntity.getTaParamCreeDocTierses()) {
					if(p.getIdParamCreeDocTiers()==((TaParamCreeDocTiersDTO) selectedParamCreeDocTiers.getValue()).getId()) {
						taParamCreeDocTiers = p;
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
							.getString("NoteTiers.Message.Supprimer"))) {				
//						dao.getModeObjet().setMode(EnumModeObjet.C_MO_SUPPRESSION);
						try {
							DeclencheCommandeControllerEvent e = new DeclencheCommandeControllerEvent(this,C_COMMAND_GLOBAL_MODIFIER_ID);
							fireDeclencheCommandeController(e);
						} catch (Exception e) {
							logger.error("",e);
						}
						for (TaParamCreeDocTiers p : masterEntity.getTaParamCreeDocTierses()) {
							if(p.getIdParamCreeDocTiers()==((TaParamCreeDocTiersDTO) selectedParamCreeDocTiers.getValue()).getId()) {
								taParamCreeDocTiers = p;
							}
						}

//						dao.begin(transaction);
//						dao.supprimer(taParamCreeDocTiers); //ejb commentaire
						taParamCreeDocTiers.setTaTiers(null);
						masterEntity.removeParamCreeDocTiers(taParamCreeDocTiers);
						Object suivant=tableViewer.getElementAt(tableViewer.getTable().getSelectionIndex()+1);
						//taNote=masterEntity.getTaNote();
//						dao.commit(transaction);
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
			// //InputVerifier inputVerifier =
			// getFocusCourant().getInputVerifier();
			// //getFocusCourant().setInputVerifier(null);
			switch (getModeEcran().getMode()) {
			case C_MO_INSERTION:
				if (silencieu || (!silencieu && MessageDialog.openQuestion(vue.getShell(), MessagesEcran
						.getString("Message.Attention"), MessagesEcran
						.getString("Adresse.Message.Annuler")))) {
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
						.getString("Adresse.Message.Annuler")))) {
					int rang =((WritableList)tableViewer.getInput()).indexOf(selectedParamCreeDocTiers.getValue());
					remetTousLesChampsApresAnnulationSWT(dbc);
					List<TaParamCreeDocTiersDTO> l = IHMmodel();
					if(rang!=-1)
						l.set(rang, swtOldParamCreeDocTiers);

					writableList = new WritableList(realm, l, classModel);
					tableViewer.setInput(writableList);
					tableViewer.refresh();
					tableViewer.setSelection(new StructuredSelection(swtOldParamCreeDocTiers), true);
					//					remetTousLesChampsApresAnnulationSWT(dbc);
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
		} finally {		
			VerrouInterface.setVerrouille(false);
		}
	}

	@Override
	protected void actImprimer() throws Exception {

//		TaInfoEntrepriseDAO taInfoEntrepriseDAO = new TaInfoEntrepriseDAO(getEm());
//		TaInfoEntreprise taInfoEntreprise = taInfoEntrepriseDAO.findInstance();
//
//		String nomChampIdTable =  dao.getChampIdTable();
//
//		FonctionGetInfosXmlAndProperties fonctionGetInfosXmlAndProperties = new FonctionGetInfosXmlAndProperties(mapperUIToModel);
//		fonctionGetInfosXmlAndProperties.cleanValueMapAttributeTable();
//
//		fonctionGetInfosXmlAndProperties.setValueMapAttributeTable(TaInfoJuridique.class.getSimpleName()+".head");
//		fonctionGetInfosXmlAndProperties.setValueMapAttributeTable(TaInfoJuridique.class.getSimpleName()+".detail");
//
//
//		LinkedHashMap<String,AttributElementResport> mapAttributeTablHead = fonctionGetInfosXmlAndProperties.getMapAttributeTablHead(); 
//		LinkedHashMap<String,AttributElementResport> mapAttributeTablDetail = fonctionGetInfosXmlAndProperties.getMapAttributeTablDetail();
//
//		Collection<TaInfoJuridique> collectionTaTelephone = masterEntity.getTaAdresses();
//
//		fonctionGetInfosXmlAndProperties.findInfosFileXml(TaInfoJuridique.class.getName(),SWTInfoJuridique.class.getName(),
//				listeChamp,"mapping");
//		fonctionGetInfosXmlAndProperties.getInfosObjetJPA(taInfoJuridique);
//
//		ConstEdition constEdition = new ConstEdition(); 
//		Impression impression = new Impression(constEdition,taInfoJuridique,collectionTaTelephone,nomChampIdTable,taInfoJuridique.getIdAdresse());
//		String nomDossier = null;
//
//		int nombreLine = collectionTaTelephone.size();
//
//
//		if(nombreLine==0){
//			MessageDialog.openWarning(vue.getShell(), ConstEdition.TITRE_MESSAGE_EDITION_VIDE,
//					ConstEdition.EDITION_VIDE);
//		}
//		else{
//
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
//			String folderEditionDynamique = Const.C_RCP_INSTANCE_LOCATION+"/"+Const.C_NOM_PROJET_TMP+"/"+TaInfoJuridique.class.getSimpleName();
//			constEdition.makeFolderEditions(folderEditionDynamique);
//			Path pathFileReport = new Path(folderEditionDynamique+"/"+Const.C_NOM_VU_ADRESSE+".rptdesign");
//			final String pathFileReportDynamic = pathFileReport.toPortableString();
//
//
//			MakeDynamiqueReport DynamiqueReport = new MakeDynamiqueReport(constEdition.getNameTableEcran(),
//					constEdition.getNameTableBDD(),pathFileReportDynamic,Const.C_NOM_VU_ADRESSE,
//					ConstEdition.PAGE_ORIENTATION_LANDSCAPE,nomDossier); 
//
//			/**************************************************************/
//			DynamiqueReport.setFonctionGetInfosXml(fonctionGetInfosXmlAndProperties);
//			DynamiqueReport.setNomObjet(TaInfoJuridique.class.getSimpleName());
//			/**************************************************************/
//
//
//			Map<String, AttributElementResport> attribuGridHeader = new LinkedHashMap<String, AttributElementResport>();
//			String nameHeaderTitle = ConstEditionTiers.TITLE_EDITION_TIERS_ADRESSE;
//			attribuGridHeader.put(nameHeaderTitle, new AttributElementResport("",ConstEdition.TEXT_ALIGN_CENTER,
//					ConstEdition.FONT_SIZE_XX_LARGE,ConstEdition.FONT_WEIGHT_BOLD,"",
//					ConstEdition.COLUMN_DATA_TYPE_STRING,ConstEdition.UNITS_VIDE
//					,""));
//			String nameSousHeaderTitle = ConstEditionTiers.SOUS_TITLE_EDITION_TIERS_ADRESSE;
//			attribuGridHeader.put(nameSousHeaderTitle, new AttributElementResport("",ConstEdition.TEXT_ALIGN_CENTER,
//					ConstEdition.FONT_SIZE_X_LARGE,ConstEdition.FONT_WEIGHT_BOLD,"",
//					ConstEdition.COLUMN_DATA_TYPE_STRING,ConstEdition.UNITS_VIDE
//					,ConstEdition.COLOR_GRAY));
//
//			ConstEdition.CONTENT_COMMENTS = ConstEditionTiers.COMMENTAIRE_EDITION_DEFAUT;
//			DynamiqueReport.setSimpleNameEntity(TaInfoJuridique.class.getSimpleName());
//			DynamiqueReport.initializeBuildDesignReportConfig();
//			DynamiqueReport.makePageMater("1", "1", "1", "1", "100");
//			DynamiqueReport.makeReportHeaderGrid(3,5,100,ConstEdition.UNITS_PERCENTAGE,attribuGridHeader);
//
//			//			DynamiqueReport.makeReportTableDB(100,ConstEdition.UNITS_PERCENTAGE,
//			//					Const.C_NOM_VU_ADRESSE,attribuTabHeader,attribuTabDetail,1,1,2,5,"30");
//			DynamiqueReport.biuldTableReport("100", ConstEdition.UNITS_PERCENTAGE, 
//					Const.C_NOM_VU_ADRESSE,1,1,2,"40", mapAttributeTablHead, mapAttributeTablDetail);
//
//			DynamiqueReport.savsAsDesignHandle();
//			impression.imprimer(true,pathFileReportDynamic,null,"Adresses",TaInfoJuridique.class.getSimpleName(),false);
//		}

	}

	@Override
	protected boolean aideDisponible() {
		boolean result = false;
//		switch ((getThis().dao.getModeObjet().getMode())) {
//		case C_MO_CONSULTATION:
//			if(getFocusCourantSWT().equals(vue.getGrille()))
//				result = true;
//			break;
//		case C_MO_EDITION:
//		case C_MO_INSERTION:
//			if(getFocusCourantSWT().equals(vue.getTfCODE_T_ADR()))
//				result = true;
//			break;
//		default:
//			break;
//		}
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
//			try {
//				VerrouInterface.setVerrouille(true);
//				vue.setCursor(Display.getCurrent().getSystemCursor(SWT.CURSOR_WAIT));
//				ParamAfficheAideRechercheSWT paramAfficheAideRecherche = new ParamAfficheAideRechercheSWT();
//				//				paramAfficheAideRecherche.setDb(getThis().getIbTaTable().getFIBBase());
//				paramAfficheAideRecherche.setMessage(message);
//				// Creation de l'ecran d'aide principal
//				Shell s = new Shell(PlatformUI.getWorkbench()
//						.getActiveWorkbenchWindow().getShell(),
//						LgrShellUtil.styleLgr);
//				PaAideSWT paAide = new PaAideSWT(s, SWT.NONE);
//				SWTPaAideControllerSWT paAideController = new SWTPaAideControllerSWT(
//						paAide);
//				/***************************************************************/
//				LgrPartListener.getLgrPartListener().setLgrActivePart(null);
//				IEditorPart e = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new EditorInputAide(), EditorAide.ID);
//				LgrPartListener.getLgrPartListener().setLgrActivePart(e);
//				LgrPartListener.getLgrPartListener().getLgrNavigation().add(e);
//				paAideController = new SWTPaAideControllerSWT(((EditorAide)e).getComposite());
//				((LgrEditorPart)e).setController(paAideController);
//				((LgrEditorPart)e).setPanel(((EditorAide)e).getComposite());
//				/***************************************************************/
//				JPABaseControllerSWTStandard controllerEcranCreation = null;
//				ParamAffiche parametreEcranCreation = null;
//				IEditorPart editorCreation = null;
//				String editorCreationId = null;
//				IEditorInput editorInputCreation = null;
//				Shell s2 = new Shell(s, LgrShellUtil.styleLgr);
//
//				switch ((getThis().dao.getModeObjet().getMode())) {
//				case C_MO_CONSULTATION:
//					if(getFocusCourantSWT().equals(vue.getGrille())){
//						PaInfosJuridiqueSWT paAdresseSWT = new PaInfosJuridiqueSWT(s2,SWT.NULL);
//						SWTPaInfosJuridiqueController swtPaAdresseController = new SWTPaInfosJuridiqueController(paAdresseSWT);
//
//						editorCreationId = EditorAdresse.ID;
//						editorInputCreation = new EditorInputAdresse();
//						
//						paramAfficheAideRecherche.setForceAffichageAideRemplie(true);
//						paramAfficheAideRecherche.setAfficheCreation(false);
//
//						ParamAfficheInfosJuridique paramAfficheAdresse = new ParamAfficheInfosJuridique();
//						paramAfficheAideRecherche.setJPQLQuery(dao.getJPQLQuery());
//						paramAfficheAdresse.setModeEcran(EnumModeObjet.C_MO_INSERTION);
//						paramAfficheAdresse.setEcranAppelant(paAideController);
//						controllerEcranCreation = swtPaAdresseController;
//						parametreEcranCreation = paramAfficheAdresse;
//
//						paramAfficheAideRecherche.setTypeEntite(TaInfoJuridique.class);
//						paramAfficheAideRecherche.setChampsRecherche(Const.C_ADRESSE1_ADRESSE);
//						paramAfficheAideRecherche.setDebutRecherche(vue.getTfADRESSE1_ADRESSE().getText());
//						paramAfficheAideRecherche.setControllerAppelant(getThis());
//						paramAfficheAideRecherche.setModel(swtPaAdresseController.getModelInfoJuridique());
//						paramAfficheAideRecherche.setTypeObjet(swtPaAdresseController.getClassModel());
//						paramAfficheAideRecherche.setChampsIdentifiant(Const.C_ID_ADRESSE);
//					}
//					break;
//				case C_MO_EDITION:
//				case C_MO_INSERTION:
//					if(getFocusCourantSWT().equals(vue.getTfCODE_T_ADR())){
//						PaTAdrSWT paTAdrSWT = new PaTAdrSWT(s2,SWT.NULL);
//						SWTPaTAdrController swtPaTAdrController = new SWTPaTAdrController(paTAdrSWT);
//
//						editorCreationId = EditorTypeAdresse.ID;
//						editorInputCreation = new EditorInputTypeAdresse();
//
//
//						ParamAfficheTypeAdresse paramAfficheTAdr = new ParamAfficheTypeAdresse();
//						paramAfficheAideRecherche.setJPQLQuery(new TaTAdrDAO(getEm()).getJPQLQuery());
//						paramAfficheTAdr.setModeEcran(EnumModeObjet.C_MO_INSERTION);
//						paramAfficheTAdr.setEcranAppelant(paAideController);
//						controllerEcranCreation = swtPaTAdrController;
//						parametreEcranCreation = paramAfficheTAdr;
//
//						paramAfficheAideRecherche.setTypeEntite(TaTAdr.class);
//						paramAfficheAideRecherche.setChampsRecherche(Const.C_CODE_T_ADR);
//						paramAfficheAideRecherche.setDebutRecherche(vue.getTfCODE_T_ADR().getText());
//						paramAfficheAideRecherche.setControllerAppelant(getThis());
//						//paramAfficheAideRecherche.setModel(new ModelGeneral<SWTTAdr>(swtPaTAdrController.getIbTaTable().getFIBQuery(),SWTTAdr.class));
//						paramAfficheAideRecherche.setModel(new ModelGeneralObjet<SWTTAdr,TaTAdrDAO,TaTAdr>(SWTTAdr.class,getEm()));
//
//						paramAfficheAideRecherche.setTypeObjet(swtPaTAdrController.getClassModel());
//
//						paramAfficheAideRecherche.setChampsIdentifiant(swtPaTAdrController.getDao().getChampIdTable());
//					}
//					break;
//				default:
//					break;
//				}
//
//				if (paramAfficheAideRecherche.getJPQLQuery() != null) {
//
//					PaAideRechercheSWT paAideRecherche1 = new PaAideRechercheSWT(s,
//							SWT.NULL);
//					SWTPaAideRechercheControllerSWT paAideRechercheController1 = new SWTPaAideRechercheControllerSWT(
//							paAideRecherche1);
//
//					// Parametrage de la recherche
//					paramAfficheAideRecherche
//					.setFocusSWT(((PaAideRechercheSWT) paAideRechercheController1
//							.getVue()).getTfChoix());
//					paramAfficheAideRecherche
//					.setRefCreationSWT(controllerEcranCreation);
//					paramAfficheAideRecherche.setEditorCreation(editorCreation);
//					paramAfficheAideRecherche.setEditorCreationId(editorCreationId);
//					paramAfficheAideRecherche.setEditorInputCreation(editorInputCreation);
//					paramAfficheAideRecherche
//					.setParamEcranCreation(parametreEcranCreation);
//					paramAfficheAideRecherche.setShellCreation(s2);
//					paAideRechercheController1
//					.configPanel(paramAfficheAideRecherche);
//					// paramAfficheAideRecherche.setFocusDefaut(paAideRechercheController1.getVue().getTfChoix());
//
//					// Ajout d'une recherche
//					paAideController.addRecherche(paAideRechercheController1,
//							paramAfficheAideRecherche.getTitreRecherche());
//
//					// Parametrage de l'ecran d'aide principal
//					ParamAfficheAideSWT paramAfficheAideSWT = new ParamAfficheAideSWT();
//					ParamAfficheAide paramAfficheAide = new ParamAfficheAide();
//
//					// enregistrement pour le retour de l'ecran d'aide
//					paAideController.addRetourEcranListener(getThis());
//					Control focus = vue.getShell().getDisplay().getFocusControl();
//					// affichage de l'ecran d'aide principal (+ ses recherches)
//
//					dbc.getValidationStatusMap().removeMapChangeListener(
//							changeListener);
//					//LgrShellUtil.afficheAideSWT(paramAfficheAide, null, paAide,paAideController, s);
//					/*****************************************************************/
//					paAideController.configPanel(paramAfficheAide);
//					/*****************************************************************/
//					dbc.getValidationStatusMap().addMapChangeListener(
//							changeListener);
//
//				}
//
//			} finally {
//				VerrouInterface.setVerrouille(false);
//				vue.setCursor(Display.getCurrent().getSystemCursor(SWT.CURSOR_ARROW));
//			}
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
			IStatus s = null;
			
//			if(nomChamp.equals(Const.C_ID_TIERS)) {
//				s = new Status(Status.OK,gestComBdPlugin.PLUGIN_ID,"OK");
//			}
//			//if(nomChamp.equals(Const.C_TIERS)) {
//			Object valeurBoolean=value;
//				TaParamCreeDocTiers param = new TaParamCreeDocTiers();
//				if(value instanceof Boolean)valeurBoolean=LibConversion.booleanToInt(((Boolean) value).booleanValue());
//				PropertyUtils.setSimpleProperty(param, nomChamp, valeurBoolean);
//				s = dao.validate(param,nomChamp,validationContext);
//				if(s.getSeverity()!=IStatus.ERROR) {
//					PropertyUtils.setSimpleProperty(taParamCreeDocTiers, nomChamp, valeurBoolean);
//				}
//
//			return s;
//			//s = new Status(Status.OK,gestComBdPlugin.PLUGIN_ID,"OK");
			
			IStatus resultat = new Status(IStatus.OK,TiersPlugin.PLUGIN_ID,"validateUIField champ : "+nomChamp!=null?nomChamp:"null"+" valeur : "+value!=null?value.toString():"valeur nulle"+" validation OK");
			
			int VALIDATION_CLIENT = 1;
			int VALIDATION_SERVEUR = 2;
			int VALIDATION_CLIENT_ET_SERVEUR = 3;
			
			//int TYPE_VALIDATION = VALIDATION_CLIENT;
			//int TYPE_VALIDATION = VALIDATION_SERVEUR;
			int TYPE_VALIDATION = VALIDATION_CLIENT_ET_SERVEUR;
			
			AbstractApplicationDAOClient<TaParamCreeDocTiersDTO> a = new AbstractApplicationDAOClient<TaParamCreeDocTiersDTO>();
			
			if(nomChamp.equals(Const.C_ID_TIERS)) {
				resultat = new Status(Status.OK,gestComBdPlugin.PLUGIN_ID,"OK");
			}
			
			try {
				TaParamCreeDocTiersDTO u = new TaParamCreeDocTiersDTO();
				PropertyUtils.setSimpleProperty(u, nomChamp, value);
				dao.validateDTOProperty(u,nomChamp,ITaParamCreeDocTiersServiceRemote.validationContext);
			} catch(Exception e) {
				resultat = new Status(IStatus.ERROR,TiersPlugin.PLUGIN_ID, e.getMessage(), e);
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
			if ((modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION) == 0)) {
//				LgrDozerMapper<TaParamCreeDocTiersDTO,TaParamCreeDocTiers> mapper = new LgrDozerMapper<TaParamCreeDocTiersDTO,TaParamCreeDocTiers>();
//				mapper.map((TaParamCreeDocTiersDTO) selectedParamCreeDocTiers.getValue(),taParamCreeDocTiers);
				TaParamCreeDocTiersMapper mapper = new TaParamCreeDocTiersMapper();
//				mapper.setEm(getEm());
				mapper.mapDtoToEntity((TaParamCreeDocTiersDTO) selectedParamCreeDocTiers.getValue(),taParamCreeDocTiers);
				masterEntity.addParamCreeDocTiers(taParamCreeDocTiers);
				taParamCreeDocTiers.setTaTiers(masterEntity);

			} else if ((modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION) == 0)) {
//				LgrDozerMapper<TaParamCreeDocTiersDTO,TaParamCreeDocTiers> mapper = 
//						new LgrDozerMapper<TaParamCreeDocTiersDTO,TaParamCreeDocTiers>();
//				mapper.map((TaParamCreeDocTiersDTO) selectedParamCreeDocTiers.getValue(),taParamCreeDocTiers);
				taParamCreeDocTiers.setTaTiers(masterEntity);
				TaParamCreeDocTiersMapper mapper = new TaParamCreeDocTiersMapper();
//				mapper.setEm(getEm());
				mapper.mapDtoToEntity((TaParamCreeDocTiersDTO) selectedParamCreeDocTiers.getValue(),taParamCreeDocTiers);
				masterEntity.addParamCreeDocTiers(taParamCreeDocTiers);
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
			//taParamCreeDocTiers=masterEntity.getTaParamCreeDocTiers();

			changementDeSelection();
			getModeEcran().setMode(EnumModeObjet.C_MO_CONSULTATION);//ejb
			actRefresh();
//			transaction = null;


			
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
//		try {
//			vue.getTfCODE_T_ADR().setEditable(!isUtilise());
//			changeCouleur(vue);
//		} catch (Exception e) {
//			vue.getLaMessage().setText(e.getMessage());
//		}
	}

	public boolean isUtilise(){
		return (((TaParamCreeDocTiersDTO)selectedParamCreeDocTiers.getValue()).getId()!=null && 
				!dao.recordModifiable(dao.getNomTable(),
						((TaParamCreeDocTiersDTO)selectedParamCreeDocTiers.getValue()).getId()))||
						!masterDAO.autoriseModification(masterEntity);		
	}

	public TaParamCreeDocTiersDTO getSwtOldParamCreeDocTiers() {
		return swtOldParamCreeDocTiers;
	}

	public void setSwtOldParamCreeDocTiers(TaParamCreeDocTiersDTO swtOldParamCreeDocTiers) {
		this.swtOldParamCreeDocTiers = swtOldParamCreeDocTiers;
	}

	public void setSwtOldParamCreeDocTiers() {
		if (selectedParamCreeDocTiers.getValue() != null)
			this.swtOldParamCreeDocTiers = TaParamCreeDocTiersDTO.copy((TaParamCreeDocTiersDTO) selectedParamCreeDocTiers.getValue());
		else {
			if (tableViewer.selectionGrille(0)){
				this.swtOldParamCreeDocTiers = TaParamCreeDocTiersDTO.copy((TaParamCreeDocTiersDTO) selectedParamCreeDocTiers.getValue());
				tableViewer.setSelection(new StructuredSelection(
						(TaParamCreeDocTiersDTO) selectedParamCreeDocTiers.getValue()), true);
			}
		}
	}

	public void setVue(PaParamCreationDocMultiple vue) {
		super.setVue(vue);
		this.vue = vue;
	}

	@Override
	protected void initMapComposantDecoratedField() {
		if (mapComposantDecoratedField == null)
			mapComposantDecoratedField = new LinkedHashMap<Control, ControlDecoration>();

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

		//		//repositionnement sur la valeur courante
		int idActuel = 0;
		if (taParamCreeDocTiers!=null) { //enregistrement en cours de modification/insertion
			idActuel = taParamCreeDocTiers.getIdParamCreeDocTiers();
		} else 
			if(selectedParamCreeDocTiers!=null && (TaParamCreeDocTiersDTO) selectedParamCreeDocTiers.getValue()!=null) {
				idActuel = ((TaParamCreeDocTiersDTO) selectedParamCreeDocTiers.getValue()).getId();
			}
		//rafraichissement des valeurs dans la grille
		//tableViewer.setInput(null);
		
		writableList = new WritableList(realm,IHMmodel(), classModel);
		tableViewer.setInput(writableList);


		if(idActuel!=0) {
			tableViewer.setSelection(new StructuredSelection(recherche(Const.C_ID_DTO_GENERAL, idActuel)));
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

	public ModelGeneralObjetEJB<TaParamCreeDocTiers,TaParamCreeDocTiersDTO> getModelParamCreeDocTiers() {
		return modelParamCreeDocTiers;
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

	public ITaParamCreeDocTiersServiceRemote getDao() {
		return dao;
	}
	public void modifMode(){
		if (!VerrouInterface.isVerrouille() ){
			try {
				if(!getModeEcran().dataSetEnModif()) {
					if(!masterEntity.getTaParamCreeDocTierses().isEmpty()) {
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


	public boolean rechercheDansParamTiers(String tdoc,int idParam){
		if(tdoc==null)return false;
		List<TaParamCreeDocTiers> listeExistante=dao.findByCodeTypeDoc(masterEntity.getCodeTiers(),tdoc);
		for (TaParamCreeDocTiers item : listeExistante) {
			for (TaTDoc tdocItem : item.getTaTDoc()) {
				if(tdocItem.getCodeTDoc().equals(tdoc) && item.getIdParamCreeDocTiers()!=idParam)
					return true;
			}
		}
		return false;
	}
}
