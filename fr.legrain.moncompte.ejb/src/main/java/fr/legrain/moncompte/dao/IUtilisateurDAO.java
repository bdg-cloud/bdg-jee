package fr.legrain.moncompte.dao;

import fr.legrain.moncompte.data.IGenericDAO;
import fr.legrain.moncompte.model.TaUtilisateur;

//@Remote
public interface IUtilisateurDAO extends IGenericDAO<TaUtilisateur> {
	public TaUtilisateur findByEmail(String email);
}
