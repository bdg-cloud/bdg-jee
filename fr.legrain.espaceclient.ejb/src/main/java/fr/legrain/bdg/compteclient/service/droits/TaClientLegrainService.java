package fr.legrain.bdg.compteclient.service.droits;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebMethod;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.bdg.compteclient.dao.droits.IClientLegrainDAO;
import fr.legrain.bdg.compteclient.dto.droits.TaClientLegrainDTO;
import fr.legrain.bdg.compteclient.model.droits.TaClientLegrain;
import fr.legrain.bdg.compteclient.model.mapping.mapper.TaClientLegrainMapper;
import fr.legrain.bdg.compteclient.service.interfaces.remote.droits.ITaClientLegrainServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;


/**
 * Session Bean implementation class TaClientLegrainBean
 */
@Stateless
//@Interceptors(ServerTenantInterceptor.class)
public class TaClientLegrainService extends AbstractApplicationDAOServer<TaClientLegrain, TaClientLegrainDTO> implements ITaClientLegrainServiceRemote {

	static Logger logger = Logger.getLogger(TaClientLegrainService.class);

	@Inject private IClientLegrainDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaClientLegrainService() {
		super(TaClientLegrain.class,TaClientLegrainDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaClientLegrain a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaClientLegrain transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaClientLegrain transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaClientLegrain persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getId()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaClientLegrain merge(TaClientLegrain detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaClientLegrain merge(TaClientLegrain detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaClientLegrain findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaClientLegrain findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaClientLegrain> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaClientLegrainDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaClientLegrainDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaClientLegrain> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaClientLegrainDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaClientLegrainDTO entityToDTO(TaClientLegrain entity) {
//		TaClientLegrainDTO dto = new TaClientLegrainDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaClientLegrainMapper mapper = new TaClientLegrainMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaClientLegrainDTO> listEntityToListDTO(List<TaClientLegrain> entity) {
		List<TaClientLegrainDTO> l = new ArrayList<TaClientLegrainDTO>();

		for (TaClientLegrain taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaClientLegrainDTO> selectAllDTO() {
		System.out.println("List of TaClientLegrainDTO EJB :");
		ArrayList<TaClientLegrainDTO> liste = new ArrayList<TaClientLegrainDTO>();

		List<TaClientLegrain> projects = selectAll();
		for(TaClientLegrain project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaClientLegrainDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaClientLegrainDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaClientLegrainDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaClientLegrainDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaClientLegrainDTO dto, String validationContext) throws EJBException {
		try {
			TaClientLegrainMapper mapper = new TaClientLegrainMapper();
			TaClientLegrain entity = null;
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
	
	public void persistDTO(TaClientLegrainDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaClientLegrainDTO dto, String validationContext) throws CreateException {
		try {
			TaClientLegrainMapper mapper = new TaClientLegrainMapper();
			TaClientLegrain entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaClientLegrainDTO dto) throws RemoveException {
		try {
			TaClientLegrainMapper mapper = new TaClientLegrainMapper();
			TaClientLegrain entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaClientLegrain refresh(TaClientLegrain persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaClientLegrain value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaClientLegrain value, String propertyName, String validationContext) {
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
	public void validateDTO(TaClientLegrainDTO dto, String validationContext) {
		try {
			TaClientLegrainMapper mapper = new TaClientLegrainMapper();
			TaClientLegrain entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaClientLegrainDTO> validator = new BeanValidator<TaClientLegrainDTO>(TaClientLegrainDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaClientLegrainDTO dto, String propertyName, String validationContext) {
		try {
			TaClientLegrainMapper mapper = new TaClientLegrainMapper();
			TaClientLegrain entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaClientLegrainDTO> validator = new BeanValidator<TaClientLegrainDTO>(TaClientLegrainDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaClientLegrainDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaClientLegrainDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaClientLegrain value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaClientLegrain value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
	}

}
