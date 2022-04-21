package fr.legrain.bdg.webapp.dashboard;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleModel;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import fr.legrain.article.dto.TaFabricationDTO;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaFabricationServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.agenda.LgrTabScheduleEvent;
import fr.legrain.bdg.webapp.app.AutorisationBean;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.solstyce.FabricationController;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.lib.data.LibDate;
import fr.legrain.moncompte.ws.TaAutorisation;
import fr.legrain.moncompte.ws.TaDossier;
import fr.legrain.moncompte.ws.TaProduit;
import fr.legrain.moncompte.ws.client.MonCompteWebServiceClientCXF;

@Named
@ViewScoped
public class DashBoardBean extends DashBoardDocumentController implements Serializable {

	private static final long serialVersionUID = -7163169571542084927L;

	@Inject private	SessionInfo sessionInfo;
	@Inject private	TenantInfo tenantInfo;


	private MonCompteWebServiceClientCXF wsMonCompte = null;
	
	@Inject @Named(value="autorisationBean")
	private AutorisationBean autorisation;
	@Inject @Named(value="fabricationController")
	private FabricationController fabricationController;
	@EJB private ITaFabricationServiceRemote taFabricationService;
	private @EJB ITaArticleServiceRemote taArticleService;
	private @EJB ITaTiersServiceRemote taTiersService;

	private List<TaProduit> listeTousProduit = null;
	private List<TaAutorisation> listeAutorisationDossier = null;

	private ScheduleModel eventModel;
	private LgrTabScheduleEvent event = new LgrTabScheduleEvent();
	
	private LineChartModel lineModel1;
	private LineChartModel lineModel2;
	private String classeexpiration;
	
    private List<DocumentChiffreAffaireDTO> listeCaMoisDocument;// Récupère le ca ht par mois dans un liste

	private int nbArticleActif = 0;
    
    private int nbTiersActif = 0;


    private int nbDevisSurPeriode = 0;
	private List<DocumentChiffreAffaireDTO> listeCaPeriodeDevis = null;
    private BigDecimal montantTotalHtDevis = BigDecimal.ZERO;    
    private BarChartModel barChartModelDevis;
	
	private int nbFactureSurPeriode = 0;
	private List<DocumentChiffreAffaireDTO> listeCaPeriodeFacture = null;
    private BigDecimal montantTotalHtFacture = BigDecimal.ZERO;    
    private BigDecimal montantTotalTtcResteAReglerFacture = BigDecimal.ZERO;
	private int nbFactureNonTotalementPayesSurPeriode = 0;

    private BarChartModel barChartModelFacture;

	private int nbAvoirSurPeriode = 0;
	private List<DocumentChiffreAffaireDTO> listeCaPeriodeAvoir = null;
    private BigDecimal montantTotalTtcAvoir = BigDecimal.ZERO;    

	private int nbProformaSurPeriode = 0;
    private BigDecimal montantTotalHtProforma = BigDecimal.ZERO;
    private int nbProformaNonTransformeARelancerSurPeriode = 0;

	
	private int nbBonlivSurPeriode = 0;
    private BigDecimal montantTotalHtBonliv = BigDecimal.ZERO;

	private int nbBoncdeSurPeriode = 0;
    private BigDecimal montantTotalHtBoncde = BigDecimal.ZERO;
    private BarChartModel barChartModelBoncde;
    
    private int nbAcompteSurPeriode = 0;
    private BigDecimal montantTotalHtAcompte = BigDecimal.ZERO;
    


	public int getNbBonlivSurPeriode() {
		return nbBonlivSurPeriode;
	}

	public int getNbProformaNonTransformeARelancerSurPeriode() {
		return nbProformaNonTransformeARelancerSurPeriode;
	}

