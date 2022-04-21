
package fr.legrain.gandi.api.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the fr.legrain.gandi.api.ws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _DeleteDNSSubdomainResponse_QNAME = new QName("http://api.gandi.legrain.fr/", "deleteDNSSubdomainResponse");
    private final static QName _RegisterDNSSubDomain_QNAME = new QName("http://api.gandi.legrain.fr/", "registerDNSSubDomain");
    private final static QName _RegisterDNSSubDomainResponse_QNAME = new QName("http://api.gandi.legrain.fr/", "registerDNSSubDomainResponse");
    private final static QName _DeleteDNSSubdomain_QNAME = new QName("http://api.gandi.legrain.fr/", "deleteDNSSubdomain");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: fr.legrain.gandi.api.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DeleteDNSSubdomain }
     * 
     */
    public DeleteDNSSubdomain createDeleteDNSSubdomain() {
        return new DeleteDNSSubdomain();
    }

    /**
     * Create an instance of {@link RegisterDNSSubDomainResponse }
     * 
     */
    public RegisterDNSSubDomainResponse createRegisterDNSSubDomainResponse() {
        return new RegisterDNSSubDomainResponse();
    }

    /**
     * Create an instance of {@link RegisterDNSSubDomain }
     * 
     */
    public RegisterDNSSubDomain createRegisterDNSSubDomain() {
        return new RegisterDNSSubDomain();
    }

    /**
     * Create an instance of {@link DeleteDNSSubdomainResponse }
     * 
     */
    public DeleteDNSSubdomainResponse createDeleteDNSSubdomainResponse() {
        return new DeleteDNSSubdomainResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteDNSSubdomainResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.gandi.legrain.fr/", name = "deleteDNSSubdomainResponse")
    public JAXBElement<DeleteDNSSubdomainResponse> createDeleteDNSSubdomainResponse(DeleteDNSSubdomainResponse value) {
        return new JAXBElement<DeleteDNSSubdomainResponse>(_DeleteDNSSubdomainResponse_QNAME, DeleteDNSSubdomainResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegisterDNSSubDomain }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.gandi.legrain.fr/", name = "registerDNSSubDomain")
    public JAXBElement<RegisterDNSSubDomain> createRegisterDNSSubDomain(RegisterDNSSubDomain value) {
        return new JAXBElement<RegisterDNSSubDomain>(_RegisterDNSSubDomain_QNAME, RegisterDNSSubDomain.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegisterDNSSubDomainResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.gandi.legrain.fr/", name = "registerDNSSubDomainResponse")
    public JAXBElement<RegisterDNSSubDomainResponse> createRegisterDNSSubDomainResponse(RegisterDNSSubDomainResponse value) {
        return new JAXBElement<RegisterDNSSubDomainResponse>(_RegisterDNSSubDomainResponse_QNAME, RegisterDNSSubDomainResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteDNSSubdomain }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.gandi.legrain.fr/", name = "deleteDNSSubdomain")
    public JAXBElement<DeleteDNSSubdomain> createDeleteDNSSubdomain(DeleteDNSSubdomain value) {
        return new JAXBElement<DeleteDNSSubdomain>(_DeleteDNSSubdomain_QNAME, DeleteDNSSubdomain.class, null, value);
    }

}
