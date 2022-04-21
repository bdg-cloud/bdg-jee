package fr.legrain.documents.service;

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

import fr.legrain.bdg.documents.service.remote.ITaInfosBoncdeAchatServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaInfosBoncdeAchatMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.TaBoncdeAchatDTO;
import fr.legrain.document.model.TaInfosBoncdeAchat;
import fr.legrain.documents.dao.IInfosBoncdeAchatDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaInfosBoncdeBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaInfosBoncdeAchatService extends AbstractApplicationDAOServer<TaInfosBoncdeAchat, TaBoncdeAchatDTO> implements ITaInfosBoncdeAchatServiceRemote {

	static Logger logger = Logger.getLogger(TaInfosBoncdeAchatService.class);

	@Inject private IInfosBoncdeAchatDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaInfosBoncdeAchatService() {
		super(TaInfosBoncdeAchat.class,TaBoncdeAchatDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaInfosBoncdeAchat a";
	
	public TaInfosBoncdeAchat findByCodeBoncde(String code) {
		return dao.findByCodeBoncde(code);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaInfosBoncdeAchat transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaInfosBoncdeAchat transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaInfosBoncdeAchat persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaInfosBoncdeAchat merge(TaInfosBoncdeAchat detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaInfosBoncdeAchat merge(TaInfosBoncdeAchat detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaInfosBoncdeAchat findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaInfosBoncdeAchat findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaInfosBoncdeAchat> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaBoncdeAchatDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaBoncdeAchatDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaInfosBoncdeAchat> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaBoncdeAchatDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaBoncdeAchatDTO entityToDTO(TaInfosBoncdeAchat entity) {
//		TaBoncdeAchatDTO dto = new TaBoncdeAchatDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaInfosBoncdeAchatMapper mapper = new TaInfosBoncdeAchatMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaBoncdeAchatDTO> listEntityToListDTO(List<TaInfosBoncdeAchat> entity) {
		List<TaBoncdeAchatDTO> l = new ArrayList<TaBoncdeAchatDTO>();

		for (TaInfosBoncdeAchat taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaBoncdeAchatDTO> selectAllDTO() {
		System.out.println("List of TaBoncdeAchatDTO EJB :");
		ArrayList<TaBoncdeAchatDTO> liste = new ArrayList<TaBoncdeAchatDTO>();

		List<TaInfosBoncdeAchat> projects = selectAll();
		for(TaInfosBoncdeAchat project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaBoncdeAchatDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaBoncdeAchatDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaBoncdeAchatDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaBoncdeAchatDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaBoncdeAchatDTO dto, String validationContext) throws EJBException {
		try {
			TaInfosBoncdeAchatMapper mapper = new TaInfosBoncdeAchatMapper();
			TaInfosBoncdeAchat entity = null;
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
	
	public void persistDTO(TaBoncdeAchatDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaBoncdeAchatDTO dto, String validationContext) throws CreateException {
		try {
			TaInfosBoncdeAchatMapper mapper = new TaInfosBoncdeAchatMapper();
			TaInfosBoncdeAchat entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaBoncdeAchatDTO dto) throws RemoveException {
		try {
			TaInfosBoncdeAchatMapper mapper = new TaInfosBoncdeAchatMapper();
			TaInfosBoncdeAchat entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaInfosBoncdeAchat refresh(TaInfosBoncdeAchat persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaInfosBoncdeAchat value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaInfosBoncdeAchat value, String propertyName, String validationContext) {
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
	public void validateDTO(TaBoncdeAchatDTO dto, String validationContext) {
		try {
			TaInfosBoncdeAchatMapper mapper = new TaInfosBoncdeAchatMapper();
			TaInfosBoncdeAchat entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaBoncdeAchatDTO> validator = new BeanValidator<TaBoncdeAchatDTO>(TaBoncdeAchatDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaBoncdeAchatDTO dto, String propertyName, String validationContext) {
		try {
			TaInfosBoncdeAchatMapper mapper = new TaInfosBoncdeAchatMapper();
			TaInfosBoncdeAchat entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaBoncdeAchatDTO> validator = new BeanValidator<TaBoncdeAchatDTO>(TaBoncdeAchatDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaBoncdeAchatDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaBoncdeAchatDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaInfosBoncdeAchat value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaInfosBoncdeAchat value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
