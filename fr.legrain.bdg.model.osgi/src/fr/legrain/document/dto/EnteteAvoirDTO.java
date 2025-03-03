package fr.legrain.document.dto;

// Generated 20 mars 2007 09:51:19 by Hibernate Tools 3.2.0.b9

import java.math.BigDecimal;
import java.util.Date;

import fr.legrain.lib.data.LgrConstantes;
import fr.legrain.lib.data.LibDate;
import fr.legrain.lib.data.ModelObject;

/**
 * IHMEnteteAvoir generated by hbm2java
 */
public class EnteteAvoirDTO extends ModelObject  implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8292199184473779181L;
	private Integer id=0;
	private String codeDocument=LgrConstantes.C_STR_VIDE;
	private Date dateDocument=new Date();
	private Date dateEchDocument=new Date();;
	private Date dateLivDocument=new Date();;
	private String libelleDocument=LgrConstantes.C_STR_VIDE;
	private Integer idAdresse=0;
	private Integer idAdresseLiv=0;
	private Integer idTiers=0;
	private String codeTiers=LgrConstantes.C_STR_VIDE;
	private String nomTiers=LgrConstantes.C_STR_VIDE;
	private String prenomTiers=LgrConstantes.C_STR_VIDE;
	private String surnomTiers=LgrConstantes.C_STR_VIDE;
	private String codeCompta=LgrConstantes.C_STR_VIDE;
	private String compte=LgrConstantes.C_STR_VIDE;
	private Integer idTPaiement=0;
	private String codeTPaiement=LgrConstantes.C_STR_VIDE;
	private Integer idCPaiement=0;
	private BigDecimal regleDocument=new BigDecimal(0);
	private BigDecimal remHtDocument=new BigDecimal(0);
	private BigDecimal txRemHtDocument=new BigDecimal(0);
	private BigDecimal remTtcDocument=new BigDecimal(0);
	private BigDecimal txRemTtcDocument=new BigDecimal(0);
	private Integer nbEDocument=0;
	private BigDecimal mtTtcCalc=new BigDecimal(0);
	private BigDecimal mtHtCalc=new BigDecimal(0);
	private BigDecimal mtTvaCalc=new BigDecimal(0);
	private BigDecimal netTtcCalc=new BigDecimal(0);
	private BigDecimal netHtCalc=new BigDecimal(0);
	private BigDecimal netTvaCalc=new BigDecimal(0);
	private String IP_ACCES=LgrConstantes.C_STR_VIDE;
	private Boolean ttc=false;
	private Boolean export=false;
	private String commentaire = "";

	public EnteteAvoirDTO() {
	}

	public EnteteAvoirDTO(Integer ID_AVOIR, String CODE_AVOIR,
			Date DATE_AVOIR, Date DATE_ECH_AVOIR, Date DATE_LIV_AVOIR,
			String LIBELLE_AVOIR, Integer ID_ADRESSE, Integer ID_ADRESSE_LIV,
			Integer ID_TIERS, String CODE_TIERS, String NOM_TIERS,
			String PRENOM_TIERS, String SURNOM_TIERS, String CODE_COMPTA,
			String COMPTE, Integer ID_T_PAIEMENT, String CODE_T_PAIEMENT,
			Integer ID_C_PAIEMENT, BigDecimal REGLE_AVOIR,
			BigDecimal REM_HT_AVOIR, BigDecimal TX_REM_HT_AVOIR,
			BigDecimal REM_TTC_AVOIR, BigDecimal TX_REM_TTC_AVOIR,
			Integer NB_E_AVOIR, BigDecimal MT_TTC_CALC, BigDecimal MT_HT_CALC,
			BigDecimal MT_TVA_CALC, BigDecimal NET_TTC_CALC, BigDecimal NET_HT_CALC,
			BigDecimal NET_TVA_CALC, String IP_ACCES, Boolean TTC, Boolean EXPORT,String COMMENTAIRE) {
		this.id = ID_AVOIR;
		this.codeDocument = CODE_AVOIR;
		this.dateDocument = DATE_AVOIR;
		this.dateEchDocument = DATE_ECH_AVOIR;
		this.dateLivDocument = DATE_LIV_AVOIR;
		this.libelleDocument = LIBELLE_AVOIR;
		this.idAdresse = ID_ADRESSE;
		this.idAdresseLiv = ID_ADRESSE_LIV;
		this.idTiers = ID_TIERS;
		this.codeTiers = CODE_TIERS;
		this.nomTiers = NOM_TIERS;
		this.prenomTiers = PRENOM_TIERS;
		this.surnomTiers = SURNOM_TIERS;
		this.codeCompta = CODE_COMPTA;
		this.compte = COMPTE;
		this.idTPaiement = ID_T_PAIEMENT;
		this.codeTPaiement = CODE_T_PAIEMENT;
		this.idCPaiement = ID_C_PAIEMENT;
		this.regleDocument = REGLE_AVOIR;
		this.remHtDocument = REM_HT_AVOIR;
		this.txRemHtDocument = TX_REM_HT_AVOIR;
		this.remTtcDocument = REM_TTC_AVOIR;
		this.txRemTtcDocument = TX_REM_TTC_AVOIR;
		this.nbEDocument = NB_E_AVOIR;
		this.mtTtcCalc = MT_TTC_CALC;
		this.mtHtCalc = MT_HT_CALC;
		this.mtTvaCalc = MT_TVA_CALC;
		this.netTtcCalc = NET_TTC_CALC;
		this.netHtCalc = NET_HT_CALC;
		this.netTvaCalc = NET_TVA_CALC;
		this.IP_ACCES = IP_ACCES;
		this.ttc = TTC;
		this.export = EXPORT;
		this.commentaire = COMMENTAIRE;
	}

	public void setIHMEnteteAvoir(EnteteAvoirDTO ihmEnteteAvoir){
		setIdDocument(ihmEnteteAvoir.id);
		setCodeDocument(ihmEnteteAvoir.codeDocument);
		setDateDocument(ihmEnteteAvoir.dateDocument);
		setDateEchDocument(ihmEnteteAvoir.dateEchDocument);
		setDateLivDocument(ihmEnteteAvoir.dateLivDocument);
		setLibelleDocument(ihmEnteteAvoir.libelleDocument);
		setIdAdresse(ihmEnteteAvoir.idAdresse);
		setIdAdresseLiv(ihmEnteteAvoir.idAdresseLiv);
		setIdTiers(ihmEnteteAvoir.idTiers);
		setCodeTiers(ihmEnteteAvoir.codeTiers);
		setNomTiers(ihmEnteteAvoir.nomTiers);
		setPrenomTiers(ihmEnteteAvoir.prenomTiers);
		setSurnomTiers(ihmEnteteAvoir.surnomTiers);
		setCodeCompta(ihmEnteteAvoir.codeCompta);
		setCompte(ihmEnteteAvoir.compte);
		setIdTPaiement(ihmEnteteAvoir.idTPaiement);
		setCodeTPaiement(ihmEnteteAvoir.codeTPaiement);
		setIdCPaiement(ihmEnteteAvoir.idCPaiement);
		setRegleDocument(ihmEnteteAvoir.regleDocument);
		setRemHtDocument(ihmEnteteAvoir.remHtDocument);
		setTxRemHtDocument(ihmEnteteAvoir.txRemHtDocument);
		setRemTtcDocument(ihmEnteteAvoir.remTtcDocument);
		setTxRemTtcDocument(ihmEnteteAvoir.txRemTtcDocument);
		setNbEDocument(ihmEnteteAvoir.nbEDocument);
		setMtTtcCalc(ihmEnteteAvoir.mtTtcCalc);
		setMtHtCalc(ihmEnteteAvoir.mtHtCalc);
		setMtTvaCalc(ihmEnteteAvoir.mtTvaCalc);
		setNetTtcCalc(ihmEnteteAvoir.netTtcCalc);
		setNetHtCalc(ihmEnteteAvoir.netHtCalc);
		setNetTvaCalc(ihmEnteteAvoir.netTvaCalc);
		setIP_ACCES(ihmEnteteAvoir.IP_ACCES);
		setTtc(ihmEnteteAvoir.ttc);
		setExport(ihmEnteteAvoir.export);
		setCommentaire(ihmEnteteAvoir.commentaire);
	}
	
