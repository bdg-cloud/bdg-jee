
package fr.legrain.bdg.compteclient.ws;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java pour taLEcheance complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taLEcheance"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="accepte" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="dateFinCalcul" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="debAbonnement" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="debutPeriode" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="finAbonnement" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="finPeriode" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="idLEcheance" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ipAcces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="libLDocument" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="mtHtLApresRemiseGlobaleDocument" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="mtHtLDocument" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="mtTtcLApresRemiseGlobaleDocument" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="mtTtcLDocument" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="pourcIndice" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="pourcPartenaire" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="prixULDocument" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="qteLDocument" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="quandCree" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quandModif" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quiCree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quiModif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="remHtLDocument" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="remTxLDocument" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="taAbonnement" type="{http://service.bdg.legrain.fr/}taAbonnement" minOccurs="0"/&gt;
 *         &lt;element name="taArticle" type="{http://service.bdg.legrain.fr/}taArticle" minOccurs="0"/&gt;
 *         &lt;element name="tauxTvaLDocument" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="u1LDocument" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "taLEcheance", propOrder = {
    "accepte",
    "dateFinCalcul",
    "debAbonnement",
    "debutPeriode",
    "finAbonnement",
    "finPeriode",
    "idLEcheance",
    "ipAcces",
    "libLDocument",
    "mtHtLApresRemiseGlobaleDocument",
    "mtHtLDocument",
    "mtTtcLApresRemiseGlobaleDocument",
    "mtTtcLDocument",
    "pourcIndice",
    "pourcPartenaire",
    "prixULDocument",
    "qteLDocument",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "remHtLDocument",
    "remTxLDocument",
    "taAbonnement",
    "taArticle",
    "tauxTvaLDocument",
    "u1LDocument",
    "version",
    "versionObj"
})
public class TaLEcheance {

    protected Boolean accepte;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateFinCalcul;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar debAbonnement;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar debutPeriode;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar finAbonnement;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar finPeriode;
    protected int idLEcheance;
    protected String ipAcces;
    protected String libLDocument;
    protected BigDecimal mtHtLApresRemiseGlobaleDocument;
    protected BigDecimal mtHtLDocument;
    protected BigDecimal mtTtcLApresRemiseGlobaleDocument;
    protected BigDecimal mtTtcLDocument;
    protected BigDecimal pourcIndice;
    protected BigDecimal pourcPartenaire;
    protected BigDecimal prixULDocument;
    protected BigDecimal qteLDocument;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
    protected BigDecimal remHtLDocument;
    protected BigDecimal remTxLDocument;
    protected TaAbonnement taAbonnement;
    protected TaArticle taArticle;
    protected BigDecimal tauxTvaLDocument;
    protected String u1LDocument;
    protected String version;
    protected Integer versionObj;

    /**
     * Obtient la valeur de la propriété accepte.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAccepte() {
        return accepte;
    }

    /**
     * Définit la valeur de la propriété accepte.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAccepte(Boolean value) {
        this.accepte = value;
    }

    /**
     * Obtient la valeur de la propriété dateFinCalcul.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateFinCalcul() {
        return dateFinCalcul;
    }

    /**
     * Définit la valeur de la propriété dateFinCalcul.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateFinCalcul(XMLGregorianCalendar value) {
        this.dateFinCalcul = value;
    }

    /**
     * Obtient la valeur de la propriété debAbonnement.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDebAbonnement() {
        return debAbonnement;
    }

    /**
     * Définit la valeur de la propriété debAbonnement.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDebAbonnement(XMLGregorianCalendar value) {
        this.debAbonnement = value;
    }

    /**
     * Obtient la valeur de la propriété debutPeriode.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDebutPeriode() {
        return debutPeriode;
    }

    /**
     * Définit la valeur de la propriété debutPeriode.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDebutPeriode(XMLGregorianCalendar value) {
        this.debutPeriode = value;
    }

    /**
     * Obtient la valeur de la propriété finAbonnement.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFinAbonnement() {
        return finAbonnement;
    }

    /**
     * Définit la valeur de la propriété finAbonnement.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFinAbonnement(XMLGregorianCalendar value) {
        this.finAbonnement = value;
    }

    /**
     * Obtient la valeur de la propriété finPeriode.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFinPeriode() {
        return finPeriode;
    }

    /**
     * Définit la valeur de la propriété finPeriode.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFinPeriode(XMLGregorianCalendar value) {
        this.finPeriode = value;
    }

    /**
     * Obtient la valeur de la propriété idLEcheance.
     * 
     */
    public int getIdLEcheance() {
        return idLEcheance;
    }

    /**
     * Définit la valeur de la propriété idLEcheance.
     * 
     */
    public void setIdLEcheance(int value) {
        this.idLEcheance = value;
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
     * Obtient la valeur de la propriété pourcIndice.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPourcIndice() {
        return pourcIndice;
    }

    /**
     * Définit la valeur de la propriété pourcIndice.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPourcIndice(BigDecimal value) {
        this.pourcIndice = value;
    }

    /**
     * Obtient la valeur de la propriété pourcPartenaire.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPourcPartenaire() {
        return pourcPartenaire;
    }

    /**
     * Définit la valeur de la propriété pourcPartenaire.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPourcPartenaire(BigDecimal value) {
        this.pourcPartenaire = value;
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
     * Obtient la valeur de la propriété taAbonnement.
     * 
     * @return
     *     possible object is
     *     {@link TaAbonnement }
     *     
     */
    public TaAbonnement getTaAbonnement() {
        return taAbonnement;
    }

    /**
     * Définit la valeur de la propriété taAbonnement.
     * 
     * @param value
     *     allowed object is
     *     {@link TaAbonnement }
     *     
     */
    public void setTaAbonnement(TaAbonnement value) {
        this.taAbonnement = value;
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
