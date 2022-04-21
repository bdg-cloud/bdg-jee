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

import fr.legrain.bdg.droits.service.remote.ITaAuthViewServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaAuthViewMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.droits.dao.IAuthViewDAO;
import fr.legrain.droits.dto.TaAuthViewDTO;
import fr.legrain.droits.model.TaAuthView;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaAuthViewBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaAuthViewService extends AbstractApplicationDAOServer<TaAuthView, TaAuthViewDTO> implements ITaAuthViewServiceRemote {

	static Logger logger = Logger.getLogger(TaAuthViewService.class);

	@Inject private IAuthViewDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaAuthViewService() {
		super(TaAuthView.class,TaAuthViewDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaAuthView a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaAuthView transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaAuthView transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaAuthView persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getId()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaAuthView merge(TaAuthView detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaAuthView merge(TaAuthView detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaAuthView findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaAuthView findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaAuthView> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaAuthViewDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaAuthViewDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaAuthView> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaAuthViewDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaAuthViewDTO entityToDTO(TaAuthView entity) {
//		TaAuthViewDTO dto = new TaAuthViewDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaAuthViewMapper mapper = new TaAuthViewMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaAuthViewDTO> listEntityToListDTO(List<TaAuthView> entity) {
		List<TaAuthViewDTO> l = new ArrayList<TaAuthViewDTO>();

		for (TaAuthView taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaAuthViewDTO> selectAllDTO() {
		System.out.println("List of TaAuthViewDTO EJB :");
		ArrayList<TaAuthViewDTO> liste = new ArrayList<TaAuthViewDTO>();

		List<TaAuthView> projects = selectAll();
		for(TaAuthView project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaAuthViewDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaAuthViewDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaAuthViewDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaAuthViewDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaAuthViewDTO dto, String validationContext) throws EJBException {
		try {
			TaAuthViewMapper mapper = new TaAuthViewMapper();
			TaAuthView entity = null;
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
	
	public void persistDTO(TaAuthViewDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaAuthViewDTO dto, String validationContext) throws CreateException {
		try {
			TaAuthViewMapper mapper = new TaAuthViewMapper();
			TaAuthView entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaAuthViewDTO dto) throws RemoveException {
		try {
			TaAuthViewMapper mapper = new TaAuthViewMapper();
			TaAuthView entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaAuthView refresh(TaAuthView persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaAuthView value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaAuthView value, String propertyName, String validationContext) {
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
	public void validateDTO(TaAuthViewDTO dto, String validationContext) {
		try {
			TaAuthViewMapper mapper = new TaAuthViewMapper();
			TaAuthView entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaAuthViewDTO> validator = new BeanValidator<TaAuthViewDTO>(TaAuthViewDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaAuthViewDTO dto, String propertyName, String validationContext) {
		try {
			TaAuthViewMapper mapper = new TaAuthViewMapper();
			TaAuthView entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaAuthViewDTO> validator = new BeanValidator<TaAuthViewDTO>(TaAuthViewDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaAuthViewDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaAuthViewDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaAuthView value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaAuthView value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
	}

}
