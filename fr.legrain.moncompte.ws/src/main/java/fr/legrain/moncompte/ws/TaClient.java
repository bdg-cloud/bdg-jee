
package fr.legrain.moncompte.ws;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java pour taClient complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taClient"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="actif" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="adresse1" type="{http://service.moncompte.legrain.fr/}taAdresse" minOccurs="0"/&gt;
 *         &lt;element name="adresse1Banque" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="adresse2" type="{http://service.moncompte.legrain.fr/}taAdresse" minOccurs="0"/&gt;
 *         &lt;element name="adresse2Banque" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="adresse3" type="{http://service.moncompte.legrain.fr/}taAdresse" minOccurs="0"/&gt;
 *         &lt;element name="ancienCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="bicSwiftBanque" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="cpBanque" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ibanBanque" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="identifiantFtp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="listeLicence" type="{http://service.moncompte.legrain.fr/}taLicence" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="mdpFtp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nom" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nomBanque" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="numeroTva" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="prenom" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quandCree" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quandModif" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quiCree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quiModif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="serveurFtp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="taPartenaire" type="{http://service.moncompte.legrain.fr/}taPartenaire" minOccurs="0"/&gt;
 *         &lt;element name="taUtilisateur" type="{http://service.moncompte.legrain.fr/}taUtilisateur" minOccurs="0"/&gt;
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
@XmlType(name = "taClient", propOrder = {
    "actif",
    "adresse1",
    "adresse1Banque",
    "adresse2",
    "adresse2Banque",
    "adresse3",
    "ancienCode",
    "bicSwiftBanque",
    "code",
    "cpBanque",
    "ibanBanque",
    "id",
    "identifiantFtp",
    "listeLicence",
    "mdpFtp",
    "nom",
    "nomBanque",
    "numeroTva",
    "prenom",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "serveurFtp",
    "taPartenaire",
    "taUtilisateur",
    "titulaireCompteBanque",
    "versionObj",
    "villeBanque"
})
public class TaClient implements Serializable {

    protected Boolean actif;
    protected TaAdresse adresse1;
    protected String adresse1Banque;
    protected TaAdresse adresse2;
    protected String adresse2Banque;
    protected TaAdresse adresse3;
    protected String ancienCode;
    protected String bicSwiftBanque;
    protected String code;
    protected String cpBanque;
    protected String ibanBanque;
    protected Integer id;
    protected String identifiantFtp;
    @XmlElement(nillable = true)
    protected List<TaLicence> listeLicence;
    protected String mdpFtp;
    protected String nom;
    protected String nomBanque;
    protected String numeroTva;
    protected String prenom;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
    protected String serveurFtp;
    protected TaPartenaire taPartenaire;
    protected TaUtilisateur taUtilisateur;
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
     * Obtient la valeur de la propriété adresse1.
     * 
     * @return
     *     possible object is
     *     {@link TaAdresse }
     *     
     */
    public TaAdresse getAdresse1() {
        return adresse1;
    }

    /**
     * Définit la valeur de la propriété adresse1.
     * 
     * @param value
     *     allowed object is
     *     {@link TaAdresse }
     *     
     */
    public void setAdresse1(TaAdresse value) {
        this.adresse1 = value;
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
     * Obtient la valeur de la propriété adresse2.
     * 
     * @return
     *     possible object is
     *     {@link TaAdresse }
     *     
     */
    public TaAdresse getAdresse2() {
        return adresse2;
    }

    /**
     * Définit la valeur de la propriété adresse2.
     * 
     * @param value
     *     allowed object is
     *     {@link TaAdresse }
     *     
     */
    public void setAdresse2(TaAdresse value) {
        this.adresse2 = value;
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
     * Obtient la valeur de la propriété adresse3.
     * 
     * @return
     *     possible object is
     *     {@link TaAdresse }
     *     
     */
    public TaAdresse getAdresse3() {
        return adresse3;
    }

    /**
     * Définit la valeur de la propriété adresse3.
     * 
     * @param value
     *     allowed object is
     *     {@link TaAdresse }
     *     
     */
    public void setAdresse3(TaAdresse value) {
        this.adresse3 = value;
    }

    /**
     * Obtient la valeur de la propriété ancienCode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAncienCode() {
        return ancienCode;
    }

    /**
     * Définit la valeur de la propriété ancienCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAncienCode(String value) {
        this.ancienCode = value;
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
     * Obtient la valeur de la propriété code.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCode() {
        return code;
    }

    /**
     * Définit la valeur de la propriété code.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCode(String value) {
        this.code = value;
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
     * Obtient la valeur de la propriété identifiantFtp.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentifiantFtp() {
        return identifiantFtp;
    }

    /**
     * Définit la valeur de la propriété identifiantFtp.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentifiantFtp(String value) {
        this.identifiantFtp = value;
    }

    /**
     * Gets the value of the listeLicence property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listeLicence property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListeLicence().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaLicence }
     * 
     * 
     */
    public List<TaLicence> getListeLicence() {
        if (listeLicence == null) {
            listeLicence = new ArrayList<TaLicence>();
        }
        return this.listeLicence;
    }

    /**
     * Obtient la valeur de la propriété mdpFtp.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMdpFtp() {
        return mdpFtp;
    }

    /**
     * Définit la valeur de la propriété mdpFtp.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMdpFtp(String value) {
        this.mdpFtp = value;
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
     * Obtient la valeur de la propriété numeroTva.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroTva() {
        return numeroTva;
    }

    /**
     * Définit la valeur de la propriété numeroTva.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroTva(String value) {
        this.numeroTva = value;
    }

    /**
     * Obtient la valeur de la propriété prenom.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Définit la valeur de la propriété prenom.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrenom(String value) {
        this.prenom = value;
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
     * Obtient la valeur de la propriété serveurFtp.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServeurFtp() {
        return serveurFtp;
    }

    /**
     * Définit la valeur de la propriété serveurFtp.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServeurFtp(String value) {
        this.serveurFtp = value;
    }

    /**
     * Obtient la valeur de la propriété taPartenaire.
     * 
     * @return
     *     possible object is
     *     {@link TaPartenaire }
     *     
     */
    public TaPartenaire getTaPartenaire() {
        return taPartenaire;
    }

    /**
     * Définit la valeur de la propriété taPartenaire.
     * 
     * @param value
     *     allowed object is
     *     {@link TaPartenaire }
     *     
     */
    public void setTaPartenaire(TaPartenaire value) {
        this.taPartenaire = value;
    }

    /**
     * Obtient la valeur de la propriété taUtilisateur.
     * 
     * @return
     *     possible object is
     *     {@link TaUtilisateur }
     *     
     */
    public TaUtilisateur getTaUtilisateur() {
        return taUtilisateur;
    }

    /**
     * Définit la valeur de la propriété taUtilisateur.
     * 
     * @param value
     *     allowed object is
     *     {@link TaUtilisateur }
     *     
     */
    public void setTaUtilisateur(TaUtilisateur value) {
        this.taUtilisateur = value;
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
