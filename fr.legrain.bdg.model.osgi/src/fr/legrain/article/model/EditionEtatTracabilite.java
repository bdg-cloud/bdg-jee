package fr.legrain.article.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/** Créé par Dima **/

public class EditionEtatTracabilite implements Serializable{

	public final String R_VIDE = "V";
	public final String R_OK = "OK";
	public final String R_FAUX = "F";
	public final String R_PARTIEL_OK = "P_OK";
	public final String R_PARTIEL_FAUX = "P_FAUX";
	
	
	/** Variables **/
	// Produit Fini
	private String		pfNumLot;
	private String		pfCodeArticle;
	private String		pfLibelleArticle;
	private BigDecimal	pfQte1;
	private BigDecimal	pfQte2;
	private String		pfUnite1;
	private String		pfUnite2;
	private String		pfCodeFabrication;
	private String		pfLibelleFabrication;
	private Date		pfDateFabrication;
	private Date		pfDluo;
	private String		pfCodeEntrepot;
	private String		pfLibelleEntrepot;
	private BigDecimal	pfQteEmplacement;
	private String		pfControlleProbleme;
	private boolean		pfValid;
	
	// Matière Première
	private String		mpNumLot;
	private String		mpCodeArticle;
	private String		mpLibelleArticle;
	private BigDecimal	mpQte1;
	private BigDecimal	mpQte2;
	private String		mpUnite1;
	private String		mpUnite2;
	private Date		mpDateFabrication;
	private Date		mpDluo;
	private String		mpCodeEntrepot;
	private String		mpLibelleEntrepot;
	private BigDecimal	mpQteEmplacement;
	private String		mpControlleProbleme;
	private String		mpFamille; // Famille et Type à même temps
	private boolean		mpEntree; // ?
	private boolean		mpSortie; // ?
	private boolean		mpValid;
	
	// Bon de Reception de Matière Première
	private String		brNumReception;
	private Date		brDateReception;
	private BigDecimal	brQteReception;
	private String		brUniteReception;
	private String		brControlleProbleme;
	private String		brNomFournisseur;
	private String		brCodeFournisseur;

	// Etat des Stocks Global - Mouvement du Stock
	private String		mouvEntrepot;
	private String		mouvLibEntrepot;
	private String		mouvCodeArticle;
	private String		mouvNumLot;
	private	String		mouvLibelle;
	private String		mouvLibelleArticle;
	private Date		mouveDate;
	private Date		mouveDluo;
	private BigDecimal	mouveQte1;
	private String		mouveUnite1;
	private BigDecimal	mouveQte2;
	private String		mouveUnite2;
	private BigDecimal	mouvQteDepart;
	private BigDecimal	mouvQteEntree; // positive
	private BigDecimal	mouvQteSortie; // négative
	private BigDecimal	mouvQteCommande; //
	private BigDecimal	mouvQteDisponible;
	private String		mouveTypeFamille;
	private String		mouveCodeDocument;
	private String		mouveLibDocument;
	
	
	// Etat de Non-Conformités
	private String		numLot;
	private Date		dateModif;
	private String		dateModifStr;
	private String		codeArticle;
	private String		libelleGroupe;
	private String		libelleConformite;
	private String		resultat; // Valeur Constatée
	private String		actionCorrective;
	private boolean		effectuee;
	private String 	    libelleLot;
	private String      TypeDocument;
	private String      codeTDocument;
	private String      codeDocument;
	private String		expressionVerifiee;
	private boolean		forcage;
	private String      observation;
	private BigDecimal  Qte1;
	private String      U1;
	/** Constructors **/
	
	public EditionEtatTracabilite(){
		
	}
	
