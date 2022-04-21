package fr.legrain.tiers.ecran;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.FinderException;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.RollbackException;

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
import org.eclipse.core.databinding.property.list.DelegatingListProperty;
import org.eclipse.core.databinding.property.list.IListProperty;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
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
import fr.legrain.bdg.tiers.service.remote.ITaAdresseServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaEmailServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTAdrServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTelephoneServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.gestCom.Appli.Const;
//import fr.legrain.gestCom.Appli.LgrDozerMapper;
import fr.legrain.gestCom.librairiesEcran.LibrairiesEcranPlugin;
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
import fr.legrain.lib.data.LibConversion;
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
import fr.legrain.lib.gui.grille.LgrTreeViewer;
import fr.legrain.lib.validator.AbstractApplicationDAOClient;
import fr.legrain.librairiesLeGrain.LibrairiesLeGrainPlugin;
import fr.legrain.tiers.TiersPlugin;
import fr.legrain.tiers.clientutility.JNDILookupClass;
import fr.legrain.tiers.dto.TaAdresseDTO;
import fr.legrain.tiers.dto.TaParamCorrespondanceDTO;
import fr.legrain.tiers.model.TaAdresse;
import fr.legrain.tiers.model.TaEmail;
import fr.legrain.tiers.model.TaTAdr;
import fr.legrain.tiers.model.TaTelephone;
import fr.legrain.tiers.model.TaTiers;

