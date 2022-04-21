package fr.legrain.documents.dao;

import java.util.Date;
import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.dto.TaLigneALigneDTO;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaFlash;
import fr.legrain.document.model.TaLFlash;

//@Remote
public interface ILFlashDAO extends IGenericDAO<TaLFlash> {

	
	public List<TaLigneALigneDTO> selectLigneDocNonAffecte(TaFlash doc ,String codeTiers,String typeOrigine,String typeDest,Date debut,Date fin,TaEtat etat,String tEtat);
	
}
