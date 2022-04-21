package fr.legrain.documents.dao;

import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.dto.TaLRemiseDTO;
import fr.legrain.document.model.TaLRemise;

//@Remote
public interface ILRemiseDAO extends IGenericDAO<TaLRemise> {

	public List<TaLRemiseDTO> RechercheLigneRemiseDTO(String codeRemise);
}
