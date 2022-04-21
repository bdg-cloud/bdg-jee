package fr.legrain.bdg.webapp.tunnelVente;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.bdg.documents.service.remote.ITaLEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPanierServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.general.service.remote.ITaLiaisonDossierMaitreServiceRemote;
import fr.legrain.bdg.rest.model.ParamEmailConfirmationCommandeBoutique;
import fr.legrain.bdg.tiers.service.remote.ITaEspaceClientServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.paiement.PaiementCbController;
import fr.legrain.bdg.webapp.paiement.PaiementCbWsController;
import fr.legrain.bdg.webapp.paiement.PaiementParam;
import fr.legrain.bdg.webapp.paiement.PaiementParamWs;
import fr.legrain.bdg.ws.rest.client.BdgRestClient;
import fr.legrain.bdg.ws.rest.client.Config;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.TaLEcheanceDTO;
import fr.legrain.document.dto.TaLPanierDTO;
import fr.legrain.document.dto.TaPanierDTO;
import fr.legrain.document.model.ImageLgr;
import fr.legrain.document.model.RetourGenerationDoc;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaLEcheance;
import fr.legrain.document.model.TaLPanier;
import fr.legrain.document.model.TaPanier;
import fr.legrain.document.model.TypeDoc;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.email.model.TaContactMessageEmail;
import fr.legrain.email.service.EnvoiEmailService;
import fr.legrain.general.model.TaLiaisonDossierMaitre;
import fr.legrain.general.model.TaParametre;
import fr.legrain.general.service.remote.ITaParametreServiceRemote;
import fr.legrain.generation.service.CreationDocumentLigneEcheanceAbonnementMultiple;
import fr.legrain.generationDocument.model.ParamAfficheChoixGenerationDocument;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibCrypto;
import fr.legrain.paiement.service.LgrEnvironnementServeur;
import fr.legrain.tiers.model.TaEmail;
import fr.legrain.tiers.model.TaEspaceClient;
import fr.legrain.tiers.model.TaTiers;

