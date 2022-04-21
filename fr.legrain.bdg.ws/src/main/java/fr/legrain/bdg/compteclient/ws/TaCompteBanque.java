
package fr.legrain.bdg.compteclient.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;


/**
 * <p>Classe Java pour taCompteBanque complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taCompteBanque"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="adresse1Banque" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="adresse2Banque" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="cleRib" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codeBIC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codeBanque" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codeGuichet" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="compte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="cpBanque" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="cptcomptable" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="iban" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idCompteBanque" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ipAcces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nomBanque" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nomCompte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quandCree" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quandModif" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quiCree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quiModif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="taTBanque" type="{http://service.bdg.legrain.fr/}taTBanque" minOccurs="0"/&gt;
 *         &lt;element name="taTiers" type="{http://service.bdg.legrain.fr/}taTiers" minOccurs="0"/&gt;
 *         &lt;element name="titulaire" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "taCompteBanque", propOrder = {
    "adresse1Banque",
    "adresse2Banque",
    "cleRib",
    "codeBIC",
    "codeBanque",
    "codeGuichet",
    "compte",
    "cpBanque",
    "cptcomptable",
    "iban",
    "idCompteBanque",
    "ipAcces",
    "nomBanque",
    "nomCompte",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "taTBanque",
    "taTiers",
    "titulaire",
    "version",
    "versionObj",
    "villeBanque"
})
public class TaCompteBanque {

    protected String adresse1Banque;
    protected String adresse2Banque;
    protected String cleRib;
    protected String codeBIC;
    protected String codeBanque;
    protected String codeGuichet;
    protected String compte;
    protected String cpBanque;
    protected String cptcomptable;
    protected String iban;
    protected int idCompteBanque;
    protected String ipAcces;
    protected String nomBanque;
    protected String nomCompte;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
    protected TaTBanque taTBanque;
    @XmlInverseReference(mappedBy = "taCompteBanques")
    @XmlElement
    protected TaTiers taTiers;
    protected String titulaire;
    protected String version;
    protected Integer versionObj;
    protected String villeBanque;

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
     * Obtient la valeur de la propriété cleRib.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCleRib() {
        return cleRib;
    }

    /**
     * Définit la valeur de la propriété cleRib.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCleRib(String value) {
        this.cleRib = value;
    }

    /**
     * Obtient la valeur de la propriété codeBIC.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeBIC() {
        return codeBIC;
    }

    /**
     * Définit la valeur de la propriété codeBIC.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeBIC(String value) {
        this.codeBIC = value;
    }

    /**
     * Obtient la valeur de la propriété codeBanque.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeBanque() {
        return codeBanque;
    }

    /**
     * Définit la valeur de la propriété codeBanque.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeBanque(String value) {
        this.codeBanque = value;
    }

    /**
     * Obtient la valeur de la propriété codeGuichet.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeGuichet() {
        return codeGuichet;
    }

    /**
     * Définit la valeur de la propriété codeGuichet.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeGuichet(String value) {
        this.codeGuichet = value;
    }

    /**
     * Obtient la valeur de la propriété compte.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompte() {
        return compte;
    }

    /**
     * Définit la valeur de la propriété compte.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompte(String value) {
        this.compte = value;
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
     * Obtient la valeur de la propriété cptcomptable.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCptcomptable() {
        return cptcomptable;
    }

    /**
     * Définit la valeur de la propriété cptcomptable.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCptcomptable(String value) {
        this.cptcomptable = value;
    }

    /**
     * Obtient la valeur de la propriété iban.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIban() {
        return iban;
    }

    /**
     * Définit la valeur de la propriété iban.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIban(String value) {
        this.iban = value;
    }

    /**
     * Obtient la valeur de la propriété idCompteBanque.
     * 
     */
    public int getIdCompteBanque() {
        return idCompteBanque;
    }

    /**
     * Définit la valeur de la propriété idCompteBanque.
     * 
     */
    public void setIdCompteBanque(int value) {
        this.idCompteBanque = value;
    }

    /**
     * Obtient la valeur de la propriété ipAcces.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIpAcces() {
        return ipAcces;
    }

    /**
     * Définit la valeur de la propriété ipAcces.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIpAcces(String value) {
        this.ipAcces = value;
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
     * Obtient la valeur de la propriété nomCompte.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomCompte() {
        return nomCompte;
    }

    /**
     * Définit la valeur de la propriété nomCompte.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomCompte(String value) {
        this.nomCompte = value;
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
     * Obtient la valeur de la propriété taTBanque.
     * 
     * @return
     *     possible object is
     *     {@link TaTBanque }
     *     
     */
    public TaTBanque getTaTBanque() {
        return taTBanque;
    }

    /**
     * Définit la valeur de la propriété taTBanque.
     * 
     * @param value
     *     allowed object is
     *     {@link TaTBanque }
     *     
     */
    public void setTaTBanque(TaTBanque value) {
        this.taTBanque = value;
    }

    /**
     * Obtient la valeur de la propriété taTiers.
     * 
     * @return
     *     possible object is
     *     {@link TaTiers }
     *     
     */
    public TaTiers getTaTiers() {
        return taTiers;
    }

    /**
     * Définit la valeur de la propriété taTiers.
     * 
     * @param value
     *     allowed object is
     *     {@link TaTiers }
     *     
     */
    public void setTaTiers(TaTiers value) {
        this.taTiers = value;
    }

    /**
     * Obtient la valeur de la propriété titulaire.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitulaire() {
        return titulaire;
    }

    /**
     * Définit la valeur de la propriété titulaire.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitulaire(String value) {
        this.titulaire = value;
    }

    /**
     * Obtient la valeur de la propriété version.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Définit la valeur de la propriété version.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
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
