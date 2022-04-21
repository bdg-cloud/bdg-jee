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

import fr.legrain.article.titretransport.dao.ITitreTransportCapsulesDAO;
import fr.legrain.article.titretransport.dto.TaTitreTransportDTO;
import fr.legrain.article.titretransport.model.TaTitreTransport;
import fr.legrain.bdg.article.titretransport.service.remote.ITaTitreTransportServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaTitreTransportMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaTitreTransportBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaTitreTransportService extends AbstractApplicationDAOServer<TaTitreTransport, TaTitreTransportDTO> implements ITaTitreTransportServiceRemote {

	static Logger logger = Logger.getLogger(TaTitreTransportService.class);

	@Inject private ITitreTransportCapsulesDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaTitreTransportService() {
		super(TaTitreTransport.class,TaTitreTransportDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTitreTransport a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTitreTransport transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTitreTransport transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaTitreTransport persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdTitreTransport()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaTitreTransport merge(TaTitreTransport detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTitreTransport merge(TaTitreTransport detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaTitreTransport findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTitreTransport findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTitreTransport> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTitreTransportDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTitreTransportDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTitreTransport> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTitreTransportDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTitreTransportDTO entityToDTO(TaTitreTransport entity) {
//		TaTitreTransportDTO dto = new TaTitreTransportDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaTitreTransportMapper mapper = new TaTitreTransportMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTitreTransportDTO> listEntityToListDTO(List<TaTitreTransport> entity) {
		List<TaTitreTransportDTO> l = new ArrayList<TaTitreTransportDTO>();

		for (TaTitreTransport taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTitreTransportDTO> selectAllDTO() {
		System.out.println("List of TaTitreTransportDTO EJB :");
		ArrayList<TaTitreTransportDTO> liste = new ArrayList<TaTitreTransportDTO>();

		List<TaTitreTransport> projects = selectAll();
		for(TaTitreTransport project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTitreTransportDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTitreTransportDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTitreTransportDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTitreTransportDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTitreTransportDTO dto, String validationContext) throws EJBException {
		try {
			TaTitreTransportMapper mapper = new TaTitreTransportMapper();
			TaTitreTransport entity = null;
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
	
	public void persistDTO(TaTitreTransportDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTitreTransportDTO dto, String validationContext) throws CreateException {
		try {
			TaTitreTransportMapper mapper = new TaTitreTransportMapper();
			TaTitreTransport entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTitreTransportDTO dto) throws RemoveException {
		try {
			TaTitreTransportMapper mapper = new TaTitreTransportMapper();
			TaTitreTransport entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTitreTransport refresh(TaTitreTransport persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTitreTransport value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTitreTransport value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTitreTransportDTO dto, String validationContext) {
		try {
			TaTitreTransportMapper mapper = new TaTitreTransportMapper();
			TaTitreTransport entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTitreTransportDTO> validator = new BeanValidator<TaTitreTransportDTO>(TaTitreTransportDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTitreTransportDTO dto, String propertyName, String validationContext) {
		try {
			TaTitreTransportMapper mapper = new TaTitreTransportMapper();
			TaTitreTransport entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTitreTransportDTO> validator = new BeanValidator<TaTitreTransportDTO>(TaTitreTransportDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTitreTransportDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTitreTransportDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTitreTransport value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTitreTransport value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

	@Override
	public List<TaTitreTransportDTO> findByCodeLight(String code) {
		// TODO Auto-generated method stub
		return dao.findByCodeLight(code);
	}



}
