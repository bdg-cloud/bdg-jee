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

import fr.legrain.article.dao.IFamilleUniteDAO;
import fr.legrain.article.dto.TaFamilleUniteDTO;
import fr.legrain.article.model.TaFamilleUnite;
import fr.legrain.bdg.article.service.remote.ITaFamilleUniteServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaFamilleUniteMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaFamilleUniteBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaFamilleUniteService extends AbstractApplicationDAOServer<TaFamilleUnite, TaFamilleUniteDTO> implements ITaFamilleUniteServiceRemote {

	static Logger logger = Logger.getLogger(TaFamilleUniteService.class);

	@Inject private IFamilleUniteDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaFamilleUniteService() {
		super(TaFamilleUnite.class,TaFamilleUniteDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaFamilleUnite a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaFamilleUnite transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaFamilleUnite transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaFamilleUnite persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdFamille()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaFamilleUnite merge(TaFamilleUnite detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaFamilleUnite merge(TaFamilleUnite detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaFamilleUnite findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaFamilleUnite findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaFamilleUnite> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaFamilleUniteDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaFamilleUniteDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaFamilleUnite> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaFamilleUniteDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaFamilleUniteDTO entityToDTO(TaFamilleUnite entity) {
//		TaFamilleUniteDTO dto = new TaFamilleUniteDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaFamilleUniteMapper mapper = new TaFamilleUniteMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaFamilleUniteDTO> listEntityToListDTO(List<TaFamilleUnite> entity) {
		List<TaFamilleUniteDTO> l = new ArrayList<TaFamilleUniteDTO>();

		for (TaFamilleUnite taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaFamilleUniteDTO> selectAllDTO() {
		System.out.println("List of TaFamilleUniteDTO EJB :");
		ArrayList<TaFamilleUniteDTO> liste = new ArrayList<TaFamilleUniteDTO>();

		List<TaFamilleUnite> projects = selectAll();
		for(TaFamilleUnite project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaFamilleUniteDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaFamilleUniteDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaFamilleUniteDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaFamilleUniteDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaFamilleUniteDTO dto, String validationContext) throws EJBException {
		try {
			TaFamilleUniteMapper mapper = new TaFamilleUniteMapper();
			TaFamilleUnite entity = null;
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
	
	public void persistDTO(TaFamilleUniteDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaFamilleUniteDTO dto, String validationContext) throws CreateException {
		try {
			TaFamilleUniteMapper mapper = new TaFamilleUniteMapper();
			TaFamilleUnite entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaFamilleUniteDTO dto) throws RemoveException {
		try {
			TaFamilleUniteMapper mapper = new TaFamilleUniteMapper();
			TaFamilleUnite entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaFamilleUnite refresh(TaFamilleUnite persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaFamilleUnite value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaFamilleUnite value, String propertyName, String validationContext) {
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
	public void validateDTO(TaFamilleUniteDTO dto, String validationContext) {
		try {
			TaFamilleUniteMapper mapper = new TaFamilleUniteMapper();
			TaFamilleUnite entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaFamilleUniteDTO> validator = new BeanValidator<TaFamilleUniteDTO>(TaFamilleUniteDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaFamilleUniteDTO dto, String propertyName, String validationContext) {
		try {
			TaFamilleUniteMapper mapper = new TaFamilleUniteMapper();
			TaFamilleUnite entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaFamilleUniteDTO> validator = new BeanValidator<TaFamilleUniteDTO>(TaFamilleUniteDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaFamilleUniteDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaFamilleUniteDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaFamilleUnite value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaFamilleUnite value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
