package fr.legrain.facture.actions;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

import fr.legrain.bdg.documents.service.remote.ITaBonlivServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaDevisServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaFacture;
import fr.legrain.gestCom.Appli.EntityManagerUtil;
import fr.legrain.lib.ejb.EJBLookup;
import fr.legrain.tiers.clientutility.JNDILookupClass;

public class MajDocument implements IWorkbenchWindowActionDelegate {

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub

	}

	public void run(IAction action) {
		try {
			if(MessageDialog.openConfirm(PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell(),
						"ATTENTION", "Voulez-vous vraiment mettre à jour les totaux des documents ?"
						+"\r\n"+"Cette procédure peut prendre du temps.")){
			//gestion des bonliv
			ITaBonlivServiceRemote daoBonliv = new EJBLookup<ITaBonlivServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_BONLIV_SERVICE, ITaBonlivServiceRemote.class.getName());
//			daoBonliv.begin(daoBonliv.getEntityManager().getTransaction());
			List<TaBonliv> lBonliv = daoBonliv.selectAll();
			for (TaBonliv taDocument : lBonliv) {
				//passage ejb
//				taDocument.calculeTvaEtTotaux();
				daoBonliv.merge(taDocument);
			}
//			daoBonliv.commit(daoBonliv.getEntityManager().getTransaction());
				
			//gestion des devis
			ITaDevisServiceRemote daoDevis = new EJBLookup<ITaDevisServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_DEVIS_SERVICE, ITaDevisServiceRemote.class.getName());
//			daoDevis.begin(daoDevis.getEntityManager().getTransaction());
			List<TaDevis> lDevis = daoDevis.selectAll();
//			List<TaDevis> lDevis = daoDevis.rechercheDocument(new Date("01/01/1900"),new Date("01/01/2009"));
			for (TaDevis taDocument : lDevis) {
				taDocument.calculeTvaEtTotaux();
				daoDevis.merge(taDocument);
			}
//			daoDevis.commit(daoDevis.getEntityManager().getTransaction());
			
			//gestion des factures
			ITaFactureServiceRemote daoFacture = new EJBLookup<ITaFactureServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_FACTURE_SERVICE, ITaFactureServiceRemote.class.getName());
//			daoFacture.begin(daoFacture.getEntityManager().getTransaction());
			List<TaFacture> lFacture = daoFacture.selectAll();
//			List<TaFacture> lFacture = daoFacture.rechercheDocument(new Date("01/01/1900"),new Date("01/01/2009"));
			for (TaFacture taDocument : lFacture) {
				taDocument.calculeTvaEtTotaux();
				daoFacture.merge(taDocument);
			}
//			daoFacture.commit(daoFacture.getEntityManager().getTransaction());
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

}
