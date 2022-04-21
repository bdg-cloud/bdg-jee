
package fr.legrain.bdg.compteclient.ws;

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
 * <p>Classe Java pour taLot complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taLot"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="dateLot" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dluo" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="idLot" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ipAcces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="libelle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="lotCompletDefinitivementInvalide" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="lotConforme" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="nbDecimal" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="numLot" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="presenceDeNonConformite" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="quandCree" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quandModif" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quiCree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quiModif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="rapport" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="sens" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="taArticle" type="{http://service.bdg.legrain.fr/}taArticle" minOccurs="0"/&gt;
 *         &lt;element name="taCodeBarre" type="{http://service.bdg.legrain.fr/}taCodeBarre" minOccurs="0"/&gt;
 *         &lt;element name="taLabelArticles" type="{http://service.bdg.legrain.fr/}taLabelArticle" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="taStatusConformite" type="{http://service.bdg.legrain.fr/}taStatusConformite" minOccurs="0"/&gt;
 *         &lt;element name="taTiersPrestationService" type="{http://service.bdg.legrain.fr/}taTiers" minOccurs="0"/&gt;
 *         &lt;element name="termine" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="unite1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="unite2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="versionObj" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="virtuel" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="virtuelUnique" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "taLot", propOrder = {
    "dateLot",
    "dluo",
    "idLot",
    "ipAcces",
    "libelle",
    "lotCompletDefinitivementInvalide",
    "lotConforme",
    "nbDecimal",
    "numLot",
    "presenceDeNonConformite",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "rapport",
    "sens",
    "taArticle",
    "taCodeBarre",
    "taLabelArticles",
    "taStatusConformite",
    "taTiersPrestationService",
    "termine",
    "unite1",
    "unite2",
    "version",
    "versionObj",
    "virtuel",
    "virtuelUnique"
})
public class TaLot {

    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateLot;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dluo;
    protected int idLot;
    protected String ipAcces;
    protected String libelle;
    protected Boolean lotCompletDefinitivementInvalide;
    protected Boolean lotConforme;
    protected Integer nbDecimal;
    protected String numLot;
    protected Boolean presenceDeNonConformite;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
    protected BigDecimal rapport;
    protected Integer sens;
    protected TaArticle taArticle;
    protected TaCodeBarre taCodeBarre;
    @XmlElement(nillable = true)
    protected List<TaLabelArticle> taLabelArticles;
    protected TaStatusConformite taStatusConformite;
    protected TaTiers taTiersPrestationService;
    protected Boolean termine;
    protected String unite1;
    protected String unite2;
    protected String version;
    protected Integer versionObj;
    protected Boolean virtuel;
    protected Boolean virtuelUnique;

    /**
     * Obtient la valeur de la propriété dateLot.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateLot() {
        return dateLot;
    }

    /**
     * Définit la valeur de la propriété dateLot.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateLot(XMLGregorianCalendar value) {
        this.dateLot = value;
    }

    /**
     * Obtient la valeur de la propriété dluo.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDluo() {
        return dluo;
    }

    /**
     * Définit la valeur de la propriété dluo.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDluo(XMLGregorianCalendar value) {
        this.dluo = value;
    }

    /**
     * Obtient la valeur de la propriété idLot.
     * 
     */
    public int getIdLot() {
        return idLot;
    }

