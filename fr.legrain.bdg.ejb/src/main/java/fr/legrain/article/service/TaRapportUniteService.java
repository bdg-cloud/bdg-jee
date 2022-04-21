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

import fr.legrain.article.dao.IRapportUniteDAO;
import fr.legrain.article.dto.TaRapportUniteDTO;
import fr.legrain.article.model.TaRapportUnite;
import fr.legrain.bdg.article.service.remote.ITaRapportUniteServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaRapportUniteMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaRapportUniteBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaRapportUniteService extends AbstractApplicationDAOServer<TaRapportUnite, TaRapportUniteDTO> implements ITaRapportUniteServiceRemote {

	static Logger logger = Logger.getLogger(TaRapportUniteService.class);

	@Inject private IRapportUniteDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaRapportUniteService() {
		super(TaRapportUnite.class,TaRapportUniteDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaRapportUnite a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaRapportUnite transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaRapportUnite transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaRapportUnite persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getId()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaRapportUnite merge(TaRapportUnite detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaRapportUnite merge(TaRapportUnite detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaRapportUnite findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaRapportUnite findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaRapportUnite> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaRapportUniteDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaRapportUniteDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaRapportUnite> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaRapportUniteDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaRapportUniteDTO entityToDTO(TaRapportUnite entity) {
//		TaRapportUniteDTO dto = new TaRapportUniteDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaRapportUniteMapper mapper = new TaRapportUniteMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaRapportUniteDTO> listEntityToListDTO(List<TaRapportUnite> entity) {
		List<TaRapportUniteDTO> l = new ArrayList<TaRapportUniteDTO>();

		for (TaRapportUnite taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaRapportUniteDTO> selectAllDTO() {
		System.out.println("List of TaRapportUniteDTO EJB :");
		ArrayList<TaRapportUniteDTO> liste = new ArrayList<TaRapportUniteDTO>();

		List<TaRapportUnite> projects = selectAll();
		for(TaRapportUnite project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaRapportUniteDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaRapportUniteDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaRapportUniteDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaRapportUniteDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaRapportUniteDTO dto, String validationContext) throws EJBException {
		try {
			TaRapportUniteMapper mapper = new TaRapportUniteMapper();
			TaRapportUnite entity = null;
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
	
	public void persistDTO(TaRapportUniteDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaRapportUniteDTO dto, String validationContext) throws CreateException {
		try {
			TaRapportUniteMapper mapper = new TaRapportUniteMapper();
			TaRapportUnite entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaRapportUniteDTO dto) throws RemoveException {
		try {
			TaRapportUniteMapper mapper = new TaRapportUniteMapper();
			TaRapportUnite entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaRapportUnite refresh(TaRapportUnite persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaRapportUnite value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaRapportUnite value, String propertyName, String validationContext) {
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
	public void validateDTO(TaRapportUniteDTO dto, String validationContext) {
		try {
			TaRapportUniteMapper mapper = new TaRapportUniteMapper();
			TaRapportUnite entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaRapportUniteDTO> validator = new BeanValidator<TaRapportUniteDTO>(TaRapportUniteDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaRapportUniteDTO dto, String propertyName, String validationContext) {
		try {
			TaRapportUniteMapper mapper = new TaRapportUniteMapper();
			TaRapportUnite entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaRapportUniteDTO> validator = new BeanValidator<TaRapportUniteDTO>(TaRapportUniteDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaRapportUniteDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaRapportUniteDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaRapportUnite value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaRapportUnite value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

	@Override
	public TaRapportUnite findByCode1(String code) {
		// TODO Auto-generated method stub
		return dao.findByCode1(code);
	}

	@Override
	public TaRapportUnite findByCode2(String code) {
		// TODO Auto-generated method stub
		return dao.findByCode2(code);
	}

	@Override
	public TaRapportUnite findByCode1And2(String code1, String code2) {
		// TODO Auto-generated method stub
		return dao.findByCode1And2(code1, code2);
	}

}
