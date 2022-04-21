
package fr.legrain.moncompte.ws;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java pour taPrixPerso complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taPrixPerso"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="prixHT" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="prixHTLicence" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="prixHTLight" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="prixHTMaintenance" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="prixHTParPosteInstalle" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="prixHTWs" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="prixTTC" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="prixTTCLicence" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="prixTTCLight" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="prixTTCMaintenance" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="prixTTCParPosteInstalle" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="prixTTCWs" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="quandCree" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quandModif" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quiCree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quiModif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="taProduit" type="{http://service.moncompte.legrain.fr/}taProduit" minOccurs="0"/&gt;
 *         &lt;element name="tauxTVA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="tva" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="tvaLicence" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="tvaLight" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="tvaMaintenance" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="tvaParPosteInstalle" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="tvaWs" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
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
@XmlType(name = "taPrixPerso", propOrder = {
    "id",
    "prixHT",
    "prixHTLicence",
    "prixHTLight",
    "prixHTMaintenance",
    "prixHTParPosteInstalle",
    "prixHTWs",
    "prixTTC",
    "prixTTCLicence",
    "prixTTCLight",
    "prixTTCMaintenance",
    "prixTTCParPosteInstalle",
    "prixTTCWs",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "taProduit",
    "tauxTVA",
    "tva",
    "tvaLicence",
    "tvaLight",
    "tvaMaintenance",
    "tvaParPosteInstalle",
    "tvaWs",
    "versionObj"
})
public class TaPrixPerso implements Serializable {

    protected Integer id;
    protected BigDecimal prixHT;
    protected BigDecimal prixHTLicence;
    protected BigDecimal prixHTLight;
    protected BigDecimal prixHTMaintenance;
    protected BigDecimal prixHTParPosteInstalle;
    protected BigDecimal prixHTWs;
    protected BigDecimal prixTTC;
    protected BigDecimal prixTTCLicence;
    protected BigDecimal prixTTCLight;
    protected BigDecimal prixTTCMaintenance;
    protected BigDecimal prixTTCParPosteInstalle;
    protected BigDecimal prixTTCWs;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
    protected TaProduit taProduit;
    protected BigDecimal tauxTVA;
    protected BigDecimal tva;
    protected BigDecimal tvaLicence;
    protected BigDecimal tvaLight;
    protected BigDecimal tvaMaintenance;
    protected BigDecimal tvaParPosteInstalle;
    protected BigDecimal tvaWs;
    protected Integer versionObj;

