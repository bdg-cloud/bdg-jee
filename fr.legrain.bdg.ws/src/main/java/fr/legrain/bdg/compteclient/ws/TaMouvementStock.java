
package fr.legrain.bdg.compteclient.ws;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java pour taMouvementStock complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taMouvementStock"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="dateStock" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="emplacement" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="emplacementDest" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idMouvementStock" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ipAcces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="lettrageDeplacement" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="libelleStock" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="qte1Stock" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="qte2Stock" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="quandCree" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quandModif" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quiCree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quiModif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="taEntrepot" type="{http://service.bdg.legrain.fr/}taEntrepot" minOccurs="0"/&gt;
 *         &lt;element name="taEntrepotDest" type="{http://service.bdg.legrain.fr/}taEntrepot" minOccurs="0"/&gt;
 *         &lt;element name="taGrMouvStock" type="{http://service.bdg.legrain.fr/}taGrMouvStock" minOccurs="0"/&gt;
 *         &lt;element name="taLot" type="{http://service.bdg.legrain.fr/}taLot" minOccurs="0"/&gt;
 *         &lt;element name="un1Stock" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="un2Stock" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="utilise" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
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
@XmlType(name = "taMouvementStock", propOrder = {
    "dateStock",
    "description",
    "emplacement",
    "emplacementDest",
    "idMouvementStock",
    "ipAcces",
    "lettrageDeplacement",
    "libelleStock",
    "qte1Stock",
    "qte2Stock",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "taEntrepot",
    "taEntrepotDest",
    "taGrMouvStock",
    "taLot",
    "un1Stock",
    "un2Stock",
    "utilise",
    "version",
    "versionObj"
})
public class TaMouvementStock {

    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateStock;
    protected String description;
    protected String emplacement;
    protected String emplacementDest;
    protected int idMouvementStock;
    protected String ipAcces;
    protected Long lettrageDeplacement;
    protected String libelleStock;
    protected BigDecimal qte1Stock;
    protected BigDecimal qte2Stock;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
    protected TaEntrepot taEntrepot;
    protected TaEntrepot taEntrepotDest;
    protected TaGrMouvStock taGrMouvStock;
    protected TaLot taLot;
    protected String un1Stock;
    protected String un2Stock;
    protected Boolean utilise;
    protected String version;
    protected Integer versionObj;

    /**
     * Obtient la valeur de la propriété dateStock.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateStock() {
        return dateStock;
    }

    /**
     * Définit la valeur de la propriété dateStock.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateStock(XMLGregorianCalendar value) {
        this.dateStock = value;
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
     * Obtient la valeur de la propriété emplacement.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmplacement() {
        return emplacement;
    }

    /**
     * Définit la valeur de la propriété emplacement.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmplacement(String value) {
        this.emplacement = value;
    }

    /**
     * Obtient la valeur de la propriété emplacementDest.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmplacementDest() {
        return emplacementDest;
    }

    /**
     * Définit la valeur de la propriété emplacementDest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmplacementDest(String value) {
        this.emplacementDest = value;
    }

    /**
     * Obtient la valeur de la propriété idMouvementStock.
     * 
     */
    public int getIdMouvementStock() {
        return idMouvementStock;
    }

    /**
     * Définit la valeur de la propriété idMouvementStock.
     * 
     */
    public void setIdMouvementStock(int value) {
        this.idMouvementStock = value;
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
     * Obtient la valeur de la propriété lettrageDeplacement.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getLettrageDeplacement() {
        return lettrageDeplacement;
    }

    /**
     * Définit la valeur de la propriété lettrageDeplacement.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setLettrageDeplacement(Long value) {
        this.lettrageDeplacement = value;
    }

    /**
     * Obtient la valeur de la propriété libelleStock.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLibelleStock() {
        return libelleStock;
    }

    /**
     * Définit la valeur de la propriété libelleStock.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLibelleStock(String value) {
        this.libelleStock = value;
    }

    /**
     * Obtient la valeur de la propriété qte1Stock.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getQte1Stock() {
        return qte1Stock;
    }

    /**
     * Définit la valeur de la propriété qte1Stock.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setQte1Stock(BigDecimal value) {
        this.qte1Stock = value;
    }

    /**
     * Obtient la valeur de la propriété qte2Stock.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getQte2Stock() {
        return qte2Stock;
    }

    /**
     * Définit la valeur de la propriété qte2Stock.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setQte2Stock(BigDecimal value) {
        this.qte2Stock = value;
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
     * Obtient la valeur de la propriété taEntrepot.
     * 
     * @return
     *     possible object is
     *     {@link TaEntrepot }
     *     
     */
    public TaEntrepot getTaEntrepot() {
        return taEntrepot;
    }

    /**
     * Définit la valeur de la propriété taEntrepot.
     * 
     * @param value
     *     allowed object is
     *     {@link TaEntrepot }
     *     
     */
    public void setTaEntrepot(TaEntrepot value) {
        this.taEntrepot = value;
    }

    /**
     * Obtient la valeur de la propriété taEntrepotDest.
     * 
     * @return
     *     possible object is
     *     {@link TaEntrepot }
     *     
     */
    public TaEntrepot getTaEntrepotDest() {
        return taEntrepotDest;
    }

    /**
     * Définit la valeur de la propriété taEntrepotDest.
     * 
     * @param value
     *     allowed object is
     *     {@link TaEntrepot }
     *     
     */
    public void setTaEntrepotDest(TaEntrepot value) {
        this.taEntrepotDest = value;
    }

    /**
     * Obtient la valeur de la propriété taGrMouvStock.
     * 
     * @return
     *     possible object is
     *     {@link TaGrMouvStock }
     *     
     */
    public TaGrMouvStock getTaGrMouvStock() {
        return taGrMouvStock;
    }

    /**
     * Définit la valeur de la propriété taGrMouvStock.
     * 
     * @param value
     *     allowed object is
     *     {@link TaGrMouvStock }
     *     
     */
    public void setTaGrMouvStock(TaGrMouvStock value) {
        this.taGrMouvStock = value;
    }

    /**
     * Obtient la valeur de la propriété taLot.
     * 
     * @return
     *     possible object is
     *     {@link TaLot }
     *     
     */
    public TaLot getTaLot() {
        return taLot;
    }

    /**
     * Définit la valeur de la propriété taLot.
     * 
     * @param value
     *     allowed object is
     *     {@link TaLot }
     *     
     */
    public void setTaLot(TaLot value) {
        this.taLot = value;
    }

    /**
     * Obtient la valeur de la propriété un1Stock.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUn1Stock() {
        return un1Stock;
    }

    /**
     * Définit la valeur de la propriété un1Stock.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUn1Stock(String value) {
        this.un1Stock = value;
    }

    /**
     * Obtient la valeur de la propriété un2Stock.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUn2Stock() {
        return un2Stock;
    }

    /**
     * Définit la valeur de la propriété un2Stock.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUn2Stock(String value) {
        this.un2Stock = value;
    }

    /**
     * Obtient la valeur de la propriété utilise.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isUtilise() {
        return utilise;
    }

    /**
     * Définit la valeur de la propriété utilise.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setUtilise(Boolean value) {
        this.utilise = value;
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
