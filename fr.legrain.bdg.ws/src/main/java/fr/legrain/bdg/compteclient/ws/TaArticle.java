
package fr.legrain.bdg.compteclient.ws;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java pour taArticle complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taArticle"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="actif" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="autoAlimenteFournisseurs" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="codeArticle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codeBarre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="commentaireArticle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="diversArticle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="gestionLot" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="hauteur" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="idArticle" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="idImport" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ipAcces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="largeur" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="libellecArticle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="libellelArticle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="longueur" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="matierePremiere" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="numcptArticle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="origineImport" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="paramDluo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="poids" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="produitFini" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="quandCree" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quandModif" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quiCree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quiModif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="stockMinArticle" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="taCatalogueWeb" type="{http://service.bdg.legrain.fr/}taCatalogueWeb" minOccurs="0"/&gt;
 *         &lt;element name="taCategorieArticles" type="{http://service.bdg.legrain.fr/}taCategorieArticle" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="taConditionnementsArticle" type="{http://service.bdg.legrain.fr/}taUnite" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="taConformites" type="{http://service.bdg.legrain.fr/}taConformite" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="taFamille" type="{http://service.bdg.legrain.fr/}taFamille" minOccurs="0"/&gt;
 *         &lt;element name="taFamilles" type="{http://service.bdg.legrain.fr/}taFamille" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="taFournisseurs" type="{http://service.bdg.legrain.fr/}taTiers" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="taImageArticle" type="{http://service.bdg.legrain.fr/}taImageArticle" minOccurs="0"/&gt;
 *         &lt;element name="taImageArticles" type="{http://service.bdg.legrain.fr/}taImageArticle" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="taLabelArticles" type="{http://service.bdg.legrain.fr/}taLabelArticle" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="taMarqueArticle" type="{http://service.bdg.legrain.fr/}taMarqueArticle" minOccurs="0"/&gt;
 *         &lt;element name="taNotes" type="{http://service.bdg.legrain.fr/}taNoteArticle" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="taPrix" type="{http://service.bdg.legrain.fr/}taPrix" minOccurs="0"/&gt;
 *         &lt;element name="taPrixes" type="{http://service.bdg.legrain.fr/}taPrix" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="taRTaTitreTransport" type="{http://service.bdg.legrain.fr/}taRTaTitreTransport" minOccurs="0"/&gt;
 *         &lt;element name="taRapportUnite" type="{http://service.bdg.legrain.fr/}taRapportUnite" minOccurs="0"/&gt;
 *         &lt;element name="taRapportUnites" type="{http://service.bdg.legrain.fr/}taRapportUnite" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="taRecetteArticle" type="{http://service.bdg.legrain.fr/}taRecette" minOccurs="0"/&gt;
 *         &lt;element name="taRefArticleFournisseurs" type="{http://service.bdg.legrain.fr/}taRefArticleFournisseur" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="taTTva" type="{http://service.bdg.legrain.fr/}taTTva" minOccurs="0"/&gt;
 *         &lt;element name="taTva" type="{http://service.bdg.legrain.fr/}taTva" minOccurs="0"/&gt;
 *         &lt;element name="taUnite1" type="{http://service.bdg.legrain.fr/}taUnite" minOccurs="0"/&gt;
 *         &lt;element name="taUniteReference" type="{http://service.bdg.legrain.fr/}taUnite" minOccurs="0"/&gt;
 *         &lt;element name="taUniteStock" type="{http://service.bdg.legrain.fr/}taUnite" minOccurs="0"/&gt;
 *         &lt;element name="utiliseDlc" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="versionObj" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "taArticle", propOrder = {
    "actif",
    "autoAlimenteFournisseurs",
    "codeArticle",
    "codeBarre",
    "commentaireArticle",
    "diversArticle",
    "gestionLot",
    "hauteur",
    "idArticle",
    "idImport",
    "ipAcces",
    "largeur",
    "libellecArticle",
    "libellelArticle",
    "longueur",
    "matierePremiere",
    "numcptArticle",
    "origineImport",
    "paramDluo",
    "poids",
    "produitFini",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "stockMinArticle",
    "taCatalogueWeb",
    "taCategorieArticles",
    "taConditionnementsArticle",
    "taConformites",
    "taFamille",
    "taFamilles",
    "taFournisseurs",
    "taImageArticle",
    "taImageArticles",
    "taLabelArticles",
    "taMarqueArticle",
    "taNotes",
    "taPrix",
    "taPrixes",
    "taRTaTitreTransport",
    "taRapportUnite",
    "taRapportUnites",
    "taRecetteArticle",
    "taRefArticleFournisseurs",
    "taTTva",
    "taTva",
    "taUnite1",
    "taUniteReference",
    "taUniteStock",
    "utiliseDlc",
    "version",
    "versionObj"
})
public class TaArticle {

