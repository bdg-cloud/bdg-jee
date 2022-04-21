package fr.legrain.sauvegarde.handlers;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.ui.PlatformUI;

import fr.legrain.gestCom.librairiesEcran.swt.LgrAbstractHandler;


public class HandlerSauvegarde extends LgrAbstractHandler {

	static Logger logger = Logger.getLogger(HandlerSauvegarde.class.getName());


	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub
	}

	public void dispose() {
		// TODO Auto-generated method stub
	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
//		this.window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		FonctionSauvegarde fonctionSauvegarde = new FonctionSauvegarde();
		fonctionSauvegarde.setShell(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		fonctionSauvegarde.sauvegarde();		
		return null;
	}

	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isHandled() {
		// TODO Auto-generated method stub
		return true;
	}

	public void removeHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub
	}
}
