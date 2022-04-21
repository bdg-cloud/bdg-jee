//package fr.legrain.moncompte.service;
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
//import javax.jws.WebMethod;
//
//import org.apache.log4j.Logger;
//import org.hibernate.OptimisticLockException;
//
//import fr.legrain.bdg.moncompte.service.remote.IAdministrationServiceRemote;
//import fr.legrain.bdg.moncompte.service.remote.ITaAdresseServiceRemote;
//import fr.legrain.data.AbstractApplicationDAOServer;
//import fr.legrain.moncompte.dao.IAdresseDAO;
//import fr.legrain.moncompte.dto.TaAdresseDTO;
//import fr.legrain.moncompte.model.TaAdresse;
//import fr.legrain.moncompte.model.mapping.mapper.TaAdresseMapper;
//
//
///**
// * Session Bean implementation class TaAdresseBean
// */
//@Stateless
////@DeclareRoles("admin")
//public class AdministrationService extends AbstractApplicationDAOServer<TaAdresse, TaAdresseDTO> implements IAdministrationServiceRemote {
//
//	static Logger logger = Logger.getLogger(AdministrationService.class);
//
//	@Inject private IAdresseDAO dao;
//
//	private Admin admin;
//
//	/**
//	 * Default constructor. 
//	 */
//	public AdministrationService() {
//		super(TaAdresse.class,TaAdresseDTO.class);
//	}
//	
//	//	private String defaultJPQLQuery = "select a from TaAdresse a";
//
//	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	// 										ENTITY
//	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	
//	public void persist(TaAdresse transientInstance) throws CreateException {
//		persist(transientInstance, null);
//	}
//
//	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
//	@WebMethod(operationName = "persistValidationContext")
//	public void persist(TaAdresse transientInstance, String validationContext) throws CreateException {
//
//		validateEntity(transientInstance, validationContext);
//
//		dao.persist(transientInstance);
//	}
//
//	public void remove(TaAdresse persistentInstance) throws RemoveException {
//		try {
//			dao.remove(findById(persistentInstance.getIdAdresse()));
//		} catch (FinderException e) {
//			logger.error("", e);
//		}
//	}
//	
//	public TaAdresse merge(TaAdresse detachedInstance) {
//		return merge(detachedInstance, null);
//	}
//
//	@Override
//	@WebMethod(operationName = "mergeValidationContext")
//	public TaAdresse merge(TaAdresse detachedInstance, String validationContext) {
//		validateEntity(detachedInstance, validationContext);
//
//		return dao.merge(detachedInstance);
//	}
//
//	public TaAdresse findById(int id) throws FinderException {
//		return dao.findById(id);
//	}
//
//	public TaAdresse findByCode(String code) throws FinderException {
//		return dao.findByCode(code);
//	}
//
////	@RolesAllowed("admin")
//	public List<TaAdresse> selectAll() {
//		return dao.selectAll();
//	}
//
//	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	// 										DTO
//	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//	public List<TaAdresseDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
//		return null;
//	}
//
//	@Override
//	public List<TaAdresseDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
//		List<TaAdresse> entityList = dao.findWithJPQLQuery(JPQLquery);
//		List<TaAdresseDTO> l = null;
//		if(entityList!=null) {
//			l = listEntityToListDTO(entityList);
//		}
//		return l;
//	}
//
//	public TaAdresseDTO entityToDTO(TaAdresse entity) {
////		TaAdresseDTO dto = new TaAdresseDTO();
////		dto.setId(entity.getIdTCivilite());
////		dto.setCodeTCivilite(entity.getCodeTCivilite());
////		return dto;
//		TaAdresseMapper mapper = new TaAdresseMapper();
//		return mapper.mapEntityToDto(entity, null);
//	}
//
//	public List<TaAdresseDTO> listEntityToListDTO(List<TaAdresse> entity) {
//		List<TaAdresseDTO> l = new ArrayList<TaAdresseDTO>();
//
//		for (TaAdresse taTCivilite : entity) {
//			l.add(entityToDTO(taTCivilite));
//		}
//
//		return l;
//	}
//
////	@RolesAllowed("admin")
//	public List<TaAdresseDTO> selectAllDTO() {
//		System.out.println("List of TaAdresseDTO EJB :");
//		ArrayList<TaAdresseDTO> liste = new ArrayList<TaAdresseDTO>();
//
//		List<TaAdresse> projects = selectAll();
//		for(TaAdresse project : projects) {
//			liste.add(entityToDTO(project));
//		}
//
//		return liste;
//	}
//
//	public TaAdresseDTO findByIdDTO(int id) throws FinderException {
//		return entityToDTO(findById(id));
//	}
//
//	public TaAdresseDTO findByCodeDTO(String code) throws FinderException {
//		return entityToDTO(findByCode(code));
//	}
//
//	@Override
//	public void error(TaAdresseDTO dto) {
//		throw new EJBException("Test erreur EJB");
//	}
//
//	@Override
//	public int selectCount() {
//		return dao.selectAll().size();
//		//return 0;
//	}
//	
//	public void mergeDTO(TaAdresseDTO dto) throws EJBException {
//		mergeDTO(dto, null);
//	}
//
//	@Override
//	@WebMethod(operationName = "mergeDTOValidationContext")
//	public void mergeDTO(TaAdresseDTO dto, String validationContext) throws EJBException {
//		try {
//			TaAdresseMapper mapper = new TaAdresseMapper();
//			TaAdresse entity = null;
//			if(dto.getId()!=null) {
//				entity = dao.findById(dto.getId());
//				if(dto.getVersionObj()!=entity.getVersionObj()) {
//					throw new OptimisticLockException(entity,
//							"L'objet à été modifié depuis le dernier accés. Client ID : "+dto.getId()+" - Client Version objet : "+dto.getVersionObj()+" -Serveur Version Objet : "+entity.getVersionObj());
//				} else {
//					 entity = mapper.mapDtoToEntity(dto,entity);
//				}
//			}
//			
//			//dao.merge(entity);
//			dao.detach(entity); //pour passer les controles
//			enregistrerMerge(entity, validationContext);
//		} catch (Exception e) {
//			e.printStackTrace();
//			//throw new CreateException(e.getMessage());
//			throw new EJBException(e.getMessage());
//		}
//	}
//	
//	public void persistDTO(TaAdresseDTO dto) throws CreateException {
//		persistDTO(dto, null);
//	}
//
//	@Override
//	@WebMethod(operationName = "persistDTOValidationContext")
//	public void persistDTO(TaAdresseDTO dto, String validationContext) throws CreateException {
//		try {
//			TaAdresseMapper mapper = new TaAdresseMapper();
//			TaAdresse entity = mapper.mapDtoToEntity(dto,null);
//			//dao.persist(entity);
//			enregistrerPersist(entity, validationContext);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new CreateException(e.getMessage());
//		}
//	}
//
//	@Override
//	public void removeDTO(TaAdresseDTO dto) throws RemoveException {
//		try {
//			TaAdresseMapper mapper = new TaAdresseMapper();
//			TaAdresse entity = mapper.mapDtoToEntity(dto,null);
//			//dao.remove(entity);
//			supprimer(entity);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RemoveException(e.getMessage());
//		}
//	}
//
//	@Override
//	protected TaAdresse refresh(TaAdresse persistentInstance) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	@WebMethod(operationName = "validateEntityValidationContext")
//	public void validateEntity(TaAdresse value, String validationContext) /*throws ExceptLgr*/ {
//		try {
//			if(validationContext==null) validationContext="";
//			validateAll(value,validationContext,false); //ancienne validation, extraction de l'annotation et appel
//			//dao.validate(value); //validation automatique via la JSR bean validation
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new EJBException(e.getMessage());
//		}
//	}
//
//	@Override
//	@WebMethod(operationName = "validateEntityPropertyValidationContext")
//	public void validateEntityProperty(TaAdresse value, String propertyName, String validationContext) {
//		try {
//			if(validationContext==null) validationContext="";
//			validate(value, propertyName, validationContext);
//			//dao.validateField(value,propertyName);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new EJBException(e.getMessage());
//		}
//	}
//
//	@Override
//	@WebMethod(operationName = "validateDTOValidationContext")
//	public void validateDTO(TaAdresseDTO dto, String validationContext) {
//		try {
//			TaAdresseMapper mapper = new TaAdresseMapper();
//			TaAdresse entity = mapper.mapDtoToEntity(dto,null);
//			validateEntity(entity,validationContext);
//			
//			//validation automatique via la JSR bean validation
////			BeanValidator<TaAdresseDTO> validator = new BeanValidator<TaAdresseDTO>(TaAdresseDTO.class);
////			validator.validate(dto);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new EJBException(e.getMessage());
//		}
//	}
//
//	@Override
//	@WebMethod(operationName = "validateDTOPropertyValidationContext")
//	public void validateDTOProperty(TaAdresseDTO dto, String propertyName, String validationContext) {
//		try {
//			TaAdresseMapper mapper = new TaAdresseMapper();
//			TaAdresse entity = mapper.mapDtoToEntity(dto,null);
//			validateEntityProperty(entity,propertyName,validationContext);
//			
//			//validation automatique via la JSR bean validation
////			BeanValidator<TaAdresseDTO> validator = new BeanValidator<TaAdresseDTO>(TaAdresseDTO.class);
////			validator.validateField(dto,propertyName);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new EJBException(e.getMessage());
//		}
//
//	}
//	
//	@Override
//	@WebMethod(operationName = "validateDTO")
//	public void validateDTO(TaAdresseDTO dto) {
//		validateDTO(dto,null);
//		
//	}
//
//	@Override
//	@WebMethod(operationName = "validateDTOProperty")
//	public void validateDTOProperty(TaAdresseDTO dto, String propertyName) {
//		validateDTOProperty(dto,propertyName,null);
//		
//	}
//
//	@Override
//	@WebMethod(operationName = "validateEntity")
//	public void validateEntity(TaAdresse value) {
//		validateEntity(value,null);
//	}
//
//	@Override
//	@WebMethod(operationName = "validateEntityProperty")
//	public void validateEntityProperty(TaAdresse value, String propertyName) {
//		validateEntityProperty(value,propertyName,null);
//	}
//
//
//
//
//
//}
