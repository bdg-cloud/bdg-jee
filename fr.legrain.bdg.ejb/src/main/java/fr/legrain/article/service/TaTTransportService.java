package fr.legrain.article.service;

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
import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.article.dao.ITReceptionDAO;
import fr.legrain.article.dao.ITTransportDAO;
import fr.legrain.article.dto.TaTTransportDTO;
import fr.legrain.article.model.TaTTransport;
import fr.legrain.article.model.TaTTransport;
import fr.legrain.bdg.article.service.remote.ITaTTransportServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaTTransportMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
//import javax.ejb.Remote;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


@Stateless
@DeclareRoles("admin")
@WebService
@Interceptors(ServerTenantInterceptor.class)
public class TaTTransportService extends AbstractApplicationDAOServer<TaTTransport, TaTTransportDTO> implements ITaTTransportServiceRemote {

	static Logger logger = Logger.getLogger(TaTTransportService.class);

	@Inject private ITTransportDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaTTransportService() {
		super(TaTTransport.class,TaTTransportDTO.class);
	}
	
	public List<TaTTransportDTO> findByCodeLight(String code) {
		return dao.findByCodeLight(code);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTTransport a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTTransport transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTTransport transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);
		transientInstance.setCodeTTransport(transientInstance.getCodeTTransport().toUpperCase());
		dao.persist(transientInstance);
	}

	public void remove(TaTTransport persistentInstance) throws RemoveException {
		dao.remove(dao.findById(persistentInstance.getIdTTransport()));
	}
	
	public TaTTransport merge(TaTTransport detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTTransport merge(TaTTransport detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);
		detachedInstance.setCodeTTransport(detachedInstance.getCodeTTransport().toUpperCase());
		return dao.merge(detachedInstance);
	}

	public TaTTransport findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTTransport findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTTransport> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTTransportDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTTransportDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTTransport> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTTransportDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTTransportDTO entityToDTO(TaTTransport entity) {
		TaTTransportMapper mapper = new TaTTransportMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTTransportDTO> listEntityToListDTO(List<TaTTransport> entity) {
		List<TaTTransportDTO> l = new ArrayList<TaTTransportDTO>();

		for (TaTTransport TaTTransport : entity) {
			l.add(entityToDTO(TaTTransport));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTTransportDTO> selectAllDTO() {
		System.out.println("List of TaTTransportDTO EJB :");
		ArrayList<TaTTransportDTO> liste = new ArrayList<TaTTransportDTO>();

		List<TaTTransport> projects = selectAll();
		for(TaTTransport project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTTransportDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTTransportDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTTransportDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTTransportDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTTransportDTO dto, String validationContext) throws EJBException {
		try {
			TaTTransportMapper mapper = new TaTTransportMapper();
			TaTTransport entity = null;
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
	
	public void persistDTO(TaTTransportDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTTransportDTO dto, String validationContext) throws CreateException {
		try {
			TaTTransportMapper mapper = new TaTTransportMapper();
			TaTTransport entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTTransportDTO dto) throws RemoveException {
		try {
			TaTTransportMapper mapper = new TaTTransportMapper();
			TaTTransport entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTTransport refresh(TaTTransport persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTTransport value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTTransport value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTTransportDTO dto, String validationContext) {
		try {
			TaTTransportMapper mapper = new TaTTransportMapper();
			TaTTransport entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTTransportDTO> validator = new BeanValidator<TaTTransportDTO>(TaTTransportDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTTransportDTO dto, String propertyName, String validationContext) {
		try {
			TaTTransportMapper mapper = new TaTTransportMapper();
			TaTTransport entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTTransportDTO> validator = new BeanValidator<TaTTransportDTO>(TaTTransportDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTTransportDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTTransportDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTTransport value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTTransport value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
