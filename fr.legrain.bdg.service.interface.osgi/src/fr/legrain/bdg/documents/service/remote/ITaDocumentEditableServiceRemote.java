package fr.legrain.bdg.documents.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.DocumentEditableDTO;
import fr.legrain.document.dto.TaReglementDTO;
import fr.legrain.document.model.TaDocumentEditable;
import fr.legrain.document.model.TaReglement;

@Remote
public interface ITaDocumentEditableServiceRemote extends IGenericCRUDServiceRemote<TaDocumentEditable,DocumentEditableDTO>,
IAbstractLgrDAOServer<TaDocumentEditable>,IAbstractLgrDAOServerDTO<DocumentEditableDTO>,
IDocumentService<TaDocumentEditable>{
	public List<TaDocumentEditable> findByCodeTypeDoc(String codeTypeDoc);
}
