package fr.legrain.bdg.webapp.app;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.ZoneId;
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
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import fr.legrain.bdg.droits.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.webapp.Auth;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.email.service.LgrEmailSMTPService;
import fr.legrain.moncompte.ws.TaCgPartenaire;
import fr.legrain.moncompte.ws.TaClient;
import fr.legrain.moncompte.ws.TaCommission;
import fr.legrain.moncompte.ws.TaPartenaire;
import fr.legrain.moncompte.ws.TaTypePartenaire;
import fr.legrain.moncompte.ws.client.MonCompteWebServiceClientCXF;

@Named
@ViewScoped 
public class PartenariatBean implements Serializable {

	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;
	
	private MonCompteWebServiceClientCXF wsMonCompte = null;
	
	private static final String CODE_PARTENAIRE_TEMPORAIRE = "tmp";
	
	private static final String CODE_TYPE_PARTENAIRE_REVENDEUR_PRESCRIPTEUR = "PREVPRE"; //"1";"PREVPRE";"Revendeur / Prescripteur"
	private static final String CODE_TYPE_PARTENAIRE_PARRAIN = "PPARRAIN"; //"2";"PPARRAIN";"Parrainage"
	private static final String CODE_TYPE_PARTENAIRE_GROUPEMENT = "PGROUPEMENT"; //"3";"PGROUPEMENT";"Groupement"
	private static final String CODE_TYPE_PARTENAIRE_SALARIE = "PSALARIE"; //"4";"PSALARIE"
	
	private TaUtilisateur u = null;
	private TaClient client = null;
	private TaPartenaire partenaire = null;
//	private TaPartenaire infosPartenaire = null;
	private TaCgPartenaire cgPartenaireCourant = null;
	private TaTypePartenaire typePartenaire = null;
	
	private @EJB LgrEmailSMTPService lgrEmail;
	private BdgProperties bdgProperties;
	private boolean cgPartenaire;
	
	private boolean saisieDetailRevendeur = false;
	private boolean saisieDetailParrain = false;
	private boolean demandePartenariatEnCours = false;
	private boolean partenariatActif= false;
	
	private List<TaCommission> listeCommission = null;
	private TaCommission selectedCommission = null;
	
	private String lienPartenaire;
	private Date datePaiementDernierDossierValidant;
	private BigDecimal chiffreAffaireReference;
	private BigDecimal tauxCommissionReference;
	private BigDecimal decoteTauxCommissionReference;
	
	private Date debutRechercheCom;
	private Date finRechercheCom;
	
	private Date debutPeriodeReference;
	private Date finPeriodeReference;
	
	private StreamedContent fichierCg;
	
//	@Inject private	SessionInfo sessionInfo;
	@Inject private	TenantInfo tenantInfo;
	
	@EJB private ITaUtilisateurServiceRemote userService;
	
