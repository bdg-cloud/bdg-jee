package fr.legrain.bdg.webapp;
//package fr.legrain.bdg.webapp;
//
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.Serializable;
//import java.rmi.RemoteException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.GregorianCalendar;
//import java.util.List;
//
//import javax.annotation.PostConstruct;
//import javax.ejb.EJB;
//import javax.faces.application.FacesMessage;
//import javax.faces.bean.ManagedBean;
//import javax.enterprise.context.SessionScoped;
//import javax.faces.view.ViewScoped;
//import javax.faces.context.FacesContext;
//import javax.faces.event.ActionEvent;
//import javax.inject.Inject;
//import javax.xml.datatype.DatatypeConfigurationException;
//import javax.xml.datatype.DatatypeFactory;
//import javax.xml.datatype.XMLGregorianCalendar;
//
//import fr.legrain.autorisations.ws.client.AutorisationWebServiceClientCXF;
//import fr.legrain.bdg.lib.server.osgi.BdgProperties;
//import fr.legrain.bdg.ws.client.Admin;
//import fr.legrain.droits.service.SessionInfo;
//import fr.legrain.droits.service.TenantInfo;
//import fr.legrain.email.LgrEmailSMTPService;
//import fr.legrain.general.service.local.IDatabaseServiceLocal;
//import fr.legrain.lib.data.LibConversion;
//import fr.legrain.lib.data.LibDate;
//import fr.legrain.moncompte.ws.TaAutorisation;
//import fr.legrain.moncompte.ws.TaClient;
//import fr.legrain.moncompte.ws.TaDossier;
//import fr.legrain.moncompte.ws.TaProduit;
//import fr.legrain.moncompte.ws.TaUtilisateur;
//import fr.legrain.moncompte.ws.client.MonCompteWebServiceClientCXF;
//
//@Named
//@ViewScoped
////@SessionScoped 
//public class NouveauDossierBean implements Serializable {  
//	
//
//	private static final long serialVersionUID = 1331238708223531056L;
//
////	private @EJB IAdministrationServiceRemote adminService;
//	private Admin adminBddService = new Admin();
//	private MonCompteWebServiceClientCXF wsMonCompte = null;
//	private AutorisationWebServiceClientCXF wsAutorisation = null;
//	private BdgProperties bdgProperties;
//	private @EJB LgrEmailSMTPService lgrEmail;
//	private @EJB IDatabaseServiceLocal adminService;
//	
//	@Inject private	SessionInfo sessionInfo;
//	@Inject private	TenantInfo tenantInfo;
//	
//	private String nomEntreprise;	
//	private String emailEntreprise;
//	private String nomDossier;
//	private String motDePasse;
//	private String confirmationMotDePasse;
//	private List<String> listeDossier;
//	private List<TaProduit> listeModule;
//	
//	private Boolean actionOk = false;
//	
//	private String[] listeNomDossierTemporaire = new String[]{"tmp1","tmp2","tmp3","tmp4","tmp5"};
//	
//	private TaProduit[] selectedModules;
//	
//	private int idProduit = 4;
//	private boolean tousLesModules = false;
//
//		
//	public void refresh() {
//		
//	}
//	
//	@PostConstruct
//	public void init() {
//		bdgProperties = new BdgProperties();
//		initBdd();
//		
//		try {
//			wsMonCompte = new MonCompteWebServiceClientCXF();
//			wsAutorisation = new AutorisationWebServiceClientCXF();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		initListeModule();
//	}
//	
//	public void initBdd() {
//		//listeDossier = adminBddService.listeBdd();
//		
//	}
//	
//	public void initListeModule() {
//		try {
//			listeModule = wsMonCompte.listeProduit(null, null, null);
//			
//		} catch (RemoteException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		List<TaProduit> listeModuleVendable = new ArrayList<TaProduit>();
//		for (TaProduit p : listeModule) {
//			if(p.isVendable()!=null && p.isVendable()) {
//				listeModuleVendable.add(p);
//			}
//		}
//		listeModule = listeModuleVendable;
//		selectedModules = new TaProduit[listeModuleVendable.size()];
//		selectedModules = listeModuleVendable.toArray(selectedModules);
//		
//		if(tousLesModules) {
//			selectedModules = new TaProduit[listeModule.size()];
//			selectedModules = listeModule.toArray(selectedModules);
//		}
//	}
//	
//	public void ajoutDossier(ActionEvent event) {
//		
//		try {
//			FacesContext context = FacesContext.getCurrentInstance();  
//			
//			//listeDossier = adminBddService.listeSchemaTailleConnection();
//			/*
//			 * Vérication que le nom du dossier et le compte utilisateur(email) n'existe pas
//			 */
//			if(!wsMonCompte.utilisateurExiste(emailEntreprise)) {
//
//				if(!wsMonCompte.dossierExiste(nomDossier)) {		
//
//					if(motDePasse!=null && confirmationMotDePasse!=null 
//							&& !confirmationMotDePasse.equals("") 
//							&& motDePasse.equals(confirmationMotDePasse)) {
//						//le nouveau mot de passe saisie est correct
//
//						if(verifComplexiteMotDePasse(motDePasse)) {
//
//							/*
//							 * création schéma
//							 */
//							//if(!listeDossier.contains(nomDossier))
//							//adminBddService.ajoutDossier(nomDossier);
//							//adminService.ajoutDossier(nomDossier);
//							motDePasse="mdp";
//							adminService.ajoutDossier(nomDossier,emailEntreprise,motDePasse);
//
//							/*
//							 * Création client
//							 */
//							TaClient c = new TaClient();
//							TaUtilisateur u = new TaUtilisateur();
//							TaDossier dossier = new TaDossier();
//							u.setUsername(emailEntreprise);
//							u.setEmail(emailEntreprise);
//							u.setActif(1);
//							u.setPasswd(nomDossier);
//							c.setTaUtilisateur(u);
//							c.setNom(nomEntreprise);
//							c.setCode(nomEntreprise);
//
//							//c.getListeDossier().add(dossier);
//
//							c = wsMonCompte.mergeClient(c);
//							dossier.setTaClient(c);
//							dossier.setActif(true);
//							dossier.setCode(nomDossier);
//							dossier.setNbPosteInstalle(0);
//							dossier.setNbUtilisateur(1);
//							dossier.setAccesWs(false);
//
//
//							/*
//							 * Affectation module
//							 */
//							GregorianCalendar d = new GregorianCalendar();
//							GregorianCalendar d2 = new GregorianCalendar();
//							Date maintenant = new Date();
//							d.setTime(maintenant);
//							d2.setTime(LibDate.incrementDate(maintenant, 45, 0, 0)); //+45 jours
//							XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(d);
//							XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(d2);
//
//							List<TaAutorisation> aut = new ArrayList<TaAutorisation>();
//							TaAutorisation a1 = null;
//							for (TaProduit p : selectedModules) {
//								a1 = new TaAutorisation();
//								a1.setDateAchat(date);
//								a1.setDateFin(date2);
//								a1.setTaProduit(p);
//								a1.setTaDossier(dossier);
//								//				if(!c.getListeAutorisation().contains(a1)) {
//								dossier.getListeAutorisation().add(a1);
//								//				};
//								aut.add(a1);
//							}
//
//							/*
//							 * Enregistrement du client avec ses autorisations
//							 */
//							wsMonCompte.mergeDossier(dossier);
//
//							fr.legrain.autorisation.xml.ListeModules listeXml = new fr.legrain.autorisation.xml.ListeModules();
//							fr.legrain.autorisation.xml.Module moduleXml = null;
//							for (TaAutorisation taAutorisation : aut) {
//								//TODO if(taAutorisation.getDateFin().before(new Date())) { //autorisation encore valide
//								//				moduleXml = new fr.legrain.autorisation.xml.Module();
//								//				moduleXml.nomModule = taAutorisation.getTaProduit().getIdentifiantModule();
//								//				listeXml.module.add(moduleXml);
//								//				System.out.println(moduleXml.nomModule);
//								//				//gestion des dépendances et compostion
//								//				for(TaProduit p : taAutorisation.getTaProduit().getListeProduitDependant()) {
//								//					moduleXml = new fr.legrain.autorisation.xml.Module();
//								//					moduleXml.nomModule = p.getIdentifiantModule();
//								//					listeXml.module.add(moduleXml);
//								//					//TODO appel récursif à faire
//								//				}
//								listeXml = ajouteAutorisation(listeXml,taAutorisation.getTaProduit());
//								listeXml.accessWebservice = dossier.isAccesWs()?"1":"0";
//								listeXml.nbPosteClient = LibConversion.integerToString(dossier.getNbPosteInstalle());
//								listeXml.nbUtilisateur = LibConversion.integerToString(dossier.getNbUtilisateur());
//								//}
//							}
//
//							/*
//							 * MAJ autorisations
//							 */
//							wsAutorisation.createUpdateDroits(c.getCode(), idProduit, listeXml);
//
//							/*
//							 * Initialisation d'un des dossiers temporaires en attendant que les DNS du sous domaine du nouveau dossier soit propagés
//							 */
//							// lister les dossiers/domaine temporaires
//							//adminService.
//							// chercher ceux qui ne sont utilisés
//							// choisir le dossier qui sera utilisé
//							// initialisé le dossier, schéma vide et à jour, mot de passe de l'utilisateur
//							//***
//							// timer pour vérifier quand les DNS sont ok
//							// bascule vers le vrai dossier (renommer le schema)
//							// réinitialisé le dossier temporaire
//
//							/*
//							 * Envoie du mot de passe par email
//							 */
//							emailConfirmation(dossier);
//
//							actionOk = true;
//							
//						    context.addMessage(null, new FacesMessage("Dossier créé ", 
//						    		"Le dossier '"+nomDossier+"' a bien été créé.\n "
//						    		)); 
//						} else {
//							context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Votre nouveau mot de passe est trop simple", "")); 
//						}
//					} else {
//						context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Les valeurs saisie pour le mot de passe et sa confirmation doivent être identique", "")); 
//					}
//				} else {
//					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Ce nom de dossier existe déjà, veuillez en saisir un autre.", "")); 
//				}
//			} else {
//				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Cet utilisateur existe déjà, veulliez vous connecté à votre compte pour céer un nouveau dossier.", "")); 
//			}
//			
//			
//		} catch (RemoteException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (DatatypeConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//	
//	private boolean verifComplexiteMotDePasse(String pwd) {
//		return true;
//	}
//	
//	public void emailConfirmation(TaDossier d) {
////		String subject = "mon sujet ..";
////		String msgTxt = "mon email ...";
////		String fromEmail = "nicolas@legrain.fr";
////		String fromName = "Nicolas";
////		String[][] toEmailandName = new String[][]{ {"nico290382@gmail.com","NP"}};
//		
//		String subject = bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE)+" - Confirmation de la création de votre dossier de gestion";
//		
//		String utilisateur = nomEntreprise;
//		String lienCnx = "http://"+bdgProperties.getProperty(BdgProperties.KEY_PREFIXE_SOUS_DOMAINE,"")+nomDossier+"."+bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE)+":"+bdgProperties.getProperty(BdgProperties.KEY_PORT_HTTP,"")+"";
//		
//		String msgTxt = "Bonjour "+utilisateur+",\n"
//			+"\n"
//			+"Félicitation ! Votre dossier Bureau de Gestion a bien été créé.\n"
//			+"\n"
//			+"Dès maintenant, vous pouvez vous connecter sur votre Bureau de Gestion à l'adresse suivante :\n"
//			+"\n"
//			+""+lienCnx+"\n"
//			+"\n"
//			+"Nom d'utilisateur : "+emailEntreprise+"\n"
//			+"\n"
//			+"Mot de passe : celui défini lors de la création du dossier.\n"
//			+"\n"
//			+"Vous bénéficiez de 45 jours d'utilisation gratuite sans engagement avec tous les modules disponibles. A l'issue de cette période, vous pourrez  choisir les modules dont vous avez besoin, en ajouter ou en supprimer et cela à tout moment.\n"
//			+"\n"
//			+"Si vous avez besoin d'aide, contactez le support, soit par téléphone au 05.63.30.31.44, soit par email à support@legrain.fr\n"
//			+"\n"
//			+"Pour bénéficier d'une formation, il suffit de contacter notre service commercial au 05.63.30.31.44. Nous étudierons ensemble votre besoin et les prises en charge possible.\n"
//			+"\n"
//			+"nous vous souhaitons une bonne utilisation de votre Bureau de Gestion.\n"
//			+"\n"
//			+"Très cordialement,\n"
//			+"\n"
//			+"Service Gestion \n"
//			;
//		
//		
//		String fromEmail = bdgProperties.getProperty(BdgProperties.KEY_SMTP_LOGIN);
//		String fromName = "Bureau de Gestion";
//		String[][] toEmailandName = new String[][]{ {d.getTaClient().getTaUtilisateur().getEmail(),d.getTaClient().getNom()}};
//		
//		lgrEmail.sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
//		
//		//envoie d'une copie à legrain
//		toEmailandName = new String[][]{ {"notifications@bdg.cloud",null}};
//		lgrEmail.sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
//		//TODO faire avec BCC
//	}
//	
//	/**
//	 * COPIER DE ClientController dans MonCompte à déplacer dans les services si besoin
//	 * @param l
//	 * @param p
//	 * @return
//	 */
//	public fr.legrain.autorisation.xml.ListeModules ajouteAutorisation(fr.legrain.autorisation.xml.ListeModules l, TaProduit p) {
//		fr.legrain.autorisation.xml.Module moduleXml = new fr.legrain.autorisation.xml.Module();
//		moduleXml.id = p.getIdentifiantModule();
//		if(!l.contientModule(moduleXml)) {
//			l.module.add(moduleXml);
//		}
//		System.out.println(moduleXml.id);
//		//gestion des dépendances et compostion
//		for(TaProduit prod : p.getListeProduitDependant()) {
//			moduleXml = new fr.legrain.autorisation.xml.Module();
//			moduleXml.id = prod.getIdentifiantModule();
//			if(!l.contientModule(moduleXml)) {
//				l.module.add(moduleXml);
//			}
//			//TODO appel récursif à faire
//			if(prod.getListeProduitDependant()!=null) {
//				l = ajouteAutorisation(l,prod);
//			}
//		}
//		for(TaProduit prod : p.getListeSousProduit()) {
//			moduleXml = new fr.legrain.autorisation.xml.Module();
//			moduleXml.id = prod.getIdentifiantModule();
//			if(!l.contientModule(moduleXml)) {
//				l.module.add(moduleXml);
//			}
//			//TODO appel récursif à faire
//			if(prod.getListeSousProduit()!=null) {
//				l = ajouteAutorisation(l,prod);
//			}
//		}
//		return l;
//	}
//
//	public String getNomDossier() {
//		return nomDossier;
//	}
//
//	public void setNomDossier(String nomDossier) {
//		this.nomDossier = nomDossier;
//	}
//
//	public List<TaProduit> getListeModule() {
//		return listeModule;
//	}
//
//	public TaProduit[] getSelectedModules() {
//		return selectedModules;
//	}
//
//	public void setSelectedModules(TaProduit[] selectedModules) {
//		this.selectedModules = selectedModules;
//	}
//
//	public String getNomEntreprise() {
//		return nomEntreprise;
//	}
//
//	public void setNomEntreprise(String nomEntreprise) {
//		this.nomEntreprise = nomEntreprise;
//	}
//
//	public String getEmailEntreprise() {
//		return emailEntreprise;
//	}
//
//	public void setEmailEntreprise(String emailEntreprise) {
//		this.emailEntreprise = emailEntreprise;
//	}
//
//	public String getMotDePasse() {
//		return motDePasse;
//	}
//
//	public void setMotDePasse(String motDePasse) {
//		this.motDePasse = motDePasse;
//	}
//
//	public String getConfirmationMotDePasse() {
//		return confirmationMotDePasse;
//	}
//
//	public void setConfirmationMotDePasse(String confirmationMotDePasse) {
//		this.confirmationMotDePasse = confirmationMotDePasse;
//	}
//
//	public Boolean getActionOk() {
//		return actionOk;
//	}
//
//	public void setActionOk(Boolean actionOk) {
//		this.actionOk = actionOk;
//	}
//	
//}  
//              