    protected Integer actif;
    protected Boolean autoAlimenteFournisseurs;
    protected String codeArticle;
    protected String codeBarre;
    protected String commentaireArticle;
    protected String diversArticle;
    protected Boolean gestionLot;
    protected BigDecimal hauteur;
    protected int idArticle;
    protected String idImport;
    protected String ipAcces;
    protected BigDecimal largeur;
    protected String libellecArticle;
    protected String libellelArticle;
    protected BigDecimal longueur;
    protected Boolean matierePremiere;
    protected String numcptArticle;
    protected String origineImport;
    protected String paramDluo;
    protected BigDecimal poids;
    protected Boolean produitFini;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
    protected BigDecimal stockMinArticle;
    protected TaCatalogueWeb taCatalogueWeb;
    @XmlElement(nillable = true)
    protected List<TaCategorieArticle> taCategorieArticles;
    @XmlElement(nillable = true)
    protected List<TaUnite> taConditionnementsArticle;
    @XmlElement(nillable = true)
    protected List<TaConformite> taConformites;
    protected TaFamille taFamille;
    @XmlElement(nillable = true)
    protected List<TaFamille> taFamilles;
    @XmlElement(nillable = true)
    protected List<TaTiers> taFournisseurs;
    protected TaImageArticle taImageArticle;
    @XmlElement(nillable = true)
    protected List<TaImageArticle> taImageArticles;
    @XmlElement(nillable = true)
    protected List<TaLabelArticle> taLabelArticles;
    protected TaMarqueArticle taMarqueArticle;
    @XmlElement(nillable = true)
    protected List<TaNoteArticle> taNotes;
    protected TaPrix taPrix;
    @XmlElement(nillable = true)
    protected List<TaPrix> taPrixes;
    protected TaRTaTitreTransport taRTaTitreTransport;
    protected TaRapportUnite taRapportUnite;
    @XmlElement(nillable = true)
    protected List<TaRapportUnite> taRapportUnites;
    protected TaRecette taRecetteArticle;
    @XmlElement(nillable = true)
    protected List<TaRefArticleFournisseur> taRefArticleFournisseurs;
    protected TaTTva taTTva;
    protected TaTva taTva;
    protected TaUnite taUnite1;
    protected TaUnite taUniteReference;
    protected TaUnite taUniteStock;
    protected Boolean utiliseDlc;
    protected String version;
    protected Integer versionObj;

