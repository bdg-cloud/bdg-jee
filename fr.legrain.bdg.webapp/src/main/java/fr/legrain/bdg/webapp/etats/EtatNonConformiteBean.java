package fr.legrain.bdg.webapp.etats;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;

import fr.legrain.article.dto.TaLotDTO;
import fr.legrain.article.model.TaLot;
import fr.legrain.article.model.TracabiliteLot;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaUniteServiceRemote;
import fr.legrain.bdg.conformite.service.remote.ITaResultatConformiteServiceRemote;
import fr.legrain.bdg.conformite.service.remote.ITaStatusConformiteServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.webapp.codebarre.CodeBarreParam;
import fr.legrain.bdg.webapp.codebarre.GenerationCodeBarreController;
import fr.legrain.conformite.model.TaConformite;
import fr.legrain.conformite.model.TaResultatConformite;
import fr.legrain.conformite.model.TaStatusConformite;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.lib.data.LibConversion;



@Named
@ViewScoped 
public class EtatNonConformiteBean implements Serializable {
	
	@EJB private ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	@EJB private ITaArticleServiceRemote taArticleService;
	@EJB private ITaLotServiceRemote taLotService;
	@EJB private ITaUniteServiceRemote taUniteService;
	
//	private @EJB ITaConformiteServiceRemote taConformiteService;
	private @EJB ITaResultatConformiteServiceRemote taResultatConformiteService;
//	private @EJB ITaResultatCorrectionServiceRemote taResultatCorrectionService;
	
	private @EJB ITaStatusConformiteServiceRemote taStatusConformiteServiceRemote;

	private List<TaLotDTO> model;
	
	private TaLotDTO[] selection;
	private TaLotDTO detailLigne;
	
	private List<TaStatusConformite> listeTaStatusConformite;

	private TaInfoEntreprise infosEntreprise = null;
	
	private Date dateDebut;
	private Date dateFin;
	
	private List<TaResultatConformite> historique;
	private List<TaResultatConformite> dernieresValeur = new LinkedList<>();
	private TaResultatConformite selectionRC;
	private TracabiliteLot tracaLot;
	
	private TaLot taLot;
	private Integer idLot;

	@PostConstruct
	public void init() {

		infosEntreprise =taInfoEntrepriseService.findInstance();

		dateDebut = infosEntreprise.getDatedebInfoEntreprise();
		dateFin = infosEntreprise.getDatefinInfoEntreprise();
		
		listeTaStatusConformite = taStatusConformiteServiceRemote.selectAll();
		
		model=new LinkedList<TaLotDTO>();
	}
	
	public void actRecherche(ActionEvent e) {
		try{
			
			List<TaLotDTO> listeTemp =new LinkedList<TaLotDTO>();
			model=new LinkedList<TaLotDTO>();
			
			model = taLotService.findLotAvecQte(dateDebut, dateFin,null);
			
		}catch (Exception ex) {
			ex.printStackTrace();
		}
		finally{
		}
	}
	
	public void actFermer() {
		model=new LinkedList<TaLotDTO>();
	}
	
