package fr.legrain.moncompte.dao;

import java.util.List;

import fr.legrain.moncompte.data.IGenericDAO;
import fr.legrain.moncompte.model.TaClient;

//@Remote
public interface IClientDAO extends IGenericDAO<TaClient> {
	public List<TaClient> listeDemandePartenariat();
	public List<TaClient> listePartenaire();
	public List<TaClient> listePartenaireType(int idTypePartenaire);
	public String maxCodePartenaire(String debutCode);
	public String maxCodeClient(String debutCode);
	
	public TaClient findByCodePartenaire(String codePartenaire);
}
