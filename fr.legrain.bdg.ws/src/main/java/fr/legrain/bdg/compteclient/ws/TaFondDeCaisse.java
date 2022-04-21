
package fr.legrain.bdg.compteclient.ws;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java pour taFondDeCaisse complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taFondDeCaisse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="idFondDeCaisse" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ipAcces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="libelle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="montant" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="numeroCaisse" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quandCree" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quandModif" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quiCree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quiModif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="taSessionCaisse" type="{http://service.bdg.legrain.fr/}taSessionCaisse" minOccurs="0"/&gt;
 *         &lt;element name="taTFondDeCaisse" type="{http://service.bdg.legrain.fr/}taTFondDeCaisse" minOccurs="0"/&gt;
 *         &lt;element name="taUtilisateur" type="{http://service.bdg.legrain.fr/}taUtilisateur" minOccurs="0"/&gt;
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
@XmlType(name = "taFondDeCaisse", propOrder = {
    "date",
    "idFondDeCaisse",
    "ipAcces",
    "libelle",
    "montant",
    "numeroCaisse",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "taSessionCaisse",
    "taTFondDeCaisse",
    "taUtilisateur",
    "version",
    "versionObj"
})
public class TaFondDeCaisse {

    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar date;
    protected int idFondDeCaisse;
    protected String ipAcces;
    protected String libelle;
    protected BigDecimal montant;
    protected String numeroCaisse;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
    protected TaSessionCaisse taSessionCaisse;
    protected TaTFondDeCaisse taTFondDeCaisse;
    protected TaUtilisateur taUtilisateur;
    protected String version;
    protected Integer versionObj;

    /**
     * Obtient la valeur de la propriété date.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDate() {
        return date;
    }

    /**
     * Définit la valeur de la propriété date.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDate(XMLGregorianCalendar value) {
        this.date = value;
    }

    /**
     * Obtient la valeur de la propriété idFondDeCaisse.
     * 
     */
    public int getIdFondDeCaisse() {
        return idFondDeCaisse;
    }

    /**
     * Définit la valeur de la propriété idFondDeCaisse.
     * 
     */
    public void setIdFondDeCaisse(int value) {
        this.idFondDeCaisse = value;
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
     * Obtient la valeur de la propriété montant.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMontant() {
        return montant;
    }

    /**
     * Définit la valeur de la propriété montant.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMontant(BigDecimal value) {
        this.montant = value;
    }

    /**
     * Obtient la valeur de la propriété numeroCaisse.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroCaisse() {
        return numeroCaisse;
    }

    /**
     * Définit la valeur de la propriété numeroCaisse.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroCaisse(String value) {
        this.numeroCaisse = value;
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
     * Obtient la valeur de la propriété taSessionCaisse.
     * 
     * @return
     *     possible object is
     *     {@link TaSessionCaisse }
     *     
     */
    public TaSessionCaisse getTaSessionCaisse() {
        return taSessionCaisse;
    }

    /**
     * Définit la valeur de la propriété taSessionCaisse.
     * 
     * @param value
     *     allowed object is
     *     {@link TaSessionCaisse }
     *     
     */
    public void setTaSessionCaisse(TaSessionCaisse value) {
        this.taSessionCaisse = value;
    }

    /**
     * Obtient la valeur de la propriété taTFondDeCaisse.
     * 
     * @return
     *     possible object is
     *     {@link TaTFondDeCaisse }
     *     
     */
    public TaTFondDeCaisse getTaTFondDeCaisse() {
        return taTFondDeCaisse;
    }

    /**
     * Définit la valeur de la propriété taTFondDeCaisse.
     * 
     * @param value
     *     allowed object is
     *     {@link TaTFondDeCaisse }
     *     
     */
    public void setTaTFondDeCaisse(TaTFondDeCaisse value) {
        this.taTFondDeCaisse = value;
    }

    /**
     * Obtient la valeur de la propriété taUtilisateur.
     * 
     * @return
     *     possible object is
     *     {@link TaUtilisateur }
     *     
     */
    public TaUtilisateur getTaUtilisateur() {
        return taUtilisateur;
    }

    /**
     * Définit la valeur de la propriété taUtilisateur.
     * 
     * @param value
     *     allowed object is
     *     {@link TaUtilisateur }
     *     
     */
    public void setTaUtilisateur(TaUtilisateur value) {
        this.taUtilisateur = value;
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
