package fr.legrain.moncompte.admin.webapp.admin;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

import fr.legrain.bdg.moncompte.service.remote.ITaClientServiceRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.moncompte.admin.webapp.app.TabListModelBean;
import fr.legrain.moncompte.admin.webapp.app.TabViewsBean;
import fr.legrain.moncompte.dto.TaClientDTO;
import fr.legrain.moncompte.model.TaClient;
import fr.legrain.moncompte.model.mapping.LgrDozerMapper;


@ManagedBean
@ViewScoped  
public class DemandePartenariatController implements Serializable {  
	
	@ManagedProperty(value="#{tabListModelCenterBean}")
	private TabListModelBean tabsCenter;
	
	@ManagedProperty(value="#{tabViewsBean}")
	private TabViewsBean tabViews;
	
//	private List<TaClientDTO> values; 
	private List<TaClient> values; 
	
	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
	
	private @EJB ITaUtilisateurServiceRemote taUtilisateurService;
	private @EJB ITaClientServiceRemote taClientService;
	
	private TaClientDTO[] selectedTaClientDTOs; 
    private TaClientDTO newTaClientDTO = new TaClientDTO();
    private TaClientDTO selectedTaClientDTO = new TaClientDTO();
    private TaClient selectedTaClient = new TaClient();
    private TaClient taClient = new TaClient();
    
	private String urlPourEdition;
	
    private LgrDozerMapper<TaClientDTO,TaClient> mapperUIToModel  = new LgrDozerMapper<TaClientDTO,TaClient>();
	private LgrDozerMapper<TaClient,TaClientDTO> mapperModelToUI  = new LgrDozerMapper<TaClient,TaClientDTO>();
	
	public DemandePartenariatController() {  
		//values = getValues();
	}  
	
	@PostConstruct
	public void postConstruct(){
		refresh();
				
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		String path = request.getRequestURI().substring(request.getContextPath().length());
		urlPourEdition = request.getRequestURL().substring(0,request.getRequestURL().length()-path.length());
	}

	public void refresh(){
		values = taClientService.listeDemandePartenariat();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}
	

	public void actSupprimerDemande() {
		actSupprimerDemande(null);
	}

	public void actSupprimerDemande(ActionEvent actionEvent) {
		TaClient obj = new TaClient();
		try {
			if(selectedTaClient!=null && selectedTaClient.getId()!=null){
				obj = taClientService.findById(selectedTaClient.getId());
			}

			obj.setTaPartenaire(null);
			taClientService.merge(obj);
			values.remove(selectedTaClient);

			//envoyer email client
			//envoyer email legrain
			
		} catch (FinderException e) {
			e.printStackTrace();
		}	    
	}
	
	public void actAccepteDemande() {
		actAccepteDemande(null);
	}

	public void actAccepteDemande(ActionEvent actionEvent) {
		TaClient obj = new TaClient();
		try {
			if(selectedTaClient!=null && selectedTaClient.getId()!=null){
				obj = taClientService.findById(selectedTaClient.getId());
			}

			obj.getTaPartenaire().setCodePartenaire(taClientService.genereCodePartenaire(obj));
			obj.getTaPartenaire().setActif(true);
			obj.getTaPartenaire().setDateDebut(new Date());
			
			taClientService.merge(obj);
			values.remove(selectedTaClient);
			
			//envoyer email client
			//envoyer email legrain


		} catch (FinderException e) {
			e.printStackTrace();
		}	    
	}

	public List<TaClient> getValues() {
		return values;
	}

	public void setValues(List<TaClient> values) {
		this.values = values;
	}

	public TaClient getSelectedTaClient() {
		return selectedTaClient;
	}

	public void setSelectedTaClient(TaClient selectedTaClient) {
		this.selectedTaClient = selectedTaClient;
	}

	public TabListModelBean getTabsCenter() {
		return tabsCenter;
	}

	public void setTabsCenter(TabListModelBean tabsCenter) {
		this.tabsCenter = tabsCenter;
	}

	public TabViewsBean getTabViews() {
		return tabViews;
	}

	public void setTabViews(TabViewsBean tabViews) {
		this.tabViews = tabViews;
	} 
    

}  
