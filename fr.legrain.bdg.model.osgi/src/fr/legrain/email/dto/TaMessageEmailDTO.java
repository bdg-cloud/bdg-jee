package fr.legrain.email.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.ColumnResult;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.email.model.TaEtiquetteEmail;
import fr.legrain.email.model.TaPieceJointeEmail;
import fr.legrain.tiers.model.TaTiers;

public class TaMessageEmailDTO extends ModelObject implements java.io.Serializable {
	
	public static final String C_DEBUT_NOM_CLASSE_CSS_ETIQUETTE_EMAIL = "etiquette-email-";

	public TaMessageEmailDTO(Integer id, Integer idUtilisateur, String subject, String adresseExpediteur,
			String bodyPlain, String bodyHtml, Date dateCreation, Date dateEnvoi, boolean suivi, boolean spam,
			boolean lu, boolean accuseReceptionLu, boolean recu, String smtp, String nomExpediteur, String messageID,
			String infosService, List<TaContactMessageEmailDTO> destinataires, List<TaContactMessageEmailDTO> cc,
			List<TaContactMessageEmailDTO> bcc, List<TaEtiquetteEmailDTO> etiquettes,
			List<TaPieceJointeEmailDTO> piecesJointes) {
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
		this.smtp = smtp;
		this.nomExpediteur = nomExpediteur;
		this.messageID = messageID;
		this.infosService = infosService;
		this.destinataires = destinataires;
		this.cc = cc;
		this.bcc = bcc;
		this.etiquettes = etiquettes;
		this.piecesJointes = piecesJointes;
	}
	
//	@ColumnResult(name="id_email",type = int.class), 
//	@ColumnResult(name="subject"),
//	@ColumnResult(name="adresse_expediteur"),
//	@ColumnResult(name="body_plain"),
//	@ColumnResult(name="body_html"),
//	@ColumnResult(name="date_creation"),
//	@ColumnResult(name="date_envoi"),
//	@ColumnResult(name="suivi"),
//	@ColumnResult(name="spam"),
//	@ColumnResult(name="lu"),
//	@ColumnResult(name="accuse_reception_lu"),
//	@ColumnResult(name="smtp"),
//	@ColumnResult(name="liste_adresses"),
//	@ColumnResult(name="nb_pj"),
//	@ColumnResult(name="liste_etiquette")
	
	public TaMessageEmailDTO(Integer id, /*Integer idUtilisateur,*/ String subject, String adresseExpediteur,
			String bodyPlain, String bodyHtml, Date dateCreation, Date dateEnvoi, boolean suivi, boolean spam,
			boolean lu, boolean accuseReceptionLu, /*boolean recu,*/ String smtp, /*, String nomExpediteur, String messageID,
			String infosService, */
//			List<TaContactMessageEmailDTO> destinataires, 
//			List<TaContactMessageEmailDTO> cc,
//			List<TaContactMessageEmailDTO> bcc, 
//			List<TaEtiquetteEmailDTO> etiquettes,
//			List<TaPieceJointeEmailDTO> piecesJointes
			String listeAdresseEmail, Integer nbPj, String listeEtiquette
			) {
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
		this.smtp = smtp;
		this.nomExpediteur = nomExpediteur;
		this.messageID = messageID;
		this.infosService = infosService;
		this.destinataires = destinataires;
		this.cc = cc;
		this.bcc = bcc;
		this.etiquettes = etiquettes;
		this.piecesJointes = piecesJointes;
		
		this.listeAdresseEmail = listeAdresseEmail;
		this.nbPiecesJointes = nbPj;
		this.listeEtiquette = listeEtiquette;
	}
	
	private static final long serialVersionUID = 6276712985361296130L;
	
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
	private String smtp;
//	private TaLogEmail taLogEmailPrecedent;
//	private TaLogEmail taLogEmailSuivant;
	
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
	
	private List<TaContactMessageEmailDTO> destinataires;
	private List<TaContactMessageEmailDTO> cc;
	private List<TaContactMessageEmailDTO> bcc;
	
	private List<TaEtiquetteEmailDTO> etiquettes;
	private List<TaPieceJointeEmailDTO> piecesJointes;
	
	private String infosTechniques;
	
	private String listeAdresseEmail;
	private Integer nbPiecesJointes;
	private String listeEtiquette;
	
	private Integer versionObj;
	

	public TaMessageEmailDTO() {
	}

	public void setSWTAdresse(TaMessageEmailDTO taLogEmailDTO) {
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
		this.smtp = taLogEmailDTO.smtp;
	}

	
	public static TaMessageEmailDTO copy(TaMessageEmailDTO taLogEmailDTO){
		TaMessageEmailDTO taLogEmailDTOLoc = new TaMessageEmailDTO();
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
		taLogEmailDTOLoc.setSmtp(taLogEmailDTO.getSmtp()); 
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

	public String getSmtp() {
		return smtp;
	}

	public void setSmtp(String smtp) {
		firePropertyChange("smtp", this.smtp, this.smtp = smtp);
	}

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public List<TaContactMessageEmailDTO> getDestinataires() {
		return destinataires;
	}

	public void setDestinataires(List<TaContactMessageEmailDTO> destinataires) {
		this.destinataires = destinataires;
	}

	public List<TaContactMessageEmailDTO> getCc() {
		return cc;
	}

	public void setCc(List<TaContactMessageEmailDTO> cc) {
		this.cc = cc;
	}

	public List<TaContactMessageEmailDTO> getBcc() {
		return bcc;
	}

	public void setBcc(List<TaContactMessageEmailDTO> bcc) {
		this.bcc = bcc;
	}

	public List<TaEtiquetteEmailDTO> getEtiquettes() {
		return etiquettes;
	}

	public void setEtiquettes(List<TaEtiquetteEmailDTO> etiquettes) {
		this.etiquettes = etiquettes;
	}

	public List<TaPieceJointeEmailDTO> getPiecesJointes() {
		return piecesJointes;
	}

	public void setPiecesJointes(List<TaPieceJointeEmailDTO> piecesJointes) {
		this.piecesJointes = piecesJointes;
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

	public String getListeAdresseEmail() {
		return listeAdresseEmail;
	}

	public void setListeAdresseEmail(String listeAdresseEmail) {
		this.listeAdresseEmail = listeAdresseEmail;
	}

	public Integer getNbPiecesJointes() {
		return nbPiecesJointes;
	}

	public void setNbPiecesJointes(Integer nbPiecesJointes) {
		this.nbPiecesJointes = nbPiecesJointes;
	}

	public String getListeEtiquette() {
		return listeEtiquette;
	}

	public void setListeEtiquette(String listeEtiquette) {
		this.listeEtiquette = listeEtiquette;
	}

}
