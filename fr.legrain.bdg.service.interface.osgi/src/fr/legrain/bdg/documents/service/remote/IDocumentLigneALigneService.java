package fr.legrain.bdg.documents.service.remote;

import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaProformaDTO;

public interface IDocumentLigneALigneService<T> {
	

	
	public T findDocByLDoc(ILigneDocumentTiers lDoc);
	public T mergeEtat(IDocumentTiers detachedInstance);
//	public T mergeEtat(IDocumentTiers detachedInstance,boolean avecLien);
	
}
