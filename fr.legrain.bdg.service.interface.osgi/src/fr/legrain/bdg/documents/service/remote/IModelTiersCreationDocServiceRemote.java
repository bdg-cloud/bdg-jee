package fr.legrain.bdg.documents.service.remote;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import javax.ejb.Remote;

import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaTiers;

@Remote
public interface IModelTiersCreationDocServiceRemote {

	public LinkedList<TaTiersDTO> remplirListe(Date dateDeb,Date dateFin,String selectedTypeSelection,String selectedTypeCreation
			,String partieRequeteTiers,IDocumentTiers doc , TaTPaiement taTPaiement);
	public LinkedList<TaTiersDTO> remplirListe();
	public Boolean rechercheRDocument(IDocumentTiers doc, String selectedTypeDoc);
	
	public LinkedList<TaTiersDTO> getListeObjet();
	
	public Collection<TaTiers> getListeEntity() ;
}
