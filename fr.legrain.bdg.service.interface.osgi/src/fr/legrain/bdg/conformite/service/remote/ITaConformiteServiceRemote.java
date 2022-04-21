package fr.legrain.bdg.conformite.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.documents.service.remote.IDocumentCodeGenere;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.conformite.dto.TaConformiteDTO;
import fr.legrain.conformite.model.TaConformite;


@Remote
public interface ITaConformiteServiceRemote extends IGenericCRUDServiceRemote<TaConformite,TaConformiteDTO>,
														IAbstractLgrDAOServer<TaConformite>,IAbstractLgrDAOServerDTO<TaConformiteDTO>,
														IDocumentCodeGenere{
	public static final String validationContext = "CONFORMITE";
	
	public Boolean controleUtiliseDansUnLot(int idControleConformite);
	public List<TaConformite> controleArticleDerniereVersion(int idArticle);
}
