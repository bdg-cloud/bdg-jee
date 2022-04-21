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

import fr.legrain.abonnement.stripe.dao.IStripeCouponDAO;
import fr.legrain.abonnement.stripe.dao.IStripeProductDAO;
import fr.legrain.abonnement.stripe.dto.TaStripeCouponDTO;
import fr.legrain.abonnement.stripe.model.TaStripeCoupon;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeCouponServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaStripeCouponMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaStripeCouponBean
 */
@SuppressWarnings("deprecation")
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaStripeCouponService extends AbstractApplicationDAOServer<TaStripeCoupon, TaStripeCouponDTO> implements ITaStripeCouponServiceRemote {

	static Logger logger = Logger.getLogger(TaStripeCouponService.class);

	@Inject private IStripeCouponDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaStripeCouponService() {
		super(TaStripeCoupon.class,TaStripeCouponDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaStripeCoupon a";
	
	public List<TaStripeCouponDTO> findByCodeLight(String code) {
		return dao.findByCodeLight(code);
	}
	
	public List<TaStripeCouponDTO> findAllLight() {
		return dao.findAllLight();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaStripeCoupon transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaStripeCoupon transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaStripeCoupon persistentInstance) throws RemoveException {
		dao.remove(dao.findById(persistentInstance.getIdStripeCoupon()));
	}
	
	public TaStripeCoupon merge(TaStripeCoupon detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaStripeCoupon merge(TaStripeCoupon detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaStripeCoupon findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaStripeCoupon findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaStripeCoupon> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaStripeCouponDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaStripeCouponDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaStripeCoupon> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaStripeCouponDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaStripeCouponDTO entityToDTO(TaStripeCoupon entity) {
//		TaStripeCouponDTO dto = new TaStripeCouponDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaStripeCouponMapper mapper = new TaStripeCouponMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaStripeCouponDTO> listEntityToListDTO(List<TaStripeCoupon> entity) {
		List<TaStripeCouponDTO> l = new ArrayList<TaStripeCouponDTO>();

		for (TaStripeCoupon taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaStripeCouponDTO> selectAllDTO() {
		System.out.println("List of TaStripeCouponDTO EJB :");
		ArrayList<TaStripeCouponDTO> liste = new ArrayList<TaStripeCouponDTO>();

		List<TaStripeCoupon> projects = selectAll();
		for(TaStripeCoupon project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaStripeCouponDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaStripeCouponDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaStripeCouponDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaStripeCouponDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaStripeCouponDTO dto, String validationContext) throws EJBException {
		try {
			TaStripeCouponMapper mapper = new TaStripeCouponMapper();
			TaStripeCoupon entity = null;
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
	
	public void persistDTO(TaStripeCouponDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaStripeCouponDTO dto, String validationContext) throws CreateException {
		try {
			TaStripeCouponMapper mapper = new TaStripeCouponMapper();
			TaStripeCoupon entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaStripeCouponDTO dto) throws RemoveException {
		try {
			TaStripeCouponMapper mapper = new TaStripeCouponMapper();
			TaStripeCoupon entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaStripeCoupon refresh(TaStripeCoupon persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaStripeCoupon value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaStripeCoupon value, String propertyName, String validationContext) {
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
	public void validateDTO(TaStripeCouponDTO dto, String validationContext) {
		try {
			TaStripeCouponMapper mapper = new TaStripeCouponMapper();
			TaStripeCoupon entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaStripeCouponDTO> validator = new BeanValidator<TaStripeCouponDTO>(TaStripeCouponDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaStripeCouponDTO dto, String propertyName, String validationContext) {
		try {
			TaStripeCouponMapper mapper = new TaStripeCouponMapper();
			TaStripeCoupon entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaStripeCouponDTO> validator = new BeanValidator<TaStripeCouponDTO>(TaStripeCouponDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaStripeCouponDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaStripeCouponDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaStripeCoupon value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaStripeCoupon value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
