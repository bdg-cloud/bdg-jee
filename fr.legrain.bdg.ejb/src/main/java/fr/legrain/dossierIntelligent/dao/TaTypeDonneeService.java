package fr.legrain.dossierIntelligent.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.bdg.dossierIntelligent.service.remote.ITaTypeDonneeServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaTypeDonneeMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.dossierIntelligent.dto.TaTypeDonneeDTO;
import fr.legrain.dossierIntelligent.model.TaTypeDonnee;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaTypeDonneeService extends AbstractApplicationDAOServer<TaTypeDonnee, TaTypeDonneeDTO> implements ITaTypeDonneeServiceRemote {

	static Logger logger = Logger.getLogger(TaTypeDonneeService.class);

	@Inject private ITaTypeDonneeDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaTypeDonneeService() {
		super(TaTypeDonnee.class,TaTypeDonneeDTO.class);
	}
	


	
	public void persist(TaTypeDonnee transientInstance) throws CreateException {
		persist(transientInstance, null);
	}


	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTypeDonnee transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaTypeDonnee persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getId()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaTypeDonnee merge(TaTypeDonnee detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTypeDonnee merge(TaTypeDonnee detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaTypeDonnee findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTypeDonnee findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}


	public List<TaTypeDonnee> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTypeDonneeDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTypeDonneeDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTypeDonnee> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTypeDonneeDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTypeDonneeDTO entityToDTO(TaTypeDonnee entity) {

		TaTypeDonneeMapper mapper = new TaTypeDonneeMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTypeDonneeDTO> listEntityToListDTO(List<TaTypeDonnee> entity) {
		List<TaTypeDonneeDTO> l = new ArrayList<TaTypeDonneeDTO>();

		for (TaTypeDonnee taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

	public List<TaTypeDonneeDTO> selectAllDTO() {
		System.out.println("List of TaTypeDonneeDTO EJB :");
		ArrayList<TaTypeDonneeDTO> liste = new ArrayList<TaTypeDonneeDTO>();

		List<TaTypeDonnee> projects = selectAll();
		for(TaTypeDonnee project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTypeDonneeDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTypeDonneeDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTypeDonneeDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTypeDonneeDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTypeDonneeDTO dto, String validationContext) throws EJBException {
		try {
			TaTypeDonneeMapper mapper = new TaTypeDonneeMapper();
			TaTypeDonnee entity = null;
			if(dto.getId()!=0) {
				entity = dao.findById(dto.getId());
				if(dto.getVersionObj()!=entity.getVersionObj()) {
					throw new OptimisticLockException(entity,
							"L'objet à été modifié depuis le dernier accés. Client ID : "+dto.getId()+" - Client Version objet : "+dto.getVersionObj()+" -Serveur Version Objet : "+entity.getVersionObj());
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
	
	public void persistDTO(TaTypeDonneeDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTypeDonneeDTO dto, String validationContext) throws CreateException {
		try {
			TaTypeDonneeMapper mapper = new TaTypeDonneeMapper();
			TaTypeDonnee entity = mapper.mapDtoToEntity(dto,null);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTypeDonneeDTO dto) throws RemoveException {
		try {
			TaTypeDonneeMapper mapper = new TaTypeDonneeMapper();
			TaTypeDonnee entity = mapper.mapDtoToEntity(dto,null);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTypeDonnee refresh(TaTypeDonnee persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTypeDonnee value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTypeDonnee value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTypeDonneeDTO dto, String validationContext) {
		try {
			TaTypeDonneeMapper mapper = new TaTypeDonneeMapper();
			TaTypeDonnee entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			

		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTypeDonneeDTO dto, String propertyName, String validationContext) {
		try {
			TaTypeDonneeMapper mapper = new TaTypeDonneeMapper();
			TaTypeDonnee entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);

		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTypeDonneeDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTypeDonneeDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTypeDonnee value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTypeDonnee value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}



}
