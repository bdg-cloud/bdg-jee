package fr.legrain.bdg.generation.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.model.TaLEcheance;

@Remote
public interface IGenereDocLigneEcheanceAbonnementVersPanierServiceRemote extends IAbstractGenereDocLigneEcheanceAbonnementServiceRemote{
	public IDocumentTiers copieDocumentSpecifique(List<TaLEcheance> ds, IDocumentTiers dd
			,Boolean ligneSeparatrice,String libelleSeparateur);
}
