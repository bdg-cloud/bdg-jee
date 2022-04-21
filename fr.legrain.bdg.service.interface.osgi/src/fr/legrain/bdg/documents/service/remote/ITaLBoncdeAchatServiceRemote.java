package fr.legrain.bdg.documents.service.remote;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.TaLBoncdeAchatDTO;
import fr.legrain.document.dto.TaLigneALigneDTO;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaLBoncdeAchat;
import fr.legrain.document.model.TaTEtat;

@Remote
public interface ITaLBoncdeAchatServiceRemote extends IGenericCRUDServiceRemote<TaLBoncdeAchat,TaLBoncdeAchatDTO>,
														IAbstractLgrDAOServer<TaLBoncdeAchat>,IAbstractLgrDAOServerDTO<TaLBoncdeAchatDTO>{
	public static final String validationContext = "L_BONCDE";
	
//	public List<ILigneDocumentTiers> selectLigneDocNonAffecte2(IDocumentTiers doc ,String codeTiers,String typeOrigine,String typeDest,Date debut,Date fin);
	public List<TaLigneALigneDTO> selectLigneDocNonAffecte(IDocumentTiers doc ,String codeTiers,String typeOrigine,String typeDest,Date debut,Date fin,TaEtat etat,String tEtat);
}
