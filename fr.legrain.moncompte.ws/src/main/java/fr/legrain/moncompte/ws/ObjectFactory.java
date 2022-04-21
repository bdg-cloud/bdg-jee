
package fr.legrain.moncompte.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the fr.legrain.moncompte.ws package. 
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

    private final static QName _AjouteAutorisation_QNAME = new QName("http://service.moncompte.legrain.fr/", "ajouteAutorisation");
    private final static QName _AjouteAutorisationResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "ajouteAutorisationResponse");
    private final static QName _ChargeDernierSetup_QNAME = new QName("http://service.moncompte.legrain.fr/", "chargeDernierSetup");
    private final static QName _ChargeDernierSetupResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "chargeDernierSetupResponse");
    private final static QName _DossierExiste_QNAME = new QName("http://service.moncompte.legrain.fr/", "dossierExiste");
    private final static QName _DossierExisteResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "dossierExisteResponse");
    private final static QName _EqualsProduitAndVersion_QNAME = new QName("http://service.moncompte.legrain.fr/", "equalsProduitAndVersion");
    private final static QName _EqualsProduitAndVersionResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "equalsProduitAndVersionResponse");
    private final static QName _FindCgPartenaire_QNAME = new QName("http://service.moncompte.legrain.fr/", "findCgPartenaire");
    private final static QName _FindCgPartenaireCourant_QNAME = new QName("http://service.moncompte.legrain.fr/", "findCgPartenaireCourant");
    private final static QName _FindCgPartenaireCourantResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "findCgPartenaireCourantResponse");
    private final static QName _FindCgPartenaireResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "findCgPartenaireResponse");
    private final static QName _FindCgv_QNAME = new QName("http://service.moncompte.legrain.fr/", "findCgv");
    private final static QName _FindCgvCourant_QNAME = new QName("http://service.moncompte.legrain.fr/", "findCgvCourant");
    private final static QName _FindCgvCourantResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "findCgvCourantResponse");
    private final static QName _FindCgvResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "findCgvResponse");
    private final static QName _FindClientDossier_QNAME = new QName("http://service.moncompte.legrain.fr/", "findClientDossier");
    private final static QName _FindClientDossierResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "findClientDossierResponse");
    private final static QName _FindCommissionPartenaire_QNAME = new QName("http://service.moncompte.legrain.fr/", "findCommissionPartenaire");
    private final static QName _FindCommissionPartenaireResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "findCommissionPartenaireResponse");
    private final static QName _FindDecoteTauxCommissionPartenaire_QNAME = new QName("http://service.moncompte.legrain.fr/", "findDecoteTauxCommissionPartenaire");
    private final static QName _FindDecoteTauxCommissionPartenaireResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "findDecoteTauxCommissionPartenaireResponse");
    private final static QName _FindDerniereCreationDossierPayantPartenaire_QNAME = new QName("http://service.moncompte.legrain.fr/", "findDerniereCreationDossierPayantPartenaire");
    private final static QName _FindDerniereCreationDossierPayantPartenaireResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "findDerniereCreationDossierPayantPartenaireResponse");
    private final static QName _FindDossier_QNAME = new QName("http://service.moncompte.legrain.fr/", "findDossier");
    private final static QName _FindDossierResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "findDossierResponse");
    private final static QName _FindLimitationFacture_QNAME = new QName("http://service.moncompte.legrain.fr/", "findLimitationFacture");
    private final static QName _FindLimitationFactureResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "findLimitationFactureResponse");
    private final static QName _FindListeDossierClient_QNAME = new QName("http://service.moncompte.legrain.fr/", "findListeDossierClient");
    private final static QName _FindListeDossierClientResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "findListeDossierClientResponse");
    private final static QName _FindListeDossierProduit_QNAME = new QName("http://service.moncompte.legrain.fr/", "findListeDossierProduit");
    private final static QName _FindListeDossierProduitResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "findListeDossierProduitResponse");
    private final static QName _FindMontantVentePartenaire_QNAME = new QName("http://service.moncompte.legrain.fr/", "findMontantVentePartenaire");
    private final static QName _FindMontantVentePartenaireResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "findMontantVentePartenaireResponse");
    private final static QName _FindPanier_QNAME = new QName("http://service.moncompte.legrain.fr/", "findPanier");
    private final static QName _FindPanierClient_QNAME = new QName("http://service.moncompte.legrain.fr/", "findPanierClient");
    private final static QName _FindPanierClientResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "findPanierClientResponse");
    private final static QName _FindPanierDossier_QNAME = new QName("http://service.moncompte.legrain.fr/", "findPanierDossier");
    private final static QName _FindPanierDossierResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "findPanierDossierResponse");
    private final static QName _FindPanierResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "findPanierResponse");
    private final static QName _FindPartenaire_QNAME = new QName("http://service.moncompte.legrain.fr/", "findPartenaire");
    private final static QName _FindPartenaireByCode_QNAME = new QName("http://service.moncompte.legrain.fr/", "findPartenaireByCode");
    private final static QName _FindPartenaireByCodeResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "findPartenaireByCodeResponse");
    private final static QName _FindPartenaireResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "findPartenaireResponse");
    private final static QName _FindPrixParUtilisateur_QNAME = new QName("http://service.moncompte.legrain.fr/", "findPrixParUtilisateur");
    private final static QName _FindPrixParUtilisateurResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "findPrixParUtilisateurResponse");
    private final static QName _FindTauxCommissionPartenaireAvecDecote_QNAME = new QName("http://service.moncompte.legrain.fr/", "findTauxCommissionPartenaireAvecDecote");
    private final static QName _FindTauxCommissionPartenaireAvecDecoteResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "findTauxCommissionPartenaireAvecDecoteResponse");
    private final static QName _FindTauxCommissionPartenaireSansDecote_QNAME = new QName("http://service.moncompte.legrain.fr/", "findTauxCommissionPartenaireSansDecote");
    private final static QName _FindTauxCommissionPartenaireSansDecoteResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "findTauxCommissionPartenaireSansDecoteResponse");
    private final static QName _FindTypePaiement_QNAME = new QName("http://service.moncompte.legrain.fr/", "findTypePaiement");
    private final static QName _FindTypePaiementResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "findTypePaiementResponse");
    private final static QName _FindTypePartenaire_QNAME = new QName("http://service.moncompte.legrain.fr/", "findTypePartenaire");
    private final static QName _FindTypePartenaireResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "findTypePartenaireResponse");
    private final static QName _GenereCodeClient_QNAME = new QName("http://service.moncompte.legrain.fr/", "genereCodeClient");
    private final static QName _GenereCodeClientResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "genereCodeClientResponse");
    private final static QName _GenereCommission_QNAME = new QName("http://service.moncompte.legrain.fr/", "genereCommission");
    private final static QName _GenereCommissionResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "genereCommissionResponse");
    private final static QName _MajAutorisation_QNAME = new QName("http://service.moncompte.legrain.fr/", "majAutorisation");
    private final static QName _MajAutorisationResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "majAutorisationResponse");
    private final static QName _MergeClient_QNAME = new QName("http://service.moncompte.legrain.fr/", "mergeClient");
    private final static QName _MergeClientResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "mergeClientResponse");
    private final static QName _MergeDossier_QNAME = new QName("http://service.moncompte.legrain.fr/", "mergeDossier");
    private final static QName _MergeDossierResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "mergeDossierResponse");
    private final static QName _MergePanier_QNAME = new QName("http://service.moncompte.legrain.fr/", "mergePanier");
    private final static QName _MergePanierResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "mergePanierResponse");
    private final static QName _SelectAllCategoriePro_QNAME = new QName("http://service.moncompte.legrain.fr/", "selectAllCategoriePro");
    private final static QName _SelectAllCategorieProResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "selectAllCategorieProResponse");
    private final static QName _SelectAllProduit_QNAME = new QName("http://service.moncompte.legrain.fr/", "selectAllProduit");
    private final static QName _SelectAllProduitCategoriePro_QNAME = new QName("http://service.moncompte.legrain.fr/", "selectAllProduitCategoriePro");
    private final static QName _SelectAllProduitCategorieProResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "selectAllProduitCategorieProResponse");
    private final static QName _SelectAllProduitResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "selectAllProduitResponse");
    private final static QName _SelectAllTNiveau_QNAME = new QName("http://service.moncompte.legrain.fr/", "selectAllTNiveau");
    private final static QName _SelectAllTNiveauResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "selectAllTNiveauResponse");
    private final static QName _UtilisateurExiste_QNAME = new QName("http://service.moncompte.legrain.fr/", "utilisateurExiste");
    private final static QName _UtilisateurExisteResponse_QNAME = new QName("http://service.moncompte.legrain.fr/", "utilisateurExisteResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: fr.legrain.moncompte.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AjouteAutorisation }
     * 
     */
    public AjouteAutorisation createAjouteAutorisation() {
        return new AjouteAutorisation();
    }

    /**
     * Create an instance of {@link AjouteAutorisationResponse }
     * 
     */
    public AjouteAutorisationResponse createAjouteAutorisationResponse() {
        return new AjouteAutorisationResponse();
    }

    /**
     * Create an instance of {@link ChargeDernierSetup }
     * 
     */
    public ChargeDernierSetup createChargeDernierSetup() {
        return new ChargeDernierSetup();
    }

    /**
     * Create an instance of {@link ChargeDernierSetupResponse }
     * 
     */
    public ChargeDernierSetupResponse createChargeDernierSetupResponse() {
        return new ChargeDernierSetupResponse();
    }

    /**
     * Create an instance of {@link DossierExiste }
     * 
     */
    public DossierExiste createDossierExiste() {
        return new DossierExiste();
    }

    /**
     * Create an instance of {@link DossierExisteResponse }
     * 
     */
    public DossierExisteResponse createDossierExisteResponse() {
        return new DossierExisteResponse();
    }

    /**
     * Create an instance of {@link EqualsProduitAndVersion }
     * 
     */
    public EqualsProduitAndVersion createEqualsProduitAndVersion() {
        return new EqualsProduitAndVersion();
    }

    /**
     * Create an instance of {@link EqualsProduitAndVersionResponse }
     * 
     */
    public EqualsProduitAndVersionResponse createEqualsProduitAndVersionResponse() {
        return new EqualsProduitAndVersionResponse();
    }

    /**
     * Create an instance of {@link FindCgPartenaire }
     * 
     */
    public FindCgPartenaire createFindCgPartenaire() {
        return new FindCgPartenaire();
    }

    /**
     * Create an instance of {@link FindCgPartenaireCourant }
     * 
     */
    public FindCgPartenaireCourant createFindCgPartenaireCourant() {
        return new FindCgPartenaireCourant();
    }

    /**
     * Create an instance of {@link FindCgPartenaireCourantResponse }
     * 
     */
    public FindCgPartenaireCourantResponse createFindCgPartenaireCourantResponse() {
        return new FindCgPartenaireCourantResponse();
    }

    /**
     * Create an instance of {@link FindCgPartenaireResponse }
     * 
     */
    public FindCgPartenaireResponse createFindCgPartenaireResponse() {
        return new FindCgPartenaireResponse();
    }

    /**
     * Create an instance of {@link FindCgv }
     * 
     */
    public FindCgv createFindCgv() {
        return new FindCgv();
    }

    /**
     * Create an instance of {@link FindCgvCourant }
     * 
     */
    public FindCgvCourant createFindCgvCourant() {
        return new FindCgvCourant();
    }

    /**
     * Create an instance of {@link FindCgvCourantResponse }
     * 
     */
    public FindCgvCourantResponse createFindCgvCourantResponse() {
        return new FindCgvCourantResponse();
    }

    /**
     * Create an instance of {@link FindCgvResponse }
     * 
     */
    public FindCgvResponse createFindCgvResponse() {
        return new FindCgvResponse();
    }

    /**
     * Create an instance of {@link FindClientDossier }
     * 
     */
    public FindClientDossier createFindClientDossier() {
        return new FindClientDossier();
    }

    /**
     * Create an instance of {@link FindClientDossierResponse }
     * 
     */
    public FindClientDossierResponse createFindClientDossierResponse() {
        return new FindClientDossierResponse();
    }

    /**
     * Create an instance of {@link FindCommissionPartenaire }
     * 
     */
    public FindCommissionPartenaire createFindCommissionPartenaire() {
        return new FindCommissionPartenaire();
    }

    /**
     * Create an instance of {@link FindCommissionPartenaireResponse }
     * 
     */
    public FindCommissionPartenaireResponse createFindCommissionPartenaireResponse() {
        return new FindCommissionPartenaireResponse();
    }

    /**
     * Create an instance of {@link FindDecoteTauxCommissionPartenaire }
     * 
     */
    public FindDecoteTauxCommissionPartenaire createFindDecoteTauxCommissionPartenaire() {
        return new FindDecoteTauxCommissionPartenaire();
    }

    /**
     * Create an instance of {@link FindDecoteTauxCommissionPartenaireResponse }
     * 
     */
    public FindDecoteTauxCommissionPartenaireResponse createFindDecoteTauxCommissionPartenaireResponse() {
        return new FindDecoteTauxCommissionPartenaireResponse();
    }

    /**
     * Create an instance of {@link FindDerniereCreationDossierPayantPartenaire }
     * 
     */
    public FindDerniereCreationDossierPayantPartenaire createFindDerniereCreationDossierPayantPartenaire() {
        return new FindDerniereCreationDossierPayantPartenaire();
    }

    /**
     * Create an instance of {@link FindDerniereCreationDossierPayantPartenaireResponse }
     * 
     */
    public FindDerniereCreationDossierPayantPartenaireResponse createFindDerniereCreationDossierPayantPartenaireResponse() {
        return new FindDerniereCreationDossierPayantPartenaireResponse();
    }

    /**
     * Create an instance of {@link FindDossier }
     * 
     */
    public FindDossier createFindDossier() {
        return new FindDossier();
    }

    /**
     * Create an instance of {@link FindDossierResponse }
     * 
     */
    public FindDossierResponse createFindDossierResponse() {
        return new FindDossierResponse();
    }

    /**
     * Create an instance of {@link FindLimitationFacture }
     * 
     */
    public FindLimitationFacture createFindLimitationFacture() {
        return new FindLimitationFacture();
    }

    /**
     * Create an instance of {@link FindLimitationFactureResponse }
     * 
     */
    public FindLimitationFactureResponse createFindLimitationFactureResponse() {
        return new FindLimitationFactureResponse();
    }

    /**
     * Create an instance of {@link FindListeDossierClient }
     * 
     */
    public FindListeDossierClient createFindListeDossierClient() {
        return new FindListeDossierClient();
    }

    /**
     * Create an instance of {@link FindListeDossierClientResponse }
     * 
     */
    public FindListeDossierClientResponse createFindListeDossierClientResponse() {
        return new FindListeDossierClientResponse();
    }

    /**
     * Create an instance of {@link FindListeDossierProduit }
     * 
     */
    public FindListeDossierProduit createFindListeDossierProduit() {
        return new FindListeDossierProduit();
    }

    /**
     * Create an instance of {@link FindListeDossierProduitResponse }
     * 
     */
    public FindListeDossierProduitResponse createFindListeDossierProduitResponse() {
        return new FindListeDossierProduitResponse();
    }

    /**
     * Create an instance of {@link FindMontantVentePartenaire }
     * 
     */
    public FindMontantVentePartenaire createFindMontantVentePartenaire() {
        return new FindMontantVentePartenaire();
    }

    /**
     * Create an instance of {@link FindMontantVentePartenaireResponse }
     * 
     */
    public FindMontantVentePartenaireResponse createFindMontantVentePartenaireResponse() {
        return new FindMontantVentePartenaireResponse();
    }

    /**
     * Create an instance of {@link FindPanier }
     * 
     */
    public FindPanier createFindPanier() {
        return new FindPanier();
    }

    /**
     * Create an instance of {@link FindPanierClient }
     * 
     */
    public FindPanierClient createFindPanierClient() {
        return new FindPanierClient();
    }

    /**
     * Create an instance of {@link FindPanierClientResponse }
     * 
     */
    public FindPanierClientResponse createFindPanierClientResponse() {
        return new FindPanierClientResponse();
    }

    /**
     * Create an instance of {@link FindPanierDossier }
     * 
     */
    public FindPanierDossier createFindPanierDossier() {
        return new FindPanierDossier();
    }

    /**
     * Create an instance of {@link FindPanierDossierResponse }
     * 
     */
    public FindPanierDossierResponse createFindPanierDossierResponse() {
        return new FindPanierDossierResponse();
    }

    /**
     * Create an instance of {@link FindPanierResponse }
     * 
     */
    public FindPanierResponse createFindPanierResponse() {
        return new FindPanierResponse();
    }

    /**
     * Create an instance of {@link FindPartenaire }
     * 
     */
    public FindPartenaire createFindPartenaire() {
        return new FindPartenaire();
    }

    /**
     * Create an instance of {@link FindPartenaireByCode }
     * 
     */
    public FindPartenaireByCode createFindPartenaireByCode() {
        return new FindPartenaireByCode();
    }

    /**
     * Create an instance of {@link FindPartenaireByCodeResponse }
     * 
     */
    public FindPartenaireByCodeResponse createFindPartenaireByCodeResponse() {
        return new FindPartenaireByCodeResponse();
    }

    /**
     * Create an instance of {@link FindPartenaireResponse }
     * 
     */
    public FindPartenaireResponse createFindPartenaireResponse() {
        return new FindPartenaireResponse();
    }

    /**
     * Create an instance of {@link FindPrixParUtilisateur }
     * 
     */
    public FindPrixParUtilisateur createFindPrixParUtilisateur() {
        return new FindPrixParUtilisateur();
    }

    /**
     * Create an instance of {@link FindPrixParUtilisateurResponse }
     * 
     */
    public FindPrixParUtilisateurResponse createFindPrixParUtilisateurResponse() {
        return new FindPrixParUtilisateurResponse();
    }

    /**
     * Create an instance of {@link FindTauxCommissionPartenaireAvecDecote }
     * 
     */
    public FindTauxCommissionPartenaireAvecDecote createFindTauxCommissionPartenaireAvecDecote() {
        return new FindTauxCommissionPartenaireAvecDecote();
    }

    /**
     * Create an instance of {@link FindTauxCommissionPartenaireAvecDecoteResponse }
     * 
     */
    public FindTauxCommissionPartenaireAvecDecoteResponse createFindTauxCommissionPartenaireAvecDecoteResponse() {
        return new FindTauxCommissionPartenaireAvecDecoteResponse();
    }

    /**
     * Create an instance of {@link FindTauxCommissionPartenaireSansDecote }
     * 
     */
    public FindTauxCommissionPartenaireSansDecote createFindTauxCommissionPartenaireSansDecote() {
        return new FindTauxCommissionPartenaireSansDecote();
    }

    /**
     * Create an instance of {@link FindTauxCommissionPartenaireSansDecoteResponse }
     * 
     */
    public FindTauxCommissionPartenaireSansDecoteResponse createFindTauxCommissionPartenaireSansDecoteResponse() {
        return new FindTauxCommissionPartenaireSansDecoteResponse();
    }

    /**
     * Create an instance of {@link FindTypePaiement }
     * 
     */
    public FindTypePaiement createFindTypePaiement() {
        return new FindTypePaiement();
    }

    /**
     * Create an instance of {@link FindTypePaiementResponse }
     * 
     */
    public FindTypePaiementResponse createFindTypePaiementResponse() {
        return new FindTypePaiementResponse();
    }

    /**
     * Create an instance of {@link FindTypePartenaire }
     * 
     */
    public FindTypePartenaire createFindTypePartenaire() {
        return new FindTypePartenaire();
    }

    /**
     * Create an instance of {@link FindTypePartenaireResponse }
     * 
     */
    public FindTypePartenaireResponse createFindTypePartenaireResponse() {
        return new FindTypePartenaireResponse();
    }

    /**
     * Create an instance of {@link GenereCodeClient }
     * 
     */
    public GenereCodeClient createGenereCodeClient() {
        return new GenereCodeClient();
    }

    /**
     * Create an instance of {@link GenereCodeClientResponse }
     * 
     */
    public GenereCodeClientResponse createGenereCodeClientResponse() {
        return new GenereCodeClientResponse();
    }

    /**
     * Create an instance of {@link GenereCommission }
     * 
     */
    public GenereCommission createGenereCommission() {
        return new GenereCommission();
    }

    /**
     * Create an instance of {@link GenereCommissionResponse }
     * 
     */
    public GenereCommissionResponse createGenereCommissionResponse() {
        return new GenereCommissionResponse();
    }

    /**
     * Create an instance of {@link MajAutorisation }
     * 
     */
    public MajAutorisation createMajAutorisation() {
        return new MajAutorisation();
    }

    /**
     * Create an instance of {@link MajAutorisationResponse }
     * 
     */
    public MajAutorisationResponse createMajAutorisationResponse() {
        return new MajAutorisationResponse();
    }

    /**
     * Create an instance of {@link MergeClient }
     * 
     */
    public MergeClient createMergeClient() {
        return new MergeClient();
    }

    /**
     * Create an instance of {@link MergeClientResponse }
     * 
     */
    public MergeClientResponse createMergeClientResponse() {
        return new MergeClientResponse();
    }

    /**
     * Create an instance of {@link MergeDossier }
     * 
     */
    public MergeDossier createMergeDossier() {
        return new MergeDossier();
    }

    /**
     * Create an instance of {@link MergeDossierResponse }
     * 
     */
    public MergeDossierResponse createMergeDossierResponse() {
        return new MergeDossierResponse();
    }

    /**
     * Create an instance of {@link MergePanier }
     * 
     */
    public MergePanier createMergePanier() {
        return new MergePanier();
    }

    /**
     * Create an instance of {@link MergePanierResponse }
     * 
     */
    public MergePanierResponse createMergePanierResponse() {
        return new MergePanierResponse();
    }

    /**
     * Create an instance of {@link SelectAllCategoriePro }
     * 
     */
    public SelectAllCategoriePro createSelectAllCategoriePro() {
        return new SelectAllCategoriePro();
    }

    /**
     * Create an instance of {@link SelectAllCategorieProResponse }
     * 
     */
    public SelectAllCategorieProResponse createSelectAllCategorieProResponse() {
        return new SelectAllCategorieProResponse();
    }

    /**
     * Create an instance of {@link SelectAllProduit }
     * 
     */
    public SelectAllProduit createSelectAllProduit() {
        return new SelectAllProduit();
    }

    /**
     * Create an instance of {@link SelectAllProduitCategoriePro }
     * 
     */
    public SelectAllProduitCategoriePro createSelectAllProduitCategoriePro() {
        return new SelectAllProduitCategoriePro();
    }

    /**
     * Create an instance of {@link SelectAllProduitCategorieProResponse }
     * 
     */
    public SelectAllProduitCategorieProResponse createSelectAllProduitCategorieProResponse() {
        return new SelectAllProduitCategorieProResponse();
    }

    /**
     * Create an instance of {@link SelectAllProduitResponse }
     * 
     */
    public SelectAllProduitResponse createSelectAllProduitResponse() {
        return new SelectAllProduitResponse();
    }

    /**
     * Create an instance of {@link SelectAllTNiveau }
     * 
     */
    public SelectAllTNiveau createSelectAllTNiveau() {
        return new SelectAllTNiveau();
    }

    /**
     * Create an instance of {@link SelectAllTNiveauResponse }
     * 
     */
    public SelectAllTNiveauResponse createSelectAllTNiveauResponse() {
        return new SelectAllTNiveauResponse();
    }

    /**
     * Create an instance of {@link UtilisateurExiste }
     * 
     */
    public UtilisateurExiste createUtilisateurExiste() {
        return new UtilisateurExiste();
    }

    /**
     * Create an instance of {@link UtilisateurExisteResponse }
     * 
     */
    public UtilisateurExisteResponse createUtilisateurExisteResponse() {
        return new UtilisateurExisteResponse();
    }

    /**
     * Create an instance of {@link TaAdresse }
     * 
     */
    public TaAdresse createTaAdresse() {
        return new TaAdresse();
    }

    /**
     * Create an instance of {@link TaAutorisation }
     * 
     */
    public TaAutorisation createTaAutorisation() {
        return new TaAutorisation();
    }

    /**
     * Create an instance of {@link TaCategoriePro }
     * 
     */
    public TaCategoriePro createTaCategoriePro() {
        return new TaCategoriePro();
    }

    /**
     * Create an instance of {@link TaCgPartenaire }
     * 
     */
    public TaCgPartenaire createTaCgPartenaire() {
        return new TaCgPartenaire();
    }

    /**
     * Create an instance of {@link TaCgv }
     * 
     */
    public TaCgv createTaCgv() {
        return new TaCgv();
    }

    /**
     * Create an instance of {@link TaClient }
     * 
     */
    public TaClient createTaClient() {
        return new TaClient();
    }

    /**
     * Create an instance of {@link TaCommission }
     * 
     */
    public TaCommission createTaCommission() {
        return new TaCommission();
    }

    /**
     * Create an instance of {@link TaDossier }
     * 
     */
    public TaDossier createTaDossier() {
        return new TaDossier();
    }

    /**
     * Create an instance of {@link TaGroupeProduit }
     * 
     */
    public TaGroupeProduit createTaGroupeProduit() {
        return new TaGroupeProduit();
    }

    /**
     * Create an instance of {@link TaLicence }
     * 
     */
    public TaLicence createTaLicence() {
        return new TaLicence();
    }

    /**
     * Create an instance of {@link TaLigneCommission }
     * 
     */
    public TaLigneCommission createTaLigneCommission() {
        return new TaLigneCommission();
    }

    /**
     * Create an instance of {@link TaLignePanier }
     * 
     */
    public TaLignePanier createTaLignePanier() {
        return new TaLignePanier();
    }

    /**
     * Create an instance of {@link TaLimitation }
     * 
     */
    public TaLimitation createTaLimitation() {
        return new TaLimitation();
    }

    /**
     * Create an instance of {@link TaPanier }
     * 
     */
    public TaPanier createTaPanier() {
        return new TaPanier();
    }

    /**
     * Create an instance of {@link TaPartenaire }
     * 
     */
    public TaPartenaire createTaPartenaire() {
        return new TaPartenaire();
    }

    /**
     * Create an instance of {@link TaPrixParUtilisateur }
     * 
     */
    public TaPrixParUtilisateur createTaPrixParUtilisateur() {
        return new TaPrixParUtilisateur();
    }

    /**
     * Create an instance of {@link TaPrixParUtilisateurPerso }
     * 
     */
    public TaPrixParUtilisateurPerso createTaPrixParUtilisateurPerso() {
        return new TaPrixParUtilisateurPerso();
    }

    /**
     * Create an instance of {@link TaPrixPerso }
     * 
     */
    public TaPrixPerso createTaPrixPerso() {
        return new TaPrixPerso();
    }

    /**
     * Create an instance of {@link TaProduit }
     * 
     */
    public TaProduit createTaProduit() {
        return new TaProduit();
    }

    /**
     * Create an instance of {@link TaRRoleUtilisateur }
     * 
     */
    public TaRRoleUtilisateur createTaRRoleUtilisateur() {
        return new TaRRoleUtilisateur();
    }

    /**
     * Create an instance of {@link TaRole }
     * 
     */
    public TaRole createTaRole() {
        return new TaRole();
    }

    /**
     * Create an instance of {@link TaSetup }
     * 
     */
    public TaSetup createTaSetup() {
        return new TaSetup();
    }

    /**
     * Create an instance of {@link TaTNiveau }
     * 
     */
    public TaTNiveau createTaTNiveau() {
        return new TaTNiveau();
    }

    /**
     * Create an instance of {@link TaTypePaiement }
     * 
     */
    public TaTypePaiement createTaTypePaiement() {
        return new TaTypePaiement();
    }

    /**
     * Create an instance of {@link TaTypePartenaire }
     * 
     */
    public TaTypePartenaire createTaTypePartenaire() {
        return new TaTypePartenaire();
    }

    /**
     * Create an instance of {@link TaTypeProduit }
     * 
     */
    public TaTypeProduit createTaTypeProduit() {
        return new TaTypeProduit();
    }

    /**
     * Create an instance of {@link TaUtilisateur }
     * 
     */
    public TaUtilisateur createTaUtilisateur() {
        return new TaUtilisateur();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AjouteAutorisation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "ajouteAutorisation")
    public JAXBElement<AjouteAutorisation> createAjouteAutorisation(AjouteAutorisation value) {
        return new JAXBElement<AjouteAutorisation>(_AjouteAutorisation_QNAME, AjouteAutorisation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AjouteAutorisationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "ajouteAutorisationResponse")
    public JAXBElement<AjouteAutorisationResponse> createAjouteAutorisationResponse(AjouteAutorisationResponse value) {
        return new JAXBElement<AjouteAutorisationResponse>(_AjouteAutorisationResponse_QNAME, AjouteAutorisationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ChargeDernierSetup }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "chargeDernierSetup")
    public JAXBElement<ChargeDernierSetup> createChargeDernierSetup(ChargeDernierSetup value) {
        return new JAXBElement<ChargeDernierSetup>(_ChargeDernierSetup_QNAME, ChargeDernierSetup.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ChargeDernierSetupResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "chargeDernierSetupResponse")
    public JAXBElement<ChargeDernierSetupResponse> createChargeDernierSetupResponse(ChargeDernierSetupResponse value) {
        return new JAXBElement<ChargeDernierSetupResponse>(_ChargeDernierSetupResponse_QNAME, ChargeDernierSetupResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DossierExiste }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "dossierExiste")
    public JAXBElement<DossierExiste> createDossierExiste(DossierExiste value) {
        return new JAXBElement<DossierExiste>(_DossierExiste_QNAME, DossierExiste.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DossierExisteResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "dossierExisteResponse")
    public JAXBElement<DossierExisteResponse> createDossierExisteResponse(DossierExisteResponse value) {
        return new JAXBElement<DossierExisteResponse>(_DossierExisteResponse_QNAME, DossierExisteResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EqualsProduitAndVersion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "equalsProduitAndVersion")
    public JAXBElement<EqualsProduitAndVersion> createEqualsProduitAndVersion(EqualsProduitAndVersion value) {
        return new JAXBElement<EqualsProduitAndVersion>(_EqualsProduitAndVersion_QNAME, EqualsProduitAndVersion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EqualsProduitAndVersionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "equalsProduitAndVersionResponse")
    public JAXBElement<EqualsProduitAndVersionResponse> createEqualsProduitAndVersionResponse(EqualsProduitAndVersionResponse value) {
        return new JAXBElement<EqualsProduitAndVersionResponse>(_EqualsProduitAndVersionResponse_QNAME, EqualsProduitAndVersionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindCgPartenaire }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findCgPartenaire")
    public JAXBElement<FindCgPartenaire> createFindCgPartenaire(FindCgPartenaire value) {
        return new JAXBElement<FindCgPartenaire>(_FindCgPartenaire_QNAME, FindCgPartenaire.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindCgPartenaireCourant }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findCgPartenaireCourant")
    public JAXBElement<FindCgPartenaireCourant> createFindCgPartenaireCourant(FindCgPartenaireCourant value) {
        return new JAXBElement<FindCgPartenaireCourant>(_FindCgPartenaireCourant_QNAME, FindCgPartenaireCourant.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindCgPartenaireCourantResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findCgPartenaireCourantResponse")
    public JAXBElement<FindCgPartenaireCourantResponse> createFindCgPartenaireCourantResponse(FindCgPartenaireCourantResponse value) {
        return new JAXBElement<FindCgPartenaireCourantResponse>(_FindCgPartenaireCourantResponse_QNAME, FindCgPartenaireCourantResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindCgPartenaireResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findCgPartenaireResponse")
    public JAXBElement<FindCgPartenaireResponse> createFindCgPartenaireResponse(FindCgPartenaireResponse value) {
        return new JAXBElement<FindCgPartenaireResponse>(_FindCgPartenaireResponse_QNAME, FindCgPartenaireResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindCgv }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findCgv")
    public JAXBElement<FindCgv> createFindCgv(FindCgv value) {
        return new JAXBElement<FindCgv>(_FindCgv_QNAME, FindCgv.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindCgvCourant }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findCgvCourant")
    public JAXBElement<FindCgvCourant> createFindCgvCourant(FindCgvCourant value) {
        return new JAXBElement<FindCgvCourant>(_FindCgvCourant_QNAME, FindCgvCourant.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindCgvCourantResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findCgvCourantResponse")
    public JAXBElement<FindCgvCourantResponse> createFindCgvCourantResponse(FindCgvCourantResponse value) {
        return new JAXBElement<FindCgvCourantResponse>(_FindCgvCourantResponse_QNAME, FindCgvCourantResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindCgvResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findCgvResponse")
    public JAXBElement<FindCgvResponse> createFindCgvResponse(FindCgvResponse value) {
        return new JAXBElement<FindCgvResponse>(_FindCgvResponse_QNAME, FindCgvResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindClientDossier }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findClientDossier")
    public JAXBElement<FindClientDossier> createFindClientDossier(FindClientDossier value) {
        return new JAXBElement<FindClientDossier>(_FindClientDossier_QNAME, FindClientDossier.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindClientDossierResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findClientDossierResponse")
    public JAXBElement<FindClientDossierResponse> createFindClientDossierResponse(FindClientDossierResponse value) {
        return new JAXBElement<FindClientDossierResponse>(_FindClientDossierResponse_QNAME, FindClientDossierResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindCommissionPartenaire }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findCommissionPartenaire")
    public JAXBElement<FindCommissionPartenaire> createFindCommissionPartenaire(FindCommissionPartenaire value) {
        return new JAXBElement<FindCommissionPartenaire>(_FindCommissionPartenaire_QNAME, FindCommissionPartenaire.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindCommissionPartenaireResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findCommissionPartenaireResponse")
    public JAXBElement<FindCommissionPartenaireResponse> createFindCommissionPartenaireResponse(FindCommissionPartenaireResponse value) {
        return new JAXBElement<FindCommissionPartenaireResponse>(_FindCommissionPartenaireResponse_QNAME, FindCommissionPartenaireResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindDecoteTauxCommissionPartenaire }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findDecoteTauxCommissionPartenaire")
    public JAXBElement<FindDecoteTauxCommissionPartenaire> createFindDecoteTauxCommissionPartenaire(FindDecoteTauxCommissionPartenaire value) {
        return new JAXBElement<FindDecoteTauxCommissionPartenaire>(_FindDecoteTauxCommissionPartenaire_QNAME, FindDecoteTauxCommissionPartenaire.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindDecoteTauxCommissionPartenaireResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findDecoteTauxCommissionPartenaireResponse")
    public JAXBElement<FindDecoteTauxCommissionPartenaireResponse> createFindDecoteTauxCommissionPartenaireResponse(FindDecoteTauxCommissionPartenaireResponse value) {
        return new JAXBElement<FindDecoteTauxCommissionPartenaireResponse>(_FindDecoteTauxCommissionPartenaireResponse_QNAME, FindDecoteTauxCommissionPartenaireResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindDerniereCreationDossierPayantPartenaire }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findDerniereCreationDossierPayantPartenaire")
    public JAXBElement<FindDerniereCreationDossierPayantPartenaire> createFindDerniereCreationDossierPayantPartenaire(FindDerniereCreationDossierPayantPartenaire value) {
        return new JAXBElement<FindDerniereCreationDossierPayantPartenaire>(_FindDerniereCreationDossierPayantPartenaire_QNAME, FindDerniereCreationDossierPayantPartenaire.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindDerniereCreationDossierPayantPartenaireResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findDerniereCreationDossierPayantPartenaireResponse")
    public JAXBElement<FindDerniereCreationDossierPayantPartenaireResponse> createFindDerniereCreationDossierPayantPartenaireResponse(FindDerniereCreationDossierPayantPartenaireResponse value) {
        return new JAXBElement<FindDerniereCreationDossierPayantPartenaireResponse>(_FindDerniereCreationDossierPayantPartenaireResponse_QNAME, FindDerniereCreationDossierPayantPartenaireResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindDossier }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findDossier")
    public JAXBElement<FindDossier> createFindDossier(FindDossier value) {
        return new JAXBElement<FindDossier>(_FindDossier_QNAME, FindDossier.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindDossierResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findDossierResponse")
    public JAXBElement<FindDossierResponse> createFindDossierResponse(FindDossierResponse value) {
        return new JAXBElement<FindDossierResponse>(_FindDossierResponse_QNAME, FindDossierResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindLimitationFacture }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findLimitationFacture")
    public JAXBElement<FindLimitationFacture> createFindLimitationFacture(FindLimitationFacture value) {
        return new JAXBElement<FindLimitationFacture>(_FindLimitationFacture_QNAME, FindLimitationFacture.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindLimitationFactureResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findLimitationFactureResponse")
    public JAXBElement<FindLimitationFactureResponse> createFindLimitationFactureResponse(FindLimitationFactureResponse value) {
        return new JAXBElement<FindLimitationFactureResponse>(_FindLimitationFactureResponse_QNAME, FindLimitationFactureResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindListeDossierClient }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findListeDossierClient")
    public JAXBElement<FindListeDossierClient> createFindListeDossierClient(FindListeDossierClient value) {
        return new JAXBElement<FindListeDossierClient>(_FindListeDossierClient_QNAME, FindListeDossierClient.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindListeDossierClientResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findListeDossierClientResponse")
    public JAXBElement<FindListeDossierClientResponse> createFindListeDossierClientResponse(FindListeDossierClientResponse value) {
        return new JAXBElement<FindListeDossierClientResponse>(_FindListeDossierClientResponse_QNAME, FindListeDossierClientResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindListeDossierProduit }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findListeDossierProduit")
    public JAXBElement<FindListeDossierProduit> createFindListeDossierProduit(FindListeDossierProduit value) {
        return new JAXBElement<FindListeDossierProduit>(_FindListeDossierProduit_QNAME, FindListeDossierProduit.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindListeDossierProduitResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findListeDossierProduitResponse")
    public JAXBElement<FindListeDossierProduitResponse> createFindListeDossierProduitResponse(FindListeDossierProduitResponse value) {
        return new JAXBElement<FindListeDossierProduitResponse>(_FindListeDossierProduitResponse_QNAME, FindListeDossierProduitResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindMontantVentePartenaire }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findMontantVentePartenaire")
    public JAXBElement<FindMontantVentePartenaire> createFindMontantVentePartenaire(FindMontantVentePartenaire value) {
        return new JAXBElement<FindMontantVentePartenaire>(_FindMontantVentePartenaire_QNAME, FindMontantVentePartenaire.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindMontantVentePartenaireResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findMontantVentePartenaireResponse")
    public JAXBElement<FindMontantVentePartenaireResponse> createFindMontantVentePartenaireResponse(FindMontantVentePartenaireResponse value) {
        return new JAXBElement<FindMontantVentePartenaireResponse>(_FindMontantVentePartenaireResponse_QNAME, FindMontantVentePartenaireResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindPanier }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findPanier")
    public JAXBElement<FindPanier> createFindPanier(FindPanier value) {
        return new JAXBElement<FindPanier>(_FindPanier_QNAME, FindPanier.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindPanierClient }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findPanierClient")
    public JAXBElement<FindPanierClient> createFindPanierClient(FindPanierClient value) {
        return new JAXBElement<FindPanierClient>(_FindPanierClient_QNAME, FindPanierClient.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindPanierClientResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findPanierClientResponse")
    public JAXBElement<FindPanierClientResponse> createFindPanierClientResponse(FindPanierClientResponse value) {
        return new JAXBElement<FindPanierClientResponse>(_FindPanierClientResponse_QNAME, FindPanierClientResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindPanierDossier }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findPanierDossier")
    public JAXBElement<FindPanierDossier> createFindPanierDossier(FindPanierDossier value) {
        return new JAXBElement<FindPanierDossier>(_FindPanierDossier_QNAME, FindPanierDossier.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindPanierDossierResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findPanierDossierResponse")
    public JAXBElement<FindPanierDossierResponse> createFindPanierDossierResponse(FindPanierDossierResponse value) {
        return new JAXBElement<FindPanierDossierResponse>(_FindPanierDossierResponse_QNAME, FindPanierDossierResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindPanierResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findPanierResponse")
    public JAXBElement<FindPanierResponse> createFindPanierResponse(FindPanierResponse value) {
        return new JAXBElement<FindPanierResponse>(_FindPanierResponse_QNAME, FindPanierResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindPartenaire }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findPartenaire")
    public JAXBElement<FindPartenaire> createFindPartenaire(FindPartenaire value) {
        return new JAXBElement<FindPartenaire>(_FindPartenaire_QNAME, FindPartenaire.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindPartenaireByCode }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findPartenaireByCode")
    public JAXBElement<FindPartenaireByCode> createFindPartenaireByCode(FindPartenaireByCode value) {
        return new JAXBElement<FindPartenaireByCode>(_FindPartenaireByCode_QNAME, FindPartenaireByCode.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindPartenaireByCodeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findPartenaireByCodeResponse")
    public JAXBElement<FindPartenaireByCodeResponse> createFindPartenaireByCodeResponse(FindPartenaireByCodeResponse value) {
        return new JAXBElement<FindPartenaireByCodeResponse>(_FindPartenaireByCodeResponse_QNAME, FindPartenaireByCodeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindPartenaireResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findPartenaireResponse")
    public JAXBElement<FindPartenaireResponse> createFindPartenaireResponse(FindPartenaireResponse value) {
        return new JAXBElement<FindPartenaireResponse>(_FindPartenaireResponse_QNAME, FindPartenaireResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindPrixParUtilisateur }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findPrixParUtilisateur")
    public JAXBElement<FindPrixParUtilisateur> createFindPrixParUtilisateur(FindPrixParUtilisateur value) {
        return new JAXBElement<FindPrixParUtilisateur>(_FindPrixParUtilisateur_QNAME, FindPrixParUtilisateur.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindPrixParUtilisateurResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findPrixParUtilisateurResponse")
    public JAXBElement<FindPrixParUtilisateurResponse> createFindPrixParUtilisateurResponse(FindPrixParUtilisateurResponse value) {
        return new JAXBElement<FindPrixParUtilisateurResponse>(_FindPrixParUtilisateurResponse_QNAME, FindPrixParUtilisateurResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindTauxCommissionPartenaireAvecDecote }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findTauxCommissionPartenaireAvecDecote")
    public JAXBElement<FindTauxCommissionPartenaireAvecDecote> createFindTauxCommissionPartenaireAvecDecote(FindTauxCommissionPartenaireAvecDecote value) {
        return new JAXBElement<FindTauxCommissionPartenaireAvecDecote>(_FindTauxCommissionPartenaireAvecDecote_QNAME, FindTauxCommissionPartenaireAvecDecote.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindTauxCommissionPartenaireAvecDecoteResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findTauxCommissionPartenaireAvecDecoteResponse")
    public JAXBElement<FindTauxCommissionPartenaireAvecDecoteResponse> createFindTauxCommissionPartenaireAvecDecoteResponse(FindTauxCommissionPartenaireAvecDecoteResponse value) {
        return new JAXBElement<FindTauxCommissionPartenaireAvecDecoteResponse>(_FindTauxCommissionPartenaireAvecDecoteResponse_QNAME, FindTauxCommissionPartenaireAvecDecoteResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindTauxCommissionPartenaireSansDecote }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findTauxCommissionPartenaireSansDecote")
    public JAXBElement<FindTauxCommissionPartenaireSansDecote> createFindTauxCommissionPartenaireSansDecote(FindTauxCommissionPartenaireSansDecote value) {
        return new JAXBElement<FindTauxCommissionPartenaireSansDecote>(_FindTauxCommissionPartenaireSansDecote_QNAME, FindTauxCommissionPartenaireSansDecote.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindTauxCommissionPartenaireSansDecoteResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findTauxCommissionPartenaireSansDecoteResponse")
    public JAXBElement<FindTauxCommissionPartenaireSansDecoteResponse> createFindTauxCommissionPartenaireSansDecoteResponse(FindTauxCommissionPartenaireSansDecoteResponse value) {
        return new JAXBElement<FindTauxCommissionPartenaireSansDecoteResponse>(_FindTauxCommissionPartenaireSansDecoteResponse_QNAME, FindTauxCommissionPartenaireSansDecoteResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindTypePaiement }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findTypePaiement")
    public JAXBElement<FindTypePaiement> createFindTypePaiement(FindTypePaiement value) {
        return new JAXBElement<FindTypePaiement>(_FindTypePaiement_QNAME, FindTypePaiement.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindTypePaiementResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findTypePaiementResponse")
    public JAXBElement<FindTypePaiementResponse> createFindTypePaiementResponse(FindTypePaiementResponse value) {
        return new JAXBElement<FindTypePaiementResponse>(_FindTypePaiementResponse_QNAME, FindTypePaiementResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindTypePartenaire }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findTypePartenaire")
    public JAXBElement<FindTypePartenaire> createFindTypePartenaire(FindTypePartenaire value) {
        return new JAXBElement<FindTypePartenaire>(_FindTypePartenaire_QNAME, FindTypePartenaire.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindTypePartenaireResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "findTypePartenaireResponse")
    public JAXBElement<FindTypePartenaireResponse> createFindTypePartenaireResponse(FindTypePartenaireResponse value) {
        return new JAXBElement<FindTypePartenaireResponse>(_FindTypePartenaireResponse_QNAME, FindTypePartenaireResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenereCodeClient }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "genereCodeClient")
    public JAXBElement<GenereCodeClient> createGenereCodeClient(GenereCodeClient value) {
        return new JAXBElement<GenereCodeClient>(_GenereCodeClient_QNAME, GenereCodeClient.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenereCodeClientResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "genereCodeClientResponse")
    public JAXBElement<GenereCodeClientResponse> createGenereCodeClientResponse(GenereCodeClientResponse value) {
        return new JAXBElement<GenereCodeClientResponse>(_GenereCodeClientResponse_QNAME, GenereCodeClientResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenereCommission }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "genereCommission")
    public JAXBElement<GenereCommission> createGenereCommission(GenereCommission value) {
        return new JAXBElement<GenereCommission>(_GenereCommission_QNAME, GenereCommission.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenereCommissionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "genereCommissionResponse")
    public JAXBElement<GenereCommissionResponse> createGenereCommissionResponse(GenereCommissionResponse value) {
        return new JAXBElement<GenereCommissionResponse>(_GenereCommissionResponse_QNAME, GenereCommissionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MajAutorisation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "majAutorisation")
    public JAXBElement<MajAutorisation> createMajAutorisation(MajAutorisation value) {
        return new JAXBElement<MajAutorisation>(_MajAutorisation_QNAME, MajAutorisation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MajAutorisationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "majAutorisationResponse")
    public JAXBElement<MajAutorisationResponse> createMajAutorisationResponse(MajAutorisationResponse value) {
        return new JAXBElement<MajAutorisationResponse>(_MajAutorisationResponse_QNAME, MajAutorisationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MergeClient }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "mergeClient")
    public JAXBElement<MergeClient> createMergeClient(MergeClient value) {
        return new JAXBElement<MergeClient>(_MergeClient_QNAME, MergeClient.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MergeClientResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "mergeClientResponse")
    public JAXBElement<MergeClientResponse> createMergeClientResponse(MergeClientResponse value) {
        return new JAXBElement<MergeClientResponse>(_MergeClientResponse_QNAME, MergeClientResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MergeDossier }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "mergeDossier")
    public JAXBElement<MergeDossier> createMergeDossier(MergeDossier value) {
        return new JAXBElement<MergeDossier>(_MergeDossier_QNAME, MergeDossier.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MergeDossierResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "mergeDossierResponse")
    public JAXBElement<MergeDossierResponse> createMergeDossierResponse(MergeDossierResponse value) {
        return new JAXBElement<MergeDossierResponse>(_MergeDossierResponse_QNAME, MergeDossierResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MergePanier }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "mergePanier")
    public JAXBElement<MergePanier> createMergePanier(MergePanier value) {
        return new JAXBElement<MergePanier>(_MergePanier_QNAME, MergePanier.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MergePanierResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "mergePanierResponse")
    public JAXBElement<MergePanierResponse> createMergePanierResponse(MergePanierResponse value) {
        return new JAXBElement<MergePanierResponse>(_MergePanierResponse_QNAME, MergePanierResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SelectAllCategoriePro }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "selectAllCategoriePro")
    public JAXBElement<SelectAllCategoriePro> createSelectAllCategoriePro(SelectAllCategoriePro value) {
        return new JAXBElement<SelectAllCategoriePro>(_SelectAllCategoriePro_QNAME, SelectAllCategoriePro.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SelectAllCategorieProResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "selectAllCategorieProResponse")
    public JAXBElement<SelectAllCategorieProResponse> createSelectAllCategorieProResponse(SelectAllCategorieProResponse value) {
        return new JAXBElement<SelectAllCategorieProResponse>(_SelectAllCategorieProResponse_QNAME, SelectAllCategorieProResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SelectAllProduit }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "selectAllProduit")
    public JAXBElement<SelectAllProduit> createSelectAllProduit(SelectAllProduit value) {
        return new JAXBElement<SelectAllProduit>(_SelectAllProduit_QNAME, SelectAllProduit.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SelectAllProduitCategoriePro }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "selectAllProduitCategoriePro")
    public JAXBElement<SelectAllProduitCategoriePro> createSelectAllProduitCategoriePro(SelectAllProduitCategoriePro value) {
        return new JAXBElement<SelectAllProduitCategoriePro>(_SelectAllProduitCategoriePro_QNAME, SelectAllProduitCategoriePro.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SelectAllProduitCategorieProResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "selectAllProduitCategorieProResponse")
    public JAXBElement<SelectAllProduitCategorieProResponse> createSelectAllProduitCategorieProResponse(SelectAllProduitCategorieProResponse value) {
        return new JAXBElement<SelectAllProduitCategorieProResponse>(_SelectAllProduitCategorieProResponse_QNAME, SelectAllProduitCategorieProResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SelectAllProduitResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "selectAllProduitResponse")
    public JAXBElement<SelectAllProduitResponse> createSelectAllProduitResponse(SelectAllProduitResponse value) {
        return new JAXBElement<SelectAllProduitResponse>(_SelectAllProduitResponse_QNAME, SelectAllProduitResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SelectAllTNiveau }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "selectAllTNiveau")
    public JAXBElement<SelectAllTNiveau> createSelectAllTNiveau(SelectAllTNiveau value) {
        return new JAXBElement<SelectAllTNiveau>(_SelectAllTNiveau_QNAME, SelectAllTNiveau.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SelectAllTNiveauResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "selectAllTNiveauResponse")
    public JAXBElement<SelectAllTNiveauResponse> createSelectAllTNiveauResponse(SelectAllTNiveauResponse value) {
        return new JAXBElement<SelectAllTNiveauResponse>(_SelectAllTNiveauResponse_QNAME, SelectAllTNiveauResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UtilisateurExiste }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "utilisateurExiste")
    public JAXBElement<UtilisateurExiste> createUtilisateurExiste(UtilisateurExiste value) {
        return new JAXBElement<UtilisateurExiste>(_UtilisateurExiste_QNAME, UtilisateurExiste.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UtilisateurExisteResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.moncompte.legrain.fr/", name = "utilisateurExisteResponse")
    public JAXBElement<UtilisateurExisteResponse> createUtilisateurExisteResponse(UtilisateurExisteResponse value) {
        return new JAXBElement<UtilisateurExisteResponse>(_UtilisateurExisteResponse_QNAME, UtilisateurExisteResponse.class, null, value);
    }

}
