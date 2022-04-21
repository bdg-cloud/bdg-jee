package fr.legrain.bdg.documents.service.remote;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Remote;

import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaLigneALigneDTO;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaFlash;
import fr.legrain.document.model.TaLFlash;
import fr.legrain.tiers.model.TaTiers;

@Remote
public interface IModelRecupLigneFlashCreationDocServiceRemote {

	public LinkedList<TaLFlash> remplirListe(Date dateDeb,Date dateFin,List<TaTiers> listeTiers,String selectedTypeSelection,
			String selectedTypeCreation);


	public LinkedList<TaLigneALigneDTO> remplirListe(Date dateDeb,Date dateFin,String codeTiers,String selectedTypeSelection,
			String selectedTypeCreation,TaFlash doc,TaEtat etat);
	public LinkedList<TaLigneALigneDTO> remplirListe();
	public Boolean rechercheRDocument(TaLFlash doc, String selectedTypeDoc);
	public LinkedList<TaLigneALigneDTO> getListeObjet() ;
	
	public Collection<TaLFlash> getListeEntity() ;
	
	public IModelCreationDocServiceRemote getModelCreationDoc();
}
