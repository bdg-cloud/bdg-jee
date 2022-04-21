package fr.legrain.moncompte.service;

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

import fr.legrain.bdg.moncompte.service.remote.ITaTNiveauServiceRemote;
import fr.legrain.moncompte.dao.ITNiveauDAO;
import fr.legrain.moncompte.data.AbstractApplicationDAOServer;
import fr.legrain.moncompte.dto.TaTNiveauDTO;
import fr.legrain.moncompte.model.TaTNiveau;
import fr.legrain.moncompte.model.mapping.mapper.TaTNiveauMapper;


/**
 * Session Bean implementation class TaTNiveauBean
 */
@Stateless
//@DeclareRoles("admin")
public class TaTNiveauService extends AbstractApplicationDAOServer<TaTNiveau, TaTNiveauDTO> implements ITaTNiveauServiceRemote {

	static Logger logger = Logger.getLogger(TaTNiveauService.class);

	@Inject private ITNiveauDAO dao;
	

	/**
	 * Default constructor. 
	 */
	public TaTNiveauService() {
		super(TaTNiveau.class,TaTNiveauDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTNiveau a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTNiveau transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTNiveau transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaTNiveau persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdTNiveau()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaTNiveau merge(TaTNiveau detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTNiveau merge(TaTNiveau detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaTNiveau findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTNiveau findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTNiveau> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTNiveauDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTNiveauDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTNiveau> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTNiveauDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTNiveauDTO entityToDTO(TaTNiveau entity) {
//		TaTNiveauDTO dto = new TaTNiveauDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaTNiveauMapper mapper = new TaTNiveauMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTNiveauDTO> listEntityToListDTO(List<TaTNiveau> entity) {
		List<TaTNiveauDTO> l = new ArrayList<TaTNiveauDTO>();

		for (TaTNiveau taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTNiveauDTO> selectAllDTO() {
		System.out.println("List of TaTNiveauDTO EJB :");
		ArrayList<TaTNiveauDTO> liste = new ArrayList<TaTNiveauDTO>();

		List<TaTNiveau> projects = selectAll();
		for(TaTNiveau project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTNiveauDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTNiveauDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTNiveauDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTNiveauDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTNiveauDTO dto, String validationContext) throws EJBException {
		try {
			TaTNiveauMapper mapper = new TaTNiveauMapper();
			TaTNiveau entity = null;
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
	
	public void persistDTO(TaTNiveauDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTNiveauDTO dto, String validationContext) throws CreateException {
		try {
			TaTNiveauMapper mapper = new TaTNiveauMapper();
			TaTNiveau entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTNiveauDTO dto) throws RemoveException {
		try {
			TaTNiveauMapper mapper = new TaTNiveauMapper();
			TaTNiveau entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTNiveau refresh(TaTNiveau persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTNiveau value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTNiveau value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTNiveauDTO dto, String validationContext) {
		try {
			TaTNiveauMapper mapper = new TaTNiveauMapper();
			TaTNiveau entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTNiveauDTO> validator = new BeanValidator<TaTNiveauDTO>(TaTNiveauDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTNiveauDTO dto, String propertyName, String validationContext) {
		try {
			TaTNiveauMapper mapper = new TaTNiveauMapper();
			TaTNiveau entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTNiveauDTO> validator = new BeanValidator<TaTNiveauDTO>(TaTNiveauDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTNiveauDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTNiveauDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTNiveau value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTNiveau value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
	}

}
