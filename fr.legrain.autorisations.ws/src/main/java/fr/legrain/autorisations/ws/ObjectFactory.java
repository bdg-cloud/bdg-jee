
package fr.legrain.autorisations.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the fr.legrain.autorisations.ws package. 
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

    private final static QName _ValidateEntityPropertyResponse_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "validateEntityPropertyResponse");
    private final static QName _FindByCodeResponse_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "findByCodeResponse");
    private final static QName _PersistValidationContextResponse_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "persistValidationContextResponse");
    private final static QName _ValidateDTOProperty_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "validateDTOProperty");
    private final static QName _ValidateEntityValidationContext_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "validateEntityValidationContext");
    private final static QName _MergeValidationContext_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "mergeValidationContext");
    private final static QName _MergeDTOValidationContext_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "mergeDTOValidationContext");
    private final static QName _Auth_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "auth");
    private final static QName _ListEntityToListDTOResponse_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "listEntityToListDTOResponse");
    private final static QName _MergeDTOResponse_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "mergeDTOResponse");
    private final static QName _SelectAllDTO_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "selectAllDTO");
    private final static QName _FindWithJPQLQueryDTOResponse_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "findWithJPQLQueryDTOResponse");
    private final static QName _CreateException_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "CreateException");
    private final static QName _RemoveDTO_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "removeDTO");
    private final static QName _FindByDossierTypeProduitResponse_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "findByDossierTypeProduitResponse");
    private final static QName _ErrorResponse_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "errorResponse");
    private final static QName _ValidateEntityPropertyValidationContext_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "validateEntityPropertyValidationContext");
    private final static QName _FindWithNamedQueryDTO_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "findWithNamedQueryDTO");
    private final static QName _RemoveDTOResponse_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "removeDTOResponse");
    private final static QName _FindWithNamedQueryDTOResponse_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "findWithNamedQueryDTOResponse");
    private final static QName _ValidateDTOValidationContextResponse_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "validateDTOValidationContextResponse");
    private final static QName _EJBException_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "EJBException");
    private final static QName _ValidateDTOPropertyResponse_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "validateDTOPropertyResponse");
    private final static QName _MergeDTO_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "mergeDTO");
    private final static QName _PersistDTO_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "persistDTO");
    private final static QName _FindByCodeDTO_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "findByCodeDTO");
    private final static QName _FindByIdDTOResponse_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "findByIdDTOResponse");
    private final static QName _ValidateDTOValidationContext_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "validateDTOValidationContext");
    private final static QName _SelectCount_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "selectCount");
    private final static QName _MergeValidationContextResponse_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "mergeValidationContextResponse");
    private final static QName _Persist_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "persist");
    private final static QName _SelectAllResponse_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "selectAllResponse");
    private final static QName _ValidateEntityValidationContextResponse_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "validateEntityValidationContextResponse");
    private final static QName _ValidateDTOResponse_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "validateDTOResponse");
    private final static QName _EntityToDTO_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "entityToDTO");
    private final static QName _ValidateDTOPropertyValidationContextResponse_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "validateDTOPropertyValidationContextResponse");
    private final static QName _PersistDTOResponse_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "persistDTOResponse");
    private final static QName _EntityToDTOResponse_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "entityToDTOResponse");
    private final static QName _ValidateEntityResponse_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "validateEntityResponse");
    private final static QName _SelectAllDTOResponse_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "selectAllDTOResponse");
    private final static QName _RemoveException_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "RemoveException");
    private final static QName _PersistDTOValidationContextResponse_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "persistDTOValidationContextResponse");
    private final static QName _FindByIdResponse_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "findByIdResponse");
    private final static QName _Error_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "error");
    private final static QName _ValidateEntity_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "validateEntity");
    private final static QName _PersistValidationContext_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "persistValidationContext");
    private final static QName _FindByIdDTO_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "findByIdDTO");
    private final static QName _ListEntityToListDTO_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "listEntityToListDTO");
    private final static QName _ValidateEntityPropertyValidationContextResponse_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "validateEntityPropertyValidationContextResponse");
    private final static QName _PersistDTOValidationContext_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "persistDTOValidationContext");
    private final static QName _ValidateDTO_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "validateDTO");
    private final static QName _SelectAll_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "selectAll");
    private final static QName _MergeDTOValidationContextResponse_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "mergeDTOValidationContextResponse");
    private final static QName _FindById_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "findById");
    private final static QName _FindByDossierTypeProduit_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "findByDossierTypeProduit");
    private final static QName _FindByCode_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "findByCode");
    private final static QName _PersistResponse_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "persistResponse");
    private final static QName _ValidateDTOPropertyValidationContext_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "validateDTOPropertyValidationContext");
    private final static QName _ValidateEntityProperty_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "validateEntityProperty");
    private final static QName _MergeResponse_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "mergeResponse");
    private final static QName _SelectCountResponse_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "selectCountResponse");
    private final static QName _FinderException_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "FinderException");
    private final static QName _Remove_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "remove");
    private final static QName _Merge_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "merge");
    private final static QName _RemoveResponse_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "removeResponse");
    private final static QName _FindByCodeDTOResponse_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "findByCodeDTOResponse");
    private final static QName _FindWithJPQLQueryDTO_QNAME = new QName("http://service.autorisations.autorisations.legrain.fr/", "findWithJPQLQueryDTO");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: fr.legrain.autorisations.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ListeModules }
     * 
     */
    public ListeModules createListeModules() {
        return new ListeModules();
    }

    /**
     * Create an instance of {@link FindByCodeDTO }
     * 
     */
    public FindByCodeDTO createFindByCodeDTO() {
        return new FindByCodeDTO();
    }

    /**
     * Create an instance of {@link FindByIdDTOResponse }
     * 
     */
    public FindByIdDTOResponse createFindByIdDTOResponse() {
        return new FindByIdDTOResponse();
    }

    /**
     * Create an instance of {@link ValidateDTOValidationContext }
     * 
     */
    public ValidateDTOValidationContext createValidateDTOValidationContext() {
        return new ValidateDTOValidationContext();
    }

    /**
     * Create an instance of {@link SelectCount }
     * 
     */
    public SelectCount createSelectCount() {
        return new SelectCount();
    }

    /**
     * Create an instance of {@link MergeValidationContextResponse }
     * 
     */
    public MergeValidationContextResponse createMergeValidationContextResponse() {
        return new MergeValidationContextResponse();
    }

    /**
     * Create an instance of {@link Persist }
     * 
     */
    public Persist createPersist() {
        return new Persist();
    }

    /**
     * Create an instance of {@link SelectAllResponse }
     * 
     */
    public SelectAllResponse createSelectAllResponse() {
        return new SelectAllResponse();
    }

    /**
     * Create an instance of {@link ValidateEntityValidationContextResponse }
     * 
     */
    public ValidateEntityValidationContextResponse createValidateEntityValidationContextResponse() {
        return new ValidateEntityValidationContextResponse();
    }

    /**
     * Create an instance of {@link ValidateDTOResponse }
     * 
     */
    public ValidateDTOResponse createValidateDTOResponse() {
        return new ValidateDTOResponse();
    }

    /**
     * Create an instance of {@link EntityToDTO }
     * 
     */
    public EntityToDTO createEntityToDTO() {
        return new EntityToDTO();
    }

    /**
     * Create an instance of {@link ValidateDTOPropertyValidationContextResponse }
     * 
     */
    public ValidateDTOPropertyValidationContextResponse createValidateDTOPropertyValidationContextResponse() {
        return new ValidateDTOPropertyValidationContextResponse();
    }

    /**
     * Create an instance of {@link FindWithNamedQueryDTOResponse }
     * 
     */
    public FindWithNamedQueryDTOResponse createFindWithNamedQueryDTOResponse() {
        return new FindWithNamedQueryDTOResponse();
    }

    /**
     * Create an instance of {@link ValidateDTOValidationContextResponse }
     * 
     */
    public ValidateDTOValidationContextResponse createValidateDTOValidationContextResponse() {
        return new ValidateDTOValidationContextResponse();
    }

    /**
     * Create an instance of {@link EJBException }
     * 
     */
    public EJBException createEJBException() {
        return new EJBException();
    }

    /**
     * Create an instance of {@link ValidateDTOPropertyResponse }
     * 
     */
    public ValidateDTOPropertyResponse createValidateDTOPropertyResponse() {
        return new ValidateDTOPropertyResponse();
    }

    /**
     * Create an instance of {@link MergeDTO }
     * 
     */
    public MergeDTO createMergeDTO() {
        return new MergeDTO();
    }

    /**
     * Create an instance of {@link PersistDTO }
     * 
     */
    public PersistDTO createPersistDTO() {
        return new PersistDTO();
    }

    /**
     * Create an instance of {@link ListEntityToListDTOResponse }
     * 
     */
    public ListEntityToListDTOResponse createListEntityToListDTOResponse() {
        return new ListEntityToListDTOResponse();
    }

    /**
     * Create an instance of {@link MergeDTOResponse }
     * 
     */
    public MergeDTOResponse createMergeDTOResponse() {
        return new MergeDTOResponse();
    }

    /**
     * Create an instance of {@link CreateException }
     * 
     */
    public CreateException createCreateException() {
        return new CreateException();
    }

    /**
     * Create an instance of {@link FindWithJPQLQueryDTOResponse }
     * 
     */
    public FindWithJPQLQueryDTOResponse createFindWithJPQLQueryDTOResponse() {
        return new FindWithJPQLQueryDTOResponse();
    }

    /**
     * Create an instance of {@link SelectAllDTO }
     * 
     */
    public SelectAllDTO createSelectAllDTO() {
        return new SelectAllDTO();
    }

    /**
     * Create an instance of {@link FindByDossierTypeProduitResponse }
     * 
     */
    public FindByDossierTypeProduitResponse createFindByDossierTypeProduitResponse() {
        return new FindByDossierTypeProduitResponse();
    }

    /**
     * Create an instance of {@link RemoveDTO }
     * 
     */
    public RemoveDTO createRemoveDTO() {
        return new RemoveDTO();
    }

    /**
     * Create an instance of {@link ErrorResponse }
     * 
     */
    public ErrorResponse createErrorResponse() {
        return new ErrorResponse();
    }

    /**
     * Create an instance of {@link ValidateEntityPropertyValidationContext }
     * 
     */
    public ValidateEntityPropertyValidationContext createValidateEntityPropertyValidationContext() {
        return new ValidateEntityPropertyValidationContext();
    }

    /**
     * Create an instance of {@link FindWithNamedQueryDTO }
     * 
     */
    public FindWithNamedQueryDTO createFindWithNamedQueryDTO() {
        return new FindWithNamedQueryDTO();
    }

    /**
     * Create an instance of {@link RemoveDTOResponse }
     * 
     */
    public RemoveDTOResponse createRemoveDTOResponse() {
        return new RemoveDTOResponse();
    }

    /**
     * Create an instance of {@link ValidateEntityPropertyResponse }
     * 
     */
    public ValidateEntityPropertyResponse createValidateEntityPropertyResponse() {
        return new ValidateEntityPropertyResponse();
    }

    /**
     * Create an instance of {@link FindByCodeResponse }
     * 
     */
    public FindByCodeResponse createFindByCodeResponse() {
        return new FindByCodeResponse();
    }

    /**
     * Create an instance of {@link PersistValidationContextResponse }
     * 
     */
    public PersistValidationContextResponse createPersistValidationContextResponse() {
        return new PersistValidationContextResponse();
    }

    /**
     * Create an instance of {@link ValidateDTOProperty }
     * 
     */
    public ValidateDTOProperty createValidateDTOProperty() {
        return new ValidateDTOProperty();
    }

    /**
     * Create an instance of {@link ValidateEntityValidationContext }
     * 
     */
    public ValidateEntityValidationContext createValidateEntityValidationContext() {
        return new ValidateEntityValidationContext();
    }

    /**
     * Create an instance of {@link MergeValidationContext }
     * 
     */
    public MergeValidationContext createMergeValidationContext() {
        return new MergeValidationContext();
    }

    /**
     * Create an instance of {@link MergeDTOValidationContext }
     * 
     */
    public MergeDTOValidationContext createMergeDTOValidationContext() {
        return new MergeDTOValidationContext();
    }

    /**
     * Create an instance of {@link ValidateEntityProperty }
     * 
     */
    public ValidateEntityProperty createValidateEntityProperty() {
        return new ValidateEntityProperty();
    }

    /**
     * Create an instance of {@link MergeResponse }
     * 
     */
    public MergeResponse createMergeResponse() {
        return new MergeResponse();
    }

    /**
     * Create an instance of {@link SelectCountResponse }
     * 
     */
    public SelectCountResponse createSelectCountResponse() {
        return new SelectCountResponse();
    }

    /**
     * Create an instance of {@link FinderException }
     * 
     */
    public FinderException createFinderException() {
        return new FinderException();
    }

    /**
     * Create an instance of {@link Remove }
     * 
     */
    public Remove createRemove() {
        return new Remove();
    }

    /**
     * Create an instance of {@link Merge }
     * 
     */
    public Merge createMerge() {
        return new Merge();
    }

    /**
     * Create an instance of {@link RemoveResponse }
     * 
     */
    public RemoveResponse createRemoveResponse() {
        return new RemoveResponse();
    }

    /**
     * Create an instance of {@link FindByCodeDTOResponse }
     * 
     */
    public FindByCodeDTOResponse createFindByCodeDTOResponse() {
        return new FindByCodeDTOResponse();
    }

    /**
     * Create an instance of {@link FindWithJPQLQueryDTO }
     * 
     */
    public FindWithJPQLQueryDTO createFindWithJPQLQueryDTO() {
        return new FindWithJPQLQueryDTO();
    }

    /**
     * Create an instance of {@link MergeDTOValidationContextResponse }
     * 
     */
    public MergeDTOValidationContextResponse createMergeDTOValidationContextResponse() {
        return new MergeDTOValidationContextResponse();
    }

    /**
     * Create an instance of {@link FindById }
     * 
     */
    public FindById createFindById() {
        return new FindById();
    }

    /**
     * Create an instance of {@link FindByDossierTypeProduit }
     * 
     */
    public FindByDossierTypeProduit createFindByDossierTypeProduit() {
        return new FindByDossierTypeProduit();
    }

    /**
     * Create an instance of {@link FindByCode }
     * 
     */
    public FindByCode createFindByCode() {
        return new FindByCode();
    }

    /**
     * Create an instance of {@link PersistResponse }
     * 
     */
    public PersistResponse createPersistResponse() {
        return new PersistResponse();
    }

    /**
     * Create an instance of {@link ValidateDTOPropertyValidationContext }
     * 
     */
    public ValidateDTOPropertyValidationContext createValidateDTOPropertyValidationContext() {
        return new ValidateDTOPropertyValidationContext();
    }

    /**
     * Create an instance of {@link FindByIdResponse }
     * 
     */
    public FindByIdResponse createFindByIdResponse() {
        return new FindByIdResponse();
    }

    /**
     * Create an instance of {@link Error }
     * 
     */
    public Error createError() {
        return new Error();
    }

    /**
     * Create an instance of {@link ValidateEntity }
     * 
     */
    public ValidateEntity createValidateEntity() {
        return new ValidateEntity();
    }

    /**
     * Create an instance of {@link PersistValidationContext }
     * 
     */
    public PersistValidationContext createPersistValidationContext() {
        return new PersistValidationContext();
    }

    /**
     * Create an instance of {@link ValidateEntityPropertyValidationContextResponse }
     * 
     */
    public ValidateEntityPropertyValidationContextResponse createValidateEntityPropertyValidationContextResponse() {
        return new ValidateEntityPropertyValidationContextResponse();
    }

    /**
     * Create an instance of {@link ListEntityToListDTO }
     * 
     */
    public ListEntityToListDTO createListEntityToListDTO() {
        return new ListEntityToListDTO();
    }

    /**
     * Create an instance of {@link FindByIdDTO }
     * 
     */
    public FindByIdDTO createFindByIdDTO() {
        return new FindByIdDTO();
    }

    /**
     * Create an instance of {@link PersistDTOValidationContext }
     * 
     */
    public PersistDTOValidationContext createPersistDTOValidationContext() {
        return new PersistDTOValidationContext();
    }

    /**
     * Create an instance of {@link SelectAll }
     * 
     */
    public SelectAll createSelectAll() {
        return new SelectAll();
    }

    /**
     * Create an instance of {@link ValidateDTO }
     * 
     */
    public ValidateDTO createValidateDTO() {
        return new ValidateDTO();
    }

    /**
     * Create an instance of {@link ValidateEntityResponse }
     * 
     */
    public ValidateEntityResponse createValidateEntityResponse() {
        return new ValidateEntityResponse();
    }

    /**
     * Create an instance of {@link EntityToDTOResponse }
     * 
     */
    public EntityToDTOResponse createEntityToDTOResponse() {
        return new EntityToDTOResponse();
    }

    /**
     * Create an instance of {@link PersistDTOResponse }
     * 
     */
    public PersistDTOResponse createPersistDTOResponse() {
        return new PersistDTOResponse();
    }

    /**
     * Create an instance of {@link SelectAllDTOResponse }
     * 
     */
    public SelectAllDTOResponse createSelectAllDTOResponse() {
        return new SelectAllDTOResponse();
    }

    /**
     * Create an instance of {@link RemoveException }
     * 
     */
    public RemoveException createRemoveException() {
        return new RemoveException();
    }

    /**
     * Create an instance of {@link PersistDTOValidationContextResponse }
     * 
     */
    public PersistDTOValidationContextResponse createPersistDTOValidationContextResponse() {
        return new PersistDTOValidationContextResponse();
    }

    /**
     * Create an instance of {@link ModelObject }
     * 
     */
    public ModelObject createModelObject() {
        return new ModelObject();
    }

    /**
     * Create an instance of {@link TaTypeProduit }
     * 
     */
    public TaTypeProduit createTaTypeProduit() {
        return new TaTypeProduit();
    }

    /**
     * Create an instance of {@link TaAutorisationsDTO }
     * 
     */
    public TaAutorisationsDTO createTaAutorisationsDTO() {
        return new TaAutorisationsDTO();
    }

    /**
     * Create an instance of {@link Module }
     * 
     */
    public Module createModule() {
        return new Module();
    }

    /**
     * Create an instance of {@link TaAutorisations }
     * 
     */
    public TaAutorisations createTaAutorisations() {
        return new TaAutorisations();
    }

    /**
     * Create an instance of {@link ListeModules.Modules }
     * 
     */
    public ListeModules.Modules createListeModulesModules() {
        return new ListeModules.Modules();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidateEntityPropertyResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "validateEntityPropertyResponse")
    public JAXBElement<ValidateEntityPropertyResponse> createValidateEntityPropertyResponse(ValidateEntityPropertyResponse value) {
        return new JAXBElement<ValidateEntityPropertyResponse>(_ValidateEntityPropertyResponse_QNAME, ValidateEntityPropertyResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindByCodeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "findByCodeResponse")
    public JAXBElement<FindByCodeResponse> createFindByCodeResponse(FindByCodeResponse value) {
        return new JAXBElement<FindByCodeResponse>(_FindByCodeResponse_QNAME, FindByCodeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PersistValidationContextResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "persistValidationContextResponse")
    public JAXBElement<PersistValidationContextResponse> createPersistValidationContextResponse(PersistValidationContextResponse value) {
        return new JAXBElement<PersistValidationContextResponse>(_PersistValidationContextResponse_QNAME, PersistValidationContextResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidateDTOProperty }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "validateDTOProperty")
    public JAXBElement<ValidateDTOProperty> createValidateDTOProperty(ValidateDTOProperty value) {
        return new JAXBElement<ValidateDTOProperty>(_ValidateDTOProperty_QNAME, ValidateDTOProperty.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidateEntityValidationContext }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "validateEntityValidationContext")
    public JAXBElement<ValidateEntityValidationContext> createValidateEntityValidationContext(ValidateEntityValidationContext value) {
        return new JAXBElement<ValidateEntityValidationContext>(_ValidateEntityValidationContext_QNAME, ValidateEntityValidationContext.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MergeValidationContext }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "mergeValidationContext")
    public JAXBElement<MergeValidationContext> createMergeValidationContext(MergeValidationContext value) {
        return new JAXBElement<MergeValidationContext>(_MergeValidationContext_QNAME, MergeValidationContext.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MergeDTOValidationContext }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "mergeDTOValidationContext")
    public JAXBElement<MergeDTOValidationContext> createMergeDTOValidationContext(MergeDTOValidationContext value) {
        return new JAXBElement<MergeDTOValidationContext>(_MergeDTOValidationContext_QNAME, MergeDTOValidationContext.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListeModules }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "auth")
    public JAXBElement<ListeModules> createAuth(ListeModules value) {
        return new JAXBElement<ListeModules>(_Auth_QNAME, ListeModules.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListEntityToListDTOResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "listEntityToListDTOResponse")
    public JAXBElement<ListEntityToListDTOResponse> createListEntityToListDTOResponse(ListEntityToListDTOResponse value) {
        return new JAXBElement<ListEntityToListDTOResponse>(_ListEntityToListDTOResponse_QNAME, ListEntityToListDTOResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MergeDTOResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "mergeDTOResponse")
    public JAXBElement<MergeDTOResponse> createMergeDTOResponse(MergeDTOResponse value) {
        return new JAXBElement<MergeDTOResponse>(_MergeDTOResponse_QNAME, MergeDTOResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SelectAllDTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "selectAllDTO")
    public JAXBElement<SelectAllDTO> createSelectAllDTO(SelectAllDTO value) {
        return new JAXBElement<SelectAllDTO>(_SelectAllDTO_QNAME, SelectAllDTO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindWithJPQLQueryDTOResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "findWithJPQLQueryDTOResponse")
    public JAXBElement<FindWithJPQLQueryDTOResponse> createFindWithJPQLQueryDTOResponse(FindWithJPQLQueryDTOResponse value) {
        return new JAXBElement<FindWithJPQLQueryDTOResponse>(_FindWithJPQLQueryDTOResponse_QNAME, FindWithJPQLQueryDTOResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "CreateException")
    public JAXBElement<CreateException> createCreateException(CreateException value) {
        return new JAXBElement<CreateException>(_CreateException_QNAME, CreateException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveDTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "removeDTO")
    public JAXBElement<RemoveDTO> createRemoveDTO(RemoveDTO value) {
        return new JAXBElement<RemoveDTO>(_RemoveDTO_QNAME, RemoveDTO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindByDossierTypeProduitResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "findByDossierTypeProduitResponse")
    public JAXBElement<FindByDossierTypeProduitResponse> createFindByDossierTypeProduitResponse(FindByDossierTypeProduitResponse value) {
        return new JAXBElement<FindByDossierTypeProduitResponse>(_FindByDossierTypeProduitResponse_QNAME, FindByDossierTypeProduitResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ErrorResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "errorResponse")
    public JAXBElement<ErrorResponse> createErrorResponse(ErrorResponse value) {
        return new JAXBElement<ErrorResponse>(_ErrorResponse_QNAME, ErrorResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidateEntityPropertyValidationContext }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "validateEntityPropertyValidationContext")
    public JAXBElement<ValidateEntityPropertyValidationContext> createValidateEntityPropertyValidationContext(ValidateEntityPropertyValidationContext value) {
        return new JAXBElement<ValidateEntityPropertyValidationContext>(_ValidateEntityPropertyValidationContext_QNAME, ValidateEntityPropertyValidationContext.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindWithNamedQueryDTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "findWithNamedQueryDTO")
    public JAXBElement<FindWithNamedQueryDTO> createFindWithNamedQueryDTO(FindWithNamedQueryDTO value) {
        return new JAXBElement<FindWithNamedQueryDTO>(_FindWithNamedQueryDTO_QNAME, FindWithNamedQueryDTO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveDTOResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "removeDTOResponse")
    public JAXBElement<RemoveDTOResponse> createRemoveDTOResponse(RemoveDTOResponse value) {
        return new JAXBElement<RemoveDTOResponse>(_RemoveDTOResponse_QNAME, RemoveDTOResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindWithNamedQueryDTOResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "findWithNamedQueryDTOResponse")
    public JAXBElement<FindWithNamedQueryDTOResponse> createFindWithNamedQueryDTOResponse(FindWithNamedQueryDTOResponse value) {
        return new JAXBElement<FindWithNamedQueryDTOResponse>(_FindWithNamedQueryDTOResponse_QNAME, FindWithNamedQueryDTOResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidateDTOValidationContextResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "validateDTOValidationContextResponse")
    public JAXBElement<ValidateDTOValidationContextResponse> createValidateDTOValidationContextResponse(ValidateDTOValidationContextResponse value) {
        return new JAXBElement<ValidateDTOValidationContextResponse>(_ValidateDTOValidationContextResponse_QNAME, ValidateDTOValidationContextResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EJBException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "EJBException")
    public JAXBElement<EJBException> createEJBException(EJBException value) {
        return new JAXBElement<EJBException>(_EJBException_QNAME, EJBException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidateDTOPropertyResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "validateDTOPropertyResponse")
    public JAXBElement<ValidateDTOPropertyResponse> createValidateDTOPropertyResponse(ValidateDTOPropertyResponse value) {
        return new JAXBElement<ValidateDTOPropertyResponse>(_ValidateDTOPropertyResponse_QNAME, ValidateDTOPropertyResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MergeDTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "mergeDTO")
    public JAXBElement<MergeDTO> createMergeDTO(MergeDTO value) {
        return new JAXBElement<MergeDTO>(_MergeDTO_QNAME, MergeDTO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PersistDTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "persistDTO")
    public JAXBElement<PersistDTO> createPersistDTO(PersistDTO value) {
        return new JAXBElement<PersistDTO>(_PersistDTO_QNAME, PersistDTO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindByCodeDTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "findByCodeDTO")
    public JAXBElement<FindByCodeDTO> createFindByCodeDTO(FindByCodeDTO value) {
        return new JAXBElement<FindByCodeDTO>(_FindByCodeDTO_QNAME, FindByCodeDTO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindByIdDTOResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "findByIdDTOResponse")
    public JAXBElement<FindByIdDTOResponse> createFindByIdDTOResponse(FindByIdDTOResponse value) {
        return new JAXBElement<FindByIdDTOResponse>(_FindByIdDTOResponse_QNAME, FindByIdDTOResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidateDTOValidationContext }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "validateDTOValidationContext")
    public JAXBElement<ValidateDTOValidationContext> createValidateDTOValidationContext(ValidateDTOValidationContext value) {
        return new JAXBElement<ValidateDTOValidationContext>(_ValidateDTOValidationContext_QNAME, ValidateDTOValidationContext.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SelectCount }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "selectCount")
    public JAXBElement<SelectCount> createSelectCount(SelectCount value) {
        return new JAXBElement<SelectCount>(_SelectCount_QNAME, SelectCount.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MergeValidationContextResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "mergeValidationContextResponse")
    public JAXBElement<MergeValidationContextResponse> createMergeValidationContextResponse(MergeValidationContextResponse value) {
        return new JAXBElement<MergeValidationContextResponse>(_MergeValidationContextResponse_QNAME, MergeValidationContextResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Persist }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "persist")
    public JAXBElement<Persist> createPersist(Persist value) {
        return new JAXBElement<Persist>(_Persist_QNAME, Persist.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SelectAllResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "selectAllResponse")
    public JAXBElement<SelectAllResponse> createSelectAllResponse(SelectAllResponse value) {
        return new JAXBElement<SelectAllResponse>(_SelectAllResponse_QNAME, SelectAllResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidateEntityValidationContextResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "validateEntityValidationContextResponse")
    public JAXBElement<ValidateEntityValidationContextResponse> createValidateEntityValidationContextResponse(ValidateEntityValidationContextResponse value) {
        return new JAXBElement<ValidateEntityValidationContextResponse>(_ValidateEntityValidationContextResponse_QNAME, ValidateEntityValidationContextResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidateDTOResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "validateDTOResponse")
    public JAXBElement<ValidateDTOResponse> createValidateDTOResponse(ValidateDTOResponse value) {
        return new JAXBElement<ValidateDTOResponse>(_ValidateDTOResponse_QNAME, ValidateDTOResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityToDTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "entityToDTO")
    public JAXBElement<EntityToDTO> createEntityToDTO(EntityToDTO value) {
        return new JAXBElement<EntityToDTO>(_EntityToDTO_QNAME, EntityToDTO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidateDTOPropertyValidationContextResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "validateDTOPropertyValidationContextResponse")
    public JAXBElement<ValidateDTOPropertyValidationContextResponse> createValidateDTOPropertyValidationContextResponse(ValidateDTOPropertyValidationContextResponse value) {
        return new JAXBElement<ValidateDTOPropertyValidationContextResponse>(_ValidateDTOPropertyValidationContextResponse_QNAME, ValidateDTOPropertyValidationContextResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PersistDTOResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "persistDTOResponse")
    public JAXBElement<PersistDTOResponse> createPersistDTOResponse(PersistDTOResponse value) {
        return new JAXBElement<PersistDTOResponse>(_PersistDTOResponse_QNAME, PersistDTOResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityToDTOResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "entityToDTOResponse")
    public JAXBElement<EntityToDTOResponse> createEntityToDTOResponse(EntityToDTOResponse value) {
        return new JAXBElement<EntityToDTOResponse>(_EntityToDTOResponse_QNAME, EntityToDTOResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidateEntityResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "validateEntityResponse")
    public JAXBElement<ValidateEntityResponse> createValidateEntityResponse(ValidateEntityResponse value) {
        return new JAXBElement<ValidateEntityResponse>(_ValidateEntityResponse_QNAME, ValidateEntityResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SelectAllDTOResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "selectAllDTOResponse")
    public JAXBElement<SelectAllDTOResponse> createSelectAllDTOResponse(SelectAllDTOResponse value) {
        return new JAXBElement<SelectAllDTOResponse>(_SelectAllDTOResponse_QNAME, SelectAllDTOResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "RemoveException")
    public JAXBElement<RemoveException> createRemoveException(RemoveException value) {
        return new JAXBElement<RemoveException>(_RemoveException_QNAME, RemoveException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PersistDTOValidationContextResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "persistDTOValidationContextResponse")
    public JAXBElement<PersistDTOValidationContextResponse> createPersistDTOValidationContextResponse(PersistDTOValidationContextResponse value) {
        return new JAXBElement<PersistDTOValidationContextResponse>(_PersistDTOValidationContextResponse_QNAME, PersistDTOValidationContextResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindByIdResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "findByIdResponse")
    public JAXBElement<FindByIdResponse> createFindByIdResponse(FindByIdResponse value) {
        return new JAXBElement<FindByIdResponse>(_FindByIdResponse_QNAME, FindByIdResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Error }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "error")
    public JAXBElement<Error> createError(Error value) {
        return new JAXBElement<Error>(_Error_QNAME, Error.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidateEntity }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "validateEntity")
    public JAXBElement<ValidateEntity> createValidateEntity(ValidateEntity value) {
        return new JAXBElement<ValidateEntity>(_ValidateEntity_QNAME, ValidateEntity.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PersistValidationContext }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "persistValidationContext")
    public JAXBElement<PersistValidationContext> createPersistValidationContext(PersistValidationContext value) {
        return new JAXBElement<PersistValidationContext>(_PersistValidationContext_QNAME, PersistValidationContext.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindByIdDTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "findByIdDTO")
    public JAXBElement<FindByIdDTO> createFindByIdDTO(FindByIdDTO value) {
        return new JAXBElement<FindByIdDTO>(_FindByIdDTO_QNAME, FindByIdDTO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListEntityToListDTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "listEntityToListDTO")
    public JAXBElement<ListEntityToListDTO> createListEntityToListDTO(ListEntityToListDTO value) {
        return new JAXBElement<ListEntityToListDTO>(_ListEntityToListDTO_QNAME, ListEntityToListDTO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidateEntityPropertyValidationContextResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "validateEntityPropertyValidationContextResponse")
    public JAXBElement<ValidateEntityPropertyValidationContextResponse> createValidateEntityPropertyValidationContextResponse(ValidateEntityPropertyValidationContextResponse value) {
        return new JAXBElement<ValidateEntityPropertyValidationContextResponse>(_ValidateEntityPropertyValidationContextResponse_QNAME, ValidateEntityPropertyValidationContextResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PersistDTOValidationContext }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "persistDTOValidationContext")
    public JAXBElement<PersistDTOValidationContext> createPersistDTOValidationContext(PersistDTOValidationContext value) {
        return new JAXBElement<PersistDTOValidationContext>(_PersistDTOValidationContext_QNAME, PersistDTOValidationContext.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidateDTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "validateDTO")
    public JAXBElement<ValidateDTO> createValidateDTO(ValidateDTO value) {
        return new JAXBElement<ValidateDTO>(_ValidateDTO_QNAME, ValidateDTO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SelectAll }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "selectAll")
    public JAXBElement<SelectAll> createSelectAll(SelectAll value) {
        return new JAXBElement<SelectAll>(_SelectAll_QNAME, SelectAll.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MergeDTOValidationContextResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "mergeDTOValidationContextResponse")
    public JAXBElement<MergeDTOValidationContextResponse> createMergeDTOValidationContextResponse(MergeDTOValidationContextResponse value) {
        return new JAXBElement<MergeDTOValidationContextResponse>(_MergeDTOValidationContextResponse_QNAME, MergeDTOValidationContextResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindById }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "findById")
    public JAXBElement<FindById> createFindById(FindById value) {
        return new JAXBElement<FindById>(_FindById_QNAME, FindById.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindByDossierTypeProduit }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "findByDossierTypeProduit")
    public JAXBElement<FindByDossierTypeProduit> createFindByDossierTypeProduit(FindByDossierTypeProduit value) {
        return new JAXBElement<FindByDossierTypeProduit>(_FindByDossierTypeProduit_QNAME, FindByDossierTypeProduit.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindByCode }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "findByCode")
    public JAXBElement<FindByCode> createFindByCode(FindByCode value) {
        return new JAXBElement<FindByCode>(_FindByCode_QNAME, FindByCode.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PersistResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "persistResponse")
    public JAXBElement<PersistResponse> createPersistResponse(PersistResponse value) {
        return new JAXBElement<PersistResponse>(_PersistResponse_QNAME, PersistResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidateDTOPropertyValidationContext }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "validateDTOPropertyValidationContext")
    public JAXBElement<ValidateDTOPropertyValidationContext> createValidateDTOPropertyValidationContext(ValidateDTOPropertyValidationContext value) {
        return new JAXBElement<ValidateDTOPropertyValidationContext>(_ValidateDTOPropertyValidationContext_QNAME, ValidateDTOPropertyValidationContext.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidateEntityProperty }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "validateEntityProperty")
    public JAXBElement<ValidateEntityProperty> createValidateEntityProperty(ValidateEntityProperty value) {
        return new JAXBElement<ValidateEntityProperty>(_ValidateEntityProperty_QNAME, ValidateEntityProperty.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MergeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "mergeResponse")
    public JAXBElement<MergeResponse> createMergeResponse(MergeResponse value) {
        return new JAXBElement<MergeResponse>(_MergeResponse_QNAME, MergeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SelectCountResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "selectCountResponse")
    public JAXBElement<SelectCountResponse> createSelectCountResponse(SelectCountResponse value) {
        return new JAXBElement<SelectCountResponse>(_SelectCountResponse_QNAME, SelectCountResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FinderException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "FinderException")
    public JAXBElement<FinderException> createFinderException(FinderException value) {
        return new JAXBElement<FinderException>(_FinderException_QNAME, FinderException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Remove }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "remove")
    public JAXBElement<Remove> createRemove(Remove value) {
        return new JAXBElement<Remove>(_Remove_QNAME, Remove.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Merge }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "merge")
    public JAXBElement<Merge> createMerge(Merge value) {
        return new JAXBElement<Merge>(_Merge_QNAME, Merge.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "removeResponse")
    public JAXBElement<RemoveResponse> createRemoveResponse(RemoveResponse value) {
        return new JAXBElement<RemoveResponse>(_RemoveResponse_QNAME, RemoveResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindByCodeDTOResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "findByCodeDTOResponse")
    public JAXBElement<FindByCodeDTOResponse> createFindByCodeDTOResponse(FindByCodeDTOResponse value) {
        return new JAXBElement<FindByCodeDTOResponse>(_FindByCodeDTOResponse_QNAME, FindByCodeDTOResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindWithJPQLQueryDTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.autorisations.autorisations.legrain.fr/", name = "findWithJPQLQueryDTO")
    public JAXBElement<FindWithJPQLQueryDTO> createFindWithJPQLQueryDTO(FindWithJPQLQueryDTO value) {
        return new JAXBElement<FindWithJPQLQueryDTO>(_FindWithJPQLQueryDTO_QNAME, FindWithJPQLQueryDTO.class, null, value);
    }

}
