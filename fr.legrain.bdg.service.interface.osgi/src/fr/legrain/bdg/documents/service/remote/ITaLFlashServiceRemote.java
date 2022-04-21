package fr.legrain.bdg.documents.service.remote;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaLFlashDTO;
import fr.legrain.document.dto.TaLigneALigneDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.TaLFactureDTO;
import fr.legrain.document.model.TaLFlash;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaFlash;
import fr.legrain.document.model.TaLFacture;

@Remote
public interface ITaLFlashServiceRemote extends IGenericCRUDServiceRemote<TaLFlash,TaLFlashDTO>,
														IAbstractLgrDAOServer<TaLFlash>,IAbstractLgrDAOServerDTO<TaLFlashDTO>{
	public static final String validationContext = "L_FLASH";
	
	
	public List<TaLigneALigneDTO> selectLigneDocNonAffecte(TaFlash doc ,String codeTiers,String typeOrigine,String typeDest,Date debut,Date fin,TaEtat etat,String tEtat);
	
}
