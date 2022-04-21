
package fr.legrain.bdg.compteclient.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour taROptionAbon complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taROptionAbon"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="idROptionAbon" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="taOptionAbon" type="{http://service.bdg.legrain.fr/}taOptionAbon" minOccurs="0"/&gt;
 *         &lt;element name="taSupportAbon" type="{http://service.bdg.legrain.fr/}taSupportAbon" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "taROptionAbon", propOrder = {
    "idROptionAbon",
    "taOptionAbon",
    "taSupportAbon"
})
public class TaROptionAbon {

    protected Integer idROptionAbon;
    protected TaOptionAbon taOptionAbon;
    protected TaSupportAbon taSupportAbon;

    /**
     * Obtient la valeur de la propriété idROptionAbon.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIdROptionAbon() {
        return idROptionAbon;
    }

    /**
     * Définit la valeur de la propriété idROptionAbon.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIdROptionAbon(Integer value) {
        this.idROptionAbon = value;
    }

    /**
     * Obtient la valeur de la propriété taOptionAbon.
     * 
     * @return
     *     possible object is
     *     {@link TaOptionAbon }
     *     
     */
    public TaOptionAbon getTaOptionAbon() {
        return taOptionAbon;
    }

    /**
     * Définit la valeur de la propriété taOptionAbon.
     * 
     * @param value
     *     allowed object is
     *     {@link TaOptionAbon }
     *     
     */
    public void setTaOptionAbon(TaOptionAbon value) {
        this.taOptionAbon = value;
    }

    /**
     * Obtient la valeur de la propriété taSupportAbon.
     * 
     * @return
     *     possible object is
     *     {@link TaSupportAbon }
     *     
     */
    public TaSupportAbon getTaSupportAbon() {
        return taSupportAbon;
    }

    /**
     * Définit la valeur de la propriété taSupportAbon.
     * 
     * @param value
     *     allowed object is
     *     {@link TaSupportAbon }
     *     
     */
    public void setTaSupportAbon(TaSupportAbon value) {
        this.taSupportAbon = value;
    }

}
