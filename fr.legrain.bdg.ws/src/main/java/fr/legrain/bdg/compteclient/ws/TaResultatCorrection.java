
package fr.legrain.bdg.compteclient.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java pour taResultatCorrection complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taResultatCorrection"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="actionImmediate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dateCorrection" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dateForceValidation" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dateVisaDirection" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dateVisaServiceQualite" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="effectuee" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="idResultatCorrection" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ipAcces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="observation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quandCree" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quandModif" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quiCree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quiModif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="taBareme" type="{http://service.bdg.legrain.fr/}taBareme" minOccurs="0"/&gt;
 *         &lt;element name="taResultatConformite" type="{http://service.bdg.legrain.fr/}taResultatConformite" minOccurs="0"/&gt;
 *         &lt;element name="taResultatConformiteApresActionCorrective" type="{http://service.bdg.legrain.fr/}taResultatConformite" minOccurs="0"/&gt;
 *         &lt;element name="taUtilisateurControleur" type="{http://service.bdg.legrain.fr/}taUtilisateur" minOccurs="0"/&gt;
 *         &lt;element name="taUtilisateurDirection" type="{http://service.bdg.legrain.fr/}taUtilisateur" minOccurs="0"/&gt;
 *         &lt;element name="taUtilisateurForceValidation" type="{http://service.bdg.legrain.fr/}taUtilisateur" minOccurs="0"/&gt;
 *         &lt;element name="taUtilisateurServiceQualite" type="{http://service.bdg.legrain.fr/}taUtilisateur" minOccurs="0"/&gt;
 *         &lt;element name="validationForcee" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="valide" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
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
@XmlType(name = "taResultatCorrection", propOrder = {
    "actionImmediate",
    "dateCorrection",
    "dateForceValidation",
    "dateVisaDirection",
    "dateVisaServiceQualite",
    "effectuee",
    "idResultatCorrection",
    "ipAcces",
    "observation",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "taBareme",
    "taResultatConformite",
    "taResultatConformiteApresActionCorrective",
    "taUtilisateurControleur",
    "taUtilisateurDirection",
    "taUtilisateurForceValidation",
    "taUtilisateurServiceQualite",
    "validationForcee",
    "valide",
    "version",
    "versionObj"
})
public class TaResultatCorrection {

    protected String actionImmediate;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateCorrection;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateForceValidation;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateVisaDirection;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateVisaServiceQualite;
    protected Boolean effectuee;
    protected int idResultatCorrection;
    protected String ipAcces;
    protected String observation;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
    protected TaBareme taBareme;
    protected TaResultatConformite taResultatConformite;
    protected TaResultatConformite taResultatConformiteApresActionCorrective;
    protected TaUtilisateur taUtilisateurControleur;
    protected TaUtilisateur taUtilisateurDirection;
    protected TaUtilisateur taUtilisateurForceValidation;
    protected TaUtilisateur taUtilisateurServiceQualite;
    protected Boolean validationForcee;
    protected Boolean valide;
    protected String version;
    protected Integer versionObj;

    /**
     * Obtient la valeur de la propriété actionImmediate.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActionImmediate() {
        return actionImmediate;
    }

    /**
     * Définit la valeur de la propriété actionImmediate.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActionImmediate(String value) {
        this.actionImmediate = value;
    }

    /**
     * Obtient la valeur de la propriété dateCorrection.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateCorrection() {
        return dateCorrection;
    }

    /**
     * Définit la valeur de la propriété dateCorrection.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateCorrection(XMLGregorianCalendar value) {
        this.dateCorrection = value;
    }

    /**
     * Obtient la valeur de la propriété dateForceValidation.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateForceValidation() {
        return dateForceValidation;
    }

    /**
     * Définit la valeur de la propriété dateForceValidation.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateForceValidation(XMLGregorianCalendar value) {
        this.dateForceValidation = value;
    }

    /**
     * Obtient la valeur de la propriété dateVisaDirection.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateVisaDirection() {
        return dateVisaDirection;
    }

    /**
     * Définit la valeur de la propriété dateVisaDirection.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateVisaDirection(XMLGregorianCalendar value) {
        this.dateVisaDirection = value;
    }

    /**
     * Obtient la valeur de la propriété dateVisaServiceQualite.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateVisaServiceQualite() {
        return dateVisaServiceQualite;
    }

    /**
     * Définit la valeur de la propriété dateVisaServiceQualite.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateVisaServiceQualite(XMLGregorianCalendar value) {
        this.dateVisaServiceQualite = value;
    }

    /**
     * Obtient la valeur de la propriété effectuee.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEffectuee() {
        return effectuee;
    }

    /**
     * Définit la valeur de la propriété effectuee.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEffectuee(Boolean value) {
        this.effectuee = value;
    }

    /**
     * Obtient la valeur de la propriété idResultatCorrection.
     * 
     */
    public int getIdResultatCorrection() {
        return idResultatCorrection;
    }

    /**
     * Définit la valeur de la propriété idResultatCorrection.
     * 
     */
    public void setIdResultatCorrection(int value) {
        this.idResultatCorrection = value;
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
     * Obtient la valeur de la propriété observation.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObservation() {
        return observation;
    }

    /**
     * Définit la valeur de la propriété observation.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObservation(String value) {
        this.observation = value;
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
     * Obtient la valeur de la propriété taBareme.
     * 
     * @return
     *     possible object is
     *     {@link TaBareme }
     *     
     */
    public TaBareme getTaBareme() {
        return taBareme;
    }

    /**
     * Définit la valeur de la propriété taBareme.
     * 
     * @param value
     *     allowed object is
     *     {@link TaBareme }
     *     
     */
    public void setTaBareme(TaBareme value) {
        this.taBareme = value;
    }

    /**
     * Obtient la valeur de la propriété taResultatConformite.
     * 
     * @return
     *     possible object is
     *     {@link TaResultatConformite }
     *     
     */
    public TaResultatConformite getTaResultatConformite() {
        return taResultatConformite;
    }

    /**
     * Définit la valeur de la propriété taResultatConformite.
     * 
     * @param value
     *     allowed object is
     *     {@link TaResultatConformite }
     *     
     */
    public void setTaResultatConformite(TaResultatConformite value) {
        this.taResultatConformite = value;
    }

    /**
     * Obtient la valeur de la propriété taResultatConformiteApresActionCorrective.
     * 
     * @return
     *     possible object is
     *     {@link TaResultatConformite }
     *     
     */
    public TaResultatConformite getTaResultatConformiteApresActionCorrective() {
        return taResultatConformiteApresActionCorrective;
    }

    /**
     * Définit la valeur de la propriété taResultatConformiteApresActionCorrective.
     * 
     * @param value
     *     allowed object is
     *     {@link TaResultatConformite }
     *     
     */
    public void setTaResultatConformiteApresActionCorrective(TaResultatConformite value) {
        this.taResultatConformiteApresActionCorrective = value;
    }

    /**
     * Obtient la valeur de la propriété taUtilisateurControleur.
     * 
     * @return
     *     possible object is
     *     {@link TaUtilisateur }
     *     
     */
    public TaUtilisateur getTaUtilisateurControleur() {
        return taUtilisateurControleur;
    }

    /**
     * Définit la valeur de la propriété taUtilisateurControleur.
     * 
     * @param value
     *     allowed object is
     *     {@link TaUtilisateur }
     *     
     */
    public void setTaUtilisateurControleur(TaUtilisateur value) {
        this.taUtilisateurControleur = value;
    }

    /**
     * Obtient la valeur de la propriété taUtilisateurDirection.
     * 
     * @return
     *     possible object is
     *     {@link TaUtilisateur }
     *     
     */
    public TaUtilisateur getTaUtilisateurDirection() {
        return taUtilisateurDirection;
    }

    /**
     * Définit la valeur de la propriété taUtilisateurDirection.
     * 
     * @param value
     *     allowed object is
     *     {@link TaUtilisateur }
     *     
     */
    public void setTaUtilisateurDirection(TaUtilisateur value) {
        this.taUtilisateurDirection = value;
    }

    /**
     * Obtient la valeur de la propriété taUtilisateurForceValidation.
     * 
     * @return
     *     possible object is
     *     {@link TaUtilisateur }
     *     
     */
    public TaUtilisateur getTaUtilisateurForceValidation() {
        return taUtilisateurForceValidation;
    }

    /**
     * Définit la valeur de la propriété taUtilisateurForceValidation.
     * 
     * @param value
     *     allowed object is
     *     {@link TaUtilisateur }
     *     
     */
    public void setTaUtilisateurForceValidation(TaUtilisateur value) {
        this.taUtilisateurForceValidation = value;
    }

    /**
     * Obtient la valeur de la propriété taUtilisateurServiceQualite.
     * 
     * @return
     *     possible object is
     *     {@link TaUtilisateur }
     *     
     */
    public TaUtilisateur getTaUtilisateurServiceQualite() {
        return taUtilisateurServiceQualite;
    }

    /**
     * Définit la valeur de la propriété taUtilisateurServiceQualite.
     * 
     * @param value
     *     allowed object is
     *     {@link TaUtilisateur }
     *     
     */
    public void setTaUtilisateurServiceQualite(TaUtilisateur value) {
        this.taUtilisateurServiceQualite = value;
    }

    /**
     * Obtient la valeur de la propriété validationForcee.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isValidationForcee() {
        return validationForcee;
    }

    /**
     * Définit la valeur de la propriété validationForcee.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setValidationForcee(Boolean value) {
        this.validationForcee = value;
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
