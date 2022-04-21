
package fr.legrain.moncompte.ws;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java pour taProduit complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taProduit"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="compose" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descriptionHtml" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="eligibleCommission" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="idProduit" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="identifiantModule" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ipAcces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="libelle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="libelleHtml" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="listePrixParUtilisateur" type="{http://service.moncompte.legrain.fr/}taPrixParUtilisateur" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="listeProduitDependant" type="{http://service.moncompte.legrain.fr/}taProduit" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="listeSetup" type="{http://service.moncompte.legrain.fr/}taSetup" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="listeSousProduit" type="{http://service.moncompte.legrain.fr/}taProduit" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="prixHT" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="prixHTLicence" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="prixHTLight" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="prixHTMaintenance" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="prixHTParPosteInstalle" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="prixHTWs" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="prixTTC" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="prixTTCLicence" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="prixTTCLight" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="prixTTCMaintenance" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="prixTTCParPosteInstalle" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="prixTTCWs" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="quandCree" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quandModif" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quiCree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quiModif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="sousProduit" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="taGroupeProduit" type="{http://service.moncompte.legrain.fr/}taGroupeProduit" minOccurs="0"/&gt;
 *         &lt;element name="taTNiveau" type="{http://service.moncompte.legrain.fr/}taTNiveau" minOccurs="0"/&gt;
 *         &lt;element name="taTypeProduit" type="{http://service.moncompte.legrain.fr/}taTypeProduit" minOccurs="0"/&gt;
 *         &lt;element name="tauxTVA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="tva" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="tvaLicence" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="tvaLight" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="tvaMaintenance" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="tvaParPosteInstalle" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="tvaWs" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="vendable" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="versionBrowser" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="versionOS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="versionObj" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="versionProduit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "taProduit", propOrder = {
    "code",
    "compose",
    "description",
    "descriptionHtml",
    "eligibleCommission",
    "idProduit",
    "identifiantModule",
    "ipAcces",
    "libelle",
    "libelleHtml",
    "listePrixParUtilisateur",
    "listeProduitDependant",
    "listeSetup",
    "listeSousProduit",
    "prixHT",
    "prixHTLicence",
    "prixHTLight",
    "prixHTMaintenance",
    "prixHTParPosteInstalle",
    "prixHTWs",
    "prixTTC",
    "prixTTCLicence",
    "prixTTCLight",
    "prixTTCMaintenance",
    "prixTTCParPosteInstalle",
    "prixTTCWs",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "sousProduit",
    "taGroupeProduit",
    "taTNiveau",
    "taTypeProduit",
    "tauxTVA",
    "tva",
    "tvaLicence",
    "tvaLight",
    "tvaMaintenance",
    "tvaParPosteInstalle",
    "tvaWs",
    "vendable",
    "versionBrowser",
    "versionOS",
    "versionObj",
    "versionProduit"
})
public class TaProduit implements Serializable {

    protected String code;
    protected Boolean compose;
    protected String description;
    protected String descriptionHtml;
    protected Boolean eligibleCommission;
    protected Integer idProduit;
    protected String identifiantModule;
    protected String ipAcces;
    protected String libelle;
    protected String libelleHtml;
    @XmlElement(nillable = true)
    protected List<TaPrixParUtilisateur> listePrixParUtilisateur;
    @XmlElement(nillable = true)
    protected List<TaProduit> listeProduitDependant;
    @XmlElement(nillable = true)
    protected List<TaSetup> listeSetup;
    @XmlElement(nillable = true)
    protected List<TaProduit> listeSousProduit;
    protected BigDecimal prixHT;
    protected BigDecimal prixHTLicence;
    protected BigDecimal prixHTLight;
    protected BigDecimal prixHTMaintenance;
    protected BigDecimal prixHTParPosteInstalle;
    protected BigDecimal prixHTWs;
    protected BigDecimal prixTTC;
    protected BigDecimal prixTTCLicence;
    protected BigDecimal prixTTCLight;
    protected BigDecimal prixTTCMaintenance;
    protected BigDecimal prixTTCParPosteInstalle;
    protected BigDecimal prixTTCWs;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
    protected Boolean sousProduit;
    protected TaGroupeProduit taGroupeProduit;
    protected TaTNiveau taTNiveau;
    protected TaTypeProduit taTypeProduit;
    protected BigDecimal tauxTVA;
    protected BigDecimal tva;
    protected BigDecimal tvaLicence;
    protected BigDecimal tvaLight;
    protected BigDecimal tvaMaintenance;
    protected BigDecimal tvaParPosteInstalle;
    protected BigDecimal tvaWs;
    protected Boolean vendable;
    protected String versionBrowser;
    protected String versionOS;
    protected Integer versionObj;
    protected String versionProduit;

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
     * Obtient la valeur de la propriété compose.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCompose() {
        return compose;
    }

