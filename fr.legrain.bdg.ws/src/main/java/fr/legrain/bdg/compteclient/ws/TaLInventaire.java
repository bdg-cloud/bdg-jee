
package fr.legrain.bdg.compteclient.ws;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java pour taLInventaire complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taLInventaire"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="emplacement" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idLInventaire" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="insertionAuto" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="ipAcces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="libelleInventaire" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="modifie" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="numLot" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="qte2LInventaire" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="qteReelle" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="qteTheorique" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="quandCree" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quandModif" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quiCree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quiModif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="taEntrepot" type="{http://service.bdg.legrain.fr/}taEntrepot" minOccurs="0"/&gt;
 *         &lt;element name="taInventaire" type="{http://service.bdg.legrain.fr/}taInventaire" minOccurs="0"/&gt;
 *         &lt;element name="taLot" type="{http://service.bdg.legrain.fr/}taLot" minOccurs="0"/&gt;
 *         &lt;element name="taMouvementStock" type="{http://service.bdg.legrain.fr/}taMouvementStock" minOccurs="0"/&gt;
 *         &lt;element name="un1Inventaire" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="un2Inventaire" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "taLInventaire", propOrder = {
    "emplacement",
    "idLInventaire",
    "insertionAuto",
    "ipAcces",
    "libelleInventaire",
    "modifie",
    "numLot",
    "qte2LInventaire",
    "qteReelle",
    "qteTheorique",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "taEntrepot",
    "taInventaire",
    "taLot",
    "taMouvementStock",
    "un1Inventaire",
    "un2Inventaire",
    "version",
    "versionObj"
})
public class TaLInventaire {

    protected String emplacement;
    protected int idLInventaire;
    protected Boolean insertionAuto;
    protected String ipAcces;
    protected String libelleInventaire;
    protected boolean modifie;
    protected String numLot;
    protected BigDecimal qte2LInventaire;
    protected BigDecimal qteReelle;
    protected BigDecimal qteTheorique;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
    protected TaEntrepot taEntrepot;
    protected TaInventaire taInventaire;
    protected TaLot taLot;
    protected TaMouvementStock taMouvementStock;
    protected String un1Inventaire;
    protected String un2Inventaire;
    protected String version;
    protected Integer versionObj;

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
     * Obtient la valeur de la propriété idLInventaire.
     * 
     */
    public int getIdLInventaire() {
        return idLInventaire;
    }

    /**
     * Définit la valeur de la propriété idLInventaire.
     * 
     */
    public void setIdLInventaire(int value) {
        this.idLInventaire = value;
    }

    /**
     * Obtient la valeur de la propriété insertionAuto.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isInsertionAuto() {
        return insertionAuto;
    }

    /**
     * Définit la valeur de la propriété insertionAuto.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setInsertionAuto(Boolean value) {
        this.insertionAuto = value;
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
     * Obtient la valeur de la propriété libelleInventaire.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLibelleInventaire() {
        return libelleInventaire;
    }

    /**
     * Définit la valeur de la propriété libelleInventaire.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLibelleInventaire(String value) {
        this.libelleInventaire = value;
    }

    /**
     * Obtient la valeur de la propriété modifie.
     * 
     */
    public boolean isModifie() {
        return modifie;
    }

    /**
     * Définit la valeur de la propriété modifie.
     * 
     */
    public void setModifie(boolean value) {
        this.modifie = value;
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
     * Obtient la valeur de la propriété qte2LInventaire.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getQte2LInventaire() {
        return qte2LInventaire;
    }

    /**
     * Définit la valeur de la propriété qte2LInventaire.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setQte2LInventaire(BigDecimal value) {
        this.qte2LInventaire = value;
    }

    /**
     * Obtient la valeur de la propriété qteReelle.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getQteReelle() {
        return qteReelle;
    }

    /**
     * Définit la valeur de la propriété qteReelle.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setQteReelle(BigDecimal value) {
        this.qteReelle = value;
    }

    /**
     * Obtient la valeur de la propriété qteTheorique.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getQteTheorique() {
        return qteTheorique;
    }

    /**
     * Définit la valeur de la propriété qteTheorique.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setQteTheorique(BigDecimal value) {
        this.qteTheorique = value;
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
     * Obtient la valeur de la propriété taInventaire.
     * 
     * @return
     *     possible object is
     *     {@link TaInventaire }
     *     
     */
    public TaInventaire getTaInventaire() {
        return taInventaire;
    }

    /**
     * Définit la valeur de la propriété taInventaire.
     * 
     * @param value
     *     allowed object is
     *     {@link TaInventaire }
     *     
     */
    public void setTaInventaire(TaInventaire value) {
        this.taInventaire = value;
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
     * Obtient la valeur de la propriété taMouvementStock.
     * 
     * @return
     *     possible object is
     *     {@link TaMouvementStock }
     *     
     */
    public TaMouvementStock getTaMouvementStock() {
        return taMouvementStock;
    }

    /**
     * Définit la valeur de la propriété taMouvementStock.
     * 
     * @param value
     *     allowed object is
     *     {@link TaMouvementStock }
     *     
     */
    public void setTaMouvementStock(TaMouvementStock value) {
        this.taMouvementStock = value;
    }

    /**
     * Obtient la valeur de la propriété un1Inventaire.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUn1Inventaire() {
        return un1Inventaire;
    }

    /**
     * Définit la valeur de la propriété un1Inventaire.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUn1Inventaire(String value) {
        this.un1Inventaire = value;
    }

    /**
     * Obtient la valeur de la propriété un2Inventaire.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUn2Inventaire() {
        return un2Inventaire;
    }

    /**
     * Définit la valeur de la propriété un2Inventaire.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUn2Inventaire(String value) {
        this.un2Inventaire = value;
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
