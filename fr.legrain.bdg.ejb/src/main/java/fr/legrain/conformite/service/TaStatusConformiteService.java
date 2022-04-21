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

import fr.legrain.bdg.conformite.service.remote.ITaStatusConformiteServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaStatusConformiteMapper;
import fr.legrain.conformite.dao.IGroupeDAO;
import fr.legrain.conformite.dao.IStatusConformiteDAO;
import fr.legrain.conformite.dto.TaStatusConformiteDTO;
import fr.legrain.conformite.model.TaStatusConformite;
import fr.legrain.conformite.model.TaStatusConformite;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaStatusConformiteBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaStatusConformiteService extends AbstractApplicationDAOServer<TaStatusConformite, TaStatusConformiteDTO> implements ITaStatusConformiteServiceRemote {

	static Logger logger = Logger.getLogger(TaStatusConformiteService.class);

	@Inject private IStatusConformiteDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaStatusConformiteService() {
		super(TaStatusConformite.class,TaStatusConformiteDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaStatusConformite a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaStatusConformite transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaStatusConformite transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaStatusConformite persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdStatusConformite()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaStatusConformite merge(TaStatusConformite detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaStatusConformite merge(TaStatusConformite detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaStatusConformite findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaStatusConformite findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaStatusConformite> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaStatusConformiteDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaStatusConformiteDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaStatusConformite> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaStatusConformiteDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaStatusConformiteDTO entityToDTO(TaStatusConformite entity) {
//		TaStatusConformiteDTO dto = new TaStatusConformiteDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaStatusConformiteMapper mapper = new TaStatusConformiteMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaStatusConformiteDTO> listEntityToListDTO(List<TaStatusConformite> entity) {
		List<TaStatusConformiteDTO> l = new ArrayList<TaStatusConformiteDTO>();

		for (TaStatusConformite taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaStatusConformiteDTO> selectAllDTO() {
		System.out.println("List of TaStatusConformiteDTO EJB :");
		ArrayList<TaStatusConformiteDTO> liste = new ArrayList<TaStatusConformiteDTO>();

		List<TaStatusConformite> projects = selectAll();
		for(TaStatusConformite project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaStatusConformiteDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaStatusConformiteDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaStatusConformiteDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaStatusConformiteDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaStatusConformiteDTO dto, String validationContext) throws EJBException {
		try {
			TaStatusConformiteMapper mapper = new TaStatusConformiteMapper();
			TaStatusConformite entity = null;
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
	
	public void persistDTO(TaStatusConformiteDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaStatusConformiteDTO dto, String validationContext) throws CreateException {
		try {
			TaStatusConformiteMapper mapper = new TaStatusConformiteMapper();
			TaStatusConformite entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaStatusConformiteDTO dto) throws RemoveException {
		try {
			TaStatusConformiteMapper mapper = new TaStatusConformiteMapper();
			TaStatusConformite entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaStatusConformite refresh(TaStatusConformite persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaStatusConformite value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaStatusConformite value, String propertyName, String validationContext) {
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
	public void validateDTO(TaStatusConformiteDTO dto, String validationContext) {
		try {
			TaStatusConformiteMapper mapper = new TaStatusConformiteMapper();
			TaStatusConformite entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaStatusConformiteDTO> validator = new BeanValidator<TaStatusConformiteDTO>(TaStatusConformiteDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaStatusConformiteDTO dto, String propertyName, String validationContext) {
		try {
			TaStatusConformiteMapper mapper = new TaStatusConformiteMapper();
			TaStatusConformite entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaStatusConformiteDTO> validator = new BeanValidator<TaStatusConformiteDTO>(TaStatusConformiteDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaStatusConformiteDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaStatusConformiteDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaStatusConformite value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaStatusConformite value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