	public EditionEtatTracabilite(String pfNumLot, String pfCodeArticle,
			String pfLibelleArticle, BigDecimal pfQte1, BigDecimal pfQte2,
			String pfUnite1, String pfUnite2, String pfCodeFabrication,
			String pfLibelleFabrication, Date pfDateFabrication, Date pfDluo,
			String pfCodeEntrepot, String pfLibelleEntrepot,
			BigDecimal pfQteEmplacement, String pfControlleProbleme,
			boolean pfValid, String mpNumLot, String mpCodeArticle,
			String mpLibelleArticle, BigDecimal mpQte1, BigDecimal mpQte2,
			String mpUnite1, String mpUnite2, Date mpDateFabrication,
			Date mpDluo, String mpCodeEntrepot, String mpLibelleEntrepot,
			BigDecimal mpQteEmplacement, String mpControlleProbleme,
			String mpFamille, boolean mpEntree, boolean mpSortie,
			boolean mpValid, String brNumReception, Date brDateReception,
			BigDecimal brQteReception, String brUniteReception,
			String brControlleProbleme, String brNomFournisseur,
			String brCodeFournisseur, String mouvEntrepot,
			String mouvCodeArticle, String mouvNumLot, String mouvLibelle,
			String mouvLibelleArticle, Date mouveDate, BigDecimal mouveQte1,
			String mouveUnite1, BigDecimal mouvQteDepart,
			BigDecimal mouvQteEntree, BigDecimal mouvQteSortie,
			BigDecimal mouvQteCommande, BigDecimal mouvQteDisponible,
			String mouveTypeFamille, String mouveCodeDocument, String numLot,
			Date dateModif, String codeArticle, String libelleGroupe,
			String libelleConformite, String resultat, String actionCorrective,
			boolean effectuee,String libelleLot,String  TypeDocument,String codeTDocument, String codeDocument) {
		super();
		
		this.pfNumLot = pfNumLot;
		this.pfCodeArticle = pfCodeArticle;
		this.pfLibelleArticle = pfLibelleArticle;
		this.pfQte1 = pfQte1;
		this.pfQte2 = pfQte2;
		this.pfUnite1 = pfUnite1;
		this.pfUnite2 = pfUnite2;
		this.pfCodeFabrication = pfCodeFabrication;
		this.pfLibelleFabrication = pfLibelleFabrication;
		this.pfDateFabrication = pfDateFabrication;
		this.pfDluo = pfDluo;
		this.pfCodeEntrepot = pfCodeEntrepot;
		this.pfLibelleEntrepot = pfLibelleEntrepot;
		this.pfQteEmplacement = pfQteEmplacement;
		this.pfControlleProbleme = pfControlleProbleme;
		this.pfValid = pfValid;
		
		this.mpNumLot = mpNumLot;
		this.mpCodeArticle = mpCodeArticle;
		this.mpLibelleArticle = mpLibelleArticle;
		this.mpQte1 = mpQte1;
		this.mpQte2 = mpQte2;
		this.mpUnite1 = mpUnite1;
		this.mpUnite2 = mpUnite2;
		this.mpDateFabrication = mpDateFabrication;
		this.mpDluo = mpDluo;
		this.mpCodeEntrepot = mpCodeEntrepot;
		this.mpLibelleEntrepot = mpLibelleEntrepot;
		this.mpQteEmplacement = mpQteEmplacement;
		this.mpControlleProbleme = mpControlleProbleme;
		this.mpFamille = mpFamille;
		this.mpEntree = mpEntree;
		this.mpSortie = mpSortie;
		this.mpValid = mpValid;
		
		this.brNumReception = brNumReception;
		this.brDateReception = brDateReception;
		this.brQteReception = brQteReception;
		this.brUniteReception = brUniteReception;
		this.brControlleProbleme = brControlleProbleme;
		this.brNomFournisseur = brNomFournisseur;
		this.brCodeFournisseur = brCodeFournisseur;
		
		this.mouvEntrepot = mouvEntrepot;
		this.mouvCodeArticle = mouvCodeArticle;
		this.mouvNumLot = mouvNumLot;
		this.mouvLibelle = mouvLibelle;
		this.mouvLibelleArticle = mouvLibelleArticle;
		this.mouveDate = mouveDate;
		this.mouveQte1 = mouveQte1;
		this.mouveUnite1 = mouveUnite1;
		this.mouvQteDepart = mouvQteDepart;
		this.mouvQteEntree = mouvQteEntree;
		this.mouvQteSortie = mouvQteSortie;
		this.mouvQteCommande = mouvQteCommande;
		this.mouvQteDisponible = mouvQteDisponible;
		this.mouveTypeFamille = mouveTypeFamille;
		this.mouveCodeDocument = mouveCodeDocument;
		
		this.numLot = numLot;
		this.dateModif = dateModif;
		this.codeArticle = codeArticle;
		this.libelleGroupe = libelleGroupe;
		this.libelleConformite = libelleConformite;
		this.resultat = resultat;
		this.actionCorrective = actionCorrective;
		this.effectuee = effectuee;
		this.libelleLot = libelleLot;
		this.codeTDocument = codeTDocument;
		this.TypeDocument = TypeDocument;
		this.codeDocument = codeDocument;
	}

