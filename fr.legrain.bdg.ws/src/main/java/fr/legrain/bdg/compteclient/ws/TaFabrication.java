
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
 * <p>Classe Java pour taFabrication complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taFabrication"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://service.bdg.legrain.fr/}swtDocument"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codeDocument" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="commentaire" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dateDebR" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dateDebT" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dateDocument" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dateExport" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dateFinR" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dateFinT" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dateLivDocument" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dateVerrouillage" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="gestionTVA" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="grpMouvement" type="{http://service.bdg.legrain.fr/}taGrMouvStock" minOccurs="0"/&gt;
 *         &lt;element name="ipAcces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="legrain" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="libelleDocument" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="lignesMatierePremieres" type="{http://service.bdg.legrain.fr/}taLFabricationMP" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="lignesProduits" type="{http://service.bdg.legrain.fr/}taLFabricationPF" maxOccurs="unbounded" minOccurs="0"/&gt;
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
 *         &lt;element name="remHtDocument" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="remTtcDocument" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="taEtat" type="{http://service.bdg.legrain.fr/}taEtat" minOccurs="0"/&gt;
 *         &lt;element name="taGrMouvStock" type="{http://service.bdg.legrain.fr/}taGrMouvStock" minOccurs="0"/&gt;
 *         &lt;element name="taInfosDocument" type="{http://service.bdg.legrain.fr/}taInfosFabrication" minOccurs="0"/&gt;
 *         &lt;element name="taLabelArticles" type="{http://service.bdg.legrain.fr/}taLabelArticle" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="taMiseADisposition" type="{http://service.bdg.legrain.fr/}taMiseADisposition" minOccurs="0"/&gt;
 *         &lt;element name="taRAcomptes" type="{http://service.bdg.legrain.fr/}taRAcompte" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="taRDocuments" type="{http://service.bdg.legrain.fr/}taRDocument" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="taTFabrication" type="{http://service.bdg.legrain.fr/}taTFabrication" minOccurs="0"/&gt;
 *         &lt;element name="taTPaiement" type="{http://service.bdg.legrain.fr/}taTPaiement" minOccurs="0"/&gt;
 *         &lt;element name="taTiers" type="{http://service.bdg.legrain.fr/}taTiers" minOccurs="0"/&gt;
 *         &lt;element name="taTiersPrestationService" type="{http://service.bdg.legrain.fr/}taTiers" minOccurs="0"/&gt;
 *         &lt;element name="taUtilisateurControleur" type="{http://service.bdg.legrain.fr/}taUtilisateur" minOccurs="0"/&gt;
 *         &lt;element name="taUtilisateurRedacteur" type="{http://service.bdg.legrain.fr/}taUtilisateur" minOccurs="0"/&gt;
 *         &lt;element name="ttc" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="txRemHtDocument" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="txRemTtcDocument" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
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
@XmlType(name = "taFabrication", propOrder = {
    "codeDocument",
    "commentaire",
    "dateDebR",
    "dateDebT",
    "dateDocument",
    "dateExport",
    "dateFinR",
    "dateFinT",
    "dateLivDocument",
    "dateVerrouillage",
    "gestionTVA",
    "grpMouvement",
    "ipAcces",
    "legrain",
    "libelleDocument",
    "lignesMatierePremieres",
    "lignesProduits",
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
    "remHtDocument",
    "remTtcDocument",
    "taEtat",
    "taGrMouvStock",
    "taInfosDocument",
    "taLabelArticles",
    "taMiseADisposition",
    "taRAcomptes",
    "taRDocuments",
    "taTFabrication",
    "taTPaiement",
    "taTiers",
    "taTiersPrestationService",
    "taUtilisateurControleur",
    "taUtilisateurRedacteur",
    "ttc",
    "txRemHtDocument",
    "txRemTtcDocument",
    "version"
})
public class TaFabrication
    extends SwtDocument
{

    protected String codeDocument;
    protected String commentaire;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateDebR;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateDebT;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateDocument;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateExport;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateFinR;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateFinT;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateLivDocument;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateVerrouillage;
    protected boolean gestionTVA;
    protected TaGrMouvStock grpMouvement;
    protected String ipAcces;
    protected boolean legrain;
    protected String libelleDocument;
    @XmlElement(nillable = true)
    protected List<TaLFabricationMP> lignesMatierePremieres;
    @XmlElement(nillable = true)
    protected List<TaLFabricationPF> lignesProduits;
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
    protected BigDecimal remHtDocument;
    protected BigDecimal remTtcDocument;
    protected TaEtat taEtat;
    protected TaGrMouvStock taGrMouvStock;
    protected TaInfosFabrication taInfosDocument;
    @XmlElement(nillable = true)
    protected List<TaLabelArticle> taLabelArticles;
    protected TaMiseADisposition taMiseADisposition;
    @XmlElement(nillable = true)
    protected List<TaRAcompte> taRAcomptes;
    @XmlElement(nillable = true)
    protected List<TaRDocument> taRDocuments;
    protected TaTFabrication taTFabrication;
    protected TaTPaiement taTPaiement;
    protected TaTiers taTiers;
    protected TaTiers taTiersPrestationService;
    protected TaUtilisateur taUtilisateurControleur;
    protected TaUtilisateur taUtilisateurRedacteur;
    protected Integer ttc;
    protected BigDecimal txRemHtDocument;
    protected BigDecimal txRemTtcDocument;
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
     * Obtient la valeur de la propriété dateDebR.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateDebR() {
        return dateDebR;
    }

    /**
     * Définit la valeur de la propriété dateDebR.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateDebR(XMLGregorianCalendar value) {
        this.dateDebR = value;
    }

    /**
     * Obtient la valeur de la propriété dateDebT.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateDebT() {
        return dateDebT;
    }

    /**
     * Définit la valeur de la propriété dateDebT.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateDebT(XMLGregorianCalendar value) {
        this.dateDebT = value;
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
     * Obtient la valeur de la propriété dateFinR.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateFinR() {
        return dateFinR;
    }

    /**
     * Définit la valeur de la propriété dateFinR.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateFinR(XMLGregorianCalendar value) {
        this.dateFinR = value;
    }

    /**
     * Obtient la valeur de la propriété dateFinT.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateFinT() {
        return dateFinT;
    }

    /**
     * Définit la valeur de la propriété dateFinT.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateFinT(XMLGregorianCalendar value) {
        this.dateFinT = value;
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
     * Obtient la valeur de la propriété grpMouvement.
     * 
     * @return
     *     possible object is
     *     {@link TaGrMouvStock }
     *     
     */
    public TaGrMouvStock getGrpMouvement() {
        return grpMouvement;
    }

    /**
     * Définit la valeur de la propriété grpMouvement.
     * 
     * @param value
     *     allowed object is
     *     {@link TaGrMouvStock }
     *     
     */
    public void setGrpMouvement(TaGrMouvStock value) {
        this.grpMouvement = value;
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
     * Gets the value of the lignesMatierePremieres property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lignesMatierePremieres property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLignesMatierePremieres().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaLFabricationMP }
     * 
     * 
     */
    public List<TaLFabricationMP> getLignesMatierePremieres() {
        if (lignesMatierePremieres == null) {
            lignesMatierePremieres = new ArrayList<TaLFabricationMP>();
        }
        return this.lignesMatierePremieres;
    }

    /**
     * Gets the value of the lignesProduits property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lignesProduits property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLignesProduits().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaLFabricationPF }
     * 
     * 
     */
    public List<TaLFabricationPF> getLignesProduits() {
        if (lignesProduits == null) {
            lignesProduits = new ArrayList<TaLFabricationPF>();
        }
        return this.lignesProduits;
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
     * Obtient la valeur de la propriété taGrMouvStock.
     * 
     * @return
     *     possible object is
     *     {@link TaGrMouvStock }
     *     
     */
    public TaGrMouvStock getTaGrMouvStock() {
        return taGrMouvStock;
    }

    /**
     * Définit la valeur de la propriété taGrMouvStock.
     * 
     * @param value
     *     allowed object is
     *     {@link TaGrMouvStock }
     *     
     */
    public void setTaGrMouvStock(TaGrMouvStock value) {
        this.taGrMouvStock = value;
    }

    /**
     * Obtient la valeur de la propriété taInfosDocument.
     * 
     * @return
     *     possible object is
     *     {@link TaInfosFabrication }
     *     
     */
    public TaInfosFabrication getTaInfosDocument() {
        return taInfosDocument;
    }

    /**
     * Définit la valeur de la propriété taInfosDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link TaInfosFabrication }
     *     
     */
    public void setTaInfosDocument(TaInfosFabrication value) {
        this.taInfosDocument = value;
    }

    /**
     * Gets the value of the taLabelArticles property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taLabelArticles property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaLabelArticles().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaLabelArticle }
     * 
     * 
     */
    public List<TaLabelArticle> getTaLabelArticles() {
        if (taLabelArticles == null) {
            taLabelArticles = new ArrayList<TaLabelArticle>();
        }
        return this.taLabelArticles;
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
     * Gets the value of the taRAcomptes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taRAcomptes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaRAcomptes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaRAcompte }
     * 
     * 
     */
    public List<TaRAcompte> getTaRAcomptes() {
        if (taRAcomptes == null) {
            taRAcomptes = new ArrayList<TaRAcompte>();
        }
        return this.taRAcomptes;
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
     * Obtient la valeur de la propriété taTFabrication.
     * 
     * @return
     *     possible object is
     *     {@link TaTFabrication }
     *     
     */
    public TaTFabrication getTaTFabrication() {
        return taTFabrication;
    }

    /**
     * Définit la valeur de la propriété taTFabrication.
     * 
     * @param value
     *     allowed object is
     *     {@link TaTFabrication }
     *     
     */
    public void setTaTFabrication(TaTFabrication value) {
        this.taTFabrication = value;
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
     * Obtient la valeur de la propriété taTiersPrestationService.
     * 
     * @return
     *     possible object is
     *     {@link TaTiers }
     *     
     */
    public TaTiers getTaTiersPrestationService() {
        return taTiersPrestationService;
    }

    /**
     * Définit la valeur de la propriété taTiersPrestationService.
     * 
     * @param value
     *     allowed object is
     *     {@link TaTiers }
     *     
     */
    public void setTaTiersPrestationService(TaTiers value) {
        this.taTiersPrestationService = value;
    }

    /**
     * Obtient la valeur de la propriété taUtilisateurControleur.
     * 
     * @return
     *     possible object is
     *     {@link TaUtilisateur }
     *     
     */
    public TaUtilisateur getTaUtilisateurControleur() {
        return taUtilisateurControleur;
    }

    /**
     * Définit la valeur de la propriété taUtilisateurControleur.
     * 
     * @param value
     *     allowed object is
     *     {@link TaUtilisateur }
     *     
     */
    public void setTaUtilisateurControleur(TaUtilisateur value) {
        this.taUtilisateurControleur = value;
    }

    /**
     * Obtient la valeur de la propriété taUtilisateurRedacteur.
     * 
     * @return
     *     possible object is
     *     {@link TaUtilisateur }
     *     
     */
    public TaUtilisateur getTaUtilisateurRedacteur() {
        return taUtilisateurRedacteur;
    }

    /**
     * Définit la valeur de la propriété taUtilisateurRedacteur.
     * 
     * @param value
     *     allowed object is
     *     {@link TaUtilisateur }
     *     
     */
    public void setTaUtilisateurRedacteur(TaUtilisateur value) {
        this.taUtilisateurRedacteur = value;
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
