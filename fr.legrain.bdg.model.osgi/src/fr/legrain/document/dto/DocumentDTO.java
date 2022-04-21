package fr.legrain.document.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.SqlResultSetMapping;

import fr.legrain.general.dto.AbstractDTO;
import fr.legrain.lib.data.LgrConstantes;
import fr.legrain.lib.data.LibDate;



//@SqlResultSetMapping(
//	    name = "TaFactureDTORegroupement",
//	    entities = {
//	        @EntityResult(
//	            entityClass = TaFactureDTO.class,
//	            fields = {
//	         @FieldResult(column = "code_Famille",name = "codeFamille"),
//	        @FieldResult(column = "id_Document",name = "idDocument"),
//	        @FieldResult(column = "code_Document",name="codeDocument"),
//	        @FieldResult(column = "date_Document",name="dateDocument"),
//	        @FieldResult(column = "libelle_Document",name="libelleDocument"),
//	        @FieldResult(column = "code_Tiers",name="codeTiers"),
//	        @FieldResult(column = "nom_Tiers",name="nomTiers"),
//	        @FieldResult(column = "date_Ech_Document",name="dateEchDocument"),
//	        @FieldResult(column = "date_Export",name="dateExport"),
//	        @FieldResult(column = "mt_Ht_Calc",name="mtHtCalc"),
//	        @FieldResult(column = "mt_Tva_Calc",name="mtTvaCalc"),
//	        @FieldResult(column = "mt_Ttc_Calc",name="mtTtcCalc"),
//	        @FieldResult(column = "affectationTotale",name="affectationTotale"),
//	        @FieldResult(column = "resteARegler",name="resteARegler"),
//	       })})




public class DocumentDTO extends AbstractDTO  implements IDocumentDTO,Serializable  {
	
	
	///// décrire chaque champ en constante	
	public static final String f_idDocument="id";
	public static final String f_codeDocument="codeDocument";
	public static final String f_dateDocument="dateDocument";
	public static final String f_dateEchDocument="dateEchDocument";
	public static final String f_dateLivDocument="dateLivDocument";
	public static final String f_libelleDocument="libelleDocument";
	public static final String f_codeTiers="codeTiers";
	public static final String f_nomTiers="nomTiers";
	public static final String f_prenomTiers="prenomTiers";
	public static final String f_codeCompta="codeCompta";
	public static final String f_compte="compte";
	public static final String f_codeTPaiement="codeTPaiement";	
	public static final String f_remHtDocument="remHtDocument";
	public static final String f_txRemHtDocument="txRemHtDocument";
	public static final String f_remTtcDocument="remTtcDocument";
	public static final String f_txRemTtcDocument="txRemTtcDocument";	
	public static final String f_mtTtcCalc="mtTtcCalc";
	public static final String f_mtHtCalc="mtHtCalc";
	public static final String f_mtTvaCalc="mtTvaCalc";
	public static final String f_netTtcCalc="netTtcCalc";
	public static final String f_netHtCalc="netHtCalc";
	public static final String f_netTvaCalc="netTvaCalc";
	public static final String f_codeTTvaDoc="codeTTvaDoc";
	public static final String f_regleCompletDocument="regleCompletDocument";
	public static final String f_reglementComplet="reglementComplet";
	public static final String f_resteAReglerComplet="resteAReglerComplet";
	public static final String f_typeDocument="typeDocument";
	public static final String f_codeEtat="codeEtat";
	public static final String f_dateExport="dateExport";
	public static final String f_estMiseADisposition="estMiseADisposition";
	public static final String f_dateVerrouillage="dateVerrouillage";
	public static final String f_nomEntreprise="nomEntreprise";
	public static final String f_ttc="ttc";
	public static final String f_commentaire="commentaire";
	
	public static final String f_mt_Ttc_Calc="mt_Ttc_Calc";
	public static final String f_mt_Ht_Calc="mt_Ht_Calc";
	public static final String f_mt_Tva_Calc="mt_Tva_Calc";
	public static final String f_net_Ttc_Calc="net_Ttc_Calc";
	public static final String f_net_Ht_Calc="net_Ht_Calc";
	public static final String f_net_Tva_Calc="net_Tva_Calc";
	
