package fr.legrain.bdg.tache.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tache.dto.TaRecurrenceEvenementDTO;
import fr.legrain.tache.model.TaRecurrenceEvenement;

@Remote
public interface ITaRecurrenceEvenementServiceRemote extends IGenericCRUDServiceRemote<TaRecurrenceEvenement,TaRecurrenceEvenementDTO>,
														IAbstractLgrDAOServer<TaRecurrenceEvenement>,IAbstractLgrDAOServerDTO<TaRecurrenceEvenementDTO>{
	public static final String validationContext = "RECURRENCE_EVENEMENT";
}
