
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
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;


/**
 * <p>Classe Java pour taDossier complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taDossier"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="actif" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="modeLocatif" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="nbUtilisateur" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="nbPosteInstalle" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="accesWs" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="codePartenaire" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tauxReduction" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="taClient" type="{http://service.moncompte.legrain.fr/}taClient" minOccurs="0"/&gt;
 *         &lt;element name="listeAutorisation" type="{http://service.moncompte.legrain.fr/}taAutorisation" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="listePrixPerso" type="{http://service.moncompte.legrain.fr/}taPrixPerso" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="listePrixParUtilisateurPerso" type="{http://service.moncompte.legrain.fr/}taPrixParUtilisateurPerso" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="taCategoriePro" type="{http://service.moncompte.legrain.fr/}taCategoriePro" minOccurs="0"/&gt;
 *         &lt;element name="taTNiveau" type="{http://service.moncompte.legrain.fr/}taTNiveau" minOccurs="0"/&gt;
 *         &lt;element name="quandCree" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quandModif" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quiCree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quiModif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "taDossier", propOrder = {
    "id",
    "code",
    "description",
    "actif",
    "modeLocatif",
    "nbUtilisateur",
    "nbPosteInstalle",
    "accesWs",
    "codePartenaire",
    "tauxReduction",
    "taClient",
    "listeAutorisation",
    "listePrixPerso",
    "listePrixParUtilisateurPerso",
    "taCategoriePro",
    "taTNiveau",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "versionObj"
})
public class TaDossier implements Serializable {

    protected Integer id;
    protected String code;
    protected String description;
    protected boolean actif;
    protected Boolean modeLocatif;
    protected Integer nbUtilisateur;
    protected Integer nbPosteInstalle;
    protected Boolean accesWs;
    protected String codePartenaire;
    protected BigDecimal tauxReduction;
    @XmlInverseReference(mappedBy = "listeDossier")
    @XmlElement
    protected TaClient taClient;
    protected List<TaAutorisation> listeAutorisation;
    protected List<TaPrixPerso> listePrixPerso;
    @XmlElement(nillable = true)
    protected List<TaPrixParUtilisateurPerso> listePrixParUtilisateurPerso;
    protected TaCategoriePro taCategoriePro;
    protected TaTNiveau taTNiveau;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
    protected Integer versionObj;

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
     * Obtient la valeur de la propriété actif.
     * 
     */
    public boolean isActif() {
        return actif;
    }

    /**
     * Définit la valeur de la propriété actif.
     * 
     */
    public void setActif(boolean value) {
        this.actif = value;
    }

    /**
     * Obtient la valeur de la propriété modeLocatif.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isModeLocatif() {
        return modeLocatif;
    }

    /**
     * Définit la valeur de la propriété modeLocatif.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setModeLocatif(Boolean value) {
        this.modeLocatif = value;
    }

    /**
     * Obtient la valeur de la propriété nbUtilisateur.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNbUtilisateur() {
        return nbUtilisateur;
    }

    /**
     * Définit la valeur de la propriété nbUtilisateur.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNbUtilisateur(Integer value) {
        this.nbUtilisateur = value;
    }

    /**
     * Obtient la valeur de la propriété nbPosteInstalle.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNbPosteInstalle() {
        return nbPosteInstalle;
    }

    /**
     * Définit la valeur de la propriété nbPosteInstalle.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNbPosteInstalle(Integer value) {
        this.nbPosteInstalle = value;
    }

    /**
     * Obtient la valeur de la propriété accesWs.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAccesWs() {
        return accesWs;
    }

    /**
     * Définit la valeur de la propriété accesWs.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAccesWs(Boolean value) {
        this.accesWs = value;
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
     * Obtient la valeur de la propriété tauxReduction.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTauxReduction() {
        return tauxReduction;
    }

    /**
     * Définit la valeur de la propriété tauxReduction.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTauxReduction(BigDecimal value) {
        this.tauxReduction = value;
    }

    /**
     * Obtient la valeur de la propriété taClient.
     * 
     * @return
     *     possible object is
     *     {@link TaClient }
     *     
     */
    public TaClient getTaClient() {
        return taClient;
    }

    /**
     * Définit la valeur de la propriété taClient.
     * 
     * @param value
     *     allowed object is
     *     {@link TaClient }
     *     
     */
    public void setTaClient(TaClient value) {
        this.taClient = value;
    }

    /**
     * Gets the value of the listeAutorisation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listeAutorisation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListeAutorisation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaAutorisation }
     * 
     * 
     */
    public List<TaAutorisation> getListeAutorisation() {
        if (listeAutorisation == null) {
            listeAutorisation = new ArrayList<TaAutorisation>();
        }
        return this.listeAutorisation;
    }

    /**
     * Gets the value of the listePrixPerso property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listePrixPerso property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListePrixPerso().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaPrixPerso }
     * 
     * 
     */
    public List<TaPrixPerso> getListePrixPerso() {
        if (listePrixPerso == null) {
            listePrixPerso = new ArrayList<TaPrixPerso>();
        }
        return this.listePrixPerso;
    }

    /**
     * Gets the value of the listePrixParUtilisateurPerso property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listePrixParUtilisateurPerso property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListePrixParUtilisateurPerso().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaPrixParUtilisateurPerso }
     * 
     * 
     */
    public List<TaPrixParUtilisateurPerso> getListePrixParUtilisateurPerso() {
        if (listePrixParUtilisateurPerso == null) {
            listePrixParUtilisateurPerso = new ArrayList<TaPrixParUtilisateurPerso>();
        }
        return this.listePrixParUtilisateurPerso;
    }

    /**
     * Obtient la valeur de la propriété taCategoriePro.
     * 
     * @return
     *     possible object is
     *     {@link TaCategoriePro }
     *     
     */
    public TaCategoriePro getTaCategoriePro() {
        return taCategoriePro;
    }

    /**
     * Définit la valeur de la propriété taCategoriePro.
     * 
     * @param value
     *     allowed object is
     *     {@link TaCategoriePro }
     *     
     */
    public void setTaCategoriePro(TaCategoriePro value) {
        this.taCategoriePro = value;
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
