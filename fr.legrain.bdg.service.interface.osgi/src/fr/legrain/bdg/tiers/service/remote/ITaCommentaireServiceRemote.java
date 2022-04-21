package fr.legrain.bdg.tiers.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tiers.dto.TaCommentaireDTO;
import fr.legrain.tiers.model.TaCommentaire;

@Remote
public interface ITaCommentaireServiceRemote extends IGenericCRUDServiceRemote<TaCommentaire,TaCommentaireDTO>,
														IAbstractLgrDAOServer<TaCommentaire>,IAbstractLgrDAOServerDTO<TaCommentaireDTO>{
	public static final String validationContext = "COMMENTAIRE";
}
