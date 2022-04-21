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

import fr.legrain.bdg.documents.service.remote.ITaInfosAvisEcheanceServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaInfosAvisEcheanceMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.TaAvisEcheanceDTO;
import fr.legrain.document.model.TaInfosAvisEcheance;
import fr.legrain.documents.dao.IInfosAvisEcheanceDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaInfosAvisEcheanceBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaInfosAvisEcheanceService extends AbstractApplicationDAOServer<TaInfosAvisEcheance, TaAvisEcheanceDTO> implements ITaInfosAvisEcheanceServiceRemote {

	static Logger logger = Logger.getLogger(TaInfosAvisEcheanceService.class);

	@Inject private IInfosAvisEcheanceDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaInfosAvisEcheanceService() {
		super(TaInfosAvisEcheance.class,TaAvisEcheanceDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaInfosAvisEcheance a";
	
	public TaInfosAvisEcheance findByCodeAvisEcheance(String code) {
		return dao.findByCodeAvisEcheance(code);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaInfosAvisEcheance transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaInfosAvisEcheance transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaInfosAvisEcheance persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaInfosAvisEcheance merge(TaInfosAvisEcheance detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaInfosAvisEcheance merge(TaInfosAvisEcheance detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaInfosAvisEcheance findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaInfosAvisEcheance findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaInfosAvisEcheance> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaAvisEcheanceDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaAvisEcheanceDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaInfosAvisEcheance> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaAvisEcheanceDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaAvisEcheanceDTO entityToDTO(TaInfosAvisEcheance entity) {
//		TaAvisEcheanceDTO dto = new TaAvisEcheanceDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaInfosAvisEcheanceMapper mapper = new TaInfosAvisEcheanceMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaAvisEcheanceDTO> listEntityToListDTO(List<TaInfosAvisEcheance> entity) {
		List<TaAvisEcheanceDTO> l = new ArrayList<TaAvisEcheanceDTO>();

		for (TaInfosAvisEcheance taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaAvisEcheanceDTO> selectAllDTO() {
		System.out.println("List of TaAvisEcheanceDTO EJB :");
		ArrayList<TaAvisEcheanceDTO> liste = new ArrayList<TaAvisEcheanceDTO>();

		List<TaInfosAvisEcheance> projects = selectAll();
		for(TaInfosAvisEcheance project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaAvisEcheanceDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaAvisEcheanceDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaAvisEcheanceDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaAvisEcheanceDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaAvisEcheanceDTO dto, String validationContext) throws EJBException {
		try {
			TaInfosAvisEcheanceMapper mapper = new TaInfosAvisEcheanceMapper();
			TaInfosAvisEcheance entity = null;
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
	
	public void persistDTO(TaAvisEcheanceDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaAvisEcheanceDTO dto, String validationContext) throws CreateException {
		try {
			TaInfosAvisEcheanceMapper mapper = new TaInfosAvisEcheanceMapper();
			TaInfosAvisEcheance entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaAvisEcheanceDTO dto) throws RemoveException {
		try {
			TaInfosAvisEcheanceMapper mapper = new TaInfosAvisEcheanceMapper();
			TaInfosAvisEcheance entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaInfosAvisEcheance refresh(TaInfosAvisEcheance persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaInfosAvisEcheance value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaInfosAvisEcheance value, String propertyName, String validationContext) {
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
	public void validateDTO(TaAvisEcheanceDTO dto, String validationContext) {
		try {
			TaInfosAvisEcheanceMapper mapper = new TaInfosAvisEcheanceMapper();
			TaInfosAvisEcheance entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaAvisEcheanceDTO> validator = new BeanValidator<TaAvisEcheanceDTO>(TaAvisEcheanceDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaAvisEcheanceDTO dto, String propertyName, String validationContext) {
		try {
			TaInfosAvisEcheanceMapper mapper = new TaInfosAvisEcheanceMapper();
			TaInfosAvisEcheance entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaAvisEcheanceDTO> validator = new BeanValidator<TaAvisEcheanceDTO>(TaAvisEcheanceDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaAvisEcheanceDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaAvisEcheanceDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaInfosAvisEcheance value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaInfosAvisEcheance value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