public class SWTPaParamCorrespondanceController extends EJBBaseControllerSWTStandard
implements RetourEcranListener,IDetailController {

	static Logger logger = Logger.getLogger(SWTPaParamCorrespondanceController.class.getName());
	private PaParamCorrespondanceSWT vue = null;
	private ITaAdresseServiceRemote dao = null;
	private ITaTelephoneServiceRemote daoTelephone = null;
	private ITaEmailServiceRemote daoEmail = null;
	
	public static final String validationContext = "ADRESSE";


	private Object ecranAppelant = null;
	private TaParamCorrespondanceDTO swtAdresse;
	private TaParamCorrespondanceDTO swtOldAdresse;
	private Realm realm;
	private DataBindingContext dbc;

	private Class  classModel = TaParamCorrespondanceDTO.class;
	//private ModelGeneralObjet<TaParamCorrespondanceDTO,TaAdresseDAO,TaAdresse> modelAdresse = null;//new ModelGeneralObjet<TaParamCorrespondanceDTO,TaAdresseDAO,TaAdresse>(dao,classModel);
	private ModelParamCorrespondance modelAdresse = null;	
	private LgrTreeViewer treeViewer;
	private WritableList writableList;
	private IObservableValue selectedAdresse;
	private String[] listeChamp;
	private String nomClassController = this.getClass().getSimpleName();

	private TaTiers masterEntity = null;
	private ITaTiersServiceRemote masterDAO = null;

	private LgrDozerMapper<TaParamCorrespondanceDTO,TaAdresse> mapperUIToModel  = new LgrDozerMapper<TaParamCorrespondanceDTO,TaAdresse>();
	private TaAdresse taAdresse = null;
	private TaEmail taEmail = null;
	private TaTelephone taTelephone = null;

	private MapChangeListener changeListener = new MapChangeListener();

	private Integer idTiers = null;

	public SWTPaParamCorrespondanceController(PaParamCorrespondanceSWT vue) {
		this(vue,null);
	}

	public SWTPaParamCorrespondanceController(PaParamCorrespondanceSWT vue,EntityManager em) {
//		if(em!=null) {
//			setEm(em);
//		}
//		dao = new TaAdresseDAO(getEm());
//		daoEmail = new TaEmailDAO(getEm());
//		daoTelephone = new TaTelephoneDAO(getEm());
		try {
			dao = new EJBLookup<ITaAdresseServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_ADRESSE_SERVICE, ITaAdresseServiceRemote.class.getName());
			daoEmail = new EJBLookup<ITaEmailServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_EMAIL_SERVICE, ITaEmailServiceRemote.class.getName());
			daoTelephone = new EJBLookup<ITaTelephoneServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_TELEPHONE_SERVICE, ITaTelephoneServiceRemote.class.getName());
		
			//modelAdresse = new ModelGeneralObjet<TaParamCorrespondanceDTO,TaAdresseDAO,TaAdresse>(dao,classModel);
			modelAdresse = new ModelParamCorrespondance(
				new EJBLookup<ITaTiersServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_TIERS_SERVICE, ITaTiersServiceRemote.class.getName()),
				TaTiers.class);
		} catch (NamingException e1) {
			e1.printStackTrace();
		}
		setVue(vue);

		// a faire avant l'initialisation du Binding
		vue.getTree().addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setSwtOldAdresse();
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
			//settGrille(vue.getGrille());
			initSashFormWeight(new int[]{100,0});
			vue.getPaFomulaire().setVisible(false);
			vue.getPaBtn().setVisible(false);
			
			
			setDaoStandard(dao);
			setTreeViewerStandard(treeViewer);
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
//	public List<TaParamCorrespondanceDTO> IHMmodel() {
//		LinkedList<TaAdresse> ldao = new LinkedList<TaAdresse>();
//		LinkedList<TaParamCorrespondanceDTO> lswt = new LinkedList<TaParamCorrespondanceDTO>();
//
//		if(masterEntity!=null && !masterEntity.getTaAdresses().isEmpty()) {
//
//			ldao.addAll(masterEntity.getTaAdresses());
//
//			LgrDozerMapper<TaAdresse,TaParamCorrespondanceDTO> mapper = new LgrDozerMapper<TaAdresse,TaParamCorrespondanceDTO>();
//			for (TaAdresse o : ldao) {
//				TaParamCorrespondanceDTO t = null;
//				t = (TaParamCorrespondanceDTO) mapper.map(o, TaParamCorrespondanceDTO.class);
//				lswt.add(t);
//			}
//
//		}
//		return lswt;
//	}

	protected void initEtatBouton() {
		initEtatBouton(IHMmodel());

//		boolean trouve = contientDesEnregistrement(IHMmodel());
//		switch (daoStandard.getModeObjet().getMode()) {
//		case C_MO_INSERTION:
//			enableActionAndHandler(C_COMMAND_GEOLOCALISATION_ID,false);
//			enableActionAndHandler(C_COMMAND_ITINERAIRE_ID,false);
//			break;
//		case C_MO_EDITION:
//			enableActionAndHandler(C_COMMAND_GEOLOCALISATION_ID,false);
//			enableActionAndHandler(C_COMMAND_ITINERAIRE_ID,false);
//			break;
//		case C_MO_CONSULTATION:
//			enableActionAndHandler(C_COMMAND_GEOLOCALISATION_ID,trouve);
//			enableActionAndHandler(C_COMMAND_ITINERAIRE_ID,trouve);
//			break;
//		default:
//			break;
//		}
	}

	@Override
	protected void initImageBouton() {
		super.initImageBouton();
//		String imageCarte = "/icons/map_magnify.png";
//		String imageIti = "/icons/map.png";
//		vue.getBtnGeolocalisation().setImage(TiersPlugin.getImageDescriptor(imageCarte).createImage());
//		vue.getBtnItineraire().setImage(TiersPlugin.getImageDescriptor(imageIti).createImage());
		vue.layout(true);
	}

	public void bind(PaParamCorrespondanceSWT paParamCorrespondanceSWT) {
		try {
			realm = SWTObservables.getRealm(vue.getParent().getDisplay());

			treeViewer = new LgrTreeViewer(new CheckboxTreeViewer(paParamCorrespondanceSWT.getTree()));
			treeViewer.createTableCol(classModel,paParamCorrespondanceSWT.getTree(), nomClassController,
					Const.C_FICHIER_LISTE_CHAMP_GRILLE,-1);
			listeChamp = treeViewer.setListeChampGrille(nomClassController,
					Const.C_FICHIER_LISTE_CHAMP_GRILLE);

			// Set up data binding.
			TaParamCorrespondanceDTO root = refreshTreeModel();

			IListProperty childrenProp = new DelegatingListProperty() {
				IListProperty inputChildren = BeanProperties.list("list", TaParamCorrespondanceDTO.class);
				protected IListProperty doGetDelegate(Object source) {
					if (source instanceof TaParamCorrespondanceDTO)
						return inputChildren;
					return null;
				}
			};

			IValueProperty[] labelProps = BeanProperties.values(TaParamCorrespondanceDTO.class ,listeChamp);

			ViewerSupportParamCorrespondance.bind(treeViewer.getViewer(),
					root,
					childrenProp,
					labelProps
			);
			treeViewer.getViewer().refresh();
			treeViewer.getViewer().expandAll();

			//selectedDocumentEditable = ViewersObservables.observeSingleSelection(treeViewer.getViewer());

			class LgrListener implements ISelectionChangedListener, ISelectionProvider {

				protected ListenerList listenerList = new ListenerList();

				@Override
				public void selectionChanged(SelectionChangedEvent event) {
//					if(event.getSelection() instanceof TreeSelection) {
//						Object o = ((TreeSelection)event.getSelection()).getFirstElement();
//						if(o instanceof TaParamCorrespondanceDTO) {
//							if(((TaParamCorrespondanceDTO)o).getLibelleDocumentDoc()!=null
//									/*&& ((SWTDocumentEditable)o).getCodeDocumentDoc()!=null*/) {
//								//setSelection(event.getSelection());
//								fireChangementDeSelection(new SelectionChangedEvent(this, getSelection()));
//							} else {
//							}
//						}
//					}
				}

				@Override
				public void setSelection(ISelection selection) {
					treeViewer.getViewer().setSelection(selection);
					fireChangementDeSelection(new SelectionChangedEvent(this, getSelection()));
					initEtatBouton();
				}

				@Override
				public void removeSelectionChangedListener(ISelectionChangedListener listener) {
					listenerList.remove(listener);
				}

				@Override
				public ISelection getSelection() {
//					if(treeViewer.getViewer().getSelection() instanceof TreeSelection) {
//						Object o = ((TreeSelection)treeViewer.getViewer().getSelection()).getFirstElement();
//						if(o instanceof TaParamCorrespondanceDTO) {
//							if(((TaParamCorrespondanceDTO)o).getLibelleDocumentDoc()!=null
//									/*&& ((SWTDocumentEditable)o).getCodeDocumentDoc()!=null*/) {
//								//setSelection(event.getSelection());
//								//fireChangementDeSelection(new SelectionChangedEvent(this, getSelection()));
//								return treeViewer.getViewer().getSelection();
//							} else {
//								return null;
//							}
//						}
//					}
					return treeViewer.getViewer().getSelection();
				}

				@Override
				public void addSelectionChangedListener(ISelectionChangedListener listener) {
					listenerList.add(listener);
				}

				protected void fireChangementDeSelection(SelectionChangedEvent e) {
					Object[] listeners = listenerList.getListeners();
					for (int i = listeners.length - 1; i >= 0; i -= 1) {
						if (listeners[i] == ISelectionChangedListener.class) {
							if (e == null)
								e = new SelectionChangedEvent(this,null);
							( (ISelectionChangedListener) listeners[i]).selectionChanged(e);
						}
					}
				}
			}

			TreeViewerColumn col0Libelle = new TreeViewerColumn(treeViewer.getViewer(), treeViewer.getViewer().getTree().getColumns()[0]);
			TreeViewerColumn col1Administratif = new TreeViewerColumn(treeViewer.getViewer(), treeViewer.getViewer().getTree().getColumns()[1]);
			TreeViewerColumn col2Commercial = new TreeViewerColumn(treeViewer.getViewer(), treeViewer.getViewer().getTree().getColumns()[2]);
			col0Libelle.setLabelProvider(new MyColumnLabelProvider(0));
			col1Administratif.setLabelProvider(new MyColumnLabelProvider(1));
			col2Commercial.setLabelProvider(new MyColumnLabelProvider(2));


			EditingSupport col1AdministratifEditingSupport = new ColBooleanEditingSupport(treeViewer.getViewer(),1);
			col1Administratif.setEditingSupport(col1AdministratifEditingSupport);
			
			EditingSupport col2CommercialEditingSupport = new ColBooleanEditingSupport(treeViewer.getViewer(),2);
			col2Commercial.setEditingSupport(col2CommercialEditingSupport);

			LgrListener l = new LgrListener();
			//treeViewer.getViewer().addSelectionChangedListener(l);
			selectedAdresse = ViewersObservables.observeSingleSelection(l);
			treeViewer.selectionGrille(0);

			dbc = new DataBindingContext(realm);

			dbc.getValidationStatusMap().addMapChangeListener(changeListener);
			treeViewer.selectionGrille(0);

			setTreeViewerStandard(treeViewer);
			setDbcStandard(dbc);

			//bindingFormMaitreDetail(dbc, realm, selectedAdresse,classModel);
			changementDeSelection();
			selectedAdresse.addChangeListener(new IChangeListener() {

				public void handleChange(ChangeEvent event) {
					changementDeSelection();
//					if(tableTypeDocViewer!=null) {
//						tableTypeDocViewer.setInput(new WritableList(modelTypeDoc.remplirListe(),classModelTypeDoc));
//					}
				}

			});

		} catch (Exception e) {
			vue.getLaMessage().setText(e.getMessage());
			logger.error("", e);
		}
	}
	
	private class MyColumnLabelProvider extends ColumnLabelProvider {
		private int column = 0;
		private Font f = null;
		
		public MyColumnLabelProvider(int column) {
			this.column = column;

			f = Display.getCurrent().getSystemFont();
			f = new Font(Display.getCurrent(),
					f.getFontData()[0].getName()
					,f.getFontData()[0].getHeight()
					,SWT.BOLD);
		}
		
		@Override
		public String getText(Object element) {
			switch (column) {
			case 0:
				//treeViewer.getViewer().getLabelProvider().
				if (element!=null 
					&& ((TaParamCorrespondanceDTO) element).getLibelle()!=null ) {
					return ((TaParamCorrespondanceDTO) element).getLibelle().toString();
				}
				break;
			default:
				break;
			}
			return null;
		}

		@Override
		public Image getImage(Object element) {
			switch (column) {
			case 0:
				//treeViewer.getViewer().getLabelProvider().
				if (element!=null 
					&& ((TaParamCorrespondanceDTO) element).getId()==null ) {
					if(((TaParamCorrespondanceDTO) element).getType() == TaParamCorrespondanceDTO.TYPE_EMAIL) {
						return LibrairiesEcranPlugin.ir.get(LibrairiesEcranPlugin.IMAGE_EMAIL);
					 } else if(((TaParamCorrespondanceDTO) element).getType() == TaParamCorrespondanceDTO.TYPE_TELEPHONE) {
						 return LibrairiesEcranPlugin.ir.get(LibrairiesEcranPlugin.IMAGE_SMS);
					 } else if(((TaParamCorrespondanceDTO) element).getType() == TaParamCorrespondanceDTO.TYPE_ADRESSE) {
						 return LibrairiesEcranPlugin.ir.get(LibrairiesEcranPlugin.IMAGE_ADRESSE_FAC);
					 }
					//return LibrairiesEcranPlugin.ir.get(LibrairiesEcranPlugin.IMAGE_REPERTOIRE);
				}
				break;
			case 1:
				LibrairiesLeGrainPlugin.initCheckImage(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
				if (element!=null 
						&& ((TaParamCorrespondanceDTO) element).getAdministratif()!=null 
						&& ((TaParamCorrespondanceDTO) element).getAdministratif()) {
					return LibrairiesLeGrainPlugin.ir.get(LibrairiesLeGrainPlugin.CHECK);
				} else {
					if( ((TaParamCorrespondanceDTO) element).getId()!=null )
						return LibrairiesLeGrainPlugin.ir.get(LibrairiesLeGrainPlugin.UNCHECK);
				}
				break;
			case 2:
				LibrairiesLeGrainPlugin.initCheckImage(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
				if (element!=null 
						&& ((TaParamCorrespondanceDTO) element).getCommercial()!=null 
						&& ((TaParamCorrespondanceDTO) element).getCommercial()) {
					return LibrairiesLeGrainPlugin.ir.get(LibrairiesLeGrainPlugin.CHECK);
				} else {
					if( ((TaParamCorrespondanceDTO) element).getId()!=null )
						return LibrairiesLeGrainPlugin.ir.get(LibrairiesLeGrainPlugin.UNCHECK);
				}
				break;
			default:
				break;
			}
			return null;
		}
		
		@Override
		public Font getFont(Object element) {
			if (element!=null 
					&& ((TaParamCorrespondanceDTO) element).getId()==null ) {
				return f;
			}
			return super.getFont(element);
		}
	}
	
	public void changeEtat(TaParamCorrespondanceDTO selection, String champ, boolean value) {
//		EntityTransaction transaction = null;
		try {	
			 if(selection.getType() == TaParamCorrespondanceDTO.TYPE_EMAIL) {
				 
				taEmail = daoEmail.findById(selection.getId());
//				transaction = daoEmail.getEntityManager().getTransaction();
				if(champ.equals("com"))
					taEmail.setCommCommercialEmail(LibConversion.booleanToInt(value));
				else if(champ.equals("adm"))
					taEmail.setCommAdministratifEmail(LibConversion.booleanToInt(value));
//				daoEmail.begin(transaction);

				daoEmail.enregistrerMerge(taEmail);
				
//				daoEmail.commit(transaction);
//				transaction = null;
			 } else if(selection.getType() == TaParamCorrespondanceDTO.TYPE_TELEPHONE) {
				taTelephone = daoTelephone.findById(selection.getId());
//				transaction = daoTelephone.getEntityManager().getTransaction();
				if(champ.equals("com"))
					taTelephone.setCommCommercialTelephone(LibConversion.booleanToInt(value));
				else if(champ.equals("adm"))
					taTelephone.setCommAdministratifTelephone(LibConversion.booleanToInt(value));
//				daoTelephone.begin(transaction);

				daoTelephone.enregistrerMerge(taTelephone);

//				daoTelephone.commit(transaction);
//				transaction = null;
			 } else if(selection.getType() == TaParamCorrespondanceDTO.TYPE_ADRESSE) {
				 taAdresse = dao.findById(selection.getId());
//				 transaction = dao.getEntityManager().getTransaction();
				 if(champ.equals("com"))
					 taAdresse.setCommCommercialAdresse(LibConversion.booleanToInt(value));
				 else if(champ.equals("adm"))
					 taAdresse.setCommAdministratifAdresse(LibConversion.booleanToInt(value));
//				 dao.begin(transaction);

				 dao.enregistrerMerge(taAdresse);

//				 dao.commit(transaction);
//				 transaction = null;
			 }

		} catch (RollbackException e) {	
			logger.error("",e);
		} catch (Exception e) {
			logger.error("",e);
		} finally {
//			if(transaction!=null && transaction.isActive()) {
//				transaction.rollback();
//			}
		}

	}
	
	public final class ColBooleanEditingSupport extends EditingSupport /*ObservableValueEditingSupport*/ {

		private CheckboxCellEditor cellEditor = null;
		private int column = 0;

		private final TreeViewer viewer;

		public ColBooleanEditingSupport(TreeViewer viewer, int column) {
			super(viewer);
			this.viewer = viewer;
			this.column = column;

			cellEditor = new CheckboxCellEditor((Composite) getViewer().getControl(), SWT.READ_ONLY);

			cellEditor.addListener(new ICellEditorListener() {

				@Override
				public void applyEditorValue() {
					String v = cellEditor.getValue().toString();
				}

				@Override
				public void cancelEditor() {
				}

				@Override
				public void editorValueChanged(boolean oldValidState, boolean newValidState) {					
				}

			});
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			return cellEditor;
		}

		@Override
		protected boolean canEdit(Object element) {
			TaParamCorrespondanceDTO person = (TaParamCorrespondanceDTO) element;
			if(person!=null && person.getId()!=null)
				return true;
			else
				return false;
		}

		@Override
		protected Object getValue(Object element) {
			TaParamCorrespondanceDTO person = (TaParamCorrespondanceDTO) element;
			switch (column) {
			case 1:
				return person.getAdministratif();
				//break;
			case 2:
				return person.getCommercial();
				//break;
			default:
				break;
			}
			return null;
		}

		@Override
		protected void setValue(Object element, Object value) {
			TaParamCorrespondanceDTO pers = (TaParamCorrespondanceDTO) element;
			switch (column) {
			case 1:
				System.out.println("administratif : "+value);
				pers.setAdministratif((Boolean) value);
				changeEtat(pers,"adm",LibConversion.StringToBoolean(value.toString()));
				break;
			case 2:
				System.out.println("commercial : "+value);
				pers.setCommercial((Boolean) value);
				changeEtat(pers,"com",LibConversion.StringToBoolean(value.toString()));
				break;
			default:
				break;
			}
			viewer.update(element, null);
		}

	}

	private TaParamCorrespondanceDTO refreshTreeModel() {
		return refreshTreeModel(null);
	}

	private TaParamCorrespondanceDTO refreshTreeModel(List<TaParamCorrespondanceDTO> list) {
		if(list == null) //on travaille à partir de la liste d'objet d'interface passée en paramètre (qui ne correspond pas forcement aux lignes en bdd)
			list = modelAdresse.remplirListe(masterEntity);
		//list = inverseStructureListe(list);
		TaParamCorrespondanceDTO root = new TaParamCorrespondanceDTO();
		root.setList(list);
		return root;
	}

	private void changementDeSelection() {
		try {
		if(selectedAdresse!=null && selectedAdresse.getValue()!=null) {
			if(((TaParamCorrespondanceDTO) selectedAdresse.getValue()).getId()!=null) {
				taAdresse = null;
				taEmail = null;
				taTelephone = null;
				if(((TaParamCorrespondanceDTO) selectedAdresse.getValue()).getType() == TaParamCorrespondanceDTO.TYPE_ADRESSE) {
					taAdresse = dao.findById(((TaParamCorrespondanceDTO) selectedAdresse.getValue()).getId());
				} else if(((TaParamCorrespondanceDTO) selectedAdresse.getValue()).getType() == TaParamCorrespondanceDTO.TYPE_EMAIL) {
					taEmail = daoEmail.findById(((TaParamCorrespondanceDTO) selectedAdresse.getValue()).getId());
				} else if(((TaParamCorrespondanceDTO) selectedAdresse.getValue()).getType() == TaParamCorrespondanceDTO.TYPE_TELEPHONE) {
					taTelephone = daoTelephone.findById(((TaParamCorrespondanceDTO) selectedAdresse.getValue()).getId());
				}
				taAdresse = dao.findById(((TaParamCorrespondanceDTO) selectedAdresse.getValue()).getId());
			}
			//null a tester ailleurs, car peut etre null en cas d'insertion
			fireChangementDeSelection(new ChangementDeSelectionEvent(SWTPaParamCorrespondanceController.this));
		}
		} catch (FinderException e) {
			logger.error("",e);
		}
	}

	public Composite getVue() {
		return vue;
	}

	public ResultAffiche configPanel(ParamAffiche param) {
		if (param != null) {
			Map<String,String[]> map = dao.getParamWhereSQL();
			//			ibTaTable.ouvreDataset();
			if (((ParamAfficheParamCorrespondance) param).getFocusDefautSWT() != null && !((ParamAfficheParamCorrespondance) param).getFocusDefautSWT().isDisposed())
				((ParamAfficheParamCorrespondance) param).getFocusDefautSWT().setFocus();
			else
				((ParamAfficheParamCorrespondance) param).setFocusDefautSWT(vue.getTree());
			vue.getLaTitreFormulaire().setText(
					((ParamAfficheParamCorrespondance) param).getTitreFormulaire());
			vue.getLaTitreGrille().setText(
					((ParamAfficheParamCorrespondance) param).getTitreGrille());
			vue.getLaTitreFenetre().setText(
					((ParamAfficheParamCorrespondance) param).getSousTitre());


			if (param.getEcranAppelant() != null) {
				ecranAppelant = param.getEcranAppelant();
			}

			if(treeViewer==null) {
				//databinding pas encore realise
				bind(vue);
				treeViewer.tri(classModel, nomClassController,Const.C_FICHIER_LISTE_CHAMP_GRILLE);
			} else {
				try {
					taAdresse=null;
					selectedAdresse.setValue(null);
					actRefresh();
				} catch (Exception e) {
					logger.error("",e);
				}
			}
			// permet de se positionner sur le 1er enregistrement et de remplir
			// le formulaire

			//			tableViewer.tri(classModel, nomClassController,Const.C_FICHIER_LISTE_CHAMP_GRILLE);
			VerrouInterface.setVerrouille(false);
			setSwtOldAdresse();

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
		
//		mapInfosVerifSaisie.put(vue.getTfADRESSE1_ADRESSE(), new InfosVerifSaisie(new TaAdresse(),Const.C_ADRESSE1_ADRESSE,null));
//		mapInfosVerifSaisie.put(vue.getTfADRESSE2_ADRESSE(), new InfosVerifSaisie(new TaAdresse(),Const.C_ADRESSE2_ADRESSE,null));
//		mapInfosVerifSaisie.put(vue.getTfADRESSE3_ADRESSE(), new InfosVerifSaisie(new TaAdresse(),Const.C_ADRESSE3_ADRESSE,null));
//		mapInfosVerifSaisie.put(vue.getTfCODEPOSTAL_ADRESSE(), new InfosVerifSaisie(new TaAdresse(),Const.C_CODEPOSTAL_ADRESSE,null));
//		mapInfosVerifSaisie.put(vue.getTfVILLE_ADRESSE(), new InfosVerifSaisie(new TaAdresse(),Const.C_VILLE_ADRESSE,null));
//		mapInfosVerifSaisie.put(vue.getTfPAYS_ADRESSE(), new InfosVerifSaisie(new TaAdresse(),Const.C_PAYS_ADRESSE,null));
//		mapInfosVerifSaisie.put(vue.getTfCODE_T_ADR(), new InfosVerifSaisie(new TaTAdr(),Const.C_CODE_T_ADR,null));

		initVerifyListener(mapInfosVerifSaisie, dao);
	}

	protected void initComposantsVue() throws ExceptLgr {
	}

	protected void initMapComposantChamps() {
		if (mapComposantChamps == null)
			mapComposantChamps = new LinkedHashMap<Control, String>();

		if (listeComposantFocusable == null)
			listeComposantFocusable = new ArrayList<Control>();
		listeComposantFocusable.add(vue.getTree());

//		vue.getTfADRESSE1_ADRESSE().setToolTipText(Const.C_ADRESSE1_ADRESSE);
//		vue.getTfADRESSE2_ADRESSE().setToolTipText(Const.C_ADRESSE2_ADRESSE);
//		vue.getTfADRESSE3_ADRESSE().setToolTipText(Const.C_ADRESSE3_ADRESSE);
//		vue.getTfCODEPOSTAL_ADRESSE().setToolTipText(Const.C_CODEPOSTAL_ADRESSE);
//		vue.getTfVILLE_ADRESSE().setToolTipText(Const.C_VILLE_ADRESSE);
//		vue.getTfPAYS_ADRESSE().setToolTipText(Const.C_PAYS_ADRESSE);
//		vue.getTfCODE_T_ADR().setToolTipText(Const.C_CODE_T_ADR);
//
//		mapComposantChamps.put(vue.getTfADRESSE1_ADRESSE(), Const.C_ADRESSE1_ADRESSE);
//		mapComposantChamps.put(vue.getTfADRESSE2_ADRESSE(), Const.C_ADRESSE2_ADRESSE);
//		mapComposantChamps.put(vue.getTfADRESSE3_ADRESSE(),Const.C_ADRESSE3_ADRESSE);
//		mapComposantChamps.put(vue.getTfCODEPOSTAL_ADRESSE(),Const.C_CODEPOSTAL_ADRESSE);
//		mapComposantChamps.put(vue.getTfVILLE_ADRESSE(),Const.C_VILLE_ADRESSE);
//		mapComposantChamps.put(vue.getTfPAYS_ADRESSE(),Const.C_PAYS_ADRESSE);
//		mapComposantChamps.put(vue.getTfCODE_T_ADR(),Const.C_CODE_T_ADR);

		for (Control c : mapComposantChamps.keySet()) {
			listeComposantFocusable.add(c);
		}

//		listeComposantFocusable.add(vue.getBtnGeolocalisation());
//		listeComposantFocusable.add(vue.getBtnItineraire());
//
//		listeComposantFocusable.add(vue.getBtnEnregistrer());
//		listeComposantFocusable.add(vue.getBtnInserer());
//		listeComposantFocusable.add(vue.getBtnModifier());
//		listeComposantFocusable.add(vue.getBtnSupprimer());
//		listeComposantFocusable.add(vue.getBtnFermer());
//		listeComposantFocusable.add(vue.getBtnAnnuler());
//		listeComposantFocusable.add(vue.getBtnImprimer());

		if (mapInitFocus == null)
			mapInitFocus = new LinkedHashMap<EnumModeObjet, Control>();
		mapInitFocus.put(EnumModeObjet.C_MO_INSERTION, vue
				.getTfADRESSE1_ADRESSE());
		mapInitFocus.put(EnumModeObjet.C_MO_EDITION, vue
				.getTfADRESSE1_ADRESSE());
		mapInitFocus.put(EnumModeObjet.C_MO_CONSULTATION, vue
				.getTree());

		activeModifytListener();
	}

	protected void initActions() {
		mapCommand = new HashMap<String, IHandler>();

//		mapCommand.put(C_COMMAND_GLOBAL_MODIFIER_ID, handlerModifier);
//		mapCommand.put(C_COMMAND_GLOBAL_ANNULER_ID, handlerAnnuler);
//		mapCommand.put(C_COMMAND_GLOBAL_ENREGISTRER_ID, handlerEnregistrer);
//		mapCommand.put(C_COMMAND_GLOBAL_INSERER_ID, handlerInserer);
//		mapCommand.put(C_COMMAND_GLOBAL_SUPPRIMER_ID, handlerSupprimer);
//		mapCommand.put(C_COMMAND_GLOBAL_REFRESH_ID, handlerRefresh);
//		mapCommand.put(C_COMMAND_GLOBAL_AIDE_ID, handlerAide);
//		mapCommand.put(C_COMMAND_GLOBAL_FERMER_ID, handlerFermer);
//		mapCommand.put(C_COMMAND_GLOBAL_IMPRIMER_ID, handlerImprimer);

		mapCommand.put(C_COMMAND_GLOBAL_PRECEDENT_ID, handlerPrecedent);
		mapCommand.put(C_COMMAND_GLOBAL_SUIVANT_ID, handlerSuivant);

		initFocusCommand(String.valueOf(this.hashCode()),listeComposantFocusable,mapCommand);

		if (mapActions == null)
			mapActions = new LinkedHashMap<Button, Object>();

//		mapActions.put(vue.getBtnAnnuler(), C_COMMAND_GLOBAL_ANNULER_ID);
//		mapActions.put(vue.getBtnEnregistrer(), C_COMMAND_GLOBAL_ENREGISTRER_ID);
//		mapActions.put(vue.getBtnInserer(), C_COMMAND_GLOBAL_INSERER_ID);
//		mapActions.put(vue.getBtnModifier(), C_COMMAND_GLOBAL_MODIFIER_ID);
//		mapActions.put(vue.getBtnSupprimer(), C_COMMAND_GLOBAL_SUPPRIMER_ID);
//		mapActions.put(vue.getBtnFermer(), C_COMMAND_GLOBAL_FERMER_ID);
//		mapActions.put(vue.getBtnImprimer(), C_COMMAND_GLOBAL_IMPRIMER_ID);

		Object[] tabActionsAutres = new Object[] { C_COMMAND_GLOBAL_AIDE_ID, C_COMMAND_GLOBAL_REFRESH_ID };
		mapActions.put(null, tabActionsAutres);

	}

	public SWTPaParamCorrespondanceController getThis() {
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
				setListeEntity(getModelAdresse().remplirListe());
				dao.initValeurIdTable(taAdresse);
				fireRetourEcran(new RetourEcranEvent(this, new ResultAide(
						dao.getChampIdEntite(), dao.getValeurIdTable(),
						selectedAdresse.getValue())));

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
					if(getFocusAvantAideSWT().equals(vue.getTfCODE_T_ADR())){
						TaTAdr u = null;
						//TaTAdrDAO t = new TaTAdrDAO(getEm());
						ITaTAdrServiceRemote t = new EJBLookup<ITaTAdrServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_T_ADRESSE_SERVICE, ITaTAdrServiceRemote.class.getName());
						u = t.findById(Integer.parseInt(((ResultAffiche) evt.getRetour()).getIdResult()));
						t = null;
						taAdresse.setTaTAdr(u);
					}
					ctrlUnChampsSWT(getFocusAvantAideSWT());
				} catch (Exception e) {
					logger.error("",e);
					vue.getLaMessage().setText(e.getMessage());
				}
			}
			if (getFocusAvantAideSWT() instanceof Table) {
				if (getFocusAvantAideSWT() == vue.getTree()) {
					//ibTaTable.lgrLocateID(((ResultAffiche) evt.getRetour()).getIdResult());
					if(((ResultAffiche) evt.getRetour()).getSelection()!=null)
						treeViewer.getViewer().setSelection(((ResultAffiche) evt.getRetour()).getSelection(),true);
				}
			}
		} else if (evt.getRetour() != null) {
			if (getFocusAvantAideSWT() instanceof Table) {
				if (getFocusAvantAideSWT() == vue.getTree()) {
					//ibTaTable.lgrLocateID(((ResultAffiche) evt.getRetour()).getIdResult());
					if(((ResultAffiche) evt.getRetour()).getSelection()!=null)
						treeViewer.getViewer().setSelection(((ResultAffiche) evt.getRetour()).getSelection(),true);
				}
			}
		}
		super.retourEcran(evt);
	}

	@Override
	protected void actInserer() throws Exception {
//		try {
//			boolean continuer=true;
//			VerrouInterface.setVerrouille(true);
//			masterDAO.verifAutoriseModification(masterEntity);
//			setSwtOldAdresse();
//			if(LgrMess.isDOSSIER_EN_RESEAU()){
//				DeclencheCommandeControllerEvent e = new DeclencheCommandeControllerEvent(this,C_COMMAND_GLOBAL_MODIFIER_ID);
//				fireDeclencheCommandeController(e);
//				continuer=masterDAO.dataSetEnModif();
//			}
//			if(continuer){
////				setMasterEntity(masterDAO.findById(masterEntity.getIdTiers()));
//				swtAdresse = new TaParamCorrespondanceDTO();
//				swtAdresse.setIdTiers(idTiers);
//				dao.getModeObjet().setMode(EnumModeObjet.C_MO_INSERTION);
//				taAdresse = new TaAdresse();
//				taAdresse.setPaysAdresse("FRANCE");
//				List l = IHMmodel();
//				l.add(swtAdresse);
//				writableList = new WritableList(realm, l, classModel);
//				treeViewer.setInput(writableList);
//				treeViewer.getViewer().refresh();
//				treeViewer.getViewer().setSelection(new StructuredSelection(swtAdresse));
//				initEtatBouton();
//
//				try {
//					DeclencheCommandeControllerEvent e = new DeclencheCommandeControllerEvent(this,C_COMMAND_GLOBAL_MODIFIER_ID);
//					fireDeclencheCommandeController(e);
//				} catch (Exception e) {
//					logger.error("",e);
//				}
//			}
//		} catch (Exception e1) {
//			vue.getLaMessage().setText(e1.getMessage());
//			logger.error("Erreur : actionPerformed", e1);
//		} finally {
//			initEtatComposant();
//			VerrouInterface.setVerrouille(false);
//		}
	}

	@Override
	protected void actModifier() throws Exception {
//		try {
//			boolean continuer=true;
//			setSwtOldAdresse();
//			if(LgrMess.isDOSSIER_EN_RESEAU()){
//				DeclencheCommandeControllerEvent e = new DeclencheCommandeControllerEvent(this,C_COMMAND_GLOBAL_MODIFIER_ID);
//				fireDeclencheCommandeController(e);
//				continuer=masterDAO.dataSetEnModif();
//			}
//			if(continuer){
////				setMasterEntity(masterDAO.findById(masterEntity.getIdTiers()));
//				for (TaAdresse p : masterEntity.getTaAdresses()) {
//					if(p.getIdAdresse()==((TaParamCorrespondanceDTO) selectedAdresse.getValue()).getId()) {
//						taAdresse = p;
////						if(LgrMess.isDOSSIER_EN_RESEAU())actRefresh();
//					}
//				}
//				masterDAO.verifAutoriseModification(masterEntity);
//				DeclencheCommandeControllerEvent e = new DeclencheCommandeControllerEvent(this,C_COMMAND_GLOBAL_MODIFIER_ID);
//				fireDeclencheCommandeController(e);
//				dao.getModeObjet().setMode(EnumModeObjet.C_MO_EDITION);
//				initEtatBouton();	
//			}
//		} catch (Exception e1) {
//			vue.getLaMessage().setText(e1.getMessage());
//			logger.error("Erreur : actionPerformed", e1);
//		}
	}

	@Override
	protected void actSupprimer() throws Exception {
//		EntityTransaction transaction = dao.getEntityManager().getTransaction();		
//		try {
//			boolean continuer=true;
//			VerrouInterface.setVerrouille(true);
//			if(isUtilise())MessageDialog.openInformation(vue.getShell(), MessagesEcran
//					.getString("Message.Attention"), MessagesEcran
//					.getString("Message.suppression"));
//			else
//				if(LgrMess.isDOSSIER_EN_RESEAU()){
//					DeclencheCommandeControllerEvent e = new DeclencheCommandeControllerEvent(this,C_COMMAND_GLOBAL_MODIFIER_ID);
//					fireDeclencheCommandeController(e);
//					continuer=masterDAO.dataSetEnModif();
//				}
//
//			if(continuer){
//					setMasterEntity(masterDAO.findById(masterEntity.getIdTiers()));
//					if (MessageDialog.openConfirm(vue.getShell(), MessagesEcran
//							.getString("Message.Attention"), MessagesEcran
//							.getString("Adresse.Message.Supprimer"))) {				
//						dao.getModeObjet().setMode(EnumModeObjet.C_MO_SUPPRESSION);
//						try {
//							DeclencheCommandeControllerEvent e = new DeclencheCommandeControllerEvent(this,C_COMMAND_GLOBAL_MODIFIER_ID);
//							fireDeclencheCommandeController(e);
//						} catch (Exception e) {
//							logger.error("",e);
//						}
//						for (TaAdresse p : masterEntity.getTaAdresses()) {
//							if(p.getIdAdresse()==((TaParamCorrespondanceDTO) selectedAdresse.getValue()).getIdAdresse()) {
//								taAdresse = p;
//							}
//						}
//						dao.begin(transaction);
//						dao.supprimer(taAdresse);
//						taAdresse.setTaTiers(null);
//						masterEntity.removeAdresse(taAdresse);
//						Object suivant=treeViewer.getElementAt(treeViewer.getViewer().getTree().getSelectionIndex()+1);
//						taAdresse=masterEntity.getTaAdresse();
//						dao.commit(transaction);
//						transaction=null;
//						if(suivant!=null)treeViewer.getViewer().setSelection(new StructuredSelection(suivant),true);
//						else treeViewer.selectionGrille(0);
//						try {
//							DeclencheCommandeControllerEvent e = new DeclencheCommandeControllerEvent(this,C_COMMAND_GLOBAL_ENREGISTRER_ID);
//							fireDeclencheCommandeController(e);
//						} catch (Exception e) {
//							logger.error("",e);
//						}
//						
//						actRefresh();		
//						initEtatBouton();
//
//					}
//			}
//		} catch (Exception e1) {
//			modelAdresse.addEntity(taAdresse);
//			vue.getLaMessage().setText(e1.getMessage());
//			logger.error("Erreur : actionPerformed", e1);
//		} finally {
//			if(transaction!=null && transaction.isActive()) {
//				transaction.rollback();
//			}
//			initEtatBouton();
//			VerrouInterface.setVerrouille(false);
//		}
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
					int rang =((WritableList)treeViewer.getViewer().getInput()).indexOf(selectedAdresse.getValue());
					remetTousLesChampsApresAnnulationSWT(dbc);
					List<TaParamCorrespondanceDTO> l = IHMmodel();
					if(rang!=-1)
						l.set(rang, swtOldAdresse);

					writableList = new WritableList(realm, l, classModel);
					treeViewer.setInput(writableList);
					treeViewer.getViewer().refresh();
					treeViewer.getViewer().setSelection(new StructuredSelection(swtOldAdresse), true);
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
//
//		TaInfoEntrepriseDAO taInfoEntrepriseDAO = new TaInfoEntrepriseDAO(getEm());
//		TaInfoEntreprise taInfoEntreprise = taInfoEntrepriseDAO.findInstance();
//
//		String nomChampIdTable =  dao.getChampIdTable();
//
//		FonctionGetInfosXmlAndProperties fonctionGetInfosXmlAndProperties = new FonctionGetInfosXmlAndProperties(mapperUIToModel);
//		fonctionGetInfosXmlAndProperties.cleanValueMapAttributeTable();
//
//		fonctionGetInfosXmlAndProperties.setValueMapAttributeTable(TaAdresse.class.getSimpleName()+".head");
//		fonctionGetInfosXmlAndProperties.setValueMapAttributeTable(TaAdresse.class.getSimpleName()+".detail");
//
//
//		LinkedHashMap<String,AttributElementResport> mapAttributeTablHead = fonctionGetInfosXmlAndProperties.getMapAttributeTablHead(); 
//		LinkedHashMap<String,AttributElementResport> mapAttributeTablDetail = fonctionGetInfosXmlAndProperties.getMapAttributeTablDetail();
//
//		Collection<TaAdresse> collectionTaTelephone = masterEntity.getTaAdresses();
//
//		fonctionGetInfosXmlAndProperties.findInfosFileXml(TaAdresse.class.getName(),TaParamCorrespondanceDTO.class.getName(),
//				listeChamp,"mapping");
//		fonctionGetInfosXmlAndProperties.getInfosObjetJPA(taAdresse);
//
//		ConstEdition constEdition = new ConstEdition(); 
//		Impression impression = new Impression(constEdition,taAdresse,collectionTaTelephone,nomChampIdTable,taAdresse.getIdAdresse());
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
//			constEdition.addValueList(treeViewer, nomClassController);
//
//			/**
//			 * pathFileReport ==> le path de ficher de edition dynamique
//			 */
//			String folderEditionDynamique = Const.C_RCP_INSTANCE_LOCATION+"/"+Const.C_NOM_PROJET_TMP+"/"+TaAdresse.class.getSimpleName();
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
//			DynamiqueReport.setNomObjet(TaAdresse.class.getSimpleName());
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
//			DynamiqueReport.setSimpleNameEntity(TaAdresse.class.getSimpleName());
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
//			impression.imprimer(true,pathFileReportDynamic,null,"Adresses",TaAdresse.class.getSimpleName(),false);
//		}

	}

	@Override
	protected boolean aideDisponible() {
		boolean result = false;
		switch (getModeEcran().getMode()) {
		case C_MO_CONSULTATION:
//			if(getFocusCourantSWT().equals(vue.getTree()))
//				result = true;
			break;
		case C_MO_EDITION:
		case C_MO_INSERTION:
//			if(getFocusCourantSWT().equals(vue.getTfCODE_T_ADR()))
//				result = true;
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
				//				paramAfficheAideRecherche.setDb(getThis().getIbTaTable().getFIBBase());
				paramAfficheAideRecherche.setMessage(message);
				// Creation de l'ecran d'aide principal
				Shell s = new Shell(PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell(),
						LgrShellUtil.styleLgr);
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

				switch (getModeEcran().getMode()) {
				case C_MO_CONSULTATION:
					if(getFocusCourantSWT().equals(vue.getTree())){
//						PaAdresseSWT paAdresseSWT = new PaAdresseSWT(s2,SWT.NULL);
//						SWTPaAdresseController swtPaAdresseController = new SWTPaAdresseController(paAdresseSWT);
//
////						editorCreationId = EditorAdresse.ID;
////						editorInputCreation = new EditorInputAdresse();
//						
//						paramAfficheAideRecherche.setForceAffichageAideRemplie(true);
//						paramAfficheAideRecherche.setAfficheDetail(false);
//
//						ParamAfficheAdresse paramAfficheAdresse = new ParamAfficheAdresse();
//						paramAfficheAideRecherche.setJPQLQuery(dao.getJPQLQuery());
//						paramAfficheAdresse.setModeEcran(EnumModeObjet.C_MO_INSERTION);
//						paramAfficheAdresse.setEcranAppelant(paAideController);
//						controllerEcranCreation = swtPaAdresseController;
//						parametreEcranCreation = paramAfficheAdresse;
//
//						paramAfficheAideRecherche.setTypeEntite(TaAdresse.class);
//						paramAfficheAideRecherche.setChampsRecherche(Const.C_ADRESSE1_ADRESSE);
//						paramAfficheAideRecherche.setDebutRecherche(vue.getTfADRESSE1_ADRESSE().getText());
//						paramAfficheAideRecherche.setControllerAppelant(getThis());
//						paramAfficheAideRecherche.setModel(swtPaAdresseController.getModelAdresse());
//						paramAfficheAideRecherche.setTypeObjet(swtPaAdresseController.getClassModel());
//						paramAfficheAideRecherche.setChampsIdentifiant(Const.C_ID_ADRESSE);
					}
					break;
				case C_MO_EDITION:
				case C_MO_INSERTION:
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
//						paramAfficheAideRecherche.setModel(new ModelGeneralObjet<SWTTAdr,TaTAdrDAO,TaTAdr>(SWTTAdr.class,dao.getEntityManager()));
//
//						paramAfficheAideRecherche.setTypeObjet(swtPaTAdrController.getClassModel());
//
//						paramAfficheAideRecherche.setChampsIdentifiant(swtPaTAdrController.getDao().getChampIdTable());
//					}
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

					// Parametrage de l'ecran d'aide principal
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

				}

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
//			IStatus s = null;
//
////			if(nomChamp.equals(Const.C_CODE_T_ADR)) {
////				TaTAdrDAO dao = new TaTAdrDAO(getEm());
////
////				dao.setModeObjet(getDao().getModeObjet());
////				TaTAdr f = new TaTAdr();
////				PropertyUtils.setSimpleProperty(f, nomChamp, value);
////				s = dao.validate(f,nomChamp,validationContext);
////
////				if(s.getSeverity()!=IStatus.ERROR) {
////					f = dao.findByCode((String)value);
////					taAdresse.setTaTAdr(f);
////				}
////				dao = null;
////			} else {
//				TaAdresse u = new TaAdresse();
//				u.setTaTiers(masterEntity);
//				PropertyUtils.setSimpleProperty(u, nomChamp, value);
//				s = dao.validate(u,nomChamp,validationContext);
//
////			}
//			return s;
			
			//IStatus s = null;
			
			IStatus resultat = new Status(IStatus.OK,TiersPlugin.PLUGIN_ID,"validateUIField champ : "+nomChamp!=null?nomChamp:"null"+" valeur : "+value!=null?value.toString():"valeur nulle"+" validation OK");
			
			int VALIDATION_CLIENT = 1;
			int VALIDATION_SERVEUR = 2;
			int VALIDATION_CLIENT_ET_SERVEUR = 3;
			
			//int TYPE_VALIDATION = VALIDATION_CLIENT;
			//int TYPE_VALIDATION = VALIDATION_SERVEUR;
			int TYPE_VALIDATION = VALIDATION_CLIENT_ET_SERVEUR;
			
			AbstractApplicationDAOClient<TaAdresseDTO> a = new AbstractApplicationDAOClient<TaAdresseDTO>();
			
			try {
				TaAdresseDTO u = new TaAdresseDTO();
				//u.setTaTiers(masterEntity);
				PropertyUtils.setSimpleProperty(u, nomChamp, value);
				dao.validateDTOProperty(u,nomChamp,validationContext);
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
				LgrDozerMapper<TaParamCorrespondanceDTO,TaAdresse> mapper = new LgrDozerMapper<TaParamCorrespondanceDTO,TaAdresse>();
				mapper.map((TaParamCorrespondanceDTO) selectedAdresse.getValue(),taAdresse);
				masterEntity.addAdresse(taAdresse);

			} else if ((modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION) == 0)) {
				LgrDozerMapper<TaParamCorrespondanceDTO,TaAdresse> mapper = new LgrDozerMapper<TaParamCorrespondanceDTO,TaAdresse>();
				mapper.map((TaParamCorrespondanceDTO) selectedAdresse.getValue(),taAdresse);
				taAdresse.setTaTiers(masterEntity);
				masterEntity.addAdresse(taAdresse);				
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

//			dao.commit(transaction);
			taAdresse=masterEntity.getTaAdresse();
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
//			vue.getTfCODE_T_ADR().setEditable(!isUtilise());
			changeCouleur(vue);
		} catch (Exception e) {
			vue.getLaMessage().setText(e.getMessage());
		}
	}

	public boolean isUtilise(){
//		return (((TaParamCorrespondanceDTO)selectedAdresse.getValue()).getIdAdresse()!=null && 
//				!dao.recordModifiable(dao.getNomTable(),
//						((TaParamCorrespondanceDTO)selectedAdresse.getValue()).getIdAdresse()))||
//						!masterDAO.autoriseModification(masterEntity);	
		return false;
	}

	public TaParamCorrespondanceDTO getSwtOldAdresse() {
		return swtOldAdresse;
	}

	public void setSwtOldAdresse(TaParamCorrespondanceDTO swtOldAdresse) {
		this.swtOldAdresse = swtOldAdresse;
	}

	public void setSwtOldAdresse() {
		if (selectedAdresse.getValue() != null)
			this.swtOldAdresse = TaParamCorrespondanceDTO.copy((TaParamCorrespondanceDTO) selectedAdresse.getValue());
		else {
			if (treeViewer.selectionGrille(0)){
				this.swtOldAdresse = TaParamCorrespondanceDTO.copy((TaParamCorrespondanceDTO) selectedAdresse.getValue());
				treeViewer.getViewer().setSelection(new StructuredSelection(
						(TaParamCorrespondanceDTO) selectedAdresse.getValue()), true);
			}
		}
	}

	public void setVue(PaParamCorrespondanceSWT vue) {
		super.setVue(vue);
		this.vue = vue;
	}

	@Override
	protected void initMapComposantDecoratedField() {
		if (mapComposantDecoratedField == null)
			mapComposantDecoratedField = new LinkedHashMap<Control, ControlDecoration>();
//		mapComposantDecoratedField.put(vue.getTfADRESSE1_ADRESSE(), vue
//				.getFieldADRESSE1_ADRESSE());
//		mapComposantDecoratedField.put(vue.getTfADRESSE2_ADRESSE(), vue
//				.getFieldADRESSE2_ADRESSE());
//		mapComposantDecoratedField.put(vue.getTfADRESSE3_ADRESSE(), vue
//				.getFieldADRESSE3_ADRESSE());
//		mapComposantDecoratedField.put(vue.getTfCODEPOSTAL_ADRESSE(), vue
//				.getFieldCODEPOSTAL_ADRESSE());
//		mapComposantDecoratedField.put(vue.getTfVILLE_ADRESSE(), vue
//				.getFieldVILLE_ADRESSE());
//		mapComposantDecoratedField.put(vue.getTfPAYS_ADRESSE(), vue
//				.getFieldPAYS_ADRESSE());
//		mapComposantDecoratedField.put(vue.getTfCODE_T_ADR(), vue
//				.getFieldCODE_T_ADR());
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

//		//		//repositionnement sur la valeur courante
//		int idActuel = 0;
//		if (taAdresse!=null) { //enregistrement en cours de modification/insertion
//			idActuel = taAdresse.getIdAdresse();
//		} else 
//			if(selectedAdresse!=null && (TaParamCorrespondanceDTO) selectedAdresse.getValue()!=null) {
//				idActuel = ((TaParamCorrespondanceDTO) selectedAdresse.getValue()).getIdAdresse();
//			}
//		//rafraichissement des valeurs dans la grille
//		//tableViewer.setInput(null);
//		writableList = new WritableList(realm,IHMmodel(), classModel);
//		treeViewer.setInput(writableList);
//
//
//		if(idActuel!=0) {
//			treeViewer.getViewer().setSelection(new StructuredSelection(recherche(Const.C_ID_ADRESSE, idActuel)));
//		}else
//			treeViewer.selectionGrille(0);		
		TreePath[] expandedTreePaths = treeViewer.getViewer().getExpandedTreePaths();

		treeViewer.setInput(refreshTreeModel());

		treeViewer.getViewer().setExpandedTreePaths(expandedTreePaths);

		treeViewer.selectionGrille(
				treeViewer.selectionGrille(selectedAdresse.getValue()));
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

	public ModelParamCorrespondance getModelAdresse() {
		return modelAdresse;
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

	public ITaAdresseServiceRemote getDao() {
		return dao;
	}
	public void modifMode(){
		if (!VerrouInterface.isVerrouille() ){
			try {
				if(!getModeEcran().dataSetEnModif()) {
					if(!masterEntity.getTaAdresses().isEmpty()) {
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

	@Override
	public List IHMmodel() {
		// TODO Auto-generated method stub
		return null;
	}
}
