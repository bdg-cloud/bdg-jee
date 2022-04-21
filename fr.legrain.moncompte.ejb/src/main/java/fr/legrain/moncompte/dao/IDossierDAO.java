package fr.legrain.moncompte.dao;

import java.util.List;

import javax.ejb.RemoveException;

import fr.legrain.moncompte.data.IGenericDAO;
import fr.legrain.moncompte.dto.TaDossierDTO;
import fr.legrain.moncompte.model.TaAutorisation;
import fr.legrain.moncompte.model.TaClient;
import fr.legrain.moncompte.model.TaDossier;
import fr.legrain.moncompte.model.TaPrixPerso;

//@Remote
public interface IDossierDAO extends IGenericDAO<TaDossier> {
	public TaClient findClientDossier(String codeDossier);
	
	public List<TaDossier> findListeDossierClient(int idClient);
	public List<TaDossier> findListeDossierProduit(String idProduit);
	
	public void removeLigneAutorisation(TaDossier persistentInstance, TaAutorisation ligne);
	public void removeLignePrixPerso(TaDossier persistentInstance, TaPrixPerso ligne)throws RemoveException;
	
	public List<TaDossierDTO> selectAllLight();
}
