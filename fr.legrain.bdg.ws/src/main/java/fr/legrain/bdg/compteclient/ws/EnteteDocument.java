
package fr.legrain.bdg.compteclient.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java pour enteteDocument complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="enteteDocument"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CODE_TIERS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DATE" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="ID_TIERS" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="LIBELLE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="OLD_CODE_TIERS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="mode" type="{http://service.bdg.legrain.fr/}enumModeObjet" minOccurs="0"/&gt;
 *         &lt;element name="tableEntete" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "enteteDocument", propOrder = {
    "code",
    "codetiers",
    "date",
    "idtiers",
    "libelle",
    "oldcodetiers",
    "mode",
    "tableEntete"
})
public abstract class EnteteDocument {

    @XmlElement(name = "CODE")
    protected String code;
    @XmlElement(name = "CODE_TIERS")
    protected String codetiers;
    @XmlElement(name = "DATE")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar date;
    @XmlElement(name = "ID_TIERS")
    protected Integer idtiers;
    @XmlElement(name = "LIBELLE")
    protected String libelle;
    @XmlElement(name = "OLD_CODE_TIERS")
    protected String oldcodetiers;
    @XmlSchemaType(name = "string")
    protected EnumModeObjet mode;
    protected String tableEntete;

    /**
     * Obtient la valeur de la propriété code.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODE() {
        return code;
    }

    /**
     * Définit la valeur de la propriété code.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODE(String value) {
        this.code = value;
    }

    /**
     * Obtient la valeur de la propriété codetiers.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODETIERS() {
        return codetiers;
    }

    /**
     * Définit la valeur de la propriété codetiers.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODETIERS(String value) {
        this.codetiers = value;
    }

    /**
     * Obtient la valeur de la propriété date.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDATE() {
        return date;
    }

    /**
     * Définit la valeur de la propriété date.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDATE(XMLGregorianCalendar value) {
        this.date = value;
    }

    /**
     * Obtient la valeur de la propriété idtiers.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIDTIERS() {
        return idtiers;
    }

    /**
     * Définit la valeur de la propriété idtiers.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIDTIERS(Integer value) {
        this.idtiers = value;
    }

    /**
     * Obtient la valeur de la propriété libelle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLIBELLE() {
        return libelle;
    }

    /**
     * Définit la valeur de la propriété libelle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLIBELLE(String value) {
        this.libelle = value;
    }

    /**
     * Obtient la valeur de la propriété oldcodetiers.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOLDCODETIERS() {
        return oldcodetiers;
    }

    /**
     * Définit la valeur de la propriété oldcodetiers.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOLDCODETIERS(String value) {
        this.oldcodetiers = value;
    }

    /**
     * Obtient la valeur de la propriété mode.
     * 
     * @return
     *     possible object is
     *     {@link EnumModeObjet }
     *     
     */
    public EnumModeObjet getMode() {
        return mode;
    }

    /**
     * Définit la valeur de la propriété mode.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumModeObjet }
     *     
     */
    public void setMode(EnumModeObjet value) {
        this.mode = value;
    }

    /**
     * Obtient la valeur de la propriété tableEntete.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTableEntete() {
        return tableEntete;
    }

    /**
     * Définit la valeur de la propriété tableEntete.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTableEntete(String value) {
        this.tableEntete = value;
    }

}
