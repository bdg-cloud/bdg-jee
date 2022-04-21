package fr.legrain.articles.views;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;

import fr.legrain.articles.editor.ArticleMultiPageEditor;
import fr.legrain.gestCom.librairiesEcran.swt.LgrAbstractHandler;
import fr.legrain.gestCom.librairiesEcran.workbench.EJBAbstractLgrMultiPageEditor;
import fr.legrain.lib.gui.ParamAffiche;
import fr.legrain.tiers.editor.TiersMultiPageEditor;

public class HandlerListeArticleViewVoir extends LgrAbstractHandler {
	
	static Logger logger = Logger.getLogger(HandlerListeArticleViewVoir.class.getName());

	public Object execute(ExecutionEvent event) throws ExecutionException{
		try {
			
			IViewReference[] vr = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences();
			ISelection selection = null;
			for (int i = 0; i < vr.length; i++) {
				if(vr[i].getId().endsWith(ListeArticleView.ID)) {					
					selection = ((ListeArticleView)vr[i].getView(false)).getController().getView().getViewSite().getSelectionProvider().getSelection();
					ParamAffiche param = new ParamAffiche();
					param.setSelection(selection);
					EJBAbstractLgrMultiPageEditor.ouvreFiche(null,null, ArticleMultiPageEditor.ID_EDITOR,param,false);
				}
				break;
			}
			
		} catch (Exception e) {
			logger.error("",e);
		}
		return event;
	}
} 
