
package fr.legrain.bdg.compteclient.ws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java pour taTiers complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taTiers"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="actifTiers" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="blobLogo" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 *         &lt;element name="cleLiaisonCompteClient" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codeCompta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codeTiers" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="compte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="contact" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="dateAnniv" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dateDerniereConnexionCompteClient" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="emailCleCompteClientEnvoye" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="idImport" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idTiers" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ipAcces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="maRefTiers" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nomTiers" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="origineImport" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="prenomTiers" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quandCree" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quandModif" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quiCree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quiModif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="surnomTiers" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="systeme" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="taAdresse" type="{http://service.bdg.legrain.fr/}taAdresse" minOccurs="0"/&gt;
 *         &lt;element name="taAdresses" type="{http://service.bdg.legrain.fr/}taAdresse" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="taCPaiement" type="{http://service.bdg.legrain.fr/}taCPaiement" minOccurs="0"/&gt;
 *         &lt;element name="taCommentaire" type="{http://service.bdg.legrain.fr/}taCommentaire" minOccurs="0"/&gt;
 *         &lt;element name="taCommercial" type="{http://service.bdg.legrain.fr/}taTiers" minOccurs="0"/&gt;
 *         &lt;element name="taCompl" type="{http://service.bdg.legrain.fr/}taCompl" minOccurs="0"/&gt;
 *         &lt;element name="taCompteBanques" type="{http://service.bdg.legrain.fr/}taCompteBanque" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="taConctacts" type="{http://service.bdg.legrain.fr/}taTiers" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="taContact" type="{http://service.bdg.legrain.fr/}taTiers" minOccurs="0"/&gt;
 *         &lt;element name="taEmail" type="{http://service.bdg.legrain.fr/}taEmail" minOccurs="0"/&gt;
 *         &lt;element name="taEmails" type="{http://service.bdg.legrain.fr/}taEmail" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="taEntreprise" type="{http://service.bdg.legrain.fr/}taEntreprise" minOccurs="0"/&gt;
 *         &lt;element name="taFamilleTiers" type="{http://service.bdg.legrain.fr/}taFamilleTiers" minOccurs="0"/&gt;
 *         &lt;element name="taFamilleTierses" type="{http://service.bdg.legrain.fr/}taFamilleTiers" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="taInfoJuridique" type="{http://service.bdg.legrain.fr/}taInfoJuridique" minOccurs="0"/&gt;
 *         &lt;element name="taTCivilite" type="{http://service.bdg.legrain.fr/}taTCivilite" minOccurs="0"/&gt;
 *         &lt;element name="taTEntite" type="{http://service.bdg.legrain.fr/}taTEntite" minOccurs="0"/&gt;
 *         &lt;element name="taTPaiement" type="{http://service.bdg.legrain.fr/}taTPaiement" minOccurs="0"/&gt;
 *         &lt;element name="taTTarif" type="{http://service.bdg.legrain.fr/}taTTarif" minOccurs="0"/&gt;
 *         &lt;element name="taTTiers" type="{http://service.bdg.legrain.fr/}taTTiers" minOccurs="0"/&gt;
 *         &lt;element name="taTTvaDoc" type="{http://service.bdg.legrain.fr/}taTTvaDoc" minOccurs="0"/&gt;
 *         &lt;element name="taTelephone" type="{http://service.bdg.legrain.fr/}taTelephone" minOccurs="0"/&gt;
 *         &lt;element name="taTelephones" type="{http://service.bdg.legrain.fr/}taTelephone" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="taWeb" type="{http://service.bdg.legrain.fr/}taWeb" minOccurs="0"/&gt;
 *         &lt;element name="taWebs" type="{http://service.bdg.legrain.fr/}taWeb" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="ttcTiers" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="utiliseCompteClient" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="versionObj" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "taTiers", propOrder = {
    "actifTiers",
    "blobLogo",
    "cleLiaisonCompteClient",
    "codeCompta",
    "codeTiers",
    "compte",
    "contact",
    "dateAnniv",
    "dateDerniereConnexionCompteClient",
    "emailCleCompteClientEnvoye",
    "idImport",
    "idTiers",
    "ipAcces",
    "maRefTiers",
    "nomTiers",
    "origineImport",
    "prenomTiers",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "surnomTiers",
    "systeme",
    "taAdresse",
    "taAdresses",
    "taCPaiement",
    "taCommentaire",
    "taCommercial",
    "taCompl",
    "taCompteBanques",
    "taConctacts",
    "taContact",
    "taEmail",
    "taEmails",
    "taEntreprise",
    "taFamilleTiers",
    "taFamilleTierses",
    "taInfoJuridique",
    "taTCivilite",
    "taTEntite",
    "taTPaiement",
    "taTTarif",
    "taTTiers",
    "taTTvaDoc",
    "taTelephone",
    "taTelephones",
    "taWeb",
    "taWebs",
    "ttcTiers",
    "utiliseCompteClient",
    "version",
    "versionObj"
})
public class TaTiers {

