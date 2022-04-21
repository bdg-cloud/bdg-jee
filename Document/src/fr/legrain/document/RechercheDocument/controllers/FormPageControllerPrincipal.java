package fr.legrain.document.RechercheDocument.controllers;

import java.util.Date;

import javax.naming.NamingException;
import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import fr.legrain.articles.dao.TaArticleDAO;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.document.RechercheDocument.ecrans.PaFormPage;
import fr.legrain.lib.ejb.EJBLookup;
import fr.legrain.lib.gui.ParamAffiche;
import fr.legrain.lib.gui.ResultAffiche;
import fr.legrain.tiers.clientutility.JNDILookupClass;


public class FormPageControllerPrincipal  {

	static Logger logger = Logger.getLogger(FormPageControllerPrincipal.class.getName());

	// On récupère uniquement l'entity manager de TaArticleDAO
	//protected EntityManager masterDAO = new TaArticleDAO().getEntityManager();
	protected ITaArticleServiceRemote masterDAO;
	
	private String typeDocEnCours="";
	// Vue
	protected PaFormPage vue = null;

	// Controllers
	protected ParamControllerMini paramControllerMini = null;
	protected TotauxControllerPrincipal totauxControllerPrincipal = null;
	protected DocEcheanceController docEcheanceController = null;

	protected Date datedeb;
	protected Date datefin;

	public void refreshAll() {
		initialisationModel(true);
	}
	
	public void init(){
		initialisationModel(false);
	}

	private void initialisationModel() {
		initialisationModel(true);
	}
	
	public FormPageControllerPrincipal() {
		super();
	}


	public FormPageControllerPrincipal(PaFormPage vue) {
		initVue(vue);
	}
	
	public void initVue(PaFormPage vue) {
		this.vue = vue;
		try {
			masterDAO = new EJBLookup<ITaArticleServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_ARTICLE_SERVICE, ITaArticleServiceRemote.class.getName(),false);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		paramControllerMini = new ParamControllerMini(this,vue, null);
		totauxControllerPrincipal = new TotauxControllerPrincipal(this,vue,null);
		docEcheanceController = new DocEcheanceController(this,vue,null);
	}

	public void raz(boolean tout) {
		try {
			docEcheanceController.modelLDocument.clear();
			docEcheanceController.modelDocument.clear();
			docEcheanceController.bind();			
		} catch(Exception e) {
			logger.error("", e);
		} finally {
			vue.reflow();
		}
	}
	
	private void initialisationModel(boolean tout) {
		try {
			// if(masterDAO!=null && masterEntity!=null) {

			//if(tout) {
			// Initialisation des paramètres
			paramControllerMini.initialiseModelIHM();
			//}
			if(tout){
				docEcheanceController.initialiseModelIHM(0); 
			}
			docEcheanceController.bind();

			totauxControllerPrincipal.initialiseModelIHM();
			totauxControllerPrincipal.bind();


			//  @busy : Mise en attente du Form (icone chargement) pendant le traitement des données de factures
			//vue.getForm().setBusy(true);

			//			Thread t = new Thread(){
			////			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			//				public void run() {
			//					// Initialisation des factures
			//					facturesController.initialiseModelIHM();
			//					facturesController.bind(); // @busy : La fin du chargement se fait dans le bind
			//				}
			////			});
			//			};
			//			t.start();

		} catch(Exception e) {
			logger.error("", e);
		} finally {
			//vue.reflow();
		}
	}


	public ITaArticleServiceRemote getMasterDAOEM() {
		return masterDAO;
	}


	public void setMasterDAOEM(ITaArticleServiceRemote masterDAO) {
		this.masterDAO = masterDAO;
	}

	public DocEcheanceController getDocEcheanceController() {
		return docEcheanceController;
	}

	public ParamControllerMini getParamControllerMini() {
		return paramControllerMini;
	}

	public TotauxControllerPrincipal getTotauxControllerPrincipal() {
		return totauxControllerPrincipal;
	}
	
	public ResultAffiche configPanel(ParamAffiche param){
//		Date dateDeb = new Date();
//		Date dateFin = new Date();
		if (param!=null){
			if(param.getCodeDocument()!=null){
				setTypeDocEnCours(param.getCodeDocument());
				getParamControllerMini().setTypeDocEnCours(param.getCodeDocument());
				if(vue.getCompositeSectionParam()!=null){
					getParamControllerMini().initListe();
				}
			}
			
		}
	
//		logger.info("temp config panel "+new Date(dateFin.getTime()-dateDeb.getTime()));
		return null;
	}

	public String getTypeDocEnCours() {
		return typeDocEnCours;
	}

	public void setTypeDocEnCours(String typeDocEnCours) {
		this.typeDocEnCours = typeDocEnCours;
	}

}
