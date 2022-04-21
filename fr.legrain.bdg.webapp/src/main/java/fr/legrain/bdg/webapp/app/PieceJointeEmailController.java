package fr.legrain.bdg.webapp.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.hibernate.multitenant.SchemaResolver;

@Named
@ViewScoped  
public class PieceJointeEmailController extends AbstractController implements Serializable {  
	
	private StreamedContent logo;
	
	private BdgProperties bdgProperties = new BdgProperties();
	
	private List<File> listeFichier;

	
	public void actFermerDialog(ActionEvent actionEvent) {
		//PrimeFaces.current().dialog().closeDynamic(null);
		
		//PrimeFaces.current().dialog().closeDynamic(listeControle);
		PrimeFaces.current().dialog().closeDynamic(listeFichier);
	}
	
	public void handleFileUpload(FileUploadEvent event) {
		FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, message);

		try {
			
			SchemaResolver sr = new SchemaResolver();
			//String localPath = bdgProperties.osTempDirDossier(sr.resolveCurrentTenantIdentifier())+"/"+bdgProperties.tmpFileName(event.getFile().getFileName());
			String localPath = bdgProperties.osTempDir()+"/"+bdgProperties.tmpFileName(event.getFile().getFileName());
			File f = new File(localPath);
			//IOUtils.copy(event.getFile().getInputstream(), new FileOutputStream(f));
			
		    byte[] buffer = new byte[event.getFile().getInputStream().available()];
		    event.getFile().getInputStream().read(buffer);
		 
		    OutputStream outStream = new FileOutputStream(f);
		    outStream.write(buffer);
				    
			if(listeFichier==null) {
				listeFichier = new ArrayList<>();
			}
			listeFichier.add(f);
			//nouveauBareme.setCheminDoc(event.getFile().getFileName());
//			taServiceWebExterne.setLogo(IOUtils.toByteArray(event.getFile().getInputstream()));

//			actEnregistrer(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<File> getListeFichier() {
		return listeFichier;
	}

	public void setListeFichier(List<File> listeFichier) {
		this.listeFichier = listeFichier;
	}
}  
  
