package fr.legrain.article.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.ejb.CreateException;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.article.dao.ILModeleFabricationMPDAO;
import fr.legrain.article.model.TaLModeleFabricationMP;
import fr.legrain.bdg.article.service.remote.ITaLModeleFabricationMPServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaLModeleFabricationMPMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.TaLModeleFabricationDTO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaLModeleFabricationBean
 */
@Stateless
//@Stateful
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaLModeleFabricationMPService extends AbstractApplicationDAOServer<TaLModeleFabricationMP, TaLModeleFabricationDTO> implements ITaLModeleFabricationMPServiceRemote {

	static Logger logger = Logger.getLogger(TaLModeleFabricationMPService.class);

	@Inject private ILModeleFabricationMPDAO dao;
	@EJB private ITaGenCodeExServiceRemote gencode;
	/**
	 * Default constructor. 
	 */
	public TaLModeleFabricationMPService() {
		super(TaLModeleFabricationMP.class,TaLModeleFabricationDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaLModeleFabricationMP a";
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaLModeleFabricationMP transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaLModeleFabricationMP transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaLModeleFabricationMP persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdLDocument()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaLModeleFabricationMP merge(TaLModeleFabricationMP detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaLModeleFabricationMP merge(TaLModeleFabricationMP detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaLModeleFabricationMP findById(int id) throws FinderException {
		return dao.findById(id);
	}
	
	public TaLModeleFabricationMP findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaLModeleFabricationMP> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



	@Override
	public List<TaLModeleFabricationDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaLModeleFabricationMP> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaLModeleFabricationDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public List<TaLModeleFabricationDTO> listEntityToListDTO(List<TaLModeleFabricationMP> entity) {
		List<TaLModeleFabricationDTO> l = new ArrayList<TaLModeleFabricationDTO>();

		for (TaLModeleFabricationMP taLotArticle : entity) {
			l.add(entityToDTO(taLotArticle));
		}

		return l;
	}
	
	public TaLModeleFabricationDTO entityToDTO(TaLModeleFabricationMP entity)  {
//		TaFabricationDTO dto = new TaFabricationDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;

		TaLModeleFabricationMPMapper mapper = new TaLModeleFabricationMPMapper();
		TaLModeleFabricationDTO dto = new TaLModeleFabricationDTO();
		dto = mapper.mapEntityToDto(entity, null);
		
		
		
		return dto;
	}

//	@RolesAllowed("admin")
	public List<TaLModeleFabricationDTO> selectAllDTO() {
		System.out.println("List of TaLModeleFabricationDTO EJB :");
		ArrayList<TaLModeleFabricationDTO> liste = new ArrayList<TaLModeleFabricationDTO>();

		List<TaLModeleFabricationMP> projects = selectAll();
		for(TaLModeleFabricationMP project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}
	
	public TaLModeleFabricationDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaLModeleFabricationDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaLModeleFabricationDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaLModeleFabricationDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@SuppressWarnings("deprecation")
	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaLModeleFabricationDTO dto, String validationContext) throws EJBException {
		try {
			TaLModeleFabricationMPMapper mapper = new TaLModeleFabricationMPMapper();
			TaLModeleFabricationMP entity = null;
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
	
	public void persistDTO(TaLModeleFabricationDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaLModeleFabricationDTO dto, String validationContext) throws CreateException {
		try {
			TaLModeleFabricationMP entity =  dao.findById(dto.getIdLDocument());
			dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}
	

	@Override
	public void removeDTO(TaLModeleFabricationDTO dto) throws RemoveException {
		try {
			TaLModeleFabricationMPMapper mapper = new TaLModeleFabricationMPMapper();
			TaLModeleFabricationMP entity = mapper.mapDtoToEntity(dto,null);
			dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaLModeleFabricationMP refresh(TaLModeleFabricationMP persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaLModeleFabricationMP value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaLModeleFabricationMP value, String propertyName, String validationContext) {
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
	public void validateDTO(TaLModeleFabricationDTO dto, String validationContext) {
		try {
			TaLModeleFabricationMPMapper mapper = new TaLModeleFabricationMPMapper();
			TaLModeleFabricationMP entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLModeleFabricationDTO> validator = new BeanValidator<TaLModeleFabricationDTO>(TaLModeleFabricationDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaLModeleFabricationDTO dto, String propertyName, String validationContext) {
		try {
			TaLModeleFabricationMPMapper mapper = new TaLModeleFabricationMPMapper();
			TaLModeleFabricationMP entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLModeleFabricationDTO> validator = new BeanValidator<TaLModeleFabricationDTO>(TaLModeleFabricationDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaLModeleFabricationDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaLModeleFabricationDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaLModeleFabricationMP value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaLModeleFabricationMP value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

	
	public String genereCode() {
		return null;
	}

	@Override
	public List<TaLModeleFabricationDTO> findWithNamedQueryDTO(String arg0)
			throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	
}