	@PostConstruct
	public void init() {
		try {
			u = Auth.findUserInSession();
			bdgProperties = new BdgProperties();
			//infosPartenaire = new TaPartenaire();
			
			LocalDate maintenant = LocalDate.now();
			debutRechercheCom = Date.from(LocalDate.of(maintenant.getYear(), maintenant.getMonthValue(),1).atStartOfDay(ZoneId.systemDefault()).toInstant());
			finRechercheCom = Date.from(LocalDate.of(maintenant.getYear(), maintenant.getMonthValue(),maintenant.lengthOfMonth()).atStartOfDay(ZoneId.systemDefault()).toInstant());
			
			if(u.getAdminDossier()!=null && u.getAdminDossier()) {
				wsMonCompte = new MonCompteWebServiceClientCXF();
				String dossier = tenantInfo.getTenantId();
				client = wsMonCompte.findClientDossier(tenantInfo.getTenantId());
				if(client.getTaPartenaire()!=null) {
					saisieDetailRevendeur = true;
					if(client.getTaPartenaire().getCodePartenaire().equals(CODE_PARTENAIRE_TEMPORAIRE)) {
						demandePartenariatEnCours = true;
					} else {
						if(client.getTaPartenaire().isActif()) {
							partenariatActif = true;
							cgPartenaire = true;
							lienPartenaire = "https://"+bdgProperties.getProperty(BdgProperties.KEY_PREFIXE_SOUS_DOMAINE)+"welcome."+bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE)+"/nouveau_dossier.xhtml?p="+client.getTaPartenaire().getCodePartenaire();
							
//							debutPeriodeReference = Date.from(LocalDate.of(maintenant.getYear(), maintenant.getMonthValue(), 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
//							finPeriodeReference = Date.from(LocalDate.of(maintenant.getYear(), maintenant.getMonthValue(), maintenant.lengthOfMonth()).atStartOfDay(ZoneId.systemDefault()).toInstant());
							debutPeriodeReference = Date.from(LocalDate.of(maintenant.getYear()-1, maintenant.getMonthValue(),1).atStartOfDay(ZoneId.systemDefault()).toInstant());
							finPeriodeReference = Date.from(LocalDate.of(maintenant.getYear(), maintenant.getMonthValue(), maintenant.lengthOfMonth()).minusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
							
							listeCommission = new ArrayList<>();
							listeCommission = wsMonCompte.findCommissionPartenaire(client.getTaPartenaire().getCodePartenaire(), debutRechercheCom, finRechercheCom);
							datePaiementDernierDossierValidant = wsMonCompte.findDerniereCreationDossierPayantPartenaire(client.getTaPartenaire().getCodePartenaire());
							chiffreAffaireReference = wsMonCompte.findMontantVentePartenaire(client.getTaPartenaire().getCodePartenaire(),debutPeriodeReference,finPeriodeReference);
							tauxCommissionReference = wsMonCompte.findTauxCommissionPartenaireSansDecote(client.getTaPartenaire().getCodePartenaire());
							decoteTauxCommissionReference = wsMonCompte.findDecoteTauxCommissionPartenaire(client.getTaPartenaire().getCodePartenaire());
						}
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void actRechercheCom(ActionEvent e) {
		try {
			listeCommission = wsMonCompte.findCommissionPartenaire(client.getTaPartenaire().getCodePartenaire(), debutRechercheCom, finRechercheCom);
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void actFermer() {

	}
	
	public void actSaisieDetailRevendeur(ActionEvent e) {
		//vérifier que le client n'est pas deja partenaire
		if(client.getTaPartenaire()==null) {
			client.setTaPartenaire(new TaPartenaire());
			saisieDetailRevendeur = true;
		}
	}
	
	public void actSaisieDetailParrain(ActionEvent e) {
		//vérifier que le client n'est pas deja partenaire
		if(client.getTaPartenaire()==null) {
			client.setTaPartenaire(new TaPartenaire());
			saisieDetailParrain = true;
		}
	}
	
	public void actDemandeCodeRevendeurPrescripteur(ActionEvent e) {
		try {	
			//vérifier que le client n'est pas deja partenaire
			//if(client.getTaPartenaire()==null) {
				if(cgPartenaire) {
					//recupérer le type de partenaire via le web service
					TaTypePartenaire type = wsMonCompte.findTypePartenaire(CODE_TYPE_PARTENAIRE_REVENDEUR_PRESCRIPTEUR);
					//récuperer le client via le ws
					//créer et initialisé l'objet partenaire avec un code temporaire ou booleen actif
					//TaPartenaire part = in
					client.getTaPartenaire().setTaTypePartenaire(type);
					client.getTaPartenaire().setTaCgPartenaire(cgPartenaireCourant);
					client.getTaPartenaire().setActif(false);
					client.getTaPartenaire().setCodePartenaire(CODE_PARTENAIRE_TEMPORAIRE);
					//demander la saisie des infos complémentaires, RIB
					//accepter les conditions générales
					//l'affecter au client
					//client.setTaPartenaire(part);
					//merge du client/partenaire
					wsMonCompte.mergeClient(client);
					//envoyer un mail au client et à legrain
					
					//envoyer autre email au client/partenaire quand on valide/active le partenariat dans "moncompte"
					emailConfirmationDemande(null);
			
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void actModifInfosRevendeurPrescripteur(ActionEvent e) {
		try {	
			if(cgPartenaire && client.getTaPartenaire()!=null && client.getTaPartenaire().isActif()) {
				wsMonCompte.mergeClient(client);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void actModifInfosRevendeurParrain(ActionEvent e) {
		try {	
			if(cgPartenaire && client.getTaPartenaire()!=null && client.getTaPartenaire().isActif()) {
				wsMonCompte.mergeClient(client);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void actDemandeCodeParrainage(ActionEvent e) {
		try {
			if(client.getTaPartenaire()==null) {
				if(cgPartenaire) {
					TaTypePartenaire type = wsMonCompte.findTypePartenaire(CODE_TYPE_PARTENAIRE_PARRAIN);
					
				}
			}
		} catch(Exception ex) {
			
		}
	}
	
	public void emailConfirmationDemande(ActionEvent event) {
//		String subject = "mon sujet ..";
//		String msgTxt = "mon email ...";
//		String fromEmail = "nicolas@legrain.fr";
//		String fromName = "Nicolas";
		
		String subject = bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE)+" - Confirmation de votre demande de code Alliance";
//		String msgTxt = "Votre paiement a bien été pris en compte : "+panier.getRefPaiement();
		
		String msgTxt = "Bonjour "+(client.getPrenom()!=null?client.getPrenom():"") +" "+client.getNom()+",\n"
				+"\n"
				+"Nous avons bien reçu votre demande de code Alliance pour devenir partenaire.\n"
				+"\n"
				+"Nos services étudient votre dossier et vous répondront dans les meilleurs délais\n"
				+"\n"
				+"Legrain Informatique vous remercie de votre confiance.\n"
				+"\n"
				+"Très cordialement,\n"
				+"\n"
				+"Service Gestion \n"
				;
		
		String fromEmail = bdgProperties.getProperty(BdgProperties.KEY_SMTP_LOGIN);
		String fromName = "Bureau de Gestion";
		String[][] toEmailandName = new String[][]{ {client.getTaUtilisateur().getEmail(),client.getNom()}};
		
		lgrEmail.sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
		
		//envoie d'une copie à legrain
		toEmailandName = new String[][]{ {"notifications@bdg.cloud",null}};
		lgrEmail.sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
		//TODO faire avec BCC
	}
	
	public StreamedContent getFichierCg() {
		   // InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/demo/images/optimus.jpg");
	       // file = new DefaultStreamedContent(stream, "image/jpg", "downloaded_optimus.jpg");
		
		try {
			cgPartenaireCourant = wsMonCompte.findCgPartenaireCourant();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputStream stream = new ByteArrayInputStream(cgPartenaireCourant.getBlobFichier()); 
		fichierCg = new DefaultStreamedContent(stream,null,"cg-partenaire.pdf");
		return fichierCg;
	}
	
	public void actDialogCg(ActionEvent actionEvent) {
		    
		Map<String,Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", true);
		options.put("resizable", true);
		options.put("contentHeight", 1024);
		options.put("contentWidth", 1024);

		Map<String,List<String>> params = new HashMap<String,List<String>>();
		List<String> list = new ArrayList<String>();
		//list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
		params.put("modeEcranDefaut", list);

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		try {
			cgPartenaireCourant = wsMonCompte.findCgPartenaireCourant();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sessionMap.put("cgv", cgPartenaire);

		PrimeFaces.current().dialog().openDynamic("/admin/dialog_cg_partenaire", options, params);
	}
	
	public void handleReturnDialogCgv(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			//taTTiers = (TaTTiers) event.getObject();
		}
	}
	
	public TabListModelBean getTabsCenter() {
		return tabsCenter;
	}

	public void setTabsCenter(TabListModelBean tabsCenter) {
		this.tabsCenter = tabsCenter;
	}

	public TaUtilisateur getU() {
		return u;
	}

	public TenantInfo getTenantInfo() {
		return tenantInfo;
	}

	public MonCompteWebServiceClientCXF getWsMonCompte() {
		return wsMonCompte;
	}

	public void setWsMonCompte(MonCompteWebServiceClientCXF wsMonCompte) {
		this.wsMonCompte = wsMonCompte;
	}

	public TaClient getClient() {
		return client;
	}

	public void setClient(TaClient client) {
		this.client = client;
	}

	public TaPartenaire getPartenaire() {
		return partenaire;
	}

	public void setPartenaire(TaPartenaire partenaire) {
		this.partenaire = partenaire;
	}

	public TaCgPartenaire getCgPartenaireCourant() {
		return cgPartenaireCourant;
	}

	public void setCgPartenaireCourant(TaCgPartenaire cgPartenaireCourant) {
		this.cgPartenaireCourant = cgPartenaireCourant;
	}

	public TaTypePartenaire getTypePartenaire() {
		return typePartenaire;
	}

	public void setTypePartenaire(TaTypePartenaire typePartenaire) {
		this.typePartenaire = typePartenaire;
	}

	public boolean isCgPartenaire() {
		return cgPartenaire;
	}

	public void setCgPartenaire(boolean cgPartenaire) {
		this.cgPartenaire = cgPartenaire;
	}

	public void setFichierCg(StreamedContent fichierCg) {
		this.fichierCg = fichierCg;
	}

//	public TaPartenaire getInfosPartenaire() {
//		return infosPartenaire;
//	}
//
//	public void setInfosPartenaire(TaPartenaire infosPartenaire) {
//		this.infosPartenaire = infosPartenaire;
//	}

	public boolean isSaisieDetailRevendeur() {
		return saisieDetailRevendeur;
	}

	public void setSaisieDetailRevendeur(boolean saisieDetailRevendeur) {
		this.saisieDetailRevendeur = saisieDetailRevendeur;
	}

	public boolean isDemandePartenariatEnCours() {
		return demandePartenariatEnCours;
	}

	public void setDemandePartenariatEnCours(boolean demandePartenariatEnCours) {
		this.demandePartenariatEnCours = demandePartenariatEnCours;
	}

	public boolean isPartenariatActif() {
		return partenariatActif;
	}

	public void setPartenariatActif(boolean partenariatActif) {
		this.partenariatActif = partenariatActif;
	}

	public String getLienPartenaire() {
		return lienPartenaire;
	}

	public void setLienPartenaire(String lienPartenaire) {
		this.lienPartenaire = lienPartenaire;
	}

	public List<TaCommission> getListeCommission() {
		return listeCommission;
	}

	public void setListeCommission(List<TaCommission> listeCommission) {
		this.listeCommission = listeCommission;
	}

	public TaCommission getSelectedCommission() {
		return selectedCommission;
	}

	public void setSelectedCommission(TaCommission selectedCommission) {
		this.selectedCommission = selectedCommission;
	}

	public Date getDatePaiementDernierDossierValidant() {
		return datePaiementDernierDossierValidant;
	}

	public void setDatePaiementDernierDossierValidant(
			Date datePaiementDernierDossierValidant) {
		this.datePaiementDernierDossierValidant = datePaiementDernierDossierValidant;
	}

	public Date getDebutRechercheCom() {
		return debutRechercheCom;
	}

	public void setDebutRechercheCom(Date debutRechercheCom) {
		this.debutRechercheCom = debutRechercheCom;
	}

	public Date getFinRechercheCom() {
		return finRechercheCom;
	}

	public void setFinRechercheCom(Date finRechercheCom) {
		this.finRechercheCom = finRechercheCom;
	}

	public BigDecimal getChiffreAffaireReference() {
		return chiffreAffaireReference;
	}

	public void setChiffreAffaireReference(BigDecimal chiffreAffaireReference) {
		this.chiffreAffaireReference = chiffreAffaireReference;
	}

	public BigDecimal getTauxCommissionReference() {
		return tauxCommissionReference;
	}

	public void setTauxCommissionReference(BigDecimal tauxCommissionReference) {
		this.tauxCommissionReference = tauxCommissionReference;
	}

	public BigDecimal getDecoteTauxCommissionReference() {
		return decoteTauxCommissionReference;
	}

	public void setDecoteTauxCommissionReference(
			BigDecimal decoteTauxCommissionReference) {
		this.decoteTauxCommissionReference = decoteTauxCommissionReference;
	}

	public Date getDebutPeriodeReference() {
		return debutPeriodeReference;
	}

	public void setDebutPeriodeReference(Date debutPeriodeReference) {
		this.debutPeriodeReference = debutPeriodeReference;
	}

	public Date getFinPeriodeReference() {
		return finPeriodeReference;
	}

	public void setFinPeriodeReference(Date finPeriodeReference) {
		this.finPeriodeReference = finPeriodeReference;
	}

	public boolean isSaisieDetailParrain() {
		return saisieDetailParrain;
	}

	public void setSaisieDetailParrain(boolean saisieDetailParrain) {
		this.saisieDetailParrain = saisieDetailParrain;
	}
	
}
