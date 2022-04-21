package fr.legrain.bdg.compteclient.dao.droits;

import fr.legrain.data.IGenericDAO;
import fr.legrain.bdg.compteclient.model.droits.TaUtilisateur;

//@Remote
public interface IUtilisateurDAO extends IGenericDAO<TaUtilisateur> {

	public TaUtilisateur ctrlSaisieEmail(String email);
		
	
}

