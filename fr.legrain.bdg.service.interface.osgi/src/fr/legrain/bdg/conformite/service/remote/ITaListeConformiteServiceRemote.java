package fr.legrain.bdg.conformite.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.conformite.dto.TaListeConformiteDTO;
import fr.legrain.conformite.model.TaConformite;
import fr.legrain.conformite.model.TaListeConformite;

@Remote
public interface ITaListeConformiteServiceRemote extends IGenericCRUDServiceRemote<TaListeConformite,TaListeConformiteDTO>,
														IAbstractLgrDAOServer<TaListeConformite>,IAbstractLgrDAOServerDTO<TaListeConformiteDTO>{
	public static final String validationContext = "LISTE_CONFORMITE";
	
	public List<TaConformite>  findControleConformiteByCodeArticle(String codeArticle);
	
	public List<TaListeConformite>  findByCodeArticle(String codeArticle);
}
