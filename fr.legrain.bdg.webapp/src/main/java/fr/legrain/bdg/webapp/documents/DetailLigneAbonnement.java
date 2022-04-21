package fr.legrain.bdg.webapp.documents;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import fr.legrain.bdg.documents.service.remote.ITaLEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLigneALigneEcheanceServiceRemote;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.document.dto.TaLigneALigneEcheanceDTO;
import fr.legrain.document.model.TaAbonnement;

@Named
@ViewScoped
public class DetailLigneAbonnement extends AbstractController {

	
	private static final String C_DIALOG = "dialog";
	private String titlePartOne="Détail d'une ligne d'abonnement : ";
	private String titlePartTwo="";
	

//	private  ILigneDocumentJSF ligneDocumentDTO;
	private TaLigneALigneEcheanceDTO taLigneALigneEcheanceDTO;
//	private TaLigneALigneDTO selectedLigneALigneEcheanceDTO;
	
	@Inject @Named(value="duplicationDocumentBean")
	private DuplicationDocumentBean duplicationDocumentBean;
	
	private @EJB  ITaLEcheanceServiceRemote taLEcheanceService;
	private @EJB  ITaLigneALigneEcheanceServiceRemote taLigneALigneEcheanceService;
	
	
//	private List<TaLigneALigneEcheanceDTO> ligneLie;

	private Boolean readOnly=false;
	private Boolean dansDialogue;
	
	List<TaLigneALigneEcheanceDTO> ligneAbonnement;
	private String typeDocAbonnement = TaAbonnement.TYPE_DOC;
	
	public Boolean getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	
	@PostConstruct
	public void postConstruct(){
		try {


			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();

			if(sessionMap.get("mapDialogue")!=null) {
				Map<String,Object>	mapDialogue = (Map<String, Object>) sessionMap.get("mapDialogue");
				if(mapDialogue!=null && mapDialogue.size()>0){
					if(mapDialogue.get("taLigneALigneEcheanceDTO")!=null){
						taLigneALigneEcheanceDTO = (TaLigneALigneEcheanceDTO) mapDialogue.get("taLigneALigneEcheanceDTO");
						dansDialogue =  (Boolean) mapDialogue.get("dansDialogue");
						titlePartTwo=taLigneALigneEcheanceDTO.getLibelleLigne();		
					}
					if(mapDialogue.get("readOnly")!=null){
						readOnly=(Boolean) mapDialogue.get("readOnly");
					}
				}
				cleanAppelDialogue();
			}
			refresh();


		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void cleanAppelDialogue() {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		
		sessionMap.remove("mapDialogue");
	}
	
	
	
	public void refresh(){
		try {
			
			
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

	public String getTitlePartOne() {
		return titlePartOne;
	}

	public void setTitlePartOne(String titlePartOne) {
		this.titlePartOne = titlePartOne;
	}

	public String getTitlePartTwo() {
		return titlePartTwo;
	}

	public void setTitlePartTwo(String titlePartTwo) {
		this.titlePartTwo = titlePartTwo;
	}

	
	public DuplicationDocumentBean getDuplicationDocumentBean() {
		return duplicationDocumentBean;
	}

	public void setDuplicationDocumentBean(DuplicationDocumentBean duplicationDocumentBean) {
		this.duplicationDocumentBean = duplicationDocumentBean;
	}

		


	public void actFermerDialog(ActionEvent actionEvent) {
		PrimeFaces.current().dialog().closeDynamic(null);
	}

	public List<TaLigneALigneEcheanceDTO> getLigneAbonnement() {
		return ligneAbonnement;
	}

	public void setLigneAbonnement(List<TaLigneALigneEcheanceDTO> ligneAbonnement) {
		this.ligneAbonnement = ligneAbonnement;
	}

	public String getTypeDocAbonnement() {
		return typeDocAbonnement;
	}

	public void setTypeDocAbonnement(String typeDocAbonnement) {
		this.typeDocAbonnement = typeDocAbonnement;
	}



	public TaLigneALigneEcheanceDTO getTaLigneALigneEcheanceDTO() {
		return taLigneALigneEcheanceDTO;
	}

	public void setTaLigneALigneEcheanceDTO(TaLigneALigneEcheanceDTO taLigneALigneEcheanceDTO) {
		this.taLigneALigneEcheanceDTO = taLigneALigneEcheanceDTO;
	}

	public ITaLEcheanceServiceRemote getTaLEcheanceService() {
		return taLEcheanceService;
	}

	public void setTaLEcheanceService(ITaLEcheanceServiceRemote taLEcheanceService) {
		this.taLEcheanceService = taLEcheanceService;
	}

	public ITaLigneALigneEcheanceServiceRemote getTaLigneALigneEcheanceService() {
		return taLigneALigneEcheanceService;
	}

	public void setTaLigneALigneEcheanceService(ITaLigneALigneEcheanceServiceRemote taLigneALigneEcheanceService) {
		this.taLigneALigneEcheanceService = taLigneALigneEcheanceService;
	}

	public Boolean getDansDialogue() {
		return dansDialogue;
	}

	public void setDansDialogue(Boolean dansDialogue) {
		this.dansDialogue = dansDialogue;
	}


}
