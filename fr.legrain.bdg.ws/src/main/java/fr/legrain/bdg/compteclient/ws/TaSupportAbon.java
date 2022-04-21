
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
 * <p>Classe Java pour taSupportAbon complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taSupportAbon"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="className" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codeSupportAbon" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="commentaire" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="commercial" type="{http://service.bdg.legrain.fr/}taTiers" minOccurs="0"/&gt;
 *         &lt;element name="dateAcquisition" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="idSupportAbon" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="inactif" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="libelle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="taAbonnements" type="{http://service.bdg.legrain.fr/}taAbonnement" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="taArticle" type="{http://service.bdg.legrain.fr/}taArticle" minOccurs="0"/&gt;
 *         &lt;element name="taLDocument" type="{http://service.bdg.legrain.fr/}taLFacture" minOccurs="0"/&gt;
 *         &lt;element name="taROptionAbons" type="{http://service.bdg.legrain.fr/}taROptionAbon" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="taTSupport" type="{http://service.bdg.legrain.fr/}taTSupport" minOccurs="0"/&gt;
 *         &lt;element name="taTiers" type="{http://service.bdg.legrain.fr/}taTiers" minOccurs="0"/&gt;
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
@XmlType(name = "taSupportAbon", propOrder = {
    "className",
    "codeSupportAbon",
    "commentaire",
    "commercial",
    "dateAcquisition",
    "idSupportAbon",
    "inactif",
    "libelle",
    "taAbonnements",
    "taArticle",
    "taLDocument",
    "taROptionAbons",
    "taTSupport",
    "taTiers",
    "versionObj"
})
public class TaSupportAbon {

    protected String className;
    protected String codeSupportAbon;
    protected String commentaire;
    protected TaTiers commercial;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateAcquisition;
    protected Integer idSupportAbon;
    protected Integer inactif;
    protected String libelle;
    @XmlElement(nillable = true)
    protected List<TaAbonnement> taAbonnements;
    protected TaArticle taArticle;
    protected TaLFacture taLDocument;
    @XmlElement(nillable = true)
    protected List<TaROptionAbon> taROptionAbons;
    protected TaTSupport taTSupport;
    protected TaTiers taTiers;
    protected Integer versionObj;

    /**
     * Obtient la valeur de la propriété className.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClassName() {
        return className;
    }

    /**
     * Définit la valeur de la propriété className.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClassName(String value) {
        this.className = value;
    }

    /**
     * Obtient la valeur de la propriété codeSupportAbon.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeSupportAbon() {
        return codeSupportAbon;
    }

    /**
     * Définit la valeur de la propriété codeSupportAbon.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeSupportAbon(String value) {
        this.codeSupportAbon = value;
    }

    /**
     * Obtient la valeur de la propriété commentaire.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCommentaire() {
        return commentaire;
    }

    /**
     * Définit la valeur de la propriété commentaire.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCommentaire(String value) {
        this.commentaire = value;
    }

    /**
     * Obtient la valeur de la propriété commercial.
     * 
     * @return
     *     possible object is
     *     {@link TaTiers }
     *     
     */
    public TaTiers getCommercial() {
        return commercial;
    }

    /**
     * Définit la valeur de la propriété commercial.
     * 
     * @param value
     *     allowed object is
     *     {@link TaTiers }
     *     
     */
    public void setCommercial(TaTiers value) {
        this.commercial = value;
    }

    /**
     * Obtient la valeur de la propriété dateAcquisition.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateAcquisition() {
        return dateAcquisition;
    }

    /**
     * Définit la valeur de la propriété dateAcquisition.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateAcquisition(XMLGregorianCalendar value) {
        this.dateAcquisition = value;
    }

    /**
     * Obtient la valeur de la propriété idSupportAbon.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIdSupportAbon() {
        return idSupportAbon;
    }

    /**
     * Définit la valeur de la propriété idSupportAbon.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIdSupportAbon(Integer value) {
        this.idSupportAbon = value;
    }

    /**
     * Obtient la valeur de la propriété inactif.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getInactif() {
        return inactif;
    }

    /**
     * Définit la valeur de la propriété inactif.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setInactif(Integer value) {
        this.inactif = value;
    }

    /**
     * Obtient la valeur de la propriété libelle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLibelle() {
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
    public void setLibelle(String value) {
        this.libelle = value;
    }

    /**
     * Gets the value of the taAbonnements property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taAbonnements property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaAbonnements().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaAbonnement }
     * 
     * 
     */
    public List<TaAbonnement> getTaAbonnements() {
        if (taAbonnements == null) {
            taAbonnements = new ArrayList<TaAbonnement>();
        }
        return this.taAbonnements;
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
     * Obtient la valeur de la propriété taLDocument.
     * 
     * @return
     *     possible object is
     *     {@link TaLFacture }
     *     
     */
    public TaLFacture getTaLDocument() {
        return taLDocument;
    }

    /**
     * Définit la valeur de la propriété taLDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link TaLFacture }
     *     
     */
    public void setTaLDocument(TaLFacture value) {
        this.taLDocument = value;
    }

    /**
     * Gets the value of the taROptionAbons property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taROptionAbons property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaROptionAbons().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaROptionAbon }
     * 
     * 
     */
    public List<TaROptionAbon> getTaROptionAbons() {
        if (taROptionAbons == null) {
            taROptionAbons = new ArrayList<TaROptionAbon>();
        }
        return this.taROptionAbons;
    }

    /**
     * Obtient la valeur de la propriété taTSupport.
     * 
     * @return
     *     possible object is
     *     {@link TaTSupport }
     *     
     */
    public TaTSupport getTaTSupport() {
        return taTSupport;
    }

    /**
     * Définit la valeur de la propriété taTSupport.
     * 
     * @param value
     *     allowed object is
     *     {@link TaTSupport }
     *     
     */
    public void setTaTSupport(TaTSupport value) {
        this.taTSupport = value;
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
