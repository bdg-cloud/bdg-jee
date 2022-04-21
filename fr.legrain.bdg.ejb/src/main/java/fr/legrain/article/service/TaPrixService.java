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

import fr.legrain.article.dao.IPrixDAO;
import fr.legrain.article.dto.TaPrixDTO;
import fr.legrain.article.model.TaPrix;
import fr.legrain.bdg.article.service.remote.ITaPrixServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaPrixMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaPrixBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaPrixService extends AbstractApplicationDAOServer<TaPrix, TaPrixDTO> implements ITaPrixServiceRemote {

	static Logger logger = Logger.getLogger(TaPrixService.class);

	@Inject private IPrixDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaPrixService() {
		super(TaPrix.class,TaPrixDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaPrix a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaPrix transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaPrix transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void removeSansFind(TaPrix persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);

	}
	
	
	public void remove(TaPrix persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdPrix()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaPrix merge(TaPrix detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaPrix merge(TaPrix detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaPrix findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaPrix findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaPrix> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaPrixDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaPrixDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaPrix> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaPrixDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaPrixDTO entityToDTO(TaPrix entity) {
//		TaPrixDTO dto = new TaPrixDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaPrixMapper mapper = new TaPrixMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaPrixDTO> listEntityToListDTO(List<TaPrix> entity) {
		List<TaPrixDTO> l = new ArrayList<TaPrixDTO>();

		for (TaPrix taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaPrixDTO> selectAllDTO() {
		System.out.println("List of TaPrixDTO EJB :");
		ArrayList<TaPrixDTO> liste = new ArrayList<TaPrixDTO>();

		List<TaPrix> projects = selectAll();
		for(TaPrix project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaPrixDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaPrixDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaPrixDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaPrixDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaPrixDTO dto, String validationContext) throws EJBException {
		try {
			TaPrixMapper mapper = new TaPrixMapper();
			TaPrix entity = null;
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
	
	public void persistDTO(TaPrixDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaPrixDTO dto, String validationContext) throws CreateException {
		try {
			TaPrixMapper mapper = new TaPrixMapper();
			TaPrix entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaPrixDTO dto) throws RemoveException {
		try {
			TaPrixMapper mapper = new TaPrixMapper();
			TaPrix entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaPrix refresh(TaPrix persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaPrix value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaPrix value, String propertyName, String validationContext) {
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
	public void validateDTO(TaPrixDTO dto, String validationContext) {
		try {
			TaPrixMapper mapper = new TaPrixMapper();
			TaPrix entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaPrixDTO> validator = new BeanValidator<TaPrixDTO>(TaPrixDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaPrixDTO dto, String propertyName, String validationContext) {
		try {
			TaPrixMapper mapper = new TaPrixMapper();
			TaPrix entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaPrixDTO> validator = new BeanValidator<TaPrixDTO>(TaPrixDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaPrixDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaPrixDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaPrix value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaPrix value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
