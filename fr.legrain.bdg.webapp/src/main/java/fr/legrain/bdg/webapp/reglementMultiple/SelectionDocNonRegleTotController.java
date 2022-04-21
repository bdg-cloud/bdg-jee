package fr.legrain.bdg.webapp.reglementMultiple;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import fr.legrain.article.dto.TaUniteDTO;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.AutorisationBean;
import fr.legrain.bdg.webapp.documents.OuvertureDocumentBean;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.document.dto.IDocumentDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.dto.TaRDocumentDTO;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaTiers;


@Named
@ViewScoped 
public class SelectionDocNonRegleTotController extends AbstractController{

	@Inject @Named(value="autorisationBean")
	private AutorisationBean autorisation;

	@Inject @Named(value="ouvertureDocumentBean")
	private OuvertureDocumentBean ouvertureDocumentBean;


	private final String C_MESS_SELECTION_VIDE = "Aucun document n'a été sélectionné !!!";
	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
	private IDocumentTiers detailLigne;
	private TaTiers detailLigneTiers;
	private Date dateDeb=null;
	private Date dateFin=null;

	private String libelle="";
	private TaTiersDTO taTiersDTO;
	private DocumentDTO taFactureDTO;
	private IDocumentTiers document = null;
	private String codeDocument="";
	private String codeTiers="";

	@EJB private ITaTiersServiceRemote taTiersService;
	@EJB private ITaFactureServiceRemote taFactureService;
	@EJB private ITaInfoEntrepriseServiceRemote daoInfos;

	
	TaInfoEntreprise infos =null;
    
	private List<TaFactureDTO> listeDocumentDTO;
	private IDocumentDTO selectedDocument;
	private TaTPaiement taTPaiement=null;




	@PostConstruct
	public void init() {


		infos=daoInfos.findInstance();
		
		dateDeb=infos.getDatedebInfoEntreprise();
		dateFin=infos.getDatefinInfoEntreprise();





	}


	public void actFermer(ActionEvent e) {

	}
	public void actAnnuler(ActionEvent e) {

	}

	public void actDialogGenerationReglement(ActionEvent actionEvent) {

		Map<String,Object> options = new HashMap<String, Object>();
		
        options.put("modal", true);
        options.put("draggable", false);
        options.put("closable", false);
        options.put("resizable", true);
        options.put("contentHeight", 850);
        options.put("contentWidth", 1000);
        options.put("width", 1024);

		Map<String,Object> mapDialogue = new HashMap<String,Object>();
		

		mapDialogue.put("masterEntity",selectedDocument );
		
		Map<String,List<String>> params = new HashMap<String,List<String>>();
		List<String> list = new ArrayList<String>();
		list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
		params.put("modeEcranDefaut", list);
		
		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		sessionMap.put("mapDialogue", mapDialogue);


		
		PrimeFaces.current().dialog().openDynamic("reglement/multiple/reglement_multiple_facture.xhtml", options, params);
		
	}
	

	
	public void handleReturnDialogGenerationDocument(SelectEvent event) {

		FacesContext context = FacesContext.getCurrentInstance();
		
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Selection vide",C_MESS_SELECTION_VIDE)); 	
	
				
	}

//	
	
	public void actEnregistrer(ActionEvent e) {
		FacesContext context = FacesContext.getCurrentInstance();


	}

	public void actRefresh(ActionEvent e) {
		actRefresh();
	}





	public void actRefresh() {
		try{
			codeTiers="%";
			codeDocument="%";
			if(taTiersDTO!=null)codeTiers=taTiersDTO.getCodeTiers();
			if(taFactureDTO!=null){
				codeDocument=taFactureDTO.getCodeDocument();
				codeTiers="";
			}
			
			//afficher toutes factures non réglées pour le tiers sélectionné ou tous les tiers
			listeDocumentDTO= taFactureService.rechercheDocumentNonTotalementRegle(dateDeb,
					dateFin,codeTiers,codeDocument);

			if(listeDocumentDTO!=null && !listeDocumentDTO.isEmpty()){
				selectedDocument=listeDocumentDTO.get(0);
			}
		}catch (Exception e1) {

		}
		finally{

		}
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



	
	public class TaDocComparator implements Comparator<IDocumentDTO> {

		public int compare(IDocumentDTO doc1, IDocumentDTO doc2) {

			int premier = doc1.getCodeTiers().compareTo(doc2.getCodeTiers());

			int deuxieme = doc1.getDateDocument().compareTo(doc2.getDateDocument());

			int compared = premier;
			if (compared == 0) {
				compared = deuxieme;
			}

			return compared;
		}
	}





	





	


	public AutorisationBean getAutorisation() {
		return autorisation;
	}

	public void setAutorisation(AutorisationBean autorisation) {
		this.autorisation = autorisation;
	}



	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}



	public IDocumentTiers getDocument() {
		return document;
	}

	public void setDocument(IDocumentTiers document) {
		this.document = document;
	}



	public TaTPaiement getTaTPaiement() {
		return taTPaiement;
	}

	public void setTaTPaiement(TaTPaiement taTPaiement) {
		this.taTPaiement = taTPaiement;
	}


	public OuvertureDocumentBean getOuvertureDocumentBean() {
		return ouvertureDocumentBean;
	}

	public void setOuvertureDocumentBean(OuvertureDocumentBean ouvertureDocumentBean) {
		this.ouvertureDocumentBean = ouvertureDocumentBean;
	}

