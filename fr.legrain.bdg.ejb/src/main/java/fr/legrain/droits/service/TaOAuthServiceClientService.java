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

import fr.legrain.bdg.droits.service.remote.ITaOAuthServiceClientServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaOAuthServiceClientServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaOAuthServiceClientMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.droits.dao.IOAuthServiceClientDAO;
import fr.legrain.droits.dao.IUtilisateurDAO;
import fr.legrain.droits.dto.TaOAuthServiceClientDTO;
import fr.legrain.droits.model.TaOAuthServiceClient;
import fr.legrain.droits.model.TaOAuthServiceClient;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaOAuthServiceClientBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaOAuthServiceClientService extends AbstractApplicationDAOServer<TaOAuthServiceClient, TaOAuthServiceClientDTO> implements ITaOAuthServiceClientServiceRemote {

	static Logger logger = Logger.getLogger(TaOAuthServiceClientService.class);

	@Inject private IOAuthServiceClientDAO dao;
	
	@Inject private	SessionInfo sessionInfo;
	
	@Inject private	TenantInfo tenantInfo;

	/**
	 * Default constructor. 
	 */
	public TaOAuthServiceClientService() {
		super(TaOAuthServiceClient.class,TaOAuthServiceClientDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaOAuthServiceClient a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaOAuthServiceClient transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaOAuthServiceClient transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaOAuthServiceClient persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getId()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaOAuthServiceClient merge(TaOAuthServiceClient detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaOAuthServiceClient merge(TaOAuthServiceClient detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaOAuthServiceClient findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaOAuthServiceClient findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaOAuthServiceClient> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaOAuthServiceClientDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaOAuthServiceClientDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaOAuthServiceClient> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaOAuthServiceClientDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaOAuthServiceClientDTO entityToDTO(TaOAuthServiceClient entity) {
//		TaOAuthServiceClientDTO dto = new TaOAuthServiceClientDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaOAuthServiceClientMapper mapper = new TaOAuthServiceClientMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaOAuthServiceClientDTO> listEntityToListDTO(List<TaOAuthServiceClient> entity) {
		List<TaOAuthServiceClientDTO> l = new ArrayList<TaOAuthServiceClientDTO>();

		for (TaOAuthServiceClient taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaOAuthServiceClientDTO> selectAllDTO() {
		System.out.println("List of TaOAuthServiceClientDTO EJB :");
		ArrayList<TaOAuthServiceClientDTO> liste = new ArrayList<TaOAuthServiceClientDTO>();

		List<TaOAuthServiceClient> projects = selectAll();
		for(TaOAuthServiceClient project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaOAuthServiceClientDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaOAuthServiceClientDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaOAuthServiceClientDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaOAuthServiceClientDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaOAuthServiceClientDTO dto, String validationContext) throws EJBException {
		try {
			TaOAuthServiceClientMapper mapper = new TaOAuthServiceClientMapper();
			TaOAuthServiceClient entity = null;
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
	
	public void persistDTO(TaOAuthServiceClientDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaOAuthServiceClientDTO dto, String validationContext) throws CreateException {
		try {
			TaOAuthServiceClientMapper mapper = new TaOAuthServiceClientMapper();
			TaOAuthServiceClient entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaOAuthServiceClientDTO dto) throws RemoveException {
		try {
			TaOAuthServiceClientMapper mapper = new TaOAuthServiceClientMapper();
			TaOAuthServiceClient entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaOAuthServiceClient refresh(TaOAuthServiceClient persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaOAuthServiceClient value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaOAuthServiceClient value, String propertyName, String validationContext) {
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
	public void validateDTO(TaOAuthServiceClientDTO dto, String validationContext) {
		try {
			TaOAuthServiceClientMapper mapper = new TaOAuthServiceClientMapper();
			TaOAuthServiceClient entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaOAuthServiceClientDTO> validator = new BeanValidator<TaOAuthServiceClientDTO>(TaOAuthServiceClientDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaOAuthServiceClientDTO dto, String propertyName, String validationContext) {
		try {
			TaOAuthServiceClientMapper mapper = new TaOAuthServiceClientMapper();
			TaOAuthServiceClient entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaOAuthServiceClientDTO> validator = new BeanValidator<TaOAuthServiceClientDTO>(TaOAuthServiceClientDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaOAuthServiceClientDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaOAuthServiceClientDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaOAuthServiceClient value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaOAuthServiceClient value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
	}

//	public TaOAuthServiceClient getSessionInfo() {
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
//	public List<TaOAuthServiceClientDTO> findByCodeLight(String code) {
//		return dao.findByCodeLight(code);
//	}
//
//	@Override
//	public List<TaOAuthServiceClientDTO> findByNomLight(String nom) {
//		return dao.findByNomLight(nom);
//	}

}
