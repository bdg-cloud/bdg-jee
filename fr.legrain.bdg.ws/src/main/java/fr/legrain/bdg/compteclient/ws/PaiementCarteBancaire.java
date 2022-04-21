
package fr.legrain.bdg.compteclient.ws;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour paiementCarteBancaire complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="paiementCarteBancaire"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="anneeCarte" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="compteClient" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="cryptogrammeCarte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descriptionPaiement" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="moisCarte" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="montantPaiement" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="nomPorteurCarte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="numeroCarte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="originePaiement" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="servicePaiement" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "paiementCarteBancaire", propOrder = {
    "anneeCarte",
    "compteClient",
    "cryptogrammeCarte",
    "descriptionPaiement",
    "moisCarte",
    "montantPaiement",
    "nomPorteurCarte",
    "numeroCarte",
    "originePaiement",
    "servicePaiement"
})
public class PaiementCarteBancaire {

    protected int anneeCarte;
    protected boolean compteClient;
    protected String cryptogrammeCarte;
    protected String descriptionPaiement;
    protected int moisCarte;
    protected BigDecimal montantPaiement;
    protected String nomPorteurCarte;
    protected String numeroCarte;
    protected String originePaiement;
    protected String servicePaiement;

    /**
     * Obtient la valeur de la propriété anneeCarte.
     * 
     */
    public int getAnneeCarte() {
        return anneeCarte;
    }

    /**
     * Définit la valeur de la propriété anneeCarte.
     * 
     */
    public void setAnneeCarte(int value) {
        this.anneeCarte = value;
    }

    /**
     * Obtient la valeur de la propriété compteClient.
     * 
     */
    public boolean isCompteClient() {
        return compteClient;
    }

    /**
     * Définit la valeur de la propriété compteClient.
     * 
     */
    public void setCompteClient(boolean value) {
        this.compteClient = value;
    }

    /**
     * Obtient la valeur de la propriété cryptogrammeCarte.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCryptogrammeCarte() {
        return cryptogrammeCarte;
    }

    /**
     * Définit la valeur de la propriété cryptogrammeCarte.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCryptogrammeCarte(String value) {
        this.cryptogrammeCarte = value;
    }

    /**
     * Obtient la valeur de la propriété descriptionPaiement.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescriptionPaiement() {
        return descriptionPaiement;
    }

    /**
     * Définit la valeur de la propriété descriptionPaiement.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescriptionPaiement(String value) {
        this.descriptionPaiement = value;
    }

    /**
     * Obtient la valeur de la propriété moisCarte.
     * 
     */
    public int getMoisCarte() {
        return moisCarte;
    }

    /**
     * Définit la valeur de la propriété moisCarte.
     * 
     */
    public void setMoisCarte(int value) {
        this.moisCarte = value;
    }

    /**
     * Obtient la valeur de la propriété montantPaiement.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMontantPaiement() {
        return montantPaiement;
    }

    /**
     * Définit la valeur de la propriété montantPaiement.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMontantPaiement(BigDecimal value) {
        this.montantPaiement = value;
    }

    /**
     * Obtient la valeur de la propriété nomPorteurCarte.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomPorteurCarte() {
        return nomPorteurCarte;
    }

    /**
     * Définit la valeur de la propriété nomPorteurCarte.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomPorteurCarte(String value) {
        this.nomPorteurCarte = value;
    }

    /**
     * Obtient la valeur de la propriété numeroCarte.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroCarte() {
        return numeroCarte;
    }

    /**
     * Définit la valeur de la propriété numeroCarte.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroCarte(String value) {
        this.numeroCarte = value;
    }

    /**
     * Obtient la valeur de la propriété originePaiement.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginePaiement() {
        return originePaiement;
    }

    /**
     * Définit la valeur de la propriété originePaiement.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginePaiement(String value) {
        this.originePaiement = value;
    }

    /**
     * Obtient la valeur de la propriété servicePaiement.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServicePaiement() {
        return servicePaiement;
    }

    /**
     * Définit la valeur de la propriété servicePaiement.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServicePaiement(String value) {
        this.servicePaiement = value;
    }

}
