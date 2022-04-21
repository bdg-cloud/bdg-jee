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

import fr.legrain.bdg.moncompte.service.remote.ITaCgPartenaireServiceRemote;
import fr.legrain.moncompte.dao.ICgPartenaireDAO;
import fr.legrain.moncompte.dao.ICgvDAO;
import fr.legrain.moncompte.data.AbstractApplicationDAOServer;
import fr.legrain.moncompte.dto.TaCgPartenaireDTO;
import fr.legrain.moncompte.model.TaCgPartenaire;
import fr.legrain.moncompte.model.mapping.mapper.TaCgPartenaireMapper;


/**
 * Session Bean implementation class TaCgPartenaireBean
 */
@Stateless
@DeclareRoles("admin")
public class TaCgPartenaireService extends AbstractApplicationDAOServer<TaCgPartenaire, TaCgPartenaireDTO> implements ITaCgPartenaireServiceRemote {

	static Logger logger = Logger.getLogger(TaCgPartenaireService.class);

	@Inject private ICgPartenaireDAO dao;


	/**
	 * Default constructor. 
	 */
	public TaCgPartenaireService() {
		super(TaCgPartenaire.class,TaCgPartenaireDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaCgPartenaire a";
	
	public TaCgPartenaire findCgPartenaireCourant() {
		return dao.findCgPartenaireCourant();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaCgPartenaire transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaCgPartenaire transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaCgPartenaire persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdCgPartenaire()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaCgPartenaire merge(TaCgPartenaire detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaCgPartenaire merge(TaCgPartenaire detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaCgPartenaire findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaCgPartenaire findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaCgPartenaire> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaCgPartenaireDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaCgPartenaireDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaCgPartenaire> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaCgPartenaireDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaCgPartenaireDTO entityToDTO(TaCgPartenaire entity) {
//		TaCgPartenaireDTO dto = new TaCgPartenaireDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaCgPartenaireMapper mapper = new TaCgPartenaireMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaCgPartenaireDTO> listEntityToListDTO(List<TaCgPartenaire> entity) {
		List<TaCgPartenaireDTO> l = new ArrayList<TaCgPartenaireDTO>();

		for (TaCgPartenaire taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaCgPartenaireDTO> selectAllDTO() {
		System.out.println("List of TaCgPartenaireDTO EJB :");
		ArrayList<TaCgPartenaireDTO> liste = new ArrayList<TaCgPartenaireDTO>();

		List<TaCgPartenaire> projects = selectAll();
		for(TaCgPartenaire project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaCgPartenaireDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaCgPartenaireDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaCgPartenaireDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaCgPartenaireDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaCgPartenaireDTO dto, String validationContext) throws EJBException {
		try {
			TaCgPartenaireMapper mapper = new TaCgPartenaireMapper();
			TaCgPartenaire entity = null;
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
	
	public void persistDTO(TaCgPartenaireDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaCgPartenaireDTO dto, String validationContext) throws CreateException {
		try {
			TaCgPartenaireMapper mapper = new TaCgPartenaireMapper();
			TaCgPartenaire entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaCgPartenaireDTO dto) throws RemoveException {
		try {
			TaCgPartenaireMapper mapper = new TaCgPartenaireMapper();
			TaCgPartenaire entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaCgPartenaire refresh(TaCgPartenaire persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaCgPartenaire value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaCgPartenaire value, String propertyName, String validationContext) {
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
	public void validateDTO(TaCgPartenaireDTO dto, String validationContext) {
		try {
			TaCgPartenaireMapper mapper = new TaCgPartenaireMapper();
			TaCgPartenaire entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaCgPartenaireDTO> validator = new BeanValidator<TaCgPartenaireDTO>(TaCgPartenaireDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaCgPartenaireDTO dto, String propertyName, String validationContext) {
		try {
			TaCgPartenaireMapper mapper = new TaCgPartenaireMapper();
			TaCgPartenaire entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaCgPartenaireDTO> validator = new BeanValidator<TaCgPartenaireDTO>(TaCgPartenaireDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaCgPartenaireDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaCgPartenaireDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaCgPartenaire value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaCgPartenaire value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
	}





}