//	public void setIHMEnteteAvoir(SWTEnteteAvoir swtEnteteAvoir){
//		System.out.println("mise à jour ihm par SWTEnteteAvoir");
//		setCodeAvoir(swtEnteteAvoir.getCODE());
//		System.out.println("Code facture swtEnteteAvoir : "+swtEnteteAvoir.getCODE());
//		setDateAvoir(swtEnteteAvoir.getDATE());
//		setDateEchAvoir(swtEnteteAvoir.getDATE_ECH_AVOIR());
//		setDateLivAvoir(swtEnteteAvoir.getDATE_LIV_AVOIR());
//		setLibelleAvoir(swtEnteteAvoir.getLIBELLE());
//		setIdAdresse(swtEnteteAvoir.getID_ADRESSE());
//		setIdAdresseLiv(swtEnteteAvoir.getID_ADRESSE_LIV());
//		setIdTiers(swtEnteteAvoir.getID_TIERS());
//		setCodeTiers(swtEnteteAvoir.getCODE_TIERS());
//		setIdTPaiement(swtEnteteAvoir.getID_T_PAIEMENT());
//		setCodeTPaiement(swtEnteteAvoir.getCODE_T_PAIEMENT());
//		setIdCPaiement(swtEnteteAvoir.getID_C_PAIEMENT());
//		setRegleAvoir(swtEnteteAvoir.getREGLE_AVOIR());
//		setRemHtAvoir(swtEnteteAvoir.getREM_HT_AVOIR());
//		setTxRemHtAvoir(swtEnteteAvoir.getTX_REM_HT_AVOIR());
//		setRemTtcAvoir(swtEnteteAvoir.getREM_TTC_AVOIR());
//		setTxRemTtcAvoir(swtEnteteAvoir.getTX_REM_TTC_AVOIR());
//		setNbEAvoir(swtEnteteAvoir.getNB_E_AVOIR());
//		setMtTtcCalc(swtEnteteAvoir.getMT_TTC_CALC());
//		setMtHtCalc(swtEnteteAvoir.getMT_HT_CALC());
//		setMtTvaCalc(swtEnteteAvoir.getMT_TVA_CALC());
//		setTtc(swtEnteteAvoir.getTTC());
//		setExport(swtEnteteAvoir.getEXPORT());
//		setNetHtCalc(swtEnteteAvoir.getNET_HT_CALC());
//		setNetTvaCalc(swtEnteteAvoir.getNET_TVA_CALC());
//		setNetTtcCalc(swtEnteteAvoir.getNET_TTC_CALC());
//		setCommentaire(swtEnteteAvoir.getCOMMENTAIRE());
//	}
	
	public static EnteteAvoirDTO copy(EnteteAvoirDTO ihmEnteteAvoir){
		EnteteAvoirDTO ihmEnteteAvoirLoc = new EnteteAvoirDTO();
		ihmEnteteAvoirLoc.setIdDocument(ihmEnteteAvoir.id);
		ihmEnteteAvoirLoc.setCodeDocument(ihmEnteteAvoir.codeDocument);
		ihmEnteteAvoirLoc.setDateDocument(ihmEnteteAvoir.dateDocument);
		ihmEnteteAvoirLoc.setDateEchDocument(ihmEnteteAvoir.dateEchDocument);
		ihmEnteteAvoirLoc.setDateLivDocument(ihmEnteteAvoir.dateLivDocument);
		ihmEnteteAvoirLoc.setLibelleDocument(ihmEnteteAvoir.libelleDocument);
		ihmEnteteAvoirLoc.setIdAdresse(ihmEnteteAvoir.idAdresse);
		ihmEnteteAvoirLoc.setIdAdresseLiv(ihmEnteteAvoir.idAdresseLiv);
		ihmEnteteAvoirLoc.setIdTiers(ihmEnteteAvoir.idTiers);
		ihmEnteteAvoirLoc.setCodeTiers(ihmEnteteAvoir.codeTiers);
		ihmEnteteAvoirLoc.setNomTiers(ihmEnteteAvoir.nomTiers);
		ihmEnteteAvoirLoc.setPrenomTiers(ihmEnteteAvoir.prenomTiers);
		ihmEnteteAvoirLoc.setSurnomTiers(ihmEnteteAvoir.surnomTiers);
		ihmEnteteAvoirLoc.setCodeCompta(ihmEnteteAvoir.codeCompta);
		ihmEnteteAvoirLoc.setCompte(ihmEnteteAvoir.compte);
		ihmEnteteAvoirLoc.setIdTPaiement(ihmEnteteAvoir.idTPaiement);
		ihmEnteteAvoirLoc.setCodeTPaiement(ihmEnteteAvoir.codeTPaiement);
		ihmEnteteAvoirLoc.setIdCPaiement(ihmEnteteAvoir.idCPaiement);
		ihmEnteteAvoirLoc.setRegleDocument(ihmEnteteAvoir.regleDocument);
		ihmEnteteAvoirLoc.setRemHtDocument(ihmEnteteAvoir.remHtDocument);
		ihmEnteteAvoirLoc.setTxRemHtDocument(ihmEnteteAvoir.txRemHtDocument);
		ihmEnteteAvoirLoc.setRemTtcDocument(ihmEnteteAvoir.remTtcDocument);
		ihmEnteteAvoirLoc.setTxRemTtcDocument(ihmEnteteAvoir.txRemTtcDocument);
		ihmEnteteAvoirLoc.setNbEDocument(ihmEnteteAvoir.nbEDocument);
		ihmEnteteAvoirLoc.setMtTtcCalc(ihmEnteteAvoir.mtTtcCalc);
		ihmEnteteAvoirLoc.setMtHtCalc(ihmEnteteAvoir.mtHtCalc);
		ihmEnteteAvoirLoc.setMtTvaCalc(ihmEnteteAvoir.mtTvaCalc);
		ihmEnteteAvoirLoc.setNetTtcCalc(ihmEnteteAvoir.netTtcCalc);
		ihmEnteteAvoirLoc.setNetHtCalc(ihmEnteteAvoir.netHtCalc);
		ihmEnteteAvoirLoc.setNetTvaCalc(ihmEnteteAvoir.netTvaCalc);
		ihmEnteteAvoirLoc.setIP_ACCES(ihmEnteteAvoir.IP_ACCES);
		ihmEnteteAvoirLoc.setTtc(ihmEnteteAvoir.ttc);
		ihmEnteteAvoirLoc.setExport(ihmEnteteAvoir.export);
		ihmEnteteAvoirLoc.setCommentaire(ihmEnteteAvoir.commentaire);
		return ihmEnteteAvoirLoc;
	}
	
	public boolean factureEstVide(){
		Date dateExemple = new Date();
//		if(!getCodeAvoir().equals(LgrConstantes.C_STR_VIDE))return true;
		if(!(LibDate.compareTo(getDateDocument(), dateExemple)==0))return false;
		if(!(LibDate.compareTo(getDateEchDocument(), dateExemple)==0))return false;
		if(!(LibDate.compareTo(getDateLivDocument(), dateExemple)==0))return false;
		
		if(!getLibelleDocument().equals(LgrConstantes.C_STR_VIDE))return false;
//		if(!getIdAdresse().equals(0))return false;
//		if(!getIdAdresseLiv().equals(0))return false;
		if(!getIdTiers().equals(0))return false;
		if(!getCodeTiers().equals(LgrConstantes.C_STR_VIDE))return false;
		if(!getNomTiers().equals(LgrConstantes.C_STR_VIDE))return false;
		if(!getPrenomTiers().equals(LgrConstantes.C_STR_VIDE))return false;
		if(!getSurnomTiers().equals(LgrConstantes.C_STR_VIDE))return false;
		if(!getCodeCompta().equals(LgrConstantes.C_STR_VIDE))return false;
		if(!getCompte().equals(LgrConstantes.C_STR_VIDE))return false;
//		if(!getIdTPaiement().equals(0))return false;
//		if(!getCodeTPaiement().equals(LgrConstantes.C_STR_VIDE))return false;
//		if(!getIdCPaiement().equals(0))return false;
//		if(!LibChaine.emptyNumeric(getRegleAvoir()))return false;
//		if(!LibChaine.emptyNumeric(getRemHtAvoir()))return false;
//		if(!LibChaine.emptyNumeric(getTxRemHtAvoir()))return false;
//		if(!LibChaine.emptyNumeric(getRemTtcAvoir()))return false;
//		if(!LibChaine.emptyNumeric(getTxRemTtcAvoir()))return false;
//		if(!getNbEAvoir().equals(0))return false;
//		if(!LibChaine.emptyNumeric(getMtTtcCalc()))return false;
//		if(!LibChaine.emptyNumeric(getMtHtCalc()))return false;
//		if(!LibChaine.emptyNumeric(getMtTvaCalc()))return false;
//		if(!LibChaine.emptyNumeric(getNetTtcCalc()))return false;
//		if(!LibChaine.emptyNumeric(getNetHtCalc()))return false;
//		if(!LibChaine.emptyNumeric(getNetTvaCalc()))return false;
//		if(!getIP_ACCES().equals(""))return false;
//		if(!getTtc().equals(false))return false;
//		if(!getExport().equals(false))return false;
//		if(!getCommentaire().equals(""))return false;
		return true;
	}
	
	public boolean factureEstVide(Date dateDansExercice){
		Date dateExemple = new Date();
//		if(!getCodeAvoir().equals(LgrConstantes.C_STR_VIDE))return true;
		if(!((LibDate.compareTo(getDateDocument(), dateDansExercice)==0) || 
				(LibDate.compareTo(getDateDocument(), dateExemple)==0)))return false;
//		if(!((LibDate.compareTo(getDateEchDocument(), dateDansExercice)==0) || 
//				(LibDate.compareTo(getDateEchDocument(), dateExemple)==0)))return false;
//		if(!((LibDate.compareTo(getDateLivDocument(), dateDansExercice)==0) || 
//				(LibDate.compareTo(getDateLivDocument(), dateExemple)==0)))return false;
		
		if(getLibelleDocument()!=null && !getLibelleDocument().equals(LgrConstantes.C_STR_VIDE))return false;
//		if(!getIdAdresse().equals(0))return false;
//		if(!getIdAdresseLiv().equals(0))return false;
		if(getIdTiers()!=null &&!getIdTiers().equals(0))return false;
		if(getCodeTiers()!=null &&!getCodeTiers().equals(LgrConstantes.C_STR_VIDE))return false;
		if(getNomTiers()!=null &&!getNomTiers().equals(LgrConstantes.C_STR_VIDE))return false;
		if(getPrenomTiers()!=null &&!getPrenomTiers().equals(LgrConstantes.C_STR_VIDE))return false;
		if(getSurnomTiers()!=null &&!getSurnomTiers().equals(LgrConstantes.C_STR_VIDE))return false;
		if(getCodeCompta()!=null &&!getCodeCompta().equals(LgrConstantes.C_STR_VIDE))return false;
		if(getCompte()!=null &&!getCompte().equals(LgrConstantes.C_STR_VIDE))return false;
		//if(!getIdTPaiement().equals(0))return false;
//		if(!getCodeTPaiement().equals(LgrConstantes.C_STR_VIDE))return false;
		//if(!getIdCPaiement().equals(0))return false;
//		if(!LibChaine.emptyNumeric(getRegleAvoir()))return false;
//		if(!LibChaine.emptyNumeric(getRemHtAvoir()))return false;
//		if(!LibChaine.emptyNumeric(getTxRemHtAvoir()))return false;
//		if(!LibChaine.emptyNumeric(getRemTtcAvoir()))return false;
//		if(!LibChaine.emptyNumeric(getTxRemTtcAvoir()))return false;
//		if(!getNbEAvoir().equals(0))return false;
//		if(!LibChaine.emptyNumeric(getMtTtcCalc()))return false;
//		if(!LibChaine.emptyNumeric(getMtHtCalc()))return false;
//		if(!LibChaine.emptyNumeric(getMtTvaCalc()))return false;
//		if(!LibChaine.emptyNumeric(getNetTtcCalc()))return false;
//		if(!LibChaine.emptyNumeric(getNetHtCalc()))return false;
//		if(!LibChaine.emptyNumeric(getNetTvaCalc()))return false;
//		if(!getIP_ACCES().equals(""))return false;
//		if(!getTtc().equals(false))return false;
//		if(!getExport().equals(false))return false;
//		if(!getCommentaire().equals(""))return false;
		return true;
	}

	public Integer getIdDocument() {
		return this.id;
	}

	public void setIdDocument(Integer ID_AVOIR) {
		firePropertyChange("idDocument", this.id, this.id = ID_AVOIR);
	}

	public String getCodeDocument() {
		return this.codeDocument;
	}

	public void setCodeDocument(String CODE_AVOIR) {
		firePropertyChange("codeDocument", this.codeDocument, this.codeDocument = CODE_AVOIR);
	}

	public Date getDateDocument() {
		return this.dateDocument;
	}

	public void setDateDocument(Date DATE_AVOIR) {
		firePropertyChange("dateDocument", this.dateDocument, this.dateDocument = DATE_AVOIR);
	}

	public Date getDateEchDocument() {
		return this.dateEchDocument;
	}

	public void setDateEchDocument(Date DATE_ECH_AVOIR) {
		firePropertyChange("dateEchDocument", this.dateEchDocument, this.dateEchDocument = DATE_ECH_AVOIR);
	}
	public Date getDateLivDocument() {
		return dateLivDocument;
	}

	public void setDateLivDocument(Date dateLivDocument) {
		firePropertyChange("dateLivDocument", this.dateLivDocument, this.dateLivDocument = dateLivDocument);
	}
	public String getLibelleDocument() {
		return this.libelleDocument;
	}

	public void setLibelleDocument(String LIBELLE_AVOIR) {
		firePropertyChange("libelleDocument", this.libelleDocument, this.libelleDocument = LIBELLE_AVOIR);
	}

	public Integer getIdAdresse() {
		return this.idAdresse;
	}

	public void setIdAdresse(Integer ID_ADRESSE) {
		firePropertyChange("idAdresse", this.idAdresse, this.idAdresse = ID_ADRESSE);
	}

	public Integer getIdAdresseLiv() {
		return this.idAdresseLiv;
	}

	public void setIdAdresseLiv(Integer ID_ADRESSE_LIV) {
		firePropertyChange("idAdresseLiv", this.idAdresseLiv, this.idAdresseLiv = ID_ADRESSE_LIV);
	}

	public Integer getIdTiers() {
		return this.idTiers;
	}

	public void setIdTiers(Integer ID_TIERS) {
		firePropertyChange("idTiers", this.idTiers, this.idTiers = ID_TIERS);
	}

	public String getCodeTiers() {
		return this.codeTiers;
	}

	public void setCodeTiers(String CODE_TIERS) {
		firePropertyChange("codeTiers", this.codeTiers, this.codeTiers = CODE_TIERS);
	}

	public String getNomTiers() {
		return this.nomTiers;
	}

	public void setNomTiers(String NOM_TIERS) {
		firePropertyChange("nomTiers", this.nomTiers, this.nomTiers = NOM_TIERS);
	}

	public String getPrenomTiers() {
		return this.prenomTiers;
	}

	public void setPrenomTiers(String PRENOM_TIERS) {
		firePropertyChange("prenomTiers", this.prenomTiers, this.prenomTiers = PRENOM_TIERS);
	}

	public String getSurnomTiers() {
		return this.surnomTiers;
	}

	public void setSurnomTiers(String SURNOM_TIERS) {
		firePropertyChange("surnomTiers", this.surnomTiers, this.surnomTiers = SURNOM_TIERS);
	}

	public String getCodeCompta() {
		return this.codeCompta;
	}

	public void setCodeCompta(String CODE_COMPTA) {
		firePropertyChange("codeCompta", this.codeCompta, this.codeCompta = CODE_COMPTA);
	}

	public String getCompte() {
		return this.compte;
	}

	public void setCompte(String COMPTE) {
		firePropertyChange("compte", this.compte, this.compte = COMPTE);
	}

	public Integer getIdTPaiement() {
		return this.idTPaiement;
	}

	public void setIdTPaiement(Integer ID_T_PAIEMENT) {
		firePropertyChange("idTPaiement", this.idTPaiement, this.idTPaiement = ID_T_PAIEMENT);
	}

	public String getCodeTPaiement() {
		return this.codeTPaiement;
	}

	public void setCodeTPaiement(String CODE_T_PAIEMENT) {
		firePropertyChange("codeTPaiement", this.codeTPaiement, this.codeTPaiement = CODE_T_PAIEMENT);
	}

	public Integer getIdCPaiement() {
		return this.idCPaiement;
	}

	public void setIdCPaiement(Integer ID_C_PAIEMENT) {
		firePropertyChange("idCPaiement", this.idCPaiement, this.idCPaiement = ID_C_PAIEMENT);
	}

	public BigDecimal getRegleDocument() {
		return this.regleDocument;
	}

	public void setRegleDocument(BigDecimal REGLE_AVOIR) {
		firePropertyChange("regleDocument", this.regleDocument, this.regleDocument = REGLE_AVOIR);
	}

	public BigDecimal getRemHtDocument() {
		return this.remHtDocument;
	}

	public void setRemHtDocument(BigDecimal REM_HT_AVOIR) {
		firePropertyChange("remHtDocument", this.remHtDocument, this.remHtDocument = REM_HT_AVOIR);
	}

	public BigDecimal getTxRemHtDocument() {
		return this.txRemHtDocument;
	}

	public void setTxRemHtDocument(BigDecimal TX_REM_HT_AVOIR) {
		firePropertyChange("txRemHtDocument", this.txRemHtDocument, this.txRemHtDocument = TX_REM_HT_AVOIR);
	}

	public BigDecimal getRemTtcDocument() {
		return this.remTtcDocument;
	}

	public void setRemTtcDocument(BigDecimal REM_TTC_AVOIR) {
		firePropertyChange("remTtcDocument", this.remTtcDocument, this.remTtcDocument = REM_TTC_AVOIR);
	}

	public BigDecimal getTxRemTtcDocument() {
		return this.txRemTtcDocument;
	}

	public void setTxRemTtcDocument(BigDecimal TX_REM_TTC_AVOIR) {
		firePropertyChange("txRemTtcDocument", this.txRemTtcDocument, this.txRemTtcDocument = TX_REM_TTC_AVOIR);
	}

	public Integer getNbEDocument() {
		return this.nbEDocument;
	}

	public void setNbEDocument(Integer NB_E_AVOIR) {
		firePropertyChange("nbEDocument", this.nbEDocument, this.nbEDocument = NB_E_AVOIR);
	}

	public BigDecimal getMtTtcCalc() {
		return this.mtTtcCalc;
	}

	public void setMtTtcCalc(BigDecimal MT_TTC_CALC) {
		firePropertyChange("mtTtcCalc", this.mtTtcCalc, this.mtTtcCalc = MT_TTC_CALC);
	}

	public BigDecimal getMtHtCalc() {
		return this.mtHtCalc;
	}

	public void setMtHtCalc(BigDecimal MT_HT_CALC) {
		firePropertyChange("mtHtCalc", this.mtHtCalc, this.mtHtCalc = MT_HT_CALC);
	}

	public BigDecimal getMtTvaCalc() {
		return this.mtTvaCalc;
	}

	public void setMtTvaCalc(BigDecimal MT_TVA_CALC) {
		firePropertyChange("mtTvaCalc", this.mtTvaCalc, this.mtTvaCalc = MT_TVA_CALC);
	}

	public BigDecimal getNetTtcCalc() {
		return this.netTtcCalc;
	}

	public void setNetTtcCalc(BigDecimal NET_TTC_CALC) {
		firePropertyChange("netTtcCalc", this.netTtcCalc, this.netTtcCalc = NET_TTC_CALC);
	}

	public BigDecimal getNetHtCalc() {
		return this.netHtCalc;
	}

	public void setNetHtCalc(BigDecimal NET_HT_CALC) {
		firePropertyChange("netHtCalc", this.netHtCalc, this.netHtCalc = NET_HT_CALC);
	}

	public BigDecimal getNetTvaCalc() {
		return this.netTvaCalc;
	}

	public void setNetTvaCalc(BigDecimal NET_TVA_CALC) {
		firePropertyChange("netTvaCalc", this.netTvaCalc, this.netTvaCalc = NET_TVA_CALC);
	}

	public String getIP_ACCES() {
		return this.IP_ACCES;
	}

	public void setIP_ACCES(String IP_ACCES) {
		firePropertyChange("IP_ACCES", this.IP_ACCES, this.IP_ACCES = IP_ACCES);
	}

	public Boolean getTtc() {
		return this.ttc;
	}

	public void setTtc(Boolean TTC) {
		firePropertyChange("ttc", this.ttc, this.ttc = TTC);
	}

	public Boolean getExport() {
		return this.export;
	}

	public void setExport(Boolean EXPORT) {
		firePropertyChange("export", this.export, this.export = EXPORT);
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String COMMENTAIRE) {
		firePropertyChange("commentaire", this.commentaire, this.commentaire = COMMENTAIRE);
	}
	
	
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof EnteteAvoirDTO))
			return false;
		EnteteAvoirDTO castOther = (EnteteAvoirDTO) other;

		return ((this.getIdDocument() == castOther.getIdDocument()) || (this
				.getIdDocument() != null
				&& castOther.getIdDocument() != null && this.getIdDocument()
				.equals(castOther.getIdDocument())))
				&& ((this.getCodeDocument() == castOther.getCodeDocument()) || (this
						.getCodeDocument() != null
						&& castOther.getCodeDocument() != null && this
						.getCodeDocument().equals(castOther.getCodeDocument())))
				&& ((this.getDateDocument() == castOther.getDateDocument()) || (this
						.getDateDocument() != null
						&& castOther.getDateDocument() != null && this
						.getDateDocument().equals(castOther.getDateDocument())))
				&& ((this.getDateEchDocument() == castOther
						.getDateEchDocument()) || (this.getDateEchDocument() != null
						&& castOther.getDateEchDocument() != null && this
						.getDateEchDocument().equals(
								castOther.getDateEchDocument())))
				&& ((this.getDateLivDocument() == castOther
						.getDateLivDocument()) || (this.getDateLivDocument() != null
						&& castOther.getDateLivDocument() != null && this
						.getDateLivDocument().equals(
								castOther.getDateLivDocument())))
				&& ((this.getLibelleDocument() == castOther
						.getLibelleDocument()) || (this.getLibelleDocument() != null
						&& castOther.getLibelleDocument() != null && this
						.getLibelleDocument().equals(
								castOther.getLibelleDocument())))
				&& ((this.getIdAdresse() == castOther.getIdAdresse()) || (this
						.getIdAdresse() != null
						&& castOther.getIdAdresse() != null && this
						.getIdAdresse().equals(castOther.getIdAdresse())))
				&& ((this.getIdAdresseLiv() == castOther.getIdAdresseLiv()) || (this
						.getIdAdresseLiv() != null
						&& castOther.getIdAdresseLiv() != null && this
						.getIdAdresseLiv().equals(
								castOther.getIdAdresseLiv())))
				&& ((this.getIdTiers() == castOther.getIdTiers()) || (this
						.getIdTiers() != null
						&& castOther.getIdTiers() != null && this
						.getIdTiers().equals(castOther.getIdTiers())))
				&& ((this.getCodeTiers() == castOther.getCodeTiers()) || (this
						.getCodeTiers() != null
						&& castOther.getCodeTiers() != null && this
						.getCodeTiers().equals(castOther.getCodeTiers())))
				&& ((this.getNomTiers() == castOther.getNomTiers()) || (this
						.getNomTiers() != null
						&& castOther.getNomTiers() != null && this
						.getNomTiers().equals(castOther.getNomTiers())))
				&& ((this.getPrenomTiers() == castOther.getPrenomTiers()) || (this
						.getPrenomTiers() != null
						&& castOther.getPrenomTiers() != null && this
						.getPrenomTiers().equals(castOther.getPrenomTiers())))
				&& ((this.getSurnomTiers() == castOther.getSurnomTiers()) || (this
						.getSurnomTiers() != null
						&& castOther.getSurnomTiers() != null && this
						.getSurnomTiers().equals(castOther.getSurnomTiers())))
				&& ((this.getCodeCompta() == castOther.getCodeCompta()) || (this
						.getCodeCompta() != null
						&& castOther.getCodeCompta() != null && this
						.getCodeCompta().equals(castOther.getCodeCompta())))
				&& ((this.getCompte() == castOther.getCompte()) || (this
						.getCompte() != null
						&& castOther.getCompte() != null && this.getCompte()
						.equals(castOther.getCompte())))
				&& ((this.getIdTPaiement() == castOther.getIdTPaiement()) || (this
						.getIdTPaiement() != null
						&& castOther.getIdTPaiement() != null && this
						.getIdTPaiement()
						.equals(castOther.getIdTPaiement())))
				&& ((this.getCodeTPaiement() == castOther
						.getCodeTPaiement()) || (this.getCodeTPaiement() != null
						&& castOther.getCodeTPaiement() != null && this
						.getCodeTPaiement().equals(
								castOther.getCodeTPaiement())))
				&& ((this.getIdCPaiement() == castOther.getIdCPaiement()) || (this
						.getIdCPaiement() != null
						&& castOther.getIdCPaiement() != null && this
						.getIdCPaiement()
						.equals(castOther.getIdCPaiement())))
				&& ((this.getRegleDocument() == castOther.getRegleDocument()) || (this
						.getRegleDocument() != null
						&& castOther.getRegleDocument() != null && this
						.getRegleDocument()
						.equals(castOther.getRegleDocument())))
				&& ((this.getRemHtDocument() == castOther.getRemHtDocument()) || (this
						.getRemHtDocument() != null
						&& castOther.getRemHtDocument() != null && this
						.getRemHtDocument().equals(
								castOther.getRemHtDocument())))
				&& ((this.getTxRemHtDocument() == castOther
						.getTxRemHtDocument()) || (this
						.getTxRemHtDocument() != null
						&& castOther.getTxRemHtDocument() != null && this
						.getTxRemHtDocument().equals(
								castOther.getTxRemHtDocument())))
				&& ((this.getRemTtcDocument() == castOther
						.getRemTtcDocument()) || (this.getRemTtcDocument() != null
						&& castOther.getRemTtcDocument() != null && this
						.getRemTtcDocument().equals(
								castOther.getRemTtcDocument())))
				&& ((this.getTxRemTtcDocument() == castOther
						.getTxRemTtcDocument()) || (this
						.getTxRemTtcDocument() != null
						&& castOther.getTxRemTtcDocument() != null && this
						.getTxRemTtcDocument().equals(
								castOther.getTxRemTtcDocument())))
				&& ((this.getNbEDocument() == castOther.getNbEDocument()) || (this
						.getNbEDocument() != null
						&& castOther.getNbEDocument() != null && this
						.getNbEDocument().equals(castOther.getNbEDocument())))
				&& ((this.getMtTtcCalc() == castOther.getMtTtcCalc()) || (this
						.getMtTtcCalc() != null
						&& castOther.getMtTtcCalc() != null && this
						.getMtTtcCalc().equals(castOther.getMtTtcCalc())))
				&& ((this.getMtHtCalc() == castOther.getMtHtCalc()) || (this
						.getMtHtCalc() != null
						&& castOther.getMtHtCalc() != null && this
						.getMtHtCalc().equals(castOther.getMtHtCalc())))
				&& ((this.getMtTvaCalc() == castOther.getMtTvaCalc()) || (this
						.getMtTvaCalc() != null
						&& castOther.getMtTvaCalc() != null && this
						.getMtTvaCalc().equals(castOther.getMtTvaCalc())))
				&& ((this.getNetTtcCalc() == castOther.getNetTtcCalc()) || (this
						.getNetTtcCalc() != null
						&& castOther.getNetTtcCalc() != null && this
						.getNetTtcCalc().equals(castOther.getNetTtcCalc())))
				&& ((this.getNetHtCalc() == castOther.getNetHtCalc()) || (this
						.getNetHtCalc() != null
						&& castOther.getNetHtCalc() != null && this
						.getNetHtCalc().equals(castOther.getNetHtCalc())))
				&& ((this.getNetTvaCalc() == castOther.getNetTvaCalc()) || (this
						.getNetTvaCalc() != null
						&& castOther.getNetTvaCalc() != null && this
						.getNetTvaCalc().equals(castOther.getNetTvaCalc())))
				&& ((this.getIP_ACCES() == castOther.getIP_ACCES()) || (this
						.getIP_ACCES() != null
						&& castOther.getIP_ACCES() != null && this
						.getIP_ACCES().equals(castOther.getIP_ACCES())))
				&& ((this.getTtc() == castOther.getTtc()) || (this.getTtc() != null
						&& castOther.getTtc() != null && this.getTtc().equals(
						castOther.getTtc())))
				&& ((this.getCommentaire() == castOther.getCommentaire()) || (this.getCommentaire() != null
						&& castOther.getCommentaire() != null && this.getCommentaire().equals(
						castOther.getCommentaire())))						
				&& ((this.getExport() == castOther.getExport()) || (this
						.getExport() != null
						&& castOther.getExport() != null && this.getExport()
						.equals(castOther.getExport())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getIdDocument() == null ? 0 : this.getIdDocument()
						.hashCode());
		result = 37
				* result
				+ (getCodeDocument() == null ? 0 : this.getCodeDocument()
						.hashCode());
		result = 37
				* result
				+ (getDateDocument() == null ? 0 : this.getDateDocument()
						.hashCode());
		result = 37
				* result
				+ (getDateEchDocument() == null ? 0 : this
						.getDateEchDocument().hashCode());
		result = 37
				* result
				+ (getDateLivDocument() == null ? 0 : this
						.getDateLivDocument().hashCode());
		result = 37
				* result
				+ (getLibelleDocument() == null ? 0 : this.getLibelleDocument()
						.hashCode());
		result = 37
				* result
				+ (getIdAdresse() == null ? 0 : this.getIdAdresse()
						.hashCode());
		result = 37
				* result
				+ (getIdAdresseLiv() == null ? 0 : this.getIdAdresseLiv()
						.hashCode());
		result = 37 * result
				+ (getIdTiers() == null ? 0 : this.getIdTiers().hashCode());
		result = 37
				* result
				+ (getCodeTiers() == null ? 0 : this.getCodeTiers()
						.hashCode());
		result = 37 * result
				+ (getNomTiers() == null ? 0 : this.getNomTiers().hashCode());
		result = 37
				* result
				+ (getPrenomTiers() == null ? 0 : this.getPrenomTiers()
						.hashCode());
		result = 37
				* result
				+ (getSurnomTiers() == null ? 0 : this.getSurnomTiers()
						.hashCode());
		result = 37
				* result
				+ (getCodeCompta() == null ? 0 : this.getCodeCompta()
						.hashCode());
		result = 37 * result
				+ (getCompte() == null ? 0 : this.getCompte().hashCode());
		result = 37
				* result
				+ (getIdTPaiement() == null ? 0 : this.getIdTPaiement()
						.hashCode());
		result = 37
				* result
				+ (getCodeTPaiement() == null ? 0 : this.getCodeTPaiement()
						.hashCode());
		result = 37
				* result
				+ (getIdCPaiement() == null ? 0 : this.getIdCPaiement()
						.hashCode());
		result = 37
				* result
				+ (getRegleDocument() == null ? 0 : this.getRegleDocument()
						.hashCode());
		result = 37
				* result
				+ (getRemHtDocument() == null ? 0 : this.getRemHtDocument()
						.hashCode());
		result = 37
				* result
				+ (getTxRemHtDocument() == null ? 0 : this
						.getTxRemHtDocument().hashCode());
		result = 37
				* result
				+ (getRemTtcDocument() == null ? 0 : this.getRemTtcDocument()
						.hashCode());
		result = 37
				* result
				+ (getTxRemTtcDocument() == null ? 0 : this
						.getTxRemTtcDocument().hashCode());
		result = 37
				* result
				+ (getNbEDocument() == null ? 0 : this.getNbEDocument()
						.hashCode());
		result = 37
				* result
				+ (getMtTtcCalc() == null ? 0 : this.getMtTtcCalc()
						.hashCode());
		result = 37
				* result
				+ (getMtHtCalc() == null ? 0 : this.getMtHtCalc()
						.hashCode());
		result = 37
				* result
				+ (getMtTvaCalc() == null ? 0 : this.getMtTvaCalc()
						.hashCode());
		result = 37
				* result
				+ (getNetTtcCalc() == null ? 0 : this.getNetTtcCalc()
						.hashCode());
		result = 37
				* result
				+ (getNetHtCalc() == null ? 0 : this.getNetHtCalc()
						.hashCode());
		result = 37
				* result
				+ (getNetTvaCalc() == null ? 0 : this.getNetTvaCalc()
						.hashCode());
		result = 37 * result
				+ (getIP_ACCES() == null ? 0 : this.getIP_ACCES().hashCode());
		result = 37 * result
				+ (getTtc() == null ? 0 : this.getTtc().hashCode());
		result = 37 * result
				+ (getCommentaire() == null ? 0 : this.getCommentaire().hashCode());		
		result = 37 * result
				+ (getExport() == null ? 0 : this.getExport().hashCode());
		return result;
	}






	

}
