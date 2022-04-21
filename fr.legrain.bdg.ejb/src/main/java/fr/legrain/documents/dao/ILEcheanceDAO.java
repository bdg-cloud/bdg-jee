package fr.legrain.documents.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Query;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.dto.TaLEcheanceDTO;
import fr.legrain.document.model.TaAbonnement;
import fr.legrain.document.model.TaLAvisEcheance;
import fr.legrain.document.model.TaLEcheance;

//@Remote
public interface ILEcheanceDAO extends IGenericDAO<TaLEcheance> {
	
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
	
	public TaLEcheance findByIdLAvisEcheance(Integer idLAvisEcheance);
	public TaLEcheance findByIdLFacture(Integer idLFacture);
	public List<TaLEcheance> findAllByIdLAvisEcheance(Integer idLAvisEcheance);
	public List<TaLEcheance> findAllByIdLFacture(Integer idLFacture);
	
	public long countTiersEcheanceNonLieAAvisEcheance();
	public BigDecimal montantHtApresRemiseEcheanceNonLieAAvisEcheanceTiers(String codeTiers);
	public long countEcheanceNonLieAAvisEcheanceTiers(String codeTiers);
	public List<TaLEcheance> rechercheEcheanceEnCoursTiers(String codeTiers);
	
	public List<TaLEcheance> findAllEnCoursDepasse();
	
	public List<TaLEcheance> rechercheEcheanceEnCoursOuSuspendusTiers(String codeTiers);
	public List<TaLEcheance> rechercheEcheanceSuspendusTiers(String codeTiers);
	public List<TaLEcheance> rechercheEcheanceEnCoursOuSuspendusByIdLAbonnement(Integer id);
	public List<TaLEcheance> rechercheEcheanceByIdLAbonnement(Integer id);
	public List<TaLEcheance> rechercheEcheanceEnCoursByIdLAbonnement(Integer id);
	public List<TaLEcheance> rechercheEcheanceEnCoursByIdAbonnement(Integer id);
	public List<TaLEcheance> rechercheEcheanceEnCoursOuSuspendusTiersAndByCodeTPaiement(String codeTiers, String codeTPaiement);
	public List<TaLEcheance> findAllSuspendues();
	
	public List<TaLEcheance> rechercheEcheanceBycodeAvisEcheance(String code);
	
	public List<TaLEcheance> selectAllEcheanceARelancer(String codeTiers);
	
	public List<TaLEcheance> findAllLieAAvisEcheanceTiers(String codeTiers);
	public List<TaLEcheance> findAllLieAAvisEcheancePayeTiers(String codeTiers);
	public List<TaLEcheance> findAllNonLieAAvisEcheancePayeTiers(String codeTiers);
	
	public List<TaLEcheance> findAllEnCoursArrivantAEcheanceDans60Jours();
	public List<TaLEcheance> findAllEcheanceNonTotTransforme();
	public List<TaLEcheance> findAllEnCoursByIdLAbonnement(Integer id);
	public List<TaLEcheance> findAllEnCoursOuSuspenduByIdLAbonnement(Integer id);
	
	public List<TaLEcheance> findAllEcheanceEnCoursOuSuspendueModuleBDG(Integer idTiers);
	public List<TaLEcheanceDTO> findAllEcheanceEnCoursOuSuspendueModuleBDGDTO(Integer idTiers);
	public List<TaLEcheanceDTO> findAllEcheanceByCodeEtatsAndByIdTiersDTO(List<String> etats, Boolean codeModuleBDG, Integer idTiers);
	
	public List<TaLEcheance> findAllEcheanceSansEtat();
}
