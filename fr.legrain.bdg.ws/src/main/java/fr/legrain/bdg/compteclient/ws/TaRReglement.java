
package fr.legrain.bdg.compteclient.ws;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;


/**
 * <p>Classe Java pour taRReglement complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taRReglement"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="accepte" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="affectation" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="dateExport" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dateVerrouillage" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="etat" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="etatDeSuppression" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ipAcces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quandCree" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quandModif" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quiCree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quiModif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="taFacture" type="{http://service.bdg.legrain.fr/}taFacture" minOccurs="0"/&gt;
 *         &lt;element name="taMiseADisposition" type="{http://service.bdg.legrain.fr/}taMiseADisposition" minOccurs="0"/&gt;
 *         &lt;element name="taReglement" type="{http://service.bdg.legrain.fr/}taReglement" minOccurs="0"/&gt;
 *         &lt;element name="taTicketDeCaisse" type="{http://service.bdg.legrain.fr/}taTicketDeCaisse" minOccurs="0"/&gt;
 *         &lt;element name="typeDocument" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "taRReglement", propOrder = {
    "accepte",
    "affectation",
    "dateExport",
    "dateVerrouillage",
    "etat",
    "etatDeSuppression",
    "id",
    "ipAcces",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "taFacture",
    "taMiseADisposition",
    "taReglement",
    "taTicketDeCaisse",
    "typeDocument",
    "version",
    "versionObj"
})
public class TaRReglement {

    protected Boolean accepte;
    protected BigDecimal affectation;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateExport;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateVerrouillage;
    protected Integer etat;
    protected int etatDeSuppression;
    protected int id;
    protected String ipAcces;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
    @XmlInverseReference(mappedBy = "taRReglements")
    @XmlElement
    protected TaFacture taFacture;
    protected TaMiseADisposition taMiseADisposition;
    protected TaReglement taReglement;
    protected TaTicketDeCaisse taTicketDeCaisse;
    protected String typeDocument;
    protected String version;
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
     * Obtient la valeur de la propriété affectation.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAffectation() {
        return affectation;
    }

    /**
     * Définit la valeur de la propriété affectation.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAffectation(BigDecimal value) {
        this.affectation = value;
    }

    /**
     * Obtient la valeur de la propriété dateExport.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateExport() {
        return dateExport;
    }

    /**
     * Définit la valeur de la propriété dateExport.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateExport(XMLGregorianCalendar value) {
        this.dateExport = value;
    }

    /**
     * Obtient la valeur de la propriété dateVerrouillage.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateVerrouillage() {
        return dateVerrouillage;
    }

    /**
     * Définit la valeur de la propriété dateVerrouillage.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateVerrouillage(XMLGregorianCalendar value) {
        this.dateVerrouillage = value;
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
     * Obtient la valeur de la propriété etatDeSuppression.
     * 
     */
    public int getEtatDeSuppression() {
        return etatDeSuppression;
    }

    /**
     * Définit la valeur de la propriété etatDeSuppression.
     * 
     */
    public void setEtatDeSuppression(int value) {
        this.etatDeSuppression = value;
    }

    /**
     * Obtient la valeur de la propriété id.
     * 
     */
    public int getId() {
        return id;
    }

    /**
     * Définit la valeur de la propriété id.
     * 
     */
    public void setId(int value) {
        this.id = value;
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
     * Obtient la valeur de la propriété taFacture.
     * 
     * @return
     *     possible object is
     *     {@link TaFacture }
     *     
     */
    public TaFacture getTaFacture() {
        return taFacture;
    }

    /**
     * Définit la valeur de la propriété taFacture.
     * 
     * @param value
     *     allowed object is
     *     {@link TaFacture }
     *     
     */
    public void setTaFacture(TaFacture value) {
        this.taFacture = value;
    }

    /**
     * Obtient la valeur de la propriété taMiseADisposition.
     * 
     * @return
     *     possible object is
     *     {@link TaMiseADisposition }
     *     
     */
    public TaMiseADisposition getTaMiseADisposition() {
        return taMiseADisposition;
    }

    /**
     * Définit la valeur de la propriété taMiseADisposition.
     * 
     * @param value
     *     allowed object is
     *     {@link TaMiseADisposition }
     *     
     */
    public void setTaMiseADisposition(TaMiseADisposition value) {
        this.taMiseADisposition = value;
    }

    /**
     * Obtient la valeur de la propriété taReglement.
     * 
     * @return
     *     possible object is
     *     {@link TaReglement }
     *     
     */
    public TaReglement getTaReglement() {
        return taReglement;
    }

    /**
     * Définit la valeur de la propriété taReglement.
     * 
     * @param value
     *     allowed object is
     *     {@link TaReglement }
     *     
     */
    public void setTaReglement(TaReglement value) {
        this.taReglement = value;
    }

    /**
     * Obtient la valeur de la propriété taTicketDeCaisse.
     * 
     * @return
     *     possible object is
     *     {@link TaTicketDeCaisse }
     *     
     */
    public TaTicketDeCaisse getTaTicketDeCaisse() {
        return taTicketDeCaisse;
    }

    /**
     * Définit la valeur de la propriété taTicketDeCaisse.
     * 
     * @param value
     *     allowed object is
     *     {@link TaTicketDeCaisse }
     *     
     */
    public void setTaTicketDeCaisse(TaTicketDeCaisse value) {
        this.taTicketDeCaisse = value;
    }

    /**
     * Obtient la valeur de la propriété typeDocument.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypeDocument() {
        return typeDocument;
    }

    /**
     * Définit la valeur de la propriété typeDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypeDocument(String value) {
        this.typeDocument = value;
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
