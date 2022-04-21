package fr.legrain.bdg.webapp.documents;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
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
import org.primefaces.event.SelectEvent;

import fr.legrain.article.model.TaRefArticleFournisseur;
import fr.legrain.bdg.documents.service.remote.ITaAcompteServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAvoirServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.document.dto.IDocumentDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.TaLigneALigneDTO;
import fr.legrain.document.dto.TaLigneALigneEcheanceDTO;
import fr.legrain.document.model.TaAbonnement;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.lib.data.EnumModeObjet;

@Named
@ViewScoped
public class DetailLiaisonDocumentController extends AbstractController {
		//extends AbstractDocumentController<TaFacture, IDocumentDTO , TaLFacture, ILigneDocumentDTO, ILigneDocumentJSF, TaInfosFacture > {
	
	private static final String C_DIALOG = "dialog";
	private String titlePartOne="Détail des liaisons pour le document : ";
	private String titlePartTwo="";
	
	private Date dateDeb=null;
	private Date dateFin=null;
	private IDocumentDTO masterEntityDTO;
	private IDocumentTiers masterEntity;
	
	private TaLigneALigneDTO selectedLigneALigneDTO;
	
	TaInfoEntreprise infos =null;
	private @EJB  ITaInfoEntrepriseServiceRemote daoInfos;
	private @EJB ITaPreferencesServiceRemote taPreferencesService;
	private @EJB ITaFactureServiceRemote taFactureService;
	private @EJB ITaAvoirServiceRemote taAvoirService;
	private @EJB ITaAcompteServiceRemote taAcompteService;
	
	@Inject @Named(value="duplicationDocumentBean")
	private DuplicationDocumentBean duplicationDocumentBean;
	
	
	private List<TaLigneALigneDTO> ligneLie;

	private Boolean readOnly=false;
	List<TaLigneALigneDTO> ligneLieeFils;
	List<TaLigneALigneDTO> ligneLieeMere; 
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

			infos=daoInfos.findInstance();

			dateDeb=infos.getDatedebInfoEntreprise();
			dateFin=infos.getDatefinInfoEntreprise();

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();

