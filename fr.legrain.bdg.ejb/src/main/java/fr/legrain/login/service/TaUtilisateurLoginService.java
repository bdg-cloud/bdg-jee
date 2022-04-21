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
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.bdg.login.service.remote.ITaUtilisateurLoginServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaUtilisateurLoginMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.login.dao.IUtilisateurLoginDAO;
import fr.legrain.login.dto.TaUtilisateurLoginDTO;
import fr.legrain.login.model.TaUtilisateurLogin;


/**
 * Session Bean implementation class TaUtilisateurBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaUtilisateurLoginService extends AbstractApplicationDAOServer<TaUtilisateurLogin, TaUtilisateurLoginDTO> implements ITaUtilisateurLoginServiceRemote {

	static Logger logger = Logger.getLogger(TaUtilisateurLoginService.class);

	@Inject private IUtilisateurLoginDAO dao;
	
	@Inject private	SessionInfo sessionInfo;
	
	@Inject private	TenantInfo tenantInfo;

	/**
	 * Default constructor. 
	 */
	public TaUtilisateurLoginService() {
		super(TaUtilisateurLogin.class,TaUtilisateurLoginDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaUtilisateurLogin a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaUtilisateurLogin transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaUtilisateurLogin transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaUtilisateurLogin persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getId()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaUtilisateurLogin merge(TaUtilisateurLogin detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaUtilisateurLogin merge(TaUtilisateurLogin detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaUtilisateurLogin findById(int id) throws FinderException {
		return dao.findById(id);
	}

//	@Transactional(value=TxType.REQUIRES_NEW) //mise en comentaire pour le passage de WildFly 17 à 24
	public TaUtilisateurLogin findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaUtilisateurLogin> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaUtilisateurLoginDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaUtilisateurLoginDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaUtilisateurLogin> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaUtilisateurLoginDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaUtilisateurLoginDTO entityToDTO(TaUtilisateurLogin entity) {
//		TaUtilisateurLoginDTO dto = new TaUtilisateurLoginDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaUtilisateurLoginMapper mapper = new TaUtilisateurLoginMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaUtilisateurLoginDTO> listEntityToListDTO(List<TaUtilisateurLogin> entity) {
		List<TaUtilisateurLoginDTO> l = new ArrayList<TaUtilisateurLoginDTO>();

		for (TaUtilisateurLogin taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaUtilisateurLoginDTO> selectAllDTO() {
		System.out.println("List of TaUtilisateurLoginDTO EJB :");
		ArrayList<TaUtilisateurLoginDTO> liste = new ArrayList<TaUtilisateurLoginDTO>();

		List<TaUtilisateurLogin> projects = selectAll();
		for(TaUtilisateurLogin project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaUtilisateurLoginDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaUtilisateurLoginDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaUtilisateurLoginDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaUtilisateurLoginDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaUtilisateurLoginDTO dto, String validationContext) throws EJBException {
		try {
			TaUtilisateurLoginMapper mapper = new TaUtilisateurLoginMapper();
			TaUtilisateurLogin entity = null;
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
	
	public void persistDTO(TaUtilisateurLoginDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaUtilisateurLoginDTO dto, String validationContext) throws CreateException {
		try {
			TaUtilisateurLoginMapper mapper = new TaUtilisateurLoginMapper();
			TaUtilisateurLogin entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaUtilisateurLoginDTO dto) throws RemoveException {
		try {
			TaUtilisateurLoginMapper mapper = new TaUtilisateurLoginMapper();
			TaUtilisateurLogin entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaUtilisateurLogin refresh(TaUtilisateurLogin persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaUtilisateurLogin value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaUtilisateurLogin value, String propertyName, String validationContext) {
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
	public void validateDTO(TaUtilisateurLoginDTO dto, String validationContext) {
		try {
			TaUtilisateurLoginMapper mapper = new TaUtilisateurLoginMapper();
			TaUtilisateurLogin entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaUtilisateurLoginDTO> validator = new BeanValidator<TaUtilisateurLoginDTO>(TaUtilisateurLoginDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaUtilisateurLoginDTO dto, String propertyName, String validationContext) {
		try {
			TaUtilisateurLoginMapper mapper = new TaUtilisateurLoginMapper();
			TaUtilisateurLogin entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaUtilisateurLoginDTO> validator = new BeanValidator<TaUtilisateurLoginDTO>(TaUtilisateurLoginDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaUtilisateurLoginDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaUtilisateurLoginDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaUtilisateurLogin value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaUtilisateurLogin value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
	}

//	public TaUtilisateurLogin getSessionInfo() {
//		if(sessionInfo!=null && sessionInfo.getUtilisateur()!=null)
//			return sessionInfo.getUtilisateur();
//		else
//			return null;
//	}

//	public String getTenantId() {
//		return tenantInfo.getTenantId();
//	}

}
