package fr.legrain.documents.dao;

import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.dto.TaRDocumentDTO;
import fr.legrain.document.model.TaRDocument;

//@Remote
public interface IRDocumentDAO extends IGenericDAO<TaRDocument> {

	public List<TaRDocument> dejaGenere(String requete);
	public List<TaRDocumentDTO> dejaGenereDocument(String requete);
}
