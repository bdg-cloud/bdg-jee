//package fr.legrain.article.service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.annotation.security.DeclareRoles;
//import javax.annotation.security.RolesAllowed;
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
//import fr.legrain.article.dao.IMatierePremiereDAO;
//import fr.legrain.article.dto.TaMatierePremiereDTO;
//import fr.legrain.article.model.TaMatierePremiere;
//import fr.legrain.bdg.article.service.remote.ITaMatierePremiereServiceRemote;
//import fr.legrain.bdg.model.mapping.mapper.TaMatierePremiereMapper;
//import fr.legrain.data.AbstractApplicationDAOServer;
//
//
///**
// * Session Bean implementation class TaMatierePremiereBean
// */
//@SuppressWarnings("deprecation")
//@Stateless
//@DeclareRoles("admin")
//public class TaMatierePremiereService extends AbstractApplicationDAOServer<TaMatierePremiere, TaMatierePremiereDTO> implements ITaMatierePremiereServiceRemote {
//
//	static Logger logger = Logger.getLogger(TaMatierePremiereService.class);
//
//	@Inject private IMatierePremiereDAO dao;
//
//	/**
//	 * Default constructor. 
//	 */
//	public TaMatierePremiereService() {
//		super(TaMatierePremiere.class,TaMatierePremiereDTO.class);
//	}
//	
//	//	private String defaultJPQLQuery = "select a from TaMatierePremiere a";
//
//	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	// 										ENTITY
//	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	
//	public void persist(TaMatierePremiere transientInstance) throws CreateException {
//		persist(transientInstance, null);
//	}
//
//	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
//	@WebMethod(operationName = "persistValidationContext")
//	public void persist(TaMatierePremiere transientInstance, String validationContext) throws CreateException {
//
//		validateEntity(transientInstance, validationContext);
//
//		dao.persist(transientInstance);
//	}
//
//	public void remove(TaMatierePremiere persistentInstance) throws RemoveException {
//		dao.remove(persistentInstance);
//	}
//	
//	public TaMatierePremiere merge(TaMatierePremiere detachedInstance) {
//		return merge(detachedInstance, null);
//	}
//
//	@Override
//	@WebMethod(operationName = "mergeValidationContext")
//	public TaMatierePremiere merge(TaMatierePremiere detachedInstance, String validationContext) {
//		validateEntity(detachedInstance, validationContext);
//
//		return dao.merge(detachedInstance);
//	}
//
//	public TaMatierePremiere findById(int id) throws FinderException {
//		return dao.findById(id);
//	}
//
//	public TaMatierePremiere findByCode(String code) throws FinderException {
//		return dao.findByCode(code);
//	}
//	public List<TaMatierePremiere> findByIdFab(Integer id)  {
//		return dao.findByIdFab(id);
//	}
//	@RolesAllowed("admin")
//	public List<TaMatierePremiere> selectAll() {
//		return dao.selectAll();
//	}
//
//	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	// 										DTO
//	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//	public List<TaMatierePremiereDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
//		return null;
//	}
//
//	@Override
//	public List<TaMatierePremiereDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
//		List<TaMatierePremiere> entityList = dao.findWithJPQLQuery(JPQLquery);
//		List<TaMatierePremiereDTO> l = null;
//		if(entityList!=null) {
//			l = listEntityToListDTO(entityList);
//		}
//		return l;
//	}
//	
//	public List<TaMatierePremiereDTO> findDTOByIdFab(Integer id)  {
//		List<TaMatierePremiereDTO> list = new ArrayList<TaMatierePremiereDTO>();
//		for (TaMatierePremiere matierePremiere : dao.findByIdFab(id)){
//			list.add(entityToDTO(matierePremiere));
//		}
//			
//		return	list;
//	}
//	public TaMatierePremiereDTO entityToDTO(TaMatierePremiere entity) {
////		TaMatierePremiereDTO dto = new TaMatierePremiereDTO();
////		dto.setId(entity.getIdTCivilite());
////		dto.setCodeTCivilite(entity.getCodeTCivilite());
////		return dto;
//		TaMatierePremiereMapper mapper = new TaMatierePremiereMapper();
//		return mapper.mapEntityToDto(entity, null);
//	}
//
//	public List<TaMatierePremiereDTO> listEntityToListDTO(List<TaMatierePremiere> entity) {
//		List<TaMatierePremiereDTO> l = new ArrayList<TaMatierePremiereDTO>();
//
//		for (TaMatierePremiere taTCivilite : entity) {
//			l.add(entityToDTO(taTCivilite));
//		}
//
//		return l;
//	}
//
//	@RolesAllowed("admin")
//	public List<TaMatierePremiereDTO> selectAllDTO() {
//		System.out.println("List of TaMatierePremiereDTO EJB :");
//		ArrayList<TaMatierePremiereDTO> liste = new ArrayList<TaMatierePremiereDTO>();
//
//		List<TaMatierePremiere> projects = selectAll();
//		for(TaMatierePremiere project : projects) {
//			liste.add(entityToDTO(project));
//		}
//
//		return liste;
//	}
//
//	public TaMatierePremiereDTO findByIdDTO(int id) throws FinderException {
//		return entityToDTO(findById(id));
//	}
//
//	public TaMatierePremiereDTO findByCodeDTO(String code) throws FinderException {
//		return entityToDTO(findByCode(code));
//	}
//
//	@Override
//	public void error(TaMatierePremiereDTO dto) {
//		throw new EJBException("Test erreur EJB");
//	}
//
//	@Override
//	public int selectCount() {
//		return dao.selectAll().size();
//		//return 0;
//	}
//	
//	public void mergeDTO(TaMatierePremiereDTO dto) throws EJBException {
//		mergeDTO(dto, null);
//	}
//
//	@Override
//	@WebMethod(operationName = "mergeDTOValidationContext")
//	public void mergeDTO(TaMatierePremiereDTO dto, String validationContext) throws EJBException {
//		try {
//			TaMatierePremiereMapper mapper = new TaMatierePremiereMapper();
//			TaMatierePremiere entity = null;
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
//	public void persistDTO(TaMatierePremiereDTO dto) throws CreateException {
//		persistDTO(dto, null);
//	}
//
//	@Override
//	@WebMethod(operationName = "persistDTOValidationContext")
//	public void persistDTO(TaMatierePremiereDTO dto, String validationContext) throws CreateException {
//		try {
//			TaMatierePremiereMapper mapper = new TaMatierePremiereMapper();
//			TaMatierePremiere entity = mapper.mapDtoToEntity(dto,null);
//			//dao.persist(entity);
//			enregistrerPersist(entity, validationContext);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new CreateException(e.getMessage());
//		}
//	}
//
//	@Override
//	public void removeDTO(TaMatierePremiereDTO dto) throws RemoveException {
//		try {
//			TaMatierePremiereMapper mapper = new TaMatierePremiereMapper();
//			TaMatierePremiere entity = mapper.mapDtoToEntity(dto,null);
//			//dao.remove(entity);
//			supprimer(entity);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RemoveException(e.getMessage());
//		}
//	}
//
//	@Override
//	protected TaMatierePremiere refresh(TaMatierePremiere persistentInstance) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	@WebMethod(operationName = "validateEntityValidationContext")
//	public void validateEntity(TaMatierePremiere value, String validationContext) /*throws ExceptLgr*/ {
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
//	public void validateEntityProperty(TaMatierePremiere value, String propertyName, String validationContext) {
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
//	public void validateDTO(TaMatierePremiereDTO dto, String validationContext) {
//		try {
//			TaMatierePremiereMapper mapper = new TaMatierePremiereMapper();
//			TaMatierePremiere entity = mapper.mapDtoToEntity(dto,null);
//			validateEntity(entity,validationContext);
//			
//			//validation automatique via la JSR bean validation
////			BeanValidator<TaMatierePremiereDTO> validator = new BeanValidator<TaMatierePremiereDTO>(TaMatierePremiereDTO.class);
////			validator.validate(dto);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new EJBException(e.getMessage());
//		}
//	}
//
//	@Override
//	@WebMethod(operationName = "validateDTOPropertyValidationContext")
//	public void validateDTOProperty(TaMatierePremiereDTO dto, String propertyName, String validationContext) {
//		try {
//			TaMatierePremiereMapper mapper = new TaMatierePremiereMapper();
//			TaMatierePremiere entity = mapper.mapDtoToEntity(dto,null);
//			validateEntityProperty(entity,propertyName,validationContext);
//			
//			//validation automatique via la JSR bean validation
////			BeanValidator<TaMatierePremiereDTO> validator = new BeanValidator<TaMatierePremiereDTO>(TaMatierePremiereDTO.class);
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
//	public void validateDTO(TaMatierePremiereDTO dto) {
//		validateDTO(dto,null);
//		
//	}
//
//	@Override
//	@WebMethod(operationName = "validateDTOProperty")
//	public void validateDTOProperty(TaMatierePremiereDTO dto, String propertyName) {
//		validateDTOProperty(dto,propertyName,null);
//		
//	}
//
//	@Override
//	@WebMethod(operationName = "validateEntity")
//	public void validateEntity(TaMatierePremiere value) {
//		validateEntity(value,null);
//	}
//
//	@Override
//	@WebMethod(operationName = "validateEntityProperty")
//	public void validateEntityProperty(TaMatierePremiere value, String propertyName) {
//		validateEntityProperty(value,propertyName,null);
//		
//	}
//
//}
