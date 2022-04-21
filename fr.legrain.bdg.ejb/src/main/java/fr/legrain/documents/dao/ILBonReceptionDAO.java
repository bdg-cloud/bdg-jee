package fr.legrain.documents.dao;

import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.model.TaLBonReception;

//@Remote
public interface ILBonReceptionDAO extends IGenericDAO<TaLBonReception> {

	public List<TaLBonReception> findLigneAvecResultatConformites(int idBr) ;
}
