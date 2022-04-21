package fr.legrain.bdg.documents.service.remote;

import java.util.Collection;
import java.util.LinkedList;

import fr.legrain.document.dto.CreationDocLigneDTO;
import fr.legrain.document.model.TaCreationDoc;

public interface IModelCreationDocServiceRemote {

	public LinkedList<CreationDocLigneDTO> remplirListe();
	public Collection<TaCreationDoc> getListeEntity();
	public LinkedList<CreationDocLigneDTO> getListeObjet();
	public void setListeEntity(Collection<TaCreationDoc> listeEntity);
}