    /**
     * Obtient la valeur de la propriété id.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getId() {
        return id;
    }

    /**
     * Définit la valeur de la propriété id.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setId(Integer value) {
        this.id = value;
    }

    /**
     * Obtient la valeur de la propriété prixHT.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPrixHT() {
        return prixHT;
    }

    /**
     * Définit la valeur de la propriété prixHT.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPrixHT(BigDecimal value) {
        this.prixHT = value;
    }

    /**
     * Obtient la valeur de la propriété prixHTLicence.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPrixHTLicence() {
        return prixHTLicence;
    }

    /**
     * Définit la valeur de la propriété prixHTLicence.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPrixHTLicence(BigDecimal value) {
        this.prixHTLicence = value;
    }

    /**
     * Obtient la valeur de la propriété prixHTLight.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPrixHTLight() {
        return prixHTLight;
    }

    /**
     * Définit la valeur de la propriété prixHTLight.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPrixHTLight(BigDecimal value) {
        this.prixHTLight = value;
    }

    /**
     * Obtient la valeur de la propriété prixHTMaintenance.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPrixHTMaintenance() {
        return prixHTMaintenance;
    }

    /**
     * Définit la valeur de la propriété prixHTMaintenance.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPrixHTMaintenance(BigDecimal value) {
        this.prixHTMaintenance = value;
    }

    /**
     * Obtient la valeur de la propriété prixHTParPosteInstalle.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPrixHTParPosteInstalle() {
        return prixHTParPosteInstalle;
    }

    /**
     * Définit la valeur de la propriété prixHTParPosteInstalle.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPrixHTParPosteInstalle(BigDecimal value) {
        this.prixHTParPosteInstalle = value;
    }

    /**
     * Obtient la valeur de la propriété prixHTWs.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPrixHTWs() {
        return prixHTWs;
    }

    /**
     * Définit la valeur de la propriété prixHTWs.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPrixHTWs(BigDecimal value) {
        this.prixHTWs = value;
    }

    /**
     * Obtient la valeur de la propriété prixTTC.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPrixTTC() {
        return prixTTC;
    }

    /**
     * Définit la valeur de la propriété prixTTC.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPrixTTC(BigDecimal value) {
        this.prixTTC = value;
    }

    /**
     * Obtient la valeur de la propriété prixTTCLicence.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPrixTTCLicence() {
        return prixTTCLicence;
    }

    /**
     * Définit la valeur de la propriété prixTTCLicence.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPrixTTCLicence(BigDecimal value) {
        this.prixTTCLicence = value;
    }

    /**
     * Obtient la valeur de la propriété prixTTCLight.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPrixTTCLight() {
        return prixTTCLight;
    }

    /**
     * Définit la valeur de la propriété prixTTCLight.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPrixTTCLight(BigDecimal value) {
        this.prixTTCLight = value;
    }

    /**
     * Obtient la valeur de la propriété prixTTCMaintenance.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPrixTTCMaintenance() {
        return prixTTCMaintenance;
    }

    /**
     * Définit la valeur de la propriété prixTTCMaintenance.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPrixTTCMaintenance(BigDecimal value) {
        this.prixTTCMaintenance = value;
    }

    /**
     * Obtient la valeur de la propriété prixTTCParPosteInstalle.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPrixTTCParPosteInstalle() {
        return prixTTCParPosteInstalle;
    }

    /**
     * Définit la valeur de la propriété prixTTCParPosteInstalle.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPrixTTCParPosteInstalle(BigDecimal value) {
        this.prixTTCParPosteInstalle = value;
    }

    /**
     * Obtient la valeur de la propriété prixTTCWs.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPrixTTCWs() {
        return prixTTCWs;
    }

    /**
     * Définit la valeur de la propriété prixTTCWs.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPrixTTCWs(BigDecimal value) {
        this.prixTTCWs = value;
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
     * Obtient la valeur de la propriété taProduit.
     * 
     * @return
     *     possible object is
     *     {@link TaProduit }
     *     
     */
    public TaProduit getTaProduit() {
        return taProduit;
    }

    /**
     * Définit la valeur de la propriété taProduit.
     * 
     * @param value
     *     allowed object is
     *     {@link TaProduit }
     *     
     */
    public void setTaProduit(TaProduit value) {
        this.taProduit = value;
    }

    /**
     * Obtient la valeur de la propriété tauxTVA.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTauxTVA() {
        return tauxTVA;
    }

    /**
     * Définit la valeur de la propriété tauxTVA.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTauxTVA(BigDecimal value) {
        this.tauxTVA = value;
    }

    /**
     * Obtient la valeur de la propriété tva.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTva() {
        return tva;
    }

    /**
     * Définit la valeur de la propriété tva.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTva(BigDecimal value) {
        this.tva = value;
    }

    /**
     * Obtient la valeur de la propriété tvaLicence.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTvaLicence() {
        return tvaLicence;
    }

    /**
     * Définit la valeur de la propriété tvaLicence.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTvaLicence(BigDecimal value) {
        this.tvaLicence = value;
    }

    /**
     * Obtient la valeur de la propriété tvaLight.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTvaLight() {
        return tvaLight;
    }

    /**
     * Définit la valeur de la propriété tvaLight.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTvaLight(BigDecimal value) {
        this.tvaLight = value;
    }

    /**
     * Obtient la valeur de la propriété tvaMaintenance.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTvaMaintenance() {
        return tvaMaintenance;
    }

    /**
     * Définit la valeur de la propriété tvaMaintenance.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTvaMaintenance(BigDecimal value) {
        this.tvaMaintenance = value;
    }

    /**
     * Obtient la valeur de la propriété tvaParPosteInstalle.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTvaParPosteInstalle() {
        return tvaParPosteInstalle;
    }

    /**
     * Définit la valeur de la propriété tvaParPosteInstalle.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTvaParPosteInstalle(BigDecimal value) {
        this.tvaParPosteInstalle = value;
    }

    /**
     * Obtient la valeur de la propriété tvaWs.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTvaWs() {
        return tvaWs;
    }

    /**
     * Définit la valeur de la propriété tvaWs.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTvaWs(BigDecimal value) {
        this.tvaWs = value;
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
