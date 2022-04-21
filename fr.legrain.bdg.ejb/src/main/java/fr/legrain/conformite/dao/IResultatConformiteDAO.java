package fr.legrain.conformite.dao;

import java.util.List;

import fr.legrain.conformite.model.TaResultatConformite;
import fr.legrain.conformite.model.TaStatusConformite;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface IResultatConformiteDAO extends IGenericDAO<TaResultatConformite> {
	
	public TaResultatConformite findByLotAndControleConformite(int idLot, int idControleConformite);
	public List<TaResultatConformite> findByLotAndControleConformiteHistorique(int idLot, int idControleConformite);
	
	public TaStatusConformite etatLot(int idLot);
	public TaStatusConformite etatLot(int idLot, boolean priseEnCompteDesControleFactultatif);
	public TaStatusConformite etatLotPourLesControlesObligatoires(int idLot);
	public TaStatusConformite etatLotPourLesControlesFacultatif(int idLot);
	
}
