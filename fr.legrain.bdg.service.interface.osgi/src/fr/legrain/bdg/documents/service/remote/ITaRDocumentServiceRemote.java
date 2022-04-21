package fr.legrain.bdg.documents.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.IDocumentTiersDTO;
import fr.legrain.document.dto.TaRDocumentDTO;
import fr.legrain.document.model.TaRDocument;

@Remote
public interface ITaRDocumentServiceRemote extends IGenericCRUDServiceRemote<TaRDocument,TaRDocumentDTO>,
IAbstractLgrDAOServer<TaRDocument>,IAbstractLgrDAOServerDTO<TaRDocumentDTO>,
IDocumentService<TaRDocument>, IDocumentTiersStatistiquesService<TaRDocument>,
IDocumentCodeGenere {
	
	public List<TaRDocument> dejaGenere(String requete);
	public List<TaRDocumentDTO> dejaGenereDocument(String requete);
}
