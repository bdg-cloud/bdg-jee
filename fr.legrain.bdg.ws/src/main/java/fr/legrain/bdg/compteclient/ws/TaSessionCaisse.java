
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
 * <p>Classe Java pour taSessionCaisse complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taSessionCaisse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="automatique" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="codeSessionCaisse" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dateDebutSession" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dateFinSession" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dateSession" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="idSessionCaisse" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ipAcces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="libelle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="lignes" type="{http://service.bdg.legrain.fr/}taLSessionCaisse" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="montantFondDeCaisseCloture" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="montantFondDeCaisseOuverture" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="montantHtCumulAnnuel" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="montantHtCumulExercice" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="montantHtCumulMensuel" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="montantHtSession" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="montantRemiseSession" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="montantTtcCumulAnnuel" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="montantTtcCumulExercice" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="montantTtcCumulMensuel" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="montantTtcSession" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="montantTvaCumulAnnuel" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="montantTvaCumulExercice" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="montantTvaCumulMensuel" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="montantTvaSession" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="numeroCaisse" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quandCree" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quandModif" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quiCree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quiModif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="taFondDeCaisseCloture" type="{http://service.bdg.legrain.fr/}taFondDeCaisse" minOccurs="0"/&gt;
 *         &lt;element name="taFondDeCaisseOuverture" type="{http://service.bdg.legrain.fr/}taFondDeCaisse" minOccurs="0"/&gt;
 *         &lt;element name="taUtilisateur" type="{http://service.bdg.legrain.fr/}taUtilisateur" minOccurs="0"/&gt;
 *         &lt;element name="verrouillageTicketZ" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
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
@XmlType(name = "taSessionCaisse", propOrder = {
    "automatique",
    "codeSessionCaisse",
    "dateDebutSession",
    "dateFinSession",
    "dateSession",
    "idSessionCaisse",
    "ipAcces",
    "libelle",
    "lignes",
    "montantFondDeCaisseCloture",
    "montantFondDeCaisseOuverture",
    "montantHtCumulAnnuel",
    "montantHtCumulExercice",
    "montantHtCumulMensuel",
    "montantHtSession",
    "montantRemiseSession",
    "montantTtcCumulAnnuel",
    "montantTtcCumulExercice",
    "montantTtcCumulMensuel",
    "montantTtcSession",
    "montantTvaCumulAnnuel",
    "montantTvaCumulExercice",
    "montantTvaCumulMensuel",
    "montantTvaSession",
    "numeroCaisse",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "taFondDeCaisseCloture",
    "taFondDeCaisseOuverture",
    "taUtilisateur",
    "verrouillageTicketZ",
    "version",
    "versionObj"
})
public class TaSessionCaisse {

    protected Boolean automatique;
    protected String codeSessionCaisse;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateDebutSession;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateFinSession;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateSession;
    protected int idSessionCaisse;
    protected String ipAcces;
    protected String libelle;
    @XmlElement(nillable = true)
    protected List<TaLSessionCaisse> lignes;
    protected BigDecimal montantFondDeCaisseCloture;
    protected BigDecimal montantFondDeCaisseOuverture;
    protected BigDecimal montantHtCumulAnnuel;
    protected BigDecimal montantHtCumulExercice;
    protected BigDecimal montantHtCumulMensuel;
    protected BigDecimal montantHtSession;
    protected BigDecimal montantRemiseSession;
    protected BigDecimal montantTtcCumulAnnuel;
    protected BigDecimal montantTtcCumulExercice;
    protected BigDecimal montantTtcCumulMensuel;
    protected BigDecimal montantTtcSession;
    protected BigDecimal montantTvaCumulAnnuel;
    protected BigDecimal montantTvaCumulExercice;
    protected BigDecimal montantTvaCumulMensuel;
    protected BigDecimal montantTvaSession;
    protected String numeroCaisse;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
    protected TaFondDeCaisse taFondDeCaisseCloture;
    protected TaFondDeCaisse taFondDeCaisseOuverture;
    protected TaUtilisateur taUtilisateur;
    protected Boolean verrouillageTicketZ;
    protected String version;
    protected Integer versionObj;