	public EditionEtatTracabilite(String pfNumLot, String pfCodeArticle,
			String pfLibelleArticle, BigDecimal pfQte1, BigDecimal pfQte2,
			String pfUnite1, String pfUnite2, String pfCodeFabrication,
			String pfLibelleFabrication, Date pfDateFabrication, Date pfDluo,
			String pfCodeEntrepot, String pfLibelleEntrepot,
			BigDecimal pfQteEmplacement, String pfControlleProbleme,
			boolean pfValid, String mpNumLot, String mpCodeArticle,
			String mpLibelleArticle, BigDecimal mpQte1, BigDecimal mpQte2,
			String mpUnite1, String mpUnite2, Date mpDateFabrication,
			Date mpDluo, String mpCodeEntrepot, String mpLibelleEntrepot,
			BigDecimal mpQteEmplacement, String mpControlleProbleme,
			String mpFamille, boolean mpEntree, boolean mpSortie,
			boolean mpValid, String brNumReception, Date brDateReception,
			BigDecimal brQteReception, String brUniteReception,
			String brControlleProbleme, String brNomFournisseur,
			String brCodeFournisseur) {
		super();
		
		// -- Produit Fini
		this.pfNumLot = pfNumLot;
		this.pfCodeArticle = pfCodeArticle;
		this.pfLibelleArticle = pfLibelleArticle;
		this.pfQte1 = pfQte1;
		this.pfQte2 = pfQte2;
		this.pfUnite1 = pfUnite1;
		this.pfUnite2 = pfUnite2;
		this.pfCodeFabrication = pfCodeFabrication;
		this.pfLibelleFabrication = pfLibelleFabrication;
		this.pfDateFabrication = pfDateFabrication;
		this.pfDluo = pfDluo;
		this.pfCodeEntrepot = pfCodeEntrepot;
		this.pfLibelleEntrepot = pfLibelleEntrepot;
		this.pfQteEmplacement = pfQteEmplacement;
		this.pfControlleProbleme = pfControlleProbleme;
		this.pfValid = pfValid;
		
		// -- Matière Première
		this.mpNumLot = mpNumLot;
		this.mpCodeArticle = mpCodeArticle;
		this.mpLibelleArticle = mpLibelleArticle;
		this.mpQte1 = mpQte1;
		this.mpQte2 = mpQte2;
		this.mpUnite1 = mpUnite1;
		this.mpUnite2 = mpUnite2;
		this.mpDateFabrication = mpDateFabrication;
		this.mpDluo = mpDluo;
		this.mpCodeEntrepot = mpCodeEntrepot;
		this.mpLibelleEntrepot = mpLibelleEntrepot;
		this.mpQteEmplacement = mpQteEmplacement;
		this.mpControlleProbleme = mpControlleProbleme;
		this.mpFamille = mpFamille;
		this.mpEntree = mpEntree;
		this.mpSortie = mpSortie;
		this.mpValid = mpValid;
		
		// -- Bon de Reception
		this.brNumReception = brNumReception;
		this.brDateReception = brDateReception;
		this.brQteReception = brQteReception;
		this.brUniteReception = brUniteReception;
		this.brControlleProbleme = brControlleProbleme;
		this.brNomFournisseur = brNomFournisseur;
		this.brCodeFournisseur = brCodeFournisseur;
	}
	
