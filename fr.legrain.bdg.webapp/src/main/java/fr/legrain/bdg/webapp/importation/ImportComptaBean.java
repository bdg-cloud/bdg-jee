package fr.legrain.bdg.webapp.importation;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

import fr.legrain.bdg.documents.service.remote.ITaFlashServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.importation.service.remote.IImportationServiceRemote;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.document.dto.IDocumentDTO;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.importation.model.Importation;

@Named
@ViewScoped 
public class ImportComptaBean extends AbstractController{
	
	

	


	private FileReader fichierImportTiers = null;
	private FileReader fichierImportArticles = null;
	private File fileLogMaj = null;
	private StreamedContent fichierLogMaj;
	private BdgProperties bdgProperties;
	private String nomFichierARenommer;
	@Inject SessionInfo sessionInfo;
	@Inject TenantInfo tenantInfo;
	

	Importation importation;
	

	
	@EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;

	@EJB private ITaPreferencesServiceRemote PreferencesService;

	@EJB private IImportationServiceRemote ImportationService;

	@EJB private ITaFlashServiceRemote taFlashService;
	
	
	@PostConstruct
	public void init() {
		bdgProperties = new BdgProperties();
	}
	
	public void etatExportationDesDocument(ActionEvent e) {
		
	}
	
	public void actFermer(ActionEvent e) {
		
	}

	public void actImporterAbonnement(ActionEvent e) throws Exception {
//		ImportationService.importationAbonnement();
		ImportationService.importationAbonnementPlusieursLignes();
	}
	
	
	public void actImporterBonReception(ActionEvent e) throws Exception {
		ImportationService.importationBonReception();
	}
	
	public void actImporterArticles(ActionEvent e) throws Exception {
		int deb=1;
		int interval=25;
		boolean retour=true;
		while (retour) {
//			retour=ImportationService.importationArticlesRemanieParDenis(deb,deb+interval);
			retour=ImportationService.importationArticles(deb,deb+interval);
			deb=deb+interval;
		}
	}
	
	public void actImporterTiers(ActionEvent e) throws Exception {
		int deb=1;
		int interval=25;
		boolean retour=true;
		while (retour) {
			retour=ImportationService.importationTiers(deb,deb+interval);
			deb=deb+interval;
		}
	}
	
	public void actTestDivers(ActionEvent e) throws Exception {

	}
	
	
	public void handleFileUploadFichierBonReception(FileUploadEvent event) {
		FacesMessage message = new FacesMessage("Fichier", event.getFile().getFileName() + " envoyé sur le serveur.");
		FacesContext.getCurrentInstance().addMessage(null, message);

		try {

			String localPath = bdgProperties.osTempDirDossier(tenantInfo.getTenantId())+"/"+bdgProperties.tmpFileName(event.getFile().getFileName());
			Files.copy(event.getFile().getInputStream(), Paths.get(localPath));

			ImportationService.setFichierImportBonReception(localPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}	

	
	public void handleFileUploadFichierAbonnement(FileUploadEvent event) {
		FacesMessage message = new FacesMessage("Fichier", event.getFile().getFileName() + " envoyé sur le serveur.");
		FacesContext.getCurrentInstance().addMessage(null, message);

		try {

			String localPath = bdgProperties.osTempDirDossier(tenantInfo.getTenantId())+"/"+bdgProperties.tmpFileName(event.getFile().getFileName());
			Files.copy(event.getFile().getInputStream(), Paths.get(localPath));

			ImportationService.setFichierImportAbonnements(localPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}	
	
	public void handleFileUploadFichierArticles(FileUploadEvent event) {
		FacesMessage message = new FacesMessage("Fichier", event.getFile().getFileName() + " envoyé sur le serveur.");
		FacesContext.getCurrentInstance().addMessage(null, message);

		try {

			String localPath = bdgProperties.osTempDirDossier(tenantInfo.getTenantId())+"/"+bdgProperties.tmpFileName(event.getFile().getFileName());
			Files.copy(event.getFile().getInputStream(), Paths.get(localPath));

			ImportationService.setFichierImportArticles(localPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}	

	public void handleFileUploadFichierTiers(FileUploadEvent event) {
		FacesMessage message = new FacesMessage("Fichier", event.getFile().getFileName() + " envoyé sur le serveur.");
		FacesContext.getCurrentInstance().addMessage(null, message);

		try {

			String localPath = bdgProperties.osTempDirDossier(tenantInfo.getTenantId())+"/"+bdgProperties.tmpFileName(event.getFile().getFileName());
			Files.copy(event.getFile().getInputStream(), Paths.get(localPath));

			ImportationService.setFichierImportTiers(localPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
