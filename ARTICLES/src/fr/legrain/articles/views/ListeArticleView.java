package fr.legrain.articles.views;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.model.TaArticle;
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
import fr.legrain.tiers.views.ListeTiersView;
import fr.legrain.tiers.views.ListeTiersViewController;
import fr.legrain.tiers.views.PaListeTiersView;

public class ListeArticleView extends ViewPart implements ILgrListView<TaArticle>, RetourEcranListener {
	
	static Logger logger = Logger.getLogger(ListeArticleView.class.getName());

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "fr.legrain.articles.views.ListeArticleView";

	private LgrCompositeTableViewer viewer;

	/**
	 * The constructor.
	 */
	public ListeArticleView() {
	}
	
	private PaListeArticleView vue = null;
	private ListeArticleViewController controller = null;

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		vue = new PaListeArticleView(parent, SWT.NONE);
		controller = new ListeArticleViewController(vue, this);
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
	
	public void update(TaArticle taTiers) {

		getController().getModelArticle().addToModel(viewer.getViewer(), taTiers);
	}
	
	public void refresh(TaArticle taTiers) {

		getController().getModelArticle().refreshModel(viewer.getViewer(), taTiers);
	}
	
	public void remove(TaArticle taTiers) {

		getController().getModelArticle().removeFromModel(viewer.getViewer(), taTiers);
	}

	@Override
	public void select(TaArticle t) {
		if(t!=null) {
			TaArticleDTO tiers = getController().getModelArticle().recherche(Const.C_ID_DTO_GENERAL, t.getIdArticle());
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
				TaArticleDTO tiersRetour =getController().getModelArticle().recherche(Const.C_ID_DTO_GENERAL, LibConversion.stringToInteger(((ResultAffiche) evt.getRetour()).getIdResult())); 
				if(tiersRetour!=null){
					viewer.getViewer().setSelection(new StructuredSelection(tiersRetour),true);
				}
			}
		}
	}

	public ListeArticleViewController getController() {
		return controller;
	}

	public PaListeArticleView getVue() {
		return vue;
	}

}
