package fr.legrain.document.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.DecoratedField;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import fr.legrain.bdg.documents.service.remote.ITaAcompteServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAvoirServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaReglementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaRemiseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceLocal;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.document.DocumentPlugin;
import fr.legrain.document.divers.DeclencheChangementEtatEvent;
import fr.legrain.document.divers.IDeclencheChangementEtatListener;
import fr.legrain.document.divers.ParamAfficheListeExporte;
import fr.legrain.document.divers.TypeDoc;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.ecran.PaCritereListeDocumentCochable;
import fr.legrain.document.ecran.PaSelectionLigneDocumentCochable;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TaRemise;
import fr.legrain.documents.dao.IDocumentDAO;
import fr.legrain.dossier.dao.TaInfoEntrepriseDAO;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.gestCom.Appli.Const;
import fr.legrain.gestCom.Appli.ModelGeneralObjetEJB;
import fr.legrain.gestCom.Module_Tiers.SWTTiers;
import fr.legrain.gestCom.librairiesEcran.LibrairiesEcranPlugin;
import fr.legrain.gestCom.librairiesEcran.editor.EditorAide;
import fr.legrain.gestCom.librairiesEcran.editor.EditorInputAide;
import fr.legrain.gestCom.librairiesEcran.swt.EJBBaseControllerSWTStandard;
import fr.legrain.gestCom.librairiesEcran.swt.LgrShellUtil;
import fr.legrain.gestCom.librairiesEcran.swt.LibDateTime;
import fr.legrain.gestCom.librairiesEcran.swt.ParamAfficheAideRechercheSWT;
import fr.legrain.gestCom.librairiesEcran.swt.ParamAfficheAideSWT;
import fr.legrain.gestCom.librairiesEcran.swt.SWTPaAideControllerSWT;
import fr.legrain.gestCom.librairiesEcran.swt.SWTPaAideRechercheControllerSWT;
import fr.legrain.gestCom.librairiesEcran.workbench.ChangementDePageEvent;
import fr.legrain.gestCom.librairiesEcran.workbench.EJBLgrEditorPart;
import fr.legrain.gestCom.librairiesEcran.workbench.LgrPartListener;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.lib.data.InfosVerifSaisie;
import fr.legrain.lib.data.ModeObjet;
import fr.legrain.lib.data.VerrouInterface;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.ejb.EJBLookup;
import fr.legrain.lib.gui.ParamAffiche;
import fr.legrain.lib.gui.ResultAffiche;
import fr.legrain.lib.gui.RetourEcranEvent;
import fr.legrain.lib.gui.RetourEcranListener;
import fr.legrain.lib.gui.aide.PaAideRechercheSWT;
import fr.legrain.lib.gui.aide.PaAideSWT;
import fr.legrain.lib.gui.aide.ParamAfficheAide;
import fr.legrain.libMessageLGR.LgrMess;
import fr.legrain.tiers.clientutility.JNDILookupClass;
import fr.legrain.tiers.dao.TaTiers;
import fr.legrain.tiers.dao.TaTiersDAO;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.ecran.PaTiersSWT;
import fr.legrain.tiers.ecran.ParamAfficheTiers;
import fr.legrain.tiers.ecran.SWTPaTiersController;
import fr.legrain.tiers.editor.EditorInputTiers;
import fr.legrain.tiers.editor.TiersMultiPageEditor;


