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

import fr.legrain.bdg.moncompte.service.remote.ITaPrixParUtilisateurPersoServiceRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaPrixParUtilisateurPersoServiceRemote;
import fr.legrain.moncompte.dao.IPrixParUtilisateurDAO;
import fr.legrain.moncompte.dao.IPrixParUtilisateurPersoDAO;
import fr.legrain.moncompte.dao.IPrixPersoDAO;
import fr.legrain.moncompte.data.AbstractApplicationDAOServer;
import fr.legrain.moncompte.dto.TaPrixParUtilisateurPersoDTO;
import fr.legrain.moncompte.model.TaPrixParUtilisateurPerso;
import fr.legrain.moncompte.model.mapping.mapper.TaPrixParUtilisateurPersoMapper;


/**
 * Session Bean implementation class TaPrixParUtilisateurPersoBean
 */
@Stateless
@DeclareRoles("admin")
public class TaPrixParUtilisateurPersoService extends AbstractApplicationDAOServer<TaPrixParUtilisateurPerso, TaPrixParUtilisateurPersoDTO> implements ITaPrixParUtilisateurPersoServiceRemote {

	static Logger logger = Logger.getLogger(TaPrixParUtilisateurPersoService.class);

	@Inject private IPrixParUtilisateurPersoDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaPrixParUtilisateurPersoService() {
		super(TaPrixParUtilisateurPerso.class,TaPrixParUtilisateurPersoDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaPrixParUtilisateurPerso a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaPrixParUtilisateurPerso transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaPrixParUtilisateurPerso transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaPrixParUtilisateurPerso persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getId()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaPrixParUtilisateurPerso merge(TaPrixParUtilisateurPerso detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaPrixParUtilisateurPerso merge(TaPrixParUtilisateurPerso detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaPrixParUtilisateurPerso findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaPrixParUtilisateurPerso findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaPrixParUtilisateurPerso> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaPrixParUtilisateurPersoDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaPrixParUtilisateurPersoDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaPrixParUtilisateurPerso> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaPrixParUtilisateurPersoDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaPrixParUtilisateurPersoDTO entityToDTO(TaPrixParUtilisateurPerso entity) {
//		TaPrixParUtilisateurPersoDTO dto = new TaPrixParUtilisateurPersoDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaPrixParUtilisateurPersoMapper mapper = new TaPrixParUtilisateurPersoMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaPrixParUtilisateurPersoDTO> listEntityToListDTO(List<TaPrixParUtilisateurPerso> entity) {
		List<TaPrixParUtilisateurPersoDTO> l = new ArrayList<TaPrixParUtilisateurPersoDTO>();

		for (TaPrixParUtilisateurPerso taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaPrixParUtilisateurPersoDTO> selectAllDTO() {
		System.out.println("List of TaPrixParUtilisateurPersoDTO EJB :");
		ArrayList<TaPrixParUtilisateurPersoDTO> liste = new ArrayList<TaPrixParUtilisateurPersoDTO>();

		List<TaPrixParUtilisateurPerso> projects = selectAll();
		for(TaPrixParUtilisateurPerso project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaPrixParUtilisateurPersoDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaPrixParUtilisateurPersoDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaPrixParUtilisateurPersoDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaPrixParUtilisateurPersoDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaPrixParUtilisateurPersoDTO dto, String validationContext) throws EJBException {
		try {
			TaPrixParUtilisateurPersoMapper mapper = new TaPrixParUtilisateurPersoMapper();
			TaPrixParUtilisateurPerso entity = null;
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
	
	public void persistDTO(TaPrixParUtilisateurPersoDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaPrixParUtilisateurPersoDTO dto, String validationContext) throws CreateException {
		try {
			TaPrixParUtilisateurPersoMapper mapper = new TaPrixParUtilisateurPersoMapper();
			TaPrixParUtilisateurPerso entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaPrixParUtilisateurPersoDTO dto) throws RemoveException {
		try {
			TaPrixParUtilisateurPersoMapper mapper = new TaPrixParUtilisateurPersoMapper();
			TaPrixParUtilisateurPerso entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaPrixParUtilisateurPerso refresh(TaPrixParUtilisateurPerso persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaPrixParUtilisateurPerso value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaPrixParUtilisateurPerso value, String propertyName, String validationContext) {
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
	public void validateDTO(TaPrixParUtilisateurPersoDTO dto, String validationContext) {
		try {
			TaPrixParUtilisateurPersoMapper mapper = new TaPrixParUtilisateurPersoMapper();
			TaPrixParUtilisateurPerso entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaPrixParUtilisateurPersoDTO> validator = new BeanValidator<TaPrixParUtilisateurPersoDTO>(TaPrixParUtilisateurPersoDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaPrixParUtilisateurPersoDTO dto, String propertyName, String validationContext) {
		try {
			TaPrixParUtilisateurPersoMapper mapper = new TaPrixParUtilisateurPersoMapper();
			TaPrixParUtilisateurPerso entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaPrixParUtilisateurPersoDTO> validator = new BeanValidator<TaPrixParUtilisateurPersoDTO>(TaPrixParUtilisateurPersoDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaPrixParUtilisateurPersoDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaPrixParUtilisateurPersoDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaPrixParUtilisateurPerso value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaPrixParUtilisateurPerso value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
	}

}
