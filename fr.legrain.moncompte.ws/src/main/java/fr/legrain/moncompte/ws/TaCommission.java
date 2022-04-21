
package fr.legrain.moncompte.ws;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java pour taCommission complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taCommission"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="taPartenaire" type="{http://service.moncompte.legrain.fr/}taPartenaire" minOccurs="0"/&gt;
 *         &lt;element name="taPanier" type="{http://service.moncompte.legrain.fr/}taPanier" minOccurs="0"/&gt;
 *         &lt;element name="montantReference" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="pourcentageCommission" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="montantCommission" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="codeDocumentAssocie" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="datePaiementCommission" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="commentaireLegrain" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="commentairePartenaire" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="commissionPayee" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="lignesCommission" type="{http://service.moncompte.legrain.fr/}taLigneCommission" maxOccurs="unbounded" minOccurs="0"/&gt;
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
@XmlType(name = "taCommission", propOrder = {
    "id",
    "taPartenaire",
    "taPanier",
    "montantReference",
    "pourcentageCommission",
    "montantCommission",
    "codeDocumentAssocie",
    "datePaiementCommission",
    "commentaireLegrain",
    "commentairePartenaire",
    "commissionPayee",
    "lignesCommission",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "versionObj"
})
public class TaCommission implements Serializable {

    protected Integer id;
    protected TaPartenaire taPartenaire;
    protected TaPanier taPanier;
    protected BigDecimal montantReference;
    protected BigDecimal pourcentageCommission;
    protected BigDecimal montantCommission;
    protected String codeDocumentAssocie;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar datePaiementCommission;
    protected String commentaireLegrain;
    protected String commentairePartenaire;
    protected boolean commissionPayee;
    @XmlElement(nillable = true)
    protected List<TaLigneCommission> lignesCommission;
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
     * Obtient la valeur de la propriété taPartenaire.
     * 
     * @return
     *     possible object is
     *     {@link TaPartenaire }
     *     
     */
    public TaPartenaire getTaPartenaire() {
        return taPartenaire;
    }

    /**
     * Définit la valeur de la propriété taPartenaire.
     * 
     * @param value
     *     allowed object is
     *     {@link TaPartenaire }
     *     
     */
    public void setTaPartenaire(TaPartenaire value) {
        this.taPartenaire = value;
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
     * Obtient la valeur de la propriété codeDocumentAssocie.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeDocumentAssocie() {
        return codeDocumentAssocie;
    }

    /**
     * Définit la valeur de la propriété codeDocumentAssocie.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeDocumentAssocie(String value) {
        this.codeDocumentAssocie = value;
    }

    /**
     * Obtient la valeur de la propriété datePaiementCommission.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDatePaiementCommission() {
        return datePaiementCommission;
    }

    /**
     * Définit la valeur de la propriété datePaiementCommission.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDatePaiementCommission(XMLGregorianCalendar value) {
        this.datePaiementCommission = value;
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
     * Obtient la valeur de la propriété commissionPayee.
     * 
     */
    public boolean isCommissionPayee() {
        return commissionPayee;
    }

    /**
     * Définit la valeur de la propriété commissionPayee.
     * 
     */
    public void setCommissionPayee(boolean value) {
        this.commissionPayee = value;
    }

    /**
     * Gets the value of the lignesCommission property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lignesCommission property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLignesCommission().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaLigneCommission }
     * 
     * 
     */
    public List<TaLigneCommission> getLignesCommission() {
        if (lignesCommission == null) {
            lignesCommission = new ArrayList<TaLigneCommission>();
        }
        return this.lignesCommission;
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
