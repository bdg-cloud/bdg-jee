package fr.legrain.abonnement.stripe.dao;

import java.util.List;

import fr.legrain.abonnement.stripe.dto.TaStripeBankAccountDTO;
import fr.legrain.abonnement.stripe.model.TaStripeBankAccount;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface IStripeBankAccountDAO extends IGenericDAO<TaStripeBankAccount> {

	public List<TaStripeBankAccountDTO> findByCodeLight(String code);
	public List<TaStripeBankAccountDTO> findAllLight();
}
