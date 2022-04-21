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

import fr.legrain.article.dao.ICatalogueWebDAO;
import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.TaCatalogueWebDTO;
import fr.legrain.article.dto.TaCategorieArticleDTO;
import fr.legrain.article.model.TaCatalogueWeb;
import fr.legrain.bdg.article.service.remote.ITaCatalogueWebServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaCatalogueWebMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaCatalogueWebBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaCatalogueWebService extends AbstractApplicationDAOServer<TaCatalogueWeb, TaCatalogueWebDTO> implements ITaCatalogueWebServiceRemote {

	static Logger logger = Logger.getLogger(TaCatalogueWebService.class);

	@Inject private ICatalogueWebDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaCatalogueWebService() {
		super(TaCatalogueWeb.class,TaCatalogueWebDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaCatalogueWeb a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaCatalogueWeb transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaCatalogueWeb transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaCatalogueWeb persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdCatalogueWeb()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaCatalogueWeb merge(TaCatalogueWeb detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaCatalogueWeb merge(TaCatalogueWeb detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaCatalogueWeb findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaCatalogueWeb findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaCatalogueWeb> selectAll() {
		return dao.selectAll();
	}
	
	public List<TaArticleDTO> findListeArticleCatalogue() {
		return dao.findListeArticleCatalogue();
	}
	
	public List<TaArticleDTO> findListeArticleCatalogue(int idCategorie) {
		return dao.findListeArticleCatalogue(idCategorie);
	}
	
	public TaArticleDTO findArticleCatalogue(int idArticle) {
		return dao.findArticleCatalogue(idArticle);
	}
	
	public List<TaCategorieArticleDTO> findListeCategorieArticleCatalogue() {
		return dao.findListeCategorieArticleCatalogue();
	}
	
	public TaCategorieArticleDTO findCategorieArticleCatalogue(int idCategorie) {
		return dao.findCategorieArticleCatalogue(idCategorie);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaCatalogueWebDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaCatalogueWebDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaCatalogueWeb> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaCatalogueWebDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaCatalogueWebDTO entityToDTO(TaCatalogueWeb entity) {
//		TaCatalogueWebDTO dto = new TaCatalogueWebDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaCatalogueWebMapper mapper = new TaCatalogueWebMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaCatalogueWebDTO> listEntityToListDTO(List<TaCatalogueWeb> entity) {
		List<TaCatalogueWebDTO> l = new ArrayList<TaCatalogueWebDTO>();

		for (TaCatalogueWeb taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaCatalogueWebDTO> selectAllDTO() {
		System.out.println("List of TaCatalogueWebDTO EJB :");
		ArrayList<TaCatalogueWebDTO> liste = new ArrayList<TaCatalogueWebDTO>();

		List<TaCatalogueWeb> projects = selectAll();
		for(TaCatalogueWeb project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaCatalogueWebDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaCatalogueWebDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaCatalogueWebDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaCatalogueWebDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaCatalogueWebDTO dto, String validationContext) throws EJBException {
		try {
			TaCatalogueWebMapper mapper = new TaCatalogueWebMapper();
			TaCatalogueWeb entity = null;
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
	
	public void persistDTO(TaCatalogueWebDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaCatalogueWebDTO dto, String validationContext) throws CreateException {
		try {
			TaCatalogueWebMapper mapper = new TaCatalogueWebMapper();
			TaCatalogueWeb entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaCatalogueWebDTO dto) throws RemoveException {
		try {
			TaCatalogueWebMapper mapper = new TaCatalogueWebMapper();
			TaCatalogueWeb entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaCatalogueWeb refresh(TaCatalogueWeb persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaCatalogueWeb value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaCatalogueWeb value, String propertyName, String validationContext) {
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
	public void validateDTO(TaCatalogueWebDTO dto, String validationContext) {
		try {
			TaCatalogueWebMapper mapper = new TaCatalogueWebMapper();
			TaCatalogueWeb entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaCatalogueWebDTO> validator = new BeanValidator<TaCatalogueWebDTO>(TaCatalogueWebDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaCatalogueWebDTO dto, String propertyName, String validationContext) {
		try {
			TaCatalogueWebMapper mapper = new TaCatalogueWebMapper();
			TaCatalogueWeb entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaCatalogueWebDTO> validator = new BeanValidator<TaCatalogueWebDTO>(TaCatalogueWebDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaCatalogueWebDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaCatalogueWebDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaCatalogueWeb value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaCatalogueWeb value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
