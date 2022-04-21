package fr.legrain.abonnement.stripe.dao;

import java.util.Date;
import java.util.List;

import fr.legrain.abonnement.stripe.dto.TaStripePaiementPrevuDTO;
import fr.legrain.abonnement.stripe.model.TaStripePaiementPrevu;
import fr.legrain.data.IGenericDAO;
import fr.legrain.document.model.TaAbonnement;

//@Remote
public interface IStripePaiementPrevuDAO extends IGenericDAO<TaStripePaiementPrevu> {

	public List<TaStripePaiementPrevuDTO> rechercherPaiementPrevuCustomerDTO(String idExterneCustomer);
	public List<TaStripePaiementPrevu> rechercherPaiementPrevuCustomer(String idExterneCustomer);
	
	public List<TaStripePaiementPrevu> findByAbonnementAndDate(TaAbonnement taAbonnement, Date dateEcheance);
}
