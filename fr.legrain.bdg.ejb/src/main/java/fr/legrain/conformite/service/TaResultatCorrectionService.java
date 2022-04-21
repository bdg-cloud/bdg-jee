package fr.legrain.conformite.service;

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

import fr.legrain.bdg.conformite.service.remote.ITaResultatCorrectionServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaResultatCorrectionMapper;
import fr.legrain.conformite.dao.IResultatCorrectionDAO;
import fr.legrain.conformite.dto.TaResultatCorrectionDTO;
import fr.legrain.conformite.model.TaResultatCorrection;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaResultatCorrectionBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaResultatCorrectionService extends AbstractApplicationDAOServer<TaResultatCorrection, TaResultatCorrectionDTO> implements ITaResultatCorrectionServiceRemote {

	static Logger logger = Logger.getLogger(TaResultatCorrectionService.class);

	@Inject private IResultatCorrectionDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaResultatCorrectionService() {
		super(TaResultatCorrection.class,TaResultatCorrectionDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaResultatCorrection a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaResultatCorrection transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaResultatCorrection transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaResultatCorrection persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdResultatCorrection()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaResultatCorrection merge(TaResultatCorrection detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaResultatCorrection merge(TaResultatCorrection detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaResultatCorrection findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaResultatCorrection findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaResultatCorrection> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaResultatCorrectionDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaResultatCorrectionDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaResultatCorrection> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaResultatCorrectionDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaResultatCorrectionDTO entityToDTO(TaResultatCorrection entity) {
//		TaResultatCorrectionDTO dto = new TaResultatCorrectionDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaResultatCorrectionMapper mapper = new TaResultatCorrectionMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaResultatCorrectionDTO> listEntityToListDTO(List<TaResultatCorrection> entity) {
		List<TaResultatCorrectionDTO> l = new ArrayList<TaResultatCorrectionDTO>();

		for (TaResultatCorrection taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaResultatCorrectionDTO> selectAllDTO() {
		System.out.println("List of TaResultatCorrectionDTO EJB :");
		ArrayList<TaResultatCorrectionDTO> liste = new ArrayList<TaResultatCorrectionDTO>();

		List<TaResultatCorrection> projects = selectAll();
		for(TaResultatCorrection project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaResultatCorrectionDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaResultatCorrectionDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaResultatCorrectionDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaResultatCorrectionDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaResultatCorrectionDTO dto, String validationContext) throws EJBException {
		try {
			TaResultatCorrectionMapper mapper = new TaResultatCorrectionMapper();
			TaResultatCorrection entity = null;
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
	
	public void persistDTO(TaResultatCorrectionDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaResultatCorrectionDTO dto, String validationContext) throws CreateException {
		try {
			TaResultatCorrectionMapper mapper = new TaResultatCorrectionMapper();
			TaResultatCorrection entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaResultatCorrectionDTO dto) throws RemoveException {
		try {
			TaResultatCorrectionMapper mapper = new TaResultatCorrectionMapper();
			TaResultatCorrection entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaResultatCorrection refresh(TaResultatCorrection persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaResultatCorrection value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaResultatCorrection value, String propertyName, String validationContext) {
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
	public void validateDTO(TaResultatCorrectionDTO dto, String validationContext) {
		try {
			TaResultatCorrectionMapper mapper = new TaResultatCorrectionMapper();
			TaResultatCorrection entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaResultatCorrectionDTO> validator = new BeanValidator<TaResultatCorrectionDTO>(TaResultatCorrectionDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaResultatCorrectionDTO dto, String propertyName, String validationContext) {
		try {
			TaResultatCorrectionMapper mapper = new TaResultatCorrectionMapper();
			TaResultatCorrection entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaResultatCorrectionDTO> validator = new BeanValidator<TaResultatCorrectionDTO>(TaResultatCorrectionDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaResultatCorrectionDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaResultatCorrectionDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaResultatCorrection value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaResultatCorrection value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
