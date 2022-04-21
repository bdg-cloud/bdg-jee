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

import fr.legrain.bdg.documents.service.remote.ITaInfosApporteurServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaInfosApporteurServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaInfosApporteurMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.IDocumentDTO;
import fr.legrain.document.dto.TaApporteurDTO;
import fr.legrain.document.dto.TaApporteurDTO;
import fr.legrain.document.model.TaInfosApporteur;
import fr.legrain.document.model.TaInfosApporteur;
import fr.legrain.documents.dao.IInfosApporteurDAO;
import fr.legrain.documents.dao.IInfosFactureDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaInfosApporteurBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaInfosApporteurService extends AbstractApplicationDAOServer<TaInfosApporteur, TaApporteurDTO> implements ITaInfosApporteurServiceRemote {

	static Logger logger = Logger.getLogger(TaInfosApporteurService.class);

	@Inject private IInfosApporteurDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaInfosApporteurService() {
		super(TaInfosApporteur.class,TaApporteurDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaInfosApporteur a";
	
	public TaInfosApporteur findByCodeApporteur(String code) {
		return dao.findByCodeApporteur(code);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaInfosApporteur transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaInfosApporteur transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaInfosApporteur persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaInfosApporteur merge(TaInfosApporteur detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaInfosApporteur merge(TaInfosApporteur detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaInfosApporteur findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaInfosApporteur findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaInfosApporteur> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaApporteurDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaApporteurDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaInfosApporteur> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaApporteurDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaApporteurDTO entityToDTO(TaInfosApporteur entity) {
//		TaApporteurDTO dto = new TaApporteurDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaInfosApporteurMapper mapper = new TaInfosApporteurMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaApporteurDTO> listEntityToListDTO(List<TaInfosApporteur> entity) {
		List<TaApporteurDTO> l = new ArrayList<TaApporteurDTO>();

		for (TaInfosApporteur taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaApporteurDTO> selectAllDTO() {
		System.out.println("List of TaApporteurDTO EJB :");
		ArrayList<TaApporteurDTO> liste = new ArrayList<TaApporteurDTO>();

		List<TaInfosApporteur> projects = selectAll();
		for(TaInfosApporteur project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaApporteurDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaApporteurDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaApporteurDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaApporteurDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaApporteurDTO dto, String validationContext) throws EJBException {
		try {
			TaInfosApporteurMapper mapper = new TaInfosApporteurMapper();
			TaInfosApporteur entity = null;
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
	
	public void persistDTO(TaApporteurDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaApporteurDTO dto, String validationContext) throws CreateException {
		try {
			TaInfosApporteurMapper mapper = new TaInfosApporteurMapper();
			TaInfosApporteur entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaApporteurDTO dto) throws RemoveException {
		try {
			TaInfosApporteurMapper mapper = new TaInfosApporteurMapper();
			TaInfosApporteur entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaInfosApporteur refresh(TaInfosApporteur persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaInfosApporteur value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaInfosApporteur value, String propertyName, String validationContext) {
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
	public void validateDTO(TaApporteurDTO dto, String validationContext) {
		try {
			TaInfosApporteurMapper mapper = new TaInfosApporteurMapper();
			TaInfosApporteur entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaApporteurDTO> validator = new BeanValidator<TaApporteurDTO>(TaApporteurDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaApporteurDTO dto, String propertyName, String validationContext) {
		try {
			TaInfosApporteurMapper mapper = new TaInfosApporteurMapper();
			TaInfosApporteur entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaApporteurDTO> validator = new BeanValidator<TaApporteurDTO>(TaApporteurDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaApporteurDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaApporteurDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaInfosApporteur value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaInfosApporteur value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
