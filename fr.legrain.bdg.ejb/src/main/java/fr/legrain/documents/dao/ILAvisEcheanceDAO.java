package fr.legrain.documents.dao;

import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.model.TaLAvisEcheance;

//@Remote
public interface ILAvisEcheanceDAO extends IGenericDAO<TaLAvisEcheance> {
	
	public List<TaLAvisEcheance> findAllByIdEcheance(Integer id);
	public List<TaLAvisEcheance> findAllByIdEcheanceSansLigneCom(Integer id);

}