	public static final String f_identifiantEtat="identifiant";
	public static final String f_relancer="relancer";
			
	/////////////// Fin décrire chaque champ en constante pour requete dynamique
	
	
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
	private String ipAcces=LgrConstantes.C_STR_VIDE;
	private Boolean ttc=false;
//	private Boolean export=false;
	private String commentaire = "";
	private Boolean accepte=true;
	private String codeTTvaDoc=LgrConstantes.C_STR_VIDE;

	private Boolean regleCompletDocument=false;
	private BigDecimal reglementComplet=new BigDecimal(0);
	private BigDecimal resteAReglerComplet=new BigDecimal(0);
	private String typeDocument;
	private String codeEtat;
	private String liblEtat;
	private boolean estMiseADisposition;
	private Date dateExport;
	private Date dateVerrouillage;
	private String nomEntreprise=LgrConstantes.C_STR_VIDE;
	private String codeFamille=LgrConstantes.C_STR_VIDE;
	private String identifiant = LgrConstantes.C_STR_VIDE;
	private Boolean relancer = false;

	public DocumentDTO() {
		initMapDTO();
	}



	public void setIHMEnteteBonliv(DocumentDTO ihmEnteteBonliv){
		setId(ihmEnteteBonliv.id);
		setCodeDocument(ihmEnteteBonliv.codeDocument);
		setDateDocument(ihmEnteteBonliv.dateDocument);
//		setDateEchDocument(ihmEnteteBonliv.dateEchDocument);
		setDateLivDocument(ihmEnteteBonliv.dateLivDocument);
		setLibelleDocument(ihmEnteteBonliv.libelleDocument);
		setIdAdresse(ihmEnteteBonliv.idAdresse);
		setIdAdresseLiv(ihmEnteteBonliv.idAdresseLiv);
		setIdTiers(ihmEnteteBonliv.idTiers);
		setCodeTiers(ihmEnteteBonliv.codeTiers);
		setNomTiers(ihmEnteteBonliv.nomTiers);
		setPrenomTiers(ihmEnteteBonliv.prenomTiers);
		setSurnomTiers(ihmEnteteBonliv.surnomTiers);
		setCodeCompta(ihmEnteteBonliv.codeCompta);
		setCompte(ihmEnteteBonliv.compte);
		setIdTPaiement(ihmEnteteBonliv.idTPaiement);
		setCodeTPaiement(ihmEnteteBonliv.codeTPaiement);
		setIdCPaiement(ihmEnteteBonliv.idCPaiement);
		//setREGLE_BONLIV(ihmEnteteBonliv.REGLE_BONLIV);
		setRemHtDocument(ihmEnteteBonliv.remHtDocument);
		setTxRemHtDocument(ihmEnteteBonliv.txRemHtDocument);
		setRemTtcDocument(ihmEnteteBonliv.remTtcDocument);
		setTxRemTtcDocument(ihmEnteteBonliv.txRemTtcDocument);
		setNbEDocument(ihmEnteteBonliv.nbEDocument);
		setMtTtcCalc(ihmEnteteBonliv.mtTtcCalc);
		setMtHtCalc(ihmEnteteBonliv.mtHtCalc);
		setMtTvaCalc(ihmEnteteBonliv.mtTvaCalc);
		setNetTtcCalc(ihmEnteteBonliv.netTtcCalc);
		setNetHtCalc(ihmEnteteBonliv.netHtCalc);
		setNetTvaCalc(ihmEnteteBonliv.netTvaCalc);
		setIpAcces(ihmEnteteBonliv.ipAcces);
		setTtc(ihmEnteteBonliv.ttc);
		setDateExport(ihmEnteteBonliv.dateExport);
		setCommentaire(ihmEnteteBonliv.commentaire);
	}
	
