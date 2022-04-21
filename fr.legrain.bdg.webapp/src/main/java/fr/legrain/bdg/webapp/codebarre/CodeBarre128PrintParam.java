package fr.legrain.bdg.webapp.codebarre;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaLot;

public class CodeBarre128PrintParam implements Serializable {
	
	public static final String NOM_OBJET_EN_SESSION = "paramCodebarre128Print";
	
	private Map<String, TaArticle> listeCodebarreArticle;
	private Map<String, TaLot> listeCodebarreLot;
	
	private boolean afficherEAN128 = true;
	private boolean afficherEAN13 = false;
	private boolean afficherEAN14 = false;
	private boolean afficherQrCode = false;
	private boolean afficherDatamatrix = false;
	private boolean afficherDetail = false;
	
	private String codeArticle;
	private Date ddm;
	private Date dlc;
	private String eanArticle;
	private String eanSurembalage;
	private BigDecimal hauteurEnMetre;
	private BigDecimal largeurEnMetre;
	private BigDecimal longueurEnMetre;
	private String uniteHauteurEnMetre;
	private String uniteLargeurEnMetre;
	private String uniteLongueurEnMetre;
	private String lotFabrication;
	private BigDecimal poidsEnKg;
	private String unitePoidsEnKg;
	private BigDecimal quantite;
	private BigDecimal quantiteUnitaire;
	private BigDecimal surfaceEnMetreCarre;
	private BigDecimal volumeEnLitre;
	private String uniteSurfaceEnMetreCarre;
	private String uniteVolumeEnLitre;
	
