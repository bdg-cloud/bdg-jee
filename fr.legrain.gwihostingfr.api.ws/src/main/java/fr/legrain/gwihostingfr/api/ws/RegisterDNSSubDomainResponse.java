
package fr.legrain.gwihostingfr.api.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour anonymous complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="registerDNSSubDomainResult" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "registerDNSSubDomainResult"
})
@XmlRootElement(name = "registerDNSSubDomainResponse")
public class RegisterDNSSubDomainResponse {

    protected boolean registerDNSSubDomainResult;

    /**
     * Obtient la valeur de la propriété registerDNSSubDomainResult.
     * 
     */
    public boolean isRegisterDNSSubDomainResult() {
        return registerDNSSubDomainResult;
    }

    /**
     * Définit la valeur de la propriété registerDNSSubDomainResult.
     * 
     */
    public void setRegisterDNSSubDomainResult(boolean value) {
        this.registerDNSSubDomainResult = value;
    }

}
