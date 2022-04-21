
package fr.legrain.bdg.compteclient.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour swtLigneDocument complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="swtLigneDocument"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ID_DOCUMENT" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="idLDocument" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="numLigneImpression" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="rowGrille" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="taArticle" type="{http://service.bdg.legrain.fr/}taArticle" minOccurs="0"/&gt;
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
@XmlType(name = "swtLigneDocument", propOrder = {
    "iddocument",
    "idLDocument",
    "numLigneImpression",
    "rowGrille",
    "taArticle",
    "versionObj"
})
@XmlSeeAlso({
    TaLFabricationMP.class,
    TaLFabricationPF.class,
    TaLAcompte.class,
    TaLApporteur.class,
    TaLAvisEcheance.class,
    TaLAvoir.class,
    TaLBonReception.class,
    TaLBoncde.class,
    TaLBonliv.class,
    TaLDevis.class,
    TaLFacture.class,
    TaLPrelevement.class,
    TaLProforma.class,
    TaLTicketDeCaisse.class
})
public abstract class SwtLigneDocument {

    @XmlElement(name = "ID_DOCUMENT")
    protected Integer iddocument;
    protected int idLDocument;
    protected int numLigneImpression;
    protected Integer rowGrille;
    protected TaArticle taArticle;
    protected Integer versionObj;

    /**
     * Obtient la valeur de la propriété iddocument.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIDDOCUMENT() {
        return iddocument;
    }

    /**
     * Définit la valeur de la propriété iddocument.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIDDOCUMENT(Integer value) {
        this.iddocument = value;
    }

    /**
     * Obtient la valeur de la propriété idLDocument.
     * 
     */
    public int getIdLDocument() {
        return idLDocument;
    }

    /**
     * Définit la valeur de la propriété idLDocument.
     * 
     */
    public void setIdLDocument(int value) {
        this.idLDocument = value;
    }

    /**
     * Obtient la valeur de la propriété numLigneImpression.
     * 
     */
    public int getNumLigneImpression() {
        return numLigneImpression;
    }

    /**
     * Définit la valeur de la propriété numLigneImpression.
     * 
     */
    public void setNumLigneImpression(int value) {
        this.numLigneImpression = value;
    }

    /**
     * Obtient la valeur de la propriété rowGrille.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRowGrille() {
        return rowGrille;
    }

    /**
     * Définit la valeur de la propriété rowGrille.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRowGrille(Integer value) {
        this.rowGrille = value;
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
