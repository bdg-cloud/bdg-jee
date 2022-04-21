
package fr.legrain.bdg.compteclient.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;


/**
 * <p>Classe Java pour taInfosAcompte complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="taInfosAcompte"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://service.bdg.legrain.fr/}swtInfosDocument"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="adresse1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="adresse1Liv" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="adresse2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="adresse2Liv" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="adresse3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="adresse3Liv" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codeCPaiement" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codeCompta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codeTCivilite" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codeTEntite" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codeTTvaDoc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codepostal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codepostalLiv" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="compte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="finMoisCPaiement" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="libCPaiement" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nomEntreprise" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nomTiers" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="pays" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="paysLiv" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="prenomTiers" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="reportCPaiement" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="surnomTiers" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="taDocument" type="{http://service.bdg.legrain.fr/}taAcompte" minOccurs="0"/&gt;
 *         &lt;element name="tvaIComCompl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ville" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="villeLiv" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "taInfosAcompte", propOrder = {
    "adresse1",
    "adresse1Liv",
    "adresse2",
    "adresse2Liv",
    "adresse3",
    "adresse3Liv",
    "codeCPaiement",
    "codeCompta",
    "codeTCivilite",
    "codeTEntite",
    "codeTTvaDoc",
    "codepostal",
    "codepostalLiv",
    "compte",
    "finMoisCPaiement",
    "libCPaiement",
    "nomEntreprise",
    "nomTiers",
    "pays",
    "paysLiv",
    "prenomTiers",
    "reportCPaiement",
    "surnomTiers",
    "taDocument",
    "tvaIComCompl",
    "ville",
    "villeLiv"
})
public class TaInfosAcompte
    extends SwtInfosDocument
{

    protected String adresse1;
    protected String adresse1Liv;
    protected String adresse2;
    protected String adresse2Liv;
    protected String adresse3;
    protected String adresse3Liv;
    protected String codeCPaiement;
    protected String codeCompta;
    protected String codeTCivilite;
    protected String codeTEntite;
    protected String codeTTvaDoc;
    protected String codepostal;
    protected String codepostalLiv;
    protected String compte;
    protected Integer finMoisCPaiement;
    protected String libCPaiement;
    protected String nomEntreprise;
    protected String nomTiers;
    protected String pays;
    protected String paysLiv;
    protected String prenomTiers;
    protected Integer reportCPaiement;
    protected String surnomTiers;
    @XmlInverseReference(mappedBy = "taInfosDocument")
    @XmlElement
    protected TaAcompte taDocument;
    protected String tvaIComCompl;
    protected String ville;
    protected String villeLiv;

    /**
     * Obtient la valeur de la propriété adresse1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdresse1() {
        return adresse1;
    }

    /**
     * Définit la valeur de la propriété adresse1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdresse1(String value) {
        this.adresse1 = value;
    }

    /**
     * Obtient la valeur de la propriété adresse1Liv.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdresse1Liv() {
        return adresse1Liv;
    }

    /**
     * Définit la valeur de la propriété adresse1Liv.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdresse1Liv(String value) {
        this.adresse1Liv = value;
    }

    /**
     * Obtient la valeur de la propriété adresse2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdresse2() {
        return adresse2;
    }

    /**
     * Définit la valeur de la propriété adresse2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdresse2(String value) {
        this.adresse2 = value;
    }

    /**
     * Obtient la valeur de la propriété adresse2Liv.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdresse2Liv() {
        return adresse2Liv;
    }

    /**
     * Définit la valeur de la propriété adresse2Liv.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdresse2Liv(String value) {
        this.adresse2Liv = value;
    }

    /**
     * Obtient la valeur de la propriété adresse3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdresse3() {
        return adresse3;
    }

    /**
     * Définit la valeur de la propriété adresse3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdresse3(String value) {
        this.adresse3 = value;
    }

    /**
     * Obtient la valeur de la propriété adresse3Liv.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdresse3Liv() {
        return adresse3Liv;
    }

    /**
     * Définit la valeur de la propriété adresse3Liv.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdresse3Liv(String value) {
        this.adresse3Liv = value;
    }

    /**
     * Obtient la valeur de la propriété codeCPaiement.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeCPaiement() {
        return codeCPaiement;
    }

    /**
     * Définit la valeur de la propriété codeCPaiement.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeCPaiement(String value) {
        this.codeCPaiement = value;
    }

    /**
     * Obtient la valeur de la propriété codeCompta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeCompta() {
        return codeCompta;
    }

    /**
     * Définit la valeur de la propriété codeCompta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeCompta(String value) {
        this.codeCompta = value;
    }

    /**
     * Obtient la valeur de la propriété codeTCivilite.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeTCivilite() {
        return codeTCivilite;
    }

    /**
     * Définit la valeur de la propriété codeTCivilite.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeTCivilite(String value) {
        this.codeTCivilite = value;
    }

    /**
     * Obtient la valeur de la propriété codeTEntite.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeTEntite() {
        return codeTEntite;
    }

    /**
     * Définit la valeur de la propriété codeTEntite.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeTEntite(String value) {
        this.codeTEntite = value;
    }

    /**
     * Obtient la valeur de la propriété codeTTvaDoc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeTTvaDoc() {
        return codeTTvaDoc;
    }

    /**
     * Définit la valeur de la propriété codeTTvaDoc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeTTvaDoc(String value) {
        this.codeTTvaDoc = value;
    }

    /**
     * Obtient la valeur de la propriété codepostal.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodepostal() {
        return codepostal;
    }

    /**
     * Définit la valeur de la propriété codepostal.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodepostal(String value) {
        this.codepostal = value;
    }

    /**
     * Obtient la valeur de la propriété codepostalLiv.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodepostalLiv() {
        return codepostalLiv;
    }

    /**
     * Définit la valeur de la propriété codepostalLiv.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodepostalLiv(String value) {
        this.codepostalLiv = value;
    }

    /**
     * Obtient la valeur de la propriété compte.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompte() {
        return compte;
    }

    /**
     * Définit la valeur de la propriété compte.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompte(String value) {
        this.compte = value;
    }

    /**
     * Obtient la valeur de la propriété finMoisCPaiement.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getFinMoisCPaiement() {
        return finMoisCPaiement;
    }

    /**
     * Définit la valeur de la propriété finMoisCPaiement.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setFinMoisCPaiement(Integer value) {
        this.finMoisCPaiement = value;
    }

    /**
     * Obtient la valeur de la propriété libCPaiement.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLibCPaiement() {
        return libCPaiement;
    }

    /**
     * Définit la valeur de la propriété libCPaiement.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLibCPaiement(String value) {
        this.libCPaiement = value;
    }

    /**
     * Obtient la valeur de la propriété nomEntreprise.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomEntreprise() {
        return nomEntreprise;
    }

    /**
     * Définit la valeur de la propriété nomEntreprise.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomEntreprise(String value) {
        this.nomEntreprise = value;
    }

    /**
     * Obtient la valeur de la propriété nomTiers.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomTiers() {
        return nomTiers;
    }

    /**
     * Définit la valeur de la propriété nomTiers.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomTiers(String value) {
        this.nomTiers = value;
    }

    /**
     * Obtient la valeur de la propriété pays.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPays() {
        return pays;
    }

    /**
     * Définit la valeur de la propriété pays.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPays(String value) {
        this.pays = value;
    }

    /**
     * Obtient la valeur de la propriété paysLiv.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaysLiv() {
        return paysLiv;
    }

    /**
     * Définit la valeur de la propriété paysLiv.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaysLiv(String value) {
        this.paysLiv = value;
    }

    /**
     * Obtient la valeur de la propriété prenomTiers.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrenomTiers() {
        return prenomTiers;
    }

    /**
     * Définit la valeur de la propriété prenomTiers.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrenomTiers(String value) {
        this.prenomTiers = value;
    }

    /**
     * Obtient la valeur de la propriété reportCPaiement.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getReportCPaiement() {
        return reportCPaiement;
    }

    /**
     * Définit la valeur de la propriété reportCPaiement.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setReportCPaiement(Integer value) {
        this.reportCPaiement = value;
    }

    /**
     * Obtient la valeur de la propriété surnomTiers.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSurnomTiers() {
        return surnomTiers;
    }

    /**
     * Définit la valeur de la propriété surnomTiers.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSurnomTiers(String value) {
        this.surnomTiers = value;
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
     * Obtient la valeur de la propriété tvaIComCompl.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTvaIComCompl() {
        return tvaIComCompl;
    }

    /**
     * Définit la valeur de la propriété tvaIComCompl.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTvaIComCompl(String value) {
        this.tvaIComCompl = value;
    }

    /**
     * Obtient la valeur de la propriété ville.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVille() {
        return ville;
    }

    /**
     * Définit la valeur de la propriété ville.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVille(String value) {
        this.ville = value;
    }

    /**
     * Obtient la valeur de la propriété villeLiv.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVilleLiv() {
        return villeLiv;
    }

    /**
     * Définit la valeur de la propriété villeLiv.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVilleLiv(String value) {
        this.villeLiv = value;
    }

}