	public Map<String, TaArticle> getListeCodebarreArticle() {
		return listeCodebarreArticle;
	}
	public void setListeCodebarreArticle(Map<String, TaArticle> listeCodebarreArticle) {
		this.listeCodebarreArticle = listeCodebarreArticle;
	}
	public Map<String, TaLot> getListeCodebarreLot() {
		return listeCodebarreLot;
	}
	public void setListeCodebarreLot(Map<String, TaLot> listeCodebarreLot) {
		this.listeCodebarreLot = listeCodebarreLot;
	}
	public boolean isAfficherEAN128() {
		return afficherEAN128;
	}
	public void setAfficherEAN128(boolean afficherEAN128) {
		this.afficherEAN128 = afficherEAN128;
	}
	public boolean isAfficherEAN13() {
		return afficherEAN13;
	}
	public void setAfficherEAN13(boolean afficherEAN13) {
		this.afficherEAN13 = afficherEAN13;
	}
	public boolean isAfficherEAN14() {
		return afficherEAN14;
	}
	public void setAfficherEAN14(boolean afficherEAN14) {
		this.afficherEAN14 = afficherEAN14;
	}
	public boolean isAfficherQrCode() {
		return afficherQrCode;
	}
	public void setAfficherQrCode(boolean afficherQrCode) {
		this.afficherQrCode = afficherQrCode;
	}
	public boolean isAfficherDatamatrix() {
		return afficherDatamatrix;
	}
	public void setAfficherDatamatrix(boolean afficherDatamatrix) {
		this.afficherDatamatrix = afficherDatamatrix;
	}
	public String getCodeArticle() {
		return codeArticle;
	}
	public void setCodeArticle(String codeArticle) {
		this.codeArticle = codeArticle;
	}
	public Date getDdm() {
		return ddm;
	}
	public void setDdm(Date ddm) {
		this.ddm = ddm;
	}
	public Date getDlc() {
		return dlc;
	}
	public void setDlc(Date dlc) {
		this.dlc = dlc;
	}
	public String getEanArticle() {
		return eanArticle;
	}
	public void setEanArticle(String eanArticle) {
		this.eanArticle = eanArticle;
	}
	public String getEanSurembalage() {
		return eanSurembalage;
	}
	public void setEanSurembalage(String eanSurembalage) {
		this.eanSurembalage = eanSurembalage;
	}
	public BigDecimal getHauteurEnMetre() {
		return hauteurEnMetre;
	}
	public void setHauteurEnMetre(BigDecimal hauteurEnMetre) {
		this.hauteurEnMetre = hauteurEnMetre;
	}
	public BigDecimal getLargeurEnMetre() {
		return largeurEnMetre;
	}
	public void setLargeurEnMetre(BigDecimal largeurEnMetre) {
		this.largeurEnMetre = largeurEnMetre;
	}
	public BigDecimal getLongueurEnMetre() {
		return longueurEnMetre;
	}
	public void setLongueurEnMetre(BigDecimal longueurEnMetre) {
		this.longueurEnMetre = longueurEnMetre;
	}
	public String getUniteHauteurEnMetre() {
		return uniteHauteurEnMetre;
	}
	public void setUniteHauteurEnMetre(String uniteHauteurEnMetre) {
		this.uniteHauteurEnMetre = uniteHauteurEnMetre;
	}
	public String getUniteLargeurEnMetre() {
		return uniteLargeurEnMetre;
	}
	public void setUniteLargeurEnMetre(String uniteLargeurEnMetre) {
		this.uniteLargeurEnMetre = uniteLargeurEnMetre;
	}
	public String getUniteLongueurEnMetre() {
		return uniteLongueurEnMetre;
	}
	public void setUniteLongueurEnMetre(String uniteLongueurEnMetre) {
		this.uniteLongueurEnMetre = uniteLongueurEnMetre;
	}
	public String getLotFabrication() {
		return lotFabrication;
	}
	public void setLotFabrication(String lotFabrication) {
		this.lotFabrication = lotFabrication;
	}
	public BigDecimal getPoidsEnKg() {
		return poidsEnKg;
	}
	public void setPoidsEnKg(BigDecimal poidsEnKg) {
		this.poidsEnKg = poidsEnKg;
	}
	public String getUnitePoidsEnKg() {
		return unitePoidsEnKg;
	}
	public void setUnitePoidsEnKg(String unitePoidsEnKg) {
		this.unitePoidsEnKg = unitePoidsEnKg;
	}
	public BigDecimal getQuantite() {
		return quantite;
	}
	public void setQuantite(BigDecimal quantite) {
		this.quantite = quantite;
	}
	public BigDecimal getQuantiteUnitaire() {
		return quantiteUnitaire;
	}
	public void setQuantiteUnitaire(BigDecimal quantiteUnitaire) {
		this.quantiteUnitaire = quantiteUnitaire;
	}
	public BigDecimal getSurfaceEnMetreCarre() {
		return surfaceEnMetreCarre;
	}
	public void setSurfaceEnMetreCarre(BigDecimal surfaceEnMetreCarre) {
		this.surfaceEnMetreCarre = surfaceEnMetreCarre;
	}
	public BigDecimal getVolumeEnLitre() {
		return volumeEnLitre;
	}
	public void setVolumeEnLitre(BigDecimal volumeEnLitre) {
		this.volumeEnLitre = volumeEnLitre;
	}
	public String getUniteSurfaceEnMetreCarre() {
		return uniteSurfaceEnMetreCarre;
	}
	public void setUniteSurfaceEnMetreCarre(String uniteSurfaceEnMetreCarre) {
		this.uniteSurfaceEnMetreCarre = uniteSurfaceEnMetreCarre;
	}
	public String getUniteVolumeEnLitre() {
		return uniteVolumeEnLitre;
	}
	public void setUniteVolumeEnLitre(String uniteVolumeEnLitre) {
		this.uniteVolumeEnLitre = uniteVolumeEnLitre;
	}
	public boolean isAfficherDetail() {
		return afficherDetail;
	}
	public void setAfficherDetail(boolean afficherDetail) {
		this.afficherDetail = afficherDetail;
	}
	
}
