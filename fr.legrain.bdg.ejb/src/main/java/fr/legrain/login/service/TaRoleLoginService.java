package fr.legrain.login.service;

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

import fr.legrain.bdg.login.service.remote.ITaRoleLoginServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaRoleLoginMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.login.dao.IRoleLoginDAO;
import fr.legrain.login.dto.TaRoleLoginDTO;
import fr.legrain.login.model.TaRoleLogin;


/**
 * Session Bean implementation class TaRoleBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaRoleLoginService extends AbstractApplicationDAOServer<TaRoleLogin, TaRoleLoginDTO> implements ITaRoleLoginServiceRemote {

	static Logger logger = Logger.getLogger(TaRoleLoginService.class);

	@Inject private IRoleLoginDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaRoleLoginService() {
		super(TaRoleLogin.class,TaRoleLoginDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaRoleLogin a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaRoleLogin transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaRoleLogin transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaRoleLogin persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getId()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaRoleLogin merge(TaRoleLogin detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaRoleLogin merge(TaRoleLogin detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaRoleLogin findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaRoleLogin findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaRoleLogin> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaRoleLoginDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaRoleLoginDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaRoleLogin> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaRoleLoginDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaRoleLoginDTO entityToDTO(TaRoleLogin entity) {
//		TaRoleLoginDTO dto = new TaRoleLoginDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaRoleLoginMapper mapper = new TaRoleLoginMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaRoleLoginDTO> listEntityToListDTO(List<TaRoleLogin> entity) {
		List<TaRoleLoginDTO> l = new ArrayList<TaRoleLoginDTO>();

		for (TaRoleLogin taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaRoleLoginDTO> selectAllDTO() {
		System.out.println("List of TaRoleLoginDTO EJB :");
		ArrayList<TaRoleLoginDTO> liste = new ArrayList<TaRoleLoginDTO>();

		List<TaRoleLogin> projects = selectAll();
		for(TaRoleLogin project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaRoleLoginDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaRoleLoginDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaRoleLoginDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaRoleLoginDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaRoleLoginDTO dto, String validationContext) throws EJBException {
		try {
			TaRoleLoginMapper mapper = new TaRoleLoginMapper();
			TaRoleLogin entity = null;
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
	
	public void persistDTO(TaRoleLoginDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaRoleLoginDTO dto, String validationContext) throws CreateException {
		try {
			TaRoleLoginMapper mapper = new TaRoleLoginMapper();
			TaRoleLogin entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaRoleLoginDTO dto) throws RemoveException {
		try {
			TaRoleLoginMapper mapper = new TaRoleLoginMapper();
			TaRoleLogin entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaRoleLogin refresh(TaRoleLogin persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaRoleLogin value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaRoleLogin value, String propertyName, String validationContext) {
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
	public void validateDTO(TaRoleLoginDTO dto, String validationContext) {
		try {
			TaRoleLoginMapper mapper = new TaRoleLoginMapper();
			TaRoleLogin entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaRoleLoginDTO> validator = new BeanValidator<TaRoleLoginDTO>(TaRoleLoginDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaRoleLoginDTO dto, String propertyName, String validationContext) {
		try {
			TaRoleLoginMapper mapper = new TaRoleLoginMapper();
			TaRoleLogin entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaRoleLoginDTO> validator = new BeanValidator<TaRoleLoginDTO>(TaRoleLoginDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaRoleLoginDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaRoleLoginDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaRoleLogin value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaRoleLogin value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
	}

}
