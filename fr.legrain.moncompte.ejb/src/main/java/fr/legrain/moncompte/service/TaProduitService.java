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
import javax.jws.WebService;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.bdg.moncompte.service.remote.ITaProduitServiceRemote;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.moncompte.dao.IProduitDAO;
import fr.legrain.moncompte.data.AbstractApplicationDAOServer;
import fr.legrain.moncompte.dto.TaProduitDTO;
import fr.legrain.moncompte.model.TaProduit;
import fr.legrain.moncompte.model.TaSetup;
import fr.legrain.moncompte.model.mapping.mapper.TaProduitMapper;


/**
 * Session Bean implementation class TaProduitBean
 */
@Stateless
//@DeclareRoles("admin")
@WebService
//@Path("/product")
public class TaProduitService extends AbstractApplicationDAOServer<TaProduit, TaProduitDTO> implements ITaProduitServiceRemote {

	static Logger logger = Logger.getLogger(TaProduitService.class);

	@Inject private IProduitDAO dao;
	
//	@GET
//	@Path("{id}")
	public TaProduit getProductId(@PathParam("id") String id) {
		System.out.println("TaProduitService.getProductId() " +id +" ** "+dao.findById(LibConversion.stringToInteger(id)).getCode());
		
		//return Response.status(200).entity("getProductId is called, id : " + id).build();
		return dao.findById(LibConversion.stringToInteger(id));

	}
	
	/**
	 * Default constructor. 
	 */
	public TaProduitService() {
		super(TaProduit.class,TaProduitDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaProduit a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaProduit transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaProduit transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaProduit persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdProduit()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaProduit merge(TaProduit detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaProduit merge(TaProduit detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaProduit findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaProduit findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaProduit> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaProduitDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaProduitDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaProduit> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaProduitDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaProduitDTO entityToDTO(TaProduit entity) {
//		TaProduitDTO dto = new TaProduitDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaProduitMapper mapper = new TaProduitMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaProduitDTO> listEntityToListDTO(List<TaProduit> entity) {
		List<TaProduitDTO> l = new ArrayList<TaProduitDTO>();

		for (TaProduit taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaProduitDTO> selectAllDTO() {
		System.out.println("List of TaProduitDTO EJB :");
		ArrayList<TaProduitDTO> liste = new ArrayList<TaProduitDTO>();

		List<TaProduit> projects = selectAll();
		for(TaProduit project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaProduitDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaProduitDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaProduitDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaProduitDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaProduitDTO dto, String validationContext) throws EJBException {
		try {
			TaProduitMapper mapper = new TaProduitMapper();
			TaProduit entity = null;
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
	
	public void persistDTO(TaProduitDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaProduitDTO dto, String validationContext) throws CreateException {
		try {
			TaProduitMapper mapper = new TaProduitMapper();
			TaProduit entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaProduitDTO dto) throws RemoveException {
		try {
			TaProduitMapper mapper = new TaProduitMapper();
			TaProduit entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaProduit refresh(TaProduit persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaProduit value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaProduit value, String propertyName, String validationContext) {
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
	public void validateDTO(TaProduitDTO dto, String validationContext) {
		try {
			TaProduitMapper mapper = new TaProduitMapper();
			TaProduit entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaProduitDTO> validator = new BeanValidator<TaProduitDTO>(TaProduitDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaProduitDTO dto, String propertyName, String validationContext) {
		try {
			TaProduitMapper mapper = new TaProduitMapper();
			TaProduit entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaProduitDTO> validator = new BeanValidator<TaProduitDTO>(TaProduitDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaProduitDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaProduitDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaProduit value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaProduit value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
	}

}
