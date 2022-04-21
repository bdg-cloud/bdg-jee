package fr.legrain.sms.dto;

import java.util.Date;
import java.util.List;

import fr.legrain.bdg.model.ModelObject;

public class TaMessageSMSDTO extends ModelObject implements java.io.Serializable {
	
	private static final long serialVersionUID = 6305427619076776968L;

	public TaMessageSMSDTO(Integer id, Integer idUtilisateur, String subject, String adresseExpediteur,
			String bodyPlain, String bodyHtml, Date dateCreation, Date dateEnvoi, boolean suivi, boolean spam,
			boolean lu, boolean accuseReceptionLu, boolean recu, boolean stop, String nomExpediteur, String messageID,
			String infosService, List<TaContactMessageSMSDTO> destinataires) {
		super();
		this.id = id;
		this.idUtilisateur = idUtilisateur;
		this.subject = subject;
		this.adresseExpediteur = adresseExpediteur;
		this.bodyPlain = bodyPlain;
		this.bodyHtml = bodyHtml;
		this.dateCreation = dateCreation;
		this.dateEnvoi = dateEnvoi;
		this.suivi = suivi;
		this.spam = spam;
		this.lu = lu;
		this.accuseReceptionLu = accuseReceptionLu;
		this.recu = recu;
		this.stop = stop;
		this.nomExpediteur = nomExpediteur;
		this.messageID = messageID;
		this.infosService = infosService;
		this.destinataires = destinataires;
	}
	
	private Integer id;
	
	//private TaUtilisateur taUtilisateur;
	private Integer idUtilisateur;
	private String subject;
	private String adresseExpediteur;
	private String bodyPlain;
	private String bodyHtml;
	private Date dateCreation;
	private Date dateEnvoi;
	private boolean suivi;
	private boolean spam;
	private boolean lu;
	private boolean accuseReceptionLu;
	private boolean recu;
	private boolean stop;
	
	private String styleClass;
	
	private String nomExpediteur;
	private String messageID;
	private String infosService;
	
	private String adresseReplyTo;
	private String typeServiceExterne;
	private String codeCompteServiceWebExterne;
	private Boolean expedie = false;
	
	private String etatMessageServiceExterne;
	private String statusServiceExterne;
	
	private List<TaContactMessageSMSDTO> destinataires;

	
	private String infosTechniques;
	
	private Integer versionObj;
	

	public TaMessageSMSDTO() {
	}

