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

import fr.legrain.bdg.model.mapping.mapper.TaTelephoneMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTelephoneServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.dao.ITelephoneDAO;
import fr.legrain.tiers.dto.TaTelephoneDTO;
import fr.legrain.tiers.model.TaTelephone;

/**
 * Session Bean implementation class TaTelephoneBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaTelephoneService extends AbstractApplicationDAOServer<TaTelephone, TaTelephoneDTO> implements ITaTelephoneServiceRemote {

	static Logger logger = Logger.getLogger(TaTelephoneService.class);

	@Inject private ITelephoneDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaTelephoneService() {
		super(TaTelephone.class,TaTelephoneDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTelephone a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTelephone transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTelephone transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance);

		dao.persist(transientInstance);
	}

	public void remove(TaTelephone persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdTelephone()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaTelephone merge(TaTelephone detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTelephone merge(TaTelephone detachedInstance, String validationContext) {
		validateEntity(detachedInstance);

		return dao.merge(detachedInstance);
	}

	public TaTelephone findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTelephone findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTelephone> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTelephoneDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTelephoneDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTelephone> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTelephoneDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTelephoneDTO entityToDTO(TaTelephone entity) {
//		TaTelephoneDTO dto = new TaTelephoneDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaTelephoneMapper mapper = new TaTelephoneMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTelephoneDTO> listEntityToListDTO(List<TaTelephone> entity) {
		List<TaTelephoneDTO> l = new ArrayList<TaTelephoneDTO>();

		for (TaTelephone taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTelephoneDTO> selectAllDTO() {
		System.out.println("List of TaTelephoneDTO EJB :");
		ArrayList<TaTelephoneDTO> liste = new ArrayList<TaTelephoneDTO>();

		List<TaTelephone> projects = selectAll();
		for(TaTelephone project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTelephoneDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTelephoneDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTelephoneDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTelephoneDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTelephoneDTO dto, String validationContext) throws EJBException {
		try {
			TaTelephoneMapper mapper = new TaTelephoneMapper();
			TaTelephone entity = null;
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
	
	public void persistDTO(TaTelephoneDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTelephoneDTO dto, String validationContext) throws CreateException {
		try {
			TaTelephoneMapper mapper = new TaTelephoneMapper();
			TaTelephone entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTelephoneDTO dto) throws RemoveException {
		try {
			TaTelephoneMapper mapper = new TaTelephoneMapper();
			TaTelephone entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTelephone refresh(TaTelephone persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTelephone value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTelephone value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTelephoneDTO dto, String validationContext) {
		try {
			TaTelephoneMapper mapper = new TaTelephoneMapper();
			TaTelephone entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTelephoneDTO> validator = new BeanValidator<TaTelephoneDTO>(TaTelephoneDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTelephoneDTO dto, String propertyName, String validationContext) {
		try {
			TaTelephoneMapper mapper = new TaTelephoneMapper();
			TaTelephone entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTelephoneDTO> validator = new BeanValidator<TaTelephoneDTO>(TaTelephoneDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTelephoneDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTelephoneDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTelephone value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTelephone value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

	@Override
	public List<TaTelephoneDTO> findAllLight() {
		return dao.findAllLight();
	}

}