    /**
     * Obtient la valeur de la propriété actif.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getActif() {
        return actif;
    }

    /**
     * Définit la valeur de la propriété actif.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setActif(Integer value) {
        this.actif = value;
    }

    /**
     * Obtient la valeur de la propriété autoAlimenteFournisseurs.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAutoAlimenteFournisseurs() {
        return autoAlimenteFournisseurs;
    }

    /**
     * Définit la valeur de la propriété autoAlimenteFournisseurs.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAutoAlimenteFournisseurs(Boolean value) {
        this.autoAlimenteFournisseurs = value;
    }

    /**
     * Obtient la valeur de la propriété codeArticle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeArticle() {
        return codeArticle;
    }

    /**
     * Définit la valeur de la propriété codeArticle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeArticle(String value) {
        this.codeArticle = value;
    }

    /**
     * Obtient la valeur de la propriété codeBarre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeBarre() {
        return codeBarre;
    }

    /**
     * Définit la valeur de la propriété codeBarre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeBarre(String value) {
        this.codeBarre = value;
    }

    /**
     * Obtient la valeur de la propriété commentaireArticle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCommentaireArticle() {
        return commentaireArticle;
    }

    /**
     * Définit la valeur de la propriété commentaireArticle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCommentaireArticle(String value) {
        this.commentaireArticle = value;
    }

    /**
     * Obtient la valeur de la propriété diversArticle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDiversArticle() {
        return diversArticle;
    }

    /**
     * Définit la valeur de la propriété diversArticle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDiversArticle(String value) {
        this.diversArticle = value;
    }

    /**
     * Obtient la valeur de la propriété gestionLot.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isGestionLot() {
        return gestionLot;
    }

    /**
     * Définit la valeur de la propriété gestionLot.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setGestionLot(Boolean value) {
        this.gestionLot = value;
    }

    /**
     * Obtient la valeur de la propriété hauteur.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getHauteur() {
        return hauteur;
    }

    /**
     * Définit la valeur de la propriété hauteur.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setHauteur(BigDecimal value) {
        this.hauteur = value;
    }

    /**
     * Obtient la valeur de la propriété idArticle.
     * 
     */
    public int getIdArticle() {
        return idArticle;
    }

    /**
     * Définit la valeur de la propriété idArticle.
     * 
     */
    public void setIdArticle(int value) {
        this.idArticle = value;
    }

    /**
     * Obtient la valeur de la propriété idImport.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdImport() {
        return idImport;
    }

    /**
     * Définit la valeur de la propriété idImport.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdImport(String value) {
        this.idImport = value;
    }

    /**
     * Obtient la valeur de la propriété ipAcces.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIpAcces() {
        return ipAcces;
    }

    /**
     * Définit la valeur de la propriété ipAcces.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIpAcces(String value) {
        this.ipAcces = value;
    }

    /**
     * Obtient la valeur de la propriété largeur.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLargeur() {
        return largeur;
    }

    /**
     * Définit la valeur de la propriété largeur.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLargeur(BigDecimal value) {
        this.largeur = value;
    }

    /**
     * Obtient la valeur de la propriété libellecArticle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLibellecArticle() {
        return libellecArticle;
    }

    /**
     * Définit la valeur de la propriété libellecArticle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLibellecArticle(String value) {
        this.libellecArticle = value;
    }

    /**
     * Obtient la valeur de la propriété libellelArticle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLibellelArticle() {
        return libellelArticle;
    }

    /**
     * Définit la valeur de la propriété libellelArticle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLibellelArticle(String value) {
        this.libellelArticle = value;
    }

    /**
     * Obtient la valeur de la propriété longueur.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLongueur() {
        return longueur;
    }

    /**
     * Définit la valeur de la propriété longueur.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLongueur(BigDecimal value) {
        this.longueur = value;
    }

    /**
     * Obtient la valeur de la propriété matierePremiere.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isMatierePremiere() {
        return matierePremiere;
    }

    /**
     * Définit la valeur de la propriété matierePremiere.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMatierePremiere(Boolean value) {
        this.matierePremiere = value;
    }

    /**
     * Obtient la valeur de la propriété numcptArticle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumcptArticle() {
        return numcptArticle;
    }

    /**
     * Définit la valeur de la propriété numcptArticle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumcptArticle(String value) {
        this.numcptArticle = value;
    }

    /**
     * Obtient la valeur de la propriété origineImport.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrigineImport() {
        return origineImport;
    }

    /**
     * Définit la valeur de la propriété origineImport.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrigineImport(String value) {
        this.origineImport = value;
    }

    /**
     * Obtient la valeur de la propriété paramDluo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParamDluo() {
        return paramDluo;
    }

    /**
     * Définit la valeur de la propriété paramDluo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParamDluo(String value) {
        this.paramDluo = value;
    }

    /**
     * Obtient la valeur de la propriété poids.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPoids() {
        return poids;
    }

    /**
     * Définit la valeur de la propriété poids.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPoids(BigDecimal value) {
        this.poids = value;
    }

    /**
     * Obtient la valeur de la propriété produitFini.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isProduitFini() {
        return produitFini;
    }

    /**
     * Définit la valeur de la propriété produitFini.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setProduitFini(Boolean value) {
        this.produitFini = value;
    }

    /**
     * Obtient la valeur de la propriété quandCree.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getQuandCree() {
        return quandCree;
    }

    /**
     * Définit la valeur de la propriété quandCree.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setQuandCree(XMLGregorianCalendar value) {
        this.quandCree = value;
    }

    /**
     * Obtient la valeur de la propriété quandModif.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getQuandModif() {
        return quandModif;
    }

    /**
     * Définit la valeur de la propriété quandModif.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setQuandModif(XMLGregorianCalendar value) {
        this.quandModif = value;
    }

    /**
     * Obtient la valeur de la propriété quiCree.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQuiCree() {
        return quiCree;
    }

    /**
     * Définit la valeur de la propriété quiCree.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQuiCree(String value) {
        this.quiCree = value;
    }

    /**
     * Obtient la valeur de la propriété quiModif.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQuiModif() {
        return quiModif;
    }

    /**
     * Définit la valeur de la propriété quiModif.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQuiModif(String value) {
        this.quiModif = value;
    }

    /**
     * Obtient la valeur de la propriété stockMinArticle.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getStockMinArticle() {
        return stockMinArticle;
    }

    /**
     * Définit la valeur de la propriété stockMinArticle.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setStockMinArticle(BigDecimal value) {
        this.stockMinArticle = value;
    }

    /**
     * Obtient la valeur de la propriété taCatalogueWeb.
     * 
     * @return
     *     possible object is
     *     {@link TaCatalogueWeb }
     *     
     */
    public TaCatalogueWeb getTaCatalogueWeb() {
        return taCatalogueWeb;
    }

