package fr.legrain.article.dao;

import java.util.Date;
import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.stock.dto.GrMouvStockDTO;
import fr.legrain.stock.model.TaGrMouvStock;
import fr.legrain.stock.model.TaMouvementStock;

//@Remote
public interface IGrMouvStockDAO extends IGenericDAO<TaGrMouvStock> {

	public List<TaMouvementStock> findAllWithDates(Date dateDebut, Date dateFin);
	
	public List<GrMouvStockDTO> findByCodeLight(String code);
	public List<GrMouvStockDTO> findAllLight();

}