    /**
     * Définit la valeur de la propriété compose.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCompose(Boolean value) {
        this.compose = value;
    }

    /**
     * Obtient la valeur de la propriété description.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Définit la valeur de la propriété description.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Obtient la valeur de la propriété descriptionHtml.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescriptionHtml() {
        return descriptionHtml;
    }

    /**
     * Définit la valeur de la propriété descriptionHtml.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescriptionHtml(String value) {
        this.descriptionHtml = value;
    }

    /**
     * Obtient la valeur de la propriété eligibleCommission.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEligibleCommission() {
        return eligibleCommission;
    }

    /**
     * Définit la valeur de la propriété eligibleCommission.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEligibleCommission(Boolean value) {
        this.eligibleCommission = value;
    }

    /**
     * Obtient la valeur de la propriété idProduit.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIdProduit() {
        return idProduit;
    }

    /**
     * Définit la valeur de la propriété idProduit.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIdProduit(Integer value) {
        this.idProduit = value;
    }

    /**
     * Obtient la valeur de la propriété identifiantModule.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentifiantModule() {
        return identifiantModule;
    }

    /**
     * Définit la valeur de la propriété identifiantModule.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentifiantModule(String value) {
        this.identifiantModule = value;
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
     * Obtient la valeur de la propriété libelle.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Définit la valeur de la propriété libelle.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLibelle(String value) {
        this.libelle = value;
    }

    /**
     * Obtient la valeur de la propriété libelleHtml.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLibelleHtml() {
        return libelleHtml;
    }

    /**
     * Définit la valeur de la propriété libelleHtml.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLibelleHtml(String value) {
        this.libelleHtml = value;
    }

    /**
     * Gets the value of the listePrixParUtilisateur property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listePrixParUtilisateur property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListePrixParUtilisateur().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaPrixParUtilisateur }
     * 
     * 
     */
    public List<TaPrixParUtilisateur> getListePrixParUtilisateur() {
        if (listePrixParUtilisateur == null) {
            listePrixParUtilisateur = new ArrayList<TaPrixParUtilisateur>();
        }
        return this.listePrixParUtilisateur;
    }

    /**
     * Gets the value of the listeProduitDependant property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listeProduitDependant property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListeProduitDependant().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaProduit }
     * 
     * 
     */
    public List<TaProduit> getListeProduitDependant() {
        if (listeProduitDependant == null) {
            listeProduitDependant = new ArrayList<TaProduit>();
        }
        return this.listeProduitDependant;
    }

    /**
     * Gets the value of the listeSetup property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listeSetup property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListeSetup().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaSetup }
     * 
     * 
     */
    public List<TaSetup> getListeSetup() {
        if (listeSetup == null) {
            listeSetup = new ArrayList<TaSetup>();
        }
        return this.listeSetup;
    }

    /**
     * Gets the value of the listeSousProduit property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listeSousProduit property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListeSousProduit().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaProduit }
     * 
     * 
     */
    public List<TaProduit> getListeSousProduit() {
        if (listeSousProduit == null) {
            listeSousProduit = new ArrayList<TaProduit>();
        }
        return this.listeSousProduit;
    }

