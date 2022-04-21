
package fr.legrain.bdg.compteclient.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java pour taCategorieArticle complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taCategorieArticle"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="categorieMereArticle" type="{http://service.bdg.legrain.fr/}taCategorieArticle" minOccurs="0"/&gt;
 *         &lt;element name="cheminImageCategorieArticle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codeCategorieArticle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="desciptionCategorieArticle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idCategorieArticle" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ipAcces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="libelleCategorieArticle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nomImageCategorieArticle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quandCree" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quandModif" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quiCree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quiModif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="urlRewritingCategorieArticle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "taCategorieArticle", propOrder = {
    "categorieMereArticle",
    "cheminImageCategorieArticle",
    "codeCategorieArticle",
    "desciptionCategorieArticle",
    "idCategorieArticle",
    "ipAcces",
    "libelleCategorieArticle",
    "nomImageCategorieArticle",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "urlRewritingCategorieArticle",
    "version",
    "versionObj"
})
public class TaCategorieArticle {

    protected TaCategorieArticle categorieMereArticle;
    protected String cheminImageCategorieArticle;
    protected String codeCategorieArticle;
    protected String desciptionCategorieArticle;
    protected int idCategorieArticle;
    protected String ipAcces;
    protected String libelleCategorieArticle;
    protected String nomImageCategorieArticle;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
    protected String urlRewritingCategorieArticle;
    protected String version;
    protected Integer versionObj;

    /**
     * Obtient la valeur de la propriété categorieMereArticle.
     * 
     * @return
     *     possible object is
     *     {@link TaCategorieArticle }
     *     
     */
    public TaCategorieArticle getCategorieMereArticle() {
        return categorieMereArticle;
    }

    /**
     * Définit la valeur de la propriété categorieMereArticle.
     * 
     * @param value
     *     allowed object is
     *     {@link TaCategorieArticle }
     *     
     */
    public void setCategorieMereArticle(TaCategorieArticle value) {
        this.categorieMereArticle = value;
    }

    /**
     * Obtient la valeur de la propriété cheminImageCategorieArticle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCheminImageCategorieArticle() {
        return cheminImageCategorieArticle;
    }

    /**
     * Définit la valeur de la propriété cheminImageCategorieArticle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCheminImageCategorieArticle(String value) {
        this.cheminImageCategorieArticle = value;
    }

    /**
     * Obtient la valeur de la propriété codeCategorieArticle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeCategorieArticle() {
        return codeCategorieArticle;
    }

    /**
     * Définit la valeur de la propriété codeCategorieArticle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeCategorieArticle(String value) {
        this.codeCategorieArticle = value;
    }

    /**
     * Obtient la valeur de la propriété desciptionCategorieArticle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDesciptionCategorieArticle() {
        return desciptionCategorieArticle;
    }

    /**
     * Définit la valeur de la propriété desciptionCategorieArticle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDesciptionCategorieArticle(String value) {
        this.desciptionCategorieArticle = value;
    }

    /**
     * Obtient la valeur de la propriété idCategorieArticle.
     * 
     */
    public int getIdCategorieArticle() {
        return idCategorieArticle;
    }

    /**
     * Définit la valeur de la propriété idCategorieArticle.
     * 
     */
    public void setIdCategorieArticle(int value) {
        this.idCategorieArticle = value;
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
     * Obtient la valeur de la propriété libelleCategorieArticle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLibelleCategorieArticle() {
        return libelleCategorieArticle;
    }

    /**
     * Définit la valeur de la propriété libelleCategorieArticle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLibelleCategorieArticle(String value) {
        this.libelleCategorieArticle = value;
    }

    /**
     * Obtient la valeur de la propriété nomImageCategorieArticle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomImageCategorieArticle() {
        return nomImageCategorieArticle;
    }

    /**
     * Définit la valeur de la propriété nomImageCategorieArticle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomImageCategorieArticle(String value) {
        this.nomImageCategorieArticle = value;
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
     * Obtient la valeur de la propriété urlRewritingCategorieArticle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrlRewritingCategorieArticle() {
        return urlRewritingCategorieArticle;
    }

    /**
     * Définit la valeur de la propriété urlRewritingCategorieArticle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrlRewritingCategorieArticle(String value) {
        this.urlRewritingCategorieArticle = value;
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
