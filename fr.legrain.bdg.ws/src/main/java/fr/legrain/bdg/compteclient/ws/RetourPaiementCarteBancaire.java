
package fr.legrain.bdg.compteclient.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour retourPaiementCarteBancaire complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="retourPaiementCarteBancaire"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codeReglement" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idReglement" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="paye" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="refPaiement" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "retourPaiementCarteBancaire", propOrder = {
    "codeReglement",
    "idReglement",
    "paye",
    "refPaiement"
})
public class RetourPaiementCarteBancaire {

    protected String codeReglement;
    protected String idReglement;
    protected boolean paye;
    protected String refPaiement;

    /**
     * Obtient la valeur de la propriété codeReglement.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeReglement() {
        return codeReglement;
    }

    /**
     * Définit la valeur de la propriété codeReglement.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeReglement(String value) {
        this.codeReglement = value;
    }

    /**
     * Obtient la valeur de la propriété idReglement.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdReglement() {
        return idReglement;
    }

    /**
     * Définit la valeur de la propriété idReglement.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdReglement(String value) {
        this.idReglement = value;
    }

    /**
     * Obtient la valeur de la propriété paye.
     * 
     */
    public boolean isPaye() {
        return paye;
    }

    /**
     * Définit la valeur de la propriété paye.
     * 
     */
    public void setPaye(boolean value) {
        this.paye = value;
    }

    /**
     * Obtient la valeur de la propriété refPaiement.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefPaiement() {
        return refPaiement;
    }

    /**
     * Définit la valeur de la propriété refPaiement.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefPaiement(String value) {
        this.refPaiement = value;
    }

}
