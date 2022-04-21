
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
 * <p>Classe Java pour taParamCreeDocTiers complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taParamCreeDocTiers"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="appliquerATous" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="codeParam" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="decade" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="deuxSemaines" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="document" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="idParamCreeDocTiers" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ipAcces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="mois" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="nbJours" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="quandCree" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quandModif" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quiCree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quiModif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="semaine" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="taTDoc" type="{http://service.bdg.legrain.fr/}taTDoc" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="taTiers" type="{http://service.bdg.legrain.fr/}taTiers" minOccurs="0"/&gt;
 *         &lt;element name="tiers" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="versionObj" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="xJours" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "taParamCreeDocTiers", propOrder = {
    "appliquerATous",
    "codeParam",
    "decade",
    "deuxSemaines",
    "document",
    "idParamCreeDocTiers",
    "ipAcces",
    "mois",
    "nbJours",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "semaine",
    "taTDoc",
    "taTiers",
    "tiers",
    "version",
    "versionObj",
    "xJours"
})
public class TaParamCreeDocTiers {

    protected Boolean appliquerATous;
    protected String codeParam;
    protected Integer decade;
    protected Integer deuxSemaines;
    protected Integer document;
    protected int idParamCreeDocTiers;
    protected String ipAcces;
    protected Integer mois;
    protected Integer nbJours;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
    protected Integer semaine;
    @XmlElement(nillable = true)
    protected List<TaTDoc> taTDoc;
    protected TaTiers taTiers;
    protected Integer tiers;
    protected String version;
    protected Integer versionObj;
    protected Integer xJours;

    /**
     * Obtient la valeur de la propriété appliquerATous.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAppliquerATous() {
        return appliquerATous;
    }

    /**
     * Définit la valeur de la propriété appliquerATous.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAppliquerATous(Boolean value) {
        this.appliquerATous = value;
    }

    /**
     * Obtient la valeur de la propriété codeParam.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeParam() {
        return codeParam;
    }

    /**
     * Définit la valeur de la propriété codeParam.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeParam(String value) {
        this.codeParam = value;
    }

    /**
     * Obtient la valeur de la propriété decade.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDecade() {
        return decade;
    }

    /**
     * Définit la valeur de la propriété decade.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDecade(Integer value) {
        this.decade = value;
    }

    /**
     * Obtient la valeur de la propriété deuxSemaines.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDeuxSemaines() {
        return deuxSemaines;
    }

    /**
     * Définit la valeur de la propriété deuxSemaines.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDeuxSemaines(Integer value) {
        this.deuxSemaines = value;
    }

    /**
     * Obtient la valeur de la propriété document.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDocument() {
        return document;
    }

    /**
     * Définit la valeur de la propriété document.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDocument(Integer value) {
        this.document = value;
    }

    /**
     * Obtient la valeur de la propriété idParamCreeDocTiers.
     * 
     */
    public int getIdParamCreeDocTiers() {
        return idParamCreeDocTiers;
    }

    /**
     * Définit la valeur de la propriété idParamCreeDocTiers.
     * 
     */
    public void setIdParamCreeDocTiers(int value) {
        this.idParamCreeDocTiers = value;
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
     * Obtient la valeur de la propriété mois.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMois() {
        return mois;
    }

    /**
     * Définit la valeur de la propriété mois.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMois(Integer value) {
        this.mois = value;
    }

    /**
     * Obtient la valeur de la propriété nbJours.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNbJours() {
        return nbJours;
    }

    /**
     * Définit la valeur de la propriété nbJours.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNbJours(Integer value) {
        this.nbJours = value;
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
     * Obtient la valeur de la propriété semaine.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSemaine() {
        return semaine;
    }

    /**
     * Définit la valeur de la propriété semaine.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSemaine(Integer value) {
        this.semaine = value;
    }

    /**
     * Gets the value of the taTDoc property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taTDoc property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaTDoc().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaTDoc }
     * 
     * 
     */
    public List<TaTDoc> getTaTDoc() {
        if (taTDoc == null) {
            taTDoc = new ArrayList<TaTDoc>();
        }
        return this.taTDoc;
    }

    /**
     * Obtient la valeur de la propriété taTiers.
     * 
     * @return
     *     possible object is
     *     {@link TaTiers }
     *     
     */
    public TaTiers getTaTiers() {
        return taTiers;
    }

    /**
     * Définit la valeur de la propriété taTiers.
     * 
     * @param value
     *     allowed object is
     *     {@link TaTiers }
     *     
     */
    public void setTaTiers(TaTiers value) {
        this.taTiers = value;
    }

    /**
     * Obtient la valeur de la propriété tiers.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTiers() {
        return tiers;
    }

    /**
     * Définit la valeur de la propriété tiers.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTiers(Integer value) {
        this.tiers = value;
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

    /**
     * Obtient la valeur de la propriété xJours.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getXJours() {
        return xJours;
    }

    /**
     * Définit la valeur de la propriété xJours.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setXJours(Integer value) {
        this.xJours = value;
    }

}
