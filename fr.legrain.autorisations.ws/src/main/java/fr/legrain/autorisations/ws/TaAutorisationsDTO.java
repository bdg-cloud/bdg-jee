
package fr.legrain.autorisations.ws;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour taAutorisationsDTO complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taAutorisationsDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://service.autorisations.autorisations.legrain.fr/}modelObject">
 *       &lt;sequence>
 *         &lt;element name="codeDossier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/>
 *         &lt;element name="codeType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0" form="unqualified"/>
 *         &lt;element name="idType" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0" form="unqualified"/>
 *         &lt;element name="modules" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/>
 *         &lt;element name="valide" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0" form="unqualified"/>
 *         &lt;element name="versionObj" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0" form="unqualified"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "taAutorisationsDTO", propOrder = {
    "codeDossier",
    "codeType",
    "id",
    "idType",
    "modules",
    "valide",
    "versionObj"
})
public class TaAutorisationsDTO 
    extends ModelObject implements Serializable
{

    @XmlElement(namespace = "")
    protected String codeDossier;
    @XmlElement(namespace = "")
    protected String codeType;
    @XmlElement(namespace = "")
    protected Integer id;
    @XmlElement(namespace = "")
    protected Integer idType;
    @XmlElement(namespace = "")
    protected String modules;
    @XmlElement(namespace = "")
    protected Boolean valide;
    @XmlElement(namespace = "")
    protected Integer versionObj;

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
     * Obtient la valeur de la propriété codeType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeType() {
        return codeType;
    }

    /**
     * Définit la valeur de la propriété codeType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeType(String value) {
        this.codeType = value;
    }

    /**
     * Obtient la valeur de la propriété id.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getId() {
        return id;
    }

    /**
     * Définit la valeur de la propriété id.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setId(Integer value) {
        this.id = value;
    }

    /**
     * Obtient la valeur de la propriété idType.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIdType() {
        return idType;
    }

    /**
     * Définit la valeur de la propriété idType.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIdType(Integer value) {
        this.idType = value;
    }

    /**
     * Obtient la valeur de la propriété modules.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModules() {
        return modules;
    }

    /**
     * Définit la valeur de la propriété modules.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModules(String value) {
        this.modules = value;
    }

    /**
     * Obtient la valeur de la propriété valide.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isValide() {
        return valide;
    }

    /**
     * Définit la valeur de la propriété valide.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setValide(Boolean value) {
        this.valide = value;
    }

    /**
     * Obtient la valeur de la propriété versionObj.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getVersionObj() {
        return versionObj;
    }

    /**
     * Définit la valeur de la propriété versionObj.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setVersionObj(Integer value) {
        this.versionObj = value;
    }

}
