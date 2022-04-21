package fr.legrain.droits.service;

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

import fr.legrain.bdg.droits.service.remote.ITaOAuthScopeClientServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaOAuthScopeClientMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.droits.dao.IOAuthScopeClientDAO;
import fr.legrain.droits.dao.IOAuthServiceClientDAO;
import fr.legrain.droits.dto.TaOAuthScopeClientDTO;
import fr.legrain.droits.model.TaOAuthScopeClient;
import fr.legrain.droits.model.TaOAuthServiceClient;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaOAuthScopeClientBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaOAuthScopeClientService extends AbstractApplicationDAOServer<TaOAuthScopeClient, TaOAuthScopeClientDTO> implements ITaOAuthScopeClientServiceRemote {

	static Logger logger = Logger.getLogger(TaOAuthScopeClientService.class);

	@Inject private IOAuthScopeClientDAO dao;
	@Inject private IOAuthServiceClientDAO daoService;
	
	@Inject private	SessionInfo sessionInfo;
	
	@Inject private	TenantInfo tenantInfo;

	/**
	 * Default constructor. 
	 */
	public TaOAuthScopeClientService() {
		super(TaOAuthScopeClient.class,TaOAuthScopeClientDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaOAuthScopeClient a";
	
	public List<TaOAuthScopeClient> selectAll(TaOAuthServiceClient taOAuthServiceClient) {
		return dao.selectAll(taOAuthServiceClient);
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaOAuthScopeClient transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaOAuthScopeClient transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaOAuthScopeClient persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getId()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
//	@Transactional(value=TxType.REQUIRES_NEW)
	public TaOAuthScopeClient merge(TaOAuthScopeClient detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaOAuthScopeClient merge(TaOAuthScopeClient detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaOAuthScopeClient findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaOAuthScopeClient findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaOAuthScopeClient> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaOAuthScopeClientDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaOAuthScopeClientDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaOAuthScopeClient> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaOAuthScopeClientDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaOAuthScopeClientDTO entityToDTO(TaOAuthScopeClient entity) {
//		TaOAuthScopeClientDTO dto = new TaOAuthScopeClientDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaOAuthScopeClientMapper mapper = new TaOAuthScopeClientMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaOAuthScopeClientDTO> listEntityToListDTO(List<TaOAuthScopeClient> entity) {
		List<TaOAuthScopeClientDTO> l = new ArrayList<TaOAuthScopeClientDTO>();

		for (TaOAuthScopeClient taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaOAuthScopeClientDTO> selectAllDTO() {
		System.out.println("List of TaOAuthScopeClientDTO EJB :");
		ArrayList<TaOAuthScopeClientDTO> liste = new ArrayList<TaOAuthScopeClientDTO>();

		List<TaOAuthScopeClient> projects = selectAll();
		for(TaOAuthScopeClient project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaOAuthScopeClientDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaOAuthScopeClientDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaOAuthScopeClientDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaOAuthScopeClientDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaOAuthScopeClientDTO dto, String validationContext) throws EJBException {
		try {
			TaOAuthScopeClientMapper mapper = new TaOAuthScopeClientMapper();
			TaOAuthScopeClient entity = null;
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
	
	public void persistDTO(TaOAuthScopeClientDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaOAuthScopeClientDTO dto, String validationContext) throws CreateException {
		try {
			TaOAuthScopeClientMapper mapper = new TaOAuthScopeClientMapper();
			TaOAuthScopeClient entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaOAuthScopeClientDTO dto) throws RemoveException {
		try {
			TaOAuthScopeClientMapper mapper = new TaOAuthScopeClientMapper();
			TaOAuthScopeClient entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaOAuthScopeClient refresh(TaOAuthScopeClient persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaOAuthScopeClient value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaOAuthScopeClient value, String propertyName, String validationContext) {
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
	public void validateDTO(TaOAuthScopeClientDTO dto, String validationContext) {
		try {
			TaOAuthScopeClientMapper mapper = new TaOAuthScopeClientMapper();
			TaOAuthScopeClient entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaOAuthScopeClientDTO> validator = new BeanValidator<TaOAuthScopeClientDTO>(TaOAuthScopeClientDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaOAuthScopeClientDTO dto, String propertyName, String validationContext) {
		try {
			TaOAuthScopeClientMapper mapper = new TaOAuthScopeClientMapper();
			TaOAuthScopeClient entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaOAuthScopeClientDTO> validator = new BeanValidator<TaOAuthScopeClientDTO>(TaOAuthScopeClientDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaOAuthScopeClientDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaOAuthScopeClientDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaOAuthScopeClient value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaOAuthScopeClient value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
	}

//	public TaOAuthScopeClient getSessionInfo() {
//		if(sessionInfo!=null && sessionInfo.getUtilisateur()!=null)
//			return sessionInfo.getUtilisateur();
//		else
//			return null;
//	}
//
//	public String getTenantId() {
//		return tenantInfo.getTenantId();
//	}
//
//	@Override
//	public List<TaOAuthScopeClientDTO> findByCodeLight(String code) {
//		return dao.findByCodeLight(code);
//	}
//
//	@Override
//	public List<TaOAuthScopeClientDTO> findByNomLight(String nom) {
//		return dao.findByNomLight(nom);
//	}

}
