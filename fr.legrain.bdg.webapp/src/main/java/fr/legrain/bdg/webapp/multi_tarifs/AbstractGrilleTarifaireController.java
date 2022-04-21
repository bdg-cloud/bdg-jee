package fr.legrain.bdg.webapp.multi_tarifs;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.RowEditEvent;

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.TaFamilleDTO;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaFamilleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaPrixServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.tiers.service.remote.ITaTTarifServiceRemote;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;


@Named
@ViewScoped  
public class AbstractGrilleTarifaireController extends AbstractController{

	@Inject @Named(value="tabListModelCenterBean")
	protected TabListModelBean tabsCenter;
	
	@Inject @Named(value="tabViewsBean")
	protected TabViewsBean tabViews;
	protected boolean cellEdit = false;

	protected List<TaLMultiTarifDTOJSF> valuesLignes=new LinkedList<>();
	protected List<TaLMultiTarifDTOJSF> filteredValuesLignes=new LinkedList<>();
	protected TaLMultiTarifDTOJSF nouvelleLigne ;
	protected TaLMultiTarifDTOJSF selectionLigne  ;
	protected TaLMultiTarifDTOJSF oldSelectionLigne  ;
	protected TaLMultiTarifDTOJSF[] selectionLignes ;
	
	protected boolean saisieHT=true;
	protected boolean saisieDetail=true;
	protected String modeSaisie=Const.C_SAISIE_DETAIL;
	protected String modeSaisiePrix=Const.C_SAISIE_HT;
	
	protected String titreSaisieDetail="Saisie en détail";
	protected String titreSaisieRapide="Saisie rapide";
	protected Date dateDeb=null;
	protected Date dateFin=null;	

	protected TaInfoEntreprise infos =null;
	protected String modeEcranDefaut;
	protected static final String C_DIALOG = "dialog";
	


	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
	protected Boolean afficheCritere=false;
	
	protected String titreSaisieHt="Saisie des prix en HT";
	protected String titreSaisieTtc="Saisie des prix en TTC";
	protected String titreSaisieCoef="Saisie des prix par coefficient";

	

	

