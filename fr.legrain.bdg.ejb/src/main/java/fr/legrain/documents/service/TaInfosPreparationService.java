package fr.legrain.documents.service;

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

import fr.legrain.bdg.documents.service.remote.ITaInfosPreparationServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.TaPreparationDTO;
import fr.legrain.document.model.TaInfosPreparation;
import fr.legrain.documents.dao.IInfosPreparationDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaInfosBoncdeBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaInfosPreparationService extends AbstractApplicationDAOServer<TaInfosPreparation, TaPreparationDTO> implements ITaInfosPreparationServiceRemote {

	static Logger logger = Logger.getLogger(TaInfosPreparationService.class);

	@Inject private IInfosPreparationDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaInfosPreparationService() {
		super(TaInfosPreparation.class,TaPreparationDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaInfosPreparation a";
	
	public TaInfosPreparation findByCodeBoncde(String code) {
		return dao.findByCodePreparation(code);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaInfosPreparation transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaInfosPreparation transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaInfosPreparation persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaInfosPreparation merge(TaInfosPreparation detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaInfosPreparation merge(TaInfosPreparation detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaInfosPreparation findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaInfosPreparation findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaInfosPreparation> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaPreparationDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaPreparationDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaInfosPreparation> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaPreparationDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaPreparationDTO entityToDTO(TaInfosPreparation entity) {
//		TaPreparationDTO dto = new TaPreparationDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
//		TaInfosPreparationMapper mapper = new TaInfosPreparationMapper();
		return null;
	}

	public List<TaPreparationDTO> listEntityToListDTO(List<TaInfosPreparation> entity) {
		List<TaPreparationDTO> l = new ArrayList<TaPreparationDTO>();

		for (TaInfosPreparation taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaPreparationDTO> selectAllDTO() {
		System.out.println("List of TaPreparationDTO EJB :");
		ArrayList<TaPreparationDTO> liste = new ArrayList<TaPreparationDTO>();

		List<TaInfosPreparation> projects = selectAll();
		for(TaInfosPreparation project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaPreparationDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaPreparationDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaPreparationDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaPreparationDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaPreparationDTO dto, String validationContext) throws EJBException {
//		try {
//			TaInfosPreparationMapper mapper = new TaInfosPreparationMapper();
//			TaInfosPreparation entity = null;
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
	}
	
	public void persistDTO(TaPreparationDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaPreparationDTO dto, String validationContext) throws CreateException {
//		try {
//			TaInfosPreparationMapper mapper = new TaInfosPreparationMapper();
//			TaInfosPreparation entity = mapper.mapDtoToEntity(dto,null);
//			//dao.persist(entity);
//			enregistrerPersist(entity, validationContext);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new CreateException(e.getMessage());
//		}
	}

	@Override
	public void removeDTO(TaPreparationDTO dto) throws RemoveException {
//		try {
//			TaInfosPreparationMapper mapper = new TaInfosPreparationMapper();
//			TaInfosPreparation entity = mapper.mapDtoToEntity(dto,null);
//			//dao.remove(entity);
//			supprimer(entity);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RemoveException(e.getMessage());
//		}
	}

	@Override
	protected TaInfosPreparation refresh(TaInfosPreparation persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaInfosPreparation value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaInfosPreparation value, String propertyName, String validationContext) {
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
	public void validateDTO(TaPreparationDTO dto, String validationContext) {
//		try {
//			TaInfosPreparationMapper mapper = new TaInfosPreparationMapper();
//			TaInfosPreparation entity = mapper.mapDtoToEntity(dto,null);
//			validateEntity(entity,validationContext);
//			
//			//validation automatique via la JSR bean validation
////			BeanValidator<TaPreparationDTO> validator = new BeanValidator<TaPreparationDTO>(TaPreparationDTO.class);
////			validator.validate(dto);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new EJBException(e.getMessage());
//		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaPreparationDTO dto, String propertyName, String validationContext) {
//		try {
//			TaInfosPreparationMapper mapper = new TaInfosPreparationMapper();
//			TaInfosPreparation entity = mapper.mapDtoToEntity(dto,null);
//			validateEntityProperty(entity,propertyName,validationContext);
//			
//			//validation automatique via la JSR bean validation
////			BeanValidator<TaPreparationDTO> validator = new BeanValidator<TaPreparationDTO>(TaPreparationDTO.class);
////			validator.validateField(dto,propertyName);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new EJBException(e.getMessage());
//		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaPreparationDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaPreparationDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaInfosPreparation value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaInfosPreparation value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

	@Override
	public TaInfosPreparation findByCodePreparation(String code) {
		// TODO Auto-generated method stub
		return dao.findByCodePreparation(code);
	}

}
