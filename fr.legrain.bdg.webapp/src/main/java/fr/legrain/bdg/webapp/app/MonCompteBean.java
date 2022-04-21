package fr.legrain.bdg.webapp.app;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import fr.legrain.autorisations.ws.client.AutorisationWebServiceClientCXF;
import fr.legrain.bdg.droits.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.bdg.webapp.Auth;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.general.service.local.IDatabaseServiceLocal;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;
import fr.legrain.moncompte.ws.TaAutorisation;
import fr.legrain.moncompte.ws.TaDossier;
import fr.legrain.moncompte.ws.TaProduit;
import fr.legrain.moncompte.ws.client.MonCompteWebServiceClientCXF;

@Named
@ViewScoped 
public class MonCompteBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -643207837619830455L;

	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;
	
	private String motDePasseActuel = "";
	private String nouveauMotDePasse = "";
	private String confirmationMotDePasse = "";
	
	private MonCompteWebServiceClientCXF wsMonCompte = null;
	private AutorisationWebServiceClientCXF wsAutorisation = null;
	private TaDossier dossierCourant = null;
	
	public TaDossier getDossierCourant() {
		return dossierCourant;
	}

	public void setDossierCourant(TaDossier dossierCourant) {
		this.dossierCourant = dossierCourant;
	}

	private List<TaProduit> listeTousProduit = null;
	private List<TaAutorisation> listeAutorisationDossier = null;
	
	private List<TaDossier> listeDossier = null;
	@NotNull
	@Size(min = 1, max = 50, message="La longueur maximum du code dossier est de 50 caractères.")
	@Pattern(regexp = "^[A-Za-z]+[A-Za-z0-9\\-]*$", message="Seul les caractères alpha-numériques (a-z), les chiffres (0-9) et le tiret (-) sont autorisés dans le nom du dossier. Le nom du dossier ne peut pas commencer par un tiret ou un chiffre.")
	private String nomNouveauDossier;
	private String loginNouveauDossier = null;
	private String motDePasseNouveauDossier = null;
	private String confirmationMotDePasseNouveauDossier = null;
	
	private List<TaProduit> listeModule;
	private TaProduit[] selectedModules;
	private Boolean actionOk = false;
	private boolean tousLesModules = false;

	private TaUtilisateur u = null;
	
