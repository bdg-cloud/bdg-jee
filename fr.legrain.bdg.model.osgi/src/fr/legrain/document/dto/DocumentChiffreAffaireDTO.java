package fr.legrain.document.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.legrain.general.dto.AbstractDTO;
import fr.legrain.lib.data.LgrConstantes;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;





public class DocumentChiffreAffaireDTO extends AbstractDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2106619267983492895L;
	
	
	
	///// décrire chaque champ en constante	

	public static final String f_count="count";
	public static final String f_jour="jour";
	public static final String f_mois="mois";
	public static final String f_annee="annee";
	public static final String f_jourInt="jourInt";
	public static final String f_moisInt="moisInt";
	public static final String f_anneeInt="anneeInt";
	public static final String f_mtHtCalc="mtHtCalc";
	public static final String f_mtTvaCalc="mtTvaCalc";
	public static final String f_mtTtcCalc="mtTtcCalc";
	public static final String f_affectationTotale="affectationTotale";
	public static final String f_resteARegler="resteARegler";
	public static final String f_codeFamille="codeFamille";
	public static final String f_libcFamille="libcFamille";
	public static final String f_codeTPaiement="codeTPaiement";
	public static final String f_tauxTva="tauxTva";
	public static final String f_vendeur="vendeur";
	public static final String f_codeFamilleTiers="codeFamilleTiers";
	public static final String f_codeTiers_codeArticle="codeTiers,codeArticle";
	public static final String f_codeTiers="codeTiers";
	public static final String f_codeArticle="codeArticle";
	public static final String f_codeEtat="codeEtat";
	public static final String f_username="username";
	public static final String f_reglementComplet="reglementComplet";
	public static final String f_resteAReglerComplet="resteAReglerComplet";
	/**RAJOUT YANN**/
	public static final String f_libellecArticle="libellecArticle";
	public static final String f_codeDocument="codeDocument";
	public static final String f_dateDocument="dateDocument";
	public static final String f_dateLivraison="dateLivraison";
	public static final String f_nomTiers="nomTiers";
	public static final String f_prenomTiers="prenomTiers";
	public static final String f_nomEntreprise="nomEntreprise";
	public static final String f_qteLDocument= "qteLDocument";
	public static final String f_u1LDocument = "u1LDocument";
	public static final String f_typeDoc="typeDoc";
	/**RAJOUT ISA**/
	public static final String f_fournisseur="fournisseur";
	public static final String f_nomFournisseur="nomFournisseur";
	public static final String f_qte2LDocument= "qte2LDocument";
	public static final String f_u2LDocument = "u2LDocument";
	public static final String f_lot = "lot";
	public static final String f_libLDocument="libLDocument";
	
	public static final String f_codeTransporteur="codeTransporteur";
	public static final String f_liblTransporteur="liblTransporteur";
	
	public static final String f_utiliseUniteSaisie="utiliseUniteSaisie";
	public static final String f_qteSaisieLDocument="qteSaisieLDocument";
	public static final String f_uSaisieLDocument="uSaisieLDocument";
	
	public static final String f_identifiantEtat="identifiant";
	public static final String f_relancer="relancer";
	
	/////////////// Fin décrire chaque champ en constante pour requete dynamique	
	
	//CONST REQUETES
		// orderBy
			public static final String ORDER_BY_CODE_DOCUMENT= f_codeDocument;
			public static final String ORDER_BY_DATE_DOCUMENT= f_dateDocument;
			public static final String ORDER_BY_CODE_ARTICLE= f_codeArticle;
			public static final String ORDER_BY_CODE_TIERS= f_codeTiers;
			public static final String ORDER_BY_CODE_TIERS_ARTICLE= f_codeTiers_codeArticle;
			public static final String ORDER_BY_CODE_FAMILLE_ARTICLE = f_codeFamille;
			public static final String ORDER_BY_LIBL_FAMILLE_ARTICLE = f_libcFamille;

	private List<DocumentChiffreAffaireDTO> listeDetail = new ArrayList<DocumentChiffreAffaireDTO>();
	
	private boolean typeReglement=false;
	private String jour=LgrConstantes.C_STR_VIDE;
	private String mois=LgrConstantes.C_STR_VIDE;
	private String annee=LgrConstantes.C_STR_VIDE;
	private int jourInt;
	private int moisInt;
	private int anneeInt;
	private BigDecimal mtHtCalc=new BigDecimal(0);
	private BigDecimal mtTvaCalc=new BigDecimal(0);
	private BigDecimal mtTtcCalc=new BigDecimal(0);
	
	private BigDecimal affectationTotale=new BigDecimal(0);
