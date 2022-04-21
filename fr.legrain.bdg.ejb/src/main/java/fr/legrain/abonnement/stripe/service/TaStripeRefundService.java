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

import fr.legrain.abonnement.stripe.dao.IStripeChargeDAO;
import fr.legrain.abonnement.stripe.dao.IStripeProductDAO;
import fr.legrain.abonnement.stripe.dao.IStripeRefundDAO;
import fr.legrain.abonnement.stripe.dto.TaStripeRefundDTO;
import fr.legrain.abonnement.stripe.model.TaStripeRefund;
import fr.legrain.abonnement.stripe.model.TaStripeRefund;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeRefundServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaStripeRefundMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaStripeRefundBean
 */
@SuppressWarnings("deprecation")
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaStripeRefundService extends AbstractApplicationDAOServer<TaStripeRefund, TaStripeRefundDTO> implements ITaStripeRefundServiceRemote {

	static Logger logger = Logger.getLogger(TaStripeRefundService.class);

	@Inject private IStripeRefundDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaStripeRefundService() {
		super(TaStripeRefund.class,TaStripeRefundDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaStripeRefund a";
	
	public List<TaStripeRefundDTO> findByCodeLight(String code) {
		return dao.findByCodeLight(code);
	}
	
	public List<TaStripeRefundDTO> findAllLight() {
		return dao.findAllLight();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaStripeRefund transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaStripeRefund transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaStripeRefund persistentInstance) throws RemoveException {
		dao.remove(dao.findById(persistentInstance.getIdStripeRefund()));
	}
	
	public TaStripeRefund merge(TaStripeRefund detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaStripeRefund merge(TaStripeRefund detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaStripeRefund findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaStripeRefund findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaStripeRefund> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaStripeRefundDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaStripeRefundDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaStripeRefund> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaStripeRefundDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaStripeRefundDTO entityToDTO(TaStripeRefund entity) {
//		TaStripeRefundDTO dto = new TaStripeRefundDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaStripeRefundMapper mapper = new TaStripeRefundMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaStripeRefundDTO> listEntityToListDTO(List<TaStripeRefund> entity) {
		List<TaStripeRefundDTO> l = new ArrayList<TaStripeRefundDTO>();

		for (TaStripeRefund taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaStripeRefundDTO> selectAllDTO() {
		System.out.println("List of TaStripeRefundDTO EJB :");
		ArrayList<TaStripeRefundDTO> liste = new ArrayList<TaStripeRefundDTO>();

		List<TaStripeRefund> projects = selectAll();
		for(TaStripeRefund project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaStripeRefundDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaStripeRefundDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaStripeRefundDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaStripeRefundDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaStripeRefundDTO dto, String validationContext) throws EJBException {
		try {
			TaStripeRefundMapper mapper = new TaStripeRefundMapper();
			TaStripeRefund entity = null;
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
	
	public void persistDTO(TaStripeRefundDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaStripeRefundDTO dto, String validationContext) throws CreateException {
		try {
			TaStripeRefundMapper mapper = new TaStripeRefundMapper();
			TaStripeRefund entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaStripeRefundDTO dto) throws RemoveException {
		try {
			TaStripeRefundMapper mapper = new TaStripeRefundMapper();
			TaStripeRefund entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaStripeRefund refresh(TaStripeRefund persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaStripeRefund value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaStripeRefund value, String propertyName, String validationContext) {
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
	public void validateDTO(TaStripeRefundDTO dto, String validationContext) {
		try {
			TaStripeRefundMapper mapper = new TaStripeRefundMapper();
			TaStripeRefund entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaStripeRefundDTO> validator = new BeanValidator<TaStripeRefundDTO>(TaStripeRefundDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaStripeRefundDTO dto, String propertyName, String validationContext) {
		try {
			TaStripeRefundMapper mapper = new TaStripeRefundMapper();
			TaStripeRefund entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaStripeRefundDTO> validator = new BeanValidator<TaStripeRefundDTO>(TaStripeRefundDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaStripeRefundDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaStripeRefundDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaStripeRefund value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaStripeRefund value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
