
package fr.legrain.bdg.compteclient.ws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java pour taModeleConformite complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taModeleConformite"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ctrlBloquant" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="ctrlFacultatif" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="deuxiemeValeur" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idModeleConformite" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ipAcces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="libelleConformite" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quandCree" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quandModif" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quiCree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quiModif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="taGroupe" type="{http://service.bdg.legrain.fr/}taGroupe" minOccurs="0"/&gt;
 *         &lt;element name="taModeleBaremes" type="{http://service.bdg.legrain.fr/}taModeleBareme" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="taModeleGroupe" type="{http://service.bdg.legrain.fr/}taModeleGroupe" minOccurs="0"/&gt;
 *         &lt;element name="taTypeConformite" type="{http://service.bdg.legrain.fr/}taTypeConformite" minOccurs="0"/&gt;
 *         &lt;element name="valeurCalculee" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="valeurDefaut" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "taModeleConformite", propOrder = {
    "code",
    "ctrlBloquant",
    "ctrlFacultatif",
    "deuxiemeValeur",
    "idModeleConformite",
    "ipAcces",
    "libelleConformite",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "taGroupe",
    "taModeleBaremes",
    "taModeleGroupe",
    "taTypeConformite",
    "valeurCalculee",
    "valeurDefaut",
    "version",
    "versionObj"
})
public class TaModeleConformite {

    protected String code;
    protected Boolean ctrlBloquant;
    protected Boolean ctrlFacultatif;
    protected String deuxiemeValeur;
    protected int idModeleConformite;
    protected String ipAcces;
    protected String libelleConformite;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
    protected TaGroupe taGroupe;
    @XmlElement(nillable = true)
    protected List<TaModeleBareme> taModeleBaremes;
    protected TaModeleGroupe taModeleGroupe;
    protected TaTypeConformite taTypeConformite;
    protected String valeurCalculee;
    protected String valeurDefaut;
    protected String version;
    protected Integer versionObj;

    /**
     * Obtient la valeur de la propriété code.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCode() {
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
    public void setCode(String value) {
        this.code = value;
    }

    /**
     * Obtient la valeur de la propriété ctrlBloquant.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCtrlBloquant() {
        return ctrlBloquant;
    }

    /**
     * Définit la valeur de la propriété ctrlBloquant.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCtrlBloquant(Boolean value) {
        this.ctrlBloquant = value;
    }

    /**
     * Obtient la valeur de la propriété ctrlFacultatif.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCtrlFacultatif() {
        return ctrlFacultatif;
    }

    /**
     * Définit la valeur de la propriété ctrlFacultatif.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCtrlFacultatif(Boolean value) {
        this.ctrlFacultatif = value;
    }

    /**
     * Obtient la valeur de la propriété deuxiemeValeur.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeuxiemeValeur() {
        return deuxiemeValeur;
    }

    /**
     * Définit la valeur de la propriété deuxiemeValeur.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeuxiemeValeur(String value) {
        this.deuxiemeValeur = value;
    }

    /**
     * Obtient la valeur de la propriété idModeleConformite.
     * 
     */
    public int getIdModeleConformite() {
        return idModeleConformite;
    }

    /**
     * Définit la valeur de la propriété idModeleConformite.
     * 
     */
    public void setIdModeleConformite(int value) {
        this.idModeleConformite = value;
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
     * Obtient la valeur de la propriété libelleConformite.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLibelleConformite() {
        return libelleConformite;
    }

    /**
     * Définit la valeur de la propriété libelleConformite.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLibelleConformite(String value) {
        this.libelleConformite = value;
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
     * Obtient la valeur de la propriété taGroupe.
     * 
     * @return
     *     possible object is
     *     {@link TaGroupe }
     *     
     */
    public TaGroupe getTaGroupe() {
        return taGroupe;
    }

    /**
     * Définit la valeur de la propriété taGroupe.
     * 
     * @param value
     *     allowed object is
     *     {@link TaGroupe }
     *     
     */
    public void setTaGroupe(TaGroupe value) {
        this.taGroupe = value;
    }

    /**
     * Gets the value of the taModeleBaremes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taModeleBaremes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaModeleBaremes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaModeleBareme }
     * 
     * 
     */
    public List<TaModeleBareme> getTaModeleBaremes() {
        if (taModeleBaremes == null) {
            taModeleBaremes = new ArrayList<TaModeleBareme>();
        }
        return this.taModeleBaremes;
    }

    /**
     * Obtient la valeur de la propriété taModeleGroupe.
     * 
     * @return
     *     possible object is
     *     {@link TaModeleGroupe }
     *     
     */
    public TaModeleGroupe getTaModeleGroupe() {
        return taModeleGroupe;
    }

    /**
     * Définit la valeur de la propriété taModeleGroupe.
     * 
     * @param value
     *     allowed object is
     *     {@link TaModeleGroupe }
     *     
     */
    public void setTaModeleGroupe(TaModeleGroupe value) {
        this.taModeleGroupe = value;
    }

    /**
     * Obtient la valeur de la propriété taTypeConformite.
     * 
     * @return
     *     possible object is
     *     {@link TaTypeConformite }
     *     
     */
    public TaTypeConformite getTaTypeConformite() {
        return taTypeConformite;
    }

    /**
     * Définit la valeur de la propriété taTypeConformite.
     * 
     * @param value
     *     allowed object is
     *     {@link TaTypeConformite }
     *     
     */
    public void setTaTypeConformite(TaTypeConformite value) {
        this.taTypeConformite = value;
    }

    /**
     * Obtient la valeur de la propriété valeurCalculee.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValeurCalculee() {
        return valeurCalculee;
    }

    /**
     * Définit la valeur de la propriété valeurCalculee.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValeurCalculee(String value) {
        this.valeurCalculee = value;
    }

    /**
     * Obtient la valeur de la propriété valeurDefaut.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValeurDefaut() {
        return valeurDefaut;
    }

    /**
     * Définit la valeur de la propriété valeurDefaut.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValeurDefaut(String value) {
        this.valeurDefaut = value;
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
