package fr.legrain.caisse.dto;

import java.math.BigDecimal;

import fr.legrain.bdg.model.ModelObject;


public class TaLSessionCaisseDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 7897291462025293861L;
	
	private Integer id;
	
	private int idSessionCaisse;
	private String codeSessionCaisse;

	private int idTLSessionCaisse;
	private String codeTLSessionCaisse;
	
	private String libelle;
	private String code;
	private BigDecimal taux;
	private Integer idExt; //id tx tva ou id article ou id TaTPaiement ou ...

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
	
	private Integer versionObj;

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public TaLSessionCaisseDTO() {
	}

	public TaLSessionCaisseDTO(Integer id, int idSessionCaisse, String codeSessionCaisse, int idTLSessionCaisse,
			String codeTLSessionCaisse, String libelle, String code, BigDecimal taux, Integer idExt,
			BigDecimal montantHtSession, BigDecimal montantHtCumulMensuel, BigDecimal montantHtCumulAnnuel,
			BigDecimal montantHtCumulExercice, BigDecimal montantTtcSession, BigDecimal montantTtcCumulMensuel,
			BigDecimal montantTtcCumulAnnuel, BigDecimal montantTtcCumulExercice, BigDecimal montantTvaSession,
			BigDecimal montantTvaCumulMensuel, BigDecimal montantTvaCumulAnnuel, BigDecimal montantTvaCumulExercice,
			Integer versionObj) {
		super();
		this.id = id;
		this.idSessionCaisse = idSessionCaisse;
		this.codeSessionCaisse = codeSessionCaisse;
		this.idTLSessionCaisse = idTLSessionCaisse;
		this.codeTLSessionCaisse = codeTLSessionCaisse;
		this.libelle = libelle;
		this.code = code;
		this.taux = taux;
		this.idExt = idExt;
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
		this.versionObj = versionObj;
	}
	
	public void setSWTTaSessionCaisse(TaLSessionCaisseDTO taSessionCaisseDTO) {
		this.id = taSessionCaisseDTO.id;
		this.codeSessionCaisse = taSessionCaisseDTO.codeSessionCaisse;
		this.libelle = taSessionCaisseDTO.libelle;

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
		
		this.idSessionCaisse = taSessionCaisseDTO.idSessionCaisse;
		this.idTLSessionCaisse = taSessionCaisseDTO.idTLSessionCaisse;
		this.codeTLSessionCaisse = taSessionCaisseDTO.codeTLSessionCaisse;
		this.code = taSessionCaisseDTO.code;
		this.taux = taSessionCaisseDTO.taux;
		this.idExt = taSessionCaisseDTO.idExt;
		
	}
	
	public static TaLSessionCaisseDTO copy(TaLSessionCaisseDTO taLSessionCaisseDTO){
		TaLSessionCaisseDTO taLSessionCaisseDTOLoc = new TaLSessionCaisseDTO();
		taLSessionCaisseDTOLoc.setId(taLSessionCaisseDTO.id);
		taLSessionCaisseDTOLoc.setCodeSessionCaisse(taLSessionCaisseDTO.codeSessionCaisse);

		taLSessionCaisseDTOLoc.setLibelle(taLSessionCaisseDTO.libelle);

		taLSessionCaisseDTOLoc.setMontantHtSession(taLSessionCaisseDTO.montantHtSession);
		taLSessionCaisseDTOLoc.setMontantHtCumulMensuel(taLSessionCaisseDTO.montantHtCumulMensuel);
		taLSessionCaisseDTOLoc.setMontantHtCumulAnnuel(taLSessionCaisseDTO.montantHtCumulAnnuel);
		taLSessionCaisseDTOLoc.setMontantHtCumulExercice(taLSessionCaisseDTO.montantHtCumulExercice);

		taLSessionCaisseDTOLoc.setMontantTtcSession(taLSessionCaisseDTO.montantTtcSession);
		taLSessionCaisseDTOLoc.setMontantTtcCumulMensuel(taLSessionCaisseDTO.montantTtcCumulMensuel);
		taLSessionCaisseDTOLoc.setMontantTtcCumulAnnuel(taLSessionCaisseDTO.montantTtcCumulAnnuel);
		taLSessionCaisseDTOLoc.setMontantTtcCumulExercice(taLSessionCaisseDTO.montantTtcCumulExercice);

		taLSessionCaisseDTOLoc.setMontantTvaSession(taLSessionCaisseDTO.montantTvaSession);
		taLSessionCaisseDTOLoc.setMontantTvaCumulMensuel(taLSessionCaisseDTO.montantTvaCumulMensuel);
		taLSessionCaisseDTOLoc.setMontantTvaCumulAnnuel(taLSessionCaisseDTO.montantTvaCumulAnnuel);
		taLSessionCaisseDTOLoc.setMontantTvaCumulExercice(taLSessionCaisseDTO.montantTvaCumulExercice);
		
		taLSessionCaisseDTOLoc.setIdSessionCaisse(taLSessionCaisseDTO.idSessionCaisse);
		taLSessionCaisseDTOLoc.setIdTLSessionCaisse(taLSessionCaisseDTO.idTLSessionCaisse);
		taLSessionCaisseDTOLoc.setCodeTLSessionCaisse(taLSessionCaisseDTO.codeTLSessionCaisse);
		taLSessionCaisseDTOLoc.setCode(taLSessionCaisseDTO.code);
		taLSessionCaisseDTOLoc.setTaux(taLSessionCaisseDTO.taux);
		taLSessionCaisseDTOLoc.setIdExt(taLSessionCaisseDTO.idExt);

		return taLSessionCaisseDTOLoc;
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
		TaLSessionCaisseDTO other = (TaLSessionCaisseDTO) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (codeSessionCaisse == null) {
			if (other.codeSessionCaisse != null)
				return false;
		} else if (!codeSessionCaisse.equals(other.codeSessionCaisse))
			return false;
		if (codeTLSessionCaisse == null) {
			if (other.codeTLSessionCaisse != null)
				return false;
		} else if (!codeTLSessionCaisse.equals(other.codeTLSessionCaisse))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (idExt == null) {
			if (other.idExt != null)
				return false;
		} else if (!idExt.equals(other.idExt))
			return false;
		if (idSessionCaisse != other.idSessionCaisse)
			return false;
		if (idTLSessionCaisse != other.idTLSessionCaisse)
			return false;
		if (libelle == null) {
			if (other.libelle != null)
				return false;
		} else if (!libelle.equals(other.libelle))
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
		if (taux == null) {
			if (other.taux != null)
				return false;
		} else if (!taux.equals(other.taux))
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
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((codeSessionCaisse == null) ? 0 : codeSessionCaisse.hashCode());
		result = prime * result + ((codeTLSessionCaisse == null) ? 0 : codeTLSessionCaisse.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((idExt == null) ? 0 : idExt.hashCode());
		result = prime * result + idSessionCaisse;
		result = prime * result + idTLSessionCaisse;
		result = prime * result + ((libelle == null) ? 0 : libelle.hashCode());
		result = prime * result + ((montantHtCumulAnnuel == null) ? 0 : montantHtCumulAnnuel.hashCode());
		result = prime * result + ((montantHtCumulExercice == null) ? 0 : montantHtCumulExercice.hashCode());
		result = prime * result + ((montantHtCumulMensuel == null) ? 0 : montantHtCumulMensuel.hashCode());
		result = prime * result + ((montantHtSession == null) ? 0 : montantHtSession.hashCode());
		result = prime * result + ((montantTtcCumulAnnuel == null) ? 0 : montantTtcCumulAnnuel.hashCode());
		result = prime * result + ((montantTtcCumulExercice == null) ? 0 : montantTtcCumulExercice.hashCode());
		result = prime * result + ((montantTtcCumulMensuel == null) ? 0 : montantTtcCumulMensuel.hashCode());
		result = prime * result + ((montantTtcSession == null) ? 0 : montantTtcSession.hashCode());
		result = prime * result + ((montantTvaCumulAnnuel == null) ? 0 : montantTvaCumulAnnuel.hashCode());
		result = prime * result + ((montantTvaCumulExercice == null) ? 0 : montantTvaCumulExercice.hashCode());
		result = prime * result + ((montantTvaCumulMensuel == null) ? 0 : montantTvaCumulMensuel.hashCode());
		result = prime * result + ((montantTvaSession == null) ? 0 : montantTvaSession.hashCode());
		result = prime * result + ((taux == null) ? 0 : taux.hashCode());
		result = prime * result + ((versionObj == null) ? 0 : versionObj.hashCode());
		return result;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		firePropertyChange("libelle", this.libelle, this.libelle = libelle);
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

	public int getIdSessionCaisse() {
		return idSessionCaisse;
	}

	public void setIdSessionCaisse(int idSessionCaisse) {
		firePropertyChange("idSessionCaisse", this.idSessionCaisse, this.idSessionCaisse = idSessionCaisse);
	}

	public int getIdTLSessionCaisse() {
		return idTLSessionCaisse;
	}

	public void setIdTLSessionCaisse(int idTLSessionCaisse) {
		firePropertyChange("idTLSessionCaisse", this.idTLSessionCaisse, this.idTLSessionCaisse = idTLSessionCaisse);
	}

	public String getCodeTLSessionCaisse() {
		return codeTLSessionCaisse;
	}

	public void setCodeTLSessionCaisse(String codeTLSessionCaisse) {
		firePropertyChange("codeTLSessionCaisse", this.codeTLSessionCaisse, this.codeTLSessionCaisse = codeTLSessionCaisse);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		firePropertyChange("code", this.code, this.code = code);
	}

	public BigDecimal getTaux() {
		return taux;
	}

	public void setTaux(BigDecimal taux) {
		firePropertyChange("taux", this.taux, this.taux = taux);
	}

	public Integer getIdExt() {
		return idExt;
	}

	public void setIdExt(Integer idExt) {
		firePropertyChange("idExt", this.idExt, this.idExt = idExt);
	}

}
