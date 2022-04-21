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

import fr.legrain.abonnement.stripe.dao.IStripeDiscountDAO;
import fr.legrain.abonnement.stripe.dao.IStripeProductDAO;
import fr.legrain.abonnement.stripe.dto.TaStripeDiscountDTO;
import fr.legrain.abonnement.stripe.model.TaStripeDiscount;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeDiscountServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaStripeDiscountMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaStripeDiscountBean
 */
@SuppressWarnings("deprecation")
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaStripeDiscountService extends AbstractApplicationDAOServer<TaStripeDiscount, TaStripeDiscountDTO> implements ITaStripeDiscountServiceRemote {

	static Logger logger = Logger.getLogger(TaStripeDiscountService.class);

	@Inject private IStripeDiscountDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaStripeDiscountService() {
		super(TaStripeDiscount.class,TaStripeDiscountDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaStripeDiscount a";
	
	public List<TaStripeDiscountDTO> findByCodeLight(String code) {
		return dao.findByCodeLight(code);
	}
	
	public List<TaStripeDiscountDTO> findAllLight() {
		return dao.findAllLight();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaStripeDiscount transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaStripeDiscount transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaStripeDiscount persistentInstance) throws RemoveException {
		dao.remove(dao.findById(persistentInstance.getIdStripeDiscount()));
	}
	
	public TaStripeDiscount merge(TaStripeDiscount detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaStripeDiscount merge(TaStripeDiscount detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaStripeDiscount findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaStripeDiscount findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaStripeDiscount> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaStripeDiscountDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaStripeDiscountDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaStripeDiscount> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaStripeDiscountDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaStripeDiscountDTO entityToDTO(TaStripeDiscount entity) {
//		TaStripeDiscountDTO dto = new TaStripeDiscountDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaStripeDiscountMapper mapper = new TaStripeDiscountMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaStripeDiscountDTO> listEntityToListDTO(List<TaStripeDiscount> entity) {
		List<TaStripeDiscountDTO> l = new ArrayList<TaStripeDiscountDTO>();

		for (TaStripeDiscount taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaStripeDiscountDTO> selectAllDTO() {
		System.out.println("List of TaStripeDiscountDTO EJB :");
		ArrayList<TaStripeDiscountDTO> liste = new ArrayList<TaStripeDiscountDTO>();

		List<TaStripeDiscount> projects = selectAll();
		for(TaStripeDiscount project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaStripeDiscountDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaStripeDiscountDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaStripeDiscountDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaStripeDiscountDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaStripeDiscountDTO dto, String validationContext) throws EJBException {
		try {
			TaStripeDiscountMapper mapper = new TaStripeDiscountMapper();
			TaStripeDiscount entity = null;
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
	
	public void persistDTO(TaStripeDiscountDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaStripeDiscountDTO dto, String validationContext) throws CreateException {
		try {
			TaStripeDiscountMapper mapper = new TaStripeDiscountMapper();
			TaStripeDiscount entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaStripeDiscountDTO dto) throws RemoveException {
		try {
			TaStripeDiscountMapper mapper = new TaStripeDiscountMapper();
			TaStripeDiscount entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaStripeDiscount refresh(TaStripeDiscount persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaStripeDiscount value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaStripeDiscount value, String propertyName, String validationContext) {
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
	public void validateDTO(TaStripeDiscountDTO dto, String validationContext) {
		try {
			TaStripeDiscountMapper mapper = new TaStripeDiscountMapper();
			TaStripeDiscount entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaStripeDiscountDTO> validator = new BeanValidator<TaStripeDiscountDTO>(TaStripeDiscountDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaStripeDiscountDTO dto, String propertyName, String validationContext) {
		try {
			TaStripeDiscountMapper mapper = new TaStripeDiscountMapper();
			TaStripeDiscount entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaStripeDiscountDTO> validator = new BeanValidator<TaStripeDiscountDTO>(TaStripeDiscountDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaStripeDiscountDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaStripeDiscountDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaStripeDiscount value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaStripeDiscount value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
