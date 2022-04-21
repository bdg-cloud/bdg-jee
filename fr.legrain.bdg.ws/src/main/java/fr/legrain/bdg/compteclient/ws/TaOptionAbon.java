
package fr.legrain.bdg.compteclient.ws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour taOptionAbon complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taOptionAbon"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="idOptionAbon" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="optionAbon" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="taArticle" type="{http://service.bdg.legrain.fr/}taArticle" minOccurs="0"/&gt;
 *         &lt;element name="taROptionAbons" type="{http://service.bdg.legrain.fr/}taROptionAbon" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="taTArticle" type="{http://service.bdg.legrain.fr/}taTSupport" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "taOptionAbon", propOrder = {
    "idOptionAbon",
    "optionAbon",
    "taArticle",
    "taROptionAbons",
    "taTArticle"
})
public class TaOptionAbon {

    protected Integer idOptionAbon;
    protected String optionAbon;
    protected TaArticle taArticle;
    @XmlElement(nillable = true)
    protected List<TaROptionAbon> taROptionAbons;
    protected TaTSupport taTArticle;

    /**
     * Obtient la valeur de la propriété idOptionAbon.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIdOptionAbon() {
        return idOptionAbon;
    }

    /**
     * Définit la valeur de la propriété idOptionAbon.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIdOptionAbon(Integer value) {
        this.idOptionAbon = value;
    }

    /**
     * Obtient la valeur de la propriété optionAbon.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOptionAbon() {
        return optionAbon;
    }

    /**
     * Définit la valeur de la propriété optionAbon.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOptionAbon(String value) {
        this.optionAbon = value;
    }

    /**
     * Obtient la valeur de la propriété taArticle.
     * 
     * @return
     *     possible object is
     *     {@link TaArticle }
     *     
     */
    public TaArticle getTaArticle() {
        return taArticle;
    }

    /**
     * Définit la valeur de la propriété taArticle.
     * 
     * @param value
     *     allowed object is
     *     {@link TaArticle }
     *     
     */
    public void setTaArticle(TaArticle value) {
        this.taArticle = value;
    }

    /**
     * Gets the value of the taROptionAbons property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taROptionAbons property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaROptionAbons().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaROptionAbon }
     * 
     * 
     */
    public List<TaROptionAbon> getTaROptionAbons() {
        if (taROptionAbons == null) {
            taROptionAbons = new ArrayList<TaROptionAbon>();
        }
        return this.taROptionAbons;
    }

    /**
     * Obtient la valeur de la propriété taTArticle.
     * 
     * @return
     *     possible object is
     *     {@link TaTSupport }
     *     
     */
    public TaTSupport getTaTArticle() {
        return taTArticle;
    }

    /**
     * Définit la valeur de la propriété taTArticle.
     * 
     * @param value
     *     allowed object is
     *     {@link TaTSupport }
     *     
     */
    public void setTaTArticle(TaTSupport value) {
        this.taTArticle = value;
    }

}
