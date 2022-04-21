package fr.legrain.moncompte.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import fr.legrain.moncompte.data.IGenericDAO;
import fr.legrain.moncompte.model.TaPanier;

//@Remote
public interface IPanierDAO extends IGenericDAO<TaPanier> {

	public List<TaPanier> findPanierDossier(String codeDossier);

	public List<TaPanier> findPanierClient(String codeClient);
	
	public List<TaPanier> findPanierDate(Date debut, Date fin);
}