	public void actImprimerEtiquetteCB128(ActionEvent actionEvent) {

		String numLot = (String)actionEvent.getComponent().getAttributes().get("numLot");
		String qteRefString = (String)actionEvent.getComponent().getAttributes().get("qteRef");
		String codeUniteRef = (String)actionEvent.getComponent().getAttributes().get("codeUniteRef");
		CodeBarreParam param = new CodeBarreParam();
		try {
			BigDecimal qteRef = null;
			if(qteRefString!=null) {
				qteRef = LibConversion.stringToBigDecimal(qteRefString);
			}
			param.setTaLot(taLotService.findByCode(numLot));
			param.setTaUnite(taUniteService.findByCode(codeUniteRef));
			param.setQuantite(qteRef);
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		param.setTaLot(taLot);
		GenerationCodeBarreController.actDialoguePaiementEcheanceParCarte(param);
		
		
	}
	
	public void actNonTermineLot(ActionEvent e) {
		try{
			
			if(selection!=null) {
				List<Integer> l = new ArrayList<>();
				for (TaLotDTO dto : selection) {
					l.add(dto.getId());
				}
				taLotService.changeLotTermine(l,false);
				actRecherche(null);
				selection = new TaLotDTO[] {};
				
			}
			
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void actTermineLot(ActionEvent e) {
		try{
			
			if(selection!=null && selection.length>0) {
//				TaLot l = null;
//				for (TaLotDTO dto : selection) {
//					l = taLotService.findById(dto.getId());
//					l.setTermine(!l.getTermine());
//					l = taLotService.merge(l);
//				}
				List<Integer> l = new ArrayList<>();
				for (TaLotDTO dto : selection) {
					l.add(dto.getId());
				}
				taLotService.changeLotTermine(l,true);
				actRecherche(null);
				selection = new TaLotDTO[] {};
				
			}
			
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void actDetailTracaLot() {
		try {
			//detailLigne = ((TaLotDTO) event.getData());
//			taLot = taLotService.findById(detailLigne.getId());
			taLot = taLotService.fetchResultatControleLazy(detailLigne.getId());
			boolean lotSansControle = false;
			if(taLot == null) {
				//A prioris, cas d'un lot sans resultat de conformité, donc sans controle saisi dans l'article et donc toujours valide par défaut
				taLot = taLotService.findById(detailLigne.getId());
				 lotSansControle = true;
			}
			initListeRC(lotSansControle);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onRowToggle(ToggleEvent event) {
		try {
			detailLigne = ((TaLotDTO) event.getData());
//			taLot = taLotService.findById(detailLigne.getId());
			taLot = taLotService.fetchResultatControleLazy(detailLigne.getId());
			boolean lotSansControle = false;
			if(taLot == null) {
				//A prioris, cas d'un lot sans resultat de conformité, donc sans controle saisi dans l'article et donc toujours valide par défaut
				taLot = taLotService.findById(detailLigne.getId());
				 lotSansControle = true;
			}
			initListeRC(lotSansControle);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initListeRC(boolean lotSansControle) {
		TaConformite taConformite = null;
		List<String> l = new ArrayList<>();
		dernieresValeur = new LinkedList<>();
		if(!lotSansControle) {
			if(taLot.getTaResultatConformites()!=null) {
				for (TaResultatConformite taResultatConformite : taLot.getTaResultatConformites()) {
					taConformite = taResultatConformite.getTaConformite();
					if(!l.contains(LibConversion.integerToString(taConformite.getIdConformite()))) {
						l.add(LibConversion.integerToString(taConformite.getIdConformite())); //pour éviter d'afficher plusieur fois un controle dans le cas ou il aurait plusieur résultat (historique)
						
						//Si des résultat existe pour ce controle pour ce lot, remonter les résultats, 
						//sinon affiché un nouveau controle vierge pour la saisie de valeur constatée
		//					TaResultatConformite taResultatConformite = taResultatConformiteService.findByLotAndControleConformite(taLot.getIdLot(),taConformite.getIdConformite());
						historique = taResultatConformiteService.findByLotAndControleConformiteHistorique(taLot.getIdLot(),taConformite.getIdConformite());
						
						dernieresValeur.add(historique.get(0));
						
						Collections.sort(dernieresValeur, (c1, c2) -> c1.getTaConformite().getPosition().compareTo(c2.getTaConformite().getPosition()));
					}
				}
			}
		}
		tracaLot = taLotService.findTracaLot(taLot.getNumLot());
	}
	
	public void actImprimerUneFiche() {
		actImprimerUneFiche(null);
	}
	
	public void actImprimerUneFiche(ActionEvent actionEvent) {
//		FacesContext context = FacesContext.getCurrentInstance();  
//		context.addMessage(null, new FacesMessage("Non conformité", "actImprimerUneFiche")); 
		try {
						
//		FacesContext facesContext = FacesContext.getCurrentInstance();
//		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
			
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		Map<String, Object> edition = new HashMap<>();
		
		List<TaResultatConformite> listeRC = new ArrayList<>();
		listeRC.add(selectionRC);
		edition.put("listeRC", listeRC);
		
//		taLot = taLotService.fetchResultatControleLazy(idLot);
		taLot = taLotService.findById(idLot);
		
		edition.put("lot", taLot);
		edition.put("modeLot", false);
		
		List<TaResultatConformite> h = null;
		Map<String,List<TaResultatConformite>> historique = new HashMap<>();
		for (TaResultatConformite taResultatConformite : listeRC) {
			 h = taResultatConformiteService.findByLotAndControleConformiteHistorique(idLot,taResultatConformite.getTaConformite().getIdConformite());
			 historique.put(taResultatConformite.getTaConformite().getLibelleConformite(), h);
		}
		edition.put("historique", historique);
		
		sessionMap.put("edition", edition);
		
//			session.setAttribute("tiers", taTiersService.findById(selectedTaTiersDTO.getId()));
			System.out.println("EtatNonConformiteBean.actImprimerUneFiche()");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	public void actImprimerControleLot() {
		actImprimerControleLot(null);
	}
	
	public void actImprimerControleLot(ActionEvent actionEvent) {
//		FacesContext context = FacesContext.getCurrentInstance();  
//		context.addMessage(null, new FacesMessage("Non conformité", "actImprimerControleLot")); 
		try {
						
//		FacesContext facesContext = FacesContext.getCurrentInstance();
//		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
			
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		Map<String, Object> edition = new HashMap<>();
		
//		taLot = taLotService.findById(idLot);
		taLot = taLotService.fetchResultatControleLazy(idLot);
		boolean lotSansControle = false;
		if(taLot == null) {
			//A prioris, cas d'un lot sans resultat de conformité, donc sans controle saisi dans l'article et donc toujours valide par défaut
			taLot = taLotService.findById(idLot);
			 lotSansControle = true;
		}
		initListeRC(lotSansControle);
		
		edition.put("listeRC", dernieresValeur);
		
		edition.put("lot", taLot);
		edition.put("modeLot", true);
		
		List<TaResultatConformite> h = null;
		Map<String,List<TaResultatConformite>> historique = new HashMap<>();
		for (TaResultatConformite taResultatConformite : dernieresValeur) {
			 h = taResultatConformiteService.findByLotAndControleConformiteHistorique(taLot.getIdLot(),taResultatConformite.getTaConformite().getIdConformite());
			 historique.put(taResultatConformite.getTaConformite().getLibelleConformite(), h);
		}
		edition.put("historique", historique);
		
		sessionMap.put("edition", edition);
		
//			session.setAttribute("tiers", taTiersService.findById(selectedTaTiersDTO.getId()));
			System.out.println("EtatNonConformiteBean.actImprimerControleLot()");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void actDialogControleLot(ActionEvent actionEvent) {
		
//		PrimeFaces.current().dialog().openDynamic("aide");
    
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
//        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
        params.put("modeEcranDefaut", list);
        
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		TaLot l = new TaLot();
		
//		creerLot();
//		sessionMap.put("lotBR", selectedLigneJSF.getTaLot());
		String numLot =  (String) actionEvent.getComponent().getAttributes().get("lot");
		//sessionMap.put("numLot", selectedLigneJSF.getDto().getNumLot());
		sessionMap.put("numLot", numLot);
        
        PrimeFaces.current().dialog().openDynamic("solstyce/dialog_controle_article", options, params);
		
//		FacesContext context = FacesContext.getCurrentInstance();  
//		context.addMessage(null, new FacesMessage("Tiers", "actAbout")); 	    
	}

	public void handleReturnDialogControleLot(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			TaLot j = (TaLot) event.getObject();
			
////			String action = lotService.validationLot(masterEntityLignePF.getTaLot());
////			TaStatusConformite s = taResultatConformiteService.etatLot(masterEntityLignePF.getTaLot().getIdLot());
//			masterEntityLigne.setTaLot(j);
//			selectedLigneJSF.setTaLot(j);
//			taLot = j;
//			System.out.println(j.getTaStatusConformite().getCodeStatusConformite());
//			detailLigne.setCodeStatusConformite(j.getTaStatusConformite().getCodeStatusConformite());
			
			actRecherche(null);
			
//			if(selectedLigneJSF instanceof ILigneDocumentLotDTO) {
//				//((ILigneDocumentLotDTO)masterEntityLignePF).setCodeStatusConformite(action);
//				((ILigneDocumentLotDTO)masterEntityLigne).setCodeStatusConformite(masterEntityLigne.getTaLot().getTaStatusConformite().getCodeStatusConformite());
//			}
		}
	}

	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}


	public List<TaLotDTO> getModel() {
		return model;
	}

	public void setModel(List<TaLotDTO> modelDocument) {
		this.model = modelDocument;
	}

	public TaLotDTO[] getSelection() {
		return selection;
	}

	public void setSelection(TaLotDTO[] selection) {
		this.selection = selection;
	}

	public TaLotDTO getDetailLigne() {
		return detailLigne;
	}

	public void setDetailLigne(TaLotDTO detailLigne) {
		this.detailLigne = detailLigne;
	}

	public List<TaResultatConformite> getHistorique() {
		return historique;
	}

	public void setHistorique(List<TaResultatConformite> historique) {
		this.historique = historique;
	}

	public TaLot getTaLot() {
		return taLot;
	}

	public void setTaLot(TaLot taLot) {
		this.taLot = taLot;
	}

	public List<TaResultatConformite> getDernieresValeur() {
		return dernieresValeur;
	}

	public void setDernieresValeur(List<TaResultatConformite> dernieresValeur) {
		this.dernieresValeur = dernieresValeur;
	}

	public TaResultatConformite getSelectionRC() {
		return selectionRC;
	}

	public void setSelectionRC(TaResultatConformite selectionRC) {
		this.selectionRC = selectionRC;
	}

	public Integer getIdLot() {
		return idLot;
	}

	public void setIdLot(Integer idLot) {
		this.idLot = idLot;
	}

	public List<TaStatusConformite> getListeTaStatusConformite() {
		return listeTaStatusConformite;
	}

	public void setListeTaStatusConformite(List<TaStatusConformite> listeTaStatusConformite) {
		this.listeTaStatusConformite = listeTaStatusConformite;
	}

	public TracabiliteLot getTracaLot() {
		return tracaLot;
	}

	public void setTracaLot(TracabiliteLot tracaLot) {
		this.tracaLot = tracaLot;
	}

	
}