//	private BigDecimal resteARegler=new BigDecimal(0);
	
	private BigDecimal reglementComplet=new BigDecimal(0);
	private BigDecimal resteAReglerComplet=new BigDecimal(0);
	
	private String codeFamille = LgrConstantes.C_STR_VIDE;
	private String libcFamille = LgrConstantes.C_STR_VIDE;
	private String codeTPaiement;
	private BigDecimal tauxTva;
	private String vendeur;
	private String codeFamilleTiers;
	private String codeTiers;
	private String codeArticle;
	private String codeEtat;
	/**RAJOUT YANN**/
	private String libellecArticle = LgrConstantes.C_STR_VIDE ;
//	private String codeDocument=LgrConstantes.C_STR_VIDE;
	private String codeDocument=LgrConstantes.C_STR_VIDE;
	private Date dateDocument;
	private Date dateLivraison;
	private String nomTiers=LgrConstantes.C_STR_VIDE;
	private String prenomTiers=LgrConstantes.C_STR_VIDE;
	private String nomEntreprise=LgrConstantes.C_STR_VIDE;
	private BigDecimal qteLDocument = new BigDecimal(0);
	private String u1LDocument = LgrConstantes.C_STR_VIDE;
	private String typeDoc = LgrConstantes.C_STR_VIDE;
	private BigDecimal qte2LDocument = new BigDecimal(0);
	private String u2LDocument = LgrConstantes.C_STR_VIDE;
	
	private String lot = LgrConstantes.C_STR_VIDE;
	private String fournisseur = LgrConstantes.C_STR_VIDE;
	private String nomFournisseur = LgrConstantes.C_STR_VIDE;
	
	private String codeTransporteur = LgrConstantes.C_STR_VIDE;
	private String liblTransporteur = LgrConstantes.C_STR_VIDE;
	
	private Boolean utiliseUniteSaisie = false;
	private BigDecimal qteSaisieLDocument = new BigDecimal(0);
	private String uSaisieLDocument = LgrConstantes.C_STR_VIDE;
	private String identifiant = LgrConstantes.C_STR_VIDE;

	private List<DocumentChiffreAffaireDTO> list = null;
	private List<TaTDocDTO> taTDoc = null;
	private long count=0;
	private Boolean relancer = false;
	


	
	public DocumentChiffreAffaireDTO( int jour, int mois, int annee, boolean typeReglement, BigDecimal mtHtCalc,
			BigDecimal mtTvaCalc, BigDecimal mtTtcCalc, BigDecimal affectationTotale, BigDecimal resteAReglerComplet) {
		super();
		this.typeReglement = typeReglement;
		
		this.jour = LibConversion.integerToString(jour);
		this.mois = LibConversion.integerToString(mois);
		this.annee = LibConversion.integerToString(annee);
		this.mtHtCalc = mtHtCalc;
		this.mtTvaCalc = mtTvaCalc;
		this.mtTtcCalc = mtTtcCalc;
		this.affectationTotale = affectationTotale;
		this.resteAReglerComplet = resteAReglerComplet;
	}
	
	public DocumentChiffreAffaireDTO(  int mois, int annee, boolean typeReglement, BigDecimal mtHtCalc,
			BigDecimal mtTvaCalc, BigDecimal mtTtcCalc, BigDecimal affectationTotale, BigDecimal resteAReglerComplet) {
		super();
		this.typeReglement = typeReglement;
		
		this.mois = LibConversion.integerToString(mois);
		this.annee = LibConversion.integerToString(annee);
		this.mtHtCalc = mtHtCalc;
		this.mtTvaCalc = mtTvaCalc;
		this.mtTtcCalc = mtTtcCalc;
		this.affectationTotale = affectationTotale;
		this.resteAReglerComplet = resteAReglerComplet;
	}

	public DocumentChiffreAffaireDTO(  int annee, boolean typeReglement, BigDecimal mtHtCalc,
			BigDecimal mtTvaCalc, BigDecimal mtTtcCalc, BigDecimal affectationTotale, BigDecimal resteAReglerComplet) {
		super();
		this.typeReglement = typeReglement;

		this.annee = LibConversion.integerToString(annee);
		this.mtHtCalc = mtHtCalc;
		this.mtTvaCalc = mtTvaCalc;
		this.mtTtcCalc = mtTtcCalc;
		this.affectationTotale = affectationTotale;
		this.resteAReglerComplet = resteAReglerComplet;
	}

	public DocumentChiffreAffaireDTO( boolean typeReglement, BigDecimal mtHtCalc,
			BigDecimal mtTvaCalc, BigDecimal mtTtcCalc, BigDecimal affectationTotale, BigDecimal resteAReglerComplet) {
		super();
		this.typeReglement = typeReglement;

		this.mtHtCalc = mtHtCalc;
		this.mtTvaCalc = mtTvaCalc;
		this.mtTtcCalc = mtTtcCalc;
		this.affectationTotale = affectationTotale;
		this.resteAReglerComplet = resteAReglerComplet;
	}
	
	public DocumentChiffreAffaireDTO(
			int jour,
			int mois,
			int annee,
			BigDecimal mtHtCalc,
			BigDecimal mtTvaCalc,
			BigDecimal mtTtcCalc)
	{
		this.jour = LibConversion.integerToString(jour);
		this.mois = LibConversion.integerToString(mois);
		this.annee = LibConversion.integerToString(annee);
		this.mtHtCalc = mtHtCalc;
		this.mtTvaCalc = mtTvaCalc;
		this.mtTtcCalc = mtTtcCalc;
	}

	
	public DocumentChiffreAffaireDTO(
			int jour,
			int mois,
			int annee,
			boolean typeReglement,
			BigDecimal mtTtcCalc,
			BigDecimal affectationTotale,
			BigDecimal resteAReglerComplet)
	{
		this.jour = LibConversion.integerToString(jour);
		this.mois = LibConversion.integerToString(mois);
		this.annee = LibConversion.integerToString(annee);
		this.typeReglement = typeReglement;
		this.affectationTotale = affectationTotale;
		this.resteAReglerComplet = resteAReglerComplet;
		this.mtTtcCalc = mtTtcCalc;
	}
	

	
	public DocumentChiffreAffaireDTO(
			int mois,
			int annee,
			boolean typeReglement,
			BigDecimal mtTtcCalc,
			BigDecimal affectationTotale,
			BigDecimal resteAReglerComplet)
	{
		this.mois = LibConversion.integerToString(mois);
		this.annee = LibConversion.integerToString(annee);
		this.typeReglement = typeReglement;
		this.affectationTotale = affectationTotale;
		this.resteAReglerComplet = resteAReglerComplet;
		this.mtTtcCalc = mtTtcCalc;
	}
	
	public DocumentChiffreAffaireDTO(

			int annee,
			boolean typeReglement,
			BigDecimal mtTtcCalc,
			BigDecimal affectationTotale,
			BigDecimal resteAReglerComplet)
	{
		this.annee = LibConversion.integerToString(annee);
		this.typeReglement = typeReglement;
		this.affectationTotale = affectationTotale;
		this.resteAReglerComplet = resteAReglerComplet;
		this.mtTtcCalc = mtTtcCalc;
	}
	
