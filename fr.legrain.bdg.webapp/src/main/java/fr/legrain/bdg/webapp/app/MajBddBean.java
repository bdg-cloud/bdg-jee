package fr.legrain.bdg.webapp.app;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import fr.legrain.bdg.droits.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.general.service.local.IDatabaseServiceLocal;

@Named
@ViewScoped 
public class MajBddBean implements Serializable {

	@EJB private ITaUtilisateurServiceRemote userService;
	@EJB private IDatabaseServiceLocal databaseService;
	@Inject SessionInfo sessionInfo;
	@Inject TenantInfo tenantInfo;


	private File fichierSQLMaj = null;
	private File fileLogMaj = null;
	private StreamedContent fichierLogMaj;
	private BdgProperties bdgProperties;
	private String nomFichierARenommer;

	@PostConstruct
	public void init() {
		//u = Auth.findUserInSession();
		bdgProperties = new BdgProperties();
	}

	public void majBdd(ActionEvent e) {
		try {
			databaseService.backupDB("avant_maj_bdd");
			fileLogMaj = databaseService.majBdd(fichierSQLMaj.getPath());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public StreamedContent getFichierLogMaj() {
		try {
			if(fileLogMaj!=null) {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
				Date dateSysteme = formatter.parse(fileLogMaj.getName().replace("_log.txt",""));
				SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd-HHmmss");
				nomFichierARenommer = formatter2.format(dateSysteme)+".sql";
				
				Path path = Paths.get(fileLogMaj.getPath());
				byte[] data = Files.readAllBytes(path);
				InputStream stream = new ByteArrayInputStream(data); 
				fichierLogMaj = new DefaultStreamedContent(stream,null,fileLogMaj.getName());
				return fichierLogMaj;
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return fichierLogMaj;
	}

	public void handleFileUpload(FileUploadEvent event) {
		FacesMessage message = new FacesMessage("Fichier", event.getFile().getFileName() + " envoye sur le serveur.");
		FacesContext.getCurrentInstance().addMessage(null, message);

		try {

			String localPath = bdgProperties.osTempDirDossier(tenantInfo.getTenantId())+"/"+bdgProperties.tmpFileName(event.getFile().getFileName());
			Files.copy(event.getFile().getInputStream(), Paths.get(localPath));

			fichierSQLMaj = new File(localPath);
			System.out.println("MajBddBean.handleFileUpload() "+localPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public File getFichierSQLMaj() {
		return fichierSQLMaj;
	}

	public void setFichierSQLMaj(File fichierSQLMaj) {
		this.fichierSQLMaj = fichierSQLMaj;
	}

	public File getFileLogMaj() {
		return fileLogMaj;
	}

	public void setFileLogMaj(File fileLogMaj) {
		this.fileLogMaj = fileLogMaj;
	}

	public void setFichierLogMaj(StreamedContent fichierLogMaj) {
		this.fichierLogMaj = fichierLogMaj;
	}

	public String getNomFichierARenommer() {
		return nomFichierARenommer;
	}

	public void setNomFichierARenommer(String nomFichierARenommer) {
		this.nomFichierARenommer = nomFichierARenommer;
	}




}
