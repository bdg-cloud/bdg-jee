package fr.legrain.documents.dao;

import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaTDoc;
import fr.legrain.document.model.TaTEtat;

//@Remote
public interface IEtatDAO extends IGenericDAO<TaEtat> {


	
	public List<TaEtat> findByIdentifiantAndTypeEtat(String identifiant, String typeEtat);


}