//	@Inject private	SessionInfo sessionInfo;
	@Inject private	TenantInfo tenantInfo;
	
	private @EJB ITaUtilisateurServiceRemote userService;
	private @EJB IDatabaseServiceLocal adminService;
	
	@PostConstruct
	public void init() {
		u = Auth.findUserInSession();
		
		if(u.getAdminDossier()!=null && u.getAdminDossier()) {
			try {
				wsMonCompte = new MonCompteWebServiceClientCXF();
				wsAutorisation = new AutorisationWebServiceClientCXF();
				
				dossierCourant = wsMonCompte.findDossier(tenantInfo.getTenantId());
				
				listeTousProduit = wsMonCompte.listeProduit(null,null,null);
				List<TaProduit> listeModuleVendable = new ArrayList<TaProduit>();
				for (TaProduit p : listeTousProduit) {
					if(p.isVendable()!=null && p.isVendable()) {
						listeModuleVendable.add(p);
					}
				}
				listeTousProduit = listeModuleVendable;
				
				listeAutorisationDossier = dossierCourant.getListeAutorisation();
				List<TaAutorisation> listeAutorisationVendable = new ArrayList<TaAutorisation>();
				for (TaAutorisation aut : listeAutorisationDossier) {
					if(aut.getTaProduit()!=null && aut.getTaProduit().isVendable()!=null && aut.getTaProduit().isVendable()) {
						if(dossierCourant.getTaTNiveau().getCode().equals("DEMO") || //dossier en version demo
								aut.getTaProduit().getTaTNiveau().getCode().equals(dossierCourant.getTaTNiveau().getCode())) {
							listeAutorisationVendable.add(aut);
						}
					}
				}
				listeAutorisationDossier = listeAutorisationVendable;
				
				listeDossier = wsMonCompte.findListeDossierClient(dossierCourant.getTaClient().getId());
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void openMonCompte(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.monCompte");
		b.setTitre("Mon compte");
		b.setTemplate("admin/mon_compte.xhtml");
		//b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
	}
	
	public void changeMotDePasse(ActionEvent event) throws Exception {
		
		if(motDePasseActuel!=null && !motDePasseActuel.equals("") && u.passwordHashSHA256_Base64(motDePasseActuel).equals(u.getPasswd())) {
			//le mot de passe actuel est bien le bon
			
			if(nouveauMotDePasse!=null && !nouveauMotDePasse.equals("") 
					&& confirmationMotDePasse!=null && !confirmationMotDePasse.equals("") 
					&& nouveauMotDePasse.equals(confirmationMotDePasse)) {
				//le nouveau mot de passe saisie est correct
				
				if(verifComplexiteMotDePasse(nouveauMotDePasse)) {
					u.setPasswd(u.passwordHashSHA256_Base64(nouveauMotDePasse));
					userService.enregistrerMerge(u);
					
					Auth a = new Auth();
					a.logout();
					
					FacesContext context = FacesContext.getCurrentInstance();  
					context.addMessage(null, new FacesMessage("Mon compte", 
							"Votre mot de passe à bien été modifié."
							)); 
					
				} else {
					FacesContext context = FacesContext.getCurrentInstance();  
					context.addMessage(null, new FacesMessage("Mon compte", 
							"Votre nouveau mot de passe n'est pas assez complexe"
							)); 
				}
			} else {
				
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Mon compte", 
						"Votre nouveau mot de passe n'est pas correct"
						)); 
			}
		} else {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Mon compte", 
					"Votre mot de passe actuel n'est pas correct"
					)); 
		}
		
	}
	
	public void actCreerDossier(ActionEvent event) {  
		FacesContext context = FacesContext.getCurrentInstance();  
		
		try {
			if(!wsMonCompte.dossierExiste(nomNouveauDossier)) {		

				if(motDePasseNouveauDossier!=null && confirmationMotDePasseNouveauDossier!=null 
						&& !confirmationMotDePasseNouveauDossier.equals("") 
						&& motDePasseNouveauDossier.equals(confirmationMotDePasseNouveauDossier)) {
					//le nouveau mot de passe saisie est correct

					if(verifComplexiteMotDePasse(motDePasseNouveauDossier)) {

						/*
						 * création schéma
						 */
						adminService.ajoutDossier(nomNouveauDossier,loginNouveauDossier,motDePasseNouveauDossier);

						/*
						 * Création du dossier et affection au client du dossier courant
						 */
						TaDossier nouveauDossier = new TaDossier();
						nouveauDossier.setTaClient(dossierCourant.getTaClient());
						nouveauDossier.setActif(true);
						nouveauDossier.setCode(nomNouveauDossier);
						nouveauDossier.setNbPosteInstalle(0);
						nouveauDossier.setNbUtilisateur(1);
						nouveauDossier.setAccesWs(false);
						nouveauDossier.setCodePartenaire(dossierCourant.getCodePartenaire());

						/*
						 * Affectation module
						 */
						GregorianCalendar d = new GregorianCalendar();
						GregorianCalendar d2 = new GregorianCalendar();
						Date maintenant = new Date();
						d.setTime(maintenant);
						d2.setTime(LibDate.incrementDate(maintenant, 45, 0, 0)); //+45 jours
						XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(d);
						XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(d2);

						List<TaAutorisation> aut = new ArrayList<TaAutorisation>();
						TaAutorisation a1 = null;
						initListeModule();
						for (TaProduit p : selectedModules) {
							a1 = new TaAutorisation();
							a1.setDateAchat(date);
							a1.setDateFin(date2);
							a1.setTaProduit(p);
							a1.setPaye(false); //autorisation "offerte" pour la demo
							a1.setTaDossier(nouveauDossier);
							//				if(!c.getListeAutorisation().contains(a1)) {
							nouveauDossier.getListeAutorisation().add(a1);
							//				};
							aut.add(a1);
						}

						/*
						 * Enregistrement du client avec ses autorisations
						 */
						wsMonCompte.mergeDossier(nouveauDossier);

						fr.legrain.autorisation.xml.ListeModules listeXml = new fr.legrain.autorisation.xml.ListeModules();
						fr.legrain.autorisation.xml.Module moduleXml = null;
						for (TaAutorisation taAutorisation : aut) {
							listeXml = wsMonCompte.ajouteAutorisation(listeXml,taAutorisation.getTaProduit());
							listeXml.accessWebservice = nouveauDossier.isAccesWs()?"1":"0";
							listeXml.nbPosteClient = LibConversion.integerToString(nouveauDossier.getNbPosteInstalle());
							listeXml.nbUtilisateur = LibConversion.integerToString(nouveauDossier.getNbUtilisateur());
						}

						/*
						 * MAJ autorisations
						 */
						int idProduit = 4;
						wsAutorisation.createUpdateDroits(nouveauDossier.getCode(), idProduit, listeXml);

//						emailConfirmation(nouveauDossier);
//						actionOk = true;

						context.addMessage(null, new FacesMessage("Dossier créé ", 
								"Le dossier '"+nomNouveauDossier+"' a bien été créé.\n "
										+ "Un Email de confirmation vient de vous être envoyé.\n\n"
										+ "Si vous ne le voyez pas dans votre boite de réception, il est possible qu'il se trouve dans les indésirables."
								)); 
					} else {
						context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Votre nouveau mot de passe est trop simple", "")); 
					}
				} else {
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Les valeurs saisie pour le mot de passe et sa confirmation doivent être identique", "")); 
				}
			} else {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Ce nom de dossier existe déjà, veuillez en saisir un autre.", "")); 
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initListeModule() {
		try {
			listeModule = wsMonCompte.listeProduit(null, null, null);
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<TaProduit> listeModuleVendable = new ArrayList<TaProduit>();
		for (TaProduit p : listeModule) {
			if(p.isVendable()!=null && p.isVendable()) {
				listeModuleVendable.add(p);
			}
		}
		listeModule = listeModuleVendable;
		selectedModules = new TaProduit[listeModuleVendable.size()];
		selectedModules = listeModuleVendable.toArray(selectedModules);
		
		if(tousLesModules) {
			selectedModules = new TaProduit[listeModule.size()];
			selectedModules = listeModule.toArray(selectedModules);
		}
	}
	
	public void actFermer() {

	}
	
	private boolean verifComplexiteMotDePasse(String pwd) {
		return true;
	}
	
	public TabListModelBean getTabsCenter() {
		return tabsCenter;
	}

	public void setTabsCenter(TabListModelBean tabsCenter) {
		this.tabsCenter = tabsCenter;
	}

	public String getMotDePasseActuel() {
		return motDePasseActuel;
	}

	public void setMotDePasseActuel(String motDePasseActuel) {
		this.motDePasseActuel = motDePasseActuel;
	}

	public String getNouveauMotDePasse() {
		return nouveauMotDePasse;
	}

	public void setNouveauMotDePasse(String nouveauMotDePasse) {
		this.nouveauMotDePasse = nouveauMotDePasse;
	}

	public String getConfirmationMotDePasse() {
		return confirmationMotDePasse;
	}

	public void setConfirmationMotDePasse(String confirmationMotDePasse) {
		this.confirmationMotDePasse = confirmationMotDePasse;
	}

	public TaUtilisateur getU() {
		return u;
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

	public TenantInfo getTenantInfo() {
		return tenantInfo;
	}

	public List<TaDossier> getListeDossier() {
		return listeDossier;
	}

	public void setListeDossier(List<TaDossier> listeDossier) {
		this.listeDossier = listeDossier;
	}

	public String getLoginNouveauDossier() {
		return loginNouveauDossier;
	}

	public void setLoginNouveauDossier(String loginNouveauDossier) {
		this.loginNouveauDossier = loginNouveauDossier;
	}

	public String getMotDePasseNouveauDossier() {
		return motDePasseNouveauDossier;
	}

	public void setMotDePasseNouveauDossier(String motDePasseNouveauDossier) {
		this.motDePasseNouveauDossier = motDePasseNouveauDossier;
	}

	public String getConfirmationMotDePasseNouveauDossier() {
		return confirmationMotDePasseNouveauDossier;
	}

	public void setConfirmationMotDePasseNouveauDossier(
			String confirmationMotDePasseNouveauDossier) {
		this.confirmationMotDePasseNouveauDossier = confirmationMotDePasseNouveauDossier;
	}

	public String getNomNouveauDossier() {
		return nomNouveauDossier;
	}

	public void setNomNouveauDossier(String nomDossier) {
		this.nomNouveauDossier = nomDossier;
	}
	
}
