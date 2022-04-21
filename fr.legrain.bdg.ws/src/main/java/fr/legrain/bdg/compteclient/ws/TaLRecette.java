
package fr.legrain.bdg.compteclient.ws;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;


/**
 * <p>Classe Java pour taLRecette complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taLRecette"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="emplacementLDocument" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idLRecette" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ipAcces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="libLDocument" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="numLigneLDocument" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="qte2LDocument" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="qteLDocument" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="quandCree" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quandModif" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quiCree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quiModif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="taArticle" type="{http://service.bdg.legrain.fr/}taArticle" minOccurs="0"/&gt;
 *         &lt;element name="taRecette" type="{http://service.bdg.legrain.fr/}taRecette" minOccurs="0"/&gt;
 *         &lt;element name="u1LDocument" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="u2LDocument" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "taLRecette", propOrder = {
    "emplacementLDocument",
    "idLRecette",
    "ipAcces",
    "libLDocument",
    "numLigneLDocument",
    "qte2LDocument",
    "qteLDocument",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "taArticle",
    "taRecette",
    "u1LDocument",
    "u2LDocument",
    "version",
    "versionObj"
})
public class TaLRecette {

    protected String emplacementLDocument;
    protected int idLRecette;
    protected String ipAcces;
    protected String libLDocument;
    protected Integer numLigneLDocument;
    protected BigDecimal qte2LDocument;
    protected BigDecimal qteLDocument;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
    protected TaArticle taArticle;
    @XmlInverseReference(mappedBy = "lignes")
    @XmlElement
    protected TaRecette taRecette;
    protected String u1LDocument;
    protected String u2LDocument;
    protected String version;
    protected Integer versionObj;

    /**
     * Obtient la valeur de la propriété emplacementLDocument.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmplacementLDocument() {
        return emplacementLDocument;
    }

    /**
     * Définit la valeur de la propriété emplacementLDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmplacementLDocument(String value) {
        this.emplacementLDocument = value;
    }

    /**
     * Obtient la valeur de la propriété idLRecette.
     * 
     */
    public int getIdLRecette() {
        return idLRecette;
    }

    /**
     * Définit la valeur de la propriété idLRecette.
     * 
     */
    public void setIdLRecette(int value) {
        this.idLRecette = value;
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
     * Obtient la valeur de la propriété libLDocument.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLibLDocument() {
        return libLDocument;
    }

    /**
     * Définit la valeur de la propriété libLDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLibLDocument(String value) {
        this.libLDocument = value;
    }

    /**
     * Obtient la valeur de la propriété numLigneLDocument.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumLigneLDocument() {
        return numLigneLDocument;
    }

    /**
     * Définit la valeur de la propriété numLigneLDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumLigneLDocument(Integer value) {
        this.numLigneLDocument = value;
    }

    /**
     * Obtient la valeur de la propriété qte2LDocument.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getQte2LDocument() {
        return qte2LDocument;
    }

    /**
     * Définit la valeur de la propriété qte2LDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setQte2LDocument(BigDecimal value) {
        this.qte2LDocument = value;
    }

    /**
     * Obtient la valeur de la propriété qteLDocument.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getQteLDocument() {
        return qteLDocument;
    }

    /**
     * Définit la valeur de la propriété qteLDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setQteLDocument(BigDecimal value) {
        this.qteLDocument = value;
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
     * Obtient la valeur de la propriété taRecette.
     * 
     * @return
     *     possible object is
     *     {@link TaRecette }
     *     
     */
    public TaRecette getTaRecette() {
        return taRecette;
    }

    /**
     * Définit la valeur de la propriété taRecette.
     * 
     * @param value
     *     allowed object is
     *     {@link TaRecette }
     *     
     */
    public void setTaRecette(TaRecette value) {
        this.taRecette = value;
    }

    /**
     * Obtient la valeur de la propriété u1LDocument.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getU1LDocument() {
        return u1LDocument;
    }

    /**
     * Définit la valeur de la propriété u1LDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setU1LDocument(String value) {
        this.u1LDocument = value;
    }

    /**
     * Obtient la valeur de la propriété u2LDocument.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getU2LDocument() {
        return u2LDocument;
    }

    /**
     * Définit la valeur de la propriété u2LDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setU2LDocument(String value) {
        this.u2LDocument = value;
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
