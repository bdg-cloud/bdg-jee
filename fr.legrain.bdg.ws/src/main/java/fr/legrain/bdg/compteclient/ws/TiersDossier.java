
package fr.legrain.bdg.compteclient.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour tiersDossier complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="tiersDossier"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codeDossier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="taTiers" type="{http://service.bdg.legrain.fr/}taTiers" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tiersDossier", propOrder = {
    "codeDossier",
    "taTiers"
})
public class TiersDossier {

    protected String codeDossier;
    protected TaTiers taTiers;

    /**
     * Obtient la valeur de la propriété codeDossier.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeDossier() {
        return codeDossier;
    }

    /**
     * Définit la valeur de la propriété codeDossier.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeDossier(String value) {
        this.codeDossier = value;
    }

    /**
     * Obtient la valeur de la propriété taTiers.
     * 
     * @return
     *     possible object is
     *     {@link TaTiers }
     *     
     */
    public TaTiers getTaTiers() {
        return taTiers;
    }

    /**
     * Définit la valeur de la propriété taTiers.
     * 
     * @param value
     *     allowed object is
     *     {@link TaTiers }
     *     
     */
    public void setTaTiers(TaTiers value) {
        this.taTiers = value;
    }

}
