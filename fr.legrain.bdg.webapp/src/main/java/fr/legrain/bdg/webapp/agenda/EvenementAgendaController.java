package fr.legrain.bdg.webapp.agenda;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.ejb.NoSuchObjectLocalException;
import javax.ejb.RemoveException;
import javax.ejb.Timer;
import javax.ejb.TimerHandle;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.apache.commons.lang.SerializationUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.StreamedContent;

import fr.legrain.article.dto.TaFamilleDTO;
import fr.legrain.article.dto.TaTTvaDTO;
import fr.legrain.article.dto.TaTvaDTO;
import fr.legrain.article.dto.TaUniteDTO;
import fr.legrain.article.model.TaMarqueArticle;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tache.service.remote.ITaAgendaServiceRemote;
import fr.legrain.bdg.tache.service.remote.ITaEvenementServiceRemote;
import fr.legrain.bdg.tache.service.remote.ITaNotificationServiceRemote;
import fr.legrain.bdg.tache.service.remote.ITaTypeEvenementServiceRemote;
import fr.legrain.bdg.tache.service.remote.ITaTypeNotificationServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.bdg.webapp.oauth.google.GoogleBean;
import fr.legrain.document.dto.TaTPaiementDTO;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.droits.dto.TaUtilisateurDTO;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.email.dto.TaMessageEmailDTO;
import fr.legrain.email.dto.TaPieceJointeEmailDTO;
import fr.legrain.email.model.TaMessageEmail;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.tache.dto.TaNotificationDTO;
import fr.legrain.tache.dto.TaTypeNotificationDTO;
import fr.legrain.tache.model.TaAgenda;
import fr.legrain.tache.model.TaEvenement;
import fr.legrain.tache.model.TaNotification;
import fr.legrain.tache.model.TaRDocumentEvenement;
import fr.legrain.tache.model.TaTypeEvenement;
import fr.legrain.tache.model.TaTypeNotification;
import fr.legrain.tache.service.TimerProgEvenementService;
import fr.legrain.tiers.dto.TaCPaiementDTO;
import fr.legrain.tiers.dto.TaTCiviliteDTO;
import fr.legrain.tiers.dto.TaTEntiteDTO;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaCPaiement;
import fr.legrain.tiers.model.TaFamilleTiers;
import fr.legrain.tiers.model.TaTTvaDoc;
import fr.legrain.tiers.model.TaTiers;

@Named
@ViewScoped  
public class EvenementAgendaController extends AbstractController implements Serializable {  
	
	public static final String constChampUtilisateurAcAgenda = "C_CHAMP_UTILISATEUR_AC_AGENDA";
	public static final String constChampTiersAcAgenda = "C_CHAMP_TIERS_AC_AGENDA";
	public static final String constChampDocAcAgenda = "C_CHAMP_DOC_AC_AGENDA";
	
	private static final String ouvertureDialogueEvenement = "ouvertureDialogueEvenement";

	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;

	@Inject @Named(value="tabViewsBean")
	private TabViewsBean tabViews;

	private String paramId;	

	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	protected @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;

	private @EJB ITaTiersServiceRemote taTiersService;
	private @EJB ITaUtilisateurServiceRemote taUtilisateurService;
	private @EJB ITaEvenementServiceRemote taEvenementService;
	private @EJB ITaNotificationServiceRemote taNotificationService;
	private @EJB ITaAgendaServiceRemote taAgendaService;
	private @EJB ITaTypeEvenementServiceRemote taTypeEvenementService;
	private @EJB ITaTypeNotificationServiceRemote taTypeNotificationService;
	private @EJB TimerProgEvenementService timerProgEvenementService;

	private @Inject SessionInfo sessionInfo;

	private StreamedContent streamedPieceJointe;
	private TaPieceJointeEmailDTO selectedTaPieceJointeEmailDTO;


	private TaAgenda taAgenda; //agenda courant
	private TaTypeEvenement taTypeEvenement;
	private LgrTabScheduleEvent event;
	
	private TaUtilisateurDTO utilisateurDTOAutoComplete;
	private TaTiersDTO tiersDTOAutoComplete;
	
	private List<TaUtilisateur> selectedUtilisateurs;
	private List<TaTiers> selectedTiers;

	
	private boolean showParamQuandNotificationUtilisateur = false;
	private boolean showAvantNotificationUtilisateur = false;
	private boolean showDateNotificationUtilisateur = false;
	private boolean showDebutNotificationUtilisateur = false;
	private boolean showTexteNotificationUtilisateur = false;
	
	private boolean showParamQuandNotificationTiers = false;
	private boolean showAvantNotificationTiers = false;
	private boolean showDateNotificationTiers = false;
	private boolean showDebutNotificationTiers = false;
	private boolean showTexteNotificationTiers = false;

	/////
	private List<TaTypeNotificationDTO> listeTypeNotificationDTO;
	private List<TaTypeNotificationDTO> selectedTypeNotificationDTOUtilisateur;
	private List<TaTypeNotificationDTO> selectedTypeNotificationDTOTiers;
	
	//idem que dans controller, acceder aux const depuis le controleur
	final private String NOTIFICATION_AVANT_EVENEMENT = "NOTIFICATION_AVANT_EVENEMENT";
	final private String NOTIFICATION_DATE_PRECISE = "NOTIFICATION_DATE_PRECISE";
	final private String NOTIFICATION_DEBUT_EVENEMENT = "NOTIFICATION_DEBUT_EVENEMENT";
	
	final private String NOTIFICATION_DEBUT_NB_MINUTES = "NOTIFICATION_DEBUT_NB_MINUTES";
	final private String NOTIFICATION_DEBUT_NB_HEURES = "NOTIFICATION_DEBUT_NB_HEURES";
	final private String NOTIFICATION_DEBUT_NB_JOURS = "NOTIFICATION_DEBUT_NB_JOURS";
	final private String NOTIFICATION_DEBUT_NB_SEMAINES = "NOTIFICATION_DEBUT_NB_SEMAINES";
	
	private TaNotificationDTO selectedNotificationDTOUtilisateur;
	private TaNotificationDTO selectedNotificationDTOTiers;
	
	private TaNotification taNotificationUtilisateur;
	private TaNotification taNotificationTiers;
	
	private int nbXxxAvantEvenementUtilisateur;
	private int nbXxxAvantEvenementTiers;
	private String uniteNbXxxAvantEvenement;
	private String uniteNbXxxAvantEvenementTiers;
	private Date datePreciseNotificationUtilisateur;
	private Date datePreciseNotificationTiers;
	private String texteNotificationUtilisateur;
	private String texteNotificationTiers;
	
	private String selectedQuandNotificationUtilisateur = NOTIFICATION_DEBUT_EVENEMENT;
	private String selectedQuandNotificationTiers = NOTIFICATION_DEBUT_EVENEMENT;
	
	private final MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap(); 
	
	private EvenementParam param;
	
	private boolean noticationUtilisateurEtTiersIdentique = true;
	
	private boolean evenementCalendrierExterne = false;
	
	private @Inject GoogleBean g;
	
	private LgrDozerMapper<TaTypeNotificationDTO,TaTypeNotification> mapperUIToModelTypeNotification  = new LgrDozerMapper<TaTypeNotificationDTO,TaTypeNotification>();
	private LgrDozerMapper<TaTypeNotification,TaTypeNotificationDTO> mapperModelToUITypeNotification = new LgrDozerMapper<TaTypeNotification,TaTypeNotificationDTO>();
	
	
	public EvenementAgendaController() {  
		
	}  
	
	

