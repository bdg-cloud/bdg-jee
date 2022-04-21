package fr.legrain.bdg.general.service.remote;

import java.util.List;
import java.util.Map;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import fr.legrain.edition.model.TaActionEdition;

//@Remote
public interface IGenericCRUDServiceDocumentRemote<Entity, DTO> extends IGenericCRUDServiceRemoteDTO<DTO>, IGenericCRUDServiceRemoteEntity<Entity> {
	public String generePDF(int idDoc, Map<String,Object> mapParametreEdition, List<String> listResourcesPath, String theme);
	public String generePDF(int idDoc, String modeleEdition, Map<String,Object> mapParametreEdition, List<String> listResourcesPath, String theme);
	public String generePDF(int idDoc, Map<String,Object> mapParametreEdition, List<String> listResourcesPath, String theme, TaActionEdition action);

	public Entity findByCodeFetch(String code) throws FinderException;
	public Entity findByIDFetch(int id) throws FinderException;	
	public void remove(Entity persistentInstance,boolean recharger) throws RemoveException;

}
