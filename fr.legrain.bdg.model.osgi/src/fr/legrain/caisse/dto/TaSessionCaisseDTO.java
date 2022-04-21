package fr.legrain.caisse.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.caisse.model.TaFondDeCaisse;
import fr.legrain.droits.model.TaUtilisateur;


public class TaSessionCaisseDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 7897291462025293861L;
	
	private Integer id;
	
	private String codeSessionCaisse;
	private String numeroCaisse;
	
	private Integer idUtilisateur;
	private String username;
	
	private Date dateSession;
	private Date dateDebutSession;
	private Date dateFinSession;
	private String libelle;
	private Boolean automatique;
	private Boolean verrouillageTicketZ;
  
	private BigDecimal montantFondDeCaisseOuverture;
	private Integer idFondDeCaisseOuverture;

	private BigDecimal montantHtSession;
	private BigDecimal montantHtCumulMensuel;
	private BigDecimal montantHtCumulAnnuel;
	private BigDecimal montantHtCumulExercice;

	private BigDecimal montantTtcSession;
	private BigDecimal montantTtcCumulMensuel;
	private BigDecimal montantTtcCumulAnnuel;
	private BigDecimal montantTtcCumulExercice;

	private BigDecimal montantTvaSession;
	private BigDecimal montantTvaCumulMensuel;
	private BigDecimal montantTvaCumulAnnuel;
	private BigDecimal montantTvaCumulExercice;

	private BigDecimal montantRemiseSession;

	private BigDecimal montantFondDeCaisseCloture;
	private Integer idFondDeCaisseCloture;
	
	private Integer versionObj;

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public TaSessionCaisseDTO() {
	}

	public TaSessionCaisseDTO(Integer id, String codeSessionCaisse, String numeroCaisse, Integer idUtilisateur,
			String username, Date dateSession, Date dateDebutSession, Date dateFinSession, String libelle,
			Boolean automatique, Boolean verrouillageTicketZ, BigDecimal montantFondDeCaisseOuverture,
			Integer idFondDeCaisseOuverture, BigDecimal montantHtSession, BigDecimal montantHtCumulMensuel,
			BigDecimal montantHtCumulAnnuel, BigDecimal montantHtCumulExercice, BigDecimal montantTtcSession,
			BigDecimal montantTtcCumulMensuel, BigDecimal montantTtcCumulAnnuel, BigDecimal montantTtcCumulExercice,
			BigDecimal montantTvaSession, BigDecimal montantTvaCumulMensuel, BigDecimal montantTvaCumulAnnuel,
			BigDecimal montantTvaCumulExercice, BigDecimal montantRemiseSession, BigDecimal montantFondDeCaisseCloture,
			Integer idFondDeCaisseCloture, Integer versionObj) {
		super();
		this.id = id;
		this.codeSessionCaisse = codeSessionCaisse;
		this.numeroCaisse = numeroCaisse;
		this.idUtilisateur = idUtilisateur;
		this.username = username;
		this.dateSession = dateSession;
		this.dateDebutSession = dateDebutSession;
		this.dateFinSession = dateFinSession;
		this.libelle = libelle;
		this.automatique = automatique;
		this.verrouillageTicketZ = verrouillageTicketZ;
		this.montantFondDeCaisseOuverture = montantFondDeCaisseOuverture;
		this.idFondDeCaisseOuverture = idFondDeCaisseOuverture;
		this.montantHtSession = montantHtSession;
		this.montantHtCumulMensuel = montantHtCumulMensuel;
		this.montantHtCumulAnnuel = montantHtCumulAnnuel;
		this.montantHtCumulExercice = montantHtCumulExercice;
		this.montantTtcSession = montantTtcSession;
		this.montantTtcCumulMensuel = montantTtcCumulMensuel;
		this.montantTtcCumulAnnuel = montantTtcCumulAnnuel;
		this.montantTtcCumulExercice = montantTtcCumulExercice;
		this.montantTvaSession = montantTvaSession;
		this.montantTvaCumulMensuel = montantTvaCumulMensuel;
		this.montantTvaCumulAnnuel = montantTvaCumulAnnuel;
		this.montantTvaCumulExercice = montantTvaCumulExercice;
		this.montantRemiseSession = montantRemiseSession;
		this.montantFondDeCaisseCloture = montantFondDeCaisseCloture;
		this.idFondDeCaisseCloture = idFondDeCaisseCloture;
		this.versionObj = versionObj;
	}
	
	public void setSWTTaSessionCaisse(TaSessionCaisseDTO taSessionCaisseDTO) {
		this.id = taSessionCaisseDTO.id;
		this.codeSessionCaisse = taSessionCaisseDTO.codeSessionCaisse;
		this.numeroCaisse = taSessionCaisseDTO.numeroCaisse;
		
		this.idUtilisateur = taSessionCaisseDTO.idUtilisateur;
		this.username = taSessionCaisseDTO.username;
		
		this.dateSession = taSessionCaisseDTO.dateSession;
		this.dateDebutSession = taSessionCaisseDTO.dateDebutSession;
		this.dateFinSession = taSessionCaisseDTO.dateFinSession;
		this.libelle = taSessionCaisseDTO.libelle;
		this.automatique = taSessionCaisseDTO.automatique;
		this.verrouillageTicketZ = taSessionCaisseDTO.verrouillageTicketZ;
	  
		this.montantFondDeCaisseOuverture = taSessionCaisseDTO.montantFondDeCaisseOuverture;
		this.idFondDeCaisseOuverture = taSessionCaisseDTO.idFondDeCaisseOuverture;

		this.montantHtSession = taSessionCaisseDTO.montantHtSession;
		this.montantHtCumulMensuel = taSessionCaisseDTO.montantHtCumulMensuel;
		this.montantHtCumulAnnuel = taSessionCaisseDTO.montantHtCumulAnnuel;
		this.montantHtCumulExercice = taSessionCaisseDTO.montantHtCumulExercice;

		this.montantTtcSession = taSessionCaisseDTO.montantTtcSession;
		this.montantTtcCumulMensuel = taSessionCaisseDTO.montantTtcCumulMensuel;
		this.montantTtcCumulAnnuel = taSessionCaisseDTO.montantTtcCumulAnnuel;
		this.montantTtcCumulExercice = taSessionCaisseDTO.montantTtcCumulExercice;

		this.montantTvaSession = taSessionCaisseDTO.montantTvaSession;
		this.montantTvaCumulMensuel = taSessionCaisseDTO.montantTvaCumulMensuel;
		this.montantTvaCumulAnnuel = taSessionCaisseDTO.montantTvaCumulAnnuel;
		this.montantTvaCumulExercice = taSessionCaisseDTO.montantTvaCumulExercice;

		this.montantRemiseSession = taSessionCaisseDTO.montantRemiseSession;

		this.montantFondDeCaisseCloture = taSessionCaisseDTO.montantFondDeCaisseCloture;
		this.idFondDeCaisseCloture = taSessionCaisseDTO.idFondDeCaisseCloture;

	}
	
	public static TaSessionCaisseDTO copy(TaSessionCaisseDTO taSessionCaisseDTO){
		TaSessionCaisseDTO taSessionCaisseDTOLoc = new TaSessionCaisseDTO();
		taSessionCaisseDTOLoc.setId(taSessionCaisseDTO.id);
		taSessionCaisseDTOLoc.setCodeSessionCaisse(taSessionCaisseDTO.codeSessionCaisse);
		taSessionCaisseDTOLoc.setCodeSessionCaisse(taSessionCaisseDTO.numeroCaisse);
		
		taSessionCaisseDTOLoc.setIdUtilisateur(taSessionCaisseDTO.idUtilisateur);
		taSessionCaisseDTOLoc.setUsername(taSessionCaisseDTO.username);
		
		taSessionCaisseDTOLoc.setDateSession(taSessionCaisseDTO.dateSession);
		taSessionCaisseDTOLoc.setDateDebutSession(taSessionCaisseDTO.dateDebutSession);
		taSessionCaisseDTOLoc.setDateFinSession(taSessionCaisseDTO.dateFinSession);
		taSessionCaisseDTOLoc.setLibelle(taSessionCaisseDTO.libelle);
		taSessionCaisseDTOLoc.setAutomatique(taSessionCaisseDTO.automatique);
		taSessionCaisseDTOLoc.setVerrouillageTicketZ(taSessionCaisseDTO.verrouillageTicketZ);
	  
		taSessionCaisseDTOLoc.setMontantFondDeCaisseOuverture(taSessionCaisseDTO.montantFondDeCaisseOuverture);
		taSessionCaisseDTOLoc.setIdFondDeCaisseOuverture( taSessionCaisseDTO.idFondDeCaisseOuverture);

		taSessionCaisseDTOLoc.setMontantHtSession(taSessionCaisseDTO.montantHtSession);
		taSessionCaisseDTOLoc.setMontantHtCumulMensuel(taSessionCaisseDTO.montantHtCumulMensuel);
		taSessionCaisseDTOLoc.setMontantHtCumulAnnuel(taSessionCaisseDTO.montantHtCumulAnnuel);
		taSessionCaisseDTOLoc.setMontantHtCumulExercice(taSessionCaisseDTO.montantHtCumulExercice);

		taSessionCaisseDTOLoc.setMontantTtcSession(taSessionCaisseDTO.montantTtcSession);
		taSessionCaisseDTOLoc.setMontantTtcCumulMensuel(taSessionCaisseDTO.montantTtcCumulMensuel);
		taSessionCaisseDTOLoc.setMontantTtcCumulAnnuel(taSessionCaisseDTO.montantTtcCumulAnnuel);
		taSessionCaisseDTOLoc.setMontantTtcCumulExercice(taSessionCaisseDTO.montantTtcCumulExercice);

		taSessionCaisseDTOLoc.setMontantTvaSession(taSessionCaisseDTO.montantTvaSession);
		taSessionCaisseDTOLoc.setMontantTvaCumulMensuel(taSessionCaisseDTO.montantTvaCumulMensuel);
		taSessionCaisseDTOLoc.setMontantTvaCumulAnnuel(taSessionCaisseDTO.montantTvaCumulAnnuel);
		taSessionCaisseDTOLoc.setMontantTvaCumulExercice(taSessionCaisseDTO.montantTvaCumulExercice);

		taSessionCaisseDTOLoc.setMontantRemiseSession(taSessionCaisseDTO.montantRemiseSession);

		taSessionCaisseDTOLoc.setMontantFondDeCaisseCloture(taSessionCaisseDTO.montantFondDeCaisseCloture);
		taSessionCaisseDTOLoc.setIdFondDeCaisseCloture(taSessionCaisseDTO.idFondDeCaisseCloture);
		return taSessionCaisseDTOLoc;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}

	public String getCodeSessionCaisse() {
		return this.codeSessionCaisse;
	}

	public void setCodeSessionCaisse(String codeSessionCaisse) {
		firePropertyChange("codeSessionCaisse", this.codeSessionCaisse, this.codeSessionCaisse = codeSessionCaisse);
	}

	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaSessionCaisseDTO other = (TaSessionCaisseDTO) obj;
		if (automatique == null) {
			if (other.automatique != null)
				return false;
		} else if (!automatique.equals(other.automatique))
			return false;
		if (codeSessionCaisse == null) {
			if (other.codeSessionCaisse != null)
				return false;
		} else if (!codeSessionCaisse.equals(other.codeSessionCaisse))
			return false;
		if (dateDebutSession == null) {
			if (other.dateDebutSession != null)
				return false;
		} else if (!dateDebutSession.equals(other.dateDebutSession))
			return false;
		if (dateFinSession == null) {
			if (other.dateFinSession != null)
				return false;
		} else if (!dateFinSession.equals(other.dateFinSession))
			return false;
		if (dateSession == null) {
			if (other.dateSession != null)
				return false;
		} else if (!dateSession.equals(other.dateSession))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (idFondDeCaisseCloture == null) {
			if (other.idFondDeCaisseCloture != null)
				return false;
		} else if (!idFondDeCaisseCloture.equals(other.idFondDeCaisseCloture))
			return false;
		if (idFondDeCaisseOuverture == null) {
			if (other.idFondDeCaisseOuverture != null)
				return false;
		} else if (!idFondDeCaisseOuverture.equals(other.idFondDeCaisseOuverture))
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
		if (montantFondDeCaisseCloture == null) {
			if (other.montantFondDeCaisseCloture != null)
				return false;
		} else if (!montantFondDeCaisseCloture.equals(other.montantFondDeCaisseCloture))
			return false;
		if (montantFondDeCaisseOuverture == null) {
			if (other.montantFondDeCaisseOuverture != null)
				return false;
		} else if (!montantFondDeCaisseOuverture.equals(other.montantFondDeCaisseOuverture))
			return false;
		if (montantHtCumulAnnuel == null) {
			if (other.montantHtCumulAnnuel != null)
				return false;
		} else if (!montantHtCumulAnnuel.equals(other.montantHtCumulAnnuel))
			return false;
		if (montantHtCumulExercice == null) {
			if (other.montantHtCumulExercice != null)
				return false;
		} else if (!montantHtCumulExercice.equals(other.montantHtCumulExercice))
			return false;
		if (montantHtCumulMensuel == null) {
			if (other.montantHtCumulMensuel != null)
				return false;
		} else if (!montantHtCumulMensuel.equals(other.montantHtCumulMensuel))
			return false;
		if (montantHtSession == null) {
			if (other.montantHtSession != null)
				return false;
		} else if (!montantHtSession.equals(other.montantHtSession))
			return false;
		if (montantRemiseSession == null) {
			if (other.montantRemiseSession != null)
				return false;
		} else if (!montantRemiseSession.equals(other.montantRemiseSession))
			return false;
		if (montantTtcCumulAnnuel == null) {
			if (other.montantTtcCumulAnnuel != null)
				return false;
		} else if (!montantTtcCumulAnnuel.equals(other.montantTtcCumulAnnuel))
			return false;
		if (montantTtcCumulExercice == null) {
			if (other.montantTtcCumulExercice != null)
				return false;
		} else if (!montantTtcCumulExercice.equals(other.montantTtcCumulExercice))
			return false;
		if (montantTtcCumulMensuel == null) {
			if (other.montantTtcCumulMensuel != null)
				return false;
		} else if (!montantTtcCumulMensuel.equals(other.montantTtcCumulMensuel))
			return false;
		if (montantTtcSession == null) {
			if (other.montantTtcSession != null)
				return false;
		} else if (!montantTtcSession.equals(other.montantTtcSession))
			return false;
		if (montantTvaCumulAnnuel == null) {
			if (other.montantTvaCumulAnnuel != null)
				return false;
		} else if (!montantTvaCumulAnnuel.equals(other.montantTvaCumulAnnuel))
			return false;
		if (montantTvaCumulExercice == null) {
			if (other.montantTvaCumulExercice != null)
				return false;
		} else if (!montantTvaCumulExercice.equals(other.montantTvaCumulExercice))
			return false;
		if (montantTvaCumulMensuel == null) {
			if (other.montantTvaCumulMensuel != null)
				return false;
		} else if (!montantTvaCumulMensuel.equals(other.montantTvaCumulMensuel))
			return false;
		if (montantTvaSession == null) {
			if (other.montantTvaSession != null)
				return false;
		} else if (!montantTvaSession.equals(other.montantTvaSession))
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
		if (verrouillageTicketZ == null) {
			if (other.verrouillageTicketZ != null)
				return false;
		} else if (!verrouillageTicketZ.equals(other.verrouillageTicketZ))
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
		result = prime * result + ((automatique == null) ? 0 : automatique.hashCode());
		result = prime * result + ((codeSessionCaisse == null) ? 0 : codeSessionCaisse.hashCode());
		result = prime * result + ((dateDebutSession == null) ? 0 : dateDebutSession.hashCode());
		result = prime * result + ((dateFinSession == null) ? 0 : dateFinSession.hashCode());
		result = prime * result + ((dateSession == null) ? 0 : dateSession.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((idFondDeCaisseCloture == null) ? 0 : idFondDeCaisseCloture.hashCode());
		result = prime * result + ((idFondDeCaisseOuverture == null) ? 0 : idFondDeCaisseOuverture.hashCode());
		result = prime * result + ((idUtilisateur == null) ? 0 : idUtilisateur.hashCode());
		result = prime * result + ((libelle == null) ? 0 : libelle.hashCode());
		result = prime * result + ((montantFondDeCaisseCloture == null) ? 0 : montantFondDeCaisseCloture.hashCode());
		result = prime * result
				+ ((montantFondDeCaisseOuverture == null) ? 0 : montantFondDeCaisseOuverture.hashCode());
		result = prime * result + ((montantHtCumulAnnuel == null) ? 0 : montantHtCumulAnnuel.hashCode());
		result = prime * result + ((montantHtCumulExercice == null) ? 0 : montantHtCumulExercice.hashCode());
		result = prime * result + ((montantHtCumulMensuel == null) ? 0 : montantHtCumulMensuel.hashCode());
		result = prime * result + ((montantHtSession == null) ? 0 : montantHtSession.hashCode());
		result = prime * result + ((montantRemiseSession == null) ? 0 : montantRemiseSession.hashCode());
		result = prime * result + ((montantTtcCumulAnnuel == null) ? 0 : montantTtcCumulAnnuel.hashCode());
		result = prime * result + ((montantTtcCumulExercice == null) ? 0 : montantTtcCumulExercice.hashCode());
		result = prime * result + ((montantTtcCumulMensuel == null) ? 0 : montantTtcCumulMensuel.hashCode());
		result = prime * result + ((montantTtcSession == null) ? 0 : montantTtcSession.hashCode());
		result = prime * result + ((montantTvaCumulAnnuel == null) ? 0 : montantTvaCumulAnnuel.hashCode());
		result = prime * result + ((montantTvaCumulExercice == null) ? 0 : montantTvaCumulExercice.hashCode());
		result = prime * result + ((montantTvaCumulMensuel == null) ? 0 : montantTvaCumulMensuel.hashCode());
		result = prime * result + ((montantTvaSession == null) ? 0 : montantTvaSession.hashCode());
		result = prime * result + ((numeroCaisse == null) ? 0 : numeroCaisse.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		result = prime * result + ((verrouillageTicketZ == null) ? 0 : verrouillageTicketZ.hashCode());
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

	public Date getDateSession() {
		return dateSession;
	}

	public void setDateSession(Date dateSession) {
		firePropertyChange("dateSession", this.dateSession, this.dateSession = dateSession);
	}

	public Date getDateDebutSession() {
		return dateDebutSession;
	}

	public void setDateDebutSession(Date dateDebutSession) {
		firePropertyChange("dateDebutSession", this.dateDebutSession, this.dateDebutSession = dateDebutSession);
	}

	public Date getDateFinSession() {
		return dateFinSession;
	}

	public void setDateFinSession(Date dateFinSession) {
		firePropertyChange("dateFinSession", this.dateFinSession, this.dateFinSession = dateFinSession);
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		firePropertyChange("libelle", this.libelle, this.libelle = libelle);
	}

	public Boolean getAutomatique() {
		return automatique;
	}

	public void setAutomatique(Boolean automatique) {
		firePropertyChange("automatique", this.automatique, this.automatique = automatique);
	}

	public Boolean getVerrouillageTicketZ() {
		return verrouillageTicketZ;
	}

	public void setVerrouillageTicketZ(Boolean verrouillageTicketZ) {
		firePropertyChange("verrouillageTicketZ", this.verrouillageTicketZ, this.verrouillageTicketZ = verrouillageTicketZ);
	}

	public BigDecimal getMontantFondDeCaisseOuverture() {
		return montantFondDeCaisseOuverture;
	}

	public void setMontantFondDeCaisseOuverture(BigDecimal montantFondDeCaisseOuverture) {
		firePropertyChange("montantFondDeCaisseOuverture", this.montantFondDeCaisseOuverture, this.montantFondDeCaisseOuverture = montantFondDeCaisseOuverture);
	}

	public Integer getIdFondDeCaisseOuverture() {
		return idFondDeCaisseOuverture;
	}

	public void setIdFondDeCaisseOuverture(Integer idFondDeCaisseOuverture) {
		firePropertyChange("idFondDeCaisseOuverture", this.idFondDeCaisseOuverture, this.idFondDeCaisseOuverture = idFondDeCaisseOuverture);
	}

	public BigDecimal getMontantHtSession() {
		return montantHtSession;
	}

	public void setMontantHtSession(BigDecimal montantHtSession) {
		firePropertyChange("montantHtSession", this.montantHtSession, this.montantHtSession = montantHtSession);
	}

	public BigDecimal getMontantHtCumulMensuel() {
		return montantHtCumulMensuel;
	}

	public void setMontantHtCumulMensuel(BigDecimal montantHtCumulMensuel) {
		firePropertyChange("montantHtCumulMensuel", this.montantHtCumulMensuel, this.montantHtCumulMensuel = montantHtCumulMensuel);
	}

	public BigDecimal getMontantHtCumulAnnuel() {
		return montantHtCumulAnnuel;
	}

	public void setMontantHtCumulAnnuel(BigDecimal montantHtCumulAnnuel) {
		firePropertyChange("montantHtCumulAnnuel", this.montantHtCumulAnnuel, this.montantHtCumulAnnuel = montantHtCumulAnnuel);
	}

	public BigDecimal getMontantHtCumulExercice() {
		return montantHtCumulExercice;
	}

	public void setMontantHtCumulExercice(BigDecimal montantHtCumulExercice) {
		firePropertyChange("montantHtCumulExercice", this.montantHtCumulExercice, this.montantHtCumulExercice = montantHtCumulExercice);
	}

	public BigDecimal getMontantTtcSession() {
		return montantTtcSession;
	}

	public void setMontantTtcSession(BigDecimal montantTtcSession) {
		firePropertyChange("montantTtcSession", this.montantTtcSession, this.montantTtcSession = montantTtcSession);
	}

	public BigDecimal getMontantTtcCumulMensuel() {
		return montantTtcCumulMensuel;
	}

	public void setMontantTtcCumulMensuel(BigDecimal montantTtcCumulMensuel) {
		firePropertyChange("montantTtcCumulMensuel", this.montantTtcCumulMensuel, this.montantTtcCumulMensuel = montantTtcCumulMensuel);
	}

	public BigDecimal getMontantTtcCumulAnnuel() {
		return montantTtcCumulAnnuel;
	}

	public void setMontantTtcCumulAnnuel(BigDecimal montantTtcCumulAnnuel) {
		firePropertyChange("montantTtcCumulAnnuel", this.montantTtcCumulAnnuel, this.montantTtcCumulAnnuel = montantTtcCumulAnnuel);
	}

	public BigDecimal getMontantTtcCumulExercice() {
		return montantTtcCumulExercice;
	}

	public void setMontantTtcCumulExercice(BigDecimal montantTtcCumulExercice) {
		firePropertyChange("montantTtcCumulExercice", this.montantTtcCumulExercice, this.montantTtcCumulExercice = montantTtcCumulExercice);
	}

	public BigDecimal getMontantTvaSession() {
		return montantTvaSession;
	}

	public void setMontantTvaSession(BigDecimal montantTvaSession) {
		firePropertyChange("montantTvaSession", this.montantTvaSession, this.montantTvaSession = montantTvaSession);
	}

	public BigDecimal getMontantTvaCumulMensuel() {
		return montantTvaCumulMensuel;
	}

	public void setMontantTvaCumulMensuel(BigDecimal montantTvaCumulMensuel) {
		firePropertyChange("montantTvaCumulMensuel", this.montantTvaCumulMensuel, this.montantTvaCumulMensuel = montantTvaCumulMensuel);
	}

	public BigDecimal getMontantTvaCumulAnnuel() {
		return montantTvaCumulAnnuel;
	}

	public void setMontantTvaCumulAnnuel(BigDecimal montantTvaCumulAnnuel) {
		firePropertyChange("montantTvaCumulAnnuel", this.montantTvaCumulAnnuel, this.montantTvaCumulAnnuel = montantTvaCumulAnnuel);
	}

	public BigDecimal getMontantTvaCumulExercice() {
		return montantTvaCumulExercice;
	}

	public void setMontantTvaCumulExercice(BigDecimal montantTvaCumulExercice) {
		firePropertyChange("montantTvaCumulExercice", this.montantTvaCumulExercice, this.montantTvaCumulExercice = montantTvaCumulExercice);
	}

	public BigDecimal getMontantRemiseSession() {
		return montantRemiseSession;
	}

	public void setMontantRemiseSession(BigDecimal montantRemiseSession) {
		firePropertyChange("montantRemiseSession", this.montantRemiseSession, this.montantRemiseSession = montantRemiseSession);
	}

	public BigDecimal getMontantFondDeCaisseCloture() {
		return montantFondDeCaisseCloture;
	}

	public void setMontantFondDeCaisseCloture(BigDecimal montantFondDeCaisseCloture) {
		firePropertyChange("montantFondDeCaisseCloture", this.montantFondDeCaisseCloture, this.montantFondDeCaisseCloture = montantFondDeCaisseCloture);
	}

	public Integer getIdFondDeCaisseCloture() {
		return idFondDeCaisseCloture;
	}

	public void setIdFondDeCaisseCloture(Integer idFondDeCaisseCloture) {
		firePropertyChange("idFondDeCaisseCloture", this.idFondDeCaisseCloture, this.idFondDeCaisseCloture = idFondDeCaisseCloture);
	}

}
