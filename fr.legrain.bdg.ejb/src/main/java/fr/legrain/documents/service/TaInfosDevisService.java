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

import fr.legrain.bdg.documents.service.remote.ITaInfosDevisServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaInfosDevisMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.TaDevisDTO;
import fr.legrain.document.model.TaInfosDevis;
import fr.legrain.documents.dao.IInfosDevisDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaInfosDevisBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaInfosDevisService extends AbstractApplicationDAOServer<TaInfosDevis, TaDevisDTO> implements ITaInfosDevisServiceRemote {

	static Logger logger = Logger.getLogger(TaInfosDevisService.class);

	@Inject private IInfosDevisDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaInfosDevisService() {
		super(TaInfosDevis.class,TaDevisDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaInfosDevis a";
	
	public TaInfosDevis findByCodeDevis(String code) {
		return dao.findByCodeDevis(code);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaInfosDevis transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaInfosDevis transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaInfosDevis persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaInfosDevis merge(TaInfosDevis detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaInfosDevis merge(TaInfosDevis detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaInfosDevis findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaInfosDevis findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaInfosDevis> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaDevisDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaDevisDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaInfosDevis> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaDevisDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaDevisDTO entityToDTO(TaInfosDevis entity) {
//		TaDevisDTO dto = new TaDevisDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaInfosDevisMapper mapper = new TaInfosDevisMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaDevisDTO> listEntityToListDTO(List<TaInfosDevis> entity) {
		List<TaDevisDTO> l = new ArrayList<TaDevisDTO>();

		for (TaInfosDevis taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaDevisDTO> selectAllDTO() {
		System.out.println("List of TaDevisDTO EJB :");
		ArrayList<TaDevisDTO> liste = new ArrayList<TaDevisDTO>();

		List<TaInfosDevis> projects = selectAll();
		for(TaInfosDevis project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaDevisDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaDevisDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaDevisDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaDevisDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaDevisDTO dto, String validationContext) throws EJBException {
		try {
			TaInfosDevisMapper mapper = new TaInfosDevisMapper();
			TaInfosDevis entity = null;
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
	
	public void persistDTO(TaDevisDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaDevisDTO dto, String validationContext) throws CreateException {
		try {
			TaInfosDevisMapper mapper = new TaInfosDevisMapper();
			TaInfosDevis entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaDevisDTO dto) throws RemoveException {
		try {
			TaInfosDevisMapper mapper = new TaInfosDevisMapper();
			TaInfosDevis entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaInfosDevis refresh(TaInfosDevis persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaInfosDevis value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaInfosDevis value, String propertyName, String validationContext) {
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
	public void validateDTO(TaDevisDTO dto, String validationContext) {
		try {
			TaInfosDevisMapper mapper = new TaInfosDevisMapper();
			TaInfosDevis entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaDevisDTO> validator = new BeanValidator<TaDevisDTO>(TaDevisDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaDevisDTO dto, String propertyName, String validationContext) {
		try {
			TaInfosDevisMapper mapper = new TaInfosDevisMapper();
			TaInfosDevis entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaDevisDTO> validator = new BeanValidator<TaDevisDTO>(TaDevisDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaDevisDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaDevisDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaInfosDevis value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaInfosDevis value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
