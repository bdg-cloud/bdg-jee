package fr.legrain.moncompte.admin.webapp.admin;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.primefaces.model.StreamedContent;

import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.moncompte.service.remote.ITaClientServiceRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaCommissionServiceRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.moncompte.admin.webapp.app.LgrTab;
import fr.legrain.moncompte.admin.webapp.app.TabListModelBean;
import fr.legrain.moncompte.model.TaCgPartenaire;
import fr.legrain.moncompte.model.TaClient;
import fr.legrain.moncompte.model.TaCommission;
import fr.legrain.moncompte.model.TaPartenaire;
import fr.legrain.moncompte.model.TaTypePartenaire;
import fr.legrain.moncompte.service.TenantInfo;

@ManagedBean
@ViewScoped 
public class PartenaireBean implements Serializable {

	@ManagedProperty(value="#{tabListModelCenterBean}")
	private TabListModelBean tabsCenter;
	
	private TaClient client = null;
	private TaPartenaire partenaire = null;
//	private TaPartenaire infosPartenaire = null;
	private TaCgPartenaire cgPartenaireCourant = null;
	private TaTypePartenaire typePartenaire = null;
	
	private String paramPartenaire;
	
//	private @EJB LgrEmail lgrEmail;
	private BdgProperties bdgProperties;
	private boolean cgPartenaire;
	
	private boolean saisieDetailRevendeur = false;
	private boolean demandePartenariatEnCours = false;
	private boolean partenariatActif= false;
	
	private List<TaCommission> listeCommission = null;
	private TaCommission selectedCommission = null;
	private TaCommission[] selectedCommissions; 
	
	private String lienPartenaire;
	private Date datePaiementDernierDossierValidant;
	
	private Date debutRechercheCom;
	private Date finRechercheCom;
	
	private StreamedContent fichierCg;
	
//	@Inject private	SessionInfo sessionInfo;
	@Inject private	TenantInfo tenantInfo;
	
	@EJB private ITaUtilisateurServiceRemote userService;
	@EJB private ITaClientServiceRemote clientServiceRemote;
	@EJB private ITaCommissionServiceRemote commissionServiceRemote;
	
	@PostConstruct
	public void init() {
		try {
//			u = Auth.findUserInSession();
			bdgProperties = new BdgProperties();
			//infosPartenaire = new TaPartenaire();
			
			selectedCommission = new TaCommission(); //pour éviter le null à l'affichage de la page, à cause du dialogue "commentaire"
			
			LocalDate maintenant = LocalDate.now();
			debutRechercheCom = Date.from(LocalDate.of(maintenant.getYear(), maintenant.getMonthValue(),1).atStartOfDay(ZoneId.systemDefault()).toInstant());
			finRechercheCom = Date.from(LocalDate.of(maintenant.getYear(), maintenant.getMonthValue(),maintenant.lengthOfMonth()).atStartOfDay(ZoneId.systemDefault()).toInstant());
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void preRenderView() {
		System.out.println("paramPartenaire : "+paramPartenaire);
		//clientServiceRemote.
	}
	
	public void openPartenaire() {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.partenaire");
		b.setTitre("Partenaire");
		b.setTemplate("admin/partenaire.xhtml");
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARTENAIRE);
		tabsCenter.ajouterOnglet(b);
		
		if(client!=null) {
			partenaire = client.getTaPartenaire();
			lienPartenaire = "https://"+bdgProperties.getProperty(BdgProperties.KEY_PREFIXE_SOUS_DOMAINE)+"welcome."+bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE)+"/nouveau_dossier.xhtml?p="+client.getTaPartenaire().getCodePartenaire();
			listeCommission = commissionServiceRemote.findCommissionPartenaire(partenaire.getCodePartenaire(),debutRechercheCom,finRechercheCom);
			datePaiementDernierDossierValidant = commissionServiceRemote.findDerniereCreationDossierPayantPartenaire(partenaire.getCodePartenaire());
			if(listeCommission!=null && !listeCommission.isEmpty()) {
				selectedCommission = listeCommission.get(0);
			}
		}
	}
	
