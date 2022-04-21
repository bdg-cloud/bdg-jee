package fr.legrain.conformite.service;

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

import fr.legrain.bdg.conformite.service.remote.ITaTypeConformiteServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaTypeConformiteMapper;
import fr.legrain.conformite.dao.ITypeConformiteDAO;
import fr.legrain.conformite.dto.TaTypeConformiteDTO;
import fr.legrain.conformite.model.TaTypeConformite;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaTypeConformiteBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaTypeConformiteService extends AbstractApplicationDAOServer<TaTypeConformite, TaTypeConformiteDTO> implements ITaTypeConformiteServiceRemote {

	static Logger logger = Logger.getLogger(TaTypeConformiteService.class);

	@Inject private ITypeConformiteDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaTypeConformiteService() {
		super(TaTypeConformite.class,TaTypeConformiteDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTypeConformite a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTypeConformite transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTypeConformite transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaTypeConformite persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdTypeConformite()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaTypeConformite merge(TaTypeConformite detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTypeConformite merge(TaTypeConformite detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaTypeConformite findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTypeConformite findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTypeConformite> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTypeConformiteDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTypeConformiteDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTypeConformite> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTypeConformiteDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTypeConformiteDTO entityToDTO(TaTypeConformite entity) {
//		TaTypeConformiteDTO dto = new TaTypeConformiteDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaTypeConformiteMapper mapper = new TaTypeConformiteMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTypeConformiteDTO> listEntityToListDTO(List<TaTypeConformite> entity) {
		List<TaTypeConformiteDTO> l = new ArrayList<TaTypeConformiteDTO>();

		for (TaTypeConformite taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTypeConformiteDTO> selectAllDTO() {
		System.out.println("List of TaTypeConformiteDTO EJB :");
		ArrayList<TaTypeConformiteDTO> liste = new ArrayList<TaTypeConformiteDTO>();

		List<TaTypeConformite> projects = selectAll();
		for(TaTypeConformite project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTypeConformiteDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTypeConformiteDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTypeConformiteDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTypeConformiteDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTypeConformiteDTO dto, String validationContext) throws EJBException {
		try {
			TaTypeConformiteMapper mapper = new TaTypeConformiteMapper();
			TaTypeConformite entity = null;
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
	
	public void persistDTO(TaTypeConformiteDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTypeConformiteDTO dto, String validationContext) throws CreateException {
		try {
			TaTypeConformiteMapper mapper = new TaTypeConformiteMapper();
			TaTypeConformite entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTypeConformiteDTO dto) throws RemoveException {
		try {
			TaTypeConformiteMapper mapper = new TaTypeConformiteMapper();
			TaTypeConformite entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTypeConformite refresh(TaTypeConformite persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTypeConformite value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTypeConformite value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTypeConformiteDTO dto, String validationContext) {
		try {
			TaTypeConformiteMapper mapper = new TaTypeConformiteMapper();
			TaTypeConformite entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTypeConformiteDTO> validator = new BeanValidator<TaTypeConformiteDTO>(TaTypeConformiteDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTypeConformiteDTO dto, String propertyName, String validationContext) {
		try {
			TaTypeConformiteMapper mapper = new TaTypeConformiteMapper();
			TaTypeConformite entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTypeConformiteDTO> validator = new BeanValidator<TaTypeConformiteDTO>(TaTypeConformiteDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTypeConformiteDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTypeConformiteDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTypeConformite value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTypeConformite value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
