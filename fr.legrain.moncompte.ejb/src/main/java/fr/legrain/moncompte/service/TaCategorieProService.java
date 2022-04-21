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

import fr.legrain.bdg.moncompte.service.remote.ITaCategorieProServiceRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaCategorieProServiceRemote;
import fr.legrain.moncompte.dao.IAdresseDAO;
import fr.legrain.moncompte.dao.ICategorieProDAO;
import fr.legrain.moncompte.data.AbstractApplicationDAOServer;
import fr.legrain.moncompte.dto.TaCategorieProDTO;
import fr.legrain.moncompte.model.TaCategoriePro;
import fr.legrain.moncompte.model.TaCategoriePro;
import fr.legrain.moncompte.model.mapping.mapper.TaCategorieProMapper;


/**
 * Session Bean implementation class TaCategorieProBean
 */
@Stateless
@DeclareRoles("admin")
public class TaCategorieProService extends AbstractApplicationDAOServer<TaCategoriePro, TaCategorieProDTO> implements ITaCategorieProServiceRemote {

	static Logger logger = Logger.getLogger(TaCategorieProService.class);

	@Inject private ICategorieProDAO dao;


	/**
	 * Default constructor. 
	 */
	public TaCategorieProService() {
		super(TaCategoriePro.class,TaCategorieProDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaCategoriePro a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaCategoriePro transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaCategoriePro transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaCategoriePro persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdCategoriePro()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaCategoriePro merge(TaCategoriePro detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaCategoriePro merge(TaCategoriePro detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaCategoriePro findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaCategoriePro findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaCategoriePro> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaCategorieProDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaCategorieProDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaCategoriePro> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaCategorieProDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaCategorieProDTO entityToDTO(TaCategoriePro entity) {
//		TaCategorieProDTO dto = new TaCategorieProDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaCategorieProMapper mapper = new TaCategorieProMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaCategorieProDTO> listEntityToListDTO(List<TaCategoriePro> entity) {
		List<TaCategorieProDTO> l = new ArrayList<TaCategorieProDTO>();

		for (TaCategoriePro taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaCategorieProDTO> selectAllDTO() {
		System.out.println("List of TaCategorieProDTO EJB :");
		ArrayList<TaCategorieProDTO> liste = new ArrayList<TaCategorieProDTO>();

		List<TaCategoriePro> projects = selectAll();
		for(TaCategoriePro project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaCategorieProDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaCategorieProDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaCategorieProDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaCategorieProDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaCategorieProDTO dto, String validationContext) throws EJBException {
		try {
			TaCategorieProMapper mapper = new TaCategorieProMapper();
			TaCategoriePro entity = null;
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
	
	public void persistDTO(TaCategorieProDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaCategorieProDTO dto, String validationContext) throws CreateException {
		try {
			TaCategorieProMapper mapper = new TaCategorieProMapper();
			TaCategoriePro entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaCategorieProDTO dto) throws RemoveException {
		try {
			TaCategorieProMapper mapper = new TaCategorieProMapper();
			TaCategoriePro entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaCategoriePro refresh(TaCategoriePro persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaCategoriePro value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaCategoriePro value, String propertyName, String validationContext) {
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
	public void validateDTO(TaCategorieProDTO dto, String validationContext) {
		try {
			TaCategorieProMapper mapper = new TaCategorieProMapper();
			TaCategoriePro entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaCategorieProDTO> validator = new BeanValidator<TaCategorieProDTO>(TaCategorieProDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaCategorieProDTO dto, String propertyName, String validationContext) {
		try {
			TaCategorieProMapper mapper = new TaCategorieProMapper();
			TaCategoriePro entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaCategorieProDTO> validator = new BeanValidator<TaCategorieProDTO>(TaCategorieProDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaCategorieProDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaCategorieProDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaCategoriePro value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaCategoriePro value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
	}





}