    protected Integer actifTiers;
    protected byte[] blobLogo;
    protected String cleLiaisonCompteClient;
    protected String codeCompta;
    protected String codeTiers;
    protected String compte;
    protected Boolean contact;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateAnniv;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateDerniereConnexionCompteClient;
    protected Boolean emailCleCompteClientEnvoye;
    protected String idImport;
    protected int idTiers;
    protected String ipAcces;
    protected String maRefTiers;
    protected String nomTiers;
    protected String origineImport;
    protected String prenomTiers;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
    protected String surnomTiers;
    protected Boolean systeme;
    protected TaAdresse taAdresse;
    @XmlElement(nillable = true)
    protected List<TaAdresse> taAdresses;
    protected TaCPaiement taCPaiement;
    protected TaCommentaire taCommentaire;
    protected TaTiers taCommercial;
    protected TaCompl taCompl;
    @XmlElement(nillable = true)
    protected List<TaCompteBanque> taCompteBanques;
    @XmlElement(nillable = true)
    protected List<TaTiers> taConctacts;
    protected TaTiers taContact;
    protected TaEmail taEmail;
    @XmlElement(nillable = true)
    protected List<TaEmail> taEmails;
    protected TaEntreprise taEntreprise;
    protected TaFamilleTiers taFamilleTiers;
    @XmlElement(nillable = true)
    protected List<TaFamilleTiers> taFamilleTierses;
    protected TaInfoJuridique taInfoJuridique;
    protected TaTCivilite taTCivilite;
    protected TaTEntite taTEntite;
    protected TaTPaiement taTPaiement;
    protected TaTTarif taTTarif;
    protected TaTTiers taTTiers;
    protected TaTTvaDoc taTTvaDoc;
    protected TaTelephone taTelephone;
    @XmlElement(nillable = true)
    protected List<TaTelephone> taTelephones;
    protected TaWeb taWeb;
    @XmlElement(nillable = true)
    protected List<TaWeb> taWebs;
    protected Integer ttcTiers;
    protected Boolean utiliseCompteClient;
    protected String version;
    protected Integer versionObj;