	// constructeur pour editionCA
	public EditionEtatTracabilite(String pfNumLot, String pfCodeArticle,
			String pfLibelleArticle, BigDecimal pfQte1, BigDecimal pfQte2,
			String pfUnite1, String pfUnite2, String pfCodeFabrication,
			String pfLibelleFabrication, Date pfDateFabrication, Date pfDluo,
			String pfCodeEntrepot, String pfLibelleEntrepot,
			BigDecimal pfQteEmplacement, String pfControlleProbleme,
			boolean pfValid, String mpNumLot, String mpCodeArticle,
			String mpLibelleArticle, BigDecimal mpQte1, BigDecimal mpQte2,
			String mpUnite1, String mpUnite2, 
			String mpCodeEntrepot, String mpLibelleEntrepot,
			BigDecimal mpQteEmplacement, String mpControlleProbleme,
			String mpFamille, boolean mpValid, 
			String brNumReception, Date brDateReception,
			BigDecimal brQteReception, String brUniteReception,
			String brControlleProbleme, String brNomFournisseur,
			String brCodeFournisseur) {
		super();
		
		// -- Produit Fini
		this.pfNumLot = pfNumLot;
		this.pfCodeArticle = pfCodeArticle;
		this.pfLibelleArticle = pfLibelleArticle;
		this.pfQte1 = pfQte1;
		this.pfQte2 = pfQte2;
		this.pfUnite1 = pfUnite1;
		this.pfUnite2 = pfUnite2;
		this.pfCodeFabrication = pfCodeFabrication;
		this.pfLibelleFabrication = pfLibelleFabrication;
		this.pfDateFabrication = pfDateFabrication;
		this.pfDluo = pfDluo;
		this.pfCodeEntrepot = pfCodeEntrepot;
		this.pfLibelleEntrepot = pfLibelleEntrepot;
		this.pfQteEmplacement = pfQteEmplacement;
		this.pfControlleProbleme = pfControlleProbleme;
		this.pfValid = pfValid;
		
		// -- Matière Première
		this.mpNumLot = mpNumLot;
		this.mpCodeArticle = mpCodeArticle;
		this.mpLibelleArticle = mpLibelleArticle;
		this.mpQte1 = mpQte1;
		this.mpQte2 = mpQte2;
		this.mpUnite1 = mpUnite1;
		this.mpUnite2 = mpUnite2;
		this.mpCodeEntrepot = mpCodeEntrepot;
		this.mpLibelleEntrepot = mpLibelleEntrepot;
		this.mpQteEmplacement = mpQteEmplacement;
		this.mpControlleProbleme = mpControlleProbleme;
		this.mpFamille = mpFamille;
		this.mpValid = mpValid;
		
		// -- Bon de Reception
		this.brNumReception = brNumReception;
		this.brDateReception = brDateReception;
		this.brQteReception = brQteReception;
		this.brUniteReception = brUniteReception;
		this.brControlleProbleme = brControlleProbleme;
		this.brNomFournisseur = brNomFournisseur;
		this.brCodeFournisseur = brCodeFournisseur;
	}

	// sans pfQteEmplacement, pfControlleProbleme, mpQteEmplacement,
	// mpControlleProbleme, mpDateFabrication, pfValid, mpEntree, 
	// mpSortie, mpValid et brControlleProbleme
	public EditionEtatTracabilite(String pfNumLot, String pfCodeArticle,
			String pfLibelleArticle, BigDecimal pfQte1, BigDecimal pfQte2,
			String pfUnite1, String pfUnite2, String pfCodeFabrication,
			String pfLibelleFabrication, Date pfDateFabrication, Date pfDluo,
			String pfCodeEntrepot, String pfLibelleEntrepot,
			String mpNumLot, String mpCodeArticle,
			String mpLibelleArticle, BigDecimal mpQte1, BigDecimal mpQte2,
			String mpUnite1, String mpUnite2, 
			Date mpDluo, String mpCodeEntrepot, String mpLibelleEntrepot,
			String mpFamille, String brNumReception, Date brDateReception,
			BigDecimal brQteReception, String brUniteReception,
			String brNomFournisseur, String brCodeFournisseur) {
		super();
		
		this.pfNumLot = pfNumLot;
		this.pfCodeArticle = pfCodeArticle;
		this.pfLibelleArticle = pfLibelleArticle;
		this.pfQte1 = pfQte1;
		this.pfQte2 = pfQte2;
		this.pfUnite1 = pfUnite1;
		this.pfUnite2 = pfUnite2;
		this.pfCodeFabrication = pfCodeFabrication;
		this.pfLibelleFabrication = pfLibelleFabrication;
		this.pfDateFabrication = pfDateFabrication;
		this.pfDluo = pfDluo;
		this.pfCodeEntrepot = pfCodeEntrepot;
		this.pfLibelleEntrepot = pfLibelleEntrepot;
		
		this.mpNumLot = mpNumLot;
		this.mpCodeArticle = mpCodeArticle;
		this.mpLibelleArticle = mpLibelleArticle;
		this.mpQte1 = mpQte1;
		this.mpQte2 = mpQte2;
		this.mpUnite1 = mpUnite1;
		this.mpUnite2 = mpUnite2;
		this.mpDluo = mpDluo;
		this.mpCodeEntrepot = mpCodeEntrepot;
		this.mpLibelleEntrepot = mpLibelleEntrepot;
		this.mpFamille = mpFamille;
		
		this.brNumReception = brNumReception;
		this.brDateReception = brDateReception;
		this.brQteReception = brQteReception;
		this.brUniteReception = brUniteReception;
		this.brNomFournisseur = brNomFournisseur;
		this.brCodeFournisseur = brCodeFournisseur;
	}
	
