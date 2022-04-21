
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
 * <p>Classe Java pour taImageArticle complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taImageArticle"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="checksum" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="cheminImageArticle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="defautImageArticle" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="idImageArticle" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="imageOrigine" type="{http://service.bdg.legrain.fr/}taImageArticle" minOccurs="0"/&gt;
 *         &lt;element name="ipAcces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nomImageArticle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quandCree" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quandModif" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quiCree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quiModif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="taArticle" type="{http://service.bdg.legrain.fr/}taArticle" minOccurs="0"/&gt;
 *         &lt;element name="taImagesGenere" type="{http://service.bdg.legrain.fr/}taImageArticle" maxOccurs="unbounded" minOccurs="0"/&gt;
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
@XmlType(name = "taImageArticle", propOrder = {
    "checksum",
    "cheminImageArticle",
    "defautImageArticle",
    "idImageArticle",
    "imageOrigine",
    "ipAcces",
    "nomImageArticle",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "taArticle",
    "taImagesGenere",
    "version",
    "versionObj"
})
public class TaImageArticle {

    protected String checksum;
    protected String cheminImageArticle;
    protected Boolean defautImageArticle;
    protected int idImageArticle;
    protected TaImageArticle imageOrigine;
    protected String ipAcces;
    protected String nomImageArticle;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
    protected TaArticle taArticle;
    @XmlElement(nillable = true)
    protected List<TaImageArticle> taImagesGenere;
    protected String version;
    protected Integer versionObj;

    /**
     * Obtient la valeur de la propriété checksum.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChecksum() {
        return checksum;
    }

    /**
     * Définit la valeur de la propriété checksum.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChecksum(String value) {
        this.checksum = value;
    }

    /**
     * Obtient la valeur de la propriété cheminImageArticle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCheminImageArticle() {
        return cheminImageArticle;
    }

    /**
     * Définit la valeur de la propriété cheminImageArticle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCheminImageArticle(String value) {
        this.cheminImageArticle = value;
    }

    /**
     * Obtient la valeur de la propriété defautImageArticle.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDefautImageArticle() {
        return defautImageArticle;
    }

    /**
     * Définit la valeur de la propriété defautImageArticle.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDefautImageArticle(Boolean value) {
        this.defautImageArticle = value;
    }

    /**
     * Obtient la valeur de la propriété idImageArticle.
     * 
     */
    public int getIdImageArticle() {
        return idImageArticle;
    }

    /**
     * Définit la valeur de la propriété idImageArticle.
     * 
     */
    public void setIdImageArticle(int value) {
        this.idImageArticle = value;
    }

    /**
     * Obtient la valeur de la propriété imageOrigine.
     * 
     * @return
     *     possible object is
     *     {@link TaImageArticle }
     *     
     */
    public TaImageArticle getImageOrigine() {
        return imageOrigine;
    }

    /**
     * Définit la valeur de la propriété imageOrigine.
     * 
     * @param value
     *     allowed object is
     *     {@link TaImageArticle }
     *     
     */
    public void setImageOrigine(TaImageArticle value) {
        this.imageOrigine = value;
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
     * Obtient la valeur de la propriété nomImageArticle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomImageArticle() {
        return nomImageArticle;
    }

    /**
     * Définit la valeur de la propriété nomImageArticle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomImageArticle(String value) {
        this.nomImageArticle = value;
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
     * Obtient la valeur de la propriété taArticle.
     * 
     * @return
     *     possible object is
     *     {@link TaArticle }
     *     
     */
    public TaArticle getTaArticle() {
        return taArticle;
    }

    /**
     * Définit la valeur de la propriété taArticle.
     * 
     * @param value
     *     allowed object is
     *     {@link TaArticle }
     *     
     */
    public void setTaArticle(TaArticle value) {
        this.taArticle = value;
    }

    /**
     * Gets the value of the taImagesGenere property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taImagesGenere property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaImagesGenere().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaImageArticle }
     * 
     * 
     */
    public List<TaImageArticle> getTaImagesGenere() {
        if (taImagesGenere == null) {
            taImagesGenere = new ArrayList<TaImageArticle>();
        }
        return this.taImagesGenere;
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
