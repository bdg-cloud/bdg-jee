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

import fr.legrain.article.dao.IEntrepotDAO;
import fr.legrain.article.dto.TaEntrepotDTO;
import fr.legrain.article.model.TaEntrepot;
import fr.legrain.bdg.article.service.remote.ITaEntrepotServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaEntrepotMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaEntrepotBean
 */
@SuppressWarnings("deprecation")
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaEntrepotService extends AbstractApplicationDAOServer<TaEntrepot, TaEntrepotDTO> implements ITaEntrepotServiceRemote {

	static Logger logger = Logger.getLogger(TaEntrepotService.class);

	@Inject private IEntrepotDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaEntrepotService() {
		super(TaEntrepot.class,TaEntrepotDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaEntrepot a";
	
	public List<TaEntrepotDTO> findByCodeLight(String code) {
		return dao.findByCodeLight(code);
	}
	
	public List<TaEntrepotDTO> findAllLight() {
		return dao.findAllLight();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaEntrepot transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaEntrepot transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaEntrepot persistentInstance) throws RemoveException {
		dao.remove(dao.findById(persistentInstance.getIdEntrepot()));
	}
	
	public TaEntrepot merge(TaEntrepot detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaEntrepot merge(TaEntrepot detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaEntrepot findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaEntrepot findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaEntrepot> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaEntrepotDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaEntrepotDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaEntrepot> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaEntrepotDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaEntrepotDTO entityToDTO(TaEntrepot entity) {
//		TaEntrepotDTO dto = new TaEntrepotDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaEntrepotMapper mapper = new TaEntrepotMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaEntrepotDTO> listEntityToListDTO(List<TaEntrepot> entity) {
		List<TaEntrepotDTO> l = new ArrayList<TaEntrepotDTO>();

		for (TaEntrepot taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaEntrepotDTO> selectAllDTO() {
		System.out.println("List of TaEntrepotDTO EJB :");
		ArrayList<TaEntrepotDTO> liste = new ArrayList<TaEntrepotDTO>();

		List<TaEntrepot> projects = selectAll();
		for(TaEntrepot project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaEntrepotDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaEntrepotDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaEntrepotDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaEntrepotDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaEntrepotDTO dto, String validationContext) throws EJBException {
		try {
			TaEntrepotMapper mapper = new TaEntrepotMapper();
			TaEntrepot entity = null;
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
	
	public void persistDTO(TaEntrepotDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaEntrepotDTO dto, String validationContext) throws CreateException {
		try {
			TaEntrepotMapper mapper = new TaEntrepotMapper();
			TaEntrepot entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaEntrepotDTO dto) throws RemoveException {
		try {
			TaEntrepotMapper mapper = new TaEntrepotMapper();
			TaEntrepot entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaEntrepot refresh(TaEntrepot persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaEntrepot value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaEntrepot value, String propertyName, String validationContext) {
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
	public void validateDTO(TaEntrepotDTO dto, String validationContext) {
		try {
			TaEntrepotMapper mapper = new TaEntrepotMapper();
			TaEntrepot entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaEntrepotDTO> validator = new BeanValidator<TaEntrepotDTO>(TaEntrepotDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaEntrepotDTO dto, String propertyName, String validationContext) {
		try {
			TaEntrepotMapper mapper = new TaEntrepotMapper();
			TaEntrepot entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaEntrepotDTO> validator = new BeanValidator<TaEntrepotDTO>(TaEntrepotDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaEntrepotDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaEntrepotDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaEntrepot value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaEntrepot value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
