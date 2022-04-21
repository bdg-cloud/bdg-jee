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

import fr.legrain.bdg.documents.service.remote.ITaLBoncdeServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLBoncdeServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaLBoncdeMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.TaLBoncdeDTO;
import fr.legrain.document.model.TaLBoncde;
import fr.legrain.documents.dao.ILBoncdeDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaLBoncdeBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaLBoncdeService extends AbstractApplicationDAOServer<TaLBoncde, TaLBoncdeDTO> implements ITaLBoncdeServiceRemote {

	static Logger logger = Logger.getLogger(TaLBoncdeService.class);

	@Inject private ILBoncdeDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaLBoncdeService() {
		super(TaLBoncde.class,TaLBoncdeDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaLBoncde a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaLBoncde transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaLBoncde transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaLBoncde persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaLBoncde merge(TaLBoncde detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaLBoncde merge(TaLBoncde detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaLBoncde findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaLBoncde findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaLBoncde> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaLBoncdeDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaLBoncdeDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaLBoncde> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaLBoncdeDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaLBoncdeDTO entityToDTO(TaLBoncde entity) {
//		TaLBoncdeDTO dto = new TaLBoncdeDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaLBoncdeMapper mapper = new TaLBoncdeMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaLBoncdeDTO> listEntityToListDTO(List<TaLBoncde> entity) {
		List<TaLBoncdeDTO> l = new ArrayList<TaLBoncdeDTO>();

		for (TaLBoncde taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaLBoncdeDTO> selectAllDTO() {
		System.out.println("List of TaLBoncdeDTO EJB :");
		ArrayList<TaLBoncdeDTO> liste = new ArrayList<TaLBoncdeDTO>();

		List<TaLBoncde> projects = selectAll();
		for(TaLBoncde project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaLBoncdeDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaLBoncdeDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaLBoncdeDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaLBoncdeDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaLBoncdeDTO dto, String validationContext) throws EJBException {
		try {
			TaLBoncdeMapper mapper = new TaLBoncdeMapper();
			TaLBoncde entity = null;
			if(dto.getIdLDocument()!=null) {
				entity = dao.findById(dto.getIdLDocument());
				if(dto.getVersionObj()!=entity.getVersionObj()) {
					throw new OptimisticLockException(entity,
							"L'objet à été modifié depuis le dernier accés. Client ID : "+dto.getIdLDocument()+" - Client Version objet : "+dto.getVersionObj()+" -Serveur Version Objet : "+entity.getVersionObj());
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
	
	public void persistDTO(TaLBoncdeDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaLBoncdeDTO dto, String validationContext) throws CreateException {
		try {
			TaLBoncdeMapper mapper = new TaLBoncdeMapper();
			TaLBoncde entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaLBoncdeDTO dto) throws RemoveException {
		try {
			TaLBoncdeMapper mapper = new TaLBoncdeMapper();
			TaLBoncde entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaLBoncde refresh(TaLBoncde persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaLBoncde value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaLBoncde value, String propertyName, String validationContext) {
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
	public void validateDTO(TaLBoncdeDTO dto, String validationContext) {
		try {
			TaLBoncdeMapper mapper = new TaLBoncdeMapper();
			TaLBoncde entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLBoncdeDTO> validator = new BeanValidator<TaLBoncdeDTO>(TaLBoncdeDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaLBoncdeDTO dto, String propertyName, String validationContext) {
		try {
			TaLBoncdeMapper mapper = new TaLBoncdeMapper();
			TaLBoncde entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLBoncdeDTO> validator = new BeanValidator<TaLBoncdeDTO>(TaLBoncdeDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaLBoncdeDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaLBoncdeDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaLBoncde value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaLBoncde value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
