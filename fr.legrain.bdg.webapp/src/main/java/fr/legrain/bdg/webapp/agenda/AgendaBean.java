package fr.legrain.bdg.webapp.agenda;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleModel;

import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaFabricationServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.bdg.tache.service.remote.ITaAgendaServiceRemote;
import fr.legrain.bdg.tache.service.remote.ITaEvenementServiceRemote;
import fr.legrain.bdg.tache.service.remote.ITaNotificationServiceRemote;
import fr.legrain.bdg.tache.service.remote.ITaTypeEvenementServiceRemote;
import fr.legrain.bdg.tache.service.remote.ITaTypeNotificationServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.app.AutorisationBean;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.oauth.google.GoogleBean;
import fr.legrain.bdg.webapp.oauth.microsoft.MSBean;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.droits.dto.TaUtilisateurDTO;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibDate;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.tache.model.TaAgenda;
import fr.legrain.tache.model.TaEvenement;
import fr.legrain.tache.model.TaNotification;
import fr.legrain.tache.model.TaTypeEvenement;
import fr.legrain.tiers.dto.TaTiersDTO;

@Named
@ViewScoped
public class AgendaBean  implements Serializable {

	private static final long serialVersionUID = 3969761644014803920L;
	
	
	
	@Inject private	SessionInfo sessionInfo;
	@Inject private	TenantInfo tenantInfo;
	
	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
	
	@Inject @Named(value="autorisationBean")
	private AutorisationBean autorisation;
	
	@Inject @Named(value="notificationAgendaBean")
	private NotificationAgendaBean notificationAgenda;
	
	
//	@Inject @Named(value="abstractController")
//	private AbstractController abstractController;
//	
//	@Inject @Named(value="fabricationController")
//	private FabricationController fabricationController;
	
	private @EJB ITaFabricationServiceRemote taFabricationService;
	private @EJB ITaArticleServiceRemote taArticleService;
	private @EJB ITaTiersServiceRemote taTiersService;
	
	private @EJB ITaUtilisateurServiceRemote taUtilisateurService;
	private @EJB ITaEvenementServiceRemote taEvenementService;
	private @EJB ITaNotificationServiceRemote taNotificationServiceRemote;
	private @EJB ITaAgendaServiceRemote taAgendaService;
	private @EJB ITaTypeEvenementServiceRemote taTypeEvenementService;
	private @EJB ITaTypeNotificationServiceRemote taTypeNotificationService;
	
	private @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	
	private @Inject GoogleBean g;
	private @Inject MSBean ms;
	
	private List<TaAgenda> selectedAgenda; //agendas selectionnes pour l'affichage
	private List<TaAgenda> agendas; //
	private TaAgenda taAgenda; //agenda courant
	private List<TaAgenda> listeAgenda; //agenda de l'utilisateur
	
	private TaTypeEvenement taTypeEvenement;
	
	private String cssAgenda = "";
	
	private TaUtilisateurDTO utilisateurDTOAutoComplete;
	private TaTiersDTO tiersDTOAutoComplete;

	private ScheduleModel eventModel;
	private LgrTabScheduleEvent event;
	private static final String ouvertureDialogueEvenement = "ouvertureDialogueEvenement";
	
	private List<TaEvenement> listeEvtDatatable;
	private TaEvenement selectedEvenement;
	
	private List<TaNotification> listeNotifDatatable;
	private TaNotification selectedNotification;
	
	private Date dateDebutExo;
	private Date dateFinExo;
	
