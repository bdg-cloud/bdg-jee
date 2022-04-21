package fr.legrain.bdg.webapp;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.RowEditEvent;

import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.dossier.service.TaPreferencesService;
import fr.legrain.gandi.api.InfosCertifSSL;
import fr.legrain.gandi.service.remote.IGandiService;

@Named
@ViewScoped  
public class GandiCertifSSLController implements Serializable {  

	/**
	 * 
	 */
	private static final long serialVersionUID = 5331153993979458219L;

	private List<InfosCertifSSL> values; 
	
	private @EJB ITaPreferencesServiceRemote taPreferencesService;
	private @EJB IGandiService gandiService;
	
		
	
	private InfosCertifSSL selectedInfosCertifSSL;
	List<TaPreferences> listePreferences;
	private TaPreferences prefCleSecuritéGandi;

	public GandiCertifSSLController() {  
		//values = getValues();
	}  
	
	@PostConstruct
	public void init(){
		refresh();
	}
	
	public void refresh() {
		try {
			initPreferencesGandi();
			values =  gandiService.listInfosCertifSSL(prefCleSecuritéGandi.getValeur());
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
   public void OnRowEditInfosDomainList(RowEditEvent actionEvent){
//   	try {
//   		//ws.merge(selectedTaControle);
//   		selectedTaControle=controleService.enregistrerMerge(selectedTaControle,ITaControleServiceRemote.validationContext);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
   }
   
//   public void startInfosDomainList(){
//	   startInfosDomainList(null);
//   }
//   public void startInfosDomainList(ActionEvent actionEvent){
//	   try {
//		gandiService.startInfosDomainList(prefCleSecuritéGandi.getValeur(), selectedInfosCertifSSL.getId());
//		 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Démarrage en cour de "+selectedInfosCertifSSL.getHostname()+" ", ""));
//	} catch (Exception e) {
//		 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Echec dans le démarrage de "+selectedInfosCertifSSL.getHostname()+" ", ""));
//		e.printStackTrace();
//	}
//   }
//   
//   public void stopInfosDomainList(){
//	   stopInfosDomainList(null);
//   }
//   public void stopInfosDomainList(ActionEvent actionEvent){
//	   try {
//		gandiService.stopInfosDomainList(prefCleSecuritéGandi.getValeur(), selectedInfosCertifSSL.getId());
//		 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Arrêt en cours de "+selectedInfosCertifSSL.getHostname()+" ", ""));
//	} catch (Exception e) {
//		 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Echec dans l'arrêt de "+selectedInfosCertifSSL.getHostname()+" ", ""));
//		e.printStackTrace();
//	}
//   }

	public List<InfosCertifSSL> getValues(){  
		return values;
	}

	public InfosCertifSSL getSelectedInfosCertifSSL() {
		return selectedInfosCertifSSL;
	}

	public void setSelectedInfosCertifSSL(InfosCertifSSL selectedInfosCertifSSL) {
		this.selectedInfosCertifSSL = selectedInfosCertifSSL;
	}

	public TaPreferences getPrefCleSecuritéGandi() {
		return prefCleSecuritéGandi;
	}

	public void setPrefCleSecuritéGandi(TaPreferences prefCleSecuritéGandi) {
		this.prefCleSecuritéGandi = prefCleSecuritéGandi;
	}


  



}  
