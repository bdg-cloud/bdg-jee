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

import fr.legrain.bdg.model.mapping.mapper.TaTTelMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTTelServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.dao.ITTelDAO;
import fr.legrain.tiers.dto.TaTTelDTO;
import fr.legrain.tiers.model.TaTTel;
//import javax.ejb.Remote;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;


@Stateless
@DeclareRoles("admin")
@WebService
@Interceptors(ServerTenantInterceptor.class)
public class TaTTelService extends AbstractApplicationDAOServer<TaTTel, TaTTelDTO> implements ITaTTelServiceRemote {

	static Logger logger = Logger.getLogger(TaTTelService.class);

	@Inject private ITTelDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaTTelService() {
		super(TaTTel.class,TaTTelDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTTel a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTTel transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTTel transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);
transientInstance.setCodeTTel(transientInstance.getCodeTTel().toUpperCase());
		dao.persist(transientInstance);
	}

	public void remove(TaTTel persistentInstance) throws RemoveException {
		dao.remove(dao.findById(persistentInstance.getIdTTel()));
	}
	
	public TaTTel merge(TaTTel detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTTel merge(TaTTel detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);
		detachedInstance.setCodeTTel(detachedInstance.getCodeTTel().toUpperCase());
		return dao.merge(detachedInstance);
	}

	public TaTTel findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTTel findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTTel> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTTelDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTTelDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTTel> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTTelDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTTelDTO entityToDTO(TaTTel entity) {
		TaTTelMapper mapper = new TaTTelMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTTelDTO> listEntityToListDTO(List<TaTTel> entity) {
		List<TaTTelDTO> l = new ArrayList<TaTTelDTO>();

		for (TaTTel TaTTel : entity) {
			l.add(entityToDTO(TaTTel));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTTelDTO> selectAllDTO() {
		System.out.println("List of TaTTelDTO EJB :");
		ArrayList<TaTTelDTO> liste = new ArrayList<TaTTelDTO>();

		List<TaTTel> projects = selectAll();
		for(TaTTel project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTTelDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTTelDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTTelDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTTelDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTTelDTO dto, String validationContext) throws EJBException {
		try {
			TaTTelMapper mapper = new TaTTelMapper();
			TaTTel entity = null;
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
	
	public void persistDTO(TaTTelDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTTelDTO dto, String validationContext) throws CreateException {
		try {
			TaTTelMapper mapper = new TaTTelMapper();
			TaTTel entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTTelDTO dto) throws RemoveException {
		try {
			TaTTelMapper mapper = new TaTTelMapper();
			TaTTel entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTTel refresh(TaTTel persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTTel value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTTel value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTTelDTO dto, String validationContext) {
		try {
			TaTTelMapper mapper = new TaTTelMapper();
			TaTTel entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTTelDTO> validator = new BeanValidator<TaTTelDTO>(TaTTelDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTTelDTO dto, String propertyName, String validationContext) {
		try {
			TaTTelMapper mapper = new TaTTelMapper();
			TaTTel entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTTelDTO> validator = new BeanValidator<TaTTelDTO>(TaTTelDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTTelDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTTelDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTTel value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTTel value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
