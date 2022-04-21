package fr.legrain.general.dto;

import fr.legrain.bdg.model.ModelObject;


// Generated Mar 1, 2007 11:26:10 AM by Hibernate Tools 3.2.0.b9

/**
 * SWTFichier modifier par Dima
 */
public class TaFichierDTO extends ModelObject implements java.io.Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6425882089902323836L;

	private int idFichier;
	private String nomFichierOriginal;
	private byte[] blobFichierOriginal;
	private String cheminFichierOriginal;
	private String typeFichierOriginal;
	private String nomFichierMiniature;
	private byte[] blobFichierMiniature;
	private String cheminFichierMiniature;
	private String typeFichierMiniature;
	
	private Integer versionObj;

	public TaFichierDTO() {
	}

	/**
	 * @param idFichier
	 * @param nomFichierOriginal
	 * @param blobFichierOriginal
	 * @param cheminFichierOriginal
	 * @param typeFichierOriginal
	 * @param nomFichierMiniature
	 * @param blobFichierMiniature
	 * @param cheminFichierMiniature
	 * @param typeFichierMiniature
	 * @param versionObj
	 */
	public TaFichierDTO(int idFichier, String nomFichierOriginal,
			byte[] blobFichierOriginal, String cheminFichierOriginal,
			String typeFichierOriginal, String nomFichierMiniature,
			byte[] blobFichierMiniature, String cheminFichierMiniature,
			String typeFichierMiniature, Integer versionObj) {
		super();
		this.idFichier = idFichier;
		this.nomFichierOriginal = nomFichierOriginal;
		this.blobFichierOriginal = blobFichierOriginal;
		this.cheminFichierOriginal = cheminFichierOriginal;
		this.typeFichierOriginal = typeFichierOriginal;
		this.nomFichierMiniature = nomFichierMiniature;
		this.blobFichierMiniature = blobFichierMiniature;
		this.cheminFichierMiniature = cheminFichierMiniature;
		this.typeFichierMiniature = typeFichierMiniature;
		this.versionObj = versionObj;
	}

	public void setSWTFichier(TaFichierDTO swtFichier) {
		this.idFichier = swtFichier.idFichier;
		this.nomFichierOriginal = swtFichier.nomFichierOriginal;
		this.blobFichierOriginal = swtFichier.blobFichierOriginal;
		this.cheminFichierOriginal = swtFichier.cheminFichierOriginal;
		this.typeFichierOriginal = swtFichier.typeFichierOriginal;
		this.nomFichierMiniature = swtFichier.nomFichierMiniature;
		this.blobFichierMiniature = swtFichier.blobFichierMiniature;
		this.cheminFichierMiniature = swtFichier.cheminFichierMiniature;
		this.typeFichierMiniature = swtFichier.typeFichierMiniature;
		this.versionObj = swtFichier.versionObj;
	}
	
	/**
	 * @return the idFichier
	 */
	public int getIdFichier() {
		return idFichier;
	}

	/**
	 * @param idFichier the idFichier to set
	 */
	public void setIdFichier(int idFichier) {
		this.idFichier = idFichier;
	}

	/**
	 * @return the nomFichierOriginal
	 */
	public String getNomFichierOriginal() {
		return nomFichierOriginal;
	}

	/**
	 * @param nomFichierOriginal the nomFichierOriginal to set
	 */
	public void setNomFichierOriginal(String nomFichierOriginal) {
		this.nomFichierOriginal = nomFichierOriginal;
	}

	/**
	 * @return the blobFichierOriginal
	 */
	public byte[] getBlobFichierOriginal() {
		return blobFichierOriginal;
	}

	/**
	 * @param blobFichierOriginal the blobFichierOriginal to set
	 */
	public void setBlobFichierOriginal(byte[] blobFichierOriginal) {
		this.blobFichierOriginal = blobFichierOriginal;
	}

	/**
	 * @return the cheminFichierOriginal
	 */
	public String getCheminFichierOriginal() {
		return cheminFichierOriginal;
	}

	/**
	 * @param cheminFichierOriginal the cheminFichierOriginal to set
	 */
	public void setCheminFichierOriginal(String cheminFichierOriginal) {
		this.cheminFichierOriginal = cheminFichierOriginal;
	}

	/**
	 * @return the typeFichierOriginal
	 */
	public String getTypeFichierOriginal() {
		return typeFichierOriginal;
	}

	/**
	 * @param typeFichierOriginal the typeFichierOriginal to set
	 */
	public void setTypeFichierOriginal(String typeFichierOriginal) {
		this.typeFichierOriginal = typeFichierOriginal;
	}

	/**
	 * @return the nomFichierMiniature
	 */
	public String getNomFichierMiniature() {
		return nomFichierMiniature;
	}

	/**
	 * @param nomFichierMiniature the nomFichierMiniature to set
	 */
	public void setNomFichierMiniature(String nomFichierMiniature) {
		this.nomFichierMiniature = nomFichierMiniature;
	}

	/**
	 * @return the blobFichierMiniature
	 */
	public byte[] getBlobFichierMiniature() {
		return blobFichierMiniature;
	}

	/**
	 * @param blobFichierMiniature the blobFichierMiniature to set
	 */
	public void setBlobFichierMiniature(byte[] blobFichierMiniature) {
		this.blobFichierMiniature = blobFichierMiniature;
	}

	/**
	 * @return the cheminFichierMiniature
	 */
	public String getCheminFichierMiniature() {
		return cheminFichierMiniature;
	}

	/**
	 * @param cheminFichierMiniature the cheminFichierMiniature to set
	 */
	public void setCheminFichierMiniature(String cheminFichierMiniature) {
		this.cheminFichierMiniature = cheminFichierMiniature;
	}

	/**
	 * @return the typeFichierMiniature
	 */
	public String getTypeFichierMiniature() {
		return typeFichierMiniature;
	}

	/**
	 * @param typeFichierMiniature the typeFichierMiniature to set
	 */
	public void setTypeFichierMiniature(String typeFichierMiniature) {
		this.typeFichierMiniature = typeFichierMiniature;
	}

	/**
	 * @return the versionObj
	 */
	public Integer getVersionObj() {
		return versionObj;
	}

	/**
	 * @param versionObj the versionObj to set
	 */
	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}
	
	
	