	public boolean etatBouton(String bouton) {
		boolean retour = true;
		switch (modeEcran.getMode()) {
		case C_MO_INSERTION:
			switch (bouton) {
				case "annuler":
				case "enregistrer":
				case "fermer":
					retour = false;
					break;
				default:
					break;
			}
			break;
		case C_MO_EDITION:
			switch (bouton) {
			case "annuler":
			case "enregistrer":
			case "fermer":
				retour = false;
				break;
			default:
				break;
			}
			break;
		case C_MO_CONSULTATION:
			switch (bouton) {
			case "inserer":
			case "fermer":
				retour = false;
			case "modifier":
			case "supprimer":
			case "imprimer":
				retour = false;
				break;				
			default:
				break;
		}
			break;
		default:
			break;
		}
		
		return retour;

	}

	public ModeObjetEcranServeur getModeEcran() {
		return modeEcran;
	}

	public void setModeEcran(ModeObjetEcranServeur modeEcran) {
		this.modeEcran = modeEcran;
	}




	public String getCodeDocument() {
		return codeDocument;
	}

	public void setCodeDocument(String codeDocument) {
		this.codeDocument = codeDocument;
	}
	
	

	public TaTiers getDetailLigneTiers() {
		return detailLigneTiers;
	}


	public void setDetailLigneTiers(TaTiers detailLigneTiers) {
		this.detailLigneTiers = detailLigneTiers;
	}

public TaTiers recupCodetiers(String code){
	FacesContext context = FacesContext.getCurrentInstance();
	if(code!=null && !code.isEmpty())
		try {
			return taTiersService.findByCode(code);
		} catch (FinderException e) {
			context.addMessage(null, new FacesMessage("erreur", "code tiers vide")); 	
		}
	return null;
}

public IDocumentTiers recupCodeDocument(String code){
	FacesContext context = FacesContext.getCurrentInstance();
	if(code!=null && !code.isEmpty())
		return documentValide(code);
	return null;
}

private IDocumentTiers documentValide(String code){
	try {	
			document=taFactureService.findByCode(code);
		return document;
	} catch (FinderException e) {
		return null;
	}
}


	public IDocumentTiers getDetailLigne() {
		return detailLigne;
	}


	public void setDetailLigne(IDocumentTiers detailLigne) {
		this.detailLigne = detailLigne;
	}

		

	public boolean ImageEstPasVide(String pathImage){
		return ( pathImage!=null && !pathImage.isEmpty() );
	}


	public List<TaFactureDTO> getListeDocumentDTO() {
		return listeDocumentDTO;
	}


	public void setListeDocumentDTO(List<TaFactureDTO> listeDocumentDTO) {
		this.listeDocumentDTO = listeDocumentDTO;
	}


	public IDocumentDTO getSelectedDocument() {
		return selectedDocument;
	}


	public void setSelectedDocument(IDocumentDTO selectedDocument) {
		this.selectedDocument = selectedDocument;
	}

	

	public void handleReturnDialogGestionReglement(SelectEvent event) {
		int idCourant=selectedDocument.getId();
		actRefresh();
		if(idCourant!=0 && !listeDocumentDTO.isEmpty()){
			for (TaFactureDTO taFactureDTO : listeDocumentDTO) {
				if(idCourant==taFactureDTO.getId())
					selectedDocument=taFactureDTO;
			}
		}

	}


	public TaTiersDTO getTaTiersDTO() {
		return taTiersDTO;
	}


	public void setTaTiersDTO(TaTiersDTO taTiersDTO) {
		this.taTiersDTO = taTiersDTO;
	}


	public DocumentDTO getTaFactureDTO() {
		return taFactureDTO;
	}


	public void setTaFactureDTO(DocumentDTO taFactureDTO) {
		this.taFactureDTO = taFactureDTO;
	}


	public List<TaTiersDTO> tiersAutoCompleteDTOLight(String query) {
		List<TaTiersDTO> allValues = taTiersService.findByCodeLight("*");
		List<TaTiersDTO> filteredValues = new ArrayList<TaTiersDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			TaTiersDTO civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeTiers().toLowerCase().contains(query.toLowerCase())
					|| civ.getNomTiers().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	
	public List<DocumentDTO> factureAutoCompleteDTOLight(String query) {
		String code;
		if(taTiersDTO==null ||taTiersDTO.getCodeTiers().isEmpty())code="%";
		else code=taTiersDTO.getCodeTiers();
		List<DocumentDTO> allValues = taFactureService.findAllDTOPeriode(dateDeb,dateFin,code);
		List<DocumentDTO> filteredValues = new ArrayList<DocumentDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			DocumentDTO civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeDocument().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	
	public void autcompleteSelection(SelectEvent event) {
		Object value = event.getObject();
		//		FacesMessage msg = new FacesMessage("Selected", "Item:" + value);

		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");
		System.out.println("FactureController.autcompleteSelection() : "+nomChamp);


		if(value!=null) {
			if(value instanceof String && value.equals(Const.C_AUCUN))value="";	
			if(value instanceof TaTiersDTO && ((TaTiersDTO) value).getCodeTiers()!=null ) {
				taFactureDTO=null;
			};	
		}
//		validateUIField(nomChamp,value);
	}


	public String getCodeTiers() {
		return codeTiers;
	}


	public void setCodeTiers(String codeTiers) {
		this.codeTiers = codeTiers;
	}

	public void onClearDocument(AjaxBehaviorEvent event){
	       taFactureDTO=null;
		}
	public void onClearTiers(AjaxBehaviorEvent event){
       taTiersDTO=null;
       taFactureDTO=null;
	}

}