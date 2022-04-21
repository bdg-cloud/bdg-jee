package fr.legrain.bdg.webapp.dev;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import fr.legrain.general.service.DevService;
import fr.legrain.general.service.remote.IGenerationDonneesServiceRemote;

@Named
@ViewScoped 
public class GenerationDonneesBean implements Serializable {  

	private static final long serialVersionUID = -1074175405914235174L;

	@EJB DevService devService;

	private @EJB IGenerationDonneesServiceRemote generationDonneesService;
	
	private Integer nbTotal = 1;
	private Integer nbFait = 0;
	private Integer progress = 0;
	
	private Integer nbArticle = 5;
	private Integer nbTiers = 5;
	private Integer nbBR = 0;
	private Integer nbFAB = 0;
	private Integer nbControleConf = 0;


	public void refresh() {

	}

	@PostConstruct
	public void init() {
		
	}

	public void genTousLesParametres(ActionEvent event) {
		generationDonneesService.genTousLesParametres();

		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("genTousLesParametres", 
				"Génération de données tous"
				)); 
	}
	
	public void genTousLesParametresArticles(ActionEvent event) {
		generationDonneesService.genTousLesParametresArticles();
		
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("genTousLesParametresArticles", 
				"Génération de données tous"
				)); 
	}
	
	public void genTousLesParametresTiers(ActionEvent event) {
		
		generationDonneesService.genTousLesParametresTiers();
		
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("genTousLesParametresTiers", 
				"Génération de données tous"
				)); 
	}
	
	public void genTousLesParametresSolstyce(ActionEvent event) {
		
		generationDonneesService.genTousLesParametresSolstyce();

		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("genTousLesParametresSolstyce", 
				"Génération de données tous"
				)); 
	}
	
	public void genFamilleArticle(ActionEvent event) {
		generationDonneesService.genFamilleArticle();

		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("genFamilleArticle", 
				"Génération de données"
				)); 
	}
	
	public void genUnites(ActionEvent event) {
		generationDonneesService.genUnites();

		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("genUnites", 
				"Génération de données"
				)); 
	}
	
	public void genTypeAdresse(ActionEvent event) {
		generationDonneesService.genTypeAdresse();

		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("genTypeAdresse", 
				"Génération de données"
				)); 
	}
	
	public void genTypeCivilite(ActionEvent event) {
		generationDonneesService.genTypeCivilite();

		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("genTypeCivilite", 
				"Génération de données"
				)); 
	}
	
	public void genTypeEntite(ActionEvent event) {
		generationDonneesService.genTypeEntite();

		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("gentypeEntite", 
				"Génération de données"
				)); 
	}
	
	public void genTypeTelephone(ActionEvent event) {
		generationDonneesService.genTypeTelephone();

		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("genTypeTelephone", 
				"Génération de données"
				)); 
	}
	
	public void genTypeTiers(ActionEvent event) {
		generationDonneesService.genTypeTiers();

		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("genTypeTiers", 
				"Génération de données"
				)); 
	}
	
	public void genTypeWeb(ActionEvent event) {
		generationDonneesService.genTypeWeb();

		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("genTypeWeb", 
				"Génération de données"
				)); 
	}
	
	public void genTypeEmail(ActionEvent event) {
		generationDonneesService.genTypeEmail();

		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("genTypeEmail", 
				"Génération de données"
				)); 
	}
	
	public void genFamilleTiers(ActionEvent event) {
		generationDonneesService.genFamilleTiers();

		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("genFamilleTiers", 
				"Génération de données"
				)); 
	}
	
	public void genEntrepot(ActionEvent event) {
		generationDonneesService.genEntrepot();

		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("genEntrepot", 
				"Génération de données"
				)); 
	}
	
	public void genTypeFabrication(ActionEvent event) {
		generationDonneesService.genTypeFabrication();

		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("genTypeFabrication", 
				"Génération de données"
				)); 
	}
	
	public void genTypeReception(ActionEvent event) {
		generationDonneesService.genTypeReception();

		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("genTypeReception", 
				"Génération de données"
				)); 
	}
	
	public void genTypeCodeBarre(ActionEvent event) {
		generationDonneesService.genTypeCodeBarre();

		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("genTypeCodeBarre", 
				"Génération de données"
				)); 
	}
	
	public void genGroupeControles(ActionEvent event) {
		generationDonneesService.genGroupeControles();

		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("genGroupeControles", 
				"Génération de données"
				)); 
	}
	
	public void genData(ActionEvent event) {
		nbTotal = nbArticle+nbTiers+nbBR+nbFAB+nbControleConf;
		generationDonneesService.genData();

		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Data Gen", 
				"Génération de données"
				)); 
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	 
	public Integer getProgress() {
		if(progress == null) {
			progress = 0;
		}
		progress = (nbFait*100)/nbTotal;
		if(progress > 100) {
			progress = 100;
		}
		return progress;
	}
 
    public void setProgress(Integer progress) {
        this.progress = progress;
    }
     
    public void onComplete() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Progress Completed"));
    }
     
    public void cancel() {
        progress = null;
    }

	public Integer getNbArticle() {
		return nbArticle;
	}

	public void setNbArticle(Integer nbArticle) {
		this.nbArticle = nbArticle;
	}

	public Integer getNbTiers() {
		return nbTiers;
	}

	public void setNbTiers(Integer nbTiers) {
		this.nbTiers = nbTiers;
	}

	public Integer getNbBR() {
		return nbBR;
	}

	public void setNbBR(Integer nbBR) {
		this.nbBR = nbBR;
	}

	public Integer getNbFAB() {
		return nbFAB;
	}

	public void setNbFAB(Integer nbFAB) {
		this.nbFAB = nbFAB;
	}

	public Integer getNbControleConf() {
		return nbControleConf;
	}

	public void setNbControleConf(Integer nbControleConf) {
		this.nbControleConf = nbControleConf;
	}

}  
