package fr.legrain.bdg.compteclient.dao;


import java.util.List;

import fr.legrain.bdg.compteclient.model.TaMesFournisseurs;
import fr.legrain.data.IGenericDAO;

public interface IMesFournisseursDAO extends IGenericDAO<TaMesFournisseurs> {
	public TaMesFournisseurs findInstance();
	public List<TaMesFournisseurs> rechercheMesFournisseurs(int idUtilisateur);
	public boolean verifieSiLiaisonClientFournisseurExisteDeja(int idUtilisateur, String codeClient);
}
