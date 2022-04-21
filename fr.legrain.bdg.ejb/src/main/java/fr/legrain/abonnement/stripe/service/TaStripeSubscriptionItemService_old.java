//package fr.legrain.abonnement.stripe.service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.annotation.security.DeclareRoles;
//import javax.ejb.CreateException;
//import javax.ejb.EJBException;
//import javax.ejb.FinderException;
//import javax.ejb.RemoveException;
//import javax.ejb.Stateless;
//import javax.inject.Inject;
//import javax.interceptor.Interceptors;
//import javax.jws.WebMethod;
//
//import org.apache.log4j.Logger;
//import org.hibernate.OptimisticLockException;
//
//import fr.legrain.abonnement.stripe.dao.IStripeProductDAO;
//import fr.legrain.abonnement.stripe.dao.IStripeSubscriptionDAO;
//import fr.legrain.abonnement.stripe.dao.IStripeSubscriptionItemDAO;
//import fr.legrain.abonnement.stripe.dto.TaStripeSubscriptionItemDTO;
//import fr.legrain.abonnement.stripe.model.TaStripeSubscriptionItem_old;
//import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeSubscriptionItemServiceRemote;
//import fr.legrain.bdg.general.service.remote.BusinessValidationException;
//import fr.legrain.bdg.model.mapping.mapper.TaStripeSubscriptionItemMapper;
//import fr.legrain.data.AbstractApplicationDAOServer;
//import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
//
//
///**
// * Session Bean implementation class TaStripeSubscriptionItemBean
// */
//@SuppressWarnings("deprecation")
//@Stateless
//@DeclareRoles("admin")
//@Interceptors(ServerTenantInterceptor.class)
//public class TaStripeSubscriptionItemService_old extends AbstractApplicationDAOServer<TaStripeSubscriptionItem_old, TaStripeSubscriptionItemDTO> implements ITaStripeSubscriptionItemServiceRemote {
//
//	@Override
//	public void persistDTO(TaStripeSubscriptionItemDTO transientInstance) throws CreateException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void removeDTO(TaStripeSubscriptionItemDTO persistentInstance) throws RemoveException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void mergeDTO(TaStripeSubscriptionItemDTO detachedInstance) throws EJBException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void persistDTO(TaStripeSubscriptionItemDTO transientInstance, String validationContext)
//			throws CreateException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void mergeDTO(TaStripeSubscriptionItemDTO detachedInstance, String validationContext) throws EJBException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public List<TaStripeSubscriptionItemDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<TaStripeSubscriptionItemDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<TaStripeSubscriptionItemDTO> selectAllDTO() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public TaStripeSubscriptionItemDTO findByIdDTO(int id) throws FinderException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public TaStripeSubscriptionItemDTO findByCodeDTO(String code) throws FinderException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void validateDTO(TaStripeSubscriptionItemDTO dto) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void validateDTOProperty(TaStripeSubscriptionItemDTO dto, String propertyName)
//			throws BusinessValidationException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void validateDTO(TaStripeSubscriptionItemDTO dto, String validationContext) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void validateDTOProperty(TaStripeSubscriptionItemDTO dto, String propertyName, String validationContext)
//			throws BusinessValidationException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void error(TaStripeSubscriptionItemDTO dto) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void persist(TaStripeSubscriptionItem_old transientInstance) throws CreateException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void remove(TaStripeSubscriptionItem_old persistentInstance) throws RemoveException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public TaStripeSubscriptionItem_old merge(TaStripeSubscriptionItem_old detachedInstance) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void persist(TaStripeSubscriptionItem_old transientInstance, String validationContext)
//			throws CreateException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public TaStripeSubscriptionItem_old merge(TaStripeSubscriptionItem_old detachedInstance, String validationContext) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public TaStripeSubscriptionItem_old findById(int id) throws FinderException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public TaStripeSubscriptionItem_old findByCode(String code) throws FinderException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void validateEntity(TaStripeSubscriptionItem_old value) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void validateEntityProperty(TaStripeSubscriptionItem_old value, String propertyName) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void validateEntity(TaStripeSubscriptionItem_old value, String validationContext) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void validateEntityProperty(TaStripeSubscriptionItem_old value, String propertyName,
//			String validationContext) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public List<TaStripeSubscriptionItem_old> selectAll() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	protected TaStripeSubscriptionItem_old refresh(TaStripeSubscriptionItem_old persistentInstance) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
////	static Logger logger = Logger.getLogger(TaStripeSubscriptionItemService.class);
////
////	@Inject private IStripeSubscriptionItemDAO dao;
////
////	/**
////	 * Default constructor. 
////	 */
////	public TaStripeSubscriptionItemService() {
////		super(TaStripeSubscriptionItem_old.class,TaStripeSubscriptionItemDTO.class);
////	}
////	
////	//	private String defaultJPQLQuery = "select a from TaStripeSubscriptionItem_old a";
////	
////	public List<TaStripeSubscriptionItemDTO> findByCodeLight(String code) {
////		return dao.findByCodeLight(code);
////	}
////	
////	public List<TaStripeSubscriptionItemDTO> findAllLight() {
////		return dao.findAllLight();
////	}
////	
////	public List<TaStripeSubscriptionItemDTO> rechercherSubscriptionItemDTO(String idExterneSubscription) {
////		return dao.rechercherSubscriptionItemDTO(idExterneSubscription);
////	}
////	
////	public List<TaStripeSubscriptionItem_old> rechercherSubscriptionItem(String idExterneSubscription) {
////		return dao.rechercherSubscriptionItem(idExterneSubscription);
////	}
////	
////	public List<TaStripeSubscriptionItemDTO> rechercherSubscriptionItemDTO(int idSubscription) {
////		return dao.rechercherSubscriptionItemDTO(idSubscription);
////	}
////
////	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////	// 										ENTITY
////	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////	
////	public void persist(TaStripeSubscriptionItem_old transientInstance) throws CreateException {
////		persist(transientInstance, null);
////	}
////
////	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
////	@WebMethod(operationName = "persistValidationContext")
////	public void persist(TaStripeSubscriptionItem_old transientInstance, String validationContext) throws CreateException {
////
////		validateEntity(transientInstance, validationContext);
////
////		dao.persist(transientInstance);
////	}
////
////	public void remove(TaStripeSubscriptionItem_old persistentInstance) throws RemoveException {
////		dao.remove(dao.findById(persistentInstance.getIdStripeSubscriptionItem()));
////	}
////	
////	public TaStripeSubscriptionItem_old merge(TaStripeSubscriptionItem_old detachedInstance) {
////		return merge(detachedInstance, null);
////	}
////
////	@Override
////	@WebMethod(operationName = "mergeValidationContext")
////	public TaStripeSubscriptionItem_old merge(TaStripeSubscriptionItem_old detachedInstance, String validationContext) {
////		validateEntity(detachedInstance, validationContext);
////
////		return dao.merge(detachedInstance);
////	}
////
////	public TaStripeSubscriptionItem_old findById(int id) throws FinderException {
////		return dao.findById(id);
////	}
////
////	public TaStripeSubscriptionItem_old findByCode(String code) throws FinderException {
////		return dao.findByCode(code);
////	}
////
//////	@RolesAllowed("admin")
////	public List<TaStripeSubscriptionItem_old> selectAll() {
////		return dao.selectAll();
////	}
////
////	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////	// 										DTO
////	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////
////	public List<TaStripeSubscriptionItemDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
////		return null;
////	}
////
////	@Override
////	public List<TaStripeSubscriptionItemDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
////		List<TaStripeSubscriptionItem_old> entityList = dao.findWithJPQLQuery(JPQLquery);
////		List<TaStripeSubscriptionItemDTO> l = null;
////		if(entityList!=null) {
////			l = listEntityToListDTO(entityList);
////		}
////		return l;
////	}
////
////	public TaStripeSubscriptionItemDTO entityToDTO(TaStripeSubscriptionItem_old entity) {
//////		TaStripeSubscriptionItemDTO dto = new TaStripeSubscriptionItemDTO();
//////		dto.setId(entity.getIdTCivilite());
//////		dto.setCodeTCivilite(entity.getCodeTCivilite());
//////		return dto;
////		TaStripeSubscriptionItemMapper mapper = new TaStripeSubscriptionItemMapper();
////		return mapper.mapEntityToDto(entity, null);
////	}
////
////	public List<TaStripeSubscriptionItemDTO> listEntityToListDTO(List<TaStripeSubscriptionItem_old> entity) {
////		List<TaStripeSubscriptionItemDTO> l = new ArrayList<TaStripeSubscriptionItemDTO>();
////
////		for (TaStripeSubscriptionItem_old taTCivilite : entity) {
////			l.add(entityToDTO(taTCivilite));
////		}
////
////		return l;
////	}
////
//////	@RolesAllowed("admin")
////	public List<TaStripeSubscriptionItemDTO> selectAllDTO() {
////		System.out.println("List of TaStripeSubscriptionItemDTO EJB :");
////		ArrayList<TaStripeSubscriptionItemDTO> liste = new ArrayList<TaStripeSubscriptionItemDTO>();
////
////		List<TaStripeSubscriptionItem_old> projects = selectAll();
////		for(TaStripeSubscriptionItem_old project : projects) {
////			liste.add(entityToDTO(project));
////		}
////
////		return liste;
////	}
////
////	public TaStripeSubscriptionItemDTO findByIdDTO(int id) throws FinderException {
////		return entityToDTO(findById(id));
////	}
////
////	public TaStripeSubscriptionItemDTO findByCodeDTO(String code) throws FinderException {
////		return entityToDTO(findByCode(code));
////	}
////
////	@Override
////	public void error(TaStripeSubscriptionItemDTO dto) {
////		throw new EJBException("Test erreur EJB");
////	}
////
////	@Override
////	public int selectCount() {
////		return dao.selectAll().size();
////		//return 0;
////	}
////	
////	public void mergeDTO(TaStripeSubscriptionItemDTO dto) throws EJBException {
////		mergeDTO(dto, null);
////	}
////
////	@Override
////	@WebMethod(operationName = "mergeDTOValidationContext")
////	public void mergeDTO(TaStripeSubscriptionItemDTO dto, String validationContext) throws EJBException {
////		try {
////			TaStripeSubscriptionItemMapper mapper = new TaStripeSubscriptionItemMapper();
////			TaStripeSubscriptionItem_old entity = null;
////			if(dto.getId()!=null) {
////				entity = dao.findById(dto.getId());
////				if(dto.getVersionObj()!=entity.getVersionObj()) {
////					throw new OptimisticLockException(entity,
////							"L'objet à été modifié depuis le dernier accés. Client ID : "+dto.getId()+" - Client Version objet : "+dto.getVersionObj()+" -Serveur Version Objet : "+entity.getVersionObj());
////				} else {
////					 entity = mapper.mapDtoToEntity(dto,entity);
////				}
////			}
////			
////			//dao.merge(entity);
////			dao.detach(entity); //pour passer les controles
////			enregistrerMerge(entity, validationContext);
////		} catch (Exception e) {
////			e.printStackTrace();
////			//throw new CreateException(e.getMessage());
////			throw new EJBException(e.getMessage());
////		}
////	}
////	
////	public void persistDTO(TaStripeSubscriptionItemDTO dto) throws CreateException {
////		persistDTO(dto, null);
////	}
////
////	@Override
////	@WebMethod(operationName = "persistDTOValidationContext")
////	public void persistDTO(TaStripeSubscriptionItemDTO dto, String validationContext) throws CreateException {
////		try {
////			TaStripeSubscriptionItemMapper mapper = new TaStripeSubscriptionItemMapper();
////			TaStripeSubscriptionItem_old entity = mapper.mapDtoToEntity(dto,null);
////			//dao.persist(entity);
////			enregistrerPersist(entity, validationContext);
////		} catch (Exception e) {
////			e.printStackTrace();
////			throw new CreateException(e.getMessage());
////		}
////	}
////
////	@Override
////	public void removeDTO(TaStripeSubscriptionItemDTO dto) throws RemoveException {
////		try {
////			TaStripeSubscriptionItemMapper mapper = new TaStripeSubscriptionItemMapper();
////			TaStripeSubscriptionItem_old entity = mapper.mapDtoToEntity(dto,null);
////			//dao.remove(entity);
////			supprimer(entity);
////		} catch (Exception e) {
////			e.printStackTrace();
////			throw new RemoveException(e.getMessage());
////		}
////	}
////
////	@Override
////	protected TaStripeSubscriptionItem_old refresh(TaStripeSubscriptionItem_old persistentInstance) {
////		// TODO Auto-generated method stub
////		return null;
////	}
////
////	@Override
////	@WebMethod(operationName = "validateEntityValidationContext")
////	public void validateEntity(TaStripeSubscriptionItem_old value, String validationContext) /*throws ExceptLgr*/ {
////		try {
////			if(validationContext==null) validationContext="";
////			validateAll(value,validationContext,false); //ancienne validation, extraction de l'annotation et appel
////			//dao.validate(value); //validation automatique via la JSR bean validation
////		} catch (Exception e) {
////			e.printStackTrace();
////			throw new EJBException(e.getMessage());
////		}
////	}
////
////	@Override
////	@WebMethod(operationName = "validateEntityPropertyValidationContext")
////	public void validateEntityProperty(TaStripeSubscriptionItem_old value, String propertyName, String validationContext) {
////		try {
////			if(validationContext==null) validationContext="";
////			validate(value, propertyName, validationContext);
////			//dao.validateField(value,propertyName);
////		} catch (Exception e) {
////			e.printStackTrace();
////			throw new EJBException(e.getMessage());
////		}
////	}
////
////	@Override
////	@WebMethod(operationName = "validateDTOValidationContext")
////	public void validateDTO(TaStripeSubscriptionItemDTO dto, String validationContext) {
////		try {
////			TaStripeSubscriptionItemMapper mapper = new TaStripeSubscriptionItemMapper();
////			TaStripeSubscriptionItem_old entity = mapper.mapDtoToEntity(dto,null);
////			validateEntity(entity,validationContext);
////			
////			//validation automatique via la JSR bean validation
//////			BeanValidator<TaStripeSubscriptionItemDTO> validator = new BeanValidator<TaStripeSubscriptionItemDTO>(TaStripeSubscriptionItemDTO.class);
//////			validator.validate(dto);
////		} catch (Exception e) {
////			e.printStackTrace();
////			throw new EJBException(e.getMessage());
////		}
////	}
////
////	@Override
////	@WebMethod(operationName = "validateDTOPropertyValidationContext")
////	public void validateDTOProperty(TaStripeSubscriptionItemDTO dto, String propertyName, String validationContext) {
////		try {
////			TaStripeSubscriptionItemMapper mapper = new TaStripeSubscriptionItemMapper();
////			TaStripeSubscriptionItem_old entity = mapper.mapDtoToEntity(dto,null);
////			validateEntityProperty(entity,propertyName,validationContext);
////			
////			//validation automatique via la JSR bean validation
//////			BeanValidator<TaStripeSubscriptionItemDTO> validator = new BeanValidator<TaStripeSubscriptionItemDTO>(TaStripeSubscriptionItemDTO.class);
//////			validator.validateField(dto,propertyName);
////		} catch (Exception e) {
////			e.printStackTrace();
////			throw new EJBException(e.getMessage());
////		}
////
////	}
////	
////	@Override
////	@WebMethod(operationName = "validateDTO")
////	public void validateDTO(TaStripeSubscriptionItemDTO dto) {
////		validateDTO(dto,null);
////		
////	}
////
////	@Override
////	@WebMethod(operationName = "validateDTOProperty")
////	public void validateDTOProperty(TaStripeSubscriptionItemDTO dto, String propertyName) {
////		validateDTOProperty(dto,propertyName,null);
////		
////	}
////
////	@Override
////	@WebMethod(operationName = "validateEntity")
////	public void validateEntity(TaStripeSubscriptionItem_old value) {
////		validateEntity(value,null);
////	}
////
////	@Override
////	@WebMethod(operationName = "validateEntityProperty")
////	public void validateEntityProperty(TaStripeSubscriptionItem_old value, String propertyName) {
////		validateEntityProperty(value,propertyName,null);
////		
////	}
//
//}
