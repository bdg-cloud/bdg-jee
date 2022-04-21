package fr.legrain.bdg.webapp.documents;

import java.util.Date;

import fr.legrain.document.model.TaMiseADisposition;

public class Verrouillage  implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6301658456400387040L;
	
	
	private static String messageVerrouille="- Document verrouillé ";
	private static String messageExporte="- Document exporté ";
	private static String messageMad="- Document mis à disposition du client ";
	
	private static String messageVerrouilleAffectation="- Affectation verrouillée ";
	private static String messageExporteAffectation="- Affectation exportée ";
	private static String messageMadAffectation="- Affectation mis à disposition du client ";
	
	private String message="";
	private boolean verrouille;
	private boolean verrouillagePeriode;
	private boolean mad;
	private boolean exporte;
	
	
	
	
	


	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isMad() {
		return mad;
	}
	public void setMad(boolean mad) {
		this.mad = mad;
	}
	public boolean isExporte() {
		return exporte;
	}
	public void setExporte(boolean exporte) {
		this.exporte = exporte;
	}
	public boolean isVerrouillagePeriode() {
		return verrouillagePeriode;
	}
	public void setVerrouillagePeriode(boolean verrouillagePeriode) {
		this.verrouillagePeriode = verrouillagePeriode;
	}
	public boolean isVerrouille() {
		return verrouille;
	}
	public void setVerrouille(boolean verrouille) {
		this.verrouille = verrouille;
		message="";
		if(exporte)message+=messageExporte;
		if(verrouillagePeriode)message+=messageVerrouille;
		if(mad)message+=messageMad;
	}
	public void setVerrouilleAffectation(boolean verrouille) {
		this.verrouille = verrouille;
		message="";
		if(exporte)message+=messageExporteAffectation;
		if(verrouillagePeriode)message+=messageVerrouilleAffectation;
		if(mad)message+=messageMadAffectation;
	}
	
	public static Verrouillage determineVerrouillage(TaMiseADisposition mad,Date dateExport,Date dateVerrouillage) {
		Verrouillage verrou=new Verrouillage();
		if(mad!=null)verrou.setMad(mad.estMisADisposition());
		verrou.setExporte(dateExport!=null);
		verrou.setVerrouillagePeriode(dateVerrouillage!=null);
		verrou.setVerrouille(verrou.isMad()||verrou.isExporte()||verrou.isVerrouillagePeriode());
		return verrou;
	}
	
	public static Verrouillage determineVerrouillage(boolean estMisADisposition,Date dateExport,Date dateVerrouillage) {
		Verrouillage verrou=new Verrouillage();
		verrou.setMad(estMisADisposition);
		verrou.setExporte(dateExport!=null);
		verrou.setVerrouillagePeriode(dateVerrouillage!=null);
		verrou.setVerrouille(verrou.isMad()||verrou.isExporte()||verrou.isVerrouillagePeriode());
		return verrou;
	}
	
	
	public static Verrouillage determineVerrouillageAffectation(TaMiseADisposition mad,Date dateExport,Date dateVerrouillage) {
		Verrouillage verrou=new Verrouillage();
		if(mad!=null)verrou.setMad(mad.estMisADisposition());
		verrou.setExporte(dateExport!=null);
		verrou.setVerrouillagePeriode(dateVerrouillage!=null);
		verrou.setVerrouilleAffectation(verrou.isMad()||verrou.isExporte()||verrou.isVerrouillagePeriode());
		return verrou;
	}
	
	public static Verrouillage determineVerrouillageAffectation(boolean estMisADisposition,Date dateExport,Date dateVerrouillage) {
		Verrouillage verrou=new Verrouillage();
		verrou.setMad(estMisADisposition);
		verrou.setExporte(dateExport!=null);
		verrou.setVerrouillagePeriode(dateVerrouillage!=null);
		verrou.setVerrouilleAffectation(verrou.isMad()||verrou.isExporte()||verrou.isVerrouillagePeriode());
		return verrou;
	}
}
