package fr.legrain.bdg.documents.service.remote;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import fr.legrain.abonnement.dto.TaAbonnementFullDTO;
import fr.legrain.bdg.dashboard.service.remote.IDashboardDocumentServiceRemote;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceDocumentRemote;
import fr.legrain.document.dashboard.dto.TaArticlesParTiersDTO;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.document.dto.TaAbonnementDTO;
import fr.legrain.document.model.TaAbonnement;
import fr.legrain.document.model.TaLAbonnement;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaLEcheance;

@Remote
public interface ITaAbonnementServiceRemote extends IGenericCRUDServiceDocumentRemote<TaAbonnement,TaAbonnementDTO>,
IDocumentLigneALigneService<TaAbonnement>,
														IAbstractLgrDAOServer<TaAbonnement>,IAbstractLgrDAOServerDTO<TaAbonnementDTO>,
														IDocumentService<TaAbonnement>, IDocumentTiersStatistiquesService<TaAbonnement>,IDashboardDocumentServiceRemote{
	
	public static final String validationContext = "ABONNEMENT";
	
	public String genereCode( Map<String, String> params);
	public void annuleCode(String code);
	public void verrouilleCode(String code);
	
	public TaAbonnement findByIdSubscription(Integer idStripeSubscription);
	
	public TaAbonnement mergeAndFindById(TaAbonnement detachedInstance, String validationContext);
	
	public void calculeTvaEtTotaux(TaAbonnement doc);
	
	/**
	 * Calcul des totaux de la facture
	 */
	public void calculTotaux(TaAbonnement doc);

	/**
	 * Reparti le total chaque code TVA sur l'ensemble des lignes concernées par ce code. 
	 */
	public void dispatcherTva(TaAbonnement doc);
	
	/**
	 * Lance la fonction de calcul du montant sur chacunes des lignes du document.
	 */
	public void calculMontantLigneDocument(TaAbonnement doc);
	
	/**
	 * Calcul de la grille de TVA en fonction de lignes du document et du taux de remise HT global.
	 * Mise à jour de la propriété <code>lignesTVA</code>
	 * et mise à jour du montant de la TVA dans les lignes du document
	 */
	public void calculTvaTotal(TaAbonnement doc);
	
	/**
	 * Calcul de la grille de TVA en fonction de lignes du document et du taux de remise HT global.
	 * Mise à jour de la propriété <code>lignesTVA</code>
	 */
	public void calculLignesTva(TaAbonnement doc);
	
	public Date calculDateEcheanceAbstract(TaAbonnement doc, Integer report, Integer finDeMois);
	
	public Date calculDateEcheance(TaAbonnement doc, Integer report, Integer finDeMois);
	
	public DocumentChiffreAffaireDTO chiffreAffaireTotalAnnuleDTO(Date dateDebut, Date dateFin,String codeTiers);
	public long countDocumentAnnule(Date dateDebut, Date dateFin,String codeTiers);
	public List<TaAbonnementDTO> findAllAnnuleDTOPeriode(Date dateDebut, Date dateFin,String codeTiers);
	public List<TaAbonnementDTO> findAllEnCoursDTOPeriode(Date dateDebut, Date dateFin,String codeTiers);
	public DocumentChiffreAffaireDTO chiffreAffaireTotalEnCoursDTO(Date dateDebut, Date dateFin,String codeTiers);
	public long countDocumentEnCours(Date dateDebut, Date dateFin,String codeTiers);
	public List<TaAbonnementDTO> findAllSuspenduDTOPeriode(Date dateDebut, Date dateFin,String codeTiers);
	public DocumentChiffreAffaireDTO chiffreAffaireTotalSuspenduDTO(Date dateDebut, Date dateFin,String codeTiers);
	public long countDocumentSuspendu(Date dateDebut, Date dateFin,String codeTiers);
	
	public List<TaLEcheance> genereProchainesEcheances(TaAbonnement taAbonnement, TaLEcheance echeancePrecedente);
	public List<TaLEcheance> generePremieresEcheances(TaAbonnement taAbonnement);
	//rajout yann
	public TaLEcheance genereUneProchaineEcheance(TaLEcheance echeancePrecedente);
	public List<TaLEcheance> generePremiereEcheance(TaLAbonnement taLAbonnement);
	public Integer genereProchainesEcheancesRegul();
	public Integer generePremiereEcheancesRegul();
	
	public List<TaAbonnement> selectAllAvecFetchLignesEtPlan();
	
	public List<TaAbonnementFullDTO> findAllFullDTOByIdTiers(Integer idTiers);
	
	public List<TaAbonnementDTO> rechercherAbonnementNonStripeCustomerDTO(String idExterneCustomer);
	public List<TaAbonnementDTO> rechercherAbonnementCustomerDTO(String idExterneCustomer);
	public List<TaAbonnementDTO> rechercherAbonnementCustomerDTO(String idExterneCustomer, Boolean stripe);
	
	public List<TaAbonnementDTO> selectAllASuspendre();
	public TaAbonnement findByIdLEcheance(Integer idLEcheance);
	
	public List<TaAbonnementDTO> findAllEnCoursDTOArrivantAEcheanceDansXJours(Integer nbJours, String codeTiers);
	public List<TaLEcheance> genereProchainesEcheances(TaAbonnementDTO taAbonnement);
	
	public List<TaAbonnement> findAllEnCours();
	public Integer supprimeAvisEcheanceFaux();
	public Integer supprimeEcheanceEnCours();
	public Integer supprimeTousAvisEcheance();
	
	public void supprimeTousRDocumentEvenementAvisEcheance();
	public void metADispositionTousAvisEcheance();
	
	public void insertionLigneALigneEcheanceAvis();	
	public void insertionLigneALigneEcheanceFacture();	
	public void insertionLigneALigneClassiqueAboAvis();
	
	public void addTaJourRelance();
	
	public void insertionPeriode();
	public void insertionPeriodeV2();
	public void annuleLigneAbo();
	public List<TaAbonnementDTO> findAllAnnuleDTO(String codeTiers);
	
	public List<TaAbonnement>  findAllSansDatesPeriode();
	public Integer donneEtatEnCoursLigneAboSansEtat();
	


}
