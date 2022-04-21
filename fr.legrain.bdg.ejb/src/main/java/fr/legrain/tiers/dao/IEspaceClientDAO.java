package fr.legrain.tiers.dao;

import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.tiers.dto.TaEspaceClientDTO;
//import javax.ejb.Remote;
import fr.legrain.tiers.model.TaEspaceClient;

//@Remote
public interface IEspaceClientDAO extends IGenericDAO<TaEspaceClient> {
	public TaEspaceClient login(String login, String password);
	public TaEspaceClientDTO loginDTO(String login, String password);
	
	public TaEspaceClient findByCodeTiers(String codeTiers);
	public List<TaEspaceClientDTO> findAllLight();
}
