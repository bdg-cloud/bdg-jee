package fr.legrain.articles.views;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;

import fr.legrain.gestCom.librairiesEcran.swt.LgrAbstractHandler;

public class HandlerListeArticleViewRefresh extends LgrAbstractHandler {
	
	static Logger logger = Logger.getLogger(HandlerListeArticleViewRefresh.class.getName());

	public Object execute(ExecutionEvent event) throws ExecutionException{
		try {
			System.out.println("ListeArticleViewController.HandlerListeArticleViewRefresh.execute()");
			
			IViewReference[] vr = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences();
			ISelection selection = null;
			for (int i = 0; i < vr.length; i++) {
				if(vr[i].getId().endsWith(ListeArticleView.ID)) {			
					((ListeArticleView)vr[i].getView(false)).getVue().getLgrViewer().getViewer().refresh();
				}
				break;
			}
			
		} catch (Exception e) {
			logger.error("",e);
		}
		return event;
	}
} 