    /**
     * Obtient la valeur de la propriété actifTiers.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getActifTiers() {
        return actifTiers;
    }

    /**
     * Définit la valeur de la propriété actifTiers.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setActifTiers(Integer value) {
        this.actifTiers = value;
    }

    /**
     * Obtient la valeur de la propriété blobLogo.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getBlobLogo() {
        return blobLogo;
    }

    /**
     * Définit la valeur de la propriété blobLogo.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setBlobLogo(byte[] value) {
        this.blobLogo = value;
    }

    /**
     * Obtient la valeur de la propriété cleLiaisonCompteClient.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCleLiaisonCompteClient() {
        return cleLiaisonCompteClient;
    }

    /**
     * Définit la valeur de la propriété cleLiaisonCompteClient.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCleLiaisonCompteClient(String value) {
        this.cleLiaisonCompteClient = value;
    }

    /**
     * Obtient la valeur de la propriété codeCompta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeCompta() {
        return codeCompta;
    }

    /**
     * Définit la valeur de la propriété codeCompta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeCompta(String value) {
        this.codeCompta = value;
    }

    /**
     * Obtient la valeur de la propriété codeTiers.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeTiers() {
        return codeTiers;
    }

    /**
     * Définit la valeur de la propriété codeTiers.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeTiers(String value) {
        this.codeTiers = value;
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
     * Obtient la valeur de la propriété contact.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isContact() {
        return contact;
    }

    /**
     * Définit la valeur de la propriété contact.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setContact(Boolean value) {
        this.contact = value;
    }

    /**
     * Obtient la valeur de la propriété dateAnniv.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateAnniv() {
        return dateAnniv;
    }

    /**
     * Définit la valeur de la propriété dateAnniv.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateAnniv(XMLGregorianCalendar value) {
        this.dateAnniv = value;
    }

    /**
     * Obtient la valeur de la propriété dateDerniereConnexionCompteClient.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateDerniereConnexionCompteClient() {
        return dateDerniereConnexionCompteClient;
    }

    /**
     * Définit la valeur de la propriété dateDerniereConnexionCompteClient.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateDerniereConnexionCompteClient(XMLGregorianCalendar value) {
        this.dateDerniereConnexionCompteClient = value;
    }

    /**
     * Obtient la valeur de la propriété emailCleCompteClientEnvoye.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEmailCleCompteClientEnvoye() {
        return emailCleCompteClientEnvoye;
    }

    /**
     * Définit la valeur de la propriété emailCleCompteClientEnvoye.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEmailCleCompteClientEnvoye(Boolean value) {
        this.emailCleCompteClientEnvoye = value;
    }

    /**
     * Obtient la valeur de la propriété idImport.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdImport() {
        return idImport;
    }

    /**
     * Définit la valeur de la propriété idImport.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdImport(String value) {
        this.idImport = value;
    }

    /**
     * Obtient la valeur de la propriété idTiers.
     * 
     */
    public int getIdTiers() {
        return idTiers;
    }

