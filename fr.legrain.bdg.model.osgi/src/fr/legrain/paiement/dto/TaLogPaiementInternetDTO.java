package fr.legrain.paiement.dto;

import java.math.BigDecimal;
import java.util.Date;

import fr.legrain.bdg.model.ModelObject;

public class TaLogPaiementInternetDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 6276712985361296130L;
	
	private Integer id;
	
	private Date datePaiement;
	private String refPaiementService;
	private BigDecimal montantPaiement;
	private String devise;
	private Integer idReglement;
	private String codeReglement;
	private Integer idTiers;
	private String codeTiers;
	private String nomTiers;
	private Integer idDocument;
	private String codeDocument;
	private String typeDocument;
	
	private String originePaiement;
	private String servicePaiement;
	private String commentaire;
	
	private Integer versionObj;
	

	public TaLogPaiementInternetDTO() {
	}

	public void setSWTAdresse(TaLogPaiementInternetDTO taLogPaiementInternetDTO) {
		this.id = taLogPaiementInternetDTO.id;
		this.datePaiement = taLogPaiementInternetDTO.datePaiement;
		this.refPaiementService = taLogPaiementInternetDTO.refPaiementService;
		this.montantPaiement = taLogPaiementInternetDTO.montantPaiement;
		this.idReglement = taLogPaiementInternetDTO.idReglement;
		this.codeReglement = taLogPaiementInternetDTO.codeReglement;
		this.idTiers = taLogPaiementInternetDTO.idTiers;
		this.codeTiers = taLogPaiementInternetDTO.codeTiers;
		this.nomTiers = taLogPaiementInternetDTO.nomTiers;
		this.idDocument = taLogPaiementInternetDTO.idDocument;
		this.codeDocument = taLogPaiementInternetDTO.codeDocument;
		this.typeDocument = taLogPaiementInternetDTO.typeDocument;
		this.originePaiement = taLogPaiementInternetDTO.originePaiement;
		this.servicePaiement = taLogPaiementInternetDTO.servicePaiement;
		this.commentaire = taLogPaiementInternetDTO.commentaire;
	}

	
	public static TaLogPaiementInternetDTO copy(TaLogPaiementInternetDTO taLogPaiementInternetDTO){
		TaLogPaiementInternetDTO taLogPaiementInternetDTOLoc = new TaLogPaiementInternetDTO();
		taLogPaiementInternetDTOLoc.setId(taLogPaiementInternetDTO.getId());                //1
		taLogPaiementInternetDTOLoc.setDatePaiement(taLogPaiementInternetDTO.getDatePaiement());                //1
		taLogPaiementInternetDTOLoc.setRefPaiementService(taLogPaiementInternetDTO.getRefPaiementService());                //1
		taLogPaiementInternetDTOLoc.setMontantPaiement(taLogPaiementInternetDTO.getMontantPaiement());                //1
		taLogPaiementInternetDTOLoc.setIdReglement(taLogPaiementInternetDTO.getIdReglement());                //1
		taLogPaiementInternetDTOLoc.setCodeReglement(taLogPaiementInternetDTO.getCodeReglement());                //1
		taLogPaiementInternetDTOLoc.setIdTiers(taLogPaiementInternetDTO.getIdTiers());                //1
		taLogPaiementInternetDTOLoc.setCodeTiers(taLogPaiementInternetDTO.getCodeTiers());                //1
		taLogPaiementInternetDTOLoc.setNomTiers(taLogPaiementInternetDTO.getNomTiers());                //1
		taLogPaiementInternetDTOLoc.setIdDocument(taLogPaiementInternetDTO.getIdDocument()); 
		taLogPaiementInternetDTOLoc.setCodeDocument(taLogPaiementInternetDTO.getCodeDocument()); 
		taLogPaiementInternetDTOLoc.setTypeDocument(taLogPaiementInternetDTO.getTypeDocument()); 
		taLogPaiementInternetDTOLoc.setOriginePaiement(taLogPaiementInternetDTO.getOriginePaiement()); 
		taLogPaiementInternetDTOLoc.setServicePaiement(taLogPaiementInternetDTO.getServicePaiement()); 
		taLogPaiementInternetDTOLoc.setCommentaire(taLogPaiementInternetDTO.getCommentaire()); 
		return taLogPaiementInternetDTOLoc;
	}
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}

	public Date getDatePaiement() {
		return datePaiement;
	}

	public void setDatePaiement(Date datePaiement) {
		this.datePaiement = datePaiement;
	}

	public String getRefPaiementService() {
		return refPaiementService;
	}

	public void setRefPaiementService(String refPaiementService) {
		this.refPaiementService = refPaiementService;
	}

	public BigDecimal getMontantPaiement() {
		return montantPaiement;
	}

	public void setMontantPaiement(BigDecimal montantPaiement) {
		this.montantPaiement = montantPaiement;
	}

	public Integer getIdReglement() {
		return idReglement;
	}

	public void setIdReglement(Integer idReglement) {
		this.idReglement = idReglement;
	}

	public String getCodeReglement() {
		return codeReglement;
	}

	public void setCodeReglement(String codeReglement) {
		this.codeReglement = codeReglement;
	}

	public Integer getIdTiers() {
		return idTiers;
	}

	public void setIdTiers(Integer idTiers) {
		this.idTiers = idTiers;
	}

	public String getCodeTiers() {
		return codeTiers;
	}

	public void setCodeTiers(String codeTiers) {
		this.codeTiers = codeTiers;
	}

	public String getNomTiers() {
		return nomTiers;
	}

	public void setNomTiers(String nomTiers) {
		this.nomTiers = nomTiers;
	}

	public Integer getIdDocument() {
		return idDocument;
	}

	public void setIdDocument(Integer idDocument) {
		this.idDocument = idDocument;
	}

	public String getCodeDocument() {
		return codeDocument;
	}

	public void setCodeDocument(String codeDocument) {
		this.codeDocument = codeDocument;
	}

	public String getTypeDocument() {
		return typeDocument;
	}

	public void setTypeDocument(String typeDocument) {
		this.typeDocument = typeDocument;
	}

	public String getOriginePaiement() {
		return originePaiement;
	}

	public void setOriginePaiement(String originePaiement) {
		this.originePaiement = originePaiement;
	}

	public String getServicePaiement() {
		return servicePaiement;
	}

	public void setServicePaiement(String servicePaiement) {
		this.servicePaiement = servicePaiement;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public String getDevise() {
		return devise;
	}

	public void setDevise(String devise) {
		this.devise = devise;
	}

}
