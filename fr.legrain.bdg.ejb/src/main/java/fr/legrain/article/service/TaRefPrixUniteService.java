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

import fr.legrain.article.dao.IRefPrixDAO;
import fr.legrain.article.dto.TaRefPrixDTO;
import fr.legrain.article.model.TaRefPrix;
import fr.legrain.bdg.article.service.remote.ITaRefPrixServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaRefPrixMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaRefPrixBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaRefPrixUniteService extends AbstractApplicationDAOServer<TaRefPrix, TaRefPrixDTO> implements ITaRefPrixServiceRemote {

	static Logger logger = Logger.getLogger(TaRefPrixUniteService.class);

	@Inject private IRefPrixDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaRefPrixUniteService() {
		super(TaRefPrix.class,TaRefPrixDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaRefPrix a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaRefPrix transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaRefPrix transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaRefPrix persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdRefPrix()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaRefPrix merge(TaRefPrix detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaRefPrix merge(TaRefPrix detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaRefPrix findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaRefPrix findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaRefPrix> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaRefPrixDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaRefPrixDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaRefPrix> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaRefPrixDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaRefPrixDTO entityToDTO(TaRefPrix entity) {
//		TaRefPrixDTO dto = new TaRefPrixDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaRefPrixMapper mapper = new TaRefPrixMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaRefPrixDTO> listEntityToListDTO(List<TaRefPrix> entity) {
		List<TaRefPrixDTO> l = new ArrayList<TaRefPrixDTO>();

		for (TaRefPrix taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaRefPrixDTO> selectAllDTO() {
		System.out.println("List of TaRefPrixDTO EJB :");
		ArrayList<TaRefPrixDTO> liste = new ArrayList<TaRefPrixDTO>();

		List<TaRefPrix> projects = selectAll();
		for(TaRefPrix project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaRefPrixDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaRefPrixDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaRefPrixDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaRefPrixDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaRefPrixDTO dto, String validationContext) throws EJBException {
		try {
			TaRefPrixMapper mapper = new TaRefPrixMapper();
			TaRefPrix entity = null;
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
	
	public void persistDTO(TaRefPrixDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaRefPrixDTO dto, String validationContext) throws CreateException {
		try {
			TaRefPrixMapper mapper = new TaRefPrixMapper();
			TaRefPrix entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaRefPrixDTO dto) throws RemoveException {
		try {
			TaRefPrixMapper mapper = new TaRefPrixMapper();
			TaRefPrix entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaRefPrix refresh(TaRefPrix persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaRefPrix value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaRefPrix value, String propertyName, String validationContext) {
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
	public void validateDTO(TaRefPrixDTO dto, String validationContext) {
		try {
			TaRefPrixMapper mapper = new TaRefPrixMapper();
			TaRefPrix entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaRefPrixDTO> validator = new BeanValidator<TaRefPrixDTO>(TaRefPrixDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaRefPrixDTO dto, String propertyName, String validationContext) {
		try {
			TaRefPrixMapper mapper = new TaRefPrixMapper();
			TaRefPrix entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaRefPrixDTO> validator = new BeanValidator<TaRefPrixDTO>(TaRefPrixDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaRefPrixDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaRefPrixDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaRefPrix value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaRefPrix value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
