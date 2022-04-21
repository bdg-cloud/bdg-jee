
package fr.legrain.autorisations.ws;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour module complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="module">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="nom" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="niveau" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="limite" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="date-module" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "module")
public class Module implements Serializable{

    @XmlAttribute(name = "id")
    protected String id;
    @XmlAttribute(name = "nom")
    protected String nom;
    @XmlAttribute(name = "niveau")
    protected String niveau;
    @XmlAttribute(name = "limite")
    protected String limite;
    @XmlAttribute(name = "date-module")
    protected String dateModule;

    /**
     * Obtient la valeur de la propriété id.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Définit la valeur de la propriété id.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Obtient la valeur de la propriété nom.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNom() {
        return nom;
    }

    /**
     * Définit la valeur de la propriété nom.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNom(String value) {
        this.nom = value;
    }

    /**
     * Obtient la valeur de la propriété niveau.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNiveau() {
        return niveau;
    }

    /**
     * Définit la valeur de la propriété niveau.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNiveau(String value) {
        this.niveau = value;
    }

    /**
     * Obtient la valeur de la propriété limite.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLimite() {
        return limite;
    }

    /**
     * Définit la valeur de la propriété limite.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLimite(String value) {
        this.limite = value;
    }

    /**
     * Obtient la valeur de la propriété dateModule.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateModule() {
        return dateModule;
    }

    /**
     * Définit la valeur de la propriété dateModule.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateModule(String value) {
        this.dateModule = value;
    }

}