    /**
     * Obtient la valeur de la propriété automatique.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAutomatique() {
        return automatique;
    }

    /**
     * Définit la valeur de la propriété automatique.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAutomatique(Boolean value) {
        this.automatique = value;
    }

    /**
     * Obtient la valeur de la propriété codeSessionCaisse.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeSessionCaisse() {
        return codeSessionCaisse;
    }

    /**
     * Définit la valeur de la propriété codeSessionCaisse.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeSessionCaisse(String value) {
        this.codeSessionCaisse = value;
    }

    /**
     * Obtient la valeur de la propriété dateDebutSession.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateDebutSession() {
        return dateDebutSession;
    }

    /**
     * Définit la valeur de la propriété dateDebutSession.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateDebutSession(XMLGregorianCalendar value) {
        this.dateDebutSession = value;
    }

    /**
     * Obtient la valeur de la propriété dateFinSession.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateFinSession() {
        return dateFinSession;
    }

    /**
     * Définit la valeur de la propriété dateFinSession.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateFinSession(XMLGregorianCalendar value) {
        this.dateFinSession = value;
    }

    /**
     * Obtient la valeur de la propriété dateSession.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateSession() {
        return dateSession;
    }

    /**
     * Définit la valeur de la propriété dateSession.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateSession(XMLGregorianCalendar value) {
        this.dateSession = value;
    }

    /**
     * Obtient la valeur de la propriété idSessionCaisse.
     * 
     */
    public int getIdSessionCaisse() {
        return idSessionCaisse;
    }

