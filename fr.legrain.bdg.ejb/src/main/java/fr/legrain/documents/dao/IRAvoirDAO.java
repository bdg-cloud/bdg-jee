package fr.legrain.documents.dao;

import java.util.Date;
import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.model.TaRAvoir;
import fr.legrain.tiers.model.TaTiers;

//@Remote
public interface IRAvoirDAO extends IGenericDAO<TaRAvoir> {
	public List<TaRAvoir> selectAll(TaTiers taTiers,Date dateDeb,Date dateFin);
}
