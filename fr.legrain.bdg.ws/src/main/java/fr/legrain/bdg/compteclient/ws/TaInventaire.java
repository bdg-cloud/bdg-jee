
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
 * <p>Classe Java pour taInventaire complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taInventaire"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codeInventaire" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="commentaire" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dateInventaire" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="idInventaire" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ipAcces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="libelleInventaire" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="lignes" type="{http://service.bdg.legrain.fr/}taLInventaire" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="quandCree" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quandModif" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quiCree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quiModif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="taEntrepot" type="{http://service.bdg.legrain.fr/}taEntrepot" minOccurs="0"/&gt;
 *         &lt;element name="taEtat" type="{http://service.bdg.legrain.fr/}taEtat" minOccurs="0"/&gt;
 *         &lt;element name="taGrMouvStock" type="{http://service.bdg.legrain.fr/}taGrMouvStock" minOccurs="0"/&gt;
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
@XmlType(name = "taInventaire", propOrder = {
    "codeInventaire",
    "commentaire",
    "dateInventaire",
    "idInventaire",
    "ipAcces",
    "libelleInventaire",
    "lignes",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "taEntrepot",
    "taEtat",
    "taGrMouvStock",
    "version",
    "versionObj"
})
public class TaInventaire {

    protected String codeInventaire;
    protected String commentaire;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateInventaire;
    protected int idInventaire;
    protected String ipAcces;
    protected String libelleInventaire;
    @XmlElement(nillable = true)
    protected List<TaLInventaire> lignes;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
    protected TaEntrepot taEntrepot;
    protected TaEtat taEtat;
    protected TaGrMouvStock taGrMouvStock;
    protected String version;
    protected Integer versionObj;

    /**
     * Obtient la valeur de la propriété codeInventaire.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeInventaire() {
        return codeInventaire;
    }

    /**
     * Définit la valeur de la propriété codeInventaire.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeInventaire(String value) {
        this.codeInventaire = value;
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
     * Obtient la valeur de la propriété dateInventaire.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateInventaire() {
        return dateInventaire;
    }

    /**
     * Définit la valeur de la propriété dateInventaire.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateInventaire(XMLGregorianCalendar value) {
        this.dateInventaire = value;
    }

    /**
     * Obtient la valeur de la propriété idInventaire.
     * 
     */
    public int getIdInventaire() {
        return idInventaire;
    }

    /**
     * Définit la valeur de la propriété idInventaire.
     * 
     */
    public void setIdInventaire(int value) {
        this.idInventaire = value;
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
     * Obtient la valeur de la propriété libelleInventaire.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLibelleInventaire() {
        return libelleInventaire;
    }

    /**
     * Définit la valeur de la propriété libelleInventaire.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLibelleInventaire(String value) {
        this.libelleInventaire = value;
    }

    /**
     * Gets the value of the lignes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lignes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLignes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaLInventaire }
     * 
     * 
     */
    public List<TaLInventaire> getLignes() {
        if (lignes == null) {
            lignes = new ArrayList<TaLInventaire>();
        }
        return this.lignes;
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
     * Obtient la valeur de la propriété taEntrepot.
     * 
     * @return
     *     possible object is
     *     {@link TaEntrepot }
     *     
     */
    public TaEntrepot getTaEntrepot() {
        return taEntrepot;
    }

    /**
     * Définit la valeur de la propriété taEntrepot.
     * 
     * @param value
     *     allowed object is
     *     {@link TaEntrepot }
     *     
     */
    public void setTaEntrepot(TaEntrepot value) {
        this.taEntrepot = value;
    }

    /**
     * Obtient la valeur de la propriété taEtat.
     * 
     * @return
     *     possible object is
     *     {@link TaEtat }
     *     
     */
    public TaEtat getTaEtat() {
        return taEtat;
    }

    /**
     * Définit la valeur de la propriété taEtat.
     * 
     * @param value
     *     allowed object is
     *     {@link TaEtat }
     *     
     */
    public void setTaEtat(TaEtat value) {
        this.taEtat = value;
    }

    /**
     * Obtient la valeur de la propriété taGrMouvStock.
     * 
     * @return
     *     possible object is
     *     {@link TaGrMouvStock }
     *     
     */
    public TaGrMouvStock getTaGrMouvStock() {
        return taGrMouvStock;
    }

    /**
     * Définit la valeur de la propriété taGrMouvStock.
     * 
     * @param value
     *     allowed object is
     *     {@link TaGrMouvStock }
     *     
     */
    public void setTaGrMouvStock(TaGrMouvStock value) {
        this.taGrMouvStock = value;
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
