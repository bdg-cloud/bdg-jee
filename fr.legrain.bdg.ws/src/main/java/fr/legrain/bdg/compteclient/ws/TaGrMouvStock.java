
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
 * <p>Classe Java pour taGrMouvStock complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taGrMouvStock"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codeGrMouvStock" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="commentaire" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dateGrMouvStock" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="idGrMouvStock" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ipAcces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="libelleGrMouvStock" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="manuel" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="quandCree" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quandModif" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quiCree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quiModif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="taAvoir" type="{http://service.bdg.legrain.fr/}taAvoir" minOccurs="0"/&gt;
 *         &lt;element name="taBonReception" type="{http://service.bdg.legrain.fr/}taBonReception" minOccurs="0"/&gt;
 *         &lt;element name="taBonliv" type="{http://service.bdg.legrain.fr/}taBonliv" minOccurs="0"/&gt;
 *         &lt;element name="taEtat" type="{http://service.bdg.legrain.fr/}taEtat" minOccurs="0"/&gt;
 *         &lt;element name="taFabrication" type="{http://service.bdg.legrain.fr/}taFabrication" minOccurs="0"/&gt;
 *         &lt;element name="taFacture" type="{http://service.bdg.legrain.fr/}taFacture" minOccurs="0"/&gt;
 *         &lt;element name="taInventaire" type="{http://service.bdg.legrain.fr/}taInventaire" minOccurs="0"/&gt;
 *         &lt;element name="taMouvementStockes" type="{http://service.bdg.legrain.fr/}taMouvementStock" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="taTicketDeCaisse" type="{http://service.bdg.legrain.fr/}taTicketDeCaisse" minOccurs="0"/&gt;
 *         &lt;element name="taTypeMouvStock" type="{http://service.bdg.legrain.fr/}taTypeMouvement" minOccurs="0"/&gt;
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
@XmlType(name = "taGrMouvStock", propOrder = {
    "codeGrMouvStock",
    "commentaire",
    "dateGrMouvStock",
    "idGrMouvStock",
    "ipAcces",
    "libelleGrMouvStock",
    "manuel",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "taAvoir",
    "taBonReception",
    "taBonliv",
    "taEtat",
    "taFabrication",
    "taFacture",
    "taInventaire",
    "taMouvementStockes",
    "taTicketDeCaisse",
    "taTypeMouvStock",
    "version",
    "versionObj"
})
public class TaGrMouvStock {

    protected String codeGrMouvStock;
    protected String commentaire;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateGrMouvStock;
    protected int idGrMouvStock;
    protected String ipAcces;
    protected String libelleGrMouvStock;
    protected Boolean manuel;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
    protected TaAvoir taAvoir;
    protected TaBonReception taBonReception;
    protected TaBonliv taBonliv;
    protected TaEtat taEtat;
    protected TaFabrication taFabrication;
    protected TaFacture taFacture;
    protected TaInventaire taInventaire;
    @XmlElement(nillable = true)
    protected List<TaMouvementStock> taMouvementStockes;
    protected TaTicketDeCaisse taTicketDeCaisse;
    protected TaTypeMouvement taTypeMouvStock;
    protected String version;
    protected Integer versionObj;

    /**
     * Obtient la valeur de la propriété codeGrMouvStock.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeGrMouvStock() {
        return codeGrMouvStock;
    }

    /**
     * Définit la valeur de la propriété codeGrMouvStock.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeGrMouvStock(String value) {
        this.codeGrMouvStock = value;
    }

    /**
     * Obtient la valeur de la propriété commentaire.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCommentaire() {
        return commentaire;
    }

    /**
     * Définit la valeur de la propriété commentaire.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCommentaire(String value) {
        this.commentaire = value;
    }

    /**
     * Obtient la valeur de la propriété dateGrMouvStock.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateGrMouvStock() {
        return dateGrMouvStock;
    }

    /**
     * Définit la valeur de la propriété dateGrMouvStock.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateGrMouvStock(XMLGregorianCalendar value) {
        this.dateGrMouvStock = value;
    }

    /**
     * Obtient la valeur de la propriété idGrMouvStock.
     * 
     */
    public int getIdGrMouvStock() {
        return idGrMouvStock;
    }