@Named
@ViewScoped
public class TunnelVenteController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1653801083238227860L;
	
	private Boolean afficheTunnelVente = false;
	
	private List<TaLEcheanceDTO> listeLEcheanceRenouvellement = new ArrayList<TaLEcheanceDTO>();
	
	private List<TaLEcheanceDTO> listeSelectedLEcheanceDTO = new ArrayList<TaLEcheanceDTO>();
	
	private List<TaLEcheance> listeSelectedLEcheance= new ArrayList<TaLEcheance>();
	
	private List<ModuleBDGJSF> listeModuleBDG = new ArrayList<ModuleBDGJSF>();
	
	private List<ModuleBDGJSF> listeSelectedModuleBDG = new ArrayList<ModuleBDGJSF>();
	
	private List<ModuleBDGJSF> listeOptionModuleBDG = new ArrayList<ModuleBDGJSF>();
	
	private List<ModuleBDGJSF> listeSelectedOptionModuleBDG = new ArrayList<ModuleBDGJSF>();
	
	private @EJB EnvoiEmailService emailServiceFinder;
	private @EJB ITaTiersServiceRemote taTiersService;
	private @EJB ITaEspaceClientServiceRemote espaceClientService;
	
	private TaPanier taPanier = null;
	private TaPanierDTO taPanierDTO = null;
	protected List<DocumentDTO> values; 
	//protected List<LigneJSF> valuesLigne;
	private List<TaLPanier> lignePanier = new ArrayList<TaLPanier>();
	private List<TaLPanierDTO> lignePanierDTO = new ArrayList<TaLPanierDTO>();
	
	private TaLPanierDTO selectedTaLPanierDTO;
	
	protected Integer prefNbDecimalesPrix;
	protected Integer prefNbDecimalesQte;
	
	protected @EJB ITaPreferencesServiceRemote taPreferencesService;
	
	
	protected TaLEcheance ligneDocument = null;
	
	private String loginDeLaTableEspaceClient = null;
	private String passwordDeLaTableEspaceClient = null;
	
	@EJB private ITaLEcheanceServiceRemote taLEcheanceService;
	@EJB private ITaLiaisonDossierMaitreServiceRemote taLiaisonDossierMaitreService;
	@EJB private ITaPanierServiceRemote taPanierService;
	
	@EJB private ITaParametreServiceRemote taParametreService;
	
	//faire un appel WS plutot qu'une injection
	@EJB private CreationDocumentLigneEcheanceAbonnementMultiple creation;
	
	@EJB private LgrEnvironnementServeur lgrEnvironnementServeur;
	
	private Boolean panierPaye = false;
	
	String dossierTenant = "";
	
	@PostConstruct
	public void init() {
		panierPaye = false;
		initIdentifiantEspaceClient();
		initListEcheance();
		initPanier();
		//initListFacture();
		addEcheanceToSelectedModuleBDGJSF(listeLEcheanceRenouvellement);
		initOptionModuleBDGJSF();
		TaPreferences nb;
		nb = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_NB_DECIMALES, ITaPreferencesServiceRemote.NB_DECIMALES_PRIX);
		if(nb!=null && nb.getValeur() != null) {
			prefNbDecimalesPrix=LibConversion.stringToInteger(nb.getValeur());
		}
	}
	
	public void initIdentifiantEspaceClient() {
		TaParametre param = taParametreService.findInstance();
		dossierTenant = param.getDossierMaitre();
		loginDeLaTableEspaceClient = null;
		passwordDeLaTableEspaceClient = null;
		List<TaLiaisonDossierMaitre> listeLiaison =  taLiaisonDossierMaitreService.selectAll();
		
		if (listeLiaison != null && !listeLiaison.isEmpty()) {
			loginDeLaTableEspaceClient = listeLiaison.get(0).getEmail();
			passwordDeLaTableEspaceClient = listeLiaison.get(0).getPassword();
			if(passwordDeLaTableEspaceClient!=null) { 
				passwordDeLaTableEspaceClient = LibCrypto.decrypt(passwordDeLaTableEspaceClient);
			}
		}else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
			FacesMessage.SEVERITY_WARN, "Veuillez vous connecter a l'espace client",""));
		}
		
	}

	public void initListEcheance() {

		if(loginDeLaTableEspaceClient != null && passwordDeLaTableEspaceClient != null) {
			System.out.println("************* TOKEN JWT ESPACE CLIENT **********************");
			//Création et configuration du client
			Config config = new Config(dossierTenant,loginDeLaTableEspaceClient, passwordDeLaTableEspaceClient);
			config.setVerificationSsl(false);
			config.setDevLegrain(true);
			BdgRestClient bdg = BdgRestClient.build(config);
			
			//Connexion du client pour une connexion par token JWT
			String codeTiers = bdg.connexionEspaceClient();
			System.out.println("code tier  : "+codeTiers);
			
			List<String> codeEtats = new ArrayList<String>();
			codeEtats.add("doc_suspendu");
			codeEtats.add("doc_encours");
			
			//Appel utilisant le token
			try {
				listeLEcheanceRenouvellement = bdg.abonnements().listeEcheance(codeTiers, codeEtats, true);
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
				FacesMessage.SEVERITY_WARN, e.getMessage(),""));
				e.printStackTrace();
			}
		}else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
			FacesMessage.SEVERITY_WARN, "Veuillez vous connecter a l'espace client",""));
		}
		
	}
	
	
	
	public void addEcheanceToSelectedModuleBDGJSF(List<TaLEcheanceDTO> liste) {
		for (TaLEcheanceDTO ech : liste) {
			ModuleBDGJSF module = new ModuleBDGJSF();
			module.setIdLEcheance(ech.getIdLEcheance());
			module.setCodeArticle(ech.getCodeArticle());
			module.setCodeModuleBDG(ech.getCodeModuleBdg());
			module.setDateDebutPeriode(ech.getDebutPeriode());
			module.setDateFinPeriode(ech.getFinPeriode());
			module.setDateEcheance(ech.getDateEcheance());
			module.setLibelleModule(ech.getLibLDocument());
			module.setNbMois(12);
			module.setLiblEtat(ech.getCodeEtat());
			module.setNbLicenses(ech.getCoefMultiplicateur());
			module.setPrixTTCXMois(ech.getMtTtcLDocument());
			module.setTaLEcheanceDTO(ech);
			//module.setLiblPrixMois(liblPrixMois);
			listeModuleBDG.add(module);
			listeSelectedModuleBDG.add(module);
		}
	}
	

	public void initPanier() {
		if(loginDeLaTableEspaceClient != null && passwordDeLaTableEspaceClient != null) {
			//Création et configuration du client
			Config config = new Config(dossierTenant,loginDeLaTableEspaceClient, passwordDeLaTableEspaceClient);
			config.setVerificationSsl(false);
			config.setDevLegrain(true);
			BdgRestClient bdg = BdgRestClient.build(config);
			
			//Connexion du client pour une connexion par token JWT
			String codeTiers = bdg.connexionEspaceClient();
			System.out.println("code tier  : "+codeTiers);
			
			List<String> codeEtats = new ArrayList<String>();
			codeEtats.add("doc_suspendu");
			codeEtats.add("doc_encours");
			
			//Appel utilisant le token
			try {
				taPanierDTO = bdg.paniers().getPanierActif(codeTiers);
				if(taPanierDTO != null) {
					lignePanierDTO = taPanierDTO.getLignesDTO();
				}
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
				FacesMessage.SEVERITY_ERROR, e.getMessage(),""));
				e.printStackTrace();
			}
			
			
		}else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
			FacesMessage.SEVERITY_WARN, "Veuillez vous connecter a l'espace client",""));
		}
		
	}
	
	public void addSelectedEcheanceToPanier() {
		if (taPanierDTO != null) {
			//connexion ws
			Config config = new Config(dossierTenant,loginDeLaTableEspaceClient, passwordDeLaTableEspaceClient);
			config.setVerificationSsl(false);
			config.setDevLegrain(true);
			BdgRestClient bdg = BdgRestClient.build(config);
			
			//Connexion du client pour une connexion par token JWT
			String codeTiers = bdg.connexionEspaceClient();
			System.out.println("code tier  : "+codeTiers);

				try {
					for (ModuleBDGJSF module : listeSelectedModuleBDG) {
						taPanierDTO = bdg.paniers().ajoutLigneEcheance(taPanierDTO.getId(), module.getTaLEcheanceDTO());
						if(taPanierDTO != null) {
							lignePanierDTO = taPanierDTO.getLignesDTO();
						}
					
					}
				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, e.getMessage(),"(erreur dans l'ajout de ligne d'échéance au panier)"));
					e.printStackTrace();
				}
				
			
		}
		
	}
	

	
	public void onRowSelectLigne(SelectEvent event) { 
		if(event.getObject()!=null)
			selectedTaLPanierDTO =  (TaLPanierDTO) event.getObject();		
	}
	
	public void actSupprimerLignePanier() {
		actSupprimerLignePanier(null);
	}
	
	public void actSupprimerLignePanier(ActionEvent actionEvent) {
		//Integer idLPanier =  (Integer) event.getComponent().getAttributes().get("idLPanier");
		if(selectedTaLPanierDTO != null) {
			
			Config config = new Config(dossierTenant,loginDeLaTableEspaceClient, passwordDeLaTableEspaceClient);
			config.setVerificationSsl(false);
			config.setDevLegrain(true);
			BdgRestClient bdg = BdgRestClient.build(config);
			
			//Connexion du client pour une connexion par token JWT
			String codeTiers = bdg.connexionEspaceClient();
			System.out.println("code tier  : "+codeTiers);

				try {
					taPanierDTO = bdg.paniers().supprimeLigne(taPanierDTO.getId(), selectedTaLPanierDTO.getNumLigneLDocument());
					if(taPanierDTO != null) {
						lignePanierDTO = taPanierDTO.getLignesDTO();
					}
				

				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, e.getMessage(),"(erreur dans la suppréssion de ligne du panier)"));
					e.printStackTrace();
				}
			
			
		}
	}
	
	private TaLEcheance ligneDocumentValide(Integer id){
		try {	
				//ligneDocument=ligneServiceWebExterneService.findById(id);
			ligneDocument= taLEcheanceService.findById(id);
			return ligneDocument;
		} catch (FinderException e) {
			return null;
		}
	}
	
	public void addSelectedMudleBDGJSFToPanier() {
		for (ModuleBDGJSF module : listeModuleBDG) {
			
		}
	}
	
	public void addOptionsToSelectedModuleBDGJSF() {
		listeModuleBDG.addAll(listeSelectedOptionModuleBDG);
	}
	
	public void initOptionModuleBDGJSF() {
		ModuleBDGJSF moduleBoutique = new ModuleBDGJSF();
		moduleBoutique.setCodeArticle("BDC_BOUTIQUE");
		moduleBoutique.setCodeModuleBDG(moduleBoutique.getCodeArticle());
		moduleBoutique.setLibelleModule("Boutique");
		moduleBoutique.setNbMois(12);
		moduleBoutique.setPrixTTCXMois(new BigDecimal(60));
		moduleBoutique.setLiblPrixMois(moduleBoutique.getPrixTTCXMois()+" € par mois");
		listeOptionModuleBDG.add(moduleBoutique);
		
		ModuleBDGJSF moduleEspaceClient = new ModuleBDGJSF();
		moduleEspaceClient.setCodeArticle("BDC_CPTCLI");
		moduleEspaceClient.setCodeModuleBDG(moduleEspaceClient.getCodeArticle());
		moduleEspaceClient.setLibelleModule("Espace client");
		moduleEspaceClient.setNbMois(12);
		moduleEspaceClient.setPrixTTCXMois(new BigDecimal(20));
		moduleEspaceClient.setLiblPrixMois(moduleEspaceClient.getPrixTTCXMois()+" € par mois");
		listeOptionModuleBDG.add(moduleEspaceClient);
		
		
	}
	
	public void actInitPaiementCarteBancaireWs(ActionEvent actionEvent) {
		if(taPanierDTO != null) {

			//taPanier = taPanierService.findById(taPanierDTO.getId());
			PaiementParamWs param = new PaiementParamWs();
			   // param.setDocumentPayableCB(taPanier);
			   // param.setSwtDocumentPayableCB(taPanier);
			    param.setMontantPaiement(taPanierDTO.getNetTtcCalc());
			    param.setOriginePaiement("BDG Panier");
			    param.setMontantLibre(false);	
			    param.setTypeDocmument(TaPanier.TYPE_DOC);
			    param.setCodeDocument(taPanierDTO.getCodeDocument());
			    param.setLoginDeLaTableEspaceClient(loginDeLaTableEspaceClient);
			    param.setPasswordDeLaTableEspaceClient(passwordDeLaTableEspaceClient);
			    param.setDossierTenant(dossierTenant);
			    param.setVerificationSsl(false);
			    param.setDevLegrain(true);
			    
			    PaiementCbWsController.actDialoguePaiementEcheanceParCarte(param);
			 	
		}
	   
	}
	public void envoiMailApresPaiement(Integer idPanier, String idPaymentIntent) {
		
		List<TaLiaisonDossierMaitre> listeLiaison =  taLiaisonDossierMaitreService.selectAll();
		TaLiaisonDossierMaitre liaison = listeLiaison.get(0);
		String destinataire = null;
		String codeTiers = liaison.getCodeTiers();
		
		//Connexion du client pour une connexion par token JWT
		System.out.println("code tier  : "+codeTiers);

		//si on est sur serveur de test, on envoi un mail à yann@legrain.fr 
		if(lgrEnvironnementServeur.isModeTestEmailDossier())	{
			 destinataire = "yann@legrain.fr";
		}
		
		if(destinataire != null) {
			List<String> adressesDestinataireLoc = new ArrayList<>();
			List<TaEmail> emailTiersDestinataire = new ArrayList<>();
			List<TaContactMessageEmail> contactMessageEmailDestinataire = new ArrayList<>();
			List<String> adressesCcLoc = new ArrayList<>();
			List<TaEmail> emailTiersCc = null;
			List<TaContactMessageEmail> contactMessageEmailCc = new ArrayList<>();
			List<String> adressesBccLoc = new ArrayList<>();
			List<TaEmail> emailTiersBcc = null;
			List<TaContactMessageEmail> contactMessageEmailBcc = new ArrayList<>();
			File[] attachment = new File[1];
			List<String> adressesCc= new ArrayList<String>();
			List<String> adressesBcc= new ArrayList<String>();
			List<String> adressesDestinataire = new ArrayList<String>();
			String adresseExpediteur = "";
			attachment = null;
			
			adressesDestinataire.add(destinataire);
			String message = "Cher Client (code "+codeTiers+"), <br><br>"
					+ "Nous avons bien reçus votre paiement concernant le renouvellement d'un ou plusieurs module de BDG"
					+ " et nous vous remercions de votre confiance.<br><br>"
					+ "Nous nous occupons de mettre à jour votre dossier dans les plus bref délais."
					+"<br>"
					+"<br>"
					+"Cordialement,"
					+"<br>"
					+"<br>"
					+"Service client.<br>";
			String objet = "Paiement pour renouvellement bien reçus (code tiers "+codeTiers+" )";
			//envoi au tiers
			emailServiceFinder.sendAndLogEmailDossier(
					null,
					adresseExpediteur,//null,
					"BDG",
					objet,//sujet
					message, // message en string
					message, //message html 
					adressesDestinataire, //destinataires
					emailTiersDestinataire,
					contactMessageEmailDestinataire,
					adressesCc, 
					emailTiersCc,
					contactMessageEmailCc,
					adressesBcc, 
					emailTiersBcc,
					contactMessageEmailBcc,
					attachment,
					null,//replyTo
					null//nicolas : normalement c'est sessionInfo.getUtilisateur() mais encore une fois, il y a un probleme de SessionScope pour ce code si il est initié par un Timer
					);
		}
		
		System.out.println("Succès Envoi de mail de confirmation de paiement renvoullement de BDG depuis tunnel de vente.");
	}
	
	public void handleReturnDialoguePaiementParCarte(SelectEvent event) {
		try {
						if(event!=null && event.getObject()!=null) {
							String idExternePaymentIntent = (String) event.getObject();
							//actActionApresPaiementAccepte();
							Config config = new Config(dossierTenant,loginDeLaTableEspaceClient, passwordDeLaTableEspaceClient);
							config.setVerificationSsl(false);
							config.setDevLegrain(true);
							BdgRestClient bdg = BdgRestClient.build(config);
							
							//Connexion du client pour une connexion par token JWT
							String codeTiers = bdg.connexionEspaceClient();
							System.out.println("code tier  : "+codeTiers);
                            Integer etat = bdg.paiement().etatPaiementCourant(idExternePaymentIntent);
//                            Pas de réponse 0
//                	  		IEtatPaiementCourant.PAIEMENT_OK 1
//                	  		IEtatPaiementCourant.PAIEMENT_ERREUR 2
//                			IEtatPaiementCourant.PAIEMENT_EN_COURS 3
                            if(etat!=null) {
                            	if(etat == 0) {
                            		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                					FacesMessage.SEVERITY_ERROR, "Paiement Stripe ","Aucune réponse de la part de Stripe"));
                            		
                            	}else if (etat == 1) {
                            		initListEcheance();
                            		listeModuleBDG.clear();
                        			listeSelectedModuleBDG.clear();
                            		addEcheanceToSelectedModuleBDGJSF(listeLEcheanceRenouvellement);
                            		initPanier();
                            		panierPaye = true;
                            		envoiMailApresPaiement(taPanierDTO.getId(), idExternePaymentIntent);
                            		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                					FacesMessage.SEVERITY_INFO, "Paiement Stripe ","Paiement réalisé, Merci pour votre achat. La mise à jour de votre dossier sera faite dans les meilleurs délais"));
                            		
                            	}else if (etat == 2) {
                            		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                					FacesMessage.SEVERITY_ERROR, "Paiement Stripe ","Une erreur est survenue."));
                            	}else if (etat == 3) {
                            		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                					FacesMessage.SEVERITY_WARN, "Paiement Stripe ","Paiement en cours, Merci pour votre achat. La mise à jour de votre dossier sera faite dans les meilleurs délais"));
                            	}
                            }
						}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String onFlowProcess(FlowEvent event) {
		panierPaye = false;
			if(event.getOldStep().equals("version") && event.getNewStep().equals("recap")) {
				addSelectedEcheanceToPanier();
			}
			
			
			
			return event.getNewStep();
		
	}
	
	
	
	public void initWizard(){
		afficheTunnelVente = true;
		initPanier();
	}
	
	public void actFermer(ActionEvent actionEvent) {


	}
	
	
	
	

	public Boolean getAfficheTunnelVente() {
		return afficheTunnelVente;
	}

	public void setAfficheTunnelVente(Boolean afficheTunnelVente) {
		this.afficheTunnelVente = afficheTunnelVente;
	}



	public List<TaLEcheanceDTO> getListeLEcheanceRenouvellement() {
		return listeLEcheanceRenouvellement;
	}



	public void setListeLEcheanceRenouvellement(List<TaLEcheanceDTO> listeLEcheanceRenouvellement) {
		this.listeLEcheanceRenouvellement = listeLEcheanceRenouvellement;
	}



	public List<ModuleBDGJSF> getListeSelectedModuleBDG() {
		return listeSelectedModuleBDG;
	}



	public void setListeSelectedModuleBDG(List<ModuleBDGJSF> listeSelectedModuleBDG) {
		this.listeSelectedModuleBDG = listeSelectedModuleBDG;
	}


	public List<ModuleBDGJSF> getListeOptionModuleBDG() {
		return listeOptionModuleBDG;
	}


	public void setListeOptionModuleBDG(List<ModuleBDGJSF> listeOptionModuleBDG) {
		this.listeOptionModuleBDG = listeOptionModuleBDG;
	}


	public List<ModuleBDGJSF> getListeSelectedOptionModuleBDG() {
		return listeSelectedOptionModuleBDG;
	}


	public void setListeSelectedOptionModuleBDG(List<ModuleBDGJSF> listeSelectedOptionModuleBDG) {
		this.listeSelectedOptionModuleBDG = listeSelectedOptionModuleBDG;
	}


	public TaPanier getTaPanier() {
		return taPanier;
	}


	public void setTaPanier(TaPanier taPanier) {
		this.taPanier = taPanier;
	}


	public List<TaLPanier> getLignePanier() {
		return lignePanier;
	}


	public void setLignePanier(List<TaLPanier> lignePanier) {
		this.lignePanier = lignePanier;
	}


	public List<TaLEcheanceDTO> getListeSelectedLEcheanceDTO() {
		return listeSelectedLEcheanceDTO;
	}


	public void setListeSelectedLEcheanceDTO(List<TaLEcheanceDTO> listeSelectedLEcheanceDTO) {
		this.listeSelectedLEcheanceDTO = listeSelectedLEcheanceDTO;
	}


	public List<TaLEcheance> getListeSelectedLEcheance() {
		return listeSelectedLEcheance;
	}


	public void setListeSelectedLEcheance(List<TaLEcheance> listeSelectedLEcheance) {
		this.listeSelectedLEcheance = listeSelectedLEcheance;
	}


	public List<TaLPanierDTO> getLignePanierDTO() {
		return lignePanierDTO;
	}


	public void setLignePanierDTO(List<TaLPanierDTO> lignePanierDTO) {
		this.lignePanierDTO = lignePanierDTO;
	}


	public TaPanierDTO getTaPanierDTO() {
		return taPanierDTO;
	}


	public void setTaPanierDTO(TaPanierDTO taPanierDTO) {
		this.taPanierDTO = taPanierDTO;
	}


	public Integer getPrefNbDecimalesPrix() {
		return prefNbDecimalesPrix;
	}


	public Integer getPrefNbDecimalesQte() {
		return prefNbDecimalesQte;
	}


	public void setPrefNbDecimalesPrix(Integer prefNbDecimalesPrix) {
		this.prefNbDecimalesPrix = prefNbDecimalesPrix;
	}


	public void setPrefNbDecimalesQte(Integer prefNbDecimalesQte) {
		this.prefNbDecimalesQte = prefNbDecimalesQte;
	}

	public String getLoginDeLaTableEspaceClient() {
		return loginDeLaTableEspaceClient;
	}

	public void setLoginDeLaTableEspaceClient(String loginDeLaTableEspaceClient) {
		this.loginDeLaTableEspaceClient = loginDeLaTableEspaceClient;
	}

	public String getPasswordDeLaTableEspaceClient() {
		return passwordDeLaTableEspaceClient;
	}

	public void setPasswordDeLaTableEspaceClient(String passwordDeLaTableEspaceClient) {
		this.passwordDeLaTableEspaceClient = passwordDeLaTableEspaceClient;
	}

	public TaLPanierDTO getSelectedTaLPanierDTO() {
		return selectedTaLPanierDTO;
	}

	public void setSelectedTaLPanierDTO(TaLPanierDTO selectedTaLPanierDTO) {
		this.selectedTaLPanierDTO = selectedTaLPanierDTO;
	}

	public List<ModuleBDGJSF> getListeModuleBDG() {
		return listeModuleBDG;
	}

	public void setListeModuleBDG(List<ModuleBDGJSF> listeModuleBDG) {
		this.listeModuleBDG = listeModuleBDG;
	}

	public Boolean getPanierPaye() {
		return panierPaye;
	}

	public void setPanierPaye(Boolean panierPaye) {
		this.panierPaye = panierPaye;
	}
	

}
