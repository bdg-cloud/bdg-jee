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

import fr.legrain.bdg.model.mapping.mapper.TaTDocMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTDocServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.TaTDocDTO;
import fr.legrain.document.model.TaTDoc;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.dao.ITDocDAO;

/**
 * Session Bean implementation class TaTDocBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaTDocService extends AbstractApplicationDAOServer<TaTDoc, TaTDocDTO> implements ITaTDocServiceRemote {

	static Logger logger = Logger.getLogger(TaTDocService.class);

	@Inject private ITDocDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaTDocService() {
		super(TaTDoc.class,TaTDocDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTDoc a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTDoc transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTDoc transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);
transientInstance.setCodeTDoc(transientInstance.getCodeTDoc().toUpperCase());
		dao.persist(transientInstance);
	}

	public void remove(TaTDoc persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdTDoc()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaTDoc merge(TaTDoc detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTDoc merge(TaTDoc detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);
detachedInstance.setCodeTDoc(detachedInstance.getCodeTDoc().toUpperCase());
		return dao.merge(detachedInstance);
	}

	public TaTDoc findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTDoc findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTDoc> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTDocDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTDocDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTDoc> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTDocDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTDocDTO entityToDTO(TaTDoc entity) {
//		TaTDocDTO dto = new TaTDocDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaTDocMapper mapper = new TaTDocMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTDocDTO> listEntityToListDTO(List<TaTDoc> entity) {
		List<TaTDocDTO> l = new ArrayList<TaTDocDTO>();

		for (TaTDoc taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTDocDTO> selectAllDTO() {
		System.out.println("List of TaTDocDTO EJB :");
		ArrayList<TaTDocDTO> liste = new ArrayList<TaTDocDTO>();

		List<TaTDoc> projects = selectAll();
		for(TaTDoc project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTDocDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTDocDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTDocDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTDocDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTDocDTO dto, String validationContext) throws EJBException {
		try {
			TaTDocMapper mapper = new TaTDocMapper();
			TaTDoc entity = null;
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
	
	public void persistDTO(TaTDocDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTDocDTO dto, String validationContext) throws CreateException {
		try {
			TaTDocMapper mapper = new TaTDocMapper();
			TaTDoc entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTDocDTO dto) throws RemoveException {
		try {
			TaTDocMapper mapper = new TaTDocMapper();
			TaTDoc entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTDoc refresh(TaTDoc persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTDoc value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTDoc value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTDocDTO dto, String validationContext) {
		try {
			TaTDocMapper mapper = new TaTDocMapper();
			TaTDoc entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTDocDTO> validator = new BeanValidator<TaTDocDTO>(TaTDocDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTDocDTO dto, String propertyName, String validationContext) {
		try {
			TaTDocMapper mapper = new TaTDocMapper();
			TaTDoc entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTDocDTO> validator = new BeanValidator<TaTDocDTO>(TaTDocDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTDocDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTDocDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTDoc value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTDoc value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