    /**
     * Définit la valeur de la propriété idLot.
     * 
     */
    public void setIdLot(int value) {
        this.idLot = value;
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
     * Obtient la valeur de la propriété lotCompletDefinitivementInvalide.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isLotCompletDefinitivementInvalide() {
        return lotCompletDefinitivementInvalide;
    }

    /**
     * Définit la valeur de la propriété lotCompletDefinitivementInvalide.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setLotCompletDefinitivementInvalide(Boolean value) {
        this.lotCompletDefinitivementInvalide = value;
    }

    /**
     * Obtient la valeur de la propriété lotConforme.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isLotConforme() {
        return lotConforme;
    }

    /**
     * Définit la valeur de la propriété lotConforme.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setLotConforme(Boolean value) {
        this.lotConforme = value;
    }

    /**
     * Obtient la valeur de la propriété nbDecimal.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNbDecimal() {
        return nbDecimal;
    }

    /**
     * Définit la valeur de la propriété nbDecimal.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNbDecimal(Integer value) {
        this.nbDecimal = value;
    }

    /**
     * Obtient la valeur de la propriété numLot.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumLot() {
        return numLot;
    }

    /**
     * Définit la valeur de la propriété numLot.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumLot(String value) {
        this.numLot = value;
    }

    /**
     * Obtient la valeur de la propriété presenceDeNonConformite.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPresenceDeNonConformite() {
        return presenceDeNonConformite;
    }

    /**
     * Définit la valeur de la propriété presenceDeNonConformite.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPresenceDeNonConformite(Boolean value) {
        this.presenceDeNonConformite = value;
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
     * Obtient la valeur de la propriété rapport.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRapport() {
        return rapport;
    }

    /**
     * Définit la valeur de la propriété rapport.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRapport(BigDecimal value) {
        this.rapport = value;
    }

    /**
     * Obtient la valeur de la propriété sens.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSens() {
        return sens;
    }

    /**
     * Définit la valeur de la propriété sens.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSens(Integer value) {
        this.sens = value;
    }

    /**
     * Obtient la valeur de la propriété taArticle.
     * 
     * @return
     *     possible object is
     *     {@link TaArticle }
     *     
     */
    public TaArticle getTaArticle() {
        return taArticle;
    }

    /**
     * Définit la valeur de la propriété taArticle.
     * 
     * @param value
     *     allowed object is
     *     {@link TaArticle }
     *     
     */
    public void setTaArticle(TaArticle value) {
        this.taArticle = value;
    }

    /**
     * Obtient la valeur de la propriété taCodeBarre.
     * 
     * @return
     *     possible object is
     *     {@link TaCodeBarre }
     *     
     */
    public TaCodeBarre getTaCodeBarre() {
        return taCodeBarre;
    }

    /**
     * Définit la valeur de la propriété taCodeBarre.
     * 
     * @param value
     *     allowed object is
     *     {@link TaCodeBarre }
     *     
     */
    public void setTaCodeBarre(TaCodeBarre value) {
        this.taCodeBarre = value;
    }

    /**
     * Gets the value of the taLabelArticles property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taLabelArticles property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaLabelArticles().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaLabelArticle }
     * 
     * 
     */
    public List<TaLabelArticle> getTaLabelArticles() {
        if (taLabelArticles == null) {
            taLabelArticles = new ArrayList<TaLabelArticle>();
        }
        return this.taLabelArticles;
    }

    /**
     * Obtient la valeur de la propriété taStatusConformite.
     * 
     * @return
     *     possible object is
     *     {@link TaStatusConformite }
     *     
     */
    public TaStatusConformite getTaStatusConformite() {
        return taStatusConformite;
    }

    /**
     * Définit la valeur de la propriété taStatusConformite.
     * 
     * @param value
     *     allowed object is
     *     {@link TaStatusConformite }
     *     
     */
    public void setTaStatusConformite(TaStatusConformite value) {
        this.taStatusConformite = value;
    }

    /**
     * Obtient la valeur de la propriété taTiersPrestationService.
     * 
     * @return
     *     possible object is
     *     {@link TaTiers }
     *     
     */
    public TaTiers getTaTiersPrestationService() {
        return taTiersPrestationService;
    }

    /**
     * Définit la valeur de la propriété taTiersPrestationService.
     * 
     * @param value
     *     allowed object is
     *     {@link TaTiers }
     *     
     */
    public void setTaTiersPrestationService(TaTiers value) {
        this.taTiersPrestationService = value;
    }

    /**
     * Obtient la valeur de la propriété termine.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTermine() {
        return termine;
    }

    /**
     * Définit la valeur de la propriété termine.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTermine(Boolean value) {
        this.termine = value;
    }

    /**
     * Obtient la valeur de la propriété unite1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnite1() {
        return unite1;
    }

    /**
     * Définit la valeur de la propriété unite1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnite1(String value) {
        this.unite1 = value;
    }

    /**
     * Obtient la valeur de la propriété unite2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnite2() {
        return unite2;
    }

    /**
     * Définit la valeur de la propriété unite2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnite2(String value) {
        this.unite2 = value;
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
     * Obtient la valeur de la propriété virtuel.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isVirtuel() {
        return virtuel;
    }

    /**
     * Définit la valeur de la propriété virtuel.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setVirtuel(Boolean value) {
        this.virtuel = value;
    }

    /**
     * Obtient la valeur de la propriété virtuelUnique.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isVirtuelUnique() {
        return virtuelUnique;
    }

    /**
     * Définit la valeur de la propriété virtuelUnique.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setVirtuelUnique(Boolean value) {
        this.virtuelUnique = value;
    }

}
