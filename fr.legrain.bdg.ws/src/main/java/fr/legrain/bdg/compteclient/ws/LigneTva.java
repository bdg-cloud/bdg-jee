
package fr.legrain.bdg.compteclient.ws;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour ligneTva complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="ligneTva"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codeTva" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="libelle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="montantTotalHt" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="montantTotalHtAvecRemise" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="montantTotalTtc" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="montantTotalTtcAvecRemise" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="mtTva" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="mtTvaAvantRemise" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="nbLigneDocument" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="tauxTva" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ligneTva", propOrder = {
    "codeTva",
    "libelle",
    "montantTotalHt",
    "montantTotalHtAvecRemise",
    "montantTotalTtc",
    "montantTotalTtcAvecRemise",
    "mtTva",
    "mtTvaAvantRemise",
    "nbLigneDocument",
    "tauxTva"
})
public class LigneTva {

    protected String codeTva;
    protected String libelle;
    protected BigDecimal montantTotalHt;
    protected BigDecimal montantTotalHtAvecRemise;
    protected BigDecimal montantTotalTtc;
    protected BigDecimal montantTotalTtcAvecRemise;
    protected BigDecimal mtTva;
    protected BigDecimal mtTvaAvantRemise;
    protected int nbLigneDocument;
    protected BigDecimal tauxTva;

    /**
     * Obtient la valeur de la propriété codeTva.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeTva() {
        return codeTva;
    }

    /**
     * Définit la valeur de la propriété codeTva.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeTva(String value) {
        this.codeTva = value;
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
     * Obtient la valeur de la propriété montantTotalHt.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMontantTotalHt() {
        return montantTotalHt;
    }

    /**
     * Définit la valeur de la propriété montantTotalHt.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMontantTotalHt(BigDecimal value) {
        this.montantTotalHt = value;
    }

    /**
     * Obtient la valeur de la propriété montantTotalHtAvecRemise.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMontantTotalHtAvecRemise() {
        return montantTotalHtAvecRemise;
    }

    /**
     * Définit la valeur de la propriété montantTotalHtAvecRemise.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMontantTotalHtAvecRemise(BigDecimal value) {
        this.montantTotalHtAvecRemise = value;
    }

    /**
     * Obtient la valeur de la propriété montantTotalTtc.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMontantTotalTtc() {
        return montantTotalTtc;
    }

    /**
     * Définit la valeur de la propriété montantTotalTtc.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMontantTotalTtc(BigDecimal value) {
        this.montantTotalTtc = value;
    }

    /**
     * Obtient la valeur de la propriété montantTotalTtcAvecRemise.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMontantTotalTtcAvecRemise() {
        return montantTotalTtcAvecRemise;
    }

    /**
     * Définit la valeur de la propriété montantTotalTtcAvecRemise.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMontantTotalTtcAvecRemise(BigDecimal value) {
        this.montantTotalTtcAvecRemise = value;
    }

    /**
     * Obtient la valeur de la propriété mtTva.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMtTva() {
        return mtTva;
    }

    /**
     * Définit la valeur de la propriété mtTva.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMtTva(BigDecimal value) {
        this.mtTva = value;
    }

    /**
     * Obtient la valeur de la propriété mtTvaAvantRemise.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMtTvaAvantRemise() {
        return mtTvaAvantRemise;
    }

    /**
     * Définit la valeur de la propriété mtTvaAvantRemise.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMtTvaAvantRemise(BigDecimal value) {
        this.mtTvaAvantRemise = value;
    }

    /**
     * Obtient la valeur de la propriété nbLigneDocument.
     * 
     */
    public int getNbLigneDocument() {
        return nbLigneDocument;
    }

    /**
     * Définit la valeur de la propriété nbLigneDocument.
     * 
     */
    public void setNbLigneDocument(int value) {
        this.nbLigneDocument = value;
    }

    /**
     * Obtient la valeur de la propriété tauxTva.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTauxTva() {
        return tauxTva;
    }

    /**
     * Définit la valeur de la propriété tauxTva.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTauxTva(BigDecimal value) {
        this.tauxTva = value;
    }

}
