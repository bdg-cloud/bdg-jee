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

import fr.legrain.moncompte.model.mapping.mapper.TaTypePartenaireMapper;
import fr.legrain.bdg.moncompte.service.remote.ITaTypePartenaireServiceRemote;
import fr.legrain.moncompte.dao.ITypePartenaireDAO;
import fr.legrain.moncompte.dao.ITypeProduitDAO;
import fr.legrain.moncompte.data.AbstractApplicationDAOServer;
import fr.legrain.moncompte.dto.TaTypePartenaireDTO;
import fr.legrain.moncompte.model.TaTypePartenaire;
import fr.legrain.moncompte.model.TaTypePartenaire;


/**
 * Session Bean implementation class TaTypePartenaireBean
 */
@Stateless
//@DeclareRoles("admin")
public class TaTypePartenaireService extends AbstractApplicationDAOServer<TaTypePartenaire, TaTypePartenaireDTO> implements ITaTypePartenaireServiceRemote {

	static Logger logger = Logger.getLogger(TaTypePartenaireService.class);

	@Inject private ITypePartenaireDAO dao;
	

	/**
	 * Default constructor. 
	 */
	public TaTypePartenaireService() {
		super(TaTypePartenaire.class,TaTypePartenaireDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTypePartenaire a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTypePartenaire transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTypePartenaire transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaTypePartenaire persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdTypePartenaire()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaTypePartenaire merge(TaTypePartenaire detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTypePartenaire merge(TaTypePartenaire detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaTypePartenaire findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTypePartenaire findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTypePartenaire> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTypePartenaireDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTypePartenaireDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTypePartenaire> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTypePartenaireDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTypePartenaireDTO entityToDTO(TaTypePartenaire entity) {
//		TaTypePartenaireDTO dto = new TaTypePartenaireDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaTypePartenaireMapper mapper = new TaTypePartenaireMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTypePartenaireDTO> listEntityToListDTO(List<TaTypePartenaire> entity) {
		List<TaTypePartenaireDTO> l = new ArrayList<TaTypePartenaireDTO>();

		for (TaTypePartenaire taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTypePartenaireDTO> selectAllDTO() {
		System.out.println("List of TaTypePartenaireDTO EJB :");
		ArrayList<TaTypePartenaireDTO> liste = new ArrayList<TaTypePartenaireDTO>();

		List<TaTypePartenaire> projects = selectAll();
		for(TaTypePartenaire project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTypePartenaireDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTypePartenaireDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTypePartenaireDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTypePartenaireDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTypePartenaireDTO dto, String validationContext) throws EJBException {
		try {
			TaTypePartenaireMapper mapper = new TaTypePartenaireMapper();
			TaTypePartenaire entity = null;
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
	
	public void persistDTO(TaTypePartenaireDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTypePartenaireDTO dto, String validationContext) throws CreateException {
		try {
			TaTypePartenaireMapper mapper = new TaTypePartenaireMapper();
			TaTypePartenaire entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTypePartenaireDTO dto) throws RemoveException {
		try {
			TaTypePartenaireMapper mapper = new TaTypePartenaireMapper();
			TaTypePartenaire entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTypePartenaire refresh(TaTypePartenaire persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTypePartenaire value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTypePartenaire value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTypePartenaireDTO dto, String validationContext) {
		try {
			TaTypePartenaireMapper mapper = new TaTypePartenaireMapper();
			TaTypePartenaire entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTypePartenaireDTO> validator = new BeanValidator<TaTypePartenaireDTO>(TaTypePartenaireDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTypePartenaireDTO dto, String propertyName, String validationContext) {
		try {
			TaTypePartenaireMapper mapper = new TaTypePartenaireMapper();
			TaTypePartenaire entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTypePartenaireDTO> validator = new BeanValidator<TaTypePartenaireDTO>(TaTypePartenaireDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTypePartenaireDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTypePartenaireDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTypePartenaire value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTypePartenaire value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
	}

}
