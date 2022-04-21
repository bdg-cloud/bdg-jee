
package fr.legrain.bdg.compteclient.ws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour taTSupport complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taTSupport"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codeTSupport" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idTSupport" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="taArticles" type="{http://service.bdg.legrain.fr/}taArticle" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="types" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "taTSupport", propOrder = {
    "codeTSupport",
    "idTSupport",
    "taArticles",
    "types"
})
public class TaTSupport {

    protected String codeTSupport;
    protected int idTSupport;
    @XmlElement(nillable = true)
    protected List<TaArticle> taArticles;
    protected String types;

    /**
     * Obtient la valeur de la propriété codeTSupport.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeTSupport() {
        return codeTSupport;
    }

    /**
     * Définit la valeur de la propriété codeTSupport.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeTSupport(String value) {
        this.codeTSupport = value;
    }

    /**
     * Obtient la valeur de la propriété idTSupport.
     * 
     */
    public int getIdTSupport() {
        return idTSupport;
    }

    /**
     * Définit la valeur de la propriété idTSupport.
     * 
     */
    public void setIdTSupport(int value) {
        this.idTSupport = value;
    }

    /**
     * Gets the value of the taArticles property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taArticles property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaArticles().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaArticle }
     * 
     * 
     */
    public List<TaArticle> getTaArticles() {
        if (taArticles == null) {
            taArticles = new ArrayList<TaArticle>();
        }
        return this.taArticles;
    }

    /**
     * Obtient la valeur de la propriété types.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypes() {
        return types;
    }

    /**
     * Définit la valeur de la propriété types.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypes(String value) {
        this.types = value;
    }

}
