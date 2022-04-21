package fr.legrain.abonnement.stripe.dao;

import java.util.List;

import fr.legrain.abonnement.dto.TaAbonnementFullDTO;
import fr.legrain.abonnement.stripe.model.TaStripeSubscription_old;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface IStripeSubscriptionDAO extends IGenericDAO<TaStripeSubscription_old> {

//	public List<TaStripeSubscriptionDTO> findByCodeLight(String code);
//	public List<TaStripeSubscriptionDTO> findAllLight();
//	
//	public TaStripeSubscriptionDTO findDTOByIdAbonnement(Integer idAbonnement);
//	public List<TaStripeSubscriptionDTO> findAllDTOByIdTiers(Integer idTiers);
//	public List<TaStripeSubscriptionDTO> selectAllASuspendre();
//	public List<TaAbonnementFullDTO> findAllFullDTOByIdTiers(Integer idTiers);
//	public TaStripeSubscription_old findByIdLEcheance(Integer idLEcheance);
//	
//	public List<TaStripeSubscriptionDTO> rechercherSubscriptionCustomerDTO(String idExterneCustomer);
//	public List<TaStripeSubscriptionDTO> rechercherSubscriptionNonStripeCustomerDTO(String idExterneCustomer);
//	public List<TaStripeSubscription_old> rechercherSubscriptionCustomer(String idExterneCustomer);
//	
//	public List<TaStripeSubscriptionDTO> rechercherSubscriptionPlanDTO(String idExternePlan);
//	public List<TaStripeSubscription_old> rechercherSubscriptionPlan(String idExternePlan);
}
