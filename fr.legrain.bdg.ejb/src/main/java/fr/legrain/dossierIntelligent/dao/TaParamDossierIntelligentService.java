package fr.legrain.dossierIntelligent.dao;

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

import fr.legrain.bdg.dossierIntelligent.service.remote.ITaParamDossierIntelligentServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaParamDossierIntelligentMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.dossierIntelligent.dto.TaParamDossierIntelligentDTO;
import fr.legrain.dossierIntelligent.model.TaParamDossierIntel;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaParamDossierIntelligentService extends AbstractApplicationDAOServer<TaParamDossierIntel, TaParamDossierIntelligentDTO> implements ITaParamDossierIntelligentServiceRemote {

	static Logger logger = Logger.getLogger(TaParamDossierIntelligentService.class);

	@Inject private ITaParamDossierIntelligentDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaParamDossierIntelligentService() {
		super(TaParamDossierIntel.class,TaParamDossierIntelligentDTO.class);
	}
	


	
	public void persist(TaParamDossierIntel transientInstance) throws CreateException {
		persist(transientInstance, null);
	}


	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaParamDossierIntel transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaParamDossierIntel persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getId()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaParamDossierIntel merge(TaParamDossierIntel detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaParamDossierIntel merge(TaParamDossierIntel detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaParamDossierIntel findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaParamDossierIntel findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaParamDossierIntel> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaParamDossierIntelligentDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaParamDossierIntelligentDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaParamDossierIntel> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaParamDossierIntelligentDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaParamDossierIntelligentDTO entityToDTO(TaParamDossierIntel entity) {
//		TaAutorisationsDossierDossierDTO dto = new TaAutorisationsDossierDossierDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaParamDossierIntelligentMapper mapper = new TaParamDossierIntelligentMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaParamDossierIntelligentDTO> listEntityToListDTO(List<TaParamDossierIntel> entity) {
		List<TaParamDossierIntelligentDTO> l = new ArrayList<TaParamDossierIntelligentDTO>();

		for (TaParamDossierIntel taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaParamDossierIntelligentDTO> selectAllDTO() {
		System.out.println("List of TaParamDossierIntelligentDTO EJB :");
		ArrayList<TaParamDossierIntelligentDTO> liste = new ArrayList<TaParamDossierIntelligentDTO>();

		List<TaParamDossierIntel> projects = selectAll();
		for(TaParamDossierIntel project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaParamDossierIntelligentDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaParamDossierIntelligentDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaParamDossierIntelligentDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaParamDossierIntelligentDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaParamDossierIntelligentDTO dto, String validationContext) throws EJBException {
		try {
			TaParamDossierIntelligentMapper mapper = new TaParamDossierIntelligentMapper();
			TaParamDossierIntel entity = null;
			if(dto.getId()!=0) {
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
	
	public void persistDTO(TaParamDossierIntelligentDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaParamDossierIntelligentDTO dto, String validationContext) throws CreateException {
		try {
			TaParamDossierIntelligentMapper mapper = new TaParamDossierIntelligentMapper();
			TaParamDossierIntel entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaParamDossierIntelligentDTO dto) throws RemoveException {
		try {
			TaParamDossierIntelligentMapper mapper = new TaParamDossierIntelligentMapper();
			TaParamDossierIntel entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaParamDossierIntel refresh(TaParamDossierIntel persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaParamDossierIntel value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaParamDossierIntel value, String propertyName, String validationContext) {
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
	public void validateDTO(TaParamDossierIntelligentDTO dto, String validationContext) {
		try {
			TaParamDossierIntelligentMapper mapper = new TaParamDossierIntelligentMapper();
			TaParamDossierIntel entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaAutorisationsDossierDossierDTO> validator = new BeanValidator<TaAutorisationsDossierDossierDTO>(TaAutorisationsDossierDossierDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaParamDossierIntelligentDTO dto, String propertyName, String validationContext) {
		try {
			TaParamDossierIntelligentMapper mapper = new TaParamDossierIntelligentMapper();
			TaParamDossierIntel entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaAutorisationsDossierDossierDTO> validator = new BeanValidator<TaAutorisationsDossierDossierDTO>(TaAutorisationsDossierDossierDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaParamDossierIntelligentDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaParamDossierIntelligentDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaParamDossierIntel value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaParamDossierIntel value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}



}
