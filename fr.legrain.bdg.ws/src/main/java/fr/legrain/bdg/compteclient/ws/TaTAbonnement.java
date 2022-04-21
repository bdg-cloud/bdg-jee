
package fr.legrain.bdg.compteclient.ws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour taTAbonnement complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taTAbonnement"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codeTAbonnement" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="duree" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="idTAbonnement" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="libelleType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "taTAbonnement", propOrder = {
    "codeTAbonnement",
    "duree",
    "idTAbonnement",
    "libelleType",
    "taArticles",
    "types"
})
public class TaTAbonnement {

    protected String codeTAbonnement;
    protected Integer duree;
    protected int idTAbonnement;
    protected String libelleType;
    @XmlElement(nillable = true)
    protected List<TaArticle> taArticles;
    protected String types;

    /**
     * Obtient la valeur de la propriété codeTAbonnement.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeTAbonnement() {
        return codeTAbonnement;
    }

    /**
     * Définit la valeur de la propriété codeTAbonnement.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeTAbonnement(String value) {
        this.codeTAbonnement = value;
    }

    /**
     * Obtient la valeur de la propriété duree.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDuree() {
        return duree;
    }

    /**
     * Définit la valeur de la propriété duree.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDuree(Integer value) {
        this.duree = value;
    }

    /**
     * Obtient la valeur de la propriété idTAbonnement.
     * 
     */
    public int getIdTAbonnement() {
        return idTAbonnement;
    }

    /**
     * Définit la valeur de la propriété idTAbonnement.
     * 
     */
    public void setIdTAbonnement(int value) {
        this.idTAbonnement = value;
    }

    /**
     * Obtient la valeur de la propriété libelleType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLibelleType() {
        return libelleType;
    }

    /**
     * Définit la valeur de la propriété libelleType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLibelleType(String value) {
        this.libelleType = value;
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
