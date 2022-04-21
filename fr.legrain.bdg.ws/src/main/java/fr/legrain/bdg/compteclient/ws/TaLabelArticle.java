
package fr.legrain.bdg.compteclient.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java pour taLabelArticle complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taLabelArticle"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="blobLogo" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 *         &lt;element name="cheminImageLabelArticle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codeLabelArticle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="desciptionLabelArticle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idLabelArticle" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ipAcces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="libelleLabelArticle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nomImageLabelArticle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "taLabelArticle", propOrder = {
    "blobLogo",
    "cheminImageLabelArticle",
    "codeLabelArticle",
    "desciptionLabelArticle",
    "idLabelArticle",
    "ipAcces",
    "libelleLabelArticle",
    "nomImageLabelArticle",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "version",
    "versionObj"
})
public class TaLabelArticle {

    protected byte[] blobLogo;
    protected String cheminImageLabelArticle;
    protected String codeLabelArticle;
    protected String desciptionLabelArticle;
    protected int idLabelArticle;
    protected String ipAcces;
    protected String libelleLabelArticle;
    protected String nomImageLabelArticle;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
    protected String version;
    protected Integer versionObj;

    /**
     * Obtient la valeur de la propriété blobLogo.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getBlobLogo() {
        return blobLogo;
    }

    /**
     * Définit la valeur de la propriété blobLogo.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setBlobLogo(byte[] value) {
        this.blobLogo = value;
    }

    /**
     * Obtient la valeur de la propriété cheminImageLabelArticle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCheminImageLabelArticle() {
        return cheminImageLabelArticle;
    }

    /**
     * Définit la valeur de la propriété cheminImageLabelArticle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCheminImageLabelArticle(String value) {
        this.cheminImageLabelArticle = value;
    }

    /**
     * Obtient la valeur de la propriété codeLabelArticle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeLabelArticle() {
        return codeLabelArticle;
    }

    /**
     * Définit la valeur de la propriété codeLabelArticle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeLabelArticle(String value) {
        this.codeLabelArticle = value;
    }

    /**
     * Obtient la valeur de la propriété desciptionLabelArticle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDesciptionLabelArticle() {
        return desciptionLabelArticle;
    }

    /**
     * Définit la valeur de la propriété desciptionLabelArticle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDesciptionLabelArticle(String value) {
        this.desciptionLabelArticle = value;
    }

    /**
     * Obtient la valeur de la propriété idLabelArticle.
     * 
     */
    public int getIdLabelArticle() {
        return idLabelArticle;
    }

    /**
     * Définit la valeur de la propriété idLabelArticle.
     * 
     */
    public void setIdLabelArticle(int value) {
        this.idLabelArticle = value;
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
     * Obtient la valeur de la propriété libelleLabelArticle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLibelleLabelArticle() {
        return libelleLabelArticle;
    }

    /**
     * Définit la valeur de la propriété libelleLabelArticle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLibelleLabelArticle(String value) {
        this.libelleLabelArticle = value;
    }

    /**
     * Obtient la valeur de la propriété nomImageLabelArticle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomImageLabelArticle() {
        return nomImageLabelArticle;
    }

    /**
     * Définit la valeur de la propriété nomImageLabelArticle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomImageLabelArticle(String value) {
        this.nomImageLabelArticle = value;
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
