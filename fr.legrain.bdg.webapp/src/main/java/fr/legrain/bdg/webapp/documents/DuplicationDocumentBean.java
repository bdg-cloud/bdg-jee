package fr.legrain.bdg.webapp.documents;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import fr.legrain.bdg.webapp.LgrTabEvent;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.model.RetourGenerationDoc;
import fr.legrain.generationDocument.model.ParamAfficheChoixGenerationDocument;

@Named
@ViewScoped
public class DuplicationDocumentBean implements Serializable {


	private static final long serialVersionUID = -7644781947083554599L;


//	@Inject @Named(value="autorisationBean")
//	private AutorisationBean autorisation;
//
	@Inject @Named(value="ouvertureDocumentBean")
	private OuvertureDocumentBean ouvertureDocumentBean;
	
	private String css;
	private IDocumentTiers doc;
	private boolean genereToutesLesLignes=false;
	
	@PostConstruct
	public void init() {
		
	}

	public void actDialogGenerationDocument() {

		
		Map<String,Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", true);
		options.put("resizable", false);
		options.put("contentHeight", 600);
		options.put("modal", true);

		Map<String,List<String>> params = new HashMap<String,List<String>>();
//		List<String> list = new ArrayList<String>();
//		list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
//		params.put("modeEcranDefaut", list);
		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		Map<String,ParamAfficheChoixGenerationDocument> mapEdition = new HashMap<String,ParamAfficheChoixGenerationDocument>();
		ParamAfficheChoixGenerationDocument paramGeneration=new ParamAfficheChoixGenerationDocument();
		paramGeneration.setCodeDocument(doc.getCodeDocument());
		paramGeneration.setTypeSrc(doc.getTypeDocument());
		paramGeneration.setDocumentSrc(doc);
		if(genereToutesLesLignes)
			for (ILigneDocumentTiers l : doc.getLignesGeneral()) {
				l.setGenereLigne(true);
				l.setQteGenere(l.getQteLDocument());
				l.setUniteGenere(l.getU1LDocument());
			}
//		paramGeneration.setDateDocument(masterEntity.getDateDocument());
		paramGeneration.setCodeTiers(doc.getTaTiers().getCodeTiers());
		paramGeneration.setGenerationModele(false);
		paramGeneration.setConserveNbDecimalesDoc(true);
		paramGeneration.setTitreFormulaire("Génération d'un document à partir du document : "+doc.getCodeDocument());
		sessionMap.put("generation", paramGeneration);
		
		PrimeFaces.current().dialog().openDynamic("generation/generation_documents_simple", options, params);
		
	}

	public void actDialogGenerationModeleDocument() {//ActionEvent actionEvent

		
		Map<String,Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", true);
		options.put("resizable", false);
		options.put("contentHeight", 700);
		options.put("contentWidth", 800);
		options.put("modal", true);

		Map<String,List<String>> params = new HashMap<String,List<String>>();
//		List<String> list = new ArrayList<String>();
//		list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
//		params.put("modeEcranDefaut", list);
		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		ParamAfficheChoixGenerationDocument paramGeneration=new ParamAfficheChoixGenerationDocument();
		paramGeneration.setCodeDocument(doc.getCodeDocument());
		paramGeneration.setTypeSrc(doc.getTypeDocument());
		paramGeneration.setDocumentSrc(doc);
//		paramGeneration.setDateDocument(masterEntity.getDateDocument());
		paramGeneration.setCodeTiers(doc.getTaTiers().getCodeTiers());
		paramGeneration.setGenerationModele(true);
		paramGeneration.setConserveNbDecimalesDoc(false);
		paramGeneration.setTitreFormulaire("Génération d'un modèle à partir du document : "+doc.getCodeDocument());
		sessionMap.put("generation", paramGeneration);
		
		PrimeFaces.current().dialog().openDynamic("generation/generation_documents_simple", options, params);
		
	}

	public void handleReturnDialogGenerationDocument(SelectEvent event) {
		css = null;
		IDocumentTiers docCree = null; 
		LgrTabEvent retourEvent;
		FacesContext context = FacesContext.getCurrentInstance();
		if(event!=null && event.getObject()!=null) {
			if(event.getObject() instanceof LgrTabEvent){
				retourEvent=(LgrTabEvent) event.getObject();
				css =retourEvent.getCssLgrTab();
				docCree=(IDocumentTiers) retourEvent.getData();
				
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Création document", "Le document '"+docCree.getCodeDocument()+"' à été généré correctement."));
			}else if(event.getObject() instanceof RetourGenerationDoc){
				RetourGenerationDoc retour=(RetourGenerationDoc) event.getObject();
				if(retour.getMessage()!=null && !retour.getMessage().isEmpty())
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Création document", retour.getMessage()));
				if(retour.isOuvrirDoc())ouvertureDocumentBean.detailDocument(retour.getDoc());
			}
		}
	}

	public void ouvreDocument(String valeurIdentifiant,String  tabEcran) {
//		if(valeurIdentifiant!=null ) {					
//			ouvertureDocumentBean.setEvent(new LgrTabEvent());
//			ouvertureDocumentBean.getEvent().setCodeObjet(valeurIdentifiant);
//			ouvertureDocumentBean.getEvent().setData(listeDocCrees.get(0));
//			ouvertureDocumentBean.getEvent().setCssLgrTab(tabEcran);
//			ouvertureDocumentBean.getEvent().setAfficheDoc(false);/* A remettre à true dès que l'on pourra afficher le document à la suite d'un dialogue*/
//			ouvertureDocumentBean.openDocument(null);
//		}
	}
	public IDocumentTiers getDoc() {
		return doc;
	}

	public void setDoc(IDocumentTiers doc) {
		this.doc = doc;
	}

	public String getCss() {
		return css;
	}

	public void setCss(String css) {
		this.css = css;
	}

	public OuvertureDocumentBean getOuvertureDocumentBean() {
		return ouvertureDocumentBean;
	}

	public void setOuvertureDocumentBean(OuvertureDocumentBean ouvertureDocumentBean) {
		this.ouvertureDocumentBean = ouvertureDocumentBean;
	}

//	public AutorisationBean getAutorisation() {
//		return autorisation;
//	}
//
//	public void setAutorisation(AutorisationBean autorisation) {
//		this.autorisation = autorisation;
//	}
//
//	public OuvertureDocumentBean getOuvertureDocumentBean() {
//		return ouvertureDocumentBean;
//	}
//
//	public void setOuvertureDocumentBean(OuvertureDocumentBean ouvertureDocumentBean) {
//		this.ouvertureDocumentBean = ouvertureDocumentBean;
//	}


	public void handleReturnDialogOuvertureDocumentAPartirDialog(SelectEvent event) {
		css = null;
		IDocumentTiers doc = null; 
		LgrTabEvent retourEvent;
		FacesContext context = FacesContext.getCurrentInstance();
		if(event!=null && event.getObject()!=null) {
			if(event.getObject() instanceof LgrTabEvent){
				retourEvent=(LgrTabEvent) event.getObject();
				css =retourEvent.getCssLgrTab();
				doc=(IDocumentTiers) retourEvent.getData();
				if(doc!=null)ouvertureDocumentBean.detailDocument(doc);				
			}
		}
	}

	public boolean getGenereToutesLesLignes() {
		return genereToutesLesLignes;
	}

	public void setGenereToutesLesLignes(boolean genereToutesLesLignes) {
		this.genereToutesLesLignes = genereToutesLesLignes;
	}

	

	
}
