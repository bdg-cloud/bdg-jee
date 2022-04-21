
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
 * <p>Classe Java pour taPanier complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taPanier"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="accesWS" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="codePromo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codeRevendeur" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="commentaireClient" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="commentaireLegrain" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dateCreation" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="datePaiement" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="idPanier" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="ipAcces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="libelle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="lignes" type="{http://service.moncompte.legrain.fr/}taLignePanier" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="nbMois" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="nbMoisRecurPaiement" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="nbPosteInstalle" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="nbUtilisateur" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="niveau" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="paye" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="quandCree" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quandModif" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quiCree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quiModif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="refPaiement" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="taCategoriePro" type="{http://service.moncompte.legrain.fr/}taCategoriePro" minOccurs="0"/&gt;
 *         &lt;element name="taCgv" type="{http://service.moncompte.legrain.fr/}taCgv" minOccurs="0"/&gt;
 *         &lt;element name="taClient" type="{http://service.moncompte.legrain.fr/}taClient" minOccurs="0"/&gt;
 *         &lt;element name="taDossier" type="{http://service.moncompte.legrain.fr/}taDossier" minOccurs="0"/&gt;
 *         &lt;element name="taTypePaiement" type="{http://service.moncompte.legrain.fr/}taTypePaiement" minOccurs="0"/&gt;
 *         &lt;element name="tauxReduction" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="totalHT" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="totalTTC" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="totalTVA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="valideParClient" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
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
@XmlType(name = "taPanier", propOrder = {
    "accesWS",
    "codePromo",
    "codeRevendeur",
    "commentaireClient",
    "commentaireLegrain",
    "dateCreation",
    "datePaiement",
    "idPanier",
    "ipAcces",
    "libelle",
    "lignes",
    "nbMois",
    "nbMoisRecurPaiement",
    "nbPosteInstalle",
    "nbUtilisateur",
    "niveau",
    "paye",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "refPaiement",
    "taCategoriePro",
    "taCgv",
    "taClient",
    "taDossier",
    "taTypePaiement",
    "tauxReduction",
    "totalHT",
    "totalTTC",
    "totalTVA",
    "valideParClient",
    "versionObj"
})
public class TaPanier implements Serializable {

    protected Boolean accesWS;
    protected String codePromo;
    protected String codeRevendeur;
    protected String commentaireClient;
    protected String commentaireLegrain;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateCreation;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar datePaiement;
    protected Integer idPanier;
    protected String ipAcces;
    protected String libelle;
    @XmlElement(nillable = true)
    protected List<TaLignePanier> lignes;
    protected Integer nbMois;
    protected Integer nbMoisRecurPaiement;
    protected Integer nbPosteInstalle;
    protected Integer nbUtilisateur;
    protected String niveau;
    protected Boolean paye;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
    protected String refPaiement;
    protected TaCategoriePro taCategoriePro;
    protected TaCgv taCgv;
    protected TaClient taClient;
    protected TaDossier taDossier;
    protected TaTypePaiement taTypePaiement;
    protected BigDecimal tauxReduction;
    protected BigDecimal totalHT;
    protected BigDecimal totalTTC;
    protected BigDecimal totalTVA;
    protected boolean valideParClient;
    protected Integer versionObj;

    /**
     * Obtient la valeur de la propriété accesWS.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAccesWS() {
        return accesWS;
    }

    /**
     * Définit la valeur de la propriété accesWS.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAccesWS(Boolean value) {
        this.accesWS = value;
    }

    /**
     * Obtient la valeur de la propriété codePromo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodePromo() {
        return codePromo;
    }

    /**
     * Définit la valeur de la propriété codePromo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodePromo(String value) {
        this.codePromo = value;
    }

    /**
     * Obtient la valeur de la propriété codeRevendeur.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeRevendeur() {
        return codeRevendeur;
    }

    /**
     * Définit la valeur de la propriété codeRevendeur.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeRevendeur(String value) {
        this.codeRevendeur = value;
    }

    /**
     * Obtient la valeur de la propriété commentaireClient.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCommentaireClient() {
        return commentaireClient;
    }

    /**
     * Définit la valeur de la propriété commentaireClient.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCommentaireClient(String value) {
        this.commentaireClient = value;
    }

    /**
     * Obtient la valeur de la propriété commentaireLegrain.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCommentaireLegrain() {
        return commentaireLegrain;
    }

    /**
     * Définit la valeur de la propriété commentaireLegrain.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCommentaireLegrain(String value) {
        this.commentaireLegrain = value;
    }

    /**
     * Obtient la valeur de la propriété dateCreation.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateCreation() {
        return dateCreation;
    }

    /**
     * Définit la valeur de la propriété dateCreation.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateCreation(XMLGregorianCalendar value) {
        this.dateCreation = value;
    }

    /**
     * Obtient la valeur de la propriété datePaiement.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDatePaiement() {
        return datePaiement;
    }

    /**
     * Définit la valeur de la propriété datePaiement.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDatePaiement(XMLGregorianCalendar value) {
        this.datePaiement = value;
    }

    /**
     * Obtient la valeur de la propriété idPanier.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIdPanier() {
        return idPanier;
    }

    /**
     * Définit la valeur de la propriété idPanier.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIdPanier(Integer value) {
        this.idPanier = value;
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
     * Gets the value of the lignes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lignes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLignes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaLignePanier }
     * 
     * 
     */
    public List<TaLignePanier> getLignes() {
        if (lignes == null) {
            lignes = new ArrayList<TaLignePanier>();
        }
        return this.lignes;
    }

    /**
     * Obtient la valeur de la propriété nbMois.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNbMois() {
        return nbMois;
    }

    /**
     * Définit la valeur de la propriété nbMois.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNbMois(Integer value) {
        this.nbMois = value;
    }

    /**
     * Obtient la valeur de la propriété nbMoisRecurPaiement.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNbMoisRecurPaiement() {
        return nbMoisRecurPaiement;
    }

    /**
     * Définit la valeur de la propriété nbMoisRecurPaiement.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNbMoisRecurPaiement(Integer value) {
        this.nbMoisRecurPaiement = value;
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
     * Obtient la valeur de la propriété niveau.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNiveau() {
        return niveau;
    }

    /**
     * Définit la valeur de la propriété niveau.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNiveau(String value) {
        this.niveau = value;
    }

    /**
     * Obtient la valeur de la propriété paye.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPaye() {
        return paye;
    }

    /**
     * Définit la valeur de la propriété paye.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPaye(Boolean value) {
        this.paye = value;
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
     * Obtient la valeur de la propriété refPaiement.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefPaiement() {
        return refPaiement;
    }

    /**
     * Définit la valeur de la propriété refPaiement.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefPaiement(String value) {
        this.refPaiement = value;
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
     * Obtient la valeur de la propriété taCgv.
     * 
     * @return
     *     possible object is
     *     {@link TaCgv }
     *     
     */
    public TaCgv getTaCgv() {
        return taCgv;
    }

    /**
     * Définit la valeur de la propriété taCgv.
     * 
     * @param value
     *     allowed object is
     *     {@link TaCgv }
     *     
     */
    public void setTaCgv(TaCgv value) {
        this.taCgv = value;
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
     * Obtient la valeur de la propriété taDossier.
     * 
     * @return
     *     possible object is
     *     {@link TaDossier }
     *     
     */
    public TaDossier getTaDossier() {
        return taDossier;
    }

    /**
     * Définit la valeur de la propriété taDossier.
     * 
     * @param value
     *     allowed object is
     *     {@link TaDossier }
     *     
     */
    public void setTaDossier(TaDossier value) {
        this.taDossier = value;
    }

    /**
     * Obtient la valeur de la propriété taTypePaiement.
     * 
     * @return
     *     possible object is
     *     {@link TaTypePaiement }
     *     
     */
    public TaTypePaiement getTaTypePaiement() {
        return taTypePaiement;
    }

    /**
     * Définit la valeur de la propriété taTypePaiement.
     * 
     * @param value
     *     allowed object is
     *     {@link TaTypePaiement }
     *     
     */
    public void setTaTypePaiement(TaTypePaiement value) {
        this.taTypePaiement = value;
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
     * Obtient la valeur de la propriété totalHT.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTotalHT() {
        return totalHT;
    }

    /**
     * Définit la valeur de la propriété totalHT.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTotalHT(BigDecimal value) {
        this.totalHT = value;
    }

    /**
     * Obtient la valeur de la propriété totalTTC.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTotalTTC() {
        return totalTTC;
    }

    /**
     * Définit la valeur de la propriété totalTTC.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTotalTTC(BigDecimal value) {
        this.totalTTC = value;
    }

    /**
     * Obtient la valeur de la propriété totalTVA.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTotalTVA() {
        return totalTVA;
    }

    /**
     * Définit la valeur de la propriété totalTVA.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTotalTVA(BigDecimal value) {
        this.totalTVA = value;
    }

    /**
     * Obtient la valeur de la propriété valideParClient.
     * 
     */
    public boolean isValideParClient() {
        return valideParClient;
    }

    /**
     * Définit la valeur de la propriété valideParClient.
     * 
     */
    public void setValideParClient(boolean value) {
        this.valideParClient = value;
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
