package fr.legrain.documents.dao;

import java.util.Date;
import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.TaLigneALigneDTO;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaLBoncdeAchat;
import fr.legrain.document.model.TaTEtat;

//@Remote
public interface ILBoncdeAchatDAO extends IGenericDAO<TaLBoncdeAchat> {

	public List<TaLigneALigneDTO> selectLigneDocNonAffecte(IDocumentTiers doc ,String codeTiers,String typeOrigine,String typeDest,Date debut,Date fin,TaEtat etat,String tEtat);
}
