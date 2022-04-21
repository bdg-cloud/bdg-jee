
package fr.legrain.bdg.compteclient.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java pour taCompl complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taCompl"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="accise" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ics" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idCompl" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ipAcces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="numAgrementSanitaire" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quandCree" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quandModif" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quiCree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quiModif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="siretCompl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tvaIComCompl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "taCompl", propOrder = {
    "accise",
    "ics",
    "idCompl",
    "ipAcces",
    "numAgrementSanitaire",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "siretCompl",
    "tvaIComCompl",
    "version",
    "versionObj"
})
public class TaCompl {

    protected String accise;
    protected String ics;
    protected int idCompl;
    protected String ipAcces;
    protected String numAgrementSanitaire;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
    protected String siretCompl;
    protected String tvaIComCompl;
    protected String version;
    protected Integer versionObj;

    /**
     * Obtient la valeur de la propriété accise.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccise() {
        return accise;
    }

    /**
     * Définit la valeur de la propriété accise.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccise(String value) {
        this.accise = value;
    }

    /**
     * Obtient la valeur de la propriété ics.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIcs() {
        return ics;
    }

    /**
     * Définit la valeur de la propriété ics.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIcs(String value) {
        this.ics = value;
    }

    /**
     * Obtient la valeur de la propriété idCompl.
     * 
     */
    public int getIdCompl() {
        return idCompl;
    }

    /**
     * Définit la valeur de la propriété idCompl.
     * 
     */
    public void setIdCompl(int value) {
        this.idCompl = value;
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
     * Obtient la valeur de la propriété numAgrementSanitaire.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumAgrementSanitaire() {
        return numAgrementSanitaire;
    }

    /**
     * Définit la valeur de la propriété numAgrementSanitaire.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumAgrementSanitaire(String value) {
        this.numAgrementSanitaire = value;
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
     * Obtient la valeur de la propriété siretCompl.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSiretCompl() {
        return siretCompl;
    }

    /**
     * Définit la valeur de la propriété siretCompl.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSiretCompl(String value) {
        this.siretCompl = value;
    }

    /**
     * Obtient la valeur de la propriété tvaIComCompl.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTvaIComCompl() {
        return tvaIComCompl;
    }

    /**
     * Définit la valeur de la propriété tvaIComCompl.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTvaIComCompl(String value) {
        this.tvaIComCompl = value;
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
