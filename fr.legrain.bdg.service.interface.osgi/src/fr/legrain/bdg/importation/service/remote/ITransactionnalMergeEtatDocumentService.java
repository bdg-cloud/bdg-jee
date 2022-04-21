package fr.legrain.bdg.importation.service.remote;

import java.util.List;

import fr.legrain.document.dto.IDocumentDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.model.TaRDocument;

public interface ITransactionnalMergeEtatDocumentService {
	public void mergeEtatDocument(IDocumentDTO doc) throws Exception;
	public IDocumentTiers mergeEtatDocument(IDocumentTiers doc, boolean avecCommit) throws Exception;
	public void mergeEtatDocument(IDocumentTiers doc) throws Exception;
//	public void updateLigneALigne_etatDocument(TaRDocument o) throws Exception;
	public List<IDocumentDTO> recupListeLigneALigne()throws Exception;
}
