package fr.legrain.tiers.dao;

import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.tiers.model.TaAdresse;
//import javax.ejb.Remote;

//@Remote
public interface IAdresseDAO extends IGenericDAO<TaAdresse> {
	public List<TaAdresse> rechercheParType(String codeType);
	public List<TaAdresse> rechercheParImport(String origineImport, String idImport);
	public int rechercheOdrePourType(String codeType,String codeTiers);
	
}
