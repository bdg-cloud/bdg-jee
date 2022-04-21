package fr.legrain.documents.dao;

import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.model.TaDocumentEditable;

//@Remote
public interface IDocumentEditableDAO extends IGenericDAO<TaDocumentEditable> {
	public List<TaDocumentEditable> findByCodeTypeDoc(String codeTypeDoc);
}
