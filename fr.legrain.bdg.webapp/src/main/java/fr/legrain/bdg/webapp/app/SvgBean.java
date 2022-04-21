package fr.legrain.bdg.webapp.app;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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
import fr.legrain.bdg.webapp.Auth;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.general.service.local.IDatabaseServiceLocal;
import fr.legrain.hibernate.multitenant.SchemaResolver;

@Named
@ViewScoped 
public class SvgBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5039242841176665140L;
	@EJB private ITaUtilisateurServiceRemote userService;
	@EJB private IDatabaseServiceLocal databaseService;
	@Inject SessionInfo sessionInfo;
	@Inject TenantInfo tenantInfo;
	
	@Inject @Named(value="auth")
	private Auth auth;
	
	private File fichierDump = null;
	private File fichierDumpARestaurer = null;
	private Boolean telechargerDump = true;
	private String tagDump = null;
	private StreamedContent fichierDumpATelecharger;
	private List<String> listeDump;
	private BdgProperties bdgProperties;
	private String nomSchemaDestination;
	
	@PostConstruct
	public void init() {
		//u = Auth.findUserInSession();
		bdgProperties = new BdgProperties();
	}
	
	public void sauvegardeDossier(ActionEvent e) {
		try {
			if(nomSchemaDestination!=null && !nomSchemaDestination.equals("")) {
				databaseService.renameSchema(null, tenantInfo.getTenantId(), nomSchemaDestination);
				fichierDump = databaseService.backupDB(tagDump, nomSchemaDestination);
				databaseService.renameSchema(null, nomSchemaDestination, tenantInfo.getTenantId());
			} else {
				fichierDump = databaseService.backupDB(tagDump, tenantInfo.getTenantId());
			}
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void listeDumpDossier(ActionEvent e) {
		try {
			listeDump = databaseService.listeFichierDump(tenantInfo.getTenantId(),"bdg");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void restaureDossier(ActionEvent e) {
		try {
			databaseService.restaureDB(fichierDumpARestaurer.getPath(), tenantInfo.getTenantId());
			auth.logout();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public StreamedContent getFichierDumpATelecharger() {
		try {
			if(fichierDump!=null) {
				Path path = Paths.get(fichierDump.getPath());
				byte[] data = Files.readAllBytes(path);
				InputStream stream = new ByteArrayInputStream(data); 
				fichierDumpATelecharger = new DefaultStreamedContent(stream,null,fichierDump.getName());
				return fichierDumpATelecharger;
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return fichierDumpATelecharger;
	}
	
	 public void handleFileUpload(FileUploadEvent event) {
	        FacesMessage message = new FacesMessage("Fichier", event.getFile().getFileName() + " envoyé sur le serveur.");
	        FacesContext.getCurrentInstance().addMessage(null, message);
	        
	        try {
	        	
	        	SchemaResolver sr = new SchemaResolver();
				String localPath = bdgProperties.osTempDirDossier(tenantInfo.getTenantId())+"/"+bdgProperties.tmpFileName("dump_upload.backup");
				Files.copy(event.getFile().getInputStream(), Paths.get(localPath));
				
				fichierDumpARestaurer = new File(localPath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	    }

	public File getFichierDump() {
		return fichierDump;
	}

	public void setFichierDump(File fichierDump) {
		this.fichierDump = fichierDump;
	}

	public Boolean getTelechargerDump() {
		return telechargerDump;
	}

	public void setTelechargerDump(Boolean telechargerDump) {
		this.telechargerDump = telechargerDump;
	}

	public String getTagDump() {
		return tagDump;
	}

	public void setTagDump(String tagDump) {
		this.tagDump = tagDump;
	}

	public List<String> getListeDump() {
		return listeDump;
	}

	public void setListeDump(List<String> listeDump) {
		this.listeDump = listeDump;
	}

	public void setFichierDumpATelecharger(StreamedContent fichierDumpATelecharger) {
		this.fichierDumpATelecharger = fichierDumpATelecharger;
	}

	public File getFichierDumpARestaurer() {
		return fichierDumpARestaurer;
	}

	public void setFichierDumpARestaurer(File fichierDumpARestaurer) {
		this.fichierDumpARestaurer = fichierDumpARestaurer;
	}

	public Auth getAuth() {
		return auth;
	}

	public void setAuth(Auth auth) {
		this.auth = auth;
	}

	public String getNomSchemaDestination() {
		return nomSchemaDestination;
	}

	public void setNomSchemaDestination(String nomSchemaDestination) {
		this.nomSchemaDestination = nomSchemaDestination;
	}
	
	
}
