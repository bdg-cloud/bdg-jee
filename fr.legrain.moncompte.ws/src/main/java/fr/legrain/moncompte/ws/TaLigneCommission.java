
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
 * <p>Classe Java pour taLigneCommission complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taLigneCommission"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="taCommission" type="{http://service.moncompte.legrain.fr/}taCommission" minOccurs="0"/&gt;
 *         &lt;element name="taLignePanier" type="{http://service.moncompte.legrain.fr/}taLignePanier" minOccurs="0"/&gt;
 *         &lt;element name="taProduit" type="{http://service.moncompte.legrain.fr/}taProduit" minOccurs="0"/&gt;
 *         &lt;element name="montantReference" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="pourcentageCommission" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="montantCommission" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="commentaireLegrain" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="commentairePartenaire" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "taLigneCommission", propOrder = {
    "id",
    "taCommission",
    "taLignePanier",
    "taProduit",
    "montantReference",
    "pourcentageCommission",
    "montantCommission",
    "commentaireLegrain",
    "commentairePartenaire",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "versionObj"
})
public class TaLigneCommission implements Serializable {

    protected Integer id;
    @XmlInverseReference(mappedBy = "lignesCommission")
    @XmlElement
    protected TaCommission taCommission;
    protected TaLignePanier taLignePanier;
    protected TaProduit taProduit;
    protected BigDecimal montantReference;
    protected BigDecimal pourcentageCommission;
    protected BigDecimal montantCommission;
    protected String commentaireLegrain;
    protected String commentairePartenaire;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
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
     * Obtient la valeur de la propriété taCommission.
     * 
     * @return
     *     possible object is
     *     {@link TaCommission }
     *     
     */
    public TaCommission getTaCommission() {
        return taCommission;
    }

    /**
     * Définit la valeur de la propriété taCommission.
     * 
     * @param value
     *     allowed object is
     *     {@link TaCommission }
     *     
     */
    public void setTaCommission(TaCommission value) {
        this.taCommission = value;
    }

    /**
     * Obtient la valeur de la propriété taLignePanier.
     * 
     * @return
     *     possible object is
     *     {@link TaLignePanier }
     *     
     */
    public TaLignePanier getTaLignePanier() {
        return taLignePanier;
    }

    /**
     * Définit la valeur de la propriété taLignePanier.
     * 
     * @param value
     *     allowed object is
     *     {@link TaLignePanier }
     *     
     */
    public void setTaLignePanier(TaLignePanier value) {
        this.taLignePanier = value;
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
     * Obtient la valeur de la propriété montantReference.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMontantReference() {
        return montantReference;
    }

    /**
     * Définit la valeur de la propriété montantReference.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMontantReference(BigDecimal value) {
        this.montantReference = value;
    }

    /**
     * Obtient la valeur de la propriété pourcentageCommission.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPourcentageCommission() {
        return pourcentageCommission;
    }

    /**
     * Définit la valeur de la propriété pourcentageCommission.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPourcentageCommission(BigDecimal value) {
        this.pourcentageCommission = value;
    }

    /**
     * Obtient la valeur de la propriété montantCommission.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMontantCommission() {
        return montantCommission;
    }

    /**
     * Définit la valeur de la propriété montantCommission.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMontantCommission(BigDecimal value) {
        this.montantCommission = value;
    }

    /**
     * Obtient la valeur de la propriété commentaireLegrain.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCommentaireLegrain() {
        return commentaireLegrain;
    }

    /**
     * Définit la valeur de la propriété commentaireLegrain.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCommentaireLegrain(String value) {
        this.commentaireLegrain = value;
    }

    /**
     * Obtient la valeur de la propriété commentairePartenaire.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCommentairePartenaire() {
        return commentairePartenaire;
    }

    /**
     * Définit la valeur de la propriété commentairePartenaire.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCommentairePartenaire(String value) {
        this.commentairePartenaire = value;
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
