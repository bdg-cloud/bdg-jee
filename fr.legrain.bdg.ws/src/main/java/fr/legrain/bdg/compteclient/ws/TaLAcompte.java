
package fr.legrain.bdg.compteclient.ws;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;


/**
 * <p>Classe Java pour taLAcompte complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taLAcompte"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://service.bdg.legrain.fr/}swtLigneDocument"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codeTTvaLDocument" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codeTvaLDocument" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="compteLDocument" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ipAcces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="libLDocument" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="libTvaLDocument" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="mtHtLApresRemiseGlobaleDocument" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="mtHtLDocument" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="mtTtcLApresRemiseGlobaleDocument" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="mtTtcLDocument" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="numLigneLDocument" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="prixULDocument" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="qte2LDocument" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="qteLDocument" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="quandCree" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quandModif" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quiCree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quiModif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="remHtLDocument" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="remTxLDocument" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="taDocument" type="{http://service.bdg.legrain.fr/}taAcompte" minOccurs="0"/&gt;
 *         &lt;element name="taLot" type="{http://service.bdg.legrain.fr/}taLot" minOccurs="0"/&gt;
 *         &lt;element name="taTLigne" type="{http://service.bdg.legrain.fr/}taTLigne" minOccurs="0"/&gt;
 *         &lt;element name="tauxTvaLDocument" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="txRemHtDocument" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="u1LDocument" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="u2LDocument" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "taLAcompte", propOrder = {
    "codeTTvaLDocument",
    "codeTvaLDocument",
    "compteLDocument",
    "ipAcces",
    "libLDocument",
    "libTvaLDocument",
    "mtHtLApresRemiseGlobaleDocument",
    "mtHtLDocument",
    "mtTtcLApresRemiseGlobaleDocument",
    "mtTtcLDocument",
    "numLigneLDocument",
    "prixULDocument",
    "qte2LDocument",
    "qteLDocument",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "remHtLDocument",
    "remTxLDocument",
    "taDocument",
    "taLot",
    "taTLigne",
    "tauxTvaLDocument",
    "txRemHtDocument",
    "u1LDocument",
    "u2LDocument",
    "version"
})
public class TaLAcompte
    extends SwtLigneDocument
{

    protected String codeTTvaLDocument;
    protected String codeTvaLDocument;
    protected String compteLDocument;
    protected String ipAcces;
    protected String libLDocument;
    protected String libTvaLDocument;
    protected BigDecimal mtHtLApresRemiseGlobaleDocument;
    protected BigDecimal mtHtLDocument;
    protected BigDecimal mtTtcLApresRemiseGlobaleDocument;
    protected BigDecimal mtTtcLDocument;
    protected Integer numLigneLDocument;
    protected BigDecimal prixULDocument;
    protected BigDecimal qte2LDocument;
    protected BigDecimal qteLDocument;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
    protected BigDecimal remHtLDocument;
    protected BigDecimal remTxLDocument;
    @XmlInverseReference(mappedBy = "lignes")
    @XmlElement
    protected TaAcompte taDocument;
    protected TaLot taLot;
    protected TaTLigne taTLigne;
    protected BigDecimal tauxTvaLDocument;
    protected BigDecimal txRemHtDocument;
    protected String u1LDocument;
    protected String u2LDocument;
    protected String version;

    /**
     * Obtient la valeur de la propriété codeTTvaLDocument.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeTTvaLDocument() {
        return codeTTvaLDocument;
    }

    /**
     * Définit la valeur de la propriété codeTTvaLDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeTTvaLDocument(String value) {
        this.codeTTvaLDocument = value;
    }

    /**
     * Obtient la valeur de la propriété codeTvaLDocument.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeTvaLDocument() {
        return codeTvaLDocument;
    }

    /**
     * Définit la valeur de la propriété codeTvaLDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeTvaLDocument(String value) {
        this.codeTvaLDocument = value;
    }

    /**
     * Obtient la valeur de la propriété compteLDocument.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompteLDocument() {
        return compteLDocument;
    }

    /**
     * Définit la valeur de la propriété compteLDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompteLDocument(String value) {
        this.compteLDocument = value;
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
     * Obtient la valeur de la propriété libLDocument.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLibLDocument() {
        return libLDocument;
    }

    /**
     * Définit la valeur de la propriété libLDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLibLDocument(String value) {
        this.libLDocument = value;
    }

    /**
     * Obtient la valeur de la propriété libTvaLDocument.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLibTvaLDocument() {
        return libTvaLDocument;
    }

    /**
     * Définit la valeur de la propriété libTvaLDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLibTvaLDocument(String value) {
        this.libTvaLDocument = value;
    }

    /**
     * Obtient la valeur de la propriété mtHtLApresRemiseGlobaleDocument.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMtHtLApresRemiseGlobaleDocument() {
        return mtHtLApresRemiseGlobaleDocument;
    }

    /**
     * Définit la valeur de la propriété mtHtLApresRemiseGlobaleDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMtHtLApresRemiseGlobaleDocument(BigDecimal value) {
        this.mtHtLApresRemiseGlobaleDocument = value;
    }

    /**
     * Obtient la valeur de la propriété mtHtLDocument.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMtHtLDocument() {
        return mtHtLDocument;
    }

    /**
     * Définit la valeur de la propriété mtHtLDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMtHtLDocument(BigDecimal value) {
        this.mtHtLDocument = value;
    }

    /**
     * Obtient la valeur de la propriété mtTtcLApresRemiseGlobaleDocument.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMtTtcLApresRemiseGlobaleDocument() {
        return mtTtcLApresRemiseGlobaleDocument;
    }

    /**
     * Définit la valeur de la propriété mtTtcLApresRemiseGlobaleDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMtTtcLApresRemiseGlobaleDocument(BigDecimal value) {
        this.mtTtcLApresRemiseGlobaleDocument = value;
    }

    /**
     * Obtient la valeur de la propriété mtTtcLDocument.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMtTtcLDocument() {
        return mtTtcLDocument;
    }

    /**
     * Définit la valeur de la propriété mtTtcLDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMtTtcLDocument(BigDecimal value) {
        this.mtTtcLDocument = value;
    }

    /**
     * Obtient la valeur de la propriété numLigneLDocument.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumLigneLDocument() {
        return numLigneLDocument;
    }

    /**
     * Définit la valeur de la propriété numLigneLDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumLigneLDocument(Integer value) {
        this.numLigneLDocument = value;
    }

    /**
     * Obtient la valeur de la propriété prixULDocument.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPrixULDocument() {
        return prixULDocument;
    }

    /**
     * Définit la valeur de la propriété prixULDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPrixULDocument(BigDecimal value) {
        this.prixULDocument = value;
    }

    /**
     * Obtient la valeur de la propriété qte2LDocument.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getQte2LDocument() {
        return qte2LDocument;
    }

    /**
     * Définit la valeur de la propriété qte2LDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setQte2LDocument(BigDecimal value) {
        this.qte2LDocument = value;
    }

    /**
     * Obtient la valeur de la propriété qteLDocument.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getQteLDocument() {
        return qteLDocument;
    }

    /**
     * Définit la valeur de la propriété qteLDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setQteLDocument(BigDecimal value) {
        this.qteLDocument = value;
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
     * Obtient la valeur de la propriété remHtLDocument.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRemHtLDocument() {
        return remHtLDocument;
    }

    /**
     * Définit la valeur de la propriété remHtLDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRemHtLDocument(BigDecimal value) {
        this.remHtLDocument = value;
    }

    /**
     * Obtient la valeur de la propriété remTxLDocument.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRemTxLDocument() {
        return remTxLDocument;
    }

    /**
     * Définit la valeur de la propriété remTxLDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRemTxLDocument(BigDecimal value) {
        this.remTxLDocument = value;
    }

    /**
     * Obtient la valeur de la propriété taDocument.
     * 
     * @return
     *     possible object is
     *     {@link TaAcompte }
     *     
     */
    public TaAcompte getTaDocument() {
        return taDocument;
    }

    /**
     * Définit la valeur de la propriété taDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link TaAcompte }
     *     
     */
    public void setTaDocument(TaAcompte value) {
        this.taDocument = value;
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
     * Obtient la valeur de la propriété taTLigne.
     * 
     * @return
     *     possible object is
     *     {@link TaTLigne }
     *     
     */
    public TaTLigne getTaTLigne() {
        return taTLigne;
    }

    /**
     * Définit la valeur de la propriété taTLigne.
     * 
     * @param value
     *     allowed object is
     *     {@link TaTLigne }
     *     
     */
    public void setTaTLigne(TaTLigne value) {
        this.taTLigne = value;
    }

    /**
     * Obtient la valeur de la propriété tauxTvaLDocument.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTauxTvaLDocument() {
        return tauxTvaLDocument;
    }

    /**
     * Définit la valeur de la propriété tauxTvaLDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTauxTvaLDocument(BigDecimal value) {
        this.tauxTvaLDocument = value;
    }

    /**
     * Obtient la valeur de la propriété txRemHtDocument.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTxRemHtDocument() {
        return txRemHtDocument;
    }

    /**
     * Définit la valeur de la propriété txRemHtDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTxRemHtDocument(BigDecimal value) {
        this.txRemHtDocument = value;
    }

    /**
     * Obtient la valeur de la propriété u1LDocument.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getU1LDocument() {
        return u1LDocument;
    }

    /**
     * Définit la valeur de la propriété u1LDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setU1LDocument(String value) {
        this.u1LDocument = value;
    }

    /**
     * Obtient la valeur de la propriété u2LDocument.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getU2LDocument() {
        return u2LDocument;
    }

    /**
     * Définit la valeur de la propriété u2LDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setU2LDocument(String value) {
        this.u2LDocument = value;
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

}