	private Integer nbNotificationNonLu;

	
	@PostConstruct
	public void init() {
		TaInfoEntreprise infoEntreprise = taInfoEntrepriseService.findInstance();
    	dateDebutExo = infoEntreprise.getDatedebInfoEntreprise();
    	dateFinExo = infoEntreprise.getDatefinInfoEntreprise();
		
		listeAgenda = taAgendaService.findAgendaUtilisateur(sessionInfo.getUtilisateur());
		if(g.validToken()) {
			listeAgenda.addAll(g.findListeGoogleCal());
		}
		if(ms.validToken()) {
			listeAgenda.addAll(ms.findListeMSCal());
		}
		agendas = listeAgenda;
		selectedAgenda = agendas;
		
		initListeEvenement();
		initListeNotification();

		for (TaAgenda taAgenda : listeAgenda) {
			String cssClassName = "."+taAgenda.getNom().replaceAll(" ", "");
			cssAgenda += cssClassName+".fc-event,"
					//+cssClassName+" a,"
					//+cssClassName+" .fc.fc-ltr .fc-event"
					+cssClassName+" .fc-event-inner"+"{ "
							+ "background-color: #"+taAgenda.getCouleur()+" !important;"
							+ "border-color: #"+taAgenda.getCouleur()+" !important;"
							+ "}";
		}
		
		initCalendrier();

	}
	
	private void initCalendrier() {
		initCalendrier(false);
	}
	
	private void initCalendrier(boolean force) {
		boolean dynamic = true;
		if(eventModel==null || force) {
			if(dynamic) {
			
				eventModel = new LazyScheduleModel() {
					private static final long serialVersionUID = -3000709249377994005L;
					@Override
		            public void loadEvents(LocalDateTime start, LocalDateTime end) {
						modelCalendar(LibDate.localDateTimeToDate(start),LibDate.localDateTimeToDate(end));
						//PrimeFaces.current().ajax().update("@widgetVar(wvDatatableEventModel)");
		            }   
		        };
			} else {
				eventModel = new DefaultScheduleModel();
				modelCalendar(null,null);
			}
		}
	}
	
	public void modelCalendar(Date start, Date end) {
//		if(autorisation.autoriseMenu(AutorisationBean.ID_MODULE_TRACABILITE)) {
//			List<TaFabricationDTO> listeFab = taFabricationService.findByDateLight(start,end);
//			LgrTabScheduleEvent evt = null;
//			for (TaFabricationDTO taFabricationDTO : listeFab) {
//				evt = new LgrTabScheduleEvent(taFabricationDTO.getCodeDocument(), taFabricationDTO.getDateDebR(), taFabricationDTO.getDateDebR());
//				evt.setCssLgrTab(LgrTab.CSS_CLASS_TAB_FABRICATION);
//				evt.setIdObjet(taFabricationDTO.getId());
//				evt.setCodeObjet(taFabricationDTO.getCodeDocument());
//				evt.setData(taFabricationDTO);
////				evt.setStyleClass("");
//				eventModel.addEvent(evt);
//			}
//		}
		
//		List<TaAgenda> l = new ArrayList<>();
//		for (TaAgenda taAgenda : agendas) {
//			boolean trouve = false;
//			for (String nomAg : selectedAgenda) {
//				if(taAgenda.getNom().equals(nomAg)) {
//					trouve = true;
//				}
//			}
//			if(trouve) {
//				l.add(taAgenda);
//			}
//		}
		
		//List<TaEvenement> listeEvt = taEvenementService.selectAll();
		//List<TaEvenement> listeEvt = taEvenementService.findByDate(start,end,listeAgenda);
		//List<TaEvenement> listeEvt = taEvenementService.findByDate(start,end,selectedAgenda);
		List<TaEvenement> listeEvt = taEvenementService.findByDate(start,end,findSelectedAgendaType(null));
		LgrTabScheduleEvent evt = null;
		for (TaEvenement taEvenement : listeEvt) {
			evt = new LgrTabScheduleEvent(taEvenement.getLibelleEvenement(), taEvenement.getDateDebut(), taEvenement.getDateFin());
			evt.setCssLgrTab(LgrTab.CSS_CLASS_TAB_FABRICATION);
			evt.setIdObjet(taEvenement.getIdEvenement());
			evt.setCodeObjet(taEvenement.getCodeEvenement());
			evt.setData(taEvenement);
			evt.setTaEvenement(taEvenement);
			if(taEvenement.getTaAgenda()!=null) {
				evt.setStyleClass(taEvenement.getTaAgenda().getNom().replaceAll(" ", ""));
			}
			eventModel.addEvent(evt);
		}
		
		if(g.validToken()) {
			List<LgrTabScheduleEvent> aa =  g.findListeEventGoogleCal(start,end,findSelectedAgendaType(TaAgenda.AGENDA_GOOGLE_CALENDAR));
			for (LgrTabScheduleEvent ggg : aa) {
				eventModel.addEvent(ggg);
			}
		}
		if(ms.validToken()) {
			List<LgrTabScheduleEvent> aa =  ms.findListeEventMSCal(start,end,findSelectedAgendaType(TaAgenda.AGENDA_MICROSOFT_OUTLOOK));
			for (LgrTabScheduleEvent ggg : aa) {
				eventModel.addEvent(ggg);
			}
		}
	}
	
