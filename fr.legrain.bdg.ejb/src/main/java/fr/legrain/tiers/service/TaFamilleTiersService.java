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

import fr.legrain.bdg.model.mapping.mapper.TaFamilleTiersMapper;
import fr.legrain.bdg.tiers.service.remote.ITaFamilleTiersServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.dao.IFamilleTiersDAO;
import fr.legrain.tiers.dto.TaFamilleTiersDTO;
import fr.legrain.tiers.model.TaFamilleTiers;

/**
 * Session Bean implementation class TaFamilleTiersBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaFamilleTiersService extends AbstractApplicationDAOServer<TaFamilleTiers, TaFamilleTiersDTO> implements ITaFamilleTiersServiceRemote {

	static Logger logger = Logger.getLogger(TaFamilleTiersService.class);

	@Inject private IFamilleTiersDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaFamilleTiersService() {
		super(TaFamilleTiers.class,TaFamilleTiersDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaFamilleTiers a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaFamilleTiers transientInstance) throws CreateException {
		persist(transientInstance,null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaFamilleTiers transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaFamilleTiers persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdFamille()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaFamilleTiers merge(TaFamilleTiers detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaFamilleTiers merge(TaFamilleTiers detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaFamilleTiers findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaFamilleTiers findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaFamilleTiers> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaFamilleTiersDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaFamilleTiersDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaFamilleTiers> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaFamilleTiersDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaFamilleTiersDTO entityToDTO(TaFamilleTiers entity) {
//		TaFamilleTiersDTO dto = new TaFamilleTiersDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaFamilleTiersMapper mapper = new TaFamilleTiersMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaFamilleTiersDTO> listEntityToListDTO(List<TaFamilleTiers> entity) {
		List<TaFamilleTiersDTO> l = new ArrayList<TaFamilleTiersDTO>();

		for (TaFamilleTiers taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaFamilleTiersDTO> selectAllDTO() {
		System.out.println("List of TaFamilleTiersDTO EJB :");
		ArrayList<TaFamilleTiersDTO> liste = new ArrayList<TaFamilleTiersDTO>();

		List<TaFamilleTiers> projects = selectAll();
		for(TaFamilleTiers project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaFamilleTiersDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaFamilleTiersDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaFamilleTiersDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaFamilleTiersDTO dto) throws EJBException {
		mergeDTO(dto,null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaFamilleTiersDTO dto, String validationContext) throws EJBException {
		try {
			TaFamilleTiersMapper mapper = new TaFamilleTiersMapper();
			TaFamilleTiers entity = null;
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
	
	public void persistDTO(TaFamilleTiersDTO dto) throws CreateException {
		persistDTO(dto,null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaFamilleTiersDTO dto, String validationContext) throws CreateException {
		try {
			TaFamilleTiersMapper mapper = new TaFamilleTiersMapper();
			TaFamilleTiers entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaFamilleTiersDTO dto) throws RemoveException {
		try {
			TaFamilleTiersMapper mapper = new TaFamilleTiersMapper();
			TaFamilleTiers entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaFamilleTiers refresh(TaFamilleTiers persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaFamilleTiers value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaFamilleTiers value, String propertyName, String validationContext) {
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
	public void validateDTO(TaFamilleTiersDTO dto, String validationContext) {
		try {
			TaFamilleTiersMapper mapper = new TaFamilleTiersMapper();
			TaFamilleTiers entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaFamilleTiersDTO> validator = new BeanValidator<TaFamilleTiersDTO>(TaFamilleTiersDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaFamilleTiersDTO dto, String propertyName, String validationContext) {
		try {
			TaFamilleTiersMapper mapper = new TaFamilleTiersMapper();
			TaFamilleTiers entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaFamilleTiersDTO> validator = new BeanValidator<TaFamilleTiersDTO>(TaFamilleTiersDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaFamilleTiersDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaFamilleTiersDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaFamilleTiers value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaFamilleTiers value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
