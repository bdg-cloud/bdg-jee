package fr.legrain.tiers.service;

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

import fr.legrain.bdg.model.mapping.mapper.TaParamPublipostageMapper;
import fr.legrain.bdg.tiers.service.remote.ITaParamPublipostageServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.TaParamPublipostageDTO;
import fr.legrain.document.model.TaParamPublipostage;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.dao.IParamPublipostageDAO;

/**
 * Session Bean implementation class TaParamPublipostageBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaParamPublipostageService extends AbstractApplicationDAOServer<TaParamPublipostage, TaParamPublipostageDTO> implements ITaParamPublipostageServiceRemote {

	static Logger logger = Logger.getLogger(TaParamPublipostageService.class);

	@Inject private IParamPublipostageDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaParamPublipostageService() {
		super(TaParamPublipostage.class,TaParamPublipostageDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaParamPublipostage a";
	
	public TaParamPublipostage findInstance() {
		return dao.findInstance();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaParamPublipostage transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaParamPublipostage transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaParamPublipostage persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaParamPublipostage merge(TaParamPublipostage detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaParamPublipostage merge(TaParamPublipostage detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaParamPublipostage findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaParamPublipostage findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaParamPublipostage> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaParamPublipostageDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaParamPublipostageDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaParamPublipostage> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaParamPublipostageDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaParamPublipostageDTO entityToDTO(TaParamPublipostage entity) {
//		TaParamPublipostageDTO dto = new TaParamPublipostageDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaParamPublipostageMapper mapper = new TaParamPublipostageMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaParamPublipostageDTO> listEntityToListDTO(List<TaParamPublipostage> entity) {
		List<TaParamPublipostageDTO> l = new ArrayList<TaParamPublipostageDTO>();

		for (TaParamPublipostage taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaParamPublipostageDTO> selectAllDTO() {
		System.out.println("List of TaParamPublipostageDTO EJB :");
		ArrayList<TaParamPublipostageDTO> liste = new ArrayList<TaParamPublipostageDTO>();

		List<TaParamPublipostage> projects = selectAll();
		for(TaParamPublipostage project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaParamPublipostageDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaParamPublipostageDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaParamPublipostageDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaParamPublipostageDTO dto) throws EJBException {
		mergeDTO(dto,null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaParamPublipostageDTO dto, String validationContext) throws EJBException {
		try {
			TaParamPublipostageMapper mapper = new TaParamPublipostageMapper();
			TaParamPublipostage entity = null;
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
	
	public void persistDTO(TaParamPublipostageDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaParamPublipostageDTO dto, String validationContext) throws CreateException {
		try {
			TaParamPublipostageMapper mapper = new TaParamPublipostageMapper();
			TaParamPublipostage entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaParamPublipostageDTO dto) throws RemoveException {
		try {
			TaParamPublipostageMapper mapper = new TaParamPublipostageMapper();
			TaParamPublipostage entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaParamPublipostage refresh(TaParamPublipostage persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaParamPublipostage value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaParamPublipostage value, String propertyName, String validationContext) {
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
	public void validateDTO(TaParamPublipostageDTO dto, String validationContext) {
		try {
			TaParamPublipostageMapper mapper = new TaParamPublipostageMapper();
			TaParamPublipostage entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaParamPublipostageDTO> validator = new BeanValidator<TaParamPublipostageDTO>(TaParamPublipostageDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaParamPublipostageDTO dto, String propertyName, String validationContext) {
		try {
			TaParamPublipostageMapper mapper = new TaParamPublipostageMapper();
			TaParamPublipostage entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaParamPublipostageDTO> validator = new BeanValidator<TaParamPublipostageDTO>(TaParamPublipostageDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaParamPublipostageDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaParamPublipostageDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaParamPublipostage value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaParamPublipostage value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
