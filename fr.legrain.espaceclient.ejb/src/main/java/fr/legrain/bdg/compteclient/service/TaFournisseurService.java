package fr.legrain.bdg.compteclient.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebMethod;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.bdg.compteclient.dao.IFournisseurDAO;
import fr.legrain.bdg.compteclient.dto.TaFournisseurDTO;
import fr.legrain.bdg.compteclient.model.TaFournisseur;
import fr.legrain.bdg.compteclient.model.mapping.mapper.TaFournisseurMapper;
import fr.legrain.bdg.compteclient.service.interfaces.remote.ITaFournisseurServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;


@Stateless // Implémente les interfaces distante et locale précédemment définies.
public class TaFournisseurService extends AbstractApplicationDAOServer<TaFournisseur, TaFournisseurDTO> implements ITaFournisseurServiceRemote{

	static Logger logger = Logger.getLogger(TaFournisseurService.class);

	@Inject private IFournisseurDAO dao;

	/**
	* Default constructor. 
	*/
	public TaFournisseurService() {
		super(TaFournisseur.class,TaFournisseurDTO.class);
	}
	
	public TaFournisseur findInstance() {
		return dao.findInstance();
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaFournisseur transientInstance) throws CreateException {
		persist(transientInstance, null);
	}
		
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaFournisseur transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaFournisseur persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaFournisseur merge(TaFournisseur detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaFournisseur merge(TaFournisseur detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaFournisseur findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaFournisseur findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

	public List<TaFournisseur> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaFournisseurDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaFournisseurDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaFournisseur> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaFournisseurDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaFournisseurDTO entityToDTO(TaFournisseur entity) {
		TaFournisseurMapper mapper = new TaFournisseurMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaFournisseurDTO> listEntityToListDTO(List<TaFournisseur> entity) {
		List<TaFournisseurDTO> l = new ArrayList<TaFournisseurDTO>();

		for (TaFournisseur TaFournisseur : entity) {
			l.add(entityToDTO(TaFournisseur));
		}

		return l;
	}

	public List<TaFournisseurDTO> selectAllDTO() {
		System.out.println("List of TaFournisseurDTO EJB :");
		ArrayList<TaFournisseurDTO> liste = new ArrayList<TaFournisseurDTO>();

		List<TaFournisseur> projects = selectAll();
		for(TaFournisseur project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaFournisseurDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaFournisseurDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaFournisseurDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
	}
	
	public void mergeDTO(TaFournisseurDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaFournisseurDTO dto, String validationContext) throws EJBException {
		try {
			TaFournisseurMapper mapper = new TaFournisseurMapper();
			TaFournisseur entity = null;
			if(dto.getIdFournisseur()!=0) {
				entity = dao.findById(dto.getIdFournisseur());
				if(dto.getVersionObj()!=entity.getVersionObj()) {
					throw new OptimisticLockException(entity,
							"L'objet à été modifié depuis le dernier accés. Client ID : "+dto.getIdFournisseur()+" - Client Version objet : "+dto.getVersionObj()+" -Serveur Version Objet : "+entity.getVersionObj());
				} else {
					entity = mapper.mapDtoToEntity(dto,entity);
				}
			}
			
			dao.detach(entity); //pour passer les controles
			enregistrerMerge(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}
	
	public void persistDTO(TaFournisseurDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaFournisseurDTO dto, String validationContext) throws CreateException {
		try {
			TaFournisseurMapper mapper = new TaFournisseurMapper();
			TaFournisseur entity = mapper.mapDtoToEntity(dto,null);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaFournisseurDTO dto) throws RemoveException {
		try {
			TaFournisseurMapper mapper = new TaFournisseurMapper();
			TaFournisseur entity = mapper.mapDtoToEntity(dto,null);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaFournisseur refresh(TaFournisseur persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaFournisseur value, String validationContext) /*throws ExceptLgr*/ {
		try {
			if(validationContext==null) validationContext="";
			validateAll(value,validationContext,false); //ancienne validation, extraction de l'annotation et appel
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateEntityPropertyValidationContext")
	public void validateEntityProperty(TaFournisseur value, String propertyName, String validationContext) {
		try {
			if(validationContext==null) validationContext="";
			validate(value, propertyName, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOValidationContext")
	public void validateDTO(TaFournisseurDTO dto, String validationContext) {
		try {
			TaFournisseurMapper mapper = new TaFournisseurMapper();
			TaFournisseur entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaFournisseurDTO dto, String propertyName, String validationContext) {
		try {
			TaFournisseurMapper mapper = new TaFournisseurMapper();
			TaFournisseur entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaFournisseurDTO dto) {
		validateDTO(dto,null);
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaFournisseurDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaFournisseur value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaFournisseur value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}
	public TaFournisseur findByCodeDossier(String codeDossier) {
		return dao.findByCodeDossier(codeDossier);
	}

}


