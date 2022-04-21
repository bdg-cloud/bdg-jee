package fr.legrain.import_agrigest.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name ="ParametreImport")
public class ParametreImport implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 2380754002968797881L;

	
	private String cheminAccesBase;
	private String cheminAccesBasePlanType;
	private String cheminFichierExport;
	private String dossier;
//	private Boolean tvaAEncaissement;
	
	
	
	
	public String getCheminAccesBase() {
		return cheminAccesBase;
	}
	public void setCheminAccesBase(String cheminAccesBase) {
		this.cheminAccesBase = cheminAccesBase;
	}
	public String getCheminAccesBasePlanType() {
		return cheminAccesBasePlanType;
	}
	public void setCheminAccesBasePlanType(String cheminAccesBasePlanType) {
		this.cheminAccesBasePlanType = cheminAccesBasePlanType;
	}
	public String getCheminFichierExport() {
		return cheminFichierExport;
	}
	public void setCheminFichierExport(String cheminFichierExport) {
		this.cheminFichierExport = cheminFichierExport;
	}
	public String getDossier() {
		return dossier;
	}
	public void setDossier(String dossier) {
		this.dossier = dossier;
	}
//	public Boolean getTvaAEncaissement() {
//		return tvaAEncaissement;
//	}
//	public void setTvaAEncaissement(Boolean tvaAEncaissement) {
//		this.tvaAEncaissement = tvaAEncaissement;
//	}
	

	
	
	
	
	
	
}
