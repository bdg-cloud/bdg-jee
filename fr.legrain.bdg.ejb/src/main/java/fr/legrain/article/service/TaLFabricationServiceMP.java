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

import fr.legrain.article.dao.ILFabricationMPDAO;
import fr.legrain.article.model.TaLFabricationMP;
import fr.legrain.bdg.article.service.remote.ITaLFabricationMPServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaLFabricationMPMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.TaLFabricationDTO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaLFabricationBean
 */
@Stateless
//@Stateful
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaLFabricationServiceMP extends AbstractApplicationDAOServer<TaLFabricationMP, TaLFabricationDTO> implements ITaLFabricationMPServiceRemote {

	static Logger logger = Logger.getLogger(TaLFabricationServiceMP.class);

	@Inject private ILFabricationMPDAO dao;
	@EJB private ITaGenCodeExServiceRemote gencode;
	
	/**
	 * Default constructor. 
	 */
	public TaLFabricationServiceMP() {
		super(TaLFabricationMP.class,TaLFabricationDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaLFabricationMP a";
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaLFabricationMP transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaLFabricationMP transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaLFabricationMP persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdLDocument()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaLFabricationMP merge(TaLFabricationMP detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaLFabricationMP merge(TaLFabricationMP detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaLFabricationMP findById(int id) throws FinderException {
		return dao.findById(id);
	}
	
	public TaLFabricationMP findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaLFabricationMP> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



	@Override
	public List<TaLFabricationDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaLFabricationMP> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaLFabricationDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public List<TaLFabricationDTO> listEntityToListDTO(List<TaLFabricationMP> entity) {
		List<TaLFabricationDTO> l = new ArrayList<TaLFabricationDTO>();

		for (TaLFabricationMP taLotArticle : entity) {
			l.add(entityToDTO(taLotArticle));
		}

		return l;
	}
	
	public TaLFabricationDTO entityToDTO(TaLFabricationMP entity)  {
//		TaFabricationDTO dto = new TaFabricationDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;

		TaLFabricationMPMapper mapper = new TaLFabricationMPMapper();
		TaLFabricationDTO dto = new TaLFabricationDTO();
		dto = mapper.mapEntityToDto(entity, null);
		
		
		
		return dto;
	}

//	@RolesAllowed("admin")
	public List<TaLFabricationDTO> selectAllDTO() {
		System.out.println("List of TaLFabricationDTO EJB :");
		ArrayList<TaLFabricationDTO> liste = new ArrayList<TaLFabricationDTO>();

		List<TaLFabricationMP> projects = selectAll();
		for(TaLFabricationMP project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}
	
	public TaLFabricationDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaLFabricationDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaLFabricationDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaLFabricationDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@SuppressWarnings("deprecation")
	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaLFabricationDTO dto, String validationContext) throws EJBException {
		try {
			TaLFabricationMPMapper mapper = new TaLFabricationMPMapper();
			TaLFabricationMP entity = null;
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
	
	public void persistDTO(TaLFabricationDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaLFabricationDTO dto, String validationContext) throws CreateException {
		try {
			TaLFabricationMP entity =  dao.findById(dto.getIdLDocument());
			dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}
	

	@Override
	public void removeDTO(TaLFabricationDTO dto) throws RemoveException {
		try {
			TaLFabricationMPMapper mapper = new TaLFabricationMPMapper();
			TaLFabricationMP entity = mapper.mapDtoToEntity(dto,null);
			dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaLFabricationMP refresh(TaLFabricationMP persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaLFabricationMP value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaLFabricationMP value, String propertyName, String validationContext) {
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
	public void validateDTO(TaLFabricationDTO dto, String validationContext) {
		try {
			TaLFabricationMPMapper mapper = new TaLFabricationMPMapper();
			TaLFabricationMP entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLFabricationDTO> validator = new BeanValidator<TaLFabricationDTO>(TaLFabricationDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaLFabricationDTO dto, String propertyName, String validationContext) {
		try {
			TaLFabricationMPMapper mapper = new TaLFabricationMPMapper();
			TaLFabricationMP entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLFabricationDTO> validator = new BeanValidator<TaLFabricationDTO>(TaLFabricationDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaLFabricationDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaLFabricationDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaLFabricationMP value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaLFabricationMP value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

	
	public String genereCode() {
		return null;
	}

	@Override
	public List<TaLFabricationDTO> findWithNamedQueryDTO(String arg0)
			throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	
}
