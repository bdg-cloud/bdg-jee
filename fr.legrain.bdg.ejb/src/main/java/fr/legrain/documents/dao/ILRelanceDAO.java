package fr.legrain.documents.dao;

import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.dto.TaLRelanceDTO;
import fr.legrain.document.model.TaLRelance;

//@Remote
public interface ILRelanceDAO extends IGenericDAO<TaLRelance	> {

	public List<TaLRelanceDTO> rechercheLigneRelanceDTO(String codeRelance);
}
