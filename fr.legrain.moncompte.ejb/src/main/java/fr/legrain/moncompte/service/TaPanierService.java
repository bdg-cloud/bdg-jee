package fr.legrain.moncompte.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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

import fr.legrain.moncompte.model.mapping.mapper.TaPanierMapper;
import fr.legrain.bdg.moncompte.service.remote.ITaPanierServiceRemote;
import fr.legrain.moncompte.dao.IPanierDAO;
import fr.legrain.moncompte.data.AbstractApplicationDAOServer;
import fr.legrain.moncompte.dto.TaPanierDTO;
import fr.legrain.moncompte.model.TaPanier;


/**
 * Session Bean implementation class TaPanierBean
 */
@Stateless
//@DeclareRoles("admin")
public class TaPanierService extends AbstractApplicationDAOServer<TaPanier, TaPanierDTO> implements ITaPanierServiceRemote {

	static Logger logger = Logger.getLogger(TaPanierService.class);

	@Inject private IPanierDAO dao;
	
	
	public List<TaPanier> findPanierDossier(String codeDossier) {
		return dao.findPanierDossier(codeDossier);
	}

	@Override
	public List<TaPanier> findPanierClient(String codeClient) {
		return dao.findPanierDossier(codeClient);
	}

	/**
	 * Default constructor. 
	 */
	public TaPanierService() {
		super(TaPanier.class,TaPanierDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaPanier a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaPanier transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaPanier transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaPanier persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdPanier()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaPanier merge(TaPanier detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaPanier merge(TaPanier detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaPanier findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaPanier findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaPanier> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaPanierDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaPanierDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaPanier> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaPanierDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaPanierDTO entityToDTO(TaPanier entity) {
//		TaPanierDTO dto = new TaPanierDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaPanierMapper mapper = new TaPanierMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaPanierDTO> listEntityToListDTO(List<TaPanier> entity) {
		List<TaPanierDTO> l = new ArrayList<TaPanierDTO>();

		for (TaPanier taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaPanierDTO> selectAllDTO() {
		System.out.println("List of TaPanierDTO EJB :");
		ArrayList<TaPanierDTO> liste = new ArrayList<TaPanierDTO>();

		List<TaPanier> projects = selectAll();
		for(TaPanier project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaPanierDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaPanierDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaPanierDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaPanierDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaPanierDTO dto, String validationContext) throws EJBException {
		try {
			TaPanierMapper mapper = new TaPanierMapper();
			TaPanier entity = null;
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
	
	public void persistDTO(TaPanierDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaPanierDTO dto, String validationContext) throws CreateException {
		try {
			TaPanierMapper mapper = new TaPanierMapper();
			TaPanier entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaPanierDTO dto) throws RemoveException {
		try {
			TaPanierMapper mapper = new TaPanierMapper();
			TaPanier entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaPanier refresh(TaPanier persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaPanier value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaPanier value, String propertyName, String validationContext) {
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
	public void validateDTO(TaPanierDTO dto, String validationContext) {
		try {
			TaPanierMapper mapper = new TaPanierMapper();
			TaPanier entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaPanierDTO> validator = new BeanValidator<TaPanierDTO>(TaPanierDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaPanierDTO dto, String propertyName, String validationContext) {
		try {
			TaPanierMapper mapper = new TaPanierMapper();
			TaPanier entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaPanierDTO> validator = new BeanValidator<TaPanierDTO>(TaPanierDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaPanierDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaPanierDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaPanier value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaPanier value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
	}
	
	public List<TaPanier> findPanierDate(Date debut, Date fin) {
		return dao.findPanierDate(debut, fin);
	}

}
