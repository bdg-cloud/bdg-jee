package fr.legrain.bdg.documents.service.remote;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.FinderException;
import javax.ejb.Remote;

//import fr.legrain.abonnement.stripe.model.TaStripeSubscription;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaLAvisEcheanceDTO;
import fr.legrain.document.dto.TaLEcheanceDTO;
import fr.legrain.document.model.TaAbonnement;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaLAbonnement;
import fr.legrain.document.model.TaLAvisEcheance;
import fr.legrain.document.model.TaLEcheance;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaLFacture;

@Remote
public interface ITaLEcheanceServiceRemote extends IGenericCRUDServiceRemote<TaLEcheance,TaLEcheanceDTO>,
														IAbstractLgrDAOServer<TaLEcheance>,IAbstractLgrDAOServerDTO<TaLEcheanceDTO>{
	public static final String validationContext = "L_ECHEANCE";
	
	public List<TaLEcheance> rechercheEcheanceLieAAbonnement(TaAbonnement taAbonnement);
	public List<TaLEcheance> rechercheEcheanceNonLieAAvisEcheanceTiers();
	public List<TaLEcheance> rechercheEcheanceNonLieAAvisEcheanceTiers(String codeTiers);
//	public List<TaLEcheance> rechercheEcheanceNonLieAAvisEcheanceSubscription();
//	public List<TaLEcheance> rechercheEcheanceNonLieAAvisEcheanceSubscription(TaStripeSubscription taStripeSubscription);
	
	public List<TaLEcheance> rechercheEcheanceNonLieAAvisEcheanceAbonnement();
	public List<TaLEcheance> rechercheEcheanceNonLieAAvisEcheanceAbonnement(TaAbonnement taAbonnement);
	public List<TaLEcheance> rechercheEcheanceLightNonLieAAvisEcheanceAbonnement();
	
	//public Integer countAllByIdSubscriptionItem(Integer idSubscriptionItem);
	public Integer countAllByIdLigneAbo(Integer idLigneAbo);
	
	public TaLEcheance findByIdFetchLigneAbo(int id) throws FinderException;
	public TaLAbonnement fetchLigneAbo(int id) throws FinderException;
	
	public TaLEcheance findByIdLAvisEcheance(Integer idLAvisEcheance);
	public TaLEcheance findByIdLFacture(Integer idLFacture);
	public List<TaLEcheance> findAllByIdLAvisEcheance(Integer idLAvisEcheance);
	public List<TaLEcheance> findAllByIdLFacture(Integer idLFacture);
	
	public long countTiersEcheanceNonLieAAvisEcheance();
	public BigDecimal montantHtApresRemiseEcheanceNonLieAAvisEcheanceTiers(String codeTiers);
	public long countEcheanceNonLieAAvisEcheanceTiers(String codeTiers);
	
	public TaLEcheance donneEtat(TaLEcheance detachedInstance, TaEtat etat);
	public List<TaLEcheance> rechercheEcheanceEnCoursTiers(String codeTiers);
	
	public List<TaLEcheance> findAllEnCoursDepasse();
	public List<TaLEcheance> rechercheEcheanceEnCoursOuSuspendusTiers(String codeTiers) ;
	public List<TaLEcheance> rechercheEcheanceSuspendusTiers(String codeTiers);
	
	public List<TaLEcheance> rechercheEcheanceEnCoursOuSuspendusByIdLAbonnement(Integer id);
	public TaLEcheance donneEtatSuspendu(TaLEcheance detachedInstance);
	
	public List<TaLEcheance> rechercheEcheanceByIdLAbonnement(Integer id);
	public List<TaLEcheance> rechercheEcheanceEnCoursByIdLAbonnement(Integer id);
	public List<TaLEcheance> rechercheEcheanceEnCoursByIdAbonnement(Integer id);
	public void regleEcheances(List<TaLEcheance> liste);
	public List<TaLEcheance> rechercheEcheanceEnCoursOuSuspendusTiersAndByCodeTPaiement(String codeTiers, String codeTPaiement);
	public List<TaLEcheance> findAllSuspendues();
	public void supprimeEcheanceDelaiSurvieDepasse();
	public void suspendEcheances();
	public void transformeEcheance();
	
	public List<TaLEcheance> findAllLieAAvisEcheanceTiers(String codeTiers);
	public List<TaLEcheance> findAllLieAAvisEcheancePayeTiers(String codeTiers);
	public List<TaLEcheance> findAllNonLieAAvisEcheancePayeTiers(String codeTiers);
	public void donneEtatEnCours();
	public void suppressionEcheanceDans60Jours();
	public List<TaLEcheance> findAllEnCoursArrivantAEcheanceDans60Jours();
	public List<TaLEcheance> findAllEcheanceNonTotTransforme();
	
	public List<TaLEcheance> rechercheEcheanceBycodeAvisEcheance(String code);
	
//	public void supprimeLiaisonsAvisEcheance(TaAvisEcheance avis);
//	public void supprimeLiaisonsAvisEcheance(TaLAvisEcheance ligne);
//	public void supprimeLiaisonsFacture(TaFacture facture);
//	public void supprimeLiaisonsFacture(TaLFacture ligne);
	
	public List<TaLEcheance> selectAllEcheanceARelancer(String codeTiers);
	
	public void suppressionEcheanceAnnule();
	public List<TaLEcheance> findAllEnCoursByIdLAbonnement(Integer id);
	public List<TaLEcheance> findAllEnCoursOuSuspenduByIdLAbonnement(Integer id);
	
	public List<TaLEcheance> findAllEcheanceEnCoursOuSuspendueModuleBDG(Integer idTiers);
	public List<TaLEcheanceDTO> findAllEcheanceEnCoursOuSuspendueModuleBDGDTO(Integer idTiers);
	public List<TaLEcheanceDTO> findAllEcheanceEnCoursOuSuspendueModuleBDGDTO();
	public List<TaLEcheanceDTO> findAllEcheanceByCodeEtatsAndByIdTiersDTO(List<String> etats, Boolean codeModuleBDG, Integer idTiers);
	
	public Integer supprimeEcheanceSansEtat();
}
