package fr.legrain.document.controller;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

import fr.legrain.articles.ecran.MessagesEcran;
import fr.legrain.bdg.documents.service.remote.ITaAcompteServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaApporteurServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAvoirServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBoncdeServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBonlivServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaDevisServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPrelevementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaProformaServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaRDocumentServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaReglementServiceRemote;
import fr.legrain.document.divers.ModelDocument;
import fr.legrain.document.divers.ModelRecupDocumentCreationDoc;
import fr.legrain.document.divers.ModelDocumentLiaisonDoc;
import fr.legrain.document.divers.TypeDoc;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaPrelevement;
import fr.legrain.document.model.TaProforma;
import fr.legrain.document.model.TaRDocument;
import fr.legrain.gestCom.Module_Document.IHMAideFacture;
import fr.legrain.gestCom.Module_Document.IHMEnteteDocument;
import fr.legrain.gestCom.Module_Document.IHMEnteteFacture;
import fr.legrain.gestCom.librairiesEcran.swt.DeclencheCommandeControllerEvent;
import fr.legrain.gestCom.librairiesEcran.swt.EJBBaseControllerSWTStandard;
import fr.legrain.gestCom.librairiesEcran.swt.LgrDatabindingUtilEJB;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.lib.data.ModeObjet;
import fr.legrain.lib.ejb.EJBLookup;
import fr.legrain.lib.gui.DefaultFrameGrilleSansBouton;
import fr.legrain.lib.gui.ParamAffiche;
import fr.legrain.lib.gui.ResultAffiche;
import fr.legrain.lib.gui.RetourEcranListener;
import fr.legrain.lib.gui.grille.LgrTableViewer;
import fr.legrain.tiers.clientutility.JNDILookupClass;


public class PaVisuLiaisonDocumentControlleur extends EJBBaseControllerSWTStandard implements RetourEcranListener {
	static Logger logger = Logger.getLogger(PaVisuLiaisonDocumentControlleur.class
			.getName());
	
	private DefaultFrameGrilleSansBouton vue;
	//private TaReglementDAO daoReglement = null;
	private ModelDocumentLiaisonDoc modelDocument;
	private IDocumentTiers masterEntity = null;
	private ITaRDocumentServiceRemote masterDAO = null;
	private LgrDatabindingUtilEJB lgrDatabindingUtil = new LgrDatabindingUtilEJB();
	private Realm realm;
	private DataBindingContext dbc;
	private LgrTableViewer tableViewer;
	private IObservableValue selectedDocument;
	
	private ITaFactureServiceRemote daoFacture = null;
	private ITaAvoirServiceRemote daoAvoir = null;
	private ITaBonlivServiceRemote daoBonLiv = null;
	private ITaBoncdeServiceRemote daoBoncde = null;
	private ITaApporteurServiceRemote daoApporteur = null;
	private ITaProformaServiceRemote daoProforma = null;
	private ITaDevisServiceRemote daoDevis = null;
	private ITaPrelevementServiceRemote daoPrelevement = null;
	private ITaAcompteServiceRemote daoAcompte = null;
	