	public List<TaAgenda> findSelectedAgendaType(String type) { 
		List<TaAgenda> l = null;
		if(type == null) {
			//BDG
			List<TaAgenda> agendaNonBdg = new ArrayList<>();
			for (TaAgenda taAgenda : selectedAgenda) {
				if(taAgenda.getOrigine()!=null) {
					agendaNonBdg.add(taAgenda);
				}
			}
			List<TaAgenda> agendaBdg = new ArrayList<>();
			agendaBdg.addAll(selectedAgenda);
			agendaBdg.removeAll(agendaNonBdg);
			l = agendaBdg;
		} else if (type.equals(TaAgenda.AGENDA_GOOGLE_CALENDAR)) {
			//GOOGLE
			List<TaAgenda> agendaGoogle = new ArrayList<>();
			for (TaAgenda taAgenda : selectedAgenda) {
				if(taAgenda.getOrigine()!=null && taAgenda.getOrigine().equals(TaAgenda.AGENDA_GOOGLE_CALENDAR)) {
					agendaGoogle.add(taAgenda);
				}
			}
			l = agendaGoogle;
		} else if (type.equals(TaAgenda.AGENDA_MICROSOFT_OUTLOOK)) {
			//MICROSOFT
			List<TaAgenda> agendaMs = new ArrayList<>();
			for (TaAgenda taAgenda : selectedAgenda) {
				if(taAgenda.getOrigine()!=null && taAgenda.getOrigine().equals(TaAgenda.AGENDA_MICROSOFT_OUTLOOK)) {
					agendaMs.add(taAgenda);
				}
			}
			l = agendaMs;
		}
		return l;
	}
	
	
	public void initListeEvenement() {
		List<TaAgenda> agendaBdg = findSelectedAgendaType(null);
		//listeEvtDatatable = taEvenementService.findByDate(dateDebutExo,dateFinExo,selectedAgenda);
		listeEvtDatatable = taEvenementService.findByDate(dateDebutExo,dateFinExo,agendaBdg);
		
		if(g.validToken()) {
			List<TaAgenda> agendaGoogle = findSelectedAgendaType(TaAgenda.AGENDA_GOOGLE_CALENDAR);
			if(listeEvtDatatable == null) {
				listeEvtDatatable = new ArrayList<>();
			}
			
			List<LgrTabScheduleEvent> liste =  g.findListeEventGoogleCal(dateDebutExo,dateFinExo,agendaGoogle);
			for (LgrTabScheduleEvent ggg : liste) {
				listeEvtDatatable.add(ggg.getTaEvenement());
			}
		}
		
		if(ms.validToken()) {
			List<TaAgenda> agendaMs = findSelectedAgendaType(TaAgenda.AGENDA_MICROSOFT_OUTLOOK);
			if(listeEvtDatatable == null) {
				listeEvtDatatable = new ArrayList<>();
			}
			
			List<LgrTabScheduleEvent> liste =  ms.findListeEventMSCal(dateDebutExo,dateFinExo,agendaMs);
			for (LgrTabScheduleEvent ggg : liste) {
				listeEvtDatatable.add(ggg.getTaEvenement());
			}
		}
	}
	
	public void initListeNotification() {
		listeNotifDatatable = taNotificationServiceRemote.findNotificationInWebAppEnvoyee(null, sessionInfo.getUtilisateur().getId(),null);
		
		List<TaNotification> listeNotifNonLu = taNotificationServiceRemote.findNotificationInWebAppEnvoyee(null, sessionInfo.getUtilisateur().getId(),false);
		nbNotificationNonLu = null;
		if(listeNotifNonLu!=null && !listeNotifNonLu.isEmpty()) {
			nbNotificationNonLu = listeNotifNonLu.size();
			System.out.println(nbNotificationNonLu);
		}
	}
	
