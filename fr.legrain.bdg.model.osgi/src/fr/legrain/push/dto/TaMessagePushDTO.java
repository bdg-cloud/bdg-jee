package fr.legrain.push.dto;

import java.util.Date;
import java.util.List;

import fr.legrain.bdg.model.ModelObject;

public class TaMessagePushDTO extends ModelObject implements java.io.Serializable {
	
	private static final long serialVersionUID = 6305427619076776968L;

	public TaMessagePushDTO(Integer id, Integer idUtilisateur, String sujet, String resume,
			String contenu, String url, Date dateCreation, Date dateEnvoi, String urlImage, String style,
			boolean lu, boolean accuseReceptionLu, boolean recu, boolean stop, String nomExpediteur, String messageID,
			String infosService, List<TaContactMessagePushDTO> destinataires) {
		super();
		this.id = id;
		this.idUtilisateur = idUtilisateur;
		this.sujet = sujet;
		this.resume = resume;
		this.contenu = contenu;
		this.url = url;
		this.dateCreation = dateCreation;
		this.dateEnvoi = dateEnvoi;
		this.urlImage = urlImage;
		this.style = style;
		this.lu = lu;
		this.recu = recu;
		this.nomExpediteur = nomExpediteur;
		this.messageID = messageID;
		this.infosService = infosService;
		this.destinataires = destinataires;
	}
	
	private Integer id;
	
	//private TaUtilisateur taUtilisateur;
	private Integer idUtilisateur;
	private String sujet;
	private String resume;
	private String contenu;
	private String url;
	private String urlImage;
	private String style;
	private Date dateCreation;
	private Date dateEnvoi;
	private boolean lu;
	private boolean recu;
	
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
	
	private List<TaContactMessagePushDTO> destinataires;

	
	private String infosTechniques;
	
	private Integer versionObj;
	

	public TaMessagePushDTO() {
	}

	public void setSWTAdresse(TaMessagePushDTO taLogEmailDTO) {
		this.id = taLogEmailDTO.id;
		this.idUtilisateur = taLogEmailDTO.idUtilisateur;
		this.sujet = taLogEmailDTO.sujet;
		this.resume = taLogEmailDTO.resume;
		this.contenu = taLogEmailDTO.contenu;
		this.url = taLogEmailDTO.url;
		this.dateCreation = taLogEmailDTO.dateCreation;
		this.dateEnvoi = taLogEmailDTO.dateEnvoi;
		this.urlImage = taLogEmailDTO.urlImage;
		this.style = taLogEmailDTO.style;
		this.lu = taLogEmailDTO.lu;
		this.recu = taLogEmailDTO.recu;
	}

	
	public static TaMessagePushDTO copy(TaMessagePushDTO taLogEmailDTO){
		TaMessagePushDTO taLogEmailDTOLoc = new TaMessagePushDTO();
		taLogEmailDTOLoc.setId(taLogEmailDTO.getId());               
		taLogEmailDTOLoc.setIdUtilisateur(taLogEmailDTO.getIdUtilisateur());               
		taLogEmailDTOLoc.setSujet(taLogEmailDTO.getSujet());             
		taLogEmailDTOLoc.setResume(taLogEmailDTO.getResume());              
		taLogEmailDTOLoc.setContenu(taLogEmailDTO.getContenu());             
		taLogEmailDTOLoc.setUrl(taLogEmailDTO.getUrl());                
		taLogEmailDTOLoc.setDateCreation(taLogEmailDTO.getDateCreation());        
		taLogEmailDTOLoc.setDateEnvoi(taLogEmailDTO.getDateEnvoi());             
		taLogEmailDTOLoc.setUrlImage(taLogEmailDTO.getUrlImage());               
		taLogEmailDTOLoc.setStyle(taLogEmailDTO.getStyle()); 
		taLogEmailDTOLoc.setLu(taLogEmailDTO.isLu()); 
		taLogEmailDTOLoc.setLu(taLogEmailDTO.isLu()); 
		taLogEmailDTOLoc.setRecu(taLogEmailDTO.isRecu()); 
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


	public boolean isLu() {
		return lu;
	}

	public void setLu(boolean lu) {
		firePropertyChange("lu", this.lu, this.lu = lu);
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

	public void setDestinataires(List<TaContactMessagePushDTO> destinataires) {
		this.destinataires = destinataires;
	}

	public List<TaContactMessagePushDTO> getDestinataires() {
		return destinataires;
	}

	public String getSujet() {
		return sujet;
	}

	public void setSujet(String sujet) {
		this.sujet = sujet;
	}

	public String getResume() {
		return resume;
	}

	public void setResume(String resume) {
		this.resume = resume;
	}

	public String getContenu() {
		return contenu;
	}

	public void setContenu(String contenu) {
		this.contenu = contenu;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrlImage() {
		return urlImage;
	}

	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

}
