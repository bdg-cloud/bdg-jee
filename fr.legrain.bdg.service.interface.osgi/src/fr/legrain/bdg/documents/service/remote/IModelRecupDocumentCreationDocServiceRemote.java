package fr.legrain.bdg.documents.service.remote;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Remote;

import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IDocumentTiersDTO;
import fr.legrain.tiers.model.TaTiers;

@Remote
public interface IModelRecupDocumentCreationDocServiceRemote {

	public LinkedList<IDocumentTiers> remplirListe(Date dateDeb,Date dateFin,List<TaTiers> listeTiers,String selectedTypeSelection,
			String selectedTypeCreation);
	public LinkedList<IDocumentTiers> remplirListe(Date dateDeb,Date dateFin,String codeTiers,String selectedTypeSelection,
			String selectedTypeCreation,IDocumentTiers doc);
	public LinkedList<DocumentDTO> remplirListe();
	public Boolean rechercheRDocument(IDocumentTiers doc, String selectedTypeDoc);
	public LinkedList<DocumentDTO> getListeObjet() ;
	
	public Collection<IDocumentTiers> getListeEntity() ;
	
	public IModelCreationDocServiceRemote getModelCreationDoc();
}
