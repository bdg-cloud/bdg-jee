
package fr.legrain.bdg.compteclient.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java pour taInfoJuridique complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taInfoJuridique"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="apeInfoJuridique" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="capitalInfoJuridique" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idInfoJuridique" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ipAcces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quandCree" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quandModif" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quiCree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quiModif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="rcsInfoJuridique" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="siretInfoJuridique" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "taInfoJuridique", propOrder = {
    "apeInfoJuridique",
    "capitalInfoJuridique",
    "idInfoJuridique",
    "ipAcces",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "rcsInfoJuridique",
    "siretInfoJuridique",
    "version",
    "versionObj"
})
public class TaInfoJuridique {

    protected String apeInfoJuridique;
    protected String capitalInfoJuridique;
    protected int idInfoJuridique;
    protected String ipAcces;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
    protected String rcsInfoJuridique;
    protected String siretInfoJuridique;
    protected String version;
    protected Integer versionObj;

    /**
     * Obtient la valeur de la propriété apeInfoJuridique.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApeInfoJuridique() {
        return apeInfoJuridique;
    }

    /**
     * Définit la valeur de la propriété apeInfoJuridique.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApeInfoJuridique(String value) {
        this.apeInfoJuridique = value;
    }

    /**
     * Obtient la valeur de la propriété capitalInfoJuridique.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCapitalInfoJuridique() {
        return capitalInfoJuridique;
    }

    /**
     * Définit la valeur de la propriété capitalInfoJuridique.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCapitalInfoJuridique(String value) {
        this.capitalInfoJuridique = value;
    }

    /**
     * Obtient la valeur de la propriété idInfoJuridique.
     * 
     */
    public int getIdInfoJuridique() {
        return idInfoJuridique;
    }

    /**
     * Définit la valeur de la propriété idInfoJuridique.
     * 
     */
    public void setIdInfoJuridique(int value) {
        this.idInfoJuridique = value;
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
     * Obtient la valeur de la propriété rcsInfoJuridique.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRcsInfoJuridique() {
        return rcsInfoJuridique;
    }

    /**
     * Définit la valeur de la propriété rcsInfoJuridique.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRcsInfoJuridique(String value) {
        this.rcsInfoJuridique = value;
    }

    /**
     * Obtient la valeur de la propriété siretInfoJuridique.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSiretInfoJuridique() {
        return siretInfoJuridique;
    }

    /**
     * Définit la valeur de la propriété siretInfoJuridique.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSiretInfoJuridique(String value) {
        this.siretInfoJuridique = value;
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
