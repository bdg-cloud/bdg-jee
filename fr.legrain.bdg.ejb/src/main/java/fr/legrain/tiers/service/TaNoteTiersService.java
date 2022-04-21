package fr.legrain.tiers.service;

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

import fr.legrain.bdg.model.mapping.mapper.TaNoteTiersMapper;
import fr.legrain.bdg.tiers.service.remote.ITaNoteTiersServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.dao.INoteTiersDAO;
import fr.legrain.tiers.dto.TaNoteTiersDTO;
import fr.legrain.tiers.model.TaNoteTiers;

/**
 * Session Bean implementation class TaNoteTiersBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaNoteTiersService extends AbstractApplicationDAOServer<TaNoteTiers, TaNoteTiersDTO> implements ITaNoteTiersServiceRemote {

	static Logger logger = Logger.getLogger(TaNoteTiersService.class);

	@Inject private INoteTiersDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaNoteTiersService() {
		super(TaNoteTiers.class,TaNoteTiersDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaNoteTiers a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaNoteTiers transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaNoteTiers transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaNoteTiers persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdNoteTiers()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaNoteTiers merge(TaNoteTiers detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaNoteTiers merge(TaNoteTiers detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaNoteTiers findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaNoteTiers findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaNoteTiers> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaNoteTiersDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaNoteTiersDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaNoteTiers> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaNoteTiersDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaNoteTiersDTO entityToDTO(TaNoteTiers entity) {
//		TaNoteTiersDTO dto = new TaNoteTiersDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaNoteTiersMapper mapper = new TaNoteTiersMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaNoteTiersDTO> listEntityToListDTO(List<TaNoteTiers> entity) {
		List<TaNoteTiersDTO> l = new ArrayList<TaNoteTiersDTO>();

		for (TaNoteTiers taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaNoteTiersDTO> selectAllDTO() {
		System.out.println("List of TaNoteTiersDTO EJB :");
		ArrayList<TaNoteTiersDTO> liste = new ArrayList<TaNoteTiersDTO>();

		List<TaNoteTiers> projects = selectAll();
		for(TaNoteTiers project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaNoteTiersDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaNoteTiersDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaNoteTiersDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaNoteTiersDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaNoteTiersDTO dto, String validationContext) throws EJBException {
		try {
			TaNoteTiersMapper mapper = new TaNoteTiersMapper();
			TaNoteTiers entity = null;
			if(dto.getId()!=null) {
				entity = dao.findById(dto.getId());
				if(dto.getVersionObj()!=entity.getVersionObj()) {
					throw new OptimisticLockException(entity,
							"L'objet à été modifié depuis le dernier accés. Client ID : "+dto.getId()+" - Client Version objet : "+dto.getVersionObj()+" -Serveur Version Objet : "+entity.getVersionObj());
				} else {
					 entity = mapper.mapDtoToEntity(dto,entity);
				}
			}
			
			//dao.merge(entity);
			dao.detach(entity); //pour passer les controles
			enregistrerMerge(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			//throw new CreateException(e.getMessage());
			throw new EJBException(e.getMessage());
		}
	}
	
	public void persistDTO(TaNoteTiersDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaNoteTiersDTO dto, String validationContext) throws CreateException {
		try {
			TaNoteTiersMapper mapper = new TaNoteTiersMapper();
			TaNoteTiers entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaNoteTiersDTO dto) throws RemoveException {
		try {
			TaNoteTiersMapper mapper = new TaNoteTiersMapper();
			TaNoteTiers entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaNoteTiers refresh(TaNoteTiers persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaNoteTiers value, String validationContext) /*throws ExceptLgr*/ {
		try {
			if(validationContext==null) validationContext="";
			validateAll(value,validationContext,false); //ancienne validation, extraction de l'annotation et appel
			//dao.validate(value); //validation automatique via la JSR bean validation
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateEntityPropertyValidationContext")
	public void validateEntityProperty(TaNoteTiers value, String propertyName, String validationContext) {
		try {
			if(validationContext==null) validationContext="";
			validate(value, propertyName, validationContext);
			//dao.validateField(value,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOValidationContext")
	public void validateDTO(TaNoteTiersDTO dto, String validationContext) {
		try {
			TaNoteTiersMapper mapper = new TaNoteTiersMapper();
			TaNoteTiers entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaNoteTiersDTO> validator = new BeanValidator<TaNoteTiersDTO>(TaNoteTiersDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaNoteTiersDTO dto, String propertyName, String validationContext) {
		try {
			TaNoteTiersMapper mapper = new TaNoteTiersMapper();
			TaNoteTiers entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaNoteTiersDTO> validator = new BeanValidator<TaNoteTiersDTO>(TaNoteTiersDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaNoteTiersDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaNoteTiersDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaNoteTiers value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaNoteTiers value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