    /**
     * Définit la valeur de la propriété idGrMouvStock.
     * 
     */
    public void setIdGrMouvStock(int value) {
        this.idGrMouvStock = value;
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
     * Obtient la valeur de la propriété libelleGrMouvStock.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLibelleGrMouvStock() {
        return libelleGrMouvStock;
    }

    /**
     * Définit la valeur de la propriété libelleGrMouvStock.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLibelleGrMouvStock(String value) {
        this.libelleGrMouvStock = value;
    }

    /**
     * Obtient la valeur de la propriété manuel.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isManuel() {
        return manuel;
    }

    /**
     * Définit la valeur de la propriété manuel.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setManuel(Boolean value) {
        this.manuel = value;
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
     * Obtient la valeur de la propriété taAvoir.
     * 
     * @return
     *     possible object is
     *     {@link TaAvoir }
     *     
     */
    public TaAvoir getTaAvoir() {
        return taAvoir;
    }

    /**
     * Définit la valeur de la propriété taAvoir.
     * 
     * @param value
     *     allowed object is
     *     {@link TaAvoir }
     *     
     */
    public void setTaAvoir(TaAvoir value) {
        this.taAvoir = value;
    }

    /**
     * Obtient la valeur de la propriété taBonReception.
     * 
     * @return
     *     possible object is
     *     {@link TaBonReception }
     *     
     */
    public TaBonReception getTaBonReception() {
        return taBonReception;
    }

    /**
     * Définit la valeur de la propriété taBonReception.
     * 
     * @param value
     *     allowed object is
     *     {@link TaBonReception }
     *     
     */
    public void setTaBonReception(TaBonReception value) {
        this.taBonReception = value;
    }

    /**
     * Obtient la valeur de la propriété taBonliv.
     * 
     * @return
     *     possible object is
     *     {@link TaBonliv }
     *     
     */
    public TaBonliv getTaBonliv() {
        return taBonliv;
    }

    /**
     * Définit la valeur de la propriété taBonliv.
     * 
     * @param value
     *     allowed object is
     *     {@link TaBonliv }
     *     
     */
    public void setTaBonliv(TaBonliv value) {
        this.taBonliv = value;
    }

    /**
     * Obtient la valeur de la propriété taEtat.
     * 
     * @return
     *     possible object is
     *     {@link TaEtat }
     *     
     */
    public TaEtat getTaEtat() {
        return taEtat;
    }

    /**
     * Définit la valeur de la propriété taEtat.
     * 
     * @param value
     *     allowed object is
     *     {@link TaEtat }
     *     
     */
    public void setTaEtat(TaEtat value) {
        this.taEtat = value;
    }

    /**
     * Obtient la valeur de la propriété taFabrication.
     * 
     * @return
     *     possible object is
     *     {@link TaFabrication }
     *     
     */
    public TaFabrication getTaFabrication() {
        return taFabrication;
    }

    /**
     * Définit la valeur de la propriété taFabrication.
     * 
     * @param value
     *     allowed object is
     *     {@link TaFabrication }
     *     
     */
    public void setTaFabrication(TaFabrication value) {
        this.taFabrication = value;
    }

    /**
     * Obtient la valeur de la propriété taFacture.
     * 
     * @return
     *     possible object is
     *     {@link TaFacture }
     *     
     */
    public TaFacture getTaFacture() {
        return taFacture;
    }

    /**
     * Définit la valeur de la propriété taFacture.
     * 
     * @param value
     *     allowed object is
     *     {@link TaFacture }
     *     
     */
    public void setTaFacture(TaFacture value) {
        this.taFacture = value;
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
     * Gets the value of the taMouvementStockes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taMouvementStockes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaMouvementStockes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaMouvementStock }
     * 
     * 
     */
    public List<TaMouvementStock> getTaMouvementStockes() {
        if (taMouvementStockes == null) {
            taMouvementStockes = new ArrayList<TaMouvementStock>();
        }
        return this.taMouvementStockes;
    }

    /**
     * Obtient la valeur de la propriété taTicketDeCaisse.
     * 
     * @return
     *     possible object is
     *     {@link TaTicketDeCaisse }
     *     
     */
    public TaTicketDeCaisse getTaTicketDeCaisse() {
        return taTicketDeCaisse;
    }

    /**
     * Définit la valeur de la propriété taTicketDeCaisse.
     * 
     * @param value
     *     allowed object is
     *     {@link TaTicketDeCaisse }
     *     
     */
    public void setTaTicketDeCaisse(TaTicketDeCaisse value) {
        this.taTicketDeCaisse = value;
    }

    /**
     * Obtient la valeur de la propriété taTypeMouvStock.
     * 
     * @return
     *     possible object is
     *     {@link TaTypeMouvement }
     *     
     */
    public TaTypeMouvement getTaTypeMouvStock() {
        return taTypeMouvStock;
    }

    /**
     * Définit la valeur de la propriété taTypeMouvStock.
     * 
     * @param value
     *     allowed object is
     *     {@link TaTypeMouvement }
     *     
     */
    public void setTaTypeMouvStock(TaTypeMouvement value) {
        this.taTypeMouvStock = value;
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
