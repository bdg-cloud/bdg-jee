
package fr.legrain.bdg.compteclient.ws;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java pour taCatalogueWeb complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taCatalogueWeb"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="descriptionLongueCatWeb" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="expediableCatalogueWeb" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="exportationCatalogueWeb" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="idCatalogueWeb" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ipAcces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nouveauteCatalogueWeb" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="promotionCatalogueWeb" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="promotionU2CatalogueWeb" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="quandCree" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quandModif" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quiCree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quiModif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="specialCatalogueWeb" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="urlRewritingCatalogueWeb" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "taCatalogueWeb", propOrder = {
    "descriptionLongueCatWeb",
    "expediableCatalogueWeb",
    "exportationCatalogueWeb",
    "idCatalogueWeb",
    "ipAcces",
    "nouveauteCatalogueWeb",
    "promotionCatalogueWeb",
    "promotionU2CatalogueWeb",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "specialCatalogueWeb",
    "urlRewritingCatalogueWeb",
    "version",
    "versionObj"
})
public class TaCatalogueWeb {

    protected String descriptionLongueCatWeb;
    protected Integer expediableCatalogueWeb;
    protected Integer exportationCatalogueWeb;
    protected int idCatalogueWeb;
    protected String ipAcces;
    protected Integer nouveauteCatalogueWeb;
    protected BigDecimal promotionCatalogueWeb;
    protected BigDecimal promotionU2CatalogueWeb;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
    protected Integer specialCatalogueWeb;
    protected String urlRewritingCatalogueWeb;
    protected String version;
    protected Integer versionObj;

    /**
     * Obtient la valeur de la propriété descriptionLongueCatWeb.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescriptionLongueCatWeb() {
        return descriptionLongueCatWeb;
    }

    /**
     * Définit la valeur de la propriété descriptionLongueCatWeb.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescriptionLongueCatWeb(String value) {
        this.descriptionLongueCatWeb = value;
    }

    /**
     * Obtient la valeur de la propriété expediableCatalogueWeb.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getExpediableCatalogueWeb() {
        return expediableCatalogueWeb;
    }

    /**
     * Définit la valeur de la propriété expediableCatalogueWeb.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setExpediableCatalogueWeb(Integer value) {
        this.expediableCatalogueWeb = value;
    }

    /**
     * Obtient la valeur de la propriété exportationCatalogueWeb.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getExportationCatalogueWeb() {
        return exportationCatalogueWeb;
    }

    /**
     * Définit la valeur de la propriété exportationCatalogueWeb.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setExportationCatalogueWeb(Integer value) {
        this.exportationCatalogueWeb = value;
    }

    /**
     * Obtient la valeur de la propriété idCatalogueWeb.
     * 
     */
    public int getIdCatalogueWeb() {
        return idCatalogueWeb;
    }

    /**
     * Définit la valeur de la propriété idCatalogueWeb.
     * 
     */
    public void setIdCatalogueWeb(int value) {
        this.idCatalogueWeb = value;
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
     * Obtient la valeur de la propriété nouveauteCatalogueWeb.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNouveauteCatalogueWeb() {
        return nouveauteCatalogueWeb;
    }

    /**
     * Définit la valeur de la propriété nouveauteCatalogueWeb.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNouveauteCatalogueWeb(Integer value) {
        this.nouveauteCatalogueWeb = value;
    }

    /**
     * Obtient la valeur de la propriété promotionCatalogueWeb.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPromotionCatalogueWeb() {
        return promotionCatalogueWeb;
    }

    /**
     * Définit la valeur de la propriété promotionCatalogueWeb.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPromotionCatalogueWeb(BigDecimal value) {
        this.promotionCatalogueWeb = value;
    }

    /**
     * Obtient la valeur de la propriété promotionU2CatalogueWeb.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPromotionU2CatalogueWeb() {
        return promotionU2CatalogueWeb;
    }

    /**
     * Définit la valeur de la propriété promotionU2CatalogueWeb.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPromotionU2CatalogueWeb(BigDecimal value) {
        this.promotionU2CatalogueWeb = value;
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
     * Obtient la valeur de la propriété specialCatalogueWeb.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSpecialCatalogueWeb() {
        return specialCatalogueWeb;
    }

    /**
     * Définit la valeur de la propriété specialCatalogueWeb.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSpecialCatalogueWeb(Integer value) {
        this.specialCatalogueWeb = value;
    }

    /**
     * Obtient la valeur de la propriété urlRewritingCatalogueWeb.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrlRewritingCatalogueWeb() {
        return urlRewritingCatalogueWeb;
    }

    /**
     * Définit la valeur de la propriété urlRewritingCatalogueWeb.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrlRewritingCatalogueWeb(String value) {
        this.urlRewritingCatalogueWeb = value;
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
