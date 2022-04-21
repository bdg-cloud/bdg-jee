package fr.legrain.bdg.webapp.capsules;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
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
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.dms.model.TaEtatMouvementDms;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.lib.data.LibDate;


@Named
@ViewScoped 
public class EtatStockCapsuleController implements Serializable {
	static Logger logger = Logger.getLogger(EtatStockCapsuleController.class.getName());		

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
	private String  synthese ;
	private List<TaEtatStockCapsules> listeEtat;
	
	private boolean exclureQteSansUnite;
	
	private TabView tabViewCapsules;
	
	public EtatStockCapsuleController() {

	}

	@PostConstruct
	public void init() {
		infos=taInfoEntrepriseService.findInstance();
		dateCalcul=new Date();
		
		if(dateCalcul.after(
				infos.getDatefinInfoEntreprise())){
			if(infos.getDatefinInfoEntreprise()!=null)
//				dateCalcul=LibDate.incrementDate(infos.getDatefinInfoEntreprise(), -1, 0, 0) ;
				dateCalcul=infos.getDatefinInfoEntreprise() ; //Enlevé par isa à la demande de Denis le 20/01/2021 
		}
	}

	public void actFermer(ActionEvent e) {
		
	}
	
	
	public void actImprimer(String synthese) throws Exception {
		TaEtatStockCapsules criteres = new TaEtatStockCapsules();
		autoCompleteMapUIToDTO();
		
		if(dto.getCodeTitreTransport()!=null && !dto.getCodeTitreTransport().equals("")){
			criteres.setCodeTitreTransport(dto.getCodeTitreTransport());
		}else criteres.setCodeTitreTransport("");
		
		if(dto.getLibelleArticle()!=null && !dto.getLibelleArticle().equals("")){
			criteres.setLibelleTitreTransport(dto.getLibelleArticle());
		}else criteres.setLibelleTitreTransport("");
		
			criteres.setDateStock(dateCalcul);
			criteres.setDateStockDeb(null);

			criteres.setAvecReport(true);

			if(synthese.equals(C_SYNTHESE)){
				listeEtat = etatStocks.calculEtatStocks(criteres,true,true);
//				listeEtat = etatStocks.calculEtatStocks(criteres,dateCalcul ,true,true);
			}
			if(synthese.equals(C_MOUVEMENT_DATE)){
				listeEtat = etatStocks.calculEtatStocksMouvements(criteres,true);
			}
			if(synthese.equals(C_MOUVEMENT_TYPE)){
				listeEtat = etatStocks.calculEtatStocksMouvements(criteres,true);
			}
		imprimer(criteres);
	}
	
	public String choixEdition() {
		if(synthese.equals(C_SYNTHESE)){
			return  C_EDITION_SYNTHESE+"&DateFin="+LibDate.dateToString(dateCalcul);
		}else if(synthese.equals(C_EDITION_MOUVEMENTS_DATE)){
			return  C_EDITION_MOUVEMENTS_DATE+"&DateFin="+LibDate.dateToString(dateCalcul);
		}	else if(synthese.equals(C_EDITION_MOUVEMENTS_TYPE)){
			return  C_EDITION_MOUVEMENTS_TYPE+"&DateFin="+LibDate.dateToString(dateCalcul);
		}		
		return "";
	}
	
	public void imprimer(TaEtatStockCapsules criteres) {
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Etat stocks capsules", "actImprimer")); 
		}
		try {
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			
			Map<String,Object> mapEdition = new HashMap<String,Object>();
			
			
			mapEdition.put("listeObjet",listeEtat );
			mapEdition.put("taInfoEntreprise", taInfoEntrepriseService.findInstance());
			mapEdition.put("criteres", criteres);
		
			Map<String,Object> mapParametreEdition = new HashMap<String,Object>();
			List<TaPreferences> liste= taPreferencesService.findByGroupe("capsules");
			mapParametreEdition.put("preferences", liste);
			mapEdition.put("param", mapParametreEdition);
			
			sessionMap.put("edition", mapEdition);

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void autoCompleteMapUIToDTO() {

		if(titreTransportDTO!=null) {
			dto.setCodeTitreTransport(titreTransportDTO.getCodeTitreTransport());
		}
	}
		
	public void imprimer(TaEtatMouvementDms criteres) {
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Dms", "actImprimer")); 
		}
		try {
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			
			Map<String,Object> mapEdition = new HashMap<String,Object>();
			
			
			mapEdition.put("listeObjet",listeEtat );
			mapEdition.put("taInfoEntreprise", taInfoEntrepriseService.findInstance());
			mapEdition.put("criteres", criteres);
		
			Map<String,Object> mapParametreEdition = new HashMap<String,Object>();
			List<TaPreferences> liste= taPreferencesService.findByGroupe("dms");
			mapParametreEdition.put("preferences", liste);
			mapEdition.put("param", mapParametreEdition);
			
			sessionMap.put("edition", mapEdition);

			System.out.println("EtatDmsController.actImprimer()");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

}



