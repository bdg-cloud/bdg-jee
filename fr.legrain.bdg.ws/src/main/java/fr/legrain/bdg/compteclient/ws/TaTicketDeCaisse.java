
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
 * <p>Classe Java pour taTicketDeCaisse complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taTicketDeCaisse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://service.bdg.legrain.fr/}swtDocument"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="acomptes" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="avoirs" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="avoirsComplet" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="codeDocument" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="commentaire" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dateDocument" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dateEchDocument" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dateExport" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dateLivDocument" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dateVerrouillage" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="gestionTVA" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="ipAcces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="legrain" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="libelleDocument" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="libellePaiement" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="lignes" type="{http://service.bdg.legrain.fr/}taLTicketDeCaisse" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="lignesTVA" type="{http://service.bdg.legrain.fr/}ligneTva" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="mtHtCalc" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="mtTtcAvantRemiseGlobaleCalc" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="mtTtcCalc" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="mtTvaCalc" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="nbEDocument" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="netAPayer" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="netHtCalc" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="netTtcCalc" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="netTvaCalc" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="regleCompletDocument" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="regleDocument" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="remHtDocument" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="remTtcDocument" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="resteARegler" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="resteAReglerComplet" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="taEtat" type="{http://service.bdg.legrain.fr/}taEtat" minOccurs="0"/&gt;
 *         &lt;element name="taInfosDocument" type="{http://service.bdg.legrain.fr/}taInfosTicketDeCaisse" minOccurs="0"/&gt;
 *         &lt;element name="taMiseADisposition" type="{http://service.bdg.legrain.fr/}taMiseADisposition" minOccurs="0"/&gt;
 *         &lt;element name="taRAvoirs" type="{http://service.bdg.legrain.fr/}taRAvoir" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="taRReglements" type="{http://service.bdg.legrain.fr/}taRReglement" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="taSessionCaisse" type="{http://service.bdg.legrain.fr/}taSessionCaisse" minOccurs="0"/&gt;
 *         &lt;element name="taTPaiement" type="{http://service.bdg.legrain.fr/}taTPaiement" minOccurs="0"/&gt;
 *         &lt;element name="taTiers" type="{http://service.bdg.legrain.fr/}taTiers" minOccurs="0"/&gt;
 *         &lt;element name="taUtilisateurVendeur" type="{http://service.bdg.legrain.fr/}taUtilisateur" minOccurs="0"/&gt;
 *         &lt;element name="ttc" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="txRemHtDocument" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="txRemTtcDocument" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="typeDocument" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "taTicketDeCaisse", propOrder = {
    "acomptes",
    "avoirs",
    "avoirsComplet",
    "codeDocument",
    "commentaire",
    "dateDocument",
    "dateEchDocument",
    "dateExport",
    "dateLivDocument",
    "dateVerrouillage",
    "gestionTVA",
    "ipAcces",
    "legrain",
    "libelleDocument",
    "libellePaiement",
    "lignes",
    "lignesTVA",
    "mtHtCalc",
    "mtTtcAvantRemiseGlobaleCalc",
    "mtTtcCalc",
    "mtTvaCalc",
    "nbEDocument",
    "netAPayer",
    "netHtCalc",
    "netTtcCalc",
    "netTvaCalc",
    "regleCompletDocument",
    "regleDocument",
    "remHtDocument",
    "remTtcDocument",
    "resteARegler",
    "resteAReglerComplet",
    "taEtat",
    "taInfosDocument",
    "taMiseADisposition",
    "taRAvoirs",
    "taRReglements",
    "taSessionCaisse",
    "taTPaiement",
    "taTiers",
    "taUtilisateurVendeur",
    "ttc",
    "txRemHtDocument",
    "txRemTtcDocument",
    "typeDocument",
    "version"
})
public class TaTicketDeCaisse
    extends SwtDocument
{

    protected BigDecimal acomptes;
    protected BigDecimal avoirs;
    protected BigDecimal avoirsComplet;
    protected String codeDocument;
    protected String commentaire;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateDocument;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateEchDocument;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateExport;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateLivDocument;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateVerrouillage;
    protected boolean gestionTVA;
    protected String ipAcces;
    protected boolean legrain;
    protected String libelleDocument;
    protected String libellePaiement;
    @XmlElement(nillable = true)
    protected List<TaLTicketDeCaisse> lignes;
    @XmlElement(nillable = true)
    protected List<LigneTva> lignesTVA;
    protected BigDecimal mtHtCalc;
    protected BigDecimal mtTtcAvantRemiseGlobaleCalc;
    protected BigDecimal mtTtcCalc;
    protected BigDecimal mtTvaCalc;
    protected Integer nbEDocument;
    protected BigDecimal netAPayer;
    protected BigDecimal netHtCalc;
    protected BigDecimal netTtcCalc;
    protected BigDecimal netTvaCalc;
    protected BigDecimal regleCompletDocument;
    protected BigDecimal regleDocument;
    protected BigDecimal remHtDocument;
    protected BigDecimal remTtcDocument;
    protected BigDecimal resteARegler;
    protected BigDecimal resteAReglerComplet;
    protected TaEtat taEtat;
    protected TaInfosTicketDeCaisse taInfosDocument;
    protected TaMiseADisposition taMiseADisposition;
    @XmlElement(nillable = true)
    protected List<TaRAvoir> taRAvoirs;
    @XmlElement(nillable = true)
    protected List<TaRReglement> taRReglements;
    protected TaSessionCaisse taSessionCaisse;
    protected TaTPaiement taTPaiement;
    protected TaTiers taTiers;
    protected TaUtilisateur taUtilisateurVendeur;
    protected Integer ttc;
    protected BigDecimal txRemHtDocument;
    protected BigDecimal txRemTtcDocument;
    protected String typeDocument;
    protected String version;

    /**
     * Obtient la valeur de la propriété acomptes.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAcomptes() {
        return acomptes;
    }

    /**
     * Définit la valeur de la propriété acomptes.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAcomptes(BigDecimal value) {
        this.acomptes = value;
    }

    /**
     * Obtient la valeur de la propriété avoirs.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAvoirs() {
        return avoirs;
    }

    /**
     * Définit la valeur de la propriété avoirs.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAvoirs(BigDecimal value) {
        this.avoirs = value;
    }

    /**
     * Obtient la valeur de la propriété avoirsComplet.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAvoirsComplet() {
        return avoirsComplet;
    }

    /**
     * Définit la valeur de la propriété avoirsComplet.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAvoirsComplet(BigDecimal value) {
        this.avoirsComplet = value;
    }

    /**
     * Obtient la valeur de la propriété codeDocument.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeDocument() {
        return codeDocument;
    }

    /**
     * Définit la valeur de la propriété codeDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeDocument(String value) {
        this.codeDocument = value;
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
     * Obtient la valeur de la propriété dateDocument.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateDocument() {
        return dateDocument;
    }

    /**
     * Définit la valeur de la propriété dateDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateDocument(XMLGregorianCalendar value) {
        this.dateDocument = value;
    }

    /**
     * Obtient la valeur de la propriété dateEchDocument.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateEchDocument() {
        return dateEchDocument;
    }

    /**
     * Définit la valeur de la propriété dateEchDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateEchDocument(XMLGregorianCalendar value) {
        this.dateEchDocument = value;
    }

    /**
     * Obtient la valeur de la propriété dateExport.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateExport() {
        return dateExport;
    }

    /**
     * Définit la valeur de la propriété dateExport.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateExport(XMLGregorianCalendar value) {
        this.dateExport = value;
    }

    /**
     * Obtient la valeur de la propriété dateLivDocument.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateLivDocument() {
        return dateLivDocument;
    }

    /**
     * Définit la valeur de la propriété dateLivDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateLivDocument(XMLGregorianCalendar value) {
        this.dateLivDocument = value;
    }

    /**
     * Obtient la valeur de la propriété dateVerrouillage.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateVerrouillage() {
        return dateVerrouillage;
    }

    /**
     * Définit la valeur de la propriété dateVerrouillage.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateVerrouillage(XMLGregorianCalendar value) {
        this.dateVerrouillage = value;
    }

    /**
     * Obtient la valeur de la propriété gestionTVA.
     * 
     */
    public boolean isGestionTVA() {
        return gestionTVA;
    }

    /**
     * Définit la valeur de la propriété gestionTVA.
     * 
     */
    public void setGestionTVA(boolean value) {
        this.gestionTVA = value;
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
     * Obtient la valeur de la propriété legrain.
     * 
     */
    public boolean isLegrain() {
        return legrain;
    }

    /**
     * Définit la valeur de la propriété legrain.
     * 
     */
    public void setLegrain(boolean value) {
        this.legrain = value;
    }

    /**
     * Obtient la valeur de la propriété libelleDocument.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLibelleDocument() {
        return libelleDocument;
    }

    /**
     * Définit la valeur de la propriété libelleDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLibelleDocument(String value) {
        this.libelleDocument = value;
    }

    /**
     * Obtient la valeur de la propriété libellePaiement.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLibellePaiement() {
        return libellePaiement;
    }

    /**
     * Définit la valeur de la propriété libellePaiement.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLibellePaiement(String value) {
        this.libellePaiement = value;
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
     * {@link TaLTicketDeCaisse }
     * 
     * 
     */
    public List<TaLTicketDeCaisse> getLignes() {
        if (lignes == null) {
            lignes = new ArrayList<TaLTicketDeCaisse>();
        }
        return this.lignes;
    }

    /**
     * Gets the value of the lignesTVA property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lignesTVA property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLignesTVA().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LigneTva }
     * 
     * 
     */
    public List<LigneTva> getLignesTVA() {
        if (lignesTVA == null) {
            lignesTVA = new ArrayList<LigneTva>();
        }
        return this.lignesTVA;
    }

    /**
     * Obtient la valeur de la propriété mtHtCalc.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMtHtCalc() {
        return mtHtCalc;
    }

    /**
     * Définit la valeur de la propriété mtHtCalc.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMtHtCalc(BigDecimal value) {
        this.mtHtCalc = value;
    }

    /**
     * Obtient la valeur de la propriété mtTtcAvantRemiseGlobaleCalc.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMtTtcAvantRemiseGlobaleCalc() {
        return mtTtcAvantRemiseGlobaleCalc;
    }

    /**
     * Définit la valeur de la propriété mtTtcAvantRemiseGlobaleCalc.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMtTtcAvantRemiseGlobaleCalc(BigDecimal value) {
        this.mtTtcAvantRemiseGlobaleCalc = value;
    }

    /**
     * Obtient la valeur de la propriété mtTtcCalc.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMtTtcCalc() {
        return mtTtcCalc;
    }

    /**
     * Définit la valeur de la propriété mtTtcCalc.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMtTtcCalc(BigDecimal value) {
        this.mtTtcCalc = value;
    }

    /**
     * Obtient la valeur de la propriété mtTvaCalc.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMtTvaCalc() {
        return mtTvaCalc;
    }

    /**
     * Définit la valeur de la propriété mtTvaCalc.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMtTvaCalc(BigDecimal value) {
        this.mtTvaCalc = value;
    }

    /**
     * Obtient la valeur de la propriété nbEDocument.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNbEDocument() {
        return nbEDocument;
    }

    /**
     * Définit la valeur de la propriété nbEDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNbEDocument(Integer value) {
        this.nbEDocument = value;
    }

    /**
     * Obtient la valeur de la propriété netAPayer.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getNetAPayer() {
        return netAPayer;
    }

    /**
     * Définit la valeur de la propriété netAPayer.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setNetAPayer(BigDecimal value) {
        this.netAPayer = value;
    }

    /**
     * Obtient la valeur de la propriété netHtCalc.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getNetHtCalc() {
        return netHtCalc;
    }

    /**
     * Définit la valeur de la propriété netHtCalc.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setNetHtCalc(BigDecimal value) {
        this.netHtCalc = value;
    }

    /**
     * Obtient la valeur de la propriété netTtcCalc.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getNetTtcCalc() {
        return netTtcCalc;
    }

    /**
     * Définit la valeur de la propriété netTtcCalc.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setNetTtcCalc(BigDecimal value) {
        this.netTtcCalc = value;
    }

    /**
     * Obtient la valeur de la propriété netTvaCalc.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getNetTvaCalc() {
        return netTvaCalc;
    }

    /**
     * Définit la valeur de la propriété netTvaCalc.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setNetTvaCalc(BigDecimal value) {
        this.netTvaCalc = value;
    }

    /**
     * Obtient la valeur de la propriété regleCompletDocument.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRegleCompletDocument() {
        return regleCompletDocument;
    }

    /**
     * Définit la valeur de la propriété regleCompletDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRegleCompletDocument(BigDecimal value) {
        this.regleCompletDocument = value;
    }

    /**
     * Obtient la valeur de la propriété regleDocument.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRegleDocument() {
        return regleDocument;
    }

    /**
     * Définit la valeur de la propriété regleDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRegleDocument(BigDecimal value) {
        this.regleDocument = value;
    }

    /**
     * Obtient la valeur de la propriété remHtDocument.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRemHtDocument() {
        return remHtDocument;
    }

    /**
     * Définit la valeur de la propriété remHtDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRemHtDocument(BigDecimal value) {
        this.remHtDocument = value;
    }

    /**
     * Obtient la valeur de la propriété remTtcDocument.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRemTtcDocument() {
        return remTtcDocument;
    }

    /**
     * Définit la valeur de la propriété remTtcDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRemTtcDocument(BigDecimal value) {
        this.remTtcDocument = value;
    }

    /**
     * Obtient la valeur de la propriété resteARegler.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getResteARegler() {
        return resteARegler;
    }

    /**
     * Définit la valeur de la propriété resteARegler.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setResteARegler(BigDecimal value) {
        this.resteARegler = value;
    }

    /**
     * Obtient la valeur de la propriété resteAReglerComplet.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getResteAReglerComplet() {
        return resteAReglerComplet;
    }

    /**
     * Définit la valeur de la propriété resteAReglerComplet.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setResteAReglerComplet(BigDecimal value) {
        this.resteAReglerComplet = value;
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
     * Obtient la valeur de la propriété taInfosDocument.
     * 
     * @return
     *     possible object is
     *     {@link TaInfosTicketDeCaisse }
     *     
     */
    public TaInfosTicketDeCaisse getTaInfosDocument() {
        return taInfosDocument;
    }

    /**
     * Définit la valeur de la propriété taInfosDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link TaInfosTicketDeCaisse }
     *     
     */
    public void setTaInfosDocument(TaInfosTicketDeCaisse value) {
        this.taInfosDocument = value;
    }

    /**
     * Obtient la valeur de la propriété taMiseADisposition.
     * 
     * @return
     *     possible object is
     *     {@link TaMiseADisposition }
     *     
     */
    public TaMiseADisposition getTaMiseADisposition() {
        return taMiseADisposition;
    }

    /**
     * Définit la valeur de la propriété taMiseADisposition.
     * 
     * @param value
     *     allowed object is
     *     {@link TaMiseADisposition }
     *     
     */
    public void setTaMiseADisposition(TaMiseADisposition value) {
        this.taMiseADisposition = value;
    }

    /**
     * Gets the value of the taRAvoirs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taRAvoirs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaRAvoirs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaRAvoir }
     * 
     * 
     */
    public List<TaRAvoir> getTaRAvoirs() {
        if (taRAvoirs == null) {
            taRAvoirs = new ArrayList<TaRAvoir>();
        }
        return this.taRAvoirs;
    }

    /**
     * Gets the value of the taRReglements property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taRReglements property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaRReglements().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaRReglement }
     * 
     * 
     */
    public List<TaRReglement> getTaRReglements() {
        if (taRReglements == null) {
            taRReglements = new ArrayList<TaRReglement>();
        }
        return this.taRReglements;
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
     * Obtient la valeur de la propriété taTPaiement.
     * 
     * @return
     *     possible object is
     *     {@link TaTPaiement }
     *     
     */
    public TaTPaiement getTaTPaiement() {
        return taTPaiement;
    }

    /**
     * Définit la valeur de la propriété taTPaiement.
     * 
     * @param value
     *     allowed object is
     *     {@link TaTPaiement }
     *     
     */
    public void setTaTPaiement(TaTPaiement value) {
        this.taTPaiement = value;
    }

    /**
     * Obtient la valeur de la propriété taTiers.
     * 
     * @return
     *     possible object is
     *     {@link TaTiers }
     *     
     */
    public TaTiers getTaTiers() {
        return taTiers;
    }

    /**
     * Définit la valeur de la propriété taTiers.
     * 
     * @param value
     *     allowed object is
     *     {@link TaTiers }
     *     
     */
    public void setTaTiers(TaTiers value) {
        this.taTiers = value;
    }

    /**
     * Obtient la valeur de la propriété taUtilisateurVendeur.
     * 
     * @return
     *     possible object is
     *     {@link TaUtilisateur }
     *     
     */
    public TaUtilisateur getTaUtilisateurVendeur() {
        return taUtilisateurVendeur;
    }

    /**
     * Définit la valeur de la propriété taUtilisateurVendeur.
     * 
     * @param value
     *     allowed object is
     *     {@link TaUtilisateur }
     *     
     */
    public void setTaUtilisateurVendeur(TaUtilisateur value) {
        this.taUtilisateurVendeur = value;
    }

    /**
     * Obtient la valeur de la propriété ttc.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTtc() {
        return ttc;
    }

    /**
     * Définit la valeur de la propriété ttc.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTtc(Integer value) {
        this.ttc = value;
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
     * Obtient la valeur de la propriété txRemTtcDocument.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTxRemTtcDocument() {
        return txRemTtcDocument;
    }

    /**
     * Définit la valeur de la propriété txRemTtcDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTxRemTtcDocument(BigDecimal value) {
        this.txRemTtcDocument = value;
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

}
