package fr.legrain.article.dao;

import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.stock.dto.InventaireDTO;
import fr.legrain.stock.model.TaInventaire;

//@Remote
public interface IInventaireDAO extends IGenericDAO<TaInventaire> {
	public InventaireDTO findByCodeLight(String code);
	public List<InventaireDTO> findAllLight();
}