	public static DocumentDTO copy(DocumentDTO ihmEnteteBonliv){
		DocumentDTO ihmEnteteBonlivLoc = new DocumentDTO();
		ihmEnteteBonlivLoc.setId(ihmEnteteBonliv.id);
		ihmEnteteBonlivLoc.setCodeDocument(ihmEnteteBonliv.codeDocument);
		ihmEnteteBonlivLoc.setDateDocument(ihmEnteteBonliv.dateDocument);
//		ihmEnteteBonlivLoc.setDateEchDocument(ihmEnteteBonliv.dateEchDocument);
		ihmEnteteBonlivLoc.setDateLivDocument(ihmEnteteBonliv.dateLivDocument);
		ihmEnteteBonlivLoc.setLibelleDocument(ihmEnteteBonliv.libelleDocument);
		ihmEnteteBonlivLoc.setIdAdresse(ihmEnteteBonliv.idAdresse);
		ihmEnteteBonlivLoc.setIdAdresseLiv(ihmEnteteBonliv.idAdresseLiv);
		ihmEnteteBonlivLoc.setIdTiers(ihmEnteteBonliv.idTiers);
		ihmEnteteBonlivLoc.setCodeTiers(ihmEnteteBonliv.codeTiers);
		ihmEnteteBonlivLoc.setNomTiers(ihmEnteteBonliv.nomTiers);
		ihmEnteteBonlivLoc.setPrenomTiers(ihmEnteteBonliv.prenomTiers);
		ihmEnteteBonlivLoc.setSurnomTiers(ihmEnteteBonliv.surnomTiers);
		ihmEnteteBonlivLoc.setCodeCompta(ihmEnteteBonliv.codeCompta);
		ihmEnteteBonlivLoc.setCompte(ihmEnteteBonliv.compte);
		ihmEnteteBonlivLoc.setIdTPaiement(ihmEnteteBonliv.idTPaiement);
		ihmEnteteBonlivLoc.setCodeTPaiement(ihmEnteteBonliv.codeTPaiement);
		ihmEnteteBonlivLoc.setIdCPaiement(ihmEnteteBonliv.idCPaiement);
		//ihmEnteteBonlivLoc.setREGLE_BONLIV(ihmEnteteBonliv.REGLE_BONLIV);
		ihmEnteteBonlivLoc.setRemHtDocument(ihmEnteteBonliv.remHtDocument);
		ihmEnteteBonlivLoc.setTxRemHtDocument(ihmEnteteBonliv.txRemHtDocument);
		ihmEnteteBonlivLoc.setRemTtcDocument(ihmEnteteBonliv.remTtcDocument);
		ihmEnteteBonlivLoc.setTxRemTtcDocument(ihmEnteteBonliv.txRemTtcDocument);
		ihmEnteteBonlivLoc.setNbEDocument(ihmEnteteBonliv.nbEDocument);
		ihmEnteteBonlivLoc.setMtTtcCalc(ihmEnteteBonliv.mtTtcCalc);
		ihmEnteteBonlivLoc.setMtHtCalc(ihmEnteteBonliv.mtHtCalc);
		ihmEnteteBonlivLoc.setMtTvaCalc(ihmEnteteBonliv.mtTvaCalc);
		ihmEnteteBonlivLoc.setNetTtcCalc(ihmEnteteBonliv.netTtcCalc);
		ihmEnteteBonlivLoc.setNetHtCalc(ihmEnteteBonliv.netHtCalc);
		ihmEnteteBonlivLoc.setNetTvaCalc(ihmEnteteBonliv.netTvaCalc);
		ihmEnteteBonlivLoc.setIpAcces(ihmEnteteBonliv.ipAcces);
		ihmEnteteBonlivLoc.setTtc(ihmEnteteBonliv.ttc);
		ihmEnteteBonlivLoc.setDateExport(ihmEnteteBonliv.dateExport);
		ihmEnteteBonlivLoc.setCommentaire(ihmEnteteBonliv.commentaire);
		return ihmEnteteBonlivLoc;
	}
	