	// que mp + br
	public EditionEtatTracabilite(String mpNumLot, String mpCodeArticle,
			String mpLibelleArticle, BigDecimal mpQte1, BigDecimal mpQte2,
			String mpUnite1, String mpUnite2, 
			Date mpDluo, String mpCodeEntrepot, String mpLibelleEntrepot,
			String mpFamille, String brNumReception, Date brDateReception,
			BigDecimal brQteReception, String brUniteReception,
			String brNomFournisseur, String brCodeFournisseur) {
		super();
		
		// mp
		this.mpNumLot = mpNumLot;
		this.mpCodeArticle = mpCodeArticle;
		this.mpLibelleArticle = mpLibelleArticle;
		this.mpQte1 = mpQte1;
		this.mpQte2 = mpQte2;
		this.mpUnite1 = mpUnite1;
		this.mpUnite2 = mpUnite2;
		this.mpDluo = mpDluo;
		this.mpCodeEntrepot = mpCodeEntrepot;
		this.mpLibelleEntrepot = mpLibelleEntrepot;
		this.mpFamille = mpFamille;
		
		// br
		this.brNumReception = brNumReception;
		this.brDateReception = brDateReception;
		this.brQteReception = brQteReception;
		this.brUniteReception = brUniteReception;
		this.brNomFournisseur = brNomFournisseur;
		this.brCodeFournisseur = brCodeFournisseur;
	}
	
	/** Getters et Setters **/
	
