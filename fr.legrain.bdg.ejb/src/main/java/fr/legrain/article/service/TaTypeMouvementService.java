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

import fr.legrain.article.dao.ITypeMouvementDAO;
import fr.legrain.article.dto.TaTypeMouvementDTO;
import fr.legrain.article.model.TaTypeMouvement;
import fr.legrain.bdg.article.service.remote.ITaTypeMouvementServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaTypeMouvementMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaTypeMouvementBean
 */
@SuppressWarnings("deprecation")
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaTypeMouvementService extends AbstractApplicationDAOServer<TaTypeMouvement, TaTypeMouvementDTO> implements ITaTypeMouvementServiceRemote {

	static Logger logger = Logger.getLogger(TaTypeMouvementService.class);

	@Inject private ITypeMouvementDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaTypeMouvementService() {
		super(TaTypeMouvement.class,TaTypeMouvementDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTypeMouvement a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTypeMouvement transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTypeMouvement transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaTypeMouvement persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdTypeMouvement()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaTypeMouvement merge(TaTypeMouvement detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTypeMouvement merge(TaTypeMouvement detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaTypeMouvement findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTypeMouvement findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTypeMouvement> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTypeMouvementDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTypeMouvementDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTypeMouvement> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTypeMouvementDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTypeMouvementDTO entityToDTO(TaTypeMouvement entity) {
//		TaTypeMouvementDTO dto = new TaTypeMouvementDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaTypeMouvementMapper mapper = new TaTypeMouvementMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTypeMouvementDTO> listEntityToListDTO(List<TaTypeMouvement> entity) {
		List<TaTypeMouvementDTO> l = new ArrayList<TaTypeMouvementDTO>();

		for (TaTypeMouvement taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTypeMouvementDTO> selectAllDTO() {
		System.out.println("List of TaTypeMouvementDTO EJB :");
		ArrayList<TaTypeMouvementDTO> liste = new ArrayList<TaTypeMouvementDTO>();

		List<TaTypeMouvement> projects = selectAll();
		for(TaTypeMouvement project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTypeMouvementDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTypeMouvementDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTypeMouvementDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTypeMouvementDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTypeMouvementDTO dto, String validationContext) throws EJBException {
		try {
			TaTypeMouvementMapper mapper = new TaTypeMouvementMapper();
			TaTypeMouvement entity = null;
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
	
	public void persistDTO(TaTypeMouvementDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTypeMouvementDTO dto, String validationContext) throws CreateException {
		try {
			TaTypeMouvementMapper mapper = new TaTypeMouvementMapper();
			TaTypeMouvement entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTypeMouvementDTO dto) throws RemoveException {
		try {
			TaTypeMouvementMapper mapper = new TaTypeMouvementMapper();
			TaTypeMouvement entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTypeMouvement refresh(TaTypeMouvement persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTypeMouvement value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTypeMouvement value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTypeMouvementDTO dto, String validationContext) {
		try {
			TaTypeMouvementMapper mapper = new TaTypeMouvementMapper();
			TaTypeMouvement entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTypeMouvementDTO> validator = new BeanValidator<TaTypeMouvementDTO>(TaTypeMouvementDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTypeMouvementDTO dto, String propertyName, String validationContext) {
		try {
			TaTypeMouvementMapper mapper = new TaTypeMouvementMapper();
			TaTypeMouvement entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTypeMouvementDTO> validator = new BeanValidator<TaTypeMouvementDTO>(TaTypeMouvementDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTypeMouvementDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTypeMouvementDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTypeMouvement value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTypeMouvement value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

	@Override
	public Boolean typeMouvementReserve(String code) {
		// TODO Auto-generated method stub
		return dao.typeMouvementReserve(code);
	}

	
}
