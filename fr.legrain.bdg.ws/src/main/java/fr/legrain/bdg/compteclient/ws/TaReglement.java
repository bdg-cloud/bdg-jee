
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
 * <p>Classe Java pour taReglement complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taReglement"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://service.bdg.legrain.fr/}swtDocument"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codeDocument" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="commentaire" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="compteClient" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="dateDocument" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dateEchDocument" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dateExport" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dateLivDocument" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dateVerrouillage" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="etatDeSuppression" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="gestionTVA" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="ipAcces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="legrain" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="libelleDocument" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="netTtcCalc" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="refPaiement" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="regleCompletDocument" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="resteAAffecter" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="resteAReglerComplet" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="service" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="taAcompte" type="{http://service.bdg.legrain.fr/}taAcompte" minOccurs="0"/&gt;
 *         &lt;element name="taCompteBanque" type="{http://service.bdg.legrain.fr/}taCompteBanque" minOccurs="0"/&gt;
 *         &lt;element name="taEtat" type="{http://service.bdg.legrain.fr/}taEtat" minOccurs="0"/&gt;
 *         &lt;element name="taInfosDocument" type="{http://service.bdg.legrain.fr/}iInfosDocumentTiers" minOccurs="0"/&gt;
 *         &lt;element name="taMiseADisposition" type="{http://service.bdg.legrain.fr/}taMiseADisposition" minOccurs="0"/&gt;
 *         &lt;element name="taPrelevement" type="{http://service.bdg.legrain.fr/}taPrelevement" minOccurs="0"/&gt;
 *         &lt;element name="taRDocuments" type="{http://service.bdg.legrain.fr/}taRDocument" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="taRReglements" type="{http://service.bdg.legrain.fr/}taRReglement" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="taTPaiement" type="{http://service.bdg.legrain.fr/}taTPaiement" minOccurs="0"/&gt;
 *         &lt;element name="taTiers" type="{http://service.bdg.legrain.fr/}taTiers" minOccurs="0"/&gt;
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
@XmlType(name = "taReglement", propOrder = {
    "codeDocument",
    "commentaire",
    "compteClient",
    "dateDocument",
    "dateEchDocument",
    "dateExport",
    "dateLivDocument",
    "dateVerrouillage",
    "etatDeSuppression",
    "gestionTVA",
    "ipAcces",
    "legrain",
    "libelleDocument",
    "netTtcCalc",
    "refPaiement",
    "regleCompletDocument",
    "resteAAffecter",
    "resteAReglerComplet",
    "service",
    "taAcompte",
    "taCompteBanque",
    "taEtat",
    "taInfosDocument",
    "taMiseADisposition",
    "taPrelevement",
    "taRDocuments",
    "taRReglements",
    "taTPaiement",
    "taTiers",
    "ttc",
    "txRemHtDocument",
    "txRemTtcDocument",
    "typeDocument",
    "version"
})
public class TaReglement
    extends SwtDocument
{

    protected String codeDocument;
    protected String commentaire;
    protected Boolean compteClient;
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
    protected int etatDeSuppression;
    protected boolean gestionTVA;
    protected String ipAcces;
    protected boolean legrain;
    protected String libelleDocument;
    protected BigDecimal netTtcCalc;
    protected String refPaiement;
    protected BigDecimal regleCompletDocument;
    protected BigDecimal resteAAffecter;
    protected BigDecimal resteAReglerComplet;
    protected String service;
    protected TaAcompte taAcompte;
    protected TaCompteBanque taCompteBanque;
    protected TaEtat taEtat;
    protected IInfosDocumentTiers taInfosDocument;
    protected TaMiseADisposition taMiseADisposition;
    protected TaPrelevement taPrelevement;
    @XmlElement(nillable = true)
    protected List<TaRDocument> taRDocuments;
    @XmlElement(nillable = true)
    protected List<TaRReglement> taRReglements;
    protected TaTPaiement taTPaiement;
    protected TaTiers taTiers;
    protected Integer ttc;
    protected BigDecimal txRemHtDocument;
    protected BigDecimal txRemTtcDocument;
    protected String typeDocument;
    protected String version;

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
     * Obtient la valeur de la propriété compteClient.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCompteClient() {
        return compteClient;
    }

    /**
     * Définit la valeur de la propriété compteClient.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCompteClient(Boolean value) {
        this.compteClient = value;
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
     * Obtient la valeur de la propriété etatDeSuppression.
     * 
     */
    public int getEtatDeSuppression() {
        return etatDeSuppression;
    }

    /**
     * Définit la valeur de la propriété etatDeSuppression.
     * 
     */
    public void setEtatDeSuppression(int value) {
        this.etatDeSuppression = value;
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
     * Obtient la valeur de la propriété refPaiement.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefPaiement() {
        return refPaiement;
    }

    /**
     * Définit la valeur de la propriété refPaiement.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefPaiement(String value) {
        this.refPaiement = value;
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
     * Obtient la valeur de la propriété resteAAffecter.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getResteAAffecter() {
        return resteAAffecter;
    }

    /**
     * Définit la valeur de la propriété resteAAffecter.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setResteAAffecter(BigDecimal value) {
        this.resteAAffecter = value;
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
     * Obtient la valeur de la propriété service.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getService() {
        return service;
    }

    /**
     * Définit la valeur de la propriété service.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setService(String value) {
        this.service = value;
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
     * Obtient la valeur de la propriété taCompteBanque.
     * 
     * @return
     *     possible object is
     *     {@link TaCompteBanque }
     *     
     */
    public TaCompteBanque getTaCompteBanque() {
        return taCompteBanque;
    }

    /**
     * Définit la valeur de la propriété taCompteBanque.
     * 
     * @param value
     *     allowed object is
     *     {@link TaCompteBanque }
     *     
     */
    public void setTaCompteBanque(TaCompteBanque value) {
        this.taCompteBanque = value;
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
     *     {@link IInfosDocumentTiers }
     *     
     */
    public IInfosDocumentTiers getTaInfosDocument() {
        return taInfosDocument;
    }

    /**
     * Définit la valeur de la propriété taInfosDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link IInfosDocumentTiers }
     *     
     */
    public void setTaInfosDocument(IInfosDocumentTiers value) {
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
     * Gets the value of the taRDocuments property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taRDocuments property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaRDocuments().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaRDocument }
     * 
     * 
     */
    public List<TaRDocument> getTaRDocuments() {
        if (taRDocuments == null) {
            taRDocuments = new ArrayList<TaRDocument>();
        }
        return this.taRDocuments;
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
