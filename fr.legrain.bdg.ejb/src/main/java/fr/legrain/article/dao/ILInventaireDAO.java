package fr.legrain.article.dao;

import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.stock.dto.LInventaireDTO;
import fr.legrain.stock.model.TaLInventaire;

//@Remote
public interface ILInventaireDAO extends IGenericDAO<TaLInventaire> {
	public List<LInventaireDTO> findAllLight(Integer idInventaire);
}