	@PostConstruct
	public void init() {
////    	dateDebut = infoEntreprise.getDatedebInfoEntreprise();
////    	dateFin = infoEntreprise.getDatefinInfoEntreprise();
    	initInfosAccueil();
//		//ScheduleModel
//		//eventModel = new DefaultScheduleModel();
//		initCalendrier();
//
//		//LineChartModel
//		createLineModels();

		if(sessionInfo.getUtilisateur().getAdminDossier()!=null && sessionInfo.getUtilisateur().getAdminDossier()) {
			try {
				wsMonCompte = new MonCompteWebServiceClientCXF();

				TaDossier dossier = wsMonCompte.findDossier(tenantInfo.getTenantId());

				listeTousProduit = wsMonCompte.listeProduit(null,null,null);
				List<TaProduit> listeModuleVendable = new ArrayList<TaProduit>();
				for (TaProduit p : listeTousProduit) {
					if(p.isVendable()!=null && p.isVendable()) {
						listeModuleVendable.add(p);
					}
				}
				listeTousProduit = listeModuleVendable;

				listeAutorisationDossier = dossier.getListeAutorisation();
				List<TaAutorisation> listeAutorisationVendable = new ArrayList<TaAutorisation>();
				for (TaAutorisation aut : listeAutorisationDossier) {
					if(aut.getTaProduit().isVendable()!=null && aut.getTaProduit().isVendable()) {
						listeAutorisationVendable.add(aut);
					}
				}
				listeAutorisationDossier = listeAutorisationVendable;

			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void initInfosAccueil(){

		// section Articles
		nbArticleActif = taArticleService.countAllArticleActif();
		// section Tiers
		nbTiersActif = taTiersService.countAllTiersActifTaTiersDTO();

//		// Section Devis
//		// Retourne le nombre total de documents sur la période
		nbDevisSurPeriode = (int) taDevisService.countDocument(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		// Calcule les CA (HT, TVA, TTC) total sur la période
		montantTotalHtDevis = taDevisService.chiffreAffaireTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null).getMtHtCalc();

		barChartModelDevis = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
		listeCaMoisDocument = taDevisService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
		barChartModelDevis.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
		
//		// Section Facture
//		// Retourne le nombre total de documents sur la période
		nbFactureSurPeriode = (int) taFactureService.countDocument(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		montantTotalHtFacture = taFactureService.chiffreAffaireTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null).getMtHtCalc();
		montantTotalTtcResteAReglerFacture = taFactureService.chiffreAffaireNonTransformeTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), null).getResteAReglerComplet();
		nbFactureNonTotalementPayesSurPeriode = (int) taFactureService.countDocumentNonTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);


		barChartModelFacture = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
		listeCaMoisDocument = taFactureService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
		barChartModelFacture.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));		

		// Section Avoir
		// Retourne le nombre total de documents sur la période
		nbAvoirSurPeriode = (int) taAvoirService.countDocument(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		montantTotalTtcAvoir = taAvoirService.chiffreAffaireTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), null).getMtTtcCalc();
		// Section Proforma
		// Retourne le nombre total de documents sur la période
		nbProformaSurPeriode = (int) taProformaService.countDocument(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		// Calcule les CA (HT, TVA, TTC) total sur la période
		montantTotalHtProforma = taProformaService.chiffreAffaireTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null).getMtHtCalc();
		
		// Bon de livraison
		nbBonlivSurPeriode = (int) taBonlivService.countDocument(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		montantTotalHtBonliv = taBonlivService.chiffreAffaireTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null).getMtHtCalc();
		

		// Bon de commande
		nbBoncdeSurPeriode = (int) taBoncdeService.countDocument(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		montantTotalHtBoncde = taBoncdeService.chiffreAffaireTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null).getMtHtCalc();
		
		barChartModelBoncde = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
		listeCaMoisDocument = taBoncdeService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
		barChartModelBoncde.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));		

		// Acompte
		nbAcompteSurPeriode = (int) taAcompteService.countDocument(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		montantTotalHtAcompte = taAcompteService.chiffreAffaireTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null).getMtHtCalc();


	}
	
    public void refreshDashBoard(String etatDocument){
    	
    }

    private void createLineModels() {
		lineModel1 = initLinearModel();
		lineModel1.setTitle("Linear Chart");
		lineModel1.setLegendPosition("e");
		Axis yAxis = lineModel1.getAxis(AxisType.Y);
		yAxis.setMin(0);
		yAxis.setMax(10);

		lineModel2 = initCategoryModel();
		lineModel2.setTitle("Category Chart");
		lineModel2.setLegendPosition("e");
		lineModel2.setShowPointLabels(true);
		lineModel2.getAxes().put(AxisType.X, new CategoryAxis("Years"));
		yAxis = lineModel2.getAxis(AxisType.Y);
		yAxis.setLabel("Births");
		yAxis.setMin(0);
		yAxis.setMax(200);
	}

	private LineChartModel initLinearModel() {
		LineChartModel model = new LineChartModel();

		LineChartSeries series1 = new LineChartSeries();
		series1.setLabel("Series 1");

		series1.set(1, 2);
		series1.set(2, 1);
		series1.set(3, 3);
		series1.set(4, 6);
		series1.set(5, 8);

		LineChartSeries series2 = new LineChartSeries();
		series2.setLabel("Series 2");

		series2.set(1, 6);
		series2.set(2, 3);
		series2.set(3, 2);
		series2.set(4, 7);
		series2.set(5, 9);

		model.addSeries(series1);
		model.addSeries(series2);

		return model;
	}

	private LineChartModel initCategoryModel() {
		LineChartModel model = new LineChartModel();

		ChartSeries boys = new ChartSeries();
		boys.setLabel("Boys");
		boys.set("2004", 120);
		boys.set("2005", 100);
		boys.set("2006", 44);
		boys.set("2007", 150);
		boys.set("2008", 25);

		ChartSeries girls = new ChartSeries();
		girls.setLabel("Girls");
		girls.set("2004", 52);
		girls.set("2005", 60);
		girls.set("2006", 110);
		girls.set("2007", 90);
		girls.set("2008", 120);

		model.addSeries(boys);
		model.addSeries(girls);

		return model;
	}
	
	private void initCalendrier() {
		boolean dynamic = false;
		if(eventModel== null) {
			if(dynamic) {
			
				eventModel = new LazyScheduleModel() {
					private static final long serialVersionUID = -3000709249377994005L;
					@Override
		            public void loadEvents(LocalDateTime start, LocalDateTime end) {
						modelCalendar(LibDate.localDateTimeToDate(start),LibDate.localDateTimeToDate(end));
		            }   
		        };
			} else {
				eventModel = new DefaultScheduleModel();
				modelCalendar(null,null);
			}
		}
	}
	
	public void modelCalendar(Date start, Date end) {
		if(autorisation.autoriseMenu(AutorisationBean.ID_MODULE_TRACABILITE)) {
			List<TaFabricationDTO> listeFab = taFabricationService.findByDateLight(start,end);
			LgrTabScheduleEvent evt = null;
			for (TaFabricationDTO taFabricationDTO : listeFab) {
				evt = new LgrTabScheduleEvent(taFabricationDTO.getCodeDocument(), taFabricationDTO.getDateDebR(), taFabricationDTO.getDateDebR());
				evt.setCssLgrTab(LgrTab.CSS_CLASS_TAB_FABRICATION);
				evt.setIdObjet(taFabricationDTO.getId());
				evt.setCodeObjet(taFabricationDTO.getCodeDocument());
				evt.setData(taFabricationDTO);
//				evt.setStyleClass("");
				eventModel.addEvent(evt);
			}
		}
	}
	
	public void onEventSelect(SelectEvent selectEvent) {
        event = (LgrTabScheduleEvent) selectEvent.getObject();
    }
	
	public void openDocument() {
		openDocument(null);
	}
	
	public void openDocument(ActionEvent e) {
		if(event.getCssLgrTab().equals(LgrTab.CSS_CLASS_TAB_FABRICATION)) {
			fabricationController.setSelectedTaFabricationDTO((TaFabricationDTO) event.getData());
			fabricationController.onRowSelect(null);
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



	public LineChartModel getLineModel1() {
		return lineModel1;
	}



	public void setLineModel1(LineChartModel lineModel1) {
		this.lineModel1 = lineModel1;
	}



	public LineChartModel getLineModel2() {
		return lineModel2;
	}



	public void setLineModel2(LineChartModel lineModel2) {
		this.lineModel2 = lineModel2;
	}



	public List<TaProduit> getListeTousProduit() {
		return listeTousProduit;
	}



	public void setListeTousProduit(List<TaProduit> listeTousProduit) {
		this.listeTousProduit = listeTousProduit;
	}



	public List<TaAutorisation> getListeAutorisationDossier() {
		return listeAutorisationDossier;
	}



	public void setListeAutorisationDossier(
			List<TaAutorisation> listeAutorisationDossier) {
		this.listeAutorisationDossier = listeAutorisationDossier;
	}



	public String getClasseExpiration(Date datefin) {
		Calendar dateTmp1 = Calendar.getInstance();
		classeexpiration = "text-noir";
		if (LibDate.nbJoursBetween2Dates(dateTmp1.getTime(),datefin) <= 0) {
			classeexpiration = "text-rouge";
		} else if (( LibDate.nbJoursBetween2Dates(dateTmp1.getTime(),datefin) > 0) && (LibDate.nbJoursBetween2Dates(dateTmp1.getTime(),datefin) <= 30)) {
			classeexpiration = "text-orange";
		}

		return classeexpiration;
	}



	public void setClasseExpiration(String classeexpiration) {
		this.classeexpiration = classeexpiration;
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

	public FabricationController getFabricationController() {
		return fabricationController;
	}

	public void setFabricationController(FabricationController fabricationController) {
		this.fabricationController = fabricationController;
	}

	public int getNbDevisSurPeriode() {
		return nbDevisSurPeriode;
	}

	public int getNbFactureSurPeriode() {
		return nbFactureSurPeriode;
	}

	public List<DocumentChiffreAffaireDTO> getListeCaPeriodeFacture() {
		return listeCaPeriodeFacture;
	}

	public List<DocumentChiffreAffaireDTO> getListeCaPeriodeDevis() {
		return listeCaPeriodeDevis;
	}

	public BigDecimal getMontantTotalHtDevis() {
		return montantTotalHtDevis;
	}

	public BigDecimal getMontantTotalHtFacture() {
		return montantTotalHtFacture;
	}

	public int getNbAvoirSurPeriode() {
		return nbAvoirSurPeriode;
	}

	public List<DocumentChiffreAffaireDTO> getListeCaPeriodeAvoir() {
		return listeCaPeriodeAvoir;
	}

	public int getNbArticleActif() {
		return nbArticleActif;
	}

	public int getNbTiersActif() {
		return nbTiersActif;
	}

	public BigDecimal getMontantTotalHtProforma() {
		return montantTotalHtProforma;
	}

	public int getNbProformaSurPeriode() {
		return nbProformaSurPeriode;
	}

	public BigDecimal getMontantTotalHtBonliv() {
		return montantTotalHtBonliv;
	}

	public int getNbBoncdeSurPeriode() {
		return nbBoncdeSurPeriode;
	}

	public BigDecimal getMontantTotalHtBoncde() {
		return montantTotalHtBoncde;
	}

	public int getNbAcompteSurPeriode() {
		return nbAcompteSurPeriode;
	}

	public BigDecimal getMontantTotalHtAcompte() {
		return montantTotalHtAcompte;
	}

	public List<DocumentChiffreAffaireDTO> getListeCaMoisDocument() {
		return listeCaMoisDocument;
	}

	public void setListeCaMoisDocument(List<DocumentChiffreAffaireDTO> listeCaMoisDocument) {
		this.listeCaMoisDocument = listeCaMoisDocument;
	}

	public BarChartModel getBarChartModelFacture() {
		return barChartModelFacture;
	}

	public BarChartModel getBarChartModelDevis() {
		return barChartModelDevis;
	}

	public void setBarChartModelDevis(BarChartModel barChartModelDevis) {
		this.barChartModelDevis = barChartModelDevis;
	}

	public BarChartModel getBarChartModelBoncde() {
		return barChartModelBoncde;
	}

	public void setBarChartModelBoncde(BarChartModel barChartModelBoncde) {
		this.barChartModelBoncde = barChartModelBoncde;
	}

	public BigDecimal getMontantTotalTtcAvoir() {
		return montantTotalTtcAvoir;
	}

	public void setMontantTotalTtcAvoir(BigDecimal montantTotalTtcAvoir) {
		this.montantTotalTtcAvoir = montantTotalTtcAvoir;
	}

	public BigDecimal getMontantTotalTtcResteAReglerFacture() {
		return montantTotalTtcResteAReglerFacture;
	}

	public void setMontantTotalTtcResteAReglerFacture(BigDecimal montantTotalTtcResteAReglerFacture) {
		this.montantTotalTtcResteAReglerFacture = montantTotalTtcResteAReglerFacture;
	}

	public int getNbFactureNonTotalementPayesSurPeriode() {
		return nbFactureNonTotalementPayesSurPeriode;
	}

	public void setNbFactureNonTotalementPayesSurPeriode(int nbFactureNonTotalementPayesSurPeriode) {
		this.nbFactureNonTotalementPayesSurPeriode = nbFactureNonTotalementPayesSurPeriode;
	}



}