	public boolean BonlivEstVide(){
		Date dateExemple = new Date();
//		if(!getCODE_BONLIV().equals(LgrConstantes.C_STR_VIDE))return true;
		if(!(LibDate.compareTo(getDateDocument(), dateExemple)==0))return false;
//		if(!(LibDate.compareTo(getDateEchDocument(), dateExemple)==0))return false;
		if(!(LibDate.compareTo(getDateLivDocument(), dateExemple)==0))return false;
		
		if(!getLibelleDocument().equals(LgrConstantes.C_STR_VIDE))return false;
//		if(!getID_ADRESSE().equals(0))return false;
//		if(!getID_ADRESSE_LIV().equals(0))return false;
		if(!getIdTiers().equals(0))return false;
		if(!getCodeTiers().equals(LgrConstantes.C_STR_VIDE))return false;
		if(!getNomTiers().equals(LgrConstantes.C_STR_VIDE))return false;
		if(!getPrenomTiers().equals(LgrConstantes.C_STR_VIDE))return false;
		if(!getSurnomTiers().equals(LgrConstantes.C_STR_VIDE))return false;
		if(!getCodeCompta().equals(LgrConstantes.C_STR_VIDE))return false;
		if(!getCompte().equals(LgrConstantes.C_STR_VIDE))return false;
//		if(!getID_T_PAIEMENT().equals(0))return false;
//		if(!getCODE_T_PAIEMENT().equals(LgrConstantes.C_STR_VIDE))return false;
//		if(!getID_C_PAIEMENT().equals(0))return false;
//		//if(!LibChaine.emptyNumeric(getREGLE_BONLIV()))return false;
//		if(!LibChaine.emptyNumeric(getREM_HT_BONLIV()))return false;
//		if(!LibChaine.emptyNumeric(getTX_REM_HT_BONLIV()))return false;
//		if(!LibChaine.emptyNumeric(getREM_TTC_BONLIV()))return false;
//		if(!LibChaine.emptyNumeric(getTX_REM_TTC_BONLIV()))return false;
//		if(!getNB_E_BONLIV().equals(0))return false;
//		if(!LibChaine.emptyNumeric(getMT_TTC_CALC()))return false;
//		if(!LibChaine.emptyNumeric(getMT_HT_CALC()))return false;
//		if(!LibChaine.emptyNumeric(getMT_TVA_CALC()))return false;
//		if(!LibChaine.emptyNumeric(getNET_TTC_CALC()))return false;
//		if(!LibChaine.emptyNumeric(getNET_HT_CALC()))return false;
//		if(!LibChaine.emptyNumeric(getNET_TVA_CALC()))return false;
//		if(!getIP_ACCES().equals(""))return false;
//		if(!getTTC().equals(false))return false;
//		if(!getEXPORT().equals(false))return false;
		return true;
	}
	
