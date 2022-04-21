package fr.legrain.bdg.webapp.dms;

import java.io.Serializable;
import java.math.BigDecimal;
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

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.TaFamilleDTO;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaFamilleServiceRemote;
import fr.legrain.bdg.dms.service.remote.IEtatDmsServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceLocal;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.dms.dto.DmsDTO;
import fr.legrain.dms.model.TaEtatMouvementDms;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;


@Named
@ViewScoped 
public class EtatDmsController implements Serializable {
	static Logger logger = Logger.getLogger(EtatDmsController.class.getName());		

	private @EJB ITaInfoEntrepriseServiceLocal taInfoEntrepriseService;
	protected @EJB ITaPreferencesServiceRemote taPreferencesService;
	private @EJB ITaArticleServiceRemote taArticleService;
	private @EJB ITaFamilleServiceRemote taFamilleService;
	private @EJB IEtatDmsServiceRemote etatDms; 

	public static final String C_SYNTHESE ="synthese";
	public static final String C_MOUVEMENT ="mouvement";
	
	public static final String C_EDITION_SYNTHESE = "/reports/stock/EtatSyntheseDms.rptdesign&amp;__format=pdf";
	public static final String C_EDITION_MOUVEMENTS = "/reports/stock/EtatDms.rptdesign&amp;__format=pdf";
	
	private TaArticleDTO articleDTO;
	private TaFamilleDTO familleDTO;
	
	private DmsDTO dto=new DmsDTO();
	private TaInfoEntreprise infos;
	private Integer mois;
	private Integer annee;
	private String  synthese ;
	private List<TaEtatMouvementDms> listeEtat;
	
	private boolean exclureQteSansUnite;
	
	
	
	public EtatDmsController() {

	}

	@PostConstruct
	public void init() {
		infos=taInfoEntrepriseService.findInstance();
		mois=LibConversion.stringToInteger(LibDate.getMois(new Date()));
		annee=LibConversion.stringToInteger(LibDate.getAnnee(new Date()));
	}

	public void actFermer(ActionEvent e) {
		
	}
	
	
	public void actImprimer(String synthese) throws Exception {
		autoCompleteMapUIToDTO();
		TaEtatMouvementDms criteres = new TaEtatMouvementDms();
		
		if(dto.getCodeArticle()!=null && !dto.getCodeArticle().equals("")){
			criteres.setCodeArticle(dto.getCodeArticle());
		}else criteres.setCodeArticle("");
		
		if(dto.getCodeFamille()!=null && !dto.getCodeFamille().equals("")){
			criteres.setCodeFamille(dto.getCodeFamille());
		}else criteres.setCodeFamille("");
		
		if(mois!=null && !mois.equals("")){
			criteres.setMois(mois);
		}else criteres.setMois(0);
		
		if(annee!=null &&!annee.equals("")){
			criteres.setAnnee(annee);
		}else criteres.setAnnee(0);
		
		if(dto.getQte1()!=null && !dto.getQte1().equals("")){
			criteres.setQte1(dto.getQte1());
		}else criteres.setQte1(BigDecimal.ZERO);
		
		if(dto.getQte2()!=null && !dto.getQte2().equals("")){
			criteres.setQte2(dto.getQte2());
		}else criteres.setQte2(BigDecimal.ZERO);
		
		if(dto.getUn1()!=null && !dto.getUn1().equals("")){
			criteres.setUn1(dto.getUn1());
		}else criteres.setUn1("");
		
		if(dto.getUn2()!=null && !dto.getUn2().equals("")){
			criteres.setUn2(dto.getUn2());
		}else criteres.setUn2("");
		
		criteres.setExclusionQteSsUnite(exclureQteSansUnite);
		//Récupérer les critères de séléction et récupérer les lignes de stocks
		//en fonction de ces critères
//		listeEtat.clear();
		if(synthese.equals(C_SYNTHESE)){
			listeEtat = etatDms.calculEtatDms(criteres,true);

		}else{
			listeEtat = etatDms.calculEtatDms(criteres,false);
		}
		imprimer(criteres);
	}
	
	public String choixEdition() {
		if(synthese.equals(C_SYNTHESE)){
			return  C_EDITION_SYNTHESE;
		}else if(synthese.equals(C_MOUVEMENT)){
			return  C_EDITION_MOUVEMENTS;
		}		
	return "";
	}
	
	public void autoCompleteMapUIToDTO() {

		if(articleDTO!=null) {
			dto.setCodeArticle(articleDTO.getCodeArticle());
		}
		if(familleDTO!=null) {
			dto.setCodeFamille(familleDTO.getCodeFamille());
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





	public DmsDTO getDto() {
		return dto;
	}





	public void setDto(DmsDTO dto) {
		this.dto = dto;
	}





	public Integer getMois() {
		return mois;
	}





	public void setMois(Integer mois) {
		this.mois = mois;
	}





	public Integer getAnnee() {
		return annee;
	}





	public void setAnnee(Integer annee) {
		this.annee = annee;
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
	
	public List<TaArticleDTO> articlesAutoCompleteDTOLight(String query) {
		List<TaArticleDTO> allValues = taArticleService.findByCodeLight(query.toUpperCase());
		List<TaArticleDTO> filteredValues = new ArrayList<TaArticleDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			TaArticleDTO civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeArticle().toLowerCase().contains(query.toLowerCase())
					) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}

	public TaArticleDTO getArticleDTO() {
		return articleDTO;
	}

	public void setArticleDTO(TaArticleDTO articleDTO) {
		this.articleDTO = articleDTO;
	}

	public TaFamilleDTO getFamilleDTO() {
		return familleDTO;
	}

	public void setFamilleDTO(TaFamilleDTO familleDTO) {
		this.familleDTO = familleDTO;
	}

}



