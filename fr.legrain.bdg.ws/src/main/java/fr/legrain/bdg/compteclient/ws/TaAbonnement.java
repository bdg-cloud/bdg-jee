
package fr.legrain.bdg.compteclient.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java pour taAbonnement complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taAbonnement"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="commentaire" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dateDebut" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dateFin" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="etat" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="idAbonnement" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="taArticle" type="{http://service.bdg.legrain.fr/}taArticle" minOccurs="0"/&gt;
 *         &lt;element name="taLDocument" type="{http://service.bdg.legrain.fr/}taLFacture" minOccurs="0"/&gt;
 *         &lt;element name="taSupportAbon" type="{http://service.bdg.legrain.fr/}taSupportAbon" minOccurs="0"/&gt;
 *         &lt;element name="taTAbonnement" type="{http://service.bdg.legrain.fr/}taTAbonnement" minOccurs="0"/&gt;
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
@XmlType(name = "taAbonnement", propOrder = {
    "commentaire",
    "dateDebut",
    "dateFin",
    "etat",
    "idAbonnement",
    "taArticle",
    "taLDocument",
    "taSupportAbon",
    "taTAbonnement",
    "taTiers",
    "versionObj"
})
public class TaAbonnement {

    protected String commentaire;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateDebut;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateFin;
    protected Integer etat;
    protected int idAbonnement;
    protected TaArticle taArticle;
    protected TaLFacture taLDocument;
    protected TaSupportAbon taSupportAbon;
    protected TaTAbonnement taTAbonnement;
    protected TaTiers taTiers;
    protected Integer versionObj;

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
     * Obtient la valeur de la propriété dateDebut.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateDebut() {
        return dateDebut;
    }

    /**
     * Définit la valeur de la propriété dateDebut.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateDebut(XMLGregorianCalendar value) {
        this.dateDebut = value;
    }

    /**
     * Obtient la valeur de la propriété dateFin.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateFin() {
        return dateFin;
    }

    /**
     * Définit la valeur de la propriété dateFin.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateFin(XMLGregorianCalendar value) {
        this.dateFin = value;
    }

    /**
     * Obtient la valeur de la propriété etat.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getEtat() {
        return etat;
    }

    /**
     * Définit la valeur de la propriété etat.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setEtat(Integer value) {
        this.etat = value;
    }

    /**
     * Obtient la valeur de la propriété idAbonnement.
     * 
     */
    public int getIdAbonnement() {
        return idAbonnement;
    }

    /**
     * Définit la valeur de la propriété idAbonnement.
     * 
     */
    public void setIdAbonnement(int value) {
        this.idAbonnement = value;
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
     * Obtient la valeur de la propriété taSupportAbon.
     * 
     * @return
     *     possible object is
     *     {@link TaSupportAbon }
     *     
     */
    public TaSupportAbon getTaSupportAbon() {
        return taSupportAbon;
    }

    /**
     * Définit la valeur de la propriété taSupportAbon.
     * 
     * @param value
     *     allowed object is
     *     {@link TaSupportAbon }
     *     
     */
    public void setTaSupportAbon(TaSupportAbon value) {
        this.taSupportAbon = value;
    }

    /**
     * Obtient la valeur de la propriété taTAbonnement.
     * 
     * @return
     *     possible object is
     *     {@link TaTAbonnement }
     *     
     */
    public TaTAbonnement getTaTAbonnement() {
        return taTAbonnement;
    }

    /**
     * Définit la valeur de la propriété taTAbonnement.
     * 
     * @param value
     *     allowed object is
     *     {@link TaTAbonnement }
     *     
     */
    public void setTaTAbonnement(TaTAbonnement value) {
        this.taTAbonnement = value;
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
