
package fr.legrain.bdg.compteclient.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;


/**
 * <p>Classe Java pour taAdresse complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taAdresse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="adresse1Adresse" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="adresse2Adresse" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="adresse3Adresse" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codepostalAdresse" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="commAdministratifAdresse" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="commCommercialAdresse" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="idAdresse" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="idImport" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ipAcces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ordre" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="origineImport" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="paysAdresse" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quandCree" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quandModif" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quiCree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quiModif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="taTAdr" type="{http://service.bdg.legrain.fr/}taTAdr" minOccurs="0"/&gt;
 *         &lt;element name="taTiers" type="{http://service.bdg.legrain.fr/}taTiers" minOccurs="0"/&gt;
 *         &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="versionObj" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="villeAdresse" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "taAdresse", propOrder = {
    "adresse1Adresse",
    "adresse2Adresse",
    "adresse3Adresse",
    "codepostalAdresse",
    "commAdministratifAdresse",
    "commCommercialAdresse",
    "idAdresse",
    "idImport",
    "ipAcces",
    "ordre",
    "origineImport",
    "paysAdresse",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "taTAdr",
    "taTiers",
    "version",
    "versionObj",
    "villeAdresse"
})
public class TaAdresse {

    protected String adresse1Adresse;
    protected String adresse2Adresse;
    protected String adresse3Adresse;
    protected String codepostalAdresse;
    protected Integer commAdministratifAdresse;
    protected Integer commCommercialAdresse;
    protected int idAdresse;
    protected String idImport;
    protected String ipAcces;
    protected Integer ordre;
    protected String origineImport;
    protected String paysAdresse;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
    protected TaTAdr taTAdr;
    @XmlInverseReference(mappedBy = "taAdresses")
    @XmlElement
    protected TaTiers taTiers;
    protected String version;
    protected Integer versionObj;
    protected String villeAdresse;

    /**
     * Obtient la valeur de la propriété adresse1Adresse.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdresse1Adresse() {
        return adresse1Adresse;
    }

    /**
     * Définit la valeur de la propriété adresse1Adresse.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdresse1Adresse(String value) {
        this.adresse1Adresse = value;
    }

    /**
     * Obtient la valeur de la propriété adresse2Adresse.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdresse2Adresse() {
        return adresse2Adresse;
    }

    /**
     * Définit la valeur de la propriété adresse2Adresse.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdresse2Adresse(String value) {
        this.adresse2Adresse = value;
    }

    /**
     * Obtient la valeur de la propriété adresse3Adresse.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdresse3Adresse() {
        return adresse3Adresse;
    }

    /**
     * Définit la valeur de la propriété adresse3Adresse.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdresse3Adresse(String value) {
        this.adresse3Adresse = value;
    }

    /**
     * Obtient la valeur de la propriété codepostalAdresse.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodepostalAdresse() {
        return codepostalAdresse;
    }

    /**
     * Définit la valeur de la propriété codepostalAdresse.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodepostalAdresse(String value) {
        this.codepostalAdresse = value;
    }

    /**
     * Obtient la valeur de la propriété commAdministratifAdresse.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCommAdministratifAdresse() {
        return commAdministratifAdresse;
    }

    /**
     * Définit la valeur de la propriété commAdministratifAdresse.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCommAdministratifAdresse(Integer value) {
        this.commAdministratifAdresse = value;
    }

    /**
     * Obtient la valeur de la propriété commCommercialAdresse.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCommCommercialAdresse() {
        return commCommercialAdresse;
    }

    /**
     * Définit la valeur de la propriété commCommercialAdresse.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCommCommercialAdresse(Integer value) {
        this.commCommercialAdresse = value;
    }

    /**
     * Obtient la valeur de la propriété idAdresse.
     * 
     */
    public int getIdAdresse() {
        return idAdresse;
    }

    /**
     * Définit la valeur de la propriété idAdresse.
     * 
     */
    public void setIdAdresse(int value) {
        this.idAdresse = value;
    }

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
     * Obtient la valeur de la propriété ordre.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOrdre() {
        return ordre;
    }

    /**
     * Définit la valeur de la propriété ordre.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOrdre(Integer value) {
        this.ordre = value;
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
     * Obtient la valeur de la propriété paysAdresse.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaysAdresse() {
        return paysAdresse;
    }

    /**
     * Définit la valeur de la propriété paysAdresse.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaysAdresse(String value) {
        this.paysAdresse = value;
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
     * Obtient la valeur de la propriété taTAdr.
     * 
     * @return
     *     possible object is
     *     {@link TaTAdr }
     *     
     */
    public TaTAdr getTaTAdr() {
        return taTAdr;
    }

    /**
     * Définit la valeur de la propriété taTAdr.
     * 
     * @param value
     *     allowed object is
     *     {@link TaTAdr }
     *     
     */
    public void setTaTAdr(TaTAdr value) {
        this.taTAdr = value;
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
     * Obtient la valeur de la propriété villeAdresse.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVilleAdresse() {
        return villeAdresse;
    }

    /**
     * Définit la valeur de la propriété villeAdresse.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVilleAdresse(String value) {
        this.villeAdresse = value;
    }

}
