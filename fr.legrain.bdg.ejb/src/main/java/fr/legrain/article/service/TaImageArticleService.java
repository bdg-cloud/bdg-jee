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

import fr.legrain.article.dao.IArticleDAO;
import fr.legrain.article.dao.IImageArticleDAO;
import fr.legrain.article.dto.TaImageArticleDTO;
import fr.legrain.article.model.TaImageArticle;
import fr.legrain.bdg.article.service.remote.ITaImageArticleServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaImageArticleMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaImageArticleBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaImageArticleService extends AbstractApplicationDAOServer<TaImageArticle, TaImageArticleDTO> implements ITaImageArticleServiceRemote {

	static Logger logger = Logger.getLogger(TaImageArticleService.class);

	@Inject private IImageArticleDAO dao;
	@Inject private IArticleDAO daoArticle;

	/**
	 * Default constructor. 
	 */
	public TaImageArticleService() {
		super(TaImageArticle.class,TaImageArticleDTO.class);
	}
	
	public List<TaImageArticle> findByArticle(int id) {
		return dao.findByArticle(id);
	}
	
	public List<TaImageArticleDTO> findByArticleLight(int id) {
		return dao.findByArticleLight(id);
	}
	
	public void changeImageDefaut(TaImageArticle image) {
		dao.changeImageDefaut(image);
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaImageArticle transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaImageArticle transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaImageArticle persistentInstance) throws RemoveException {
		try {
			persistentInstance = findById(persistentInstance.getIdImageArticle());
			daoArticle.findById(persistentInstance.getTaArticle().getIdArticle());
			//persistentInstance.getTaArticle().removeImageArticle(persistentInstance);
			if(persistentInstance.getTaArticle().getTaImageArticles()!=null && persistentInstance.getTaArticle().getTaImageArticles().size()==1) {
				persistentInstance.getTaArticle().setTaImageArticle(null);
				persistentInstance.getTaArticle().getTaImageArticles().clear();
			}
			daoArticle.merge(persistentInstance.getTaArticle());
			
			dao.remove(findById(persistentInstance.getIdImageArticle()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaImageArticle merge(TaImageArticle detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaImageArticle merge(TaImageArticle detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaImageArticle findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaImageArticle findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaImageArticle> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaImageArticleDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaImageArticleDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaImageArticle> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaImageArticleDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaImageArticleDTO entityToDTO(TaImageArticle entity) {
//		TaImageArticleDTO dto = new TaImageArticleDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaImageArticleMapper mapper = new TaImageArticleMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaImageArticleDTO> listEntityToListDTO(List<TaImageArticle> entity) {
		List<TaImageArticleDTO> l = new ArrayList<TaImageArticleDTO>();

		for (TaImageArticle taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaImageArticleDTO> selectAllDTO() {
		System.out.println("List of TaImageArticleDTO EJB :");
		ArrayList<TaImageArticleDTO> liste = new ArrayList<TaImageArticleDTO>();

		List<TaImageArticle> projects = selectAll();
		for(TaImageArticle project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaImageArticleDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaImageArticleDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaImageArticleDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaImageArticleDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaImageArticleDTO dto, String validationContext) throws EJBException {
		try {
			TaImageArticleMapper mapper = new TaImageArticleMapper();
			TaImageArticle entity = null;
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
	
	public void persistDTO(TaImageArticleDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaImageArticleDTO dto, String validationContext) throws CreateException {
		try {
			TaImageArticleMapper mapper = new TaImageArticleMapper();
			TaImageArticle entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaImageArticleDTO dto) throws RemoveException {
		try {
			TaImageArticleMapper mapper = new TaImageArticleMapper();
			TaImageArticle entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaImageArticle refresh(TaImageArticle persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaImageArticle value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaImageArticle value, String propertyName, String validationContext) {
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
	public void validateDTO(TaImageArticleDTO dto, String validationContext) {
		try {
			TaImageArticleMapper mapper = new TaImageArticleMapper();
			TaImageArticle entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaImageArticleDTO> validator = new BeanValidator<TaImageArticleDTO>(TaImageArticleDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaImageArticleDTO dto, String propertyName, String validationContext) {
		try {
			TaImageArticleMapper mapper = new TaImageArticleMapper();
			TaImageArticle entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaImageArticleDTO> validator = new BeanValidator<TaImageArticleDTO>(TaImageArticleDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaImageArticleDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaImageArticleDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaImageArticle value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaImageArticle value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
