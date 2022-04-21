package fr.legrain.bdg.webapp.app;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;

import org.primefaces.PrimeFaces;
import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.event.SelectEvent;

import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaCoefficientUnite;
import fr.legrain.bdg.article.service.remote.ITaCoefficientUniteServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaMouvementStockServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaUniteServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLigneALigneEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLigneALigneServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaRDocumentServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.webapp.agenda.AgendaBean;
import fr.legrain.bdg.webapp.agenda.EvenementParam;
import fr.legrain.bdg.webapp.documents.ILigneDocumentJSF;
import fr.legrain.document.dto.IDocumentDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentDTO;
import fr.legrain.document.dto.TaLigneALigneDTO;
import fr.legrain.document.dto.TaLigneALigneEcheanceDTO;
import fr.legrain.document.dto.TaRDocumentDTO;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaBonReception;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaBoncdeAchat;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaFlash;
import fr.legrain.document.model.TaLigneALigne;
import fr.legrain.document.model.TaLigneALigneEcheance;
import fr.legrain.document.model.TaPanier;
import fr.legrain.document.model.TaPrelevement;
import fr.legrain.document.model.TaPreparation;
import fr.legrain.document.model.TaProforma;
import fr.legrain.document.model.TaRDocument;
import fr.legrain.document.model.TaTicketDeCaisse;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.email.model.TaMessageEmail;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.paiement.service.LgrEnvironnementServeur;
import fr.legrain.push.model.TaMessagePush;
import fr.legrain.tiers.model.TaTiers;

public abstract class AbstractController implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -163763502981910975L;

	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
	
	private @EJB LgrEnvironnementServeur lgrEnvironnementServeur;
	protected  @EJB ITaRDocumentServiceRemote taRDocumentService;
	protected  @EJB ITaLigneALigneServiceRemote taLigneALigneService;
	protected  @EJB ITaLigneALigneEcheanceServiceRemote taLigneALigneEcheanceService;
	protected @EJB ITaPreferencesServiceRemote taPreferencesService;
	protected @EJB ITaMouvementStockServiceRemote taMouvementStockService;
	protected @EJB ITaCoefficientUniteServiceRemote taCoefficientUniteService;
	protected @EJB ITaUniteServiceRemote taUniteService;
	protected @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	
	protected String nomDocument;
	

	protected String queryArticle = null;
	protected String queryTiers = null;
	protected boolean rechercheAvecCommentaire=false;
	
	@Inject protected AutorisationBean autorisationBean;
	protected boolean gestionCapsules = true;

	public void scrollToTop() {
//		RequestContext requestContext = RequestContext.getCurrentInstance();
		PrimeFaces.current().executeScript("window.scrollTo(0,0);");
	}
	

	public static String customFormatDate(Date date) {
		return LibDate.customFormatDate(date);
	}
	
	public static String customFormatDate(LocalDate date) {
		if(date==null)date=LibDate.dateToLocalDate(new Date());
		return LibDate.customFormatDate(LibDate.localDateToDate(date));
	}
	
public void actDialogEvenement(EvenementParam param) {
	    
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentHeight", 640);
        options.put("modal", true);
        
        AgendaBean.initAppelDialogue();
        
        //Map<String,List<String>> params = null;
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
        params.put("modeEcranDefaut", list);
        
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		
		//String numLot =  (String) actionEvent.getComponent().getAttributes().get("idEvenement");
//		EvenementParam param = new EvenementParam();
//		param.setListeTiers(new ArrayList<>());
//		param.getListeTiers().add(masterEntity.getTaTiers());
//		param.setListeDocuments(new ArrayList<>());
//		param.getListeDocuments().add(masterEntity);
		sessionMap.put(EvenementParam.NOM_OBJET_EN_SESSION, param);

		PrimeFaces.current().dialog().openDynamic("agenda/dialog_evenement", options, params);
		
//		FacesContext context = FacesContext.getCurrentInstance();  
//		context.addMessage(null, new FacesMessage("Tiers", "actAbout")); 	    
	}

public void actDialogEmail(EmailParam email) {
	 Map<String,Object> options = new HashMap<String, Object>();
	    options.put("modal", true);
	    options.put("draggable", false);
	    options.put("closable", false);
	    options.put("resizable", true);
	    options.put("contentHeight", 710);
	    options.put("contentWidth", "100%");
	    //options.put("contentHeight", "100%");
	    options.put("width", 1024);
	    
	    Map<String,List<String>> params = new HashMap<String,List<String>>();
	    List<String> list = new ArrayList<String>();
	    list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
	    params.put("modeEcranDefaut", list);
	    
	    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		

		sessionMap.put(EmailParam.NOM_OBJET_EN_SESSION, email);
	    
		PrimeFaces.current().dialog().openDynamic("/dialog_email", options, params);
		
    
}





public void handleReturnDialogEmail(SelectEvent event) {
	if(event!=null && event.getObject()!=null) {
		TaMessageEmail j = (TaMessageEmail) event.getObject();
		
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Email", 
				"Email envoyée "
				)); 
	}
}

public void actDialogPush(PushParam push) {
    
    Map<String,Object> options = new HashMap<String, Object>();
    options.put("modal", true);
    options.put("draggable", false);
    options.put("closable", false);
    options.put("resizable", true);
    options.put("contentHeight", 710);
    options.put("contentWidth", "100%");
    //options.put("contentHeight", "100%");
    options.put("width", 1024);
    
    //Map<String,List<String>> params = null;
    Map<String,List<String>> params = new HashMap<String,List<String>>();
    List<String> list = new ArrayList<String>();
    list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
    params.put("modeEcranDefaut", list);
    
    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	Map<String, Object> sessionMap = externalContext.getSessionMap();
	
	sessionMap.put(PushParam.NOM_OBJET_EN_SESSION, push);
    
    PrimeFaces.current().dialog().openDynamic("/dialog_message_push", options, params);    
}

public void handleReturnDialogPush(SelectEvent event) {
	if(event!=null && event.getObject()!=null) {
		//List<ControleConformiteJSF> listeControle = (List<ControleConformiteJSF>) event.getObject();
		TaMessagePush j = (TaMessagePush) event.getObject();
		
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Message Push", 
				"Message push envoyé "
				)); 
	}
}

public String genereUrlDashboardStripe(String path, String id, boolean test) {
	
	//TODO vérifier le mode "test" par rapport aux clés Stripe réellement utilisé et non par rapport au serveur.
	//par rapport au serveur, fonctionne uniquement pour legrain, pas pour les dossiers clients.
	test = lgrEnvironnementServeur.isModeTestPaiementProgramme();
	
	String url = "https://dashboard.stripe.com/";
	if(test) {
		url += "test/";
	}
	url += path+"/";
	url += id;
	return url;
}



public ModeObjetEcranServeur getModeEcran() {
	return modeEcran;
}
public String getNomDocument() {
	return nomDocument;
}

public void setNomDocument(String nomDocument) {
	this.nomDocument = nomDocument;
}

