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

import fr.legrain.article.dao.IUniteDAO;
import fr.legrain.article.dto.TaUniteDTO;
import fr.legrain.article.model.TaUnite;
import fr.legrain.bdg.article.service.remote.ITaUniteServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaUniteMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaUniteBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaUniteService extends AbstractApplicationDAOServer<TaUnite, TaUniteDTO> implements ITaUniteServiceRemote {

	static Logger logger = Logger.getLogger(TaUniteService.class);

	@Inject private IUniteDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaUniteService() {
		super(TaUnite.class,TaUniteDTO.class);
	}
	
	public List<TaUniteDTO> findByCodeLight(String code){
		return dao.findByCodeLight(code);
	}
	
	public List<TaUniteDTO> findByCodeLightUniteStock(String codeUniteStock, String code) {
		return dao.findByCodeLightUniteStock(codeUniteStock, code);
	}
	public List<TaUnite> findByCodeUniteStock(String codeUniteStock, String code) {
		return dao.findByCodeUniteStock(codeUniteStock, code);
	}
	
	//	private String defaultJPQLQuery = "select a from TaUnite a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void persist(TaUnite transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaUnite transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaUnite persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdUnite()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaUnite merge(TaUnite detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaUnite merge(TaUnite detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaUnite findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaUnite findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaUnite> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaUniteDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaUniteDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaUnite> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaUniteDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaUniteDTO entityToDTO(TaUnite entity) {
//		TaUniteDTO dto = new TaUniteDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaUniteMapper mapper = new TaUniteMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaUniteDTO> listEntityToListDTO(List<TaUnite> entity) {
		List<TaUniteDTO> l = new ArrayList<TaUniteDTO>();

		for (TaUnite taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaUniteDTO> selectAllDTO() {
		System.out.println("List of TaUniteDTO EJB :");
		ArrayList<TaUniteDTO> liste = new ArrayList<TaUniteDTO>();

		List<TaUnite> projects = selectAll();
		for(TaUnite project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaUniteDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaUniteDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaUniteDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaUniteDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaUniteDTO dto, String validationContext) throws EJBException {
		try {
			TaUniteMapper mapper = new TaUniteMapper();
			TaUnite entity = null;
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
	
	public void persistDTO(TaUniteDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaUniteDTO dto, String validationContext) throws CreateException {
		try {
			TaUniteMapper mapper = new TaUniteMapper();
			TaUnite entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaUniteDTO dto) throws RemoveException {
		try {
			TaUniteMapper mapper = new TaUniteMapper();
			TaUnite entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaUnite refresh(TaUnite persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaUnite value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaUnite value, String propertyName, String validationContext) {
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
	public void validateDTO(TaUniteDTO dto, String validationContext) {
		try {
			TaUniteMapper mapper = new TaUniteMapper();
			TaUnite entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaUniteDTO> validator = new BeanValidator<TaUniteDTO>(TaUniteDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaUniteDTO dto, String propertyName, String validationContext) {
		try {
			TaUniteMapper mapper = new TaUniteMapper();
			TaUnite entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaUniteDTO> validator = new BeanValidator<TaUniteDTO>(TaUniteDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaUniteDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaUniteDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaUnite value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaUnite value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
