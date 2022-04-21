package fr.legrain.caisse.dto;

import java.math.BigDecimal;
import java.util.Date;

import fr.legrain.bdg.model.ModelObject;


public class TaFondDeCaisseDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 7897291462025293861L;
	
	private Integer id;
	
	private String numeroCaisse;
	private Integer idUtilisateur;
	private String username;
	private Date date;
	private int idTFondDeCaisse;
	private String codeTFondDeCaisse;
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

	public TaFondDeCaisseDTO() {
	}

	public TaFondDeCaisseDTO(Integer id, String numeroCaisse, Integer idUtilisateur, String username, Date date,
			int idTFondDeCaisse, String codeTFondDeCaisse, String libelle, BigDecimal montant, int idSessionCaisse,
			String codeSessionCaisse, Integer versionObj) {
		super();
		this.id = id;
		this.numeroCaisse = numeroCaisse;
		this.idUtilisateur = idUtilisateur;
		this.username = username;
		this.date = date;
		this.idTFondDeCaisse = idTFondDeCaisse;
		this.codeTFondDeCaisse = codeTFondDeCaisse;
		this.libelle = libelle;
		this.montant = montant;
		this.idSessionCaisse = idSessionCaisse;
		this.codeSessionCaisse = codeSessionCaisse;
		this.versionObj = versionObj;
	}
	
	public void setSWTTaTFondDeCaisse(TaFondDeCaisseDTO taTFondDeCaisseDTO) {
		this.id = taTFondDeCaisseDTO.id;
		this.numeroCaisse = taTFondDeCaisseDTO.numeroCaisse;
		this.idUtilisateur = taTFondDeCaisseDTO.idUtilisateur;
		this.username = taTFondDeCaisseDTO.username;
		this.date = taTFondDeCaisseDTO.date;
		this.idTFondDeCaisse = taTFondDeCaisseDTO.idTFondDeCaisse;
		this.codeTFondDeCaisse = taTFondDeCaisseDTO.codeTFondDeCaisse;
		this.libelle = taTFondDeCaisseDTO.libelle;
		this.montant = taTFondDeCaisseDTO.montant;
		this.idSessionCaisse = taTFondDeCaisseDTO.idSessionCaisse;
		this.codeSessionCaisse = taTFondDeCaisseDTO.codeSessionCaisse;
		this.versionObj = taTFondDeCaisseDTO.versionObj;

	}
	
	public static TaFondDeCaisseDTO copy(TaFondDeCaisseDTO taTFondDeCaisseDTO){
		TaFondDeCaisseDTO taTFondDeCaisseDTOLoc = new TaFondDeCaisseDTO();
		taTFondDeCaisseDTOLoc.setId(taTFondDeCaisseDTO.id);
		taTFondDeCaisseDTOLoc.setNumeroCaisse(taTFondDeCaisseDTO.numeroCaisse);
		taTFondDeCaisseDTOLoc.setIdUtilisateur(taTFondDeCaisseDTO.idUtilisateur);
		taTFondDeCaisseDTOLoc.setUsername(taTFondDeCaisseDTO.username);
		taTFondDeCaisseDTOLoc.setDate(taTFondDeCaisseDTO.date);
		taTFondDeCaisseDTOLoc.setIdTFondDeCaisse(taTFondDeCaisseDTO.idTFondDeCaisse);
		taTFondDeCaisseDTOLoc.setCodeTFondDeCaisse(taTFondDeCaisseDTO.codeTFondDeCaisse);
		taTFondDeCaisseDTOLoc.setCodeTFondDeCaisse(taTFondDeCaisseDTO.libelle);
		taTFondDeCaisseDTOLoc.setMontant(taTFondDeCaisseDTO.montant);
		taTFondDeCaisseDTOLoc.setIdSessionCaisse(taTFondDeCaisseDTO.idSessionCaisse);
		taTFondDeCaisseDTOLoc.setCodeSessionCaisse(taTFondDeCaisseDTO.codeSessionCaisse);
		return taTFondDeCaisseDTOLoc;
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
		TaFondDeCaisseDTO other = (TaFondDeCaisseDTO) obj;
		if (codeSessionCaisse == null) {
			if (other.codeSessionCaisse != null)
				return false;
		} else if (!codeSessionCaisse.equals(other.codeSessionCaisse))
			return false;
		if (codeTFondDeCaisse == null) {
			if (other.codeTFondDeCaisse != null)
				return false;
		} else if (!codeTFondDeCaisse.equals(other.codeTFondDeCaisse))
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
		if (idTFondDeCaisse != other.idTFondDeCaisse)
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
		result = prime * result + ((codeTFondDeCaisse == null) ? 0 : codeTFondDeCaisse.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + idSessionCaisse;
		result = prime * result + idTFondDeCaisse;
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

	public int getIdTFondDeCaisse() {
		return idTFondDeCaisse;
	}

	public void setIdTFondDeCaisse(int idTFondDeCaisse) {
		firePropertyChange("idTFondDeCaisse", this.idTFondDeCaisse, this.idTFondDeCaisse = idTFondDeCaisse);
	}

	public String getCodeTFondDeCaisse() {
		return codeTFondDeCaisse;
	}

	public void setCodeTFondDeCaisse(String codeTFondDeCaisse) {
		firePropertyChange("codeTFondDeCaisse", this.codeTFondDeCaisse, this.codeTFondDeCaisse = codeTFondDeCaisse);
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
