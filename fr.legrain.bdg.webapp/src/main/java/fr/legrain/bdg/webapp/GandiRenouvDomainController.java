package fr.legrain.bdg.webapp;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import fr.legrain.gandi.api.InfosDomainList;
import fr.legrain.gandi.api.InfosOperationDomainRenouv;
import fr.legrain.gandi.api.InfosPackMail;
import fr.legrain.gandi.service.remote.IGandiService;

@Named
@ViewScoped  
public class GandiRenouvDomainController implements Serializable {  
	/**
	 * 
	 */
	private static final long serialVersionUID = -3637305893407365044L;
	
//	private List<InfosDomainList> values; 
	private InfosOperationDomainRenouv opeRenouv = new InfosOperationDomainRenouv();
	
	
	private @EJB ITaPreferencesServiceRemote taPreferencesService;
	private @EJB IGandiService gandiService;
	
		
	
	private InfosDomainList selectedInfosDomainList;
	List<TaPreferences> listePreferences;
	private TaPreferences prefCleSecuritéGandi;
	private InfosPackMail packMail;
	
	private int dureeRenouv;
	
	private String nomDomaine;
	
	private boolean renouvNom = true;
	private boolean renouvPackMail = false;
	private int year;

	public GandiRenouvDomainController() {  
	}  
	
	@PostConstruct
	public void init(){
		refresh();
	}
	
	public void refresh() {
		try {
			initPreferencesGandi();
			 year = Calendar.getInstance().get(Calendar.YEAR);
			 Map map = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
			 nomDomaine = (String) map.get("domaine");
			 packMail = gandiService.infosPackMail(prefCleSecuritéGandi.getValeur(), nomDomaine);
			 System.out.println(packMail.getDate_end());
			 
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

	public void renouvDomain() {
		renouvDomain(null);
	}
	public void renouvDomain(ActionEvent actionEvent) {
		try {
			if(renouvNom) {
				HashMap<String, Integer> params = new HashMap<String, Integer>();
				params.put("current_year", year);
				params.put("duration", dureeRenouv);
				System.out.println(nomDomaine);
				opeRenouv = gandiService.renouvDomaine(prefCleSecuritéGandi.getValeur(), nomDomaine,params);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Renouvelement du nom de domaine pris en compte", ""));
			}
			
			if(renouvPackMail) {
				System.out.println("Renouvelement du pack mail");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Renouvelement du pack mail pris en compte", ""));
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Echec du renouvellement"));
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

	public InfosOperationDomainRenouv getOpeRenouv() {
		return opeRenouv;
	}

	public void setOpeRenouv(InfosOperationDomainRenouv opeRenouv) {
		this.opeRenouv = opeRenouv;
	}

	public boolean isRenouvNom() {
		return renouvNom;
	}

	public void setRenouvNom(boolean renouvNom) {
		this.renouvNom = renouvNom;
	}

	public boolean isRenouvPackMail() {
		return renouvPackMail;
	}

	public void setRenouvPackMail(boolean renouvPackMail) {
		this.renouvPackMail = renouvPackMail;
	}

	public InfosPackMail getPackMail() {
		return packMail;
	}

	public void setPackMail(InfosPackMail packMail) {
		this.packMail = packMail;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getDureeRenouv() {
		return dureeRenouv;
	}

	public void setDureeRenouv(int dureeRenouv) {
		this.dureeRenouv = dureeRenouv;
	}


  



}  
