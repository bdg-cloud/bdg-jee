
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
 * <p>Classe Java pour taRAcompte complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taRAcompte"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="affectation" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="etat" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="etatDeSuppression" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ipAcces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quandCree" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quandModif" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quiCree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quiModif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="taAcompte" type="{http://service.bdg.legrain.fr/}taAcompte" minOccurs="0"/&gt;
 *         &lt;element name="taApporteur" type="{http://service.bdg.legrain.fr/}taApporteur" minOccurs="0"/&gt;
 *         &lt;element name="taAvoir" type="{http://service.bdg.legrain.fr/}taAvoir" minOccurs="0"/&gt;
 *         &lt;element name="taBoncde" type="{http://service.bdg.legrain.fr/}taBoncde" minOccurs="0"/&gt;
 *         &lt;element name="taBonliv" type="{http://service.bdg.legrain.fr/}taBonliv" minOccurs="0"/&gt;
 *         &lt;element name="taDevis" type="{http://service.bdg.legrain.fr/}taDevis" minOccurs="0"/&gt;
 *         &lt;element name="taFacture" type="{http://service.bdg.legrain.fr/}taFacture" minOccurs="0"/&gt;
 *         &lt;element name="taPrelevement" type="{http://service.bdg.legrain.fr/}taPrelevement" minOccurs="0"/&gt;
 *         &lt;element name="taProforma" type="{http://service.bdg.legrain.fr/}taProforma" minOccurs="0"/&gt;
 *         &lt;element name="taTicketDeCaisse" type="{http://service.bdg.legrain.fr/}taTicketDeCaisse" minOccurs="0"/&gt;
 *         &lt;element name="typeDocument" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "taRAcompte", propOrder = {
    "affectation",
    "etat",
    "etatDeSuppression",
    "id",
    "ipAcces",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "taAcompte",
    "taApporteur",
    "taAvoir",
    "taBoncde",
    "taBonliv",
    "taDevis",
    "taFacture",
    "taPrelevement",
    "taProforma",
    "taTicketDeCaisse",
    "typeDocument",
    "version",
    "versionObj"
})
public class TaRAcompte {

    protected BigDecimal affectation;
    protected Integer etat;
    protected boolean etatDeSuppression;
    protected int id;
    protected String ipAcces;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
    protected TaAcompte taAcompte;
    protected TaApporteur taApporteur;
    protected TaAvoir taAvoir;
    protected TaBoncde taBoncde;
    protected TaBonliv taBonliv;
    protected TaDevis taDevis;
    @XmlInverseReference(mappedBy = "taRAcomptes")
    @XmlElement
    protected TaFacture taFacture;
    protected TaPrelevement taPrelevement;
    protected TaProforma taProforma;
    protected TaTicketDeCaisse taTicketDeCaisse;
    protected String typeDocument;
    protected String version;
    protected Integer versionObj;

    /**
     * Obtient la valeur de la propriété affectation.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAffectation() {
        return affectation;
    }

    /**
     * Définit la valeur de la propriété affectation.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAffectation(BigDecimal value) {
        this.affectation = value;
    }

    /**
     * Obtient la valeur de la propriété etat.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getEtat() {
        return etat;
    }

    /**
     * Définit la valeur de la propriété etat.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setEtat(Integer value) {
        this.etat = value;
    }

    /**
     * Obtient la valeur de la propriété etatDeSuppression.
     * 
     */
    public boolean isEtatDeSuppression() {
        return etatDeSuppression;
    }

    /**
     * Définit la valeur de la propriété etatDeSuppression.
     * 
     */
    public void setEtatDeSuppression(boolean value) {
        this.etatDeSuppression = value;
    }

    /**
     * Obtient la valeur de la propriété id.
     * 
     */
    public int getId() {
        return id;
    }

    /**
     * Définit la valeur de la propriété id.
     * 
     */
    public void setId(int value) {
        this.id = value;
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
     * Obtient la valeur de la propriété taAcompte.
     * 
     * @return
     *     possible object is
     *     {@link TaAcompte }
     *     
     */
    public TaAcompte getTaAcompte() {
        return taAcompte;
    }

    /**
     * Définit la valeur de la propriété taAcompte.
     * 
     * @param value
     *     allowed object is
     *     {@link TaAcompte }
     *     
     */
    public void setTaAcompte(TaAcompte value) {
        this.taAcompte = value;
    }

    /**
     * Obtient la valeur de la propriété taApporteur.
     * 
     * @return
     *     possible object is
     *     {@link TaApporteur }
     *     
     */
    public TaApporteur getTaApporteur() {
        return taApporteur;
    }

    /**
     * Définit la valeur de la propriété taApporteur.
     * 
     * @param value
     *     allowed object is
     *     {@link TaApporteur }
     *     
     */
    public void setTaApporteur(TaApporteur value) {
        this.taApporteur = value;
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
     * Obtient la valeur de la propriété taBoncde.
     * 
     * @return
     *     possible object is
     *     {@link TaBoncde }
     *     
     */
    public TaBoncde getTaBoncde() {
        return taBoncde;
    }

    /**
     * Définit la valeur de la propriété taBoncde.
     * 
     * @param value
     *     allowed object is
     *     {@link TaBoncde }
     *     
     */
    public void setTaBoncde(TaBoncde value) {
        this.taBoncde = value;
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
     * Obtient la valeur de la propriété taDevis.
     * 
     * @return
     *     possible object is
     *     {@link TaDevis }
     *     
     */
    public TaDevis getTaDevis() {
        return taDevis;
    }

    /**
     * Définit la valeur de la propriété taDevis.
     * 
     * @param value
     *     allowed object is
     *     {@link TaDevis }
     *     
     */
    public void setTaDevis(TaDevis value) {
        this.taDevis = value;
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
     * Obtient la valeur de la propriété taPrelevement.
     * 
     * @return
     *     possible object is
     *     {@link TaPrelevement }
     *     
     */
    public TaPrelevement getTaPrelevement() {
        return taPrelevement;
    }

    /**
     * Définit la valeur de la propriété taPrelevement.
     * 
     * @param value
     *     allowed object is
     *     {@link TaPrelevement }
     *     
     */
    public void setTaPrelevement(TaPrelevement value) {
        this.taPrelevement = value;
    }

    /**
     * Obtient la valeur de la propriété taProforma.
     * 
     * @return
     *     possible object is
     *     {@link TaProforma }
     *     
     */
    public TaProforma getTaProforma() {
        return taProforma;
    }

    /**
     * Définit la valeur de la propriété taProforma.
     * 
     * @param value
     *     allowed object is
     *     {@link TaProforma }
     *     
     */
    public void setTaProforma(TaProforma value) {
        this.taProforma = value;
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
     * Obtient la valeur de la propriété typeDocument.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypeDocument() {
        return typeDocument;
    }

    /**
     * Définit la valeur de la propriété typeDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypeDocument(String value) {
        this.typeDocument = value;
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
