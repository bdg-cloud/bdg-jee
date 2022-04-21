package fr.legrain.tache.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Size;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.tache.model.TaEvenement;
import fr.legrain.tache.model.TaTypeNotification;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.LgrHibernateValidated;

public class TaNotificationDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 1730797494999173806L;
	
	private Integer id;
	
	private int idTypeNotification;
	private String codeTypeNotification;
	private String libelleTypeNotification;
	private Integer idEvenement;
	private String codeEvenement;
	private String libelleEvenement;
	private Date dateNotification; // ou nbjour avant et heure
	private boolean notificationEnvoyee;
//	private List<TaTiers> listeTiers;
//	private List<TaUtilisateur> listeUtilisateur;
	
	private int nbMinutesAvantNotification;
	private int nbHeuresAvantNotification;
	private int nbJourAvantNotification;
	private int nbsemainesAvantNotification;
	private int texteNotification;
	
	private boolean auDebutdeEvenement;
	
	private Integer versionObj;

	public TaNotificationDTO() {
	}

	public void setTaNotificationDTO(TaNotificationDTO taNotificationDTO) {
		this.id = taNotificationDTO.id;
		this.codeEvenement = taNotificationDTO.codeEvenement;
		this.codeTypeNotification = taNotificationDTO.codeTypeNotification;
		this.dateNotification = taNotificationDTO.dateNotification;
		this.idEvenement = taNotificationDTO.idEvenement;
		this.idTypeNotification = taNotificationDTO.idTypeNotification;
		this.libelleEvenement = taNotificationDTO.libelleEvenement;
		this.libelleTypeNotification = taNotificationDTO.libelleTypeNotification;
		this.nbJourAvantNotification = taNotificationDTO.nbJourAvantNotification;
		this.notificationEnvoyee = taNotificationDTO.notificationEnvoyee;
	}

	public static TaNotificationDTO copy(TaNotificationDTO taNotificationDTO){
		TaNotificationDTO taNotificationDTOLoc = new TaNotificationDTO();
		taNotificationDTOLoc.setId(taNotificationDTO.getId());              
		taNotificationDTOLoc.setCodeEvenement(taNotificationDTO.getCodeEvenement());                
		taNotificationDTOLoc.setCodeTypeNotification(taNotificationDTO.getCodeTypeNotification());           
		taNotificationDTOLoc.setDateNotification(taNotificationDTO.getDateNotification());             
		taNotificationDTOLoc.setIdEvenement(taNotificationDTO.getIdEvenement());
		taNotificationDTOLoc.setIdTypeNotification(taNotificationDTO.getIdTypeNotification());        
		taNotificationDTOLoc.setLibelleEvenement(taNotificationDTO.getLibelleEvenement());        
		taNotificationDTOLoc.setLibelleTypeNotification(taNotificationDTO.getLibelleTypeNotification());        
		taNotificationDTOLoc.setNbJourAvantNotification(taNotificationDTO.getNbJourAvantNotification());        
		taNotificationDTOLoc.setNotificationEnvoyee(taNotificationDTO.isNotificationEnvoyee());        
		return taNotificationDTOLoc;
	}
	
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}
	
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public int getIdTypeNotification() {
		return idTypeNotification;
	}

	public void setIdTypeNotification(int idTypeNotification) {
		firePropertyChange("idTypeNotification", this.idTypeNotification, this.idTypeNotification = idTypeNotification);
	}

	public String getCodeTypeNotification() {
		return codeTypeNotification;
	}

	public void setCodeTypeNotification(String codeTypeNotification) {
		firePropertyChange("codeTypeNotification", this.codeTypeNotification, this.codeTypeNotification = codeTypeNotification);
	}

	public String getLibelleTypeNotification() {
		return libelleTypeNotification;
	}

	public void setLibelleTypeNotification(String libelleTypeNotification) {
		firePropertyChange("libelleTypeNotification", this.libelleTypeNotification, this.libelleTypeNotification = libelleTypeNotification);
	}

	public Integer getIdEvenement() {
		return idEvenement;
	}

	public void setIdEvenement(Integer idEvenement) {
		firePropertyChange("idEvenement", this.idEvenement, this.idEvenement = idEvenement);
	}

	public String getCodeEvenement() {
		return codeEvenement;
	}

	public void setCodeEvenement(String codeEvenement) {
		firePropertyChange("codeEvenement", this.codeEvenement, this.codeEvenement = codeEvenement);
	}

	public String getLibelleEvenement() {
		return libelleEvenement;
	}

	public void setLibelleEvenement(String libelleEvenement) {
		firePropertyChange("libelleEvenement", this.libelleEvenement, this.libelleEvenement = libelleEvenement);
	}

	public Date getDateNotification() {
		return dateNotification;
	}

	public void setDateNotification(Date dateNotification) {
		firePropertyChange("dateNotification", this.dateNotification, this.dateNotification = dateNotification);
	}

	public int getNbJourAvantNotification() {
		return nbJourAvantNotification;
	}

	public void setNbJourAvantNotification(int nbJourAvantNotification) {
		firePropertyChange("nbJourAvantNotification", this.nbJourAvantNotification, this.nbJourAvantNotification = nbJourAvantNotification);
	}

	public boolean isNotificationEnvoyee() {
		return notificationEnvoyee;
	}

	public void setNotificationEnvoyee(boolean notificationEnvoyee) {
		firePropertyChange("notificationEnvoyee", this.notificationEnvoyee, this.notificationEnvoyee = notificationEnvoyee);
	}

	public int getNbMinutesAvantNotification() {
		return nbMinutesAvantNotification;
	}

	public void setNbMinutesAvantNotification(int nbMinutesAvantNotification) {
		this.nbMinutesAvantNotification = nbMinutesAvantNotification;
	}

	public int getNbHeuresAvantNotification() {
		return nbHeuresAvantNotification;
	}

	public void setNbHeuresAvantNotification(int nbHeuresAvantNotification) {
		this.nbHeuresAvantNotification = nbHeuresAvantNotification;
	}

	public int getNbsemainesAvantNotification() {
		return nbsemainesAvantNotification;
	}

	public void setNbsemainesAvantNotification(int nbsemainesAvantNotification) {
		this.nbsemainesAvantNotification = nbsemainesAvantNotification;
	}

	public int getTexteNotification() {
		return texteNotification;
	}

	public void setTexteNotification(int texteNotification) {
		this.texteNotification = texteNotification;
	}

	public boolean isAuDebutdeEvenement() {
		return auDebutdeEvenement;
	}

	public void setAuDebutdeEvenement(boolean auDebutdeEvenement) {
		this.auDebutdeEvenement = auDebutdeEvenement;
	}

}