/*
 * 	Permet de remonter des infos sur les règlements des factures ou avoirs
 */
	public DocumentChiffreAffaireDTO(
			boolean typeReglement, 
			BigDecimal mtTtcCalc, 
			BigDecimal affectationTotale,
			BigDecimal resteAReglerComplet) 
	{
		this.typeReglement = typeReglement;
		this.mtTtcCalc = mtTtcCalc;
		this.affectationTotale = affectationTotale;
		this.resteAReglerComplet = resteAReglerComplet;
	}







	public DocumentChiffreAffaireDTO(
			int mois,
			int annee,
			BigDecimal mtHtCalc,
			BigDecimal mtTvaCalc,
			BigDecimal mtTtcCalc)
	{
		this.mois = LibConversion.integerToString(mois);
		this.annee = LibConversion.integerToString(annee);
		this.mtHtCalc = mtHtCalc;
		this.mtTvaCalc = mtTvaCalc;
		this.mtTtcCalc = mtTtcCalc;
	}

	public DocumentChiffreAffaireDTO(
			int annee,
			BigDecimal mtHtCalc,
			BigDecimal mtTvaCalc,
			BigDecimal mtTtcCalc)
	{
		this.annee = LibConversion.integerToString(annee);
		this.mtHtCalc = mtHtCalc;
		this.mtTvaCalc = mtTvaCalc;
		this.mtTtcCalc = mtTtcCalc;
	}
	
	public DocumentChiffreAffaireDTO(
			BigDecimal mtHtCalc,
			BigDecimal mtTvaCalc,
			BigDecimal mtTtcCalc)
	{
		this.mtHtCalc = mtHtCalc;
		this.mtTvaCalc = mtTvaCalc;
		this.mtTtcCalc = mtTtcCalc;
	}	
	
	
	public List<DocumentChiffreAffaireDTO> getList() {
		if(list==null)
			list = new ArrayList<DocumentChiffreAffaireDTO>();
		return list;
	}


	
	public List<TaTDocDTO> getTaTDoc() {
//		if(liste==null)
//			liste = new ArrayList<SWTTDoc>(2);
//		liste.add(new SWTTDoc("a","b"));
//		liste.add(new SWTTDoc("c","d"));
		return taTDoc;
	}

	
	public DocumentChiffreAffaireDTO(){
		super();
		initMapDTO();
	}

	public String getJour() {
		return jour;
	}

	public void setJour(String jour) {
		this.jour = jour;
	}

	public String getMois() {
		return mois;
	}

	public void setMois(String mois) {
		this.mois = mois;
	}

	public String getAnnee() {
		return annee;
	}

	public void setAnnee(String annee) {
		this.annee = annee;
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
	
	
	
	public BigDecimal getAffectationTotale() {
		return affectationTotale;
	}

	public void setAffectationTotale(BigDecimal affectationTotale) {
		this.affectationTotale = affectationTotale;
	}

//	public BigDecimal getResteARegler() {
//		return resteARegler;
//	}
//
//	public void setResteARegler(BigDecimal resteARegler) {
//		this.resteARegler = resteARegler;
//	}
	
	/**
	 * Initialise et retourne une liste avec un seul objet de type DocumentChiffreAffaireDTO avec des valeurs à zéros et la date par défaut.
	 * 
	 * @param dateDefaut
	 * @return retourne une liste avec un seul objet de type DocumentChiffreAffaireDTO avec des valeurs à zéros et la date par défaut.
	 */
	static public ArrayList<DocumentChiffreAffaireDTO> initialiseAZero(Date dateDefaut) {
	ArrayList<DocumentChiffreAffaireDTO> l = new ArrayList<DocumentChiffreAffaireDTO>();
	DocumentChiffreAffaireDTO docCaDTO = new DocumentChiffreAffaireDTO();
	docCaDTO.setAnnee(LibDate.getAnnee(dateDefaut));
	docCaDTO.setMois(LibDate.getMois(dateDefaut));
	docCaDTO.setJour(LibDate.getJour(dateDefaut));
	docCaDTO.setMtTtcCalc(BigDecimal.ZERO);
	docCaDTO.setMtTvaCalc(BigDecimal.ZERO);
	docCaDTO.setMtHtCalc(BigDecimal.ZERO);
	docCaDTO.setAffectationTotale(BigDecimal.ZERO);
	docCaDTO.setResteAReglerComplet(BigDecimal.ZERO);
	l.add(docCaDTO);
	return l;
	}







	public boolean isTypeReglement() {
		return typeReglement;
	}







	public void setTypeReglement(boolean typeReglement) {
		this.typeReglement = typeReglement;
	}

	public int getJourInt() {
		return jourInt;
	}

	public void setJourInt(int jourInt) {
		this.jourInt = jourInt;
		this.jour=LibConversion.integerToString(jourInt);
	}

	public int getMoisInt() {
		return moisInt;
	}

	public void setMoisInt(int moisInt) {
		this.moisInt = moisInt;
		this.mois=LibConversion.integerToString(moisInt);
	}

	public int getAnneeInt() {
		return anneeInt;
	}

	public void setAnneeInt(int anneeInt) {
		this.anneeInt = anneeInt;
		this.annee=LibConversion.integerToString(anneeInt);
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public String getCodeFamille() {
		return codeFamille;
	}

	public void setCodeFamille(String codeFamille) {
		this.codeFamille = codeFamille;
	}

	public String getCodeTPaiement() {
		return codeTPaiement;
	}

	public void setCodeTPaiement(String codeTPaiement) {
		this.codeTPaiement = codeTPaiement;
	}

	public BigDecimal getTauxTva() {
		return tauxTva;
	}

	public void setTauxTva(BigDecimal tauxTva) {
		this.tauxTva = tauxTva;
	}

	public String getVendeur() {
		return vendeur;
	}

	public void setVendeur(String vendeur) {
		this.vendeur = vendeur;
	}

	public String getCodeFamilleTiers() {
		return codeFamilleTiers;
	}

	public void setCodeFamilleTiers(String codeFamilleTiers) {
		this.codeFamilleTiers = codeFamilleTiers;
	}

	public String getCodeTiers() {
		return codeTiers;
	}

	public void setCodeTiers(String codeTiers) {
		this.codeTiers = codeTiers;
	}

	public String getCodeArticle() {
		return codeArticle;
	}

	public void setCodeArticle(String codeArticle) {
		this.codeArticle = codeArticle;
	}

	@Override
	protected void initMapDTO() {
		//remplir ceux qui sont différents
		mapAliasDTO.put("count", f_count);	
		mapAliasDTO.put("username", f_vendeur);	
		mapAliasDTO.put("ldoc.mtHtLApresRemiseGlobaleDocument", f_mtHtCalc);
		mapAliasDTO.put("ldoc.mtTvaCalc", f_mtTvaCalc);	
		mapAliasDTO.put("ldoc.mtTtcLApresRemiseGlobaleDocument", f_mtTtcCalc);
		mapAliasDTO.put("famTiers.codeFamille", f_codeFamilleTiers);
		mapAliasDTO.put("fam.codeFamille", f_codeFamille);
		mapAliasDTO.put("fam.libcFamille", f_libcFamille);
		mapAliasDTO.put("tiers.codeTiers", f_codeTiers);
		mapAliasDTO.put("art.codeArticle", f_codeArticle);
		mapAliasDTO.put("ldoc.tauxTvaLDocument", f_tauxTva);
		mapAliasDTO.put("tp.codeTPaiement", f_codeTPaiement);
		mapAliasDTO.put("etat.codeEtat",f_codeEtat);
		/**TEST YANN**/
//		mapAliasDTO.put("codeDocument",f_codeDocument);
//		mapAliasDTO.put("doc.code_document",f_codeDocument);
		/**RAJOUT YANN **/
		mapAliasDTO.put("art.libellecArticle",f_libellecArticle);
		mapAliasDTO.put("doc.code_Document",f_codeDocument);
		mapAliasDTO.put("doc.date_Document",f_dateDocument);
		mapAliasDTO.put("doc.codeDocument",f_codeDocument);
		mapAliasDTO.put("doc.dateDocument",f_dateDocument);
		mapAliasDTO.put("infos.nomTiers",f_nomTiers);
		mapAliasDTO.put("infos.prenomTiers",f_prenomTiers);
		mapAliasDTO.put("infos.nomEntreprise",f_nomEntreprise);
		mapAliasDTO.put("infos.nom_Tiers",f_nomTiers);
		mapAliasDTO.put("infos.prenom_Tiers",f_prenomTiers);
		mapAliasDTO.put("infos.nom_Entreprise",f_nomEntreprise);
		mapAliasDTO.put("ldoc.qteLDocument",f_qteLDocument);
		mapAliasDTO.put("ldoc.u1LDocument",f_u1LDocument);
		mapAliasDTO.put("ldoc.qte_l_document",f_qteLDocument);
		mapAliasDTO.put("ldoc.u1_l_document",f_u1LDocument);
		mapAliasDTO.put("ldoc.qte2LDocument",f_qte2LDocument);
		mapAliasDTO.put("ldoc.u2LDocument",f_u2LDocument);
		mapAliasDTO.put("ldoc.qte2_l_document",f_qte2LDocument);
		mapAliasDTO.put("ldoc.u2_l_document",f_u2LDocument);
		
		mapAliasDTO.put(f_extract_day, f_jour);	
		mapAliasDTO.put(f_extract_month, f_mois);	
		mapAliasDTO.put(f_extract_year, f_annee);
		mapAliasDTO.put(f_extract_day_int, f_jourInt);	
		mapAliasDTO.put(f_extract_month_int, f_moisInt);	
		mapAliasDTO.put(f_extract_year_int, f_anneeInt);	
		mapAliasDTO.put("doc.netHtCalc", f_mtHtCalc);	
		mapAliasDTO.put("doc.netTvaCalc", f_mtTvaCalc);
		mapAliasDTO.put("doc.netTtcCalc", f_mtTtcCalc);
		mapAliasDTO.put("doc.net_Ht_Calc", f_mtHtCalc);	
		mapAliasDTO.put("doc.net_Tva_Calc", f_mtTvaCalc);
		mapAliasDTO.put("doc.net_Ttc_Calc", f_mtTtcCalc);
		mapAliasDTO.put("doc.reglementComplet",f_reglementComplet);
		mapAliasDTO.put("doc.resteAReglerComplet",f_resteAReglerComplet);
		
		mapAliasDTO.put("et.identifiant",f_identifiantEtat);
		mapAliasDTO.put("relancer",f_relancer);
	}

	public BigDecimal getReglementComplet() {
		return reglementComplet;
	}

	public void setReglementComplet(BigDecimal reglementComplet) {
		this.reglementComplet = reglementComplet;
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

	public String getLibcFamille() {
		return libcFamille;
	}

	public void setLibcFamille(String libcFamille) {
		this.libcFamille = libcFamille;
	}

	public String getLibellecArticle() {
		return libellecArticle;
	}

	public void setLibellecArticle(String libellecArticle) {
		this.libellecArticle = libellecArticle;
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

	public String getNomEntreprise() {
		return nomEntreprise;
	}

	public void setNomEntreprise(String nomEntreprise) {
		this.nomEntreprise = nomEntreprise;
	}

	public String getU1LDocument() {
		return u1LDocument;
	}

	public void setU1LDocument(String u1lDocument) {
		u1LDocument = u1lDocument;
	}

	public BigDecimal getQteLDocument() {
		return qteLDocument;
	}

	public void setQteLDocument(BigDecimal qteLDocument) {
		this.qteLDocument = qteLDocument;
	}

	public String getTypeDoc() {
		return typeDoc;
	}

	public void setTypeDoc(String typeDoc) {
		this.typeDoc = typeDoc;
	}

	public List<DocumentChiffreAffaireDTO> getListeDetail() {
		return listeDetail;
	}

	public void setListeDetail(List<DocumentChiffreAffaireDTO> listeDetail) {
		this.listeDetail = listeDetail;
	}

	public BigDecimal getQte2LDocument() {
		return qte2LDocument;
	}

	public void setQte2LDocument(BigDecimal qte2lDocument) {
		qte2LDocument = qte2lDocument;
	}

	public String getU2LDocument() {
		return u2LDocument;
	}

	public void setU2LDocument(String u2lDocument) {
		u2LDocument = u2lDocument;
	}

	public String getLot() {
		return lot;
	}

	public void setLot(String lot) {
		this.lot = lot;
	}

	public String getFournisseur() {
		return fournisseur;
	}

	public void setFournisseur(String fournisseur) {
		this.fournisseur = fournisseur;
	}

	public String getNomFournisseur() {
		return nomFournisseur;
	}

	public void setNomFournisseur(String nomFournisseur) {
		this.nomFournisseur = nomFournisseur;
	}

	public Date getDateLivraison() {
		return dateLivraison;
	}

	public void setDateLivraison(Date dateLivraison) {
		this.dateLivraison = dateLivraison;
	}


	public String getCodeTransporteur() {
		return codeTransporteur;
	}

	public void setCodeTransporteur(String codeTransporteur) {
		this.codeTransporteur = codeTransporteur;
	}

	public String getLiblTransporteur() {
		return liblTransporteur;
	}

	public void setLiblTransporteur(String liblTransporteur) {
		this.liblTransporteur = liblTransporteur;
	}

	public Boolean getUtiliseUniteSaisie() {
		return utiliseUniteSaisie;
	}

	public void setUtiliseUniteSaisie(Boolean utiliseUniteSaisie) {
		this.utiliseUniteSaisie = utiliseUniteSaisie;
	}

	public BigDecimal getQteSaisieLDocument() {
		return qteSaisieLDocument;
	}

	public void setQteSaisieLDocument(BigDecimal qteSaisieLDocument) {
		this.qteSaisieLDocument = qteSaisieLDocument;
	}

	public String getUSaisieLDocument() {
		return uSaisieLDocument;
	}

	public void setUSaisieLDocument(String uSaisieLDocument) {
		this.uSaisieLDocument = uSaisieLDocument;
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



	
//	public String retournAlias(String prefixe,String champ) {
//		return " as "+mapAliasDTO.get(prefixe+"."+champ);
//	}
//	public String retournChampAndAlias(String prefixe,String champ) {
//		return prefixe+"."+champ+" as "+mapAliasDTO.get(prefixe+"."+champ);
//	}

}