	public boolean BonlivEstVide(Date dateDansExercice){
		Date dateExemple = new Date();
//		if(!getCODE_BONLIV().equals(LgrConstantes.C_STR_VIDE))return true;
		if(!((LibDate.compareTo(getDateDocument(), dateDansExercice)==0) || 
				(LibDate.compareTo(getDateDocument(), dateExemple)==0)))return false;
//		if(!((LibDate.compareTo(getDateEchDocument(), dateDansExercice)==0) || 
//				(LibDate.compareTo(getDateEchDocument(), dateExemple)==0)))return false;
//		if(!((LibDate.compareTo(getDateLivDocument(), dateDansExercice)==0) || 
//				(LibDate.compareTo(getDateLivDocument(), dateExemple)==0)))return false;
		
		if(getLibelleDocument()!=null && !getLibelleDocument().equals(LgrConstantes.C_STR_VIDE))return false;
//		if(!getID_ADRESSE().equals(0))return false;
//		if(!getID_ADRESSE_LIV().equals(0))return false;
		if(getIdTiers()!=null && !getIdTiers().equals(0))return false;
		if(getCodeTiers()!=null && !getCodeTiers().equals(LgrConstantes.C_STR_VIDE))return false;
		if(getNomTiers()!=null && !getNomTiers().equals(LgrConstantes.C_STR_VIDE))return false;
		if(getPrenomTiers()!=null && !getPrenomTiers().equals(LgrConstantes.C_STR_VIDE))return false;
		if(getSurnomTiers()!=null && !getSurnomTiers().equals(LgrConstantes.C_STR_VIDE))return false;
		if(getCodeCompta()!=null && !getCodeCompta().equals(LgrConstantes.C_STR_VIDE))return false;
		if(getCompte()!=null && !getCompte().equals(LgrConstantes.C_STR_VIDE))return false;
//		if(!getID_T_PAIEMENT().equals(0))return false;
//		if(!getCODE_T_PAIEMENT().equals(LgrConstantes.C_STR_VIDE))return false;
//		if(!getID_C_PAIEMENT().equals(0))return false;
//		//if(!LibChaine.emptyNumeric(getREGLE_BONLIV()))return false;
//		if(!LibChaine.emptyNumeric(getREM_HT_BONLIV()))return false;
//		if(!LibChaine.emptyNumeric(getTX_REM_HT_BONLIV()))return false;
//		if(!LibChaine.emptyNumeric(getREM_TTC_BONLIV()))return false;
//		if(!LibChaine.emptyNumeric(getTX_REM_TTC_BONLIV()))return false;
//		if(!getNB_E_BONLIV().equals(0))return false;
//		if(!LibChaine.emptyNumeric(getMT_TTC_CALC()))return false;
//		if(!LibChaine.emptyNumeric(getMT_HT_CALC()))return false;
//		if(!LibChaine.emptyNumeric(getMT_TVA_CALC()))return false;
//		if(!LibChaine.emptyNumeric(getNET_TTC_CALC()))return false;
//		if(!LibChaine.emptyNumeric(getNET_HT_CALC()))return false;
//		if(!LibChaine.emptyNumeric(getNET_TVA_CALC()))return false;
//		if(!getIP_ACCES().equals(""))return false;
//		if(!getTTC().equals(false))return false;
//		if(!getEXPORT().equals(false))return false;
		return true;
	}




	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codeDocument == null) ? 0 : codeDocument.hashCode());
		result = prime * result
				+ ((codeTiers == null) ? 0 : codeTiers.hashCode());
		result = prime * result + ((compte == null) ? 0 : compte.hashCode());
		result = prime * result
				+ ((dateDocument == null) ? 0 : dateDocument.hashCode());
		result = prime * result + ((dateExport == null) ? 0 : dateExport.hashCode());
		result = prime * result + ((idTiers == null) ? 0 : idTiers.hashCode());
		result = prime * result
				+ ((libelleDocument == null) ? 0 : libelleDocument.hashCode());
		result = prime * result
				+ ((netTtcCalc == null) ? 0 : netTtcCalc.hashCode());
		result = prime * result
				+ ((nomTiers == null) ? 0 : nomTiers.hashCode());
		result = prime * result
				+ ((typeDocument == null) ? 0 : typeDocument.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocumentDTO other = (DocumentDTO) obj;
		if (codeDocument == null) {
			if (other.codeDocument != null)
				return false;
		} else if (!codeDocument.equals(other.codeDocument))
			return false;
		if (codeTiers == null) {
			if (other.codeTiers != null)
				return false;
		} else if (!codeTiers.equals(other.codeTiers))
			return false;
		if (compte == null) {
			if (other.compte != null)
				return false;
		} else if (!compte.equals(other.compte))
			return false;
		if (dateDocument == null) {
			if (other.dateDocument != null)
				return false;
		} else if (!dateDocument.equals(other.dateDocument))
			return false;
		if (dateExport == null) {
			if (other.dateExport != null)
				return false;
		} else if (!dateExport.equals(other.dateExport))
			return false;
		if (idTiers == null) {
			if (other.idTiers != null)
				return false;
		} else if (!idTiers.equals(other.idTiers))
			return false;
		if (libelleDocument == null) {
			if (other.libelleDocument != null)
				return false;
		} else if (!libelleDocument.equals(other.libelleDocument))
			return false;
		if (netTtcCalc == null) {
			if (other.netTtcCalc != null)
				return false;
		} else if (!netTtcCalc.equals(other.netTtcCalc))
			return false;
		if (nomTiers == null) {
			if (other.nomTiers != null)
				return false;
		} else if (!nomTiers.equals(other.nomTiers))
			return false;
		if (typeDocument == null) {
			if (other.typeDocument != null)
				return false;
		} else if (!typeDocument.equals(other.typeDocument))
			return false;
		return true;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCodeDocument() {
		return codeDocument;
	}

	public void setCodeDocument(String codeDocument) {
		this.codeDocument = codeDocument;
	}

	public Date getDateDocument() {
		return dateDocument;
	}

	public void setDateDocument(Date dateDocument) {
		this.dateDocument = dateDocument;
	}

	public Date getDateLivDocument() {
		return dateLivDocument;
	}

	public void setDateLivDocument(Date dateLivDocument) {
		this.dateLivDocument = dateLivDocument;
	}

	public String getLibelleDocument() {
		return libelleDocument;
	}

	public void setLibelleDocument(String libelleDocument) {
		this.libelleDocument = libelleDocument;
	}

	public Integer getIdAdresse() {
		return idAdresse;
	}

	public void setIdAdresse(Integer idAdresse) {
		this.idAdresse = idAdresse;
	}

	public Integer getIdAdresseLiv() {
		return idAdresseLiv;
	}

	public void setIdAdresseLiv(Integer idAdresseLiv) {
		this.idAdresseLiv = idAdresseLiv;
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

	public String getPrenomTiers() {
		return prenomTiers;
	}

	public void setPrenomTiers(String prenomTiers) {
		this.prenomTiers = prenomTiers;
	}

	public String getSurnomTiers() {
		return surnomTiers;
	}

	public void setSurnomTiers(String surnomTiers) {
		this.surnomTiers = surnomTiers;
	}

	public String getCodeCompta() {
		return codeCompta;
	}

	public void setCodeCompta(String codeCompta) {
		this.codeCompta = codeCompta;
	}

	public String getCompte() {
		return compte;
	}

	public void setCompte(String compte) {
		this.compte = compte;
	}

	public Integer getIdTPaiement() {
		return idTPaiement;
	}

	public void setIdTPaiement(Integer idTPaiement) {
		this.idTPaiement = idTPaiement;
	}

	public String getCodeTPaiement() {
		return codeTPaiement;
	}

	public void setCodeTPaiement(String codeTPaiement) {
		this.codeTPaiement = codeTPaiement;
	}

	public Integer getIdCPaiement() {
		return idCPaiement;
	}

	public void setIdCPaiement(Integer idCPaiement) {
		this.idCPaiement = idCPaiement;
	}

	public BigDecimal getRemHtDocument() {
		return remHtDocument;
	}

	public void setRemHtDocument(BigDecimal remHtDocument) {
		this.remHtDocument = remHtDocument;
	}

	public BigDecimal getTxRemHtDocument() {
		return txRemHtDocument;
	}

	public void setTxRemHtDocument(BigDecimal txRemHtDocument) {
		this.txRemHtDocument = txRemHtDocument;
	}

	public BigDecimal getRemTtcDocument() {
		return remTtcDocument;
	}

	public void setRemTtcDocument(BigDecimal remTtcDocument) {
		this.remTtcDocument = remTtcDocument;
	}

	public BigDecimal getTxRemTtcDocument() {
		return txRemTtcDocument;
	}

	public void setTxRemTtcDocument(BigDecimal txRemTtcDocument) {
		this.txRemTtcDocument = txRemTtcDocument;
	}

	public Integer getNbEDocument() {
		return nbEDocument;
	}

	public void setNbEDocument(Integer nbEDocument) {
		this.nbEDocument = nbEDocument;
	}

	public BigDecimal getMtTtcCalc() {
		return mtTtcCalc;
	}

	public void setMtTtcCalc(BigDecimal mtTtcCalc) {
		this.mtTtcCalc = mtTtcCalc;
	}

	public BigDecimal getMtHtCalc() {
		return mtHtCalc;
	}

	public void setMtHtCalc(BigDecimal mtHtCalc) {
		this.mtHtCalc = mtHtCalc;
	}

	public BigDecimal getMtTvaCalc() {
		return mtTvaCalc;
	}

	public void setMtTvaCalc(BigDecimal mtTvaCalc) {
		this.mtTvaCalc = mtTvaCalc;
	}

	public BigDecimal getNetTtcCalc() {
		return netTtcCalc;
	}

	public void setNetTtcCalc(BigDecimal netTtcCalc) {
		this.netTtcCalc = netTtcCalc;
	}

	public BigDecimal getNetHtCalc() {
		return netHtCalc;
	}

	public void setNetHtCalc(BigDecimal netHtCalc) {
		this.netHtCalc = netHtCalc;
	}

	public BigDecimal getNetTvaCalc() {
		return netTvaCalc;
	}

	public void setNetTvaCalc(BigDecimal netTvaCalc) {
		this.netTvaCalc = netTvaCalc;
	}

	public String getIpAcces() {
		return ipAcces;
	}

	public void setIpAcces(String ipAcces) {
		this.ipAcces = ipAcces;
	}

	public Boolean getTtc() {
		return ttc;
	}

	public void setTtc(Boolean ttc) {
		this.ttc = ttc;
	}

//	public Boolean getExport() {
//		return export;
//	}
//
//	public void setExport(Boolean export) {
//		this.export = export;
//	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public Boolean getAccepte() {
		return accepte;
	}

	public void setAccepte(Boolean accepte) {
		this.accepte = accepte;
	}

	public String getTypeDocument() {
		return typeDocument;
	}

	public void setTypeDocument(String typeDocument) {
		this.typeDocument = typeDocument;
	}

	public String getCodeTTvaDoc() {
		return codeTTvaDoc;
	}

	public void setCodeTTvaDoc(String codeTTvaDoc) {
		this.codeTTvaDoc = codeTTvaDoc;
	}

	public Boolean getRegleCompletDocument() {
		return regleCompletDocument;
	}

	public void setRegleCompletDocument(Boolean regleCompletDocument) {
		this.regleCompletDocument = regleCompletDocument;
	}

	public BigDecimal getResteAReglerComplet() {
		return resteAReglerComplet;
	}

	public void setResteAReglerComplet(BigDecimal resteAReglerComplet) {
		this.resteAReglerComplet = resteAReglerComplet;
	}

	public String getCodeEtat() {
		return codeEtat;
	}

	public void setCodeEtat(String codeEtat) {
		this.codeEtat = codeEtat;
	}

	public String getLiblEtat() {
		return liblEtat;
	}

	public void setLiblEtat(String liblEtat) {
		this.liblEtat = liblEtat;
	}

	public Date getDateEchDocument() {
		return dateEchDocument;
	}

	public void setDateEchDocument(Date dateEchDocument) {
		this.dateEchDocument = dateEchDocument;
	}

	@Override
	public Date getDateExport() {
		// TODO Auto-generated method stub
		return dateExport;
	}

	@Override
	public Date getDateVerrouillage() {
		// TODO Auto-generated method stub
		return dateVerrouillage;
	}

	@Override
	public boolean getEstMisADisposition() {
		// TODO Auto-generated method stub
		return estMiseADisposition;
	}

	@Override
	public boolean setEstMisADisposition(boolean mad) {
		// TODO Auto-generated method stub
		return estMiseADisposition=mad;
	}



	public void setDateExport(Date dateExport) {
		this.dateExport = dateExport;
	}



	public String getNomEntreprise() {
		// TODO Auto-generated method stub
		return nomEntreprise;
	}



	public void setNomEntreprise(String nomEntreprise) {
		this.nomEntreprise = nomEntreprise;
	}

	
	
	protected  void initMapDTO() {
		mapAliasDTO.put("doc.idDocument",f_idDocument);
		mapAliasDTO.put("doc.codeDocument",f_codeDocument);
		mapAliasDTO.put("doc.dateDocument",f_dateDocument);
		mapAliasDTO.put("doc.dateEchDocument",f_dateEchDocument);
		mapAliasDTO.put("doc.libelleDocument",f_libelleDocument);
		mapAliasDTO.put("tiers.codeTiers",f_codeTiers);
		mapAliasDTO.put("infos.nomTiers",f_nomTiers);
		mapAliasDTO.put("infos.prenomTiers",f_prenomTiers);
		mapAliasDTO.put("infos.nomEntreprise",f_nomEntreprise);		
		mapAliasDTO.put("doc.dateLivDocument",f_dateLivDocument);
		mapAliasDTO.put("doc.netHtCalc",f_netHtCalc);
		mapAliasDTO.put("doc.netTvaCalc",f_netTvaCalc);
		mapAliasDTO.put("doc.netTtcCalc",f_netTtcCalc);
		mapAliasDTO.put("doc.dateExport",f_dateExport);
		mapAliasDTO.put("doc.reglementComplet",f_reglementComplet);
		mapAliasDTO.put("doc.resteAReglerComplet",f_resteAReglerComplet);
		mapAliasDTO.put("etat.codeEtat",f_codeEtat);
		
		mapAliasDTO.put("doc.id_Document",f_idDocument);
		mapAliasDTO.put("doc.code_Document",f_codeDocument);
		mapAliasDTO.put("doc.date_Document",f_dateDocument);
		mapAliasDTO.put("doc.date_Ech_Document",f_dateEchDocument);
		mapAliasDTO.put("doc.libelle_Document",f_libelleDocument);
		mapAliasDTO.put("tiers.code_Tiers",f_codeTiers);
		mapAliasDTO.put("infos.nom_Tiers",f_nomTiers);
		mapAliasDTO.put("infos.prenom_Tiers",f_prenomTiers);
		mapAliasDTO.put("infos.nom_Entreprise",f_nomEntreprise);	
		mapAliasDTO.put("doc.date_Liv_Document",f_dateLivDocument);
		mapAliasDTO.put("doc.net_Ht_Calc",f_netHtCalc);
		mapAliasDTO.put("doc.net_Tva_Calc",f_netTvaCalc);
		mapAliasDTO.put("doc.net_Ttc_Calc",f_netTtcCalc);
		mapAliasDTO.put("doc.date_Export",f_dateExport);
		
		mapAliasDTO.put("et.identifiant",f_identifiantEtat);
		mapAliasDTO.put("relancer",f_relancer);
		
		
//		mapAliasDTO.put("codeDocument",f_codeDocument);
//		mapAliasDTO.put("codeDocument",f_codeDocument);
//		mapAliasDTO.put("codeDocument",f_codeDocument);
//		mapAliasDTO.put("codeDocument",f_codeDocument);
//		mapAliasDTO.put("codeDocument",f_codeDocument);
//		mapAliasDTO.put("codeDocument",f_codeDocument);
//		mapAliasDTO.put("codeDocument",f_codeDocument);
//		mapAliasDTO.put("codeDocument",f_codeDocument);
//		mapAliasDTO.put("codeDocument",f_codeDocument);
//		mapAliasDTO.put("codeDocument",f_codeDocument);
//		mapAliasDTO.put("codeDocument",f_codeDocument);
		
		
	}




	public BigDecimal getReglementComplet() {
		return reglementComplet;
	}



	public void setReglementComplet(BigDecimal reglementComplet) {
		this.reglementComplet = reglementComplet;
	}



	public String getCodeFamille() {
		return codeFamille;
	}



	public void setCodeFamille(String codeFamille) {
		this.codeFamille = codeFamille;
	}


	public String getIdentifiant() {
		return identifiant;
	}

	public void setIdentifiant(String identifiant) {
		this.identifiant = identifiant;
	}



	public Boolean getRelancer() {
		return relancer;
	}



	public void setRelancer(Boolean relancer) {
		this.relancer = relancer;
	}
}