    /**
     * Obtient la valeur de la propriété prixHT.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPrixHT() {
        return prixHT;
    }

    /**
     * Définit la valeur de la propriété prixHT.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPrixHT(BigDecimal value) {
        this.prixHT = value;
    }

    /**
     * Obtient la valeur de la propriété prixHTLicence.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPrixHTLicence() {
        return prixHTLicence;
    }

    /**
     * Définit la valeur de la propriété prixHTLicence.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPrixHTLicence(BigDecimal value) {
        this.prixHTLicence = value;
    }

    /**
     * Obtient la valeur de la propriété prixHTLight.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPrixHTLight() {
        return prixHTLight;
    }

    /**
     * Définit la valeur de la propriété prixHTLight.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPrixHTLight(BigDecimal value) {
        this.prixHTLight = value;
    }

    /**
     * Obtient la valeur de la propriété prixHTMaintenance.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPrixHTMaintenance() {
        return prixHTMaintenance;
    }

    /**
     * Définit la valeur de la propriété prixHTMaintenance.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPrixHTMaintenance(BigDecimal value) {
        this.prixHTMaintenance = value;
    }

    /**
     * Obtient la valeur de la propriété prixHTParPosteInstalle.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPrixHTParPosteInstalle() {
        return prixHTParPosteInstalle;
    }

    /**
     * Définit la valeur de la propriété prixHTParPosteInstalle.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPrixHTParPosteInstalle(BigDecimal value) {
        this.prixHTParPosteInstalle = value;
    }

    /**
     * Obtient la valeur de la propriété prixHTWs.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPrixHTWs() {
        return prixHTWs;
    }

    /**
     * Définit la valeur de la propriété prixHTWs.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPrixHTWs(BigDecimal value) {
        this.prixHTWs = value;
    }

    /**
     * Obtient la valeur de la propriété prixTTC.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPrixTTC() {
        return prixTTC;
    }

    /**
     * Définit la valeur de la propriété prixTTC.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPrixTTC(BigDecimal value) {
        this.prixTTC = value;
    }

    /**
     * Obtient la valeur de la propriété prixTTCLicence.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPrixTTCLicence() {
        return prixTTCLicence;
    }

    /**
     * Définit la valeur de la propriété prixTTCLicence.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPrixTTCLicence(BigDecimal value) {
        this.prixTTCLicence = value;
    }

    /**
     * Obtient la valeur de la propriété prixTTCLight.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPrixTTCLight() {
        return prixTTCLight;
    }

    /**
     * Définit la valeur de la propriété prixTTCLight.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPrixTTCLight(BigDecimal value) {
        this.prixTTCLight = value;
    }

    /**
     * Obtient la valeur de la propriété prixTTCMaintenance.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPrixTTCMaintenance() {
        return prixTTCMaintenance;
    }

    /**
     * Définit la valeur de la propriété prixTTCMaintenance.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPrixTTCMaintenance(BigDecimal value) {
        this.prixTTCMaintenance = value;
    }

    /**
     * Obtient la valeur de la propriété prixTTCParPosteInstalle.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPrixTTCParPosteInstalle() {
        return prixTTCParPosteInstalle;
    }

    /**
     * Définit la valeur de la propriété prixTTCParPosteInstalle.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPrixTTCParPosteInstalle(BigDecimal value) {
        this.prixTTCParPosteInstalle = value;
    }

    /**
     * Obtient la valeur de la propriété prixTTCWs.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPrixTTCWs() {
        return prixTTCWs;
    }

    /**
     * Définit la valeur de la propriété prixTTCWs.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPrixTTCWs(BigDecimal value) {
        this.prixTTCWs = value;
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
     * Obtient la valeur de la propriété sousProduit.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSousProduit() {
        return sousProduit;
    }

    /**
     * Définit la valeur de la propriété sousProduit.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSousProduit(Boolean value) {
        this.sousProduit = value;
    }

    /**
     * Obtient la valeur de la propriété taGroupeProduit.
     * 
     * @return
     *     possible object is
     *     {@link TaGroupeProduit }
     *     
     */
    public TaGroupeProduit getTaGroupeProduit() {
        return taGroupeProduit;
    }