public class PaCritereListeDocumentCochableController extends EJBBaseControllerSWTStandard 
implements RetourEcranListener,IDeclencheChangementEtatListener{

	static Logger logger = Logger.getLogger(PaCritereListeDocumentCochableController.class.getName());	
	private PaCritereListeDocumentCochable  vue = null;
	private Date dateDeb=null;
	private Date dateFin=null;
	private Integer indiceAppelant=null;
	
	private ITaFactureServiceRemote daoFacture = null;
	private ITaAvoirServiceRemote daoAvoir = null;
	private ITaAcompteServiceRemote daoAcompte = null;
	private ITaReglementServiceRemote daoReglement = null;
	private ITaRemiseServiceRemote daoRemise = null;
	
	
	private List<IDocumentTiers> modelDocument = null;
	
	TaInfoEntreprise infos =null;
	ITaInfoEntrepriseServiceRemote daoInfos=null;
	
	private Object ecranAppelant = null;
	private Realm realm;
	private DataBindingContext dbc;
	private TypeDoc typeDoc = TypeDoc.getInstance();

	private PaSelectionLigneDocumentCochableControlleur paSelectionLigneDocumentCochableControlleur=null;
	private String typePaiementCourant=null;
	
	public PaCritereListeDocumentCochableController(PaCritereListeDocumentCochable vue,EntityManager em) {
//		if(em!=null) {
//			setEm(em);
//		}
		try {
			daoFacture = new EJBLookup<ITaFactureServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_FACTURE_SERVICE, ITaFactureServiceRemote.class.getName());
			setDaoStandard(daoFacture);
			setVue(vue);
	
			vue.getShell().addShellListener(this);
	
			//Branchement action annuler : empeche la fermeture automatique de la fenetre sur ESC
			vue.getShell().addTraverseListener(new Traverse());
	
			initController();
	//		initSashFormWeight();
			
			daoInfos = new EJBLookup<ITaInfoEntrepriseServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_INFO_ENTREPRISE_SERVICE, ITaInfoEntrepriseServiceRemote.class.getName());
			infos =daoInfos.findInstance();
	//		vue.getTfDateDebutPeriode().setSelection(infos.getDatedebInfoEntreprise());
	//		vue.getTfDateFinPeriode().setSelection(infos.getDatefinInfoEntreprise());
			LibDateTime.setDate(vue.getTfDateDebutPeriode(), infos.getDatedebInfoEntreprise());
			LibDateTime.setDate(vue.getTfDateFinPeriode(), infos.getDatefinInfoEntreprise());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public PaCritereListeDocumentCochableController(PaCritereListeDocumentCochable vue) {
		this(vue,null);
	}
	
	private void initController()	{
		try {	
//			setGrille(vue.getTableFacture());
//			initSashFormWeight();
			daoFacture = new EJBLookup<ITaFactureServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_FACTURE_SERVICE, ITaFactureServiceRemote.class.getName());
			daoAvoir = new EJBLookup<ITaAvoirServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_AVOIR_SERVICE, ITaAvoirServiceRemote.class.getName());
			daoAcompte = new EJBLookup<ITaAcompteServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_ACOMPTE_SERVICE, ITaAcompteServiceRemote.class.getName());
			daoReglement = new EJBLookup<ITaReglementServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_REGLEMENT_SERVICE, ITaReglementServiceRemote.class.getName());
			daoRemise = new EJBLookup<ITaRemiseServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_REMISE_SERVICE, ITaRemiseServiceRemote.class.getName());
			
			setDaoStandard(daoFacture);
			setDbcStandard(dbc);

			initVue();
			//boutton cahcés pour l'instant mais peut être à supprimer définitivement
			vue.getBtnPrecedent().setVisible(true);
			vue.getBtnFermer().setVisible(false);
			
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
			vue.getParent().setMenu(popupMenuFormulaire);
