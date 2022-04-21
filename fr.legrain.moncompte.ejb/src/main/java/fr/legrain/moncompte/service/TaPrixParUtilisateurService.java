package fr.legrain.moncompte.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebMethod;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.bdg.moncompte.service.remote.ITaPrixParUtilisateurServiceRemote;
import fr.legrain.moncompte.dao.IPrixParUtilisateurDAO;
import fr.legrain.moncompte.data.AbstractApplicationDAOServer;
import fr.legrain.moncompte.dto.TaPrixParUtilisateurDTO;
import fr.legrain.moncompte.model.TaPrixParUtilisateur;
import fr.legrain.moncompte.model.mapping.mapper.TaPrixParUtilisateurMapper;


/**
 * Session Bean implementation class TaPrixParUtilisateurBean
 */
@Stateless
@DeclareRoles("admin")
public class TaPrixParUtilisateurService extends AbstractApplicationDAOServer<TaPrixParUtilisateur, TaPrixParUtilisateurDTO> implements ITaPrixParUtilisateurServiceRemote {

	static Logger logger = Logger.getLogger(TaPrixParUtilisateurService.class);

	@Inject private IPrixParUtilisateurDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaPrixParUtilisateurService() {
		super(TaPrixParUtilisateur.class,TaPrixParUtilisateurDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaPrixParUtilisateur a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaPrixParUtilisateur transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaPrixParUtilisateur transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaPrixParUtilisateur persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getId()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaPrixParUtilisateur merge(TaPrixParUtilisateur detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaPrixParUtilisateur merge(TaPrixParUtilisateur detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaPrixParUtilisateur findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaPrixParUtilisateur findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaPrixParUtilisateur> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaPrixParUtilisateurDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaPrixParUtilisateurDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaPrixParUtilisateur> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaPrixParUtilisateurDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaPrixParUtilisateurDTO entityToDTO(TaPrixParUtilisateur entity) {
//		TaPrixParUtilisateurDTO dto = new TaPrixParUtilisateurDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaPrixParUtilisateurMapper mapper = new TaPrixParUtilisateurMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaPrixParUtilisateurDTO> listEntityToListDTO(List<TaPrixParUtilisateur> entity) {
		List<TaPrixParUtilisateurDTO> l = new ArrayList<TaPrixParUtilisateurDTO>();

		for (TaPrixParUtilisateur taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaPrixParUtilisateurDTO> selectAllDTO() {
		System.out.println("List of TaPrixParUtilisateurDTO EJB :");
		ArrayList<TaPrixParUtilisateurDTO> liste = new ArrayList<TaPrixParUtilisateurDTO>();

		List<TaPrixParUtilisateur> projects = selectAll();
		for(TaPrixParUtilisateur project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaPrixParUtilisateurDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaPrixParUtilisateurDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaPrixParUtilisateurDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaPrixParUtilisateurDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaPrixParUtilisateurDTO dto, String validationContext) throws EJBException {
		try {
			TaPrixParUtilisateurMapper mapper = new TaPrixParUtilisateurMapper();
			TaPrixParUtilisateur entity = null;
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
	
	public void persistDTO(TaPrixParUtilisateurDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaPrixParUtilisateurDTO dto, String validationContext) throws CreateException {
		try {
			TaPrixParUtilisateurMapper mapper = new TaPrixParUtilisateurMapper();
			TaPrixParUtilisateur entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaPrixParUtilisateurDTO dto) throws RemoveException {
		try {
			TaPrixParUtilisateurMapper mapper = new TaPrixParUtilisateurMapper();
			TaPrixParUtilisateur entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaPrixParUtilisateur refresh(TaPrixParUtilisateur persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaPrixParUtilisateur value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaPrixParUtilisateur value, String propertyName, String validationContext) {
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
	public void validateDTO(TaPrixParUtilisateurDTO dto, String validationContext) {
		try {
			TaPrixParUtilisateurMapper mapper = new TaPrixParUtilisateurMapper();
			TaPrixParUtilisateur entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaPrixParUtilisateurDTO> validator = new BeanValidator<TaPrixParUtilisateurDTO>(TaPrixParUtilisateurDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaPrixParUtilisateurDTO dto, String propertyName, String validationContext) {
		try {
			TaPrixParUtilisateurMapper mapper = new TaPrixParUtilisateurMapper();
			TaPrixParUtilisateur entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaPrixParUtilisateurDTO> validator = new BeanValidator<TaPrixParUtilisateurDTO>(TaPrixParUtilisateurDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaPrixParUtilisateurDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaPrixParUtilisateurDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaPrixParUtilisateur value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaPrixParUtilisateur value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
	}

}
