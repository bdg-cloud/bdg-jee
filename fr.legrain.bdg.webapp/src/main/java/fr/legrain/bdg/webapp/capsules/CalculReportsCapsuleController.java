package fr.legrain.bdg.webapp.capsules;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.primefaces.component.tabview.TabView;

import fr.legrain.article.dto.TaFamilleDTO;
import fr.legrain.article.titretransport.dto.TaStockCapsulesDTO;
import fr.legrain.article.titretransport.dto.TaTitreTransportDTO;
import fr.legrain.article.titretransport.model.TaEtatStockCapsules;
import fr.legrain.bdg.article.service.remote.ITaFamilleServiceRemote;
import fr.legrain.bdg.article.titretransport.service.remote.IEtatStockCapsulesServiceRemote;
import fr.legrain.bdg.article.titretransport.service.remote.ITaStockCapsulesServiceRemote;
import fr.legrain.bdg.article.titretransport.service.remote.ITaTitreTransportServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceLocal;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.dossier.model.TaInfoEntreprise;


@Named
@ViewScoped 
public class CalculReportsCapsuleController implements Serializable {
	static Logger logger = Logger.getLogger(CalculReportsCapsuleController.class.getName());		

	private @EJB ITaInfoEntrepriseServiceLocal taInfoEntrepriseService;
	protected @EJB ITaPreferencesServiceRemote taPreferencesService;
	private @EJB ITaTitreTransportServiceRemote taTitreTransportService;
	private @EJB ITaFamilleServiceRemote taFamilleService;
	private @EJB ITaStockCapsulesServiceRemote stocksCapsulesService; 
	private @EJB IEtatStockCapsulesServiceRemote etatStocks;

	public static final String C_SYNTHESE ="synthese";
	public static final String C_MOUVEMENT_TYPE ="mouvement_type";
	public static final String C_MOUVEMENT_DATE ="mouvement_date";
	
	public static final String C_EDITION_SYNTHESE = "/reports/capsules/EtatStocksCapsules.rptdesign&amp;__format=pdf";
	public static final String C_EDITION_MOUVEMENTS_DATE = "/reports/capsules/EtatStocksCapsulesMouvementsDate.rptdesign&amp;__format=pdf";
	public static final String C_EDITION_MOUVEMENTS_TYPE = "/reports/capsules/EtatStocksCapsulesMouvementsType.rptdesign&amp;__format=pdf";
	
	private TaTitreTransportDTO titreTransportDTO;
	
	private TaStockCapsulesDTO dto=new TaStockCapsulesDTO();
	private TaInfoEntreprise infos;
	private Date dateCalcul = new Date();
	private Date dateCalculDeb = new Date();
	private String  synthese ;
	private List<TaEtatStockCapsules> listeEtat;
	
	private boolean exclureQteSansUnite;
	
	private TabView tabViewCapsules;
	
	public CalculReportsCapsuleController() {

	}

	@PostConstruct
	public void init() {
		infos=taInfoEntrepriseService.findInstance();
		dateCalcul=new Date();
		dateCalculDeb=etatStocks.recupDerniereDateCalcul();
		if(dateCalculDeb==null)dateCalculDeb=new Date(0);
	}

	public void actFermer(ActionEvent e) {
		
	}
	
	
	public void actImprimer(String synthese) throws Exception {
		try {

			TaEtatStockCapsules criteres = new TaEtatStockCapsules();
			
			criteres.setDateStock(dateCalcul);

			List<TaEtatStockCapsules> listeReports = etatStocks.calculEtatStocks(criteres,null,false);
			etatStocks.recalculReport(listeReports, dateCalcul);

		
		} catch (Exception e) {

			logger.error("",e);
		}
	
	}
	
	

	
	
	public void autoCompleteMapUIToDTO() {

		if(titreTransportDTO!=null) {
			dto.setCodeTitreTransport(titreTransportDTO.getCodeTitreTransport());
		}
	}
		



	public TaStockCapsulesDTO getDto() {
		return dto;
	}





	public void setDto(TaStockCapsulesDTO dto) {
		this.dto = dto;
	}



	public String getSynthese() {
		return synthese;
	}





	public void setSynthese(String synthese) {
		this.synthese = synthese;
	}





	public boolean isExclureQteSansUnite() {
		return exclureQteSansUnite;
	}





	public void setExclureQteSansUnite(boolean exclureQteSansUnite) {
		this.exclureQteSansUnite = exclureQteSansUnite;
	}
	
	public List<TaFamilleDTO> famillesAutoCompleteDTOLight(String query) {
		List<TaFamilleDTO> allValues = taFamilleService.findByCodeLight("*");
		List<TaFamilleDTO> filteredValues = new ArrayList<TaFamilleDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			TaFamilleDTO civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeFamille().toLowerCase().contains(query.toLowerCase())
					) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	
	public List<TaTitreTransportDTO> titreTransportAutoCompleteDTOLight(String query) {
		List<TaTitreTransportDTO> allValues = taTitreTransportService.selectAllDTO();
		List<TaTitreTransportDTO> filteredValues = new ArrayList<TaTitreTransportDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			TaTitreTransportDTO civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeTitreTransport().toLowerCase().contains(query.toLowerCase())
					) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}




	public Date getDateCalcul() {
		return dateCalcul;
	}

	public void setDateCalcul(Date dateCalcul) {
		this.dateCalcul = dateCalcul;
	}

	public TaTitreTransportDTO getTitreTransportDTO() {
		return titreTransportDTO;
	}

	public void setTitreTransportDTO(TaTitreTransportDTO titreTransportDTO) {
		this.titreTransportDTO = titreTransportDTO;
	}

	public TabView getTabViewCapsules() {
		return tabViewCapsules;
	}

	public void setTabViewCapsules(TabView tabViewCapsules) {
		this.tabViewCapsules = tabViewCapsules;
	}

	public Date getDateCalculDeb() {
		return dateCalculDeb;
	}

	public void setDateCalculDeb(Date dateCalculDeb) {
		this.dateCalculDeb = dateCalculDeb;
	}

}



