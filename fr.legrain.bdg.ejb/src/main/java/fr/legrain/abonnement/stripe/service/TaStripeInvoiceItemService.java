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

import fr.legrain.abonnement.stripe.dao.IStripeInvoiceItemDAO;
import fr.legrain.abonnement.stripe.dao.IStripeProductDAO;
import fr.legrain.abonnement.stripe.dto.TaStripeInvoiceItemDTO;
import fr.legrain.abonnement.stripe.model.TaStripeInvoiceItem;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeInvoiceItemServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaStripeInvoiceItemMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaStripeInvoiceItemBean
 */
@SuppressWarnings("deprecation")
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaStripeInvoiceItemService extends AbstractApplicationDAOServer<TaStripeInvoiceItem, TaStripeInvoiceItemDTO> implements ITaStripeInvoiceItemServiceRemote {

	static Logger logger = Logger.getLogger(TaStripeInvoiceItemService.class);

	@Inject private IStripeInvoiceItemDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaStripeInvoiceItemService() {
		super(TaStripeInvoiceItem.class,TaStripeInvoiceItemDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaStripeInvoiceItem a";
	
	public List<TaStripeInvoiceItemDTO> findByCodeLight(String code) {
		return dao.findByCodeLight(code);
	}
	
	public List<TaStripeInvoiceItemDTO> findAllLight() {
		return dao.findAllLight();
	}
	
	public List<TaStripeInvoiceItemDTO> rechercherInvoiceItemDTO(String idExterneInvoice) {
		return dao.rechercherInvoiceItemDTO(idExterneInvoice);
	}
	public List<TaStripeInvoiceItem> rechercherInvoiceItem(String idExterneInvoice) {
		return dao.rechercherInvoiceItem(idExterneInvoice);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaStripeInvoiceItem transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaStripeInvoiceItem transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaStripeInvoiceItem persistentInstance) throws RemoveException {
		dao.remove(dao.findById(persistentInstance.getIdStripeInvoiceItem()));
	}
	
	public TaStripeInvoiceItem merge(TaStripeInvoiceItem detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaStripeInvoiceItem merge(TaStripeInvoiceItem detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaStripeInvoiceItem findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaStripeInvoiceItem findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaStripeInvoiceItem> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaStripeInvoiceItemDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaStripeInvoiceItemDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaStripeInvoiceItem> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaStripeInvoiceItemDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaStripeInvoiceItemDTO entityToDTO(TaStripeInvoiceItem entity) {
//		TaStripeInvoiceItemDTO dto = new TaStripeInvoiceItemDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaStripeInvoiceItemMapper mapper = new TaStripeInvoiceItemMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaStripeInvoiceItemDTO> listEntityToListDTO(List<TaStripeInvoiceItem> entity) {
		List<TaStripeInvoiceItemDTO> l = new ArrayList<TaStripeInvoiceItemDTO>();

		for (TaStripeInvoiceItem taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaStripeInvoiceItemDTO> selectAllDTO() {
		System.out.println("List of TaStripeInvoiceItemDTO EJB :");
		ArrayList<TaStripeInvoiceItemDTO> liste = new ArrayList<TaStripeInvoiceItemDTO>();

		List<TaStripeInvoiceItem> projects = selectAll();
		for(TaStripeInvoiceItem project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaStripeInvoiceItemDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaStripeInvoiceItemDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaStripeInvoiceItemDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaStripeInvoiceItemDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaStripeInvoiceItemDTO dto, String validationContext) throws EJBException {
		try {
			TaStripeInvoiceItemMapper mapper = new TaStripeInvoiceItemMapper();
			TaStripeInvoiceItem entity = null;
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
	
	public void persistDTO(TaStripeInvoiceItemDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaStripeInvoiceItemDTO dto, String validationContext) throws CreateException {
		try {
			TaStripeInvoiceItemMapper mapper = new TaStripeInvoiceItemMapper();
			TaStripeInvoiceItem entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaStripeInvoiceItemDTO dto) throws RemoveException {
		try {
			TaStripeInvoiceItemMapper mapper = new TaStripeInvoiceItemMapper();
			TaStripeInvoiceItem entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaStripeInvoiceItem refresh(TaStripeInvoiceItem persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaStripeInvoiceItem value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaStripeInvoiceItem value, String propertyName, String validationContext) {
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
	public void validateDTO(TaStripeInvoiceItemDTO dto, String validationContext) {
		try {
			TaStripeInvoiceItemMapper mapper = new TaStripeInvoiceItemMapper();
			TaStripeInvoiceItem entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaStripeInvoiceItemDTO> validator = new BeanValidator<TaStripeInvoiceItemDTO>(TaStripeInvoiceItemDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaStripeInvoiceItemDTO dto, String propertyName, String validationContext) {
		try {
			TaStripeInvoiceItemMapper mapper = new TaStripeInvoiceItemMapper();
			TaStripeInvoiceItem entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaStripeInvoiceItemDTO> validator = new BeanValidator<TaStripeInvoiceItemDTO>(TaStripeInvoiceItemDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaStripeInvoiceItemDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaStripeInvoiceItemDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaStripeInvoiceItem value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaStripeInvoiceItem value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
