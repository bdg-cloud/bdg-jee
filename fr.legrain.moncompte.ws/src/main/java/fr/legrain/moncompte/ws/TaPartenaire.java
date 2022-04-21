
package fr.legrain.moncompte.ws;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java pour taPartenaire complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taPartenaire"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="actif" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="adresse1Banque" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="adresse2Banque" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="bicSwiftBanque" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codePartenaire" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="cpBanque" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dateDebut" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="ibanBanque" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="nomBanque" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quandCree" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quandModif" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quiCree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quiModif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="taCgPartenaire" type="{http://service.moncompte.legrain.fr/}taCgPartenaire" minOccurs="0"/&gt;
 *         &lt;element name="taTypePartenaire" type="{http://service.moncompte.legrain.fr/}taTypePartenaire" minOccurs="0"/&gt;
 *         &lt;element name="titulaireCompteBanque" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="versionObj" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="villeBanque" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "taPartenaire", propOrder = {
    "actif",
    "adresse1Banque",
    "adresse2Banque",
    "bicSwiftBanque",
    "codePartenaire",
    "cpBanque",
    "dateDebut",
    "ibanBanque",
    "id",
    "nomBanque",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "taCgPartenaire",
    "taTypePartenaire",
    "titulaireCompteBanque",
    "versionObj",
    "villeBanque"
})
public class TaPartenaire implements Serializable {

    protected Boolean actif;
    protected String adresse1Banque;
    protected String adresse2Banque;
    protected String bicSwiftBanque;
    protected String codePartenaire;
    protected String cpBanque;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateDebut;
    protected String ibanBanque;
    protected Integer id;
    protected String nomBanque;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
    protected TaCgPartenaire taCgPartenaire;
    protected TaTypePartenaire taTypePartenaire;
    protected String titulaireCompteBanque;
    protected Integer versionObj;
    protected String villeBanque;

    /**
     * Obtient la valeur de la propriété actif.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isActif() {
        return actif;
    }

    /**
     * Définit la valeur de la propriété actif.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setActif(Boolean value) {
        this.actif = value;
    }

    /**
     * Obtient la valeur de la propriété adresse1Banque.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdresse1Banque() {
        return adresse1Banque;
    }

    /**
     * Définit la valeur de la propriété adresse1Banque.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdresse1Banque(String value) {
        this.adresse1Banque = value;
    }

    /**
     * Obtient la valeur de la propriété adresse2Banque.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdresse2Banque() {
        return adresse2Banque;
    }

    /**
     * Définit la valeur de la propriété adresse2Banque.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdresse2Banque(String value) {
        this.adresse2Banque = value;
    }

    /**
     * Obtient la valeur de la propriété bicSwiftBanque.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBicSwiftBanque() {
        return bicSwiftBanque;
    }

    /**
     * Définit la valeur de la propriété bicSwiftBanque.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBicSwiftBanque(String value) {
        this.bicSwiftBanque = value;
    }

    /**
     * Obtient la valeur de la propriété codePartenaire.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodePartenaire() {
        return codePartenaire;
    }

    /**
     * Définit la valeur de la propriété codePartenaire.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodePartenaire(String value) {
        this.codePartenaire = value;
    }

    /**
     * Obtient la valeur de la propriété cpBanque.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCpBanque() {
        return cpBanque;
    }

    /**
     * Définit la valeur de la propriété cpBanque.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCpBanque(String value) {
        this.cpBanque = value;
    }

    /**
     * Obtient la valeur de la propriété dateDebut.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateDebut() {
        return dateDebut;
    }

    /**
     * Définit la valeur de la propriété dateDebut.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateDebut(XMLGregorianCalendar value) {
        this.dateDebut = value;
    }

    /**
     * Obtient la valeur de la propriété ibanBanque.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIbanBanque() {
        return ibanBanque;
    }

    /**
     * Définit la valeur de la propriété ibanBanque.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIbanBanque(String value) {
        this.ibanBanque = value;
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
     * Obtient la valeur de la propriété nomBanque.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomBanque() {
        return nomBanque;
    }

    /**
     * Définit la valeur de la propriété nomBanque.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomBanque(String value) {
        this.nomBanque = value;
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
     * Obtient la valeur de la propriété taCgPartenaire.
     * 
     * @return
     *     possible object is
     *     {@link TaCgPartenaire }
     *     
     */
    public TaCgPartenaire getTaCgPartenaire() {
        return taCgPartenaire;
    }

    /**
     * Définit la valeur de la propriété taCgPartenaire.
     * 
     * @param value
     *     allowed object is
     *     {@link TaCgPartenaire }
     *     
     */
    public void setTaCgPartenaire(TaCgPartenaire value) {
        this.taCgPartenaire = value;
    }

    /**
     * Obtient la valeur de la propriété taTypePartenaire.
     * 
     * @return
     *     possible object is
     *     {@link TaTypePartenaire }
     *     
     */
    public TaTypePartenaire getTaTypePartenaire() {
        return taTypePartenaire;
    }

    /**
     * Définit la valeur de la propriété taTypePartenaire.
     * 
     * @param value
     *     allowed object is
     *     {@link TaTypePartenaire }
     *     
     */
    public void setTaTypePartenaire(TaTypePartenaire value) {
        this.taTypePartenaire = value;
    }

    /**
     * Obtient la valeur de la propriété titulaireCompteBanque.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitulaireCompteBanque() {
        return titulaireCompteBanque;
    }

    /**
     * Définit la valeur de la propriété titulaireCompteBanque.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitulaireCompteBanque(String value) {
        this.titulaireCompteBanque = value;
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

    /**
     * Obtient la valeur de la propriété villeBanque.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVilleBanque() {
        return villeBanque;
    }

    /**
     * Définit la valeur de la propriété villeBanque.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVilleBanque(String value) {
        this.villeBanque = value;
    }

}
