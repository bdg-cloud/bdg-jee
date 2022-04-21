
package fr.legrain.autorisations.ws;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour listeModules complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="listeModules">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="modules" minOccurs="0" form="unqualified">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="module" type="{http://service.autorisations.autorisations.legrain.fr/}module" maxOccurs="unbounded" minOccurs="0" form="unqualified"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="nb-poste-client" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/>
 *         &lt;element name="nb-utilisateur" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/>
 *         &lt;element name="acces-webservice" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listeModules", propOrder = {
    "modules",
    "nbPosteClient",
    "nbUtilisateur",
    "accesWebservice"
})
public class ListeModules implements Serializable{

    @XmlElement(namespace = "")
    protected ListeModules.Modules modules;
    @XmlElement(name = "nb-poste-client", namespace = "")
    protected String nbPosteClient;
    @XmlElement(name = "nb-utilisateur", namespace = "")
    protected String nbUtilisateur;
    @XmlElement(name = "acces-webservice", namespace = "")
    protected String accesWebservice;

    /**
     * Obtient la valeur de la propriété modules.
     * 
     * @return
     *     possible object is
     *     {@link ListeModules.Modules }
     *     
     */
    public ListeModules.Modules getModules() {
        return modules;
    }

    /**
     * Définit la valeur de la propriété modules.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeModules.Modules }
     *     
     */
    public void setModules(ListeModules.Modules value) {
        this.modules = value;
    }

    /**
     * Obtient la valeur de la propriété nbPosteClient.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNbPosteClient() {
        return nbPosteClient;
    }

    /**
     * Définit la valeur de la propriété nbPosteClient.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNbPosteClient(String value) {
        this.nbPosteClient = value;
    }

    /**
     * Obtient la valeur de la propriété nbUtilisateur.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNbUtilisateur() {
        return nbUtilisateur;
    }

    /**
     * Définit la valeur de la propriété nbUtilisateur.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNbUtilisateur(String value) {
        this.nbUtilisateur = value;
    }

    /**
     * Obtient la valeur de la propriété accesWebservice.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccesWebservice() {
        return accesWebservice;
    }

    /**
     * Définit la valeur de la propriété accesWebservice.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccesWebservice(String value) {
        this.accesWebservice = value;
    }


    /**
     * <p>Classe Java pour anonymous complex type.
     * 
     * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="module" type="{http://service.autorisations.autorisations.legrain.fr/}module" maxOccurs="unbounded" minOccurs="0" form="unqualified"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "module"
    })
    public static class Modules {

        @XmlElement(namespace = "", nillable = true)
        protected List<Module> module;

        /**
         * Gets the value of the module property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the module property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getModule().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Module }
         * 
         * 
         */
        public List<Module> getModule() {
            if (module == null) {
                module = new ArrayList<Module>();
            }
            return this.module;
        }

    }

}