    /**
     * Définit la valeur de la propriété taCatalogueWeb.
     * 
     * @param value
     *     allowed object is
     *     {@link TaCatalogueWeb }
     *     
     */
    public void setTaCatalogueWeb(TaCatalogueWeb value) {
        this.taCatalogueWeb = value;
    }

    /**
     * Gets the value of the taCategorieArticles property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taCategorieArticles property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaCategorieArticles().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaCategorieArticle }
     * 
     * 
     */
    public List<TaCategorieArticle> getTaCategorieArticles() {
        if (taCategorieArticles == null) {
            taCategorieArticles = new ArrayList<TaCategorieArticle>();
        }
        return this.taCategorieArticles;
    }

    /**
     * Gets the value of the taConditionnementsArticle property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taConditionnementsArticle property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaConditionnementsArticle().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaUnite }
     * 
     * 
     */
    public List<TaUnite> getTaConditionnementsArticle() {
        if (taConditionnementsArticle == null) {
            taConditionnementsArticle = new ArrayList<TaUnite>();
        }
        return this.taConditionnementsArticle;
    }

    /**
     * Gets the value of the taConformites property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taConformites property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaConformites().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaConformite }
     * 
     * 
     */
    public List<TaConformite> getTaConformites() {
        if (taConformites == null) {
            taConformites = new ArrayList<TaConformite>();
        }
        return this.taConformites;
    }

    /**
     * Obtient la valeur de la propriété taFamille.
     * 
     * @return
     *     possible object is
     *     {@link TaFamille }
     *     
     */
    public TaFamille getTaFamille() {
        return taFamille;
    }

    /**
     * Définit la valeur de la propriété taFamille.
     * 
     * @param value
     *     allowed object is
     *     {@link TaFamille }
     *     
     */
    public void setTaFamille(TaFamille value) {
        this.taFamille = value;
    }

    /**
     * Gets the value of the taFamilles property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taFamilles property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaFamilles().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaFamille }
     * 
     * 
     */
    public List<TaFamille> getTaFamilles() {
        if (taFamilles == null) {
            taFamilles = new ArrayList<TaFamille>();
        }
        return this.taFamilles;
    }

    /**
     * Gets the value of the taFournisseurs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taFournisseurs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaFournisseurs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaTiers }
     * 
     * 
     */
    public List<TaTiers> getTaFournisseurs() {
        if (taFournisseurs == null) {
            taFournisseurs = new ArrayList<TaTiers>();
        }
        return this.taFournisseurs;
    }