	@PostConstruct
	public void postConstruct(){
		refresh();
	}
	
	public void refresh(){

		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		
		listeTypeNotificationDTO = taTypeNotificationService.selectAllDTO();
		Map<String, TaTypeNotificationDTO> mapCodeTypeNotification = new HashMap<>();
		for (TaTypeNotificationDTO taTypeNotificationDTO : listeTypeNotificationDTO) {
			mapCodeTypeNotification.put(taTypeNotificationDTO.getCodeTypeNotification(), taTypeNotificationDTO);
		}
		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		
		try {
			
			Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
			String modeEcranDefaut = params.get("modeEcranDefaut");
						
			if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
				actInsererEvenement(null);
				
				if(sessionMap.get(EvenementParam.NOM_OBJET_EN_SESSION)!=null) {
					param = (EvenementParam) sessionMap.get(EvenementParam.NOM_OBJET_EN_SESSION);
					sessionMap.remove(EvenementParam.NOM_OBJET_EN_SESSION);
				}
				
				if(param!=null) {
					event.getTaEvenement().setTaAgenda(param.getTaAgenda());
					event.getTaEvenement().setTaTypeEvenement(param.getTaTypeEvenement());
					event.getTaEvenement().setListeTiers(param.getListeTiers());
					event.getTaEvenement().setListeUtilisateur(param.getListeUtilisateurs());
					if(param.getListeDocuments()!=null && !param.getListeDocuments().isEmpty()) {
						
						if(event.getTaEvenement().getListeDocument()==null) {
							event.getTaEvenement().setListeDocument(new ArrayList<>());
						}
						TaRDocumentEvenement taRDocumentEvenement = new TaRDocumentEvenement();
						taRDocumentEvenement.affecteDocument(param.getListeDocuments().get(0));
						taRDocumentEvenement.setTaEvenement(event.getTaEvenement());
						event.getTaEvenement().getListeDocument().add(taRDocumentEvenement);
					}
					
					event.getTaEvenement().setLibelleEvenement(param.getTitre());
					event.getTaEvenement().setDescription(param.getDescription());
					
					event.getTaEvenement().setDateDebut(param.getDebut());
					event.getTaEvenement().setDateFin(param.getFin());
					event.getTaEvenement().setRecurrent(param.isRecurent());

					
					event.updateUI();
					
					if(param.getTaAgenda()!=null 
							&& (
									param.getTaAgenda().getOrigine()!=null
									|| param.getTaAgenda().getOrigine().equals(TaAgenda.AGENDA_BDG))
							) {
						evenementCalendrierExterne = true;
					}
					
				}
				
				
				
			} else if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_EDITION))) {
				
				actModifierEvenement(null);
		
				event = (LgrTabScheduleEvent) sessionMap.get("event");
				
				sessionMap.remove("event");
				
				if(event!=null) {
					autoCompleteMapDTOtoUI();
					
					if(event.getTaEvenement().getOrigine()!=null || event.getTaEvenement().getOrigine().equals(TaAgenda.AGENDA_BDG)) {
						evenementCalendrierExterne = true;
					}
					
					//NOTIFICATION UTILISATEUR
					selectedTypeNotificationDTOUtilisateur = new ArrayList<>();
					for (TaNotification taNotification : event.getTaEvenement().getTaNotificationUtilisateur()) {
						selectedTypeNotificationDTOUtilisateur.add(mapCodeTypeNotification.get(taNotification.getTaTypeNotification().getCodeTypeNotification()));
					} 
					if(event.getTaEvenement().getTaNotificationUtilisateur()!=null && !event.getTaEvenement().getTaNotificationUtilisateur().isEmpty()) {
						TaNotification taNotification = event.getTaEvenement().getTaNotificationUtilisateur().iterator().next();
						if(taNotification.getTimerHandle()!=null) {
							TimerHandle h = (TimerHandle) SerializationUtils.deserialize(taNotification.getTimerHandle());
							try {
								Timer t = h.getTimer();
								//t.cancel();
							} catch (NoSuchObjectLocalException ex) {
								System.out.println("Le timer pour cette notification n'existe pas/plus. Il a été annulé ou dejà executé.");
							}
						}
						if(taNotification.isAuDebutdeEvenement()) {
							selectedQuandNotificationUtilisateur = NOTIFICATION_DEBUT_EVENEMENT;
						} else if(taNotification.getNbMinutesAvantNotification()!=0 
								|| taNotification.getNbHeuresAvantNotification()!=0 
								|| taNotification.getNbJourAvantNotification()!=0
								|| taNotification.getNbsemainesAvantNotification()!=0) {
							selectedQuandNotificationUtilisateur = NOTIFICATION_AVANT_EVENEMENT;
							if(taNotification.getNbMinutesAvantNotification()!=0) {
								uniteNbXxxAvantEvenement = NOTIFICATION_DEBUT_NB_MINUTES;
								nbXxxAvantEvenementUtilisateur = taNotification.getNbMinutesAvantNotification();
							} else if(taNotification.getNbHeuresAvantNotification()!=0) {
								uniteNbXxxAvantEvenement = NOTIFICATION_DEBUT_NB_HEURES;
								nbXxxAvantEvenementUtilisateur = taNotification.getNbHeuresAvantNotification();
							} else if(taNotification.getNbJourAvantNotification()!=0) {
								uniteNbXxxAvantEvenement = NOTIFICATION_DEBUT_NB_JOURS;
								nbXxxAvantEvenementUtilisateur = taNotification.getNbJourAvantNotification();
							} else if(taNotification.getNbsemainesAvantNotification()!=0) {
								uniteNbXxxAvantEvenement = NOTIFICATION_DEBUT_NB_SEMAINES;
								nbXxxAvantEvenementUtilisateur = taNotification.getNbsemainesAvantNotification();
							}
							
						} else {
							selectedQuandNotificationUtilisateur = NOTIFICATION_DATE_PRECISE;
							datePreciseNotificationUtilisateur = taNotification.getDateNotification();
						}
						if(taNotification.getTexteNotification()!=null) {
							texteNotificationUtilisateur = taNotification.getTexteNotification();
						}
						if(taNotification.getListeUtilisateur()!=null && !taNotification.getListeUtilisateur().isEmpty()) {
							if(selectedUtilisateurs==null) {
								selectedUtilisateurs = new ArrayList<>();
							} else {
								selectedUtilisateurs.clear();
							}
							for (TaUtilisateur u : taNotification.getListeUtilisateur()) {
								selectedUtilisateurs.add(u);
							}
						}
					}
					
					//NOTIFICATION TIERS
					selectedTypeNotificationDTOTiers = new ArrayList<>();
					for (TaNotification taNotification : event.getTaEvenement().getTaNotificationTiers()) {
						selectedTypeNotificationDTOTiers.add(mapCodeTypeNotification.get(taNotification.getTaTypeNotification().getCodeTypeNotification()));
					} 
					if(event.getTaEvenement().getTaNotificationTiers()!=null && !event.getTaEvenement().getTaNotificationTiers().isEmpty()) {
						TaNotification taNotification = event.getTaEvenement().getTaNotificationTiers().iterator().next();
						if(taNotification.isAuDebutdeEvenement()) {
							selectedQuandNotificationTiers = NOTIFICATION_DEBUT_EVENEMENT;
						} else if(taNotification.getNbMinutesAvantNotification()!=0 
								|| taNotification.getNbHeuresAvantNotification()!=0 
								|| taNotification.getNbJourAvantNotification()!=0
								|| taNotification.getNbsemainesAvantNotification()!=0) {
							selectedQuandNotificationTiers = NOTIFICATION_AVANT_EVENEMENT;
							if(taNotification.getNbMinutesAvantNotification()!=0) {
								uniteNbXxxAvantEvenementTiers = NOTIFICATION_DEBUT_NB_MINUTES;
								nbXxxAvantEvenementTiers = taNotification.getNbMinutesAvantNotification();
							} else if(taNotification.getNbHeuresAvantNotification()!=0) {
								uniteNbXxxAvantEvenementTiers = NOTIFICATION_DEBUT_NB_HEURES;
								nbXxxAvantEvenementTiers = taNotification.getNbHeuresAvantNotification();
							} else if(taNotification.getNbJourAvantNotification()!=0) {
								uniteNbXxxAvantEvenementTiers = NOTIFICATION_DEBUT_NB_JOURS;
								nbXxxAvantEvenementTiers = taNotification.getNbJourAvantNotification();
							} else if(taNotification.getNbsemainesAvantNotification()!=0) {
								uniteNbXxxAvantEvenementTiers = NOTIFICATION_DEBUT_NB_SEMAINES;
								nbXxxAvantEvenementTiers = taNotification.getNbsemainesAvantNotification();
							}
							
						} else {
							selectedQuandNotificationTiers = NOTIFICATION_DATE_PRECISE;
							datePreciseNotificationTiers = taNotification.getDateNotification();
						}
						if(taNotification.getTexteNotification()!=null) {
							texteNotificationTiers = taNotification.getTexteNotification();
						}
						if(taNotification.getListeTiers()!=null && !taNotification.getListeTiers().isEmpty()) {
							if(selectedTiers==null) {
								selectedTiers = new ArrayList<>();
							} else {
								selectedTiers.clear();
							}
							for (TaTiers u : taNotification.getListeTiers()) {
								selectedTiers.add(u);
							}
						}
					}
					
					
					initAffichageNotification();
				}
				
			} 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	public void initAffichageNotification() {
		
		//UTILISATEUR
		showParamQuandNotificationUtilisateur = false;
		
		showAvantNotificationUtilisateur = false;
		showDateNotificationUtilisateur = false;
		showDebutNotificationUtilisateur = false;
		
		showTexteNotificationUtilisateur = false;
		
		if(selectedTypeNotificationDTOUtilisateur!=null && !selectedTypeNotificationDTOUtilisateur.isEmpty()) {
			showParamQuandNotificationUtilisateur = true;
		} 
		
		if(showParamQuandNotificationUtilisateur) {
		
			if(selectedQuandNotificationUtilisateur.equals(NOTIFICATION_AVANT_EVENEMENT)) {
				showAvantNotificationUtilisateur = true;
			} else if(selectedQuandNotificationUtilisateur.equals(NOTIFICATION_DATE_PRECISE)) {
				showDateNotificationUtilisateur = true;
				nbXxxAvantEvenementUtilisateur = 0;
			} else if(selectedQuandNotificationUtilisateur.equals(NOTIFICATION_DEBUT_EVENEMENT)) {
				showDebutNotificationUtilisateur = true;
				nbXxxAvantEvenementUtilisateur = 0;
			}
			
			if(showAvantNotificationUtilisateur || showDateNotificationUtilisateur || showDebutNotificationUtilisateur) {
				showTexteNotificationUtilisateur = true;
			}
		}
		
		//TIERS
		showParamQuandNotificationTiers = false;
		
		showAvantNotificationTiers = false;
		showDateNotificationTiers = false;
		showDebutNotificationTiers = false;
		
		showTexteNotificationTiers = false;
		
		if(selectedTypeNotificationDTOTiers!=null && !selectedTypeNotificationDTOTiers.isEmpty()) {
			showParamQuandNotificationTiers = true;
		} 
		
		if(showParamQuandNotificationTiers) {
		
			if(selectedQuandNotificationTiers.equals(NOTIFICATION_AVANT_EVENEMENT)) {
				showAvantNotificationTiers = true;
			} else if(selectedQuandNotificationTiers.equals(NOTIFICATION_DATE_PRECISE)) {
				showDateNotificationTiers = true;
				nbXxxAvantEvenementTiers = 0;
			} else if(selectedQuandNotificationTiers.equals(NOTIFICATION_DEBUT_EVENEMENT)) {
				showDebutNotificationTiers = true;
				nbXxxAvantEvenementTiers = 0;
			}
			
			if(showAvantNotificationTiers || showDateNotificationTiers || showDebutNotificationTiers) {
				showTexteNotificationTiers = true;
			}
		}

	}
	
	public void actModifierEvenement(ActionEvent e) {
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}
	
	public void actInsererEvenement(ActionEvent e) {
		event = new LgrTabScheduleEvent();
		event.setTaEvenement(new TaEvenement());
		taTypeEvenement = null;
		taAgenda = null;
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
		initAppelDialogue();
	}


	public void autoCompleteMapUIToDTO() {
//		if(taTEmailDTO!=null) {
//			validateUIField(Const.C_CODE_T_EMAIL,taTEmailDTO.getCodeTEmail());
//			selectedTaMessageEmailDTO.setCodeTTiers(taTEmailDTO.getCodeTEmail());
//		}else {
//			selectedTaMessageEmailDTO.setCodeTTiers(null);
//		}
//		
//		if(taEmailDTO!=null) {
//			validateUIField(Const.C_CODE_eT_ENTITE,taEmailDTO.getCodeTEntite());
//			selectedTaMessageEmailDTO.setCodeTEntite(taEmailDTO.getCodeTEntite());
//		}else {
//			selectedTaMessageEmailDTO.setCodeTEntite(null);
//		}
		
	}

	public void autoCompleteMapDTOtoUI() {
			try {
				taAgenda = null;
				taTypeEvenement = null;
			
				if(event!=null) {
					if(event.getTaEvenement()!=null && event.getTaEvenement().getTaAgenda()!=null /*&& !selectedTaAdresseDTO.getCodeTAdr().equals("")*/) {
			//			taTAdrDTO=taTAdresseService.findByCodeDTO(selectedTaAdresseDTO.getCodeTAdr());
						taAgenda = event.getTaEvenement().getTaAgenda();
					}
					if(event.getTaEvenement()!=null && event.getTaEvenement().getTaTypeEvenement()!=null /*&& !selectedTaAdresseDTO.getCodeTAdr().equals("")*/) {
						taTypeEvenement = event.getTaEvenement().getTaTypeEvenement();
					}
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void actEnregistrerEvenement(ActionEvent e) {
		try {
			//TaEvenement evt = new TaEvenement();
			
			String messageErreur = null;

			event.updateModel();
			
			if(event.getOrigine()==null 
					|| !event.getOrigine().equals(TaAgenda.AGENDA_GOOGLE_CALENDAR)) {
				//evenement BDG
			
				
				if(event.getTaEvenement().getProprietaire()==null) {
					event.getTaEvenement().setProprietaire(taUtilisateurService.findById(sessionInfo.getUtilisateur().getId()));
				}
				
				if(taAgenda!=null) {
					event.getTaEvenement().setTaAgenda(taAgenda);
				} else {
					messageErreur = "L'agenda est obligatoire";
				}
				
				if(event.getTaEvenement().getLibelleEvenement()==null || event.getTaEvenement().getLibelleEvenement().equals("")) {
					messageErreur = "Le titre est obligatoire";
				}
				if(event.getTaEvenement().getDateDebut()==null) {
					messageErreur = "La date début est obligatoire";
				}
				
				if(taTypeEvenement!=null) {
					event.getTaEvenement().setTaTypeEvenement(taTypeEvenement);
				}
				
				if(event.getTaEvenement().getDateDebut()==null) {
					event.getTaEvenement().setDateDebut(new Date()); // ou lever une exception
				}
				if(event.getTaEvenement().getDateFin()==null) {
					event.getTaEvenement().setDateFin(event.getTaEvenement().getDateDebut());
				}
				
				//gestion des notifications utilisateur
				if(selectedTypeNotificationDTOUtilisateur!=null && !selectedTypeNotificationDTOUtilisateur.isEmpty()) {
					
					for (TaTypeNotificationDTO taTypeNotificationDTO : selectedTypeNotificationDTOUtilisateur) {
						
						TaNotification notif = null;
						
						if(event.getTaEvenement().getTaNotificationUtilisateur()==null || event.getTaEvenement().getTaNotificationUtilisateur().isEmpty()) {
							event.getTaEvenement().setTaNotificationUtilisateur(new HashSet<>());
						} else {
							//chercher une notification de ce type deja existante pour la mettre a jour ou en creer une nouvell
							for (TaNotification taNotification : event.getTaEvenement().getTaNotificationUtilisateur()) {
								if(taNotification.getTaTypeNotification().getCodeTypeNotification().equals(taTypeNotificationDTO.getCodeTypeNotification())) {
									notif = taNotification;
								}
							}
						}
						if(notif==null) {
							notif = new TaNotification();
							notif.setTaEvenementUtilisateur(event.getTaEvenement());
						}
						
						notif.setTaTypeNotification(taTypeNotificationService.findByCode(taTypeNotificationDTO.getCodeTypeNotification()));
						notif.setDateNotification(event.getTaEvenement().getDateDebut());
						notif.setTexteNotification(texteNotificationUtilisateur);
						
						if(notif.getListeUtilisateur()==null) {
							notif.setListeUtilisateur(new ArrayList<>());
						}
						notif.getListeUtilisateur().clear();
						notif.getListeUtilisateur().add(sessionInfo.getUtilisateur());
						//TODO ajouter une boucle sur les utilisateur sélectionnés
						if(selectedUtilisateurs!=null && !selectedUtilisateurs.isEmpty()) {
							for (TaUtilisateur utilisateur : selectedUtilisateurs) {
								notif.getListeUtilisateur().add(utilisateur);
							}
						}
						
						if(selectedQuandNotificationUtilisateur.equals(NOTIFICATION_AVANT_EVENEMENT)) {
							notif.setAuDebutdeEvenement(false);
							 LocalDateTime localDateTime = event.getTaEvenement().getDateDebut().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
							if(uniteNbXxxAvantEvenement.equals(NOTIFICATION_DEBUT_NB_MINUTES)) {
								notif.setNbMinutesAvantNotification(nbXxxAvantEvenementUtilisateur);
							    localDateTime = localDateTime.minusMinutes(nbXxxAvantEvenementUtilisateur);     
							} else if(uniteNbXxxAvantEvenement.equals(NOTIFICATION_DEBUT_NB_HEURES)) {
								notif.setNbHeuresAvantNotification(nbXxxAvantEvenementUtilisateur);
								 localDateTime = localDateTime.minusHours(nbXxxAvantEvenementUtilisateur);     
							} else if(uniteNbXxxAvantEvenement.equals(NOTIFICATION_DEBUT_NB_JOURS)) {
								notif.setNbJourAvantNotification(nbXxxAvantEvenementUtilisateur);
								localDateTime = localDateTime.minusDays(nbXxxAvantEvenementUtilisateur);     
							} else if(uniteNbXxxAvantEvenement.equals(NOTIFICATION_DEBUT_NB_SEMAINES)) {
								notif.setNbsemainesAvantNotification(nbXxxAvantEvenementUtilisateur);
								localDateTime = localDateTime.minusWeeks(nbXxxAvantEvenementUtilisateur);     
							}
							Date dateNotifCalculee = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
							notif.setDateNotification(dateNotifCalculee);
						} else if(selectedQuandNotificationUtilisateur.equals(NOTIFICATION_DATE_PRECISE)) {
							notif.setAuDebutdeEvenement(false);
							notif.setNbMinutesAvantNotification(0);
							notif.setNbHeuresAvantNotification(0);
							notif.setNbJourAvantNotification(0);
							notif.setNbsemainesAvantNotification(0);
						} else if(selectedQuandNotificationUtilisateur.equals(NOTIFICATION_DEBUT_EVENEMENT)) {
							notif.setAuDebutdeEvenement(true);
							notif.setNbMinutesAvantNotification(0);
							notif.setNbHeuresAvantNotification(0);
							notif.setNbJourAvantNotification(0);
							notif.setNbsemainesAvantNotification(0);
						}
						
						event.getTaEvenement().getTaNotificationUtilisateur().add(notif);
					}
				} else {
					//aucune notification utilisateur
					//effacer celles qui existent
					if(event.getTaEvenement().getTaNotificationUtilisateur()!=null) {
						for (TaNotification n : event.getTaEvenement().getTaNotificationUtilisateur()) {
							taNotificationService.remove(n);
						}
						event.getTaEvenement().getTaNotificationUtilisateur().clear();
					}
				}
					
				//gestion des notifications tiers
				if(selectedTypeNotificationDTOTiers!=null && !selectedTypeNotificationDTOTiers.isEmpty()) {
	
						for (TaTypeNotificationDTO taTypeNotificationDTO : selectedTypeNotificationDTOTiers) {
							
							TaNotification notif = null;
							
							if(event.getTaEvenement().getTaNotificationTiers()==null || event.getTaEvenement().getTaNotificationTiers().isEmpty()) {
								event.getTaEvenement().setTaNotificationTiers(new HashSet<>());
							} else {
								//chercher une notification de ce type deja existante pour la mettre a jour ou en creer une nouvell
								for (TaNotification taNotification : event.getTaEvenement().getTaNotificationTiers()) {
									if(taNotification.getTaTypeNotification().getCodeTypeNotification().equals(taTypeNotificationDTO.getCodeTypeNotification())) {
										notif = taNotification;
									}
								}
							}
							if(notif==null) {
								notif = new TaNotification();
								notif.setTaEvenementTiers(event.getTaEvenement());
							}
							
							notif.setTaTypeNotification(taTypeNotificationService.findByCode(taTypeNotificationDTO.getCodeTypeNotification()));
							notif.setDateNotification(event.getTaEvenement().getDateDebut());
							notif.setTexteNotification(texteNotificationTiers);
							
							if(notif.getListeTiers()==null) {
								notif.setListeTiers(new ArrayList<>());
							}
							notif.getListeTiers().clear();
							if(selectedTiers!=null && !selectedTiers.isEmpty()) {
								for (TaTiers t : selectedTiers) {
									notif.getListeTiers().add(t);
								}
							}
							
							if(selectedQuandNotificationTiers.equals(NOTIFICATION_AVANT_EVENEMENT)) {
								notif.setAuDebutdeEvenement(false);
								LocalDateTime localDateTime = event.getTaEvenement().getDateDebut().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
								if(uniteNbXxxAvantEvenementTiers.equals(NOTIFICATION_DEBUT_NB_MINUTES)) {
									notif.setNbMinutesAvantNotification(nbXxxAvantEvenementTiers);
								    localDateTime = localDateTime.minusMinutes(nbXxxAvantEvenementTiers);     
								} else if(uniteNbXxxAvantEvenementTiers.equals(NOTIFICATION_DEBUT_NB_HEURES)) {
									notif.setNbHeuresAvantNotification(nbXxxAvantEvenementTiers);
									localDateTime = localDateTime.minusHours(nbXxxAvantEvenementTiers);     
								} else if(uniteNbXxxAvantEvenementTiers.equals(NOTIFICATION_DEBUT_NB_JOURS)) {
									notif.setNbJourAvantNotification(nbXxxAvantEvenementTiers);
									localDateTime = localDateTime.minusDays(nbXxxAvantEvenementTiers);     
								} else if(uniteNbXxxAvantEvenementTiers.equals(NOTIFICATION_DEBUT_NB_SEMAINES)) {
									notif.setNbsemainesAvantNotification(nbXxxAvantEvenementTiers);
									localDateTime = localDateTime.minusWeeks(nbXxxAvantEvenementTiers);     
								}
								Date dateNotifCalculee = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
								notif.setDateNotification(dateNotifCalculee);
							} else if(selectedQuandNotificationTiers.equals(NOTIFICATION_DATE_PRECISE)) {
								notif.setAuDebutdeEvenement(false);
								notif.setNbMinutesAvantNotification(0);
								notif.setNbHeuresAvantNotification(0);
								notif.setNbJourAvantNotification(0);
								notif.setNbsemainesAvantNotification(0);
							} else if(selectedQuandNotificationTiers.equals(NOTIFICATION_DEBUT_EVENEMENT)) {
								notif.setAuDebutdeEvenement(true);
								notif.setNbMinutesAvantNotification(0);
								notif.setNbHeuresAvantNotification(0);
								notif.setNbJourAvantNotification(0);
								notif.setNbsemainesAvantNotification(0);
							}
							
							event.getTaEvenement().getTaNotificationTiers().add(notif);
						}
						
				} else {
					//aucune notification utilisateur
					//effacer celles qui existent
					if(event.getTaEvenement().getTaNotificationTiers()!=null) {
						for (TaNotification n : event.getTaEvenement().getTaNotificationTiers()) {
							taNotificationService.remove(n);
						}
						event.getTaEvenement().getTaNotificationTiers().clear();
					}
				}
	
				if(messageErreur==null || messageErreur.equals("") ) {
	
					event.setTaEvenement(taEvenementService.merge(event.getTaEvenement()));
					actFermerDialog(null);
					
				} else {
					FacesContext context = FacesContext.getCurrentInstance();  
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Agenda", messageErreur)); 
				}
			} else if(event.getOrigine()!=null && event.getOrigine().equals(TaAgenda.AGENDA_GOOGLE_CALENDAR)) {
				//evenement google calendar, on ecrit chez google via l'API
				if(g.validToken()) {
					System.out.println("Creation et mise à jour d'évenement google calendar désactivé pour l'instant");
					//g.updateEvent(event, null);
					actFermerDialog(null);
				}
			}
			
		} catch (FinderException e1) {
			e1.printStackTrace();
		} catch (RemoveException e1) {
			e1.printStackTrace();
		}
	}
	
	public void actSupprimerEvenement(ActionEvent e) {
		try {
			taEvenementService.remove(event.getTaEvenement());
			//initCalendrier(true);
			actFermerDialog(null);
		} catch (RemoveException e1) {
			e1.printStackTrace();
		}
	}
	
	public void actSupprimerUtilisateurListe(TaUtilisateur u) {
		event.getTaEvenement().getListeUtilisateur().remove(u);
	}
	
	public void actSupprimerTiersListe(TaTiers u) {
		event.getTaEvenement().getListeTiers().remove(u);
	}
	
	public void actSupprimerDocumentListe(TaRDocumentEvenement u) {
		event.getTaEvenement().getListeDocument().remove(u);
	}
	
	public List<TaTypeEvenement> typeEvenementAutoComplete(String query) {
        List<TaTypeEvenement> allValues = taTypeEvenementService.selectAll();
        List<TaTypeEvenement> filteredValues = new ArrayList<TaTypeEvenement>();
        
        TaTypeEvenement civ = new TaTypeEvenement();
        civ.setCodeTypeEvenement(Const.C_AUCUN);
        filteredValues.add(civ);
        for (int i = 0; i < allValues.size(); i++) {
        	 civ = allValues.get(i);
            if(query.equals("*") || civ.getCodeTypeEvenement().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(civ);
            }
        }
        return filteredValues;
    }
	
	public List<TaAgenda> agendaAutoComplete(String query) {
        List<TaAgenda> allValues = taAgendaService.selectAll();
        List<TaAgenda> filteredValues = new ArrayList<TaAgenda>();
        
        TaAgenda civ = new TaAgenda();
        civ.setNom(Const.C_AUCUN);
        filteredValues.add(civ);
        for (int i = 0; i < allValues.size(); i++) {
        	 civ = allValues.get(i);
            if(query.equals("*") || civ.getNom().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(civ);
            }
        }
        return filteredValues;
    }
	
	public List<TaUtilisateurDTO> utilisateurAutoCompleteDTOLight(String query) {
//		List<TaUtilisateurDTO> allValues = utilisateurService.findByCodeLight(query);
		List<TaUtilisateurDTO> allValues = taUtilisateurService.selectAllDTO();
		List<TaUtilisateurDTO> filteredValues = new ArrayList<TaUtilisateurDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			TaUtilisateurDTO civ = allValues.get(i);
			if(query.equals("*") || civ.getEmail()!=null && civ.getEmail().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	
	public List<TaUtilisateur> utilisateurAutoComplete(String query) {
		//List<TaUtilisateur> allValues = utilisateurService.findByCode(query);
		List<TaUtilisateur> allValues = taUtilisateurService.selectAll();
		List<TaUtilisateur> filteredValues = new ArrayList<TaUtilisateur>();

		for (int i = 0; i < allValues.size(); i++) {
			TaUtilisateur civ = allValues.get(i);
//			if(query.equals("*") || civ.getEmail().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
//			}
		}

		return filteredValues;
	}
	
	public List<TaTiers> tiersAutoComplete(String query) {
		List<TaTiers> allValues = taTiersService.selectAll();
		List<TaTiers> filteredValues = new ArrayList<TaTiers>();

		for (int i = 0; i < allValues.size(); i++) {
			TaTiers civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeTiers().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
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
	
	public void actFermerDialog(ActionEvent actionEvent) {
		//PrimeFaces.current().dialog().closeDynamic(null);
		
		//PrimeFaces.current().dialog().closeDynamic(listeControle);
		PrimeFaces.current().dialog().closeDynamic(null);
	}

	public void actFermer(ActionEvent actionEvent) {

		switch (modeEcran.getMode()) {
		case C_MO_INSERTION:
			//actAnnuler(actionEvent);
			break;
		case C_MO_EDITION:
			//actAnnuler(actionEvent);							
			break;

		default:
			break;
		}
//		selectedTaMessageEmailDTO=new TaMessageEmailDTO();
//		selectedTaMessageEmailDTOs=new TaMessageEmailDTO[]{selectedTaMessageEmailDTO};

		
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
				break;
			case "supprimerListe":retour = false;break;	
			case "supprimer":
			case "modifier":
			case "imprimer":
				//if(selectedTaMessageEmailDTO!=null && selectedTaMessageEmailDTO.getId()!=null  && selectedTaMessageEmailDTO.getId()!=0 ) retour = false;
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



	public TabListModelBean getTabsCenter() {
		return tabsCenter;
	}

	public void setTabsCenter(TabListModelBean tabsCenter) {
		this.tabsCenter = tabsCenter;
	}

	public String getParamId() {
		return paramId;
	}

	public void setParamId(String paramId) {
		this.paramId = paramId;
	} 

	public void validateTiers(FacesContext context, UIComponent component, Object value) throws ValidatorException {

		String messageComplet = "";

		try {
			//String selectedRadio = (String) value;

			String nomChamp =  (String) component.getAttributes().get("nomChamp");

			//msg = "Mon message d'erreur pour : "+nomChamp;

			//			  validateUIField(nomChamp,value);
			//			  TaMessageEmailDTO dtoTemp=new TaMessageEmailDTO();
			//			  PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
			//			  taTiersService.validateDTOProperty(dtoTemp, nomChamp, ITaMessageEmailServiceRemote.validationContext );

			//				//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<TaMessageEmailDTO>> violations = factory.getValidator().validateValue(TaMessageEmailDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				//					List<IStatus> statusList = new ArrayList<IStatus>();
				for (ConstraintViolation<TaMessageEmailDTO> cv : violations) {
					//						statusList.add(ValidationStatus.error(cv.getMessage()));
					messageComplet+=" "+cv.getMessage()+"\n";
				}
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
				//					return new MultiStatus(LibrairiesEcranPlugin.PLUGIN_ID, IStatus.ERROR,
				//							//statusList.toArray(new IStatus[statusList.size()]), "Validation errors", null);
				//							statusList.toArray(new IStatus[statusList.size()]), messageComplet, null);
			} else {
				//aucune erreur de validation "automatique", on déclanche les validations du controller
				//					if(controller!=null) {
				validateUIField(nomChamp,value);
				//						if(!s.isOK()) {
				//							return s;
				//						}
				//					}
			}
			//				return ValidationStatus.ok();

			//selectedTaMessageEmailDTO.setAdresse1Adresse("abcd");

			//			  if(selectedRadio.equals("aa")) {
			//				  throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
			//			  }

		} catch(Exception e) {
			//messageComplet += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
		}
	}

	public void autcompleteSelection(SelectEvent event) {
		Object value = event.getObject();
		//			FacesMessage msg = new FacesMessage("Selected", "Item:" + value);

		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");

		if(value!=null) {
			if(value instanceof String && value.equals(Const.C_AUCUN))value="";	
			if(value instanceof TaTEntiteDTO && ((TaTEntiteDTO) value).getCodeTEntite()!=null && ((TaTEntiteDTO) value).getCodeTEntite().equals(Const.C_AUCUN))value=null;	
			if(value instanceof TaTCiviliteDTO && ((TaTCiviliteDTO) value).getCodeTCivilite()!=null && ((TaTCiviliteDTO) value).getCodeTCivilite().equals(Const.C_AUCUN))value=null;
			if(value instanceof TaCPaiementDTO && ((TaCPaiementDTO) value).getCodeCPaiement()!=null && ((TaCPaiementDTO) value).getCodeCPaiement().equals(Const.C_AUCUN))value=null;
			if(value instanceof TaTPaiementDTO && ((TaTPaiementDTO) value).getCodeTPaiement()!=null && ((TaTPaiementDTO) value).getCodeTPaiement().equals(Const.C_AUCUN))value=null;
			//if(value instanceof TaTTiersDTO && ((TaTTiersDTO) value).getCodeTTiers()!=null && ((TaTTiersDTO) value).getCodeTTiers().equals(Const.C_AUCUN))value=null;
			if(value instanceof TaCPaiement && ((TaCPaiement) value).getCodeCPaiement()!=null && ((TaCPaiement) value).getCodeCPaiement().equals(Const.C_AUCUN))value=null;
			if(value instanceof TaTPaiement && ((TaTPaiement) value).getCodeTPaiement()!=null && ((TaTPaiement) value).getCodeTPaiement().equals(Const.C_AUCUN))value=null;
			if(value instanceof TaTTvaDoc && ((TaTTvaDoc) value).getCodeTTvaDoc()!=null && ((TaTTvaDoc) value).getCodeTTvaDoc().equals(Const.C_AUCUN))value=null;
			if(value instanceof TaFamilleTiers && ((TaFamilleTiers) value).getCodeFamille()!=null && ((TaFamilleTiers) value).getCodeFamille().equals(Const.C_AUCUN))value=null;
		}

		validateUIField(nomChamp,value);
	}
	
	public void autcompleteUnSelect(UnselectEvent event) {
		Object value = event.getObject();
		
		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");

		if(value!=null) {
			if(value instanceof String && value.equals(Const.C_AUCUN))value="";	
			if(value instanceof TaFamilleDTO && ((TaFamilleDTO) value).getCodeFamille()!=null && ((TaFamilleDTO) value).getCodeFamille().equals(Const.C_AUCUN))value=null;	
			if(value instanceof TaUniteDTO && ((TaUniteDTO) value).getCodeUnite()!=null && ((TaUniteDTO) value).getCodeUnite().equals(Const.C_AUCUN))value=null;
			if(value instanceof TaTvaDTO && ((TaTvaDTO) value).getCodeTva()!=null && ((TaTvaDTO) value).getCodeTva().equals(Const.C_AUCUN))value=null;
			if(value instanceof TaTTvaDTO && ((TaTTvaDTO) value).getCodeTTva()!=null && ((TaTTvaDTO) value).getCodeTTva().equals(Const.C_AUCUN))value=null;	
			if(value instanceof TaMarqueArticle && ((TaMarqueArticle) value).getCodeMarqueArticle()!=null && ((TaMarqueArticle) value).getCodeMarqueArticle().equals(Const.C_AUCUN))value=null;	
		}
		
//		//validateUIField(nomChamp,value);
//		try {
//			if(nomChamp.equals(Const.C_ADRESSE_EMAIL+"_DESTINATAIRE")) {
//				TaEmail entity =null;
//				if(value!=null){
//					if(value instanceof TaEmailDTO){
//	//				entity=(TaFamille) value;
//						entity = taEmailService.findByCode(((TaFamilleDTO)value).getCodeFamille());
//					}else{
//						entity = taEmailService.findByCode((String)value);
//					}
//				}else{
//					selectedTaArticleDTO.setCodeFamille("");
//				}
//				//taArticle.setTaFamille(entity);
//				for (TaEmail f : taMessageEmail.getDestinataires()) {
//					if(f.getIdEmail()==((TaEmailDTO)value).getId())
//						entity = f;
//				}
//				taMessageEmail.getDestinataires().remove(entity);
//				if(taArticle.getTaFamille()!=null && taArticle.getTaFamille().getCodeFamille().equals(entity.getCodeFamille())) {
//					taArticle.setTaFamille(null);
//					taFamilleDTO = null;
//					if(!taArticle.getTaFamilles().isEmpty()) {
//						taArticle.setTaFamille(taArticle.getTaFamilles().iterator().next());
//						//taFamilleDTO = null;
//					}
//				}
//			}
//
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
	
	}

	public boolean validateUIField(String nomChamp,Object value) {

		boolean changement=false;

		try {	
			if(nomChamp.equals(constChampUtilisateurAcAgenda)) {
				 TaUtilisateur u = taUtilisateurService.findById(utilisateurDTOAutoComplete.getId());
				 if(event.getTaEvenement().getListeUtilisateur()==null) {
					 event.getTaEvenement().setListeUtilisateur(new ArrayList<>());
				 }
				 event.getTaEvenement().getListeUtilisateur().add(u);
				 if(selectedUtilisateurs==null) {
					 selectedUtilisateurs = new ArrayList<>();
				 }
				 selectedUtilisateurs.add(u);
			} else if(nomChamp.equals(constChampTiersAcAgenda)) {
				 TaTiers u = taTiersService.findById(tiersDTOAutoComplete.getId());
				 if(event.getTaEvenement().getListeTiers()==null) {
					 event.getTaEvenement().setListeTiers(new ArrayList<>());
				 }
				 event.getTaEvenement().getListeTiers().add(u);
				 if(selectedTiers==null) {
					 selectedTiers = new ArrayList<>();
				 }
				 selectedTiers.add(u);
			}
			
			

			return false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	

	public TabViewsBean getTabViews() {
		return tabViews;
	}

	public void setTabViews(TabViewsBean tabViews) {
		this.tabViews = tabViews;
	}

	public ModeObjetEcranServeur getModeEcran() {
		return modeEcran;
	}

	public void setModeEcran(ModeObjetEcranServeur modeEcran) {
		this.modeEcran = modeEcran;
	}

	public TaPieceJointeEmailDTO getSelectedTaPieceJointeEmailDTO() {
		return selectedTaPieceJointeEmailDTO;
	}

	public void setSelectedTaPieceJointeEmailDTO(TaPieceJointeEmailDTO selectedTaPieceJointeEmailDTO) {
		this.selectedTaPieceJointeEmailDTO = selectedTaPieceJointeEmailDTO;
	}
	
	public void actDialogEmail(ActionEvent actionEvent) {
	    
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
		
//		PaiementParam email = new PaiementParam();
//		email.setEmailExpediteur(null);
//		email.setNomExpediteur(taInfoEntrepriseService.findInstance().getNomInfoEntreprise()); 
//		email.setSubject(taInfoEntrepriseService.findInstance().getNomInfoEntreprise()+" Facture "+masterEntity.getCodeDocument()); 
//		email.setBodyPlain("Votre facture "+masterEntity.getCodeDocument());
//		email.setBodyHtml("Votre facture "+masterEntity.getCodeDocument());
//		email.setDestinataires(new String[]{masterEntity.getTaTiers().getTaEmail().getAdresseEmail()});
//		email.setEmailDestinataires(new TaEmail[]{masterEntity.getTaTiers().getTaEmail()});
		
//		EmailPieceJointeParam pj1 = new EmailPieceJointeParam();
//		pj1.setFichier(new File(((ITaFactureServiceRemote)taDocumentService).generePDF(masterEntity.getIdDocument())));
//		pj1.setTypeMime("pdf");
//		pj1.setNomFichier(pj1.getFichier().getName());
//		email.setPiecesJointes(
//				new EmailPieceJointeParam[]{pj1}
//				);
		
//		sessionMap.put("email", email);
        
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

	public TaAgenda getTaAgenda() {
		return taAgenda;
	}

	public void setTaAgenda(TaAgenda taAgenda) {
		this.taAgenda = taAgenda;
	}

	public TaTypeEvenement getTaTypeEvenement() {
		return taTypeEvenement;
	}

	public void setTaTypeEvenement(TaTypeEvenement taTypeEvenement) {
		this.taTypeEvenement = taTypeEvenement;
	}

	public LgrTabScheduleEvent getEvent() {
		return event;
	}

	public void setEvent(LgrTabScheduleEvent event) {
		this.event = event;
	}

	public SessionInfo getSessionInfo() {
		return sessionInfo;
	}

	public void setSessionInfo(SessionInfo sessionInfo) {
		this.sessionInfo = sessionInfo;
	}

	public TaUtilisateurDTO getUtilisateurDTOAutoComplete() {
		return utilisateurDTOAutoComplete;
	}

	public void setUtilisateurDTOAutoComplete(TaUtilisateurDTO utilisateurDTOAutoComplete) {
		this.utilisateurDTOAutoComplete = utilisateurDTOAutoComplete;
	}

	public static String getConstchamputilisateuracagenda() {
		return constChampUtilisateurAcAgenda;
	}

	public static String getConstchamptiersacagenda() {
		return constChampTiersAcAgenda;
	}

	public static String getConstchampdocacagenda() {
		return constChampDocAcAgenda;
	}

	public List<TaTypeNotificationDTO> getSelectedTypeNotificationDTOUtilisateur() {
		return selectedTypeNotificationDTOUtilisateur;
	}

	public void setSelectedTypeNotificationDTOUtilisateur(
			List<TaTypeNotificationDTO> selectedTypeNotificationDTOUtilisateur) {
		this.selectedTypeNotificationDTOUtilisateur = selectedTypeNotificationDTOUtilisateur;
	}

	public List<TaTypeNotificationDTO> getSelectedTypeNotificationDTOTiers() {
		return selectedTypeNotificationDTOTiers;
	}

	public void setSelectedTypeNotificationDTOTiers(List<TaTypeNotificationDTO> selectedTypeNotificationDTOTiers) {
		this.selectedTypeNotificationDTOTiers = selectedTypeNotificationDTOTiers;
	}

	public TaNotificationDTO getSelectedNotificationDTOUtilisateur() {
		return selectedNotificationDTOUtilisateur;
	}

	public void setSelectedNotificationDTOUtilisateur(TaNotificationDTO selectedNotificationDTOUtilisateur) {
		this.selectedNotificationDTOUtilisateur = selectedNotificationDTOUtilisateur;
	}

	public TaNotificationDTO getSelectedNotificationDTOTiers() {
		return selectedNotificationDTOTiers;
	}

	public void setSelectedNotificationDTOTiers(TaNotificationDTO selectedNotificationDTOTiers) {
		this.selectedNotificationDTOTiers = selectedNotificationDTOTiers;
	}

	public List<TaTypeNotificationDTO> getListeTypeNotificationDTO() {
		return listeTypeNotificationDTO;
	}

	public void setListeTypeNotificationDTO(List<TaTypeNotificationDTO> listeTypeNotificationDTO) {
		this.listeTypeNotificationDTO = listeTypeNotificationDTO;
	}

	public TaTiersDTO getTiersDTOAutoComplete() {
		return tiersDTOAutoComplete;
	}

	public void setTiersDTOAutoComplete(TaTiersDTO tiersDTOAutoComplete) {
		this.tiersDTOAutoComplete = tiersDTOAutoComplete;
	}

	public boolean isShowParamQuandNotificationUtilisateur() {
		return showParamQuandNotificationUtilisateur;
	}

	public void setShowParamQuandNotificationUtilisateur(boolean showParamQuandNotificationUtilisateur) {
		this.showParamQuandNotificationUtilisateur = showParamQuandNotificationUtilisateur;
	}

	public boolean isShowAvantNotificationUtilisateur() {
		return showAvantNotificationUtilisateur;
	}

	public void setShowAvantNotificationUtilisateur(boolean showAvantNotificationUtilisateur) {
		this.showAvantNotificationUtilisateur = showAvantNotificationUtilisateur;
	}

	public boolean isShowDateNotificationUtilisateur() {
		return showDateNotificationUtilisateur;
	}

	public void setShowDateNotificationUtilisateur(boolean showDateNotificationUtilisateur) {
		this.showDateNotificationUtilisateur = showDateNotificationUtilisateur;
	}

	public boolean isShowDebutNotificationUtilisateur() {
		return showDebutNotificationUtilisateur;
	}

	public void setShowDebutNotificationUtilisateur(boolean showDebutNotificationUtilisateur) {
		this.showDebutNotificationUtilisateur = showDebutNotificationUtilisateur;
	}

	public boolean isShowTexteNotificationUtilisateur() {
		return showTexteNotificationUtilisateur;
	}

	public void setShowTexteNotificationUtilisateur(boolean showTexteNotificationUtilisateur) {
		this.showTexteNotificationUtilisateur = showTexteNotificationUtilisateur;
	}

	public String getSelectedQuandNotificationUtilisateur() {
		return selectedQuandNotificationUtilisateur;
	}

	public void setSelectedQuandNotificationUtilisateur(String selectedQuandNotification) {
		this.selectedQuandNotificationUtilisateur = selectedQuandNotification;
	}

	public TaNotification getTaNotificationUtilisateur() {
		return taNotificationUtilisateur;
	}

	public void setTaNotificationUtilisateur(TaNotification taNotificationUtilisateur) {
		this.taNotificationUtilisateur = taNotificationUtilisateur;
	}

	public TaNotification getTaNotificationTiers() {
		return taNotificationTiers;
	}

	public void setTaNotificationTiers(TaNotification taNotificationTiers) {
		this.taNotificationTiers = taNotificationTiers;
	}

	public boolean isNoticationUtilisateurEtTiersIdentique() {
		return noticationUtilisateurEtTiersIdentique;
	}

	public void setNoticationUtilisateurEtTiersIdentique(boolean noticationUtilisateurEtTiersIdentique) {
		this.noticationUtilisateurEtTiersIdentique = noticationUtilisateurEtTiersIdentique;
	}

	public int getNbXxxAvantEvenementUtilisateur() {
		return nbXxxAvantEvenementUtilisateur;
	}

	public void setNbXxxAvantEvenementUtilisateur(int nbXxxAvantEvenementUtilisateur) {
		this.nbXxxAvantEvenementUtilisateur = nbXxxAvantEvenementUtilisateur;
	}

	public String getUniteNbXxxAvantEvenement() {
		return uniteNbXxxAvantEvenement;
	}

	public void setUniteNbXxxAvantEvenement(String uniteNbXxxAvantEvenement) {
		this.uniteNbXxxAvantEvenement = uniteNbXxxAvantEvenement;
	}

	public Date getDatePreciseNotificationUtilisateur() {
		return datePreciseNotificationUtilisateur;
	}

	public void setDatePreciseNotificationUtilisateur(Date datePreciseNotificationUtilisateur) {
		this.datePreciseNotificationUtilisateur = datePreciseNotificationUtilisateur;
	}

	public String getTexteNotificationUtilisateur() {
		return texteNotificationUtilisateur;
	}

	public void setTexteNotificationUtilisateur(String texteNotificationUtilisateur) {
		this.texteNotificationUtilisateur = texteNotificationUtilisateur;
	}

	public List<TaUtilisateur> getSelectedUtilisateurs() {
		return selectedUtilisateurs;
	}

	public void setSelectedUtilisateurs(List<TaUtilisateur> selectedUtilisateurs) {
		this.selectedUtilisateurs = selectedUtilisateurs;
	}

	public int getNbXxxAvantEvenementTiers() {
		return nbXxxAvantEvenementTiers;
	}

	public void setNbXxxAvantEvenementTiers(int nbXxxAvantEvenementTiers) {
		this.nbXxxAvantEvenementTiers = nbXxxAvantEvenementTiers;
	}

	public String getUniteNbXxxAvantEvenementTiers() {
		return uniteNbXxxAvantEvenementTiers;
	}

	public void setUniteNbXxxAvantEvenementTiers(String uniteNbXxxAvantEvenementTiers) {
		this.uniteNbXxxAvantEvenementTiers = uniteNbXxxAvantEvenementTiers;
	}

	public Date getDatePreciseNotificationTiers() {
		return datePreciseNotificationTiers;
	}

	public void setDatePreciseNotificationTiers(Date datePreciseNotificationTiers) {
		this.datePreciseNotificationTiers = datePreciseNotificationTiers;
	}

	public String getTexteNotificationTiers() {
		return texteNotificationTiers;
	}

	public void setTexteNotificationTiers(String texteNotificationTiers) {
		this.texteNotificationTiers = texteNotificationTiers;
	}

	public String getSelectedQuandNotificationTiers() {
		return selectedQuandNotificationTiers;
	}

	public void setSelectedQuandNotificationTiers(String selectedQuandNotificationTiers) {
		this.selectedQuandNotificationTiers = selectedQuandNotificationTiers;
	}

	public List<TaTiers> getSelectedTiers() {
		return selectedTiers;
	}

	public void setSelectedTiers(List<TaTiers> selectedTiers) {
		this.selectedTiers = selectedTiers;
	}

	public boolean isShowParamQuandNotificationTiers() {
		return showParamQuandNotificationTiers;
	}

	public void setShowParamQuandNotificationTiers(boolean showParamQuandNotificationTiers) {
		this.showParamQuandNotificationTiers = showParamQuandNotificationTiers;
	}

	public boolean isShowAvantNotificationTiers() {
		return showAvantNotificationTiers;
	}

	public void setShowAvantNotificationTiers(boolean showAvantNotificationTiers) {
		this.showAvantNotificationTiers = showAvantNotificationTiers;
	}

	public boolean isShowDateNotificationTiers() {
		return showDateNotificationTiers;
	}

	public void setShowDateNotificationTiers(boolean showDateNotificationTiers) {
		this.showDateNotificationTiers = showDateNotificationTiers;
	}

	public boolean isShowDebutNotificationTiers() {
		return showDebutNotificationTiers;
	}

	public void setShowDebutNotificationTiers(boolean showDebutNotificationTiers) {
		this.showDebutNotificationTiers = showDebutNotificationTiers;
	}

	public boolean isShowTexteNotificationTiers() {
		return showTexteNotificationTiers;
	}

	public void setShowTexteNotificationTiers(boolean showTexteNotificationTiers) {
		this.showTexteNotificationTiers = showTexteNotificationTiers;
	}

	public boolean getEvenementCalendrierExterne() {
		return evenementCalendrierExterne;
	}

	public void setEvenementCalendrierExterne(boolean evenementCalendrierExterne) {
		this.evenementCalendrierExterne = evenementCalendrierExterne;
	}

}  
  
