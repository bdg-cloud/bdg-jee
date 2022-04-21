package fr.legrain.tiers.views;


import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import fr.legrain.gestCom.Appli.Const;
import fr.legrain.gestCom.librairiesEcran.swt.ILgrListView;
import fr.legrain.gestCom.librairiesEcran.swt.SWTPaAideControllerSWT;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.gui.ResultAffiche;
import fr.legrain.lib.gui.RetourEcranEvent;
import fr.legrain.lib.gui.RetourEcranListener;
import fr.legrain.lib.gui.grille.LgrCompositeTableViewer;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaTiers;
//import fr.legrain.tiers.dao.TaTiers;

public class ListeTiersView extends ViewPart implements ILgrListView<TaTiers>, RetourEcranListener {
	
	static Logger logger = Logger.getLogger(ListeTiersView.class.getName());

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "fr.legrain.tiers.views.ListeTiersView";

	private LgrCompositeTableViewer viewer;

	/**
	 * The constructor.
	 */
	public ListeTiersView() {
	}
	
	private PaListeTiersView vue = null;
	private ListeTiersViewController controller = null;

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		vue = new PaListeTiersView(parent, SWT.NONE);
		controller = new ListeTiersViewController(vue, this);
		viewer = vue.getLgrViewer();

//		getSite().setSelectionProvider(viewer.getViewer());
		
//		IEditorPart editor = AbstractLgrMultiPageEditor.chercheEditeurDocumentOuvert(TiersMultiPageEditor.ID_EDITOR);
//		if(editor!=null) {
//			editor.getSite().setSelectionProvider(getSite().getSelectionProvider());
//		}
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		if(vue!=null)
			vue.getLgrViewer().getViewer().getControl().setFocus();
	}
	
	public void update(TaTiers taTiers) {

		getController().getModelTiers().addToModel(viewer.getViewer(), taTiers);
	}
	
	public void refresh(TaTiers taTiers) {

		getController().getModelTiers().refreshModel(viewer.getViewer(), taTiers);
	}
	
	public void remove(TaTiers taTiers) {

		getController().getModelTiers().removeFromModel(viewer.getViewer(), taTiers);
	}

	@Override
	public void select(TaTiers t) {
		if(t!=null) {
			TaTiersDTO tiers = getController().getModelTiers().recherche(Const.C_ID_TIERS, t.getIdTiers());
			viewer.getViewer().setSelection(new StructuredSelection(tiers));
		}
	}

	@Override
	public void select(int index) {
		viewer.selectionGrille(index);
	}

	@Override
	public void retourEcran(RetourEcranEvent evt) {
		if (evt.getRetour() != null
				&& (evt.getSource() instanceof SWTPaAideControllerSWT)) {
			if(((ResultAffiche) evt.getRetour()).getSelection()!=null){
				TaTiersDTO tiersRetour =getController().getModelTiers().recherche(Const.C_ID_TIERS, LibConversion.stringToInteger(((ResultAffiche) evt.getRetour()).getIdResult())); 
				if(tiersRetour!=null){
					viewer.getViewer().setSelection(new StructuredSelection(tiersRetour),true);
				}
			}
		}
	}

	public ListeTiersViewController getController() {
		return controller;
	}

	public PaListeTiersView getVue() {
		return vue;
	}
	
}