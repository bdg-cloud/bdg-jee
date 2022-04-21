package fr.legrain.bdg.compteclient.webapp;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import fr.legrain.bdg.compteclient.ws.TaFacture;
import fr.legrain.bdg.compteclient.ws.TaTiers;
import fr.legrain.bdg.compteclient.ws.TiersDossier;
import fr.legrain.bdg.ws.client.CompteClientServiceClientCXF;

@ManagedBean
@ViewScoped
public class TestController {
	
	private CompteClientServiceClientCXF wsCompteClient;
	private List<TiersDossier> listeFrs = null;
	
	private TiersDossier selectedDossier = null;
	private String codeDossierFrs = null;
	private String codeClient = null;
	
	private TaTiers clientFinal = null;
	
	private StreamedContent file;
	
	private StreamedContent logo;
	
	private List<TaFacture> listeFacture = null;
	private TaFacture selectedFacture = null;
	
	@PostConstruct
	public void init() {
		try {
			
			System.out.println("TestController.init()");
			
			wsCompteClient = new CompteClientServiceClientCXF();
			
	//		codeClient = "3133";
			codeClient = "3818";
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void test(ActionEvent e) {
		try {
			refeshListeFournisseur(e);
			//wsCompteClient = new CompteClientServiceClientCXF();
			
			rechercheClient(e);
			rechercheFacture(e);
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	public void refeshListeFournisseur(ActionEvent e) {
		try {
			//wsCompteClient = new CompteClientServiceClientCXF();
			listeFrs = wsCompteClient.listeFournisseur();
			//dossier = frs.keySet();
			System.out.println("TestController.test()");
			if(listeFrs!=null) {
				for (TiersDossier td : listeFrs) {
					System.out.println("Dossier : "+td.getCodeDossier());
					codeDossierFrs = td.getCodeDossier();
					System.out.println("Tiers : "+td.getTaTiers().getNomTiers());
					System.out.println("***********************************************");
				}
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void updateFrs() {
		if(selectedDossier.getTaTiers().getBlobLogo()!=null) {
			InputStream stream = new ByteArrayInputStream(selectedDossier.getTaTiers().getBlobLogo()); 
			//logo = new DefaultStreamedContent(stream, "image/png");
			logo = new DefaultStreamedContent(stream);
		}
	}
	
	public void rechercheClient(ActionEvent e) {
//		clientFinal = null;
//		codeDossierFrs = selectedDossier.getCodeDossier();
//		boolean b = wsCompteClient.clientExisteChezFournisseur(codeDossierFrs, codeClient);
//		if(b) {
//			clientFinal = wsCompteClient.infosClientChezFournisseur(codeDossierFrs, codeClient);
//			System.out.println("Nom : "+clientFinal.getNomTiers());
//		}
	}
	
	public void rechercheFacture(ActionEvent e) {
		try {
			codeDossierFrs = selectedDossier.getCodeDossier();
			if(clientFinal!=null) {
				
				Date debutRecherche;
				Date finRecherche;
				LocalDate maintenant = LocalDate.now();
	//			debutRecherche = Date.from(LocalDate.of(maintenant.getYear(), maintenant.getMonthValue(),1).atStartOfDay(ZoneId.systemDefault()).toInstant());
	//			finRecherche = Date.from(LocalDate.of(maintenant.getYear(), maintenant.getMonthValue(),maintenant.lengthOfMonth()).atStartOfDay(ZoneId.systemDefault()).toInstant());
				debutRecherche = Date.from(LocalDate.of(maintenant.getYear(), 1,1).atStartOfDay(ZoneId.systemDefault()).toInstant());
				finRecherche = Date.from(LocalDate.of(maintenant.getYear(), 12,31).atStartOfDay(ZoneId.systemDefault()).toInstant());
			
				
				listeFacture = wsCompteClient.facturesClientChezFournisseur(codeDossierFrs, codeClient, debutRecherche, finRecherche);
				if(listeFacture!=null) {
					for (TaFacture taFacture : listeFacture) {
						System.out.println("Facture : "+taFacture.getCodeDocument());
					}
				}
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void imprimer(ActionEvent e) {
		System.out.println("TestController.imprimer()");
		codeDossierFrs = selectedDossier.getCodeDossier();
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		System.out.println("TestController.imprimer() : ----- "+selectedFacture.getTaTiers().getCodeTiers());
		sessionMap.put("tiers", selectedFacture.getTaTiers());
		
	}
	
	public void download(ActionEvent e) {
		System.out.println("TestController.download()");
		codeDossierFrs = selectedDossier.getCodeDossier();
		try {
//			File f = wsCompteClient.pdfClient(codeDossierFrs, selectedFacture.getTaTiers().getCodeTiers());
			File f = wsCompteClient.pdfFacture(codeDossierFrs, selectedFacture.getCodeDocument());
//			File f = wsCompteClient.pdfClient(codeDossierFrs, "3133");
			file = new DefaultStreamedContent(new FileInputStream(f), "application/pdf", "Facture_"+selectedFacture.getCodeDocument()+".pdf");
		} catch(Exception e1) {
			e1.printStackTrace();
		}
	}

	public List<TiersDossier> getListeFrs() {
		return listeFrs;
	}

	public void setListeFrs(List<TiersDossier> frs) {
		this.listeFrs = frs;
	}

	public TiersDossier getSelectedDossier() {
		return selectedDossier;
	}

	public void setSelectedDossier(TiersDossier selectedDossier) {
		this.selectedDossier = selectedDossier;
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

	public TaTiers getClientFinal() {
		return clientFinal;
	}

	public void setClientFinal(TaTiers clientFinal) {
		this.clientFinal = clientFinal;
	}

	public List<TaFacture> getListeFacture() {
		return listeFacture;
	}

	public void setListeFacture(List<TaFacture> listeFacture) {
		this.listeFacture = listeFacture;
	}

	public TaFacture getSelectedFacture() {
		return selectedFacture;
	}

	public void setSelectedFacture(TaFacture selectedFacture) {
		this.selectedFacture = selectedFacture;
	}

	public StreamedContent getFile() {
		return file;
	}

	public StreamedContent getLogo() {
		return logo;
	}

	public void setLogo(StreamedContent logo) {
		this.logo = logo;
	}
	
	
}
