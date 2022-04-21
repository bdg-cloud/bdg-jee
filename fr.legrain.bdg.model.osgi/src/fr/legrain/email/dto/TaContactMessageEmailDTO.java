package fr.legrain.email.dto;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.tiers.model.TaTiers;

public class TaContactMessageEmailDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 1730797494999173806L;
	
	private Integer id;
	
	private String adresseEmail;
	private int idTiers;
	private String codeTiers;
	private String nomTiers;
	private String prenomTiers;
	
	private Integer versionObj;

	public TaContactMessageEmailDTO() {
	}
	
	public TaContactMessageEmailDTO(Integer id, String adresseEmail, int idTiers, String codeTiers, String nomTiers,
			String prenomTiers) {
		super();
		this.id = id;
		this.adresseEmail = adresseEmail;
		this.idTiers = idTiers;
		this.codeTiers = codeTiers;
		this.nomTiers = nomTiers;
		this.prenomTiers = prenomTiers;
	}

	public void setTaAgendaDTO(TaContactMessageEmailDTO taAgendaDTO) {
		this.id = taAgendaDTO.id;
		this.adresseEmail = taAgendaDTO.adresseEmail;
		this.idTiers = taAgendaDTO.idTiers;
		this.nomTiers = taAgendaDTO.nomTiers;
		this.codeTiers = taAgendaDTO.codeTiers;
		this.prenomTiers = taAgendaDTO.prenomTiers;

	}

	public static TaContactMessageEmailDTO copy(TaContactMessageEmailDTO taAgendaDTO){
		TaContactMessageEmailDTO taAgendaDTOLoc = new TaContactMessageEmailDTO();
		taAgendaDTOLoc.setId(taAgendaDTO.getId());              
		taAgendaDTOLoc.setIdTiers(taAgendaDTO.getIdTiers());                
		taAgendaDTOLoc.setAdresseEmail(taAgendaDTO.getAdresseEmail());           
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

	public String getAdresseEmail() {
		return adresseEmail;
	}

	public void setAdresseEmail(String adresseEmail) {
		this.adresseEmail = adresseEmail;
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
