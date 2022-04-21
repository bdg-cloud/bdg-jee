package fr.legrain.moncompte.controle.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebMethod;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.bdg.controle.service.remote.ITaControleServiceRemote;
import fr.legrain.moncompte.controle.dao.IControleDAO;
import fr.legrain.moncompte.controle.dto.TaControleDTO;
import fr.legrain.moncompte.controle.model.TaControle;
import fr.legrain.moncompte.data.AbstractApplicationDAOServer;
import fr.legrain.moncompte.model.mapping.mapper.TaControleMapper;

/**
 * Session Bean implementation class TaControleBean
 */
@Stateless
//@DeclareRoles("admin")
public class TaControleService extends AbstractApplicationDAOServer<TaControle, TaControleDTO> implements ITaControleServiceRemote {

	static Logger logger = Logger.getLogger(TaControleService.class);

	@Inject private IControleDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaControleService() {
		super(TaControle.class,TaControleDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaControle a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void persist(TaControle transientInstance) throws CreateException {
		persist(transientInstance, null);
	}
	
	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaControle transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaControle persistentInstance) throws RemoveException {
		persistentInstance = dao.findById(persistentInstance.getIdControle());
		dao.remove(persistentInstance);
	}
	
	public TaControle merge(TaControle detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaControle merge(TaControle detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaControle findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaControle findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaControle> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaControleDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaControleDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaControle> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaControleDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaControleDTO entityToDTO(TaControle entity) {
//		TaControleDTO dto = new TaControleDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaControleMapper mapper = new TaControleMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaControleDTO> listEntityToListDTO(List<TaControle> entity) {
		List<TaControleDTO> l = new ArrayList<TaControleDTO>();

		for (TaControle taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaControleDTO> selectAllDTO() {
		System.out.println("List of TaControleDTO EJB :");
		ArrayList<TaControleDTO> liste = new ArrayList<TaControleDTO>();

		List<TaControle> projects = selectAll();
		for(TaControle project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaControleDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaControleDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaControleDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	@Override
	public void mergeDTO(TaControleDTO dto) throws EJBException {
		mergeDTO(dto,null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaControleDTO dto, String validationContext) throws EJBException {
		try {
			TaControleMapper mapper = new TaControleMapper();
			TaControle entity = null;
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
	
	@Override
	public void persistDTO(TaControleDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaControleDTO dto, String validationContext) throws CreateException {
		try {
			TaControleMapper mapper = new TaControleMapper();
			TaControle entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaControleDTO dto) throws RemoveException {
		try {
			TaControleMapper mapper = new TaControleMapper();
			TaControle entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaControle refresh(TaControle persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validateEntity(TaControle value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaControle value, String propertyName, String validationContext) {
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
	public void validateDTO(TaControleDTO dto, String validationContext) {
		try {
			TaControleMapper mapper = new TaControleMapper();
			TaControle entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaControleDTO> validator = new BeanValidator<TaControleDTO>(TaControleDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	public void validateDTOProperty(TaControleDTO dto, String propertyName, String validationContext) {
		try {
			TaControleMapper mapper = new TaControleMapper();
			TaControle entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaControleDTO> validator = new BeanValidator<TaControleDTO>(TaControleDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}

	@Override
	public void validateDTO(TaControleDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	public void validateDTOProperty(TaControleDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	public void validateEntity(TaControle value) {
		validateEntity(value,null);
	}

	@Override
	public void validateEntityProperty(TaControle value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