    /**
     * Obtient la valeur de la propriété taImageArticle.
     * 
     * @return
     *     possible object is
     *     {@link TaImageArticle }
     *     
     */
    public TaImageArticle getTaImageArticle() {
        return taImageArticle;
    }

    /**
     * Définit la valeur de la propriété taImageArticle.
     * 
     * @param value
     *     allowed object is
     *     {@link TaImageArticle }
     *     
     */
    public void setTaImageArticle(TaImageArticle value) {
        this.taImageArticle = value;
    }

    /**
     * Gets the value of the taImageArticles property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taImageArticles property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaImageArticles().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaImageArticle }
     * 
     * 
     */
    public List<TaImageArticle> getTaImageArticles() {
        if (taImageArticles == null) {
            taImageArticles = new ArrayList<TaImageArticle>();
        }
        return this.taImageArticles;
    }

    /**
     * Gets the value of the taLabelArticles property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taLabelArticles property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaLabelArticles().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaLabelArticle }
     * 
     * 
     */
    public List<TaLabelArticle> getTaLabelArticles() {
        if (taLabelArticles == null) {
            taLabelArticles = new ArrayList<TaLabelArticle>();
        }
        return this.taLabelArticles;
    }

    /**
     * Obtient la valeur de la propriété taMarqueArticle.
     * 
     * @return
     *     possible object is
     *     {@link TaMarqueArticle }
     *     
     */
    public TaMarqueArticle getTaMarqueArticle() {
        return taMarqueArticle;
    }

    /**
     * Définit la valeur de la propriété taMarqueArticle.
     * 
     * @param value
     *     allowed object is
     *     {@link TaMarqueArticle }
     *     
     */
    public void setTaMarqueArticle(TaMarqueArticle value) {
        this.taMarqueArticle = value;
    }

    /**
     * Gets the value of the taNotes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taNotes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaNotes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaNoteArticle }
     * 
     * 
     */
    public List<TaNoteArticle> getTaNotes() {
        if (taNotes == null) {
            taNotes = new ArrayList<TaNoteArticle>();
        }
        return this.taNotes;
    }

    /**
     * Obtient la valeur de la propriété taPrix.
     * 
     * @return
     *     possible object is
     *     {@link TaPrix }
     *     
     */
    public TaPrix getTaPrix() {
        return taPrix;
    }

    /**
     * Définit la valeur de la propriété taPrix.
     * 
     * @param value
     *     allowed object is
     *     {@link TaPrix }
     *     
     */
    public void setTaPrix(TaPrix value) {
        this.taPrix = value;
    }

    /**
     * Gets the value of the taPrixes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taPrixes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaPrixes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaPrix }
     * 
     * 
     */
    public List<TaPrix> getTaPrixes() {
        if (taPrixes == null) {
            taPrixes = new ArrayList<TaPrix>();
        }
        return this.taPrixes;
    }

    /**
     * Obtient la valeur de la propriété taRTaTitreTransport.
     * 
     * @return
     *     possible object is
     *     {@link TaRTaTitreTransport }
     *     
     */
    public TaRTaTitreTransport getTaRTaTitreTransport() {
        return taRTaTitreTransport;
    }

    /**
     * Définit la valeur de la propriété taRTaTitreTransport.
     * 
     * @param value
     *     allowed object is
     *     {@link TaRTaTitreTransport }
     *     
     */
    public void setTaRTaTitreTransport(TaRTaTitreTransport value) {
        this.taRTaTitreTransport = value;
    }

    /**
     * Obtient la valeur de la propriété taRapportUnite.
     * 
     * @return
     *     possible object is
     *     {@link TaRapportUnite }
     *     
     */
    public TaRapportUnite getTaRapportUnite() {
        return taRapportUnite;
    }

    /**
     * Définit la valeur de la propriété taRapportUnite.
     * 
     * @param value
     *     allowed object is
     *     {@link TaRapportUnite }
     *     
     */
    public void setTaRapportUnite(TaRapportUnite value) {
        this.taRapportUnite = value;
    }

    /**
     * Gets the value of the taRapportUnites property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taRapportUnites property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaRapportUnites().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaRapportUnite }
     * 
     * 
     */
    public List<TaRapportUnite> getTaRapportUnites() {
        if (taRapportUnites == null) {
            taRapportUnites = new ArrayList<TaRapportUnite>();
        }
        return this.taRapportUnites;
    }