    /**
     * Définit la valeur de la propriété idTiers.
     * 
     */
    public void setIdTiers(int value) {
        this.idTiers = value;
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
     * Obtient la valeur de la propriété maRefTiers.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaRefTiers() {
        return maRefTiers;
    }

    /**
     * Définit la valeur de la propriété maRefTiers.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaRefTiers(String value) {
        this.maRefTiers = value;
    }

    /**
     * Obtient la valeur de la propriété nomTiers.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomTiers() {
        return nomTiers;
    }

    /**
     * Définit la valeur de la propriété nomTiers.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomTiers(String value) {
        this.nomTiers = value;
    }

    /**
     * Obtient la valeur de la propriété origineImport.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrigineImport() {
        return origineImport;
    }

    /**
     * Définit la valeur de la propriété origineImport.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrigineImport(String value) {
        this.origineImport = value;
    }

    /**
     * Obtient la valeur de la propriété prenomTiers.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrenomTiers() {
        return prenomTiers;
    }

    /**
     * Définit la valeur de la propriété prenomTiers.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrenomTiers(String value) {
        this.prenomTiers = value;
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
     * Obtient la valeur de la propriété surnomTiers.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSurnomTiers() {
        return surnomTiers;
    }

    /**
     * Définit la valeur de la propriété surnomTiers.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSurnomTiers(String value) {
        this.surnomTiers = value;
    }

    /**
     * Obtient la valeur de la propriété systeme.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSysteme() {
        return systeme;
    }

    /**
     * Définit la valeur de la propriété systeme.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSysteme(Boolean value) {
        this.systeme = value;
    }

    /**
     * Obtient la valeur de la propriété taAdresse.
     * 
     * @return
     *     possible object is
     *     {@link TaAdresse }
     *     
     */
    public TaAdresse getTaAdresse() {
        return taAdresse;
    }

    /**
     * Définit la valeur de la propriété taAdresse.
     * 
     * @param value
     *     allowed object is
     *     {@link TaAdresse }
     *     
     */
    public void setTaAdresse(TaAdresse value) {
        this.taAdresse = value;
    }

    /**
     * Gets the value of the taAdresses property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taAdresses property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaAdresses().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaAdresse }
     * 
     * 
     */
    public List<TaAdresse> getTaAdresses() {
        if (taAdresses == null) {
            taAdresses = new ArrayList<TaAdresse>();
        }
        return this.taAdresses;
    }

    /**
     * Obtient la valeur de la propriété taCPaiement.
     * 
     * @return
     *     possible object is
     *     {@link TaCPaiement }
     *     
     */
    public TaCPaiement getTaCPaiement() {
        return taCPaiement;
    }

    /**
     * Définit la valeur de la propriété taCPaiement.
     * 
     * @param value
     *     allowed object is
     *     {@link TaCPaiement }
     *     
     */
    public void setTaCPaiement(TaCPaiement value) {
        this.taCPaiement = value;
    }

    /**
     * Obtient la valeur de la propriété taCommentaire.
     * 
     * @return
     *     possible object is
     *     {@link TaCommentaire }
     *     
     */
    public TaCommentaire getTaCommentaire() {
        return taCommentaire;
    }

    /**
     * Définit la valeur de la propriété taCommentaire.
     * 
     * @param value
     *     allowed object is
     *     {@link TaCommentaire }
     *     
     */
    public void setTaCommentaire(TaCommentaire value) {
        this.taCommentaire = value;
    }

    /**
     * Obtient la valeur de la propriété taCommercial.
     * 
     * @return
     *     possible object is
     *     {@link TaTiers }
     *     
     */
    public TaTiers getTaCommercial() {
        return taCommercial;
    }

    /**
     * Définit la valeur de la propriété taCommercial.
     * 
     * @param value
     *     allowed object is
     *     {@link TaTiers }
     *     
     */
    public void setTaCommercial(TaTiers value) {
        this.taCommercial = value;
    }

    /**
     * Obtient la valeur de la propriété taCompl.
     * 
     * @return
     *     possible object is
     *     {@link TaCompl }
     *     
     */
    public TaCompl getTaCompl() {
        return taCompl;
    }

    /**
     * Définit la valeur de la propriété taCompl.
     * 
     * @param value
     *     allowed object is
     *     {@link TaCompl }
     *     
     */
    public void setTaCompl(TaCompl value) {
        this.taCompl = value;
    }

    /**
     * Gets the value of the taCompteBanques property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taCompteBanques property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaCompteBanques().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaCompteBanque }
     * 
     * 
     */
    public List<TaCompteBanque> getTaCompteBanques() {
        if (taCompteBanques == null) {
            taCompteBanques = new ArrayList<TaCompteBanque>();
        }
        return this.taCompteBanques;
    }

    /**
     * Gets the value of the taConctacts property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taConctacts property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaConctacts().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaTiers }
     * 
     * 
     */
    public List<TaTiers> getTaConctacts() {
        if (taConctacts == null) {
            taConctacts = new ArrayList<TaTiers>();
        }
        return this.taConctacts;
    }

    /**
     * Obtient la valeur de la propriété taContact.
     * 
     * @return
     *     possible object is
     *     {@link TaTiers }
     *     
     */
    public TaTiers getTaContact() {
        return taContact;
    }

    /**
     * Définit la valeur de la propriété taContact.
     * 
     * @param value
     *     allowed object is
     *     {@link TaTiers }
     *     
     */
    public void setTaContact(TaTiers value) {
        this.taContact = value;
    }

    /**
     * Obtient la valeur de la propriété taEmail.
     * 
     * @return
     *     possible object is
     *     {@link TaEmail }
     *     
     */
    public TaEmail getTaEmail() {
        return taEmail;
    }

    /**
     * Définit la valeur de la propriété taEmail.
     * 
     * @param value
     *     allowed object is
     *     {@link TaEmail }
     *     
     */
    public void setTaEmail(TaEmail value) {
        this.taEmail = value;
    }

    /**
     * Gets the value of the taEmails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taEmails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaEmails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaEmail }
     * 
     * 
     */
    public List<TaEmail> getTaEmails() {
        if (taEmails == null) {
            taEmails = new ArrayList<TaEmail>();
        }
        return this.taEmails;
    }

    /**
     * Obtient la valeur de la propriété taEntreprise.
     * 
     * @return
     *     possible object is
     *     {@link TaEntreprise }
     *     
     */
    public TaEntreprise getTaEntreprise() {
        return taEntreprise;
    }

    /**
     * Définit la valeur de la propriété taEntreprise.
     * 
     * @param value
     *     allowed object is
     *     {@link TaEntreprise }
     *     
     */
    public void setTaEntreprise(TaEntreprise value) {
        this.taEntreprise = value;
    }

    /**
     * Obtient la valeur de la propriété taFamilleTiers.
     * 
     * @return
     *     possible object is
     *     {@link TaFamilleTiers }
     *     
     */
    public TaFamilleTiers getTaFamilleTiers() {
        return taFamilleTiers;
    }

    /**
     * Définit la valeur de la propriété taFamilleTiers.
     * 
     * @param value
     *     allowed object is
     *     {@link TaFamilleTiers }
     *     
     */
    public void setTaFamilleTiers(TaFamilleTiers value) {
        this.taFamilleTiers = value;
    }

    /**
     * Gets the value of the taFamilleTierses property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taFamilleTierses property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaFamilleTierses().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaFamilleTiers }
     * 
     * 
     */
    public List<TaFamilleTiers> getTaFamilleTierses() {
        if (taFamilleTierses == null) {
            taFamilleTierses = new ArrayList<TaFamilleTiers>();
        }
        return this.taFamilleTierses;
    }

    /**
     * Obtient la valeur de la propriété taInfoJuridique.
     * 
     * @return
     *     possible object is
     *     {@link TaInfoJuridique }
     *     
     */
    public TaInfoJuridique getTaInfoJuridique() {
        return taInfoJuridique;
    }

    /**
     * Définit la valeur de la propriété taInfoJuridique.
     * 
     * @param value
     *     allowed object is
     *     {@link TaInfoJuridique }
     *     
     */
    public void setTaInfoJuridique(TaInfoJuridique value) {
        this.taInfoJuridique = value;
    }

    /**
     * Obtient la valeur de la propriété taTCivilite.
     * 
     * @return
     *     possible object is
     *     {@link TaTCivilite }
     *     
     */
    public TaTCivilite getTaTCivilite() {
        return taTCivilite;
    }

    /**
     * Définit la valeur de la propriété taTCivilite.
     * 
     * @param value
     *     allowed object is
     *     {@link TaTCivilite }
     *     
     */
    public void setTaTCivilite(TaTCivilite value) {
        this.taTCivilite = value;
    }

    /**
     * Obtient la valeur de la propriété taTEntite.
     * 
     * @return
     *     possible object is
     *     {@link TaTEntite }
     *     
     */
    public TaTEntite getTaTEntite() {
        return taTEntite;
    }

    /**
     * Définit la valeur de la propriété taTEntite.
     * 
     * @param value
     *     allowed object is
     *     {@link TaTEntite }
     *     
     */
    public void setTaTEntite(TaTEntite value) {
        this.taTEntite = value;
    }

    /**
     * Obtient la valeur de la propriété taTPaiement.
     * 
     * @return
     *     possible object is
     *     {@link TaTPaiement }
     *     
     */
    public TaTPaiement getTaTPaiement() {
        return taTPaiement;
    }

    /**
     * Définit la valeur de la propriété taTPaiement.
     * 
     * @param value
     *     allowed object is
     *     {@link TaTPaiement }
     *     
     */
    public void setTaTPaiement(TaTPaiement value) {
        this.taTPaiement = value;
    }

    /**
     * Obtient la valeur de la propriété taTTarif.
     * 
     * @return
     *     possible object is
     *     {@link TaTTarif }
     *     
     */
    public TaTTarif getTaTTarif() {
        return taTTarif;
    }

    /**
     * Définit la valeur de la propriété taTTarif.
     * 
     * @param value
     *     allowed object is
     *     {@link TaTTarif }
     *     
     */
    public void setTaTTarif(TaTTarif value) {
        this.taTTarif = value;
    }

    /**
     * Obtient la valeur de la propriété taTTiers.
     * 
     * @return
     *     possible object is
     *     {@link TaTTiers }
     *     
     */
    public TaTTiers getTaTTiers() {
        return taTTiers;
    }

    /**
     * Définit la valeur de la propriété taTTiers.
     * 
     * @param value
     *     allowed object is
     *     {@link TaTTiers }
     *     
     */
    public void setTaTTiers(TaTTiers value) {
        this.taTTiers = value;
    }

    /**
     * Obtient la valeur de la propriété taTTvaDoc.
     * 
     * @return
     *     possible object is
     *     {@link TaTTvaDoc }
     *     
     */
    public TaTTvaDoc getTaTTvaDoc() {
        return taTTvaDoc;
    }

    /**
     * Définit la valeur de la propriété taTTvaDoc.
     * 
     * @param value
     *     allowed object is
     *     {@link TaTTvaDoc }
     *     
     */
    public void setTaTTvaDoc(TaTTvaDoc value) {
        this.taTTvaDoc = value;
    }

    /**
     * Obtient la valeur de la propriété taTelephone.
     * 
     * @return
     *     possible object is
     *     {@link TaTelephone }
     *     
     */
    public TaTelephone getTaTelephone() {
        return taTelephone;
    }

    /**
     * Définit la valeur de la propriété taTelephone.
     * 
     * @param value
     *     allowed object is
     *     {@link TaTelephone }
     *     
     */
    public void setTaTelephone(TaTelephone value) {
        this.taTelephone = value;
    }

    /**
     * Gets the value of the taTelephones property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taTelephones property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaTelephones().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaTelephone }
     * 
     * 
     */
    public List<TaTelephone> getTaTelephones() {
        if (taTelephones == null) {
            taTelephones = new ArrayList<TaTelephone>();
        }
        return this.taTelephones;
    }

    /**
     * Obtient la valeur de la propriété taWeb.
     * 
     * @return
     *     possible object is
     *     {@link TaWeb }
     *     
     */
    public TaWeb getTaWeb() {
        return taWeb;
    }

    /**
     * Définit la valeur de la propriété taWeb.
     * 
     * @param value
     *     allowed object is
     *     {@link TaWeb }
     *     
     */
    public void setTaWeb(TaWeb value) {
        this.taWeb = value;
    }

    /**
     * Gets the value of the taWebs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taWebs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaWebs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaWeb }
     * 
     * 
     */
    public List<TaWeb> getTaWebs() {
        if (taWebs == null) {
            taWebs = new ArrayList<TaWeb>();
        }
        return this.taWebs;
    }

    /**
     * Obtient la valeur de la propriété ttcTiers.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTtcTiers() {
        return ttcTiers;
    }

    /**
     * Définit la valeur de la propriété ttcTiers.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTtcTiers(Integer value) {
        this.ttcTiers = value;
    }

    /**
     * Obtient la valeur de la propriété utiliseCompteClient.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isUtiliseCompteClient() {
        return utiliseCompteClient;
    }

    /**
     * Définit la valeur de la propriété utiliseCompteClient.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setUtiliseCompteClient(Boolean value) {
        this.utiliseCompteClient = value;
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

}
