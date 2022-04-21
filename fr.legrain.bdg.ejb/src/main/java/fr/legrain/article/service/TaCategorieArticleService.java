package fr.legrain.article.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.ejb.CreateException;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.article.dao.ICategorieArticleDAO;
import fr.legrain.article.dto.TaCategorieArticleDTO;
import fr.legrain.article.model.TaCategorieArticle;
import fr.legrain.bdg.article.service.remote.ITaCategorieArticleServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaCategorieArticleMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.model.TaFacture;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;

/**
 * Session Bean implementation class TaCategorieArticleBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaCategorieArticleService extends AbstractApplicationDAOServer<TaCategorieArticle, TaCategorieArticleDTO> implements ITaCategorieArticleServiceRemote {

	static Logger logger = Logger.getLogger(TaCategorieArticleService.class);

	@Inject private ICategorieArticleDAO dao;
	@EJB private ITaGenCodeExServiceRemote gencode;
	@Inject private	SessionInfo sessionInfo;

	/**
	 * Default constructor. 
	 */
	public TaCategorieArticleService() {
		super(TaCategorieArticle.class,TaCategorieArticleDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaCategorieArticle a";
	
	public List<TaCategorieArticle> findByCodeCategorieParent(String codeCategorieParent) {
		return dao.findByCodeCategorieParent(codeCategorieParent);
	}
	
	public List<TaCategorieArticle> findCategorieMere() {
		return dao.findCategorieMere();
	}
	
	public List<TaCategorieArticleDTO> findByCodeLight(String code) {
		return dao.findByCodeLight(code);
	}
	
	public List<TaCategorieArticleDTO> findAllLight() {
		return dao.findAllLight();
	}
	
	public List<TaCategorieArticleDTO> findCategorieDTOByIdArticle(int idArticle) {
		return dao.findCategorieDTOByIdArticle(idArticle);
	}
	
	public List<TaCategorieArticle> findCategorieByIdArticle(int idArticle) {
		return dao.findCategorieByIdArticle(idArticle);
	}
	
	public String genereCode( Map<String, String> params) {
		//return dao.genereCode();
		try {
			return gencode.genereCodeJPA(TaCategorieArticle.class.getSimpleName(),params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NOUVEAU CODE";
	}
	
	public void annuleCode(String code) {
		try {
			
			gencode.annulerCodeGenere(gencode.findByCode(TaCategorieArticle.class.getSimpleName()),code);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void verrouilleCode(String code) {
		try {
			gencode.rentreCodeGenere(gencode.findByCode(TaCategorieArticle.class.getSimpleName()),code, sessionInfo.getSessionID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaCategorieArticle transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaCategorieArticle transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaCategorieArticle persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdCategorieArticle()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaCategorieArticle merge(TaCategorieArticle detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaCategorieArticle merge(TaCategorieArticle detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaCategorieArticle findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaCategorieArticle findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaCategorieArticle> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaCategorieArticleDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaCategorieArticleDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaCategorieArticle> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaCategorieArticleDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaCategorieArticleDTO entityToDTO(TaCategorieArticle entity) {
//		TaCategorieArticleDTO dto = new TaCategorieArticleDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaCategorieArticleMapper mapper = new TaCategorieArticleMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaCategorieArticleDTO> listEntityToListDTO(List<TaCategorieArticle> entity) {
		List<TaCategorieArticleDTO> l = new ArrayList<TaCategorieArticleDTO>();

		for (TaCategorieArticle taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaCategorieArticleDTO> selectAllDTO() {
		System.out.println("List of TaCategorieArticleDTO EJB :");
		ArrayList<TaCategorieArticleDTO> liste = new ArrayList<TaCategorieArticleDTO>();

		List<TaCategorieArticle> projects = selectAll();
		for(TaCategorieArticle project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaCategorieArticleDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaCategorieArticleDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaCategorieArticleDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaCategorieArticleDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaCategorieArticleDTO dto, String validationContext) throws EJBException {
		try {
			TaCategorieArticleMapper mapper = new TaCategorieArticleMapper();
			TaCategorieArticle entity = null;
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
	
	public void persistDTO(TaCategorieArticleDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaCategorieArticleDTO dto, String validationContext) throws CreateException {
		try {
			TaCategorieArticleMapper mapper = new TaCategorieArticleMapper();
			TaCategorieArticle entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaCategorieArticleDTO dto) throws RemoveException {
		try {
			TaCategorieArticleMapper mapper = new TaCategorieArticleMapper();
			TaCategorieArticle entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaCategorieArticle refresh(TaCategorieArticle persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaCategorieArticle value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaCategorieArticle value, String propertyName, String validationContext) {
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
	public void validateDTO(TaCategorieArticleDTO dto, String validationContext) {
		try {
			TaCategorieArticleMapper mapper = new TaCategorieArticleMapper();
			TaCategorieArticle entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaCategorieArticleDTO> validator = new BeanValidator<TaCategorieArticleDTO>(TaCategorieArticleDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaCategorieArticleDTO dto, String propertyName, String validationContext) {
		try {
			TaCategorieArticleMapper mapper = new TaCategorieArticleMapper();
			TaCategorieArticle entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaCategorieArticleDTO> validator = new BeanValidator<TaCategorieArticleDTO>(TaCategorieArticleDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaCategorieArticleDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaCategorieArticleDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaCategorieArticle value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaCategorieArticle value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
