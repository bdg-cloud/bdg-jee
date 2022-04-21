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

import fr.legrain.bdg.login.service.remote.ITaRRoleUtilisateurLoginServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaRRoleUtilisateurLoginMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.login.dao.IRRoleUtilisateurLoginDAO;
import fr.legrain.login.dto.TaRRoleUtilisateurLoginDTO;
import fr.legrain.login.model.TaRRoleUtilisateurLogin;


/**
 * Session Bean implementation class TaRRoleUtilisateurBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaRRoleUtilisateurLoginService extends AbstractApplicationDAOServer<TaRRoleUtilisateurLogin, TaRRoleUtilisateurLoginDTO> implements ITaRRoleUtilisateurLoginServiceRemote {

	static Logger logger = Logger.getLogger(TaRRoleUtilisateurLoginService.class);

	@Inject private IRRoleUtilisateurLoginDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaRRoleUtilisateurLoginService() {
		super(TaRRoleUtilisateurLogin.class,TaRRoleUtilisateurLoginDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaRRoleUtilisateurLogin a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaRRoleUtilisateurLogin transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaRRoleUtilisateurLogin transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaRRoleUtilisateurLogin persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getId()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaRRoleUtilisateurLogin merge(TaRRoleUtilisateurLogin detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaRRoleUtilisateurLogin merge(TaRRoleUtilisateurLogin detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaRRoleUtilisateurLogin findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaRRoleUtilisateurLogin findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaRRoleUtilisateurLogin> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaRRoleUtilisateurLoginDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaRRoleUtilisateurLoginDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaRRoleUtilisateurLogin> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaRRoleUtilisateurLoginDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaRRoleUtilisateurLoginDTO entityToDTO(TaRRoleUtilisateurLogin entity) {
//		TaRRoleUtilisateurLoginDTO dto = new TaRRoleUtilisateurLoginDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaRRoleUtilisateurLoginMapper mapper = new TaRRoleUtilisateurLoginMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaRRoleUtilisateurLoginDTO> listEntityToListDTO(List<TaRRoleUtilisateurLogin> entity) {
		List<TaRRoleUtilisateurLoginDTO> l = new ArrayList<TaRRoleUtilisateurLoginDTO>();

		for (TaRRoleUtilisateurLogin taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaRRoleUtilisateurLoginDTO> selectAllDTO() {
		System.out.println("List of TaRRoleUtilisateurLoginDTO EJB :");
		ArrayList<TaRRoleUtilisateurLoginDTO> liste = new ArrayList<TaRRoleUtilisateurLoginDTO>();

		List<TaRRoleUtilisateurLogin> projects = selectAll();
		for(TaRRoleUtilisateurLogin project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaRRoleUtilisateurLoginDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaRRoleUtilisateurLoginDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaRRoleUtilisateurLoginDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaRRoleUtilisateurLoginDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaRRoleUtilisateurLoginDTO dto, String validationContext) throws EJBException {
		try {
			TaRRoleUtilisateurLoginMapper mapper = new TaRRoleUtilisateurLoginMapper();
			TaRRoleUtilisateurLogin entity = null;
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
	
	public void persistDTO(TaRRoleUtilisateurLoginDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaRRoleUtilisateurLoginDTO dto, String validationContext) throws CreateException {
		try {
			TaRRoleUtilisateurLoginMapper mapper = new TaRRoleUtilisateurLoginMapper();
			TaRRoleUtilisateurLogin entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaRRoleUtilisateurLoginDTO dto) throws RemoveException {
		try {
			TaRRoleUtilisateurLoginMapper mapper = new TaRRoleUtilisateurLoginMapper();
			TaRRoleUtilisateurLogin entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaRRoleUtilisateurLogin refresh(TaRRoleUtilisateurLogin persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaRRoleUtilisateurLogin value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaRRoleUtilisateurLogin value, String propertyName, String validationContext) {
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
	public void validateDTO(TaRRoleUtilisateurLoginDTO dto, String validationContext) {
		try {
			TaRRoleUtilisateurLoginMapper mapper = new TaRRoleUtilisateurLoginMapper();
			TaRRoleUtilisateurLogin entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaRRoleUtilisateurLoginDTO> validator = new BeanValidator<TaRRoleUtilisateurLoginDTO>(TaRRoleUtilisateurLoginDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaRRoleUtilisateurLoginDTO dto, String propertyName, String validationContext) {
		try {
			TaRRoleUtilisateurLoginMapper mapper = new TaRRoleUtilisateurLoginMapper();
			TaRRoleUtilisateurLogin entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaRRoleUtilisateurLoginDTO> validator = new BeanValidator<TaRRoleUtilisateurLoginDTO>(TaRRoleUtilisateurLoginDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaRRoleUtilisateurLoginDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaRRoleUtilisateurLoginDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaRRoleUtilisateurLogin value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaRRoleUtilisateurLogin value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
	}

}
