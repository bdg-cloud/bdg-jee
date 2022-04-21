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

import fr.legrain.bdg.documents.service.remote.ITaReglementTracaServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaReglementTracaMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.TaReglementTracaDTO;
import fr.legrain.document.model.TaReglementTraca;
import fr.legrain.documents.dao.IReglementTracaDAO;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaReglementTracaService extends AbstractApplicationDAOServer<TaReglementTraca, TaReglementTracaDTO> implements ITaReglementTracaServiceRemote {

	static Logger logger = Logger.getLogger(TaReglementTracaService.class);

	@Inject private IReglementTracaDAO dao;
	@Inject private	SessionInfo sessionInfo;



	
	public void persist(TaReglementTraca transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaReglementTraca transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaReglementTraca persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdReglementTraca()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaReglementTraca merge(TaReglementTraca detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaReglementTraca merge(TaReglementTraca detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaReglementTraca findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaReglementTraca findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaReglementTraca> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaReglementTracaDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaReglementTracaDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaReglementTraca> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaReglementTracaDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaReglementTracaDTO entityToDTO(TaReglementTraca entity) {
		TaReglementTracaMapper mapper = new TaReglementTracaMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaReglementTracaDTO> listEntityToListDTO(List<TaReglementTraca> entity) {
		List<TaReglementTracaDTO> l = new ArrayList<TaReglementTracaDTO>();

		for (TaReglementTraca taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaReglementTracaDTO> selectAllDTO() {
		System.out.println("List of TaReglementTracaDTO EJB :");
		ArrayList<TaReglementTracaDTO> liste = new ArrayList<TaReglementTracaDTO>();

		List<TaReglementTraca> projects = selectAll();
		for(TaReglementTraca project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaReglementTracaDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaReglementTracaDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaReglementTracaDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaReglementTracaDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaReglementTracaDTO dto, String validationContext) throws EJBException {
		try {
			TaReglementTracaMapper mapper = new TaReglementTracaMapper();
			TaReglementTraca entity = null;
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
	
	public void persistDTO(TaReglementTracaDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaReglementTracaDTO dto, String validationContext) throws CreateException {
		try {
			TaReglementTracaMapper mapper = new TaReglementTracaMapper();
			TaReglementTraca entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaReglementTracaDTO dto) throws RemoveException {
		try {
			TaReglementTracaMapper mapper = new TaReglementTracaMapper();
			TaReglementTraca entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaReglementTraca refresh(TaReglementTraca persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaReglementTraca value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaReglementTraca value, String propertyName, String validationContext) {
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
	public void validateDTO(TaReglementTracaDTO dto, String validationContext) {
		try {
			TaReglementTracaMapper mapper = new TaReglementTracaMapper();
			TaReglementTraca entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaReglementTracaDTO> validator = new BeanValidator<TaReglementTracaDTO>(TaReglementTracaDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaReglementTracaDTO dto, String propertyName, String validationContext) {
		try {
			TaReglementTracaMapper mapper = new TaReglementTracaMapper();
			TaReglementTraca entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaReglementTracaDTO> validator = new BeanValidator<TaReglementTracaDTO>(TaReglementTracaDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaReglementTracaDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaReglementTracaDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaReglementTraca value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaReglementTraca value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
	}







}
