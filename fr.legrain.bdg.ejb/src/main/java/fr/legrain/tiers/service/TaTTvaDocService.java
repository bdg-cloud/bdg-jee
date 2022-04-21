package fr.legrain.tiers.service;

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

import fr.legrain.bdg.model.mapping.mapper.TaTTvaDocMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTTvaDocServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.dao.ITTvaDocDAO;
import fr.legrain.tiers.dto.TaTTvaDocDTO;
import fr.legrain.tiers.model.TaTTvaDoc;
//import javax.ejb.Remote;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;


@Stateless
@DeclareRoles("admin")
@WebService
@Interceptors(ServerTenantInterceptor.class)
public class TaTTvaDocService extends AbstractApplicationDAOServer<TaTTvaDoc, TaTTvaDocDTO> implements ITaTTvaDocServiceRemote {

	static Logger logger = Logger.getLogger(TaTTvaDocService.class);

	@Inject private ITTvaDocDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaTTvaDocService() {
		super(TaTTvaDoc.class,TaTTvaDocDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTTvaDoc a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTTvaDoc transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTTvaDoc transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);
transientInstance.setCodeTTvaDoc(transientInstance.getCodeTTvaDoc().toUpperCase());
		dao.persist(transientInstance);
	}

	public void remove(TaTTvaDoc persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdTTvaDoc()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaTTvaDoc merge(TaTTvaDoc detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTTvaDoc merge(TaTTvaDoc detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);
detachedInstance.setCodeTTvaDoc(detachedInstance.getCodeTTvaDoc().toUpperCase());
		return dao.merge(detachedInstance);
	}

	public TaTTvaDoc findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTTvaDoc findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTTvaDoc> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTTvaDocDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTTvaDocDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTTvaDoc> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTTvaDocDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTTvaDocDTO entityToDTO(TaTTvaDoc entity) {
		TaTTvaDocMapper mapper = new TaTTvaDocMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTTvaDocDTO> listEntityToListDTO(List<TaTTvaDoc> entity) {
		List<TaTTvaDocDTO> l = new ArrayList<TaTTvaDocDTO>();

		for (TaTTvaDoc TaTTvaDoc : entity) {
			l.add(entityToDTO(TaTTvaDoc));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTTvaDocDTO> selectAllDTO() {
		System.out.println("List of TaTTvaDocDTO EJB :");
		ArrayList<TaTTvaDocDTO> liste = new ArrayList<TaTTvaDocDTO>();

		List<TaTTvaDoc> projects = selectAll();
		for(TaTTvaDoc project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTTvaDocDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTTvaDocDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTTvaDocDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTTvaDocDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTTvaDocDTO dto, String validationContext) throws EJBException {
		try {
			TaTTvaDocMapper mapper = new TaTTvaDocMapper();
			TaTTvaDoc entity = null;
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
	
	public void persistDTO(TaTTvaDocDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTTvaDocDTO dto, String validationContext) throws CreateException {
		try {
			TaTTvaDocMapper mapper = new TaTTvaDocMapper();
			TaTTvaDoc entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTTvaDocDTO dto) throws RemoveException {
		try {
			TaTTvaDocMapper mapper = new TaTTvaDocMapper();
			TaTTvaDoc entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTTvaDoc refresh(TaTTvaDoc persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTTvaDoc value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTTvaDoc value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTTvaDocDTO dto, String validationContext) {
		try {
			TaTTvaDocMapper mapper = new TaTTvaDocMapper();
			TaTTvaDoc entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTTvaDocDTO> validator = new BeanValidator<TaTTvaDocDTO>(TaTTvaDocDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTTvaDocDTO dto, String propertyName, String validationContext) {
		try {
			TaTTvaDocMapper mapper = new TaTTvaDocMapper();
			TaTTvaDoc entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTTvaDocDTO> validator = new BeanValidator<TaTTvaDocDTO>(TaTTvaDocDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTTvaDocDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTTvaDocDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTTvaDoc value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTTvaDoc value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
