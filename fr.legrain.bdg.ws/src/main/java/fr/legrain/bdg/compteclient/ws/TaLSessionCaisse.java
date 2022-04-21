
package fr.legrain.bdg.compteclient.ws;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java pour taLSessionCaisse complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taLSessionCaisse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idExt" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="idLSessionCaisse" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ipAcces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="libelle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="montantHtCumulAnnuel" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="montantHtCumulExercice" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="montantHtCumulMensuel" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="montantHtSession" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="montantTtcCumulAnnuel" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="montantTtcCumulExercice" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="montantTtcCumulMensuel" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="montantTtcSession" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="montantTvaCumulAnnuel" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="montantTvaCumulExercice" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="montantTvaCumulMensuel" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="montantTvaSession" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="quandCree" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quandModif" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="quiCree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quiModif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="taSessionCaisse" type="{http://service.bdg.legrain.fr/}taSessionCaisse" minOccurs="0"/&gt;
 *         &lt;element name="taTLSessionCaisse" type="{http://service.bdg.legrain.fr/}taTLSessionCaisse" minOccurs="0"/&gt;
 *         &lt;element name="taux" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
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
@XmlType(name = "taLSessionCaisse", propOrder = {
    "code",
    "idExt",
    "idLSessionCaisse",
    "ipAcces",
    "libelle",
    "montantHtCumulAnnuel",
    "montantHtCumulExercice",
    "montantHtCumulMensuel",
    "montantHtSession",
    "montantTtcCumulAnnuel",
    "montantTtcCumulExercice",
    "montantTtcCumulMensuel",
    "montantTtcSession",
    "montantTvaCumulAnnuel",
    "montantTvaCumulExercice",
    "montantTvaCumulMensuel",
    "montantTvaSession",
    "quandCree",
    "quandModif",
    "quiCree",
    "quiModif",
    "taSessionCaisse",
    "taTLSessionCaisse",
    "taux",
    "version",
    "versionObj"
})
public class TaLSessionCaisse {

    protected String code;
    protected Integer idExt;
    protected int idLSessionCaisse;
    protected String ipAcces;
    protected String libelle;
    protected BigDecimal montantHtCumulAnnuel;
    protected BigDecimal montantHtCumulExercice;
    protected BigDecimal montantHtCumulMensuel;
    protected BigDecimal montantHtSession;
    protected BigDecimal montantTtcCumulAnnuel;
    protected BigDecimal montantTtcCumulExercice;
    protected BigDecimal montantTtcCumulMensuel;
    protected BigDecimal montantTtcSession;
    protected BigDecimal montantTvaCumulAnnuel;
    protected BigDecimal montantTvaCumulExercice;
    protected BigDecimal montantTvaCumulMensuel;
    protected BigDecimal montantTvaSession;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandCree;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar quandModif;
    protected String quiCree;
    protected String quiModif;
    protected TaSessionCaisse taSessionCaisse;
    protected TaTLSessionCaisse taTLSessionCaisse;
    protected BigDecimal taux;
    protected String version;
    protected Integer versionObj;

    /**
     * Obtient la valeur de la propriété code.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCode() {
        return code;
    }

    /**
     * Définit la valeur de la propriété code.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCode(String value) {
        this.code = value;
    }

    /**
     * Obtient la valeur de la propriété idExt.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIdExt() {
        return idExt;
    }

    /**
     * Définit la valeur de la propriété idExt.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIdExt(Integer value) {
        this.idExt = value;
    }

    /**
     * Obtient la valeur de la propriété idLSessionCaisse.
     * 
     */
    public int getIdLSessionCaisse() {
        return idLSessionCaisse;
    }

    /**
     * Définit la valeur de la propriété idLSessionCaisse.
     * 
     */
    public void setIdLSessionCaisse(int value) {
        this.idLSessionCaisse = value;
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
     * Obtient la valeur de la propriété taTLSessionCaisse.
     * 
     * @return
     *     possible object is
     *     {@link TaTLSessionCaisse }
     *     
     */
    public TaTLSessionCaisse getTaTLSessionCaisse() {
        return taTLSessionCaisse;
    }

    /**
     * Définit la valeur de la propriété taTLSessionCaisse.
     * 
     * @param value
     *     allowed object is
     *     {@link TaTLSessionCaisse }
     *     
     */
    public void setTaTLSessionCaisse(TaTLSessionCaisse value) {
        this.taTLSessionCaisse = value;
    }

    /**
     * Obtient la valeur de la propriété taux.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTaux() {
        return taux;
    }

    /**
     * Définit la valeur de la propriété taux.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTaux(BigDecimal value) {
        this.taux = value;
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
