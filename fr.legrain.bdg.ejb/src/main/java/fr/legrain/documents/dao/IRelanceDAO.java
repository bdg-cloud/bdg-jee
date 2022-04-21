package fr.legrain.documents.dao;

import javax.ejb.FinderException;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.TaRelanceDTO;
import fr.legrain.document.dto.TaRemiseDTO;
import fr.legrain.document.model.TaRelance;
import fr.legrain.document.model.TaTRelance;

//@Remote
public interface IRelanceDAO extends IGenericDAO<TaRelance>{

	public TaTRelance maxTaTRelance(IDocumentTiers taDocument);
	public TaRelanceDTO findByCodeDTO(String code)throws FinderException;
	
}
