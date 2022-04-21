
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
 * <p>Classe Java pour taResultatConformite complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taResultatConformite"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="dateControle" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="idResultatConformite" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ipAcces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quandCree" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quandModif" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quiCree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quiModif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="taBareme" type="{http://service.bdg.legrain.fr/}taBareme" minOccurs="0"/&gt;
 *         &lt;element name="taConformite" type="{http://service.bdg.legrain.fr/}taConformite" minOccurs="0"/&gt;
 *         &lt;element name="taLot" type="{http://service.bdg.legrain.fr/}taLot" minOccurs="0"/&gt;
 *         &lt;element name="taResultatCorrections" type="{http://service.bdg.legrain.fr/}taResultatCorrection" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="taStatusConformite" type="{http://service.bdg.legrain.fr/}taStatusConformite" minOccurs="0"/&gt;
 *         &lt;element name="taUtilisateurControleur" type="{http://service.bdg.legrain.fr/}taUtilisateur" minOccurs="0"/&gt;
 *         &lt;element name="valeurConstatee" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "taResultatConformite", propOrder = {
    "dateControle",
    "idResultatConformite",
    "ipAcces",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "taBareme",
    "taConformite",
    "taLot",
    "taResultatCorrections",
    "taStatusConformite",
    "taUtilisateurControleur",
    "valeurConstatee",
    "version",
    "versionObj"
})
public class TaResultatConformite {

    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateControle;
    protected int idResultatConformite;
    protected String ipAcces;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
    protected TaBareme taBareme;
    protected TaConformite taConformite;
    protected TaLot taLot;
    @XmlElement(nillable = true)
    protected List<TaResultatCorrection> taResultatCorrections;
    protected TaStatusConformite taStatusConformite;
    protected TaUtilisateur taUtilisateurControleur;
    protected String valeurConstatee;
    protected String version;
    protected Integer versionObj;

    /**
     * Obtient la valeur de la propriété dateControle.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateControle() {
        return dateControle;
    }

    /**
     * Définit la valeur de la propriété dateControle.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateControle(XMLGregorianCalendar value) {
        this.dateControle = value;
    }

    /**
     * Obtient la valeur de la propriété idResultatConformite.
     * 
     */
    public int getIdResultatConformite() {
        return idResultatConformite;
    }

    /**
     * Définit la valeur de la propriété idResultatConformite.
     * 
     */
    public void setIdResultatConformite(int value) {
        this.idResultatConformite = value;
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
     * Obtient la valeur de la propriété taBareme.
     * 
     * @return
     *     possible object is
     *     {@link TaBareme }
     *     
     */
    public TaBareme getTaBareme() {
        return taBareme;
    }

    /**
     * Définit la valeur de la propriété taBareme.
     * 
     * @param value
     *     allowed object is
     *     {@link TaBareme }
     *     
     */
    public void setTaBareme(TaBareme value) {
        this.taBareme = value;
    }

    /**
     * Obtient la valeur de la propriété taConformite.
     * 
     * @return
     *     possible object is
     *     {@link TaConformite }
     *     
     */
    public TaConformite getTaConformite() {
        return taConformite;
    }

    /**
     * Définit la valeur de la propriété taConformite.
     * 
     * @param value
     *     allowed object is
     *     {@link TaConformite }
     *     
     */
    public void setTaConformite(TaConformite value) {
        this.taConformite = value;
    }

    /**
     * Obtient la valeur de la propriété taLot.
     * 
     * @return
     *     possible object is
     *     {@link TaLot }
     *     
     */
    public TaLot getTaLot() {
        return taLot;
    }

    /**
     * Définit la valeur de la propriété taLot.
     * 
     * @param value
     *     allowed object is
     *     {@link TaLot }
     *     
     */
    public void setTaLot(TaLot value) {
        this.taLot = value;
    }

    /**
     * Gets the value of the taResultatCorrections property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taResultatCorrections property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaResultatCorrections().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaResultatCorrection }
     * 
     * 
     */
    public List<TaResultatCorrection> getTaResultatCorrections() {
        if (taResultatCorrections == null) {
            taResultatCorrections = new ArrayList<TaResultatCorrection>();
        }
        return this.taResultatCorrections;
    }

    /**
     * Obtient la valeur de la propriété taStatusConformite.
     * 
     * @return
     *     possible object is
     *     {@link TaStatusConformite }
     *     
     */
    public TaStatusConformite getTaStatusConformite() {
        return taStatusConformite;
    }

    /**
     * Définit la valeur de la propriété taStatusConformite.
     * 
     * @param value
     *     allowed object is
     *     {@link TaStatusConformite }
     *     
     */
    public void setTaStatusConformite(TaStatusConformite value) {
        this.taStatusConformite = value;
    }

    /**
     * Obtient la valeur de la propriété taUtilisateurControleur.
     * 
     * @return
     *     possible object is
     *     {@link TaUtilisateur }
     *     
     */
    public TaUtilisateur getTaUtilisateurControleur() {
        return taUtilisateurControleur;
    }

    /**
     * Définit la valeur de la propriété taUtilisateurControleur.
     * 
     * @param value
     *     allowed object is
     *     {@link TaUtilisateur }
     *     
     */
    public void setTaUtilisateurControleur(TaUtilisateur value) {
        this.taUtilisateurControleur = value;
    }

    /**
     * Obtient la valeur de la propriété valeurConstatee.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValeurConstatee() {
        return valeurConstatee;
    }

    /**
     * Définit la valeur de la propriété valeurConstatee.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValeurConstatee(String value) {
        this.valeurConstatee = value;
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
