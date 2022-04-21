package fr.legrain.bdg.webapp;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.RowEditEvent;

import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.dossier.service.TaPreferencesService;
import fr.legrain.gandi.api.ServerIaas;
import fr.legrain.gandi.service.remote.IGandiService;

@Named
@ViewScoped  
public class GandiServerController implements Serializable {  

	/**
	 * 
	 */
	private static final long serialVersionUID = 5331153993979458219L;

	private List<ServerIaas> values; 
	
	private @EJB ITaPreferencesServiceRemote taPreferencesService;
	private @EJB IGandiService gandiService;
	
		
	
	private ServerIaas selectedServer;
	List<TaPreferences> listePreferences;
	private TaPreferences prefCleSecuritéGandi;

	public GandiServerController() {  
		//values = getValues();
	}  
	
	@PostConstruct
	public void init(){
		refresh();
	}
	
	public void refresh() {
		try {
			initPreferencesGandi();
			values =  gandiService.listServer(prefCleSecuritéGandi.getValeur());
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
   public void OnRowEditServer(RowEditEvent actionEvent){
//   	try {
//   		//ws.merge(selectedTaControle);
//   		selectedTaControle=controleService.enregistrerMerge(selectedTaControle,ITaControleServiceRemote.validationContext);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
   }
   
   public void startServer(){
	   startServer(null);
   }
   public void startServer(ActionEvent actionEvent){
	   try {
		gandiService.startServer(prefCleSecuritéGandi.getValeur(), selectedServer.getId());
		 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Démarrage en cour de "+selectedServer.getHostname()+" ", ""));
	} catch (Exception e) {
		 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Echec dans le démarrage de "+selectedServer.getHostname()+" ", ""));
		e.printStackTrace();
	}
   }
   
   public void stopServer(){
	   stopServer(null);
   }
   public void stopServer(ActionEvent actionEvent){
	   try {
		gandiService.stopServer(prefCleSecuritéGandi.getValeur(), selectedServer.getId());
		 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Arrêt en cours de "+selectedServer.getHostname()+" ", ""));
	} catch (Exception e) {
		 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Echec dans l'arrêt de "+selectedServer.getHostname()+" ", ""));
		e.printStackTrace();
	}
   }

	public List<ServerIaas> getValues(){  
		return values;
	}

	public ServerIaas getSelectedServer() {
		return selectedServer;
	}

	public void setSelectedServer(ServerIaas selectedServer) {
		this.selectedServer = selectedServer;
	}

	public TaPreferences getPrefCleSecuritéGandi() {
		return prefCleSecuritéGandi;
	}

	public void setPrefCleSecuritéGandi(TaPreferences prefCleSecuritéGandi) {
		this.prefCleSecuritéGandi = prefCleSecuritéGandi;
	}

	
  



}  
