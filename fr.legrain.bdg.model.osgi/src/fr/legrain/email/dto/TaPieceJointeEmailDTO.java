package fr.legrain.email.dto;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.email.model.TaMessageEmail;

public class TaPieceJointeEmailDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 1730797494999173806L;
	
	private Integer id;
	
	//private TaLogEmail taLogEmail;
	private Integer idLogEmail;
	private String nomFichier;
	private Integer taille;
	private String typeMime;
	private byte[] fichier;
	private byte[] fichierImageMiniature;
	
	private Integer versionObj;

	public TaPieceJointeEmailDTO() {
	}

	public void setTaAgendaDTO(TaPieceJointeEmailDTO taLogPieceJointeEmailDTO) {
		this.id = taLogPieceJointeEmailDTO.id;
		this.nomFichier = taLogPieceJointeEmailDTO.nomFichier;
		this.taille = taLogPieceJointeEmailDTO.taille;
		this.typeMime = taLogPieceJointeEmailDTO.typeMime;
		this.fichier = taLogPieceJointeEmailDTO.fichier;
		this.fichierImageMiniature = taLogPieceJointeEmailDTO.fichierImageMiniature;
		this.idLogEmail = taLogPieceJointeEmailDTO.idLogEmail;
	}

	public static TaPieceJointeEmailDTO copy(TaPieceJointeEmailDTO taLogPieceJointeEmailDTO){
		TaPieceJointeEmailDTO taLogPieceJointeEmailDTOLoc = new TaPieceJointeEmailDTO();
		taLogPieceJointeEmailDTOLoc.setId(taLogPieceJointeEmailDTO.getId());              
		taLogPieceJointeEmailDTOLoc.setNomFichier(taLogPieceJointeEmailDTO.getNomFichier());                
		taLogPieceJointeEmailDTOLoc.setTaille(taLogPieceJointeEmailDTO.getTaille());           
		taLogPieceJointeEmailDTOLoc.setTypeMime(taLogPieceJointeEmailDTO.getTypeMime());             
		taLogPieceJointeEmailDTOLoc.setFichier(taLogPieceJointeEmailDTO.getFichier());              
		taLogPieceJointeEmailDTOLoc.setFichierImageMiniature(taLogPieceJointeEmailDTO.getFichierImageMiniature());
		taLogPieceJointeEmailDTOLoc.setIdLogEmail(taLogPieceJointeEmailDTO.getIdLogEmail());      
		return taLogPieceJointeEmailDTOLoc;
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

	public Integer getIdLogEmail() {
		return idLogEmail;
	}

	public void setIdLogEmail(Integer idLogEmail) {
		firePropertyChange("idLogEmail", this.idLogEmail, this.idLogEmail = idLogEmail);
	}

	public String getNomFichier() {
		return nomFichier;
	}

	public void setNomFichier(String nomFichier) {
		firePropertyChange("nomFichier", this.nomFichier, this.nomFichier = nomFichier);
	}

	public Integer getTaille() {
		return taille;
	}

	public void setTaille(Integer taille) {
		firePropertyChange("taille", this.taille, this.taille = taille);
	}

	public String getTypeMime() {
		return typeMime;
	}

	public void setTypeMime(String typeMime) {
		firePropertyChange("typeMime", this.typeMime, this.typeMime = typeMime);
	}

	public byte[] getFichier() {
		return fichier;
	}

	public void setFichier(byte[] fichier) {
		firePropertyChange("fichier", this.fichier, this.fichier = fichier);
	}

	public byte[] getFichierImageMiniature() {
		return fichierImageMiniature;
	}

	public void setFichierImageMiniature(byte[] fichierImageMiniature) {
		firePropertyChange("fichierImageMiniature", this.fichierImageMiniature, this.fichierImageMiniature = fichierImageMiniature);
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

}