//			vue.getPaGrille().setMenu(popupMenuGrille);

			initEtatBouton(true);		

			

		} catch (Exception e) {
			vue.getLaMessage().setText(e.getMessage());
			logger.error("Erreur : PaCritereListeDocumentCochableController", e);
		}
	}
	
	protected void initVerifySaisie() {
		if (mapInfosVerifSaisie == null)
			mapInfosVerifSaisie = new HashMap<Control, InfosVerifSaisie>();
			
		initVerifyListener(mapInfosVerifSaisie, daoFacture);

	}

	private void initVue() {
		PaSelectionLigneDocumentCochable selectionLigneRelance = new PaSelectionLigneDocumentCochable(vue.getExpandBarGroup(), SWT.PUSH,1,
				 SWT.FULL_SELECTION
					| SWT.H_SCROLL
					| SWT.V_SCROLL
					| SWT.BORDER
					| SWT.CHECK);
		paSelectionLigneDocumentCochableControlleur = new PaSelectionLigneDocumentCochableControlleur(selectionLigneRelance/*,getEm()*/);
		ParamAfficheListeExporte paramAfficheRelanceFacture =new ParamAfficheListeExporte();
		paramAfficheRelanceFacture.setEnregistreDirect(true);
		paSelectionLigneDocumentCochableControlleur.configPanel(paramAfficheRelanceFacture);
		paSelectionLigneDocumentCochableControlleur.setImpressionUniquement(false);
		paSelectionLigneDocumentCochableControlleur.addDeclencheCommandeControllerListener(this);
		
		paSelectionLigneDocumentCochableControlleur.addDeclencheChangementEtatListener(this);
		
		addExpandBarItem(vue.getExpandBarGroup(), selectionLigneRelance,
		"Liste des documents", DocumentPlugin.getImageDescriptor(
		"/icons/logo_lgr_16.png").createImage(), SWT.DEFAULT, 400);
		vue.getExpandBarGroup().getItem(0).setExpanded(true);
	}

	@Override
	protected void actAide() throws Exception {
		// TODO Auto-generated method stub
		actAide(null);
	}

	@Override
	protected boolean aideDisponible() {
		return getFocusCourantSWT().equals(vue.getTfTiers());
	}
	

	@Override
	protected void actAide(String message) throws Exception {
		if (aideDisponible()) {
//			boolean affichageAideRemplie = DocumentPlugin.getDefault().getPreferenceStore().getBoolean(fr.legrain.document.preferences.PreferenceConstants.TYPE_AFFICHAGE_AIDE);
			boolean affichageAideRemplie = LgrMess.isAfficheAideRemplie();
			setActiveAide(true);
			boolean verrouLocal = VerrouInterface.isVerrouille();
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
				/** ************************************************************ */
				LgrPartListener.getLgrPartListener().setLgrActivePart(null);
				IEditorPart e = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new EditorInputAide(), EditorAide.ID);
				LgrPartListener.getLgrPartListener().setLgrActivePart(e);
				LgrPartListener.getLgrPartListener().getLgrNavigation().add(e);
				paAideController = new SWTPaAideControllerSWT(((EditorAide) e).getComposite());
				((EJBLgrEditorPart) e).setController(paAideController);
				((EJBLgrEditorPart) e).setPanel(((EditorAide) e).getComposite());
				/** ************************************************************ */
				EJBBaseControllerSWTStandard controllerEcranCreation = null;
				ParamAffiche parametreEcranCreation = null;
				IEditorPart editorCreation = null;
				String editorCreationId = null;
				IEditorInput editorInputCreation = null;
				Shell s2 = new Shell(s, LgrShellUtil.styleLgr);

					if (getFocusCourantSWT().equals(vue.getTfTiers())) {
						//permet de paramètrer l'affichage remplie ou non de l'aide
						PaTiersSWT paTiersSWT = new PaTiersSWT(s2, SWT.NULL);
						SWTPaTiersController swtPaTiersController = new SWTPaTiersController(paTiersSWT);
						paramAfficheAideRecherche.setForceAffichageAideRemplie(false);

						editorCreationId = TiersMultiPageEditor.ID_EDITOR;
						editorInputCreation = new EditorInputTiers();

						ParamAfficheTiers paramAfficheTiers = new ParamAfficheTiers();
						ITaTiersServiceRemote dao = new EJBLookup<ITaTiersServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_TIERS_SERVICE, ITaTiersServiceRemote.class.getName());
						paramAfficheAideRecherche.setJPQLQuery(dao.getTiersActif());
						paramAfficheTiers.setModeEcran(EnumModeObjet.C_MO_INSERTION);
						paramAfficheTiers.setEcranAppelant(paAideController);
						controllerEcranCreation = swtPaTiersController;
						parametreEcranCreation = paramAfficheTiers;

						paramAfficheAideRecherche.setTypeEntite(TaTiers.class);
						paramAfficheAideRecherche.setChampsRecherche(Const.C_CODE_TIERS);
						paramAfficheAideRecherche.setDebutRecherche(vue.getTfTiers().getText());
						paramAfficheAideRecherche.setControllerAppelant(this);
//						ModelTiers modelTiers = new ModelTiers(swtPaTiersController.getIbTaTable());
//						paramAfficheAideRecherche.setModel(modelTiers);
						ModelGeneralObjetEJB<TaTiers,TaTiersDTO> modelTiers = new ModelGeneralObjetEJB<TaTiers,TaTiersDTO>(dao);
						paramAfficheAideRecherche.setModel(modelTiers);
						paramAfficheAideRecherche.setTypeObjet(swtPaTiersController.getClassModel());
						paramAfficheAideRecherche.setChampsIdentifiant(Const.C_ID_TIERS);
					}
					
				if (paramAfficheAideRecherche.getJPQLQuery() != null) {

					PaAideRechercheSWT paAideRecherche1 = new PaAideRechercheSWT(s, SWT.NULL);
					SWTPaAideRechercheControllerSWT paAideRechercheController1 = new SWTPaAideRechercheControllerSWT(paAideRecherche1);

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
					paAideController.addRecherche(paAideRechercheController1,paramAfficheAideRecherche.getTitreRecherche());

					// Parametrage de l'ecran d'aide principal
					ParamAfficheAideSWT paramAfficheAideSWT = new ParamAfficheAideSWT();
					ParamAfficheAide paramAfficheAide = new ParamAfficheAide();

					// enregistrement pour le retour de l'ecran d'aide
					paAideController.addRetourEcranListener(this);
					Control focus = vue.getShell().getDisplay().getFocusControl();
					// affichage de l'ecran d'aide principal (+ ses recherches)


					/*****************************************************************/
					paAideController.configPanel(paramAfficheAide);
					/*****************************************************************/
					// je rends enable false tous les boutons avant de passer
					// dans l'écran d'aide
					// pour ne pas que les actions de l'écran des factures
					// interfèrent ceux de l'écran d'aide
					// activeWorkenchPartCommands(false);
				}

			} finally {
				VerrouInterface.setVerrouille(false);
				vue.setCursor(Display.getCurrent().getSystemCursor(SWT.CURSOR_ARROW));
			}
		}
	}


	

	@Override
	protected void actAnnuler() throws Exception {
		try{
			//passage ejb
//			if(typePaiementCourant.equals(typeDoc.TYPE_TOUS)){
//				((AbstractLgrDAO)daoFacture).getEntityManager().clear();
//				((AbstractLgrDAO)daoAvoir).getEntityManager().clear();
//				((AbstractLgrDAO)daoAcompte).getEntityManager().clear();
//				((AbstractLgrDAO)daoReglement).getEntityManager().clear();
//				((AbstractLgrDAO)daoRemise).getEntityManager().clear();
//			}
//
//			if(typePaiementCourant.equals(TaFacture.TYPE_DOC)){
//				((AbstractLgrDAO)daoFacture).getEntityManager().clear();
//			}
//			if(typePaiementCourant.equals(TaAvoir.TYPE_DOC)){
//				((AbstractLgrDAO)daoAvoir).getEntityManager().clear();
//			}
//			if(typePaiementCourant.equals(TaAcompte.TYPE_DOC)){
//				((AbstractLgrDAO)daoAcompte).getEntityManager().clear();
//			}
//			if(typePaiementCourant.equals(TaReglement.TYPE_DOC)){
//				((AbstractLgrDAO)daoReglement).getEntityManager().clear();
//			}
//			if(typePaiementCourant.equals(TaRemise.TYPE_DOC)){
//				((AbstractLgrDAO)daoRemise).getEntityManager().clear();
//			}
		} catch (Exception e1) {
			logger.error("Erreur : actionPerformed", e1);
		}
		
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		actImprimer();
		initEtatBouton(false);
	}

	@Override
	protected void actEnregistrer() throws Exception {
//		EntityTransaction transaction = ((AbstractLgrDAO)daoFacture).getEntityManager().getTransaction();
		try{
//			((AbstractLgrDAO)daoFacture).begin(transaction);
			
		for (Object document : modelDocument) {
			if(((IDocumentTiers)document).getTypeDocument().equals(TaFacture.TYPE_DOC))
				document=daoFacture.enregistrerMerge((TaFacture)document);
			if(((IDocumentTiers)document).getTypeDocument().equals(TaAvoir.TYPE_DOC))
				document=daoAvoir.enregistrerMerge((TaAvoir)document);
			if(((IDocumentTiers)document).getTypeDocument().equals(TaAcompte.TYPE_DOC))
				document=daoAcompte.enregistrerMerge((TaAcompte)document);
			if(((IDocumentTiers)document).getTypeDocument().equals(TaReglement.TYPE_DOC))
				document=daoReglement.enregistrerMerge((TaReglement)document);
			if(((IDocumentTiers)document).getTypeDocument().equals(TaRemise.TYPE_DOC))
				document=daoRemise.enregistrerMerge((TaRemise)document);
			
		}
									
//		((AbstractLgrDAO)daoFacture).commit(transaction);
//		transaction = null;
		initEtatBouton(false);
		} catch (ExceptLgr e1) {
			logger.error("Erreur : actionPerformed", e1);
		}
	}

	@Override
	protected void actFermer() throws Exception {
		// TODO Auto-generated method stub
		if(paSelectionLigneDocumentCochableControlleur.getVue().getBtnEnregister().getEnabled()){
			if(MessageDialog.openQuestion(vue.getShell(), "Enregistrer les documents", "" +
			"Voulez-vous enregistrer les modifications avant de fermer ?")){
				paSelectionLigneDocumentCochableControlleur.actEnregistrer();
			}
			else actAnnuler();
		}
		if(indiceAppelant!=null){
			ChangementDePageEvent change = new ChangementDePageEvent(this,ChangementDePageEvent.AUTRE,indiceAppelant);
			fireChangementDePage(change);
		}else{
			if (onClose()) {
				closeWorkbenchPart();
			}
		}
	}

	
	protected void actReinitialiser() throws Exception {
//
//		modelRemise.getListeEntity().clear();
//		modelRemise.getListeObjet().clear();
		actRefresh();
	}
	@Override
	protected void actImprimer() throws Exception {
		try{
			//passage EJB
//			String type="";
//		
//			TaRemiseDAO daoRemise = new TaRemiseDAO(getEm());
//			List<IDocumentTiers> listeTemp =new LinkedList<IDocumentTiers>();
//			modelDocument=new LinkedList<IDocumentTiers>();
//			type=vue.getCbListeDocument().getItem(vue.getCbListeDocument().getSelectionIndex());
//			if(type.equalsIgnoreCase(typeDoc.TYPE_TOUS)){
//				for (String item : vue.getCbListeDocument().getItems()) {
//					remonteDocument(item);
//				}
//			}else{
//				remonteDocument(type);
//			}
//			if(paSelectionLigneDocumentCochableControlleur!=null ){
////				if(modelDocument.size()>0){
//			if(paSelectionLigneDocumentCochableControlleur.getMasterListEntity()!=null)
//				paSelectionLigneDocumentCochableControlleur.getMasterListEntity().clear();
//			paSelectionLigneDocumentCochableControlleur.setMasterListEntity(modelDocument.subList(0, modelDocument.size()));
//			paSelectionLigneDocumentCochableControlleur.setMasterDAO(daoFacture);
//			paSelectionLigneDocumentCochableControlleur.setDocument(null);
//			paSelectionLigneDocumentCochableControlleur.actRefresh();
//				}
//		}
		}catch (Exception e) {
			logger.error("",e);
		}
		finally{
			vue.setCursor(Display.getCurrent().getSystemCursor(SWT.CURSOR_ARROW));
		}
	}

	

	@Override
	protected void actInserer() throws Exception {

	}

	@Override
	protected void actModifier() throws Exception {

	}

	@Override
	protected void actRefresh() throws Exception {

	}

	private void remonteDocument(String type){
		List<IDocumentTiers> listeTemp =new LinkedList<IDocumentTiers>();
		Boolean export =null;
		if(vue.getCbNonExporte().getSelection())export=false;
		else
		if(vue.getCbExporte().getSelection())export=true;
		//si export=null cela represente le tous
		if(type.equals(TaFacture.TYPE_DOC)){
				listeTemp =  ((IDocumentDAO)daoFacture).rechercheDocument(LibDateTime.getDate(vue.getTfDateDebutPeriode()),
						LibDateTime.getDate(vue.getTfDateFinPeriode()),vue.getTfTiers().getText(),export);
		}
		if(type.equals(TaAvoir.TYPE_DOC)){
			listeTemp = ((IDocumentDAO) daoAvoir).rechercheDocument(LibDateTime.getDate(vue.getTfDateDebutPeriode()),
					LibDateTime.getDate(vue.getTfDateFinPeriode()),vue.getTfTiers().getText(),export);
		}
		if(type.equals(TaAcompte.TYPE_DOC)){
			listeTemp = ((IDocumentDAO) daoAcompte).rechercheDocument(LibDateTime.getDate(vue.getTfDateDebutPeriode()),
					LibDateTime.getDate(vue.getTfDateFinPeriode()),vue.getTfTiers().getText(),export);
		}
		if(type.equals(TaReglement.TYPE_DOC)){
			listeTemp = ((IDocumentDAO) daoReglement).rechercheDocument(LibDateTime.getDate(vue.getTfDateDebutPeriode()),
					LibDateTime.getDate(vue.getTfDateFinPeriode()),vue.getTfTiers().getText(),export);
		}
		if(type.equals(TaRemise.TYPE_DOC)){
			listeTemp = ((IDocumentDAO) daoRemise).rechercheDocument(LibDateTime.getDate(vue.getTfDateDebutPeriode()),
					LibDateTime.getDate(vue.getTfDateFinPeriode()),vue.getTfTiers().getText(),export);
		}
		for (IDocumentTiers doc : listeTemp) {
			if(doc.getTypeDocument().equals(TaAcompte.TYPE_DOC) ){
				if(daoRemise.findSiAcompteDansRemise(doc.getCodeDocument()).size()==0)
					modelDocument.add(doc);
			}
			else if(doc.getTypeDocument().equals(TaReglement.TYPE_DOC) ){
				if(daoRemise.findSiReglementDansRemise(doc.getCodeDocument()).size()==0)
					modelDocument.add(doc);
			}
			else modelDocument.add(doc);
		}
	}
	public IStatus validateUI() throws Exception {
		return null;
	}

	public IStatus validateUIField(String nomChamp,Object value) {
		return null;
	}

	@Override
	protected void actSupprimer() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
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

		mapActions.put(vue.getBtnPrecedent(), C_COMMAND_GLOBAL_FERMER_ID);
		mapActions.put(vue.getBtnValiderParam(), C_COMMAND_GLOBAL_IMPRIMER_ID);


		Object[] tabActionsAutres = new Object[] { C_COMMAND_GLOBAL_AIDE_ID, C_COMMAND_GLOBAL_REFRESH_ID };
		mapActions.put(null, tabActionsAutres);
	}


	@Override
	protected void initComposantsVue() throws ExceptLgr {
		// TODO Auto-generated method stub

	}

	@Override
	public void initEtatComposant() {
		// TODO Auto-generated method stub
		
	}

	public void bind(){
		try {
			realm = SWTObservables.getRealm(vue.getParent().getDisplay());
			dbc = new DataBindingContext(realm);
			setDbcStandard(dbc);
			initEtatBouton(true);
			
		} catch(Exception e) {
			logger.error("",e);
			vue.getLaMessage().setText(e.getMessage());
		}
	}

	@Override
	protected void initMapComposantChamps() {
		if (mapComposantChamps == null) 
			mapComposantChamps = new LinkedHashMap<Control,String>();

		mapComposantChamps.put(vue.getTfDateDebutPeriode(), Const.C_DATE_DEBUT);
		mapComposantChamps.put(vue.getTfDateFinPeriode(), Const.C_DATE_FIN);

		
		if (listeComposantFocusable == null) 
			listeComposantFocusable = new ArrayList<Control>();

		listeComposantFocusable.add(vue.getTfDateDebutPeriode());
		listeComposantFocusable.add(vue.getTfDateFinPeriode());
		listeComposantFocusable.add(vue.getCbListeDocument());
		listeComposantFocusable.add(vue.getTfTiers());
		listeComposantFocusable.add(vue.getBtnValiderParam());
		listeComposantFocusable.add(vue.getBtnPrecedent());
		
		if(mapInitFocus == null) 
			mapInitFocus = new LinkedHashMap<EnumModeObjet,Control>();
		mapInitFocus.put(EnumModeObjet.C_MO_INSERTION,vue.getTfDateDebutPeriode());
		mapInitFocus.put(EnumModeObjet.C_MO_EDITION,vue.getTfDateDebutPeriode());
		mapInitFocus.put(EnumModeObjet.C_MO_CONSULTATION,vue.getTfDateDebutPeriode());

		activeModifytListener();

		vue.getCbListeDocument().addModifyListener(new LgrModifyListener());


		vue.getCbListeDocument().add(typeDoc.TYPE_TOUS);
		for (String libelle : typeDoc.getTypeDocCompletPresent().values()) {
			if(libelle.equals(TaFacture.TYPE_DOC) || 
					libelle.equals(TaAvoir.TYPE_DOC)  ||
					libelle.equals(TaAcompte.TYPE_DOC)  ||
					libelle.equals(TaReglement.TYPE_DOC)  ||
					libelle.equals(TaRemise.TYPE_DOC) ){
				vue.getCbListeDocument().add(libelle);
			}
			
		}
		vue.getCbListeDocument().select(0);
		vue.getCbTous().setSelection(true);
		typePaiementCourant=vue.getCbListeDocument().getItem(vue.getCbListeDocument().getSelectionIndex());
		vue.getCbListeDocument().setVisibleItemCount(vue.getCbListeDocument().getItemCount());
		vue.getCbListeDocument().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					if(!typePaiementCourant.equals(vue.getCbListeDocument().getItem(vue.getCbListeDocument().getSelectionIndex()))){	
						if(paSelectionLigneDocumentCochableControlleur.getVue().getBtnEnregister().getEnabled()){
							if(MessageDialog.openQuestion(vue.getShell(), "Annuler les modifications", "Voulez-vous annuler les modifications en cours")){						
								actAnnuler();
								typePaiementCourant=vue.getCbListeDocument().getItem(vue.getCbListeDocument().getSelectionIndex());
								actRefresh();
							}else{
								vue.getCbListeDocument().select(vue.getCbListeDocument().indexOf(typePaiementCourant));
							}
						}else{
							typePaiementCourant=vue.getCbListeDocument().getItem(vue.getCbListeDocument().getSelectionIndex());
							actRefresh();
						}
					}
					
//					actRefresh();
				} catch (Exception e1) {
					logger.error("", e1);
				}
			}
		});

		

	}
	private void initTiers() {

	}

	@Override
	protected void initMapComposantDecoratedField() {
		if (mapComposantDecoratedField == null) 
			mapComposantDecoratedField = new LinkedHashMap<Control,ControlDecoration>();
		mapComposantDecoratedField.put(vue.getTfDateDebutPeriode(), vue.getFieldDateDebutPeriode());
		mapComposantDecoratedField.put(vue.getTfDateFinPeriode(), vue.getFieldDateFinPeriode());
	}
	protected void initImageBouton() {
		super.initImageBouton();
		vue.getBtnValiderParam().setImage(LibrairiesEcranPlugin.ir.get(LibrairiesEcranPlugin.IMAGE_ACCEPTER));
		vue.getBtnPrecedent().setImage(LibrairiesEcranPlugin.ir.get(LibrairiesEcranPlugin.IMAGE_FERMER));
		vue.layout(true);
	}

	@Override
	public boolean onClose() throws ExceptLgr {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public ResultAffiche configPanel(ParamAffiche param) {
		if (param!=null){
			Map<String,String[]> map = daoFacture.getParamWhereSQL();
			if (param.getFocusDefautSWT()!=null && !param.getFocusDefautSWT().isDisposed())
				param.getFocusDefautSWT().setFocus();
			else param.setFocusDefautSWT(vue.getTfDateDebutPeriode());
			}

			if(param instanceof ParamAfficheListeExporte){
				if(((ParamAfficheListeExporte)param).getDateDeb()!=null){
					setDateDeb(((ParamAfficheListeExporte)param).getDateDeb());
					//vue.getTfDateDebutPeriode().setSelection(getDateDeb());
					LibDateTime.setDate(vue.getTfDateDebutPeriode(), getDateDeb());
				}else{
					//vue.getTfDateDebutPeriode().setSelection(infos.getDatedebRelInfoEntreprise());
					LibDateTime.setDate(vue.getTfDateDebutPeriode(), infos.getDatedebRelInfoEntreprise());
				}
				if(((ParamAfficheListeExporte)param).getDateFin()!=null){
					setDateFin(((ParamAfficheListeExporte)param).getDateFin());
//					vue.getTfDateFinPeriode().setSelection(getDateFin());
					LibDateTime.setDate(vue.getTfDateFinPeriode(), getDateFin());
				}else{
//					vue.getTfDateFinPeriode().setSelection(infos.getDatefinInfoEntreprise());
					LibDateTime.setDate(vue.getTfDateFinPeriode(), infos.getDatefinInfoEntreprise());
				}
			}
			bind();

			VerrouInterface.setVerrouille(false);
			initEtatBouton(true);
		
		return null;
	}

	@Override
	public Composite getVue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void sortieChamps() {
		// TODO Auto-generated method stub

	}

	public Date getDateDeb() {
		return dateDeb;
	}

	public void setDateDeb(Date dateDeb) {
		this.dateDeb = dateDeb;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}



	public void setVue(PaCritereListeDocumentCochable vue) {
		super.setVue(vue);
		this.vue = vue;
	}


	public PaSelectionLigneDocumentCochableControlleur getPaSelectionLigneRemiseControlleur() {
		return paSelectionLigneDocumentCochableControlleur;
	}

	public void setPaSelectionLigneRemiseControlleur(
			PaSelectionLigneDocumentCochableControlleur paSelectionLigneRelanceControlleur) {
		this.paSelectionLigneDocumentCochableControlleur = paSelectionLigneRelanceControlleur;
	}

	@Override
	protected void initEtatBouton() {
		initEtatBouton(false);
	}
	
	@Override
	protected void initEtatBouton(boolean initFocus) {
		boolean trouve = true;
		switch (modeEcran.getMode()) {
		case C_MO_INSERTION:
			enableActionAndHandler(C_COMMAND_GLOBAL_INSERER_ID,false);
			enableActionAndHandler(C_COMMAND_GLOBAL_MODIFIER_ID,false);
			enableActionAndHandler(C_COMMAND_GLOBAL_ENREGISTRER_ID,false);
			enableActionAndHandler(C_COMMAND_GLOBAL_ANNULER_ID,false);
			enableActionAndHandler(C_COMMAND_GLOBAL_IMPRIMER_ID,false);
			enableActionAndHandler(C_COMMAND_GLOBAL_FERMER_ID,false);
			enableActionAndHandler(C_COMMAND_GLOBAL_SUPPRIMER_ID,false);
			enableActionAndHandler(C_COMMAND_GLOBAL_AIDE_ID,true);
			enableActionAndHandler(C_COMMAND_GLOBAL_REFRESH_ID,true);
			break;
		case C_MO_EDITION:
			enableActionAndHandler(C_COMMAND_GLOBAL_INSERER_ID,false);
			enableActionAndHandler(C_COMMAND_GLOBAL_MODIFIER_ID,false);
			enableActionAndHandler(C_COMMAND_GLOBAL_ENREGISTRER_ID,true);
			enableActionAndHandler(C_COMMAND_GLOBAL_ANNULER_ID,false);
			enableActionAndHandler(C_COMMAND_GLOBAL_IMPRIMER_ID,false);
			enableActionAndHandler(C_COMMAND_GLOBAL_FERMER_ID,true);
			enableActionAndHandler(C_COMMAND_GLOBAL_SUPPRIMER_ID,false);
			enableActionAndHandler(C_COMMAND_GLOBAL_AIDE_ID,true);
			enableActionAndHandler(C_COMMAND_GLOBAL_REFRESH_ID,true);
			break;
		case C_MO_CONSULTATION:
			enableActionAndHandler(C_COMMAND_GLOBAL_INSERER_ID,false);
			enableActionAndHandler(C_COMMAND_GLOBAL_MODIFIER_ID,false);
			enableActionAndHandler(C_COMMAND_GLOBAL_ENREGISTRER_ID,false);
			enableActionAndHandler(C_COMMAND_GLOBAL_ANNULER_ID,false);
			enableActionAndHandler(C_COMMAND_GLOBAL_IMPRIMER_ID,true);
			enableActionAndHandler(C_COMMAND_GLOBAL_FERMER_ID,true);
			enableActionAndHandler(C_COMMAND_GLOBAL_SUPPRIMER_ID,false);
			enableActionAndHandler(C_COMMAND_GLOBAL_AIDE_ID,true);
			enableActionAndHandler(C_COMMAND_GLOBAL_REFRESH_ID,true);
			break;
		default:
			break;
		}
		vue.getCbExporte().setEnabled(vue.getBtnValiderParam().getEnabled());
		vue.getCbNonExporte().setEnabled(vue.getBtnValiderParam().getEnabled());
		vue.getCbTous().setEnabled(vue.getBtnValiderParam().getEnabled());
		vue.getCbListeDocument().setEnabled(vue.getBtnValiderParam().getEnabled());
		vue.getTfDateDebutPeriode().setEnabled(vue.getBtnValiderParam().getEnabled());
		vue.getTfDateFinPeriode().setEnabled(vue.getBtnValiderParam().getEnabled());
		vue.getTfTiers().setEnabled(vue.getBtnValiderParam().getEnabled());
		initEtatComposant();
		if (initFocus)
			initFocusSWT(modeEcran, mapInitFocus);	
		
	}
	
	
	public void retourEcran(final RetourEcranEvent evt) {
		if (evt.getRetour() != null
				&& (evt.getSource() instanceof SWTPaAideControllerSWT)) {
			if (getFocusAvantAideSWT() instanceof Text) {
				try {
					((Text) getFocusAvantAideSWT()).setText(((ResultAffiche) evt
							.getRetour()).getResult());		

					ctrlUnChampsSWT(getFocusAvantAideSWT());
				} catch (Exception e) {
					logger.error("",e);
					if(getFocusAvantAideSWT()!=null)setFocusCourantSWT(getFocusAvantAideSWT());
					vue.getLaMessage().setText(e.getMessage());
				}
			}			
		} else if (evt.getRetour() != null){

		}
		super.retourEcran(evt);
	}

	private class LgrModifyListener implements ModifyListener{

		public void modifyText(ModifyEvent e) {
			modif(e);
		}
	}

	private void modif(TypedEvent e) {
		try {
			if(e.getSource().equals(vue.getCbListeDocument())) {
				int typeDoc = vue.getCbListeDocument().getSelectionIndex();
				//réinitialisation des références si changement de type de document
				if(vue.getCbListeDocument().getItem(typeDoc).equals(TypeDoc.TYPE_TOUS)) {
					daoFacture = new EJBLookup<ITaFactureServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_FACTURE_SERVICE, ITaFactureServiceRemote.class.getName());
					daoAvoir = new EJBLookup<ITaAvoirServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_AVOIR_SERVICE, ITaAvoirServiceRemote.class.getName());
					daoAcompte = new EJBLookup<ITaAcompteServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_ACOMPTE_SERVICE, ITaAcompteServiceRemote.class.getName());
					daoReglement = new EJBLookup<ITaReglementServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_REGLEMENT_SERVICE, ITaReglementServiceRemote.class.getName());
					daoRemise = new EJBLookup<ITaRemiseServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_REMISE_SERVICE, ITaRemiseServiceRemote.class.getName());
				} else if(vue.getCbListeDocument().getItem(typeDoc).equals(TypeDoc.TYPE_FACTURE)) {
					daoFacture = new EJBLookup<ITaFactureServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_FACTURE_SERVICE, ITaFactureServiceRemote.class.getName());
				} else if(vue.getCbListeDocument().getItem(typeDoc).equals(TypeDoc.TYPE_AVOIR)) {
					daoAvoir = new EJBLookup<ITaAvoirServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_AVOIR_SERVICE, ITaAvoirServiceRemote.class.getName());
				} else if(vue.getCbListeDocument().getItem(typeDoc).equals(TypeDoc.TYPE_ACOMPTE)) {
					daoAcompte = new EJBLookup<ITaAcompteServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_ACOMPTE_SERVICE, ITaAcompteServiceRemote.class.getName());
				}else if(vue.getCbListeDocument().getItem(typeDoc).equals(TypeDoc.TYPE_REGLEMENT)) {
					daoReglement = new EJBLookup<ITaReglementServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_REGLEMENT_SERVICE, ITaReglementServiceRemote.class.getName());
				}else if(vue.getCbListeDocument().getItem(typeDoc).equals(TypeDoc.TYPE_REMISE)) {
					daoRemise = new EJBLookup<ITaRemiseServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_REMISE_SERVICE, ITaRemiseServiceRemote.class.getName());
				}
				setDaoStandard(daoFacture);
;

			}
		} catch (Exception e1) {
			logger.error(e1);
		}		
	}

	public int getIndiceAppelant() {
		return indiceAppelant;
	}

	public void setIndiceAppelant(int indiceAppelant) {
		this.indiceAppelant = indiceAppelant;
	}

	@Override
	public void declencheChangementEtat(DeclencheChangementEtatEvent evt) {
//		if(evt.getEtat().equals(EnumModeObjet.C_MO_EDITION)){
			initEtatBouton(false);
//		}
	}


	public void addDeclencheChangementEtatListener(IDeclencheChangementEtatListener l) {
		listenerList.add(IDeclencheChangementEtatListener.class, l);
	}
	
	public void removeDeclencheChangementEtatListener(IDeclencheChangementEtatListener l) {
		listenerList.remove(IDeclencheChangementEtatListener.class, l);
	}
	
	protected void fireDeclencheChangementEtat(DeclencheChangementEtatEvent e) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == IDeclencheChangementEtatListener.class) {
				if (e == null)
					e = new DeclencheChangementEtatEvent(this);
				( (IDeclencheChangementEtatListener) listeners[i + 1]).declencheChangementEtat(e);
			}
		}
	}
}
