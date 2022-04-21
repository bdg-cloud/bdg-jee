package fr.legrain.bdg.tiers.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tiers.dto.TaTCiviliteDTO;
import fr.legrain.tiers.model.TaTCivilite;

@Remote
/*
public interface ITaTCiviliteServiceRemote {
	public void persist(TaTCivilite transientInstance) throws CreateException;
	public void remove(TaTCivilite persistentInstance) throws RemoveException;
	public TaTCivilite merge(TaTCivilite detachedInstance);
	public TaTCivilite findById(int id) throws FinderException;
	public TaTCivilite findByCode(String code) throws FinderException;
	public List<TaTCivilite> selectAll();
	
	public List<TaTCiviliteDTO> selectAllDTO();
	
	public void error(TaTCiviliteDTO dto);
}
*/
public interface ITaTCiviliteServiceRemote extends IGenericCRUDServiceRemote<TaTCivilite,TaTCiviliteDTO>,
														IAbstractLgrDAOServer<TaTCivilite>,IAbstractLgrDAOServerDTO<TaTCiviliteDTO>{
	public static final String validationContext = "T_CIVILITE";
}