	public String getPfNumLot() {
		return pfNumLot;
	}
	public void setPfNumLot(String pfNumLot) {
		this.pfNumLot = pfNumLot;
	}
	public String getPfCodeArticle() {
		return pfCodeArticle;
	}
	public void setPfCodeArticle(String pfCodeArticle) {
		this.pfCodeArticle = pfCodeArticle;
	}
	public String getPfLibelleArticle() {
		return pfLibelleArticle;
	}
	public void setPfLibelleArticle(String pfLibelleArticle) {
		this.pfLibelleArticle = pfLibelleArticle;
	}
	public BigDecimal getPfQte1() {
		return pfQte1;
	}
	public void setPfQte1(BigDecimal pfQte1) {
		this.pfQte1 = pfQte1;
	}
	public BigDecimal getPfQte2() {
		return pfQte2;
	}
	public void setPfQte2(BigDecimal pfQte2) {
		this.pfQte2 = pfQte2;
	}
	public String getPfUnite1() {
		return pfUnite1;
	}
	public void setPfUnite1(String pfUnite1) {
		this.pfUnite1 = pfUnite1;
	}
	public String getPfUnite2() {
		return pfUnite2;
	}
	public void setPfUnite2(String pfUnite2) {
		this.pfUnite2 = pfUnite2;
	}
	public String getPfCodeFabrication() {
		return pfCodeFabrication;
	}
	public void setPfCodeFabrication(String pfCodeFabrication) {
		this.pfCodeFabrication = pfCodeFabrication;
	}
	public String getPfLibelleFabrication() {
		return pfLibelleFabrication;
	}
	public void setPfLibelleFabrication(String pfLibelleFabrication) {
		this.pfLibelleFabrication = pfLibelleFabrication;
	}
	public Date getPfDateFabrication() {
		return pfDateFabrication;
	}
	public void setPfDateFabrication(Date pfDateFabrication) {
		this.pfDateFabrication = pfDateFabrication;
	}
	public Date getPfDluo() {
		return pfDluo;
	}
	public void setPfDluo(Date pfDluo) {
		this.pfDluo = pfDluo;
	}
	public String getPfCodeEntrepot() {
		return pfCodeEntrepot;
	}
	public void setPfCodeEntrepot(String pfCodeEntrepot) {
		this.pfCodeEntrepot = pfCodeEntrepot;
	}
	public String getPfLibelleEntrepot() {
		return pfLibelleEntrepot;
	}
	public void setPfLibelleEntrepot(String pfLibelleEntrepot) {
		this.pfLibelleEntrepot = pfLibelleEntrepot;
	}
	public BigDecimal getPfQteEmplacement() {
		return pfQteEmplacement;
	}
	public void setPfQteEmplacement(BigDecimal pfQteEmplacement) {
		this.pfQteEmplacement = pfQteEmplacement;
	}
	public String getPfControlleProbleme() {
		return pfControlleProbleme;
	}
	public void setPfControlleProbleme(String pfControlleProbleme) {
		this.pfControlleProbleme = pfControlleProbleme;
	}
	public boolean isPfValid() {
		return pfValid;
	}
	public void setPfValid(boolean pfValid) {
		this.pfValid = pfValid;
	}
	public String getMpNumLot() {
		return mpNumLot;
	}
	public void setMpNumLot(String mpNumLot) {
		this.mpNumLot = mpNumLot;
	}
	public String getMpCodeArticle() {
		return mpCodeArticle;
	}
	public void setMpCodeArticle(String mpCodeArticle) {
		this.mpCodeArticle = mpCodeArticle;
	}
	public String getMpLibelleArticle() {
		return mpLibelleArticle;
	}
	public void setMpLibelleArticle(String mpLibelleArticle) {
		this.mpLibelleArticle = mpLibelleArticle;
	}
	public BigDecimal getMpQte1() {
		return mpQte1;
	}
	public void setMpQte1(BigDecimal mpQte1) {
		this.mpQte1 = mpQte1;
	}
	public BigDecimal getMpQte2() {
		return mpQte2;
	}
	public void setMpQte2(BigDecimal mpQte2) {
		this.mpQte2 = mpQte2;
	}
	public String getMpUnite1() {
		return mpUnite1;
	}
	public void setMpUnite1(String mpUnite1) {
		this.mpUnite1 = mpUnite1;
	}
	public String getMpUnite2() {
		return mpUnite2;
	}
	public void setMpUnite2(String mpUnite2) {
		this.mpUnite2 = mpUnite2;
	}
	public Date getMpDateFabrication() {
		return mpDateFabrication;
	}
	public void setMpDateFabrication(Date mpDateFabrication) {
		this.mpDateFabrication = mpDateFabrication;
	}
	public Date getMpDluo() {
		return mpDluo;
	}
	public void setMpDluo(Date mpDluo) {
		this.mpDluo = mpDluo;
	}
	public String getMpCodeEntrepot() {
		return mpCodeEntrepot;
	}
	public void setMpCodeEntrepot(String mpCodeEntrepot) {
		this.mpCodeEntrepot = mpCodeEntrepot;
	}
	public String getMpLibelleEntrepot() {
		return mpLibelleEntrepot;
	}
	public void setMpLibelleEntrepot(String mpLibelleEntrepot) {
		this.mpLibelleEntrepot = mpLibelleEntrepot;
	}
	public BigDecimal getMpQteEmplacement() {
		return mpQteEmplacement;
	}
	public void setMpQteEmplacement(BigDecimal mpQteEmplacement) {
		this.mpQteEmplacement = mpQteEmplacement;
	}
	public String getMpControlleProbleme() {
		return mpControlleProbleme;
	}
	public void setMpControlleProbleme(String mpControlleProbleme) {
		this.mpControlleProbleme = mpControlleProbleme;
	}
	public String getMpFamille() {
		return mpFamille;
	}
	public void setMpFamille(String mpFamille) {
		this.mpFamille = mpFamille;
	}
	public boolean isMpEntree() {
		return mpEntree;
	}
	public void setMpEntree(boolean mpEntree) {
		this.mpEntree = mpEntree;
	}
	public boolean isMpSortie() {
		return mpSortie;
	}
	public void setMpSortie(boolean mpSortie) {
		this.mpSortie = mpSortie;
	}
	public boolean isMpValid() {
		return mpValid;
	}
	public void setMpValid(boolean mpValid) {
		this.mpValid = mpValid;
	}
	public String getBrNumReception() {
		return brNumReception;
	}
	public void setBrNumReception(String brNumReception) {
		this.brNumReception = brNumReception;
	}
	public Date getBrDateReception() {
		return brDateReception;
	}
	public void setBrDateReception(Date brDateReception) {
		this.brDateReception = brDateReception;
	}
	public BigDecimal getBrQteReception() {
		return brQteReception;
	}
	public void setBrQteReception(BigDecimal brQteReception) {
		this.brQteReception = brQteReception;
	}
	public String getBrUniteReception() {
		return brUniteReception;
	}
	public void setBrUniteReception(String brUniteReception) {
		this.brUniteReception = brUniteReception;
	}
	public String getBrControlleProbleme() {
		return brControlleProbleme;
	}
	public void setBrControlleProbleme(String brControlleProbleme) {
		this.brControlleProbleme = brControlleProbleme;
	}
	public String getBrNomFournisseur() {
		return brNomFournisseur;
	}
	public void setBrNomFournisseur(String brNomFournisseur) {
		this.brNomFournisseur = brNomFournisseur;
	}
	public String getBrCodeFournisseur() {
		return brCodeFournisseur;
	}
	public void setBrCodeFournisseur(String brCodeFournisseur) {
		this.brCodeFournisseur = brCodeFournisseur;
	}

