
package fr.legrain.bdg.compteclient.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java pour swtDocument complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="swtDocument"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="accepte" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="entete" type="{http://service.bdg.legrain.fr/}enteteDocument" minOccurs="0"/&gt;
 *         &lt;element name="idDocument" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ligneCourante" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="oldCODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="oldDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quandCree" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quandModif" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quiCree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quiModif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "swtDocument", propOrder = {
    "accepte",
    "entete",
    "idDocument",
    "ligneCourante",
    "oldCODE",
    "oldDate",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "versionObj"
})
@XmlSeeAlso({
    TaFabrication.class,
    TaAcompte.class,
    TaApporteur.class,
    TaAvisEcheance.class,
    TaAvoir.class,
    TaBonReception.class,
    TaBoncde.class,
    TaBonliv.class,
    TaDevis.class,
    TaFacture.class,
    TaPrelevement.class,
    TaProforma.class,
    TaReglement.class,
    TaTicketDeCaisse.class
})
public abstract class SwtDocument {

    protected Boolean accepte;
    protected EnteteDocument entete;
    protected int idDocument;
    protected int ligneCourante;
    protected String oldCODE;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar oldDate;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
    protected Integer versionObj;

    /**
     * Obtient la valeur de la propriété accepte.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAccepte() {
        return accepte;
    }

    /**
     * Définit la valeur de la propriété accepte.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAccepte(Boolean value) {
        this.accepte = value;
    }

    /**
     * Obtient la valeur de la propriété entete.
     * 
     * @return
     *     possible object is
     *     {@link EnteteDocument }
     *     
     */
    public EnteteDocument getEntete() {
        return entete;
    }

    /**
     * Définit la valeur de la propriété entete.
     * 
     * @param value
     *     allowed object is
     *     {@link EnteteDocument }
     *     
     */
    public void setEntete(EnteteDocument value) {
        this.entete = value;
    }

    /**
     * Obtient la valeur de la propriété idDocument.
     * 
     */
    public int getIdDocument() {
        return idDocument;
    }

    /**
     * Définit la valeur de la propriété idDocument.
     * 
     */
    public void setIdDocument(int value) {
        this.idDocument = value;
    }

    /**
     * Obtient la valeur de la propriété ligneCourante.
     * 
     */
    public int getLigneCourante() {
        return ligneCourante;
    }

    /**
     * Définit la valeur de la propriété ligneCourante.
     * 
     */
    public void setLigneCourante(int value) {
        this.ligneCourante = value;
    }

    /**
     * Obtient la valeur de la propriété oldCODE.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOldCODE() {
        return oldCODE;
    }

    /**
     * Définit la valeur de la propriété oldCODE.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOldCODE(String value) {
        this.oldCODE = value;
    }

    /**
     * Obtient la valeur de la propriété oldDate.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getOldDate() {
        return oldDate;
    }

    /**
     * Définit la valeur de la propriété oldDate.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setOldDate(XMLGregorianCalendar value) {
        this.oldDate = value;
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
