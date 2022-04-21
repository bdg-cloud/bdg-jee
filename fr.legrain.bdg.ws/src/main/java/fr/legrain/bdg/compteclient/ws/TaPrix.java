
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
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;


/**
 * <p>Classe Java pour taPrix complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taPrix"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="idImport" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idPrix" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="idRefPrix" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="ipAcces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="origineImport" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="prixPrix" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="prixttcPrix" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="quandCree" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quandModif" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quiCree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quiModif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="taArticle" type="{http://service.bdg.legrain.fr/}taArticle" minOccurs="0"/&gt;
 *         &lt;element name="taRPrixes" type="{http://service.bdg.legrain.fr/}taRPrix" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="taRPrixesTiers" type="{http://service.bdg.legrain.fr/}taRPrixTiers" maxOccurs="unbounded" minOccurs="0"/&gt;
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
@XmlType(name = "taPrix", propOrder = {
    "idImport",
    "idPrix",
    "idRefPrix",
    "ipAcces",
    "origineImport",
    "prixPrix",
    "prixttcPrix",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "taArticle",
    "taRPrixes",
    "taRPrixesTiers",
    "version",
    "versionObj"
})
public class TaPrix {

    protected String idImport;
    protected int idPrix;
    protected Boolean idRefPrix;
    protected String ipAcces;
    protected String origineImport;
    protected BigDecimal prixPrix;
    protected BigDecimal prixttcPrix;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
    @XmlInverseReference(mappedBy = "taPrixes")
    @XmlElement
    protected TaArticle taArticle;
    @XmlElement(nillable = true)
    protected List<TaRPrix> taRPrixes;
    @XmlElement(nillable = true)
    protected List<TaRPrixTiers> taRPrixesTiers;
    protected String version;
    protected Integer versionObj;

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
     * Obtient la valeur de la propriété idPrix.
     * 
     */
    public int getIdPrix() {
        return idPrix;
    }

    /**
     * Définit la valeur de la propriété idPrix.
     * 
     */
    public void setIdPrix(int value) {
        this.idPrix = value;
    }

    /**
     * Obtient la valeur de la propriété idRefPrix.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIdRefPrix() {
        return idRefPrix;
    }

    /**
     * Définit la valeur de la propriété idRefPrix.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIdRefPrix(Boolean value) {
        this.idRefPrix = value;
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
     * Obtient la valeur de la propriété prixPrix.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPrixPrix() {
        return prixPrix;
    }

    /**
     * Définit la valeur de la propriété prixPrix.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPrixPrix(BigDecimal value) {
        this.prixPrix = value;
    }

    /**
     * Obtient la valeur de la propriété prixttcPrix.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPrixttcPrix() {
        return prixttcPrix;
    }

    /**
     * Définit la valeur de la propriété prixttcPrix.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPrixttcPrix(BigDecimal value) {
        this.prixttcPrix = value;
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
     * Obtient la valeur de la propriété taArticle.
     * 
     * @return
     *     possible object is
     *     {@link TaArticle }
     *     
     */
    public TaArticle getTaArticle() {
        return taArticle;
    }

    /**
     * Définit la valeur de la propriété taArticle.
     * 
     * @param value
     *     allowed object is
     *     {@link TaArticle }
     *     
     */
    public void setTaArticle(TaArticle value) {
        this.taArticle = value;
    }

    /**
     * Gets the value of the taRPrixes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taRPrixes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaRPrixes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaRPrix }
     * 
     * 
     */
    public List<TaRPrix> getTaRPrixes() {
        if (taRPrixes == null) {
            taRPrixes = new ArrayList<TaRPrix>();
        }
        return this.taRPrixes;
    }

    /**
     * Gets the value of the taRPrixesTiers property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taRPrixesTiers property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaRPrixesTiers().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaRPrixTiers }
     * 
     * 
     */
    public List<TaRPrixTiers> getTaRPrixesTiers() {
        if (taRPrixesTiers == null) {
            taRPrixesTiers = new ArrayList<TaRPrixTiers>();
        }
        return this.taRPrixesTiers;
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