	public String getMouvEntrepot() {
		return mouvEntrepot;
	}

	public void setMouvEntrepot(String mouvEntrepot) {
		this.mouvEntrepot = mouvEntrepot;
	}

	public String getMouvLibEntrepot() {
		return mouvLibEntrepot;
	}

	public void setMouvLibEntrepot(String mouvLibEntrepot) {
		this.mouvLibEntrepot = mouvLibEntrepot;
	}

	public String getMouvCodeArticle() {
		return mouvCodeArticle;
	}

	public void setMouvCodeArticle(String mouvCodeArticle) {
		this.mouvCodeArticle = mouvCodeArticle;
	}

	public String getMouvNumLot() {
		return mouvNumLot;
	}

	public void setMouvNumLot(String mouvNumLot) {
		this.mouvNumLot = mouvNumLot;
	}

	public String getMouvLibelle() {
		return mouvLibelle;
	}

	public void setMouvLibelle(String mouvLibelle) {
		this.mouvLibelle = mouvLibelle;
	}

	public String getMouvLibelleArticle() {
		return mouvLibelleArticle;
	}

	public void setMouvLibelleArticle(String mouvLibelleArticle) {
		this.mouvLibelleArticle = mouvLibelleArticle;
	}

	public Date getMouveDate() {
		return mouveDate;
	}

	public void setMouveDate(Date mouveDate) {
		this.mouveDate = mouveDate;
	}

	public Date getMouveDluo() {
		return mouveDluo;
	}

	public void setMouveDluo(Date mouveDluo) {
		this.mouveDluo = mouveDluo;
	}

	public BigDecimal getMouveQte1() {
		return mouveQte1;
	}

	public void setMouveQte1(BigDecimal mouveQte1) {
		this.mouveQte1 = mouveQte1;
	}

	public String getMouveUnite1() {
		return mouveUnite1;
	}

	public void setMouveUnite1(String mouveUnite1) {
		this.mouveUnite1 = mouveUnite1;
	}

	public BigDecimal getMouveQte2() {
		return mouveQte2;
	}

	public void setMouveQte2(BigDecimal mouveQte2) {
		this.mouveQte2 = mouveQte2;
	}

	public String getMouveUnite2() {
		return mouveUnite2;
	}

	public void setMouveUnite2(String mouveUnite2) {
		this.mouveUnite2 = mouveUnite2;
	}

	public BigDecimal getMouvQteDepart() {
		return mouvQteDepart;
	}

	public void setMouvQteDepart(BigDecimal mouvQteDepart) {
		this.mouvQteDepart = mouvQteDepart;
	}

