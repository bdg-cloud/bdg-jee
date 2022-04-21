package fr.legrain.articles.views;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import fr.legrain.articles.ecran.SWTPaArticlesController;
import fr.legrain.gestCom.Appli.Const;
import fr.legrain.gestCom.librairiesEcran.swt.LgrAbstractHandler;
import fr.legrain.gestCom.librairiesEcran.swt.ParamAfficheVisualisation;
import fr.legrain.gestCom.librairiesEcran.workbench.EJBLgrEditorPart;
import fr.legrain.gestCom.librairiesEcran.workbench.LgrPartListener;
import fr.legrain.tiers.ecran.SWTPaTiersController;

public class HandlerListeArticleViewSelection extends LgrAbstractHandler {
	
	static Logger logger = Logger.getLogger(HandlerListeArticleViewSelection.class.getName());

	public Object execute(ExecutionEvent event) throws ExecutionException{
		try{
			IEditorPart e = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
			.getActivePage().openEditor(new fr.legrain.visualisation.editor.EditorInputSelectionVisualisation(), 
					fr.legrain.visualisation.editor.EditorSelectionVisualisation.ID);
			LgrPartListener.getLgrPartListener().getLgrNavigation().add(e);

			ParamAfficheVisualisation paramAfficheSelectionVisualisation = new ParamAfficheVisualisation();

			paramAfficheSelectionVisualisation.setNomClassController(SWTPaArticlesController.class.getSimpleName());
			paramAfficheSelectionVisualisation.setNomRequete(Const.C_NOM_VU_ARTICLE);
			paramAfficheSelectionVisualisation.setModule("articles");

			((EJBLgrEditorPart)e).setPanel(((EJBLgrEditorPart)e).getControllerSelection().getVue());
			((EJBLgrEditorPart)e).getControllerSelection().configPanel(paramAfficheSelectionVisualisation);

		}catch (Exception e) {
			logger.error("",e);
		}	
		return event;
	}
} 
