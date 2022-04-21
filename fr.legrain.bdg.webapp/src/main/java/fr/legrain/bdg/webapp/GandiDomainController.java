package fr.legrain.bdg.webapp;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.RowEditEvent;

import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.dossier.service.TaPreferencesService;
import fr.legrain.gandi.api.InfosAvailabilityDomainList;
import fr.legrain.gandi.api.InfosDomainList;
import fr.legrain.gandi.api.InfosOperationDomainRenouv;
import fr.legrain.gandi.service.remote.IGandiService;

@Named
@ViewScoped  
public class GandiDomainController implements Serializable {  

	/**
	 * 
	 */
	private static final long serialVersionUID = 5331153993979458219L;

	private List<InfosDomainList> values; 
	private List<InfosAvailabilityDomainList> availabilityList = new ArrayList<InfosAvailabilityDomainList>();
	private InfosOperationDomainRenouv opeRenouv = new InfosOperationDomainRenouv();
	
	
	private @EJB ITaPreferencesServiceRemote taPreferencesService;
	private @EJB IGandiService gandiService;
	
	private @Inject GandiRenouvDomainController  gandiRenouvDomainController;
	
		
	
	private InfosDomainList selectedInfosDomainList;
	List<TaPreferences> listePreferences;
	private TaPreferences prefCleSecuritéGandi;
	
	private String nomDomaine;

	public GandiDomainController() {  
	}  
	
	@PostConstruct
	public void init(){
		refresh();
	}
	
	public void refresh() {
		try {
			initPreferencesGandi();
			values =  gandiService.listInfosDomaine(prefCleSecuritéGandi.getValeur());
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
	
	
	public void checkAvailability() {
		checkAvailability(null);
	}
	public void checkAvailability(ActionEvent actionEvent) {
		List<String> listNomDomaine = new ArrayList<String>();
		listNomDomaine.add(nomDomaine);
		availabilityList = gandiService.listInfosDomaineAvailability(prefCleSecuritéGandi.getValeur(), listNomDomaine);
	}
	
	
	public void redirectRenouvDomain() {
		redirectRenouvDomain(null);
	}
	public void redirectRenouvDomain(ActionEvent actionEvent) {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		try {
			System.out.println(selectedInfosDomainList);
			context.redirect(context.getRequestContextPath() + "/admin/dev/gandi_domain_renouv.xhtml?domaine="+selectedInfosDomainList.getFqdn());
		} catch (IOException e) {
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
   

	public List<InfosDomainList> getValues(){  
		return values;
	}

	public InfosDomainList getSelectedInfosDomainList() {
		return selectedInfosDomainList;
	}

	public void setSelectedInfosDomainList(InfosDomainList selectedInfosDomainList) {
		this.selectedInfosDomainList = selectedInfosDomainList;
	}

	public TaPreferences getPrefCleSecuritéGandi() {
		return prefCleSecuritéGandi;
	}

	public void setPrefCleSecuritéGandi(TaPreferences prefCleSecuritéGandi) {
		this.prefCleSecuritéGandi = prefCleSecuritéGandi;
	}

	public String getNomDomaine() {
		return nomDomaine;
	}

	public void setNomDomaine(String nomDomaine) {
		this.nomDomaine = nomDomaine;
	}

	public List<InfosAvailabilityDomainList> getAvailabilityList() {
		return availabilityList;
	}

	public void setAvailabilityList(List<InfosAvailabilityDomainList> availabilityList) {
		this.availabilityList = availabilityList;
	}

	public InfosOperationDomainRenouv getOpeRenouv() {
		return opeRenouv;
	}

	public void setOpeRenouv(InfosOperationDomainRenouv opeRenouv) {
		this.opeRenouv = opeRenouv;
	}

	public GandiRenouvDomainController getGandiRenouvDomainController() {
		return gandiRenouvDomainController;
	}

	public void setGandiRenouvDomainController(GandiRenouvDomainController gandiRenouvDomainController) {
		this.gandiRenouvDomainController = gandiRenouvDomainController;
	}


  



}  
