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

import fr.legrain.bdg.documents.service.remote.ITaLAcompteServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLAcompteServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaLAcompteMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.ILigneDocumentDTO;
import fr.legrain.document.dto.TaLAcompteDTO;
import fr.legrain.document.model.TaLAcompte;
import fr.legrain.document.model.TaLAcompte;
import fr.legrain.documents.dao.ILAcompteDAO;
import fr.legrain.documents.dao.ILDevisDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaLAcompteBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaLAcompteService extends AbstractApplicationDAOServer<TaLAcompte, TaLAcompteDTO> implements ITaLAcompteServiceRemote {

	static Logger logger = Logger.getLogger(TaLAcompteService.class);

	@Inject private ILAcompteDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaLAcompteService() {
		super(TaLAcompte.class,TaLAcompteDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaLAcompte a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaLAcompte transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaLAcompte transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaLAcompte persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaLAcompte merge(TaLAcompte detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaLAcompte merge(TaLAcompte detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaLAcompte findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaLAcompte findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaLAcompte> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaLAcompteDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaLAcompteDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaLAcompte> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaLAcompteDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaLAcompteDTO entityToDTO(TaLAcompte entity) {
//		TaLAcompteDTO dto = new TaLAcompteDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaLAcompteMapper mapper = new TaLAcompteMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaLAcompteDTO> listEntityToListDTO(List<TaLAcompte> entity) {
		List<TaLAcompteDTO> l = new ArrayList<TaLAcompteDTO>();

		for (TaLAcompte taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaLAcompteDTO> selectAllDTO() {
		System.out.println("List of TaLAcompteDTO EJB :");
		ArrayList<TaLAcompteDTO> liste = new ArrayList<TaLAcompteDTO>();

		List<TaLAcompte> projects = selectAll();
		for(TaLAcompte project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaLAcompteDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaLAcompteDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaLAcompteDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaLAcompteDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaLAcompteDTO dto, String validationContext) throws EJBException {
		try {
			TaLAcompteMapper mapper = new TaLAcompteMapper();
			TaLAcompte entity = null;
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
	
	public void persistDTO(TaLAcompteDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaLAcompteDTO dto, String validationContext) throws CreateException {
		try {
			TaLAcompteMapper mapper = new TaLAcompteMapper();
			TaLAcompte entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaLAcompteDTO dto) throws RemoveException {
		try {
			TaLAcompteMapper mapper = new TaLAcompteMapper();
			TaLAcompte entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaLAcompte refresh(TaLAcompte persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaLAcompte value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaLAcompte value, String propertyName, String validationContext) {
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
	public void validateDTO(TaLAcompteDTO dto, String validationContext) {
		try {
			TaLAcompteMapper mapper = new TaLAcompteMapper();
			TaLAcompte entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLAcompteDTO> validator = new BeanValidator<TaLAcompteDTO>(TaLAcompteDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaLAcompteDTO dto, String propertyName, String validationContext) {
		try {
			TaLAcompteMapper mapper = new TaLAcompteMapper();
			TaLAcompte entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLAcompteDTO> validator = new BeanValidator<TaLAcompteDTO>(TaLAcompteDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaLAcompteDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaLAcompteDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaLAcompte value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaLAcompte value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
