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

import fr.legrain.bdg.documents.service.remote.ITaInfosBoncdeServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaInfosBoncdeMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.TaBoncdeDTO;
import fr.legrain.document.model.TaInfosBoncde;
import fr.legrain.documents.dao.IInfosBoncdeDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaInfosBoncdeBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaInfosBoncdeService extends AbstractApplicationDAOServer<TaInfosBoncde, TaBoncdeDTO> implements ITaInfosBoncdeServiceRemote {

	static Logger logger = Logger.getLogger(TaInfosBoncdeService.class);

	@Inject private IInfosBoncdeDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaInfosBoncdeService() {
		super(TaInfosBoncde.class,TaBoncdeDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaInfosBoncde a";
	
	public TaInfosBoncde findByCodeBoncde(String code) {
		return dao.findByCodeBoncde(code);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaInfosBoncde transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaInfosBoncde transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaInfosBoncde persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaInfosBoncde merge(TaInfosBoncde detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaInfosBoncde merge(TaInfosBoncde detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaInfosBoncde findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaInfosBoncde findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaInfosBoncde> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaBoncdeDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaBoncdeDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaInfosBoncde> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaBoncdeDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaBoncdeDTO entityToDTO(TaInfosBoncde entity) {
//		TaBoncdeDTO dto = new TaBoncdeDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaInfosBoncdeMapper mapper = new TaInfosBoncdeMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaBoncdeDTO> listEntityToListDTO(List<TaInfosBoncde> entity) {
		List<TaBoncdeDTO> l = new ArrayList<TaBoncdeDTO>();

		for (TaInfosBoncde taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaBoncdeDTO> selectAllDTO() {
		System.out.println("List of TaBoncdeDTO EJB :");
		ArrayList<TaBoncdeDTO> liste = new ArrayList<TaBoncdeDTO>();

		List<TaInfosBoncde> projects = selectAll();
		for(TaInfosBoncde project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaBoncdeDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaBoncdeDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaBoncdeDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaBoncdeDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaBoncdeDTO dto, String validationContext) throws EJBException {
		try {
			TaInfosBoncdeMapper mapper = new TaInfosBoncdeMapper();
			TaInfosBoncde entity = null;
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
	
	public void persistDTO(TaBoncdeDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaBoncdeDTO dto, String validationContext) throws CreateException {
		try {
			TaInfosBoncdeMapper mapper = new TaInfosBoncdeMapper();
			TaInfosBoncde entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaBoncdeDTO dto) throws RemoveException {
		try {
			TaInfosBoncdeMapper mapper = new TaInfosBoncdeMapper();
			TaInfosBoncde entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaInfosBoncde refresh(TaInfosBoncde persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaInfosBoncde value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaInfosBoncde value, String propertyName, String validationContext) {
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
	public void validateDTO(TaBoncdeDTO dto, String validationContext) {
		try {
			TaInfosBoncdeMapper mapper = new TaInfosBoncdeMapper();
			TaInfosBoncde entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaBoncdeDTO> validator = new BeanValidator<TaBoncdeDTO>(TaBoncdeDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaBoncdeDTO dto, String propertyName, String validationContext) {
		try {
			TaInfosBoncdeMapper mapper = new TaInfosBoncdeMapper();
			TaInfosBoncde entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaBoncdeDTO> validator = new BeanValidator<TaBoncdeDTO>(TaBoncdeDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaBoncdeDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaBoncdeDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaInfosBoncde value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaInfosBoncde value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
