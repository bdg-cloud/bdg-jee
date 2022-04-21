
package fr.legrain.moncompte.ws;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;


/**
 * <p>Classe Java pour taLignePanier complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taLignePanier"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codeProduit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idLignePanier" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="ipAcces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="libelleProduit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="montantHT" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="montantTTC" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="montantTVA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="quandCree" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quandModif" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quiCree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quiModif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="taPanier" type="{http://service.moncompte.legrain.fr/}taPanier" minOccurs="0"/&gt;
 *         &lt;element name="taProduit" type="{http://service.moncompte.legrain.fr/}taProduit" minOccurs="0"/&gt;
 *         &lt;element name="tauxTVA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
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
@XmlType(name = "taLignePanier", propOrder = {
    "codeProduit",
    "idLignePanier",
    "ipAcces",
    "libelleProduit",
    "montantHT",
    "montantTTC",
    "montantTVA",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "taPanier",
    "taProduit",
    "tauxTVA",
    "versionObj"
})
public class TaLignePanier implements Serializable {

    protected String codeProduit;
    protected Integer idLignePanier;
    protected String ipAcces;
    protected String libelleProduit;
    protected BigDecimal montantHT;
    protected BigDecimal montantTTC;
    protected BigDecimal montantTVA;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
    @XmlInverseReference(mappedBy = "lignes")
    @XmlElement
    protected TaPanier taPanier;
    protected TaProduit taProduit;
    protected BigDecimal tauxTVA;
    protected Integer versionObj;

    /**
     * Obtient la valeur de la propriété codeProduit.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeProduit() {
        return codeProduit;
    }

    /**
     * Définit la valeur de la propriété codeProduit.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeProduit(String value) {
        this.codeProduit = value;
    }

    /**
     * Obtient la valeur de la propriété idLignePanier.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIdLignePanier() {
        return idLignePanier;
    }

    /**
     * Définit la valeur de la propriété idLignePanier.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIdLignePanier(Integer value) {
        this.idLignePanier = value;
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
     * Obtient la valeur de la propriété libelleProduit.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLibelleProduit() {
        return libelleProduit;
    }

    /**
     * Définit la valeur de la propriété libelleProduit.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLibelleProduit(String value) {
        this.libelleProduit = value;
    }

    /**
     * Obtient la valeur de la propriété montantHT.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMontantHT() {
        return montantHT;
    }

    /**
     * Définit la valeur de la propriété montantHT.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMontantHT(BigDecimal value) {
        this.montantHT = value;
    }

    /**
     * Obtient la valeur de la propriété montantTTC.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMontantTTC() {
        return montantTTC;
    }

    /**
     * Définit la valeur de la propriété montantTTC.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMontantTTC(BigDecimal value) {
        this.montantTTC = value;
    }

    /**
     * Obtient la valeur de la propriété montantTVA.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMontantTVA() {
        return montantTVA;
    }

    /**
     * Définit la valeur de la propriété montantTVA.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMontantTVA(BigDecimal value) {
        this.montantTVA = value;
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
     * Obtient la valeur de la propriété taPanier.
     * 
     * @return
     *     possible object is
     *     {@link TaPanier }
     *     
     */
    public TaPanier getTaPanier() {
        return taPanier;
    }

    /**
     * Définit la valeur de la propriété taPanier.
     * 
     * @param value
     *     allowed object is
     *     {@link TaPanier }
     *     
     */
    public void setTaPanier(TaPanier value) {
        this.taPanier = value;
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