	protected @EJB  ITaInfoEntrepriseServiceRemote TaInfoEntrepriseService;
	protected @EJB ITaPreferencesServiceRemote taPreferencesService;
	protected @EJB ITaArticleServiceRemote taArticleService;
	protected @EJB ITaTTarifServiceRemote taTTarifService;
	protected @EJB ITaFamilleServiceRemote taFamilleService;
	protected @EJB ITaPrixServiceRemote taPrixService;

	
	@PostConstruct
	public void postConstruct(){
		try {

			infos=TaInfoEntrepriseService.findInstance();

			dateDeb=infos.getDatedebInfoEntreprise();
			dateFin=infos.getDatefinInfoEntreprise();
			initEcran();
			


		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public AbstractGrilleTarifaireController() {  
	}  



	
	public List<TaFamilleDTO> familleAutoComplete(String query) {
		List<TaFamilleDTO> allValues = taFamilleService.selectAllDTO();
		List<TaFamilleDTO> filteredValues = new ArrayList<TaFamilleDTO>();
		TaFamilleDTO cp = new TaFamilleDTO();
		cp.setCodeFamille(Const.C_AUCUN);
//		filteredValues.add(cp);
		for (int i = 0; i < allValues.size(); i++) {
			cp = allValues.get(i);
			if(query.equals("*") || cp.getCodeFamille().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(cp);
			}
		}

		return filteredValues;
	}
	
	public List<TaArticleDTO> articleAutoComplete(String query) {
		List<TaArticleDTO> allValues = taArticleService.findAllLight();
		List<TaArticleDTO> filteredValues = new ArrayList<TaArticleDTO>();
		TaArticleDTO cp = new TaArticleDTO();
		cp.setCodeArticle(Const.C_AUCUN);
//		filteredValues.add(cp);
		for (int i = 0; i < allValues.size(); i++) {
			cp = allValues.get(i);
			if(query.equals("*") || cp.getCodeArticle().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(cp);
			}
		}

		return filteredValues;
	}
	
	

	

	
	public boolean editable() {
		return !modeEcran.dataSetEnModif();
	}
	
	public void actInitInserer(ActionEvent actionEvent){
		
	}
	
	public void actModifier(ActionEvent actionEvent){
		try {
			if(modeEcran.getMode().equals(EnumModeObjet.C_MO_CONSULTATION)){
				modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	




	public boolean isSaisieDetail() {
		return saisieDetail;
	}


	public void setSaisieDetail(boolean saisieDetail) {
		this.saisieDetail = saisieDetail;
	}


	public String getTitreSaisieDetail() {
		return titreSaisieDetail;
	}


	public String getTitreSaisieRapide() {
		return titreSaisieRapide;
	}


	public String getModeSaisie() {
		return modeSaisie;
	}


	public void setModeSaisie(String modeSaisie) {
		this.modeSaisie = modeSaisie;
	}


	public void initEcran() {
		saisieDetail=modeSaisie.equals(Const.C_SAISIE_DETAIL);
		saisieHT=modeSaisiePrix.equals(Const.C_SAISIE_HT);
	}


	public String getModeSaisiePrix() {
		return modeSaisiePrix;
	}


	public void setModeSaisiePrix(String modeSaisiePrix) {
		this.modeSaisiePrix = modeSaisiePrix;
	}

	public void onRowEditInit(RowEditEvent event) {
		AjaxBehaviorEvent evt = (AjaxBehaviorEvent)event;
		DataTable table = (DataTable) evt.getSource();
		int activeRow = table.getRowIndex()+1;
		if(event.getObject()!=null && !event.getObject().equals(selectionLigne))
			selectionLigne=(TaLMultiTarifDTOJSF) event.getObject();
		actModifier(null);
	}


	public TaLMultiTarifDTOJSF getNouvelleLigne() {
		return nouvelleLigne;
	}


	public void setNouvelleLigne(TaLMultiTarifDTOJSF nouvelleLigne) {
		this.nouvelleLigne = nouvelleLigne;
	}


	public TaLMultiTarifDTOJSF getSelectionLigne() {
		return selectionLigne;
	}


	public void setSelectionLigne(TaLMultiTarifDTOJSF selectionLigne) {
		this.selectionLigne = selectionLigne;
	}


	public TaLMultiTarifDTOJSF[] getSelectionLignes() {
		return selectionLignes;
	}
	



	public void setSelectionLignes(TaLMultiTarifDTOJSF[] selectionLignes) {
		this.selectionLignes = selectionLignes;
	}


	public TaLMultiTarifDTOJSF getOldSelectionLigne() {
		return oldSelectionLigne;
	}


	public void setOldSelectionLigne(TaLMultiTarifDTOJSF oldSelectionLigne) {
		this.oldSelectionLigne = oldSelectionLigne;
	}


	public List<TaLMultiTarifDTOJSF> getValuesLignes() {
		return valuesLignes;
	}


	public void setValuesLignes(List<TaLMultiTarifDTOJSF> valuesLignes) {
		this.valuesLignes = valuesLignes;
	}


	public List<TaLMultiTarifDTOJSF> getFilteredValuesLignes() {
		return filteredValuesLignes;
	}


	public void setFilteredValuesLignes(List<TaLMultiTarifDTOJSF> filteredValuesLignes) {
		this.filteredValuesLignes = filteredValuesLignes;
	}


	public String getTitreSaisieCoef() {
		return titreSaisieCoef;
	}


	public void setTitreSaisieCoef(String titreSaisieCoef) {
		this.titreSaisieCoef = titreSaisieCoef;
	}

	public String getTitreSaisieHt() {
		return titreSaisieHt;
	}


	public void setTitreSaisieHt(String titreSaisieHt) {
		this.titreSaisieHt = titreSaisieHt;
	}


	public String getTitreSaisieTtc() {
		return titreSaisieTtc;
	}


	public void setTitreSaisieTtc(String titreSaisieTtc) {
		this.titreSaisieTtc = titreSaisieTtc;
	}
}
