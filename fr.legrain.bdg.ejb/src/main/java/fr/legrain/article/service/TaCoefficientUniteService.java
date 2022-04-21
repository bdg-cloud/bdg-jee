package fr.legrain.article.service;

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

import fr.legrain.article.dao.ICoefficientUniteDAO;
import fr.legrain.article.dto.TaCoefficientUniteDTO;
import fr.legrain.article.model.TaCoefficientUnite;
import fr.legrain.bdg.article.service.remote.ITaCoefficientUniteServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaCoefficientUniteMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaCoefficientUniteBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaCoefficientUniteService extends AbstractApplicationDAOServer<TaCoefficientUnite, TaCoefficientUniteDTO> implements ITaCoefficientUniteServiceRemote {

	static Logger logger = Logger.getLogger(TaCoefficientUniteService.class);

	@Inject private ICoefficientUniteDAO dao;


	/**
	 * Default constructor. 
	 */
	public TaCoefficientUniteService() {
		super(TaCoefficientUnite.class,TaCoefficientUniteDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaCoefficientUnite a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void persist(TaCoefficientUnite transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaCoefficientUnite transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaCoefficientUnite persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getId()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaCoefficientUnite merge(TaCoefficientUnite detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaCoefficientUnite merge(TaCoefficientUnite detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaCoefficientUnite findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaCoefficientUnite findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaCoefficientUnite> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaCoefficientUniteDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaCoefficientUniteDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaCoefficientUnite> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaCoefficientUniteDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaCoefficientUniteDTO entityToDTO(TaCoefficientUnite entity) {
//		TaCoefficientUniteDTO dto = new TaCoefficientUniteDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaCoefficientUniteMapper mapper = new TaCoefficientUniteMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaCoefficientUniteDTO> listEntityToListDTO(List<TaCoefficientUnite> entity) {
		List<TaCoefficientUniteDTO> l = new ArrayList<TaCoefficientUniteDTO>();

		for (TaCoefficientUnite taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaCoefficientUniteDTO> selectAllDTO() {
		System.out.println("List of TaCoefficientUniteDTO EJB :");
		ArrayList<TaCoefficientUniteDTO> liste = new ArrayList<TaCoefficientUniteDTO>();

		List<TaCoefficientUnite> projects = selectAll();
		for(TaCoefficientUnite project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaCoefficientUniteDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaCoefficientUniteDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaCoefficientUniteDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaCoefficientUniteDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaCoefficientUniteDTO dto, String validationContext) throws EJBException {
		try {
			TaCoefficientUniteMapper mapper = new TaCoefficientUniteMapper();
			TaCoefficientUnite entity = null;
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
	
	public void persistDTO(TaCoefficientUniteDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaCoefficientUniteDTO dto, String validationContext) throws CreateException {
		try {
			TaCoefficientUniteMapper mapper = new TaCoefficientUniteMapper();
			TaCoefficientUnite entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaCoefficientUniteDTO dto) throws RemoveException {
		try {
			TaCoefficientUniteMapper mapper = new TaCoefficientUniteMapper();
			TaCoefficientUnite entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaCoefficientUnite refresh(TaCoefficientUnite persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaCoefficientUnite value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaCoefficientUnite value, String propertyName, String validationContext) {
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
	public void validateDTO(TaCoefficientUniteDTO dto, String validationContext) {
		try {
			TaCoefficientUniteMapper mapper = new TaCoefficientUniteMapper();
			TaCoefficientUnite entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaCoefficientUniteDTO> validator = new BeanValidator<TaCoefficientUniteDTO>(TaCoefficientUniteDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaCoefficientUniteDTO dto, String propertyName, String validationContext) {
		try {
			TaCoefficientUniteMapper mapper = new TaCoefficientUniteMapper();
			TaCoefficientUnite entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaCoefficientUniteDTO> validator = new BeanValidator<TaCoefficientUniteDTO>(TaCoefficientUniteDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaCoefficientUniteDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaCoefficientUniteDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaCoefficientUnite value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaCoefficientUnite value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

	@Override
	public TaCoefficientUnite findByCode(String code1, String code2) {
		// TODO Auto-generated method stub
		return dao.findByCode(code1, code2);
	}

}