	public void payeCommission() {  
		payeCommission(selectedCommission);
	}
	
	public void genereAvoir() {  
		payeCommission(selectedCommission);
	}
	
	public void actAfficheDialogCommentaire() { 
		//l'appel d'une action permet d'utiliser "<f:setPropertyActionListener ... >" sur la commande et d'initialisé l'objet sur le quel travaillera de dialogue
		
		//System.out.println(selectedCommission.toString());
	}
	
	public void payeCommission(TaCommission com) { 
		if(com.getCommissionPayee()==null || !com.getCommissionPayee()) {
			com.setCommissionPayee(true);
			com.setDatePaiementCommission(new Date());
			commissionServiceRemote.merge(com);
		}
	}
	
	public void actFermer() {

	}
	
	public void actRechercheCom(ActionEvent e) {
		listeCommission = commissionServiceRemote.findCommissionPartenaire(partenaire.getCodePartenaire(),debutRechercheCom,finRechercheCom);
	}
	
	public void actPayerSelection(ActionEvent e) {
		for (TaCommission com : selectedCommissions) {
			payeCommission(com);
		}
	}
	
	public void actEnregistrerCommentaire(ActionEvent e) {
		commissionServiceRemote.merge(selectedCommission);
	}
	
	public void actSaisieDetailRevendeur(ActionEvent e) {
		//vérifier que le client n'est pas deja partenaire
		if(client.getTaPartenaire()==null) {
			client.setTaPartenaire(new TaPartenaire());
			saisieDetailRevendeur = true;
		}
	}
	
	
	
	public void emailConfirmationDemande(ActionEvent event) {
////		String subject = "mon sujet ..";
////		String msgTxt = "mon email ...";
////		String fromEmail = "nicolas@legrain.fr";
////		String fromName = "Nicolas";
////		String[][] toEmailandName = new String[][]{ {"nico290382@gmail.com","NP"}};
//		
//		String subject = bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE)+" - Confirmation de paiement";
////		String msgTxt = "Votre paiement a bien été pris en compte : "+panier.getRefPaiement();
//		
//		String msgTxt = "Bonjour "+client.getPrenom() +" "+client.getNom()+",\n"
//				+"\n"
//				+"Nous avons bien enregistré votre règlement pour les modules du Bureau de Gestion que vous avez sélectionnés.\n"
//				+"\n"
//				//+"La référence du règlement est : "+panier.getRefPaiement()+"\n"
//				+"\n"
//				+"Si vous avez besoin d'aide, contactez le support, soit par téléphone au 05.63.30.31.44, soit par email à support@legrain.fr\n"
//				+"\n"
//				+"Pour bénéficier d'une formation, il suffit de contacter notre service commercial au 05.63.30.31.44. Nous étudierons ensemble votre besoin et les prises en charge possible.\n"
//				+"\n"
//				+"Très cordialement,\n"
//				+"\n"
//				+"Service Gestion \n"
//				;
//		
//		String fromEmail = bdgProperties.getProperty(BdgProperties.KEY_SMTP_LOGIN);
//		String fromName = "Bureau de Gestion";
//		String[][] toEmailandName = new String[][]{ {client.getTaUtilisateur().getEmail(),client.getNom()}};
//		
//		lgrEmail.sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
//		
//		//envoie d'une copie à legrain
//		toEmailandName = new String[][]{ {"notifications@bdg.cloud",null}};
//		lgrEmail.sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
//		//TODO faire avec BCC
	}
	
	public TabListModelBean getTabsCenter() {
		return tabsCenter;
	}

	public void setTabsCenter(TabListModelBean tabsCenter) {
		this.tabsCenter = tabsCenter;
	}

	public TenantInfo getTenantInfo() {
		return tenantInfo;
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

	public String getParamPartenaire() {
		return paramPartenaire;
	}

	public void setParamPartenaire(String paramPartenaire) {
		this.paramPartenaire = paramPartenaire;
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

	public TaCommission[] getSelectedCommissions() {
		return selectedCommissions;
	}

	public void setSelectedCommissions(TaCommission[] selectedCommissions) {
		this.selectedCommissions = selectedCommissions;
	}
	
}
