package fr.legrain.bdg.documents.service.remote;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.FinderException;
import javax.ejb.Remote;
import javax.ejb.RemoveException;

import fr.legrain.bdg.dashboard.service.remote.IDashboardDocumentPayableServiceRemote;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceDocumentRemote;
import fr.legrain.document.dashboard.dto.TaArticlesParTiersDTO;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IDocumentTiersDTO;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaRAcompte;
import fr.legrain.document.model.TaRAvoir;
import fr.legrain.document.model.TaRReglement;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.edition.model.TaEdition;

@Remote
public interface ITaFactureServiceRemote extends IGenericCRUDServiceDocumentRemote<TaFacture,TaFactureDTO>,
IDocumentLigneALigneService<TaFacture>,														
IAbstractLgrDAOServer<TaFacture>,IAbstractLgrDAOServerDTO<TaFactureDTO>,
														IDocumentService<TaFacture>, IDocumentTiersStatistiquesService<TaFacture>,
														IDocumentCodeGenere,IDashboardDocumentPayableServiceRemote {
	public static final String validationContext = "FACTURE";
	
	public TaFacture mergeAndFindById(TaFacture detachedInstance, String validationContext);
	
	public TaFacture getReference(int factureId);
	
	public List<TaFacture> findByCodeTiersAndDateCompteClient(String codeTiers, Date debut, Date fin);
	
	
	public void calculeTvaEtTotaux(TaFacture doc);
	
	/**
	 * Calcul des totaux de la facture
	 */
	public void calculTotaux(TaFacture doc);

	/**
	 * Reparti le total chaque code TVA sur l'ensemble des lignes concernées par ce code. 
	 */
	public void dispatcherTva(TaFacture doc);
	
	/**
	 * Lance la fonction de calcul du montant sur chacunes des lignes du document.
	 */
	public void calculMontantLigneDocument(TaFacture doc);
	
	/**
	 * Calcul de la grille de TVA en fonction de lignes du document et du taux de remise HT global.
	 * Mise à jour de la propriété <code>lignesTVA</code>
	 * et mise à jour du montant de la TVA dans les lignes du document
	 */
	public void calculTvaTotal(TaFacture doc);
	
	/**
	 * Calcul de la grille de TVA en fonction de lignes du document et du taux de remise HT global.
	 * Mise à jour de la propriété <code>lignesTVA</code>
	 */
	public void calculLignesTva(TaFacture doc);
	
	public Date calculDateEcheanceAbstract(TaFacture doc, Integer report, Integer finDeMois) ;
	
	public Date calculDateEcheanceAbstract(TaFacture doc, Integer report, Integer finDeMois,TaTPaiement taTPaiement) ;
	
	public Date calculDateEcheance(TaFacture doc, Integer report, Integer finDeMois,TaTPaiement taTPaiement) ;
	
	public void mettreAJourDateEcheanceReglement(TaFacture doc, TaReglement reglement);
		
	public boolean contientReglementAffectationMultiple(TaFacture doc);
	public void mettreAJourDateEcheanceReglements(TaFacture doc);
	public boolean reglementRempli(TaFacture doc);
	public boolean reglementExiste(TaFacture doc);
	public LinkedList<TaRReglement> rechercheSiDocumentContientTraite(TaFacture doc, String typeTraite);
	public void modifieLibellePaiementMultiple(TaFacture doc);
	public void modifieTypePaiementMultiple(TaFacture doc);
	public TaFacture affecteReglementFacture(TaFacture doc, String typePaiementDefaut) throws Exception;
	public void gestionReglement(TaFacture doc, TaRReglement taReglement);
	public void removeTousRAvoirs(TaFacture doc) throws Exception;
//	public void removeTousRAcomptes(TaFacture doc) throws Exception;
	public TaFacture removeTousRReglements(TaFacture doc) throws Exception;
	public TaFacture removeReglement(TaFacture doc, TaRReglement taRReglement);
	public TaFacture addRReglement(TaFacture doc, TaRReglement taReglement);
//	public void removeRAcompte(TaFacture doc, TaRAcompte taRAcompte);
	public void addRAvoir(TaFacture doc, TaRAvoir taRAvoir);
	public void removeRAvoir(TaFacture doc, TaRAvoir taRAvoir);
//	public void addRAcompte(TaFacture doc, TaRAcompte taRAcompte);
	public TaRReglement creeRReglement(TaFacture doc,TaRReglement taRReglement,Boolean integrer,String typePaiementDefaut) throws Exception;
	public TaRReglement creeRReglement(TaFacture doc,TaRReglement taRReglement,Boolean integrer,String typePaiementDefaut,TaReglement reglement) throws Exception;
	public boolean multiReglement(TaFacture doc);
	public BigDecimal calculResteAReglerComplet(TaFacture doc);
	public TaFacture calculRegleDocument(TaFacture doc);
	public BigDecimal calculRegleDocumentComplet(TaFacture doc);
	public BigDecimal calculSommeReglementsComplet(TaFacture doc,TaRReglement taRReglementEnCours);
	public BigDecimal calculSommeReglementsComplet(TaFacture doc);
	public Boolean aDesAvoirsIndirects(TaFacture doc);
	public Boolean aDesReglementsIndirects(TaFacture doc);
	public BigDecimal calculSommeReglementsIntegres(TaFacture doc);
	public BigDecimal calculSommeReglementsIntegresEcran(TaFacture doc);
//	public void calculSommeAcomptes(TaFacture doc);
	public BigDecimal calculSommeAcomptes(TaFacture doc, TaRAcompte acompteEnCours);
	public BigDecimal calculSommeAvoir(TaFacture doc, TaRAvoir avoirEnCours);
	public BigDecimal calculSommeAvoir(TaFacture doc);
//	public BigDecimal calculSommeAvoirIntegres(TaFacture doc);
//	public void dispatcherTvaAvantRemise(TaFacture doc);
//	public BigDecimal resteTVAAvantRemise(TaFacture doc, LigneTva ligneTva);
//	public BigDecimal prorataMontantTVALigneAvantRemise(TaFacture doc, TaLFacture ligne, LigneTva ligneTva);
//	public BigDecimal prorataMontantTVALigne(TaFacture doc, TaLFacture ligne, LigneTva ligneTva);
	public void removeTousLesAbonnements(TaFacture persistentInstance) throws Exception;
	public void removeTousLesSupportAbons(TaFacture persistentInstance) throws Exception;
	
	public List<TaFactureDTO> findAllLight();
	public List<TaFactureDTO> findAllDTOPeriode(Date dateDebut, Date dateFin);
	public List<TaFacture> rechercheDocumentNonExporte(Date dateDeb, Date dateFin,Boolean parDate);
	public List<TaFacture> rechercheDocumentOrderByDate(Date dateDeb, Date dateFin);
	public List<TaFactureDTO> rechercheDocumentNonExporteLight(Date dateDeb, Date dateFin,boolean parDate) ;
	public List<Object> findChiffreAffaireTotal(Date debut, Date fin, int precision);
//	public List<TaFactureDTO> findAllDTOPeriode(Date dateDebut, Date dateFin);
	
	public List<TaFacture> rechercheDocumentNonTotalementRegle(Date dateDeb, Date dateFin) ;
	public List<TaFactureDTO> rechercheDocumentNonTotalementRegle(Date dateDeb, Date dateFin,String tiers,String document);
	public List<TaFacture> rechercheDocumentNonTotalementRegleAEcheance(Date dateDeb, Date dateFin,String tiers,String document,BigDecimal limite);
	public List<Object[]> rechercheDocumentNonTotalementRegleAEcheance2(Date dateDeb, Date dateFin,String tiers,String document);


	
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalSansAvoirDTO(Date dateDebut, Date dateFin , String codeTiers);
	
	/**RAJOUT YANN**/
	public List<DocumentChiffreAffaireDTO> countFamilleArticleDTOArticle(Date dateDebut, Date dateFin, String codeArticle, String codeTiers);
	
	public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiers(Date dateDebut, Date dateFin, String codeArticle, String codeTiers, String orderBy);
	public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiersUnite(Date dateDebut, Date dateFin, String codeArticle, String codeUnite, String codeTiers, String orderBy);
	public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiersUniteFamille(Date dateDebut, Date dateFin, String codeArticle, String codeUnite,String codeFamille, String codeTiers, String orderBy);
	public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiers(Date dateDebut, Date dateFin, String codeArticle, String codeTiers);
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeParArticleParUniteAvoirFactureDTO(Date dateDebut, Date dateFin, String codeArticle,String codeUnite, String orderBy);
	
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeParArticleAvoirFactureDTO(Date dateDebut, Date dateFin, String codeArticle);
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeParArticleAvoirFactureDTO(Date dateDebut, Date dateFin, String codeArticle, Boolean synthese);
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeParArticleAvoirFactureDTO(Date dateDebut, Date dateFin, String codeArticle, Boolean synthese, String orderBy);
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeParTiersAvoirFactureDTO(Date dateDebut, Date dateFin, String codeArticle,String codeTiers, Boolean synthese, String orderBy);
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeParTiersParUniteAvoirFactureDTO(Date dateDebut, Date dateFin, String codeArticle,String codeTiers,String codeUnite, String orderBy);
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeParTiersEtArticlesAvoirFactureDTO(Date dateDebut, Date dateFin, String codeArticle,String codeTiers, Boolean synthese, String orderBy);
	
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeParFamilleArticleAvoirFactureDTO(Date dateDebut, Date dateFin, String codeArticle, String orderBy);
	public List<DocumentChiffreAffaireDTO>  detailParFamilleArticleAvoirFactureDTO(Date dateDebut, Date dateFin, String codeFamille, String orderBy);
	public List<DocumentChiffreAffaireDTO> detailParFamilleUniteArticleAvoirFactureDTO(Date dateDebut, Date dateFin, String codeFamille, String codeUnite, String orderBy);
	public long countTiersAyantAcheterArticleSurPeriode(Date dateDebut, Date dateFin, String codeArticle);
	public List<DocumentChiffreAffaireDTO> listTiersAyantAcheterArticleDTO(Date dateDebut, Date dateFin, String codeArticle);
//	public List<DocumentDTO> listLigneArticlePeriodeParArticleAvoirFactureDTO(Date dateDebut, Date dateFin, String codeArticle);
	//Pour le graphique dans l'onglet facture et avoirs du tableau de bord par article
	public List<DocumentChiffreAffaireDTO> listSumCAMoisTotalLigneArticlePeriodeParArticleMoinsAvoir(Date dateDebut, Date dateFin, String codeArticle);
	
	
	public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiersParTypeEtatDoc(Date dateDebut, Date dateFin, String codeArticle, String codeTiers, String typeEtatDoc, int deltaNbJours);


	public String writingFileEdition(TaEdition taEdition);
	
	public List<TaFactureDTO> rechercheDocumentDTO(Date dateExport, String codeTiers, Date dateDeb, Date dateFin);

	public List<TaFactureDTO> rechercheDocumentDTO(Date dateDeb, Date dateFin, String codeTiers, Boolean export);

	public List<TaFactureDTO> rechercheDocumentDTO(String codeDeb, String codeFin);

	public List<TaFactureDTO> rechercheDocumentDTO(Date dateDeb, Date dateFin);
	
	public void executeUpdate(String requete,List<Integer> ids,Date dateExport);
	
	public Date selectMinDateDocumentNonExporte(Date dateDeb, Date dateFin);
	
	public TaEtat rechercheEtatInitialDocument();
	public TaEtat etatLigneInsertion(TaFacture masterEntity);

	TaFacture mergeEtat(IDocumentTiers detachedInstance, boolean avecLien);
	
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeAvoirFactureFournisseurDTO(Date dateDebut, Date dateFin, String codeArticle);
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeAvoirFactureFournisseurDTO(Date dateDebut, Date dateFin, String codeArticle, Boolean synthese);
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeAvoirFactureFournisseurDTO(Date dateDebut, Date dateFin, String codeArticle, Boolean synthese, String orderBy);
	
	public long countDocument(Date debut, Date fin, String codeTiers , String codeArticle);
//	public List<TaArticlesParTiersDTO> findArticlesParTiersParMois(Date debut, Date fin);
//	public List<TaArticlesParTiersDTO> findArticlesParTiersTransforme(Date debut, Date fin);
//	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransforme(Date debut, Date fin);
//	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransformeARelancer(Date debut, Date fin, int deltaNbJours);
//	public long countFacture(Date debut, Date fin);
//	public long countFactureNonPaye(Date debut, Date fin);
//	public long countFactureNonPayesARelancer(Date debut, Date fin, int deltaNbJours);
//	public long countFacturePayes(Date debut, Date fin);

	public List<TaFacture> selectAllFetch();


	
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireJmaDTO(Date debut, Date fin, int precision);	
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotalDTO(Date debut, Date fin);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffairePayeTotalDTO(Date dateDebut, Date dateFin);	
	

	public List<TaFactureDTO> rechercheDocumentPartiellementOrTotalementRegle();
	
	public List<DocumentDTO> findAllDTOPeriodeAvecEtat(Date dateDebut, Date dateFin,String codeTiers, String etat);
	public List<DocumentChiffreAffaireDTO> chiffreAffaireTotalAvecEtatDTO(Date dateDebut, Date dateFin,String codeTiers, String etat);
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalAvecEtatDTO(Date dateDebut, Date dateFin,String codeTiers, String etat);
	
	public DocumentChiffreAffaireDTO chiffreAffaireTotalSurEtatDTO(Date dateDebut, Date dateFin,String codeTiers, String etat);
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalSurEtatDTO(Date dateDebut, Date dateFin,String codeTiers, String etat);

	public List<DocumentChiffreAffaireDTO> countDocumentAvecEtat(Date debut, Date fin, String codeTiers, String codeArticle,
			String etat);
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalJmaAvecEtatDTO(Date dateDebut, Date dateFin, int precision,String codeTiers,String etat);
	
	public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiersAvecEtat(Date dateDebut, Date dateFin, String codeArticle, String codeTiers,String etat,int deltaNbJours);
	public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiersAvecEtat(Date dateDebut, Date dateFin, String codeArticle, String codeTiers,String etat, String orderBy, int deltaNbJours);
	
	
	public List<TaArticlesParTiersDTO> findArticlesParTiersParMoisAvecEtat(Date debut, Date fin,String codeTiers, String etat,int deltaNbJours);
}
