package fr.legrain.bdg.webapp.dev;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

import fr.legrain.bdg.documents.service.remote.ITaFlashServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.importation.service.remote.IImportationServiceRemote;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.document.dto.IDocumentDTO;
import fr.legrain.document.model.RetourMajDossier;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.general.service.remote.IMajDossierDiversesServiceRemote;
import fr.legrain.importation.model.Importation;

@Named
@ViewScoped 
public class MajDossierDiversesBean extends AbstractController{
	
	

	



	@Inject SessionInfo sessionInfo;
	@Inject TenantInfo tenantInfo;
	

	Importation importation;
	

	
	@EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;

	@EJB private ITaPreferencesServiceRemote PreferencesService;

	@EJB private IMajDossierDiversesServiceRemote MajDossierDiverses;

	
	
	@PostConstruct
	public void init() {
	}
	

	
	public void actFermer(ActionEvent e) {
		
	}

	public void actEtatMinimalTousDocs(ActionEvent e) throws Exception {
		RetourMajDossier retour=MajDossierDiverses.updateEtatEncoursTousDocs();
		if(retour.isRetour())PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO,"Maj Etat Encours de tous les documents Terminée", retour.getMessage()));
		else PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur pendant la Maj Etat Encours de tous les documents ", retour.getMessage()));
	}

	public void actEtatTousDocs(ActionEvent e) throws Exception {
		RetourMajDossier retour=MajDossierDiverses.updateEtatTousDocs();
		if(retour.isRetour())PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO,"Maj Etat de tous les documents Terminée", retour.getMessage()));
		else PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur pendant la Maj Etat de tous les documents ", retour.getMessage()));
	}
	
	public void actGenereLigneALigne(ActionEvent e) throws Exception {
		RetourMajDossier retour=MajDossierDiverses.majLigneALigne();
		if(retour.isRetour())PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO,"Génération des liaisons Terminée", retour.getMessage()));
		else PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur pendant Génération des liaisons ", retour.getMessage()));
	}
	public void actGenereLigneALigneProcedure(ActionEvent e) throws Exception {
		RetourMajDossier retour=MajDossierDiverses.majLigneALigneProcedureStockee();
		if(retour.isRetour())PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO,"Génération des liaisons Terminée", retour.getMessage()));
		else PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur pendant Génération des liaisons ", retour.getMessage()));
	}

	public void actModifEtatDocsLies(ActionEvent e) throws Exception {
		RetourMajDossier retour=MajDossierDiverses.majEtatDocument();
		if(retour.isRetour())PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO,"Maj Etat de tous les documents liés Terminée", retour.getMessage()));
		else PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur pendant la Maj Etat de tous les documents liés", retour.getMessage()));
	}

	public void actModifEtatFactureReglee(ActionEvent e) throws Exception {
		RetourMajDossier retour=MajDossierDiverses.majEtatFactureReglee();
		if(retour.isRetour())PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO,"Maj Etat de toutes les factures réglées terminée", retour.getMessage()));
		else PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur pendant la Maj Etat de toutes les factures réglées", retour.getMessage()));
	}

	
	public void actTestDivers(ActionEvent e) throws Exception {

	}
	
	

}
