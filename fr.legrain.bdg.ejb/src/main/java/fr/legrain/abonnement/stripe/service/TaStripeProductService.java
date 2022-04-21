package fr.legrain.abonnement.stripe.service;

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

import fr.legrain.abonnement.stripe.dao.IStripeProductDAO;
import fr.legrain.abonnement.stripe.dto.TaStripeProductDTO;
import fr.legrain.abonnement.stripe.model.TaStripeProduct;
import fr.legrain.article.model.TaArticle;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeProductServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaStripeProductMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaStripeProductBean
 */
@SuppressWarnings("deprecation")
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaStripeProductService extends AbstractApplicationDAOServer<TaStripeProduct, TaStripeProductDTO> implements ITaStripeProductServiceRemote {

	static Logger logger = Logger.getLogger(TaStripeProductService.class);

	@Inject private IStripeProductDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaStripeProductService() {
		super(TaStripeProduct.class,TaStripeProductDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaStripeProduct a";
	
	public List<TaStripeProductDTO> findByCodeLight(String code) {
		return dao.findByCodeLight(code);
	}
	
	public List<TaStripeProductDTO> findAllLight() {
		return dao.findAllLight();
	}
	
	public TaStripeProduct rechercherProduct(TaArticle article) {
		return dao.rechercherProduct(article);
	}
	public TaStripeProduct rechercherProduct(String idExterne) {
		return dao.rechercherProduct(idExterne);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaStripeProduct transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaStripeProduct transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaStripeProduct persistentInstance) throws RemoveException {
		dao.remove(dao.findById(persistentInstance.getIdStripeProduct()));
	}
	
	public TaStripeProduct merge(TaStripeProduct detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaStripeProduct merge(TaStripeProduct detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaStripeProduct findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaStripeProduct findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaStripeProduct> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaStripeProductDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaStripeProductDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaStripeProduct> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaStripeProductDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}
	
	public List<TaStripeProductDTO> selectAllDTOAvecPlan(){
		return dao.selectAllDTOAvecPlan();
	}
	
	public List<TaStripeProductDTO> selectAllDTOAvecPlanByIdFrequence(Integer idFrequence){
		return dao.selectAllDTOAvecPlanByIdFrequence( idFrequence);
	}

	public TaStripeProductDTO entityToDTO(TaStripeProduct entity) {
//		TaStripeProductDTO dto = new TaStripeProductDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaStripeProductMapper mapper = new TaStripeProductMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaStripeProductDTO> listEntityToListDTO(List<TaStripeProduct> entity) {
		List<TaStripeProductDTO> l = new ArrayList<TaStripeProductDTO>();

		for (TaStripeProduct taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaStripeProductDTO> selectAllDTO() {
		System.out.println("List of TaStripeProductDTO EJB :");
		ArrayList<TaStripeProductDTO> liste = new ArrayList<TaStripeProductDTO>();

		List<TaStripeProduct> projects = selectAll();
		for(TaStripeProduct project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaStripeProductDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaStripeProductDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaStripeProductDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaStripeProductDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaStripeProductDTO dto, String validationContext) throws EJBException {
		try {
			TaStripeProductMapper mapper = new TaStripeProductMapper();
			TaStripeProduct entity = null;
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
	
	public void persistDTO(TaStripeProductDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaStripeProductDTO dto, String validationContext) throws CreateException {
		try {
			TaStripeProductMapper mapper = new TaStripeProductMapper();
			TaStripeProduct entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaStripeProductDTO dto) throws RemoveException {
		try {
			TaStripeProductMapper mapper = new TaStripeProductMapper();
			TaStripeProduct entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaStripeProduct refresh(TaStripeProduct persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaStripeProduct value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaStripeProduct value, String propertyName, String validationContext) {
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
	public void validateDTO(TaStripeProductDTO dto, String validationContext) {
		try {
			TaStripeProductMapper mapper = new TaStripeProductMapper();
			TaStripeProduct entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaStripeProductDTO> validator = new BeanValidator<TaStripeProductDTO>(TaStripeProductDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaStripeProductDTO dto, String propertyName, String validationContext) {
		try {
			TaStripeProductMapper mapper = new TaStripeProductMapper();
			TaStripeProduct entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaStripeProductDTO> validator = new BeanValidator<TaStripeProductDTO>(TaStripeProductDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaStripeProductDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaStripeProductDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaStripeProduct value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaStripeProduct value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
