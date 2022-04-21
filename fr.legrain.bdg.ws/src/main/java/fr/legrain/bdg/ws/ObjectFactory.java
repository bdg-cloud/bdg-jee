
package fr.legrain.bdg.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the fr.legrain.bdg.ws package. 
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

    private final static QName _RestaureDBDumpSchemaResponse_QNAME = new QName("http://service.general.legrain.fr/", "restaureDBDumpSchemaResponse");
    private final static QName _BackupDBResponse_QNAME = new QName("http://service.general.legrain.fr/", "backupDBResponse");
    private final static QName _BackupDBDefaultAll_QNAME = new QName("http://service.general.legrain.fr/", "backupDBDefaultAll");
    private final static QName _SupprimerDossierResponse_QNAME = new QName("http://service.general.legrain.fr/", "supprimerDossierResponse");
    private final static QName _RestaureDBDumpSchema_QNAME = new QName("http://service.general.legrain.fr/", "restaureDBDumpSchema");
    private final static QName _RestaureDBDumpResponse_QNAME = new QName("http://service.general.legrain.fr/", "restaureDBDumpResponse");
    private final static QName _RebootServeur_QNAME = new QName("http://service.general.legrain.fr/", "rebootServeur");
    private final static QName _BackupDBSchema_QNAME = new QName("http://service.general.legrain.fr/", "backupDBSchema");
    private final static QName _ListeSchemaTailleConnectionResponse_QNAME = new QName("http://service.general.legrain.fr/", "listeSchemaTailleConnectionResponse");
    private final static QName _RestaureDBDump_QNAME = new QName("http://service.general.legrain.fr/", "restaureDBDump");
    private final static QName _IOException_QNAME = new QName("http://service.general.legrain.fr/", "IOException");
    private final static QName _BackupDBDefaultAllResponse_QNAME = new QName("http://service.general.legrain.fr/", "backupDBDefaultAllResponse");
    private final static QName _BackupDBMySQLResponse_QNAME = new QName("http://service.general.legrain.fr/", "backupDBMySQLResponse");
    private final static QName _ListeBddResponse_QNAME = new QName("http://service.general.legrain.fr/", "listeBddResponse");
    private final static QName _SupprimerDossier_QNAME = new QName("http://service.general.legrain.fr/", "supprimerDossier");
    private final static QName _AjoutDossier_QNAME = new QName("http://service.general.legrain.fr/", "ajoutDossier");
    private final static QName _ListeBdd_QNAME = new QName("http://service.general.legrain.fr/", "listeBdd");
    private final static QName _ListeSchemaTailleConnection_QNAME = new QName("http://service.general.legrain.fr/", "listeSchemaTailleConnection");
    private final static QName _BackupDB_QNAME = new QName("http://service.general.legrain.fr/", "backupDB");
    private final static QName _RebootServeurResponse_QNAME = new QName("http://service.general.legrain.fr/", "rebootServeurResponse");
    private final static QName _BackupDBSchemaBaseResponse_QNAME = new QName("http://service.general.legrain.fr/", "backupDBSchemaBaseResponse");
    private final static QName _BackupDBMySQL_QNAME = new QName("http://service.general.legrain.fr/", "backupDBMySQL");
    private final static QName _BackupDBSchemaResponse_QNAME = new QName("http://service.general.legrain.fr/", "backupDBSchemaResponse");
    private final static QName _AjoutDossierResponse_QNAME = new QName("http://service.general.legrain.fr/", "ajoutDossierResponse");
    private final static QName _ListeFichierDump_QNAME = new QName("http://service.general.legrain.fr/", "listeFichierDump");
    private final static QName _ListeFichierDumpResponse_QNAME = new QName("http://service.general.legrain.fr/", "listeFichierDumpResponse");
    private final static QName _BackupDBSchemaBase_QNAME = new QName("http://service.general.legrain.fr/", "backupDBSchemaBase");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: fr.legrain.bdg.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BackupDBSchema }
     * 
     */
    public BackupDBSchema createBackupDBSchema() {
        return new BackupDBSchema();
    }

    /**
     * Create an instance of {@link IOException }
     * 
     */
    public IOException createIOException() {
        return new IOException();
    }

    /**
     * Create an instance of {@link RestaureDBDump }
     * 
     */
    public RestaureDBDump createRestaureDBDump() {
        return new RestaureDBDump();
    }

    /**
     * Create an instance of {@link ListeSchemaTailleConnectionResponse }
     * 
     */
    public ListeSchemaTailleConnectionResponse createListeSchemaTailleConnectionResponse() {
        return new ListeSchemaTailleConnectionResponse();
    }

    /**
     * Create an instance of {@link SupprimerDossierResponse }
     * 
     */
    public SupprimerDossierResponse createSupprimerDossierResponse() {
        return new SupprimerDossierResponse();
    }

    /**
     * Create an instance of {@link BackupDBDefaultAll }
     * 
     */
    public BackupDBDefaultAll createBackupDBDefaultAll() {
        return new BackupDBDefaultAll();
    }

    /**
     * Create an instance of {@link RebootServeur }
     * 
     */
    public RebootServeur createRebootServeur() {
        return new RebootServeur();
    }

    /**
     * Create an instance of {@link RestaureDBDumpResponse }
     * 
     */
    public RestaureDBDumpResponse createRestaureDBDumpResponse() {
        return new RestaureDBDumpResponse();
    }

    /**
     * Create an instance of {@link RestaureDBDumpSchema }
     * 
     */
    public RestaureDBDumpSchema createRestaureDBDumpSchema() {
        return new RestaureDBDumpSchema();
    }

    /**
     * Create an instance of {@link BackupDBResponse }
     * 
     */
    public BackupDBResponse createBackupDBResponse() {
        return new BackupDBResponse();
    }

    /**
     * Create an instance of {@link RestaureDBDumpSchemaResponse }
     * 
     */
    public RestaureDBDumpSchemaResponse createRestaureDBDumpSchemaResponse() {
        return new RestaureDBDumpSchemaResponse();
    }

    /**
     * Create an instance of {@link BackupDBSchemaBase }
     * 
     */
    public BackupDBSchemaBase createBackupDBSchemaBase() {
        return new BackupDBSchemaBase();
    }

    /**
     * Create an instance of {@link ListeFichierDump }
     * 
     */
    public ListeFichierDump createListeFichierDump() {
        return new ListeFichierDump();
    }

    /**
     * Create an instance of {@link ListeFichierDumpResponse }
     * 
     */
    public ListeFichierDumpResponse createListeFichierDumpResponse() {
        return new ListeFichierDumpResponse();
    }

    /**
     * Create an instance of {@link BackupDBMySQL }
     * 
     */
    public BackupDBMySQL createBackupDBMySQL() {
        return new BackupDBMySQL();
    }

    /**
     * Create an instance of {@link BackupDBSchemaBaseResponse }
     * 
     */
    public BackupDBSchemaBaseResponse createBackupDBSchemaBaseResponse() {
        return new BackupDBSchemaBaseResponse();
    }

    /**
     * Create an instance of {@link RebootServeurResponse }
     * 
     */
    public RebootServeurResponse createRebootServeurResponse() {
        return new RebootServeurResponse();
    }

    /**
     * Create an instance of {@link AjoutDossierResponse }
     * 
     */
    public AjoutDossierResponse createAjoutDossierResponse() {
        return new AjoutDossierResponse();
    }

    /**
     * Create an instance of {@link BackupDBSchemaResponse }
     * 
     */
    public BackupDBSchemaResponse createBackupDBSchemaResponse() {
        return new BackupDBSchemaResponse();
    }

    /**
     * Create an instance of {@link ListeSchemaTailleConnection }
     * 
     */
    public ListeSchemaTailleConnection createListeSchemaTailleConnection() {
        return new ListeSchemaTailleConnection();
    }

    /**
     * Create an instance of {@link ListeBdd }
     * 
     */
    public ListeBdd createListeBdd() {
        return new ListeBdd();
    }

    /**
     * Create an instance of {@link BackupDB }
     * 
     */
    public BackupDB createBackupDB() {
        return new BackupDB();
    }

    /**
     * Create an instance of {@link SupprimerDossier }
     * 
     */
    public SupprimerDossier createSupprimerDossier() {
        return new SupprimerDossier();
    }

    /**
     * Create an instance of {@link AjoutDossier }
     * 
     */
    public AjoutDossier createAjoutDossier() {
        return new AjoutDossier();
    }

    /**
     * Create an instance of {@link ListeBddResponse }
     * 
     */
    public ListeBddResponse createListeBddResponse() {
        return new ListeBddResponse();
    }

    /**
     * Create an instance of {@link BackupDBMySQLResponse }
     * 
     */
    public BackupDBMySQLResponse createBackupDBMySQLResponse() {
        return new BackupDBMySQLResponse();
    }

    /**
     * Create an instance of {@link BackupDBDefaultAllResponse }
     * 
     */
    public BackupDBDefaultAllResponse createBackupDBDefaultAllResponse() {
        return new BackupDBDefaultAllResponse();
    }

    /**
     * Create an instance of {@link AnyTypeArray }
     * 
     */
    public AnyTypeArray createAnyTypeArray() {
        return new AnyTypeArray();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RestaureDBDumpSchemaResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.general.legrain.fr/", name = "restaureDBDumpSchemaResponse")
    public JAXBElement<RestaureDBDumpSchemaResponse> createRestaureDBDumpSchemaResponse(RestaureDBDumpSchemaResponse value) {
        return new JAXBElement<RestaureDBDumpSchemaResponse>(_RestaureDBDumpSchemaResponse_QNAME, RestaureDBDumpSchemaResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BackupDBResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.general.legrain.fr/", name = "backupDBResponse")
    public JAXBElement<BackupDBResponse> createBackupDBResponse(BackupDBResponse value) {
        return new JAXBElement<BackupDBResponse>(_BackupDBResponse_QNAME, BackupDBResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BackupDBDefaultAll }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.general.legrain.fr/", name = "backupDBDefaultAll")
    public JAXBElement<BackupDBDefaultAll> createBackupDBDefaultAll(BackupDBDefaultAll value) {
        return new JAXBElement<BackupDBDefaultAll>(_BackupDBDefaultAll_QNAME, BackupDBDefaultAll.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SupprimerDossierResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.general.legrain.fr/", name = "supprimerDossierResponse")
    public JAXBElement<SupprimerDossierResponse> createSupprimerDossierResponse(SupprimerDossierResponse value) {
        return new JAXBElement<SupprimerDossierResponse>(_SupprimerDossierResponse_QNAME, SupprimerDossierResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RestaureDBDumpSchema }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.general.legrain.fr/", name = "restaureDBDumpSchema")
    public JAXBElement<RestaureDBDumpSchema> createRestaureDBDumpSchema(RestaureDBDumpSchema value) {
        return new JAXBElement<RestaureDBDumpSchema>(_RestaureDBDumpSchema_QNAME, RestaureDBDumpSchema.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RestaureDBDumpResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.general.legrain.fr/", name = "restaureDBDumpResponse")
    public JAXBElement<RestaureDBDumpResponse> createRestaureDBDumpResponse(RestaureDBDumpResponse value) {
        return new JAXBElement<RestaureDBDumpResponse>(_RestaureDBDumpResponse_QNAME, RestaureDBDumpResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RebootServeur }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.general.legrain.fr/", name = "rebootServeur")
    public JAXBElement<RebootServeur> createRebootServeur(RebootServeur value) {
        return new JAXBElement<RebootServeur>(_RebootServeur_QNAME, RebootServeur.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BackupDBSchema }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.general.legrain.fr/", name = "backupDBSchema")
    public JAXBElement<BackupDBSchema> createBackupDBSchema(BackupDBSchema value) {
        return new JAXBElement<BackupDBSchema>(_BackupDBSchema_QNAME, BackupDBSchema.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListeSchemaTailleConnectionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.general.legrain.fr/", name = "listeSchemaTailleConnectionResponse")
    public JAXBElement<ListeSchemaTailleConnectionResponse> createListeSchemaTailleConnectionResponse(ListeSchemaTailleConnectionResponse value) {
        return new JAXBElement<ListeSchemaTailleConnectionResponse>(_ListeSchemaTailleConnectionResponse_QNAME, ListeSchemaTailleConnectionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RestaureDBDump }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.general.legrain.fr/", name = "restaureDBDump")
    public JAXBElement<RestaureDBDump> createRestaureDBDump(RestaureDBDump value) {
        return new JAXBElement<RestaureDBDump>(_RestaureDBDump_QNAME, RestaureDBDump.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IOException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.general.legrain.fr/", name = "IOException")
    public JAXBElement<IOException> createIOException(IOException value) {
        return new JAXBElement<IOException>(_IOException_QNAME, IOException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BackupDBDefaultAllResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.general.legrain.fr/", name = "backupDBDefaultAllResponse")
    public JAXBElement<BackupDBDefaultAllResponse> createBackupDBDefaultAllResponse(BackupDBDefaultAllResponse value) {
        return new JAXBElement<BackupDBDefaultAllResponse>(_BackupDBDefaultAllResponse_QNAME, BackupDBDefaultAllResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BackupDBMySQLResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.general.legrain.fr/", name = "backupDBMySQLResponse")
    public JAXBElement<BackupDBMySQLResponse> createBackupDBMySQLResponse(BackupDBMySQLResponse value) {
        return new JAXBElement<BackupDBMySQLResponse>(_BackupDBMySQLResponse_QNAME, BackupDBMySQLResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListeBddResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.general.legrain.fr/", name = "listeBddResponse")
    public JAXBElement<ListeBddResponse> createListeBddResponse(ListeBddResponse value) {
        return new JAXBElement<ListeBddResponse>(_ListeBddResponse_QNAME, ListeBddResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SupprimerDossier }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.general.legrain.fr/", name = "supprimerDossier")
    public JAXBElement<SupprimerDossier> createSupprimerDossier(SupprimerDossier value) {
        return new JAXBElement<SupprimerDossier>(_SupprimerDossier_QNAME, SupprimerDossier.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AjoutDossier }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.general.legrain.fr/", name = "ajoutDossier")
    public JAXBElement<AjoutDossier> createAjoutDossier(AjoutDossier value) {
        return new JAXBElement<AjoutDossier>(_AjoutDossier_QNAME, AjoutDossier.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListeBdd }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.general.legrain.fr/", name = "listeBdd")
    public JAXBElement<ListeBdd> createListeBdd(ListeBdd value) {
        return new JAXBElement<ListeBdd>(_ListeBdd_QNAME, ListeBdd.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListeSchemaTailleConnection }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.general.legrain.fr/", name = "listeSchemaTailleConnection")
    public JAXBElement<ListeSchemaTailleConnection> createListeSchemaTailleConnection(ListeSchemaTailleConnection value) {
        return new JAXBElement<ListeSchemaTailleConnection>(_ListeSchemaTailleConnection_QNAME, ListeSchemaTailleConnection.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BackupDB }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.general.legrain.fr/", name = "backupDB")
    public JAXBElement<BackupDB> createBackupDB(BackupDB value) {
        return new JAXBElement<BackupDB>(_BackupDB_QNAME, BackupDB.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RebootServeurResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.general.legrain.fr/", name = "rebootServeurResponse")
    public JAXBElement<RebootServeurResponse> createRebootServeurResponse(RebootServeurResponse value) {
        return new JAXBElement<RebootServeurResponse>(_RebootServeurResponse_QNAME, RebootServeurResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BackupDBSchemaBaseResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.general.legrain.fr/", name = "backupDBSchemaBaseResponse")
    public JAXBElement<BackupDBSchemaBaseResponse> createBackupDBSchemaBaseResponse(BackupDBSchemaBaseResponse value) {
        return new JAXBElement<BackupDBSchemaBaseResponse>(_BackupDBSchemaBaseResponse_QNAME, BackupDBSchemaBaseResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BackupDBMySQL }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.general.legrain.fr/", name = "backupDBMySQL")
    public JAXBElement<BackupDBMySQL> createBackupDBMySQL(BackupDBMySQL value) {
        return new JAXBElement<BackupDBMySQL>(_BackupDBMySQL_QNAME, BackupDBMySQL.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BackupDBSchemaResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.general.legrain.fr/", name = "backupDBSchemaResponse")
    public JAXBElement<BackupDBSchemaResponse> createBackupDBSchemaResponse(BackupDBSchemaResponse value) {
        return new JAXBElement<BackupDBSchemaResponse>(_BackupDBSchemaResponse_QNAME, BackupDBSchemaResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AjoutDossierResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.general.legrain.fr/", name = "ajoutDossierResponse")
    public JAXBElement<AjoutDossierResponse> createAjoutDossierResponse(AjoutDossierResponse value) {
        return new JAXBElement<AjoutDossierResponse>(_AjoutDossierResponse_QNAME, AjoutDossierResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListeFichierDump }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.general.legrain.fr/", name = "listeFichierDump")
    public JAXBElement<ListeFichierDump> createListeFichierDump(ListeFichierDump value) {
        return new JAXBElement<ListeFichierDump>(_ListeFichierDump_QNAME, ListeFichierDump.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListeFichierDumpResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.general.legrain.fr/", name = "listeFichierDumpResponse")
    public JAXBElement<ListeFichierDumpResponse> createListeFichierDumpResponse(ListeFichierDumpResponse value) {
        return new JAXBElement<ListeFichierDumpResponse>(_ListeFichierDumpResponse_QNAME, ListeFichierDumpResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BackupDBSchemaBase }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.general.legrain.fr/", name = "backupDBSchemaBase")
    public JAXBElement<BackupDBSchemaBase> createBackupDBSchemaBase(BackupDBSchemaBase value) {
        return new JAXBElement<BackupDBSchemaBase>(_BackupDBSchemaBase_QNAME, BackupDBSchemaBase.class, null, value);
    }

}