public List<TaRDocumentDTO> dejaGenereDocument(IDocumentDTO ds ) {
    List<TaRDocumentDTO> dejaGenere = null;
    String requeteFixeRDocument = null;
    String codeSrc;
    int idSrc;
    String typeSrc;
    codeSrc=ds.getCodeDocument();
    idSrc=ds.getId();
    typeSrc=ds.getTypeDocument();
    if(ds.getTypeDocument().equals(TaAcompte.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taAcompte.idDocument="+ds.getId();
    if(ds.getTypeDocument().equals(TaAvoir.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taAvoir.idDocument="+ds.getId();
    if(ds.getTypeDocument().equals(TaAvisEcheance.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taAvisEcheance.idDocument="+ds.getId();
    if(ds.getTypeDocument().equals(TaApporteur.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taApporteur.idDocument="+ds.getId();
    if(ds.getTypeDocument().equals(TaBoncdeAchat.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taBoncdeAchat.idDocument="+ds.getId();
    if(ds.getTypeDocument().equals(TaBoncde.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taBoncde.idDocument="+ds.getId();
    if(ds.getTypeDocument().equals(TaBonReception.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taBonReception.idDocument="+ds.getId();
    if(ds.getTypeDocument().equals(TaBonliv.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taBonliv.idDocument="+ds.getId();
    if(ds.getTypeDocument().equals(TaDevis.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taDevis.idDocument="+ds.getId();
    if(ds.getTypeDocument().equals(TaFacture.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taFacture.idDocument="+ds.getId();
    if(ds.getTypeDocument().equals(TaPrelevement.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taPrelevement.idDocument="+ds.getId();
    if(ds.getTypeDocument().equals(TaProforma.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taProforma.idDocument="+ds.getId();
    if(ds.getTypeDocument().equals(TaPanier.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taPanier.idDocument="+ds.getId();
    if(ds.getTypeDocument().equals(TaTicketDeCaisse.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taTicketDeCaisse.idDocument="+ds.getId();
    if(ds.getTypeDocument().equals(TaPreparation.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taPreparation.idDocument="+ds.getId();
    String requeteDoc="";
    if(ds!=null && ds.getId()!=0 && requeteFixeRDocument != null) {
        String jpql = "select x "+requeteFixeRDocument;
        List<TaRDocument> l = taRDocumentService.dejaGenere(jpql);
        if(l!=null && l.size()>0)dejaGenere=new LinkedList<>();
        if(l== null) {
        	l = new LinkedList<>();
        }
        for (TaRDocument taRDocument : l) {
            if(taRDocument.getTaAcompte()!=null && !ds.getTypeDocument().equals(TaAcompte.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taAcompte.idDocument,x.taAcompte.codeDocument,'"+TaAcompte.TYPE_DOC+"',x.idSrc) ";
            if(taRDocument.getTaAvoir()!=null && !ds.getTypeDocument().equals(TaAvoir.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taAvoir.idDocument,x.taAvoir.codeDocument,'"+TaAvoir.TYPE_DOC+"',x.idSrc) ";
            if(taRDocument.getTaAvisEcheance()!=null && !ds.getTypeDocument().equals(TaAvisEcheance.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taAvisEcheance.idDocument,x.taAvisEcheance.codeDocument,'"+TaAvisEcheance.TYPE_DOC+"',x.idSrc) ";
            if(taRDocument.getTaApporteur()!=null && !ds.getTypeDocument().equals(TaApporteur.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taApporteur.idDocument,x.taApporteur.codeDocument,'"+TaApporteur.TYPE_DOC+"',x.idSrc) ";
            if(taRDocument.getTaBoncde()!=null && !ds.getTypeDocument().equals(TaBoncde.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taBoncde.idDocument,x.taBoncde.codeDocument,'"+TaBoncde.TYPE_DOC+"',x.idSrc) ";
            if(taRDocument.getTaBoncdeAchat()!=null && !ds.getTypeDocument().equals(TaBoncdeAchat.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taBoncdeAchat.idDocument,x.taBoncdeAchat.codeDocument,'"+TaBoncdeAchat.TYPE_DOC+"',x.idSrc) ";
            if(taRDocument.getTaBonliv()!=null && !ds.getTypeDocument().equals(TaBonliv.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taBonliv.idDocument,x.taBonliv.codeDocument,'"+TaBonliv.TYPE_DOC+"',x.idSrc) ";
            if(taRDocument.getTaBonReception()!=null && !ds.getTypeDocument().equals(TaBonReception.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taBonReception.idDocument,x.taBonReception.codeDocument,'"+TaBonReception.TYPE_DOC+"',x.idSrc) ";
            if(taRDocument.getTaDevis()!=null && !ds.getTypeDocument().equals(TaDevis.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taDevis.idDocument,x.taDevis.codeDocument,'"+TaDevis.TYPE_DOC+"',x.idSrc) ";
            if(taRDocument.getTaFacture()!=null && !ds.getTypeDocument().equals(TaFacture.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taFacture.idDocument,x.taFacture.codeDocument,'"+TaFacture.TYPE_DOC+"',x.idSrc) ";
            if(taRDocument.getTaPrelevement()!=null && !ds.getTypeDocument().equals(TaPrelevement.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taPrelevement.idDocument,x.taPrelevement.codeDocument,'"+TaPrelevement.TYPE_DOC+"',x.idSrc) ";
            if(taRDocument.getTaProforma()!=null && !ds.getTypeDocument().equals(TaProforma.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taProforma.idDocument,x.taProforma.codeDocument,'"+TaProforma.TYPE_DOC+"',x.idSrc) ";
            if(taRDocument.getTaTicketDeCaisse()!=null && !ds.getTypeDocument().equals(TaTicketDeCaisse.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taTicketDeCaisse.idDocument,x.taTicketDeCaisse.codeDocument,'"+TaTicketDeCaisse.TYPE_DOC+"',x.idSrc) ";
            if(taRDocument.getTaPreparation()!=null && !ds.getTypeDocument().equals(TaPreparation.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taPreparation.idDocument,x.taPreparation.codeDocument,'"+TaPreparation.TYPE_DOC+"',x.idSrc) ";
            if(taRDocument.getTaPanier()!=null && !ds.getTypeDocument().equals(TaPanier.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taPanier.idDocument,x.taPanier.codeDocument,'"+TaPanier.TYPE_DOC+"',x.idSrc) ";
            List<TaRDocumentDTO> l2 = new LinkedList<>();
            if(requeteDoc!=null && !requeteDoc.equals(""))
                l2 =taRDocumentService.dejaGenereDocument(requeteDoc+requeteFixeRDocument);
            for (TaRDocumentDTO i : l2) {
            	if(i.getIdDocumentSrc().compareTo(ds.getId())==0) {
            		i.setCodeDocumentSrc(ds.getCodeDocument());
            		i.setTypeDocumentSrc(ds.getTypeDocument());
            	}else {
            		i.setCodeDocumentSrc(i.getCodeDocumentDest());
            		i.setTypeDocumentSrc(i.getTypeDocumentDest());
            		
            		i.setIdDocumentDest(ds.getId());
            		i.setCodeDocumentDest(ds.getCodeDocument());
            		i.setTypeDocumentDest(ds.getTypeDocument());
            	}
            	dejaGenere.add(i);
            }
        }
        System.err.println(l.size());
    }
    return dejaGenere;
}


public List<TaRDocumentDTO> dejaGenereDocument(IDocumentTiers ds ) {
	List<TaRDocumentDTO> dejaGenere = null;
	String requeteFixeRDocument = null;
	if(ds.getTypeDocument().equals(TaAcompte.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taAcompte.idDocument="+ds.getIdDocument();
	if(ds.getTypeDocument().equals(TaAvoir.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taAvoir.idDocument="+ds.getIdDocument();
	if(ds.getTypeDocument().equals(TaAvisEcheance.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taAvisEcheance.idDocument="+ds.getIdDocument();
	if(ds.getTypeDocument().equals(TaApporteur.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taApporteur.idDocument="+ds.getIdDocument();
	if(ds.getTypeDocument().equals(TaBoncde.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taBoncdeAchat.idDocument="+ds.getIdDocument();
	if(ds.getTypeDocument().equals(TaBoncdeAchat.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taBoncde.idDocument="+ds.getIdDocument();
	if(ds.getTypeDocument().equals(TaBonReception.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taBonReception.idDocument="+ds.getIdDocument();
	if(ds.getTypeDocument().equals(TaBonliv.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taBonliv.idDocument="+ds.getIdDocument();
	if(ds.getTypeDocument().equals(TaDevis.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taDevis.idDocument="+ds.getIdDocument();
	if(ds.getTypeDocument().equals(TaFacture.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taFacture.idDocument="+ds.getIdDocument();
	if(ds.getTypeDocument().equals(TaPrelevement.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taPrelevement.idDocument="+ds.getIdDocument();
	if(ds.getTypeDocument().equals(TaPanier.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taPanier.idDocument="+ds.getIdDocument();
	if(ds.getTypeDocument().equals(TaProforma.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taProforma.idDocument="+ds.getIdDocument();
	if(ds.getTypeDocument().equals(TaTicketDeCaisse.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taTicketDeCaisse.idDocument="+ds.getIdDocument();
	String requeteDoc="";
	if(ds!=null && ds.getIdDocument()!=0) {
		String jpql = "select x "+requeteFixeRDocument;
		List<TaRDocument> l = taRDocumentService.dejaGenere(jpql);
		if(l!=null && l.size()>0)dejaGenere=new LinkedList<>();
		for (TaRDocument taRDocument : l) {
			if(taRDocument.getTaAcompte()!=null && !ds.getTypeDocument().equals(TaAcompte.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taAcompte.idDocument,x.taAcompte.codeDocument,'"+TaAcompte.TYPE_DOC+"') ";
			if(taRDocument.getTaAvoir()!=null && !ds.getTypeDocument().equals(TaAvoir.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taAvoir.idDocument,x.taAvoir.codeDocument,'"+TaAvoir.TYPE_DOC+"') ";
			if(taRDocument.getTaAvisEcheance()!=null && !ds.getTypeDocument().equals(TaAvisEcheance.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taAvisEcheance.idDocument,x.taAvisEcheance.codeDocument,'"+TaAvisEcheance.TYPE_DOC+"') ";
			if(taRDocument.getTaApporteur()!=null && !ds.getTypeDocument().equals(TaApporteur.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taApporteur.idDocument,x.taApporteur.codeDocument,'"+TaApporteur.TYPE_DOC+"') ";
			if(taRDocument.getTaBoncde()!=null && !ds.getTypeDocument().equals(TaBoncde.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taBoncde.idDocument,x.taBoncde.codeDocument,'"+TaBoncde.TYPE_DOC+"') ";
			if(taRDocument.getTaBoncdeAchat()!=null && !ds.getTypeDocument().equals(TaBoncdeAchat.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taBoncdeAchat.idDocument,x.taBoncdeAchat.codeDocument,'"+TaBoncdeAchat.TYPE_DOC+"') ";
			if(taRDocument.getTaBonliv()!=null && !ds.getTypeDocument().equals(TaBonliv.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taBonliv.idDocument,x.taBonliv.codeDocument,'"+TaBonliv.TYPE_DOC+"') ";
			if(taRDocument.getTaBonReception()!=null && !ds.getTypeDocument().equals(TaBonReception.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taBonReception.idDocument,x.taBonReception.codeDocument,'"+TaBonReception.TYPE_DOC+"') ";
			if(taRDocument.getTaDevis()!=null && !ds.getTypeDocument().equals(TaDevis.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taDevis.idDocument,x.taDevis.codeDocument,'"+TaDevis.TYPE_DOC+"') ";
			if(taRDocument.getTaFacture()!=null && !ds.getTypeDocument().equals(TaFacture.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taFacture.idDocument,x.taFacture.codeDocument,'"+TaFacture.TYPE_DOC+"') ";
			if(taRDocument.getTaPrelevement()!=null && !ds.getTypeDocument().equals(TaPrelevement.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taPrelevement.idDocument,x.taPrelevement.codeDocument,'"+TaPrelevement.TYPE_DOC+"') ";
			if(taRDocument.getTaProforma()!=null && !ds.getTypeDocument().equals(TaProforma.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taProforma.idDocument,x.taProforma.codeDocument,'"+TaProforma.TYPE_DOC+"') ";
			if(taRDocument.getTaPanier()!=null && !ds.getTypeDocument().equals(TaPanier.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taPanier.idDocument,x.taPanier.codeDocument,'"+TaPanier.TYPE_DOC+"') ";
			if(taRDocument.getTaTicketDeCaisse()!=null && !ds.getTypeDocument().equals(TaTicketDeCaisse.TYPE_DOC))requeteDoc="select new fr.legrain.document.dto.TaRDocumentDTO(x.taTicketDeCaisse.idDocument,x.taTicketDeCaisse.codeDocument,'"+TaTicketDeCaisse.TYPE_DOC+"') ";
			List<TaRDocumentDTO> l2 = new LinkedList<>();
			if(requeteDoc!=null && !requeteDoc.equals(""))
				l2 =taRDocumentService.dejaGenereDocument(requeteDoc+requeteFixeRDocument);
			for (TaRDocumentDTO iDocumentTiers : l2) {
				dejaGenere.add(iDocumentTiers);
			}
		}
		System.err.println(l.size());
	}
	return dejaGenere;
}


public List<TaLigneALigneDTO> dejaGenereLDocument(ILigneDocumentDTO ds ) {
    List<TaLigneALigneDTO> dejaGenere = null;
    String requeteFixeRDocument = null;
    String requeteWhereSupp = null;
    if(ds.getTypeDocument().equals(TaAcompte.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLAcompte l join l.taDocument d  left join l.taArticle art  left join l.taREtatLigneDocument le join le.taEtat et  where l.idLDocument="+ds.getIdLDocument();
    if(ds.getTypeDocument().equals(TaAvoir.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLAvoir l join l.taDocument d  left join l.taArticle art left join l.taREtatLigneDocument le  join le.taEtat et   where l.idLDocument="+ds.getIdLDocument();
    if(ds.getTypeDocument().equals(TaAvisEcheance.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLAvisEcheance l join l.taDocument d  left join l.taArticle art left join l.taREtatLigneDocument le  join le.taEtat et   where l.idLDocument="+ds.getIdLDocument();
    if(ds.getTypeDocument().equals(TaApporteur.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLApporteur l join l.taDocument d  left join l.taArticle art left join l.taREtatLigneDocument le  join le.taEtat et   where l.idLDocument="+ds.getIdLDocument();
    if(ds.getTypeDocument().equals(TaBoncde.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLBoncde l join l.taDocument d left join l.taArticle art left join l.taREtatLigneDocument le  join le.taEtat et  where l.idLDocument="+ds.getIdLDocument();
    if(ds.getTypeDocument().equals(TaBoncdeAchat.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLBoncdeAchat l join l.taDocument d left join l.taArticle art left join l.taREtatLigneDocument le  join le.taEtat et   where l.idLDocument="+ds.getIdLDocument();
    if(ds.getTypeDocument().equals(TaBonReception.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLBonReception l join l.taDocument d left join l.taArticle art left join l.taREtatLigneDocument le  join le.taEtat et  where l.idLDocument="+ds.getIdLDocument();
    if(ds.getTypeDocument().equals(TaBonliv.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLBonliv l join l.taDocument d left join l.taArticle art left join l.taREtatLigneDocument le  join le.taEtat et  where l.idLDocument="+ds.getIdLDocument();
    if(ds.getTypeDocument().equals(TaDevis.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLDevis l join l.taDocument d left join l.taArticle art left join l.taREtatLigneDocument le  join le.taEtat et  where l.idLDocument="+ds.getIdLDocument();
    if(ds.getTypeDocument().equals(TaFacture.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLFacture l join l.taDocument d left join l.taArticle art left join l.taREtatLigneDocument le  join le.taEtat et  where l.idLDocument="+ds.getIdLDocument();
    if(ds.getTypeDocument().equals(TaPrelevement.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLPrelevement l join l.taDocument d left join l.taArticle art left join l.taREtatLigneDocument le  join le.taEtat et   where l.idLDocument="+ds.getIdLDocument();
    if(ds.getTypeDocument().equals(TaProforma.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLProforma l join l.taDocument d left join l.taArticle art left join l.taREtatLigneDocument le  join le.taEtat et  where l.idLDocument="+ds.getIdLDocument();
    if(ds.getTypeDocument().equals(TaTicketDeCaisse.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLTicketDeCaisse l join l.taDocument d left join l.taArticle art left join l.taREtatLigneDocument le  join le.taEtat et  where l.idLDocument="+ds.getIdLDocument();
    if(ds.getTypeDocument().equals(TaPreparation.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLPreparation l join l.taDocument d left join l.taArticle art left join l.taREtatLigneDocument le  join le.taEtat et  where l.idLDocument="+ds.getIdLDocument();
    if(ds.getTypeDocument().equals(TaFlash.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLFlash l join l.taFlash d left join l.taArticle art left join l.taREtatLigneDocument le  join le.taEtat et  where l.idLFlash="+ds.getIdLDocument();
    if(ds.getTypeDocument().equals(TaPanier.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLPanier l join l.taDocument d left join l.taArticle art left join l.taREtatLigneDocument le  join le.taEtat et  where l.idLDocument="+ds.getIdLDocument();

    String requeteDoc="";
    if(ds!=null && ds.getIdLDocument()!=null && ds.getIdLDocument()!=0) {
        String jpql = "select x "+requeteFixeRDocument;
        String nomEntity = null;
        String typeEntity = null;
        int idEntity = -1;
        List<TaLigneALigne> l = taLigneALigneService.dejaGenere(jpql);
        if(l!=null && l.size()>0)dejaGenere=new LinkedList<>();
        for (TaLigneALigne ligne : l) {
        	requeteWhereSupp="";
        	requeteDoc="";
            if(ligne.getTaLAcompte()!=null && !ds.getTypeDocument().equals(TaAcompte.TYPE_DOC)) {
                nomEntity="taLAcompte";
                typeEntity=TaAcompte.TYPE_DOC;
                idEntity=ligne.getTaLAcompte().getIdLDocument();
            }
            if(ligne.getTaLAvoir()!=null && !ds.getTypeDocument().equals(TaAvoir.TYPE_DOC)) {
                nomEntity="taLAvoir";
                typeEntity=TaAvoir.TYPE_DOC;
                idEntity=ligne.getTaLAvoir().getIdLDocument();
            }
            if(ligne.getTaLAvisEcheance()!=null && !ds.getTypeDocument().equals(TaAvisEcheance.TYPE_DOC)) {
                nomEntity="taLAvisEcheance";
                typeEntity=TaAvisEcheance.TYPE_DOC;
                idEntity=ligne.getTaLAvisEcheance().getIdLDocument();
            }
            if(ligne.getTaLApporteur()!=null && !ds.getTypeDocument().equals(TaApporteur.TYPE_DOC)) {
                nomEntity="taLApporteur";
                typeEntity=TaApporteur.TYPE_DOC;
                idEntity=ligne.getTaLApporteur().getIdLDocument();
            }
            if(ligne.getTaLBoncde()!=null && !ds.getTypeDocument().equals(TaBoncde.TYPE_DOC)) {
                nomEntity="taLBoncde";
                idEntity=ligne.getTaLBoncde().getIdLDocument();
                typeEntity=TaBoncde.TYPE_DOC;
            }
            if(ligne.getTaLBoncdeAchat()!=null && !ds.getTypeDocument().equals(TaBoncdeAchat.TYPE_DOC)) {
                nomEntity="taLBoncdeAchat";
                typeEntity=TaBoncdeAchat.TYPE_DOC;
                idEntity=ligne.getTaLBoncdeAchat().getIdLDocument();
            }
            if(ligne.getTaLBonliv()!=null && !ds.getTypeDocument().equals(TaBonliv.TYPE_DOC)) {
                nomEntity="taLBonliv";
                typeEntity=TaBonliv.TYPE_DOC;
                idEntity=ligne.getTaLBonliv().getIdLDocument();
            }
            if(ligne.getTaLBonReception()!=null && !ds.getTypeDocument().equals(TaBonReception.TYPE_DOC)){
                nomEntity="taLBonReception";
                typeEntity=TaBonReception.TYPE_DOC;
                idEntity=ligne.getTaLBonReception().getIdLDocument();
            }
            if(ligne.getTaLDevis()!=null && !ds.getTypeDocument().equals(TaDevis.TYPE_DOC)){
                nomEntity="taLDevis";
                typeEntity=TaDevis.TYPE_DOC;
                idEntity=ligne.getTaLDevis().getIdLDocument();
            }
            if(ligne.getTaLFacture()!=null && !ds.getTypeDocument().equals(TaFacture.TYPE_DOC)){
                nomEntity="taLFacture";
                typeEntity=TaFacture.TYPE_DOC;
                idEntity=ligne.getTaLFacture().getIdLDocument();
            }
            if(ligne.getTaLPrelevement()!=null && !ds.getTypeDocument().equals(TaPrelevement.TYPE_DOC)){
                nomEntity="taLPrelevement";
                typeEntity=TaPrelevement.TYPE_DOC;
                idEntity=ligne.getTaLPrelevement().getIdLDocument();
            }
            if(ligne.getTaLProforma()!=null && !ds.getTypeDocument().equals(TaProforma.TYPE_DOC)){
                nomEntity="taLProforma";
                typeEntity=TaProforma.TYPE_DOC;
                idEntity=ligne.getTaLProforma().getIdLDocument();
            }
            if(ligne.getTaLPanier()!=null && !ds.getTypeDocument().equals(TaPanier.TYPE_DOC)){
                nomEntity="taLPanier";
                typeEntity=TaPanier.TYPE_DOC;
                idEntity=ligne.getTaLPanier().getIdLDocument();
            }
           if(ligne.getTaLTicketDeCaisse()!=null && !ds.getTypeDocument().equals(TaTicketDeCaisse.TYPE_DOC)){
                nomEntity="taLTicketDeCaisse";
                typeEntity=TaTicketDeCaisse.TYPE_DOC;
                idEntity=ligne.getTaLTicketDeCaisse().getIdLDocument();
            }
            if(ligne.getTaLPreparation()!=null && !ds.getTypeDocument().equals(TaPreparation.TYPE_DOC)){
                nomEntity="taLPreparation";
                typeEntity=TaPreparation.TYPE_DOC;
                idEntity=ligne.getTaLPreparation().getIdLDocument();
                if(ds.getTypeDocument().equals(TaFlash.TYPE_DOC)) {
                    requeteWhereSupp=" and x."+nomEntity+".idLDocument="+idEntity;
                    requeteDoc="select new fr.legrain.document.dto.TaLigneALigneDTO(x.id,x.idLigneSrc,l.idLFlash,x."+nomEntity+".idLDocument,x."+nomEntity+".taDocument.idDocument,x."+nomEntity+".taDocument.codeDocument,art.codeArticle,x.numLot,x."+nomEntity+".libLDocument,x.qteRecue,et.codeEtat,'"+typeEntity+"') ";
                 }
            }            
            if(ligne.getTaLPanier()!=null && !ds.getTypeDocument().equals(TaPanier.TYPE_DOC)){
                nomEntity="taLPanier";
                typeEntity=TaPanier.TYPE_DOC;
                idEntity=ligne.getTaLPanier().getIdLDocument();
            }            
            if(ligne.getTaLFlash()!=null && !ds.getTypeDocument().equals(TaFlash.TYPE_DOC)){
                nomEntity="taLFlash";
                typeEntity=TaFlash.TYPE_DOC;
                idEntity=ligne.getTaLFlash().getIdLFlash();
                requeteWhereSupp=" and x."+nomEntity+".idLFlash="+idEntity;
                requeteDoc="select new fr.legrain.document.dto.TaLigneALigneDTO(x.id,x.idLigneSrc,l.idLDocument,x."+nomEntity+".idLFlash,x."+nomEntity+".taFlash.idFlash,x."+nomEntity+".taFlash.codeFlash,art.codeArticle,x.numLot,x."+nomEntity+".libLFlash,x.qteRecue,et.codeEtat,'"+typeEntity+"') ";
            }
            if(requeteWhereSupp==null || requeteWhereSupp.isEmpty()) {            
            requeteWhereSupp=" and x."+nomEntity+".idLDocument="+idEntity;
            requeteDoc="select new fr.legrain.document.dto.TaLigneALigneDTO(x.id,x.idLigneSrc,l.idLDocument,x."+nomEntity+".idLDocument,x."+nomEntity+".taDocument.idDocument,x."+nomEntity+".taDocument.codeDocument,art.codeArticle,x.numLot,x."+nomEntity+".libLDocument,x.qteRecue,et.codeEtat,'"+typeEntity+"') ";
            }
            List<TaLigneALigneDTO> l2 = new LinkedList<>();
            if(requeteDoc!=null && !requeteDoc.equals(""))
                l2 =taLigneALigneService.dejaGenereLigneDocument(requeteDoc+requeteFixeRDocument+requeteWhereSupp);
            for (TaLigneALigneDTO iLDocumentTiers : l2) {
                dejaGenere.add(iLDocumentTiers);
            }
        }
        System.err.println(l.size());
    }
    return dejaGenere;
}


public List<TaLigneALigneDTO> dejaGenereDocumentLigneALigne(IDocumentDTO ds ) {
    List<TaLigneALigneDTO> dejaGenere = null;
    String requeteFixeRDocument = null;
    String requeteWhereSupp = null;
    if(ds.getTypeDocument().equals(TaAcompte.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLAcompte l join l.taDocument d  left join l.taArticle art  left join l.taREtatLigneDocument le join le.taEtat et    where d.idDocument="+ds.getId();
    if(ds.getTypeDocument().equals(TaAvoir.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLAvoir l join l.taDocument d  left join l.taArticle art  left join l.taREtatLigneDocument le join le.taEtat et    where d.idDocument="+ds.getId();
    if(ds.getTypeDocument().equals(TaAvisEcheance.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLAvisEcheance l join l.taDocument d  left join l.taArticle art  left join l.taREtatLigneDocument le join le.taEtat et    where d.idDocument="+ds.getId();
    if(ds.getTypeDocument().equals(TaApporteur.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLApporteur l join l.taDocument d  left join l.taArticle art  left join l.taREtatLigneDocument le join le.taEtat et    where d.idDocument="+ds.getId();
    if(ds.getTypeDocument().equals(TaBoncde.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLBoncde l join l.taDocument d left join l.taArticle art  left join l.taREtatLigneDocument le join le.taEtat et   where d.idDocument="+ds.getId();
    if(ds.getTypeDocument().equals(TaBoncdeAchat.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLBoncdeAchat l join l.taDocument d left join l.taArticle art  left join l.taREtatLigneDocument le join le.taEtat et    where d.idDocument="+ds.getId();
    if(ds.getTypeDocument().equals(TaBonReception.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLBonReception l join l.taDocument d left join l.taArticle art  left join l.taREtatLigneDocument le join le.taEtat et   where d.idDocument="+ds.getId();
    if(ds.getTypeDocument().equals(TaBonliv.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLBonliv l join l.taDocument d left join l.taArticle art  left join l.taREtatLigneDocument le join le.taEtat et   where d.idDocument="+ds.getId();
    if(ds.getTypeDocument().equals(TaDevis.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLDevis l join l.taDocument d left join l.taArticle art  left join l.taREtatLigneDocument le join le.taEtat et   where d.idDocument="+ds.getId();
    if(ds.getTypeDocument().equals(TaFacture.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLFacture l join l.taDocument d left join l.taArticle art  left join l.taREtatLigneDocument le join le.taEtat et   where d.idDocument="+ds.getId();
    if(ds.getTypeDocument().equals(TaPrelevement.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLPrelevement l join l.taDocument d left join l.taArticle art  left join l.taREtatLigneDocument le join le.taEtat et    where d.idDocument="+ds.getId();
    if(ds.getTypeDocument().equals(TaProforma.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLProforma l join l.taDocument d left join l.taArticle art  left join l.taREtatLigneDocument le join le.taEtat et   where d.idDocument="+ds.getId();
    if(ds.getTypeDocument().equals(TaTicketDeCaisse.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLTicketDeCaisse l join l.taDocument d left join l.taArticle art  left join l.taREtatLigneDocument le join le.taEtat et   where d.idDocument="+ds.getId();
    if(ds.getTypeDocument().equals(TaPreparation.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLPreparation l join l.taDocument d left join l.taArticle art  left join l.taREtatLigneDocument le join le.taEtat et   where d.idDocument="+ds.getId();
    if(ds.getTypeDocument().equals(TaPanier.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLPanier l join l.taDocument d left join l.taArticle art  left join l.taREtatLigneDocument le join le.taEtat et   where d.idDocument="+ds.getId();
    if(ds.getTypeDocument().equals(TaFlash.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigne x join x.taLFlash l join l.taFlash d left join l.taArticle art where d.idFlash="+ds.getId();
    String requeteDoc="";
    if(ds!=null && ds.getId()!=0 && requeteFixeRDocument != null) {
        String jpql = "select x "+requeteFixeRDocument;
        List<TaLigneALigne> l = taLigneALigneService.dejaGenere(jpql);
        if(l!=null && l.size()>0)dejaGenere=new LinkedList<>();
        String nomEntity = null;
        String typeEntity = null;
        int idEntity = -1;
        for (TaLigneALigne ligne : l) {
        	requeteWhereSupp="";
        	requeteDoc="";
            if(ligne.getTaLAcompte()!=null && !ds.getTypeDocument().equals(TaAcompte.TYPE_DOC)) {
                nomEntity="taLAcompte";
                typeEntity=TaAcompte.TYPE_DOC;
                idEntity=ligne.getTaLAcompte().getIdLDocument();
            }
            if(ligne.getTaLAvoir()!=null && !ds.getTypeDocument().equals(TaAvoir.TYPE_DOC)) {
                nomEntity="taLAvoir";
                typeEntity=TaAvoir.TYPE_DOC;
                idEntity=ligne.getTaLAvoir().getIdLDocument();
            }
            if(ligne.getTaLAvisEcheance()!=null && !ds.getTypeDocument().equals(TaAvisEcheance.TYPE_DOC)) {
                nomEntity="taLAvisEcheance";
                typeEntity=TaAvisEcheance.TYPE_DOC;
                idEntity=ligne.getTaLAvisEcheance().getIdLDocument();
            }
            if(ligne.getTaLApporteur()!=null && !ds.getTypeDocument().equals(TaApporteur.TYPE_DOC)) {
                nomEntity="taLApporteur";
                typeEntity=TaApporteur.TYPE_DOC;
                idEntity=ligne.getTaLApporteur().getIdLDocument();
            }
            if(ligne.getTaLBoncde()!=null && !ds.getTypeDocument().equals(TaBoncde.TYPE_DOC)) {
                nomEntity="taLBoncde";
                idEntity=ligne.getTaLBoncde().getIdLDocument();
                typeEntity=TaBoncde.TYPE_DOC;
            }
            if(ligne.getTaLBoncdeAchat()!=null && !ds.getTypeDocument().equals(TaBoncdeAchat.TYPE_DOC)) {
                nomEntity="taLBoncdeAchat";
                typeEntity=TaBoncdeAchat.TYPE_DOC;
                idEntity=ligne.getTaLBoncdeAchat().getIdLDocument();
            }
            if(ligne.getTaLBonliv()!=null && !ds.getTypeDocument().equals(TaBonliv.TYPE_DOC)) {
                nomEntity="taLBonliv";
                typeEntity=TaBonliv.TYPE_DOC;
                idEntity=ligne.getTaLBonliv().getIdLDocument();
            }
            if(ligne.getTaLBonReception()!=null && !ds.getTypeDocument().equals(TaBonReception.TYPE_DOC)){
                nomEntity="taLBonReception";
                typeEntity=TaBonReception.TYPE_DOC;
                idEntity=ligne.getTaLBonReception().getIdLDocument();
            }
            if(ligne.getTaLDevis()!=null && !ds.getTypeDocument().equals(TaDevis.TYPE_DOC)){
                nomEntity="taLDevis";
                typeEntity=TaDevis.TYPE_DOC;
                idEntity=ligne.getTaLDevis().getIdLDocument();
            }
            if(ligne.getTaLFacture()!=null && !ds.getTypeDocument().equals(TaFacture.TYPE_DOC)){
                nomEntity="taLFacture";
                typeEntity=TaFacture.TYPE_DOC;
                idEntity=ligne.getTaLFacture().getIdLDocument();
            }
            if(ligne.getTaLPrelevement()!=null && !ds.getTypeDocument().equals(TaPrelevement.TYPE_DOC)){
                nomEntity="taLPrelevement";
                typeEntity=TaPrelevement.TYPE_DOC;
                idEntity=ligne.getTaLPrelevement().getIdLDocument();
            }
            if(ligne.getTaLProforma()!=null && !ds.getTypeDocument().equals(TaProforma.TYPE_DOC)){
                nomEntity="taLProforma";
                typeEntity=TaProforma.TYPE_DOC;
                idEntity=ligne.getTaLProforma().getIdLDocument();
            }
            if(ligne.getTaLPanier()!=null && !ds.getTypeDocument().equals(TaPanier.TYPE_DOC)){
                nomEntity="taLPanier";
                typeEntity=TaPanier.TYPE_DOC;
                idEntity=ligne.getTaLPanier().getIdLDocument();
            }
            if(ligne.getTaLTicketDeCaisse()!=null && !ds.getTypeDocument().equals(TaTicketDeCaisse.TYPE_DOC)){
                nomEntity="taLTicketDeCaisse";
                typeEntity=TaTicketDeCaisse.TYPE_DOC;
                idEntity=ligne.getTaLTicketDeCaisse().getIdLDocument();
            }
            if(ligne.getTaLPreparation()!=null && !ds.getTypeDocument().equals(TaPreparation.TYPE_DOC)){
                nomEntity="taLPreparation";
                typeEntity=TaPreparation.TYPE_DOC;
                idEntity=ligne.getTaLPreparation().getIdLDocument();
                if(ds.getTypeDocument().equals(TaFlash.TYPE_DOC)) {																																																					
                    requeteWhereSupp=" and x."+nomEntity+".idLDocument="+idEntity;
                    requeteDoc="select new fr.legrain.document.dto.TaLigneALigneDTO(x.id,x.idLigneSrc,l.idLFlash,x."+nomEntity+".idLDocument,x."+nomEntity+".taDocument.idDocument,x."+nomEntity+".taDocument.codeDocument,art.codeArticle,x.numLot,x."+nomEntity+".libLDocument,x.qteRecue,et.codeEtat,'"+typeEntity+"') ";
                }
            }
            if(ligne.getTaLFlash()!=null && !ds.getTypeDocument().equals(TaFlash.TYPE_DOC)){
                nomEntity="taLFlash";
                typeEntity=TaFlash.TYPE_DOC;
                idEntity=ligne.getTaLFlash().getIdLFlash();
                requeteWhereSupp=" and x."+nomEntity+".idLFlash="+idEntity;
                requeteDoc="select new fr.legrain.document.dto.TaLigneALigneDTO(x.id,x.idLigneSrc,l.idLDocument,x."+nomEntity+".idLFlash,x."+nomEntity+".taFlash.idFlash,x."+nomEntity+".taFlash.codeFlash,art.codeArticle,x.numLot,x."+nomEntity+".libLFlash,x.qteRecue,et.codeEtat,'"+typeEntity+"') ";
            }
            if(requeteWhereSupp==null || requeteWhereSupp.isEmpty()) {
            requeteWhereSupp=" and x."+nomEntity+".idLDocument="+idEntity;
            requeteDoc="select new fr.legrain.document.dto.TaLigneALigneDTO(x.id,x.idLigneSrc,l.idLDocument,x."+nomEntity+".idLDocument,x."+nomEntity+".taDocument.idDocument,x."+nomEntity+".taDocument.codeDocument,art.codeArticle,x.numLot,x."+nomEntity+".libLDocument,x.qteRecue,et.codeEtat,'"+typeEntity+"') ";
            }
            List<TaLigneALigneDTO> l2 = new LinkedList<>();
            if(requeteDoc!=null && !requeteDoc.equals(""))
                l2 =taLigneALigneService.dejaGenereLigneDocument(requeteDoc+requeteFixeRDocument+requeteWhereSupp);
            for (TaLigneALigneDTO ligneDoc : l2) {
                dejaGenere.add(ligneDoc);
            }
        }
        System.err.println(l.size());
    }
    return dejaGenere;
}

public List<TaLigneALigneEcheanceDTO> rechercheSiLigneEcheanceDocLie(IDocumentDTO ds) {
		return taLigneALigneEcheanceService.dejaGenereLEcheanceDocument(ds);
}

public List<TaRDocumentDTO> rechercheSiDocLie(IDocumentDTO ds) {
	return dejaGenereDocument(ds);
}

public List<TaLigneALigneDTO> rechercheSiDocLieLigneALigne(IDocumentDTO ds) {
	return dejaGenereDocumentLigneALigne(ds);
}

public List<TaLigneALigneDTO> rechercheSiLigneDocLie(ILigneDocumentJSF ligne) {
	if(ligne!=null && ligne.getDto()!=null)
		return dejaGenereLDocument(ligne.getDto());
	return null;
}


public void onMoreTextArticle(AjaxBehaviorEvent event) {
    AutoComplete ac = (AutoComplete) event.getSource();
    ac.setMaxResults(ac.getMaxResults() + taPreferencesService.nbMaxChargeListeArticle());
   if(queryArticle==null) {
	   queryArticle = "*";
   }
    PrimeFaces.current().executeScript("PF('"+ ac.getWidgetVar()+"').search('" + queryArticle+ "')");
}

public void onMoreTextTiers(AjaxBehaviorEvent event) {
    AutoComplete ac = (AutoComplete) event.getSource();
    ac.setMaxResults(ac.getMaxResults() + taPreferencesService.nbMaxChargeListeTiers());
   if(queryTiers==null) {
	   queryTiers = "*";
   }
    PrimeFaces.current().executeScript("PF('"+ ac.getWidgetVar()+"').search('" + queryTiers+ "')");
}

public ITaPreferencesServiceRemote getTaPreferencesService() {
	return taPreferencesService;
}

public boolean lotEstVirtuelEtAUneSeuleLigne(TaArticle article){
	if(article!=null && !article.getGestionLot()) {
		TaPreferences p = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_LOTS, ITaPreferencesServiceRemote.LOTS_AUTORISER_UTILISATION_LOTS_NON_CONFORME);
		Boolean utiliserLotNonConforme = null;
		if(p!=null && LibConversion.StringToBoolean(p.getValeur()) != null) {
			utiliserLotNonConforme = LibConversion.StringToBoolean(p.getValeur());
		}	
		Integer retour=taMouvementStockService.getNBEtatStockByArticleVirtuel(article, utiliserLotNonConforme);
		if(retour!=null)return retour<=1;
	}
	return false;
}

public boolean rechercheAvecCommentaire(boolean init) {
	if(init) {
		rechercheAvecCommentaire=false;

		TaPreferences p = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_RECHERCHE, ITaPreferencesServiceRemote.COMMENTAIRE_RECHERCHE_ARTICLE);
		if(p!=null && LibConversion.StringToBoolean(p.getValeur()) != null) {
			rechercheAvecCommentaire=LibConversion.StringToBoolean(p.getValeur());
		}
	}
	return rechercheAvecCommentaire;
}

//public List<TaLigneALigneEcheanceDTO> dejaGenereLEcheanceDocument(ILigneDocumentDTO ds ) {
//	List<TaLigneALigneEcheanceDTO> dejaGenere = null;
//	String requeteFixeRDocument = null;
//	String requeteWhereSupp = null;
//	if(ds.getTypeDocument().equals(TaAcompte.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLAcompte l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
//	if(ds.getTypeDocument().equals(TaAvoir.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLAvoir l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
//	if(ds.getTypeDocument().equals(TaAvisEcheance.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLAvisEcheance l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
//	if(ds.getTypeDocument().equals(TaApporteur.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLApporteur l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
//	if(ds.getTypeDocument().equals(TaBoncde.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLBoncde l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
//	if(ds.getTypeDocument().equals(TaBoncdeAchat.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLBoncdeAchat l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
//	if(ds.getTypeDocument().equals(TaBonReception.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLBonReception l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
//	if(ds.getTypeDocument().equals(TaBonliv.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLBonliv l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
//	if(ds.getTypeDocument().equals(TaDevis.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLDevis l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
//	if(ds.getTypeDocument().equals(TaFacture.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLFacture l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
//	if(ds.getTypeDocument().equals(TaPrelevement.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLPrelevement l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
//	if(ds.getTypeDocument().equals(TaProforma.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLProforma l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
//	if(ds.getTypeDocument().equals(TaTicketDeCaisse.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLTicketDeCaisse l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
//	if(ds.getTypeDocument().equals(TaPreparation.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLPreparation l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
//	if(ds.getTypeDocument().equals(TaFlash.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLFlash l join l.taFlash d  left join l.taArticle art    where l.idLFlash="+ds.getIdLDocument();
//	if(ds.getTypeDocument().equals(TaPanier.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLPanier l join l.taDocument d  left join l.taArticle art    where l.idLDocument="+ds.getIdLDocument();
//
//	if(requeteFixeRDocument!=null) {
//		String requeteDoc="";
//		if(ds!=null && ds.getIdLDocument()!=null && ds.getIdLDocument()!=0) {
//			String jpql = "select x "+requeteFixeRDocument;
//			String nomEntity = null;
//			String typeEntity = null;
//			int idEntity = -1;
//			List<TaLigneALigneEcheance> l = taLigneALigneEcheanceService.dejaGenere(jpql);
//			if(l!=null && l.size()>0)dejaGenere=new LinkedList<>();
//			for (TaLigneALigneEcheance ligne : l) {
//				requeteWhereSupp="";
//				requeteDoc="";
//
//
//
//
//				if(requeteWhereSupp==null || requeteWhereSupp.isEmpty()) {            
//					//            requeteWhereSupp=" and x."+nomEntity+".idLDocument="+idEntity;
//					requeteDoc="select new fr.legrain.document.dto.TaLigneALigneEcheanceDTO(x.id,x.idLigneSrc,l.idLDocument,x.taLEcheance.idLEcheance,x.taLEcheance.taAbonnement.idDocument,x.taLEcheance.taAbonnement.codeDocument,"
//							+ "x.taLEcheance.libLDocument,art.codeArticle,x.taLEcheance.debutPeriode,x.taLEcheance.finPeriode,x.taLEcheance.taLAbonnement.complement1,x.taLEcheance.taLAbonnement.complement2,x.taLEcheance.taLAbonnement.complement1) ";
//				}
//				List<TaLigneALigneEcheanceDTO> l2 = new LinkedList<>();
//				if(requeteDoc!=null && !requeteDoc.equals(""))
//					l2 =taLigneALigneEcheanceService.dejaGenereLigneDocument(requeteDoc+requeteFixeRDocument+requeteWhereSupp);
//				for (TaLigneALigneEcheanceDTO iLDocumentTiers : l2) {
//					dejaGenere.add(iLDocumentTiers);
//				}
//			}
//
//			System.err.println(l.size());
//		}
//	}
//	return dejaGenere;
//}
//public List<TaLigneALigneEcheanceDTO> dejaGenereLEcheanceDocument(IDocumentDTO ds ) {
//	List<TaLigneALigneEcheanceDTO> dejaGenere = null;
//	String requeteFixeRDocument = null;
//	String requeteWhereSupp = null;
//	if(ds.getTypeDocument().equals(TaAcompte.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLAcompte l join l.taDocument d   left join l.taArticle art   where d.idDocument="+ds.getId();
//	if(ds.getTypeDocument().equals(TaAvoir.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLAvoir l join l.taDocument d   left join l.taArticle art   where d.idDocument="+ds.getId();
//	if(ds.getTypeDocument().equals(TaAvisEcheance.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLAvisEcheance l join l.taDocument d    left join l.taArticle art  where d.idDocument="+ds.getId();
//	if(ds.getTypeDocument().equals(TaApporteur.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLApporteur l join l.taDocument d    left join l.taArticle art  where d.idDocument="+ds.getId();
//	if(ds.getTypeDocument().equals(TaBoncde.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLBoncde l join l.taDocument d    left join l.taArticle art  where d.idDocument="+ds.getId();
//	if(ds.getTypeDocument().equals(TaBoncdeAchat.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLBoncdeAchat l join l.taDocument d   left join l.taArticle art   where d.idDocument="+ds.getId();
//	if(ds.getTypeDocument().equals(TaBonReception.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLBonReception l join l.taDocument d   left join l.taArticle art   where d.idDocument="+ds.getId();
//	if(ds.getTypeDocument().equals(TaBonliv.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLBonliv l join l.taDocument d   left join l.taArticle art   where d.idDocument="+ds.getId();
//	if(ds.getTypeDocument().equals(TaDevis.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLDevis l join l.taDocument d   left join l.taArticle art   where d.idDocument="+ds.getId();
//	if(ds.getTypeDocument().equals(TaFacture.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLFacture l join l.taDocument d   left join l.taArticle art   where d.idDocument="+ds.getId();
//	if(ds.getTypeDocument().equals(TaPrelevement.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLPrelevement l join l.taDocument d   left join l.taArticle art   where d.idDocument="+ds.getId();
//	if(ds.getTypeDocument().equals(TaProforma.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLProforma l join l.taDocument d   left join l.taArticle art   where d.idDocument="+ds.getId();
//	if(ds.getTypeDocument().equals(TaTicketDeCaisse.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLTicketDeCaisse l join l.taDocument d   left join l.taArticle art   where d.idDocument="+ds.getId();
//	if(ds.getTypeDocument().equals(TaPreparation.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLPreparation l join l.taDocument d   left join l.taArticle art   where d.idDocument="+ds.getId();
//	if(ds.getTypeDocument().equals(TaFlash.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLFlash l join l.taFlash d   left join l.taArticle art   where d.idFlash="+ds.getId();
//	if(ds.getTypeDocument().equals(TaPanier.TYPE_DOC))requeteFixeRDocument=" from TaLigneALigneEcheance x join x.taLPanier l join l.taDocument d   left join l.taArticle art   where d.idDocument="+ds.getId();
//
//	if(requeteFixeRDocument!=null) {
//		String requeteDoc="";
//		if(ds!=null && ds.getId()!=null && ds.getId()!=0) {
//			String jpql = "select x "+requeteFixeRDocument;
//			String nomEntity = null;
//			String typeEntity = null;
//			int idEntity = -1;
//			List<TaLigneALigneEcheance> l = taLigneALigneEcheanceService.dejaGenere(jpql);
//			if(l!=null && l.size()>0)dejaGenere=new LinkedList<>();
//			for (TaLigneALigneEcheance ligne : l) {
//				requeteWhereSupp="";
//				requeteDoc="";
//
//
//
//
//				if(requeteWhereSupp==null || requeteWhereSupp.isEmpty()) {            
//					//            requeteWhereSupp=" and x."+nomEntity+".idLDocument="+idEntity;
//					requeteDoc="select new fr.legrain.document.dto.TaLigneALigneEcheanceDTO(x.id,x.idLigneSrc,l.idLDocument,x.taLEcheance.idLEcheance,x.taLEcheance.taAbonnement.idDocument,x.taLEcheance.taAbonnement.codeDocument,"
//							+ "x.taLEcheance.libLDocument,art.codeArticle,x.taLEcheance.debutPeriode,x.taLEcheance.finPeriode,x.taLEcheance.taLAbonnement.complement1,x.taLEcheance.taLAbonnement.complement2,x.taLEcheance.taLAbonnement.complement1) ";
//				}
//				List<TaLigneALigneEcheanceDTO> l2 = new LinkedList<>();
//				if(requeteDoc!=null && !requeteDoc.equals(""))
//					l2 =taLigneALigneEcheanceService.dejaGenereLigneDocument(requeteDoc+requeteFixeRDocument+requeteWhereSupp);
//				for (TaLigneALigneEcheanceDTO iLDocumentTiers : l2) {
//					dejaGenere.add(iLDocumentTiers);
//				}
//			}
//			System.err.println(l.size());
//		} 
//	}
//	return dejaGenere;
//}


public List<TaLigneALigneEcheanceDTO> rechercheSiLigneEcheanceDocLie(ILigneDocumentJSF ligne) {
	if(ligne!=null && ligne.getDto()!=null)
		return taLigneALigneEcheanceService.dejaGenereLEcheanceDocument(ligne.getDto());
	return null;
}



public TaCoefficientUnite recupCoefficientUnite(String code1, String code2) {
	try {
		TaCoefficientUnite coef = null;
		if(code1!=null && code2!=null && code1.equals(code2)) {
			//unite 1 = unite 2 => rapport "1"
			coef = new TaCoefficientUnite();
			coef.setUniteA(taUniteService.findByCode(code1));
			coef.setUniteB(coef.getUniteA());
			coef.setCoeffUniteAVersB(new BigDecimal(1));
			coef.setCoeffUniteBVersA(new BigDecimal(1));
			coef.setCoeff(new BigDecimal(1));
			coef.setNbDecimale(2);
			coef.setNbDecimaleAVersB(2);
			coef.setNbDecimaleBVersA(2);
			coef.setOperateurDivise(false);
			coef.setOperateurAVersBDivise(false);
			coef.setOperateurBVersADivise(false);
		} else {
			coef = taCoefficientUniteService.findByCode(code1,code2);
			if(coef==null)coef = taCoefficientUniteService.findByCode(code2,code1);
		}
		if(coef!=null) {
			coef.recupRapportEtSens(code1,code2);
		}
		return coef;
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
}


public boolean isGestionCapsules() {
	return gestionCapsules;
}

public void setGestionCapsules(boolean gestionCapsules) {
	this.gestionCapsules = gestionCapsules;
}



}