    /**
     * Définit la valeur de la propriété idSessionCaisse.
     * 
     */
    public void setIdSessionCaisse(int value) {
        this.idSessionCaisse = value;
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
     * {@link TaLSessionCaisse }
     * 
     * 
     */
    public List<TaLSessionCaisse> getLignes() {
        if (lignes == null) {
            lignes = new ArrayList<TaLSessionCaisse>();
        }
        return this.lignes;
    }

    /**
     * Obtient la valeur de la propriété montantFondDeCaisseCloture.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMontantFondDeCaisseCloture() {
        return montantFondDeCaisseCloture;
    }

    /**
     * Définit la valeur de la propriété montantFondDeCaisseCloture.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMontantFondDeCaisseCloture(BigDecimal value) {
        this.montantFondDeCaisseCloture = value;
    }

    /**
     * Obtient la valeur de la propriété montantFondDeCaisseOuverture.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMontantFondDeCaisseOuverture() {
        return montantFondDeCaisseOuverture;
    }

    /**
     * Définit la valeur de la propriété montantFondDeCaisseOuverture.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMontantFondDeCaisseOuverture(BigDecimal value) {
        this.montantFondDeCaisseOuverture = value;
    }

    /**
     * Obtient la valeur de la propriété montantHtCumulAnnuel.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMontantHtCumulAnnuel() {
        return montantHtCumulAnnuel;
    }

    /**
     * Définit la valeur de la propriété montantHtCumulAnnuel.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMontantHtCumulAnnuel(BigDecimal value) {
        this.montantHtCumulAnnuel = value;
    }

    /**
     * Obtient la valeur de la propriété montantHtCumulExercice.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMontantHtCumulExercice() {
        return montantHtCumulExercice;
    }

    /**
     * Définit la valeur de la propriété montantHtCumulExercice.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMontantHtCumulExercice(BigDecimal value) {
        this.montantHtCumulExercice = value;
    }

    /**
     * Obtient la valeur de la propriété montantHtCumulMensuel.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMontantHtCumulMensuel() {
        return montantHtCumulMensuel;
    }

    /**
     * Définit la valeur de la propriété montantHtCumulMensuel.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMontantHtCumulMensuel(BigDecimal value) {
        this.montantHtCumulMensuel = value;
    }

    /**
     * Obtient la valeur de la propriété montantHtSession.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMontantHtSession() {
        return montantHtSession;
    }

    /**
     * Définit la valeur de la propriété montantHtSession.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMontantHtSession(BigDecimal value) {
        this.montantHtSession = value;
    }

    /**
     * Obtient la valeur de la propriété montantRemiseSession.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMontantRemiseSession() {
        return montantRemiseSession;
    }

    /**
     * Définit la valeur de la propriété montantRemiseSession.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMontantRemiseSession(BigDecimal value) {
        this.montantRemiseSession = value;
    }

    /**
     * Obtient la valeur de la propriété montantTtcCumulAnnuel.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMontantTtcCumulAnnuel() {
        return montantTtcCumulAnnuel;
    }

    /**
     * Définit la valeur de la propriété montantTtcCumulAnnuel.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMontantTtcCumulAnnuel(BigDecimal value) {
        this.montantTtcCumulAnnuel = value;
    }

    /**
     * Obtient la valeur de la propriété montantTtcCumulExercice.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMontantTtcCumulExercice() {
        return montantTtcCumulExercice;
    }

    /**
     * Définit la valeur de la propriété montantTtcCumulExercice.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMontantTtcCumulExercice(BigDecimal value) {
        this.montantTtcCumulExercice = value;
    }

    /**
     * Obtient la valeur de la propriété montantTtcCumulMensuel.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMontantTtcCumulMensuel() {
        return montantTtcCumulMensuel;
    }

    /**
     * Définit la valeur de la propriété montantTtcCumulMensuel.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMontantTtcCumulMensuel(BigDecimal value) {
        this.montantTtcCumulMensuel = value;
    }

    /**
     * Obtient la valeur de la propriété montantTtcSession.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMontantTtcSession() {
        return montantTtcSession;
    }

    /**
     * Définit la valeur de la propriété montantTtcSession.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMontantTtcSession(BigDecimal value) {
        this.montantTtcSession = value;
    }

    /**
     * Obtient la valeur de la propriété montantTvaCumulAnnuel.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMontantTvaCumulAnnuel() {
        return montantTvaCumulAnnuel;
    }

    /**
     * Définit la valeur de la propriété montantTvaCumulAnnuel.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMontantTvaCumulAnnuel(BigDecimal value) {
        this.montantTvaCumulAnnuel = value;
    }

    /**
     * Obtient la valeur de la propriété montantTvaCumulExercice.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMontantTvaCumulExercice() {
        return montantTvaCumulExercice;
    }

    /**
     * Définit la valeur de la propriété montantTvaCumulExercice.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMontantTvaCumulExercice(BigDecimal value) {
        this.montantTvaCumulExercice = value;
    }

    /**
     * Obtient la valeur de la propriété montantTvaCumulMensuel.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMontantTvaCumulMensuel() {
        return montantTvaCumulMensuel;
    }

    /**
     * Définit la valeur de la propriété montantTvaCumulMensuel.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMontantTvaCumulMensuel(BigDecimal value) {
        this.montantTvaCumulMensuel = value;
    }

    /**
     * Obtient la valeur de la propriété montantTvaSession.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMontantTvaSession() {
        return montantTvaSession;
    }

    /**
     * Définit la valeur de la propriété montantTvaSession.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMontantTvaSession(BigDecimal value) {
        this.montantTvaSession = value;
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
     * Obtient la valeur de la propriété taFondDeCaisseCloture.
     * 
     * @return
     *     possible object is
     *     {@link TaFondDeCaisse }
     *     
     */
    public TaFondDeCaisse getTaFondDeCaisseCloture() {
        return taFondDeCaisseCloture;
    }

    /**
     * Définit la valeur de la propriété taFondDeCaisseCloture.
     * 
     * @param value
     *     allowed object is
     *     {@link TaFondDeCaisse }
     *     
     */
    public void setTaFondDeCaisseCloture(TaFondDeCaisse value) {
        this.taFondDeCaisseCloture = value;
    }

    /**
     * Obtient la valeur de la propriété taFondDeCaisseOuverture.
     * 
     * @return
     *     possible object is
     *     {@link TaFondDeCaisse }
     *     
     */
    public TaFondDeCaisse getTaFondDeCaisseOuverture() {
        return taFondDeCaisseOuverture;
    }

    /**
     * Définit la valeur de la propriété taFondDeCaisseOuverture.
     * 
     * @param value
     *     allowed object is
     *     {@link TaFondDeCaisse }
     *     
     */
    public void setTaFondDeCaisseOuverture(TaFondDeCaisse value) {
        this.taFondDeCaisseOuverture = value;
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
     * Obtient la valeur de la propriété verrouillageTicketZ.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isVerrouillageTicketZ() {
        return verrouillageTicketZ;
    }

    /**
     * Définit la valeur de la propriété verrouillageTicketZ.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setVerrouillageTicketZ(Boolean value) {
        this.verrouillageTicketZ = value;
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
