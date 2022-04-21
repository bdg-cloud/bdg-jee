
package fr.legrain.bdg.compteclient.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the fr.legrain.bdg.compteclient.ws package. 
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

    private final static QName _ClientExisteChezFournisseur_QNAME = new QName("http://service.bdg.legrain.fr/", "clientExisteChezFournisseur");
    private final static QName _ClientExisteChezFournisseurResponse_QNAME = new QName("http://service.bdg.legrain.fr/", "clientExisteChezFournisseurResponse");
    private final static QName _DevisClientChezFournisseur_QNAME = new QName("http://service.bdg.legrain.fr/", "devisClientChezFournisseur");
    private final static QName _DevisClientChezFournisseurDate_QNAME = new QName("http://service.bdg.legrain.fr/", "devisClientChezFournisseurDate");
    private final static QName _DevisClientChezFournisseurDateResponse_QNAME = new QName("http://service.bdg.legrain.fr/", "devisClientChezFournisseurDateResponse");
    private final static QName _DevisClientChezFournisseurResponse_QNAME = new QName("http://service.bdg.legrain.fr/", "devisClientChezFournisseurResponse");
    private final static QName _FacturesClientChezFournisseur_QNAME = new QName("http://service.bdg.legrain.fr/", "facturesClientChezFournisseur");
    private final static QName _FacturesClientChezFournisseurDate_QNAME = new QName("http://service.bdg.legrain.fr/", "facturesClientChezFournisseurDate");
    private final static QName _FacturesClientChezFournisseurDateResponse_QNAME = new QName("http://service.bdg.legrain.fr/", "facturesClientChezFournisseurDateResponse");
    private final static QName _FacturesClientChezFournisseurResponse_QNAME = new QName("http://service.bdg.legrain.fr/", "facturesClientChezFournisseurResponse");
    private final static QName _FournisseurPermetPaiementParCB_QNAME = new QName("http://service.bdg.legrain.fr/", "fournisseurPermetPaiementParCB");
    private final static QName _FournisseurPermetPaiementParCBResponse_QNAME = new QName("http://service.bdg.legrain.fr/", "fournisseurPermetPaiementParCBResponse");
    private final static QName _InfosClientChezFournisseur_QNAME = new QName("http://service.bdg.legrain.fr/", "infosClientChezFournisseur");
    private final static QName _InfosClientChezFournisseurResponse_QNAME = new QName("http://service.bdg.legrain.fr/", "infosClientChezFournisseurResponse");
    private final static QName _Init_QNAME = new QName("http://service.bdg.legrain.fr/", "init");
    private final static QName _InitResponse_QNAME = new QName("http://service.bdg.legrain.fr/", "initResponse");
    private final static QName _ListeFournisseur_QNAME = new QName("http://service.bdg.legrain.fr/", "listeFournisseur");
    private final static QName _ListeFournisseurResponse_QNAME = new QName("http://service.bdg.legrain.fr/", "listeFournisseurResponse");
    private final static QName _PayerFactureCB_QNAME = new QName("http://service.bdg.legrain.fr/", "payerFactureCB");
    private final static QName _PayerFactureCBResponse_QNAME = new QName("http://service.bdg.legrain.fr/", "payerFactureCBResponse");
    private final static QName _PdfClient_QNAME = new QName("http://service.bdg.legrain.fr/", "pdfClient");
    private final static QName _PdfClientResponse_QNAME = new QName("http://service.bdg.legrain.fr/", "pdfClientResponse");
    private final static QName _PdfFacture_QNAME = new QName("http://service.bdg.legrain.fr/", "pdfFacture");
    private final static QName _PdfFactureResponse_QNAME = new QName("http://service.bdg.legrain.fr/", "pdfFactureResponse");
    private final static QName _EJBException_QNAME = new QName("http://service.bdg.legrain.fr/", "EJBException");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: fr.legrain.bdg.compteclient.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ClientExisteChezFournisseur }
     * 
     */
    public ClientExisteChezFournisseur createClientExisteChezFournisseur() {
        return new ClientExisteChezFournisseur();
    }

    /**
     * Create an instance of {@link ClientExisteChezFournisseurResponse }
     * 
     */
    public ClientExisteChezFournisseurResponse createClientExisteChezFournisseurResponse() {
        return new ClientExisteChezFournisseurResponse();
    }

    /**
     * Create an instance of {@link DevisClientChezFournisseur }
     * 
     */
    public DevisClientChezFournisseur createDevisClientChezFournisseur() {
        return new DevisClientChezFournisseur();
    }

    /**
     * Create an instance of {@link DevisClientChezFournisseurDate }
     * 
     */
    public DevisClientChezFournisseurDate createDevisClientChezFournisseurDate() {
        return new DevisClientChezFournisseurDate();
    }

    /**
     * Create an instance of {@link DevisClientChezFournisseurDateResponse }
     * 
     */
    public DevisClientChezFournisseurDateResponse createDevisClientChezFournisseurDateResponse() {
        return new DevisClientChezFournisseurDateResponse();
    }

    /**
     * Create an instance of {@link DevisClientChezFournisseurResponse }
     * 
     */
    public DevisClientChezFournisseurResponse createDevisClientChezFournisseurResponse() {
        return new DevisClientChezFournisseurResponse();
    }

    /**
     * Create an instance of {@link FacturesClientChezFournisseur }
     * 
     */
    public FacturesClientChezFournisseur createFacturesClientChezFournisseur() {
        return new FacturesClientChezFournisseur();
    }

    /**
     * Create an instance of {@link FacturesClientChezFournisseurDate }
     * 
     */
    public FacturesClientChezFournisseurDate createFacturesClientChezFournisseurDate() {
        return new FacturesClientChezFournisseurDate();
    }

    /**
     * Create an instance of {@link FacturesClientChezFournisseurDateResponse }
     * 
     */
    public FacturesClientChezFournisseurDateResponse createFacturesClientChezFournisseurDateResponse() {
        return new FacturesClientChezFournisseurDateResponse();
    }

    /**
     * Create an instance of {@link FacturesClientChezFournisseurResponse }
     * 
     */
    public FacturesClientChezFournisseurResponse createFacturesClientChezFournisseurResponse() {
        return new FacturesClientChezFournisseurResponse();
    }

    /**
     * Create an instance of {@link FournisseurPermetPaiementParCB }
     * 
     */
    public FournisseurPermetPaiementParCB createFournisseurPermetPaiementParCB() {
        return new FournisseurPermetPaiementParCB();
    }

    /**
     * Create an instance of {@link FournisseurPermetPaiementParCBResponse }
     * 
     */
    public FournisseurPermetPaiementParCBResponse createFournisseurPermetPaiementParCBResponse() {
        return new FournisseurPermetPaiementParCBResponse();
    }

    /**
     * Create an instance of {@link InfosClientChezFournisseur }
     * 
     */
    public InfosClientChezFournisseur createInfosClientChezFournisseur() {
        return new InfosClientChezFournisseur();
    }

    /**
     * Create an instance of {@link InfosClientChezFournisseurResponse }
     * 
     */
    public InfosClientChezFournisseurResponse createInfosClientChezFournisseurResponse() {
        return new InfosClientChezFournisseurResponse();
    }

    /**
     * Create an instance of {@link Init }
     * 
     */
    public Init createInit() {
        return new Init();
    }

    /**
     * Create an instance of {@link InitResponse }
     * 
     */
    public InitResponse createInitResponse() {
        return new InitResponse();
    }

    /**
     * Create an instance of {@link ListeFournisseur }
     * 
     */
    public ListeFournisseur createListeFournisseur() {
        return new ListeFournisseur();
    }

    /**
     * Create an instance of {@link ListeFournisseurResponse }
     * 
     */
    public ListeFournisseurResponse createListeFournisseurResponse() {
        return new ListeFournisseurResponse();
    }

    /**
     * Create an instance of {@link PayerFactureCB }
     * 
     */
    public PayerFactureCB createPayerFactureCB() {
        return new PayerFactureCB();
    }

    /**
     * Create an instance of {@link PayerFactureCBResponse }
     * 
     */
    public PayerFactureCBResponse createPayerFactureCBResponse() {
        return new PayerFactureCBResponse();
    }

    /**
     * Create an instance of {@link PdfClient }
     * 
     */
    public PdfClient createPdfClient() {
        return new PdfClient();
    }

    /**
     * Create an instance of {@link PdfClientResponse }
     * 
     */
    public PdfClientResponse createPdfClientResponse() {
        return new PdfClientResponse();
    }

    /**
     * Create an instance of {@link PdfFacture }
     * 
     */
    public PdfFacture createPdfFacture() {
        return new PdfFacture();
    }

    /**
     * Create an instance of {@link PdfFactureResponse }
     * 
     */
    public PdfFactureResponse createPdfFactureResponse() {
        return new PdfFactureResponse();
    }

    /**
     * Create an instance of {@link EJBException }
     * 
     */
    public EJBException createEJBException() {
        return new EJBException();
    }

    /**
     * Create an instance of {@link TaAbonnement }
     * 
     */
    public TaAbonnement createTaAbonnement() {
        return new TaAbonnement();
    }

    /**
     * Create an instance of {@link TaArticle }
     * 
     */
    public TaArticle createTaArticle() {
        return new TaArticle();
    }

    /**
     * Create an instance of {@link TaCatalogueWeb }
     * 
     */
    public TaCatalogueWeb createTaCatalogueWeb() {
        return new TaCatalogueWeb();
    }

    /**
     * Create an instance of {@link TaCategorieArticle }
     * 
     */
    public TaCategorieArticle createTaCategorieArticle() {
        return new TaCategorieArticle();
    }

    /**
     * Create an instance of {@link TaCodeBarre }
     * 
     */
    public TaCodeBarre createTaCodeBarre() {
        return new TaCodeBarre();
    }

    /**
     * Create an instance of {@link TaEntrepot }
     * 
     */
    public TaEntrepot createTaEntrepot() {
        return new TaEntrepot();
    }

    /**
     * Create an instance of {@link TaFabrication }
     * 
     */
    public TaFabrication createTaFabrication() {
        return new TaFabrication();
    }

    /**
     * Create an instance of {@link TaFamille }
     * 
     */
    public TaFamille createTaFamille() {
        return new TaFamille();
    }

    /**
     * Create an instance of {@link TaFamilleUnite }
     * 
     */
    public TaFamilleUnite createTaFamilleUnite() {
        return new TaFamilleUnite();
    }

    /**
     * Create an instance of {@link TaImageArticle }
     * 
     */
    public TaImageArticle createTaImageArticle() {
        return new TaImageArticle();
    }

    /**
     * Create an instance of {@link TaInfosFabrication }
     * 
     */
    public TaInfosFabrication createTaInfosFabrication() {
        return new TaInfosFabrication();
    }

    /**
     * Create an instance of {@link TaLFabricationMP }
     * 
     */
    public TaLFabricationMP createTaLFabricationMP() {
        return new TaLFabricationMP();
    }

    /**
     * Create an instance of {@link TaLFabricationPF }
     * 
     */
    public TaLFabricationPF createTaLFabricationPF() {
        return new TaLFabricationPF();
    }

    /**
     * Create an instance of {@link TaLRecette }
     * 
     */
    public TaLRecette createTaLRecette() {
        return new TaLRecette();
    }

    /**
     * Create an instance of {@link TaLabelArticle }
     * 
     */
    public TaLabelArticle createTaLabelArticle() {
        return new TaLabelArticle();
    }

    /**
     * Create an instance of {@link TaLot }
     * 
     */
    public TaLot createTaLot() {
        return new TaLot();
    }

    /**
     * Create an instance of {@link TaMarqueArticle }
     * 
     */
    public TaMarqueArticle createTaMarqueArticle() {
        return new TaMarqueArticle();
    }

    /**
     * Create an instance of {@link TaNoteArticle }
     * 
     */
    public TaNoteArticle createTaNoteArticle() {
        return new TaNoteArticle();
    }

    /**
     * Create an instance of {@link TaPrix }
     * 
     */
    public TaPrix createTaPrix() {
        return new TaPrix();
    }

    /**
     * Create an instance of {@link TaRTaTitreTransport }
     * 
     */
    public TaRTaTitreTransport createTaRTaTitreTransport() {
        return new TaRTaTitreTransport();
    }

    /**
     * Create an instance of {@link TaRapportUnite }
     * 
     */
    public TaRapportUnite createTaRapportUnite() {
        return new TaRapportUnite();
    }

    /**
     * Create an instance of {@link TaRecette }
     * 
     */
    public TaRecette createTaRecette() {
        return new TaRecette();
    }

    /**
     * Create an instance of {@link TaRefArticleFournisseur }
     * 
     */
    public TaRefArticleFournisseur createTaRefArticleFournisseur() {
        return new TaRefArticleFournisseur();
    }

    /**
     * Create an instance of {@link TaTAbonnement }
     * 
     */
    public TaTAbonnement createTaTAbonnement() {
        return new TaTAbonnement();
    }

    /**
     * Create an instance of {@link TaTFabrication }
     * 
     */
    public TaTFabrication createTaTFabrication() {
        return new TaTFabrication();
    }

    /**
     * Create an instance of {@link TaTNoteArticle }
     * 
     */
    public TaTNoteArticle createTaTNoteArticle() {
        return new TaTNoteArticle();
    }

    /**
     * Create an instance of {@link TaTReception }
     * 
     */
    public TaTReception createTaTReception() {
        return new TaTReception();
    }

    /**
     * Create an instance of {@link TaTSupport }
     * 
     */
    public TaTSupport createTaTSupport() {
        return new TaTSupport();
    }

    /**
     * Create an instance of {@link TaTTva }
     * 
     */
    public TaTTva createTaTTva() {
        return new TaTTva();
    }

    /**
     * Create an instance of {@link TaTva }
     * 
     */
    public TaTva createTaTva() {
        return new TaTva();
    }

    /**
     * Create an instance of {@link TaTypeCodeBarre }
     * 
     */
    public TaTypeCodeBarre createTaTypeCodeBarre() {
        return new TaTypeCodeBarre();
    }

    /**
     * Create an instance of {@link TaTypeMouvement }
     * 
     */
    public TaTypeMouvement createTaTypeMouvement() {
        return new TaTypeMouvement();
    }

    /**
     * Create an instance of {@link TaUnite }
     * 
     */
    public TaUnite createTaUnite() {
        return new TaUnite();
    }

    /**
     * Create an instance of {@link TaTitreTransport }
     * 
     */
    public TaTitreTransport createTaTitreTransport() {
        return new TaTitreTransport();
    }

    /**
     * Create an instance of {@link TaFondDeCaisse }
     * 
     */
    public TaFondDeCaisse createTaFondDeCaisse() {
        return new TaFondDeCaisse();
    }

    /**
     * Create an instance of {@link TaLSessionCaisse }
     * 
     */
    public TaLSessionCaisse createTaLSessionCaisse() {
        return new TaLSessionCaisse();
    }

    /**
     * Create an instance of {@link TaSessionCaisse }
     * 
     */
    public TaSessionCaisse createTaSessionCaisse() {
        return new TaSessionCaisse();
    }

    /**
     * Create an instance of {@link TaTFondDeCaisse }
     * 
     */
    public TaTFondDeCaisse createTaTFondDeCaisse() {
        return new TaTFondDeCaisse();
    }

    /**
     * Create an instance of {@link TaTLSessionCaisse }
     * 
     */
    public TaTLSessionCaisse createTaTLSessionCaisse() {
        return new TaTLSessionCaisse();
    }

    /**
     * Create an instance of {@link TaBareme }
     * 
     */
    public TaBareme createTaBareme() {
        return new TaBareme();
    }

    /**
     * Create an instance of {@link TaConformite }
     * 
     */
    public TaConformite createTaConformite() {
        return new TaConformite();
    }

    /**
     * Create an instance of {@link TaGroupe }
     * 
     */
    public TaGroupe createTaGroupe() {
        return new TaGroupe();
    }

    /**
     * Create an instance of {@link TaModeleBareme }
     * 
     */
    public TaModeleBareme createTaModeleBareme() {
        return new TaModeleBareme();
    }

    /**
     * Create an instance of {@link TaModeleConformite }
     * 
     */
    public TaModeleConformite createTaModeleConformite() {
        return new TaModeleConformite();
    }

    /**
     * Create an instance of {@link TaModeleGroupe }
     * 
     */
    public TaModeleGroupe createTaModeleGroupe() {
        return new TaModeleGroupe();
    }

    /**
     * Create an instance of {@link TaRGroupeModeleConformite }
     * 
     */
    public TaRGroupeModeleConformite createTaRGroupeModeleConformite() {
        return new TaRGroupeModeleConformite();
    }

    /**
     * Create an instance of {@link TaStatusConformite }
     * 
     */
    public TaStatusConformite createTaStatusConformite() {
        return new TaStatusConformite();
    }

    /**
     * Create an instance of {@link TaTypeConformite }
     * 
     */
    public TaTypeConformite createTaTypeConformite() {
        return new TaTypeConformite();
    }

    /**
     * Create an instance of {@link LigneTva }
     * 
     */
    public LigneTva createLigneTva() {
        return new LigneTva();
    }

    /**
     * Create an instance of {@link TaAcompte }
     * 
     */
    public TaAcompte createTaAcompte() {
        return new TaAcompte();
    }

    /**
     * Create an instance of {@link TaApporteur }
     * 
     */
    public TaApporteur createTaApporteur() {
        return new TaApporteur();
    }

    /**
     * Create an instance of {@link TaAvisEcheance }
     * 
     */
    public TaAvisEcheance createTaAvisEcheance() {
        return new TaAvisEcheance();
    }

    /**
     * Create an instance of {@link TaAvoir }
     * 
     */
    public TaAvoir createTaAvoir() {
        return new TaAvoir();
    }

    /**
     * Create an instance of {@link TaBonReception }
     * 
     */
    public TaBonReception createTaBonReception() {
        return new TaBonReception();
    }

    /**
     * Create an instance of {@link TaBoncde }
     * 
     */
    public TaBoncde createTaBoncde() {
        return new TaBoncde();
    }

    /**
     * Create an instance of {@link TaBonliv }
     * 
     */
    public TaBonliv createTaBonliv() {
        return new TaBonliv();
    }

    /**
     * Create an instance of {@link TaDevis }
     * 
     */
    public TaDevis createTaDevis() {
        return new TaDevis();
    }

    /**
     * Create an instance of {@link TaEtat }
     * 
     */
    public TaEtat createTaEtat() {
        return new TaEtat();
    }

    /**
     * Create an instance of {@link TaFacture }
     * 
     */
    public TaFacture createTaFacture() {
        return new TaFacture();
    }

    /**
     * Create an instance of {@link TaInfosAcompte }
     * 
     */
    public TaInfosAcompte createTaInfosAcompte() {
        return new TaInfosAcompte();
    }

    /**
     * Create an instance of {@link TaInfosApporteur }
     * 
     */
    public TaInfosApporteur createTaInfosApporteur() {
        return new TaInfosApporteur();
    }

    /**
     * Create an instance of {@link TaInfosAvisEcheance }
     * 
     */
    public TaInfosAvisEcheance createTaInfosAvisEcheance() {
        return new TaInfosAvisEcheance();
    }

    /**
     * Create an instance of {@link TaInfosAvoir }
     * 
     */
    public TaInfosAvoir createTaInfosAvoir() {
        return new TaInfosAvoir();
    }

    /**
     * Create an instance of {@link TaInfosBonReception }
     * 
     */
    public TaInfosBonReception createTaInfosBonReception() {
        return new TaInfosBonReception();
    }

    /**
     * Create an instance of {@link TaInfosBoncde }
     * 
     */
    public TaInfosBoncde createTaInfosBoncde() {
        return new TaInfosBoncde();
    }

    /**
     * Create an instance of {@link TaInfosBonliv }
     * 
     */
    public TaInfosBonliv createTaInfosBonliv() {
        return new TaInfosBonliv();
    }

    /**
     * Create an instance of {@link TaInfosDevis }
     * 
     */
    public TaInfosDevis createTaInfosDevis() {
        return new TaInfosDevis();
    }

    /**
     * Create an instance of {@link TaInfosFacture }
     * 
     */
    public TaInfosFacture createTaInfosFacture() {
        return new TaInfosFacture();
    }

    /**
     * Create an instance of {@link TaInfosPrelevement }
     * 
     */
    public TaInfosPrelevement createTaInfosPrelevement() {
        return new TaInfosPrelevement();
    }

    /**
     * Create an instance of {@link TaInfosProforma }
     * 
     */
    public TaInfosProforma createTaInfosProforma() {
        return new TaInfosProforma();
    }

    /**
     * Create an instance of {@link TaInfosTicketDeCaisse }
     * 
     */
    public TaInfosTicketDeCaisse createTaInfosTicketDeCaisse() {
        return new TaInfosTicketDeCaisse();
    }

    /**
     * Create an instance of {@link TaLAcompte }
     * 
     */
    public TaLAcompte createTaLAcompte() {
        return new TaLAcompte();
    }

    /**
     * Create an instance of {@link TaLApporteur }
     * 
     */
    public TaLApporteur createTaLApporteur() {
        return new TaLApporteur();
    }

    /**
     * Create an instance of {@link TaLAvisEcheance }
     * 
     */
    public TaLAvisEcheance createTaLAvisEcheance() {
        return new TaLAvisEcheance();
    }

    /**
     * Create an instance of {@link TaLAvoir }
     * 
     */
    public TaLAvoir createTaLAvoir() {
        return new TaLAvoir();
    }

    /**
     * Create an instance of {@link TaLBonReception }
     * 
     */
    public TaLBonReception createTaLBonReception() {
        return new TaLBonReception();
    }

    /**
     * Create an instance of {@link TaLBoncde }
     * 
     */
    public TaLBoncde createTaLBoncde() {
        return new TaLBoncde();
    }

    /**
     * Create an instance of {@link TaLBonliv }
     * 
     */
    public TaLBonliv createTaLBonliv() {
        return new TaLBonliv();
    }

    /**
     * Create an instance of {@link TaLDevis }
     * 
     */
    public TaLDevis createTaLDevis() {
        return new TaLDevis();
    }

    /**
     * Create an instance of {@link TaLEcheance }
     * 
     */
    public TaLEcheance createTaLEcheance() {
        return new TaLEcheance();
    }

    /**
     * Create an instance of {@link TaLFacture }
     * 
     */
    public TaLFacture createTaLFacture() {
        return new TaLFacture();
    }

    /**
     * Create an instance of {@link TaLPrelevement }
     * 
     */
    public TaLPrelevement createTaLPrelevement() {
        return new TaLPrelevement();
    }

    /**
     * Create an instance of {@link TaLProforma }
     * 
     */
    public TaLProforma createTaLProforma() {
        return new TaLProforma();
    }

    /**
     * Create an instance of {@link TaLTicketDeCaisse }
     * 
     */
    public TaLTicketDeCaisse createTaLTicketDeCaisse() {
        return new TaLTicketDeCaisse();
    }

    /**
     * Create an instance of {@link TaMiseADisposition }
     * 
     */
    public TaMiseADisposition createTaMiseADisposition() {
        return new TaMiseADisposition();
    }

    /**
     * Create an instance of {@link TaPrelevement }
     * 
     */
    public TaPrelevement createTaPrelevement() {
        return new TaPrelevement();
    }

    /**
     * Create an instance of {@link TaProforma }
     * 
     */
    public TaProforma createTaProforma() {
        return new TaProforma();
    }

    /**
     * Create an instance of {@link TaRAcompte }
     * 
     */
    public TaRAcompte createTaRAcompte() {
        return new TaRAcompte();
    }

    /**
     * Create an instance of {@link TaRAvoir }
     * 
     */
    public TaRAvoir createTaRAvoir() {
        return new TaRAvoir();
    }

    /**
     * Create an instance of {@link TaRDocument }
     * 
     */
    public TaRDocument createTaRDocument() {
        return new TaRDocument();
    }

    /**
     * Create an instance of {@link TaRReglement }
     * 
     */
    public TaRReglement createTaRReglement() {
        return new TaRReglement();
    }

    /**
     * Create an instance of {@link TaReglement }
     * 
     */
    public TaReglement createTaReglement() {
        return new TaReglement();
    }

    /**
     * Create an instance of {@link TaTLigne }
     * 
     */
    public TaTLigne createTaTLigne() {
        return new TaTLigne();
    }

    /**
     * Create an instance of {@link TaTPaiement }
     * 
     */
    public TaTPaiement createTaTPaiement() {
        return new TaTPaiement();
    }

    /**
     * Create an instance of {@link TaTicketDeCaisse }
     * 
     */
    public TaTicketDeCaisse createTaTicketDeCaisse() {
        return new TaTicketDeCaisse();
    }

    /**
     * Create an instance of {@link TaClientLegrain }
     * 
     */
    public TaClientLegrain createTaClientLegrain() {
        return new TaClientLegrain();
    }

    /**
     * Create an instance of {@link TaEntrepriseClient }
     * 
     */
    public TaEntrepriseClient createTaEntrepriseClient() {
        return new TaEntrepriseClient();
    }

    /**
     * Create an instance of {@link TaGroupeEntreprise }
     * 
     */
    public TaGroupeEntreprise createTaGroupeEntreprise() {
        return new TaGroupeEntreprise();
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
     * Create an instance of {@link TaUtilisateur }
     * 
     */
    public TaUtilisateur createTaUtilisateur() {
        return new TaUtilisateur();
    }

    /**
     * Create an instance of {@link PaiementCarteBancaire }
     * 
     */
    public PaiementCarteBancaire createPaiementCarteBancaire() {
        return new PaiementCarteBancaire();
    }

    /**
     * Create an instance of {@link RetourPaiementCarteBancaire }
     * 
     */
    public RetourPaiementCarteBancaire createRetourPaiementCarteBancaire() {
        return new RetourPaiementCarteBancaire();
    }

    /**
     * Create an instance of {@link TaGrMouvStock }
     * 
     */
    public TaGrMouvStock createTaGrMouvStock() {
        return new TaGrMouvStock();
    }

    /**
     * Create an instance of {@link TaInventaire }
     * 
     */
    public TaInventaire createTaInventaire() {
        return new TaInventaire();
    }

    /**
     * Create an instance of {@link TaLInventaire }
     * 
     */
    public TaLInventaire createTaLInventaire() {
        return new TaLInventaire();
    }

    /**
     * Create an instance of {@link TaMouvementStock }
     * 
     */
    public TaMouvementStock createTaMouvementStock() {
        return new TaMouvementStock();
    }

    /**
     * Create an instance of {@link TaOptionAbon }
     * 
     */
    public TaOptionAbon createTaOptionAbon() {
        return new TaOptionAbon();
    }

    /**
     * Create an instance of {@link TaROptionAbon }
     * 
     */
    public TaROptionAbon createTaROptionAbon() {
        return new TaROptionAbon();
    }

    /**
     * Create an instance of {@link TaSupportAbon }
     * 
     */
    public TaSupportAbon createTaSupportAbon() {
        return new TaSupportAbon();
    }

    /**
     * Create an instance of {@link TaAdresse }
     * 
     */
    public TaAdresse createTaAdresse() {
        return new TaAdresse();
    }

    /**
     * Create an instance of {@link TaCPaiement }
     * 
     */
    public TaCPaiement createTaCPaiement() {
        return new TaCPaiement();
    }

    /**
     * Create an instance of {@link TaCommentaire }
     * 
     */
    public TaCommentaire createTaCommentaire() {
        return new TaCommentaire();
    }

    /**
     * Create an instance of {@link TaCompl }
     * 
     */
    public TaCompl createTaCompl() {
        return new TaCompl();
    }

    /**
     * Create an instance of {@link TaCompteBanque }
     * 
     */
    public TaCompteBanque createTaCompteBanque() {
        return new TaCompteBanque();
    }

    /**
     * Create an instance of {@link TaEmail }
     * 
     */
    public TaEmail createTaEmail() {
        return new TaEmail();
    }

    /**
     * Create an instance of {@link TaEntreprise }
     * 
     */
    public TaEntreprise createTaEntreprise() {
        return new TaEntreprise();
    }

    /**
     * Create an instance of {@link TaFamilleTiers }
     * 
     */
    public TaFamilleTiers createTaFamilleTiers() {
        return new TaFamilleTiers();
    }

    /**
     * Create an instance of {@link TaInfoJuridique }
     * 
     */
    public TaInfoJuridique createTaInfoJuridique() {
        return new TaInfoJuridique();
    }

    /**
     * Create an instance of {@link TaRPrix }
     * 
     */
    public TaRPrix createTaRPrix() {
        return new TaRPrix();
    }

    /**
     * Create an instance of {@link TaRPrixTiers }
     * 
     */
    public TaRPrixTiers createTaRPrixTiers() {
        return new TaRPrixTiers();
    }

    /**
     * Create an instance of {@link TaTAdr }
     * 
     */
    public TaTAdr createTaTAdr() {
        return new TaTAdr();
    }

    /**
     * Create an instance of {@link TaTBanque }
     * 
     */
    public TaTBanque createTaTBanque() {
        return new TaTBanque();
    }

    /**
     * Create an instance of {@link TaTCPaiement }
     * 
     */
    public TaTCPaiement createTaTCPaiement() {
        return new TaTCPaiement();
    }

    /**
     * Create an instance of {@link TaTCivilite }
     * 
     */
    public TaTCivilite createTaTCivilite() {
        return new TaTCivilite();
    }

    /**
     * Create an instance of {@link TaTEmail }
     * 
     */
    public TaTEmail createTaTEmail() {
        return new TaTEmail();
    }

    /**
     * Create an instance of {@link TaTEntite }
     * 
     */
    public TaTEntite createTaTEntite() {
        return new TaTEntite();
    }

    /**
     * Create an instance of {@link TaTTarif }
     * 
     */
    public TaTTarif createTaTTarif() {
        return new TaTTarif();
    }

    /**
     * Create an instance of {@link TaTTel }
     * 
     */
    public TaTTel createTaTTel() {
        return new TaTTel();
    }

    /**
     * Create an instance of {@link TaTTiers }
     * 
     */
    public TaTTiers createTaTTiers() {
        return new TaTTiers();
    }

    /**
     * Create an instance of {@link TaTTvaDoc }
     * 
     */
    public TaTTvaDoc createTaTTvaDoc() {
        return new TaTTvaDoc();
    }

    /**
     * Create an instance of {@link TaTWeb }
     * 
     */
    public TaTWeb createTaTWeb() {
        return new TaTWeb();
    }

    /**
     * Create an instance of {@link TaTelephone }
     * 
     */
    public TaTelephone createTaTelephone() {
        return new TaTelephone();
    }

    /**
     * Create an instance of {@link TaTiers }
     * 
     */
    public TaTiers createTaTiers() {
        return new TaTiers();
    }

    /**
     * Create an instance of {@link TaWeb }
     * 
     */
    public TaWeb createTaWeb() {
        return new TaWeb();
    }

    /**
     * Create an instance of {@link TiersDossier }
     * 
     */
    public TiersDossier createTiersDossier() {
        return new TiersDossier();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ClientExisteChezFournisseur }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.bdg.legrain.fr/", name = "clientExisteChezFournisseur")
    public JAXBElement<ClientExisteChezFournisseur> createClientExisteChezFournisseur(ClientExisteChezFournisseur value) {
        return new JAXBElement<ClientExisteChezFournisseur>(_ClientExisteChezFournisseur_QNAME, ClientExisteChezFournisseur.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ClientExisteChezFournisseurResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.bdg.legrain.fr/", name = "clientExisteChezFournisseurResponse")
    public JAXBElement<ClientExisteChezFournisseurResponse> createClientExisteChezFournisseurResponse(ClientExisteChezFournisseurResponse value) {
        return new JAXBElement<ClientExisteChezFournisseurResponse>(_ClientExisteChezFournisseurResponse_QNAME, ClientExisteChezFournisseurResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DevisClientChezFournisseur }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.bdg.legrain.fr/", name = "devisClientChezFournisseur")
    public JAXBElement<DevisClientChezFournisseur> createDevisClientChezFournisseur(DevisClientChezFournisseur value) {
        return new JAXBElement<DevisClientChezFournisseur>(_DevisClientChezFournisseur_QNAME, DevisClientChezFournisseur.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DevisClientChezFournisseurDate }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.bdg.legrain.fr/", name = "devisClientChezFournisseurDate")
    public JAXBElement<DevisClientChezFournisseurDate> createDevisClientChezFournisseurDate(DevisClientChezFournisseurDate value) {
        return new JAXBElement<DevisClientChezFournisseurDate>(_DevisClientChezFournisseurDate_QNAME, DevisClientChezFournisseurDate.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DevisClientChezFournisseurDateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.bdg.legrain.fr/", name = "devisClientChezFournisseurDateResponse")
    public JAXBElement<DevisClientChezFournisseurDateResponse> createDevisClientChezFournisseurDateResponse(DevisClientChezFournisseurDateResponse value) {
        return new JAXBElement<DevisClientChezFournisseurDateResponse>(_DevisClientChezFournisseurDateResponse_QNAME, DevisClientChezFournisseurDateResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DevisClientChezFournisseurResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.bdg.legrain.fr/", name = "devisClientChezFournisseurResponse")
    public JAXBElement<DevisClientChezFournisseurResponse> createDevisClientChezFournisseurResponse(DevisClientChezFournisseurResponse value) {
        return new JAXBElement<DevisClientChezFournisseurResponse>(_DevisClientChezFournisseurResponse_QNAME, DevisClientChezFournisseurResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FacturesClientChezFournisseur }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.bdg.legrain.fr/", name = "facturesClientChezFournisseur")
    public JAXBElement<FacturesClientChezFournisseur> createFacturesClientChezFournisseur(FacturesClientChezFournisseur value) {
        return new JAXBElement<FacturesClientChezFournisseur>(_FacturesClientChezFournisseur_QNAME, FacturesClientChezFournisseur.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FacturesClientChezFournisseurDate }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.bdg.legrain.fr/", name = "facturesClientChezFournisseurDate")
    public JAXBElement<FacturesClientChezFournisseurDate> createFacturesClientChezFournisseurDate(FacturesClientChezFournisseurDate value) {
        return new JAXBElement<FacturesClientChezFournisseurDate>(_FacturesClientChezFournisseurDate_QNAME, FacturesClientChezFournisseurDate.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FacturesClientChezFournisseurDateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.bdg.legrain.fr/", name = "facturesClientChezFournisseurDateResponse")
    public JAXBElement<FacturesClientChezFournisseurDateResponse> createFacturesClientChezFournisseurDateResponse(FacturesClientChezFournisseurDateResponse value) {
        return new JAXBElement<FacturesClientChezFournisseurDateResponse>(_FacturesClientChezFournisseurDateResponse_QNAME, FacturesClientChezFournisseurDateResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FacturesClientChezFournisseurResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.bdg.legrain.fr/", name = "facturesClientChezFournisseurResponse")
    public JAXBElement<FacturesClientChezFournisseurResponse> createFacturesClientChezFournisseurResponse(FacturesClientChezFournisseurResponse value) {
        return new JAXBElement<FacturesClientChezFournisseurResponse>(_FacturesClientChezFournisseurResponse_QNAME, FacturesClientChezFournisseurResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FournisseurPermetPaiementParCB }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.bdg.legrain.fr/", name = "fournisseurPermetPaiementParCB")
    public JAXBElement<FournisseurPermetPaiementParCB> createFournisseurPermetPaiementParCB(FournisseurPermetPaiementParCB value) {
        return new JAXBElement<FournisseurPermetPaiementParCB>(_FournisseurPermetPaiementParCB_QNAME, FournisseurPermetPaiementParCB.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FournisseurPermetPaiementParCBResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.bdg.legrain.fr/", name = "fournisseurPermetPaiementParCBResponse")
    public JAXBElement<FournisseurPermetPaiementParCBResponse> createFournisseurPermetPaiementParCBResponse(FournisseurPermetPaiementParCBResponse value) {
        return new JAXBElement<FournisseurPermetPaiementParCBResponse>(_FournisseurPermetPaiementParCBResponse_QNAME, FournisseurPermetPaiementParCBResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InfosClientChezFournisseur }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.bdg.legrain.fr/", name = "infosClientChezFournisseur")
    public JAXBElement<InfosClientChezFournisseur> createInfosClientChezFournisseur(InfosClientChezFournisseur value) {
        return new JAXBElement<InfosClientChezFournisseur>(_InfosClientChezFournisseur_QNAME, InfosClientChezFournisseur.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InfosClientChezFournisseurResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.bdg.legrain.fr/", name = "infosClientChezFournisseurResponse")
    public JAXBElement<InfosClientChezFournisseurResponse> createInfosClientChezFournisseurResponse(InfosClientChezFournisseurResponse value) {
        return new JAXBElement<InfosClientChezFournisseurResponse>(_InfosClientChezFournisseurResponse_QNAME, InfosClientChezFournisseurResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Init }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.bdg.legrain.fr/", name = "init")
    public JAXBElement<Init> createInit(Init value) {
        return new JAXBElement<Init>(_Init_QNAME, Init.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InitResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.bdg.legrain.fr/", name = "initResponse")
    public JAXBElement<InitResponse> createInitResponse(InitResponse value) {
        return new JAXBElement<InitResponse>(_InitResponse_QNAME, InitResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListeFournisseur }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.bdg.legrain.fr/", name = "listeFournisseur")
    public JAXBElement<ListeFournisseur> createListeFournisseur(ListeFournisseur value) {
        return new JAXBElement<ListeFournisseur>(_ListeFournisseur_QNAME, ListeFournisseur.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListeFournisseurResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.bdg.legrain.fr/", name = "listeFournisseurResponse")
    public JAXBElement<ListeFournisseurResponse> createListeFournisseurResponse(ListeFournisseurResponse value) {
        return new JAXBElement<ListeFournisseurResponse>(_ListeFournisseurResponse_QNAME, ListeFournisseurResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PayerFactureCB }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.bdg.legrain.fr/", name = "payerFactureCB")
    public JAXBElement<PayerFactureCB> createPayerFactureCB(PayerFactureCB value) {
        return new JAXBElement<PayerFactureCB>(_PayerFactureCB_QNAME, PayerFactureCB.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PayerFactureCBResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.bdg.legrain.fr/", name = "payerFactureCBResponse")
    public JAXBElement<PayerFactureCBResponse> createPayerFactureCBResponse(PayerFactureCBResponse value) {
        return new JAXBElement<PayerFactureCBResponse>(_PayerFactureCBResponse_QNAME, PayerFactureCBResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PdfClient }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.bdg.legrain.fr/", name = "pdfClient")
    public JAXBElement<PdfClient> createPdfClient(PdfClient value) {
        return new JAXBElement<PdfClient>(_PdfClient_QNAME, PdfClient.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PdfClientResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.bdg.legrain.fr/", name = "pdfClientResponse")
    public JAXBElement<PdfClientResponse> createPdfClientResponse(PdfClientResponse value) {
        return new JAXBElement<PdfClientResponse>(_PdfClientResponse_QNAME, PdfClientResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PdfFacture }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.bdg.legrain.fr/", name = "pdfFacture")
    public JAXBElement<PdfFacture> createPdfFacture(PdfFacture value) {
        return new JAXBElement<PdfFacture>(_PdfFacture_QNAME, PdfFacture.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PdfFactureResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.bdg.legrain.fr/", name = "pdfFactureResponse")
    public JAXBElement<PdfFactureResponse> createPdfFactureResponse(PdfFactureResponse value) {
        return new JAXBElement<PdfFactureResponse>(_PdfFactureResponse_QNAME, PdfFactureResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EJBException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.bdg.legrain.fr/", name = "EJBException")
    public JAXBElement<EJBException> createEJBException(EJBException value) {
        return new JAXBElement<EJBException>(_EJBException_QNAME, EJBException.class, null, value);
    }

}
