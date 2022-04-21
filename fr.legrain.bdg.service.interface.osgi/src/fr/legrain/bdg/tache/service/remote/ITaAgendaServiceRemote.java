package fr.legrain.bdg.tache.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.tache.dto.TaAgendaDTO;
import fr.legrain.tache.model.TaAgenda;

@Remote
public interface ITaAgendaServiceRemote extends IGenericCRUDServiceRemote<TaAgenda,TaAgendaDTO>,
														IAbstractLgrDAOServer<TaAgenda>,IAbstractLgrDAOServerDTO<TaAgendaDTO>{
	public static final String validationContext = "AGENDA";
	
	public List<TaAgenda> findAgendaUtilisateur(TaUtilisateur utilisateur);
}