	public BigDecimal getMouvQteEntree() {
		return mouvQteEntree;
	}

	public void setMouvQteEntree(BigDecimal mouvQteEntree) {
		this.mouvQteEntree = mouvQteEntree;
	}

	public BigDecimal getMouvQteSortie() {
		return mouvQteSortie;
	}

	public void setMouvQteSortie(BigDecimal mouvQteSortie) {
		this.mouvQteSortie = mouvQteSortie;
	}

	public BigDecimal getMouvQteCommande() {
		return mouvQteCommande;
	}

	public void setMouvQteCommande(BigDecimal mouvQteCommande) {
		this.mouvQteCommande = mouvQteCommande;
	}

	public BigDecimal getMouvQteDisponible() {
		return mouvQteDisponible;
	}

	public void setMouvQteDisponible(BigDecimal mouvQteDisponible) {
		this.mouvQteDisponible = mouvQteDisponible;
	}

	public String getMouveTypeFamille() {
		return mouveTypeFamille;
	}

	public void setMouveTypeFamille(String mouveTypeFamille) {
		this.mouveTypeFamille = mouveTypeFamille;
	}

	public String getMouveCodeDocument() {
		return mouveCodeDocument;
	}

	public void setMouveCodeDocument(String mouveCodeDocument) {
		this.mouveCodeDocument = mouveCodeDocument;
	}

	public String getMouveLibDocument() {
		return mouveLibDocument;
	}

	public void setMouveLibDocument(String mouveLibDocument) {
		this.mouveLibDocument = mouveLibDocument;
	}

	public String getNumLot() {
		return numLot;
	}

	public void setNumLot(String numLot) {
		this.numLot = numLot;
	}

	public Date getDateModif() {
		return dateModif;
	}

	public void setDateModif(Date dateModif) {
		this.dateModif = dateModif;
	}

	public String getCodeArticle() {
		return codeArticle;
	}

	public void setCodeArticle(String codeArticle) {
		this.codeArticle = codeArticle;
	}

	public String getLibelleGroupe() {
		return libelleGroupe;
	}

	public void setLibelleGroupe(String libelleGroupe) {
		this.libelleGroupe = libelleGroupe;
	}

	public String getLibelleConformite() {
		return libelleConformite;
	}

	public void setLibelleConformite(String libelleConformite) {
		this.libelleConformite = libelleConformite;
	}

	public String getResultat() {
		return resultat;
	}

	public void setResultat(String resultat) {
		this.resultat = resultat;
	}

	public String getActionCorrective() {
		return actionCorrective;
	}

	public void setActionCorrective(String actionCorrective) {
		this.actionCorrective = actionCorrective;
	}

	public boolean isEffectuee() {
		return effectuee;
	}

	public void setEffectuee(boolean effectuee) {
		this.effectuee = effectuee;
	}

	public String getLibelleLot() {
		return libelleLot;
	}

	public void setLibelleLot(String libelleLot) {
		this.libelleLot = libelleLot;
	}

	public String getCodeTDocument() {
		return codeTDocument;
	}

	public void setCodeTDocument(String codeTDocument) {
		this.codeTDocument = codeTDocument;
	}

	public String getTypeDocument() {
		return TypeDocument;
	}

	public void setTypeDocument(String typeDocument) {
		TypeDocument = typeDocument;
	}

	public String getCodeDocument() {
		return codeDocument;
	}

	public void setCodeDocument(String codeDocument) {
		this.codeDocument = codeDocument;
	}

	public String getDateModifStr() {
		return dateModifStr;
	}

	public void setDateModifStr(String dateModifStr) {
		this.dateModifStr = dateModifStr;
	}

	public String getExpressionVerifiee() {
		return expressionVerifiee;
	}

	public void setExpressionVerifiee(String expressionVerifiee) {
		this.expressionVerifiee = expressionVerifiee;
	}

	public boolean isForcage() {
		return forcage;
	}

	public void setForcage(boolean forcage) {
		this.forcage = forcage;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public BigDecimal getQte1() {
		return Qte1;
	}

	public void setQte1(BigDecimal qte1) {
		Qte1 = qte1;
	}

	public String getU1() {
		return U1;
	}

	public void setU1(String u1) {
		U1 = u1;
	}

}
