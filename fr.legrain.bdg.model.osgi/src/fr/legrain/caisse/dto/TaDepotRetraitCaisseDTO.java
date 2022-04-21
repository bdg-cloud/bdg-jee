package fr.legrain.caisse.dto;

import java.math.BigDecimal;
import java.util.Date;

import fr.legrain.bdg.model.ModelObject;


public class TaDepotRetraitCaisseDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 7897291462025293861L;
	
	private Integer id;
	
	private String numeroCaisse;
	private Integer idUtilisateur;
	private String username;
	private Date date;
	private int idTDepotRetraitCaisse;
	private String codeTDepotRetraitCaisse;
	private String libelle;
	private BigDecimal montant;

	private int idSessionCaisse;
	private String codeSessionCaisse;
	
	private Integer versionObj;

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public TaDepotRetraitCaisseDTO() {
	}

	public TaDepotRetraitCaisseDTO(Integer id, String numeroCaisse, Integer idUtilisateur, String username, Date date,
			int idTDepotRetraitCaisse, String codeTDepotRetraitCaisse, String libelle, BigDecimal montant,
			int idSessionCaisse, String codeSessionCaisse, Integer versionObj) {
		super();
		this.id = id;
		this.numeroCaisse = numeroCaisse;
		this.idUtilisateur = idUtilisateur;
		this.username = username;
		this.date = date;
		this.idTDepotRetraitCaisse = idTDepotRetraitCaisse;
		this.codeTDepotRetraitCaisse = codeTDepotRetraitCaisse;
		this.libelle = libelle;
		this.montant = montant;
		this.idSessionCaisse = idSessionCaisse;
		this.codeSessionCaisse = codeSessionCaisse;
		this.versionObj = versionObj;
	}

	
	public void setSWTTaTFondDeCaisse(TaDepotRetraitCaisseDTO taTFondDeCaisseDTO) {
		this.id = taTFondDeCaisseDTO.id;
		this.codeTDepotRetraitCaisse = taTFondDeCaisseDTO.codeTDepotRetraitCaisse;
		this.numeroCaisse = taTFondDeCaisseDTO.numeroCaisse;
		this.idUtilisateur = taTFondDeCaisseDTO.idUtilisateur;
		this.username = taTFondDeCaisseDTO.username;
		this.date = taTFondDeCaisseDTO.date;
		this.idTDepotRetraitCaisse = taTFondDeCaisseDTO.idTDepotRetraitCaisse;
		this.codeTDepotRetraitCaisse = taTFondDeCaisseDTO.codeTDepotRetraitCaisse;
		this.libelle = taTFondDeCaisseDTO.libelle;
		this.montant = taTFondDeCaisseDTO.montant;
		this.idSessionCaisse = taTFondDeCaisseDTO.idSessionCaisse;
		this.codeSessionCaisse = taTFondDeCaisseDTO.codeSessionCaisse;
		this.versionObj = taTFondDeCaisseDTO.versionObj;

	}
	
	public static TaDepotRetraitCaisseDTO copy(TaDepotRetraitCaisseDTO taDepotRetraitCaisseDTO){
		TaDepotRetraitCaisseDTO taDepotRetraitCaisseDTOLoc = new TaDepotRetraitCaisseDTO();
		taDepotRetraitCaisseDTOLoc.setId(taDepotRetraitCaisseDTO.id);
		taDepotRetraitCaisseDTOLoc.setNumeroCaisse(taDepotRetraitCaisseDTO.numeroCaisse);
		taDepotRetraitCaisseDTOLoc.setIdUtilisateur(taDepotRetraitCaisseDTO.idUtilisateur);
		taDepotRetraitCaisseDTOLoc.setUsername(taDepotRetraitCaisseDTO.username);
		taDepotRetraitCaisseDTOLoc.setDate(taDepotRetraitCaisseDTO.date);
		taDepotRetraitCaisseDTOLoc.setIdTDepotRetraitCaisse(taDepotRetraitCaisseDTO.idTDepotRetraitCaisse);
		taDepotRetraitCaisseDTOLoc.setCodeTDepotRetraitCaisse(taDepotRetraitCaisseDTO.codeTDepotRetraitCaisse);
		taDepotRetraitCaisseDTOLoc.setLibelle(taDepotRetraitCaisseDTO.libelle);
		taDepotRetraitCaisseDTOLoc.setMontant(taDepotRetraitCaisseDTO.montant);
		taDepotRetraitCaisseDTOLoc.setIdSessionCaisse(taDepotRetraitCaisseDTO.idSessionCaisse);
		taDepotRetraitCaisseDTOLoc.setCodeSessionCaisse(taDepotRetraitCaisseDTO.codeSessionCaisse);
		return taDepotRetraitCaisseDTOLoc;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaDepotRetraitCaisseDTO other = (TaDepotRetraitCaisseDTO) obj;
		if (codeSessionCaisse == null) {
			if (other.codeSessionCaisse != null)
				return false;
		} else if (!codeSessionCaisse.equals(other.codeSessionCaisse))
			return false;
		if (codeTDepotRetraitCaisse == null) {
			if (other.codeTDepotRetraitCaisse != null)
				return false;
		} else if (!codeTDepotRetraitCaisse.equals(other.codeTDepotRetraitCaisse))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (idSessionCaisse != other.idSessionCaisse)
			return false;
		if (idTDepotRetraitCaisse != other.idTDepotRetraitCaisse)
			return false;
		if (idUtilisateur == null) {
			if (other.idUtilisateur != null)
				return false;
		} else if (!idUtilisateur.equals(other.idUtilisateur))
			return false;
		if (libelle == null) {
			if (other.libelle != null)
				return false;
		} else if (!libelle.equals(other.libelle))
			return false;
		if (montant == null) {
			if (other.montant != null)
				return false;
		} else if (!montant.equals(other.montant))
			return false;
		if (numeroCaisse == null) {
			if (other.numeroCaisse != null)
				return false;
		} else if (!numeroCaisse.equals(other.numeroCaisse))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		if (versionObj == null) {
			if (other.versionObj != null)
				return false;
		} else if (!versionObj.equals(other.versionObj))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codeSessionCaisse == null) ? 0 : codeSessionCaisse.hashCode());
		result = prime * result + ((codeTDepotRetraitCaisse == null) ? 0 : codeTDepotRetraitCaisse.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + idSessionCaisse;
		result = prime * result + idTDepotRetraitCaisse;
		result = prime * result + ((idUtilisateur == null) ? 0 : idUtilisateur.hashCode());
		result = prime * result + ((libelle == null) ? 0 : libelle.hashCode());
		result = prime * result + ((montant == null) ? 0 : montant.hashCode());
		result = prime * result + ((numeroCaisse == null) ? 0 : numeroCaisse.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		result = prime * result + ((versionObj == null) ? 0 : versionObj.hashCode());
		return result;
	}

	public String getNumeroCaisse() {
		return numeroCaisse;
	}

	public void setNumeroCaisse(String numeroCaisse) {
		firePropertyChange("numeroCaisse", this.numeroCaisse, this.numeroCaisse = numeroCaisse);
	}

	public Integer getIdUtilisateur() {
		return idUtilisateur;
	}

	public void setIdUtilisateur(Integer idUtilisateur) {
		firePropertyChange("idUtilisateur", this.idUtilisateur, this.idUtilisateur = idUtilisateur);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		firePropertyChange("username", this.username, this.username = username);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		firePropertyChange("date", this.date, this.date = date);
	}

	public int getIdTDepotRetraitCaisse() {
		return idTDepotRetraitCaisse;
	}

	public void setIdTDepotRetraitCaisse(int idTDepotRetraitCaisse) {
		firePropertyChange("idTDepotRetraitCaisse", this.idTDepotRetraitCaisse, this.idTDepotRetraitCaisse = idTDepotRetraitCaisse);
	}

	public String getCodeTDepotRetraitCaisse() {
		return codeTDepotRetraitCaisse;
	}

	public void setCodeTDepotRetraitCaisse(String codeTDepotRetraitCaisse) {
		firePropertyChange("codeTDepotRetraitCaisse", this.codeTDepotRetraitCaisse, this.codeTDepotRetraitCaisse = codeTDepotRetraitCaisse);
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		firePropertyChange("libelle", this.libelle, this.libelle = libelle);
	}

	public BigDecimal getMontant() {
		return montant;
	}

	public void setMontant(BigDecimal montant) {
		firePropertyChange("montant", this.montant, this.montant = montant);
	}

	public int getIdSessionCaisse() {
		return idSessionCaisse;
	}

	public void setIdSessionCaisse(int idSessionCaisse) {
		firePropertyChange("idSessionCaisse", this.idSessionCaisse, this.idSessionCaisse = idSessionCaisse);
	}

	public String getCodeSessionCaisse() {
		return codeSessionCaisse;
	}

	public void setCodeSessionCaisse(String codeSessionCaisse) {
		firePropertyChange("codeSessionCaisse", this.codeSessionCaisse, this.codeSessionCaisse = codeSessionCaisse);
	}
	
}
