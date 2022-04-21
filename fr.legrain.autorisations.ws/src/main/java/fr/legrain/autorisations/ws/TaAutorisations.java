
package fr.legrain.autorisations.ws;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java pour taAutorisations complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taAutorisations">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cheminFichierXlm" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/>
 *         &lt;element name="codeDossier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/>
 *         &lt;element name="idAutorisation" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0" form="unqualified"/>
 *         &lt;element name="modules" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/>
 *         &lt;element name="quandCree" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" form="unqualified"/>
 *         &lt;element name="quandModif" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" form="unqualified"/>
 *         &lt;element name="quiCree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/>
 *         &lt;element name="quiModif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/>
 *         &lt;element name="taTypeProduit" type="{http://service.autorisations.autorisations.legrain.fr/}taTypeProduit" minOccurs="0" form="unqualified"/>
 *         &lt;element name="valide" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0" form="unqualified"/>
 *         &lt;element name="versionObj" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0" form="unqualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "taAutorisations", propOrder = {
    "cheminFichierXlm",
    "codeDossier",
    "idAutorisation",
    "modules",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "taTypeProduit",
    "valide",
    "versionObj"
})
public class TaAutorisations implements Serializable{

    @XmlElement(namespace = "")
    protected String cheminFichierXlm;
    @XmlElement(namespace = "")
    protected String codeDossier;
    @XmlElement(namespace = "")
    protected Integer idAutorisation;
    @XmlElement(namespace = "")
    protected String modules;
    @XmlElement(namespace = "")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlElement(namespace = "")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    @XmlElement(namespace = "")
    protected String quiCree;
    @XmlElement(namespace = "")
    protected String quiModif;
    @XmlElement(namespace = "")
    protected TaTypeProduit taTypeProduit;
    @XmlElement(namespace = "")
    protected Boolean valide;
    @XmlElement(namespace = "")
    protected Integer versionObj;

    /**
     * Obtient la valeur de la propriété cheminFichierXlm.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCheminFichierXlm() {
        return cheminFichierXlm;
    }

    /**
     * Définit la valeur de la propriété cheminFichierXlm.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCheminFichierXlm(String value) {
        this.cheminFichierXlm = value;
    }

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
     * Obtient la valeur de la propriété idAutorisation.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIdAutorisation() {
        return idAutorisation;
    }

    /**
     * Définit la valeur de la propriété idAutorisation.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIdAutorisation(Integer value) {
        this.idAutorisation = value;
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
     * Obtient la valeur de la propriété quandCree.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getQuandCree() {
        return quandCree;
    }

    /**
     * Définit la valeur de la propriété quandCree.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setQuandCree(XMLGregorianCalendar value) {
        this.quandCree = value;
    }

    /**
     * Obtient la valeur de la propriété quandModif.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getQuandModif() {
        return quandModif;
    }

    /**
     * Définit la valeur de la propriété quandModif.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setQuandModif(XMLGregorianCalendar value) {
        this.quandModif = value;
    }

    /**
     * Obtient la valeur de la propriété quiCree.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQuiCree() {
        return quiCree;
    }

    /**
     * Définit la valeur de la propriété quiCree.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQuiCree(String value) {
        this.quiCree = value;
    }

    /**
     * Obtient la valeur de la propriété quiModif.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQuiModif() {
        return quiModif;
    }

    /**
     * Définit la valeur de la propriété quiModif.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQuiModif(String value) {
        this.quiModif = value;
    }

    /**
     * Obtient la valeur de la propriété taTypeProduit.
     * 
     * @return
     *     possible object is
     *     {@link TaTypeProduit }
     *     
     */
    public TaTypeProduit getTaTypeProduit() {
        return taTypeProduit;
    }

    /**
     * Définit la valeur de la propriété taTypeProduit.
     * 
     * @param value
     *     allowed object is
     *     {@link TaTypeProduit }
     *     
     */
    public void setTaTypeProduit(TaTypeProduit value) {
        this.taTypeProduit = value;
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
