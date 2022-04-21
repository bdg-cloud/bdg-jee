package fr.legrain.sms.dto;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.tiers.model.TaTiers;

public class TaContactMessageSMSDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 9107429146078637190L;

	private Integer id;
	
	private String numeroTelephone;
	private int idTiers;
	private String codeTiers;
	private String nomTiers;
	private String prenomTiers;
	
	private Integer versionObj;

	public TaContactMessageSMSDTO() {
	}
	
	public TaContactMessageSMSDTO(Integer id, String adresseEmail, int idTiers, String codeTiers, String nomTiers,
			String prenomTiers) {
		super();
		this.id = id;
		this.numeroTelephone = adresseEmail;
		this.idTiers = idTiers;
		this.codeTiers = codeTiers;
		this.nomTiers = nomTiers;
		this.prenomTiers = prenomTiers;
	}

	public void setTaAgendaDTO(TaContactMessageSMSDTO taAgendaDTO) {
		this.id = taAgendaDTO.id;
		this.numeroTelephone = taAgendaDTO.numeroTelephone;
		this.idTiers = taAgendaDTO.idTiers;
		this.nomTiers = taAgendaDTO.nomTiers;
		this.codeTiers = taAgendaDTO.codeTiers;
		this.prenomTiers = taAgendaDTO.prenomTiers;

	}

	public static TaContactMessageSMSDTO copy(TaContactMessageSMSDTO taAgendaDTO){
		TaContactMessageSMSDTO taAgendaDTOLoc = new TaContactMessageSMSDTO();
		taAgendaDTOLoc.setId(taAgendaDTO.getId());              
		taAgendaDTOLoc.setIdTiers(taAgendaDTO.getIdTiers());                
		taAgendaDTOLoc.setNumeroTelephone(taAgendaDTO.getNumeroTelephone());           
		taAgendaDTOLoc.setNomTiers(taAgendaDTO.getNomTiers());             
		taAgendaDTOLoc.setPrenomTiers(taAgendaDTO.getPrenomTiers());
		taAgendaDTOLoc.setCodeTiers(taAgendaDTO.getCodeTiers());     
		return taAgendaDTOLoc;
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

	public String getNumeroTelephone() {
		return numeroTelephone;
	}

	public void setNumeroTelephone(String adresseEmail) {
		this.numeroTelephone = adresseEmail;
	}

	public int getIdTiers() {
		return idTiers;
	}

	public void setIdTiers(int idTiers) {
		this.idTiers = idTiers;
	}

	public String getNomTiers() {
		return nomTiers;
	}

	public void setNomTiers(String nomTiers) {
		this.nomTiers = nomTiers;
	}

	public String getPrenomTiers() {
		return prenomTiers;
	}

	public void setPrenomTiers(String prenomTiers) {
		this.prenomTiers = prenomTiers;
	}

	public String getCodeTiers() {
		return codeTiers;
	}

	public void setCodeTiers(String codeTiers) {
		this.codeTiers = codeTiers;
	}

	

}