	public void remoteCommandNotificationAgenda(ActionEvent actionEvent) {
		initListeNotification();
		notificationAgenda.initListeNotification();
		System.out.println("AgendaBean.remoteCommandNotificationAgenda()");
	}
	
	public void actDialogEvenement(ActionEvent actionEvent) {
	    
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentHeight", 640);
        options.put("modal", true);
        
        //Map<String,List<String>> params = null;
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(modeEcran.getMode()));
        params.put("modeEcranDefaut", list);
        
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		
		//String numLot =  (String) actionEvent.getComponent().getAttributes().get("idEvenement");
		if(event!=null && event.getTaEvenement()!=null) {
			if (event.getTaEvenement().getOrigine()==null 
					|| !event.getTaEvenement().getOrigine().equals(TaAgenda.AGENDA_GOOGLE_CALENDAR)
					) {
				//pas de liaison aux document pour les evenement google ppour l'instant
				if(event.getTaEvenement().getIdEvenement()!=0) {
					event.getTaEvenement().setListeDocument(taEvenementService.findListeDocuments(event.getTaEvenement()));
				}
			}
		}
		sessionMap.put("event", event);
        
        PrimeFaces.current().dialog().openDynamic("agenda/dialog_evenement", options, params);
		
//		FacesContext context = FacesContext.getCurrentInstance();  
//		context.addMessage(null, new FacesMessage("Tiers", "actAbout")); 	    
	}
	
	public void onRowSelectEvenement(SelectEvent e) {  
		LgrTabScheduleEvent evt = new LgrTabScheduleEvent(selectedEvenement.getLibelleEvenement(), selectedEvenement.getDateDebut(), selectedEvenement.getDateFin());
//		evt.setCssLgrTab(LgrTab.CSS_CLASS_TAB_FABRICATION);
//		evt.setIdObjet(selectedEvenement.getIdEvenement());
//		evt.setCodeObjet(selectedEvenement.getCodeEvenement());
//		evt.setData(selectedEvenement);
		evt.setTaEvenement(selectedEvenement);
		if(selectedEvenement.getTaAgenda()!=null) {
			evt.setStyleClass(selectedEvenement.getTaAgenda().getNom().replaceAll(" ", ""));
		}
		event = evt;
	    autoCompleteMapDTOtoUI();
	    modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}
	
	public void onRowSelectNotification(SelectEvent event) {  
		selectedNotification.setLu(true);
		selectedNotification = taNotificationServiceRemote.merge(selectedNotification);
		initListeNotification();
	}
	
	public static void initAppelDialogue() {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		
		sessionMap.put(ouvertureDialogueEvenement, true);
	}
	
	private void cleanAppelDialogue() {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		
		sessionMap.remove(ouvertureDialogueEvenement);
	}
	
	
	
	public void handleReturnDialogEvenement(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			initCalendrier(true);
		}
		initListeEvenement();
		initListeNotification();
	}
	
	public void actFermerDialog(ActionEvent actionEvent) {
		PrimeFaces.current().dialog().closeDynamic(null);
//		PrimeFaces.current().dialog().closeDynamic(listeControle);
	}
	
	public void onEventSelect(SelectEvent selectEvent) {
        event = (LgrTabScheduleEvent) selectEvent.getObject();
       // event.setTaEvenement(taEvenementService.loadEventDocument(event.getTaEvenement()));
        autoCompleteMapDTOtoUI();
        modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
    }
	
	public void agendaChange(AjaxBehaviorEvent event) {
		System.out.println("AgendaBean.agendaChange()");
		initCalendrier(true);
	}
	
//	public void openDocument() {
//		openDocument(null);
//	}
	
