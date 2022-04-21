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

import fr.legrain.bdg.model.mapping.mapper.TaParamCreeDocTiersMapper;
import fr.legrain.bdg.tiers.service.remote.ITaParamCreeDocTiersServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.dao.IParamCreeDocTiersDAO;
import fr.legrain.tiers.dto.TaParamCreeDocTiersDTO;
import fr.legrain.tiers.model.TaParamCreeDocTiers;

/**
 * Session Bean implementation class TaParamCreeDocTiersBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaParamCreeDocTiersService extends AbstractApplicationDAOServer<TaParamCreeDocTiers, TaParamCreeDocTiersDTO> implements ITaParamCreeDocTiersServiceRemote {

	static Logger logger = Logger.getLogger(TaParamCreeDocTiersService.class);

	@Inject private IParamCreeDocTiersDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaParamCreeDocTiersService() {
		super(TaParamCreeDocTiers.class,TaParamCreeDocTiersDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaParamCreeDocTiers a";
	
	public List<TaParamCreeDocTiers> findByCodeTypeDoc(String codeTiers,String typeDoc) {
		return dao.findByCodeTypeDoc(codeTiers,typeDoc);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaParamCreeDocTiers transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaParamCreeDocTiers transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaParamCreeDocTiers persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaParamCreeDocTiers merge(TaParamCreeDocTiers detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaParamCreeDocTiers merge(TaParamCreeDocTiers detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaParamCreeDocTiers findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaParamCreeDocTiers findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaParamCreeDocTiers> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaParamCreeDocTiersDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaParamCreeDocTiersDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaParamCreeDocTiers> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaParamCreeDocTiersDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaParamCreeDocTiersDTO entityToDTO(TaParamCreeDocTiers entity) {
//		TaParamCreeDocTiersDTO dto = new TaParamCreeDocTiersDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaParamCreeDocTiersMapper mapper = new TaParamCreeDocTiersMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaParamCreeDocTiersDTO> listEntityToListDTO(List<TaParamCreeDocTiers> entity) {
		List<TaParamCreeDocTiersDTO> l = new ArrayList<TaParamCreeDocTiersDTO>();

		for (TaParamCreeDocTiers taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaParamCreeDocTiersDTO> selectAllDTO() {
		System.out.println("List of TaParamCreeDocTiersDTO EJB :");
		ArrayList<TaParamCreeDocTiersDTO> liste = new ArrayList<TaParamCreeDocTiersDTO>();

		List<TaParamCreeDocTiers> projects = selectAll();
		for(TaParamCreeDocTiers project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaParamCreeDocTiersDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaParamCreeDocTiersDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaParamCreeDocTiersDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaParamCreeDocTiersDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaParamCreeDocTiersDTO dto, String validationContext) throws EJBException {
		try {
			TaParamCreeDocTiersMapper mapper = new TaParamCreeDocTiersMapper();
			TaParamCreeDocTiers entity = null;
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
	
	public void persistDTO(TaParamCreeDocTiersDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaParamCreeDocTiersDTO dto, String validationContext) throws CreateException {
		try {
			TaParamCreeDocTiersMapper mapper = new TaParamCreeDocTiersMapper();
			TaParamCreeDocTiers entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaParamCreeDocTiersDTO dto) throws RemoveException {
		try {
			TaParamCreeDocTiersMapper mapper = new TaParamCreeDocTiersMapper();
			TaParamCreeDocTiers entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaParamCreeDocTiers refresh(TaParamCreeDocTiers persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaParamCreeDocTiers value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaParamCreeDocTiers value, String propertyName, String validationContext) {
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
	public void validateDTO(TaParamCreeDocTiersDTO dto, String validationContext) {
		try {
			TaParamCreeDocTiersMapper mapper = new TaParamCreeDocTiersMapper();
			TaParamCreeDocTiers entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaParamCreeDocTiersDTO> validator = new BeanValidator<TaParamCreeDocTiersDTO>(TaParamCreeDocTiersDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaParamCreeDocTiersDTO dto, String propertyName, String validationContext) {
		try {
			TaParamCreeDocTiersMapper mapper = new TaParamCreeDocTiersMapper();
			TaParamCreeDocTiers entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaParamCreeDocTiersDTO> validator = new BeanValidator<TaParamCreeDocTiersDTO>(TaParamCreeDocTiersDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaParamCreeDocTiersDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaParamCreeDocTiersDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaParamCreeDocTiers value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaParamCreeDocTiers value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
