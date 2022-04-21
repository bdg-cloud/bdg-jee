
package fr.legrain.bdg.compteclient.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java pour taMarqueArticle complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taMarqueArticle"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="cheminImageMarqueArticle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codeMarqueArticle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="desciptionMarqueArticle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idImageMarqueArticle" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="idMarqueArticle" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ipAcces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="libelleMarqueArticle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nomImageMarqueArticle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quandCree" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quandModif" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quiCree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quiModif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "taMarqueArticle", propOrder = {
    "cheminImageMarqueArticle",
    "codeMarqueArticle",
    "desciptionMarqueArticle",
    "idImageMarqueArticle",
    "idMarqueArticle",
    "ipAcces",
    "libelleMarqueArticle",
    "nomImageMarqueArticle",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "version",
    "versionObj"
})
public class TaMarqueArticle {

    protected String cheminImageMarqueArticle;
    protected String codeMarqueArticle;
    protected String desciptionMarqueArticle;
    protected int idImageMarqueArticle;
    protected int idMarqueArticle;
    protected String ipAcces;
    protected String libelleMarqueArticle;
    protected String nomImageMarqueArticle;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
    protected String version;
    protected Integer versionObj;

    /**
     * Obtient la valeur de la propriété cheminImageMarqueArticle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCheminImageMarqueArticle() {
        return cheminImageMarqueArticle;
    }

    /**
     * Définit la valeur de la propriété cheminImageMarqueArticle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCheminImageMarqueArticle(String value) {
        this.cheminImageMarqueArticle = value;
    }

    /**
     * Obtient la valeur de la propriété codeMarqueArticle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeMarqueArticle() {
        return codeMarqueArticle;
    }

    /**
     * Définit la valeur de la propriété codeMarqueArticle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeMarqueArticle(String value) {
        this.codeMarqueArticle = value;
    }

    /**
     * Obtient la valeur de la propriété desciptionMarqueArticle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDesciptionMarqueArticle() {
        return desciptionMarqueArticle;
    }

    /**
     * Définit la valeur de la propriété desciptionMarqueArticle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDesciptionMarqueArticle(String value) {
        this.desciptionMarqueArticle = value;
    }

    /**
     * Obtient la valeur de la propriété idImageMarqueArticle.
     * 
     */
    public int getIdImageMarqueArticle() {
        return idImageMarqueArticle;
    }

    /**
     * Définit la valeur de la propriété idImageMarqueArticle.
     * 
     */
    public void setIdImageMarqueArticle(int value) {
        this.idImageMarqueArticle = value;
    }

    /**
     * Obtient la valeur de la propriété idMarqueArticle.
     * 
     */
    public int getIdMarqueArticle() {
        return idMarqueArticle;
    }

    /**
     * Définit la valeur de la propriété idMarqueArticle.
     * 
     */
    public void setIdMarqueArticle(int value) {
        this.idMarqueArticle = value;
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
     * Obtient la valeur de la propriété libelleMarqueArticle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLibelleMarqueArticle() {
        return libelleMarqueArticle;
    }

    /**
     * Définit la valeur de la propriété libelleMarqueArticle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLibelleMarqueArticle(String value) {
        this.libelleMarqueArticle = value;
    }

    /**
     * Obtient la valeur de la propriété nomImageMarqueArticle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomImageMarqueArticle() {
        return nomImageMarqueArticle;
    }

    /**
     * Définit la valeur de la propriété nomImageMarqueArticle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomImageMarqueArticle(String value) {
        this.nomImageMarqueArticle = value;
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
