package fr.legrain.bdg.webapp.generationDocument;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import fr.legrain.document.model.TaBonReception;
import fr.legrain.document.model.TaBoncdeAchat;
import fr.legrain.document.model.TypeDoc;
import fr.legrain.generationDocument.model.ParamAfficheChoixGenerationDocument;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibDate;


@Named
@ViewScoped 
public class GenerationLALBcaBrMultipleController extends GenerationLigneALigneMultipleController{


	
	@PostConstruct
	public void init() {
		super.init();
		selectedTypeSelection=TypeDoc.getInstance().getPathImageCouleurDoc().get(TaBoncdeAchat.TYPE_DOC);
		selectedTypeCreation=TypeDoc.getInstance().getPathImageCouleurDoc().get(TaBonReception.TYPE_DOC);
	}
	public void actDialogGenerationDocument(ActionEvent actionEvent) {

		
		Map<String,Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", true);
		options.put("resizable", false);
		options.put("contentHeight", 600);
		options.put("modal", true);

		Map<String,List<String>> params = new HashMap<String,List<String>>();
		List<String> list = new ArrayList<String>();
		list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
		params.put("modeEcranDefaut", list);
		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		Map<String,ParamAfficheChoixGenerationDocument> mapEdition = new HashMap<String,ParamAfficheChoixGenerationDocument>();
		ParamAfficheChoixGenerationDocument paramGeneration=new ParamAfficheChoixGenerationDocument();
		paramGeneration.setTypeSrc(TypeDoc.TYPE_BON_COMMANDE_ACHAT);
		paramGeneration.setTypeDest(TypeDoc.TYPE_BON_RECEPTION);
//		String libelle="Reprise de ";
		paramGeneration.setDateDocument(new Date());
		paramGeneration.setDateLivraison(new Date());
		paramGeneration.setLibelle(initialiseLibelleDoc(selectedTypeSelection.getName())
				+" du "+LibDate.dateToString(dateDeb)+" au "+
				LibDate.dateToString(dateFin));
		paramGeneration.setTiersModifiable(false);
		paramGeneration.setMultiple(true);
		

		paramGeneration.setTitreFormulaire("Génération multiple");
		sessionMap.put("generation", paramGeneration);
		
		PrimeFaces.current().dialog().openDynamic("generation/generation_documents_simple", options, params);
		
	}
}