//	public Integer getVersionObj() {
//		return versionObj;
//	}
//
//	public void setVersionObj(Integer versionObj) {
//		this.versionObj = versionObj;
//	}
//	
//	public TaFichierDTO(Integer ID_T_TVA, String CODE_T_TVA, String LIB_T_TVA) {
//		this.id = ID_T_TVA;
//		this.codeFichier = CODE_T_TVA;
//		this.libFichier = LIB_T_TVA;
//	}
//	
//	public void setSWTFichier(TaFichierDTO swtFichier) {
//		this.id = swtFichier.id;
//		this.codeFichier = swtFichier.codeFichier;
//		this.libFichier = swtFichier.libFichier;
//
//	}
//	
//	public static TaFichierDTO copy(TaFichierDTO swtFichier){
//		TaFichierDTO swtFichierLoc = new TaFichierDTO();
//		swtFichierLoc.setId(swtFichier.id);
//		swtFichierLoc.setCodeFichier(swtFichier.codeFichier);
//		swtFichierLoc.setLibFichier(swtFichier.libFichier);
//		return swtFichierLoc;
//	}
//
//	public Integer getId() {
//		return this.id;
//	}
//
//	public void setId(Integer ID_T_TVA) {
//		firePropertyChange("id", this.id, this.id = ID_T_TVA);
//	}
//
//	public String getCodeFichier() {
//		return this.codeFichier;
//	}
//
//	public void setCodeFichier(String CODE_T_TVA) {
//		firePropertyChange("codeFichier", this.codeFichier, this.codeFichier = CODE_T_TVA);
//	}
//
//	public String getLibFichier() {
//		return this.libFichier;
//	}
//
//	public void setLibFichier(String LIB_T_TVA) {
//		firePropertyChange("libFichier", this.libFichier, this.libFichier = LIB_T_TVA);
//	}
	
	

}