	public void setSWTAdresse(TaMessageSMSDTO taLogEmailDTO) {
		this.id = taLogEmailDTO.id;
		this.idUtilisateur = taLogEmailDTO.idUtilisateur;
		this.subject = taLogEmailDTO.subject;
		this.adresseExpediteur = taLogEmailDTO.adresseExpediteur;
		this.bodyPlain = taLogEmailDTO.bodyPlain;
		this.bodyHtml = taLogEmailDTO.bodyHtml;
		this.dateCreation = taLogEmailDTO.dateCreation;
		this.dateEnvoi = taLogEmailDTO.dateEnvoi;
		this.suivi = taLogEmailDTO.suivi;
		this.spam = taLogEmailDTO.spam;
		this.lu = taLogEmailDTO.lu;
		this.accuseReceptionLu = taLogEmailDTO.accuseReceptionLu;
		this.recu = taLogEmailDTO.recu;
		this.stop = taLogEmailDTO.stop;
	}

	
	public static TaMessageSMSDTO copy(TaMessageSMSDTO taLogEmailDTO){
		TaMessageSMSDTO taLogEmailDTOLoc = new TaMessageSMSDTO();
		taLogEmailDTOLoc.setId(taLogEmailDTO.getId());               
		taLogEmailDTOLoc.setIdUtilisateur(taLogEmailDTO.getIdUtilisateur());               
		taLogEmailDTOLoc.setSubject(taLogEmailDTO.getSubject());             
		taLogEmailDTOLoc.setAdresseExpediteur(taLogEmailDTO.getAdresseExpediteur());              
		taLogEmailDTOLoc.setBodyPlain(taLogEmailDTO.getBodyPlain());             
		taLogEmailDTOLoc.setBodyHtml(taLogEmailDTO.getBodyHtml());                
		taLogEmailDTOLoc.setDateCreation(taLogEmailDTO.getDateCreation());        
		taLogEmailDTOLoc.setDateEnvoi(taLogEmailDTO.getDateEnvoi());             
		taLogEmailDTOLoc.setSuivi(taLogEmailDTO.isSuivi());               
		taLogEmailDTOLoc.setSpam(taLogEmailDTO.isSpam()); 
		taLogEmailDTOLoc.setLu(taLogEmailDTO.isLu()); 
		taLogEmailDTOLoc.setAccuseReceptionLu(taLogEmailDTO.isAccuseReceptionLu()); 
		taLogEmailDTOLoc.setLu(taLogEmailDTO.isLu()); 
		taLogEmailDTOLoc.setRecu(taLogEmailDTO.isRecu()); 
		//taLogEmailDTOLoc.setStop(taLogEmailDTO.getStmtp()); 
		return taLogEmailDTOLoc;
	}
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}

	public Integer getIdUtilisateur() {
		return idUtilisateur;
	}

	public void setIdUtilisateur(Integer idUtilisateur) {
		firePropertyChange("idUtilisateur", this.idUtilisateur, this.idUtilisateur = idUtilisateur);
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		firePropertyChange("subject", this.subject, this.subject = subject);
	}

	public String getAdresseExpediteur() {
		return adresseExpediteur;
	}

	public void setAdresseExpediteur(String adresseExpediteur) {
		firePropertyChange("adresseExpediteur", this.adresseExpediteur, this.adresseExpediteur = adresseExpediteur);
	}

	public String getBodyPlain() {
		return bodyPlain;
	}

	public void setBodyPlain(String bodyPlain) {
		firePropertyChange("bodyPlain", this.bodyPlain, this.bodyPlain = bodyPlain);
	}

	public String getBodyHtml() {
		return bodyHtml;
	}

	public void setBodyHtml(String bodyHtml) {
		firePropertyChange("bodyHtml", this.bodyHtml, this.bodyHtml = bodyHtml);
	}

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		firePropertyChange("dateCreation", this.dateCreation, this.dateCreation = dateCreation);
	}

	public Date getDateEnvoi() {
		return dateEnvoi;
	}

	public void setDateEnvoi(Date dateEnvoi) {
		firePropertyChange("dateEnvoi", this.dateEnvoi, this.dateEnvoi = dateEnvoi);
	}

	public boolean isSuivi() {
		return suivi;
	}

	public void setSuivi(boolean suivi) {
		firePropertyChange("suivi", this.suivi, this.suivi = suivi);
	}

	public boolean isSpam() {
		return spam;
	}

	public void setSpam(boolean spam) {
		firePropertyChange("spam", this.spam, this.spam = spam);
	}

	public boolean isLu() {
		return lu;
	}

	public void setLu(boolean lu) {
		firePropertyChange("lu", this.lu, this.lu = lu);
	}

	public boolean isAccuseReceptionLu() {
		return accuseReceptionLu;
	}

	public void setAccuseReceptionLu(boolean accuseReceptionLu) {
		firePropertyChange("accuseReceptionLu", this.accuseReceptionLu, this.accuseReceptionLu = accuseReceptionLu);
	}

	public boolean isRecu() {
		return recu;
	}

	public void setRecu(boolean recu) {
		firePropertyChange("recu", this.recu, this.recu = recu);
	}

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public String getNomExpediteur() {
		return nomExpediteur;
	}

	public void setNomExpediteur(String nomExpediteur) {
		this.nomExpediteur = nomExpediteur;
	}

	public String getMessageID() {
		return messageID;
	}

	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}

	public String getInfosService() {
		return infosService;
	}

	public void setInfosService(String infosService) {
		this.infosService = infosService;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getAdresseReplyTo() {
		return adresseReplyTo;
	}

	public void setAdresseReplyTo(String adresseReplyTo) {
		this.adresseReplyTo = adresseReplyTo;
	}

	public String getTypeServiceExterne() {
		return typeServiceExterne;
	}

	public void setTypeServiceExterne(String typeServiceExterne) {
		this.typeServiceExterne = typeServiceExterne;
	}

	public String getCodeCompteServiceWebExterne() {
		return codeCompteServiceWebExterne;
	}

	public void setCodeCompteServiceWebExterne(String codeCompteServiceWebExterne) {
		this.codeCompteServiceWebExterne = codeCompteServiceWebExterne;
	}

	public Boolean getExpedie() {
		return expedie;
	}

	public void setExpedie(Boolean expedie) {
		this.expedie = expedie;
	}

	public String getEtatMessageServiceExterne() {
		return etatMessageServiceExterne;
	}

	public void setEtatMessageServiceExterne(String etatMessageServiceExterne) {
		this.etatMessageServiceExterne = etatMessageServiceExterne;
	}

	public String getStatusServiceExterne() {
		return statusServiceExterne;
	}

	public void setStatusServiceExterne(String statusServiceExterne) {
		this.statusServiceExterne = statusServiceExterne;
	}

	public String getInfosTechniques() {
		return infosTechniques;
	}

	public void setInfosTechniques(String infosTechniques) {
		this.infosTechniques = infosTechniques;
	}

	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	public void setDestinataires(List<TaContactMessageSMSDTO> destinataires) {
		this.destinataires = destinataires;
	}

	public List<TaContactMessageSMSDTO> getDestinataires() {
		return destinataires;
	}

}
