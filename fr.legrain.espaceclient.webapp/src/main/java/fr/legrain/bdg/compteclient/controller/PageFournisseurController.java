package fr.legrain.bdg.compteclient.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import fr.legrain.article.model.TaLot;
import fr.legrain.bdg.compteclient.model.TaMesFournisseurs;
import fr.legrain.bdg.compteclient.service.interfaces.remote.ITaMesFournisseursServiceRemote;
import fr.legrain.bdg.compteclient.ws.TaFacture;
import fr.legrain.bdg.ws.client.CompteClientServiceClientCXF;

@ManagedBean //Est responsable de la gestion des opérations sur une entité notamment grâce à plusieurs méthodes
@ViewScoped
public class PageFournisseurController implements Serializable {

	private static final long serialVersionUID = 987460187689622614L;

	@EJB private ITaMesFournisseursServiceRemote serviceMesFournisseurs;

	private TaMesFournisseurs mesFournisseurs = null;
	private CompteClientServiceClientCXF wsCompteClient;

	private String codeDossierFrs = null;
	
	private boolean fournisseurPermetPaiementCB;

	private String id=null;
	private String codeClient = null;

	private StreamedContent file;	
	private List<TaFacture> listeFacture = null;

	private TaFacture selectedFacture = null;

	@ManagedProperty ("#{c_langue}")
	private ResourceBundle cLangue;

	@PostConstruct
	public void init() {
		System.out.println("passage init");
		refresh();
	}

	public void refresh() {
		try {
			System.out.println("TestController.init()");
			System.out.println(id);

			if(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idFrs")!=null) {
				id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idFrs");
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idFrsPageFournisseur", id);
			} else {
				id = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idFrsPageFournisseur");
			}

			if(id!=null) {
				wsCompteClient = new CompteClientServiceClientCXF();
				mesFournisseurs = serviceMesFournisseurs.findById(Integer.parseInt(id));
				codeDossierFrs = mesFournisseurs.getTaFournisseur().getCodeDossier();
				codeClient = mesFournisseurs.getCodeClient();
				
				fournisseurPermetPaiementCB = wsCompteClient.fournisseurPermetPaiementParCB(codeDossierFrs, codeClient);

				rechercheFacture(null);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public PageFournisseurController() {

	}

	public void rechercheFacture(ActionEvent e) {
		try {
			Date debutRecherche;
			Date finRecherche;
			LocalDate maintenant = LocalDate.now();
			debutRecherche = Date.from(LocalDate.of(maintenant.getYear(), 1,1).atStartOfDay(ZoneId.systemDefault()).toInstant());
			finRecherche = Date.from(LocalDate.of(maintenant.getYear(), 12,31).atStartOfDay(ZoneId.systemDefault()).toInstant());


			listeFacture = wsCompteClient.facturesClientChezFournisseur(codeDossierFrs, codeClient, debutRecherche, finRecherche);
			if(listeFacture!=null && !listeFacture.isEmpty()) {
				for (TaFacture taFacture : listeFacture) {
					System.out.println("Facture : "+taFacture.getCodeDocument());
				}
			if(listeFacture!=null && listeFacture.isEmpty()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", "Votre accés est restreint, veuillez contacter votre fournisseur"));

					}
			}else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", cLangue.getString("page_fournisseur_alerte_objet_vide")));
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void download(ActionEvent e) {
		System.out.println("TestController.download()");
		try {
			String codeDoc = (String) e.getComponent().getAttributes().get("code");
			System.out.println("pageFournisseurController.download() : "+codeDoc);

			File f = wsCompteClient.pdfFacture(codeDossierFrs, codeDoc);
			setFile(new DefaultStreamedContent(new FileInputStream(f), "application/pdf", "Facture_"+codeDoc+".pdf"));
		} catch(Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public String payerFacture(TaFacture f) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("mesfrs", mesFournisseurs);
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fact", f);
		
//		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//		Map<String, Object> sessionMap = externalContext.getSessionMap();
//		taLot = (TaLot) sessionMap.get("lotBR");
		
		return "paiement-cb.xhtml?faces-redirect=true";
	}
	
	public void actPayerFacture(ActionEvent e) {
		System.out.println("PageFournisseurController.payerFacture()");
		
	}

	public TaFacture getSelectedFacture() {
		return selectedFacture;
	}

	public void setSelectedFacture(TaFacture selectedFacture) {
		this.selectedFacture = selectedFacture;
	}

	public TaMesFournisseurs getMesFournisseurs() {
		return mesFournisseurs;
	}

	public void setMesFournisseurs(TaMesFournisseurs mesFournisseurs) {
		this.mesFournisseurs = mesFournisseurs;
	}
	
	public ResourceBundle getcLangue() {
		return cLangue;
	}

	public void setcLangue(ResourceBundle cLangue) {
		this.cLangue = cLangue;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	public String getCodeDossierFrs() {
		return codeDossierFrs;
	}

	public void setCodeDossierFrs(String codeDossierFrs) {
		this.codeDossierFrs = codeDossierFrs;
	}

	public String getCodeClient() {
		return codeClient;
	}

	public void setCodeClient(String codeClient) {
		this.codeClient = codeClient;
	}

	public List<TaFacture> getListeFacture() {
		return listeFacture;
	}

	public void setListeFacture(List<TaFacture> listeFacture) {
		this.listeFacture = listeFacture;
	}

	public boolean isFournisseurPermetPaiementCB() {
		return fournisseurPermetPaiementCB;
	}

	public void setFournisseurPermetPaiementCB(boolean fournisseurPermetPaiementCB) {
		this.fournisseurPermetPaiementCB = fournisseurPermetPaiementCB;
	}

}
