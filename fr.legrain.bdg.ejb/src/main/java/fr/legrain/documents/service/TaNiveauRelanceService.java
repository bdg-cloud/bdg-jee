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

import fr.legrain.bdg.documents.service.remote.ITaNiveauRelanceServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaNiveauRelanceMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.TaNiveauRelanceDTO;
import fr.legrain.document.model.TaTRelance;
import fr.legrain.documents.dao.ITRelanceDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaTRelanceBean
 */
@Stateless
@DeclareRoles("admin")@Interceptors(ServerTenantInterceptor.class)

public class TaNiveauRelanceService extends AbstractApplicationDAOServer<TaTRelance, TaNiveauRelanceDTO> implements ITaNiveauRelanceServiceRemote {

	static Logger logger = Logger.getLogger(TaNiveauRelanceService.class);

	@Inject private ITRelanceDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaNiveauRelanceService() {
		super(TaTRelance.class,TaNiveauRelanceDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTRelance a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTRelance transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTRelance transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaTRelance persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdTRelance()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaTRelance merge(TaTRelance detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTRelance merge(TaTRelance detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaTRelance findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTRelance findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTRelance> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaNiveauRelanceDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaNiveauRelanceDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTRelance> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaNiveauRelanceDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaNiveauRelanceDTO entityToDTO(TaTRelance entity) {
//		TaNiveauRelanceDTO dto = new TaNiveauRelanceDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		
		TaNiveauRelanceMapper mapper = new TaNiveauRelanceMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaNiveauRelanceDTO> listEntityToListDTO(List<TaTRelance> entity) {
		List<TaNiveauRelanceDTO> l = new ArrayList<TaNiveauRelanceDTO>();

		for (TaTRelance taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaNiveauRelanceDTO> selectAllDTO() {
		System.out.println("List of TaNiveauRelanceDTO EJB :");
		ArrayList<TaNiveauRelanceDTO> liste = new ArrayList<TaNiveauRelanceDTO>();

		List<TaTRelance> projects = selectAll();
		for(TaTRelance project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaNiveauRelanceDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaNiveauRelanceDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaNiveauRelanceDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaNiveauRelanceDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaNiveauRelanceDTO dto, String validationContext) throws EJBException {
		try {
			TaNiveauRelanceMapper mapper = new TaNiveauRelanceMapper();
			TaTRelance entity = null;
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
	
	public void persistDTO(TaNiveauRelanceDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaNiveauRelanceDTO dto, String validationContext) throws CreateException {
		try {
			TaNiveauRelanceMapper mapper = new TaNiveauRelanceMapper();
			TaTRelance entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaNiveauRelanceDTO dto) throws RemoveException {
		try {
			TaNiveauRelanceMapper mapper = new TaNiveauRelanceMapper();
			TaTRelance entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTRelance refresh(TaTRelance persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTRelance value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTRelance value, String propertyName, String validationContext) {
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
	public void validateDTO(TaNiveauRelanceDTO dto, String validationContext) {
		try {
			TaNiveauRelanceMapper mapper = new TaNiveauRelanceMapper();
			TaTRelance entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaNiveauRelanceDTO> validator = new BeanValidator<TaNiveauRelanceDTO>(TaNiveauRelanceDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaNiveauRelanceDTO dto, String propertyName, String validationContext) {
		try {
			TaNiveauRelanceMapper mapper = new TaNiveauRelanceMapper();
			TaTRelance entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaNiveauRelanceDTO> validator = new BeanValidator<TaNiveauRelanceDTO>(TaNiveauRelanceDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaNiveauRelanceDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaNiveauRelanceDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTRelance value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTRelance value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
