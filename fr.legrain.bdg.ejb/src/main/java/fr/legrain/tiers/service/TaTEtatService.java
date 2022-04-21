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

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.bdg.model.mapping.mapper.TaTEtatMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTEtatServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.TaTEtatDTO;
import fr.legrain.document.model.TaTEtat;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.dao.ITEtatDAO;

/**
 * Session Bean implementation class TaTEtatBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaTEtatService extends AbstractApplicationDAOServer<TaTEtat, TaTEtatDTO> implements ITaTEtatServiceRemote {

	static Logger logger = Logger.getLogger(TaTEtatService.class);

	@Inject private ITEtatDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaTEtatService() {
		super(TaTEtat.class,TaTEtatDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTEtat a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTEtat transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTEtat transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);
transientInstance.setCodeTEtat(transientInstance.getCodeTEtat().toUpperCase());
		dao.persist(transientInstance);
	}

	public void remove(TaTEtat persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdTEtat()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaTEtat merge(TaTEtat detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTEtat merge(TaTEtat detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);
detachedInstance.setCodeTEtat(detachedInstance.getCodeTEtat().toUpperCase());
		return dao.merge(detachedInstance);
	}

	public TaTEtat findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTEtat findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTEtat> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTEtatDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTEtatDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTEtat> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTEtatDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTEtatDTO entityToDTO(TaTEtat entity) {
//		TaTEtatDTO dto = new TaTEtatDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaTEtatMapper mapper = new TaTEtatMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTEtatDTO> listEntityToListDTO(List<TaTEtat> entity) {
		List<TaTEtatDTO> l = new ArrayList<TaTEtatDTO>();

		for (TaTEtat taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTEtatDTO> selectAllDTO() {
		System.out.println("List of TaTEtatDTO EJB :");
		ArrayList<TaTEtatDTO> liste = new ArrayList<TaTEtatDTO>();

		List<TaTEtat> projects = selectAll();
		for(TaTEtat project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTEtatDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTEtatDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTEtatDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTEtatDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTEtatDTO dto, String validationContext) throws EJBException {
		try {
			TaTEtatMapper mapper = new TaTEtatMapper();
			TaTEtat entity = null;
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
	
	public void persistDTO(TaTEtatDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTEtatDTO dto, String validationContext) throws CreateException {
		try {
			TaTEtatMapper mapper = new TaTEtatMapper();
			TaTEtat entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTEtatDTO dto) throws RemoveException {
		try {
			TaTEtatMapper mapper = new TaTEtatMapper();
			TaTEtat entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTEtat refresh(TaTEtat persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTEtat value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTEtat value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTEtatDTO dto, String validationContext) {
		try {
			TaTEtatMapper mapper = new TaTEtatMapper();
			TaTEtat entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTEtatDTO> validator = new BeanValidator<TaTEtatDTO>(TaTEtatDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTEtatDTO dto, String propertyName, String validationContext) {
		try {
			TaTEtatMapper mapper = new TaTEtatMapper();
			TaTEtat entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTEtatDTO> validator = new BeanValidator<TaTEtatDTO>(TaTEtatDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTEtatDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTEtatDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTEtat value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTEtat value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
