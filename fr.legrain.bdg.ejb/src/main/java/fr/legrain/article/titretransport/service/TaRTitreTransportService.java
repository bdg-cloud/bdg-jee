package fr.legrain.article.titretransport.service;

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

import fr.legrain.article.dao.IRTaTitreTransportDAO;
import fr.legrain.article.dto.TaRTitreTransportDTO;
import fr.legrain.article.model.TaRTaTitreTransport;
import fr.legrain.bdg.article.service.remote.ITaRTitreTransportServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaRTitreTransportMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaRTaTitreTransportBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaRTitreTransportService extends AbstractApplicationDAOServer<TaRTaTitreTransport, TaRTitreTransportDTO> implements ITaRTitreTransportServiceRemote {

	static Logger logger = Logger.getLogger(TaRTitreTransportService.class);

	@Inject private IRTaTitreTransportDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaRTitreTransportService() {
		super(TaRTaTitreTransport.class,TaRTitreTransportDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaRTaTitreTransport a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaRTaTitreTransport transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaRTaTitreTransport transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaRTaTitreTransport persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdRTitreTransport()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaRTaTitreTransport merge(TaRTaTitreTransport detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaRTaTitreTransport merge(TaRTaTitreTransport detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaRTaTitreTransport findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaRTaTitreTransport findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

	public TaRTaTitreTransport findByIdArticle(int idArticle) throws FinderException {
		return dao.findByIdArticle(idArticle);
	}
	
//	@RolesAllowed("admin")
	public List<TaRTaTitreTransport> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaRTitreTransportDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaRTitreTransportDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaRTaTitreTransport> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaRTitreTransportDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaRTitreTransportDTO entityToDTO(TaRTaTitreTransport entity) {
//		TaRTitreTransportDTO dto = new TaRTitreTransportDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaRTitreTransportMapper mapper = new TaRTitreTransportMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaRTitreTransportDTO> listEntityToListDTO(List<TaRTaTitreTransport> entity) {
		List<TaRTitreTransportDTO> l = new ArrayList<TaRTitreTransportDTO>();

		for (TaRTaTitreTransport taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaRTitreTransportDTO> selectAllDTO() {
		System.out.println("List of TaRTitreTransportDTO EJB :");
		ArrayList<TaRTitreTransportDTO> liste = new ArrayList<TaRTitreTransportDTO>();

		List<TaRTaTitreTransport> projects = selectAll();
		for(TaRTaTitreTransport project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaRTitreTransportDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaRTitreTransportDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaRTitreTransportDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaRTitreTransportDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaRTitreTransportDTO dto, String validationContext) throws EJBException {
		try {
			TaRTitreTransportMapper mapper = new TaRTitreTransportMapper();
			TaRTaTitreTransport entity = null;
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
	
	public void persistDTO(TaRTitreTransportDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaRTitreTransportDTO dto, String validationContext) throws CreateException {
		try {
			TaRTitreTransportMapper mapper = new TaRTitreTransportMapper();
			TaRTaTitreTransport entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaRTitreTransportDTO dto) throws RemoveException {
		try {
			TaRTitreTransportMapper mapper = new TaRTitreTransportMapper();
			TaRTaTitreTransport entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaRTaTitreTransport refresh(TaRTaTitreTransport persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaRTaTitreTransport value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaRTaTitreTransport value, String propertyName, String validationContext) {
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
	public void validateDTO(TaRTitreTransportDTO dto, String validationContext) {
		try {
			TaRTitreTransportMapper mapper = new TaRTitreTransportMapper();
			TaRTaTitreTransport entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaRTitreTransportDTO> validator = new BeanValidator<TaRTitreTransportDTO>(TaRTitreTransportDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaRTitreTransportDTO dto, String propertyName, String validationContext) {
		try {
			TaRTitreTransportMapper mapper = new TaRTitreTransportMapper();
			TaRTaTitreTransport entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaRTitreTransportDTO> validator = new BeanValidator<TaRTitreTransportDTO>(TaRTitreTransportDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaRTitreTransportDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaRTitreTransportDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaRTaTitreTransport value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaRTaTitreTransport value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