			if(sessionMap.get("mapDialogue")!=null) {
				Map<String,Object>	mapDialogue = (Map<String, Object>) sessionMap.get("mapDialogue");
				if(mapDialogue!=null && mapDialogue.size()>0){
					if(mapDialogue.get("masterEntityDTO")!=null){
						masterEntityDTO=(IDocumentDTO) mapDialogue.get("masterEntityDTO");
//						if(mapDialogue.get("masterEntityDTO") instanceof TaFactureDTO){
//							masterEntityDTO=(TaFactureDTO) mapDialogue.get("masterEntityDTO");
//						}
//						if(mapDialogue.get("masterEntityDTO") instanceof TaAcompteDTO){
//							masterEntityDTO=(TaAcompteDTO) mapDialogue.get("masterEntityDTO");
//						}
//						if(mapDialogue.get("masterEntityDTO") instanceof TaApporteurDTO){
//							masterEntityDTO=(TaApporteurDTO) mapDialogue.get("masterEntityDTO");
//						}
//						if(mapDialogue.get("masterEntityDTO") instanceof TaAvoirDTO){
//							masterEntityDTO=(TaAvoirDTO) mapDialogue.get("masterEntityDTO");
//						}
//						if(mapDialogue.get("masterEntityDTO") instanceof TaAvisEcheanceDTO){
//							TaAvisEcheanceDTO fac=(TaAvisEcheanceDTO) mapDialogue.get("masterEntityDTO");
//						}
//						if(mapDialogue.get("masterEntityDTO") instanceof TaDevisDTO){
//							TaDevisDTO fac=(TaDevisDTO) mapDialogue.get("masterEntityDTO");
//						}
//						if(mapDialogue.get("masterEntityDTO") instanceof TaBonlivDTO){
//							TaBonlivDTO fac=(TaBonlivDTO) mapDialogue.get("masterEntityDTO");
//						}
//						if(mapDialogue.get("masterEntityDTO") instanceof TaBoncdeDTO){
//							TaBoncdeDTO fac=(TaBoncdeDTO) mapDialogue.get("masterEntityDTO");
//						}
//						if(mapDialogue.get("masterEntityDTO") instanceof TaBoncdeAchatDTO){
//							TaProformaDTO fac=(TaProformaDTO) mapDialogue.get("masterEntityDTO");
//						}
//						if(mapDialogue.get("masterEntityDTO") instanceof TaProformaDTO){
//							TaProformaDTO fac=(TaProformaDTO) mapDialogue.get("masterEntityDTO");
//						}
//						if(mapDialogue.get("masterEntityDTO") instanceof TaPrelevementDTO){
//							TaPrelevementDTO fac=(TaPrelevementDTO) mapDialogue.get("masterEntityDTO");
//						}
//						if(mapDialogue.get("masterEntityDTO") instanceof TaBonReceptionDTO){
//							TaBonReceptionDTO fac=(TaBonReceptionDTO) mapDialogue.get("masterEntityDTO");
//						}
//						if(mapDialogue.get("masterEntityDTO") instanceof TaFabricationDTO){
//							TaFabricationDTO fac=(TaFabricationDTO) mapDialogue.get("masterEntityDTO");
//						}
//						if(mapDialogue.get("masterEntityDTO") instanceof TaTicketDeCaisseDTO){
//							TaTicketDeCaisseDTO fac=(TaTicketDeCaisseDTO) mapDialogue.get("masterEntityDTO");
//						}
					}
					if(mapDialogue.get("readOnly")!=null){
						readOnly=(Boolean) mapDialogue.get("readOnly");
					}
				}
				cleanAppelDialogue();
			}
			if(masterEntityDTO!=null)titlePartTwo=masterEntityDTO.getCodeDocument();
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
			if(masterEntityDTO!=null){
				List<Integer> listeLigne=new LinkedList<>();
				List<TaLigneALigneDTO> docLie=rechercheSiDocLieLigneALigne(masterEntityDTO);
				List<TaLigneALigneEcheanceDTO>  le=rechercheSiLigneEcheanceDocLie(masterEntityDTO);
				if(le!=null) {
					for (TaLigneALigneEcheanceDTO ligne : le) {
							if(getLigneAbonnement()==null)setLigneAbonnement(new LinkedList<>());
							getLigneAbonnement().add(ligne);
						}
				}
				if(docLie!=null) {
					for (TaLigneALigneDTO ligne : docLie) {
						if(ligne.getIdLigneSrc().equals(ligne.getIdLDocumentSrc())) {
							if(getLigneLieeFils()==null)setLigneLieeFils(new LinkedList<>());
							if(!listeLigne.contains(ligne.getId())) {
								getLigneLieeFils().add(ligne);
								listeLigne.add(ligne.getId());
							}
						}
						else { 
							if(getLigneLieeMere()==null)setLigneLieeMere(new LinkedList<>());
							if(!listeLigne.contains(ligne.getId())) {
								getLigneLieeMere().add(ligne);
								listeLigne.add(ligne.getId());
							}
						}							
					}
				}
				
			}
		
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

	public Date getDateDeb() {
		return dateDeb;
	}

	public void setDateDeb(Date dateDeb) {
		this.dateDeb = dateDeb;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	public IDocumentDTO getMasterEntityDTO() {
		return masterEntityDTO;
	}

	public void setMasterEntityDTO(IDocumentDTO masterEntityDTO) {
		this.masterEntityDTO = masterEntityDTO;
	}

	public IDocumentTiers getMasterEntity() {
		return masterEntity;
	}

	public void setMasterEntity(IDocumentTiers masterEntity) {
		this.masterEntity = masterEntity;
	}

	public TaInfoEntreprise getInfos() {
		return infos;
	}

	public void setInfos(TaInfoEntreprise infos) {
		this.infos = infos;
	}

	public DuplicationDocumentBean getDuplicationDocumentBean() {
		return duplicationDocumentBean;
	}

	public void setDuplicationDocumentBean(DuplicationDocumentBean duplicationDocumentBean) {
		this.duplicationDocumentBean = duplicationDocumentBean;
	}

	public List<TaLigneALigneDTO> getLigneLieeFils() {
		return ligneLieeFils;
	}

	public void setLigneLieeFils(List<TaLigneALigneDTO> ligneLieeFils) {
		this.ligneLieeFils = ligneLieeFils;
	}

	public List<TaLigneALigneDTO> getLigneLieeMere() {
		return ligneLieeMere;
	}

	public void setLigneLieeMere(List<TaLigneALigneDTO> ligneLieeMere) {
		this.ligneLieeMere = ligneLieeMere;
	}

	public TaLigneALigneDTO getSelectedLigneALigneDTO() {
		return selectedLigneALigneDTO;
	}

	public void setSelectedLigneALigneDTO(TaLigneALigneDTO selectedLigneALigneDTO) {
		this.selectedLigneALigneDTO = selectedLigneALigneDTO;
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


}
