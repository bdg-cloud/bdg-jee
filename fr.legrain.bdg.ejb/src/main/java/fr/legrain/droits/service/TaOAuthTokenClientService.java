package fr.legrain.droits.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

import fr.legrain.bdg.droits.service.remote.ITaOAuthTokenClientServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaOAuthTokenClientServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaOAuthTokenClientMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.droits.dao.IOAuthServiceClientDAO;
import fr.legrain.droits.dao.IOAuthTokenClientDAO;
import fr.legrain.droits.dao.IUtilisateurDAO;
import fr.legrain.droits.dto.TaOAuthTokenClientDTO;
import fr.legrain.droits.model.TaOAuthServiceClient;
import fr.legrain.droits.model.TaOAuthTokenClient;
import fr.legrain.droits.model.TaOAuthTokenClient;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaOAuthTokenClientBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaOAuthTokenClientService extends AbstractApplicationDAOServer<TaOAuthTokenClient, TaOAuthTokenClientDTO> implements ITaOAuthTokenClientServiceRemote {

	static Logger logger = Logger.getLogger(TaOAuthTokenClientService.class);

	@Inject private IOAuthTokenClientDAO dao;
	@Inject private IOAuthServiceClientDAO daoService;
	
	@Inject private	SessionInfo sessionInfo;
	
	@Inject private	TenantInfo tenantInfo;

	/**
	 * Default constructor. 
	 */
	public TaOAuthTokenClientService() {
		super(TaOAuthTokenClient.class,TaOAuthTokenClientDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaOAuthTokenClient a";

	public TaOAuthTokenClient findByKey(String key) {
		return dao.findByKey(key);
	}
	public TaOAuthTokenClient findByAccessToken(String key) {
		return dao.findByAccessToken(key);
	}
	public void removeAll() {
		dao.removeAll();
	}
	public Set<String> findAllKeys() {
		return 	dao.findAllKeys();
	}
	
	public void removeKey(String key) {
		dao.removeKey(key);
	}
	
	public TaOAuthServiceClient findByCodeService(String code) {
		return daoService.findByCode(code);
	}
	
	@Override
	public TaOAuthTokenClient findByKey(String key, TaOAuthServiceClient taOAuthServiceClient) {
		return dao.findByKey(key, taOAuthServiceClient);
	}

	@Override
	public TaOAuthTokenClient findByAccessToken(String key, TaOAuthServiceClient taOAuthServiceClient) {
		return dao.findByAccessToken(key,taOAuthServiceClient);
	}

	@Override
	public void removeAll(TaOAuthServiceClient taOAuthServiceClient) {
		dao.removeAll(taOAuthServiceClient);
	}

	@Override
	public void removeKey(String key, TaOAuthServiceClient taOAuthServiceClient) {
		dao.removeKey(key,taOAuthServiceClient);	
	}

	@Override
	public Set<String> findAllKeys(TaOAuthServiceClient taOAuthServiceClient) {
		// TODO Auto-generated method stub
		return dao.findAllKeys(taOAuthServiceClient);
	}

	@Override
	public List<TaOAuthTokenClient> selectAll(TaOAuthServiceClient taOAuthServiceClient) {
		// TODO Auto-generated method stub
		return dao.selectAll(taOAuthServiceClient);
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaOAuthTokenClient transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaOAuthTokenClient transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaOAuthTokenClient persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getId()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
//	@Transactional(value=TxType.REQUIRES_NEW)
	public TaOAuthTokenClient merge(TaOAuthTokenClient detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaOAuthTokenClient merge(TaOAuthTokenClient detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaOAuthTokenClient findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaOAuthTokenClient findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaOAuthTokenClient> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaOAuthTokenClientDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaOAuthTokenClientDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaOAuthTokenClient> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaOAuthTokenClientDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaOAuthTokenClientDTO entityToDTO(TaOAuthTokenClient entity) {
//		TaOAuthTokenClientDTO dto = new TaOAuthTokenClientDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaOAuthTokenClientMapper mapper = new TaOAuthTokenClientMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaOAuthTokenClientDTO> listEntityToListDTO(List<TaOAuthTokenClient> entity) {
		List<TaOAuthTokenClientDTO> l = new ArrayList<TaOAuthTokenClientDTO>();

		for (TaOAuthTokenClient taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaOAuthTokenClientDTO> selectAllDTO() {
		System.out.println("List of TaOAuthTokenClientDTO EJB :");
		ArrayList<TaOAuthTokenClientDTO> liste = new ArrayList<TaOAuthTokenClientDTO>();

		List<TaOAuthTokenClient> projects = selectAll();
		for(TaOAuthTokenClient project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaOAuthTokenClientDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaOAuthTokenClientDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaOAuthTokenClientDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaOAuthTokenClientDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaOAuthTokenClientDTO dto, String validationContext) throws EJBException {
		try {
			TaOAuthTokenClientMapper mapper = new TaOAuthTokenClientMapper();
			TaOAuthTokenClient entity = null;
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
	
	public void persistDTO(TaOAuthTokenClientDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaOAuthTokenClientDTO dto, String validationContext) throws CreateException {
		try {
			TaOAuthTokenClientMapper mapper = new TaOAuthTokenClientMapper();
			TaOAuthTokenClient entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaOAuthTokenClientDTO dto) throws RemoveException {
		try {
			TaOAuthTokenClientMapper mapper = new TaOAuthTokenClientMapper();
			TaOAuthTokenClient entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaOAuthTokenClient refresh(TaOAuthTokenClient persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaOAuthTokenClient value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaOAuthTokenClient value, String propertyName, String validationContext) {
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
	public void validateDTO(TaOAuthTokenClientDTO dto, String validationContext) {
		try {
			TaOAuthTokenClientMapper mapper = new TaOAuthTokenClientMapper();
			TaOAuthTokenClient entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaOAuthTokenClientDTO> validator = new BeanValidator<TaOAuthTokenClientDTO>(TaOAuthTokenClientDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaOAuthTokenClientDTO dto, String propertyName, String validationContext) {
		try {
			TaOAuthTokenClientMapper mapper = new TaOAuthTokenClientMapper();
			TaOAuthTokenClient entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaOAuthTokenClientDTO> validator = new BeanValidator<TaOAuthTokenClientDTO>(TaOAuthTokenClientDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaOAuthTokenClientDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaOAuthTokenClientDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaOAuthTokenClient value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaOAuthTokenClient value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
	}

//	public TaOAuthTokenClient getSessionInfo() {
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
//	public List<TaOAuthTokenClientDTO> findByCodeLight(String code) {
//		return dao.findByCodeLight(code);
//	}
//
//	@Override
//	public List<TaOAuthTokenClientDTO> findByNomLight(String nom) {
//		return dao.findByNomLight(nom);
//	}

}
