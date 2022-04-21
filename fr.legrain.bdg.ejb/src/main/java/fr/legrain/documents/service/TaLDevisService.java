package fr.legrain.documents.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.bdg.documents.service.remote.ITaLDevisServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaLDevisMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.ILigneDocumentDTO;
import fr.legrain.document.dto.TaLDevisDTO;
import fr.legrain.document.model.TaLDevis;
import fr.legrain.documents.dao.ILDevisDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaLDevisBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaLDevisService extends AbstractApplicationDAOServer<TaLDevis, TaLDevisDTO> implements ITaLDevisServiceRemote {

	static Logger logger = Logger.getLogger(TaLDevisService.class);

	@Inject private ILDevisDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaLDevisService() {
		super(TaLDevis.class,TaLDevisDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaLDevis a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaLDevis transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaLDevis transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaLDevis persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaLDevis merge(TaLDevis detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaLDevis merge(TaLDevis detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaLDevis findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaLDevis findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaLDevis> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaLDevisDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaLDevisDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaLDevis> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaLDevisDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaLDevisDTO entityToDTO(TaLDevis entity) {
//		TaLDevisDTO dto = new TaLDevisDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaLDevisMapper mapper = new TaLDevisMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaLDevisDTO> listEntityToListDTO(List<TaLDevis> entity) {
		List<TaLDevisDTO> l = new ArrayList<TaLDevisDTO>();

		for (TaLDevis taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaLDevisDTO> selectAllDTO() {
		System.out.println("List of TaLDevisDTO EJB :");
		ArrayList<TaLDevisDTO> liste = new ArrayList<TaLDevisDTO>();

		List<TaLDevis> projects = selectAll();
		for(TaLDevis project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaLDevisDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaLDevisDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaLDevisDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaLDevisDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaLDevisDTO dto, String validationContext) throws EJBException {
		try {
			TaLDevisMapper mapper = new TaLDevisMapper();
			TaLDevis entity = null;
			if(dto.getIdLDocument()!=null) {
				entity = dao.findById(dto.getIdLDocument());
				if(dto.getVersionObj()!=entity.getVersionObj()) {
					throw new OptimisticLockException(entity,
							"L'objet à été modifié depuis le dernier accés. Client ID : "+dto.getIdLDocument()+" - Client Version objet : "+dto.getVersionObj()+" -Serveur Version Objet : "+entity.getVersionObj());
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
	
	public void persistDTO(TaLDevisDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaLDevisDTO dto, String validationContext) throws CreateException {
		try {
			TaLDevisMapper mapper = new TaLDevisMapper();
			TaLDevis entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaLDevisDTO dto) throws RemoveException {
		try {
			TaLDevisMapper mapper = new TaLDevisMapper();
			TaLDevis entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaLDevis refresh(TaLDevis persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaLDevis value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaLDevis value, String propertyName, String validationContext) {
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
	public void validateDTO(TaLDevisDTO dto, String validationContext) {
		try {
			TaLDevisMapper mapper = new TaLDevisMapper();
			TaLDevis entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLDevisDTO> validator = new BeanValidator<TaLDevisDTO>(TaLDevisDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaLDevisDTO dto, String propertyName, String validationContext) {
		try {
			TaLDevisMapper mapper = new TaLDevisMapper();
			TaLDevis entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLDevisDTO> validator = new BeanValidator<TaLDevisDTO>(TaLDevisDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaLDevisDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaLDevisDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaLDevis value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaLDevis value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
