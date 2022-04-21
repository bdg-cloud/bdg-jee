package fr.legrain.tache.dto;

import java.util.Date;

import fr.legrain.bdg.model.ModelObject;

public class TaEvenementDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 1730797494999173806L;
	
	private Integer id;
	
	//private TaUtilisateur proprietaire;
	private String codeEvenement;
	private String libelleEvenement;
	private String description;
	private String emplacement;
	private String couleur;
	private Date dateDebut;
	private Date dateFin;
	private boolean enCours;
	private boolean termine;
	private boolean recurrent;
	private boolean prive;
	private boolean touteLaJournee;
	private Integer nbMinuteDuree;
	private Integer nbHeureDuree;
	private Integer nbJoursDuree;
	
	private Integer idRecurrence;
	private Date dateDebutRecurrence;
	private Date dateFinRecurrence;
	
//	private List<TaTiers> listeTiers;
//	private List<TaUtilisateur> listeUtilisateur;
//	private List<TaRDocumentEvenement> listeDocument;
	
	private int idTypeEvenement;
	private String codeTypeEvenement;
	private String libelleTypeEvenement;
	
	private int idCategorieEvenement;
	private String codeCategorieEvenement;
	private String libelleCategorieEvenement;
	
	private int idAgenda;
	private String nomAgenda;
	
	private int idUtilisateur;
	
	private Integer versionObj;

	public TaEvenementDTO() {
	}

	public void setTaEvenementDTO(TaEvenementDTO taEvenementDTO) {
		this.id = taEvenementDTO.id;
		this.codeCategorieEvenement = taEvenementDTO.codeCategorieEvenement;
		this.codeEvenement = taEvenementDTO.codeEvenement;
		this.codeTypeEvenement = taEvenementDTO.codeTypeEvenement;
		this.couleur = taEvenementDTO.couleur;
		this.dateDebut = taEvenementDTO.dateDebut;
		this.dateDebutRecurrence = taEvenementDTO.dateDebutRecurrence;
		this.dateFin = taEvenementDTO.dateFin;
		this.dateFinRecurrence = taEvenementDTO.dateFinRecurrence;
		this.description = taEvenementDTO.description;
		this.emplacement = taEvenementDTO.emplacement;
		this.enCours = taEvenementDTO.enCours;
		this.idAgenda = taEvenementDTO.idAgenda;
		this.idCategorieEvenement = taEvenementDTO.idCategorieEvenement;
		this.idRecurrence = taEvenementDTO.idRecurrence;
		this.idTypeEvenement = taEvenementDTO.idTypeEvenement;
		this.idUtilisateur = taEvenementDTO.idUtilisateur;
		this.libelleCategorieEvenement = taEvenementDTO.libelleCategorieEvenement;
		this.libelleEvenement = taEvenementDTO.libelleEvenement;
		this.libelleTypeEvenement = taEvenementDTO.libelleTypeEvenement;
		this.nbHeureDuree = taEvenementDTO.nbHeureDuree;
		this.nbJoursDuree = taEvenementDTO.nbJoursDuree;
		this.nbMinuteDuree = taEvenementDTO.nbMinuteDuree;
		this.nomAgenda = taEvenementDTO.nomAgenda;
		this.prive = taEvenementDTO.prive;
		this.recurrent = taEvenementDTO.recurrent;
		this.termine = taEvenementDTO.termine;
		this.touteLaJournee = taEvenementDTO.touteLaJournee;
	}

	public static TaEvenementDTO copy(TaEvenementDTO taEvenementDTO){
		TaEvenementDTO taEvenementDTOLoc = new TaEvenementDTO();
		taEvenementDTOLoc.setId(taEvenementDTO.getId());              
		taEvenementDTOLoc.setCodeCategorieEvenement(taEvenementDTO.getCodeCategorieEvenement());                
		taEvenementDTOLoc.setCodeEvenement(taEvenementDTO.getCodeEvenement());           
		taEvenementDTOLoc.setCodeTypeEvenement(taEvenementDTO.getCodeTypeEvenement());             
		taEvenementDTOLoc.setCouleur(taEvenementDTO.getCouleur());              
		taEvenementDTOLoc.setDateDebut(taEvenementDTO.getDateDebut());
		taEvenementDTOLoc.setDateDebutRecurrence(taEvenementDTO.getDateDebutRecurrence());             
		taEvenementDTOLoc.setDateFin(taEvenementDTO.getDateFin());             
		taEvenementDTOLoc.setDateFinRecurrence(taEvenementDTO.getDateFinRecurrence());             
		taEvenementDTOLoc.setDescription(taEvenementDTO.getDescription());             
		taEvenementDTOLoc.setEmplacement(taEvenementDTO.getEmplacement());             
		taEvenementDTOLoc.setEnCours(taEvenementDTO.isEnCours());             
		taEvenementDTOLoc.setIdAgenda(taEvenementDTO.getIdAgenda());             
		taEvenementDTOLoc.setIdCategorieEvenement(taEvenementDTO.getIdCategorieEvenement());             
		taEvenementDTOLoc.setIdRecurrence(taEvenementDTO.getIdRecurrence());             
		taEvenementDTOLoc.setIdTypeEvenement(taEvenementDTO.getIdTypeEvenement());             
		taEvenementDTOLoc.setIdUtilisateur(taEvenementDTO.getIdUtilisateur());             
		taEvenementDTOLoc.setLibelleCategorieEvenement(taEvenementDTO.getLibelleCategorieEvenement());             
		taEvenementDTOLoc.setLibelleEvenement(taEvenementDTO.getLibelleEvenement());             
		taEvenementDTOLoc.setLibelleTypeEvenement(taEvenementDTO.getLibelleTypeEvenement());             
		taEvenementDTOLoc.setNbHeureDuree(taEvenementDTO.getNbHeureDuree());             
		taEvenementDTOLoc.setNbJoursDuree(taEvenementDTO.getNbJoursDuree());             
		taEvenementDTOLoc.setNbMinuteDuree(taEvenementDTO.getNbMinuteDuree());             
		taEvenementDTOLoc.setNomAgenda(taEvenementDTO.getNomAgenda());             
		taEvenementDTOLoc.setPrive(taEvenementDTO.isPrive());             
		taEvenementDTOLoc.setRecurrent(taEvenementDTO.isRecurrent());             
		taEvenementDTOLoc.setTermine(taEvenementDTO.isTermine());             
		taEvenementDTOLoc.setTouteLaJournee(taEvenementDTO.isTouteLaJournee());             
		return taEvenementDTOLoc;
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

	public String getCodeEvenement() {
		return codeEvenement;
	}

	public void setCodeEvenement(String codeEvenement) {
		firePropertyChange("codeEvenement", this.codeEvenement, this.codeEvenement = codeEvenement);
	}

	public String getLibelleEvenement() {
		return libelleEvenement;
	}

	public void setLibelleEvenement(String libelleEvenement) {
		firePropertyChange("libelleEvenement", this.libelleEvenement, this.libelleEvenement = libelleEvenement);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		firePropertyChange("description", this.description, this.description = description);
	}

	public String getEmplacement() {
		return emplacement;
	}

	public void setEmplacement(String emplacement) {
		firePropertyChange("emplacement", this.emplacement, this.emplacement = emplacement);
	}

	public String getCouleur() {
		return couleur;
	}

	public void setCouleur(String couleur) {
		firePropertyChange("couleur", this.couleur, this.couleur = couleur);
	}

	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		firePropertyChange("dateDebut", this.dateDebut, this.dateDebut = dateDebut);
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		firePropertyChange("dateFin", this.dateFin, this.dateFin = dateFin);
	}

	public boolean isEnCours() {
		return enCours;
	}

	public void setEnCours(boolean enCours) {
		firePropertyChange("enCours", this.enCours, this.enCours = enCours);
	}

	public boolean isTermine() {
		return termine;
	}

	public void setTermine(boolean termine) {
		firePropertyChange("termine", this.termine, this.termine = termine);
	}

	public boolean isRecurrent() {
		return recurrent;
	}

	public void setRecurrent(boolean recurrent) {
		firePropertyChange("recurrent", this.recurrent, this.recurrent = recurrent);
	}

	public boolean isPrive() {
		return prive;
	}

	public void setPrive(boolean prive) {
		firePropertyChange("prive", this.prive, this.prive = prive);
	}

	public boolean isTouteLaJournee() {
		return touteLaJournee;
	}

	public void setTouteLaJournee(boolean touteLaJournee) {
		firePropertyChange("touteLaJournee", this.touteLaJournee, this.touteLaJournee = touteLaJournee);
	}

	public Integer getNbMinuteDuree() {
		return nbMinuteDuree;
	}

	public void setNbMinuteDuree(Integer nbMinuteDuree) {
		firePropertyChange("nbMinuteDuree", this.nbMinuteDuree, this.nbMinuteDuree = nbMinuteDuree);
	}

	public Integer getNbHeureDuree() {
		return nbHeureDuree;
	}

	public void setNbHeureDuree(Integer nbHeureDuree) {
		firePropertyChange("nbHeureDuree", this.nbHeureDuree, this.nbHeureDuree = nbHeureDuree);
	}

	public Integer getNbJoursDuree() {
		return nbJoursDuree;
	}

	public void setNbJoursDuree(Integer nbJoursDuree) {
		firePropertyChange("nbJoursDuree", this.nbJoursDuree, this.nbJoursDuree = nbJoursDuree);
	}

	public Integer getIdRecurrence() {
		return idRecurrence;
	}

	public void setIdRecurrence(Integer idRecurrence) {
		firePropertyChange("idRecurrence", this.idRecurrence, this.idRecurrence = idRecurrence);
	}

	public Date getDateDebutRecurrence() {
		return dateDebutRecurrence;
	}

	public void setDateDebutRecurrence(Date dateDebutRecurrence) {
		firePropertyChange("dateDebutRecurrence", this.dateDebutRecurrence, this.dateDebutRecurrence = dateDebutRecurrence);
	}

	public Date getDateFinRecurrence() {
		return dateFinRecurrence;
	}

	public void setDateFinRecurrence(Date dateFinRecurrence) {
		firePropertyChange("dateFinRecurrence", this.dateFinRecurrence, this.dateFinRecurrence = dateFinRecurrence);
	}

	public int getIdTypeEvenement() {
		return idTypeEvenement;
	}

	public void setIdTypeEvenement(int idTypeEvenement) {
		firePropertyChange("idTypeEvenement", this.idTypeEvenement, this.idTypeEvenement = idTypeEvenement);
	}

	public String getCodeTypeEvenement() {
		return codeTypeEvenement;
	}

	public void setCodeTypeEvenement(String codeTypeEvenement) {
		firePropertyChange("codeTypeEvenement", this.codeTypeEvenement, this.codeTypeEvenement = codeTypeEvenement);
	}

	public String getLibelleTypeEvenement() {
		return libelleTypeEvenement;
	}

	public void setLibelleTypeEvenement(String libelleTypeEvenement) {
		firePropertyChange("libelleTypeEvenement", this.libelleTypeEvenement, this.libelleTypeEvenement = libelleTypeEvenement);
	}

	public int getIdCategorieEvenement() {
		return idCategorieEvenement;
	}

	public void setIdCategorieEvenement(int idCategorieEvenement) {
		firePropertyChange("idCategorieEvenement", this.idCategorieEvenement, this.idCategorieEvenement = idCategorieEvenement);
	}

	public String getCodeCategorieEvenement() {
		return codeCategorieEvenement;
	}

	public void setCodeCategorieEvenement(String codeCategorieEvenement) {
		firePropertyChange("codeCategorieEvenement", this.codeCategorieEvenement, this.codeCategorieEvenement = codeCategorieEvenement);
	}

	public String getLibelleCategorieEvenement() {
		return libelleCategorieEvenement;
	}

	public void setLibelleCategorieEvenement(String libelleCategorieEvenement) {
		firePropertyChange("libelleCategorieEvenement", this.libelleCategorieEvenement, this.libelleCategorieEvenement = libelleCategorieEvenement);
	}

	public int getIdAgenda() {
		return idAgenda;
	}

	public void setIdAgenda(int idAgenda) {
		firePropertyChange("idAgenda", this.idAgenda, this.idAgenda = idAgenda);
	}

	public String getNomAgenda() {
		return nomAgenda;
	}

	public void setNomAgenda(String nomAgenda) {
		firePropertyChange("nomAgenda", this.nomAgenda, this.nomAgenda = nomAgenda);
	}

	public int getIdUtilisateur() {
		return idUtilisateur;
	}

	public void setIdUtilisateur(int idUtilisateur) {
		firePropertyChange("idUtilisateur", this.idUtilisateur, this.idUtilisateur = idUtilisateur);
	}

}
