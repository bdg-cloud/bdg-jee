package fr.legrain.bdg.tache.service.remote;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tache.dto.TaEvenementDTO;
import fr.legrain.tache.model.TaAgenda;
import fr.legrain.tache.model.TaEvenement;
import fr.legrain.tache.model.TaRDocumentEvenement;

@Remote
public interface ITaEvenementServiceRemote extends IGenericCRUDServiceRemote<TaEvenement,TaEvenementDTO>,
														IAbstractLgrDAOServer<TaEvenement>,IAbstractLgrDAOServerDTO<TaEvenementDTO>{
	public static final String validationContext = "EVENEMENT";
	
	public List<TaEvenement> findByDate(Date debut, Date fin, List<TaAgenda> listeAgenda);
	public List<TaRDocumentEvenement> findListeDocuments(TaEvenement event);
	
}