    /**
     * Définit la valeur de la propriété taGroupeProduit.
     * 
     * @param value
     *     allowed object is
     *     {@link TaGroupeProduit }
     *     
     */
    public void setTaGroupeProduit(TaGroupeProduit value) {
        this.taGroupeProduit = value;
    }

    /**
     * Obtient la valeur de la propriété taTNiveau.
     * 
     * @return
     *     possible object is
     *     {@link TaTNiveau }
     *     
     */
    public TaTNiveau getTaTNiveau() {
        return taTNiveau;
    }

    /**
     * Définit la valeur de la propriété taTNiveau.
     * 
     * @param value
     *     allowed object is
     *     {@link TaTNiveau }
     *     
     */
    public void setTaTNiveau(TaTNiveau value) {
        this.taTNiveau = value;
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
     * Obtient la valeur de la propriété tauxTVA.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTauxTVA() {
        return tauxTVA;
    }

    /**
     * Définit la valeur de la propriété tauxTVA.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTauxTVA(BigDecimal value) {
        this.tauxTVA = value;
    }

    /**
     * Obtient la valeur de la propriété tva.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTva() {
        return tva;
    }

    /**
     * Définit la valeur de la propriété tva.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTva(BigDecimal value) {
        this.tva = value;
    }

    /**
     * Obtient la valeur de la propriété tvaLicence.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTvaLicence() {
        return tvaLicence;
    }

    /**
     * Définit la valeur de la propriété tvaLicence.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTvaLicence(BigDecimal value) {
        this.tvaLicence = value;
    }

    /**
     * Obtient la valeur de la propriété tvaLight.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTvaLight() {
        return tvaLight;
    }

    /**
     * Définit la valeur de la propriété tvaLight.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTvaLight(BigDecimal value) {
        this.tvaLight = value;
    }

    /**
     * Obtient la valeur de la propriété tvaMaintenance.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTvaMaintenance() {
        return tvaMaintenance;
    }

    /**
     * Définit la valeur de la propriété tvaMaintenance.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTvaMaintenance(BigDecimal value) {
        this.tvaMaintenance = value;
    }

    /**
     * Obtient la valeur de la propriété tvaParPosteInstalle.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTvaParPosteInstalle() {
        return tvaParPosteInstalle;
    }

    /**
     * Définit la valeur de la propriété tvaParPosteInstalle.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTvaParPosteInstalle(BigDecimal value) {
        this.tvaParPosteInstalle = value;
    }

    /**
     * Obtient la valeur de la propriété tvaWs.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTvaWs() {
        return tvaWs;
    }

    /**
     * Définit la valeur de la propriété tvaWs.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTvaWs(BigDecimal value) {
        this.tvaWs = value;
    }

    /**
     * Obtient la valeur de la propriété vendable.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isVendable() {
        return vendable;
    }

    /**
     * Définit la valeur de la propriété vendable.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setVendable(Boolean value) {
        this.vendable = value;
    }

    /**
     * Obtient la valeur de la propriété versionBrowser.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersionBrowser() {
        return versionBrowser;
    }

    /**
     * Définit la valeur de la propriété versionBrowser.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersionBrowser(String value) {
        this.versionBrowser = value;
    }

    /**
     * Obtient la valeur de la propriété versionOS.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersionOS() {
        return versionOS;
    }

    /**
     * Définit la valeur de la propriété versionOS.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersionOS(String value) {
        this.versionOS = value;
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
     * Obtient la valeur de la propriété versionProduit.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersionProduit() {
        return versionProduit;
    }

    /**
     * Définit la valeur de la propriété versionProduit.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersionProduit(String value) {
        this.versionProduit = value;
    }

}
