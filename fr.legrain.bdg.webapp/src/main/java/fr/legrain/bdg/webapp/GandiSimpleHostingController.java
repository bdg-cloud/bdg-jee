package fr.legrain.bdg.webapp;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.component.tabview.TabView;
import org.primefaces.event.RowEditEvent;

import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.dossier.service.TaPreferencesService;
import fr.legrain.gandi.api.InfosSimpleHostingList;
import fr.legrain.gandi.service.remote.IGandiService;

@Named
@ViewScoped  
public class GandiSimpleHostingController implements Serializable {  

	/**
	 * 
	 */
	private static final long serialVersionUID = 5331153993979458219L;

	private List<InfosSimpleHostingList> values; 
	
	private @EJB ITaPreferencesServiceRemote taPreferencesService;
	private @EJB IGandiService gandiService;
	
	
	private TabView tabViewGandiSimpleHosting;
	
		
	
	private InfosSimpleHostingList selectedInfosSimpleHostingList;
	List<TaPreferences> listePreferences;
	private TaPreferences prefCleSecuritéGandi;

	public GandiSimpleHostingController() {  
		//values = getValues();
	}  
	
	@PostConstruct
	public void init(){
		refresh();
	}
	
	public void refresh() {
		try {
			initPreferencesGandi();
			values =  gandiService.listInfosSimpleHosting(prefCleSecuritéGandi.getValeur());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initPreferencesGandi() {
		try {
			prefCleSecuritéGandi = taPreferencesService.findByGoupeAndCle("Gwi-Hosting", TaPreferencesService.CLE_SECURITE_API_GANDI);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    /**
    *
    * @param actionEvent
    */
   public void OnRowEditInfosSimpleHostingList(RowEditEvent actionEvent){
//   	try {
//   		//ws.merge(selectedTaControle);
//   		selectedTaControle=controleService.enregistrerMerge(selectedTaControle,ITaControleServiceRemote.validationContext);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
   }
   
//   public void startInfosSimpleHostingList(){
//	   startInfosSimpleHostingList(null);
//   }
//   public void startInfosSimpleHostingList(ActionEvent actionEvent){
//	   try {
//		gandiService.startInfosSimpleHostingList(prefCleSecuritéGandi.getValeur(), selectedInfosSimpleHostingList.getId());
//		 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Démarrage en cour de "+selectedInfosSimpleHostingList.getHostname()+" ", ""));
//	} catch (Exception e) {
//		 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Echec dans le démarrage de "+selectedInfosSimpleHostingList.getHostname()+" ", ""));
//		e.printStackTrace();
//	}
//   }
//   
//   public void stopInfosSimpleHostingList(){
//	   stopInfosSimpleHostingList(null);
//   }
//   public void stopInfosSimpleHostingList(ActionEvent actionEvent){
//	   try {
//		gandiService.stopInfosSimpleHostingList(prefCleSecuritéGandi.getValeur(), selectedInfosSimpleHostingList.getId());
//		 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Arrêt en cours de "+selectedInfosSimpleHostingList.getHostname()+" ", ""));
//	} catch (Exception e) {
//		 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Echec dans l'arrêt de "+selectedInfosSimpleHostingList.getHostname()+" ", ""));
//		e.printStackTrace();
//	}
//   }

	public List<InfosSimpleHostingList> getValues(){  
		return values;
	}

	public InfosSimpleHostingList getSelectedInfosSimpleHostingList() {
		return selectedInfosSimpleHostingList;
	}

	public void setSelectedInfosSimpleHostingList(InfosSimpleHostingList selectedInfosSimpleHostingList) {
		this.selectedInfosSimpleHostingList = selectedInfosSimpleHostingList;
	}

	public TaPreferences getPrefCleSecuritéGandi() {
		return prefCleSecuritéGandi;
	}

	public void setPrefCleSecuritéGandi(TaPreferences prefCleSecuritéGandi) {
		this.prefCleSecuritéGandi = prefCleSecuritéGandi;
	}

	public TabView getTabViewGandiSimpleHosting() {
		return tabViewGandiSimpleHosting;
	}

	public void setTabViewGandiSimpleHosting(TabView tabViewGandiSimpleHosting) {
		this.tabViewGandiSimpleHosting = tabViewGandiSimpleHosting;
	}


  



}  
