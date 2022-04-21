package fr.legrain.conformite.service;

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

import fr.legrain.bdg.conformite.service.remote.ITaListeConformiteServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaListeConformiteMapper;
import fr.legrain.conformite.dao.IListeConformiteDAO;
import fr.legrain.conformite.dto.TaListeConformiteDTO;
import fr.legrain.conformite.model.TaConformite;
import fr.legrain.conformite.model.TaListeConformite;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaListeConformiteBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaListeConformiteService extends AbstractApplicationDAOServer<TaListeConformite, TaListeConformiteDTO> implements ITaListeConformiteServiceRemote {

	static Logger logger = Logger.getLogger(TaListeConformiteService.class);

	@Inject private IListeConformiteDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaListeConformiteService() {
		super(TaListeConformite.class,TaListeConformiteDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaListeConformite a";
	
	public List<TaConformite>  findControleConformiteByCodeArticle(String codeArticle) {
		return dao.findControleConformiteByCodeArticle(codeArticle);
	}
	
	public List<TaListeConformite>  findByCodeArticle(String codeArticle) {
		return dao.findByCodeArticle(codeArticle);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaListeConformite transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaListeConformite transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaListeConformite persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdListeConformite()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaListeConformite merge(TaListeConformite detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaListeConformite merge(TaListeConformite detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaListeConformite findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaListeConformite findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaListeConformite> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaListeConformiteDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaListeConformiteDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaListeConformite> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaListeConformiteDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaListeConformiteDTO entityToDTO(TaListeConformite entity) {
//		TaListeConformiteDTO dto = new TaListeConformiteDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaListeConformiteMapper mapper = new TaListeConformiteMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaListeConformiteDTO> listEntityToListDTO(List<TaListeConformite> entity) {
		List<TaListeConformiteDTO> l = new ArrayList<TaListeConformiteDTO>();

		for (TaListeConformite taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaListeConformiteDTO> selectAllDTO() {
		System.out.println("List of TaListeConformiteDTO EJB :");
		ArrayList<TaListeConformiteDTO> liste = new ArrayList<TaListeConformiteDTO>();

		List<TaListeConformite> projects = selectAll();
		for(TaListeConformite project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaListeConformiteDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaListeConformiteDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaListeConformiteDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaListeConformiteDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaListeConformiteDTO dto, String validationContext) throws EJBException {
		try {
			TaListeConformiteMapper mapper = new TaListeConformiteMapper();
			TaListeConformite entity = null;
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
	
	public void persistDTO(TaListeConformiteDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaListeConformiteDTO dto, String validationContext) throws CreateException {
		try {
			TaListeConformiteMapper mapper = new TaListeConformiteMapper();
			TaListeConformite entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaListeConformiteDTO dto) throws RemoveException {
		try {
			TaListeConformiteMapper mapper = new TaListeConformiteMapper();
			TaListeConformite entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaListeConformite refresh(TaListeConformite persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaListeConformite value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaListeConformite value, String propertyName, String validationContext) {
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
	public void validateDTO(TaListeConformiteDTO dto, String validationContext) {
		try {
			TaListeConformiteMapper mapper = new TaListeConformiteMapper();
			TaListeConformite entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaListeConformiteDTO> validator = new BeanValidator<TaListeConformiteDTO>(TaListeConformiteDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaListeConformiteDTO dto, String propertyName, String validationContext) {
		try {
			TaListeConformiteMapper mapper = new TaListeConformiteMapper();
			TaListeConformite entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaListeConformiteDTO> validator = new BeanValidator<TaListeConformiteDTO>(TaListeConformiteDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaListeConformiteDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaListeConformiteDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaListeConformite value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaListeConformite value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
