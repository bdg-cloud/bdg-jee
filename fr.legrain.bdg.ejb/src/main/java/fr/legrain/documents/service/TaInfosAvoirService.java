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

import fr.legrain.bdg.documents.service.remote.ITaInfosAvoirServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaInfosAvoirMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.TaAvoirDTO;
import fr.legrain.document.model.TaInfosAvoir;
import fr.legrain.documents.dao.IInfosAvoirDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaInfosAvoirBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaInfosAvoirService extends AbstractApplicationDAOServer<TaInfosAvoir, TaAvoirDTO> implements ITaInfosAvoirServiceRemote {

	static Logger logger = Logger.getLogger(TaInfosAvoirService.class);

	@Inject private IInfosAvoirDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaInfosAvoirService() {
		super(TaInfosAvoir.class,TaAvoirDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaInfosAvoir a";
	
	public TaInfosAvoir findByCodeAvoir(String code) {
		return dao.findByCodeAvoir(code);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaInfosAvoir transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaInfosAvoir transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaInfosAvoir persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaInfosAvoir merge(TaInfosAvoir detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaInfosAvoir merge(TaInfosAvoir detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaInfosAvoir findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaInfosAvoir findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaInfosAvoir> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaAvoirDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaAvoirDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaInfosAvoir> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaAvoirDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaAvoirDTO entityToDTO(TaInfosAvoir entity) {
//		TaAvoirDTO dto = new TaAvoirDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaInfosAvoirMapper mapper = new TaInfosAvoirMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaAvoirDTO> listEntityToListDTO(List<TaInfosAvoir> entity) {
		List<TaAvoirDTO> l = new ArrayList<TaAvoirDTO>();

		for (TaInfosAvoir taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaAvoirDTO> selectAllDTO() {
		System.out.println("List of TaAvoirDTO EJB :");
		ArrayList<TaAvoirDTO> liste = new ArrayList<TaAvoirDTO>();

		List<TaInfosAvoir> projects = selectAll();
		for(TaInfosAvoir project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaAvoirDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaAvoirDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaAvoirDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaAvoirDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaAvoirDTO dto, String validationContext) throws EJBException {
		try {
			TaInfosAvoirMapper mapper = new TaInfosAvoirMapper();
			TaInfosAvoir entity = null;
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
	
	public void persistDTO(TaAvoirDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaAvoirDTO dto, String validationContext) throws CreateException {
		try {
			TaInfosAvoirMapper mapper = new TaInfosAvoirMapper();
			TaInfosAvoir entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaAvoirDTO dto) throws RemoveException {
		try {
			TaInfosAvoirMapper mapper = new TaInfosAvoirMapper();
			TaInfosAvoir entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaInfosAvoir refresh(TaInfosAvoir persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaInfosAvoir value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaInfosAvoir value, String propertyName, String validationContext) {
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
	public void validateDTO(TaAvoirDTO dto, String validationContext) {
		try {
			TaInfosAvoirMapper mapper = new TaInfosAvoirMapper();
			TaInfosAvoir entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaAvoirDTO> validator = new BeanValidator<TaAvoirDTO>(TaAvoirDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaAvoirDTO dto, String propertyName, String validationContext) {
		try {
			TaInfosAvoirMapper mapper = new TaInfosAvoirMapper();
			TaInfosAvoir entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaAvoirDTO> validator = new BeanValidator<TaAvoirDTO>(TaAvoirDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaAvoirDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaAvoirDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaInfosAvoir value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaInfosAvoir value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