	public PaVisuLiaisonDocumentControlleur(DefaultFrameGrilleSansBouton ecran,EntityManager em) {
//		if (em != null) {
//			setEm(em);
//		}
		try {
			setVue(ecran);
			//daoFacture = new TaReglementDAO(getEm());
			
			daoFacture = new EJBLookup<ITaFactureServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_FACTURE_SERVICE, ITaFactureServiceRemote.class.getName());
			daoAvoir = new EJBLookup<ITaAvoirServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_AVOIR_SERVICE, ITaAvoirServiceRemote.class.getName());
			daoBonLiv = new EJBLookup<ITaBonlivServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_BONLIV_SERVICE, ITaBonlivServiceRemote.class.getName());
			daoBoncde = new EJBLookup<ITaBoncdeServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_BONCDE_SERVICE, ITaBoncdeServiceRemote.class.getName());
			daoApporteur = new EJBLookup<ITaApporteurServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_APPORTEUR_SERVICE, ITaApporteurServiceRemote.class.getName());
			daoProforma = new EJBLookup<ITaProformaServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_PROFORMA_SERVICE, ITaProformaServiceRemote.class.getName());
			daoDevis = new EJBLookup<ITaDevisServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_DEVIS_SERVICE, ITaDevisServiceRemote.class.getName());
			daoPrelevement = new EJBLookup<ITaPrelevementServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_PRELEVEMENT_SERVICE, ITaPrelevementServiceRemote.class.getName());
		    daoAcompte = new EJBLookup<ITaAcompteServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_ACOMPTE_SERVICE, ITaAcompteServiceRemote.class.getName());

			this.vue.getShell().addShellListener(this);
			this.vue.getShell().addTraverseListener(new Traverse());

			initController();

		} catch (Exception e) {
			logger.debug("", e);
		}
	}
	private void initController() {
		try {
			setGrille(vue.getGrille());
			masterDAO = new EJBLookup<ITaRDocumentServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_R_DOCUMENT_SERVICE, ITaRDocumentServiceRemote.class.getName());
			setDaoStandard(daoFacture);
			setTableViewerStandard(tableViewer);
			setDbcStandard(dbc);

//			initVue();

			initMapComposantChamps();
			initMapComposantDecoratedField();

			listeComponentFocusableSWT(listeComposantFocusable);
			initDeplacementSaisie(listeComposantFocusable);
			initFocusOrder();
			initActions();

			branchementBouton();
			Menu popupMenuFormulaire = new Menu(vue.getShell(), SWT.POP_UP);
			Menu popupMenuGrille = new Menu(vue.getShell(), SWT.POP_UP);
			Menu[] tabPopups = new Menu[] { popupMenuFormulaire, popupMenuGrille };
			this.initPopupAndButtons(mapActions, tabPopups);
			vue.getPaGrille().setMenu(popupMenuGrille);
			vue.getGrille().setMenu(popupMenuGrille);
			bind();

		} catch (Exception e) {
			if (e.getMessage() != null)
				vue.getLaMessage().setText(e.getMessage());
			logger.error("Erreur : PaArticlesController", e);
		}
	}

	public void bind() {
		//passage EJB
//		try {
//			modelDocument = new ModelDocumentLiaisonDoc(daoFacture,IHMEnteteFacture.class,"");
//			realm = SWTObservables.getRealm(vue.getParent().getDisplay());
//			vue.getLaTitreGrille().setText("Liste des documents liés au document selectionné");
//			
//			selectedDocument = lgrDatabindingUtil.bindTable(vue.getGrille(),modelDocument.remplirListe(masterEntity,getEm()),IHMEnteteFacture.class,
//					new String[] {"Code document","Date","Code tiers","Net TTC"},
//					new String[] {"100","100","150","150"},
//					new String[] {"codeDocument","dateDocument","codeTiers","netTtcCalc"}
//																											   
//			,1);
//			
//			
//			vue.getGrille().addMouseListener(
//				new MouseAdapter() {
//					public void mouseDoubleClick(MouseEvent e) {
//						String type=((IHMEnteteDocument) selectedDocument.getValue()).getTypeDocument();
//						String idEditor =TypeDoc.getInstance().getEditorDoc().get(type);
//						String valeurIdentifiant = ((IHMEnteteDocument) selectedDocument.getValue()).getCodeDocument();
//						ouvreDocument(valeurIdentifiant, idEditor);
//					}
//
//				});
//		} 
//		catch (Exception e) {
//			if (e.getMessage() != null)
//				vue.getLaMessage().setText(e.getMessage());
//			logger.error("", e);
//		}
	}

	@Override
	protected void actAide() throws Exception {
		// TODO Auto-generated method stub
		actAide(null);
	}

	@Override
	protected void actAide(String message) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void actAnnuler() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void actEnregistrer() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void actFermer() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void actImprimer() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void actInserer() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void actModifier() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void actRefresh() throws Exception {
		actRefresh(masterEntity);
	}

	public void actRefresh(IDocumentTiers taDocumentMaitre) throws Exception {
		//passage EJB
//		setMasterEntity(taDocumentMaitre);
//		masterDAO = new EJBLookup<ITaRDocumentServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_R_DOCUMENT_SERVICE, ITaRDocumentServiceRemote.class.getName());
//		if (lgrDatabindingUtil.getMapLgrTableViewer().get(vue.getGrille())==null)
//			bind();
//		WritableList writableListReglement = new WritableList(modelDocument.
//				remplirListe(taDocumentMaitre,getEm()), IHMAideFacture.class);
//		setTableViewerStandard(lgrDatabindingUtil.getMapLgrTableViewer().get(vue.getGrille()));
//		getTableViewerStandard().setInput(writableListReglement);
//		if(selectedDocument==null ||selectedDocument.getValue()==null)
//			getTableViewerStandard().selectionGrille(0);
//		initEtatBouton();
		
	}
	@Override
	protected void actSupprimer() throws Exception {
		EntityTransaction transaction = null;
		TaRDocument rDoc = null;
		try {	// TODO Auto-generated method stub
			if(selectedDocument!=null && selectedDocument.getValue()!=null){
				//			transaction = masterDAO.getEntityManager().getTransaction();
				//			masterDAO.begin(transaction);
				boolean accepte=MessageDialog.openConfirm(vue.getShell(), "Suppression","Etes-vous sur de vouloir supprimer cette liaison ?");
				if(accepte){
					DeclencheCommandeControllerEvent e = new DeclencheCommandeControllerEvent(
							this, C_COMMAND_GLOBAL_SUPPRIMER_ID);
					fireDeclencheCommandeController(e);
					//
//					daoFacture.setEm(getEm());
//					transaction = daoFacture.getEntityManager().getTransaction();
//					daoFacture.begin(transaction);
					if(masterEntity.getTypeDocument()==TaFacture.TYPE_DOC){
//						daoFacture.setEm(getEm());
						rDoc=supprimeRDocument();						
						rDoc.getTaFacture().getTaRDocuments().remove(rDoc);						
						masterDAO.supprimer(rDoc);
						rDoc.setTaFacture(null);						
						masterEntity=daoFacture.enregistrerMerge((TaFacture)masterEntity);
					}
					if(masterEntity.getTypeDocument()==TaAcompte.TYPE_DOC){
//						daoAcompte.setEm(getEm());
						rDoc=supprimeRDocument();
						rDoc.getTaAcompte().getTaRDocuments().remove(rDoc);						
						masterDAO.supprimer(rDoc);
						rDoc.setTaAcompte(null);
						masterEntity=daoAcompte.enregistrerMerge((TaAcompte)masterEntity);
					}
					if(masterEntity.getTypeDocument()==TaAvoir.TYPE_DOC){
//						daoAvoir.setEm(getEm());
						rDoc=supprimeRDocument();
						rDoc.getTaAvoir().getTaRDocuments().remove(rDoc);						
						masterDAO.supprimer(rDoc);
						rDoc.setTaAvoir(null);
						masterEntity=daoAvoir.enregistrerMerge((TaAvoir)masterEntity);
					}
					if(masterEntity.getTypeDocument()==TaApporteur.TYPE_DOC){
//						daoApporteur.setEm(getEm());
						rDoc=supprimeRDocument();
						rDoc.getTaApporteur().getTaRDocuments().remove(rDoc);						
						masterDAO.supprimer(rDoc);
						rDoc.setTaApporteur(null);
						masterEntity=daoApporteur.enregistrerMerge((TaApporteur)masterEntity);
					}
					if(masterEntity.getTypeDocument()==TaBoncde.TYPE_DOC){
//						daoBoncde.setEm(getEm());
						rDoc=supprimeRDocument();
						rDoc.getTaBoncde().getTaRDocuments().remove(rDoc);						
						masterDAO.supprimer(rDoc);
						rDoc.setTaBoncde(null);
						masterEntity=daoBoncde.enregistrerMerge((TaBoncde)masterEntity);
					}
					if(masterEntity.getTypeDocument()==TaBonliv.TYPE_DOC){
//						daoBonLiv.setEm(getEm());
						rDoc=supprimeRDocument();
						rDoc.getTaBonliv().getTaRDocuments().remove(rDoc);						
						masterDAO.supprimer(rDoc);
						rDoc.setTaBonliv(null);
						masterEntity=daoBonLiv.enregistrerMerge((TaBonliv)masterEntity);
					}
					if(masterEntity.getTypeDocument()==TaDevis.TYPE_DOC){
//						daoDevis.setEm(getEm());
						rDoc=supprimeRDocument();
						rDoc.getTaDevis().getTaRDocuments().remove(rDoc);						
						masterDAO.supprimer(rDoc);
						rDoc.setTaDevis(null);						
						masterEntity=daoDevis.enregistrerMerge((TaDevis)masterEntity);
					}
					if(masterEntity.getTypeDocument()==TaPrelevement.TYPE_DOC){
//						daoPrelevement.setEm(getEm());
						rDoc=supprimeRDocument();
						rDoc.getTaPrelevement().getTaRDocuments().remove(rDoc);						
						masterDAO.supprimer(rDoc);
						rDoc.setTaPrelevement(null);
						masterEntity=daoPrelevement.enregistrerMerge((TaPrelevement)masterEntity);
					}
					if(masterEntity.getTypeDocument()==TaProforma.TYPE_DOC){
//						daoProforma.setEm(getEm());
						rDoc=supprimeRDocument();
						rDoc.getTaProforma().getTaRDocuments().remove(rDoc);						
						masterDAO.supprimer(rDoc);
						rDoc.setTaProforma(null);
						masterEntity=daoProforma.enregistrerMerge((TaProforma)masterEntity);
					}
					rDoc=null;
//					daoFacture.commit(transaction);
					DeclencheCommandeControllerEvent e2 = new DeclencheCommandeControllerEvent(
							this, C_COMMAND_GLOBAL_REFRESH_ID);
					fireDeclencheCommandeController(e2);
					actRefresh();
//					transaction = null;
				}
			}
		} catch (Exception e) {
			logger.error("",e);

			if(transaction!=null && transaction.isActive()) {
				transaction.rollback();
			}
		} finally {
			initEtatBouton();
		}
	}
	
    private TaRDocument supprimeRDocument(){
    	TaRDocument rDoc=null;
    	LinkedList<TaRDocument> listeTemp = new LinkedList<TaRDocument>( masterEntity.getTaRDocuments()) ;
		if(((IHMEnteteDocument)selectedDocument.getValue()).getTypeDocument()==TaFacture.TYPE_DOC){
			//LinkedList<TaRDocument> listeTemp = new LinkedList<TaRDocument>( masterEntity.getTaRDocuments()) ;
			for (TaRDocument Rdocument : listeTemp) {
				if(Rdocument.getTaFacture()!=null&&
						Rdocument.getTaFacture().getIdDocument()==
					((IHMEnteteDocument)selectedDocument.getValue()).getIdDocument()){
					rDoc=Rdocument;
					Rdocument.getTaFacture().getTaRDocuments().remove(Rdocument);
					Rdocument.setTaFacture(null);
					return Rdocument;
				}
			}
		}

		if(((IHMEnteteDocument)selectedDocument.getValue()).getTypeDocument()==TaAcompte.TYPE_DOC){
			for (TaRDocument Rdocument : listeTemp) {
				if(Rdocument.getTaAcompte()!=null&&
						Rdocument.getTaAcompte().getIdDocument()==
					((IHMEnteteDocument)selectedDocument.getValue()).getIdDocument()){
					rDoc=Rdocument;
					Rdocument.getTaAcompte().getTaRDocuments().remove(Rdocument);
					Rdocument.setTaAcompte(null);
					return rDoc;
				}
			}
		}
		if(((IHMEnteteDocument)selectedDocument.getValue()).getTypeDocument()==TaAvoir.TYPE_DOC){
			for (TaRDocument Rdocument : listeTemp) {
				if(Rdocument.getTaPrelevement()!=null&&
						Rdocument.getTaAvoir().getIdDocument()==
					((IHMEnteteDocument)selectedDocument.getValue()).getIdDocument()){
					rDoc=Rdocument;
					Rdocument.getTaAvoir().getTaRDocuments().remove(Rdocument);
				Rdocument.setTaAvoir(null);
				return rDoc;
				}
			}
		}
		if(((IHMEnteteDocument)selectedDocument.getValue()).getTypeDocument()==TaApporteur.TYPE_DOC){
			for (TaRDocument Rdocument : listeTemp) {
				if(Rdocument.getTaPrelevement()!=null&&
						Rdocument.getTaApporteur().getIdDocument()==
					((IHMEnteteDocument)selectedDocument.getValue()).getIdDocument()){
					rDoc=Rdocument;
					Rdocument.getTaApporteur().getTaRDocuments().remove(Rdocument);
					Rdocument.setTaApporteur(null);
					return rDoc;
				}
			}
		}
		if(((IHMEnteteDocument)selectedDocument.getValue()).getTypeDocument()==TaDevis.TYPE_DOC){
			for (TaRDocument Rdocument : listeTemp) {
				if(Rdocument.getTaDevis()!=null&&
						Rdocument.getTaDevis().getIdDocument()==
					((IHMEnteteDocument)selectedDocument.getValue()).getIdDocument()){
					rDoc=Rdocument;
					Rdocument.getTaDevis().getTaRDocuments().remove(Rdocument);
					Rdocument.setTaDevis(null);
					return rDoc;
				}
			}
		}
		if(((IHMEnteteDocument)selectedDocument.getValue()).getTypeDocument()==TaBoncde.TYPE_DOC){
			for (TaRDocument Rdocument : listeTemp) {
				if(Rdocument.getTaBoncde()!=null&&
						Rdocument.getTaBoncde().getIdDocument()==
					((IHMEnteteDocument)selectedDocument.getValue()).getIdDocument()){
					rDoc=Rdocument;
					Rdocument.getTaBoncde().getTaRDocuments().remove(Rdocument);
					Rdocument.setTaBoncde(null);
					return rDoc;
				}
			}
		}
		if(((IHMEnteteDocument)selectedDocument.getValue()).getTypeDocument()==TaBonliv.TYPE_DOC){
			for (TaRDocument Rdocument : listeTemp) {
				if(Rdocument.getTaBonliv()!=null&&
						Rdocument.getTaBonliv().getIdDocument()==
					((IHMEnteteDocument)selectedDocument.getValue()).getIdDocument()){
					rDoc=Rdocument;
					Rdocument.getTaBonliv().getTaRDocuments().remove(Rdocument);
					Rdocument.setTaBonliv(null);
					return rDoc;
				}
			}
		}
		if(((IHMEnteteDocument)selectedDocument.getValue()).getTypeDocument()==TaPrelevement.TYPE_DOC){
			for (TaRDocument Rdocument : listeTemp) {
				if(Rdocument.getTaPrelevement()!=null&&
						Rdocument.getTaPrelevement().getIdDocument()==
					((IHMEnteteDocument)selectedDocument.getValue()).getIdDocument()){
					rDoc=Rdocument;
					Rdocument.getTaPrelevement().getTaRDocuments().remove(Rdocument);
					Rdocument.setTaPrelevement(null);
					return rDoc;
				}
			}
		}
		if(((IHMEnteteDocument)selectedDocument.getValue()).getTypeDocument()==TaProforma.TYPE_DOC){
			for (TaRDocument Rdocument : listeTemp) {
				if(Rdocument.getTaProforma()!=null&&
						Rdocument.getTaProforma().getIdDocument()==
					((IHMEnteteDocument)selectedDocument.getValue()).getIdDocument()){
					rDoc=Rdocument;
					Rdocument.getTaProforma().getTaRDocuments().remove(Rdocument);
					Rdocument.setTaProforma(null);
					return rDoc;
				}
			}
		}
		return rDoc;
    }
    
	protected void initActions() {
		mapCommand = new HashMap<String, IHandler>();

//		mapCommand.put(C_COMMAND_GLOBAL_MODIFIER_ID, handlerModifier);
//		mapCommand.put(C_COMMAND_GLOBAL_ANNULER_ID, handlerAnnuler);
//		mapCommand.put(C_COMMAND_GLOBAL_ENREGISTRER_ID, handlerEnregistrer);
//		mapCommand.put(C_COMMAND_GLOBAL_INSERER_ID, handlerInserer);

		mapCommand.put(C_COMMAND_GLOBAL_SUPPRIMER_ID, handlerSupprimer);
//		mapCommand.put(C_COMMAND_GLOBAL_REFRESH_ID, handlerRefresh);
//		mapCommand.put(C_COMMAND_GLOBAL_AIDE_ID, handlerAide);
//		mapCommand.put(C_COMMAND_GLOBAL_FERMER_ID, handlerFermer);
//		mapCommand.put(C_COMMAND_GLOBAL_IMPRIMER_ID, handlerImprimer);

//		mapCommand.put(C_COMMAND_GLOBAL_PRECEDENT_ID, handlerPrecedent);
//		mapCommand.put(C_COMMAND_GLOBAL_SUIVANT_ID, handlerSuivant);

//		mapCommand.put(C_COMMAND_GLOBAL_SELECTION_ID, handlerSelection);
//		mapCommand.put(C_COMMAND_DOCUMENT_AFFICHER_TOUS_ID, handlerAfficherTous);
		

		initFocusCommand(String.valueOf(this.hashCode()),listeComposantFocusable,mapCommand);

		if (mapActions == null)
			mapActions = new LinkedHashMap<Button, Object>();

//		C_COMMAND_GLOBAL_REFRESH_ID,
		Object[] tabActionsAutres = new Object[] {C_COMMAND_GLOBAL_SUPPRIMER_ID };
		mapActions.put(null, tabActionsAutres);

	}
	protected void initEtatBouton() {
		boolean trouve = contientDesEnregistrement(modelDocument.getListeObjet());
		
		switch (getModeEcran().getMode()) {
		case C_MO_INSERTION:
			enableActionAndHandler(C_COMMAND_GLOBAL_SUPPRIMER_ID,false);
//			enableActionAndHandler(C_COMMAND_GLOBAL_REFRESH_ID,true);
			break;
		case C_MO_EDITION:
			enableActionAndHandler(C_COMMAND_GLOBAL_SUPPRIMER_ID,false);
//			enableActionAndHandler(C_COMMAND_GLOBAL_REFRESH_ID,true);
			break;
		case C_MO_CONSULTATION:
			enableActionAndHandler(C_COMMAND_GLOBAL_SUPPRIMER_ID,true);
//			enableActionAndHandler(C_COMMAND_GLOBAL_REFRESH_ID,true);
			break;
		default:
			break;
		}
		initEtatBoutonCommand();
	}	

	@Override
	protected void initComposantsVue() throws ExceptLgr {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initEtatComposant() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initMapComposantChamps() {
		// TODO Auto-generated method stub
		if (mapComposantChamps == null)
			mapComposantChamps = new LinkedHashMap<Control, String>();

		if (listeComposantFocusable == null)
			listeComposantFocusable = new ArrayList<Control>();
		listeComposantFocusable.add(vue.getGrille());
		if (mapInitFocus == null)
			mapInitFocus = new LinkedHashMap<EnumModeObjet,Control>();
		mapInitFocus.put(EnumModeObjet.C_MO_INSERTION, vue
				.getGrille());
		mapInitFocus.put(EnumModeObjet.C_MO_EDITION, vue
				.getGrille());
		mapInitFocus.put(EnumModeObjet.C_MO_CONSULTATION, vue
				.getGrille());		
	}

	@Override
	protected void initMapComposantDecoratedField() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onClose() throws ExceptLgr {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ResultAffiche configPanel(ParamAffiche param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DefaultFrameGrilleSansBouton getVue() {
		// TODO Auto-generated method stub
		return vue;
	}
	public void setVue(DefaultFrameGrilleSansBouton vue) {
		super.setVue(vue);
		this.vue = vue;
	}

	@Override
	protected void sortieChamps() {
		// TODO Auto-generated method stub
		
	}
	public IDocumentTiers getMasterEntity() {
		return masterEntity;
	}
	public void setMasterEntity(IDocumentTiers masterEntity) {
		this.masterEntity = masterEntity;
	}
//	public TaRDocumentDAO getMasterDAO() {
//		return masterDAO;
//	}
//	public void setMasterDAO(TaRDocumentDAO masterDAO) {
//		this.masterDAO = masterDAO;
//	}
	public ModelDocumentLiaisonDoc getModelDocument() {
		return modelDocument;
	}
	public void setModelDocument(ModelDocumentLiaisonDoc modelDocument) {
		this.modelDocument = modelDocument;
	}


}