    /**
     * Obtient la valeur de la propriété taRecetteArticle.
     * 
     * @return
     *     possible object is
     *     {@link TaRecette }
     *     
     */
    public TaRecette getTaRecetteArticle() {
        return taRecetteArticle;
    }

    /**
     * Définit la valeur de la propriété taRecetteArticle.
     * 
     * @param value
     *     allowed object is
     *     {@link TaRecette }
     *     
     */
    public void setTaRecetteArticle(TaRecette value) {
        this.taRecetteArticle = value;
    }

    /**
     * Gets the value of the taRefArticleFournisseurs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taRefArticleFournisseurs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaRefArticleFournisseurs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaRefArticleFournisseur }
     * 
     * 
     */
    public List<TaRefArticleFournisseur> getTaRefArticleFournisseurs() {
        if (taRefArticleFournisseurs == null) {
            taRefArticleFournisseurs = new ArrayList<TaRefArticleFournisseur>();
        }
        return this.taRefArticleFournisseurs;
    }

    /**
     * Obtient la valeur de la propriété taTTva.
     * 
     * @return
     *     possible object is
     *     {@link TaTTva }
     *     
     */
    public TaTTva getTaTTva() {
        return taTTva;
    }

    /**
     * Définit la valeur de la propriété taTTva.
     * 
     * @param value
     *     allowed object is
     *     {@link TaTTva }
     *     
     */
    public void setTaTTva(TaTTva value) {
        this.taTTva = value;
    }

    /**
     * Obtient la valeur de la propriété taTva.
     * 
     * @return
     *     possible object is
     *     {@link TaTva }
     *     
     */
    public TaTva getTaTva() {
        return taTva;
    }

    /**
     * Définit la valeur de la propriété taTva.
     * 
     * @param value
     *     allowed object is
     *     {@link TaTva }
     *     
     */
    public void setTaTva(TaTva value) {
        this.taTva = value;
    }

    /**
     * Obtient la valeur de la propriété taUnite1.
     * 
     * @return
     *     possible object is
     *     {@link TaUnite }
     *     
     */
    public TaUnite getTaUnite1() {
        return taUnite1;
    }

    /**
     * Définit la valeur de la propriété taUnite1.
     * 
     * @param value
     *     allowed object is
     *     {@link TaUnite }
     *     
     */
    public void setTaUnite1(TaUnite value) {
        this.taUnite1 = value;
    }

    /**
     * Obtient la valeur de la propriété taUniteReference.
     * 
     * @return
     *     possible object is
     *     {@link TaUnite }
     *     
     */
    public TaUnite getTaUniteReference() {
        return taUniteReference;
    }

    /**
     * Définit la valeur de la propriété taUniteReference.
     * 
     * @param value
     *     allowed object is
     *     {@link TaUnite }
     *     
     */
    public void setTaUniteReference(TaUnite value) {
        this.taUniteReference = value;
    }

    /**
     * Obtient la valeur de la propriété taUniteStock.
     * 
     * @return
     *     possible object is
     *     {@link TaUnite }
     *     
     */
    public TaUnite getTaUniteStock() {
        return taUniteStock;
    }

    /**
     * Définit la valeur de la propriété taUniteStock.
     * 
     * @param value
     *     allowed object is
     *     {@link TaUnite }
     *     
     */
    public void setTaUniteStock(TaUnite value) {
        this.taUniteStock = value;
    }

    /**
     * Obtient la valeur de la propriété utiliseDlc.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isUtiliseDlc() {
        return utiliseDlc;
    }

    /**
     * Définit la valeur de la propriété utiliseDlc.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setUtiliseDlc(Boolean value) {
        this.utiliseDlc = value;
    }

    /**
     * Obtient la valeur de la propriété version.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Définit la valeur de la propriété version.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Obtient la valeur de la propriété versionObj.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getVersionObj() {
        return versionObj;
    }

    /**
     * Définit la valeur de la propriété versionObj.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setVersionObj(Integer value) {
        this.versionObj = value;
    }

}