//	public void openDocument(ActionEvent e) {
//		if(event.getCssLgrTab().equals(LgrTab.CSS_CLASS_TAB_FABRICATION)) {
//			fabricationController.setSelectedTaFabricationDTO((TaFabricationDTO) event.getData());
//			fabricationController.onRowSelect(null);
//		}
//	}
	
	public void actFermer(ActionEvent e) {
		
	}
	
	public void actFermerEvenement(ActionEvent e) {
		
	}
	
	
	
	

	public void actAnnulerEvenement(ActionEvent e) {
	
	}
	
	public void actEventSelectRC(ActionEvent e) {
		actDialogEvenement(null);
	}
	
	public void actNouveauEvenement(ActionEvent e) {
		event = null;
		taTypeEvenement = null;
		taAgenda = null;
		actDialogEvenement(e);
	}
	
	public void actInsererEvenement(ActionEvent e) {
		event = new LgrTabScheduleEvent();
		event.setTaEvenement(new TaEvenement());
		taTypeEvenement = null;
		taAgenda = null;
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
		initAppelDialogue();
	}
	
	
	
	public void autcompleteSelection(SelectEvent event) {
		Object value = event.getObject();
		
		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");

		validateUIField(nomChamp,value);
	}
	
	public boolean validateUIField(String nomChamp,Object value) {
		try {
//			if(nomChamp.equals(constChampUtilisateurAcAgenda)) {
//				 TaUtilisateur u = taUtilisateurService.findById(utilisateurDTOAutoComplete.getId());
//				 if(event.getTaEvenement().getListeUtilisateur()==null) {
//					 event.getTaEvenement().setListeUtilisateur(new ArrayList<>());
//				 }
//				 event.getTaEvenement().getListeUtilisateur().add(u);
//			} else if(nomChamp.equals(constChampTiersAcAgenda)) {
//				 TaTiers u = taTiersService.findById(tiersDTOAutoComplete.getId());
//				 if(event.getTaEvenement().getListeTiers()==null) {
//					 event.getTaEvenement().setListeTiers(new ArrayList<>());
//				 }
//				 event.getTaEvenement().getListeTiers().add(u);
//			}
			
//			if(nomChamp.equals(Const.C_CODE_T_EMAIL)) {
//					boolean changement=false;
//					if(selection.getCodeTEmail()!=null && value!=null && !selection.getCodeTEmail().equals(""))
//					{
//						if(value instanceof TaTypeEvenement)
//							changement=((TaTypeEvenement) value).getCodeTEmail().equals(selection.getCodeTEmail());
//						else if(value instanceof String)
//						changement=!value.equals(selection.getCodeTEmail());
//					}
//					if(changement && modeEcran.dataSetEnModeModification()){
//						FacesContext context = FacesContext.getCurrentInstance();  
//						context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Type d'email", Const.C_MESSAGE_CHANGEMENT_CODE));
//					}
//				}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void autoCompleteMapUIToDTO() {
//		if(taTAdrDTO!=null) {
//			validateUIField(Const.C_CODE_T_ADR,taTAdrDTO.getCodeTAdr());
//			selectedTaAdresseDTO.setCodeTAdr(taTAdrDTO.getCodeTAdr());
//		} else {
//			selectedTaAdresseDTO.setCodeTAdr(null);
//		}
		
	}
	
	public void autoCompleteMapDTOtoUI() {
//		try {
//			taTAdrDTO = null;
//			
//			if(selectedTaAdresseDTO.getCodeTAdr()!=null && !selectedTaAdresseDTO.getCodeTAdr().equals("")) {
//				taTAdrDTO=taTAdresseService.findByCodeDTO(selectedTaAdresseDTO.getCodeTAdr());
//			}
//			
//		} catch (FinderException e) {
//			e.printStackTrace();
//		}
		
		try {
			taAgenda = null;
			taTypeEvenement = null;
		
			if(event!=null) {
				if(event.getTaEvenement().getTaAgenda()!=null /*&& !selectedTaAdresseDTO.getCodeTAdr().equals("")*/) {
		//			taTAdrDTO=taTAdresseService.findByCodeDTO(selectedTaAdresseDTO.getCodeTAdr());
					taAgenda = event.getTaEvenement().getTaAgenda();
				}
				if(event.getTaEvenement().getTaTypeEvenement()!=null /*&& !selectedTaAdresseDTO.getCodeTAdr().equals("")*/) {
					taTypeEvenement = event.getTaEvenement().getTaTypeEvenement();
				}
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	

	public SessionInfo getSessionInfo() {
		return sessionInfo;
	}

	public void setSessionInfo(SessionInfo sessionInfo) {
		this.sessionInfo = sessionInfo;
	}

	public TenantInfo getTenantInfo() {
		return tenantInfo;
	}

	public void setTenantInfo(TenantInfo tenantInfo) {
		this.tenantInfo = tenantInfo;
	}

	public ScheduleModel getEventModel() {
		return eventModel;
	}

	public void setEventModel(ScheduleModel eventModel) {
		this.eventModel = eventModel;
	}

	public AutorisationBean getAutorisation() {
		return autorisation;
	}

	public void setAutorisation(AutorisationBean autorisation) {
		this.autorisation = autorisation;
	}

	public LgrTabScheduleEvent getEvent() {
		return event;
	}

	public void setEvent(LgrTabScheduleEvent event) {
		this.event = event;
	}

//	public FabricationController getFabricationController() {
//		return fabricationController;
//	}
//
//	public void setFabricationController(FabricationController fabricationController) {
//		this.fabricationController = fabricationController;
//	}

	public TaTypeEvenement getTaTypeEvenement() {
		return taTypeEvenement;
	}

	public void setTaTypeEvenement(TaTypeEvenement taTypeEvenement) {
		this.taTypeEvenement = taTypeEvenement;
	}

	public TaAgenda getTaAgenda() {
		return taAgenda;
	}

	public void setTaAgenda(TaAgenda taAgenda) {
		this.taAgenda = taAgenda;
	}

	public List<TaAgenda> getListeAgenda() {
		return listeAgenda;
	}

	public void setListeAgenda(List<TaAgenda> listeAgenda) {
		this.listeAgenda = listeAgenda;
	}

	public String getCssAgenda() {
		return cssAgenda;
	}

	public void setCssAgenda(String cssAgenda) {
		this.cssAgenda = cssAgenda;
	}

	public List<TaAgenda> getSelectedAgenda() {
		return selectedAgenda;
	}

	public void setSelectedAgenda(List<TaAgenda> selectedAgenda) {
		this.selectedAgenda = selectedAgenda;
	}

	public List<TaAgenda> getAgendas() {
		return agendas;
	}

	public void setAgendas(List<TaAgenda> agendas) {
		this.agendas = agendas;
	}

	public TaUtilisateurDTO getUtilisateurDTOAutoComplete() {
		return utilisateurDTOAutoComplete;
	}

	public void setUtilisateurDTOAutoComplete(TaUtilisateurDTO utilisateurDTOAutoComplete) {
		this.utilisateurDTOAutoComplete = utilisateurDTOAutoComplete;
	}

	public TaTiersDTO getTiersDTOAutoComplete() {
		return tiersDTOAutoComplete;
	}

	public void setTiersDTOAutoComplete(TaTiersDTO tiersDTOAutoComplete) {
		this.tiersDTOAutoComplete = tiersDTOAutoComplete;
	}

	public List<TaEvenement> getListeEvtDatatable() {
		return listeEvtDatatable;
	}

	public void setListeEvtDatatable(List<TaEvenement> listeEvtDatatable) {
		this.listeEvtDatatable = listeEvtDatatable;
	}

	public TaEvenement getSelectedEvenement() {
		return selectedEvenement;
	}

	public void setSelectedEvenement(TaEvenement selectedEvenement) {
		this.selectedEvenement = selectedEvenement;
	}

	public List<TaNotification> getListeNotifDatatable() {
		return listeNotifDatatable;
	}

	public void setListeNotifDatatable(List<TaNotification> listeNotifDatatable) {
		this.listeNotifDatatable = listeNotifDatatable;
	}

	public TaNotification getSelectedNotification() {
		return selectedNotification;
	}

	public void setSelectedNotification(TaNotification selectedNotification) {
		this.selectedNotification = selectedNotification;
	}

	public Integer getNbNotificationNonLu() {
		return nbNotificationNonLu;
	}

	public void setNbNotificationNonLu(Integer nbNotificationNonLu) {
		this.nbNotificationNonLu = nbNotificationNonLu;
	}


}
